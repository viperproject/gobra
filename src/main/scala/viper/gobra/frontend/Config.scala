// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.File
import java.nio.file.{Path, Paths}
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
import scala.util.Right
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
}

object CliEnumConverter {
  trait EnumCase {
    def value: String
  }

  /** trait for an enum value to configure parsing and serialization of this enum */
  trait CliEnum[E <: EnumCase] {
    def values: List[E]
    def convert(s: String): E = values.find(_.value == s).getOrElse(Violation.violation(s"Unexpected value: $s"))
  }
}

// More-complete exhale modes
object MCE {
  sealed trait Mode extends CliEnumConverter.EnumCase
  object Disabled extends Mode {
    override val value: String = "off"
  }
  // When running in `OnDemand`, mce will only be enabled when silicon retries a query.
  // More information can be found in https://github.com/viperproject/silicon/pull/682.
  object OnDemand extends Mode {
    override val value: String = "od"
  }
  object Enabled extends Mode {
    override val value: String = "on"
  }
}
object MCEConverter extends CliEnumConverter.CliEnum[MCE.Mode] {
  import MCE._
  override def values: List[MCE.Mode] = List(Enabled, OnDemand, Disabled)
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
  sealed trait Mode extends CliEnumConverter.EnumCase {
    // Option number used by Viper, as described in
    // https://github.com/viperproject/silicon/pull/823
    def viperValue: Int
  }

  object Disabled extends Mode {
    override val value: String = "off"
    override val viperValue = 0
  }

  object Impure extends Mode {
    override val value: String = "impure"
    override val viperValue = 1
  }

