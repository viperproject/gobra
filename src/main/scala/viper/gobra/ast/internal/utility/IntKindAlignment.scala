// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.utility

import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.util.TypeBounds

/**
  * Helpers to keep integer-typed binary operands of the same `IntegerKind`. Some internal AST
  * nodes hardcode `IntT(UnboundedInteger)` regardless of operand type (notably `in.Length` and
  * `in.Capacity`), while the frontend may infer a bounded kind for sibling literals. The
  * encoding requires both operands of equality / comparison nodes to be of the same kind so
  * that no Viper consistency violation arises when the encoded LHS and RHS land in different
  * Viper sorts.
  *
  * The alignment always demotes a bounded operand to `UnboundedInteger` via an `in.Conversion`
  * (the bounded → unbounded direction is total: the corresponding bridge function `from` has
  * no precondition). Promoting the unbounded operand would require an in-range check that may
  * not hold in general (e.g. `len(s)` is non-negative but unbounded above in the encoding).
  */
object IntKindAlignment {

  private val unboundedT: in.Type =
    in.IntT(Addressability.rValue, TypeBounds.UnboundedInteger)

  /**
    * If `l` and `r` are both integer-typed but with different IntegerKinds, align them.
    *
    * Preferred path: if one operand is an `in.IntLit` whose value fits in the *other* operand's
    * kind, retype the literal in place (no `in.Conversion` introduced). This keeps the
    * encoding free of `wrap` roundtrips that otherwise lose precision.
    *
    * Fallback: demote the bounded operand to `UnboundedInteger` via an `in.Conversion` (the
    * `from` bridge function has no precondition, so this is total).
    *
    * If both operands are bounded but of different kinds (e.g. `int8 == int16`), no alignment
    * is performed — that is a type-checker concern, not an alignment concern.
    */
  def alignIntKinds(l: in.Expr, r: in.Expr): (in.Expr, in.Expr) = (l.typ, r.typ) match {
    case (in.IntT(_, lk), in.IntT(_, rk)) if lk != rk =>
      // 1) Retype an IntLit to match the other side, when its value fits.
      (l, r) match {
        case (lit: in.IntLit, _) if fitsInKind(lit.v, rk) =>
          return (in.IntLit(lit.v, rk, lit.base)(lit.info), r)
        case (_, lit: in.IntLit) if fitsInKind(lit.v, lk) =>
          return (l, in.IntLit(lit.v, lk, lit.base)(lit.info))
        case _ =>
      }
      // 2) A constant expression (e.g. `-1`, i.e. Sub(0, 1)) of mathematical/untyped kind
      //    next to a bounded sibling: fold it to a literal of the bounded kind, when it fits.
      //    Demoting the bounded side instead would leak a Viper `Int` into a context whose
      //    surrounding sort (e.g. a pure function's bounded return type) is a domain type.
      if (isMathematical(lk) && rk.isInstanceOf[TypeBounds.BoundedIntegerKind])
        foldConst(l) match {
          case Some(v) if fitsInKind(v, rk) => return (in.IntLit(v, rk)(l.info), r)
          case _ =>
        }
      if (isMathematical(rk) && lk.isInstanceOf[TypeBounds.BoundedIntegerKind])
        foldConst(r) match {
          case Some(v) if fitsInKind(v, lk) => return (l, in.IntLit(v, lk)(r.info))
          case _ =>
        }
      // 3) Otherwise, demote the bounded operand to integer.
      if (isMathematical(lk))
        (l, in.Conversion(unboundedT, r)(r.info))
      else if (isMathematical(rk))
        (in.Conversion(unboundedT, l)(l.info), r)
      else
        (l, r)

    // Ghost collections of ints whose element kinds disagree (e.g. `seq[1..4] == seq[int]{1,2,3}`,
    // where the range sequence has mathematical-integer elements while the literal has bounded
    // `int` elements). The mathematical side is promoted to the bounded element kind — see
    // [[coerceToElemKind]].
    case (lt, rt) if elemKindMismatch(lt, rt) =>
      (elemIntKind(lt), elemIntKind(rt)) match {
        case (Some(lk: TypeBounds.BoundedIntegerKind), Some(_)) => (l, coerceToElemKind(r, lk))
        case (Some(_), Some(rk: TypeBounds.BoundedIntegerKind)) => (coerceToElemKind(l, rk), r)
        case _ => (l, r)
      }

    case _ => (l, r)
  }

