package viper.gobra.ast.internal.utility

/** Code was taken from @see [[viper.silver.ast.utility.ViperStrategy]] */

import viper.gobra.ast.internal._
import viper.silver.ast.utility.rewriter.Traverse.Traverse
import viper.silver.ast.utility.rewriter._

/**
  * Gobra specific Wrapper for the rewriting Strategies
  * Provides automatic back transformations for Node rewrites
  *
  * @param p Partial function to perform rewritings
  * @tparam C Type of context
  */
class GobraStrategy[C <: Context[Node]](p: PartialFunction[(Node, C), Node]) extends Strategy[Node, C](p) {
  override def preserveMetaData(old: Node, now: Node, directlyRewritten: Boolean): Node = {
    GobraStrategy.preserveMetaData(old, now, directlyRewritten)
  }
}

/**
  * Gobra specific Wrapper for Regex Strategies
  * Provides automatic back transformations for Node to Node rewrites
  *
  * @param a The automaton generated from the regular expression
  * @param p PartialFunction that describes rewriting
  * @param d Default context
  * @tparam C Type of context
  */
class GobraRegexStrategy[C](a: TRegexAutomaton, p: PartialFunction[(Node, RegexContext[Node, C]), Node], d: PartialContextR[Node, C]) extends RegexStrategy[Node, C](a, p, d) {
  override def preserveMetaData(old: Node, now: Node, directlyRewritten: Boolean): Node = {
    GobraStrategy.preserveMetaData(old, now, directlyRewritten)
  }
}

class SlimGobraRegexStrategy[C](a: TRegexAutomaton, p: PartialFunction[Node, Node]) extends SlimRegexStrategy[Node](a, p) {
  override def preserveMetaData(old: Node, now: Node, directlyRewritten: Boolean): Node = {
    GobraStrategy.preserveMetaData(old, now, directlyRewritten)
  }
}


class GobraRegexBuilder[C](acc: (C, C) => C, comp: (C, C) => C, dflt: C) extends TreeRegexBuilder[Node, C](acc, comp, dflt) {

  /**
    * Generates a TreeRegexBuilderWithMatch by adding the matching part to the mix
    *
    * @param m Regular expression
    * @return TregexBuilderWithMatch that contains regex `f`
    */
  override def &>(m: Match): GobraRegexBuilderWithMatch[C] = new GobraRegexBuilderWithMatch[C](this, m)
}


class GobraRegexBuilderWithMatch[C](v: GobraRegexBuilder[C], m: Match) extends TreeRegexBuilderWithMatch[Node, C](v, m) {

  override def |->(p: PartialFunction[(Node, RegexContext[Node, C]), Node]): GobraRegexStrategy[C] = new GobraRegexStrategy[C](m.createAutomaton(), p, new PartialContextR[Node, C](v.default, v.accumulator, v.combinator))
}


class SlimGobraRegexBuilder {

  def &>(m: Match): SlimGobraRegexBuilderWithMatch = new SlimGobraRegexBuilderWithMatch(m)
}

class SlimGobraRegexBuilderWithMatch(regex: Match) {

  def |->(p: PartialFunction[Node, Node]): SlimGobraRegexStrategy[Node] = new SlimGobraRegexStrategy[Node](regex.createAutomaton(), p)
}

/**
  * Factory for standard rewriting configurations
  */
object GobraStrategy {

  def SlimRegex(m: Match, p: PartialFunction[Node, Node]): SlimGobraRegexStrategy[Node] = {
    new SlimGobraRegexBuilder &> m |-> p
  }

  def Regex[C](m: Match, p: PartialFunction[(Node, RegexContext[Node, C]), Node], default: C, acc: (C, C) => C, comb: (C, C) => C): GobraRegexStrategy[C] = {
    new GobraRegexBuilder[C](acc, comb, default) &> m |-> p
  }

