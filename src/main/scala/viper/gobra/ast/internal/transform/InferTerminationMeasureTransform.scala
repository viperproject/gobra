// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.internal._
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.InferTerminationMeasureAnnotation
import viper.gobra.reporting.Source.Parser.Single
import viper.gobra.util.Violation.violation
import scala.collection.mutable

case class InferTerminationMeasureTransform(current:Integer, map: mutable.Map[Member, Integer]) extends InternalTransform {

  override def name(): String = "add_heuristics_to_infer_termination_measure"

  override def transform(p: Program): Program = transformMembers(memberTrans)(p)

  private def memberTrans(member: Member): Member = member match {

    case f@Function(name, args, results, pres, posts, terminationMeasure, body) =>
      val newBody = body match {
        case None => None
        case Some(block) => Some(Block(block.decls, block.stmts map loopHeuristic)(block.info))
      }
      terminationMeasure match {
        case x@Some(InferTerminationMeasure()) =>
          val info = createAnnotatedInfo(x.head.info)
          map.get(f) match {
            case None => Function(name, args, results, pres, posts, chooseHeuristic(current)(info)(args), newBody)(f.info)
            case Some(heuristic) => Function(name, args, results, pres, posts, chooseHeuristic(heuristic)(info)(args), newBody)(f.info)
          }
        case _ => Function(name, args, results, pres, posts, terminationMeasure, newBody)(f.info)
      }

    case m@Method(receiver, name, args, results, pres, posts, terminationMeasure, body) =>
      val newBody = body match {
        case None => None
        case Some(block) => Some(Block(block.decls, block.stmts map loopHeuristic)(block.info))
      }
      terminationMeasure match {
        case x@Some(InferTerminationMeasure()) =>
          val info = createAnnotatedInfo(x.head.info)
          map.get(m) match {
            case None => Method(receiver, name, args, results, pres, posts, chooseHeuristic(current)(info)(args), newBody)(m.info)
            case Some(heuristic) => Method(receiver, name, args, results, pres, posts, chooseHeuristic(heuristic)(info)(args), newBody)(m.info)
          }
        case _ => Method(receiver, name, args, results, pres, posts, terminationMeasure, newBody)(m.info)
      }

    case f@PureFunction(name, args, results, pres, posts, terminationMeasure, body) =>
      terminationMeasure match {
        case x@Some(InferTerminationMeasure()) =>
          val info = createAnnotatedInfo(x.head.info)
          map.get(f) match {
            case None => PureFunction(name, args, results, pres, posts, chooseHeuristic(current)(info)(args), body)(f.info)
            case Some(heuristic) => PureFunction(name, args, results, pres, posts, chooseHeuristic(heuristic)(info)(args), body)(f.info)
          }
        case _ => member
      }

    case m@PureMethod(receiver, name, args, results, pres, posts, terminationMeasure, body) =>
      terminationMeasure match {
        case x@Some(InferTerminationMeasure()) =>
          val info = createAnnotatedInfo(x.head.info)
          map.get(m) match {
            case None => PureMethod(receiver, name, args, results, pres, posts, chooseHeuristic(current)(info)(args), body)(m.info)
            case Some(heuristic) => PureMethod(receiver, name, args, results, pres, posts, chooseHeuristic(heuristic)(info)(args), body)(m.info)
          }
        case _ => member
      }

    case x => x
  }

  private def chooseHeuristic(number: Integer)(src: Source.Parser.Info)(args: Vector[Parameter.In]): Option[Assertion] = {
    if (number == 1) {
      parametersAsMeasureHeuristic(args)(src)
    } else if (number == 2) {
      emptyMeasure(args)(src)
    } else if (number == 3) {
      Addition(args)(src)
    } else {
      violation("Invalid number of heuristic")
    }
  }

  // number as 1
  private def parametersAsMeasureHeuristic(args: Vector[Parameter.In])(src: Source.Parser.Info): Option[Assertion] = {
    Some(TupleTerminationMeasure(args)(src))
  }

  // number as 2
  // only for testing purpose, can be removed in the future
  private def emptyMeasure(args: Vector[Parameter.In])(src: Source.Parser.Info): Option[Assertion] = {
    Some(TupleTerminationMeasure(Vector.empty)(src))
  }

  // number as 3
  // only for testing purpose, can be removed in the future
  private def Addition(args: Vector[Parameter.In])(src: Source.Parser.Info): Option[Assertion] = {
    Some(TupleTerminationMeasure(Vector(Add(args.head, args.head)(src)))(src))
  }

  private def loopHeuristic(statement: Stmt): Stmt = {
    statement match {
      case While(cond, invs, terminationMeasure, body) =>
        terminationMeasure match {
          case x@Some(InferTerminationMeasure()) =>
            val info = createAnnotatedInfo(x.head.info)
            val newMeasure = cond match {
              case LessCmp(left, right) => Some(TupleTerminationMeasure(Vector(Sub(right, left)(info)))(info))
              case AtMostCmp(left, right) => Some(TupleTerminationMeasure(Vector(Sub(right, left)(info)))(info))
              case GreaterCmp(left, right) => Some(TupleTerminationMeasure(Vector(Sub(left, right)(info)))(info))
              case AtLeastCmp(left, right) => Some(TupleTerminationMeasure(Vector(Sub(left, right)(info)))(info))
              case UneqCmp(left, right) =>
                val result = getConditionFromInvariant(left)(right)(invs)(info)
                result match {
                  case Left(_) => Some(TupleTerminationMeasure(Vector(Sub(right, left)(info)))(info))
                  case Right(exp) => Some(TupleTerminationMeasure(Vector(exp))(info))
                }
              case _ => Some(TupleTerminationMeasure(Vector.empty)(info))
            }
            While(cond, invs, newMeasure, loopHeuristic(body))(statement.info)
          case _ => statement
        }
      case Seqn(stmts) => Seqn(stmts map loopHeuristic)(statement.info)
      case If(cond, thn, els) => If(cond, loopHeuristic(thn), loopHeuristic(els))(statement.info)
      case Block(decls, stmts) => Block(decls, stmts map loopHeuristic)(statement.info)
      case _ => statement
    }
  }

  private def getConditionFromInvariant(left:Expr)(right:Expr)(invs: Vector[Assertion])(src: Source.Parser.Info): Either[Boolean, Expr] = {
    invs match {
      case Nil => Left(false)
      case _ => invs.head match {
        case ExprAssertion(exp: Expr) => exp match {
          case LessCmp(leftExp, rightExp) =>
            if(compareExps(left)(leftExp)(right)(rightExp)){
              Right(Sub(right, left)(src))
            } else {
              getConditionFromInvariant(left)(right)(invs.tail)(src)
            }
          case AtMostCmp(leftExp, rightExp) =>
            if(compareExps(left)(leftExp)(right)(rightExp)){
              Right(Sub(right, left)(src))
            } else {
              getConditionFromInvariant(left)(right)(invs.tail)(src)
            }
          case GreaterCmp(leftExp, rightExp) =>
            if(compareExps(left)(leftExp)(right)(rightExp)){
              Right(Sub(left, right)(src))
            } else {
              getConditionFromInvariant(left)(right)(invs.tail)(src)
            }
          case AtLeastCmp(leftExp, rightExp) =>
            if(compareExps(left)(leftExp)(right)(rightExp)){
              Right(Sub(left, right)(src))
            } else {
              getConditionFromInvariant(left)(right)(invs.tail)(src)
            }
          case _ => getConditionFromInvariant(left)(right)(invs.tail)(src)
        }

        case SepAnd(leftAss, rightAss) =>
          val getFromLeftAss = getConditionFromInvariant(left)(right)(Vector(leftAss))(src)
          val getFromRightAss = getConditionFromInvariant(left)(right)(Vector(rightAss))(src)
          getFromLeftAss match {
            case Left(_) =>
              getFromRightAss match {
                case Left(_) => getConditionFromInvariant(left)(right)(invs.tail)(src)
                case Right(_) => getFromLeftAss
              }
            case Right(_) => getFromLeftAss
          }

        case _ => getConditionFromInvariant(left)(right)(invs.tail)(src)
      }
    }
  }

  private def compareExps(left: Expr)(leftExp: Expr)(right: Expr)(rightExp: Expr): Boolean = {
    if ((left == leftExp && right == rightExp) || (left == rightExp && right == leftExp)) {
      true
    } else {
      false
    }
  }

  private def createAnnotatedInfo(info: Source.Parser.Info): Source.Parser.Info =
    info match {
      case s: Single => s.createAnnotatedInfo(InferTerminationMeasureAnnotation)
      case i => violation(s"l.op.info ($i) is expected to be a Single")
    }
}
