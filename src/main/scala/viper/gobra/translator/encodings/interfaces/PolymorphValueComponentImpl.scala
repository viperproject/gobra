// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.util.TypePatterns.Sh

/** Polymorphic value that can fit all countable types. */
class PolymorphValueComponentImpl(handle: PolymorphValueInterfaceHandle) extends PolymorphValueComponent {

  private val imageType: vpr.Type = vpr.Ref

  private val domainName = Names.polyValueDomain
  private val extraDomainName = s"${domainName}AdditionalAxioms"
  private val typeVar = vpr.TypeVar("T")

  /**
    * function box(x: T): Ref
    * function unbox(y: Ref): T
    *
    * */
  private lazy val (boxFunc, unboxFunc) = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val yDecl = vpr.LocalVarDecl("y", imageType)()

    val box = vpr.DomainFunc(
      name = s"${Names.polyValueBoxFunc}_$domainName",
      formalArgs = Vector(xDecl),
      typ = imageType
    )(domainName = domainName)

    val unbox = vpr.DomainFunc(
      name = s"${Names.polyValueUnboxFunc}_$domainName",
      formalArgs = Vector(yDecl),
      typ = typeVar
    )(domainName = domainName)

    isUsed = true

    (box, unbox)
  }


  /** Type of polymorphic value. */
  override def typ()(ctx: Context): vpr.Type = imageType

  /** Puts the expression into an polymorphic value. */
  override def box(arg: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    if (arg.typ == imageType) {
      arg
    } else {
      vpr.DomainFuncApp(boxFunc, Seq(arg), Map(typeVar -> arg.typ))(pos, info, errT)
    }
  }

  /** Extracts an expression from the polymorphic value. */
  override def unbox(arg: vpr.Exp, typ: in.Type)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    val vprType = ctx.typeEncoding.typ(ctx)(typ)
    if (vprType == imageType) {
      arg
    } else {
      genAxiom(typ)(ctx)
      vpr.DomainFuncApp(unboxFunc, Seq(arg), Map(typeVar -> vprType))(pos, info, errT)
    }
  }


  /**
    * Generates the following axiom:
    *
    *   // If T is a finite type:
    *
    *   axiom {
    *     forall y: Itf :: {(unbox(polyValOf(y)): [T])} typeOf(y) == [T] ==> box((unbox(polyValOf(y)): [T])) == polyValOf(y)
    *   }
    *
    *   // Otherwise:
    *
    *   axiom { forall y: Ref :: {(unbox(y): [T])} box((unbox(y): [T])) == y }
    *
    *
    *   Info:
    *   The first axiom is conservative, but also complete.
    *   Every type could be handled by it. However, the second axiom is more lightweight.
    *
    */
  private def genAxiom(t: in.Type)(ctx: Context): Unit = {
    if (!genAxiomSet.contains(t)) {
      genAxiomSet += t

      val vprT = ctx.typeEncoding.typ(ctx)(t)
      val typeVarMap = Map(typeVar -> vprT)

      def boxApp(arg: vpr.Exp): vpr.DomainFuncApp = vpr.DomainFuncApp(
        func = boxFunc, args = Vector(arg), typVarMap = typeVarMap
      )()

      def unboxApp(arg: vpr.Exp): vpr.DomainFuncApp = vpr.DomainFuncApp(
        func = unboxFunc, args = Vector(arg), typVarMap = typeVarMap
      )()


      val axiom = if (finiteCardinality(t)(ctx.lookup)) {
        val yDecl = vpr.LocalVarDecl("y", handle.typ(imageType)(ctx))()
        val y = yDecl.localVar
        val z = handle.polyValOf(y)()(ctx) // polyValOf(y)

        vpr.AnonymousDomainAxiom(
          vpr.Forall(
            variables = Seq(yDecl),
            triggers = Seq(vpr.Trigger(Seq(unboxApp(z)))()),
            exp = vpr.Implies(
              // typeOf(y) == [T]
              vpr.EqCmp(handle.dynTypeOf(y)()(ctx), handle.typeToExpr(t)()(ctx))(),
              // box((unbox(polyValOf(y)): [T])) == polyValOf(y)
              vpr.EqCmp(boxApp(unboxApp(z)), z)()
            )()
          )()
        )(domainName = extraDomainName)

      }  else {

        val yDecl = vpr.LocalVarDecl("y", imageType)()
        val y = yDecl.localVar

        vpr.AnonymousDomainAxiom(
          vpr.Forall(
            variables = Seq(yDecl),
            triggers = Seq(vpr.Trigger(Seq(unboxApp(y)))()),
            exp = vpr.EqCmp(boxApp(unboxApp(y)), y)() // box((unbox(y): [T])) == y
          )()
        )(domainName = extraDomainName)
      }

      genAxioms ::= axiom
    }
  }
  private var genAxiomSet: Set[in.Type] = Set.empty
  private var genAxioms: List[vpr.DomainAxiom] = List.empty



  /**
    * Generates the following domains:
    *
    * domain Poly[T] {
    *   function box(x: T): Ref
    *   function unbox(y: Ref): T
    *
    *   axiom { forall x: T :: {box(x)} (unbox(box(x)): T) == x }
    * }
    *
    * domain PolyAdditionalAxioms {
    *   // contains generated axioms [[genAxioms]]
    *   ...
    * }
    *
    */
  private def genDomain: Vector[vpr.Member] = {

    val typeVars = Vector(typeVar)
    val typeVarMap = Map(typeVar -> typeVar)

    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val x = xDecl.localVar

    def boxApp(arg: vpr.Exp): vpr.DomainFuncApp = vpr.DomainFuncApp(
      func = boxFunc, args = Vector(arg), typVarMap = typeVarMap
    )()

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

    val mainDomain = vpr.Domain(
      name = domainName,
      functions = Seq(boxFunc, unboxFunc),
      axioms = Seq(boxInj),
      typVars = typeVars
    )()

    if (genAxioms.isEmpty) Vector(mainDomain)
    else {
      val extraDomain = vpr.Domain(
        name = extraDomainName,
        functions = Seq.empty,
        axioms = genAxioms
      )()
      Vector(mainDomain,extraDomain)
    }
  }
  private var isUsed: Boolean = false

  override def finalize(col: Collector): Unit = if (isUsed) { genDomain.foreach(col.addMember) }

  /** Returns whether 'typ' has finite cardinality. */
  private def finiteCardinality(typ: in.Type)(ctx: in.DefinedT => in.Type): Boolean = {
    def go(t: in.Type): Boolean = finiteCardinality(t)(ctx)

    val res = typ match {
      case Sh() => false
      case t: in.DefinedT => go(ctx(t))
      case _: in.BoolT | in.VoidT => true
      case t: in.ArrayT => t.length == 0 || go(t.elems)
      case t: in.StructT => t.fields.forall(f => go(f.typ))
      case t: in.TupleT => t.ts.forall(go)
      case t: in.OptionT => go(t.t)
      case _ => false
    }

    res
  }
}

trait PolymorphValueInterfaceHandle {

  /** Returns type of an interface */
  def typ(polyType: vpr.Type)(ctx: Context): vpr.Type

  /** Creates an interface */
  def create(polyVal: vpr.Exp, dynType: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Returns dynamic type of an interface */
  def dynTypeOf(itf: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Return polymorphic value of an interface */
  def polyValOf(itf: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Translates Gobra types into Viper type expressions. */
  def typeToExpr(typ: in.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp
}