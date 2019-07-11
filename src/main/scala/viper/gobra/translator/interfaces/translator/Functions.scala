package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.MemberWriter

abstract class Functions
  extends BaseTranslator[in.Function, MemberWriter[vpr.Method]]
