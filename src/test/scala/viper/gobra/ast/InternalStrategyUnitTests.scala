// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.


package viper.gobra.ast

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.internal._
import viper.gobra.reporting.Source.Parser.Internal
import viper.gobra.theory.Addressability

import scala.collection.immutable.ListMap

class InternalStrategyUnitTests extends AnyFunSuite with Matchers with Inside {

  test("Subnodes of EqCmp ") {
    val x = LocalVar("a", BoolT(Addressability.Exclusive))(Internal)
    val y = BoolLit(false)(Internal)
    val z = EqCmp(x, y)(Internal)
    z.subnodes should matchPattern {
      case Seq(`x`, `y`) =>
    }
  }

  test("Subnodes of ArrayLit") {
    val x = LocalVar("a", BoolT(Addressability.Exclusive))(Internal)
    val y = BoolLit(false)(Internal)
    val z = ArrayLit(2, BoolT(Addressability.Exclusive), ListMap(BigInt(0) -> x, BigInt(1) -> y))(Internal)
    z.subnodes should matchPattern {
      case Seq(`x`, `y`) =>
    }
  }

  test("Replace variable in EqCmp") {
    val x = LocalVar("a", BoolT(Addressability.Exclusive))(Internal)
    val y = BoolLit(false)(Internal)
    val z = EqCmp(x, y)(Internal)
    val transformedZ = z.transform{ case `x` => BoolLit(true)(Internal) }
    transformedZ should matchPattern {
      case EqCmp(BoolLit(true), BoolLit(false)) =>
    }
  }

}
