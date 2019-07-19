package viper.gobra.ast.internal

import org.bitbucket.inkytonik.kiama
import viper.gobra.ast.printing.PrettyPrinterCombinators

trait PrettyPrinter {
  def format(node: Node): String
}

class DefaultPrettyPrinter extends PrettyPrinter with kiama.output.PrettyPrinter with PrettyPrinterCombinators {

  override val defaultIndent = 2
  override val defaultWidth  = 80

  override def format(node: Node): String =
    pretty(show(node)).layout

  def show(n: Node): Doc = n match {
    case n: Program => showProgram(n)
    case n: Function => showFunction(n)
    case n: Method => showMethod(n)
    case n: Stmt => showStmt(n)
    case n: Assignee => showAssignee(n)
    case n: Assertion => showAss(n)
    case n: Accessible => showAcc(n)
    case n: Expr => showExpr(n)
    case n: Addressable => showAddressable(n)
    case n: Proxy => showProxy(n)
  }

  // program

  def showProgram(p: Program): Doc = p match {
    case Program(types, variables, constants, methods, functions) =>
      ssep(types.collect{ case n: DefinedT => n} map  showTypeDecl, line <> line) <>
      ssep(methods map showMethod, line <> line) <>
      ssep(functions map showFunction, line <> line)
  }

  // member

  def showFunction(f: Function): Doc = f match {
    case Function(name, args, results, pres, posts, body) =>
      "func" <+> name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts)) <> opt(body)(b => block(showStmt(b)))
  }

  def showMethod(m: Method): Doc = m match {
    case Method(receiver, name, args, results, pres, posts, body) =>
      "func" <+> parens(showVarDecl(receiver)) <+> name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts)) <> opt(body)(b => block(showStmt(b)))
  }

  def showTypeDecl(t: DefinedT): Doc =
    "type" <+> t.name <+> showType(t.right)

  def showPreconditions[T <: Assertion](list: Vector[T]): Doc =
    ssep(list  map ("requires" <> showAss(_)), line)

  def showPostconditions[T <: Assertion](list: Vector[T]): Doc =
    ssep(list  map ("ensures " <> showAss(_)), line)

  def showFormalArgList[T <: Parameter](list: Vector[T]): Doc =
    showVarDeclList(list)

  // statements

  def showStmt(s: Stmt): Doc = s match {
    case Block(variables, stmts) => "decl" <+> showVarDeclList(variables) <> line <> showStmtList(stmts)
    case Seqn(stmts) => ssep(stmts map showStmt, line)
    case If(cond, thn, els) => "if" <> parens(showExpr(cond)) <+> block(showStmt(thn)) <+> "else" <+> block(showStmt(els))
    case While(cond, invs, body) => "while" <> parens(showExpr(cond)) <> line <>
      ssep(invs  map ("invariant " <> showAss(_)), line) <> block(showStmt(body))
    case SingleAss(left, right) => showAssignee(left) <+> "=" <+> showExpr(right)

    case FunctionCall(targets, func, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <>
        func.name <> parens(showExprList(args))

    case Return() => "return"
    case Assert(ass) => "assert" <+> showAss(ass)
    case Assume(ass) => "assume" <+> showAss(ass)
    case Inhale(ass) => "inhale" <+> showAss(ass)
    case Exhale(ass) => "exhale" <+> showAss(ass)
  }

  def showProxy(x: Proxy): Doc = x match {
    case FunctionProxy(name) => name
  }

  private def showStmtList[T <: Stmt](list: Vector[T]): Doc =
    ssep(list map showStmt, line)

  private def showVarList[T <: Var](list: Vector[T]): Doc =
    showList(list)(showVar)

  private def showVarDeclList[T <: Var](list: Vector[T]): Doc =
    showList(list)(showVarDecl)

  private def showAssigneeList[T <: Assignee](list: Vector[T]): Doc =
    showList(list)(showAssignee)

  private def showExprList[T <: Expr](list: Vector[T]): Doc =
    showList(list)(showExpr)

  def showAssignee(ass: Assignee): Doc = ass match {
    case Assignee.Var(v) => showVar(v)
    case Assignee.Pointer(e) => showExpr(e)
  }

  // assertions

  def showAss(a: Assertion): Doc = a match {
    case SepAnd(left, right) => showAss(left) <+> "&&" <+> showAss(right)
    case ExprAssertion(exp) => showExpr(exp)
    case Implication(left, right) => showExpr(left) <+> "==>" <+> showAss(right)
    case Access(e) => "acc" <> parens(showAcc(e))
  }

  def showAcc(acc: Accessible): Doc = acc match {
    case Accessible.Ref(der) => showExpr(der)
  }

  // expressions

  def showExpr(e: Expr): Doc = e match {
    case DfltVal(typ) => "dflt" <> braces(showType(typ))
    case Tuple(args) => parens(showExprList(args))
    case Deref(exp, typ) => "*" <> showExpr(exp)
    case Ref(ref, typ) => "&" <> showAddressable(ref)
    case Negation(op) => "!" <> showExpr(op)
    case BinaryExpr(left, op, right, _) => showExpr(left) <+> op <+> showExpr(right)
    case lit: Lit => showLit(lit)
    case v: Var   => showVar(v)
  }

  def showAddressable(a: Addressable): Doc = a match {
    case Addressable.Var(v) => showVar(v)
  }

  // literals

  def showLit(l: Lit): Doc = l match {
    case IntLit(v) => v.toString
    case BoolLit(b) => if (b) "true" else "false"
  }

  // variables

  def showVar(v: Var): Doc = v match {
    case Parameter(id, _)    => id
    case LocalVar.Ref(id, _) => id
    case LocalVar.Val(id, _) => id
  }

  def showVarDecl(v: Var): Doc = v match {
    case Parameter(id, t)    => id <> ":" <+> showType(t)
    case LocalVar.Ref(id, t) => id <> ":" <+> "!" <> showType(t)
    case LocalVar.Val(id, t) => id <> ":" <+> showType(t)
  }

  // types

  def showType(typ: Type): Doc = typ match {
    case BoolT => "bool"
    case IntT => "int"
    case VoidT => "void"
    case PermissionT => "perm"
    case DefinedT(name, _) => name
    case PointerT(t) => "*" <> showType(t)
    case TupleT(ts) => parens(showTypeList(ts))
  }

  private def showTypeList[T <: Type](list: Vector[T]): Doc =
    showList(list)(showType)

  def showList[T](list: Vector[T])(f: T => Doc): Doc = ssep(list map f, comma <> space)

}
