package viper.gobra.frontend.info.implementation.typing.modifiers

import org.bitbucket.inkytonik.kiama.attribution.Attribution
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, noMessages}
import viper.gobra.ast.frontend.{PExpression, PNode}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import Modifier._
import viper.gobra.frontend.info.implementation.property.BaseProperty
import viper.gobra.frontend.info.implementation.typing.base.TypingComponents
import viper.gobra.util.{Memoization, Safety, Validity}

trait ModifierUnit[T <: Modifier] extends TypingComponents with BaseProperty {
  def hasWellDefModifier(ctx: TypeInfoImpl): WellDefinedness[PNode]

  def getModifier(ctx: TypeInfoImpl): ModifierTyping[PNode, T]

  def addressable(ctx: TypeInfoImpl)(exp: PExpression): Boolean = true

  trait ModifierTyping[-A, M] extends Safety[A, Option[M]] with Validity[A, Option[M]] {
    override def unsafe: Option[M] = None
    override def invalid(ret: Option[M]): Boolean = ret.isEmpty
  }

  private[modifiers] def createModifier[N <: AnyRef, M <: Modifier](inference: N => M)(implicit wellDef: WellDefinedness[N]): ModifierTyping[N, M] =
    new Attribution with ModifierTyping[N, M] with Memoization[N, Option[M]] {

      override def safe(n: N): Boolean = wellDef.valid(n)

      override def compute(n: N): Option[M] = Some(inference(n))
    }
}
