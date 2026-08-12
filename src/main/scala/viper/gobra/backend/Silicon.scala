// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.reporting.IntermediateVerifierResult.{IntermediateVerifierResult, LeftIntermediateVerifierResult}
import viper.gobra.util.GobraExecutionContext
import viper.silicon
import viper.silver.ast.Program
import viper.silver.reporter._
import viper.silver.verifier.{Failure, Success, VerificationResult}

import scala.concurrent.Future

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {

  override def verify(programID: String, reporter: Reporter, program: Program)(executor: GobraExecutionContext): IntermediateVerifierResult[VerificationResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    EitherT.fromEither(Future[Either[LeftIntermediateVerifierResult, VerificationResult]] {
      val siliconApi: silicon.SiliconFrontendAPI = new silicon.SiliconFrontendAPI(reporter)
      
      val startTime = System.currentTimeMillis()
      try {
        siliconApi.initialize(commandLineArguments)
        val result = siliconApi.verify(program)
        siliconApi.stop()

        result match {
          case Success =>
            reporter report OverallSuccessMessage(siliconApi.verifier.name, System.currentTimeMillis() - startTime)
          case f@Failure(_) =>
            reporter report OverallFailureMessage(siliconApi.verifier.name, System.currentTimeMillis() - startTime, f)
        }

        Right(result)
      } catch {
        case _: java.lang.UnsatisfiedLinkError => System.err.println("Couldn't find Z3 java API. No libz3java in java.library.path")
          Right(new Failure(Seq.empty))
        case e: Throwable =>
          throw e
      }
    })
  }
}
