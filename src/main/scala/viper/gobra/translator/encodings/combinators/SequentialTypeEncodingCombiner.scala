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
