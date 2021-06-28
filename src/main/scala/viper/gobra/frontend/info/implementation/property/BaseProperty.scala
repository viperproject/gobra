// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend.PNode

trait BaseProperty {

  case class PropertyResult(opt: Option[Vector[String]]) {
    private val newLine: String = "\n\t"

    def asError(src: PNode): Messages = opt.fold(noMessages)(msgs =>
      message(src,
        if (msgs.isEmpty) ""
        else if (msgs.size == 1) msgs.head
        else "multiple errors:" + msgs.mkString(s"${newLine}error: ", s"${newLine}error: ", "")
      )
    )

    def asReason(src: PNode, msg: => String): Messages = opt.fold(noMessages)(reasons =>
      message(
        src,
        if (reasons.isEmpty) msg
        else msg + reasons.mkString(s"${newLine}reason: ", s"${newLine}reason: ", "")
      )
    )

    def asResult(msg: => String): PropertyResult = opt.fold(successProp)(reasons =>
      failedProp(
        if (reasons.isEmpty) msg
        else msg + reasons.mkString(s"${newLine}reason: ", s"${newLine}reason: ", ""),
      )
    )

    def holds: Boolean = opt.isEmpty

    def and(other: PropertyResult): PropertyResult = PropertyResult(
      (opt, other.opt) match {
        case (Some(l), Some(r)) => Some(l ++ r)
        case (Some(l), None) => Some(l)
        case (None, Some(r)) => Some(r)
        case (None, None) => None
      }
    )

    def or(other: PropertyResult): PropertyResult = PropertyResult(
      (opt, other.opt) match {
        case (Some(l), Some(r)) => Some(l ++ r)
        case _ => None
      }
    )

    def distinct: PropertyResult = PropertyResult(opt.map(_.distinct))
  }

  object PropertyResult {
    def bigAnd(xs: Vector[PropertyResult]): PropertyResult =
      xs.foldLeft(successProp) { case (l,r) => l and r }
  }

  def propForall[A](base: Iterable[A], prop: Property[A]): PropertyResult =
    base.foldLeft(successProp) { case (l, r) => l and prop.result(r) }

  def successProp: PropertyResult = PropertyResult(None)

  def failedProp(label: => String, cond: Boolean = true): PropertyResult =
    PropertyResult(if (cond) Some(Vector(label)) else None)

  def failedPropFromMessages(msgs: Vector[String]): PropertyResult = PropertyResult(
    if (msgs.isEmpty) None else Some(msgs)
  )

  def errorProp(msgs: Vector[String] = Vector.empty): PropertyResult = PropertyResult(Some(msgs))



  case class Property[A](gen: A => PropertyResult) extends (A => Boolean) {
    def result(n: A): PropertyResult = gen(n)

    def errors(n: A)(src: PNode): Messages = result(n).asError(src)

    override def apply(n: A): Boolean = result(n).holds

    def before[Z](f: Z => A): Property[Z] = Property[Z](n => gen(f(n)))
  }

  def createProperty[A](gen: A => PropertyResult): Property[A] = Property[A](gen)

  def createFlatProperty[A](msg: A => String)(check: A => Boolean): Property[A] =
    createProperty[A](n => failedProp(s"property error: ${msg(n)}", !check(n)))

  def createFlatPropertyWithReason[A](msg: A => String)(check: A => PropertyResult): Property[A] =
    createProperty[A]{ n => check(n).asResult(s"property error: ${msg(n)}") }

  def createBinaryProperty[A](name: String)(check: A => Boolean): Property[A] =
    createFlatProperty((n: A) => s"got $n that is not $name")(check)


}
