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
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.util.TypeBounds.BoundedIntegerKind
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.termination
import viper.silver.verifier.{errors => err}

import scala.collection.mutable

/**
  * Encoding for bounded integer types (int8, uint8, int16, uint16, int32, uint32, int64, uint64, byte, rune, uintptr).
  *
  * Each bounded integer type is encoded as vpr.Int at the Viper level (same as unbounded integers),
  * but arithmetic operations are translated to abstract Viper functions with range postconditions.
  * This ensures the verifier knows that results of bounded-integer arithmetic are always in range.
  *
  * When checkOverflows is true, arithmetic functions gain preconditions that make overflow a
  * verification error.
  */
class BoundedIntEncoding(checkOverflows: Boolean) extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}

  // ===== Per-kind generated functions =====

  private case class KindFunctions(
    add: vpr.Function, sub: vpr.Function, mul: vpr.Function,
    div: vpr.Function, mod: vpr.Function,
    band: vpr.Function, bor: vpr.Function, bxor: vpr.Function,
    bclear: vpr.Function, bneg: vpr.Function,
    shl: vpr.Function, shr: vpr.Function
  ) {
    def all: Seq[vpr.Function] =
      Seq(add, sub, mul, div, mod, band, bor, bxor, bclear, bneg, shl, shr)
  }

  private val kindCache: mutable.Map[BoundedIntegerKind, KindFunctions] = mutable.Map.empty
  private val convCache: mutable.Map[(BoundedIntegerKind, BoundedIntegerKind), vpr.Function] = mutable.Map.empty
  private val intToBoundedCache: mutable.Map[BoundedIntegerKind, vpr.Function] = mutable.Map.empty

  private def funcsOf(k: BoundedIntegerKind): KindFunctions =
    kindCache.getOrElseUpdate(k, buildKindFunctions(k))

  // ===== Type translation =====

  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.BoundedInt(_) / Exclusive => vpr.Int
    case ctx.BoundedInt(_) / Shared    => vpr.Ref
  }

  // ===== Range preconditions for parameters =====

  override def varPrecondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = {
    case param if ctx.BoundedInt.unapply(param.typ).isDefined =>
      val k = ctx.BoundedInt.unapply(param.typ).get
      val vParam = variable(ctx)(param).localVar
      ml.unit(inRange(k, vParam))
  }

  override def varPostcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = {
    case param if ctx.BoundedInt.unapply(param.typ).isDefined =>
      val k = ctx.BoundedInt.unapply(param.typ).get
      val vParam = variable(ctx)(param).localVar
      ml.unit(inRange(k, vParam))
  }

  // ===== Expression encoding =====

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    /** Handles a binary operation on a bounded int type, optionally adding an overflow error transformer. */
    def handleBoundedBinOp(func: vpr.Function)(left: in.Expr, right: in.Expr, src: in.Node): CodeWriter[vpr.Exp] = {
      val (pos, info, errT) = src.vprMeta
      for {
        vl <- goE(left)
        vr <- goE(right)
        app = vpr.FuncApp(func, Seq(vl, vr))(pos, info, errT)
        _ <- if (checkOverflows) errorT {
          case e@err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
            OverflowError(info)
        } else unit(())
      } yield app
    }

    def handleShift(shiftFunc: vpr.Function)(left: in.Expr, right: in.Expr): (vpr.Position, vpr.Info, vpr.ErrorTrafo) => CodeWriter[vpr.Exp] = {
      case (pos, info, errT) =>
        for {
          vl <- goE(left)
          vr <- goE(right)
          app = vpr.FuncApp(shiftFunc, Seq(vl, vr))(pos, info, errT)
          _ <- errorT {
            case e@err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
              ShiftPreconditionError(info)
          }
        } yield app
    }

    default(super.expression(ctx)) {
      // Default value
      case (e: in.DfltVal) :: ctx.BoundedInt(_) / Exclusive =>
        unit(withSrc(vpr.IntLit(0), e))

      // Arithmetic
      case e @ in.Add(l, r) :: ctx.BoundedInt(k) =>
        handleBoundedBinOp(funcsOf(k).add)(l, r, e)

      case e @ in.Sub(l, r) :: ctx.BoundedInt(k) =>
        handleBoundedBinOp(funcsOf(k).sub)(l, r, e)

      case e @ in.Mul(l, r) :: ctx.BoundedInt(k) =>
        handleBoundedBinOp(funcsOf(k).mul)(l, r, e)

      case e @ in.Div(l, r) :: ctx.BoundedInt(k) =>
        handleBoundedBinOp(funcsOf(k).div)(l, r, e)

      case e @ in.Mod(l, r) :: ctx.BoundedInt(k) =>
        handleBoundedBinOp(funcsOf(k).mod)(l, r, e)

      // Bitwise binary
      case e @ in.BitAnd(l, r) :: ctx.BoundedInt(k) =>
        val fns = funcsOf(k)
        for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.FuncApp(fns.band, Seq(vl, vr)), e)

      case e @ in.BitOr(l, r) :: ctx.BoundedInt(k) =>
        val fns = funcsOf(k)
        for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.FuncApp(fns.bor, Seq(vl, vr)), e)

      case e @ in.BitXor(l, r) :: ctx.BoundedInt(k) =>
        val fns = funcsOf(k)
        for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.FuncApp(fns.bxor, Seq(vl, vr)), e)

      case e @ in.BitClear(l, r) :: ctx.BoundedInt(k) =>
        val fns = funcsOf(k)
        for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.FuncApp(fns.bclear, Seq(vl, vr)), e)

      // Bitwise unary NOT — match on operand type since BitNeg.typ is always UnboundedInteger
      case e @ in.BitNeg(op :: ctx.BoundedInt(k)) =>
        val fns = funcsOf(k)
        for { ve <- goE(op) } yield withSrc(vpr.FuncApp(fns.bneg, Seq(ve)), e)

      // Shifts
      case e @ in.ShiftLeft(l, r) :: ctx.BoundedInt(k) =>
        val fns = funcsOf(k)
        withSrc(handleShift(fns.shl)(l, r), e)

      case e @ in.ShiftRight(l, r) :: ctx.BoundedInt(k) =>
        val fns = funcsOf(k)
        withSrc(handleShift(fns.shr)(l, r), e)

      // Type conversions:
      // (1) different bounded kinds
      case conv @ in.Conversion(_, expr :: ctx.BoundedInt(k1))
        if ctx.BoundedInt.unapply(conv.typ).exists(_ != k1) =>
        val k2 = ctx.BoundedInt.unapply(conv.typ).get
        val fn = getConvFunc(k1, k2)
        val (pos, info, errT) = conv.vprMeta
        for {
          ve <- goE(expr)
          app = vpr.FuncApp(fn, Seq(ve))(pos, info, errT)
          _ <- if (checkOverflows) errorT {
            case e@err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
              OverflowError(info)
          } else unit(())
        } yield app

      // (2) bounded → unbounded (both vpr.Int, identity)
      case conv @ in.Conversion(_, expr :: ctx.BoundedInt(_))
        if ctx.UnboundedInt.unapply(conv.typ) =>
        goE(expr)

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
            case e@err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
              OverflowError(info)
          } else unit(())
        } yield app
    }
  }

  // ===== Finalize: emit all generated functions =====

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    for ((_, fns) <- kindCache) fns.all.foreach(addMemberFn)
    for ((_, fn) <- convCache) addMemberFn(fn)
    for ((_, fn) <- intToBoundedCache) addMemberFn(fn)
  }

  // ===== Viper AST helpers =====

  private val zero: vpr.IntLit = vpr.IntLit(0)()
  private val decreases: vpr.Exp = termination.DecreasesWildcard(None)()

  /** lower <= e && e <= upper */
  private def inRange(k: BoundedIntegerKind, e: vpr.Exp): vpr.Exp =
    vpr.And(
      vpr.LeCmp(vpr.IntLit(k.lower)(), e)(),
      vpr.LeCmp(e, vpr.IntLit(k.upper)())()
    )()

  // ===== Build per-kind functions =====

  private def buildKindFunctions(k: BoundedIntegerKind): KindFunctions = {
    // Helper: build a binary arithmetic function
    // `compute` takes (x, y) as vpr.Exp and returns the mathematical result expression
    def binaryArithFunc(name: String, compute: (vpr.Exp, vpr.Exp) => vpr.Exp,
                        extraPres: Seq[vpr.Exp] = Seq.empty): vpr.Function = {
      val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl = vpr.LocalVarDecl("y", vpr.Int)()
      val x = xDecl.localVar
      val y = yDecl.localVar
      val computed = compute(x, y)
      val result = vpr.Result(vpr.Int)()
      val rangeOk = inRange(k, computed)

      val pres = if (checkOverflows) {
        extraPres ++ Seq(rangeOk, decreases)
      } else {
        extraPres :+ decreases
      }

      val posts = if (checkOverflows) {
        Seq(inRange(k, result), vpr.EqCmp(result, computed)())
      } else {
        Seq(inRange(k, result), vpr.Implies(rangeOk, vpr.EqCmp(result, computed)())())
      }

      vpr.Function(name = name, formalArgs = Seq(xDecl, yDecl), typ = vpr.Int,
        pres = pres, posts = posts, body = None)()
    }

    val addFunc = binaryArithFunc(Names.boundedIntAdd(k),
      (x, y) => vpr.Add(x, y)())

    val subFunc = binaryArithFunc(Names.boundedIntSub(k),
      (x, y) => vpr.Sub(x, y)())

    val mulFunc = binaryArithFunc(Names.boundedIntMul(k),
      (x, y) => vpr.Mul(x, y)())

    // div: Go truncation-towards-zero semantics
    // goTruncDiv(x, y) = (0 <= x ? x \ y : -((-x) \ y))
    val divFunc = {
      val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl = vpr.LocalVarDecl("y", vpr.Int)()
      val x = xDecl.localVar
      val y = yDecl.localVar
      val result = vpr.Result(vpr.Int)()
      val yNonZero = vpr.NeCmp(y, zero)()
      val truncDiv = vpr.CondExp(
        vpr.LeCmp(zero, x)(),
        vpr.Div(x, y)(),
        vpr.Minus(vpr.Div(vpr.Minus(x)(), y)())()
      )()

      val pres = if (checkOverflows) {
        Seq(yNonZero, inRange(k, truncDiv), decreases)
      } else {
        Seq(yNonZero, decreases)
      }

      val posts = if (checkOverflows) {
        Seq(inRange(k, result), vpr.EqCmp(result, truncDiv)())
      } else {
        Seq(inRange(k, result),
          vpr.Implies(inRange(k, truncDiv), vpr.EqCmp(result, truncDiv)())())
      }

      vpr.Function(name = Names.boundedIntDiv(k), formalArgs = Seq(xDecl, yDecl), typ = vpr.Int,
        pres = pres, posts = posts, body = None)()
    }

    // mod: Go truncation-towards-zero semantics
    // goTruncMod(x, y) = (0 <= x || x % y == 0) ? x % y : x % y - abs(y)
    val modFunc = {
      val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl = vpr.LocalVarDecl("y", vpr.Int)()
      val x = xDecl.localVar
      val y = yDecl.localVar
      val result = vpr.Result(vpr.Int)()
      val yNonZero = vpr.NeCmp(y, zero)()
      val absY = vpr.CondExp(vpr.LeCmp(zero, y)(), y, vpr.Minus(y)())()
      val truncMod = vpr.CondExp(
        vpr.Or(vpr.LeCmp(zero, x)(), vpr.EqCmp(vpr.Mod(x, y)(), zero)())(),
        vpr.Mod(x, y)(),
        vpr.Sub(vpr.Mod(x, y)(), absY)()
      )()

      val pres = if (checkOverflows) {
        Seq(yNonZero, inRange(k, truncMod), decreases)
      } else {
        Seq(yNonZero, decreases)
      }

      val posts = if (checkOverflows) {
        Seq(inRange(k, result), vpr.EqCmp(result, truncMod)())
      } else {
        Seq(inRange(k, result),
          vpr.Implies(inRange(k, truncMod), vpr.EqCmp(result, truncMod)())())
      }

      vpr.Function(name = Names.boundedIntMod(k), formalArgs = Seq(xDecl, yDecl), typ = vpr.Int,
        pres = pres, posts = posts, body = None)()
    }

    // Bitwise binary: abstract with range postcondition only
    def bitwiseBinaryFunc(name: String): vpr.Function = {
      val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
      val yDecl = vpr.LocalVarDecl("y", vpr.Int)()
      val result = vpr.Result(vpr.Int)()
      vpr.Function(name = name, formalArgs = Seq(xDecl, yDecl), typ = vpr.Int,
        pres = Seq(decreases), posts = Seq(inRange(k, result)), body = None)()
    }

    val bandFunc   = bitwiseBinaryFunc(Names.boundedIntBand(k))
    val borFunc    = bitwiseBinaryFunc(Names.boundedIntBor(k))
    val bxorFunc   = bitwiseBinaryFunc(Names.boundedIntBxor(k))
    val bclearFunc = bitwiseBinaryFunc(Names.boundedIntBclear(k))

    // bneg: unary bitwise NOT
    val bnegFunc = {
      val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
      val result = vpr.Result(vpr.Int)()
      vpr.Function(name = Names.boundedIntBneg(k), formalArgs = Seq(xDecl), typ = vpr.Int,
        pres = Seq(decreases), posts = Seq(inRange(k, result)), body = None)()
    }

    // shl/shr: shift amount is Int, precondition: 0 <= shift
    def shiftFunc(name: String): vpr.Function = {
      val xDecl = vpr.LocalVarDecl("x", vpr.Int)(info = vpr.Synthesized)
      val shiftDecl = vpr.LocalVarDecl("shift", vpr.Int)(info = vpr.Synthesized)
      val result = vpr.Result(vpr.Int)()
      vpr.Function(name = name, formalArgs = Seq(xDecl, shiftDecl), typ = vpr.Int,
        pres = Seq(vpr.GeCmp(shiftDecl.localVar, zero)(), decreases),
        posts = Seq(inRange(k, result)), body = None)()
    }

    val shlFunc = shiftFunc(Names.boundedIntShl(k))
    val shrFunc = shiftFunc(Names.boundedIntShr(k))

    KindFunctions(addFunc, subFunc, mulFunc, divFunc, modFunc,
      bandFunc, borFunc, bxorFunc, bclearFunc, bnegFunc, shlFunc, shrFunc)
  }

  // ===== Conversion functions =====

  private def getConvFunc(from: BoundedIntegerKind, to: BoundedIntegerKind): vpr.Function =
    convCache.getOrElseUpdate((from, to), buildConvFunc(from, to))

  private def getIntToBoundedFunc(to: BoundedIntegerKind): vpr.Function =
    intToBoundedCache.getOrElseUpdate(to, buildIntToBoundedFunc(to))

  /** Bounded → different bounded conversion.
    * Postcondition: result is in target range.
    * If the source value is also in target range, result == source.
    * With overflow checking: precondition that source is in target range. */
  private def buildConvFunc(from: BoundedIntegerKind, to: BoundedIntegerKind): vpr.Function = {
    val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
    val x = xDecl.localVar
    val result = vpr.Result(vpr.Int)()
    val pres = if (checkOverflows) Seq(inRange(to, x), decreases) else Seq(decreases)
    val posts = if (checkOverflows) {
      Seq(inRange(to, result), vpr.EqCmp(result, x)())
    } else {
      Seq(inRange(to, result), vpr.Implies(inRange(to, x), vpr.EqCmp(result, x)())())
    }
    vpr.Function(
      name = Names.boundedIntConv(from, to),
      formalArgs = Seq(xDecl), typ = vpr.Int,
      pres = pres, posts = posts, body = None
    )()
  }

  /** Unbounded → bounded conversion.
    * Postcondition: result is in target range.
    * If source is in target range, result == source.
    * With overflow checking: precondition that source is in target range. */
  private def buildIntToBoundedFunc(to: BoundedIntegerKind): vpr.Function = {
    val xDecl = vpr.LocalVarDecl("x", vpr.Int)()
    val x = xDecl.localVar
    val result = vpr.Result(vpr.Int)()
    val pres = if (checkOverflows) Seq(inRange(to, x), decreases) else Seq(decreases)
    val posts = if (checkOverflows) {
      Seq(inRange(to, result), vpr.EqCmp(result, x)())
    } else {
      Seq(inRange(to, result), vpr.Implies(inRange(to, x), vpr.EqCmp(result, x)())())
    }
    vpr.Function(
      name = Names.integerToBounded(to),
      formalArgs = Seq(xDecl), typ = vpr.Int,
      pres = pres, posts = posts, body = None
    )()
  }
}
