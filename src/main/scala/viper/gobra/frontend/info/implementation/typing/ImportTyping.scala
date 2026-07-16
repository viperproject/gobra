// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{error, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ImportTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefImport: WellDefinedness[PImport] = createWellDef { imp =>
    val qualifierMsgs = imp match {
      case _: PExplicitQualifiedImport => noMessages
      case _: PUnqualifiedImport => noMessages
      // this case should never occur as these nodes should get converted in the parse postprocessing step
      case n: PImplicitQualifiedImport => message(n, s"Explicit qualifier could not be derived")
    }
    val preHasOldExps = hasOldExpression(imp.importPres)
    val importPresAllowed = error(imp, "Import preconditions are disallowed by default. " +
      "Pass the flag --experimentalFriendClauses to allow it. This feature may change in the future.",
      !config.enableExperimentalFriendClauses && imp.importPres.nonEmpty)
    qualifierMsgs ++ preHasOldExps ++ importPresAllowed
  }
}