// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.{OptionToSeq, Options}
import viper.silver.ast.{DomainFuncApp, ErrorTrafo, Exp, Info, Position}
import viper.silver.{ast => vpr}

class OptionToSeqImpl(options : Options) extends OptionToSeq {
  private val domainName : String = "OptToSeq"
  private lazy val domainFuncName : String = "opt2seq"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the Viper domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "opt2seq" domain function:
    *
    * {{{
    * function opt2seq(o : Option[T]) : Seq[T]
    * }}}
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    domainFuncName,
    Seq(vpr.LocalVarDecl("o", options.typ(typeVar))()),
    vpr.SeqType(typeVar)
  )(domainName = domainName)

  /**
    * Definition of the "opt2seq_none" axiom:
    *
    * {{{
    * axiom opt2seq_none {
    *   forall o : Option[T] :: opt2seq(o) == Seq() <==> o == optnone()
    * }
    * }}}
    */
  private lazy val opt2seq_none_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", options.typ(typeVar))()

    vpr.NamedDomainAxiom(
      name = "opt2seq_none",
      exp = vpr.Forall(
        Seq(oDecl),
        Seq(),
        vpr.EqCmp(
          vpr.EqCmp(create(oDecl.localVar, typeVar)(), vpr.EmptySeq(typeVar)())(),
          vpr.EqCmp(oDecl.localVar, options.none(typeVar)())()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "opt2seq_some" axiom:
    *
    * {{{
    * axiom opt2seq_some {
    *   forall o : Option[T], e : T :: opt2seq(o) == Seq(e) <==> o == optsome(e)
    * }
    * }}}
    */
  private lazy val opt2seq_some_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", options.typ(typeVar))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()

    vpr.NamedDomainAxiom(
      name = "opt2seq_some",
      exp = vpr.Forall(
        Seq(oDecl, eDecl),
        Seq(),
        vpr.EqCmp(
          vpr.EqCmp(create(oDecl.localVar, typeVar)(), vpr.ExplicitSeq(Seq(eDecl.localVar))())(),
          vpr.EqCmp(oDecl.localVar, options.some(eDecl.localVar)())()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * The "Option" Viper domain:
    *
    * {{{
    * domain OptToSeq[T] {
    *   function opt2seq(o : Option[T]) : Seq[T]
    *
    *   axiom opt2seq_none {
    *     forall o : Option[T] :: opt2seq(o) == Seq() <==> o == optnone()
    *   }
    *
    *   axiom opt2seq_some {
    *     forall o : Option[T], e : T :: opt2seq(o) == Seq(e) <==> o == optsome(e)
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(opt2seq_none_axiom, opt2seq_some_axiom),
    Seq(typeVar)
  )()

  override def create(exp : Exp, typ : vpr.Type)(pos : Position, info : Info, errT : ErrorTrafo) : DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = domainFunc,
      args = Vector(exp),
      typVarMap = Map(typeVar -> typ)
    )(pos, info, errT)
  }

  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }
}
