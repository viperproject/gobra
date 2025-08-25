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

  def maybeAttachHyperFunc(info: vpr.Info, isHyper: Boolean): vpr.Info = {
    if (isHyper) {
      attachBackendAnnotation(info, "hyperFunc")
    } else {
      info
    }
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
