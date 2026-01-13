// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.frontend._

class FrontendPrettyPrinterUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Printer: should show sequence update clauses as expected") {
    val expr = PGhostCollectionUpdateClause(
      PNamedOperand(PIdnUse("x")),
      PIntLit(BigInt(42))
    )
    frontend.show(expr) should matchPattern {
      case "x = 42" =>
    }
  }

  test("Printer: should show a sequence update with a single clause as expected") {
    val expr = PGhostCollectionUpdate(
      PNamedOperand(PIdnUse("xs")),
      Vector(
        PGhostCollectionUpdateClause(PIntLit(BigInt(1)), PBoolLit(false))
      )
    )
    frontend.show(expr) should matchPattern {
      case "xs[1 = false]" =>
    }
  }

  test("Printer: should show a sequence update without any clauses as expected") {
    val expr = PGhostCollectionUpdate(
      PNamedOperand(PIdnUse("xs")),
      Vector()
    )
    frontend.show(expr) should matchPattern {
      case "xs" =>
    }
  }

  test("Printer: should show a sequence update with multiple clauses as expected") {
    val expr = PGhostCollectionUpdate(
      PNamedOperand(PIdnUse("zs")),
      Vector(
        PGhostCollectionUpdateClause(PNamedOperand(PIdnUse("i")), PBoolLit(false)),
        PGhostCollectionUpdateClause(PNamedOperand(PIdnUse("j")), PBoolLit(true)),
        PGhostCollectionUpdateClause(PNamedOperand(PIdnUse("k")), PBoolLit(false))
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
    val expr = PLiteral.sequence(
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
    val expr = PLiteral.sequence(PBoolType(), Vector())
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
    val expr = PLiteral.set(PIntType(), Vector())
    frontend.show(expr) should matchPattern {
      case "set[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty set literal with nested type") {
    val expr = PLiteral.set(PSetType(PBoolType()), Vector())
    frontend.show(expr) should matchPattern {
      case "set[set[bool]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = PLiteral.set(PIntType(), Vector(PIntLit(BigInt(42))))
    frontend.show(expr) should matchPattern {
      case "set[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = PLiteral.set(PBoolType(), Vector(
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
      PLiteral.set(PBoolType(), Vector()),
      PLiteral.set(PIntType(), Vector(PIntLit(1), PIntLit(7)))
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
      PLiteral.set(PIntType(), Vector()),
      PLiteral.set(PBoolType(), Vector(PBoolLit(true)))
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
      PLiteral.set(PIntType(), Vector(PBoolLit(true))),
      PLiteral.set(PBoolType(), Vector())
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
      PLiteral.set(PIntType(), Vector(PBoolLit(true))),
      PLiteral.set(PBoolType(), Vector())
    )

    frontend.show(expr) should matchPattern {
      case "set[int] { true } subset set[bool] { }" =>
    }
  }

  test("Printer: should correctly show a use of the 'elem' operator for sequences and (multi)sets") {
    val expr = PElem(
      PNamedOperand(PIdnUse("x")),
      PNamedOperand(PIdnUse("xs"))
    )

    frontend.show(expr) should matchPattern {
      case "x elem xs" =>
    }
  }

  test("Printer: should correctly show a short 'chain' of seq/set inclusions") {
    val expr = PElem(
      PNamedOperand(PIdnUse("x")),
      PElem(
        PNamedOperand(PIdnUse("xs")),
        PNamedOperand(PIdnUse("ys")),
      )
    )

    frontend.show(expr) should matchPattern {
      case "x elem xs elem ys" =>
    }
  }

  test("Printer: should show the unary size operator for sequences and (multi)sets as expected") {
    val expr = PLength(PNamedOperand(PIdnUse("s")))
    frontend.show(expr) should matchPattern {
      case "len(s)" =>
    }
  }

  test("Printer: should correctly show the size operator in combination with set union") {
    val expr = PLength(
      PUnion(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      )
    )
    frontend.show(expr) should matchPattern {
      case "len(s union t)" =>
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
    val expr = PLiteral.multiset(PBoolType(), Vector())
    frontend.show(expr) should matchPattern {
      case "mset[bool] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested multiset literal") {
    val expr = PLiteral.multiset(PMultisetType(PMultisetType(PIntType())), Vector())
    frontend.show(expr) should matchPattern {
      case "mset[mset[mset[int]]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer multiset literal") {
    val expr = PLiteral.multiset(PIntType(), Vector(
      PIntLit(42)
    ))

    frontend.show(expr) should matchPattern {
      case "mset[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean multiset literal") {
    val expr = PLiteral.multiset(PBoolType(), Vector(
      PBoolLit(false), PBoolLit(true), PBoolLit(true)
    ))

    frontend.show(expr) should matchPattern {
      case "mset[bool] { false, true, true }" =>
    }
  }

  test("Printer: should correctly show a nesting of multiset literals") {
    val expr = PLiteral.multiset(PMultisetType(PIntType()), Vector(
      PLiteral.multiset(PIntType(), Vector(
        PIntLit(42)
      ))
    ))

    frontend.show(expr) should matchPattern {
      case "mset[mset[int]] { mset[int] { 42 } }" =>
    }
  }

  test("Printer: should correctly show a multiset union") {
    val expr = PUnion(
      PLiteral.multiset(PBoolType(), Vector(PBoolLit(true))),
      PLiteral.multiset(PIntType(), Vector(PIntLit(42)))
    )

    frontend.show(expr) should matchPattern {
      case "mset[bool] { true } union mset[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a subset relation applied to two multiset literals") {
    val expr = PSubset(
      PLiteral.multiset(PBoolType(), Vector(PBoolLit(false))),
      PLiteral.multiset(PIntType(), Vector(PIntLit(42), PIntLit(12)))
    )

    frontend.show(expr) should matchPattern {
      case "mset[bool] { false } subset mset[int] { 42, 12 }" =>
    }
  }

  test("Printer: should correctly show the cardinality of a multiset literal") {
    val expr = PLength(
      PLiteral.multiset(PMultisetType(PIntType()), Vector(
        PLiteral.multiset(PIntType(), Vector(PIntLit(1))),
        PLiteral.multiset(PIntType(), Vector(PIntLit(2), PIntLit(3)))
      ))
    )

    frontend.show(expr) should matchPattern {
      case "len(mset[mset[int]] { mset[int] { 1 }, mset[int] { 2, 3 } })" =>
    }
  }

  test("Printer: should correctly show a multiset inclusion expression (1)") {
    val expr = PElem(
      PIntLit(42),
      PLiteral.multiset(PIntType(), Vector(PIntLit(12), PIntLit(42)))
    )

    frontend.show(expr) should matchPattern {
      case "42 elem mset[int] { 12, 42 }" =>
    }
  }

  test("Printer: should correctly show a multiset inclusion expression (2)") {
    val expr = PElem(
      PLiteral.multiset(PIntType(), Vector(PIntLit(1))),
      PLiteral.multiset(PIntType(), Vector(PIntLit(2), PIntLit(3)))
    )

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1 } elem mset[int] { 2, 3 }" =>
    }
  }

  test("Printer: should be able to show a very simple set conversion expression") {
    val expr = PSetConversion(PNamedOperand(PIdnUse("xs")))
    frontend.show(expr) should matchPattern {
      case "set(xs)" =>
    }
  }

  test("Printer: should be able to show a set conversion with a composite body") {
    val expr = PSetConversion(
      PSequenceAppend(
        PLiteral.sequence(PIntType(), Vector(PIntLit(1), PIntLit(2))),
        PNamedOperand(PIdnUse("ys"))
      )
    )

    frontend.show(expr) should matchPattern {
      case "set(seq[int] { 1, 2 } ++ ys)" =>
    }
  }

  test("Printer: should be able to correctly show the union of two simple set conversions") {
    val expr = PUnion(
      PSetConversion(PNamedOperand(PIdnUse("xs"))),
      PSetConversion(PNamedOperand(PIdnUse("ys")))
    )

    frontend.show(expr) should matchPattern {
      case "set(xs) union set(ys)" =>
    }
  }

  test("Printer: should be able to correctly show a nested set conversion") {
    val expr = PSetConversion(PSetConversion(PNamedOperand(PIdnUse("xs"))))

    frontend.show(expr) should matchPattern {
      case "set(set(xs))" =>
    }
  }

  test("Printer: should be able to parse a simple multiplicity expression") {
    val expr = PMultiplicity(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y")))

    frontend.show(expr) should matchPattern {
      case "x # y" =>
    }
  }

  test("Printer: should be able to show a slightly more complex multiplicity expression") {
    val expr = PMultiplicity(
      PAdd(PIntLit(2), PIntLit(3)),
      PLiteral.sequence(PIntType(), Vector(
        PMultiplicity(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y")))
      ))
    )

    frontend.show(expr) should matchPattern {
      case "(2 + 3) # seq[int] { x # y }" =>
    }
  }

  test("Printer: should correctly show nested multiplicity expressions") {
    val expr = PMultiplicity(
      PNamedOperand(PIdnUse("x")),
      PMultiplicity(
        PNamedOperand(PIdnUse("y")),
        PNamedOperand(PIdnUse("z"))
      )
    )

    frontend.show(expr) should matchPattern {
      case "x # y # z" =>
    }
  }

  test("Printer: should correctly show a very simple bag conversion expression") {
    val expr = PMultisetConversion(
      PNamedOperand(PIdnUse("xs"))
    )

    frontend.show(expr) should matchPattern {
      case "mset(xs)" =>
    }
  }

  test("Printer: should show a multiset conversion expression with a slightly more complex inner expression") {
    val expr = PMultisetConversion(
      PSequenceAppend(
        PLiteral.sequence(PIntType(), Vector(PIntLit(2), PIntLit(3))),
        PNamedOperand(PIdnUse("xs"))
      )
    )

    frontend.show(expr) should matchPattern {
      case "mset(seq[int] { 2, 3 } ++ xs)" =>
    }
  }

  test("Printer: should correctly show the union of two multiset conversions") {
    val expr = PUnion(
      PMultisetConversion(PNamedOperand(PIdnUse("xs"))),
      PMultisetConversion(PNamedOperand(PIdnUse("ys")))
    )

    frontend.show(expr) should matchPattern {
      case "mset(xs) union mset(ys)" =>
    }
  }

  test("Printer: should correctly show a nested multiset conversion") {
    val expr = PMultisetConversion(
      PMultisetConversion(PNamedOperand(PIdnUse("xs")))
    )

    frontend.show(expr) should matchPattern {
      case "mset(mset(xs))" =>
    }
  }

  test("Printer: should correctly show a very simple use of the 'len' function") {
    val expr = PLength(PIntLit(42))

    frontend.show(expr) should matchPattern {
      case "len(42)" =>
    }
  }

  test("Printer: should correctly show a slightly more complex of the use of the 'len' function applied to a sequence") {
    val expr = PLength(
      PSequenceAppend(
        PRangeSequence(PIntLit(1), PIntLit(12)),
        PLiteral.sequence(PBoolType(), Vector(PBoolLit(false), PBoolLit(true)))
      )
    )

    frontend.show(expr) should matchPattern {
      case "len(seq[1 .. 12] ++ seq[bool] { false, true })" =>
    }
  }

  test("Printer: should be able to show the addition of two uses of the 'len' function") {
    val expr = PAdd(
      PLength(PRangeSequence(PIntLit(1), PIntLit(12))),
      PLength(PLiteral.sequence(PBoolType(), Vector(PBoolLit(false), PBoolLit(true))))
    )

    frontend.show(expr) should matchPattern {
      case "len(seq[1 .. 12]) + len(seq[bool] { false, true })" =>
    }
  }

  test("Printer: should correctly show a simple integer array type") {
    val typ = PArrayType(PIntLit(42), PIntType())

    frontend.show(typ) should matchPattern {
      case "[42]int" =>
    }
  }

  test("Printer: should be able to show a slightly more complex array type") {
    val typ = PArrayType(
      PAdd(PNamedOperand(PIdnUse("x")), PIntLit(12)),
      PSequenceType(PBoolType())
    )

    frontend.show(typ) should matchPattern {
      case "[x + 12]seq[bool]" =>
    }
  }

  test("Printer: should correctly show a multidimensional array type") {
    val typ = PArrayType(
      PNamedOperand(PIdnUse("x")),
      PArrayType(
        PNamedOperand(PIdnUse("y")),
        PIntType()
      )
    )

    frontend.show(typ) should matchPattern {
      case "[x][y]int" =>
    }
  }

  test("Printer: should correctly show an application of the 'cap' function") {
    val expr = PCapacity(PNamedOperand(PIdnUse("e")))

    frontend.show(expr) should matchPattern {
      case "cap(e)" =>
    }
  }

  test("Printer: should correctly show a slightly more complex application of the built-in 'cap' function") {
    val expr = PCapacity(PLiteral.sequence(PIntType(), Vector(PIntLit(42))))

    frontend.show(expr) should matchPattern {
      case "cap(seq[int] { 42 })" =>
    }
  }

  test("Printer: should correctly show a nested 'cap' function application") {
    val expr = PCapacity(PCapacity(PCapacity(PIntLit(41))))

    frontend.show(expr) should matchPattern {
      case "cap(cap(cap(41)))" =>
    }
  }

  test("Printer: should correctly show a expression built from 'cap' function applications") {
    val expr = PAdd(
      PCapacity(PNamedOperand(PIdnUse("e1"))),
      PCapacity(PNamedOperand(PIdnUse("e2")))
    )

    frontend.show(expr) should matchPattern {
      case "cap(e1) + cap(e2)" =>
    }
  }

  test("Printer: should correctly show a very simple integer array literal") {
    val expr = PLiteral.array(
      PIntType(),
      Vector(PIntLit(12), PIntLit(24))
    )

    frontend.show(expr) should matchPattern {
      case "[2]int { 12, 24 }" =>
    }
  }

  test("Printer: should correctly show an empty Boolean array literal") {
    val expr = PLiteral.array(
      PBoolType(),
      Vector()
    )

    frontend.show(expr) should matchPattern {
      case "[0]bool { }" =>
    }
  }

  test("Printer: should be able to correctly show an array literal with a slightly more complex length") {
    val expr = PCompositeLit(
      PArrayType(PAdd(PNamedOperand(PIdnUse("x")), PLength(PNamedOperand(PIdnUse("xs")))), PIntType()),
      PLiteralValue(Vector())
    )

    frontend.show(expr) should matchPattern {
      case "[x + len(xs)]int { }" =>
    }
  }

  test("Printer: should correctly show an array literal with a slightly more complex type") {
    val expr = PLiteral.array(
      PSequenceType(PSetType(PBoolType())),
      Vector()
    )

    frontend.show(expr) should matchPattern {
      case "[0]seq[set[bool]] { }" =>
    }
  }

  test("Printer: Should correctly show an array singleton literal with a slightly more complex inner expression") {
    val expr = PLiteral.array(
      PIntType(),
      Vector(PAdd(PNamedOperand(PIdnUse("n")), PLength(PNamedOperand(PIdnUse("xs")))))
    )

    frontend.show(expr) should matchPattern {
      case "[1]int { n + len(xs) }" =>
    }
  }

  test("Printer: should be able to correctly show an array literal with an implicit length") {
    val expr = PCompositeLit(
      PImplicitSizeArrayType(PIntType()),
      PLiteralValue(Vector(
        PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("x")))),
        PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("y")))),
        PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("z"))))
      ))
    )

    frontend.show(expr) should matchPattern {
      case "[...]int { x, y, z }" =>
    }
  }

  test("Printer: should be able to correctly show a 2-dimensional array literal") {
    val expr = PLiteral.array(
      PArrayType(PIntLit(1), PBoolType()),
      Vector(PLiteral.array(PBoolType(), Vector(PBoolLit(false))))
    )

    frontend.show(expr) should matchPattern {
      case "[1][1]bool { [1]bool { false } }" =>
    }
  }

  test("Printer: should correctly show a nested array literal, both of an implicit length") {
    val expr = PCompositeLit(
      PImplicitSizeArrayType(PBoolType()),
      PLiteralValue(Vector(
        PKeyedElement(None, PLitCompositeVal(
          PLiteralValue(Vector())
        ))
      ))
    )

    frontend.show(expr) should matchPattern {
      case "[...]bool { { } }" =>
    }
  }

  test("Printer: should correctly show a very simple sequence conversion") {
    val expr = PSequenceConversion(PNamedOperand(PIdnUse("e")))

    frontend.show(expr) should matchPattern {
      case "seq(e)" =>
    }
  }

  test("Printer: should be able to show a slightly more complex sequence conversion expression") {
    val expr = PSequenceConversion(
      PSequenceAppend(
        PNamedOperand(PIdnUse("xs")),
        PLiteral.array(PIntType(), Vector(
          PIntLit(42)
        ))
      )
    )

    frontend.show(expr) should matchPattern {
      case "seq(xs ++ [1]int { 42 })" =>
    }
  }

  test("Printer: should correctly show a nested sequence conversion") {
    val expr = PSequenceConversion(
      PSequenceConversion(PNamedOperand(PIdnUse("a")))
    )

    frontend.show(expr) should matchPattern {
      case "seq(seq(a))" =>
    }
  }

  test("Printer: should correctly show the append of two sequence conversions") {
    val expr = PSequenceAppend(
      PSequenceConversion(PNamedOperand(PIdnUse("a"))),
      PSequenceConversion(PNamedOperand(PIdnUse("b")))
    )

    frontend.show(expr) should matchPattern {
      case "seq(a) ++ seq(b)" =>
    }
  }

  test("Printer: should correctly show a simple option type") {
    val typ = POptionType(POptionType(PBoolType()))

    frontend.show(typ) should matchPattern {
      case "option[option[bool]]" =>
    }
  }

  test("Printer: should correctly show a 'none' option type expression") {
    val exp = POptionNone(PSetType(PIntType()))

    frontend.show(exp) should matchPattern {
      case "none[set[int]]" =>
    }
  }

  test("Printer: should correctly show a 'some' option type expression") {
    val exp = POptionSome(PIntLit(42))

    frontend.show(exp) should matchPattern {
      case "some(42)" =>
    }
  }

  test("Printer: should correctly show a 'get' option type expression") {
    val exp = POptionGet(POptionSome(PAdd(PIntLit(4), PIntLit(2))))

    frontend.show(exp) should matchPattern {
      case "get(some(4 + 2))" =>
    }
  }

  test("Printer: should be able to show a simple integer slice type") {
    val expr = PSliceType(PIntType())

    frontend.show(expr) should matchPattern {
      case "[]int" =>
    }
  }

  test("Printer: should be able to show a slice type with a slightly more complex 'inner type'") {
    val expr = PSliceType(PSequenceType(PSetType(PBoolType())))

    frontend.show(expr) should matchPattern {
      case "[]seq[set[bool]]" =>
    }
  }

  test("Printer: should be able to show a nested slice type") {
    val expr = PSliceType(PSliceType(PSliceType(PIntType())))

    frontend.show(expr) should matchPattern {
      case "[][][]int" =>
    }
  }


  /* ** Stubs, mocks and other test setup */

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : PNode) : String = printer.format(n)
  }
}
