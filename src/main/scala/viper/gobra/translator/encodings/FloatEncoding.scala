// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.implementation.typing.modifiers.OwnerModifier.{ Shared, Exclusive }
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.unit
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class FloatEncoding extends LeafTypeEncoding {

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

  private lazy val floatType32: vpr.Type = {
    isUsed32 = true
    vpr.Int
  }

  private lazy val floatType64: vpr.Type = {
    isUsed64 = true
    vpr.Int
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    * X stands for either 32 or 64 below:
    * [ dflt(x: floatX) ] -> [ floatX(0) ]
    * [ (x: floatX) + (y: floatX) ] -> addFloatX([ x ], [ y ])
    * [ (x: floatX) - (y: floatX) ] -> subFloatX([ x ], [ y ])
    * [ (x: floatX) * (y: floatX) ] -> mulFloatX([ x ], [ y ])
    * [ (x: floatX) / (y: floatX) ] -> divFloatX([ x ], [ y ])
    * [ floatX(x: int) ] -> fromIntToX([ x ])
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    default(super.expression(ctx)) {
      case (e: in.DfltVal) :: ctx.Float32() / Exclusive =>
        unit(withSrc(vpr.FuncApp(fromIntTo32, Seq(vpr.IntLit(BigInt(0))())), e))
      case (e: in.DfltVal) :: ctx.Float64() / Exclusive =>
        unit(withSrc(vpr.FuncApp(fromIntTo64, Seq(vpr.IntLit(BigInt(0))())), e))
      case add @ in.Add(l, r) :: ctx.Float32() =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(addFloat32, Seq(lE, rE)), add)
      case add @ in.Add(l, r) :: ctx.Float64() =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(addFloat64, Seq(lE, rE)), add)
      case sub @ in.Sub(l :: ctx.Float32(), r :: ctx.Float32()) =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(subFloat32, Seq(lE, rE)), sub)
      case sub @ in.Sub(l :: ctx.Float64(), r :: ctx.Float64()) =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(subFloat64, Seq(lE, rE)), sub)
      case mul @ in.Mul(l :: ctx.Float32(), r :: ctx.Float32()) =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(mulFloat32, Seq(lE, rE)), mul)
      case mul @ in.Mul(l :: ctx.Float64(), r :: ctx.Float64()) =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(mulFloat64, Seq(lE, rE)), mul)
      case div @ in.Div(l :: ctx.Float32(), r :: ctx.Float32()) =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(divFloat32, Seq(lE, rE)), div)
      case div @ in.Div(l :: ctx.Float64(), r :: ctx.Float64()) =>
        for { lE <- goE(l); rE <- goE(r) } yield withSrc(vpr.FuncApp(divFloat64, Seq(lE, rE)), div)
      case conv@in.Conversion(in.Float32T(_), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(fromIntTo32, Seq(e)), conv)
      case conv@in.Conversion(in.Float64T(_), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(fromIntTo64, Seq(e)), conv)
      case conv@in.Conversion(in.IntT(_, _), expr :: ctx.Float32()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(from32ToInt, Seq(e)), conv)
      case conv@in.Conversion(in.IntT(_, _), expr :: ctx.Float64()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(from64ToInt, Seq(e)), conv)
    }
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    if (isUsed32) {
      addMemberFn(addFloat32)
      addMemberFn(subFloat32)
      addMemberFn(mulFloat32)
      addMemberFn(divFloat32)
      addMemberFn(fromIntTo32)
    }
    if (isUsed64) {
      addMemberFn(addFloat64)
      addMemberFn(subFloat64)
      addMemberFn(mulFloat64)
      addMemberFn(divFloat64)
      addMemberFn(fromIntTo64)
      addMemberFn(from32ToInt)
      addMemberFn(from64ToInt)
    }
  }
  private var isUsed32: Boolean = false
  private var isUsed64: Boolean = false

  /**
    * Generates
    *   function addFloat32(l: Int, r: Int): Int
    */
  private lazy val addFloat32 = {
    val argL = vpr.LocalVarDecl("l", floatType32)()
    val argR = vpr.LocalVarDecl("r", floatType32)()
    vpr.Function(
      name = "addFloat32",
      formalArgs = Seq(argL, argR),
      typ = floatType32,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function addFloat64(l: Int, r: Int): Int
    */
  private lazy val addFloat64 = {
    val argL = vpr.LocalVarDecl("l", floatType64)()
    val argR = vpr.LocalVarDecl("r", floatType64)()
    vpr.Function(
      name = "addFloat64",
      formalArgs = Seq(argL, argR),
      typ = floatType64,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function subFloat32(l: Int, r: Int): Int
    */
  private lazy val subFloat32 = {
    val argL = vpr.LocalVarDecl("l", floatType32)()
    val argR = vpr.LocalVarDecl("r", floatType32)()
    vpr.Function(
      name = "subFloat32",
      formalArgs = Seq(argL, argR),
      typ = floatType32,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function subFloat64(l: Int, r: Int): Int
    */
  private lazy val subFloat64 = {
    val argL = vpr.LocalVarDecl("l", floatType64)()
    val argR = vpr.LocalVarDecl("r", floatType64)()
    vpr.Function(
      name = "subFloat64",
      formalArgs = Seq(argL, argR),
      typ = floatType64,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function divFloat32(l: Int, r: Int): Int
    */
  private lazy val divFloat32 = {
    val argL = vpr.LocalVarDecl("l", floatType32)()
    val argR = vpr.LocalVarDecl("r", floatType32)()
    vpr.Function(
      name = "divFloat32",
      formalArgs = Seq(argL, argR),
      typ = floatType32,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function divFloat64(l: Int, r: Int): Int
    */
  private lazy val divFloat64 = {
    val argL = vpr.LocalVarDecl("l", floatType64)()
    val argR = vpr.LocalVarDecl("r", floatType64)()
    vpr.Function(
      name = "divFloat64",
      formalArgs = Seq(argL, argR),
      typ = floatType64,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function mulFloat32(l: Int, r: Int): Int
    */
  private lazy val mulFloat32 = {
    val argL = vpr.LocalVarDecl("l", floatType32)()
    val argR = vpr.LocalVarDecl("r", floatType32)()
    vpr.Function(
      name = "mulFloat32",
      formalArgs = Seq(argL, argR),
      typ = floatType32,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function mulFloat64(l: Int, r: Int): Int
    */
  private lazy val mulFloat64 = {
    val argL = vpr.LocalVarDecl("l", floatType64)()
    val argR = vpr.LocalVarDecl("r", floatType64)()
    vpr.Function(
      name = "mulFloat64",
      formalArgs = Seq(argL, argR),
      typ = floatType64,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function fromIntTo32(l: Int): Int
    */
  private lazy val fromIntTo32 = {
    val arg = vpr.LocalVarDecl("n", floatType32)()
    vpr.Function(
      name = "fromIntTo32",
      formalArgs = Seq(arg),
      typ = floatType32,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function fromIntTo64(l: Int): Int
    */
  private lazy val fromIntTo64 = {
    val arg = vpr.LocalVarDecl("n", floatType64)()
    vpr.Function(
      name = "fromIntTo64",
      formalArgs = Seq(arg),
      typ = floatType64,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function from32ToInt(l: Int): Int
    */
  private lazy val from32ToInt = {
    val arg = vpr.LocalVarDecl("n", floatType32)()
    vpr.Function(
      name = "from32ToInt",
      formalArgs = Seq(arg),
      typ = vpr.Int,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }

  /**
    * Generates
    *   function from64ToInt(l: Int): Int
    */
  private lazy val from64ToInt = {
    val arg = vpr.LocalVarDecl("n", floatType64)()
    vpr.Function(
      name = "from64ToInt",
      formalArgs = Seq(arg),
      typ = vpr.Int,
      pres = Seq(),
      posts = Seq(),
      body = None
    )()
  }
}