// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.util
import viper.gobra.util.BackendAnnotation
import viper.silver.{ast => vpr}

object VprInfo {
  def maybeAttachOpaque(info: vpr.Info, isOpaque: Boolean): vpr.Info = {
    if (isOpaque) {
      attachBackendAnnotation(info, "opaque")
    } else {
      info
    }
  }

  def isOpaque(info: vpr.Info): Boolean =
    info.getAllInfos[vpr.AnnotationInfo].exists(_.values.contains("opaque"))

  /** Key of the annotation marking the Viper function that encodes `active(i)`. */
  val activeExecutionAnnotationKey: String = viper.hyperfuncs.HyperfuncsAnnotations.activeExecutionKey

  def isActiveExecution(info: vpr.Info): Boolean =
    info.getAllInfos[vpr.AnnotationInfo].exists(_.values.contains(activeExecutionAnnotationKey))

  /** Key of the annotation marking Viper functions that encode hyper functions, see [[viper.hyperfuncs.HyperfuncsAnnotations]]. */
  val hyperFuncAnnotationKey: String = viper.hyperfuncs.HyperfuncsAnnotations.hyperfunctionKey

  def maybeAttachHyperFunc(info: vpr.Info, isHyper: Boolean): vpr.Info = {
    if (isHyper) {
      attachBackendAnnotation(info, hyperFuncAnnotationKey)
    } else {
      info
    }
  }

  def isHyperFunc(info: vpr.Info): Boolean =
    info.getAllInfos[vpr.AnnotationInfo].exists(_.values.contains(hyperFuncAnnotationKey))

  /**
    * Key of the annotation listing the positions (among the Viper function's parameters) of the execution parameters
    * of a hyperfunc, i.e., its parameters of type `exec`. An execution parameter has a single value for all executions,
    * so the product constructions pass it once instead of once per execution.
    */
  val hyperFuncExecParamsAnnotationKey: String = viper.hyperfuncs.HyperfuncsAnnotations.executionParametersKey

  /** The positions of the execution parameters of the hyperfunc with info `info`. */
  def hyperFuncExecParams(info: vpr.Info): Set[Int] =
    info.getAllInfos[vpr.AnnotationInfo].flatMap(_.values.getOrElse(hyperFuncExecParamsAnnotationKey, Seq.empty)).map(_.toInt).toSet

  /** Attaches the annotation listing the execution parameters, if any, from the annotations of a hyperfunc member. */
  def attachHyperFuncExecParams(info: vpr.Info, annotations: Vector[BackendAnnotation]): vpr.Info =
    annotations.find(_.key == hyperFuncExecParamsAnnotationKey) match {
      case Some(a) => attachBackendAnnotation(info, a.key, a.values: _*)
      case None => info
    }

  /** Removes the annotations marking hyper functions and their execution parameters, which are only used internally by Gobra. */
  def removeHyperFunc(info: vpr.Info): vpr.Info = info match {
    case vpr.ConsInfo(head, tail) => (removeHyperFunc(head), removeHyperFunc(tail)) match {
      case (vpr.NoInfo, t) => t
      case (h, vpr.NoInfo) => h
      case (h, t) => vpr.ConsInfo(h, t)
    }
    case a: vpr.AnnotationInfo if a.values.contains(hyperFuncAnnotationKey) || a.values.contains(hyperFuncExecParamsAnnotationKey) =>
      val remaining = a.values - hyperFuncAnnotationKey - hyperFuncExecParamsAnnotationKey
      if (remaining.isEmpty) vpr.NoInfo else vpr.AnnotationInfo(remaining)
    case i => i
  }

  def maybeAttachReveal(info: vpr.Info, reveal: Boolean): vpr.Info = {
    if (reveal) {
      attachBackendAnnotation(info, "reveal")
    } else {
      info
    }
  }

  private def attachBackendAnnotation(info: vpr.Info, key: String, values: String*) : vpr.Info = {
    val annotation = vpr.AnnotationInfo(Map(key -> values))
    vpr.ConsInfo(annotation, info)
  }

  private def backendAnnotationToInfo(a: BackendAnnotation): vpr.AnnotationInfo = {
    vpr.AnnotationInfo(Map(a.key -> a.values))
  }

  private def attachBackendAnnotation(a: BackendAnnotation, info: vpr.Info): vpr.Info = {
    val modeAnnotation = backendAnnotationToInfo(a)
    vpr.ConsInfo(modeAnnotation, info)
  }

  def attachAnnotations(as: Vector[BackendAnnotation], info: vpr.Info): vpr.Info =
    as match {
      case Vector() => info
      case _ => attachAnnotations(as.tail, attachBackendAnnotation(as.head, info))
    }
}
