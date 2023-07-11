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
import viper.gobra.frontend.info.base.SymbolTable.{Closure, Function, Regular, SingleLocalVariable}
import viper.gobra.frontend.info.implementation.typing.base.TypingComponents
import viper.gobra.frontend.info.implementation.typing.modifiers.GhostModifierUnit.getModifier
import viper.gobra.frontend.info.implementation.typing.modifiers.{GhostModifier, ModifierUnit}
import viper.gobra.util.Violation.violation

trait GhostWellDef extends TypingComponents { this: ModifierUnit[GhostModifier] =>

  def isGhost(ctx: TypeInfoImpl, n: PNode): Boolean = getModifier(ctx)(n).get == GhostModifier.Ghost

  def memberGhostSeparation(ctx: TypeInfoImpl, member: PMember): Messages = member match {
    case m: PExplicitGhostMember => m.actual match {
      case _: PTypeDecl => error(m, "ghost types are currently not supported") // TODO
      case _ => noMessages
    }

    case _: PGhostMember => noMessages

    case n : PVarDecl => n.typ match {
      case Some(typ) => error(n, s"ghost error: expected an actual type but found $typ",
        isGhost(ctx, typ) && !ctx.enclosingGhostContext(n))
      case None => noMessages
    }

    case m if ctx.enclosingGhostContext(m) => noMessages

    case _ => noMessages
  }

  def stmtGhostSeparation(ctx: TypeInfoImpl, stmt: PStatement): Messages = stmt match {
    case p: PClosureImplProof => ctx.provenSpecMatchesInGhostnessWithCall(p)

    case _: PGhostStatement => noMessages
    case s if ctx.enclosingGhostContext(s) => noMessages

    case stmt @ PForStmt(pre, cond, post, _, body) => {
      // NOTE the loop specification *is* allowed to contain ghost constructs; the rest isn't
      val ghostChildFound = Seq(pre, post, Some(cond), Some(body)).flatten.map(ctx.noGhostPropagationFromChildren)
      error(stmt, "ghost error: Found ghost child expression but expected none", ghostChildFound.exists(p => !p))
    }

    case m: PMember => memberGhostSeparation(ctx, m)

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
      ) => error(n, "ghost error: Found ghost child expression but expected none", !ctx.noGhostPropagationFromChildren(n))

    case PAssignment(right, left) => ctx.ghostAssignableToAssignee(right: _*)(left: _*)
    case PAssignmentWithOp(right, _, left) => ctx.ghostAssignableToAssignee(right)(left)

    case PShortVarDecl(right, left, _) => ctx.ghostAssignableToId(right: _*)(left: _*)

    case n@ PReturn(right) =>
      if (right.nonEmpty && ctx.tryEnclosingClosureImplementationProof(n).isEmpty) {
        ctx.ghostAssignableToParam(right: _*)(ctx.returnParamsAndTypes(n).map(_._2): _*)
      } else noMessages
  }

  def exprGhostSeparation(ctx: TypeInfoImpl, expr: PExpression): Messages = expr match {
    case _: PGhostExpression => noMessages
    case e if ctx.enclosingGhostContext(e) =>
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
      ) => error(n, "ghost error: Found ghost child expression, but expected none", !ctx.noGhostPropagationFromChildren(n))

    case n: PInvoke => (ctx.exprOrType(n.base), ctx.resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) => noMessages
      case (Left(_), Some(call: ap.FunctionCall)) => ctx.ghostAssignableToCallExpr(call)
      case (Left(_), Some(call: ap.ClosureCall)) => ctx.ghostAssignableToClosureCall(call)
      case (Left(_), Some(_: ap.PredicateCall)) => noMessages
      case (Left(_), Some(_: ap.PredExprInstance)) => noMessages
      case _ => violation("expected conversion, function call, or predicate call")
    }

    case _: PNew => noMessages

    case n@PMake(_, args) => error(
      n,
      "ghost error: make expressions may not contain ghost expressions",
      args exists (x => !ctx.noGhostPropagationFromChildren(x))
    )
  }

  def typeGhostSeparation(ctx: TypeInfoImpl, typ: PType): Messages = typ match {
    case _: PGhostType => noMessages
    case n: PStructType => n.fields.flatMap(f => {
      error(f, s"ghost error: expected an actual type but found ${f.typ}",
        ctx.isTypeGhost(f.typ) && !ctx.enclosingGhostContext(f))
    })
    case _: PInterfaceType => noMessages
    case n: PType => error(n, "ghost error: Found ghost child expression, but expected none", !ctx.noGhostPropagationFromChildren(n))
  }

  lazy val idGhostSeparation(ctx: TypeInfoImpl): WellDefinedness[PIdnNode] = createWellDefWithValidityMessages {
    id =>
      ctx.entity(id) match {
        case entity: Regular if entity.context != this => LocalMessages(noMessages) // imported entities are assumed to be well-formed

        case SingleLocalVariable(exp, _, _, _, _, _) => unsafeMessage(! {
          // exp has to be well-def if it exists (independently on the existence of opt) as we need it for ghost typing
          exp.forall(n => hasWellDefModifier(ctx)(n).valid)
        })

        // TODO check this with Felix
        case Function(PFunctionDecl(_, _, args, r, _, _), _, _) => unsafeMessage(! {
          args.forall(n => hasWellDefModifier(ctx)(n).valid) && hasWellDefModifier(ctx)(args).valid(r)
        })

        case Closure(PFunctionLit(_, PClosureDecl(args, r, _, _)), _, _) => unsafeMessage(! {
          args.forall(hasWellDefModifier(ctx)(_).valid) && hasWellDefModifier(ctx)(args).valid(r)
        })

        case _ => LocalMessages(noMessages)
      }
  }(true)

  def miscGhostSeparation(ctx: TypeInfoImpl, misc : PMisc) : Messages = misc match {
    case _: PGhostMisc => noMessages
    case p: PActualParameter => error(p, s"ghost error: expected an actual type but found ${p.typ}",
      ctx.isTypeGhost(p.typ) && !ctx.enclosingGhostContext(p))
    case _ => noMessages
  }
}
