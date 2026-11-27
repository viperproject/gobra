// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.hyperfuncs.HyperfuncsAst.isExecType
import viper.silver.ast._
import viper.silver.plugin.sif.{SIFLowEventExp, SIFLowExp, SIFRelExp}
import viper.silver.verifier.{AbstractError, ConsistencyError}

/**
  * The static rules of the hyperfunction language (hyperfuncs paper, Section "Static Rules"; docs/language.md §5), as
  * implemented in Gobra, checked on the Viper AST after type checking:
  *
  *  - Relational constructs (`low`, `rel`, `active`, `lowEvent`, execution quantifiers, and calls of hyperfunctions)
  *    may appear only in hyperfunctions, in method pre- and postconditions, loop invariants, `assert`, `assume`,
  *    `inhale` and `exhale` statements, and in predicate bodies (relational predicates). In particular, ordinary
  *    functions do not call hyperfunctions, relational constructs are not allowed in domains, in statements that
  *    compute values (assignments, calls, `fold`, ...), or in the conditions of `if` and `while`.
  *  - The argument of `low` and `rel` is unary: it contains no relational construct.
  *  - Executions are identified by the literals 0 and 1 or by values of type `Exec`, which is allowed only for
  *    parameters of hyperfunctions and for variables bound by execution quantifiers; the execution of `rel` and
  *    `active` and the arguments for `Exec` parameters are literals or such variables.
  *  - The body of a hyperfunction with a non-boolean result must be rigid, i.e., have one value for the tuple of
  *    executions; a unary expression must be projected with `rel(e, i)`. Boolean bodies are lifted.
  *  - `lowEvent` is a statement-level assertion and is not allowed in hyperfunctions.
  *  - Triggers do not contain relational constructs.
  */
class HyperfuncsChecks(p: Program, hyper: Set[String], report: AbstractError => Unit) {

  private def error(msg: String, pos: Position): Unit = report(ConsistencyError(msg, pos))

  def isRelationalNode(n: Node): Boolean = n match {
    case _: SIFLowExp | _: SIFLowEventExp | _: SIFRelExp | _: HFRelExp | _: HFActiveExp => true
    case f: FuncApp => hyper(f.funcname)
    case q: QuantifiedExp => q.variables.exists(v => isExecType(v.typ))
    case _ => false
  }

  def describe(n: Node): String = n match {
    case _: SIFLowExp => "low"
    case _: SIFLowEventExp => "lowEvent"
    case _: SIFRelExp | _: HFRelExp => "rel"
    case _: HFActiveExp => "active"
    case f: FuncApp => s"a call of the hyperfunction ${f.funcname}"
    case _: QuantifiedExp => s"a quantifier over executions"
    case _ => n.toString
  }

  private def firstRelational(n: Node): Option[Node] =
    n.deepCollect { case x if isRelationalNode(x) => x }.headOption

  private def positioned(n: Node, default: Position): Position = n match {
    case p: Positioned if p.pos != NoPosition => p.pos
    case _ => default
  }

  /** Reports the first relational construct in `n`, if any. */
  private def noRelational(n: Node, where: String, default: Position): Unit =
    firstRelational(n).foreach(r =>
      error(s"${describe(r).capitalize} is not allowed $where. Relational constructs are allowed only in hyperfunctions, " +
        "method specifications, loop invariants, assert, assume, inhale and exhale statements, and predicate bodies.",
        positioned(r, default)))

  private def mentionsExec(t: Type): Boolean = t match {
    case _ if isExecType(t) => true
    case c: CollectionType => mentionsExec(c.elementType)
    case m: MapType => mentionsExec(m.keyType) || mentionsExec(m.valueType)
    case d: DomainType => d.typVarsMap.values.exists(mentionsExec)
    case _ => false
  }

  private def checkNoExecType(t: Type, what: String, pos: Position): Unit =
    if (mentionsExec(t))
      error(s"The type ${HyperfuncsAst.ExecTypeName} is allowed only for parameters of hyperfunctions and variables of " +
        s"quantifiers over executions, not for $what.", pos)

  def check(): Unit = {
    // Exec is a type of execution identifiers, introduced by the plugin
    p.domains.find(_.name == HyperfuncsAst.ExecTypeName).foreach { d =>
      if (d.functions.nonEmpty || d.axioms.nonEmpty || d.typVars.nonEmpty)
        error(s"The domain ${HyperfuncsAst.ExecTypeName} is reserved for execution identifiers.", d.pos)
    }

    for (f <- p.fields) checkNoExecType(f.typ, s"the field ${f.name}", f.pos)
    for (d <- p.domains; f <- d.functions) {
      (f.formalArgs.map(_.typ) :+ f.typ).foreach(t => checkNoExecType(t, s"the domain function ${f.name}", f.pos))
      noRelational(f, s"in the domain function ${f.name}", f.pos)
    }
    for (d <- p.domains; a <- d.axioms) noRelational(a.exp, "in domain axioms", a.pos)

    for (f <- p.functions) {
      if (hyper(f.name)) checkHyperfunction(f)
      else {
        (f.formalArgs.map(_.typ) :+ f.typ).foreach(t => checkNoExecType(t, s"the function ${f.name}", f.pos))
        (f.pres ++ f.posts ++ f.body.toSeq).foreach(e => noRelational(e, s"in the function ${f.name} (functions cannot use relational assertions or call hyperfunctions; declare it as a hyperfunction)", f.pos))
      }
    }

    for (pr <- p.predicates) {
      pr.formalArgs.foreach(a => checkNoExecType(a.typ, s"the predicate ${pr.name}", a.pos))
      pr.body.foreach(b => checkLowEvent(b, s"in the predicate ${pr.name}"))
    }

    for (m <- p.methods) {
      (m.formalArgs ++ m.formalReturns).foreach(a => checkNoExecType(a.typ, s"parameters of the method ${m.name}", a.pos))
      m.body.foreach(checkStmt)
    }

    // everywhere: the arguments of low and rel, the executions, triggers, and the variables of type Exec
    p.visit {
      case l@SIFLowExp(e, _, _) => unaryArgument(e, "low", l.pos)
      case r@SIFRelExp(e, _) => unaryArgument(e, "rel", r.pos)
      case r@HFRelExp(e, idx) => unaryArgument(e, "rel", r.pos); executionIdentifier(idx, "rel")
      case HFActiveExp(idx) => executionIdentifier(idx, "active"); ()
      case f: FuncApp if hyper(f.funcname) =>
        p.findFunctionOptionally(f.funcname).foreach(h => h.formalArgs.zip(f.args).foreach {
          case (param, arg) if isExecType(param.typ) => executionIdentifier(arg, s"the argument for ${param.name}")
          case _ =>
        })
      case q: QuantifiedExp =>
        q.variables.filterNot(v => isExecType(v.typ)).foreach(v => checkNoExecType(v.typ, s"the variable ${v.name}", v.pos))
        q match {
          case Forall(_, triggers, _) => triggers.foreach(t => noRelational(t, "in triggers", q.pos))
          case Exists(_, triggers, _) => triggers.foreach(t => noRelational(t, "in triggers", q.pos))
          case _ =>
        }
      case l: Let => checkNoExecType(l.variable.typ, s"the variable ${l.variable.name}", l.pos)
    }
  }

