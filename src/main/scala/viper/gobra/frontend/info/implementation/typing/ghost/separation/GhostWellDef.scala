// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.GhostLevel
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.SymbolTable.{Closure, Function, Regular, SingleLocalVariable}
import viper.gobra.frontend.info.base.Type.{ActualPointerT, GhostPointerT}
import viper.gobra.util.Violation
import viper.gobra.util.Violation.violation

trait GhostWellDef { this: TypeInfoImpl =>

  /** message that an expression of level `read` occurs in code of level `written` */
  private def childMsg(read: GhostLevel, written: GhostLevel): String = {
    val expected = if (written.isGhost) s"expected at most ${ghostLattice.show(written)}" else "expected none"
    s"ghost error: Found ${ghostLattice.show(read)} child expression but $expected"
  }

  /** checks that the expressions `n` contains may flow into the code `n` belongs to, e.g., that control flow does not
    * depend on values of a higher level than the code it controls */
  private def childrenFlowIntoContext(n: PNode): Messages =
    flow(n, Seq(levelFromChildren(n)), Seq(enclosingGhostLevel(n)))(childMsg)

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
    case _: PExplicitGhostMember => noMessages
    case _: PGhostMember => noMessages

    case n : PVarDecl => n.typ match {
      case Some(typ) => error(n, s"ghost error: expected an actual type but found $typ",
        isTypeGhost(typ) && !isEnclosingGhost(n))
      case None => noMessages
    }

    case n: PTypeDecl =>
      error(n, s"ghost error: expected an actual type but found ${n.right}", isTypeGhost(n.right) && !isEnclosingGhost(n)) ++
      // to avoid confusion about how equality works for this type declaration, we require that the type declaration
      // is ghost iff its RHS is a ghost type.
      // An alternative implementation could permit all types on the RHS for which the definition of `===` matches `==`.
      // This alternative would permit, e.g., a ghost type definition with `int` on the RHS.
      error(n, s"ghost error: expected a ghost type but found ${n.right}", !isTypeGhost(n.right) && isEnclosingGhost(n))

    case m if isEnclosingGhost(m) => noMessages

    case m: PMethodDecl => error(m, "ghost error: expected an actual receiver type",
      isTypeGhost(m.receiver.typ) && !isEnclosingGhost(m))

