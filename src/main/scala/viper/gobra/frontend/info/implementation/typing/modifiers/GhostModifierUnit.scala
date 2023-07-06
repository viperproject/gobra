package viper.gobra.frontend.info.implementation.typing.modifiers
import viper.gobra.ast.frontend.PNode
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import org.bitbucket.inkytonik.kiama.util.Messaging.{error, noMessages}

object GhostModifierUnit extends ModifierUnit[GhostModifier] {
  override def hasWellDefModifier(ctx: TypeInfoImpl): WellDefinedness[PNode] = createIndependentWellDef[PNode] {
    case _ => noMessages
  }(n => ctx.children(n).forall(ctx.childrenWellDefined))

  override def getModifier(ctx: TypeInfoImpl): ModifierTyping[PNode, GhostModifier] = createModifier[PNode, GhostModifier] {
    case _ => GhostModifier.Actual
  }(hasWellDefModifier(ctx))
}
