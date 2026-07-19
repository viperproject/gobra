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
  * Each bounded integer kind is encoded as its own opaque Viper domain type (e.g. `domain Bounded_int8 {}`).
  * A pair of domain functions `int8$from` and `int8$to`, characterised by two domain axioms, bridge
  * between the domain type and mathematical integers (`Int`):
  *   - `int8$from(x: Bounded_int8): Int`  — axiom: -128 <= from(x) <= 127  (trigger `{ from(x) }`)
  *   - `int8$to(x: Int): Bounded_int8`    — axiom: inRange(n) ==> from(to(n)) == n  (trigger `{ to(n) }`)
  *
  * IMPORTANT (matching loops): the abstract arithmetic/bitwise/shift helper functions operate on
  * `Int` arguments — never on the domain type. This is deliberate. If those helpers took
  * domain-typed parameters and referred to `from(x)`/`from(y)` of their (universally quantified)
  * parameters in their postconditions, Silicon would synthesise quantified axioms reachable from
  * `from`-application triggers. Because `from` appears on essentially every bounded term, Z3's
  * e-matching would keep instantiating those axioms against the `to`/`from` bridge axioms forever
  * — a classic matching loop, which manifested as Gobra hanging on the StatsCollector tests.
  *
  * By keeping the helpers over `Int` and applying `from`/`to` only at concrete (ground) call sites,
  * the only quantified axioms over the domain are `from`'s range axiom, `from`'s injectivity axiom
  * (whose body creates no terms at all), and `to`'s inverse axiom — none of which generates a term
  * matching another's trigger. A bounded binary operation
  * `l <op> r` of kind `k` is therefore encoded as `k$to( k$op( k$from(l), k$from(r) ) )`, where the
  * helper `k$op` carries the range contract (and, when `checkOverflows`, the range precondition
  * that makes overflow a verification error) and `k$to` merely lifts the in-range `Int` back into
  * the domain. Without `checkOverflows`, the result value is known exactly only under the
  * no-overflow condition (`rangeOk ==> result == computed`): proofs about possibly-overflowing
  * arithmetic are meant to break unless the specs exclude the overflow.
  */
