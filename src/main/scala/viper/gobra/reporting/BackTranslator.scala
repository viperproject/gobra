/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.reporting
import viper.gobra.backend.ViperVerifier

object BackTranslator {

  def backTranslate(result: ViperVerifier.Result): VerifierResult = VerifierResult.Success

}
