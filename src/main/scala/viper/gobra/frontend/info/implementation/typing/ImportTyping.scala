// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.PackageResolver.RegularImport
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ImportTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefImport: WellDefinedness[PImport] = createWellDef { imp =>
    forceNonLazyImport(imp.importPath, imp)
    val qualifierMsgs = imp match {
      case _: PExplicitQualifiedImport => noMessages
      case _: PUnqualifiedImport => noMessages
      // this case should never occur as these nodes should get converted in the parse postprocessing step
      case n: PImplicitQualifiedImport => message(n, s"Explicit qualifier could not be derived")
    }
    val preHasOldExps = hasOldExpression(imp.importPres)
    qualifierMsgs ++ preHasOldExps
  }

  // This method forces a package to be processed non-lazily - every import can cause side effects,
  // and thus, every package mentioned in the source code must be analysed, even if is not used.
  // If this method is not called, a package is only processed if there are accesses to any member
  // declared in the package. This method is a quick solution that avoids larger refactorings
  // in the type-checker to perform imports non-lazily.
  // TODO: check if we can make this lazy
  private def forceNonLazyImport(importPath: String, errNode: PNode): Unit = {
    val abstractImport = RegularImport(importPath)
    getTypeChecker(abstractImport, errNode)
  }
}