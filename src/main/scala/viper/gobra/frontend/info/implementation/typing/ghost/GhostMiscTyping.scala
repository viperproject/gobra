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
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, FunctionT, InternalTupleT, PredT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation
import viper.gobra.util.Violation.violation

import scala.annotation.tailrec

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case PClosureNamedDecl(id, PClosureDecl(args, _, _, _)) => wellDefVariadicArgs(args) ++
      id.fold(noMessages)(id => wellDefID(id).out)
    case c@PClosureSpecInstance(id, _) => entity(id) match {
      case f: SymbolTable.Function => wellDefClosureSpecInstanceParams(c, f.args)
      case l: SymbolTable.Closure =>
        if (c.params.isEmpty || capturedVariables(l.decl.decl).isEmpty) wellDefClosureSpecInstanceParams(c, l.args)
        else error(c, s"cannot derive a parametrized closure spec instance from a literal that captures variables")
      case _ => error(id, s"identifier $id does not identify a user-defined function or function literal")
    }
    case PClosureSpecParameter(_, exp) => isExpr(exp).out
    case _: PClosureSpecParameterKey => noMessages
    case PBoundVariable(_, _) => noMessages
    case PTrigger(exprs) => exprs.flatMap(isWeaklyPureExpr)
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
    case PClosureNamedDecl(_, decl) => miscType(decl)
    case PClosureSpecInstance(func, params) =>
      val fType = idType(func).asInstanceOf[FunctionT]
      if (params.forall(_.key.isEmpty)) fType.copy(args = fType.args.drop(params.size))
      else {
        val f = entity(func).asInstanceOf[SymbolTable.Function]
        val paramSet = params.map(e => e.key.get.name).toSet
        fType.copy(args = (f.decl.args zip fType.args).filter{
          case (PNamedParameter(aId, _), _) if paramSet.contains(aId.name) => false
          case _ => true
        }.map(_._2))
      }
    case PClosureSpecParameter(_, exp) => exprType(exp)
    case _: PClosureSpecParameterKey => UnknownType
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
    case BuiltInMPredicate(tag, _, _) => typ(tag)
  }

  implicit lazy val wellDefSpec: WellDefinedness[PSpecification] = createWellDef {
    case n@ PFunctionSpec(pres, preserves, posts, terminationMeasures, _, _) =>
      pres.flatMap(assignableToSpec) ++ preserves.flatMap(assignableToSpec) ++ posts.flatMap(assignableToSpec) ++
      preserves.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode)) ++
      pres.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode)) ++
      terminationMeasures.flatMap(wellDefTerminationMeasure) ++
      // if has conditional clause, all clauses must be conditional
      // can only have one non-conditional clause
      error(n, "Specifications can either contain one non-conditional termination measure or multiple conditional-termination measures.", terminationMeasures.length > 1 && !terminationMeasures.forall(isConditional)) ++
      // measures must have the same type
      error(n, "Termination measures must all have the same type.", !hasSameMeasureType(terminationMeasures))

    case n@ PLoopSpec(invariants, terminationMeasure) =>
      invariants.flatMap(assignableToSpec) ++ terminationMeasure.toVector.flatMap(wellDefTerminationMeasure) ++
        error(n, "Termination measures of loops cannot be conditional.", terminationMeasure.exists(isConditional))
  }

  private def wellDefTerminationMeasure(measure: PTerminationMeasure): Messages = measure match {
    case PTupleTerminationMeasure(tuple, cond) =>
      tuple.flatMap(p => comparableType.errors(exprType(p))(p) ++ isWeaklyPureExpr(p)) ++
        cond.toVector.flatMap(p => assignableToSpec(p) ++ isPureExpr(p))
    case PWildcardMeasure(cond) =>
      cond.toVector.flatMap(p => assignableToSpec(p) ++ isPureExpr(p))
  }

  private def wellDefClosureSpecInstanceParams(c: PClosureSpecInstance, fArgs: Vector[PParameter]): Messages = c match {
    case PClosureSpecInstance(fName, ps) if ps.size > fArgs.size =>
      error(c, s"spec instance $c has too many parameters (more than the arguments of function $fName)")
    case PClosureSpecInstance(_, ps) if ps.forall(_.key.isEmpty) =>
      (ps zip fArgs) flatMap { case (p, a) => assignableTo.errors((exprType(p.exp), miscType(a)))(p.exp) }
    case PClosureSpecInstance(fName, ps) if ps.forall(_.key.nonEmpty) =>
      val argsMap = fArgs.flatMap { case a@PNamedParameter(id, _) => Vector(id.name -> a) }.toMap
      val wellDefIfNoDuplicateParams = {
        var pSet = Set[String]()
        ps flatMap(p => {
          val err = if (pSet.contains(p.key.get.name)) error(p.key.get, s"duplicate parameter key ${p.key.get}") else noMessages
          pSet = pSet + p.key.get.name
          err
        })
      }
      val wellDefIfCanAssignParams = ps flatMap { p => argsMap.get(p.key.get.name) match {
        case Some(a: PNamedParameter) => assignableTo.errors((exprType(p.exp), miscType(a)))(p.exp)
        case _ => error(p.key.get, s"could not find argument ${p.key.get} in the function $fName")
      }}
      wellDefIfNoDuplicateParams ++ wellDefIfCanAssignParams
    case _ => error(c, "mixture of 'field:expression' and 'expression' elements in closure spec instance")
  }

  private def isConditional(measure: PTerminationMeasure): Boolean = measure match {
    case PTupleTerminationMeasure(_, cond) => cond.nonEmpty
    case PWildcardMeasure(cond) => cond.nonEmpty
  }

  private def hasSameMeasureType(measures: Vector[PTerminationMeasure]): Boolean = {
    val tupleMeasureTypes =
      measures.filter(_.isInstanceOf[PTupleTerminationMeasure])
              .map(_.asInstanceOf[PTupleTerminationMeasure].tuple.map(typ))
    tupleMeasureTypes forall (_.equals(tupleMeasureTypes.head))
  }

  def assignableToSpec(e: PExpression): Messages = {
    isExpr(e).out ++ assignableTo.errors(exprType(e), AssertionT)(e) ++ isWeaklyPureExpr(e)
  }

  private def illegalPreconditionNode(n: PNode): Messages = {
    n match {
      case n@ (_: POld | _: PLabeledOld) => message(n, s"old not permitted in precondition")
      case n@ (_: PBefore) => message(n, s"old not permitted in precondition")
      case _ => noMessages
    }
  }
}
