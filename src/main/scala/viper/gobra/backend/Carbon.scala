package viper.gobra.backend

import viper.carbon
import viper.silver
import viper.silver.reporter.Reporter

class Carbon(commandLineArguments: Seq[String]) extends ViperVerifier {
  var backend: carbon.CarbonVerifier = _

  def start(reporter: Reporter): Unit = {
    require(backend == null)

    // TODO pass reporter to Carbon
    backend = carbon.CarbonVerifier(List("startedBy" -> s"Unit test ${this.getClass.getSimpleName}"))

    backend.parseCommandLine(commandLineArguments ++ Seq("--ignoreFile", "dummy.sil"))
    backend.start()
  }

  def handle(program: silver.ast.Program): silver.verifier.VerificationResult = {
    require(backend != null)

    backend.verify(program)
  }

  def stop(): Unit = {
    require(backend != null)

    backend.stop()
    backend = null
  }
}