  /**
    * Strategy without context
    *
    * @param p Partial function to perform rewriting
    * @param t Traversion mode
    * @return GobraStrategy
    */
  def Slim(p: PartialFunction[Node, Node], t: Traverse = Traverse.TopDown): Strategy[Node, SimpleContext[Node]] = {
    new GobraStrategy[SimpleContext[Node]](new AddArtificialContext(p)) defaultContext new NoContext[Node] traverse t
  }

  /**
    * Strategy with context about ancestors and siblings
    *
    * @param p Partial function to perform rewriting
    * @param t Traversion mode
    * @return GobraStrategy
    */
  def Ancestor(p: PartialFunction[(Node, ContextA[Node]), Node], t: Traverse = Traverse.TopDown): Strategy[Node, ContextA[Node]] = {
    new GobraStrategy[ContextA[Node]](p) defaultContext new PartialContextA[Node] traverse t
  }

  /**
    * Strategy with context about ancestors, siblings and custom context
    *
    * @param p          Partial function to perform rewriting
    * @param default    Default context
    * @param updateFunc Function that specifies how to update the custom context
    * @param t          Traversion mode
    * @tparam C Type of custom context
    * @return GobraStrategy
    */
  def Context[C](p: PartialFunction[(Node, ContextC[Node, C]), Node], default: C, updateFunc: PartialFunction[(Node, C), C] = PartialFunction.empty, t: Traverse = Traverse.TopDown): Strategy[Node, ContextC[Node, C]] = {
    new GobraStrategy[ContextC[Node, C]](p) defaultContext new PartialContextC[Node, C](default, updateFunc) traverse t
  }

  // AS: It might be more efficient to implement this one natively, and make Context a special case of it, rather than building a richer partial function and then desugaring whenever needed
  /**
    * Strategy with (only) custom context
    *
    * @param p          Partial function to perform rewriting
    * @param initialContext    Default context
    * @param updateFunc Function that specifies how to update the custom context
    * @param t          Traversion mode
    * @tparam C Type of custom context
    * @return GobraStrategy
    */
  def SimpleContext[C](p: PartialFunction[(Node, C), Node], initialContext: C, updateFunc: PartialFunction[(Node, C), C] = PartialFunction.empty, t: Traverse = Traverse.TopDown): Strategy[Node, ContextC[Node, C]] = {
    Context[C]({ // rewrite partial function taking context with parent access etc. to one just taking the custom context
      case (n, generalContext) if p.isDefinedAt(n, generalContext.c) => p.apply(n, generalContext.c)
    },
      initialContext, updateFunc, t
    )
  }

  /**
    * Function for automatic Error back transformation of nodes and conservation of metadata
    */
  def preserveMetaData(old: Node, now: Node, directlyRewritten: Boolean): Node = {
    /** @see [[viper.silver.ast.utility.ViperStrategy]] if meta data changes */
    now
  }


  def gobraDuplicatorStrategy: PartialFunction[(Node, Seq[AnyRef], Node.Meta), Node] = {
    case (n, args, info) => gobraDuplicator(n, args, info)
  }

