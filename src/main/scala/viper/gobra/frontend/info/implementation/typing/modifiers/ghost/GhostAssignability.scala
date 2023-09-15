// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.modifiers.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{AstPattern => ap, _}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignMode}
import viper.gobra.frontend.info.implementation.typing.modifiers.ghost.GhostType.ghost
import viper.gobra.util.Violation

trait GhostAssignability { this: GhostModifierUnit  =>

  /** conservative ghost separation assignment check */
  private[ghost] def ghostAssignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(ghostAssigneeAssignmentMsg)(exprs: _*)(lefts: _*)


  private def ghostAssigneeAssignmentMsg(isRightGhost: Boolean, left: PAssignee): Messages = left match {

    case _: PDeref => // *x := e ~ !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to pointer", isRightGhost)

    case PIndexedExp(_, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to index expressions", isRightGhost) ++
        error(left, "ghost error: ghost index are not permitted in index expressions", ghostExprResultClassification(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      error(left, "ghost error: ghost cannot be assigned to non-ghost", isRightGhost && !ghostIdClassification(id))

    case n: PDot => ctx.exprOrType(n.base) match {
      case Left(base) => // x.f := e ~ (ghost(x) || ghost(e)) ==> ghost(f)
        error(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(n.id)) ++
          error(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostExprResultClassification(base) && !ghostIdClassification(n.id))
      case _ if ctx.resolve(n).exists(_.isInstanceOf[ap.GlobalVariable]) =>
        error(left, "ghost error: ghost cannot be assigned to a global variable", isRightGhost)
      case _ => error(left, "ghost error: selections on types are not assignable")
  }

    case PBlankIdentifier() => noMessages
  }

  private def generalGhostAssignableTo[R, L](typing: R => GhostType)(msg: (Boolean, L) => Messages)(rights: R*)(lefts: L*): Messages =
    NonStrictAssignMode(lefts.size, rights.size) match {

      case AssignMode.Single => rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).isGhost, l) }.toVector

      case AssignMode.Multi =>
        val gt = typing(rights.head)
        lefts.zipWithIndex.flatMap{ case (l, idx) => msg(gt.isIdxGhost(idx), l) }.toVector

      case AssignMode.Variadic =>
        if(lefts.length - 1 == rights.length) {
          // if the variadic argument is not passed
          rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).isGhost, l) }.toVector
        } else if(rights.length >= lefts.length) {
          val dummyFill = rights(0)
          rights.zipAll(lefts, dummyFill, lefts.last).flatMap{ case (r,l) => msg(typing(r).isGhost, l) }.toVector
        } else {
          Violation.violation("assignment mismatch")
        }

      case AssignMode.Error => Violation.violation("assignment mismatch")
    }

  /* If a closure is not ghost, the arguments and results of all the specs it can be called with must have
   * the same ghostness, so that removing ghost arguments yields a consistent result.
   * To ensure this, we make sure, for all spec implementation proofs, that the ghostness of the arguments and result
   * of the spec matches that of the call inside. */
  private[ghost] def provenSpecMatchesInGhostnessWithCall(p: PClosureImplProof): Messages = {
    val specTyping = closureSpecArgsAndResGhostTyping(p.impl.spec)

    ctx.closureImplProofCallAttr(p) match {
      case c: PInvoke =>
        // If the callee is ghost, we don't care about the ghostness of the arguments.
        if (isExprGhost(c.base.asInstanceOf[PExpression])) noMessages
        else ctx.resolve(c) match {
          case Some(call: ap.FunctionCall) => error(c,
            s"the ghostness of arguments and results of ${p.impl.spec} and ${c.base} does not match",
            specTyping != (calleeArgGhostTyping(call), calleeReturnGhostTyping(call)))
          case Some(call: ap.ClosureCall) => error(c,
            s"the ghostness of arguments and results of ${p.impl.spec} and ${c.spec} does not match",
            specTyping != closureSpecArgsAndResGhostTyping(call.spec)
          )
          case _ => Violation.violation("expected function call")
        }
    }
  }
}
