package viper.gobra.translator.interfaces.components

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Fixpoint extends Generator {

  // def typ(args: Vector[vpr.Type]): vpr.DomainType
  def create(gc: in.GlobalConstDecl)(ctx: Context)
  def get(gc: in.GlobalConst)(ctx: Context): vpr.DomainFuncApp
}
