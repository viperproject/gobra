// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.{internal => in}
import viper.gobra.ast.internal.TerminationAtom
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.{ImportedTerminationMeasureAnnotation, InterfaceMethodRankAnnotation, OutlineHeightAnnotation}
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
  * The levels of members are as follows (`dyn` is the maximum level of the arguments whose type is a function type or
  * an interface type with methods, or the level given by the user as the first component of a termination measure):
  *  - function `f`:                       `{f} + dyn`
  *  - method `T.M` of a non-interface type: `{T.M} + T.TerminationLevel(recv) + dyn` (the second summand is only
  *                                          present if `T` declares a method `TerminationLevel`)
  *  - interface method:                   `levelOf(recv) + dyn`
  *  - function literal `l`:               `{l} + dyn`
  *  - `TerminationLevel` methods and members whose measure starts with `bottomLevel()`: the empty level. These members
  *    form the bottom stratum: they may only call members of the bottom stratum and terminate by their remaining
  *    measures (typically predicate instances, whose order does not depend on any package).
  *
  * The atoms of the package under verification are ordered by the strongly connected components of its dependency
  * graph, where callees are below callers, a type is above its methods, and a member is above the types that it
  * converts to interfaces with methods or whose method values it creates, and above the function literals that it
  * creates. The atoms of imported packages are below the atoms of their importers, but the order among the atoms of an
  * imported package is not exposed. The order does not depend on the textual order of declarations.
  *
  * Interface methods additionally get a rank, which orders calls between methods of the same object in contracts (see
  * [[interfaceMethodRanks]]).
  *
  * Finally, the transformation marks the measures of members of imported packages, whose termination was checked
  * when their package was verified.
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
    val methodOwner = p.members.collect {
      case m: in.MethodMember if !isInterfaceMethod(m) => namedTypeOf(m.receiver.typ).map(t => TerminationAtom.method(m.name) -> TerminationAtom.namedType(t))
    }.flatten.toMap
    val order = seed.copy(mainComponent = components, componentLt = componentLt, methodOwner = methodOwner)

    val ranks = interfaceMethodRanks(p)
    val newMembers = p.members.map(m => addLevels(m, order, ranks)(p.table))

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
          // the level of a method object is the level of its receiver, which includes the atom of the receiver's type
          case in.MethodObject(recv, meth, _) if !isInterface(recv.typ) =>
            TerminationAtom.method(meth) +: namedTypeOf(recv.typ).map(TerminationAtom.namedType).toVector
          case in.ClosureObject(func, _) => Vector(TerminationAtom.literal(func))
          // converting a value to an interface allows calling its methods; interfaces without methods do not
          case in.ToInterface(e, itf) if callableInterface(itf, table) => namedTypeOf(e.typ).map(TerminationAtom.namedType).toVector
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

  private def addLevels(m: in.Member, order: in.TerminationOrder, ranks: Map[in.MethodProxy, Int])(table: in.LookupTable): in.Member = {
    val imported = order.isImported(m)
    val withMeasures = m match {
      case f: in.Function =>
        val measures = withLevel(f.terminationMeasures, in.LevelAtom(TerminationAtom.function(f.name))(f.info), f.args, imported)(table)
        val (pres, posts) = resolveOwnLevel(f.pres, f.posts, measures, TerminationAtom.function(f.name))
        f.copy(pres = pres, posts = posts, terminationMeasures = measures)(f.info)
      case f: in.PureFunction =>
        val measures = withLevel(f.terminationMeasures, in.LevelAtom(TerminationAtom.function(f.name))(f.info), f.args, imported)(table)
        val (pres, posts) = resolveOwnLevel(f.pres, f.posts, measures, TerminationAtom.function(f.name))
        f.copy(pres = pres, posts = posts, terminationMeasures = measures)(f.info)
      case m: in.Method =>
        val measures = withLevel(m.terminationMeasures, methodOwnLevel(m)(table), methodArgs(m), imported, ranks.get(m.name))(table)
        val (pres, posts) = resolveOwnLevel(m.pres, m.posts, measures, TerminationAtom.method(m.name))
        m.copy(pres = pres, posts = posts, terminationMeasures = measures)(m.info)
      case m: in.PureMethod =>
        val measures = withLevel(m.terminationMeasures, methodOwnLevel(m)(table), methodArgs(m), imported, ranks.get(m.name))(table)
        val (pres, posts) = resolveOwnLevel(m.pres, m.posts, measures, TerminationAtom.method(m.name))
        m.copy(pres = pres, posts = posts, terminationMeasures = measures)(m.info)
      case other => other
    }
    // the outlines of a member get the level of the member; the outlines of nested literals and proofs are overwritten
    // below, as the transformation proceeds top-down
    val outlines = m.deepCollect { case o: in.Outline => o.name -> o }.toMap
    val ownAtom: Option[in.Expr] = m match {
      case f: in.Function => Some(in.LevelAtom(TerminationAtom.function(f.name))(f.info))
      case f: in.PureFunction => Some(in.LevelAtom(TerminationAtom.function(f.name))(f.info))
      case m: in.MethodMember => Some(methodOwnLevel(m)(table))
      case _ => None
    }
    val withOutlines = ownAtom.fold(withMeasures)(own => leveledOutlines(withMeasures, memberMeasures(withMeasures), own, outlines))
    withOutlines.transform({
      case l: in.FunctionLit =>
        val own = in.LevelAtom(TerminationAtom.literal(l.name))(l.info)
        val measures = withLevel(l.terminationMeasures, own, l.args, imported)(table)
        val (pres, posts) = resolveOwnLevel(l.pres, l.posts, measures, TerminationAtom.literal(l.name))
        leveledOutlines(l.copy(pres = pres, posts = posts, terminationMeasures = measures)(l.info), measures, own, outlines)
      case l: in.PureFunctionLit =>
        val measures = withLevel(l.terminationMeasures, in.LevelAtom(TerminationAtom.literal(l.name))(l.info), l.args, imported)(table)
        val (pres, posts) = resolveOwnLevel(l.pres, l.posts, measures, TerminationAtom.literal(l.name))
        l.copy(pres = pres, posts = posts, terminationMeasures = measures)(l.info)
      case p: in.SpecImplementationProof =>
        // the implementation of a spec may have at most the level with which calls through the spec are charged:
        // the level of the closure for specs given by functions and the atom of the literal for literal specs
        val own = p.spec.func match {
          case _: in.FunctionProxy => in.LevelOf(p.closure)(p.info)
          case l: in.FunctionLitProxy => in.LevelAtom(TerminationAtom.literal(l))(p.info)
        }
        val measures = withLevel(p.terminationMeasures, own, p.args, imported = false)(table)
        // the proof carries the contract of the spec, in which `ownLevel()` denotes the level of the closure
        val key = p.spec.func match {
          case f: in.FunctionProxy => TerminationAtom.function(f)
          case l: in.FunctionLitProxy => TerminationAtom.literal(l)
        }
        val (pres, posts) = resolveOwnLevel(p.pres, p.posts, measures, key)
        leveledOutlines(p.copy(pres = pres, posts = posts, terminationMeasures = measures)(p.info), measures, own, outlines)
    }, viper.silver.ast.utility.rewriter.Traverse.TopDown)
  }

  private def memberMeasures(m: in.Member): Vector[in.TerminationMeasure] = m match {
    case f: in.Function => f.terminationMeasures
    case f: in.PureFunction => f.terminationMeasures
    case m: in.Method => m.terminationMeasures
    case m: in.PureMethod => m.terminationMeasures
    case _ => Vector.empty
  }

  /**
    * Outlines. An `outline` block is encoded as a method that is called where the block occurs, so its calls are calls of
    * the enclosing member (or literal, or implementation proof). The measure of an outline starts with the level of
    * the enclosing member, continues with the measure of the outline, and ends with a kind marker that is below all
    * other kind markers (see `TerminationEncoding`). The call of the outline is checked like every other call, so the
    * measure of the outline (excluding the marker) has to be at most the measure of the enclosing member. This makes
    * the measure of the outline irrelevant for soundness: calls in the outline that are accepted are also accepted in
    * the enclosing member. Outlines nested in outlines have a smaller marker, determined by their nesting height.
    *
    * The level of the enclosing member is the first element of its measures. If its measures do not start with the
    * same level, the empty level is used, which only rejects more calls in the outline. As the body of a member works
    * with copies of the parameters, the level of an outline is stated in terms of these copies. Should a copy be
    * assigned before the outline, the outline merely gets a different level, which the check of its call compares
    * with the level of the member on entry. Members without measures
    * (i.e., members that may not terminate, or that are assumed to terminate) give their outlines their own atom: the
    * outline can only be reached through the member, whose callers do not rely on the outline.
    */
  private def leveledOutlines[N <: in.Node](n: N, enclosingMeasures: Vector[in.TerminationMeasure], own: in.Expr,
                                            originals: Map[String, in.Outline]): N = {
    val heads = enclosingMeasures.collect { case t: in.TupleTerminationMeasure => t.tuple.headOption }
    val levelHeads = heads.flatten.collect { case e: in.Expr if e.typ.isInstanceOf[in.LevelT] => e }
    val enclosingLevel =
      if (heads.isEmpty) own
      else if (levelHeads.size == heads.size && levelHeads.distinct.size == 1) levelHeads.head
      else in.LevelEmpty()(own.info)
    val copies: Map[in.Parameter.In, in.LocalVar] = n.deepCollect {
      case in.SingleAss(in.Assignee.Var(q: in.LocalVar), p: in.Parameter.In) => p -> q
    }.toMap
    val level = enclosingLevel.transform({ case p: in.Parameter.In if copies.contains(p) => copies(p) }, viper.silver.ast.utility.rewriter.Traverse.TopDown)

    def height(o: in.Outline): Int = o.body.deepCollect { case i: in.Outline => i }.map(height).maxOption.fold(0)(_ + 1)
    def annotate(i: Source.Parser.Info, annotation: Source.Annotation): Source.Parser.Info = i match {
      case s: Source.Parser.Single => s.createAnnotatedInfo(annotation)
      case other => other
    }
    n.transform({
      case o: in.Outline =>
        val original = originals.getOrElse(o.name, o)
        val h = height(original)
        val measures = original.terminationMeasures.map {
          case m@in.NonItfTupleTerminationMeasure(t, cond) =>
            in.NonItfTupleTerminationMeasure(level +: t, cond)(annotate(m.info, OutlineHeightAnnotation(h)))
          case m@in.ItfTupleTerminationMeasure(t, cond) =>
            in.ItfTupleTerminationMeasure(level +: t, cond)(annotate(m.info, OutlineHeightAnnotation(h)))
          case wildcard => wildcard
        }
        o.copy(terminationMeasures = measures)(o.info)
    }, viper.silver.ast.utility.rewriter.Traverse.TopDown)
  }

  /**
    * Ranks of interface methods. Methods of the same object have the same level, so calls between them have to be
    * ordered differently. This matters for the contracts of interface methods, which commonly call other (pure) methods
    * of the receiver, e.g., `requires IsElem(e)`. The rank of an interface method is one plus the maximum rank of the
    * interface methods called in its contract (and the same for all methods of a cycle). The rank is part of the kind
    * marker that ends the measures of interface methods and of their implementation proofs (see `TerminationEncoding`),
    * which is only compared if the levels and local measures of two members are equal. The rank only depends on the
    * declarations of interfaces, which every importing package sees as well, so all packages agree on it.
    */
  def interfaceMethodRanks(p: in.Program): Map[in.MethodProxy, Int] = {
    val itfMethods: Vector[(in.MethodProxy, Vector[in.Assertion])] = p.members.collect {
      case m: in.Method if isInterfaceMethod(m) => m.name -> (m.pres ++ m.posts)
      case m: in.PureMethod if isInterfaceMethod(m) => m.name -> (m.pres ++ m.posts)
    }
    val known = itfMethods.map(_._1).toSet
    val key = (m: in.MethodProxy) => m.uniqueName
    val edges: Set[(String, String)] = itfMethods.flatMap { case (m, contract) =>
      contract.flatMap(_.deepCollect {
        case in.PureMethodCall(recv, meth, _, _, _) if known.contains(meth) && underlying(recv.typ, p.table).isInstanceOf[in.InterfaceT] => meth
      }).map(callee => key(m) -> key(callee))
    }.toSet
    val component = stronglyConnectedComponents(itfMethods.map(m => key(m._1)).sorted.distinct, edges)
    val successors: Map[Int, Set[Int]] = edges.toVector.collect {
      case (l, r) if component(l) != component(r) => component(l) -> component(r)
    }.groupMap(_._1)(_._2).map { case (k, v) => k -> v.toSet }
    var rankOf: Map[Int, Int] = Map.empty
    def rank(c: Int): Int = rankOf.getOrElse(c, {
      val r = 1 + successors.getOrElse(c, Set.empty).map(rank).maxOption.getOrElse(0)
      rankOf += c -> r
      r
    })
    itfMethods.map { case (m, _) => m -> rank(component(key(m))) }.toMap
  }

  /**
    * Replaces `ownLevel()` in the contract of a member by the member's level, i.e., the first component of its (first)
    * termination measure, evaluated in the pre-state. A member without a termination measure has no level; there,
    * `ownLevel()` denotes an atom about which nothing is known, which makes facts about it useless but harmless.
    */
  private def resolveOwnLevel(pres: Vector[in.Assertion], posts: Vector[in.Assertion], measures: Vector[in.TerminationMeasure],
                              key: String): (Vector[in.Assertion], Vector[in.Assertion]) = {
    val level: Option[in.Expr] = measures.collectFirst {
      case t: in.TupleTerminationMeasure if t.tuple.headOption.exists(_.isInstanceOf[in.Expr]) => t.tuple.head.asInstanceOf[in.Expr]
    }
    def replace(a: in.Assertion, inPost: Boolean): in.Assertion = a.transform {
      case o: in.LevelOwn => level match {
        case Some(l) if inPost => in.Old(l)(o.info)
        case Some(l) => l
        case None => in.LevelAtom(s"own:$key")(o.info)
      }
    }
    (pres.map(replace(_, inPost = false)), posts.map(replace(_, inPost = true)))
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

  private def withLevel(measures: Vector[in.TerminationMeasure], own: in.Expr, args: Vector[in.Expr], imported: Boolean,
                        rank: Option[Int] = None)(table: in.LookupTable): Vector[in.TerminationMeasure] = {
    def annotate(i: Source.Parser.Info, annotation: Source.Annotation): Source.Parser.Info = i match {
      case s: Source.Parser.Single => s.createAnnotatedInfo(annotation)
      case other => other
    }
    def info(m: in.TerminationMeasure): Source.Parser.Info = {
      val withImport = if (imported) annotate(m.info, ImportedTerminationMeasureAnnotation) else m.info
      rank.fold(withImport)(r => annotate(withImport, InterfaceMethodRankAnnotation(r)))
    }

    def tuple(t: Vector[in.Node]): Vector[in.Node] = {
      val (level, local) = t match {
        // bottom stratum: the empty level, without an atom of its own and without the levels of the arguments
        case (b: in.LevelBottom) +: rest => (in.LevelEmpty()(b.info), rest)
        case (l: in.Expr) +: rest if l.typ.isInstanceOf[in.LevelT] => (in.LevelUnion(own, l)(own.info), rest)
        case _ =>
          val dyn = args.filter(a => isDynamic(a.typ, table)).map(a => in.LevelOf(a)(a.info): in.Expr)
          val level = dyn.reduceOption[in.Expr](in.LevelMax(_, _)(own.info)).fold(own)(d => in.LevelUnion(own, d)(own.info))
          (level, t)
      }
      level +: local
    }

    measures.map {
      case m@in.ItfTupleTerminationMeasure(t, cond) => in.ItfTupleTerminationMeasure(tuple(t), cond)(info(m))
      case m@in.NonItfTupleTerminationMeasure(t, cond) => in.NonItfTupleTerminationMeasure(tuple(t), cond)(info(m))
      case m@in.ItfMethodWildcardMeasure(cond) => in.ItfMethodWildcardMeasure(cond)(info(m))
      case m@in.NonItfMethodWildcardMeasure(cond) => in.NonItfMethodWildcardMeasure(cond)(info(m))
    }
  }

  /**
    * Values of interface types with methods and values of function types carry levels; calls through them are
    * dynamically bound. Values of interfaces without methods (e.g., `interface{}`) cannot be called.
    */
  private def isDynamic(t: in.Type, table: in.LookupTable): Boolean = underlying(t, table) match {
    case itf: in.InterfaceT => interfacesWithLeveledMethods(table).contains(itf.name)
    case _: in.FunctionT => true
    case _ => false
  }

  private def hasMethods(itf: in.InterfaceT, table: in.LookupTable): Boolean = interfacesWithMethods(table).contains(itf.name)

  /** Names of the interfaces with methods, per lookup table (computing them is linear in the number of methods). */
  private val interfacesWithMethodsCache = scala.collection.concurrent.TrieMap.empty[in.LookupTable, Set[String]]

  /**
    * Returns the names of the interfaces that declare methods, or that embed such interfaces (an embedding interface
    * implements the embedded one).
    */
  private def interfacesWithMethods(table: in.LookupTable): Set[String] = interfacesWithMethodsCache.getOrElseUpdate(table, {
    val direct = table.getMethods.collect { case m: in.MethodMember if isInterfaceMethod(m) => m.receiver.typ.asInstanceOf[in.InterfaceT].name }.toSet
    val embedding = table.getImplementations.toVector.collect {
      case (itf, impls) if direct.contains(itf.name) => impls.collect { case i: in.InterfaceT => i.name }
    }.flatten
    direct ++ embedding
  })

  /**
    * Names of the interfaces with a method whose calls are charged with the level of the receiver, i.e., a method with
    * a tuple measure outside of the bottom stratum. Calls of the other methods are charged with the empty level (bottom
    * stratum) or not compared at all (no or wildcard measures), so arguments of the other interfaces do not contribute
    * to the levels of members. This only removes components from levels, which makes the checks in these members
    * stricter (e.g., a member converting such an argument to another interface cannot call it).
    */
  private def interfacesWithLeveledMethods(table: in.LookupTable): Set[String] = interfacesWithLeveledMethodsCache.getOrElseUpdate(table, {
    def leveled(measures: Vector[in.TerminationMeasure]): Boolean = measures.exists {
      case t: in.TupleTerminationMeasure => !t.tuple.headOption.exists(_.isInstanceOf[in.LevelBottom])
      case _ => false
    }
    val direct = table.getMethods.collect {
      case m: in.Method if isInterfaceMethod(m) && leveled(m.terminationMeasures) => m.receiver.typ.asInstanceOf[in.InterfaceT].name
      case m: in.PureMethod if isInterfaceMethod(m) && leveled(m.terminationMeasures) => m.receiver.typ.asInstanceOf[in.InterfaceT].name
    }.toSet
    val embedding = table.getImplementations.toVector.collect {
      case (itf, impls) if direct.contains(itf.name) => impls.collect { case i: in.InterfaceT => i.name }
    }.flatten
    direct ++ embedding
  })

  private val interfacesWithLeveledMethodsCache = scala.collection.concurrent.TrieMap.empty[in.LookupTable, Set[String]]

  /** Returns false only for interface types without methods. */
  private def callableInterface(t: in.Type, table: in.LookupTable): Boolean = underlying(t, table) match {
    case itf: in.InterfaceT => hasMethods(itf, table)
    case _ => true
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
