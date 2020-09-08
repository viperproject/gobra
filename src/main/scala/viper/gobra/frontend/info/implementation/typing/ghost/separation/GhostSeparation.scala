// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait GhostSeparation

  extends GhostClassifier

  with GhostWellDef
  with GhostTyping
  with GhostAssignability
{ this: TypeInfoImpl =>


}
