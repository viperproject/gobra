// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.base.GhostLevel
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignMode}
import viper.gobra.util.Violation

/**
  * Checks that values only flow to locations, parameters, and results of at least their own ghost level, see
  * [[viper.gobra.frontend.info.base.GhostLattice]].
  */
trait GhostAssignability {
  this: TypeInfoImpl =>

  /**
    * Checks that the values an operation reads may flow into everything it writes: the join of the levels read has
    * to be assignable to the meet of the levels written. An operation without writes (meet = top), such as a
    * specification, accepts every read, and one without reads (join = bottom) may write anywhere.
    * `msg` produces the error message from the join of the reads and the meet of the writes.
    */
  private[separation] def flow(n: PNode, reads: Iterable[GhostLevel], writes: Iterable[GhostLevel])(msg: (GhostLevel, GhostLevel) => String): Messages = {
    val read = ghostLattice.lub(reads)
    val written = ghostLattice.glb(writes)
    error(n, msg(read, written), !ghostLattice.leq(read, written))
  }

  /** message that a value of level `right` cannot be assigned to a target of level `left` */
  private def assignMsg(right: GhostLevel, left: GhostLevel): String =
    s"ghost error: ${ghostLattice.show(right)} cannot be assigned to ${if (left.isGhost) ghostLattice.show(left) else "non-ghost"}"

  /** checks that arguments are not assigned to parameters of a lower ghost level */
  private[separation] def ghostAssignableToCallExpr(call: ap.FunctionCall): Messages = {
    val argTyping = calleeArgGhostTyping(call).levels
    argsAssignable(call, argTyping)
  }

  private def argsAssignable(call: ap.FunctionCall, argTyping: Vector[GhostLevel]): Messages = generalGhostAssignableTo[PExpression, GhostLevel](ghostExprResultTyping){
    case (r, l) => flow(call.callee.id, Seq(r), Seq(l))(assignMsg)
  }(call.args: _*)(argTyping: _*)

  /** checks that arguments are not assigned to parameters of a lower ghost level in a call with spec */
  private[separation] def ghostAssignableToClosureCall(call: ap.ClosureCall): Messages = {
    val target = closureSpecTarget(call.spec)

    // If the closure variable being called is ghost or the spec specifies ghost or pure closures,
    // ghost is assignable to all parameters.
    if (target.isPure || target.specifiesGhostClosures || isExprGhost(call.callee)) { return noMessages }

    val argTyping = closureSpecArgsAndResGhostTyping(call.maybeSpec.get)._1.levels
    call.args.zip(argTyping).flatMap {
      case (g, l) => flow(g, Seq(exprGhostTyping(g).level), Seq(l))(assignMsg)
    }
  }

  /** closure spec declaration or function literal referenced by `spec` */
  private[separation] def closureSpecTarget(spec: PClosureSpecInstance): st.ClosureSpecTarget = resolve(spec.func) match {
    case Some(t: ap.ClosureSpecTarget) => t.symb
    case _ => Violation.violation(s"expected a closure spec or function literal, but got ${spec.func}")
  }

  /** ghost types of the call arguments and results of a closure spec instance.
    * The ghost type depends on that of the corresponding argument in the spec declaration or function literal and on
    * whether the spec specifies ghost closures, but not on the ghostness of the closure spec declaration itself. */
  private [separation] def closureSpecArgsAndResGhostTyping(spec: PClosureSpecInstance): (GhostType, GhostType) = {
    val target = closureSpecTarget(spec)
    // all arguments and results of closures specified by a ghost closure spec are ghost
    val specLevel = if (target.specifiesGhostClosures) ghostLattice.ghost else ghostLattice.bottom
    def paramTyping(params: Vector[PParameter], context: ExternalTypeInfo): GhostType =
      GhostType.levelTuple(params.map(p => ghostLattice.lub(specLevel, context.paramGhostLevel(p))))

    val (fArgs, fRes, context) = (target.args, target.result.outs, target.context)

    val argTyping = if(spec.params.forall(_.key.isEmpty))
      paramTyping(fArgs.drop(spec.params.size), context)
    else {
      val pSet = spec.paramKeys.toSet
      paramTyping(fArgs.filter {
        case PNamedParameter(id, _) if pSet.contains(id.name) => false
        case PExplicitGhostParameter(PNamedParameter(id, _), _) if pSet.contains(id.name) => false
        case _ => true
      }, context)
    }
    val resTyping = paramTyping(fRes, context)
    (argTyping, resTyping)
  }

