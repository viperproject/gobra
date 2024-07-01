// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier
import viper.silver.plugin.SilverPlugin
import viper.silver.plugin.standard.refute.RefutePlugin
import viper.silver.verifier.AbstractError
import viper.silver.{ast => vpr}

class RefuteTransformer extends ViperTransformer {
  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    for {
      transformedProg <- executeTerminationPlugin(task)
    } yield transformedProg
  }

  private def executeTerminationPlugin(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    def applyPlugin(plugin: SilverPlugin, prog : vpr.Program): Either[Seq[AbstractError], vpr.Program] = {
      val transformedProgram = plugin.beforeVerify(prog)
      if (plugin.errors.isEmpty) {
        Right(transformedProgram)
      } else {
        Left(plugin.errors)
      }
    }

    val refutePlugin = new RefutePlugin(null, null, null, null)

    for {
      transformedProgram <- applyPlugin(refutePlugin, task.program)
    } yield task.copy(program = transformedProgram)
  }
}
