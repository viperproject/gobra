// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Message
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, check, error, noMessages}
import viper.gobra.ast.frontend.{AstPattern => ap, _}
import viper.gobra.frontend.info.base.SymbolTable.{AdtDestructor, AdtDiscriminator, GlobalVariable, SingleConstant}
import viper.gobra.frontend.info.base.Type.{StringT, _}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds.{BoundedIntegerKind, UnboundedInteger}
import viper.gobra.util.{Constants, TypeBounds, Violation}

import scala.annotation.nowarn

trait ExprTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  val INT_TYPE: Type = IntT(config.typeBounds.Int)
  val UNTYPED_INT_CONST: Type = IntT(config.typeBounds.UntypedConst)
  // default type of unbounded integer constant expressions when they must have a type
  val DEFAULT_INTEGER_TYPE: Type = INT_TYPE
  // Maximum value allowed by the Go compiler for the right operand of `<<` when both operands
  // are constant (obtained empirically)
  val MAX_SHIFT: Int = 512

  lazy val wellDefExprAndType: WellDefinedness[PExpressionAndType] = createWellDef {
    case n: PNamedOperand =>
      resolve(n) match {
        /* A closure name can only be used as a spec, if we are not directly within the closure itself
           (the same limitation applies within closures nested inside the closure itself) */
        case Some(ap.Closure(id, _)) => error(n, s"expected valid operand, got closure declaration name $n",
          !tree.parent(n).head.isInstanceOf[PClosureSpecInstance] &&
            tryEnclosingFunctionLit(n).fold(true)(lit => lit.id.fold(true)(encId => encId.name != id.name)))
        case _ => noMessages
      } // no more checks to avoid cycles

    case n: PDeref =>
      resolve(n) match {
        case Some(p: ap.Deref) =>
          exprType(p.base) match {
            case Single(PointerT(_)) => noMessages
            case t => error(n, s"expected pointer type but got $t")
          }

        case Some(_: ap.PointerType) => noMessages

        case _ => violation("Deref should always resolve to either the deref or pointer type pattern")
      }

    case n: PDot =>
      resolve(n) match {
        case Some(_: ap.FieldSelection) => noMessages
        case Some(_: ap.ReceivedMethod) => noMessages
        case Some(_: ap.ReceivedPredicate) => noMessages
        case Some(_: ap.MethodExpr) => noMessages
        case Some(_: ap.PredicateExpr) => noMessages
        // imported members, we simply assume that they are wellformed (and were checked in the other package's context)
        case Some(_: ap.Constant) => noMessages
        case Some(_: ap.GlobalVariable) => noMessages
        case Some(_: ap.Function) => noMessages
        case Some(_: ap.Closure) => violation(s"the name of a function literal should not be accessible from a different package")
        case Some(_: ap.NamedType) => noMessages
        case Some(_: ap.BuiltInType) => noMessages
        case Some(_: ap.Predicate) => noMessages
        case Some(_: ap.DomainFunction) => noMessages
        case Some(_: ap.AdtClause) => noMessages
        case Some(_: ap.AdtField) => noMessages

        // TODO: fully supporting packages results in further options: global variable
        // built-in members
        case Some(p: ap.BuiltInReceivedMethod) => memberType(p.symb) match {
          case t: AbstractType => t.messages(n, Vector(exprType(p.recv)))
          case t => error(n, s"expected an AbstractType for built-in method but got $t")
        }
        case Some(p: ap.BuiltInReceivedPredicate) => memberType(p.symb) match {
          case t: AbstractType => t.messages(n, Vector(exprType(p.recv)))
          case t => error(n, s"expected an AbstractType for built-in mpredicate but got $t")
        }
        case Some(p: ap.BuiltInMethodExpr) => memberType(p.symb) match {
          case t: AbstractType => t.messages(n, Vector(symbType(p.typ)))
          case t => error(n, s"expected an AbstractType for built-in method but got $t")
        }
        case Some(p: ap.BuiltInPredicateExpr) => memberType(p.symb) match {
          case t: AbstractType => t.messages(n, Vector(symbType(p.typ)))
          case t => error(n, s"expected an AbstractType for built-in mpredicate but got $t")
        }

        case _ => error(n, s"expected field selection, method or predicate with a receiver, method expression, predicate expression, adt constructor or discriminator or destructor, an imported member or a built-in member, but got $n")
      }
  }

  lazy val exprAndTypeType: Typing[PExpressionAndType] = createTyping[PExpressionAndType] {
    case n: PNamedOperand => exprOrType(n).fold(x => idType(x.asInstanceOf[PNamedOperand].id), _ => SortT)

    case n: PDeref =>
      resolve(n) match {
        case Some(p: ap.Deref) =>
          exprType(p.base) match {
            case Single(PointerT(t)) => t
            case t => violation(s"expected pointer but got $t")
          }
        case Some(_: ap.PointerType) => SortT
        case _ => violation("Deref should always resolve to either the deref or pointer type pattern")
      }

    case n: PDot =>
      resolve(n) match {
        case Some(p: ap.FieldSelection) => memberType(p.symb)
        case Some(p: ap.ReceivedMethod) => memberType(p.symb)
        case Some(p: ap.ReceivedPredicate) => memberType(p.symb)

        case Some(p: ap.MethodExpr) => memberType(p.symb) match {
          case f: FunctionT => extendFunctionType(f, typeSymbType(p.typ))
          case t => violation(s"a method should be typed to a function type, but got $t")
        }

        case Some(p: ap.PredicateExpr) => memberType(p.symb) match {
          case f: FunctionT => extendFunctionType(f, typeSymbType(p.typ))
          case t => violation(s"a predicate should be typed to a function type, but got $t")
        }

        // imported members, we simply assume that they are wellformed (and were checked in the other package's context)
        case Some(p: ap.Constant) => p.symb match {
          case sc: SingleConstant => sc.context.typ(sc.idDef)
          case _ => ???
        }
        case Some(p: ap.GlobalVariable) => p.symb match {
          case sv: GlobalVariable if sv.isSingleModeDecl =>
            sv.typOpt.map(sv.context.symbType).getOrElse(sv.context.typ(sv.expOpt.get))
          case mv: GlobalVariable =>
            // in this case, mv must occur in a declaration in AssignMode.Multi
            mv.expOpt.map(mv.context.typ) match {
              case Some(t: InternalTupleT) => t.ts(mv.idx)
              case t => violation(s"Expected a tuple, but got $t instead")
            }
        }
        case Some(p: ap.Function) => FunctionT(p.symb.args map p.symb.context.typ, p.symb.context.typ(p.symb.result))
        case Some(_: ap.NamedType) => SortT
        case Some(_: ap.BuiltInType) => SortT
        case Some(p: ap.Predicate) => FunctionT(p.symb.args map p.symb.context.typ, AssertionT)
        case Some(p: ap.DomainFunction) => FunctionT(p.symb.args map p.symb.context.typ, p.symb.context.typ(p.symb.result))

        case Some(p: ap.AdtClause) =>
          val fields = p.symb.fields.map(f => f.id.name -> p.symb.context.symbType(f.typ))
          AdtClauseT(p.symb.getName, fields, p.symb.decl, p.symb.typeDecl, p.symb.context)

        case Some(p: ap.AdtField) =>
          p.symb match {
            case dest: AdtDestructor => dest.context.symbType(dest.decl.typ)
            case _: AdtDiscriminator => BooleanT
          }

        // TODO: fully supporting packages results in further options: global variable

        // built-in members
        case Some(p: ap.BuiltInReceivedMethod) => memberType(p.symb) match {
          case t: AbstractType =>
            val recvType = exprType(p.recv)
            if (t.typing.isDefinedAt(Vector(recvType))) t.typing(Vector(recvType))
            else violation(s"expected AbstractType to be defined for $recvType")
          case t => violation(s"a built-in method should be typed to a AbstractType, but got $t")
        }
        case Some(p: ap.BuiltInReceivedPredicate) => memberType(p.symb) match {
          case t: AbstractType =>
            val recvType = exprType(p.recv)
            if (t.typing.isDefinedAt(Vector(recvType))) t.typing(Vector(recvType))
            else violation(s"expected AbstractType to be defined for $recvType")
          case t => violation(s"a built-in mpredicate should be typed to a AbstractType, but got $t")
        }
        case Some(p: ap.BuiltInMethodExpr) => memberType(p.symb) match {
          case t: AbstractType =>
            val recvType = typeSymbType(p.typ)
            if (t.typing.isDefinedAt(Vector(recvType))) extendFunctionType(t.typing(Vector(recvType)), recvType)
            else violation(s"expected AbstractType to be defined for $recvType")
          case t => violation(s"a built-in method should be typed to a AbstractType, but got $t")
        }
        case Some(p: ap.BuiltInPredicateExpr) => memberType(p.symb) match {
          case t: AbstractType =>
            val recvType = typeSymbType(p.typ)
            if (t.typing.isDefinedAt(Vector(recvType))) extendFunctionType(t.typing(Vector(recvType)), recvType)
            else violation(s"expected AbstractType to be defined for $recvType")
          case t => violation(s"a built-in mpredicate should be typed to a AbstractType, but got $t")
        }

        case p => violation(s"expected field selection, method or predicate with a receiver, method expression, or predicate expression pattern, but got $p")
      }

  }(wellDefExprAndType)

  def exprOrTypeType(n: PExpressionOrType): Type = n match {
    case n: PExpression => exprType(n)
    case _: PType => SortT
  }


  /** checks that argument is not a type. The argument might still be an assertion. */
  lazy val isExpr: WellDefinedness[PExpressionOrType] = createWellDef[PExpressionOrType] { n: PExpressionOrType =>
    val isExprCondition = exprOrType(n).isLeft
    error(n, s"expected expression, but got $n", !isExprCondition)
  }

  lazy val wellDefAndExpr: WellDefinedness[PExpression] = createWellDef { n =>
    wellDefExpr(n).out ++ isExpr(n).out
  }

  implicit lazy val wellDefExpr: WellDefinedness[PExpression] = createWellDef {
    case expr: PActualExpression => wellDefActualExpr(expr)
    case expr: PGhostExpression  => wellDefGhostExpr(expr)
  }

  private def wellDefActualExpr(expr: PActualExpression): Messages = expr match {

    case _: PBoolLit | _: PNilLit | _: PStringLit => noMessages

    case n: PIntLit => numExprWithinTypeBounds(n)

    case n: PIota =>
      error(n, s"cannot use iota outside of constant declaration", enclosingPConstDecl(n).isEmpty)

    case n: PFloatLit => error(n, s"floating point literals are not yet supported.")

    case n@PCompositeLit(t, lit) =>
      val mayInit = isEnclosingMayInit(n)
      val simplifiedT = t match {
        case PImplicitSizeArrayType(elem) => ArrayT(lit.elems.size, typeSymbType(elem))
        case t: PType => typeSymbType(t)
      }
      literalAssignableTo.errors(lit, simplifiedT, mayInit)(n)

    case f: PFunctionLit =>
      capturedLocalVariables(f.decl).flatMap(v => addressable.errors(enclosingExpr(v).get)(v)) ++
        wellDefVariadicArgs(f.args) ++
        f.id.fold(noMessages)(id => wellDefID(id).out) ++
        error(f, "Opaque function literals are not yet supported.", f.spec.isOpaque)

    case n: PInvoke =>
      val mayInit = isEnclosingMayInit(n)
      val (l, r) = (exprOrType(n.base), resolve(n))
      (l,r) match {
        case (Right(_), Some(p: ap.Conversion)) =>
          val typ = typeSymbType(p.typ)
          val argWithinBounds: Messages = underlyingTypeP(p.typ) match {
            case Some(_: PIntegerType) => intExprWithinTypeBounds(p.arg, typ)
            case _ => noMessages
          }
          error(n, "Only calls to pure functions and pure methods can be revealed: Cannot reveal a conversion.", n.reveal) ++
            convertibleTo.errors(exprType(p.arg), typ, mayInit)(n) ++
            isExpr(p.arg).out ++
            argWithinBounds

        case (Left(callee), Some(c: ap.FunctionCall)) =>
          val (isOpaque, isMayInit, isImported, isPure) = c.callee match {
            case base: ap.Symbolic => base.symb match {
              case f: st.Function => (f.isOpaque, f.decl.spec.mayBeUsedInInit, f.context != this, f.isPure)
              case m: st.MethodImpl =>
                (m.isOpaque, m.decl.spec.mayBeUsedInInit, m.context != this, m.isPure)
              case _ => (false, true, false, false)
            }
          }
          // We disallow calling interface methods whose receiver type is an interface declared in the current package
          // in initialization code, as it may be dispatched to a method that assumes the current package's invariant.
          val cannotCallItfIfInit = c.callee match {
            case base: ap.ReceivedMethod =>
              val typeRecv = typ(base.recv)
              val isLocallyDefinedItfType = isLocallyDefinedContextualType(typeRecv) && isInterfaceType(typeRecv)
              if (isLocallyDefinedItfType && mayInit)
                error(n, "Call to interface method whose receiver is of an interface type defined in this package is disallowed within code that may run during the initialization of this package.")
              else
                noMessages
            case _ =>
              noMessages
          }
          val onlyRevealOpaqueFunc =
            error(n, "Cannot reveal call to non-opaque function.", n.reveal && !isOpaque)
          val isCallToInit =
            error(n, s"${Constants.INIT_FUNC_NAME} function is not callable",
              c.callee.isInstanceOf[ap.Function] && c.callee.id.name == Constants.INIT_FUNC_NAME)
          // arguments have to be assignable to function
          val wellTypedArgs = exprType(callee) match {
            case FunctionT(args, _) => // TODO: add special assignment
              if (n.spec.nonEmpty) wellDefCallWithSpec(n)
              else if (n.args.isEmpty && args.isEmpty) noMessages
              else multiAssignableTo.errors(n.args map exprType, args, mayInit)(n) ++ n.args.flatMap(isExpr(_).out)
            case t: AbstractType => t.messages(n, n.args map exprType)
            case t => error(n.base, s"type error: got $t but expected function type or AbstractType")
          }
          // Pure functions may always be called from 'mayInit' methods, as they are not allowed to assume the
          // package invariants.
          val mayInitSeparation = error(n, "Function called from 'mayInit' context is not 'mayInit'.",
            !isImported && isEnclosingMayInit(n) && !(isMayInit || isPure))
          cannotCallItfIfInit ++ onlyRevealOpaqueFunc ++ isCallToInit ++ wellTypedArgs ++ mayInitSeparation

        case (Left(_), Some(_: ap.ClosureCall)) =>
          error(n, "Only calls to pure functions and pure methods can be revealed: Cannot reveal a closure call.", n.reveal) ++
            wellDefCallWithSpec(n) ++
            error(n, "Closures may not be called from code that may be executed during initialization", mayInit)

        case (Left(callee), Some(p: ap.PredicateCall)) => // TODO: Maybe move case to other file
          val pureReceiverMsgs = p.predicate match {
            case _: ap.Predicate => noMessages
            case _: ap.PredicateExpr => noMessages
            case pei: ap.PredExprInstance => pei.args flatMap isPureExpr
            case _: ap.BuiltInPredicate => noMessages
            case _: ap.BuiltInPredicateExpr => noMessages
            case rp: ap.ReceivedPredicate => isPureExpr(rp.recv)
            case brp: ap.BuiltInReceivedPredicate => isPureExpr(brp.recv)
            case _: ap.ImplicitlyReceivedInterfacePredicate => noMessages
          }
          val pureArgsMsgs = p.args.flatMap(isPureExpr)
          val argAssignMsgs = exprType(callee) match {
            case FunctionT(args, _) => // TODO: add special assignment
              if (n.args.isEmpty && args.isEmpty) noMessages
              else multiAssignableTo.errors(n.args map exprType, args, mayInit)(n) ++ n.args.flatMap(isExpr(_).out)
            case t: AbstractType => t.messages(n, n.args map exprType)
            case t => error(n.base, s"type error: got $t but expected function type or AbstractType")
          }
          error(n, "Only calls to pure functions and pure methods can be revealed: Cannot reveal a predicate instance.", n.reveal) ++ pureReceiverMsgs ++ pureArgsMsgs ++ argAssignMsgs

        case (Left(callee), Some(_: ap.PredExprInstance)) =>
          val wellTypedArguments = exprType(callee) match {
            case PredT(args) =>
              if (n.args.isEmpty && args.isEmpty) noMessages
              else multiAssignableTo.errors(n.args map exprType, args, mayInit)(n) ++ n.args.flatMap(isExpr(_).out)
            case c => Violation.violation(s"This case should be unreachable, but got $c")
          }
          error(n, "Only calls to pure functions and pure methods can be revealed: Cannot reveal a predicate expression instance.", n.reveal) ++ wellTypedArguments

        case _ => error(n, s"expected a call to a conversion, function, or predicate, but got $n")
      }

    case n@PBitNegation(op) =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(op).out ++ assignableTo.errors(typ(op), UNTYPED_INT_CONST, mayInit)(op)

    case n@PIndexedExp(base, index) =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(base).out ++ isExpr(index).out ++ {
        val baseType = exprType(base)
        val idxType  = exprType(index)
        (underlyingType(baseType), underlyingType(idxType)) match {
          case (Single(base), Single(idx)) => (base, idx) match {
            case (ArrayT(l, _), IntT(_)) =>
              val idxOpt = intConstantEval(index)
              error(index, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

            case (PointerT(ArrayT(l, _)), IntT(_)) =>
              val idxOpt = intConstantEval(index)
              error(index, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

            case (SequenceT(_), IntT(_)) =>
              noMessages

            case (_: SliceT | _: GhostSliceT, IntT(_)) =>
              noMessages

            case (VariadicT(_), IntT(_)) =>
              noMessages

            case (StringT, IntT(_)) =>
              for {
                cBase <- stringConstantEvaluation(n.base)
                cIdx <- intConstantEvaluation(n.index)
                if cIdx < 0 || cBase.length <= cIdx
              } yield Message(n, s"$cIdx is not a valid index of string $cBase.")

            case (MapT(key, _), underlyingIdxType) =>
              // Assignability in Go is a property between a value and and a type. In Gobra, we model this as a relation
              // between two types, which is less precise. Because of this limitation, and with the goal of handling
              // untyped literals, we introduce an extra condition here. This makes the type checker of Gobra accept Go
              // expressions that are not accepted by the compiler.
              val assignableToIdxType = error(index, s"$idxType is not assignable to map key of $key", !assignableTo(idxType, key, mayInit))
              if (assignableToIdxType.nonEmpty) {
                error(index, s"$underlyingIdxType is not assignable to map key of $key", !assignableTo(underlyingIdxType, key, mayInit))
              } else {
                assignableToIdxType
              }

            case (MathMapT(key, _), underlyingIdxType) =>
              // Assignability in Go is a property between a value and and a type. In Gobra, we model this as a relation
              // between two types, which is less precise. Because of this limitation, and with the goal of handling
              // untyped literals, we introduce an extra condition here. This makes the type checker of Gobra accept Go
              // expressions that are not accepted by the compiler.
              val assignableToIdxType = error(index, s"$idxType is not assignable to map key of $key", !assignableTo(idxType, key, mayInit))
              if (assignableToIdxType.nonEmpty) {
                error(index, s"$underlyingIdxType is not assignable to map key of $key", !assignableTo(underlyingIdxType, key, mayInit))
              } else {
                assignableToIdxType
              }

            case (bt, it) => error(index, s"$it index is not a proper index of $bt")
          }

          case (bt, it) => error(index, s"$it index is not a proper index of $bt")
        }
      }


    case n@PSliceExp(base, low, high, cap) => isExpr(base).out ++
      low.fold(noMessages)(isExpr(_).out) ++
      high.fold(noMessages)(isExpr(_).out) ++
      cap.fold(noMessages)(isExpr(_).out) ++
      ((underlyingType(exprType(base)), low map exprType, high map exprType, cap map exprType) match {
        case (ArrayT(l, _), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) =>
          val (lowOpt, highOpt, capOpt) = (low map intConstantEval, high map intConstantEval, cap map intConstantEval)
          error(low, s"index $low is out of bounds", !lowOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            error(high, s"index $high is out of bounds", !highOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            error(cap, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            error(base, s"array $base is not addressable", !addressable(base))

        case (SequenceT(_), lowT, highT, capT) => {
          lowT.fold(noMessages)(t => error(low, s"expected an integer but found $t", !t.isInstanceOf[IntT])) ++
            highT.fold(noMessages)(t => error(high, s"expected an integer but found $t", !t.isInstanceOf[IntT])) ++
            error(cap, "sequence slice expressions do not allow specifying a capacity", capT.isDefined)
        }

        case (ActualPointerT(ArrayT(l, _)), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) =>  // without ghost slices, slicing a ghost pointer is not allowed.
          val (lowOpt, highOpt, capOpt) = (low flatMap  intConstantEval, high flatMap intConstantEval, cap flatMap intConstantEval)
          error(low, s"index $low is out of bounds", !lowOpt.forall(i => i >= 0 && i < l)) ++
            error(high, s"index $high is out of bounds", !highOpt.forall(i => i >= 0 && i < l)) ++
            error(cap, s"index $cap is out of bounds", !capOpt.forall(i => i >= 0 && i <= l))

        case (_: SliceT | _: GhostSliceT, None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => //noMessages
          val lowOpt = low.flatMap(intConstantEval)
          val highOpt = high.flatMap(intConstantEval)
          val lowHighOpt = lowOpt.zip(highOpt)
          error(low, s"index $low is negative", lowOpt.exists(i => 0 > i)) ++
            error(high, s"index $high is negative", highOpt.exists(i => 0 > i)) ++
            error(high, s"invalid slice indices: $high > $low", lowHighOpt.exists { case (l, h) => l > h })

        case (StringT, None | Some(IntT(_)), None | Some(IntT(_)), None) =>
          // slice expressions of string type cannot have a third argument
          val (lenOpt, lowOpt, highOpt) = (
            stringConstantEval(base) map (_.length),
            low flatMap intConstantEval,
            high flatMap intConstantEval
          )
          val lowError = error(low, s"index $low is out of bounds", !lowOpt.forall(i => i >= 0 && lenOpt.forall(i < _)))
          val highError = error(high, s"index $high is out of bounds", !highOpt.forall(i => i >= 0 && lenOpt.forall(i < _)))
          val lowLessHighError = (lowOpt, highOpt) match {
            case (Some(l), Some(h)) =>
              // this error message is the same shown by the go compiler
              error(n, s"invalid slice index: $l > $h", l > h)
            case _ => noMessages
          }
          return lowError ++ highError ++ lowLessHighError

        case (bt, lt, ht, ct) => error(n, s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      })

    case PTypeAssertion(base, typ) =>
      isExpr(base).out ++ isType(typ).out ++ {
        val baseT = exprType(base)
        underlyingType(baseT) match {
          case t: InterfaceT =>
            val at = typeSymbType(typ)
            implements(at, t).asReason(typ, s"type error: type $at does not implement the interface $baseT")
          case t => error(base, s"type error: got $t expected interface")
        }
      }

    case PReceive(e) => isExpr(e).out ++ (exprType(e) match {
      case ChannelT(_, ChannelModus.Bi | ChannelModus.Recv) => noMessages
      case t => error(e, s"expected receive-permitting channel but got $t")
    })

    case PReference(e) => isExpr(e).out ++ effAddressable.errors(e)(e)

    case n@PNegation(e) =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(e).out ++ assignableTo.errors(exprType(e), BooleanT, mayInit)(e)

    case n: PBinaryExp[_,_] =>
        val mayInit = isEnclosingMayInit(n)
        (n, exprOrTypeType(n.left), exprOrTypeType(n.right)) match {
          case (_: PEquals | _: PUnequals | _: PLess | _: PAtMost | _: PGreater | _: PAtLeast | _: PAnd | _: POr, l, r) =>
            // from the spec: "first operand must be assignable to the type of the second operand, or vice versa"
            val fstAssignable = assignableTo.errors(l, r, mayInit)(n)
            val sndAssignable = assignableTo.errors(r, l, mayInit)(n)
            val assignable = if (fstAssignable.isEmpty || sndAssignable.isEmpty) noMessages
              else error(n, s"neither operand is assignable to the type of the other operand")
            @nowarn("msg=not.*?exhaustive")
            val applicable = if (assignable.isEmpty) {
              n match {
                case _: PEquals | _: PUnequals =>
                  // from the spec: "The equality operators == and != apply to operands of comparable types"
                  comparableTypes.errors(l, r)(n)
                case _: PLess | _: PAtMost | _: PGreater | _: PAtLeast =>
                  // from the spec: "The ordering operators <, <=, >, and >= apply to operands of ordered types"
                  orderedType.errors(l)(n.left) ++ orderedType.errors(r)(n.right)
                case _: PAnd | _: POr =>
                  // from the spec: "Logical operators apply to boolean values", which we extend from boolean to assertion values:
                  assignableTo.errors(l, AssertionT, mayInit)(n.left) ++
                    assignableTo.errors(r, AssertionT, mayInit)(n.right)
              }
            } else noMessages
            assignable ++ applicable
          case (_: PAdd, StringT, StringT) => noMessages
          case (_: PAdd | _: PSub | _: PMul | _: PDiv, l, r) if Set(l, r).intersect(Set(UnboundedFloatT, Float32T, Float64T)).nonEmpty =>
            mergeableTypes.errors(l, r)(n)
          case (_: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv, l, r)
            if l == PermissionT || r == PermissionT || getTypeFromCtxt(n).contains(PermissionT) =>
              assignableTo.errors(l, PermissionT, mayInit)(n.left) ++ assignableTo.errors(r, PermissionT, mayInit)(n.right)
          case (_: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv | _: PBitAnd | _: PBitOr | _: PBitXor | _: PBitClear, l, r) =>
            val lIsInteger = assignableTo.errors(l, UNTYPED_INT_CONST, mayInit)(n.left)
            val rIsInteger = assignableTo.errors(r, UNTYPED_INT_CONST, mayInit)(n.right)
            val typesAreMergeable = mergeableTypes.errors(l, r)(n)
            val exprWithinBounds = {
              if(typesAreMergeable.isEmpty) {
                // Only makes sense to check that a binary expression is within bounds if the types of its
                // subexpressions can be combined

                // mergedType must exist because, otherwise typesAreMergeable.isEmpty would not hold
                val mergedType = typeMerge(l, r).get

                // The first two checks ensure that, if an operand is constant, then it must be assignable to the type
                // of the result. This makes the type system capable of rejecting expressions like `uint8(1) * (-1)`,
                // which are also rejected by the go compiler
                intExprWithinTypeBounds(n.left.asInstanceOf[PExpression], mergedType) ++
                  intExprWithinTypeBounds(n.right.asInstanceOf[PExpression], mergedType) ++
                  intExprWithinTypeBounds(n, mergedType)
              } else noMessages
            }
            lIsInteger ++ rIsInteger ++ typesAreMergeable ++ exprWithinBounds
          case (_: PShiftLeft, l, r) =>
            val integerOperands = assignableTo.errors(l, UNTYPED_INT_CONST, mayInit)(n.left) ++
              assignableTo.errors(r, UNTYPED_INT_CONST, mayInit)(n.right)
            if (integerOperands.isEmpty) {
              intConstantEval(n.right.asInstanceOf[PExpression]) match {
                case Some(v) =>
                  // The Go compiler checks that the RHS of (<<) is non-negative and, at most, the size
                  // of the type of the left operand (or 512 if there is an untyped const on the left)
                  val lowerBound = error(n.right, s"constant ${n.right} overflows uint", v < 0)
                  val nBits = underlyingType(exprOrTypeType(n.left)) match {
                    case IntT(t: BoundedIntegerKind) => t.nbits
                    case IntT(UnboundedInteger) => MAX_SHIFT
                    case t => violation(s"unexpected type $t")
                  }
                  val upperBound = error(n.right, s"shift count ${n.right} too large for type ${exprOrTypeType(n.left)}", v > nBits)
                  lowerBound ++ upperBound

                case None => noMessages
              }
            } else integerOperands
          case (_: PShiftRight, l, r) =>
            val integerOperands = assignableTo.errors(l, UNTYPED_INT_CONST, mayInit)(n.left) ++
              assignableTo.errors(r, UNTYPED_INT_CONST, mayInit)(n.right)
            if (integerOperands.isEmpty) {
              (intConstantEval(n.right.asInstanceOf[PExpression]) match {
                case Some(v) =>
                  // The Go compiler only checks that the RHS of (>>) is non-negative
                  error(n, s"constant $r overflows uint", v < 0)
                case None => noMessages
              })
            } else integerOperands
          case (_, l, r) => error(n, s"$l and $r are invalid type arguments for $n")
        }

    case n: PUnfolding => isExpr(n.op).out ++ isPureExpr(n.op) ++
      wellDefFoldable(n.pred) ++
      error(
        n.pred,
        s"unfolding predicate expression instance ${n.pred} not supported",
        resolve(n.pred.pred).exists(_.isInstanceOf[ap.PredExprInstance]))

    case PLength(op) => isExpr(op).out ++ {
      underlyingType(exprType(op)) match {
        case _: ArrayT | _: SliceT | _: GhostSliceT | StringT | _: VariadicT | _: MapT | _: MathMapT => noMessages
        case ActualPointerT(_: ArrayT)  =>
          // Go allows getting the length of a pointer to an array, but it does not allow obtaining the
          // length of a pointer to type T whose underlying type is an array.
          noMessages
        case _: SequenceT | _: SetT | _: MultisetT | _: AdtT => isPureExpr(op)
        case typ => error(op, s"expected an array, string, sequence or slice type, but got $typ")
      }
    }

    case PCapacity(op) => isExpr(op).out ++ {
      underlyingType(exprType(op)) match {
        case _: ArrayT | _: SliceT | _: GhostSliceT => noMessages
        case ActualPointerT(_: ArrayT)  =>
          // Go allows getting the length of a pointer to an array, but it does not allow obtaining the
          // length of a pointer to type T whose underlying type is an array.
          noMessages
        case typ => error(op, s"expected an array or slice type, but got $typ")
      }
    }

    case _: PNew => noMessages

    case m@PMake(typ, args) =>
      val mayInit = isEnclosingMayInit(m)
      args.flatMap { arg =>
        assignableTo.errors(exprType(arg), INT_TYPE, mayInit)(arg) ++
          error(arg, s"arguments to make must be non-negative", intConstantEval(arg).exists(_ < 0))
      } ++ (underlyingTypeP(typ) match {
        case None => violation(s"unexpected case reached: underlyingTypeP($typ) returned None")
        case Some(t) => t match {
          case _: PSliceType | _: PGhostSliceType =>
            error(m, s"too many arguments to make($typ)", args.length > 2) ++
              error(m, s"missing len argument to make($typ)", args.isEmpty) ++
              check(args) {
                case args if args.length == 2 =>
                  val maybeLen = intConstantEval(args(0))
                  val maybeCap = intConstantEval(args(1))
                  error(m, s"len larger than cap in make($typ)", maybeLen.isDefined && maybeCap.isDefined && maybeLen.get > maybeCap.get)
              }

          case _: PChannelType =>
            error(m, s"too many arguments passed to make($typ)", args.length > 1)

          case PMapType(k, _) =>
            error(m, s"too many arguments passed to make($typ)", args.length > 1) ++
              error(m, s"key type $k is not comparable", !comparableType(symbType(k))) // TODO: add check that type does not contain ghost

          case _ => error(typ, s"cannot make type $typ")
        }
      })

    case b@PBlankIdentifier() => b match {
      case tree.parent(p) => p match {
        case PAssignment(_, _) => noMessages
        case PAssForRange(_, _, _,  _) => noMessages
        case PSelectAssRecv(_, _, _) => noMessages
        case x => error(b, s"blank identifier is not allowed in $x")
      }
      case _ => violation("blank identifier always has a parent")
    }



    case PUnpackSlice(elem) => underlyingType(exprType(elem)) match {
      case _: SliceT => noMessages
      case t => error(expr, s"Tried to unpack value of type $t, which is not a slice type")
    }

    case p@PPredConstructor(base, _) => {
      val mayInit = isEnclosingMayInit(p)
      def wellTypedApp(base: PPredConstructorBase): Messages = miscType(base) match {
        case FunctionT(args, AssertionT) =>
          val unappliedPositions = p.args.zipWithIndex.filter(_._1.isEmpty).map(_._2)
          val givenArgs = p.args.zipWithIndex.filterNot(x => unappliedPositions.contains(x._2)).map(_._1.get)
          val expectedArgs = args.zipWithIndex.filterNot(x => unappliedPositions.contains(x._2)).map(_._1)
          if (givenArgs.isEmpty && expectedArgs.isEmpty) {
            noMessages
          } else {
            multiAssignableTo.errors(givenArgs map exprType, expectedArgs, mayInit)(p) ++
              p.args.flatMap(x => x.map(isExpr(_).out).getOrElse(noMessages))
          }

        case abstractT: AbstractType =>
          // contextual information would be necessary to predict the constructor's return type (i.e. to find type of unapplied arguments)
          // right now we only support fully applied arguments for built-in predicates
          val givenArgs = p.args.flatten
          if (givenArgs.length != p.args.length) {
            error(p, s"partial application is not supported for built-in predicates")
          } else {
            val givenArgTypes = givenArgs map exprType
            val msgs = abstractT.messages(p, givenArgTypes)
            if (msgs.nonEmpty) {
              msgs
            } else {
              // the typing function should be defined for these arguments as `msgs` is empty
              abstractT.typing(givenArgTypes) match {
                case FunctionT(args, AssertionT) =>
                  if (givenArgs.isEmpty && args.isEmpty) {
                    noMessages
                  } else {
                    multiAssignableTo.errors(givenArgs map exprType, args, mayInit)(p) ++
                      p.args.flatMap(x => x.map(isExpr(_).out).getOrElse(noMessages))
                  }
                case t => error(p, s"expected function type for resolved AbstractType but got $t")
              }
            }
          }

        case t => error(p, s"expected base of function type, got ${base.id} of type $t")
      }

      wellDefMisc(base).out ++ wellTypedApp(base)
    }

    case n: PExpressionAndType => wellDefExprAndType(n).out
  }

  private def numExprWithinTypeBounds(num: PNumExpression): Messages =
    intExprWithinTypeBounds(num, numExprType(num))

  private def intExprWithinTypeBounds(exp: PExpression, typ: Type): Messages = {
    if (typ == UNTYPED_INT_CONST) {
      val typCtx = getNonInterfaceTypeFromCtxt(exp)
      typCtx.map(underlyingType) match {
        case Some(intTypeCtx: IntT) => assignableWithinBounds.errors(intTypeCtx, exp)(exp)
        case Some(t) => error(exp, s"$exp is not assignable to type $t")
        case None => noMessages // no type inferred from context
      }
    } else {
      assignableWithinBounds.errors(typ, exp)(exp)
    }
  }

  lazy val exprType: Typing[PExpression] = {
    def handleTypeAlias(t: Type): Type = t match {
      case DeclaredT(PTypeAlias(right, _), context) => context.symbType(right)
      case _ => t
    }
    createTyping {
      case expr: PActualExpression => handleTypeAlias(actualExprType(expr))
      case expr: PGhostExpression => handleTypeAlias(ghostExprType(expr))
    }
  }

  private def actualExprType(expr: PActualExpression): Type = expr match {

    case _: PBoolLit => BooleanT
    case _: PNilLit => NilType
    case _: PStringLit => StringT
    case _: PFloatLit => UnboundedFloatT

    case cl: PCompositeLit => expectedCompositeLitType(cl)

    case PFunctionLit(_, PClosureDecl(args, result, _, _)) =>
      FunctionT(args map miscType, miscType(result))

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(p: ap.Conversion)) => typeSymbType(p.typ)
      case (Left(_), Some(_: ap.PredExprInstance)) =>
        // a PInvoke on a predicate expression instance must fully apply the predicate arguments
        AssertionT
      case (Left(callee), Some(_: ap.FunctionCall | _: ap.PredicateCall | _: ap.ClosureCall)) =>
        exprType(callee) match {
          case FunctionT(_, res) => res
          case t: AbstractType =>
            val argTypes = n.args map exprType
            if (t.typing.isDefinedAt(argTypes)) t.typing(argTypes).result
            else violation(s"expected typing function in AbstractType to be defined for $argTypes")
          case t => violation(s"expected function type or AbstractType but got $t")
        }
      case p => violation(s"expected conversion, function call, predicate call, or predicate expression instance, but got $p")
    }

    case i@PIndexedExp(base, index) =>
      val mayInit = isEnclosingMayInit(i)
      val baseType = exprType(base)
      val idxType  = exprType(index)
      (underlyingType(baseType), underlyingType(idxType)) match {
        case (Single(base), Single(idx)) => (base, idx) match {
          case (ArrayT(_, elem), IntT(_)) => elem
          case (PointerT(ArrayT(_, elem)), IntT(_)) => elem
          case (SequenceT(elem), IntT(_)) => elem
          case (SliceT(elem), IntT(_)) => elem
          case (GhostSliceT(elem), IntT(_)) => elem
          case (VariadicT(elem), IntT(_)) => elem
          case (StringT, IntT(_)) => IntT(TypeBounds.Byte)
          case (MapT(key, elem), underlyingIdxType) if assignableTo(idxType, key, mayInit) || assignableTo(underlyingIdxType, key, mayInit) =>
            InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
          case (MathMapT(key, elem), underlyingIdxType) if assignableTo(idxType, key, mayInit) || assignableTo(underlyingIdxType, key, mayInit) =>
            InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
          case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
        }
        case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
      }

    case PSliceExp(base, low, high, cap) =>
      val baseType = exprType(base)
      (underlyingType(baseType), low map exprType, high map exprType, cap map exprType) match {
        case (ArrayT(_, elem), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) if addressable(base) => SliceT(elem)
        case (ActualPointerT(ArrayT(_, elem)), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => SliceT(elem)
        case (SequenceT(_), None | Some(IntT(_)), None | Some(IntT(_)), None) => baseType
        case (SliceT(_), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => baseType
        case (GhostSliceT(_), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => baseType
        case (StringT, None | Some(IntT(_)), None | Some(IntT(_)), None) => baseType
        case (bt, lt, ht, ct) => violation(s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      }

    case PTypeAssertion(_, typ) =>
      val resT = typeSymbType(typ)
      InternalSingleMulti(resT, InternalTupleT(Vector(resT, BooleanT)))

    case PReceive(e) => exprType(e) match {
      case ChannelT(elem, ChannelModus.Bi | ChannelModus.Recv) =>
        InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
      case t => violation(s"expected receive-permitting channel but got $t")
    }

    case PReference(exp) if effAddressable(exp) =>
      // we do not care whether the reference itself is in a ghost context or not but whether `exp` is ghost
      if (isGhostLocation(exp)) GhostPointerT(exprType(exp)) else ActualPointerT(exprType(exp))

    case n: PAnd => // is boolean if left and right argument are boolean, otherwise is an assertion
      val mayInit = isEnclosingMayInit(n)
      val lt = exprType(n.left)
      val rt = exprType(n.right)
      if (assignableTo(lt, BooleanT, mayInit) && assignableTo(rt, BooleanT, mayInit)) BooleanT else AssertionT

    case _: PNegation | _: PEquals | _: PUnequals | _: POr |
         _: PLess | _: PAtMost | _: PGreater | _: PAtLeast =>
      BooleanT

    case e: PLength => typeOfPLength(e)

    case _: PCapacity => INT_TYPE

    case exprNum: PNumExpression =>
      val typ = numExprType(exprNum)
      if (typ == UNTYPED_INT_CONST) getNonInterfaceTypeFromCtxt(exprNum).getOrElse(typ) else typ

    case n: PUnfolding => exprType(n.op)

    case n: PExpressionAndType => exprAndTypeType(n)

    case b: PBlankIdentifier => getBlankIdType(b)

    case n: PNew => if (isEnclosingGhost(n)) GhostPointerT(typeSymbType(n.typ)) else ActualPointerT(typeSymbType(n.typ))

    case PMake(typ, _) => typeSymbType(typ)

    case PPredConstructor(base, args) =>
      val errorMessage: Any => String =
        t => s"expected function or AbstractType for base of a predicate constructor but got $t"

      base match {
        case PFPredBase(id) =>
          idType(id) match {
            case FunctionT(fnArgs, AssertionT) =>
              PredT(fnArgs.zip(args).collect{ case (typ, None) => typ })
            case _: AbstractType =>
              PredT(Vector()) // because partial application is not supported yet for built-in predicates
            case t => violation(errorMessage(t))
          }

        case p: PDottedBase => resolve(p.recvWithId) match {
          case Some(_: ap.Predicate | _: ap.ReceivedPredicate | _: ap.ImplicitlyReceivedInterfacePredicate) =>
            val recvWithIdT = exprOrTypeType(p.recvWithId)
            recvWithIdT match {
              case FunctionT(fnArgs, AssertionT) =>
                PredT(fnArgs.zip(args).collect{ case (typ, None) => typ })
              case _: AbstractType =>
                PredT(Vector()) // because partial application is not supported yet for built-in predicates
              case t => violation(errorMessage(t))
            }

          case Some(_: ap.PredicateExpr) =>
            val recvWithIdT = exprOrTypeType(p.recvWithId)
            recvWithIdT match {
              case FunctionT(fnArgs, AssertionT) =>
                PredT(fnArgs.zip(args).collect{ case (typ, None) => typ })
              case _: AbstractType =>
                PredT(Vector()) // because partial application is not supported yet for built-in predicates
              case t => violation(errorMessage(t))
            }

          case _ => violation(s"unexpected base $base for predicate constructor")
        }
      }

    case PUnpackSlice(exp) =>
      underlyingType(exprType(exp)) match {
        case SliceT(elem) => VariadicT(elem)
        case e => violation(s"expression $e cannot be unpacked")
      }

    case PIota() => UNTYPED_INT_CONST

    case e => violation(s"unexpected expression $e")
  }

  /** Returns a non-interface type that is implied by the context if the integer expression is an untyped
    * constant expression. It ignores interface types. This is useful for checking the bounds of constant expressions when
    * they are assigned to a variable of an interface type. For those cases, we need to obtain the numeric type of the
    * expression.
    */
  private def getNonInterfaceTypeFromCtxt(expr: PExpression): Option[Type] = {
    // if an unbounded integer constant expression is assigned to an interface type,
    // then it has the default type
    def defaultTypeIfInterface(t: Type) : Type = {
      if (t.isInstanceOf[InterfaceT]) DEFAULT_INTEGER_TYPE else t
    }
    // handle cases where it returns a SingleMultiTuple and we only care about a single type
    getTypeFromCtxt(expr).map(defaultTypeIfInterface) match {
      case Some(t) => t match {
        case Single(t) => Some(t)
        case UnknownType => Some(UnknownType)
        case _ => violation(s"unexpected case reached $t")
      }
      case None => None
    }
  }

  /** Returns the type that is implied by the context of an integer expression. */
  private def getTypeFromCtxt(expr: PExpression): Option[Type] = {
    expr match {
      case tree.parent(p) => p match {
        case PShortVarDecl(rights, lefts, _) =>
          val index = rights.indexOf(expr)
          val iden = lefts(index)
          val isDeclaration = iden == PWildcard() || isDef(lefts(index).asInstanceOf[PIdnUnk])
          // if the var is already defined and has a non-interface type T, expr must be of type T. Otherwise,
          // expr must have the default type (Int)
          if (isDeclaration) {
            Some(DEFAULT_INTEGER_TYPE)
          } else {
            val idenType = idType(iden)
            Some(idenType)
          }

        case PAssignmentWithOp(_, _, pAssignee) => Some(exprType(pAssignee))

        case PAssignment(rights, lefts) =>
          val index = rights.indexOf(expr)
          lefts(index) match {
            case PBlankIdentifier() =>
              // if no type is specified, integer constants have the default type
              Some(DEFAULT_INTEGER_TYPE)
            case x => Some(exprType(x))
          }

        // if no type is specified, integer expressions default to unbounded integer in const declarations;
        // there is no need to handle interface types because they are not allowed in constant declarations
        case PConstSpec(typ, _, _) => typ map typeSymbType orElse Some(UNTYPED_INT_CONST)

        // if no type is specified, integer expressions have default type in var declarations
        case PVarDecl(typ, _, _, _) => typ map (x => typeSymbType(x))

        case _: PMake => Some(INT_TYPE)

        case r: PReturn =>
          val index = r.exps.indexOf(expr)
          val returns = returnParamsAndTypes(r)
          if (returns.size <= index) None else Some(returns(index)._1)

        case n: PInvoke =>
          // if the parent of `expr` (i.e. the numeric expression whose type we want to find out) is an invoke expression `inv`,
          // then p can either occur as the base or as an argument of `inv`. However, a numeric expression is not a valid base of a
          // PInvoke and thus, `expr` can onlu appear in `n` as an argument
          lazy val errorMessage = s"violation of assumption: a numeric expression $expr does not occur as an argument of its parent $n"
          resolve(n) match {
            case Some(ap.FunctionCall(_, args)) =>
              val index = args.indexWhere(_.eq(expr))
              violation(index >= 0, errorMessage)
              typOfExprOrType(n.base) match {
                case FunctionT(fArgs, _) =>
                  if (index >= fArgs.length-1 && fArgs.lastOption.exists(_.isInstanceOf[VariadicT])) {
                    fArgs.lastOption.map(_.asInstanceOf[VariadicT].elem)
                  } else {
                    fArgs.lift(index)
                  }
                case _: AbstractType =>
                  /* the abstract type cannot be resolved without creating a loop in kiama because we need to know the
                     types of all arguments in order to resolve it and we need to resolve it in order to find the type
                     of one of its arguments.
                  val messages = t.messages(n.base, args map typ)
                  if(messages.isEmpty) {
                    t.typing(args map typ) match {
                      case FunctionT(fArgs, _) => Some(fArgs(index))
                    }
                  } else {
                    violation(messages.toString())
                  }
                  */
                  None
                case c => Violation.violation(s"This case should be unreachable, but got $c")
              }

            case Some(ap.PredicateCall(_, args)) =>
              val index = args.indexWhere(_.eq(expr))
              violation(index >= 0, errorMessage)
              typOfExprOrType(n.base) match {
                case FunctionT(fArgs, AssertionT) => fArgs.lift(index)
                case _: AbstractType =>
                  /* the abstract type cannot be resolved without creating a loop in kiama for the same reason as above
                  val messages = t.messages(n.base, args map typ)
                  if(messages.isEmpty) {
                    t.typing(args map typ) match {
                      case FunctionT(fArgs, _) => Some(fArgs(index))
                    }
                  } else {
                    violation(messages.toString())
                  }
                  */
                  None
                case c => Violation.violation(s"This case should be unreachable, but got $c")
              }

            case Some(ap.PredExprInstance(base, args, _)) =>
              val index = args.indexWhere(_.eq(expr))
              violation(index >= 0, errorMessage)
              typ(base) match {
                case PredT(fArgs) => fArgs.lift(index)
                case t => violation(s"predicate expression instance has base $base with unsupported type $t")
              }

            case _ => None
          }

      case const: PPredConstructor =>
        // `expr` cannot be `const.id` and thus, it must be one of the arguments
        val index = const.args.indexWhere { _.exists(y => y.eq(expr)) }
        violation(index >= 0, s"violation of assumption: a numeric expression $expr does not occur as an argument of its parent $const")
        typ(const.id) match {
          case FunctionT(args, AssertionT) => Some(args(index))
          case _: AbstractType =>
            // here too, resolving the abstract type would cause a cycle in kiama
            None
          case UnknownType =>
            // TODO: this is a bit of a hack. at some points, the type of const.id may be unknown. This happens, for example,
            //  when a PDottedBase refers to a non existing field. For some reason, that fails to be detected in the well
            //  definedness checker of the PPredConstructorBase (e.g. the last fold of the function `error4` in
            //  https://github.com/viperproject/gobra/blob/master/src/test/resources/regressions/features/defunc/defunc-fail1.gobra
            //  crashes Gobra without this case).
            None
          case c => Violation.violation(s"This case should be unreachable, but got $c")
        }

        // expr has the default type if it appears in any other kind of statement
        case x if x.isInstanceOf[PStatement] => Some(DEFAULT_INTEGER_TYPE)

        case e: PMisc => e match {
          // The following case infers the type of an literal expression when it occurs inside a composite literal.
          // For example, it infers that the expression `1/2` in `seq[perm]{ 1/2 }` has type perm. Notice that the whole
          // expression would be parsed as
          //   PCompositeLit(
          //     PSequenceType(PPermissionType()),
          //     PLiteralValue(Vector(
          //       PKeyedElement(
          //         None,
          //         PExpCompositeVal(PDiv(PIntLit(BigInt(1)), PIntLit(BigInt(2))))))))
          case comp: PCompositeVal => comp match {
            // comp must be the exp of a [[PKeyedElement]], not its key
            case tree.parent(keyedElem: PKeyedElement) if keyedElem.exp == comp =>
              keyedElem match {
                case tree.parent(litValue: PLiteralValue) => litValue match {
                  case tree.parent(PCompositeLit(typ, _)) => typ match {
                    case PSequenceType(elem) => Some(typeSymbType(elem))
                    case PSetType(elem) => Some(typeSymbType(elem))
                    case PMultisetType(elem) => Some(typeSymbType(elem))
                    case PSliceType(elem) => Some(typeSymbType(elem))
                    case PGhostSliceType(elem) => Some(typeSymbType(elem))
                    case PArrayType(_, elem) => Some(typeSymbType(elem))
                    case _ => None // conservative choice
                  }
                  case _ => None
                }
                case _ => None
              }
            case _ => None
          }
          case _ => None
        }
        case _ => None
      }
      case c => Violation.violation(s"Only the root has no parent, but got $c")
    }
  }

  def getBlankIdType(b: PBlankIdentifier): Type = b match {
    case tree.parent(p) => p match {
      case PAssignment(right, left) => getBlankAssigneeType(b, left, right)
      case PAssForRange(range, ass, _, _) => getBlankAssigneeTypeRange(b, ass, range)
      case PSelectAssRecv(_, _, _) => ??? // TODO: implement when select statements are supported
      case x => violation("blank identifier not supported in node " + x)
    }
    case _ => violation("blank identifier always has a parent")
  }

  private def numExprType(expr: PNumExpression): Type = {
    val typ = expr match {
      case _: PIntLit => UNTYPED_INT_CONST

      case _: PFloatLit => UnboundedFloatT

      case e: PLength => typeOfPLength(e)

      case _: PCapacity => INT_TYPE

      case PBitNegation(op) => exprOrTypeType(op)

      case bExpr: PBinaryExp[_, _] =>
        bExpr match {
          case _: PShiftLeft | _: PShiftRight => exprOrTypeType(bExpr.left)
          case _ =>
            val typeLeft = exprOrTypeType(bExpr.left)
            val typeRight = exprOrTypeType(bExpr.right)
            typeMerge(typeLeft, typeRight).getOrElse(UnknownType)
        }

      case e => violation(s"unexpected expression $e while type-checking integer expressions.")
    }

    // handle cases where it returns a SingleMultiTuple and we only care about a single type
    typ match {
      case Single(t) => t
      case UnknownType => UnknownType
      case _ => violation(s"unexpected type $typ")
    }
  }

  def expectedCompositeLitType(lit: PCompositeLit): Type = lit.typ match {
    case i: PImplicitSizeArrayType => ArrayT(lit.lit.elems.size, typeSymbType(i.elem))
    case t: PType =>
      typeSymbType(t) match {
        case t: AdtClauseT => t.declaredType // adt constructors return the defined type
        case t => t
      }
  }

  private[typing] def wellDefIfConstExpr(expr: PExpression): Messages = underlyingType(typ(expr)) match {
    case BooleanT =>
      error(expr, s"expected constant boolean expression, but got $expr instead", boolConstantEval(expr).isEmpty)
    case typ if underlyingType(typ).isInstanceOf[IntT] =>
      error(expr, s"expected constant int expression, but got $expr instead", intConstantEval(expr).isEmpty)
    case StringT =>
      error(expr, s"expected constant string expression, but got $expr instead", stringConstantEval(expr).isEmpty)
    case PermissionT =>
      val constExprOpt = permConstantEval(expr)
      error(expr, s"expected constant perm expression, but got $expr instead", constExprOpt.isEmpty) ++
        error(expr, s"the divisor of the perm expression $expr evaluates to 0", constExprOpt.exists(_._2 == 0))
    case _ => error(expr, s"expected a constant expression, but got $expr instead")
  }

  private[typing] def wellDefCallWithSpec(n: PInvoke): Messages = {
    val mayInit = isEnclosingMayInit(n)
    val base = n.base.asInstanceOf[PExpression]
    val closureMatchesSpec = wellDefIfClosureMatchesSpec(base, n.spec.get)
    val assignableArgs = (exprType(base), miscType(n.spec.get)) match {
      case (tC: FunctionT, _: FunctionT) => n.args.flatMap(isExpr(_).out) ++ (
        if (n.args.isEmpty && tC.args.isEmpty) noMessages
        else multiAssignableTo.errors(n.args map exprType, tC.args, mayInit)(base))
      case (tC, _) => error(base, s"expected function type, but got $tC")
    }

    closureMatchesSpec ++ assignableArgs
  }

  private[typing] def wellDefIfClosureMatchesSpec(closure: PExpression, spec: PClosureSpecInstance): Messages =
    isExpr(closure).out ++ ((exprType(closure), miscType(spec)) match {
      case (tC: FunctionT, tS: FunctionT) => error(spec, s"expected type $tC, got ${spec} of type $tS", cond = !identicalTypes(tC, tS))
      case (tC, _) => error(closure, s"expected function type, but got $tC")
    })

  private[typing] def typeOfPLength(expr: PLength): Type =
    underlyingType(exprType(expr.exp)) match {
      case _: ArrayT | _: SliceT | _: GhostSliceT | StringT | _: VariadicT | _: MapT => INT_TYPE
      case ActualPointerT(_: ArrayT) => INT_TYPE
      case _: SequenceT | _: SetT | _: MultisetT | _: MathMapT | _: AdtT => UNTYPED_INT_CONST
      case t => violation(s"unexpected argument ${expr.exp} of type $t passed to len")
    }

  /**
    * True iff a conversion may produce side-effects, such as allocating a slice.
    * May need to be extended when we introduce support for generics and when we allow
    * a cast from a `[]T` to a `*[n]T` (described in https://go.dev/ref/spec#Conversions).
    */
  override def isEffectfulConversion(c: ap.Conversion): Boolean = {
    val fromType = underlyingType(exprType(c.arg))
    val toType = underlyingType(typeSymbType(c.typ))
    (fromType, toType) match {
      case (StringT, SliceT(IntT(TypeBounds.Byte))) => true
      case _ => false
    }
  }
}
