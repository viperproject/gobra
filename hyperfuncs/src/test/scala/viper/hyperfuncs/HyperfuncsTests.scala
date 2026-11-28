// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silicon.tests.SiliconTests

/**
  * Runs the plugin's regression tests (src/test/resources/hyperfuncs) with Silicon. Expected errors are annotated in
  * the files with `//:: ExpectedOutput(...)`. The Viper ports of the hyperfuncs evaluation (jcp19/hyperfuncs) are run
  * from that repository.
  */
class HyperfuncsRegressionTests extends SiliconTests {
  override val testDirectories: Seq[String] = Seq("hyperfuncs")

  override val commandLineArguments: Seq[String] = Seq(
    "--timeout", "300" /* seconds */,
    "--plugin=viper.hyperfuncs.HyperfuncsPlugin")
}
