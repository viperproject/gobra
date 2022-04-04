// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Paths
import java.util.concurrent.ExecutionException
import com.typesafe.scalalogging.StrictLogging
import viper.gobra.ast.frontend.PPackage
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.{CGEdgesTerminationTransform, OverflowChecksTransform}
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, PackageInfo, Parser, ScallopGobraConfig}
import viper.gobra.reporting._
import viper.gobra.translator.Translator
import viper.gobra.util.Violation.{KnownZ3BugException, LogicException, UglyErrorMessage}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silicon.BuildInfo
import viper.silver.{ast => vpr}

import scala.concurrent.{Await, Future, TimeoutException}

// TODO:
// 1. fix issues with empty packages when running in --onlyFilesWithHeader mode
// 2. fix issues with more than one package name in the same folder (?) => maybe not a problem if we only parse files with header
// 3. test with verifiedSCION
// 4. clean-up, add tests, commit and open a PR with a motivating text

object GoVerifier {

  val copyright = "(c) Copyright ETH Zurich 2012 - 2022"

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
  def verifyAllPackages(config: Config)(executor: GobraExecutionContext): Vector[VerifierError] = {
    val statsCollector = StatsCollector(config.reporter)
    var warningCount: Int = 0
    var allErrors: Vector[VerifierError] = Vector()

    // write report to file on shutdown, this makes sure a report is produced even if a run is shutdown
    // by some signal.
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        val statsFile = config.gobraDirectory.resolve("stats.json").toFile
        logger.info("Writing report to " + statsFile.getPath)
        statsCollector.writeJsonReportToFile(statsFile)

        val timedOutMembers = statsCollector.getTimedOutMembers
        if(timedOutMembers.nonEmpty) {
          timedOutMembers.foreach(member => logger.error(s"The verification of member $member did not terminate"))
        }
      }
    })

    config.packageInfoInputMap.keys.foreach(pkgInfo => {
      val pkgId = pkgInfo.id
      logger.info(s"Verifying Package $pkgId")
      val future = verify(pkgInfo, config.copy(reporter = statsCollector, taskName = pkgInfo.id))(executor)
        .map(result => {
          // report that verification of this package has finished in order that `statsCollector` can free space by getting rid of this package's typeInfo
          statsCollector.report(VerificationTaskFinishedMessage(pkgId))

          val warnings = statsCollector.getWarnings(pkgId, config)
          warningCount += warnings.size
          warnings.foreach(w => logger.warn(w))

          result match {
            case VerifierResult.Success => logger.info(s"$name found no errors")
            case VerifierResult.Failure(errors) =>
              logger.error(s"$name has found ${errors.length} error(s) in package $pkgId")
              errors.foreach(err => logger.error(s"\t${err.formattedMessage}"))
              allErrors = allErrors ++ errors
          }
        })(executor)
      try {
        Await.result(future, config.packageTimeout)
      } catch {
        case _: TimeoutException => logger.error(s"The verification of package $pkgId got terminated after " + config.packageTimeout.toString)
      }
    })

    if(warningCount > 0) {
      logger.info(s"$name has found $warningCount warning(s)")
    }

    logger.info(s"$name has found ${allErrors.size} error(s)")

    // Print statistics for caching
    if(config.cacheFile.isDefined) {
      logger.info(s"Number of cacheable Viper member(s): ${statsCollector.getNumberOfCacheableViperMembers}")
      logger.info(s"Number of cached Viper member(s): ${statsCollector.getNumberOfCachedViperMembers}")
    }

    logger.info(s"Gobra has found ${statsCollector.getNumberOfVerifiableMembers} methods and functions" )
    logger.info(s"${statsCollector.getNumberOfVerifiedMembers} have specification")
    logger.info(s"${statsCollector.getNumberOfVerifiedMembersWithAssumptions} are assumed to be satisfied")

    allErrors
  }

  protected[this] def verify(pkgInfo: PackageInfo, config: Config)(executor: GobraExecutionContext): Future[VerifierResult]
}

trait GoIdeVerifier {
  protected[this] def verifyAst(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(executor: GobraExecutionContext): Future[VerifierResult]
}

class Gobra extends GoVerifier with GoIdeVerifier {

