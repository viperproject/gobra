package viper.gobra.frontend.info.base

import viper.gobra.ast.frontend.PDerivableType
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.base.SymbolTable.AdtClauseField
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.util.Violation

object DerivableTags {

  sealed trait DerivableTag {
    def name: String
    override def toString() = name
  }

  case class Default() extends DerivableTag {
    override def name: String = "Default"
  }

  case class Collection(t: Type, blackList: Vector[AdtClauseField]) extends DerivableTag {
    override def name: String = s"Collection[$t]"
  }

  def getDerivable(n: Option[PDerivableType])(typing: ExternalTypeInfo): DerivableTag = n match {
    case Some(value) => value.id.name match {
      case "Collection" =>
        val list : Vector[AdtClauseField] = value.blacklist.map {f => typing.getTypeInfo.regular(f.id)}
          .filter {o => o.isInstanceOf[AdtClauseField]}.map {_.asInstanceOf[AdtClauseField]}
        Collection(typing.symbType(value.typ.get), list)
      case _ => Violation.violation(s"No deriviable class $n found")
    }
    case None => Default()
  }

  def allDerivables: List[String] = List("Collection")
}
