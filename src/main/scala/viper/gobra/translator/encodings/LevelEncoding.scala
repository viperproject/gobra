// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.ast.internal.transform.TerminationLevelsTransform
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

/**
  * Encodes termination levels (see [[TerminationLevelsTransform]]), i.e., finite multisets of termination atoms that
  * are compared with the multiset ordering induced by the order on atoms.
  *
  * domain $Level {
  *   function $lvl(m: Multiset[$Atom]): $Level
  *   function $atoms(l: $Level): Multiset[$Atom]
  *   axiom { forall m :: { $lvl(m) } $atoms($lvl(m)) == m }
  *   axiom { forall l :: { $atoms(l) } $lvl($atoms(l)) == l }
  *
  *   function $atomLt(a: $Atom, b: $Atom): Bool
  *   axiom { forall a :: { $atomLt(a, a) } !$atomLt(a, a) }
  *
  *   // Huet-Oppen characterization of the multiset ordering
  *   function $levelLt(l: $Level, r: $Level): Bool
  *   axiom { forall l, r :: { $levelLt(l, r) } $levelLt(l, r) <==> $atoms(l) != $atoms(r) &&
  *             forall x :: { x in $atoms(l) } { x in $atoms(r) } (x in $atoms(l)) > (x in $atoms(r)) ==>
  *               exists y :: { $atomLt(x, y) } $atomLt(x, y) && (y in $atoms(r)) > (y in $atoms(l)) }
  *
  *   // the greater of two levels w.r.t. some (unknown) linearization of the order
  *   function $levelMax(l: $Level, r: $Level): $Level
  *   axiom { forall l, r :: { $levelMax(l, r) } ($levelMax(l, r) == l || $levelMax(l, r) == r) &&
  *             ($levelMax(l, r) == l || $levelLt(l, $levelMax(l, r))) && ($levelMax(l, r) == r || $levelLt(r, $levelMax(l, r))) }
  * }
  *
  * Additionally, the encoding declares one function per termination atom and the order facts between the atoms that
  * the package under verification may assume (see [[in.TerminationOrder]]). The level of an interface value is the
  * atom of its dynamic type, plus the result of the `TerminationLevel` method if the interface declares one. The level
  * of a closure is fixed when the closure is created.
  */
class LevelEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import LevelEncoding._

  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Level() / m =>
      m match {
        case Exclusive => levelType
        case Shared => vpr.Ref
      }
  }

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    default(super.expression(ctx)) {
      case (e: in.DfltVal) :: ctx.Level() / Exclusive => unit(emptyLevel(e))
      case e: in.LevelEmpty => unit(emptyLevel(e))
      case a: in.LevelAtom => unit(atomLevel(a.key, a)(ctx))
      case u@in.LevelUnion(l, r) => for { vl <- goE(l); vr <- goE(r) } yield union(vl, vr, u)
      case m@in.LevelMax(l, r) => for { vl <- goE(l); vr <- goE(r) } yield app(levelMaxFunc, Seq(vl, vr), m)
      case lt@in.LevelLess(l, r) => for { vl <- goE(l); vr <- goE(r) } yield app(levelLtFunc, Seq(vl, vr), lt)
      case le@in.LevelAtMost(l, r) =>
        val (pos, info, errT) = le.vprMeta
        for { vl <- goE(l); vr <- goE(r) } yield vpr.Or(vpr.EqCmp(vl, vr)(pos, info, errT), app(levelLtFunc, Seq(vl, vr), le))(pos, info, errT)
      case lo@in.LevelOf(e) => levelOf(e, lo)(ctx)
    }
  }

  private val interfaces = new viper.gobra.translator.encodings.interfaces.InterfaceComponentImpl

  private def levelOf(e: in.Expr, src: in.Node)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = src.vprMeta
    ctx.underlyingType(e.typ) match {
      case itf: in.InterfaceT =>
        // the atom of the dynamic type, plus the level of the components if the interface declares a level method
        registerTypeLevels(ctx)
        val levelMethod = ctx.table.lookup(itf, TerminationLevelsTransform.levelMethodName).collect { case m: in.MethodProxy => m }
        for {
          // the dynamic type is projected directly from the interface value, as `typeOf` would introduce a function
          // and a type constant for the interface even if the level is never used
          itfValue <- ctx.expression(e)
          dynType = interfaces.dynTypeOf(itfValue)(pos, info, errT)(ctx)
          typeLevel = vpr.DomainFuncApp(typeLevelFunc(dynType.typ), Seq(dynType), Map.empty)(pos, info, errT)
          res <- levelMethod match {
            case Some(proxy) =>
              ctx.expression(in.PureMethodCall(e, proxy, Vector.empty, in.LevelT(Exclusive))(src.info)).map(union(typeLevel, _, src))
            case None => unit(typeLevel: vpr.Exp)
          }
        } yield res

      case _: in.FunctionT =>
        for {
          closure <- ctx.expression(e)
        } yield vpr.DomainFuncApp(closureLevelFunc(closure.typ), Seq(closure), Map.empty)(pos, info, errT)

      case _ =>
        TerminationLevelsTransform.namedTypeOf(e.typ) match {
          case Some(name) =>
            val typeLevel = atomLevel(in.TerminationAtom.namedType(name), src)(ctx)
            TerminationLevelsTransform.componentsLevel(e)(ctx.table) match {
              case Some(components) => ctx.expression(components).map(union(typeLevel, _, src))
              case None => unit(typeLevel)
            }
          case None => unit(emptyLevel(src))
        }
    }
  }

  /*
   * Atoms and their order.
   */

  /** Viper functions representing the atoms, indexed by atom keys of imported packages and by component indices. */
  private var importedAtoms: Map[String, vpr.DomainFunc] = Map.empty
  private var mainAtoms: Map[Int, vpr.DomainFunc] = Map.empty
  private var order: in.TerminationOrder = in.TerminationOrder.empty
  /** Registered atoms: Viper atom function together with an atom key (for main atoms, some key of the component). */
  private var registered: Map[String, vpr.DomainFunc] = Map.empty

  private def atomFunc(key: String)(ctx: Context): vpr.DomainFunc = {
    order = ctx.table.getTerminationOrder
    val func = order.mainComponent.get(key) match {
      case Some(component) =>
        mainAtoms.getOrElse(component, {
          val f = vpr.DomainFunc(s"$$atom_c$component", Seq.empty, atomType)(domainName = atomDomainName)
          mainAtoms += component -> f
          f
        })
      case None =>
        importedAtoms.getOrElse(key, {
          val f = vpr.DomainFunc(s"$$atom_${importedAtoms.size}_${sanitize(key)}", Seq.empty, atomType)(domainName = atomDomainName)
          importedAtoms += key -> f
          f
        })
    }
    if (!registered.contains(key)) registered += key -> func
    func
  }

  private def atomLevel(key: String, src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val atom = vpr.DomainFuncApp(atomFunc(key)(ctx), Seq.empty, Map.empty)(pos, info, errT)
    app(lvlFunc, Seq(vpr.ExplicitMultiset(Seq(atom))(pos, info, errT)), src)
  }

  /** Axioms relating the dynamic types implementing interfaces to their atoms. */
  private var typeLevelAxioms: Vector[vpr.Exp] = Vector.empty
  private var typeLevelsRegistered: Boolean = false

  private def registerTypeLevels(ctx: Context): Unit = if (!typeLevelsRegistered) {
    typeLevelsRegistered = true
    val src = in.LevelEmpty()(viper.gobra.reporting.Source.Parser.Internal)
    // only the types that implement interfaces (named types and pointers to named types) can be dynamic types
    val namedImpls: Vector[(String, in.TypeExpr)] = ctx.table.getImplementations.values.flatten.toVector.distinct.collect {
      case d: in.DefinedT => d.name -> (in.DefinedTExpr(d.name)(src.info): in.TypeExpr)
      case in.PointerT(d: in.DefinedT, _) => d.name -> (in.PointerTExpr(in.DefinedTExpr(d.name)(src.info))(src.info): in.TypeExpr)
    }
    typeLevelAxioms = namedImpls.sortBy(i => (i._1, i._2.isInstanceOf[in.PointerTExpr])).map { case (name, te) =>
      val level = atomLevel(in.TerminationAtom.namedType(name), src)(ctx)
      val vTe = ctx.expression(te).res
      vpr.EqCmp(vpr.DomainFuncApp(typeLevelFunc(vTe.typ), Seq(vTe), Map.empty)(), level)(): vpr.Exp
    }
  }

  private var typeLevelFuncOpt: Option[vpr.DomainFunc] = None
  private def typeLevelFunc(typeOfTypes: vpr.Type): vpr.DomainFunc = typeLevelFuncOpt.getOrElse {
    val f = vpr.DomainFunc("$typeLevel", Seq(vpr.LocalVarDecl("t", typeOfTypes)()), levelType)(domainName = atomDomainName)
    typeLevelFuncOpt = Some(f)
    f
  }

  private var closureLevelFuncOpt: Option[vpr.DomainFunc] = None
  private def closureLevelFunc(closureType: vpr.Type): vpr.DomainFunc = closureLevelFuncOpt.getOrElse {
    val f = vpr.DomainFunc("$closureLevel", Seq(vpr.LocalVarDecl("c", closureType)()), levelType)(domainName = atomDomainName)
    closureLevelFuncOpt = Some(f)
    f
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    if (used) {
      addMemberFn(levelDomain)
      addMemberFn(levelWellFoundedOrderDomain)

      val atoms = registered.toVector.sortBy(_._2.name)
      val orderAxioms = for {
        (l, fl) <- atoms
        (r, fr) <- atoms
        if fl != fr && order.atomLt(l, r)
      } yield vpr.DomainFuncApp(atomLtFunc, Seq(vpr.DomainFuncApp(fl, Seq.empty, Map.empty)(), vpr.DomainFuncApp(fr, Seq.empty, Map.empty)()), Map.empty)()
      // the atom of a method of an imported type is at most the atom of the type
      val atMostAxioms = for {
        (l, fl) <- atoms
        (r, fr) <- atoms
        if fl != fr && order.atomAtMostOnly(l, r)
      } yield {
        val (al, ar) = (vpr.DomainFuncApp(fl, Seq.empty, Map.empty)(), vpr.DomainFuncApp(fr, Seq.empty, Map.empty)())
        vpr.Or(vpr.DomainFuncApp(atomLtFunc, Seq(al, ar), Map.empty)(), vpr.EqCmp(al, ar)())(): vpr.Exp
      }
      // the nil closure cannot be called; its level is empty
      val nilClosureAxiom = closureLevelFuncOpt.map { f =>
        val nil = vpr.DomainFuncApp(Names.closureNilFunc, Seq.empty, Map.empty)(vpr.NoPosition, vpr.NoInfo, f.formalArgs.head.typ, Names.closureDomain, vpr.NoTrafos)
        vpr.EqCmp(vpr.DomainFuncApp(f, Seq(nil), Map.empty)(), vpr.DomainFuncApp(lvlFunc, Seq(vpr.EmptyMultiset(atomType)()), Map.empty)())()
      }
      val axioms = (orderAxioms.distinct ++ atMostAxioms.distinct ++ typeLevelAxioms ++ nilClosureAxiom).map(a => vpr.AnonymousDomainAxiom(a)(domainName = atomDomainName))
      val functions = (mainAtoms.values ++ importedAtoms.values).toVector.sortBy(_.name) ++ typeLevelFuncOpt ++ closureLevelFuncOpt
      addMemberFn(vpr.Domain(atomDomainName, functions, axioms)())
    }
  }

  /*
   * Helpers.
   */

  private var used: Boolean = false

  private def app(f: vpr.DomainFunc, args: Seq[vpr.Exp], src: in.Node): vpr.Exp = {
    used = true
    val (pos, info, errT) = src.vprMeta
    vpr.DomainFuncApp(f, args, Map.empty)(pos, info, errT)
  }

  private def emptyLevel(src: in.Node): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    app(lvlFunc, Seq(vpr.EmptyMultiset(atomType)(pos, info, errT)), src)
  }

  private def union(l: vpr.Exp, r: vpr.Exp, src: in.Node): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    app(lvlFunc, Seq(vpr.AnySetUnion(app(atomsFunc, Seq(l), src), app(atomsFunc, Seq(r), src))(pos, info, errT)), src)
  }

  private def sanitize(key: String): String = key.map(c => if (c.isLetterOrDigit || c == '_') c else '_')
}

