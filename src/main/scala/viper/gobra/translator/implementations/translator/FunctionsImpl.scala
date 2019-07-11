package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.Functions
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}

class FunctionsImpl extends Functions {

  import viper.gobra.translator.util.ViperWriter.{ExprLevel => el, StmtLevel => sl, _}
  import MemberLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Function)(ctx: Context): MemberWriter[vpr.Method] = withDeepInfo(x){

      def init[R](ws: Vector[ExprWriter[R]]): MemberWriter[(StmtSum, (vpr.Seqn, Vector[R]))] =
        memberS(sl.stmtSplitE(el.sequence(ws)))

      def declInit[R](ctx: Context)(ws: )

      for {
        (argsSS, (argsPre, args)) <- init(x.args map (ctx.loc.formalArg(_)(ctx)))
        (ressSS, (ressPre, ress)) <- init(x.results map (ctx.loc.formalRes(_)(ctx)))

        (presSS, (presPre, pres)) <- init(x.pres map (ctx.ass.precondition(_)(ctx)))
        (possSS, (possPre, poss)) <- init(x.posts map (ctx.ass.postcondition(_)(ctx)))

        body <- option(x.body.map{ b => blockE{
          for {
            nextCtx <- el.addGlobals(ctx, argsSS.global ++ ressSS.global ++ presSS.global ++ possSS.global: _*)
            _ <- el.addStatements(argsPre, ressPre, presPre, possPre)
            core <- el.exprS(ctx.stmt.translate(b)(nextCtx))
          } yield core
        }})

        method = vpr.Method(
          name = x.name,
          formalArgs = args,
          formalReturns = ress,
          pres = pres,
          posts = poss,
          body = body
        )()

      } yield method
  }
}
