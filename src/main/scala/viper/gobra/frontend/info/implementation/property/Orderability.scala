// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Orderability extends BaseProperty { this: TypeInfoImpl =>

  lazy val orderedType: Property[Type] = createBinaryProperty("ordered") {
    case Single(st) => underlyingType(st) match {
      case _: IntT => true
      case PermissionT => true
      case _: FloatT => true
      case StringT => true
      case _ => false
    }
    case _ => false
  }
}
