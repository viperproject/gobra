// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{GoCallPreconditionReason, PreconditionError, Source}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{Comments, ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.{CodeWriter}
import viper.gobra.util.Violation
import viper.silver.verifier.{errors => err}
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.termination
import viper.gobra.util.Violation.violation

class StatementsImpl extends Statements {

  var counter: Int = 0

  def count: Int = {counter += 1; counter}

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = ()

  /** Clients can assume that the returned writer does not contain local variable definitions or written statements. */
  override def translate(x: in.Stmt)(ctx: Context): CodeWriter[vpr.Stmt] = {

    def result(res: CodeWriter[vpr.Stmt]): CodeWriter[vpr.Stmt] = seqn(res)

    val typEncodingOptRes = ctx.typeEncoding.statement(ctx).lift(x)
    if (typEncodingOptRes.isDefined) return result(typEncodingOptRes.get map (s => stmtComment(x, s)))


    val (pos, info, errT) = x.vprMeta

    def goS(s: in.Stmt): CodeWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)

    /**
      * Translates a go call to a function or method with pre-condition `pre` which is parameterized by
      * formal parameters `formalParams` and is instantiated with `args`
      */
    def translateGoCall(pre: Vector[in.Assertion],
                        formalParams: Vector[in.Parameter.In],
                        args: Vector[in.Expr]): Writer[vpr.Stmt] = {
      Violation.violation(
        args.length == formalParams.length,
        "number of passed arguments must match number of expected arguments"
      )

      for {
        vArgss <- sequence(args map goE)
        funcArgs <- sequence(formalParams map goE)
        substitutions = (funcArgs zip vArgss).toMap
        preCond <- sequence(pre map goA)
        preCondInstance = preCond.map{ _.replace(substitutions) }
        and = vu.bigAnd(preCondInstance)(pos, info, errT)
        exhale = vpr.Exhale(and)(pos, info, errT)
        _ <- errorT {
          case err.ExhaleFailed(Source(info), _, _) => PreconditionError(info).dueTo(GoCallPreconditionReason(info))
        }
      } yield exhale
    }

