package viper.gobra.frontend.info.implementation.typing

import viper.gobra.ast.frontend.{PExpression, PIdnNode, PNode, PReturn, AstPattern => ap}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.modifiers.ghost.{GhostModifier, GhostModifierUnit}
import viper.gobra.frontend.info.implementation.typing.modifiers.owner.{OwnerModifier, OwnerModifierUnit}
import viper.gobra.frontend.info.implementation.typing.modifiers.{Modifier, ModifierUnit}
import viper.gobra.frontend.info.implementation.typing.modifiers.Modifier.{Modifier, Modifiers}

/**
  * Provides access to the modifier mapping and well-definedness
  */
trait ModifierTyping extends BaseTyping { this: TypeInfoImpl =>

  val ownerModifierUnit = new OwnerModifierUnit(this)
  val ghostModifierUnit = new GhostModifierUnit(this)

  /**
    * Register new type modifiers here
    */
  val modifierUnits: Vector[ModifierUnit[_ <: Modifier]] = Vector(ownerModifierUnit, ghostModifierUnit)

  /**
    * Gets all modifiers for a node
    */
  def getModifiers(n: PNode): Modifiers =
    modifierUnits.map(_.getModifier(n).get)

  /**
    * Gets expected argument modifiers for a function like call
    */
  def getExpectedFunctionLikeCallArgModifiers(call: ap.FunctionLikeCall): Vector[Modifiers] = {
    this.modifierUnits.map(_.getExpectedFunctionLikeCallArgModifier(call).get).transpose
  }

  /**
    * Gets expected return modifiers for a return
    */
  def getExpectedReturnModifiers(n: PReturn): Vector[Modifiers] = {
    this.modifierUnits.map(_.getExpectedReturnModifier(n).get).transpose
  }

  /**
    * Checks well-definedness of all modifiers
    */
  def wellDefModifiers: WellDefinedness[PNode] = createWellDef {
    n: PNode => modifierUnits.flatMap(_.hasWellDefModifier(n).out)
  }

  override def getOwnerModifier(n: PNode): OwnerModifier = ownerModifierUnit.getModifier(n).get

  override def getGhostModifier(n: PNode): GhostModifier = ghostModifierUnit.getModifier(n).get
}
