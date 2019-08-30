package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

abstract class Statements
  extends BaseTranslator[in.Stmt, CodeWriter[vpr.Stmt]]

