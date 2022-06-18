// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Globals
import viper.gobra.translator.util.ViperWriter.MemberLevel.unit
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.silver.{ast => vpr}

class GlobalsImpl extends Globals {

  def globalVarDecl(decl: in.GlobalVarDecl)(ctx: Context): MemberWriter[Vector[vpr.Function]] = {
    // TODO: generate proof obligations
    // TODO: improve doc, draw the encoding
    // global variable declarations are encoded as pure functions that return a pointer
    // to the variable
    unit(
      decl.left map { l =>
        val (pos, info, errTrafo) = l.vprMeta
        val typ = ctx.typeEncoding.typ(ctx)(l.typ)
        vpr.Function(
          name = l.name.uniqueName,
          formalArgs = Seq.empty,
          typ = typ,
          pres = Seq.empty,
          posts = Seq.empty,
          body = None
        )(pos, info, errTrafo)
      }
    )
  }
}



