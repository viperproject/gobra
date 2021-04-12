// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds.BoundedIntegerKind
import viper.gobra.util.Violation.violation

trait Assignability extends BaseProperty { this: TypeInfoImpl =>

  lazy val declarableTo: Property[(Vector[Type], Option[Type], Vector[Type])] =
    createProperty[(Vector[Type], Option[Type], Vector[Type])] {
      case (right, None, left) => multiAssignableTo.result(right, left)
      case (right, Some(t), _) => propForall(right, assignableTo.before((l: Type) => (l, t)))
    }

  lazy val multiAssignableTo: Property[(Vector[Type], Vector[Type])] = createProperty[(Vector[Type], Vector[Type])] {
    case (right, left) =>
      StrictAssignModi(left.size, right.size) match {
        case AssignMode.Single =>
          right match {
            // To support Go's function chaining when a tuple with the results of a function call are passed to the
            // only variadic argument of another function
            case Vector(InternalTupleT(t)) if left.lastOption.exists(_.isInstanceOf[VariadicT]) =>
              multiAssignableTo.result(t, left)
            case _ => propForall(right.zip(left), assignableTo)
          }
        case AssignMode.Multi => right.head match {
          case Assign(InternalTupleT(ts)) => multiAssignableTo.result(ts, left)
          case t =>
            if (left.length == right.length + 1 && left.last.isInstanceOf[VariadicT]) {
              // this handles the case when all parameters but the last are passed in a function call and the last parameter
              // is variadic
              multiAssignableTo.result(right, left.init)
            } else {
              failedProp(s"got $t but expected tuple type of size ${left.size}")
            }
        }
        case AssignMode.Variadic => variadicAssignableTo.result(right, left)

        case AssignMode.Error => failedProp(s"cannot assign ${right.size} to ${left.size} elements")
      }
  }

  lazy val variadicAssignableTo: Property[(Vector[Type], Vector[Type])] = createProperty[(Vector[Type], Vector[Type])] {
    case (right, left) =>
      StrictAssignModi(left.size, right.size) match {
        case AssignMode.Variadic => left.lastOption match {
          case Some(VariadicT(elem)) =>
            val dummyFill = UnknownType
            // left.init corresponds to the parameter list on the left except for the variadic type
            propForall(right.zipAll(left.init, dummyFill, elem), assignableTo)
          case _ => failedProp(s"expected the last element of $left to be a variadic type")
        }
        case _ => failedProp(s"cannot assign $right to $left")
      }
  }

  lazy val parameterAssignableTo: Property[(Type, Type)] = createProperty[(Type, Type)] {
    case (Argument(InternalTupleT(rs)), Argument(InternalTupleT(ls))) if rs.size == ls.size =>
      propForall(rs zip ls, assignableTo)

    case (r, l) => assignableTo.result(r, l)
  }

