package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Arrays extends Generator {
  def typ() : vpr.DomainType
  def length(exp : vpr.Exp) : vpr.DomainFuncApp
  def slot(base : vpr.Exp, index : vpr.Exp) : vpr.DomainFuncApp
}
