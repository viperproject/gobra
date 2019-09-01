package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Tuples extends Generator {

  def typ(args: Vector[vpr.Type]): vpr.DomainType
  def create(args: Vector[vpr.Exp]): vpr.DomainFuncApp
  def get(arg: vpr.Exp, index: Int, arity: Int): vpr.DomainFuncApp
}
