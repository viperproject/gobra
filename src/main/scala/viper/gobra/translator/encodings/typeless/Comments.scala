// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.Comments
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class Comments extends Encoding {

  override def extendStatement(ctx: Context): in.Stmt ==> Extension[CodeWriter[vpr.Stmt]] = {
    case s => x => x.map(v => stmtComment(s,v))
  }

  def stmtComment(x: in.Stmt, res: vpr.Stmt): vpr.Stmt = {
    x match {
      case _: in.Seqn => res
      case _ => Comments.prependComment(x.formattedShort, res)
    }
  }
}
