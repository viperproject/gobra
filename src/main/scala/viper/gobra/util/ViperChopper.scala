// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util


import viper.silver.{ast => vpr}
import viper.gobra.util.ViperChopper.Vertex.Always

import scala.annotation.tailrec
import scala.collection.{immutable, mutable}

object ViperChopper {

  /** chops 'choppee' into independent Viper programs */
  def chop(choppee: vpr.Program)(
    isolate: Option[vpr.Method => Boolean] = None,
    bound: Option[Int] = Some(1),
    penalty: Penalty[Vertex] = Penalty.Default
  ): Vector[vpr.Program] = {

    val (n, isolatesIds, edges, idToVertex, inverse) = ViperGraph.toGraph(choppee, isolate)
    val programs = Cut.boundedCut(n, isolatesIds, edges, idToVertex)(bound, penalty)
    programs map (list => inverse(list.toSet))
  }

  object Cut {

    def boundedCut[T](N: Int, nodes: Vector[Int], edges: Array[mutable.SortedSet[Int]], idToVertex: Int => Vertex)(
      bound: Option[Int],
      penalty: Penalty[Vertex]
    ): Vector[List[Int]] = {
      require(bound.forall(_ > 0), s"Got $bound as the size of the cut, but expected positive number")

      val t1 = System.nanoTime()
      val smallestPrograms = smallestCut(N, nodes, edges)
      val t2 = System.nanoTime()
      val mergedPrograms = mergePrograms(smallestPrograms)(bound, penalty.contravariantLift(idToVertex))
      val t3 = System.nanoTime()
      val timeCutting = BigDecimal((t2 - t1) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      val timeMerging = BigDecimal((t3 - t2) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)

      println(s"Chopped verification condition into ${mergedPrograms.size} parts. Maximum number of parts is ${smallestPrograms.size}. (Time: ${timeCutting}s + ${timeMerging}s)")

      mergedPrograms
    }

    def smallestCut(N: Int, nodes: Vector[Int], edges: Array[mutable.SortedSet[Int]]): Vector[List[Int]] = {

      /**
        *
        */

      // Stores the color of nodes. A color of a node is only valid if the node was visited.
      val coloring = Array.ofDim[Int](N)

      // Stores whether a note was visited
      val visited  = Array.ofDim[Boolean](N)

      // Stores whether a color is not a root. Only valid if color was used.
      val colorIsNotRoot  = Array.ofDim[Boolean](N)

      def dfs1(start: Int, color: Int): (immutable.SortedSet[Int], List[Int]) = {
        val stack = mutable.Stack[Int](start)
        var members = immutable.SortedSet.empty[Int] // nodes of current color
        var neighbors = List.empty[Int] // other colors reached from current color
        val isAlreadyNeighbor = Array.ofDim[Boolean](color) // Assumes that `color` is max color used so far

        while (stack.nonEmpty) {
          val node = stack.pop()
          if (!visited(node)) {
            visited(node) = true
            members += node
            coloring(node) = color
            stack.pushAll(edges(node))
          } else {
            val otherColor = coloring(node)
            if (otherColor != color && !isAlreadyNeighbor(otherColor)) {
              isAlreadyNeighbor(otherColor) = true
              colorIsNotRoot(otherColor) = true
              neighbors ::= otherColor
            }
          }
        }
        (members, neighbors)
      }

      var numColors = 0
      val colorMembers = Array.ofDim[immutable.SortedSet[Int]](N)
      val colorEdges = Array.ofDim[List[Int]](N)
      for (node <- nodes if !visited(node)) {
        val (members, neighbors) = dfs1(node, numColors)
        colorMembers(numColors) = members
        colorEdges(numColors) = neighbors
        numColors += 1
      }

      def dfs2(startColor: Int): immutable.SortedSet[Int] = {
        val visitedColor  = Array.ofDim[Boolean](numColors)
        val stack = mutable.Stack[Int](startColor)
        var result = immutable.SortedSet.empty[Int]

        while (stack.nonEmpty) {
          val color = stack.pop()
          if (!visitedColor(color)) {
            visitedColor(color) = true
            result ++= colorMembers(color) // using SSC, number of merges might go down
            stack.pushAll(colorEdges(color))
          }
        }

        result
      }

      (for (color <- 1 until numColors if !colorIsNotRoot(color)) yield dfs2(color).toList).toVector
    }

    /** Merges programs  */
    def mergePrograms[T](programs: Vector[List[T]])(
      bound: Option[Int],
      penalty: Penalty[T]
    )(implicit order: Ordering[T]): Vector[List[T]] = {

      /**
        *
        */

      val start = programs.map(_.map(c => (c, penalty.price(c))))
      val entries = start.zipWithIndex.map{ case (p,idx) => (idx,p) }
      val sets = mutable.Map(entries:_*)
      var counter = entries.size // not used as key in map
      def isAlive(x: (Int, Int)): Boolean = sets.contains(x._1) && sets.contains(x._2)

      val init = for {
        (aIdx, a) <- entries
        (bIdx, b) <- entries
        if aIdx < bIdx
        (price, rep) = penaltyAndMerge(a,b)(penalty)
      } yield (price, (aIdx, bIdx), rep)

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
            val (price, newNewRep) = penaltyAndMerge(rep, newRep)(penalty)
            queue.enqueue((price, (idx, newIdx), newNewRep))
          }

          sets.put(newIdx, newRep)
        }
      }

