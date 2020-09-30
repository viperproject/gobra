// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.SeqMultiplicity
import viper.silver.{ast => vpr}

class SeqMultiplicityImpl extends SeqMultiplicity {
  private val domainName : String = "SeqMultiplicity"
  private lazy val domainFuncName : String = domainName.toLowerCase()
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the "SeqMultiplicity" domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "seqmultiplicity" domain function:
    *
    * {{{
    * function seqmultiplicity(x : T, xs : Seq[T]) : Int
    * }}}
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    domainFuncName,
    Seq(
      vpr.LocalVarDecl("x", typeVar)(),
      vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    ),
    vpr.Int
  )(domainName = domainName)

  /**
    * Definition of a (domain) function application
    * "seqmultiplicity(`left`, `right`)".
    */
  private def domainFuncApp(left : vpr.Exp, right : vpr.Exp)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = {
    vpr.DomainFuncApp(
      func = domainFunc,
      args = Vector(left, right),
      typVarMap = Map(typeVar -> left.typ)
    )(pos, info, errT)
  }

  /**
    * Definition of the "seqmultiplicity_bounds" domain axiom:
    *
    * {{{
    * axiom seqmultiplicity_bounds {
    *   forall x : T, xs : Seq[T] :: { seqmultiplicity(x, xs) }
    *     0 <= seqmultiplicity(x, xs) && seqmultiplicity(x, xs) <= |xs|
    * }
    * }}}
    */
  private lazy val axiom_bounds : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val funcApp = domainFuncApp(xDecl.localVar, xsDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_bounds",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl),
        Seq(vpr.Trigger(Seq(funcApp))()),
        vpr.And(
          vpr.LeCmp(vpr.IntLit(0)(), funcApp)(),
          vpr.LeCmp(funcApp, vpr.SeqLength(xsDecl.localVar)())()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seqmultiplicity_singleton" domain axiom:
    *
    * {{{
    * axiom seqmultiplicity_singleton {
    *   forall x : T, y : T :: { seqmultiplicity(x, Seq(y)) }
    *     seqmultiplicity(x, Seq(y)) == (x == y ? 1 : 0)
    * }
    * }}}
    */
  private lazy val axiom_singleton : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val yDecl = vpr.LocalVarDecl("y", typeVar)()
    val funcApp = domainFuncApp(xDecl.localVar, vpr.ExplicitSeq(Seq(yDecl.localVar))())()
    val right = vpr.CondExp(vpr.EqCmp(xDecl.localVar, yDecl.localVar)(), vpr.IntLit(1)(), vpr.IntLit(0)())()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_singleton",
      exp = vpr.Forall(
        Seq(xDecl, yDecl),
        Seq(vpr.Trigger(Seq(funcApp))()),
        vpr.EqCmp(funcApp, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seqmultiplicity_app" domain axiom:
    *
    * {{{
    * axiom seqmultiplicity_app {
    *   forall x : T, xs : Seq[T], ys : Seq[T] :: { seqmultiplicity(x, xs ++ ys) }
    *     seqmultiplicity(x, xs ++ ys) ==
    *       seqmultiplicity(x, xs) + seqmultiplicity(x, ys)
    * }
    * }}}
    */
  private lazy val axiom_app : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val ysDecl = vpr.LocalVarDecl("ys", vpr.SeqType(typeVar))()
    val left = domainFuncApp(xDecl.localVar, vpr.SeqAppend(xsDecl.localVar, ysDecl.localVar)())()
    val right = vpr.Add(domainFuncApp(xDecl.localVar, xsDecl.localVar)(), domainFuncApp(xDecl.localVar, ysDecl.localVar)())()

    vpr.NamedDomainAxiom(
      name = s"${domainFuncName}_app",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl, ysDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seqmultiplicity_in" domain axiom:
    *
    * {{{
    * axiom seqmultiplicity_in {
    *   forall x : T, xs : Seq[T] :: { x in xs } x in xs == 0 < seqmult(x, xs)
    * }
    * }}}
    */
  private lazy val axiom_in : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val left = vpr.SeqContains(xDecl.localVar, xsDecl.localVar)()
    val right = vpr.LtCmp(vpr.IntLit(0)(), domainFuncApp(xDecl.localVar, xsDecl.localVar)())()

    // Is there no logical equivalence operator in Silver's AST?
    // Instead I, for now, translate the quantifier body to a `vpr.EqCmp`.

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
    * The "SeqMultiplicity" Viper domain:
    *
    * {{{
    * domain SeqMultiplicity[T] {
    *   function seqmultiplicity(x : T, xs : Seq[T]) : Int
    *
    *   axiom seqmultiplicity_bounds {
    *     forall x : T, xs : Seq[T] :: { seqmultiplicity(x, xs) }
    *       0 <= seqmultiplicity(x, xs) && seqmultiplicity(x, xs) <= |xs|
    *   }
    *
    *   axiom seqmultiplicity_singleton {
    *     forall x : T, y : T :: { seqmultiplicity(x, Seq(y)) }
    *       seqmultiplicity(x, Seq(y)) == (x == y ? 1 : 0)
    *   }
    *
    *   axiom seqmultiplicity_app {
    *     forall x : T, xs : Seq[T], ys : Seq[T] :: { seqmultiplicity(x, xs ++ ys) }
    *       seqmultiplicity(x, xs ++ ys) ==
    *         seqmultiplicity(x, xs) + seqmultiplicity(x, ys)
    *   }
    *
    *   axiom seqmultiplicity_in {
    *     forall x : T, xs : Seq[T] :: { x in xs } x in xs == 0 < seqmult(x, xs)
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(axiom_bounds, axiom_singleton, axiom_app, axiom_in),
    Seq(typeVar)
  )()

  /**
    * Finalises translation. May add to collector.
    */
  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }

  /**
    * Creates the Viper (domain) function application for the
    * sequence multiplicity operation "`left` # `right`".
    * Here `right` is expected to be of a sequence type.
    */
  override def create(left : vpr.Exp, right : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    domainFuncApp(left, right)(pos, info, errT)
  }
}
