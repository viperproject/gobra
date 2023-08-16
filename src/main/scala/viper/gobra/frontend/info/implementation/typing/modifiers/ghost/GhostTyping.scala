// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.modifiers.ghost

import viper.gobra.ast.frontend.{AstPattern => ap, _}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.base.SymbolTable.{Closure, MultiLocalVariable, Regular, SingleLocalVariable}
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.frontend.info.implementation.typing.modifiers.ghost.GhostType.ghost
import viper.gobra.util.Violation

trait GhostTyping extends GhostClassifier { this: GhostModifierUnit =>

  /** returns true iff member is classified as ghost */
  lazy val ghostMemberClassification: PMember => Boolean =
    attr[PMember, Boolean] {
      case _: PGhostMember => true
      case m if enclosingGhostContext(m) => true
      case _ => false
    }

  /** returns true iff statement is classified as ghost */
  lazy val ghostStmtClassification: PStatement => Boolean = {
    def varDeclClassification(left: Vector[PIdnNode], right: Vector[PExpression]): Boolean =
      StrictAssignMode(left.size, right.size) match {
        case AssignMode.Single =>
          left.map(Some(_)).zipAll(right.map(Some(_)), None, None).forall {
            case (l, r) => l.exists(ghostIdClassification) || r.exists(ghostExprResultClassification)
          }

        case AssignMode.Multi =>
          // right should be a singleton vector
          val isRightResultGhost = right.exists(ghostExprResultClassification)
          left.forall(l => isRightResultGhost || ghostIdClassification(l))

        case AssignMode.Error if right.isEmpty => left.forall(ghostIdClassification)

        case AssignMode.Error | AssignMode.Variadic => Violation.violation("expected single or multi assignment mode")
      }

    attr[PStatement, Boolean] {
      case _: PGhostStatement => true
      case s if enclosingGhostContext(s) => true
      case PAssignment(_, left) => left.forall(ghostExprClassification)
      case PAssignmentWithOp(_, _, left) => ghostExprClassification(left)
      case PShortVarDecl(right, left, _) => varDeclClassification(left, right)
      case PVarDecl(_, right, left, _) => varDeclClassification(left, right)
      case PExpressionStmt(exp) => ghostExprClassification(exp)
      case _ => false
    }
  }

