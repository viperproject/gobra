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
import viper.gobra.util.Violation.violation
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
  *   // marks the witnesses of assumed comparisons, which trigger the transitivity of $atomLt (see `finalize`)
  *   function $atomLtW(a: $Atom, b: $Atom): Bool
  *
  *   function $union(l: $Level, r: $Level): $Level
  *   axiom { forall l, r :: { $union(l, r) } $atoms($union(l, r)) == $atoms(l) union $atoms(r) }
  *
  *   // multiplicities: $cnt triggers the definition of the order, $cntW (for witnesses) does not
  *   function $cnt(x: $Atom, l: $Level): Int
  *   function $cntW(x: $Atom, l: $Level): Int
  *   axiom { forall x, l :: { $cnt(x, l) } $cnt(x, l) == (x in $atoms(l)) }
  *   axiom { forall x, l :: { $cntW(x, l) } $cntW(x, l) == (x in $atoms(l)) }
  *   axiom { forall x, l, r :: { $cnt(x, $union(l, r)) } $cnt(x, $union(l, r)) == $cnt(x, l) + $cnt(x, r) }
  *
  *   // Huet-Oppen characterization of the multiset ordering
  *   function $levelLt(l: $Level, r: $Level): Bool
  *   axiom { forall l, r :: { $levelLt(l, r) } $levelLt(l, r) <==> $atoms(l) != $atoms(r) &&
  *             forall x :: { $cnt(x, l) } { $cnt(x, r) } $cnt(x, l) > $cnt(x, r) ==>
  *               exists y :: { $atomLt(x, y) } $atomLt(x, y) && $cntW(y, r) > $cntW(y, l) }
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
      case e: in.LevelBottom => unit(emptyLevel(e))
      case e: in.LevelOwn => violation(s"ownLevel() may only occur in contracts, but found it at ${e.info}")
      case a: in.LevelAtom => unit(atomLevel(a.key, a)(ctx))
      case u@in.LevelUnion(l, r) => for { vl <- goE(l); vr <- goE(r) } yield union(vl, vr, u)
      case m@in.LevelMax(l, r) => for { vl <- goE(l); vr <- goE(r) } yield app(levelMaxFunc, Seq(vl, vr), m)
      case lt@in.LevelLess(l, r) =>
        for { vl <- goE(l); vr <- goE(r) } yield withHints(vl, vr, app(levelLtFunc, Seq(vl, vr), lt), lt)
      case le@in.LevelAtMost(l, r) =>
        val (pos, info, errT) = le.vprMeta
        for { vl <- goE(l); vr <- goE(r) } yield withHints(vl, vr, vpr.Or(vpr.EqCmp(vl, vr)(pos, info, errT), app(levelLtFunc, Seq(vl, vr), le))(pos, info, errT), le)
      case lo@in.LevelOf(e) => levelOf(e, lo)(ctx)
    }
  }

  private val interfaces = new viper.gobra.translator.encodings.interfaces.InterfaceComponentImpl

  private def levelOf(e: in.Expr, src: in.Node)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = src.vprMeta
    (e, ctx.underlyingType(e.typ)) match {
      // the level of a named function is its atom (which the closure getter of the function also states), e.g., to
      // bound the levels of objects by the level of the function that created them
      case (in.FunctionObject(func, _), _) => unit(atomLevel(in.TerminationAtom.function(func), src)(ctx))
      case (_, t) => levelOfType(e, t, src)(ctx)
    }
  }

  private def levelOfType(e: in.Expr, typ: in.Type, src: in.Node)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = src.vprMeta
    typ match {
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
          val f = vpr.DomainFunc(s"$mainAtomPrefix$component", Seq.empty, atomType)(domainName = atomDomainName)
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
      // Transitivity of the order on atoms. It is needed to compare levels that are only known to be below some level
      // (e.g., by a postcondition `levelOf(res) < ownLevel()`). The order facts between the atoms above are already
      // transitively closed, so transitivity is only needed for chains that start with an atom of such a level. These
      // chains start with a witness of an assumed comparison, which is marked by `$atomLtW` (see `levelDomain`). A
      // trigger on two applications of `$atomLt` would instead be instantiated O(n^3) times for n atoms in every proof
      // obligation, as all order facts above are applications of `$atomLt`.
      val transitivityAxiom = {
        val (a, b, c) = (vpr.LocalVarDecl("a", atomType)(), vpr.LocalVarDecl("b", atomType)(), vpr.LocalVarDecl("c", atomType)())
        val abW = vpr.DomainFuncApp(atomLtWitnessFunc, Seq(a.localVar, b.localVar), Map.empty)()
        val bc = vpr.DomainFuncApp(atomLtFunc, Seq(b.localVar, c.localVar), Map.empty)()
        val ac = vpr.DomainFuncApp(atomLtFunc, Seq(a.localVar, c.localVar), Map.empty)()
        vpr.Forall(Seq(a, b, c), Seq(vpr.Trigger(Seq(abW, bc))()), vpr.Implies(vpr.And(abW, bc)(), ac)())(): vpr.Exp
      }
      val axioms = (Seq(transitivityAxiom) ++ typeLevelAxioms ++ nilClosureAxiom).map(a => vpr.AnonymousDomainAxiom(a)(domainName = atomDomainName))
      // The order facts are spread over small domains: Viper's chopper hashes the enclosing domain of every axiom it
      // considers, which is quadratic in the number of facts if they all belong to one domain.
      (orderAxioms.distinct ++ atMostAxioms.distinct).grouped(orderDomainSize).zipWithIndex.foreach { case (facts, i) =>
        val name = s"$orderDomainPrefix$i"
        addMemberFn(vpr.Domain(name, Seq.empty, facts.map(a => vpr.AnonymousDomainAxiom(a)(domainName = name)))())
      }
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

  private def union(l: vpr.Exp, r: vpr.Exp, src: in.Node): vpr.Exp = app(unionFunc, Seq(l, r), src)

  /**
    * Conjoins `comparison` with the (trivially true) facts that the multiplicities of the atoms occurring in `l` and `r`
    * are non-negative. Mentioning these multiplicities triggers the axioms needed to find the dominating atoms of the
    * multiset ordering (as the hints of [[viper.gobra.translator.transformers.ModularTerminationTransformer]] do for
    * the termination checks).
    */
  private def withHints(l: vpr.Exp, r: vpr.Exp, comparison: vpr.Exp, src: in.Node): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val atoms = (l.deepCollect { case a: vpr.DomainFuncApp if a.args.isEmpty && a.typ == atomType => a } ++
      r.deepCollect { case a: vpr.DomainFuncApp if a.args.isEmpty && a.typ == atomType => a }).distinct
    val hints = atoms.flatMap(a => Seq(l, r).map(level =>
      vpr.GeCmp(vpr.DomainFuncApp(countFunc, Seq(a, level), Map.empty)(pos, info, errT), vpr.IntLit(0)(pos, info, errT))(pos, info, errT): vpr.Exp))
    hints.foldRight(comparison)((h, acc) => vpr.And(h, acc)(pos, info, errT))
  }

  private def sanitize(key: String): String = key.map(c => if (c.isLetterOrDigit || c == '_') c else '_')
}

