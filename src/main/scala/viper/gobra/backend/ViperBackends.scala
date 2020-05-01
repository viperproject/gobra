package viper.gobra.backend

import scala.concurrent.ExecutionContextExecutor
import viper.server.ViperCoreServer

trait ViperBackend {
  def create(implicit executionContext: ExecutionContextExecutor): ViperVerifier
}

object ViperBackends {
  object SiliconBackend extends ViperBackend {
    def create(implicit executionContext: ExecutionContextExecutor): Silicon = {

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

  object ViperServerBackend extends ViperBackend {
    var server: ViperCoreServer = _

    def setServer(coreServer: ViperCoreServer) {
      require(server == null, "ViperCoreServer is already set.")
      server = coreServer
    }

    def create(implicit executionContext: ExecutionContextExecutor): ViperServer = {
      require(server != null, "ViperCoreServer needs to be set before creation.")
      new ViperServer(server)
    }

    def resetServer() {
      server = null
    }
  }
}
