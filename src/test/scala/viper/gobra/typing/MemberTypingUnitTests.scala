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
import viper.gobra.ast.frontend.{PIntType, PPackage, PPkgDef, PProgram, _}
import viper.gobra.frontend.PackageInfo
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.Config
import viper.gobra.util.TypeBounds

class MemberTypingUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("TypeChecker: should accept function that contains reference of shared variable") {
    // func foo() {
    //   var x@ int
    //   var y = &x
    // }
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(), Vector(PIdnDef("x")), Vector(true)),
        PVarDecl(None, Vector(PReference(PNamedOperand(PIdnUse("x")))), Vector(PIdnDef("y")), Vector(false))
      )))
    )

    assert(frontend.wellDefMember(functionDecl).valid)
  }

  test("TypeChecker: should reject function that contains reference of exclusive variable") {
    // func foo() {
    //   var x int
    //   var y = &x
    // }
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(), Vector(PIdnDef("x")), Vector(false)),
        PVarDecl(None, Vector(PReference(PNamedOperand(PIdnUse("x")))), Vector(PIdnDef("y")), Vector(false))
      )))
    )

    assert(!frontend.wellDefMember(functionDecl).valid)
  }

  test("TypeChecker: should accept valid simple assignment") {
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PNamedParameter(PIdnDef("y"), PIntType())),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("y"))), Vector(PIdnDef("x")), Vector(false)),
      )))
    )

    assert(frontend.wellDefMember(functionDecl).valid)
  }

  test("TypeChecker: should accept valid simple assignment from actual to ghost") {
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PNamedParameter(PIdnDef("y"), PIntType())),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PExplicitGhostStatement(PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("y"))), Vector(PIdnDef("x")), Vector(false)))
      )))
    )

    assert(frontend.wellDefMember(functionDecl).valid)
  }

  test("TypeChecker: should accept valid simple assignment from ghost to ghost") {
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PExplicitGhostStatement(PVarDecl(Some(PIntType()), Vector(PIntLit(BigInt(3))), Vector(PIdnDef("y")), Vector(false))),
        PExplicitGhostStatement(PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("y"))), Vector(PIdnDef("x")), Vector(false)))
      )))
    )

    assert(frontend.wellDefMember(functionDecl).valid)
  }

  test("TypeChecker: should reject invalid simple assignment from ghost to actual") {
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PExplicitGhostStatement(PVarDecl(Some(PIntType()), Vector(PIntLit(BigInt(3))), Vector(PIdnDef("y")), Vector(false))),
        PVarDecl(Some(PIntType()), Vector(), Vector(PIdnDef("x")), Vector(false)),
        PAssignment(Vector(PNamedOperand(PIdnUse("y"))), Vector(PNamedOperand(PIdnUse("x"))))
      )))
    )

    assert(!frontend.wellDefMember(functionDecl).valid)
  }

  test("TypeChecker: should not reject ghost return in ghost function") {
    val functionDecl = PExplicitGhostMember(PFunctionDecl(
      PIdnDef("foo"),
      Vector(),
      PResult(Vector(PUnnamedParameter(PIntType()))),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PReturn(Vector(
          PConditional(PEquals(PIntLit(0), PIntLit(0)), PIntLit(1), PIntLit(2))
        ))
      )))
    ))

    assert(frontend.wellDefMember(functionDecl).valid)
  }

  class TestFrontend {
    def singleMemberProgram(members: Vector[PMember]): PProgram =
      PProgram(
        PPackageClause(PPkgDef("pkg")),
        Vector(),
        Vector(),
        members
      )

    def memberTypeInfo(member: PMember)(otherMembers: Vector[PMember]): TypeInfoImpl = {
      val program = singleMemberProgram(member +: otherMembers )
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

    def wellDefMember(member: PMember, otherMembers: Vector[PMember] = Vector()) =
      memberTypeInfo(member)(otherMembers).wellDefMember(member)
  }
}
