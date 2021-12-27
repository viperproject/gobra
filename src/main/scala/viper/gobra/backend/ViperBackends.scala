// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.frontend.Config
import viper.gobra.util.GobraExecutionContext
import viper.server.ViperConfig
import viper.server.core.{VerificationExecutionContext, ViperCoreServer}

trait ViperBackend {
  def create(exePaths: Vector[String], config: Config)(implicit executor: GobraExecutionContext): ViperVerifier
}

object ViperBackends {

  object SiliconBackend extends ViperBackend {
    def create(exePaths: Vector[String], config: Config)(implicit executor: GobraExecutionContext): Silicon = {

      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--disableCatchingExceptions")
      options ++= Vector("--enableMoreCompleteExhale")
      options ++= exePaths

      new Silicon(options)
    }
  }

  object CarbonBackend extends ViperBackend {
    def create(exePaths: Vector[String], config: Config)(implicit executor: GobraExecutionContext): Carbon = {
      var options: Vector[String] = Vector.empty
      // options ++= Vector("--logLevel", "ERROR")
      options ++= exePaths

      new Carbon(options)
    }
  }

  object ViperServerBackend  {
    var executor: VerificationExecutionContext = _
    var server: ViperCoreServer = _

    def setExecutor(executionContext: VerificationExecutionContext): Unit = {
      require(executor == null || executor.executorService.isShutdown)
      executor = executionContext
      // reset server since it depends on the execution context
      resetServer()
    }

    def initServer(config: Config): Unit = {
      require(server == null, "ViperCoreServer is already set.")
      require(executor != null, "Executor is not set.")

      var serverConfig = List("--logLevel", config.logLevel.levelStr)
      if(config.cacheFile.isDefined) {
        serverConfig = serverConfig.appendedAll(List("--cacheFile", config.cacheFile.get))
      }

      server = new ViperCoreServer(new ViperConfig(serverConfig))(executor)
    }

    def resetExecutor(): Unit =
      executor = null

    def resetServer(): Unit =
      server = null
  }

  object ViperServerWithSilicon extends ViperBackend {
    def create(exePaths: Vector[String], config: Config)(implicit executor: GobraExecutionContext): ViperServer = {

      // Initialize viperserver if it wasn't already or if the execution context was shut down
      if(ViperServerBackend.executor == null || ViperServerBackend.executor.executorService.isShutdown) {
        ViperServerBackend.setExecutor(executor)
      }

      if(ViperServerBackend.server == null) {
        ViperServerBackend.initServer(config)
      }

      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--disableCatchingExceptions")
      options ++= Vector("--enableMoreCompleteExhale")
      options ++= exePaths

      new ViperServer(ViperServerBackend.server, ViperServerConfig.ConfigWithSilicon(options.toList))(ViperServerBackend.executor)
    }
  }

  object ViperServerWithCarbon extends ViperBackend {
    def create(exePaths: Vector[String], config: Config)(implicit executor: GobraExecutionContext): ViperServer = {

      // Initialize viperserver if it wasn't already or if the execution context was shut down
      if(ViperServerBackend.executor == null || ViperServerBackend.executor.executorService.isShutdown) {
        ViperServerBackend.setExecutor(executor)
      }

      if(ViperServerBackend.server == null) {
        ViperServerBackend.initServer(config)
      }


      new ViperServer(ViperServerBackend.server, ViperServerConfig.ConfigWithCarbon(List("--logLevel", "ERROR")))(ViperServerBackend.executor)
    }
  }
}
