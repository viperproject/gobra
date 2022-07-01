package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend.{PClosureDecl, PFunctionLit, PIdnNode, PIdnUnk, PIdnUse, PNode}
import viper.gobra.frontend.info.base.SymbolTable.Variable
import viper.gobra.frontend.info.implementation.TypeInfoImpl

import scala.collection.mutable

trait CapturingVars { this: TypeInfoImpl =>
  def capturedVars(decl: PClosureDecl): Vector[PIdnNode] = capturedVariables(decl)

  private lazy val capturedVariables: PClosureDecl => Vector[PIdnNode] =
    attr[PClosureDecl, Vector[PIdnNode]] { decl =>
      val lit = enclosingFunctionLit(decl).get

      def isCaptured(id: PIdnNode): Boolean = id match {
        case _: PIdnUse | _: PIdnUnk => entity(id) match {
          case v:Variable => tryLookupAt(id, lit).fold(false)(_ eq v)
          case _ => false
        }
        case _ => false
      }

      val capturedIds: mutable.Set[PIdnNode] = mutable.Set.empty
      def collectCapturedDescendents(n: PNode): Unit = n match {
        case id: PIdnNode => if (isCaptured(id) && !capturedIds.contains(id)) capturedIds += id
        case _ => tree.child(n) foreach collectCapturedDescendents
      }
      collectCapturedDescendents(decl)
      capturedIds.toVector
    }
}
