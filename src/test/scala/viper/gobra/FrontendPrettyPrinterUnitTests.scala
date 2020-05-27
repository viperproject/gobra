package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._

class FrontendPrettyPrinterUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("PrettyPrinter: should show sequence update clauses as expected") {
    val expr = PSequenceUpdateClause(
      PNamedOperand(PIdnUse("x")),
      PIntLit(BigInt(42))
    )
    frontend.show(expr) should matchPattern {
      case "x = 42" =>
    }
  }

  test("PrettyPrinter: should show a sequence update with a single clause as expected") {
    val expr = PSequenceUpdate(
      PNamedOperand(PIdnUse("xs")),
      Vector(
        PSequenceUpdateClause(PIntLit(BigInt(1)), PBoolLit(false))
      )
    )
    frontend.show(expr) should matchPattern {
      case "xs[1 = false]" =>
    }
  }

  test("PrettyPrinter: should show a sequence update without any clauses as expected") {
    val expr = PSequenceUpdate(
      PNamedOperand(PIdnUse("xs")),
      Vector()
    )
    frontend.show(expr) should matchPattern {
      case "xs" =>
    }
  }

  test("PrettyPrinter: should show a sequence update with multiple clauses as expected") {
    val expr = PSequenceUpdate(
      PNamedOperand(PIdnUse("zs")),
      Vector(
        PSequenceUpdateClause(PNamedOperand(PIdnUse("i")), PBoolLit(false)),
        PSequenceUpdateClause(PNamedOperand(PIdnUse("j")), PBoolLit(true)),
        PSequenceUpdateClause(PNamedOperand(PIdnUse("k")), PBoolLit(false))
      )
    )
    frontend.show(expr) should matchPattern {
      case "zs[i = false, j = true, k = false]" =>
    }
  }

  test("PrettyPrinter: should show a range sequence as expected") {
    val expr = PRangeSequence(PIntLit(BigInt(1)), PIntLit(BigInt(42)))
    frontend.show(expr) should matchPattern {
      case "seq[1 .. 42]" =>
    }
  }

  test("PrettyPrinter: should show a non-empty sequence literal expression as expected") {
    val expr = PSequenceLiteral(
      PIntType(),
      Vector(
        PNamedOperand(PIdnUse("i")),
        PIntLit(BigInt(7)),
        PAdd(PNamedOperand(PIdnUse("x")), PIntLit(BigInt(2)))
      )
    )
    frontend.show(expr) should matchPattern {
      case "seq[int] { i, 7, x + 2 }" =>
    }
  }

  test("PrettyPrinter: should show an empty sequence literal expression as expected") {
    val expr = PSequenceLiteral(PBoolType(), Vector())
    frontend.show(expr) should matchPattern {
      case "seq[bool] { }" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with three indices as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      Some(PNamedOperand(PIdnUse("i"))),
      Some(PNamedOperand(PIdnUse("j"))),
      Some(PNamedOperand(PIdnUse("k")))
    )
    frontend.show(expr) should matchPattern {
      case "xs[i:j:k]" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with missing capacity as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      Some(PNamedOperand(PIdnUse("i"))),
      Some(PNamedOperand(PIdnUse("j"))),
      None
    )
    frontend.show(expr) should matchPattern {
      case "xs[i:j]" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with missing 'high' and 'cap' as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      Some(PNamedOperand(PIdnUse("i"))),
      None,
      None
    )
    frontend.show(expr) should matchPattern {
      case "xs[i:]" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with missing 'low' and 'cap' as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      None,
      Some(PNamedOperand(PIdnUse("i"))),
      None
    )
    frontend.show(expr) should matchPattern {
      case "xs[:i]" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with missing 'high' as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      Some(PNamedOperand(PIdnUse("i"))),
      None,
      Some(PNamedOperand(PIdnUse("j")))
    )
    frontend.show(expr) should matchPattern {
      case "xs[i::j]" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with missing 'low' as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      None,
      Some(PNamedOperand(PIdnUse("i"))),
      Some(PNamedOperand(PIdnUse("j")))
    )
    frontend.show(expr) should matchPattern {
      case "xs[:i:j]" =>
    }
  }

  test("PrettyPrinter: should show a slice expression with missing 'low', 'high' and 'cap' as expected") {
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      None,
      None,
      None
    )
    frontend.show(expr) should matchPattern {
      case "xs[:]" =>
    }
  }

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : PNode) : String = printer.format(n)
  }
}
