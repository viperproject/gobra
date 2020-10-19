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
    * {{{
    * axiom {
    *   opt2seq(optnone()) == Seq()
    * }
    * }}}
    */
  private lazy val opt2seq_none_axiom : vpr.DomainAxiom = {
    vpr.AnonymousDomainAxiom(
      vpr.EqCmp(
        create(options.none(typeVar)(), typeVar)(),
        vpr.EmptySeq(typeVar)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom {
    *   forall e : T :: { opt2seq(optsome(e)) } opt2seq(optsome(e)) == Seq(e)
    * }
    * }}}
    */
  private lazy val opt2seq_some_axiom : vpr.DomainAxiom = {
    val eDecl = vpr.LocalVarDecl("e", typeVar)()
    val left = create(options.some(eDecl.localVar)(), typeVar)()
    val right = vpr.ExplicitSeq(Seq(eDecl.localVar))()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(eDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(
          left,
          right
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * domain OptToSeq[T] {
    *   function opt2seq(o : Option[T]) : Seq[T]
    *
    *   axiom {
    *     opt2seq(optnone()) == Seq()
    *   }
    *
    *   axiom {
    *     forall e : T :: { opt2seq(optsome(e)) } opt2seq(optsome(e)) == Seq(e)
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
