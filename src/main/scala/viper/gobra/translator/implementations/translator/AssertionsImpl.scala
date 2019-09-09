package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Assertions
import viper.gobra.translator.util.ViperWriter.{MemberWriter, CodeWriter}
import viper.silver.{ast => vpr}


class AssertionsImpl extends Assertions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(ass: in.Assertion)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(ass){

    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    ass match {
      case in.SepAnd(l, r) => for {vl <- goA(l); vr <- goA(r)} yield vpr.And(vl, vr)()
      case in.ExprAssertion(e) => goE(e)
      case in.Implication(l, r) => for {vl <- goE(l); vr <- goA(r)} yield vpr.Implies(vl, vr)()
      case acc: in.Access => ctx.loc.access(acc)(ctx)
    }
  }

  override def invariant(x: in.Assertion)(ctx: Context): (CodeWriter[Unit], vpr.Exp) = {
    translate(x)(ctx).cut.swap
  }

  override def precondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp] = assumeExp(translate(x)(ctx))

  override def postcondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp] = assertExp(translate(x)(ctx))
}
