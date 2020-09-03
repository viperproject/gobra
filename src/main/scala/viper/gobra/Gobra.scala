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
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, Parser, ScallopGobraConfig}
import viper.gobra.reporting.{BackTranslator, CopyrightReport, VerifierError, VerifierResult}
import viper.gobra.translator.Translator
import viper.silver.{ast => vpr}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}
import scala.io.Source


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
    verify(config.inputFiles, config)
  }

  protected[this] def verify(input: Vector[File], config: Config): Future[VerifierResult]
}

trait GoIdeVerifier {
  protected[this] def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo): Future[VerifierResult]
}

class Gobra extends GoVerifier with GoIdeVerifier {

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

  override def verify(input: Vector[File], config: Config): Future[VerifierResult] = {

    val task = Future {
      val finalConfig = getAndMergeInFileConfig(config)

      config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

      for {
        parsedPackage <- performParsing(input, finalConfig)
        typeInfo <- performTypeChecking(parsedPackage, finalConfig)
        program <- performDesugaring(parsedPackage, typeInfo, finalConfig)
        viperTask <- performViperEncoding(program, finalConfig)
      } yield (viperTask, finalConfig)
    }

    task.flatMap{
      case Left(Vector()) => Future(VerifierResult.Success)
      case Left(errors)   => Future(VerifierResult.Failure(errors))
      case Right((job, finalConfig)) => verifyAst(finalConfig, job.program, job.backtrack)
    }
  }

  override def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo): Future[VerifierResult] = {
    val viperTask = BackendVerifier.Task(ast, backtrack)
    performVerification(viperTask, config)
      .map(BackTranslator.backTranslate(_)(config))
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

    private def performParsing(input: Vector[File], config: Config): Either[Vector[VerifierError], PPackage] = {
      if (config.shouldParse) {
        Parser.parse(input)(config)
      } else {
        Left(Vector())
      }
    }

    private def performTypeChecking(parsedPackage: PPackage, config: Config): Either[Vector[VerifierError], TypeInfo] = {
      if (config.shouldTypeCheck) {
        Info.check(parsedPackage)(config)
      } else {
        Left(Vector())
      }
    }

    private def performDesugaring(parsedPackage: PPackage, typeInfo: TypeInfo, config: Config): Either[Vector[VerifierError], Program] = {
      if (config.shouldDesugar) {
        Right(Desugar.desugar(parsedPackage, typeInfo)(config))
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

  private def performVerification(viperTask: BackendVerifier.Task, config: Config): Future[BackendVerifier.Result] = {
    if (config.shouldVerify) {
      BackendVerifier.verify(viperTask)(config)
    } else {
      Future(BackendVerifier.Success)
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
    implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

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
