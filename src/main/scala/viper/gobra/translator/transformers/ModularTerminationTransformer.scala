// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier
import viper.gobra.translator.encodings.LevelEncoding
import viper.gobra.translator.encodings.typeless.TerminationEncoding
import viper.silver.ast.utility.Simplifier
import viper.silver.ast.utility.rewriter.Traverse
import viper.silver.ast.utility.Statements.EmptyStmt
import viper.silver.plugin.standard.termination.{DecreasesClause, DecreasesSpecification, DecreasesTuple, DecreasesWildcard, FunctionTerminationError, MethodTerminationError}
import viper.silver.plugin.standard.termination.transformation.{DecreasesCheck, ExpTransformer, ExpressionContext, NestedPredicates, ProgramManager, ReasonTrafoFactory}
import viper.silver.verifier.AbstractError
import viper.silver.verifier.errors.ErrorNode
import viper.silver.verifier.errors.AssertFailed
import viper.silver.{ast => vpr}

/**
  * Makes termination checking modular: Viper's termination plugin checks that the termination measure decreases only
  * for calls between (mutually) recursive members of the Viper program, which requires the whole program to be
  * present. Instead, this transformation checks that the measure decreases at *every* call between members whose
  * measures start with a termination level (see [[viper.gobra.ast.internal.transform.TerminationLevelsTransform]]),
  * including calls of interface methods, closure specs, and members of imported packages:
  *  - before every method call in a method body, it adds assertions that the callee's measure (at the call) is smaller
  *    than the caller's measure (in the pre-state of the caller),
  *  - for every function, it adds methods that perform the same checks for the calls in the function's precondition,
  *    postcondition, and body.
  * Calls in the bodies of closure implementation proofs are compared with the measure of the implemented spec, which
  * makes the measure of the implementation at most the measure of the spec.
  *
  * Members of imported packages have already been checked when their package was verified. Their measures are only
  * used to check the calls to them; for Viper's termination plugin, they are replaced by `decreases _`.
  * The termination plugin still checks loops and the remaining calls.
  */
class ModularTerminationTransformer extends ViperTransformer {
  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    var errors: Seq[AbstractError] = Seq.empty
    val checks = new ModularTerminationChecks(task.program, e => errors :+= e)
    val newProgram = ModularTerminationTransformer.pruneLevelEncoding(checks.transformedProgram)
    if (errors.nonEmpty) Left(errors) else Right(task.copy(program = newProgram))
  }
}

object ModularTerminationTransformer {
  /**
    * Part of the names of the methods that check the termination of functions. The checks are not relational, which
    * is why hyper-property verification only considers one execution of these methods (see
    * [[viper.gobra.translator.transformers.hyper.SIFLowGuardTransformerImpl]]).
    */
  val checkMethodMarker: String = "termination_levels"

