package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostTypeTyping extends BaseTyping { this : TypeInfoImpl =>

  private[typing] def wellDefGhostType(typ : PGhostType) : Messages = typ match {
    case PSequenceType(elem) => isType(elem).out
    case PSetType(elem) => isType(elem).out
    case PMultisetType(elem) => isType(elem).out
  }

  private[typing] def ghostTypeType(typ : PGhostType) : Type = typ match {
    case PSequenceType(elem) => SequenceT(typeType(elem))
    case PSetType(elem) => SetT(typeType(elem))
    case PMultisetType(elem) => MultisetT(typeType(elem))
  }
}
