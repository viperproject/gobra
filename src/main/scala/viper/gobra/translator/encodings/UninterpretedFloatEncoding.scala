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
import viper.silver.{ast => vpr}

import scala.collection.mutable

/**
  * Encodes float32/float64 as viper Int together with uninterpreted, axiom-free functions for all
  * operations. This encoding knows nothing about the arithmetic of floats (two float expressions
  * are only known to be equal if they are built from syntactically equal function applications),
  * but it is much cheaper for the SMT solver than the IEEE 754 theory used by [[FloatEncoding]].
  * It is enabled with the `--uninterpretedFloats` flag.
  *
  * NaN and the sign of zero are not modeled. To remain sound nonetheless, Go's == on floats is
  * encoded as an uninterpreted symmetric predicate: in particular, `x == x` is NOT provable
  * (x may be NaN), and neither is its negation. The structural ghost equality === remains
  * available for specifications.
  */
class UninterpretedFloatEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Float32() / Exclusive => floatType32
    case ctx.Float64() / Exclusive => floatType64
    case ctx.Float32() / Shared => vpr.Ref
    case ctx.Float64() / Shared => vpr.Ref
  }

  private lazy val floatType32: vpr.Type = vpr.Int
  private lazy val floatType64: vpr.Type = vpr.Int

  /** Functions are registered here on first use so that [[finalize]] only emits what the program needs. */
  private val usedFunctions = mutable.LinkedHashSet.empty[vpr.Function]

  private def uninterpreted(name: String, args: Seq[vpr.Type], ret: vpr.Type): vpr.Function = {
    val formals = args.zipWithIndex map { case (t, i) => vpr.LocalVarDecl(s"x$i", t)() }
    val func = vpr.Function(name, formals, ret, Seq(), Seq(), None)()
    usedFunctions += func
    func
  }

  private lazy val addFloat32 = uninterpreted("addFloat32", Seq(floatType32, floatType32), floatType32)
  private lazy val subFloat32 = uninterpreted("subFloat32", Seq(floatType32, floatType32), floatType32)
  private lazy val mulFloat32 = uninterpreted("mulFloat32", Seq(floatType32, floatType32), floatType32)
  private lazy val divFloat32 = uninterpreted("divFloat32", Seq(floatType32, floatType32), floatType32)
  private lazy val addFloat64 = uninterpreted("addFloat64", Seq(floatType64, floatType64), floatType64)
  private lazy val subFloat64 = uninterpreted("subFloat64", Seq(floatType64, floatType64), floatType64)
  private lazy val mulFloat64 = uninterpreted("mulFloat64", Seq(floatType64, floatType64), floatType64)
  private lazy val divFloat64 = uninterpreted("divFloat64", Seq(floatType64, floatType64), floatType64)

  private lazy val ltFloat32 = uninterpreted("ltFloat32", Seq(floatType32, floatType32), vpr.Bool)
  private lazy val leqFloat32 = uninterpreted("leqFloat32", Seq(floatType32, floatType32), vpr.Bool)
  private lazy val ltFloat64 = uninterpreted("ltFloat64", Seq(floatType64, floatType64), vpr.Bool)
  private lazy val leqFloat64 = uninterpreted("leqFloat64", Seq(floatType64, floatType64), vpr.Bool)

  /** A float literal is represented by an uninterpreted function of its IEEE bit pattern:
    * syntactically equal literals are known to be equal, and nothing else is known. */
  private lazy val litFloat32 = uninterpreted("litFloat32", Seq(vpr.Int), floatType32)
  private lazy val litFloat64 = uninterpreted("litFloat64", Seq(vpr.Int), floatType64)

  /** Go's == on floats is encoded as an uninterpreted predicate: this encoding does not model
    * which values are NaN, so neither `x == x` (which is false in Go when x is NaN) nor its
    * negation may be provable. Symmetry, which holds for IEEE 754 equality, is its only axiom. */
  private val eqDomainName = "UninterpretedFloatEquality"
  private val usedEqFuncs = mutable.LinkedHashSet.empty[vpr.DomainFunc]

  private def eqFunc(name: String, typ: vpr.Type): vpr.DomainFunc = {
    val func = vpr.DomainFunc(name, Seq(vpr.LocalVarDecl("x", typ)(), vpr.LocalVarDecl("y", typ)()), vpr.Bool, unique = false, interpretation = None)(domainName = eqDomainName)
    usedEqFuncs += func
    func
  }

  private lazy val eqFloat32 = eqFunc("eqFloat32", floatType32)
  private lazy val eqFloat64 = eqFunc("eqFloat64", floatType64)

  private def eqSymmetryAxioms(): Seq[vpr.DomainAxiom] = usedEqFuncs.toSeq.map { f =>
    val xDecl = vpr.LocalVarDecl("x", f.formalArgs.head.typ)()
    val yDecl = vpr.LocalVarDecl("y", f.formalArgs.head.typ)()
    def app(a: vpr.Exp, b: vpr.Exp): vpr.Exp = vpr.DomainFuncApp(f, Seq(a, b), Map.empty[vpr.TypeVar, vpr.Type])()
    val x = xDecl.localVar
    val y = yDecl.localVar
    val body = vpr.Forall(
      Seq(xDecl, yDecl),
      Seq(vpr.Trigger(Seq(app(x, y)))()),
      vpr.EqCmp(app(x, y), app(y, x))()
    )()
    vpr.AnonymousDomainAxiom(body)(domainName = eqDomainName): vpr.DomainAxiom
  }

  /**
    * Encodes Go's == on floats as the uninterpreted predicate above. The structural (ghost)
    * equality === and Viper-internal equality remain unaffected.
    */
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: ctx.Float32(), rhs :: ctx.Float32(), src) =>
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield withSrc(vpr.DomainFuncApp(eqFloat32, Seq(vLhs, vRhs), Map.empty[vpr.TypeVar, vpr.Type]), src)
    case (lhs :: ctx.Float64(), rhs :: ctx.Float64(), src) =>
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield withSrc(vpr.DomainFuncApp(eqFloat64, Seq(vLhs, vRhs), Map.empty[vpr.TypeVar, vpr.Type]), src)
  }

  private lazy val fromIntTo32 = uninterpreted("fromIntTo32", Seq(vpr.Int), floatType32)
  private lazy val fromIntTo64 = uninterpreted("fromIntTo64", Seq(vpr.Int), floatType64)
  private lazy val from32ToInt = uninterpreted("from32ToInt", Seq(floatType32), vpr.Int)
  private lazy val from64ToInt = uninterpreted("from64ToInt", Seq(floatType64), vpr.Int)
  private lazy val from32To64 = uninterpreted("from32To64", Seq(floatType32), floatType64)
  private lazy val from64To32 = uninterpreted("from64To32", Seq(floatType64), floatType32)

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    * X stands for either 32 or 64 below:
    * [ lit: floatX ] -> litFloatX(<IEEE bits of lit>)
    * [ dflt(x: floatX) ] -> litFloatX(0)
    * [ (x: floatX) ⊕ (y: floatX) ] -> ⊕FloatX([ x ], [ y ])   for ⊕ in + - * /
    * [ (x: floatX) < (y: floatX) ] -> ltFloatX([ x ], [ y ])   (<=, >, >= analogously)
    * [ floatX(e: int) ] -> fromIntToX([ e ])
    * [ int(e: floatX) ] -> fromXToInt([ e ])
    * [ float64(e: float32) ] -> from32To64([ e ])   (and vice versa)
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    def binary(func: vpr.Function, l: in.Expr, r: in.Expr, src: in.Node): CodeWriter[vpr.Exp] =
      for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(func, Seq(lE, rE)), src)

    def float32Lit(lit: in.FloatLit): vpr.Exp = {
      val bits = BigInt(java.lang.Integer.toUnsignedLong(java.lang.Float.floatToIntBits(lit.v.toFloat)))
      withSrc(vpr.FuncApp(litFloat32, Seq(withSrc(vpr.IntLit(bits), lit))), lit)
    }

    def float64Lit(lit: in.FloatLit): vpr.Exp = {
      val bits = BigInt(java.lang.Double.doubleToLongBits(lit.v.toDouble)) & ((BigInt(1) << 64) - 1)
      withSrc(vpr.FuncApp(litFloat64, Seq(withSrc(vpr.IntLit(bits), lit))), lit)
    }

    default(super.expression(ctx)) {
      case (lit: in.FloatLit) :: ctx.Float32() / Exclusive => unit(float32Lit(lit))
      case (lit: in.FloatLit) :: ctx.Float64() / Exclusive => unit(float64Lit(lit))

      case (e: in.DfltVal) :: ctx.Float32() / Exclusive =>
        unit(withSrc(vpr.FuncApp(litFloat32, Seq(withSrc(vpr.IntLit(BigInt(0)), e))), e))
      case (e: in.DfltVal) :: ctx.Float64() / Exclusive =>
        unit(withSrc(vpr.FuncApp(litFloat64, Seq(withSrc(vpr.IntLit(BigInt(0)), e))), e))

      case add@in.Add(l, r) :: ctx.Float32() => binary(addFloat32, l, r, add)
      case add@in.Add(l, r) :: ctx.Float64() => binary(addFloat64, l, r, add)
      case sub@in.Sub(l, r) :: ctx.Float32() => binary(subFloat32, l, r, sub)
      case sub@in.Sub(l, r) :: ctx.Float64() => binary(subFloat64, l, r, sub)
      case mul@in.Mul(l, r) :: ctx.Float32() => binary(mulFloat32, l, r, mul)
      case mul@in.Mul(l, r) :: ctx.Float64() => binary(mulFloat64, l, r, mul)
      case div@in.Div(l, r) :: ctx.Float32() => binary(divFloat32, l, r, div)
      case div@in.Div(l, r) :: ctx.Float64() => binary(divFloat64, l, r, div)

      case n@in.LessCmp(l :: ctx.Float32(), r) => binary(ltFloat32, l, r, n)
      case n@in.AtMostCmp(l :: ctx.Float32(), r) => binary(leqFloat32, l, r, n)
      case n@in.GreaterCmp(l :: ctx.Float32(), r) => binary(ltFloat32, r, l, n)
      case n@in.AtLeastCmp(l :: ctx.Float32(), r) => binary(leqFloat32, r, l, n)
      case n@in.LessCmp(l :: ctx.Float64(), r) => binary(ltFloat64, l, r, n)
      case n@in.AtMostCmp(l :: ctx.Float64(), r) => binary(leqFloat64, l, r, n)
      case n@in.GreaterCmp(l :: ctx.Float64(), r) => binary(ltFloat64, r, l, n)
      case n@in.AtLeastCmp(l :: ctx.Float64(), r) => binary(leqFloat64, r, l, n)

      case conv@in.Conversion(ctx.Float32(), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(fromIntTo32, Seq(e)), conv)
      case conv@in.Conversion(ctx.Float64(), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(fromIntTo64, Seq(e)), conv)
      case conv@in.Conversion(ctx.Float64(), expr :: ctx.Float32()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(from32To64, Seq(e)), conv)
      case conv@in.Conversion(ctx.Float32(), expr :: ctx.Float64()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(from64To32, Seq(e)), conv)
      case conv@in.Conversion(in.IntT(_, _), expr :: ctx.Float32()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(from32ToInt, Seq(e)), conv)
      case conv@in.Conversion(in.IntT(_, _), expr :: ctx.Float64()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(from64ToInt, Seq(e)), conv)
    }
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    if (usedEqFuncs.nonEmpty) {
      addMemberFn(vpr.Domain(eqDomainName, usedEqFuncs.toSeq, eqSymmetryAxioms(), Seq.empty, interpretations = None)())
    }
    usedFunctions.foreach(addMemberFn)
  }
}
