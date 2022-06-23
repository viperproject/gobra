// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.defaults

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.util.ViperWriter.MemberLevel.unit
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.silver.{ast => vpr}

class DefaultGlobalVarDeclarationEncoding extends Encoding {

  override def globalVarDeclaration(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Function]] = {
    case decl: in.GlobalVarDecl =>
      // TODO: generate proof obligations
      // TODO: improve doc, draw the encoding
      // global variable declarations are encoded as pure functions that return a pointer
      // to the variable
      unit(
        decl.left map { l =>
          val (pos, info, errTrafo) = l.vprMeta
          val typ = ctx.typ(l.typ)
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



