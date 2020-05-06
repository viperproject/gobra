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

import scala.concurrent.{ExecutionContextExecutor, Future, ExecutionContext}
import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// TODO: move to separate file
case class FutureEither[E, T](x: Future[Either[E, T]]) {

  implicit val executionContext = ExecutionContext.global

  def map[Q](f: T => Q): FutureEither[E, Q] = FutureEither(x.map(_.map(f)))
  def flatMap[Q](f: T => FutureEither[E, Q]): FutureEither[E, Q] =
    FutureEither(x.flatMap{
      _.map(f) match {
        case Left(data) => Future(Left(data))
        case Right(z) => z.x
      }
    })
}


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

  def verify(config: Config): Future[VerifierResult] = {
    verify(config.inputFile, config)
  }

  protected[this] def verify(file: File, config: Config): Future[VerifierResult]
}

class Gobra extends GoVerifier {

  implicit val executionContext = ExecutionContext.global
  

  override def verify(file: File, config: Config): Future[VerifierResult] = {

    config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")


    val futureResult = for {
      parsedProgram <- performParsing(file, config)
      typeInfo <- performTypeChecking(parsedProgram, config)
      program <- performDesugaring(parsedProgram, typeInfo, config)
      viperTask <- performViperEncoding(program, config)
      verifierResult <- performVerification(viperTask, config)
    } yield BackTranslator.backTranslate(verifierResult)(config)

    futureResult.x.map{ result =>
      result.fold({
        case Vector() => VerifierResult.Success
        case errs => VerifierResult.Failure(errs)
      }, identity)
    }
  }

  private def performParsing(file: File, config: Config): FutureEither[Vector[VerifierError], PProgram] = {
    if (config.shouldParse) {
      FutureEither(Parser.parse(file)(config))
    } else {
      FutureEither(Future(Left(Vector())))
    }
  }

  private def performTypeChecking(parsedProgram: PProgram, config: Config): FutureEither[Vector[VerifierError], TypeInfo] = {
    if (config.shouldTypeCheck) {
      FutureEither(Info.check(parsedProgram)(config))
    } else {
      FutureEither(Future(Left(Vector())))
    }
  }

  private def performDesugaring(parsedProgram: PProgram, typeInfo: TypeInfo, config: Config): FutureEither[Vector[VerifierError], Program] = {
    if (config.shouldDesugar) {
      val programFuture = Desugar.desugar(parsedProgram, typeInfo)(config)
      FutureEither(programFuture.map(program => Right(program)))
    } else {
      FutureEither(Future(Left(Vector())))
    }
  }

  private def performViperEncoding(program: Program, config: Config): FutureEither[Vector[VerifierError], BackendVerifier.Task] = {
    if (config.shouldViperEncode) {
      val translationFuture = Translator.translate(program)(config)
      FutureEither(translationFuture.map(translation => Right(translation)))
    } else {
      FutureEither(Future(Left(Vector())))
    }
  }

