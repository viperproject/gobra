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
import viper.gobra.frontend.info.base.SymbolTable.{Function, Regular, SingleLocalVariable}
import viper.gobra.util.Violation.violation

trait GhostWellDef { this: TypeInfoImpl =>

  private def selfWellGhostSeparated(n: PNode): Boolean = n match {
    case i: PIdnNode => idGhostSeparation(i).valid
    case n => wellGhostSeparated.valid(n)
  }

  lazy val wellGhostSeparated: WellDefinedness[PNode] = createIndependentWellDef[PNode]{
    case m: PMember => memberGhostSeparation(m)
    case s: PStatement => stmtGhostSeparation(s)
    case e: PExpression => exprGhostSeparation(e)
    case t: PType => typeGhostSeparation(t)
    case m: PMisc => miscGhostSeparation(m)
  }{ n => isWellDefined(n) && children(n).forall(selfWellGhostSeparated) }

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

    case m: PMember => memberGhostSeparation(m)

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
    case e if enclosingGhostContext(e) =>
      e match {
        case PMake(_: PGhostSliceType, _) => noMessages
        case _: PMake | _: PNew | PReference(_: PCompositeLit) => error(e, "Allocating memory within ghost code is forbidden")
        case _ => noMessages
      }

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
       | _: PPredConstructor
       | _: PUnpackSlice
    => noMessages

    case n@ ( // these are just suggestions for now. We will have to adapt then, when we decide on proper ghost separation rules.
      _: PReceive
      ) => error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) =>  error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
      case (Left(_), Some(call: ap.FunctionCall)) => ghostAssignableToCallExpr(call)
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
    case _: PInterfaceType => noMessages
    case n: PType => error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
  }

  private lazy val idGhostSeparation: WellDefinedness[PIdnNode] = createWellDefWithValidityMessages {
    id =>
      entity(id) match {
        case entity: Regular if entity.context != this => LocalMessages(noMessages) // imported entities are assumed to be well-formed

        case SingleLocalVariable(exp, _, _, _, _, _) => unsafeMessage(! {
          // exp has to be well-def if it exists (independently on the existence of opt) as we need it for ghost typing
          exp.forall(wellGhostSeparated.valid)
        })

        case Function(PFunctionDecl(_, args, r, _, _), _, _) => unsafeMessage(! {
          args.forall(wellGhostSeparated.valid) && wellGhostSeparated.valid(r)
        })

        case _ => LocalMessages(noMessages)
      }
  }

  private def miscGhostSeparation(misc : PMisc) : Messages = misc match {
    case _: PGhostMisc => noMessages
    case p: PActualParameter => error(p, s"ghost error: expected an actual type but found ${p.typ}",
      isTypeGhost(p.typ) && !enclosingGhostContext(p))
    case _ => noMessages
  }
}
