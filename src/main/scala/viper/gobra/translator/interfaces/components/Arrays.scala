package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Arrays extends Generator {
  def typ() : vpr.DomainType
  def length(exp : vpr.Exp)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
  def slot(base : vpr.Exp, index : vpr.Exp)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
}
