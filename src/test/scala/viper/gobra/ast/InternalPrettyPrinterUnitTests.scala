package viper.gobra.ast

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.internal._
import viper.gobra.reporting.Source.Parser.Internal

class InternalPrettyPrinterUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Printer: should correctly show a standard sequence index expression") {
    val expr = IndexedExp(
      LocalVar.Ref("xs", SequenceT(IntT))(Internal),
      IntLit(BigInt(42))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[42]" =>
    }
  }

  test("Printer: should correctly show a sequence update expression") {
    val expr = SequenceUpdate(
      LocalVar.Ref("xs", SequenceT(BoolT))(Internal),
      IntLit(BigInt(4))(Internal),
      BoolLit(false)(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[4 = false]" =>
    }
  }

  test("Printer: should correctly show an empty integer sequence") {
    val expr = SequenceLit(IntT, Vector())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty (nested) Boolean sequence") {
    val expr = SequenceLit(SequenceT(SequenceT(BoolT)), Vector())(Internal)

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
      IntT,
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
      IntT,
      Vector(IntLit(BigInt(42))(Internal))
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show an empty sequence literal") {
    val expr = SequenceLit(BoolT, Vector())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[bool] { }" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence drop operation") {
    val expr = SequenceDrop(
      LocalVar.Ref("xs", SequenceT(IntT))(Internal),
      IntLit(BigInt(42))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[42:]" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence take operation") {
    val expr = SequenceTake(
      LocalVar.Ref("xs", SequenceT(IntT))(Internal),
      IntLit(BigInt(4))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[:4]" =>
    }
  }

  test("Printer: should correctly show a sequence drop followed by a take") {
    val expr1 = SequenceDrop(
      LocalVar.Ref("xs", SequenceT(IntT))(Internal),
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
    val expr = SetLit(IntT, Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "set[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested set") {
    val expr = SetLit(SetT(BoolT), Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "set[set[bool]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = SetLit(IntT, Vector(IntLit(42)(Internal)))(Internal)
    frontend.show(expr) should matchPattern {
      case "set[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = SetLit(BoolT, Vector(
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
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      LocalVar.Ref("t", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s union t" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (1)") {
    val expr = Union(
      Union(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
        LocalVar.Ref("t", SequenceT(BoolT))(Internal)
      )(Internal),
      LocalVar.Ref("u", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (2)") {
    val expr = Union(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      Union(
        LocalVar.Ref("t", SequenceT(BoolT))(Internal),
        LocalVar.Ref("u", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should correctly show set union in combination with literals") {
    val expr = Union(
      SetLit(BoolT, Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      ))(Internal),
      SetLit(BoolT, Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Internal),
        LocalVar.Ref("u", SequenceT(BoolT))(Internal)
      ))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } union set[bool] { t, u }" =>
    }
  }

  test("Printer: should show a set intersection as expected") {
    val expr = Intersection(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      LocalVar.Ref("t", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s intersection t" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (1)") {
    val expr = Intersection(
      Intersection(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
        LocalVar.Ref("t", SequenceT(BoolT))(Internal)
      )(Internal),
      LocalVar.Ref("u", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (2)") {
    val expr = Intersection(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      Intersection(
        LocalVar.Ref("t", SequenceT(BoolT))(Internal),
        LocalVar.Ref("u", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s intersection t intersection u" =>
    }
  }

  test("Printer: should correctly show set intersection in combination with literals") {
    val expr = Intersection(
      SetLit(BoolT, Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      ))(Internal),
      SetLit(BoolT, Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Internal),
        LocalVar.Ref("u", SequenceT(BoolT))(Internal)
      ))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } intersection set[bool] { t, u }" =>
    }
  }

  test("Printer: should show a set difference as expected") {
    val expr = SetMinus(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      LocalVar.Ref("t", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s setminus t" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (1)") {
    val expr = SetMinus(
      SetMinus(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
        LocalVar.Ref("t", SequenceT(BoolT))(Internal)
      )(Internal),
      LocalVar.Ref("u", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (2)") {
    val expr = SetMinus(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      SetMinus(
        LocalVar.Ref("t", SequenceT(BoolT))(Internal),
        LocalVar.Ref("u", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s setminus t setminus u" =>
    }
  }

  test("Printer: should correctly show set differences in combination with literals") {
    val expr = SetMinus(
      SetLit(BoolT, Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      ))(Internal),
      SetLit(BoolT, Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Internal),
        LocalVar.Ref("u", SequenceT(BoolT))(Internal)
      ))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { s } setminus set[bool] { t, u }" =>
    }
  }

  test("Printer: should print a subset relation as expected") {
    val expr = Subset(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal),
      LocalVar.Ref("t", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s subset t" =>
    }
  }

  test("Printer: should print a chain of subset relations as expected") {
    val expr = Subset(
      Subset(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
        LocalVar.Ref("t", SequenceT(BoolT))(Internal)
      )(Internal),
      LocalVar.Ref("u", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s subset t subset u" =>
    }
  }

  test("Printer: should properly print a subset relation in combination with literals") {
    val expr = Subset(
      SetLit(IntT, Vector(IntLit(42)(Internal)))(Internal),
      SetLit(BoolT, Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[int] { 42 } subset set[bool] { }" =>
    }
  }

  test("Printer: should correctly show a standard sequence inclusion") {
    val expr = Contains(
      LocalVar.Ref("x", SequenceT(BoolT))(Internal),
      LocalVar.Ref("xs", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in xs" =>
    }
  }

  test("Printer: should correctly show a 'chain' of sequence inclusions") {
    val expr = Contains(
      Contains(
        LocalVar.Ref("x", SequenceT(BoolT))(Internal),
        LocalVar.Ref("xs", SequenceT(BoolT))(Internal)
      )(Internal),
      LocalVar.Ref("ys", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in xs in ys" =>
    }
  }

  test("Printer: should correctly show a simple set membership expression") {
    val expr = Contains(
      LocalVar.Ref("x", SequenceT(BoolT))(Internal),
      LocalVar.Ref("s", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in s" =>
    }
  }

  test("Printer: should correctly show a small 'chain' of set membership expressions") {
    val expr = Contains(
      Contains(
        LocalVar.Ref("x", SequenceT(BoolT))(Internal),
        LocalVar.Ref("s", SequenceT(BoolT))(Internal)
      )(Internal),
      LocalVar.Ref("t", SequenceT(BoolT))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x in s in t" =>
    }
  }

  test("Printer: should correctly show set membership in the context of literals") {
    val expr = Contains(
      SetLit(BoolT, Vector(
        BoolLit(true)(Internal),
        BoolLit(false)(Internal))
      )(Internal),
      SetLit(SetT(SetT(IntT)), Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool] { true, false } in set[set[set[int]]] { }" =>
    }
  }

  test("Printer: should correctly show the size of a simple set") {
    val expr = Cardinality(
      LocalVar.Ref("s", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|s|" =>
    }
  }

  test("Printer: should correctly show the size of a set in combination with a set intersection") {
    val expr = Cardinality(
      Intersection(
        LocalVar.Ref("s", SequenceT(BoolT))(Internal),
        LocalVar.Ref("t", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "|s intersection t|" =>
    }
  }

  test("Printer: should correctly show the size of a set literal") {
    val expr = Cardinality(
      SetLit(IntT, Vector(
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
      SetLit(SequenceT(IntT), Vector())(Internal)
    )(Internal)

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
    val expr = MultisetLit(IntT, Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "mset[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty multiset literal of a nested type") {
    val expr = MultisetLit(MultisetT(MultisetT(BoolT)), Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "mset[mset[mset[bool]]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer multiset literal") {
    val expr = MultisetLit(IntT, Vector(
      IntLit(42)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 42 }" =>
    }
  }

  test("Printer: should correctly show a singleton nested multiset literal") {
    val expr = MultisetLit(MultisetT(BoolT), Vector(
      MultisetLit(BoolT, Vector(
        BoolLit(false)(Internal)
      ))(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[mset[bool]] { mset[bool] { false } }" =>
    }
  }

  test("Printer: should correctly show a non-empty integer multiset literal") {
    val expr = MultisetLit(IntT, Vector(
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
      MultisetLit(IntT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(IntT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } union mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show the intersection of two multiset integer literals") {
    val expr = Intersection(
      MultisetLit(IntT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(IntT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } intersection mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show a subset relation applied to two multiset literals") {
    val expr = Subset(
      MultisetLit(IntT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(IntT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int] { 1, 2 } subset mset[int] { 3 }" =>
    }
  }

  test("Printer: should correctly show the cardinality of a multiset literal") {
    val expr = Cardinality(
      MultisetLit(MultisetT(IntT), Vector(
        MultisetLit(IntT, Vector(
          IntLit(1)(Internal)
        ))(Internal),
        MultisetLit(IntT, Vector(
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
      MultisetLit(IntT, Vector(
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
      MultisetLit(IntT, Vector(
        IntLit(1)(Internal)
      ))(Internal),
      MultisetLit(IntT, Vector(
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
      LocalVar.Ref("xs", SequenceT(BoolT))(Internal)
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
        LocalVar.Ref("xs", SequenceT(BoolT))(Internal),
        LocalVar.Ref("ys", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(xs ++ ys)" =>
    }
  }

  test("Printer: should correctly show the union of two set conversions") {
    val expr = Union(
      SetConversion(LocalVar.Ref("xs", SequenceT(BoolT))(Internal))(Internal),
      SetConversion(LocalVar.Ref("ys", SequenceT(BoolT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(xs) union set(ys)" =>
    }
  }

  test("Printer: should be able to show a very simple (sequence) multiplicity operator") {
    val expr = Multiplicity(
      LocalVar.Ref("x", BoolT)(Internal),
      LocalVar.Ref("xs", SequenceT(BoolT))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x # xs" =>
    }
  }

  test("Printer: should correctly show a slightly more complex (sequence) multiplicity operator") {
    val expr = Multiplicity(
      Add(IntLit(40)(Internal), IntLit(2)(Internal))(Internal),
      SequenceLit(IntT, Vector(
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
        LocalVar.Ref("x", BoolT)(Internal),
        LocalVar.Ref("xs", SequenceT(BoolT))(Internal),
      )(Internal),
      LocalVar.Ref("ys", SequenceT(IntT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "x # xs # ys" =>
    }
  }

  test("Printer: should correctly show a very simple multiset conversion") {
    val expr = MultisetConversion(
      LocalVar.Ref("xs", SequenceT(BoolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs)" =>
    }
  }

  test("Printer: should correctly show a multiset conversion with a sequence concatenation in the inner expression") {
    val expr = MultisetConversion(
      SequenceAppend(
        LocalVar.Ref("xs", SequenceT(BoolT))(Internal),
        LocalVar.Ref("ys", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs ++ ys)" =>
    }
  }

  test("Printer: should correctly show the union of two multiset conversions") {
    val expr = Union(
      MultisetConversion(LocalVar.Ref("xs", SequenceT(BoolT))(Internal))(Internal),
      MultisetConversion(LocalVar.Ref("ys", SequenceT(BoolT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs) union mset(ys)" =>
    }
  }

  test("Printer: should correctly show a nested multiset conversion") {
    val expr = MultisetConversion(
      MultisetConversion(
        LocalVar.Ref("xs", SequenceT(BoolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(mset(xs))" =>
    }
  }

  test("Printer: should correctly show a simple exclusive integer array type") {
    val t = ExclusiveArrayT(42, IntT)

    frontend.show(t) should matchPattern {
      case "[42]int" =>
    }
  }

  test("Printer: should correctly show an exclusive multidimensional Boolean array type") {
    val t = ExclusiveArrayT(1, ExclusiveArrayT(2, ExclusiveArrayT(3, BoolT)))

    frontend.show(t) should matchPattern {
      case "[1][2][3]bool" =>
    }
  }

  test("Printer: should correctly show an exclusive sequence array type") {
    val t = ExclusiveArrayT(12, SequenceT(IntT))

    frontend.show(t) should matchPattern {
      case "[12]seq[int]" =>
    }
  }

  test("Printer: should correctly show a sequence type of exclusive Boolean arrays") {
    val t = SequenceT(ExclusiveArrayT(42, BoolT))

    frontend.show(t) should matchPattern {
      case "seq[[42]bool]" =>
    }
  }

  test("Printer: should correctly show a very simple exclusive array length expression") {
    val expr = Length(
      LocalVar.Ref("a", ExclusiveArrayT(12, IntT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(a)" =>
    }
  }

  test("Printer: should correctly show a slightly more complex array length expression") {
    val expr = Length(
      Add(
        Cardinality(LocalVar.Ref("s", SetT(BoolT))(Internal))(Internal),
        IntLit(42)(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(|s| + 42)" =>
    }
  }

  test("Printer: should correctly show a nested array length expression") {
    val expr = Length(Length(IntLit(42)(Internal))(Internal))(Internal)

    frontend.show(expr) should matchPattern {
      case "len(len(42))" =>
    }
  }

  test("Printer: should be able to show the addition of two uses of the array length function") {
    val expr = Add(
      Length(LocalVar.Ref("a", ExclusiveArrayT(24, BoolT))(Internal))(Internal),
      Length(LocalVar.Ref("b", ExclusiveArrayT(24, BoolT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(a) + len(b)" =>
    }
  }

  test("Printer: should be able to show a very simple sequence length expression") {
    val expr = Length(
      LocalVar.Ref("xs", SequenceT(IntT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(xs)" =>
    }
  }

  test("Printer: should be able to show a slightly more complicated use of the sequence length function") {
    val expr = Length(
      SequenceAppend(
        SequenceLit(BoolT, Vector(BoolLit(false)(Internal)))(Internal),
        IntLit(12)(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(seq[bool] { false } ++ 12)" =>
    }
  }

  test("Printer: should correctly show a nested use of the built-in sequence length operator") {
    val expr = Length(
      Length(SequenceLit(IntT, Vector())(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(len(seq[int] { }))" =>
    }
  }

  test("Printer: should correctly show a composition of two sequence length function applications") {
    val expr = Add(
      Length(LocalVar.Ref("xs", SequenceT(IntT))(Internal))(Internal),
      Length(LocalVar.Ref("ys", SequenceT(IntT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(xs) + len(ys)" =>
    }
  }

  test("Printer: should correctly show a simple array indexing expression") {
    val expr = IndexedExp(
      LocalVar.Ref("a", ExclusiveArrayT(124, IntT))(Internal),
      IntLit(42)(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "a[42]" =>
    }
  }

  test("Printer: should correctly show an array indexing expression with a slightly more complex base") {
    val expr = IndexedExp(
      SequenceAppend(
        SequenceLit(BoolT, Vector(BoolLit(false)(Internal)))(Internal),
        Length(LocalVar.Ref("xs", SequenceT(BoolT))(Internal))(Internal)
      )(Internal),
      IntLit(42)(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[bool] { false } ++ len(xs)[42]" =>
    }
  }

  test("Printer: should correctly show an array indexing operation with a slightly more complex right-hand side") {
    val expr = IndexedExp(
      LocalVar.Ref("a", ExclusiveArrayT(124, IntT))(Internal),
      Add(
        LocalVar.Ref("x", IntT)(Internal),
        IntLit(2)(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "a[x + 2]" =>
    }
  }

  test("Printer: should correctly show a small chain of array indexing operations") {
    val expr = IndexedExp(
      IndexedExp(
        LocalVar.Ref("a", ExclusiveArrayT(12, ExclusiveArrayT(24, BoolT)))(Internal),
        IntLit(2)(Internal)
      )(Internal),
      IntLit(4)(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "a[2][4]" =>
    }
  }

  test("Printer: should correctly show a simple accessible indexed expression") {
    val expr = Accessible.Index(
      IndexedExp(
        LocalVar.Ref("a", ExclusiveArrayT(12, ExclusiveArrayT(24, BoolT)))(Internal),
        Add(IntLit(2)(Internal), IntLit(3)(Internal))(Internal)
      )(Internal)
    )

    frontend.show(expr) should matchPattern {
      case "a[2 + 3]" =>
    }
  }

  test("Printer: should be able to correctly show a very simple 'acc' predicate applied on an array") {
    val expr = Access(
      Accessible.Index(
        IndexedExp(
          LocalVar.Ref("a", ExclusiveArrayT(12, ExclusiveArrayT(24, BoolT)))(Internal),
          IntLit(2)(Internal)
        )(Internal)
      )
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "acc(a[2])" =>
    }
  }

  test("Printer: should correctly show a simple assignee indexed expression") {
    val expr = Assignee.Index(
      IndexedExp(
        LocalVar.Ref("a", ExclusiveArrayT(12, ExclusiveArrayT(24, BoolT)))(Internal),
        Add(IntLit(2)(Internal), IntLit(3)(Internal))(Internal)
      )(Internal)
    )

    frontend.show(expr) should matchPattern {
      case "a[2 + 3]" =>
    }
  }

  test("Printer: should correctly show a simple integer array literal") {
    val expr = ArrayLit(IntT, Vector(
      IntLit(12)(Internal),
      IntLit(24)(Internal),
      IntLit(48)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "[3]int { 12, 24, 48 }" =>
    }
  }

  test("Printer: should correctly show an empty Boolean array literal") {
    val expr = ArrayLit(BoolT, Vector())(Internal)

    frontend.show(expr) should matchPattern {
      case "[0]bool { }" =>
    }
  }

  test("Printer: should correctly show a nested array literal") {
    val expr = ArrayLit(
      ExclusiveArrayT(2, IntT),
      Vector(
        ArrayLit(IntT, Vector(
          IntLit(24)(Internal),
          IntLit(42)(Internal)
        ))(Internal)
      )
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "[1][2]int { [2]int { 24, 42 } }" =>
    }
  }

  test("Printer: should correctly show an empty 3D array literal") {
    val expr = ArrayLit(
      ExclusiveArrayT(24, ExclusiveArrayT(48, BoolT)), Vector()
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "[0][24][48]bool { }" =>
    }
  }

  test("Printer: should correctly show a sequence array literal") {
    val expr = ArrayLit(
      SequenceT(IntT),
      Vector(SequenceLit(IntT, Vector())(Internal))
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "[1]seq[int] { seq[int] { } }" =>
    }
  }

  test("Printer: should correctly show a shared (integer) array type") {
    val typ = SharedArrayT(12, IntT)

    frontend.show(typ) should matchPattern {
      case "[12]int" =>
    }
  }

  test("Printer: should correctly show a nested shared (Boolean) array type") {
    val typ = SharedArrayT(12, SharedArrayT(24, BoolT))

    frontend.show(typ) should matchPattern {
      case "[12][24]bool" =>
    }
  }

  test("Printer: should correctly show a nesting of a combination of shared and exclusive array types") {
    val typ = ExclusiveArrayT(4, SharedArrayT(7, ExclusiveArrayT(9, IntT)))

    frontend.show(typ) should matchPattern {
      case "[4][7][9]int" =>
    }
  }

  test("Printer: should correctly show an (internal) copy expression to a shared array") {
    val expr = ArrayCopy(
      LocalVar.Ref("a", ExclusiveArrayT(24, BoolT))(Internal),
      ArrayKind.Shared()
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "copy(a)" =>
    }
  }

  test("Printer: should correctly show an (internal) copy expression to an exclusive array") {
    val expr = ArrayCopy(
      LocalVar.Ref("arr", SharedArrayT(24, BoolT))(Internal),
      ArrayKind.Exclusive()
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "copy(arr)" =>
    }
  }

  test("Printer: should correctly show a simple sequence conversion expression") {
    val expr = SequenceConversion(
      SequenceLit(IntT, Vector(
        IntLit(1)(Internal),
        IntLit(2)(Internal),
        IntLit(4)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq(seq[int] { 1, 2, 4 })" =>
    }
  }

  test("Printer: should correctly show a simple conversion expression from an array literal to a sequence") {
    val expr = SequenceConversion(
      ArrayLit(BoolT, Vector(
        BoolLit(false)(Internal),
        BoolLit(true)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq([2]bool { false, true })" =>
    }
  }

  test("Printer: should correctly show the append of two sequence conversion operations") {
    val expr = SequenceAppend(
      SequenceConversion(LocalVar.Ref("xs", SequenceT(IntT))(Internal))(Internal),
      SequenceConversion(LocalVar.Ref("a", ExclusiveArrayT(6, IntT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq(xs) ++ seq(a)" =>
    }
  }


  /* * Stubs, mocks, and other test setup  */

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : Node) : String = printer.format(n)
    def show(t : Type) : String = printer.format(t)
  }
}
