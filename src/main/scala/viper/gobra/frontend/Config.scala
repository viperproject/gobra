// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.File
import java.nio.file.Path
import ch.qos.logback.classic.Level
import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.util.{FileSource, Source}
import org.rogach.scallop.{ScallopConf, ScallopOption, singleArgConverter}
import viper.gobra.backend.{ViperBackend, ViperBackends}
import viper.gobra.GoVerifier
import viper.gobra.frontend.PackageResolver.FileResource
import viper.gobra.frontend.Source.getPackageInfo
import viper.gobra.util.TaskManagerMode.{Lazy, Parallel, Sequential, TaskManagerMode}
import viper.gobra.reporting.{ConfigError, FileWriterReporter, GobraReporter, StdIOReporter, VerifierError}
import viper.gobra.util.{TaskManagerMode, TypeBounds, Violation}
import viper.silver.ast.SourcePosition

import scala.concurrent.duration.Duration
import scala.util.matching.Regex

object LoggerDefaults {
  val DefaultLevel: Level = Level.INFO
}

object ConfigDefaults {
  val DefaultModuleName: String = ""
  lazy val DefaultProjectRoot: File = new File("").getAbsoluteFile // current working directory
  val DefaultIncludePackages: List[String] = List.empty
  val DefaultExcludePackages: List[String] = List.empty
  val DefaultIncludeDirs: List[File] = List.empty
  lazy val DefaultReporter: GobraReporter = StdIOReporter()
  val DefaultBackend: ViperBackend = ViperBackends.SiliconBackend
  val DefaultIsolate: List[(Path, List[Int])] = List.empty
  val DefaultChoppingUpperBound: Int = 1
  val DefaultPackageTimeout: Duration = Duration.Inf
  val DefaultZ3Exe: Option[String] = None
  val DefaultBoogieExe: Option[String] = None
  val DefaultLogLevel: Level = LoggerDefaults.DefaultLevel
  val DefaultCacheFile: Option[File] = None
  val DefaultParseOnly: Boolean = false
  val DefaultStopAfterEncoding: Boolean = false
  val DefaultCheckOverflows: Boolean = false
  val DefaultCheckConsistency: Boolean = false
  val DefaultShouldChop: Boolean = false
  // The go language specification states that int and uint variables can have either 32bit or 64, as long
  // as they have the same size. This flag allows users to pick the size of int's and uints's: 32 if true,
  // 64 bit otherwise.
  val DefaultInt32bit: Boolean = false
  // the following option is currently not controllable via CLI as it is meaningless without a constantly
  // running JVM. It is targeted in particular to Gobra Server and Gobra IDE
  val DefaultCacheParserAndTypeChecker: Boolean = false
  // this option introduces a mode where Gobra only considers files with a specific annotation ("// +gobra").
  // this is useful when verifying large packages where some files might use some unsupported feature of Gobra,
  // or when the goal is to gradually verify part of a package without having to provide an explicit list of the files
  // to verify.
  val DefaultOnlyFilesWithHeader: Boolean = false
  // In the past, the default Gobra directory used to be Path.of(".gobra")
  lazy val DefaultGobraDirectory: Option[Path] = None
  val DefaultTaskName: String = "gobra-task"
  val DefaultAssumeInjectivityOnInhale: Boolean = true
  val DefaultParallelizeBranches: Boolean = false
  val DefaultConditionalizePermissions: Boolean = false
  val DefaultZ3APIMode: Boolean = false
  val DefaultDisableNL: Boolean = false
  val DefaultMCEMode: MCE.Mode = MCE.Enabled
  val DefaultHyperMode: Hyper.Mode = Hyper.Disabled
  lazy val DefaultEnableLazyImports: Boolean = false
  val DefaultNoVerify: Boolean = false
  val DefaultNoStreamErrors: Boolean = false
  val DefaultParseAndTypeCheckMode: TaskManagerMode = TaskManagerMode.Parallel
  val DefaultRequireTriggers: Boolean = false
  val DefaultDisableSetAxiomatization: Boolean = false
  val DefaultDisableCheckTerminationPureFns: Boolean = false
  val DefaultUnsafeWildcardOptimization: Boolean = false
  val DefaultMoreJoins: MoreJoins.Mode = MoreJoins.Disabled
  val DefaultRespectFunctionPrePermAmounts: Boolean = false
  val DefaultEnableExperimentalFriendClauses: Boolean = false
  val DefaultDisableInfeasibilityChecks: Boolean = false
  val DefaultEnableDependencyAnalysis: Boolean = false
  val DefaultStartDependencyAnalysisTool: Boolean = false
}

// More-complete exhale modes
object MCE {
  sealed trait Mode
  object Disabled extends Mode
  // When running in `OnDemand`, mce will only be enabled when silicon retries a query.
  // More information can be found in https://github.com/viperproject/silicon/pull/682.
  object OnDemand extends Mode
  object Enabled extends Mode
}

object Hyper {
  sealed trait Mode
  /** uses more complete encoding that does not enforce low guards for control flow */
  object EnabledExtended extends Mode
  object Enabled extends Mode
  object Disabled extends Mode
  object NoMajor extends Mode
}

object MoreJoins {
  sealed trait Mode {
    // Option number used by Viper, as described in
    // https://github.com/viperproject/silicon/pull/823
    def viperValue: Int
  }

  object Disabled extends Mode {
    override val viperValue = 0
  }

  object Impure extends Mode {
    override val viperValue = 1
  }

  object All extends Mode {
    override val viperValue = 2
  }

  def merge(m1: Mode, m2: Mode): Mode = {
    (m1, m2) match {
      case (All, _) | (_, All) => All
      case (Impure, _) | (_, Impure) => Impure
      case _ => Disabled
    }
  }
}