object LevelEncoding {
  val levelDomainName: String = "$Level"
  val atomDomainName: String = "$Atom"
  /** Prefix of the domains holding the order facts between atoms (see `finalize`). */
  val orderDomainPrefix: String = "$AtomOrder"
  private val orderDomainSize: Int = 64
  def isLevelDomain(name: String): Boolean =
    name == levelDomainName || name == atomDomainName || name == s"${levelDomainName}WellFoundedOrder" || name.startsWith(orderDomainPrefix)

  val atomType: vpr.DomainType = vpr.DomainType(atomDomainName, Map.empty)(Seq.empty)
  val levelType: vpr.DomainType = vpr.DomainType(levelDomainName, Map.empty)(Seq.empty)
  private val atomMultisetType: vpr.Type = vpr.MultisetType(atomType)

  val lvlFuncName: String = "$lvl"
  /** Prefix of the atoms of the package under verification; all other atoms stem from imported packages. */
  val mainAtomPrefix: String = "$atom_c"
  val atomLtFuncName: String = "$atomLt"
  val unionFuncName: String = "$union"

  private val lvlFunc = vpr.DomainFunc(lvlFuncName, Seq(vpr.LocalVarDecl("m", atomMultisetType)()), levelType)(domainName = levelDomainName)
  val atomsFunc: vpr.DomainFunc = vpr.DomainFunc("$atoms", Seq(vpr.LocalVarDecl("l", levelType)()), atomMultisetType)(domainName = levelDomainName)
  private val atomLtFunc = vpr.DomainFunc(atomLtFuncName, Seq(vpr.LocalVarDecl("a", atomType)(), vpr.LocalVarDecl("b", atomType)()), vpr.Bool)(domainName = levelDomainName)
  /** Marks the order facts that stem from the witnesses of assumed comparisons of levels (see `levelDomain`). */
  private val atomLtWitnessFunc = vpr.DomainFunc("$atomLtW", Seq(vpr.LocalVarDecl("a", atomType)(), vpr.LocalVarDecl("b", atomType)()), vpr.Bool)(domainName = levelDomainName)
  private val levelLtFunc = vpr.DomainFunc("$levelLt", Seq(vpr.LocalVarDecl("l", levelType)(), vpr.LocalVarDecl("r", levelType)()), vpr.Bool)(domainName = levelDomainName)
  private val levelMaxFunc = vpr.DomainFunc("$levelMax", Seq(vpr.LocalVarDecl("l", levelType)(), vpr.LocalVarDecl("r", levelType)()), levelType)(domainName = levelDomainName)
  private val unionFunc = vpr.DomainFunc(unionFuncName, Seq(vpr.LocalVarDecl("l", levelType)(), vpr.LocalVarDecl("r", levelType)()), levelType)(domainName = levelDomainName)
  /** The multiplicity of an atom in a level; its applications trigger the definition of the order on levels. */
  val countFunc: vpr.DomainFunc = vpr.DomainFunc("$cnt", Seq(vpr.LocalVarDecl("x", atomType)(), vpr.LocalVarDecl("l", levelType)()), vpr.Int)(domainName = levelDomainName)
  /** The multiplicity of an atom in a level, used for the witnesses of the order (see below). */
  private val witness1CountFunc = vpr.DomainFunc("$cnt1", Seq(vpr.LocalVarDecl("x", atomType)(), vpr.LocalVarDecl("l", levelType)()), vpr.Int)(domainName = levelDomainName)
  /** The multiplicity of an atom in a level, used for the witnesses of witnesses; it triggers nothing (see below). */
  private val witnessCountFunc = vpr.DomainFunc("$cntW", Seq(vpr.LocalVarDecl("x", atomType)(), vpr.LocalVarDecl("l", levelType)()), vpr.Int)(domainName = levelDomainName)

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