  /**
    * Removes the parts of the encoding of termination levels that the program does not use. The measures of members
    * are encoded with levels, but many of these measures are hidden from the termination plugin and never used by
    * a check. The unused declarations and axioms (in particular, the multiset theory of atoms) would only burden the
    * SMT solver when verifying unrelated members.
    */
  def pruneLevelEncoding(p: vpr.Program): vpr.Program = {
    val levelDomainNames = Set(LevelEncoding.levelDomainName, LevelEncoding.atomDomainName, s"${LevelEncoding.levelDomainName}WellFoundedOrder")
    val (levelDomains, otherDomains) = p.domains.partition(d => LevelEncoding.isLevelDomain(d.name))
    if (levelDomains.isEmpty) return p

    def mentionsLevelType(t: vpr.Type): Boolean = t match {
      case vpr.DomainType(name, typVarsMap) => levelDomainNames.contains(name) || typVarsMap.values.exists(mentionsLevelType)
      case vpr.MultisetType(e) => mentionsLevelType(e)
      case vpr.SetType(e) => mentionsLevelType(e)
      case vpr.SeqType(e) => mentionsLevelType(e)
      case vpr.MapType(k, v) => mentionsLevelType(k) || mentionsLevelType(v)
      case _ => false
    }
    val levelFuncNames = levelDomains.flatMap(_.functions.map(_.name)).toSet
    val otherNodes: Seq[vpr.Node] = otherDomains ++ p.fields ++ p.functions ++ p.predicates ++ p.methods ++ p.extensions
    val usedLevelFuncs = otherNodes.flatMap(_.deepCollect { case app: vpr.DomainFuncApp if levelFuncNames.contains(app.funcname) => app.funcname }).toSet
    val usesLevelType = p.fields.exists(f => mentionsLevelType(f.typ)) || p.functions.exists(f => mentionsLevelType(f.typ)) ||
      otherDomains.exists(_.functions.exists(f => mentionsLevelType(f.typ) || f.formalArgs.exists(a => mentionsLevelType(a.typ)))) ||
      otherNodes.exists(_.existsDefined {
      case e: vpr.Exp if mentionsLevelType(e.typ) =>
      case d: vpr.LocalVarDecl if mentionsLevelType(d.typ) =>
    })

    if (usedLevelFuncs.isEmpty && !usesLevelType) {
      p.copy(domains = otherDomains)(p.pos, p.info, p.errT)
    } else {
      // the functions relating types and closures to their levels are only needed if they are used
      val unused = Set("$typeLevel", "$closureLevel").filterNot(usedLevelFuncs.contains)
      def mentionsUnused(n: vpr.Node): Boolean = n.existsDefined { case app: vpr.DomainFuncApp if unused.contains(app.funcname) => }
      val withoutUnusedFuncs = levelDomains.map { d =>
        d.copy(functions = d.functions.filterNot(f => unused.contains(f.name)), axioms = d.axioms.filterNot(mentionsUnused))(d.pos, d.info, d.errT)
      }
      // Atoms are registered for every encoded measure, e.g., of every imported member, and the encoding axiomatizes the
      // (transitively closed) order between all pairs of them. Only the atoms used by the program (or by the remaining
      // axioms about types and closures) are needed; facts about other atoms cannot contribute to any proof, as the facts
      // are transitively closed.
      def isAtomFunc(f: vpr.DomainFunc): Boolean = f.formalArgs.isEmpty && f.typ == LevelEncoding.atomType
      val atomFuncs = withoutUnusedFuncs.flatMap(_.functions).filter(isAtomFunc).map(_.name).toSet
      def atomsIn(n: vpr.Node): Set[String] = n.deepCollect { case app: vpr.DomainFuncApp if atomFuncs.contains(app.funcname) => app.funcname }.toSet
      def isOrderFact(e: vpr.Exp): Boolean = e match {
        case vpr.DomainFuncApp(LevelEncoding.atomLtFuncName, Seq(_: vpr.DomainFuncApp, _: vpr.DomainFuncApp), _) => true
        case vpr.Or(l, _) => isOrderFact(l)
        case _ => false
      }
      val nonOrderAxioms = withoutUnusedFuncs.flatMap(_.axioms).filterNot(a => isOrderFact(a.exp)).map(_.exp)
      val usedAtoms = otherNodes.flatMap(atomsIn).toSet ++ nonOrderAxioms.flatMap(atomsIn)
      // A fact `a < b` can only contribute to a proof if `b` can be an atom of the bigger level of some comparison: the
      // dominating atoms of the multiset order (and the middle atoms of transitive chains, which are closed already)
      // are atoms of the bigger level. These are the atoms of the bigger levels of comparisons (`$levelLt(_, r)`,
      // `decreasing(_, r)`), including the atoms of the level constants, types, and closures they mention.
      val typeAtoms = nonOrderAxioms.filter(_.existsDefined { case app: vpr.DomainFuncApp if app.funcname == "$typeLevel" => }).flatMap(atomsIn).toSet
      val closureAtoms = nonOrderAxioms.filter(_.existsDefined { case app: vpr.DomainFuncApp if app.funcname == "$closureLevel" => }).flatMap(atomsIn).toSet
      val levelFunctionBodies: Map[String, vpr.Exp] = p.functions.collect { case f if f.typ == LevelEncoding.levelType && f.body.isDefined => f.name -> f.body.get }.toMap
      def upperAtoms(e: vpr.Exp, visited: Set[String]): Set[String] = {
        val calls = e.deepCollect { case app: vpr.FuncApp if levelFunctionBodies.contains(app.funcname) && !visited.contains(app.funcname) => app.funcname }.toSet
        atomsIn(e) ++
          (if (e.existsDefined { case app: vpr.DomainFuncApp if app.funcname == "$typeLevel" => }) typeAtoms else Set.empty) ++
          (if (e.existsDefined { case app: vpr.DomainFuncApp if app.funcname == "$closureLevel" => }) closureAtoms else Set.empty) ++
          calls.flatMap(f => upperAtoms(levelFunctionBodies(f), visited ++ calls))
      }
      val biggerLevels: Seq[vpr.Exp] = otherNodes.flatMap(_.deepCollect {
        case vpr.DomainFuncApp("$levelLt", Seq(_, r), _) => r
        case app: vpr.DomainFuncApp if app.funcname == "decreasing" && app.args.size == 2 && app.args(1).typ == LevelEncoding.levelType => app.args(1)
      })
      // `$levelMax` is at least both of its arguments, so its arguments may be bigger levels as well
      val maxArgs: Seq[vpr.Exp] = otherNodes.flatMap(_.deepCollect { case vpr.DomainFuncApp("$levelMax", args, _) => args }).flatten
      val upper: Set[String] = (biggerLevels ++ maxArgs).flatMap(upperAtoms(_, Set.empty)).toSet
      def neededFact(e: vpr.Exp): Boolean = {
        val atoms = atomsIn(e)
        atoms.subsetOf(usedAtoms) && (e match {
          case vpr.DomainFuncApp(_, Seq(_, b: vpr.DomainFuncApp), _) => upper.contains(b.funcname)
          case _ => true
        })
      }
      val prunedLevelDomains = withoutUnusedFuncs.map { d =>
        d.copy(
          functions = d.functions.filter(f => !isAtomFunc(f) || usedAtoms.contains(f.name)),
          axioms = d.axioms.filter(a => !isOrderFact(a.exp) || neededFact(a.exp))
        )(d.pos, d.info, d.errT)
      }
      // keep the original order of the domains, without the domains of order facts that became empty
      val pruned = prunedLevelDomains.map(d => d.name -> d).toMap
      val domains = p.domains.map(d => pruned.getOrElse(d.name, d))
        .filterNot(d => d.name.startsWith(LevelEncoding.orderDomainPrefix) && d.axioms.isEmpty)
      p.copy(domains = domains)(p.pos, p.info, p.errT)
    }
  }