case class Config(
                   gobraDirectory: Option[Path] = ConfigDefaults.DefaultGobraDirectory,
                   // Used as an identifier of a verification task, ideally it shouldn't change between verifications
                   // because it is used as a caching key. Additionally it should be unique when using the StatsCollector
                   taskName: String = ConfigDefaults.DefaultTaskName,
                   // Contains a mapping of packages to all input sources for that package
                   packageInfoInputMap: Map[PackageInfo, Vector[Source]] = Map.empty,
                   moduleName: String = ConfigDefaults.DefaultModuleName,
                   includeDirs: Vector[Path] = ConfigDefaults.DefaultIncludeDirs.map(_.toPath).toVector,
                   projectRoot: Path = ConfigDefaults.DefaultProjectRoot.toPath,
                   reporter: GobraReporter = ConfigDefaults.DefaultReporter,
                   // `None` indicates that no backend has been specified and instructs Gobra to use the default backend
                   backend: Option[ViperBackend] = None,
                   isolate: Option[Vector[SourcePosition]] = None,
                   choppingUpperBound: Int = ConfigDefaults.DefaultChoppingUpperBound,
                   packageTimeout: Duration = ConfigDefaults.DefaultPackageTimeout,
                   z3Exe: Option[String] = ConfigDefaults.DefaultZ3Exe,
                   boogieExe: Option[String] = ConfigDefaults.DefaultBoogieExe,
                   logLevel: Level = ConfigDefaults.DefaultLogLevel,
                   cacheFile: Option[Path] = ConfigDefaults.DefaultCacheFile.map(_.toPath),
                   shouldParse: Boolean = true,
                   shouldTypeCheck: Boolean = true,
                   shouldDesugar: Boolean = true,
                   shouldViperEncode: Boolean = true,
                   checkOverflows: Boolean = ConfigDefaults.DefaultCheckOverflows,
                   checkConsistency: Boolean = ConfigDefaults.DefaultCheckConsistency,
                   shouldVerify: Boolean = true,
                   shouldChop: Boolean = ConfigDefaults.DefaultShouldChop,
                   // The go language specification states that int and uint variables can have either 32bit or 64, as long
                   // as they have the same size. This flag allows users to pick the size of int's and uints's: 32 if true,
                   // 64 bit otherwise.
                   int32bit: Boolean = ConfigDefaults.DefaultInt32bit,
                   // the following option is currently not controllable via CLI as it is meaningless without a constantly
                   // running JVM. It is targeted in particular to Gobra Server and Gobra IDE
                   cacheParserAndTypeChecker: Boolean = ConfigDefaults.DefaultCacheParserAndTypeChecker,
                   // this option introduces a mode where Gobra only considers files with a specific annotation ("// +gobra").
                   // this is useful when verifying large packages where some files might use some unsupported feature of Gobra,
                   // or when the goal is to gradually verify part of a package without having to provide an explicit list of the files
                   // to verify.
                   onlyFilesWithHeader: Boolean = ConfigDefaults.DefaultOnlyFilesWithHeader,
                   // if enabled, Gobra assumes injectivity on inhale, as done by Viper versions before 2022.2.
                   assumeInjectivityOnInhale: Boolean = ConfigDefaults.DefaultAssumeInjectivityOnInhale,
                   // if enabled, and if the chosen backend is either SILICON or VSWITHSILICON,
                   // branches will be verified in parallel
                   parallelizeBranches: Boolean = ConfigDefaults.DefaultParallelizeBranches,
                   conditionalizePermissions: Boolean = ConfigDefaults.DefaultConditionalizePermissions,
                   z3APIMode: Boolean = ConfigDefaults.DefaultZ3APIMode,
                   disableNL: Boolean = ConfigDefaults.DefaultDisableNL,
                   mceMode: MCE.Mode = ConfigDefaults.DefaultMCEMode,
                   // `None` indicates that no mode has been specified and instructs Gobra to use the default hyper mode
                   hyperMode: Option[Hyper.Mode] = None,
                   noVerify: Boolean = ConfigDefaults.DefaultNoVerify,
                   noStreamErrors: Boolean = ConfigDefaults.DefaultNoStreamErrors,
                   parseAndTypeCheckMode: TaskManagerMode = ConfigDefaults.DefaultParseAndTypeCheckMode,
                   // when enabled, all quantifiers without triggers are rejected
                   requireTriggers: Boolean = ConfigDefaults.DefaultRequireTriggers,
                   disableSetAxiomatization: Boolean = ConfigDefaults.DefaultDisableSetAxiomatization,
                   disableCheckTerminationPureFns: Boolean = ConfigDefaults.DefaultDisableCheckTerminationPureFns,
                   unsafeWildcardOptimization: Boolean = ConfigDefaults.DefaultUnsafeWildcardOptimization,
                   moreJoins: MoreJoins.Mode = ConfigDefaults.DefaultMoreJoins,
                   respectFunctionPrePermAmounts: Boolean = ConfigDefaults.DefaultRespectFunctionPrePermAmounts,
                   enableExperimentalFriendClauses: Boolean = ConfigDefaults.DefaultEnableExperimentalFriendClauses,
                   disableInfeasibilityChecks: Boolean = ConfigDefaults.DefaultDisableInfeasibilityChecks,
                   enableDependencyAnalysis: Boolean = ConfigDefaults.DefaultEnableDependencyAnalysis,
                   startDependencyAnalysisTool: Boolean = ConfigDefaults.DefaultStartDependencyAnalysisTool
) {

  def merge(other: Config): Config = {
    // this config takes precedence over other config
    val newInputs: Map[PackageInfo, Vector[Source]] = {
      val keys = packageInfoInputMap.keys ++ other.packageInfoInputMap.keys
      keys.map(k => k -> (packageInfoInputMap.getOrElse(k, Vector()) ++ other.packageInfoInputMap.getOrElse(k, Vector())).distinct).toMap
    }

    Config(
      moduleName = moduleName,
      taskName = taskName,
      gobraDirectory = gobraDirectory,
      packageInfoInputMap = newInputs,
      projectRoot = projectRoot,
      includeDirs = (includeDirs ++ other.includeDirs).distinct,
      reporter = reporter,
      backend = (backend, other.backend) match {
        case (l, None) => l
        case (None, r) => r
        case (l, r) if l == r => l
        case (Some(l), Some(r)) => Violation.violation(s"Unable to merge differing backends from in-file configuration options, got $l and $r")
      },
      isolate = (isolate, other.isolate) match {
        case (None, r) => r
        case (l, None) => l
        case (Some(l), Some(r)) => Some((l ++ r).distinct)
      },
      packageTimeout = if(packageTimeout < other.packageTimeout) packageTimeout else other.packageTimeout, // take minimum
      z3Exe = z3Exe orElse other.z3Exe,
      boogieExe = boogieExe orElse other.boogieExe,
      logLevel = if (logLevel.isGreaterOrEqual(other.logLevel)) other.logLevel else logLevel, // take minimum
      // TODO merge strategy for following properties is unclear (maybe AND or OR)
      cacheFile = cacheFile orElse other.cacheFile,
      shouldParse = shouldParse,
      shouldTypeCheck = shouldTypeCheck,
      shouldDesugar = shouldDesugar,
      shouldViperEncode = shouldViperEncode,
      checkOverflows = checkOverflows || other.checkOverflows,
      shouldVerify = shouldVerify,
      int32bit = int32bit || other.int32bit,
      checkConsistency = checkConsistency || other.checkConsistency,
      cacheParserAndTypeChecker = cacheParserAndTypeChecker || other.cacheParserAndTypeChecker,
      onlyFilesWithHeader = onlyFilesWithHeader || other.onlyFilesWithHeader,
      assumeInjectivityOnInhale = assumeInjectivityOnInhale || other.assumeInjectivityOnInhale,
      parallelizeBranches = parallelizeBranches,
      conditionalizePermissions = conditionalizePermissions,
      z3APIMode = z3APIMode || other.z3APIMode,
      disableNL = disableNL || other.disableNL,
      mceMode = mceMode,
      hyperMode = (hyperMode, other.hyperMode) match {
        case (l, None) => l
        case (None, r) => r
        case (l, r) if l == r => l
        case (Some(l), Some(r)) => Violation.violation(s"Unable to merge differing hyper modes from in-file configuration options, got $l and $r")
      },
      noVerify = noVerify || other.noVerify,
      noStreamErrors = noStreamErrors || other.noStreamErrors,
      parseAndTypeCheckMode = parseAndTypeCheckMode,
      requireTriggers = requireTriggers || other.requireTriggers,
      disableSetAxiomatization = disableSetAxiomatization || other.disableSetAxiomatization,
      disableCheckTerminationPureFns = disableCheckTerminationPureFns || other.disableCheckTerminationPureFns,
      unsafeWildcardOptimization = unsafeWildcardOptimization && other.unsafeWildcardOptimization,
      moreJoins = MoreJoins.merge(moreJoins, other.moreJoins),
      respectFunctionPrePermAmounts = respectFunctionPrePermAmounts || other.respectFunctionPrePermAmounts,
      enableExperimentalFriendClauses = enableExperimentalFriendClauses || other.enableExperimentalFriendClauses,
      disableInfeasibilityChecks = disableInfeasibilityChecks || other.disableInfeasibilityChecks,
      enableDependencyAnalysis = enableDependencyAnalysis || other.enableDependencyAnalysis,
      startDependencyAnalysisTool = startDependencyAnalysisTool || other.startDependencyAnalysisTool,
    )
  }

  lazy val typeBounds: TypeBounds =
    if (int32bit) {
      TypeBounds()
    } else {
      TypeBounds(Int = TypeBounds.IntWith64Bit, UInt = TypeBounds.UIntWith64Bit)
    }

  val backendOrDefault: ViperBackend = backend.getOrElse(ConfigDefaults.DefaultBackend)
  val hyperModeOrDefault: Hyper.Mode = hyperMode.getOrElse(ConfigDefaults.DefaultHyperMode)
}

