package viper.gobra

import org.rogach.scallop.throwError
import org.scalatest.DoNotDiscover
import viper.gobra.frontend.{Config, ScallopGobraConfig}
import viper.gobra.reporting.NoopReporter
import viper.gobra.util.Violation

import java.nio.file.Path

@DoNotDiscover
class DependencyAnalysisBenchmarkTest extends OverallBenchmarkTests {
  val configOptionsPropertyName = "GOBRATESTS_FLAGS"

  // reuse the same frontend for all test files
  override val gobraFrontend: GobraFrontendForTesting = new DependencyAnalysisGobraFrontend()

  class DependencyAnalysisGobraFrontend extends OverallGobraFrontend {
    override def reset(files: Seq[Path]): Unit =
      {
        val configFlags = System.getProperty(configOptionsPropertyName, "").split(",").filter(_.nonEmpty).toSeq
        config = Some(createConfig(Array("-i", files.toVector.mkString(" ")) ++ configFlags))
      }

    private def createConfig(args: Array[String]): Config = {
      // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
      // exception occurs (e.g. a validation failure)
      throwError.value = true
      // Simulate pick of package, Gobra normally does
      val config = new ScallopGobraConfig(args.toSeq).config
      Violation.violation(config.isRight, "creating the config has failed")
      config.toOption.get.copy(reporter = NoopReporter, z3Exe = z3Exe)
    }
  }
}
