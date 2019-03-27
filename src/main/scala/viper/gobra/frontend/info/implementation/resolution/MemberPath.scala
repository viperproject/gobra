package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.frontend.info.base.SymbolTable.Embbed

sealed trait MemberPath

object MemberPath {

  case object Underlying extends MemberPath
  case object Deref extends MemberPath
  case object Ref extends MemberPath
  case class Next(decl: Embbed) extends MemberPath
}