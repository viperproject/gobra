package viper.gobra.frontend.info.implementation.typing.modifiers

import viper.gobra.ast.frontend.{PAdd, PNode}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import org.bitbucket.inkytonik.kiama.util.Messaging.{error, noMessages}

object OwnerModifierUnit extends ModifierUnit[OwnerModifier] {
  override def hasWellDefinedModifier(ctx: TypeInfoImpl): WellDefinedness[PNode] = createIndependentWellDef[PNode] {
    case n: PAdd => error(n, s"Test error")
    case _ => noMessages
  }(n => ctx.children(n).forall(ctx.childrenWellDefined))

  override def getModifier(ctx: TypeInfoImpl): ModifierTyping[PNode, OwnerModifier] = createModifier[PNode, OwnerModifier] {
    case n: PNode => OwnerModifier.Shared
  }(hasWellDefinedModifier(ctx))

  override def addressable: Boolean = true
}
