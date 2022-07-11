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

  private val _fieldGenerator: PrimitiveGenerator.PrimitiveGenerator[vpr.Type, vpr.Field] =
    PrimitiveGenerator.simpleGenerator(
      (t: vpr.Type) => {
        val f = vpr.Field(name = Names.pointerField(t), typ = t)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        (f, Vector(f))
      }
    )

  override def field(t: in.Type)(ctx: Context): vpr.Field = _fieldGenerator(ctx.typ(t))
}
