package viper.gobra.frontend.info.implementation.typing.base

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, noMessages}
import viper.gobra.ast.frontend.PNode
import viper.gobra.frontend.info.implementation.typing.modifiers.OwnerModifierUnit
import viper.gobra.util.{Memoization, Safety, Validity}

trait TypingComponents {

  sealed trait ValidityMessages {
    def out: Messages

    def valid: Boolean
  }

  private[typing] case object UnsafeForwardMessage extends ValidityMessages {
    override val out: Messages = noMessages
    override val valid: Boolean = false
  }

  private[typing] def unsafeMessage(cond: Boolean): ValidityMessages =
    if (cond) UnsafeForwardMessage else LocalMessages(noMessages)

  private[typing] case class LocalMessages(override val out: Messages) extends ValidityMessages {
    override def valid: Boolean = out.isEmpty
  }

  trait Error[-A] extends Validity[A, ValidityMessages] {
    override def invalid(ret: ValidityMessages): Boolean = !ret.valid
  }

  trait WellDefinedness[-A] extends Error[A]

  private[typing] def createIndependentWellDef[T <: PNode](check: T ==> Messages)(pre: T => Boolean): WellDefinedness[T] =
    new Attribution with WellDefinedness[T] with Safety[T, ValidityMessages] with Memoization[T, ValidityMessages] {

      override def safe(n: T): Boolean = pre(n)

      override def unsafe: ValidityMessages = UnsafeForwardMessage

      override def compute(n: T): ValidityMessages =
        LocalMessages(if (check.isDefinedAt(n)) check(n) else noMessages)
    }

  private[typing] def createWellDefWithValidityMessages[T <: PNode](check: T => ValidityMessages)(pre: T => Boolean): WellDefinedness[T] =
    new Attribution with WellDefinedness[T] with Safety[T, ValidityMessages] with Memoization[T, ValidityMessages] {

      override def safe(n: T): Boolean = pre(n)

      override def unsafe: ValidityMessages = UnsafeForwardMessage

      override def compute(n: T): ValidityMessages = check(n)
    }

  private[typing] def createWellDefInference[X <: AnyRef, Z](wellDef: X => Boolean)(inference: X => Z): X => Option[Z] =
    new Attribution with Safety[X, Option[Z]] with Memoization[X, Option[Z]] {

      override def safe(n: X): Boolean = wellDef(n)

      override def unsafe: Option[Z] = None

      override def compute(n: X): Option[Z] = Some(inference(n))
    }
}
