// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend.PType
import viper.gobra.frontend.info.base.SymbolTable.{GhostTypeMember, _}

class ConstructMemberSet[M <: GhostTypeMember] private(
                                private val internal: Map[String, (M, Vector[MemberPath], Int)]
                                , private val duplicates: Set[String]
                              ) {

  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  type Record = (M, Vector[MemberPath], Int)

  def rank(path: Vector[MemberPath]): Int = path.count {
    case _: MemberPath.Next | _: MemberPath.EmbeddedInterface => true
    case _ => false
  }

  def union(other: ConstructMemberSet[M]): ConstructMemberSet[M] = {
    val keys = internal.keySet ++ other.internal.keySet
    val (newMap, newDups) = keys.map{k => (internal.get(k), other.internal.get(k)) match {
      case (Some(l@(_, _, rl)), Some(r@(_, _, rr))) =>
        (k -> (if (rl < rr) l else r), if (rl == rr) Some(k) else None)

      case (Some(l), None) => (k -> l, None)
      case (None, Some(r)) => (k -> r, None)
      case (None, None) => violation("Key not used by operand of union")
    }}.unzip

    new ConstructMemberSet[M](newMap.toMap, duplicates ++ other.duplicates ++ newDups.flatten)
  }

  private def updatePath(f: (M, Vector[MemberPath], Int) => (M, Vector[MemberPath], Int)): ConstructMemberSet[M] = {
    new ConstructMemberSet[M](internal.transform((_, value) => f.tupled(value)), duplicates)
  }

  def surface: ConstructMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Underlying +: p, l) }
  def promote(f: Embbed): ConstructMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Next(f) +: p, l + 1) }
  def promoteItf(id: String): ConstructMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.EmbeddedInterface(id) +: p, l + 1) }
  def ref: ConstructMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Deref +: p, l) }
  def deref: ConstructMemberSet[M] = updatePath { case (m, p, l) => (m, MemberPath.Ref +: p, l) }


  def lookup(key: String): Option[M] = internal.get(key).map(_._1)

  def lookupWithPath(key: String): Option[(M, Vector[MemberPath])] =
    internal.get(key).map(r => (r._1, r._2))

  def filter(f: GhostTypeMember => Boolean): ConstructMemberSet[M] =
    new ConstructMemberSet[M](internal.filter({ case (key, _) => f(internal(key)._1)}), duplicates)

  def collect[T](f: (String, M) ==> T): Vector[T] = internal.collect {
    case (n, (m, _, _)) if f.isDefinedAt(n, m) => f(n, m)
  }.toVector

  def forall(f: (String, (M, Vector[MemberPath])) => Boolean): Boolean =
    internal.forall{ case (s, (m, p, _)) => f(s, (m,p)) }

  def map[T](f: (String, (M, Vector[MemberPath])) => T): Vector[T] = internal.map {
    case (s, (m, p, _)) => f(s, (m,p))
  }.toVector

  def flatMap[T](f: (String, (M, Vector[MemberPath])) => Vector[T]): Vector[T] = internal.flatMap {
    case (s, (m, p, _)) => f(s, (m,p))
  }.toVector

  def containsAll(other: ConstructMemberSet[M]): Boolean = other.internal.keySet.forall(internal.contains)

  def toMap: Map[String, M] = internal.transform((_, value) => value._1)

  def valid: Boolean = duplicates.isEmpty

  def errors(src: PType): Messages = {
    duplicates.flatMap(n => message(src, s"type $src has member $n more than once")).toVector
  }
}

object ConstructMemberSet {

  def init[M <: GhostTypeMember](s: IterableOnce[M]): ConstructMemberSet[M] = {
    val nmp: Vector[(String, M)] = s.iterator.map { tm =>

      def extractType(tm: GhostTypeMember): String = tm match {
        case ConstructDecl(decl, _) => if (decl.spec.isShared) s"func(${decl.typ}) *${decl.typ}" else s"func(${decl.typ}) ${decl.typ}"
        case DerefDecl(decl, _) => s"func(*${decl.typ}) ${decl.typ}"
        case AssignDecl(decl, _) => s"func(*${decl.typ},${decl.typ}) void"
        case _ => ???
      }

      extractType(tm) -> tm
    }.toVector

    val groups = nmp.map(_._1).groupBy(identity)
    val member = nmp.toMap

    val dups: Set[String] = groups.collect{ case (x, ys) if ys.size > 1 => x }.toSet
    val distinct = groups.keySet

    new ConstructMemberSet[M](distinct.map(n => n -> (member(n), Vector.empty[MemberPath], 0)).toMap, dups)
  }

  def empty[M <: GhostTypeMember]: ConstructMemberSet[M] = new ConstructMemberSet[M](Map.empty[String, (M, Vector[MemberPath], Int)], Set.empty[String])

  def union[M <: GhostTypeMember](mss: Vector[ConstructMemberSet[M]]): ConstructMemberSet[M] = mss.size match {
    case 0 => empty
    case 1 => mss.head
    case _ => mss.tail.fold(mss.head) { case (l, r) => l union r }
  }
}