package viper.gobra.backend

import viper.server.ViperBackendConfig
import viper.silver
import viper.silver.reporter.Reporter

import scala.concurrent.Future

trait ViperVerifier extends Backend[String, ViperBackendConfig, Reporter, silver.ast.Program, silver.verifier.VerificationResult] {

  def verify(programID: String, config: ViperBackendConfig, reporter: Reporter, program: silver.ast.Program): Future[silver.verifier.VerificationResult]

}
