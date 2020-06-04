package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

class TypeTypingUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Typing: should correctly type an integer sequence type") {
    val t = PSequenceType(PIntType())
    frontend.typType(t) should matchPattern {
      case Type.SequenceT(Type.IntT) =>
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
      case Type.SetT(Type.IntT) =>
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
    val t = PMultiSetType(PIntType())
    assert (frontend.isGhostType(t))
  }

  test("Typing: should let a normal use of the multiset type be well-defined") {
    val t = PMultiSetType(PIntType())
    assert (frontend.isWellDef(t).valid)
  }

  test("Typing: should correctly type a simple multiset type") {
    val t = PMultiSetType(PBoolType())
    frontend.typType(t) should matchPattern {
      case Type.MultisetT(Type.BooleanT) =>
    }
  }

  test("Typing: should correctly type a nested multiset type") {
    val t = PMultiSetType(PMultiSetType(PIntType()))
    frontend.typType(t) should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.IntT)) =>
    }
  }



  /* ** Stubs, mocks, and other test setup  */

  class TestFrontend {
    private def stubProgram(t : PType) = PProgram(
      PPackageClause(PPkgDef("pkg")),
      Vector(),
      Vector(PMethodDecl(
        PIdnDef("foo"),
        PUnnamedReceiver(PMethodReceiveName(PNamedOperand(PIdnUse("self")))),
        Vector(PNamedParameter(PIdnDef("n"), t, false)),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), true),
        None
      )),
      new PositionManager()
    )

    private def typeInfo(t : PType) : TypeInfoImpl = {
      val program = stubProgram(t)
      val tree = new Info.GoTree(program)
      new TypeInfoImpl(tree)
    }

    def typType(t : PType) : Type.Type = typeInfo(t).typ(t)
    def isGhostType(t : PType) : Boolean = typeInfo(t).isTypeGhost(t)
    def isWellDef(t : PType) = typeInfo(t).wellDefType(t)
  }
}
