// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal

import org.bitbucket.inkytonik.kiama
import org.bitbucket.inkytonik.kiama.util.Trampolines.Done
import viper.gobra.ast.printing.PrettyPrinterCombinators
import viper.gobra.theory.Addressability
import viper.silver.ast.{Position => GobraPosition}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait PrettyPrinter {
  def format(node : Node): String
  def format(typ : Type): String
}

class DefaultPrettyPrinter extends PrettyPrinter with kiama.output.PrettyPrinter with PrettyPrinterCombinators {

  override val defaultIndent = 2
  override val defaultWidth  = 80

  override def format(node : Node) : String = {
    positionStore.clear()
    pretty(show(node)).layout
  }

  override def format(typ : Type) : String = {
    positionStore.clear()
    pretty(showType(typ)).layout
  }

  /**
    * Fixes issue with Position not being correct when there are newlines with indentation.
    */
  override def line(repl: String): Doc =
    new Doc({
      case (i, w) =>
        val width = repl.length
        val outLine =
          (h: Horizontal) => (o: Out) =>
            Done(
              (r: Remaining) =>
                if (h)
                  output(o, r - width, Text(repl))
                else
                  output(o, w - i, Text("\n" + " " * i))
            )
        scan(width + i, outLine)
    })

  override def line: Doc = line(" ")

  /**
    * Used for mapping positions in a Gobra Program to Positions of the internal Program.
    */
  type ViperPosition = (PPosition, Int)
  val positionStore: mutable.Map[GobraPosition, ListBuffer[(PPosition, PPosition)]] = mutable.Map[GobraPosition, ListBuffer[ViperPosition]]()

  def addPosition(gobraPos: GobraPosition, viperPos: ViperPosition) {
    positionStore.get(gobraPos) match {
      case Some(b) => b += viperPos
      case None => positionStore += (gobraPos -> ListBuffer(viperPos))
    }
  }

  def updatePositionStore(n: Node): Doc = n.getMeta.origin match {
    case Some(origin) =>
      new Doc(
        (iw: IW) =>
          (c: TreeCont) =>
            emptyDoc(iw)((p: PPosition, dq: Dq) => {
              addPosition(origin.pos, (p, 1))
              c(p, dq)
            })
      )
    case None =>
      emptyDoc
  }


  def show(n: Node): Doc = n match {
    case n: Program => showProgram(n)
    case n: Member => showMember(n)
    case n: Field => showField(n)
    case n: Stmt => showStmt(n)
    case n: Assignee => showAssignee(n)
    case n: CompositeObject => showCompositeObject(n)
    case n: Assertion => showAss(n)
    case n: Accessible => showAcc(n)
    case n: PredicateAccess => showPredicateAcc(n)
    case n: Expr => showExpr(n)
    case n: Addressable => showAddressable(n)
    case n: Proxy => showProxy(n)
  }

  // program

  def showProgram(p: Program): Doc = p match {
    case Program(types, members, _) =>
      updatePositionStore(p) <>
      (ssep(types map showTopType, line <> line) <>
      ssep(members map showMember, line <> line))
  }

  // member

  def showTopType(t: TopType): Doc = t match {
    case d: DefinedT => showTypeDecl(d)
    case _ => emptyDoc
  }

  def showMember(m: Member): Doc = updatePositionStore(m) <> (m match {
    case n: Method => showMethod(n)
    case n: PureMethod => showPureMethod(n)
    case n: Function => showFunction(n)
    case n: PureFunction => showPureFunction(n)
    case n: FPredicate => showFPredicate(n)
    case n: MPredicate => showMPredicate(n)
    case n: GlobalConstDecl => showGlobalConstDecl(n)
  })

  def showFunction(f: Function): Doc = f match {
    case Function(name, args, results, pres, posts, body) =>
      "func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts)) <> opt(body)(b => block(showStmt(b)))
  }

  def showPureFunction(f: PureFunction): Doc = f match {
    case PureFunction(name, args, results, pres, posts, body) =>
      "pure func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts)) <> opt(body)(b => block("return" <+> showExpr(b)))
  }

