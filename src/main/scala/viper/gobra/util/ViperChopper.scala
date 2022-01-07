// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util


import viper.gobra.util.ViperChopper.Cut.MergePenalty
import viper.silver.{ast => vpr}
import viper.gobra.util.ViperChopper.Vertex.Always

import scala.annotation.tailrec
import scala.collection.mutable

object ViperChopper {

  /** chops 'choppee' into independent Viper programs */
  def chop(choppee: vpr.Program)(
    isolate: Option[vpr.Method => Boolean] = None,
    bound: Option[Int] = Some(1),
    mergePenalty: MergePenalty[SCC.Component[Vertex]] = Cut.Penalty.Default
  ): Vector[vpr.Program] = {

    val isolatedVertices = isolate.map{ f =>
      choppee.members.collect{ case x: vpr.Method if f(x) => Vertex.Method(x.name): Vertex }
    }

    val edges = choppee.members.flatMap(Edges.dependencies).distinct

    val requiredVertices = isolatedVertices.getOrElse {
      choppee.collect {
        case m: vpr.Method => Vertex.Method(m.name)
        case f: vpr.Function => Vertex.Function(f.name)
      }.toSeq
    }
    val vertices = (requiredVertices ++ edges.flatten{case (l, r) => List(l, r)}).distinct

    val (components, _, componentDAG) = SCC.compute(vertices, edges)
    val componentIsolate = isolatedVertices.map{ s => (x: SCC.Component[Vertex]) => x.nodes.exists(s.contains) }
    val subtrees = Cut.boundedCut(components, componentDAG.toVector)(componentIsolate, bound, mergePenalty)

    val alwaysIncludedVertices = edges.collect{ case (e: Vertex, Always) => e }.toSet
    val programs = subtrees.map(subtree => subtree.flatMap(_.nodes) ++ alwaysIncludedVertices)
    programs.map(Vertex.inverse(choppee))
  }

  object SCC {

    case class Component[T](nodes: Seq[T])

    def components[T](vertices: Seq[T], edges: Seq[Edge[T]]): Vector[Component[T]] = {

      // Implements Tarjan's strongly connected components algorithm
      var index = 0
      val stack = mutable.Stack[T]()
      val indices = mutable.HashMap[T, Int]()
      val lowLinks = mutable.HashMap[T, Int]()
      val onStack = mutable.HashMap[T, Boolean]()
      val components = mutable.ArrayBuffer[Component[T]]()

      // helper function which performs most of the work
      def strongConnect(v: T): Unit = {
        // initialize all values needed for the current vertex
        indices.addOne(v, index)
        lowLinks.addOne(v, index)
        index += 1
        stack.push(v)
        onStack.addOne(v, true)
        // find all successors
        val succs = edges.collect{ case (`v`, succ) => succ }
        for (s <- succs) {
          if (!indices.contains(s)) {
            // successor has not been visited yet
            strongConnect(s)
            lowLinks.update(v, Math.min(lowLinks.getOrElse(v, -1), lowLinks.getOrElse(s, -1)))
          } else if (onStack.getOrElse(s, false)) {
            // s is already on the stack and therefore in the current SSC
            lowLinks.update(v, Math.min(lowLinks.getOrElse(v, -1), indices.getOrElse(s, -1)))
          }
        }
        // if v is a root node, create new SCC from stack
        if (lowLinks.getOrElse(v, -1) == indices.getOrElse(v, -2)) {
          val component = mutable.ArrayBuffer[T]()
          var curr = v
          do {
            curr = stack.pop()
            onStack.update(curr, false)
            component += curr
          } while (curr != v)
          components += Component(component.toSeq)
        }
      }
      // perform algorithm for all vertices
      for (v <- vertices if !indices.contains(v)) strongConnect(v)
      components.toVector
    }

    def compute[T](vertices: Seq[T], edges: Seq[Edge[T]]): (Vector[Component[T]], Map[T, Component[T]], Seq[Edge[Component[T]]]) = {
      val cs = components(vertices, edges)
      val inv = cs.flatMap(c => c.nodes.map(_ -> c)).toMap
      val cgraph = edges.map{ case (l,r) => (inv(l), inv(r)) }.filter{ case (l,r) => l != r }.distinct
      (cs, inv, cgraph)
    }

  }

  object Cut {

