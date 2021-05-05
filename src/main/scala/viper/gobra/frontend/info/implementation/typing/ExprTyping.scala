// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, check, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.SymbolTable.SingleConstant
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation

trait ExprTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  val INT_TYPE: Type = IntT(config.typeBounds.Int)
  val UNTYPED_INT_CONST: Type = IntT(config.typeBounds.UntypedConst)
  // default type of unbounded integer constant expressions when they must have a type
  val DEFAULT_INTEGER_TYPE: Type = INT_TYPE

  lazy val wellDefExprAndType: WellDefinedness[PExpressionAndType] = createWellDef {

    case _: PNamedOperand => noMessages // no checks to avoid cycles

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
        case Some(_: ap.Function) => noMessages
        case Some(_: ap.NamedType) => noMessages
        case Some(_: ap.Predicate) => noMessages
        case Some(_: ap.DomainFunction) => noMessages
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

        case _ => error(n, s"expected field selection, method or predicate with a receiver, method expression, predicate expression, an imported member or a built-in member, but got $n")
      }
  }

  lazy val exprAndTypeType: Typing[PExpressionAndType] = createTyping[PExpressionAndType] {
    case n: PNamedOperand =>
      exprOrType(n).fold(x => idType(x.asInstanceOf[PNamedOperand].id), _ => SortT)

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
        case Some(p: ap.Function) => FunctionT(p.symb.args map p.symb.context.typ, p.symb.context.typ(p.symb.result))
        case Some(_: ap.NamedType) => SortT
        case Some(p: ap.Predicate) => FunctionT(p.symb.args map p.symb.context.typ, AssertionT)
        case Some(p: ap.DomainFunction) => FunctionT(p.symb.args map p.symb.context.typ, p.symb.context.typ(p.symb.result))

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

    case n@PCompositeLit(t, lit) =>
      val simplifiedT = t match {
        case PImplicitSizeArrayType(elem) => ArrayT(lit.elems.size, typeSymbType(elem))
        case t: PType => typeSymbType(t)
      }
      literalAssignableTo.errors(lit, simplifiedT)(n)

    case _: PFunctionLit => noMessages

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {

      case (Right(_), Some(p: ap.Conversion)) => // requires single argument and the expression has to be convertible to target type
        val msgs = error(n, "expected a single argument", p.arg.size != 1)
        if (msgs.nonEmpty) msgs
        else convertibleTo.errors(exprType(p.arg.head), typeSymbType(p.typ))(n) ++ isExpr(p.arg.head).out

      case (Left(callee), Some(_: ap.FunctionCall)) => // arguments have to be assignable to function
        exprType(callee) match {
          case FunctionT(args, _) => // TODO: add special assignment
            if (n.args.isEmpty && args.isEmpty) noMessages
            else multiAssignableTo.errors(n.args map exprType, args)(n) ++ n.args.flatMap(isExpr(_).out)
          case t: AbstractType => t.messages(n, n.args map exprType)
          case t => error(n, s"type error: got $t but expected function type or AbstractType")
        }

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
            else multiAssignableTo.errors(n.args map exprType, args)(n) ++ n.args.flatMap(isExpr(_).out)
          case t: AbstractType => t.messages(n, n.args map exprType)
          case t => error(n, s"type error: got $t but expected function type or AbstractType")
        }
        pureReceiverMsgs ++ pureArgsMsgs ++ argAssignMsgs

      case (Left(callee), Some(_:ap.PredExprInstance)) =>
        exprType(callee) match {
          case PredT(args) =>
            if (n.args.isEmpty && args.isEmpty) noMessages
            else multiAssignableTo.errors(n.args map exprType, args)(n) ++ n.args.flatMap(isExpr(_).out)
          case c => Violation.violation(s"This case should be unreachable, but got $c")
        }

      case _ => error(n, s"expected a call to a conversion, function, or predicate, but got $n")
    }

    case n@PIndexedExp(base, index) =>
      isExpr(base).out ++ isExpr(index).out ++
        ((exprType(base), exprType(index)) match {
          case (ArrayT(l, _), IntT(_)) =>
            val idxOpt = intConstantEval(index)
            error(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

          case (PointerT(ArrayT(l, _)), IntT(_)) =>
            val idxOpt = intConstantEval(index)
            error(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

          case (SequenceT(_), IntT(_)) =>
            noMessages

          case (_: SliceT | _: GhostSliceT, IntT(_)) =>
            noMessages

          case (VariadicT(_), IntT(_)) =>
            noMessages

          case (StringT, IntT(_)) =>
            error(n, "Indexing a string is currently not supported")

          case (MapT(key, _), indexT) =>
            error(n, s"$indexT is not assignable to map key of $key", !assignableTo(indexT, key))

          case (MathMapT(key, _), indexT) =>
            error(n, s"$indexT is not assignable to map key of $key", !assignableTo(indexT, key))

          case (bt, it) => error(n, s"$it index is not a proper index of $bt")
        })


    case n@PSliceExp(base, low, high, cap) => isExpr(base).out ++
      low.fold(noMessages)(isExpr(_).out) ++
      high.fold(noMessages)(isExpr(_).out) ++
      cap.fold(noMessages)(isExpr(_).out) ++
      ((exprType(base), low map exprType, high map exprType, cap map exprType) match {
        case (ArrayT(l, _), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) =>
          val (lowOpt, highOpt, capOpt) = (low map intConstantEval, high map intConstantEval, cap map intConstantEval)
          error(n, s"index $low is out of bounds", !lowOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            error(n, s"index $high is out of bounds", !highOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            error(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            error(n, s"array $base is not addressable", !addressable(base))

        case (SequenceT(_), lowT, highT, capT) => {
          lowT.fold(noMessages)(t => error(low, s"expected an integer but found $t", !t.isInstanceOf[IntT])) ++
            highT.fold(noMessages)(t => error(high, s"expected an integer but found $t", !t.isInstanceOf[IntT])) ++
            error(cap, "sequence slice expressions do not allow specifying a capacity", capT.isDefined)
        }

        case (PointerT(ArrayT(l, _)), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) =>
          val (lowOpt, highOpt, capOpt) = (low map intConstantEval, high map intConstantEval, cap map intConstantEval)
          error(n, s"index $low is out of bounds", !lowOpt.forall(_.forall(i => i >= 0 && i < l))) ++
            error(n, s"index $high is out of bounds", !highOpt.forall(_.forall(i => i >= 0 && i < l))) ++
            error(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => i >= 0 && i <= l)))

        case (_: SliceT | _: GhostSliceT, None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => //noMessages
          val lowOpt = low.map(intConstantEval)
          error(n, s"index $low is negative", !lowOpt.forall(_.forall(i => 0 <= i)))

        case (bt, lt, ht, ct) => error(n, s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      })

    case n@PTypeAssertion(base, typ) =>
      isExpr(base).out ++ isType(typ).out ++ {
        val baseT = exprType(base)
        underlyingType(baseT) match {
          case t: InterfaceT =>
            val at = typeSymbType(typ)
            implements(at, t).asReason(n, s"type error: type $at does not implement the interface $baseT")
          case t => error(n, s"type error: got $t expected interface")
        }
      }

    case n@PReceive(e) => isExpr(e).out ++ (exprType(e) match {
      case ChannelT(_, ChannelModus.Bi | ChannelModus.Recv) => noMessages
      case t => error(n, s"expected receive-permitting channel but got $t")
    })

    case n@PReference(e) => isExpr(e).out ++ effAddressable.errors(e)(n)

    case n@PNegation(e) => isExpr(e).out ++ assignableTo.errors(exprType(e), BooleanT)(n)

    case n: PBinaryExp[_,_] =>
        (n, exprOrTypeType(n.left), exprOrTypeType(n.right)) match {
          case (_: PEquals | _: PUnequals, l, r) => comparableTypes.errors(l, r)(n)
          case (_: PAnd | _: POr, l, r) => assignableTo.errors(l, AssertionT)(n) ++ assignableTo.errors(r, AssertionT)(n)
          case (_: PLess | _: PAtMost | _: PGreater | _: PAtLeast, l, r) => (l,r) match {
            case (StringT, StringT) => noMessages
            case _ if l == PermissionT || r == PermissionT =>
              assignableTo.errors(l, PermissionT)(n) ++ assignableTo.errors(r, PermissionT)(n)
            case _ => assignableTo.errors(l, UNTYPED_INT_CONST)(n) ++ assignableTo.errors(r, UNTYPED_INT_CONST)(n)
          }
          case (_: PAdd, StringT, StringT) => noMessages
          case (_: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv, l, r) =>
            if (l == PermissionT || r == PermissionT || getTypeFromCtxt(n.asInstanceOf[PNumExpression]).contains(PermissionT)) {
              assignableTo.errors(l, PermissionT)(n) ++ assignableTo.errors(r, PermissionT)(n)
            } else {
              assignableTo.errors(l, UNTYPED_INT_CONST)(n) ++ assignableTo.errors(r, UNTYPED_INT_CONST)(n) ++
                numExprWithinTypeBounds(n.asInstanceOf[PNumExpression])
            }
          case (_, l, r) => error(n, s"$l and $r are invalid type arguments for $n")
        }

    case n: PUnfolding => isExpr(n.op).out ++ isPureExpr(n.op) ++
      error(
        n.pred,
        s"unfolding predicate expression instance ${n.pred} not supported",
        resolve(n.pred.pred).exists(_.isInstanceOf[ap.PredExprInstance]))

    case PLength(op) => isExpr(op).out ++ {
      exprType(op) match {
        case _: ArrayT | _: SliceT | _: GhostSliceT | StringT | _: VariadicT | _: MapT | _: MathMapT => noMessages
        case _: SequenceT => isPureExpr(op)
        case typ => error(op, s"expected an array, string, sequence or slice type, but got $typ")
      }
    }

    case PCapacity(op) => isExpr(op).out ++ {
      exprType(op) match {
        case _: ArrayT | _: SliceT | _: GhostSliceT => noMessages
        case typ => error(op, s"expected an array or slice type, but got $typ")
      }
    }

    case _: PNew => noMessages

    case m@PMake(typ, args) =>
      args.flatMap { arg =>
        assignableTo.errors(exprType(arg), INT_TYPE)(arg) ++
          error(arg, s"arguments to make must be non-negative", intConstantEval(arg).exists(_ < 0))
      } ++ (typ match {
        case _: PSliceType | _: PGhostSliceType =>
          error(m, s"too many arguments to make($typ)", args.length > 2) ++
            error(m, s"missing len argument to make($typ)", args.isEmpty) ++
            check(args){
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
      })

    case PBlankIdentifier() => noMessages

    case PUnpackSlice(elem) => error(expr, "only slices can be unpacked", !exprType(elem).isInstanceOf[SliceT])

    case p@PPredConstructor(base, _) => {
      def wellTypedApp(base: PPredConstructorBase): Messages = miscType(base) match {
        case FunctionT(args, AssertionT) =>
          val unappliedPositions = p.args.zipWithIndex.filter(_._1.isEmpty).map(_._2)
          val givenArgs = p.args.zipWithIndex.filterNot(x => unappliedPositions.contains(x._2)).map(_._1.get)
          val expectedArgs = args.zipWithIndex.filterNot(x => unappliedPositions.contains(x._2)).map(_._1)
          if (givenArgs.isEmpty && expectedArgs.isEmpty) {
            noMessages
          } else {
            multiAssignableTo.errors(givenArgs map exprType, expectedArgs)(p) ++
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
                    multiAssignableTo.errors(givenArgs map exprType, args)(p) ++
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

  private def numExprWithinTypeBounds(num: PNumExpression): Messages = {
    val typ = intExprType(num)
    if (typ == UNTYPED_INT_CONST) {
      val typCtx = getNonInterfaceTypeFromCtxt(num)
      typCtx.map(underlyingType) match {
        case Some(intTypeCtx: IntT) => assignableWithinBounds.errors(intTypeCtx, num)(num)
        case Some(t) => error(num, s"$num is not assignable to type $t")
        case None => noMessages // no type inferred from context
      }
    } else {
      assignableWithinBounds.errors(typ, num)(num)
    }
  }

  lazy val exprType: Typing[PExpression] = createTyping {
    case expr: PActualExpression => actualExprType(expr)
    case expr: PGhostExpression => ghostExprType(expr)
  }

  private def actualExprType(expr: PActualExpression): Type = expr match {

    case _: PBoolLit => BooleanT
    case _: PNilLit => NilType
    case _: PStringLit => StringT

    case cl: PCompositeLit => expectedCompositeLitType(cl)

    case PFunctionLit(args, r, _) =>
      FunctionT(args map miscType, miscType(r))

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(p: ap.Conversion)) => typeSymbType(p.typ)
      case (Left(_), Some(_: ap.PredExprInstance)) =>
        // a PInvoke on a predicate expression instance must fully apply the predicate arguments
        AssertionT
      case (Left(callee), Some(_: ap.FunctionCall | _: ap.PredicateCall)) =>
        exprType(callee) match {
          case FunctionT(_, res) => res
          case t: AbstractType =>
            val argTypes = n.args map exprType
            if (t.typing.isDefinedAt(argTypes)) t.typing(argTypes)
            else violation(s"expected typing function in AbstractType to be defined for $argTypes")
          case t => violation(s"expected function type or AbstractType but got $t")
        }
      case p => violation(s"expected conversion, function call, predicate call, or predicate expression instance, but got $p")
    }

    case PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
      case (ArrayT(_, elem), IntT(_)) => elem
      case (PointerT(ArrayT(_, elem)), IntT(_)) => elem
      case (SequenceT(elem), IntT(_)) => elem
      case (SliceT(elem), IntT(_)) => elem
      case (GhostSliceT(elem), IntT(_)) => elem
      case (VariadicT(elem), IntT(_)) => elem
      case (MapT(key, elem), indexT) if assignableTo(indexT, key) =>
        InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
      case (MathMapT(key, elem), indexT) if assignableTo(indexT, key) =>
        InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
      case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
    }

    case PSliceExp(base, low, high, cap) => (exprType(base), low map exprType, high map exprType, cap map exprType) match {
      case (ArrayT(_, elem), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) if addressable(base) => SliceT(elem)
      case (PointerT(ArrayT(_, elem)), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => SliceT(elem)
      case (SequenceT(elem), None | Some(IntT(_)), None | Some(IntT(_)), None) => SequenceT(elem)
      case (SliceT(elem), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => SliceT(elem)
      case (GhostSliceT(elem), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => GhostSliceT(elem)
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

    case PReference(exp) if effAddressable(exp) => PointerT(exprType(exp))

    case n: PAnd => // is boolean if left and right argument are boolean, otherwise is an assertion
      val lt = exprType(n.left)
      val rt = exprType(n.right)
      if (assignableTo(lt, BooleanT) && assignableTo(rt, BooleanT)) BooleanT else AssertionT

    case _: PNegation | _: PEquals | _: PUnequals | _: POr |
         _: PLess | _: PAtMost | _: PGreater | _: PAtLeast =>
      BooleanT

    case _: PLength => INT_TYPE

    case _: PCapacity => INT_TYPE

    case exprNum: PNumExpression =>
      val typ = intExprType(exprNum)
      if (typ == UNTYPED_INT_CONST) getNonInterfaceTypeFromCtxt(exprNum).getOrElse(typ) else typ

    case n: PUnfolding => exprType(n.op)

    case n: PExpressionAndType => exprAndTypeType(n)

    case b: PBlankIdentifier => getBlankIdType(b)

    case PNew(typ) => PointerT(typeSymbType(typ))

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

    case PUnpackSlice(exp) => exprType(exp) match {
      case SliceT(elem) => VariadicT(elem)
      case e => violation(s"expression $e cannot be unpacked")
    }

    case e => violation(s"unexpected expression $e")
  }

  /** Returns a non-interface type that is implied by the context if the numeric expression is an untyped
    * constant expression. It ignores interface types. This is useful to check the bounds of constant expressions when
    * they are assigned to a variable of an interface type. For those cases, we need to obtain the numeric type of the
    * expression.
    */
  private def getNonInterfaceTypeFromCtxt(expr: PNumExpression): Option[Type] = {
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

  /** Returns the type that is implied by the context of a numeric expression. */
  private def getTypeFromCtxt(expr: PNumExpression): Option[Type] = {
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
        case PConstDecl(typ, _, _) => typ map typeSymbType orElse Some(UNTYPED_INT_CONST)

        // if no type is specified, integer expressions have default type in var declarations
        case PVarDecl(typ, _, _, _) => typ map (x => typeSymbType(x))

        case _: PMake => Some(INT_TYPE)

        case r: PReturn =>
          val index = r.exps.indexOf(expr)
          Some(typeSymbType(enclosingCodeRootWithResult(r).result.outs(index).typ))

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

            case Some(ap.PredExprInstance(base, args)) =>
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
      case PAssForRange(_, _, _) => ??? // TODO: implement when for range statements are supported
      case PSelectAssRecv(_, _, _) => ??? // TODO: implement when select statements are supported
      case x => violation("blank identifier not supported in node " + x)
    }
    case _ => violation("blank identifier always has a parent")
  }

  private def intExprType(expr: PNumExpression): Type = {
    val typ = expr match {
      case _: PIntLit => UNTYPED_INT_CONST

      case _: PLength | _: PCapacity => INT_TYPE

      case bExpr: PBinaryExp[_,_] =>
        val typeLeft = exprOrTypeType(bExpr.left)
        val typeRight = exprOrTypeType(bExpr.right)
        typeMerge(typeLeft, typeRight).getOrElse(UnknownType)
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
    case t: PType => typeSymbType(t)
  }

  private[typing] def wellDefIfConstExpr(expr: PExpression): Messages = typ(expr) match {
    case BooleanT => error(expr, s"expected constant boolean expression", boolConstantEval(expr).isEmpty)
    case typ if underlyingType(typ).isInstanceOf[IntT] => error(expr, s"expected constant int expression", intConstantEval(expr).isEmpty)
    case StringT => error(expr, s"expected constant string expression", stringConstantEval(expr).isEmpty)
    case _ => error(expr, s"expected a constant expression")
  }
}
