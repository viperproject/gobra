/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import java.io.File
import java.nio.file.{Files, Paths}

import ch.qos.logback.classic.{Level, Logger}
import com.typesafe.scalalogging.StrictLogging
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.SystemUtils
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import org.rogach.scallop.{ScallopConf, ScallopOption, listArgConverter, singleArgConverter}
import org.slf4j.LoggerFactory
import viper.gobra.GoVerifier

import scala.util.Properties

class Config(arguments: Seq[String])
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
    s""" Usage: ${GoVerifier.name} -i <input-files> [OPTIONS]
       |
       | Options:
       |""".stripMargin
  )

  /**
    * Command-line options
    */
  val input: ScallopOption[List[String]] = opt[List[String]](
    name = "input",
    descr = "List of Go programs to verify"
  )

  val include: ScallopOption[List[File]] = opt[List[File]](
    name = "include",
    short = 'I',
    descr = "Uses the provided directories to perform package-related lookups before falling back to $GOPATH",
    default = Some(List())
  )(listArgConverter(dir => new File(dir)))

  val debug: ScallopOption[Boolean] = toggle(
    name = "debug",
    descrYes = "Output additional debug information",
    default = Some(false)
  )

  val logLevel: ScallopOption[Level] = opt[Level](
    name = "logLevel",
    descr =
      "One of the log levels ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF (default: OFF)",
    default = Some(if (debug()) Level.DEBUG else Level.INFO),
    noshort = true
  )(singleArgConverter(arg => Level.toLevel(arg.toUpperCase)))

  val eraseGhost: ScallopOption[Boolean] = toggle(
    name = "eraseGhost",
    descrYes = "Print the input program without ghost code",
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

  /**
    * Exception handling
    */
  /**
    * Epilogue
    */

  /** Argument Dependencies */
  requireAtLeastOne(input)

  /** File Validation */
  def validateInput(inputOption: ScallopOption[List[String]],
                    includeOption: ScallopOption[List[File]]): Unit = validateOpt(inputOption, includeOption) { (inputOpt, includeOpt) =>

    def checkConversion(input: List[String], includeOpt: Option[List[File]]): Either[String, Vector[File]] = {
      val msgs = InputConverter.validate(input, includeOpt)
      if (msgs.isEmpty) Right(InputConverter.convert(input, includeOpt))
      else Left(s"The following errors have occurred: ${msgs.map(_.label).mkString(",")}")
    }

    def atLeastOneFile(files: Vector[File]): Either[String, Unit] = {
      if (files.nonEmpty) Right(()) else Left(s"Package resolution has not found any files for verification")
    }

    def filesExist(files: Vector[File]): Either[String, Unit] = {
      val notExisting = files.filterNot(_.exists())
      if (notExisting.isEmpty) Right(()) else Left(s"Files '${notExisting.mkString(",")}' do not exist")
    }

    def filesAreFiles(files: Vector[File]): Either[String, Unit] = {
      val notFiles = files.filterNot(_.isFile())
      if (notFiles.isEmpty) Right(()) else Left(s"Files '${notFiles.mkString(",")}' are not files")
    }

    def filesAreReadable(files: Vector[File]): Either[String, Unit] = {
      val notReadable = files.filterNot(file => Files.isReadable(file.toPath))
      if (notReadable.isEmpty) Right(()) else Left(s"Files '${notReadable.mkString(",")}' are not readable")
    }

    // perform the following checks:
    // - validate fileOpt using includeOpt
    // - convert fileOpt using includeOpt
    //  - result should be non-empty, exist, be files and be readable
    val input: List[String] = inputOpt.get // this is a non-optional CLI argument
    for {
      convertedFiles <- checkConversion(input, includeOpt)
      _ <- atLeastOneFile(convertedFiles)
      _ <- filesExist(convertedFiles)
      _ <- filesAreFiles(convertedFiles)
      _ <- filesAreReadable(convertedFiles)
    } yield ()
  }

  validateFilesExist(include)
  validateFilesIsDirectory(include)
  validateInput(input, include)

  verify()

  lazy val inputFiles: Vector[File] = InputConverter.convert(input.toOption.get, include.toOption)

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
    private val extension = """go"""
    private val goFileRgx = s"""(.*\\.$extension)$$""".r // without Scala string interpolation escapes: """(.*\.go)$""".r

    def validate(input: List[String], includeOpt: Option[List[File]]): Messages = {
      val files = input map isGoFilePath
      files.partition(_.isLeft) match {
        case (pkgs,  files) if pkgs.length == 1 && files.isEmpty => noMessages
        case (pkgs, files) if pkgs.isEmpty && files.nonEmpty => noMessages
        // error states:
        case (pkgs,  files) if pkgs.length > 1 && files.isEmpty =>
          message(pkgs, s"multiple package names provided: '${concatLeft(pkgs, ",")}'")
        case (pkgs, files) if pkgs.nonEmpty && files.nonEmpty =>
          message(pkgs, s"specific input files and one or more package names were simultaneously provided (files: '${concatRight(files, ",")}'; package names: '${concatLeft(pkgs, ",")}')")
        case (pkgs, files) if pkgs.isEmpty && files.isEmpty => message(null, s"no input specified")
      }
    }

    def convert(input: List[String], includeOpt: Option[List[File]]): Vector[File] = {
      val res = for {
        i <- identifyInput(input)
        files = resolvePackageOrFiles(i, includeOpt)
      } yield files
      assert(res.isDefined, "validate function did not catch this problem")
      res.get
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
    private def identifyInput(input: List[String]): Option[Either[String, Vector[File]]] = {
      val files = input map isGoFilePath
      files.partition(_.isLeft) match {
        case (pkgs,  files) if pkgs.length == 1 && files.isEmpty => Some(Left(pkgs.head.left.get))
        case (pkgs, files) if pkgs.isEmpty && files.nonEmpty => Some(Right(for(Right(s) <- files.toVector) yield s))
        case _ => None
      }
    }

    /**
      * Resolves a package name to specific input files
      * @param pkgOrFiles package name that should be resolved or files that will be returned unchanged
      * @param includeOpt list of directories that will be used for package resolution before falling back to $GOPATH
      * @return list of files belonging to the package or the input files in case pkgOrFiles is Right
      */
    private def resolvePackageOrFiles(pkgOrFiles: Either[String, Vector[File]], includeOpt: Option[List[File]]): Vector[File] = pkgOrFiles match {
      case Right(files) => files
      case Left(pkgName) =>
        // run `go help gopath` to get a detailed explanation of package resolution in go
        val path = Properties.envOrElse("GOPATH", "")
        val paths = (if (SystemUtils.IS_OS_WINDOWS) path.split(";") else path.split(":")).filter(_.nonEmpty)
        val includePaths = includeOpt.getOrElse(List()).map(_.toPath)
        // prepend includePaths before paths that have been derived based on $GOPATH:
        val packagePaths = includePaths ++ paths.map(p => Paths.get(p))
          // for now, we restrict our search to the "src" subdirectory:
          .map(_.resolve("src"))
          // the desired package should now be located in a subdirectory named after the package name:
          .map(_.resolve(pkgName))
        val pkgDirOpt = packagePaths.collectFirst { case p if Files.exists(p) => p }
        // pkgDir stores the path to the directory that should contain source files belonging to the desired package
        pkgDirOpt.map(pkgDir => getSourceFiles(pkgDir.toFile, pkgName)).getOrElse(Vector())
    }

    /**
      * Returns all source files with file extension 'extension' in a specific directory `dir` with package name `pkg`
      */
    private def getSourceFiles(dir: File, pkg: String): Vector[File] = {
      dir
        .listFiles
        .filter(_.isFile)
        // only consider file extensions "go"
        .filter(f => FilenameUtils.getExtension(f.getName) == extension)
        // get package name for each file:
        .map(f => (f, getPackageClause(f)))
        // ignore all files that have a different package name:
        .collect { case (f, Some(pkgName)) if pkgName == pkg => f }
        .toVector
    }

    private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

    private def getPackageClause(file: File): Option[String] = {
      val bufferedSource = scala.io.Source.fromFile(file)
      val content = bufferedSource.mkString
      bufferedSource.close()
      // TODO is there a way to perform the regex lazily on the file's content?
      pkgClauseRegex
        .findAllMatchIn(content)
        .collectFirst { case m if m.group(1) != null => m.group(1) }
    }
  }
}
