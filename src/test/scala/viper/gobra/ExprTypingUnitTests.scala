package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

class ExprTypingUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("ExprTypeChecker: should classify an integer literal as integer") {
    frontend.exprType(PIntLit(42))() should matchPattern {
      case Type.IntT =>
    }
  }

  test("ExprTypeChecker: should not classify integer literals as ghost") {
    assert(!frontend.isGhostExpr(PIntLit(42))())
  }

  test("ExprTypeChecker: should classify a Boolean literal as Boolean") {
    frontend.exprType(PBoolLit(false))() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("ExprTypeChecker: should not classify Boolean literals as ghost") {
    assert(!frontend.isGhostExpr(PBoolLit(true))())
  }

  test("ExprTypeChecker: should classify a named operand by its type") {
    val inArgs = Vector(PNamedParameter(PIdnDef("x"), PIntType(), false))
    frontend.exprType(PNamedOperand(PIdnUse("x")))(inArgs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("ExprTypeChecker: should classify a ghost input parameter as being ghost") {
    val inArgs = Vector(PExplicitGhostParameter(PNamedParameter(PIdnDef("x"), PIntType(), false)))
    assert(frontend.isGhostExpr(PNamedOperand(PIdnUse("x")))(inArgs))
  }

  test("ExprTypeChecker: should classify a sequence literal as ghost") {
    assert(frontend.isGhostExpr(PSequenceLiteral(PBoolType(), Vector()))())
  }

  test("ExprTypeChecker: should correctly type a Boolean sequence") {
    frontend.exprType(PSequenceLiteral(PBoolType(), Vector()))() should matchPattern {
      case Type.SequenceT(Type.BooleanT) =>
    }
  }

  test("ExprTypeChecker: should classify an sequence indexed expression as ghost") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PIntLit(0))
    assert(frontend.isGhostExpr(expr)())
  }

  test("ExprTypeChecker: should correctly type a simple sequence indexed expression") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PIntLit(0))
    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("ExprTypeChecker: mark a sequence indexed expression with an incorrect left-hand side as not well-defined") {
    val expr = PIndexedExp(PIntLit(42), PIntLit(0))
    assert(!frontend.wellDefExpr(expr)().valid)
  }

  test("ExprTypeChecker: mark a sequence indexed expression with an incorrect right-hand side as not well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PBoolLit(false))
    assert(!frontend.wellDefExpr(expr)().valid)
  }

  test("ExprTypeChecker: should correctly type chained sequence indexing expressions") {
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
