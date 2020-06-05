package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._

class FrontendPrettyPrinterUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Printer: should show sequence update clauses as expected") {
    val expr = PSequenceUpdateClause(
      PNamedOperand(PIdnUse("x")),
      PIntLit(BigInt(42))
    )
    frontend.show(expr) should matchPattern {
      case "x = 42" =>
    }
  }

  test("Printer: should show a sequence update with a single clause as expected") {
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

  test("Printer: should show a sequence update without any clauses as expected") {
    val expr = PSequenceUpdate(
      PNamedOperand(PIdnUse("xs")),
      Vector()
    )
    frontend.show(expr) should matchPattern {
      case "xs" =>
    }
  }

  test("Printer: should show a sequence update with multiple clauses as expected") {
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

  test("Printer: should show a range sequence as expected") {
    val expr = PRangeSequence(PIntLit(BigInt(1)), PIntLit(BigInt(42)))
    frontend.show(expr) should matchPattern {
      case "seq[1 .. 42]" =>
    }
  }

  test("Printer: should show a non-empty sequence literal expression as expected") {
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

  test("Printer: should show an empty sequence literal expression as expected") {
    val expr = PSequenceLiteral(PBoolType(), Vector())
    frontend.show(expr) should matchPattern {
      case "seq[bool] { }" =>
    }
  }

  test("Printer: should show a slice expression with three indices as expected") {
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

  test("Printer: should show a slice expression with missing capacity as expected") {
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

  test("Printer: should show a slice expression with missing 'high' and 'cap' as expected") {
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

  test("Printer: should show a slice expression with missing 'low' and 'cap' as expected") {
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

  test("Printer: should show a slice expression with missing 'high' as expected") {
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

  test("Printer: should show a slice expression with missing 'low' as expected") {
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

  test("Printer: should show a slice expression with missing 'low', 'high' and 'cap' as expected") {
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

  test("Printer: should correctly show a simple sequence type") {
    val node = PSequenceType(PIntType())
    frontend.show(node) should matchPattern {
      case "seq[int]" =>
    }
  }

  test("Printer: should correctly show a nested sequence type") {
    val node = PSequenceType(PSequenceType(PBoolType()))
    frontend.show(node) should matchPattern {
      case "seq[seq[bool]]" =>
    }
  }

  test("Printer: should correctly show a simple set type") {
    val node = PSetType(PIntType())
    frontend.show(node) should matchPattern {
      case "set[int]" =>
    }
  }

  test("Printer: should correctly show a nested set type") {
    val node = PSetType(PSetType(PBoolType()))
    frontend.show(node) should matchPattern {
      case "set[set[bool]]" =>
    }
  }

  test("Printer: should correctly show an empty integer set literal") {
    val expr = PSetLiteral(PIntType(), Vector())
    frontend.show(expr) should matchPattern {
      case "set[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty set literal with nested type") {
    val expr = PSetLiteral(PSetType(PBoolType()), Vector())
    frontend.show(expr) should matchPattern {
      case "set[set[bool]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = PSetLiteral(PIntType(), Vector(PIntLit(BigInt(42))))
    frontend.show(expr) should matchPattern {
      case "set[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = PSetLiteral(PBoolType(), Vector(
      PBoolLit(true), PBoolLit(false), PBoolLit(false)
    ))
    frontend.show(expr) should matchPattern {
      case "set[bool] { true, false, false }" =>
    }
  }

  test("Printer: should correctly show a simple set union") {
    val expr = PUnion(
      PNamedOperand(PIdnUse("s")),
      PNamedOperand(PIdnUse("t"))
    )
    frontend.show(expr) should matchPattern {
      case "s union t" =>
    }
  }

  test("Printer: should correctly show a chain of three set unions (1)") {
    val expr = PUnion(
      PUnion(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ),
      PNamedOperand(PIdnUse("u"))
    )
    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should correctly show a chain of three set unions (2)") {
    val expr = PUnion(
      PNamedOperand(PIdnUse("s")),
      PUnion(
        PNamedOperand(PIdnUse("t")),
        PNamedOperand(PIdnUse("u"))
      )
    )

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should correctly show set union in combination with literals") {
    val expr = PUnion(
      PSetLiteral(PBoolType(), Vector()),
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(7)))
    )

    frontend.show(expr) should matchPattern {
      case "set[bool] { } union set[int] { 1, 7 }" =>
    }
  }

  test("Printer: should correctly show a simple set intersection") {
    val expr = PIntersection(
      PNamedOperand(PIdnUse("s")),
      PNamedOperand(PIdnUse("t"))
    )

    frontend.show(expr) should matchPattern {
      case "s intersection t" =>
    }
  }

  test("Printer: should correctly show a chain of set intersections (1)") {
    val expr = PIntersection(
      PIntersection(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ),
      PNamedOperand(PIdnUse("u"))
    )

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should correctly show a chain of set intersections (2)") {
    val expr = PIntersection(
      PNamedOperand(PIdnUse("s")),
      PIntersection(
        PNamedOperand(PIdnUse("t")),
        PNamedOperand(PIdnUse("u"))
      ),
    )

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should correctly show set intersection in combination with literals") {
    val expr = PIntersection(
      PSetLiteral(PIntType(), Vector()),
      PSetLiteral(PBoolType(), Vector(PBoolLit(true)))
    )

    frontend.show(expr) should matchPattern {
      case "set[int] { } intersection set[bool] { true }" =>
    }
  }

  test("Printer: should correctly show a simple set difference") {
    val expr = PSetMinus(
      PNamedOperand(PIdnUse("s")),
      PNamedOperand(PIdnUse("t"))
    )

    frontend.show(expr) should matchPattern {
      case "s setminus t" =>
    }
  }

  test("Printer: should correctly show a chain of set differences (1)") {
    val expr = PSetMinus(
      PSetMinus(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ),
      PNamedOperand(PIdnUse("u"))
    )

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should correctly show a chain of set differences (2)") {
    val expr = PSetMinus(
      PNamedOperand(PIdnUse("s")),
      PSetMinus(
        PNamedOperand(PIdnUse("t")),
        PNamedOperand(PIdnUse("u"))
      )
    )

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should correctly show set difference in combination with literals") {
    val expr = PSetMinus(
      PSetLiteral(PIntType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector())
    )

    frontend.show(expr) should matchPattern {
      case "set[int] { true } setminus set[bool] { }" =>
    }
  }

  test("Printer: should correctly show a simple subset expression") {
    val expr = PSubset(
      PNamedOperand(PIdnUse("s")),
      PNamedOperand(PIdnUse("t"))
    )

    frontend.show(expr) should matchPattern {
      case "s subset t" =>
    }
  }

  test("Printer: should correctly show a chain of subset uses (1)") {
    val expr = PSubset(
      PSubset(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ),
      PNamedOperand(PIdnUse("u"))
    )

    frontend.show(expr) should matchPattern {
      case "s subset t subset u" =>
    }
  }

  test("Printer: should correctly show a chain of set subset uses (2)") {
    val expr = PSubset(
      PNamedOperand(PIdnUse("s")),
      PSubset(
        PNamedOperand(PIdnUse("t")),
        PNamedOperand(PIdnUse("u"))
      )
    )

    frontend.show(expr) should matchPattern {
      case "s subset t subset u" =>
    }
  }

  test("Printer: should correctly show a subset relation in combination with literals") {
    val expr = PSubset(
      PSetLiteral(PIntType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector())
    )

    frontend.show(expr) should matchPattern {
      case "set[int] { true } subset set[bool] { }" =>
    }
  }

  test("Printer: should correctly show a use of the 'in' operator for sequences and (multi)sets") {
    val expr = PIn(
      PNamedOperand(PIdnUse("x")),
      PNamedOperand(PIdnUse("xs"))
    )

    frontend.show(expr) should matchPattern {
      case "x in xs" =>
    }
  }

  test("Printer: should correctly show a short 'chain' of seq/set inclusions") {
    val expr = PIn(
      PNamedOperand(PIdnUse("x")),
      PIn(
        PNamedOperand(PIdnUse("xs")),
        PNamedOperand(PIdnUse("ys")),
      )
    )

    frontend.show(expr) should matchPattern {
      case "x in xs in ys" =>
    }
  }

  test("Printer: should show the unary size operator for sequences and (multi)sets as expected") {
    val expr = PSize(PNamedOperand(PIdnUse("s")))
    frontend.show(expr) should matchPattern {
      case "|s|" =>
    }
  }

  test("Printer: should correctly show the size operator in combination with set union") {
    val expr = PSize(
      PUnion(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      )
    )
    frontend.show(expr) should matchPattern {
      case "|s union t|" =>
    }
  }

  test("Printer: should correctly show the type of integer multisets") {
    val typ = PMultisetType(PIntType())
    frontend.show(typ) should matchPattern {
      case "mset[int]" =>
    }
  }

  test("Printer: should correctly show a nested multiset type") {
    val typ = PMultisetType(PMultisetType(PBoolType()))
    frontend.show(typ) should matchPattern {
      case "mset[mset[bool]]" =>
    }
  }

  test("Printer: should correctly show an empty Boolean multiset literal") {
    val expr = PMultisetLiteral(PBoolType(), Vector())
    frontend.show(expr) should matchPattern {
      case "mset[bool] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested multiset literal") {
    val expr = PMultisetLiteral(PMultisetType(PMultisetType(PIntType())), Vector())
    frontend.show(expr) should matchPattern {
      case "mset[mset[mset[int]]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer multiset literal") {
    val expr = PMultisetLiteral(PIntType(), Vector(
      PIntLit(42)
    ))
    frontend.show(expr) should matchPattern {
      case "mset[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean multiset literal") {
    val expr = PMultisetLiteral(PBoolType(), Vector(
      PBoolLit(false), PBoolLit(true), PBoolLit(true)
    ))
    frontend.show(expr) should matchPattern {
      case "mset[bool] { false, true, true }" =>
    }
  }

  test("Printer: should correctly show a nesting of multiset literals") {
    val expr = PMultisetLiteral(PMultisetType(PIntType()), Vector(
      PMultisetLiteral(PIntType(), Vector(
        PIntLit(42)
      ))
    ))
    frontend.show(expr) should matchPattern {
      case "mset[mset[int]] { mset[int] { 42 } }" =>
    }
  }

  test("Printer: should correctly show a multiset union") {
    val expr = PUnion(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(42)))
    )
    frontend.show(expr) should matchPattern {
      case "mset[bool] { true } union mset[int] { 42 }" =>
    }
  }

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : PNode) : String = printer.format(n)
  }
}
