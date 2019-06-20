package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

abstract class Functions
  extends BaseTranslator[in.Function, vpr.Method]
