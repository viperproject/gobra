package viper.gobra.backend

import viper.silicon.dependencyAnalysis.{DependencyAnalysisNode, DependencyGraphInterpreter, ReadOnlyDependencyGraph}
import viper.silver.ast
import viper.silver.ast.Program
import viper.silver.dependencyAnalysis.AbstractDependencyGraphInterpreter



class GobraDependencyGraphInterpreter(name: String, dependencyGraph: ReadOnlyDependencyGraph, member: Option[ast.Member] = None) extends DependencyGraphInterpreter(name, dependencyGraph, member) {

  override def getPrunedProgram(crucialNodes: Set[DependencyAnalysisNode], program: Program): (Program, Double) = {
    throw new Exception("Pruning of Gobra programs is not yet supported.")
  }

}

object GobraDependencyGraphInterpreter {
  def convertFromDependencyGraphInterpreter(interpreter: AbstractDependencyGraphInterpreter): GobraDependencyGraphInterpreter = {
    interpreter match {
      case interpreter: DependencyGraphInterpreter => new GobraDependencyGraphInterpreter(interpreter.getName, interpreter.getGraph, interpreter.getMember)
      case _ => throw new Exception(s"Unknown dependency graph interpreter $interpreter")
    }
  }
}
