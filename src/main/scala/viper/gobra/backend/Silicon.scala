package viper.gobra.backend

import viper.silicon
import viper.silver

class Silicon(commandLineArguments: Seq[String]) extends ViperVerifier {
  var backend: silicon.Silicon = _

  def start(): Unit = {
    require(backend == null)

    backend = new silicon.Silicon(List("startedBy" -> s"Unit test ${this.getClass.getSimpleName}"))

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
