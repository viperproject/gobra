// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend.{PExpression, PFieldDecl, PIdnNode, PMatchAdt}
import viper.gobra.frontend.info.base.SymbolTable.{AdtClause, AdtDestructor, AdtDiscriminator, BoundVariable, BuiltInFPredicate, BuiltInMPredicate, DomainFunction, GhostRegular, MatchVariable, Predicate}
import viper.gobra.frontend.info.base.Type.{AdtClauseT, AssertionT, FunctionT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

import scala.annotation.unused

trait GhostIdTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostRegular(entity: GhostRegular, @unused id: PIdnNode): ValidityMessages = entity match {
    case _: BoundVariable => LocalMessages(noMessages)
    case predicate: Predicate => unsafeMessage(! {
      predicate.args.forall(wellDefMisc.valid)
    })
    case f: DomainFunction => unsafeMessage(! {
      f.result.outs.size == 1 && f.args.forall(wellDefMisc.valid) && wellDefMisc.valid(f.result)
    })
    case c: AdtClause => unsafeMessage(! {
      c.decl.args.forall(decls => decls.fields.forall { case PFieldDecl(_, typ) => wellDefAndType.valid(typ) })
    })
    case c: AdtDestructor => wellDefAndType(c.decl.typ)
    case _: AdtDiscriminator => LocalMessages(noMessages)
    case MatchVariable(_, p, context) => p match {
      case PMatchAdt(clause, _) =>
        unsafeMessage(!context.symbType(clause).isInstanceOf[AdtClauseT])
      case _: PExpression => LocalMessages(noMessages)
      case _ => unsafeMessage(true)
    }
    case _: BuiltInFPredicate | _: BuiltInMPredicate => LocalMessages(noMessages)
  }

  private[typing] def ghostEntityType(entity: GhostRegular, @unused id: PIdnNode): Type = entity match {

    case x: BoundVariable => typeSymbType(x.decl.typ)
    case predicate: Predicate => FunctionT(predicate.args map predicate.context.typ, AssertionT)
    case func: DomainFunction => FunctionT(func.args map func.context.typ, func.context.typ(func.result.outs.head))

    case c: AdtClause =>
      val fields = c.fields.map(f => f.id.name -> c.context.symbType(f.typ))
      AdtClauseT(c.getName, fields, c.decl, c.typeDecl, c.context)

    case MatchVariable(decl, p, context) => p match {
      case PMatchAdt(clause, fields) =>
        val clauseT = context.symbType(clause)
        clauseT match {
          case clauseT: AdtClauseT => clauseT.typeAt(fields.indexOf(decl))
          // the following case is possible, e.g., if the clause is not well-defined
          // and, thus, `clauseT` is `UnknownType`:
          case _ => UnknownType
        }

      case e: PExpression => context.typ(e)
      case _ => violation("untypeable")
    }

    case BuiltInFPredicate(tag, _, _) => typ(tag)
    case BuiltInMPredicate(tag, _, _) => typ(tag)
    case _ => violation("untypeable")
  }
}
