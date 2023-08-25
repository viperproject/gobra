package viper.gobra.frontend.info.implementation.typing.modifiers

import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend.{PAssignee, PExpression, PIdnNode, PNode, PReturn, AstPattern => ap}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import Modifier._
import viper.gobra.frontend.info.implementation.property.BaseProperty
import viper.gobra.frontend.info.implementation.typing.base.TypingComponents
import viper.gobra.util.{Memoization, Safety, Validity}

trait ModifierUnit[T <: Modifier] extends TypingComponents {
  def ctx: TypeInfoImpl

  def hasWellDefModifier: WellDefinedness[PNode]

  def getModifier: ModifierTyping[PNode, T]

  def getFunctionLikeCallArgModifier: ModifierTyping[ap.FunctionLikeCall, Vector[T]]

  def getReturnModifier: ModifierTyping[PReturn, Vector[T]]

  def addressable(exp: PExpression): Boolean = true

  def assignableTo(right: Modifier, left: Modifier): Boolean = true

  trait ModifierTyping[-A, M] extends Safety[A, Option[M]] with Validity[A, Option[M]] {
    override def unsafe: Option[M] = None
    override def invalid(ret: Option[M]): Boolean = ret.isEmpty
  }

  private[modifiers] def createModifier[N <: AnyRef, M <: Modifier](inference: N => M)(implicit wellDef: WellDefinedness[N]): ModifierTyping[N, M] =
    new Attribution with ModifierTyping[N, M] with Memoization[N, Option[M]] {

      override def safe(n: N): Boolean = wellDef.valid(n)

      override def compute(n: N): Option[M] = Some(inference(n))
    }

  private[modifiers] def createVectorModifier[N <: AnyRef, M <: Modifier](inference: N => Vector[M]): ModifierTyping[N, Vector[M]] =
    new Attribution with ModifierTyping[N, Vector[M]] with Memoization[N, Option[Vector[M]]] {

      override def safe(n: N): Boolean = true

      override def compute(n: N): Option[Vector[M]] = Some(inference(n))
    }
}