  override def verify(pkgInfo: PackageInfo, config: Config)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor

    val task = Future {
      val finalConfig = getAndMergeInFileConfig(config, pkgInfo)
      for {
        parsedPackage <- performParsing(pkgInfo, finalConfig)
        typeInfo <- performTypeChecking(parsedPackage, finalConfig)
        program <- performDesugaring(parsedPackage, typeInfo, finalConfig)
        program <- performInternalTransformations(program, finalConfig, pkgInfo)
        viperTask <- performViperEncoding(program, finalConfig, pkgInfo)
      } yield (viperTask, finalConfig)
    }

    task.flatMap{
      case Left(Vector()) => Future(VerifierResult.Success)
      case Left(errors)   => Future(VerifierResult.Failure(errors))
      case Right((job, finalConfig)) => verifyAst(finalConfig, pkgInfo, job.program,  job.backtrack)(executor)
    }
  }

  override def verifyAst(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    val viperTask = BackendVerifier.Task(ast, backtrack)
    performVerification(viperTask, config, pkgInfo)
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
  def getAndMergeInFileConfig(config: Config, pkgInfo: PackageInfo): Config = {
    val inFileConfigs = config.packageInfoInputMap(pkgInfo).flatMap(input => {
      val content = input.content
      val configs = for (m <- inFileConfigRegex.findAllMatchIn(content)) yield m.group(1)
      if (configs.isEmpty) {
        None
      } else {
        // our current "merge" strategy for potentially different, duplicate, or even contradicting configurations is to concatenate them:
        val args = configs.flatMap(configString => configString.split(" ")).toList
        val actualArgs = args filter (_ != Config.headerTag)
        // skip include dir checks as the include should only be parsed and is not resolved yet based on the current directory
        val inFileConfig = new ScallopGobraConfig(actualArgs, isInputOptional = true, skipIncludeDirChecks = true).config
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

  private def performParsing(pkgInfo: PackageInfo, config: Config): Either[Vector[VerifierError], PPackage] = {
    if (config.shouldParse) {
      // TODO explain why needed?
      val sourcesToParse = config.packageInfoInputMap(pkgInfo).filter {
        p => !config.onlyFilesWithHeader || Config.sourceHasHeader(p)
      }
      Parser.parse(sourcesToParse, pkgInfo)(config)
    } else {
      Left(Vector())
    }
  }

  private def performTypeChecking(parsedPackage: PPackage, config: Config): Either[Vector[VerifierError], TypeInfo] = {
    if (config.shouldTypeCheck) {
      Info.check(parsedPackage, config.packageInfoInputMap(parsedPackage.info), isMainContext = true)(config)
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
  private def performInternalTransformations(program: Program, config: Config, pkgInfo: PackageInfo): Either[Vector[VerifierError], Program] = {
    val transformed = CGEdgesTerminationTransform.transform(program)

    if (config.checkOverflows) {
      val result = OverflowChecksTransform.transform(transformed)
      config.reporter report AppliedInternalTransformsMessage(config.packageInfoInputMap(pkgInfo).map(_.name), () => result)
      Right(result)
    } else {
      Right(transformed)
    }
  }

  private def performViperEncoding(program: Program, config: Config, pkgInfo: PackageInfo): Either[Vector[VerifierError], BackendVerifier.Task] = {
    if (config.shouldViperEncode) {
      Right(Translator.translate(program, pkgInfo)(config))
    } else {
      Left(Vector())
    }
  }

  private def performVerification(viperTask: BackendVerifier.Task, config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): Future[BackendVerifier.Result] = {
    if (config.shouldVerify) {
      BackendVerifier.verify(viperTask, pkgInfo)(config)
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
    val verifier = createVerifier()
    var exitCode = 0
    try {
      val scallopGobraConfig = new ScallopGobraConfig(args.toSeq)
      val config = scallopGobraConfig.config
      // Print copyright report
      config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")

      val errors = verifier.verifyAllPackages(config)(executor)

      if(errors.nonEmpty) {
        exitCode = 1
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
