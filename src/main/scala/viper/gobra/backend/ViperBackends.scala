// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.frontend.Config
import viper.gobra.util.GobraExecutionContext
import viper.server.ViperConfig
import viper.server.core.ViperCoreServer

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
      options ++= Vector("--assumeInjectivityOnInhale")
      options ++= exePaths

      new Silicon(options)
    }
  }

  object CarbonBackend extends ViperBackend {
    def create(exePaths: Vector[String], config: Config)(implicit executor: GobraExecutionContext): Carbon = {
      var options: Vector[String] = Vector.empty
      // options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--assumeInjectivityOnInhale")
      options ++= exePaths

      new Carbon(options)
    }
  }

  abstract class ViperServerBackend(initialServer: Option[ViperCoreServer] = None) extends ViperBackend {
    private var server: Option[ViperCoreServer] = initialServer

    /** abstract method that should return the backend-specific configuration. The configuration will then be passed
      * on to the ViperServer instance.
      */
    def getViperVerifierConfig(exePaths: Vector[String], config: Config): ViperVerifierConfig

    /** returns a ViperServer instance with an underlying ViperCoreServer. A fresh ViperServer instance should be used
      * for each verification. The underlying ViperCoreServer instance is reused if one already exists.
      */
    def create(exePaths: Vector[String], config: Config)(implicit executionContext: GobraExecutionContext): ViperServer = {
      val initializedServer = getOrCreateServer(config)(executionContext)
      val executor = initializedServer.executor
      // the executor used to verify is expected to correspond to the one used by the server:
      assert(executor == executionContext, "a different execution context is used than expected")
      val verifierConfig = getViperVerifierConfig(exePaths, config)
      new ViperServer(initializedServer, verifierConfig)(executor)
    }

    /** returns an existing ViperCoreServer instance or otherwise creates a new one */
    protected def getOrCreateServer(config: Config)(executionContext: GobraExecutionContext): ViperCoreServer = {
      server.getOrElse({
        var serverConfig = List("--logLevel", config.logLevel.levelStr)
        if(config.cacheFile.isDefined) {
          serverConfig = serverConfig.appendedAll(List("--cacheFile", config.cacheFile.get))
        }

        val createdServer = new ViperCoreServer(new ViperConfig(serverConfig))(executionContext)
        // store server for next time:
        server = Some(createdServer)
        createdServer
      })
    }

    /** resets the ViperCoreServer instance such that a new one will be created for the next verification */
    def resetServer(): Unit =
      server = None
  }

  case class ViperServerWithSilicon(initialServer: Option[ViperCoreServer] = None) extends ViperServerBackend(initialServer) {
    override def getViperVerifierConfig(exePaths: Vector[String], config: Config): ViperVerifierConfig = {
      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--disableCatchingExceptions")
      options ++= Vector("--enableMoreCompleteExhale")
      options ++= Vector("--assumeInjectivityOnInhale")
      options ++= exePaths
      ViperServerConfig.ConfigWithSilicon(options.toList)
    }
  }

  case class ViperServerWithCarbon(initialServer: Option[ViperCoreServer] = None) extends ViperServerBackend(initialServer) {
    override def getViperVerifierConfig(exePaths: Vector[String], config: Config): ViperVerifierConfig = {
      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--assumeInjectivityOnInhale")
      options ++= exePaths
      ViperServerConfig.ConfigWithCarbon(options.toList)
    }
  }
}
