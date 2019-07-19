package viper.gobra.ast.internal.utility

import viper.gobra.ast.internal.Node
import viper.gobra.ast.internal._

object Nodes {

  /** Returns a list of all direct sub-nodes of this node. The type of nodes is
    * included in this list only for declarations (but not for expressions, for
    * instance).
    *
    * Furthermore, pointers to declarations are not included either (e.g., the
    * `field` (which is of type `Node`) of a `FieldAccess`), and neither are
    * non-`Node` children (e.g., the `predicateName` (a `String`) of a
    * `PredicateAccess`).
    *
    * As a consequence, it is not sufficient to compare the subnodes of two
    * nodes for equality if one has to compare those two nodes for equality.
    */
  def subnodes(n: Node): Seq[Node] = { // TODO: maybe can be solved generally
    val subnodesWithoutType: Seq[Node] = n match {
      case Program(types, variables, constants, methods, functions) => variables ++ constants ++ methods ++ functions
      case Method(receiver, name, args, results, pres, posts, body) => Seq(receiver) ++ args ++ results ++ pres ++ posts ++ body
      case Function(name, args, results, pres, posts, body) => args ++ results ++ pres ++ posts ++ body
      case s: Stmt => s match {
        case Block(variables, stmts) => variables ++ stmts
        case Seqn(stmts) => stmts
        case If(cond, thn, els) => Seq(cond, thn, els)
        case While(cond, invs, body) => Seq(cond) ++ invs ++ Seq(body)
        case SingleAss(left, right) => Seq(left, right)
        case FunctionCall(targets, func, args) => targets ++ Seq(func) ++ args
        case Return() => Seq()
        case Assert(ass) => Seq(ass)
        case Exhale(ass) => Seq(ass)
        case Inhale(ass) => Seq(ass)
        case Assume(ass) => Seq(ass)
      }
      case Assignee.Var(v) => Seq(v)
      case Assignee.Pointer(e) => Seq(e)
      case a: Assertion => a match {
        case SepAnd(left, right) => Seq(left, right)
        case ExprAssertion(exp) => Seq(exp)
        case Implication(left, right) => Seq(left, right)
        case Access(e) => Seq(e)
      }
      case Accessible.Ref(der) => Seq(der)
      case e: Expr => e match {
        case DfltVal(typ) => Seq()
        case Tuple(args) => args
        case Deref(exp, typ) => Seq(exp)
        case Ref(ref, typ) => Seq(ref)
        case Negation(operand) => Seq(operand)
        case BinaryExpr(left, _, right, _) => Seq(left, right)
        case EqCmp(l, r) => Seq(l, r)
        case l: Lit => l match {
          case IntLit(v) => Seq()
          case BoolLit(v) => Seq()
        }
        case Parameter(id, typ) => Seq()
        case LocalVar.Ref(id, typ) => Seq()
        case LocalVar.Val(id, typ) => Seq()
      }
      case Addressable.Var(v) => Seq(v)
      case p: Proxy => p match {
        case FunctionProxy(name) => Seq()
      }
    }
//    n match {
//      case t: Typed => subnodesWithType ++ Seq(t.typ)
//      case _ => subnodesWithType
//    }
    subnodesWithoutType
  }

}
