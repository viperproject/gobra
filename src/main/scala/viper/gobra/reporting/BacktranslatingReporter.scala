package viper.gobra.reporting

import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.silver.reporter.{Message, Reporter => SilverReporter}

trait MessageBackTranslator {
  def translate(msg: Message): GobraMessage
}

case class BacktranslatingReporter(reporter: GobraReporter, backTrackInfo: BackTrackInfo, config: Config) extends SilverReporter {
  override val name: String = reporter.name
  private val msgTranslator: MessageBackTranslator = new DefaultMessageBackTranslator(backTrackInfo, config)

  override def report(msg: Message): Unit = {
    reporter.report(msgTranslator.translate(msg))
  }
}
