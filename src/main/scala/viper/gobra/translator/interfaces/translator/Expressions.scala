package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

trait Expressions
  extends BaseTranslator[in.Expr, CodeWriter[vpr.Exp]]
{
  def trigger(t: in.Trigger)(ctx: Context): CodeWriter[vpr.Trigger]
}
