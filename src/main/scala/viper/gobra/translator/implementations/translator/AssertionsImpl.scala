package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Assertions
import viper.gobra.util.ViperWriter.ExprWriter
import viper.silver.{ast => vpr}


class AssertionsImpl extends Assertions {

  override def finalize(col: Collector): Unit = ()

  override def translate(ass: in.Assertion)(ctx: Context): ExprWriter[vpr.Exp] = {

    def goA(a: in.Assertion): ExprWriter[vpr.Exp] = translate(a)(ctx)
    def goE(e: in.Expr): ExprWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    ass match {
      case in.Star(l, r) => for {vl <- goA(l); vr <- goA(r)} yield vpr.Add(vl, vr)()
      case in.ExprAssertion(e) => goE(e)
      case in.Implication(l, r) => for {vl <- goE(l); vr <- goA(r)} yield vpr.Implies(vl, vr)()
      case acc: in.Access => access(acc)(ctx)
    }
  }.withInfo(ass)

  def access(acc: in.Access)(ctx: Context): ExprWriter[vpr.AccessPredicate] = {
    acc.e match {
      case in.Accessible.Ref(der) =>
        for {loc <- ctx.expr.toFieldAcc(der)(ctx)} yield vpr.FieldAccessPredicate(loc, vpr.FullPerm()())()
    }
  }.withInfo(acc)

  private def specification(x: in.Assertion)(ctx: Context): vpr.Exp = {
    val vExp = translate(x)(ctx)
    assert(vExp.isEmpty, s"Encoding of assertion $x requries statements which is currently not supported")
    vExp.res
  }

  override def precondition(x: in.Assertion)(ctx: Context): vpr.Exp = specification(x)(ctx)

  override def postcondition(x: in.Assertion)(ctx: Context): vpr.Exp = specification(x)(ctx)
}
