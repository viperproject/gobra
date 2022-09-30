// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier
import viper.silver.{ast => vpr}

trait SimpleViperTransformer extends ViperTransformer {

  final override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    val transformedProgram = {
      // Transformations are not applied on domains.
      val (pos, info, errT) = task.program.meta
      val preTransformation = task.program.copy(domains = Seq.empty)(pos, info, errT) // remove domains
      val postTransformation = transform(preTransformation)
      postTransformation.copy(domains = task.program.domains)(pos, info, errT) // add domains back
    }
    task.copy(program = transformedProgram)
  }

  def transform(program: vpr.Program): vpr.Program
}