  /**
    * returns true iff expression is classified as ghost, i.e. whether ghost erasure should remove an expression.
    * note the difference to ghostExprResultClassification in particular for function calls:
    * `ghostExprClassification` returns true iff callee is ghost. On the other hand, `ghostExprResultClassification` returns true iff the
    * callee's result is ghost.
    **/
  lazy val ghostExprClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostExprTyping(e).isGhost)

  /**
    * returns ghost typing of expression, i.e. whether ghost erasure should remove an expression.
    * note the difference to ghostExprResultTyping in particular for function calls:
    * `ghostExprTyping` returns the ghost typing of the callee. On the other hand, `ghostExprResultTyping` returns the
    * ghost typing of the callee's results
    */
  lazy val ghostExprTyping: PExpression => GhostType = {

    createGhostTyping[PExpression]{
      case _: PGhostExpression => GhostType.isGhost
      case e if ctx.exprType(e).isInstanceOf[Type.GhostType] => GhostType.isGhost

      case PNamedOperand(id) => GhostType.ghost(ghostIdClassification(id))

      case _: PFunctionLit => GhostType.notGhost

      case n: PInvoke => (ctx.exprOrType(n.base), ctx.resolve(n)) match {
        case (Right(_), Some(_: ap.Conversion)) => GhostType.notGhost // conversions cannot be ghost (for now)
        case (Left(_), Some(call: ap.FunctionCall)) =>
          if (ctx.tryEnclosingClosureImplementationProof(n).nonEmpty) GhostType.notGhost
          else calleeGhostTyping(call)
        case (Left(_), Some(_: ap.ClosureCall)) =>
          if (ctx.tryEnclosingClosureImplementationProof(n).nonEmpty) GhostType.notGhost
          else ghostExprTyping(n.base.asInstanceOf[PExpression])
        case (Left(_), Some(_: ap.PredicateCall)) => GhostType.isGhost
        case _ => Violation.violation("expected conversion, function call, or predicate call")
      }

      // catches ghost field reads, method calls, function calls since their id is ghost
      case exp => GhostType.ghost(!noGhostPropagationFromChildren(exp))
    }
  }

  /** returns true iff expression is classified as ghost */
  lazy val ghostExprResultClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostExprResultTyping(e).isGhost)

  /** returns ghost typing of expression */
  lazy val ghostExprResultTyping: PExpression => GhostType = {

    createGhostTyping[PExpression]{
      case n: PInvoke => (ctx.exprOrType(n.base), ctx.resolve(n)) match {
        case (Right(_), Some(_: ap.Conversion)) => GhostType.notGhost // conversions cannot be ghost (for now)
        case (Left(_), Some(call: ap.FunctionCall)) => calleeReturnGhostTyping(call)
        case (Left(_), Some(_: ap.ClosureCall)) => closureCallReturnGhostTyping(n)
        case (Left(_), Some(_: ap.PredicateCall)) => GhostType.isGhost
        case _ => Violation.violation("expected conversion, function call, or predicate call")
      }

      case e => ghostExprTyping(e)
    }
  }

  /** returns true iff type is classified as ghost */
  lazy val ghostTypeClassification: PType => Boolean = createGhostClassification[PType]{
    case _: PGhostType => true // TODO: This check seems insufficient to me in the long run. What if a type definition is ghost?
    case PArrayType(_, t) => isTypeGhost(t)
    case PSliceType(t) => isTypeGhost(t)
    case _ => false
  }

  /** returns true iff identifier is classified as ghost */
  lazy val ghostIdClassification: PIdnNode => Boolean = createGhostClassification[PIdnNode]{
    id => {
      val ent = ctx.entity(id)
      ent match {
        case r: SingleLocalVariable => r.ghost || r.exp.exists(ghostExprResultClassification)
        case r: MultiLocalVariable => r.ghost || ghostExprResultTyping(r.exp).isIdxGhost(r.idx)
        case _: Closure => true
        case r: Regular => r.ghost
        case _ => Violation.violation("expected Regular Entity")
      }
    }
  }

  /** returns true iff parameter is classified as ghost */
  lazy val ghostParameterClassification: PParameter => Boolean = createGhostClassification[PParameter] {
    case _: PActualParameter => false
    case _: PExplicitGhostParameter => true
  }

  /** returns true iff node is contained in ghost code */
  def enclosingGhostContext(n: PNode): Boolean =
    ctx.isEnclosingExplicitGhost(n) || ctx.isEnclosingDomain(n)

  /** returns true iff node does not contain ghost expression or id that is not contained in another statement */
  lazy val noGhostPropagationFromChildren: PNode => Boolean =
    attr[PNode, Boolean] { node =>

      def noGhostPropagationFromSelfAndChildren(n: PNode): Boolean = n match {
        case i: PIdnNode => !ghostIdClassification(i)
        case e: PExpression => !ghostExprClassification(e)
        case _: PStatement => true
        case _: PParameter => true
        case _: PResult => true
        case x => noGhostPropagationFromChildren(x)
      }

      val childrenToVisit: Iterable[PNode] = node match {
        case p: PProofAnnotation => p.nonGhostChildren
        case _ => ctx.tree.child(node)
      }

      childrenToVisit.forall(noGhostPropagationFromSelfAndChildren)
    }

  def createGhostTyping[X <: PNode](typing: X => GhostType): X => GhostType =
    createWellDefInference[X, GhostType](hasWellDefModifier.valid)(typing)
      .andThen(_.getOrElse(Violation.violation("ghost typing on unsafe node")))

  def createGhostClassification[X <: PNode](classification: X => Boolean): X => Boolean =
    createWellDefInference[X, Boolean](hasWellDefModifier.valid)(classification)
      .andThen(_.getOrElse(Violation.violation("ghost classification on unsafe node")))



  // GhostClassifier interface

  override def isMemberGhost(member: PMember): Boolean =
    ghostMemberClassification(member)

  override def isStmtGhost(stmt: PStatement): Boolean =
    ghostStmtClassification(stmt)

  override def exprGhostTyping(expr: PExpression): GhostType =
    ghostExprTyping(expr)

  override def isTypeGhost(typ: PType): Boolean =
    ghostTypeClassification(typ)

  override def isIdGhost(id: PIdnNode): Boolean =
    ghostIdClassification(id)

  override def isParamGhost(param: PParameter): Boolean =
    ghostParameterClassification(param)

  override def isStructClauseGhost(clause: PStructClause): Boolean = clause match {
    case _: PActualStructClause => false
    case _: PExplicitGhostStructClause => true
  }

  override def isInterfaceClauseGhost(clause: PInterfaceClause): Boolean = clause match {
    case _: PInterfaceName => false
    case m: PMethodSig => m.isGhost
    case _: PMPredicateSig => true
  }

  override def expectedReturnGhostTyping(ret: PReturn): GhostType = {
    val res = ctx.enclosingCodeRootWithResult(ret).result
    GhostType.ghostTuple(res.outs.map(isParamGhost))
  }

  override def expectedArgGhostTyping(n: PInvoke): GhostType = {
    (ctx.exprOrType(n.base), ctx.resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) => GhostType.notGhost
      case (Left(_), Some(call: ap.FunctionCall)) => calleeArgGhostTyping(call)
      case (Left(_), Some(call: ap.ClosureCall)) => expectedArgGhostTyping(call.maybeSpec.get)
      case (Left(_), Some(_: ap.PredicateCall)) => GhostType.isGhost
      case p => Violation.violation(s"expected conversion, function call, or predicate call, but got $p")
    }
  }

  override def expectedArgGhostTyping(spec: PClosureSpecInstance): GhostType = closureSpecArgsAndResGhostTyping(spec)._1

  override def isExprPure(expr: PExpression): Boolean = ctx.isPureExpr(expr).isEmpty

  /* ghost type of the callee itself (not considering its arguments or results) */
  private[ghost] def calleeGhostTyping(call: ap.FunctionCall): GhostType = call.callee match {
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

  /** ghost type of the arguments of a callee */
  private[ghost] def calleeArgGhostTyping(call: ap.FunctionCall): GhostType = {
    // a parameter of a ghost member is ghost (even if such a explicit declaration is missing)
    def argTyping(args: Vector[PParameter], isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(args.map(p => isMemberGhost || context.getGhostModifier(p) == GhostModifier.Ghost))

    call.callee match {
      case p: ap.Function => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.Closure => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.ReceivedMethod => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case p: ap.MethodExpr => GhostType.ghostTuple(false +: argTyping(p.symb.args, p.symb.ghost, p.symb.context).toTuple)
      case _: ap.PredicateKind => GhostType.isGhost
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => ctx.argGhostTyping(symb.tag, call.args.map(ctx.typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => ctx.argGhostTyping(symb.tag, Vector(ctx.typ(recv)))
      case ap.BuiltInMethodExpr(typ, _, _, symb) => GhostType.ghostTuple(false +: ctx.argGhostTyping(symb.tag, Vector(ctx.typeSymbType(typ))).toTuple)
      case p: ap.ImplicitlyReceivedInterfaceMethod => argTyping(p.symb.args, p.symb.ghost, p.symb.context)
      case _ => GhostType.notGhost // conservative choice
    }
  }

  /** ghost type of the result of a callee */
  private[ghost] def calleeReturnGhostTyping(call: ap.FunctionCall): GhostType = {
    // a result of a ghost member is ghost (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, isMemberGhost: Boolean, context: ExternalTypeInfo): GhostType = {
      GhostType.ghostTuple(result.outs.map(p => isMemberGhost || context.getGhostModifier(p) == GhostModifier.Ghost))
    }

    call.callee match {
      case p: ap.Function => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case p: ap.ReceivedMethod => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case p: ap.MethodExpr => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case _: ap.PredicateKind => GhostType.isGhost
      case _: ap.DomainFunction => GhostType.isGhost
      case ap.BuiltInFunction(_, symb) => ctx.returnGhostTyping(symb.tag, call.args.map(ctx.typ))
      case ap.BuiltInReceivedMethod(recv, _, _, symb) => ctx.returnGhostTyping(symb.tag, Vector(ctx.typ(recv)))
      case ap.BuiltInMethodExpr(typ, _, _, symb) => ctx.returnGhostTyping(symb.tag, Vector(ctx.typeSymbType(typ)))
      case p: ap.ImplicitlyReceivedInterfaceMethod => resultTyping(p.symb.result, p.symb.ghost, p.symb.context)
      case _ => GhostType.isGhost // conservative choice
    }
  }

  /** ghost types of the call arguments and results of a closure spec instance.
    * The ghost type depends on that of the corresponding argument in the base function,
    * not on the ghostness of the function. */
  private[ghost] def closureSpecArgsAndResGhostTyping(spec: PClosureSpecInstance): (GhostType, GhostType) = {
    def paramTyping(params: Vector[PParameter], context: ExternalTypeInfo): GhostType =
      GhostType.ghostTuple(params.map(p => context.getGhostModifier(p) == GhostModifier.Ghost))

    val (fArgs, fRes, context) = ctx.resolve(spec.func) match {
      case Some(ap.Function(_, f)) => (f.args, f.result.outs, f.context)
      case Some(ap.Closure(_, c)) => (c.args, c.result.outs, c.context)
      case _ => Violation.violation("this case should be unreachable")
    }

    val argTyping = if (spec.params.forall(_.key.isEmpty))
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

  private[ghost] def closureCallReturnGhostTyping(call: PInvoke): GhostType = {
    // a result is ghost if the closure is ghost (even if such a explicit declaration is missing)
    def resultTyping(result: PResult, isClosureGhost: Boolean, context: ExternalTypeInfo): GhostType = {
      GhostType.ghostTuple(result.outs.map(p => isClosureGhost || context.getGhostModifier(p) == GhostModifier.Ghost))
    }

    ctx.resolve(call.spec.get.func) match {
      case Some(p: ap.Function) => resultTyping(p.symb.result, isExprGhost(call.base.asInstanceOf[PExpression]), p.symb.context)
      case Some(p: ap.Closure) => resultTyping(p.symb.result, isExprGhost(call.base.asInstanceOf[PExpression]), p.symb.context)
      case _ => GhostType.isGhost // conservative choice
    }
  }
}
