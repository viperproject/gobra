package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

abstract class Types
  extends BaseTranslator[in.Type, vpr.Type]
