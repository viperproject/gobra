// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend.{PClosureImplProof, AstPattern => ap, _}
import viper.gobra.frontend.info.base.Type.BooleanT
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostStmtTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostStmt(stmt: PGhostStatement): Messages = stmt match {
    case n@PExplicitGhostStatement(s) => error(n, "ghost error: expected ghostifiable statement", !s.isInstanceOf[PGhostifiableStatement])
    case PAssert(exp) => assignableToSpec(exp)
    case PRefute(exp) => assignableToSpec(exp)
    case PExhale(exp) => assignableToSpec(exp)
    case PAssume(exp) => assignableToSpec(exp)
    case PInhale(exp) => assignableToSpec(exp)
    case PFold(acc) => wellDefFoldable(acc)
    case PUnfold(acc) => wellDefFoldable(acc)
    case n: PAssertBy =>
      isExpr(n.exp).out ++
        isPureExpr(n.exp) ++
        comparableTypes.errors(exprType(n.exp), BooleanT)(n.exp)
    case POpenDupPkgInv() =>
      val occursInInitMember = isEnclosingMayInit(stmt)
      error(stmt, "Opening the package invariant in a function that may execute during initialization is not allowed.", occursInInitMember)
    case n@PPackageWand(wand, optBlock) => assignableToSpec(wand) ++
      error(n, "ghost error: expected ghostifiable statement", !optBlock.forall(_.isInstanceOf[PGhostifiableStatement]))
    case PApplyWand(wand) => assignableToSpec(wand)
    case n@PMatchStatement(exp, clauses, _) =>
      val mayInit = isEnclosingMayInit(n)
      clauses.flatMap(c => c.pattern match {
        case p: PMatchAdt => assignableTo.errors(miscType(p), exprType(exp), mayInit)(c)
        case _ => comparableTypes.errors((miscType(c.pattern), exprType(exp)))(c)
      }) ++ isPureExpr(exp)
  }

  private[typing] def wellDefFoldable(acc: PPredicateAccess): Messages = {
    def isAbstract(p: st.Predicate): Boolean = p match {
      case fp: st.FPredicate => fp.decl.body.isEmpty
      case mp: st.MPredicateImpl => mp.decl.body.isEmpty
      case _: st.MPredicateSpec =>
        // counter-intuitive: interface well-definedness will make sure that implementations implement the declared predicates
        false
    }

    resolve(acc.pred) match {
      case Some(_: ap.PredExprInstance) =>
        error(
          acc,
          s"expected a predicate constructor, but got ${acc.pred.base}",
          !acc.pred.base.isInstanceOf[PPredConstructor])
      case Some(ap.PredicateCall(pred, _)) => pred match {
        case p: ap.SymbolicPredicateKind => error(acc, s"abstract predicates are not foldable", isAbstract(p.symb))
        case p: ap.BuiltInPredicateKind => error(acc, s"abstract predicates are not foldable", p.symb.tag.isAbstract)
        case _: ap.PredExprInstance => error(acc, s"predicate expression calls are not foldable")
      }

      case _ => error(acc, s"unexpected predicate access")
    }
  }

  lazy val closureImplProofCallAttr: PClosureImplProof => PInvoke =
    attr[PClosureImplProof, PInvoke] { proof =>
      def findCallDescendent(n: PNode): Option[PInvoke] = n match {
        case call: PInvoke => resolve(call) match {
          case Some(_: ap.FunctionCall) => Some(call)
          case Some(_: ap.ClosureCall) => Some(call)
          case _ => None
        }
        case _ => tree.child(n).iterator.map(findCallDescendent).find(_.nonEmpty).flatten
      }

      findCallDescendent(proof).get
    }
}

