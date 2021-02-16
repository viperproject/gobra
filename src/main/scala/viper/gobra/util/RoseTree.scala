// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util


/** A simple rose tree. */
case class RoseTree[T](head: T, children: Vector[RoseTree[T]] = Vector.empty) {
  def map[R](f: T => R): RoseTree[R] = RoseTree(f(head), children map (_ map f))
  def fold[R](f: (T, Vector[R]) => R): R = f(head, children map (_ fold f))
  def toVector: Vector[T] = fold[Vector[T]]{ case (hd, tl) => hd +: tl.flatten }
}
