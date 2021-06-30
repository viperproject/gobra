// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.
package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Contains
import viper.silver.ast.{DomainType, GenericType}
import viper.silver.{ast => vpr}

class ContainsImpl extends Contains{

   override def finalize(col: Collector) = {
     if (isUsed) {
       col.addMember(containsDomain.get)
       col.addMember(genTransitivityDomain())
     }
   }

  private var containsDomain: Option[vpr.Domain] = None
  private var containsFunc: Option[vpr.DomainFunc] = None

  private var transitiveTuples: Set[(vpr.Type, vpr.Type)] = Set.empty

  private var isUsed: Boolean = false;

  private def makeTypeMap(types: Seq[vpr.Type]): Map[vpr.TypeVar, vpr.Type] = {
    val typeVars: Seq[vpr.TypeVar] = Seq(vpr.TypeVar("A"), vpr.TypeVar("B"))
    typeVars.zip(types).toMap
  }

  def addTransitive(s: Set[(vpr.Type, vpr.Type)]) =
    s ++ (for ((x1, y1) <- s; (x2, y2) <- s if y1 == x2) yield (x1, y2))

  def transitiveClosure(s: Set[(vpr.Type, vpr.Type)]): Set[(vpr.Type, vpr.Type)] = {
    val t = addTransitive(s)
    if (t.size == s.size) s else transitiveClosure(t)
  }

  private def genTransitivityDomain(): vpr.Domain = {


    def subTypes(t: vpr.Type): Set[(vpr.Type, vpr.Type)] = t match {
      case g: GenericType => g match {
        case DomainType(_, partialTypVarsMap) =>
          partialTypVarsMap.values.map(t2 => (t2, t)).toSet
        case _ => Set.empty
      }
      case _ => Set.empty
    }

    def addTypes(s: Set[(vpr.Type, vpr.Type)]): Set[(vpr.Type, vpr.Type)] = {
      s ++ (
        for (
          (x, y) <- s
        ) yield subTypes(x) ++ subTypes(y)
      ).flatten
    }

    def subTypesOfDomains(s: Set[(vpr.Type, vpr.Type)]): Set[(vpr.Type, vpr.Type)] = {
      val t = addTypes(s)
      if (t.size == s.size) s else subTypesOfDomains(t)
    }

    val withSubtypes = subTypesOfDomains(transitiveTuples)
    val closure = transitiveClosure(withSubtypes)

    var triples: Set[(vpr.Type, vpr.Type, vpr.Type)] = Set.empty

    val domainName = "ContainsTransitivity"

    for ((a,b) <- closure) {
      for ((c,d) <- closure) {
        if (b == c)
          triples = triples + ((a,b,d))
      }
    }

    def genAxiom(a: vpr.Type, b: vpr.Type, c: vpr.Type): vpr.AnonymousDomainAxiom = {
      val aVar = vpr.LocalVarDecl("a", a)()
      val bVar = vpr.LocalVarDecl("b", b)()
      val cVar = vpr.LocalVarDecl("c", c)()

      def cFuncApp(l: vpr.Exp, r: vpr.Exp) = vpr.DomainFuncApp(
        containsFunc.get,
        Seq(l, r),
        Map(
          vpr.TypeVar("A") -> l.typ,
          vpr.TypeVar("B") -> r.typ
        )
      )()

      vpr.AnonymousDomainAxiom(
        vpr.Forall(
          Seq(aVar, bVar, cVar),
          Seq(
            vpr.Trigger(
              Seq(
                cFuncApp(aVar.localVar, bVar.localVar),
                cFuncApp(bVar.localVar, cVar.localVar)
              )
            )()
          ),
          vpr.Implies(
            vpr.And(
              cFuncApp(aVar.localVar, bVar.localVar),
              cFuncApp(bVar.localVar, cVar.localVar)
            )(),
            cFuncApp(aVar.localVar, cVar.localVar)
          )()
        )()
      )(domainName = domainName)
    }

    val axioms = triples.toSeq.map(a => genAxiom(a._1,a._2,a._3))

    vpr.Domain(
      domainName,
      Seq(),
      axioms
    )()
  }

  private def genContainsDomain(): vpr.DomainFunc = {
    val domainName: String = "ContainsDomain"
    val typeVars: Seq[vpr.TypeVar] = Seq(vpr.TypeVar("A"), vpr.TypeVar("B"))
    val funcName: String = "contains"

    val aVar: vpr.LocalVarDecl = vpr.LocalVarDecl("a", typeVars.head)()
    val bVar: vpr.LocalVarDecl = vpr.LocalVarDecl("b", typeVars(1))()

    val cFunc: vpr.DomainFunc = vpr.DomainFunc(
      funcName,
      Seq(aVar, bVar),
      vpr.Bool
    )(domainName = domainName)

    containsFunc = Some(cFunc)

    val cDomain: vpr.Domain = vpr.Domain(
      domainName,
      Seq(cFunc),
      Seq(),
      typeVars
    )()

    containsDomain = Some(cDomain)
    cFunc
  }

  override def contains(a: vpr.Exp, b: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    isUsed = true

    val aT = a.typ
    val bT = b.typ

    if (aT.isConcrete && bT.isConcrete) {
      transitiveTuples = transitiveTuples + ((aT, bT))
    }

    vpr.DomainFuncApp(
      containsFunc.getOrElse(genContainsDomain()),
      Seq(a,b),
      makeTypeMap(Seq(aT, bT))
    )(pos, info, errT)
  }
}