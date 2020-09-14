// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal

/**
  * Trait Rewritable defines required methods for rewriting
  */
trait Rewritable extends Product {

  /**
    * Method that accesses all children of a node.
    * We allow 3 different types of children: Rewritable, Seq[Rewritable] and Option[Rewritable]
    * The supertype of all 3 is AnyRef
    * @return Sequence of children
    */
  def getChildren: Seq[AnyRef] = {
    ((0 until productArity) map { x: Int => productElement(x) }) collect {
      case s: Seq[Rewritable @unchecked] => s
      case o: Option[Rewritable @unchecked] => o
      case i: Rewritable => i
    }
  }

  /**
    * Duplicate children. Children list must be in the same order as in getChildren
    * @see [[Rewritable.getChildren()]] to see why we use type AnyRef for children
    *
    * @param children New children for this node
    * @return Duplicated node
    */
  def duplicate(children: Seq[AnyRef]): Rewritable
}