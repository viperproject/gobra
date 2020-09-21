package viper.gobra.translator.encodings.sets

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import org.checkerframework.checker.units.qual.Area
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
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
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as r-values, i.e. values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * Most cases are a one-to-one mapping to Viper's set operations.
    * R[ x # (e: set[T]) ] -> [x] in [e] ? 1 : 0
    */
  override def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.rValue(ctx)){

      case (e: in.DfltVal) :: ctx.Set(t) => unit(vpr.EmptySet(ctx.typeEncoding.typ(ctx)(t)).tupled(e.vprMeta))
      case (e: in.DfltVal) :: ctx.Multiset(t) => unit(vpr.EmptyMultiset(ctx.typeEncoding.typ(ctx)(t)).tupled(e.vprMeta))

      case (lit: in.SequenceLit) :: ctx.Set(t) =>
        val (pos, info, errT) = lit.vprMeta
        if (lit.exprs.isEmpty) unit(vpr.EmptySet(ctx.typeEncoding.typ(ctx)(t))(pos, info, errT))
        else sequence(lit.exprs map goE).map(args => vpr.ExplicitSet(args)(pos, info, errT))

      case (lit: in.SequenceLit) :: ctx.Multiset(t) =>
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

}
