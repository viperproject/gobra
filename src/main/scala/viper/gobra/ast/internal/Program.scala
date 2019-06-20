/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.internal

case class Program(
                  types: Set[TopType],
                  variables: Set[GlobalVarDecl],
                  constants: Set[GlobalConst],
                  methods: Set[Method],
                  functions: Set[Function]
                  )(var src: Source) extends Node {

}

sealed trait GlobalVarDecl extends Node

//case class SingleGlobalVarDecl(left: GlobalVar, right: Expr)

//case class MultiGlobalVarDecl(lefts: Vector[GlobalVar], right: Expr)

sealed trait GlobalConst extends Node



case class Method(
                 receiver: Parameter,
                 name: String,
                 args: Vector[Parameter],
                 results: Vector[LocalVar],
                 pres: Vector[Assertion],
                 posts: Vector[Assertion],
                 body: Option[Block]
                 )(var src: Source) extends Node

case class Function(
                     name: String,
                     args: Vector[Parameter],
                     results: Vector[LocalVar],
                     pres: Vector[Assertion],
                     posts: Vector[Assertion],
                     body: Option[Block]
                   )(var src: Source) extends Node


sealed trait Stmt extends Node

case class Block(
                variables: Vector[LocalVar],
                stmts: Vector[Stmt]
                )(var src: Source) extends Stmt

case class Seq(stmts: Vector[Stmt])(var src: Source) extends Stmt

sealed trait Assignment extends Stmt

case class SingleAss(left: Assignee, right: Expr)(var src: Source) extends Assignment

case class MultiAss(lefts: Vector[Assignee], right: Expr)(var src: Source) extends Assignment


sealed trait Assignee

object Assignee {
  case class Var(v: BodyVar) extends Assignee
  // case class Field(f: FieldAccess) extends Assignee
  case class Pointer(e: Deref) extends Assignee
  // TODO: Index
}

case class Return()(var src: Source) extends Stmt


case class Assert(ass: Assertion)(var src: Source) extends Stmt
case class Assume(ass: Assertion)(var src: Source) extends Stmt
case class Inhale(ass: Assertion)(var src: Source) extends Stmt
case class Exhale(ass: Assertion)(var src: Source) extends Stmt


sealed trait Assertion extends Node

case class Star(left: Assertion, right: Assertion)(var src: Source) extends Assertion

case class ExprAssertion(exp: Expr)(var src: Source) extends Assertion

case class Implication(left: Expr, right: Assertion)(var src: Source) extends Assertion

case class Access(e: Accessible)(var src: Source) extends Assertion

sealed trait Accessible

object Accessible {
  case class Ref(der: Deref) extends Accessible
}




sealed trait Expr extends Node with Typed

// case class FieldAccess() extends Expr

case class DfltVal(typ: Type)(var src: Source) extends Expr

case class Deref(exp: Expr, typ: Type)(var src: Source) extends Expr {
  require(exp.typ.isInstanceOf[PointerT])
}

case class Ref(ref: Addressable, typ: PointerT)(var src: Source) extends Expr

sealed trait Addressable

object Addressable {
  case class Var(v: LocalVar.Ref) extends Addressable
  // TODO: Field, Global
}



sealed trait Lit extends Expr

case class IntLit(v: BigInt)(var src: Source) extends Lit {
  override def typ: Type = IntT
}

case class BoolLit(b: Boolean)(var src: Source) extends Lit {
  override def typ: Type = BoolT
}


sealed trait Var extends Expr {
  def id: String
}

case class Parameter(id: String, typ: Type)(var src: Source) extends Var

sealed trait BodyVar extends Var

sealed trait LocalVar extends BodyVar {
  def unapply(arg: LocalVar): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object LocalVar {
  case class Ref(id: String, typ: Type)(var src: Source) extends LocalVar
  case class Val(id: String, typ: Type)(var src: Source) extends LocalVar
}

//sealed trait GlobalVar extends Var {
//  def unapply(arg: LocalVar): Option[(String, Type)] =
//    Some((arg.id, arg.typ))
//}

//object GlobalVar {
//  case class Var(id: String, typ: Type)(val src: Source) extends LocalVar
//  case class Val(id: String, typ: Type)(val src: Source) extends LocalVar
//}


sealed trait Typed {
  def typ: Type
}

sealed trait TopType extends Type


sealed trait Type

case object BoolT extends TopType

case object IntT extends TopType

case object VoidT extends TopType

case object FracT extends TopType

case class DefinedT(name: String, right: Type) extends TopType

case class PointerT(t: Type) extends TopType



