// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds.BoundedIntegerKind
import viper.gobra.util.Violation.violation

trait Assignability extends BaseProperty { this: TypeInfoImpl =>
  lazy val declarableTo: Property[(Vector[Type], Option[Type], Vector[Type], Boolean)] =
    createProperty[(Vector[Type], Option[Type], Vector[Type], Boolean)] {
      case (right, None, left, mayInit) => multiAssignableTo.result(right, left, mayInit)
      case (right, Some(t), _, mayInit) =>
        propForall(right, assignableTo.before((l: Type) => (l, t, mayInit)))
    }

  def isImportedContextualType(t: Type): Boolean = t match {
    case t: PointerT => isImportedContextualType(t.elem)
    case t: ContextualType => t.context != this
    case _ => false
  }

  def isLocallyDefinedContextualType(t: Type): Boolean = t match {
    case t: PointerT => isLocallyDefinedContextualType(t.elem)
    case t: ContextualType => t.context == this
    case _ => false
  }

  // To guarantee that no dynamically dispatched methods that may depend on invariants of the package
  // under initialization are called, we disallow assigning a value of a type defined in the current
  // package to a variable of an interface type defined in an exported package. This allows us to call,
  // without restriction, any interface methods with receiver types defined in imported packages during
  // initialization of a package.
  private lazy val assignableToIfInit: Property[(Type, Type)] = createProperty[(Type, Type)] {
    case (targetType, srcType) =>
      if (isLocallyDefinedContextualType(targetType) && isImportedContextualType(srcType)) {
        failedProp("Assigning values of types defined in the current package to locations " +
          "of an interface type that is defined in imported packages is disallowed in code that may run during package initialization.")
      } else {
        successProp
      }
  }

  lazy val multiAssignableTo: Property[(Vector[Type], Vector[Type], Boolean)] = createProperty[(Vector[Type], Vector[Type], Boolean)] {
    case (right, left, mayInit) =>
      StrictAssignMode(left.size, right.size) match {
        case AssignMode.Single =>
          right match {
            // To support Go's function chaining when a tuple with the results of a function call are passed to the
            // only variadic argument of another function
            case Vector(InternalTupleT(t)) if left.lastOption.exists(_.isInstanceOf[VariadicT]) =>
              multiAssignableTo.result(t, left, mayInit)
            case _ =>
              propForall(
                right.zip(left),
                createProperty[(Type, Type)]{ case (l: Type, r: Type) => assignableTo.result(l, r, mayInit) }
              )
          }
        case AssignMode.Multi => right.head match {
          case Assign(InternalTupleT(ts)) => multiAssignableTo.result(ts, left, mayInit)
          case t =>
            if (left.length == right.length + 1 && left.last.isInstanceOf[VariadicT]) {
              // this handles the case when all parameters but the last are passed in a function call and the last parameter
              // is variadic
              multiAssignableTo.result(right, left.init, mayInit)
            } else {
              failedProp(s"got $t but expected tuple type of size ${left.size}")
            }
        }
        case AssignMode.Variadic => variadicAssignableTo.result(right, left, mayInit)

        case AssignMode.Error => failedProp(s"cannot assign ${right.size} to ${left.size} elements")
      }
  }

  lazy val variadicAssignableTo: Property[(Vector[Type], Vector[Type], Boolean)] = createProperty[(Vector[Type], Vector[Type], Boolean)] {
    case (right, left, mayInit) =>
      StrictAssignMode(left.size, right.size) match {
        case AssignMode.Variadic => left.lastOption match {
          case Some(VariadicT(elem)) =>
            val dummyFill = UnknownType
            // left.init corresponds to the parameter list on the left except for the variadic type
            propForall(
              right.zipAll(left.init, dummyFill, elem),
              createProperty[(Type, Type)]{ case (l: Type, r: Type) => assignableTo.result(l, r, mayInit) }
            )
          case _ => failedProp(s"expected the last element of $left to be a variadic type")
        }
        case _ => failedProp(s"cannot assign $right to $left")
      }
  }