    private def smallestCut[T](nodes: Vector[T], forest: Vector[Edge[T]]): Vector[Set[T]] = {
      val roots = {
        val negatives = forest.map(_._2).toSet ++ Set(Vertex.Always)
        nodes.filterNot(negatives.contains)
      }

      val forestM = forest.groupMap(_._1)(_._2)
      def reachable (current: T): Vector[T] =
        current +: forestM.get(current).fold(Vector.empty[T])(_.flatMap(reachable))

      roots map (x => reachable(x).toSet)
    }

    def boundedCut[T](nodes: Vector[T], forest: Vector[Edge[T]])(
      isolate: Option[T => Boolean],
      bound: Option[Int],
      mergePenalty: MergePenalty[T]
    ): Vector[Set[T]] = {
      require(bound.forall(_ > 0), s"Got $bound as the size of the cut, but expected positive number")

      val t1 = System.nanoTime()
      val (mappedNodes, mappedForest, rev) = mergePenalty.init(nodes, forest)
      val start = smallestCut(mappedNodes, mappedForest)
      val filtered = isolate match {
        case None => start
        case Some(f) => start.filter(_.exists(node => f(mergePenalty.inverse(node, rev))))
      }

      val t2 = System.nanoTime()
      val entries = filtered.zipWithIndex.map{ case (set,idx) => (idx,mergePenalty.toProgram(set)) }
      val sets = mutable.Map(entries:_*)
      var counter = entries.size // not used as key in map
      def isAlive(x: (Int, Int)): Boolean = sets.contains(x._1) && sets.contains(x._2)

      val init = for {
        (aIdx, a) <- entries
        (bIdx, b) <- entries
        if aIdx < bIdx
        (penalty, rep) = mergePenalty.penaltyAndMerge(a,b)
      } yield (penalty, (aIdx, bIdx), rep)

      val queue = mutable.PriorityQueue(init:_*)(Ordering.by(- _._1))

      while (queue.headOption.exists(_._1 <= 0) || bound.exists(sets.size > _)) {
        var x = queue.dequeue()
        while (!isAlive(x._2)) { x = queue.dequeue() }

        if (x._1 <= 0 || bound.exists(sets.size > _)) {
          val (_, (lIdx, rIdx), newRep) = x
          sets.remove(lIdx); sets.remove(rIdx)
          val newIdx = counter
          counter += 1

          for ((idx, rep) <- sets) {
            val (penalty, newNewRep) = mergePenalty.penaltyAndMerge(rep, newRep)
            queue.enqueue((penalty, (idx, newIdx), newNewRep))
          }

          sets.put(newIdx, newRep)
        }
      }

      val result = mergePenalty.finish(sets.values.toVector, rev)
      val t3 = System.nanoTime()
      val timeCutting = BigDecimal((t2 - t1) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      val timeMerging = BigDecimal((t3 - t2) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)

      println(s"Chopped verification condition into ${result.size} parts. Maximum number of parts is ${filtered.size}. (Time: ${timeCutting}s + ${timeMerging}s)")

      result
    }

    sealed trait MergePenalty[T] {
      type Node
      type Program
      type Mapping

      def init(nodes: Vector[T], forest: Vector[Edge[T]]): (Vector[Node], Vector[Edge[Node]], Mapping)
      def inverse(node: Node, m: Mapping): T
      def toProgram(set: Set[Node]): Program
      def finish(y: Vector[Program], m: Mapping): Vector[Set[T]]
      def penaltyAndMerge(l: Program, r: Program): (Int, Program)
    }

    object Penalty {

      trait DefaultImpl extends MergePenalty[SCC.Component[Vertex]] {

        override type Node = (Int,Int)
        override type Program = List[(Int, Int)]
        override type Mapping = Map[Int, SCC.Component[Vertex]]

        override def init(nodes: Vector[SCC.Component[Vertex]], forest: Vector[Edge[SCC.Component[Vertex]]]): (Vector[Node], Vector[Edge[Node]], Mapping) = {
          var m = Map.empty[Int, SCC.Component[Vertex]]
          var rev = Map.empty[SCC.Component[Vertex], (Int,Int)]
          var counter = 0

          def get(component: SCC.Component[Vertex]): (Int, Int) = {
            rev.getOrElse(component, {
              val score = component.nodes.map(price).sum
              val id = counter

              counter += 1
              rev += (component -> (id, score))
              m += (id -> component)

              (id, score)
            })
          }

          (nodes.map(get), forest.map{ case (l,r) => (get(l), get(r))}, m)
        }

        override def inverse(node: Node, m: Mapping): SCC.Component[Vertex] = m(node._1)

        override def toProgram(set: Set[(Int, Int)]): List[(Int, Int)] = set.toList.sorted

        override def finish(y: Vector[List[(Int, Int)]], m: Map[Int, SCC.Component[Vertex]]): Vector[Set[SCC.Component[Vertex]]] = {

          // For safety, we check that every key of the map is used at least once.
          // If a key is not used, then this means that a component that was present
          // in the beginning is not present in the result.
          var safetyCheckSet = m.keySet

          val result = for (set <- y) yield {
            set.map{ case (id,_) =>
              safetyCheckSet -= id
              m(id)
            }.toSet
          }

          assert(safetyCheckSet.isEmpty, "Some Methods got lost during the merge set of the chopper")

          result
        }

        def price(xs: Vertex): Int = xs match {
          case _: Vertex.Method | _: Vertex.MethodSpec => 0
          case _: Vertex.Field => 1
          case _: Vertex.PredicateSig => 2
          case _: Vertex.PredicateBody => 3
          case _: Vertex.Function => 3
          case _: Vertex.DomainFunction => 1
          case _: Vertex.DomainType => 1
          case _: Vertex.DomainAxiom => 4
          case Vertex.Always => 0
        }

        override def penaltyAndMerge(l: List[(Int,Int)], r: List[(Int,Int)]): (Int, List[(Int,Int)]) = {

          @tailrec
          def go(l: List[(Int,Int)], r: List[(Int,Int)], a: Int, b: Int, c: Int, acc: List[(Int,Int)]): (Int, Int, Int, List[(Int,Int)]) = {
            (l, r) match {
              case (xs, Nil) => (a + xs.map(_._2).sum, b, c, acc.reverse ++ xs)
              case (Nil, ys) => (a, b + ys.map(_._2).sum, c, acc.reverse ++ ys)
              case ((xId, xScore) :: xs, (yId, yScore) :: ys) =>
                if (xId == yId)     go(xs, ys, a, b, c+xScore, (xId,xScore)::acc)
                else if (xId < yId) go(xs, r,  a+xScore, b, c, (xId,xScore)::acc)
                else                go(l,  ys, a, b+yScore, c, (yId,yScore)::acc)
            }
          }

          val (leftPrice, rightPrice, sharedPrice, merged) = go(l, r, 0, 0, 0, Nil)
          ((leftPrice + rightPrice) * ((20 + sharedPrice).toFloat / 20).toInt, merged)
        }
      }

      object Default extends DefaultImpl

      object NoAlways extends DefaultImpl {

        override def penaltyAndMerge(l: Program, r: Program): (Int, Program) = {
          val (penalty, rep) = Default.penaltyAndMerge(l,r)
          (if (penalty <= 0) 1 else penalty, rep)
        }

      }
    }
  }


