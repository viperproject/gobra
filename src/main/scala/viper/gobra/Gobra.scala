/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra

import java.io.File

import com.typesafe.scalalogging.StrictLogging
import viper.gobra.ast.frontend.PProgram
import viper.gobra.ast.internal.Program
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, Parser, ScallopGobraConfig}
import viper.gobra.reporting.{BackTranslator, CopyrightReport, VerifierError, VerifierResult}
import viper.gobra.translator.Translator

import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration


object GoVerifier {

  val copyright = "(c) Copyright ETH Zurich 2012 - 2020"

  val name = "Gobra"

  val rootLogger = "viper.gobra"

  val version: String = {
    val buildRevision = BuildInfo.git("revision")
    val buildBranch = BuildInfo.git("branch")
    val buildVersion = s"$buildRevision${if (buildBranch == "master") "" else s"@$buildBranch"}"

    s"${BuildInfo.projectVersion} ($buildVersion)"
  }
}

trait GoVerifier {

  def name: String = {
    this.getClass.getSimpleName
  }

  def verify(config: Config): VerifierResult = {
    verify(config.inputFile, config)
  }

  protected[this] def verify(file: File, config: Config): VerifierResult
}

class Gobra extends GoVerifier {

  implicit val system: ActorSystem = ActorSystem("Main")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // TODO: implement here what was spoken about in the meeting
  /*
   * NEXT STEPS: change the returntype of performVerification to this FutureEither type and rewrite the method as spoken about in the second meeting
   *             such that the overall return type of the verify method below is Future[VerifierResult]
   */

  override def verify(file: File, config: Config): VerifierResult = {

    config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

    val result = for {
      parsedProgram <- performParsing(file, config)
      typeInfo <- performTypeChecking(parsedProgram, config)
      program <- performDesugaring(parsedProgram, typeInfo, config)
      viperTask <- performViperEncoding(program, config)
      verifierResult <- performVerification(viperTask, config)
    } yield BackTranslator.backTranslate(verifierResult)(config)

    result.fold({
      case Vector() => VerifierResult.Success
      case errs => VerifierResult.Failure(errs)
    }, identity)
  }

  private def performParsing(file: File, config: Config): Either[Vector[VerifierError], PProgram] = {
    if (config.shouldParse) {
      Parser.parse(file)(config)
    } else {
      Left(Vector())
    }
  }

  private def performTypeChecking(parsedProgram: PProgram, config: Config): Either[Vector[VerifierError], TypeInfo] = {
    if (config.shouldTypeCheck) {
      Info.check(parsedProgram)(config)
    } else {
      Left(Vector())
    }
  }

  private def performDesugaring(parsedProgram: PProgram, typeInfo: TypeInfo, config: Config): Either[Vector[VerifierError], Program] = {
    if (config.shouldDesugar) {
      Right(Desugar.desugar(parsedProgram, typeInfo)(config))
    } else {
      Left(Vector())
    }
  }

  private def performViperEncoding(program: Program, config: Config): Either[Vector[VerifierError], BackendVerifier.Task] = {
    if (config.shouldViperEncode) {
      Right(Translator.translate(program)(config))
    } else {
      Left(Vector())
    }
  }

  private def performVerification(viperTask: BackendVerifier.Task, config: Config): Either[Vector[VerifierError], BackendVerifier.Result] = {
    if (config.shouldVerify) {
      // Only for testing
      val result = BackendVerifier.verify(viperTask)(config)
      val res = Await.result(result, Duration.Inf)
      Right(res)

      //Right(BackendVerifier.verify(viperTask)(config))
    } else {
      Left(Vector())
    }
  }
}

class GobraFrontend {

  def createVerifier(config: Config): GoVerifier = {
    new Gobra
  }
}

object GobraRunner extends GobraFrontend with StrictLogging {
  def main(args: Array[String]): Unit = {
    val scallopGobraconfig = new ScallopGobraConfig(args)
    val config = scallopGobraconfig.config
    val verifier = createVerifier(config)
    val result = verifier.verify(config)

    result match {
      case VerifierResult.Success =>
        logger.info(s"${verifier.name} found no errors")
        sys.exit(0)
      case VerifierResult.Failure(errors) =>
        logger.error(s"${verifier.name} has found ${errors.length} error(s):")
        errors foreach (e => logger.error(s"\t${e.formattedMessage}"))
        sys.exit(1)
    }
  }
}

// CODE FROM MEETING ##################################################################################################################

