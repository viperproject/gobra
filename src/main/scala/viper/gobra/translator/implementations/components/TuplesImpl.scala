// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Tuples
import viper.silver.{ast => vpr}

import scala.collection.mutable

class TuplesImpl extends Tuples {

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = {
    generatedDomains foreach col.addMember
  }

  override def typ(args: Vector[vpr.Type]): vpr.DomainType = {
    val arity = args.size

    vpr.DomainType(
      domain = domain(arity),
      typVarsMap = typeVarMap(args)
    )
  }

  override def create(args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.DomainFuncApp = {
    val arity = args.size

    vpr.DomainFuncApp(
      func = tuple(arity),
      args = args,
      typVarMap = typeVarMap(args map (_.typ))
    )(pos, info, errT)
  }

  override def get(arg: vpr.Exp, index: Int, arity: Int)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.DomainFuncApp = {
    vpr.DomainFuncApp(
      func = getter(index, arity),
      args = Vector(arg),
      typVarMap = arg.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, errT)
  }

  def tuple(arity: Int): vpr.DomainFunc =
    constructors.getOrElse(arity, {addNTuplesDomain(arity); constructors(arity)})
  def getter(index: Int, arity: Int): vpr.DomainFunc =
    getters.getOrElse((index, arity), {addNTuplesDomain(arity); getters((index, arity))})

  def domain(arity: Int): vpr.Domain =
    domains.getOrElse(arity, {addNTuplesDomain(arity); domains(arity)})

  def typeVarMap(ts: Vector[vpr.Type]): Map[vpr.TypeVar, vpr.Type] =
    domain(ts.length).typVars.zip(ts).toMap

  def generatedDomains: List[vpr.Domain] = _generatedDomains

  private var _generatedDomains: List[vpr.Domain] = List.empty
  private val domains: mutable.Map[Int, vpr.Domain] = mutable.Map.empty
  private val constructors: mutable.Map[Int, vpr.DomainFunc] = mutable.Map.empty
  private val getters: mutable.Map[(Int,Int), vpr.DomainFunc] = mutable.Map.empty

  private def addNTuplesDomain(arity: Int): Unit = {
    val domainName = s"Tuple$arity"

    val typeVars = 0.until(arity) map (ix => vpr.TypeVar(s"T$ix"))
    val decls = 0.until(arity) map (ix => vpr.LocalVarDecl(s"t$ix", typeVars(ix))())
    val vars = decls map (_.localVar)
    val typVarMap = typeVars.zip(typeVars).toMap

    val domainTyp = vpr.DomainType(domainName, typVarMap)(typeVars)
    val domainDecl = vpr.LocalVarDecl("p", domainTyp)()
    val domainVar = domainDecl.localVar

    val tupleFunc = vpr.DomainFunc(s"tuple$arity",decls, domainTyp)(domainName = domainName)
    val getFuncs = 0.until(arity) map (ix =>
      vpr.DomainFunc(s"get${ix}of$arity", Seq(domainDecl), typeVars(ix))(domainName = domainName)
      )

    val getOverTupleAxiom = {
      val nTupleApp = vpr.DomainFuncApp(tupleFunc, vars, typVarMap)()
      val eqs = 0.until(arity) map {ix =>
        vpr.EqCmp(
          vpr.DomainFuncApp(
            getFuncs(ix),
            Seq(nTupleApp),
            typVarMap
          )(),
          vars(ix)
        )()
      }

      vpr.NamedDomainAxiom(
        name = s"getter_over_tuple$arity",
        exp = vpr.Forall(
          decls,
          Seq(vpr.Trigger(Seq(nTupleApp))()),
          viper.silicon.utils.ast.BigAnd(eqs)
        )()
      )(domainName = domainName)
    }

    val tupleOverGetAxiom = {
      val nGetApp = getFuncs map (f =>
        vpr.DomainFuncApp(f, Seq(domainVar), typVarMap)()
        )

      vpr.NamedDomainAxiom(
        name = s"tuple${arity}_over_getter",
        exp = vpr.Forall(
          Seq(domainDecl),
          nGetApp map (g => vpr.Trigger(Seq(g))()),
          vpr.EqCmp(
            vpr.DomainFuncApp(
              tupleFunc,
              nGetApp,
              typVarMap
            )(),
            domainVar
          )()
        )()
      )(domainName = domainName)
    }

    val domain = vpr.Domain(
      domainName,
      tupleFunc +: getFuncs,
      Seq(getOverTupleAxiom, tupleOverGetAxiom),
      typeVars
    )()

    domains.update(arity, domain)
    constructors.update(arity, tupleFunc)
    (0 until arity) foreach (ix => getters.update((ix, arity), getFuncs(ix)))

    _generatedDomains ::= domain
  }
}
