// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.frontend.info.base.{GhostLattice, GhostLevel}

/** The ghost levels of the values that an expression denotes (several for calls with multiple results). */
sealed trait GhostType {
  /** true iff any of the values is ghost code of some level */
  def isGhost: Boolean
  def isIdxGhost(idx: Int): Boolean
  /** the level of the `idx`-th value, or the bottom level if there is no such value */
  def levelAt(idx: Int)(implicit lattice: GhostLattice): GhostLevel
  /** the least upper bound of the levels of all values */
  def level(implicit lattice: GhostLattice): GhostLevel
  def length: Int
  def levels: Vector[GhostLevel]
  /** per value, whether it is ghost code of some level */
  def toTuple: Vector[Boolean]
}

object GhostType {
  def isGhost(implicit lattice: GhostLattice): GhostType = ofLevel(lattice.ghost)
  def notGhost: GhostType = TupleGhostType(Vector.empty)
  def ghost(bool: Boolean)(implicit lattice: GhostLattice): GhostType = if (bool) isGhost else notGhost
  def ofLevel(level: GhostLevel): GhostType = TupleGhostType(Vector(level))
  def ghostTuple(ghostTyping: Vector[Boolean])(implicit lattice: GhostLattice): GhostType =
    levelTuple(ghostTyping.map(isGhost => if (isGhost) lattice.ghost else lattice.actual))
  def levelTuple(levels: Vector[GhostLevel]): GhostType = TupleGhostType(levels)

  case class TupleGhostType(levels: Vector[GhostLevel]) extends GhostType {
    override lazy val length: Int = levels.length
    override lazy val toTuple: Vector[Boolean] = levels.map(_.isGhost)
    override lazy val isGhost: Boolean = levels.exists(_.isGhost)
    override def isIdxGhost(idx: Int): Boolean = levels.isDefinedAt(idx) && levels(idx).isGhost
    override def levelAt(idx: Int)(implicit lattice: GhostLattice): GhostLevel =
      if (levels.isDefinedAt(idx)) levels(idx) else lattice.bottom
    override def level(implicit lattice: GhostLattice): GhostLevel = lattice.lub(levels)
  }
}
