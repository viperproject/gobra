package viper.gobra.backend

import viper.server.ViperBackendConfig
import viper.silicon
import viper.silver.ast.Program
import viper.silver.reporter._
import viper.silver.verifier.{Failure, Success, VerificationResult}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

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
