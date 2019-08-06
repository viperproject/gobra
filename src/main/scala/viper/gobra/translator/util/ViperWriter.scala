package viper.gobra.translator.util

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer}
import viper.gobra.reporting.Source.RichViperNode
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context


object ViperWriter {

  sealed trait DataKind
  sealed trait DataSum

  case class DataContainer[T <: DataKind](data: Vector[T], remainder: Vector[DataKind]) {
    def combine(other: DataContainer[T]): DataContainer[T] =
      DataContainer(data ++ other.data, remainder ++ other.remainder)

    lazy val all: Vector[DataKind] = data ++ remainder
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
  }

  trait StmtKind extends DataKind

  case class StmtSum(
                      global: Vector[vpr.LocalVarDecl]
                    ) extends DataSum

  case object StmtKindCompanion extends DataKindCompanion[StmtKind, StmtSum] {

    case class Global(x: vpr.LocalVarDecl) extends StmtKind

    override def ownKind(point: DataKind): Option[StmtKind] = point match {
      case x: StmtKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[StmtKind]): StmtSum = {
      var global: Vector[vpr.LocalVarDecl] = Vector.empty

      data foreach {
        case Global(x) => global = global :+ x
      }

      StmtSum(global)
    }
  }

  trait ExprKind extends DataKind

  case class ExprSum(
                      local: Vector[vpr.LocalVarDecl],
                      stmt: Vector[vpr.Stmt]
                    ) extends DataSum

  case object ExprKindCompanion extends DataKindCompanion[ExprKind, ExprSum] {

    case class Local(x: vpr.LocalVarDecl) extends ExprKind
    case class Stmt(x: vpr.Stmt) extends ExprKind

    override def ownKind(point: DataKind): Option[ExprKind] = point match {
      case x: ExprKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[ExprKind]): ExprSum = {
      var local: Vector[vpr.LocalVarDecl] = Vector.empty
      var stmt: Vector[vpr.Stmt] = Vector.empty

      data foreach {
        case Local(x)  => local = local :+ x
        case Stmt(x) => stmt = stmt :+ x
      }

      ExprSum(local, stmt)
    }
  }

  sealed class LeveledViperWriter[K <: DataKind, S <: DataSum](companion: DataKindCompanion[K, S]) {

    def unit[R](res: R): Writer[R] =
      Writer(DataContainer.empty, res)

    def create[R](sum: Vector[DataKind], res: R): Writer[R] =
      Writer(companion.container(sum), res)

    def forceTo[R, K <: DataKind, S <: DataSum](w: Writer[R])(lvw: LeveledViperWriter[K, S]): lvw.Writer[R] =
      lvw.create(w.sum.all, w.res)

    def sequence[R](ws: Vector[Writer[R]]): Writer[Vector[R]] = {
      val (sums, ress) = ws.map(w => (w.sum, w.res)).unzip
      Writer(DataContainer.combine(sums), ress)
    }

    def option[R](ws: Option[Writer[R]]): Writer[Option[R]] = ws match {
      case Some(w) => w.map(Some(_))
      case None => unit(None)
    }

    def upWithWriter[NK <: DataKind, NS <: DataSum, R, Q](lvw: LeveledViperWriter[NK, NS])(w: Writer[R])(f: S => R => Q): lvw.Writer[Q] = {
      val nextRes = f(companion.sum(w.sum.data))(w.res)
      lvw.create(w.sum.remainder, nextRes)
    }

    def withInfo[R <: vpr.Node](src: in.Node)(w: Writer[R]): Writer[R] = w.map(_.withInfo(src))
    def withDeepInfo[R <: vpr.Node](src: in.Node)(w: Writer[R]): Writer[R] = w.map(_.withDeepInfo(src))

    case class Writer[+R](sum: DataContainer[K], res: R) {

      def execute: (S, R) = (companion.sum(sum.data), res)

      def run: (Vector[DataKind], R) = (sum.all, res)

      def foreach(fun: R => Unit): Unit = fun(res)

      def map[Q](fun: R => Q): Writer[Q] = copy(sum, fun(res))

      def isolate[Q, Z](fun: R => (Q, Z)): (Z, Writer[Q]) = {
        val (l, r) = fun(res)
        (r, map(_ => l))
      }

      def cut: (R, Writer[Unit]) = isolate(((), _))



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

      def flush: Writer[(S, R)] = copy(companion.container(sum.remainder), (companion.sum(sum.data), res))

      def partitionData(f: DataKind => Boolean): Writer[(R, Writer[Unit])] = {
        val (keep, remove) = sum.all.partition(f)
        create(keep, (res, create(remove, ())))
      }

      def flushW: Writer[(R, Writer[Unit])] = create(sum.remainder, (res, create(sum.data, ())))
    }

