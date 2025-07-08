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
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.DomainGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.TypeBounds.IntegerKind
import viper.silver.ast.Domain
import viper.silver.plugin.standard.termination
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
  private var isUsedGoIntDiv: Boolean = false
  private var isUsedGoIntMod: Boolean = false

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Int(kind) / m =>
      m match {
        case Exclusive => IntDomainGenerator(Vector.empty, kind)(ctx)
        case Shared    => vpr.Ref
      }
  }
  private case object IntDomainGenerator extends DomainGenerator[IntegerKind] {
    def add(x: IntegerKind)(ctx: Context) = {
      val dom = genDomain(x)(ctx)
      add_internal()
    }
    def sub() = ???
    private def add_internal() = ???
    private def sub_internal() = ???
    override def genDomain(x: IntegerKind)(ctx: Context): Domain = ???
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)
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

    default(super.expression(ctx)){
      case (e: in.DfltVal) :: ctx.Int(kind) / Exclusive => unit(withSrc(vpr.IntLit(0), e))
      case lit: in.IntLit => unit(withSrc(vpr.IntLit(lit.v), lit))

      case e@ in.Add(l, r) :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Add(vl, vr), e)
      case e@ in.Sub(l, r) :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Sub(vl, vr), e)
      case e@ in.Mul(l, r) :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Mul(vl, vr), e)
      case e@ in.Mod(l, r) :: ctx.Int(kind) =>
        // We currently implement our own modulo algorithm to mimic what Go does. The default modulo implementation in
        // Viper does not match Go's semantics. Check https://github.com/viperproject/gobra/issues/858 and
        // https://github.com/viperproject/silver/issues/297
        for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(goIntMod, Seq(vl, vr)), e)
      case e@ in.Div(l, r) :: ctx.Int(kind) =>
        // We currently implement our own division algorithm to mimic what Go does. The default division implementation in
        // Viper does not match Go's semantics. Check https://github.com/viperproject/gobra/issues/858 and
        // https://github.com/viperproject/silver/issues/297
        for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(goIntDiv, Seq(vl, vr)), e)
      case e@ in.BitAnd(l, r) :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitwiseAnd, Seq(vl, vr)), e)
      case e@ in.BitOr(l, r)  :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitwiseOr,  Seq(vl, vr)), e)
      case e@ in.BitXor(l, r) :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitwiseXor, Seq(vl, vr)), e)
      case e@ in.BitClear(l, r)   :: ctx.Int(kind) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FuncApp(bitClear, Seq(vl, vr)), e)
      case e@ in.ShiftLeft(l, r)  :: ctx.Int(kind) => withSrc(handleShift(shiftLeft)(l, r), e)
      case e@ in.ShiftRight(l, r) :: ctx.Int(kind) => withSrc(handleShift(shiftRight)(l, r), e)
      case e@ in.BitNeg(exp) :: ctx.Int(kind)  => for {ve <- goE(exp)} yield withSrc(vpr.FuncApp(bitwiseNegation, Seq(ve)), e)
    }
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    if(isUsedBitAnd) { addMemberFn(bitwiseAnd) }
    if(isUsedBitOr) { addMemberFn(bitwiseOr) }
    if(isUsedBitXor) { addMemberFn(bitwiseXor) }
    if(isUsedBitClear) { addMemberFn(bitClear) }
    if(isUsedLeftShift) { addMemberFn(shiftLeft) }
    if(isUsedRightShift) { addMemberFn(shiftRight) }
    if(isUsedBitNeg) { addMemberFn(bitwiseNegation) }
    if(isUsedGoIntMod) { addMemberFn(goIntMod) }
    if(isUsedGoIntDiv) { addMemberFn(goIntDiv) }
  }

  /* Bitwise Operations */
  private lazy val bitwiseAnd: vpr.Function = {
    isUsedBitAnd = true
    vpr.Function(
      name = Names.bitwiseAnd,
      formalArgs = Seq(vpr.LocalVarDecl("left", vpr.Int)(), vpr.LocalVarDecl("right", vpr.Int)()),
      typ = vpr.Int,
      pres = Seq(termination.DecreasesWildcard(None)()),
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
      pres = Seq(termination.DecreasesWildcard(None)()),
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
      pres = Seq(termination.DecreasesWildcard(None)()),
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
      pres = Seq(termination.DecreasesWildcard(None)()),
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
      pres = Seq(vpr.GeCmp(right.localVar, vpr.IntLit(BigInt(0))())(), termination.DecreasesWildcard(None)()),
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
      pres = Seq(vpr.GeCmp(right.localVar, vpr.IntLit(BigInt(0))())(), termination.DecreasesWildcard(None)()),
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
      pres = Seq(termination.DecreasesWildcard(None)()),
      posts = Seq.empty,
      body = None
    )()
  }

  /**
   * Generates the following viper function that captures the semantics of the '/' operator in Go:
   *   function goIntDiv(l: Int, r: Int): Int
   *     requires r != 0
   *     decreases _
   *   {
   *     (0 <= l ? l \ r : -(-l \ r))
   *   }
   */
  private lazy val goIntDiv: vpr.Function = {
    isUsedGoIntDiv = true
    val lDecl = vpr.LocalVarDecl("l", vpr.Int)()
    val rDecl = vpr.LocalVarDecl("r", vpr.Int)()
    val l = lDecl.localVar
    val r = rDecl.localVar
    val zero = vpr.IntLit(0)()
    val rNotZero = vpr.NeCmp(r, zero)()
    vpr.Function(
      name = Names.intDiv,
      formalArgs = Seq(lDecl, rDecl),
      typ = vpr.Int,
      pres = Seq(rNotZero, termination.DecreasesWildcard(None)()),
      posts = Seq.empty,
      body = Some(
        // 0 <= l ? l \ r : -((-l) \ r)
        vpr.CondExp(
          cond = vpr.LeCmp(zero, l)(),
          thn = vpr.Div(l, r)(),
          els = vpr.Minus(vpr.Div(vpr.Minus(l)(), r)())()
        )()
      )
    )()
  }

  /**
   * Generates the following viper function that captures the semantics of the '%' operator in Go:
   *   function goIntMod(l: Int, r: Int): Int
   *     requires r != 0
   *     decreases _
   *   {
   *     (0 <= l || l % r == 0 ? l % r : l % r - (0 <= r ? r : -r))
   *   }
   */
  private lazy val goIntMod: vpr.Function = {
    isUsedGoIntMod = true
    val lDecl = vpr.LocalVarDecl("l", vpr.Int)()
    val rDecl = vpr.LocalVarDecl("r", vpr.Int)()
    val l = lDecl.localVar
    val r = rDecl.localVar
    val zero = vpr.IntLit(0)()
    val absR = vpr.CondExp(cond = vpr.LeCmp(zero, r)(), thn = r, els = vpr.Minus(r)())()
    val rNotZero = vpr.NeCmp(r, zero)()
    vpr.Function(
      name = Names.intMod,
      formalArgs = Seq(lDecl, rDecl),
      typ = vpr.Int,
      pres = Seq(rNotZero, termination.DecreasesWildcard(None)()),
      posts = Seq.empty,
      body = Some(
        // (0 <= l || l % r == 0) ? l % r : (l % r - abs(r))
        vpr.CondExp(
          cond = vpr.Or(left = vpr.LeCmp(zero, l)(), right = vpr.EqCmp(vpr.Mod(l, r)(), zero)())(),
          thn = vpr.Mod(l, r)(),
          els = vpr.Sub(vpr.Mod(l, r)(), absR)()
        )()
      )
    )()
  }
}