// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

// Adapted from Viper's SIF plugin (viper.silver.plugin.sif.SIFExtendedTransformer, silver commit c4f54b8), extended
// with hyperfuncs, partial `rel`, and `active(i)` (see the class documentation).
package viper.gobra.translator.transformers.hyper.extended

import viper.silver.ast.Program
import viper.silver.plugin.SilverPlugin

import scala.annotation.unused

/**
  * Gobra's version of Viper's SIF plugin for the extended information-flow encoding (`--hyperMode extended`). Viper
  * loads it by name (option `--plugin`), after its default plugins, such that termination measures have already been
  * desugared when the product is constructed.
  */
class GobraSIFPlugin(@unused reporter: viper.silver.reporter.Reporter,
                     @unused logger: ch.qos.logback.classic.Logger,
                     @unused config: viper.silver.frontend.SilFrontendConfig,
                     @unused fp: viper.silver.parser.FastParser) extends SilverPlugin {

  override def beforeVerify(input: Program): Program = {
    GobraSIFExtendedTransformer.Config.reportError = this.reportError
    GobraSIFExtendedTransformer.transform(input, false)
  }
}
