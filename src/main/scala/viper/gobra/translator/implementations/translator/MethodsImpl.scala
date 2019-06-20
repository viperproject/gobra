package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.Methods
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}

class MethodsImpl extends Methods {

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Method)(ctx: Context): vpr.Method = ???
}
