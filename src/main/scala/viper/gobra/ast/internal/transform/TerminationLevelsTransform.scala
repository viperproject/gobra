// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.{internal => in}
import viper.gobra.ast.internal.TerminationAtom
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.ImportedTerminationMeasureAnnotation
import viper.gobra.theory.Addressability

import scala.annotation.tailrec

/**
  * Prepares termination checking with termination levels, which makes termination checking modular.
  *
  * Every terminating member gets a level, i.e., a finite multiset of termination atoms, which becomes the first
  * component of its termination measures. The remaining components are the local measures written by the user, which
  * only matter for calls between members of the same level. The encoding then checks at every call that the
  * measure of the callee (at the call) is smaller than the measure of the caller (in its pre-state). Interface methods
  * and closure specs are handled like any other member: their level is the level of the receiver (resp. closure),
  * i.e., of the object whose implementation is invoked. Thus, no call graph of the whole program is needed.
  *
  * The levels of members are as follows (`dyn` is the maximum level of the arguments whose type is an interface or
  * a function type, or the level given by the user as the first component of a termination measure):
  *  - function `f`:                       `{f} + dyn`
  *  - method `T.M` of a non-interface type: `{T.M} + T.TerminationLevel(recv) + dyn` (the second summand is only
  *                                          present if `T` declares a method `TerminationLevel`)
  *  - interface method:                   `levelOf(recv) + dyn`
  *  - function literal `l`:               `{l} + dyn`
  *  - `TerminationLevel` methods:         the empty level (they may only call other `TerminationLevel` methods)
  *
  * The atoms of the package under verification are ordered by the strongly connected components of its dependency
  * graph, where callees are below callers, a type is above its methods, and a member is above the types that it
  * converts to interfaces and the function literals that it creates. The atoms of imported packages are below the
  * atoms of their importers, but the order among the atoms of an imported package is not exposed. The order does not
  * depend on the textual order of declarations.
  *
  * Finally, the transformation marks the measures of members of imported packages (whose termination was checked
  * when their package was verified) and hides the local measures of imported members without bodies, as these
  * measures only matter for calls within their package.
  */
object TerminationLevelsTransform extends InternalTransform {
  override def name(): String = "add_termination_levels"

  /** Name of the ghost pure method with which a type defines the level of its values (in addition to its atom). */
  val levelMethodName: String = "TerminationLevel"

  /** Returns the atoms of the functions, methods, and function literals declared in `members`. */
  def declaredAtoms(members: Iterable[in.Member]): Iterable[String] = members.flatMap { m =>
    val own = m match {
      case f: in.FunctionMember => Vector(TerminationAtom.function(f.name))
      case m: in.MethodMember => Vector(TerminationAtom.method(m.name))
      case _ => Vector.empty
    }
    own ++ m.deepCollect { case l: in.FunctionLitLike => TerminationAtom.literal(l.name) }
  }

  override def transform(p: in.Program): in.Program = {
    val seed = p.table.getTerminationOrder
    val (components, componentLt) = mainComponents(p, seed)
    val order = seed.copy(mainComponent = components, componentLt = componentLt)

    val newMembers = p.members.map(m => addLevels(m, order)(p.table))

    val definedMethods = newMembers.collect { case m: in.MethodLikeMember if !m.isInstanceOf[in.BuiltInMember] => m.name -> m }.toMap
    val definedFunctions = newMembers.collect { case f: in.FunctionLikeMember if !f.isInstanceOf[in.BuiltInMember] => f.name -> f }.toMap
    val definedLits = newMembers.flatMap(_.deepCollect { case l: in.FunctionLitLike => l.name -> l }).toMap
    val delta = new in.LookupTable(
      definedMethods = definedMethods,
      definedFunctions = definedFunctions,
      definedFuncLiterals = definedLits,
      terminationOrder = Some(order),
    )
    in.Program(p.types, newMembers, p.table.merge(delta))(p.info)
  }

  /*
   * Dependency graph and atom order of the package under verification.
   */

