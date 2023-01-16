// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.silver.reporter.{EntityFailureMessage, EntitySuccessMessage, Message, OverallFailureMessage, OverallSuccessMessage}
import viper.silver.verifier.VerificationResult

class DefaultMessageBackTranslator(backTrackInfo: BackTrackInfo, config: Config) extends MessageBackTranslator {
  override def translate(msg: Message): GobraMessage = {
    msg match {
      case m@EntityFailureMessage(verifier, Source(info), time, result, cached) => println("HERE")
      case m@EntityFailureMessage(verifier, _, time, result, cached) => println("THERE")
      case _ =>
    }
    defaultTranslate.lift.apply(msg).getOrElse(RawMessage(msg))
  }

  private def defaultTranslate: PartialFunction[Message, GobraMessage] = {
    case m: OverallSuccessMessage => GobraOverallSuccessMessage(m.verifier)
    case m: OverallFailureMessage => GobraOverallFailureMessage(m.verifier, translate(m.result))
    case m@EntitySuccessMessage(verifier, Source(info), time, cached) => GobraEntitySuccessMessage(config.taskName, verifier, m.concerning, info, time, cached)
    case m@EntityFailureMessage(verifier, Source(info), time, result, cached) => GobraEntityFailureMessage(config.taskName, verifier, m.concerning, info, translate(result), time, cached)
  }

  private def translate(result: VerificationResult): VerifierResult =
    BackTranslator.backTranslate(BackendVerifier.convertVerificationResult(result, backTrackInfo))(config)
}
