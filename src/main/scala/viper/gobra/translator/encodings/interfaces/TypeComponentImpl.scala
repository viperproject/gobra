// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}
import viper.gobra.translator.Names
import viper.gobra.util.TypeBounds
import viper.gobra.util.TypeBounds.IntegerKind

/** Encoding of Gobra types into Viper expressions. */
class TypeComponentImpl extends TypeComponent {

  private val domainName = Names.typesDomain

  private val domainType: vpr.DomainType =
    vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(typeParameters = Seq.empty)


  /** Generates:
    * Type += {
    *   function tag(Type): Int
    * }
    */
  private val tagFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = s"tag_$domainName", formalArgs = Seq(vpr.LocalVarDecl("t", domainType)()), typ = vpr.Int
  )(domainName = domainName)

  /**
    * Generates:
    * Type += {
    *   function behavioral_subtype(Type, Type): Bool
    *
    *   axiom {
    *     forall a: Type, b: Type, c: Type :: {behavioral_subtype(a,b), behavioral_subtype(b,c)}
    *       behavioral_subtype(a,b) && behavioral_subtype(b,c) ==> behavioral_subtype(a,c)
    *   }
    *
    *   axiom {
    *     forall a: Type :: {behavioral_subtype(a,a)} behavioral_subtype(a,a)
    *   }
    *
    *   axiom {
    *     forall a: Type :: {behavioral_subtype(t, empty)} behavioral_subtype(t, empty)
    *   }
    * }
    */
  private lazy val behavioralSubtypeFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = s"behavioral_subtype_$domainName", formalArgs = Seq(vpr.LocalVarDecl("l", domainType)(), vpr.LocalVarDecl("r", domainType)()), typ = vpr.Bool
  )(domainName = domainName)

  private lazy val behavioralSubtypeAxioms: Vector[vpr.DomainAxiom] = {
    val aDecl = vpr.LocalVarDecl("a", domainType)(); val a = aDecl.localVar
    val bDecl = vpr.LocalVarDecl("b", domainType)(); val b = bDecl.localVar
    val cDecl = vpr.LocalVarDecl("c", domainType)(); val c = cDecl.localVar

    val appAB = vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(a, b), Map.empty)()
    val appBC = vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(b, c), Map.empty)()
    val appAC = vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(a, c), Map.empty)()

    val transitivity = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(aDecl, bDecl, cDecl),
        Seq(vpr.Trigger(Seq(appAB, appBC))()),
        vpr.Implies(
          vpr.And(appAB, appBC)(),
          appAC
        )()
      )()
    )(domainName = domainName)


    val appAA = vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(a, a), Map.empty)()

    val reflexivity = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(aDecl),
        Seq(vpr.Trigger(Seq(appAA))()),
        appAA
      )()
    )(domainName = domainName)


    val appAEmpty = vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(a, appType(Names.emptyInterface, Vector.empty)(vpr.NoPosition, vpr.NoInfo , vpr.NoTrafos)), Map.empty)()

    val empty = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(aDecl),
        Seq(vpr.Trigger(Seq(appAEmpty))()),
        appAEmpty
      )()
    )(domainName = domainName)

    Vector(transitivity, reflexivity, empty)
  }

  /**
    * Generates:
    * Type += {
    *   function name(args): Type
    *
    *   axiom {
    *     forall args :: {name(args)} tag(name(args)) == 'tag'
    *   }
    * }
    */
  private def genTypeFunc(name: String, args: Vector[vpr.Type], tag: Int): (vpr.DomainFunc, Vector[vpr.DomainAxiom]) = {

    val varsDecl = args.zipWithIndex.map{ case(t,i) => vpr.LocalVarDecl(s"p$i", t)() }
    val vars = varsDecl map (_.localVar)

    val func = vpr.DomainFunc(
      name = s"${name}_$domainName",
      formalArgs = varsDecl,
      typ = domainType
    )(domainName = domainName)

    val tagAxiom = vpr.AnonymousDomainAxiom(
      if (args.isEmpty) {
        vpr.EqCmp(vpr.DomainFuncApp(func = tagFunc, Seq(vpr.DomainFuncApp(func = func, Seq(), Map())()), Map())(), vpr.IntLit(tag)())()
      } else {
        vpr.Forall(
          variables = varsDecl,
          triggers = Seq(vpr.Trigger(Seq(vpr.DomainFuncApp(func = func, vars, Map())()))()),
          exp = vpr.EqCmp(vpr.DomainFuncApp(func = tagFunc, Seq(vpr.DomainFuncApp(func = func, vars, Map())()), Map())(), vpr.IntLit(tag)())()
        )()
      }
    )(domainName = domainName)

    (func, Vector(tagAxiom))
  }

  private def appType(name: String, args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.DomainFuncApp = {
    if (genTypesMap.contains(name)) {
      vpr.DomainFuncApp(func = genTypesMap(name), args, Map.empty)(pos, info, errT)
    } else {
      val (func, axioms) = genTypeFunc(name, args.map(_.typ), genFuncs.size)
      genFuncs ::= func
      genAxioms ++= axioms
      genTypesMap += (name -> func)
      vpr.DomainFuncApp(func = func, args, Map.empty)(pos, info, errT)
    }
  }

  /** Type of viper expressions encoding Gobra types.  */
  override def typ()(ctx: Context): vpr.Type = domainType

  /** behavioral subtype relation. */
  override def behavioralSubtype(subType: vpr.Exp, superType: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(subType, superType), Map.empty)(pos, info, errT)

  /** constructor for defined type. */
  override def defined(name: String)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType(name, Vector.empty)(pos, info, errT)

  /** constructor for pointer type. */
  override def pointer(elem: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("pointer", Vector(elem))(pos, info, errT)

  /** constructor for nil type. */
  override def nil()(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("nil", Vector.empty)(pos, info, errT)

  /** constructor for boolean type. */
  override def bool()(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("bool", Vector.empty)(pos, info, errT)

  /** constructor for integer type. */
  override def int(kind: IntegerKind)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    // in accordance with the type identity defined in [[frontend.info.implementation.property.TypeIdentity]]
    val name = kind match {
      case TypeBounds.Rune => TypeBounds.SignedInteger32.name
      case TypeBounds.UnsignedInteger8 => TypeBounds.Byte.name
      case tb => tb.name
    }

    appType(s"int_$name", Vector.empty)(pos, info, errT)
  }

  /** constructor for struct type. */
  override def struct(fields: Vector[(String, vpr.Exp, Boolean)])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    val (fieldNames, args) = fields.collect{ case (n, e, true) => (n, e) }.unzip
    val name = s"struct_${fieldNames.mkString("_")}"
    appType(name, args)(pos, info , errT)
  }

  /** constructor for array type. */
  override def array(len: vpr.Exp, elem: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("array", Vector(len, elem))(pos, info, errT)

  /** constructor for interface type. */
  override def interface(name: String)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType(name, Vector.empty)(pos, info , errT)

  /** constructor for permission type. */
  override def perm()(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("perm", Vector.empty)(pos, info, errT)

  /** constructor for sequence type. */
  override def sequence(elem: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("sequence", Vector(elem))(pos, info, errT)

  /** constructor for set type. */
  override def set(elem: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("set", Vector(elem))(pos, info, errT)

  /** constructor for multiset type. */
  override def mset(elem: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("mset", Vector(elem))(pos, info, errT)

  /** constructor for option type. */
  override def option(elem: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("option", Vector(elem))(pos, info, errT)

  /** constructor for tuple type. */
  override def tuple(elems: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    appType("tuple", elems)(pos, info, errT)


  private def genDomain: vpr.Domain = {
    val lazyAxioms = behavioralSubtypeAxioms
    vpr.Domain(
      name = domainName,
      functions = tagFunc +: behavioralSubtypeFunc +: genFuncs,
      axioms = lazyAxioms ++ genAxioms,
      typVars = Seq.empty
    )()
  }

  private var genFuncs: List[vpr.DomainFunc] = List.empty
  private var genAxioms: List[vpr.DomainAxiom] = List.empty
  private var genTypesMap: Map[String, vpr.DomainFunc] = Map.empty


  override def finalize(collector: Collector): Unit = {
    if (genFuncs.nonEmpty) collector.addMember(genDomain)
  }

}
