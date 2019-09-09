package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.implementation.TypeInfoImpl
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
  private[separation] lazy val ghostStmtClassification: PStatement => Boolean =
    attr[PStatement, Boolean] {
      case _: PGhostStatement => true
      case s if enclosingGhostContext(s) => true
      case PAssignment(_, left) => left.forall(ghostExprClassification)
      case PAssignmentWithOp(_, _, left) => ghostExprClassification(left)
      case PShortVarDecl(_, left) => left.forall(ghostIdClassification)
      case _ => false
    }

  /** returns true iff expression is classified as ghost */
  private[separation] lazy val ghostExprClassification: PExpression => Boolean =
    createGhostClassification[PExpression](e => ghostExprTyping(e).isGhost)

  /** returns ghost typing of expression */
  private[separation] lazy val ghostExprTyping: PExpression => GhostType = {
    import GhostType._

    createGhostTyping[PExpression]{
      case _: PGhostExpression => isGhost
      case e if exprType(e).isInstanceOf[GhostType] => isGhost

      case PNamedOperand(id) => ghost(ghostIdClassification(id))

      case PCall(callee, _) => calleeReturnGhostTyping(callee)

      case n: PConversionOrUnaryCall => resolveConversionOrUnaryCall(n) {
        case (base, id) => notGhost // conversions cannot be ghost (for now)
      } {
        case (base, id) => calleeReturnGhostTyping(base)
      }.get

        // ghostness of proof annotations is decided by the argument
      case ann: PActualExprProofAnnotation => ghost(!noGhostPropagationFromChildren(ann.op))

      // catches ghost field reads, method calls, function calls since their id is ghost
      case exp => ghost(!noGhostPropagationFromChildren(exp))
    }
  }

  /** returns true iff type is classified as ghost */
  private[separation] lazy val ghostTypeClassification: PType => Boolean = createGhostClassification[PType]{
    case _: PGhostType => true
    case _ => false
  }

  /** returns true iff identifier is classified as ghost */
  private[separation] lazy val ghostIdClassification: PIdnNode => Boolean = createGhostClassification[PIdnNode]{
    id => entity(id) match {
      case r: Regular => r.isGhost
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
  }

  override def expectedReturnGhostTyping(ret: PReturn): GhostType = enclosingCodeRootWithResult(ret).result match {
    case PVoidResult() => GhostType.ghostTuple(Vector.empty)
    case PResultClause(left) => GhostType.ghostTuple(left.map(isParamGhost))
  }

  override def expectedArgGhostTyping(call: PCall): GhostType =
    calleeArgGhostTyping(call.callee)

  override def expectedArgGhostTyping(call: PConversionOrUnaryCall): GhostType =
    resolveConversionOrUnaryCall(call) {
      case (base, id) => GhostType.notGhost
    } {
      case (base, id) => calleeArgGhostTyping(base)
    }.get

}
