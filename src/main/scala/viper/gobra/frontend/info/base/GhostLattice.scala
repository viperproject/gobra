// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend.info.base

import viper.gobra.util.Violation

/** An element of a [[GhostLattice]]. */
trait GhostLevel {
  /** Name of the level, used in error messages and by the pretty printer unless the level is denoted by a keyword. */
  def name: String

  /** True iff this level lies above the bottom of its lattice, i.e., iff code and values of this level are erased
    * before execution. */
  def isGhost: Boolean

  override def toString: String = name
}

/**
  * The lattice of ghost levels. Code, locations, and values are classified by levels. A value of level `l1` may flow
  * into code or a location of level `l2` (be assigned to it, be passed as argument, be returned, or control it) iff
  * `leq(l1, l2)`, and the level of a compound expression is the least upper bound of the levels of its parts.
  *
  * The type checker only relies on this interface, so any lattice can be plugged in, including non-linear ones.
  * The bottom of the lattice is the level of actual code, i.e., the code that remains after erasing all ghost
  * code. The top of the lattice is the level of specifications: they cannot modify state, so every value may be
  * used in them.
  */
trait GhostLattice {
  /** The least level: the join of no reads, e.g., the level of a literal; values of this level may flow anywhere. */
  def bottom: GhostLevel

  /** The greatest level: the meet of no writes, e.g., the level of specifications and of proof-only statements, into
    * which values of every level may flow. */
  def top: GhostLevel

  /** The level of actual code and of the values it computes. */
  def actual: GhostLevel

  /** The level of inherently ghost constructs, such as quantifiers, `old` expressions, and predicates, and the
    * level that the `ghost` keyword denotes. */
  def ghost: GhostLevel

  /** True iff values of level `l1` may flow into code or locations of level `l2`. */
  def leq(l1: GhostLevel, l2: GhostLevel): Boolean

  def lub(l1: GhostLevel, l2: GhostLevel): GhostLevel

  def glb(l1: GhostLevel, l2: GhostLevel): GhostLevel

  /** The join of the levels read by an operation; `bottom` if nothing is read. */
  def lub(ls: Iterable[GhostLevel]): GhostLevel = ls.foldLeft(bottom)(lub)

  /** The meet of the levels written by an operation; `top` if nothing is written. */
  def glb(ls: Iterable[GhostLevel]): GhostLevel = ls.foldLeft(top)(glb)

  /** The levels denoted by modifier keywords in the surface syntax. */
  def ofKeyword: Map[String, GhostLevel]

  /** The modifier keyword denoting `l`, if there is one. */
  def keywordOf(l: GhostLevel): Option[String] = ofKeyword.collectFirst { case (keyword, `l`) => keyword }

  /** How `l` is referred to in error messages: by its keyword if it has one and by its name otherwise. */
  def show(l: GhostLevel): String = keywordOf(l).getOrElse(l.name)
}

object GhostLattice {
  /** The lattice used by the parser, the symbol table, and the type checker. */
  val default: GhostLattice = LinearGhostLattice(maxLevel = 2)
}

/**
  * The linear lattice `⊥ ⊑ actual ⊑ ghost(1) ⊑ ... ⊑ ghost(maxLevel) ⊑ ⊤`. Today's ghost code has level `ghost(1)`,
  * which the keyword `ghost` denotes, and the keyword `predicted` denotes `ghost(2)`. `⊥` and `⊤` are the identities
  * of join and meet and thus the levels of operations that read nothing (e.g., literals) or write nothing (e.g.,
  * specifications); no code or value is declared at them.
  */
case class LinearGhostLattice(maxLevel: Int) extends GhostLattice {
  import LinearGhostLattice._

  Violation.violation(maxLevel >= 2, s"the linear ghost lattice needs at least the levels ghost(1) and ghost(2), but got maxLevel = $maxLevel")

  override val bottom: GhostLevel = Bottom
  override val actual: GhostLevel = Actual
  override val ghost: GhostLevel = Ghost(1)
  override val top: GhostLevel = Top
  override val ofKeyword: Map[String, GhostLevel] = Map("ghost" -> Ghost(1), "predicted" -> Ghost(2))

  private def rank(l: GhostLevel): Int = l match {
    case Bottom => 0
    case Actual => 1
    case Ghost(level) if level <= maxLevel => 1 + level
    case Top => 2 + maxLevel
    case _ => Violation.violation(s"level $l does not belong to the ghost lattice $this")
  }

  override def leq(l1: GhostLevel, l2: GhostLevel): Boolean = rank(l1) <= rank(l2)
  override def lub(l1: GhostLevel, l2: GhostLevel): GhostLevel = if (leq(l1, l2)) l2 else l1
  override def glb(l1: GhostLevel, l2: GhostLevel): GhostLevel = if (leq(l1, l2)) l1 else l2
}

object LinearGhostLattice {
  /**
    * The elements of the linear lattice. They are deliberately not case classes: a level is stored in AST nodes, and
    * Kiama treats every `Product` field of a node as a child node.
    */
  sealed abstract class Level(override val name: String, override val isGhost: Boolean) extends GhostLevel

  object Bottom extends Level("bottom", isGhost = false)

  object Actual extends Level("actual", isGhost = false)

  object Top extends Level("top", isGhost = true)

  final class Ghost(val level: Int) extends Level(s"ghost($level)", isGhost = true) {
    Violation.violation(level >= 1, s"ghost levels start at 1, but got $level")
    override def equals(other: Any): Boolean = other match {
      case that: Ghost => that.level == level
      case _ => false
    }
    override def hashCode(): Int = level
  }

  object Ghost {
    def apply(level: Int): Ghost = new Ghost(level)
    def unapply(ghost: Ghost): Option[Int] = Some(ghost.level)
  }
}
