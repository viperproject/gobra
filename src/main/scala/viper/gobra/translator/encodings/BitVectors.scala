// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import viper.gobra.translator.Names
import viper.gobra.util.TypeBounds.{BoundedIntegerKind, Signed}
import viper.silver.ast.utility.BVFactory
import viper.silver.{ast => vpr}

import scala.collection.mutable

/**
  * Bitvector machinery backing `--interpretBitwise`.
  *
  * Under that flag, the bitwise and shift helpers of [[BoundedIntEncoding]] stop being abstract and
  * receive bodies defined over SMT bitvectors. This class owns everything those bodies need, and is
  * entirely demand-driven: a program that performs no bitwise operation causes nothing to be emitted.
  *
  * Two layers are generated.
  *
  * (1) Per *bit width* (not per kind — `int32`, `uint32`, `rune` and 32-bit `int` all share one), an
  * interpreted Viper domain built by Silver's [[BVFactory]]. Its functions are backend functions:
  * they carry an SMT-LIB name (`bvand`, `bvshl`, `(_ int2bv 32)`, …) and are emitted as builtins
  * rather than declared, so an unused one costs nothing.
  *
  * (2) Per *kind*, a bridge domain holding two ordinary (uninterpreted) domain functions
  * `k$toBv` / `k$fromBv` and four axioms. The wrappers exist for a specific reason: a
  * `BackendFuncApp` is not a `PossibleTrigger` in Silver, so an axiom can never be triggered on
  * `(_ int2bv 32)(i)` directly. Wrapping the conversions in domain functions gives the two
  * round-trip axioms legal triggers, and those axioms are what keep *nested* bitwise expressions
  * tractable: without them, every intermediate result would have to be reconstructed from an opaque
  * `Int`, which does not scale past 8-bit operands.
  *
  * The signed/unsigned distinction lives exclusively in `k$fromBv`. SMT-LIB's `bv2int` is the
  * *unsigned* reading of a bitvector, so signed kinds reinterpret the top half of the range as
  * negative. Everything else (the bitwise operators themselves) is width-agnostic; only the choice
  * between an arithmetic and a logical right shift depends on signedness, and that is made by the
  * caller in [[BoundedIntEncoding]].
  */
class BitVectors {

  // ===== Per-width interpreted domains =====

  /** The backend functions of one bit width. All are builtins; declaring an unused one is free. */
  private case class WidthFuncs(factory: BVFactory) {
    val and:     vpr.DomainFunc = factory.and(Names.bvAnd(factory.size))
    val or:      vpr.DomainFunc = factory.or(Names.bvOr(factory.size))
    val xor:     vpr.DomainFunc = factory.xor(Names.bvXor(factory.size))
    val not:     vpr.DomainFunc = factory.not(Names.bvNot(factory.size))
    val shl:     vpr.DomainFunc = factory.shl(Names.bvShl(factory.size))
    val lshr:    vpr.DomainFunc = factory.lshr(Names.bvLshr(factory.size))
    val ashr:    vpr.DomainFunc = factory.ashr(Names.bvAshr(factory.size))
    val fromInt: vpr.DomainFunc = factory.from_int(Names.bvFromInt(factory.size))
    val toInt:   vpr.DomainFunc = factory.to_int(Names.bvToInt(factory.size))

    def all: Seq[vpr.DomainFunc] = Seq(and, or, xor, not, shl, lshr, ashr, fromInt, toInt)
  }

  private val widthCache: mutable.Map[Int, WidthFuncs] = mutable.Map.empty

  private def widthFuncs(bits: Int): WidthFuncs =
    widthCache.getOrElseUpdate(bits, WidthFuncs(BVFactory(bits)))

  // ===== Per-kind bridge domains =====

  private case class BridgeFuncs(fromBv: vpr.DomainFunc, axioms: Seq[vpr.DomainAxiom])

  private val bridgeCache: mutable.Map[BoundedIntegerKind, BridgeFuncs] = mutable.Map.empty

  private def bridge(k: BoundedIntegerKind): BridgeFuncs =
    bridgeCache.getOrElseUpdate(k, buildBridge(k))

  // ===== Public API used by BoundedIntEncoding =====

  /** The Viper type of the bitvectors backing kind `k`. */
  def typ(k: BoundedIntegerKind): vpr.Type = widthFuncs(k.nbits).factory.typ

