// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import ch.qos.logback.classic.Logger
import scalaz.EitherT

import java.nio.file.Paths
import java.util.concurrent.ExecutionException
import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import scalaz.Scalaz.futureInstance
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.{CGEdgesTerminationTransform, ConstantPropagation, InternalTransform, OverflowChecksTransform}
import viper.gobra.backend.BackendVerifier
import viper.gobra.backend.ViperBackends.{CarbonBackend, SiliconBackend}
import viper.gobra.frontend.PackageResolver.{AbstractPackage, RegularPackage}
import viper.gobra.frontend.Parser.ParseResult
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, PackageInfo, Parser, ScallopGobraConfig}
import viper.gobra.reporting._
import viper.gobra.translator.Translator
import viper.gobra.util.Violation.{KnownZ3BugException, LogicException, UglyErrorMessage}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silicon.BuildInfo
import viper.silver.ast.pretty.FastPrettyPrinter
import viper.silver.utility.ManualProgramSubmitter
import viper.silver.{ast => vpr}
import viper.silver.ast.pretty.FastPrettyPrinter
import viper.silver.utility.ManualProgramSubmitter

import java.time.format.DateTimeFormatter
import java.time.LocalTime
import scala.concurrent.{Await, Future, TimeoutException}

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

  protected var submitters: Map[String, ManualProgramSubmitter] = Map()

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
    var warningCount: Int = 0
    var allVerifierErrors: Vector[VerifierError] = Vector()
    var allTimeoutErrors: Vector[TimeoutError] = Vector()

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

    val viperBackendName: String = config.backend match {
      case CarbonBackend => "Carbon"
      case SiliconBackend => "Silicon"
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    config.packageInfoInputMap.keys.foreach(pkgInfo => {
      val pkgId = pkgInfo.id

      //adds a submitter for this package to submitters if config.submitForEvaluation is set, key is pkgId
      createSubmitter(config.submitForEvaluation, pkgId, viperBackendName)

      logger.info(s"Verifying package $pkgId [${LocalTime.now().format(timeFormatter)}]")
      val future = verify(pkgInfo, config.copy(reporter = statsCollector, taskName = pkgId))(executor)
        .map(result => {
          // report that verification of this package has finished in order that `statsCollector` can free space by getting rid of this package's typeInfo
          statsCollector.report(VerificationTaskFinishedMessage(pkgId))

          val warnings = statsCollector.getMessagesAboutDependencies(pkgId, config)
          warningCount += warnings.size
          warnings.foreach(w => logger.debug(w))

          submitters.get(pkgId).foreach(s => {
            s.setSuccess(result == VerifierResult.Success)
            s.submit()}
          )
          submitters = submitters.removed(pkgId)

          result match {
            case VerifierResult.Success => logger.info(s"$name found no errors")
            case VerifierResult.Failure(errors) =>
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
    logger.debug(s"Gobra has found ${statsCollector.getNumberOfVerifiableMembers} methods and functions" )
    logger.debug(s"${statsCollector.getNumberOfSpecifiedMembers} have specification")
    logger.debug(s"${statsCollector.getNumberOfSpecifiedMembersWithAssumptions} are assumed to be satisfied")

    // Print warnings
    if(warningCount > 0) {
      logger.info(s"$name has found $warningCount warning(s)")
    }

    // Print errors
    logger.info(s"$name has found ${allVerifierErrors.size} error(s)")
    if(allTimeoutErrors.nonEmpty) {
      logger.info(s"The verification of ${allTimeoutErrors.size} members timed out")
    }

    val allErrors = allVerifierErrors ++ allTimeoutErrors
    if (allErrors.isEmpty) VerifierResult.Success else VerifierResult.Failure(allErrors)
  }

  private def createSubmitter(allowSubmission: Boolean, id: String, verifier: String): Unit = {
    if (allowSubmission) {
      val submitter = new ManualProgramSubmitter(true, "", "Gobra", verifier, Array())
      submitters = submitters + (id -> submitter)
    }
  }

  protected[this] def verify(pkgInfo: PackageInfo, config: Config)(implicit executor: GobraExecutionContext): Future[VerifierResult]
}

trait GoIdeVerifier {
  protected[this] def verifyAst(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(executor: GobraExecutionContext): Future[VerifierResult]
}

class Gobra extends GoVerifier with GoIdeVerifier {

  override def verify(pkgInfo: PackageInfo, config: Config)(implicit executor: GobraExecutionContext): Future[VerifierResult] = {
    val task = for {
      finalConfig <- EitherT.fromEither(Future.successful(getAndMergeInFileConfig(config, pkgInfo)))
      _ = setLogLevel(finalConfig)
      parseResults <- performParsing(finalConfig, pkgInfo)
      typeInfo <- performTypeChecking(finalConfig, pkgInfo, parseResults)
      program <- performDesugaring(finalConfig, typeInfo)
      program <- performInternalTransformations(finalConfig, pkgInfo, program)
      viperTask <- performViperEncoding(finalConfig, pkgInfo, program)
    } yield (viperTask, finalConfig)

    submitters.get(pkgInfo.id).foreach(s => task.bimap(
      _ => s.setAllowSubmission(false), //don't submit since no viper program could be produced
      { case (job, _) => s.setProgram(FastPrettyPrinter.pretty(job.program)) }))

    task.foldM({
      case Vector() => Future(VerifierResult.Success)
      case errors => Future(VerifierResult.Failure(errors))
    }, {
      case (job, finalConfig) => performVerification(finalConfig, pkgInfo, job.program,  job.backtrack)
    })
  }

  override def verifyAst(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(executor: GobraExecutionContext): Future[VerifierResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor
    val viperTask = BackendVerifier.Task(ast, backtrack)
    performVerification(config, pkgInfo, viperTask)
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
  def getAndMergeInFileConfig(config: Config, pkgInfo: PackageInfo): Either[Vector[VerifierError], Config] = {
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
    val (errors, inFileConfigs) = inFileEitherConfigs.partitionMap(identity)
    if (errors.nonEmpty) Left(errors.map(ConfigError))
    else {
      // start with original config `config` and merge in every in file config:
      val mergedConfig = inFileConfigs.flatten.foldLeft(config) {
        case (oldConfig, fileConfig) => oldConfig.merge(fileConfig)
      }
      Right(mergedConfig)
    }
  }

  private def setLogLevel(config: Config): Unit = {
    LoggerFactory.getLogger(GoVerifier.rootLogger)
      .asInstanceOf[Logger]
      .setLevel(config.logLevel)
  }

  // returns `Left(...)` if parsing of the package identified by `pkgInfo` failed. Note that `Right(...)` does not imply
  // that all imported packages have been parsed successfully (this is only checked during type-checking)
  private def performParsing(config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, Map[AbstractPackage, ParseResult]] = {
    if (config.shouldParse) {
      val startMs = System.currentTimeMillis()
      val res = Parser.parse(config, pkgInfo)
      logger.debug {
        val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
        s"parser phase done, took ${durationS}s"
      }
      res
    } else {
      EitherT.left(Vector.empty)
    }
  }

  private def performTypeChecking(config: Config, pkgInfo: PackageInfo, parseResults: Map[AbstractPackage, ParseResult])(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, TypeInfo] = {
    if (config.shouldTypeCheck) {
      Info.check(config, RegularPackage(pkgInfo.id), parseResults)
    } else {
      EitherT.left(Vector.empty)
    }
  }

  private def performDesugaring(config: Config, typeInfo: TypeInfo)(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, Program] = {
    if (config.shouldDesugar) {
      val startMs = System.currentTimeMillis()
      val res = EitherT.right[Vector[VerifierError], Future, Program](Desugar.desugar(config, typeInfo)(executor))
      logger.debug {
        val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
        s"desugaring done, took ${durationS}s"
      }
      res
    } else {
      EitherT.left(Vector.empty)
    }
  }

  /**
    * Applies transformations to programs in the internal language. Currently, only adds overflow checks but it can
    * be easily extended to perform more transformations
    */
  private def performInternalTransformations(config: Config, pkgInfo: PackageInfo, program: Program)(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, Program] = {
    // constant propagation does not cause duplication of verification errors caused
    // by overflow checks (if enabled) because all overflows in constant declarations 
    // can be found by the well-formedness checks.
    val startMs = System.currentTimeMillis()
    var transformations: Vector[InternalTransform] = Vector(CGEdgesTerminationTransform, ConstantPropagation)
    if (config.checkOverflows) {
      transformations :+= OverflowChecksTransform
    }
    val result = transformations.foldLeft(program)((prog, transf) => transf.transform(prog))
    logger.debug {
      val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
      s"internal transformations done, took ${durationS}s"
    }
    config.reporter.report(AppliedInternalTransformsMessage(config.packageInfoInputMap(pkgInfo).map(_.name), () => result))
    EitherT.right(result)
  }

  private def performViperEncoding(config: Config, pkgInfo: PackageInfo, program: Program)(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, BackendVerifier.Task] = {
    if (config.shouldViperEncode) {
      val startMs = System.currentTimeMillis()
      val res = EitherT.fromEither[Future, Vector[VerifierError], BackendVerifier.Task](Future.successful(Translator.translate(program, pkgInfo)(config)))
      logger.debug {
        val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
        s"Viper encoding done, took ${durationS}s"
      }
      res
    } else {
      EitherT.left(Vector.empty)
    }
  }

  private def performVerification(config: Config, pkgInfo: PackageInfo, ast: vpr.Program, backtrack: BackTranslator.BackTrackInfo)(implicit executor: GobraExecutionContext): Future[VerifierResult] = {
    if (config.noVerify) {
      Future(VerifierResult.Success)(executor)
    } else {
      verifyAst(config, pkgInfo, ast, backtrack)(executor)
    }
  }

  private def performVerification(config: Config, pkgInfo: PackageInfo, viperTask: BackendVerifier.Task)(implicit executor: GobraExecutionContext): Future[BackendVerifier.Result] = {
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
      exitCode = config match {
        case Left(validationError) =>
          logger.error(validationError)
          1
        case Right(config) =>
          // Print copyright report
          config.reporter report CopyrightReport(s"${GoVerifier.name} ${GoVerifier.version}\n${GoVerifier.copyright}")
          verifier.verifyAllPackages(config)(executor) match {
            case VerifierResult.Failure(_) => 1
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
