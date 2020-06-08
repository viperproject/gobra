package viper.gobra.reporting

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

import ch.qos.logback.classic.Level
import org.apache.commons.io.FileUtils
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
                              unparse: Boolean = false,
                              eraseGhost: Boolean = false,
                              goify: Boolean = false,
                              debug: Boolean = false,
                              printInternal: Boolean = false,
                              printVpr: Boolean = false) extends GobraReporter {
  override def report(msg: GobraMessage): Unit = msg match {
    case ParsedInputMessage(file, program) if unparse => write(file, "unparsed", program().formatted)
    case TypeCheckSuccessMessage(file, _, erasedGhostCode) if eraseGhost => write(file, "ghostLess", erasedGhostCode())
    case TypeCheckSuccessMessage(file, _, goifiedGhostCode) if goify => write(file, "go", goifiedGhostCode())
    case TypeCheckDebugMessage(file, _, debugTypeInfo) if debug => write(file, "debugType", debugTypeInfo())
    case DesugaredMessage(file, internal) if printInternal => write(file, "internal", internal().formatted)
    case m@GeneratedViperMessage(file, _) if printVpr => write(file, "vpr", m.vprAstFormatted)
    case CopyrightReport(text) => println(text)
    case _ => // ignore
  }

  private def write(file: File, fileExt: String, content: String): Unit = {
    val outputFile = OutputUtil.postfixFile(file, fileExt)
    FileUtils.writeStringToFile(outputFile, content, UTF_8)
  }
}
