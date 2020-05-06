package viper.gobra.backend

import scala.concurrent.ExecutionContext
import viper.server.ViperCoreServer

trait ViperBackend {
  def create: ViperVerifier
}

object ViperBackends {

  implicit val executionContext = ExecutionContext.global

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

  object ViperServerBackend extends ViperBackend {
    var server: ViperCoreServer = _

    def setServer(coreServer: ViperCoreServer) {
      require(server == null, "ViperCoreServer is already set.")
      server = coreServer
    }

    def create: ViperServer = {
      require(server != null, "ViperCoreServer needs to be set before creation.")
      new ViperServer(server)
    }

    def resetServer() {
      server = null
    }
  }
}
