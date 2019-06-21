/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.reporting
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.Config
import viper.silver

object BackTranslator {

  trait ErrorBackTranslator {
    def translate(error: silver.verifier.VerificationError): VerifierError
    def translate(reason: silver.verifier.ErrorReason): VerifierError
  }

  case class BackTrackInfo(
                            errorT: Seq[BackTranslator.ErrorTransformer],
                            reasonT: Seq[BackTranslator.ReasonTransformer]
                          )

  type ErrorTransformer = PartialFunction[silver.verifier.VerificationError, VerifierError]
  type ReasonTransformer = PartialFunction[silver.verifier.ErrorReason, VerifierError]

  def backTranslate(result: BackendVerifier.Result)(config: Config): VerifierResult = result match {
    case BackendVerifier.Success => VerifierResult.Success
    case BackendVerifier.Failure(errors, backtrack) =>

      ???
  }
}
