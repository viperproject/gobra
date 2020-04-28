package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{PIdnNode, PMisc, PPkgDef, PScope, PType}
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.Type.Type

trait ExternalTypeInfo {

  def pkgName: PPkgDef

  def externalRegular(n: PIdnNode): Option[Regular]

  def regular(n: PIdnNode): Regular

  def typ(misc: PMisc): Type

  def typ(typ: PType): Type

  def scope(n: PIdnNode): PScope

}
