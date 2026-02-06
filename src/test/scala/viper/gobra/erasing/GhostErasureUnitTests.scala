// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.erasing

import org.bitbucket.inkytonik.kiama.util.{Positions, StringSource}
import org.scalatest.{Assertion, Inside, Succeeded}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config, PackageInfo, Parser}
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter

class GhostErasureUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()
  test("Ghost Erasure: variable declaration should not have a trailing equal sign") {
    // var value int
    val decl = PVarDecl(Some(PIntType()), Vector(), Vector(PIdnDef("value")), Vector(false))
    frontend.ghostLessStmt(decl)() should matchPattern {
      case d: PVarDecl if d == decl =>
    }
  }

  test("Ghost Erasure: calls to (pure) ghost functions should be removed") {
    val modes: Set[Boolean] = Set(false, true)
    modes.foreach(isPure => {
      val input = s"""
        |package pkg
        |ghost ${if (isPure) "pure" else ""} func test() (res bool) {
        | return true
        |}
        |func main() {
        | test()
        | var t1 = test()
        | t2 := test()
        |}""".stripMargin
      val expected = s"""
        |package pkg
        |func main() {
        |}""".stripMargin
      frontend.testProg(input, expected)
    })
  }

  test("Ghost Erasure: calls to (pure) ghost functions with multiple return values should be removed") {
    val modes: Set[Boolean] = Set(false, true)
    modes.foreach(isPure => {
      val input = s"""
        |package pkg
        |ghost ${if (isPure) "pure" else ""} func test() (res1 bool, res2 int) {
        | return true, 42
        |}
        |func main() {
        | test()
        | var t1, t2 = test()
        | t3, t4 := test()
        |}""".stripMargin
      val expected = s"""
        |package pkg
        |func main() {
        |}""".stripMargin
      frontend.testProg(input, expected)
    })
  }

  test("Ghost Erasure: variable declarations with mixed ghost types should be correctly ghost erased") {
    val input = s"""
      |package pkg
      |func test() (ghost res1 bool, res2 int) {
      | return true, 42
      |}
      |func main() {
      | test()
      | var t1, t2 = test()
      | t3, t4 := test()
      |}""".stripMargin
    val expected = s"""
      |package pkg
      |func test() (res2 int) {
      | return 42
      |}
      |func main() {
      | test()
      | var t2 = test()
      | t4 := test()
      |}""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: assignments with mixed ghost types should be correctly ghost erased") {
    val input = s"""
      |package pkg
      |func test() (ghost res1 bool, res2 int) {
      | return true, 42
      |}
      |func main() {
      | t1, t2 := test()
      | t1 = false
      | t2 = 0
      | t1, t2 = true, 42
      |}""".stripMargin
    val expected = s"""
      |package pkg
      |func test() (res2 int) {
      | return 42
      |}
      |func main() {
      | t2 := test()
      | t2 = 0
      | t2 = 42
      |}""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: if ... else if ... else ... should correctly be erased") {
    // if true {} else if (false) {} else {}
    val input = PIfStmt(Vector(
      PIfClause(None, PBoolLit(true), PBlock(Vector())),
      PIfClause(None, PBoolLit(false), PBlock(Vector()))),
      Some(PBlock(Vector())))
    frontend.ghostLessStmt(input)() should matchPattern {
      case PIfStmt(Vector(
        PIfClause(None, PBoolLit(true), b1),
        PIfClause(None, PBoolLit(false), b2)),
        Some(b3)) if b1.nonEmptyStmts == Vector() && b2.nonEmptyStmts == Vector() && b3.nonEmptyStmts == Vector() =>
    }
  }

  test("Ghost Erasure: var decls of inferred ghost type from a pure function call should be erased") {
    val input = s"""
      |package pkg
      |pure func test(ghost s seq[int]) (ghost res seq[int]) {
      | return s
      |}
      |func main() {
      | s := seq[int] { 1 }
      | res := test(s)
      |}""".stripMargin
    val expected = s"""
      |package pkg
      |func test() {
      | return
      |}
      |func main() {
      | // no call to test() as it is pure
      |}""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: var decls of inferred ghost type from function call should be erased but not the call itself") {
    val input = s"""
      |package pkg
      |func test1() (ghost res int) {
      | return 1
      |}
      |func test2() (res int) {
      | return 2
      |}
      |func test3() (ghost res int) {
      | return 3
      |}
      |func main() {
      | res1, res2, res3 := test1(), test2(), test3()
      | res1 = 41
      | res2 = 42
      | res3 = 43
      | res1, res2, res3 = test1(), test2(), test3()
      |}""".stripMargin
    val expected = s"""
      |package pkg
      |func test1() {
      | return
      |}
      |func test2() (res int) {
      | return 2
      |}
      |func test3() {
      | return
      |}
      |func main() {
      | test1()
      | res2 := test2()
      | test3()
      | res2 = 42
      | test1()
      | res2 = test2()
      | test3()
      |}""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: var decls of inferred ghost type from function call should be erased but not the call itself for a multi assignment") {
    val input = s"""
      |package pkg
      |func test() (ghost res1 bool, ghost res2 int) {
      | return true, 1
      |}
      |func main() {
      | res1, res2 := test()
      | res1 = false
      | res2 = 42
      | res1, res2 = test()
      |}""".stripMargin
    val expected = s"""
      |package pkg
      |func test() {
      | return
      |}
      |func main() {
      | test()
      | test()
      |}""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: spec of a function literal should be erased") {
    val input =
      s"""
         |package pkg
         |func main() {
         |  x@ := 0
         |  c := preserves acc(&x)
         |       func() int { return x }
         |}
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |func main() {
         |  x := 0
         |  c := func() int { return x }
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: name of a function literal should be erased") {
    val input =
      s"""
         |package pkg
         |func main() {
         |  c := func f() int { return 42 }
         |}
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |func main() {
         |  c := func() int { return 42 }
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: ghost arguments of a function literal should be erased") {
    val input =
      s"""
         |package pkg
         |func main() {
         |  c := func f(ghost a int) int { return 42 }
         |}
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |func main() {
         |  c := func() int { return 42 }
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: spec should be erased from closure calls") {
    val input =
      s"""
         |package pkg
         |func main() {
         |  c := func f() int { return 42 }
         |  c() as f
         |}
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |func main() {
         |  c := func() int { return 42 }
         |  c()
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: ghost arguments should be erased from closure calls") {
    val input =
      s"""
         |package pkg
         |func main() {
         |  c := func f(x int, ghost y int, z bool) { }
         |  c(42, 21, false) as f
         |}
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |func main() {
         |  c := func(x int, z bool) { }
         |  c(42, false)
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: ghost types should be erased") {
    val input =
      s"""
         |package pkg
         |type Struct struct {
         |    f int
         |    ghost g int
         |}
         |ghost type GhostStruct struct {
         |    f int
         |}
         |ghost type GhostIntDef int
         |ghost type GhostIntAlias = int
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |type Struct struct {
         |    f int
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  test("Ghost Erasure: ghost calls to non-ghost pure functions should be erased") {
    // this corresponds to the program in "issues/000861-3.gobra"
    val input =
      s"""
         |package pkg
         |func test() (ghost res bool) {
         |    ghost var x = 42
         |    ghost var y = 0
         |    tmp := isEqual(x, y)
         |    res = tmp
         |    return
         |}
         |decreases
         |pure func isEqual(x, y int) bool {
         |    return x == y
         |}
         |""".stripMargin
    val expected =
      s"""
         |package pkg
         |func test() {
         |  return
         |}
         |func isEqual(x, y int) bool {
         |  return x == y
         |}
         |""".stripMargin
    frontend.testProg(input, expected)
  }

  /* ** Stubs, mocks, and other test setup  */

  class TestFrontend {

    def stubProgram(inArgs: Vector[(PParameter, Boolean)], body : PStatement) : PProgram = PProgram(
      PPackageClause(PPkgDef("pkg")),
      Vector.empty,
      Vector.empty,
      Vector.empty,
      Vector(PFunctionDecl(
        PIdnDef("foo"),
        inArgs.map(_._1),
        PResult(Vector()),
        PFunctionSpec(Vector.empty, Vector.empty, Vector.empty, Vector.empty, Vector.empty),
        Some(PBodyParameterInfo(inArgs.collect{ case (n: PNamedParameter, true) => PIdnUse(n.id.name) }), PBlock(Vector(body)))
      ))
    )

    def ghostLessProg(program: PProgram): PProgram = {
      val positions = new Positions
      val pkg = PPackage(
        PPackageClause(PPkgDef("pkg")),
        Vector(program),
        new PositionManager(positions),
        new PackageInfo("pkg", "pkg", false)
      )
      val tree = new Info.GoTree(pkg)
      val config = Config(disableCheckTerminationPureFns = Some(true))
      val info = new TypeInfoImpl(tree, Map.empty)(config)
      info.errors match {
        case Vector(msgs) => fail(s"Type-checking failed: $msgs")
        case _ =>
      }

      val ghostLess = new GhostLessPrinter(info).format(pkg)
      // try to parse ghostLess string:
      val parseRes = Parser.parseProgram(StringSource(ghostLess, "Ghostless Program"), false)
      parseRes match {
        case Right(prog) => prog
        case Left(messages) => fail(s"Parsing failed: $messages")
      }
    }

    def ghostLessStmt(stmt: PStatement)(inArgs: Vector[(PParameter, Boolean)] = Vector()): PStatement = {
      val program = stubProgram(inArgs, stmt)
      val ghostLess = ghostLessProg(program)
      val block = ghostLess match {
        case PProgram(_, _, _, _, Vector(PFunctionDecl(PIdnDef("foo"), _, _, _, Some((_, b))))) => b
        case p => fail(s"Parsing succeeded but with an unexpected program $p")
      }
      normalize(block.stmts) match {
        case Vector(stmt) => stmt
        case stmts => fail(s"Parsing succeeded but with unexpected stmts $stmts")
      }
    }

    def ghostLessExpr(expr: PExpression)(inArgs: Vector[(PParameter, Boolean)] = Vector()): PExpression = {
      val stmt = PShortVarDecl(Vector(expr), Vector(PIdnUnk("n")), Vector(false))
      val ghostLess = ghostLessStmt(stmt)(inArgs)
      ghostLess match {
        case PShortVarDecl(Vector(e), _, _) => e
        case stmt => fail(s"Parsing succeeded but with unexpected stmt $stmt")
      }
    }

    /**
      * Normalizes statements by removing PSeq and PEmptyStatement
      */
    def normalize(stmts: Vector[PStatement]): Vector[PStatement] = stmts flatMap {
      case _: PEmptyStmt => Vector()
      case PSeq(s) => normalize(s)
      case s => Vector(s)
    }

    def testProg(inputProg: String, expectedErasedProg: String): Assertion = {
      val inputParseAst = Parser.parseProgram(StringSource(inputProg, "Input Program"))
      val ghostlessProg = inputParseAst match {
        case Right(prog) => ghostLessProg(prog)
        case Left(msgs) => fail(s"Parsing input program has failed with $msgs")
      }
      val expectedParseAst = Parser.parseProgram(StringSource(expectedErasedProg, "Expected Program"))
      expectedParseAst match {
        case Right(prog) => equal(ghostlessProg, prog)
        case Left(msgs) => fail(s"Parsing expected erased program has failed with $msgs")
      }
    }

    private def equal(actual: PProgram, expected: PProgram): Assertion = {
      assert(actual.packageClause == expected.packageClause)
      assert(actual.imports == expected.imports)
      assert(actual.declarations.length == expected.declarations.length)
      actual.declarations.zip(expected.declarations) map {
        case (actualMember, expectedMember) => equal(actualMember, expectedMember)
      }
      Succeeded
    }

    @scala.annotation.tailrec
    private def equal(actual: PMember, expected: PMember): Assertion = {
      (actual, expected) match {
        case (a: PConstDecl, e: PConstDecl) => assert(a == e)
        case (a: PVarDecl, e: PVarDecl) => assert(a == e)
        case (PFunctionDecl(aId, aArgs, aResult, aSpec, aBody), PFunctionDecl(eId, eArgs, eResult, eSpec, eBody)) =>
          assert(aId == eId)
          assert(aArgs == eArgs)
          assert(aResult == eResult)
          assert(aSpec == eSpec)
          equal(aBody, eBody)
        case (PMethodDecl(aId, aRecv, aArgs, aResult, aSpec, aBody), PMethodDecl(eId, eRecv, eArgs, eResult, eSpec, eBody)) =>
          assert(aId == eId)
          assert(aRecv == eRecv)
          assert(aArgs == eArgs)
          assert(aResult == eResult)
          assert(aSpec == eSpec)
          equal(aBody, eBody)
        case (a: PTypeDecl, e: PTypeDecl) => assert(a == e)
        case (PExplicitGhostMember(a), PExplicitGhostMember(e)) => equal(a, e)
        case (a: PFPredicateDecl, e: PFPredicateDecl) => assert(a == e)
        case (a: PMPredicateDecl, e: PMPredicateDecl) => assert(a == e)
      }
    }

    private def equal(actual: Option[(PBodyParameterInfo, PBlock)], expected: Option[(PBodyParameterInfo, PBlock)]): Assertion = {
      assert(actual.isDefined == expected.isDefined)
      actual.zip(expected) foreach {
        case ((aParamInfo, aBlock), (eParamInfo, eBlock)) =>
          assert(aParamInfo == eParamInfo)
          val aNormalizedBlock = normalize(aBlock.stmts)
          val eNormalizedBlock = normalize(eBlock.stmts)
          assert(aNormalizedBlock == eNormalizedBlock)
      }
      Succeeded
    }
  }
}
