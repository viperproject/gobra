package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Function, MethodImpl, MethodSpec, Regular}
import viper.gobra.frontend.info.base.Type.GhostType
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation
import viper.gobra.util.Violation.violation

trait GhostSeparation extends BaseTyping { this: TypeInfoImpl =>

  def preCheck(n: PNode): Boolean = selfWellDefined(n) && children(n).forall(selfWellGhostSeparated)

  def selfWellGhostSeparated(n: PNode): Boolean = n match {
    case stmt: PStatement => wellDefGhostSeparated(stmt).valid
    case m: PNode => preCheck(m)
  }

  lazy val wellDefGhostSeparated: WellDefinedness[PNode] = createIndependentWellDef{
    case s: PStatement => stmtGhostSeparation(s)
    case e: PExpression => exprGhostSeparation(e)
  }{ preCheck }

  private def stmtGhostSeparation(stmt: PStatement): Messages = stmt match {
    case _: PGhostStatement => noMessages
    case s if enclosingGhostContext(s) => noMessages

    case _: PLabeledStmt
      |  _: PEmptyStmt
      |  _: PBlock
      |  _: PSeq
         => noMessages

    case n@ (
         _: PSendStmt
      |  _: PIfStmt
      |  _: PExprSwitchStmt
      |  _: PTypeSwitchStmt
      |  _: PForStmt
      |  _: PAssForRange
      |  _: PShortForRange
      |  _: PGoStmt
      |  _: PSelectStmt
      |  _: PBreak
      |  _: PContinue
      |  _: PGoto
      |  _: PDeferStmt
      ) => message(n, "ghost error: Found ghost child expression, but expected none", !noClassifiedGhostChildDataDependent(n))

    case n@ PAssignment(right, left) => assignableToAssignee(right: _*)(left: _*)
    case n@ PAssignmentWithOp(right, _, left) => assignableToAssignee(right)(left)

    case n@ PShortVarDecl(right, left) => assignableToId(right: _*)(left: _*)

    case n@ PReturn(right) => (enclosingCodeRoot(n) match {
      case f: PFunctionDecl  => f.result
      case f: PFunctionLit   => f.result
      case m: PMethodDecl    => m.result
    }) match {
      case PVoidResult() => violation("return arity not consistent with required enclosing arguments")
      case PResultClause(left) => assignableToParam(right: _*)(left: _*)
    }
  }

  private def exprGhostSeparation(expr: PExpression): Messages = expr match {
    case _: PGhostExpression => noMessages
    case e if enclosingGhostContext(e) => noMessages

    case _: PSelectionOrMethodExpr
      |  _: PSelection
      |  _: PMethodExpr
      |  _: PIndexedExp
      |  _: PSliceExp
      |  _: PTypeAssertion
      |  _: PNegation
      |  _: PBinaryExp
         => noMessages

    case n@ (
         _: PLiteral
      |  _: PReceive
      |  _: PReference
      |  _: PDereference
      |  _: PConversion
      ) => message(n, "ghost error: Found ghost child expression, but expected none", !noClassifiedGhostChildDataDependent(n))

    case n: PConversionOrUnaryCall => resolveConversionOrUnaryCall(n){
      case (base, id) => message(n, "ghost error: Found ghost child expression, but expected none", !noClassifiedGhostChildDataDependent(n))
    } {
      case (base, id) => assignableToCallId(base)(id)
    }.get

    case n@ PCall(callee, args) => assignableToCallExpr(args: _*)(callee)
  }

  private def assignableToCallExpr(right: PExpression*)(callee: PExpression): Messages = {
    val argTyping = calleeArgGhostTyping(callee).toTuple
    generalAssignableTo[PExpression, Boolean](ghostExprTyping){
      case (g, l) => message(callee, "ghost error: ghost cannot be assigned to non-ghost", g && !l)
    }(right: _*)(argTyping: _*)
  }

  private def assignableToCallId(right: PIdnNode)(callee: PExpression): Messages = {
    val argTyping = calleeArgGhostTyping(callee)
    message(right, "ghost error: ghost cannot be assigned to non-ghost", ghostIdClassification(right) && !argTyping.isGhost)
  }

  /** conservative ghost separation assignment check */
  private def assignableToAssignee(exprs: PExpression*)(lefts: PAssignee*): Messages =
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