  /**
    * The bitvector representing the (in-range) integer `e`.
    *
    * This deliberately applies the backend function — i.e. SMT-LIB's `(_ int2bv n)` — rather than
    * wrapping it in an ordinary domain function. The wrapper is tempting, because it would let the
    * round-trip axioms below trigger on it, but it is fatal for performance: Z3's bitvector rewriter
    * folds `int2bv` of a *numeral* into a bitvector constant and then simplifies terms such as
    * `bvor(b, #x00000000)` away, and it does so syntactically. Behind a wrapper the argument is an
    * uninterpreted application, nothing folds, and the solver falls back to bit-blasting the whole
    * expression. Measured on `x | 0 == x` at 32 bits, end to end: wrapper 90s+ (no result),
    * backend function 5s.
    */
  def toBv(k: BoundedIntegerKind, e: vpr.Exp): vpr.Exp =
    vpr.BackendFuncApp(widthFuncs(k.nbits).fromInt, Seq(e))()

  /** `k$fromBv(e)` — the integer represented by the bitvector `e`, read with `k`'s signedness. */
  def fromBv(k: BoundedIntegerKind, e: vpr.Exp): vpr.Exp =
    vpr.DomainFuncApp(bridge(k).fromBv, Seq(e), Map.empty)()

  def bvAnd(k: BoundedIntegerKind, x: vpr.Exp, y: vpr.Exp): vpr.Exp = app(k, widthFuncs(k.nbits).and, Seq(x, y))
  def bvOr(k: BoundedIntegerKind, x: vpr.Exp, y: vpr.Exp): vpr.Exp = app(k, widthFuncs(k.nbits).or, Seq(x, y))
  def bvXor(k: BoundedIntegerKind, x: vpr.Exp, y: vpr.Exp): vpr.Exp = app(k, widthFuncs(k.nbits).xor, Seq(x, y))
  def bvNot(k: BoundedIntegerKind, x: vpr.Exp): vpr.Exp = app(k, widthFuncs(k.nbits).not, Seq(x))
  def bvShl(k: BoundedIntegerKind, x: vpr.Exp, y: vpr.Exp): vpr.Exp = app(k, widthFuncs(k.nbits).shl, Seq(x, y))

  /**
    * Go's right shift is arithmetic on signed operands (sign-extending) and logical on unsigned ones.
    */
  def bvShr(k: BoundedIntegerKind, x: vpr.Exp, y: vpr.Exp): vpr.Exp = {
    val fns = widthFuncs(k.nbits)
    app(k, if (isSigned(k)) fns.ashr else fns.lshr, Seq(x, y))
  }

  private def app(k: BoundedIntegerKind, fn: vpr.DomainFunc, args: Seq[vpr.Exp]): vpr.Exp =
    vpr.BackendFuncApp(fn, args)()

  /**
    * The domains to emit: one interpreted bitvector domain per width touched, and one bridge domain
    * per kind touched. Empty unless a bitwise operation was actually encoded.
    */
  def members: Seq[vpr.Member] = {
    val bvDomains = widthCache.toSeq.sortBy(_._1).map { case (_, fns) => fns.factory.constructDomain(fns.all) }
    val bridgeDomains = bridgeCache.toSeq.sortBy(_._1.name).map { case (k, b) =>
      vpr.Domain(name = Names.boundedIntBvDomain(k), functions = Seq(b.fromBv), axioms = b.axioms)()
    }
    bvDomains ++ bridgeDomains
  }

  // ===== Construction =====

  private def isSigned(k: BoundedIntegerKind): Boolean = k.isInstanceOf[Signed]

  private def lit(n: BigInt): vpr.Exp = vpr.IntLit(n)()

  /** `k.lower <= e && e <= k.upper` */
  private def inRange(k: BoundedIntegerKind, e: vpr.Exp): vpr.Exp =
    vpr.And(vpr.LeCmp(lit(k.lower), e)(), vpr.LeCmp(e, lit(k.upper))())()

