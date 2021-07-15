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
import viper.gobra.translator.implementations.translator.ProgramsImpl
import viper.gobra.reporting.GeneratedViperMessage
import viper.gobra.translator.transformers.{AssumeTransformer, TerminationTransformer, ViperTransformer}
import viper.gobra.util.Violation

object Translator {

  def translate(program: Program)(config: Config): BackendVerifier.Task = {
    val translationConfig = new DfltTranslatorConfig()
    val programTranslator = new ProgramsImpl()
    val task = programTranslator.translate(program)(translationConfig)
    
    if (config.checkConsistency) {
       val errors = task.program.checkTransitively
       if (errors.nonEmpty) Violation.violation(errors.toString)
     }

     val transformers: Seq[ViperTransformer] = Seq(
       new AssumeTransformer,
       new TerminationTransformer
     )

     val transformedTask = transformers.foldLeft(task) {
       case (t, transformer) => transformer.transform(t)
     }

     config.reporter report GeneratedViperMessage(config.inputFiles.head, () => transformedTask.program, () => transformedTask.backtrack)
     transformedTask
 
  }

}
