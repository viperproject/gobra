// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator

import viper.gobra.reporting.BackTranslator.VerificationBackTrackInfo
import viper.silver.{ast => vpr}
import viper.gobra.ast.internal.Program
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.Config
import viper.gobra.translator.implementations.DfltTranslatorConfig
import viper.gobra.translator.implementations.translator.ProgramsImpl
import viper.gobra.reporting.GeneratedViperMessage

object Translator {

  def translate(program: Program)(config: Config): (vpr.Program, VerificationBackTrackInfo) = {
    val translationConfig = new DfltTranslatorConfig()
    val programTranslator = new ProgramsImpl()
    val (encodedProgram, backtrack) = programTranslator.translate(program)(translationConfig)

    config.reporter report GeneratedViperMessage(config.inputFiles.head, () => encodedProgram, () => backtrack)
    (encodedProgram, backtrack)

  }

}
