// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{BuiltInMPredicate, GhostTypeMember, MPredicateImpl, MPredicateSpec}
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, FunctionT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case PBoundVariable(_, _) => noMessages
    case PTrigger(exprs) => exprs.flatMap(isPureExpr)
    case PExplicitGhostParameter(_) => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case PBoundVariable(_, typ) => typeSymbType(typ)
    case PTrigger(_) => BooleanT
    case PExplicitGhostParameter(param) => miscType(param)
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case MPredicateSpec(decl, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case _: SymbolTable.GhostStructMember => ???
    case BuiltInMPredicate(tag, _, _) => tag.typ(config)
  }

  implicit lazy val wellDefSpec: WellDefinedness[PSpecification] = createWellDef {
    case n@ PFunctionSpec(pres, posts, _) =>
      pres.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
        posts.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
      pres.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode))

    case n@ PLoopSpec(invariants) =>
      invariants.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n))
  }

  private def illegalPreconditionNode(n: PNode): Messages = {
    n match {
      case n: POld => message(n, s"old not permitted in precondition")
      case _ => noMessages
    }
  }

}
