// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.util
import viper.silver.{ast => vpr}

object VprInfo {
  def maybeAttachOpaque(info: vpr.Info, isOpaque: Boolean): vpr.Info = {
    if (isOpaque) {
      attachAnnotation(info, "opaque")
    } else {
      info
    }
  }

  def maybeAttachReveal(info: vpr.Info, reveal: Boolean): vpr.Info = {
    if (reveal) {
      attachAnnotation(info, "reveal")
    } else {
      info
    }
  }

  private def attachAnnotation(info: vpr.Info, key: String, values: String*) : vpr.Info = {
    val annotation = vpr.AnnotationInfo(Map(key -> values))
    vpr.ConsInfo(annotation, info)
  }
}
