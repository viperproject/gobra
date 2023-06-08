package viper.gobra.ast

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.frontend.{PBoolType, PIdnDef, PIntType, PInterfaceType, PTypeElement}
import viper.gobra.frontend.info.base.Type
import viper.gobra.util.TypeBounds

class TypeNodeUnitTests extends AnyFunSuite with Matchers with Inside {
   test ("TypeNode: should correctly substitute simple TypeParameter") {
    val typeNode = Type.TypeParameterT(PIdnDef("x"), Type.InterfaceT(PInterfaceType(Vector(PTypeElement(Vector(PIntType()))), Vector(), Vector()), null))
    val sub: PartialFunction[PIdnDef, Type.Type] = {
      case PIdnDef("z") => Type.StringT
      case PIdnDef("x") => Type.IntT(TypeBounds.DefaultInt)
      case PIdnDef("y") => Type.BooleanT
    }

    typeNode.substitute(sub) should matchPattern {
      case Type.IntT(TypeBounds.DefaultInt) =>
    }
  }

  test("TypeNode: should not substitute anything if type argument is not provided") {
    val typeNode = Type.TypeParameterT(PIdnDef("x"), Type.InterfaceT(PInterfaceType(Vector(PTypeElement(Vector(PIntType()))), Vector(), Vector()), null))
    val sub: PartialFunction[PIdnDef, Type.Type] = {
      case PIdnDef("y") => Type.IntT(TypeBounds.DefaultInt)
    }

    typeNode.substitute(sub) shouldBe typeNode
  }

  test("TypeNode: should correctly substitute in children (single)") {
    val typeNode = Type.MultisetT(Type.TypeParameterT(PIdnDef("x"), Type.InterfaceT(PInterfaceType(Vector(PTypeElement(Vector(PIntType()))), Vector(), Vector()), null)))
    val sub: PartialFunction[PIdnDef, Type.Type] = {
      case PIdnDef("x") => Type.IntT(TypeBounds.DefaultInt)
    }

    typeNode.substitute(sub) should matchPattern {
      case Type.MultisetT(Type.IntT(TypeBounds.DefaultInt)) =>
    }
  }

  test("TypeNode: should correctly substitute in children (multiple)") {
    val typeNode = Type.MathMapT(
      Type.TypeParameterT(PIdnDef("x"), Type.InterfaceT(PInterfaceType(Vector(PTypeElement(Vector(PIntType()))), Vector(), Vector()), null)),
      Type.TypeParameterT(PIdnDef("y"), Type.InterfaceT(PInterfaceType(Vector(PTypeElement(Vector(PBoolType()))), Vector(), Vector()), null))
    )
    val sub: PartialFunction[PIdnDef, Type.Type] = {
      case PIdnDef("x") => Type.IntT(TypeBounds.DefaultInt)
      case PIdnDef("y") => Type.BooleanT
    }

    typeNode.substitute(sub) should matchPattern {
      case Type.MathMapT(Type.IntT(TypeBounds.DefaultInt), Type.BooleanT) =>
    }
  }
}
