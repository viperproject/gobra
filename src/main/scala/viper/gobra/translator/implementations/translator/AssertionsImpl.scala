package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.silver.verifier.{errors => vprerr}
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{DefaultErrorBackTranslator, LoopInvariantNotWellFormedError, MethodContractNotWellFormedError, Source}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Assertions
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}


class AssertionsImpl extends Assertions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(ass: in.Assertion)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = ass.vprMeta

    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    val ret = ass match {
      case in.SepAnd(l, r) => for {vl <- goA(l); vr <- goA(r)} yield vpr.And(vl, vr)(pos, info, errT)
      case in.ExprAssertion(e) => goE(e)
      case in.Implication(l, r) => for {vl <- goE(l); vr <- goA(r)} yield vpr.Implies(vl, vr)(pos, info, errT)
      case acc: in.Access => ctx.loc.access(acc)(ctx)

      case in.SepForall(vars, triggers, body) => {
        val (decls, _) = vars.map(ctx.loc.parameter(_)(ctx)).unzip
        val newVars = decls.flatten

        for {
          newTriggers <- sequence(triggers map (ctx.expr.trigger(_)(ctx)))
          newBody <- goA(body)
          newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT)
          desugaredForall = vpr.utility.QuantifiedPermissions.desugarSourceQuantifiedPermissionSyntax(newForall)
          reducedForall = desugaredForall.reduce[vpr.Exp] { (a, b) => vpr.And(a, b)(pos, info, errT) }
        } yield reducedForall
      }
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

  override def precondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp] = assumeExp(contract(x)(ctx))

  override def postcondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp] = assertExp(contract(x)(ctx))
}
