/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.internal

/**
  * When adding a new node:
  * - extend @see [[viper.gobra.ast.internal.utility.Nodes.subnodes]]
  * - extend @see [[viper.gobra.ast.internal.utility.GobraStrategy.gobraDuplicator]]
  * - extend @see [[DefaultPrettyPrinter.show]]
  * - extend desugar
  * - extend translator
  */

import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.Parser




case class Program(
                    types: Vector[TopType],
                    variables: Vector[GlobalVarDecl],
                    constants: Vector[GlobalConst],
                    methods: Vector[Method],
                    functions: Vector[Function]
                  )(val info: Source.Parser.Info) extends Node {

}

sealed trait GlobalVarDecl extends Node

//case class SingleGlobalVarDecl(left: GlobalVar, right: Expr)

//case class MultiGlobalVarDecl(lefts: Vector[GlobalVar], right: Expr)

sealed trait GlobalConst extends Node



case class Method(
                 receiver: Parameter,
                 name: String,
                 args: Vector[Parameter],
                 results: Vector[LocalVar.Val],
                 pres: Vector[Assertion],
                 posts: Vector[Assertion],
                 body: Option[Block]
                 )(val info: Source.Parser.Info) extends Node

case class Function(
                     name: String,
                     args: Vector[Parameter],
                     results: Vector[LocalVar.Val],
                     pres: Vector[Assertion],
                     posts: Vector[Assertion],
                     body: Option[Block]
                   )(val info: Source.Parser.Info) extends Node


sealed trait Stmt extends Node

case class Block(
                variables: Vector[LocalVar],
                stmts: Vector[Stmt]
                )(val info: Source.Parser.Info) extends Stmt

case class Seqn(stmts: Vector[Stmt])(val info: Source.Parser.Info) extends Stmt

sealed trait Assignment extends Stmt

case class SingleAss(left: Assignee, right: Expr)(val info: Source.Parser.Info) extends Assignment

case class MultiAss(lefts: Vector[Assignee], right: Expr)(val info: Source.Parser.Info) extends Assignment


sealed trait Assignee extends Node

object Assignee {
  case class Var(v: BodyVar) extends Assignee {
    override def info: Parser.Info = v.info
  }
  // case class Field(f: FieldAccess) extends Assignee
  case class Pointer(e: Deref) extends Assignee {
    override def info: Parser.Info = e.info
  }
  // TODO: Index
}

case class Return()(val info: Source.Parser.Info) extends Stmt


case class Assert(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Assume(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Inhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Exhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt


sealed trait Assertion extends Node

case class Star(left: Assertion, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class ExprAssertion(exp: Expr)(val info: Source.Parser.Info) extends Assertion

case class Implication(left: Expr, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Access(e: Accessible)(val info: Source.Parser.Info) extends Assertion

sealed trait Accessible extends Node

object Accessible {
  case class Ref(der: Deref) extends Accessible {
    override def info: Parser.Info = der.info
  }
}




sealed trait Expr extends Node with Typed

// case class FieldAccess() extends Expr

case class DfltVal(typ: Type)(val info: Source.Parser.Info) extends Expr

case class Deref(exp: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr {
  require(exp.typ.isInstanceOf[PointerT])
}

case class Ref(ref: Addressable, typ: PointerT)(val info: Source.Parser.Info) extends Expr

sealed trait Addressable extends Node

object Addressable {
  case class Var(v: LocalVar.Ref) extends Addressable {
    override def info: Parser.Info = v.info
  }
  // TODO: Field, Global
}

sealed trait BinaryExpr extends Expr {
  def left: Expr
  def right: Expr
}

case class EqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr {
  override def typ: Type = BoolT
}





sealed trait Lit extends Expr

case class IntLit(v: BigInt)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = IntT
}

case class BoolLit(b: Boolean)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = BoolT
}


sealed trait Var extends Expr {
  def id: String
}

case class Parameter(id: String, typ: Type)(val info: Source.Parser.Info) extends Var

sealed trait BodyVar extends Var

sealed trait LocalVar extends BodyVar {
  def unapply(arg: LocalVar): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object LocalVar {
  case class Ref(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar
  case class Val(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar
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

case object PermissionT extends TopType

case class DefinedT(name: String, right: Type) extends TopType

case class PointerT(t: Type) extends TopType



