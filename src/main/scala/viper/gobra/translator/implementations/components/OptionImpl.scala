// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Options
import viper.silver.{ast => vpr}

class OptionImpl extends Options {
  private val domainName : String = "Option"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the Viper domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * {{{
    * function optsome(e : T) : Option[T]
    * }}}
    */
  private lazy val optsome_func : vpr.DomainFunc = vpr.DomainFunc(
    "optsome",
    Seq(vpr.LocalVarDecl("e", typeVar)()),
    vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar))
  )(domainName = domainName)

  /**
    * {{{
    * function optnone() : Option[T]
    * }}}
    */
  private lazy val optnone_func : vpr.DomainFunc = vpr.DomainFunc(
    "optnone",
    Seq(),
    vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar))
  )(domainName = domainName)

  /**
    * {{{
    * function optget(o : Option[T]) : T
    * }}}
    */
  private lazy val optget_func : vpr.DomainFunc = vpr.DomainFunc(
    "optget",
    Seq(vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    typeVar
  )(domainName = domainName)

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: o == optnone() || exists e : T :: o == optsome(e)
    * }
    * }}}
    */
  private lazy val option_ex_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(),
        vpr.Or(
          vpr.EqCmp(oDecl.localVar, none(typeVar)())(),
          vpr.Exists(
            Seq(eDecl),
            Seq(),
            vpr.EqCmp(oDecl.localVar, some(eDecl.localVar)())()
          )()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall e : T :: { optsome(e) } optsome(e) != optnone()
    * }
    * }}}
    */
  private lazy val optnone_some_axiom : vpr.DomainAxiom = {
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val left = some(eDecl.localVar)()
    val right = none(typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(eDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.NeCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall e : T :: { optget(optsome(e)) } optget(optsome(e)) == e
    * }
    * }}}
    */
  private lazy val optsome_over_get_axiom : vpr.DomainAxiom = {
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val expr = get(some(eDecl.localVar)(), typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(eDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.EqCmp(expr, eDecl.localVar)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * domain Option[T] {
    *   function optsome(e : T) : Option[T]
    *   function optnone() : Option[T]
    *   function optget(o : Option[T]) : T
    *
    *   axiom {
    *     forall e : T :: { optsome(e) } optsome(e) != optnone()
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: o == optnone() || exists e : T :: o == optsome(e)
    *   }
    *
    *   axiom {
    *     forall e : T :: { optget(optsome(e)) } optget(optsome(e)) == e
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(optsome_func, optnone_func, optget_func),
    Seq(optnone_some_axiom, option_ex_axiom, optsome_over_get_axiom),
    Seq(typeVar)
  )()

  /**
    * A function application of the "optget[Option[`t`]](`exp`)" function
    * of the Viper domain of options. Here `exp` should be
    * of an option type with an inner type `t`.
    */
  def get(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optget_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /**
    * A function application of the "optnone[Option[`t`]]()" function
    * of the Viper domain of options.
    */
  def none(t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optnone_func,
      args = Vector(),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /**
    * A function application of the "optsome" function
    * of the Viper domain of options.
    */
  def some(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optsome_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> exp.typ)
    )(pos, info, errT)
  }

  /**
    * Gives the Viper domain type of options.
    */
  def typ(t : vpr.Type) : vpr.DomainType = {
    generateDomain = true
    vpr.DomainType(domain, Map(typeVar -> t))
  }

  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }
}
