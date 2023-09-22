// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.encodings.defaults

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.Encoding
import viper.silver.{ast => vpr}

class DefaultTriggerExprEncoding extends Encoding {
  import viper.gobra.translator.util.ViperWriter._

  override def triggerExpr(ctx: Context): in.TriggerExpr ==> CodeWriter[vpr.Exp] = {
    // use predicate access encoding but then take just the predicate access, i.e. remove `acc` and the permission amount:
    case in.Accessible.Predicate(op) =>
      for {
        v <- ctx.assertion(in.Access(in.Accessible.Predicate(op), in.FullPerm(op.info))(op.info))
        pap = v.asInstanceOf[vpr.PredicateAccessPredicate]
      } yield pap.loc
    case e: in.Expr => ctx.expression(e)
  }
}
