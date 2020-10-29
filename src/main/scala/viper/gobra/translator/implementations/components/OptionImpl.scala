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
    * function optType(o : Option[T]) : Int
    * }}}
    */
  private lazy val optType_func : vpr.DomainFunc = vpr.DomainFunc(
    "optType",
    Seq(vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * unique function optTypeNone() : Int
    * }}}
    */
  private lazy val optTypeNone_func : vpr.DomainFunc = vpr.DomainFunc(
    "optTypeNone",
    Seq(),
    vpr.Int,
    true
  )(domainName = domainName)

  /**
    * {{{
    * unique function optTypeSome() : Int
    * }}}
    */
  private lazy val optTypeSome_func : vpr.DomainFunc = vpr.DomainFunc(
    "optTypeSome",
    Seq(),
    vpr.Int,
    true
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
    * function optIsNone(o : Option[T]) : Bool
    * }}}
    */
  private lazy val optIsSome_func : vpr.DomainFunc = vpr.DomainFunc(
    "optIsSome",
    Seq(vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    vpr.Bool
  )(domainName = domainName)

  /**
    * {{{
    * axiom {
    *   forall e : T :: { optSome(e) } optGet(optSome(e)) == e
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
        vpr.EqCmp(get(expr, typeVar)(), eDecl.localVar)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: { optGet(o) } optIsSome(o) ==> optSome(optGet(o)) == o
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
          isSome(oDecl.localVar, typeVar)(),
          vpr.EqCmp(
            some(expr)(),
            oDecl.localVar
          )()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   optType(optNone()) == optTypeNone()
    * }
    * }}}
    */
  private lazy val optType_none_axiom : vpr.DomainAxiom = {
    vpr.AnonymousDomainAxiom(
      vpr.EqCmp(
        optType(none(typeVar)(), typeVar)(),
        optTypeNone(typeVar)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall e : T :: { optSome(e) } optType(optSome(e)) == optTypeSome()
    * }
    * }}}
    */
  private lazy val optType_some_axiom : vpr.DomainAxiom = {
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val expr = some(eDecl.localVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(eDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.EqCmp(
          optType(expr, typeVar)(),
          optTypeSome(typeVar)()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: { optType(o) }
    *     optType(o) == optTypeNone() && o == optNone() ||
    *     optType(o) == optTypeSome() && exists e : T :: o == optSome(e)
    * }
    * }}}
    */
  private lazy val optType_existence_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val expr = optType(oDecl.localVar, typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.Or(
          vpr.And(
            vpr.EqCmp(expr, optTypeNone(typeVar)())(),
            vpr.EqCmp(oDecl.localVar, none(typeVar)())()
          )(),
          vpr.And(
            vpr.EqCmp(expr, optTypeSome(typeVar)())(),
            vpr.Exists(Seq(eDecl), Seq(), vpr.EqCmp(oDecl.localVar, some(eDecl.localVar)())())()
          )()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: { optIsNone(o) } optType(o) == optTypeNone() <==> optIsNone(o)
    * }
    * }}}
    */
  private lazy val optIsNone_type_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val expr = isNone(oDecl.localVar, typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.EqCmp(
          vpr.EqCmp(optType(oDecl.localVar, typeVar)(), optTypeNone(typeVar)())(),
          expr
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall o : Option[T] :: { optIsSome(o) } optType(o) == optTypeSome() <==> optIsSome(o)
    * }
    * }}}
    */
  private lazy val optIsSome_type_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val expr = isSome(oDecl.localVar, typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.EqCmp(
          vpr.EqCmp(optType(oDecl.localVar, typeVar)(), optTypeSome(typeVar)())(),
          expr
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * domain Option[T] {
    *
    *   // Constructors
    *
    *   function optSome(e : T) : Option[T]
    *   function optNone() : Option[T]
    *
    *   // Destructor
    *
    *   function optGet(o : Option[T]) : T
    *
    *   // Constructor Types
    *
    *   function optType(o : Option[T]) : Int
    *   unique function optTypeNone() : Int
    *   unique function optTypeSome() : Int
    *   function optIsNone(o : Option[T]) : Bool
    *   function optIsSome(o : Option[T]) : Bool
    *
    *   // Axioms
    *
    *   axiom {
    *     forall e : T :: { optSome(e) } optGet(optSome(e)) == e
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: { optGet(o) } optIsSome(o) ==> o == optSome(optGet(o))
    *   }
    *
    *   axiom {
    *     optType(optNone()) == optTypeNone()
    *   }
    *
    *   axiom {
    *     forall e : T :: { optSome(e) } optType(optSome(e)) == optTypeSome()
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: { optType(o) }
    *       optType(o) == optTypeNone() && o == optNone() ||
    *       optType(o) == optTypeSome() && exists e : T :: o == optSome(e)
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: { optIsNone(o) } optType(o) == optTypeNone() <==> optIsNone(o)
    *   }
    *
    *   axiom {
    *     forall o : Option[T] :: { optIsSome(o) } optType(o) == optTypeSome() <==> optIsSome(o)
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    name = domainName,
    functions = Seq(optSome_func, optNone_func, optGet_func, optType_func,
      optTypeNone_func, optTypeSome_func, optIsNone_func, optIsSome_func),
    axioms = Seq(optGet_some_axiom, optSome_get_axiom, optType_none_axiom,
      optType_some_axiom, optType_existence_axiom, optIsNone_type_axiom,
      optIsSome_type_axiom),
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

  /** A function application of 'optIsNone'. */
  def isSome(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optIsSome_func,
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

  /** A function application of 'optType'. */
  private def optType(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optType_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /** A function application of 'optTypeNone'. */
  private def optTypeNone(t : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optTypeNone_func,
      args = Vector(),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /** A function application of 'optTypeSome'. */
  private def optTypeSome(t : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = optTypeSome_func,
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