  object All extends Mode {
    override val value: String = "all"
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
object MoreJoinsConverter extends CliEnumConverter.CliEnum[MoreJoins.Mode] {
  import MoreJoins._
  override def values: List[MoreJoins.Mode] = List(All, Impure, Disabled)
}

object ViperBackendConverter extends CliEnumConverter.CliEnum[ViperBackend] {
  import viper.gobra.backend.ViperBackends._
  override def values: List[ViperBackend] = List(
    SiliconBackend, CarbonBackend, ViperServerWithSilicon(), ViperServerWithCarbon())
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
                   checkOverflows: Option[Boolean] = None,
                   checkConsistency: Option[Boolean] = None,
                   shouldVerify: Boolean = true,
                   shouldChop: Boolean = ConfigDefaults.DefaultShouldChop,
                   // The go language specification states that int and uint variables can have either 32bit or 64, as long
                   // as they have the same size. This flag allows users to pick the size of int's and uints's: 32 if true,
                   // 64 bit otherwise.
                   int32bit: Option[Boolean] = None,
                   // the following option is currently not controllable via CLI as it is meaningless without a constantly
                   // running JVM. It is targeted in particular to Gobra Server and Gobra IDE
                   cacheParserAndTypeChecker: Boolean = ConfigDefaults.DefaultCacheParserAndTypeChecker,
                   // this option introduces a mode where Gobra only considers files with a specific annotation ("// +gobra").
                   // this is useful when verifying large packages where some files might use some unsupported feature of Gobra,
                   // or when the goal is to gradually verify part of a package without having to provide an explicit list of the files
                   // to verify.
                   onlyFilesWithHeader: Option[Boolean] = None,
                   // if enabled, Gobra assumes injectivity on inhale, as done by Viper versions before 2022.2.
                   assumeInjectivityOnInhale: Option[Boolean] = None,
                   // if enabled, and if the chosen backend is either SILICON or VSWITHSILICON,
                   // branches will be verified in parallel
                   parallelizeBranches: Option[Boolean] = None,
                   conditionalizePermissions: Option[Boolean] = None,
                   z3APIMode: Option[Boolean] = None,
                   disableNL: Option[Boolean] = None,
                   mceMode: MCE.Mode = ConfigDefaults.DefaultMCEMode,
                   // `None` indicates that no mode has been specified and instructs Gobra to use the default hyper mode
                   hyperMode: Option[Hyper.Mode] = None,
                   noVerify: Boolean = ConfigDefaults.DefaultNoVerify,
                   noStreamErrors: Boolean = ConfigDefaults.DefaultNoStreamErrors,
                   parseAndTypeCheckMode: TaskManagerMode = ConfigDefaults.DefaultParseAndTypeCheckMode,
                   // when enabled, all quantifiers without triggers are rejected
                   requireTriggers: Option[Boolean] = None,
                   disableSetAxiomatization: Option[Boolean] = None,
                   disableCheckTerminationPureFns: Option[Boolean] = None,
                   unsafeWildcardOptimization: Option[Boolean] = None,
                   moreJoins: MoreJoins.Mode = ConfigDefaults.DefaultMoreJoins,
                   respectFunctionPrePermAmounts: Boolean = ConfigDefaults.DefaultRespectFunctionPrePermAmounts,
                   enableExperimentalFriendClauses: Boolean = ConfigDefaults.DefaultEnableExperimentalFriendClauses,
) {

  /** Merges values from an InputConfig into this Config.
    * This Config takes precedence; InputConfig values are used as fallbacks.
    * Keeps packageInfoInputMap, reporter, and taskName from this Config.
    * Uses same merge semantics as the old merge method:
    * - includeDirs: concatenate and deduplicate
    * - backend/hyperMode: must match if both defined
    * - packageTimeout/logLevel: take minimum
    * - Boolean OR fields: use OR
    * - Others: this Config takes precedence */
  def applyInputConfig(input: InputConfig): Config = {
    copy(
      gobraDirectory = gobraDirectory orElse input.gobraDirectory.value,
      includeDirs = input.includeDirs.value.map(dirs => (includeDirs ++ dirs).distinct) getOrElse includeDirs,
      backend = (backend, input.backend.value) match {
        case (l, None) => l
        case (None, r) => r
        case (l, r) if l == r => l
        case (Some(l), Some(r)) => Violation.violation(s"Unable to merge differing backends from in-file configuration options, got $l and $r")
      },
      packageTimeout = input.packageTimeout.value.map(pt => if (packageTimeout < pt) packageTimeout else pt) getOrElse packageTimeout,
      z3Exe = z3Exe orElse input.z3Exe.value,
      boogieExe = boogieExe orElse input.boogieExe.value,
      logLevel = input.logLevel.value.map(ll => if (logLevel.isGreaterOrEqual(ll)) ll else logLevel) getOrElse logLevel,
      // TODO merge strategy for following properties is unclear (maybe AND or OR)
      cacheFile = cacheFile orElse input.cacheFile.value,
      checkOverflows = checkOverflows orElse input.checkOverflows.value,
      int32bit = int32bit orElse input.int32bit.value,
      checkConsistency = checkConsistency orElse input.checkConsistency.value,
      cacheParserAndTypeChecker = cacheParserAndTypeChecker,
      onlyFilesWithHeader = onlyFilesWithHeader orElse input.onlyFilesWithHeader.value,
      assumeInjectivityOnInhale = assumeInjectivityOnInhale orElse input.assumeInjectivityOnInhale.value,
      parallelizeBranches = parallelizeBranches orElse input.parallelizeBranches.value,
      conditionalizePermissions = conditionalizePermissions orElse input.conditionalizePermissions.value,
      z3APIMode = z3APIMode orElse input.z3APIMode.value,
      disableNL = disableNL orElse input.disableNL.value,
      hyperMode = (hyperMode, input.hyperMode.value) match {
        case (l, None) => l
        case (None, r) => r
        case (l, r) if l == r => l
        case (Some(l), Some(r)) => Violation.violation(s"Unable to merge differing hyper modes from in-file configuration options, got $l and $r")
      },
      noVerify = noVerify || input.noVerify.value.contains(true),
      noStreamErrors = noStreamErrors || input.noStreamErrors.value.contains(true),
      requireTriggers = requireTriggers orElse input.requireTriggers.value,
      disableSetAxiomatization = disableSetAxiomatization orElse input.disableSetAxiomatization.value,
      disableCheckTerminationPureFns = disableCheckTerminationPureFns orElse input.disableCheckTerminationPureFns.value,
      unsafeWildcardOptimization = unsafeWildcardOptimization orElse input.unsafeWildcardOptimization.value,
      moreJoins = input.moreJoins.value.map(mj => MoreJoins.merge(moreJoins, mj)) getOrElse moreJoins,
      respectFunctionPrePermAmounts = respectFunctionPrePermAmounts || input.respectFunctionPrePermAmounts.value.contains(true),
      enableExperimentalFriendClauses = enableExperimentalFriendClauses || input.enableExperimentalFriendClauses.value.contains(true),
    )
  }

  val backendOrDefault: ViperBackend = backend getOrElse ConfigDefaults.DefaultBackend
  val hyperModeOrDefault: Hyper.Mode = hyperMode getOrElse ConfigDefaults.DefaultHyperMode
  val checkOverflowsOrDefault: Boolean = checkOverflows getOrElse ConfigDefaults.DefaultCheckOverflows
  val checkConsistencyOrDefault: Boolean = checkConsistency getOrElse ConfigDefaults.DefaultCheckConsistency
  val int32bitOrDefault: Boolean = int32bit getOrElse ConfigDefaults.DefaultInt32bit
  val onlyFilesWithHeaderOrDefault: Boolean = onlyFilesWithHeader getOrElse ConfigDefaults.DefaultOnlyFilesWithHeader
  val assumeInjectivityOnInhaleOrDefault: Boolean = assumeInjectivityOnInhale getOrElse ConfigDefaults.DefaultAssumeInjectivityOnInhale
  val parallelizeBranchesOrDefault: Boolean = parallelizeBranches getOrElse ConfigDefaults.DefaultParallelizeBranches
  val conditionalizePermissionsOrDefault: Boolean = conditionalizePermissions getOrElse ConfigDefaults.DefaultConditionalizePermissions
  val z3APIModeOrDefault: Boolean = z3APIMode getOrElse ConfigDefaults.DefaultZ3APIMode
  val disableNLOrDefault: Boolean = disableNL getOrElse ConfigDefaults.DefaultDisableNL
  val requireTriggersOrDefault: Boolean = requireTriggers getOrElse ConfigDefaults.DefaultRequireTriggers
  val disableSetAxiomatizationOrDefault: Boolean = disableSetAxiomatization getOrElse ConfigDefaults.DefaultDisableSetAxiomatization
  val disableCheckTerminationPureFnsOrDefault: Boolean = disableCheckTerminationPureFns getOrElse ConfigDefaults.DefaultDisableCheckTerminationPureFns
  val unsafeWildcardOptimizationOrDefault: Boolean = unsafeWildcardOptimization getOrElse ConfigDefaults.DefaultUnsafeWildcardOptimization

  lazy val typeBounds: TypeBounds =
    if (int32bitOrDefault) {
      TypeBounds()
    } else {
      TypeBounds(Int = TypeBounds.IntWith64Bit, UInt = TypeBounds.UIntWith64Bit)
    }

  /** Returns a human-readable summary of the resolved configuration. */
  def formatted: String = {
    val entries = Seq(
      "moduleName" -> moduleName,
      "projectRoot" -> projectRoot,
      "includeDirs" -> (if (includeDirs.isEmpty) "(none)" else includeDirs.mkString(", ")),
      "backend" -> backendOrDefault.value,
      "choppingUpperBound" -> choppingUpperBound,
      "packageTimeout" -> packageTimeout,
      "z3Exe" -> z3Exe.getOrElse("(default)"),
      "boogieExe" -> boogieExe.getOrElse("(default)"),
      "logLevel" -> logLevel,
      "cacheFile" -> cacheFile.map(_.toString).getOrElse("(none)"),
      "checkOverflows" -> checkOverflowsOrDefault,
      "checkConsistency" -> checkConsistencyOrDefault,
      "int32bit" -> int32bitOrDefault,
      "onlyFilesWithHeader" -> onlyFilesWithHeaderOrDefault,
      "gobraDirectory" -> gobraDirectory.map(_.toString).getOrElse("(none)"),
      "assumeInjectivityOnInhale" -> assumeInjectivityOnInhaleOrDefault,
      "parallelizeBranches" -> parallelizeBranchesOrDefault,
      "conditionalizePermissions" -> conditionalizePermissionsOrDefault,
      "z3APIMode" -> z3APIModeOrDefault,
      "disableNL" -> disableNLOrDefault,
      "mceMode" -> mceMode.value,
      "hyperMode" -> hyperModeOrDefault,
      "requireTriggers" -> requireTriggersOrDefault,
      "moreJoins" -> moreJoins.value,
      "disableSetAxiomatization" -> disableSetAxiomatizationOrDefault,
      "disableCheckTerminationPureFns" -> disableCheckTerminationPureFnsOrDefault,
      "unsafeWildcardOptimization" -> unsafeWildcardOptimizationOrDefault,
      "respectFunctionPrePermAmounts" -> respectFunctionPrePermAmounts,
      "enableExperimentalFriendClauses" -> enableExperimentalFriendClauses,
      "noVerify" -> noVerify,
      "noStreamErrors" -> noStreamErrors,
      "parseAndTypeCheckMode" -> parseAndTypeCheckMode,
    )
    val maxKeyLen = entries.map(_._1.length).max
    val lines = entries.map { case (k, v) => s"  ${k.padTo(maxKeyLen, ' ')}  $v" }
    val packagesStr = if (packageInfoInputMap.isEmpty) "  (none)"
    else packageInfoInputMap.map { case (pkg, srcs) =>
      s"  ${pkg.id} -> [${srcs.map(_.name).mkString(", ")}]"
    }.mkString("\n")
    (Seq("Gobra Configuration:") ++ lines ++ Seq("  packages:") ++ Seq(packagesStr)).mkString("\n")
  }
}

object Config {
  // the header signals that a file should be considered when running on "header-only" mode
  val header: Regex = """\/\/\s*\+gobra""".r
  val prettyPrintedHeader = "// +gobra"
  require(header.matches(prettyPrintedHeader))
  def sourceHasHeader(s: Source): Boolean = header.findFirstIn(s.content).nonEmpty

  val enableLazyImportOptionName = "enableLazyImport"
  val enableLazyImportOptionPrettyPrinted = s"--$enableLazyImportOptionName"

  /** Options that must not appear in the `other` field of a JSON config because their paths
    * cannot be correctly resolved (they are turned into Source objects before path resolution). */
  private val disallowedInOther: Set[String] = Set("--input", "-i", "--directory", "-p")