  // The notion of assignability depends on the context where the assignments are performed.
  // In particular, in code that may execute during package initialization, some assignments
  // to interface types are disallowed to guarantee that interface methods that assume the invariant
  // of the package under initialization are never called.
  lazy val assignableTo: Property[(Type, Type, Boolean)] = createFlatPropertyWithReason[(Type, Type, Boolean)] {
    case (right, left, _) => s"$right is not assignable to $left"
  } {
    case (Single(lst), Single(rst), mayInit) => (lst, rst) match {
      case _ if mayInit && !assignableToIfInit(lst, rst) => assignableToIfInit.result(lst, rst)
      // for go's types according to go's specification (mostly)
      case (UNTYPED_INT_CONST, r) if underlyingType(r).isInstanceOf[IntT] => successProp
      // not part of Go spec, but necessary for the definition of comparability
      case (l, UNTYPED_INT_CONST) if underlyingType(l).isInstanceOf[IntT] => successProp
      case (UNTYPED_INT_CONST, r) if underlyingType(r).isInstanceOf[FloatT] => successProp
      case (UnboundedFloatT, r) if underlyingType(r).isInstanceOf[FloatT] => successProp
      case (l, r) if identicalTypes(l, r) => successProp
      // the go language spec states that a value x of type V is assignable to a variable of type T
      // if V and T have identical underlying types and at least one of V or T is not a defined type
      case (l, r) if !(isDefinedType(l) && isDefinedType(r))
        && identicalTypes(underlyingType(l), underlyingType(r)) => successProp

      case (l, r) if underlyingType(r).isInstanceOf[InterfaceT] => implements(l, r)
      case (ChannelT(le, ChannelModus.Bi), ChannelT(re, _)) if identicalTypes(le, re) => successProp
      case (NilType, r) if isPointerType(r) => successProp
      case (VariadicT(t1), VariadicT(t2)) => assignableTo.result(t1, t2, mayInit)
      case (t1, VariadicT(t2)) => assignableTo.result(t1, t2, mayInit)
      case (VariadicT(t1), SliceT(t2)) if identicalTypes(t1, t2) => successProp

        // for ghost types
      case (BooleanT, AssertionT) => successProp
      case (SequenceT(l), SequenceT(r)) => assignableTo.result(l, r, mayInit) // implies that Sequences are covariant
      case (SetT(l), SetT(r)) => assignableTo.result(l, r, mayInit)
      case (MultisetT(l), MultisetT(r)) => assignableTo.result(l, r, mayInit)
      case (OptionT(l), OptionT(r)) => assignableTo.result(l, r, mayInit)
      case (IntT(_), PermissionT) => successProp

        // conservative choice
      case _ => errorProp()
    }
    case _ => errorProp()
  }

  lazy val assignable: Property[PExpression] = createBinaryProperty("assignable") {
    case e if !isMutable(e) => false
    case PIndexedExp(b, _) => underlyingType(exprType(b)) match {
      case _: ArrayT => assignable(b)
      case _: SliceT | _: GhostSliceT => assignable(b)
      case PointerT(_: ArrayT) => assignable(b)
      case _: VariadicT => assignable(b)
      case _: MapT => assignable(b)
      case _: SequenceT => true
      case _: MathMapT => true
      case _ => false
    }
    case PBlankIdentifier() => true
    case e => goAddressable(e)
  }

  lazy val compatibleWithAssOp: Property[(Type, PAssOp)] = createFlatProperty[(Type, PAssOp)] {
    case (t, op) => s"type error: got $t, but expected type compatible with $op"
  } {
    case (Single(t), PAddOp() | PSubOp() | PMulOp() | PDivOp() |
                     PModOp() | PBitAndOp() | PBitOrOp() | PBitXorOp() |
                     PBitClearOp() | PShiftLeftOp() | PShiftRightOp()) if underlyingType(t).isInstanceOf[IntT] => true
    case (Single(t), PAddOp()) if underlyingType(t) == StringT => true
    case _ => false
  }

  lazy val compositeKeyAssignableTo: Property[(PCompositeKey, Type, Boolean)] = createProperty[(PCompositeKey, Type, Boolean)] {
    case (PIdentifierKey(id), t, mayInit) => assignableTo.result(idType(id), t, mayInit)
    case (k: PCompositeVal, t, mayInit) => compositeValAssignableTo.result(k, t, mayInit)
  }

