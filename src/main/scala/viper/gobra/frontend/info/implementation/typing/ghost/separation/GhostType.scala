package viper.gobra.frontend.info.implementation.typing.ghost.separation

sealed trait GhostType {
  def isGhost: Boolean
  def isIdxGhost(idx: Int): Boolean
  def length: Int
  def toTuple: Vector[Boolean]
}

object GhostType {
  def isGhost: GhostType = TupleGhostType(Vector(true))
  def notGhost: GhostType = TupleGhostType(Vector.empty)
  def ghost(bool: Boolean): GhostType = if (bool) isGhost else notGhost
  def ghostTuple(ghostTyping: Vector[Boolean]): GhostType = TupleGhostType(ghostTyping)

  case class TupleGhostType(ghostTyping: Vector[Boolean]) extends GhostType {
    override lazy val length: Int = ghostTyping.length
    override def toTuple: Vector[Boolean] = ghostTyping
    override lazy val isGhost: Boolean = ghostTyping.nonEmpty && ghostTyping.exists(identity)
    override def isIdxGhost(idx: Int): Boolean = ghostTyping.isDefinedAt(idx) && ghostTyping(idx)
  }
}


