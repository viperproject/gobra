// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.SeqToSet
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class SeqToSetImpl extends SeqToSet {
  private val domainName : String = "Seq2Set"
  private lazy val domainFuncName : String = domainName.toLowerCase()
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the "Seq2Set" domain should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "seq2set" domain function:
    *
    * {{{
    * function seq2set(xs : Seq[T]) : Set[T]
    * }}}
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    domainFuncName,
    Seq(vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()),
    vpr.SetType(typeVar)
  )(domainName = domainName)

  /**
    * Definition of a (domain) function application "seq2set(`exp`)".
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
    * Definition of the "seq2set_in" domain axiom:
    *
    * {{{
    * axiom seq2set_in {
    *   forall xs : Seq[T], e : T :: { e in seq2set(xs) }
    *     e in xs <==> e in seq2set(xs)
    * }
    * }}}
    */
  private lazy val axiom_in : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val left = vpr.SeqContains(eDecl.localVar, xsDecl.localVar)()
    val right = vpr.AnySetContains(eDecl.localVar, domainFuncApp(xsDecl.localVar)())()

    // NOTE: there doesn't seem to be an AST node for logical equivalence
    // in Silver, so I translate to an equality comparison instead.

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_in",
      exp = vpr.Forall(
        Seq(xsDecl, eDecl),
        Seq(vpr.Trigger(Seq(right))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2set_app" domain axiom:
    *
    * {{{
    * axiom seq2set_app {
    *   forall xs : Seq[T], ys : Seq[T] ::
    *     { seq2set(xs ++ ys) } { seq2set(xs) union seq2set(ys) }
    *       seq2set(xs ++ ys) == seq2set(xs) union seq2set(ys)
    *   }
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
    * Definition of the "seq2set_len" domain axiom:
    *
    * {{{
    * axiom seq2set_len {
    *   forall xs : Seq[T] :: { |seq2set(xs)| } |seq2set(xs)| <= |xs|
    * }
    * }}}
    */
  private lazy val axiom_len : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val left = vpr.AnySetCardinality(domainFuncApp(xsDecl.localVar)())()
    val right = vpr.SeqLength(xsDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_len",
      exp = vpr.Forall(
        Seq(xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.LeCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * The "Seq2Set" Viper domain:
    *
    * {{{
    * domain SeqToSet[T] {
    *   function seq2set(xs : Seq[T]) : Set[T]
    *
    *   axiom seq2set_in {
    *     forall xs : Seq[T], e : T :: { e in seq2set(xs) }
    *       e in xs <==> e in seq2set(xs)
    *   }
    *
    *   axiom seq2set_app {
    *     forall xs : Seq[T], ys : Seq[T] ::
    *       { seq2set(xs ++ ys) } { seq2set(xs) union seq2set(ys) }
    *         seq2set(xs ++ ys) == seq2set(xs) union seq2set(ys)
    *   }
    *
    *   axiom seq2set_len {
    *     forall xs : Seq[T] :: { |seq2set(xs)| } |seq2set(xs)| <= |xs|
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(axiom_in, axiom_app, axiom_len),
    Seq(typeVar)
  )()

  /**
    * Finalises translation. May add to collector.
    */
  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }

  /**
    * Creates the Viper (domain) function application
    * that converts "seq2set(`exp`)" to a set.
    */
  override def create(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    domainFuncApp(exp)(pos, info, errT)
  }
}
