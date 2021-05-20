// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings.maps

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.encodings.maps.MapEncoding.repeatedKeyErrorT
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.{assert, sequence, unit}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class MathematicalMapEncoding extends LeafTypeEncoding {
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.MathematicalMap(k, v) / m =>
      m match {
        case Exclusive => vpr.MapType(ctx.typeEncoding.typ(ctx)(k), ctx.typeEncoding.typ(ctx)(v))
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes an assignment.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    * [(e: mmap[K]V)[idx] = rhs] -> [ e = e[idx := rhs] ]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee(in.IndexedExp(base :: ctx.MathematicalMap(_, _), idx) :: _ / Exclusive), rhs, src) =>
      for {
        stmt <- ctx.typeEncoding.assignment(ctx)(in.Assignee(base), in.GhostCollectionUpdate(base, idx, rhs)(src.info), src)
      } yield stmt
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * Most cases are a one-to-one mapping to Viper's sequence operations.
    * R[ dflt(mmap[K]V°) ] -> EmptyMap([K], [V])
    * R[ mmapLit(E) ] -> if E.isEmpty then EmptyMap else ExplicitMap([E]) (also checks that the values of keys are all
    * *   distinct and throws an error if not)
    * R[ (e: mmap[K]V)[idx] ] -> MapLookup([e], [idx])
    * R[ (e: mmap[K]V)[idx = val] ] -> MapUpdate([e], [idx], [val])
    * R[ len(e: mmap[K]V) ] -> MapCardinality([e])
    * R[ keySet(e: mmap[K]V) ] -> MapDomain([e])
    * R[ valueSet(e: mmap[K]V) ] -> MapRange([e])
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.expr(ctx)){
      case (e: in.DfltVal) :: ctx.MathematicalMap(k, v) / Exclusive =>
        unit(withSrc(vpr.EmptyMap(ctx.typeEncoding.typ(ctx)(k), ctx.typeEncoding.typ(ctx)(v)), e))

      case (lit: in.MathMapLit) :: ctx.MathematicalMap(keyT, valT) / Exclusive =>
        val (pos, info, errT) = lit.vprMeta
        val vprKeyT = goT(keyT)
        val vprValT = goT(valT)
        for {
          mapletList <- sequence(lit.entries.toVector.map {
            case (key, value) => for { k <- goE(key); v <- goE(value) } yield vpr.Maplet(k, v)(pos, info, errT)
          })
          mmapVal <- if (mapletList.nonEmpty) {
            // silver assumes that the argument to ExplicitMap is not empty
            val keySeq = mapletList map (_.key)
            // checks whether all keys are distinct
            val checkAllDiffKeys = vpr.EqCmp(
              vpr.AnySetCardinality(vpr.ExplicitSet(keySeq)(pos, info, errT))(pos, info, errT),
              vpr.SeqLength(vpr.ExplicitSeq(keySeq)(pos, info, errT))(pos, info, errT)
            )(pos, info, errT)
            assert(checkAllDiffKeys, vpr.ExplicitMap(mapletList)(pos, info, errT), repeatedKeyErrorT)(ctx)
          } else {
            unit(vpr.EmptyMap(vprKeyT, vprValT)(pos, info, errT))
          }
        } yield mmapVal

      case n@ in.IndexedExp(e :: ctx.MathematicalMap(_, _), idx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vE <- goE(e)
          vIdx <- goE(idx)
        } yield vpr.MapLookup(vE, vIdx)(pos, info, errT)

      case n@ in.GhostCollectionUpdate(base :: ctx.MathematicalMap(_, _), left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vBase <- goE(base)
          vLeft <- goE(left)
          vRight <- goE(right)
        } yield vpr.MapUpdate(vBase, vLeft, vRight)(pos, info, errT)

      case n@ in.Length(e :: ctx.MathematicalMap(_, _)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(vpr.MapCardinality(_)(pos, info, errT))

      case n@ in.MapKeys(e :: ctx.MathematicalMap(_, _)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(vpr.MapDomain(_)(pos, info, errT))

      case n@ in.MapValues(e :: ctx.MathematicalMap(_, _)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(vpr.MapRange(_)(pos, info, errT))
    }
  }
}
