package viper.gobra.translator.util

import viper.silver.ast._

object ViperUtil {


  def toVarDecl(v: LocalVar): LocalVarDecl = {
    LocalVarDecl(v.name, v.typ)(v.pos, v.info, v.errT)
  }
}
