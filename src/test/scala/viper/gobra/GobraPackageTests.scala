package viper.gobra

import java.io.File

import org.rogach.scallop.exceptions.ValidationFailure
import org.rogach.scallop.throwError
import viper.gobra.frontend.{Config, ScallopGobraConfig}
import viper.gobra.reporting.ParserError
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GobraPackageTests extends GobraTests {
  override val testDirectories: Seq[String] = Vector("same_package")

  override val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {
        // extract package clause of input:
        val resultOpt = for {
          pkgName <- getPackageClause(input.file.toFile)
          currentDir = input.file.getParent
          config <- createConfig(Array(
            "--logLevel", "Error",
            "-i", pkgName,
            "-I", currentDir.toFile.getPath
          ))
          (result, elapsedMilis) = time(() => gobraInstance.verify(config))
          _ = info(s"Time required: $elapsedMilis ms")
        } yield result

        resultOpt match {
          case None => Vector(GobraTestOuput(ParserError("package clause extraction or config creation failed", None)))
          case Some(f) =>
            val result = Await.result(f, Duration.Inf)
            result match {
              case Success => Vector.empty
              case Failure(errors) => errors map GobraTestOuput
            }
        }
      }

      private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

      private def getPackageClause(file: File): Option[String] = {
        val bufferedSource = scala.io.Source.fromFile(file)
        val content = bufferedSource.mkString
        bufferedSource.close()
        // TODO is there a way to perform the regex lazily on the file's content?
        pkgClauseRegex
          .findAllMatchIn(content)
          .collectFirst { case m if m.group(1) != null => m.group(1) }
      }

      private def createConfig(args: Array[String]): Option[Config] = {
        try {
          // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
          // exception occurs (e.g. a validation failure)
          throwError.value = true
          Some(new ScallopGobraConfig(args).config)
        } catch {
          case _: ValidationFailure => None
          case other: Throwable => throw other
        }
      }
    }
}
