// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{ShiftPreconditionError, Source}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.verifier.{errors => err}
import viper.silver.{ast => vpr}

class IntEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  private var isUsedBitAnd: Boolean = false
  private var isUsedBitOr: Boolean = false
  private var isUsedBitXor: Boolean = false
  private var isUsedBitClear: Boolean = false
  private var isUsedLeftShift: Boolean = false
  private var isUsedRightShift: Boolean = false
  private var isUsedBitNeg: Boolean = false

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Int() / m =>
      m match {
        case Exclusive => vpr.Int
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def handleShift(shiftFunc: vpr.Function)(left: in.Expr, right: in.Expr): (vpr.Position, vpr.Info, vpr.ErrorTrafo) => CodeWriter[vpr.Exp]  = {
      case (pos, info, errT) =>
        for {
          vl <- goE(left);
          vr <- goE(right)
          app = vpr.FuncApp(shiftFunc, Seq(vl, vr))(pos, info, errT)
          _ <- errorT {
            case e@err.PreconditionInAppFalse(Source(info), _, _) if e.causedBy(app) =>
              ShiftPreconditionError(info)
          }
        } yield app
    }

    default(super.expr(ctx)){
      case (e: in.DfltVal) :: ctx.Int() / Exclusive => unit(withSrc(vpr.IntLit(0), e))
      case lit: in.IntLit => unit(withSrc(vpr.IntLit(lit.v), lit))

      case e@ in.Add(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Add(vl, vr), e)
      case e@ in.Sub(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Sub(vl, vr), e)
      case e@ in.Mul(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Mul(vl, vr), e)
      case e@ in.Mod(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Mod(vl, vr), e)
      case e@ in.Div(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Div(vl, vr), e)

      case e@ in.BitAnd(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitwiseAnd, Seq(vl, vr)), e)
      case e@ in.BitOr(l, r)  :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitwiseOr,  Seq(vl, vr)), e)
      case e@ in.BitXor(l, r) :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitwiseXor, Seq(vl, vr)), e)
      case e@ in.BitClear(l, r)   :: ctx.Int() => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitClear, Seq(vl, vr)), e)
      case e@ in.ShiftLeft(l, r)  :: ctx.Int() => withSrc(handleShift(shiftLeft)(l, r), e)
      case e@ in.ShiftRight(l, r) :: ctx.Int() => withSrc(handleShift(shiftRight)(l, r), e)
      case e@ in.BitNeg(exp) :: ctx.Int()  => for {ve <- goE(exp)} yield withSrc(vpr.FuncApp(bitwiseNegation, Seq(ve)), e)
    }
  }

  override def finalize(col: Collector): Unit = {
    if(isUsedBitAnd) { col.addMember(bitwiseAnd) }
    if(isUsedBitOr) { col.addMember(bitwiseOr) }
    if(isUsedBitXor) { col.addMember(bitwiseXor) }
    if(isUsedBitClear) { col.addMember(bitClear) }
    if(isUsedLeftShift) { col.addMember(shiftLeft) }
    if(isUsedRightShift) { col.addMember(shiftRight) }
    if(isUsedBitNeg) { col.addMember(bitwiseNegation) }
  }

  /* Bitwise Operations */
  private lazy val bitwiseAnd: vpr.Function = {
    isUsedBitAnd = true
    vpr.Function(
      name = Names.bitwiseAnd,
      formalArgs = Seq(vpr.LocalVarDecl("left", vpr.Int)(), vpr.LocalVarDecl("right", vpr.Int)()),
      typ = vpr.Int,
      pres = Seq.empty,
      posts = Seq.empty,
      body = None
    )()
  }

  private lazy val bitwiseOr: vpr.Function = {
    isUsedBitOr = true
    vpr.Function(
      name = Names.bitwiseOr,
      formalArgs = Seq(vpr.LocalVarDecl("left", vpr.Int)(), vpr.LocalVarDecl("right", vpr.Int)()),
      typ = vpr.Int,
      pres = Seq.empty,
      posts = Seq.empty,
      body = None
    )()
  }

  private lazy val bitwiseXor: vpr.Function = {
    isUsedBitXor = true
    vpr.Function(
      name = Names.bitwiseXor,
      formalArgs = Seq(vpr.LocalVarDecl("left", vpr.Int)(), vpr.LocalVarDecl("right", vpr.Int)()),
      typ = vpr.Int,
      pres = Seq.empty,
      posts = Seq.empty,
      body = None
    )()
  }

  private lazy val bitClear: vpr.Function = {
    isUsedBitClear = true
    vpr.Function(
      name = Names.bitClear,
      formalArgs = Seq(vpr.LocalVarDecl("left", vpr.Int)(), vpr.LocalVarDecl("right", vpr.Int)()),
      typ = vpr.Int,
      pres = Seq.empty,
      posts = Seq.empty,
      body = None
    )()
  }

  private lazy val shiftLeft: vpr.Function = {
    isUsedLeftShift = true
    val left = vpr.LocalVarDecl("left", vpr.Int)(info = vpr.Synthesized)
    val right = vpr.LocalVarDecl("right", vpr.Int)(info = vpr.Synthesized)
    vpr.Function(
      name = Names.shiftLeft,
      formalArgs = Seq(left, right),
      typ = vpr.Int,
      // if the value at the right is < 0, it panics
      pres = Seq(vpr.GeCmp(right.localVar, vpr.IntLit(BigInt(0))())()),
      posts = Seq.empty,
      body = None
    )()
  }

  private lazy val shiftRight: vpr.Function = {
    isUsedRightShift = true
    val left = vpr.LocalVarDecl("left", vpr.Int)()
    val right = vpr.LocalVarDecl("right", vpr.Int)()
    vpr.Function(
      name = Names.shiftRight,
      formalArgs = Seq(left, right),
      typ = vpr.Int,
      // if the value at the right is < 0, it panics
      pres = Seq(vpr.GeCmp(right.localVar, vpr.IntLit(BigInt(0))())()),
      posts = Seq.empty,
      body = None
    )()
  }

  private lazy val bitwiseNegation: vpr.Function = {
    isUsedBitNeg = true
    vpr.Function(
      name = Names.bitwiseNeg,
      formalArgs = Seq(vpr.LocalVarDecl("exp", vpr.Int)()),
      typ = vpr.Int,
      pres = Seq.empty,
      posts = Seq.empty,
      body = None
    )()
  }
}