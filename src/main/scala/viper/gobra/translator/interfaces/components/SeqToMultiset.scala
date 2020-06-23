package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait SeqToMultiset extends Generator {
  def create(exp : vpr.Exp) : vpr.DomainFuncApp
}