  /** conservative ghost separation assignment check for assignment in actual and ghost code */
  private[separation] def ghostAssignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(ghostAssigneeAssignmentMsg)(exprs: _*)(lefts: _*)

  private def ghostAssigneeAssignmentMsg(rightLevel: GhostLevel, left: PAssignee): Messages =
    if (isEnclosingGhost(left)) ghostAssigneeAssignmentMsgInGhostCode(rightLevel, left) else ghostAssigneeAssignmentMsgInActualCode(rightLevel, left)

  // handles the case of assignments in actual code
  private def ghostAssigneeAssignmentMsgInActualCode(rightLevel: GhostLevel, left: PAssignee): Messages = left match {

    case _: PDeref => // *x := e ~ !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to pointer", rightLevel.isGhost)

    case PIndexedExp(_, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to index expressions", rightLevel.isGhost) ++
        error(left, "ghost error: ghost index are not permitted in index expressions", ghostExprResultClassification(index))

    case PNamedOperand(id) => // x := e ~ level(e) ⊑ level(x)
      flow(left, Seq(rightLevel, enclosingGhostLevel(left)), Seq(ghostIdLevel(id)))(assignMsg)

    case n: PDot => exprOrType(n.base) match {
      case Left(_) => // x.f := e ~ level(e) ⊑ level(x.f)
        flow(left, Seq(rightLevel, enclosingGhostLevel(left)), Seq(ghostLocationTyping(left).level))((r, w) => s"${assignMsg(r, w)} location")
      case _ if resolve(n).exists(_.isInstanceOf[ap.GlobalVariable]) =>
        error(left, "ghost error: ghost cannot be assigned to a global variable", rightLevel.isGhost)
      case _ => error(left, "ghost error: selections on types are not assignable")
    }

    case PBlankIdentifier() => noMessages
  }

  // handles the case of assignments in ghost code of any level: the location has to be at least at the level of the
  // surrounding code and at least at the level of the assigned value
  private def ghostAssigneeAssignmentMsgInGhostCode(rightLevel: GhostLevel, left: PAssignee): Messages = {
    val contextLevel = enclosingGhostLevel(left)
    val leftLevel = ghostLocationTyping(left).level
    // the location is written by code of the context's level with a value of the right-hand side's level
    flow(left, Seq(contextLevel, rightLevel), Seq(leftLevel)) { (_, written) =>
      if (!ghostLattice.leq(contextLevel, written)) s"ghost error: only ${ghostLattice.show(contextLevel)} locations can be assigned to in ${ghostLattice.show(contextLevel)} code"
      else assignMsg(rightLevel, written)
    }
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(dfltGhostAssignableMsg[PIdnNode](wellGhostSeparated)(ghostIdLevel))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check; a result parameter of a ghost member is at least at the member's level */
  private[separation] def ghostAssignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(dfltGhostAssignableMsg[PParameter](wellGhostSeparated)(p => ghostLattice.lub(ghostParameterLevel(p), enclosingGhostLevel(p))))(exprs: _*)(lefts: _*)

  /**
    * default factory for producing messages that a right-hand side cannot be assigned to a left-hand side of a lower level
    * @param pre well-definedness condition for a left-hand side that must hold before `level` might be invoked
    * @param level the ghost level of a left-hand side
    * @tparam L type of the left-hand sides
    * @return
    */
  private def dfltGhostAssignableMsg[L <: PNode](pre: WellDefinedness[L])(level: L => GhostLevel): (GhostLevel, L) => Messages = {
    case (r, l) =>
      if (pre(l).valid) {
        // `l` is well-defined
        flow(l, Seq(r), Seq(level(l)))(assignMsg)
      } else {
        // we skip all dependent checks as `l` is not well-defined.
        // we assume that the corresponding messages about `l` non-well-definedness are reported when visiting `l` in the AST.
        noMessages
      }
  }

  private def generalGhostAssignableTo[R, L](typing: R => GhostType)(msg: (GhostLevel, L) => Messages)(rights: R*)(lefts: L*): Messages =
    NonStrictAssignMode(lefts.size, rights.size) match {

      case AssignMode.Single => rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).level, l) }.toVector

