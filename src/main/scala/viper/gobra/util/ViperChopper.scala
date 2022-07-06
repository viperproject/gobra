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

  /**
    * chops 'choppee' into multiple Viper programs.
    * @param choppee Targeted program.
    * @param isolate Specifies which members of the program should be verified.
    *                If none, then all members that induce a proof obligation are verified.
    * @param bound Specifies upper bound on the number of returned programs.
    *              If none, then maximum number of programs is returned.
    * @param penalty Specifies penalty of merging programs.
    * @return Chopped programs.
    */
  def chop(choppee: vpr.Program)(
    isolate: Option[vpr.Member => Boolean] = None,
    bound: Option[Int] = Some(1),
    penalty: Penalty[Vertex] = Penalty.Default
  ): Vector[vpr.Program] = {

    val (n, isolatesIds, edges, idToVertex, inverse) = ViperGraph.toGraph(choppee, isolate)
    val programs = Cut.boundedCut(n, isolatesIds, edges, idToVertex)(bound, penalty)
    programs flatMap (list => inverse(list))
  }

  object Cut {

    /**
      * Returns a set of chopped programs where the number of programs is bounded by `bound`.
      * @param N Number of nodes.
      * @param nodes Nodes that must be included.
      * @param edges Edges of the dependency graph.
      * @param idToVertex Mapping from node ids to vertices used for price computations.
      * @param bound Specifies upper bound on the number of returned programs.
      *              If none, then maximum number of programs is returned.
      * @param penalty Specifies penalty of merging programs.
      * @return Set of programs.
      */
    def boundedCut[T](N: Int, nodes: Vector[Int], edges: Array[mutable.SortedSet[Int]], idToVertex: Int => Vertex)(
      bound: Option[Int],
      penalty: Penalty[Vertex]
    ): Vector[Set[Int]] = {
      require(bound.forall(_ > 0), s"Got $bound as the size of the cut, but expected positive number")

      /**
        * During intermediate steps, programs are represented as sorted lists of ints.
        * This representation makes merging programs linear time, which is done by [[penaltyAndMerge]].
        * Furthermore, by pairing a node with its contribution to the merge penalty,
        * the merge penalty can, together with the merging, be computed in linear time, too.
        */

      var timeSCC = BigDecimal(0)
      var timeCutting = BigDecimal(0)
      var timeMerging = BigDecimal(0)
      var maxNumberOfParts = 0
      var forallSmallNode: (Int => Boolean) => Boolean = null // for the safety check

      val result = {
        if (nodes.size <= 2) {
          val t1 = System.nanoTime()
          val smallestPrograms = smallestCutWithCycles(N, nodes, edges, identity[Int])
          val t2 = System.nanoTime()
          val mergedPrograms = mergePrograms(smallestPrograms)(bound, penalty.contravariantLift(idToVertex))
          val res = mergedPrograms.map(_.toSet)
          val t3 = System.nanoTime()
          timeCutting = BigDecimal((t2 - t1) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
          timeMerging = BigDecimal((t3 - t2) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
          maxNumberOfParts = smallestPrograms.size
          forallSmallNode = f => smallestPrograms.forall(_.forall(f))
          res
        } else {
          val t0 = System.nanoTime()
          val (_, idToSCC, sccEdges) = SCC.fastCompute(N, edges)
          val sccNodes = nodes.map(idToSCC) // may contain duplicates, but smallestCut can deal with that
          val t1 = System.nanoTime()
          // SCC.fastCompute guarantees that sccEdges has no cycles
          val smallestPrograms = smallestCutWithoutCycles(N, sccNodes, sccEdges, (x: SCC.Component[Int]) => x.proxy)
          val t2 = System.nanoTime()
          val mergedPrograms = mergePrograms(smallestPrograms)(bound, penalty.contravariantSumLift(_.nodes.map(idToVertex)))
          val res = mergedPrograms.map(_.toSet.flatMap((c: SCC.Component[Int]) => c.nodes))
          val t3 = System.nanoTime()
          timeSCC     = BigDecimal((t1 - t0) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
          timeCutting = BigDecimal((t2 - t1) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
          timeMerging = BigDecimal((t3 - t2) / 1e9d).setScale(2, BigDecimal.RoundingMode.HALF_UP)
          maxNumberOfParts = smallestPrograms.size
          forallSmallNode = f => smallestPrograms.forall(_.forall(_.nodes.forall(f)))
          res
        }
      }

      println(s"Chopped verification condition into ${result.size} parts. Maximum number of parts is $maxNumberOfParts. (Time: ${timeSCC}s + ${timeCutting}s + ${timeMerging}s)")

      // Safety check validating the result partially. Can be removed in a year if no error has been found.
      {
        val containedInResult = Array.ofDim[Boolean](N)
        for (program <- result; node <- program) { containedInResult(node) = true }
        // check all nodes of the smallest programs are present (no node should be lost)
        val smallestProgramContainedInResult = forallSmallNode(containedInResult(_))
        assert(smallestProgramContainedInResult, "Chopper Error: Lost nodes during merging step.")

        val containedInSmallest = Array.ofDim[Boolean](N)
        forallSmallNode{node => containedInSmallest(node) = true; true }
        // checks all selected notes are present in the result
        val selectedNodesContainedInSmallest = nodes.forall(containedInSmallest(_))
        assert(selectedNodesContainedInSmallest, "Chopper Error: Not all selected nodes present in solution.")
      }

      result
    }

    /**
      * Returns the set of smallest possible programs.
      * @param N Number of nodes.
      * @param nodes Nodes that must be included. The vector may be unsorted and may contain duplicates.
      * @param edges Edges of the dependency graph. The graph must have no cycles.
      * @param id Mapping from nodes to node ids. The result is used as the index for `edges`.
      * @return Set of smallest possible programs. A program is represented as a *sorted* list of node ids.
      */
    private def smallestCutWithoutCycles[T](N: Int, nodes: Vector[T], edges: Array[mutable.SortedSet[T]], id: T => Int): Vector[List[T]] = {

      /**
        * Computes which of the nodes in `nodes` are dominating, i.e. not reached by other nodes in `nodes`,
        * and then returns for each dominating node, the set of reachable nodes in a separate sorted list.
        */

      // Stores state of a node
      sealed trait State
      case object NotVisited extends State
      case object NotFinalized extends State
      case class  Finalized(startId: Int) extends State
      val state = Array.fill[State](N)(NotVisited)

      // Stores whether a node is not a root.
      val notRoot = Array.ofDim[Boolean](N)

      // Stores all dependencies of a node (including itself).
      // Serves as memoization table.
      val reachableNodes = Array.ofDim[immutable.SortedSet[T]](N)

      def dfs(start: T): Unit = {
        val stack = mutable.Stack[T](start)
        val startId = id(start)

        while (stack.nonEmpty) {
          val node = stack.pop()
          val nodeId = id(node)

          state(nodeId) match {
            case NotVisited =>
              state(nodeId) = NotFinalized
              // visit this node again after visiting the children
              stack.push(node)
              stack.pushAll(edges(nodeId))
            case NotFinalized =>
              state(nodeId) = Finalized(startId)
              // children were visited, so now the result can be computed
              reachableNodes(nodeId) =
                edges(nodeId).foldLeft(immutable.SortedSet[T](node)(Ordering.by(id))) {
                  case (result, neighbor) => result ++ reachableNodes(id(neighbor))
                }
            case Finalized(`startId`) =>
              // node was visited in another call to dfs with the same argument ('nodes' may contain duplicates).
            case _: Finalized =>
              // node was visited in another call to dfs with a different argument.
              notRoot(nodeId) = true
          }
        }
      }

      for (node <- nodes) dfs(node)
      for (node <- nodes; nodeId = id(node) if !notRoot(nodeId)) yield reachableNodes(nodeId).toList
    }

    /**
      * Returns the set of smallest possible programs.
      * @param N Number of nodes.
      * @param nodes Nodes that must be included. The vector may be unsorted and may contain duplicates.
      * @param edges Edges of the dependency graph. The graph may have cycles.
      * @param id Mapping from nodes to node ids. The result is used as the index for `edges`.
      * @return Set of smallest possible programs. A program is represented as a *sorted* list of node ids.
      */
    private def smallestCutWithCycles[T](N: Int, nodes: Vector[T], edges: Array[mutable.SortedSet[T]], id: T => Int): Vector[List[T]] = {

      /**
        * Computes which of the nodes in `nodes` are dominating, i.e. not reached by other nodes in `nodes`,
        * and then returns for each dominating node, the set of reachable nodes in a separate sorted list.
        */

      // Stores whether a node was visited by any call to dfs.
      val globalVisited = Array.ofDim[Boolean](N)

      // Stores whether a node is not a root.
      val notRoot = Array.ofDim[Boolean](N)

      // Stores all dependencies of a node (including itself).
      val reachableNodes = Array.ofDim[mutable.SortedSet[T]](N)

      def dfs(start: T): Unit = {
        val stack = mutable.Stack[T](start)
        val result = mutable.SortedSet[T]()(Ordering.by(id))

        // Stores whether a node was visited by this call to dfs.
        val localVisited = Array.ofDim[Boolean](N)

        while (stack.nonEmpty) {
          val node = stack.pop()
          val nodeId = id(node)

          if (!localVisited(nodeId)) {
            localVisited(nodeId) = true

            if (globalVisited(nodeId)) { notRoot(nodeId) = true }
            globalVisited(nodeId) = true

            result.add(node)
            stack.pushAll(edges(nodeId))
          }
        }
        reachableNodes(id(start)) = result
      }

      for (node <- nodes) dfs(node)
      for (node <- nodes; nodeId = id(node) if !notRoot(nodeId)) yield reachableNodes(nodeId).toList
    }

    /**
      * Merges set of programs into smaller set of programs bounded by `bound`.
      * @param programs Vector of programs. A program is represented as a *sorted* list of node ids.
      * @param bound Specifies upper bound on the number of returned programs.
      *              If none, then maximum number of programs is returned.
      * @param penalty Specifies penalty of merging programs.
      * @return
      */
    private def mergePrograms[T](programs: Vector[List[T]])(
      bound: Option[Int],
      penalty: Penalty[T]
    )(implicit order: Ordering[T]): Vector[List[T]] = {

      /**
        * A program is represented as a *sorted* list of node ids.
        * `sets` contains the current set of programs, where we use the keys to index the programs.
        * The code computes all combinations of merges together with their penalty and stores them in a priority queue.
        * The priority queue uses the inverted penalties as the ranking.
        *
        * Until the desired number of programs is reached, an element from the queue is popped and then:
        * 1) the code checks whether the merge is still valid (i.e. none of the operands has already been merged).
        * 2) the merge is computed.
        * 3) both operands of the merge are marked as invalid.
        * 4) all combinations of the merge result and all other programs are computed and added to the queue.
        */

      val start = programs.map(_.map(c => (c, penalty.price(c))))
      val entries = start.zipWithIndex.map{ case (p,idx) => (idx,p) }
      val sets = mutable.Map(entries:_*) // current set of programs. Keys are used as indices.
      var counter = entries.size // not used as key in map
      def isAlive(x: (Int, Int)): Boolean = sets.contains(x._1) && sets.contains(x._2) // checks if merge is valid based on indices

      // initial computation of all combinations
      val init = for {
        (aIdx, a) <- entries
        (bIdx, b) <- entries
        if aIdx < bIdx
        (price, rep) = penaltyAndMerge(a,b)(penalty)
      } yield (price, (aIdx, bIdx), rep) // penalty, both indices, and merge result (in this order)

      val queue = mutable.PriorityQueue(init:_*)(Ordering.by(- _._1))

      while (queue.headOption.exists(_._1 <= 0) || bound.exists(sets.size > _)) {
        var x = queue.dequeue()
        while (!isAlive(x._2)) { x = queue.dequeue() } // dequeue until valid merge

        // if head had penalty 0 before, this might be outdated now.
        // Therefore, loop condition is checked again.
        if (x._1 <= 0 || bound.exists(sets.size > _)) {
          val (_, (lIdx, rIdx), newRep) = x
          sets.remove(lIdx); sets.remove(rIdx)
          val newIdx = counter
          counter += 1

          // compute new combinations of merge result with current sets of programs.
          for ((idx, rep) <- sets) {
            val (price, newNewRep) = penaltyAndMerge(rep, newRep)(penalty)
            queue.enqueue((price, (idx, newIdx), newNewRep))
          }

          sets.put(newIdx, newRep)
        }
      }

      sets.values.map(_.map(_._1)).toVector
    }

    /** Merges two programs and computes their merge penalty. A program is represented as a *sorted* list of T. */
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
    def contravariantLift[S](f: S => T): Penalty[S] = new Penalty.SumPenalty(this, x => Some(f(x)))
    def contravariantSumLift[S](f: S => Iterable[T]): Penalty[S] = new Penalty.SumPenalty(this, f)
  }

  object Penalty {

    private class SumPenalty[T,R](p: Penalty[R], f: T => Iterable[R]) extends Penalty[T] {
      override def price(x: T): Int = f(x).map(p.price).sum
      override def mergePenalty(exclusive1: Int, exclusive2: Int, shared: Int): Int =
        p.mergePenalty(exclusive1, exclusive2, shared)
    }

    case class PenaltyConfig(
                              method: Int,
                              methodSpec: Int,
                              function: Int,
                              predicate: Int,
                              predicateSig: Int,
                              field: Int,
                              domainType: Int,
                              domainFunction: Int,
                              domainAxiom: Int,
                              sharedThreshold: Int
                            )

    val defaultPenaltyConfig: PenaltyConfig = PenaltyConfig(
      method = 0, methodSpec = 0, function = 20, predicate = 10, predicateSig = 2, field = 1,
      domainType = 1, domainFunction = 1, domainAxiom = 5,
      sharedThreshold = 50
    )

    class DefaultImpl(conf: PenaltyConfig) extends Penalty[Vertex] {

      override def price(xs: Vertex): Int = xs match {
        case _: Vertex.Method         => conf.method
        case _: Vertex.MethodSpec     => conf.methodSpec
        case _: Vertex.Function       => conf.function
        case _: Vertex.PredicateBody  => conf.predicate
        case _: Vertex.PredicateSig   => conf.predicateSig
        case _: Vertex.Field          => conf.field
        case _: Vertex.DomainType     => conf.domainType
        case _: Vertex.DomainFunction => conf.domainFunction
        case _: Vertex.DomainAxiom    => conf.domainAxiom
        case Vertex.Always => 0
      }

      override def mergePenalty(lhsExclusivePrice: Int, rhsExclusivePrice: Int, sharedPrice: Int): Int = {
        (lhsExclusivePrice + rhsExclusivePrice) * ((conf.sharedThreshold + sharedPrice).toFloat / conf.sharedThreshold).toInt
      }
    }

    object Default extends DefaultImpl(defaultPenaltyConfig)

    object DefaultWithoutForcedMerge extends DefaultImpl(defaultPenaltyConfig) {
      override def mergePenalty(lhsExclusivePrice: Int, rhsExclusivePrice: Int, sharedPrice: Int): Int =
        Math.max(super.mergePenalty(lhsExclusivePrice, rhsExclusivePrice, sharedPrice), 1)
    }
  }

  object ViperGraph {

    /**
      * Transforms program into a graph with int nodes, which enable faster algorithms.
      * @return
      *   _1 : Number of nodes.
      *   _2 : Isolated nodes, i.e. nodes that must be included in one of the chopped programs.
      *        Vector is not sorted and may contain duplicates.
      *   _3 : Edges of the dependency graph.
      *   _4 : Map from node id's to their vertex representation.
      *   _5 : Function that takes a set of nodes and returns the corresponding Viper program.
      * */
    def toGraph(
                 program: vpr.Program,
                 isolate: Option[vpr.Member => Boolean] = None
               ): (Int, Vector[Int], Array[mutable.SortedSet[Int]], Int => Vertex, Set[Int] => Option[vpr.Program]) = {

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
      val selector: vpr.Member => Boolean = isolate.getOrElse{
        // per default, isolated nodes are everything with a proof obligation, i.e. methods, functions, and predicates
        // We could filter further by checking that the member was present in the input (i.e. not generated by Gobra).
        // However, this information is not stored reliably at this point in time.
        case _: vpr.Method | _: vpr.Function | _: vpr.Predicate => true; case _ => false
      }
      // The isotated nodes are always and all selected nodes
      val alwaysId = id(Always)
      val isolatedNodes = alwaysId +: members.filter(selector).map(m => id(Vertex.toDefVertex(m)))

      val vertices = Array.ofDim[Vertex](N)
      for ((vertex, idx) <- vertexToId) { vertices(idx) = vertex }
      val idToVertex = vertices.apply _

      val fastEdges = Array.fill(N)(mutable.SortedSet.empty[Int])
      for ((l,r) <- edges) { fastEdges(l).add(r) }

      val setOfVerticesToProgram = Vertex.inverse(program)
      val setOfIdsToProgram = (set: Set[Int]) =>
        if (set == Set(alwaysId)) None else Some(setOfVerticesToProgram(set map idToVertex))

      (N, isolatedNodes, fastEdges, idToVertex, setOfIdsToProgram)
    }
  }

  /** Abstract the smallest parts of a Viper program and provides necessary information to compute merge penalties. */
  sealed trait Vertex
  object Vertex {
    case class Method private[Vertex] (methodName: String) extends Vertex
    case class MethodSpec(methodName: String) extends Vertex
    case class Function(functionName: String) extends Vertex
    case class PredicateSig(predicateName: String) extends Vertex
    case class PredicateBody private[Vertex] (predicateName: String) extends Vertex
    case class Field(fieldName: String) extends Vertex
    case class DomainFunction(funcName: String) extends Vertex
    case class DomainAxiom(v: vpr.DomainAxiom, d: vpr.Domain) extends Vertex
    case class DomainType(v: vpr.DomainType) extends Vertex
    case object Always extends Vertex // if something always has to be included, then it is a dependency of Always

    // the creation of the following vertices has special cases and should not happen outside of the Vertex object
    object Method { private[Vertex] def apply(methodName: String): Method = new Method(methodName) }
    object PredicateBody { private[Vertex] def apply(predicateName: String): PredicateBody = new PredicateBody(predicateName) }
    /** This function is only allowed to be called in the following cases:
      * 1) applying [[toDefVertex]] to the predicate referenced by `predicateName` returns a [[Vertex.PredicateBody]] instance.
      * 2) The result is used as the target of a dependency.
      * */
    def predicateBody_check_that_call_is_ok(predicateName: String): PredicateBody = Vertex.PredicateBody(predicateName)

    def toDefVertex(m: vpr.Member): Vertex = {

      m match {
        case m: vpr.Method => m.body match {
          case Some(_) => Vertex.Method(m.name)
          case None    => Vertex.MethodSpec(m.name)
        }
        case m: vpr.Predicate => m.body match {
          case Some(_) => Vertex.PredicateBody(m.name)
          case None    => Vertex.PredicateSig(m.name)
        }
        case m: vpr.Function => Vertex.Function(m.name)
        case m: vpr.Field => Vertex.Field(m.name)
        case m: vpr.Domain => Vertex.DomainType(vpr.DomainType(domain = m, (m.typVars zip m.typVars).toMap))
        case _: vpr.ExtensionMember => ???
      }
    }

    def toUseVertex(m: vpr.Member): Vertex = {
      m match {
        case m: vpr.Method => Vertex.MethodSpec(m.name)
        case m: vpr.Function => Vertex.Function(m.name)
        case m: vpr.Predicate => Vertex.PredicateSig(m.name)
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

    /**
      * Returns all dependencies induced by a member.
      * The result is an unsorted sequence of edges.
      * The edges are sorted at a later point, after the translation to int nodes (where it is cheaper).
      * */
    def dependencies(member: vpr.Member): Seq[Edge[Vertex]] = {
      val defVertex = Vertex.toDefVertex(member)
      val useVertex = Vertex.toUseVertex(member)

      member match {
        case m: vpr.Method =>
          dependenciesToChildren(member, defVertex) ++
            (m.pres ++ m.posts ++ m.formalArgs ++ m.formalReturns).flatMap(dependenciesToChildren(_, useVertex))

        case p: vpr.Predicate =>
          dependenciesToChildren(member, defVertex) ++
            p.formalArgs.flatMap(dependenciesToChildren(_, useVertex)) ++
            Seq(defVertex -> useVertex)

        case _: vpr.Function | _: vpr.Field => dependenciesToChildren(member, defVertex)

        case d: vpr.Domain =>
          d.axioms.flatMap { ax =>
            /**
              * For Axioms, we do not make the distinction between vertices that depend on the axiom
              * and vertices that the axiom depends on. Instead, all vertices that are in a relation with the axiom
              * are considered as dependencies in both directions (from and to the axiom).
              * If no other related vertices can be identified, then the axiom is always included,
              * as a conservative choice. We use that dependencies of `Always` are always included.
              */
            val mid = Vertex.DomainAxiom(ax, d)
            val tos = usages(ax.exp) // `tos` can be used as source because axioms do not contain un-/foldings.
            val froms = tos
            val finalFroms = if (froms.nonEmpty) froms else Vector(Vertex.Always)
            finalFroms.map(_ -> mid) ++ tos.map(mid -> _)
          } ++ d.functions.flatMap { f => dependenciesToChildren(f, Vertex.DomainFunction(f.name)) }

        case _ => Vector.empty
      }
    }

    /** Returns edges from `nodeVertex` to all children of `node` that are usages. */
    def dependenciesToChildren(node: vpr.Node, nodeVertex: Vertex): Seq[Edge[Vertex]] = {
      usages(node) map (nodeVertex -> _)
    }

    /**
      * Returns all entities referenced in the subtree of node `n`.
      * May only be used as the target of a dependency.
      * The result is an unsorted sequence of vertices.
      * The vertices are never sorted, and duplicates are fine.
      * Note that they are sorted indirectly when the edges are sorted.
      * */
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
        // The call is fine because the result is used as the target of a dependency.
        case n: vpr.Unfold          => Seq(Vertex.predicateBody_check_that_call_is_ok(n.acc.loc.predicateName))
        case n: vpr.Fold            => Seq(Vertex.predicateBody_check_that_call_is_ok(n.acc.loc.predicateName))
        case n: vpr.Unfolding       => Seq(Vertex.predicateBody_check_that_call_is_ok(n.acc.loc.predicateName))
        case n: vpr.FieldAccess     => Seq(Vertex.Field(n.field.name))
        case n: vpr.Type => deepType(n).collect{ case t: vpr.DomainType => Vertex.DomainType(t) }
      }.flatten
    }
  }

  object SCC {

    case class Component[T](nodes: Iterable[T]) { val proxy: T = nodes.head }
    implicit val orderingIntComponent: Ordering[Component[Int]] = Ordering.by(_.proxy)

    /**
      * Computes the strongly connected components of a graph using Tarjan's algorithm.
      * @param N Number of vertices in the graph.
      * @param edges Edges of the graph.
      * @return Vector containing the strongly connected components of the graph.
      * */
    def fastComponents(N: Int, edges: Array[mutable.SortedSet[Int]]): Vector[Component[Int]] = {

      var index = 0
      val stack = mutable.Stack.empty[Int]
      val visited  = Array.ofDim[Boolean](N)
      val indices  = Array.ofDim[Int](N)
      val lowLinks = Array.ofDim[Int](N)
      val onStack  = Array.ofDim[Boolean](N)
      var components = List.empty[Component[Int]]

      def strongConnect(currentNode: Int): Unit = {

        // set values for the current node
        visited(currentNode) = true
        indices(currentNode) = index
        lowLinks(currentNode) = index
        index += 1
        stack.push(currentNode)
        onStack(currentNode) = true

        for (successor <- edges(currentNode)) {
          if (!visited(successor)) {
            // if a successor has not been visited yet, compute its lowLink (recursively)
            strongConnect(successor)
            lowLinks(currentNode) = Math.min(lowLinks(currentNode), lowLinks(successor))
          } else if (onStack(successor)) {
            // if successor is already on the stack, it is part of the current component
            lowLinks(currentNode) = Math.min(lowLinks(currentNode), indices(successor))
          }
        }
        // if v is a root node, create new component from stack
        if (lowLinks(currentNode) == indices(currentNode)) {
          var component = List.empty[Int]
          var curr = currentNode
          do {
            curr = stack.pop()
            onStack(curr) = false
            component ::= curr
          } while (curr != currentNode)
          components ::= Component(component)
        }
      }

      // perform algorithm on all vertices
      for (v <- 0 until N if !visited(v)) { strongConnect(v) }
      components.toVector
    }

    /**
      * Computes the strongly connected components of a graph using Tarjan's algorithm.
      * @param N Number of vertices in the graph.
      * @param edges Edges of the graph.
      * @return
      *    _1 : Vector containing the strongly connected components of the graph.
      *    _2 : Mapping from node ids to the component that the node is contained in.
      *    _3 : Edges between the components in the graph. This induced graph on components is acyclic.
      * */
    def fastCompute(N: Int, edges: Array[mutable.SortedSet[Int]]): (Vector[Component[Int]], Int => Component[Int], Array[mutable.SortedSet[Component[Int]]]) = {
      val cs = fastComponents(N, edges)

      val idToComponent = Array.ofDim[Component[Int]](N)
      for (c <- cs; v <- c.nodes) { idToComponent(v) = c }
      val idToComponentF = idToComponent.apply _

      val csEdges = Array.fill(N)(mutable.SortedSet.empty[Component[Int]])
      for (l <- 0 until N; r <- edges(l)) {
        val cl = idToComponent(l)
        val cr = idToComponent(r)
        if (cl != null && cr != null && cl.proxy != cr.proxy) {
          csEdges(cl.proxy).add(cr)
        }
      }

      //// safety check, usually commented out
      // val onStack = Array.ofDim[Boolean](N)
      // val checkedCycles = Array.ofDim[Boolean](N)
      // def noCycle(x: Int): Unit = {
      //   assert(!onStack(x), "found cycle, but expected none.")
      //   if (!checkedCycles(x)) {
      //     checkedCycles(x) = true; onStack(x) = true
      //     for (y <- csEdges(x)) { noCycle(y.proxy) }
      //     onStack(x) = false
      //   }
      // }
      // for (idx <- 0 until N) noCycle(idToComponent(idx).proxy)

      (cs, idToComponentF, csEdges)
    }

    /**
      * Computes the strongly connected components of a graph using Tarjan's algorithm.
      * @param nodes Nodes of the graph.
      * @param edges Edges of the graph.
      * @return Vector containing the strongly connected components of the graph.
      * */
    def components[T](nodes: Seq[T], edges: Seq[Edge[T]]): Vector[Component[T]] = {
      val (n, idEdges, _, rev) = toFastGraph(nodes, edges)
      val idResult = fastComponents(n, idEdges)
      idResult.map(c => Component(c.nodes map (rev(_))))
    }

    /**
      * Decides whether a graph is acyclic using Tarjan's algorithm.
      * @param nodes Nodes of the graph.
      * @param edges Edges of the graph.
      * @return Bool indicating whether graph is acyclic.
      * */
    def isAcylic[T](nodes: Seq[T], edges: Seq[Edge[T]]): Boolean = {
      components(nodes, edges).forall(_.nodes.toVector.length == 1)
    }

    /**
      * Computes the strongly connected components of a graph using Tarjan's algorithm.
      * @param nodes Nodes of the graph.
      * @param edges Edges of the graph.
      * @return
      *    _1 : Vector containing the strongly connected components of the graph.
      *    _2 : Mapping from nodes to the component that the node is contained in.
      *    _3 : Edges between the components in the graph. This induced graph on components is acyclic.
      * */
    def compute[T](nodes: Seq[T], edges: Seq[Edge[T]]): (Vector[Component[T]], Map[T, Component[T]], Seq[Edge[Component[T]]]) = {
      val cs = components(nodes, edges)
      val inv = cs.flatMap(c => c.nodes.map(_ -> c)).toMap
      val cgraph = edges.map { case (l, r) => (inv(l), inv(r)) }.filter { case (l, r) => l != r }.distinct
      (cs, inv, cgraph)
    }

    /**
      * Translates a graph to a graph on node ids.
      * @param nodes Nodes of the graph.
      * @param edges Edges of the graph.
      * @return
      *    _1 : Number of nodes in the result graph.
      *    _2 : Edges of the result graph.
      *    _3 : Mapping from nodes to node ids.
      *    _4 : Mapping from node ids to nodes.
      * */
    private def toFastGraph[T](nodes: Seq[T], edges: Seq[Edge[T]]): (Int, Array[mutable.SortedSet[Int]], T => Int, Int => T) = {
      var counter = 0
      val indices = mutable.HashMap[T, Int]()
      def id(x: T): Int = indices.getOrElse(x, {
        val index = counter
        indices.put(x, index)
        counter += 1
        index
      })

      nodes.foreach(id)
      val idEdges = edges.map{ case (l,r) => (id(l), id(r)) }
      var rev = Map.empty[Int, T]
      for ((t,idx) <- indices) rev += (idx -> t)
      val fastIdEdges = Array.fill(counter)(mutable.SortedSet.empty[Int])
      for ((l,r) <- idEdges) { fastIdEdges(l).add(r) }

      (counter, fastIdEdges, id, rev(_))
    }

  }
}
