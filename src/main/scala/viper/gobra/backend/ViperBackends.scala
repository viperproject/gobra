package viper.gobra.backend

import scala.concurrent.ExecutionContextExecutor

trait ViperBackend {
  def create(implicit executionContext: ExecutionContextExecutor): ViperVerifier
}

object ViperBackends {
  object SiliconBackend extends ViperBackend {
    def create (implicit executionContext: ExecutionContextExecutor): Silicon = {

      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--disableCatchingExceptions")
      options ++= Vector("--enableMoreCompleteExhale")

      new Silicon(options)
    }
  }

  object CarbonBackend extends ViperBackend {
    def create(implicit executionContext: ExecutionContextExecutor): Carbon = {
      var options: Vector[String] = Vector.empty
      // options ++= Vector("--logLevel", "ERROR")

      new Carbon(options)
    }
  }
}
