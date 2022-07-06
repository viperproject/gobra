// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend.{PFunctionSpec, PGlobalVarDecl, PProgram}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ProgramTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefProgram: WellDefinedness[PProgram] = createWellDef {
    case PProgram(_, spec, _, members) =>
      val globalDecls = members.collect{ case d: PGlobalVarDecl => d }
      /* noDynamicBoundCalls(globalDecls) ++*/ isValidProgramSpec(spec)
  }

  private def isValidProgramSpec: PFunctionSpec => Messages = {
    case s@ PFunctionSpec(pres, preserves, _, terminationMeasures, isPure, isTrusted) =>
      val validCond = pres.isEmpty && preserves.isEmpty && terminationMeasures.isEmpty && !isPure && !isTrusted
      error(s, s"Only postconditions can be specified in package specifications", !validCond)
  }

  private def noDynamicBoundCalls: Vector[PGlobalVarDecl] => Messages = {
    ???
  }
}