  /** Validates that `other` args do not contain disallowed options. */
  def validateOtherArgs(args: List[String]): Either[Vector[VerifierError], Unit] = {
    val found = args.filter(disallowedInOther.contains)
    if (found.nonEmpty)
      Left(Vector(ConfigError(
        s"The 'other' field in the JSON config must not contain ${found.mkString(", ")}. " +
          "Use the dedicated JSON fields (input_files, pkg_path) instead.")))
    else Right(())
  }
}

/**
 * BaseConfig holds configuration options that can be shared across different RawConfig modes.
 * Have a look at `Config` to see an inline description of some of these parameters.
 *
 * IMPORTANT: All fields that can be explicitly set by the user (via CLI or JSON config) should use
 * `Option[T]` rather than `T` with a default value. This allows us to distinguish between:
 *   - `None`: the user did not specify this option (in which case the default value should be used)
 *   - `Some(value)`: the user explicitly set this option
 *
 * This is critical for config layering (job config overriding module config) (when using `--config`)
 * because we need to know whether a field was explicitly set in the job config or should be inherited
 * from the module.
 *
 * Pattern: For a field `foo: Option[T]`, add a corresponding `fooOrDefault: T` accessor in `Config`
 * that provides the resolved value with defaults applied. Example:
 *   val fooOrDefault: T = foo.getOrElse(ConfigDefaults.DefaultFoo)
 */
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
                      checkOverflows: Option[Boolean] = None,
                      checkConsistency: Option[Boolean] = None,
                      int32bit: Option[Boolean] = None,
                      cacheParserAndTypeChecker: Boolean = ConfigDefaults.DefaultCacheParserAndTypeChecker,
                      onlyFilesWithHeader: Option[Boolean] = None,
                      assumeInjectivityOnInhale: Option[Boolean] = None,
                      parallelizeBranches: Option[Boolean] = None,
                      conditionalizePermissions: Option[Boolean] = None,
                      z3APIMode: Option[Boolean] = None,
                      disableNL: Option[Boolean] = None,
                      mceMode: MCE.Mode = ConfigDefaults.DefaultMCEMode,
                      hyperMode: Option[Hyper.Mode] = None,
                      noVerify: Boolean = ConfigDefaults.DefaultNoVerify,
                      noStreamErrors: Boolean = ConfigDefaults.DefaultNoStreamErrors,
                      parseAndTypeCheckMode: TaskManagerMode = ConfigDefaults.DefaultParseAndTypeCheckMode,
                      requireTriggers: Option[Boolean] = None,
                      disableSetAxiomatization: Option[Boolean] = None,
                      disableCheckTerminationPureFns: Option[Boolean] = None,
                      unsafeWildcardOptimization: Option[Boolean] = None,
                      moreJoins: MoreJoins.Mode = ConfigDefaults.DefaultMoreJoins,
                      respectFunctionPrePermAmounts: Boolean = ConfigDefaults.DefaultRespectFunctionPrePermAmounts,
                      enableExperimentalFriendClauses: Boolean = ConfigDefaults.DefaultEnableExperimentalFriendClauses,
                     ) {
  val onlyFilesWithHeaderOrDefault: Boolean = onlyFilesWithHeader getOrElse ConfigDefaults.DefaultOnlyFilesWithHeader

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

case class InputConfigOption[T](name: String, value: Option[T]) {
  /** Transforms the value if present, keeping the same name */
  def map[U](f: T => U): InputConfigOption[U] = InputConfigOption(name, value.map(f))

  /** Returns this option if it has a value, otherwise returns the other option */
  def orElse(other: InputConfigOption[T]): InputConfigOption[T] =
    if (value.isDefined) this else other
}

object InputConfigOption {
  /** Implicit class to extend ScallopOption with toInputConfigOption conversion */
  implicit class ScallopOptionExtension[T](opt: ScallopOption[T]) {
    /** Converts a ScallopOption to an InputConfigOption using the option's name and value.
      * Only includes the value if the option was explicitly supplied by the user,
      * not just set to a default value. */
    def toInputConfigOption: InputConfigOption[T] =
      InputConfigOption(opt.name, if (opt.isSupplied) opt.toOption else None)
  }
}

/**
 * InputConfig represents user-provided configuration options where all fields are optional.
 * This mirrors ScallopGobraConfig's fields but with InputConfigOption[T] for uniform merging
 * and meaningful error messages (option names are preserved).
 * Fields are ordered to match the CLI option definitions in ScallopGobraConfig.
 */
case class InputConfig(
  // Input mode fields
  input: InputConfigOption[List[String]] = InputConfigOption("input", None),
  cutInputWithIdxs: InputConfigOption[List[(File, List[Int])]] = InputConfigOption("cutInputWithIdxs", None),
  directory: InputConfigOption[List[File]] = InputConfigOption("directory", None),
  recursive: InputConfigOption[Boolean] = InputConfigOption("recursive", None),
  configFile: InputConfigOption[File] = InputConfigOption("configFile", None),
  printConfig: InputConfigOption[Boolean] = InputConfigOption("printConfig", None),
  projectRoot: InputConfigOption[File] = InputConfigOption("projectRoot", None),
  inclPackages: InputConfigOption[List[String]] = InputConfigOption("inclPackages", None),
  exclPackages: InputConfigOption[List[String]] = InputConfigOption("exclPackages", None),
  // Configuration options (in CLI definition order)
  gobraDirectory: InputConfigOption[Path] = InputConfigOption("gobraDirectory", None),
  moduleName: InputConfigOption[String] = InputConfigOption("moduleName", None),
  include: InputConfigOption[List[File]] = InputConfigOption("include", None),
  backend: InputConfigOption[ViperBackend] = InputConfigOption("backend", None),
  debug: InputConfigOption[Boolean] = InputConfigOption("debug", None),
  logLevel: InputConfigOption[Level] = InputConfigOption("logLevel", None),
  eraseGhost: InputConfigOption[Boolean] = InputConfigOption("eraseGhost", None),
  goify: InputConfigOption[Boolean] = InputConfigOption("goify", None),
  unparse: InputConfigOption[Boolean] = InputConfigOption("unparse", None),
  printInternal: InputConfigOption[Boolean] = InputConfigOption("printInternal", None),
  printVpr: InputConfigOption[Boolean] = InputConfigOption("printVpr", None),
  parseOnly: InputConfigOption[Boolean] = InputConfigOption("parseOnly", None),
  choppingUpperBound: InputConfigOption[Int] = InputConfigOption("choppingUpperBound", None),
  packageTimeout: InputConfigOption[Duration] = InputConfigOption("packageTimeout", None),
  z3Exe: InputConfigOption[String] = InputConfigOption("z3Exe", None),
  boogieExe: InputConfigOption[String] = InputConfigOption("boogieExe", None),
  checkOverflows: InputConfigOption[Boolean] = InputConfigOption("checkOverflows", None),
  cacheFile: InputConfigOption[Path] = InputConfigOption("cacheFile", None),
  int32bit: InputConfigOption[Boolean] = InputConfigOption("int32bit", None),
  onlyFilesWithHeader: InputConfigOption[Boolean] = InputConfigOption("onlyFilesWithHeader", None),
  checkConsistency: InputConfigOption[Boolean] = InputConfigOption("checkConsistency", None),
  assumeInjectivityOnInhale: InputConfigOption[Boolean] = InputConfigOption("assumeInjectivityOnInhale", None),
  parallelizeBranches: InputConfigOption[Boolean] = InputConfigOption("parallelizeBranches", None),
  conditionalizePermissions: InputConfigOption[Boolean] = InputConfigOption("conditionalizePermissions", None),
  z3APIMode: InputConfigOption[Boolean] = InputConfigOption("z3APIMode", None),
  disableNL: InputConfigOption[Boolean] = InputConfigOption("disableNL", None),
  unsafeWildcardOptimization: InputConfigOption[Boolean] = InputConfigOption("unsafeWildcardOptimization", None),
  moreJoins: InputConfigOption[MoreJoins.Mode] = InputConfigOption("moreJoins", None),
  mceMode: InputConfigOption[MCE.Mode] = InputConfigOption("mceMode", None),
  respectFunctionPrePermAmounts: InputConfigOption[Boolean] = InputConfigOption("respectFunctionPrePermAmounts", None),
  hyperMode: InputConfigOption[Hyper.Mode] = InputConfigOption("hyperMode", None),
  requireTriggers: InputConfigOption[Boolean] = InputConfigOption("requireTriggers", None),
  noVerify: InputConfigOption[Boolean] = InputConfigOption("noVerify", None),
  noStreamErrors: InputConfigOption[Boolean] = InputConfigOption("noStreamErrors", None),
  disableCheckTerminationPureFns: InputConfigOption[Boolean] = InputConfigOption("disableCheckTerminationPureFns", None),
  parseAndTypeCheckMode: InputConfigOption[TaskManagerMode] = InputConfigOption("parseAndTypeCheckMode", None),
  disableSetAxiomatization: InputConfigOption[Boolean] = InputConfigOption("disableSetAxiomatization", None),
  enableExperimentalFriendClauses: InputConfigOption[Boolean] = InputConfigOption("enableExperimentalFriendClauses", None),
) {
  /** Derived field: extracts just the files from cutInputWithIdxs */
  val cutInput: InputConfigOption[List[File]] = cutInputWithIdxs.map(_.map(_._1))

  /** Derived field: converts include directories to Vector[Path] */
  val includeDirs: InputConfigOption[Vector[Path]] = include.map(_.map(_.toPath).toVector)

  /** Derived field: converts package timeout string to Duration */
  val packageTimeoutDuration: InputConfigOption[Duration] = packageTimeout

  /** Combines this config with another, giving precedence to this config's values.
    * For each field, uses this config's value if defined, otherwise uses other's value. */
  def orElse(other: InputConfig): InputConfig = InputConfig(
    input = input orElse other.input,
    cutInputWithIdxs = cutInputWithIdxs orElse other.cutInputWithIdxs,
    directory = directory orElse other.directory,
    recursive = recursive orElse other.recursive,
    configFile = configFile orElse other.configFile,
    printConfig = printConfig orElse other.printConfig,
    projectRoot = projectRoot orElse other.projectRoot,
    inclPackages = inclPackages orElse other.inclPackages,
    exclPackages = exclPackages orElse other.exclPackages,
    gobraDirectory = gobraDirectory orElse other.gobraDirectory,
    moduleName = moduleName orElse other.moduleName,
    include = include orElse other.include,
    backend = backend orElse other.backend,
    debug = debug orElse other.debug,
    logLevel = logLevel orElse other.logLevel,
    eraseGhost = eraseGhost orElse other.eraseGhost,
    goify = goify orElse other.goify,
    unparse = unparse orElse other.unparse,
    printInternal = printInternal orElse other.printInternal,
    printVpr = printVpr orElse other.printVpr,
    parseOnly = parseOnly orElse other.parseOnly,
    choppingUpperBound = choppingUpperBound orElse other.choppingUpperBound,
    packageTimeout = packageTimeout orElse other.packageTimeout,
    z3Exe = z3Exe orElse other.z3Exe,
    boogieExe = boogieExe orElse other.boogieExe,
    checkOverflows = checkOverflows orElse other.checkOverflows,
    cacheFile = cacheFile orElse other.cacheFile,
    int32bit = int32bit orElse other.int32bit,
    onlyFilesWithHeader = onlyFilesWithHeader orElse other.onlyFilesWithHeader,
    checkConsistency = checkConsistency orElse other.checkConsistency,
    assumeInjectivityOnInhale = assumeInjectivityOnInhale orElse other.assumeInjectivityOnInhale,
    parallelizeBranches = parallelizeBranches orElse other.parallelizeBranches,
    conditionalizePermissions = conditionalizePermissions orElse other.conditionalizePermissions,
    z3APIMode = z3APIMode orElse other.z3APIMode,
    disableNL = disableNL orElse other.disableNL,
    unsafeWildcardOptimization = unsafeWildcardOptimization orElse other.unsafeWildcardOptimization,
    moreJoins = moreJoins orElse other.moreJoins,
    mceMode = mceMode orElse other.mceMode,
    respectFunctionPrePermAmounts = respectFunctionPrePermAmounts orElse other.respectFunctionPrePermAmounts,
    hyperMode = hyperMode orElse other.hyperMode,
    requireTriggers = requireTriggers orElse other.requireTriggers,
    noVerify = noVerify orElse other.noVerify,
    noStreamErrors = noStreamErrors orElse other.noStreamErrors,
    disableCheckTerminationPureFns = disableCheckTerminationPureFns orElse other.disableCheckTerminationPureFns,
    parseAndTypeCheckMode = parseAndTypeCheckMode orElse other.parseAndTypeCheckMode,
    disableSetAxiomatization = disableSetAxiomatization orElse other.disableSetAxiomatization,
    enableExperimentalFriendClauses = enableExperimentalFriendClauses orElse other.enableExperimentalFriendClauses,
  )

  /** Merges this config with another, combining values according to Config.merge semantics.
    * This config takes precedence for most fields, but some fields have special merge behavior:
    * - List fields (include, input, cutInputWithIdxs, directory): concatenate and deduplicate
    * - backend, hyperMode: must match if both defined
    * - packageTimeout: takes minimum
    * - logLevel: takes minimum (more verbose)
    * - noVerify, noStreamErrors, respectFunctionPrePermAmounts, enableExperimentalFriendClauses: OR
    * - moreJoins: uses MoreJoins.merge */
  def merge(other: InputConfig): InputConfig = {
    // Helper to merge list options by concatenating and deduplicating
    def mergeListOption[A](a: InputConfigOption[List[A]], b: InputConfigOption[List[A]]): InputConfigOption[List[A]] =
      InputConfigOption(a.name, (a.value, b.value) match {
        case (Some(l1), Some(l2)) => Some((l1 ++ l2).distinct)
        case (l, None) => l
        case (None, r) => r
      })

    // Helper to merge options that must match if both defined
    def mergeMustMatch[A](a: InputConfigOption[A], b: InputConfigOption[A], fieldName: String): InputConfigOption[A] =
      InputConfigOption(a.name, (a.value, b.value) match {
        case (l, None) => l
        case (None, r) => r
        case (l, r) if l == r => l
        case (Some(l), Some(r)) => Violation.violation(s"Unable to merge differing $fieldName from configuration options, got $l and $r")
      })

    // Helper to merge boolean options with OR
    def mergeOr(a: InputConfigOption[Boolean], b: InputConfigOption[Boolean]): InputConfigOption[Boolean] =
      InputConfigOption(a.name, (a.value, b.value) match {
        case (Some(true), _) | (_, Some(true)) => Some(true)
        case (Some(false), Some(false)) => Some(false)
        case (l, None) => l
        case (None, r) => r
      })

    InputConfig(
      input = mergeListOption(input, other.input),
      cutInputWithIdxs = InputConfigOption(cutInputWithIdxs.name, (cutInputWithIdxs.value, other.cutInputWithIdxs.value) match {
        case (Some(l1), Some(l2)) => Some((l1 ++ l2).distinct)
        case (l, None) => l
        case (None, r) => r
      }),
      directory = mergeListOption(directory, other.directory),
      recursive = recursive orElse other.recursive,
      configFile = configFile orElse other.configFile,
      printConfig = printConfig orElse other.printConfig,
      projectRoot = projectRoot orElse other.projectRoot,
      inclPackages = mergeListOption(inclPackages, other.inclPackages),
      exclPackages = mergeListOption(exclPackages, other.exclPackages),
      gobraDirectory = gobraDirectory orElse other.gobraDirectory,
      moduleName = moduleName orElse other.moduleName,
      include = mergeListOption(include, other.include),
      backend = mergeMustMatch(backend, other.backend, "backends"),
      debug = debug orElse other.debug,
      logLevel = InputConfigOption(logLevel.name, (logLevel.value, other.logLevel.value) match {
        case (Some(l), Some(r)) => Some(if (l.isGreaterOrEqual(r)) r else l) // take minimum (more verbose)
        case (l, None) => l
        case (None, r) => r
      }),
      eraseGhost = eraseGhost orElse other.eraseGhost,
      goify = goify orElse other.goify,
      unparse = unparse orElse other.unparse,
      printInternal = printInternal orElse other.printInternal,
      printVpr = printVpr orElse other.printVpr,
      parseOnly = parseOnly orElse other.parseOnly,
      choppingUpperBound = choppingUpperBound orElse other.choppingUpperBound,
      packageTimeout = InputConfigOption(packageTimeout.name, (packageTimeout.value, other.packageTimeout.value) match {
        case (Some(l), Some(r)) => Some(if (l < r) l else r) // take minimum
        case (l, None) => l
        case (None, r) => r
      }),
      z3Exe = z3Exe orElse other.z3Exe,
      boogieExe = boogieExe orElse other.boogieExe,
      checkOverflows = checkOverflows orElse other.checkOverflows,
      cacheFile = cacheFile orElse other.cacheFile,
      int32bit = int32bit orElse other.int32bit,
      onlyFilesWithHeader = onlyFilesWithHeader orElse other.onlyFilesWithHeader,
      checkConsistency = checkConsistency orElse other.checkConsistency,
      assumeInjectivityOnInhale = assumeInjectivityOnInhale orElse other.assumeInjectivityOnInhale,
      parallelizeBranches = parallelizeBranches orElse other.parallelizeBranches,
      conditionalizePermissions = conditionalizePermissions orElse other.conditionalizePermissions,
      z3APIMode = z3APIMode orElse other.z3APIMode,
      disableNL = disableNL orElse other.disableNL,
      unsafeWildcardOptimization = unsafeWildcardOptimization orElse other.unsafeWildcardOptimization,
      moreJoins = InputConfigOption(moreJoins.name, (moreJoins.value, other.moreJoins.value) match {
        case (Some(l), Some(r)) => Some(MoreJoins.merge(l, r))
        case (l, None) => l
        case (None, r) => r
      }),
      mceMode = mceMode orElse other.mceMode,
      respectFunctionPrePermAmounts = mergeOr(respectFunctionPrePermAmounts, other.respectFunctionPrePermAmounts),
      hyperMode = mergeMustMatch(hyperMode, other.hyperMode, "hyper modes"),
      requireTriggers = requireTriggers orElse other.requireTriggers,
      noVerify = mergeOr(noVerify, other.noVerify),
      noStreamErrors = mergeOr(noStreamErrors, other.noStreamErrors),
      disableCheckTerminationPureFns = disableCheckTerminationPureFns orElse other.disableCheckTerminationPureFns,
      parseAndTypeCheckMode = parseAndTypeCheckMode orElse other.parseAndTypeCheckMode,
      disableSetAxiomatization = disableSetAxiomatization orElse other.disableSetAxiomatization,
      enableExperimentalFriendClauses = mergeOr(enableExperimentalFriendClauses, other.enableExperimentalFriendClauses),
    )
  }

  /** Validates the configuration options.
    * Performs the same checks as ScallopGobraConfig's validation.
    * @param isInputOptional if true, input mode is optional (mutually exclusive); if false, exactly one is required
    * @param skipIncludeDirChecks if true, skips file existence checks for include directories
    * @return Right(()) if validation passes, Left with errors otherwise */
  def validate(isInputOptional: Boolean = false, skipIncludeDirChecks: Boolean = false): Either[Vector[VerifierError], Unit] = {
    // to check that no further config options get provided when using `configFile`, we have to use reflection to obtain
    // all ScallopOptions as Scallop does not seem to provide any built-in functionality to achieve the same:
    val allOptions = getClass.getDeclaredFields
      .map(_.get(this)) // get field value
      .collect { case o : InputConfigOption[_] => o } // filter by InputConfigOption


    val isSiliconBasedBackend = backend.value.getOrElse(ConfigDefaults.DefaultBackend) match {
      case ViperBackends.SiliconBackend | _: ViperBackends.ViperServerWithSilicon => true
      case _ => false
    }

    validateConditions(
      if (isInputOptional) {
        mutuallyExclusive(input, directory, recursive, configFile)
      } else {
        // either `input`, `directory` or `recursive` must be provided but not both.
        // this also checks that at least one file or directory is provided in the case of `input` and `directory`.
        requireOne(input, directory, recursive, configFile)
      },
      // `inclPackages` and `exclPackages` only make sense when `recursive` is specified, `projectRoot` can only be used in `directory` or `recursive` mode.
      // Thus, we restrict their use:
      conflicts(input, List(projectRoot, inclPackages, exclPackages)),
      conflicts(directory, List(inclPackages, exclPackages)),
      // `--configFile` cannot be used with any other options except `--printConfig`:
      conflicts(configFile, allOptions.toList.filterNot(o => o == configFile || o == printConfig)),
      if (parallelizeBranches.value.contains(true) && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The selected backend does not support branch parallelization.")))
      } else {
        Right(())
      },
      if (conditionalizePermissions.value.contains(true) && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The selected backend does not support --conditionalizePermissions.")))
      } else {
        Right(())
      },
      if (z3APIMode.value.contains(true) && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The selected backend does not support --z3APIMode.")))
      } else {
        Right(())
      },
      // `mceMode` can only be provided when using a silicon-based backend
      if (mceMode.value.isDefined && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The flag --mceMode can only be used with Silicon or ViperServer with Silicon.")))
      } else {
        Right(())
      },
      if (unsafeWildcardOptimization.value.isDefined && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The flag --unsafeWildcardOptimization can only be used with Silicon or ViperServer with Silicon.")))
      } else {
        Right(())
      },
      if (moreJoins.value.isDefined && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The flag --moreJoins can only be used with Silicon or ViperServer with Silicon.")))
      } else {
        Right(())
      },
      // `disableSetAxiomatization` can only be provided when using a silicon-based backend
      // since, at the time of writing, we rely on Silicon's setAxiomatizationFile for the
      // implementation
      if (disableSetAxiomatization.value.contains(true) && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("The selected backend does not support --disableSetAxiomatization.")))
      } else {
        Right(())
      },
      if (disableNL.value.contains(true) && !isSiliconBasedBackend) {
        Left(Vector(ConfigError("--disableNL can only be used with Silicon or ViperServer with Silicon.")))
      } else {
        Right(())
      },
      if (printConfig.value.contains(true) && configFile.value.isEmpty) {
        Left(Vector(ConfigError("--printConfig requires --config.")))
      } else {
        Right(())
      },

      // file validations
      validateFilesExist(cutInput),
      validateFilesIsFile(cutInput),
      validateFilesExist(directory),
      validateFilesIsDirectory(directory),
      validateFileExists(configFile), // either points to a directory containing a package or a config file
      validateFileExists(projectRoot),
      validateFileIsDirectory(projectRoot),
      if (!skipIncludeDirChecks) validateFilesExist(include) else Right(()),
      if (!skipIncludeDirChecks) validateFilesIsDirectory(include) else Right(())
    )
  }

