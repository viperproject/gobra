// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, message, noMessages}
import viper.gobra.ast.frontend.PLabelNode.lhsLabel
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{BuiltInMPredicate, GhostTypeMember, MPredicateImpl, MPredicateSpec}
import viper.gobra.frontend.info.base.Type.{AdtClauseT, AssertionT, BooleanT, DeclaredT, FunctionT, PredT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation
import viper.gobra.util.Violation.violation

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case c@PClosureSpecInstance(id, _) => resolve(id) match {
      case Some(ap.Function(_, f)) => wellDefClosureSpecInstanceParams(c, f.args zip exprOrTypeType(id).asInstanceOf[FunctionT].args)
      case Some(ap.Closure(_, l)) => if (c.params.isEmpty || capturedLocalVariables(l.lit.decl).isEmpty)
        wellDefClosureSpecInstanceParams(c, l.args zip exprOrTypeType(id).asInstanceOf[FunctionT].args)
        else error(c, s"function literal ${l.lit.id.get} captures variables, so it cannot be used to derive a parametrized spec instance")
      case _ => error(id, s"identifier $id does not identify a user-defined function or function literal")
    }
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
      assignableTo.errors(exprType(ax.exp), BooleanT, false)(ax) ++ isPureExpr(ax.exp)

    case f: PDomainFunction =>
      error(f, s"Uninterpreted functions must have exactly one return argument", f.result.outs.size != 1) ++
        nonVariadicArguments(f.args)

    case _: PAdtClause => noMessages

    case m: PMatchPattern => m match {
      case PMatchAdt(clause, fields) => symbType(clause) match {
        case t: AdtClauseT =>
          val fieldTypes = fields.map(typ)
          val clauseFieldTypes = t.fields.map(_._2)
          val mayInit = isEnclosingMayInit(m)
          error(m, s"Expected ${clauseFieldTypes.size} patterns, but got ${fieldTypes.size}", clauseFieldTypes.size != fieldTypes.size) ++
            fieldTypes.zip(clauseFieldTypes).flatMap{case (a, b) => assignableTo.errors(a, b, mayInit)(m)}
        case _ => violation("Pattern matching only works on ADT Literals")
      }
      case PMatchValue(lit) => isPureExpr(lit)
      case _ => noMessages
    }

    case _: PMatchStmtCase => noMessages
    case _: PMatchExpCase => noMessages
    case _: PMatchExpDefault => noMessages

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

    case _: PBackendAnnotation => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case spec@PClosureSpecInstance(func, params) =>
      val fType = exprOrTypeType(func).asInstanceOf[FunctionT]
      if (spec.paramKeys.isEmpty) fType.copy(args = fType.args.drop(params.size))
      else {
        val f = resolve(func) match {
          case Some(ap.Function(_, f)) => f
          case Some(ap.Closure(_, c)) => c
          case _ => Violation.violation(s"expected a function or closure, but got $func")
        }
        val paramSet = spec.paramKeys.toSet
        fType.copy(args = (f.args zip fType.args).filter{
          case (PNamedParameter(id, _), _) if paramSet.contains(id.name) => false
          case (PExplicitGhostParameter(PNamedParameter(id, _)), _) if paramSet.contains(id.name) => false
          case _ => true
        }.map(_._2))
      }
    case PBoundVariable(_, typ) => typeSymbType(typ)
    case PTrigger(_) => BooleanT
    case PExplicitGhostParameter(param) => miscType(param)
    case b: PPredConstructorBase => b match {
      case PDottedBase(recvWithId) => exprOrTypeType(recvWithId)
      case PFPredBase(id) => idType(id)
    }
    case _: PDomainAxiom | _: PDomainFunction => UnknownType

    case _: PAdtClause => UnknownType
    case exp: PMatchPattern => exp match {
      case PMatchBindVar(idn) => idType(idn)
      case PMatchAdt(clause, _) =>
        symbType(clause) match {
          case t: AdtClauseT => t.declaredType
          case t => t
        }
      case PMatchValue(lit) => typ(lit)
      case w: PMatchWildcard => wildcardMatchType(w)
    }
    case _: PMatchStmtCase => UnknownType
    case _: PMatchExpCase => UnknownType
    case _: PMatchExpDefault => UnknownType

    case _: PImplementationProofPredicateAlias => UnknownType
    case _: PBackendAnnotation => UnknownType
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case MPredicateSpec(decl, _, ctx) => FunctionT(decl.args map ctx.typ, AssertionT)
    case _: SymbolTable.GhostStructMember => ???
    case dest: SymbolTable.AdtDestructor => dest.context.symbType(dest.decl.typ)
    case _: SymbolTable.AdtDiscriminator => BooleanT
    case const: SymbolTable.AdtClause => DeclaredT(const.typeDecl, const.context)
    case BuiltInMPredicate(tag, _, _) => typ(tag)
    case f: SymbolTable.DomainFunction => FunctionT(f.args map f.context.typ, f.context.typ(f.result))
  }

  implicit lazy val wellDefSpec: WellDefinedness[PSpecification] = createWellDef {
    case n@ PFunctionSpec(pres, preserves, posts, terminationMeasures, _, isPure, _, isOpaque, _) =>
      pres.flatMap(assignableToSpec) ++ preserves.flatMap(assignableToSpec) ++ posts.flatMap(assignableToSpec) ++
      preserves.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode)) ++
      pres.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode)) ++
      terminationMeasures.flatMap(wellDefTerminationMeasure) ++
      presFreeOfOld(n.pres) ++
      postsFreeOfOldIfPure(n.posts, isPure) ++
      // if has conditional clause, all clauses must be conditional
      // can only have one non-conditional clause
      error(n, "Specifications can either contain one non-conditional termination measure or multiple conditional-termination measures.", terminationMeasures.length > 1 && !terminationMeasures.forall(isConditional)) ++
      // measures must have the same type
      error(n, "Termination measures must all have the same type.", !hasSameMeasureType(terminationMeasures)) ++
      error(n, "Opaque can only be used in combination with pure.", isOpaque && !isPure)

    case n@ PLoopSpec(invariants, terminationMeasure) =>
      invariants.flatMap(assignableToSpec) ++ terminationMeasure.toVector.flatMap(wellDefTerminationMeasure) ++
        error(n, "Termination measures of loops cannot be conditional.", terminationMeasure.exists(isConditional))
  }

  private[typing] def hasOldExpressionExceptValidWand(n: PExpression): Boolean =
    (n +: allChildren(n)).exists {
      case _: POld => true
      case o: PLabeledOld if o.label.name == lhsLabel => !isEnclosingMagicWand(o)
      case _: PLabeledOld => true
      case _ => false
    }

  private def presFreeOfOld(pres: Vector[PExpression]): Messages = {
    pres.flatMap { pre =>
      error(pre, "old(_) expressions cannot occur in preconditions of functions and methods.", hasOldExpressionExceptValidWand(pre))
    }
  }

  private def postsFreeOfOldIfPure(posts: Vector[PExpression], isPure: Boolean): Messages = {
    if (isPure)
      posts.flatMap { post =>
        error(post, "old(_) expressions cannot occur in postconditions of pure functions.", hasOldExpressionExceptValidWand(post))
      }
    else
      noMessages
  }

  private def wellDefTerminationMeasure(measure: PTerminationMeasure): Messages = measure match {
    case PTupleTerminationMeasure(tuple, cond) =>
      tuple.flatMap(p => comparableType.errors(exprType(p))(p) ++ isWeaklyPureExpr(p)) ++
        cond.toVector.flatMap(p => assignableToSpec(p) ++ isPureExpr(p)) ++
        (tuple ++ cond).flatMap { expr =>
          error(expr, "old(_) expressions cannot occur in termination measures.", hasOldExpressionExceptValidWand(expr))
        }
    case PWildcardMeasure(cond) =>
      cond.toVector.flatMap(p => assignableToSpec(p) ++ isPureExpr(p)) ++
        cond.map { expr =>
          error(expr, "old(_) expressions cannot occur in termination measures.", hasOldExpressionExceptValidWand(expr))
        }.getOrElse(noMessages)
  }

  private def wellDefClosureSpecInstanceParams(c: PClosureSpecInstance, fArgs: Vector[(PParameter, Type)]): Messages = c match {
    case PClosureSpecInstance(fName, ps) if ps.size > fArgs.size =>
      error(c, s"spec instance $c has too many parameters (more than the arguments of function $fName)")
    case spec: PClosureSpecInstance if spec.paramKeys.isEmpty =>
      (spec.paramExprs zip fArgs) flatMap { case (exp, a) =>
        // we disallow calling closures from initialization code. Thus, it is safe to use the more permissive notion
        // of assignability here (where mayInit = false).
        assignableTo.errors((exprType(exp), a._2, false))(exp)
      }
    case spec@PClosureSpecInstance(fName, ps) if spec.paramKeys.size == ps.size =>
      val argsTypeMap = fArgs.collect {
        case (PNamedParameter(id, _), t) => id.name -> t
        case (PExplicitGhostParameter(PNamedParameter(id, _)), t) => id.name -> t
      }.toMap
      val wellDefIfNoDuplicateParams = (spec.paramKeys foldLeft (Set[String](), noMessages)) {
        case ((seen, msg), k) => (seen + k, msg ++ (if (seen.contains(k)) error(k, s"duplicate parameter key $k") else noMessages))
      }._2
      val wellDefIfCanAssignParams = (spec.paramKeys zip spec.paramExprs zip ps) flatMap {
        case ((k, exp), p) => argsTypeMap.get(k) match {
          case Some(t: Type) =>
            // we disallow calling closures from initialization code. Thus, it is safe to use the more permissive notion
            // of assignability here (where mayInit = false).
            assignableTo.errors((exprType(exp), t, false))(exp)
          case _ => error(p.key.get, s"could not find argument $k in the function $fName")
      }}
      wellDefIfNoDuplicateParams ++ wellDefIfCanAssignParams ++ c.paramExprs.flatMap(exp => isPureExpr(exp))
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
    val mayInit = isEnclosingMayInit(e)
    isExpr(e).out ++ assignableTo.errors(exprType(e), AssertionT, mayInit)(e) ++ isWeaklyPureExpr(e)
  }

  private def illegalPreconditionNode(n: PNode): Messages = {
    n match {
      case PLabeledOld(PLabelUse(PLabelNode.lhsLabel), _) => noMessages
      case n@ (_: POld | _: PLabeledOld) => message(n, s"old not permitted in precondition")
      case n@ (_: PBefore) => message(n, s"old not permitted in precondition")
      case _ => noMessages
    }
  }

  /** Returns the type matched by the wildcard `w`. */
  private def wildcardMatchType(w: PMatchWildcard): Type = {
    w match {
      case tree.parent(p) => p match {
        case PMatchAdt(c, fields) =>
          val index = fields indexWhere {
            w eq _
          }
          val adtClauseT = underlyingType(typeSymbType(c)).asInstanceOf[AdtClauseT]
          if (index < adtClauseT.fields.size) {
            adtClauseT.typeAt(index)
          } else UnknownType // Error is found when PMatchADT is checked higher up the ADT

        case tree.parent.pair(_: PMatchExpCase, m: PMatchExp) => exprType(m.exp)
        case tree.parent.pair(_: PMatchStmtCase, m: PMatchStatement) => exprType(m.exp)

        case _ => Violation.violation("Found wildcard with unexpected root in wildcardMatchType")
      }

      case _ => Violation.violation(s"Did not find root for wildcard in wildcardMatchType")
    }
  }
}
