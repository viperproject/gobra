package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend.PIdnNode
import viper.gobra.frontend.info.base.SymbolTable.{BoundVariable, GhostRegular, Predicate}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

trait GhostIdTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostRegular(entity: GhostRegular, id: PIdnNode): Messages = entity match {

    case boundVariable: BoundVariable => message(id, s"variable $id is not defined", ! {
      wellDefType.valid(boundVariable.decl.typ)
    })

    case predicate: Predicate => noMessages
  }

  private[typing] def ghostEntityType(entity: GhostRegular, id: PIdnNode): Type = entity match {
    case boundVariable: BoundVariable => typeType(boundVariable.decl.typ)
    case _ => violation("untypable")
  }
}
