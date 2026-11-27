// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast.{Info, Infoed, NoInfo, Node, Positioned, Program}

/**
  * Configuration of the plugin. Viper instantiates the plugin itself, so front ends that run Viper in the same process
  * (such as Gobra) configure it here before verification. As for the SIF plugin's own configuration, the settings are
  * global: they apply to all programs verified until they are changed.
  */
object HyperfuncsConfig {
  /**
    * Whether the plugin checks the static rules of hyperfunctions ([[HyperfuncsChecks]]). Front ends that enforce
    * these rules on their own language (such as Gobra's type checker) turn it off, since their generated Viper code
    * need not satisfy them literally.
    */
  var checkStaticRules: Boolean = true

  /** Whether `rel` may be used (Gobra: only with `--enableExperimentalHyperFeatures`). */
  var enableRel: Boolean = true

  /** The SIF plugin's experimental features (see `SIFExtendedTransformer.Config`). */
  var enableExperimentalFeatures: Boolean = false

  /** The SIF plugin's encoding of `goto`s that keeps them (see `SIFExtendedTransformer.Config`). */
  var enableGotoLowEventEncoding: Boolean = false

  /** Called with each product program, e.g., to print it. */
  var transformedProgramCallback: Option[Program => Unit] =
    sys.env.get("HYPERFUNCS_PRINT_PRODUCT").map(_ => (p: Program) => println(p))

  /**
    * The info of the assertion `active(i)` that guards the read `at` of execution `i` (`rel(e, i)`), such that the
    * front end can map a failure of this assertion back to its source. By default, the plugin reports such a failure
    * as "Assertion active(i) might not hold" at the position of `at`.
    */
  var activeConditionInfo: (Node with Infoed with Positioned, Int) => Info = (_, _) => NoInfo
}
