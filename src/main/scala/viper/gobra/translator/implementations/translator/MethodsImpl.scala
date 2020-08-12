package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Methods
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.ast.Method
import viper.silver.{ast => vpr}

class MethodsImpl extends Methods {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def finalize(col: Collector): Unit = ()

  override def method(x: in.Method)(ctx: Context): MemberWriter[Method] = {
    val (pos, info, errT) = x.vprMeta

    val (vRecv, recvWells) = ctx.loc.parameter(x.receiver)(ctx)
    val recvWell = cl.assertUnit(recvWells)

    val (vArgss, argWells) = x.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = argWells map cl.assertUnit

    val (vResultss, resultWells) = x.results.map(ctx.loc.localDecl(_)(ctx)).unzip
    val vResults = vResultss.flatten.asInstanceOf[Vector[vpr.LocalVarDecl]]
    val resultWell = resultWells map cl.assertUnit
    val resultInit = cl.seqns(x.results map (ctx.loc.initialize(_)(ctx)))

    for {
      pres <- sequence((recvWell +: argWell) ++ x.pres.map(ctx.ass.precondition(_)(ctx)))
      posts <- sequence(resultWell ++ x.posts.map(ctx.ass.postcondition(_)(ctx)))

      returnLabel = vpr.Label(Names.returnLabel, Vector.empty)(pos, info, errT)

      body <- option(x.body.map{ b => block{
        for {
          init <- resultInit
          _ <- cl.global(returnLabel)
          core <- ctx.stmt.translate(b)(ctx)
        } yield vu.seqn(Vector(init, core, returnLabel))(pos, info, errT)
      }})

      method = vpr.Method(
        name = x.name.uniqueName,
        formalArgs = vRecv ++ vArgs,
        formalReturns = vResults,
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield method
  }


  override def function(x: in.Function)(ctx: Context): MemberWriter[Method] = {
    assert(x.info.origin.isDefined, s"$x has no defined source")

    val (pos, info, errT) = x.vprMeta

    val (vArgss, argWells) = x.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = argWells map cl.assertUnit

    val (vResultss, resultWells) = x.results.map(ctx.loc.outparameter(_)(ctx)).unzip
    val vResults = vResultss.flatten
    val resultWell = resultWells map cl.assertUnit
    val resultInit = cl.seqns(x.results map (ctx.loc.initialize(_)(ctx)))

    for {
      pres <- sequence(argWell ++ x.pres.map(ctx.ass.precondition(_)(ctx)))
      posts <- sequence(resultWell ++ x.posts.map(ctx.ass.postcondition(_)(ctx)))

      returnLabel = vpr.Label(Names.returnLabel, Vector.empty)(pos, info, errT)

      body <- option(x.body.map{ b => block{
        for {
          init <- resultInit
          _ <- cl.global(returnLabel)
          core <- ctx.stmt.translate(b)(ctx)
        } yield vu.seqn(Vector(init, core, returnLabel))(pos, info, errT)
      }})

      method = vpr.Method(
        name = x.name.name,
        formalArgs = vArgs,
        formalReturns = vResults,
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield method
  }
}
