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
import viper.gobra.reporting.GeneratedViperMessage

import scala.concurrent.{Future, ExecutionContext}

object Translator {

  implicit val executionContext = ExecutionContext.global

  def translate(program: Program)(config: Config): Future[BackendVerifier.Task] = {
    Future {
      val translationConfig = new DfltTranslatorConfig()
      val programTranslator = new ProgramsImpl()
      val task = programTranslator.translate(program)(translationConfig)

      config.reporter report GeneratedViperMessage(config.inputFile, () => task.program, () => task.backtrack)
      task
    }

  }

}
