/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.translator


import viper.gobra.ast.internal.Program
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.Config
import viper.gobra.translator.implementations.DfltTranslatorConfig
import viper.gobra.translator.implementations.translator.ProgramsImpl

object Translator {

  def translate(program: Program)(config: Config): BackendVerifier.Task = {
    val translationConfig = new DfltTranslatorConfig()
    val programTranslator = new ProgramsImpl()
    programTranslator.translate(program)(translationConfig)
  }

}
