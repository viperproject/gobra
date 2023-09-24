// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.base.Type.{ImportT, PredT, FunctionT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

trait AmbiguityResolution { this: TypeInfoImpl =>

  override def isDef(n: PIdnUnk): Boolean = !isDefinedAt(n,n)

  def exprOrType(n: PExpressionOrType): Either[PExpression, PType] = {
    n match {
      // Ambiguous nodes
      case n: PNamedOperand =>
        if (pointsToType(n.id)) Right(n) else Left(n)

      case n: PDeref =>
        if (exprOrType(n.base).isLeft) Left(n) else Right(n)

      case n: PDot =>
        exprOrType(n.base)
          .fold(
            _ => Left(n),
            symbType(_) match { // check if base is a package qualifier and id points to a type
              case _: ImportT if pointsToType(n.id) => Right(n)
              case _ => Left(n)
            })

      case n: PIndexedExp =>
        // expression indexes have an expression base and type instantiations have a type base
        if (exprOrType(n.base).isLeft) Left(n) else Right(n)

      // Otherwise just expression or type
      case n: PExpression => Left(n)
      case n: PType => Right(n)
    }
  }

  def asExpr(n: PExpressionOrType): Option[PExpression] = exprOrType(n).left.toOption
  def asType(n: PExpressionOrType): Option[PType] = exprOrType(n).toOption
  def asExprList(n: Vector[PExpressionOrType]): Option[Vector[PExpression]] = {
    val asExprs = n.map(asExpr)
    if (asExprs.forall(_.nonEmpty)) Some(asExprs.map(_.get)) else None
  }
  def asTypeList(n: Vector[PExpressionOrType]): Option[Vector[PType]] = {
    val asTypes = n.map(asType)
    if (asTypes.forall(_.nonEmpty)) Some(asTypes.map(_.get)) else None
  }

  def resolve(n: PExpressionOrType): Option[ap.Pattern] = n match {

    case n: PNamedOperand =>
      entity(n.id) match {
        case s: st.NamedType => Some(ap.NamedType(n.id, s))
        case s: st.TypeParameter => Some(ap.TypeArgument(n.id, s))
        case s: st.Variable => s match {
          case g: st.GlobalVariable => Some(ap.GlobalVariable(n.id, g))
          case _ => Some(ap.LocalVariable(n.id, s))
        }
        case s: st.Constant => Some(ap.Constant(n.id, s))
        case s: st.Function => Some(ap.Function(n.id, s))
        case s: st.Closure => Some(ap.Closure(n.id, s))
        case s: st.FPredicate => Some(ap.Predicate(n.id, s))
        case s: st.DomainFunction => Some(ap.DomainFunction(n.id, s))
        // built-in members
        case s: st.BuiltInFunction => Some(ap.BuiltInFunction(n.id, s))
        case s: st.BuiltInFPredicate => Some(ap.BuiltInPredicate(n.id, s))
        case s : st.BuiltInType => Some(ap.BuiltInType(n.id, s))
        // interface method and predicate when referenced inside of the interface definition
        // (otherwise a receiver would be present)
        case s: st.MethodSpec => Some(ap.ImplicitlyReceivedInterfaceMethod(n.id, s))
        case s: st.MPredicateSpec => Some(ap.ImplicitlyReceivedInterfacePredicate(n.id, s))
        case _ => None
      }

    case n: PDeref =>
      exprOrType(n.base) match {
        case Left(expr) => Some(ap.Deref(expr))
        case Right(typ) => Some(ap.PointerType(typ))
      }

    case n: PDot =>
      (exprOrType(n.base), tryDotLookup(n.base, n.id)) match {

        case (Left(base), Some((s: st.StructMember, path))) => Some(ap.FieldSelection(base, n.id, path, s))
        case (Left(base), Some((s: st.Method, path))) => Some(ap.ReceivedMethod(base, n.id, path, s))
        case (Left(base), Some((s: st.MPredicate, path))) => Some(ap.ReceivedPredicate(base, n.id, path, s))
        case (Left(base), Some((s: st.AdtMember, _))) => Some(ap.AdtField(base, n.id, s))

        case (Right(base), Some((s: st.Method, path))) => Some(ap.MethodExpr(base, n.id, path, s))
        case (Right(base), Some((s: st.MPredicate, path))) => Some(ap.PredicateExpr(base, n.id, path, s))

        // imported members
        case (Right(_), Some((s: st.ActualTypeEntity, _))) => Some(ap.NamedType(n.id, s))
        case (Right(_), Some((s: st.Constant, _))) => Some(ap.Constant(n.id, s))
        case (Right(_), Some((s: st.GlobalVariable, _))) => Some(ap.GlobalVariable(n.id, s))
        case (Right(_), Some((s: st.Function, _))) => Some(ap.Function(n.id, s))
        case (Right(_), Some((s: st.FPredicate, _))) => Some(ap.Predicate(n.id, s))
        case (Right(_), Some((s: st.DomainFunction, _))) => Some(ap.DomainFunction(n.id, s))
        case (Right(_), Some((s: st.AdtClause, _))) => Some(ap.AdtClause(n.id, s))

        // built-in members
        case (Left(base), Some((s: st.BuiltInMethod, path))) => Some(ap.BuiltInReceivedMethod(base, n.id, path, s))
        case (Left(base), Some((s: st.BuiltInMPredicate, path))) => Some(ap.BuiltInReceivedPredicate(base, n.id, path, s))
        case (Right(base), Some((s: st.BuiltInMethod, path))) =>   Some(ap.BuiltInMethodExpr(base, n.id, path, s))
        case (Right(base), Some((s: st.BuiltInMPredicate, path))) =>   Some(ap.BuiltInPredicateExpr(base, n.id, path, s))

        case _ => None
      }

    case n: PInvoke =>
      exprOrType(n.base) match {
        case Right(t) if n.args.length == 1 => Some(ap.Conversion(t, n.args.head))
        case Left(e) =>
          resolve(e) match {
            case Some(ap.BuiltInType(_, st.BuiltInType(tag, _, _))) if n.args.length == 1 =>
              Some(ap.Conversion(tag.node , n.args.head))
            case _ if n.spec.nonEmpty && exprType(e).isInstanceOf[FunctionT] => Some(ap.ClosureCall(e, n.args, n.spec.get))
            case Some(p: ap.FunctionKind) => Some(ap.FunctionCall(p, n.args))
            case Some(p: ap.PredicateKind) => Some(ap.PredicateCall(p, n.args))
            case _ if exprType(e).isInstanceOf[PredT] => Some(ap.PredExprInstance(e, n.args, exprType(e).asInstanceOf[PredT]))
            case _ => None
          }
        case _ => violation(s"unexpected case reached: type conversion with arguments ${n.args}, expected single argument instead")
      }

    case n: PIndexedExp => resolve(n.base) match {
      case Some(f: ap.Parameterizable) =>
        asTypeList(n.index) match {
          case Some(typeArgs) => Some(f.withTypeArgs(typeArgs))
          case _ => None
        }
      case _ if n.index.length == 1 => {
        exprOrType(n.index.head) match {
          case Left(expression) => Some(ap.IndexedExp(n.base, expression))
          case _ => None
        }
      }
      case _ => None // unknow pattern
    }

    case b: PBlankIdentifier => Some(ap.BlankIdentifier(b))

      // unknown pattern
    case _ => None
  }
}
