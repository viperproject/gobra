package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.util.ViperWriter.StmtWriter
import viper.silver.{ast => vpr}

abstract class Statements
  extends BaseTranslator[in.Stmt, StmtWriter[vpr.Stmt]]

