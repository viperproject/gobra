// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

import scala.annotation.unused

trait Encoding extends TypeEncoding {

  /**
    * An Encoding that is not associated with a type, does not encode types
    * or things associated with types.
    */

  final def typ(@unused ctx: Context): in.Type ==> vpr.Type = PartialFunction.empty
}
