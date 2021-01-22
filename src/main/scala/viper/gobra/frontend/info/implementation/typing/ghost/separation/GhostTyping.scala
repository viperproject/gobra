// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignModi}
import viper.gobra.util.Violation

trait GhostTyping extends GhostClassifier { this: TypeInfoImpl =>

  /** returns true iff member is classified as ghost */
  private[separation] lazy val ghostMemberClassification: PMember => Boolean =
    attr[PMember, Boolean] {
      case _: PGhostMember => true
      case m if enclosingGhostContext(m) => true
      case _ => false
    }

  /** returns true iff statement is classified as ghost */
  private[separation] lazy val ghostStmtClassification: PStatement => Boolean = {
    def varDeclClassification(left: Vector[PIdnNode], right: Vector[PExpression]): Boolean = {
      if (right.isEmpty) left.forall(ghostIdClassification)
      else StrictAssignModi(left.size, right.size) match {
        case AssignMode.Single =>
          left.map(Some(_)).zipAll(right.map(Some(_)), None, None).forall {
            case (Some(l), Some(r)) => ghostIdClassification(l) || ghostExprClassification(r)
            case (Some(l), _) => ghostIdClassification(l)
            case (_, Some(r)) => ghostExprClassification(r)
          }

        case AssignMode.Multi =>
          // right should be a singleton vector
          val isRightGhost = right.exists(ghostExprClassification)
          left.forall(l => isRightGhost || ghostIdClassification(l))

        case AssignMode.Error => Violation.violation("expected single or multi assignment mode")
      }


    }

    attr[PStatement, Boolean] {
      case _: PGhostStatement => true
      case s if enclosingGhostContext(s) => true
      case PAssignment(_, left) => left.forall(ghostExprClassification)
      case PAssignmentWithOp(_, _, left) => ghostExprClassification(left)
      case PShortVarDecl(right, left, _) => varDeclClassification(left, right)
      case PVarDecl(_, right, left, _) => varDeclClassification(left, right)
      case _ => false
    }
  }

  /** returns true iff expression is classified as ghost */
  private[separation] lazy val ghostExprClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostExprTyping(e).isGhost)

  /** returns ghost typing of expression */
  private[separation] lazy val ghostExprTyping: PExpression => GhostType = {
    import GhostType._

    createGhostTyping[PExpression]{
      case _: PGhostExpression => isGhost
      case e if exprType(e).isInstanceOf[Type.GhostType] => isGhost

      case PNamedOperand(id) => ghost(ghostIdClassification(id))

      case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
        case (Right(_), Some(_: ap.Conversion)) => notGhost // conversions cannot be ghost (for now)
        case (Left(callee), Some(_: ap.FunctionCall)) => calleeReturnGhostTyping(callee)
        case (Left(_), Some(_: ap.PredicateCall)) => isGhost
        case _ => Violation.violation("expected conversion, function call, or predicate call")
      }

      // ghostness of proof annotations is decided by the argument
      case ann: PActualExprProofAnnotation => ghost(!noGhostPropagationFromChildren(ann.op))

      // catches ghost field reads, method calls, function calls since their id is ghost
      case exp => ghost(!noGhostPropagationFromChildren(exp))
    }
  }

  /** returns true iff type is classified as ghost */
  private[separation] lazy val ghostTypeClassification: PType => Boolean = createGhostClassification[PType]{
    case _: PGhostType => true // TODO: This check seems insufficient to me in the long run. What if a type definition is ghost?
    case PArrayType(_, t) => isTypeGhost(t)
    case PSliceType(t) => isTypeGhost(t)
    case _ => false
  }

  /** returns true iff identifier is classified as ghost */
  private[separation] lazy val ghostIdClassification: PIdnNode => Boolean = createGhostClassification[PIdnNode]{
    id => entity(id) match {
      case r: Regular => r.ghost
      case _ => Violation.violation("expected Regular Entity")
    }
  }

  /** returns true iff parameter is classified as ghost */
  private[separation] lazy val ghostParameterClassification: PParameter => Boolean = createGhostClassification[PParameter] {
    case _: PActualParameter => false
    case _: PExplicitGhostParameter => true
  }

  /** returns true iff node is contained in ghost code */
  private[separation] def enclosingGhostContext(n: PNode): Boolean = isEnclosingExplicitGhost(n)

  /** returns true iff node does not contain ghost expression or id that is not contained in another statement */
  private[separation] lazy val noGhostPropagationFromChildren: PNode => Boolean =
    attr[PNode, Boolean] { node =>

      def noGhostPropagationFromSelfAndChildren(n: PNode): Boolean = n match {
        case i: PIdnNode => !ghostIdClassification(i)
        case e: PExpression => !ghostExprClassification(e)
        case _: PStatement => true
        case _: PParameter => true
        case _: PResult => true
        case x => noGhostPropagationFromChildren(x)
      }

      tree.child(node).forall(noGhostPropagationFromSelfAndChildren)
    }

  private[separation] def createGhostTyping[X <: PNode](typing: X => GhostType): X => GhostType =
    createWellDefInference[X, GhostType](selfWellDefined)(typing) // TODO: could use wellGhostSeparation as safety condition
      .andThen(_.getOrElse(Violation.violation("ghost typing on unsafe node")))

  private[separation] def createGhostClassification[X <: PNode](classification: X => Boolean): X => Boolean =
    createWellDefInference[X, Boolean](selfWellDefined)(classification)
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
    case _: PMethodSig => false
    case _: PInterfaceClause => assert(false); ???
  }

  override def expectedReturnGhostTyping(ret: PReturn): GhostType = {
    val res = enclosingCodeRootWithResult(ret).result
    GhostType.ghostTuple(res.outs.map(isParamGhost))
  }

  override def expectedArgGhostTyping(n: PInvoke): GhostType = {
    (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) => GhostType.notGhost
      case (Left(callee), Some(_: ap.FunctionCall)) => calleeArgGhostTyping(callee)
      case (Left(_), Some(_: ap.PredicateCall)) => GhostType.isGhost
      case p => Violation.violation(s"expected conversion, function call, or predicate call, but got $p")
    }
  }
}
