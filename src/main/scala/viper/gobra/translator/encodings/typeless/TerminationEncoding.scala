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
import viper.silver.ast.Member
import viper.silver.plugin.standard.{predicateinstance, termination}
import viper.silver.{ast => vpr}

class TerminationEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

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
            lastElem: vpr.Exp = measure match {
              case _: in.ItfMethodMeasure => itfMethodApp
              case _: in.NonItfMethodMeasure => nonItfMethodApp
            }
          } yield termination.DecreasesTuple(v :+ lastElem, c)(pos, info, errT)
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

  private val domainName = "TerminationDomain"
  private val itfMethod = vpr.DomainFunc(
    name = "ItfMethodMeasure",
    formalArgs = Seq.empty,
    typ = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty),
    interpretation = None
  )(vpr.NoPosition, vpr.NoInfo, domainName, vpr.NoTrafos)
  private val itfMethodApp = vpr.DomainFuncApp(func = itfMethod, args = Seq.empty, typVarMap = Map.empty)()
  private val nonItfMethod = vpr.DomainFunc(
    name = "NonItfMethodMeasure",
    formalArgs = Seq.empty,
    typ = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty),
    interpretation = None
  )(vpr.NoPosition, vpr.NoInfo, domainName, vpr.NoTrafos)
  private val nonItfMethodApp = vpr.DomainFuncApp(func = nonItfMethod, args = Seq.empty, typVarMap = Map.empty)()
  private val termDomain = vpr.Domain(
    name = domainName,
    functions = Seq(itfMethod, nonItfMethod),
    axioms = Seq.empty,
    typVars = Seq.empty,
    interpretations = None
  )(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

  // TODO: doc
  private val wfOrderDomain = "WellFoundedOrder"
  private val termDomainWFOrderName = domainName + "WellFoundedOrder"
  private val termDomainWFOrder = vpr.Domain(
    name = termDomainWFOrderName,
    functions = Seq.empty,
    axioms = Seq(
      // decreasing(nonItfMethod, itfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "decreasing",
          args = Seq(nonItfMethodApp, itfMethodApp),
          typVarMap = Map.empty
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomain, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName),
      // bounded(nonItfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "bounded",
          args = Seq(nonItfMethodApp),
          typVarMap = Map.empty
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomain, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName),
      // bounded(itfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "bounded",
          args = Seq(itfMethodApp),
          typVarMap = Map.empty
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomain, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName)

    ),
    typVars = Seq.empty,
    interpretations = None
  )(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

  override def finalize(addMemberFn: Member => Unit): Unit = {
    addMemberFn(termDomain)
    addMemberFn(termDomainWFOrder)
  }
}
