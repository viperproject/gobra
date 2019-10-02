package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{MPredicate, Method, Regular, StructMember}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.resolution.MemberPath

trait TypeInfo {

  def resolver: Rewriter.AmbiguityResolver

  def typ(expr: PExpression): Type

  def typ(misc: PMisc): Type

  def typ(typ: PType): Type

  def typ(id: PIdnNode): Type

  def scope(n: PIdnNode): PScope

  def addressed(id: PIdnNode): Boolean

  def tree: Tree[PNode, PProgram]

  def regular(n: PIdnNode): Regular

  def variables(s: PScope): Vector[PIdnNode]

  def fieldLookup(t: Type, id: PIdnUse): (StructMember, Vector[MemberPath])

  def methodLookup(e: PExpression, id: PIdnUse): (Method, Vector[MemberPath])
  def methodLookup(e: PIdnNode, id: PIdnUse): (Method, Vector[MemberPath])
  def methodLookup(e: Type, id: PIdnUse): (Method, Vector[MemberPath])
  def predicateLookup(e: PExpression, id: PIdnUse): (MPredicate, Vector[MemberPath])
  def predicateLookup(e: PIdnNode, id: PIdnUse): (MPredicate, Vector[MemberPath])
  def predicateLookup(e: Type, id: PIdnUse): (MPredicate, Vector[MemberPath])
}
