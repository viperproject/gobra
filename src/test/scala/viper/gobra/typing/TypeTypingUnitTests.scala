// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.typing

import org.bitbucket.inkytonik.kiama.util.Positions
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.Inside
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds.DefaultInt

class TypeTypingUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Typing: should correctly type an integer sequence type") {
    val t = PSequenceType(PIntType())
    frontend.typType(t) should matchPattern {
      case Type.SequenceT(Type.IntT(DefaultInt)) =>
    }
  }

  test("Typing: should classify an integer sequence as ghost") {
    val t = PSequenceType(PIntType())
    assert (frontend.isGhostType(t))
  }

  test("Typing: should mark an Boolean sequence as well-defined") {
    val t = PSequenceType(PBoolType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should correctly type an integer set type") {
    val t = PSetType(PIntType())
    frontend.typType(t) should matchPattern {
      case Type.SetT(Type.IntT(DefaultInt)) =>
    }
  }

  test("Typing: should classify an integer set as ghost") {
    val t = PSetType(PIntType())
    assert (frontend.isGhostType(t))
  }

  test("Typing: should mark an Boolean set as well-defined") {
    val t = PSetType(PBoolType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should classify any multiset as ghost") {
    val t = PMultisetType(PIntType())
    assert (frontend.isGhostType(t))
  }

  test("Typing: should let a normal use of the multiset type be well-defined") {
    val t = PMultisetType(PIntType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should correctly type a simple multiset type") {
    val t = PMultisetType(PBoolType())
    frontend.typType(t) should matchPattern {
      case Type.MultisetT(Type.BooleanT) =>
    }
  }

  test("Typing: should correctly type a nested multiset type") {
    val t = PMultisetType(PMultisetType(PIntType()))
    frontend.typType(t) should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.IntT(DefaultInt))) =>
    }
  }

  test("Typing: should let two integer sequence types be comparable") {
    val t1 = PSequenceType(PIntType())
    val t2 = PSequenceType(PIntType())
    assert (frontend.areComparable(t1, t2))
  }

  test("Typing: should not let an integer sequence and Boolean sequence be comparable") {
    val t1 = PSequenceType(PIntType())
    val t2 = PSequenceType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should let two Boolean set types be comparable") {
    val t1 = PSetType(PBoolType())
    val t2 = PSetType(PBoolType())
    assert (frontend.areComparable(t1, t2))
  }

  test("Typing: should not let a Boolean set type and integer set type be comparable") {
    val t1 = PSetType(PIntType())
    val t2 = PSetType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should let two integer multiset types be comparable") {
    val t1 = PMultisetType(PIntType())
    val t2 = PMultisetType(PIntType())
    assert (frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the Boolean multiset type and integer multiset type be comparable (1)") {
    val t1 = PMultisetType(PIntType())
    val t2 = PMultisetType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the Boolean multiset type and integer multiset type be comparable (2)") {
    val t1 = PMultisetType(PBoolType())
    val t2 = PMultisetType(PIntType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the set and multiset types be comparable (1)") {
    val t1 = PSetType(PBoolType())
    val t2 = PMultisetType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the set and multiset types be comparable (2)") {
    val t1 = PMultisetType(PBoolType())
    val t2 = PSetType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the sequence type and set type be comparable (1)") {
    val t1 = PSetType(PBoolType())
    val t2 = PSequenceType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the sequence type and set type be comparable (2)") {
    val t1 = PSequenceType(PBoolType())
    val t2 = PSetType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the sequence type and multiset type be comparable (1)") {
    val t1 = PMultisetType(PBoolType())
    val t2 = PSequenceType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let the sequence type and multiset type be comparable (2)") {
    val t1 = PSequenceType(PBoolType())
    val t2 = PMultisetType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should mark a simple integer array type as non-ghost") {
    val t = PArrayType(PIntLit(42), PIntType())
    assert (!frontend.isGhostType(t))
  }

  test("Typing: should mark a simple sequence array type as ghost") {
    val t = PArrayType(PIntLit(12), PSequenceType(PBoolType()))
    assert (frontend.isGhostType(t))
  }

  test("Typing: should let a simple multidimensional array not be marked as ghost") {
    val t = PArrayType(PIntLit(1), PArrayType(PIntLit(2), PIntType()))
    assert (!frontend.isGhostType(t))
  }

  test("Typing: should let a simple integer array type be well-defined") {
    val t = PArrayType(PIntLit(42), PIntType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should not let an integer array be well-defined if its length is not a constant expression (1)") {
    val t = PArrayType(
      PMultiplicity(PIntLit(1), PLiteral.set(PIntType(), Vector())),
      PIntType()
    )

    assert (!frontend.isWellDef(t).valid)
  }

  test("Typing: should not let an integer array be well-defined if its length is not a constant expression (2)") {
    val t = PArrayType(
      PLength(PRangeSequence(PIntLit(1), PIntLit(10))),
      PBoolType()
    )

    assert (!frontend.isWellDef(t).valid)
  }

  test("Typing: should let a very simple multidimensional integer array be well-defined") {
    val t = PArrayType(PIntLit(1), PArrayType(PIntLit(2), PIntType()))
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should assign the correct type to a simple integer array") {
    val t = PArrayType(PIntLit(42), PIntType())

    frontend.typType(t) should matchPattern {
      case Type.ArrayT(n, Type.IntT(_)) if n == BigInt(42) =>
    }
  }

  test("Typing: should correctly type a Boolean array with a slightly more complex length") {
    val t = PArrayType(
      PMul(PAdd(PIntLit(2), PIntLit(3)), PIntLit(4)),
      PBoolType()
    )

    frontend.typType(t) should matchPattern {
      case Type.ArrayT(n, Type.BooleanT)
        if n == BigInt(20) =>
    }
  }

  test("Typing: should correctly type a very simple multidimensional integer array") {
    val t = PArrayType(PIntLit(1), PArrayType(PIntLit(2), PIntType()))

    frontend.typType(t) should matchPattern {
      case Type.ArrayT(m, Type.ArrayT(n, Type.IntT(_)))
        if m == BigInt(1) && n == BigInt(2) =>
    }
  }

  test("Typing: should correctly type a simple sequence array type") {
    val t = PArrayType(PIntLit(12), PSequenceType(PBoolType()))

    frontend.typType(t) should matchPattern {
      case Type.ArrayT(n, Type.SequenceT(Type.BooleanT))
        if n == BigInt(12) =>
    }
  }

  test("Typing: should not type an (integer) array type with a negative length") {
    val t = PArrayType(PIntLit(-12), PIntType())
    assert (!frontend.isWellDef(t).valid)
  }

  test("Typing: should type an (integer) array type with a length of zero") {
    val t = PArrayType(PIntLit(0), PIntType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should let a simply option type be well-defined") {
    val t = POptionType(PIntType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should mark option types as ghost") {
    val t = POptionType(PIntType())
    assert (frontend.isGhostType(t))
  }

  test("Typing: should correctly type a simple option type") {
    val t = POptionType(POptionType(PBoolType()))

    frontend.typType(t) should matchPattern {
      case Type.OptionT(Type.OptionT(Type.BooleanT)) =>
    }
  }

  test("Typing: should mark a simple integer slice type as well-defined") {
    val t = PSliceType(PIntType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should mark a slightly more complex slice type as well-defined") {
    val t = PSliceType(PSequenceType(PSetType(PBoolType())))
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should mark a nested slice type as well-defined") {
    val t = PSliceType(PSliceType(PIntType()))
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should not mark a slice type as well-defined if there is a typing problem in the inner type") {
    val t = PSliceType(PArrayType(PIntLit(-4), PIntType()))
    assert (!frontend.isWellDef(t).valid)
  }

  test("Typing: should mark a simple integer slice type as non-ghost") {
    val t = PSliceType(PIntType())
    assert (!frontend.isGhostType(t))
  }

  test("Typing: should mark a sequence slice type as ghost") {
    val t = PSliceType(PSequenceType(PBoolType()))
    assert (frontend.isGhostType(t))
  }

  test("Typing: should correctly type a simple integer slice type") {
    val t = PSliceType(PIntType())

    frontend.typType(t) should matchPattern {
      case Type.SliceT(Type.IntT(_)) =>
    }
  }

  test("Typing: should correctly type a slightly more complex slice type") {
    val t = PSliceType(PSetType(PSequenceType(PBoolType())))

    frontend.typType(t) should matchPattern {
      case Type.SliceT(Type.SetT(Type.SequenceT(Type.BooleanT))) =>
    }
  }

  test("Typing: should correctly type a nested slice type") {
    val t = PSliceType(PSliceType(PSliceType(PIntType())))

    frontend.typType(t) should matchPattern {
      case Type.SliceT(Type.SliceT(Type.SliceT(Type.IntT(_)))) =>
    }
  }

  test("Typing: should not let a(ny) slice type be comparable to itself") {
    val t1 = PSliceType(PIntType())
    val t2 = PSliceType(PIntType())
    assert (!frontend.areComparable(t1, t2)) // i.e., slices are always incomparable
  }

  test("Typing: should not let two slice type be comparable if their inner types are incomparable (1)") {
    val t1 = PSliceType(PIntType())
    val t2 = PSliceType(PBoolType())
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let two slice type be comparable if their inner types are incomparable (2)") {
    val t1 = PSliceType(PIntType())
    val t2 = PSliceType(PSliceType(PIntType()))
    assert (!frontend.areComparable(t1, t2))
  }

  test("Typing: should not let slices be comparable to arrays") {
    val t1 = PSliceType(PIntType())
    val t2 = PArrayType(PIntLit(0), PIntType())
    assert (!frontend.areComparable(t1, t2))
  }



  /* ** Stubs, mocks, and other test setup  */

  class TestFrontend {
    private def stubParams(xs : Vector[PType]) : Vector[PNamedParameter] = {
      xs.zipWithIndex.foldLeft(Vector[PNamedParameter]()) {
        case (ys, (t, i)) => ys ++ Vector(PNamedParameter(PIdnDef("n" + i), t))
      }
    }

    private def stubProgram(ts : Vector[PType]) = PProgram(
      PPackageClause(PPkgDef("pkg")),
      Vector(),
      Vector(),
      Vector(),
      Vector(PMethodDecl(
        PIdnDef("foo"),
        PUnnamedReceiver(PMethodReceiveName(PNamedOperand(PIdnUse("self")))),
        stubParams(ts),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), Vector(), Vector(), Vector(), isPure = true),
        None
      ))
    )

    private def typeInfo(ts : Vector[PType]) : TypeInfoImpl = {
      val program = stubProgram(ts)
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

    def areComparable(t1 : PType, t2 : PType) : Boolean = {
      val T1 = typType(t1)
      val T2 = typType(t2)
      typeInfo(Vector(t1, t2)).comparableTypes((T1, T2))
    }

    def isGhostType(t : PType) : Boolean = typeInfo(Vector(t)).isTypeGhost(t)
    def isWellDef(t : PType) = typeInfo(Vector(t)).wellDefType(t)
    def typType(t : PType) : Type.Type = typeInfo(Vector(t)).symbType(t)
  }
}
