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
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("any"))))),
        Vector(),
        Vector()
      ))),
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
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("any"))))),
        Vector(),
        Vector()
      ))),
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
        PTypeParameter(PIdnDef("T"), PInterfaceType(
          Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("any"))))),
          Vector(),
          Vector()
        )),
        PTypeParameter(PIdnDef("V"), PInterfaceType(
          Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("any"))))),
          Vector(),
          Vector()
        ))
      ),
      PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("x"), PNamedOperand(PIdnUse("T"))))))),
      PIdnDef("Bar")
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept valid assignment of constant to simple type parameter") {
    // func foo[T int]() {
    //	 var _ T = 3 // valid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PIntType()))),
        Vector(),
        Vector()
      ))),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PIntLit(BigInt(3))), Vector(PWildcard()), Vector())
      )))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should not accept invalid assignment of constant to simple type parameter") {
    // func foo[T int]() {
    //	 var _ T = false // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PIntType()))),
        Vector(),
        Vector()
      ))),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PBoolLit(false)), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should not accept invalid assignment of constant to union type parameter") {
    // func foo[T int | bool]() {
    //	 var _ T = 3 // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PIntType(), PBoolType()))),
        Vector(),
        Vector()
      ))),
      Vector(),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PIntLit(BigInt(3))), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should not accept invalid assignment of generic function parameter to static type") {
    // func foo[T int](x T) {
    //	 var _ int = x // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PIntType()))),
        Vector(),
        Vector()
      ))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("x"))), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept valid assignment of generic interface parameter to interface type") {
    // func foo[T interface{ m(); n() }](x T) {
    //	 var _ interface{ m() } = x // valid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(Vector(), Vector(
        PMethodSig(PIdnDef("m"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), Vector(), Vector()), isGhost = false),
        PMethodSig(PIdnDef("n"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), Vector(), Vector()), isGhost = false),
      ), Vector()))),
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

  test("TypeChecker: should not accept invalid assignment of generic union parameter to static type") {
    // func foo[T int | bool](x T) {
    //	 var _ int = x // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(PTypeElement(Vector(PIntType(), PBoolType()))),
        Vector(),
        Vector()
      ))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("x"))), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should not accept invalid assignment of interface parameter to generic interface type") {
    // func foo[T interface{ m() }](x interface {
    //	 m()
    //	 n()
    // }) {
    //	 var _ T = x // invalid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
          Vector(),
          Vector(PMethodSig(
            PIdnDef("m"),
            Vector(),
            PResult(Vector()),
            PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
            isGhost = false
          )),
          Vector()
      ))),
      Vector(PNamedParameter(PIdnDef("x"), PInterfaceType(
        Vector(),
        Vector(
          PMethodSig(
            PIdnDef("m"),
            Vector(),
            PResult(Vector()),
            PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
            isGhost = false
          ),
          PMethodSig(
            PIdnDef("n"),
            Vector(),
            PResult(Vector()),
            PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
            isGhost = false
          )
        ),
        Vector()
      ))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PIntType()), Vector(PNamedOperand(PIdnUse("x"))), Vector(PWildcard()), Vector())
      )))
    )

    assert(!frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept assignment to identical type parameter types") {
    // func foo[T interface { m() }](x T) {
    //	 var _ T = x // valid
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(
        Vector(),
        Vector(PMethodSig(
          PIdnDef("m"),
          Vector(),
          PResult(Vector()),
          PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
          isGhost = false
        )),
        Vector()
      ))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(Some(PNamedOperand(PIdnUse("T"))), Vector(PNamedOperand(PIdnUse("x"))), Vector(PWildcard()), Vector())
      )))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept comparison of int and int type parameter") {
    // func foo[T int](x T) {
    //	 var _ = (x == 3)
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(Vector(PTypeElement(Vector(PIntType()))), Vector(), Vector()))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(None, Vector(PEquals(PNamedOperand(PIdnUse("x")), PIntLit(BigInt(3)))), Vector(PWildcard()), Vector())
      )))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept comparison of two comparable parameters") {
    // func foo[T comparable](x T, y T) {
    //	 var _ = (x == y)
    // }
    val member = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("comparable"))))), Vector(), Vector()))),
      Vector(PNamedParameter(PIdnDef("x"), PNamedOperand(PIdnUse("T"))), PNamedParameter(PIdnDef("y"), PNamedOperand(PIdnUse("T")))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      Some(PBodyParameterInfo(Vector()), PBlock(Vector(
        PVarDecl(None, Vector(PEquals(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y")))), Vector(PWildcard()), Vector())
      )))
    )

    assert(frontend.wellDefMember(member).valid)
  }

  test("TypeChecker: should accept instantiation of generic function with a type parameter") {
    // type Bar[T interface{ m() }] struct {}
    var typeDecl = PTypeDef(
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(Vector(), Vector(PMethodSig(
        PIdnDef("m"),
        Vector(),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
        isGhost = false
      )), Vector()))),
      PStructType(Vector()),
      PIdnDef("Bar")
    )

    // func foo[T interface{ m() }](x Bar[T]) {}
    val functionDecl = PFunctionDecl(
      PIdnDef("foo"),
      Vector(PTypeParameter(PIdnDef("T"), PInterfaceType(Vector(), Vector(PMethodSig(
        PIdnDef("m"),
        Vector(),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
        isGhost = false
      )), Vector()))),
      Vector(PNamedParameter(PIdnDef("x"), PParameterizedTypeName(PNamedOperand(PIdnUse("Bar")), Vector(PNamedOperand(PIdnUse("T")))))),
      PResult(Vector()),
      PFunctionSpec(Vector(), Vector(), Vector(), Vector()),
      None
    )

    assert(frontend.wellDefMember(functionDecl, Vector(typeDecl)).valid)
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
