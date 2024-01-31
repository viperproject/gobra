// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.util
import viper.silver.{ast => vpr}

object VprInfo {
  def attachOpaque(info: vpr.Info): vpr.Info = {
    attachAnnotation(info, "opaque")
  }

  def attachReveal(info: vpr.Info): vpr.Info = {
    attachAnnotation(info, "reveal")
  }

  private def attachAnnotation(info: vpr.Info, key: String, values: String*) : vpr.Info = {
    val annotation = vpr.AnnotationInfo(Map(key -> values))
    vpr.ConsInfo(annotation, info)
  }
}