  sealed trait Vertex
  object Vertex {
    case class Method(methodName: String) extends Vertex
    case class MethodSpec(methodName: String) extends Vertex
    case class Function(functionName: String) extends Vertex
    case class PredicateSig(predicateName: String) extends Vertex
    case class PredicateBody(predicateName: String) extends Vertex
    case class Field(fieldName: String) extends Vertex
    case class DomainFunction(funcName: String) extends Vertex
    case class DomainAxiom(v: vpr.DomainAxiom, d: vpr.Domain) extends Vertex
    case class DomainType(v: vpr.DomainType) extends Vertex
    case object Always extends Vertex // if something always has to be included

    /** Returns the subprogram induced by the set of vertices. */
    def inverse(program: vpr.Program): Set[Vertex] => vpr.Program = {
      val methodTable = program.methods.map(n => (n.name, n)).toMap
      val functionTable = program.functions.map(n => (n.name, n)).toMap
      val predicateTable = program.predicates.map(n => (n.name, n)).toMap
      val fieldTable = program.fields.map(n => (n.name, n)).toMap
      val domainTable = program.domains.map(n => (n.name, n)).toMap
      val domainFunctionTable = program.domains.flatMap(d => d.functions.map(f => (f.name, (f, d)))).toMap

      { (vertices: Set[Vertex]) =>

        val methods = {
          val ms = vertices.collect{ case v: Method => methodTable(v.methodName) }
          val stubs = vertices.collect{ case v: MethodSpec => val m = methodTable(v.methodName); m.copy(body = None)(m.pos, m.info, m.errT) }
          val filteredStubs = stubs.filterNot(stub => ms.exists(_.name == stub.name))
          (ms ++ filteredStubs).toSeq
        }
        val funcs = vertices.collect{ case v: Function => functionTable(v.functionName) }.toSeq
        val preds = {
          val psigs = vertices.collect{ case v: PredicateSig => val p = predicateTable(v.predicateName); p.copy(body = None)(p.pos, p.info, p.errT) }.toSeq
          val pbodies = vertices.collect{ case v: PredicateBody => predicateTable(v.predicateName) }.toSeq
          val filteredSigs = psigs.filterNot(sig => pbodies.exists(_.name == sig.name))
          pbodies ++ filteredSigs
        }
        val fields = vertices.collect{ case v: Field => fieldTable(v.fieldName) }.toSeq
        val domains = {
          val fs = vertices.collect{ case v: DomainFunction => domainFunctionTable(v.funcName) }.toSeq.groupMap(_._2)(_._1)
          val as = vertices.collect{ case v: DomainAxiom => (v.v, v.d)}.toSeq.groupMap(_._2)(_._1)
          val ds = vertices.collect{ case v: DomainType => domainTable(v.v.domainName) }.toSeq
          val totalDs = (ds ++ fs.keys ++ as.keys).distinct

          totalDs.map{ d =>
            d.copy(functions = fs.getOrElse(d, Seq.empty), axioms = as.getOrElse(d, Seq.empty))(d.pos, d.info, d.errT)
          }
        }

        program.copy(
          methods = methods,
          functions = funcs,
          predicates = preds,
          fields = fields,
          domains = domains
        )(program.pos, program.info, program.errT)
      }
    }
  }

