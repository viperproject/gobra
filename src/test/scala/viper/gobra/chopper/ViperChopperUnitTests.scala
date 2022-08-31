// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.chopper

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.util.ViperChopper
import viper.gobra.util.ViperChopper.Penalty
import viper.gobra.util.ViperChopper.SCC.Component
import viper.silver.ast

class ViperChopperUnitTests extends AnyFunSuite with Matchers with Inside {

  test("Single function") {
    val functions = Seq(
      ast.Function("functionA", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)()
    )
    val program = ast.Program(Seq.empty, Seq.empty, functions, Seq.empty, Seq.empty, Seq.empty)()
    ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge) shouldEqual Vector(program)
  }

  test("Two independent functions without shared dependency") {
    val functions = Seq(
      ast.Function("functionA", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)(),
      ast.Function("functionB", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)(),
    )
    val program = ast.Program(Seq.empty, Seq.empty, functions, Seq.empty, Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq(functions(0)), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq(functions(1)), Seq.empty, Seq.empty, Seq.empty)()
    )
  }

  test("Four independent functions with shared type and field") {
    // domain domainA { }
    val domain = ast.Domain("domainA", Seq.empty, Seq.empty, Seq.empty)()

    // domainA
    val domainType = ast.DomainType(domain, Map.empty)

    // var d: domainA
    val domainVarDecl = ast.LocalVarDecl("d", domainType)()

    // var r: Ref
    val refVarDecl = ast.LocalVarDecl("r", ast.Ref)()

    // field f: domainA
    val field = ast.Field("f", domainType)()

    // r.f != r.f
    val assertionWithDomainType = ast.NeCmp(ast.FieldAccess(refVarDecl.localVar, field)(),  ast.FieldAccess(refVarDecl.localVar, field)())()

    val functions = Seq(
      ast.Function("functionA", Seq(domainVarDecl), ast.Bool, Seq.empty, Seq.empty, None)(),
      ast.Function("functionB", Seq.empty, domainType, Seq.empty, Seq.empty, None)(),
      ast.Function("functionC", Seq(refVarDecl), ast.Bool, Seq(assertionWithDomainType), Seq.empty, None)(),
      ast.Function("functionD", Seq(refVarDecl), ast.Bool, Seq.empty, Seq(assertionWithDomainType), None)(),
      ast.Function("functionE", Seq(refVarDecl), ast.Bool, Seq.empty, Seq.empty, Some(assertionWithDomainType))(),
    )
    val program = ast.Program(Seq(domain), Seq(field), functions, Seq.empty, Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(10), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 5
    result shouldEqual Vector(
      ast.Program(Seq(domain), Seq.empty, Seq(functions(0)), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq(domain), Seq.empty, Seq(functions(1)), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq(domain), Seq(field), Seq(functions(2)), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq(domain), Seq(field), Seq(functions(3)), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq(domain), Seq(field), Seq(functions(4)), Seq.empty, Seq.empty, Seq.empty)(),
    )
  }

  test("Two dependent functions without shared dependency") {
    val functions = Seq(
      ast.Function("functionA", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)(),
      ast.Function("functionB", Seq.empty, ast.Int, Seq.empty, Seq.empty, None)(),
    )
    val program = ast.Program(Seq.empty, Seq.empty, functions, Seq.empty, Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq(functions(0)), Seq.empty, Seq.empty, Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq(functions(1)), Seq.empty, Seq.empty, Seq.empty)()
    )
  }

  test("Single method") {
    val methods = Seq(
      ast.Method("methodA", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
    )
    val program = ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, methods, Seq.empty)()
    ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge) shouldEqual Vector(program)
  }

  test("Two dependent functions") {
    val calleeStub = ast.Function("functionA", Seq.empty, ast.Bool, Seq.empty, Seq.empty, None)()
    val callerStub = ast.Function("functionB", Seq.empty, ast.Bool, Seq.empty, Seq.empty, None)()
    val callee = calleeStub.copy(body = Some(ast.TrueLit()()))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)
    val call = ast.FuncApp(func = callee, Seq.empty)()
    val caller = callerStub.copy(body = Some(call))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)

    val program = ast.Program(Seq.empty, Seq.empty, Seq(callee, caller), Seq.empty, Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 1
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq(callee, caller), Seq.empty, Seq.empty, Seq.empty)()
    )
  }

  test("Five independent methods with shared dependencies") {
    // domain domainA { }
    val domain = ast.Domain("domainA", Seq.empty, Seq.empty, Seq.empty)()

    // domainA
    val domainType = ast.DomainType(domain, Map.empty)

    // var d: domainA
    val domainVarDecl = ast.LocalVarDecl("d", domainType)()

    // var i: Int
    val intVarDecl = ast.LocalVarDecl("i", ast.Int)()

    // function functionA(i int) domainA
    val function = ast.Function("functionA", Seq(intVarDecl), domainType, Seq.empty, Seq.empty, None)()

    // functionA(i) == functionA(0)
    val assertionWithDomainType = ast.EqCmp(ast.FuncApp(function, Seq(intVarDecl.localVar))(),  ast.FuncApp(function, Seq(ast.IntLit(0)()))())()

    // assert functionA(i) == functionA(0)
    val bodyWithDomainType = ast.Seqn(Seq(ast.Assert(assertionWithDomainType)()), Seq.empty)()

    val methods = Seq(
      ast.Method("methodA", Seq(domainVarDecl), Seq.empty, Seq.empty, Seq.empty, None)(),
      ast.Method("methodB", Seq.empty, Seq(domainVarDecl), Seq.empty, Seq.empty, None)(),
      ast.Method("methodC", Seq(intVarDecl), Seq.empty, Seq(assertionWithDomainType), Seq.empty, None)(),
      ast.Method("methodD", Seq(intVarDecl), Seq.empty, Seq.empty, Seq(assertionWithDomainType), None)(),
      ast.Method("methodE", Seq(intVarDecl), Seq.empty, Seq.empty, Seq.empty, Some(bodyWithDomainType))(),
    )
    val program = ast.Program(Seq(domain), Seq.empty, Seq(function), Seq.empty, methods, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(10), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 5
    result shouldEqual Vector(
      ast.Program(Seq(domain), Seq.empty, Seq.empty, Seq.empty, Seq(methods(0)), Seq.empty)(),
      ast.Program(Seq(domain), Seq.empty, Seq.empty, Seq.empty, Seq(methods(1)), Seq.empty)(),
      ast.Program(Seq(domain), Seq.empty, Seq(function), Seq.empty, Seq(methods(2)), Seq.empty)(),
      ast.Program(Seq(domain), Seq.empty, Seq(function), Seq.empty, Seq(methods(3)), Seq.empty)(),
      ast.Program(Seq(domain), Seq.empty, Seq(function), Seq.empty, Seq(methods(4)), Seq.empty)(),
    )
  }

  test("Two dependent methods") {
    val calleeStub = ast.Method("methodA", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
    val callerStub = ast.Method("methodB", Seq.empty, Seq.empty, Seq.empty, Seq.empty, None)()
    val call = ast.MethodCall("methodA", Seq.empty, Seq.empty)(ast.NoPosition, ast.NoInfo, ast.NoTrafos)
    val body = ast.Seqn(Seq(call), Seq.empty)()
    val callee = calleeStub.copy(body = Some(body))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)
    val caller = callerStub.copy(body = Some(body))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)

    val program = ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq(callee, caller), Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq(callee), Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq(caller, calleeStub), Seq.empty)()
    )
  }

  test("Two independent predicates with shared type") {
    // domain domainA { }
    val domain = ast.Domain("domainA", Seq.empty, Seq.empty, Seq.empty)()

    // domainA
    val domainType = ast.DomainType(domain, Map.empty)

    // var d: domainA
    val domainVarDecl = ast.LocalVarDecl("d", domainType)()

    // var r: Ref
    val refVarDecl = ast.LocalVarDecl("r", ast.Ref)()

    // field f: domainA
    val field = ast.Field("f", domainType)()

    // r.f != r.f
    val assertionWithDomainType = ast.NeCmp(ast.FieldAccess(refVarDecl.localVar, field)(),  ast.FieldAccess(refVarDecl.localVar, field)())()

    val predicates = Seq(
      ast.Predicate("predicateA", Seq(domainVarDecl), None)(),
      ast.Predicate("predicateB", Seq(refVarDecl), Some(assertionWithDomainType))(),
    )
    val program = ast.Program(Seq(domain), Seq(field), Seq.empty, predicates, Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(10), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq(domain), Seq.empty, Seq.empty, Seq(predicates(0)), Seq.empty, Seq.empty)(),
      ast.Program(Seq(domain), Seq(field), Seq.empty, Seq(predicates(1)), Seq.empty, Seq.empty)(),
    )
  }

  test("Two dependent predicates") {
    val calleeStub = ast.Predicate("predicateA", Seq.empty, None)()
    val callerStub1 = ast.Predicate("predicateB", Seq.empty, None)()
    val callee = calleeStub.copy(body = Some(ast.TrueLit()()))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)
    val call = ast.PredicateAccess(Seq.empty, predicateName = callee.name)()
    val caller1 = callerStub1.copy(body = Some(call))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)

    val program = ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq(callee, caller1), Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq(callee), Seq.empty, Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq(caller1, calleeStub), Seq.empty, Seq.empty)(),
    )
  }

  test("Three dependent predicates") {
    val calleeStub = ast.Predicate("predicateA", Seq.empty, None)()
    val callerStub1 = ast.Predicate("predicateB", Seq.empty, None)()
    val callerStub2 = ast.Predicate("predicateC", Seq.empty, None)()
    val callee = calleeStub.copy(body = Some(ast.TrueLit()()))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)
    val call = ast.PredicateAccess(Seq.empty, predicateName = callee.name)()
    val caller1 = callerStub1.copy(body = Some(call))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)
    val unfolding = ast.Unfolding(ast.PredicateAccessPredicate(call, ast.FullPerm()())(), ast.TrueLit()())()
    val caller2 = callerStub2.copy(body = Some(unfolding))(ast.NoPosition, ast.NoInfo, ast.NoTrafos)

    val program = ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq(callee, caller1, caller2), Seq.empty, Seq.empty)()
    val result = ViperChopper.chop(program)(bound = Some(5), penalty = Penalty.DefaultWithoutForcedMerge)
    result.length shouldBe 2
    result shouldEqual Vector(
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq(caller1, calleeStub), Seq.empty, Seq.empty)(),
      ast.Program(Seq.empty, Seq.empty, Seq.empty, Seq(caller2, callee), Seq.empty, Seq.empty)(),
    )
  }

  // SCC tests

  test("SCC with singleton graph") {
    compareComponents (ViperChopper.SCC.components(Seq("A"), Seq.empty), Vector(
      ViperChopper.SCC.Component(Vector("A"))
    ))
  }

  test("SCC with loop of size 2") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "C"),
      ("D", "E")
    )
    compareComponents (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("B")),
      ViperChopper.SCC.Component(Vector("C", "D")),
      ViperChopper.SCC.Component(Vector("E"))
    ))
  }

  test("SCC with loop of size 3") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "B"),
      ("D", "C"),
      ("D", "E")
    )
    compareComponents (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("D", "C", "B")),
      ViperChopper.SCC.Component(Vector("E"))
    ))
  }

  test("SCC with X shape graph") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "E")
    )
    compareComponents (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("B")),
      ViperChopper.SCC.Component(Vector("C")),
      ViperChopper.SCC.Component(Vector("D")),
      ViperChopper.SCC.Component(Vector("E"))
    ))
  }

  test("Complex SCC test") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "C"),
      ("D", "E")
    )
    val choppedReference = Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("B")),
      ViperChopper.SCC.Component(Vector("C", "D")),
      ViperChopper.SCC.Component(Vector("E"))
    )
    val (components, inverse, dag) = ViperChopper.SCC.compute(vertices, edges)
    compareComponents(components, choppedReference)
    inverse.keySet shouldEqual Set("A", "B", "C", "D", "E")
    compareComponent(inverse("A"), Component(Vector("A")))
    compareComponent(inverse("B"), Component(Vector("B")))
    compareComponent(inverse("C"), Component(Vector("D", "C")))
    compareComponent(inverse("D"), Component(Vector("D", "C")))
    compareComponent(inverse("E"), Component(Vector("E")))
    compareDAG(dag, Seq(
      (ViperChopper.SCC.Component(Vector("A")), ViperChopper.SCC.Component(Vector("D", "C"))),
      (ViperChopper.SCC.Component(Vector("B")), ViperChopper.SCC.Component(Vector("D", "C"))),
      (ViperChopper.SCC.Component(Vector("D", "C")), ViperChopper.SCC.Component(Vector("E")))
    ))
  }

  def compareComponent[T](one: Component[T], other: Component[T]): Unit = {
    one.nodes.toSet shouldEqual other.nodes.toSet
  }

  def compareComponents[T](one: Iterable[Component[T]], other: Iterable[Component[T]]): Unit = {
    one.map(c => c.nodes.toSet).toSet shouldEqual other.map(c => c.nodes.toSet).toSet
  }

  def compareDAG[T](one: Iterable[(Component[String], Component[String])], other: Iterable[(Component[String], Component[String])]): Unit = {
    one.map(c => (c._1.nodes.toSet, c._2.nodes.toSet)).toSet shouldEqual other.map(c => (c._1.nodes.toSet, c._2.nodes.toSet)).toSet
  }
}