  /** Returns the strongly connected component of each atom of the package under verification and their order. */
  private def mainComponents(p: in.Program, seed: in.TerminationOrder): (Map[String, Int], Set[(Int, Int)]) = {
    def isMain(atom: String): Boolean = seed.atomPackage.get(atom).contains(seed.mainPackage)

    val units: Vector[(String, in.Node)] = p.members.flatMap {
      case f: in.FunctionMember => Vector(TerminationAtom.function(f.name) -> f)
      case m: in.MethodMember if !m.receiver.typ.isInstanceOf[in.InterfaceT] => Vector(TerminationAtom.method(m.name) -> m)
      case _ => Vector.empty
    }.filter { case (atom, _) => isMain(atom) }

    var edges: Set[(String, String)] = Set.empty
    var nodes: Set[String] = Set.empty

    def addUnit(atom: String, node: in.Node): Unit = {
      nodes += atom
      directDependencies(node)(p.table).foreach { case (dep, lit) =>
        edges += atom -> dep
        lit.foreach(l => addUnit(dep, l))
      }
    }
    units.foreach { case (atom, node) => addUnit(atom, node) }

    // a type is above its methods
    units.foreach {
      case (atom, m: in.MethodMember) => namedTypeOf(m.receiver.typ).foreach(t => edges += TerminationAtom.namedType(t) -> atom)
      case _ =>
    }

    val mainEdges = edges.filter { case (l, r) => (isMain(l) || nodes.contains(l)) && (isMain(r) || nodes.contains(r)) }
    val allNodes = (nodes ++ mainEdges.flatMap { case (l, r) => Set(l, r) }).toVector.sorted
    val component = stronglyConnectedComponents(allNodes, mainEdges)
    val lt = transitiveClosure(mainEdges.collect {
      case (caller, callee) if component(caller) != component(callee) => component(callee) -> component(caller)
    })
    (component, lt)
  }

  /**
    * Returns the atoms that `node` directly depends on, without entering function literals. For a function literal,
    * the literal itself is returned as well such that its body can be analysed separately.
    */
  private def directDependencies(node: in.Node)(table: in.LookupTable): Vector[(String, Option[in.FunctionLitLike])] = {
    def isInterface(t: in.Type): Boolean = underlying(t, table).isInstanceOf[in.InterfaceT]

    def go(n: in.Node): Vector[(String, Option[in.FunctionLitLike])] = n match {
      case l: in.FunctionLitLike => Vector(TerminationAtom.literal(l.name) -> Some(l))
      case _ =>
        val here: Vector[String] = n match {
          case in.FunctionCall(_, func, _) => Vector(TerminationAtom.function(func))
          case in.PureFunctionCall(func, _, _, _) => Vector(TerminationAtom.function(func))
          case in.MethodCall(_, recv, meth, _) if !isInterface(recv.typ) => Vector(TerminationAtom.method(meth))
          case in.PureMethodCall(recv, meth, _, _, _) if !isInterface(recv.typ) => Vector(TerminationAtom.method(meth))
          case in.FunctionObject(func, _) => Vector(TerminationAtom.function(func))
          case in.MethodObject(recv, meth, _) if !isInterface(recv.typ) => Vector(TerminationAtom.method(meth))
          case in.ClosureObject(func, _) => Vector(TerminationAtom.literal(func))
          case in.ToInterface(e, _) => namedTypeOf(e.typ).map(TerminationAtom.namedType).toVector
          case _ => Vector.empty
        }
        here.map(_ -> None) ++ n.subnodes.flatMap(go)
    }

    node.subnodes.flatMap(go).toVector
  }

  /** Tarjan's algorithm; returns the index of the strongly connected component of each node. */
  private def stronglyConnectedComponents(nodes: Vector[String], edges: Set[(String, String)]): Map[String, Int] = {
    val succ: Map[String, Vector[String]] = edges.toVector.groupMap(_._1)(_._2).map { case (k, v) => k -> v.sorted }
    var index = 0
    var indices: Map[String, Int] = Map.empty
    var lowLink: Map[String, Int] = Map.empty
    var onStack: Set[String] = Set.empty
    var stack: List[String] = Nil
    var component: Map[String, Int] = Map.empty
    var nextComponent = 0

    def strongConnect(v: String): Unit = {
      indices += v -> index
      lowLink += v -> index
      index += 1
      stack = v :: stack
      onStack += v
      succ.getOrElse(v, Vector.empty).foreach { w =>
        if (!indices.contains(w)) {
          strongConnect(w)
          lowLink += v -> math.min(lowLink(v), lowLink(w))
        } else if (onStack.contains(w)) {
          lowLink += v -> math.min(lowLink(v), indices(w))
        }
      }
      if (lowLink(v) == indices(v)) {
        @tailrec
        def pop(): Unit = {
          val w = stack.head
          stack = stack.tail
          onStack -= w
          component += w -> nextComponent
          if (w != v) pop()
        }
        pop()
        nextComponent += 1
      }
    }

    nodes.foreach(v => if (!indices.contains(v)) strongConnect(v))
    component
  }

