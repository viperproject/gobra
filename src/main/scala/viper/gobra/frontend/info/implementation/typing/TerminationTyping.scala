// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PNode, PTerminationMeasure, PTupleTerminationMeasure, PWildcardMeasure}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

/**
  * Collects the functionality to reason about termination measures. Checks about termination
  * measures that are shared by multiple well-definedness checks should live here, such that
  * all of them reason about termination in a consistent way.
  */
trait TerminationTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def isConditional(measure: PTerminationMeasure): Boolean = measure match {
    case PTupleTerminationMeasure(_, cond) => cond.nonEmpty
    case PWildcardMeasure(cond) => cond.nonEmpty
  }

  /**
    * Returns errors unless `measures` guarantee that `n` (a member or a loop) terminates on every call.
    * This is the case if the specification contains a non-conditional termination measure:
    * conditional measures only guarantee termination when one of their conditions holds.
    * A non-conditional wildcard measure also guarantees termination (albeit without proof).
    * Note that, unlike Viper, Gobra currently does not support marking a member as possibly
    * non-terminating by means of a termination measure.
    *
    * `mustTerminateMsg` states why `n` has to terminate, and is used as the prefix of all
    * reported errors. If `measureMayBeOmitted` is set, the absence of any measure is not
    * reported (e.g., because `config.disableCheckTerminationPureFns` is set); measures that are present still have
    * to guarantee termination.
    */
  private[typing] def guaranteedTerminationErrors(n: PNode, measures: Vector[PTerminationMeasure], mustTerminateMsg: String, measureMayBeOmitted: Boolean): Messages =
    error(n, s"$mustTerminateMsg, but no termination measure was found.", !measureMayBeOmitted && measures.isEmpty) ++
      measures.flatMap { m =>
        error(m,
          s"$mustTerminateMsg, but Gobra does currently not support conditional termination measures to establish termination.",
          isConditional(m))
      }

  /**
    * The semantics of wildcard termination measures in interface method specifications is not clear.
    * Thus, they are rejected.
    */
  private[typing] def noWildcardMeasureErrors(measures: Vector[PTerminationMeasure]): Messages =
    measures.flatMap {
      case w: PWildcardMeasure =>
        error(w, "Wildcard termination measures are not allowed in the specifications of interface methods.")
      case _ => noMessages
    }

  private[typing] def hasSameMeasureType(measures: Vector[PTerminationMeasure]): Boolean = {
    val tupleMeasureTypes = measures
      .collect { case ttm: PTupleTerminationMeasure => ttm }
      .map(_.tuple.map(typ))
    tupleMeasureTypes forall (_.equals(tupleMeasureTypes.head))
  }

  private[typing] def wellDefTerminationMeasure(measure: PTerminationMeasure): Messages = measure match {
    case PTupleTerminationMeasure(tuple, cond) =>
      tuple.flatMap(p => comparableType.errors(exprType(p))(p) ++ isWeaklyPureExpr(p)) ++
        cond.toVector.flatMap(p => assignableToSpec(p) ++ isPureExpr(p))
    case PWildcardMeasure(cond) =>
      cond.toVector.flatMap(p => assignableToSpec(p) ++ isPureExpr(p))
  }
}
