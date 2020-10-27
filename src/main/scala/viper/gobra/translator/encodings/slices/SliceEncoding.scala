// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.slices

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.arrays.SharedArrayEmbedding
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class SliceEncoding(arrayEmb : SharedArrayEmbedding) extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  override def finalize(col : Collector) : Unit = {
    constructGenerator.finalize(col)
    fullSliceFromArrayGenerator.finalize(col)
    fullSliceFromSliceGenerator.finalize(col)
    sliceFromArrayGenerator.finalize(col)
    sliceFromSliceGenerator.finalize(col)
  }

  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Slice(t) / m => m match {
      case Exclusive => ctx.option.typ(ctx.slice.typ(ctx.typeEncoding.typ(ctx)(t)))
      case Shared => vpr.Ref
    }
  }

  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Slice(t) / m => {
        val (pos, info, errT) = exp.vprMeta
        m match {
          case Exclusive => unit(nilSlice(t)(ctx)(pos, info, errT))
          case Shared => unit(vpr.NullLit()(pos, info, errT))
        }
      }

      case (exp : in.NilLit) :: ctx.Slice(t) / Exclusive => {
        val (pos, info, errT) = exp.vprMeta
        unit(nilSlice(t)(ctx)(pos, info, errT))
      }

      case in.Length(exp :: ctx.Slice(typ)) => for {
        expT <- goE(exp)
        typT = ctx.typeEncoding.typ(ctx)(typ)
        (pos, info, errT) = exp.vprMeta
      } yield vpr.CondExp(
        vpr.EqCmp(expT, nilSlice(typT)(ctx)(pos, info, errT))(pos, info, errT),
        vpr.IntLit(0)(pos, info, errT),
        ctx.slice.len(ctx.option.get(expT, ctx.slice.typ(typT))(pos, info, errT))(pos, info, errT)
      )(pos, info, errT)

      case in.Capacity(exp :: ctx.Slice(typ)) => for {
        expT <- goE(exp)
        typT = ctx.typeEncoding.typ(ctx)(typ)
        (pos, info, errT) = exp.vprMeta
      } yield vpr.CondExp(
        vpr.EqCmp(expT, nilSlice(typT)(ctx)(pos, info, errT))(pos, info, errT),
        vpr.IntLit(0)(pos, info, errT),
        ctx.slice.cap(ctx.option.get(expT, ctx.slice.typ(typT))(pos, info, errT))(pos, info, errT)
      )(pos, info, errT)

      case exp @ in.Slice((base : in.Location) :: ctx.Array(_, _) / Shared, low, high, max) => for {
        baseT <- ctx.typeEncoding.reference(ctx)(base)
        lowT <- goE(low)
        highT <- goE(high)
        maxOptT <- option(max match {
          case Some(e) => Some(goE(e))
          case None => None
        })
      } yield {
        val (pos, info, errT) = exp.vprMeta
        val unboxedBaseT = arrayEmb.unbox(baseT, base.typ.asInstanceOf[in.ArrayT])(base)(ctx)

        ctx.option.some(maxOptT match {
          case None => sliceFromArray(vpr.Ref, unboxedBaseT, lowT, highT)(ctx)(pos, info, errT)
          case Some(maxT) => fullSliceFromArray(vpr.Ref, unboxedBaseT, lowT, highT, maxT)(ctx)(pos, info, errT)
        })(pos, info, errT)
      }

      case exp @ in.Slice((base : in.Location) :: ctx.Slice(typ), low, high, max) => for {
        baseT <- goE(base)
        lowT <- goE(low)
        highT <- goE(high)
        maxOptT <- option(max match {
          case Some(e) => Some(goE(e))
          case None => None
        })
      } yield {
        val (pos, info, errT) = exp.vprMeta
        val typT = ctx.typeEncoding.typ(ctx)(typ)
        val baseOptT = ctx.option.get(baseT, ctx.slice.typ(typT))(pos, info, errT)

        ctx.option.some(maxOptT match {
          case None => sliceFromSlice(vpr.Ref, baseOptT, lowT, highT)(ctx)(pos, info, errT)
          case Some(maxT) => fullSliceFromSlice(vpr.Ref, baseOptT, lowT, highT, maxT)(ctx)(pos, info, errT)
        })(pos, info, errT)
      }
    }
  }

  override def reference(ctx : Context) : in.Location ==> CodeWriter[vpr.Exp] = default(super.reference(ctx)) {
    case (exp @ in.IndexedExp(base :: ctx.Slice(typ), idx)) :: _ / Shared => for {
      baseT <- ctx.expr.translate(base)(ctx)
      idxT <- ctx.expr.translate(idx)(ctx)
      typT = ctx.typeEncoding.typ(ctx)(typ)
    } yield {
      val (pos, info, errT) = exp.vprMeta
      val getT = ctx.option.get(baseT, ctx.slice.typ(typT))(pos, info, errT)
      ctx.slice.loc(getT, idxT, typT)(pos, info, errT)
    }
  }

  /** An application of the "sconstruct[`typ`](...)" Viper function. */
  private def construct(typ : vpr.Type, base : vpr.Exp, offset : vpr.Exp, len : vpr.Exp, cap : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    constructGenerator(Vector(base, offset, len, cap), typ)(pos, info, errT)(ctx)

  /** An application of the "sfullSliceFromArray[`typ`](...)" Viper function. */
  private def fullSliceFromArray(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp, k : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    fullSliceFromArrayGenerator(Vector(base, i, j, k), typ)(pos, info, errT)(ctx)

  /** An application of the "sfullSliceFromSlice[`typ`](...)" Viper function. */
  private def fullSliceFromSlice(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp, k : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    fullSliceFromSliceGenerator(Vector(base, i, j, k), typ)(pos, info, errT)(ctx)

  /** Gives the 'nil' slice of type `typ`. */
  private def nilSlice(typ : vpr.Type)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp =
    ctx.option.none(ctx.slice.typ(typ))(pos, info, errT)

  /** Gives the 'nil' slice of type `typ`. */
  private def nilSlice(typ : in.Type)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp =
    nilSlice(ctx.typeEncoding.typ(ctx)(typ))(ctx)(pos, info, errT)

  /** An application of the "ssliceFromArray[`typ`](...)" Viper function. */
  private def sliceFromArray(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    sliceFromArrayGenerator(Vector(base, i, j), typ)(pos, info, errT)(ctx)

  /** An application of the "ssliceFromSlice[`typ`](...)" Viper function. */
  private def sliceFromSlice(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    sliceFromSliceGenerator(Vector(base, i, j), typ)(pos, info, errT)(ctx)


  /* ** Generators */

  private lazy val generateFunctionBodies : Boolean = false

  /**
    * Generator for the "sconstruct" Viper function supporting
    * the domain of Slices. This function is a constructor for Slices:
    *
    * {{{
    * function sconstruct(a : Array[T], offset : Int, len : Int, cap : Int) : Slice[T]
    *   requires 0 <= offset
    *   requires 0 <= len
    *   requires len <= cap
    *   requires offset + cap <= alen(a)
    *   ensures sarray(result) == a
    *   ensures soffset(result) == offset
    *   ensures slen(result) == len
    *   ensures scap(result) == cap
    * }}}
    */
  private val constructGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val aDecl = vpr.LocalVarDecl("a", ctx.array.typ(typ))()
      val offsetDecl = vpr.LocalVarDecl("offset", vpr.Int)()
      val lenDecl = vpr.LocalVarDecl("len", vpr.Int)()
      val capDecl = vpr.LocalVarDecl("cap", vpr.Int)()

      // preconditions
      val pre1 = vpr.LeCmp(vpr.IntLit(0)(), offsetDecl.localVar)()
      val pre2 = vpr.LeCmp(vpr.IntLit(0)(), lenDecl.localVar)()
      val pre3 = vpr.LeCmp(lenDecl.localVar, capDecl.localVar)()
      val pre4 = vpr.LeCmp(vpr.Add(offsetDecl.localVar, capDecl.localVar)(), ctx.array.len(aDecl.localVar)())()
      val pre = Seq(pre1, pre2, pre3, pre4).reduce[vpr.Exp](vpr.And(_, _)())

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.array(result, typ)(), aDecl.localVar)()
      val post2 = vpr.EqCmp(ctx.slice.offset(result)(), offsetDecl.localVar)()
      val post3 = vpr.EqCmp(ctx.slice.len(result)(), lenDecl.localVar)()
      val post4 = vpr.EqCmp(ctx.slice.cap(result)(), capDecl.localVar)()

      vpr.Function(
        s"${Names.sliceConstruct}_${Names.freshName}",
        Seq(aDecl, offsetDecl, lenDecl, capDecl),
        ctx.slice.typ(typ),
        Seq(pre),
        Seq(post1, post2, post3, post4),
        None
      )()
    }
  }

  /**
    * Generator for the "sfullSliceFromArray" Viper function that supports the
    * Viper domain of Slices. This function is applied when translating
    * an array slice expression "a[i:j]" or "a[i:j:k]" in Gobra.
    * Its Viper definition is:
    *
    * {{{
    * function sfullSliceFromArray(a : Array[T], i : Int, j : Int, k : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= k && k <= alen(a)
    *   ensures soffset(result) == i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == k - i
    *   ensures sarray(result) == a
    * {
    *   sconstruct(a, i, j - i, k - i)
    * }
    * }}}
    */
  private val fullSliceFromArrayGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val aDecl = vpr.LocalVarDecl("a", ctx.array.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()
      val kDecl = vpr.LocalVarDecl("k", vpr.Int)()

      // preconditions
      val pre1 = vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar)()
      val pre2 = vpr.LeCmp(iDecl.localVar, jDecl.localVar)()
      val pre3 = vpr.LeCmp(jDecl.localVar, kDecl.localVar)()
      val pre4 = vpr.LeCmp(kDecl.localVar, ctx.array.len(aDecl.localVar)())()
      val pre = Seq(pre1, pre2, pre3, pre4).reduce[vpr.Exp](vpr.And(_, _)())

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), iDecl.localVar)()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(kDecl.localVar, iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), aDecl.localVar)()

      // function body
      val body = construct(
        typ,
        aDecl.localVar,
        iDecl.localVar,
        vpr.Sub(jDecl.localVar, iDecl.localVar)(),
        vpr.Sub(kDecl.localVar, iDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.fullSliceFromArray}_${Names.freshName}",
        Seq(aDecl, iDecl, jDecl, kDecl),
        ctx.slice.typ(typ),
        Seq(pre),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "sfullSliceFromSlice" Viper function
    * that supports the Viper domain on Slices. This function is used
    * in the translation of full slice expressions "s[i:j:k]"
    * where the base expression "s" is itself a slice.
    *
    * {{{
    * function sfullSliceFromSlice(s : Slice[T], i : Int, j : Int, k : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= k && k <= scap(s)
    *   ensures soffset(result) == soffset(s) + i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == k - i
    *   ensures sarray(result) == sarray(s)
    * {
    *   sfullSliceFromArray(sarray(s), soffset(s) + i, soffset(s) + j, soffset(s) + k)
    * }
    * }}}
    */
  private val fullSliceFromSliceGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val sDecl = vpr.LocalVarDecl("s", ctx.slice.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()
      val kDecl = vpr.LocalVarDecl("k", vpr.Int)()

      // preconditions
      val pre1 = vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar)()
      val pre2 = vpr.LeCmp(iDecl.localVar, jDecl.localVar)()
      val pre3 = vpr.LeCmp(jDecl.localVar, kDecl.localVar)()
      val pre4 = vpr.LeCmp(kDecl.localVar, ctx.slice.cap(sDecl.localVar)())()
      val pre = Seq(pre1, pre2, pre3, pre4).reduce[vpr.Exp](vpr.And(_, _)())

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), vpr.Add(ctx.slice.offset(sDecl.localVar)(), iDecl.localVar)())()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(kDecl.localVar, iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), ctx.slice.array(sDecl.localVar, typ)())()

      // function body
      val offset = ctx.slice.offset(sDecl.localVar)()
      val body = fullSliceFromArray(
        typ,
        ctx.slice.array(sDecl.localVar, typ)(),
        vpr.Add(offset, iDecl.localVar)(),
        vpr.Add(offset, jDecl.localVar)(),
        vpr.Add(offset, kDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.fullSliceFromSlice}_${Names.freshName}",
        Seq(sDecl, iDecl, jDecl, kDecl),
        ctx.slice.typ(typ),
        Seq(pre),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "ssliceFromArray" Viper function supporting the
    * Viper domain of Slices. This function is used in the translation
    * of array slicing expressions "a[i:j]" in Gobra (without a third
    * slicing index). Its Viper definition is:
    *
    * {{{
    * function ssliceFromArray(a : Array[T], i : Int, j : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= alen(a)
    *   ensures soffset(result) == i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == alen(a) - i
    *   ensures sarray(result) == a
    * {
    *   sfullSliceFromArray(a, i, j, alen(a))
    * }
    * }}}
    */
  private val sliceFromArrayGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val aDecl = vpr.LocalVarDecl("a", ctx.array.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()

      // preconditions
      val pre1 = vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar)()
      val pre2 = vpr.LeCmp(iDecl.localVar, jDecl.localVar)()
      val pre3 = vpr.LeCmp(jDecl.localVar, ctx.array.len(aDecl.localVar)())()
      val pre = Seq(pre1, pre2, pre3).reduce[vpr.Exp](vpr.And(_, _)())

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), iDecl.localVar)()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(ctx.array.len(aDecl.localVar)(), iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), aDecl.localVar)()

      // function body
      val body = fullSliceFromArray(
        typ,
        aDecl.localVar,
        iDecl.localVar,
        jDecl.localVar,
        ctx.array.len(aDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.sliceFromArray}_${Names.freshName}",
        Seq(aDecl, iDecl, jDecl),
        ctx.slice.typ(typ),
        Seq(pre),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "ssliceFromSlice" Viper function that
    * supports the Viper domain of Slices. This function is used
    * in the Gobra translation of slicing expressions "s[i:k]"
    * where the base "s" is a slice itself.
    *
    * {{{
    * function ssliceFromSlice(s : Slice[T], i : Int, j : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= scap(s)
    *   ensures soffset(result) == soffset(s) + i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == scap(s) - i
    *   ensures sarray(result) == sarray(s)
    * {
    *   sfullSliceFromSlice(s, i, j, scap(s))
    * }
    * }}}
    */
  private val sliceFromSliceGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ: vpr.Type)(ctx: Context): vpr.Function = {
      // declarations
      val sDecl = vpr.LocalVarDecl("s", ctx.slice.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()

      // preconditions
      val pre1 = vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar)()
      val pre2 = vpr.LeCmp(iDecl.localVar, jDecl.localVar)()
      val pre3 = vpr.LeCmp(jDecl.localVar, ctx.slice.cap(sDecl.localVar)())()
      val pre = Seq(pre1, pre2, pre3).reduce[vpr.Exp](vpr.And(_, _)())

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), vpr.Add(ctx.slice.offset(sDecl.localVar)(), iDecl.localVar)())()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(ctx.slice.cap(sDecl.localVar)(), iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), ctx.slice.array(sDecl.localVar, typ)())()

      // function body
      val body = fullSliceFromSlice(
        typ,
        sDecl.localVar,
        iDecl.localVar,
        jDecl.localVar,
        ctx.slice.cap(sDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.sliceFromSlice}_${Names.freshName}",
        Seq(sDecl, iDecl, jDecl),
        ctx.slice.typ(typ),
        Seq(pre),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }
}