  /** only one of them is non-none */
  private def mutuallyExclusive(options: InputConfigOption[_]*): Either[Vector[VerifierError], Unit] = {
    if (options.count(_.value.isDefined) > 1) {
      Left(Vector(
        ConfigError(s"There should be only one or zero of the following options: ${options.map(_.name).mkString(", ")}")))
    } else {
      Right(())
    }
  }

  /** exactly one of them is non-none */
  private def requireOne(options: InputConfigOption[_]*): Either[Vector[VerifierError], Unit] = {
    if (options.count(_.value.isDefined) != 1) {
      Left(Vector(
        ConfigError(s"There should be exactly one of the following options: ${options.map(_.name).mkString(", ")}")))
    } else {
      Right(())
    }
  }

  /** if `opt` is non-none then all in `list` must be none */
  private def conflicts(opt: InputConfigOption[_], list: List[InputConfigOption[_]]): Either[Vector[VerifierError], Unit] = {
    val conflictOpt = list.find(_.value.isDefined)
    conflictOpt match {
      case Some(conflict) if opt.value.isDefined => Left(Vector(ConfigError(
        s"Option '${opt.name}' conflicts with option '${conflict.name}'")))
      case _ => Right(())
    }
  }

  private def validateFileExists(option: InputConfigOption[File]): Either[Vector[VerifierError], Unit] = {
    option.value.fold[Either[Vector[VerifierError], Unit]](Right(())) { file =>
      if (!file.exists) {
        Left(Vector(ConfigError(s"File '$file' not found.")))
      } else {
        Right(())
      }
    }
  }

