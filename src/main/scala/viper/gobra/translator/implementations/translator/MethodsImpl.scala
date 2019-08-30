package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Methods
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class MethodsImpl extends Methods {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Method)(ctx: Context): MemberWriter[vpr.Method] = withDeepInfo(x){

    val ((vRecv, recvInit), interCtx1) = ctx.loc.topDecl(x.receiver)(ctx)

    val (argsWithInits, interCtx2) = chain(x.args map ctx.loc.topDecl)(interCtx1)
    val (vArgss, argInits) = argsWithInits.unzip
    val vArgs = vArgss.flatten

    val (resultsWithIntis, newCtx) = chain(x.results map ctx.loc.topDecl)(interCtx2)
    val (vResultss, resultInits) = resultsWithIntis.unzip
    val vResults = vResultss.flatten

    for {
      pres <- sequence(x.pres map (ctx.ass.precondition(_)(newCtx)))
      posts <- sequence(x.posts map (ctx.ass.postcondition(_)(newCtx)))

      returnLabel = vpr.Label(Names.returnLabel, Vector.empty)()

      body <- option(x.body.map{ b => block{
        for {
          _ <- cl.global(vRecv ++ vArgs ++ vResults: _*)
          vInits <- cl.sequence(Vector(recvInit) ++ argInits ++ resultInits)
          _ <- cl.global(returnLabel)
          core <- ctx.stmt.translate(b)(newCtx)
        } yield vu.seqn(vInits :+ core)
      }})

      method = vpr.Method(
        name = x.name,
        formalArgs = vRecv ++ vArgs,
        formalReturns = vResults,
        pres = pres,
        posts = posts,
        body = body
      )()

    } yield method
  }
}
