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
  import viper.silver.plugin.standard.adt

  // TODO: adapt, call the right constructor
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case measure: in.TerminationMeasure =>
      val (pos, info, errT) = measure.vprMeta
      measure match {
        case t: in.TupleTerminationMeasure =>
          for {
            c <- option(t.cond map ctx.expression)
            v <- sequence(t.tuple.map {
              case e: in.Expr => ctx.expression(e)
              case p: in.Access => predicateInstance(p)(ctx)
              case _ => violation("invalid tuple measure argument")
            })
          } yield termination.DecreasesTuple(v, c)(pos, info, errT)
        case m: in.WildcardMeasure =>
          for {
            c <- option(m.cond map ctx.expression)
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

  val typeVar = vpr.TypeVar("T")
  val termPairADTType = adt.AdtType(
    adtName = "TerminationPair",
    partialTypVarsMap = Map(typeVar -> typeVar),
  )(typeParameters = Seq[vpr.TypeVar](typeVar))
  val terminationMeasureAdt = adt.Adt(
    name = "TerminationPair",
    constructors = Seq(
      adt.AdtConstructor(
        name = "TermPair",
        formalArgs = Seq(
          vpr.LocalVarDecl("pos1", typeVar)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos),
          vpr.LocalVarDecl("pos2", vpr.Bool)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        ),
      )(vpr.NoPosition, vpr.NoInfo, termPairADTType, termPairADTType.adtName, vpr.NoTrafos)
    ),
    derivingInfo = Map.empty,
  )(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)


  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    addMemberFn(terminationMeasureAdt)
  }
}