  lazy val compositeValAssignableTo: Property[(PCompositeVal, Type, Boolean)] = createProperty[(PCompositeVal, Type, Boolean)] {
    case (PExpCompositeVal(exp), t, mayInit) => assignableTo.result(exprType(exp), t, mayInit)
    case (PLitCompositeVal(lit), t, mayInit) => literalAssignableTo.result(lit, t, mayInit)
  }

  lazy val literalAssignableTo: Property[(PLiteralValue, Type, Boolean)] = createProperty[(PLiteralValue, Type, Boolean)] {
    case (PLiteralValue(elems), Single(typ), mayInit) =>
      underlyingType(typ) match {
        case s: StructT =>
          if (elems.isEmpty) {
            successProp
          } else if (elems.exists(_.key.nonEmpty)) {
            val tmap = s.embedded ++ s.fields

            failedProp("for struct literals either all or none elements must be keyed"
              , !elems.forall(_.key.nonEmpty)) and
              propForall(elems, createProperty[PKeyedElement] { e =>
                e.key.map {
                  case PIdentifierKey(id) if tmap.contains(id.name) =>
                    compositeValAssignableTo.result(e.exp, tmap(id.name), mayInit)

                  case v => failedProp(s"got $v but expected field name")
                }.getOrElse(successProp)
              })
          } else if (elems.size == s.embedded.size + s.fields.size) {
            propForall(
              elems.map(_.exp).zip(s.fieldsAndEmbedded.values),
              createProperty[(PCompositeVal, Type)]{ case (l: PCompositeVal, r: Type) => compositeValAssignableTo.result(l, r, mayInit) }
            )
          } else {
            failedProp("number of arguments does not match structure")
          }

        case a: AdtClauseT => // analogous to struct
          if (elems.isEmpty) {
            successProp
          } else if (elems.exists(_.key.nonEmpty)) {
            val tmap: Map[String, Type] = a.typeMap

            failedProp("for adt literals either all or none elements must be keyed",
              !elems.forall(_.key.nonEmpty)) and
              propForall(elems, createProperty[PKeyedElement] { e =>
                e.key.map {
                  case PIdentifierKey(id) if tmap.contains(id.name) =>
                    compositeValAssignableTo.result(e.exp, tmap(id.name), mayInit)

                  case v => failedProp(s"got $v but expected field name")
                }.getOrElse(successProp)
              })
          } else if (elems.size == a.fields.size) {
            propForall(
              elems.map(_.exp).zip(a.fields.map(_._2)),
              createProperty[(PCompositeVal, Type)]{ case (l: PCompositeVal, r: Type) => compositeValAssignableTo.result(l, r, mayInit) }
            )
          } else {
            failedProp("number of arguments does not match adt constructor")
          }

        case ArrayT(len, t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllKeysWithinBounds(elems, len) and
            areAllElementsAssignable(elems, t, mayInit)

        case SliceT(t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllElementsAssignable(elems, t, mayInit)

        case GhostSliceT(t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllElementsAssignable(elems, t, mayInit)

        case MapT(key, t) =>
          areAllElementsKeyed(elems) and
            areAllKeysAssignable(elems, key, mayInit) and
            areAllElementsAssignable(elems, t, mayInit) and
            areAllConstantKeysDifferent(elems, key)

        case SequenceT(t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllElementsAssignable(elems, t, mayInit)

        case SetT(t) =>
          areNoElementsKeyed(elems) and
            areAllElementsAssignable(elems, t, mayInit)

        case MultisetT(t) =>
          areNoElementsKeyed(elems) and
            areAllElementsAssignable(elems, t, mayInit)

        case MathMapT(keys, values) =>
          areAllElementsKeyed(elems) and
            areAllKeysAssignable(elems, keys, mayInit) and
            areAllElementsAssignable(elems, values, mayInit) and
            areAllConstantKeysDifferent(elems, keys)

        case t => failedProp(s"cannot assign literal to $t")
      }
    case (l, t, _) => failedProp(s"cannot assign literal $l to $t")
  }

  def assignableWithinBounds: Property[(Type, PExpression)] = createFlatProperty[(Type, PExpression)] {
    case (typ, expr) => s"constant expression $expr overflows $typ"
  } {
    case (typ, expr) =>
      val constVal = intConstantEval(expr)
      constVal.isEmpty || intValInBounds(constVal.get, typ)
  }

  private def intValInBounds(value: BigInt, typ: Type): Boolean =
    underlyingType(typ) match {
      case IntT(t) => t match {
        case typ: BoundedIntegerKind => typ.lower <= value && value <= typ.upper
        case _ => true
      }

      case _ => violation(s"Expected an integer type but instead received $typ.")
    }

  private def areAllKeysConstant(elems : Vector[PKeyedElement]) : PropertyResult = {
    val condition = elems.flatMap(_.key).exists {
      case PExpCompositeVal(exp) => intConstantEval(exp).isEmpty
      case PIdentifierKey(id) => intConstantEval(PNamedOperand(id)).isEmpty
      case _ => true
    }
    failedProp("expected integers as keys in the literal", condition)
  }

  private def areNoElementsKeyed(elems : Vector[PKeyedElement]) : PropertyResult =
    failedProp("no elements in the literal must be keyed", elems.exists(_.key.isDefined))

  private def areAllElementsKeyed(elems : Vector[PKeyedElement]) : PropertyResult =
    failedProp("all elements in the literal must be keyed", elems.exists(_.key.isEmpty))

  private def areAllKeysDisjoint(elems : Vector[PKeyedElement]) : PropertyResult = {
    val indices = keyElementIndices(elems)
    failedProp("found overlapping keys", indices.distinct.size != indices.size)
  }

  private def areAllKeysNonNegative(elems : Vector[PKeyedElement]) : PropertyResult =
    failedProp("found negative keys", keyElementIndices(elems).exists(_ < 0))

  private def areAllKeysWithinBounds(elems : Vector[PKeyedElement], length : BigInt) : PropertyResult =
    failedProp("found out-of-bound keys", keyElementIndices(elems).exists(length <= _))

  private def areAllKeysAssignable(elems : Vector[PKeyedElement], typ : Type, mayInit: Boolean) =
    propForall(elems.flatMap(_.key), compositeKeyAssignableTo.before((c: PCompositeKey) => (c, typ, mayInit)))

  private def areAllElementsAssignable(elems : Vector[PKeyedElement], typ : Type, mayInit: Boolean) =
    propForall(elems.map(_.exp), compositeValAssignableTo.before((c: PCompositeVal) => (c, typ, mayInit)))

  private def areAllConstantKeysDifferent(elems: Vector[PKeyedElement], typ: Type) = {
    def constVal[T](eval: PExpression => Option[T])(keyed: PKeyedElement) : Option[T] = keyed.key match {
      case Some(PExpCompositeVal(exp)) => eval(exp)
      case _ => None
    }
    val eval = underlyingType(typ) match {
      case _: IntT => intConstantEval
      case BooleanT => boolConstantEval
      case StringT => stringConstantEval
      case _ => _: PExpression => None
    }
    val constKeys = elems map constVal(eval) filter (_.isDefined) map (_.get)
    failedProp("key appears twice in map literal", constKeys.distinct.size != constKeys.size)
  }


  def keyElementIndices(elems : Vector[PKeyedElement]) : Vector[BigInt] = {
    elems.map(_.key).zipWithIndex.map {
      case (Some(PExpCompositeVal(exp)), i) => intConstantEval(exp).getOrElse(BigInt(i))
      case (Some(PIdentifierKey(id)), i) => intConstantEval(PNamedOperand(id)).getOrElse(BigInt(i))
      case (_, i) => BigInt(i)
    }
  }

  private def isMutable: Property[PExpression] = createBinaryProperty("mutable") { e =>
    resolve(e) match {
      case Some(g: ap.GlobalVariable) => g.symb.addressable
      case Some(i: ap.IndexedExp) => isMutable(i.base)
      case Some(f: ap.FieldSelection) => isMutable(f.base)
      case _ => true
    }
  }
}
