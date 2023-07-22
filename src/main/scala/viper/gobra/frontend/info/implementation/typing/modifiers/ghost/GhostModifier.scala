package viper.gobra.frontend.info.implementation.typing.modifiers.ghost

import viper.gobra.frontend.info.implementation.typing.modifiers.Modifier._

sealed trait GhostModifier extends Modifier

object GhostModifier {
  case object Actual extends GhostModifier

  case object Ghost extends GhostModifier
}
