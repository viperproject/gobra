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
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, FunctionT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation

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

    case ax: PDomainAxiom =>
      assignableTo.errors(exprType(ax.exp), BooleanT)(ax) ++ isPureExpr(ax.exp)

    case f: PDomainFunction =>
      error(f, s"Uninterpreted functions must have exactly one return argument", f.result.outs.size != 1) ++
        nonVariadicArguments(f.args)

    case n: PMethodImplementationProof => // TODO: check that body has the right shape
      wellDefIfPureMethodImplementationProof(n) ++ (
        entity(n.id) match {
          case _: MethodSpec => noMessages // TODO: check that the implementation proof has the right signature
          case e => Violation.violation(s"expected a method signature of an interface, but got $e")
        }
      )
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case PBoundVariable(_, typ) => typeSymbType(typ)
    case PTrigger(_) => BooleanT
    case PExplicitGhostParameter(param) => miscType(param)
    case b: PPredConstructorBase => b match {
      case PDottedBase(recvWithId) => exprOrTypeType(recvWithId)
      case PFPredBase(id) => idType(id)
    }
    case _: PDomainAxiom | _: PDomainFunction => UnknownType
    case _: PMethodImplementationProof => UnknownType
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case MPredicateSpec(decl, _, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case _: SymbolTable.GhostStructMember => ???
    case BuiltInMPredicate(tag, _, _) => tag.typ(config)
  }




  implicit lazy val wellDefSpec: WellDefinedness[PSpecification] = createWellDef {
    case n@ PFunctionSpec(pres, posts,terminationMeasure, _) =>
      pres.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
        posts.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
      pres.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode))++
        (terminationMeasure match {
        case Some(measure) =>
          measure match {
            case PTupleTerminationMeasure(tuple) => tuple.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n))
            case PConditionalMeasureCollection(tuple) => tuple.flatMap(p => p match {
              case  PConditionalMeasureExpression(tuple) => tuple match {
             case(expression,condition) => expression.flatMap (p => assignableTo.errors (exprType (p), AssertionT) (n) )++ assignableTo.errors (exprType (condition), AssertionT) (n)
              }
              case PConditionalMeasureUnderscore(tuple) => tuple match {
                case (underscore,condition)=>assignableTo.errors (exprType (condition), AssertionT) (n)
              }
            })
            }
      })



    case n@ PLoopSpec(invariants,terminationMeasure) =>
      invariants.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
        (terminationMeasure match {
          case Some(measure) =>
            measure match {
              case PTupleTerminationMeasure(tuple) => tuple.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n))
              case PConditionalMeasureCollection(tuple) => tuple.flatMap(p => p match {
                case  PConditionalMeasureExpression(tuple) => tuple match {
                  case(expression,condition) => expression.flatMap (p => assignableTo.errors (exprType (p), AssertionT) (n) )++ assignableTo.errors (exprType (condition), AssertionT) (n)
                }
                case PConditionalMeasureUnderscore(tuple) => tuple match {
                  case (underscore,condition)=>assignableTo.errors (exprType (condition), AssertionT) (n)
                }
              })


            }
        })
  }

  private def illegalPreconditionNode(n: PNode): Messages = {
    n match {
      case n: POld => message(n, s"old not permitted in precondition")
      case _ => noMessages
    }
  }

}
