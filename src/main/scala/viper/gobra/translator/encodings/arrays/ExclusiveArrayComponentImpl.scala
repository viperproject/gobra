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
import viper.gobra.translator.encodings

class ExclusiveArrayComponentImpl extends ExclusiveArrayComponent {

  override def finalize(col: Collector): Unit = {
    emb.finalize(col)
  }

  /** Embeds Sequences of fixed length as specified by ComponentParameter. */
  private val emb: EmbeddingComponent[ComponentParameter] = new encodings.EmbeddingComponent.Impl[ComponentParameter](
    p = (e: vpr.Exp, id: ComponentParameter) => (_: Context) => vpr.EqCmp(vpr.SeqLength(e)(), vpr.IntLit(id._1)())(),
    t = (id: ComponentParameter) => (_: Context) => vpr.SeqType(id._2)
  )

  /** Returns type of exclusive-array domain. */
  override def typ(t: ComponentParameter)(ctx: Context): vpr.Type = emb.typ(t)(ctx)

  /** Constructor of shared-array domain. */
  override def create(args: Vector[vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    if (args.isEmpty) {
      emb.box(vpr.EmptySeq(t._2)(pos, info, errT), t)(pos, info, errT)(ctx) // box(Seq(args))
    } else {
      emb.box(vpr.ExplicitSeq(args)(pos, info, errT), t)(pos, info, errT)(ctx) // box(Seq(args))
    }
  }

  /** Getter of exclusive-array domain. */
  override def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    vpr.SeqIndex(emb.unbox(base, t)(pos, info, errT)(ctx), idx)(pos, info, errT) // unbox(base)[idx]
  }

  /** Update function of shared-array domain. */
  override def update(base: vpr.Exp, idx: vpr.Exp, newVal: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    emb.box(vpr.SeqUpdate(emb.unbox(base, t)(pos, info, errT)(ctx), idx, newVal)(pos, info, errT), t)(pos, info, errT)(ctx) // box(unbox(base)[idx := newVal])
  }

  /** Length of exclusive-array domain. */
  override def length(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    vpr.SeqLength(emb.unbox(arg, t)(pos, info, errT)(ctx))(pos, info, errT) // len(unbox(arg))
  }

  /** Returns argument as sequence. */
  def toSeq(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    emb.unbox(arg, t)(pos, info, errT)(ctx) // unbox(arg)
  }
}
