// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PBlock, PCodeRootWithResult, PExplicitGhostMember, PFPredicateDecl, PFunctionDecl, PFunctionSpec, PGhostMember, PImplementationProof, PMPredicateDecl, PMethodDecl, PMethodImplementationProof, PParameter, PReturn, PVariadicType, PWithBody}
import viper.gobra.frontend.info.base.Type.AssertionT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMemberTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMember(member: PGhostMember): Messages = member match {
    case PExplicitGhostMember(_) => noMessages

    case n@ PFPredicateDecl(_, args, body) =>
      body.fold(noMessages)(b => assignableTo.errors(exprType(b), AssertionT)(n)) ++ nonVariadicArguments(args)

    case n@ PMPredicateDecl(_, receiver, args, body) =>
      body.fold(noMessages)(b => assignableTo.errors(exprType(b), AssertionT)(n)) ++
        isClassType.errors(miscType(receiver))(member) ++
        nonVariadicArguments(args)

    case ip: PImplementationProof =>
      error(ip, s"${ip.subT} does not implement ${ip.superT}", !goImplements(symbType(ip.subT), symbType(ip.superT)))
  }

  private[typing] def wellDefIfPureMethod(member: PMethodDecl): Messages = {

    if (member.spec.isPure) {
      isSingleResultArg(member) ++
        isSinglePureReturnExpr(member) ++
        isPurePostcondition(member.spec) ++
        nonVariadicArguments(member.args)
    } else {
      wellDefVariadicArgs(member.args)
    }
  }

  private[typing] def wellDefIfPureMethodImplementationProof(implProof: PMethodImplementationProof): Messages = {
    if (implProof.isPure) {
      isSinglePureReturnExpr(implProof) // all other checks are taken care of by super implementation
    } else noMessages
  }

  private[typing] def wellDefIfPureFunction(member: PFunctionDecl): Messages = {
    if (member.spec.isPure) {
      isSingleResultArg(member) ++
        isSinglePureReturnExpr(member) ++
        isPurePostcondition(member.spec) ++
        nonVariadicArguments(member.args)
    } else {
      wellDefVariadicArgs(member.args)
    }
  }

  private def isSingleResultArg(member: PCodeRootWithResult): Messages = {
    error(member, "For now, pure methods and pure functions must have exactly one result argument", member.result.outs.size != 1)
  }

  private def isSinglePureReturnExpr(member: PWithBody): Messages = {
    member.body match {
      case Some((_, b: PBlock)) => isPureBlock(b)
      case None => noMessages
      case Some(b) => error(member, s"For now, the body of a pure method or pure function is expected to be a single return with a pure expression, got $b instead")
    }
  }

  private def isPureBlock(block: PBlock): Messages = {
    block.nonEmptyStmts match {
      case Vector(PReturn(Vector(ret))) => isPureExpr(ret)
      case b => error(block, s"For now, the body of a pure block is expected to be a single return with a pure expression, got $b instead")
    }
  }

  private def isPurePostcondition(spec: PFunctionSpec): Messages = spec.posts flatMap isPureExpr

  private def nonVariadicArguments(args: Vector[PParameter]): Messages = args.flatMap {
    p: PParameter => error(p, s"Pure members cannot have variadic arguments, but got $p", p.typ.isInstanceOf[PVariadicType])
  }

  private def wellDefVariadicArgs(args: Vector[PParameter]): Messages =
    args.dropRight(1).flatMap {
      p => error(p, s"Only the last argument can be variadic, got $p instead", p.typ.isInstanceOf[PVariadicType])
    }

}
