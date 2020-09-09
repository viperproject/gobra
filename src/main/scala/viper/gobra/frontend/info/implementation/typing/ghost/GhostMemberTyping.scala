// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend.{PBlock, PExplicitGhostMember, PFPredicateDecl, PFunctionDecl, PGhostMember, PMPredicateDecl, PMethodDecl, PReturn}
import viper.gobra.frontend.info.base.Type.AssertionT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMemberTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMember(member: PGhostMember): Messages = member match {
    case PExplicitGhostMember(_) => noMessages

    case n@ PFPredicateDecl(id, args, body) =>
      body.fold(noMessages)(b => assignableTo.errors(exprType(b), AssertionT)(n))

    case n@ PMPredicateDecl(id, receiver, args, body) =>
      body.fold(noMessages)(b => assignableTo.errors(exprType(b), AssertionT)(n)) ++
        isClassType.errors(miscType(receiver))(member)
  }

  private[typing] def wellDefIfPureMethod(member: PMethodDecl): Messages = {

    if (member.spec.isPure) {
      message(member, "expected the same pre and postcondition", member.spec.pres != member.spec.posts) ++
      message(member, "For now, pure methods must have exactly one result argument", member.result.outs.size != 1) ++
        (member.body match {
          case Some(b: PBlock) => isPureBlock(b)
          case None => noMessages
          case Some(b) => message(member, s"For now the body of a pure method is expected to be a single return with a pure expression, got $b instead")
        })
    } else noMessages
  }

  private[typing] def wellDefIfPureFunction(member: PFunctionDecl): Messages = {
    if (member.spec.isPure) {
      message(member, "expected the same pre and postcondition", member.spec.pres != member.spec.posts) ++
        message(member, "For now, pure functions must have exactly one result argument", member.result.outs.size != 1) ++
        (member.body match {
          case Some(b: PBlock) => isPureBlock(b)
          case None => noMessages
          case Some(b) => message(member, s"For now the body of a pure method is expected to be a single return with a pure expression, got $b instead")
        })
    } else noMessages
  }

  private def isPureBlock(block: PBlock): Messages = {
    block.nonEmptyStmts match {
      case Vector(PReturn(Vector(ret))) => isPureExpr(ret)
      case b => message(block, s"For now the body of a pure block is expected to be a single return with a pure expression, got $b instead")
    }
  }
}
