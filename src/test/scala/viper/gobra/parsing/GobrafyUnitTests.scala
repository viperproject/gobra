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

  test("function with ghost args") {
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

  test("function with ghost results") {
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

  test("assignment with ghost variables") {
    val input =
      """
        |a, b, c = d, e, f //@ with: g, h = i, j
        |""".stripMargin
    val expected =
      """
        |a, b, c, g, h = d, e, f, i, j
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("short variable declaration with ghost variables") {
    val input =
      """
        |a, b, c := d, e, f //@ with: g, h := i, j
        |""".stripMargin
    val expected =
      """
        |a, b, c, g, h := d, e, f, i, j
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("short variable declaration with ghost variables and addressability") {
    val input =
      """
        |a, b, c := d, e, f //@ with: g, h := i, j; addressable: b, g
        |""".stripMargin
    val expected =
      """
        |a, b@, c, g@, h := d, e, f, i, j
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("ghost return mixed") {
    val input =
      """
        |return a, b //@ with: c, d
        |""".stripMargin
    val expected =
      """
        |return a, b, c, d
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("ghost return only actual") {
    val input =
      """
        |return a, b //@ with:
        |""".stripMargin
    val expected =
      """
        |return a, b
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("ghost return only ghost") {
    val input =
      """
        |return //@ with: a, b
        |""".stripMargin
    val expected =
      """
        |return a, b
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with only actual arguments") {
    val input =
      """
        |foo(a, b) /*@ with: @*/
        |foo(c, d) //@ with:
        |""".stripMargin
    val expected =
      """
        |foo(a, b)
        |foo(c, d)
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with only ghost arguments") {
    val input =
      """
        |foo() //@ with: a, b
        |foo() /*@ with: c, d @*/
        |""".stripMargin
    val expected =
      """
        |foo(a, b)
        |foo(c, d)
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with mix arguments") {
    val input =
      """
        |foo(a, b) /*@ with: c, d @*/
        |foo(e, f) //@ with: g, h
        |""".stripMargin
    val expected =
      """
        |foo(a, b, c, d)
        |foo(e, f, g, h)
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with spec, with only actual arguments") {
    val input =
      """
        |cl(a, b) /*@ as foo{} @*/
        |cl(c, d) /*@ as foo{} @*//*@ with: @*/
        |cl(c, d) /*@ as foo{1, 2} @*//*@ with: @*/
        |cl(c, d) /*@ as foo{x: 1, y: 2} @*//*@ with: @*/
        |cl(e, f) /*@ as foo{} @*///@ with:
        |""".stripMargin
    val expected =
      """
        |cl(a, b)  as foo{}
        |cl(c, d) as foo{}
        |cl(c, d) as foo{1, 2}
        |cl(c, d) as foo{x: 1, y: 2}
        |cl(e, f) as foo{}
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with spec, with only ghost arguments") {
    val input =
      """
        |cl() /*@ as foo{} @*//*@ with: a, b @*/
        |cl() /*@ as foo{} @*///@ with: c, d
        |cl() /*@ as foo{1, 2} @*///@ with: c, d
        |cl() /*@ as foo{x: 1, y: 2} @*///@ with: c, d
        |""".stripMargin
    val expected =
      """
        |cl(a, b) as foo{}
        |cl(c, d) as foo{}
        |cl(c, d) as foo{1, 2}
        |cl(c, d) as foo{x: 1, y: 2}
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("call with spec, with mixed arguments") {
    val input =
      """
        |cl(a, b) /*@ as foo{} @*//*@ with: c, d @*/
        |cl(e, f) /*@ as foo{} @*///@ with: g, h
        |""".stripMargin
    val expected =
      """
        |cl(a, b, c, d) as foo{}
        |cl(e, f, g, h) as foo{}
        |""".stripMargin
    frontend.gobrafy(input, expected)
  }

  test("unfolding predicate instance") {
    val input =
      """
        |v := /*@ unfolding: list(n) @*/ n.val
        |""".stripMargin
    val expected =
      """
        |v := unfolding list(n) in n.val
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
