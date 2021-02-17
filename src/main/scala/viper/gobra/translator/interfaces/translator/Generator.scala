// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.translator

import viper.gobra.translator.interfaces.{Collector, Context}

import scala.annotation.unused

trait Generator {

  /**
    * Finalizes translation. May add to collector.
    */
  def finalize(@unused col: Collector): Unit = {}

  def chain[R](fs: Vector[Context => (R, Context)])(ctx: Context): (Vector[R], Context) = {
    fs.foldLeft((Vector.empty[R], ctx)){ case ((rs, c), rf) =>
      val (r, nc) = rf(c)
      (r +: rs, nc)
    }
  }
}
