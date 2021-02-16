// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.PureMethods
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}

class PureMethodsImpl extends PureMethods {

  import viper.gobra.translator.util.ViperWriter._
  import MemberLevel._

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = ()

  override def pureMethod(meth: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function] = {
    require(meth.results.size == 1)

    val (pos, info, errT) = meth.vprMeta

    val vRecv = ctx.typeEncoding.variable(ctx)(meth.receiver)
    val vRecvPres = ctx.typeEncoding.precondition(ctx).lift(meth.receiver).toVector

    val vArgs = meth.args.map(ctx.typeEncoding.variable(ctx))
    val vArgPres = meth.args.flatMap(ctx.typeEncoding.precondition(ctx).lift(_))

    val vResults = meth.results.map(ctx.typeEncoding.variable(ctx))
    val vResultPosts = meth.results.flatMap(ctx.typeEncoding.postcondition(ctx).lift(_))
    assert(vResults.size == 1)
    val resultType = if (vResults.size == 1) vResults.head.typ else ctx.tuple.typ(vResults map (_.typ))

    val fixResultvar = (x: vpr.Exp) => {
      x.transform { case v: vpr.LocalVar if v.name == meth.results.head.id => vpr.Result(resultType)() }
    }

    for {
      pres <- sequence((vRecvPres ++ vArgPres) ++ meth.pres.map(ctx.ass.precondition(_)(ctx)))
      posts <- sequence(vResultPosts ++ meth.posts.map(ctx.ass.postcondition(_)(ctx).map(fixResultvar(_))))

      body <- option(meth.body map { b =>
        pure(
          for {
            results <- ctx.expr.translate(b)(ctx)
          } yield results
        )(ctx)
      })

      function = vpr.Function(
        name = meth.name.uniqueName,
        formalArgs = vRecv +: vArgs,
        typ = resultType,
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield function
  }

  override def pureFunction(func: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function] = {
    require(func.results.size == 1)

    val (pos, info, errT) = func.vprMeta

    val vArgs = func.args.map(ctx.typeEncoding.variable(ctx))
    val vArgPres = func.args.flatMap(ctx.typeEncoding.precondition(ctx).lift(_))

    val vResults = func.results.map(ctx.typeEncoding.variable(ctx))
    val vResultPosts = func.results.flatMap(ctx.typeEncoding.postcondition(ctx).lift(_))
    assert(vResults.size == 1)
    val resultType = if (vResults.size == 1) vResults.head.typ else ctx.tuple.typ(vResults map (_.typ))

    val fixResultvar = (x: vpr.Exp) => {
      x.transform { case v: vpr.LocalVar if v.name == func.results.head.id => vpr.Result(resultType)() }
    }

    for {
      pres <- sequence(vArgPres ++ func.pres.map(ctx.ass.precondition(_)(ctx)))
      posts <- sequence(vResultPosts ++ func.posts.map(ctx.ass.postcondition(_)(ctx).map(fixResultvar(_))))

      body <- option(func.body map { b =>
        pure(
          for {
            results <- ctx.expr.translate(b)(ctx)
          } yield results
        )(ctx)
      })

      function = vpr.Function(
        name = func.name.name,
        formalArgs = vArgs,
        typ = resultType,
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield function
  }

}
