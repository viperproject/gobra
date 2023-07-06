package viper.gobra.frontend.info.implementation.typing

import viper.gobra.ast.frontend.{PExpression, PIdnNode, PNode}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.modifiers.{GhostModifierUnit, Modifier, ModifierUnit, OwnerModifier, OwnerModifierUnit}

trait ModifierTyping extends BaseTyping { this: TypeInfoImpl =>

  final val modifierUnits: Vector[ModifierUnit[_ <: Modifier.Modifier]] = Vector(OwnerModifierUnit, GhostModifierUnit)

  def wellDefModifiers: WellDefinedness[PNode] = createWellDef {
    n: PNode => modifierUnits.flatMap(_.hasWellDefModifier(this)(n).out)
  }

  override def getOwnerModifier(expr: PExpression): OwnerModifier = OwnerModifierUnit.getModifier(this)(expr).get

  override def getVarOwnerModifier(id: PIdnNode): OwnerModifier = OwnerModifierUnit.getVarModifier(this)(id)
}
