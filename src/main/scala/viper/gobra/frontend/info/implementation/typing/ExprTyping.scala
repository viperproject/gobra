package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{ActualStructMember, Method}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation

trait ExprTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefExpr: WellDefinedness[PExpression] = createWellDef {
    case expr: PActualExpression => wellDefActualExpr(expr)
    case expr: PGhostExpression  => wellDefGhostExpr(expr)
  }

  private def wellDefActualExpr(expr: PActualExpression): Messages = expr match {

    case n@ PNamedOperand(id) => pointsToData.errors(id)(n)
    case _: PBoolLit | _: PIntLit | _: PNilLit => noMessages

    case n@PCompositeLit(t, lit) =>
      val simplifiedT = t match {
        case PImplicitSizeArrayType(elem) => ArrayT(lit.elems.size, typeType(elem))
        case t: PType => typeType(t)
      }
      literalAssignableTo.errors(lit, simplifiedT)(n)

    case _: PFunctionLit => noMessages

    case n@PConversion(t, arg) => convertibleTo.errors(exprType(arg), typeType(t))(n)

    case n@PCall(base, paras) => exprType(base) match {
      case FunctionT(args, _) =>
        if (paras.isEmpty && args.isEmpty) noMessages
        else multiAssignableTo.errors(paras map exprType, args)(n) // TODO: add special assignment
      case t => message(n, s"type error: got $t but expected function type")
    }

    case n: PConversionOrUnaryCall =>
      resolveConversionOrUnaryCall(n) {
        case (id, arg) => convertibleTo.errors(exprType(arg), idType(id))(n)
      } {
        case (id, arg) => idType(id) match {
          case FunctionT(args, _) => multiAssignableTo.errors(Vector(exprType(arg)), args)(n)
          case t => message(n, s"type error: got $t but expected function type")
        }
      }.getOrElse(message(n, s"could not determine whether $n is a conversion or unary call"))

    case n@PMethodExpr(t, id) => wellDefMethodExpr(t, id)(n)

    case n@PSelection(base, id) => wellDefSelection(base, id)(n)

    case n: PSelectionOrMethodExpr => wellDefSelectionOrMethodExpr(n.base, n.id)(n)

    case n@PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
      case (ArrayT(l, elem), IntT) =>
        val idxOpt = intConstantEval(index)
        message(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

      case (PointerT(ArrayT(l, elem)), IntT) =>
        val idxOpt = intConstantEval(index)
        message(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

      case (SliceT(elem), IntT) => noMessages
      case (MapT(key, elem), indexT) =>
        message(n, s"$indexT is not assignable to map key of $key", !assignableTo(indexT, key))

      case (bt, it) => message(n, s"$it index is not a proper index of $bt")
    }

    case n@PSliceExp(base, low, high, cap) => (exprType(base), exprType(low), exprType(high), cap map exprType) match {
      case (ArrayT(l, elem), IntT, IntT, None | Some(IntT)) =>
        val (lowOpt, highOpt, capOpt) = (intConstantEval(low), intConstantEval(high), cap map intConstantEval)
        message(n, s"index $low is out of bounds", !lowOpt.forall(i => i >= 0 && i < l)) ++
          message(n, s"index $high is out of bounds", !highOpt.forall(i => i >= 0 && i < l)) ++
          message(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => i >= 0 && i <= l))) ++
          message(n, s"array $base is not addressable", !addressable(base))

      case (PointerT(ArrayT(l, elem)), IntT, IntT, None | Some(IntT)) =>
        val (lowOpt, highOpt, capOpt) = (intConstantEval(low), intConstantEval(high), cap map intConstantEval)
        message(n, s"index $low is out of bounds", !lowOpt.forall(i => i >= 0 && i < l)) ++
          message(n, s"index $high is out of bounds", !highOpt.forall(i => i >= 0 && i < l)) ++
          message(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => i >= 0 && i <= l)))

      case (SliceT(elem), IntT, IntT, None | Some(IntT)) => noMessages
      case (bt, lt, ht, ct) => message(n, s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
    }

    case n@PTypeAssertion(base, typ) => exprType(base) match {
      case t: InterfaceT =>
        val at = typeType(typ)
        message(n, s"type error: expression $base of type $at does not implement $typ", implements(at, t))
      case t => message(n, s"type error: got $t expected interface")
    }

    case n@PReceive(e) => exprType(e) match {
      case ChannelT(_, ChannelModus.Bi | ChannelModus.Recv) => noMessages
      case t => message(n, s"expected receive-permitting channel but got $t")
    }

    case n@PReference(e) => effAddressable.errors(e)(n)

    case n@PDereference(exp) => exprType(exp) match {
      case PointerT(t) => noMessages
      case t => message(n, s"expected pointer but got $t")
    }

    case n@PNegation(e) => assignableTo.errors(exprType(e), BooleanT)(n)

    case n: PBinaryExp => (n, exprType(n.left), exprType(n.right)) match {
      case (_: PEquals | _: PUnequals, l, r) => comparableTypes.errors(l, r)(n)
      case (_: PAnd | _: POr, l, r) => assignableTo.errors(l, BooleanT)(n) ++ assignableTo.errors(r, BooleanT)(n)
      case (_: PLess | _: PAtMost | _: PGreater | _: PAtLeast | _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv
      , l, r) => assignableTo.errors(l, IntT)(n) ++ assignableTo.errors(r, IntT)(n)
      case (_, l, r) => message(n, s"$l and $r are invalid type arguments for $n")
    }

    case n: PUnfolding => isPureExpr(n.op)
  }

  def wellDefSelectionOrMethodExpr(base: PIdnUse, id: PIdnUse)(n: PNode): Messages = {
    message(n, s"type ${idType(base)} does not have method ${id.name}"
      , if (pointsToType(base)) !findMethodLike(idType(base), id).exists(_.isInstanceOf[Method])
      else if (pointsToData(base)) !findSelection(base, id).exists(m => m.isInstanceOf[Method] || m.isInstanceOf[ActualStructMember])
      else Violation.violation("base should be either a type or data")
    )
  }

  def wellDefMethodExpr(t: PMethodRecvType, id: PIdnUse)(n: PNode): Messages = {
    message(n, s"type ${typeType(t)} does not have method ${id.name}"
      , !findMethodLike(typeType(t), id).exists(_.isInstanceOf[Method]))
  }

  def wellDefSelection(base: PExpression, id: PIdnUse)(n: PNode): Messages = {
    message(n, s"type ${exprType(base)} does not have method ${id.name}"
      , !findSelection(base, id).exists(m => m.isInstanceOf[Method] || m.isInstanceOf[ActualStructMember]))
  }

  lazy val exprType: Typing[PExpression] = createTyping {
    case expr: PActualExpression => actualExprType(expr)
    case expr: PGhostExpression => ghostExprType(expr)
  }

  private def actualExprType(expr: PActualExpression): Type = expr match {

    case PNamedOperand(id) => idType(id)

    case _: PBoolLit => BooleanT
    case _: PIntLit => IntT
    case _: PNilLit => NilType

    case cl: PCompositeLit => expectedCompositeLitType(cl)

    case PFunctionLit(args, r, _) =>
      FunctionT(args map miscType, miscType(r))

    case PConversion(t, _) => typeType(t)

    case n@PCall(callee, _) => exprType(callee) match {
      case FunctionT(_, res) => res
      case t => violation(s"expected function type but got $t") //(message(n, s""))
    }

    case PConversionOrUnaryCall(base, arg) =>
      idType(base) match {
        case t: DeclaredT => t // conversion
        case FunctionT(args, res) // unary call
          if args.size == 1 && assignableTo(args.head, exprType(arg)) => res
        case t => violation(s"expected function or declared type but got $t")
      }

    case n: PSelectionOrMethodExpr =>
      resolveSelectionOrMethodExpr(n)
      { case (base, id) => findSelection(base, id).map(memberType) }
      { case (base, id) => findMethodLike(idType(base), id).map(m => methodExprType(idType(base), m.asInstanceOf[Method])) }
        .get.getOrElse(violation("no selection found"))

    case PMethodExpr(base, id) =>
      val baseType = typeType(base)
      findMethodLike(baseType, id).map(m => methodExprType(typeType(base), m.asInstanceOf[Method])).getOrElse(violation("no function found"))

    case PSelection(base, id) =>
      findSelection(base, id).map(memberType).getOrElse(violation("no selection found"))

    case PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
      case (ArrayT(_, elem), IntT) => elem
      case (PointerT(ArrayT(_, elem)), IntT) => elem
      case (SliceT(elem), IntT) => elem
      case (MapT(key, elem), indexT) if assignableTo(indexT, key) =>
        InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
      case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
    }

    case PSliceExp(base, low, high, cap) => (exprType(base), exprType(low), exprType(high), cap map exprType) match {
      case (ArrayT(_, elem), IntT, IntT, None | Some(IntT)) if addressable(base) => SliceT(elem)
      case (PointerT(ArrayT(_, elem)), IntT, IntT, None | Some(IntT)) => SliceT(elem)
      case (SliceT(elem), IntT, IntT, None | Some(IntT)) => SliceT(elem)
      case (bt, lt, ht, ct) => violation(s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
    }

    case PTypeAssertion(_, typ) => typeType(typ)

    case PReceive(e) => exprType(e) match {
      case ChannelT(elem, ChannelModus.Bi | ChannelModus.Recv) =>
        InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
      case t => violation(s"expected receive-permitting channel but got $t")
    }

    case PReference(exp) if effAddressable(exp) => PointerT(exprType(exp))

    case PDereference(exp) => exprType(exp) match {
      case PointerT(t) => t
      case t => violation(s"expected pointer but got $t")
    }

    case _: PNegation | _: PEquals | _: PUnequals | _: PAnd | _: POr |
         _: PLess | _: PAtMost | _: PGreater | _: PAtLeast =>
      BooleanT

    case _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv => IntT

    case n: PUnfolding => exprType(n.op)

    case e => violation(s"unexpected expression $e")
  }

  def expectedCompositeLitType(lit: PCompositeLit): Type = lit.typ match {
    case i: PImplicitSizeArrayType => ArrayT(lit.lit.elems.size, typeType(i.elem))
    case t: PType => typeType(t)
  }
}