    def addGlobals(ctx: Context, globals: vpr.LocalVarDecl*): Writer[Context] =
      create(globals.toVector.map(StmtKindCompanion.Global), ctx.addVars(globals: _*))

    def addGlobals(globals: vpr.LocalVarDecl*): Writer[Unit] =
      create(globals.toVector.map(StmtKindCompanion.Global), ())

    def addStatements(stmts: vpr.Stmt*): Writer[Unit] =
      create(stmts.toVector.map(ExprKindCompanion.Stmt), ())

    def addLocals(locals: vpr.LocalVar*): Writer[Unit] =
      create(locals.toVector.map(l => ExprKindCompanion.Local(ViperUtil.toVarDecl(l))), ())

    def sequenceC[R](ctx: Context)(ws: Vector[Context => Writer[(R, Context)]]): Writer[(Vector[R], Context)] =
      ws.foldLeft(unit((Vector.empty[R], ctx))){ case (w, fw) =>
        for {
          (rs, c) <- w
          (r, nc) <- fw(c)
        } yield (rs :+ r, nc)
      }
  }



  case object MemberLevel extends LeveledViperWriter[MemberKind, MemberSum](MemberKindCompanion) {

    def memberS[R](w: StmtLevel.Writer[R]): Writer[(StmtSum, R)] = StmtLevel.up(w)(s => r => (s, r))

    def splitE[R](w: ExprLevel.Writer[R]): Writer[(R, ExprLevel.Writer[Unit])] =
      ExprLevel.forceTo(w.partitionData{
        case _: MemberKind => true
        case _ => false
      })(this)

    def blockS(w: StmtLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = memberS(w).map{
      case (ss, s) => vpr.Seqn(Vector(s), ss.global)(s.pos, s.info, s.errT)
    }

    def blockE(w: ExprLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = blockS(StmtLevel.seqnE(w))
  }

  type MemberWriter[R] = MemberLevel.Writer[R]

  case object StmtLevel extends LeveledViperWriter[StmtKind, StmtSum](StmtKindCompanion) {

    def up[Q, R](w: Writer[R])(f: StmtSum => R => Q): MemberLevel.Writer[Q] =
      upWithWriter(MemberLevel)(w)(f)

    def stmtE[R](w: ExprLevel.Writer[R]): Writer[(ExprSum, R)] = ExprLevel.up(w)(s => r => (s, r))

    def closeE(w: ExprLevel.Writer[Unit])(src: vpr.Node): Writer[vpr.Stmt] = {
      stmtE(w).map{ case (es, _) =>
        val (pos, info, _) = src.getPrettyMetadata
        vpr.Seqn(es.stmt, es.local)(pos, info)
      }
    }


    def splitE[R](w: ExprLevel.Writer[R]): Writer[(R, ExprLevel.Writer[Unit])] =
      ExprLevel.up(w.flushW)(s => r => r)

    def seqnE(w: ExprLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = stmtE(w).map {
      case (es, s) => vpr.Seqn(es.stmt :+ s, es.local)(s.pos, s.info, s.errT)
    }

    def block(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] = w.flush.map{
      case (ss, s) => vpr.Seqn(Vector(s), ss.global)(s.pos, s.info, s.errT)
    }

    def blockE(w: ExprLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = block(seqnE(w))
  }

  type StmtWriter[R] = StmtLevel.Writer[R]

  case object ExprLevel extends LeveledViperWriter[ExprKind, ExprSum](ExprKindCompanion) {
    def up[R, Q](w: Writer[R])(f: ExprSum => R => Q): StmtLevel.Writer[Q] =
      upWithWriter(StmtLevel)(w)(f)

    def exprS[R](w: StmtLevel.Writer[R]): Writer[R] = create(w.sum.data ++ w.sum.remainder, w.res)

    def prelim[R <: vpr.Stmt](ws: StmtLevel.Writer[R]*): Writer[Unit] =
      sequence(ws.toVector map (w => exprS(w).flatMap(s => addStatements(s)))).map(_ => ())

    def splitWrittenStmts[R](w: Writer[R]): Writer[(R, Vector[vpr.Stmt])] = {
      val (dataWithStmt, dataWithout) = w.sum.data.partition(_.isInstanceOf[ExprKindCompanion.Stmt])
      val newWriter = Writer(DataContainer(dataWithout, w.sum.remainder), w.res)
      val stmts = dataWithStmt.collect{ case ExprKindCompanion.Stmt(x) => x }
      newWriter.map((_, stmts))
    }

  }

  type ExprWriter[R] = ExprLevel.Writer[R]

}
