package viper.gobra.frontend.info.implementation.typing

import viper.gobra.ast.frontend.{PExpression, PIdnNode, PNode, PReturn, AstPattern => ap}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.modifiers.ghost.{GhostModifier, GhostModifierUnit}
import viper.gobra.frontend.info.implementation.typing.modifiers.owner.{OwnerModifier, OwnerModifierUnit}
import viper.gobra.frontend.info.implementation.typing.modifiers.{Modifier, ModifierUnit}
import viper.gobra.frontend.info.implementation.typing.modifiers.Modifier.{Modifier, Modifiers}

trait ModifierTyping extends BaseTyping { this: TypeInfoImpl =>

  val ownerModifierUnit = new OwnerModifierUnit(this)
  val ghostModifierUnit = new GhostModifierUnit(this)

  val modifierUnits: Vector[ModifierUnit[_ <: Modifier]] = Vector(ownerModifierUnit, ghostModifierUnit)

  def getModifiers(n: PNode): Modifiers =
    modifierUnits.map(_.getModifier(n).get)

  def getFunctionLikeCallArgModifiers(call: ap.FunctionLikeCall): Vector[Modifiers] = {
    this.modifierUnits.map(_.getFunctionLikeCallArgModifier(call).get).transpose
  }

  def getReturnModifiers(n: PReturn): Vector[Modifiers] = {
    this.modifierUnits.map(_.getReturnModifier(n).get).transpose
  }

  def wellDefModifiers: WellDefinedness[PNode] = createWellDef {
    n: PNode => modifierUnits.flatMap(_.hasWellDefModifier(n).out)
  }

  override def getOwnerModifier(n: PNode): OwnerModifier = ownerModifierUnit.getModifier(n).get

  override def getGhostModifier(n: PNode): GhostModifier = ghostModifierUnit.getModifier(n).get
}
