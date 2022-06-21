// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, message, noMessages}
import viper.gobra.ast.frontend.{PExplicitQualifiedImport, PFunctionSpec, PImplicitQualifiedImport, PImport, PUnqualifiedImport}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ImportTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefImport: WellDefinedness[PImport] = createWellDef {
    case n: PExplicitQualifiedImport => isValidImportSpec(n.importSpec)
    case n: PUnqualifiedImport => isValidImportSpec(n.importSpec)
    // this case should never occur as these nodes should get converted in the parse postprocessing step
    case n: PImplicitQualifiedImport =>
      message(n, s"Explicit qualifier could not be derived") ++ isValidImportSpec(n.importSpec)
  }

  private def isValidImportSpec: PFunctionSpec => Messages = {
    case s@ PFunctionSpec(_, preserves, posts, terminationMeasures, isPure, isTrusted) =>
      val validCond = preserves.isEmpty && posts.isEmpty && terminationMeasures.isEmpty && !isPure && !isTrusted
      error(s, s"Only preconditions can be specified in specifications for imported packages", !validCond)
  }
}
