package viper.gobra.erasing

import org.bitbucket.inkytonik.kiama.util.{Positions, StringSource}
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config, Parser}
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter

class GhostErasureUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Ghost Erasure: variable declaration should not have a trailing equal sign") {
    // var value int
    val decl = PVarDecl(Some(PIntType()), Vector(), Vector(PIdnDef("value")), Vector(false))
    frontend.ghostLessStmt(decl)() should matchPattern {
      case d: PVarDecl if d == decl =>
    }
  }

  test("Ghost Erasure: calls to (pure) ghost functions should be removed") {
    // ghost (pure) func test() (res bool) {
    //    return true
    // }
    // func main() {
    //     test()
    //     var t1 = test()
    //     t2 := test()
    // }
    val modes: Set[Boolean] = Set(false, true)
    modes.foreach(isPure => {
      val testFunc = PExplicitGhostMember(PFunctionDecl(
        PIdnDef("test"),
        Vector(),
        PResult(Vector(PNamedParameter(PIdnDef("res"), PBoolType()))),
        PFunctionSpec(Vector(), Vector(), isPure),
        Some((
          PBodyParameterInfo(Vector()),
          PBlock(Vector(PReturn(Vector(PBoolLit(true))))))
        )))
      val inputMainFunc = PFunctionDecl(
        PIdnDef("main"),
        Vector(),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), false),
        Some((
          PBodyParameterInfo(Vector()),
          PBlock(Vector(
            PExpressionStmt(PInvoke(PNamedOperand(PIdnUse("test")), Vector())),
            PVarDecl(None, Vector(PInvoke(PNamedOperand(PIdnUse("test")), Vector())), Vector(PIdnDef("t1")), Vector(false)),
            PShortVarDecl(Vector(PInvoke(PNamedOperand(PIdnUse("test")), Vector())), Vector(PIdnUnk("t2")), Vector(false))
          ))
        )))
      val inputProgram = PProgram(
        PPackageClause(PPkgDef("pkg")),
        Vector(),
        Vector(testFunc, inputMainFunc)
      )
      frontend.ghostLessProg(inputProgram) should matchPattern {
        case PProgram(_, _, Vector(PFunctionDecl(PIdnDef("main"), _, _, _, Some((_, b))))) if b.nonEmptyStmts == Vector() =>
      }
    })
  }

  test("Ghost Erasure: calls to (pure) ghost functions with multiple return values should be removed") {
    // ghost (pure) func test() (res1 bool, res2 int) {
    //    return true, 42
    // }
    // func main() {
    //     test()
    //     var t1, t2 = test()
    //     t3, t4 := test()
    // }
    val modes: Set[Boolean] = Set(false, true)
    modes.foreach(isPure => {
      val testFunc = PExplicitGhostMember(PFunctionDecl(
        PIdnDef("test"),
        Vector(),
        PResult(Vector(
          PNamedParameter(PIdnDef("res1"), PBoolType()),
          PNamedParameter(PIdnDef("res2"), PIntType()))),
        PFunctionSpec(Vector(), Vector(), isPure),
        Some((
          PBodyParameterInfo(Vector()),
          PBlock(Vector(PReturn(Vector(PBoolLit(true), PIntLit(42))))))
        )))
      val inputMainFunc = PFunctionDecl(
        PIdnDef("main"),
        Vector(),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), false),
        Some((
          PBodyParameterInfo(Vector()),
          PBlock(Vector(
            PExpressionStmt(PInvoke(PNamedOperand(PIdnUse("test")), Vector())),
            PVarDecl(None, Vector(PInvoke(PNamedOperand(PIdnUse("test")), Vector())), Vector(PIdnDef("t1"), PIdnDef("t2")), Vector(false, false)),
            PShortVarDecl(Vector(PInvoke(PNamedOperand(PIdnUse("test")), Vector())), Vector(PIdnUnk("t3"), PIdnUnk("t4")), Vector(false, false))
          ))
        )))
      val inputProgram = PProgram(
        PPackageClause(PPkgDef("pkg")),
        Vector(),
        Vector(testFunc, inputMainFunc)
      )
      frontend.ghostLessProg(inputProgram) should matchPattern {
        case PProgram(_, _, Vector(PFunctionDecl(PIdnDef("main"), _, _, _, Some((_, b))))) if b.nonEmptyStmts == Vector() =>
      }
    })
  }

  test("if else if else should correctly be erased") {
    // if true {} else if (false) {} else {}
    val input = PIfStmt(Vector(
      PIfClause(None, PBoolLit(true), PBlock(Vector())),
      PIfClause(None, PBoolLit(false), PBlock(Vector()))),
      Some(PBlock(Vector())))
    frontend.ghostLessStmt(input)() should matchPattern {
      case PIfStmt(Vector(
        PIfClause(None, PBoolLit(true), b1),
        PIfClause(None, PBoolLit(false), b2)),
        Some(b3)) if b1.nonEmptyStmts == Vector() && b2.nonEmptyStmts == Vector() && b3.nonEmptyStmts == Vector() =>
    }
  }


  /* ** Stubs, mocks, and other test setup  */

  class TestFrontend {
    def stubProgram(inArgs: Vector[(PParameter, Boolean)], body : PStatement) : PProgram = PProgram(
      PPackageClause(PPkgDef("pkg")),
      Vector(),
      Vector(PFunctionDecl(
        PIdnDef("foo"),
        inArgs.map(_._1),
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), true),
        Some(PBodyParameterInfo(inArgs.collect{ case (n: PNamedParameter, true) => PIdnUse(n.id.name) }), PBlock(Vector(body)))
      ))
    )

    def ghostLessProg(program: PProgram): PProgram = {
      val positions = new Positions
      val pkg = PPackage(
        PPackageClause(PPkgDef("pkg")),
        Vector(program),
        new PositionManager(positions)
      )
      val tree = new Info.GoTree(pkg)
      val context = new Info.Context()
      val config = Config(Vector())
      val info = new TypeInfoImpl(tree, context)(config)

      val ghostLess = new GhostLessPrinter(info).format(pkg)
      // try to parse ghostLess string:
      val parseRes = Parser.parseProgram(StringSource(ghostLess, "Ghostless Program"), false)(config)
      parseRes match {
        case Right(prog) => prog
        case Left(messages) => fail(s"Parsing failed: $messages")
      }
    }

    def ghostLessStmt(stmt: PStatement)(inArgs: Vector[(PParameter, Boolean)] = Vector()): PStatement = {
      val program = stubProgram(inArgs, stmt)
      val ghostLess = ghostLessProg(program)
      val block = ghostLess match {
        case PProgram(_, _, Vector(PFunctionDecl(PIdnDef("foo"), _, _, _, Some((_, b))))) => b
        case p => fail(s"Parsing succeeded but with an unexpected program $p")
      }
      getSingleStmt(block.stmts)
    }

    def getSingleStmt(stmts: Vector[PStatement]): PStatement = {
      val nonEmptyStmts = stmts.filter(s => s match {
        case _: PEmptyStmt => false
        case _ => true
      })
      nonEmptyStmts match {
        case Vector(PSeq(s)) => getSingleStmt(s)
        case Vector(s) => s
        case s => fail(s"Parsing succeeded but with unexpected stmts $s")
      }
    }

    def ghostLessExpr(expr: PExpression)(inArgs: Vector[(PParameter, Boolean)] = Vector()): PExpression = {
      val stmt = PShortVarDecl(Vector(expr), Vector(PIdnUnk("n")), Vector(false))
      val ghostLess = ghostLessStmt(stmt)(inArgs)
      ghostLess match {
        case PShortVarDecl(Vector(e), _, _) => e
        case stmt => fail(s"Parsing succeeded but with unexpected stmt $stmt")
      }
    }
  }
}
