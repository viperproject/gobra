// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.File
import java.nio.file.Path
import ch.qos.logback.classic.{Level, Logger}
import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.util.Source
import org.rogach.scallop.{ScallopConf, ScallopOption, singleArgConverter}
import org.slf4j.LoggerFactory
import viper.gobra.backend.{ViperBackend, ViperBackends}
import viper.gobra.GoVerifier
import viper.gobra.frontend.PackageResolver.FileResource
import viper.gobra.frontend.Source.getPackageInfo
import viper.gobra.reporting.{FileWriterReporter, GobraReporter, StdIOReporter}
import viper.gobra.util.{TypeBounds, Violation}
import viper.silver.ast.SourcePosition

import scala.concurrent.duration.Duration

object LoggerDefaults {
  val DefaultLevel: Level = Level.INFO
}

object ConfigDefaults {
  val DefaultGobraDirectory: String = ".gobra"
  val DefaultTaskName: String = "gobra-task"
}

case class Config(
                   recursive: Boolean = false,
                   gobraDirectory: Path = Path.of(ConfigDefaults.DefaultGobraDirectory),
                   // Used as an identifier of a verification task, ideally it shouldn't change between verifications
                   // because it is used as a caching key. Additionally it should be unique when using the StatsCollector
                   taskName: String = ConfigDefaults.DefaultTaskName,
                   // Contains a mapping of packages to all input sources for that package
                   packageInfoInputMap: Map[PackageInfo, Vector[Source]] = Map(),
                   moduleName: String = "",
                   includeDirs: Vector[Path] = Vector(),
                   projectRoot: Path = Path.of(""),
                   reporter: GobraReporter = StdIOReporter(),
                   backend: ViperBackend = ViperBackends.SiliconBackend,
                   isolate: Option[Vector[SourcePosition]] = None,
                   choppingUpperBound: Int = 1,
                   packageTimeout: Duration = Duration.Inf,
                   z3Exe: Option[String] = None,
                   boogieExe: Option[String] = None,
                   logLevel: Level = LoggerDefaults.DefaultLevel,
                   cacheFile: Option[String] = None,
                   shouldParse: Boolean = true,
                   shouldTypeCheck: Boolean = true,
                   shouldDesugar: Boolean = true,
                   shouldViperEncode: Boolean = true,
                   checkOverflows: Boolean = false,
                   checkConsistency: Boolean = false,
                   shouldVerify: Boolean = true,
                   shouldChop: Boolean = false,
                   // The go language specification states that int and uint variables can have either 32bit or 64, as long
                   // as they have the same size. This flag allows users to pick the size of int's and uints's: 32 if true,
                   // 64 bit otherwise.
                   int32bit: Boolean = false,
                   // the following option is currently not controllable via CLI as it is meaningless without a constantly
                   // running JVM. It is targeted in particular to Gobra Server and Gobra IDE
                   cacheParser: Boolean = false,
                   // this option introduces a mode where Gobra only considers files with a specific annotation ("// +gobra").
                   // this is useful when verifying large packages where some files might use some unsupported feature of Gobra,
                   // or when the goal is to gradually verify part of a package without having to provide an explicit list of the files
                   // to verify.
                   onlyFilesWithHeader: Boolean = false,
) {

  def merge(other: Config): Config = {
    // this config takes precedence over other config
    val newInputs: Map[PackageInfo, Vector[Source]] = {
      val keys = packageInfoInputMap.keys ++ other.packageInfoInputMap.keys
      keys.map(k => k -> (packageInfoInputMap.getOrElse(k, Vector()) ++ other.packageInfoInputMap.getOrElse(k, Vector())).distinct).toMap
    }

    Config(
      recursive = recursive,
      moduleName = moduleName,
      taskName = taskName,
      gobraDirectory = gobraDirectory,
      packageInfoInputMap = newInputs,
      projectRoot = projectRoot,
      includeDirs = (includeDirs ++ other.includeDirs).distinct,
      reporter = reporter,
      backend = backend,
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
      onlyFilesWithHeader = onlyFilesWithHeader || other.onlyFilesWithHeader,
    )
  }

  lazy val typeBounds: TypeBounds =
    if (int32bit) {
      TypeBounds()
    } else {
      TypeBounds(Int = TypeBounds.IntWith64Bit, UInt = TypeBounds.UIntWith64Bit)
    }
}

