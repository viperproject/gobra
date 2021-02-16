// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
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
    * [acc(  p(as), perm] -> p(Argument[as], Permission[perm])
    * [acc(e.p(as), perm] -> p(Argument[e], Argument[as], Permission[perm])
    */
  override def predicateAccess(ctx: Context): (in.PredicateAccess, in.Expr) ==> CodeWriter[vpr.PredicateAccessPredicate] = {
    case (acc@ in.FPredicateAccess(pred, args), perm) =>
      val (pos, info, errT) = acc.vprMeta
      for {
        vArgs <- cl.sequence(args map (ctx.expr.translate(_)(ctx)))
        pacc = vpr.PredicateAccess(vArgs, pred.name)(pos, info, errT)
        vPerm <- ctx.typeEncoding.expr(ctx)(perm)
      } yield vpr.PredicateAccessPredicate(pacc, vPerm)(pos, info, errT)

    case (acc@ in.MPredicateAccess(recv, pred, args), perm) =>
      val (pos, info, errT) = acc.vprMeta
      for {
        vRecv <- ctx.expr.translate(recv)(ctx)
        vArgs <- cl.sequence(args map (ctx.expr.translate(_)(ctx)))
        pacc = vpr.PredicateAccess(vRecv +: vArgs, pred.uniqueName)(pos, info, errT)
        vPerm <- ctx.typeEncoding.expr(ctx)(perm)
      } yield vpr.PredicateAccessPredicate(pacc, vPerm)(pos, info, errT)
  }

  /** Returns proxy(args) */
  override def proxyAccess(proxy: in.PredicateProxy, args: Vector[in.Expr], perm: in.Expr)(src: Source.Parser.Info)(ctx: Context): CodeWriter[vpr.PredicateAccessPredicate] = {
    val predicateInstance = proxy match {
      case proxy: in.FPredicateProxy => in.Access(in.Accessible.Predicate(in.FPredicateAccess(proxy, args)(src)), perm)(src)
      case proxy: in.MPredicateProxy => in.Access(in.Accessible.Predicate(in.MPredicateAccess(args.head, proxy, args.tail)(src)), perm)(src)
    }
    ctx.ass.translate(predicateInstance)(ctx).map(_.asInstanceOf[vpr.PredicateAccessPredicate])
  }
}
