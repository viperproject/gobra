package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend.{PClosureDecl, PFunctionLit, PIdnNode, PIdnUnk, PIdnUse, PNode}
import viper.gobra.frontend.info.base.SymbolTable.Variable
import viper.gobra.frontend.info.implementation.TypeInfoImpl

import scala.collection.mutable

trait CapturedVar { this: TypeInfoImpl =>
  override def capturedVars(decl: PClosureDecl): Vector[PIdnNode] = capturedVariables(decl)

  lazy val capturedVariables: PClosureDecl => Vector[PIdnNode] = decl => allCapturedVariables.getOrElse(Unique(decl), Set[PIdnNode]()).toVector

  private lazy val allCapturedVariables: Map[Unique, Set[PIdnNode]] = {
    def funcLitCapturesVar(l: PFunctionLit, id: PIdnNode): Boolean = tryLookupAt(id, l).fold(false)(_ eq entity(id))
    def getCapturingFunctionLits(id: PIdnNode, n: PNode): Seq[PFunctionLit] = {
      val enclosingLit = enclosingFunctionLit(tree.parent(n).head)
      if (enclosingLit.nonEmpty && enclosingLit.get != n && funcLitCapturesVar(enclosingLit.get, id)) {
        val s = getCapturingFunctionLits(id, enclosingLit.get) :+ enclosingLit.get
        s
      } else Seq[PFunctionLit]()
    }

    val result = mutable.Map[Unique, Set[PIdnNode]]()
    def addPairToResult(decl: PClosureDecl, id: PIdnNode): Unit = {
      result += (Unique(decl) -> (result.getOrElse(Unique(decl), Set[PIdnNode]()) + id))
    }

    tree.nodes.foreach({
      case id: PIdnUse if entity(id).isInstanceOf[Variable] => getCapturingFunctionLits(id, id).foreach(lit => addPairToResult(lit.decl.decl, id))
      case id: PIdnUnk if !isDef(id) && entity(id).isInstanceOf[Variable] => getCapturingFunctionLits(id, id).foreach(lit => addPairToResult(lit.decl.decl, id))
      case _ =>
    })

    result.toMap
  }

  // Different closure declarations must be considered separately even if they are structurally identical
  private case class Unique(n: PClosureDecl) {
    override def equals(obj: Any): Boolean = obj match {
      case Unique(o) => o eq n
      case _ => false
    }
  }

}