      sets.values.map(_.map(_._1)).toVector
    }

    private def penaltyAndMerge[T](l: List[(T, Int)], r: List[(T, Int)])(penalty: Penalty[_])
                                  (implicit order: Ordering[T]): (Int, List[(T, Int)]) = {

      @tailrec
      def go(l: List[(T, Int)], r: List[(T, Int)], a: Int, b: Int, c: Int, acc: List[(T, Int)]): (Int, Int, Int, List[(T, Int)]) = {
        (l, r) match {
          case (xs, Nil) => (a + xs.map(_._2).sum, b, c, acc.reverse ++ xs)
          case (Nil, ys) => (a, b + ys.map(_._2).sum, c, acc.reverse ++ ys)
          case ((xId, xScore) :: xs, (yId, yScore) :: ys) =>
            val comp = order.compare(xId, yId)
            if (comp == 0)     go(xs, ys, a, b, c+xScore, (xId,xScore)::acc)
            else if (comp < 0) go(xs, r,  a+xScore, b, c, (xId,xScore)::acc)
            else               go(l,  ys, a, b+yScore, c, (yId,yScore)::acc)
        }
      }

      val (leftPrice, rightPrice, sharedPrice, merged) = go(l, r, 0, 0, 0, Nil)
      (penalty.mergePenalty(leftPrice, rightPrice, sharedPrice), merged)
    }
  }

  trait Penalty[T] {

    /**
      * Returns penalty of merging two programs X and Y.
      * @param lhsExclusivePrice Summed prices of components that are in X, but not in Y.
      * @param rhsExclusivePrice Summed prices of components that are in Y, but not in X.
      * @param sharedPrice Summed prices of components that are in the intersection of X and Y.
      * @return Penalty of merging X and Y. A merge with penalty <= 0 is always performed.
      */
    def mergePenalty(lhsExclusivePrice: Int, rhsExclusivePrice: Int, sharedPrice: Int): Int

    /** Returns price of a component. */
    def price(x: T): Int

    /** contravariant map */
    def contravariantLift[S](f: S => T): Penalty[S] = new Penalty.ExtPenalty(this, f)
  }

  object Penalty {

    private class ExtPenalty[T,R](p: Penalty[R], f: T => R) extends Penalty[T] {
      override def price(x: T): Int = p.price(f(x))
      override def mergePenalty(exclusive1: Int, exclusive2: Int, shared: Int): Int =
        p.mergePenalty(exclusive1, exclusive2, shared)
    }

    class DefaultImpl(sharedThreshold: Int) extends Penalty[Vertex] {

      override def price(xs: Vertex): Int = xs match {
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

      override def mergePenalty(lhsExclusivePrice: Int, rhsExclusivePrice: Int, sharedPrice: Int): Int =
        (lhsExclusivePrice + rhsExclusivePrice) * ((sharedThreshold + sharedPrice).toFloat / sharedThreshold).toInt
    }

    object Default extends DefaultImpl(100)

    object DefaultWithoutForcedMerge extends DefaultImpl(100) {
      override def mergePenalty(lhsExclusivePrice: Int, rhsExclusivePrice: Int, sharedPrice: Int): Int =
        Math.max(super.mergePenalty(lhsExclusivePrice, rhsExclusivePrice, sharedPrice), 1)
    }
  }

  object ViperGraph {
    /**
      * Transforms program into a graph.
      * @return
      *   _1 : Number of nodes
      *   _2 : Isolated nodes, i.e. nodes that must be included in on of the chopped programs.
      *   _3 :
      * */
    def toGraph(
                 program: vpr.Program,
                 isolate: Option[vpr.Method => Boolean] = None
               ): (Int, Vector[Int], Array[mutable.SortedSet[Int]], Int => Vertex, Set[Int] => vpr.Program) = {

      var vertexToId = Map.empty[Vertex, Int]
      var N = 0
      def id(v: Vertex): Int = {
        vertexToId.getOrElse(v, {
          val idx = N
          N += 1
          vertexToId += (v -> idx)
          idx
        })
      }

      val members = program.members.toVector
      val vertexEdges = members.flatMap(Edges.dependencies)
      val edges = vertexEdges.map{ case (l,r) => (id(l),id(r)) }
      val selector: vpr.Member => Boolean = isolate match {
        case Some(f) => { case m: vpr.Method => f(m); case _ => false }
        case None    => { case _: vpr.Method | _: vpr.Function | _: vpr.Predicate => true; case _ => false }
      }
      val isolatedNodes = id(Always) +: members.filter(selector).map(m => id(Vertex.toVertex(m)))

      val vertices = Array.ofDim[Vertex](N)
      for ((vertex, idx) <- vertexToId) { vertices(idx) = vertex }
      val idToVertex = vertices.apply _

      val fastEdges = Array.fill(N)(mutable.SortedSet.empty[Int])
      for ((l,r) <- edges) { fastEdges(l).add(r) }

      val setOfVerticesToProgram = Vertex.inverse(program)
      val setOfIdsToProgram = (set: Set[Int]) => setOfVerticesToProgram(set map idToVertex)

      (N, isolatedNodes, fastEdges, idToVertex, setOfIdsToProgram)
    }
  }

  /** Abstract the smallest parts of a Viper program and provides necessary information to compute merge penalties. */
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
    case object Always extends Vertex // if something always has to be included, then it is a dependency of Always

    def toVertex(m: vpr.Member): Vertex = {
      m match {
        case m: vpr.Method => Vertex.Method(m.name)
        case m: vpr.Function => Vertex.Function(m.name)
        case m: vpr.Predicate => Vertex.PredicateBody(m.name)
        case m: vpr.Field => Vertex.Field(m.name)
        case m: vpr.Domain => Vertex.DomainType(vpr.DomainType(domain = m, (m.typVars zip m.typVars).toMap))
        case _: vpr.ExtensionMember => ???
      }
    }

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


  ////////////////////// Currently unused algorithms that might be used in the future //////////////////////

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

  object FastSCC {

    case class Component(nodes: Vector[Int]) { val proxy: Int = nodes.head }
    implicit val orderingComponent: Ordering[Component] = Ordering.by(_.proxy)

    def components(N: Int, edges: Array[mutable.SortedSet[Int]]): Vector[Component] = {

      // Implements Tarjan's strongly connected components algorithm
      val stack = mutable.Stack[Int]()
      val visited  = Array.ofDim[Boolean](N)
      val lowLinks = Array.fill[Int](N)(-1)
      val onStack  = Array.ofDim[Boolean](N)
      val components = mutable.ArrayBuffer[Component]()

      // helper function which performs most of the work
      def strongConnect(v: Int): Unit = {
        // initialize all values needed for the current vertex
        visited(v) = true
        lowLinks(v) = v
        stack.push(v)
        onStack(v) = true
        // find all successors
        val succs = edges(v)
        for (s <- succs) {
          if (!visited(s)) {
            // successor has not been visited yet
            strongConnect(s)
            lowLinks.update(v, Math.min(lowLinks(v), lowLinks(s)))
          } else if (onStack(s)) {
            // s is already on the stack and therefore in the current SSC
            lowLinks.update(v, Math.min(lowLinks(v), if (visited(s)) s else -1))
          }
        }
        // if v is a root node, create new SCC from stack
        if (lowLinks(v) == (if (visited(v)) v else -2)) {
          val component = mutable.ArrayBuffer[Int]()
          var curr = v
          do {
            curr = stack.pop()
            onStack.update(curr, false)
            component += curr
          } while (curr != v)
          components += Component(component.toVector)
        }
      }

      // perform algorithm for all vertices
      for (v <- 0 until N if !visited(v)) { strongConnect(v) }
      components.toVector
    }

    def compute(N: Int, edges: Array[mutable.SortedSet[Int]]): (Vector[Component], Int => Component, Array[mutable.SortedSet[Component]]) = {
      val cs = components(N, edges)

      val idToComponent = Array.ofDim[Component](N)
      for (c <- cs; v <- c.nodes) { idToComponent(v) = c }
      val idToComponentF = idToComponent.apply _

      val csEdges = Array.fill(N)(mutable.SortedSet.empty[Component])
      for (l <- 0 until N; r <- edges(l)) {
        val cl = idToComponent(l)
        val cr = idToComponent(r)
        if (cl != null && cr != null && cl.proxy != cr.proxy) {
          csEdges(cl.proxy).add(cr)
        }
      }

      (cs, idToComponentF, csEdges)
    }

  }
}