  private def unaryArgument(e: Exp, what: String, pos: Position): Unit =
    firstRelational(e).foreach(r =>
      error(s"The argument of $what must be a unary expression, but contains ${describe(r)}.", positioned(r, pos)))

  /** An execution: a literal or a variable of type Exec (a hyperfunction parameter or a quantified variable). */
  private def executionIdentifier(e: Exp, what: String): Unit = e match {
    case _: HFExecLit | _: IntLit => // range checked by the type checker
    case v: LocalVar if isExecType(v.typ) =>
    case _ => error(s"The execution of $what must be a literal or a variable of type ${HyperfuncsAst.ExecTypeName}.", e.pos)
  }

  private def checkLowEvent(e: Node, where: String): Unit =
    e.deepCollect { case l: SIFLowEventExp => l }.headOption.foreach(l =>
      error(s"lowEvent is a statement-level assertion and is not allowed $where.", l.pos))

  private def checkStmt(s: Stmt): Unit = s match {
    case Seqn(ss, decls) =>
      decls.foreach {
        case d: LocalVarDecl => checkNoExecType(d.typ, s"the local variable ${d.name}", d.pos)
        case _ =>
      }
      ss.foreach(checkStmt)
    case _: Assert | _: Assume | _: Inhale | _: Exhale => // relational assertions are allowed
    case _: Label => // invariants of labels are assertions
    case w: While =>
      noRelational(w.cond, "in loop conditions", w.pos)
      checkStmt(w.body)
    case i: If =>
      noRelational(i.cond, "in conditions of if statements", i.pos)
      checkStmt(i.thn)
      checkStmt(i.els)
    case d: LocalVarDeclStmt => checkNoExecType(d.decl.typ, s"the local variable ${d.decl.name}", d.pos)
    case other => noRelational(other, "in this statement", other.pos)
  }

  private def checkHyperfunction(f: Function): Unit = {
    if (mentionsExec(f.typ))
      error(s"The result of the hyperfunction ${f.name} cannot have type ${HyperfuncsAst.ExecTypeName}.", f.pos)
    f.formalArgs.filterNot(a => isExecType(a.typ)).foreach(a => checkNoExecType(a.typ, s"the parameter ${a.name}", a.pos))
    (f.pres ++ f.posts ++ f.body.toSeq).foreach(e => checkLowEvent(e, s"in the hyperfunction ${f.name}"))
    f.body.foreach { b =>
      if (b.typ != Bool && !isRigid(b, executionParameters(f)))
        error(s"The body of the hyperfunction ${f.name} has type ${b.typ} and must have one value for all executions; " +
          "use rel(e, i) for the value of a unary expression e in execution i.", b.pos)
    }
  }

  /** The execution parameters: the only parameters with one value for all executions. */
  def executionParameters(f: Function): Set[String] =
    f.formalArgs.filter(a => isExecType(a.typ)).map(_.name).toSet

  private def isHeapDependentFunction(name: String): Boolean =
    p.findFunctionOptionally(name).exists(f => f.pres.exists(_.isHeapDependent(p)))

  /** The multiplicity rules: is `e` rigid, i.e., does it have one value for the tuple of executions? */
  def isRigid(e: Exp, rigid: Set[String]): Boolean = e match {
    case _: Literal | _: Result => true
    case _: SIFRelExp | _: HFRelExp | _: SIFLowExp | _: HFActiveExp | _: HFExecLit => true
    case f: FuncApp if hyper(f.funcname) => true
    case v: LocalVar => rigid(v.name)
    case f: FuncApp => !isHeapDependentFunction(f.funcname) && f.args.forall(isRigid(_, rigid))
    case _: FieldAccess | _: LocationAccess | _: AccessPredicate | _: Old | _: LabelledOld | _: CurrentPerm => false
    case q: QuantifiedExp => isRigid(q.exp, rigid ++ q.variables.map(_.name))
    case Let(v, e1, body) => isRigid(body, if (isRigid(e1, rigid)) rigid + v.name else rigid - v.name)
    case u: Unfolding => isRigid(u.body, rigid)
    case e => e.subExps.forall(isRigid(_, rigid))
  }
}
