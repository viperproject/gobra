package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{PIdnNode, PMember, PMisc, PPkgDef, PScope, PType}
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.Type.Type

trait ExternalTypeInfo {

  def pkgName: PPkgDef

  /**
    * Gets called by the type checker to perform a symbol table lookup in an imported package
    */
  def externalRegular(n: PIdnNode): Option[Regular]

  /**
    * Returns true if a symbol table lookup was made through `externalRegular` for the given member
    */
  def isUsed(m: PMember): Boolean

  def regular(n: PIdnNode): Regular

  def typ(misc: PMisc): Type

  def typ(typ: PType): Type

  def scope(n: PIdnNode): PScope

}
