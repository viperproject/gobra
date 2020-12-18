// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.util.GobraExecutionContext
import viper.silver
import viper.silver.reporter.Reporter

import scala.concurrent.Future

object ViperVerifierConfig {
  object EmptyConfig extends ViperVerifierConfig {val partialCommandLine: List[String] = Nil}
  case class Config(partialCommandLine: List[String]) extends ViperVerifierConfig
}

trait ViperVerifierConfig {
  val partialCommandLine: List[String]
}

trait ViperVerifier extends Backend[String, ViperVerifierConfig, Reporter, silver.ast.Program, silver.verifier.VerificationResult] {

  def verify(programID: String, config: ViperVerifierConfig, reporter: Reporter, program: silver.ast.Program)(executor: GobraExecutionContext): Future[silver.verifier.VerificationResult]

}
