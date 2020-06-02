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
    val expr = EmptySequence(IntT)(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty (nested) Boolean sequence") {
    val expr = EmptySequence(SequenceT(SequenceT(BoolT)))(Unsourced)

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
      Vector(
        IntLit(BigInt(2))(Unsourced),
        IntLit(BigInt(4))(Unsourced),
        IntLit(BigInt(8))(Unsourced)
      )
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq { 2, 4, 8 }" =>
    }
  }

  test("Printer: should correctly show a singleton integer sequence literal") {
    val expr = SequenceLiteral(
      Vector(IntLit(BigInt(42))(Unsourced))
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq { 42 }" =>
    }
  }

  test("Printer: should correctly show an empty sequence literal") {
    val expr = SequenceLiteral(Vector())(Unsourced)

    frontend.show(expr) should matchPattern {
      case "seq { }" =>
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
    val expr = EmptySet(IntT)(Unsourced)
    frontend.show(expr) should matchPattern {
      case "set[int] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested set") {
    val expr = EmptySet(SetT(BoolT))(Unsourced)
    frontend.show(expr) should matchPattern {
      case "set[set[bool]] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = SetLiteral(Vector(IntLit(42)(Unsourced)))(Unsourced)
    frontend.show(expr) should matchPattern {
      case "set { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = SetLiteral(Vector(
      BoolLit(false)(Unsourced),
      BoolLit(true)(Unsourced),
      BoolLit(true)(Unsourced)
    ))(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set { false, true, true }" =>
    }
  }

  test("Printer: should show a set union as expected") {
    val expr = SetUnion(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      LocalVar.Ref("t", SequenceT(BoolT))(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s union t" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (1)") {
    val expr = SetUnion(
      SetUnion(
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
    val expr = SetUnion(
      LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      SetUnion(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      )(Unsourced)
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "s union t union u" =>
    }
  }

  test("Printer: should correctly show set union in combination with literals") {
    val expr = SetUnion(
      SetLiteral(Vector(
        LocalVar.Ref("s", SequenceT(BoolT))(Unsourced),
      ))(Unsourced),
      SetLiteral(Vector(
        LocalVar.Ref("t", SequenceT(BoolT))(Unsourced),
        LocalVar.Ref("u", SequenceT(BoolT))(Unsourced)
      ))(Unsourced),
    )(Unsourced)

    frontend.show(expr) should matchPattern {
      case "set { s } union set { t, u }" =>
    }
  }

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : Node) : String = printer.format(n)
    def show(t : Type) : String = printer.format(t)
  }
}
