// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.sets

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class SetEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Set(t) / m =>
      m match {
        case Exclusive => vpr.SetType(ctx.typeEncoding.typ(ctx)(t))
        case Shared    => vpr.Ref
      }

    case ctx.Multiset(t) / m =>
      m match {
        case Exclusive => vpr.MultisetType(ctx.typeEncoding.typ(ctx)(t))
        case Shared    => vpr.Ref: vpr.Type
      }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * Most cases are a one-to-one mapping to Viper's set operations.
    * R[ x in (e: mset[T]) ] -> ([x] in [e]) > 0
    * R[ x # (e: set[T]) ] -> [x] in [e] ? 1 : 0
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){

      case (e: in.DfltVal) :: ctx.Set(t) / Exclusive => unit(withSrc(vpr.EmptySet(ctx.typeEncoding.typ(ctx)(t)), e))
      case (e: in.DfltVal) :: ctx.Multiset(t) / Exclusive => unit(withSrc(vpr.EmptyMultiset(ctx.typeEncoding.typ(ctx)(t)), e))

      case (lit: in.SetLit) :: ctx.Set(t) =>
        val (pos, info, errT) = lit.vprMeta
        if (lit.exprs.isEmpty) unit(vpr.EmptySet(ctx.typeEncoding.typ(ctx)(t))(pos, info, errT))
        else sequence(lit.exprs map goE).map(args => vpr.ExplicitSet(args)(pos, info, errT))

      case (lit: in.MultisetLit) :: ctx.Multiset(t) =>
        val (pos, info, errT) = lit.vprMeta
        if (lit.exprs.isEmpty) unit(vpr.EmptyMultiset(ctx.typeEncoding.typ(ctx)(t))(pos, info, errT))
        else sequence(lit.exprs map goE).map(args => vpr.ExplicitMultiset(args)(pos, info, errT))

      case n@ in.Cardinality(exp) =>
        val (pos, info, errT) = n.vprMeta
        for {
          expT <- goE(exp)
        } yield vpr.AnySetCardinality(expT)(pos, info, errT)

      case n@ in.Contains(x, e :: ctx.Set(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield vpr.AnySetContains(vX, vE)(pos, info, errT)

      case n@ in.Contains(x, e :: ctx.Multiset(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield vpr.AnySetContains(vX, vE)(pos, info, errT)

      case n@ in.Multiplicity(x, e :: ctx.Set(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
          multiplicity = vpr.CondExp(
            vpr.AnySetContains(vX, vE)(pos, info, errT),
            vpr.IntLit(1)(pos, info, errT),
            vpr.IntLit(0)(pos, info, errT)
          )(pos, info, errT)
        } yield multiplicity

      case n@ in.Multiplicity(x, e :: ctx.Multiset(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield vpr.AnySetContains(vX, vE)(pos, info, errT)

      case n@ in.Union(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.AnySetUnion(leftT, rightT)(pos, info, errT)

      case n@ in.Intersection(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.AnySetIntersection(leftT, rightT)(pos, info, errT)

      case n@ in.SetMinus(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.AnySetMinus(leftT, rightT)(pos, info, errT)

      case n@ in.Subset(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.AnySetSubset(leftT, rightT)(pos, info, errT)
    }
  }

  /**
    * Encodes whether a value is comparable or not.
    *
    * isComp[ e: set[T] ] -> forall s :: { s in [e], isComp[s] } s in [e] ==> isComp[s]
    * isComp[ e: mset[T] ] -> forall s :: { s in [e], isComp[s] } (s in [e]) > 0 ==> isComp[s]
    */
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case exp :: ctx.AnySet(t) =>
      super.isComparable(ctx)(exp).map{ _ =>
        val (pos, info, errT) = exp.vprMeta
        // if this is executed, then type parameter must have dynamic comparability
        val s = in.BoundVar("s", t)(exp.info)
        val vSDecl = ctx.typeEncoding.variable(ctx)(s); val vS = vSDecl.localVar
        for {
          vExp <- pure(ctx.expr.translate(exp)(ctx))(ctx)
          rhs <- pure(ctx.typeEncoding.isComparable(ctx)(s)
            .getOrElse(Violation.violation("An incomparable set or mset entails an incomparable element type.")))(ctx)
          contains = vpr.AnySetContains(vS, vExp)(pos, info, errT)
          lhs = exp.typ match {
            case ctx.Set(_) => contains
            case ctx.Multiset(_) => vpr.GtCmp(contains, vpr.IntLit(0)(pos, info, errT))(pos, info, errT)
            case t => Violation.violation(s"expected set or mset, but got $t")
          }
          res = vpr.Forall(
            variables = Seq(vSDecl),
            triggers = Seq(vpr.Trigger(Seq(rhs, contains))(pos, info, errT)),
            exp = vpr.Implies(lhs, rhs)(pos, info, errT)
          )(pos, info, errT)
        } yield res: vpr.Exp
      }
  }

}
