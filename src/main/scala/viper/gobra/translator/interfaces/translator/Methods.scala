package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.MemberWriter


abstract class Methods
  extends BaseTranslator[in.Method, MemberWriter[vpr.Method]]
