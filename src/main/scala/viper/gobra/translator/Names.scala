package viper.gobra.translator

import viper.silver.{ast => vpr}

object Names {
  def pointerFields(t: vpr.Type): String = s"val$$_$t"
  def fieldField(fieldName: String): String = s"${fieldName}_$$"
  def returnLabel: String = "returnLabel"
}
