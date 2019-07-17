package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.util.Entity
import viper.gobra.ast.frontend.{DefaultPrettyPrinter, PIdnNode}
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.implementation.TypeInfoImpl

class InfoDebugPrettyPrinter(val info: TypeInfoImpl) extends DefaultPrettyPrinter {

  override def showId(id: PIdnNode): Doc = super.showId(id) <+> parens("::" <+> showEntity(info.regular(id)))

  def showEntity(e: Entity): Doc = e match {
    case r: Regular => r.getClass.getSimpleName
    case x => x.toString
  }
}
