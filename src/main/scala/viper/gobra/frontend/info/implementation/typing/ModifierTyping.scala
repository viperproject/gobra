package viper.gobra.frontend.info.implementation.typing

import viper.gobra.ast.frontend.{PExpression, PIdnNode, PNode}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.modifiers.{GhostModifierUnit, Modifier, ModifierUnit, OwnerModifier, OwnerModifierUnit}

trait ModifierTyping extends BaseTyping { this: TypeInfoImpl =>

  val ownerModifierUnit = new OwnerModifierUnit(this)
  val ghostModifierUnit = new GhostModifierUnit(this)

  val modifierUnits: Vector[ModifierUnit[_ <: Modifier.Modifier]] = Vector(ownerModifierUnit, ghostModifierUnit)

  def wellDefModifiers: WellDefinedness[PNode] = createWellDef {
    n: PNode => modifierUnits.flatMap(_.hasWellDefModifier(n).out)
  }

  override def getOwnerModifier(expr: PExpression): OwnerModifier = ownerModifierUnit.getModifier(expr).get

  override def getVarOwnerModifier(id: PIdnNode): OwnerModifier = ownerModifierUnit.getVarModifier(id)
}
