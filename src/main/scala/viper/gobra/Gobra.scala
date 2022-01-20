// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Paths
import java.util.concurrent.ExecutionException
import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.ast.frontend.PPackage
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.{CGEdgesTerminationTransform, OverflowChecksTransform}
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, Parser, ScallopGobraConfig}
import viper.gobra.reporting._
import viper.gobra.translator.Translator
import viper.gobra.util.Violation.{KnownZ3BugException, LogicException, UglyErrorMessage}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silver.{ast => vpr}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object GoVerifier {

  val copyright = "(c) Copyright ETH Zurich 2012 - 2021"

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

  def verify(config: Config, taskName: Option[String] = None)(executor: GobraExecutionContext): Future[VerifierResult] = {
    verify(config.inputs, config, taskName.getOrElse(config.inputs.map(_.name).mkString(",")))(executor)
  }

  protected[this] def verify(input: Vector[Source], config: Config, taskName: String)(executor: GobraExecutionContext): Future[VerifierResult]
}

trait GoIdeVerifier {
  protected[this] def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo, taskName: String)(executor: GobraExecutionContext): Future[VerifierResult]
}

class Gobra extends GoVerifier with GoIdeVerifier {

  override def verify(input: Vector[Source], config: Config, taskName: String)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor

    val task = Future {

      val finalConfig = getAndMergeInFileConfig(config)
      for {
        parsedPackage <- performParsing(input, finalConfig)
        typeInfo <- performTypeChecking(taskName, parsedPackage, input, finalConfig)
        program <- performDesugaring(parsedPackage, typeInfo, finalConfig)
        program <- performInternalTransformations(program, finalConfig)
        viperTask <- performViperEncoding(program, finalConfig)
      } yield (viperTask, finalConfig)
    }

