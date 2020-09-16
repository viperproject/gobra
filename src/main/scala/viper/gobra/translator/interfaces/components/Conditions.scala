package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Conditions extends Generator {
  def assert(x: vpr.Exp): vpr.Exp
}
