/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import java.io.File
import java.nio.file.Files

import ch.qos.logback.classic.{Level, Logger}
import com.typesafe.scalalogging.StrictLogging
import org.rogach.scallop.{ScallopConf, ScallopOption, Util, singleArgConverter}
import org.slf4j.LoggerFactory
import viper.gobra.GoVerifier
import viper.gobra.backend.{ViperBackend, ViperBackends}
import viper.gobra.reporting.{FileWriterReporter, GobraReporter, StdIOReporter}


import viper.server.{ViperBackendConfig, ViperBackendConfigs}


object LoggerDefaults {
  val DefaultLevel: Level = Level.INFO
}
case class Config(
                 inputFile: File,
                 reporter: GobraReporter = StdIOReporter(),
                 backend: ViperBackend = ViperBackends.SiliconBackend,
                 // backendConfig is used for the ViperServer
                 backendConfig: ViperBackendConfig = ViperBackendConfigs.EmptyConfig,
                 z3Exe: Option[String] = None,
                 boogieExe: Option[String] = None,
                 logLevel: Level = LoggerDefaults.DefaultLevel,
                 shouldParse: Boolean = true,
                 shouldTypeCheck: Boolean = true,
                 shouldDesugar: Boolean = true,
                 shouldViperEncode: Boolean = true,
                 shouldVerify: Boolean = true
            )


class ScallopGobraConfig(arguments: Seq[String])
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
    s""" Usage: ${GoVerifier.name} -i <input-file> [OPTIONS]
       |
       | Options:
       |""".stripMargin
  )

  /**
    * Command-line options
    */
  val inputFile: ScallopOption[File] = opt[File](
    name = "input",
    descr = "Go program to verify is read from this file"
  )(singleArgConverter(arg => new File(arg)))

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


  /**
    * Exception handling
    */
  /**
    * Epilogue
    */

  /** Argument Dependencies */
  requireOne(inputFile)

  /** File Validation */
  def validateFileIsReadable(fileOption: ScallopOption[File]): Unit = addValidation {
    fileOption.toOption
      .map(file => {
        if (!Files.isReadable(file.toPath)) Left(Util.format("File '%s' is not readable", file))
        else Right(())
      })
      .getOrElse(Right(()))
  }

  validateFileExists(inputFile)
  validateFileIsFile(inputFile)
  validateFileIsReadable(inputFile)

  verify()

  /** set log level */

  LoggerFactory.getLogger(GoVerifier.rootLogger)
    .asInstanceOf[Logger]
    .setLevel(logLevel())

  def shouldParse: Boolean = true
  def shouldTypeCheck: Boolean = !parseOnly.getOrElse(true)
  def shouldDesugar: Boolean = shouldTypeCheck
  def shouldViperEncode: Boolean = shouldDesugar
  def shouldVerify: Boolean = shouldViperEncode


  lazy val config: Config = Config(
    inputFile = inputFile(),
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
    shouldVerify = shouldVerify
  )
}