object LevelEncoding {
  val levelDomainName: String = "$Level"
  val atomDomainName: String = "$Atom"

  val atomType: vpr.DomainType = vpr.DomainType(atomDomainName, Map.empty)(Seq.empty)
  val levelType: vpr.DomainType = vpr.DomainType(levelDomainName, Map.empty)(Seq.empty)
  private val atomMultisetType: vpr.Type = vpr.MultisetType(atomType)

  private val lvlFunc = vpr.DomainFunc("$lvl", Seq(vpr.LocalVarDecl("m", atomMultisetType)()), levelType)(domainName = levelDomainName)
  val atomsFunc: vpr.DomainFunc = vpr.DomainFunc("$atoms", Seq(vpr.LocalVarDecl("l", levelType)()), atomMultisetType)(domainName = levelDomainName)
  private val atomLtFunc = vpr.DomainFunc("$atomLt", Seq(vpr.LocalVarDecl("a", atomType)(), vpr.LocalVarDecl("b", atomType)()), vpr.Bool)(domainName = levelDomainName)
  private val levelLtFunc = vpr.DomainFunc("$levelLt", Seq(vpr.LocalVarDecl("l", levelType)(), vpr.LocalVarDecl("r", levelType)()), vpr.Bool)(domainName = levelDomainName)
  private val levelMaxFunc = vpr.DomainFunc("$levelMax", Seq(vpr.LocalVarDecl("l", levelType)(), vpr.LocalVarDecl("r", levelType)()), levelType)(domainName = levelDomainName)

  private def call(f: vpr.DomainFunc, args: vpr.Exp*): vpr.DomainFuncApp = vpr.DomainFuncApp(f, args, Map.empty)()

