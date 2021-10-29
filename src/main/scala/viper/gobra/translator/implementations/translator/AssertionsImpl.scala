// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.silver.verifier.{errors => vprerr}
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{DefaultErrorBackTranslator, LoopInvariantNotWellFormedError, MethodContractNotWellFormedError, Source}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Assertions
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}


class AssertionsImpl extends Assertions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => MemL}

  override def finalize(col: Collector): Unit = ()

  override def translate(ass: in.Assertion)(ctx: Context): CodeWriter[vpr.Exp] = {

    val typEncodingOptRes = ctx.typeEncoding.assertion(ctx).lift(ass)
    if (typEncodingOptRes.isDefined) return typEncodingOptRes.get

    val (pos, info, errT) = ass.vprMeta

    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)

    val ret = ass match {
      case in.SepAnd(l, r) => for {vl <- goA(l); vr <- goA(r)} yield vpr.And(vl, vr)(pos, info, errT)
      case in.ExprAssertion(e) => goE(e)
      case in.Implication(l, r) => for {vl <- goE(l); vr <- goA(r)} yield vpr.Implies(vl, vr)(pos, info, errT)
      case acc: in.Access =>
        acc.e match {
          case in.Accessible.Predicate(op) => ctx.predicate.predicateAccess(ctx)(op, acc.p)
          case in.Accessible.Address(op) => ctx.typeEncoding.addressFootprint(ctx)(op, acc.p)
          case in.Accessible.ExprAccess(op) =>
            op.typ match {
              case _: in.MapT => ctx.typeEncoding.assertion(ctx)(acc)
              case _ => Violation.violation(s"unexpected expression $op in an access predicate")
            }
          case n => Violation.violation(s"node $n should have been handled by an type encoding.")
        }

      case in.SepForall(vars, triggers, body) =>
        val newVars = vars map ctx.typeEncoding.variable(ctx)

        for {
          newTriggers <- sequence(triggers map (ctx.expr.trigger(_)(ctx)))
          newBody <- goA(body)
          newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT)
          desugaredForall = vpr.utility.QuantifiedPermissions.desugarSourceQuantifiedPermissionSyntax(newForall)
          triggeredForall = desugaredForall.map(_.autoTrigger)
          reducedForall = triggeredForall.reduce[vpr.Exp] { (a, b) => vpr.And(a, b)(pos, info, errT) }
        } yield reducedForall

      case _ => Violation.violation(s"Assertion $ass did not match with any implemented case.")
    }

    ret
  }

  override def invariant(x: in.Assertion)(ctx: Context): (CodeWriter[Unit], vpr.Exp) = {
    def invErr(inv: vpr.Exp): ErrorTransformer = {
      case e@ vprerr.ContractNotWellformed(Source(info), reason, _) if e causedBy inv =>
        LoopInvariantNotWellFormedError(info)
          .dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
    }

    val invWithErrorT = for {
      inv <- translate(x)(ctx)
      _ <- errorT(invErr(inv))
    } yield inv

    invWithErrorT.cut.swap
  }

  def contract(x: in.Assertion)(ctx: Context): CodeWriter[vpr.Exp] = {
    def contractErr(inv: vpr.Exp): ErrorTransformer = {
      case e@ vprerr.ContractNotWellformed(Source(info), reason, _) if e causedBy inv =>
        MethodContractNotWellFormedError(info)
          .dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
    }

    for {
      contract <- translate(x)(ctx)
      _ <- errorT(contractErr(contract))
    } yield contract
  }

  override def precondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp] = MemL.pure(contract(x)(ctx))(ctx)

  override def postcondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp] = MemL.pure(contract(x)(ctx))(ctx)
}
