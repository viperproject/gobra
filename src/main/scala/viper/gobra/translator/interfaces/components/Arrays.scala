package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Arrays extends Generator {
  def typ() : vpr.DomainType
}
