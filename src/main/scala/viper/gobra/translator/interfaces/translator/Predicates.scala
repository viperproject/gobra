package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.MemberWriter

abstract class Predicates extends Generator {

  def mpredicate(pred: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate]
  def fpredicate(pred: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate]
}
