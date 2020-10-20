// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator


import viper.gobra.ast.internal.Program
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.Config
import viper.gobra.translator.implementations.DfltTranslatorConfig
import viper.gobra.translator.implementations.translator.{ProgramsImpl, StatementsImpl}
import viper.gobra.reporting.GeneratedViperMessage

object Translator {

  def translate(program: Program)(config: Config): BackendVerifier.Task = {
    val translationConfig = new DfltTranslatorConfig(stmt=new StatementsImpl(config.checkOverflows))
    val programTranslator = new ProgramsImpl()
    val task = programTranslator.translate(program)(translationConfig)

    // TODO: check here to process reports
    config.reporter report GeneratedViperMessage(config.inputFiles.head, () => task.program, () => task.backtrack)
    task
  }

}
