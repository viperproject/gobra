// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.io.File

import com.typesafe.scalalogging.StrictLogging
import viper.gobra.ast.frontend.PPackage
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.OverflowChecksTransform
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, Parser, ScallopGobraConfig}
import viper.gobra.reporting.{AppliedInternalTransformsMessage, BackTranslator, CopyrightReport, VerifierError, VerifierResult}
import viper.gobra.translator.Translator
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silver.{ast => vpr}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
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

  def verify(config: Config)(executor: GobraExecutionContext): Future[VerifierResult] = {
    verify(config.inputFiles, config)(executor)
  }

  protected[this] def verify(input: Vector[File], config: Config)(executor: GobraExecutionContext): Future[VerifierResult]
}

trait GoIdeVerifier {
  protected[this] def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(executor: GobraExecutionContext): Future[VerifierResult]
}

class Gobra extends GoVerifier with GoIdeVerifier {

  override def verify(input: Vector[File], config: Config)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor

    val task = Future {

      val finalConfig = getAndMergeInFileConfig(config)

      config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

      for {
        parsedPackage <- performParsing(input, finalConfig)
        typeInfo <- performTypeChecking(parsedPackage, finalConfig)
        program <- performDesugaring(parsedPackage, typeInfo, finalConfig)
        program <- performInternalTransformations(program, finalConfig)
        viperTask <- performViperEncoding(program, finalConfig)
      } yield (viperTask, finalConfig)
    }

    task.flatMap{
      case Left(Vector()) => Future(VerifierResult.Success)
      case Left(errors)   => Future(VerifierResult.Failure(errors))
      case Right((job, finalConfig)) => verifyAst(finalConfig, job.program, job.backtrack)(executor)
    }
  }

  override def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    val viperTask = BackendVerifier.Task(ast, backtrack)
    performVerification(viperTask, config)
      .map(BackTranslator.backTranslate(_)(config))
  }

  private val inFileConfigRegex = """##\((.*)\)""".r

  /**
    * Parses all inputFiles given in the current config for in-file command line options (wrapped with "## (...)")
    * These in-file command options get combined for all files and passed to ScallopGobraConfig.
    * The current config merged with the newly created config is then returned
    */
  private def getAndMergeInFileConfig(config: Config): Config = {
    val inFileConfigStrings = config.inputFiles.flatMap(file => {
      val bufferedSource = Source.fromFile(file)
      val content = bufferedSource.mkString
      val configs = for (m <- inFileConfigRegex.findAllMatchIn(content)) yield m.group(1)
      bufferedSource.close()
      configs
    })

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

  /**
    * Applies transformations to programs in the internal language. Currently, only adds overflow checks but it can
    * be easily extended to perform more transformations
    */
  private def performInternalTransformations(program: Program, config: Config): Either[Vector[VerifierError], Program] = {
    if (config.checkOverflows) {
      val result = OverflowChecksTransform.transform(program)
      config.reporter report AppliedInternalTransformsMessage(config.inputFiles.head, () => result)
      Right(result)
    } else {
      Right(program)
    }
  }

  private def performViperEncoding(program: Program, config: Config): Either[Vector[VerifierError], BackendVerifier.Task] = {
    if (config.shouldViperEncode) {
      Right(Translator.translate(program)(config))
    } else {
      Left(Vector())
    }
  }

  private def performVerification(viperTask: BackendVerifier.Task, config: Config)(implicit executor: GobraExecutionContext): Future[BackendVerifier.Result] = {
    if (config.shouldVerify) {
      BackendVerifier.verify(viperTask)(config)
    } else {
      Future(BackendVerifier.Success)
    }
  }
}



class GobraFrontend {

  def createVerifier(): GoVerifier = {
    new Gobra
  }
}

object GobraRunner extends GobraFrontend with StrictLogging {
  def main(args: Array[String]): Unit = {
    val scallopGobraconfig = new ScallopGobraConfig(args.toSeq)
    val config = scallopGobraconfig.config
    val nThreads = Math.max(DefaultGobraExecutionContext.minimalThreadPoolSize, Runtime.getRuntime.availableProcessors())
    val executor: GobraExecutionContext = new DefaultGobraExecutionContext(nThreads)
    val verifier = createVerifier()
    val resultFuture = verifier.verify(config)(executor)
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
