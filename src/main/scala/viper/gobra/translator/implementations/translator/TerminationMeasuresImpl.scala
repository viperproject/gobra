// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.internal.TerminationMeasure
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.TerminationMeasures
import viper.gobra.translator.util.ViperWriter.MemberLevel.pure
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter, CodeLevel => cl}
import viper.gobra.util.Violation.violation
import viper.silver.ast.Exp
import viper.silver.plugin.standard.termination
import viper.silver.{ast => vpr}

class TerminationMeasuresImpl extends TerminationMeasures {

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = ()

  override def translate(measure: TerminationMeasure)(ctx: Context): CodeWriter[Exp] = {
    val (pos, info, errT) = measure.vprMeta
    measure match {
      case in.TupleTerminationMeasure(vector, cond) =>
        for {
          c <- cl.option(cond map ctx.expr.translateF(ctx))
          v <- cl.sequence(vector.map {
            case e: in.Expr => ctx.expr.translate(e)(ctx)
            case p: in.PredicateAccess => ctx.predicate.predicate(ctx)(p)
            case _ => violation("invalid tuple measure argument")
          })
        } yield termination.DecreasesTuple(v, c)(pos, info, errT)
      case in.WildcardMeasure(cond) =>
        for {
          c <- cl.option(cond map ctx.expr.translateF(ctx))
        } yield termination.DecreasesWildcard(c)(pos, info, errT)
    }
  }

  def decreases(measure: in.TerminationMeasure)(ctx: Context): MemberWriter[vpr.Exp] =
    pure(translate(measure)(ctx))(ctx)
}
