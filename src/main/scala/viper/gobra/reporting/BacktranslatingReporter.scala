// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.silver.reporter.{Message, PluginAwareReporter}

trait MessageBackTranslator {
  def translate(msg: Message): GobraMessage
}

case class BacktranslatingReporter(reporter: GobraReporter, backTrackInfo: BackTrackInfo, config: Config) extends PluginAwareReporter {
  override val name: String = reporter.name
  private val msgTranslator: MessageBackTranslator = new DefaultMessageBackTranslator(backTrackInfo, config)

  override def doReport(msg: Message): Unit = {
    reporter.report(msgTranslator.translate(msg))
  }
}
