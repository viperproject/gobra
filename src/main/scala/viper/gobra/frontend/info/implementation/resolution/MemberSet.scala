package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend.PType
import viper.gobra.frontend.info.base.SymbolTable._


class MemberSet private(
                         private val internal: Map[String, (TypeMember, Vector[MemberPath], Int)]
                         , private val duplicates: Set[String]
                       ) {

  import org.bitbucket.inkytonik.kiama.==>
  import scala.collection.breakOut
  import viper.gobra.util.Violation._

  type Record = (TypeMember, Vector[MemberPath], Int)

  def rank(path: Vector[MemberPath]): Int = path.count {
    case _: MemberPath.Next => true
    case _ => false
  }

  def union(other: MemberSet): MemberSet = {
    val keys = internal.keySet ++ other.internal.keySet
    val (newMap, newDups) = keys.map{k => (internal.get(k), other.internal.get(k)) match {
      case (Some(l@(_, _, rl)), Some(r@(_, _, rr))) =>
        (k -> (if (rl < rr) l else r), if (rl == rr) Some(k) else None)

      case (Some(l), None) => (k -> l, None)
      case (None, Some(r)) => (k -> r, None)
      case (None, None) => violation("Key not used by operand of union")
    }}.unzip

    new MemberSet(newMap.toMap, duplicates ++ other.duplicates ++ newDups.flatten)
  }

  private def updatePath(f: (TypeMember, Vector[MemberPath], Int) => (TypeMember, Vector[MemberPath], Int)): MemberSet =
    new MemberSet(internal.mapValues(f.tupled), duplicates)

  def surface: MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Underlying +: p, l) }

  def promote(f: Embbed): MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Next(f) +: p, l + 1) }

  def ref: MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Deref +: p, l) }

  def deref: MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Ref +: p, l) }


  def lookup(key: String): Option[TypeMember] = internal.get(key).map(_._1)

  def lookupWithPath(key: String): Option[(TypeMember, Vector[MemberPath])] =
    internal.get(key).map(r => (r._1, r._2))

  def filter(f: TypeMember => Boolean): MemberSet =
    new MemberSet(internal.filterKeys(n => f(internal(n)._1)), duplicates)

  def methodSet: MemberSet = filter(m => m.isInstanceOf[Method])

  def collect[T](f: (String, TypeMember) ==> T): Vector[T] = internal.collect {
    case (n, (m, _, _)) if f.isDefinedAt(n, m) => f(n, m)
  }(breakOut)

  def implements(other: MemberSet): Boolean = other.internal.keySet.forall(internal.contains)

  def toMap: Map[String, TypeMember] = internal.mapValues(_._1)

  def valid: Boolean = duplicates.isEmpty

  def errors(src: PType): Messages = {
    duplicates.flatMap(n => message(src, s"type $src has member $n more than once"))(breakOut)
  }
}

object MemberSet {

  import scala.collection.breakOut

  def init(s: TraversableOnce[TypeMember]): MemberSet = {
    val nmp: Vector[(String, TypeMember)] = s.map {
      case e@ MethodImpl(m) => m.id.name -> e
      case e@ MethodSpec(m) => m.id.name -> e
      case e@ Field(m)      => m.id.name -> e
      case e@ Embbed(m)     => m.id.name -> e
    }.toVector

    val groups = nmp.unzip._1.groupBy(identity)
    val member = nmp.toMap

    val dups: Set[String] = groups.collect{ case (x, ys) if ys.size > 1 => x }(breakOut)
    val distinct = groups.keySet

    new MemberSet(distinct.map(n => n -> (member(n), Vector.empty[MemberPath], 0)).toMap, dups)
  }

  def empty: MemberSet = new MemberSet(Map.empty[String, (TypeMember, Vector[MemberPath], Int)], Set.empty[String])

  def union(mss: Vector[MemberSet]): MemberSet = mss.size match {
    case 0 => empty
    case 1 => mss.head
    case _ => mss.tail.fold(mss.head) { case (l, r) => l union r }
  }
}



