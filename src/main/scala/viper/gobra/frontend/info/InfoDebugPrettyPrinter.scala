package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{DefaultPrettyPrinter, PIdnNode}
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.implementation.TypeInfoImpl

import scala.util.{Failure, Success, Try}

class InfoDebugPrettyPrinter(val info: TypeInfoImpl) extends DefaultPrettyPrinter {

  override def showId(id: PIdnNode): Doc = super.showId(id) <+> parens("::" <+> showEntity(id))

  def showEntity(id: PIdnNode): Doc = {
    Try(info.regular(id)) match {
      case Failure(_) => "non-regular entity"
      case Success(value) => value match {
        case r: Regular => r.getClass.getSimpleName
        case x => x.toString
      }
    }
  }
}
