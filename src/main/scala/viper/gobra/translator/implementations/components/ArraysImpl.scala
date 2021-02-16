// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Arrays
import viper.silver.{ast => vpr}

/**
  * A specialized version of the array domain, where the element type is a parameter.
  */
class ArraysImpl extends Arrays {
  /**
    * Determines whether the "ShArray" Viper domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * The "ShArray" Viper domain and its two domain functions.
    */
  private lazy val (domain, locFunc, lenFunc) = genDomain

  /**
    * Returns domain, loc function, and len function.
    *
    * Generates:
    * domain SharedArray[T] {
    *   function loc(a: SharedArray, i: Int): T
    *   function len(a: SharedArray): Int
    *   function first(r: T): SharedArray
    *   function second(r: T): Int
    *
    *   axiom {
    *     forall a: SharedArray, i: Int :: {loc(a, i)} 0 <= i && i < len(a) ==> first(loc(a, i)) == a && second(loc(a, i)) == i
    *   }
    *
    *   axiom {
    *     forall a: SharedArray :: {len(a)} len(a) >= 0
    *   }
    * }
    * */
  private def genDomain: (vpr.Domain, vpr.DomainFunc, vpr.DomainFunc) = {
    val domainName = Names.sharedArrayDomain
    val typeVar = vpr.TypeVar("T")
    val typeVarMap = Map(typeVar -> typeVar)
    val domainType = vpr.DomainType(domainName = domainName, typeVarMap)(Seq(typeVar))

    val aDecl = vpr.LocalVarDecl("a", domainType)()
    val a = aDecl.localVar
    val rDecl = vpr.LocalVarDecl("r", typeVar)()
    val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
    val i = iDecl.localVar

    val locFunc = vpr.DomainFunc(s"${Names.sharedArrayDomain}loc", Seq(aDecl, iDecl), typeVar)(domainName = domainName)
    val lenFunc = vpr.DomainFunc(s"${Names.sharedArrayDomain}len", Seq(aDecl), vpr.Int)(domainName = domainName)
    val firstFunc = vpr.DomainFunc(s"${Names.sharedArrayDomain}first", Seq(rDecl), domainType)(domainName = domainName)
    val secondFunc = vpr.DomainFunc(s"${Names.sharedArrayDomain}second", Seq(rDecl), vpr.Int)(domainName = domainName)

    val locFuncApp = vpr.DomainFuncApp(locFunc, Seq(a, i), typeVarMap)()
    val lenFuncApp = vpr.DomainFuncApp(lenFunc, Seq(a), typeVarMap)()

    val injectivity = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(aDecl, iDecl),
        Seq(vpr.Trigger(Seq(locFuncApp))()),
        vpr.Implies(
          vpr.And(
            vpr.LeCmp(vpr.IntLit(0)(), i)(),
            vpr.LtCmp(i, lenFuncApp)()
          )(),
          vpr.And(
            vpr.EqCmp(vpr.DomainFuncApp(firstFunc, Seq(locFuncApp), typeVarMap)(), a)(),
            vpr.EqCmp(vpr.DomainFuncApp(secondFunc, Seq(locFuncApp), typeVarMap)(), i)()
          )()
        )()
      )()
    )(domainName = domainName)

    val lenNonNeg = vpr.AnonymousDomainAxiom{
      vpr.Forall(
        Seq(aDecl),
        Seq(vpr.Trigger(Seq(lenFuncApp))()),
        vpr.GeCmp(lenFuncApp, vpr.IntLit(0)())()
      )()
    }(domainName = domainName)

    val domain = vpr.Domain(
      name = domainName,
      typVars = Seq(typeVar),
      functions = Seq(locFunc, lenFunc, firstFunc, secondFunc),
      axioms = Seq(injectivity, lenNonNeg)
    )()

    generateDomain = true

    (domain, locFunc, lenFunc)
  }

  /** Returns the type of the array domain. */
  override def typ(t: vpr.Type): vpr.Type = vpr.DomainType(domain, Map(vpr.TypeVar("T") -> t))

  /** Getter for the array domain. */
  override def loc(a: vpr.Exp, i: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(locFunc, Seq(a, i), a.typ.asInstanceOf[vpr.DomainType].typVarsMap)(pos, info, errT)
  }

  /** Length for the array domain. */
  override def len(a: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(lenFunc, Seq(a), a.typ.asInstanceOf[vpr.DomainType].typVarsMap)(pos, info, errT)
  }

  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }
}
