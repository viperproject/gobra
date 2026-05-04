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
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class DefaultPredicateEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.MemberLevel._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl}

  override def predicate(ctx: Context): in.Member ==> MemberWriter[vpr.Predicate] = {
    case x: in.MPredicate => mpredicateDefault(x)(ctx)
    case x: in.FPredicate => fpredicateDefault(x)(ctx)
  }

  def mpredicateDefault(pred: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {

    val (pos, info, errT) = pred.vprMeta

    val vRecv = ctx.variable(pred.receiver)
    val vRecvPres = sequence(ctx.varPrecondition(pred.receiver).toVector)

    val vArgs = pred.args.map(ctx.variable)
    val vArgPres = sequence(pred.args.flatMap(ctx.varPrecondition))

    val body = option(pred.body map {b =>
      for {
        rwc <- vRecvPres
        awc <- vArgPres
        vBody <- ctx.postcondition(b)
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


  def fpredicateDefault(pred: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {

    val (pos, info, errT) = pred.vprMeta

    val vArgs = pred.args.map(ctx.variable)
    val vArgPres = sequence(pred.args.flatMap(ctx.varPrecondition))

    val body = option(pred.body map {b =>
      for {
        wc <- vArgPres
        vBody <- ctx.postcondition(b)
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
    * Encodes assertions.
    *
    * Constraints:
    * - in.Access with in.PredicateAccess has to encode to vpr.PredicateAccessPredicate.
    *
    * [acc(  p(as), perm] -> p(Argument[as], Permission[perm])
    * [acc(e.p(as), perm] -> p(Argument[e], Argument[as], Permission[perm])
    */
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case acc@ in.Access(in.Accessible.Predicate(op: in.FPredicateAccess), perm) =>
      val (pos, info, errT) = acc.vprMeta
      for {
        vArgs <- cl.sequence(op.args map ctx.expression)
        pacc = vpr.PredicateAccess(vArgs, op.pred.name)(pos, info, errT)
        vPerm <- ctx.expression(perm)
      } yield vpr.PredicateAccessPredicate(pacc, Some(vPerm))(pos, info, errT)

    case acc@ in.Access(in.Accessible.Predicate(op: in.MPredicateAccess), perm) =>
      val (pos, info, errT) = acc.vprMeta
      for {
        vRecv <- ctx.expression(op.recv)
        vArgs <- cl.sequence(op.args map ctx.expression)
        pacc = vpr.PredicateAccess(vRecv +: vArgs, op.pred.uniqueName)(pos, info, errT)
        vPerm <- ctx.expression(perm)
      } yield vpr.PredicateAccessPredicate(pacc, Some(vPerm))(pos, info, errT)
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case fold: in.Fold =>
      val (pos, info, errT) = fold.vprMeta
      for {
        a <- ctx.assertion(fold.acc)
        pap = a.asInstanceOf[vpr.PredicateAccessPredicate]
      } yield vpr.Fold(pap)(pos, info, errT)

    case unfold: in.Unfold =>
      val (pos, info, errT) = unfold.vprMeta
      for {
        a <- ctx.assertion(unfold.acc)
        pap = a.asInstanceOf[vpr.PredicateAccessPredicate]
      } yield vpr.Unfold(pap)(pos, info, errT)
  }

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case unfold: in.Unfolding =>
      val (pos, info, errT) = unfold.vprMeta
      for {
        a <- ctx.assertion(unfold.acc)
        e <- cl.pure(ctx.expression(unfold.in))(ctx)
      } yield vpr.Unfolding(a.asInstanceOf[vpr.PredicateAccessPredicate], e)(pos, info, errT)
  }

}
