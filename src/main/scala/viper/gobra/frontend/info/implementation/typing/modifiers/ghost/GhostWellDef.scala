// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.modifiers.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{AstPattern => ap, _}
import viper.gobra.frontend.info.base.SymbolTable.{Closure, Function, Regular, SingleLocalVariable}
import viper.gobra.frontend.info.implementation.typing.base.TypingComponents
import viper.gobra.util.Violation.violation

trait GhostWellDef extends TypingComponents { this: GhostModifierUnit =>

  def isGhost(n: PNode): Boolean = getModifier(n).get == GhostModifier.Ghost

  def memberGhostSeparation(member: PMember): Messages = member match {
    case m: PExplicitGhostMember => m.actual match {
      case _: PTypeDecl => error(m, "ghost types are currently not supported") // TODO
      case _ => noMessages
    }

    case _: PGhostMember => noMessages

    case n : PVarDecl => n.typ match {
      case Some(typ) => error(n, s"ghost error: expected an actual type but found $typ",
        isGhost(typ) && !enclosingGhostContext(n))
      case None => noMessages
    }

    case m if enclosingGhostContext(m) => noMessages

    case _ => noMessages
  }

  def stmtGhostSeparation(stmt: PStatement): Messages = stmt match {
    case p: PClosureImplProof => provenSpecMatchesInGhostnessWithCall(p)

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
      |  _: POutline
      | _: PShortForRange
      | _: PAssForRange
    => noMessages

    case n@ (
      _: PSendStmt
      |  _: PIfStmt
      |  _: PExprSwitchStmt
      |  _: PTypeSwitchStmt
      |  _: PGoStmt
      |  _: PSelectStmt
      |  _: PBreak
      |  _: PContinue
      |  _: PGoto
      |  _: PDeferStmt
      ) => error(n, "ghost error: Found ghost child expression but expected none", !noGhostPropagationFromChildren(n))

    case PAssignment(right, left) => ghostAssignableToAssignee(right: _*)(left: _*)
    case PAssignmentWithOp(right, _, left) => ghostAssignableToAssignee(right)(left)

    case _: PShortVarDecl => noMessages

    case _: PReturn => noMessages
  }

  def exprGhostSeparation(expr: PExpression): Messages = expr match {
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
       | _: PBitNegation
       | _: PBinaryExp[_,_]
       | _: PUnfolding
       | _: PLength
       | _: PCapacity
       | _: PLiteral
       | _: PReference
       | _: PBlankIdentifier
       | _: PIota
       | _: PPredConstructor
       | _: PUnpackSlice
    => noMessages

    case n@ ( // these are just suggestions for now. We will have to adapt then, when we decide on proper ghost separation rules.
      _: PReceive
      ) => error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))

    case n: PInvoke => (ctx.exprOrType(n.base), ctx.resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) => noMessages
      case (Left(_), Some(_: ap.FunctionCall)) => noMessages
      case (Left(_), Some(_: ap.ClosureCall)) => noMessages
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

  def typeGhostSeparation(typ: PType): Messages = typ match {
    case _: PGhostType => noMessages
    case n: PStructType => n.fields.flatMap(f => {
      error(f, s"ghost error: expected an actual type but found ${f.typ}",
        isTypeGhost(f.typ) && !enclosingGhostContext(f))
    })
    case _: PInterfaceType => noMessages
    case n: PType => error(n, "ghost error: Found ghost child expression, but expected none", !noGhostPropagationFromChildren(n))
  }

  lazy val idGhostSeparation: WellDefinedness[PIdnNode] = createWellDefWithValidityMessages[PIdnNode] {
    id =>
      ctx.entity(id) match {
        case entity: Regular if entity.context != ctx => LocalMessages(noMessages) // imported entities are assumed to be well-formed

        case SingleLocalVariable(exp, _, _, _, _, _) => unsafeMessage(! {
          // exp has to be well-def if it exists (independently on the existence of opt) as we need it for ghost typing
          exp.forall(hasWellDefModifier.valid)
        })

        case Function(PFunctionDecl(_, args, r, _, _), _, _) => unsafeMessage(! {
          args.forall(hasWellDefModifier.valid) && hasWellDefModifier.valid(r)
        })

        case Closure(PFunctionLit(_, PClosureDecl(args, r, _, _)), _, _) => unsafeMessage(! {
          args.forall(hasWellDefModifier.valid) && hasWellDefModifier.valid(r)
        })

        case _ => LocalMessages(noMessages)
      }
  }(_ => true)

  def miscGhostSeparation(misc : PMisc) : Messages = misc match {
    case _: PGhostMisc => noMessages
    case p: PActualParameter => error(p, s"ghost error: expected an actual type but found ${p.typ}",
      isTypeGhost(p.typ) && !enclosingGhostContext(p))
    case _ => noMessages
  }
}
