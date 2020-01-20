package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Function, MethodImpl, MethodSpec, Regular}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignModi}
import viper.gobra.util.Violation

trait GhostAssignability { this: TypeInfoImpl =>

  private[separation] def assignableToCallExpr(right: PExpression*)(callee: PExpression): Messages = {
    val argTyping = calleeArgGhostTyping(callee).toTuple
    generalAssignableTo[PExpression, Boolean](ghostExprTyping){
      case (g, l) => message(callee, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
    }(right: _*)(argTyping: _*)
  }

  private[separation] def assignableToCallId(right: PExpression)(callee: PIdnNode): Messages = {
    val argTyping = calleeArgGhostTyping(callee)
    message(right, "ghost error: ghost cannot be assigned to non-ghost", ghostExprClassification(right) && !argTyping.isGhost)
  }

  /** conservative ghost separation assignment check */
  private[separation] def assignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalAssignableTo(ghostExprTyping)(assigneeAssignmentMsg)(exprs: _*)(lefts: _*)


  private def assigneeAssignmentMsg(isRightGhost: Boolean, left: PAssignee): Messages = left match {

    case PDereference(op) => // *x := e ~ !ghost(e)
      message(left, "ghost error: ghost cannot be assigned to pointer", isRightGhost)

    case PIndexedExp(base, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      message(left, "ghost error: ghost cannot be assigned to index expressions", isRightGhost) ++
        message(left, "ghost error: ghost index are not permitted in index expressions", ghostExprClassification(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      message(left, "ghost error: ghost cannot be assigned to non-ghost", isRightGhost && !ghostIdClassification(id))

    case PSelection(base, id) => // x.f := e ~ (ghost(x) || ghost(e)) ==> ghost(f)
      message(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(id)) ++
        message(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostExprClassification(base) && !ghostIdClassification(id))
    /*
    case PSelectionOrMethodExpr(base, id) =>
      message(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(id)) ++
        message(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostIdClassification(base) && !ghostIdClassification(id))
     */
  }

  /** conservative ghost separation assignment check */
  private[separation] def assignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalAssignableTo(ghostExprTyping)(dfltAssignableMsg(ghostIdClassification))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check */
  private[separation] def assignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalAssignableTo(ghostExprTyping)(dfltAssignableMsg(ghostParameterClassification))(exprs: _*)(lefts: _*)

  private def dfltAssignableMsg[L <: PNode](ghost: L => Boolean): (Boolean, L) => Messages = {
    case (g, l) => message(l, "ghost error: ghost cannot be assigned to non-ghost", g && !ghost(l))
  }

  private def generalAssignableTo[R, L](typing: R => GhostType)(msg: (Boolean, L) => Messages)(rights: R*)(lefts: L*): Messages =
    NonStrictAssignModi(lefts.size, rights.size) match {

      case AssignMode.Single => rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).isGhost, l) }.toVector

      case AssignMode.Multi =>
        val gt = typing(rights.head)
        lefts.zipWithIndex.flatMap{ case (l, idx) => msg(gt.isIdxGhost(idx), l) }.toVector

      case AssignMode.Error => Violation.violation("assignment mismatch")
    }




  private[separation] def calleeArgGhostTyping(callee: PExpression): GhostType = calleeEntity(callee) match {
    case None => GhostType.notGhost // conservative choice
    case Some(e) =>
      if (isCalleeMethodExpr(callee))
        GhostType.ghostTuple(false +: calleeArgGhostTyping(e).toTuple)
      else
        calleeArgGhostTyping(e)
  }

  private[separation] def calleeArgGhostTyping(callee: PIdnNode): GhostType =
    calleeArgGhostTyping(regular(callee))

  private[separation] def calleeArgGhostTyping(r: Entity): GhostType = {
    def argTyping(args: Vector[PParameter]): GhostType =
      GhostType.ghostTuple(args.map(ghostParameterClassification))

    r match {
      case Function(decl, _)   => argTyping(decl.args)
      case MethodImpl(decl, _) => argTyping(decl.args)
      case MethodSpec(spec, _) => argTyping(spec.args)
      case x => Violation.violation(s"expected callable but got $x")
    }
  }


  private[separation] def calleeReturnGhostTyping(callee: PExpression): GhostType = calleeEntity(callee) match {
    case None => GhostType.isGhost // conservative choice
    case Some(e) => calleeReturnGhostTyping(e)
  }

  private[separation] def calleeReturnGhostTyping(callee: PIdnNode): GhostType =
    calleeReturnGhostTyping(regular(callee))

  private[separation] def calleeReturnGhostTyping(r: Entity): GhostType = {
    def resultTyping(result: PResult): GhostType = result match {
      case PVoidResult() => GhostType.notGhost
      case PResultClause(outs) => GhostType.ghostTuple(outs.map(ghostParameterClassification))
    }

    r match {
      case x: Regular if x.ghost => GhostType.isGhost
      case Function(decl, _)   => resultTyping(decl.result)
      case MethodImpl(decl, _) => resultTyping(decl.result)
      case MethodSpec(spec, _) => resultTyping(spec.result)
      case x => Violation.violation(s"expected callable but got $x")
    }
  }

}
