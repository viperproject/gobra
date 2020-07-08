package viper.gobra.backend

import viper.carbon
import viper.silver
import viper.silver.reporter._


import viper.silver.ast.Program
import viper.silver.verifier.{ VerificationResult, Success, Failure }
import viper.server.ViperBackendConfig

import scala.concurrent.{ExecutionContextExecutor, Future, ExecutionContext}

class Carbon(commandLineArguments: Seq[String]) extends ViperVerifier {

  implicit val executionContext = ExecutionContext.global

  def verify(programID: String, config: ViperBackendConfig, reporter:Reporter, program: Program): Future[VerificationResult] = {
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
