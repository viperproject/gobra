// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.ast.internal.theory.{Comparability, TypeHead}
import viper.gobra.ast.internal.theory.TypeHead._
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperUtil
import viper.gobra.util.TypeBounds
import viper.silver.{ast => vpr}

/** Encoding of Gobra types into Viper expressions. */
class TypeComponentImpl extends TypeComponent {

  private val domainName = Names.typesDomain

  private val domainType: vpr.DomainType =
    vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(typeParameters = Seq.empty)

  private def functionName(name: String): String = s"${name}_$domainName"

  /** Returns serialized name for the type function of a type. */
  private def serialize(head: TypeHead): String = head match {

    case BoolHD => "bool"
    case StringHD => "string"
    case PointerHD => "pointer"
    case ArrayHD => "array"
    case SliceHD => "slice"
    case MapHD => "map"
    case ChannelHD => "channel"
    case NilHD => "nil"
    case UnitHD => "unit"
    case PermHD => "perm"
    case SortHD => "sort"
    case SeqHD => "seq"
    case SetHD => "set"
    case MSetHD => "mset"
    case MathMapHD => "dict"
    case OptionHD => "option"
    case t: TupleHD => s"tuple${t.arity}"
    case t: PredHD => s"pred${t.arity}"

    case t: TypeHead.DefinedHD => t.name
    case t: TypeHead.InterfaceHD => t.name
    case t: TypeHead.DomainHD => t.name

    case t: TypeHead.IntHD =>
      // For identical types a representative is picked
      t.kind match {
        case TypeBounds.Rune => TypeBounds.SignedInteger32.name
        case TypeBounds.UnsignedInteger8 => TypeBounds.Byte.name
        case tb => tb.name
      }

    case t: TypeHead.StructHD =>
      val fields = t.fields.map{ case (f, g) => if (g) s"${f}G" else s"${f}A" }
      s"struct_${fields.mkString("_")}"
  }


  private var genFuncs: List[vpr.DomainFunc] = List.empty
  private var genAxioms: List[vpr.DomainAxiom] = List.empty


  /** Generates:
    * Type += {
    *   function tag(Type): Int
    * }
    */
  private lazy val tagFunc: vpr.DomainFunc = {
    val func = vpr.DomainFunc(
      name = functionName("tag"), formalArgs = Seq(vpr.LocalVarDecl("t", domainType)()), typ = vpr.Int
    )(domainName = domainName)
    genFuncs ::= func
    func
  }

  /** Generates:
    * Type += {
    *   function comparableType(Type): Bool
    * }
    */
  private lazy val comparableTypeFunc: vpr.DomainFunc = {
    val func = vpr.DomainFunc(
      name = functionName("comparableType"), formalArgs = Seq(vpr.LocalVarDecl("t", domainType)()), typ = vpr.Bool
    )(domainName = domainName)
    genFuncs ::= func
    func
  }

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
  private lazy val behavioralSubtypeFunc: vpr.DomainFunc = {
    val func = vpr.DomainFunc(
      name = functionName("behavioral_subtype"), formalArgs = Seq(vpr.LocalVarDecl("l", domainType)(), vpr.LocalVarDecl("r", domainType)()), typ = vpr.Bool
    )(domainName = domainName)
    genFuncs ::= func
    func
  }