  /** Attached to the loop encoding a closure implementation proof; `tuple` is the measure of the implemented spec. */
  case class ClosureProofTermination(tuple: DecreasesTuple) extends vpr.Info {
    override val comment: Seq[String] = Nil
    override val isCached: Boolean = false
  }
}

private class ModularTerminationChecks(override val program: vpr.Program, override val reportError: AbstractError => Unit)
  extends ProgramManager with DecreasesCheck with ExpTransformer with NestedPredicates {

  import ModularTerminationTransformer._

  private case class Spec(tuple: Option[DecreasesTuple], wildcard: Option[DecreasesWildcard]) {
    /** Measures starting with a termination level are subject to the modular checks. */
    lazy val leveled: Boolean = tuple.exists(_.tupleExpressions.headOption.exists(_.typ == LevelEncoding.levelType))
    lazy val imported: Boolean = (tuple.toSeq ++ wildcard).exists(TerminationEncoding.isImported)
    lazy val spec: DecreasesSpecification = DecreasesSpecification(tuple, wildcard, None)
  }

  private def specOf(exps: Seq[vpr.Exp]): Spec = Spec(
    exps.collectFirst { case t: DecreasesTuple => t },
    exps.collectFirst { case w: DecreasesWildcard => w },
  )

  private lazy val methodSpecs: Map[String, Spec] = program.methods.map(m => m.name -> specOf(m.pres)).toMap
  private lazy val functionSpecs: Map[String, Spec] = program.functions.map(f => f.name -> specOf(f.pres ++ f.posts)).toMap

  lazy val transformedProgram: vpr.Program = {
    val methods = program.methods.map(transformMethod)
    val functions = program.functions.map(hideLeveledMeasures)
    val proofMethods = program.functions.flatMap(functionChecks)
    program.copy(methods = methods ++ proofMethods, functions = functions)(program.pos, program.info, program.errT)
  }

  /*
   * Hiding leveled measures from Viper's termination plugin.
   */

  /**
    * This transformation performs all termination checks for members with leveled measures (except for the checks
    * that loops decrease their own measures). Thus, for Viper's termination plugin, their termination is assumed.
    */
  private def hideLeveledMeasure(spec: Spec, exps: Seq[vpr.Exp]): Seq[vpr.Exp] = {
    if (!spec.leveled) exps
    else {
      val conditions = spec.tuple.map(_.condition) ++ spec.wildcard.map(_.condition)
      val condition =
        if (conditions.exists(_.isEmpty)) None
        else Some(conditions.flatten.reduce[vpr.Exp](vpr.Or(_, _)()))
      val (pos, info, errT) = spec.tuple.get.meta
      exps.filterNot(_.isInstanceOf[DecreasesClause]) :+ DecreasesWildcard(condition)(pos, info, errT)
    }
  }

  private def hideLeveledMeasures(f: vpr.Function): vpr.Function = {
    val spec = functionSpecs(f.name)
    if (!spec.leveled) f
    else f.copy(pres = hideLeveledMeasure(spec, f.pres), posts = f.posts.filterNot(_.isInstanceOf[DecreasesClause]))(f.pos, f.info, f.errT)
  }

  /*
   * Checks for methods.
   */

  private def transformMethod(m: vpr.Method): vpr.Method = {
    val spec = methodSpecs(m.name)
    val withChecks = m.body match {
      case Some(body) if spec.leveled && !spec.imported =>
        val callerTuple = spec.tuple.get
        val checked = addCallChecks(body, callerTuple.tupleExpressions.map(vpr.Old(_)()), callerTuple.condition.map(vpr.Old(_)()))
        val withNested = if (containsPredicateInstances(spec.spec)) addNestedPredicateInformation.execute[vpr.Seqn](checked) else checked
        m.copy(body = Some(withNested))(m.pos, m.info, m.errT)
      case _ => m
    }
    withChecks.copy(pres = hideLeveledMeasure(spec, withChecks.pres))(m.pos, m.info, m.errT)
  }

  /**
    * Adds termination checks before every method call and before every loop: the callee has to terminate and, if its
    * measure is leveled, its measure has to be smaller than the caller's measure. Loops have to terminate as well;
    * Viper's termination plugin checks that they decrease their own measures.
    */
  private def addCallChecks(body: vpr.Seqn, callerTuple: Seq[vpr.Exp], callerCondition: Option[vpr.Exp]): vpr.Seqn = {
    def goSeqn(seqn: vpr.Seqn): vpr.Seqn = vpr.Seqn(seqn.ss.map(go), seqn.scopedSeqnDeclarations)(seqn.pos, seqn.info, seqn.errT)

    def go(stmt: vpr.Stmt): vpr.Stmt = stmt match {
      case w: vpr.While if w.info.getUniqueInfo[ClosureProofTermination].isDefined =>
        // calls in closure implementation proofs are compared with the measure of the implemented spec
        val proofTuple = w.info.getUniqueInfo[ClosureProofTermination].get.tuple
        val snapshots = proofTuple.tupleExpressions.map(e => vpr.LocalVarDecl(uniqueName("proof_measure"), e.typ)(e.pos, e.info, e.errT))
        val assignments = snapshots.zip(proofTuple.tupleExpressions).map { case (v, e) => vpr.LocalVarAssign(v.localVar, e)(e.pos, e.info, e.errT) }
        val checkedBody = addCallChecks(w.body, snapshots.map(_.localVar), proofTuple.condition)
        val newBody = vpr.Seqn(assignments :+ checkedBody, snapshots)(w.body.pos, w.body.info, w.body.errT)
        w.copy(body = newBody)(w.pos, w.info.removeUniqueInfo[ClosureProofTermination], w.errT)

      case w: vpr.While =>
        val errTrafo = vpr.ErrTrafo({
          case AssertFailed(_, r, c) => MethodTerminationError(w, r, c)
          case d => d
        })
        val loopSpec = specOf(w.invs)
        val required = callerCondition.fold[vpr.Exp](w.cond)(c => vpr.And(c, w.cond)())
        val loopTerminates = createConditionCheck(Some(required), Some(loopSpec.spec.getTerminationCondition), errTrafo,
          ReasonTrafoFactory(w).generateTerminationConditionFalse(w))
        vpr.Seqn(Seq(loopTerminates, w.copy(body = goSeqn(w.body))(w.pos, w.info, w.errT)), Nil)(w.pos, vpr.NoInfo, vpr.NodeTrafo(w))
      case i: vpr.If => i.copy(thn = goSeqn(i.thn), els = goSeqn(i.els))(i.pos, i.info, i.errT)
      case seqn: vpr.Seqn => goSeqn(seqn)
      case p: vpr.Package => p.copy(proofScript = goSeqn(p.proofScript))(p.pos, p.info, p.errT)

      case mc: vpr.MethodCall =>
        val callee = program.findMethod(mc.methodName)
        val calleeSpec = methodSpecs(mc.methodName)
        val substitution: Map[vpr.LocalVar, vpr.Exp] = callee.formalArgs.map(_.localVar).zip(mc.args).toMap
        val errTrafo = vpr.ErrTrafo({
          case AssertFailed(_, r, c) => MethodTerminationError(mc, r, c)
          case d => d
        })
        val (checks, dynamic) = callChecksWithKind(callee, calleeSpec, substitution, callerTuple, callerCondition, errTrafo, inContract = false)
        if (!dynamic) vpr.Seqn(checks :+ mc, Nil)(mc.pos, vpr.NoInfo, vpr.NodeTrafo(mc))
        else {
          // A comparison that the SMT solver decides is checked on a branch that ends after the check, so that the
          // comparison is not assumed afterwards. Assumed comparisons of levels (and the lexicographic disjunctions of
          // the tuples) burden every later proof obligation of the method; see `checkInDeadBranch`.
          vpr.Seqn(Seq(checkInDeadBranch(checks, mc), mc), Nil)(mc.pos, vpr.NoInfo, vpr.NodeTrafo(mc))
        }

      case other => other
    }

    goSeqn(body)
  }

  /**
    * Executes `checks` on a branch that is closed afterwards (`if (*) { checks; inhale false }`). The checks are
    * verified as before, but their assertions are not assumed after the branch. Silicon assumes every assertion that it
    * proves; for a comparison of levels decided by the SMT solver, this adds the quantified characterization of the
    * multiset order, and for the tuple the disjunction of the lexicographic order, to all later proof obligations of the
    * method. In VerifiedSCION, this made `SCION.SerializeTo` several times slower.
    */
  private def checkInDeadBranch(checks: Seq[vpr.Stmt], at: vpr.Stmt): vpr.Stmt = {
    val choice = vpr.LocalVarDecl(uniqueName("termination_check"), vpr.Bool)(at.pos)
    val closed = vpr.Seqn(checks :+ vpr.Inhale(vpr.FalseLit()(at.pos))(at.pos), Nil)(at.pos)
    vpr.Seqn(Seq(vpr.If(choice.localVar, closed, vpr.Seqn(Nil, Nil)(at.pos))(at.pos)), Seq(choice))(at.pos)
  }

  private def callChecks(callee: ErrorNode, calleeSpec: Spec, substitution: Map[vpr.LocalVar, vpr.Exp], callerTuple: Seq[vpr.Exp],
                         callerCondition: Option[vpr.Exp], errTrafo: vpr.ErrTrafo, inContract: Boolean): Seq[vpr.Stmt] =
    callChecksWithKind(callee, calleeSpec, substitution, callerTuple, callerCondition, errTrafo, inContract)._1

  /** Returns the checks of a call, and whether the comparison of the levels is left to the SMT solver. */
  private def callChecksWithKind(callee: ErrorNode, calleeSpec: Spec, substitution: Map[vpr.LocalVar, vpr.Exp], callerTuple: Seq[vpr.Exp],
                                 callerCondition: Option[vpr.Exp], errTrafo: vpr.ErrTrafo, inContract: Boolean): (Seq[vpr.Stmt], Boolean) = {
    if (!calleeSpec.leveled) {
      // the callee does not have a leveled measure; it only needs to terminate
      val terminationCondition = calleeSpec.spec.getTerminationCondition.replace(substitution)
      (Seq(createConditionCheck(callerCondition, Some(terminationCondition), errTrafo,
        ReasonTrafoFactory(callee).generateTerminationConditionFalse(callee))), false)
    } else {
      val calleeTuple = calleeSpec.tuple.get
      val reasonTrafoFactory = ReasonTrafoFactory(calleeTuple)
      val conditionCheck = createConditionCheck(callerCondition, calleeTuple.condition.map(_.replace(substitution)),
        errTrafo, reasonTrafoFactory.generateTupleConditionFalse(calleeTuple))
      val calleeExps = calleeTuple.tupleExpressions.map(_.replace(substitution))
      val importedAtomsOnly = staticLevel(calleeExps.head).exists(l => l.nonEmpty && l.keys.forall(a => !a.startsWith(LevelEncoding.mainAtomPrefix)))
      if (inContract && isInterfaceContext(callerTuple) && importedAtomsOnly) {
        // see [[isInterfaceContext]]
        return (Seq(conditionCheck), false)
      }
      compareLevels(calleeExps.head, callerTuple.head) match {
        case LevelLess =>
          // the measure decreases in its first component
          (Seq(conditionCheck), false)
        case LevelsEqual =>
          (Seq(conditionCheck, tupleCheck(callerCondition, callerTuple.tail, calleeExps.tail, errTrafo, reasonTrafoFactory)), false)
        case LevelsUnknown =>
          val check = tupleCheck(callerCondition, callerTuple, calleeExps, errTrafo, reasonTrafoFactory)
          (Seq(conditionCheck) ++ constantBounds(callerTuple.head) ++ singletonReduction(calleeExps.head, callerTuple.head) ++
            levelHints(callerTuple.head, calleeExps.head) :+ check, true)
      }
    }
  }

  /**
    * Returns true if `callerTuple` is the measure of an interface method or of an implementation proof. The level of an
    * interface method is the level of its receiver, which is unknown. A call in the *contract* of such a member to a
    * member whose level consists of (at least one and only) atoms of imported packages is safe nevertheless: such a
    * member does not receive objects through which it could call back, as its level does not depend on any object, and
    * the objects it can create or bound have types of its own or of lower packages. The exception does not apply to
    * the bodies of implementation proofs, whose call of the implementation is the check that the implementation's
    * measure is at most the measure of the interface method, nor to members of the bottom stratum, which may call
    * interface methods of the bottom stratum.
    */
  private def isInterfaceContext(callerTuple: Seq[vpr.Exp]): Boolean = callerTuple.lastOption.exists { e =>
    val marker = e match {
      case vpr.Old(inner) => inner
      case other => other
    }
    marker match {
      case app: vpr.DomainFuncApp => app.funcname.startsWith("ItfMethodMeasure") || app.funcname.startsWith("ProofMeasure")
      case _ => false
    }
  }

  /** The last element of every leveled termination tuple records the kind of member (see [[TerminationEncoding]]). */
  private def isKindMarker(e: vpr.Exp): Boolean = e.typ match {
    case vpr.DomainType(name, _) => name == TerminationEncoding.domainName
    case _ => false
  }

  /**
    * Checks that `smaller` is below `bigger` in the lexicographic order. This differs from the plugin's check (see
    * `DecreasesCheck.createTupleCheck`) in how tuples of different lengths are compared: if `smaller` ends (i.e., reaches
    * its kind marker) where `bigger` continues with local measures, then `smaller` is below `bigger`. This is sound:
    * a member whose tuple ends after its level cannot call any member of the same level, as such a call would have to
    * decrease the (absent) local measures. Thus, it can only make finitely many calls at its level, all of which
    * decrease the kind marker. The plugin, in contrast, considers the shorter tuple to be bigger, which would reject,
    * e.g., an implementation without local measures of an interface method with local measures at the same level.
    */
  private def tupleCheck(tupleCondition: Option[vpr.Exp], bigger: Seq[vpr.Exp], smaller: Seq[vpr.Exp],
                         errTrafo: vpr.ErrTrafo, reasonTrafoFactory: ReasonTrafoFactory): vpr.Stmt = {
    if (decreasingFunc.isEmpty || boundedFunc.isEmpty) return createTupleCheck(tupleCondition, bigger, smaller, errTrafo, reasonTrafoFactory)
    val decreasesReTrafo = reasonTrafoFactory.generateTupleDecreasesFalse()
    val simpleReTrafo = reasonTrafoFactory.generateTupleSimpleFalse()
    val boundReTrafo = reasonTrafoFactory.generateTupleBoundedFalse()
    val decrTypeVars = decreasingFunc.get.formalArgs.flatMap(_.typ.typeVariables)
    val boundTypeVars = boundedFunc.get.formalArgs.flatMap(_.typ.typeVariables)

    def lex(b: Seq[vpr.Exp], s: Seq[vpr.Exp]): vpr.Exp = (b, s) match {
      case (_, Seq()) => vpr.FalseLit()(errT = decreasesReTrafo)
      case (Seq(), _) => vpr.TrueLit()(errT = simpleReTrafo)
      case (bh +: _, sh +: _) if isKindMarker(sh) && !isKindMarker(bh) =>
        // `smaller` ended, `bigger` continues
        vpr.TrueLit()(errT = simpleReTrafo)
      case (bh +: _, sh +: _) if !isKindMarker(sh) && isKindMarker(bh) =>
        vpr.FalseLit()(errT = decreasesReTrafo)
      case (bh +: bt, sh +: st) =>
        if (!(bh.isSubtype(sh) || sh.isSubtype(bh))) vpr.FalseLit()(errT = decreasesReTrafo)
        else {
          val dec = vpr.DomainFuncApp(decreasingFunc.get, Seq(sh, bh), Map(decrTypeVars.head -> sh.typ, decrTypeVars.last -> bh.typ))(errT = decreasesReTrafo)
          val bound = vpr.DomainFuncApp(boundedFunc.get, Seq(bh), Map(boundTypeVars.head -> bh.typ))(errT = boundReTrafo)
          val eq = vpr.EqCmp(sh, bh)(errT = decreasesReTrafo)
          vpr.Or(vpr.And(dec, bound)(errT = simpleReTrafo), vpr.And(eq, lex(bt, st))(errT = simpleReTrafo))(errT = simpleReTrafo)
        }
    }

    val check = lex(bigger, smaller)
    val implied = tupleCondition match {
      case Some(c) => vpr.Implies(c, check)(errT = simpleReTrafo)
      case None => check
    }
    vpr.Assert(implied)(errT = errTrafo)
  }

  /*
   * Static comparison of levels. Most calls compare levels that consist of atoms only, e.g., calls to members of lower
   * strongly connected components or of imported packages, and recursive calls within a component. These comparisons
   * are decided here, which keeps the theory of levels out of the SMT solver's way for programs without dynamically
   * bound calls (the unused parts of the encoding are pruned, see [[ModularTerminationTransformer.pruneLevelEncoding]]).
   * The decisions only use the order facts that the encoding axiomatizes and hold in every model of these facts.
   */

  private sealed trait LevelComparison
  private case object LevelLess extends LevelComparison
  private case object LevelsEqual extends LevelComparison
  private case object LevelsUnknown extends LevelComparison

  /** The strict order facts between atoms that the encoding axiomatizes. */
  private lazy val atomLtFacts: Set[(String, String)] = program.domains.filter(d => d.name.startsWith(LevelEncoding.orderDomainPrefix)).flatMap(_.axioms).map(_.exp).collect {
    case vpr.DomainFuncApp(LevelEncoding.atomLtFuncName, Seq(l: vpr.DomainFuncApp, r: vpr.DomainFuncApp), _) if l.args.isEmpty && r.args.isEmpty =>
      (l.funcname, r.funcname)
  }.toSet

  /**
    * The levels of the dynamic types that the encoding axiomatizes (`$typeLevel(T) == {T}` for named types `T`, see
    * `LevelEncoding.registerTypeLevels`), keyed by the type.
    */
  private lazy val typeLevels: Map[vpr.Exp, vpr.Exp] = program.domains.flatMap(_.axioms).map(_.exp).collect {
    case vpr.EqCmp(vpr.DomainFuncApp("$typeLevel", Seq(t), _), l) => t -> l
  }.toMap

  /** The type `T` if `t` is the dynamic type of an interface value built with the type `T`, e.g., `get1of2(tuple2(x, T))`. */
  private def constantType(t: vpr.Exp): vpr.Exp = t match {
    case vpr.DomainFuncApp("get1of2", Seq(vpr.DomainFuncApp("tuple2", Seq(_, typ), _)), _) => typ
    case other => other
  }

  /**
    * Returns the multiset of atoms denoted by `e` if `e` is built from atoms, the empty level, unions, and the levels of
    * dynamic types that are known syntactically (e.g., in the implementation proofs of a named type).
    */
  private def staticLevel(e: vpr.Exp): Option[Map[String, Int]] = e match {
    case vpr.Old(inner) => staticLevel(inner)
    case vpr.DomainFuncApp("$typeLevel", Seq(t), _) => typeLevels.get(constantType(t)).flatMap(staticLevel)
    case vpr.DomainFuncApp(LevelEncoding.lvlFuncName, Seq(vpr.ExplicitMultiset(elems)), _) =>
      val atoms = elems.collect { case a: vpr.DomainFuncApp if a.args.isEmpty && a.typ == LevelEncoding.atomType => a.funcname }
      if (atoms.size == elems.size) Some(atoms.groupMapReduce(identity)(_ => 1)(_ + _)) else None
    case vpr.DomainFuncApp(LevelEncoding.lvlFuncName, Seq(_: vpr.EmptyMultiset), _) => Some(Map.empty)
    case vpr.DomainFuncApp(LevelEncoding.unionFuncName, Seq(l, r), _) =>
      for { a <- staticLevel(l); b <- staticLevel(r) } yield (a.keySet ++ b.keySet).map(k => k -> (a.getOrElse(k, 0) + b.getOrElse(k, 0))).toMap
    case _ => None
  }

  /**
    * Splits a level into the atoms it certainly contains and the remaining parts (e.g., `levelOf(x)`), which may
    * contribute further atoms. The level is the union of both.
    */
  private def staticPart(e: vpr.Exp): Map[String, Int] = e match {
    case vpr.Old(inner) => staticPart(inner)
    case vpr.DomainFuncApp(LevelEncoding.unionFuncName, Seq(l, r), _) =>
      val (a, b) = (staticPart(l), staticPart(r))
      (a.keySet ++ b.keySet).map(k => k -> (a.getOrElse(k, 0) + b.getOrElse(k, 0))).toMap
    case other => staticLevel(other).getOrElse(Map.empty)
  }

  /**
    * Compares the static levels `callee` and `caller`. With `common` being their common atoms, `callee` = `common + e1`
    * and `caller` = `common + e2`. If both `e1` and `e2` are empty, the levels are equal. If `e2` is non-empty and every
    * atom of `e1` is known to be below some atom of `e2`, then `callee` is smaller than `caller` in the multiset
    * ordering (replace `e2` by `e1`). This holds even if distinct atom names denote the same atom.
    */
  private def compareStatic(l1: Map[String, Int], l2: Map[String, Int]): LevelComparison = {
    def excess(a: Map[String, Int], b: Map[String, Int]): Set[String] = a.collect { case (k, n) if n > b.getOrElse(k, 0) => k }.toSet
    val (e1, e2) = (excess(l1, l2), excess(l2, l1))
    if (e1.isEmpty && e2.isEmpty) LevelsEqual
    else if (e2.nonEmpty && e1.forall(x => e2.exists(y => atomLtFacts.contains((x, y))))) LevelLess
    else LevelsUnknown
  }

  /**
    * Compares `callee` with `caller`. If both are static, see `compareStatic`. If only the callee is static, it is below
    * the caller if it is below the atoms that the caller certainly contains, as adding atoms to a multiset makes it
    * bigger. This decides, e.g., the calls of members whose levels have an argument of an interface type (and thus
    * an opaque component) to members of lower components and imported packages.
    */
  private def compareLevels(callee: vpr.Exp, caller: vpr.Exp): LevelComparison = (staticLevel(callee), staticLevel(caller)) match {
    case (Some(l1), Some(l2)) => compareStatic(l1, l2)
    case (Some(l1), None) =>
      compareStatic(l1, staticPart(caller)) match {
        case LevelLess => LevelLess
        case _ => LevelsUnknown
      }
    case _ => LevelsUnknown
  }

  /**
    * The multiset ordering on levels requires the SMT solver to find atoms dominating the atoms of the smaller level.
    * These are usually atoms that occur in the levels being compared. The (trivially true) assertions returned here
    * mention the multiplicities of these atoms, which triggers the axioms needed to find such a witness.
    */
  private def levelHints(callerLevel: vpr.Exp, calleeLevel: vpr.Exp): Seq[vpr.Stmt] = {
    def atoms(e: vpr.Exp): Seq[vpr.DomainFuncApp] = e.deepCollect {
      case a: vpr.DomainFuncApp if a.args.isEmpty && a.typ == LevelEncoding.atomType => a
    }
    def count(atom: vpr.Exp, level: vpr.Exp): vpr.Exp =
      vpr.GeCmp(vpr.DomainFuncApp(LevelEncoding.countFunc, Seq(atom, level), Map.empty)(), vpr.IntLit(0)())()
    if (callerLevel.typ != LevelEncoding.levelType || calleeLevel.typ != LevelEncoding.levelType) Seq.empty
    else (atoms(callerLevel) ++ atoms(calleeLevel)).distinct.map { atom =>
      vpr.Assert(vpr.And(count(atom, calleeLevel), count(atom, callerLevel))())()
    }
  }

  /**
    * Named level constants: parameterless level functions without precondition, e.g.,
    * `func Bound() level { return levelOf(f) + levelOf(g) }`, which may be opaque.
    */
  private lazy val levelConstants: Seq[vpr.Function] = program.functions.filter { f =>
    f.formalArgs.isEmpty && f.typ == LevelEncoding.levelType && f.body.isDefined && f.pres.forall(_.isInstanceOf[DecreasesClause])
  }

  private lazy val levelConstantBodies: Map[String, vpr.Exp] = levelConstants.map(f => f.name -> f.body.get).toMap

  /** Replaces the level constants in `e` by their bodies, up to a fixed depth. */
  private def inlineLevelConstants(e: vpr.Exp): vpr.Exp = {
    def once(x: vpr.Exp): vpr.Exp = x.transform({
      case app: vpr.FuncApp if app.args.isEmpty && levelConstantBodies.contains(app.funcname) => levelConstantBodies(app.funcname)
    }, Traverse.BottomUp)
    Iterator.iterate(e)(once).take(17).sliding(2).collectFirst { case Seq(a, b) if a == b => a }.getOrElse(e)
  }

  /**
    * Facts `B() < caller` for the level constants `B` whose bodies are statically below the caller's level. A check
    * whose callee level is opaque but known to be below a constant, e.g., `levelOf(s.Path) < PathBound()` from a
    * predicate, then follows by transitivity. The facts are decided statically (see `compareLevels`) and assumed on the
    * branch of the check only (see `checkInDeadBranch`), so that the atoms of the constants never reach the other proof
    * obligations of the method. Opaque constants thus keep facts about them cheap for the SMT solver.
    */
  private def constantBounds(callerLevel: vpr.Exp): Seq[vpr.Stmt] =
    if (callerLevel.typ != LevelEncoding.levelType) Seq.empty
    else levelConstants.flatMap { f =>
      val call = vpr.FuncApp(f, Seq.empty)()
      compareLevels(inlineLevelConstants(call), callerLevel) match {
        case LevelLess => Some(vpr.Inhale(vpr.DomainFuncApp(LevelEncoding.levelLtFunc, Seq(call, callerLevel), Map.empty)())())
        case _ => None
      }
    }

  /**
    * If the caller's level is a single atom `{a}` and the callee's level is `A + R` with static atoms `A` that are all
    * statically below `a` and a single opaque part `R` (e.g., `{g} + levelOf(x)`), then `R < {a}` implies
    * `A + R < {a}`: both mean that all atoms are below `a`. The implication is assumed on the branch of the check, so
    * that the SMT solver only has to show `R < {a}` (e.g., by transitivity from a bound of `R`), rather than to compare
    * multisets with an opaque component.
    */
  private def singletonReduction(calleeLevel: vpr.Exp, callerLevel: vpr.Exp): Seq[vpr.Stmt] = {
    def leaves(e: vpr.Exp): Seq[vpr.Exp] = e match {
      case vpr.DomainFuncApp(LevelEncoding.unionFuncName, Seq(l, r), _) => leaves(l) ++ leaves(r)
      case other => Seq(other)
    }
    if (calleeLevel.typ != LevelEncoding.levelType || callerLevel.typ != LevelEncoding.levelType) return Seq.empty
    val (static, opaque) = leaves(calleeLevel).partition(staticLevel(_).isDefined)
    val staticAtoms = static.flatMap(staticLevel).foldLeft(Map.empty[String, Int]) { (acc, m) =>
      (acc.keySet ++ m.keySet).map(k => k -> (acc.getOrElse(k, 0) + m.getOrElse(k, 0))).toMap
    }
    (staticLevel(callerLevel), opaque) match {
      case (Some(caller), Seq(rest)) if caller.size == 1 && caller.values.head == 1 && static.nonEmpty &&
          staticAtoms.keys.forall(x => atomLtFacts.contains((x, caller.keys.head))) =>
        def lt(l: vpr.Exp): vpr.Exp = vpr.DomainFuncApp(LevelEncoding.levelLtFunc, Seq(l, callerLevel), Map.empty)()
        Seq(vpr.Inhale(vpr.Implies(lt(rest), lt(calleeLevel))())())
      case _ => Seq.empty
    }
  }

  /*
   * Checks for functions.
   */

  /** `contract` is true for the checks of the calls in the contract of `function`. */
  private case class FunctionContext(function: vpr.Function, callerTuple: Seq[vpr.Exp], callerCondition: Option[vpr.Exp], contract: Boolean) extends ExpressionContext {
    override val conditionInEx: Option[vpr.LocalVarDecl] = Some(vpr.LocalVarDecl(uniqueName("condInEx"), vpr.Bool)())
  }

  override def transformExp(e: vpr.Exp, c: ExpressionContext, inhaleExp: Boolean): vpr.Stmt = (e, c) match {
    case (_: DecreasesClause, _) => EmptyStmt
    case (call: vpr.FuncApp, context: FunctionContext) =>
      val argChecks = call.args.map(transformExp(_, context, inhaleExp = false))
      val callee = program.findFunction(call.funcname)
      val substitution: Map[vpr.LocalVar, vpr.Exp] = callee.formalArgs.map(_.localVar).zip(call.args).toMap
      val errTrafo = vpr.ErrTrafo({
        case AssertFailed(_, r, c) => FunctionTerminationError(call, r, c)
        case d => d
      })
      val checks = callChecks(callee, functionSpecs(call.funcname), substitution, context.callerTuple, context.callerCondition, errTrafo, context.contract)
      vpr.Seqn(argChecks ++ checks, Nil)()
    case _ => super.transformExp(e, c, inhaleExp)
  }

  /** Generates methods checking the calls in the precondition, postcondition, and body of `f`. */
  private def functionChecks(f: vpr.Function): Seq[vpr.Method] = {
    val spec = functionSpecs(f.name)
    if (!spec.leveled || spec.imported) return Seq.empty

    val tuple = spec.tuple.get
    val pres = f.pres.filterNot(_.isInstanceOf[DecreasesClause])
    val posts = f.posts.filterNot(_.isInstanceOf[DecreasesClause])

    def method(suffix: String, methodPres: Seq[vpr.Exp], check: vpr.Stmt, decls: Seq[vpr.Declaration]): Option[vpr.Method] = {
      val simplified = Simplifier.simplify(check)
      val withNested = if (containsPredicateInstances(spec.spec)) addNestedPredicateInformation.execute[vpr.Stmt](simplified) else simplified
      // a check method without assertions checks nothing
      if (isEmptyStatement(withNested) || !withNested.existsDefined { case _: vpr.Assert => }) None
      else Some(vpr.Method(uniqueName(s"${f.name}_${suffix}_$checkMethodMarker"), f.formalArgs, Nil, methodPres, Nil,
        Some(vpr.Seqn(Seq(withNested), decls)()))(f.pos, f.info, f.errT))
    }

    val oldTuple = tuple.tupleExpressions.map(vpr.Old(_)())
    val oldCondition = tuple.condition.map(vpr.Old(_)())

    val bodyCheck = f.body.flatMap { body =>
      val context = FunctionContext(f, oldTuple, oldCondition, contract = false)
      method("body", pres, transformExp(body, context, inhaleExp = false), context.conditionInEx.toSeq)
    }

    val postsCheck = if (posts.isEmpty) None else {
      val context = FunctionContext(f, oldTuple, oldCondition, contract = true)
      val result = vpr.LocalVarDecl(uniqueName("$result"), f.typ)()
      val conjunction = posts.map(_.transform({ case vpr.Result(_) => result.localVar }, Traverse.BottomUp)).reduce[vpr.Exp](vpr.And(_, _)())
      method("posts", pres, transformExp(conjunction, context, inhaleExp = false), result +: context.conditionInEx.toSeq)
    }

    // in preconditions, the caller's measure is evaluated at the call, as the precondition is not yet established
    val presCheck = if (pres.isEmpty) None else {
      val context = FunctionContext(f, tuple.tupleExpressions, tuple.condition, contract = true)
      val conjunction = pres.reduce[vpr.Exp](vpr.And(_, _)())
      method("pres", Seq.empty, transformExp(conjunction, context, inhaleExp = true), context.conditionInEx.toSeq)
    }

    bodyCheck.toSeq ++ postsCheck ++ presCheck
  }
}
