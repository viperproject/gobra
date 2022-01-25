// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostStmtTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostStmt(stmt: PGhostStatement): Messages = stmt match {
    case n@PExplicitGhostStatement(s) => error(n, "ghost error: expected ghostifiable statement", !s.isInstanceOf[PGhostifiableStatement])
    case PAssert(exp) => assignableToSpec(exp)
    case PExhale(exp) => assignableToSpec(exp)
    case PAssume(exp) => assignableToSpec(exp) ++ isPureExpr(exp)
    case PInhale(exp) => assignableToSpec(exp)
    case PFold(acc) => wellDefFoldable(acc)
    case PUnfold(acc) => wellDefFoldable(acc)
    case n@PPackageWand(wand, optBlock) => assignableToSpec(wand) ++
      error(n, "ghost error: expected ghostifiable statement", !optBlock.forall(_.isInstanceOf[PGhostifiableStatement]))
    case PApplyWand(wand) => assignableToSpec(wand)
  }

  private[typing] def wellDefFoldable(acc: PPredicateAccess): Messages = {
    def isNonAbstract(p: st.Predicate): Messages = p match {
      case fp: st.FPredicate => error(acc, s"abstract predicates are not foldable", fp.decl.body.isEmpty)
      case st.MPredicateImpl(decl, _) => error(acc, s"abstract predicates are not foldable", decl.body.isEmpty)
      case _: st.MPredicateSpec => noMessages // interface well-definedness will make sure that implementations implement the declared predicates
    }

    resolve(acc.pred) match {
      case Some(_: ap.PredExprInstance) =>
        error(
          acc,
          s"expected a predicate constructor, but got ${acc.pred.base}",
          !acc.pred.base.isInstanceOf[PPredConstructor])
      case Some(ap.PredicateCall(pred, _)) => pred match {
        case p: ap.SymbolicPredicateKind => isNonAbstract(p.symb)
        case p: ap.BuiltInPredicateKind => error(acc, s"abstract predicates are not foldable", p.symb.tag.isAbstract)
        case _: ap.PredExprInstance => error(acc, s"predicate expression calls are not foldable")
      }

      case _ => error(acc, s"unexpected predicate access")
    }
  }
}
