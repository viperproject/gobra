// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import ch.qos.logback.classic.Logger
import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.{CGEdgesTerminationTransform, ConstantPropagation, InternalTransform, OverflowChecksTransform}
import viper.gobra.backend.BackendVerifier
import viper.gobra.dependencyAnalysis.GobraDependencyAnalysisAggregator
import viper.gobra.frontend.PackageResolver.{AbstractPackage, RegularPackage}
import viper.gobra.frontend.Parser.ParseResult
import viper.gobra.frontend._
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.reporting._
import viper.gobra.translator.Translator
import viper.gobra.util.Violation.{KnownZ3BugException, LogicException, UglyErrorMessage}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silicon.BuildInfo
import viper.silicon.dependencyAnalysis.{DependencyAnalysisUserTool, DependencyGraphInterpreter}
import viper.silver.dependencyAnalysis.AbstractDependencyAnalysisResult
import viper.silver.{ast => vpr}

import java.nio.file.Paths
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutionException
import scala.concurrent.{Await, Future, TimeoutException}

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
    def addPlural(n: Int): String = if (n != 1) "s" else ""
    val statsCollector = StatsCollector(config.reporter)
    var warningCount: Int = 0
    var allVerifierErrors: Vector[VerifierError] = Vector()
    var allTimeoutErrors: Vector[TimeoutError] = Vector()
    val isVerifyingMultiplePackages = config.packageInfoInputMap.size != 1

    // write report to file on shutdown, this makes sure a report is produced even if a run is shutdown
    // by some signal.
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        config.gobraDirectory match {
          case Some(path) =>
            val statsFile = path.resolve("stats.json").toFile
            logger.info("Writing report to " + statsFile.getPath)
            val wroteFile = statsCollector.writeJsonReportToFile(statsFile)
            if (!wroteFile) {
              logger.error(s"Could not write to the file $statsFile. Check whether the permissions to the file allow writing to it.")
            }
          case _ =>
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

          val warnings = statsCollector.getMessagesAboutDependencies(pkgId, config)
          warningCount += warnings.size
          warnings.foreach(w => logger.debug(w))

          val suffix = if (isVerifyingMultiplePackages) s" in package $pkgId" else ""
          result match {
            case VerifierResult.Success =>
              logger.info(s"$name found 0 errors$suffix.")
            case VerifierResult.Failure(errors) =>
              logger.error(s"$name found ${errors.length} error${addPlural(errors.length)}$suffix.")
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
          logger.error(s"The verification of package $pkgId timed out after ${config.packageTimeout}.")
          statsCollector.report(VerificationTaskFinishedMessage(pkgId))
          val errors = statsCollector.getTimeoutErrors(pkgId)
          errors.foreach(err => logger.error(err.formattedMessage))
          allTimeoutErrors = allTimeoutErrors ++ errors
      }
    })

    // Print statistics for caching
    if(config.cacheFile.isDefined) {
      logger.debug(s"Number of cacheable Viper members: ${statsCollector.getNumberOfCacheableViperMembers}")
      logger.debug(s"Number of cached Viper members: ${statsCollector.getNumberOfCachedViperMembers}")
    }

    // Print general statistics
    logger.debug(s"Gobra found ${statsCollector.getNumberOfVerifiableMembers} methods and functions." )
    logger.debug{
      val specMem = statsCollector.getNumberOfSpecifiedMembers
      s"$specMem member${addPlural(specMem)} are explicitly specified."
    }
    logger.debug{
      val memAssum = statsCollector.getNumberOfSpecifiedMembersWithAssumptions
      val isOrAre = if (memAssum != 1) "are" else "is"
      s"$memAssum specified member${addPlural(memAssum)} of the package under verification $isOrAre trusted or abstract."
    }

    // Print warnings
    if(warningCount > 0) {
      logger.debug(s"$name reported $warningCount warning${addPlural(warningCount)}.")
    }

    // Print errors
    if (isVerifyingMultiplePackages) {
      logger.info(s"$name found ${allVerifierErrors.size} error${addPlural(allVerifierErrors.size)} across all verified packages.")
    }
    if(allTimeoutErrors.nonEmpty) {
      logger.info(s"The verification of ${allTimeoutErrors.size} member${addPlural(allTimeoutErrors.size)} timed out.")
    }

    val allErrors = allVerifierErrors ++ allTimeoutErrors
    if (allErrors.isEmpty) VerifierResult.Success else VerifierResult.Failure(allErrors)
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
      viperTask <- performViperEncoding(finalConfig, pkgInfo, program, typeInfo)
    } yield (viperTask, finalConfig)

    task.foldM({
      case Vector() => Future(VerifierResult.Success)
      case errors => Future(VerifierResult.Failure(errors))
    }, {
      case (job, finalConfig) => performVerification(finalConfig, pkgInfo, job.program,  job.backtrack)
    })
  }

  // TODO ake: where to call this?
  def runDependencyAnalysisWorkflow(dependencyAnalysisResult: AbstractDependencyAnalysisResult, result: VerifierResult, config: Config, typeInfo: TypeInfo): Unit = {
    if(config.enableDependencyAnalysis && config.startDependencyAnalysisTool){
      val errors = result match {
        case VerifierResult.Success => Vector.empty
        case VerifierResult.Failure(errors) => errors
      }
      val interpreter = GobraDependencyAnalysisAggregator.convertFromDependencyGraphInterpreter(dependencyAnalysisResult.getFullDependencyGraphInterpreter.asInstanceOf[DependencyGraphInterpreter], typeInfo, errors.toList)
      val userTool = new DependencyAnalysisUserTool(interpreter, Seq.empty /* TODO ake */, viper.silver.ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq.empty)(), List.empty)
      userTool.run()
    }
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
    var transformations: Vector[InternalTransform] = Vector(CGEdgesTerminationTransform) ++ (if(config.enableDependencyAnalysis) Vector() else Vector(ConstantPropagation))
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

  private def performViperEncoding(config: Config, pkgInfo: PackageInfo, program: Program, typeInfo: TypeInfo)(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, BackendVerifier.Task] = {
    if (config.shouldViperEncode) {
      val startMs = System.currentTimeMillis()
      val res = EitherT.fromEither[Future, Vector[VerifierError], BackendVerifier.Task](Future.successful(Translator.translate(program, pkgInfo, typeInfo)(config)))
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
        case Left(errors) =>
          errors.foreach(err => logger.error(err.formattedMessage))
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
        logger.error(s"${verifier.name} has found 1 error: ")
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
