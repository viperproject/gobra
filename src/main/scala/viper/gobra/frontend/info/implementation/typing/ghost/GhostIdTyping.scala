package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend.PIdnNode
import viper.gobra.frontend.info.base.SymbolTable.{ActualRegular, GhostRegular, GhostifiedEntity}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait GhostIdTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostRegular(entity: GhostRegular, id: PIdnNode): Messages = entity match {
    case GhostifiedEntity(regular: ActualRegular) => wellDefActualRegular(regular, id)
  }

  private[typing] def ghostEntityType(entity: GhostRegular, id: PIdnNode): Type = entity match {
    case GhostifiedEntity(regular: ActualRegular) => actualEntityType(regular, id)
  }
}
