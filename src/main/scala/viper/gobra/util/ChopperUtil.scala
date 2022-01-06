// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

import viper.silver.{ast => vpr}
import viper.silver.ast.SourcePosition
import viper.gobra.frontend.Config
import viper.gobra.reporting.{ChoppedViperMessage, Source}
import viper.gobra.ast.frontend.{PFunctionDecl, PMethodDecl}
import viper.gobra.backend.BackendVerifier.Task

object ChopperUtil {

  /** Splits task program into multiple Viper programs depending on config. */
  def computeChoppedPrograms(task: Task)(config: Config): Vector[vpr.Program] = {
    val programs = ViperChopper.chop(task.program)(isolate = computeIsolateMap(config), bound = Some(config.choppingUpperBound))

    // Report Chopped Programs
    programs.zipWithIndex.foreach{ case (chopped, idx) =>
      config.reporter report ChoppedViperMessage(config.inputs.map(_.name), idx, () => chopped, () => task.backtrack)
    }

    programs
  }

  def computeIsolateMap(config: Config): Option[vpr.Method => Boolean] = {
    def hit(x: SourcePosition, target: SourcePosition): Boolean = {
      (target.end match {
        case None => x.start.line == target.start.line
        case Some(pos) => target.start.line <= x.start.line && x.start.line <= pos.line
      }) && x.file.getFileName == target.file.getFileName
    }

    config.isolate.map { names => {
      case Source(Source.Verifier.Info(_: PFunctionDecl, _, origin, _)) => names.exists(hit(_, origin.pos))
      case Source(Source.Verifier.Info(_: PMethodDecl, _, origin, _)) => names.exists(hit(_, origin.pos))
      case _ => false
    }}
  }
}