  lazy val assignableTo: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (right, left) => s"$right is not assignable to $left"
  } {
    case (Single(lst), Single(rst)) => (lst, rst) match {
      // for go's types according to go's specification (mostly)
      case (UNTYPED_INT_CONST, r) if underlyingType(r).isInstanceOf[IntT] => true
      // not part of Go spec, but necessary for the definition of comparability
      case (l, UNTYPED_INT_CONST) if underlyingType(l).isInstanceOf[IntT] => true
      case (l, r) if identicalTypes(l, r) => true
      // the go language spec states that a value x of type V is assignable to a variable of type T
      // if V and T have identical underlying types and at least one of V or T is not a defined type
      case (l, r) if !(isDefinedType(l) && isDefinedType(r))
        && identicalTypes(underlyingType(l), underlyingType(r)) => true

      case (l, r) if implements(l, r) => true
      case (ChannelT(le, ChannelModus.Bi), ChannelT(re, _)) if identicalTypes(le, re) => true
      case (NilType, r) if isPointerType(r) => true
      case (VariadicT(t1), VariadicT(t2)) => assignableTo(t1, t2)
      case (t1, VariadicT(t2)) => assignableTo(t1, t2)

        // for ghost types
      case (BooleanT, AssertionT) => true
      case (SortT, SortT) => true
      case (PermissionT, PermissionT) => true
      case (SequenceT(l), SequenceT(r)) => assignableTo(l,r) // implies that Sequences are covariant
      case (SetT(l), SetT(r)) => assignableTo(l,r)
      case (MultisetT(l), MultisetT(r)) => assignableTo(l,r)
      case (MathematicalMapT(k1, v1), MathematicalMapT(k2, v2)) => assignableTo(k1, k2) && assignableTo(v1, v2) // TODO: rethink this with examples
      case (OptionT(l), OptionT(r)) => assignableTo(l, r)
      case (IntT(_), PermissionT) => true

        // conservative choice
      case _ => false
    }
    case _ => false
  }

  lazy val assignable: Property[PExpression] = createBinaryProperty("assignable") {
    case PIndexedExp(b, _) => exprType(b) match {
      case _: ArrayT => assignable(b)
      case _: SliceT => assignable(b)
      case _: VariadicT => assignable(b)
      case _: MapT => true
      case _: SequenceT => true
      case _: MathematicalMapT => true
      case _ => false
    }
    case PBlankIdentifier() => true
    case e => goAddressable(e)
  }

  lazy val compatibleWithAssOp: Property[(Type, PAssOp)] = createFlatProperty[(Type, PAssOp)] {
    case (t, op) => s"type error: got $t, but expected type compatible with $op"
  } {
    case (Single(IntT(_)), PAddOp() | PSubOp() | PMulOp() | PDivOp() | PModOp()) => true
    case (Single(StringT), PAddOp()) => true
    case _ => false
  }

  lazy val compositeKeyAssignableTo: Property[(PCompositeKey, Type)] = createProperty[(PCompositeKey, Type)] {
    case (PIdentifierKey(id), t) => assignableTo.result(idType(id), t)
    case (k: PCompositeVal, t) => compositeValAssignableTo.result(k, t)
  }

  lazy val compositeValAssignableTo: Property[(PCompositeVal, Type)] = createProperty[(PCompositeVal, Type)] {
    case (PExpCompositeVal(exp), t) => assignableTo.result(exprType(exp), t)
    case (PLitCompositeVal(lit), t) => literalAssignableTo.result(lit, t)
  }

  lazy val literalAssignableTo: Property[(PLiteralValue, Type)] = createProperty[(PLiteralValue, Type)] {
    case (PLiteralValue(elems), Single(typ)) =>
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
                    compositeValAssignableTo.result(e.exp, tmap(id.name))

                  case v => failedProp(s"got $v but expected field name")
                }.getOrElse(successProp)
              })
          } else if (elems.size == s.embedded.size + s.fields.size) {
            propForall(
              elems.map(_.exp).zip((s.embedded ++ s.fields).values),/*
              elems.map(_.exp).zip(decl.clauses.flatMap { cl =>
                def clauseInducedTypes(clause: PActualStructClause): Vector[Type] = clause match {
                  case PEmbeddedDecl(embeddedType, _) => Vector(context.typ(embeddedType))
                  case PFieldDecls(fields) => fields map (f => context.typ(f.typ))
                }

                cl match {
                  case PExplicitGhostStructClause(c) => clauseInducedTypes(c)
                  case c: PActualStructClause => clauseInducedTypes(c)
                }
              }),*/
              compositeValAssignableTo
            )
          } else {
            failedProp("number of arguments does not match structure")
          }

        case ArrayT(len, t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllKeysWithinBounds(elems, len) and
            areAllElementsAssignable(elems, t)

        case SliceT(t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllElementsAssignable(elems, t)

        case MapT(key, t) =>
          areAllElementsKeyed(elems) and
            areAllKeysAssignable(elems, key) and
            areAllElementsAssignable(elems, t)

        case SequenceT(t) =>
          areAllKeysConstant(elems) and
            areAllKeysDisjoint(elems) and
            areAllKeysNonNegative(elems) and
            areAllElementsAssignable(elems, t)

        case SetT(t) =>
          areNoElementsKeyed(elems) and
            areAllElementsAssignable(elems, t)

        case MultisetT(t) =>
          areNoElementsKeyed(elems) and
            areAllElementsAssignable(elems, t)

        case MathematicalMapT(keys, values) =>
          areAllElementsKeyed(elems) and
            areAllKeysAssignable(elems, keys) and
            areAllElementsAssignable(elems, values)


        case t => failedProp(s"cannot assign literal to $t")
      }
    case (l, t) => failedProp(s"cannot assign literal $l to $t")
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

  private def areAllKeysAssignable(elems : Vector[PKeyedElement], typ : Type) =
    propForall(elems.flatMap(_.key), compositeKeyAssignableTo.before((c: PCompositeKey) => (c, typ)))

  private def areAllElementsAssignable(elems : Vector[PKeyedElement], typ : Type) =
    propForall(elems.map(_.exp), compositeValAssignableTo.before((c: PCompositeVal) => (c, typ)))


  def keyElementIndices(elems : Vector[PKeyedElement]) : Vector[BigInt] = {
    elems.map(_.key).zipWithIndex.map {
      case (Some(PExpCompositeVal(exp)), i) => intConstantEval(exp).getOrElse(BigInt(i))
      case (Some(PIdentifierKey(id)), i) => intConstantEval(PNamedOperand(id)).getOrElse(BigInt(i))
      case (_, i) => BigInt(i)
    }
  }

  private def isDefinedType(t: Type): Boolean = {
    // All of the following are defined types (https://golang.org/ref/spec#Predeclared_identifiers):
    //   bool byte complex64 complex128 error float32 float64
    //   int int8 int16 int32 int64 rune string
    //   uint uint8 uint16 uint32 uint64 uintptr
    t match {
      // should be extended as new types are added to the language
      case IntT(_) | BooleanT | DeclaredT(_, _) | StringT => true
      case _ => false
    }
  }
}
