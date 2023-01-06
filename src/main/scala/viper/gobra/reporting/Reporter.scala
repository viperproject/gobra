// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Path, Paths}
import ch.qos.logback.classic.Level
import com.typesafe.scalalogging.Logger
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import viper.gobra.frontend.LoggerDefaults
import viper.gobra.util.OutputUtil

trait GobraReporter {
  val name: String

  def report(msg: GobraMessage): Unit
}

object NoopReporter extends GobraReporter {
  val name: String = "NoopReporter"
  def report(msg: GobraMessage): Unit = ()
}

case class StdIOReporter(name: String = "stdout_reporter", level: Level = LoggerDefaults.DefaultLevel) extends GobraReporter {
  override def report(msg: GobraMessage): Unit = msg match {
    case CopyrightReport(text) => println(text)
    case _ if Level.DEBUG.isGreaterOrEqual(level) => println(msg)
    case _ => // ignore
  }
}

case class FileWriterReporter(name: String = "filewriter_reporter",
                              gobraDir: Path,
                              unparse: Boolean = false,
                              eraseGhost: Boolean = false,
                              goify: Boolean = false,
                              debug: Boolean = false,
                              printInternal: Boolean = false,
                              printVpr: Boolean = false) extends GobraReporter {

  lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(getClass.getName))

  override def report(msg: GobraMessage): Unit = msg match {
    case PreprocessedInputMessage(input, content) if unparse => write(input, "gobrafied", content())
    case ParsedInputMessage(input, program) if unparse => write(input, "unparsed", program().formatted)
    case TypeCheckSuccessMessage(inputs, _, _, _, erasedGhostCode, goifiedGhostCode) =>
      if (eraseGhost) write(inputs, "ghostLess", erasedGhostCode())
      if (goify) write(inputs, "go", goifiedGhostCode())
    case TypeCheckDebugMessage(inputs, _, debugTypeInfo) if debug => write(inputs, "debugType", debugTypeInfo())
    case DesugaredMessage(inputs, internal) if printInternal => write(inputs, "internal", internal().formatted)
    case AppliedInternalTransformsMessage(inputs, internal) if printInternal => write(inputs, "internal", internal().formatted)
    case m@GeneratedViperMessage(_, inputs, _, _) if printVpr => write(inputs, "vpr", m.vprAstFormatted)
    case m: ChoppedViperMessage if printVpr => write(m.inputs, s"chopped${m.idx}.vpr", m.vprAstFormatted)
    case m: ChoppedProgressMessage => logger.info(m.toString)
    case CopyrightReport(text) => println(text)
    case _ => // ignore
  }

  // TODO: doc
  private val builtinSourcesNames = Seq("/builtin/builtin.gobra")
  private val defaultTargetName = "project"
  private val defaultTargetPath = Paths.get(gobraDir.toAbsolutePath.toString, defaultTargetName)
  private val defaultTarget = defaultTargetPath.toString

  private def write(inputs: Vector[String], fileExt: String, content: String): Unit = {
    // TODO: adapt comment
    // this message belongs to multiple inputs. We simply pick the first one for the resulting file's name
    write(defaultTarget, fileExt, content)
  }

  private def write(input: String, fileExt: String, content: String): Unit = {
    val basePath = if (builtinSourcesNames.contains(input)) defaultTargetPath else Paths.get(input)
    val outputFile = OutputUtil.postfixFile(basePath, fileExt)
    try {
      FileUtils.writeStringToFile(outputFile.toFile, content, UTF_8)
    } catch {
      case _: UnsupportedOperationException => println(s"cannot write output to file $outputFile")
    }
  }
}
