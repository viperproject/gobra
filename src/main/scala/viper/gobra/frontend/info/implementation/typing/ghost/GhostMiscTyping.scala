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
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, FunctionT, PredT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation
import viper.gobra.util.Violation.violation

import scala.annotation.tailrec

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

    case n: PImplementationProofPredicateAlias =>
      n match {
        case tree.parent(ip: PImplementationProof) =>
          entity(n.left) match {
            case itfPred: MPredicateSpec =>
              resolve(n.right) match {
                case Some(implPred: ap.Predicate) =>
                  val expectedTyp = PredT(symbType(ip.subT) +: (itfPred.args map itfPred.context.typ))
                  val actualTyp = PredT(implPred.symb.args map implPred.symb.context.typ)
                  message(n.right,
                    s"Right-hand side must have signature $expectedTyp, but has signature $actualTyp",
                    cond = !identicalTypes(expectedTyp, actualTyp)
                  )

                case _ => error(n.right, "Right-hand side must be a predicate (without a receiver)")
              }
            case _ => error(n.left, "Left-hand side must be a predicate of the super type interface")
          }

        case _ => violation("Encountered ill-formed program AST")
      }

    case n: PMethodImplementationProof =>
      val validPureCheck = wellDefIfPureMethodImplementationProof(n)
      if (validPureCheck.nonEmpty) validPureCheck
      else {
        entity(n.id) match {
          case spec: MethodSpec =>
            // check that the signatures match
            val matchingSignature = {
              val implSig = FunctionT(n.args map miscType, miscType(n.result))
              val specSig = memberType(spec)
              failedProp(
                s"implementation proof and interface member have a different signature (should be '$specSig', but is $implSig nad ${implSig == specSig})",
                cond = !identicalTypes(implSig, specSig)
              )
            }
            // check that pure annotations match
            val matchingPure = failedProp(
              s"The pure annotation does not match with the pure annotation of the interface member",
              cond = n.isPure != spec.isPure
            )
            // check that the receiver has the method
            val receiverHasMethod = failedProp(
              s"The type ${n.receiver.typ} does not have member ${n.id}",
              cond = tryMethodLikeLookup(miscType(n.receiver), n.id).isEmpty
            )
            // check that the body has the right shape
            val rightShape = {
              n.body match {
                case None => failedProp("A method in an implementation proof must not be abstract")
                case Some((_, block)) =>

                  val expectedReceiverOpt = n.receiver match {
                    case _: PUnnamedParameter => None
                    case p: PNamedParameter => Some(PNamedOperand(PIdnUse(p.id.name)))
                    case PExplicitGhostParameter(_: PUnnamedParameter) => None
                    case PExplicitGhostParameter(p: PNamedParameter) => Some(PNamedOperand(PIdnUse(p.id.name)))
                  }

                  val expectedArgs = n.args.flatMap {
                    case p: PNamedParameter => Some(PNamedOperand(PIdnUse(p.id.name)))
                    case PExplicitGhostParameter(p: PNamedParameter) => Some(PNamedOperand(PIdnUse(p.id.name)))
                    case _ => None
                  }

                  if (expectedReceiverOpt.isEmpty || expectedArgs.size != n.args.size) {
                    failedProp("Receiver and arguments must be named so that they can be used in a call")
                  } else {
                    val expectedReceiver = expectedReceiverOpt.getOrElse(violation(""))
                    val expectedInvoke = PInvoke(PDot(expectedReceiver, n.id), expectedArgs)

                    if (n.isPure) {
                      block.nonEmptyStmts match {
                        case Vector(PReturn(Vector(ret))) =>
                          @tailrec
                          def validExpression(expr: PExpression): PropertyResult = expr match {
                            case invk: PInvoke =>
                              failedProp(s"The call must be $expectedInvoke", invk != expectedInvoke)
                            case f: PUnfolding => validExpression(f.op)
                            case _ => failedProp(s"only unfolding expressions and the call $expectedInvoke is allowed")
                          }

                          validExpression(ret)

                        case _ => successProp // already checked before
                      }
                    } else {
                      // the body can only contain fold, unfold, and the call

                      val expectedResults = n.result.outs flatMap {
                        case p: PNamedParameter => Some(PNamedOperand(PIdnUse(p.id.name)))
                        case PExplicitGhostParameter(p: PNamedParameter) => Some(PNamedOperand(PIdnUse(p.id.name)))
                        case _ => None
                      }
                      val expectedCall = {
                        if (n.result.outs.isEmpty) expectedInvoke
                        else if (n.result.outs.size != expectedResults.size) PReturn(Vector(expectedInvoke))
                        else PAssignment(Vector(expectedInvoke), expectedResults)
                      }
                      val expectedReturnWithCall = PReturn(Vector(expectedInvoke))
                      val expectedReturnSet = {
                        val emptyReturn = PReturn(Vector.empty)

                        if (n.result.outs.isEmpty) Set(emptyReturn)
                        else if (n.result.outs.size != expectedResults.size) Set(emptyReturn, expectedReturnWithCall)
                        else Set(emptyReturn, expectedReturnWithCall, PReturn(expectedResults))
                      }

                      lazy val lastStatement: PStatement = {
                        @tailrec
                        def aux(stmt: PStatement): PStatement = stmt match {
                          case seq: PSeq => aux(seq.nonEmptyStmts.last)
                          case block: PBlock => aux(block.nonEmptyStmts.last)
                          case s => s
                        }

                        aux(block)
                      }

                      var numOfImplemetationCalls = 0

                      def validStatements(stmts: Vector[PStatement]): PropertyResult =
                        PropertyResult.bigAnd(stmts.map {
                          case _: PUnfold | _: PFold | _: PAssert | _: PEmptyStmt => successProp
                          case _: PAssume | _: PInhale | _: PExhale => failedProp("Assume, inhale, and exhale are forbidden in implementation proofs")

                          case b: PBlock => validStatements(b.nonEmptyStmts)
                          case seq: PSeq => validStatements(seq.nonEmptyStmts)

                          case ass: PAssignment =>
                            // Right now, we only allow assignments that are used for the one call
                            expectedCall match {
                              case expectedCall: PAssignment =>
                                if (ass != expectedCall) {
                                  failedProp(s"The only allowed assignment is $expectedCall")
                                } else {
                                  numOfImplemetationCalls += 1
                                  successProp
                                }

                              case _ =>
                                val reason =
                                  if (n.result.outs.isEmpty) "Here, the method has no out-parameters."
                                  else "Here, not all out-parameters have a name, so they cannot be assigned to."
                                failedProp("An assignment must assign to all out-parameters. " + reason)
                            }

                          case PExpressionStmt(invk: PInvoke) =>
                            // An invoke must be the call to the implementation
                            // As a consequence, an invoke alone can only occur if there are no result parameters
                            if (invk == expectedInvoke) {
                              if (n.result.outs.nonEmpty) failedProp(s"The call '$invk' is missing the out-parameters")
                              else {
                                numOfImplemetationCalls += 1
                                successProp
                              }
                            } else failedProp(s"The only allowed call is $expectedInvoke")

                          case ret: PReturn =>
                            // there has to be at most one return at the end of the block
                            if (lastStatement != ret) {
                              failedProp("A return must be the last statement")
                            } else if (expectedReturnSet.contains(ret)) {
                              if (ret == expectedReturnWithCall) numOfImplemetationCalls += 1
                              successProp
                            } else failedProp(s"A return must be one of $expectedReturnSet")

                          case _ => failedProp("Only fold, unfold, assert, and one call to the implementation are allowed")
                        })

                      val validStatementsResult = validStatements(block.nonEmptyStmts).distinct
                      val noTooManyCalls = {
                        if (numOfImplemetationCalls != 1) {
                          failedProp(s"There must be exactly one call to the implementation " +
                            s"(with results and arguments in the right order '$expectedCall')")
                        } else successProp
                      }

                      validStatementsResult and noTooManyCalls
                    }
                  }
              }
            }

            (matchingSignature and matchingPure and receiverHasMethod and rightShape)
              .asReason(n, "invalid method of an implementation proof")

          case e => Violation.violation(s"expected a method signature of an interface, but got $e")
        }
      }
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
    case _: PImplementationProofPredicateAlias => UnknownType
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case MPredicateSpec(decl, _, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case _: SymbolTable.GhostStructMember => ???
    case BuiltInMPredicate(tag, _, _) => tag.typ(config)
  }

  implicit lazy val wellDefSpec: WellDefinedness[PSpecification] = createWellDef {
    case PFunctionSpec(pres, posts,terminationMeasure, _) =>
      pres.flatMap(assignableToSpec) ++ posts.flatMap(assignableToSpec) ++
      pres.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode)) ++
      (terminationMeasure match {
        case Some(measure) =>
          measure match {
            case PTupleTerminationMeasure(tuple) => tuple.flatMap(p => comparableType.errors(exprType(p))(p) ++ isPureExpr(p) )
            case PConditionalMeasureCollection(tuple) => tuple.flatMap(p => p match {
              case  PConditionalMeasureExpression(tuple) => tuple match {
                case(expression,condition) => expression.flatMap (p => comparableType.errors(exprType (p))(p) ++ isPureExpr(p) )++ assignableToSpec(condition)
              }
              case PConditionalMeasureUnderscore(tuple) => tuple match {
                case (underscore,condition)=>assignableToSpec(condition)
              }
            })
          }
      })

    case PLoopSpec(invariants , terminationMeasure) => invariants.flatMap(assignableToSpec) ++
    (terminationMeasure match {
      case Some(measure) =>
        measure match {
          case PTupleTerminationMeasure(tuple) => tuple.flatMap(p => comparableType.errors(exprType(p))(p) ++ isPureExpr(p) )
          case PConditionalMeasureCollection(tuple) => tuple.flatMap(p => p match {
            case  PConditionalMeasureExpression(tuple) => tuple match {
              case(expression,condition) => expression.flatMap (p => comparableType.errors(exprType (p))(p) ++ isPureExpr(p) )++ assignableToSpec(condition)
            }
            case PConditionalMeasureUnderscore(tuple) => tuple match {
              case (underscore,condition)=>assignableToSpec(condition)
            }
          })
        }
    })
  }

  def assignableToSpec(e: PExpression): Messages = {
    isExpr(e).out ++ assignableTo.errors(exprType(e), AssertionT)(e) ++ isWeaklyPureExpr(e)
  }

  private def illegalPreconditionNode(n: PNode): Messages = {
    n match {
      case n: POld => message(n, s"old not permitted in precondition")
      case _ => noMessages
    }
  }

}
