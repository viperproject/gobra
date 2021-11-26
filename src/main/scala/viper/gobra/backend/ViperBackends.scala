// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.util.GobraExecutionContext
import viper.server.core.ViperCoreServer

trait ViperBackend {
  def create(exePaths: Vector[String]): ViperVerifier
}

object ViperBackends {

  object SiliconBackend extends ViperBackend {
    def create(exePaths: Vector[String]): Silicon = {

      var options: Vector[String] = Vector.empty
      options ++= Vector("--logLevel", "ERROR")
      options ++= Vector("--disableCatchingExceptions")
      options ++= Vector("--enableMoreCompleteExhale")
      options ++= exePaths

      new Silicon(options)
    }
  }

  object CarbonBackend extends ViperBackend {
    def create(exePaths: Vector[String]): Carbon = {
      var options: Vector[String] = Vector.empty
      // options ++= Vector("--logLevel", "ERROR")
      options ++= exePaths

      new Carbon(options)
    }
  }

  object ViperServerBackend extends ViperBackend {
    var executor: GobraExecutionContext = _
    var server: ViperCoreServer = _

    def setExecutor(executionContext: GobraExecutionContext): Unit = {
      require(executor == null)
      executor = executionContext
    }

    def setServer(coreServer: ViperCoreServer): Unit = {
      require(server == null, "ViperCoreServer is already set.")
      server = coreServer
    }

    def create(exePaths: Vector[String]): ViperServer = {
      require(executor != null, "ExecutionContext has to be set before creation.")
      require(server != null, "ViperCoreServer needs to be set before creation.")
      new ViperServer(server)(executor)
    }

    def resetExecutor(): Unit =
      executor = null

    def resetServer(): Unit =
      server = null
  }
}
