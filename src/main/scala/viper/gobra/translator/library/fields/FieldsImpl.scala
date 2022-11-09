// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.fields

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.PrimitiveGenerator
import viper.silver.{ast => vpr}

class FieldsImpl extends Fields {

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = _fieldGenerator.finalize(addMemberFn)

  private val _fieldGenerator: PrimitiveGenerator.PrimitiveGenerator[(String, vpr.Type), vpr.Field] =
    PrimitiveGenerator.simpleGenerator(
      (st: (String, vpr.Type)) => {
        val f = vpr.Field(name = st._1, typ = st._2)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        (f, Vector(f))
      }
    )

  override def field(t: in.Type)(ctx: Context): vpr.Field = {
    def normalizeType(t: in.Type): in.Type = {
      val ut = ctx.underlyingType(t)
      ut match {
        case in.PointerT(t, addr) => in.PointerT(normalizeType(t), addr)
        case in.ArrayT(l, t, addr) => in.ArrayT(l, normalizeType(t), addr)
        case in.SliceT(t, addr) => in.SliceT(normalizeType(t), addr)
        case in.MapT(k, v, addr) => in.MapT(normalizeType(k), normalizeType(v), addr)
        case in.ChannelT(t, addr) => in.ChannelT(normalizeType(t), addr)
        case in.SetT(t, addr) => in.SetT(normalizeType(t), addr)
        case in.MultisetT(t, addr) => in.MultisetT(normalizeType(t), addr)
        case in.SequenceT(t, addr) => in.SequenceT(normalizeType(t), addr)
        case in.MathMapT(k, v, addr) => in.MathMapT(normalizeType(k), normalizeType(v), addr)
        case in.OptionT(t, addr) => in.OptionT(normalizeType(t), addr)
        case t => t
      }
    }
    val n = normalizeType(t)
    _fieldGenerator(Names.serializeType(n), ctx.typ(n))
  }
}