  private def transitiveClosure(rel: Set[(Int, Int)]): Set[(Int, Int)] = {
    val succ = rel.groupMap(_._1)(_._2)
    rel.map(_._1).flatMap { start =>
      var visited: Set[Int] = Set.empty
      var todo: List[Int] = succ.getOrElse(start, Set.empty).toList
      while (todo.nonEmpty) {
        val n = todo.head
        todo = todo.tail
        if (!visited.contains(n)) {
          visited += n
          todo = succ.getOrElse(n, Set.empty).toList ++ todo
        }
      }
      visited.map(start -> _)
    }
  }

  /*
   * Levels of members.
   */

  private def addLevels(m: in.Member, order: in.TerminationOrder)(table: in.LookupTable): in.Member = {
    val imported = order.isImported(m)
    val withMeasures = m match {
      case f: in.Function =>
        val measures = withLevel(f.terminationMeasures, in.LevelAtom(TerminationAtom.function(f.name))(f.info), f.args, imported, hide = f.body.isEmpty)(table)
        f.copy(terminationMeasures = measures)(f.info)
      case f: in.PureFunction =>
        val measures = withLevel(f.terminationMeasures, in.LevelAtom(TerminationAtom.function(f.name))(f.info), f.args, imported, hide = f.body.isEmpty)(table)
        f.copy(terminationMeasures = measures)(f.info)
      case m: in.Method =>
        val measures = withLevel(m.terminationMeasures, methodOwnLevel(m)(table), methodArgs(m), imported, hide = m.body.isEmpty && !isInterfaceMethod(m))(table)
        m.copy(terminationMeasures = measures)(m.info)
      case m: in.PureMethod =>
        val measures = withLevel(m.terminationMeasures, methodOwnLevel(m)(table), methodArgs(m), imported, hide = m.body.isEmpty && !isInterfaceMethod(m))(table)
        m.copy(terminationMeasures = measures)(m.info)
      case other => other
    }
    withMeasures.transform({
      case l: in.FunctionLit =>
        l.copy(terminationMeasures = withLevel(l.terminationMeasures, in.LevelAtom(TerminationAtom.literal(l.name))(l.info), l.args, imported, hide = false)(table))(l.info)
      case l: in.PureFunctionLit =>
        l.copy(terminationMeasures = withLevel(l.terminationMeasures, in.LevelAtom(TerminationAtom.literal(l.name))(l.info), l.args, imported, hide = false)(table))(l.info)
      case p: in.SpecImplementationProof =>
        // the implementation of a spec may have at most the level with which calls through the spec are charged:
        // the level of the closure for specs given by functions and the atom of the literal for literal specs
        val own = p.spec.func match {
          case _: in.FunctionProxy => in.LevelOf(p.closure)(p.info)
          case l: in.FunctionLitProxy => in.LevelAtom(TerminationAtom.literal(l))(p.info)
        }
        p.copy(terminationMeasures = withLevel(p.terminationMeasures, own, p.args, imported = false, hide = false)(table))(p.info)
    }, viper.silver.ast.utility.rewriter.Traverse.TopDown)
  }

  private def isLevelMethod(m: in.MethodMember): Boolean = m.name.name == levelMethodName

  private def isInterfaceMethod(m: in.MethodMember): Boolean = m.receiver.typ.isInstanceOf[in.InterfaceT]

  /** `TerminationLevel` methods have the empty level and do not depend on the levels of their arguments. */
  private def methodArgs(m: in.MethodMember): Vector[in.Expr] = if (isLevelMethod(m)) Vector.empty else m.args