  private lazy val levelDomain: vpr.Domain = {
    val m = vpr.LocalVarDecl("m", atomMultisetType)()
    val l = vpr.LocalVarDecl("l", levelType)()
    val r = vpr.LocalVarDecl("r", levelType)()
    val a = vpr.LocalVarDecl("a", atomType)()
    val x = vpr.LocalVarDecl("x", atomType)()
    val y = vpr.LocalVarDecl("y", atomType)()
    def atoms(v: vpr.LocalVarDecl): vpr.Exp = call(atomsFunc, v.localVar)
    def count(e: vpr.LocalVarDecl, ms: vpr.Exp): vpr.Exp = vpr.AnySetContains(e.localVar, ms)()

    val lvlInj = vpr.Forall(Seq(m), Seq(vpr.Trigger(Seq(call(lvlFunc, m.localVar)))()),
      vpr.EqCmp(call(atomsFunc, call(lvlFunc, m.localVar)), m.localVar)())()
    val atomsInj = vpr.Forall(Seq(l), Seq(vpr.Trigger(Seq(atoms(l)))()),
      vpr.EqCmp(call(lvlFunc, atoms(l)), l.localVar)())()
    val irreflexive = vpr.Forall(Seq(a), Seq(vpr.Trigger(Seq(call(atomLtFunc, a.localVar, a.localVar)))()),
      vpr.Not(call(atomLtFunc, a.localVar, a.localVar))())()

    val xInL = count(x, atoms(l))
    val xInR = count(x, atoms(r))
    val yInL = count(y, atoms(l))
    val yInR = count(y, atoms(r))
    val dominated = vpr.Exists(Seq(y), Seq(vpr.Trigger(Seq(call(atomLtFunc, x.localVar, y.localVar)))()),
      vpr.And(call(atomLtFunc, x.localVar, y.localVar), vpr.GtCmp(yInR, yInL)())())()
    val huetOppen = vpr.Forall(Seq(x), Seq(vpr.Trigger(Seq(xInL))(), vpr.Trigger(Seq(xInR))()),
      vpr.Implies(vpr.GtCmp(xInL, xInR)(), dominated)())()
    val ltDef = vpr.Forall(Seq(l, r), Seq(vpr.Trigger(Seq(call(levelLtFunc, l.localVar, r.localVar)))()),
      vpr.EqCmp(call(levelLtFunc, l.localVar, r.localVar), vpr.And(vpr.NeCmp(atoms(l), atoms(r))(), huetOppen)())())()

    val max = call(levelMaxFunc, l.localVar, r.localVar)
    val maxDef = vpr.Forall(Seq(l, r), Seq(vpr.Trigger(Seq(max))()),
      vpr.And(
        vpr.Or(vpr.EqCmp(max, l.localVar)(), vpr.EqCmp(max, r.localVar)())(),
        vpr.And(
          vpr.Or(vpr.EqCmp(max, l.localVar)(), call(levelLtFunc, l.localVar, max))(),
          vpr.Or(vpr.EqCmp(max, r.localVar)(), call(levelLtFunc, r.localVar, max))()
        )()
      )())()

    vpr.Domain(
      levelDomainName,
      Seq(lvlFunc, atomsFunc, atomLtFunc, levelLtFunc, levelMaxFunc),
      Seq(lvlInj, atomsInj, irreflexive, ltDef, maxDef).map(e => vpr.AnonymousDomainAxiom(e)(domainName = levelDomainName))
    )()
  }

  /** Instance of the well-founded order of Viper's termination plugin for levels. */
  private lazy val levelWellFoundedOrderDomain: vpr.Domain = {
    val name = s"${levelDomainName}WellFoundedOrder"
    val l = vpr.LocalVarDecl("l", levelType)()
    val r = vpr.LocalVarDecl("r", levelType)()
    val typVarMap: Map[vpr.TypeVar, vpr.Type] = Map(vpr.TypeVar("T") -> levelType)
    def wfApp(fname: String, args: Seq[vpr.Exp]): vpr.Exp =
      vpr.DomainFuncApp(fname, args, typVarMap)(vpr.NoPosition, vpr.NoInfo, vpr.Bool, "WellFoundedOrder", vpr.NoTrafos)
    val decreasing = wfApp("decreasing", Seq(l.localVar, r.localVar))
    val bounded = wfApp("bounded", Seq(l.localVar))
    vpr.Domain(
      name,
      Seq.empty,
      Seq(
        vpr.Forall(Seq(l, r), Seq(vpr.Trigger(Seq(decreasing))()), vpr.EqCmp(decreasing, call(levelLtFunc, l.localVar, r.localVar))())(),
        vpr.Forall(Seq(l), Seq(vpr.Trigger(Seq(bounded))()), bounded)(),
      ).map(e => vpr.AnonymousDomainAxiom(e)(domainName = name))
    )()
  }
}
