package viper.gobra.backend

import viper.silver.reporter.Reporter
import viper.silver.ast.Program
import viper.silver.verifier.VerificationResult

import scala.concurrent.Future

import viper.server.ViperCoreServer
import viper.server.ViperBackendConfig

import viper.server.VerificationJobHandler

class ViperServer(server: ViperCoreServer) extends ViperVerifier {
  def verify(programID: String, config: ViperBackendConfig, reporter: Reporter, program: Program): Future[VerificationResult] = {
      val handler: VerificationJobHandler = server.verify(programID, config, reporter, program)
      server.getFuture(handler.id)
  }

}