  private def buildBridge(k: BoundedIntegerKind): BridgeFuncs = {
    val domName = Names.boundedIntBvDomain(k)
    val bvTyp = typ(k)
    val fns = widthFuncs(k.nbits)

    val fromBvFn = vpr.DomainFunc(
      name = Names.boundedIntFromBv(k),
      formalArgs = Seq(vpr.LocalVarDecl("b", bvTyp)()),
      typ = vpr.Int
    )(domainName = domName)

    def toBvE(e: vpr.Exp): vpr.Exp = vpr.BackendFuncApp(fns.fromInt, Seq(e))()
    def fromBvE(e: vpr.Exp): vpr.Exp = vpr.DomainFuncApp(fromBvFn, Seq(e), Map.empty)()

    // Axiom 1: forall b :: { fromBv(b) } inRange(fromBv(b)) && fromBv(b) == <value of b>.
    //
    // SMT-LIB's `bv2int` reads a bitvector as *unsigned*, so unsigned kinds use it directly while
    // signed kinds reinterpret the upper half of the range as negative. The range conjunct is a
    // consequence of the value equation (bv2int lands in [0, 2^n)), but stating it explicitly saves
    // the solver from re-deriving it at every use, which is where the cost would otherwise land.
    val fromBvDef = {
      val bDecl = vpr.LocalVarDecl("b", bvTyp)()
      val fb = fromBvE(bDecl.localVar)
      val unsigned: vpr.Exp = vpr.BackendFuncApp(fns.toInt, Seq(bDecl.localVar))()
      val value =
        if (isSigned(k)) {
          vpr.CondExp(
            vpr.LtCmp(unsigned, lit(BigInt(2).pow(k.nbits - 1)))(),
            unsigned,
            vpr.Sub(unsigned, lit(BigInt(2).pow(k.nbits)))()
          )()
        } else unsigned
      vpr.NamedDomainAxiom(
        name = s"${k.name}$$fromBv_def",
        exp = vpr.Forall(
          Seq(bDecl),
          Seq(vpr.Trigger(Seq(fb))()),
          vpr.And(inRange(k, fb), vpr.EqCmp(fb, value)())()
        )()
      )(domainName = domName)
    }

    // Axiom 2: forall b :: { fromBv(b) } int2bv(fromBv(b)) == b.
    // Collapses a bitvector -> Int -> bitvector round trip, which is what lets a nested bitwise
    // expression stay in the bitvector world even though each operation individually hands back an
    // `Int`. Note that this could not be written at all if the conversion were hidden behind a
    // domain function on the `Int` side, since the axiom would then be the one that fixes its
    // meaning rather than a consequence of it.
    val bvRoundTrip = {
      val bDecl = vpr.LocalVarDecl("b", bvTyp)()
      val fb = fromBvE(bDecl.localVar)
      vpr.NamedDomainAxiom(
        name = s"${k.name}$$bv_roundtrip",
        exp = vpr.Forall(
          Seq(bDecl),
          Seq(vpr.Trigger(Seq(fb))()),
          vpr.EqCmp(toBvE(fb), bDecl.localVar)()
        )()
      )(domainName = domName)
    }

    // Axiom 3: forall i: Int :: { fromBv(int2bv(i)) } inRange(i) ==> fromBv(int2bv(i)) == i.
    // The opposite round trip, needed whenever a value re-enters the bitvector world after having
    // been projected to `Int` — which is every operand of every bitwise operation, since the helpers
    // take and return mathematical integers. The range guard is essential: out of range, `int2bv`
    // wraps and the equation is false.
    //
    // The trigger contains a backend function application as a *subterm*. That is allowed (the
    // pattern's head is `fromBv`, an ordinary domain function; only being a trigger in its own right
    // is denied to backend applications) and it is load-bearing: without this axiom the same
    // `x | 0 == x` does not terminate, with it the whole method verifies in 5s.
    val intRoundTrip = {
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val i = iDecl.localVar
      val roundTrip = fromBvE(toBvE(i))
      vpr.NamedDomainAxiom(
        name = s"${k.name}$$int_roundtrip",
        exp = vpr.Forall(
          Seq(iDecl),
          Seq(vpr.Trigger(Seq(roundTrip))()),
          vpr.Implies(inRange(k, i), vpr.EqCmp(roundTrip, i)())()
        )()
      )(domainName = domName)
    }

    BridgeFuncs(fromBvFn, Seq(fromBvDef, bvRoundTrip, intRoundTrip))
  }
}
