package viper.gobra.backend

import viper.silicon
import viper.silver
import viper.silver.reporter._


import viper.silver.ast.Program
import viper.silver.verifier.{ VerificationResult, Success, Failure }

import scala.concurrent.{ExecutionContextExecutor, Future, ExecutionContext}
import viper.server.ViperBackendConfig

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {

  implicit val executionContext = ExecutionContext.global

  def verify(programID: String, config: ViperBackendConfig, reporter: Reporter, program: Program): Future[VerificationResult] = {
    Future {
      val backend: silicon.Silicon = silicon.Silicon.fromPartialCommandLineArguments(commandLineArguments, reporter)
      
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
