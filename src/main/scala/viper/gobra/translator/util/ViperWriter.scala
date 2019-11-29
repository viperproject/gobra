package viper.gobra.translator.util

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer}
import viper.gobra.reporting.Source.RichViperNode
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.translator.Names
import viper.gobra.translator.util.ViperWriter.MemberKindCompanion.{ErrorT, ReasonT}
import viper.gobra.util.Violation


object ViperWriter {

  sealed trait DataKind
  sealed trait DataSum

  case class DataContainer[T <: DataKind](data: Vector[T], remainder: Vector[DataKind]) {
    def combine(other: DataContainer[T]): DataContainer[T] =
      DataContainer(data ++ other.data, remainder ++ other.remainder)

    lazy val all: Vector[DataKind] = data ++ remainder

    def split: (DataContainer[T], Vector[DataKind]) =
      (DataContainer(data, Vector.empty), remainder)
  }

  object DataContainer {
    def empty[T <: DataKind]: DataContainer[T] = DataContainer(Vector.empty, Vector.empty)

    def combine[T <: DataKind](sums: Vector[DataContainer[T]]): DataContainer[T] = {
      val (datas, remainders) = sums.map(d => (d.data, d.remainder)).unzip
      DataContainer(datas.flatten, remainders.flatten)
    }
  }

  sealed trait DataKindCompanion[K <: DataKind, S <: DataSum] {
    def sum(data: Vector[K]): S
    def unsum(sum: S): Vector[K]
    def ownKind(point: DataKind): Option[K]

    def container(data: Vector[DataKind]): DataContainer[K] = {
      val (own, other) = data.foldLeft[(Vector[K], Vector[DataKind])]((Vector.empty, Vector.empty)){
        case ((ow, ot), e) => ownKind(e) match {
          case None    => (ow, e +: ot)
          case Some(k) => (k +: ow, ot)
        }
      }

      DataContainer(own, other)
    }

    def container(sum: S): DataContainer[K] = container(unsum(sum))
  }




  trait MemberKind extends DataKind

  case class MemberSum(
                        errorT: Vector[ErrorTransformer],
                        reasonT: Vector[ReasonTransformer]
                      ) extends DataSum

  case object MemberKindCompanion extends DataKindCompanion[MemberKind, MemberSum] {

    case class ErrorT(x: ErrorTransformer) extends MemberKind
    case class ReasonT(x: ReasonTransformer) extends MemberKind


    override def ownKind(point: DataKind): Option[MemberKind] = point match {
      case x: MemberKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[MemberKind]): MemberSum = {
      var errorT: Vector[ErrorTransformer] = Vector.empty
      var reasonT: Vector[ReasonTransformer] = Vector.empty

      data foreach {
        case ErrorT(x)  => errorT  = errorT :+ x
        case ReasonT(x) => reasonT = reasonT :+ x
      }

      MemberSum(errorT, reasonT)
    }

    override def unsum(s: MemberSum): Vector[MemberKind] =
      (s.errorT map ErrorT) ++ (s.reasonT map ReasonT)
  }

  trait CodeKind extends DataKind

  case class Global(x: vpr.Declaration) extends CodeKind
  case class Local(x: vpr.Declaration) extends CodeKind

  // to preserve the order of stmt-like output
  sealed trait Code extends CodeKind {
    def right: vpr.Stmt
    def left: vpr.Stmt
    def isPure: Boolean
  }

  case class Prelude(x: vpr.Stmt) extends Code {
    override lazy val right: vpr.Stmt = x
    override lazy val left: vpr.Stmt = x
    override val isPure: Boolean = false
  }

  case class WellDef(x: vpr.Exp) extends Code {
    override lazy val right: vpr.Stmt = vpr.Assert(x)(x.pos, x.info, x.errT)
    override lazy val left: vpr.Stmt = vpr.Assume(x)(x.pos, x.info, x.errT)
    override val isPure: Boolean = true
  }

  case class Binding(v: vpr.LocalVar, e: vpr.Exp) extends Code {
    override lazy val right: vpr.Stmt = vpr.LocalVarAssign(v, e)(e.pos, e.info, e.errT)
    override lazy val left: vpr.Stmt = left
    override val isPure: Boolean = true
  }

  case class CodeSum(
                      global: Vector[vpr.Declaration],
                      local: Vector[vpr.Declaration],
                      code: Vector[Code]
                    ) extends DataSum {
    lazy val left: Vector[vpr.Stmt] = code.map(_.left)
    lazy val right: Vector[vpr.Stmt] = code.map(_.right)
  }

