// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Paths
import ch.qos.logback.classic.Level
import com.typesafe.scalalogging.Logger
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import viper.gobra.frontend.LoggerDefaults
import viper.gobra.util.{OutputUtil, Violation}

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
                              unparse: Boolean = false,
                              eraseGhost: Boolean = false,
                              goify: Boolean = false,
                              debug: Boolean = false,
                              printInternal: Boolean = false,
                              printVpr: Boolean = false,
                              printSIFVpr: Boolean = false,
                              streamErrs: Boolean = true) extends GobraReporter {

  lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(getClass.getName))

  override def report(msg: GobraMessage): Unit = msg match {
    case PreprocessedInputMessage(WithoutBuiltinSources(input), content) if unparse => write(input, "gobrafied", content())
    case ParsedInputMessage(WithoutBuiltinSources(input), program) if unparse => write(input, "unparsed", program().formatted)
    case TypeCheckSuccessMessage(WithoutBuiltinSources(inputs), _, _, _, erasedGhostCode, goifiedGhostCode) =>
      if (eraseGhost) write(inputs, "ghostLess", erasedGhostCode())
      if (goify) write(inputs, "go", goifiedGhostCode())
    case TypeCheckDebugMessage(WithoutBuiltinSources(inputs), _, debugTypeInfo) if debug => write(inputs, "debugType", debugTypeInfo())
    case DesugaredMessage(WithoutBuiltinSources(inputs), internal) if printInternal => write(inputs, "internal", internal().formatted)
    case AppliedInternalTransformsMessage(WithoutBuiltinSources(inputs), internal) if printInternal => write(inputs, "internal", internal().formatted)
    case m@GeneratedViperMessage(_, WithoutBuiltinSources(inputs), _, _) if printVpr => write(inputs, "vpr", m.vprAstFormatted)
    case m: ChoppedViperMessage if printVpr => write(m.inputs, s"chopped${m.idx}.vpr", m.vprAstFormatted)
    case m: ChoppedProgressMessage => logger.info(m.toString)
    case m@SIFEncodedViperMessage(_, WithoutBuiltinSources(inputs), _) if printSIFVpr => write(inputs, "sif.vpr", m.vprAstFormatted)
    case CopyrightReport(text) => println(text)
    // Stream errors here
    case m:GobraEntityFailureMessage if streamErrs => m.result match {
      case VerifierResult.Failure(errors) => errors.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
      case _ => // ignore
    }
    case m:ParserErrorMessage if streamErrs => m.result.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
    case m:TypeCheckFailureMessage if streamErrs => m.result.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
    case m:TransformerFailureMessage if streamErrs => m.result.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
    case _ => // ignore
  }

  private object WithoutBuiltinSources {
    // the reporter generates files with different extensions in the same place as the original file.
    // unfortunately, for the builtin resources, we do not generate a suitable path for the output files (these
    // paths in particular are typically for a path for which a regular use does not have permissions).
    val builtinSourcesNames = Seq("/builtin/builtin.gobra")
    def unapply(s: String): Option[String] = if (builtinSourcesNames contains s) None else Some(s)
    def unapply(s: Vector[String]): Option[Vector[String]] = {
      val diff = s filterNot builtinSourcesNames.contains
      if (diff.isEmpty) None else Some(diff)
    }
  }

  private def write(inputs: Vector[String], fileExt: String, content: String): Unit = {
    // this message belongs to multiple inputs. We simply pick the first one for the resulting file's name
    Violation.violation(inputs.nonEmpty, s"expected at least one file path for which the following message was reported: '$content''")
    write(inputs.head, fileExt, content)
  }

  private def write(input: String, fileExt: String, content: String): Unit = {
    val outputFile = OutputUtil.postfixFile(Paths.get(input), fileExt)
    try {
      FileUtils.writeStringToFile(outputFile.toFile, content, UTF_8)
    } catch {
      case _: UnsupportedOperationException => println(s"cannot write output to file $outputFile")
    }
  }
}
