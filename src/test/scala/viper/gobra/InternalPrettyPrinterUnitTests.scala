package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.internal._
import viper.gobra.reporting.Source.Parser.Internal
import viper.gobra.theory.Addressability

class InternalPrettyPrinterUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  val intT = IntT(Addressability.Exclusive)
  val boolT = BoolT(Addressability.Exclusive)
  def sequenceT(memT: Type): Type = SequenceT(memT, Addressability.Exclusive)
  def setT(memT: Type): Type = SetT(memT, Addressability.Exclusive)
  def multisetT(memT: Type): Type = MultisetT(memT, Addressability.Exclusive)

  test("Printer: should correctly show a standard sequence index expression") {
    val expr = SequenceIndex(
      LocalVar.Val("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(42))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[42]" =>
    }
  }

  test("Printer: should correctly show a sequence update expression") {
    val expr = SequenceUpdate(
      LocalVar.Val("xs", sequenceT(boolT))(Internal),
      IntLit(BigInt(4))(Internal),
      BoolLit(false)(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[4 = false]" =>
    }
  }

  test("Printer: should correctly show an empty integer sequence") {
    val expr = SequenceLit(intT, Vector())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty (nested) Boolean sequence") {
    val expr = SequenceLit(sequenceT(sequenceT(boolT)), Vector())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[seq[seq[bool]]] { }" =>
    }
  }

  test("Printer: should correctly show a sequence range expression") {
    val expr = RangeSequence(
      IntLit(BigInt(2))(Internal),
      IntLit(BigInt(44))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[2 .. 44]" =>
    }
  }

  test("Printer: should correctly show a non-empty simple integer sequence literal") {
    val expr = SequenceLit(
      intT,
      Vector(
        IntLit(BigInt(2))(Internal),
        IntLit(BigInt(4))(Internal),
        IntLit(BigInt(8))(Internal)
      )
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int] { 2, 4, 8 }" =>
    }
  }

  test("Printer: should correctly show a singleton integer sequence literal") {
    val expr = SequenceLit(
      intT,
      Vector(IntLit(BigInt(42))(Internal))
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show an empty sequence literal") {
    val expr = SequenceLit(boolT, Vector())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[bool] { }" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence drop operation") {
    val expr = SequenceDrop(
      LocalVar.Val("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(42))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[42:]" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence take operation") {
    val expr = SequenceTake(
      LocalVar.Val("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(4))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[:4]" =>
    }
  }

  test("Printer: should correctly show a sequence drop followed by a take") {
    val expr1 = SequenceDrop(
      LocalVar.Val("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(2))(Internal)
    )(Internal)
    val expr2 = SequenceTake(
      expr1, IntLit(BigInt(4))(Internal)
    )(Internal)

    frontend.show(expr2) should matchPattern {
      case "xs[2:][:4]" =>
    }
  }

  test("Printer: should correctly show an integer sequence type") {
    val t = sequenceT(intT)
    frontend.show(t) should matchPattern {
      case "seq[int]" =>
    }
  }

  test("Printer: should correctly show a nested sequence type") {
    val t = sequenceT(sequenceT(sequenceT(boolT)))
    frontend.show(t) should matchPattern {
      case "seq[seq[seq[bool]]]" =>
    }
  }

  test("Printer: should correctly show an integer set type") {
    val t = setT(intT)
    frontend.show(t) should matchPattern {
      case "set[int]" =>
    }
  }

  test("Printer: should correctly show a nested set type") {
    val t = setT(setT(setT(boolT)))
    frontend.show(t) should matchPattern {
      case "set[set[set[bool]]]" =>
    }
  }

  test("Printer: should correctly show an empty integer set") {
    val expr = SetLit(intT, Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "set[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested set") {
    val expr = SetLit(setT(boolT), Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "set[set[bool]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = SetLit(intT, Vector(IntLit(42)(Internal)))(Internal)
    frontend.show(expr) should matchPattern {
      case "set[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = SetLit(boolT, Vector(
      BoolLit(false)(Internal),
      BoolLit(true)(Internal),
      BoolLit(true)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { false, true, true }" =>
    }
  }

  test("Printer: should show a set union as expected") {
    val expr = Union(
      LocalVar.Val("s", setT(boolT))(Internal),
      LocalVar.Val("t", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s union t" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (1)") {
    val expr = Union(
      Union(
        LocalVar.Val("s", setT(boolT))(Internal),
        LocalVar.Val("t", setT(boolT))(Internal)
      )(Internal),
      LocalVar.Val("u", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (2)") {
    val expr = Union(
      LocalVar.Val("s", setT(boolT))(Internal),
      Union(
        LocalVar.Val("t", setT(boolT))(Internal),
        LocalVar.Val("u", setT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should correctly show set union in combination with literals") {
    val expr = Union(
      SetLit(boolT, Vector(
        LocalVar.Val("s", sequenceT(boolT))(Internal),
      ))(Internal),
      SetLit(boolT, Vector(
        LocalVar.Val("t", sequenceT(boolT))(Internal),
        LocalVar.Val("u", sequenceT(boolT))(Internal)
      ))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } union set[bool] { t, u }" =>
    }
  }

  test("Printer: should show a set intersection as expected") {
    val expr = Intersection(
      LocalVar.Val("s", setT(boolT))(Internal),
      LocalVar.Val("t", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s intersection t" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (1)") {
    val expr = Intersection(
      Intersection(
        LocalVar.Val("s", setT(boolT))(Internal),
        LocalVar.Val("t", setT(boolT))(Internal)
      )(Internal),
      LocalVar.Val("u", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (2)") {
    val expr = Intersection(
      LocalVar.Val("s", setT(boolT))(Internal),
      Intersection(
        LocalVar.Val("t", setT(boolT))(Internal),
        LocalVar.Val("u", setT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should correctly show set intersection in combination with literals") {
    val expr = Intersection(
      SetLit(boolT, Vector(
        LocalVar.Val("s", setT(boolT))(Internal),
      ))(Internal),
      SetLit(boolT, Vector(
        LocalVar.Val("t", boolT)(Internal),
        LocalVar.Val("u", boolT)(Internal)
      ))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } intersection set[bool] { t, u }" =>
    }
  }

  test("Printer: should show a set difference as expected") {
    val expr = SetMinus(
      LocalVar.Val("s", setT(boolT))(Internal),
      LocalVar.Val("t", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s setminus t" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (1)") {
    val expr = SetMinus(
      SetMinus(
        LocalVar.Val("s", setT(boolT))(Internal),
        LocalVar.Val("t", setT(boolT))(Internal)
      )(Internal),
      LocalVar.Val("u", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (2)") {
    val expr = SetMinus(
      LocalVar.Val("s", setT(boolT))(Internal),
      SetMinus(
        LocalVar.Val("t", setT(boolT))(Internal),
        LocalVar.Val("u", setT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should correctly show set differences in combination with literals") {
    val expr = SetMinus(
      SetLit(boolT, Vector(
        LocalVar.Val("s", setT(boolT))(Internal),
      ))(Internal),
      SetLit(boolT, Vector(
        LocalVar.Val("t", setT(boolT))(Internal),
        LocalVar.Val("u", setT(boolT))(Internal)
      ))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } setminus set[bool] { t, u }" =>
    }
  }

  test("Printer: should print a subset relation as expected") {
    val expr = Subset(
      LocalVar.Val("s", sequenceT(boolT))(Internal),
      LocalVar.Val("t", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s subset t" =>
    }
  }

  test("Printer: should print a chain of subset relations as expected") {
    val expr = Subset(
      Subset(
        LocalVar.Val("s", setT(boolT))(Internal),
        LocalVar.Val("t", setT(boolT))(Internal)
      )(Internal),
      LocalVar.Val("u", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s subset t subset u" =>
    }
  }

  test("Printer: should properly print a subset relation in combination with literals") {
    val expr = Subset(
      SetLit(intT, Vector(IntLit(42)(Internal)))(Internal),
      SetLit(boolT, Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[int] { 42 } subset set[bool] { }" =>
    }
  }

  test("Printer: should correctly show a standard sequence inclusion") {
    val expr = Contains(
      LocalVar.Val("x", sequenceT(boolT))(Internal),
      LocalVar.Val("xs", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in xs" =>
    }
  }

  test("Printer: should correctly show a 'chain' of sequence inclusions") {
    val expr = Contains(
      Contains(
        LocalVar.Val("x", sequenceT(boolT))(Internal),
        LocalVar.Val("xs", sequenceT(boolT))(Internal)
      )(Internal),
      LocalVar.Val("ys", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in xs in ys" =>
    }
  }

  test("Printer: should correctly show a simple set membership expression") {
    val expr = Contains(
      LocalVar.Val("x", sequenceT(boolT))(Internal),
      LocalVar.Val("s", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in s" =>
    }
  }

  test("Printer: should correctly show a small 'chain' of set membership expressions") {
    val expr = Contains(
      Contains(
        LocalVar.Val("x", boolT)(Internal),
        LocalVar.Val("s", setT(boolT))(Internal)
      )(Internal),
      LocalVar.Val("t", setT(boolT))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in s in t" =>
    }
  }

  test("Printer: should correctly show set membership in the context of literals") {
    val expr = Contains(
      SetLit(boolT, Vector(
        BoolLit(true)(Internal),
        BoolLit(false)(Internal))
      )(Internal),
      SetLit(setT(setT(intT)), Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { true, false } in set[set[set[int]]] { }" =>
    }
  }

  test("Printer: should correctly show the size of a simple set") {
    val expr = Cardinality(
      LocalVar.Val("s", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|s|" =>
    }
  }

  test("Printer: should correctly show the size of a set in combination with a set intersection") {
    val expr = Cardinality(
      Intersection(
        LocalVar.Val("s", setT(boolT))(Internal),
        LocalVar.Val("t", setT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|s intersection t|" =>
    }
  }

  test("Printer: should correctly show the size of a set literal") {
    val expr = Cardinality(
      SetLit(intT, Vector(
        IntLit(1)(Internal),
        IntLit(42)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|set[int] { 1, 42 }|" =>
    }
  }

  test("Printer: should correctly show the size of an empty set") {
    val expr = Cardinality(
      SetLit(sequenceT(intT), Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|set[seq[int]] { }|" =>
    }
  }

  test("Printer: should correctly show a simple integer multiset type") {
    val typ = multisetT(intT)
    frontend.show(typ) should matchPattern {
      case "mset[int]" =>
    }
  }

  test("Printer: should correctly show a nested multiset type") {
    val typ = multisetT(multisetT(boolT))
    frontend.show(typ) should matchPattern {
      case "mset[mset[bool]]" =>
    }
  }

  test("Printer: should correctly show an empty multiset integer literal") {
    val expr = MultisetLit(intT, Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "mset[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty multiset literal of a nested type") {
    val expr = MultisetLit(multisetT(multisetT(boolT)), Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "mset[mset[mset[bool]]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer multiset literal") {
    val expr = MultisetLit(intT, Vector(
      IntLit(42)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a singleton nested multiset literal") {
    val expr = MultisetLit(multisetT(boolT), Vector(
      MultisetLit(boolT, Vector(
        BoolLit(false)(Internal)
      ))(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[mset[bool]] { mset[bool] { false } }" =>
    }
  }

  test("Printer: should correctly show a non-empty integer multiset literal") {
    val expr = MultisetLit(intT, Vector(
      IntLit(1)(Internal),
      IntLit(2)(Internal),
      IntLit(3)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2, 3 }" =>
    }
  }

  test("Printer: should correctly show the union of two multiset integer literals") {
    val expr = Union(
      MultisetLit(intT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(intT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } union mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show the intersection of two multiset integer literals") {
    val expr = Intersection(
      MultisetLit(intT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(intT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } intersection mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show a subset relation applied to two multiset literals") {
    val expr = Subset(
      MultisetLit(intT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(intT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } subset mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show the cardinality of a multiset literal") {
    val expr = Cardinality(
      MultisetLit(multisetT(intT), Vector(
        MultisetLit(intT, Vector(
          IntLit(1)(Internal)
        ))(Internal),
        MultisetLit(intT, Vector(
          IntLit(2)(Internal),
          IntLit(3)(Internal),
        ))(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|mset[mset[int]] { mset[int] { 1 }, mset[int] { 2, 3 } }|" =>
    }
  }

  test("Printer: should correctly show a multiset inclusion expression (1)") {
    val expr = Contains(
      IntLit(2)(Internal),
      MultisetLit(intT, Vector(
        IntLit(1)(Internal),
        IntLit(2)(Internal),
        IntLit(3)(Internal),
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "2 in mset[int] { 1, 2, 3 }" =>
    }
  }

  test("Printer: should correctly show a multiset inclusion expression (2)") {
    val expr = Contains(
      MultisetLit(intT, Vector(
        IntLit(1)(Internal)
      ))(Internal),
      MultisetLit(intT, Vector(
        IntLit(2)(Internal),
        IntLit(3)(Internal),
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1 } in mset[int] { 2, 3 }" =>
    }
  }

  test("Printer: should correctly show a set conversion of an identifier") {
    val expr = SetConversion(
      LocalVar.Val("xs", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(xs)" =>
    }
  }

  test("Printer: should correctly show a simple set conversion of a range sequence") {
    val expr = SetConversion(
      RangeSequence(
        IntLit(1)(Internal),
        IntLit(42)(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(seq[1 .. 42])" =>
    }
  }

  test("Printer: should correctly show a set conversion with a sequence append") {
    val expr = SetConversion(
      SequenceAppend(
        LocalVar.Val("xs", sequenceT(boolT))(Internal),
        LocalVar.Val("ys", sequenceT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(xs ++ ys)" =>
    }
  }

  test("Printer: should correctly show the union of two set conversions") {
    val expr = Union(
      SetConversion(LocalVar.Val("xs", sequenceT(boolT))(Internal))(Internal),
      SetConversion(LocalVar.Val("ys", sequenceT(boolT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(xs) union set(ys)" =>
    }
  }

  test("Printer: should be able to show a very simple (sequence) multiplicity operator") {
    val expr = Multiplicity(
      LocalVar.Val("x", boolT)(Internal),
      LocalVar.Val("xs", sequenceT(boolT))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x # xs" =>
    }
  }

  test("Printer: should correctly show a slightly more complex (sequence) multiplicity operator") {
    val expr = Multiplicity(
      Add(IntLit(40)(Internal), IntLit(2)(Internal))(Internal),
      SequenceLit(intT, Vector(
        IntLit(1)(Internal),
        IntLit(2)(Internal),
        IntLit(3)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "40 + 2 # seq[int] { 1, 2, 3 }" =>
    }
  }

  test("Printer: should be able to show a nested (sequence) multiplicity") {
    val expr = Multiplicity(
      Multiplicity(
        LocalVar.Val("x", boolT)(Internal),
        LocalVar.Val("xs", sequenceT(boolT))(Internal),
      )(Internal),
      LocalVar.Val("ys", sequenceT(intT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x # xs # ys" =>
    }
  }

  test("Printer: should correctly show a very simple multiset conversion") {
    val expr = MultisetConversion(
      LocalVar.Val("xs", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs)" =>
    }
  }

  test("Printer: should correctly show a multiset conversion with a sequence concatenation in the inner expression") {
    val expr = MultisetConversion(
      SequenceAppend(
        LocalVar.Val("xs", sequenceT(boolT))(Internal),
        LocalVar.Val("ys", sequenceT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs ++ ys)" =>
    }
  }

  test("Printer: should correctly show the union of two multiset conversions") {
    val expr = Union(
      MultisetConversion(LocalVar.Val("xs", sequenceT(boolT))(Internal))(Internal),
      MultisetConversion(LocalVar.Val("ys", sequenceT(boolT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs) union mset(ys)" =>
    }
  }

  test("Printer: should correctly show a nested multiset conversion") {
    val expr = MultisetConversion(
      MultisetConversion(
        LocalVar.Val("xs", sequenceT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(mset(xs))" =>
    }
  }


  /* * Stubs, mocks, and other test setup  */

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : Node) : String = printer.format(n)
    def show(t : Type) : String = printer.format(t)
  }
}