      case AssignMode.Multi =>
        val gt = typing(rights.head)
        lefts.zipWithIndex.flatMap{ case (l, idx) => msg(gt.levelAt(idx), l) }.toVector

      case AssignMode.Variadic =>
        if(lefts.length - 1 == rights.length) {
          // if the variadic argument is not passed
          rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).level, l) }.toVector
        } else if(rights.length >= lefts.length) {
          val dummyFill = rights(0)
          rights.zipAll(lefts, dummyFill, lefts.last).flatMap{ case (r,l) => msg(typing(r).level, l) }.toVector
        } else {
          Violation.violation("assignment mismatch")
        }

      case AssignMode.Error => Violation.violation("assignment mismatch")
    }



  /** ghost type of the arguments of a callee */
  private[separation] def calleeArgGhostTyping(call: ap.FunctionCall): GhostType = {
    val explicitGhostType = explicitCalleeArgGhostTyping(call)
    val implicitLevel = implicitCallLevel(call)
    // preserve the amount of entries in `explicitGhostType` but raise all of them to the level of the call
    // if the call is implicitly ghost. In case there are no entries (i.e., it's not ghost), simply return the
    // level of the call:
    if (!implicitLevel.isGhost) {
      explicitGhostType
    } else if (explicitGhostType.length == 0) {
      GhostType.ofLevel(implicitLevel)
    } else {
      GhostType.levelTuple(explicitGhostType.levels.map(l => ghostLattice.lub(l, implicitLevel)))
    }
  }

  /** ghost type of the arguments of a callee based on explicit annotations of the parameter or callee */
  private def explicitCalleeArgGhostTyping(call: ap.FunctionCall): GhostType = {
    // a parameter of a ghost member is at least at the member's level (even if such a explicit declaration is missing)
    def argTyping(args: Vector[PParameter], memberLevel: GhostLevel, context: ExternalTypeInfo): GhostType =
      GhostType.levelTuple(args.map(p => ghostLattice.lub(memberLevel, context.paramGhostLevel(p))))

    call.callee match {
      case p: ap.Function => argTyping(p.symb.args, p.symb.level, p.symb.context)
      case p: ap.Closure => argTyping(p.symb.args, p.symb.level, p.symb.context)
      case p: ap.ReceivedMethod => argTyping(p.symb.args, p.symb.level, p.symb.context)
      // first argument is the receiver which inherits its ghostness from the method's ghostness
      case p: ap.MethodExpr => GhostType.levelTuple(p.symb.level +: argTyping(p.symb.args, p.symb.level, p.symb.context).levels)
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => argGhostTyping(symb.tag, call.args.map(typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => argGhostTyping(symb.tag, Vector(typ(recv)))
      // first argument is the receiver which inherits its ghostness from the method's ghostness
      case ap.BuiltInMethodExpr(typ, _, _, symb) => GhostType.levelTuple(symb.level +: argGhostTyping(symb.tag, Vector(typeSymbType(typ))).levels)
      case p: ap.ImplicitlyReceivedInterfaceMethod => argTyping(p.symb.args, p.symb.level, p.symb.context)
      case _ => GhostType.notGhost // conservative choice
    }
  }

  /* ghost type of the callee itself (not considering its arguments or results) */
  private[separation] def calleeGhostTyping(call: ap.FunctionCall): GhostType =
    GhostType.ofLevel(ghostLattice.lub(call.callee.level, implicitCallLevel(call)))

  /** ghost type of the result of a callee */
  private[separation] def calleeReturnGhostTyping(call: ap.FunctionCall): GhostType = {
    // a result of a ghost member is at least at the member's level (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, memberLevel: GhostLevel, context: ExternalTypeInfo): GhostType = {
      val callLevel = ghostLattice.lub(memberLevel, implicitCallLevel(call))
      GhostType.levelTuple(result.outs.map(p => ghostLattice.lub(callLevel, context.paramGhostLevel(p))))
    }

    call.callee match {
      case p: ap.Function => resultTyping(p.symb.result, p.symb.level, p.symb.context)
      case p: ap.ReceivedMethod => resultTyping(p.symb.result, p.symb.level, p.symb.context)
      case p: ap.MethodExpr => resultTyping(p.symb.result, p.symb.level, p.symb.context)
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => returnGhostTyping(symb.tag, call.args.map(typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => returnGhostTyping(symb.tag, Vector(typ(recv)))
      case ap.BuiltInMethodExpr(typ, _, _, symb) => returnGhostTyping(symb.tag, Vector(typeSymbType(typ)))
      case p: ap.ImplicitlyReceivedInterfaceMethod => resultTyping(p.symb.result, p.symb.level, p.symb.context)
      case _ => GhostType.isGhost // conservative choice
    }
  }

  /** returns the level at which `call` is implicitly treated although the callee is not explicitly declared at that
    * level: a call to a pure function is at the level of its arguments if they are not assignable to the parameters.
    * Otherwise, the bottom level is returned.
    */
  private def implicitCallLevel(call: ap.FunctionCall): GhostLevel = {
    // we only do this special treatment for calls to pure functions as the call
    // can be erased without losing any side-effects. Furthermore, we have to do
    // this implicit treatment only if assignability of arguments to parameters
    // would otherwise fail:
    val argTyping = explicitCalleeArgGhostTyping(call).levels
    val assignable = argsAssignable(call, argTyping)
    if (call.callee.isPure && assignable.nonEmpty) ghostLattice.lub(call.args.map(a => ghostExprResultTyping(a).level))
    else ghostLattice.bottom
  }

  /* If a closure is not ghost, the arguments and results of all the specs it can be called with must have
   * the same ghostness, so that removing ghost arguments yields a consistent result.
   * To ensure this, we make sure, for all spec implementation proofs, that the ghostness of the arguments and result
   * of the spec matches that of the call inside. */
  private [separation] def provenSpecMatchesInGhostnessWithCall(p: PClosureImplProof): Messages = {
    val target = closureSpecTarget(p.impl.spec)
    val specTyping = closureSpecArgsAndResGhostTyping(p.impl.spec)

    if (target.specifiesGhostClosures) {
      // Calls with a ghost spec are ghost. Thus, the implementing closure must not have any side effects on actual
      // state, which is the case if it is pure or if it is ghost itself.
      return error(p.impl.closure,
        s"only ghost or pure closures can implement the ghost closure spec ${p.impl.spec}, but ${p.impl.closure} is neither",
        !target.isPure && !isExprGhost(p.impl.closure))
    }

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
    // a result is at least at the level of the call (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, callLevel: GhostLevel, context: ExternalTypeInfo): GhostType = {
      GhostType.levelTuple(result.outs.map(p => ghostLattice.lub(callLevel, context.paramGhostLevel(p))))
    }

    resolve(call.spec.get.func) match {
      case Some(t: ap.ClosureSpecTarget) =>
        val specLevel = if (t.symb.specifiesGhostClosures) ghostLattice.ghost else ghostLattice.bottom
        val callLevel = ghostLattice.lub(specLevel, exprGhostTyping(call.base.asInstanceOf[PExpression]).level)
        resultTyping(t.symb.result, callLevel, t.symb.context)
      case _ => GhostType.isGhost // conservative choice
    }
  }

}
