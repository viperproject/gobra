// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier.Task
import viper.gobra.translator.library.Simplifier
import viper.gobra.translator.library.tuples.TuplesImpl
import viper.silver.ast.utility.rewriter.Traverse

class SimplifierTransformer extends ViperTransformer {

  def transform(task: Task): Task = {

    // Transformations are applied in the order of the list
    val simplifiers: Seq[Simplifier] = Seq(
      TuplesImpl.Simplifier,
    )

    val transformedProgram = {
      // Transformations are not applied on domains.
      val (pos, info, errT) = task.program.meta
      val preTransformation = task.program.copy(domains = Seq.empty)(pos, info, errT) // remove domains
      val postTransformation = simplifiers.foldLeft(preTransformation) { case (p, f) =>
        val partialF = (f.simplify _).unlift
        p.transform(partialF, Traverse.BottomUp)
      }
      postTransformation.copy(domains = task.program.domains)(pos, info, errT) // add domains back
    }

    task.copy(program = transformedProgram)
  }

}