  /** True for the kinds whose Viper encoding is a plain `Int` (as opposed to a bounded domain). */
  private def isMathematical(k: TypeBounds.IntegerKind): Boolean =
    k == TypeBounds.UnboundedInteger || k == TypeBounds.UntypedConstInteger

  /** Statically evaluates simple constant integer expressions (literals combined with +, -, *). */
  private def foldConst(e: in.Expr): Option[BigInt] = e match {
    case lit: in.IntLit => Some(lit.v)
    case in.Add(l, r) => for { a <- foldConst(l); b <- foldConst(r) } yield a + b
    case in.Sub(l, r) => for { a <- foldConst(l); b <- foldConst(r) } yield a - b
    case in.Mul(l, r) => for { a <- foldConst(l); b <- foldConst(r) } yield a * b
    case _ => None
  }

  /** The integer element/member kind of a ghost collection or option type, if any. */
  private def elemIntKind(t: in.Type): Option[TypeBounds.IntegerKind] = t match {
    case in.SequenceT(in.IntT(_, k), _) => Some(k)
    case in.SetT(in.IntT(_, k), _) => Some(k)
    case in.MultisetT(in.IntT(_, k), _) => Some(k)
    case in.OptionT(in.IntT(_, k), _) => Some(k)
    case _ => None
  }

  /** True if both types are the same ghost collection / option over ints but with different elem
    * kinds, where exactly one side is mathematical (`integer` / untyped). */
  private def elemKindMismatch(lt: in.Type, rt: in.Type): Boolean = {
    def unbounded(k: TypeBounds.IntegerKind): Boolean =
      k == TypeBounds.UnboundedInteger || k == TypeBounds.UntypedConstInteger
    ((lt, rt) match {
      case (_: in.SequenceT, _: in.SequenceT) => true
      case (_: in.SetT, _: in.SetT) => true
      case (_: in.MultisetT, _: in.MultisetT) => true
      case (_: in.OptionT, _: in.OptionT) => true
      case _ => false
    }) && ((elemIntKind(lt), elemIntKind(rt)) match {
      case (Some(lk), Some(rk)) => lk != rk && (unbounded(lk) ^ unbounded(rk))
      case _ => false
    })
  }

