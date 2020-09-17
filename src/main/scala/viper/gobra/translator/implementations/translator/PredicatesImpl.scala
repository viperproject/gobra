// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.Predicates
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class PredicatesImpl extends Predicates {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = ()

  override def mpredicate(pred: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {

    val (pos, info, errT) = pred.vprMeta

    val (vRecv, recvWells) = ctx.loc.parameter(pred.receiver)(ctx)
    val recvWell = cl.assertUnit(recvWells)

    val (vArgss, argWells) = pred.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = sequence(argWells map cl.assertUnit).map(vu.bigAnd(_)(pos, info, errT))

    val body = option(pred.body map {b =>
      for {
        rwc <- recvWell
        awc <- argWell
        vBody <- ctx.ass.postcondition(b)(ctx)
      } yield vu.bigAnd(Vector(rwc, awc, vBody))(pos, info, errT)
    })

    for {
      vBody <- body

      predicate = vpr.Predicate(
        name = pred.name.uniqueName,
        formalArgs = vRecv ++ vArgs,
        body = vBody
      )(pos, info, errT)
    } yield predicate
  }


  override def fpredicate(pred: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {

    val (pos, info, errT) = pred.vprMeta

    val (vArgss, argWells) = pred.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = sequence(argWells map cl.assertUnit).map(vu.bigAnd(_)(pos, info, errT))

    val body = option(pred.body map {b =>
      for {
        wc <- argWell
        vBody <- ctx.ass.postcondition(b)(ctx)
      } yield vpr.And(wc, vBody)(pos, info, errT)
    })

    for {
      vBody <- body

      predicate = vpr.Predicate(
        name = pred.name.name,
        formalArgs = vArgs,
        body = vBody
      )(pos, info, errT)

    } yield predicate
  }
}
