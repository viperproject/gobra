package viper.gobra

import java.nio.file.Path

import ch.qos.logback.classic.Level
import org.scalatest.BeforeAndAfterAll
import viper.gobra.frontend.Config
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.gobra.reporting.{NoopReporter, VerifierError}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, AnnotationBasedTestSuite, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import scala.concurrent.ExecutionContextExecutor
import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GobraTests extends AnnotationBasedTestSuite with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem("Main")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val testDirectories: Seq[String] = Vector("regressions")
  override val defaultTestPattern: String = ".*\\.go"

  var gobraInstance: Gobra = _

  override def beforeAll(): Unit = {
    gobraInstance = new Gobra()
  }

  override def afterAll(): Unit = {
    gobraInstance = null
  }

  val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {

        val config = Config(
          logLevel = Level.INFO,
          reporter = NoopReporter,
          inputFile = input.file.toFile
        )

        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(config), Duration.Inf))

        info(s"Time required: $elapsedMilis ms")

        result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        }
      }
    }


  /**
    * The systems to test each input on.
    *
    * This method is not modeled as a constant field deliberately, such that
    * subclasses can instantiate a new [[viper.silver.testing.SystemUnderTest]]
    * for each test input.
    */
  override def systemsUnderTest: Seq[SystemUnderTest] = Vector(gobraInstanceUnderTest)

  case class GobraTestOuput(error: VerifierError) extends AbstractOutput {
    /** Whether the output belongs to the given line in the given file. */
    override def isSameLine(file: Path, lineNr: Int): Boolean = error.position.line == lineNr

    /** A short and unique identifier for this output. */
    override def fullId: String = error.id
  }
}