  /**
    * Coerces a ghost collection expression with mathematical-integer elements to the bounded
    * element kind `k`. Sequence-typed expressions are wrapped with an `in.Conversion` to
    * `seq[k]`, which the encoding translates to a per-kind `Seq[Int] -> Seq[Bounded_k]`
    * mapping function. Set-/multiset-typed expressions have no direct mapping function;
    * instead, the coercion is pushed through the structure (conversions from sequences and
    * set operations) until it reaches sequence level.
    */
  def coerceToElemKind(e: in.Expr, k: TypeBounds.BoundedIntegerKind): in.Expr = {
    def boundedElemT: in.Type = in.IntT(Addressability.mathDataStructureElement, k)
    e.typ match {
      case in.IntT(_, ek) if ek != k => // element-level coercion (used for recursion helpers)
        in.Conversion(in.IntT(Addressability.rValue, k), e)(e.info)
      case _: in.SequenceT =>
        in.Conversion(in.SequenceT(boundedElemT, e.typ.addressability), e)(e.info)
      case t: in.SetT => e match {
        case in.SetConversion(s) => in.SetConversion(coerceToElemKind(s, k))(e.info)
        case in.Union(a, b, _)        => in.Union(coerceToElemKind(a, k), coerceToElemKind(b, k), in.SetT(boundedElemT, t.addressability))(e.info)
        case in.Intersection(a, b, _) => in.Intersection(coerceToElemKind(a, k), coerceToElemKind(b, k), in.SetT(boundedElemT, t.addressability))(e.info)
        case in.SetMinus(a, b, _)     => in.SetMinus(coerceToElemKind(a, k), coerceToElemKind(b, k), in.SetT(boundedElemT, t.addressability))(e.info)
        case _ => e // no general Set[Int] -> Set[Bounded_k] mapping; leave unchanged
      }
      case t: in.MultisetT => e match {
        case in.MultisetConversion(s) => in.MultisetConversion(coerceToElemKind(s, k))(e.info)
        case in.Union(a, b, _)        => in.Union(coerceToElemKind(a, k), coerceToElemKind(b, k), in.MultisetT(boundedElemT, t.addressability))(e.info)
        case in.Intersection(a, b, _) => in.Intersection(coerceToElemKind(a, k), coerceToElemKind(b, k), in.MultisetT(boundedElemT, t.addressability))(e.info)
        case in.SetMinus(a, b, _)     => in.SetMinus(coerceToElemKind(a, k), coerceToElemKind(b, k), in.MultisetT(boundedElemT, t.addressability))(e.info)
        case _ => e
      }
      // Options: push the element coercion into the constructor — `some(v)` -> `some(int(v))`,
      // `none[integer]` -> `none[int]`. There is no general Option[Int] -> Option[Bounded_k]
      // mapping function, so a plain option variable is left unchanged.
      case _: in.OptionT => e match {
        case in.OptionSome(x) => in.OptionSome(coerceToElemKind(x, k))(e.info)
        case _: in.OptionNone => in.OptionNone(boundedElemT)(e.info)
        case _ => e
      }
      case _ => e
    }
  }

  /** True if `v` is representable in `kind`. UnboundedInteger admits any value. */
  private def fitsInKind(v: BigInt, kind: TypeBounds.IntegerKind): Boolean = kind match {
    case TypeBounds.UnboundedInteger => true
    case bk: TypeBounds.BoundedIntegerKind => v >= bk.lower && v <= bk.upper
    case _ => true
  }

  /**
    * If `e` has a bounded integer type, wrap it with an `in.Conversion` to integer. Otherwise
    * return `e` unchanged. Useful for slots that require a Viper Int (sequence indices, slice
    * indices, perm-constructor numerator/denominator, etc.) but that may receive a bounded
    * integer value because the frontend infers a concrete kind for sibling literals.
    */
  def asUnboundedInt(e: in.Expr): in.Expr = asUnboundedInt(e, e.typ)

  /**
    * Like [[asUnboundedInt(e:viper\.gobra\.ast\.internal\.Expr)*]], but decides based on a
    * caller-resolved type. Use this variant when `e`'s type may be a defined type whose
    * *underlying* type is a bounded integer (e.g. `type Type uint8`): the caller resolves the
    * underlying type (`underlyingType(e.typ)(ctx)` in encodings, `underlyingType` in the
    * desugarer) since this utility has no access to type-declaration lookups.
    */
  def asUnboundedInt(e: in.Expr, resolvedTyp: in.Type): in.Expr = resolvedTyp match {
    case in.IntT(_, k) if k != TypeBounds.UnboundedInteger =>
      // Bounded literals are NOT retyped to plain integer literals here: the Conversion's
      // `from(to(c))` roundtrip keeps the ground `to(c)` bridge-axiom anchors that Z3's
      // nonlinear reasoning demonstrably relies on (see BoundedIntEncoding.asInt).
      in.Conversion(unboundedT, e)(e.info)
    case _ => e
  }
}
