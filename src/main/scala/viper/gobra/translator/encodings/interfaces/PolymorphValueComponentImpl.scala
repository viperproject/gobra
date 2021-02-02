// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}
import viper.gobra.translator.Names

/** Polymorphic value that can fit all countable types. */
class PolymorphValueComponentImpl extends PolymorphValueComponent {

  private val imageType: vpr.Type = vpr.Ref

  /**
    * Generates the following domain:
    *
    * domain RefBox[T] {
    *   function box(x: T): Ref
    *   function unbox(y: Ref): T
    *
    *   axiom { forall y: Ref :: {(unbox(y): T)} box((unbox(y): T)) == y }
    *   axiom { forall x: T :: {box(x)} (unbox(box(x)): T) == x }
    * }
    */
  private lazy val (domain, boxFunc, unboxFunc) = {
    val domainName = Names.polyValueDomain
    val typeVar = vpr.TypeVar("T")
    val typeVars = Vector(typeVar)
    val typeVarMap = Map(typeVar -> typeVar)

    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val x = xDecl.localVar
    val yDecl = vpr.LocalVarDecl("y", imageType)()
    val y = yDecl.localVar

    val boxFunc = vpr.DomainFunc(
      name = s"${Names.polyValueBoxFunc}_$domainName",
      formalArgs = Vector(xDecl),
      typ = imageType
    )(domainName = domainName)

    def boxApp(arg: vpr.Exp): vpr.DomainFuncApp = vpr.DomainFuncApp(
      func = boxFunc, args = Vector(arg), typVarMap = typeVarMap
    )()

    val unboxFunc = vpr.DomainFunc(
      name = s"${Names.polyValueUnboxFunc}_$domainName",
      formalArgs = Vector(yDecl),
      typ = typeVar
    )(domainName = domainName)

    def unboxApp(arg: vpr.Exp): vpr.DomainFuncApp = vpr.DomainFuncApp(
      func = unboxFunc, args = Vector(arg), typVarMap = typeVarMap
    )()

    val boxInj = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        variables = Seq(xDecl),
        triggers = Seq(vpr.Trigger(Seq(boxApp(x)))()),
        exp = vpr.EqCmp(unboxApp(boxApp(x)), x)()
      )()
    )(domainName = domainName)

    val unboxInj = vpr.AnonymousDomainAxiom(
      vpr.Forall(
        variables = Seq(yDecl),
        triggers = Seq(vpr.Trigger(Seq(unboxApp(y)))()),
        exp = vpr.EqCmp(boxApp(unboxApp(y)), y)()
      )()
    )(domainName = domainName)

    val domain = vpr.Domain(
      name = domainName,
      functions = Seq(boxFunc, unboxFunc),
      axioms = Seq(boxInj, unboxInj),
      typVars = typeVars
    )()

    generatedDomain = true

    (domain, boxFunc, unboxFunc)
  }

  /** Type of polymorphic value. */
  override def typ()(ctx: Context): vpr.Type = imageType

  /** Puts the expression into an polymorphic value. */
  override def box(arg: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    if (arg.typ == imageType) {
      arg
    } else {
      vpr.DomainFuncApp(boxFunc, Seq(arg), Map(vpr.TypeVar("T") -> arg.typ))(pos, info, errT)
    }
  }

  /** Extracts an expression from the polymorphic value. */
  override def unbox(arg: vpr.Exp, typ: vpr.Type)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    if (typ == imageType) {
      arg
    } else {
      vpr.DomainFuncApp(unboxFunc, Seq(arg), Map(vpr.TypeVar("T") -> typ))(pos, info, errT)
    }
  }

  private var generatedDomain: Boolean = false

  override def finalize(col: Collector): Unit = if (generatedDomain) col.addMember(domain)

}
