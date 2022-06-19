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
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.silver.{ast => vpr}

class GlobalEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.MemberLevel._

  override def otherMember(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case gc: in.GlobalConstDecl => ctx.fixpoint.create(gc)(ctx); unit(Vector.empty)
  }
}