  case object CodeKindCompanion extends DataKindCompanion[CodeKind, CodeSum] {

    override def ownKind(point: DataKind): Option[CodeKind] = point match {
      case x: CodeKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[CodeKind]): CodeSum = {
      var global: Vector[vpr.Declaration] = Vector.empty
      var local: Vector[vpr.Declaration] = Vector.empty
      var code: Vector[Code] = Vector.empty

      data foreach {
        case Global(x) => global = global :+ x
        case Local(x)  => local = local :+ x
        case x: Code   => code = code :+ x
      }

      CodeSum(global, local, code)
    }

    override def unsum(s: CodeSum): Vector[CodeKind] =
      (s.global map Global) ++ (s.local map Local) ++ s.code
  }


  sealed class LeveledViperWriter[K <: DataKind, S <: DataSum](companion: DataKindCompanion[K, S]) {

    def unit[R](res: R): Writer[R] =
      Writer(DataContainer.empty, res)

    def create[R](sum: Vector[DataKind], res: R): Writer[R] =
      Writer(companion.container(sum), res)

    def sequence[R](ws: Vector[Writer[R]]): Writer[Vector[R]] = {
      val (sums, ress) = ws.map(w => (w.sum, w.res)).unzip
      Writer(DataContainer.combine(sums), ress)
    }

    def option[R](ws: Option[Writer[R]]): Writer[Option[R]] = ws match {
      case Some(w) => w.map(Some(_))
      case None => unit(None)
    }

    def move[R,Q](w: Writer[R])(f: S => R => (S, Q)): Writer[Q] = {
      val (codeSum, remainder, r) = w.execute
      val (newS, newR) = f(codeSum)(r)
      val newData = DataContainer(companion.unsum(newS), remainder)
      w.copy(newData, newR)
    }

    def withInfo[R <: vpr.Node](src: in.Node)(w: Writer[R]): Writer[R] = w.map(_.withInfo(src))
    def withDeepInfo[R <: vpr.Node](src: in.Node)(w: Writer[R]): Writer[R] = w.map(_.withDeepInfo(src))


    case class Writer[+R](sum: DataContainer[K], res: R) {

      def execute: (S, Vector[DataKind], R) = (companion.sum(sum.data), sum.remainder, res)

      def run: (Vector[DataKind], R) = (sum.all, res)

      @inline
      def foreach(fun: R => Unit): Unit = fun(res)

      @inline
      def map[Q](fun: R => Q): Writer[Q] = copy(sum, fun(res))

      def cut: (R, Writer[Unit]) = (res, map(_ => ()))

      @inline
      def flatMap[Q](fun: R => Writer[Q]): Writer[Q] = {
        val next = fun(res)
        copy(sum.combine(next.sum), next.res)
      }

      def withFilter(fun: R => Boolean): Writer[R] = {
        assert(fun(res))
        this
      }

      def copy[Q](newSum: DataContainer[K] = sum, newRes: Q = res): Writer[Q] = {
        Writer(newSum, newRes)
      }
    }
  }



