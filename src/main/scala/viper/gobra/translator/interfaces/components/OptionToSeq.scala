package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait OptionToSeq extends Generator {

  /**
    * Gives a Viper domain function application 'opt2seq(`exp`)'
    * for converting the option type expression `exp` to a sequence
    * of type `typ`. Here `exp` is assumed to be of type 'option[`typ`]',
    */
  def create(exp : vpr.Exp, typ : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
}
