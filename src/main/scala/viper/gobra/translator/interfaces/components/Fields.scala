package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}

trait Fields extends Generator {

  def field(t: in.Type)(ctx: Context): vpr.Field
}
