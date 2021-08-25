// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier.Task

/**
  * Trait for a Viper-to-Viper transformation. The Viper AST in the task as well as the associated backtracking
  * information can be transformed by classes implementing this trait.
  */
trait ViperTransformer {
  def transform(task: Task): Task
}

