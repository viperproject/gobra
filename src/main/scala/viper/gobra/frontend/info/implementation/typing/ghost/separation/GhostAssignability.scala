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
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.implementation.property.{AssignMode, NonStrictAssignMode}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostType.ghost
import viper.gobra.util.Violation

trait GhostAssignability {
  this: TypeInfoImpl =>

  /** checks that ghost arguments are not assigned to non-ghost arguments  */
  private[separation] def ghostAssignableToCallExpr(call: ap.FunctionCall): Messages = {

    val isPure = call.callee match {
      case p: ap.Function => p.symb.isPure
      case p: ap.Closure => p.symb.isPure
      case p: ap.MethodExpr => p.symb.isPure
      case p: ap.ReceivedMethod => p.symb.isPure
      case p: ap.BuiltInFunction => p.symb.isPure
      case p: ap.BuiltInMethodExpr => p.symb.isPure
      case p: ap.BuiltInReceivedMethod => p.symb.isPure
      case p: ap.ImplicitlyReceivedInterfaceMethod => p.symb.isPure
      case _: ap.DomainFunction => true
      case _ => false
    }
    if (isPure) {return noMessages}

    val argTyping = calleeArgGhostTyping(call).toTuple
    generalGhostAssignableTo[PExpression, Boolean](ghostExprResultTyping){
      case (g, l) => error(call.callee.id, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
    }(call.args: _*)(argTyping: _*)
  }

  /** checks that ghost expressions are not assigned to non-ghost parameters  */
  private[separation] def ghostAssignableToSpecParams(spec: PClosureSpecInstance): Messages = {
    val isPure = entity(spec.func) match {
      case f: SymbolTable.Function => f.isPure
      case c: SymbolTable.Closure => c.isPure
    }
    if (isPure) {return noMessages}

    val paramTyping = specParamsOrCallArgsGhostTyping(spec, takeParams=true).toTuple
    generalGhostAssignableTo[PExpression, Boolean](ghostExprResultTyping){
      case (g, l) => error(spec.func, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
    }(spec.params.map(_.exp): _*)(paramTyping: _*)
  }

  /** checks that ghost arguments are not assigned to non-ghost arguments in a call with spec  */
  private[separation] def ghostAssignableToCallWithSpec(call: PCallWithSpec): Messages = {
    val isPure = entity(call.spec.func) match {
      case f: SymbolTable.Function => f.isPure
      case c: SymbolTable.Closure => c.isPure
    }
    if (isPure) {return noMessages}

    lazy val isGhostArg: PExpression => Boolean = {
      val paramIsGhost = tryEnclosingClosureImplementationProof(call)
        .map(proof => entity(proof.impl.spec.func).asInstanceOf[SymbolTable.WithArguments with SymbolTable.WithResult])
        .toVector.flatMap(f => f.args ++ f.result.outs).collect {
        case PExplicitGhostParameter(PNamedParameter(id, _)) => id.name -> true
        case PNamedParameter(id, _) => id.name -> false
      }.toMap

      {
        case PNamedOperand(id) if paramIsGhost.contains(id.name) => paramIsGhost(id.name)
        case e => isExprGhost(e)
      }
    }

    val argTyping = specParamsOrCallArgsGhostTyping(call.spec, takeParams=false).toTuple
    call.args.zip(argTyping).flatMap {
      case (g, l) => error(g, "ghost error: ghost cannot be assigned to non-ghost", isGhostArg(g) && !l)
    }
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(ghostAssigneeAssignmentMsg)(exprs: _*)(lefts: _*)


  private def ghostAssigneeAssignmentMsg(isRightGhost: Boolean, left: PAssignee): Messages = left match {

    case _: PDeref => // *x := e ~ !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to pointer", isRightGhost)

    case PIndexedExp(_, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      error(left, "ghost error: ghost cannot be assigned to index expressions", isRightGhost) ++
        error(left, "ghost error: ghost index are not permitted in index expressions", ghostExprResultClassification(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      error(left, "ghost error: ghost cannot be assigned to non-ghost", isRightGhost && !ghostIdClassification(id))

    case n: PDot => exprOrType(n.base) match {
      case Left(base) => // x.f := e ~ (ghost(x) || ghost(e)) ==> ghost(f)
        error(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(n.id)) ++
          error(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostExprResultClassification(base) && !ghostIdClassification(n.id))

      case _ => error(left, "ghost error: selections on types are not assignable")
    }

    case PBlankIdentifier() => noMessages
  }

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(dfltGhostAssignableMsg(ghostIdClassification))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check */
  private[separation] def ghostAssignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalGhostAssignableTo(ghostExprResultTyping)(dfltGhostAssignableMsg(ghostParameterClassification))(exprs: _*)(lefts: _*)

  private def dfltGhostAssignableMsg[L <: PNode](ghost: L => Boolean): (Boolean, L) => Messages = {
    case (g, l) => error(l, "ghost error: ghost cannot be assigned to non-ghost", g && !ghost(l))
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
    // a parameter of a ghost member is ghost (even if such a explicit declaration is missing)
    def argTyping(args: Vector[PParameter], isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(args.map(p => isMemberGhost || context.isParamGhost(p)))

    call.callee match {
      case p: ap.Function => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.Closure => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.ReceivedMethod => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.MethodExpr => GhostType.ghostTuple(false +: argTyping(p.symb.args, p.symb.ghost, p.symb.context).toTuple)
      case _: ap.PredicateKind => GhostType.isGhost
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => argGhostTyping(symb.tag, call.args.map(typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => argGhostTyping(symb.tag, Vector(typ(recv)))
      case ap.BuiltInMethodExpr(typ, _, _, symb) => GhostType.ghostTuple(false +: argGhostTyping(symb.tag, Vector(typeSymbType(typ))).toTuple)
      case p: ap.ImplicitlyReceivedInterfaceMethod => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case _ => GhostType.notGhost // conservative choice
    }
  }

  /* ghost type of the callee itself (not considering its arguments or results) */
  private[separation] def calleeGhostTyping(call: ap.FunctionCall): GhostType = call.callee match {
    case p: ap.Function => ghost(p.symb.ghost)
    case p: ap.ReceivedMethod => ghost(p.symb.ghost)
    case p: ap.MethodExpr => ghost(p.symb.ghost)
    case _: ap.PredicateKind => GhostType.isGhost
    case _: ap.DomainFunction => GhostType.isGhost
    case p: ap.BuiltInFunction => ghost(p.symb.ghost)
    case p: ap.BuiltInReceivedMethod => ghost(p.symb.ghost)
    case p: ap.BuiltInMethodExpr => ghost(p.symb.ghost)
    case _ => GhostType.isGhost // conservative choice
  }

  /** ghost type of the result of a callee */
  private[separation] def calleeReturnGhostTyping(call: ap.FunctionCall): GhostType = {
    // a result of a ghost member is ghost (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType = {
      GhostType.ghostTuple(result.outs.map(p => isMemberGhost || context.isParamGhost(p)))
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

  private [separation] def callWithSpecArgsGhostTyping(spec: PClosureSpecInstance): GhostType =
    specParamsOrCallArgsGhostTyping(spec, takeParams = false)

  /** ghost types of the parameters or call arguments of a closure spec instance where
    * - the parameters are those specified in the closure spec literal
    * - the arguments are the remaining arguments of the base functions
    * The ghost type depends on that of the corresponding argument in the base function.
    * @param takeParams is used to switch between the two behaviours */
  private def specParamsOrCallArgsGhostTyping(spec: PClosureSpecInstance, takeParams: Boolean): GhostType = {
    def argTyping(args: Vector[PParameter], isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(args.map(p => isMemberGhost || context.isParamGhost(p)))

    val (isMemberGhost, fArgs, context) = entity(spec.func) match {
      case f: SymbolTable.Function => (f.ghost, f.args, f.context)
      case c: SymbolTable.Closure => (c.ghost, c.args, c.context)
    }

    if(spec.params.forall(_.key.isEmpty))
      argTyping(if (takeParams) fArgs.take(spec.params.size) else fArgs.drop(spec.params.size), isMemberGhost, context)
    else {
      val pSet = spec.params.map(p => p.key.get.name).toSet
      argTyping(fArgs.filter {
        case PNamedParameter(id, _) if pSet.contains(id.name) => takeParams
        case PExplicitGhostParameter(PNamedParameter(id, _)) if pSet.contains(id.name) => takeParams
        case _ => !takeParams
      }, isMemberGhost, context)
    }
  }

}
