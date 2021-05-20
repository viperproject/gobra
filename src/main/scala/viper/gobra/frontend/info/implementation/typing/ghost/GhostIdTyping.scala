// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend.{PExpression, PFieldDecl, PIdnNode, PMatchAdt}
import viper.gobra.frontend.info.base.SymbolTable.{AdtClause, AdtDestructor, AdtDiscriminator, BoundVariable, BuiltInFPredicate, BuiltInMPredicate, GhostRegular, MatchVariable, Predicate}
import viper.gobra.frontend.info.base.Type.{AdtClauseT, AssertionT, FunctionT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

import scala.annotation.unused

trait GhostIdTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostRegular(entity: GhostRegular, @unused id: PIdnNode): ValidityMessages = entity match {
    case _: BoundVariable => LocalMessages(noMessages)
    case predicate: Predicate => unsafeMessage(! {
      predicate.args.forall(wellDefMisc.valid)
    })
    case _: BuiltInFPredicate | _: BuiltInMPredicate => LocalMessages(noMessages)
    case c: AdtClause => unsafeMessage(! {
      c.decl.args.forall(decls => decls.fields.forall {case PFieldDecl(_, typ) => wellDefAndType.valid(typ)})
    })
    case c: AdtDestructor => wellDefAndType(c.decl.typ)
    case _: AdtDiscriminator => LocalMessages(noMessages)
    case _: MatchVariable => LocalMessages(noMessages)
  }

  private[typing] def ghostEntityType(entity: GhostRegular, @unused id: PIdnNode): Type = entity match {

    case x: BoundVariable => typeSymbType(x.decl.typ)
    case predicate: Predicate => FunctionT(predicate.args map predicate.context.typ, AssertionT)
    case BuiltInFPredicate(tag, _, _) => tag.typ(config)
    case BuiltInMPredicate(tag, _, _) => tag.typ(config)
    case AdtClause(decl, adtDecl, context) => {
      AdtClauseT(
        decl.args.flatMap(_.fields).map(f => f.id.name -> context.symbType(f.typ)).toMap,
        decl,
        adtDecl,
        context
      )
    }
    case MatchVariable(decl, p, context) => p match {
      case PMatchAdt(clause, fields) => {
        val argTypeWithIndex = symbType(clause).asInstanceOf[AdtClauseT].decl.args.flatMap(_.fields).map(_.typ).zipWithIndex
        val fieldsWithIndex = fields.zipWithIndex
        val fieldIndex = fieldsWithIndex.iterator.find(e => e._1 == decl).get._2
        context.symbType(argTypeWithIndex.iterator.find(e => e._2 == fieldIndex).get._1)
      }
      case e: PExpression => exprType(e)
      case _ => violation("untypable")
    }
    case _ => violation("untypable")
  }
}
