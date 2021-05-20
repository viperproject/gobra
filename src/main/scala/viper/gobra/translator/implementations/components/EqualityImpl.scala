// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.components.Equality
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Collector
import viper.silver.{ast => vpr}

class EqualityImpl extends Equality {

  override def finalize(col: Collector): Unit = {
    if (isUsed) {
      col.addMember(equalityDomain)
    }
  }

  /**
    * Generates:
    *
    * domain Equality[T] {
    *   function eq(l: T, r: T): Boolean
    *
    *   axiom {
    *     forall l: T, r: T :: {eq(l, r)} eq(l, r) <==> l == r
    *   }
    * }
    */
  private val (equalityDomain: vpr.Domain, equalityFunc: vpr.DomainFunc, typeVar: vpr.TypeVar) = {
    val domainName = Names.equalityDomain

    val typeVar = vpr.TypeVar("T")
    val typeVarMap = Map(typeVar -> typeVar)
    val lDecl = vpr.LocalVarDecl("l", typeVar)()
    val rDecl = vpr.LocalVarDecl("r", typeVar)()

    val eqFunc = vpr.DomainFunc(Names.equalityFunc, Seq(lDecl, rDecl), vpr.Bool)(domainName = domainName)

    val eqApp = vpr.DomainFuncApp(eqFunc, Seq(lDecl.localVar, rDecl.localVar), typeVarMap)()
    val eqAxiom = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        variables = Seq(lDecl, rDecl),
        triggers = Seq(vpr.Trigger(Seq(eqApp))()),
        exp = vpr.EqCmp(eqApp, vpr.EqCmp(lDecl.localVar, rDecl.localVar)())()
      )()
    )(domainName = domainName)

    val domain = vpr.Domain(
      name = domainName,
      functions = Seq(eqFunc),
      axioms = Seq(eqAxiom),
      typVars = Seq(typeVar)
    )()

    (domain, eqFunc, typeVar)
  }
  private var isUsed: Boolean = false


  /** Return eq('l', 'r'), where eq(x,y) <==> x == y holds. */
  def eq(l: vpr.Exp, r: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.Exp = {
    isUsed = true
    val typeVarMap = Map(typeVar -> l.typ)
    vpr.DomainFuncApp(equalityFunc, Seq(l, r), typeVarMap)(pos, info, errT)
  }
}
