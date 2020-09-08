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

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = ()

  override def pureMethod(meth: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function] = {
    require(meth.results.size == 1)

    val (pos, info, errT) = meth.vprMeta

    val (vRecv, recvWells) = ctx.loc.parameter(meth.receiver)(ctx)
    val recvWell = cl.assertUnit(recvWells)

    val (vArgss, argWells) = meth.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = argWells map cl.assertUnit

    val (vResult, resultWells) = ctx.loc.parameter(meth.results.head)(ctx)
    val resultType = if (vResult.size == 1) vResult.head.typ else ctx.tuple.typ(vResult map (_.typ))

    for {
      pres <- sequence((recvWell +: argWell) ++ meth.pres.map(ctx.ass.precondition(_)(ctx)))

      body <- option(meth.body map {b =>
        cl.assumeExp(
          for {
            _ <- resultWells
            results <- cl.sequence(ctx.loc.values(b)(ctx))
            res = if (results.size == 1) results.head else ctx.tuple.create(results)
          } yield res
        )
      })

      function = vpr.Function(
        name = meth.name.uniqueName,
        formalArgs = vRecv ++ vArgs,
        typ = resultType,
        pres = pres,
        posts = Vector.empty,
        body = body
      )(pos, info, errT)

    } yield function
  }


  override def pureFunction(func: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function] = {
    require(func.results.size == 1)

    val (pos, info, errT) = func.vprMeta

    val (vArgss, argWells) = func.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = argWells map cl.assertUnit

    val (vResult, resultWells) = ctx.loc.parameter(func.results.head)(ctx)
    val resultType = if (vResult.size == 1) vResult.head.typ else ctx.tuple.typ(vResult map (_.typ))

    for {
      pres <- sequence(argWell ++ func.pres.map(ctx.ass.precondition(_)(ctx)))

      body <- option(func.body map {b =>
        cl.assumeExp(
          for {
            _ <- resultWells
            results <- cl.sequence(ctx.loc.values(b)(ctx))
            res = if (results.size == 1) results.head else ctx.tuple.create(results)
          } yield res
        )
      })

      function = vpr.Function(
        name = func.name.name,
        formalArgs = vArgs,
        typ = resultType,
        pres = pres,
        posts = Vector.empty,
        body = body
      )(pos, info, errT)

    } yield function
  }


}
