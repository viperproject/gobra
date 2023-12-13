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
import viper.gobra.translator.library.outlines.{Outlines, OutlinesImpl}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class OutlineEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    outlines.finalize(addMemberFn)
  }

  private val outlines: Outlines = new OutlinesImpl

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n: in.Outline =>
      val (pos, info, errT) = n.vprMeta
      fromMemberLevel(
        for {
          pres <- ml.sequence(n.pres map (p => ctx.precondition(p)))
          posts <- ml.sequence(n.posts map (p => ctx.postcondition(p)))
          measures <- ml.sequence(n.terminationMeasures map (m => ml.pure(ctx.assertion(m))(ctx)))
          exhaleMode = n.exhaleMode
          body <- ml.block(ctx.statement(n.body))
        } yield outlines.outline(n.name, pres ++ measures, posts, exhaleMode, body, n.trusted)(pos, info, errT)
      )
  }
}
