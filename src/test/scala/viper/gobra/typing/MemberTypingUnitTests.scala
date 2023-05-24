// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.typing

import org.bitbucket.inkytonik.kiama.util.Positions
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import viper.gobra.ast.frontend.{PPackage, PPkgDef, PProgram}
import viper.gobra.frontend.PackageInfo
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.Config

class MemberTypingUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("TypeChecker: should be able to type non-generic function") {
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("int")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector()))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should be able to type generic function") {
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PSimpleTypeConstraint(PInterfaceType(Vector(), Vector(), Vector())))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      None
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should not accept generic function that uses type parameters that are not defined") {
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PSimpleTypeConstraint(PInterfaceType(Vector(), Vector(), Vector())))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T"))), PNamedParameter(PIdnDef("y"), PNamedOperand(PIdnUse("V")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      None
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  class TestFrontend {
    def singleMemberProgram(member: PMember): PProgram =
      PProgram(
        PPackageClause(PPkgDef("pkg")),
        Vector(),
        Vector(),
        Vector(member)
      )

    def memberTypeInfo(member: PMember): TypeInfoImpl = {
      val program = singleMemberProgram(member)
      val positions = new Positions
      val pkg = PPackage(
        PPackageClause(PPkgDef("pkg")),
        Vector(program),
        new PositionManager(positions),
        new PackageInfo("pkg", "pkg", false)
      )
      val tree = new Info.GoTree(pkg)
      val context = new Info.Context()
      val config = Config()
      new TypeInfoImpl(tree, context)(config)
    }

    def wellDefMember(member: PMember) =
      memberTypeInfo(member).wellDefMember(member)
  }
}
