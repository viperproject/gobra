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
      // 2) Otherwise, demote the bounded operand to integer.
      if (lk == TypeBounds.UnboundedInteger)
        (l, in.Conversion(unboundedT, r)(r.info))
      else if (rk == TypeBounds.UnboundedInteger)
        (in.Conversion(unboundedT, l)(l.info), r)
      else
        (l, r)
    case _ => (l, r)
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
  def asUnboundedInt(e: in.Expr): in.Expr = e.typ match {
    case in.IntT(_, k) if k != TypeBounds.UnboundedInteger =>
      in.Conversion(unboundedT, e)(e.info)
    case _ => e
  }
}
