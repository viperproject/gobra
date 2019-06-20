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

  type ErrorTransformer = PartialFunction[silver.verifier.VerificationError, VerifierError]
  type ReasonTransformer = PartialFunction[silver.verifier.ErrorReason, VerifierError]

  def backTranslate(result: BackendVerifier.Result)(config: Config) = VerifierResult.Success
}
