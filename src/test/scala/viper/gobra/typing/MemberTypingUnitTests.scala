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
import viper.gobra.util.TypeBounds

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
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PNamedOperand(PIdnUse("any")))))),
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
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PNamedOperand(PIdnUse("any")))))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T"))), PNamedParameter(PIdnDef("y"), PNamedOperand(PIdnUse("V")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      None
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept struct type definition") {
    val member = PTypeDef(
      Vector(),
      PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("x"), PNamedOperand(PIdnUse("int"))))))),
      PIdnDef("Bar")
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept generic type definition") {
    val member = PTypeDef(
      Vector(
        PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PNamedOperand(PIdnUse("any"))))),
        PTypeParameter(PIdnDef("V"), PTypeElement(Vector(PNamedOperand(PIdnUse("any")))))
      ),
      PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("x"), PNamedOperand(PIdnUse("T"))))))),
      PIdnDef("Bar")
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: foo1") {
    // func foo[T int]() {
    //	 var _ T = 3 // valid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PIntType())))),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PIntLit(BigInt(3))), Vector(PWildcard()), Vector())
      )))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: foo2") {
    // func foo[T int]() {
    //	 var _ T = false // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PIntType())))),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PBoolLit(false)), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: foo3") {
    // func foo[T int | bool]() {
    //	 var _ T = 3 // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PIntType(), PBoolType())))),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PIntLit(BigInt(3))), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: foo41") {
    // func foo[T int](x T) {
    //	 var _ int = x // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PIntType())))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("x"))), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: foo42") {
    // func foo[T interface{ m(); n() }](x T) {
    //	 var _ interface{ m() } = x // valid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PInterfaceType(Vector(), Vector(
        PMethodSig(PIdnDef("m"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), Vector(), Vector()), isGhost = false),
        PMethodSig(PIdnDef("n"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), Vector(), Vector()), isGhost = false),
      ), Vector()))))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(
          Some(PInterfaceType(Vector(), Vector(
            PMethodSig(PIdnDef("m"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), Vector(), Vector()), isGhost = false),
          ), Vector())),
          Vector(PNamedOperand(PIdnUse("x"))),
          Vector(PWildcard()), Vector())
      )))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: foo5") {
    // func foo5[T int | bool](x T) {
    //	 var _ int = x // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PTypeElement(Vector(PIntType(), PBoolType())))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("x"))), Vector(PWildcard()), Vector())
      )))
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
