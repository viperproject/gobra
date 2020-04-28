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

  def verify(config: Config): VerifierResult = {
    verify(config.inputFiles, config)
  }

  protected[this] def verify(files: Vector[File], config: Config): VerifierResult
}

class Gobra extends GoVerifier {

  override def verify(files: Vector[File], c: Config): VerifierResult = {
    val config = getAndMergeInFileConfig(c)

    config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

    val result = for {
      parsedProgram <- performParsing(files, config)
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

  private val inFileConfigRegex = """(?:.|\n)*\/\/ ##\((.*)\)(?:.|\n)*""".r

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

  private def performParsing(files: Vector[File], config: Config): Either[Vector[VerifierError], PPackage] = {
    if (config.shouldParse) {
      Parser.parse(files)(config)
    } else {
      Left(Vector())
    }
  }

  private def performTypeChecking(parsedProgram: PPackage, config: Config): Either[Vector[VerifierError], TypeInfo] = {
    if (config.shouldTypeCheck) {
      Info.check(parsedProgram)(config)
    } else {
      Left(Vector())
    }
  }

  private def performDesugaring(parsedProgram: PPackage, typeInfo: TypeInfo, config: Config): Either[Vector[VerifierError], Program] = {
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
      Right(BackendVerifier.verify(viperTask)(config))
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
