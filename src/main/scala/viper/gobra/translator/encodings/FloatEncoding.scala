// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeLevel._
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

  /** TODO
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case (e: in.DfltVal) :: ctx.Float32() / Exclusive =>
        unit(withSrc(vpr.FuncApp(func = defaultValue32, Seq()), e))
      case (e: in.DfltVal) :: ctx.Float64() / Exclusive =>
        unit(withSrc(vpr.FuncApp(func = defaultValue64, Seq()), e))
      case add @ in.Add(l, r) :: ctx.Float32() =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(addFloat32, Seq(lEncoded, rEncoded)), add)
      case add @ in.Add(l, r) :: ctx.Float64() =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(addFloat64, Seq(lEncoded, rEncoded)), add)
      case sub @ in.Sub(l :: ctx.Float32(), r :: ctx.Float32()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(subFloat32, Seq(lEncoded, rEncoded)), sub)
      case sub @ in.Sub(l :: ctx.Float64(), r :: ctx.Float64()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(subFloat64, Seq(lEncoded, rEncoded)), sub)
      case mul @ in.Mul(l :: ctx.Float32(), r :: ctx.Float32()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(mulFloat32, Seq(lEncoded, rEncoded)), mul)
      case mul @ in.Mul(l :: ctx.Float64(), r :: ctx.Float64()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(mulFloat64, Seq(lEncoded, rEncoded)), mul)
      case div @ in.Div(l :: ctx.Float32(), r :: ctx.Float32()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(divFloat32, Seq(lEncoded, rEncoded)), div)
      case div @ in.Div(l :: ctx.Float64(), r :: ctx.Float64()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.FuncApp(divFloat64, Seq(lEncoded, rEncoded)), div)
      case conv@in.Conversion(in.Float32T(_), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(fromIntTo32, Seq(e)), conv)
      case conv@in.Conversion(in.Float64T(_), expr :: ctx.Int()) =>
        for { e <- goE(expr) } yield withSrc(vpr.FuncApp(fromIntTo64, Seq(e)), conv)
    }
  }

  override def finalize(col: Collector): Unit = {
    if (isUsed32) {
      col.addMember(defaultValue32)
      col.addMember(addFloat32)
      col.addMember(subFloat32)
      col.addMember(mulFloat32)
      col.addMember(divFloat32)
      col.addMember(fromIntTo32)
    }
    if (isUsed64) {
      col.addMember(defaultValue64)
      col.addMember(addFloat64)
      col.addMember(subFloat64)
      col.addMember(mulFloat64)
      col.addMember(divFloat64)
      col.addMember(fromIntTo64)
    }
  }
  private var isUsed32: Boolean = false
  private var isUsed64: Boolean = false

  private lazy val defaultValue32 = vpr.Function(
    name = "defaultValue32",
    formalArgs = Seq(),
    typ = floatType32,
    pres  = Seq(),
    posts = Seq(),
    body  = None
  )()

  private lazy val defaultValue64 = vpr.Function(
    name = "defaultValue64",
    formalArgs = Seq(),
    typ = floatType64,
    pres  = Seq(),
    posts = Seq(),
    body  = None
  )()

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
}