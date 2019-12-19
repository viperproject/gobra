package viper.gobra

import org.scalatest.BeforeAndAfterAll
import viper.gobra.frontend.Config
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, AnnotationBasedTestSuite, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils


class ParserTests extends GobraTests {
  override val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))
      override val keyIdPrefix: Option[String] = Some("parser_error")

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {

        val config = new Config(Array(
          "--logLevel", "Error",
          "-i", input.file.toFile.getPath,
          "--parseOnly"
        ))

        val (result, elapsedMilis) = time(() => gobraInstance.verify(config))

        info(s"Time required: $elapsedMilis ms")

        result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        }
      }
    }
}