object Config {
  // the header signals that a file should be considered when running on "header-only" mode
  val header: Regex = """\/\/\s*\+gobra""".r
  val prettyPrintedHeader = "// +gobra"
  require(header.matches(prettyPrintedHeader))
  def sourceHasHeader(s: Source): Boolean = header.findFirstIn(s.content).nonEmpty

  val enableLazyImportOptionName = "enableLazyImport"
  val enableLazyImportOptionPrettyPrinted = s"--$enableLazyImportOptionName"
}

// have a look at `Config` to see an inline description of some of these parameters
case class BaseConfig(gobraDirectory: Option[Path] = ConfigDefaults.DefaultGobraDirectory,
                      moduleName: String = ConfigDefaults.DefaultModuleName,
                      includeDirs: Vector[Path] = ConfigDefaults.DefaultIncludeDirs.map(_.toPath).toVector,
                      reporter: GobraReporter = ConfigDefaults.DefaultReporter,
                      backend: Option[ViperBackend] = None,
                      // list of pairs of file and line numbers. Indicates which lines of files should be isolated.
                      isolate: List[(Path, List[Int])] = ConfigDefaults.DefaultIsolate,
                      choppingUpperBound: Int = ConfigDefaults.DefaultChoppingUpperBound,
                      packageTimeout: Duration = ConfigDefaults.DefaultPackageTimeout,
                      z3Exe: Option[String] = ConfigDefaults.DefaultZ3Exe,
                      boogieExe: Option[String] = ConfigDefaults.DefaultBoogieExe,
                      logLevel: Level = ConfigDefaults.DefaultLogLevel,
                      cacheFile: Option[Path] = ConfigDefaults.DefaultCacheFile.map(_.toPath),
                      shouldParseOnly: Boolean = ConfigDefaults.DefaultParseOnly,
                      stopAfterEncoding: Boolean = ConfigDefaults.DefaultStopAfterEncoding,
                      checkOverflows: Boolean = ConfigDefaults.DefaultCheckOverflows,
                      checkConsistency: Boolean = ConfigDefaults.DefaultCheckConsistency,
                      int32bit: Boolean = ConfigDefaults.DefaultInt32bit,
                      cacheParserAndTypeChecker: Boolean = ConfigDefaults.DefaultCacheParserAndTypeChecker,
                      onlyFilesWithHeader: Boolean = ConfigDefaults.DefaultOnlyFilesWithHeader,
                      assumeInjectivityOnInhale: Boolean = ConfigDefaults.DefaultAssumeInjectivityOnInhale,
                      parallelizeBranches: Boolean = ConfigDefaults.DefaultParallelizeBranches,
                      conditionalizePermissions: Boolean = ConfigDefaults.DefaultConditionalizePermissions,
                      z3APIMode: Boolean = ConfigDefaults.DefaultZ3APIMode,
                      disableNL: Boolean = ConfigDefaults.DefaultDisableNL,
                      mceMode: MCE.Mode = ConfigDefaults.DefaultMCEMode,
                      hyperMode: Option[Hyper.Mode] = None,
                      noVerify: Boolean = ConfigDefaults.DefaultNoVerify,
                      noStreamErrors: Boolean = ConfigDefaults.DefaultNoStreamErrors,
                      parseAndTypeCheckMode: TaskManagerMode = ConfigDefaults.DefaultParseAndTypeCheckMode,
                      requireTriggers: Boolean = ConfigDefaults.DefaultRequireTriggers,
                      disableSetAxiomatization: Boolean = ConfigDefaults.DefaultDisableSetAxiomatization,
                      disableCheckTerminationPureFns: Boolean = ConfigDefaults.DefaultDisableCheckTerminationPureFns,
                      unsafeWildcardOptimization: Boolean = ConfigDefaults.DefaultUnsafeWildcardOptimization,
                      moreJoins: MoreJoins.Mode = ConfigDefaults.DefaultMoreJoins,
                      respectFunctionPrePermAmounts: Boolean = ConfigDefaults.DefaultRespectFunctionPrePermAmounts,
                      enableExperimentalFriendClauses: Boolean = ConfigDefaults.DefaultEnableExperimentalFriendClauses,
                      disableInfeasibilityChecks: Boolean = ConfigDefaults.DefaultDisableInfeasibilityChecks,
                      enableDependencyAnalysis: Boolean = ConfigDefaults.DefaultEnableDependencyAnalysis,
                      startDependencyAnalysisTool: Boolean = ConfigDefaults.DefaultStartDependencyAnalysisTool,
                     ) {
  def shouldParse: Boolean = true
  def shouldTypeCheck: Boolean = !shouldParseOnly
  def shouldDesugar: Boolean = shouldTypeCheck
  def shouldViperEncode: Boolean = shouldDesugar
  def shouldVerify: Boolean = shouldViperEncode && !stopAfterEncoding
  def shouldChop: Boolean = choppingUpperBound > 1 || isolated.exists(_.nonEmpty)
  lazy val isolated: Option[Vector[SourcePosition]] = {
    val positions = isolate.flatMap{ case (path, idxs) => idxs.map(idx => SourcePosition(path, idx, 0)) }
    // if there are zero positions, no member should be isolated as verifying the empty program is uninteresting.
    positions match {
      case Nil => None
      case _ => Some(positions.toVector)
    }
  }
}