  private def methodOwnLevel(m: in.MethodMember)(table: in.LookupTable): in.Expr = {
    val src = m.info
    if (isLevelMethod(m)) in.LevelEmpty()(src)
    else if (isInterfaceMethod(m)) in.LevelOf(m.receiver)(src)
    else {
      val atom = in.LevelAtom(TerminationAtom.method(m.name))(src)
      componentsLevel(m.receiver)(table).fold[in.Expr](atom)(in.LevelUnion(atom, _)(src))
    }
  }

  /**
    * Returns `e.TerminationLevel()` if the (non-interface) type of `e` declares a `TerminationLevel` method. The method
    * may be declared with a value or a pointer receiver.
    */
  def componentsLevel(e: in.Expr)(table: in.LookupTable): Option[in.Expr] = {
    val src = e.info
    val levelT = in.LevelT(Addressability.rValue)
    namedTypeOf(e.typ).flatMap { name =>
      table.getMethods.collectFirst {
        case m: in.PureMethod if m.name.name == levelMethodName && namedTypeOf(m.receiver.typ).contains(name) && !isInterfaceMethod(m) => m
      }
    }.flatMap { m =>
      (m.receiver.typ, e.typ) match {
        case (_: in.PointerT, _: in.PointerT) | (_: in.DefinedT, _: in.DefinedT) =>
          Some(in.PureMethodCall(e, m.name, Vector.empty, levelT)(src))
        case (d: in.DefinedT, _: in.PointerT) =>
          Some(in.PureMethodCall(in.Deref(e, d)(src), m.name, Vector.empty, levelT)(src))
        case _ =>
          // the level method has a pointer receiver but `e` is a value; its level cannot be computed from `e` alone
          None
      }
    }
  }

  private def withLevel(measures: Vector[in.TerminationMeasure], own: in.Expr, args: Vector[in.Expr], imported: Boolean, hide: Boolean)(table: in.LookupTable): Vector[in.TerminationMeasure] = {
    def info(m: in.TerminationMeasure): Source.Parser.Info =
      if (!imported) m.info else m.info match {
        case s: Source.Parser.Single => s.createAnnotatedInfo(ImportedTerminationMeasureAnnotation)
        case other => other
      }

    def tuple(t: Vector[in.Node]): Vector[in.Node] = {
      val (level, local) = t match {
        case (l: in.Expr) +: rest if l.typ.isInstanceOf[in.LevelT] => (in.LevelUnion(own, l)(own.info), rest)
        case _ =>
          val dyn = args.filter(a => isDynamic(a.typ, table)).map(a => in.LevelOf(a)(a.info): in.Expr)
          val level = dyn.reduceOption[in.Expr](in.LevelMax(_, _)(own.info)).fold(own)(d => in.LevelUnion(own, d)(own.info))
          (level, t)
      }
      // the local measures of imported members without bodies only matter for calls within their package
      if (imported && hide) Vector(level) else level +: local
    }

    measures.map {
      case m@in.ItfTupleTerminationMeasure(t, cond) => in.ItfTupleTerminationMeasure(tuple(t), cond)(info(m))
      case m@in.NonItfTupleTerminationMeasure(t, cond) => in.NonItfTupleTerminationMeasure(tuple(t), cond)(info(m))
      case m@in.ItfMethodWildcardMeasure(cond) => in.ItfMethodWildcardMeasure(cond)(info(m))
      case m@in.NonItfMethodWildcardMeasure(cond) => in.NonItfMethodWildcardMeasure(cond)(info(m))
    }
  }

  /** Values of interface and function types carry levels; calls through them are dynamically bound. */
  private def isDynamic(t: in.Type, table: in.LookupTable): Boolean = underlying(t, table) match {
    case _: in.InterfaceT | _: in.FunctionT => true
    case _ => false
  }

  @tailrec
  private def underlying(t: in.Type, table: in.LookupTable): in.Type = t match {
    case d: in.DefinedT => table.definedTypes.get((d.name, d.addressability)) match {
      case Some(u) => underlying(u, table)
      case None => d
    }
    case other => other
  }

  def namedTypeOf(t: in.Type): Option[String] = t match {
    case d: in.DefinedT => Some(d.name)
    case p: in.PointerT => namedTypeOf(p.t)
    case _ => None
  }
}
