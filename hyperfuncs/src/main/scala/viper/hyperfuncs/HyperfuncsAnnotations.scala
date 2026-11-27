// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast._

/**
  * The input convention of the plugin for front ends that produce Viper ASTs (such as Gobra), as opposed to Viper's
  * surface syntax:
  *
  *  - a hyperfunction is a Viper function with the annotation [[hyperfunctionKey]];
  *  - its execution parameters, i.e., parameters that denote an execution and are passed once instead of once per
  *    execution, are listed by position in the annotation [[executionParametersKey]]. Execution identifiers are
  *    integers, and `rel(e, i)` with such a parameter `i` is already a case distinction over the executions;
  *  - `active(i)` is a call of an abstract function with the annotation [[activeExecutionKey]] and one integer
  *    argument;
  *  - `low(e)`, `rel(e, i)` and `lowEvent` are the SIF plugin's `SIFLowExp`, `SIFRelExp` and `SIFLowEventExp`.
  *
  * Viper's surface syntax produces the same form, except for execution identifiers, which have type `Exec` until
  * [[HyperfuncsPreprocessing]] lowers them to integers.
  */
object HyperfuncsAnnotations {
  val hyperfunctionKey: String = "hyperFunc"
  /** The spelling of [[hyperfunctionKey]] in Viper's surface syntax, `@hf()`, see [[HyperfuncsPlugin.beforeParse]]. */
  val surfaceHyperfunctionKey: String = "hf"
  val executionParametersKey: String = "hyperFuncExecParams"
  val activeExecutionKey: String = "activeExecution"

  private def annotations(info: Info): Seq[AnnotationInfo] = info.getAllInfos[AnnotationInfo]
  private def hasKey(info: Info, key: String): Boolean = annotations(info).exists(_.values.contains(key))

  def isHyperfunction(info: Info): Boolean = hasKey(info, hyperfunctionKey) || hasKey(info, surfaceHyperfunctionKey)

  /** The positions of the execution parameters listed in the annotation [[executionParametersKey]]. */
  def executionParameters(info: Info): Set[Int] =
    annotations(info).flatMap(_.values.getOrElse(executionParametersKey, Seq.empty)).map(_.toInt).toSet

  def isActiveExecution(info: Info): Boolean = hasKey(info, activeExecutionKey)

  /** Removes the annotations of this convention, which are meaningless after the product construction. */
  def withoutHyperfunctionAnnotations(info: Info): Info = info match {
    case a: AnnotationInfo =>
      val remaining = a.values - hyperfunctionKey - surfaceHyperfunctionKey - executionParametersKey
      if (remaining.isEmpty) NoInfo else AnnotationInfo(remaining)
    case ConsInfo(a, b) => MakeInfoPair(withoutHyperfunctionAnnotations(a), withoutHyperfunctionAnnotations(b))
    case i => i
  }

  /** Replaces the calls of [[activeExecutionKey]] functions by [[HFActiveExp]] and removes these functions. */
  def normalize(p: Program): Program = {
    val active = p.functions.filter(f => isActiveExecution(f.info)).map(_.name).toSet
    if (active.isEmpty) p
    else {
      val withoutActive = p.copy(functions = p.functions.filterNot(f => active(f.name)))(p.pos, p.info, p.errT)
      withoutActive.transform {
        case f: FuncApp if active(f.funcname) => HFActiveExp(f.args.head)(f.pos, f.info, f.errT)
      }
    }
  }
}
