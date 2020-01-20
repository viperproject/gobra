package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

trait GhostWellDef { this: TypeInfoImpl =>

  lazy val wellGhostSeparated: WellDefinedness[PNode] = createIndependentWellDef{
    case m: PMember => memberGhostSeparation(m)
    case s: PStatement => stmtGhostSeparation(s)
    case e: PExpression => exprGhostSeparation(e)
    case t: PType => typeGhostSeparation(t)
  }{ n => selfWellDefined(n) && children(n).forall(wellGhostSeparated.valid) }

  private def memberGhostSeparation(member: PMember): Messages = member match {
    case m: PExplicitGhostMember => m.actual match {
      case _: PTypeDecl => message(m, "ghost types are currently not supported") // TODO
      case _ => noMessages
    }

    case _: PGhostMember => noMessages

    case m if enclosingGhostContext(m) => noMessages
    case _ => noMessages
  }

  private def stmtGhostSeparation(stmt: PStatement): Messages = stmt match {
    case _: PGhostStatement => noMessages
    case s if enclosingGhostContext(s) => noMessages

    case _: PLabeledStmt
         |  _: PEmptyStmt
         |  _: PBlock
         |  _: PSeq
         |  _: PExpressionStmt
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
      ) => message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))

    case n@ PAssignment(right, left) => assignableToAssignee(right: _*)(left: _*)
    case n@ PAssignmentWithOp(right, _, left) => assignableToAssignee(right)(left)

    case n@ PShortVarDecl(right, left, _) => assignableToId(right: _*)(left: _*)

    case n@ PReturn(right) => enclosingCodeRootWithResult(n).result match {
      case PVoidResult() => violation("return arity not consistent with required enclosing arguments")
      case PResultClause(left) => assignableToParam(right: _*)(left: _*)
    }
  }

  private def exprGhostSeparation(expr: PExpression): Messages = expr match {
    case _: PGhostExpression => noMessages
    case e if enclosingGhostContext(e) => noMessages

    case /*_: PSelectionOrMethodExpr
         |*/  _: PSelection
         //|  _: PMethodExpr
         |  _: PIndexedExp
         |  _: PSliceExp
         |  _: PTypeAssertion
         |  _: PNamedOperand
         |  _: PNegation
         |  _: PBinaryExp
         |  _: PUnfolding
    => noMessages

    case n@ (
      _: PLiteral
      |  _: PReceive
      |  _: PReference
      |  _: PDereference
      |  _: PConversion
      ) => message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))

    case n: PConversionOrUnaryCall => resolveConversionOrUnaryCall(n){
      case (base, id) => message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
    } {
      case (base, id) => assignableToCallId(id)(base)
    }.get

    //case n@ PCall(callee, args) => assignableToCallExpr(args: _*)(callee)
  }

  private def typeGhostSeparation(typ: PType): Messages = typ match {
    case _: PGhostType => noMessages
    case n: PType => message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
  }
}
