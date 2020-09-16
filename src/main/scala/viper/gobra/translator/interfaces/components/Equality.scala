package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Equality extends Generator {
  def eq(l: vpr.Exp, r: vpr.Exp): vpr.Exp
}
