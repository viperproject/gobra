import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer}
import viper.silver.{ast => vpr}


for {
  _ <- Some(println("gello"))
  Some(x) <- Some(Some(5))
} yield x

//sealed trait DataKind
//sealed trait DataSum
//
//sealed trait MemberKind extends DataKind
//sealed trait StmtKind extends DataKind
//sealed trait ExprKind extends DataKind
//
//sealed trait DataContainer {
//  def combine(other: this.type): this.type
//}

//case class Writer[+S <: DataContainer, +R](s: S, r: R) {
//  def map[Q](f: R => Q): Writer[S, Q] = Writer(s, f(r))
//  def flatMap[Q](f: R => Writer[S, Q]): Writer[S, Q] = {
//    val z = f(r)
//    Writer(s.combine(z.s), z.r)
//  }
//}

//
//case class DataContainer[T <: DataKind](data: Vector[T], remainder: Vector[DataKind]) {
//  def combine(other: DataContainer[T]): DataContainer[T] =
//    DataContainer(data ++ other.data, remainder ++ other.remainder)
//}
//
//object DataContainer {
//  def combine[T <: DataKind](sums: Vector[DataContainer[T]]): DataContainer[T] = {
//    val (datas, remainders) = sums.map(d => (d.data, d.remainder)).unzip
//    DataContainer(datas.flatten, remainders.flatten)
//  }
//}
//
//sealed trait DataKindCompanion[K <: DataKind, S <: DataSum] {
//  def sum(data: Vector[K]): S
//  def ownKind(point: DataKind): Option[K]
//
//  def container(data: Vector[DataKind]): DataContainer[K] = {
//    val (own, other) = data.foldLeft[(Vector[K], Vector[DataKind])]((Vector.empty, Vector.empty)){
//      case ((ow, ot), e) => ownKind(e) match {
//        case None    => (ow, e +: ot)
//        case Some(k) => (k +: ow, ot)
//      }
//    }
//
//    DataContainer(own, other)
//  }
//}
//
//
//
//trait MemberKind extends DataKind
//
//case class MemberSum(
//                      errorT: Vector[ErrorTransformer],
//                      reasonT: Vector[ReasonTransformer]
//                    ) extends DataSum
//
//case object MemberKindCompanion extends DataKindCompanion[MemberKind, MemberSum] {
//
//  case class ErrorT(x: ErrorTransformer) extends MemberKind
//  case class ReasonT(x: ReasonTransformer) extends MemberKind
//
//
//  override def ownKind(point: DataKind): Option[MemberKind] = point match {
//    case x: MemberKind => Some(x)
//    case _ => None
//  }
//
//  override def sum(data: Vector[MemberKind]): MemberSum = {
//    var errorT: Vector[ErrorTransformer] = Vector.empty
//    var reasonT: Vector[ReasonTransformer] = Vector.empty
//
//    data foreach {
//      case ErrorT(x)  => errorT  = errorT :+ x
//      case ReasonT(x) => reasonT = reasonT :+ x
//    }
//
//    MemberSum(errorT, reasonT)
//  }
//}
//
//trait StmtKind extends DataKind
//
//case class StmtSum(
//                    global: Vector[vpr.LocalVarDecl]
//                  ) extends DataSum
//
//case object StmtKindCompanion extends DataKindCompanion[StmtKind, StmtSum] {
//
//  case class Global(x: vpr.LocalVarDecl) extends StmtKind
//
//  override def ownKind(point: DataKind): Option[StmtKind] = point match {
//    case x: StmtKind => Some(x)
//    case _ => None
//  }
//
//  override def sum(data: Vector[StmtKind]): StmtSum = {
//    var global: Vector[vpr.LocalVarDecl] = Vector.empty
//
//    data foreach {
//      case Global(x) => global = global :+ x
//    }
//
//    StmtSum(global)
//  }
//}
//
//trait ExprKind extends DataKind
//
//case class ExprSum(
//                    local: Vector[vpr.LocalVarDecl],
//                    stmt: Vector[vpr.Stmt]
//                  ) extends DataSum
//
//case object ExprKindCompanion extends DataKindCompanion[ExprKind, ExprSum] {
//
//  case class Local(x: vpr.LocalVarDecl) extends ExprKind
//  case class Stmt(x: vpr.Stmt) extends ExprKind
//
//  override def ownKind(point: DataKind): Option[ExprKind] = point match {
//    case x: ExprKind => Some(x)
//    case _ => None
//  }
//
//  override def sum(data: Vector[ExprKind]): ExprSum = {
//    var local: Vector[vpr.LocalVarDecl] = Vector.empty
//    var stmt: Vector[vpr.Stmt] = Vector.empty
//
//    data foreach {
//      case Local(x)  => local = local :+ x
//      case Stmt(x) => stmt = stmt :+ x
//    }
//
//    ExprSum(local, stmt)
//  }
//}



