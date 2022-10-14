// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library

import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

import scala.annotation.unused

trait Generator {

  /**
    * Finalizes translation. `addMemberFn` is called with any member that is part of the encoding.
    */
  def finalize(@unused addMemberFn: vpr.Member => Unit): Unit = {}

  def chain[R](fs: Vector[Context => (R, Context)])(ctx: Context): (Vector[R], Context) = {
    fs.foldLeft((Vector.empty[R], ctx)) { case ((rs, c), rf) =>
      val (r, nc) = rf(c)
      (r +: rs, nc)
    }
  }
}