  case object MemberLevel extends LeveledViperWriter[MemberKind, MemberSum](MemberKindCompanion) {

    def split[R](w: CodeLevel.Writer[R]): Writer[(R, CodeLevel.Writer[Unit])] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.res, w.copy(codeData, ())))
    }

    def block(w: CodeLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = {
      val (codeSum, remainder, r) = w.execute
      val newR = vpr.Seqn(codeSum.code.map(_.right) :+ r, codeSum.local ++ codeSum.global)(r.pos, r.info, r.errT)
      create(remainder, newR)
    }

    def errorT(errTs: ErrorTransformer*): Writer[Unit] =
      create(errTs.toVector.map(ErrorT), ())

    def reasonR(reaTs: ReasonTransformer*): Writer[Unit] =
      create(reaTs.toVector.map(ReasonT), ())
  }

  type MemberWriter[R] = MemberLevel.Writer[R]



  case object CodeLevel extends LeveledViperWriter[CodeKind, CodeSum](CodeKindCompanion) {

    def assumeUnit[R](w: Writer[R]): MemberLevel.Writer[vpr.Exp] = assumeExp(w.map(_ => vpr.TrueLit()()))
    def assertUnit[R](w: Writer[R]): MemberLevel.Writer[vpr.Exp] = assertExp(w.map(_ => vpr.TrueLit()()))

    def assumeExp(w: Writer[vpr.Exp]): MemberLevel.Writer[vpr.Exp] = pure(w, assume = true)
    def assertExp(w: Writer[vpr.Exp]): MemberLevel.Writer[vpr.Exp] = pure(w)

    def withoutWellDef[R](w: Writer[R]): Writer[R] = {
      val newCode = w.sum.data.filter(!_.isInstanceOf[WellDef])
      val newData = w.sum.copy(data = newCode)
      w.copy(newData)
    }

    def pure(w: Writer[vpr.Exp], assume: Boolean = false): MemberLevel.Writer[vpr.Exp] = {
      val (codeSum, remainder, r) = w.execute
      require(!codeSum.code.exists(_.isPure))
      val newR = codeSum.code.foldRight(r){
        case (WellDef(c), e) =>
          if (assume) vpr.Implies(c, e)(e.pos, e.info, e.errT) // c => e
          else vpr.And(c, e)(e.pos, e.info, e.errT) // c && e

        case (Binding(lhs, rhs), e) =>
          vpr.Let(ViperUtil.toVarDecl(lhs), rhs, e)(e.pos, e.info, e.errT) // let lhs = rhs in e

        case _ => Violation.violation("pure expected but impure output found")
      }
      MemberLevel.create(remainder, newR)
    }

    def seqns(ws: Vector[Writer[vpr.Stmt]], assume: Boolean = false): Writer[vpr.Seqn] =
      sequence(ws.map(seqn(_, assume))).map(vpr.Seqn(_, Vector.empty)())

    def seqn(w: Writer[vpr.Stmt], assume: Boolean = false): Writer[vpr.Seqn] =
      move(w){ s => r =>
        val newSum = s.copy(local = Vector.empty, code = Vector.empty)
        val code = if (assume) s.left else s.right
        (newSum, vpr.Seqn(code :+ r, s.local)(r.pos, r.info, r.errT))
      }

    def seqnUnits(ws: Vector[Writer[Unit]], assume: Boolean = false): Writer[vpr.Seqn] =
      sequence(ws.map(seqnUnit(_, assume))).map(vpr.Seqn(_, Vector.empty)())

    def seqnUnit(w: Writer[Unit], assume: Boolean = false): Writer[vpr.Seqn] =
      move(w){ s => _ =>
        val newSum = s.copy(local = Vector.empty, code = Vector.empty)
        val code = if (assume) s.left else s.right
        (newSum, vpr.Seqn(code, s.local)())
      }

    def block(w: Writer[vpr.Stmt], assume: Boolean = false): Writer[vpr.Seqn] =
      move(w){ s => r =>
        val newSum = s.copy(global = Vector.empty, local = Vector.empty, code = Vector.empty)
        val code = if (assume) s.left else s.right
        (newSum, vpr.Seqn(code :+ r, s.local ++ s.global)(r.pos, r.info, r.errT))
      }

    def split[R](w: Writer[R]): Writer[(Writer[Unit], R)] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.copy(codeData, ()), w.res))
    }

    def write(stmts: vpr.Stmt*): Writer[Unit] =
      create(stmts.toVector.map(Prelude), ())

    def bind(lhs: vpr.LocalVar, rhs: vpr.Exp): Writer[Unit] =
      create(Vector(Binding(lhs, rhs)), ())

    def wellDef(cond: vpr.Exp*): Writer[Unit] =
      create(cond.toVector.map(WellDef), ())

    def local(locals: vpr.Declaration*): Writer[Unit] =
      create(locals.toVector.map(Local), ())

    def global(globals: vpr.Declaration*): Writer[Unit] =
      create(globals.toVector.map(Global), ())

    def errorT(errTs: ErrorTransformer*): Writer[Unit] =
      create(errTs.toVector.map(ErrorT), ())

    def reasonR(reaTs: ReasonTransformer*): Writer[Unit] =
      create(reaTs.toVector.map(ReasonT), ())

    def copyResult(r: vpr.Exp): CodeWriter[vpr.LocalVar] = {
      val z = vpr.LocalVar(Names.freshName, r.typ)(r.pos, r.info, r.errT)
      for {
        _ <- local(vu.toVarDecl(z))
        _ <- bind(z, r)
      } yield z
    }
  }

  type CodeWriter[R] = CodeLevel.Writer[R]

}
