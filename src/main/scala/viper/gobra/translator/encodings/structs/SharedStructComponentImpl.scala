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
  * Right now, this is just a tuples domain instantiated for Refs and
  * with an additional injectivity axiom to enable quantified permissions.
  * */
class SharedStructComponentImpl extends SharedStructComponent {

  override def finalize(col: Collector): Unit = genDomains.foreach(col.addMember)

  private var genDomains: List[vpr.Domain] = List.empty
  private var genArities: Set[Int] = Set.empty
  private var types: Map[Int, vpr.DomainType] = Map.empty
  private var creates: Map[Int, vpr.DomainFunc] = Map.empty
  private var gets: Map[(Int, Int), vpr.DomainFunc] = Map.empty

  /**
    * Generates:
    * domain SharedStruct {
    *   function createN(v1: Ref, ..., vN: Ref): SharedStruct
    *   function get1ofN(x: SharedStruct): Ref
    *   ...
    *   function getNofN(x: SharedStruct): Ref
    *   function revN(r: Ref): SharedStruct
    *
    * axiom {
    *   forall v1, ..., vN :: {createN(v1, ..., vN)} get1ofN(createN(v1, ..., vN)) == v1 && ... && getNofN(createN(v1, ..., vN)) = vN
    * }
    *
    * axiom {
    *   forall x: SharedStruct :: {get1ofN(x)}...{getNofN(x)} createN(get1ofN(x), ..., getNofN(x)) == x
    * }
    *
    * axiom {
    *   ( forall x: SharedStruct :: {get1ofN(x)} revN(get1ofN(x)) == x ) &&
    *   ...
    *   ( forall x: SharedStruct :: {getNofN(x)} revN(getNofN(x)) == x )
    * }
    */
  private def genDomain(arity: Int): Unit = {
    val domainName: String = s"${Names.sharedStructDomain}$arity"
    val domainType = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty)
    val xDecl = Vector(vpr.LocalVarDecl("x", domainType)())
    val x = xDecl.head.localVar
    val vsDecl = (0 until arity) map (i => vpr.LocalVarDecl(s"v$i", vpr.Ref)())
    val vs = vsDecl map (_.localVar)

    val createFunc = vpr.DomainFunc(s"${Names.sharedStructCreateFunc}$arity", vsDecl, domainType)(domainName = domainName)
    val createApp = vpr.DomainFuncApp(func = createFunc, vs, Map.empty)()
    val createAppTriggers = Seq(vpr.Trigger(Seq(createApp))())

    val getFuncs = (0 until arity) map (i => vpr.DomainFunc(s"${Names.sharedStructGetFunc}${i}of$arity", xDecl, vpr.Ref)(domainName = domainName))
    val getApps = getFuncs map (f => vpr.DomainFuncApp(func = f, Seq(x), Map.empty)())
    val getAppTriggers = getApps map (g => vpr.Trigger(Seq(g))())

    val revFunc = vpr.DomainFunc(s"${Names.sharedStructRevFunc}$arity", Vector(vpr.LocalVarDecl("r", vpr.Ref)()), domainType)(domainName = domainName)

    val getOverCreateAxiom = {
      val eqs = (0 until arity) map {ix =>
        vpr.EqCmp(
          vpr.DomainFuncApp(func = getFuncs(ix), Seq(createApp), Map.empty)(),
          vs(ix)
        )()
      }

      vpr.AnonymousDomainAxiom(
        vpr.Forall(vsDecl, createAppTriggers, viper.silicon.utils.ast.BigAnd(eqs))()
      )(domainName = domainName)
    }

    val createOverGetAxiom = {
      vpr.AnonymousDomainAxiom(
        vpr.Forall(
          xDecl,
          getAppTriggers,
          vpr.EqCmp(
            vpr.DomainFuncApp(func = createFunc, getApps, Map.empty)(),
            x
          )()
        )()
      )(domainName = domainName)
    }

    val injective = {
      val conjuncts = (0 until arity) map { i =>
        vpr.Forall(
          xDecl,
          Seq(getAppTriggers(i)),
          vpr.EqCmp(
            vpr.DomainFuncApp(func = revFunc, Seq(getApps(i)), Map.empty)(),
            x
          )()
        )()
      }

      vpr.AnonymousDomainAxiom(
        viper.silicon.utils.ast.BigAnd(conjuncts)
      )(domainName = domainName)
    }

    genDomains ::= vpr.Domain(
      name = domainName,
      functions = Vector(revFunc, createFunc) ++ getFuncs,
      typVars = Seq.empty,
      axioms = Vector(getOverCreateAxiom, createOverGetAxiom, injective)
    )()

    types += (arity -> domainType)
    creates += (arity -> createFunc)
    getFuncs.zipWithIndex foreach { case(f, i) =>  gets += ((i, arity) -> f) }
    genArities += arity
  }

  /** Returns type of shared-struct domain. */
  override def typ(t: ComponentParameter)(ctx: Context): vpr.Type = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)
    types(arity)
  }

  /** Constructor of shared-struct domain. */
  override def create(args: Vector[vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)
    val (pos, info, errT) = src.vprMeta
    vpr.DomainFuncApp(func = creates(arity), args, Map.empty)(pos, info, errT)
  }

  /** Getter of shared-struct domain. */
  override def get(base: vpr.Exp, idx: Int, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)
    val (pos, info, errT) = src.vprMeta
    vpr.DomainFuncApp(func = gets(idx, arity), Seq(base), Map.empty)(pos, info, errT)
  }


}
