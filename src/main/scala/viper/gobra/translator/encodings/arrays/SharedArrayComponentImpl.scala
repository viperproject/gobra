// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.arrays

import viper.gobra.translator.encodings.EmbeddingComponent
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import ArrayEncoding.ComponentParameter
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.{Names, encodings}
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeLevel.pure

class SharedArrayComponentImpl extends SharedArrayComponent {

  override def finalize(col: Collector): Unit = {
    emb.finalize(col)
    arrayNilFunc.finalize(col)
  }

  /**
    * Generates:
    * function arrayNil(): Array[ [T@] ]
    *   ensures len(result) == 1 && Forall idx :: {array_get(result, idx)} array_get(result, idx) == [dflt(T@)]
    * */
  private val arrayNilFunc: FunctionGenerator[ComponentParameter] = new FunctionGenerator[(BigInt, in.Type)]{

    def genFunction(t: (BigInt, in.Type))(ctx: Context): vpr.Function = {
      val vResType = ctx.array.typ(ctx.typeEncoding.typ(ctx)(t._2))
      val src = in.DfltVal(in.ArrayT(t._1, t._2, Shared))(Source.Parser.Internal)
      val idx = in.BoundVar("idx", in.IntT(Exclusive))(src.info)
      val vIdx = ctx.typeEncoding.variable(ctx)(idx)
      val resAccess = ctx.array.loc(vpr.Result(vResType)(), vIdx.localVar)()
      val idxEq = vpr.EqCmp(resAccess, pure(ctx.expr.translate(in.DfltVal(t._2)(src.info))(ctx))(ctx).res)()
      val forall = vpr.Forall(
        Seq(vIdx),
        Seq(vpr.Trigger(Seq(resAccess))()),
        idxEq
      )()

      vpr.Function(
        name = s"${Names.arrayDefaultFunc}_${Names.freshName}",
        formalArgs = Seq.empty,
        typ = vResType,
        pres = Seq.empty,
        posts = Vector(vpr.EqCmp(ctx.array.len(vpr.Result(vResType)())(), vpr.IntLit(1)())(), forall),
        body = None
      )()
    }
  }

  /** Embeds Arrays of fixed length as specified by ComponentParameter. */
  private val emb: EmbeddingComponent[ComponentParameter] = new encodings.EmbeddingComponent.Impl[ComponentParameter](
    p = (e: vpr.Exp, id: ComponentParameter) => (ctx: Context) =>
      vpr.Or( // len(a) == n || a == arrayNil
        vpr.EqCmp(ctx.array.len(e)(), vpr.IntLit(id._1)())(),
        vpr.EqCmp(e, arrayNilFunc(Vector.empty, id)()(ctx))()
      )(),
    t = (id: ComponentParameter) => (ctx: Context) => ctx.array.typ(ctx.typeEncoding.typ(ctx)(id._2))
  )

  /** Returns type of exclusive-array domain. */
  override def typ(t: ComponentParameter)(ctx: Context): vpr.Type = emb.typ(t)(ctx)

  /** Getter of shared-array domain. */
  override def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    ctx.array.loc(emb.unbox(base, t)(pos, info, errT)(ctx), idx)(pos, info, errT) // unbox(base)[idx]
  }

  /** Nil of shared-struct domain */
  override def nil(t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    emb.box(arrayNilFunc(Vector.empty, t)(pos, info, errT)(ctx), t)(pos, info, errT)(ctx) // box(arrayNil())
  }

  /** Length of shared-array domain. */
  override def length(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    ctx.array.len(emb.unbox(arg, t)(pos, info, errT)(ctx))(pos, info, errT) // len(unbox(arg))
  }

  /** Boxing in the context of the shared-array domain. */
  override def box(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    emb.box(arg, t)(pos, info, errT)(ctx)
  }

  /** Unboxing in the context of the shared-array domain. */
  override def unbox(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    emb.unbox(arg, t)(pos, info, errT)(ctx)
  }
}
