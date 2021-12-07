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
    defaultTranslate.lift.apply(msg).getOrElse(RawMessage(msg))
  }

  private def defaultTranslate: PartialFunction[Message, GobraMessage] = {
    case m: OverallSuccessMessage => GobraOverallSuccessMessage(m.verifier)
    case m: OverallFailureMessage => GobraOverallFailureMessage(m.verifier, translate(m.result))
    case EntitySuccessMessage(verifier, Source(info), _, _) => GobraEntitySuccessMessage(verifier, info)
    case EntityFailureMessage(verifier, Source(info), _, result, _) => GobraEntityFailureMessage(verifier, info, translate(result))
  }

  private def translate(result: VerificationResult): VerifierResult =
    BackTranslator.backTranslate(BackendVerifier.convertVerificationResult(result, backTrackInfo))(config)
}
