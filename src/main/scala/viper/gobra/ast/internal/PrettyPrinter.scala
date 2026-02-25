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
import viper.gobra.util.{BackendAnnotation, Binary, Decimal, Hexadecimal, Octal}
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

  def addPosition(gobraPos: GobraPosition, viperPos: ViperPosition): Unit = {
    positionStore.get(gobraPos) match {
      case Some(b) => b += viperPos
      case None => positionStore += (gobraPos -> ListBuffer(viperPos))
    }
  }

  def updatePositionStore(n: Node): Doc = n.info.origin match {
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
    case s: ClosureSpec => showClosureSpec(s)
    case n: DomainFunc => showDomainFunc(n)
    case n: DomainAxiom => showDomainAxiom(n)
    case n: Stmt => showStmt(n)
    case n: Assignee => showAssignee(n)
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
    case n: DomainDefinition => showDomainDefinition(n)
    case n: MethodSubtypeProof => showMethodSubtypeProof(n)
    case n: PureMethodSubtypeProof => showPureMethodSubtypeProof(n)
    case n: GlobalConstDecl => showGlobalConstDecl(n)
    case n: GlobalVarDecl => showGlobalVarDecl(n)
    case n: BuiltInMember => showBuiltInMember(n)
    case n: AdtDefinition => showAdtDefinition(n)
  })
  
  def showTerminationMeasure(measure: TerminationMeasure): Doc = {
    def showCond(cond: Option[Expr]): Doc = opt(cond)("if" <+> showExpr(_))
    measure match {
      case m: WildcardMeasure => "_" <+> showCond(m.cond)
      case m: TupleTerminationMeasure =>
        hcat(m.tuple map show) <+> showCond(m.cond)
    }
  }

  def showFunction(f: Function): Doc = f match {
    case Function(name, args, results, pres, posts, measures, backendAnnotations, body) =>
      "func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <> opt(body)(b => block(showStmt(b)))
  }

  def showPureFunction(f: PureFunction): Doc = f match {
    case PureFunction(name, args, results, pres, posts, measures, backendAnnotations, body, isOpaque) =>
      val funcPrefix = (if (isOpaque) text("opaque ") else emptyDoc) <> "pure func"
      funcPrefix <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <> opt(body)(b => block("return" <+> showExpr(b)))
  }

  def showMethod(m: Method): Doc = m match {
    case Method(receiver, name, args, results, pres, posts, measures, backendAnnotations, body) =>
      "func" <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <> opt(body)(b => block(showStmt(b)))
  }

  def showPureMethod(m: PureMethod): Doc = m match {
    case PureMethod(receiver, name, args, results, pres, posts, measures, backendAnnotations, body, isOpaque) =>
      val funcPrefix = (if (isOpaque) text("opaque ") else emptyDoc) <> "pure func"
      funcPrefix <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <> opt(body)(b => block("return" <+> showExpr(b)))
  }

  def showMethodSubtypeProof(m: MethodSubtypeProof): Doc = m match {
    case MethodSubtypeProof(subProxy, superT, _, receiver, args, results, body) =>
      "proof" <+> parens(showType(superT)) <+> parens(showVarDecl(receiver)) <+> subProxy.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        opt(body)(b => block(showStmt(b)))
  }

  def showPureMethodSubtypeProof(m: PureMethodSubtypeProof): Doc = m match {
    case PureMethodSubtypeProof(subProxy, superT, _, receiver, args, results, body) =>
      "proof" <+> parens(showType(superT)) <+> "pure" <+> parens(showVarDecl(receiver)) <+> subProxy.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        opt(body)(b => block("return" <+> showExpr(b)))
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

  def showGlobalVarDecl(decl: GlobalVarDecl): Doc = {
    "var" <+> showVarDeclList(decl.left) <+> block(showStmtList(decl.declStmts))
  }

  def showBuiltInMember(member: BuiltInMember): Doc = {
    // return arguments, contracts, and potential bodies are not known to the internal representation
    def makeRecv(receiverT: Type): Parameter.In = Parameter.In("recv", receiverT)(member.info)
    val args: Vector[Parameter.In] = member.argsT.zipWithIndex.map {
      case (argT, idx) => Parameter.In(s"arg$idx", argT)(member.info)
    }
    member match {
      case BuiltInMethod(receiverT, tag, name, _) =>
        (if (tag.isPure) "pure" <> space else emptyDoc) <> "func" <+> parens(showVarDecl(makeRecv(receiverT))) <+> name.name <> parens(showFormalArgList(args))
      case BuiltInFunction(tag, name, _) =>
        (if (tag.isPure) "pure" <> space else emptyDoc) <> "func" <+> name.name <> parens(showFormalArgList(args))
      case BuiltInFPredicate(_, name, _) =>
        "pred" <+> name.name <> parens(showFormalArgList(args))
      case BuiltInMPredicate(receiverT, _, name, _) =>
        "pred" <+> parens(showVarDecl(makeRecv(receiverT))) <+> name.name <> parens(showFormalArgList(args))
    }
  }

  def showField(field: Field): Doc = updatePositionStore(field) <> (field match {
    case Field(name, typ, _) => "field" <> name <> ":" <+> showType(typ)
  })

  def showBackendAnnotations(annotations: Vector[BackendAnnotation]): Doc =
    "#backend" <> brackets(showList(annotations)(showBackendAnnotation)) <> line

  def showBackendAnnotation(annotation: BackendAnnotation): Doc =
    annotation.key <> parens(showList(annotation.values)(d => d))

  def showClosureSpec(spec: ClosureSpec): Doc =
    showProxy(spec.func) <> braces(ssep(spec.params.map(p => p._1.toString <> colon <> showExpr(p._2)).toSeq, comma <> space))

  def showDomainDefinition(n: DomainDefinition): Doc = updatePositionStore(n) <> (
    n.name <+> block(ssep(n.funcs map showDomainFunc, line) <> ssep(n.axioms map showDomainAxiom, line))
  )

  def showDomainFunc(func: DomainFunc): Doc = updatePositionStore(func) <> (func match {
    case DomainFunc(name, args, res) =>  "func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVar(res))
  })

  def showDomainAxiom(ax: DomainAxiom): Doc = updatePositionStore(ax) <> (ax match {
    case DomainAxiom(expr) =>  "axiom" <+> block(showExpr(expr))
  })

  def showAdtDefinition(n: AdtDefinition): Doc = updatePositionStore(n) <> (
    n.name <+> block(ssep(n.clauses map showAdtClause, line))
    )

  def showAdtClause(clause: AdtClause): Doc = updatePositionStore(clause) <> (clause match {
    case AdtClause(name, args) => name.name <+> block(ssep(args map showField, line))
  })

  def showTypeDecl(t: DefinedT): Doc =
    "type" <+> t.name <+> "..." <> line

  def showPreconditions[T <: Assertion](list: Vector[T]): Doc =
    hcat(list  map ("requires " <> showAss(_) <> line))

  def showPostconditions[T <: Assertion](list: Vector[T]): Doc =
    hcat(list  map ("ensures " <> showAss(_) <> line))

  def showTerminationMeasures(list: Vector[TerminationMeasure]): Doc =
    hcat(list  map ("decreases " <> showTerminationMeasure(_) <> line))

  def showCaptured(captured: Vector[(Expr, Parameter.In)]): Doc =
    angles(showList(captured) {
      case (e, p) => showVar(p) <+> ":=" <+> showExpr(e)
    })

  def showFormalArgList[T <: Parameter](list: Vector[T]): Doc =
    showVarDeclList(list)

  // statements

  def showStmt(s: Stmt): Doc = updatePositionStore(s) <> (s match {
    case s: MethodBody =>
      "decl" <+> showBlockDeclList(s.decls) <> line <>
        showStmtList(s.seqn.stmts) <> line <>
        showStmtList(s.postprocessing)
    case s: MethodBodySeqn => showStmtList(s.stmts)
    case Block(decls, stmts) => "decl" <+> showBlockDeclList(decls) <> line <> showStmtList(stmts)
    case Seqn(stmts) => ssep(stmts map showStmt, line)
    case Label(label) => showProxy(label)
    case If(cond, thn, els) => "if" <> parens(showExpr(cond)) <+> block(showStmt(thn)) <+> "else" <+> block(showStmt(els))
    case While(cond, invs, measure, body) => "while" <> parens(showExpr(cond)) <> line <>
      hcat(invs  map ("invariant " <> showAss(_) <> line)) <>
      opt(measure)("decreases" <> showTerminationMeasure(_) <> line) <>
      block(showStmt(body))

    case New(target, expr) => showVar(target) <+> "=" <+> "new" <> parens(showExpr(expr))

    case NewSliceLit(target, typ, elems) =>
      val typP = showType(typ)
      val exprsP = braces(space <> showIndexedExprMap(elems) <> (if (elems.nonEmpty) space else emptyDoc))
      showVar(target) <+> "=" <+> "new" <> parens(brackets(emptyDoc) <> typP <+> exprsP)

    case lit@NewMapLit(target, _, _, entries) =>
      val entriesDoc = showList(entries) { case (x, y) => showExpr(x) <> ":" <+> showExpr(y) }
      showVar(target) <+> "=" <+> "new" <> (showType(lit.typ) <+> braces(space <> entriesDoc <> (if (entries.nonEmpty) space else emptyDoc)))

    case MakeSlice(target, typeParam, lenArg, capArg) => showVar(target) <+> "=" <+> "make" <>
      parens(showType(typeParam) <> comma <+> showExprList(lenArg +: capArg.toVector))

    case MakeChannel(target, typeParam, bufferSizeArg, _, _) => showVar(target) <+> "=" <+> "make" <>
      parens(showType(typeParam) <> opt(bufferSizeArg)(comma <+> showExpr(_)))

    case MakeMap(target, typeParam, initialSpaceArg) =>
      showVar(target) <+> "=" <+> "make" <> parens(showType(typeParam) <> opt(initialSpaceArg)(comma <+> showExpr(_)))

    case EffectfulConversion(target, newType, expr) =>
      showVar(target) <+> "=" <+> showType(newType) <> parens(showExpr(expr))

    case SafeTypeAssertion(resTarget, successTarget, expr, typ) =>
      showVar(resTarget) <> "," <+> showVar(successTarget) <+> "=" <+> showExpr(expr) <> "." <> parens(showType(typ))

    case Initialization(left) => "init" <+> showVar(left)
    case SingleAss(left, right) => showAssignee(left) <+> "=" <+> showExpr(right)

    case FunctionCall(targets, func, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <> func.name <> parens(showExprList(args))

    case MethodCall(targets, recv, meth, args) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <> showExpr(recv) <> meth.name <> parens(showExprList(args))

    case ClosureCall(targets, closure, args, spec) =>
      (if (targets.nonEmpty) showVarList(targets) <+> "=" <> space else emptyDoc) <>
        showExpr(closure) <> parens(showExprList(args)) <+> "as" <+> showClosureSpec(spec)

    case SpecImplementationProof(closure, spec, body, _, _) =>
      "proof" <+> showExpr(closure) <+> "implements" <+> show(spec) <+> block(showStmt(body))

    case GoFunctionCall(func, args) => "go" <+> func.name <> parens(showExprList(args))

    case GoMethodCall(recv, meth, args) =>
      "go" <+> showExpr(recv) <> "." <>  meth.name <> parens(showExprList(args))

    case GoClosureCall(closure, args, spec) =>
      "go" <+> showExpr(closure) <> parens(showExprList(args)) <+> "as" <+> showClosureSpec(spec)

    case s: Defer => "defer" <+> showStmt(s.stmt)

    case Return() => "return"
    case Assert(ass) => "assert" <+> showAss(ass)
    case s: AssertBy => s match {
      case AssertByProof(ass, block) => "assert" <+> showAss(ass) <+> "by" <+> showStmt(block)
      case AssertByContra(ass, block) => "assert" <+> showAss(ass) <+> "by" <+> "contra" <+> showStmt(block)
    }
    case Refute(ass) => "refute" <+> showAss(ass)
    case Assume(ass) => "assume" <+> showAss(ass)
    case Inhale(ass) => "inhale" <+> showAss(ass)
    case Exhale(ass) => "exhale" <+> showAss(ass)
    case Fold(acc)   => "fold" <+> showAss(acc)
    case Unfold(acc) => "unfold" <+> showAss(acc)
    case PackageWand(wand, block) => "package" <+> showAss(wand) <+> opt(block)(showStmt)
    case ApplyWand(wand) => "apply" <+> showAss(wand)
    case PatternMatchStmt(exp, cases, _) => "match" <+>
      showExpr(exp) <+> block(ssep(cases map showPatternMatchCaseStmt, line))
    case Send(channel, msg, _, _, _) => showExpr(channel) <+> "<-" <+> showExpr(msg)
    case SafeReceive(resTarget, successTarget, channel, _, _, _, _) =>
      showVar(resTarget) <> "," <+> showVar(successTarget) <+> "=" <+> "<-" <+> showExpr(channel)
    case SafeMapLookup(resTarget, successTarget, mapLookup) =>
      showVar(resTarget) <> "," <+> showVar(successTarget) <+> "=" <+> showExpr(mapLookup)
    case PredExprFold(base, args, p) => "fold" <+> "acc" <> parens(showExpr(base) <> parens(showExprList(args)) <> "," <+> showExpr(p))
    case PredExprUnfold(base, args, p) => "unfold" <+> "acc" <> parens(showExpr(base) <> parens(showExprList(args)) <> "," <+> showExpr(p))
    case Outline(_, pres, posts, measures, backendAnnotations, body, trusted) =>
      spec(showPreconditions(pres) <>
        showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <>
        "outline" <> (if (trusted) emptyDoc else parens(nest(line <> showStmt(body)) <> line))
    case Continue(l, _) => "continue" <+> opt(l)(text)
    case Break(l, _) => "break" <+> opt(l)(text)
  })

  def showProxy(x: Proxy): Doc = updatePositionStore(x) <> (x match {
    case FunctionProxy(name) => name
    case MethodProxy(name, _) => name
    case GlobalVarProxy(name, _) => name
    case p: DomainFuncProxy => p.name
    case FPredicateProxy(name) => name
    case MPredicateProxy(name, _) => name
    case FunctionLitProxy(name) => name
    case AdtClauseProxy(name, _) => name
    case l: LabelProxy => l.name
  })

  def showBlockDecl(x: BlockDeclaration): Doc = x match {
    case localVar: LocalVar => showVar(localVar) <> ":" <+> showType(localVar.typ) <> showAddressability(localVar.typ.addressability)
    case l: LabelProxy => showProxy(l)
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

  protected def showExprMap[K, V <: Expr](map : Map[K, V])(f : K => Doc) : Doc =
    showMap(map)(f, showExpr)

  protected def showIndexedExprMap[T <: Expr](map : Map[BigInt, T]) : Doc =
    showExprMap(map)(_.toString())

  def showAssignee(ass: Assignee): Doc = updatePositionStore(ass) <> (ass match {
    case Assignee.Var(v) => showVar(v)
    case Assignee.Pointer(e) => showExpr(e)
    case Assignee.Field(f) => showExpr(f)
    case Assignee.Index(e) => showExpr(e)
  })

  def showPatternMatchCaseStmt(c: PatternMatchCaseStmt): Doc = "case" <+> showMatchPattern(c.mExp) <> ":" <+> nest(showStmt(c.body))

  def showPatternMatchCaseExp(c: PatternMatchCaseExp): Doc = "case" <+> showMatchPattern(c.mExp) <> ":" <+> showExpr(c.exp)

  def showPatternMatchCaseAss(c: PatternMatchCaseAss): Doc = "case" <+> showMatchPattern(c.mExp) <> ":" <+> showAss(c.ass)

  def showMatchPattern(expr: MatchPattern): Doc = expr match {
    case MatchBindVar(name, _) => name
    case MatchAdt(clause, expr) => clause.name <+> "{" <> ssep(expr map showMatchPattern, ",") <> "}"
    case MatchValue(exp) => "`" <> showExpr(exp) <> "`"
    case MatchWildcard() => "_"
  }

  // assertions
  def showAss(a: Assertion): Doc = updatePositionStore(a) <> (a match {
    case SepAnd(left, right) => showAss(left) <+> "&&" <+> showAss(right)
    case ExprAssertion(exp) => showExpr(exp)
    case Let(left, right, exp) =>
      "let" <+> showVar(left) <+> "==" <+> parens(showExpr(right)) <+> "in" <+> showAss(exp)
    case PatternMatchAss(exp, cases, default) => "match" <+> showExpr(exp) <+>
      block(ssep(cases map showPatternMatchCaseAss, line) <> (if (default.isDefined) line <> "default:" <+> showAss(default.get) else ""))

    case MagicWand(left, right) => showAss(left) <+> "--*" <+> showAss(right)
    case Implication(left, right) => showExpr(left) <+> "==>" <+> showAss(right)
    case Access(e, FullPerm(_)) => "acc" <> parens(showAcc(e))
    case Access(e, p) => "acc" <> parens(showAcc(e) <> "," <+> showExpr(p))
    case SepForall(vars, triggers, body) =>
      "forall" <+> showVarDeclList(vars) <+> "::" <+> showTriggers(triggers) <+> showAss(body)
    case t: TerminationMeasure => showTerminationMeasure(t)
  })

  def showAcc(acc: Accessible): Doc = updatePositionStore(acc) <> (acc match {
    case Accessible.Address(der) => showExpr(der)
    case Accessible.Predicate(op) => showPredicateAcc(op)
    case Accessible.ExprAccess(op) => showExpr(op)
    case Accessible.PredExpr(PredExprInstance(base, args)) => showExpr(base) <> parens(showExprList(args))
  })

  def showPredicateAcc(access: PredicateAccess): Doc = access match {
    case FPredicateAccess(pred, args) => pred.name <> parens(showExprList(args))
    case MPredicateAccess(recv, pred, args) => showExpr(recv) <> dot <> pred.name <> parens(showExprList(args))
    case MemoryPredicateAccess(arg) => "memory" <> parens(showExpr(arg))
  }

  def showTriggerExpr(expr: TriggerExpr): Doc = expr match {
    case Accessible.Predicate(op) => showPredicateAcc(op)
    case e: Expr => showExpr(e)
  }
  def showTrigger(trigger: Trigger) : Doc = showList(trigger.exprs)(showTriggerExpr)
  def showTriggers(triggers: Vector[Trigger]) : Doc = "{" <+> showList(triggers)(showTrigger) <+> "}"

  // expressions

  def showExpr(e: Expr): Doc = updatePositionStore(e) <> (e match {
    case Unfolding(acc, exp) => "unfolding" <+> showAss(acc) <+> "in" <+> showExpr(exp)

    case PureLet(left, right, exp) =>
      "let" <+> showVar(left) <+> "==" <+> parens(showExpr(right)) <+> "in" <+> showExpr(exp)

    case Old(op) => "old" <> parens(showExpr(op))

    case LabeledOld(label, operand) => "old" <> brackets(showProxy(label)) <> parens(showExpr(operand))

    case Conditional(cond, thn, els, _) => showExpr(cond) <> "?" <> showExpr(thn) <> ":" <> showExpr(els)

    case PureForall(vars, triggers, body) =>
      "forall" <+> showVarDeclList(vars) <+> "::" <+> showTriggers(triggers) <+> showExpr(body)

    case Exists(vars, triggers, body) =>
      "exists" <+>  showVarDeclList(vars) <+> "::" <+> showTriggers(triggers) <+> showExpr(body)

    case _: FullPerm => "write"
    case _: NoPerm => "none"
    case FractionalPerm(left, right) => showExpr(left) <> "/" <> showExpr(right)
    case _: WildcardPerm => "_"
    case c: CurrentPerm => "perm" <> parens(showAcc(c.acc))
    case PermMinus(exp) => "-" <> showExpr(exp)

    case PureFunctionCall(func, args, _, reveal) =>
      val revealDoc: Doc = if (reveal) "reveal" else emptyDoc
      revealDoc <+> func.name <> parens(showExprList(args))

    case PureMethodCall(recv, meth, args, _, reveal) =>
      val revealDoc: Doc = if (reveal) "reveal " else emptyDoc
      revealDoc <> showExpr(recv) <> dot <> meth.name <> parens(showExprList(args))

    case PureClosureCall(closure, args, spec, _) => showExpr(closure) <> parens(showExprList(args)) <+> "as" <+> showClosureSpec(spec)

    case DomainFunctionCall(func, args, _) =>
      func.name <> parens(showExprList(args))

    case ClosureObject(func, _) => func.name
    case FunctionObject(func, _) => func.name
    case MethodObject(recv, meth, _) => showExpr(recv) <> dot <> meth.name

    case ClosureImplements(closure, spec) => showExpr(closure) <+> "implements" <+> showClosureSpec(spec)

    case IndexedExp(base, index, _) => showExpr(base) <> brackets(showExpr(index))
    case ArrayUpdate(base, left, right) => showExpr(base) <> brackets(showExpr(left) <+> "=" <+> showExpr(right))
    case Length(exp) => "len" <> parens(showExpr(exp))
    case Capacity(exp) => "cap" <> parens(showExpr(exp))
    case RangeSequence(low, high) =>
      "seq" <> brackets(showExpr(low) <+> ".." <+> showExpr(high))
    case GhostCollectionUpdate(seq, left, right, _) =>
      showExpr(seq) <> brackets(showExpr(left) <+> "=" <+> showExpr(right))
    case SequenceDrop(left, right) => showExpr(left) <> brackets(showExpr(right) <> colon)
    case SequenceTake(left, right) => showExpr(left) <> brackets(colon <> showExpr(right))
    case SequenceConversion(exp) => "seq" <> parens(showExpr(exp))
    case SetConversion(exp) => "set" <> parens(showExpr(exp))
    case MultisetConversion(exp) => "mset" <> parens(showExpr(exp))
    case MapKeys(exp, _) => "domain" <> parens(showExpr(exp))
    case MapValues(exp, _) => "range" <> parens(showExpr(exp))
    case MapConversion(exp) => "dict" <> parens(showExpr(exp))
    case Conversion(typ, exp) => showType(typ) <> parens(showExpr(exp))
    case Receive(channel, _, _, _) => "<-" <+> showExpr(channel)

    case OptionNone(t) => "none" <> brackets(showType(t))
    case OptionSome(exp) => "some" <> parens(showExpr(exp))
    case OptionGet(exp) => "get" <> parens(showExpr(exp))

    case AdtDiscriminator(base, clause) => showExpr(base) <> "." <> showProxy(clause)
    case AdtDestructor(base, field) => showExpr(base) <> "." <> showField(field)
    case PatternMatchExp(exp, _, cases, default) => "match" <+> showExpr(exp) <+>
      block(ssep(cases map showPatternMatchCaseExp, line) <> (if (default.isDefined) line <> "default:" <+> showExpr(default.get) else ""))

    case Slice(exp, low, high, max, _) => {
      val maxD = max.map(e => ":" <> showExpr(e)).getOrElse(emptyDoc)
      showExpr(exp) <> brackets(showExpr(low) <> ":" <> showExpr(high) <> maxD)
    }

    case TypeAssertion(exp, arg) => showExpr(exp) <> "." <> parens(showType(arg))
    case TypeOf(exp) => "typeOf" <> parens(showExpr(exp))
    case ToInterface(exp, _) => "toInterface" <> parens(showExpr(exp))
    case IsBehaviouralSubtype(left, right) => showExpr(left) <+> "<:" <+> showExpr(right)
    case IsComparableType(exp) => "isComparableType" <> parens(showExpr(exp))
    case IsComparableInterface(exp) => "isComparableInterface" <> parens(showExpr(exp))

    case PredicateConstructor(pred, _, args) =>
      showProxy(pred) <> braces(showList(args){
        case Some(e) => showExpr(e)
        case None => "_"
      })

    case BoolTExpr() => "bool"
    case StringTExpr() => "string"
    case IntTExpr(kind) => kind.name
    case Float32TExpr() => "float32"
    case Float64TExpr() => "float64"
    case PermTExpr() => "perm"
    case PointerTExpr(elem) => "*" <> showExpr(elem)
    case StructTExpr(fs) => "struct" <> braces(showList(fs)(f => f._1 <> ":" <+> showExpr(f._2)))
    case ArrayTExpr(len, elem) => brackets(showExpr(len)) <> showExpr(elem)
    case SliceTExpr(elem) => brackets(emptyDoc) <> showExpr(elem)
    case MapTExpr(key, elem) => "map" <> brackets(showExpr(key) <> comma <+> showExpr(elem))
    case SequenceTExpr(elem) => "seq" <> brackets(showExpr(elem))
    case SetTExpr(elem) => "set" <> brackets(showExpr(elem))
    case MultisetTExpr(elem) => "mset" <> brackets(showExpr(elem))
    case MathMapTExpr(key, elem) => "dict" <> brackets(showExpr(key) <> comma <+> showExpr(elem))
    case OptionTExpr(elem) => "option" <> brackets(showExpr(elem))
    case TupleTExpr(elem) => parens(showExprList(elem))
    case DefinedTExpr(name) => name

    case Low(exp) => "low" <> parens(showExpr(exp))
    case LowContext() => "low_context"
    case Rel(exp, lit) => "rel" <> parens(showExpr(exp) <> "," <+> showExpr(lit))

    case DfltVal(typ) => "dflt" <> brackets(showType(typ))
    case Tuple(args) => parens(showExprList(args))
    case Deref(exp, _) => "*" <> showExpr(exp)
    case Ref(ref, _) => "&" <> showAddressable(ref)
    case FieldRef(recv, field) => showExpr(recv) <> "."  <> field.name
    case StructUpdate(base, field, newVal) => showExpr(base) <> brackets(showField(field) <+> ":=" <+> showExpr(newVal))
    case Negation(op) => "!" <> showExpr(op)
    case BitNeg(op) => "^" <> showExpr(op)
    case BinaryExpr(left, op, right, _) => parens(showExpr(left) <+> op <+> showExpr(right))
    case lit: Lit => showLit(lit)
    case v: Var => showVar(v)
  })

  def showAddressable(a: Addressable): Doc = showExpr(a.op)

  // literals

  private def showGhostCollectionLiteral(front : String, typ : Type, exprs : Vector[Expr]) : Doc =
    front <> brackets(showType(typ)) <+>
      braces(space <> showExprList(exprs) <> (if (exprs.nonEmpty) space else emptyDoc))

  def showLit(l: Lit): Doc = l match {
    case IntLit(lit, _, base) =>
      val prefix = base match {
        case Binary => "0b"
        case Octal => "0o"
        case Decimal => ""
        case Hexadecimal => "0x"
      }
      prefix + lit.toString(base.base)
    case StringLit(s) => "\"" <> s <> "\""
    case PermLit(a, b) => "perm" <> parens(a.toString() <> "/" <> b.toString())
    case BoolLit(b) => if (b) "true" else "false"
    case NilLit(t) => parens("nil" <> ":" <> showType(t))

    case FunctionLit(name, args, captured, results, pres, posts, measures, backendAnnotations, body) =>
      "func" <+> showProxy(name) <> showCaptured(captured) <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <>
          showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <>
        opt(body)(b => block(showStmt(b)))

    case PureFunctionLit(name, args, captured, results, pres, posts, measures, backendAnnotations, body) =>
      "pure func" <+> showProxy(name)  <> showCaptured(captured) <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations)) <>
        opt(body)(b => block("return" <+> showExpr(b)))

    case ArrayLit(len, typ, elems) => {
      val lenP = brackets(len.toString)
      val typP = showType(typ)
      val exprsP = braces(space <> showIndexedExprMap(elems) <> (if (elems.nonEmpty) space else emptyDoc))
      lenP <> typP <+> exprsP
    }

    case SequenceLit(_, typ, elems) => {
      val exprsP = braces(space <> showIndexedExprMap(elems) <> (if (elems.nonEmpty) space else emptyDoc))
      "seq" <> brackets(showType(typ)) <+> exprsP
    }

    case StructLit(t, args) => showType(t) <> braces(showExprList(args))
    case SetLit(typ, exprs) => showGhostCollectionLiteral("set", typ, exprs)
    case MultisetLit(typ, exprs) => showGhostCollectionLiteral("mset", typ, exprs)
    case lit@MathMapLit(_, _, entries) =>
      val entriesDoc = showList(entries){ case (x,y) => showExpr(x) <> ":" <+> showExpr(y) }
      showType(lit.typ) <+> braces(space <> entriesDoc <> (if (entries.nonEmpty) space else emptyDoc))

    case lit: AdtConstructorLit => lit.clause.name <> braces(showExprList(lit.args))
  }

  // variables

  def showVar(v: Var): Doc = v match {
    case BoundVar(id, _) => id
    case Parameter.In(id, _)    => id
    case Parameter.Out(id, _)    => id
    case LocalVar(id, _) => id
    case GlobalConst.Val(id, _) => id
    case GlobalVar(proxy, _) => proxy.name
  }

  def showVarDecl(v: Var): Doc = v match {
    case BoundVar(id, t) => id <> ":" <+> showType(t)
    case Parameter.In(id, t)    => id <> ":" <+> showType(t)
    case Parameter.Out(id, t)    => id <> ":" <+> showType(t)
    case LocalVar(id, t) => id <> ":" <+> showType(t)
    case GlobalConst.Val(id, t) => id <> ":" <+> showType(t)
    case GlobalVar(proxy, t) => proxy.name <> ":" <+> showType(t)
  }

  // types

  def showType(typ : Type) : Doc = typ match {
    case t: PrettyType => t.toString
  }

  def showList[T](list: Seq[T])(f: T => Doc): Doc = ssep(list map f, comma <> space)

  def showMap[K, V](map : Map[K, V])(f : K => Doc, g : V => Doc) : Doc =
    ssep(map.map { case (k, v) => f(k) <> ":" <> g(v) }.toVector, comma <> space)
}

class ShortPrettyPrinter extends DefaultPrettyPrinter {

  override val defaultIndent = 2
  override val defaultWidth  = 80

  override def format(node: Node): String =
    pretty(show(node)).layout


  override def showFunction(f: Function): Doc = f match {
    case Function(name, args, results, pres, posts, measures, backendAnnotations, _) =>
      "func" <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <>
          showPostconditions(posts) <> showTerminationMeasures(measures) <> showBackendAnnotations(backendAnnotations))
  }

  override def showPureFunction(f: PureFunction): Doc = f match {
    case PureFunction(name, args, results, pres, posts, measures, backendAnnotations, _, isOpaque) =>
    val funcPrefix = if (isOpaque) "pure opaque func" else "pure func"
      funcPrefix <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <>
        showBackendAnnotations(backendAnnotations))
  }

  override def showMethod(m: Method): Doc = m match {
    case Method(receiver, name, args, results, pres, posts, measures, backendAnnotations, _) =>
      "func" <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <>
          showBackendAnnotations(backendAnnotations))
  }

  override def showPureMethod(m: PureMethod): Doc = m match {
    case PureMethod(receiver, name, args, results, pres, posts, measures, backendAnnotations, _, isOpaque) =>
      val funcPrefix = if (isOpaque) "pure opaque func" else "pure func"
      funcPrefix <+> parens(showVarDecl(receiver)) <+> name.name <> parens(showFormalArgList(args)) <+> parens(showVarDeclList(results)) <>
        spec(showPreconditions(pres) <> showPostconditions(posts) <> showTerminationMeasures(measures) <>
          showBackendAnnotations(backendAnnotations))
  }

  override def showFPredicate(predicate: FPredicate): Doc = predicate match {
    case FPredicate(name, args, _) =>
      "pred" <+> name.name <> parens(showFormalArgList(args))
  }

  override def showMPredicate(predicate: MPredicate): Doc = predicate match {
    case MPredicate(recv, name, args, _) =>
      "pred" <+> parens(showVarDecl(recv)) <+> name.name <> parens(showFormalArgList(args))
  }

  // statements

  override def showStmt(s: Stmt): Doc = s match {
    case s: MethodBody => "decl" <+> showBlockDeclList(s.decls)
    case _: MethodBodySeqn => emptyDoc
    case Block(decls, _) => "decl" <+> showBlockDeclList(decls)
    case Seqn(_) => emptyDoc
    case Label(label) => showProxy(label)
    case If(cond, _, _) => "if" <> parens(showExpr(cond)) <+> "{...}" <+> "else" <+> "{...}"
    case While(cond, invs, measure, _) => "while" <> parens(showExpr(cond)) <> line <>
      hcat(invs  map ("invariant " <> showAss(_) <> line)) <>
      opt(measure)("decreases" <> showTerminationMeasure(_) <> line)

    case New(target, expr) => showVar(target) <+> "=" <+> "new" <> parens(showExpr(expr))

    case NewSliceLit(target, typ, elems) =>
      val typP = showType(typ)
      val exprsP = braces(space <> showIndexedExprMap(elems) <> (if (elems.nonEmpty) space else emptyDoc))
      showVar(target) <+> "=" <+> "new" <> parens(brackets(emptyDoc) <> typP <+> exprsP)

    case lit@NewMapLit(target, _, _, entries) =>
      val entriesDoc = showList(entries) { case (x, y) => showExpr(x) <> ":" <+> showExpr(y) }
      showVar(target) <+> "=" <+> "new" <> (showType(lit.typ) <+> braces(space <> entriesDoc <> (if (entries.nonEmpty) space else emptyDoc)))

    case MakeSlice(target, typeParam, lenArg, capArg) => showVar(target) <+> "=" <+> "make" <>
      parens(showType(typeParam) <> comma <+> showExprList(lenArg +: capArg.toVector))

    case MakeChannel(target, typeParam, bufferSizeArg, _, _) => showVar(target) <+> "=" <+> "make" <>
      parens(showType(typeParam) <> opt(bufferSizeArg)(comma <+> showExpr(_)))

    case MakeMap(target, typeParam, initialSpaceArg) =>
      showVar(target) <+> "=" <+> "make" <> parens(showType(typeParam) <> opt(initialSpaceArg)(comma <+> showExpr(_)))

    case EffectfulConversion(target, newType, expr) =>
      showVar(target) <+> "=" <+> showType(newType) <> parens(showExpr(expr))

    case SafeTypeAssertion(resTarget, successTarget, expr, typ) =>
      showVar(resTarget) <> "," <+> showVar(successTarget) <+> "=" <+> showExpr(expr) <> "." <> parens(showType(typ))

    case SafeMapLookup(resTarget, successTarget, expr) =>
      showVar(resTarget) <> "," <+> showVar(successTarget) <+> "=" <+> showExpr(expr)

    case Initialization(left) => "init" <+> showVar(left)
    case SingleAss(left, right) => showAssignee(left) <+> "=" <+> showExpr(right)

    case _: FunctionCall | _: MethodCall | _: ClosureCall => super.showStmt(s)

    case SpecImplementationProof(closure, spec, _, _, _) =>
      "proof" <+> showExpr(closure) <+> "implements" <+> show(spec)

    case GoFunctionCall(func, args) =>
      "go" <+> func.name <> parens(showExprList(args))

    case GoMethodCall(recv, meth, args) =>
      "go" <+> showExpr(recv) <> "." <> meth.name <> parens(showExprList(args))

    case GoClosureCall(closure, args, spec) =>
      "go" <+> showExpr(closure) <> parens(showExprList(args)) <+> "as" <+> showClosureSpec(spec)

    case s: Defer => "defer" <+> showStmt(s.stmt)

    case Return() => "return"
    case Assert(ass) => "assert" <+> showAss(ass)
    case s: AssertBy => s match {
      case AssertByProof(ass, _) => "assert" <+> showAss(ass) <+> "by" <+> "{...}"
      case AssertByContra(ass, _) => "assert" <+> showAss(ass) <+> "by" <+> "contra" <+> "{...}"
    }
    case Refute(ass) => "refute" <+> showAss(ass)
    case Assume(ass) => "assume" <+> showAss(ass)
    case Inhale(ass) => "inhale" <+> showAss(ass)
    case Exhale(ass) => "exhale" <+> showAss(ass)
    case Fold(acc)   => "fold" <+> showAss(acc)
    case Unfold(acc) => "unfold" <+> showAss(acc)
    case PackageWand(wand, _) => "package" <+> showAss(wand)
    case ApplyWand(wand) => "apply" <+> showAss(wand)
    case PatternMatchStmt(exp, cases, strict) => (if (strict) "!" else "") <> "match" <+>
      showExpr(exp) <+> block(ssep(cases map showPatternMatchCaseStmt, line))
    case Send(channel, msg, _, _, _) => showExpr(channel) <+> "<-" <+> showExpr(msg)
    case SafeReceive(resTarget, successTarget, channel, _, _, _, _) =>
      showVar(resTarget) <> "," <+> showVar(successTarget) <+> "=" <+> "<-" <+> showExpr(channel)
    case PredExprFold(base, args, p) => "fold" <+> "acc" <> parens(showExpr(base) <> parens(showExprList(args)) <> "," <+> showExpr(p))
    case PredExprUnfold(base, args, p) => "unfold" <+> "acc" <> parens(showExpr(base) <> parens(showExprList(args)) <> "," <+> showExpr(p))
    case Continue(l, _) => "continue" <+> opt(l)(text)
    case Break(l, _) => "break" <+> opt(l)(text)
    case Outline(_, pres, posts, measures, backendAnnotations, _, _) =>
      spec(showPreconditions(pres) <>
        showPostconditions(posts) <> showTerminationMeasures(measures)) <>
        showBackendAnnotations(backendAnnotations) <>
        "outline"
  }
}
