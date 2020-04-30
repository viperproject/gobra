package viper.gobra.backend

import viper.silicon
import viper.silver
import viper.silver.reporter.Reporter


import viper.silver.ast.Program
import viper.silver.verifier.VerificationResult

import scala.concurrent.{ExecutionContextExecutor, Future}

class Silicon(commandLineArguments: Seq[String])
             (implicit val executionContext: ExecutionContextExecutor) extends ViperVerifier {

  def verify(reporter: Reporter, config: ViperBackendConfig, program: Program): Future[VerificationResult] = {
    Future {
      val backend: silicon.Silicon = silicon.Silicon.fromPartialCommandLineArguments(config.partialCommandLine, reporter)
      
      backend.start()
      val result = backend.verify(program)
      backend.stop()

      result
    }
  }
/*
  var backend: silicon.Silicon = _
  

  def start(reporter: Reporter): Unit = {
    require(backend == null)

    backend = silicon.Silicon.fromPartialCommandLineArguments(commandLineArguments, reporter)
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
