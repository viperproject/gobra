package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}

trait Assertions
  extends BaseTranslator[in.Assertion, CodeWriter[vpr.Exp]] {

  def invariant(x: in.Assertion)(ctx: Context): (CodeWriter[Unit], vpr.Exp)
  def precondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp]
  def postcondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp]
}