    case _ => noMessages
  }

  private def stmtGhostSeparation(stmt: PStatement): Messages = stmt match {
    case p: PClosureImplProof => provenSpecMatchesInGhostnessWithCall(p)

    case PAssignment(right, left) => ghostAssignableToAssignee(right: _*)(left: _*)
    case PAssignmentWithOp(right, _, left) => ghostAssignableToAssignee(right)(left)

    case _: PGhostStatement => noMessages

    case stmt @ PForStmt(pre, cond, post, _, body) =>
      // NOTE the loop specification *is* allowed to contain ghost constructs; the rest has to flow into the code the
      // loop belongs to
      flow(stmt, Seq(pre, post, Some(cond), Some(body)).flatten.map(levelFromChildren), Seq(enclosingGhostLevel(stmt)))(childMsg)

    case m: PMember => memberGhostSeparation(m)

    case _: PLabeledStmt
      |  _: PEmptyStmt
      |  _: PBlock
      |  _: PSeq
      |  _: PExpressionStmt
      |  _: POutline
      |  _: PCritical
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
      ) => childrenFlowIntoContext(n)

    case PShortVarDecl(right, left, _) => ghostAssignableToId(right: _*)(left: _*)

    case n@ PReturn(right) =>
      if (right.nonEmpty && tryEnclosingClosureImplementationProof(n).isEmpty) {
        ghostAssignableToParam(right: _*)(returnParamsAndTypes(n).map(_._2): _*)
      } else noMessages
  }

  private def exprGhostSeparation(expr: PExpression): Messages = expr match {
    case _: PGhostExpression => noMessages

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
       | _: PAsserting
       | _: PLength
       | _: PCapacity
       | _: PLiteral
       | _: PReference
       | _: PBlankIdentifier
       | _: PIota
       | _: PPredConstructor
       | _: PUnpackSlice
    => noMessages

    // resolved into PCompositeLit/PPredConstructor before type-checking; unreachable here
    case n: PCompositeLitOrPredConstructor => violation(s"unresolved literal/predicate-constructor ambiguity: $n")

    case n@ ( // these are just suggestions for now. We will have to adapt then, when we decide on proper ghost separation rules.
      _: PReceive
      ) => childrenFlowIntoContext(n)

    case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
      case (Right(_), Some(_: ap.Conversion)) => noMessages
      case (Left(_), Some(call: ap.FunctionCall)) =>
        val contextLevel = enclosingGhostLevel(n)
        // an impure callee modifies state of its own level, so the code calling it has to flow into that level;
        // impure calls in specifications, which write nothing, are reported by the purity checks instead
        val impureCallInCode = isPureExpr(n).nonEmpty && contextLevel != ghostLattice.top
        (if (impureCallInCode) flow(n, Seq(contextLevel), Seq(calleeGhostTyping(call).level)) { (read, written) =>
          s"ghost error: Found call to impure ${ghostLattice.show(written)} function in ${ghostLattice.show(read)} code"
        } else noMessages) ++
          ghostAssignableToCallExpr(call)
      case (Left(_), Some(call: ap.ClosureCall)) =>
        val target = closureSpecTarget(call.spec)
        error(n, s"ghost error: Found call to a closure with spec ${call.spec}, which specifies neither ghost nor pure closures, in ghost code or with a ghost closure",
          // as for functions, only ghost or pure closures may be called in ghost code. Calls with a ghost callee are ghost.
          (isEnclosingGhost(n) || isExprGhost(call.callee)) && !target.specifiesGhostClosures && !target.isPure) ++
          ghostAssignableToClosureCall(call)
      case (Left(_), Some(_: ap.PredicateCall)) => noMessages
      case (Left(_), Some(_: ap.PredExprInstance)) => noMessages
      case _ => violation("expected conversion, function call, or predicate call")
    }

    case e: PNew =>
      if (isEnclosingGhost(e)) {
        Violation.violation(exprType(e).isInstanceOf[GhostPointerT], s"Cannot allocate non-ghost memory in ghost code.")
      } else {
        Violation.violation(exprType(e).isInstanceOf[ActualPointerT], s"Cannot allocate ghost memory in non-ghost code.")
      }
      noMessages

    case e: PMake => (e, isEnclosingGhost(e)) match {
      case (PMake(_: PGhostSliceType, _), true)  => noMessages
      case (_, true) => error(e, "Allocating memory with make within ghost code is forbidden")
      case (PMake(_, args), false) => error(
        e,
        "ghost error: make expressions may not contain ghost expressions",
        args exists (x => !noGhostPropagationFromChildren(x))
      )
    }
  }

  private def typeGhostSeparation(typ: PType): Messages = typ match {
    case _: PGhostType => noMessages
    case n: PStructType => n.fields.flatMap(f => {
      error(f, s"ghost error: expected an actual type but found ${f.typ}",
        isTypeGhost(f.typ) && !isEnclosingGhost(f))
    })
    case _: PInterfaceType => noMessages
    case n: PType => childrenFlowIntoContext(n)
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

        case Closure(PFunctionLit(_, PClosureDecl(args, r, _, _)), _, _) => unsafeMessage(! {
          args.forall(wellGhostSeparated.valid) && wellGhostSeparated.valid(r)
        })

        case _ => LocalMessages(noMessages)
      }
  }

  private def miscGhostSeparation(misc : PMisc) : Messages = misc match {
    case _: PGhostMisc => noMessages
    case p: PActualParameter => error(p, s"ghost error: expected an actual type but found ${p.typ}",
      isTypeGhost(p.typ) && !isEnclosingGhost(p))
    case _ => noMessages
  }
}
