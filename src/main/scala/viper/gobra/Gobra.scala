// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Paths
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import scala.concurrent.{Await, Future, TimeoutException}
import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Logger
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.ast.internal.transform.Transformations
import viper.gobra.backend.{BackendVerifier, Task}
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.{Config, Desugar, PackageInfo, Parser, ScallopGobraConfig}
import viper.gobra.reporting._
import viper.gobra.translator.Translator
import viper.gobra.util.VerifierPhase.{ErrorsAndWarnings, PhaseResult, Warnings}
import viper.gobra.util.Violation.{KnownZ3BugException, LogicException, UglyErrorMessage}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext, VerifierPhase, VerifierPhaseNonFinal}
import viper.silicon.BuildInfo
import viper.silver.{ast => vpr}

object GoVerifier {

  val copyright = "(c) Copyright ETH Zurich 2012 - 2024"

  val name = "Gobra"

  val rootLogger = "viper.gobra"

  val version: String = {
    val buildRevision = BuildInfo.gitRevision
    val buildBranch = BuildInfo.gitBranch
    val buildVersion = s"$buildRevision${if (buildBranch == "master") "" else s"@$buildBranch"}"

    s"${BuildInfo.projectVersion} ($buildVersion)"
  }
}

trait GoVerifier extends StrictLogging {

  def name: String = {
    this.getClass.getSimpleName
  }

  /**
   * Verifies all packages defined in the packageInfoInputMap of the config.
   * It uses the package identifier to uniquely identify each verification task on a package level.
   * Additionally statistics are collected with the StatsCollector reporter class
   */
  def verifyAllPackages(config: Config)(executor: GobraExecutionContext): VerifierResult = {
    val statsCollector = StatsCollector(config.reporter)
    var dependencyWarningCount: Int = 0
    var allVerifierErrors: Vector[VerifierError] = Vector.empty
    var allTimeoutErrors: Vector[TimeoutError] = Vector.empty
    var allVerifierWarnings: Vector[VerifierWarning] = Vector.empty

    // write report to file on shutdown, this makes sure a report is produced even if a run is shutdown
    // by some signal.
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        val statsFile = config.gobraDirectory.resolve("stats.json").toFile
        logger.info("Writing report to " + statsFile.getPath)
        val wroteFile = statsCollector.writeJsonReportToFile(statsFile)
        if (!wroteFile) {
          logger.error(s"Could not write to the file $statsFile. Check whether the permissions to the file allow writing to it.")
        }

        // Report timeouts that were not previously reported
        statsCollector.getTimeoutErrorsForNonFinishedTasks.foreach(err => logger.error(err.formattedMessage))
      }
    })

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    config.packageInfoInputMap.keys.foreach(pkgInfo => {
      val pkgId = pkgInfo.id
      logger.info(s"Verifying package $pkgId [${LocalTime.now().format(timeFormatter)}]")
      val future = verify(pkgInfo, config.copy(reporter = statsCollector, taskName = pkgId))(executor)
        .map(result => {
          // report that verification of this package has finished in order that `statsCollector` can free space by getting rid of this package's typeInfo
          statsCollector.report(VerificationTaskFinishedMessage(pkgId))

          val dependencyWarnings = statsCollector.getMessagesAboutDependencies(pkgId, config)
          dependencyWarningCount += dependencyWarnings.size
          dependencyWarnings.foreach(w => logger.debug(w))

          def handleWarnings(warnings: Vector[VerifierWarning]): Unit = {
            logger.warn(s"$name has found ${warnings.length} warning(s) in package $pkgId")
            if (config.noStreamErrors) {
              warnings.foreach(warning => logger.warn(s"\t${warning.formattedMessage}"))
            }
            allVerifierWarnings = allVerifierWarnings ++ warnings
          }

          result match {
            case VerifierResult.Success(warnings) =>
              if (warnings.isEmpty) {
                logger.info(s"$name found no errors")
              } else {
                handleWarnings(warnings)
              }
            case VerifierResult.Failure(errors, warnings) =>
              if (warnings.nonEmpty) handleWarnings(warnings)
              logger.error(s"$name has found ${errors.length} error(s) in package $pkgId")
              if (config.noStreamErrors) {
                errors.foreach(err => logger.error(s"\t${err.formattedMessage}"))
              }
              allVerifierErrors = allVerifierErrors ++ errors
          }
        })(executor)
      try {
        Await.result(future, config.packageTimeout)
      } catch {
        case _: TimeoutException =>
          logger.error(s"The verification of package $pkgId got terminated after " + config.packageTimeout.toString)
          statsCollector.report(VerificationTaskFinishedMessage(pkgId))
          val errors = statsCollector.getTimeoutErrors(pkgId)
          errors.foreach(err => logger.error(err.formattedMessage))
          allTimeoutErrors = allTimeoutErrors ++ errors
      }
    })

    // Print statistics for caching
    if(config.cacheFile.isDefined) {
      logger.debug(s"Number of cacheable Viper member(s): ${statsCollector.getNumberOfCacheableViperMembers}")
      logger.debug(s"Number of cached Viper member(s): ${statsCollector.getNumberOfCachedViperMembers}")
    }

    // Print general statistics
    logger.debug(s"$name has found ${statsCollector.getNumberOfVerifiableMembers} methods and functions" )
    logger.debug(s"${statsCollector.getNumberOfSpecifiedMembers} have specification")
    logger.debug(s"${statsCollector.getNumberOfSpecifiedMembersWithAssumptions} are assumed to be satisfied")

    // Print warnings
    if(dependencyWarningCount > 0) {
      logger.warn(s"$name has found $dependencyWarningCount warning(s)")
    }

    // Print errors
    logger.error(s"$name has found ${allVerifierErrors.size} error(s)")
    if(allTimeoutErrors.nonEmpty) {
      logger.error(s"The verification of ${allTimeoutErrors.size} members timed out")
    }

    val allErrors = allVerifierErrors ++ allTimeoutErrors
    if (allErrors.isEmpty) VerifierResult.Success(allVerifierWarnings) else VerifierResult.Failure(allErrors, allVerifierWarnings)
  }

  protected[this] def verify(pkgInfo: PackageInfo, config: Config)(implicit executor: GobraExecutionContext): Future[VerifierResult]
}

