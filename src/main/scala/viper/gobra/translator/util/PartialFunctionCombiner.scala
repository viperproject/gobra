// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.util.Violation

import scala.annotation.unused

object PartialFunctionCombiner {

  class SafeCombiner[X, Y](funcs: Vector[X ==> Y])(select: (X, Seq[(Y, Int)]) => Option[Y]) {

    private val lifted = funcs map (_.lift)

    def unapplySeq(a: X): Seq[Y] = lifted.flatMap(_(a))

    def safeApply(a: X): Seq[(Y, Int)] = {
      lifted.zipWithIndex.flatMap { case (f, idx) => f(a).map(_ -> idx) }
    }

    val combination: X ==> Y = new (X ==> Y){
      override def unapply(a: X): Option[Y] = select(a, safeApply(a))
      override def isDefinedAt(x: X): Boolean = funcs.exists(_.isDefinedAt(x))
      override def apply(v1: X): Y = unapply(v1).get

      // Necessary for performance. This avoids that [[isDefinedAt]] is called
      override def applyOrElse[A1 <: X, B1 >: Y](x: A1, default: A1 => B1): B1 =
        select(x, safeApply(x)).getOrElse(default(x))
    }
  }

  private def firstSelect[X, Y](@unused arg: X, results: Seq[(Y, Int)]): Option[Y] = results.headOption.map(_._1)

  private def forceUniqueSelect[X, Y](arg: X, results: Seq[(Y, Int)]): Option[Y] = results match {
    case Seq() => None
    case Seq((r, _)) => Some(r)
    case _ => Violation.violation(s"Argument $arg is defined at more than one partial function.")
  }

  private def selectWithErrorMsg[X, Y](errorMsg: (X, Seq[Int]) => Nothing)(arg: X, results: Seq[(Y, Int)]): Option[Y] = results match {
    case Seq() => None
    case Seq((r, _)) => Some(r)
    case results => errorMsg(arg, results.map(_._2))
  }


  /** If an argument is defined at more than one partial function, then the first result is returned. */
  def combine[X,Y](funcs: Vector[X ==> Y]): X ==> Y = new SafeCombiner(funcs)(firstSelect[X,Y]).combination

  /** If an argument is defined at more than one partial function, then an exception is thrown. */
  def combineNoDup[X,Y](funcs: Vector[X ==> Y]): X ==> Y = new SafeCombiner(funcs)(forceUniqueSelect[X,Y]).combination

  /**
    * If an argument is defined at more than one partial function,
    * then `errorMsg` is invoked with the sequence of corresponding matching `funcs` elements.
    * */
  def combineWithErrorMsg[T, X,Y](funcs: Vector[T])(getFunc: T => (X ==> Y))(errorMsg: (X, Seq[T]) => Nothing): X ==> Y = {
    new SafeCombiner(funcs map getFunc)(selectWithErrorMsg[X,Y]{
      case (arg, idxs) => errorMsg(arg, idxs map (idx => funcs(idx)))
    }).combination
  }
}
