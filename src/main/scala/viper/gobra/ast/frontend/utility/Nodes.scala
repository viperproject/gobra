// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.ast.frontend.utility

import viper.gobra.ast.frontend.PNode

object Nodes {

  /** Returns a list of all direct sub-nodes of this node. */
  def subnodes(n: PNode): Seq[PNode] = {

    // extracts all reachable `PNode`s including the current node
    def extractChildren(a: Any): Seq[PNode] = a match {
      case t: PNode => Seq(t)
      // Handles Vector, Seq, Option, Map (as iterator of key and value pairs)
      case t: Iterable[_] => t.flatMap(extractChildren).toSeq
      // Handles all tuples and case classes that are not subtypes of node
      case t: Product => t.productIterator.flatMap(extractChildren).toSeq
      // If it is not a node nor contains a node, then it is irrelevant.
      case _ => Seq.empty
    }

    n.productIterator.flatMap(extractChildren).toSeq
  }

}
