package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Regular, TypeMember}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.resolution.MemberPath

trait TypeInfo {

  def typ(expr: PExpression): Type

  def typ(typ: PType): Type

  def typ(id: PIdnNode): Type

  def scope(n: PIdnNode): PScope

  def addressed(id: PIdnNode): Boolean

  def addressed(lit: PStructType): Boolean

  def tree: Tree[PNode, PProgram]

  def regular(n: PIdnNode): Regular

  def variables(s: PScope): Vector[PIdnNode]

  def memberLookup(t: Type, id: PIdnUse): (TypeMember, Vector[MemberPath])

  def selectionLookup(t: Type, id: PIdnUse): (TypeMember, Vector[MemberPath])

}