object Config {
  // the header signals that a file should be considered when running on "header-only" mode
  val header = """\/\/\s*\+gobra""".r
  val prettyPrintedHeader = "// +gobra"
  require(header.matches(prettyPrintedHeader))
  def sourceHasHeader(s: Source): Boolean = header.findFirstIn(s.content).nonEmpty
}

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
    s""" Usage: ${GoVerifier.name} -i <input-files> [OPTIONS] OR
       |  ${GoVerifier.name} -p <directory> [OPTIONS]
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
    descr = "List of Gobra programs to verify with optional line information (e.g. `foo.gobra@42,111`)",
    short = 'i'
  )

  val directory: ScallopOption[File] = opt[File](
    name = "directory",
    descr = "A directory in which to search for a package to verify",
    short = 'p'
  )

  val recursive: ScallopOption[Boolean] = opt[Boolean](
    name = "recursive",
    descr = "Verify nested packages recursively",
    default = Some(false),
    short = 'r'
  )

  val includePackages: ScallopOption[List[String]] = opt[List[String]](
    name = "includePackages",
    descr = "Packages to verify. All packages found in the specified directories are verified by default.",
    default = Some(List.empty),
    noshort = true
  )

  val excludePackages: ScallopOption[List[String]] = opt[List[String]](
    name = "excludePackages",
    descr = "Packages to ignore. These packages will not be verified, even if they are found in the specified directories.",
    default = Some(List.empty),
    noshort = true
  )

  val gobraDirectory: ScallopOption[Path] = opt[Path](
    name = "gobraDirectory",
    descr = "Output directory for Gobra",
    default = Some(Path.of(ConfigDefaults.DefaultGobraDirectory)),
    short = 'g'
  )(singleArgConverter(arg => Path.of(arg)))

  val module: ScallopOption[String] = opt[String](
    name = "module",
    descr = "Name of current module that should be used for resolving imports",
    default = Some("")
  )

  val include: ScallopOption[List[File]] = opt[List[File]](
    name = "include",
    short = 'I',
    descr = "Uses the provided directories to perform package-related lookups before falling back to $GOPATH",
    default = Some(List())
  )

  val backend: ScallopOption[ViperBackend] = choice(
    choices = Seq("SILICON", "CARBON", "VSWITHSILICON", "VSWITHCARBON"),
    name = "backend",
    descr = "Specifies the used Viper backend. The default is SILICON.",
    default = Some("SILICON"),
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
    default = Some(if (debug()) Level.DEBUG.toString else LoggerDefaults.DefaultLevel.toString),
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
    default = Some(false),
    noshort = true
  )

  val chopUpperBound: ScallopOption[Int] = opt[Int](
    name = "chop",
    descr = "Number of parts the generated verification condition is split into (at most)",
    default = Some(1),
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
    default = None,
    noshort = true
  )

  val boogieExe: ScallopOption[String] = opt[String](
    name = "boogieExe",
    descr = "The Boogie executable",
    default = None,
    noshort = true
  )

  val checkOverflows: ScallopOption[Boolean] = opt[Boolean](
    name = "overflow",
    descr = "Find expressions that may lead to integer overflow",
    default = Some(false),
    noshort = false
  )

  val rootDirectory: ScallopOption[Path] = opt[Path](
    name = "projectRoot",
    descr = "The root directory of the project",
    default = None,
    noshort = true
  )

  val cacheFile: ScallopOption[String] = opt[String](
    name = "cacheFile",
    descr = "Cache file to be used by Viper Server",
    default = None,
    noshort = true
  )

  val int32Bit: ScallopOption[Boolean] = opt[Boolean](
    name = "int32",
    descr = "Run with 32-bit sized integers (the default is 64-bit ints)",
    default = Some(false),
    noshort = false
  )

  val onlyFilesWithHeader: ScallopOption[Boolean] = opt[Boolean](
    name = "onlyFilesWithHeader",
    descr = s"When enabled, Gobra only looks at files that contain the header comment '${Config.prettyPrintedHeader}'",
    default = Some(false),
    noshort = false
  )

  val checkConsistency: ScallopOption[Boolean] = opt[Boolean](
    name = "checkConsistency",
    descr = "Perform consistency checks on the generated Viper code",
    default = Some(false),
    noshort = true
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

  /**
    * Exception handling
    */
  /**
    * Epilogue
    */

  /** Argument Dependencies */
  if (!isInputOptional) {
    // either `input` or `directory` must be provided but not both.
    // this also checks that at least one file or a directory is provided (as opposed to just `-i` or `-p`)
    requireOne(input, directory)
  }

  // `recursive`, `includePackages`, and `excludePackages` only make sense when `directory` is specified and thus are
  // forbidden when using `input`:
  conflicts(input, List(recursive, includePackages, excludePackages))

  /** File Validation */
  validateFilesExist(cutInput)
  validateFilesIsFile(cutInput)
  validateFileExists(directory)
  validateFileIsDirectory(directory)
  if (!skipIncludeDirChecks) {
    validateFilesExist(include)
    validateFilesIsDirectory(include)
  }
  InputConverter.validate(cutInput, directory, recursive, includePackages, excludePackages)

  private object InputConverter {
    /**
      * stores the computed inputPackageMap after validating them to avoid a recomputation. `None` indicates that either validation
      * has not yet happened or that validation has failed
      */
    var inputPackageMapOpt: Option[Map[PackageInfo, Vector[Source]]] = None
    /** can only be accessed after calling `validate` */
    lazy val inputPackageMap: Map[PackageInfo, Vector[Source]] = inputPackageMapOpt.get

    /**
      * we distinguish between two modes:
      * - input mode in which the user simply provides all files that should be verified
      * - directory mode in which Gobra searches for the files to be verified
      * if inputs are provided, it checks that there is at least one source after resolving the sources.
      * if a directory is provided, it checks that at least 1 package remains to be verified after applying the desired filters */
    def validate(cutInput: ScallopOption[List[File]], directory: ScallopOption[File], recursive: ScallopOption[Boolean], includePackages: ScallopOption[List[String]], excludePackages: ScallopOption[List[String]]): Unit = {
      validateOpt (cutInput, directory, recursive, includePackages, excludePackages) ((cutInputOpt, dirOpt, recOpt, includeOpt, excludeOpt) => {
        // `requireOne(input, directory)` above makes sure that only `input` or `directory` is provided but not both.
        // Thus, we can simply take their concatenation
        // Note however that both options might not exist if the flag `isInputOptional` is set to true
        val fileOrDirInputs = cutInputOpt.getOrElse(List.empty) ++ dirOpt.map(List(_)).getOrElse(List.empty)
        val sources = fileOrDirInputs
          .map(i => FileResource(i.toPath))
          .flatMap(inputResource => PackageResolver.getSourceFiles(inputResource, recOpt.getOrElse(false)))
          .map { resource =>
            val source = resource.asSource()
            resource.close()
            source
          }
          .toVector
        val inputModeRes = cutInputOpt
          // perform input validation if we are in input mode:
          .map(_ => validateInputMode(sources))
          .getOrElse(Right(Map.empty[PackageInfo, Vector[Source]]))
        val directoryModeRes = dirOpt
          // perform directory validation if we are in directory mode:
          .map(_ => validateDirectoryMode(sources, includeOpt, excludeOpt))
          .getOrElse(Right(Map.empty[PackageInfo, Vector[Source]]))
        // combine `inputModeRes` and `directoryModeRes` by either returning the error if one occurred or by combining the
        // resulting maps:
        val validationRes = inputModeRes.flatMap(inputMap => directoryModeRes.map(dirMap => inputMap ++ dirMap))
        // store the result to avoid recomputation:
        inputPackageMapOpt = validationRes.toOption
        // return only the messages:
        validationRes.map(_ => ())
      })
    }

    private def validateInputMode(sources: Vector[Source]): Either[String, Map[PackageInfo, Vector[Source]]] = {
      // there should be at least one source:
      if (sources.isEmpty) {
        Left(s"expected at least one file after resolving the source files")
      } else {
        // we do not check whether the provided files all belong to the same package
        // instead, we trust the programmer that she knows what she's doing.
        // If they do not belong to the same package, Gobra will report an error after parsing.
        // we simply use the first source's package info to create a single map entry:
        Right(Map(getPackageInfo(sources.head, projectRoot) -> sources))
      }
    }

    private def validateDirectoryMode(sources: Vector[Source], includePackagesOpt: Option[List[String]], excludePackagesOpt: Option[List[String]]): Either[String, Map[PackageInfo, Vector[Source]]] = {
      /** an empty list means that all packages are allowed */
      val allowedPackages = includePackagesOpt.getOrElse(List.empty)
      val blockedPackages = excludePackagesOpt.getOrElse(List.empty)
      val pkgMap = sources
        .groupBy(source => getPackageInfo(source, projectRoot))
        // filter packages:
        .filter { case (pkgInfo, _) => (allowedPackages.isEmpty || allowedPackages.contains(pkgInfo.name)) && !blockedPackages.contains(pkgInfo.name) }
        // filter packages with zero source files:
        .filter { case (_, pkgFiles) => pkgFiles.nonEmpty }
      // validate `pkgMap`:
      if (pkgMap.isEmpty) {
        Left(s"No packages have been found that should be verified")
      } else {
        Right(pkgMap)
      }
    }
  }

  // cache file should only be usable when using viper server
  validateOpt (backend, cacheFile) {
    case (Some(_: ViperBackends.ViperServerWithSilicon), Some(_)) => Right(())
    case (Some(_: ViperBackends.ViperServerWithCarbon), Some(_)) => Right(())
    case (_, None) => Right(())
    case (_, Some(_)) => Left("Cache file can only be specified when the backend uses Viper Server")
  }

  validateOpt (gobraDirectory) {
    case Some(dir) =>
      // Set up gobra directory
      val gobraDirectory = dir.toFile
      if(!gobraDirectory.exists && !gobraDirectory.mkdir()) {
        Left( s"Could not create directory $gobraDirectory")
      } else if (!gobraDirectory.isDirectory) {
        Left(s"$dir is not a directory")
      } else if (!gobraDirectory.canRead) {
        Left(s"Couldn't read gobra directory $dir")
      } else if (!gobraDirectory.canWrite) {
        Left(s"Couldn't write to gobra directory $dir")
      } else {
        Right(())
      }
    case _ => Right(())
  }

  validateOpt (packageTimeout) {
    case Some(s) => try {
      Right(Duration(s))
    } catch {
      case e: NumberFormatException => Left(s"Couldn't parse package timeout: " + e.getMessage)
    }
    case None => Right(())
  }

  lazy val packageTimeoutDuration: Duration = packageTimeout.toOption match {
    case Some(d) => Duration(d)
    case _ => Duration.Inf
  }

  verify()

  lazy val includeDirs: Vector[Path] = include.toOption.map(_.map(_.toPath).toVector).getOrElse(Vector())

  // Take the user input for the project root or fallback to the fist include directory or the current directory
  lazy val projectRoot: Path = rootDirectory.getOrElse(
    includeDirs.headOption.getOrElse(Path.of(""))
  )

  lazy val isolated: Option[Vector[SourcePosition]] = {
    cutInputWithIdxs.toOption.map(_.flatMap {
      case (file, idxs) => idxs.map(idx => SourcePosition(file.toPath, idx, 0))
    }.toVector)
  }


  /** set log level */

  LoggerFactory.getLogger(GoVerifier.rootLogger)
    .asInstanceOf[Logger]
    .setLevel(logLevel())

  def shouldParse: Boolean = true
  def shouldTypeCheck: Boolean = !parseOnly.getOrElse(true)
  def shouldDesugar: Boolean = shouldTypeCheck
  def shouldViperEncode: Boolean = shouldDesugar
  def shouldVerify: Boolean = shouldViperEncode
  def shouldChop: Boolean = chopUpperBound.toOption.exists(_ > 1) || isolated.exists(_.nonEmpty)

  lazy val config: Config = Config(
    recursive = recursive(),
    gobraDirectory = gobraDirectory(),
    packageInfoInputMap = InputConverter.inputPackageMap,
    moduleName = module(),
    includeDirs = includeDirs,
    projectRoot = projectRoot,
    reporter = FileWriterReporter(
      unparse = unparse(),
      eraseGhost = eraseGhost(),
      goify = goify(),
      debug = debug(),
      printInternal = printInternal(),
      printVpr = printVpr()),
    backend = backend(),
    isolate = isolated,
    choppingUpperBound = chopUpperBound(),
    packageTimeout = packageTimeoutDuration,
    z3Exe = z3Exe.toOption,
    boogieExe = boogieExe.toOption,
    logLevel = logLevel(),
    cacheFile = cacheFile.toOption,
    shouldParse = shouldParse,
    shouldTypeCheck = shouldTypeCheck,
    shouldDesugar = shouldDesugar,
    shouldViperEncode = shouldViperEncode,
    checkOverflows = checkOverflows(),
    int32bit = int32Bit(),
    shouldVerify = shouldVerify,
    shouldChop = shouldChop,
    checkConsistency = checkConsistency(),
    onlyFilesWithHeader = onlyFilesWithHeader(),
  )
}
