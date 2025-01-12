// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2025 ETH Zurich.

package viper.gobra

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.util.PluginAwareChopper
import viper.silver.ast
import viper.silver.ast.utility.chopper.Penalty

class PluginAwareChopperTests extends AnyFunSuite with Matchers with Inside {
  test("Domains ending in WellFoundedOrders are kept in chopped programs") {
    val intVarDecl = ast.LocalVarDecl("i", ast.Int)()
    val function = ast.Function("functionA", Seq(intVarDecl), ast.Bool, Seq.empty, Seq.empty, None)()
    val domainName = "IntWellFoundedOrder"
    val decreasingFn = ast.DomainFunc(
      "decreasing",
      Seq(ast.LocalVarDecl("l1", ast.Int)(), ast.LocalVarDecl("l2", ast.Int)()),
      ast.Bool,
    )(domainName = domainName)
    val boundedFn = ast.DomainFunc("bounded", Seq(ast.LocalVarDecl("l", ast.Int)()), ast.Bool)(domainName = domainName)
    val wfoDomain = ast.Domain(domainName, Seq(decreasingFn, boundedFn), Seq())()
    val program = ast.Program(Seq(wfoDomain), Seq.empty, Seq(function), Seq.empty, Seq.empty, Seq.empty)()
    val result = PluginAwareChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 1
    result.head shouldEqual program
  }
}