class BoundedIntEncoding(checkOverflows: Boolean) extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  // ===== Per-kind generated functions =====

  private case class KindFunctions(
    from:        vpr.DomainFunc,    // (x: domType): Int  — DOMAIN function; characterised by domain axioms
    to:          vpr.DomainFunc,    // (x: Int): domType  — DOMAIN function; characterised by domain axioms
    rangeAxiom:  vpr.DomainAxiom,   // forall x: domType :: lower <= from(x) && from(x) <= upper
    injAxiom:    vpr.DomainAxiom,   // forall x, y: domType :: { from(x), from(y) } from(x) == from(y) ==> x == y
    toFromAxiom: vpr.DomainAxiom,   // forall n: Int    :: { to(n) }   inRange(n) ==> from(to(n)) == n
    add:    vpr.Function,  // (x y: Int): Int
    sub:    vpr.Function,  // (x y: Int): Int
    mul:    vpr.Function,  // (x y: Int): Int
    div:    vpr.Function,  // (x y: Int): Int
    mod:    vpr.Function,  // (x y: Int): Int
    band:   vpr.Function,  // (x y: Int): Int
    bor:    vpr.Function,  // (x y: Int): Int
    bxor:   vpr.Function,  // (x y: Int): Int
    bclear: vpr.Function,  // (x y: Int): Int
    bneg:   vpr.Function,  // (x: Int): Int
    shl:    vpr.Function,  // (x: Int, shift: Int): Int
    shr:    vpr.Function   // (x: Int, shift: Int): Int
  ) {
    /** Top-level Viper functions emitted for this kind (not the domain members). */
    def topLevelFns: Seq[vpr.Function] =
      Seq(add, sub, mul, div, mod, band, bor, bxor, bclear, bneg, shl, shr)
  }

  private val kindCache:         mutable.Map[BoundedIntegerKind, KindFunctions] = mutable.Map.empty
  private val intToBoundedCache: mutable.Map[BoundedIntegerKind, vpr.Function]  = mutable.Map.empty

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

  // ===== Equal: compare the `from`-images so domain values are compared mathematically =====

  /** True iff at least one of the two expressions has a bounded integer type. */
  private def hasBoundedOperand(ctx: Context)(l: in.Expr, r: in.Expr): Boolean =
    ctx.BoundedInt.unapply(l.typ).isDefined || ctx.BoundedInt.unapply(r.typ).isDefined

  // A bounded operand can appear on EITHER side (e.g. `0 == c.BufferSize()`, or mixed
  // bounded/unbounded siblings the desugarer did not align). Both operands are projected
  // to Int, applying `from` to the bounded one(s).
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] =
    default(super.equal(ctx)) {
      case (lhs, rhs, src) if hasBoundedOperand(ctx)(lhs, rhs) =>
        val (pos, info, errT) = src.vprMeta
        for {
          vLhs <- ctx.expression(lhs)
          vRhs <- ctx.expression(rhs)
        } yield vpr.EqCmp(asInt(ctx)(lhs, vLhs), asInt(ctx)(rhs, vRhs))(pos, info, errT): vpr.Exp
    }

  // ===== Expression encoding =====

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    // Encodes `left <op> right` of kind `k` as `to(helper(from(left), from(right)))`. The helper
    // operates on Int and carries the range contract; `to` lifts the in-range result back into the
    // domain. The overflow error (if any) is attributed to the helper application.
    def handleBoundedBinOp(k: BoundedIntegerKind, helper: vpr.Function)(left: in.Expr, right: in.Expr, src: in.Node): CodeWriter[vpr.Exp] = {
      val (pos, info, errT) = src.vprMeta
      for {
        vl <- goE(left)
        vr <- goE(right)
        app = vpr.FuncApp(helper, Seq(asInt(ctx)(left, vl), asInt(ctx)(right, vr)))(pos, info, errT)
        _ <- if (checkOverflows) errorT {
          case e @ err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
            OverflowError(info)
        } else unit(())
      } yield toApp(k, app, pos, info, errT)
    }

    default(super.expression(ctx)) {

      // Default value: to(0) — the domain value corresponding to integer 0
      case (e: in.DfltVal) :: ctx.BoundedInt(k) / Exclusive =>
        val (pos, info, errT) = e.vprMeta
        unit(vpr.DomainFuncApp(funcsOf(k).to, Seq(zero), Map.empty)(pos, info, errT))

      // Integer literal of bounded type: to(lit.v)
      case (lit: in.IntLit) :: ctx.BoundedInt(k) =>
        val (pos, info, errT) = lit.vprMeta
        unit(vpr.DomainFuncApp(funcsOf(k).to, Seq(vpr.IntLit(lit.v)()), Map.empty)(pos, info, errT))

      // Arithmetic
      case e @ in.Add(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).add)(l, r, e)
      case e @ in.Sub(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).sub)(l, r, e)
      case e @ in.Mul(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).mul)(l, r, e)
      case e @ in.Div(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).div)(l, r, e)
      case e @ in.Mod(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).mod)(l, r, e)

      // Bitwise binary — no overflow possible (helper postcondition guarantees range)
      case e @ in.BitAnd(l, r)   :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).band)(l, r, e)
      case e @ in.BitOr(l, r)    :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).bor)(l, r, e)
      case e @ in.BitXor(l, r)   :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).bxor)(l, r, e)
      case e @ in.BitClear(l, r) :: ctx.BoundedInt(k) => handleBoundedBinOp(k, funcsOf(k).bclear)(l, r, e)

      // Bitwise unary NOT — BitNeg.typ is always UnboundedInteger; the operand is bounded by the
      // pattern. Encode as to(bneg(from(op))).
      case e @ in.BitNeg(op :: ctx.BoundedInt(k)) =>
        val (pos, info, errT) = e.vprMeta
        for { ve <- goE(op) } yield
          toApp(k, vpr.FuncApp(funcsOf(k).bneg, Seq(fromApp(k, ve)))(pos, info, errT), pos, info, errT)

      // Shifts — value operand is projected to Int via from; shift amount is Int.
      case e @ in.ShiftLeft(l, r) :: ctx.BoundedInt(k) => handleShift(ctx, k, funcsOf(k).shl)(l, r, e)
      case e @ in.ShiftRight(l, r) :: ctx.BoundedInt(k) => handleShift(ctx, k, funcsOf(k).shr)(l, r, e)

      // Comparisons — MemoryEncoding is guarded to skip comparisons with a bounded-int
      // operand on either side, so we must handle them here. Both operands are projected
      // to Int (`from` is applied to the bounded one(s)); a bounded operand can appear on
      // either side (e.g. `0 < c.BufferSize()`).
      case e @ in.LessCmp(l, r) if hasBoundedOperand(ctx)(l, r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.LtCmp(asInt(ctx)(l, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      case e @ in.AtMostCmp(l, r) if hasBoundedOperand(ctx)(l, r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.LeCmp(asInt(ctx)(l, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      case e @ in.GreaterCmp(l, r) if hasBoundedOperand(ctx)(l, r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.GtCmp(asInt(ctx)(l, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      case e @ in.AtLeastCmp(l, r) if hasBoundedOperand(ctx)(l, r) =>
        val (pos, info, errT) = e.vprMeta
        for { vl <- goE(l); vr <- goE(r) }
          yield vpr.GeCmp(asInt(ctx)(l, vl), asInt(ctx)(r, vr))(pos, info, errT): vpr.Exp

      // Type conversions

      // (1) bounded → different bounded kind: intToBounded_k2(from_k1(x)). The Int→bounded
      //     function's range precondition is the (narrowing) overflow check.
      case conv @ in.Conversion(_, expr :: ctx.BoundedInt(k1))
        if ctx.BoundedInt.unapply(conv.typ).exists(_ != k1) =>
        val k2 = ctx.BoundedInt.unapply(conv.typ).get
        val fn = getIntToBoundedFunc(k2)
        val (pos, info, errT) = conv.vprMeta
        for {
          ve <- goE(expr)
          app = vpr.FuncApp(fn, Seq(fromApp(k1, ve)))(pos, info, errT)
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
    // Emit one domain per kind, holding `from`, `to`, and the bridging axioms.
    //
    // The bijection between the domain and [lower, upper] is axiomatised as:
    //   1. range:        forall x :: { from(x) }          lower <= from(x) <= upper
    //   2. injectivity:  forall x, y :: { from(x), from(y) } from(x) == from(y) ==> x == y
    //   3. right-inverse: forall n :: { to(n) }           inRange(n) ==> from(to(n)) == n
    // We deliberately do NOT emit the left-inverse direction `to(from(x)) == x` triggered
    // on `{from(x)}`: together with (3) it sets up a matching loop in Z3 — each
    // instantiation of `to(from(x))` matches the to-trigger and introduces
    // `from(to(from(x)))`, which matches the from-trigger and introduces
    // `to(from(to(from(x))))`, ad infinitum. The left inverse is nevertheless derivable
    // by e-matching from (2) + (3): a ground term `to(from(x))` instantiates (3) to give
    // `from(to(from(x))) == from(x)`, and (2) then yields `to(from(x)) == x` — without
    // creating further terms.
    for ((k, fns) <- kindCache) {
      addMemberFn(vpr.Domain(
        name = Names.boundedIntDomain(k),
        functions = Seq(fns.from, fns.to),
        axioms = Seq(fns.rangeAxiom, fns.injAxiom, fns.toFromAxiom)
      )())
    }
    for ((_, fns) <- kindCache)       fns.topLevelFns.foreach(addMemberFn)
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

  /** Apply the `to` domain function of kind k to an Int-typed Viper expression. */
  private def toApp(k: BoundedIntegerKind, v: vpr.Exp, pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp =
    vpr.DomainFuncApp(funcsOf(k).to, Seq(v), Map.empty)(pos, info, errT)

  /** Encodes a shift `value <shift_op> amount` of kind `k` as `to(shiftHelper(from(value), amount))`. */
  private def handleShift(ctx: Context, k: BoundedIntegerKind, helper: vpr.Function)(left: in.Expr, right: in.Expr, src: in.Node): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = src.vprMeta
    for {
      vl <- ctx.expression(left)
      vr <- ctx.expression(right)
      app = vpr.FuncApp(helper, Seq(asInt(ctx)(left, vl), asInt(ctx)(right, vr)))(pos, info, errT)
      _ <- errorT {
        case e2 @ err.PreconditionInAppFalse(Source(info2), _, _) if e2.causedBy(app) =>
          ShiftPreconditionError(info2)
      }
    } yield toApp(k, app, pos, info, errT)
  }

  /**
    * Normalise a Viper expression to the domain type of kind `k`. If the source Gobra
    * expression is already a bounded integer, its Viper translation is already a domain value —
    * return it unchanged. Otherwise the Viper value is an `Int` (produced by IntEncoding) and is
    * converted with `to`.
    *
    * This is needed because Gobra's internal AST mixes unbounded-integer `IntLit`s with bounded
    * targets (e.g. an explicit `return 0` where the result is bounded) without inserting explicit
    * conversions.
    */
  private def asDomain(ctx: Context)(k: BoundedIntegerKind, expr: in.Expr, v: vpr.Exp): vpr.Exp =
    ctx.BoundedInt.unapply(expr.typ) match {
      case Some(_) => v
      case None    => vpr.DomainFuncApp(funcsOf(k).to, Seq(v), Map.empty)()
    }

  /**
    * If `expr` has a bounded integer type, wrap `v` with `from`; otherwise return `v` unchanged.
    * Used to normalise operands of mixed (bounded/unbounded) comparisons, equalities, and
    * arithmetic helper applications.
    *
    * Bounded literals deliberately stay as `from(to(c))` rather than being folded to `c`:
    * the ground `to(c)` terms instantiate the bridge axiom and act as congruence anchors
    * linking the domain values to their integer images. Folding them away was measured to
    * send otherwise-fast nonlinear queries (division/multiplication chains) from seconds
    * into multi-minute Z3 timeouts (blank-identifier1: 12s -> 600s+ on identical Silicon).
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

    // Axiom: forall x, y: domType :: { from(x), from(y) } from(x) == from(y) ==> x == y.
    //
    // `from` must be injective: equality of domain values has to coincide with equality of
    // their integer images wherever Viper compares domain values natively — map domains
    // (`k in domain(m)`), set membership, sequence/tuple/ADT equality, etc. The encoder's
    // `equal` override only covers *direct* equalities on bounded-int expressions; without
    // this axiom, `n !in domain(m) && 0 in domain(m)` fails to entail `from(n) != 0` and
    // e.g. map-based caches become unverifiable (same_package/pkg_init/fib regression).
    //
    // The pair trigger cannot cause a matching loop: the axiom's body introduces no new
    // function applications, so instantiations never feed further e-matching rounds. Its
    // cost is quadratic in the number of distinct `from`-terms per query, which stays small
    // now that the arithmetic helpers operate on `Int` (they no longer manufacture
    // `from`-applications of quantified variables; see the class comment).
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

    // `to` is a total domain function (no precondition). Its defining property is
    // expressed by the domain axiom `toFromAxiom` below (under the in-range condition).
    //
    // We deliberately do NOT also emit a `to(from(x)) == x` axiom — the pair of
    // directions would form a matching loop in Z3 (see comment in `finalize`).
    // That direction is instead recovered by `injAxiom` + `toFromAxiom`.
    val toFn = vpr.DomainFunc(
      name       = Names.boundedIntTo(k),
      formalArgs = Seq(vpr.LocalVarDecl("x", vpr.Int)()),
      typ        = domTyp
    )(domainName = domName)

    def toE(e: vpr.Exp): vpr.Exp = vpr.DomainFuncApp(toFn, Seq(e), Map.empty)()

    // Axiom: forall n: Int :: { to(n) } inRange(n) ==> from(to(n)) == n.
    // Replaces `to`'s old function postcondition. The trigger fires only at concrete
    // call sites (e.g. `to(0)` for default values, `to(literal)` for literals) and
    // does not introduce new domain values — bounded the way the encoder uses `to`.
    val toFromAxiom = {
      val nDecl = vpr.LocalVarDecl("n", vpr.Int)()
      val n     = nDecl.localVar
      val tn    = toE(n)
      val body  = vpr.Implies(inRange(k, n), vpr.EqCmp(fromE(tn), n)())()
      vpr.NamedDomainAxiom(
        name = s"${k.name}$$from_to_inverse",
        exp  = vpr.Forall(Seq(nDecl), Seq(vpr.Trigger(Seq(tn))()), body)()
      )(domainName = domName)
    }

    // ----- Int-valued helper functions (arguments are Int, NOT the domain type) -----
    // Keeping the arguments over Int is what prevents the e-matching matching loop: no synthesised
    // axiom mentions `from` of a quantified variable. The result range is asserted unconditionally;
    // the value equality is conditional on no overflow when --overflow is off.

    // The result value is known exactly only when the mathematical result lies within the
    // kind's range (`rangeOk ==> result == computed`); a possibly-overflowing operation is
    // deliberately opaque (only `inRange(result)` is known). Callers must either prove the
    // absence of overflow via their specs or run with --overflow, which turns `rangeOk`
    // into a precondition whose violation is reported as an integer overflow error.
    def binaryArithFunc(name: String, compute: (vpr.Exp, vpr.Exp) => vpr.Exp,
                        extraPres: Seq[vpr.Exp] = Seq.empty): vpr.Function = {
      val xDecl    = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl    = vpr.LocalVarDecl("y", vpr.Int)()
      val computed = compute(xDecl.localVar, yDecl.localVar)
      val result   = vpr.Result(vpr.Int)()
      val rangeOk  = inRange(k, computed)

      val pres = if (checkOverflows) extraPres ++ Seq(rangeOk, decreases)
                 else                extraPres :+ decreases

      val posts = if (checkOverflows) Seq(inRange(k, result), vpr.EqCmp(result, computed)())
                  else                Seq(inRange(k, result), vpr.Implies(rangeOk, vpr.EqCmp(result, computed)())())

      vpr.Function(name, Seq(xDecl, yDecl), vpr.Int, pres, posts, None)()
    }

    val addFn = binaryArithFunc(Names.boundedIntAdd(k), (x, y) => vpr.Add(x, y)())
    val subFn = binaryArithFunc(Names.boundedIntSub(k), (x, y) => vpr.Sub(x, y)())
    val mulFn = binaryArithFunc(Names.boundedIntMul(k), (x, y) => vpr.Mul(x, y)())

    // div: Go truncation-towards-zero semantics; divisor must be non-zero.
    val divFn = {
      val xDecl    = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl    = vpr.LocalVarDecl("y", vpr.Int)()
      val x        = xDecl.localVar
      val y        = yDecl.localVar
      val result   = vpr.Result(vpr.Int)()
      val yNonZero = vpr.NeCmp(y, zero)()
      val truncDiv = vpr.CondExp(
        vpr.LeCmp(zero, x)(),
        vpr.Div(x, y)(),
        vpr.Minus(vpr.Div(vpr.Minus(x)(), y)())()
      )()
      val rangeOk = inRange(k, truncDiv)
      val pres    = if (checkOverflows) Seq(yNonZero, rangeOk, decreases)
                    else                Seq(yNonZero, decreases)
      val posts   = if (checkOverflows) Seq(inRange(k, result), vpr.EqCmp(result, truncDiv)())
                    else                Seq(inRange(k, result), vpr.Implies(rangeOk, vpr.EqCmp(result, truncDiv)())())
      vpr.Function(Names.boundedIntDiv(k), Seq(xDecl, yDecl), vpr.Int, pres, posts, None)()
    }

    // mod: Go truncation-towards-zero semantics; divisor must be non-zero.
    val modFn = {
      val xDecl    = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl    = vpr.LocalVarDecl("y", vpr.Int)()
      val x        = xDecl.localVar
      val y        = yDecl.localVar
      val result   = vpr.Result(vpr.Int)()
      val yNonZero = vpr.NeCmp(y, zero)()
      val absY     = vpr.CondExp(vpr.LeCmp(zero, y)(), y, vpr.Minus(y)())()
      val truncMod = vpr.CondExp(
        vpr.Or(vpr.LeCmp(zero, x)(), vpr.EqCmp(vpr.Mod(x, y)(), zero)())(),
        vpr.Mod(x, y)(),
        vpr.Sub(vpr.Mod(x, y)(), absY)()
      )()
      val rangeOk = inRange(k, truncMod)
      val pres    = if (checkOverflows) Seq(yNonZero, rangeOk, decreases)
                    else                Seq(yNonZero, decreases)
      val posts   = if (checkOverflows) Seq(inRange(k, result), vpr.EqCmp(result, truncMod)())
                    else                Seq(inRange(k, result), vpr.Implies(rangeOk, vpr.EqCmp(result, truncMod)())())
      vpr.Function(Names.boundedIntMod(k), Seq(xDecl, yDecl), vpr.Int, pres, posts, None)()
    }

    // Bitwise binary: abstract; result is in range. Bitwise operations never overflow.
    def bitwiseBinaryFunc(name: String): vpr.Function = {
      val xDecl  = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl  = vpr.LocalVarDecl("y", vpr.Int)()
      val result = vpr.Result(vpr.Int)()
      vpr.Function(name, Seq(xDecl, yDecl), vpr.Int,
        pres  = Seq(decreases),
        posts = Seq(inRange(k, result)),
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

    // shifts: (x: Int, shift: Int): Int; shift amount must be non-negative.
    def shiftFn(name: String): vpr.Function = {
      val xDecl     = vpr.LocalVarDecl("x",     vpr.Int)(info = vpr.Synthesized)
      val shiftDecl = vpr.LocalVarDecl("shift", vpr.Int)(info = vpr.Synthesized)
      val result    = vpr.Result(vpr.Int)()
      vpr.Function(name, Seq(xDecl, shiftDecl), vpr.Int,
        pres  = Seq(vpr.GeCmp(shiftDecl.localVar, zero)(), decreases),
        posts = Seq(inRange(k, result)),
        body  = None)()
    }

    val shlFn = shiftFn(Names.boundedIntShl(k))
    val shrFn = shiftFn(Names.boundedIntShr(k))

    KindFunctions(fromFn, toFn, rangeAxiom, injAxiom, toFromAxiom, addFn, subFn, mulFn, divFn, modFn,
      bandFn, borFn, bxorFn, bclearFn, bnegFn, shlFn, shrFn)
  }

  // ===== Conversion functions =====

  private def getIntToBoundedFunc(to: BoundedIntegerKind): vpr.Function =
    intToBoundedCache.getOrElseUpdate(to, buildIntToBoundedFunc(to))

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
