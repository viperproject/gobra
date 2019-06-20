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

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Stmt)(ctx: Context): StmtWriter[vpr.Stmt] = {


    def goS(s: in.Stmt): StmtWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): ExprWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): ExprWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    val src = x.src.vprSrc

    x match {
      case in.Block(vars, stmts) => // TODO: maybe eliminate block variables
        val pre = ExprWriter.sequence(
          vars.map(v => ctx.expr.toLocalVar(v)(ctx) map ViperUtil.toVarDecl)
        )

        val bod = sequence(stmts map goS)

        unit(vpr.Seqn(
          pre.written ++ bod.res,
          pre.local ++ pre.global ++ bod.global
        )(src))

      case in.Seq(stmts) => sequence(stmts map goS) map (vpr.Seqn(_, Vector.empty)(src))

      case in.SingleAss(left, right) =>
        (for {
          r <- goE(right)
          ass <- ctx.loc.assignment(left, r)(ctx)(x.src).open
        } yield ass).close

      case in.Assert(ass) => (for {v <- goA(ass)} yield vpr.Assert(v)(src)).close
      case in.Assume(ass) => (for {v <- goA(ass)} yield vpr.Assume(v)(src)).close
      case in.Inhale(ass) => (for {v <- goA(ass)} yield vpr.Inhale(v)(src)).close
      case in.Exhale(ass) => (for {v <- goA(ass)} yield vpr.Exhale(v)(src)).close

      case in.Return() => unit(vpr.Goto(Names.returnLabel)(src))


    }
  }


}
