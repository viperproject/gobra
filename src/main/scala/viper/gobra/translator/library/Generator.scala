// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library

import viper.gobra.translator.transformers.ViperTransformer
import viper.silver.{ast => vpr}

import scala.annotation.unused

trait Generator {

  /**
    * Finalizes translation. `addMemberFn` is called with any member that is part of the encoding.
    */
  def finalize(@unused addMemberFn: vpr.Member => Unit): Unit = {}

  def collectTransformers(@unused addTransformer: ViperTransformer => Unit): Unit = {}
}
