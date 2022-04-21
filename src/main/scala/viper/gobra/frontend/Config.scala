// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.File
import java.nio.file.{Files, Path, Paths}
import ch.qos.logback.classic.{Level, Logger}
import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import org.bitbucket.inkytonik.kiama.util.{FileSource, Source}
import org.rogach.scallop.{ScallopConf, ScallopOption, listArgConverter, singleArgConverter}
import org.slf4j.LoggerFactory
import viper.gobra.backend.{ViperBackend, ViperBackends}
import viper.gobra.GoVerifier
import viper.gobra.frontend.Source.{FromFileSource, getPackageInfo}
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
    s""" Usage: ${GoVerifier.name} -i <input-files or a single package name> [OPTIONS]
       |
       | Options:
       |""".stripMargin
  )

  /**
    * Command-line options
    */
  val input: ScallopOption[List[String]] = opt[List[String]](
    name = "input",
    descr = "List of Gobra programs, a project directory or a single package name to verify",
    short = 'i'
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
    short = 'p'
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
  )(listArgConverter(dir => new File(dir)))

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

  val onlyFilesWithHeader: ScallopOption[Boolean] = opt(
    name = "onlyFilesWithHeader",
    descr = s"When enabled, Gobra only looks at files that contain the header comment '${Config.prettyPrintedHeader}'",
    default = Some(false),
    noshort = false
  )

  /**
    * Exception handling
    */
  /**
    * Epilogue
    */

  /** Argument Dependencies */
  if (!isInputOptional) {
    requireAtLeastOne(input)
  }


  /** File Validation */
  def validateInput(inputOption: ScallopOption[List[String]],
                    recOption: ScallopOption[Boolean],
                    rootDirectoryOption: ScallopOption[Path],
                    includeOption: ScallopOption[List[File]],
                    includePkgOption: ScallopOption[List[String]],
                    excludePkgOption: ScallopOption[List[String]]): Unit =
    validateOpt(inputOption, recOption, rootDirectoryOption, includeOption, includePkgOption, excludePkgOption) {
      (inputOpt, recOpt, rootDirectoryOpt, includeOpt, includePkgOpt, excludePkgOpt) =>
      def checkConversion(input: List[String]): Either[String, Vector[Path]] = {
        def validateSources(sources: Vector[Source]): Either[Messages, Vector[Path]] = {
          val (remainingSources, paths) = sources.partitionMap {
            case FileSource(name, _) => Right(Paths.get(name))
            case FromFileSource(path, _, _) => Right(path)
            case s => Left(s)
          }
          if (remainingSources.isEmpty) Right(paths)
          else Left(message(null, s"Expected file sources but got $remainingSources"))
        }

        val shouldParseRecursively = recOpt.getOrElse(false)
        val inputValidationMsgs = InputConverter.validate(input, shouldParseRecursively)

        lazy val projectRoot = rootDirectoryOpt.getOrElse(
          includeOpt.flatMap(_.headOption.map(_.toPath)).getOrElse(Path.of(""))
        )

        val paths = for {
          _ <- if (inputValidationMsgs.isEmpty) Right(()) else Left(inputValidationMsgs)
          sources = InputConverter.convert(input, shouldParseRecursively, projectRoot, includePkgOpt.getOrElse(List()), excludePkgOpt.getOrElse(List()))
          paths <- validateSources(sources.values.flatten.toVector)
        } yield paths

        paths.left.map(msgs => s"The following errors have occurred: ${msgs.map(_.label).mkString(",")}")
      }

      def atLeastOnePath(paths: Vector[Path]): Either[String, Unit] = {
        if (paths.nonEmpty || isInputOptional) Right(()) else Left(s"Package resolution has not found any files for verification - are you using '.${PackageResolver.gobraExtension}' or '.${PackageResolver.goExtension}' as file extension?")
      }

      def pathsExist(paths: Vector[Path]): Either[String, Unit] = {
        val notExisting = paths.filterNot(Files.exists(_))
        if (notExisting.isEmpty) Right(()) else Left(s"Files '${notExisting.mkString(",")}' do not exist")
      }

      def pathsAreFilesOrDirectories(paths: Vector[Path]): Either[String, Unit] = {
        val notFilesOrDirectories = paths.filterNot(file => Files.isRegularFile(file) || Files.isDirectory(file))
        if (notFilesOrDirectories.isEmpty) Right(()) else Left(s"Files '${notFilesOrDirectories.mkString(",")}' are neither files or directories")
      }

      def pathsAreReadable(paths: Vector[Path]): Either[String, Unit] = {
        val notReadable = paths.filterNot(Files.isReadable)
        if (notReadable.isEmpty) Right(()) else Left(s"Files '${notReadable.mkString(",")}' are not readable")
      }

      // perform the following checks:
      // - validate fileOpt using includeOpt
      // - convert fileOpt using includeOpt
      //  - result should be non-empty, exist, be files and be readable
      val input: List[String] = inputOpt.getOrElse(List())
      for {
        convertedFiles <- checkConversion(input)
        _ <- atLeastOnePath(convertedFiles)
        _ <- pathsExist(convertedFiles)
        _ <- pathsAreFilesOrDirectories(convertedFiles)
        _ <- pathsAreReadable(convertedFiles)
      } yield ()
  }

  if (!skipIncludeDirChecks) {
    validateFilesExist(include)
    validateFilesIsDirectory(include)
  }

  // List of input arguments together with their specified line numbers.
  // Specified line numbers are removed from their corresponding input argument.
  val cutInputWithIdxs: ScallopOption[List[(String, List[Int])]] = input.map(_.map{ arg =>
    val pattern = """(.*)@(\d+(?:,\d+)*)""".r
    arg match {
      case pattern(prefix, idxs) =>
        (prefix, idxs.split(',').toList.map(_.toInt))

      case _ => (arg, List.empty[Int])
    }
  })
  val cutInput: ScallopOption[List[String]] = cutInputWithIdxs.map(_.map(_._1))

  validateInput(input, recursive, rootDirectory, include, includePackages, excludePackages)

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

  lazy val inputPackageMap: Map[PackageInfo, Vector[Source]] = InputConverter.convert(
    input.toOption.getOrElse(List()),
    recursive.getOrElse(false),
    projectRoot,
    includePackages.getOrElse(List()),
    excludePackages.getOrElse(List())
  )
  lazy val isolated: Option[Vector[SourcePosition]] =
    InputConverter.isolatedPosition(cutInputWithIdxs.toOption) match {
      case Nil => None
      case positions => Some(positions.toVector)
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

  private object InputConverter {

    def validate(inputs: List[String], recursive: Boolean): Messages = {
      val files = inputs flatMap (file => getAllGobraFiles(file, recursive))
      files match {
        case files if files.nonEmpty => noMessages
        case _ if isInputOptional => noMessages
        // error states
        case files if files.isEmpty => message(null, s"no files found in the specified input (use -r to traverse directories)")
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
    }

    def convert(input: List[String], recursive: Boolean, projectRoot: Path, includePackages: List[String], excludePackages: List[String]): Map[PackageInfo, Vector[Source]] = {
      val sources = parseInputStrings(input.toVector, recursive)
      sources.groupBy(src => getPackageInfo(src, projectRoot))
        .filter({case (pkgInfo, _) => (includePackages.isEmpty || includePackages.contains(pkgInfo.name)) && !excludePackages.contains(pkgInfo.name)})
    }

    /**
     * Parses all provided inputs and returns a list of all gobra files found
     *
     */
    private def parseInputStrings(inputs: Vector[String], recursive: Boolean): Vector[Source] = {
      inputs.flatMap(input => getAllGobraFiles(input, recursive))
        .map(file => FileSource(file.getPath))
    }

    /**
     * Gets all gobra files in a input location. If the location is a file that ends in ".<ext>" it is returned.
     * If it is a directory, all files contained in it, ending in ".<ext>", are returned.
     *
     * @param recursive if true, directories are traversed recursively
     * @return a Vector of the resolved gobra files
     */
    private def getAllGobraFiles(input: String, recursive: Boolean): Vector[File] =
      input match {
        case PackageResolver.inputFileRegex(filename) => Vector(new File(filename))
        case dirname => getInputFilesInDir(new File(dirname), recursive)
      }

    /**
     * Gets all files with a go/gobra extension inside this directory
     *
     * @param directory directory to look for files
     * @param recursive traverse subdirectories if this is set to true
     * @return a List of go/gobra files
     */
    private def getInputFilesInDir(directory: File, recursive: Boolean): Vector[File] = {
      Violation.violation(directory.exists && directory.isDirectory, "getInputFilesInDir didn't receive a directory as input: " + directory.toString)
      directory.listFiles()
        // Filters out all files that aren't either go/gobra files or directories (if recursive is set)
        .filter(file => (file.isDirectory && recursive) || PackageResolver.inputFileRegex.matches(file.getName))
        .flatMap(file => if (file.isDirectory) getInputFilesInDir(file, recursive) else List(file))
        .toVector
    }

    def isolatedPosition(cutInputWithIdxs: Option[List[(String, List[Int])]]): List[SourcePosition] = {
      cutInputWithIdxs.map(_.flatMap { case (input, idxs) =>
        getAllGobraFiles(input, recursive = false) match {
          // only go and gobra files and not directories can have a position
          case Vector(file) => idxs.map(idx => SourcePosition(file.toPath, idx, 0))
          case _ => List.empty
        }
      }).getOrElse(List.empty)
    }
  }

  lazy val config: Config = Config(
    recursive = recursive(),
    gobraDirectory = gobraDirectory(),
    packageInfoInputMap = inputPackageMap,
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
    onlyFilesWithHeader = onlyFilesWithHeader(),
  )
}
