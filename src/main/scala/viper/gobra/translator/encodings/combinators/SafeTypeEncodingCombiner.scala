package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.translator.encodings.TypeEncoding
import viper.gobra.util.Violation

/**
  * Combines encodings by sequentially picking the first encoding that is defined on an argument.
  * An error is thrown if more than one encoding is defined on an argument.
  */
class SafeTypeEncodingCombiner(encodings: Vector[TypeEncoding]) extends TypeEncodingCombiner(encodings) {

  override protected[combinators] def combiner[X, Y](get: TypeEncoding => (X ==> Y)): X ==> Y = {
    case x if encodings.exists(enc => get(enc).isDefinedAt(x)) =>
      val encodingsResultPairs = encodings.flatMap(enc => get(enc).lift(x).map(enc -> _))
      if (encodingsResultPairs.size == 1) {
        encodingsResultPairs.head._2
      } else {
        val listOfSupportingEncodings = encodingsResultPairs.map(_._1.getClass).mkString(", ")
        Violation.violation(s"Argument $x is supported by more than one encoding: $listOfSupportingEncodings")
      }
  }
}
