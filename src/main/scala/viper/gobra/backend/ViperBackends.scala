// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

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
}
