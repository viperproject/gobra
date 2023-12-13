// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2023 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

object VprInfo {
  def exhaleModeToInfo(mode: in.ExhaleMode): vpr.AnnotationInfo = {
    val annotVal = mode match {
      case in.Greedy => "greedy"
      case in.Mce => "mce"
    }
    vpr.AnnotationInfo(Map("exhaleMode" -> Seq(annotVal)))
  }

  def attachExhaleModeAnnotation(mode: in.ExhaleMode, info: vpr.Info): vpr.Info = {
    val modeAnnotation = exhaleModeToInfo(mode)
    vpr.ConsInfo(modeAnnotation, info)
  }

  def attachOptExhaleModeAnnotation(modeOpt: Option[in.ExhaleMode], info: vpr.Info): vpr.Info =
    modeOpt match {
      case None => info
      case Some(i) => attachExhaleModeAnnotation(i, info)
    }
}