  private def validateFileIsDirectory(option: InputConfigOption[File]): Either[Vector[VerifierError], Unit] = {
    option.value.fold[Either[Vector[VerifierError], Unit]](Right(())) { file =>
      if (!file.isDirectory) {
        Left(Vector(ConfigError(s"File '$file' is not a directory.")))
      } else {
        Right(())
      }
    }
  }

  private def validateFilesExist(option: InputConfigOption[List[File]]): Either[Vector[VerifierError], Unit] = {
    option.value.map(files => {
      val problems = files.filterNot(_.exists)
      if (problems.nonEmpty) Left(Vector(ConfigError(s"File(s) ${problems.map(_.toString).mkString(", ")} not found.")))
      else Right(())
    }).getOrElse(Right(()))
  }

  private def validateFilesIsFile(option: InputConfigOption[List[File]]): Either[Vector[VerifierError], Unit] = {
    option.value.map(files => {
      val problems = files.filterNot(_.isFile)
      if (problems.nonEmpty) Left(Vector(ConfigError(s"File(s) ${problems.map(_.toString).mkString(", ")} is not a file.")))
      else Right(())
    }).getOrElse(Right(()))
  }

  private def validateFilesIsDirectory(option: InputConfigOption[List[File]]): Either[Vector[VerifierError], Unit] = {
    option.value.map(files => {
      val problems = files.filterNot(_.isDirectory)
      if (problems.nonEmpty) Left(Vector(ConfigError(s"File(s) ${problems.map(_.toString).mkString(", ")} is not a directory.")))
      else Right(())
    }).getOrElse(Right(()))
  }

