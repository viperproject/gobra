// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.{SeqMultiplicity, SeqToMultiset}
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class SeqToMultisetImpl(val seqMultiplicity : SeqMultiplicity) extends SeqToMultiset {
  private val domainName : String = "Seq2Multiset"
  private lazy val domainFuncName : String = domainName.toLowerCase()
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the "Seq2Multiset" domain should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "seq2multiset" domain function:
    *
    * {{{
    * function seq2multiset(xs : Seq[T]) : Multiset[T]
    * }}}
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    domainFuncName,
    Seq(vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()),
    vpr.MultisetType(typeVar)
  )(domainName = domainName)

  /**
    * Definition of a (domain) function application "seq2multiset(`exp`)".
    */
  private def domainFuncApp(exp : vpr.Exp)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = exp.typ match {
    case vpr.SeqType(t) => vpr.DomainFuncApp(
      func = domainFunc,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
    case t => Violation.violation(s"expected a sequence type, but got $t")
  }

  /**
    * Definition of the "seq2multiset_in" domain axiom:
    *
    * {{{
    * axiom seq2multiset_in {
    *   forall x : T, xs : Seq[T] :: { x in seq2multiset(xs) }
    *     x in seq2multiset(xs) == seqmultiplicity(x, xs)
    *}
    * }}}
    */
  private lazy val axiom_in : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val left = vpr.AnySetContains(xDecl.localVar, domainFuncApp(xsDecl.localVar)())()
    val right = seqMultiplicity.create(xDecl.localVar, xsDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_in",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2multiset_app" domain axiom:
    *
    * {{{
    * axiom seq2multiset_app {
    *   forall xs : Seq[T], ys : Seq[T] :: { seq2multiset(xs ++ ys) }
    *     seq2multiset(xs ++ ys) == seq2multiset(xs) union seq2multiset(ys)
    * }
    * }}}
    */
  private lazy val axiom_app : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val ysDecl = vpr.LocalVarDecl("ys", vpr.SeqType(typeVar))()
    val left = domainFuncApp(vpr.SeqAppend(xsDecl.localVar, ysDecl.localVar)())()
    val right = vpr.AnySetUnion(domainFuncApp(xsDecl.localVar)(), domainFuncApp(ysDecl.localVar)())()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_app",
      exp = vpr.Forall(
        Seq(xsDecl, ysDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2multiset_size" domain axiom:
    *
    * {{{
    * axiom seq2multiset_size {
    *   forall xs : Seq[T] :: { |seq2multiset(xs)| } |seq2multiset(xs)| == |xs|
    * }
    * }}}
    */
  private lazy val axiom_size : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val left = vpr.AnySetCardinality(domainFuncApp(xsDecl.localVar)())()
    val right = vpr.SeqLength(xsDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_size",
      exp = vpr.Forall(
        Seq(xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * The "Seq2Multiset" Viper domain:
    *
    * {{{
    * domain SeqToMultiset[T] {
    *   function seq2multiset(xs : Seq[T]) : Multiset[T]
    *
    *   axiom seq2multiset_in {
    *     forall x : T, xs : Seq[T] :: { x in seq2multiset(xs) }
    *       x in seq2multiset(xs) == seqmultiplicity(x, xs)
    *   }
    *
    *   axiom seq2multiset_app {
    *     forall xs : Seq[T], ys : Seq[T] :: { seq2multiset(xs ++ ys) }
    *       seq2multiset(xs ++ ys) == seq2multiset(xs) union seq2multiset(ys)
    *   }
    *
    *   axiom seq2multiset_size {
    *     forall xs : Seq[T] :: { |seq2multiset(xs)| } |seq2multiset(xs)| == |xs|
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(axiom_in, axiom_app, axiom_size),
    Seq(typeVar)
  )()

  /**
    * Finalises translation. May add to collector.
    */
  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }

  /**
    * Creates the Viper (domain) function application that converts `exp` to a set.
    */
  override def create(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    domainFuncApp(exp)(pos, info, errT)
  }
}
