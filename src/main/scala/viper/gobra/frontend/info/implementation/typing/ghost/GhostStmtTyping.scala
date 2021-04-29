// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostStmtTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostStmt(stmt: PGhostStatement): Messages = stmt match {
    case n@PExplicitGhostStatement(s) => error(n, "ghost error: expected ghostifiable statement", !s.isInstanceOf[PGhostifiableStatement])
    case PAssert(exp) => assignableToSpec(exp)
    case PExhale(exp) => assignableToSpec(exp)
    case PAssume(exp) => assignableToSpec(exp)
    case PInhale(exp) => assignableToSpec(exp)
    case PFold(acc) => wellDefFoldable(acc)
    case PUnfold(acc) => wellDefFoldable(acc)
  }

  private def wellDefFoldable(acc: PPredicateAccess): Messages =
    resolve(acc.pred) match {
      case Some(_: ap.PredExprInstance) =>
        error(
          acc,
          s"expected a predicate constructor, but got ${acc.pred.base}",
          !acc.pred.base.isInstanceOf[PPredConstructor])
      case _ => noMessages
    }
}
