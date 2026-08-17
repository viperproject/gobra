// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.{internal => in}
import viper.gobra.util.TypeBounds.{BoundedIntegerKind, Signed}
import viper.gobra.util.Violation

object ConstantPropagation extends InternalTransform {
  override def name(): String = "constant_propagation"

  /**
    * Upper bound on a shift count we are willing to evaluate. Mirrors the limit the type-checker
    * applies to untyped constant shifts (`ExprTyping.MAX_SHIFT`), and guards against materialising an
    * astronomically large `BigInt` for something like `1 << 1000000`.
    */
  private val MaxFoldShift: Int = 512

  override def transform(p: in.Program): in.Program = {
    val (constDecls, noConstDecls) = p.members.partition(_.isInstanceOf[in.GlobalConstDecl])
    def propagate[T <: in.Node](n: T): T = n.transform{
      case c: in.GlobalConst =>
        val litOpt = constDecls collectFirst { case in.GlobalConstDecl(l, r) if l == c => r.withInfo(c.info) }
        litOpt match {
          case Some(l) => l
          case _ => Violation.violation(s"Did not find declaration of constant $c")
        }
    }

    def propagateAndFold[T <: in.Node](n: T): T = foldBitwise(propagate(n))

    // TODO: duplicated work can be avoided - currently, propagate is applied twice per member
    val newTable = new in.LookupTable(
      definedTypes = p.table.definedTypes,
      definedMethods = p.table.definedMethods.view.mapValues(propagateAndFold).toMap,
      definedFunctions = p.table.definedFunctions.view.mapValues(propagateAndFold).toMap,
      definedMPredicates = p.table.definedMPredicates.view.mapValues(propagateAndFold).toMap,
      definedFPredicates = p.table.definedFPredicates.view.mapValues(propagateAndFold).toMap,
      definedFuncLiterals = p.table.definedFuncLiterals.view.mapValues(propagateAndFold).toMap,
      directMemberProxies = p.table.directMemberProxies,
      directInterfaceImplementations = p.table.directInterfaceImplementations,
      implementationProofPredicateAliases = p.table.implementationProofPredicateAliases,
    )

    in.Program(
      types = p.types,
      members = noConstDecls.map(propagateAndFold), // does not emit constant declarations
      table = newTable,
    )(p.info)
  }

  /**
    * Evaluates bitwise and shift operations whose operands are integer literals.
    *
    * Go evaluates constant expressions at compile time with arbitrary precision, so `0 | 1` is the
    * constant `1` and never a runtime operation. Gobra only reproduced half of that: the frontend
    * evaluates *named* constants, but an expression written inline — say `ensures 0 | 1 == 1`, the
    * shape VerifiedSCION's serialization lemmas use — survived into the encoding, where it became an
    * application of an uninterpreted bitwise function and so could not be proved at all.
    *
    * Folding here rather than in the frontend means constants substituted by [[propagate]] above are
    * folded too, which is what makes `const C = 240 & 760` behave like the literal it denotes.
    *
    * The traversal is innermost, so nested expressions collapse from the inside out.
    */
  private def foldBitwise[T <: in.Node](n: T): T = n.transform(Function.unlift(tryFold))

  /**
    * The rewrite rule, as a total function returning `None` where nothing folds.
    *
    * It matters that this is `unlift`ed into the partial function rather than written as a `case`
    * per operator returning the node unchanged: a partial function that matches every bitwise node
    * reports a successful rewrite even when it changes nothing, which makes the strategy rebuild the
    * surrounding tree and lose sharing. `FoldChecksumLemma` in VerifiedSCION — whose postcondition
    * binds a `let` and mentions it twice — went from verifying in seconds to not terminating.
    */
  private def tryFold(n: in.Node): Option[in.Node] = n match {
    case e: in.BitAnd   => binary(e, e.left, e.right)(_ & _)
    case e: in.BitOr    => binary(e, e.left, e.right)(_ | _)
    case e: in.BitXor   => binary(e, e.left, e.right)(_ ^ _)
    case e: in.BitClear => binary(e, e.left, e.right)(_ &~ _)
    case e: in.BitNeg   => literal(e.op).flatMap(complement(e, _))

    case e: in.ShiftLeft => (literal(e.left), literal(e.right)) match {
      // A shift count beyond the fold limit is left alone: it is either rejected by the
      // type-checker or, for a bounded operand, saturates — both of which the encoding handles.
      case (Some(a), Some(b)) if b >= 0 && b <= MaxFoldShift => folded(e, a << b.toInt)
      case _ => None
    }

    case e: in.ShiftRight => (literal(e.left), literal(e.right)) match {
      // BigInt's `>>` is arithmetic, which matches Go for both signed and (non-negative) unsigned
      // operands. Counts past the operand's bit length are answered directly, since shifting that
      // far only ever yields 0 or -1 and `toInt` would be unsafe for very large counts.
      case (Some(a), Some(b)) if b >= 0 =>
        if (b > a.bitLength + 1) folded(e, if (a < 0) BigInt(-1) else BigInt(0))
        else folded(e, a >> b.toInt)
      case _ => None
    }

    case _ => None
  }

  private def literal(e: in.Expr): Option[BigInt] = e match {
    case in.IntLit(v, _, _) => Some(v)
    case _ => None
  }

  private def binary(e: in.Expr, l: in.Expr, r: in.Expr)(op: (BigInt, BigInt) => BigInt): Option[in.Expr] =
    for { a <- literal(l); b <- literal(r); res <- folded(e, op(a, b)) } yield res

  /** `^x`, which Go defines as `-x - 1` for signed operands and `max - x` for unsigned ones. */
  private def complement(e: in.Expr, a: BigInt): Option[in.Expr] = e.typ match {
    case in.IntT(_, k: BoundedIntegerKind) if !k.isInstanceOf[Signed] => folded(e, (~a).mod(k.upper + 1))
    case _ => folded(e, ~a)
  }

  /**
    * The literal `value` in `e`'s integer kind, or `None` if `e` must be left alone.
    *
    * A value outside a bounded kind's range is deliberately not folded. Such a constant is rejected
    * by the type-checker where Go rejects it too, and leaving the expression intact keeps the
    * wrap-around cases (which only arise for non-constant operands) with the encoding that models
    * them.
    */
  private def folded(e: in.Expr, value: BigInt): Option[in.Expr] = e.typ match {
    case in.IntT(_, k: BoundedIntegerKind) if value < k.lower || value > k.upper => None
    case in.IntT(_, k) => Some(in.IntLit(value, k)(e.info))
    case _ => None
  }
}