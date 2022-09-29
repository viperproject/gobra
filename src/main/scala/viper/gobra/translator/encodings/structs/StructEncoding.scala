// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.combinators.TypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

private[structs] object StructEncoding {
  /** Parameter of struct components. */
  type ComponentParameter = Vector[(vpr.Type, Boolean)]

  /** Computes the component parameter. */
  def cptParam(fields: Vector[in.Field])(ctx: Context): ComponentParameter = {
    fields.map(f => (ctx.typ(f.typ), f.ghost))
  }
}

class StructEncoding extends TypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.{ViperUtil => VU}
  import StructEncoding.{ComponentParameter, cptParam}
  import viper.silver.plugin.standard.termination

  private val ex: ExclusiveStructComponent = new ExclusiveStructComponent{ // For now, we use a simple tuple domain.
    override def typ(vti: ComponentParameter)(ctx: Context): vpr.Type = ctx.tuple.typ(vti.map(_._1))
    override def get(base: vpr.Exp, idx: Int, vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = withSrc(ctx.tuple.get(base, idx, vti.size), src)
    override def create(args: Vector[vpr.Exp], vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = withSrc(ctx.tuple.create(args), src)
  }

  private val sh: SharedStructComponent = new SharedStructComponentImpl

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    ex.finalize(addMemberFn)
    sh.finalize(addMemberFn)
    shDfltFunc.finalize(addMemberFn)
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
      seqns(fieldAccesses(l, fs).map(x => ctx.initialization(x)))
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
    * [lhs: Struct{F}@ = rhs if size != 0] -> FOREACH f in F: [lhs.f = rhs.f]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee((fa: in.FieldRef) :: _ / Exclusive), rhs, src) =>
      ctx.assignment(in.Assignee(fa.recv), in.StructUpdate(fa.recv, fa.field, rhs)(src.info))(src)

    case (in.Assignee(lhs :: ctx.NoZeroSize(ctx.Struct(lhsFs)) / Shared), rhs :: ctx.Struct(rhsFs), src) =>
      val lhsFAs = fieldAccesses(lhs, lhsFs).map(in.Assignee.Field)
      val rhsFAs = fieldAccesses(rhs, rhsFs)
      seqns((lhsFAs zip rhsFAs).map{ case (lhsFA, rhsFA) => ctx.assignment(lhsFA, rhsFA)(src) })
  }

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    * An encoding for type T should be defined at left-hand sides of type T and exclusive *T.
    * (Except the encoding of pointer types, which is not defined at exclusive *T to avoid a conflict).
    *
    * The default implements:
    * [lhs: T == rhs: T] -> [lhs] == [rhs]
    * [lhs: *T° == rhs: *T] -> [lhs] == [rhs]
    *
    * [(lhs: Struct{F}) == rhs: Struct{_}] -> AND f in F: [lhs.f == rhs.f]
    */
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = default(super.equal(ctx)){
    case (lhs :: ctx.Struct(lhsFs), rhs :: ctx.Struct(rhsFs), src) =>
      val lhsFAccs = fieldAccesses(lhs, lhsFs)
      val rhsFAccs = fieldAccesses(rhs, rhsFs)
      val equalFields = sequence((lhsFAccs zip rhsFAccs).map{ case (lhsFA, rhsFA) => ctx.equal(lhsFA, rhsFA)(src) })
      val (pos, info, errT) = src.vprMeta
      equalFields.map(VU.bigAnd(_)(pos, info, errT))
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
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
    * R[ loc: Struct{F}@ ] -> convert_to_exclusive( Ref[loc] ) // assert [&loc != nil] if Struct{F} has size zero
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){
    case (loc@ in.FieldRef(recv :: ctx.Struct(fs), field)) :: _ / Exclusive =>
      for {
        vBase <- ctx.expression(recv)
        idx = indexOfField(fs, field)
      } yield ex.get(vBase, idx, cptParam(fs)(ctx))(loc)(ctx)

    case (upd: in.StructUpdate) :: ctx.Struct(fs) =>
      for {
        vBase <- ctx.expression(upd.base)
        idx = indexOfField(fs, upd.field)
        vVal <- ctx.expression(upd.newVal)
      } yield ex.update(vBase, idx, vVal, cptParam(fs)(ctx))(upd)(ctx)

    case (e: in.DfltVal) :: ctx.Struct(fs) / Exclusive =>
      val fieldDefaults = fs.map(f => in.DfltVal(f.typ)(e.info))
      sequence(fieldDefaults.map(ctx.expression)).map(ex.create(_, cptParam(fs)(ctx))(e)(ctx))

    case (e: in.DfltVal) :: ctx.Struct(fs) / Shared =>
      val (pos, info, errT) = e.vprMeta
      unit(shDfltFunc(Vector.empty, fs)(pos, info, errT)(ctx))

    case (lit: in.StructLit) :: ctx.Struct(fs) =>
      val fieldExprs = lit.args.map(arg => ctx.expression(arg))
      sequence(fieldExprs).map(ex.create(_, cptParam(fs)(ctx))(lit)(ctx))

    case (loc: in.Location) :: ctx.NoZeroSize(ctx.Struct(_)) / Shared =>
      sh.convertToExclusive(loc)(ctx, ex)
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
        vBase <- ctx.reference(recv.asInstanceOf[in.Location])
        idx = indexOfField(fs, field)
      } yield sh.get(vBase, idx, cptParam(fs)(ctx))(loc)(ctx)
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    *
    * The default implements:
    * Footprint[loc: T@ if sizeOf(T) == 0] -> [&loc != nil: *T°]
    *
    * Footprint[loc: Struct{F}@] -> AND f in F: Footprint[loc.f]
    */
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = super.addressFootprint(ctx).orElse {
    case (loc :: ctx.Struct(_) / Shared, perm) => sh.addressFootprint(loc, perm)(ctx)
  }

  /**
    * Encodes whether a value is comparable or not.
    *
    * isComp[ e: Struct{F} ] -> AND f in F: isComp[e.f]
    */
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case exp :: ctx.Struct(fs) =>
      super.isComparable(ctx)(exp).map{ _ =>
        // if executed, then for all fields f, isComb[exp.f] != Left(false)
        val (pos, info, errT) = exp.vprMeta
        // fields that are not ghost and with dynamic comparability
        val fsAccs = fieldAccesses(exp, fs.filter(f => !f.ghost))
        val fsComp = fsAccs map ctx.isComparable
        // Left(true) can be removed.
        for {
          args <- sequence(fsComp collect { case Right(e) => e })
        } yield VU.bigAnd(args)(pos, info, errT)
      }
  }

  /** Returns 'base'.f for every f in 'fields'. */
  private def fieldAccesses(base: in.Expr, fields: Vector[in.Field]): Vector[in.FieldRef] = {
    fields.map(f => in.FieldRef(base, f)(base.info))
  }

  private def indexOfField(fs: Vector[in.Field], f: in.Field): Int = {
    val idx = fs.indexOf(f)
    Violation.violation(idx >= 0, s"$idx, ${f.typ.addressability}, ${fs.map(_.typ.addressability)} - Did not find field $f in $fs")
    idx
  }

  /**
    * Generates:
    * function shStructDefault(): [Struct{F}@]
    *   ensures AND (f: T) in F. [&result.f == dflt(T)]
    *   decreases _
    */
  private val shDfltFunc: FunctionGenerator[Vector[in.Field]] = new FunctionGenerator[Vector[in.Field]] {
    override def genFunction(fs: Vector[in.Field])(ctx: Context): vpr.Function = {
      val resType = in.StructT(fs, Shared)
      val vResType = typ(ctx)(resType)
      val src = in.DfltVal(resType)(Source.Parser.Internal)
      // variable name does not matter because it is turned into a vpr.Result
      val resDummy = in.LocalVar("res", resType)(src.info)
      val resFAccs = fs.map(f => in.UncheckedRef(in.FieldRef(resDummy, f)(src.info))(src.info))
      val fieldEq = resFAccs map (f => ctx.equal(f, in.DfltVal(f.typ)(src.info))(src))
      // termination measure
      val pre = synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
      val post = pure(sequence(fieldEq).map(VU.bigAnd(_)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)))(ctx).res
          .transform{ case x: vpr.LocalVar if x.name == resDummy.id => vpr.Result(vResType)() }

      vpr.Function(
        name = s"${Names.sharedStructDfltFunc}_${Names.serializeFields(fs)}",
        formalArgs = Seq.empty,
        typ = vResType,
        pres = Seq(pre),
        posts = Seq(post),
        body = None
      )()
    }
  }
}
