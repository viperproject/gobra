package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

abstract class Methods
  extends BaseTranslator[in.Method, vpr.Method]
