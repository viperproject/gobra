package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.{StmtWriter, ExprWriter}
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  import viper.gobra.translator.util.ViperWriter.{ExprLevel => el}
  import viper.gobra.translator.util.ViperWriter.StmtLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Stmt)(ctx: Context): StmtWriter[vpr.Stmt] = withDeepInfo(x){


    def goS(s: in.Stmt): StmtWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): ExprWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): ExprWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {
      case in.Block(vars, stmts) => block{
        for {
          (declsWithPre, nextCtx) <- sequence(ctx)(vars map { v => (c: Context) =>
            val (w, nc) = ctx.loc.declaration(v)(c).isolate(identity)
            stmtSplitE(w).map((_, nc))
          })
          (pre, decls) = declsWithPre.unzip
          body <- sequence(stmts map ctx.stmt.translateF(nextCtx))
        } yield vpr.Seqn(pre ++ body, decls)()
      }

      case in.Seqn(stmts) => sequence(stmts map goS) map (vpr.Seqn(_, Vector.empty)())

      case ass: in.SingleAss => ctx.loc.assignment(ass)(ctx)

      case in.MultiAss(left, right) => ??? // TODO

      case in.Assert(ass) => seqnE(for {v <- goA(ass)} yield vpr.Assert(v)())
      case in.Assume(ass) => seqnE(for {v <- goA(ass)} yield vpr.Assume(v)())
      case in.Inhale(ass) => seqnE(for {v <- goA(ass)} yield vpr.Inhale(v)())
      case in.Exhale(ass) => seqnE(for {v <- goA(ass)} yield vpr.Exhale(v)())

      case in.Return() => unit(vpr.Goto(Names.returnLabel)())


    }
  }


}
