package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation.violation

trait GhostWellDef { this: TypeInfoImpl =>

  lazy val wellGhostSeparated: WellDefinedness[PNode] = createIndependentWellDef[PNode]{
    case m: PMember => memberGhostSeparation(m)
    case s: PStatement => stmtGhostSeparation(s)
    case e: PExpression => exprGhostSeparation(e)
    case t: PType => typeGhostSeparation(t)
    case m: PMisc => miscGhostSeparation(m)
  }{ n => selfWellDefined(n) && children(n).forall(wellGhostSeparated.valid) }

  private def memberGhostSeparation(member: PMember): Messages = member match {
    case m: PExplicitGhostMember => m.actual match {
      case _: PTypeDecl => message(m, "ghost types are currently not supported") // TODO
      case _ => noMessages
    }

    case _: PGhostMember => noMessages

    case n : PVarDecl => n.typ match {
      case Some(typ) => message(n, s"ghost error: expected an actual type but found $typ",
        isTypeGhost(typ) && !enclosingGhostContext(n))
      case None => noMessages
    }

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
      ) => message(n, "ghost error: Found ghost child expression but expected none", !noGhostPropagationFromChildren(n))

    case n@ PAssignment(right, left) => ghostAssignableToAssignee(right: _*)(left: _*)
    case n@ PAssignmentWithOp(right, _, left) => ghostAssignableToAssignee(right)(left)

    case n@ PShortVarDecl(right, left, _) => ghostAssignableToId(right: _*)(left: _*)

    case n@ PReturn(right) =>
      val res = enclosingCodeRootWithResult(n).result
      if (right.nonEmpty) {
        ghostAssignableToParam(right: _*)(res.outs: _*)
      } else noMessages
  }

  private def exprGhostSeparation(expr: PExpression): Messages = expr match {
    case _: PGhostExpression => noMessages
    case e if enclosingGhostContext(e) => noMessages

    case _: PDot
       | _: PDeref
       | _: PIndexedExp
       | _: PSliceExp
       | _: PTypeAssertion
       | _: PNamedOperand
       | _: PNegation
       | _: PBinaryExp
       | _: PUnfolding
       | _: PLength
       | _: PLiteral
    => noMessages

    case n@ ( // these are just suggestions for now. We will have to adapt then, when we decide on proper ghost separation rules.
      _: PReceive
      |  _: PReference
      ) => message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(p: ap.Conversion)) =>  message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
      case (Left(callee), Some(p: ap.FunctionCall)) => ghostAssignableToCallExpr(p.args: _*)(callee)
      case (Left(_), Some(p: ap.PredicateCall)) => noMessages
      case _ => violation("expected conversion, function call, or predicate call")
    }
  }

  private def typeGhostSeparation(typ: PType): Messages = typ match {
    case _: PGhostType => noMessages
    case n: PType => message(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
  }

  private def miscGhostSeparation(misc : PMisc) : Messages = {
    // helper definition
    def check(n : PNode, t : PType) : Messages =
      message(n, s"ghost error: expected an actual type but found $t",
        isTypeGhost(t) && !enclosingGhostContext(n))

    misc match {
      case _: PGhostMisc => noMessages
      case p: PActualParameter => check(p, p.typ)
      case f: PFieldDecl => check(f, f.typ)
      case _ => noMessages
    }
  }
}