    val vprStmt: CodeWriter[vpr.Stmt] = x match {
      case in.Block(decls, stmts) =>
        val vDecls = decls map (blockDecl(_)(ctx))
        block{
          for {
            _ <- global(vDecls: _*)
            vBody <- sequence(stmts map goS)
          } yield vu.seqn(vBody)(pos, info, errT)
        }

      case in.Initialization(left) => ctx.typeEncoding.initialization(ctx)(left)

      case in.Seqn(stmts) => seqns(stmts map goS)

      case in.Label(id) =>
        unit(vpr.Label(id.name, Seq.empty)(pos, info, errT))

      case in.If(cond, thn, els) =>
          for {
            c <- goE(cond)
            t <- goS(thn)
            e <- goS(els)
          } yield vpr.If(c, vu.toSeq(t), vu.toSeq(e))(pos, info, errT)

      case in.While(cond, invs, terminationMeasure, body) =>

        for {
          (cws, vCond) <- split(goE(cond))
          (iws, vInvs) = invs.map(ctx.ass.invariant(_)(ctx)).unzip
          cpre <- seqnUnit(cws)
          ipre <- seqnUnits(iws)
          vBody <- goS(body)

          cpost = vpr.If(vCond, vu.toSeq(cpre), vu.nop(pos, info, errT))(pos, info, errT)
          ipost = ipre

          measure = terminationMeasure match {
            case Some(measure) => translateTerminationMeasure(measure)(ctx)
            case None => Seq.empty
          }

          wh = vu.seqn(Vector(
            cpre, ipre, vpr.While(vCond, vInvs ++ measure, vu.seqn(Vector(vBody, cpost, ipost))(pos, info, errT))(pos, info, errT)
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

      case in.GoFunctionCall(func, args) =>
        val funcM = ctx.lookup(func)
        translateGoCall(funcM.pres, funcM.args, args)
      case in.GoMethodCall(recv, meth, args) =>
        val methM = ctx.lookup(meth)
        translateGoCall(methM.pres, methM.receiver +: methM.args, recv +: args)
      case in.Assert(ass) => for {v <- goA(ass)} yield vpr.Assert(v)(pos, info, errT)
      case in.Assume(ass) => for {v <- goA(ass)} yield vpr.Assume(v)(pos, info, errT) // Assumes are later rewritten
      case in.Inhale(ass) => for {v <- goA(ass)} yield vpr.Inhale(v)(pos, info, errT)
      case in.Exhale(ass) => for {v <- goA(ass)} yield vpr.Exhale(v)(pos, info, errT)

      case fold: in.Fold => for {a <- ctx.ass.translate(fold.acc)(ctx) } yield vpr.Fold(a.asInstanceOf[vpr.PredicateAccessPredicate])(pos, info, errT)
      case unfold: in.Unfold => for { a <- ctx.ass.translate(unfold.acc)(ctx) } yield vpr.Unfold(a.asInstanceOf[vpr.PredicateAccessPredicate])(pos, info, errT)

      case in.Return() => unit(vpr.Goto(Names.returnLabel)(pos, info, errT))

      case _ => Violation.violation(s"Statement $x did not match with any implemented case.")
    }

    result(vprStmt) map (s => stmtComment(x, s))
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
      case l: in.LabelProxy =>
        val (pos, info, errT) = x.vprMeta
        vpr.Label(l.name, Seq.empty)(pos, info, errT)
    }
  }

  def translateTerminationMeasure(x: in.Assertion)(ctx: Context): Vector[vpr.Exp] = {
    val (pos, info, errT) = x.vprMeta
    x match {
      case in.TupleTerminationMeasure(vector) =>
        val res = vector.map(n => n match {
          case e: in.Expr => ctx.expr.translate(e)(ctx).res
          case p: in.PredicateAccess => ctx.predicate.predicate(ctx)(p).res
          case _ => violation("invalid tuple measure argument")
        })
        //val res = (vector.map(ctx.ass.translate(_)(ctx))) map getExprs
        Vector(termination.DecreasesTuple(res, None)(pos, info, errT))
      case in.WildcardMeasure() =>
        Vector(termination.DecreasesWildcard(None)(pos, info, errT))
      case in.StarMeasure() =>
        Vector(termination.DecreasesStar()(pos, info, errT))
      case in.InferTerminationMeasure() =>
        violation("Infer measure should already be hanled by internal transformation")
      case in.ConditionalTerminationMeasures(clauses) =>
        clauses.map(translateClause(_)(x)(ctx))
      case _ => violation("assertion not subtype of TerminationMeasure")
    }
  }

  def translateClause(x: in.ConditionalTerminationMeasureClause)(ass: in.Assertion)(ctx: Context): vpr.Exp = {
    val(pos, info, errT) = ass.vprMeta
    x match {
      case in.ConditionalTerminationMeasureIfClause(measure, cond) =>
        measure match {
          case in.WildcardMeasure() =>
            termination.DecreasesWildcard(Some(ctx.expr.translate(cond)(ctx).res))(pos, info, errT)
          case in.TupleTerminationMeasure(vector) =>
            val res = vector.map(n => n match {
              case e: in.Expr => ctx.expr.translate(e)(ctx).res
              case p: in.PredicateAccess => ctx.predicate.predicate(ctx)(p).res
              case _ => violation("invalid tuple measure argument")
            })
           // val res = (vector.map(ctx.ass.translate(_)(ctx))) map getExprs
            termination.DecreasesTuple(res, Some(ctx.expr.translate(cond)(ctx).res))(pos, info, errT)
          case in.StarMeasure() => violation("Star measure occurs in if clause")
          case in.InferTerminationMeasure() => violation("Infer measure occurs in if clause")
        }
      case in.StarMeasure() =>
        termination.DecreasesStar()(pos, info, errT)
    }
  }

  def getExprs(x: CodeWriter[vpr.Exp]): vpr.Exp = x.res

}
