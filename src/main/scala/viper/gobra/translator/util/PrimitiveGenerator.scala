// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.util.Computation
import viper.silver.{ast => vpr}

object PrimitiveGenerator {

  trait PrimitiveGenerator[-A, +R] extends (A => R) with Generator {
    def gen(v: A): (R, Vector[vpr.Member])
  }

  def simpleGenerator[A <: AnyRef, R](f: A => (R, Vector[vpr.Member])): PrimitiveGenerator[A, R] = new PrimitiveGenerator[A, R] {

    var generatedMember: Set[vpr.Member] = Set.empty
    val cachedGen: A => (R, Vector[vpr.Member]) = Computation.cachedComputation(f)

    override def gen(v: A): (R, Vector[vpr.Member]) = cachedGen(v)

    override def finalize(col: Collector): Unit = generatedMember foreach col.addMember

    override def apply(v: A): R = {
      val (r, ss) = gen(v)
      generatedMember ++= ss.toSet
      r
    }
  }
}
