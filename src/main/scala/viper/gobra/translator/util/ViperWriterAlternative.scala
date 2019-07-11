package viper.gobra.translator.util

import viper.gobra.util.WriterUtil1._
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}

object ViperWriterAlternative {

  case class MemberSum()

  private implicit val memberSumMonoid: Monoid[MemberSum] = new Monoid[MemberSum] {
    override def empty: MemberSum = MemberSum()
    override def combine(l: MemberSum, r: MemberSum): MemberSum = MemberSum()
  }

  case class StmtSum(
                      global: Vector[vpr.LocalVarDecl]
                    )

  private implicit val stmtSumMonoid: Monoid[StmtSum] = new Monoid[StmtSum] {
    override def empty: StmtSum = StmtSum(Vector.empty)
    override def combine(l: StmtSum, r: StmtSum): StmtSum = StmtSum(l.global ++ r.global)
  }

  case class ExprSum(
                    local: Vector[vpr.LocalVarDecl],
                    stmts: Vector[vpr.Stmt]
                    )

  private implicit val exprSumMonoid: Monoid[ExprSum] = new Monoid[ExprSum] {
    override def empty: ExprSum = ExprSum(Vector.empty, Vector.empty)
    override def combine(l: ExprSum, r: ExprSum): ExprSum =
      ExprSum(l.local ++ r.local, l.stmts ++ r.stmts)
  }



  class ExprWriter[+R](sum: ExprUpSum, res: R) extends Writer[ExprUpSum, R, ExprWriter]

  implicit object ExprWriter extends WriterCompanion[ExprUpSum, ExprWriter] {
    override def create[R](newSum: ExprUpSum, newRes: R): ExprWriter[R] = new ExprWriter(newSum, newRes)
    override def destruct[R](writer: ExprWriter[R]): (ExprUpSum, R) = writer.run
  }


  class StmtWriter[+R](sum: StmtUpSum, res: R) extends Writer[StmtUpSum, R, StmtWriter]

  implicit object StmtWriter extends WriterCompanion[StmtUpSum, StmtWriter] {
    override def create[R](newSum: StmtUpSum, newRes: R): StmtWriter[R] = new StmtWriter(newSum, newRes)
    override def destruct[R](writer: StmtWriter[R]): (StmtUpSum, R) = writer.run
  }


  class MemberWriter[+R](sum: MemberSum, res: R) extends Writer[MemberSum, R, MemberWriter]

  implicit object MemberWriter extends WriterCompanion[MemberSum, MemberWriter] {
    override def create[R](newSum: MemberSum, newRes: R): MemberWriter[R] = new MemberWriter(newSum, newRes)
    override def destruct[R](writer: MemberWriter[R]): (MemberSum, R) = writer.run
  }


  case class StmtUpSum(head: StmtSum, tail: MemberSum) extends SplitSum[StmtSum, MemberSum]

  private implicit val stmtUpSumMonoid: Monoid[StmtUpSum] = new Monoid[StmtUpSum] {
    override def empty: StmtUpSum = StmtUpSum(stmtSumMonoid.empty, memberSumMonoid.empty)
    override def combine(l: StmtUpSum, r: StmtUpSum): StmtUpSum =
      StmtUpSum(stmtSumMonoid.combine(l.head, r.head), memberSumMonoid.combine(l.tail, r.tail))
  }

  case class ExprUpSum(head: ExprSum, tail: StmtUpSum) extends SplitSum[ExprSum, StmtUpSum]

  private implicit val exprUpSumMonoid: Monoid[ExprUpSum] = new Monoid[ExprUpSum] {
    override def empty: ExprUpSum = ExprUpSum(exprSumMonoid.empty, stmtUpSumMonoid.empty)
    override def combine(l: ExprUpSum, r: ExprUpSum): ExprUpSum =
      ExprUpSum(exprSumMonoid.combine(l.head, r.head), stmtUpSumMonoid.combine(l.tail, r.tail))
  }
}
