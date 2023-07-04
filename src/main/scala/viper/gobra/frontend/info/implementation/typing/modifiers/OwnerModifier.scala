package viper.gobra.frontend.info.implementation.typing.modifiers

import Modifier._

sealed trait OwnerModifier extends Modifier

object OwnerModifier {
  case object Shared extends OwnerModifier
  case object Exclusive extends OwnerModifier
}
