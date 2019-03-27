package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.{BooleanT, ChannelModus, ChannelT, InterfaceT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait StmtTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val wellDefStmt: WellDefinedness[PStatement] = createWellDef {

    case n@PConstDecl(typ, right, left) =>
      declarableTo.errors(right map exprType, typ map typeType, left map idType)(n)

    case n@PVarDecl(typ, right, left) =>
      declarableTo.errors(right map exprType, typ map typeType, left map idType)(n)

    case n: PTypeDecl => noMessages

    case n@PExpressionStmt(exp) => isExecutable.errors(exp)(n)

    case n@PSendStmt(chn, msg) => (exprType(chn), exprType(msg)) match {
      case (ChannelT(elem, ChannelModus.Bi | ChannelModus.Send), t) => assignableTo.errors(t, elem)(n)
      case (chnt, _) => message(n, s"type error: got $chnt but expected send-permitting channel")
    }

    case n@PAssignment(lefts, rights) =>
      lefts.flatMap(a => assignable.errors(a)(a)) ++ multiAssignableTo.errors(rights map exprType, lefts map exprType)(n)

    case n@PAssignmentWithOp(left, op, right) =>
      assignable.errors(left)(n) ++ compatibleWithAssOp.errors(exprType(left), op)(n) ++
        assignableTo.errors(exprType(right), exprType(left))(n)

    case n@PShortVarDecl(rights, lefts) =>
      if (lefts.forall(pointsToData)) multiAssignableTo.errors(rights map exprType, lefts map idType)(n)
      else message(n, s"at least one assignee in $lefts points to a type")

    case n: PIfStmt => n.ifs.flatMap(ic => comparableTypes.errors(exprType(ic.condition), BooleanT)(ic))

    case n@PExprSwitchStmt(_, exp, _, dflt) =>
      message(n, s"found more than one default case", dflt.size > 1) ++
        comparableType.errors(exprType(exp))(n)

    case _: PExprSwitchDflt => noMessages

    case n@tree.parent.pair(PExprSwitchCase(left, _), sw: PExprSwitchStmt) =>
      left.flatMap(e => comparableTypes.errors(exprType(e), exprType(sw.exp))(n))

    case n: PTypeSwitchStmt =>
      message(n, s"found more than one default case", n.dflt.size > 1) ++ {
        val et = exprType(n.exp)
        val ut = underlyingType(et)
        message(n, s"type error: got $et but expected underlying interface type", !ut.isInstanceOf[InterfaceT])
      } // TODO: also check that cases have type that could implement the type

    case n@PForStmt(_, cond, _, _) => comparableTypes.errors(exprType(cond), BooleanT)(n)

    case n@PShortForRange(exp, lefts, _) =>
      if (lefts.forall(pointsToData)) multiAssignableTo.errors(Vector(miscType(exp)), lefts map idType)(n)
      else message(n, s"at least one assignee in $lefts points to a type")


    case n@PAssForRange(exp, lefts, _) =>
      multiAssignableTo.errors(Vector(miscType(exp)), lefts map exprType)(n) ++
        lefts.flatMap(t => addressable.errors(t)(t))

    case n@PGoStmt(exp) => isExecutable.errors(exp)(n)

    case n: PSelectStmt =>
      n.aRec.flatMap(rec =>
        multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.ass.map(exprType))(rec) ++
          rec.ass.flatMap(a => assignable.errors(a)(a))
      ) ++ n.sRec.flatMap(rec =>
        if (rec.shorts.forall(pointsToData))
          multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.shorts map idType)(rec)
        else message(n, s"at least one assignee in ${rec.shorts} points to a type")

      )


    case n@PReturn(exps) =>
      (enclosingCodeRoot(n) match {
        case f: PFunctionDecl  => f.result
        case f: PFunctionLit   => f.result
        case m: PMethodDecl    => m.result
      }) match {
        case PVoidResult() => message(n, s"expected no arguments but got $exps", exps.nonEmpty)
        case PResultClause(outs) =>
          if (outs forall wellDefMisc.valid)
            multiAssignableTo.errors(exps map exprType, outs map miscType)(n)
          else message(n, s"return cannot be checked because the enclosing signature is incorrect")
      }

    case n@PDeferStmt(exp) => isExecutable.errors(exp)(n)

    case _: PBlock => noMessages
    case _: PSeq => noMessages

    case s => violation(s"$s was not handled")
  }
}
