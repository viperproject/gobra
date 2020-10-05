// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

class Registrator[T <: vpr.Member] extends Generator {

  private var gens: Set[T] = Set.empty

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = {
    gens.foreach(col.addMember)
    clean()
  }

  def clean(): Unit = gens = Set.empty

  def register(x: T): T = {
    gens += x
    x
  }
}