  /** collects the errors produced by multiple conditions */
  private def validateConditions(conditions: Either[Vector[VerifierError], Unit]*): Either[Vector[VerifierError], Unit] = {
    conditions.foldLeft[Either[Vector[VerifierError], Unit]](Right(())) {
      case (Right(()), cur) => cur
      case (Left(prevErrs), Left(curErrs)) => Left(prevErrs ++ curErrs)
      case (prev, _) => prev
    }
  }

  /** Constructs a Config from this InputConfig.
    * First validates the configuration, then constructs the appropriate RawConfig.
    * @param isInputOptional if true, input mode is optional (mutually exclusive)
    * @param skipIncludeDirChecks if true, skips include directory validation
    */
  def config(isInputOptional: Boolean = false, skipIncludeDirChecks: Boolean = false): Either[Vector[VerifierError], Config] = {
    validate(isInputOptional, skipIncludeDirChecks).flatMap { _ =>
      rawConfig(isInputOptional).config
    }
  }

  private def rawConfig(isInputOptional: Boolean): RawConfig = {
    (configFile.value, cutInputWithIdxs.value, directory.value, recursive.value.contains(true)) match {
      case (Some(cf), _, _, _) => configFileModeConfig(cf)
      case (None, Some(inputsWithIdxs), None, false) => fileModeConfig(inputsWithIdxs)
      case (None, None, Some(dirs), false) => packageModeConfig(dirs)
      case (None, None, None, true) => recursiveModeConfig()
      case (None, None, None, false) =>
        Violation.violation(isInputOptional, "the configuration mode should be one of file, package, recursive or config unless inputs are optional")
        noInputModeConfig()
      case _ => Violation.violation(s"multiple modes have been found, which should have been caught by input validation")
    }
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
    projectRoot = projectRoot.value.getOrElse(ConfigDefaults.DefaultProjectRoot).toPath,
    // we currently do not offer a way via CLI to pass isolate information to Gobra in the package mode
    baseConfig = baseConfig(ConfigDefaults.DefaultIsolate),
  )

  private def recursiveModeConfig(): RecursiveModeConfig = RecursiveModeConfig(
    projectRoot = projectRoot.value.getOrElse(ConfigDefaults.DefaultProjectRoot).toPath,
    includePackages = inclPackages.value.getOrElse(ConfigDefaults.DefaultIncludePackages),
    excludePackages = exclPackages.value.getOrElse(ConfigDefaults.DefaultExcludePackages),
    // we currently do not offer a way via CLI to pass isolate information to Gobra in the recursive mode
    baseConfig(ConfigDefaults.DefaultIsolate),
  )

  private def configFileModeConfig(configFile: File): RawConfig =
    ConfigFileModeConfig(configFile)

  private def noInputModeConfig(): NoInputModeConfig = NoInputModeConfig(
    // we currently do not offer a way via CLI to pass isolate information to Gobra in the no-input mode
    baseConfig(ConfigDefaults.DefaultIsolate),
  )

  private def baseConfig(isolate: List[(Path, List[Int])]): BaseConfig = BaseConfig(
    gobraDirectory = gobraDirectory.value,
    moduleName = moduleName.value.getOrElse(ConfigDefaults.DefaultModuleName),
    includeDirs = includeDirs.value.getOrElse(ConfigDefaults.DefaultIncludeDirs.map(_.toPath).toVector),
    reporter = FileWriterReporter(
        unparse = unparse.value.getOrElse(false),
        eraseGhost = eraseGhost.value.getOrElse(false),
        goify = goify.value.getOrElse(false),
        debug = debug.value.getOrElse(false),
        printInternal = printInternal.value.getOrElse(false),
        printVpr = printVpr.value.getOrElse(false),
        streamErrs = !noStreamErrors.value.getOrElse(false)),
    backend = backend.value,
    isolate = isolate,
    choppingUpperBound = choppingUpperBound.value.getOrElse(ConfigDefaults.DefaultChoppingUpperBound),
    packageTimeout = packageTimeoutDuration.value.getOrElse(ConfigDefaults.DefaultPackageTimeout),
    z3Exe = z3Exe.value,
    boogieExe = boogieExe.value,
    logLevel = logLevel.value.getOrElse(ConfigDefaults.DefaultLogLevel),
    cacheFile = cacheFile.value,
    shouldParseOnly = parseOnly.value.getOrElse(ConfigDefaults.DefaultParseOnly),
    checkOverflows = checkOverflows.value,
    checkConsistency = checkConsistency.value,
    int32bit = int32bit.value,
    cacheParserAndTypeChecker = false, // caching does not make sense when using the CLI. Thus, we simply set it to `false`
    onlyFilesWithHeader = onlyFilesWithHeader.value,
    assumeInjectivityOnInhale = assumeInjectivityOnInhale.value,
    parallelizeBranches = parallelizeBranches.value,
    conditionalizePermissions = conditionalizePermissions.value,
    z3APIMode = z3APIMode.value,
    disableNL = disableNL.value,
    mceMode = mceMode.value.getOrElse(ConfigDefaults.DefaultMCEMode),
    hyperMode = hyperMode.value,
    noVerify = noVerify.value.getOrElse(false),
    noStreamErrors = noStreamErrors.value.getOrElse(false),
    parseAndTypeCheckMode = parseAndTypeCheckMode.value.getOrElse(ConfigDefaults.DefaultParseAndTypeCheckMode),
    requireTriggers = requireTriggers.value,
    disableSetAxiomatization = disableSetAxiomatization.value,
    disableCheckTerminationPureFns = disableCheckTerminationPureFns.value,
    unsafeWildcardOptimization = unsafeWildcardOptimization.value,
    moreJoins = moreJoins.value.getOrElse(ConfigDefaults.DefaultMoreJoins),
    respectFunctionPrePermAmounts = respectFunctionPrePermAmounts.value.getOrElse(false),
    enableExperimentalFriendClauses = enableExperimentalFriendClauses.value.getOrElse(false),
  )
}

