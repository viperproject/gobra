// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend.PNode

trait BaseProperty {

  case class PropertyResult(opt: Option[PNode => Messages]) {
    def errors(src: PNode): Messages = opt.fold(noMessages)(_.apply(src))

    def holds: Boolean = opt.isEmpty

    def and(other: PropertyResult): PropertyResult = PropertyResult((opt, other.opt) match {
      case (Some(l), Some(r)) => Some(src => l(src) ++ r(src))
      case (Some(l), None) => Some(l)
      case (None, Some(r)) => Some(r)
      case (None, None) => None
    })

    def or(other: PropertyResult): PropertyResult = PropertyResult((opt, other.opt) match {
      case (Some(l), Some(r)) => Some(src => l(src) ++ r(src))
      case _ => None
    })
  }

  def propForall[A](base: Iterable[A], prop: Property[A]): PropertyResult =
    base.foldLeft(successProp) { case (l, r) => l and prop.result(r) }


  def successProp: PropertyResult = PropertyResult(None)

  def failedProp(label: => String, cond: Boolean = true): PropertyResult =
    PropertyResult(if (cond) Some(src => message(src, label)) else None)

  case class Property[A](gen: A => PropertyResult) extends (A => Boolean) {
    def result(n: A): PropertyResult = gen(n)

    def errors(n: A)(src: PNode): Messages = result(n).errors(src)

    override def apply(n: A): Boolean = result(n).holds

    def before[Z](f: Z => A): Property[Z] = Property[Z](n => gen(f(n)))
  }

  def createProperty[A](gen: A => PropertyResult): Property[A] = Property[A](gen)

  def createFlatProperty[A](msg: A => String)(check: A => Boolean): Property[A] =
    createProperty[A](n => failedProp(s"property error: ${msg(n)}", !check(n)))

  def createBinaryProperty[A](name: String)(check: A => Boolean): Property[A] =
    createFlatProperty((n: A) => s"got $n that is not $name")(check)
}