    val unionLR = call(unionFunc, l.localVar, r.localVar)
    val unionDef = vpr.Forall(Seq(l, r), Seq(vpr.Trigger(Seq(unionLR))()),
      vpr.EqCmp(call(atomsFunc, unionLR), vpr.AnySetUnion(atoms(l), atoms(r))())())()

    // both count functions are the multiplicity of an atom in a level
    val countDef = vpr.Forall(Seq(x, l), Seq(vpr.Trigger(Seq(call(countFunc, x.localVar, l.localVar)))()),
      vpr.EqCmp(call(countFunc, x.localVar, l.localVar), count(x, atoms(l)))())()
    val witnessCountDef = vpr.Forall(Seq(x, l), Seq(vpr.Trigger(Seq(call(witnessCountFunc, x.localVar, l.localVar)))()),
      vpr.EqCmp(call(witnessCountFunc, x.localVar, l.localVar), count(x, atoms(l)))())()
    val witness1CountDef = vpr.Forall(Seq(x, l), Seq(vpr.Trigger(Seq(call(witness1CountFunc, x.localVar, l.localVar)))()),
      vpr.EqCmp(call(witness1CountFunc, x.localVar, l.localVar), count(x, atoms(l)))())()
    val witness1CountUnion = vpr.Forall(Seq(x, l, r), Seq(vpr.Trigger(Seq(call(witness1CountFunc, x.localVar, unionLR)))()),
      vpr.EqCmp(call(witness1CountFunc, x.localVar, unionLR), vpr.Add(call(witness1CountFunc, x.localVar, l.localVar), call(witness1CountFunc, x.localVar, r.localVar))())())()
    // the multiplicities in a union are the sums of the multiplicities in its operands, which exposes the
    // multiplicities in the operands to the definition of the order
    val countUnion = vpr.Forall(Seq(x, l, r), Seq(vpr.Trigger(Seq(call(countFunc, x.localVar, unionLR)))()),
      vpr.EqCmp(call(countFunc, x.localVar, unionLR), vpr.Add(call(countFunc, x.localVar, l.localVar), call(countFunc, x.localVar, r.localVar))())())()

