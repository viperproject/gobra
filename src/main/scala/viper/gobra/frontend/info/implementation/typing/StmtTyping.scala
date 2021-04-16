// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.{BooleanT, ChannelModus, ChannelT, InterfaceT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait StmtTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val wellDefStmt: WellDefinedness[PStatement] = createWellDef {
    case stmt: PActualStatement => wellDefActualStmt(stmt)
    case stmt: PGhostStatement  => wellDefGhostStmt(stmt)
  }

  private[typing] def wellDefActualStmt(stmt: PActualStatement): Messages = stmt match {

    case n@PConstDecl(typ, right, left) =>
      right.flatMap(isExpr(_).out) ++
        declarableTo.errors(right map exprType, typ map typeSymbType, left map idType)(n)

    case n@PVarDecl(typ, right, left, _) =>
      right.flatMap(isExpr(_).out) ++
        declarableTo.errors(right map exprType, typ map typeSymbType, left map idType)(n)

    case n: PTypeDecl => isType(n.right).out ++ (n.right match {
      case s: PStructType =>
        error(n, s"invalid recursive type ${n.left.name}", cyclicStructDef(s, Some(n.left)))
      case _ => noMessages
    })

    case n@PExpressionStmt(exp) => isExpr(exp).out ++ isExecutable.errors(exp)(n)

    case n@PSendStmt(chn, msg) =>
      isExpr(chn).out ++ isExpr(msg).out ++
        ((exprType(chn), exprType(msg)) match {
          case (ChannelT(elem, ChannelModus.Bi | ChannelModus.Send), t) => assignableTo.errors(t, elem)(n)
          case (chnt, _) => error(n, s"type error: got $chnt but expected send-permitting channel")
        })

    case n@PAssignment(rights, lefts) =>
      rights.flatMap(isExpr(_).out) ++ lefts.flatMap(isExpr(_).out) ++
        lefts.flatMap(a => assignable.errors(a)(a)) ++ multiAssignableTo.errors(rights map exprType, lefts map exprType)(n)

    case n@PAssignmentWithOp(right, op, left) =>
      isExpr(right).out ++ isExpr(left).out ++
        assignable.errors(left)(n) ++ compatibleWithAssOp.errors(exprType(left), op)(n) ++
        assignableTo.errors(exprType(right), exprType(left))(n)

    case n@PShortVarDecl(rights, lefts, _) =>
      // TODO: check that at least one of the variables is new
      if (lefts.forall(pointsToData))
        rights.flatMap(isExpr(_).out) ++
          multiAssignableTo.errors(rights map exprType, lefts map idType)(n)
      else error(n, s"at least one assignee in $lefts points to a type")

    case _: PLabeledStmt => noMessages

    case n: PIfStmt => n.ifs.flatMap(ic =>
      isExpr(ic.condition).out ++
        comparableTypes.errors(exprType(ic.condition), BooleanT)(ic)
    )

    case n@PExprSwitchStmt(_, exp, _, dflt) =>
      error(n, s"found more than one default case", dflt.size > 1) ++
        isExpr(exp).out ++ comparableType.errors(exprType(exp))(n)

    case _: PExprSwitchDflt => noMessages

    case n@tree.parent.pair(PExprSwitchCase(left, _), sw: PExprSwitchStmt) =>
      left.flatMap(e => isExpr(e).out ++ comparableTypes.errors(exprType(e), exprType(sw.exp))(n))

    case n: PTypeSwitchStmt =>
      error(n, s"found more than one default case", n.dflt.size > 1) ++
        isExpr(n.exp).out ++ {
        val et = exprType(n.exp)
        val ut = underlyingType(et)
        error(n, s"type error: got $et but expected underlying interface type", !ut.isInstanceOf[InterfaceT])
      } // TODO: also check that cases have type that could implement the type

    case n@PForStmt(_, cond, _, _, _) => isExpr(cond).out ++ comparableTypes.errors(exprType(cond), BooleanT)(n)

    case n@PShortForRange(exp, lefts, _) =>
      if (lefts.forall(pointsToData)) multiAssignableTo.errors(Vector(miscType(exp)), lefts map idType)(n)
      else error(n, s"at least one assignee in $lefts points to a type")


    case n@PAssForRange(exp, lefts, _) =>
      multiAssignableTo.errors(Vector(miscType(exp)), lefts map exprType)(n) ++
        lefts.flatMap(t => addressable.errors(t)(t))

    case n@PGoStmt(exp) => isExpr(exp).out ++ isExecutable.errors(exp)(n)

    case n: PSelectStmt =>
      n.aRec.flatMap(rec =>
        rec.ass.flatMap(isExpr(_).out) ++
          multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.ass.map(exprType))(rec) ++
          rec.ass.flatMap(a => assignable.errors(a)(a))
      ) ++ n.sRec.flatMap(rec =>
        if (rec.shorts.forall(pointsToData))
          multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.shorts map idType)(rec)
        else error(n, s"at least one assignee in ${rec.shorts} points to a type")
      )

    case n@PReturn(exps) =>
      exps.flatMap(isExpr(_).out) ++ {
        if (exps.nonEmpty) {
          val res = enclosingCodeRootWithResult(n).result
          if (res.outs forall wellDefMisc.valid)
            multiAssignableTo.errors(exps map exprType, res.outs map miscType)(n)
          else error(n, s"return cannot be checked because the enclosing signature is incorrect")
        } else noMessages // a return without arguments is always well-defined
      }

    case n@PDeferStmt(exp) => isExpr(exp).out ++ isExecutable.errors(exp)(n)

    case _: PBlock => noMessages
    case _: PSeq => noMessages
    case _: PEmptyStmt => noMessages

    case s => violation(s"$s was not handled")
  }
}
