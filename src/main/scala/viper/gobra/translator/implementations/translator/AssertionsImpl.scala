package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Assertions
import viper.gobra.translator.util.ViperWriter.{ExprWriter, MemberWriter, StmtWriter}
import viper.silver.{ast => vpr}


class AssertionsImpl extends Assertions {

  import viper.gobra.translator.util.ViperWriter.ExprLevel._
  import viper.gobra.translator.util.ViperWriter.{StmtLevel => sl, MemberLevel => ml}

  override def finalize(col: Collector): Unit = ()

  override def translate(ass: in.Assertion)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(ass){

    def goA(a: in.Assertion): ExprWriter[vpr.Exp] = translate(a)(ctx)
    def goE(e: in.Expr): ExprWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    ass match {
      case in.SepAnd(l, r) => for {vl <- goA(l); vr <- goA(r)} yield vpr.Add(vl, vr)()
      case in.ExprAssertion(e) => goE(e)
      case in.Implication(l, r) => for {vl <- goE(l); vr <- goA(r)} yield vpr.Implies(vl, vr)()
      case acc: in.Access => ctx.loc.access(acc)(ctx)
    }
  }

  private def specification(x: in.Assertion)(ctx: Context): MemberWriter[(vpr.Exp, StmtWriter[vpr.Stmt])] = {
    ml.splitE(translate(x)(ctx)).map{ case (e, w) => (e, sl.closeE(w)(e))}
  }

  override def precondition(x: in.Assertion)(ctx: Context): MemberWriter[(vpr.Exp, StmtWriter[vpr.Stmt])] = specification(x)(ctx)

  override def postcondition(x: in.Assertion)(ctx: Context): MemberWriter[(vpr.Exp, StmtWriter[vpr.Stmt])] = specification(x)(ctx)
}
