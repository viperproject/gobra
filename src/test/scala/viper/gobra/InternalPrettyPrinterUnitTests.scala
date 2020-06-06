package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.internal._
import viper.gobra.reporting.Source.Parser.Unsourced

class InternalPrettyPrinterUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Printer: should correctly show a standard sequence index expression") {
    val expr = SequenceIndex(
      LocalVar.Ref("xs", SequenceT(IntT))(Unsourced),
      IntLit(BigInt(42))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "xs[42]" =>
    }
  }

  test("Printer: should correctly show a sequence update expression") {
    val expr = SequenceUpdate(
      LocalVar.Ref("xs", SequenceT(BoolT))(Unsourced),
      IntLit(BigInt(4))(Unsourced),
      BoolLit(false)(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "xs[4 = false]" =>
    }
  }

  test("Printer: should correctly show an empty integer sequence") {
    val expr = SequenceLiteral(IntT, Vector())(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty (nested) Boolean sequence") {
    val expr = SequenceLiteral(SequenceT(SequenceT(BoolT)), Vector())(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[seq[seq[bool]]] { }" =>
    }
  }

  test("Printer: should correctly show a sequence range expression") {
    val expr = RangeSequence(
      IntLit(BigInt(2))(Unsourced),
      IntLit(BigInt(44))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[2 .. 44]" =>
    }
  }

  test("Printer: should correctly show a non-empty simple integer sequence literal") {
    val expr = SequenceLiteral(
      IntT,
      Vector(
        IntLit(BigInt(2))(Unsourced),
        IntLit(BigInt(4))(Unsourced),
        IntLit(BigInt(8))(Unsourced)
      )
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[int] { 2, 4, 8 }" =>
    }
  }

  test("Printer: should correctly show a singleton integer sequence literal") {
    val expr = SequenceLiteral(
      IntT,
      Vector(IntLit(BigInt(42))(Unsourced))
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show an empty sequence literal") {
    val expr = SequenceLiteral(BoolT, Vector())(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[bool] { }" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence drop operation") {
    val expr = SequenceDrop(
      LocalVar.Ref("xs", SequenceT(IntT))(Unsourced),
      IntLit(BigInt(42))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "xs[42:]" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence take operation") {
    val expr = SequenceTake(
      LocalVar.Ref("xs", SequenceT(IntT))(Unsourced),
      IntLit(BigInt(4))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "xs[:4]" =>
    }
  }

  test("Printer: should correctly show a sequence drop followed by a take") {
    val expr1 = SequenceDrop(
      LocalVar.Ref("xs", SequenceT(IntT))(Unsourced),
      IntLit(BigInt(2))(Unsourced)
    )(Unsourced)
    val expr2 = SequenceTake(
      expr1, IntLit(BigInt(4))(Unsourced)
    )(Unsourced)

    frontend.show(expr2) should matchPattern {
      case "xs[2:][:4]" =>
    }
  }

  test("Printer: should correctly show an integer sequence type") {
    val t = SequenceT(IntT)
    frontend.show(t) should matchPattern {
      case "seq[int]" =>
    }
  }

  test("Printer: should correctly show a nested sequence type") {
    val t = SequenceT(SequenceT(SequenceT(BoolT)))
    frontend.show(t) should matchPattern {
      case "seq[seq[seq[bool]]]" =>
    }
  }

  test("Printer: should correctly show an integer set type") {
    val t = SetT(IntT)
    frontend.show(t) should matchPattern {
      case "set[int]" =>
    }
  }

  test("Printer: should correctly show a nested set type") {
    val t = SetT(SetT(SetT(BoolT)))
    frontend.show(t) should matchPattern {
      case "set[set[set[bool]]]" =>
    }
  }

  test("Printer: should correctly show an empty integer set") {
    val expr = SetLiteral(IntT, Vector())(Unsourced)
    frontend.show(expr) should matchPattern {
      case "set[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested set") {
    val expr = SetLiteral(SetT(BoolT), Vector())(Unsourced)
    frontend.show(expr) should matchPattern {
      case "set[set[bool]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = SetLiteral(IntT, Vector(IntLit(42)(Unsourced)))(Unsourced)
    frontend.show(expr) should matchPattern {
      case "set[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = SetLiteral(BoolT, Vector(
      BoolLit(false)(Unsourced),
      BoolLit(true)(Unsourced),
      BoolLit(true)(Unsourced)
    ))(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set[bool] { false, true, true }" =>
    }
  }

  test("Printer: should show a set union as expected") {
    val expr = Union(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s union t" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (1)") {
    val expr = Union(
      Union(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
      )(Unsourced),
      LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (2)") {
    val expr = Union(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      Union(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      )(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should correctly show set union in combination with literals") {
    val expr = Union(
      SetLiteral(BoolT, Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      ))(Unsourced),
      SetLiteral(BoolT, Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      ))(Unsourced),
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } union set[bool] { t, u }" =>
    }
  }

  test("Printer: should show a set intersection as expected") {
    val expr = Intersection(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s intersection t" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (1)") {
    val expr = Intersection(
      Intersection(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
      )(Unsourced),
      LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (2)") {
    val expr = Intersection(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      Intersection(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      )(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should correctly show set intersection in combination with literals") {
    val expr = Intersection(
      SetLiteral(BoolT, Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      ))(Unsourced),
      SetLiteral(BoolT, Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      ))(Unsourced),
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } intersection set[bool] { t, u }" =>
    }
  }

  test("Printer: should show a set difference as expected") {
    val expr = SetMinus(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s setminus t" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (1)") {
    val expr = SetMinus(
      SetMinus(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
      )(Unsourced),
      LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (2)") {
    val expr = SetMinus(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      SetMinus(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      )(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should correctly show set differences in combination with literals") {
    val expr = SetMinus(
      SetLiteral(BoolT, Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      ))(Unsourced),
      SetLiteral(BoolT, Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      ))(Unsourced),
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } setminus set[bool] { t, u }" =>
    }
  }

  test("Printer: should print a subset relation as expected") {
    val expr = Subset(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s subset t" =>
    }
  }

  test("Printer: should print a chain of subset relations as expected") {
    val expr = Subset(
      Subset(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
      )(Unsourced),
      LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s subset t subset u" =>
    }
  }

  test("Printer: should properly print a subset relation in combination with literals") {
    val expr = Subset(
      SetLiteral(IntT, Vector(IntLit(42)(Unsourced)))(Unsourced),
      SetLiteral(BoolT, Vector())(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set[int] { 42 } subset set[bool] { }" =>
    }
  }

  test("Printer: should correctly show a standard sequence inclusion") {
    val expr = SequenceContains(
      LocalVar.Ref("x", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("xs", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "x in xs" =>
    }
  }

  test("Printer: should correctly show a 'chain' of sequence inclusions") {
    val expr = SequenceContains(
      SequenceContains(
        LocalVar.Ref("x", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("xs", SequenceT(BoolT))(Unsourced)
      )(Unsourced),
      LocalVar.Ref("ys", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "x in xs in ys" =>
    }
  }

  test("Printer: should correctly show a simple set membership expression") {
    val expr = SetContains(
      LocalVar.Ref("x", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "x in s" =>
    }
  }

  test("Printer: should correctly show a small 'chain' of set membership expressions") {
    val expr = SetContains(
      SetContains(
        LocalVar.Ref("x", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced)
      )(Unsourced),
      LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "x in s in t" =>
    }
  }

  test("Printer: should correctly show set membership in the context of literals") {
    val expr = SetContains(
      SetLiteral(BoolT, Vector(
        BoolLit(true)(Unsourced),
        BoolLit(false)(Unsourced))
      )(Unsourced),
      SetLiteral(SetT(SetT(IntT)), Vector())(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set[bool] { true, false } in set[set[set[int]]] { }" =>
    }
  }

  test("Printer: should correctly show the size of a simple set") {
    val expr = SetCardinality(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "|s|" =>
    }
  }

  test("Printer: should correctly show the size of a set in combination with a set intersection") {
    val expr = SetCardinality(
      Intersection(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
      )(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "|s intersection t|" =>
    }
  }

  test("Printer: should correctly show the size of a set literal") {
    val expr = SetCardinality(
      SetLiteral(IntT, Vector(
        IntLit(1)(Unsourced),
        IntLit(42)(Unsourced)
      ))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "|set[int] { 1, 42 }|" =>
    }
  }

  test("Printer: should correctly show the size of an empty set") {
    val expr = SetCardinality(
      SetLiteral(SequenceT(IntT), Vector())(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "|set[seq[int]] { }|" =>
    }
  }

  test("Printer: should correctly show a simple integer multiset type") {
    val typ = MultisetT(IntT)
    frontend.show(typ) should matchPattern {
      case "mset[int]" =>
    }
  }

  test("Printer: should correctly show a nested multiset type") {
    val typ = MultisetT(MultisetT(BoolT))
    frontend.show(typ) should matchPattern {
      case "mset[mset[bool]]" =>
    }
  }

  test("Printer: should correctly show an empty multiset integer literal") {
    val expr = MultisetLiteral(IntT, Vector())(Unsourced)
    frontend.show(expr) should matchPattern {
      case "mset[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty multiset literal of a nested type") {
    val expr = MultisetLiteral(MultisetT(MultisetT(BoolT)), Vector())(Unsourced)
    frontend.show(expr) should matchPattern {
      case "mset[mset[mset[bool]]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer multiset literal") {
    val expr = MultisetLiteral(IntT, Vector(
      IntLit(42)(Unsourced)
    ))(Unsourced)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a singleton nested multiset literal") {
    val expr = MultisetLiteral(MultisetT(BoolT), Vector(
      MultisetLiteral(BoolT, Vector(
        BoolLit(false)(Unsourced)
      ))(Unsourced)
    ))(Unsourced)

    frontend.show(expr) should matchPattern {
      case "mset[mset[bool]] { mset[bool] { false } }" =>
    }
  }

  test("Printer: should correctly show a non-empty integer multiset literal") {
    val expr = MultisetLiteral(IntT, Vector(
      IntLit(1)(Unsourced),
      IntLit(2)(Unsourced),
      IntLit(3)(Unsourced)
    ))(Unsourced)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2, 3 }" =>
    }
  }

  test("Printer: should correctly show the union of two multiset integer literals") {
    val expr = Union(
      MultisetLiteral(IntT, Vector(IntLit(1)(Unsourced), IntLit(2)(Unsourced)))(Unsourced),
      MultisetLiteral(IntT, Vector(IntLit(3)(Unsourced)))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } union mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show the intersection of two multiset integer literals") {
    val expr = Intersection(
      MultisetLiteral(IntT, Vector(IntLit(1)(Unsourced), IntLit(2)(Unsourced)))(Unsourced),
      MultisetLiteral(IntT, Vector(IntLit(3)(Unsourced)))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } intersection mset[int] { 3 }" =>
    }
  }

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : Node) : String = printer.format(n)
    def show(t : Type) : String = printer.format(t)
  }
}
