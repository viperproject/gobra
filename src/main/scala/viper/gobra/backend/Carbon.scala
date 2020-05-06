package viper.gobra.backend

import viper.carbon
import viper.silver
import viper.silver.reporter.Reporter


import viper.silver.ast.Program
import viper.silver.verifier.VerificationResult
import viper.server.ViperBackendConfig

import scala.concurrent.{ExecutionContextExecutor, Future, ExecutionContext}

class Carbon(commandLineArguments: Seq[String]) extends ViperVerifier {

  implicit val executionContext = ExecutionContext.global

  def verify(programID: String, config: ViperBackendConfig, reporter:Reporter, program: Program): Future[VerificationResult] = {
    Future {
      val backend: carbon.CarbonVerifier = carbon.CarbonVerifier(List("startedBy" -> s"Unit test ${this.getClass.getSimpleName}"))
      backend.parseCommandLine(commandLineArguments ++ Seq("--ignoreFile", "dummy.sil"))

      backend.start()
      val result = backend.verify(program)
      backend.stop()

      result
    }
  }

/*
  var backend: carbon.CarbonVerifier = _

  def start(reporter: Reporter): Unit = {
    require(backend == null)

    // TODO pass reporter to Carbon
    backend = carbon.CarbonVerifier(List("startedBy" -> s"Unit test ${this.getClass.getSimpleName}"))

    backend.parseCommandLine(commandLineArguments ++ Seq("--ignoreFile", "dummy.sil"))
    backend.start()
  }

  def handle(program: silver.ast.Program): Future[silver.verifier.VerificationResult] = {
    require(backend != null)

    Future {
      backend.verify(program)
    }

    //backend.verify(program)
  }

  def stop(): Unit = {
    require(backend != null)

    backend.stop()
    backend = null
  }
*/
}
