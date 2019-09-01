package viper.gobra.translator.util

import viper.silver.ast._

object ViperUtil {

  def toVarDecl(v: LocalVar): LocalVarDecl = {
    LocalVarDecl(v.name, v.typ)(v.pos, v.info, v.errT)
  }

  def toVar(d: LocalVarDecl): LocalVar = {
    d.localVar
  }

  def bigAnd(it: Iterable[Exp]): Exp = {
    it.foldLeft[Exp](TrueLit()()){case (l, r) => And(l, r)()}
  }

  def seqn(ss: Vector[Stmt]): Seqn = Seqn(ss, Vector.empty)()

  def nop: Seqn = Seqn(Vector.empty, Vector.empty)()
}
