// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.typing

import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import org.bitbucket.inkytonik.kiama.util.Positions
import viper.gobra.frontend.PackageInfo
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.Config

class StmtTypingUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("TypeChecker: should detect labeled continues outside of loops") {
    assert(!frontend.wellDefStmt(PContinue(Some(PLabelUse("l"))))().valid)
  }

  test("TypeChecker: should detect continues outside of loops") {
    assert(!frontend.wellDefStmt(PContinue(None))().valid)
  }

  test("TypeChecker: should detect labeled breaks outside of loops") {
    assert(!frontend.wellDefStmt(PBreak(Some(PLabelUse("l"))))().valid)
  }

  test("TypeChecker: should detect breaks outside of loops") {
    assert(!frontend.wellDefStmt(PBreak(None))().valid)
  }

  test("TypeChecker: valid for with a break statement") {
    assert(frontend.wellDefStmt(
      PForStmt(
        None,
        PBoolLit(true),
        None,
        PLoopSpec(Vector.empty, None),
        PBlock(Vector[PStatement](PBreak(None)))
      ))().valid)
  }

  class TestFrontend {

    /**
      * Creates the program:
      * 
      * package pkg
      * 
      * func foo(<inArgs>) {
      *     <body>
      * }
      */
    def singleStmtProgram(inArgs: Vector[(PParameter, Boolean)], body : PStatement) : PProgram =
      PProgram(
        PPackageClause(PPkgDef("pkg")),
        Vector(),
        Vector(),
        Vector(),
        Vector(PFunctionDecl(
          PIdnDef("foo"),
          inArgs.map(_._1),
          PResult(Vector()),
          PFunctionSpec(Vector(), Vector(), Vector(), Vector(), Vector(), isPure = false),
          Some(PBodyParameterInfo(inArgs.collect{ case (n: PNamedParameter, true) => PIdnUse(n.id.name) }), PBlock(Vector(body)))
        ))
      )

    def singleStmtTypeInfo(inArgs: Vector[(PParameter, Boolean)], stmt : PStatement) : TypeInfoImpl = {
      val program = singleStmtProgram(inArgs, stmt)
      val positions = new Positions
      val pkg = PPackage(
        PPackageClause(PPkgDef("pkg")),
        Vector(program),
        new PositionManager(positions),
        new PackageInfo("pkg", "pkg", false)
      )
      val tree = new Info.GoTree(pkg)
      val config = Config()
      new TypeInfoImpl(tree, Map.empty)(config)
    }

    def wellDefStmt(stmt : PStatement)(inArgs: Vector[(PParameter, Boolean)] = Vector()) =
      singleStmtTypeInfo(inArgs, stmt).wellDefStmt(stmt)
  }
}
