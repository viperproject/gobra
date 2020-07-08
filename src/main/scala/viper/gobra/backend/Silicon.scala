package viper.gobra.backend

import viper.silicon
import viper.silver
import viper.silver.reporter.Reporter


import viper.silver.ast.Program
import viper.silver.verifier.VerificationResult

import scala.concurrent.{ExecutionContextExecutor, Future, ExecutionContext}
import viper.server.ViperBackendConfig

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {

  implicit val executionContext = ExecutionContext.global

  def verify(programID: String, config: ViperBackendConfig, reporter: Reporter, program: Program): Future[VerificationResult] = {
    Future {
      val backend: silicon.Silicon = silicon.Silicon.fromPartialCommandLineArguments(commandLineArguments, reporter)
      
      backend.start()
      val result = backend.verify(program)
      backend.stop()

      result
    }
  }
}
