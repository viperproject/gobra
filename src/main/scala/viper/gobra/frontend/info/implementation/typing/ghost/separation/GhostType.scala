// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

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


