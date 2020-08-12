package viper.gobra.translator.util

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

class Registrator[T <: vpr.Member] extends Generator {

  private var gens: Set[T] = Set.empty

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = {
    gens.foreach(col.addMember)
    clean()
  }

  def clean(): Unit = gens = Set.empty

  def register(x: T): T = {
    gens += x
    x
  }
}
