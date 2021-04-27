// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.File
import java.nio.file.{Files, Path}

import ch.qos.logback.classic.{Level, Logger}
import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import org.rogach.scallop.{ScallopConf, ScallopOption, listArgConverter, singleArgConverter}
import org.slf4j.LoggerFactory
import viper.gobra.backend.{ViperBackend, ViperBackends, ViperVerifierConfig}
import viper.gobra.GoVerifier
import viper.gobra.frontend.PackageResolver.{FileResource, RegularImport}
import viper.gobra.reporting.{FileWriterReporter, GobraReporter, StdIOReporter}
import viper.gobra.util.{TypeBounds, Violation}


object LoggerDefaults {
  val DefaultLevel: Level = Level.INFO
}
case class Config(
                 inputFiles: Vector[Path],
                 includeDirs: Vector[Path] = Vector(),
                 reporter: GobraReporter = StdIOReporter(),
                 backend: ViperBackend = ViperBackends.SiliconBackend,
                 // backendConfig is used for the ViperServer
                 backendConfig: ViperVerifierConfig = ViperVerifierConfig.EmptyConfig,
                 z3Exe: Option[String] = None,
                 boogieExe: Option[String] = None,
                 logLevel: Level = LoggerDefaults.DefaultLevel,
                 shouldParse: Boolean = true,
                 shouldTypeCheck: Boolean = true,
                 shouldDesugar: Boolean = true,
                 shouldViperEncode: Boolean = true,
                 checkOverflows: Boolean = false,
                 shouldVerify: Boolean = true,
                 // The go language specification states that int and uint variables can have either 32bit or 64, as long
                 // as they have the same size. This flag allows users to pick the size of int's and uints's: 32 if true,
                 // 64 bit otherwise.
                 int32bit: Boolean = false
            ) {
  def merge(other: Config): Config = {
    // this config takes precedence over other config
    Config(
      inputFiles = (inputFiles ++ other.inputFiles).distinct,
      includeDirs = (includeDirs ++ other.includeDirs).distinct,
      reporter = reporter,
      backend = backend,
      z3Exe = z3Exe orElse other.z3Exe,
      boogieExe = boogieExe orElse other.boogieExe,
      logLevel = if (logLevel.isGreaterOrEqual(other.logLevel)) other.logLevel else logLevel, // take minimum
      // TODO merge strategy for following properties is unclear (maybe AND or OR)
      shouldParse = shouldParse,
      shouldTypeCheck = shouldTypeCheck,
      shouldDesugar = shouldDesugar,
      shouldViperEncode = shouldViperEncode,
      checkOverflows = checkOverflows || other.checkOverflows,
      shouldVerify = shouldVerify,
      int32bit = int32bit || other.int32bit
    )
  }

  lazy val typeBounds: TypeBounds =
    if (int32bit) {
      TypeBounds()
    } else {
      TypeBounds(Int = TypeBounds.IntWith64Bit, UInt = TypeBounds.UIntWith64Bit)
    }
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
    descr = "List of Gobra programs or a single package name to verify"
  )

  val include: ScallopOption[List[File]] = opt[List[File]](
    name = "include",
    short = 'I',
    descr = "Uses the provided directories to perform package-related lookups before falling back to $GOPATH",
    default = Some(List())
  )(listArgConverter(dir => new File(dir)))

  val backend: ScallopOption[ViperBackend] = opt[ViperBackend](
    name = "backend",
    descr = "Specifies the used Viper backend, one of SILICON, CARBON (default: SILICON)",
    default = Some(ViperBackends.SiliconBackend),
    noshort = true
  )(singleArgConverter({
    case "SILICON" => ViperBackends.SiliconBackend
    case "CARBON" => ViperBackends.CarbonBackend
    case _ => ViperBackends.SiliconBackend
  }))

  val debug: ScallopOption[Boolean] = toggle(
    name = "debug",
    descrYes = "Output additional debug information",
    default = Some(false)
  )

  val logLevel: ScallopOption[Level] = opt[Level](
    name = "logLevel",
    descr =
      "One of the log levels ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF (default: OFF)",
    default = Some(if (debug()) Level.DEBUG else LoggerDefaults.DefaultLevel),
    noshort = true
  )(singleArgConverter(arg => Level.toLevel(arg.toUpperCase)))

  val eraseGhost: ScallopOption[Boolean] = toggle(
    name = "eraseGhost",
    descrYes = "Print the input program without ghost code",
    default = Some(false),
    noshort = true
  )

  val goify: ScallopOption[Boolean] = toggle(
    name = "goify",
    descrYes = "Print the input program with the ghost code commented out",
    default = Some(false),
    noshort = true
  )

  val unparse: ScallopOption[Boolean] = toggle(
    name = "unparse",
    descrYes = "Print the parsed program",
    default = Some(debug()),
    noshort = true
  )

  val printInternal: ScallopOption[Boolean] = toggle(
    name = "printInternal",
    descrYes = "Print the internal program representation",
    default = Some(debug()),
    noshort = true
  )

  val printVpr: ScallopOption[Boolean] = toggle(
    name = "printVpr",
    descrYes = "Print the encoded Viper program",
    default = Some(debug()),
    noshort = true
  )

  val parseOnly: ScallopOption[Boolean] = toggle(
    name = "parseOnly",
    descrYes = "Perform only the parsing step",
    default = Some(false),
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

  val checkOverflows: ScallopOption[Boolean] = toggle(
    name = "overflow",
    descrYes = "Find expressions that may lead to integer overflow",
    default = Some(false),
    noshort = false
  )

  val int32Bit: ScallopOption[Boolean] = toggle(
    name = "int32",
    descrYes = "Run with 32-bit sized integers",
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
                    includeOption: ScallopOption[List[File]]): Unit = validateOpt(inputOption, includeOption) { (inputOpt, includeOpt) =>

    def checkConversion(input: List[String], includeDirs: Vector[Path]): Either[String, Vector[Path]] = {
      val msgs = InputConverter.validate(input)
      if (msgs.isEmpty) Right(InputConverter.convert(input, includeDirs))
      else Left(s"The following errors have occurred: ${msgs.map(_.label).mkString(",")}")
    }

    def atLeastOneFile(files: Vector[Path]): Either[String, Unit] = {
      if (files.nonEmpty || isInputOptional) Right(()) else Left(s"Package resolution has not found any files for verification - are you using '.${PackageResolver.extension}' as file extension?")
    }

    def filesExist(files: Vector[Path]): Either[String, Unit] = {
      val notExisting = files.filterNot(Files.exists(_))
      if (notExisting.isEmpty) Right(()) else Left(s"Files '${notExisting.mkString(",")}' do not exist")
    }

    def filesAreFiles(files: Vector[Path]): Either[String, Unit] = {
      val notFiles = files.filterNot(Files.isRegularFile(_))
      if (notFiles.isEmpty) Right(()) else Left(s"Files '${notFiles.mkString(",")}' are not files")
    }

    def filesAreReadable(files: Vector[Path]): Either[String, Unit] = {
      val notReadable = files.filterNot(Files.isReadable)
      if (notReadable.isEmpty) Right(()) else Left(s"Files '${notReadable.mkString(",")}' are not readable")
    }

    // perform the following checks:
    // - validate fileOpt using includeOpt
    // - convert fileOpt using includeOpt
    //  - result should be non-empty, exist, be files and be readable
    val input: List[String] = inputOpt.getOrElse(List())
    for {
      convertedFiles <- checkConversion(input, includeOpt.map(_.map(_.toPath).toVector).getOrElse(Vector()))
      _ <- atLeastOneFile(convertedFiles)
      _ <- filesExist(convertedFiles)
      _ <- filesAreFiles(convertedFiles)
      _ <- filesAreReadable(convertedFiles)
    } yield ()
  }

  if (!skipIncludeDirChecks) {
    validateFilesExist(include)
    validateFilesIsDirectory(include)
  }
  validateInput(input, include)

  verify()

  lazy val includeDirs: Vector[Path] = include.toOption.map(_.map(_.toPath).toVector).getOrElse(Vector())
  lazy val inputFiles: Vector[Path] = InputConverter.convert(input.toOption.getOrElse(List()), includeDirs)

  /** set log level */

  LoggerFactory.getLogger(GoVerifier.rootLogger)
    .asInstanceOf[Logger]
    .setLevel(logLevel())

  def shouldParse: Boolean = true
  def shouldTypeCheck: Boolean = !parseOnly.getOrElse(true)
  def shouldDesugar: Boolean = shouldTypeCheck
  def shouldViperEncode: Boolean = shouldDesugar
  def shouldVerify: Boolean = shouldViperEncode

  private object InputConverter {

    private val goFileRgx = s"""(.*\\.${PackageResolver.extension})$$""".r // without Scala string interpolation escapes: """(.*\.go)$""".r

    def validate(input: List[String]): Messages = {
      val files = input map isGoFilePath
      files.partition(_.isLeft) match {
        case (pkgs,  files) if pkgs.length == 1 && files.isEmpty => noMessages
        case (pkgs, files) if pkgs.isEmpty && files.nonEmpty => noMessages
        // error states:
        case (pkgs,  files) if pkgs.length > 1 && files.isEmpty =>
          message(pkgs, s"multiple package names provided: '${concatLeft(pkgs, ",")}'")
        case (pkgs, files) if pkgs.nonEmpty && files.nonEmpty =>
          message(pkgs, s"specific input files and one or more package names were simultaneously provided (files: '${concatRight(files, ",")}'; package names: '${concatLeft(pkgs, ",")}')")
        case _ if isInputOptional => noMessages
        case (pkgs, files) if pkgs.isEmpty && files.isEmpty => message(null, s"no input specified")
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
    }

    def convert(input: List[String], includeDirs: Vector[Path]): Vector[Path] = {
      val res = for {
        i <- identifyInput(input).toRight("invalid input")
        files <- i match {
          case Right(files) => Right(files)
          case Left(_) =>
            for {
              // look for files in the current directory, i.e. use an empty importPath
              resolvedResources <- PackageResolver.resolve(RegularImport(""), includeDirs)
              resolvedFiles = resolvedResources.flatMap({
                case fileResource: FileResource => Some(fileResource.path)
                case _ => None
              })
              // we do not need the underlying resources anymore as we are only using FileResources:
              _ = resolvedResources.foreach(_.close())
            } yield resolvedFiles
        }
      } yield files
      assert(isInputOptional || res.isRight, s"validate function did not catch this problem: '${res.swap.getOrElse(None)}'")
      res.getOrElse(Vector())
    }

    /**
      * Checks whether string ends in ".<ext>" where <ext> corresponds to the extension defined in InputConverter
      * @return Right with the string converted to a File if the condition is met, otherwise Left containing `input`
      */
    private def isGoFilePath(input: String): Either[String, File] = input match {
      case goFileRgx(filename) => Right(new File(filename))
      case pkgName => Left(pkgName)
    }

    private def concatLeft(p: List[Either[String, File]], sep: String): String = {
      (for(Left(pkg) <- p.toVector) yield pkg).mkString(sep)
    }

    private def concatRight(p: List[Either[String, File]], sep: String): String = {
      (for(Right(f) <- p.toVector) yield f).mkString(sep)
    }

    /**
      * Decides whether the provided input strings should be interpreted as a single package name (Left) or
      * a vector of file paths (Right). If a mix is provided None is returned.
      */
    private def identifyInput(input: List[String]): Option[Either[String, Vector[Path]]] = {
      val files = input map isGoFilePath
      files.partition(_.isLeft) match {
        case (pkgs,  files) if pkgs.length == 1 && files.isEmpty => pkgs.head.swap.map(Left(_)).toOption
        case (pkgs, files) if pkgs.isEmpty && files.nonEmpty => Some(Right(for(Right(s) <- files.toVector) yield s.toPath))
        case _ => None
      }
    }
  }

  lazy val config: Config = Config(
    inputFiles = inputFiles,
    includeDirs = includeDirs,
    reporter = FileWriterReporter(
      unparse = unparse(),
      eraseGhost = eraseGhost(),
      goify = goify(),
      debug = debug(),
      printInternal = printInternal(),
      printVpr = printVpr()),
    backend = backend(),
    z3Exe = z3Exe.toOption,
    boogieExe = boogieExe.toOption,
    logLevel = logLevel(),
    shouldParse = shouldParse,
    shouldTypeCheck = shouldTypeCheck,
    shouldDesugar = shouldDesugar,
    shouldViperEncode = shouldViperEncode,
    checkOverflows = checkOverflows(),
    int32bit = int32Bit(),
    shouldVerify = shouldVerify
  )
}
