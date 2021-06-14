// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers
import viper.gobra.backend.BackendVerifier
import viper.silver.plugin.standard.termination.TerminationPlugin

class TerminationTransformer extends Transformer {
  override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    val plugin = new TerminationPlugin(null, null, null)
    val transformedProgram = plugin.beforeVerify(task.program)
    BackendVerifier.Task(
      program = transformedProgram,
      backtrack = task.backtrack
    )
  }
}
