package viper.gobra.translator

import viper.silver.{ast => vpr}

object Names {
  def pointerFields(t: vpr.Type): String = s"val$$_$t"
  def returnLabel: String = "returnLabel"
}
