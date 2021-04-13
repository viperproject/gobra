// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.frontend

import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.resolution.MemberPath

object AstPattern {

  sealed trait Pattern

  sealed trait Symbolic {
    def symb: st.Regular
  }

  sealed trait Type extends Pattern

  case class NamedType(id: PIdnUse, symb: st.ActualTypeEntity) extends Type with Symbolic
  case class PointerType(base: PType) extends Type

  sealed trait Expr extends Pattern

  case class Constant(id: PIdnUse, symb: st.Constant) extends Expr with Symbolic
  case class LocalVariable(id: PIdnUse, symb: st.Variable) extends Expr with Symbolic // In the future: with FunctionKind
  case class Deref(base: PExpression) extends Expr
  case class FieldSelection(base: PExpression, id: PIdnUse, path: Vector[MemberPath], symb: st.StructMember) extends Expr with Symbolic
  case class Conversion(typ: PType, arg: Vector[PExpression]) extends Expr
  case class FunctionCall(callee: FunctionKind, args: Vector[PExpression]) extends Expr
  case class IndexedExp(base : PExpression, index : PExpression) extends Expr
  case class BlankIdentifier(decl: PBlankIdentifier) extends Expr

  sealed trait FunctionKind extends Expr {
    def id: PIdnUse
  }

  case class Function(id: PIdnUse, symb: st.Function) extends FunctionKind with Symbolic
  case class DomainFunction(id: PIdnUse, symb: st.DomainFunction) extends FunctionKind with Symbolic
  case class ReceivedMethod(recv: PExpression, id: PIdnUse, path: Vector[MemberPath], symb: st.Method) extends FunctionKind with Symbolic
  case class ImplicitlyReceivedInterfaceMethod(id: PIdnUse, symb: st.MethodSpec) extends FunctionKind with Symbolic // for method references withing an interface definition
  case class MethodExpr(typ: PType, id: PIdnUse, path: Vector[MemberPath], symb: st.Method) extends FunctionKind with Symbolic

  sealed trait BuiltInFunctionKind extends FunctionKind with Symbolic {
    def symb: st.BuiltInActualEntity
  }
  sealed trait BuiltInMethodKind extends BuiltInFunctionKind {
    def path: Vector[MemberPath]
    def symb: st.BuiltInMethod
  }

  case class BuiltInFunction(id: PIdnUse, symb: st.BuiltInFunction) extends BuiltInFunctionKind with Symbolic
  case class BuiltInReceivedMethod(recv: PExpression, id: PIdnUse, path: Vector[MemberPath], symb: st.BuiltInMethod) extends BuiltInMethodKind
  case class BuiltInMethodExpr(typ: PType, id: PIdnUse, path: Vector[MemberPath], symb: st.BuiltInMethod) extends BuiltInMethodKind

  sealed trait Assertion extends Pattern

  case class PredicateCall(predicate: PredicateKind, args: Vector[PExpression]) extends Assertion

  sealed trait PredicateKind extends Assertion

  case class Predicate(id: PIdnUse, symb: st.FPredicate) extends PredicateKind with Symbolic
  case class ReceivedPredicate(recv: PExpression, id: PIdnUse, path: Vector[MemberPath], symb: st.MPredicate) extends PredicateKind with Symbolic
  case class ImplicitlyReceivedInterfacePredicate(id: PIdnUse, symb: st.MPredicateSpec) extends PredicateKind with Symbolic // for predicate references within an interface definition
  case class PredicateExpr(typ: PType, id: PIdnUse, path: Vector[MemberPath], symb: st.MPredicate) extends PredicateKind with Symbolic
  case class PredExprInstance(base: PExpression, args: Vector[PExpression]) extends PredicateKind

  sealed trait BuiltInPredicateKind extends PredicateKind

  case class BuiltInPredicate(id: PIdnUse, symb: st.BuiltInFPredicate) extends BuiltInPredicateKind with Symbolic
  case class BuiltInReceivedPredicate(recv: PExpression, id: PIdnUse, path: Vector[MemberPath], symb: st.BuiltInMPredicate) extends BuiltInPredicateKind with Symbolic
  case class BuiltInPredicateExpr(typ: PType, id: PIdnUse, path: Vector[MemberPath], symb: st.BuiltInMPredicate) extends BuiltInPredicateKind with Symbolic
}
