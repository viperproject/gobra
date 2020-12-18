// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

import org.bitbucket.inkytonik.kiama.attribution.Attribution


trait Computation[-A, +R] extends (A => R) {

def compute(n: A): R

override def apply(n: A): R = compute(n)
}

trait Memoization[-A <: AnyRef, +R] extends Computation[A, R] { this: Attribution =>

  lazy val store: A => R = attr(super.apply)

  override def apply(v: A): R = store(v)
}

trait Safety[-A, +R] extends Computation[A, R] {
  def safe(n: A): Boolean

  def unsafe: R

  override def apply(n: A): R = if (safe(n)) super.apply(n) else unsafe
}

trait Validity[-A, R] extends Computation[A, R] {

  def invalid(ret: R): Boolean

  def valid(n: A): Boolean = !invalid(apply(n))
}

object Computation {
  def cachedComputation[A <: AnyRef, R](f: A => R): A => R = new Attribution with Memoization[A, R] {
    override def compute(n: A): R = f(n)
  }
}

