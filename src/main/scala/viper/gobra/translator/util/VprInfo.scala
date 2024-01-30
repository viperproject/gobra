// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.util
import viper.silver.{ast => vpr}

object VprInfo {
  def attachOpaqueAnnotation(isOpaque: Boolean, info: vpr.Info): vpr.Info = {
    if (isOpaque) {
      val opaqueAnnotation = vpr.AnnotationInfo(Map("opaque" -> Seq()))
      vpr.ConsInfo(opaqueAnnotation, info)
    } else {
      info
    }
  }
}
