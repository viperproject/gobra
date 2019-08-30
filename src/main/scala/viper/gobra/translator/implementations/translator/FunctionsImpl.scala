package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Functions
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class FunctionsImpl extends Functions {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Function)(ctx: Context): MemberWriter[vpr.Method] = withDeepInfo(x){

    val (argsWithInits, interCtx) = chain(x.args map ctx.loc.topDecl)(ctx)
    val (vArgss, argInits) = argsWithInits.unzip
    val vArgs = vArgss.flatten

    val (resultsWithIntis, newCtx) = chain(x.results map ctx.loc.topDecl)(interCtx)
    val (vResultss, resultInits) = resultsWithIntis.unzip
    val vResults = vResultss.flatten

    for {
      pres <- sequence(x.pres map (ctx.ass.precondition(_)(newCtx)))
      posts <- sequence(x.posts map (ctx.ass.postcondition(_)(newCtx)))

      returnLabel = vpr.Label(Names.returnLabel, Vector.empty)()

      body <- option(x.body.map{ b => block{
        for {
          _ <- cl.global(vArgs ++ vResults: _*)
          vInits <- cl.sequence(argInits ++ resultInits)
          _ <- cl.global(returnLabel)
          core <- ctx.stmt.translate(b)(newCtx)
        } yield vu.seqn(vInits :+ core)
      }})

      method = vpr.Method(
        name = x.name,
        formalArgs = vArgs,
        formalReturns = vResults,
        pres = pres,
        posts = posts,
        body = body
      )()

    } yield method
  }
}
