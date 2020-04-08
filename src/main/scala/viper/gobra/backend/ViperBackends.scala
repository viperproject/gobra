package viper.gobra.backend

trait ViperBackend {
  def create: ViperVerifier
}

object ViperBackends {
  object SiliconBackend extends ViperBackend {
    def create: Silicon = {
      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--disableCatchingExceptions")
      options ++= Vector("--enableMoreCompleteExhale")

      new Silicon(options)
    }
  }

  object CarbonBackend extends ViperBackend {
    def create: Carbon = {
      var options: Vector[String] = Vector.empty
      // options ++= Vector("--logLevel", "ERROR")

      new Carbon(options)
    }
  }
}