    case PSelectionOrMethodExpr(base, id) =>
      message(left, "ghost error: ghost cannot be assigned to non-ghost field", isRightGhost && !ghostIdClassification(id)) ++
        message(left, "ghost error: cannot assign to non-ghost field of ghost reference", ghostIdClassification(base) && !ghostIdClassification(id))
  }

  /** conservative ghost separation assignment check */
  private def assignableToId(exprs: PExpression*)(lefts: PIdnNode*): Messages =
    generalAssignableTo(ghostExprTyping)(dfltAssignableMsg(ghostIdClassification))(exprs: _*)(lefts: _*)

  /** conservative ghost separation assignment check */
  private def assignableToParam(exprs: PExpression*)(lefts: PParameter*): Messages =
    generalAssignableTo(ghostExprTyping)(dfltAssignableMsg(ghostParameterClassification))(exprs: _*)(lefts: _*)

  private def dfltAssignableMsg[L <: PNode](ghost: L => Boolean): (Boolean, L) => Messages = {
    case (g, l) => message(l, "ghost error: ghost cannot be assigned to non-ghost", g && !ghost(l))
  }

  private def generalAssignableTo[R, L](typing: R => GhostTyping)(msg: (Boolean, L) => Messages)(rights: R*)(lefts: L*): Messages =
    assignModi(lefts.size, rights.size) match {

      case SingleAssign => rights.zip(lefts).flatMap{ case (r,l) => msg(typing(r).isGhost, l) }.toVector

      case MultiAssign =>
        val gt = typing(rights.head)
        lefts.zipWithIndex.flatMap{ case (l, idx) => msg(gt.isIdxGhost(idx), l) }.toVector

      case ErrorAssign => Violation.violation("assignment mismatch")
    }



  /** returns true iff node is contained in ghost code */
  private[separation] def enclosingGhostContext(n: PNode): Boolean = isEnclosingExplicitGhost(n)

  /** returns true iff node does not contain ghost expression or id that is not contained in another statement */
  private[separation] lazy val noClassifiedGhostChildDataDependent: PNode => Boolean =
    attr[PNode, Boolean] { node =>

      def selfAndChildNotGhostDataDependent(n: PNode): Boolean = n match {
        case i: PIdnNode => !ghostIdClassification(i)
        case e: PExpression => !ghostExprClassification(e)
        case _: PStatement => true
        case _: PParameter => true
        case _: PResult => true
        case x => noClassifiedGhostChildDataDependent(x)
      }

      tree.child(node).forall(selfAndChildNotGhostDataDependent)
    }

  private[separation] lazy val ghostStmtClassification: PStatement => Boolean =
    attr[PStatement, Boolean] {
      case _: PGhostStatement => true
      case s if enclosingGhostContext(s) => true
      case PAssignment(_, left) => left.forall(ghostAssigneeClassification)
      case PAssignmentWithOp(_, _, left) => ghostAssigneeClassification(left)
      case PShortVarDecl(_, left) => left.forall(ghostIdClassification)
      case _ => false
    }

  private[separation] lazy val ghostAssigneeClassification: PAssignee => Boolean =
    attr[PAssignee, Boolean] {
      case PNamedOperand(id) => ghostIdClassification(id)
      case PSelection(base, id) => ghostIdClassification(id) || ghostExprClassification(base)
      case PSelectionOrMethodExpr(base, id) => ghostIdClassification(id) || ghostIdClassification(base)
      case _: PIndexedExp => false
      case _: PDereference => false
    }

  sealed trait GhostTyping {
    def isGhost: Boolean
    def isIdxGhost(idx: Int): Boolean
    def length: Int
    def toTuple: Vector[Boolean]
  }

  object GhostTyping {
    def isGhost: GhostTyping = TupleGhostTyping(Vector(true))
    def notGhost: GhostTyping = TupleGhostTyping(Vector.empty)
    def ghost(bool: Boolean): GhostTyping = if (bool) isGhost else notGhost
    def ghostTuple(ghostTyping: Vector[Boolean]): GhostTyping = TupleGhostTyping(ghostTyping)
  }

  case class TupleGhostTyping(ghostTyping: Vector[Boolean]) extends GhostTyping {
    override lazy val length: Int = ghostTyping.length
    override def toTuple: Vector[Boolean] = ghostTyping
    override lazy val isGhost: Boolean = ghostTyping.nonEmpty && ghostTyping.exists(identity)
    override def isIdxGhost(idx: Int): Boolean = ghostTyping.isDefinedAt(idx) && ghostTyping(idx)
  }

  /** returns true iff expression is classified as ghost */
  private[separation] def ghostExprClassification(expr: PExpression): Boolean = ghostExprTyping(expr).isGhost

  /** returns true iff expression is classified as ghost */
  private[separation] lazy val ghostExprTyping: PExpression => GhostTyping = {
    import GhostTyping._

    attr[PExpression, GhostTyping] {

      case _: PGhostExpression => isGhost
      case e if exprType(e).isInstanceOf[GhostType] => isGhost

      case PNamedOperand(id) => ghost(ghostIdClassification(id))

      case PCall(callee, _) => calleeReturnGhostTyping(callee)

      case n: PConversionOrUnaryCall => resolveConversionOrUnaryCall(n) {
        case (base, id) => notGhost // conversions cannot be ghost (for now)
      } {
        case (base, id) => calleeReturnGhostTyping(base)
      }.get

        // catches ghost field reads, method calls, function calls since their id is ghost
      case exp => ghost(tree.child(exp).collect{ case x: PExpression => x }.exists(ghostExprClassification))
    }
  }

  private[separation] def calleeArgGhostTyping(callee: PExpression): GhostTyping = calleeEntity(callee) match {
    case None => GhostTyping.notGhost // conservative choice
    case Some(e) =>
      if (isCalleeMethodExpr(callee))
        TupleGhostTyping(false +: calleeArgGhostTyping(e).toTuple)
      else
        calleeArgGhostTyping(e)
  }

  private[separation] def calleeArgGhostTyping(callee: PIdnNode): GhostTyping =
    calleeArgGhostTyping(regular(callee))

  private[separation] def calleeReturnGhostTyping(callee: PExpression): GhostTyping = calleeEntity(callee) match {
    case None => GhostTyping.isGhost // conservative choice
    case Some(e) => calleeReturnGhostTyping(e)
  }

  private[separation] def calleeReturnGhostTyping(callee: PIdnNode): GhostTyping =
    calleeReturnGhostTyping(regular(callee))

  private def calleeEntity(callee: PExpression): Option[Entity] = callee match {
    case PNamedOperand(id)     => Some(regular(id))
    case PMethodExpr(base, id) => Some(findSelection(typeType(base), id).get)
    case PSelection(base, id)  => Some(findSelection(exprType(base), id).get)
    case n: PSelectionOrMethodExpr => resolveSelectionOrMethodExpr(n){
      case (base, id) => findSelection(idType(base), id).get // selection
    } {
      case (base, id) => findSelection(idType(base), id).get // methodExpr
    }
    case _ => None
  }

  private def isCalleeMethodExpr(callee: PExpression): Boolean = callee match {
    case PMethodExpr(base, id) => true
    case n: PSelectionOrMethodExpr => resolveSelectionOrMethodExpr(n){
      case (base, id) => false // selection
    } {
      case (base, id) => true // methodExpr
    }.get
    case _ => false
  }

  private[separation] def calleeArgGhostTyping(r: Entity): GhostTyping = {
    def argTyping(args: Vector[PParameter]): GhostTyping =
      GhostTyping.ghostTuple(args.map(ghostParameterClassification))

    r match {
      case Function(decl, _)   => argTyping(decl.args)
      case MethodImpl(decl, _) => argTyping(decl.args)
      case MethodSpec(spec, _) => argTyping(spec.args)
      case x => Violation.violation(s"expected callable but got $x")
    }
  }

  private[separation] def calleeReturnGhostTyping(r: Entity): GhostTyping = {
    def resultTyping(result: PResult): GhostTyping = result match {
      case PVoidResult() => GhostTyping.notGhost
      case PResultClause(outs) => GhostTyping.ghostTuple(outs.map(ghostParameterClassification))
    }

    r match {
      case x: Regular if x.isGhost => GhostTyping.isGhost
      case Function(decl, _)   => resultTyping(decl.result)
      case MethodImpl(decl, _) => resultTyping(decl.result)
      case MethodSpec(spec, _) => resultTyping(spec.result)
      case x => Violation.violation(s"expected callable but got $x")
    }
  }


  private[separation] def ghostParameterClassification(param: PParameter): Boolean = param match {
    case _: PActualParameter        => false
    case _: PExplicitGhostParameter => true
  }


  /** returns true iff identifier is classified as ghost */
  private[separation] def ghostIdClassification(id: PIdnNode): Boolean = entity(id) match {
    case r: Regular => r.isGhost
    case _ => false
  }
}
