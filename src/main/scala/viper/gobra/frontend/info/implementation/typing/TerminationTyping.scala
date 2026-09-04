// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PFunctionSpec, PNode, PTerminationMeasure, PTupleTerminationMeasure, PWildcardMeasure}
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
    * Returns true if `measures` guarantee that a member terminates on every call.
    * This is the case if the specification contains a non-conditional termination measure:
    * conditional measures only guarantee termination when one of their conditions holds.
    * A non-conditional wildcard measure also guarantees termination (albeit without proof).
    * Note that, unlike Viper, Gobra currently does not support marking a member as possibly
    * non-terminating by means of a termination measure. To be defensive against the
    * introduction of such measures, the case analysis below conservatively defaults to
    * false for kinds of termination measures that it does not know.
    */
  private[typing] def measuresGuaranteeTermination(measures: Vector[PTerminationMeasure]): Boolean =
    measures.exists {
      case m: PTupleTerminationMeasure => !isConditional(m)
      case m: PWildcardMeasure => !isConditional(m)
      case _ => false
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

  private[typing] def noConditionalMeasureErrors(measures: Vector[PTerminationMeasure]): Messages =
    measures.flatMap { m =>
      error(m,
        "Conditional termination measures are not allowed on ghost or pure functions, methods, and interface methods.",
        isConditional(m))
    }

  /**
    * Checks that `spec` guarantees that the member it belongs to terminates on every call, as Gobra
    * requires of all ghost and pure members: functions, methods, and interface method signatures alike.
    * The missing-measure error is reported on `node`, which should be the member or signature that
    * `spec` specifies.
    *
    * Ghost and pure members are held to a stricter rule than `measuresGuaranteeTermination`: they may
    * not carry a conditional measure at all because we do not check that the conditions of conditional
    * measures cover all inputs. The two problems are therefore reported separately, the missing
    * measure on `node` and each conditional measure on the measure itself, and only the former
    * is subject to `disableCheckTerminationPureFns`.
    */
  private[typing] def mustTerminateErrors(node: PNode, spec: PFunctionSpec): Messages = {
    val missingMeasureError = error(node,
      "Ghost and pure functions, methods, and interface methods must have termination measures, but none was found.",
      !config.disableCheckTerminationPureFns && spec.terminationMeasures.isEmpty)
    missingMeasureError ++ noConditionalMeasureErrors(spec.terminationMeasures)
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