  type Edge[T] = (T, T)
  object Edges {

    /** Returns all dependencies induced by a member. */
    def dependencies(member: vpr.Member): Seq[Edge[Vertex]] = {
      member match {
        case m: vpr.Method =>
          {
            val from = Vertex.Method(m.name)
            usages(m).map(from -> _)
          } ++ {
            val from = Vertex.MethodSpec(m.name)
            (m.pres ++ m.posts ++ m.formalArgs ++ m.formalReturns).flatMap(exp => usages(exp).map(from -> _))
          }

        case f: vpr.Function =>
          val from = Vertex.Function(f.name)
          usages(f).map(from -> _)

        case p: vpr.Predicate =>
          {
            val from = Vertex.PredicateSig(p.name)
            p.formalArgs.flatMap(exp => usages(exp).map(from -> _))
          } ++ {
            val from = Vertex.PredicateBody(p.name)
            usages(p).map(from -> _)
          } ++ { Seq(Vertex.PredicateBody(p.name) -> Vertex.PredicateSig(p.name)) }


        case f: vpr.Field =>
          val from = Vertex.Field(f.name)
          usages(f).map(from -> _)

        case d: vpr.Domain =>
          d.axioms.flatMap { ax =>
            val mid = Vertex.DomainAxiom(ax, d)
            val tos = usages(ax.exp)
            val froms = tos // this is just an approximation for now. This could be improved.
            val finalFroms = if (froms.nonEmpty) froms else Vector(Vertex.Always)
            finalFroms.map(_ -> mid) ++ tos.map(mid -> _)
          } ++ d.functions.flatMap { f =>
            val from = Vertex.DomainFunction(f.name)
            usages(f).map(from -> _)
          }

        case _ => Vector.empty
      }
    }

    /** Returns all entities referenced in the subtree of node `n`. */
    def usages(n: vpr.Node): Seq[Vertex] = {

      def deepType(t: vpr.Type): Seq[vpr.Type] = t +: (t match {
        case t: vpr.GenericType => t.typeArguments.flatMap(deepType)
        case _ => Seq.empty
      })

      n.deepCollect{
        case n: vpr.MethodCall      => Seq(Vertex.MethodSpec(n.methodName))
        case n: vpr.FuncApp         => Seq(Vertex.Function(n.funcname))
        case n: vpr.DomainFuncApp   => Seq(Vertex.DomainFunction(n.funcname))
        case n: vpr.PredicateAccess => Seq(Vertex.PredicateSig(n.predicateName))
        case n: vpr.Unfold          => Seq(Vertex.PredicateBody(n.acc.loc.predicateName))
        case n: vpr.Fold            => Seq(Vertex.PredicateBody(n.acc.loc.predicateName))
        case n: vpr.Unfolding       => Seq(Vertex.PredicateBody(n.acc.loc.predicateName))
        case n: vpr.FieldAccess     => Seq(Vertex.Field(n.field.name))
        case n: vpr.Type => deepType(n).collect{ case t: vpr.DomainType => Vertex.DomainType(t) }
      }.flatten
    }
  }
}
