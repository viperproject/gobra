package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend.{PClosureDecl, PClosureNamedDecl, PFunctionLit, PIdnNode, PIdnUnk, PIdnUse, PNode, PPackage}
import viper.gobra.frontend.info.base.SymbolTable.Variable
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.silver.ast.Int.meta
import viper.silver.ast.SourcePosition

import scala.collection.mutable

trait CapturedVar { this: TypeInfoImpl =>
  override def capturedVars(decl: PClosureDecl): Vector[PIdnNode] = capturedVariables(decl)

  lazy val capturedVariables: PClosureDecl => Vector[PIdnNode] = decl => allCapturedVariables.getOrElse((decl, getPos(decl)), Set[PIdnNode]()).toVector

  private lazy val allCapturedVariables: Map[(PClosureDecl, SourcePosition), Set[PIdnNode]] = {
    def funcLitCapturesVar(l: PFunctionLit, id: PIdnNode): Boolean = tryLookupAt(id, l).fold(false)(_ eq entity(id))
    def getCapturingFunctionLits(id: PIdnNode, n: PNode): Seq[PFunctionLit] = {
      val enclosingLit = enclosingFunctionLit(tree.parent(n).head)
      if (enclosingLit.nonEmpty && enclosingLit.get != n && funcLitCapturesVar(enclosingLit.get, id)) {
        val s = getCapturingFunctionLits(id, enclosingLit.get) :+ enclosingLit.get
        s
      } else Seq[PFunctionLit]()
    }

    val result = mutable.Map[(PClosureDecl, SourcePosition), Set[PIdnNode]]()
    def addPairToResult(decl: PClosureDecl, id: PIdnNode): Unit = {
      val pos = getPos(decl)
      result += ((decl, pos) -> (result.getOrElse((decl, pos), Set[PIdnNode]()) + id))
    }

    tree.nodes.foreach({
      case id: PIdnUse if entity(id).isInstanceOf[Variable] => getCapturingFunctionLits(id, id).foreach(lit => addPairToResult(lit.decl.decl, id))
      case id: PIdnUnk if !isDef(id) && entity(id).isInstanceOf[Variable] => getCapturingFunctionLits(id, id).foreach(lit => addPairToResult(lit.decl.decl, id))
      case _ =>
    })

    result.toMap
  }

  private def getPos(n: PClosureDecl): SourcePosition = {
    val enclosingFuncLit = enclosingFunctionLit(n).get
    val pom = tree.originalRoot.positions
    val start = pom.positions.getStart(enclosingFuncLit).get
    val finish = pom.positions.getFinish(enclosingFuncLit).get
    pom.translate(start, finish)
  }
}
