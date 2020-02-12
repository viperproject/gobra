package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{MPredicate, Method, Regular, StructMember}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.resolution.MemberPath

trait TypeInfo {

  def typ(expr: PExpression): Type

  def typ(misc: PMisc): Type

  def typ(typ: PType): Type

  def typ(id: PIdnNode): Type

  def scope(n: PIdnNode): PScope

  def addressableVar(id: PIdnNode): Boolean

  def tree: Tree[PNode, PProgram]

  def regular(n: PIdnNode): Regular

  def variables(s: PScope): Vector[PIdnNode]

  def resolve(n: PExpressionOrType): Option[AstPattern.Pattern]
  def exprOrType(n: PExpressionOrType): Either[PExpression, PType]

}