  private def genBehavioralSubtypeAxioms(ctx: Context): Unit = {
    if (!generatedBehaviouralSubtypeAxioms) {
      generatedBehaviouralSubtypeAxioms = true

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


      val appAEmpty = vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(a, typeApp(emptyInterfaceHD, Vector.empty)()(ctx)), Map.empty)()

      val empty = vpr.AnonymousDomainAxiom(
        vpr.Forall(
          Seq(aDecl),
          Seq(vpr.Trigger(Seq(appAEmpty))()),
          appAEmpty
        )()
      )(domainName = domainName)

      genAxioms ::= transitivity
      genAxioms ::= reflexivity
      genAxioms ::= empty
    }
  }
  private var generatedBehaviouralSubtypeAxioms = false

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
  private def genPreciseEqualityAxioms(typeHead: TypeHead, args: Vector[vpr.Type])(ctx: Context): Unit = {
    if (args.nonEmpty && !preciseTypes.contains(typeHead)) {
      preciseTypes += typeHead

      val name = serialize(typeHead)

      val funArgDecl = vpr.LocalVarDecl("t", domainType)()
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

      val funcApp = typeApp(typeHead, getterVarDecls map (_.localVar))()(ctx)
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
  private var preciseTypes: Set[TypeHead] = Set.empty


  /**
    * Generates:
    * Type += {
    *   function name(args): Type
    *
    *   axiom {
    *     forall args :: {name(args)} tag(name(args)) == 'tag'
    *   }
    *
    *   // if comparability is Comparable
    *   axiom {
    *     forall args :: {comparable(name(args))} comparable(name(args)) == true
    *   }
    *
    *   // if comparability is NonComparable or Dynamic
    *   axiom {
    *     forall args :: {comparable(name(args))} comparable(name(args)) == false
    *   }
    *
    *   // if comparability is Recursive
    *   axiom {
    *     forall args :: {comparable(name(args))}
    *       comparable(name(args)) == comparable(args0) && .. [all arguments of type Type] .. && comparable(argsN)
    *   }
    * }
    */
  private def genTypeFunc(typeHead: TypeHead, args: Vector[vpr.Type], tag: Int)(ctx: Context): vpr.DomainFunc = {

    if (genTypesMap.contains(typeHead)) {
      genTypesMap(typeHead)
    } else {

      val name = serialize(typeHead)
      val varsDecl = args.zipWithIndex.map{ case(t,i) => vpr.LocalVarDecl(s"p$i", t)() }
      val vars = varsDecl map (_.localVar)

      val func = vpr.DomainFunc(
        name = functionName(name),
        formalArgs = varsDecl,
        typ = domainType
      )(domainName = domainName)

      val funcApp = vpr.DomainFuncApp(func = func, vars, Map.empty)()

      val tagAxiom = {
        val tagApp = vpr.DomainFuncApp(func = tagFunc, Seq(funcApp), Map.empty)()
        val eq = vpr.EqCmp(tagApp, vpr.IntLit(tag)())()

        vpr.AnonymousDomainAxiom(
          if (args.isEmpty) eq
          else vpr.Forall(varsDecl, Seq(vpr.Trigger(Seq(funcApp))()), eq)()
        )(domainName = domainName)
      }


      val comparableAxiom = {
        val rhs: vpr.Exp = Comparability.compareKind(typeHead)(ctx.lookup) match {
          case Comparability.Kind.Comparable => vpr.TrueLit()();
          case Comparability.Kind.NonComparable | Comparability.Kind.Dynamic => vpr.FalseLit()();
          case Comparability.Kind.Recursive =>
            val typVars = vars.filter(_.typ == domainType) // only arguments of type 'Type' have to be comparable
            ViperUtil.bigAnd(
              typVars map (v => vpr.DomainFuncApp(func = comparableTypeFunc, Seq(v), Map.empty)())
            )(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        }

        val comparableApp = vpr.DomainFuncApp(func = comparableTypeFunc, Seq(funcApp), Map.empty)()
        val eq = vpr.EqCmp(comparableApp, rhs)()

        vpr.AnonymousDomainAxiom(
          if (args.isEmpty) eq
          else vpr.Forall(varsDecl, Seq(vpr.Trigger(Seq(comparableApp))()), eq)()
        )(domainName = domainName)
      }

      genFuncs ::= func
      genAxioms ::= tagAxiom
      genAxioms ::= comparableAxiom
      genBehavioralSubtypeAxioms(ctx)

      genTypesMap += (typeHead -> func)

      func
    }
  }
  private var genTypesMap: Map[TypeHead, vpr.DomainFunc] = Map.empty

  /** Type of viper expressions encoding Gobra types.  */
  override def typ()(ctx: Context): vpr.Type = domainType

  /** Translates Gobra types into Viper type expressions. */
  override def typeToExpr(typ: in.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    def go(typ: in.Type): vpr.Exp = typeToExpr(typ)(pos, info, errT)(ctx)

    typ match {
      case t: in.ArrayT =>
        typeApp(typeHead(t), Vector(vpr.IntLit(t.length)(pos, info, errT), go(t.elems)))(pos, info, errT)(ctx)

      case t =>
        typeApp(typeHead(t), children(t) map go)(pos, info, errT)(ctx)
    }
  }

  /** Generates precise equality axioms for 'typ'. */
  override def genPreciseEqualityAxioms(typ: in.Type)(ctx: Context): Unit = {

    typeTree(typ).toVector foreach { hd => arity(hd) match {
      case 0 => // already precise
      case 1 if hd == ArrayHD =>
        genPreciseEqualityAxioms(hd, Vector(vpr.Int, domainType))(ctx)

      case n =>
        genPreciseEqualityAxioms(hd, (0 until n).toVector map (_ => domainType))(ctx)
    }}
  }

  /** Behavioral subtype relation. */
  override def behavioralSubtype(subType: vpr.Exp, superType: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    vpr.DomainFuncApp(func = behavioralSubtypeFunc, Seq(subType, superType), Map.empty)(pos, info, errT)

  /** Function returning whether a type is comparable. */
  override def isComparableType(typ: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    vpr.DomainFuncApp(func = comparableTypeFunc, Seq(typ), Map.empty)(pos, info, errT)

  /** Constructor for Viper type expressions. */
  override def typeApp(typeHead: TypeHead, args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    val func = genTypeFunc(typeHead, args.map(_.typ), genFuncs.size)(ctx)
    vpr.DomainFuncApp(func = func, args, Map.empty)(pos, info, errT)
  }


  private def genDomain: vpr.Domain = {
    vpr.Domain(
      name = domainName,
      functions = genFuncs,
      axioms = genAxioms,
      typVars = Seq.empty
    )()
  }

  override def finalize(collector: Collector): Unit = {
    collector.addMember(genDomain)
  }

}
