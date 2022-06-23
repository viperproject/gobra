// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl => wellDefVariadicArgs(n.args) ++ wellDefIfPureFunction(n)
    case m: PMethodDecl => wellDefVariadicArgs(m.args) ++ isReceiverType.errors(miscType(m.receiver))(member) ++ wellDefIfPureMethod(m)
    case b: PConstDecl => b.specs.flatMap(wellDefConstSpec)
    case s: PActualStatement => wellDefStmt(s).out
  }

  private def wellDefConstSpec(spec: PConstSpec): Messages = {
    val hasInitExpr = error(spec, s"missing init expr for ${spec.left}", spec.right.isEmpty)
    lazy val canAssignInitExpr = error(
      spec,
      s"${spec.right} cannot be assigned to ${spec.left}",
      !multiAssignableTo(spec.left.map(typ), spec.right.map(typ))
    )
    lazy val constExprMsgs = spec.right.flatMap(wellDefIfConstExpr)
    // helps producing less redundant error messages
    if (hasInitExpr.nonEmpty) hasInitExpr else if (canAssignInitExpr.nonEmpty) canAssignInitExpr else constExprMsgs
  }

  private[typing] def wellDefVariadicArgs(args: Vector[PParameter]): Messages =
    args.dropRight(1).flatMap {
      p => error(p, s"Only the last argument can be variadic, got $p instead", p.typ.isInstanceOf[PVariadicType])
    }
}
