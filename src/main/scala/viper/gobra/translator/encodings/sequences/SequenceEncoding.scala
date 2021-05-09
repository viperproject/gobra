// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.sequences

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class SequenceEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  override def finalize(col: Collector): Unit = {
    emptySeqFunc.finalize(col)
  }

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Seq(t) / m =>
      m match {
        case Exclusive => vpr.SeqType(ctx.typeEncoding.typ(ctx)(t))
        case Shared    => vpr.Ref
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
    * [loc: T@ = rhs] -> [loc] = [rhs]
    *
    * [(e: seq[T])[idx] = rhs] -> [ e = e[idx := rhs] ]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee(in.IndexedExp(base :: ctx.Seq(_), idx) :: _ / Exclusive), rhs, src) =>
      ctx.typeEncoding.assignment(ctx)(in.Assignee(base), in.GhostCollectionUpdate(base, idx, rhs)(src.info), src)
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * Most cases are a one-to-one mapping to Viper's sequence operations.
    * R[ seq(e: [n]T) ] -> [e]
    * R[ set(e: [n]T) ] -> seqToSet([e])
    * R[ mset(e: [n]T) ] -> seqToMultiset([e])
    * R[ x # (e: [n]T) ] -> [x] # [e]
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){

      case n@ in.IndexedExp(e :: ctx.Seq(_), idx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vE <- goE(e)
          vIdx <- goE(idx)
        } yield vpr.SeqIndex(vE, vIdx)(pos, info, errT)

      case n@ in.GhostCollectionUpdate(base :: ctx.Seq(_), left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vBase <- goE(base)
          vLeft <- goE(left)
          vRight <- goE(right)
        } yield vpr.SeqUpdate(vBase, vLeft, vRight)(pos, info, errT)

      case (e: in.DfltVal) :: ctx.Seq(t) / Exclusive =>
        unit(withSrc(vpr.EmptySeq(ctx.typeEncoding.typ(ctx)(t)), e))

      case (lit: in.SequenceLit) :: ctx.Seq(t) => {
        val (indices, exprs) = lit.elems.unzip
        val chunks = chunkify(indices.toVector, lit.length)
        val (pos, info, errT) = lit.vprMeta

        for {
          vExprs <- sequence(exprs.toVector.map(e => ctx.expr.translate(e)(ctx)))
          vElems = indices.zip(vExprs).toMap
          vChunks = chunks.map(translate(t, _, vElems)(ctx)(lit))
        } yield vChunks match {
          case Vector() => vpr.EmptySeq(ctx.typeEncoding.typ(ctx)(t))(pos, info, errT)
          case vChunks => vChunks.reduce[vpr.Exp] {
            case (l, r) => vpr.SeqAppend(l, r)(pos, info, errT)
          }
        }
      }

      case n@ in.Length(e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(vpr.SeqLength(_)(pos, info, errT))

      case in.SequenceConversion(e :: ctx.Seq(_)) => goE(e)

      case n@ in.SetConversion(e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(ctx.seqToSet.create(_)(pos, info, errT))

      case n@ in.MultisetConversion(e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(ctx.seqToMultiset.create(_)(pos, info, errT))

      case n@ in.Contains(x, e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield vpr.SeqContains(vX, vE)(pos, info, errT)

      case n@ in.Multiplicity(x, e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield ctx.seqMultiplicity.create(vX, vE)(pos, info, errT)

      case n@ in.RangeSequence(low, high) =>
        val (pos, info, errT) = n.vprMeta
        for {
          lowT <- goE(low)
          highT <- goE(high)
        } yield vpr.RangeSeq(lowT, highT)(pos, info, errT)

      case n@ in.SequenceAppend(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.SeqAppend(leftT, rightT)(pos, info, errT)

      case n@ in.SequenceDrop(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.SeqDrop(leftT, rightT)(pos, info, errT)

      case n@ in.SequenceTake(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.SeqTake(leftT, rightT)(pos, info, errT)
    }
  }

/**
  * Encodes whether a value is comparable or not.
  *
  * isComp[ e: seq[T] ] -> forall s :: { s in [e], isComp[s] } s in [e] ==> isComp[s]
  */
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case exp :: ctx.Seq(t) =>
      super.isComparable(ctx)(exp).map { _ =>
        val (pos, info, errT) = exp.vprMeta
        // if this is executed, then type parameter must have dynamic comparability
        val s = in.BoundVar("s", t)(exp.info)
        val vSDecl = ctx.typeEncoding.variable(ctx)(s); val vS = vSDecl.localVar
        for {
          vExp <- pure(ctx.expr.translate(exp)(ctx))(ctx)
          rhs <- pure(ctx.typeEncoding.isComparable(ctx)(s)
            .getOrElse(Violation.violation("An incomparable sequence entails an incomparable element type.")))(ctx)
          contains = vpr.SeqContains(vS, vExp)(pos, info, errT)
          res = vpr.Forall(
          variables = Seq(vSDecl),
          triggers = Seq(vpr.Trigger(Seq(rhs, contains))(pos, info, errT)),
          exp = vpr.Implies(contains, rhs)(pos, info, errT)
          )(pos, info, errT)
        } yield res
      }
  }

  /**
    * Translates `chunk` into a sequence expression.
    *
    * [EmptyChunk(size)] -> emptySeq(size)
    * [NonEmptyChunk(e0,...,en)] -> Seq([e0], ..., [en])
    */
  private def translate(typ : in.Type, chunk : LiteralChunk, elems : Map[BigInt, vpr.Exp])(ctx : Context)(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    chunk match {
      case EmptyChunk(size) => emptySeqFunc(Vector(vpr.IntLit(size)(pos, info, errT)), typ)(pos, info, errT)(ctx)
      case chunk: NonEmptyChunk => {
        val dfltElem = in.DfltVal(typ)(Source.Parser.Internal)
        val vDfltElem = ctx.expr.translate(dfltElem)(ctx).res
        val vElems = Range.BigInt(0, chunk.size, 1).map(i => elems.getOrElse(chunk.firstIndex + i, vDfltElem))
        vpr.ExplicitSeq(vElems)(pos, info, errT)
      }
    }
  }

  /**
    * Generates, for any given type `T`:
    * {{{
    * function emptySeq_`T`(n : Int) : Seq[`T`]
    *   requires 0 <= n
    *   ensures |result| == n
    *   ensures forall i : Int :: { result[i] } 0 <= i < n ==> result[i] == dfltVal(`T`)
    * }}}
    */
  private val emptySeqFunc: FunctionGenerator[in.Type] = new FunctionGenerator[in.Type] {
    def genFunction(t : in.Type)(ctx : Context) : vpr.Function = {
      // parameters
      val nDecl = vpr.LocalVarDecl("n", vpr.Int)()
      val iDecl = vpr.LocalVarDecl("i", vpr.Int)()

      // return type
      val vInnerType = ctx.typeEncoding.typ(ctx)(t)
      val vResultType = vpr.SeqType(vInnerType)
      val vResult = vpr.Result(vResultType)()
      val vResultLength = vpr.SeqLength(vResult)()

      // default value of type `t`
      val dfltElem = in.DfltVal(t)(Source.Parser.Internal)
      val vDfltElem = ctx.expr.translate(dfltElem)(ctx).res

      // preconditions
      val pre1 = synthesized(vpr.LeCmp(vpr.IntLit(0)(), nDecl.localVar))("Sequence length might be negative")

      // postconditions
      val post1 = vpr.EqCmp(vResultLength, nDecl.localVar)()
      val vResultIndex = vpr.SeqIndex(vResult, iDecl.localVar)()
      val post2 = vpr.Forall(
        Vector(iDecl),
        Vector(vpr.Trigger(Vector(vResultIndex))()),
        vpr.Implies(
          vpr.And(vpr.LeCmp(vpr.IntLit(0)(), iDecl.localVar)(), vpr.LtCmp(iDecl.localVar, nDecl.localVar)())(),
          vpr.EqCmp(vResultIndex, vDfltElem)()
        )()
      )()

      vpr.Function(
        name = s"${Names.emptySequenceFunc}_${Names.freshName}",
        formalArgs = Vector(nDecl),
        typ = vResultType,
        pres = Vector(pre1),
        posts = Vector(post1, post2),
        body = None
      )()
    }
  }


  /* * Utils */

  /**
    * Transforms a given sequence `indices` of keys/indices
    * used in a sequence literal into a list of "chunks".
    * An example is given below.
    *
    * The goal of `chunkify` is to identify any big gaps between
    * consecutive indices (i.e., gaps of size at least `threshold`),
    * and turn these into `EmptyChunk`s.
    *
    * Consecutive indices that are relatively close to each other
    * (i.e., differ at most `threshold`) are grouped into `NonEmptyChunk`s.
    *
    * The key idea now is to translate `NonEmptyChunk`s differently
    * from `EmptyChunk`s, in a way that allows efficiently representing
    * sequence literals with potentially large differences in keys/literals.
    *
    * To give an example, the keys in this Gobra sequence
    *
    *   'seq[int] { 2, 1, 5:3, 124:5, 127:9, 1000:12 }'
    *
    * are "chunkified" roughly into
    *
    *   '{ NonEmptyChunk({ 0, 1, 5 }), EmptyChunk(118),
    *       NonEmptyChunk({ 124, 127 }), EmptyChunk(872),
    *         NonEmptyChunk({ 1000 }) }'
    *
    * from which the following Viper translation can be derived:
    *
    *   'Seq(2, 1, 0, 0, 0, 3) ++ sequenceEmpty(118) ++
    *     Seq(5, 0, 0, 9) ++ sequenceEmpty(872) ++ Seq(12)'
    */
  private def chunkify(indices : Vector[BigInt], length : BigInt, threshold : Int = 5) : Vector[LiteralChunk] = {
    require(0 <= length, "Non-negative length expected")
    require(0 < threshold, "Positive threshold expected")

    def prependZero(xs : Vector[BigInt]) : Vector[BigInt] = xs match {
      case Vector() => Vector()
      case xs if 0 < xs.head && xs.head < threshold => BigInt(0) +: xs
      case xs => xs
    }

    def appendLength(xs : Vector[BigInt]) : Vector[BigInt] = xs match {
      case Vector() => Vector()
      case xs if xs.last + 1 < length && length < xs.last + threshold => xs :+ length - 1
      case xs => xs
    }

    def nonEmptyChunks(xs : Vector[BigInt]) : Vector[NonEmptyChunk] = {
      val zippedIndices = false +: xs.sliding(2).map(x => x.last < x.head + threshold).toVector
      NonEmptyChunk.from(zippedIndices.zip(xs))
    }

    prependZero(appendLength(indices.sortBy(x => x))) match {
      case Vector() if length == 0 => Vector()
      case Vector() => Vector(EmptyChunk(length))
      case sortedIndices => {
        val prefix = if (0 < sortedIndices.head) Vector(EmptyChunk(sortedIndices.head)) else Vector()
        val body = completeGaps(nonEmptyChunks(sortedIndices))
        val suffix = if (sortedIndices.last + 1 < length) Vector(EmptyChunk(length - sortedIndices.last - 1)) else Vector()
        prefix ++ body ++ suffix
      }
    }
  }

  private sealed trait LiteralChunk {
    def size : BigInt
  }

  private case class EmptyChunk(size : BigInt) extends LiteralChunk {
    require(0 < size, "Positive size expected")
  }

  private case class NonEmptyChunk(indices : Vector[BigInt]) extends LiteralChunk {
    require(indices.nonEmpty, "Non-empty collection of indices expected")
    val firstIndex : BigInt = indices.head
    val lastIndex : BigInt = indices.last
    val size : BigInt = lastIndex - firstIndex + 1
  }

  private object NonEmptyChunk {

    /**
      * Groups the right elements of tuples in `indices` according to
      * patterns 'false, true, true, true, ....' in the left elements of
      * the tuples. For any such pattern encountered a new group will be formed.
      */
    def from(indices : Vector[(Boolean, BigInt)]) : Vector[NonEmptyChunk] = {
      indices match {
        case Vector() => Vector()
        case indices =>
          val left = indices.head +: indices.tail.takeWhile(_._1)
          val right = indices.tail.dropWhile(_._1)
          NonEmptyChunk(left.map(_._2)) +: from(right)
      }
    }
  }

  private def completeGaps(chunks : Vector[NonEmptyChunk]) : Vector[LiteralChunk] = chunks match {
    case Vector() => Vector()
    case Vector(chunk) => Vector(chunk)
    case c1 +: c2 +: cs =>
      c1 +: EmptyChunk(c2.firstIndex - c1.lastIndex - 1) +: completeGaps(c2 +: cs)
    case c => Violation.violation(s"This case should be unreachable, but got $c")
  }
}
