// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier
import viper.gobra.translator.util.{ViperParser, ViperUtil}
import viper.silver.plugin.standard.termination.TerminationPlugin
import viper.silver.plugin.standard.predicateinstance.PredicateInstancePlugin

class TerminationTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    (addDecreasesDomains _ andThen executeTerminationPlugin)(task)
  }

  private def addDecreasesDomains(task: BackendVerifier.Task): BackendVerifier.Task = {
    // constructs a separate Viper program (as a string) that should be parsed
    // after parsing this separate Viper program, the resulting AST is combined with `task`

    /** list of Viper standard imports that should be parsed */
    val imports = Seq("decreases/all.vpr")
    val progWithImports = imports.map(p => s"import <${p}>").mkString("\n")
    val vprProgram = ViperParser.parseProgram(progWithImports)
    task.copy(program = ViperUtil.combine(task.program, vprProgram))
  }

  def executeTerminationPlugin(task: BackendVerifier.Task): BackendVerifier.Task = {
    val plugin = new TerminationPlugin(null, null, null)
    val predInstancePlugin = new PredicateInstancePlugin()
    val transformedProgram = plugin.beforeVerify(task.program)
    val programWithoutPredicateInstances = predInstancePlugin.beforeVerify(transformedProgram)
    task.copy(program = programWithoutPredicateInstances)
  }

}

