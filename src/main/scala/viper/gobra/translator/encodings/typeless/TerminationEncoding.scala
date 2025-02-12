// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation.violation
import viper.silver.plugin.standard.{predicateinstance, termination}
import viper.silver.{ast => vpr}

class TerminationEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case measure: in.TerminationMeasure =>
      val (pos, info, errT) = measure.vprMeta
      measure match {
        case in.TupleTerminationMeasure(vector, cond) =>
          for {
            c <- option(cond map ctx.expression)
            v <- sequence(vector.map {
              case e: in.Expr => ctx.expression(e)
              case p: in.Access => predicateInstance(p)(ctx)
              case _ => violation("invalid tuple measure argument")
            })
          } yield termination.DecreasesTuple(v, c)(pos, info, errT)
        case in.WildcardMeasure(cond) =>
          for {
            c <- option(cond map ctx.expression)
          } yield termination.DecreasesWildcard(c)(pos, info, errT)
      }
  }

  def predicateInstance(x: in.Access)(ctx: Context): CodeWriter[predicateinstance.PredicateInstance] = {
    val (pos, info, errT) = x.vprMeta
    for {
      v <- ctx.assertion(x)
      pap = v.asInstanceOf[vpr.PredicateAccessPredicate]
    } yield predicateinstance.PredicateInstance(pap.loc.predicateName, pap.loc.args)(pos, info, errT)
  }
}
