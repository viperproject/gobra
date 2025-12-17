package viper.gobra.backend

import viper.silicon.dependencyAnalysis.{DependencyAnalysisNode, DependencyAnalysisUserTool, DependencyGraphInterpreter}
import viper.silicon.interfaces.Failure
import viper.silver.ast

class GobraDependencyAnalysisUserTool(fullGraphInterpreter: GobraDependencyGraphInterpreter, verificationErrors: List[Failure])
  extends DependencyAnalysisUserTool(fullGraphInterpreter, Seq.empty, ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq.empty)(), verificationErrors) {

//  protected override def getSourceInfoString(nodes: Set[DependencyAnalysisNode]): String = {
//
//
//    gobraNodes.map(gNode => allNodes.filter { case (pos, _) => pos match {
//      case p: AbstractSourcePosition => gNode.start <= p.start && (p.end.isEmpty || gNode.end.isEmpty || p.end.get <= gNode.end.get)
//      case _ => false
//    }})
//    nodes.groupBy(node => node.sourceInfo.getTopLevelSource.toString).map{case (_, nodes) => nodes.head.sourceInfo.getTopLevelSource}.toList.sortBy(_.getLineNumber).mkString("\n\t")
//  }

}
