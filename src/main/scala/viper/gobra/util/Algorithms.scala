// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

object Algorithms {

  /** Simple Union-Find. `unionUse` takes the union functions and calls it on all edges. */
  def unionFind[T](unionUse: ((T, T) => Unit) => Unit): (Map[T, Int], Map[Int, Set[T]]) = {
    var idx = Map.empty[T, Int]
    def getIdx(k: T): Int = idx.getOrElse(k, { val res = idx.size; idx += (k -> res); res })
    var id = Map.empty[Int, Int]
    def getId(k: Int): Int = id.getOrElse(k, { val res = id.size; id += (k -> res); res })

    def find(e: T): Int = getId(getIdx(e))
    def union(l: T, r: T): Unit = id += (getIdx(r) -> find(l))

    unionUse(union)

    (
      idx.view.mapValues(id).toMap,
      idx.keySet.groupBy(find)
    )
  }

}
