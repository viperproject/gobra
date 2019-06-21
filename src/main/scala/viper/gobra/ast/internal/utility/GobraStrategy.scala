package viper.gobra.ast.internal.utility

import viper.gobra.ast.internal._
import viper.gobra.reporting.Source

object GobraStrategy {

  def gobraDuplicatorStrategy: PartialFunction[(Node, Seq[AnyRef], Node.Meta), Node] = {
    case (n, args, info) => gobraDuplicator(n, args, info)
  }

  def gobraDuplicator[N <: Node](x: N, args: Seq[AnyRef], meta: Node.Meta): N = {
    val node: Node = (x, args) match {
        // Members
      case (p: Program, Seq(t: Vector[TopType@unchecked], v: Vector[GlobalVarDecl@unchecked], c: Vector[GlobalConst@unchecked], m: Vector[Method@unchecked], f: Vector[Function@unchecked])) => Program(t,v,c,m,f)(meta)
      case (m: Method, Seq(rec: Parameter, arg: Vector[Parameter@unchecked], res: Vector[LocalVar@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Block@unchecked])) => Method(rec, m.name, arg, res, pre, post, b)(meta)
      case (f: Function, Seq(arg: Vector[Parameter@unchecked], res: Vector[LocalVar@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Block@unchecked])) => Function(f.name, arg, res, pre, post, b)(meta)
        // Statements
      case (b: Block, Seq(v: Vector[LocalVar@unchecked], s: Vector[Stmt@unchecked])) => Block(v, s)(meta)
      case (s: Seqn, Seq(stmts: Vector[Stmt@unchecked])) => Seqn(stmts)(meta)
      case (s: SingleAss, Seq(l: Assignee, r: Expr)) => SingleAss(l, r)(meta)
      case (m: MultiAss, Seq(l: Vector[Assignee@unchecked], r: Expr)) => MultiAss(l, r)(meta)
      case (r: Return, Seq()) => Return()(meta)
      case (a: Assert, Seq(ass: Assertion)) => Assert(ass)(meta)
      case (a: Assume, Seq(ass: Assertion)) => Assume(ass)(meta)
      case (i: Inhale, Seq(ass: Assertion)) => Inhale(ass)(meta)
      case (e: Exhale, Seq(ass: Assertion)) => Exhale(ass)(meta)
        // Assertions
      case (s: Star, Seq(l: Assertion, r: Assertion)) => Star(l, r)(meta)
      case (e: ExprAssertion, Seq(exp: Expr)) => ExprAssertion(exp)(meta)
      case (i: Implication, Seq(l: Expr, r: Assertion)) => Implication(l, r)(meta)
      case (a: Access, Seq(acc: Accessible)) => Access(acc)(meta)
        // Expressions
      case (d: DfltVal, Seq()) => DfltVal(d.typ)(meta)
      case (d: Deref, Seq(e: Expr)) => Deref(e, d.typ)(meta)
      case (r: Ref, Seq(ref: Addressable, t: PointerT)) => Ref(ref, t)(meta)
      case (i: IntLit, Seq()) => IntLit(i.v)(meta)
      case (b: BoolLit, Seq()) => BoolLit(b.b)(meta)
      case (p: Parameter, Seq()) => Parameter(p.id, p.typ)(meta)
      case (l: LocalVar.Val, Seq()) => LocalVar.Val(l.id, l.typ)(meta)
      case (l: LocalVar.Ref, Seq()) => LocalVar.Ref(l.id, l.typ)(meta)

      case _ => ???
    }

    node.asInstanceOf[N]
  }
}
