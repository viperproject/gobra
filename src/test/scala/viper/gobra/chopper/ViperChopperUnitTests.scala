// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.chopper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import viper.gobra.util.ViperChopper
import viper.silver.ast

class ViperChopperUnitTests extends AnyFunSuite with Matchers with Inside {

  test("ViperChopper 1: should chop program as expected") {
    val functions = Seq(
      ast.Function("functionA", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)()
    )
    val program = ast.Program(Seq.empty, Seq.empty, functions, Seq.empty, Seq.empty, Seq.empty)()
    ViperChopper.chop(program) shouldEqual Vector(program)
  }

  test("ViperChopper 2: should chop program as expected") {
    val functions = Seq(
      ast.Function("functionA", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)(),
      ast.Function("functionB", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)()
    )
    val program = ast.Program(Seq.empty, Seq.empty, functions, Seq.empty, Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq(
        ast.Function("functionA", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)()
      ), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq(
        ast.Function("functionB", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)()
      ), Seq.empty, Seq.empty, Seq.empty)()
    )
  }

  test("ViperChopper 3: should chop program as expected") {
    val methods = Seq(
      ast.Method("methodA", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
    )
    val program = ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, methods, Seq.empty)()
    ViperChopper.chop(program) shouldEqual Vector(program)
  }

  test("ViperChopper 4: should chop program as expected") {
    val methods = Seq(
      ast.Method("methodA", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)(),
      ast.Method("methodB", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
    )
    val program = ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, methods, Seq.empty)()
    val result = ViperChopper.chop(program)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq(
        ast.Method("methodA", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
      ), Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq(
        ast.Method("methodB", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
      ), Seq.empty)()
    )
  }
}