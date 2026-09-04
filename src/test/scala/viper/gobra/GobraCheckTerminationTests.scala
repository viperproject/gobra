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
  * disabled, i.e., with the requirement that all ghost and pure members carry a termination measure.
  *
  * This suite exists because that requirement cannot be tested from the main regression test suite:
  * [[GobraTests]] sets `disableCheckTerminationPureFns` to `true`, as adding termination measures to
  * all of its test files is still pending work, and an in-file configuration (`// ##(...)`) cannot opt
  * out of that setting. In-file configurations are merged into the base configuration by taking the
  * disjunction of the boolean flags (see `Config.merge`), so they can only ever turn this flag on.
  *
  * Only test files about that requirement belong here. Everything else, including the requirements on
  * ghost and pure members that hold irrespective of the flag, belongs to the main regression test
  * suite, which is where a reader looks for it.
  */
class GobraCheckTerminationTests extends GobraTests {
  val checkTerminationPropertyName = "GOBRATESTS_CHECK_TERMINATION_DIR"

  val checkTerminationDir: String = System.getProperty(checkTerminationPropertyName, "check_termination")

  override val testDirectories: Seq[String] = Vector(checkTerminationDir)

  override protected def getConfig(source: Source): Config =
    super.getConfig(source).copy(disableCheckTerminationPureFns = false)
}
