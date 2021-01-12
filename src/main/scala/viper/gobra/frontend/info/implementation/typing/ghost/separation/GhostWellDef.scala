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
      case _: PTypeDecl => error(m, "ghost types are currently not supported") // TODO
      case _ => noMessages
    }

    case _: PGhostMember => noMessages

    case n : PVarDecl => n.typ match {
      case Some(typ) => error(n, s"ghost error: expected an actual type but found $typ",
        isTypeGhost(typ) && !enclosingGhostContext(n))
      case None => noMessages
    }

    case m if enclosingGhostContext(m) => noMessages

    case _ => noMessages
  }

  private def stmtGhostSeparation(stmt: PStatement): Messages = stmt match {
    case _: PGhostStatement => noMessages
    case s if enclosingGhostContext(s) => noMessages

    case stmt @ PForStmt(pre, cond, post, _, body) => {
      // NOTE the loop specification *is* allowed to contain ghost constructs; the rest isn't
      val ghostChildFound = Seq(pre, post, Some(cond), Some(body)).flatten.map(noGhostPropagationFromChildren)
      error(stmt, "ghost error: Found ghost child expression but expected none", ghostChildFound.exists(p => !p))
    }

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
      |  _: PAssForRange
      |  _: PShortForRange
      |  _: PGoStmt
      |  _: PSelectStmt
      |  _: PBreak
      |  _: PContinue
      |  _: PGoto
      |  _: PDeferStmt
      ) => error(n, "ghost error: Found ghost child expression but expected none", !noGhostPropagationFromChildren(n))

    case PAssignment(right, left) => ghostAssignableToAssignee(right: _*)(left: _*)
    case PAssignmentWithOp(right, _, left) => ghostAssignableToAssignee(right)(left)

    case PShortVarDecl(right, left, _) => ghostAssignableToId(right: _*)(left: _*)

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
       | _: PBinaryExp[_,_]
       | _: PUnfolding
       | _: PLength
       | _: PCapacity
       | _: PLiteral
       | _: PReference
       | _: PBlankIdentifier
       | _: PFPredConstructor
    => noMessages

    case n@ ( // these are just suggestions for now. We will have to adapt then, when we decide on proper ghost separation rules.
      _: PReceive
      ) => error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) =>  error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
      case (Left(callee), Some(p: ap.FunctionCall)) => ghostAssignableToCallExpr(p.args: _*)(callee)
      case (Left(_), Some(_: ap.PredicateCall)) => noMessages
      case (Left(_), Some(_: ap.PredExprInstance)) => noMessages
      case _ => violation("expected conversion, function call, or predicate call")
    }

    case _: PNew => noMessages

    case n@PMake(_, args) => error(
      n,
      "ghost error: make expressions may not contain ghost expressions",
      args exists (x => !noGhostPropagationFromChildren(x))
    )
  }

  private def typeGhostSeparation(typ: PType): Messages = typ match {
    case _: PGhostType => noMessages
    case n: PStructType => n.fields.flatMap(f => {
      error(f, s"ghost error: expected an actual type but found ${f.typ}",
        isTypeGhost(f.typ) && !enclosingGhostContext(f))
    })
    case n: PType => error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
  }

  private def miscGhostSeparation(misc : PMisc) : Messages = misc match {
    case _: PGhostMisc => noMessages
    case p: PActualParameter => error(p, s"ghost error: expected an actual type but found ${p.typ}",
      isTypeGhost(p.typ) && !enclosingGhostContext(p))
    case _ => noMessages
  }
}
