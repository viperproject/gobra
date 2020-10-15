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
    * Definition of the "optsome" function for the Viper domain of options:
    *
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
    * Definition of the "optnone" function for the Viper domain of options:
    *
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
    * Definition of the "optget" function for the Viper domain of options:
    *
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
    * Definition of the "option_ex" axiom for the Viper domain of options:
    *
    * {{{
    * axiom option_ex {
    *   forall o : Option[T] :: o == optnone() || exists e : T :: o == optsome(e)
    * }
    * }}}
    */
  private lazy val option_ex_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()

    vpr.NamedDomainAxiom(
      name = "option_ex",
      exp = vpr.Forall(
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
    * Definition of the "optnone_some" axiom for the Viper domain of options:
    *
    * {{{
    * axiom optnone_some {
    *   forall e : T :: optsome(e) != optnone()
    * }
    * }}}
    */
  private lazy val optnone_some_axiom : vpr.DomainAxiom = {
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val left = some(eDecl.localVar)()
    val right = none(typeVar)()

    vpr.NamedDomainAxiom(
      name = "optnone_some",
      exp = vpr.Forall(
        Seq(eDecl),
        Seq(),
        vpr.NeCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "optsome_over_get" axiom:
    *
    * {{{
    * axiom optsome_over_get {
    *   forall o : Option[T], e : T :: optget(o) == e <==> o == optsome(e)
    * }
    * }}}
    */
  private lazy val optsome_over_get_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val left = get(oDecl.localVar, typeVar)()
    val right = some(eDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = "optsome_over_get",
      exp = vpr.Forall(
        Seq(oDecl, eDecl),
        Seq(),
        vpr.EqCmp(
          vpr.EqCmp(left, eDecl.localVar)(),
          vpr.EqCmp(oDecl.localVar, right)()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * The "Option" Viper domain:
    *
    * {{{
    * domain Option[T] {
    *   function optsome(e : T) : Option[T]
    *   function optnone() : Option[T]
    *   function optget(o : Option[T]) : T
    *
    *   axiom optnone_some {
    *     forall e : T :: optsome(e) != optnone()
    *   }
    *
    *   axiom option_ex {
    *     forall o : Option[T] :: o == optnone() || exists e : T :: o == optsome(e)
    *   }
    *
    *   axiom optsome_over_get {
    *     forall o : Option[T], e : T :: optget(o) == e <==> o == optsome(e)
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
