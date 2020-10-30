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
    * function optSome(e : T) : Option[T]
    * }}}
    */
  private lazy val optSome_func : vpr.DomainFunc = vpr.DomainFunc(
    "optSome",
    Seq(vpr.LocalVarDecl("e", typeVar)()),
    vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar))
  )(domainName = domainName)

  /**
    * {{{
    * function optNone() : Option[T]
    * }}}
    */
  private lazy val optNone_func : vpr.DomainFunc = vpr.DomainFunc(
    "optNone",
    Seq(),
    vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar))
  )(domainName = domainName)

  /**
    * {{{
    * function optGet(o : Option[T]) : T
    * }}}
    */
  private lazy val optGet_func : vpr.DomainFunc = vpr.DomainFunc(
    "optGet",
    Seq(vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    typeVar
  )(domainName = domainName)

  /**
    * {{{
    * function optIsNone(o : Option[T]) : Bool
    * }}}
    */
  private lazy val optIsNone_func : vpr.DomainFunc = vpr.DomainFunc(
    "optIsNone",
    Seq(vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    vpr.Bool
  )(domainName = domainName)

  /**
    * {{{
    * axiom {
    *   forall e : T :: { optSome(e) } optGet(optSome(e)) == e && !optIsNone(optSome(e))
    * }
    * }}}
    */
  private lazy val optGet_some_axiom : vpr.DomainAxiom = {
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val expr = some(eDecl.localVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(eDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.And(
          vpr.EqCmp(get(expr, typeVar)(), eDecl.localVar)(),
          vpr.Not(isNone(expr, typeVar)())()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: { optGet(o) } !optIsNone(o) ==> o == optSome(optGet(o))
    * }
    * }}}
    */
  private lazy val optSome_get_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val expr = get(oDecl.localVar, typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.Implies(
          vpr.Not(isNone(oDecl.localVar, typeVar)())(),
          vpr.EqCmp(oDecl.localVar, some(expr)())()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   optIsNone(optNone())
    * }
    * }}}
    */
  private lazy val optIsNone_none_axiom : vpr.DomainAxiom = {
    vpr.AnonymousDomainAxiom(
      isNone(none(typeVar)(), typeVar)()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: { optIsNone(o) } o == optNone() || exists e : T :: { optSome(e) } o == optSome(e)
    * }
    * }}}
    */
  private lazy val optType_existence_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val expr = some(eDecl.localVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(vpr.Trigger(Seq(isNone(oDecl.localVar, typeVar)()))()),
        vpr.Or(
          vpr.EqCmp(oDecl.localVar, none(typeVar)())(),
          vpr.Exists(
            Seq(eDecl),
            Seq(vpr.Trigger(Seq(expr))()),
            vpr.EqCmp(oDecl.localVar, expr)()
          )()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * domain Option[T] {
    *   function optSome(e : T) : Option[T]
    *   function optNone() : Option[T]
    *   function optGet(o : Option[T]) : T
    *   function optIsNone(o : Option[T]) : Bool
    *
    *   axiom {
    *     forall e : T :: { optSome(e) } optGet(optSome(e)) == e && !optIsNone(optSome(e))
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: { optGet(o) } !optIsNone(o) ==> o == optSome(optGet(o))
    *   }
    *
    *   axiom {
    *     optIsNone(optNone())
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: { optIsNone(o) }
    *       o == optNone() || exists e : T :: { optSome(e) } o == optSome(e)
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    name = domainName,
    functions = Seq(optSome_func, optNone_func,
      optGet_func, optIsNone_func),
    axioms = Seq(optGet_some_axiom, optSome_get_axiom,
      optIsNone_none_axiom, optType_existence_axiom),
    typVars = Seq(typeVar)
  )()

  /**
    * A function application of the "optget[Option[`t`]](`exp`)" function.
    * Here `exp` should be of an option type with an inner type `t`.
    */
  def get(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optGet_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /** A function application of 'optIsNone'. */
  def isNone(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optIsNone_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /**
    * A function application of the "optnone[Option[`t`]]()" function.
    */
  def none(t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optNone_func,
      args = Vector(),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /**
    * A function application of the "optsome" function.
    */
  def some(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optSome_func,
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
