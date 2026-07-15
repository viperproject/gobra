package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend.PNode
import viper.gobra.ast.internal.Type
import viper.silver.ast.{NoPosition, Position}
import viper.silver.dependencyAnalysis.DependencyAnalysisSourceInfo

case class GobraDependencyAnalysisSourceInfo(pNode: PNode, pos: Position) extends DependencyAnalysisSourceInfo {
  override def toString: String = getDescription + " (" + super.toString + ")"

  override def getDescription: String = pNode.toString.replaceAll("\n", "\t")

  override def getPosition: Position = pos
}

case class ImplProofDependencyAnalysisSourceInfo(fromType: Type, toType: Type) extends DependencyAnalysisSourceInfo {
  override def getDescription: String = s"Implementation proof from $fromType to $toType"

  override def getPosition: Position = NoPosition

  override def toString: String = getDescription
}
