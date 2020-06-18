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
    verify(Left(config.inputFile), config)
  }

  def verify(content: String, config: Config): Future[VerifierResult] = {
    verify(Right(content), config)
  }

  protected[this] def verify(input: Either[File, String], config: Config): Future[VerifierResult]
}

class Gobra extends GoVerifier {

  implicit val executionContext = ExecutionContext.global
  

  override def verify(input: Either[File, String], config: Config): Future[VerifierResult] = {

    config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

    val futureResult = for {
      parsedProgram <- performParsing(input, config)
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

  private def performParsing(input: Either[File, String], config: Config): FutureEither[Vector[VerifierError], PProgram] = {
    if (config.shouldParse) {
      FutureEither(Parser.parse(input)(config))
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