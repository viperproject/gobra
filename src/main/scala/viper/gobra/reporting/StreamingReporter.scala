// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import com.typesafe.scalalogging.StrictLogging

case class StreamingReporter(reporter: GobraReporter) extends GobraReporter with StrictLogging {
  override val name: String = reporter.name

  def report(msg: GobraMessage): Unit = {
    msg match {
      // TODO: just emit this at log level debug
      //case m:GobraEntitySuccessMessage if debug =>
      //  ??? //logger.debug(s"Member ${m.toString}")
      case m:GobraEntityFailureMessage => m.result match {
        case VerifierResult.Failure(errors) => errors.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
        case _ => // ignore
      }
      case m:ParserErrorMessage => m.result.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
      case m:TypeCheckFailureMessage => m.result.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
      case _ => // ignore
    }
    reporter.report(msg)
  }
}
