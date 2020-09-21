// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait TypeEncoding extends Generator {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  def typ(ctx: Context): in.Type ==> vpr.Type = PartialFunction.empty

  /**
    * Translates variables that have the scope of a code body. Returns the encoded Viper variables.
    * As the default encoding, returns a local var with the argument name and the translated type.
    */
  def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = {
    case v if typ(ctx) isDefinedAt v.typ =>
      val (pos, info, errT) = v.vprMeta
      val t = typ(ctx)(v.typ)
      vpr.LocalVarDecl(v.id, t)(pos, info, errT)
  }

  /**
    * Translates variables that have a global scope. Returns the encoded Viper expression.
    * As the default encoding, returns a call to a function with the argument name and the translated type.
    */
  def globalVar(ctx: Context): in.GlobalVar ==> CodeWriter[vpr.Exp] = {
    case v: in.GlobalConst if typ(ctx) isDefinedAt v.typ =>
      unit(ctx.fixpoint.get(v)(ctx))
  }

  /**
    * Returns extensions to the precondition for an in-parameter.
    */
  def precondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Returns extensions to the postcondition for an out-parameter
    */
  def postcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Returns initialization code for a declared location with the scope of a body.
    * The initialization code has to guarantee that:
    * (1) the declared variable has its default value afterwards
    * (2) all permissions for the declared variables are owned afterwards
    *
    * The default implements:
    * Initialize[loc: T°] -> assume [loc == dflt(T)]
    * Initialize[loc: T@] -> inhale Footprint[loc]; assume [loc == dflt(T)]
    */
  def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = {
    case loc :: t / m if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = loc.vprMeta
      for {
        footprint <- m match {
          case Exclusive => unit(vpr.TrueLit()(pos, info, errT))
          case Shared => addressFootprint(ctx)(loc)
        }
        eq <- equal(ctx)(loc, in.DfltVal(t)(loc.info), loc)
      } yield vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT)
  }

  /**
    * Encodes an assignment.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    *
    * To avoid conflicts with other encodings, an encoding for type T
    * should be defined at the following left-hand sides:
    * (1) exclusive variables of type T
    * (2) exclusive operations on type T (e.g. a field access for structs)
    * (3) shared expressions of type T
    * In particular, being defined at shared operations on type T causes conflicts with (3)
    *
    * The default implements:
    * [v: T° = rhs] -> VAR[v] = [rhs]
    * [loc: T@ = rhs] -> exhale Footprint[loc]; inhale Footprint[loc] && [loc == rhs]
    */
  def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = {
    case (in.Assignee((v: in.BodyVar) :: t / Exclusive), rhs, src) if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = src.vprMeta
      for {
        vRhs <- ctx.expr.translate(rhs)(ctx)
        vLhs = variable(ctx)(v).localVar
      } yield vpr.LocalVarAssign(vLhs, vRhs)(pos, info, errT)

    case (in.Assignee((loc: in.Location) :: t / Shared), rhs, src) if  typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = src.vprMeta
      for {
        footprint <- addressFootprint(ctx)(loc)
        eq <- equal(ctx)(loc, rhs, src)
        _ <- write(vpr.Exhale(footprint)(pos, info, errT))
        inhale = vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT)
      } yield inhale
  }

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    *
    * The default implements: [lhs: T == rhs] -> [lhs] == [rhs]
    */
  def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: t, rhs, src) if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = src.vprMeta
      for {
        vLhs <- ctx.expr.translate(lhs)(ctx)
        vRhs <- ctx.expr.translate(rhs)(ctx)
      } yield vpr.EqCmp(vLhs, vRhs)(pos, info, errT)
  }

  /**
    * Encodes expressions as r-values, i.e. values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    * (2) shared expression of type T
    * The default implements exclusive variables and constants with [[variable]] and [[globalVar]], respectively.
    */
  def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case (v: in.BodyVar) :: t / Exclusive if typ(ctx).isDefinedAt(t) => unit(variable(ctx)(v).localVar)
    case (v: in.GlobalVar) :: t / Exclusive if typ(ctx).isDefinedAt(t) => globalVar(ctx)(v)
  }

  /**
    * Encodes expressions as l-values, i.e. values that do occupy some identifiable location in memory.
    * This includes literals and default values.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at:
    * (1) shared variables of type T
    * (2) shared operations on T
    */
  def lValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes the reference of an expression.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at shared operations on type T.
    * The default implements shared variables with [[variable]].
    */
  def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = {
    case (v: in.BodyVar) :: t / Shared if typ(ctx).isDefinedAt(t) => unit(variable(ctx)(v).localVar)
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    */
  def addressFootprint(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = PartialFunction.empty


  /**
    * Encodes statements.
    * This includes make-statements.
    *
    * The default implements:
    * [v: *T = make(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
    */
  def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case make@ in.Make(target, in.CompositeObject(lit :: t)) if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = make.vprMeta
      val z = in.LocalVar(Names.freshName, target.typ.withAddressability(Exclusive))(make.info)
      val zDeref = in.Deref(z)(make.info)
      for {
        _ <- local(ctx.typeEncoding.variable(ctx)(z))
        footprint <- addressFootprint(ctx)(zDeref)
        eq <- equal(ctx)(zDeref, lit, make)
        _ <- write(vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT))
        ass <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(target), z, make)
      } yield ass
  }

  /**
    * Alternative version of `orElse` to simplify delegations to super implementations.
    * @param dflt default partial function, applied if 'f' is not defined at argument
    * @return
    */
  protected def default[X, Y](dflt: X ==> Y)(f: X ==> Y): X ==> Y = f orElse dflt
}

