// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.translator.util.PartialFunctionCombiner
import viper.gobra.util.Violation

/**
  * Combines encodings by sequentially picking the first encoding that is defined on an argument.
  * An error is thrown if more than one encoding is defined on an argument.
  */
class SafeTypeEncodingCombiner(encodings: Vector[TypeEncoding], defaults: Vector[TypeEncoding]) extends TypeEncodingCombiner(encodings, defaults) {

  override protected[combinators] def combiner[X, Y](get: TypeEncoding => (X ==> Y)): X ==> Y = {
    combine(encodings)(get) orElse combine(defaults)(get)
  }

  def combine[X, Y](encodings: Vector[TypeEncoding])(get: TypeEncoding => (X ==> Y)): X ==> Y = {
    PartialFunctionCombiner.combineWithErrorMsg(encodings)(get){ case (arg, encodings) =>
      val listOfSupportingEncodings = encodings.map(_.getClass).mkString(", ")
      Violation.violation(s"Argument $arg is supported by more than one encoding: $listOfSupportingEncodings")
    }
  }
}
