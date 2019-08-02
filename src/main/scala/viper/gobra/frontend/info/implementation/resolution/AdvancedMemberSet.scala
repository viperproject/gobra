package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend.PType
import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field, MethodImpl, MethodSpec, TypeMember}

class AdvancedMemberSet[M <: TypeMember] private(
                                private val internal: Map[String, (M, Vector[MemberPath], Int)]
                                , private val duplicates: Set[String]
                              ) {

  import org.bitbucket.inkytonik.kiama.==>
  import scala.collection.breakOut
  import viper.gobra.util.Violation._

  type Record = (M, Vector[MemberPath], Int)

  def rank(path: Vector[MemberPath]): Int = path.count {
    case _: MemberPath.Next => true
    case _ => false
  }

  def union(other: AdvancedMemberSet[M]): AdvancedMemberSet[M] = {
    val keys = internal.keySet ++ other.internal.keySet
    val (newMap, newDups) = keys.map{k => (internal.get(k), other.internal.get(k)) match {
      case (Some(l@(_, _, rl)), Some(r@(_, _, rr))) =>
        (k -> (if (rl < rr) l else r), if (rl == rr) Some(k) else None)

      case (Some(l), None) => (k -> l, None)
      case (None, Some(r)) => (k -> r, None)
      case (None, None) => violation("Key not used by operand of union")
    }}.unzip

    new AdvancedMemberSet[M](newMap.toMap, duplicates ++ other.duplicates ++ newDups.flatten)
  }

  private def updatePath(f: (M, Vector[MemberPath], Int) => (M, Vector[MemberPath], Int)): AdvancedMemberSet[M] =
    new AdvancedMemberSet[M](internal.mapValues(f.tupled), duplicates)

  def surface: AdvancedMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Underlying +: p, l) }
  def promote(f: Embbed): AdvancedMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Next(f) +: p, l + 1) }
  def ref: AdvancedMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Deref +: p, l) }
  def deref: AdvancedMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Ref +: p, l) }


  def lookup(key: String): Option[M] = internal.get(key).map(_._1)

  def lookupWithPath(key: String): Option[(M, Vector[MemberPath])] =
    internal.get(key).map(r => (r._1, r._2))

  def filter(f: TypeMember => Boolean): AdvancedMemberSet[M] =
    new AdvancedMemberSet[M](internal.filterKeys(n => f(internal(n)._1)), duplicates)

  def collect[T](f: (String, M) ==> T): Vector[T] = internal.collect {
    case (n, (m, _, _)) if f.isDefinedAt(n, m) => f(n, m)
  }(breakOut)

  def containsAll(other: AdvancedMemberSet[M]): Boolean = other.internal.keySet.forall(internal.contains)

  def toMap: Map[String, M] = internal.mapValues(_._1)

  def valid: Boolean = duplicates.isEmpty

  def errors(src: PType): Messages = {
    duplicates.flatMap(n => message(src, s"type $src has member $n more than once"))(breakOut)
  }
}

object AdvancedMemberSet {

  import scala.collection.breakOut

  def init[M <: TypeMember](s: TraversableOnce[M]): AdvancedMemberSet[M] = {
    val nmp: Vector[(String, M)] = s.map { tm =>

      def extractMemberName(tm: TypeMember): String = tm match {
        case MethodImpl(m, _) => m.id.name
        case MethodSpec(m, _) => m.id.name
        case Field(m, _)      => m.id.name
        case Embbed(m, _)     => m.id.name
      }

      extractMemberName(tm) -> tm
    }.toVector

    val groups = nmp.map(_._1).groupBy(identity)
    val member = nmp.toMap

    val dups: Set[String] = groups.collect{ case (x, ys) if ys.size > 1 => x }(breakOut)
    val distinct = groups.keySet

    new AdvancedMemberSet[M](distinct.map(n => n -> (member(n), Vector.empty[MemberPath], 0)).toMap, dups)
  }

  def empty[M <: TypeMember]: AdvancedMemberSet[M] = new AdvancedMemberSet[M](Map.empty[String, (M, Vector[MemberPath], Int)], Set.empty[String])

  def union[M <: TypeMember](mss: Vector[AdvancedMemberSet[M]]): AdvancedMemberSet[M] = mss.size match {
    case 0 => empty
    case 1 => mss.head
    case _ => mss.tail.fold(mss.head) { case (l, r) => l union r }
  }
}