// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Closure, MultiLocalVariable, NamedType, Regular, SingleLocalVariable, TypeAlias}
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.util.Violation

trait GhostTyping extends GhostClassifier { this: TypeInfoImpl =>

  /** returns true iff member is classified as ghost */
  private[separation] lazy val ghostMemberClassification: PMember => Boolean =
    attr[PMember, Boolean] {
      case _: PGhostMember => true
      case m if isEnclosingGhost(m) => true
      case _ => false
    }

  /** returns true iff statement is classified as ghost */
  private[separation] lazy val ghostStmtClassification: PStatement => Boolean = {
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
      case c: PCritical => c.stmts.forall(ghostStmtClassification)
      case s if isEnclosingGhost(s) => true
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
  private[separation] lazy val ghostExprClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostExprTyping(e).isGhost)

  /**
    * returns ghost typing of expression, i.e. whether ghost erasure should remove an expression.
    * note the difference to ghostExprResultTyping in particular for function calls:
    * `ghostExprTyping` returns the ghost typing of the callee. On the other hand, `ghostExprResultTyping` returns the
    * ghost typing of the callee's results
    */
  private[separation] lazy val ghostExprTyping: PExpression => GhostType = {
    import GhostType._

    createGhostTyping[PExpression]{
      case _: PGhostExpression => isGhost
      case e if exprType(e).isInstanceOf[Type.GhostType] => isGhost

      case PNamedOperand(id) => ghost(ghostIdClassification(id))

      case _: PFunctionLit => notGhost
      case e: PCompositeLit if isEnclosingGhost(e) => isGhost // struct literals occurring in ghost code are treated as ghost no matter whether the type is ghost or not

      case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
        case (Right(_), Some(_: ap.Conversion)) => notGhost // conversions cannot be ghost (for now)
        case (Left(_), Some(call: ap.FunctionCall)) =>
          if (tryEnclosingClosureImplementationProof(n).nonEmpty) notGhost
          else calleeGhostTyping(call)
        case (Left(_), Some(_: ap.ClosureCall)) =>
          if (tryEnclosingClosureImplementationProof(n).nonEmpty) notGhost
          else ghostExprTyping(n.base.asInstanceOf[PExpression])
        case (Left(_), Some(_: ap.PredicateCall)) => isGhost
        case _ => Violation.violation("expected conversion, function call, or predicate call")
      }

      // catches ghost field reads, method calls, function calls since their id is ghost
      case exp => ghost(!noGhostPropagationFromChildren(exp))
    }
  }

  /** returns true iff expression is classified as ghost */
  private[separation] lazy val ghostExprResultClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostExprResultTyping(e).isGhost)

  /** returns ghost typing of expression */
  private[separation] lazy val ghostExprResultTyping: PExpression => GhostType = {
    import GhostType._

    createGhostTyping[PExpression]{
      case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
        case (Right(_), Some(_: ap.Conversion)) => notGhost // conversions cannot be ghost (for now)
        case (Left(_), Some(call: ap.FunctionCall)) => calleeReturnGhostTyping(call)
        case (Left(_), Some(_: ap.ClosureCall)) => closureCallReturnGhostTyping(n)
        case (Left(_), Some(_: ap.PredicateCall)) => isGhost
        case _ => Violation.violation("expected conversion, function call, or predicate call")
      }

      case e => ghostExprTyping(e)
    }
  }

  /** returns true iff expression refers to a ghost location (ghost heap or local ghost variable) */
  private[separation] lazy val ghostLocationClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostLocationTyping(e).isGhost)

  /** returns ghost typing of the location that expression represents */
  private[separation] lazy val ghostLocationTyping: PExpression => GhostType = {
    import GhostType._

    createGhostTyping[PExpression] {
      case PNamedOperand(id) => ghost(ghostIdClassification(id))
      case e: PDeref => resolve(e) match {
        case Some(ap.Deref(base)) => underlyingType(exprType(base)) match {
          case _: Type.GhostPointerT => isGhost
          case _ => notGhost
        }
        case _ => Violation.violation(s"type-checker should only permit PDeref assignees that have the Deref pattern")
      }
      case e: PDot => resolve(e) match {
        case Some(s: ap.FieldSelection) =>
          val isGhostField = ghostIdClassification(s.id)
          val isGhostEmbedding = s.path.exists {
            case MemberPath.Next(decl) => decl.ghost
            case _ => false
          }
          (underlyingType(typ(s.base)), isGhostField || isGhostEmbedding) match {
            case (_, true) => isGhost // ghost fields and ghost embeddings are always ghost memory
            case (tb: Type.PointerT, _) => ghost(tb.isInstanceOf[Type.GhostPointerT]) // (implicitly) dereferencing a field of a ghost pointer leads to a ghost heap location
            case _ => ghostLocationTyping(s.base) // assignee is on the stack, recurse to find out if it's a ghost or actual variable
          }
        case Some(g: ap.GlobalVariable) => ghost(g.symb.ghost)
        case _ => Violation.violation(s"type-checker should only permit PDot assignees that are either field or global variable accesses")
      }
      case PIndexedExp(base, _) => underlyingType(typ(base)) match {
        case t if !isPointerType(t) => ghostLocationTyping(base) // assignee is on the stack, recurse to find out if it's a ghost or actual variable
        case _: Type.GhostPointerT | _: Type.GhostSliceT => isGhost // (implicitly) dereferencing a ghost pointer leads to a ghost heap location
        case _ => notGhost
      }

      case e => ghostExprTyping(e)
    }
  }

  /** returns true iff type is classified as ghost */
  private[separation] lazy val ghostTypeClassification: PType => Boolean = createGhostClassification[PType] {
    case _: PGhostType => true
    case _: PBoolType | _: PIntegerType | _: PFloatType | _: PStringType | _: PFunctionType | _: PInterfaceType => false
    case PArrayType(_, elem) => isTypeGhost(elem)
    case PSliceType(elem) => isTypeGhost(elem)
    case PMapType(key, elem) => isTypeGhost(key) || isTypeGhost(elem)
    case t: PChannelType => isTypeGhost(t.elem)
    case t: PMethodRecvType => isTypeGhost(t.typ)
    case t: PTypeDecl => entity(t.left) match {
      case nt: NamedType => nt.ghost
      case at: TypeAlias => at.ghost
      case _ => false
    }
    case _: PStructType => false // `PExplicitGhostStructType` is already captured by the `PGhostType` case above
    case PVariadicType(elem) => isTypeGhost(elem)
    case t @ (_: PNamedOperand | _: PDeref | _: PDot) => resolve(t) match {
      case Some(tp: ap.Type) => tp match {
        case sp: ap.Symbolic => sp.symb.ghost
        case ap.PointerType(base) => isTypeGhost(base)
      }
      case _ => false
    }
  }

  /** returns true iff identifier is classified as ghost */
  private[separation] lazy val ghostIdClassification: PIdnNode => Boolean = createGhostClassification[PIdnNode]{
    id => {
      val ent = entity(id)
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
  private[separation] lazy val ghostParameterClassification: PParameter => Boolean = createGhostClassification[PParameter] {
    case _: PActualParameter => false
    case _: PExplicitGhostParameter => true
  }

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

      val childrenToVisit: Iterable[PNode] = node match {
        case p: PProofAnnotation => p.nonGhostChildren
        case _ => tree.child(node)
      }

      childrenToVisit.forall(noGhostPropagationFromSelfAndChildren)
    }

  private[separation] def createGhostTyping[X <: PNode](typing: X => GhostType): X => GhostType =
    createWellDefInference[X, GhostType](wellGhostSeparated.valid)(typing)
      .andThen(_.getOrElse(Violation.violation("ghost typing on unsafe node")))

  private[separation] def createGhostClassification[X <: PNode](classification: X => Boolean): X => Boolean =
    createWellDefInference[X, Boolean](wellGhostSeparated.valid)(classification)
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

  override def isGhostLocation(expr: PExpression): Boolean =
    ghostLocationClassification(expr)

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
    val res = enclosingCodeRootWithResult(ret).result
    GhostType.ghostTuple(res.outs.map(isParamGhost))
  }

  override def expectedArgGhostTyping(n: PInvoke): GhostType = {
    (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) => GhostType.notGhost
      case (Left(_), Some(call: ap.FunctionCall)) => calleeArgGhostTyping(call)
      case (Left(_), Some(call: ap.ClosureCall)) => expectedArgGhostTyping(call.maybeSpec.get)
      case (Left(_), Some(_: ap.PredicateCall)) => GhostType.isGhost
      case p => Violation.violation(s"expected conversion, function call, or predicate call, but got $p")
    }
  }

  override def expectedArgGhostTyping(spec: PClosureSpecInstance): GhostType = closureSpecArgsAndResGhostTyping(spec)._1

  override def isExprPure(expr: PExpression): Boolean = isPureExpr(expr).isEmpty
}
