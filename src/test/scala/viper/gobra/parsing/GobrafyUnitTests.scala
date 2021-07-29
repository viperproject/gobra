// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.parsing

import org.scalatest.{Assertion, Inside}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.frontend.Gobrafier

class GobrafyUnitTests extends AnyFunSuite with Matchers with Inside {
  private val frontend = new TestFrontend()

  test("call with ghost args") {
    val input =
      """
        |//@ ghost-parameters: b int, c int
        |//@ requires a >= 0 && b >= 0
        |func foo(a int) {}
        |""".stripMargin
    val expected =
      """
        |requires a >= 0 && b >= 0
        |func foo(a int, ghost b int, ghost c int) {}
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with ghost results") {
    val input =
      """
        |//@ ghost-results: b int, c int
        |//@ requires a >= 0
        |//@ ensures a == b && b == c
        |func foo(a int) {}
        |""".stripMargin
    val expected =
      """
        |requires a >= 0
        |ensures a == b && b == c
        |func foo(a int) (ghost b int, ghost c int) {}
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("pure interface function should stay pure") {
    val input =
      """
        |type stream interface {
        |  //@ pred mem()
        |
        |  //@ requires acc(mem(), 1/2)
        |  //@ pure
        |  hasNext() bool
        |
        |  //@ requires mem() && hasNext()
        |  //@ ensures mem()
        |  next() interface{}
        |}""".stripMargin
    val expected =
      """
        |type stream interface {
        |  pred mem()
        |
        |  requires acc(mem(), 1/2)
        |  pure
        |  hasNext() bool
        |
        |  requires mem() && hasNext()
        |  ensures mem()
        |  next() interface{}
        |}""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("pure interface function should stay pure even with provided implementation") {
    val input =
      """
        |package presentation
        |
        |type stream interface {
        | //@ pred mem()
        |
        | //@ requires acc(mem(), 1/2)
        | //@ pure
        | hasNext() bool
        |}
        |
        |/** Implementation **/
        |
        |type counter struct{ f, max int }
        |
        |//@ requires acc(x, 1/2)
        |//@ pure
        |func (x *counter) hasNext() bool {
        |	return x.f < x.max
        |}
        |
        |""".stripMargin
    val expected =
      """
        |package presentation
        |
        |type stream interface {
        | pred mem()
        |
        | requires acc(mem(), 1/2)
        | pure
        | hasNext() bool
        |}
        |
        |/** Implementation **/
        |
        |type counter struct{ f, max int }
        |
        |requires acc(x, 1/2)
        |pure
        |func (x *counter) hasNext() bool {
        |	return x.f < x.max
        |}
        |
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }


  /* ** Stubs, mocks and other test setup */

  class TestFrontend {
    def gobrafy(input: String, expected: String): Assertion = {
      val actual = Gobrafier.gobrafy(input)
      actual.strip() should be (expected.strip())
    }
  }
}
