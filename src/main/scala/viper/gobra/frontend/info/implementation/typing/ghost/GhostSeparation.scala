package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation._

trait GhostSeparation extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefStmtGhostSeparated: WellDefinedness[PStatement] = createIndependentWellDef{
    case s: PStatement => stmtGhostSeparation(s)
  }{ childrenWellDefined }

  private def stmtGhostSeparation(stmt: PStatement): Messages = stmt match {
    case _: PGhostStatement => noMessages
    case s if stmtGhostContext(s) => noMessages

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

    case n@ PAssignment(right, left) => left.zip(right).flatMap{ case (l, r) => assignableTo(r, l) }
    case n@ PAssignmentWithOp(right, _, left) => assignableTo(right, left)

    case n@ PShortVarDecl(right, left) => left.zip(right).flatMap{ case (l, r) => assignableTo(r, l) }

    case n@ PReturn(right) => (enclosingCodeRoot(n) match {
      case f: PFunctionDecl  => f.result
      case f: PFunctionLit   => f.result
      case m: PMethodDecl    => m.result
    }) match {
      case PVoidResult() => violation("return arity not consistent with required enclosing arguments")
      case PResultClause(left) => left.zip(right).flatMap{ case (l, r) => assignableTo(r, l) }
    }
  }

  /** conservative ghost separation assignment check */
  private def assignableTo(expr: PExpression, left: PAssignee): Messages = left match {

    case PDereference(op) => // *x := e ~ !ghost(e)
      message(left, "ghost error: ghost cannot be assigned to pointer", classifiedGhostExpr(expr))

    case PIndexedExp(base, index) => // a[i] := e ~ !ghost(i) && !ghost(e)
      message(left, "ghost error: ghost cannot be assigned to index expressions", classifiedGhostExpr(expr)) ++
        message(left, "ghost error: ghost index are not permitted in index expressions", classifiedGhostExpr(index))

    case PNamedOperand(id) => // x := e ~ ghost(e) ==> ghost(x)
      message(left, "ghost error: ghost cannot be assigned to non-ghost", classifiedGhostExpr(expr) && !classifiedGhostId(id))

    case PSelection(base, id) => // x.f := e ~ ghost(x) || ghost(e) ==> ghost(f)
      message(left, "ghost error: ghost cannot be assigned to non-ghost field", classifiedGhostExpr(expr) && !classifiedGhostId(id)) ++
        message(left, "ghost error: cannot assign to non-ghost field of ghost reference", classifiedGhostExpr(base) && !classifiedGhostId(id))

    case PSelectionOrMethodExpr(base, id) =>
      message(left, "ghost error: ghost cannot be assigned to non-ghost field", classifiedGhostExpr(expr) && !classifiedGhostId(id)) ++
        message(left, "ghost error: cannot assign to non-ghost field of ghost reference", classifiedGhostId(base) && !classifiedGhostId(id))
  }

  /** conservative ghost separation assignment check */
  private def assignableTo(expr: PExpression, left: PIdnNode): Messages =
    message(left, "ghost error: ghost cannot be assigned to non-ghost", classifiedGhostExpr(expr) && !classifiedGhostId(left))

  /** conservative ghost separation assignment check */
  private def assignableTo(expr: PExpression, left: PParameter): Messages =
    message(left, "ghost error: ghost cannot be assigned to non-ghost", classifiedGhostExpr(expr) && !isEnclosingExplicitGhost(left))



  /** returns true iff stmt is contained in ghost code */
  private def stmtGhostContext(stmt: PStatement): Boolean = isEnclosingExplicitGhost(stmt)

  /** returns true iff node does not contain ghost expression or id that is not contained in another statement */
  private lazy val noClassifiedGhostChildDataDependent: PNode => Boolean =
    attr[PNode, Boolean] { node =>

      def selfAndChildNotGhostDataDependent(n: PNode): Boolean = n match {
        case i: PIdnNode => !classifiedGhostId(i)
        case e: PExpression => !classifiedGhostExpr(e)
        case _: PStatement => true
        case x => noClassifiedGhostChildDataDependent(x)
      }

      tree.child(node).forall(selfAndChildNotGhostDataDependent)
    }

  lazy val classifiedGhostStmt: PStatement => Boolean =
    attr[PStatement, Boolean] {
      case _: PGhostStatement => true
      case s if stmtGhostContext(s) => true
      case PAssignment(_, left) => left.forall(classifiedGhostAssignee)
      case PAssignmentWithOp(_, _, left) => classifiedGhostAssignee(left)
      case PShortVarDecl(_, left) => left.forall(classifiedGhostId)
      case _ => false
    }

  private lazy val classifiedGhostAssignee: PAssignee => Boolean =
    attr[PAssignee, Boolean] {
      case PNamedOperand(id) => classifiedGhostId(id)
      case PSelection(base, id) => classifiedGhostId(id) || classifiedGhostExpr(base)
      case PSelectionOrMethodExpr(base, id) => classifiedGhostId(id) || classifiedGhostId(base)
      case _: PIndexedExp => false
      case _: PDereference => false
    }

  /** returns true iff expression is classified as ghost */
  private lazy val classifiedGhostExpr: PExpression => Boolean =
    attr[PExpression, Boolean] {
      case _: PGhostExpression => true
      case PNamedOperand(id) => classifiedGhostId(id)
      case exp => tree.child(exp).collect{ case x: PExpression => x }.exists(classifiedGhostExpr)
    }

  /** returns true iff identifier is classified as ghost */
  private def classifiedGhostId(id: PIdnNode): Boolean = entity(id) match {
    case r: Regular => r.isGhost
    case _ => false
  }
}
