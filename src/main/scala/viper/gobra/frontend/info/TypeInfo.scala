package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.Type.Type

trait TypeInfo {

  def typ(expr: PExpression): Type

  def typ(misc: PMisc): Type

  def typ(typ: PType): Type

  def typ(id: PIdnNode): Type

  def scope(n: PIdnNode): PScope

  def codeRoot(n: PNode): PScope

  def addressableVar(id: PIdnNode): Boolean

  def tree: Tree[PNode, PPackage]

  def regular(n: PIdnNode): Regular

  def variables(s: PScope): Vector[PIdnNode]

  def labeledVarUses(l: PLabelDef): Vector[PIdnUse]

  def resolve(n: PExpressionOrType): Option[AstPattern.Pattern]
  def exprOrType(n: PExpressionOrType): Either[PExpression, PType]

}
