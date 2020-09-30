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
import viper.gobra.util.Violation
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

    val vRecv = ctx.typeEncoding.variable(ctx)(pred.receiver)
    val vRecvPres = sequence(ctx.typeEncoding.precondition(ctx).lift(pred.receiver).toVector)

    val vArgs = pred.args.map(ctx.typeEncoding.variable(ctx))
    val vArgPres = sequence(pred.args.flatMap(ctx.typeEncoding.precondition(ctx).lift(_)))

    val body = option(pred.body map {b =>
      for {
        rwc <- vRecvPres
        awc <- vArgPres
        vBody <- ctx.ass.postcondition(b)(ctx)
      } yield vu.bigAnd(rwc ++ awc ++ Vector(vBody))(pos, info, errT)
    })

    for {
      vBody <- body

      predicate = vpr.Predicate(
        name = pred.name.uniqueName,
        formalArgs = vRecv +: vArgs,
        body = vBody
      )(pos, info, errT)
    } yield predicate
  }


  override def fpredicate(pred: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {

    val (pos, info, errT) = pred.vprMeta

    val vArgs = pred.args.map(ctx.typeEncoding.variable(ctx))
    val vArgPres = sequence(pred.args.flatMap(ctx.typeEncoding.precondition(ctx).lift(_)))

    val body = option(pred.body map {b =>
      for {
        wc <- vArgPres
        vBody <- ctx.ass.postcondition(b)(ctx)
      } yield vu.bigAnd(wc ++ Vector(vBody))(pos, info, errT)
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

  /**
    * [acc(  p(as)] -> p(Argument[as])
    * [acc(e.p(as)] -> p(Argument[e], Argument[as])
    */
  override def predicateAccess(acc: in.PredicateAccess)(ctx: Context): CodeWriter[vpr.PredicateAccessPredicate] = {

    val (pos, info, errT) = acc.vprMeta
    val perm = vpr.FullPerm()(pos, info, errT)

    acc match {
      case in.FPredicateAccess(pred, args) =>
        for {
          vArgs <- cl.sequence(args map (ctx.expr.translate(_)(ctx)))
          pacc = vpr.PredicateAccess(vArgs, pred.name)(pos, info, errT)
        } yield vpr.PredicateAccessPredicate(pacc, perm)(pos, info, errT)

      case in.MPredicateAccess(recv, pred, args) =>
        for {
          vRecv <- ctx.expr.translate(recv)(ctx)
          vArgs <- cl.sequence(args map (ctx.expr.translate(_)(ctx)))
          pacc = vpr.PredicateAccess(vRecv +: vArgs, pred.uniqueName)(pos, info, errT)
        } yield vpr.PredicateAccessPredicate(pacc, perm)(pos, info, errT)

      case in.MemoryPredicateAccess(_) =>
        Violation.violation("memory predicate accesses are not supported")
    }
  }
}
