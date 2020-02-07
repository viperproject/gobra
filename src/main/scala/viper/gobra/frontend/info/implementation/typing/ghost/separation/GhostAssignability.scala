package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignModi}
import viper.gobra.util.Violation

trait GhostAssignability {
  this: TypeInfoImpl =>

  /** checks that ghost arguments are not assigned to non-ghost arguments  */
  private[separation] def ghostAssignableToCallExpr(right: PExpression*)(callee: PExpression): Messages = {
    val argTyping = calleeArgGhostTyping(callee).toTuple
    generalGhostAssignableTo[PExpression, Boolean](ghostExprTyping){
      case (g, l) => message(callee, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
    }(right: _*)(argTyping: _*)
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalGhostAssignableTo(ghostExprTyping)(ghostAssigneeAssignmentMsg)(exprs: _*)(lefts: _*)


  private def ghostAssigneeAssignmentMsg(isRightGhost: Boolean, left: PAssignee): Messages = left match {

    case _: PDeref => // *x := e ~ !ghost(e)
      message(left, "ghost error: ghost cannot be assigned to pointer", isRightGhost)

    case PIndexedExp(base, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      message(left, "ghost error: ghost cannot be assigned to index expressions", isRightGhost) ++
        message(left, "ghost error: ghost index are not permitted in index expressions", ghostExprClassification(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      message(left, "ghost error: ghost cannot be assigned to non-ghost", isRightGhost && !ghostIdClassification(id))

    case n: PDot => exprOrType(n.base) match {
      case Left(base) => // x.f := e ~ (ghost(x) || ghost(e)) ==> ghost(f)
        message(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(n.id)) ++
          message(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostExprClassification(base) && !ghostIdClassification(n.id))

      case _ => message(left, "ghost error: selections on types are not assignable")
    }
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalGhostAssignableTo(ghostExprTyping)(dfltGhostAssignableMsg(ghostIdClassification))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalGhostAssignableTo(ghostExprTyping)(dfltGhostAssignableMsg(ghostParameterClassification))(exprs: _*)(lefts: _*)

  private def dfltGhostAssignableMsg[L <: PNode](ghost: L => Boolean): (Boolean, L) => Messages = {
    case (g, l) => message(l, "ghost error: ghost cannot be assigned to non-ghost", g && !ghost(l))
  }

  private def generalGhostAssignableTo[R, L](typing: R => GhostType)(msg: (Boolean, L) => Messages)(rights: R*)(lefts: L*): Messages =
    NonStrictAssignModi(lefts.size, rights.size) match {

      case AssignMode.Single => rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).isGhost, l) }.toVector

      case AssignMode.Multi =>
        val gt = typing(rights.head)
        lefts.zipWithIndex.flatMap{ case (l, idx) => msg(gt.isIdxGhost(idx), l) }.toVector

      case AssignMode.Error => Violation.violation("assignment mismatch")
    }



  /** ghost type of the arguments of a callee */
  private[separation] def calleeArgGhostTyping(callee: PExpression): GhostType = {
    def argTyping(args: Vector[PParameter]): GhostType =
      GhostType.ghostTuple(args.map(ghostParameterClassification))

    val x = resolve(callee)

    resolve(callee) match {
      case Some(p: ap.Function) => argTyping(p.symb.args)
      case Some(p: ap.ReceivedMethod) => argTyping(p.symb.args)
      case Some(p: ap.MethodExpr) => GhostType.ghostTuple(false +: argTyping(p.symb.args).toTuple)
      case Some(p: ap.PredicateKind) => GhostType.isGhost
      case _ => GhostType.notGhost // conservative choice
    }
  }

  /** ghost type of the result of a callee */
  private[separation] def calleeReturnGhostTyping(callee: PExpression): GhostType = {
    def resultTyping(result: PResult): GhostType = result match {
      case PVoidResult() => GhostType.notGhost
      case PResultClause(outs) => GhostType.ghostTuple(outs.map(ghostParameterClassification))
    }

    resolve(callee) match {
      case Some(p: ap.Function) => resultTyping(p.symb.result)
      case Some(p: ap.ReceivedMethod) => resultTyping(p.symb.result)
      case Some(p: ap.MethodExpr) => resultTyping(p.symb.result)
      case Some(p: ap.PredicateKind) => GhostType.isGhost
      case _ => GhostType.isGhost // conservative choice
    }
  }

}