trait RawConfig {
  /** converts a RawConfig to an actual `Config` for Gobra. Returns Left if validation fails. */
  def config: Either[Vector[VerifierError], Config]
  protected def baseConfig: BaseConfig

  protected def createConfig(packageInfoInputMap: Map[PackageInfo, Vector[Source]]): Config = Config(
    gobraDirectory = baseConfig.gobraDirectory,
    packageInfoInputMap = packageInfoInputMap,
    moduleName = baseConfig.moduleName,
    includeDirs = baseConfig.includeDirs,
    reporter = baseConfig.reporter,
    backend = baseConfig.backend,
    isolate = baseConfig.isolated,
    choppingUpperBound = baseConfig.choppingUpperBound,
    packageTimeout = baseConfig.packageTimeout,
    z3Exe = baseConfig.z3Exe,
    boogieExe = baseConfig.boogieExe,
    logLevel = baseConfig.logLevel,
    cacheFile = baseConfig.cacheFile,
    shouldParse = baseConfig.shouldParse,
    shouldTypeCheck = baseConfig.shouldTypeCheck,
    shouldDesugar = baseConfig.shouldDesugar,
    shouldViperEncode = baseConfig.shouldViperEncode,
    checkOverflows = baseConfig.checkOverflows,
    checkConsistency = baseConfig.checkConsistency,
    shouldVerify = baseConfig.shouldVerify,
    shouldChop = baseConfig.shouldChop,
    int32bit = baseConfig.int32bit,
    cacheParserAndTypeChecker = baseConfig.cacheParserAndTypeChecker,
    onlyFilesWithHeader = baseConfig.onlyFilesWithHeader,
    assumeInjectivityOnInhale = baseConfig.assumeInjectivityOnInhale,
    parallelizeBranches = baseConfig.parallelizeBranches,
    conditionalizePermissions = baseConfig.conditionalizePermissions,
    z3APIMode = baseConfig.z3APIMode,
    disableNL = baseConfig.disableNL,
    mceMode = baseConfig.mceMode,
    hyperMode = baseConfig.hyperMode,
    noVerify = baseConfig.noVerify,
    noStreamErrors = baseConfig.noStreamErrors,
    parseAndTypeCheckMode = baseConfig.parseAndTypeCheckMode,
    requireTriggers = baseConfig.requireTriggers,
    disableSetAxiomatization = baseConfig.disableSetAxiomatization,
    disableCheckTerminationPureFns = baseConfig.disableCheckTerminationPureFns,
    unsafeWildcardOptimization = baseConfig.unsafeWildcardOptimization,
    moreJoins = baseConfig.moreJoins,
    respectFunctionPrePermAmounts = baseConfig.respectFunctionPrePermAmounts,
    enableExperimentalFriendClauses = baseConfig.enableExperimentalFriendClauses,
    disableInfeasibilityChecks = baseConfig.disableInfeasibilityChecks,
    enableDependencyAnalysis = baseConfig.enableDependencyAnalysis,
    startDependencyAnalysisTool = baseConfig.startDependencyAnalysisTool,
  )
}

/**
  * Special case where we do not enforce that inputs (be it files, directories or recursive files) have to be provided.
  * This is for example used when parsing in-file configs.
  */
case class NoInputModeConfig(baseConfig: BaseConfig) extends RawConfig {
  override lazy val config: Either[Vector[VerifierError], Config] = Right(createConfig(Map.empty))
}

case class FileModeConfig(inputFiles: Vector[Path], baseConfig: BaseConfig) extends RawConfig {
  override lazy val config: Either[Vector[VerifierError], Config] = {
    val sources = inputFiles.map(path => FileSource(path.toString))
    if (sources.isEmpty) Left(Vector(ConfigError(s"no input files have been provided")))
    else {
      // we do not check whether the provided files all belong to the same package
      // instead, we trust the programmer that she knows what she's doing.
      // If they do not belong to the same package, Gobra will report an error after parsing.
      // we simply use the first source's package info to create a single map entry:
      for {
        pkgInfo <- getPackageInfo(sources.head, inputFiles.head)
        packageInfoInputMap = Map(pkgInfo -> sources)
      } yield createConfig(packageInfoInputMap)
    }
  }
}

trait PackageAndRecursiveModeConfig extends RawConfig {
  def getSources(directory: Path, recursive: Boolean, onlyFilesWithHeader: Boolean): Vector[Source] = {
    val inputResource = FileResource(directory)
    PackageResolver.getSourceFiles(inputResource, recursive = recursive, onlyFilesWithHeader = onlyFilesWithHeader).map { resource =>
      val source = resource.asSource()
      // we do not need the underlying resources anymore, thus close them:
      resource.close()
      source
    }
  }
}

case class PackageModeConfig(projectRoot: Path = ConfigDefaults.DefaultProjectRoot.toPath,
                             inputDirectories: Vector[Path], baseConfig: BaseConfig) extends PackageAndRecursiveModeConfig {
  override lazy val config: Either[Vector[VerifierError], Config] = {
    val (errors, mappings) = inputDirectories.map { directory =>
      val sources = getSources(directory, recursive = false, onlyFilesWithHeader = baseConfig.onlyFilesWithHeader)
      // we do not check whether the provided files all belong to the same package
      // instead, we trust the programmer that she knows what she's doing.
      // If they do not belong to the same package, Gobra will report an error after parsing.
      // we simply use the first source's package info to create a single map entry:
      if (sources.isEmpty) Left(Vector(ConfigError(s"no sources found in directory $directory")))
      else for {
        pkgInfo <- getPackageInfo(sources.head, projectRoot)
      } yield pkgInfo -> sources
    }.partitionMap(identity)
    if (errors.nonEmpty) {
        Left(errors.flatten)
    } else {
        Right(createConfig(mappings.toMap))
    }
  }
}

case class RecursiveModeConfig(projectRoot: Path = ConfigDefaults.DefaultProjectRoot.toPath,
                               includePackages: List[String] = ConfigDefaults.DefaultIncludePackages,
                               excludePackages: List[String] = ConfigDefaults.DefaultExcludePackages,
                               baseConfig: BaseConfig) extends PackageAndRecursiveModeConfig {
  override lazy val config: Either[Vector[VerifierError], Config] = {
    val sources = getSources(projectRoot, recursive = true, onlyFilesWithHeader = baseConfig.onlyFilesWithHeader)
    val (errors, pkgInfos) = sources.map(source => {
      for {
        pkgInfo <- getPackageInfo(source, projectRoot)
      } yield source -> pkgInfo
    }).partitionMap(identity)
    for {
      pkgInfos <- if (errors.nonEmpty) Left(errors.flatten) else Right(pkgInfos)
      pkgMap = pkgInfos
        .groupBy { case (_, pkgInfo) => pkgInfo }
        // we no longer need `pkgInfo` for the values:
        .transform { case (_, values) => values.map(_._1) }
        // filter packages:
        .filter { case (pkgInfo, _) => (includePackages.isEmpty || includePackages.contains(pkgInfo.name)) && !excludePackages.contains(pkgInfo.name) }
        // filter packages with zero source files:
        .filter { case (_, pkgFiles) => pkgFiles.nonEmpty }
      nonEmptyPkgMap <- if (pkgMap.isEmpty) Left(Vector(ConfigError(s"No packages have been found that should be verified"))) else Right(pkgMap)
    } yield createConfig(nonEmptyPkgMap)
  }
}

