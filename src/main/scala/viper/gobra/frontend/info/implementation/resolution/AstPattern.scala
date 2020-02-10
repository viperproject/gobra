package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend.{PExpression, PIdnUse, PType, PTypeOrExpr}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import org.bitbucket.inkytonik.kiama.util.Entity

sealed trait AstPattern

object AstPattern {
  sealed trait EntityLike {
    def entity: Entity
  }
  sealed trait Type extends AstPattern
  case class NamedType(id: PIdnUse, entity: st.TypeEntity) extends Type with EntityLike
  case class PointerType(t: PType) extends Type

  sealed trait Expression extends AstPattern
  sealed trait CallableExpression extends EntityLike
  case class Selection(base: PExpression, entity: st.Field, path: Vector[MemberPath]) extends Expression with EntityLike
  case class MethodExpression(t: PType, entity: st.MethodLike) extends Expression with EntityLike with CallableExpression
  case class ReceivedMethod(entity: st.Method, path: Vector[MemberPath]) extends Expression with EntityLike with CallableExpression
  case class Variable(id: PIdnUse, entity: st.Variable) extends Expression with EntityLike with CallableExpression
  case class Function(id: PIdnUse, entity: st.Function, pkg: Option[PIdnUse]) extends Expression with EntityLike with CallableExpression
  case class Dereference(e: PExpression) extends Expression
  case class Call(base: CallableExpression, args: Vector[PExpression]) extends Expression
  case class Conversion(t: PType, args: Vector[PExpression]) extends Expression

  sealed trait Assertion extends AstPattern
  sealed trait CallableAssertion
  case class ReceivedPredicate(entity: st.MPredicate, path: Vector[MemberPath]) extends Assertion with EntityLike with CallableAssertion
  case class Predicate(id: PIdnUse, entity: st.Predicate) extends Assertion with EntityLike with CallableAssertion
  case class PredicateAccess(base: CallableAssertion, args: Vector[PExpression]) extends Assertion with CallableAssertion
}
