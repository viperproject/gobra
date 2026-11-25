// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ConfigValidationUnitTests extends AnyFunSuite with Matchers {
  private val swapFile = "src/test/resources/regressions/examples/swap.gobra"

  private def errorMessages(args: String*): Vector[String] =
    new ScallopGobraConfig(args).config match {
      case Left(errors) => errors.map(_.message)
      case Right(_) => fail(s"expected a config error for arguments ${args.mkString(" ")}")
    }

  test("Config: missing input mode is reported as a config error") {
    errorMessages() should contain ("There should be exactly one of the following options: input, directory, recursive, config")
  }

  test("Config: multiple input modes are reported as a config error") {
    errorMessages("-r", "-i", swapFile) should contain ("There should be exactly one of the following options: input, directory, recursive, config")
  }

  test("Config: a single input file yields a valid config") {
    new ScallopGobraConfig(Seq("-i", swapFile)).config.isRight should be (true)
  }
}
