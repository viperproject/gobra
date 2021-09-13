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
      terminationMeasure match {
        case x@Some(InferTerminationMeasure()) =>
          val info = createAnnotatedInfo(x.head.info)
          map.get(f) match {
            case None => Function(name, args, results, pres, posts, chooseHeuristic(current)(info)(args), body)(f.info)
            case Some(heuristic) => Function(name, args, results, pres, posts, chooseHeuristic(heuristic)(info)(args), body)(f.info)
          }
        case _ => member
      }

    case m@Method(receiver, name, args, results, pres, posts, terminationMeasure, body) =>
      terminationMeasure match {
        case x@Some(InferTerminationMeasure()) =>
          val info = createAnnotatedInfo(x.head.info)
          map.get(m) match {
            case None => Method(receiver, name, args, results, pres, posts, chooseHeuristic(current)(info)(args), body)(m.info)
            case Some(heuristic) => Method(receiver, name, args, results, pres, posts, chooseHeuristic(heuristic)(info)(args), body)(m.info)
          }
        case _ => member
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
  private def emptyMeasure(args: Vector[Parameter.In])(src: Source.Parser.Info): Option[Assertion] = {
    Some(TupleTerminationMeasure(Vector.empty)(src))
  }

  // number as 3
  private def Addition(args: Vector[Parameter.In])(src: Source.Parser.Info): Option[Assertion] = {
    Some(TupleTerminationMeasure(Vector(Add(args.head, args.head)(src)))(src))
  }

  private def createAnnotatedInfo(info: Source.Parser.Info): Source.Parser.Info =
    info match {
      case s: Single => s.createAnnotatedInfo(InferTerminationMeasureAnnotation)
      case i => violation(s"l.op.info ($i) is expected to be a Single")
    }
}