//sealed trait DataKind
//sealed trait DataSum
//

//




//
//class Writer2[K <: DataKind, S <: DataSum, R] {
//
//}
//
//class LeveledViperWriter[K <: DataKind, S <: DataSum](companion: DataKindCompanion[K, S]) {
//
//  def unit[R](res: R): Writer[R] =
//    Writer(DataContainer(Vector.empty, Vector.empty), res)
//
//  def create[R](sum: Vector[DataKind], res: R): Writer[R] =
//    Writer(companion.container(sum), res)
//
//  def sequence[R](ws: Vector[Writer[R]]): Writer[Vector[R]] = {
//    val (sums, ress) = ws.map(w => (w.sum, w.res)).unzip
//    Writer(DataContainer.combine(sums), ress)
//  }
//
//  def upWithWriter[NK <: DataKind, NS <: DataSum, R, Q](lvw: LeveledViperWriter[NK, NS])(w: Writer[R])(f: S => R => Q): lvw.Writer[Q] = {
//    val nextRes = f(companion.sum(w.sum.data))(w.res)
//    lvw.create(w.sum.remainder, nextRes)
//  }
//
//  case class Writer[+R](sum: DataContainer[K], res: R){
//
//    def run: (S, R) = (companion.sum(sum.data), res)
//
//    def map[Q](fun: R => Q): Writer[Q] = copy(sum, fun(res))
//
//    def flatMap[Q](fun: R => Writer[Q]): Writer[Q] = {
//      val next = fun(res)
//      copy(sum.combine(next.sum), next.res)
//    }
//
//    def copy[Q](newSum: DataContainer[K] = sum, newRes: Q = res): Writer[Q] = {
//      Writer(newSum, newRes)
//    }
//
//    def flush: Writer[(S, R)] = copy(companion.container(sum.remainder), (companion.sum(sum.data), res))
//  }
//
//}
//
//case object MemberLevel extends LeveledViperWriter[MemberKind, MemberSum](MemberKindCompanion)
//
//type MemberWriter[R] = MemberLevel.Writer[R]
//
//case object StmtLevel extends LeveledViperWriter[StmtKind, StmtSum](StmtKindCompanion) {
//  def up[Q, R](w: Writer[R])(f: StmtSum => R => Q): MemberLevel.Writer[Q] =
//    upWithWriter(MemberLevel)(w)(f)
//
//  def block(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] = w.flush.map{
//    case (ss, s) => vpr.Seqn(Vector(s), ss.global)(s.pos, s.info, s.errT)
//  }
//
//  def member[R](w: Writer[R]): MemberLevel.Writer[(StmtSum, R)] = up(w)(s => r => (s, r))
//}
//
//case object ExprLevel extends LeveledViperWriter[ExprKind, ExprSum](ExprKindCompanion) {
//  def up[R, Q](w: Writer[R])(f: ExprSum => R => Q): StmtLevel.Writer[Q] =
//    upWithWriter(StmtLevel)(w)(f)
//
//  def seqn(w: Writer[vpr.Stmt]): StmtLevel.Writer[vpr.Seqn] = stmt(w).map {
//    case (es, s) => vpr.Seqn(es.stmt :+ s, es.local)(s.pos, s.info, s.errT)
//  }
//
//  def addLocals(ls: vpr.LocalVarDecl*): Writer[Unit] =
//    create(ls.toVector.map(ExprKindCompanion.Local), ())
//
//  def block(w: Writer[vpr.Stmt]): StmtLevel.Writer[vpr.Seqn] = StmtLevel.block(seqn(w))
//
//  def stmt[R](w: Writer[R]): StmtLevel.Writer[(ExprSum, R)] = up(w)(s => r => (s, r))
//}
//
//type ExprWriter[R] = ExprLevel.Writer[R]
//
//
//val x: ExprWriter[Int] = ExprLevel.unit(7)
//val y: ExprWriter[Int] = ExprLevel.unit(9)
//for {
//  _ <- ExprLevel.addLocals(vpr.LocalVarDecl("hey", vpr.Int)())
//  l <- x
//  r <- y
//} yield l + r
//
//
//class A
//class B extends A
//
//
//
//
//val bs: Vector[B] = Vector.empty
//val as: Vector[A] = bs
//
//println("fello")