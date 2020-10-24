// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.io.File

import ch.qos.logback.classic.Level
import viper.gobra.frontend.Config
import viper.gobra.reporting.NoopReporter
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

abstract class GobraOverflowCheckTests extends GobraTests {
  override val testDirectories: Seq[String] = Vector("overflow_checks")

  def configWithInput(files: Vector[File]): Config

  override val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {
        val config = configWithInput(Vector(input.file.toFile))

        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(config), Duration.Inf))

        info(s"Time required: $elapsedMilis ms")

        result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        }
      }
    }
}

/**
  * Tests overflow checks in 32 bit mode
  */
class GobraOverflowCheckTests32 extends GobraOverflowCheckTests {
  override def configWithInput(files: Vector[File]): Config =
    Config(
      logLevel = Level.INFO,
      reporter = NoopReporter,
      inputFiles = files,
      checkOverflows = true
    )

  override val testDirectories: Seq[String] = Vector("overflow_checks/int32")
}

/**
  * Tests overflow checks in 64 bit mode
  */
class GobraOverflowCheckTests64 extends GobraOverflowCheckTests {
  override def configWithInput(files: Vector[File]): Config =
    Config(
      logLevel = Level.INFO,
      reporter = NoopReporter,
      inputFiles = files,
      checkOverflows = true,
      int64bit = true
    )

  override val testDirectories: Seq[String] = Vector("overflow_checks/int64")
}
