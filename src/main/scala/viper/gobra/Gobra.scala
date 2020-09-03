/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra

import java.io.File

import com.typesafe.scalalogging.StrictLogging
import viper.gobra.ast.frontend.PPackage
import viper.gobra.ast.internal.Program
import viper.silver.{ast => vpr}
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, Parser, ScallopGobraConfig}
import viper.gobra.reporting.{BackTranslator, CopyrightReport, VerifierError, VerifierResult}
import viper.gobra.translator.Translator

import scala.concurrent.{ExecutionContextExecutor, Future, ExecutionContext}
import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source


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
    verify(Left(config.inputFiles), config)
  }

  def verify(content: String, config: Config): Future[VerifierResult] = {
    verify(Right(content), config)
  }

  protected[this] def verify(input: Either[Vector[File], String], config: Config): Future[VerifierResult]
  protected[this] def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo): Future[VerifierResult]
}

class Gobra extends GoVerifier {

  implicit val executionContext = ExecutionContext.global


  override def verify(input: Either[Vector[File], String], config: Config): Future[VerifierResult] = {

    val finalConfig = input match {
      case Left(_) => getAndMergeInFileConfig(config)
      case Right(_) => config
    }

    config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

    val result = for {
      parsedPackage <- performParsing(input, finalConfig)
      typeInfo <- performTypeChecking(parsedPackage, finalConfig)
      program <- performDesugaring(parsedPackage, typeInfo, finalConfig)
      viperTask <- performViperEncoding(program, finalConfig)
      verifierResult <- performVerification(viperTask, finalConfig)
    } yield BackTranslator.backTranslate(verifierResult)(finalConfig)

    result.x.map{ result =>
      result.fold({
        case Vector() => VerifierResult.Success
        case errs => VerifierResult.Failure(errs)
      }, identity)
    }
  }

  override def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo): Future[VerifierResult] = {
    val viperTask = BackendVerifier.Task(ast, backtrack)

    val result = for {
      verifierResult <- performVerification(viperTask, config)
    } yield BackTranslator.backTranslate(verifierResult)(config)

    result.x.map{ result =>
      result.fold({
        case Vector() => VerifierResult.Success
        case errs => VerifierResult.Failure(errs)
      }, identity)
    }
  }

  private val inFileConfigRegex = """(?:.|\r\n|\r|\n)*\/\/ ##\((.*)\)(?:.|\r\n|\r|\n)*""".r

  /**
    * Parses all inputFiles given in the current config for in-file command line options (wrapped with "## (...)")
    * These in-file command options get combined for all files and passed to ScallopGobraConfig.
    * The current config merged with the newly created config is then returned
    */
  private def getAndMergeInFileConfig(config: Config): Config = {
    val inFileConfigStrings = config.inputFiles.map(file => {
        val bufferedSource = Source.fromFile(file)
        val content = bufferedSource.mkString
        val config = content match {
          case inFileConfigRegex(configString) => Some(configString)
          case _ => None
        }
        bufferedSource.close()
        config
      }).collect { case Some(configString) => configString }

    // our current "merge" strategy for potentially different, duplicate, or even contradicting configurations is to concatenate them:
    val args = inFileConfigStrings.flatMap(configString => configString.split(" "))
    // input files are mandatory, therefore we take the inputFiles from the old config:
    val fullArgs = (args :+ "-i") ++ config.inputFiles.map(_.getPath)
    val inFileConfig = new ScallopGobraConfig(fullArgs).config
    config.merge(inFileConfig)
  }

  private def performParsing(input: Either[Vector[File], String], config: Config): FutureEither[Vector[VerifierError], PPackage] = {
    if (config.shouldParse) {
      FutureEither(Parser.parse(input)(config))
    } else {
      FutureEither(Future(Left(Vector())))
    }
  }

  private def performTypeChecking(parsedPackage: PPackage, config: Config): FutureEither[Vector[VerifierError], TypeInfo] = {
    if (config.shouldTypeCheck) {
      FutureEither(Info.check(parsedPackage)(config))
    } else {
      FutureEither(Future(Left(Vector())))
    }
  }

  private def performDesugaring(parsedPackage: PPackage, typeInfo: TypeInfo, config: Config): FutureEither[Vector[VerifierError], Program] = {
    if (config.shouldDesugar) {
      val programFuture = Desugar.desugar(parsedPackage, typeInfo)(config)
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
