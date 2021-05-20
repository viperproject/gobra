// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PBuildIn, PExpression, PInvoke}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}

trait Executability extends BaseProperty { this: TypeInfoImpl =>

  lazy val isExecutable: Property[PExpression] = createBinaryProperty("executable") {
    case n: PInvoke =>
      resolve(n) match {
        case Some(fc: ap.FunctionCall) =>
          fc.callee match {
            case _: ap.DomainFunction => false
            case _ => true
          }
        case _ => false
      }
    case _ => false
  }

  // TODO: probably will be unneccessary because build int functions always have to be called

  lazy val isBuildIn: Property[PExpression] = createBinaryProperty("built-in") {
    case _: PBuildIn => true
    case _ => false
  }
}
