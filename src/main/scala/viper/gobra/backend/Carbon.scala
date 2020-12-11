// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.carbon
import viper.gobra.util.GobraExecutionContext
import viper.silver.ast.Program
import viper.silver.reporter._
import viper.silver.verifier.{Failure, Success, VerificationResult}

import scala.concurrent.Future

class Carbon(commandLineArguments: Seq[String]) extends ViperVerifier {

  def verify(programID: String, config: ViperVerifierConfig, reporter:Reporter, program: Program)(executor: GobraExecutionContext): Future[VerificationResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    Future {
      val backend: carbon.CarbonVerifier = carbon.CarbonVerifier(List("startedBy" -> s"Unit test ${this.getClass.getSimpleName}"))
      backend.parseCommandLine(commandLineArguments ++ Seq("--ignoreFile", "dummy.sil"))

      val startTime = System.currentTimeMillis()
      backend.start()
      val result = backend.verify(program)
      backend.stop()

      result match {
        case Success =>
          reporter report OverallSuccessMessage(backend.name, System.currentTimeMillis() - startTime)
        case f@Failure(_) =>
          reporter report OverallFailureMessage(backend.name, System.currentTimeMillis() - startTime, f)
      }

      result
    }
  }
}