/**
  * This represents Gobra's CLI interface.
  * The idea is to just perform the necessary validations to convert the inputs into a `RawConfig`.
  * All other validations will be deferred and performed on `RawConfig` before converting it to the actual Gobra config.
  */
class ScallopGobraConfig(arguments: Seq[String], isInputOptional: Boolean = false, skipIncludeDirChecks: Boolean = false)
  extends ScallopConf(arguments)
    with StrictLogging {

  /**
    * Prologue
    */

  version(
    s"""
       | ${GoVerifier.name} ${GoVerifier.copyright}
       |   version ${GoVerifier.version}
     """.stripMargin
  )

  banner(
    s""" ${GoVerifier.name} supports three modes for specifying input files that should be verified:
       |  ${GoVerifier.name} -i <input-files> [OPTIONS] OR
       |  ${GoVerifier.name} -p <directories> <optional project root> [OPTIONS] OR
       |  ${GoVerifier.name} -r <optional project root> <optional include and exclude package names> [OPTIONS]
       |
       | Mode 1 (-i):
       |  The first mode takes a list of files that must belong to the same package.
       |  Files belonging to the same package but missing in the list are not considered for type-checking and verification.
       |  Optionally, positional information can be provided for each file, e.g. <path to file>@42,111, such that only
       |  members at these positions will be verified.
       |
       | Mode 2 (-p):
       |  ${GoVerifier.name} verifies all `.${PackageResolver.gobraExtension}` and `.${PackageResolver.goExtension}` files in the provided directories,
       |  while treating files in the same directory as belonging to the same package.
       |  Verifies these packages. The project root (by default the current working directory) is used to derive a
       |  unique package identifier, since package names might not be unique.
       |
       | Mode 3 (-r):
       |  Transitively locates source files in subdirectories relative to the project root (by default the current
       |  working directory) and groups them to packages based on the relative path and package name.
       |  --includePackages <package names> and --excludePackages <package names> can be used to allow-
       |  or block-list packages.
       |
       | Note that --include <directories> is unrelated to the modes above and controls how ${GoVerifier.name} resolves
       | package imports.
       |
       |
       | Options:
       |""".stripMargin
  )

  /**
    * Command-line options
    */
  /** input is a list of strings as opposed to a list of files because line positions can optionally be provided */
  val input: ScallopOption[List[String]] = opt[List[String]](
    name = "input",
    descr = "List of files to verify. Optionally, specific members can be verified by passing their line numbers (e.g. foo.gobra@42,111 corresponds to the members in lines 42 and 111)",
    short = 'i'
  )
  /**
    * List of input files together with their specified line numbers.
    * Specified line numbers are removed from their corresponding input argument.
    */
  val cutInputWithIdxs: ScallopOption[List[(File, List[Int])]] = input.map(_.map{ arg =>
    val pattern = """(.*)@(\d+(?:,\d+)*)""".r
    arg match {
      case pattern(prefix, idxs) =>
        (new File(prefix), idxs.split(',').toList.map(_.toInt))

      case _ => (new File(arg), List.empty[Int])
    }
  })
  /** list of input files without line numbers */
  val cutInput: ScallopOption[List[File]] = cutInputWithIdxs.map(_.map(_._1))

  val directory: ScallopOption[List[File]] = opt[List[File]](
    name = "directory",
    descr = "List of directories to verify",
    short = 'p'
  )

  val recursive: ScallopOption[Boolean] = opt[Boolean](
    name = "recursive",
    descr = "Verify nested packages recursively",
    short = 'r'
  )

  val projectRoot: ScallopOption[File] = opt[File](
    name = "projectRoot",
    descr = "The root directory of the project",
    default = Some(ConfigDefaults.DefaultProjectRoot),
    noshort = true
  )

  val inclPackages: ScallopOption[List[String]] = opt[List[String]](
    name = "includePackages",
    descr = "Packages to verify. All packages found in the specified directories are verified by default.",
    default = Some(ConfigDefaults.DefaultIncludePackages),
    noshort = true
  )

  val exclPackages: ScallopOption[List[String]] = opt[List[String]](
    name = "excludePackages",
    descr = "Packages to ignore. These packages will not be verified, even if they are found in the specified directories.",
    default = Some(ConfigDefaults.DefaultExcludePackages),
    noshort = true
  )

  val gobraDirectory: ScallopOption[Path] = opt[Path](
    name = "gobraDirectory",
    descr = "Output directory for Gobra",
    default = ConfigDefaults.DefaultGobraDirectory,
    short = 'g'
  )(singleArgConverter(arg => Path.of(arg)))

  val module: ScallopOption[String] = opt[String](
    name = "module",
    descr = "Name of current module that should be used for resolving imports",
    default = Some(ConfigDefaults.DefaultModuleName)
  )

  val include: ScallopOption[List[File]] = opt[List[File]](
    name = "include",
    short = 'I',
    descr = "Uses the provided directories to perform package-related lookups before falling back to $GOPATH",
    default = Some(ConfigDefaults.DefaultIncludeDirs)
  )
  lazy val includeDirs: Vector[Path] = include.toOption.map(_.map(_.toPath).toVector).getOrElse(Vector())

  val backend: ScallopOption[ViperBackend] = choice(
    choices = Seq("SILICON", "CARBON", "VSWITHSILICON", "VSWITHCARBON"),
    name = "backend",
    descr = "Specifies the used Viper backend. The default is SILICON.",
    default = None,
    noshort = true
  ).map{
    case "SILICON" => ViperBackends.SiliconBackend
    case "CARBON" => ViperBackends.CarbonBackend
    case "VSWITHSILICON" => ViperBackends.ViperServerWithSilicon()
    case "VSWITHCARBON" => ViperBackends.ViperServerWithCarbon()
    case s => Violation.violation(s"Unexpected backend option $s")
  }

  val debug: ScallopOption[Boolean] = opt[Boolean](
    name = "debug",
    descr = "Output additional debug information",
    default = Some(false)
  )

  val logLevel: ScallopOption[Level] = choice(
    name = "logLevel",
    choices = Seq("ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "OFF"),
    descr = "Specifies the log level. The default is OFF.",
    default = Some(if (debug()) Level.DEBUG.toString else ConfigDefaults.DefaultLogLevel.toString),
    noshort = true
  ).map{arg => Level.toLevel(arg)}

  val eraseGhost: ScallopOption[Boolean] = opt[Boolean](
    name = "eraseGhost",
    descr = "Print the input program without ghost code",
    default = Some(false),
    noshort = true
  )

  val goify: ScallopOption[Boolean] = opt[Boolean](
    name = "goify",
    descr = "Print the input program with the ghost code commented out",
    default = Some(false),
    noshort = true
  )

  val unparse: ScallopOption[Boolean] = opt[Boolean](
    name = "unparse",
    descr = "Print the parsed program",
    default = Some(debug()),
    noshort = true
  )

  val printInternal: ScallopOption[Boolean] = opt[Boolean](
    name = "printInternal",
    descr = "Print the internal program representation",
    default = Some(debug()),
    noshort = true
  )

  val printVpr: ScallopOption[Boolean] = opt[Boolean](
    name = "printVpr",
    descr = "Print the encoded Viper program",
    default = Some(debug()),
    noshort = true
  )

  val parseOnly: ScallopOption[Boolean] = opt[Boolean](
    name = "parseOnly",
    descr = "Perform only the parsing step",
    default = Some(ConfigDefaults.DefaultParseOnly),
    noshort = true
  )

  val chopUpperBound: ScallopOption[Int] = opt[Int](
    name = "chop",
    descr = "Number of parts the generated verification condition is split into (at most)",
    default = Some(ConfigDefaults.DefaultChoppingUpperBound),
    noshort = true
  )

  val packageTimeout: ScallopOption[String] = opt[String](
    name = "packageTimeout",
    descr = "Duration till the verification of a package times out",
    default = None,
    noshort = true
  )

  lazy val packageTimeoutDuration: Duration = packageTimeout.toOption match {
    case Some(d) => Duration(d)
    case _ => Duration.Inf
  }

  val z3Exe: ScallopOption[String] = opt[String](
    name = "z3Exe",
    descr = "The Z3 executable",
    default = ConfigDefaults.DefaultZ3Exe,
    noshort = true
  )

  val boogieExe: ScallopOption[String] = opt[String](
    name = "boogieExe",
    descr = "The Boogie executable",
    default = ConfigDefaults.DefaultBoogieExe,
    noshort = true
  )

  val checkOverflows: ScallopOption[Boolean] = opt[Boolean](
    name = "overflow",
    descr = "Find expressions that may lead to integer overflow",
    default = Some(ConfigDefaults.DefaultCheckOverflows),
    noshort = false
  )

  val cacheFile: ScallopOption[File] = opt[File](
    name = "cacheFile",
    descr = "Cache file to be used by Viper Server",
    default = ConfigDefaults.DefaultCacheFile,
    noshort = true
  )

  val int32Bit: ScallopOption[Boolean] = opt[Boolean](
    name = "int32",
    descr = "Run with 32-bit sized integers (the default is 64-bit ints)",
    default = Some(ConfigDefaults.DefaultInt32bit),
    noshort = false
  )

  val onlyFilesWithHeader: ScallopOption[Boolean] = opt[Boolean](
    name = "onlyFilesWithHeader",
    descr = s"When enabled, Gobra only looks at files that contain the header comment '${Config.prettyPrintedHeader}'",
    default = Some(ConfigDefaults.DefaultOnlyFilesWithHeader),
    noshort = false
  )

  val checkConsistency: ScallopOption[Boolean] = opt[Boolean](
    name = "checkConsistency",
    descr = "Perform consistency checks on the generated Viper code",
    default = Some(ConfigDefaults.DefaultCheckConsistency),
    noshort = true
  )

  val assumeInjectivityOnInhale: ScallopOption[Boolean] = toggle(
    name = "assumeInjectivityOnInhale",
    descrYes = "Assumes injectivity of the receiver expression when inhaling quantified permissions, instead of checking it, like in Viper versions previous to 2022.02 (default)",
    descrNo = "Does not assume injectivity on inhales (this will become the default in future versions)",
    default = Some(ConfigDefaults.DefaultAssumeInjectivityOnInhale),
    noshort = true
  )

  val parallelizeBranches: ScallopOption[Boolean] = opt[Boolean](
    name = "parallelizeBranches",
    descr = "Performs parallel branch verification if the chosen backend is either SILICON or VSWITHSILICON",
    default = Some(ConfigDefaults.DefaultParallelizeBranches),
    noshort = true,
  )

  val conditionalizePermissions: ScallopOption[Boolean] = opt[Boolean](
    name = "conditionalizePermissions",
    descr = "Experimental: if enabled, and if the chosen backend is either SILICON or VSWITHSILICON, silicon will try " +
      "to reduce the number of symbolic execution paths by conditionalising permission expressions. " +
      "E.g. \"b ==> acc(x.f, p)\" is rewritten to \"acc(x.f, b ? p : none)\".",
    default = Some(ConfigDefaults.DefaultConditionalizePermissions),
    short = 'c',
  )

  val z3APIMode: ScallopOption[Boolean] = opt[Boolean](
    name = "z3APIMode",
    descr = "When the backend is either SILICON or VSWITHSILICON, silicon will use Z3 via API.",
    default = Some(ConfigDefaults.DefaultZ3APIMode),
    noshort = true,
  )

  val disableNL: ScallopOption[Boolean] = opt[Boolean](
    name = "disableNL",
    descr = "Disable non-linear integer arithmetics. Non compatible with Carbon",
    default = Some(ConfigDefaults.DefaultDisableNL),
    noshort = true,
  )

  val unsafeWildcardOptimization: ScallopOption[Boolean] = opt[Boolean]("unsafeWildcardOptimization",
    descr = "Simplify wildcard terms in a way that might be unsafe. Only use this if you know what you are doing! See Silicon PR #756 for details.",
    default = Some(false),
    noshort = true
  )

  val moreJoins: ScallopOption[MoreJoins.Mode] = {
    val all = "all"
    val impure = "impure"
    val off = "off"
    choice(
      choices = Seq("all", "impure", "off"),
      name = "moreJoins",
      descr = s"Specifies if silicon should be run with more joins completely enabled ($all), disabled ($off), or only for impure conditionals ($impure).",
      default = Some(off),
      noshort = true
    ).map {
      case `all` => MoreJoins.All
      case `off` => MoreJoins.Disabled
      case `impure` => MoreJoins.Impure
      case s => Violation.violation(s"Unexpected mode for moreJoins: $s")
    }
  }

  val mceMode: ScallopOption[MCE.Mode] = {
    val on = "on"
    val off = "off"
    val od = "od"
    choice(
      choices = Seq("on", "off", "od"),
      name = "mceMode",
      descr = s"Specifies if silicon should be run with more complete exhale enabled ($on), disabled ($off), or enabled on demand ($od).",
      default = Some(on),
      noshort = true
    ).map{
      case `on` => MCE.Enabled
      case `off` => MCE.Disabled
      case `od` => MCE.OnDemand
      case s => Violation.violation(s"Unexpected mode for more complete exhale: $s")
    }
  }

  val respectFunctionPrePermAmounts: ScallopOption[Boolean] = toggle(
    name = "respectFunctionPrePermAmounts",
    descrYes = s"Respects precise permission amounts in pure function preconditions instead of only checking read access, as done in older versions of Gobra." +
      "This option should be used for verifying legacy projects written with the old interpretation of fractional permissions." +
      "New projects are encouraged to not use this option.",
    descrNo = s"Use the default interpretation for fractional permissions in pure function preconditions.",
    default = Some(ConfigDefaults.DefaultRespectFunctionPrePermAmounts),
    noshort = true,
  )

  val hyperMode: ScallopOption[Hyper.Mode] = choice(
    name = "hyperMode",
    choices = Seq("on", "extended", "off", "noMajor"),
    descr = "Specifies whether hyper properties should be verified while enforcing low control flow (on), with support for non-low control flow (extended), not verified (off), or whether the major checks should be skipped (noMajor).",
    default = None,
    noshort = true
  ).map {
    case "on" => Hyper.Enabled
    case "extended" => Hyper.EnabledExtended
    case "off" => Hyper.Disabled
    case "noMajor" => Hyper.NoMajor
  }

  val enableLazyImports: ScallopOption[Boolean] = opt[Boolean](
    name = Config.enableLazyImportOptionName,
    descr = s"Enforces that ${GoVerifier.name} parses depending packages only when necessary. Note that this disables certain language features such as global variables.",
    default = Some(ConfigDefaults.DefaultEnableLazyImports),
    noshort = true,
  )

  val requireTriggers: ScallopOption[Boolean] = opt[Boolean](
    name = "requireTriggers",
    descr = s"Enforces that all quantifiers have a user-provided trigger.",
    default = Some(ConfigDefaults.DefaultRequireTriggers),
    noshort = true,
  )

  val noVerify: ScallopOption[Boolean] = opt[Boolean](
    name = "noVerify",
    descr = s"Skip the verification step performed after encoding the ${GoVerifier.name} program into Viper.",
    default = Some(ConfigDefaults.DefaultNoVerify),
    noshort = true,
  )

  val noStreamErrors: ScallopOption[Boolean] = opt[Boolean](
    name = "noStreamErrors",
    descr = s"Do not stream errors produced by ${GoVerifier.name} but instead print them all organized by package in the end.",
    default = Some(ConfigDefaults.DefaultNoStreamErrors),
    noshort = true,
  )

  val disableCheckTerminationPureFns: ScallopOption[Boolean] = opt[Boolean](
    name = "disablePureFunctsTerminationRequirement",
    descr = "Do not enforce that all pure functions must have termination measures",
    default = Some(ConfigDefaults.DefaultDisableCheckTerminationPureFns),
    noshort = true,
  )

  val parseAndTypeCheckMode: ScallopOption[TaskManagerMode] = choice(
    name = "parseAndTypeCheckMode",
    choices = Seq("LAZY", "SEQUENTIAL", "PARALLEL"),
    descr = "Specifies the mode in which parsing and type-checking is performed.",
    default = Some("PARALLEL"),
    noshort = true
  ).map {
    case "LAZY" => Lazy
    case "SEQUENTIAL" => Sequential
    case "PARALLEL" => Parallel
    case _ => ConfigDefaults.DefaultParseAndTypeCheckMode
  }

  val disableSetAxiomatization: ScallopOption[Boolean] = opt[Boolean](
    name = "disableSetAxiomatization",
    descr = s"Disables set axiomatization in Silicon.",
    default = Some(ConfigDefaults.DefaultDisableSetAxiomatization),
    noshort = true,
  )

  val enableExperimentalFriendClauses: ScallopOption[Boolean] = opt[Boolean](
    name = "experimentalFriendClauses",
    descr = s"Enables the use of 'friendPkg' clauses (experimental).",
    default = Some(ConfigDefaults.DefaultEnableExperimentalFriendClauses),
    noshort = true,
  )

  val disableInfeasibilityChecks: ScallopOption[Boolean] = opt[Boolean](
    name = "disableInfeasibilityChecks",
    descr = "Disable infeasibility checks. As a consequence all paths will be explored to the end. (Potentially) huge performance overhead!",
    default = Some(ConfigDefaults.DefaultDisableInfeasibilityChecks),
    noshort = true
  )

  val enableDependencyAnalysis: ScallopOption[Boolean] = opt[Boolean](
    name = "enableDependencyAnalysis",
    descr = "Enable dependency analysis mode",
    default = Some(ConfigDefaults.DefaultEnableDependencyAnalysis),
    noshort = true
  )

  val startDependencyAnalysisTool: ScallopOption[Boolean] = opt[Boolean](
    name = "startDependencyAnalysisTool",
    descr = "Starts the dependency analysis command line tool after verification",
    default = Some(ConfigDefaults.DefaultStartDependencyAnalysisTool),
    noshort = true
  )

  /**
    * Exception handling
    */
  /**
    * Epilogue
    */

  /** Argument Dependencies */
  if (isInputOptional) {
    mutuallyExclusive(input, directory, recursive)
  } else {
    // either `input`, `directory` or `recursive` must be provided but not both.
    // this also checks that at least one file or directory is provided in the case of `input` and `directory`.
    requireOne(input, directory, recursive)
  }

  // `inclPackages` and `exclPackages` only make sense when `recursive` is specified, `projectRoot` can only be used in `directory` or `recursive` mode.
  // Thus, we restrict their use:
  conflicts(input, List(projectRoot, inclPackages, exclPackages))
  conflicts(directory, List(inclPackages, exclPackages))

  // must be a function or lazy to guarantee that this value is computed only during the CLI options validation and not before.
  private def isSiliconBasedBackend = backend.toOption.getOrElse(ConfigDefaults.DefaultBackend) match {
    case ViperBackends.SiliconBackend | _: ViperBackends.ViperServerWithSilicon => true
    case _ => false
  }

  addValidation {
    val lazyImportsSet = enableLazyImports.toOption.contains(true)
    if (lazyImportsSet) {
      Left(s"The flag ${Config.enableLazyImportOptionPrettyPrinted} was removed in Gobra's PR #797.")
    } else {
      Right(())
    }
  }

  // `parallelizeBranches` requires a backend that supports branch parallelization (i.e., a silicon-based backend)
  addValidation {
    val parallelizeBranchesOn = parallelizeBranches.toOption.contains(true)
    if (parallelizeBranchesOn && !isSiliconBasedBackend) {
      Left("The selected backend does not support branch parallelization.")
    } else {
      Right(())
    }
  }

  addValidation {
    val conditionalizePermissionsOn = conditionalizePermissions.toOption.contains(true)
    if (conditionalizePermissionsOn && !isSiliconBasedBackend) {
      Left("The selected backend does not support --conditionalizePermissions.")
    } else {
      Right(())
    }
  }

  addValidation {
    val z3APIModeOn = z3APIMode.toOption.contains(true)
    if (z3APIModeOn && !isSiliconBasedBackend) {
      Left("The selected backend does not support --z3APIMode.")
    } else {
      Right(())
    }
  }

  // `mceMode` can only be provided when using a silicon-based backend
  addValidation {
    val mceModeSupplied = mceMode.isSupplied
    if (mceModeSupplied && !isSiliconBasedBackend) {
      Left("The flag --mceMode can only be used with Silicon or ViperServer with Silicon")
    } else {
      Right(())
    }
  }

  addValidation {
    val unsafeWildcardOptSupplied = unsafeWildcardOptimization.isSupplied
    if (unsafeWildcardOptSupplied  && !isSiliconBasedBackend) {
      Left("The flag --unsafeWildcardOptimization can only be used with Silicon or ViperServer with Silicon")
    } else {
      Right(())
    }
  }

  addValidation {
    val moreJoinsOptSupplied = moreJoins.isSupplied
    if (moreJoinsOptSupplied  && !isSiliconBasedBackend) {
      Left("The flag --moreJoins can only be used with Silicon or ViperServer with Silicon")
    } else {
      Right(())
    }
  }

  // `disableSetAxiomatization` can only be provided when using a silicon-based backend
  // since, at the time of writing, we rely on Silicon's setAxiomatizationFile for the
  // implementation
  addValidation {
    val disableSetAxiomatizationOn = disableSetAxiomatization.toOption.contains(true)
    if (disableSetAxiomatizationOn && !isSiliconBasedBackend) {
      Left("The selected backend does not support --disableSetAxiomatization.")
    } else {
      Right(())
    }
  }

  addValidation {
    if (!disableNL.toOption.contains(true) || isSiliconBasedBackend) {
      Right(())
    } else {
      Left("--disableNL is not compatible with Carbon")
    }
  }


  /** File Validation */
  validateFilesExist(cutInput)
  validateFilesIsFile(cutInput)
  validateFilesExist(directory)
  validateFilesIsDirectory(directory)
  validateFileExists(projectRoot)
  validateFileIsDirectory(projectRoot)
  if (!skipIncludeDirChecks) {
    validateFilesExist(include)
    validateFilesIsDirectory(include)
  }

  verify()

  lazy val config: Either[Vector[VerifierError], Config] = rawConfig.config

  // note that we use `recursive.isSupplied` instead of `recursive.toOption` because it defaults to `Some(false)` if it
  // was not provided by the user. Specifying a different default value does not seem to be respected.
  private lazy val rawConfig: RawConfig = (cutInputWithIdxs.toOption, directory.toOption, recursive.isSupplied) match {
    case (Some(inputsWithIdxs), None, false) => fileModeConfig(inputsWithIdxs)
    case (None, Some(dirs), false) => packageModeConfig(dirs)
    case (None, None, true) => recursiveModeConfig()
    case (None, None, false) =>
      Violation.violation(isInputOptional, "the configuration mode should be one of file, package or recursive unless inputs are optional")
      noInputModeConfig()
    case _ => Violation.violation(s"multiple modes have been found, which should have been caught by input validation")
  }

  private def fileModeConfig(cutInputWithIdxs: List[(File, List[Int])]): FileModeConfig = {
    val cutPathsWithIdxs = cutInputWithIdxs.map { case (file, lineNrs) => (file.toPath, lineNrs) }
    FileModeConfig(
      inputFiles = cutPathsWithIdxs.map(_._1).toVector,
      baseConfig = baseConfig(cutPathsWithIdxs)
    )
  }

  private def packageModeConfig(dirs: List[File]): PackageModeConfig = PackageModeConfig(
    inputDirectories = dirs.map(_.toPath).toVector,
    projectRoot = projectRoot().toPath,
    // we currently do not offer a way via CLI to pass isolate information to Gobra in the package mode
    baseConfig = baseConfig(ConfigDefaults.DefaultIsolate),
  )

  private def recursiveModeConfig(): RecursiveModeConfig = RecursiveModeConfig(
    projectRoot = projectRoot().toPath,
    includePackages = inclPackages(),
    excludePackages = exclPackages(),
    // we currently do not offer a way via CLI to pass isolate information to Gobra in the recursive mode
    baseConfig(ConfigDefaults.DefaultIsolate),
  )

  private def noInputModeConfig(): NoInputModeConfig = NoInputModeConfig(
    // we currently do not offer a way via CLI to pass isolate information to Gobra in the recursive mode
    baseConfig(ConfigDefaults.DefaultIsolate),
  )

  private def baseConfig(isolate: List[(Path, List[Int])]): BaseConfig = BaseConfig(
    gobraDirectory = gobraDirectory.toOption,
    moduleName = module(),
    includeDirs = includeDirs,
    reporter = FileWriterReporter(
        unparse = unparse(),
        eraseGhost = eraseGhost(),
        goify = goify(),
        debug = debug(),
        printInternal = printInternal(),
        printVpr = printVpr(),
        streamErrs = !noStreamErrors()),
    backend = backend.toOption,
    isolate = isolate,
    choppingUpperBound = chopUpperBound(),
    packageTimeout = packageTimeoutDuration,
    z3Exe = z3Exe.toOption,
    boogieExe = boogieExe.toOption,
    logLevel = logLevel(),
    cacheFile = cacheFile.toOption.map(_.toPath),
    shouldParseOnly = parseOnly(),
    checkOverflows = checkOverflows(),
    checkConsistency = checkConsistency(),
    int32bit = int32Bit(),
    cacheParserAndTypeChecker = false, // caching does not make sense when using the CLI. Thus, we simply set it to `false`
    onlyFilesWithHeader = onlyFilesWithHeader(),
    assumeInjectivityOnInhale = assumeInjectivityOnInhale(),
    parallelizeBranches = parallelizeBranches(),
    conditionalizePermissions = conditionalizePermissions(),
    z3APIMode = z3APIMode(),
    disableNL = disableNL(),
    mceMode = mceMode(),
    hyperMode = hyperMode.toOption,
    noVerify = noVerify(),
    noStreamErrors = noStreamErrors(),
    parseAndTypeCheckMode = parseAndTypeCheckMode(),
    requireTriggers = requireTriggers(),
    disableSetAxiomatization = disableSetAxiomatization(),
    disableCheckTerminationPureFns = disableCheckTerminationPureFns(),
    unsafeWildcardOptimization = unsafeWildcardOptimization(),
    moreJoins = moreJoins(),
    respectFunctionPrePermAmounts = respectFunctionPrePermAmounts(),
    enableExperimentalFriendClauses = enableExperimentalFriendClauses(),
    disableInfeasibilityChecks = disableInfeasibilityChecks(),
    enableDependencyAnalysis = enableDependencyAnalysis(),
    startDependencyAnalysisTool = startDependencyAnalysisTool(),
  )
}
