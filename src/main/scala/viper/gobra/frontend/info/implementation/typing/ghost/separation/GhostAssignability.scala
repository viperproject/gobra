// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignModi}
import viper.gobra.util.Violation

trait GhostAssignability {
  this: TypeInfoImpl =>

  /** checks that ghost arguments are not assigned to non-ghost arguments  */
  private[separation] def ghostAssignableToCallExpr(call: ap.FunctionCall): Messages = {

    val isPure = call.callee match {
      case p: ap.Function => p.symb.isPure
      case p: ap.MethodExpr => p.symb.isPure
      case p: ap.ReceivedMethod => p.symb.isPure
      case p: ap.BuiltInFunction => p.symb.isPure
      case p: ap.BuiltInMethodExpr => p.symb.isPure
      case p: ap.BuiltInReceivedMethod => p.symb.isPure
      case _ => false
    }
    if (isPure) {return noMessages}

    val argTyping = calleeArgGhostTyping(call).toTuple
    generalGhostAssignableTo[PExpression, Boolean](ghostExprTyping){
      case (g, l) => error(call.callee.id, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
    }(call.args: _*)(argTyping: _*)
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalGhostAssignableTo(ghostExprTyping)(ghostAssigneeAssignmentMsg)(exprs: _*)(lefts: _*)


  private def ghostAssigneeAssignmentMsg(isRightGhost: Boolean, left: PAssignee): Messages = left match {

    case _: PDeref => // *x := e ~ !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to pointer", isRightGhost)

    case PIndexedExp(_, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to index expressions", isRightGhost) ++
        error(left, "ghost error: ghost index are not permitted in index expressions", ghostExprClassification(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      error(left, "ghost error: ghost cannot be assigned to non-ghost", isRightGhost && !ghostIdClassification(id))

    case n: PDot => exprOrType(n.base) match {
      case Left(base) => // x.f := e ~ (ghost(x) || ghost(e)) ==> ghost(f)
        error(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(n.id)) ++
          error(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostExprClassification(base) && !ghostIdClassification(n.id))

      case _ => error(left, "ghost error: selections on types are not assignable")
    }

    case PBlankIdentifier() => noMessages
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalGhostAssignableTo(ghostExprTyping)(dfltGhostAssignableMsg(ghostIdClassification))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalGhostAssignableTo(ghostExprTyping)(dfltGhostAssignableMsg(ghostParameterClassification))(exprs: _*)(lefts: _*)

  private def dfltGhostAssignableMsg[L <: PNode](ghost: L => Boolean): (Boolean, L) => Messages = {
    case (g, l) => error(l, "ghost error: ghost cannot be assigned to non-ghost", g && !ghost(l))
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
  private[separation] def calleeArgGhostTyping(call: ap.FunctionCall): GhostType = {
    def argTyping(args: Vector[PParameter], context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(args.map(context.isParamGhost))

    call.callee match {
      case p: ap.Function => argTyping(p.symb.args, p.symb.context)
      case p: ap.ReceivedMethod => argTyping(p.symb.args, p.symb.context)
      case p: ap.MethodExpr => GhostType.ghostTuple(false +: argTyping(p.symb.args, p.symb.context).toTuple)
      case _: ap.PredicateKind => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => symb.tag.argGhostTyping(call.args.map(typ))(config)
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => symb.tag.argGhostTyping(Vector(typ(recv)))(config)
      case ap.BuiltInMethodExpr(typ, _, _, symb) => GhostType.ghostTuple(false +: symb.tag.argGhostTyping(Vector(typeSymbType(typ)))(config).toTuple)
      case _ => GhostType.notGhost // conservative choice
    }
  }

  /** ghost type of the result of a callee */
  private[separation] def calleeReturnGhostTyping(call: ap.FunctionCall): GhostType = {
    def resultTyping(result: PResult, context: ExternalTypeInfo): GhostType = {
      GhostType.ghostTuple(result.outs.map(context.isParamGhost))
    }

    call.callee match {
      case p: ap.Function => resultTyping(p.symb.result, p.symb.context)
      case p: ap.ReceivedMethod => resultTyping(p.symb.result, p.symb.context)
      case p: ap.MethodExpr => resultTyping(p.symb.result, p.symb.context)
      case _: ap.PredicateKind => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => symb.tag.returnGhostTyping(call.args.map(typ))(config)
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => symb.tag.returnGhostTyping(Vector(typ(recv)))(config)
      case ap.BuiltInMethodExpr(typ, _, _, symb) => symb.tag.returnGhostTyping(Vector(typeSymbType(typ)))(config)
      case _ => GhostType.isGhost // conservative choice
    }
  }

}
