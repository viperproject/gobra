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
  * */
class SharedStructComponentImpl extends SharedStructComponent {

  override def finalize(col: Collector): Unit = genDomains.foreach(col.addMember)

  private var genDomains: List[vpr.Domain] = List.empty
  private var genArities: Set[Int] = Set.empty
  private var domains: Map[Int, vpr.Domain] = Map.empty
  private var creates: Map[Int, vpr.DomainFunc] = Map.empty
  private var gets: Map[(Int, Int), vpr.DomainFunc] = Map.empty

  /**
    * Generates:
    * domain SharedStruct[T1, ..., TN] {
    *   function createN(v1: T1, ..., vN: TN): SharedStruct
    *   function get1ofN(x: SharedStruct): T1
    *   ...
    *   function getNofN(x: SharedStruct): T2
    *   function rev1ofN(v1: T1): SharedStruct
    *   ...
    *   function revNofN(vN: TN): SharedStruct
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
    *   forall x: SharedStruct :: {get1ofN(x)} rev1ofN(get1ofN(x)) == x
    * }
    *
    * ...
    *
    * axiom {
    *   forall x: SharedStruct :: {getNofN(x)} revNofN(getNofN(x)) == x
    * }
    */
  private def genDomain(arity: Int): Unit = {
    val domainName: String = s"${Names.sharedStructDomain}$arity"
    val typeVars = (0 until arity) map (i => vpr.TypeVar(s"Q$i"))
    val typeVarMap = (typeVars zip typeVars).toMap
    val domainType = vpr.DomainType(domainName = domainName, partialTypVarsMap = typeVarMap)(typeVars)
    val xDecl = Vector(vpr.LocalVarDecl("x", domainType)())
    val x = xDecl.head.localVar
    val vsDecl = (0 until arity) map (i => vpr.LocalVarDecl(s"v$i", typeVars(i))())
    val vs = vsDecl map (_.localVar)

    val createFunc = vpr.DomainFunc(s"${Names.sharedStructCreateFunc}$arity", vsDecl, domainType)(domainName = domainName)
    val createApp = vpr.DomainFuncApp(func = createFunc, vs, typeVarMap)()
    val createAppTriggers = Seq(vpr.Trigger(Seq(createApp))())

    val getFuncs = (0 until arity) map (i => vpr.DomainFunc(s"${Names.sharedStructGetFunc}${i}of$arity", xDecl, typeVars(i))(domainName = domainName))
    val getApps = getFuncs map (f => vpr.DomainFuncApp(func = f, Seq(x), typeVarMap)())
    val getAppTriggers = getApps map (g => vpr.Trigger(Seq(g))())

    val revFuncs = (0 until arity) map (i => vpr.DomainFunc(s"${Names.sharedStructRevFunc}${i}of$arity", Seq(vsDecl(i)), domainType)(domainName = domainName))

    val getOverCreateAxiom = {
      val eqs = (0 until arity) map {ix =>
        vpr.EqCmp(
          vpr.DomainFuncApp(func = getFuncs(ix), Seq(createApp), typeVarMap)(),
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
            vpr.DomainFuncApp(func = createFunc, getApps, typeVarMap)(),
            x
          )()
        )()
      )(domainName = domainName)
    }

    val injective = {
      (0 until arity) map { i =>
        vpr.AnonymousDomainAxiom(
          vpr.Forall(
            xDecl,
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
      functions = Vector(createFunc) ++ revFuncs ++ getFuncs,
      typVars = typeVars,
      axioms = Vector(getOverCreateAxiom, createOverGetAxiom) ++ injective
    )()

    genDomains ::= domain

    domains += (arity -> domain)
    creates += (arity -> createFunc)
    getFuncs.zipWithIndex foreach { case(f, i) =>  gets += ((i, arity) -> f) }
    genArities += arity
  }

  /** Returns type of shared-struct domain. */
  override def typ(t: ComponentParameter)(ctx: Context): vpr.Type = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)

    val typeVarMap = (domains(arity).typVars zip (t map (_._1))).toMap

    vpr.DomainType(
      domain = domains(arity),
      typVarsMap = typeVarMap
    )
  }

  /** Constructor of shared-struct domain. */
  override def create(args: Vector[vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    require(args.size == t.size)
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)
    val (pos, info, errT) = src.vprMeta
    val typeVarMap = (domains(arity).typVars zip (args map (_.typ))).toMap
    vpr.DomainFuncApp(func = creates(arity), args, typeVarMap)(pos, info, errT)
  }

  /** Getter of shared-struct domain. */
  override def get(base: vpr.Exp, idx: Int, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val arity = t.size
    if (!(genArities contains arity)) genDomain(arity)
    val (pos, info, errT) = src.vprMeta
    vpr.DomainFuncApp(func = gets(idx, arity), Seq(base), base.typ.asInstanceOf[vpr.DomainType].typVarsMap)(pos, info, errT)
  }


}
