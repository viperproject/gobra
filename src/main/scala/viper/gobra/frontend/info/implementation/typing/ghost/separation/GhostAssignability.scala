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
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignMode}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostType.ghost
import viper.gobra.util.Violation

trait GhostAssignability {
  this: TypeInfoImpl =>

  /** checks that ghost arguments are not assigned to non-ghost arguments  */
  private[separation] def ghostAssignableToCallExpr(call: ap.FunctionCall): Messages = {
    val argTyping = calleeArgGhostTyping(call).toTuple
    argsAssignable(call, argTyping)
  }

  private def argsAssignable(call: ap.FunctionCall, argTyping: Vector[Boolean]): Messages = generalGhostAssignableTo[PExpression, Boolean](ghostExprResultTyping){
    case (g, l) => error(call.callee.id, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
  }(call.args: _*)(argTyping: _*)

  /** checks that ghost arguments are not assigned to non-ghost arguments in a call with spec  */
  private[separation] def ghostAssignableToClosureCall(call: ap.ClosureCall): Messages = {
    val isPure = resolve(call.maybeSpec.get.func) match {
      case Some(ap.Function(_, f)) => f.isPure
      case Some(ap.Closure(_, c)) => c.isPure
      case _ => Violation.violation("this case should be unreachable")
    }

    // If the closure variable being called is ghost, ghost is assignable to all parameters.
    if (isPure || isExprGhost(call.callee)) { return noMessages }

    val argTyping = closureSpecArgsAndResGhostTyping(call.maybeSpec.get)._1.toTuple
    call.args.zip(argTyping).flatMap {
      case (g, l) => error(g, "ghost error: ghost cannot be assigned to non-ghost", isExprGhost(g) && !l)
    }
  }

  /** ghost types of the call arguments and results of a closure spec instance.
    * The ghost type depends on that of the corresponding argument in the base function,
    * not on the ghostness of the function. */
  private [separation] def closureSpecArgsAndResGhostTyping(spec: PClosureSpecInstance): (GhostType, GhostType) = {
    def paramTyping(params: Vector[PParameter], context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(params.map(p => context.isParamGhost(p)))

    val (fArgs, fRes, context) = resolve(spec.func) match {
      case Some(ap.Function(_, f)) => (f.args, f.result.outs, f.context)
      case Some(ap.Closure(_, c)) => (c.args, c.result.outs, c.context)
      case _ => Violation.violation("this case should be unreachable")
    }

    val argTyping = if(spec.params.forall(_.key.isEmpty))
      paramTyping(fArgs.drop(spec.params.size), context)
    else {
      val pSet = spec.paramKeys.toSet
      paramTyping(fArgs.filter {
        case PNamedParameter(id, _) if pSet.contains(id.name) => false
        case PExplicitGhostParameter(PNamedParameter(id, _)) if pSet.contains(id.name) => false
        case _ => true
      }, context)
    }
    val resTyping = paramTyping(fRes, context)
    (argTyping, resTyping)
  }

  /** conservative ghost separation assignment check for assignment in actual and ghost code */
  private[separation] def ghostAssignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(ghostAssigneeAssignmentMsg)(exprs: _*)(lefts: _*)

  private def ghostAssigneeAssignmentMsg(isRightGhost: Boolean, left: PAssignee): Messages =
    if (isEnclosingGhost(left)) ghostAssigneeAssignmentMsgInGhostCode(left) else ghostAssigneeAssignmentMsgInActualCode(isRightGhost, left)

  // handles the case of assignments in actual code
  private def ghostAssigneeAssignmentMsgInActualCode(isRightGhost: Boolean, left: PAssignee): Messages = left match {

    case _: PDeref => // *x := e ~ !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to pointer", isRightGhost)

    case PIndexedExp(_, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to index expressions", isRightGhost) ++
        error(left, "ghost error: ghost index are not permitted in index expressions", ghostExprResultClassification(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      error(left, "ghost error: ghost cannot be assigned to non-ghost", isRightGhost && !ghostIdClassification(id))

    case n: PDot => exprOrType(n.base) match {
      case Left(_) => // x.f := e ~ ghost(e) ==> ghostassignee(x.f)
        error(left, "ghost error: ghost cannot be assigned to non-ghost location", isRightGhost && !ghostLocationClassification(left))
      case _ if resolve(n).exists(_.isInstanceOf[ap.GlobalVariable]) =>
        error(left, "ghost error: ghost cannot be assigned to a global variable", isRightGhost)
      case _ => error(left, "ghost error: selections on types are not assignable")
    }

    case PBlankIdentifier() => noMessages
  }

  // handles the case of assignments in ghost code
  private def ghostAssigneeAssignmentMsgInGhostCode(left: PAssignee): Messages =
    error(left, s"ghost error: only ghost locations can be assigned to in ghost code", !ghostLocationClassification(left))

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(dfltGhostAssignableMsg[PIdnNode](wellGhostSeparated)(ghostIdClassification))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(dfltGhostAssignableMsg[PParameter](wellGhostSeparated)(ghostParameterClassification))(exprs: _*)(lefts: _*)

  /**
    * default factory for producing messages that a ghost right-hand side cannot be assigned to a non-ghost left-hand side
    * @param pre well-definedness condition for a left-hand side that must hold before `ghost` might be invoked
    * @param ghost classifier whether a left-hand side is ghost
    * @tparam L type of the left-hand sides
    * @return
    */
  private def dfltGhostAssignableMsg[L <: PNode](pre: WellDefinedness[L])(ghost: L => Boolean): (Boolean, L) => Messages = {
    case (g, l) =>
      if (pre(l).valid) {
        // `l` is well-defined
        error(l, "ghost error: ghost cannot be assigned to non-ghost", g && !ghost(l))
      } else {
        // we skip all dependent checks as `l` is not well-defined.
        // we assume that the corresponding messages about `l` non-well-definedness are reported when visiting `l` in the AST.
        noMessages
      }
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



  /** ghost type of the arguments of a callee */
  private[separation] def calleeArgGhostTyping(call: ap.FunctionCall): GhostType = {
    val explicitGhostType = explicitCalleeArgGhostTyping(call)
    val isImplicitlyGhost = isCallImplicitlyGhost(call)
    // preserve the amount of entries in `explicitGhostType` but set all to true
    // if the call is implicitly ghost. In case there are no entries (i.e., it's not ghost),
    // simply return `isGhost`:
    if (isImplicitlyGhost && explicitGhostType.toTuple.isEmpty) {
      GhostType.isGhost
    } else if (isImplicitlyGhost) {
      GhostType.ghostTuple(Seq.fill(explicitGhostType.length)(true).toVector)
    } else explicitGhostType
  }

  /** ghost type of the arguments of a callee based on explicit annotations of the parameter or callee */
  private def explicitCalleeArgGhostTyping(call: ap.FunctionCall): GhostType = {
    // a parameter of a ghost member is ghost (even if such a explicit declaration is missing)
    def argTyping(args: Vector[PParameter], isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(args.map(p => isMemberGhost || context.isParamGhost(p)))

    call.callee match {
      case p: ap.Function => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.Closure => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.ReceivedMethod => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      // first argument is the receiver which inherits its ghostness from the method's ghostness
      case p: ap.MethodExpr => GhostType.ghostTuple(p.symb.ghost +: argTyping(p.symb.args, p.symb.ghost, p.symb.context).toTuple)
      case _: ap.PredicateKind => GhostType.isGhost
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => argGhostTyping(symb.tag, call.args.map(typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => argGhostTyping(symb.tag, Vector(typ(recv)))
      // first argument is the receiver which inherits its ghostness from the method's ghostness
      case ap.BuiltInMethodExpr(typ, _, _, symb) => GhostType.ghostTuple(symb.ghost +: argGhostTyping(symb.tag, Vector(typeSymbType(typ))).toTuple)
      case p: ap.ImplicitlyReceivedInterfaceMethod => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case _ => GhostType.notGhost // conservative choice
    }
  }

  /* ghost type of the callee itself (not considering its arguments or results) */
  private[separation] def calleeGhostTyping(call: ap.FunctionCall): GhostType = {
    val isExplicitlyGhost = call.callee.ghost
    ghost(isExplicitlyGhost || isCallImplicitlyGhost(call))
  }

  /** ghost type of the result of a callee */
  private[separation] def calleeReturnGhostTyping(call: ap.FunctionCall): GhostType = {
    // a result of a ghost member is ghost (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType = {
      GhostType.ghostTuple(result.outs.map(p => isMemberGhost || context.isParamGhost(p) || isCallImplicitlyGhost(call)))
    }

    call.callee match {
      case p: ap.Function => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case p: ap.ReceivedMethod => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case p: ap.MethodExpr => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case _: ap.PredicateKind => GhostType.isGhost
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => returnGhostTyping(symb.tag, call.args.map(typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => returnGhostTyping(symb.tag, Vector(typ(recv)))
      case ap.BuiltInMethodExpr(typ, _, _, symb) => returnGhostTyping(symb.tag, Vector(typeSymbType(typ)))
      case p: ap.ImplicitlyReceivedInterfaceMethod => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case _ => GhostType.isGhost // conservative choice
    }
  }

  /** returns true if `call` should be treated as being ghost because it is not explicitly
    * annotated as being ghost _and_ one of the arguments is ghost even though the corresponding
    * parameter is not explicitly annotated as being ghost.
    */
  private def isCallImplicitlyGhost(call: ap.FunctionCall): Boolean = {
    // we only do this special treatment for calls to pure functions as the call
    // can be erased without losing any side-effects. Furthermore, we have to do
    // this implicit treatment only if assignability of arguments to parameters
    // would otherwise fail:
    val argTyping = explicitCalleeArgGhostTyping(call).toTuple
    val assignable = argsAssignable(call, argTyping)
    call.callee.isPure && assignable.nonEmpty
  }

  /* If a closure is not ghost, the arguments and results of all the specs it can be called with must have
   * the same ghostness, so that removing ghost arguments yields a consistent result.
   * To ensure this, we make sure, for all spec implementation proofs, that the ghostness of the arguments and result
   * of the spec matches that of the call inside. */
  private [separation] def provenSpecMatchesInGhostnessWithCall(p: PClosureImplProof): Messages = {
    val specTyping = closureSpecArgsAndResGhostTyping(p.impl.spec)

    closureImplProofCallAttr(p) match {
      case c: PInvoke =>
        // If the callee is ghost, we don't care about the ghostness of the arguments.
        if (isExprGhost(c.base.asInstanceOf[PExpression])) noMessages
        else resolve(c) match {
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

  private[separation] def closureCallReturnGhostTyping(call: PInvoke): GhostType = {
    // a result is ghost if the closure is ghost (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, isClosureGhost: Boolean, context: ExternalTypeInfo): GhostType = {
      GhostType.ghostTuple(result.outs.map(p => isClosureGhost || context.isParamGhost(p)))
    }

    resolve(call.spec.get.func) match {
      case Some(p: ap.Function) => resultTyping(p.symb.result, isExprGhost(call.base.asInstanceOf[PExpression]), p.symb.context)
      case Some(p: ap.Closure) => resultTyping(p.symb.result, isExprGhost(call.base.asInstanceOf[PExpression]), p.symb.context)
      case _ => GhostType.isGhost // conservative choice
    }
  }

}
