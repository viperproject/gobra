package viper.gobra.translator.util

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer}
import viper.gobra.reporting.Source.RichViperNode
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}


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

  case class CodeSum(
                      global: Vector[vpr.Declaration],
                      local: Vector[vpr.Declaration],
                      stmt: Vector[vpr.Stmt]
                    ) extends DataSum

  case object CodeKindCompanion extends DataKindCompanion[CodeKind, CodeSum] {

    case class Global(x: vpr.Declaration) extends CodeKind
    case class Local(x: vpr.Declaration) extends CodeKind
    case class Stmt(x: vpr.Stmt) extends CodeKind

    override def ownKind(point: DataKind): Option[CodeKind] = point match {
      case x: CodeKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[CodeKind]): CodeSum = {
      var global: Vector[vpr.Declaration] = Vector.empty
      var local: Vector[vpr.Declaration] = Vector.empty
      var stmt: Vector[vpr.Stmt] = Vector.empty

      data foreach {
        case Global(x) => global = global :+ x
        case Local(x)  => local = local :+ x
        case Stmt(x) => stmt = stmt :+ x
      }

      CodeSum(global, local, stmt)
    }

    override def unsum(s: CodeSum): Vector[CodeKind] =
      (s.global map Global) ++ (s.local map Local) ++ (s.stmt map Stmt)
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

      def cut: (Writer[Unit], R) = (map(_ => ()), res)

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
      val newR = vpr.Seqn(codeSum.stmt :+ r, codeSum.local ++ codeSum.global)(r.pos, r.info, r.errT)
      create(remainder, newR)
    }
  }

  type MemberWriter[R] = MemberLevel.Writer[R]



  case object CodeLevel extends LeveledViperWriter[CodeKind, CodeSum](CodeKindCompanion) {

    def emptySum[R](w: Writer[R]): Boolean = w.sum.all.isEmpty

    def seqn(ws: Vector[Writer[vpr.Stmt]]): Writer[vpr.Seqn] =
      sequence(ws.map(seqn)).map(vpr.Seqn(_, Vector.empty)())

    def seqn(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] =
      move(w){ s => r =>
        val newSum = s.copy(local = Vector.empty, stmt = Vector.empty)
        (newSum, vpr.Seqn(s.stmt :+ r, s.local)(r.pos, r.info, r.errT))
      }

    def seqnUnit(ws: Vector[Writer[Unit]]): Writer[vpr.Seqn] =
      sequence(ws.map(seqnUnit)).map(vpr.Seqn(_, Vector.empty)())

    def seqnUnit(w: Writer[Unit]): Writer[vpr.Seqn] =
      move(w){ s => _ =>
        val newSum = s.copy(local = Vector.empty, stmt = Vector.empty)
        (newSum, vpr.Seqn(s.stmt, s.local)())
      }

    def block(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] =
      move(w){ s => r =>
        val newSum = s.copy(global = Vector.empty, local = Vector.empty, stmt = Vector.empty)
        (newSum, vpr.Seqn(s.stmt :+ r, s.local ++ s.global)(r.pos, r.info, r.errT))
      }

    def split[R](w: Writer[R]): Writer[(Writer[Unit], R)] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.copy(codeData, ()), w.res))
    }

    def write(stmts: vpr.Stmt*): Writer[Unit] =
      create(stmts.toVector.map(CodeKindCompanion.Stmt), ())

    def local(locals: vpr.Declaration*): Writer[Unit] =
      create(locals.toVector.map(l => CodeKindCompanion.Local(ViperUtil.toVarDecl(l))), ())

    def global(globals: vpr.Declaration*): Writer[Unit] =
      create(globals.toVector.map(l => CodeKindCompanion.Global(ViperUtil.toVarDecl(l))), ())
  }

  type CodeWriter[R] = CodeLevel.Writer[R]

}
