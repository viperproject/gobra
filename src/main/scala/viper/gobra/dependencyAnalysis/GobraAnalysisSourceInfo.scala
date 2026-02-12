package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend.PNode
import viper.silicon.dependencyAnalysis.AnalysisSourceInfo
import viper.silver.ast.Position

case class GobraAnalysisSourceInfo(pNode: PNode, pos: Position) extends AnalysisSourceInfo {
  override def toString: String = getDescription + " (" + super.toString + ")"

  override def getDescription: String = pNode.toString.replaceAll("\n", "\t")

  override def getPosition: Position = pos
}
