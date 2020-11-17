// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl => wellDefIfPureFunction(n)
    case m: PMethodDecl => isReceiverType.errors(miscType(m.receiver))(member) ++ wellDefIfPureMethod(m)

    case c: PConstDecl =>
      val idenNumMsgs = message(c, s"number of identifiers does not match the number of expressions", c.left.length != c.right.length)
      val constExprMsgs = c.right.flatMap(wellDefIfConstExpr)
      idenNumMsgs ++ constExprMsgs
    case s: PActualStatement => wellDefStmt(s).out
  }
}
