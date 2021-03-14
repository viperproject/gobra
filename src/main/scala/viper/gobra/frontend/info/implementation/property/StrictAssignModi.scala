// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

sealed trait AssignMode

object AssignMode {
  case object Single extends AssignMode
  case object Multi  extends AssignMode
  case object Variadic extends AssignMode
  case object Error  extends AssignMode
}

object StrictAssignModi {
  def apply(left: Int, right: Int): AssignMode =
    if (left > 0 && left == right) AssignMode.Single
    else if (left > right && right == 1) AssignMode.Multi
    // left - 1 == right when no argument is passed in the place of the variadic parameter
    else if (0 < left && left - 1 <= right) AssignMode.Variadic
    else AssignMode.Error
}

object NonStrictAssignModi {
  def apply(left: Int, right: Int): AssignMode =
    if (left >= 0 && left == right) AssignMode.Single
    else if (left > right && right == 1) AssignMode.Multi
    // left - 1 == right when no argument is passed in the place of the variadic parameter
    else if (0 < left && left - 1 <= right) AssignMode.Variadic
    else AssignMode.Error
}