    /*
     * Huet-Oppen characterization of the multiset ordering. The universally quantified atom `x` is matched via `$cnt`,
     * whereas the witnesses `y` are stated with `$cnt1`, and the witnesses of a second instance (`dominated2`, which
     * only follows from `l < r`) with `$cntW`. All three are the multiplicity of an atom, but only `$cnt` and `$cnt1`
     * trigger a quantifier, each a different one. With a single count function, each witness would trigger the
     * quantifier again, whose instantiation would produce a new witness, and so on (a matching loop that occurs as soon
     * as some `l < r` is assumed, e.g., after a termination check). The two layers allow chaining two facts `a < b` and
     * `b' < c` where the dominating atom of the first one lies in an opaque level `b'` (e.g., `levelOf(x)`).
     */
    /*
     * The witnesses of assumed comparisons are additionally marked with `$atomLtW`, which triggers the transitivity of
     * the order on atoms (see `finalize`). The marker is not required to prove a comparison.
     */
    def dominatedBy(countF: vpr.DomainFunc, assumed: Boolean): vpr.Exp = {
      val xy = call(atomLtFunc, x.localVar, y.localVar)
      val witness = vpr.And(xy, vpr.GtCmp(call(countF, y.localVar, r.localVar), call(countF, y.localVar, l.localVar))())()
      vpr.Exists(Seq(y), Seq(vpr.Trigger(Seq(xy))()),
        if (assumed) vpr.And(call(atomLtWitnessFunc, x.localVar, y.localVar), witness)() else witness)()
    }
    def huetOppenWith(matchF: vpr.DomainFunc, witnessF: vpr.DomainFunc, assumed: Boolean): vpr.Exp = {
      val xInL = call(matchF, x.localVar, l.localVar)
      val xInR = call(matchF, x.localVar, r.localVar)
      vpr.Forall(Seq(x), Seq(vpr.Trigger(Seq(xInL))(), vpr.Trigger(Seq(xInR))()),
        vpr.Implies(vpr.GtCmp(xInL, xInR)(), dominatedBy(witnessF, assumed))())()
    }
    val lr = call(levelLtFunc, l.localVar, r.localVar)
    val ltSecondLayer = vpr.Forall(Seq(l, r), Seq(vpr.Trigger(Seq(lr))()),
      vpr.Implies(lr, huetOppenWith(witness1CountFunc, witnessCountFunc, assumed = true))())()
    // the two directions differ only in the marker of the witnesses
    val ltDef = vpr.Forall(Seq(l, r), Seq(vpr.Trigger(Seq(lr))()),
      vpr.And(
        vpr.Implies(lr, vpr.And(vpr.NeCmp(atoms(l), atoms(r))(), huetOppenWith(countFunc, witness1CountFunc, assumed = true))())(),
        vpr.Implies(vpr.And(vpr.NeCmp(atoms(l), atoms(r))(), huetOppenWith(countFunc, witness1CountFunc, assumed = false))(), lr)()
      )())()

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
      Seq(lvlFunc, atomsFunc, atomLtFunc, atomLtWitnessFunc, levelLtFunc, levelMaxFunc, unionFunc, countFunc, witness1CountFunc, witnessCountFunc),
      Seq(lvlInj, atomsInj, irreflexive, unionDef, countDef, witness1CountDef, witnessCountDef, countUnion, witness1CountUnion, ltDef,
        ltSecondLayer, maxDef).map(e => vpr.AnonymousDomainAxiom(e)(domainName = levelDomainName))
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
