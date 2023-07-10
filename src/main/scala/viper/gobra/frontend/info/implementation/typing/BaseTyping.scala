// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.{Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.base.TypingComponents
import viper.gobra.util.Safety


trait BaseTyping extends TypingComponents { this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.attribution.Attribution
  import viper.gobra.util.{Memoization, Validity}

  def allChildren(n: PNode): Vector[PNode] = {
    tree.child(n).flatMap(m => m +: allChildren(m))
  }

  private[typing] def children[T <: PNode](n: T): Vector[PNode] =
    tree.child(n)

  private[typing] def childrenWellDefined(n: PNode): Boolean = {

    def selfWellDefined(n: PNode): Boolean = {
      if (isNotADependency(n)) true
      else (n match {
        case s: PStatement => wellDefStmt.valid(s)
        case n: PExpressionAndType => wellDefExprAndType.valid(n)
        case e: PExpression => wellDefExpr.valid(e)
        case t: PType => wellDefType.valid(t)
        case i: PIdnNode => wellDefID.valid(i)
        case l: PLabelNode => wellDefLabel.valid(l)
        case o: PMisc => wellDefMisc.valid(o)
        case s: PSpecification => wellDefSpec.valid(s)
        case m: PNode => childrenWellDefined(m)
      }) && wellDefModifiers(n).valid
    }

    /**
      * Returns true iff well-definedness of node is not required for any ancestor node
      */
    def isNotADependency(n: PNode): Boolean = n match {
      case _: PDomainAxiom => true
      case _: PMethodSig => true
      case _: PMPredicateSig => true
      // skip well-definedness checks for defined identifiers. This enables the parent node, e.g. the declaration
      // statement, to perform the necessary checks as the parent is not skipped due to an unsafe message from the
      // identifier well-definedness check. See issue #185
      case _: PIdnDef => true
      case i: PIdnUnk if isDef(i) => true
      case _ => false
    }

    children(n) forall selfWellDefined
  }

  private[typing] def isWellDefined(n: PNode): Boolean = (n match {
    case s: PStatement => wellDefStmt.valid(s)
    case n: PExpressionAndType => wellDefExprAndType.valid(n)
    case e: PExpression => wellDefExpr.valid(e)
    case t: PType => wellDefType.valid(t)
    case i: PIdnNode => wellDefID.valid(i)
    case l: PLabelNode => wellDefLabel.valid(l)
    case o: PMisc => wellDefMisc.valid(o)
    case s: PSpecification => wellDefSpec.valid(s)
    case m: PNode => childrenWellDefined(m)
  }) && wellDefModifiers(n).valid

  private[typing] def createWellDef[T <: PNode](check: T => Messages): WellDefinedness[T] =
    new Attribution with WellDefinedness[T] with Safety[T, ValidityMessages] with Memoization[T, ValidityMessages] {

      override def safe(n: T): Boolean = childrenWellDefined(n)

      override def unsafe: ValidityMessages = UnsafeForwardMessage

      override def compute(n: T): ValidityMessages = LocalMessages(check(n))
    }

  private[typing] def createWellDefWithValidityMessages[T <: PNode](check: T => ValidityMessages): WellDefinedness[T] =
    new Attribution with WellDefinedness[T] with Safety[T, ValidityMessages] with Memoization[T, ValidityMessages] {

      override def safe(n: T): Boolean = childrenWellDefined(n)

      override def unsafe: ValidityMessages = UnsafeForwardMessage

      override def compute(n: T): ValidityMessages = check(n)
    }

  trait Typing[-A] extends Safety[A, Type] with Validity[A, Type] {

    override def unsafe: Type = UnknownType

    override def invalid(ret: Type): Boolean = ret == UnknownType
  }

  private[typing] def createTyping[T <: AnyRef](inference: T => Type)(implicit wellDef: WellDefinedness[T]): Typing[T] =
    new Attribution with Typing[T] with Memoization[T, Type] {

      override def safe(n: T): Boolean = wellDef.valid(n)

      override def compute(n: T): Type = inference(n)
    }

  private[typing] def createWellDefInference[X <: AnyRef, Z](wellDef: X => Boolean)(inference: X => Z): X => Option[Z] =
    new Attribution with Safety[X, Option[Z]] with Memoization[X, Option[Z]] {

      override def safe(n: X): Boolean = wellDef(n)

      override def unsafe: Option[Z] = None

      override def compute(n: X): Option[Z] = Some(inference(n))
    }
}
