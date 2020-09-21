// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import viper.gobra.translator.encodings.TypeEncoding
import org.bitbucket.inkytonik.kiama.==>

/**
  * Combines encodings by sequentially picking the first encoding that is defined on an argument.
  */
class SequentialTypeEncodingCombiner(encodings: Vector[TypeEncoding]) extends TypeEncodingCombiner(encodings) {

  override protected[combinators] def combiner[X, Y](get: TypeEncoding => (X ==> Y)): X ==> Y = {
    encodings.foldRight[X ==> Y](PartialFunction.empty){ case (encoding, w) =>
      get(encoding) orElse w
    }
  }
}