    task.flatMap{
      case Left(Vector()) => Future(VerifierResult.Success)
      case Left(errors)   => Future(VerifierResult.Failure(errors))
      case Right((job, finalConfig)) => verifyAst(finalConfig, job.program, job.backtrack, taskName)(executor)
    }
  }

  override def verifyAst(config: Config, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo, taskName: String)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    val viperTask = BackendVerifier.Task(ast, backtrack, taskName)
    performVerification(viperTask, config)
      .map(BackTranslator.backTranslate(_)(config))
      .recoverWith {
        case e: ExecutionException if isKnownZ3Bug(e) =>
          // The Z3 instance died. This is a known issue that is caused by a Z3 bug.
          Future.failed(new KnownZ3BugException("Encountered a known Z3 bug. Please, execute the file again."))
      }
  }

  @scala.annotation.tailrec
  private def isKnownZ3Bug(e: ExecutionException): Boolean = {
    def causedByFile(st: Array[StackTraceElement], filename: String): Boolean = {
      if (st.isEmpty) false
      else st.head.getFileName == filename
    }

    e.getCause match {
      case c: ExecutionException => isKnownZ3Bug(c) // follow nested exception
      case npe: NullPointerException if causedByFile(npe.getStackTrace, "Z3ProverStdIO.scala") => true
      case _ => false
    }
  }

  private val inFileConfigRegex = """##\((.*)\)""".r

  /**
    * Parses all inputFiles given in the current config for in-file command line options (wrapped with "## (...)")
    * These in-file command options get combined for all files and passed to ScallopGobraConfig.
    * The current config merged with the newly created config is then returned
    */
  def getAndMergeInFileConfig(config: Config): Config = {
    val inFileConfigs = config.inputs.flatMap(input => {
      val content = input.content
      val configs = for (m <- inFileConfigRegex.findAllMatchIn(content)) yield m.group(1)
      if (configs.isEmpty) {
        None
      } else {
        // our current "merge" strategy for potentially different, duplicate, or even contradicting configurations is to concatenate them:
        val args = configs.flatMap(configString => configString.split(" ")).toList
        // skip include dir checks as the include should only be parsed and is not resolved yet based on the current directory
        val inFileConfig = new ScallopGobraConfig(args, isInputOptional = true, skipIncludeDirChecks = true).config
        // modify all relative includeDirs such that they are resolved relatively to the current file:
        val resolvedConfig = inFileConfig.copy(includeDirs = inFileConfig.includeDirs.map(
          // it's important to convert includeDir to a string first as `path` might be a ZipPath and `includeDir` might not
          includeDir => Paths.get(input.name).getParent.resolve(includeDir.toString)))
        Some(resolvedConfig)
      }
    })

    // start with original config `config` and merge in every in file config:
    inFileConfigs.foldLeft(config){ case (oldConfig, fileConfig) => oldConfig.merge(fileConfig) }
  }

  private def performParsing(input: Vector[Source], config: Config): Either[Vector[VerifierError], PPackage] = {
    if (config.shouldParse) {
      Parser.parse(input)(config)
    } else {
      Left(Vector())
    }
  }

  private def performTypeChecking(taskName: String, parsedPackage: PPackage, input: Vector[Source], config: Config): Either[Vector[VerifierError], TypeInfo] = {
    if (config.shouldTypeCheck) {
      val typeInfo = Info.check(parsedPackage, input, isMainContext = true)(config)

      // Report type info for later usage
      typeInfo match {
        case Right(typeInfo) => config.reporter.report(TypeInfoMessage(typeInfo, taskName))
        case _ =>
      }

      typeInfo
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
    val transformed = CGEdgesTerminationTransform.transform(program)

    if (config.checkOverflows) {
      val result = OverflowChecksTransform.transform(transformed)
      config.reporter report AppliedInternalTransformsMessage(config.inputs.map(_.name), () => result)
      Right(result)
    } else {
      Right(transformed)
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
    val executor: GobraExecutionContext = new DefaultGobraExecutionContext()

    var errorCount = 0
    var warningCount = 0

    val verifier = createVerifier()
    var exitCode = 0
    var statsCollector: StatsCollector = null

    try {
      val scallopGobraconfig = new ScallopGobraConfig(args.toSeq)
      val config = scallopGobraconfig.config
      // Print copyright report
      config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")
      statsCollector = StatsCollector(config.reporter)

      config.inputPackageMap.foreach({ case (pkg, inputs) =>
        val pkgString = pkg.path + " - " + pkg.name

        logger.info("Verifying Package " + pkgString)
        val resultFuture = verifier.verify(config.copy(inputs=inputs, reporter = statsCollector), Some(pkgString))(executor)
        val result = Await.result(resultFuture, Duration.Inf)

        val warnings = statsCollector.getWarnings
        warningCount += warnings.size
        warnings.foreach(m => logger.warn(m))

        result match {
          case VerifierResult.Success => logger.info(s"${verifier.name} found no errors")
          case VerifierResult.Failure(errors) =>
            logger.error(s"${verifier.name} has found ${errors.length} error(s) in package $pkgString")
            errors.foreach(err => logger.error(s"\t${err.formattedMessage}"))
            errorCount += errors.length;
        }
      })
    } catch {
      case e: UglyErrorMessage =>
        logger.error(s"${verifier.name} has found 1 error(s): ")
        logger.error(s"\t${e.error.formattedMessage}")
        errorCount += 1
        exitCode = 1
      case e: LogicException =>
        logger.error("An assumption was violated during execution.")
        logger.error(e.getLocalizedMessage, e)
        exitCode = 1
      case e: KnownZ3BugException =>
        logger.error(e.getLocalizedMessage, e)
        exitCode = 1
      case e: Exception =>
        logger.error("An unknown Exception was thrown.")
        logger.error(e.getLocalizedMessage, e)
        exitCode = 1
    } finally {
      executor.terminate()

      if(warningCount > 1) {
        logger.warn(s"${verifier.name} has found $warningCount warnings(s)")
      } else {
        logger.info(s"${verifier.name} found no warnings")
      }

      if(errorCount > 1) {
        logger.error(s"${verifier.name} has found $errorCount error(s)")
        exitCode = 1
      } else {
        logger.info(s"${verifier.name} found no errors")
      }
      sys.exit(exitCode)
    }
  }
}
