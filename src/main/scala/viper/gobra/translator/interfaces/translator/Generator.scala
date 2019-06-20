package viper.gobra.translator.interfaces.translator

import viper.gobra.translator.interfaces.Collector

trait Generator {

  /**
    * Finalizes translation. May add to collector.
    * @param col
    */
  def finalize(col: Collector): Unit
}