object InputConfig {
  /** Creates an InputConfig from a VerificationJobCfg's structured fields.
    * @param cfg the verification job configuration
    * @param configDir the directory containing the config file (for resolving relative paths) */
  def fromVerificationJobCfg(cfg: VerificationJobCfg, configDir: Path): InputConfig = {
    val resolvedCfg = cfg.resolvePaths(configDir)
    InputConfig(
      input = InputConfigOption("input", resolvedCfg.input_files),
      recursive = InputConfigOption("recursive", resolvedCfg.recursive),
      projectRoot = InputConfigOption("projectRoot", resolvedCfg.project_root.map(p => new File(p))),
      moduleName = InputConfigOption("moduleName", resolvedCfg.module),
      include = InputConfigOption("include", resolvedCfg.includes.map(_.map(p => new File(p)))),
      backend = InputConfigOption("backend", resolvedCfg.backend.map(_.underlying)),
      printVpr = InputConfigOption("printVpr", resolvedCfg.print_vpr),
      choppingUpperBound = InputConfigOption("choppingUpperBound", resolvedCfg.chop),
      checkOverflows = InputConfigOption("checkOverflows", resolvedCfg.overflow),
      checkConsistency = InputConfigOption("checkConsistency", resolvedCfg.check_consistency),
      onlyFilesWithHeader = InputConfigOption("onlyFilesWithHeader", resolvedCfg.only_files_with_header),
      assumeInjectivityOnInhale = InputConfigOption("assumeInjectivityOnInhale", resolvedCfg.assume_injectivity_inhale),
      parallelizeBranches = InputConfigOption("parallelizeBranches", resolvedCfg.parallelize_branches),
      conditionalizePermissions = InputConfigOption("conditionalizePermissions", resolvedCfg.conditionalize_permissions),
      moreJoins = InputConfigOption("moreJoins", resolvedCfg.more_joins),
      mceMode = InputConfigOption("mceMode", resolvedCfg.mce_mode),
      requireTriggers = InputConfigOption("requireTriggers", resolvedCfg.require_triggers),
    )
  }

  /** Parses CLI arguments and returns an InputConfig.
    * @param args the CLI arguments to parse
    * @return Right(inputConfig) on success, Left(errors) if parsing fails */
  def fromOtherArgs(args: List[String]): Either[Vector[VerifierError], InputConfig] = {
    try {
      val scallopConfig = new ScallopGobraConfig(args, isInputOptional = true, skipIncludeDirChecks = true)
      Right(scallopConfig.toInputConfig)
    } catch {
      case e: Exception => Left(Vector(ConfigError(s"Failed to parse 'other' args: ${e.getMessage}")))
    }
  }

