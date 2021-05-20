// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{BuiltInMPredicate, GhostTypeMember, MPredicateImpl, MPredicateSpec, MethodSpec}
import viper.gobra.frontend.info.base.Type.{AdtClauseT, AssertionT, BooleanT, FunctionT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation
import viper.gobra.util.Violation.violation

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case PBoundVariable(_, _) => noMessages
    case PTrigger(exprs) => exprs.flatMap(isPureExpr)
    case PExplicitGhostParameter(_) => noMessages
    case p: PPredConstructorBase => p match {
      case PFPredBase(id) => entity(id) match {
        case _: SymbolTable.FPredicate | _: SymbolTable.BuiltInFPredicate => noMessages
        case _ => error(p, s"identifier $id does not identify a predicate")
      }
      case base: PDottedBase => resolve(base.recvWithId) match {
        case Some(_: ap.Predicate | _: ap.ReceivedPredicate | _: ap.ImplicitlyReceivedInterfacePredicate) => noMessages
        case Some(_: ap.PredicateExpr) => noMessages
        case _ => error(base.recvWithId, s"invalid base $base for predicate constructor")
      }
    }
    case n: PMethodImplementationProof => // TODO: check that body has the right shape
      wellDefIfPureMethodImplementationProof(n) ++ (
        entity(n.id) match {
          case _: MethodSpec => noMessages // TODO: check that the implementation proof has the right signature
          case e => Violation.violation(s"expected a method signature of an interface, but got $e")
        }
      )
    case _ : PAdtClause => noMessages

    case m: PMatchPattern => m match {
      case PMatchAdt(clause, fields) => symbType(clause) match {
        case t: AdtClauseT => {
          val fieldTypes = fields map typ
          val clauseTypes = t.decl.args.flatMap(f => f.fields).map(f => symbType(f.typ))
          fieldTypes.zip(clauseTypes).flatMap(a => assignableTo.errors(a)(m))
        }
        case _ => violation("Pattern matching only works on ADT Literals")
      }
      case PMatchValue(lit) => isPureExpr(lit)
      case _ => noMessages
    }

    case _ : PMatchStmtCase => noMessages
    case _ : PMatchExpCase  => noMessages
    case _ : PMatchExpDefault => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case PBoundVariable(_, typ) => typeSymbType(typ)
    case PTrigger(_) => BooleanT
    case PExplicitGhostParameter(param) => miscType(param)
    case b: PPredConstructorBase => b match {
      case PDottedBase(recvWithId) => exprOrTypeType(recvWithId)
      case PFPredBase(id) => idType(id)
    }
    case _: PMethodImplementationProof => UnknownType
    case _: PAdtClause => UnknownType
    case exp: PMatchPattern => exp match {
      case PMatchBindVar(idn) => idType(idn)
      case PMatchAdt(clause, _) => symbType(clause)
      case PMatchValue(lit) => typ(lit)
      case w@PMatchWildcard() => wildcardMatchType(w)
    }
    case _: PMatchStmtCase => UnknownType
    case _: PMatchExpCase  => UnknownType
    case _: PMatchExpDefault => UnknownType
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case MPredicateSpec(decl, _, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case _: SymbolTable.GhostStructMember => ???
    case BuiltInMPredicate(tag, _, _) => tag.typ(config)
    case SymbolTable.AdtDestructor(decl, _, ctx) => ctx.symbType(decl.typ)
    case SymbolTable.AdtDiscriminator(_, _, _) => BooleanT
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

  private def wildcardMatchType(w: PMatchWildcard): Type = {
    w match {
      case tree.parent(p) => p match {
        case PMatchAdt(c, fields) => {
          val index = fields indexWhere {w eq _}
          val adtClauseT = underlyingType(typeSymbType(c)).asInstanceOf[AdtClauseT]
          val field = adtClauseT.decl.args.flatMap(f => f.fields)(index)
          typeSymbType(field.typ)
        }
        case p: PMatchExpCase => p match {
          case tree.parent(pa) => pa match {
            case PMatchExp(e, _) => exprType(e)
            case _ => ???
          }
          case _ => ???
        }
        case p: PMatchStmtCase => p match {
          case tree.parent(pa) => pa match {
            case PMatchStatement(e, _, _) => exprType(e)
            case _ => ???
          }
          case _ => ???
        }
        case _ => ???
      }

      case _ => ???
    }
  }

}
