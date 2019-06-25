package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend.PGhostType
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostTypeTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostType(typ: PGhostType): Messages = typ match {
    case _ => ???
  }

  private[typing] def ghostTypeType(typ: PGhostType): Type = typ match {
    case _ => ???
  }
}
