// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.unit
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.ast.utility.{BVFactory, FloatFactory, RoundingMode}
import viper.silver.{ast => vpr}

import scala.collection.mutable

/**
  * Encodes float32 and float64 into the SMT-LIB theory of IEEE 754 floating points
  * (binary32 and binary64, respectively) via Viper's backend-interpreted domains:
  *
  *   domain FloatDomain24e8 interpretation (SMTLIB: "(_ FloatingPoint 8 24)", ...) {
  *     function addFloat32(x: FloatDomain24e8, y: FloatDomain24e8): FloatDomain24e8 interpretation "fp.add RNE"
  *     ...
  *   }
  *
  * Design points:
  * - The rounding mode is RNE (round to nearest, ties to even), which is the rounding Go uses.
  * - Literals are encoded as their exact IEEE bit pattern, reinterpreted with `(_ to_fp e m)`
  *   from a bitvector; the rounding of the decimal literal happens here, at encoding time.
  * - Go's ==, !=, <, <=, >, >= on floats are the IEEE comparisons (fp.eq etc.), under which
  *   NaN is not equal to anything (including itself) and +0.0 equals -0.0. Viper-internal
  *   (structural) equality, used for assignments, framing, and ghost equality ===, remains
  *   SMT equality, under which every value is equal to itself.
  * - Division is total in IEEE 754 (x/±0.0 is ±Inf or NaN), so no non-zero precondition is
  *   generated, unlike for integer division.
  * - Conversions from integers go through `(_ int2bv 64)` and the signed-bitvector overload of
  *   `to_fp`; they are exact for all bounded integer kinds. Conversions from floats to integers
  *   are currently encoded as an uninterpreted function (sound, but nothing is known about the
  *   result).
  */
class FloatEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  /** IEEE binary32 and binary64. FloatFactory counts the hidden bit as part of the significand. */
  private val f32Factory = FloatFactory(24, 8, RoundingMode.RNE)
  private val f64Factory = FloatFactory(53, 11, RoundingMode.RNE)
  private val bv32Factory = BVFactory(32)
  private val bv64Factory = BVFactory(64)

  private val roundingMode = RoundingMode.RNE

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Float32() / Exclusive => vprFloat32
    case ctx.Float64() / Exclusive => vprFloat64
    case ctx.Float32() / Shared => vpr.Ref
    case ctx.Float64() / Shared => vpr.Ref
  }

  private lazy val vprFloat32: vpr.Type = {
    isUsed32 = true
    f32Factory.typ
  }

  private lazy val vprFloat64: vpr.Type = {
    isUsed64 = true
    f64Factory.typ
  }

  private var isUsed32: Boolean = false
  private var isUsed64: Boolean = false

  /** Domain functions are registered here on first use so that [[finalize]] only emits what the program needs. */
  private val usedF32Funcs = mutable.LinkedHashSet.empty[vpr.DomainFunc]
  private val usedF64Funcs = mutable.LinkedHashSet.empty[vpr.DomainFunc]
  private val usedBV32Funcs = mutable.LinkedHashSet.empty[vpr.DomainFunc]
  private val usedBV64Funcs = mutable.LinkedHashSet.empty[vpr.DomainFunc]
  private val usedFunctions = mutable.LinkedHashSet.empty[vpr.Function]

  private def interpFunc(name: String, args: Seq[vpr.Type], ret: vpr.Type, interp: String, domain: String, registry: mutable.LinkedHashSet[vpr.DomainFunc]): vpr.DomainFunc = {
    val formals = args.zipWithIndex map { case (t, i) => vpr.LocalVarDecl(s"x$i", t)() }
    val func = vpr.DomainFunc(name, formals, ret, unique = false, interpretation = Some(interp))(domainName = domain)
    registry += func
    func
  }

  private def f32Func(name: String, args: Seq[vpr.Type], ret: vpr.Type, interp: String): vpr.DomainFunc =
    interpFunc(name, args, ret, interp, f32Factory.name, usedF32Funcs)

  private def f64Func(name: String, args: Seq[vpr.Type], ret: vpr.Type, interp: String): vpr.DomainFunc =
    interpFunc(name, args, ret, interp, f64Factory.name, usedF64Funcs)

  /* Binary arithmetic */
  private lazy val addFloat32 = f32Func("addFloat32", Seq(vprFloat32, vprFloat32), vprFloat32, s"fp.add $roundingMode")
  private lazy val subFloat32 = f32Func("subFloat32", Seq(vprFloat32, vprFloat32), vprFloat32, s"fp.sub $roundingMode")
  private lazy val mulFloat32 = f32Func("mulFloat32", Seq(vprFloat32, vprFloat32), vprFloat32, s"fp.mul $roundingMode")
  private lazy val divFloat32 = f32Func("divFloat32", Seq(vprFloat32, vprFloat32), vprFloat32, s"fp.div $roundingMode")
  private lazy val addFloat64 = f64Func("addFloat64", Seq(vprFloat64, vprFloat64), vprFloat64, s"fp.add $roundingMode")
  private lazy val subFloat64 = f64Func("subFloat64", Seq(vprFloat64, vprFloat64), vprFloat64, s"fp.sub $roundingMode")
  private lazy val mulFloat64 = f64Func("mulFloat64", Seq(vprFloat64, vprFloat64), vprFloat64, s"fp.mul $roundingMode")
  private lazy val divFloat64 = f64Func("divFloat64", Seq(vprFloat64, vprFloat64), vprFloat64, s"fp.div $roundingMode")

  /* Unary minus */
  private lazy val negFloat32 = f32Func("negFloat32", Seq(vprFloat32), vprFloat32, "fp.neg")
  private lazy val negFloat64 = f64Func("negFloat64", Seq(vprFloat64), vprFloat64, "fp.neg")

  /* IEEE comparisons (Go's comparison semantics on floats) */
  private lazy val eqFloat32 = f32Func("eqFloat32", Seq(vprFloat32, vprFloat32), vpr.Bool, "fp.eq")
  private lazy val ltFloat32 = f32Func("ltFloat32", Seq(vprFloat32, vprFloat32), vpr.Bool, "fp.lt")
  private lazy val leqFloat32 = f32Func("leqFloat32", Seq(vprFloat32, vprFloat32), vpr.Bool, "fp.leq")
  private lazy val gtFloat32 = f32Func("gtFloat32", Seq(vprFloat32, vprFloat32), vpr.Bool, "fp.gt")
  private lazy val geqFloat32 = f32Func("geqFloat32", Seq(vprFloat32, vprFloat32), vpr.Bool, "fp.geq")
  private lazy val eqFloat64 = f64Func("eqFloat64", Seq(vprFloat64, vprFloat64), vpr.Bool, "fp.eq")
  private lazy val ltFloat64 = f64Func("ltFloat64", Seq(vprFloat64, vprFloat64), vpr.Bool, "fp.lt")
  private lazy val leqFloat64 = f64Func("leqFloat64", Seq(vprFloat64, vprFloat64), vpr.Bool, "fp.leq")
  private lazy val gtFloat64 = f64Func("gtFloat64", Seq(vprFloat64, vprFloat64), vpr.Bool, "fp.gt")
  private lazy val geqFloat64 = f64Func("geqFloat64", Seq(vprFloat64, vprFloat64), vpr.Bool, "fp.geq")

  /* Bitvector construction (for literals) and integer conversions */
  private lazy val intToBV32 = interpFunc("intToBV32", Seq(vpr.Int), bv32Factory.typ, "(_ int2bv 32)", bv32Factory.name, usedBV32Funcs)
  private lazy val intToBV64 = interpFunc("intToBV64", Seq(vpr.Int), bv64Factory.typ, "(_ int2bv 64)", bv64Factory.name, usedBV64Funcs)

  /** Reinterprets the bits of a bitvector as a float (used to encode literals exactly). */
  private lazy val float32FromBits = f32Func("float32FromBits", Seq(bv32Factory.typ), vprFloat32, "(_ to_fp 8 24)")
  private lazy val float64FromBits = f64Func("float64FromBits", Seq(bv64Factory.typ), vprFloat64, "(_ to_fp 11 53)")

  /** Converts a signed 64-bit bitvector to a float (the signed-integer overload of to_fp). */
  private lazy val intToFloat32 = f32Func("intToFloat32", Seq(bv64Factory.typ), vprFloat32, s"(_ to_fp 8 24) $roundingMode")
  private lazy val intToFloat64 = f64Func("intToFloat64", Seq(bv64Factory.typ), vprFloat64, s"(_ to_fp 11 53) $roundingMode")

  /* float32 <-> float64 conversions */
  private lazy val float32ToFloat64 = f64Func("float32ToFloat64", Seq(vprFloat32), vprFloat64, s"(_ to_fp 11 53) $roundingMode")
  private lazy val float64ToFloat32 = f32Func("float64ToFloat32", Seq(vprFloat64), vprFloat32, s"(_ to_fp 8 24) $roundingMode")

  /** Conversions from floats to integers are encoded as uninterpreted functions for now:
    * `fp.to_sbv` is undefined for NaN, infinities, and out-of-range values, so a faithful
    * encoding requires generating the corresponding verification conditions first. */
  private lazy val float32ToInt = {
    val func = vpr.Function("float32ToInt", Seq(vpr.LocalVarDecl("x", vprFloat32)()), vpr.Int, Seq(), Seq(), None)()
    usedFunctions += func
    func
  }
  private lazy val float64ToInt = {
    val func = vpr.Function("float64ToInt", Seq(vpr.LocalVarDecl("x", vprFloat64)()), vpr.Int, Seq(), Seq(), None)()
    usedFunctions += func
    func
  }

  /** Encodes a float literal as its exact IEEE 754 bit pattern. The decimal literal is rounded
    * (to nearest, ties to even) to the target width here, at encoding time, mirroring how the Go
    * compiler converts constants to values. */
  private def float32Lit(lit: in.FloatLit): vpr.Exp = {
    val bits = BigInt(java.lang.Integer.toUnsignedLong(java.lang.Float.floatToIntBits(lit.v.toFloat)))
    withSrc(vpr.BackendFuncApp(float32FromBits, Seq(withSrc(vpr.BackendFuncApp(intToBV32, Seq(withSrc(vpr.IntLit(bits), lit))), lit))), lit)
  }

  private def float64Lit(lit: in.FloatLit): vpr.Exp = {
    val bits = BigInt(java.lang.Double.doubleToLongBits(lit.v.toDouble)) & ((BigInt(1) << 64) - 1)
    withSrc(vpr.BackendFuncApp(float64FromBits, Seq(withSrc(vpr.BackendFuncApp(intToBV64, Seq(withSrc(vpr.IntLit(bits), lit))), lit))), lit)
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    * X stands for either 32 or 64 below:
    * [ lit: floatX ] -> floatXFromBits(intToBVX(<IEEE bits of lit>))
    * [ dflt(x: floatX) ] -> floatXFromBits(intToBVX(0))     (i.e. +0.0)
    * [ (0.0: floatX) - (y: floatX) ] -> fp.neg([ y ])       (unary minus, see comment below)
    * [ (x: floatX) ⊕ (y: floatX) ] -> fp.⊕ RNE ([ x ], [ y ])   for ⊕ in + - * /
    * [ (x: floatX) ⊗ (y: floatX) ] -> fp.⊗ ([ x ], [ y ])       for ⊗ in < <= > >=
    * [ floatX(e: int) ] -> to_fp RNE (int2bv64([ e ]))
    * [ float64(e: float32) ] -> to_fp RNE ([ e ])   (and vice versa)
    * [ int(e: floatX) ] -> floatXToInt([ e ])       (uninterpreted)
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    def binary(func: vpr.DomainFunc, l: in.Expr, r: in.Expr, src: in.Node): CodeWriter[vpr.Exp] =
      for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.BackendFuncApp(func, Seq(lE, rE)), src)

    def unary(func: vpr.DomainFunc, e: in.Expr, src: in.Node): CodeWriter[vpr.Exp] =
      for { eE <- goE(e) } yield withSrc(vpr.BackendFuncApp(func, Seq(eE)), src)

    default(super.expression(ctx)) {
      case (lit: in.FloatLit) :: ctx.Float32() / Exclusive => unit(float32Lit(lit))
      case (lit: in.FloatLit) :: ctx.Float64() / Exclusive => unit(float64Lit(lit))

      case (e: in.DfltVal) :: ctx.Float32() / Exclusive =>
        unit(withSrc(vpr.BackendFuncApp(float32FromBits, Seq(withSrc(vpr.BackendFuncApp(intToBV32, Seq(withSrc(vpr.IntLit(BigInt(0)), e))), e))), e))
      case (e: in.DfltVal) :: ctx.Float64() / Exclusive =>
        unit(withSrc(vpr.BackendFuncApp(float64FromBits, Seq(withSrc(vpr.BackendFuncApp(intToBV64, Seq(withSrc(vpr.IntLit(BigInt(0)), e))), e))), e))

      // The parser translates unary minus `-x` into `0 - x`. A subtraction from literal zero is
      // therefore encoded as IEEE negation so that `-x` flips the sign of ±0.0 (whereas
      // `fp.sub RNE +0.0 +0.0` is +0.0, Go's unary minus yields -0.0). As the two forms cannot be
      // distinguished after parsing, a user-written `0.0 - x` is also encoded as fp.neg(x); the
      // two encodings differ only in the sign of the result when x is ±0.0.
      case sub@in.Sub(in.FloatLit(z, _), r) :: ctx.Float32() if z.signum == 0 => unary(negFloat32, r, sub)
      case sub@in.Sub(in.FloatLit(z, _), r) :: ctx.Float64() if z.signum == 0 => unary(negFloat64, r, sub)

      case add@in.Add(l, r) :: ctx.Float32() => binary(addFloat32, l, r, add)
      case add@in.Add(l, r) :: ctx.Float64() => binary(addFloat64, l, r, add)
      case sub@in.Sub(l, r) :: ctx.Float32() => binary(subFloat32, l, r, sub)
      case sub@in.Sub(l, r) :: ctx.Float64() => binary(subFloat64, l, r, sub)
      case mul@in.Mul(l, r) :: ctx.Float32() => binary(mulFloat32, l, r, mul)
      case mul@in.Mul(l, r) :: ctx.Float64() => binary(mulFloat64, l, r, mul)
      // IEEE division is total (x/±0.0 is ±Inf or NaN), so, unlike for integers, no non-zero
      // precondition is generated
      case div@in.Div(l, r) :: ctx.Float32() => binary(divFloat32, l, r, div)
      case div@in.Div(l, r) :: ctx.Float64() => binary(divFloat64, l, r, div)

      case n@in.LessCmp(l :: ctx.Float32(), r) => binary(ltFloat32, l, r, n)
      case n@in.AtMostCmp(l :: ctx.Float32(), r) => binary(leqFloat32, l, r, n)
      case n@in.GreaterCmp(l :: ctx.Float32(), r) => binary(gtFloat32, l, r, n)
      case n@in.AtLeastCmp(l :: ctx.Float32(), r) => binary(geqFloat32, l, r, n)
      case n@in.LessCmp(l :: ctx.Float64(), r) => binary(ltFloat64, l, r, n)
      case n@in.AtMostCmp(l :: ctx.Float64(), r) => binary(leqFloat64, l, r, n)
      case n@in.GreaterCmp(l :: ctx.Float64(), r) => binary(gtFloat64, l, r, n)
      case n@in.AtLeastCmp(l :: ctx.Float64(), r) => binary(geqFloat64, l, r, n)

      case conv@in.Conversion(ctx.Float32(), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.BackendFuncApp(intToFloat32, Seq(withSrc(vpr.BackendFuncApp(intToBV64, Seq(e)), conv))), conv)
      case conv@in.Conversion(ctx.Float64(), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.BackendFuncApp(intToFloat64, Seq(withSrc(vpr.BackendFuncApp(intToBV64, Seq(e)), conv))), conv)

      case conv@in.Conversion(ctx.Float64(), expr :: ctx.Float32()) => unary(float32ToFloat64, expr, conv)
      case conv@in.Conversion(ctx.Float32(), expr :: ctx.Float64()) => unary(float64ToFloat32, expr, conv)

      case conv@in.Conversion(in.IntT(_, _), expr :: ctx.Float32()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(float32ToInt, Seq(e)), conv)
      case conv@in.Conversion(in.IntT(_, _), expr :: ctx.Float64()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(float64ToInt, Seq(e)), conv)
    }
  }

  /**
    * Encodes Go's == on floats as IEEE 754 equality (fp.eq), under which NaN != NaN and
    * +0.0 == -0.0. Viper-internal structural equality (assignments, framing) and the ghost
    * equality === are not affected and remain reflexive.
    */
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: ctx.Float32(), rhs :: ctx.Float32(), src) =>
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield withSrc(vpr.BackendFuncApp(eqFloat32, Seq(vLhs, vRhs)), src)
    case (lhs :: ctx.Float64(), rhs :: ctx.Float64(), src) =>
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield withSrc(vpr.BackendFuncApp(eqFloat64, Seq(vLhs, vRhs)), src)
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    // A BackendType is only consistent if its interpreted domain is part of the program,
    // so the domains are emitted whenever the corresponding type occurs.
    if (isUsed32) addMemberFn(f32Factory.constructDomain(usedF32Funcs.toSeq))
    if (isUsed64) addMemberFn(f64Factory.constructDomain(usedF64Funcs.toSeq))
    if (usedBV32Funcs.nonEmpty) addMemberFn(bv32Factory.constructDomain(usedBV32Funcs.toSeq))
    if (usedBV64Funcs.nonEmpty) addMemberFn(bv64Factory.constructDomain(usedBV64Funcs.toSeq))
    usedFunctions.foreach(addMemberFn)
  }
}
