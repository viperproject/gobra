// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.frontend.info.base.SymbolTable.Embbed

sealed trait MemberPath

object MemberPath {

  case object Underlying extends MemberPath
  case object Deref extends MemberPath
  case object Ref extends MemberPath
  case class Next(decl: Embbed) extends MemberPath
}