package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

class ExprTypingUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("TypeChecker: should classify an integer literal as integer") {
    frontend.exprType(PIntLit(42))() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should not classify integer literals as ghost") {
    assert(!frontend.isGhostExpr(PIntLit(42))())
  }

  test("TypeChecker: should classify a Boolean literal as Boolean") {
    frontend.exprType(PBoolLit(false))() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should not classify Boolean literals as ghost") {
    assert(!frontend.isGhostExpr(PBoolLit(true))())
  }

  test("TypeChecker: should classify a named operand by its type") {
    val inArgs = Vector(PNamedParameter(PIdnDef("x"), PIntType(), false))
    frontend.exprType(PNamedOperand(PIdnUse("x")))(inArgs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should classify a ghost input parameter as being ghost") {
    val inArgs = Vector(PExplicitGhostParameter(PNamedParameter(PIdnDef("x"), PIntType(), false)))
    assert (frontend.isGhostExpr(PNamedOperand(PIdnUse("x")))(inArgs))
  }

  test("TypeChecker: should classify a sequence literal as ghost") {
    assert (frontend.isGhostExpr(PSequenceLiteral(PBoolType(), Vector()))())
  }

  test("TypeChecker: should correctly type a Boolean sequence") {
    frontend.exprType(PSequenceLiteral(PBoolType(), Vector()))() should matchPattern {
      case Type.SequenceT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should classify an sequence indexed expression as ghost") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PIntLit(0))
    assert(frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should correctly type a simple sequence indexed expression") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PIntLit(0))
    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: mark a sequence indexed expression with an incorrect left-hand side as not well-defined") {
    val expr = PIndexedExp(PIntLit(42), PIntLit(0))
    assert(!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: mark a sequence indexed expression with an incorrect right-hand side as not well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PBoolLit(false))
    assert(!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type chained sequence indexing expressions") {
    val inArgs = Vector(
      PExplicitGhostParameter(
        PNamedParameter(PIdnDef("xs"), PSequenceType(PSequenceType(PIntType())), false)
      )
    )
    val expr = PIndexedExp(
      PIndexedExp(PNamedOperand(PIdnUse("xs")), PIntLit(2)),
      PIntLit(4)
    )
    frontend.exprType(expr)(inArgs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should classify a sequence slice expression as ghost") {
    val inArgs = Vector(
      PExplicitGhostParameter(
        PNamedParameter(PIdnDef("xs"), PSequenceType(PIntType()), false)
      )
    )
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      Some(PIntLit(2)),
      Some(PIntLit(4)),
      None
    )
    frontend.isGhostExpr(expr)(inArgs) should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(2)), Some(PIntLit(4)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should not classify a sequence slice expression with a capacity as being well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(2)), Some(PIntLit(4)), Some(PIntLit(6)))

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case false =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression with a missing 'low' index as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, None, Some(PIntLit(4)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression with a missing 'high' index as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(1)), None, None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression with a missing 'low' and 'high' index as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, None, None, None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should not allow the 'low' index of a slice expression to be anything other than an integer") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PBoolLit(false)), Some(PIntLit(2)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case false =>
    }
  }

  test("TypeChecker: should not allow the 'high' index of a slice expression to be anything other than an integer") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(2)), Some(PBoolLit(false)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case false =>
    }
  }

  test("TypeChecker: should correctly identify the type of a Boolean sequence slice expression") {
    val base = PSequenceLiteral(PBoolType(), Vector(PBoolLit(true), PBoolLit(false)))
    val expr = PSliceExp(base, Some(PIntLit(1)), Some(PIntLit(4)), None)

    frontend.exprType(expr)() should matchPattern {
      case Type.SequenceT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should correctly identify the type of a nested sequence slice expression") {
    val base = PSequenceLiteral(PSequenceType(PIntType()), Vector())
    val expr = PSliceExp(base, Some(PIntLit(1)), Some(PIntLit(4)), None)

    frontend.exprType(expr)() should matchPattern {
      case Type.SequenceT(Type.SequenceT(Type.IntT)) =>
    }
  }

  test("TypeChecker: should classify an (integer) set literal as ghost") {
    val expr = PSetLiteral(PIntType(), Vector())
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should correctly type an (empty) integer set literal") {
    val expr = PSetLiteral(PIntType(), Vector())
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type a nested (empty) set literal") {
    val expr = PSetLiteral(PSetType(PBoolType()), Vector())
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.SetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should classify empty Boolean set literal as well-defined") {
    val expr = PSetLiteral(PIntType(), Vector())
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a singleton (integer) set literal with wrong subexpressions") {
    val expr = PSetLiteral(PIntType(), Vector(PBoolLit(false)))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a set literal with (right and) wrong members") {
    val expr = PSetLiteral(PIntType(), Vector(
      PIntLit(1),
      PIntLit(5),
      PBoolLit(false),
      PIntLit(7)
    ))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a non-empty set literal") {
    val expr = PSetLiteral(PIntType(), Vector(
      PIntLit(1),
      PIntLit(5),
      PIntLit(7)
    ))
    assert (frontend.wellDefExpr(expr)().valid)
  }


  /* * Stubs, mocks, and other test setup  */

  class TestFrontend {
    def stubProgram(inArgs: Vector[PParameter], body : PStatement) : PProgram = PProgram(
      PPackageClause(PPkgDef("pkg")),
      Vector(),
      Vector(PMethodDecl(
        PIdnDef("foo"),
        PUnnamedReceiver(PMethodReceiveName(PNamedOperand(PIdnUse("self")))),
        inArgs,
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), true),
        Some(PBlock(Vector(body)))
      )),
      new PositionManager()
    )

    def singleExprProgram(inArgs: Vector[PParameter], expr : PExpression) : PProgram = {
      val stmt = PShortVarDecl(Vector(expr), Vector(PIdnUnk("n")), Vector(false))
      stubProgram(inArgs, stmt)
    }

    def singleExprTypeInfo(inArgs: Vector[PParameter], expr : PExpression) : TypeInfoImpl = {
      val program = singleExprProgram(inArgs, expr)
      val tree = new Info.GoTree(program)
      new TypeInfoImpl(tree)
    }

    def exprType(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Type.Type =
      singleExprTypeInfo(inArgs, expr).exprType(expr)

    def isGhostExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Boolean =
      singleExprTypeInfo(inArgs, expr).isExprGhost(expr)

    def wellDefExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) =
      singleExprTypeInfo(inArgs, expr).wellDefExpr(expr)
  }
}
