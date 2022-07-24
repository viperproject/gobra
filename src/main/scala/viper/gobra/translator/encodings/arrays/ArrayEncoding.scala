// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.arrays

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.arrays.ArrayEncoding.ComponentParameter
import viper.gobra.translator.encodings.combinators.TypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.library.embeddings.EmbeddingParameter
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

import scala.annotation.unused

private[arrays] object ArrayEncoding {
  /** Parameter of array components. */
  case class ComponentParameter(len: BigInt, elemT: in.Type) extends EmbeddingParameter {
    override val serialize: String = s"${len}_${Names.serializeType(elemT)}"
    def arrayT(addressability: Addressability): in.ArrayT = in.ArrayT(len, elemT, addressability)
  }

  /** Computes the component parameter. */
  def cptParam(len: BigInt, elemT: in.Type)(@unused ctx: Context): ComponentParameter = {
    ComponentParameter(len, elemT)
  }
}

class ArrayEncoding extends TypeEncoding with SharedArrayEmbedding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.{ViperUtil => VU}
  import ArrayEncoding.cptParam

  private val ex: ExclusiveArrayComponent = new ExclusiveArrayComponentImpl
  private val sh: SharedArrayComponent = new SharedArrayComponentImpl

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    ex.finalize(addMemberFn)
    sh.finalize(addMemberFn)
    conversionFunc.finalize(addMemberFn)
    exDfltFunc.finalize(addMemberFn)
  }

  /** Boxing in the context of the shared-array domain. */
  override def box(arg : vpr.Exp, typ : in.ArrayT)(src: in.Node)(ctx: Context): vpr.Exp =
    sh.box(arg, cptParam(typ.length, typ.elems)(ctx))(src)(ctx)

  /** Unboxing in the context of the shared-array domain. */
  override def unbox(arg : vpr.Exp, typ : in.ArrayT)(src: in.Node)(ctx: Context): vpr.Exp =
    sh.unbox(arg, cptParam(typ.length, typ.elems)(ctx))(src)(ctx)

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Array(len, t) / m =>
      val vti = cptParam(len, t)(ctx)
      m match {
        case Exclusive => ex.typ(vti)(ctx)
        case Shared    => sh.typ(vti)(ctx)
      }
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
    * [(e: [n]T°)[idx] = rhs] -> [ e = e[idx := rhs] ]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee(in.IndexedExp(base :: ctx.Array(_, _), idx, _) :: _ / Exclusive), rhs, src) =>
      ctx.assignment(in.Assignee(base), in.ArrayUpdate(base, idx, rhs)(src.info))(src)
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
    * [lhs: [n]T == rhs: [n]T] -> let x = lhs, y = rhs in Forall idx :: {trigger} 0 <= idx < n ==> [ x[idx] == y[idx] ]
    *     where trigger = array_get(x, idx, n), array_get(y, idx, n)
    */
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = default(super.equal(ctx)){
    case (lhs :: ctx.Array(len, _), rhs :: ctx.Array(len2, _), src) if len == len2 =>
      for {
        (x, xTrigger) <- copyArray(lhs)(ctx)
        (y, yTrigger) <- copyArray(rhs)(ctx)
        typLhs = underlyingType(lhs.typ)(ctx)
        typRhs = underlyingType(rhs.typ)(ctx)
        body = (idx: in.BoundVar) => ctx.equal(in.IndexedExp(x, idx, typLhs)(src.info), in.IndexedExp(y, idx, typRhs)(src.info))(src)
        res <- boundedQuant(len, idx => xTrigger(idx) ++ yTrigger(idx), body)(src)(ctx)
      } yield res
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    * (2) shared expression of type T
    * Super implements exclusive variables and constants with [[variable]] and [[globalVar]], respectively.
    *
    * R[ (e: [n]T)[idx] ] -> ex_array_get([e], [idx], n)
    * R[ (base: [n]T)[idx = e] ] -> ex_array_upd([base], [idx], [e])
    * R[ dflt([n]T) ] -> arrayDefault()
    * R[ arrayLit(E) ] -> create_ex_array( [e] | e in E )
    * R[ len(e: [n]T@°) ] -> ex_array_length([e])
    * R[ len(e: [n]T@) ] -> sh_array_length(Ref[e])
    * R[ seq(e: [n]T) ] -> ex_array_toSeq([e])
    * R[ set(e: [n]T) ] -> seqToSet(ex_array_toSeq([e]))
    * R[ mset(e: [n]T) ] -> seqToMultiset(ex_array_toSeq([e]))
    * R[ x in (e: [n]T) ] -> [x] in ex_array_toSeq([e])
    * R[ x # (e: [n]T) ] -> [x] # ex_array_toSeq([e])
    * R[ loc: ([n]T)@ ] -> arrayConversion(Ref[loc]) // assert [&loc != nil] if [n]T has size zero
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){
    case (loc@ in.IndexedExp(base :: ctx.Array(len, t), idx, _)) :: _ / Exclusive =>
      for {
        vBase <- ctx.expression(base)
        vIdx <- ctx.expression(idx)
      } yield ex.get(vBase, vIdx, cptParam(len, t)(ctx))(loc)(ctx)

    case (upd: in.ArrayUpdate) :: ctx.Array(len, t) =>
      for {
        vBase <- ctx.expression(upd.base)
        vIdx <- ctx.expression(upd.left)
        vVal <- ctx.expression(upd.right)
      } yield ex.update(vBase, vIdx, vVal, cptParam(len, t)(ctx))(upd)(ctx)

    case (e: in.DfltVal) :: ctx.Array(len, t) / Exclusive =>
      val (pos, info, errT) = e.vprMeta
      unit(exDfltFunc(Vector.empty, cptParam(len, t)(ctx))(pos, info, errT)(ctx))

    case (e: in.DfltVal) :: ctx.Array(len, t) / Shared =>
      unit(sh.nil(cptParam(len, t)(ctx))(e)(ctx))

    case (lit: in.ArrayLit) :: ctx.Array(len, t) => for {
      vLit <- ctx.expression(in.SequenceLit(len, t, lit.elems)(lit.info))
    } yield ex.fromSeq(vLit, cptParam(len, t)(ctx))(lit)(ctx)

    case n@ in.Length(e :: ctx.Array(len, t) / m) =>
      m match {
        case Exclusive => ctx.expression(e).map(ex.length(_, cptParam(len, t)(ctx))(n)(ctx))
        case Shared => ctx.reference(e.asInstanceOf[in.Location]).map(sh.length(_, cptParam(len, t)(ctx))(n)(ctx))
      }

    case n@ in.SequenceConversion(e :: ctx.Array(len, t) / Exclusive) =>
      ctx.expression(e).map(vE => ex.toSeq(vE, cptParam(len, t)(ctx))(n)(ctx))

    case n@ in.SetConversion(e :: ctx.Array(len, t)) =>
      val (pos, info, errT) = n.vprMeta
      ctx.expression(e).map(vE => ctx.seqToSet.create(ex.toSeq(vE, cptParam(len, t)(ctx))(n)(ctx))(pos, info, errT))

    case n@ in.MultisetConversion(e :: ctx.Array(len, t)) =>
      val (pos, info, errT) = n.vprMeta
      ctx.expression(e).map(vE => ctx.seqToMultiset.create(ex.toSeq(vE, cptParam(len, t)(ctx))(n)(ctx))(pos, info, errT))

    case n@ in.Contains(x, e :: ctx.Array(len, t)) =>
      val (pos, info, errT) = n.vprMeta
      for {
        vX <- ctx.expression(x)
        vE <- ctx.expression(e)
      } yield vpr.SeqContains(vX, ex.toSeq(vE, cptParam(len, t)(ctx))(n)(ctx))(pos, info, errT)

    case n@ in.Multiplicity(x, e :: ctx.Array(len, t)) =>
      val (pos, info, errT) = n.vprMeta
      for {
        vX <- ctx.expression(x)
        vE <- ctx.expression(e)
      } yield ctx.seqMultiplicity.create(vX, ex.toSeq(vE, cptParam(len, t)(ctx))(n)(ctx))(pos, info, errT)

    case (loc: in.Location) :: ctx.NoZeroSize(ctx.Array(len, t)) / Shared =>
      val (pos, info, errT) = loc.vprMeta
      for {
        arg <- ctx.reference(loc)
      } yield conversionFunc(Vector(arg), cptParam(len, t)(ctx))(pos, info, errT)(ctx)
  }

  /**
    * Encodes the reference of an expression.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at shared operations on type T.
    * Super implements shared variables with [[variable]].
    *
    * Ref[ (e: [n]T)[idx] ] -> sh_array_get(Ref[e], [idx], n)
    */
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = default(super.reference(ctx)){
    case (loc@ in.IndexedExp(base :: ctx.Array(len, t), idx, _)) :: _ / Shared =>
      for {
        vBase <- ctx.reference(base.asInstanceOf[in.Location])
        vIdx <- ctx.expression(idx)
      } yield sh.get(vBase, vIdx, cptParam(len, t)(ctx))(loc)(ctx)
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    *
    * The default implements:
    * Footprint[loc: T@ if sizeOf(T) == 0] -> [&loc != nil: *T°]
    *
    * Footprint[loc: [n]T] -> forall idx :: {trigger} 0 <= idx < n ==> Footprint[ loc[idx] ]
    *   where trigger = sh_array_get(Ref[loc], idx, n)
    *
    * We do not use let because (at the moment) Viper does not accept quantified permissions with let expressions.
    */
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = super.addressFootprint(ctx).orElse {
    case (loc :: ctx.Array(len, t) / Shared, perm) =>
      val (pos, info, errT) = loc.vprMeta
      val typ = underlyingType(loc.typ)(ctx)
      val trigger = (idx: vpr.LocalVar) =>
        Seq(vpr.Trigger(Seq(sh.get(ctx.reference(loc).res, idx, cptParam(len, t)(ctx))(loc)(ctx)))(pos, info, errT))
      val body = (idx: in.BoundVar) => ctx.footprint(in.IndexedExp(loc, idx, typ)(loc.info), perm)
      boundedQuant(len, trigger, body)(loc)(ctx).map(forall =>
        // to eliminate nested quantified permissions, which are not supported by the silver ast.
        VU.bigAnd(viper.silver.ast.utility.QuantifiedPermissions.desugarSourceQuantifiedPermissionSyntax(forall))(pos, info, errT)
      )
  }

  /**
    * Encodes whether a value is comparable or not.
    *
    * isComp[ e: [n]T ] -> forall idx :: { isComp[ e[idx] ] } 0 <= idx < n ==> isComp[ e[idx] ]
    */
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case exp :: ctx.Array(len, _) =>
      super.isComparable(ctx)(exp).map{ _ =>
        val (pos, info, errT) = exp.vprMeta
        // if this is executed, then type parameter must have dynamic comparability
        val idx = in.BoundVar("idx", in.IntT(Exclusive))(exp.info)
        val vIdxDecl = ctx.variable(idx)
        val baseTyp = underlyingType(exp.typ)(ctx)
        for {
          rhs <- pure(ctx.isComparable(in.IndexedExp(exp, idx, baseTyp)(exp.info))
            .getOrElse(Violation.violation("An incomparable array entails an incomparable element type.")))(ctx)
          res = vpr.Forall(
            variables = Seq(vIdxDecl),
            triggers = Seq(vpr.Trigger(Seq(rhs))(pos, info, errT)),
            exp = vpr.Implies(boundaryCondition(vIdxDecl.localVar, len)(exp), rhs)(pos, info, errT)
          )(pos, info, errT)
        } yield res: vpr.Exp
      }
  }

  /**
    * Generates:
    * function arrayConversion(x: [([n]T)@]): ([n]T)°
    *   requires acc(Footprint[x], _)
    *   ensures  [x == result]
    * */
  private val conversionFunc: FunctionGenerator[ComponentParameter] = new FunctionGenerator[ComponentParameter]{
    def genFunction(t: ComponentParameter)(ctx: Context): vpr.Function = {
      val info = Source.Parser.Internal

      val argType = t.arrayT(Shared)
      // variable name does not matter because it is the only argument
      val x = in.LocalVar("x", argType)(info)

      val resultType = argType.withAddressability(Exclusive)
      val vResultType = typ(ctx)(resultType)
      // variable name does not matter because it is turned into a vpr.Result
      val resultVar = in.LocalVar("res", resultType)(info)
      val post = pure(equal(ctx)(x, resultVar, x))(ctx).res
        // replace resultVar with vpr.Result
        .transform{ case v: vpr.LocalVar if v.name == resultVar.id => vpr.Result(vResultType)() }

      vpr.Function(
        name = s"${Names.arrayConversionFunc}_${t.serialize}",
        formalArgs = Vector(variable(ctx)(x)),
        typ = vResultType,
        pres = Vector(pure(addressFootprint(ctx)(x, in.WildcardPerm(info)))(ctx).res),
        posts = Vector(post),
        body = None
      )()
    }
  }


  /**
    * Generates:
    * function arrayDefault(): ([n]T)°
    *   ensures len(result) == n
    *   ensures Forall idx :: {result[idx]} 0 <= idx < n ==> [result[idx] == dflt(T)]
    * */
  private val exDfltFunc: FunctionGenerator[ComponentParameter] = new FunctionGenerator[ComponentParameter]{
    def genFunction(t: ComponentParameter)(ctx: Context): vpr.Function = {
      val resType = t.arrayT(Exclusive)
      val vResType = typ(ctx)(resType)
      val src = in.DfltVal(resType)(Source.Parser.Internal)
      // variable name does not matter because it is turned into a vpr.Result
      val resDummy = in.LocalVar("res", resType)(src.info)
      // variable name does not matter because it is the only variable occurring in the current scope
      val idx = in.BoundVar("idx", in.IntT(Exclusive))(src.info)
      val vIdx = ctx.variable(idx)
      val resAccess = in.IndexedExp(resDummy, idx, resType)(src.info)
      val lenEq = pure(ctx.equal(in.Length(resDummy)(src.info), in.IntLit(resType.length)(src.info))(src))(ctx).res
        .transform{ case x: vpr.LocalVar if x.name == resDummy.id => vpr.Result(vResType)() }
      val idxEq = pure(ctx.equal(resAccess, in.DfltVal(resType.elems)(src.info))(src))(ctx).res
        .transform{ case x: vpr.LocalVar if x.name == resDummy.id => vpr.Result(vResType)() }
      val trigger = ex.get(vpr.Result(vResType)(), vIdx.localVar, t)(src)(ctx)
      val arrayEq = vpr.Forall(
        Seq(vIdx),
        Seq(vpr.Trigger(Seq(trigger))()),
        vpr.Implies(boundaryCondition(vIdx.localVar, t.len)(src), idxEq)()
      )()

      vpr.Function(
        name = s"${Names.arrayDefaultFunc}_${t.serialize}",
        formalArgs = Seq.empty,
        typ = vResType,
        pres = Seq.empty,
        posts = Vector(lenEq, arrayEq),
        body = None
      )()
    }
  }

  /** Returns: 0 <= 'base' && 'base' < 'length'. */
  private def boundaryCondition(base: vpr.Exp, length: BigInt)(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    vpr.And(
      vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), base)(pos, info, errT),
      vpr.LtCmp(base, vpr.IntLit(length)(pos, info, errT))(pos, info, errT)
    )(pos, info, errT)
  }

  /** Returns: Forall idx :: {'trigger'(idx)} 0 <= idx && idx < 'length' => ['body'(idx)] */
  private def boundedQuant(length: BigInt, trigger: vpr.LocalVar => Seq[vpr.Trigger], body: in.BoundVar => CodeWriter[vpr.Exp])
                                  (src: in.Node)(ctx: Context)
                                  : CodeWriter[vpr.Forall] = {

    val (pos, info, errT) = src.vprMeta

    val idx = in.BoundVar(ctx.freshNames.next(), in.IntT(Exclusive))(src.info)
    val vIdx = ctx.variable(idx)

    for {
      vBody <- pure(body(idx))(ctx)
      forall = vpr.Forall(
        variables = Vector(vIdx),
        triggers = trigger(vIdx.localVar),
        exp = vpr.Implies(boundaryCondition(vIdx.localVar, length)(src), vBody)(pos, info, errT)
      )(pos, info, errT)
    } yield forall
  }

  /**
    * For e: ([n]T)° returns: x = [e]; (x, idx => ex_array_get(x, idx, n)
    * For e: ([n]T)@ returns: x = Ref[e]; (x, idx => sh_array_get(x, idx, n)
    * */
  private def copyArray(e: in.Expr)(ctx: Context): CodeWriter[(in.LocalVar, vpr.LocalVar => Seq[vpr.Trigger])] = {
    e match {
      case _ :: ctx.Array(len, t) / Exclusive =>
        val (pos, info, errT) = e.vprMeta
        for {
          vS <- ctx.expression(e)
          x = in.LocalVar(ctx.freshNames.next(), e.typ)(e.info)
          vX = variable(ctx)(x)
          _ <- local(vX)
          _ <- bind(vX.localVar, vS)
          trigger = (vIdx: vpr.LocalVar) => Seq(vpr.Trigger(Seq(ex.get(vX.localVar, vIdx, cptParam(len, t)(ctx))(e)(ctx)))(pos, info, errT))
        } yield (x, trigger)

      case (loc: in.Location) :: ctx.Array(len, t) / Shared =>
        val (pos, info, errT) = e.vprMeta
        for {
          vS <- ctx.reference(loc)
          x = in.LocalVar(ctx.freshNames.next(), e.typ)(e.info)
          vX = variable(ctx)(x)
          _ <- local(vX)
          _ <- bind(vX.localVar, vS)
          trigger = (vIdx: vpr.LocalVar) => Seq(vpr.Trigger(Seq(sh.get(vX.localVar, vIdx, cptParam(len, t)(ctx))(loc)(ctx)))(pos, info, errT))
        } yield (x, trigger)

      case t => Violation.violation(s"Expected array, but got $t.")
    }
  }
}
