package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait SeqMultiplicity extends Generator {
  def create(left : vpr.Exp, right : vpr.Exp) : vpr.DomainFuncApp
}
