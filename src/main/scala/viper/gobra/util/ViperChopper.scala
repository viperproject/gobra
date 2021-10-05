// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util
import viper.gobra.util.ViperChopper.Cut.MergePenalty
import viper.silver.{ast => vpr}
import viper.gobra.util.ViperChopper.Vertex.Always

import scala.collection.mutable

object ViperChopper {
  /** chops 'choppee' into independent Viper programs */
  def chop(choppee: vpr.Program)(
    isolate: Option[vpr.Method => Boolean] = None,
    bound: Option[Int] = Some(5),
    mergePenalty: MergePenalty[SCC.Component[Vertex]] = Cut.Penalty.Default
  ): Vector[vpr.Program] = {

    val isolatedVertices = isolate.map{ f =>
      choppee.members.collect{ case x: vpr.Method if f(x) => Vertex.Method(x.name): Vertex }
    }

    val edges = choppee.members.flatMap(Edges.dependencies).distinct

    // TODO: move into separate method
    val requiredVertices = isolatedVertices.getOrElse {
      choppee.collect {
        case m: vpr.Method => Vertex.Method(m.name)
        case f: vpr.Function => Vertex.Function(f.name)
      }.toSeq
    }
    val vertices = (requiredVertices ++ edges.flatten{case (l, r) => List(l, r)}).distinct

    val (components, _, componentDAG) = SCC.compute(vertices, edges)
    val componentIsolate = isolatedVertices.map{ s => (x: SCC.Component[Vertex]) => x.nodes.exists(s.contains) }
    val subtrees = Cut.boundedCut(components, componentDAG)(componentIsolate, bound, mergePenalty)

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

    def smallestCut[T](nodes: Vector[T], forest: Seq[Edge[T]]): Vector[Set[T]] = {
      val roots = {
        val negatives = forest.map(_._2).toSet ++ Set(Vertex.Always)
        nodes.filterNot(negatives.contains)
      }

      def DFS (current: T): Vector[T] =
        current +: forest.toVector.collect{ case (`current`, succ) => DFS(succ) }.flatten

      roots map (x => DFS(x).toSet)
    }

    def boundedCut[T](nodes: Vector[T], forest: Seq[Edge[T]])(
      isolate: Option[T => Boolean],
      bound: Option[Int],
      mergePenalty: MergePenalty[T]
    ): Vector[Set[T]] = {
      require(bound.forall(_ > 0), s"Got $bound as the size of the cut, but expected positive number")

      val start = smallestCut(nodes, forest)
      println("start: " + start.size)
      val filtered = isolate match {
        case None => start
        case Some(f) => start.filter(_.exists(f))
      }
      val x = mutable.Set(filtered:_*)

      // remove always merge
      var performedUpdate = false
      do {
        performedUpdate = false

        val combinations = for {
          (a, aIdx) <- x.zipWithIndex
          (b, bIdx) <- x.zipWithIndex
          if aIdx != bIdx
        } yield (a,b)

        if (combinations.nonEmpty) { // there is more than one program in x
          val (a,b) = combinations.minBy(identity)(mergePenalty)

          if (mergePenalty.alwaysMerge(a,b) || !bound.forall(x.size <= _)) {
            x.remove(a)
            x.remove(b)
            x.add(a ++ b)
            performedUpdate = true
          }
        }

      } while(performedUpdate)

      println("final: " + x.size)

      x.toVector
    }

    sealed trait MergePenalty[T] extends Ordering[(Set[T],Set[T])]{
      /* Must be commutative, i.e. alwaysMerge(l,r) == alwaysMerge(r,l) */
      def alwaysMerge(l: Set[T], r: Set[T]): Boolean
    }

    sealed trait NaturalMergePenalty[T] extends MergePenalty[T] {
      def penalty(l: Set[T], r: Set[T]): Int

      override def alwaysMerge(l: Set[T], r: Set[T]): Boolean = penalty(l, r) <= 0
      override def compare(x: (Set[T], Set[T]), y: (Set[T], Set[T])): Int = penalty(x._1, x._2) compare penalty(y._1, y._2)
    }

    object Penalty {
      object Default extends NaturalMergePenalty[SCC.Component[Vertex]] {
        override def penalty(l: Set[SCC.Component[Vertex]], r: Set[SCC.Component[Vertex]]): Int = {

          def price(xs: Set[Vertex]): Int = xs.toVector.map{
            case _: Vertex.Method | _: Vertex.MethodSpec => 0
            case _: Vertex.Field => 1
            case _: Vertex.PredicateSig => 2
            case _: Vertex.PredicateBody => 3
            case _: Vertex.Function => 3
            case _: Vertex.DomainFunction => 1
            case _: Vertex.DomainType => 1
            case _: Vertex.DomainAxiom => 4
            case Vertex.Always => 0
          }.sum

          val lDiff = l.diff(r).flatMap(_.nodes)
          val rDiff = r.diff(r).flatMap(_.nodes)
          val inter = l.intersect(r).flatMap(_.nodes)

          (price(lDiff) + price(rDiff)) * ((20 + price(inter)).toFloat / 20).toInt
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

    /** Returns the subprogram induces by the set of vertices. */
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
          f.typ match {
            case t: vpr.DomainType =>
              Vector(Vertex.Field(f.name) -> Vertex.DomainType(t))
            case _ => Vector.empty
          }

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

      def unit(n: vpr.Node): Seq[Vertex] = {
        n match {
          case n: vpr.Exp if n.typ.isInstanceOf[vpr.DomainType] =>
            Vector(Vertex.DomainType(n.typ.asInstanceOf[vpr.DomainType]))

          case _ => Vector.empty
        }
      }

      n.deepCollect{
        case n: vpr.MethodCall => Vertex.MethodSpec(n.methodName) +: unit(n)
        case n: vpr.FuncApp => Vertex.Function(n.funcname) +: unit(n)
        case n: vpr.DomainFuncApp => Vertex.DomainFunction(n.funcname) +: unit(n)
        case n: vpr.PredicateAccess => Vertex.PredicateSig(n.predicateName) +: unit(n)
        case n: vpr.Unfold => Vertex.PredicateBody(n.acc.loc.predicateName) +: unit(n)
        case n: vpr.Fold => Vertex.PredicateBody(n.acc.loc.predicateName) +: unit(n)
        case n: vpr.Unfolding => Vertex.PredicateBody(n.acc.loc.predicateName) +: unit(n)
        case n: vpr.FieldAccess => Vertex.Field(n.field.name) +: unit(n)
        case n: vpr.Exp => unit(n)
        case n: vpr.DomainType =>
          Vertex.DomainType(n) +: n.partialTypVarsMap.collect{
            case (_, t: vpr.DomainType) => Vertex.DomainType(t)
          }.toSeq
      }.flatten
    }
  }
}
