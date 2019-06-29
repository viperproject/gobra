package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperUtil
import viper.gobra.util.ViperWriter.{ExprWriter, StmtWriter}
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  import StmtWriter._
  import viper.gobra.reporting.Source.RichViperNode

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Stmt)(ctx: Context): StmtWriter[vpr.Stmt] = {


    def goS(s: in.Stmt): StmtWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): ExprWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): ExprWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {
      case in.Block(vars, stmts) => // TODO: maybe eliminate block variables
        val pre = ExprWriter.sequence(
          vars.map(v => ctx.expr.toLocalVar(v)(ctx) map ViperUtil.toVarDecl)
        )

        val bod = sequence(stmts map goS)

        unit(vpr.Seqn(
          pre.written ++ bod.res,
          pre.local ++ pre.global ++ bod.global
        )())

      case in.Seqn(stmts) => sequence(stmts map goS) map (vpr.Seqn(_, Vector.empty)())

      case in.SingleAss(left, right) =>
        (for {
          r <- goE(right)
          ass <- ctx.loc.assignment(left, r)(ctx)(x).open
        } yield ass).withInfo(x).close

      case in.MultiAss(left, right) => ??? // TODO

      case in.Assert(ass) => (for {v <- goA(ass)} yield vpr.Assert(v)().withInfo(x)).close
      case in.Assume(ass) => (for {v <- goA(ass)} yield vpr.Assume(v)().withInfo(x)).close
      case in.Inhale(ass) => (for {v <- goA(ass)} yield vpr.Inhale(v)().withInfo(x)).close
      case in.Exhale(ass) => (for {v <- goA(ass)} yield vpr.Exhale(v)().withInfo(x)).close

      case in.Return() => unit(vpr.Goto(Names.returnLabel)())


    }
  }.withInfo(x)


}
