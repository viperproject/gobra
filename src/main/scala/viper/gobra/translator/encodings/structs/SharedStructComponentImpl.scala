// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}
import StructEncoding.ComponentParameter
import viper.gobra.translator.Names

/**
  * Right now, this is just a tuples domain with an additional injectivity axiom to enable quantified permissions.
  * Because of the injectivity axiom, the constructor has to be removed. Otherwise the axioms are inconsistent.
  * */
class SharedStructComponentImpl extends SharedStructComponent {

  override def finalize(col: Collector): Unit = genDomains.foreach(col.addMember)

  private var genDomains: List[vpr.Domain] = List.empty
  private var genArities: Set[Int] = Set.empty
  private var domains: Map[Int, vpr.Domain] = Map.empty
  private var gets: Map[(Int, Int), vpr.DomainFunc] = Map.empty

  /**
    * Generates:
    * domain SharedStruct[T1, ..., TN] {
    *   function get1ofN(x: SharedStruct): T1
    *   ...
    *   function getNofN(x: SharedStruct): T2
    *   function rev1ofN(v1: T1): SharedStruct
    *   ...
    *   function revNofN(vN: TN): SharedStruct
    *
    * axiom {
    *   forall x: SharedStruct, y: SharedStruct :: {eq(x, y)} eq(x,y) <==> get1OfN(x) == get1ofN(y) && ... && getNofN(x) == getNofN(y)
    * }
    *
    * axiom {
    *   forall x: SharedStruct :: {get1ofN(x)} rev1ofN(get1ofN(x)) == x
    * }
    *
    * ...
    *
    * axiom {
    *   forall x: SharedStruct :: {getNofN(x)} revNofN(getNofN(x)) == x
    * }
    */
  private def genDomain(arity: Int)(ctx: Context): Unit = {
    val domainName: String = s"${Names.sharedStructDomain}$arity"
    val typeVars = (0 until arity) map (i => vpr.TypeVar(s"T$i"))
    val typeVarMap = (typeVars zip typeVars).toMap
    val domainType = vpr.DomainType(domainName = domainName, partialTypVarsMap = typeVarMap)(typeVars)
    val xDecl = vpr.LocalVarDecl("x", domainType)()
    val x = xDecl.localVar
    val yDecl = vpr.LocalVarDecl("y", domainType)()
    val y = yDecl.localVar
    val vsDecl = (0 until arity) map (i => vpr.LocalVarDecl(s"v$i", typeVars(i))())

    val getFuncs = (0 until arity) map (i => vpr.DomainFunc(s"${Names.sharedStructDomain}get${i}of$arity", Seq(xDecl), typeVars(i))(domainName = domainName))
    val getApps = getFuncs map (f => vpr.DomainFuncApp(func = f, Seq(x), typeVarMap)())
    val getAppTriggers = getApps map (g => vpr.Trigger(Seq(g))())

    val revFuncs = (0 until arity) map (i => vpr.DomainFunc(s"${Names.sharedStructDomain}rev${i}of$arity", Seq(vsDecl(i)), domainType)(domainName = domainName))

    val eqApp = ctx.equality.eq(x, y)()
    val eqAppTrigger = vpr.Trigger(Seq(eqApp))()

    val equalityAxiom = {
      vpr.AnonymousDomainAxiom(
        vpr.Forall(
          Seq(xDecl, yDecl),
          Seq(eqAppTrigger),
          vpr.EqCmp(
            eqApp,
            viper.silicon.utils.ast.BigAnd(getFuncs map (f =>
              vpr.EqCmp(
                vpr.DomainFuncApp(func = f, Seq(x), typeVarMap)(),
                vpr.DomainFuncApp(func = f, Seq(y), typeVarMap)()
              )()
              ))
          )()
        )()
      )(domainName = domainName)
    }

    val injective = {
      (0 until arity) map { i =>
        vpr.AnonymousDomainAxiom(
          vpr.Forall(
            Seq(xDecl),
            Seq(getAppTriggers(i)),
            vpr.EqCmp(
              vpr.DomainFuncApp(func = revFuncs(i), Seq(getApps(i)), typeVarMap)(),
              x
            )()
          )()
        )(domainName = domainName)
      }
    }

    val domain = vpr.Domain(
      name = domainName,
      typVars = typeVars,
      functions = revFuncs ++ getFuncs,
      axioms = equalityAxiom +: injective
    )()

    genDomains ::= domain

    domains += (arity -> domain)
    getFuncs.zipWithIndex foreach { case(f, i) =>  gets += ((i, arity) -> f) }
    genArities += arity
  }

  /** Returns type of shared-struct domain. */
  override def typ(t: ComponentParameter)(ctx: Context): vpr.Type = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)(ctx)

    val typeVarMap = (domains(arity).typVars zip (t map (_._1))).toMap

    vpr.DomainType(
      domain = domains(arity),
      typVarsMap = typeVarMap
    )
  }

  /** Getter of shared-struct domain. */
  override def get(base: vpr.Exp, idx: Int, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)(ctx)
    val (pos, info, errT) = src.vprMeta
    vpr.DomainFuncApp(func = gets(idx, arity), Seq(base), base.typ.asInstanceOf[vpr.DomainType].typVarsMap)(pos, info, errT)
  }


}