trait GoIdeVerifier {
  protected[this] def verifyAst(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(implicit executor: GobraExecutionContext): Future[VerifierResult]
}

class Gobra extends GoVerifier with GoIdeVerifier {

  override def verify(pkgInfo: PackageInfo, config: Config)(implicit executor: GobraExecutionContext): Future[VerifierResult] = {
    val phases = for {
      // while IntelliJ seems to be able to do the next 2 statements in one, the Scala compiler complains
      configRes <- FileConfigMerger.perform((config, pkgInfo), logTiming = false)
      (finalConfig, configWarnings) = configRes
      _ = setLogLevel(finalConfig)
      parseRes <- Parser.perform((finalConfig, pkgInfo), finalConfig.shouldParse)
      (parseResults, parseWarnings) = parseRes
      typeRes <- Info.perform((finalConfig, pkgInfo, parseResults), finalConfig.shouldTypeCheck)
      (typeInfo, typeWarnings) = typeRes
      desugarRes <- Desugar.perform((finalConfig, typeInfo), config.shouldDesugar)
      (program, desugarWarnings) = desugarRes
      transformRes <- Transformations.perform((finalConfig, pkgInfo, program))
      (program, transformWarnings) = transformRes
      encodingRes <- Translator.perform((finalConfig, pkgInfo, program), config.shouldViperEncode)
      (viperTask, encodingWarnings) = encodingRes
      warnings = configWarnings ++ parseWarnings ++ typeWarnings ++ desugarWarnings ++ transformWarnings ++ encodingWarnings
    } yield BackendVerifier.perform((finalConfig, pkgInfo, viperTask, warnings), config.shouldVerify)

    phases.foldM(errorsAndWarnings => {
      val (errors, warnings) = VerifierPhase.splitErrorsAndWarnings(errorsAndWarnings)
      if (errors.isEmpty) Future.successful(VerifierResult.Success(warnings))
      else Future.successful(VerifierResult.Failure(errors, warnings))
    }, identity)
  }

  override def verifyAst(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(implicit executor: GobraExecutionContext): Future[VerifierResult] = {
    val task = Task(ast, backtrack)
    BackendVerifier.perform((config, pkgInfo, task, Vector.empty), config.shouldVerify)
  }

  private def setLogLevel(config: Config): Unit = {
    LoggerFactory.getLogger(GoVerifier.rootLogger)
      .asInstanceOf[Logger]
      .setLevel(config.logLevel)
  }
}

object FileConfigMerger extends VerifierPhaseNonFinal[(Config, PackageInfo), Config] {
  override val name: String = "Configuration merging"

