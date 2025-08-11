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
import viper.gobra.util.TypeBounds.{BoundedIntegerKind, IntegerKind, UnboundedInteger}
import viper.silver.ast.{Domain, ErrorTrafo, Info, NoInfo, NoPosition, NoTrafos, Position}
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
        case Exclusive => IntEncodingGenerator(Vector.empty, kind)(ctx)
        case Shared    => vpr.Ref
      }
  }
  // TODO: make pres conditional on whether the overflow flag is enabled or not
  private case object IntEncodingGenerator extends DomainGenerator[IntegerKind] {
    private var intToDomainFuncs: Map[IntegerKind, vpr.Function] = Map.empty
    private var domainToIntFuncs: Map[IntegerKind, vpr.Function] = Map.empty
    private var addFuncs: Map[IntegerKind, vpr.Function] = Map.empty

    override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
      super.finalize(addMemberFn)
      intToDomainFuncs.values.foreach(addMemberFn)
      domainToIntFuncs.values.foreach(addMemberFn)
      addFuncs.values.foreach(addMemberFn)
    }

    override def genDomain(x: IntegerKind)(ctx: Context): Domain = {
      val kindToDomainName = s"IntDomain${x.name}"
      vpr.Domain(name = kindToDomainName, functions = Seq.empty, axioms = Seq.empty)()
    }

    def domainType(x: IntegerKind)(ctx: Context): vpr.Type = {
      this(Vector.empty, x)(ctx)
    }

    def intToDomainFuncApp(x: IntegerKind)(ctx: Context)(e: vpr.Exp)(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): vpr.Exp = {
      val funcname = intToDomainFunc(x)(ctx).name
      vpr.FuncApp(funcname = funcname, args = Seq(e))(pos, info, typ = domainType(x)(ctx), errT)
    }

    private def intToDomainFunc(x: IntegerKind)(ctx: Context): vpr.Function = {
      intToDomainFuncs.get(x) match {
        case Some(f) => f
        case _ =>
          val inputVarDecl = vpr.LocalVarDecl("x", vpr.Int)()
          val resType = domainType(x)(ctx)
          val pre = x match {
            case b: BoundedIntegerKind =>
              vpr.And(
                vpr.LeCmp(vpr.IntLit(b.lower)(), inputVarDecl.localVar)(),
                vpr.LeCmp(inputVarDecl.localVar, vpr.IntLit(b.upper)())()
              )()
            case _ => vpr.TrueLit()()
          }
          val post = vpr.EqCmp(domainToIntFuncApp(x)(ctx)(vpr.Result(resType)())(), inputVarDecl.localVar)()
          val res = vpr.Function(
            name = s"intToDomain${x.name}",
            formalArgs = Seq(inputVarDecl),
            typ = resType,
            pres = Seq(pre),
            posts = Seq(post),
            body = None
          )()
          intToDomainFuncs += x -> res
          res
      }
    }

    def domainToIntFuncApp(x: IntegerKind)(ctx: Context)(e: vpr.Exp)(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): vpr.Exp = {
      val funcname = domainToIntFunc(x)(ctx).name
      vpr.FuncApp(funcname = funcname, args = Seq(e))(pos, info, typ = vpr.Int, errT)
    }

    private def domainToIntFunc(x: IntegerKind)(ctx: Context): vpr.Function = {
      val inputVarDecl = vpr.LocalVarDecl("x", domainType(x)(ctx))()
      val post = x match {
        case b: BoundedIntegerKind =>
          vpr.And(
            vpr.LeCmp(vpr.IntLit(b.lower)(), vpr.Result(vpr.Int)())(),
            vpr.LeCmp(vpr.Result(vpr.Int)(), vpr.IntLit(b.upper)())()
          )()
        case _ => vpr.TrueLit()()
      }
      val res = vpr.Function(
        name = s"domainToInt${x.name}",
        formalArgs = Seq(inputVarDecl),
        typ = vpr.Int,
        pres = Seq.empty,
        posts = Seq(post),
        body = None
      )()
      domainToIntFuncs += x -> res
      res
    }

    def addFuncApp(x: IntegerKind)(ctx: Context)(e1: vpr.Exp, e2: vpr.Exp)(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): vpr.Exp = {
      val funcname = addFunc(x)(ctx).name
      vpr.FuncApp(funcname = funcname, args = Seq(e1, e2))(pos, info, typ = domainType(x)(ctx), errT)
    }

    private def addFunc(x: IntegerKind)(ctx: Context): vpr.Function = {
      addFuncs.get(x) match {
        case Some(f) => f
        case _ =>
          val domainT = domainType(x)(ctx)
          val inputVar1Decl = vpr.LocalVarDecl("x", domainT)()
          val inputVar2Decl = vpr.LocalVarDecl("y", domainT)()
          val pre = x match {
            case b: BoundedIntegerKind =>
              val sumExpr = vpr.Add(
                domainToIntFuncApp(x)(ctx)(inputVar1Decl.localVar)(),
                domainToIntFuncApp(x)(ctx)(inputVar2Decl.localVar)(),
              )()
              vpr.And(vpr.LeCmp(vpr.IntLit(b.lower)(), sumExpr)(), vpr.LeCmp(sumExpr, vpr.IntLit(b.upper)())())()
            case _ => vpr.TrueLit()()
          }
          val post = vpr.EqCmp(
            domainToIntFuncApp(x)(ctx)(vpr.Result(domainT)())(),
            vpr.Add(
              domainToIntFuncApp(x)(ctx)(inputVar1Decl.localVar)(),
              domainToIntFuncApp(x)(ctx)(inputVar2Decl.localVar)()
            )()
          )()
          val res = vpr.Function(
            name = s"add${x.name}",
            formalArgs = Seq(inputVar1Decl, inputVar2Decl),
            typ = domainT,
            pres = Seq(pre),
            posts = Seq(post),
            body = None
          )()
          addFuncs += x -> res
          res
      }
    }
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
      case (e: in.DfltVal) :: ctx.Int(kind) / Exclusive =>
        unit(withSrc(IntEncodingGenerator.intToDomainFuncApp(kind)(ctx)(vpr.IntLit(BigInt(0))()), e))
      case lit: in.IntLit =>
        unit(withSrc(IntEncodingGenerator.intToDomainFuncApp(lit.kind)(ctx)(vpr.IntLit(lit.v)()), lit))
      case e@ in.Add(l :: ctx.Int(kindL), r :: ctx.Int(kindR)) :: ctx.Int(kind) =>
        for {
          vl <- goE(l)
          vr <- goE(r)
          // TODO: explain the use of left and right
          left = withSrc(IntEncodingGenerator.intToDomainFuncApp(kind)(ctx)(
            withSrc(IntEncodingGenerator.domainToIntFuncApp(kindL)(ctx)(vl), l)
          ), l)
          right = withSrc(IntEncodingGenerator.intToDomainFuncApp(kind)(ctx)(
            withSrc(IntEncodingGenerator.domainToIntFuncApp(kindR)(ctx)(vr), r)
          ), r)
        } yield withSrc(IntEncodingGenerator.addFuncApp(kind)(ctx)(left, right), e)
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
      case in.Conversion(in.IntT(_, outKind), expr :: in.IntT(_, inKind)) =>
        for {
          e <- goE(expr)
          intValue = withSrc(IntEncodingGenerator.domainToIntFuncApp(inKind)(ctx)(e), expr)
        } yield withSrc(IntEncodingGenerator.intToDomainFuncApp(outKind)(ctx)(intValue), expr)
      case n@in.LessCmp(l, r) if l.typ.isInstanceOf[in.IntT] && r.typ.isInstanceOf[in.IntT] =>
        val ltyp = l.typ.asInstanceOf[in.IntT].kind
        val rtyp = r.typ.asInstanceOf[in.IntT].kind
        for {
          vl <- ctx.expression(l)
          vr <- ctx.expression(r)
          numl = withSrc(IntEncodingGenerator.domainToIntFuncApp(ltyp)(ctx)(vl), l)
          numr = withSrc(IntEncodingGenerator.domainToIntFuncApp(rtyp)(ctx)(vr), r)
        } yield withSrc(vpr.LtCmp(numl, numr), n)
      case n@in.AtMostCmp(l, r) if l.typ.isInstanceOf[in.IntT] && r.typ.isInstanceOf[in.IntT] =>
        val ltyp = l.typ.asInstanceOf[in.IntT].kind
        val rtyp = r.typ.asInstanceOf[in.IntT].kind
        for {
          vl <- ctx.expression(l)
          vr <- ctx.expression(r)
          numl = withSrc(IntEncodingGenerator.domainToIntFuncApp(ltyp)(ctx)(vl), l)
          numr = withSrc(IntEncodingGenerator.domainToIntFuncApp(rtyp)(ctx)(vr), r)
        } yield withSrc(vpr.LeCmp(numl, numr), n)
      case n@in.GreaterCmp(l, r) if l.typ.isInstanceOf[in.IntT] && r.typ.isInstanceOf[in.IntT] =>
        val ltyp = l.typ.asInstanceOf[in.IntT].kind
        val rtyp = r.typ.asInstanceOf[in.IntT].kind
        for {
          vl <- ctx.expression(l)
          vr <- ctx.expression(r)
          numl = withSrc(IntEncodingGenerator.domainToIntFuncApp(ltyp)(ctx)(vl), l)
          numr = withSrc(IntEncodingGenerator.domainToIntFuncApp(rtyp)(ctx)(vr), r)
        } yield withSrc(vpr.GtCmp(numl, numr), n)
      case n@in.AtLeastCmp(l, r) if l.typ.isInstanceOf[in.IntT] && r.typ.isInstanceOf[in.IntT] =>
        val ltyp = l.typ.asInstanceOf[in.IntT].kind
        val rtyp = r.typ.asInstanceOf[in.IntT].kind
        for {
          vl <- ctx.expression(l)
          vr <- ctx.expression(r)
          numl = withSrc(IntEncodingGenerator.domainToIntFuncApp(ltyp)(ctx)(vl), l)
          numr = withSrc(IntEncodingGenerator.domainToIntFuncApp(rtyp)(ctx)(vr), r)
        } yield withSrc(vpr.GeCmp(numl, numr), n)
    }
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    IntEncodingGenerator.finalize(addMemberFn)
    // TODO: rework the following
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