  def gobraDuplicator[N <: Node](x: N, args: Seq[AnyRef], meta: Node.Meta): N = {
    val node: Node = (x, args) match {
        // Members
      case (p: Program, Seq(t: Vector[TopType@unchecked], m: Vector[Member@unchecked])) => Program(t, m)(meta)
      case (m: Method, Seq(rec: Parameter, arg: Vector[Parameter@unchecked], res: Vector[LocalVar.Val@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Block@unchecked])) => Method(rec, m.name, arg, res, pre, post, b)(meta)
      case (m: PureMethod, Seq(rec: Parameter, arg: Vector[Parameter@unchecked], res: Vector[LocalVar.Val@unchecked], pre: Vector[Assertion@unchecked], b: Option[Expr@unchecked])) => PureMethod(rec, m.name, arg, res, pre, b)(meta)
      case (f: Function, Seq(arg: Vector[Parameter@unchecked], res: Vector[LocalVar.Val@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Block@unchecked])) => Function(f.name, arg, res, pre, post, b)(meta)
      case (f: PureFunction, Seq(arg: Vector[Parameter@unchecked], res: Vector[LocalVar.Val@unchecked], pre: Vector[Assertion@unchecked], b: Option[Expr@unchecked])) => PureFunction(f.name, arg, res, pre, b)(meta)
      case (p: MPredicate, Seq(recv: Parameter, args: Vector[Parameter@unchecked], b: Option[Assertion@unchecked])) => MPredicate(recv, p.name, args, b)(meta)
      case (p: FPredicate, Seq(args: Vector[Parameter@unchecked], b: Option[Assertion@unchecked])) => FPredicate(p.name, args, b)(meta)
      case (f: Field.Ref, Seq()) => Field.Ref(f.name, f.typ, f.isEmbedding)(meta)
      case (f: Field.Val, Seq()) => Field.Val(f.name, f.typ, f.isEmbedding)(meta)
        // Statements
      case (b: Block, Seq(v: Vector[BottomDeclaration@unchecked], s: Vector[Stmt@unchecked])) => Block(v, s)(meta)
      case (s: Seqn, Seq(stmts: Vector[Stmt@unchecked])) => Seqn(stmts)(meta)
      case (i: If, Seq(cond: Expr, thn: Stmt, els: Stmt)) => If(cond, thn, els)(meta)
      case (w: While, Seq(cond: Expr, invs: Vector[Assertion@unchecked], body: Stmt)) => While(cond, invs, body)(meta)
      case (n: Make, Seq(target: LocalVar.Val, co: CompositeObject)) => Make(target, co)(meta)
      case (s: SingleAss, Seq(l: Assignee, r: Expr)) => SingleAss(l, r)(meta)
      case (a: Assignee.Var, Seq(v: BodyVar)) => Assignee.Var(v)
      case (a: Assignee.Pointer, Seq(e: Deref)) => Assignee.Pointer(e)
      case (a: Assignee.Field, Seq(e: FieldRef)) => Assignee.Field(e)
      case (f: FunctionCall, Seq(targets: Vector[LocalVar.Val@unchecked], func: FunctionProxy, args: Vector[Expr@unchecked])) => FunctionCall(targets, func, args)(meta)
      case (m: MethodCall, Seq(targets: Vector[LocalVar.Val@unchecked], recv: Expr, meth: MethodProxy, args: Vector[Expr@unchecked])) => MethodCall(targets, recv, meth, args, m.path)(meta)
      case (r: Return, Seq()) => Return()(meta)
      case (a: Assert, Seq(ass: Assertion)) => Assert(ass)(meta)
      case (a: Assume, Seq(ass: Assertion)) => Assume(ass)(meta)
      case (i: Inhale, Seq(ass: Assertion)) => Inhale(ass)(meta)
      case (e: Exhale, Seq(ass: Assertion)) => Exhale(ass)(meta)
      case (f: Fold, Seq(acc: Access)) => Fold(acc)(meta)
      case (u: Unfold, Seq(acc: Access)) => Unfold(acc)(meta)
        // Assertions
      case (s: SepAnd, Seq(l: Assertion, r: Assertion)) => SepAnd(l, r)(meta)
      case (e: ExprAssertion, Seq(exp: Expr)) => ExprAssertion(exp)(meta)
      case (i: Implication, Seq(l: Expr, r: Assertion)) => Implication(l, r)(meta)
      case (a: Access, Seq(acc: Accessible)) => Access(acc)(meta)
      case (a: Accessible.Pointer, Seq(d: Deref)) => Accessible.Pointer(d)
      case (a: Accessible.Field, Seq(f: FieldRef)) => Accessible.Field(f)
      case (a: Accessible.Predicate, Seq(p: PredicateAccess)) => Accessible.Predicate(p)
      case (p: FPredicateAccess, Seq(pred: FPredicateProxy, args: Vector[Expr@unchecked])) => FPredicateAccess(pred, args)(meta)
      case (p: MPredicateAccess, Seq(recv: Expr, pred: MPredicateProxy, args: Vector[Expr@unchecked])) => MPredicateAccess(recv, pred, args, p.path)(meta)
      case (p: MemoryPredicateAccess, Seq(arg: Expr)) => MemoryPredicateAccess(arg)(meta)
        // Expressions
      case (u: Unfolding, Seq(acc: Access, e: Expr)) => Unfolding(acc, e)(meta)
      case (f: PureFunctionCall, Seq(func: FunctionProxy, args: Vector[Expr@unchecked])) => PureFunctionCall(func, args, f.typ)(meta)
      case (m: PureMethodCall, Seq(recv: Expr, meth: MethodProxy, args: Vector[Expr@unchecked])) => PureMethodCall(recv, meth, args, m.path, m.typ)(meta)
      case (d: DfltVal, Seq()) => DfltVal(d.typ)(meta)
      case (t: Tuple, Seq(args: Vector[Expr@unchecked])) => Tuple(args)(meta)
      case (d: Deref, Seq(e: Expr)) => Deref(e, d.typ)(meta)
      case (r: Ref, Seq(ref: Addressable, t: PointerT)) => Ref(ref, t)(meta)
      case (f: FieldRef, Seq(recv: Expr, field: Field)) => FieldRef(recv, field, f.path)(meta)
      case (n: Negation, Seq(op: Expr)) => Negation(op)(meta)
      case (e: EqCmp, Seq(l: Expr, r: Expr)) => EqCmp(l, r)(meta)
      case (e: UneqCmp, Seq(l: Expr, r: Expr)) => UneqCmp(l, r)(meta)
      case (e: LessCmp, Seq(l: Expr, r: Expr)) => LessCmp(l, r)(meta)
      case (e: AtMostCmp, Seq(l: Expr, r: Expr)) => AtMostCmp(l, r)(meta)
      case (e: GreaterCmp, Seq(l: Expr, r: Expr)) => GreaterCmp(l, r)(meta)
      case (e: AtLeastCmp, Seq(l: Expr, r: Expr)) => AtLeastCmp(l, r)(meta)
      case (e: And, Seq(l: Expr, r: Expr)) => And(l, r)(meta)
      case (e: Or, Seq(l: Expr, r: Expr)) => Or(l, r)(meta)
      case (e: And, Seq(l: Expr, r: Expr)) => And(l, r)(meta)
      case (e: Sub, Seq(l: Expr, r: Expr)) => Sub(l, r)(meta)
      case (e: Mul, Seq(l: Expr, r: Expr)) => Mul(l, r)(meta)
      case (e: Mod, Seq(l: Expr, r: Expr)) => Mod(l, r)(meta)
      case (e: Div, Seq(l: Expr, r: Expr)) => Div(l, r)(meta)
      case (i: IntLit, Seq()) => IntLit(i.v)(meta)
      case (b: BoolLit, Seq()) => BoolLit(b.b)(meta)
      case (s: StructLit, Seq(args: Vector[Expr@unchecked])) => StructLit(s.typ, args)(meta)
      case (p: Parameter, Seq()) => Parameter(p.id, p.typ)(meta)
      case (l: LocalVar.Val, Seq()) => LocalVar.Val(l.id, l.typ)(meta)
      case (l: LocalVar.Ref, Seq()) => LocalVar.Ref(l.id, l.typ)(meta)
      case (a: Addressable.Var, Seq(v: LocalVar.Ref)) => Addressable.Var(v)
      case (a: Addressable.Pointer, Seq(v: Deref)) => Addressable.Pointer(v)
      case (a: Addressable.Field, Seq(v: FieldRef)) => Addressable.Field(v)
        // Proxy
      case (f: FunctionProxy, Seq()) => FunctionProxy(f.name)(meta)
      case (m: MethodProxy, Seq()) => MethodProxy(m.name, m.uniqueName)(meta)
      case (f: FPredicateProxy, Seq()) => FPredicateProxy(f.name)(meta)
      case (m: MPredicateProxy, Seq()) => MPredicateProxy(m.name, m.uniqueName)(meta)
      case _ => ???
    }

    node.asInstanceOf[N]
  }
}
