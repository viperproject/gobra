package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.MemberWriter

abstract class PureMethods extends Generator {

  def pureMethod(meth: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function]
  def pureFunction(func: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function]

}
