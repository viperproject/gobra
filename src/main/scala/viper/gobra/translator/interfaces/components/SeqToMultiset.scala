package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait SeqToMultiset extends Generator {
  def create(exp : vpr.Exp)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
}
