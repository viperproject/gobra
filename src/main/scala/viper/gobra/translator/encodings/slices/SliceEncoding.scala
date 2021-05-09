// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.slices

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{ArrayMakePreconditionError, Source}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.encodings.arrays.SharedArrayEmbedding
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.verifier.{errors => err}
import viper.silver.{ast => vpr}

class SliceEncoding(arrayEmb : SharedArrayEmbedding) extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.{ViperUtil => vu}

  override def finalize(col : Collector) : Unit = {
    constructGenerator.finalize(col)
    fullSliceFromArrayGenerator.finalize(col)
    fullSliceFromSliceGenerator.finalize(col)
    sliceFromArrayGenerator.finalize(col)
    sliceFromSliceGenerator.finalize(col)
    nilSliceGenerator.finalize(col)
  }

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Slice(t) / m => m match {
      case Exclusive => ctx.slice.typ(ctx.typeEncoding.typ(ctx)(t))
      case Shared => vpr.Ref
    }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * R[ dflt([]T°) ] -> nilSlice()
    * R[ dflt([]T@) ] -> null
    * R[ nil: []T° ] -> nilSlice()
    * R[ len(e: []T) ] -> slen([e])
    * R[ cap(e: []T) ] -> scap([e])
    * R[ (e: [n]T@)[e1:e2] ] -> sliceFromArray([e], [e1], [e2])
    * R[ (e: [n]T@)[e1:e2:e3] ] -> fullSliceFromArray([e], [e1], [e2], [e3])
    * R[ (e: []T@)[e1:e2] ] -> sliceFromSlice([e], [e1], [e2])
    * R[ (e: []T@)[e1:e2:e3] ] -> fullSliceFromSlice([e], [e1], [e2], [e3])
    * R[ sliceLit(E) ] -> R[ arrayLit(E)[0:|E|] ]
    */
  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Slice(t) / m => m match {
        case Exclusive => unit(withSrc(nilSlice(t)(ctx), exp))
        case Shared => unit(withSrc(vpr.NullLit(), exp))
      }

      case (exp : in.NilLit) :: ctx.Slice(t) / Exclusive =>
        unit(withSrc(nilSlice(t)(ctx), exp))

      case in.Length(exp :: ctx.Slice(_)) => for {
        expT <- goE(exp)
      } yield withSrc(ctx.slice.len(expT), exp)

      case in.Capacity(exp :: ctx.Slice(_)) => for {
        expT <- goE(exp)
      } yield withSrc(ctx.slice.cap(expT), exp)

      case exp @ in.Slice((base : in.Location) :: ctx.Array(_, _) / Shared, low, high, max) => for {
        baseT <- ctx.typeEncoding.reference(ctx)(base)
        unboxedBaseT = arrayEmb.unbox(baseT, base.typ.asInstanceOf[in.ArrayT])(base)(ctx)
        lowT <- goE(low)
        highT <- goE(high)
        maxOptT <- option(max map goE)
      } yield maxOptT match {
        case None => withSrc(sliceFromArray(vpr.Ref, unboxedBaseT, lowT, highT)(ctx), exp)
        case Some(maxT) => withSrc(fullSliceFromArray(vpr.Ref, unboxedBaseT, lowT, highT, maxT)(ctx), exp)
      }

      case exp @ in.Slice((base : in.Expr) :: ctx.Slice(_), low, high, max) => for {
        baseT <- goE(base)
        lowT <- goE(low)
        highT <- goE(high)
        maxOptT <- option(max map goE)
      } yield maxOptT match {
        case None => withSrc(sliceFromSlice(vpr.Ref, baseT, lowT, highT)(ctx), exp)
        case Some(maxT) => withSrc(fullSliceFromSlice(vpr.Ref, baseT, lowT, highT, maxT)(ctx), exp)
      }

      case (lit : in.SliceLit) :: ctx.Slice(_) => {
        val litA = lit.asArrayLit
        val tmp = in.LocalVar(Names.freshName, litA.typ.withAddressability(Addressability.pointerBase))(lit.info)
        val tmpT = ctx.typeEncoding.variable(ctx)(tmp)
        for {
          initT <- ctx.typeEncoding.initialization(ctx)(tmp)
          assignT <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(tmp), litA, lit)
          sliceT <- ctx.expr.translate(in.Slice(tmp, in.IntLit(0)(lit.info), in.IntLit(litA.length)(lit.info), None)(lit.info))(ctx)
          _ <- local(tmpT)
          _ <- write(initT)
          _ <- write(assignT)
        } yield sliceT
      }
    }
  }

  /**
    * Encodes the comparison of two expressions.
    *
    * [lhs: []T == rhs: []T] ->
    *   len([lhs]) == len([rhs]) &&
    *   cap([lhs]) == cap([rhs]) &&
    *   offset([lhs]) == offset([rhs]) &&
    *   array([lhs]) == array([rhs])
    *
    * [lhs: *[]T° == rhs] -> [lhs] == [rhs]
    */
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: ctx.Slice(t), rhs, src) => for {
      lhsT <- ctx.expr.translate(lhs)(ctx)
      rhsT <- ctx.expr.translate(rhs)(ctx)
      typT = ctx.typeEncoding.typ(ctx)(t)
      offsetT = vpr.EqCmp(withSrc(ctx.slice.offset(lhsT), src), withSrc(ctx.slice.offset(rhsT), src))()
      lenT = vpr.EqCmp(withSrc(ctx.slice.len(lhsT), src), withSrc(ctx.slice.len(rhsT), src))()
      capT = vpr.EqCmp(withSrc(ctx.slice.cap(lhsT), src), withSrc(ctx.slice.cap(rhsT), src))()
      arrayT = vpr.EqCmp(withSrc(ctx.slice.array(lhsT, typT), src), withSrc(ctx.slice.array(rhsT, typT), src))()
    } yield Seq(offsetT, lenT, capT, arrayT).reduce[vpr.Exp] {
      case (l, r) => withSrc(vpr.And(l, r), src)
    }

    case (lhs :: ctx.*(ctx.Slice(_)) / Exclusive, rhs, src) => for {
      vLhs <- ctx.expr.translate(lhs)(ctx)
      vRhs <- ctx.expr.translate(rhs)(ctx)
    } yield withSrc(vpr.EqCmp(vLhs, vRhs), src)
  }

  /**
    * Encodes the allocation of a new slice
    *
    *  [r := make([]T, len, cap)] ->
    *    asserts 0 <= [len] && 0 <= [cap] && [len] <= [cap]
    *    var a [ []T ]
    *    inhales cap(a) == [cap]
    *    inhales len(a) == [len]
    *    inhales forall i: int :: {loc(a, i)} 0 <= i && i < [cap] ==> Footprint[ a[i] ]
    *    inhales forall i: int :: {loc(a, i)} 0 <= i && i < [len] ==> [ a[i] == dfltVal(T) ]
    *    r := a
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)) {
      case makeStmt@in.MakeSlice(target, in.SliceT(typeParam, _), lenArg, optCapArg) =>
        val (pos, info, errT) = makeStmt.vprMeta
        val slice = in.LocalVar(Names.freshName, in.SliceT(typeParam.withAddressability(Shared), Addressability.Exclusive))(makeStmt.info)
        val vprSlice = ctx.typeEncoding.variable(ctx)(slice)
        val typ = ctx.typeEncoding.typ(ctx)(typeParam.withAddressability(Addressability.Shared))
        seqn(
          for {
            // var a [ []T ]
            _ <- local(vprSlice)

            capArg = optCapArg.getOrElse(lenArg)
            vprLength <- ctx.expr.translate(lenArg)(ctx)
            vprCapacity <- ctx.expr.translate(capArg)(ctx)

            // Perform additional runtime checks of conditions that must be true when make is invoked, otherwise the program panics (according to the go spec)
            // asserts 0 <= [len] && 0 <= [cap] && [len] <= [cap]
            runtimeChecks = vu.bigAnd(Vector(
              vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), vprLength)(pos, info, errT), // 0 <= len
              vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), vprCapacity)(pos, info, errT), // 0 <= cap
              vpr.LeCmp(vprLength, vprCapacity)(pos, info, errT) // len <= cap
            ))(pos, info, errT)

            exhale = vpr.Exhale(runtimeChecks)(pos, info, errT)
            _ <- write(exhale)
            _ <- errorT {
              case e@err.ExhaleFailed(Source(info), _, _) if e causedBy exhale => ArrayMakePreconditionError(info)
            }

            // inhale forall i: int :: {loc(a, i)} 0 <= i && i < [cap] ==> Footprint[ a[i] ]
            footprintAssertion <- getCellPerms(ctx)(slice, in.FullPerm(slice.info))
            _ <- write(vpr.Inhale(footprintAssertion)(pos, info, errT))

            lenExpr = in.Length(slice)(makeStmt.info)
            capExpr = in.Capacity(slice)(makeStmt.info)

            // inhale cap(a) == [cap]
            eqCap <- ctx.typeEncoding.equal(ctx)(capExpr, capArg, makeStmt)
            _ <- write(vpr.Inhale(eqCap)(pos, info, errT))

            // inhale len(a) == [len]
            eqLen <- ctx.typeEncoding.equal(ctx)(lenExpr, lenArg, makeStmt)
            _ <- write(vpr.Inhale(eqLen)(pos, info, errT))

            // inhale forall i: int :: {loc(a, i)} 0 <= i && i < [len] ==> [ a[i] == dfltVal(T) ]
            eqValueAssertion <- boundedQuant(
              length = vprLength,
              trigger = (idx: vpr.LocalVar) =>
                Seq(vpr.Trigger(Seq(ctx.slice.loc(vprSlice.localVar, idx, typ)(pos, info, errT)))(pos, info, errT)),
              body = (x: in.BoundVar) =>
                ctx.typeEncoding.equal(ctx)(in.IndexedExp(slice, x)(makeStmt.info), in.DfltVal(typeParam.withAddressability(Exclusive))(makeStmt.info), makeStmt)
            )(makeStmt)(ctx)
            _ <- write(vpr.Inhale(eqValueAssertion)(pos, info, errT))

            ass <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(target), slice, makeStmt)
          } yield ass
        )
    }
  }


  /**
    * Obtains permission to all cells of a slice
    * getCellPerms[loc: []T] -> forall idx :: {loc(a, idx) 0 <= idx < cap(l) ==> Footprint[ loc[idx] ]
    */
  def getCellPerms(ctx: Context)(expr: in.Location, perm: in.Permission): CodeWriter[vpr.Exp] = expr match {
    case loc :: ctx.Slice(t) / Exclusive =>
      val (pos, info, errT) = loc.vprMeta

      val cap = in.Capacity(loc)(loc.info)
      val vprCap = ctx.expr.translate(cap)(ctx).res
      val vprLoc = ctx.expr.translate(loc)(ctx).res
      val trigger = (idx: vpr.LocalVar) =>
        Seq(vpr.Trigger(Seq(ctx.slice.loc(vprLoc, idx, ctx.typeEncoding.typ(ctx)(t.withAddressability(Shared)))(pos, info, errT)))(pos, info, errT))
      val body = (idx: in.BoundVar) => ctx.typeEncoding.addressFootprint(ctx)(in.IndexedExp(loc, idx)(loc.info), perm)
      boundedQuant(vprCap, trigger, body)(loc)(ctx).map(forall =>
        // to eliminate nested quantified permissions, which are not supported by the silver ast.
        vu.bigAnd(viper.silver.ast.utility.QuantifiedPermissions.desugarSourceQuantifiedPermissionSyntax(forall))(pos, info, errT)
      )
    case c => Violation.violation(s"getCellPerm should only be called with exclusive slices, but got $c")
  }

  /** Returns: Forall idx :: {'trigger'(idx)} 0 <= idx && idx < 'length' => ['body'(idx)] */
  private def boundedQuant(length: vpr.Exp, trigger: vpr.LocalVar => Seq[vpr.Trigger], body: in.BoundVar => CodeWriter[vpr.Exp])
                          (src: in.Node)(ctx: Context)
  : CodeWriter[vpr.Forall] = {

    val (pos, info, errT) = src.vprMeta

    val idx = in.BoundVar(Names.freshName, in.IntT(Exclusive))(src.info)
    val vIdx = ctx.typeEncoding.variable(ctx)(idx)

    for {
      vBody <- pure(body(idx))(ctx)
      forall = vpr.Forall(
        variables = Vector(vIdx),
        triggers = trigger(vIdx.localVar),
        exp = vpr.Implies(boundaryCondition(vIdx.localVar, length)(src), vBody)(pos, info, errT)
      )(pos, info, errT)
    } yield forall
  }

  /** Returns: 0 <= 'base' && 'base' < 'length'. */
  private def boundaryCondition(base: vpr.Exp, length: vpr.Exp)(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    vpr.And(
      vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), base)(pos, info, errT),
      vpr.LtCmp(base, length)(pos, info, errT)
    )(pos, info, errT)
  }

  /**
    * Encodes the reference of an expression.
    *
    * Ref[ (e: []T)[idx] ] -> slice_get(Ref[e], [idx])
    */
  override def reference(ctx : Context) : in.Location ==> CodeWriter[vpr.Exp] = default(super.reference(ctx)) {
    case (exp @ in.IndexedExp(base :: ctx.Slice(typ), idx)) :: _ / Shared => for {
      baseT <- ctx.expr.translate(base)(ctx)
      idxT <- ctx.expr.translate(idx)(ctx)
      typT = ctx.typeEncoding.typ(ctx)(typ)
    } yield withSrc(ctx.slice.loc(baseT, idxT, typT), exp)
  }

  /** An application of the "sconstruct[`typ`](...)" Viper function. */
  private def construct(typ : vpr.Type, base : vpr.Exp, offset : vpr.Exp, len : vpr.Exp, cap : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    constructGenerator(Vector(base, offset, len, cap), typ)(pos, info, errT)(ctx)

  /** An application of the "sfullSliceFromArray[`typ`](...)" Viper function. */
  private def fullSliceFromArray(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp, k : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    fullSliceFromArrayGenerator(Vector(base, i, j, k), typ)(pos, info, errT)(ctx)

  /** An application of the "sfullSliceFromSlice[`typ`](...)" Viper function. */
  private def fullSliceFromSlice(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp, k : vpr.Exp)(ctx : Context)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.FuncApp =
    fullSliceFromSliceGenerator(Vector(base, i, j, k), typ)(pos, info, errT)(ctx)

  /** Gives the 'nil' slice of inner type `typ`. */
  private def nilSlice(typ: in.Type)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.FuncApp =
    nilSliceGenerator(Vector(), typ)(pos, info, errT)(ctx)

  /** An application of the "ssliceFromArray[`typ`](...)" Viper function. */
  private def sliceFromArray(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.FuncApp =
    sliceFromArrayGenerator(Vector(base, i, j), typ)(pos, info, errT)(ctx)

  /** An application of the "ssliceFromSlice[`typ`](...)" Viper function. */
  private def sliceFromSlice(typ : vpr.Type, base : vpr.Exp, i : vpr.Exp, j : vpr.Exp)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.FuncApp =
    sliceFromSliceGenerator(Vector(base, i, j), typ)(pos, info, errT)(ctx)


  /* ** Generators */

  private lazy val generateFunctionBodies : Boolean = false

  /**
    * Generator for the "sconstruct" Viper function supporting
    * the domain of Slices, which is the constructor for Slices:
    *
    * {{{
    * function sconstruct(a : Array[T], offset : Int, len : Int, cap : Int) : Slice[T]
    *   requires 0 <= offset
    *   requires 0 <= len
    *   requires len <= cap
    *   requires offset + cap <= alen(a)
    *   ensures sarray(result) == a
    *   ensures soffset(result) == offset
    *   ensures slen(result) == len
    *   ensures scap(result) == cap
    * }}}
    */
  private val constructGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val aDecl = vpr.LocalVarDecl("a", ctx.array.typ(typ))()
      val offsetDecl = vpr.LocalVarDecl("offset", vpr.Int)()
      val lenDecl = vpr.LocalVarDecl("len", vpr.Int)()
      val capDecl = vpr.LocalVarDecl("cap", vpr.Int)()

      // preconditions
      val pre1 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), offsetDecl.localVar))("Slice offset might be negative")
      val pre2 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), lenDecl.localVar))("Slice length might be negative")
      val pre3 = synthesized(vpr.LeCmp(lenDecl.localVar, capDecl.localVar))("Slice length might exceed capacity")
      val pre4 = synthesized(vpr.LeCmp(vpr.Add(offsetDecl.localVar, capDecl.localVar)(), ctx.array.len(aDecl.localVar)()))("Slice capacity might exceed the capacity of the underlying array")

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.array(result, typ)(), aDecl.localVar)()
      val post2 = vpr.EqCmp(ctx.slice.offset(result)(), offsetDecl.localVar)()
      val post3 = vpr.EqCmp(ctx.slice.len(result)(), lenDecl.localVar)()
      val post4 = vpr.EqCmp(ctx.slice.cap(result)(), capDecl.localVar)()

      vpr.Function(
        s"${Names.sliceConstruct}_${Names.freshName}",
        Seq(aDecl, offsetDecl, lenDecl, capDecl),
        ctx.slice.typ(typ),
        Seq(pre1, pre2, pre3, pre4),
        Seq(post1, post2, post3, post4),
        None
      )()
    }
  }

  /**
    * Generator for the "sfullSliceFromArray" Viper function that supports the
    * Viper domain of Slices. This function is applied when translating
    * an array slice expression "a[i:j]" or "a[i:j:k]" in Gobra.
    * Its Viper definition is:
    *
    * {{{
    * function sfullSliceFromArray(a : Array[T], i : Int, j : Int, k : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= k && k <= alen(a)
    *   ensures soffset(result) == i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == k - i
    *   ensures sarray(result) == a
    * {
    *   sconstruct(a, i, j - i, k - i)
    * }
    * }}}
    */
  private val fullSliceFromArrayGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val aDecl = vpr.LocalVarDecl("a", ctx.array.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()
      val kDecl = vpr.LocalVarDecl("k", vpr.Int)()

      // preconditions
      val pre1 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar))("The low bound of the slice might be negative")
      val pre2 = synthesized(vpr.LeCmp(iDecl.localVar, jDecl.localVar))("The low bound of the slice might exceed the high bound")
      val pre3 = synthesized(vpr.LeCmp(jDecl.localVar, kDecl.localVar))("The high bound of the slice might exceed the max bound")
      val pre4 = synthesized(vpr.LeCmp(kDecl.localVar, ctx.array.len(aDecl.localVar)()))("The max bound of the slice might exceed the array capacity")

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), iDecl.localVar)()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(kDecl.localVar, iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), aDecl.localVar)()

      // function body
      val body = construct(
        typ,
        aDecl.localVar,
        iDecl.localVar,
        vpr.Sub(jDecl.localVar, iDecl.localVar)(),
        vpr.Sub(kDecl.localVar, iDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.fullSliceFromArray}_${Names.freshName}",
        Seq(aDecl, iDecl, jDecl, kDecl),
        ctx.slice.typ(typ),
        Seq(pre1, pre2, pre3, pre4),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "sfullSliceFromSlice" Viper function
    * that supports the Viper domain on Slices. This function is used
    * in the translation of full slice expressions "s[i:j:k]"
    * where the base expression "s" is itself a slice.
    *
    * {{{
    * function sfullSliceFromSlice(s : Slice[T], i : Int, j : Int, k : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= k && k <= scap(s)
    *   ensures soffset(result) == soffset(s) + i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == k - i
    *   ensures sarray(result) == sarray(s)
    * {
    *   sfullSliceFromArray(sarray(s), soffset(s) + i, soffset(s) + j, soffset(s) + k)
    * }
    * }}}
    */
  private val fullSliceFromSliceGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val sDecl = vpr.LocalVarDecl("s", ctx.slice.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()
      val kDecl = vpr.LocalVarDecl("k", vpr.Int)()

      // preconditions
      val pre1 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar))("The low bound of the slice might be negative")
      val pre2 = synthesized(vpr.LeCmp(iDecl.localVar, jDecl.localVar))("The low bound of the slice might exceed the high bound")
      val pre3 = synthesized(vpr.LeCmp(jDecl.localVar, kDecl.localVar))("The high bound of the slice might exceed the max bound")
      val pre4 = synthesized(vpr.LeCmp(kDecl.localVar, ctx.slice.cap(sDecl.localVar)()))("The max bound of the slice might exceed the capacity")

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), vpr.Add(ctx.slice.offset(sDecl.localVar)(), iDecl.localVar)())()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(kDecl.localVar, iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), ctx.slice.array(sDecl.localVar, typ)())()

      // function body
      val offset = ctx.slice.offset(sDecl.localVar)()
      val body = fullSliceFromArray(
        typ,
        ctx.slice.array(sDecl.localVar, typ)(),
        vpr.Add(offset, iDecl.localVar)(),
        vpr.Add(offset, jDecl.localVar)(),
        vpr.Add(offset, kDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.fullSliceFromSlice}_${Names.freshName}",
        Seq(sDecl, iDecl, jDecl, kDecl),
        ctx.slice.typ(typ),
        Seq(pre1, pre2, pre3, pre4),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "ssliceFromArray" Viper function supporting the
    * Viper domain of Slices. This function is used in the translation
    * of array slicing expressions "a[i:j]" in Gobra (without a
    * capacity argument). Its Viper definition is:
    *
    * {{{
    * function ssliceFromArray(a : Array[T], i : Int, j : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= alen(a)
    *   ensures soffset(result) == i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == alen(a) - i
    *   ensures sarray(result) == a
    * {
    *   sfullSliceFromArray(a, i, j, alen(a))
    * }
    * }}}
    */
  private val sliceFromArrayGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ : vpr.Type)(ctx : Context): vpr.Function = {
      // declarations
      val aDecl = vpr.LocalVarDecl("a", ctx.array.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()

      // preconditions
      val pre1 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar))("The low bound of the slice might be negative")
      val pre2 = synthesized(vpr.LeCmp(iDecl.localVar, jDecl.localVar))("The low bound of the slice might exceed the high bound")
      val pre3 = synthesized(vpr.LeCmp(jDecl.localVar, ctx.array.len(aDecl.localVar)()))("The high bound of the slice might exceed the array capacity")

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), iDecl.localVar)()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(ctx.array.len(aDecl.localVar)(), iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), aDecl.localVar)()

      // function body
      val body = fullSliceFromArray(
        typ,
        aDecl.localVar,
        iDecl.localVar,
        jDecl.localVar,
        ctx.array.len(aDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.sliceFromArray}_${Names.freshName}",
        Seq(aDecl, iDecl, jDecl),
        ctx.slice.typ(typ),
        Seq(pre1, pre2, pre3),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "ssliceFromSlice" Viper function that
    * supports the Viper domain of Slices. This function is used
    * in the Gobra translation of slicing expressions "s[i:k]"
    * where the base "s" is a slice itself.
    *
    * {{{
    * function ssliceFromSlice(s : Slice[T], i : Int, j : Int) : Slice[T]
    *   requires 0 <= i && i <= j && j <= scap(s)
    *   ensures soffset(result) == soffset(s) + i
    *   ensures slen(result) == j - i
    *   ensures scap(result) == scap(s) - i
    *   ensures sarray(result) == sarray(s)
    * {
    *   sfullSliceFromSlice(s, i, j, scap(s))
    * }
    * }}}
    */
  private val sliceFromSliceGenerator : FunctionGenerator[vpr.Type] = new FunctionGenerator[vpr.Type] {
    def genFunction(typ: vpr.Type)(ctx: Context): vpr.Function = {
      // declarations
      val sDecl = vpr.LocalVarDecl("s", ctx.slice.typ(typ))()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()
      val jDecl = vpr.LocalVarDecl("j", vpr.Int)()

      // preconditions
      val pre1 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar))("The low bound of the slice might be negative")
      val pre2 = synthesized(vpr.LeCmp(iDecl.localVar, jDecl.localVar))("The low bound of the slice might exceed the high bound")
      val pre3 = synthesized(vpr.LeCmp(jDecl.localVar, ctx.slice.cap(sDecl.localVar)()))("The high bound of the slice might exceed the capacity")

      // postconditions
      val result = vpr.Result(ctx.slice.typ(typ))()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), vpr.Add(ctx.slice.offset(sDecl.localVar)(), iDecl.localVar)())()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.Sub(jDecl.localVar, iDecl.localVar)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.Sub(ctx.slice.cap(sDecl.localVar)(), iDecl.localVar)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typ)(), ctx.slice.array(sDecl.localVar, typ)())()

      // function body
      val body = fullSliceFromSlice(
        typ,
        sDecl.localVar,
        iDecl.localVar,
        jDecl.localVar,
        ctx.slice.cap(sDecl.localVar)()
      )(ctx)()

      vpr.Function(
        s"${Names.sliceFromSlice}_${Names.freshName}",
        Seq(sDecl, iDecl, jDecl),
        ctx.slice.typ(typ),
        Seq(pre1, pre2, pre3),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }

  /**
    * Definition of the "nilSlice" Viper function that supports the Viper domain on Slices,
    * that represents the nil slice (of the specified type).
    *
    * {{{
    * function nilSlice() : Slice[T]
    *   ensures soffset(result) == 0
    *   ensures slen(result) == 0
    *   ensures scap(result) == 0
    *   ensures sarray(result) == defaultArray[T]()
    * {
    *   sconstruct(defaultArray[T](), 0, 0, 0)
    * }
    * }}}
    */
  private val nilSliceGenerator : FunctionGenerator[in.Type] = new FunctionGenerator[in.Type] {
    def genFunction(typ: in.Type)(ctx: Context): vpr.Function = {
      // translate type
      val typT = ctx.typeEncoding.typ(ctx)(typ)
      val sliceTypT = ctx.slice.typ(typT)

      // get default shared array of type `typ`
      val arrayT = in.ArrayT(1, typ, Shared)
      val dfltArray = in.DfltVal(arrayT)(Source.Parser.Internal)
      val dfltArrayT = arrayEmb.unbox(ctx.expr.translate(dfltArray)(ctx).res, arrayT)(dfltArray)(ctx)

      // postconditions
      val result = vpr.Result(sliceTypT)()
      val post1 = vpr.EqCmp(ctx.slice.offset(result)(), vpr.IntLit(0)())()
      val post2 = vpr.EqCmp(ctx.slice.len(result)(), vpr.IntLit(0)())()
      val post3 = vpr.EqCmp(ctx.slice.cap(result)(), vpr.IntLit(0)())()
      val post4 = vpr.EqCmp(ctx.slice.array(result, typT)(), dfltArrayT)()

      // function body
      val body: vpr.FuncApp = construct(
        typT,
        dfltArrayT,
        vpr.IntLit(0)(),
        vpr.IntLit(0)(),
        vpr.IntLit(0)()
      )(ctx)()

      vpr.Function(
        s"${Names.sliceDefaultFunc}_${Names.freshName}",
        Seq(),
        sliceTypT,
        Seq(),
        Seq(post1, post2, post3, post4),
        if (generateFunctionBodies) Some(body) else None
      )()
    }
  }
}
