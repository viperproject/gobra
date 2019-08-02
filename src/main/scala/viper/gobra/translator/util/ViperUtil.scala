package viper.gobra.translator.util

import viper.silver.ast._

object ViperUtil {


  def toVarDecl(v: LocalVar): LocalVarDecl = {
    LocalVarDecl(v.name, v.typ)(v.pos, v.info, v.errT)
  }

  def bigAnd(it: Iterable[Exp]): Exp = {
    it.foldLeft[Exp](TrueLit()()){case (l, r) => And(l, r)()}
  }
}
