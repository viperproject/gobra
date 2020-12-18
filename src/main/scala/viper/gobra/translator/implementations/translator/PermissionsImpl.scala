// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.internal.{FractionalPerm, FullPerm, NoPerm, WildcardPerm}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Permissions
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class PermissionsImpl extends Permissions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {}

  override def translate(perm: in.Permission)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = perm.vprMeta
    perm match {
      case FullPerm(_) => unit(vpr.FullPerm()(pos, info, errT))
      case NoPerm(_) => unit(vpr.NoPerm()(pos, info, errT))
      case FractionalPerm(left, right) => for {
        l <- ctx.expr.translate(left)(ctx)
        r <- ctx.expr.translate(right)(ctx)
      } yield vpr.FractionalPerm(l, r)(pos, info, errT)
      case WildcardPerm(_) => unit(vpr.WildcardPerm()(pos, info, errT))
    }
  }
}
