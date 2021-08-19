// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.backend.BackendVerifier
import viper.gobra.translator.interfaces.TranslatorConfig

trait Programs {
  def translate(program: in.Program)(conf: TranslatorConfig): BackendVerifier.Task
}