// def nonBlockingVerify(file: File, config: Config /** with backend and reporter */): Future[VerifierResult]

 /* 
  trait ViperBackend2 {

    def verify(reporter: Reporter, config: ViperConfig, program: ast.Program): Future[ViperResult]
    // this is intended to have the same singnature as the veriy method of ViperCoreServer
  }

  /*
   # I am not quite sure but I think the main() which is referred to here is the real main method.
   # This would mean that we would need to have two objects GobraRunner. Lets call them GobraRunnerSingleRun and GobraRunnerServer.
   #
   # GobraRunnerSingleRun would work in the same way as now which is just creating a config, verifier and everything that is needed.
   # Afterwards the verification is performed and the output is given.
   #
   # The GobraRunnerServer would just start the server and do verification until the interruption or stop of the runner is requested.
   # When a verification request is made the server simply verifies the program and keeps on running.
   */


  // single run main() --------------------------------------------------------------------------------------------------------------------

    // this would happen in config parsing, default value of ViperBackends.SiliconBackend, where the following code is the implementation of the create method
    /*
     # I think what is meant above is:
     # the block below is the implementation of the create method in ViperBackends.SiliconBackend.
     # This would just create the verifier.
     # The verify method would use the silicon backend in Silicon.scala to obtain the verification result.
     # => So we would need to implement a create method which creates a ViperVerifier2 which is the nonblocking version of the ViperVerifier trait.
     #    So actually we would need to replace the ViperVerifier trait with a nonblocking version which then gets used for this single run verification.
     #
     # This nonblocking single run verification method will then later be used for the server run verification by calling Gobra.verify(config, reporter)
     # with the correct arguments.
     #
     # Note: As I understand this ViperVerifier2 should not be a second ViperVerifier trait but instead the evolution of the VieprVerifier trait which
     #       is just a nonblocking version which gets a Future[VerificationResult] instead of just a VerificationResult.
     */

    backend = new ViperBackend {
      def create(): ViperVerifier2 { // this is ViperBackends.SiliconBackend

        new ViperVerifier2 { // Instance of ViperVerifier2 trait -> in ViperBackends.scala this would be the ViperVerifiers2 object or something similar

          def verify(reporter, config, program) { // this is Silicon.scala (our wrapper of the silicon backend) //# Actually I think this should be the BackendVerifier.scala
         
          
             Future future: Future[ViperResult] = new Future( () ->
                 Silicon silicon = new Silicon(reporter, config)
                silicon.start()
                val res = silicon.handle();
                silicon.stop()

                res
             )

             future
          }
        }
      }
    }

    // to verify a file with gobra in single run mode this is invoked
    // (the get is used because we want the actual result instead of the future which would be returned by just calling verify)
    Gobra.verify(file, config /* contains siliconFrontend and gobraReporter */).get()


    // server run main() ---------------------------------------------------------------------------------------------------------------

    /*
     * I think what this should do is take as an input the config which is received by the Gobra IDE server (this is of type VerifierConfig)
     * This VerifierConfig then gets translated to a Gobra Config which is used for verification.
     *
     * I think what can be done is send a flag whether server or non-server mode should be used in the VerifierConfig. Then construct depending on the
     * flag either a Gobra Config with a concrete backend (silicon, carbon) [this has an EmptyConfig as backendConfig]
     * or with ViperServer as backend [this takes a corresponding backendConfig (SiliconConfig, CarbonConfig in ViperBackendConfigs.scala) which also has to
     * be constructed from the VerifierConfig]
     *
     * Note: I think in the single-run case the backendConfig can just be omitted (will default to EmptyConfig) but this is never used anyways.
     */
    def newConfigFromTask(task, server, gobraRporter) = { // Config.scala (this also includes a ViperBackend)
        ...
        backend = new ViperBackend { // in ViperBackends.scala
          def create(): ViperVerifer2 = {
             new ViperVerifer { // ServerBackend.scala
               def verify(viperReporter, viperConfig, viperProgram): Future[ViperResult] = {
                 VerificationJobHandler handler = server.verify(viperRepoter, viperConfig, viperProgram)
                 server.getFuture(handler.id)
               }
             }
          }
        }
    }

     ViperServer server = createServer();
     server.start(viperServerConfig)

     while(until done) {
        /*
         # I think everything here in this while loop can be looked at as one method call to a GobraRunnerServer.verify method for example or something similar.
         */
        newTask <- receiveTask

        Reporter reporter = new Reporter()
        GobraConfig config = newConfigFromTask(newTask, server, reporter)

        future = Gobra.verify(config, reporter)

        // does stuff with future or reporter, e.g. attaches continuation on reporter such that on new message, a message to VSCode is sent
        /*
         # This from above means that we somehow redirect the messages from the reporter. So when the reporter reports a message, a message
         # to VSCode is sent. (I think this is meant here.)
         */
     }

     server.stop()

  */
// ##################################################################################################################################

// CODE FROM SECOND MEETING #########################################################################################################
/*
 
  * We handle nested futures with the FutureEither pattern as follows:

     case class FutureEither[E, T](x: Future[Either[E, T]]) {
        def map[Q](f: T => Q): FutureEither[E, Q] = FutureEither(x.map(_.map(f)))
        def flatMap[Q](f: T => FutureEither[E, Q]): FutureEither[E, Q] =
          FutureEither(x.flatMap{
            _.map(f) match {
              case Left(data) => Future(Left(data))
              case Right(z) => z.x
            }
          })
      }

       // you add Future[.] to result of signature of performParsing, performTypeChecking, performDesugaring, performViperEncoding, performVerification, backTranslate
      def stepQ1: FutureEither[Vector[VerifierError], Int] = ???
      def stepQ2(in: Int): FutureEither[Vector[VerifierError], Int] = ???
      def stepQ3(in: Int): FutureEither[Vector[VerifierError], VerifierResult] = ???

      val futureResult = for {
        a <- stepQ1
        b <- stepQ2(a)
      } yield b

      futureResult.x.map{ result =>
        result.fold({
          case Vector() => VerifierResult.Success
          case errs => VerifierResult.Failure(errs)
        }, identity)
      }

   * How does the Gobra VS plugin communicate the choice of backend to Gobra Server?
      * The VS setting, define the choice of backend
      * For practice, only server-carbon, server-silicon are of interest
      * For evaluation, silicon and carbon without a server are interesting, as well
      * Before verify call, the config has to be created with the right backend set

*/
// ##################################################################################################################################
