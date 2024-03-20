// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation

trait GhostTypeTyping extends BaseTyping { this : TypeInfoImpl =>

  private[typing] def wellDefGhostType(typ : PGhostType) : Messages = typ match {
    case PSequenceType(elem) => isType(elem).out
    case PSetType(elem) => isType(elem).out
    case PMultisetType(elem) => isType(elem).out
    case PMathematicalMapType(key, value) => isType(key).out ++ isType(value).out
    case POptionType(elem) => isType(elem).out
    case PGhostPointerType(elem) => isType(elem).out
    case n: PGhostSliceType => isType(n.elem).out

    case _: PDomainType => noMessages
    case n: PAdtType => n match {
      case tree.parent(_: PTypeDef) =>
        val t = adtSymbType(n)
        adtConstructorSet(t).errors(n) ++ adtMemberSet(t).errors(n)

      case _ => error(n, "Adt types are only allowed within type declarations.")
    }
  }

  private[typing] def ghostTypeSymbType(typ : PGhostType) : Type = typ match {
    case PSequenceType(elem) => SequenceT(typeSymbType(elem))
    case PSetType(elem) => SetT(typeSymbType(elem))
    case PMultisetType(elem) => MultisetT(typeSymbType(elem))
    case PMathematicalMapType(keys, values) => MathMapT(typeSymbType(keys), typeSymbType(values))
    case POptionType(elem) => OptionT(typeSymbType(elem))
    case PGhostPointerType(elem) => GhostPointerT(typeSymbType(elem))
    case PGhostSliceType(elem) => GhostSliceT(typeSymbType(elem))
    case t: PDomainType => DomainT(t, this)
    case a: PAdtType => adtSymbType(a)
  }

  /** Requires that the parent of a is PTypeDef. */
  private def adtSymbType(a: PAdtType): Type = {
    a match {
      case tree.parent(decl: PTypeDef) =>
        val clauses = a.clauses.map { clause =>
          val fields = clause.args.flatMap(_.fields.map(f => f.id.name -> typeSymbType(f.typ)))
          AdtClauseT(clause.id.name, fields, clause, decl, this)
        }
        AdtT(clauses, decl, this)

      case _ => Violation.violation(s"$a is not within a type declaration")
    }
  }
}
