// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.encodings.TypeEncoding
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

private[structs] object StructEncoding {
  /** Parameter of struct components. */
  type ComponentParameter = Vector[(vpr.Type, Boolean)]

  /** Computes the component parameter. */
  def cptParam(fields: Vector[in.Field])(ctx: Context): ComponentParameter = {
    fields.map(f => (ctx.typeEncoding.typ(ctx)(f.typ), f.ghost))
  }
}

class StructEncoding extends TypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.{ViperUtil => VU}
  import StructEncoding.{ComponentParameter, cptParam}

  private val ex: ExclusiveStructComponent = new ExclusiveStructComponent{ // For now, we use a simple tuple domain.
    override def typ(vti: ComponentParameter)(ctx: Context): vpr.Type = ctx.tuple.typ(vti.map(_._1))
    override def get(base: vpr.Exp, idx: Int, vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = ctx.tuple.get(base, idx, vti.size)
    override def create(args: Vector[vpr.Exp], vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = ctx.tuple.create(args)
  }

  private val sh: SharedStructComponent = new SharedStructComponent { // For now, we use a simple tuple domain.
    override def typ(vti: ComponentParameter)(ctx: Context): vpr.Type = ctx.tuple.typ(vti.map(_._1))
    override def get(base: vpr.Exp, idx: Int, vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = ctx.tuple.get(base, idx, vti.size)
  }

  override def finalize(col: Collector): Unit = {
    ex.finalize(col)
    sh.finalize(col)
  }

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Struct(fs) / m =>
      val vti = cptParam(fs)(ctx)
      m match {
        case Exclusive => ex.typ(vti)(ctx)
        case Shared    => sh.typ(vti)(ctx)
      }
  }

  /**
    * Returns initialization code for a declared location with the scope of a body.
    * The initialization code has to guarantee that:
    * (1) the declared variable has its default value afterwards
    * (2) all permissions for the declared variables are owned afterwards
    *
    * Super implements:
    * Initialize[loc: T°] -> assume [loc == dflt(T)]
    * Initialize[loc: T@] -> inhale Footprint[loc]; assume [loc == dflt(T)]
    *
    * Initialize[l: Struct{F}] -> FOREACH f in F: Initialize[l.f]
    */
  override def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = {
    case l :: ctx.Struct(fs) =>
      seqns(fieldAccesses(l, fs).map(x => ctx.typeEncoding.initialization(ctx)(x)))
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
    * Super implements:
    * [v: T° = rhs] -> VAR[v] = [rhs]
    * [loc: T@ = rhs] -> exhale Footprint[loc]; inhale Footprint[loc] && [loc == rhs]
    *
    * [e.f: Struct{F}° = rhs] -> [ e = e[f := rhs] ]
    * [lhs: Struct{F}@ = rhs] -> FOREACH f in F: [lhs.f = rhs.f]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee((fa: in.FieldRef) :: _ / Exclusive), rhs, src) =>
      ctx.typeEncoding.assignment(ctx)(in.Assignee(fa.recv), in.StructUpd(fa.recv, fa.field, rhs)(src.info), src)

    case (in.Assignee(lhs :: ctx.Struct(lhsFs) / Shared), rhs :: ctx.Struct(rhsFs), src) =>
      val lhsFAs = fieldAccesses(lhs, lhsFs).map(in.Assignee.Field)
      val rhsFAs = fieldAccesses(rhs, rhsFs)
      seqns((lhsFAs zip rhsFAs).map{ case (lhsFA, rhsFA) => ctx.typeEncoding.assignment(ctx)(lhsFA, rhsFA, src) })
  }

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    *
    * Super implements: [lhs: T == rhs] -> [lhs] == [rhs]
    *
    * R[(lhs: Struct{F}) == rhs] -> AND f in F: lhs.f == rhs.f
    */
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: ctx.Struct(lhsFs), rhs :: ctx.Struct(rhsFs), src) =>
      val lhsFAccs = fieldAccesses(lhs, lhsFs)
      val rhsFAccs = fieldAccesses(rhs, rhsFs)
      val equalFields = sequence((lhsFAccs zip rhsFAccs).map{ case (lhsFA, rhsFA) => ctx.typeEncoding.equal(ctx)(lhsFA, rhsFA, src) })
      val (pos, info, errT) = src.vprMeta
      equalFields.map(VU.bigAnd(_)(pos, info, errT))
  }

  /**
    * Encodes expressions as r-values, i.e. values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    * (2) shared expression of type T
    * Super implements exclusive variables and constants with [[variable]] and [[globalVar]], respectively.
    *
    * R[ (e: Struct{F}°).f ] -> ex_struct_get([e], f, F)
    * R[ (base: Struct{F})[f = e] ] -> ex_struct_upd([base], f, [e], F)
    * R[ dflt(Struct{F}) ] -> create_ex_struct( [T] | (f: T) in F )
    * R[ structLit(E) ] -> create_ex_struct( [e] | e in E )
    * R[ loc: Struct{F}@ ] -> convert_to_exclusive( L[loc] )
    */
  override def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.rValue(ctx)){
    case (loc@ in.FieldRef(recv :: ctx.Struct(fs), field)) :: _ / Exclusive =>
      for {
        vBase <- ctx.expr.translate(recv)(ctx)
        idx = fs.indexOf(field)
      } yield ex.get(vBase, idx, cptParam(fs)(ctx))(loc)(ctx)

    case (upd: in.StructUpd) :: ctx.Struct(fs) =>
      for {
        vBase <- ctx.expr.translate(upd.base)(ctx)
        idx = fs.indexOf(upd.field)
        vVal <- ctx.expr.translate(upd.newVal)(ctx)
      } yield ex.update(vBase, idx, vVal, cptParam(fs)(ctx))(upd)(ctx)

    case (e: in.DfltVal) :: ctx.Struct(fs) =>
      val fieldDefaults = fs.map(f => in.DfltVal(f.typ)(e.info))
      sequence(fieldDefaults.map(ctx.expr.translate(_)(ctx))).map(ex.create(_, cptParam(fs)(ctx))(e)(ctx))

    case (lit: in.StructLit) :: ctx.Struct(fs) =>
      val fieldExprs = lit.args.map(arg => ctx.expr.translate(arg)(ctx))
      sequence(fieldExprs).map(ex.create(_, cptParam(fs)(ctx))(lit)(ctx))

    case (loc: in.Location) :: ctx.Struct(_) / Shared =>
      sh.convertToExclusive(loc)(ctx, ex)
  }

  /**
    * Encodes expressions as l-values, i.e. values that do occupy some identifiable location in memory.
    * This includes literals and default values.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at:
    * (1) shared variables of type T
    * (2) shared operations on T
    *
    * L[ v: Struct{F}@ ] -> Var[v]
    * L[ (e: Struct{F}).f ] -> sh_struct_get(L[e], f, F)
    */
  override def lValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = {
    case (v: in.BodyVar) :: ctx.Struct(_) / Shared =>
      unit(variable(ctx)(v).localVar)

    case (loc@ in.FieldRef(recv :: ctx.Struct(fs), field)) :: _ / Shared =>
      for {
        vBase <- ctx.typeEncoding.lValue(ctx)(recv.asInstanceOf[in.Location])
        idx = fs.indexOf(field)
      } yield sh.get(vBase, idx, cptParam(fs)(ctx))(loc)(ctx)
  }

  /**
    * Encodes the reference of an expression.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at shared operations on type T.
    * Super implements shared variables with [[variable]].
    *
    * Ref[ (e: Struct{F}@).f ] -> sh_struct_get(Ref[e], f, F)
    */
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = default(super.reference(ctx)){
    case (loc@ in.FieldRef(recv :: ctx.Struct(fs), field)) :: _ / Shared =>
      for {
        vBase <- ctx.typeEncoding.reference(ctx)(recv.asInstanceOf[in.Location])
        idx = fs.indexOf(field)
      } yield sh.get(vBase, idx, cptParam(fs)(ctx))(loc)(ctx)
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    */
  override def addressFootprint(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = {
    case loc :: ctx.Struct(_) / Shared => sh.addressFootprint(loc)(ctx)
  }

//  /** // TODO: remove if everything works
//    * Encodes statements.
//    * This includes make-statements.
//    *
//    * Super implements:
//    * [v: *T = make(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
//    *
//    * [v = make(struct(E))] -> Initialize[*v]; FOREACH (f, e) in E: [*v.f = e]
//    */
//  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
//    case make@ in.Make(target, in.CompositeObject.Struct(lit :: ctx.Struct(fs))) =>
//      val (pos, info, errT) = make.vprMeta
//      val deref = in.Deref(target)(make.info)
//      val lhsFAs = fieldAccesses(deref, fs).map(in.Assignee.Field)
//      for {
//        init <- ctx.typeEncoding.initialization(ctx)(deref)
//        assignments <- sequence((lhsFAs zip lit.args).map{ case (lhs, rhs) => ctx.typeEncoding.assignment(ctx)(lhs, rhs, make) })
//      } yield VU.seqn(init +: assignments)(pos, info, errT)
//  }

  /** Returns 'base'.f for every f in 'fields'. */
  private def fieldAccesses(base: in.Expr, fields: Vector[in.Field]): Vector[in.FieldRef] = {
    fields.map(f => in.FieldRef(base, f)(base.info))
  }
}
