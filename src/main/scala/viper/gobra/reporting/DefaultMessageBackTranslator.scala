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
import com.typesafe.scalalogging.StrictLogging
import viper.gobra.util.VerifierPhase.Warnings

class DefaultMessageBackTranslator(backTrackInfo: BackTrackInfo, config: Config, warningsFromPreviousPhases: Warnings) extends MessageBackTranslator with StrictLogging {
  override def translate(msg: Message): GobraMessage = {
    // TODO: Remove this "if" when issue https://github.com/viperproject/gobra/issues/556 is fixed
    if (!config.noStreamErrors) {
      msg match {
        case _@EntityFailureMessage(_, Source(_), _, _, _) => // ignore
        case _@EntityFailureMessage(_, _, _, result, _) =>
          // Stream faulty message
          // we do not pass any warnings to `translate` as we do not know which warnings belong to the entity at hand
          translate(result, Vector.empty) match {
            case VerifierResult.Success(warnings) =>
              warnings.foreach(warning => logger.error(s"Warning at: ${warning.formattedMessage}"))
            case VerifierResult.Failure(errors, warnings) =>
              warnings.foreach(warning => logger.error(s"Warning at: ${warning.formattedMessage}"))
              errors.foreach(err => logger.error(s"Error at: ${err.formattedMessage}"))
          }
        case _ =>
      }
    }
    defaultTranslate.lift.apply(msg, warningsFromPreviousPhases).getOrElse(RawMessage(msg))
  }

  private def defaultTranslate: PartialFunction[(Message, Warnings), GobraMessage] = {
    case (m: OverallSuccessMessage, warningsFromPreviousPhases) => translateOverallMessage(m.verifier, m.result, warningsFromPreviousPhases)
    case (m: OverallFailureMessage, warningsFromPreviousPhases) => translateOverallMessage(m.verifier, m.result, warningsFromPreviousPhases)
    case (m@EntitySuccessMessage(verifier, Source(info), time, cached), _) => GobraEntitySuccessMessage(config.taskName, verifier, m.concerning, info, time, cached)
    case (m@EntityFailureMessage(verifier, Source(info), time, result, cached), _) =>
      // since we do not know which warnings belong to the current entity, we use `Vector.empty`:
      GobraEntityFailureMessage(config.taskName, verifier, m.concerning, info, translate(result, Vector.empty), time, cached)
  }

  private def translateOverallMessage(verifier: String, result: VerificationResult, warningsFromPreviousPhases: Warnings): GobraMessage = {
    translate(result, warningsFromPreviousPhases) match {
      case s: VerifierResult.Success => GobraOverallSuccessMessage(verifier, s)
      case f: VerifierResult.Failure => GobraOverallFailureMessage(verifier, f)
    }
  }

  private def translate(result: VerificationResult, warningsFromPreviousPhases: Warnings): VerifierResult =
    BackTranslator.backTranslate(BackendVerifier.convertVerificationResult(result, backTrackInfo), warningsFromPreviousPhases)(config)
}