  /** Parses CLI arguments and returns an InputConfig with optional path resolution.
    * Used for parsing in-file configuration annotations.
    * @param args the CLI arguments to parse
    * @param resolveTo if provided, resolves relative paths against this directory
    * @return Right(inputConfig) on success, Left(errors) if parsing fails */
  def parseCliArgs(args: List[String], resolveTo: Option[Path]): Either[Vector[VerifierError], InputConfig] = {
    fromOtherArgs(args).map { inputConfig =>
      resolveTo match {
        case None => inputConfig
        case Some(p) =>
          val absBase = p.toAbsolutePath
          inputConfig.copy(
            include = inputConfig.include.map(_.map(d => new File(absBase.resolve(d.toPath.toString).toString))),
            projectRoot = inputConfig.projectRoot.map(pr => new File(absBase.resolve(pr.toPath.toString).toString)),
            cacheFile = inputConfig.cacheFile.map(cf => absBase.resolve(cf.toString)),
            z3Exe = inputConfig.z3Exe.map(z => absBase.resolve(z).toString),
            boogieExe = inputConfig.boogieExe.map(b => absBase.resolve(b).toString),
          )
      }
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
      val sources = getSources(directory, recursive = false, onlyFilesWithHeader = baseConfig.onlyFilesWithHeaderOrDefault)
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
    val sources = getSources(projectRoot, recursive = true, onlyFilesWithHeader = baseConfig.onlyFilesWithHeaderOrDefault)
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

case class ConfigFileModeConfig(configFile: File) extends RawConfig with StrictLogging {
  // configFile is in the directory of the package we want to verify.
  // In the same or some parent directory, there's an additional configuration
  // file listing settings that are common to the entire module.

  private val VerificationJobConfigFilename = "gobra.json"
  // A module config file must exist in the directory tree of configFile (i.e., in the same
  // directory or any parent directory). Without it, Gobra cannot determine the module-level configuration.
  private val ModuleConfigFilename = "gobra-mod.json"

  /** Resolves the job config file path, if one exists.
    * Returns `Some(file)` if a job config file was found, `None` otherwise (e.g., because a path to
    * a package directory has been provided that does not contain a job config file).
    * Returns `Left` if `configFile` itself does not exist. */
  private lazy val resolvedJobConfigFile: Either[Vector[VerifierError], Option[File]] = {
    if (configFile.exists()) {
      // if the user provides a path to a directory (presumably containing a Go package),
      // the we want to check whether a config file with the default filename is located within
      // that package:
      val candidate = if (configFile.isFile) configFile
        else new File(configFile, VerificationJobConfigFilename)
      Right(if (candidate.exists() && candidate.isFile) Some(candidate) else None)
    } else {
      Left(Vector(ConfigError(s"The provided config path does not exist: ${configFile.getAbsoluteFile.toString}")))
    }
  }

  /** The verification job config is optional */
  private lazy val verificationJobConfig: Either[Vector[VerifierError], VerificationJobCfg] = {
    resolvedJobConfigFile.flatMap {
      case Some(file) =>
        // provide the generic type argument to avoid weird runtime errors even though the
        // Scala compiler pretends to not need it!
        GobraJsonConfigHandler.fromJson[VerificationJobCfg](file)
      case None =>
        // No job config file found; return an empty config (all fields None).
        // Missing fields will be filled from the module config's default_job_cfg.
        Right(VerificationJobCfg())
    }
  }

  /** Returns the module config together with the directory containing the module config file */
  private lazy val moduleConfigWithDir: Either[Vector[VerifierError], (GobraModuleCfg, Path)] = {
    var searchDirectory = if (configFile.isFile) configFile.getParentFile else configFile
    var moduleConfigFile: Option[File] = None
    while (searchDirectory != null) {
      val potentialModuleConfigFile = new File(searchDirectory, ModuleConfigFilename)
      if (potentialModuleConfigFile.exists() && potentialModuleConfigFile.isFile) {
        moduleConfigFile = Some(potentialModuleConfigFile)
        searchDirectory = null
      } else {
        searchDirectory = searchDirectory.getParentFile
      }
    }
    moduleConfigFile match {
      case Some(file) =>
        // provide the generic type argument to avoid weird runtime errors even though the
        // Scala compiler pretends to not need it!
        GobraJsonConfigHandler
          .fromJson[GobraModuleCfg](file)
          .map(cfg => (cfg, file.getParentFile.toPath))
      case None =>
        Left(Vector(ConfigError(s"Could not find module configuration file $ModuleConfigFilename in the directory or a parent directory of ${configFile.getAbsoluteFile.toString}")))
    }
  }

  // baseConfig is not used since config is constructed via InputConfig
  override protected def baseConfig: BaseConfig =
    Violation.violation("baseConfig should not be accessed in ConfigFileModeConfig")

  override lazy val config: Either[Vector[VerifierError], Config] = {
    val jobConfigDir: Path = (if (configFile.isDirectory) configFile else configFile.getParentFile).toPath
    for {
      moduleWithDir <- moduleConfigWithDir
      jobCfg <- verificationJobConfig
      resolvedJobFile <- resolvedJobConfigFile
      moduleCfg = moduleWithDir._1
      moduleConfigDir = moduleWithDir._2
      _ = {
        logger.info(s"Using module config: ${moduleConfigDir.resolve(ModuleConfigFilename)}")
        resolvedJobFile match {
          case Some(f) => logger.info(s"Using job config: ${f.getAbsolutePath}")
          case None => logger.info("No job config file found; using default-job config only from module config")
        }
      }
      defaultJobCfg: VerificationJobCfg = moduleCfg.default_job_cfg.getOrElse(VerificationJobCfg())

      // Build module-level InputConfig:
      // 1. Parse module's `other` field via ScallopGobraConfig
      moduleOtherConfig <- InputConfig.fromOtherArgs(defaultJobCfg.other.getOrElse(List.empty))
      // 2. Convert module's structured fields to InputConfig
      moduleStructuredConfig = InputConfig.fromVerificationJobCfg(defaultJobCfg, moduleConfigDir)
      // 3. Merge structured and other options (structured takes precedence)
      moduleLevelConfig = moduleStructuredConfig merge moduleOtherConfig

      // Build job-level InputConfig:
      // 1. Parse job's `other` field via ScallopGobraConfig
      jobOtherConfig <- InputConfig.fromOtherArgs(jobCfg.other.getOrElse(List.empty))
      // 2. Convert job's structured fields to InputConfig
      jobStructuredConfig = InputConfig.fromVerificationJobCfg(jobCfg, jobConfigDir)
      // 3. Merge structured and other options (structured takes precedence)
      jobLevelConfig = jobStructuredConfig merge jobOtherConfig

      // Apply precedence: job-level config takes precedence over module-level config
      mergedConfig = jobLevelConfig orElse moduleLevelConfig

      // Set the input directory for package mode if not specified
      finalInputConfig = if (mergedConfig.input.value.isEmpty && mergedConfig.recursive.value.isEmpty && mergedConfig.directory.value.isEmpty) {
        mergedConfig.copy(directory = InputConfigOption("directory", Some(List(jobConfigDir.toFile))))
      } else {
        mergedConfig
      }

      // Validate and convert to Config
      result <- finalInputConfig.config(isInputOptional = false, skipIncludeDirChecks = false)
    } yield result
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
       | Mode 4 (--config):
       |  Instructs Gobra to read all configuration options from the provided JSON file. The only other permitted CLI option is '--printConfig'.
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

  val configFile:  ScallopOption[File] = opt[File](
    name = "config",
    descr = "Reads all configuration options from the provided JSON file. The only other permitted CLI option is '--printConfig'.",
    noshort = true
  )

  val printConfig: ScallopOption[Boolean] = opt[Boolean](
    name = "printConfig",
    descr = "Print the resolved configuration and exit without verifying. Requires '--config'.",
    default = Some(false),
    noshort = true,
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

  val backend: ScallopOption[ViperBackend] = choice(
    choices = ViperBackendConverter.values.map(_.value),
    name = "backend",
    descr = s"Specifies the used Viper backend. The default is ${ConfigDefaults.DefaultBackend.value}.",
    default = None,
    noshort = true
  ).map(ViperBackendConverter.convert)

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

  val checkOverflows: ScallopOption[Boolean] = toggle(
    name = "overflow",
    descrYes = "Find expressions that may lead to integer overflow",
    descrNo = "Do not check for integer overflow (default)",
    default = Some(ConfigDefaults.DefaultCheckOverflows),
    noshort = true
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
    choice(
      choices = MoreJoinsConverter.values.map(_.value),
      name = "moreJoins",
      descr = s"Specifies if silicon should be run with more joins completely enabled (${MoreJoins.All.value}), disabled (${MoreJoins.Disabled.value}), or only for impure conditionals (${MoreJoins.Impure.value}).",
      default = Some(ConfigDefaults.DefaultMoreJoins.value),
      noshort = true
    ).map(MoreJoinsConverter.convert)
  }

  val mceMode: ScallopOption[MCE.Mode] = {
    choice(
      choices = MCEConverter.values.map(_.value),
      name = "mceMode",
      descr = s"Specifies if silicon should be run with more complete exhale enabled (${MCE.Enabled.value}), disabled (${MCE.Disabled.value}), or enabled on demand (${MCE.OnDemand.value}).",
      default = Some(ConfigDefaults.DefaultMCEMode.value),
      noshort = true
    ).map(MCEConverter.convert)
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

  // Required by Scallop before accessing option values
  verify()

  /** Extracts an InputConfig from CLI options.
    * Uses `.toInputConfigOption` to get InputConfigOption with the option's name and value.
    * Fields are ordered to match the CLI option definitions above. */
  def toInputConfig: InputConfig = {
    import InputConfigOption.ScallopOptionExtension
    InputConfig(
      // Input mode fields
      input = input.toInputConfigOption,
      cutInputWithIdxs = cutInputWithIdxs.toInputConfigOption,
      directory = directory.toInputConfigOption,
      recursive = InputConfigOption(recursive.name, if (recursive.isSupplied) Some(recursive()) else None),
      configFile = configFile.toInputConfigOption,
      printConfig = printConfig.toInputConfigOption,
      projectRoot = projectRoot.toInputConfigOption,
      inclPackages = inclPackages.toInputConfigOption,
      exclPackages = exclPackages.toInputConfigOption,
      // Configuration options (in CLI definition order)
      gobraDirectory = gobraDirectory.toInputConfigOption,
      moduleName = module.toInputConfigOption,
      include = include.toInputConfigOption,
      backend = backend.toInputConfigOption,
      debug = debug.toInputConfigOption,
      logLevel = logLevel.toInputConfigOption,
      eraseGhost = eraseGhost.toInputConfigOption,
      goify = goify.toInputConfigOption,
      unparse = unparse.toInputConfigOption,
      printInternal = printInternal.toInputConfigOption,
      printVpr = printVpr.toInputConfigOption,
      parseOnly = parseOnly.toInputConfigOption,
      choppingUpperBound = chopUpperBound.toInputConfigOption,
      packageTimeout = InputConfigOption(packageTimeout.name, packageTimeout.toOption.map(Duration(_))),
      z3Exe = z3Exe.toInputConfigOption,
      boogieExe = boogieExe.toInputConfigOption,
      checkOverflows = checkOverflows.toInputConfigOption,
      cacheFile = InputConfigOption(cacheFile.name, cacheFile.toOption.map(_.toPath)),
      int32bit = int32Bit.toInputConfigOption,
      onlyFilesWithHeader = onlyFilesWithHeader.toInputConfigOption,
      checkConsistency = checkConsistency.toInputConfigOption,
      assumeInjectivityOnInhale = assumeInjectivityOnInhale.toInputConfigOption,
      parallelizeBranches = parallelizeBranches.toInputConfigOption,
      conditionalizePermissions = conditionalizePermissions.toInputConfigOption,
      z3APIMode = z3APIMode.toInputConfigOption,
      disableNL = disableNL.toInputConfigOption,
      unsafeWildcardOptimization = unsafeWildcardOptimization.toInputConfigOption,
      moreJoins = moreJoins.toInputConfigOption,
      mceMode = mceMode.toInputConfigOption,
      respectFunctionPrePermAmounts = respectFunctionPrePermAmounts.toInputConfigOption,
      hyperMode = hyperMode.toInputConfigOption,
      requireTriggers = requireTriggers.toInputConfigOption,
      noVerify = noVerify.toInputConfigOption,
      noStreamErrors = noStreamErrors.toInputConfigOption,
      disableCheckTerminationPureFns = disableCheckTerminationPureFns.toInputConfigOption,
      parseAndTypeCheckMode = parseAndTypeCheckMode.toInputConfigOption,
      disableSetAxiomatization = disableSetAxiomatization.toInputConfigOption,
      enableExperimentalFriendClauses = enableExperimentalFriendClauses.toInputConfigOption,
    )
  }

  lazy val config: Either[Vector[VerifierError], Config] =
    toInputConfig.config(isInputOptional, skipIncludeDirChecks)
}
