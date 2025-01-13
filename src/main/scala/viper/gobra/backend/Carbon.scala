// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.carbon
import viper.carbon.CarbonFrontendAPI
import viper.gobra.util.GobraExecutionContext
import viper.silver.ast.Program
import viper.silver.reporter._
import viper.silver.verifier.{Failure, Success, VerificationResult}

import scala.concurrent.Future

class Carbon(commandLineArguments: Seq[String]) extends ViperVerifier {

  override def verify(programID: String, reporter: Reporter, program: Program)(executor: GobraExecutionContext): Future[VerificationResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    Future {
      val carbonApi: carbon.CarbonFrontendAPI = new CarbonFrontendAPI(reporter)

      val startTime = System.currentTimeMillis()
      carbonApi.initialize(commandLineArguments ++ Seq("--ignoreFile", "dummy.sil"))
      val result = carbonApi.verify(program)
      carbonApi.stop()

      result match {
        case Success =>
          reporter report OverallSuccessMessage(carbonApi.verifier.name, System.currentTimeMillis() - startTime)
        case f@Failure(_) =>
          reporter report OverallFailureMessage(carbonApi.verifier.name, System.currentTimeMillis() - startTime, f)
      }

      result
    }
  }
}
