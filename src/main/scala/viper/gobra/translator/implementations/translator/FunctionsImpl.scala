package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.Functions
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}

class FunctionsImpl extends Functions {

  import viper.gobra.translator.util.ViperWriter.{StmtLevel => sl, _}
  import MemberLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Function)(ctx: Context): MemberWriter[vpr.Method] = withDeepInfo(x){

      def declInit[R <: in.TopDeclaration](ds: Vector[R])(ctx: Context)
      : MemberWriter[((Vector[vpr.LocalVarDecl], Vector[sl.Writer[vpr.Stmt]]), Context)] =
        sequence(ctx)(ds map ctx.loc.topDecl).map{ case (declWithW, c) => (declWithW.unzip, c) }

      def clauseInit[R](ws: Vector[MemberWriter[(R, sl.Writer[vpr.Stmt])]]): MemberWriter[(Vector[R], Vector[sl.Writer[vpr.Stmt]])] =
        sequence(ws).map{ _.unzip }

      for {
        ((args, argW), ctx2) <- declInit(x.args)(ctx)
        ((res, resW), ctx3) <- declInit(x.results)(ctx2)

        (pres, presW) <- clauseInit(x.pres map (ctx.ass.precondition(_)(ctx3)))
        (posts, postW) <- clauseInit(x.posts map (ctx.ass.postcondition(_)(ctx3)))

        body <- option(x.body.map{ b => blockS{
          for {
            pres <- sl.sequence(argW ++ argW ++ presW ++ postW)
            core <- ctx.stmt.translate(b)(ctx3)
          } yield vpr.Seqn(pres :+ core, Vector.empty)()
        }})

        method = vpr.Method(
          name = x.name,
          formalArgs = args,
          formalReturns = res,
          pres = pres,
          posts = posts,
          body = body
        )()

      } yield method
  }
}
