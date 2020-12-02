// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.util.ViperUtil
import viper.gobra.util.{TypeBounds, Violation}
import viper.gobra.util.TypeBounds.IntegerKind

/** Encoding of Gobra types into Viper expressions. */
class TypeComponentImpl extends TypeComponent {

  private val domainName = Names.typesDomain

  private val domainType: vpr.DomainType =
    vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(typeParameters = Seq.empty)

  private def functionName(name: String): String = s"${name}_$domainName"
  private def reverseFunctionName(functionName: String): String = functionName.stripSuffix(s"_$domainName")

  /** Generates:
    * Type += {
    *   function tag(Type): Int
    * }
    */
  private val tagFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = functionName("tag"), formalArgs = Seq(vpr.LocalVarDecl("t", domainType)()), typ = vpr.Int
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
    name = functionName("behavioral_subtype"), formalArgs = Seq(vpr.LocalVarDecl("l", domainType)(), vpr.LocalVarDecl("r", domainType)()), typ = vpr.Bool
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
    *   function nameGet0(Type): 'args[0]'
    *   ...
    *   function nameGetN(Type): 'args[N]'
    *
    *   axiom {
    *     forall x0: 'args[0]', ..., xN: 'args[N]' :: {name(x0, ..., xN)} nameGet0(name(x0, ..., xN)) == x0 && ... && nameGetN(name(x0, ..., xN)) == xN
    *   }
    * }
    */
  private def precise(name: String, args: Vector[vpr.Type]): Unit = {
    if (args.nonEmpty && !preciseTypes.contains(name)) {
      preciseTypes += name

      val funArgDecl = vpr.LocalVarDecl("t", domainType)(); val funArg = funArgDecl
      val getterVarDecls = args.zipWithIndex map {
        case (t, idx) => vpr.LocalVarDecl(s"x$idx", t)()
      }

      val getters = args.zipWithIndex map {
        case (t, idx) =>
          vpr.DomainFunc(
            name = functionName(s"get_${idx}_$name"),
            formalArgs = Seq(funArgDecl),
            typ = t
          )(domainName = domainName)
      }

      val funcApp = appType(name, getterVarDecls map (_.localVar))()
      val getterApps = getters.map(f => vpr.DomainFuncApp(func = f, Seq(funcApp), Map.empty)())

      val axiom = vpr.AnonymousDomainAxiom(
        vpr.Forall(
          getterVarDecls,
          Seq(vpr.Trigger(Seq(funcApp))()),
          ViperUtil.bigAnd(getterApps.zip(getterVarDecls).map{ case (fapp, x) => vpr.EqCmp(fapp, x.localVar)() })(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        )()
      )(domainName = domainName)

      genFuncs ++= getters
      genAxioms ::= axiom
    }
  }

  private var preciseTypes: Set[String] = Set.empty

  /**
    * Generates:
    * Type += {
    *   function name(args): Type
    *
    *   axiom {
    *     forall args :: {name(args)} tag(name(args)) == 'tag'
    *   }
    *
    *   axiom {
    *     forall args :: {name(args)} get0(name(args)) == args0 && ... && getN(name(args))
    *   }
    * }
    */
  private def genTypeFunc(name: String, args: Vector[vpr.Type], tag: Int): (vpr.DomainFunc, Vector[vpr.DomainAxiom]) = {

    val varsDecl = args.zipWithIndex.map{ case(t,i) => vpr.LocalVarDecl(s"p$i", t)() }
    val vars = varsDecl map (_.localVar)

    val func = vpr.DomainFunc(
      name = functionName(name),
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

  private def appType(name: String, args: Vector[vpr.Exp])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp = {
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

  /** transaltes Gobra types into Viper type expressions. */
  override def typeExpr(typ: in.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    def go(typ: in.Type): vpr.Exp = typeExpr(typ)(pos, info, errT)(ctx)

    typ match {
      case t: in.DefinedT => defined(t.name)(pos, info, errT)(ctx)
      case t: in.PointerT => pointer(go(t.t))(pos, info, errT)(ctx)
      case _: in.BoolT => bool()(pos, info, errT)(ctx)
      case t: in.IntT => int(t.kind)(pos, info, errT)(ctx)
      case t: in.StructT => struct(t.fields.map( f => (f.name, go(f.typ), f.ghost)))(pos, info, errT)(ctx)
      case t: in.ArrayT => array(vpr.IntLit(t.length)(pos, info, errT), go(t.elems))(pos, info, errT)(ctx)
      case t: in.InterfaceT => interface(t.name)(pos, info, errT)(ctx)
      case _: in.PermissionT => perm()(pos, info, errT)(ctx)
      case t: in.SequenceT => sequence(go(t.t))(pos, info, errT)(ctx)
      case t: in.SetT => set(go(t.t))(pos, info, errT)(ctx)
      case t: in.MultisetT => mset(go(t.t))(pos, info, errT)(ctx)
      case t: in.OptionT => option(go(t.t))(pos, info, errT)(ctx)
      case t: in.TupleT => tuple(t.ts map go)(pos, info, errT)(ctx)
      case t => Violation.violation(s"type $t is not supported as a type expression.")
    }
  }

  /** Generates precise equality axioms for 'typ'. */
  override def precise(typ: in.Type)(ctx: Context): Unit = {

    def genAxiom(funcApp: vpr.Exp): Unit = {
      val actualFuncApp = funcApp.asInstanceOf[vpr.DomainFuncApp]
      val funcName = reverseFunctionName(actualFuncApp.funcname)
      val funcArgTypes = actualFuncApp.args.map(_.typ).toVector
      precise(funcName, funcArgTypes)
    }

    def go(typ: in.Type): Unit = precise(typ)(ctx)

    val typeVar = (idx: Int) => vpr.LocalVar(s"x$idx", domainType)()
    val intVar = (idx: Int) => vpr.LocalVar(s"x$idx", vpr.Int)()

    typ match {
      case _: in.DefinedT | _: in.BoolT | _: in.IntT | _: in.InterfaceT | _: in.PermissionT =>

      case t: in.PointerT =>
        genAxiom(pointer(typeVar(0))()(ctx)); go(t.t)

      case t: in.StructT =>
        genAxiom(struct(t.fields.zipWithIndex map { case (f, idx) => (f.name, typeVar(idx), f.ghost) })()(ctx))
        t.fields foreach (f => go(f.typ))

      case t: in.ArrayT =>
        genAxiom(array(intVar(0), typeVar(1))()(ctx))
        go(t.elems)

      case t: in.SequenceT =>
        genAxiom(sequence(typeVar(0))()(ctx))
        go(t.t)

      case t: in.SetT =>
        genAxiom(set(typeVar(0))()(ctx))
        go(t.t)

      case t: in.MultisetT =>
        genAxiom(mset(typeVar(0))()(ctx))
        go(t.t)

      case t: in.OptionT =>
        genAxiom(option(typeVar(0))()(ctx))
        go(t.t)

      case t: in.TupleT =>
        genAxiom(tuple(t.ts.indices.toVector map typeVar)()(ctx))
        t.ts foreach go

      case t => Violation.violation(s"type $t is not supported as a type expression.")
    }
  }

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
    val (fieldNames, args) = fields.collect{ case (n, e, false) => (n, e) }.unzip
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
