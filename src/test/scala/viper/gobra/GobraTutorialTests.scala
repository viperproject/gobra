// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra

import ch.qos.logback.classic.Level
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.frontend.{Config, Source}

import java.nio.file.Path

/** tests the tutorial examples with (mostly) default settings to ensure that they run, e.g., in VSCode */
class GobraTutorialTests extends GobraTests {
  override val testDirectories: Seq[String] = Vector("regressions/examples/tutorial-examples")

  protected override def getConfig(source: Source): Config =
    Config(
      logLevel = Level.INFO,
      reporter = StringifyReporter,
      packageInfoInputMap = Map(Source.getPackageInfoOrCrash(source, Path.of("")) -> Vector(source)),
      checkConsistency = true,
      z3Exe = z3Exe,
    )
}