  private val inFileConfigRegex = """##\((.*)\)""".r

  /**
    * Parses all inputFiles given in the current config for in-file command line options (wrapped with "## (...)")
    * These in-file command options get combined for all files and passed to ScallopGobraConfig.
    * The current config merged with the newly created config is then returned
    */
  override protected def execute(input: (Config, PackageInfo))(implicit executor: GobraExecutionContext): PhaseResult[Config] = {
    val (config, pkgInfo) = input
    val inFileEitherConfigs = config.packageInfoInputMap(pkgInfo).map(input => {
      val content = input.content
      val configs = for (m <- inFileConfigRegex.findAllMatchIn(content)) yield m.group(1)
      if (configs.isEmpty) {
        Right(None)
      } else {
        // our current "merge" strategy for potentially different, duplicate, or even contradicting configurations is to concatenate them:
        val args = configs.flatMap(configString => configString.split(" ")).toList
        // skip include dir checks as the include should only be parsed and is not resolved yet based on the current directory
        for {
          inFileConfig <- new ScallopGobraConfig(args, isInputOptional = true, skipIncludeDirChecks = true).config
          resolvedConfig = inFileConfig.copy(includeDirs = inFileConfig.includeDirs.map(
            // it's important to convert includeDir to a string first as `path` might be a ZipPath and `includeDir` might not
            includeDir => Paths.get(input.name).getParent.resolve(includeDir.toString)))
        } yield Some(resolvedConfig)
      }
    })
    val (nestedErrs, inFileConfigs) = inFileEitherConfigs.partitionMap(identity)
    val errors = nestedErrs.flatten
    if (errors.nonEmpty) EitherT.left(errors)
    else {
      // start with original config `config` and merge in every in file config:
      val mergedConfig = inFileConfigs.flatten.foldLeft(config) {
        case (oldConfig, fileConfig) => oldConfig.merge(fileConfig)
      }
      EitherT.right((mergedConfig, Vector.empty))
    }
  }

  def mergeInFileConfig(config: Config, pkgInfo: PackageInfo): Either[ErrorsAndWarnings, (Config, Warnings)] = {
    val inFileEitherConfigs = config.packageInfoInputMap(pkgInfo).map(input => {
      val content = input.content
      val configs = for (m <- inFileConfigRegex.findAllMatchIn(content)) yield m.group(1)
      if (configs.isEmpty) {
        Right(None)
      } else {
        // our current "merge" strategy for potentially different, duplicate, or even contradicting configurations is to concatenate them:
        val args = configs.flatMap(configString => configString.split(" ")).toList
        // skip include dir checks as the include should only be parsed and is not resolved yet based on the current directory
        for {
          inFileConfig <- new ScallopGobraConfig(args, isInputOptional = true, skipIncludeDirChecks = true).config
          resolvedConfig = inFileConfig.copy(includeDirs = inFileConfig.includeDirs.map(
            // it's important to convert includeDir to a string first as `path` might be a ZipPath and `includeDir` might not
            includeDir => Paths.get(input.name).toAbsolutePath.getParent.resolve(includeDir.toString)))
        } yield Some(resolvedConfig)
      }
    })
    val (errors, inFileConfigs) = inFileEitherConfigs.partitionMap(identity)
    if (errors.nonEmpty) Left(errors.flatten)
    else {
      // start with original config `config` and merge in every in file config:
      val mergedConfig = inFileConfigs.flatten.foldLeft(config) {
        case (oldConfig, fileConfig) => oldConfig.merge(fileConfig)
      }
      Right((mergedConfig, Vector.empty))
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
    val verifier = createVerifier()
    var exitCode = 0
    try {
      val scallopGobraConfig = new ScallopGobraConfig(args.toSeq)
      val config = scallopGobraConfig.config
      exitCode = config match {
        case Left(errors) =>
          errors.foreach(err => logger.error(err.formattedMessage))
          1
        case Right(config) =>
          // Print copyright report
          config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")
          verifier.verifyAllPackages(config)(executor) match {
            case _: VerifierResult.Failure => 1
            case _ => 0
          }
      }
    } catch {
      case e: UglyErrorMessage =>
        logger.error(s"${verifier.name} has found 1 error(s): ")
        logger.error(s"\t${e.error.formattedMessage}")
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
      sys.exit(exitCode)
    }
  }
}
