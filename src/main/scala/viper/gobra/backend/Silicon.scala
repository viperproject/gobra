// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.util.GobraExecutionContext
import viper.silicon
import viper.silver.ast.Program
import viper.silver.reporter._
import viper.silver.verifier.VerificationResult

import scala.concurrent.Future

class SiliconFrontendForFrontends(reporter: Reporter, override val encoding: Program)
  extends silicon.SiliconFrontend(reporter) with SilFrontendForFrontends

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {

  def verify(programID: String, config: ViperVerifierConfig, reporter: Reporter, program: Program)(executor: GobraExecutionContext): Future[VerificationResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    Future {
      val siliconFrontend = new SiliconFrontendForFrontends(reporter, program)
      siliconFrontend.execute(commandLineArguments)
      siliconFrontend.result
    }
  }
}