  private def performVerification(viperTask: BackendVerifier.Task, config: Config): FutureEither[Vector[VerifierError], BackendVerifier.Result] = {
    if (config.shouldVerify) {
      val resultFuture = BackendVerifier.verify(viperTask)(config)
      FutureEither(resultFuture.map(result => Right(result)))
    } else {
      FutureEither(Future(Left(Vector())))
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
    implicit val executionContext = ExecutionContext.global

/*
    // for testing with ViperServer as backend ######################################
    import viper.server.ViperCoreServer
    import viper.server.ViperConfig
    import viper.gobra.backend.ViperBackends.ViperServerBackend
    import viper.server.ViperBackendConfigs._

    // Viper Server config with empty arguments
    val serverConfig: ViperConfig = new ViperConfig(List())
    val server: ViperCoreServer = new ViperCoreServer(serverConfig)

    ViperServerBackend.setServer(server)
    server.start()

    val scallopGobraconfig = new ScallopGobraConfig(args)
    val config = scallopGobraconfig.config

    val carbonBackendConfig = CarbonConfig(List())
    val siliconBackendConfig = SiliconConfig(List("--logLevel", "ERROR"))
    config.backendConfig = siliconBackendConfig

    val verifier = createVerifier(config)

    println("first verification")
    val start1 = System.currentTimeMillis
    val firstResultFuture = verifier.verify(config)
    val firstResult = Await.result(firstResultFuture, Duration.Inf)
    val elapsedTime1 = System.currentTimeMillis - start1
    println("Elapsed time: " + elapsedTime1 + " ms")

    println("second (real) verification")
    val start2 = System.currentTimeMillis
    val resultFuture = verifier.verify(config)
    val result = Await.result(resultFuture, Duration.Inf)
    val elapsedTime2 = System.currentTimeMillis - start2
    println("Elapsed time: " + elapsedTime2 + " ms")


    result match {
      case VerifierResult.Success =>
        logger.info(s"${verifier.name} found no errors")

        server.stop()

        sys.exit(0)
      case VerifierResult.Failure(errors) =>
        logger.error(s"${verifier.name} has found ${errors.length} error(s):")
        errors foreach (e => logger.error(s"\t${e.formattedMessage}"))

        server.stop()

        sys.exit(1)
    }
    // end testing ##################################################################
*/



    val scallopGobraconfig = new ScallopGobraConfig(args)
    val config = scallopGobraconfig.config

    val verifier = createVerifier(config)
    val resultFuture = verifier.verify(config)
    val result = Await.result(resultFuture, Duration.Inf)

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
  trait Backend2 { // This is extended by ViperVerifier2

    def verify(reporter: Reporter, config: ViperConfig, program: ast.Program): Future[ViperResult]
    // this is intended to have the same singnature as the veriy method of ViperCoreServer
  }


  // single run main() --------------------------------------------------------------------------------------------------------------------

    // this would happen in config parsing, default value of ViperBackends.SiliconBackend, where the following code is the implementation of the create method
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

     /*
      * Additional note: The ViperBackend below should be an instance of ViperServer. This ViperServer should have as an input the server -> which is then used for the creation.
      *                  In order for ViperServer to have an instance of ViperServer add a setServer(server): Unit function to class ViperServer and call this in newConfigFromTask
      */

    // I think this method should be in Gobra IDE server
    def newConfigFromTask(task, server, gobraReporter) = { // returns Config.scala (this also includes a ViperBackend -> the ViperBackend in our case would be the ViperServer)
                          -> task is most likely meant to be the ViperVerifier Config which arrive at the Gobra IDE server.

        ...
        backend = new ViperBackend { // in ViperBackends.scala (ViperBackend is an object and thus needs no initialization)
      
          // Note: the create method gets called only afterwards in the BackendVerifier.verify method for now. I don't think this will work like this then
                   maybe make the setServer method in the object ViperBackend such that it can be accessed all the time -> could work since only one ViperServer is ever started in Gobra IDE server.
          def create(): ViperVerifer2 = {
            new ViperVerifer { // ServerBackend.scala
              def verify(viperReporter, viperConfig, viperProgram): Future[ViperResult] = {
                VerificationJobHandler handler = server.verify(viperRepoter, viperConfig, viperProgram)
                server.getFuture(handler.id)
              }
            }
          }
        }
        // set server in the backend:
        backend.setServer(server)           // or maybe ViperBackend.setServer(server) -> think this would work better (could also be done at start of server and then simply require server != null for the create method)
                                            // could then also have ViperBackend.resetServer() to delete server after stopping

        backendConfig = // create a backendConfig from the given task config... (this would be again the VerifierConfig I think. (could also change that the task is changed to the Gobra Config first which then is used to create the BackendConfig with the partialCommandLine string))
    }

     ViperServer server = createServer();
     server.start(viperServerConfig)

     while(until done) {
        /*
         # I think everything here in this while loop can be looked at as one method call to a GobraRunnerServer.verify method for example or something similar.
         */
        newTask <- receiveTask

        Reporter reporter = new Reporter() // maybe use gobraReporter from Config
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

   * How does the Gobra VS plugin communicate the choice of backend to Gobra Server?
      * The VS setting, define the choice of backend
      * For practice, only server-carbon, server-silicon are of interest
      * For evaluation, silicon and carbon without a server are interesting, as well
      * Before verify call, the config has to be created with the right backend set

*/
// ##################################################################################################################################
