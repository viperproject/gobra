package viper.gobra.reporting

import ch.qos.logback.classic.Level
import viper.gobra.frontend.LoggerDefaults

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
