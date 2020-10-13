package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Options extends Generator {
  def get(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
  def none(t : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
  def some(exp : vpr.Exp)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
  def typ(t : vpr.Type) : vpr.DomainType
}
