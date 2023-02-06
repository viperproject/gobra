// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class LetEncoding extends Encoding {

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case let: in.Let =>
      val (pos, info, errT) = let.vprMeta
      for {
        exp <- ctx.expression(let.in)
        l = vpr.LocalVarDecl(let.left.id, ctx.typ(let.right.typ))(pos, info, errT)
        r <- ctx.expression(let.right)
      } yield withSrc(vpr.Let(l, r, exp), let)
  }
}
