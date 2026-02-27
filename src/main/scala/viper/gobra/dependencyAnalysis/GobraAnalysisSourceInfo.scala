package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend.PNode
import viper.gobra.ast.internal.Type
import viper.silicon.dependencyAnalysis.AnalysisSourceInfo
import viper.silver.ast.{NoPosition, Position}

case class GobraAnalysisSourceInfo(pNode: PNode, pos: Position) extends AnalysisSourceInfo {
  override def toString: String = getDescription + " (" + super.toString + ")"

  override def getDescription: String = pNode.toString.replaceAll("\n", "\t")

  override def getPosition: Position = pos
}

case class ImplementationProofSourceInfo(fromType: Type, toType: Type) extends AnalysisSourceInfo {
  override def getDescription: String = s"Implementation proof from $fromType to $toType"

  override def getPosition: Position = NoPosition
}