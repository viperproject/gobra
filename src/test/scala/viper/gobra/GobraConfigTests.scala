// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra

import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, DefaultAnnotatedTestInput, DefaultTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import java.nio.file.Path
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GobraConfigTests extends GobraPackageTests {
  val gobraConfigDirPropertyName    = "GOBRATESTS_GOBRA_CONFIG_DIR"

  val gobraConfigTestsDir: String  = System.getProperty(gobraConfigDirPropertyName, "gobra_config")

  override val testDirectories: Seq[String] = Vector(gobraConfigTestsDir)

  override def buildTestInput(file: Path, prefix: String): DefaultAnnotatedTestInput = {
    // we treat all files in the same directory as belonging to the same package / test case
    val dir = file.getParent
    val files = dir.toFile.listFiles().toSeq.map(_.toPath)
    GobraAnnotatedTestInput(DefaultTestInput(dir.toString, prefix, files, Seq.empty))
  }

  override val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {
        // check that all files in `input` belong to the same directory and use that
        // directory for invoking Gobra
        assert(input.files.nonEmpty)
        val dir = input.files.head.getParent
        assert(input.files.forall(_.getParent == dir))
        val config = createConfig(Array("--config", dir.toFile.getPath)).get
          .copy(reporter = StringifyReporter) // we override the reporter to avoid polluting StdIO
        val pkgInfo = config.packageInfoInputMap.keys.head
        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(pkgInfo, config)(executor), Duration.Inf))

        info(s"Time required: $elapsedMilis ms")

        result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        }
      }
    }
}
