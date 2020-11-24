// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.SymbolTable.SingleConstant
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ExprTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val wellDefExprAndType: WellDefinedness[PExpressionAndType] = createWellDef {

    case _: PNamedOperand => noMessages // no checks to avoid cycles

    case n: PDeref =>
      resolve(n) match {
        case Some(p: ap.Deref) =>
          exprType(p.base) match {
            case PointerT(t) => noMessages
            case t => message(n, s"expected pointer type but got $t")
          }

        case Some(p: ap.PointerType) => noMessages

        case _ => violation("Deref should always resolve to either the deref or pointer type pattern")
      }

    case n: PDot =>
      resolve(n) match {
        case Some(p: ap.FieldSelection) => noMessages
        case Some(p: ap.ReceivedMethod) => noMessages
        case Some(p: ap.ReceivedPredicate) => noMessages
        case Some(p: ap.MethodExpr) => noMessages
        case Some(p: ap.PredicateExpr) => noMessages
        // imported members, we simply assume that they are wellformed (and were checked in the other package's context)
        case Some(p: ap.Constant) => noMessages
        case Some(p: ap.Function) => noMessages
        case Some(p: ap.NamedType) => noMessages
        case Some(p: ap.Predicate) => noMessages
        // TODO: supporting packages results in further options: global variable
        case _ => message(n, s"expected field selection, method or predicate with a receiver, method expression, predicate expression or an imported member, but got $n")
      }
  }

  lazy val exprAndTypeType: Typing[PExpressionAndType] = createTyping[PExpressionAndType] {
    case n: PNamedOperand => idType(n.id)

    case n: PDeref =>
      resolve(n) match {
        case Some(p: ap.Deref) =>
          exprType(p.base) match {
            case PointerT(t) => t
            case t => violation(s"expected pointer but got $t")
          }
        case Some(p: ap.PointerType) => PointerT(typeType(p.base))
        case _ => violation("Deref should always resolve to either the deref or pointer type pattern")
      }

    case n: PDot =>
      resolve(n) match {
        case Some(p: ap.FieldSelection) => memberType(p.symb)
        case Some(p: ap.ReceivedMethod) => memberType(p.symb)
        case Some(p: ap.ReceivedPredicate) => memberType(p.symb)

        case Some(p: ap.MethodExpr) => memberType(p.symb) match {
          case f: FunctionT => extentFunctionType(f, typeType(p.typ))
          case t => violation(s"a method should be typed to a function type, but got $t")
        }

        case Some(p: ap.PredicateExpr) => memberType(p.symb) match {
          case f: FunctionT => extentFunctionType(f, typeType(p.typ))
          case t => violation(s"a predicate should be typed to a function type, but got $t")
        }

        // imported members, we simply assume that they are wellformed (and were checked in the other package's context)
        case Some(p: ap.Constant) => p.symb match {
          case sc: SingleConstant => sc.context.typ(sc.idDef)
          case _ => ???
        }
        case Some(p: ap.Function) => FunctionT(p.symb.args map p.symb.context.typ, p.symb.context.typ(p.symb.result))
        case Some(p: ap.NamedType) => DeclaredT(p.symb.decl, p.symb.context)
        case Some(p: ap.Predicate) => FunctionT(p.symb.args map p.symb.context.typ, AssertionT)

        // TODO: supporting packages results in further options: global variable
        case p => violation(s"expected field selection, method or predicate with a receiver, method expression, or predicate expression pattern, but got $p")
      }

  }(wellDefExprAndType)


  /** checks that argument is not a type. The argument might still be an assertion. */
  lazy val isExpr: WellDefinedness[PExpressionOrType] = createWellDef[PExpressionOrType] { n: PExpressionOrType =>
    val isExprCondition = exprOrType(n).isLeft
    message(n, s"expected expression, but got $n", !isExprCondition)
  }

  lazy val wellDefAndExpr: WellDefinedness[PExpression] = createWellDef { n =>
    wellDefExpr(n).out ++ isExpr(n).out
  }

  implicit lazy val wellDefExpr: WellDefinedness[PExpression] = createWellDef {
    case expr: PActualExpression => wellDefActualExpr(expr)
    case expr: PGhostExpression  => wellDefGhostExpr(expr)
  }

  private def wellDefActualExpr(expr: PActualExpression): Messages = expr match {

    case _: PBoolLit | _: PNilLit => noMessages

    case n@PIntLit(v) =>
      val typCtx = getTypeFromContext(n)
      if (typCtx.isDefined) assignableWithinBounds.errors(typCtx.get, n)(n) else noMessages

    case n@PCompositeLit(t, lit) => {
      val simplifiedT = t match {
        case PImplicitSizeArrayType(elem) => ArrayT(lit.elems.size, typeType(elem))
        case t: PType => typeType(t)
      }
      literalAssignableTo.errors(lit, simplifiedT)(n)
    }

    case _: PFunctionLit => noMessages

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {

      case (Right(_), Some(p: ap.Conversion)) => // requires single argument and the expression has to be convertible to target type
        val msgs = message(n, "expected a single argument", p.arg.size != 1)
        if (msgs.nonEmpty) msgs
        else convertibleTo.errors(exprType(p.arg.head), typeType(p.typ))(n) ++ isExpr(p.arg.head).out

      case (Left(callee), Some(p: ap.FunctionCall)) => // arguments have to be assignable to function
        exprType(callee) match {
          case FunctionT(args, _) => // TODO: add special assignment
            if (n.args.isEmpty && args.isEmpty) noMessages
            else multiAssignableTo.errors(n.args map exprType, args)(n) ++ n.args.flatMap(isExpr(_).out)
          case t => message(n, s"type error: got $t but expected function type")
        }

      case (Left(callee), Some(p: ap.PredicateCall)) => // TODO: Maybe move case to other file
        val pureReceiverMsgs = p.predicate match {
          case _: ap.Predicate | _: ap.PredicateExpr => noMessages
          case rp: ap.ReceivedPredicate => isPureExpr(rp.recv)
        }
        val pureArgsMsgs = p.args.flatMap(isPureExpr)
        val argAssignMsgs = exprType(callee) match {
          case FunctionT(args, _) => // TODO: add special assignment
            if (n.args.isEmpty && args.isEmpty) noMessages
            else multiAssignableTo.errors(n.args map exprType, args)(n) ++ n.args.flatMap(isExpr(_).out)
          case t => message(n, s"type error: got $t but expected function type")
        }
        pureReceiverMsgs ++ pureArgsMsgs ++ argAssignMsgs


      case _ => message(n, s"expected a call to a conversion, function, or predicate, but got $n")
    }

    case n@PIndexedExp(base, index) =>
      isExpr(base).out ++ isExpr(index).out ++
        ((exprType(base), exprType(index)) match {
          case (ArrayT(l, elem), IntT(_)) =>
            val idxOpt = intConstantEval(index)
            message(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

          case (PointerT(ArrayT(l, elem)), IntT(_)) =>
            val idxOpt = intConstantEval(index)
            message(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

          case (SequenceT(_), IntT(_)) =>
            noMessages

          case (SliceT(_), IntT(_)) =>
            noMessages

          case (MapT(key, elem), indexT) =>
            message(n, s"$indexT is not assignable to map key of $key", !assignableTo(indexT, key))

          case (bt, it) => message(n, s"$it index is not a proper index of $bt")
        })


    case n@PSliceExp(base, low, high, cap) => isExpr(base).out ++
      low.fold(noMessages)(isExpr(_).out) ++
      high.fold(noMessages)(isExpr(_).out) ++
      cap.fold(noMessages)(isExpr(_).out) ++
      ((exprType(base), low map exprType, high map exprType, cap map exprType) match {
        case (ArrayT(l, _), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) =>
          val (lowOpt, highOpt, capOpt) = (low map intConstantEval, high map intConstantEval, cap map intConstantEval)
          message(n, s"index $low is out of bounds", !lowOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            message(n, s"index $high is out of bounds", !highOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            message(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => 0 <= i && i <= l))) ++
            message(n, s"array $base is not addressable", !addressable(base))

        case (SequenceT(_), lowT, highT, capT) => {
          lowT.fold(noMessages)(t => message(low, s"expected an integer but found $t", !t.isInstanceOf[IntT])) ++
            highT.fold(noMessages)(t => message(high, s"expected an integer but found $t", !t.isInstanceOf[IntT])) ++
            message(cap, "sequence slice expressions do not allow specifying a capacity", capT.isDefined)
        }

        case (PointerT(ArrayT(l, _)), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) =>
          val (lowOpt, highOpt, capOpt) = (low map intConstantEval, high map intConstantEval, cap map intConstantEval)
          message(n, s"index $low is out of bounds", !lowOpt.forall(_.forall(i => i >= 0 && i < l))) ++
            message(n, s"index $high is out of bounds", !highOpt.forall(_.forall(i => i >= 0 && i < l))) ++
            message(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => i >= 0 && i <= l)))

        case (SliceT(_), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => //noMessages
          val lowOpt = low.map(intConstantEval)
          message(n, s"index $low is negative", !lowOpt.forall(_.forall(i => 0 <= i)))

        case (bt, lt, ht, ct) => message(n, s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      })

    case n@PTypeAssertion(base, typ) =>
      isExpr(base).out ++ isType(typ).out ++
        (exprType(base) match {
          case t: InterfaceT =>
            val at = typeType(typ)
            message(n, s"type error: expression $base of type $at does not implement $typ", implements(at, t))
          case t => message(n, s"type error: got $t expected interface")
        })

    case n@PReceive(e) => isExpr(e).out ++ (exprType(e) match {
      case ChannelT(_, ChannelModus.Bi | ChannelModus.Recv) => noMessages
      case t => message(n, s"expected receive-permitting channel but got $t")
    })

    case n@PReference(e) => isExpr(e).out ++ effAddressable.errors(e)(n)

    case n@PNegation(e) => isExpr(e).out ++ assignableTo.errors(exprType(e), BooleanT)(n)


    case n: PBinaryExp =>
      val intKind = config.typeBounds.UntypedConst
      isExpr(n.left).out ++ isExpr(n.right).out ++
        ((n, exprType(n.left), exprType(n.right)) match {
          case (_: PEquals | _: PUnequals, l, r) => comparableTypes.errors(l, r)(n)
          case (_: PAnd | _: POr, l, r) => assignableTo.errors(l, AssertionT)(n) ++ assignableTo.errors(r, AssertionT)(n)
          case (_: PLess | _: PAtMost | _: PGreater | _: PAtLeast, l, r) =>
            assignableTo.errors(l, IntT(intKind))(n) ++ assignableTo.errors(r, IntT(intKind))(n)
          case (_: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv, l, r) =>
            assignableTo.errors(l, IntT(intKind))(n) ++ assignableTo.errors(r, IntT(intKind))(n) ++ {
              val res = for {
                typCtx <- getTypeFromContext(n.asInstanceOf[PNumExpression])
              } yield assignableWithinBounds.errors(typCtx, n)(n)
              res.getOrElse(noMessages)
            }
          case (_, l, r) => message(n, s"$l and $r are invalid type arguments for $n")
        })

    case n: PUnfolding => isExpr(n.op).out ++ isPureExpr(n.op)

    case PLength(op) => isExpr(op).out ++ {
      exprType(op) match {
        case _: ArrayT | _: SliceT => noMessages
        case _: SequenceT => isPureExpr(op)
        case typ => message(op, s"expected an array, sequence or slice type, but got $typ")
      }
    }

    case PCapacity(op) => isExpr(op).out ++ {
      exprType(op) match {
        case _: ArrayT | _: SliceT => noMessages
        case typ => message(op, s"expected an array or slice type, but got $typ")
      }
    }

    case n: PExpressionAndType => wellDefExprAndType(n).out
  }

  lazy val exprType: Typing[PExpression] = createTyping {
    case expr: PActualExpression => actualExprType(expr)
    case expr: PGhostExpression => ghostExprType(expr)
  }

  private def actualExprType(expr: PActualExpression): Type = expr match {

    case _: PBoolLit => BooleanT

    case _: PNilLit => NilType

    case cl: PCompositeLit => expectedCompositeLitType(cl)

    case PFunctionLit(args, r, _) =>
      FunctionT(args map miscType, miscType(r))

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(p: ap.Conversion)) => typeType(p.typ)
      case (Left(callee), Some(_: ap.FunctionCall | _: ap.PredicateCall)) =>
        exprType(callee) match {
          case FunctionT(_, res) => res
          case t => violation(s"expected function type but got $t") //(message(n, s""))
        }
      case p => violation(s"expected conversion, function call, or predicate call, but got $p")
    }

    case PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
      case (ArrayT(_, elem), IntT(_)) => elem
      case (PointerT(ArrayT(_, elem)), IntT(_)) => elem
      case (SequenceT(elem), IntT(_)) => elem
      case (SliceT(elem), IntT(_)) => elem
      case (MapT(key, elem), indexT) if assignableTo(indexT, key) =>
        InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
      case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
    }

    case PSliceExp(base, low, high, cap) => (exprType(base), low map exprType, high map exprType, cap map exprType) match {
      case (ArrayT(_, elem), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) if addressable(base) => SliceT(elem)
      case (PointerT(ArrayT(_, elem)), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => SliceT(elem)
      case (SequenceT(elem), None | Some(IntT(_)), None | Some(IntT(_)), None) => SequenceT(elem)
      case (SliceT(elem), None | Some(IntT(_)), None | Some(IntT(_)), None | Some(IntT(_))) => SliceT(elem)
      case (bt, lt, ht, ct) => violation(s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
    }

    case PTypeAssertion(_, typ) => typeType(typ)

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

    case _: PLength => IntT(config.typeBounds.Int)

    case _: PCapacity => IntT(config.typeBounds.Int)

    case exprNum: PNumExpression =>
      val typ = intExprType(exprNum)
      if (typ == IntT(config.typeBounds.UntypedConst)) getTypeFromContext(exprNum).getOrElse(typ) else typ

    case n: PUnfolding => exprType(n.op)

    case n: PExpressionAndType => exprAndTypeType(n)

    case e => violation(s"unexpected expression $e")
  }

  /** returns a type that is implied by the context if the numeric expression is an untyped constant expression */
  private def getTypeFromContext(expr: PNumExpression): Option[Type] = expr match {
    case tree.parent(p) => p match {
      // if no type is specified, integer constants default to int in short var declarations
      case _: PShortVarDecl => Some(IntT(config.typeBounds.Int))
      case PAssignmentWithOp(_, _, pAssignee) => Some(exprType(pAssignee))
      case PAssignment(rights, lefts) =>
        val index = rights.indexOf(expr)
        Some(exprType(lefts(index)))
      case PConstDecl(typ, _, _) => typ map typeType
      case PVarDecl(typ, _, _, _) => typ map typeType
      case n: PInvoke => resolve(n) match {
        case Some(ap.FunctionCall(callee, args)) =>
          val index = args.indexOf(expr)
          callee match {
            case f: ap.Function => Some(typeType(f.symb.args(index).typ))
            case _ => None
          }

        case Some(ap.PredicateCall(pred, args)) =>
          val index = args.indexOf(expr)
          pred match {
            case p: ap.Predicate => Some(typeType(p.symb.args(index).typ))
            case _ => None
          }

        case _ => None
      }
      case _ => None
    }
  }

  private def intExprType(expr: PNumExpression): Type = expr match {
    case _: PIntLit => IntT(config.typeBounds.UntypedConst)

    case _: PLength | _: PCapacity => IntT(config.typeBounds.Int)

    case bExpr: PBinaryExp =>
      val typeLeft = exprType(bExpr.left)
      val typeRight = exprType(bExpr.right)
      typeMerge(typeLeft, typeRight).getOrElse(UnknownType)
  }

  def expectedCompositeLitType(lit: PCompositeLit): Type = lit.typ match {
    case i: PImplicitSizeArrayType => ArrayT(lit.lit.elems.size, typeType(i.elem))
    case t: PType => typeType(t)
  }

  private[typing] def wellDefIfConstExpr(expr: PExpression): Messages = typ(expr) match {
    case BooleanT => message(expr, s"expected constant boolean expression", boolConstantEval(expr).isEmpty)
    case typ if underlyingType(typ).isInstanceOf[IntT] => message(expr, s"expected constant int expression", intConstantEval(expr).isEmpty)
    case _ => message(expr, s"expected a constant expression")
  }
}
