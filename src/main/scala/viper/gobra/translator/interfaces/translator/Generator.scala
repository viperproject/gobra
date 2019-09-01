package viper.gobra.translator.interfaces.translator

import viper.gobra.translator.interfaces.{Collector, Context}

trait Generator {

  /**
    * Finalizes translation. May add to collector.
    * @param col
    */
  def finalize(col: Collector): Unit

  def chain[R](fs: Vector[Context => (R, Context)])(ctx: Context): (Vector[R], Context) = {
    fs.foldLeft((Vector.empty[R], ctx)){ case ((rs, c), rf) =>
      val (r, nc) = rf(c)
      (r +: rs, nc)
    }
  }
}
