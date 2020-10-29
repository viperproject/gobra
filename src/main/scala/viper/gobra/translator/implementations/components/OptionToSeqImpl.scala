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
    *   forall o : Option[T] :: { opt2seq(o) }
    *     opt2seq(o) == (optIsNone(o) ? Seq() : Seq(optGet(o)))
    * }
    * }}}
    */
  private lazy val opt2seq_axiom : vpr.DomainAxiom = {
    val oDecl = vpr.LocalVarDecl("o", options.typ(typeVar))()
    val expr = create(oDecl.localVar, typeVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(oDecl),
        Seq(vpr.Trigger(Seq(expr))()),
        vpr.EqCmp(
          expr,
          vpr.CondExp(
            options.isNone(oDecl.localVar, typeVar)(),
            vpr.EmptySeq(typeVar)(),
            vpr.ExplicitSeq(Seq(options.get(oDecl.localVar, typeVar)()))()
          )()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * domain OptToSeq[T] {
    *   function opt2seq(o : Option[T]) : Seq[T]
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    name = domainName,
    functions = Seq(domainFunc),
    axioms = Seq(opt2seq_axiom),
    typVars = Seq(typeVar)
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
