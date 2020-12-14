// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.AssertionT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostStmtTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostStmt(stmt: PGhostStatement): Messages = stmt match {
    case n@PExplicitGhostStatement(s) => error(n, "ghost error: expected ghostifiable statement", !s.isInstanceOf[PGhostifiableStatement])
    case PAssert(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case PExhale(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case PAssume(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case PInhale(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case PFold(_) => noMessages
    case PUnfold(_) => noMessages
  }
}
