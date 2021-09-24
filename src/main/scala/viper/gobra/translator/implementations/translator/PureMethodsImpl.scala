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
import viper.silver.plugin.standard.termination
import viper.gobra.util.Violation.violation

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
      terminationMeasure = meth.terminationMeasure match {
        case Some(measure) => translateTerminationMeasure(measure)(ctx)
        case None => Seq.empty
      }

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
        posts = posts ++ terminationMeasure,
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
      terminationMeasure = func.terminationMeasure match {
        case Some(measure) => translateTerminationMeasure(measure)(ctx)
        case None => Seq.empty
      }

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
        posts = posts ++ terminationMeasure,
        body = body
      )(pos, info, errT)

    } yield function
  }

  def translateTerminationMeasure(x: in.Assertion)(ctx: Context): Vector[vpr.Exp] = {
    val (pos, info, errT) = x.vprMeta
    x match {
      case in.TupleTerminationMeasure(vector) =>
        val res = vector.map(n => n match {
          case e: in.Expr => ctx.expr.translate(e)(ctx).res
          case p: in.PredicateAccess => ctx.predicate.predicate(ctx)(p).res
          case _ => violation("invalid tuple measure argument")
        })
        //val res = (vector.map(ctx.ass.translate(_)(ctx))) map getExprs
        Vector(termination.DecreasesTuple(res, None)(pos, info, errT))
      case in.WildcardMeasure() =>
        Vector(termination.DecreasesWildcard(None)(pos, info, errT))
      case in.InferTerminationMeasure() =>
        violation("Infer measure should already be handled by internal transformation")
      case in.StarMeasure() =>
        Vector(termination.DecreasesStar()(pos, info, errT))
      case in.ConditionalTerminationMeasures(clauses) => //violation("Conditional measure should not be handled here")
        clauses.map(translateClause(_)(x)(ctx))
      case _ => violation("assertion not subtype of TerminationMeasure")
    }
  }


  def translateClause(x: in.ConditionalTerminationMeasureClause)(ass: in.Assertion)(ctx: Context): vpr.Exp = {
    val(pos, info, errT) = ass.vprMeta
    x match {
      case in.ConditionalTerminationMeasureIfClause(measure, cond) =>
        measure match {
          case in.WildcardMeasure() =>
            termination.DecreasesWildcard(Some(ctx.expr.translate(cond)(ctx).res))(pos, info, errT)
          case in.TupleTerminationMeasure(vector) =>
            val res = vector.map(n => n match {
              case e: in.Expr => ctx.expr.translate(e)(ctx).res
              case p: in.PredicateAccess => ctx.predicate.predicate(ctx)(p).res
              case _ => violation("invalid tuple measure argument")
            })
            // val res = (vector.map(ctx.ass.translate(_)(ctx))) map getExprs
            termination.DecreasesTuple(res, Some(ctx.expr.translate(cond)(ctx).res))(pos, info, errT)
          case in.StarMeasure() => violation("Star measure occurs in if clause")
          case in.InferTerminationMeasure() => violation("Infer measure occurs in if clause")
        }
      case in.StarMeasure() =>
        termination.DecreasesStar()(pos, info, errT)
    }
  }
}
