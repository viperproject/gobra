package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.ExprWriter
import viper.silver.{ast => vpr}

trait Assertions
  extends BaseTranslator[in.Assertion, ExprWriter[vpr.Exp]] {

  // def asExpEncodableExp ...
  def precondition(x: in.Assertion)(ctx: Context): ExprWriter[vpr.Exp]
  def postcondition(x: in.Assertion)(ctx: Context): ExprWriter[vpr.Exp]
}
