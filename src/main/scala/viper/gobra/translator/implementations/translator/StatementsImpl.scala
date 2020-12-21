// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{GoCallPreconditionError, PreconditionError, Source}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{Comments, ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.verifier.ErrorReason
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  var counter: Int = 0

  def count: Int = {counter += 1; counter}

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Stmt)(ctx: Context): CodeWriter[vpr.Stmt] = {

    val typEncodingOptRes = ctx.typeEncoding.statement(ctx).lift(x)
    if (typEncodingOptRes.isDefined) return typEncodingOptRes.get map (s => stmtComment(x, s))


    val (pos, info, errT) = x.vprMeta

    def goS(s: in.Stmt): CodeWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)

    /**
      * Generates an Exhale statement for the assertions `pre` parametrized by `formalParams` and instantiated with `args`
      */
    def genExhale(pre: Vector[in.Assertion], formalParams: Vector[in.Parameter.In], args: Vector[in.Expr]): Writer[vpr.Stmt] = {
      Violation.violation(args.length == formalParams.length, "number of passed arguments must match number of expected arguments")
      val errorT = (x: Source.Verifier.Info, _: ErrorReason) => PreconditionError(x) dueTo GoCallPreconditionError(x)
      for {
        vArgss <- sequence(args map goE)
        funcArgs <- sequence(formalParams map goE)
        substitutions = (funcArgs zip vArgss).toMap
        preCond <- sequence(pre map goA)
        preCondInstance = preCond.map{ _.replace(substitutions) }
        and = preCondInstance.foldRight[vpr.Exp](vpr.TrueLit()(pos, info, errT))((x,y) => vpr.And(x,y)(pos, info, errT))
        _ <- assert(and, errorT)
      } yield vpr.Exhale(and)(pos, info, errT)
    }

    val vprStmt: CodeWriter[vpr.Stmt] = x match {
      case in.Block(decls, stmts) =>
        val inits = decls collect { case x: in.BodyVar => ctx.typeEncoding.initialization(ctx)(x) }
        val vDecls = decls map (blockDecl(_)(ctx))
        block{
          for {
            _ <- global(vDecls: _*)
            vInits <- seqns(inits)
            vBody <- sequence(stmts map ctx.stmt.translateF(ctx))
          } yield vu.seqn(vInits +: vBody)(pos, info, errT)
        }

      case in.Seqn(stmts) => seqns(stmts map goS)

      case in.If(cond, thn, els) =>
          for {
            c <- goE(cond)
            t <- seqn(goS(thn))
            e <- seqn(goS(els))
          } yield vpr.If(c, t, e)(pos, info, errT)

      case in.While(cond, invs, body) =>

        for {
          (cws, vCond) <- split(goE(cond))
          (iws, vInvs) = invs.map(ctx.ass.invariant(_)(ctx)).unzip
          cpre <- seqnUnit(cws)
          ipre <- seqnUnits(iws)
          vBody <- goS(body)

          cpost = vpr.If(vCond, cpre, vu.nop(pos, info, errT))(pos, info, errT)
          ipost = ipre

          wh = vu.seqn(Vector(
            cpre, ipre, vpr.While(vCond, vInvs, vu.seqn(Vector(vBody, cpost, ipost))(pos, info, errT))(pos, info, errT)
          ))(pos, info, errT)
        } yield wh

      case ass: in.SingleAss => ctx.typeEncoding.assignment(ctx)(ass.left, ass.right, ass)

      case in.FunctionCall(targets, func, args) =>
        for {
          vArgss <- sequence(args map goE)
          vTargets <- sequence(targets map goE)
          // vTargets can be field-accesses, but a MethodCall in Viper requires variables as targets.
          // Therefore, we introduce auxiliary variables and
          // add an assignment from the auxiliary variables to the actual targets
          (vUsedTargets, auxTargetsWithAssignment) = vTargets.map(viperTarget).unzip
          (auxTargetDecls, backAssignments) = auxTargetsWithAssignment.flatten.unzip
          _ <- local(auxTargetDecls: _*)
          _ <- write(vpr.MethodCall(func.name, vArgss, vUsedTargets)(pos, info, errT))
          assignToTargets = vpr.Seqn(backAssignments, Seq())(pos, info, errT)
        } yield assignToTargets

      case in.MethodCall(targets, recv, meth, args) =>
        for {
          vRecv <- goE(recv)
          vArgss <- sequence(args map goE)
          vTargets <- sequence(targets map goE)
          // vTargets can be field-accesses, but a MethodCall in Viper requires variables as targets.
          // Therefore, we introduce auxiliary variables and
          // add an assignment from the auxiliary variables to the actual targets
          (vUsedTargets, auxTargetsWithAssignment) = vTargets.map(viperTarget).unzip
          (auxTargetDecls, backAssignments) = auxTargetsWithAssignment.flatten.unzip
          _ <- local(auxTargetDecls: _*)
          _ <- write(vpr.MethodCall(meth.uniqueName, vRecv +: vArgss, vUsedTargets)(pos, info, errT))
          assignToTargets = vpr.Seqn(backAssignments, Seq())(pos, info, errT)
        } yield assignToTargets

      case in.GoFunctionCall(func, args) => genExhale(func.pres, func.args, args)
      case in.GoMethodCall(recv, meth, args) => genExhale(meth.pres, meth.receiver +: meth.args, recv +: args)
      case in.Assert(ass) => for {v <- goA(ass)} yield vpr.Assert(v)(pos, info, errT)
      case in.Assume(ass) => for {v <- goA(ass)} yield vpr.Assume(v)(pos, info, errT) // Assumes are later rewritten
      case in.Inhale(ass) => for {v <- goA(ass)} yield vpr.Inhale(v)(pos, info, errT)
      case in.Exhale(ass) => for {v <- goA(ass)} yield vpr.Exhale(v)(pos, info, errT)

      case fold: in.Fold => for {a <- ctx.predicate.predicateAccess(fold.op)(ctx) } yield vpr.Fold(a)(pos, info, errT)
      case unfold: in.Unfold => for { a <- ctx.predicate.predicateAccess(unfold.op)(ctx) } yield vpr.Unfold(a)(pos, info, errT)

      case in.Return() => unit(vpr.Goto(Names.returnLabel)(pos, info, errT))

      case _ => Violation.violation(s"Statement $x did not match with any implemented case.")
    }

    vprStmt map (s => stmtComment(x, s))
  }

  private def viperTarget(x: vpr.Exp): (vpr.LocalVar, Option[(vpr.LocalVarDecl, vpr.AbstractAssign)]) = {
    x match {
      case x: vpr.LocalVar => (x, None)
      case _ =>
        val decl = vpr.LocalVarDecl(Names.freshName, x.typ)(x.pos, x.info, x.errT)
        val ass  = vu.valueAssign(x, decl.localVar)(x.pos, x.info, x.errT)
        (decl.localVar, Some((decl, ass)))
    }
  }

  def stmtComment(x: in.Stmt, res: vpr.Stmt): vpr.Stmt = {
    x match {
      case _: in.Seqn => res
      case _ => Comments.prependComment(x.formattedShort, res)
    }
  }

  def blockDecl(x: in.BlockDeclaration)(ctx: Context): vpr.Declaration = {
    x match {
      case x: in.BodyVar => ctx.typeEncoding.variable(ctx)(x)
    }
  }


}