  def showMethod(m: Method): Doc = m match {
    case Method(receiver, name, args, results, pres, posts, body) =>
      "func" <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts)) <> opt(body)(b => block(showStmt(b)))
  }

  def showPureMethod(m: PureMethod): Doc = m match {
    case PureMethod(receiver, name, args, results, pres, posts, body) =>
      "pure func" <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts)) <> opt(body)(b => block("return" <+> showExpr(b)))
  }

  def showFPredicate(predicate: FPredicate): Doc = predicate match {
    case FPredicate(name, args, body) =>
    "pred" <+> name.name <> parens(showFormalArgList(args)) <> opt(body)(b => block(showAss(b)))
  }

  def showMPredicate(predicate: MPredicate): Doc = predicate match {
    case MPredicate(recv, name, args, body) =>
      "pred" <+> parens(showVarDecl(recv)) <+> name.name <> parens(showFormalArgList(args)) <> opt(body)(b => block(showAss(b)))
  }

  def showGlobalConstDecl(globalConst: GlobalConstDecl): Doc = {
    "const" <+> showVarDecl(globalConst.left) <+> "=" <+> showLit(globalConst.right)
  }

  def showField(field: Field): Doc = updatePositionStore(field) <> (field match {
    case Field(name, typ, ghost) => "field" <> name <> ":" <+> showType(typ)
  })

  def showTypeDecl(t: DefinedT): Doc =
    "type" <+> t.name <+> "..." <> line

  def showPreconditions[T <: Assertion](list: Vector[T]): Doc =
    hcat(list  map ("requires " <> showAss(_) <> line))

  def showPostconditions[T <: Assertion](list: Vector[T]): Doc =
    hcat(list  map ("ensures " <> showAss(_) <> line))

  def showFormalArgList[T <: Parameter](list: Vector[T]): Doc =
    showVarDeclList(list)

  // statements

  def showStmt(s: Stmt): Doc = updatePositionStore(s) <> (s match {
    case Block(decls, stmts) => "decl" <+> showBlockDeclList(decls) <> line <> showStmtList(stmts)
    case Seqn(stmts) => ssep(stmts map showStmt, line)
    case If(cond, thn, els) => "if" <> parens(showExpr(cond)) <+> block(showStmt(thn)) <+> "else" <+> block(showStmt(els))
    case While(cond, invs, body) => "while" <> parens(showExpr(cond)) <> line <>
      hcat(invs  map ("invariant " <> showAss(_) <> line)) <> block(showStmt(body))

    case Make(target, typ) => showVar(target) <+> "=" <+> "new" <> brackets(showCompositeObject(typ))
    case SingleAss(left, right) => showAssignee(left) <+> "=" <+> showExpr(right)

    case FunctionCall(targets, func, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <>
        func.name <> parens(showExprList(args))

    case MethodCall(targets, recv, meth, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <>
        showExpr(recv) <> meth.name <> parens(showExprList(args))

    case Return() => "return"
    case Assert(ass) => "assert" <+> showAss(ass)
    case Assume(ass) => "assume" <+> showAss(ass)
    case Inhale(ass) => "inhale" <+> showAss(ass)
    case Exhale(ass) => "exhale" <+> showAss(ass)
    case Fold(acc)   => "fold" <+> showAss(acc)
    case Unfold(acc) => "unfold" <+> showAss(acc)
  })

  def showComposite(c: CompositeObject): Doc = showLit(c.op)

  def showProxy(x: Proxy): Doc = updatePositionStore(x) <> (x match {
    case FunctionProxy(name) => name
    case MethodProxy(name, _) => name
    case FPredicateProxy(name) => name
    case MPredicateProxy(name, _) => name
  })

  def showBlockDecl(x: BlockDeclaration): Doc = x match {
    case localVar: LocalVar => showVar(localVar) <> ":" <+> showType(localVar.typ) <> showAddressability(localVar.typ.addressability)
  }

  def showAddressability(x: Addressability): Doc = x match {
    case Addressability.Shared => "@"
    case Addressability.Exclusive => "°"
  }

  protected def showStmtList[T <: Stmt](list: Vector[T]): Doc =
    ssep(list map showStmt, line)

  protected def showVarList[T <: Var](list: Vector[T]): Doc =
    showList(list)(showVar)

  protected def showVarDeclList[T <: Var](list: Vector[T]): Doc =
    showList(list)(showVarDecl)

  protected def showBlockDeclList[T <: BlockDeclaration](list: Vector[T]): Doc =
    showList(list)(showBlockDecl)

  protected def showAssigneeList[T <: Assignee](list: Vector[T]): Doc =
    showList(list)(showAssignee)

  protected def showExprList[T <: Expr](list: Vector[T]): Doc =
    showList(list)(showExpr)

  def showAssignee(ass: Assignee): Doc = updatePositionStore(ass) <> (ass match {
    case Assignee.Var(v) => showVar(v)
    case Assignee.Pointer(e) => showExpr(e)
    case Assignee.Field(f) => showExpr(f)
    case Assignee.Index(e) => showExpr(e)
  })

  def showCompositeObject(co: CompositeObject): Doc = updatePositionStore(co) <> showLit(co.op)

  // assertions

  def showAss(a: Assertion): Doc = updatePositionStore(a) <> (a match {
    case SepAnd(left, right) => showAss(left) <+> "&&" <+> showAss(right)
    case ExprAssertion(exp) => showExpr(exp)
    case Implication(left, right) => showExpr(left) <+> "==>" <+> showAss(right)
    case Access(e) => "acc" <> parens(showAcc(e))
    case SepForall(vars, triggers, body) =>
      "forall" <+> showVarDeclList(vars) <+> "::" <+> showTriggers(triggers) <+> showAss(body)
  })

  def showAcc(acc: Accessible): Doc = updatePositionStore(acc) <> (acc match {
    case Accessible.Address(der) => showExpr(der)
    case Accessible.Predicate(op) => showPredicateAcc(op)
  })

  def showPredicateAcc(access: PredicateAccess): Doc = access match {
    case FPredicateAccess(pred, args) => pred.name <> parens(showExprList(args))
    case MPredicateAccess(recv, pred, args) => showExpr(recv) <> pred.name <> parens(showExprList(args))
    case MemoryPredicateAccess(arg) => "memory" <> parens(showExpr(arg))
  }

  def showTrigger(trigger: Trigger) : Doc = showExprList(trigger.exprs)
  def showTriggers(triggers: Vector[Trigger]) : Doc = "{" <+> showList(triggers)(showTrigger) <+> "}"

  // expressions

  def showExpr(e: Expr): Doc = updatePositionStore(e) <> (e match {
    case Unfolding(acc, exp) => "unfolding" <+> showAss(acc) <+> "in" <+> showExpr(exp)

    case Old(op, _) => "old(" <> showExpr(op) <> ")"

    case Conditional(cond, thn, els, _) => showExpr(cond) <> "?" <> showExpr(thn) <> ":" <> showExpr(els)

    case PureForall(vars, triggers, body) =>
      "forall" <+> showVarDeclList(vars) <+> "::" <+> showTriggers(triggers) <+> showExpr(body)

    case Exists(vars, triggers, body) =>
      "exists" <+>  showVarDeclList(vars) <+> "::" <+> showTriggers(triggers) <+> showExpr(body)

    case PureFunctionCall(func, args, _) =>
      func.name <> parens(showExprList(args))

    case PureMethodCall(recv, meth, args, _) =>
      showExpr(recv) <> meth.name <> parens(showExprList(args))

    case IndexedExp(base, index) => showExpr(base) <> brackets(showExpr(index))
    case ArrayUpdate(base, left, right) => showExpr(base) <> brackets(showExpr(left) <+> "=" <+> showExpr(right))
    case Length(exp) => "len" <> parens(showExpr(exp))
    case Capacity(exp) => "cap" <> parens(showExpr(exp))
    case RangeSequence(low, high) =>
      "seq" <> brackets(showExpr(low) <+> ".." <+> showExpr(high))
    case SequenceUpdate(seq, left, right) =>
      showExpr(seq) <> brackets(showExpr(left) <+> "=" <+> showExpr(right))
    case SequenceDrop(left, right) => showExpr(left) <> brackets(showExpr(right) <> colon)
    case SequenceTake(left, right) => showExpr(left) <> brackets(colon <> showExpr(right))
    case SequenceConversion(exp) => "seq" <> parens(showExpr(exp))
    case SetConversion(exp) => "set" <> parens(showExpr(exp))
    case Cardinality(op) => "|" <> showExpr(op) <> "|"
    case MultisetConversion(exp) => "mset" <> parens(showExpr(exp))
    case Conversion(typ, exp) => showType(typ) <> parens(showExpr(exp))

    case OptionNone(t) => "none" <> brackets(showType(t))
    case OptionSome(exp) => "some" <> parens(showExpr(exp))
    case OptionGet(exp) => "get" <> parens(showExpr(exp))

    case Slice(exp, low, high, max) => {
      val maxD = max.map(e => ":" <> showExpr(e)).getOrElse(emptyDoc)
      showExpr(exp) <> brackets(showExpr(low) <> ":" <> showExpr(high) <> maxD)
    }

    case DfltVal(typ) => "dflt" <> brackets(showType(typ))
    case Tuple(args) => parens(showExprList(args))
    case Deref(exp, typ) => "*" <> showExpr(exp)
    case Ref(ref, typ) => "&" <> showAddressable(ref)
    case FieldRef(recv, field) => showExpr(recv) <> "."  <> field.name
    case StructUpdate(base, field, newVal) => showExpr(base) <> brackets(showField(field) <+> ":=" <+> showExpr(newVal))
    case Negation(op) => "!" <> showExpr(op)
    case BinaryExpr(left, op, right, _) => showExpr(left) <+> op <+> showExpr(right)
    case lit: Lit => showLit(lit)
    case v: Var => showVar(v)
  })

  def showAddressable(a: Addressable): Doc = showExpr(a.op)

  // literals

  private def showGhostCollectionLiteral(front : String, typ : Type, exprs : Vector[Expr]) : Doc =
    front <> brackets(showType(typ)) <+>
      braces(space <> showExprList(exprs) <> (if (exprs.nonEmpty) space else emptyDoc))

  def showLit(l: Lit): Doc = l match {
    case IntLit(v) => v.toString
    case BoolLit(b) => if (b) "true" else "false"
    case NilLit(t) => parens("nil" <> ":" <> showType(t))

    case ArrayLit(typ, exprs) => {
      val lenP = brackets(exprs.length.toString)
      val typP = showType(typ)
      val exprsP = braces(space <> showExprList(exprs) <> (if (exprs.nonEmpty) space else emptyDoc))
      lenP <> typP <+> exprsP
    }

    case SliceLit(typ, exprs) => {
      val typP = showType(typ)
      val exprsP = braces(space <> showExprList(exprs) <> (if (exprs.nonEmpty) space else emptyDoc))
      brackets(emptyDoc) <> typP <+> exprsP
    }

    case StructLit(t, args) => showType(t) <> braces(showExprList(args))
    case SequenceLit(typ, exprs) => showGhostCollectionLiteral("seq", typ, exprs)
    case SetLit(typ, exprs) => showGhostCollectionLiteral("set", typ, exprs)
    case MultisetLit(typ, exprs) => showGhostCollectionLiteral("mset", typ, exprs)
  }

  // variables

  def showVar(v: Var): Doc = v match {
    case BoundVar(id, _) => id
    case Parameter.In(id, _)    => id
    case Parameter.Out(id, _)    => id
    case LocalVar(id, _) => id
    case GlobalConst.Val(id, _) => id
  }

  def showVarDecl(v: Var): Doc = v match {
    case BoundVar(id, t) => id <> ":" <+> showType(t)
    case Parameter.In(id, t)    => id <> ":" <+> showType(t)
    case Parameter.Out(id, t)    => id <> ":" <+> showType(t)
    case LocalVar(id, t) => id <> ":" <+> showType(t)
    case GlobalConst.Val(id, t) => id <> ":" <+> showType(t)
  }

  // types

  def showType(typ : Type) : Doc = typ match {
    case BoolT(_) => "bool"
    case IntT(_, kind) => kind.name
    case VoidT => "void"
    case PermissionT(_) => "perm"
    case DefinedT(name, _) => name
    case PointerT(t, _) => "*" <> showType(t)
    case TupleT(ts, _) => parens(showTypeList(ts))
    case struct: StructT => emptyDoc <> block(hcat(struct.fields map showField))
    case array : ArrayT => brackets(array.length.toString) <> showType(array.elems)
    case SequenceT(elem, _) => "seq" <> brackets(showType(elem))
    case SetT(elem, _) => "set" <> brackets(showType(elem))
    case MultisetT(elem, _) => "mset" <> brackets(showType(elem))
    case OptionT(elem, _) => "option" <> brackets(showType(elem))
    case SliceT(elem, _) => "[]" <> showType(elem)
  }

  private def showTypeList[T <: Type](list: Vector[T]): Doc =
    showList(list)(showType)

  def showList[T](list: Vector[T])(f: T => Doc): Doc = ssep(list map f, comma <> space)

}

class ShortPrettyPrinter extends DefaultPrettyPrinter {

  override val defaultIndent = 2
  override val defaultWidth  = 80

  override def format(node: Node): String =
    pretty(show(node)).layout


  override def showFunction(f: Function): Doc = f match {
    case Function(name, args, results, pres, posts, body) =>
      "func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts))
  }

  override def showPureFunction(f: PureFunction): Doc = f match {
    case PureFunction(name, args, results, pres, posts, body) =>
      "pure func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts))
  }

  override def showMethod(m: Method): Doc = m match {
    case Method(receiver, name, args, results, pres, posts, body) =>
      "func" <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts))
  }

  override def showPureMethod(m: PureMethod): Doc = m match {
    case PureMethod(receiver, name, args, results, pres, posts, body) =>
      "pure func" <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts))
  }

  override def showFPredicate(predicate: FPredicate): Doc = predicate match {
    case FPredicate(name, args, body) =>
      "pred" <+> name.name <> parens(showFormalArgList(args))
  }

  override def showMPredicate(predicate: MPredicate): Doc = predicate match {
    case MPredicate(recv, name, args, body) =>
      "pred" <+> parens(showVarDecl(recv)) <+> name.name <> parens(showFormalArgList(args))
  }

  // statements

  override def showStmt(s: Stmt): Doc = s match {
    case Block(decls, stmts) => "decl" <+> showBlockDeclList(decls)
    case Seqn(stmts) => emptyDoc
    case If(cond, thn, els) => "if" <> parens(showExpr(cond)) <+> "{...}" <+> "else" <+> "{...}"
    case While(cond, invs, body) => "while" <> parens(showExpr(cond)) <> line <>
      hcat(invs  map ("invariant " <> showAss(_) <> line))

    case Make(target, typ) => showVar(target) <+> "=" <+> "new" <> brackets(showCompositeObject(typ))
    case SingleAss(left, right) => showAssignee(left) <+> "=" <+> showExpr(right)

    case FunctionCall(targets, func, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <>
        func.name <> parens(showExprList(args))

    case MethodCall(targets, recv, meth, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <>
        showExpr(recv) <> meth.name <> parens(showExprList(args))

    case Return() => "return"
    case Assert(ass) => "assert" <+> showAss(ass)
    case Assume(ass) => "assume" <+> showAss(ass)
    case Inhale(ass) => "inhale" <+> showAss(ass)
    case Exhale(ass) => "exhale" <+> showAss(ass)
    case Fold(acc)   => "fold" <+> showAss(acc)
    case Unfold(acc) => "unfold" <+> showAss(acc)
  }
}
