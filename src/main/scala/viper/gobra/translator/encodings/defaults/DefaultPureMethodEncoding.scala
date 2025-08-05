// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.defaults

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.VprInfo
import viper.silver.{ast => vpr}

class DefaultPureMethodEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter._
  import MemberLevel._

  override def function(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = {
    case x: in.PureMethod => pureMethodDefault(x)(ctx)
    case x: in.PureFunction => pureFunctionDefault(x)(ctx)
  }

  def pureMethodDefault(meth: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function] = {
    require(meth.results.size == 1)

    val (pos, info, errT) = meth.vprMeta
    val infoWithAnnotations = VprInfo.maybeAttachHyperFunc(
      VprInfo.maybeAttachOpaque(info, meth.isOpaque),
      meth.isHyper,
    )

    val vRecv = ctx.variable(meth.receiver)
    val vRecvPres = ctx.varPrecondition(meth.receiver).toVector

    val vArgs = meth.args.map(ctx.variable)
    val vArgPres = meth.args.flatMap(ctx.varPrecondition)

    val vResults = meth.results.map(ctx.variable)
    val vResultPosts = meth.results.flatMap(ctx.varPostcondition)
    assert(vResults.size == 1)
    val resultType = if (vResults.size == 1) vResults.head.typ else ctx.tuple.typ(vResults map (_.typ))

    val fixResultvar = (x: vpr.Exp) => {
      x.transform { case v: vpr.LocalVar if v.name == meth.results.head.id => vpr.Result(resultType)() }
    }

    for {
      pres <- sequence((vRecvPres ++ vArgPres) ++ meth.pres.map(ctx.precondition))
      posts <- sequence(vResultPosts ++ meth.posts.map(ctx.postcondition(_).map(fixResultvar(_))))
      measures <- sequence(meth.terminationMeasures.map(e => pure(ctx.assertion(e))(ctx)))

      body <- option(meth.body map { b =>
        pure(
          for {
            results <- ctx.expression(b)
          } yield results
        )(ctx)
      })

      annotatedInfo = VprInfo.attachAnnotations(meth.backendAnnotations, infoWithAnnotations)

      function = vpr.Function(
        name = meth.name.uniqueName,
        formalArgs = vRecv +: vArgs,
        typ = resultType,
        pres = pres ++ measures,
        posts = posts,
        body = body
      )(pos, annotatedInfo, errT)

    } yield function
  }

  def pureFunctionDefault(func: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function] = {
    require(func.results.size == 1)

    val (pos, info, errT) = func.vprMeta
    val infoWithAnnotations = VprInfo.maybeAttachHyperFunc(
      VprInfo.maybeAttachOpaque(info, func.isOpaque),
      func.isHyper,
    )

    val vArgs = func.args.map(ctx.variable)
    val vArgPres = func.args.flatMap(ctx.varPrecondition)

    val vResults = func.results.map(ctx.variable)
    val vResultPosts = func.results.flatMap(ctx.varPostcondition)
    assert(vResults.size == 1)
    val resultType = if (vResults.size == 1) vResults.head.typ else ctx.tuple.typ(vResults map (_.typ))

    val fixResultvar = (x: vpr.Exp) => {
      x.transform { case v: vpr.LocalVar if v.name == func.results.head.id => vpr.Result(resultType)() }
    }

    for {
      pres <- sequence(vArgPres ++ func.pres.map(ctx.precondition))
      posts <- sequence(vResultPosts ++ func.posts.map(ctx.postcondition(_).map(fixResultvar(_))))
      measures <- sequence(func.terminationMeasures.map(e => pure(ctx.assertion(e))(ctx)))

      body <- option(func.body map { b =>
        pure(
          for {
            results <- ctx.expression(b)
          } yield results
        )(ctx)
      })

      annotatedInfo = VprInfo.attachAnnotations(func.backendAnnotations, infoWithAnnotations)

      function = vpr.Function(
        name = func.name.name,
        formalArgs = vArgs,
        typ = resultType,
        pres = pres ++ measures,
        posts = posts,
        body = body
      )(pos, annotatedInfo, errT)

    } yield function
  }
}
