package viper.gobra.backend

import viper.silicon
import viper.silver
import viper.silver.reporter.Reporter

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {
  var backend: silicon.Silicon = _

  def start(reporter: Reporter): Unit = {
    require(backend == null)

    backend = new silicon.Silicon(reporter, List("startedBy" -> s"Unit test ${this.getClass.getSimpleName}"))

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
