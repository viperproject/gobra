// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{OverflowError, ShiftPreconditionError, Source}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.TypeBounds.BoundedIntegerKind
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.termination
import viper.silver.verifier.{errors => err}

import scala.collection.mutable

/**
  * Encoding for bounded integer types (int8, uint8, int16, uint16, int32, uint32, int64, uint64, byte, rune, uintptr).
  *
  * Each bounded integer kind is encoded as its own opaque Viper domain type (e.g. `domain int8 {}`).
  * A pair of abstract Viper functions `int8$from` and `int8$to` bridge between the domain type and
  * mathematical integers (`Int`):
  *   - `int8$from(x: int8): Int` — postcondition: -128 <= result <= 127
  *   - `int8$to(x: Int): int8`   — requires: -128 <= x <= 127; ensures: int8$from(result) == x
  *
  * Arithmetic operations are abstract functions over the domain type whose contracts are stated in
  * terms of `from`. When `checkOverflows` is true, arithmetic functions gain preconditions that
  * make overflow a verification error.
  */
class BoundedIntEncoding(checkOverflows: Boolean) extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  // ===== Per-kind generated functions =====

  private case class KindFunctions(
    from:        vpr.DomainFunc,    // (x: domType): Int  — DOMAIN function; characterised by domain axioms
    rangeAxiom:  vpr.DomainAxiom,   // forall x: domType :: lower <= from(x) && from(x) <= upper
    injAxiom:    vpr.DomainAxiom,   // forall x, y: domType :: from(x) == from(y) ==> x == y
    to:     vpr.Function,  // (x: Int): domType   — requires inRange(x); ensures from(result)==x
    wrap:   vpr.Function,  // (x: Int): domType   — no precondition; ensures inRange(x) ==> from(result)==x
    add:    vpr.Function,  // (x y: domType): Int
    sub:    vpr.Function,  // (x y: domType): Int
    mul:    vpr.Function,  // (x y: domType): Int
    div:    vpr.Function,  // (x y: domType): Int
    mod:    vpr.Function,  // (x y: domType): Int
    band:   vpr.Function,  // (x y: domType): Int
    bor:    vpr.Function,  // (x y: domType): Int
    bxor:   vpr.Function,  // (x y: domType): Int
    bclear: vpr.Function,  // (x y: domType): Int
    bneg:   vpr.Function,  // (x: Int): Int  — BitNeg result type is always UnboundedInteger
    shl:    vpr.Function,  // (x: domType, shift: Int): Int
    shr:    vpr.Function   // (x: domType, shift: Int): Int
  ) {
    /** Top-level Viper functions emitted for this kind (not the domain members). */
    def topLevelFns: Seq[vpr.Function] =
      Seq(to, wrap, add, sub, mul, div, mod, band, bor, bxor, bclear, bneg, shl, shr)
  }

  private val kindCache:         mutable.Map[BoundedIntegerKind, KindFunctions]                   = mutable.Map.empty
  private val convCache:         mutable.Map[(BoundedIntegerKind, BoundedIntegerKind), vpr.Function] = mutable.Map.empty
  private val intToBoundedCache: mutable.Map[BoundedIntegerKind, vpr.Function]                   = mutable.Map.empty

  private def funcsOf(k: BoundedIntegerKind): KindFunctions =
    kindCache.getOrElseUpdate(k, buildKindFunctions(k))

  // ===== Type translation =====

  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.BoundedInt(k) / Exclusive =>
      // Touch the cache so finalize emits the domain — otherwise a kind referenced only via
      // its type (e.g. a `byte` function parameter that's never used in arithmetic) would
      // produce a Viper file mentioning `Bounded_byte` without declaring the domain.
      funcsOf(k)
      domainType(k)
    case ctx.BoundedInt(_) / Shared => vpr.Ref
  }

  // ===== Assignment: normalise RHS to the domain type of the LHS =====
  // Needed because Gobra's internal AST allows unbounded-integer RHS expressions (e.g. IntLit
  // with kind=UnboundedInteger) to be assigned to bounded-integer variables without explicit
  // conversion. The default encoding would produce a Viper type mismatch (Int → DomainType).

  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] =
    default(super.assignment(ctx)) {
      case (in.Assignee((v: in.BodyVar) :: t / Exclusive), rhs, src)
        if ctx.BoundedInt.unapply(t).isDefined && !ctx.BoundedInt.unapply(rhs.typ).isDefined =>
        val k = ctx.BoundedInt.unapply(t).get
        val (pos, info, errT) = src.vprMeta
        for { vRhs <- ctx.expression(rhs) } yield
          vpr.LocalVarAssign(variable(ctx)(v).localVar, asDomain(ctx)(k, rhs, vRhs))(pos, info, errT)

      case (in.Assignee((loc: in.Location) :: t / Shared), rhs, src)
        if ctx.BoundedInt.unapply(t).isDefined && !ctx.BoundedInt.unapply(rhs.typ).isDefined =>
        val k = ctx.BoundedInt.unapply(t).get
        val (pos, info, errT) = src.vprMeta
        for {
          vRhs <- ctx.expression(rhs)
          vLoc <- ctx.expression(loc).map(_.asInstanceOf[vpr.FieldAccess])
        } yield vpr.FieldAssign(vLoc, asDomain(ctx)(k, rhs, vRhs))(pos, info, errT)
    }

  // ===== Equal: use from(lhs) == from/rhs to compare domain values mathematically =====

  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] =
    default(super.equal(ctx)) {
      case (lhs :: ctx.BoundedInt(k), rhs, src) =>
        val (pos, info, errT) = src.vprMeta
        for {
          vLhs <- ctx.expression(lhs)
          vRhs <- ctx.expression(rhs)
        } yield vpr.EqCmp(fromApp(k, vLhs), asInt(ctx)(rhs, vRhs))(pos, info, errT): vpr.Exp
    }

  // ===== Expression encoding =====

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    def handleBoundedBinOp(k: BoundedIntegerKind, func: vpr.Function)(left: in.Expr, right: in.Expr, src: in.Node): CodeWriter[vpr.Exp] = {
      val (pos, info, errT) = src.vprMeta
      for {
        vl <- goE(left)
        vr <- goE(right)
        app = vpr.FuncApp(func, Seq(asDomain(ctx)(k, left, vl), asDomain(ctx)(k, right, vr)))(pos, info, errT)
        _ <- if (checkOverflows) errorT {
          case e @ err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
            OverflowError(info)
        } else unit(())
      } yield app
    }

    default(super.expression(ctx)) {

      // Default value: to(0) — the domain value corresponding to integer 0
      case (e: in.DfltVal) :: ctx.BoundedInt(k) / Exclusive =>
        val (pos, info, errT) = e.vprMeta
        unit(vpr.FuncApp(funcsOf(k).to, Seq(zero))(pos, info, errT))

      // Integer literal of bounded type: to(lit.v)
      case (lit: in.IntLit) :: ctx.BoundedInt(k) =>
        val (pos, info, errT) = lit.vprMeta
        unit(vpr.FuncApp(funcsOf(k).to, Seq(vpr.IntLit(lit.v)()))(pos, info, errT))

      // Arithmetic
      case e @ in.Add(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).add)(l, r, e)
      case e @ in.Sub(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).sub)(l, r, e)
      case e @ in.Mul(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).mul)(l, r, e)
      case e @ in.Div(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).div)(l, r, e)
      case e @ in.Mod(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).mod)(l, r, e)

      // Bitwise binary — wrap Int result back to domain
      case e @ in.BitAnd(l, r)   :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).band)(l, r, e)
      case e @ in.BitOr(l, r)    :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).bor)(l, r, e)
      case e @ in.BitXor(l, r)   :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).bxor)(l, r, e)
      case e @ in.BitClear(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).bclear)(l, r, e)

      // Bitwise unary NOT — BitNeg.typ is always UnboundedInteger; apply from to the operand
      // (operand is bounded by pattern, so ve is a domain value)
      case e @ in.BitNeg(op :: ctx.BoundedInt(k)) =>
        for { ve <- goE(op) } yield withSrc(vpr.FuncApp(funcsOf(k).bneg, Seq(fromApp(k, ve))), e)

      // Shifts — left operand and result are domTyp; right operand is Int.
      case e @ in.ShiftLeft(l, r) :: ctx.BoundedInt(k) =>
        val (pos, info, errT) = e.vprMeta
        for {
          vl <- goE(l)
          vr <- goE(r)
          app = vpr.FuncApp(funcsOf(k).shl, Seq(asDomain(ctx)(k, l, vl), asInt(ctx)(r, vr)))(pos, info, errT)
          _ <- errorT {
            case e2 @ err.PreconditionInAppFalse(Source(info2), _, _) if e2.causedBy(app) =>
              ShiftPreconditionError(info2)
          }
        } yield app

      case e @ in.ShiftRight(l, r) :: ctx.BoundedInt(k) =>
        val (pos, info, errT) = e.vprMeta
        for {
          vl <- goE(l)
          vr <- goE(r)
          app = vpr.FuncApp(funcsOf(k).shr, Seq(asDomain(ctx)(k, l, vl), asInt(ctx)(r, vr)))(pos, info, errT)
          _ <- errorT {
            case e2 @ err.PreconditionInAppFalse(Source(info2), _, _) if e2.causedBy(app) =>
              ShiftPreconditionError(info2)
          }
        } yield app

      // Comparisons — MemoryEncoding is guarded to skip bounded-int operands,
      // so we must handle them here using from(lhs) cmp from/rhs
      case e @ in.LessCmp(l :: ctx.BoundedInt(k), r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.LtCmp(fromApp(k, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      case e @ in.AtMostCmp(l :: ctx.BoundedInt(k), r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.LeCmp(fromApp(k, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      case e @ in.GreaterCmp(l :: ctx.BoundedInt(k), r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.GtCmp(fromApp(k, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      case e @ in.AtLeastCmp(l :: ctx.BoundedInt(k), r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.GeCmp(fromApp(k, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      // Type conversions

      // (1) bounded → different bounded kind
      case conv @ in.Conversion(_, expr :: ctx.BoundedInt(k1))
        if ctx.BoundedInt.unapply(conv.typ).exists(_ != k1) =>
        val k2 = ctx.BoundedInt.unapply(conv.typ).get
        val fn = getConvFunc(k1, k2)
        val (pos, info, errT) = conv.vprMeta
        for {
          ve <- goE(expr)
          app = vpr.FuncApp(fn, Seq(ve))(pos, info, errT)
          _ <- if (checkOverflows) errorT {
            case e @ err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
              OverflowError(info)
          } else unit(())
        } yield app

      // (2) bounded → unbounded: extract the Int value via from
      case conv @ in.Conversion(_, expr :: ctx.BoundedInt(k))
        if ctx.UnboundedInt.unapply(conv.typ) =>
        for { ve <- goE(expr) } yield {
          val (pos, info, errT) = conv.vprMeta
          vpr.DomainFuncApp(funcsOf(k).from.name, Seq(ve), Map.empty)(pos, info, vpr.Int, Names.boundedIntDomain(k), errT)
        }

      // (3) unbounded → bounded
      case conv @ in.Conversion(_, expr)
        if ctx.UnboundedInt.unapply(expr.typ) && ctx.BoundedInt.unapply(conv.typ).isDefined =>
        val k2 = ctx.BoundedInt.unapply(conv.typ).get
        val fn = getIntToBoundedFunc(k2)
        val (pos, info, errT) = conv.vprMeta
        for {
          ve <- goE(expr)
          app = vpr.FuncApp(fn, Seq(ve))(pos, info, errT)
          _ <- if (checkOverflows) errorT {
            case e @ err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
              OverflowError(info)
          } else unit(())
        } yield app
    }
  }

  // ===== Finalize: emit domains and all generated functions =====

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    // Emit one domain per kind, holding `from` and its characterising axioms.
    for ((k, fns) <- kindCache) {
      addMemberFn(vpr.Domain(
        name = Names.boundedIntDomain(k),
        functions = Seq(fns.from),
        axioms = Seq(fns.rangeAxiom, fns.injAxiom)
      )())
    }
    for ((_, fns) <- kindCache)       fns.topLevelFns.foreach(addMemberFn)
    for ((_, fn)  <- convCache)       addMemberFn(fn)
    for ((_, fn)  <- intToBoundedCache) addMemberFn(fn)
    // Emit a well-founded order domain for each kind so termination measures over
    // bounded-int values type-check. Mirrors the shape of `IntWellFoundedOrder` from
    // Silver's `import <decreases/int.vpr>`, but the underlying order is the Int order
    // on `from(x)`, and `bounded` is unconditional (every domain value satisfies
    // `lower <= from(x)` by the range axiom).
    for (k <- kindCache.keys) addMemberFn(buildWellFoundedOrderDomain(k))
  }

  /** Build a `<Bounded_k>WellFoundedOrder` domain with `decreasing`/`bounded` axioms. */
  private def buildWellFoundedOrderDomain(k: BoundedIntegerKind): vpr.Domain = {
    val domTyp = domainType(k)
    val wfDomName = Names.boundedIntDomain(k) + "WellFoundedOrder"

    def wfApp(name: String, args: Seq[vpr.Exp]): vpr.DomainFuncApp =
      vpr.DomainFuncApp(
        funcname = name,
        args = args,
        typVarMap = Map(vpr.TypeVar("T") -> domTyp)
      )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = "WellFoundedOrder", vpr.NoTrafos)

    val xDecl = vpr.LocalVarDecl("x", domTyp)()
    val yDecl = vpr.LocalVarDecl("y", domTyp)()
    val x     = xDecl.localVar
    val y     = yDecl.localVar
    val fx    = fromApp(k, x)
    val fy    = fromApp(k, y)

    // forall x, y: D :: { decreasing(x, y) } from(x) < from(y) ==> decreasing(x, y)
    val decAxiom = vpr.NamedDomainAxiom(
      name = s"${k.name}$$dec_ax",
      exp  = vpr.Forall(
        Seq(xDecl, yDecl),
        Seq(vpr.Trigger(Seq(wfApp("decreasing", Seq(x, y))))()),
        vpr.Implies(vpr.LtCmp(fx, fy)(), wfApp("decreasing", Seq(x, y)))()
      )()
    )(domainName = wfDomName)

    // forall x: D :: { bounded(x) } bounded(x)
    // (Every domain value's `from`-image is >= `lower` by the range axiom, so the
    // standard `lower <= from(x) ==> bounded(x)` simplifies to an unconditional
    // `bounded(x)`. Termination is sound because the Int order on `[lower, upper]`
    // is well-founded.)
    val boundedAxiom = vpr.NamedDomainAxiom(
      name = s"${k.name}$$bounded_ax",
      exp  = vpr.Forall(
        Seq(xDecl),
        Seq(vpr.Trigger(Seq(wfApp("bounded", Seq(x))))()),
        wfApp("bounded", Seq(x))
      )()
    )(domainName = wfDomName)

    vpr.Domain(
      name = wfDomName,
      functions = Seq.empty,
      axioms = Seq(decAxiom, boundedAxiom)
    )()
  }

  // ===== Viper AST helpers =====

  private val zero:      vpr.IntLit = vpr.IntLit(0)()
  private val decreases: vpr.Exp    = termination.DecreasesWildcard(None)()

  private def domainType(k: BoundedIntegerKind): vpr.DomainType =
    vpr.DomainType(Names.boundedIntDomain(k), Map.empty)(Seq.empty)

  /** lower <= e && e <= upper */
  private def inRange(k: BoundedIntegerKind, e: vpr.Exp): vpr.Exp =
    vpr.And(
      vpr.LeCmp(vpr.IntLit(k.lower)(), e)(),
      vpr.LeCmp(e, vpr.IntLit(k.upper)())()
    )()

  /** Apply the `from` domain function of kind k to a domain-typed Viper expression. */
  private def fromApp(k: BoundedIntegerKind, v: vpr.Exp): vpr.Exp =
    vpr.DomainFuncApp(funcsOf(k).from, Seq(v), Map.empty)()

  /**
    * Normalise a Viper expression to the domain type of kind `k`. If the source Gobra
    * expression is already a bounded integer (any kind), its Viper translation is already
    * a domain value — return it unchanged. Otherwise the Viper value is an `Int` (produced
    * by IntEncoding) and must be wrapped to obtain a domain value.
    *
    * Uses `wrap` (not `to`) so there is no precondition to discharge. This matters because
    * Gobra's internal AST mixes unbounded-integer `IntLit`s with bounded operands (e.g.
    * `u + 1` where `u: int`, `1: UnboundedInteger`) without inserting explicit conversions.
    */
  private def asDomain(ctx: Context)(k: BoundedIntegerKind, expr: in.Expr, v: vpr.Exp): vpr.Exp =
    ctx.BoundedInt.unapply(expr.typ) match {
      case Some(_) => v
      case None    => vpr.FuncApp(funcsOf(k).wrap, Seq(v))()
    }

  /**
    * If `expr` has a bounded integer type, wrap `v` with `from`; otherwise return `v` unchanged.
    * Used to normalise the right-hand side of mixed (bounded/unbounded) comparisons and equalities.
    */
  private def asInt(ctx: Context)(expr: in.Expr, v: vpr.Exp): vpr.Exp =
    ctx.BoundedInt.unapply(expr.typ) match {
      case Some(k) => fromApp(k, v)
      case None    => v
    }

  // ===== Build per-kind functions =====

  private def buildKindFunctions(k: BoundedIntegerKind): KindFunctions = {
    val domTyp = domainType(k)
    val domName = Names.boundedIntDomain(k)

    // Build `from` first — all other function contracts reference it.
    // IMPORTANT: use a local `fromE` helper rather than funcsOf(k).from to avoid recursion.
    val fromFn = {
      val xDecl = vpr.LocalVarDecl("x", domTyp)()
      vpr.DomainFunc(
        name       = Names.boundedIntFrom(k),
        formalArgs = Seq(xDecl),
        typ        = vpr.Int
      )(domainName = domName)
    }

    def fromE(e: vpr.Exp): vpr.Exp = vpr.DomainFuncApp(fromFn, Seq(e), Map.empty)()

    // Axiom: forall x: domType :: lower <= from(x) && from(x) <= upper.
    // This replaces the old `from` postcondition: every domain value's image under `from`
    // lies inside the kind's range.
    val rangeAxiom = {
      val xDecl = vpr.LocalVarDecl("x", domTyp)()
      val fx    = fromE(xDecl.localVar)
      val body  = inRange(k, fx)
      vpr.NamedDomainAxiom(
        name = s"${k.name}$$from_in_range",
        exp  = vpr.Forall(Seq(xDecl), Seq(vpr.Trigger(Seq(fx))()), body)()
      )(domainName = domName)
    }

    // Axiom: forall x, y: domType :: from(x) == from(y) ==> x == y.
    // Together with `to`'s postcondition `from(to(n)) == n` for in-range n, this makes
    // `from` a bijection between the domain and the [lower, upper] subset of Int.
    val injAxiom = {
      val xDecl = vpr.LocalVarDecl("x", domTyp)()
      val yDecl = vpr.LocalVarDecl("y", domTyp)()
      val fx    = fromE(xDecl.localVar)
      val fy    = fromE(yDecl.localVar)
      val body  = vpr.Implies(
        vpr.EqCmp(fx, fy)(),
        vpr.EqCmp(xDecl.localVar, yDecl.localVar)()
      )()
      vpr.NamedDomainAxiom(
        name = s"${k.name}$$from_injective",
        exp  = vpr.Forall(Seq(xDecl, yDecl), Seq(vpr.Trigger(Seq(fx, fy))()), body)()
      )(domainName = domName)
    }

    val toFn = {
      val xDecl  = vpr.LocalVarDecl("x", vpr.Int)()
      val x      = xDecl.localVar
      val result = vpr.Result(domTyp)()
      vpr.Function(
        name       = Names.boundedIntTo(k),
        formalArgs = Seq(xDecl),
        typ        = domTyp,
        pres       = Seq(inRange(k, x), decreases),
        posts      = Seq(vpr.EqCmp(fromE(result), x)()),
        body       = None
      )()
    }

    // wrap: total conversion from Int to domTyp. Unlike `to`, it has no precondition;
    // its postcondition is conditional on the input being in range. Used to lift Int
    // results of arithmetic/bitwise/shift functions back into the domain type.
    val wrapFn = {
      val xDecl  = vpr.LocalVarDecl("x", vpr.Int)()
      val x      = xDecl.localVar
      val result = vpr.Result(domTyp)()
      vpr.Function(
        name       = Names.boundedIntWrap(k),
        formalArgs = Seq(xDecl),
        typ        = domTyp,
        pres       = Seq(decreases),
        posts      = Seq(vpr.Implies(inRange(k, x), vpr.EqCmp(fromE(result), x)())()),
        body       = None
      )()
    }

    // Binary arithmetic: args and result are domTyp. Contracts relate `from(result)` to the
    // operation on `from(x)`/`from(y)`. The post is conditional on the result being in range
    // when --overflow is off — overflow values are intentionally left unconstrained.
    def binaryArithFunc(name: String, compute: (vpr.Exp, vpr.Exp) => vpr.Exp,
                        extraPres: Seq[vpr.Exp] = Seq.empty): vpr.Function = {
      val xDecl    = vpr.LocalVarDecl("x", domTyp)()
      val yDecl    = vpr.LocalVarDecl("y", domTyp)()
      val computed = compute(fromE(xDecl.localVar), fromE(yDecl.localVar))
      val result   = vpr.Result(domTyp)()
      val rangeOk  = inRange(k, computed)

      val pres = if (checkOverflows) extraPres ++ Seq(rangeOk, decreases)
                 else                extraPres :+ decreases

      val posts = if (checkOverflows) Seq(vpr.EqCmp(fromE(result), computed)())
                  else                Seq(vpr.Implies(rangeOk, vpr.EqCmp(fromE(result), computed)())())

      vpr.Function(name, Seq(xDecl, yDecl), domTyp, pres, posts, None)()
    }

    val addFn = binaryArithFunc(Names.boundedIntAdd(k), (x, y) => vpr.Add(x, y)())
    val subFn = binaryArithFunc(Names.boundedIntSub(k), (x, y) => vpr.Sub(x, y)())
    val mulFn = binaryArithFunc(Names.boundedIntMul(k), (x, y) => vpr.Mul(x, y)())

    // div: Go truncation-towards-zero semantics; returns domTyp.
    val divFn = {
      val xDecl    = vpr.LocalVarDecl("x", domTyp)()
      val yDecl    = vpr.LocalVarDecl("y", domTyp)()
      val fx       = fromE(xDecl.localVar)
      val fy       = fromE(yDecl.localVar)
      val result   = vpr.Result(domTyp)()
      val yNonZero = vpr.NeCmp(fy, zero)()
      val truncDiv = vpr.CondExp(
        vpr.LeCmp(zero, fx)(),
        vpr.Div(fx, fy)(),
        vpr.Minus(vpr.Div(vpr.Minus(fx)(), fy)())()
      )()
      val rangeOk = inRange(k, truncDiv)
      val pres    = if (checkOverflows) Seq(yNonZero, rangeOk, decreases)
                    else                Seq(yNonZero, decreases)
      val posts   = if (checkOverflows) Seq(vpr.EqCmp(fromE(result), truncDiv)())
                    else                Seq(vpr.Implies(rangeOk, vpr.EqCmp(fromE(result), truncDiv)())())
      vpr.Function(Names.boundedIntDiv(k), Seq(xDecl, yDecl), domTyp, pres, posts, None)()
    }

    // mod: Go truncation-towards-zero semantics; returns domTyp.
    val modFn = {
      val xDecl    = vpr.LocalVarDecl("x", domTyp)()
      val yDecl    = vpr.LocalVarDecl("y", domTyp)()
      val fx       = fromE(xDecl.localVar)
      val fy       = fromE(yDecl.localVar)
      val result   = vpr.Result(domTyp)()
      val yNonZero = vpr.NeCmp(fy, zero)()
      val absY     = vpr.CondExp(vpr.LeCmp(zero, fy)(), fy, vpr.Minus(fy)())()
      val truncMod = vpr.CondExp(
        vpr.Or(vpr.LeCmp(zero, fx)(), vpr.EqCmp(vpr.Mod(fx, fy)(), zero)())(),
        vpr.Mod(fx, fy)(),
        vpr.Sub(vpr.Mod(fx, fy)(), absY)()
      )()
      val rangeOk = inRange(k, truncMod)
      val pres    = if (checkOverflows) Seq(yNonZero, rangeOk, decreases)
                    else                Seq(yNonZero, decreases)
      val posts   = if (checkOverflows) Seq(vpr.EqCmp(fromE(result), truncMod)())
                    else                Seq(vpr.Implies(rangeOk, vpr.EqCmp(fromE(result), truncMod)())())
      vpr.Function(Names.boundedIntMod(k), Seq(xDecl, yDecl), domTyp, pres, posts, None)()
    }

    // Bitwise binary: abstract; returns domTyp.
    def bitwiseBinaryFunc(name: String): vpr.Function = {
      val xDecl  = vpr.LocalVarDecl("x", domTyp)()
      val yDecl  = vpr.LocalVarDecl("y", domTyp)()
      vpr.Function(name, Seq(xDecl, yDecl), domTyp,
        pres  = Seq(decreases),
        posts = Seq.empty,
        body  = None)()
    }

    val bandFn   = bitwiseBinaryFunc(Names.boundedIntBand(k))
    val borFn    = bitwiseBinaryFunc(Names.boundedIntBor(k))
    val bxorFn   = bitwiseBinaryFunc(Names.boundedIntBxor(k))
    val bclearFn = bitwiseBinaryFunc(Names.boundedIntBclear(k))

    // bneg: unary NOT; takes Int (caller applies from to the operand), returns Int
    val bnegFn = {
      val xDecl  = vpr.LocalVarDecl("x", vpr.Int)()
      val result = vpr.Result(vpr.Int)()
      vpr.Function(Names.boundedIntBneg(k), Seq(xDecl), vpr.Int,
        pres  = Seq(decreases),
        posts = Seq(inRange(k, result)),
        body  = None)()
    }

    // shifts: (x: domTyp, shift: Int): domTyp
    def shiftFn(name: String): vpr.Function = {
      val xDecl     = vpr.LocalVarDecl("x",     domTyp)(info = vpr.Synthesized)
      val shiftDecl = vpr.LocalVarDecl("shift",  vpr.Int)(info = vpr.Synthesized)
      vpr.Function(name, Seq(xDecl, shiftDecl), domTyp,
        pres  = Seq(vpr.GeCmp(shiftDecl.localVar, zero)(), decreases),
        posts = Seq.empty,
        body  = None)()
    }

    val shlFn = shiftFn(Names.boundedIntShl(k))
    val shrFn = shiftFn(Names.boundedIntShr(k))

    KindFunctions(fromFn, rangeAxiom, injAxiom, toFn, wrapFn, addFn, subFn, mulFn, divFn, modFn,
      bandFn, borFn, bxorFn, bclearFn, bnegFn, shlFn, shrFn)
  }

  // ===== Conversion functions =====

  private def getConvFunc(from: BoundedIntegerKind, to: BoundedIntegerKind): vpr.Function =
    convCache.getOrElseUpdate((from, to), buildConvFunc(from, to))

  private def getIntToBoundedFunc(to: BoundedIntegerKind): vpr.Function =
    intToBoundedCache.getOrElseUpdate(to, buildIntToBoundedFunc(to))

  /** bounded → different bounded: arg is fromKind domain, result is toKind domain. */
  private def buildConvFunc(fromKind: BoundedIntegerKind, toKind: BoundedIntegerKind): vpr.Function = {
    val fromDomTyp = domainType(fromKind)
    val toDomTyp   = domainType(toKind)
    val xDecl      = vpr.LocalVarDecl("x", fromDomTyp)()
    val x          = xDecl.localVar
    val fx         = fromApp(fromKind, x)
    val result     = vpr.Result(toDomTyp)()
    val fResult    = fromApp(toKind, result)

    val pres  = if (checkOverflows) Seq(inRange(toKind, fx), decreases) else Seq(decreases)
    val posts = if (checkOverflows) Seq(vpr.EqCmp(fResult, fx)())
                else                Seq(vpr.Implies(inRange(toKind, fx), vpr.EqCmp(fResult, fx)())())
    vpr.Function(Names.boundedIntConv(fromKind, toKind), Seq(xDecl), toDomTyp, pres, posts, None)()
  }

  /** unbounded (Int) → bounded: result is toKind domain. */
  private def buildIntToBoundedFunc(toKind: BoundedIntegerKind): vpr.Function = {
    val toDomTyp = domainType(toKind)
    val xDecl    = vpr.LocalVarDecl("x", vpr.Int)()
    val x        = xDecl.localVar
    val result   = vpr.Result(toDomTyp)()
    val fResult  = fromApp(toKind, result)

    val pres  = if (checkOverflows) Seq(inRange(toKind, x), decreases) else Seq(decreases)
    val posts = if (checkOverflows) Seq(vpr.EqCmp(fResult, x)())
                else                Seq(vpr.Implies(inRange(toKind, x), vpr.EqCmp(fResult, x)())())
    vpr.Function(Names.integerToBounded(toKind), Seq(xDecl), toDomTyp, pres, posts, None)()
  }
}
