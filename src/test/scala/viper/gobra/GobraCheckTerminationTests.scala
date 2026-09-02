// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra

import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.frontend.Config

/**
  * Runs the test files in `src/test/resources/check_termination` with `disableCheckTerminationPureFns`
  * disabled, i.e., with the requirement that all pure and ghost members carry a termination measure.
  *
  * The main regression test suite ([[GobraTests]]) sets `disableCheckTerminationPureFns` to `true`,
  * because adding termination measures to all of its test files is still pending work. Note that an
  * in-file configuration (`// ##(...)`) cannot be used to opt out of that setting, as configurations
  * are merged by taking the disjunction of the boolean flags. This suite exists so that the
  * requirement itself can be tested; test files that are unrelated to termination checking belong to
  * the main regression test suite instead.
  */
class GobraCheckTerminationTests extends GobraTests {
  val checkTerminationPropertyName = "GOBRATESTS_CHECK_TERMINATION_DIR"

  val checkTerminationDir: String = System.getProperty(checkTerminationPropertyName, "check_termination")

  override val testDirectories: Seq[String] = Vector(checkTerminationDir)

  override protected def getConfig(source: Source): Config =
    super.getConfig(source).copy(disableCheckTerminationPureFns = false)
}
