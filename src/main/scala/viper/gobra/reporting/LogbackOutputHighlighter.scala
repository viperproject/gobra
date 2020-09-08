// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants._
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

import scala.annotation.switch

class LogbackOutputHighlighter extends ForegroundCompositeConverterBase[ILoggingEvent] {
  override def getForegroundColorCode(event: ILoggingEvent): String = {
    val level: Level = event.getLevel

    (level.toInt: @switch) match {
      case Level.ERROR_INT => BOLD + RED_FG
      case Level.WARN_INT => YELLOW_FG
      case Level.INFO_INT => CYAN_FG
      case _ => DEFAULT_FG
    }
  }
}
