package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier.GobraDependencyAnalysisInfo
import viper.gobra.reporting.VerifierError
import viper.silicon.dependencyAnalysis.DependencyGraphInterpreter
import viper.silver.ast.TranslatedPosition
import viper.silver.dependencyAnalysis.AbstractDependencyGraphInterpreter

object GobraDependencyAnalysisAggregator {
  def convertFromDependencyGraphInterpreter(interpreter: AbstractDependencyGraphInterpreter, typeInfo: TypeInfo, errors: List[VerifierError]): GobraDependencyGraphInterpreter = {
    interpreter match {
      case interpreter: DependencyGraphInterpreter => new GobraDependencyGraphInterpreter(interpreter.getGraph, typeInfo, errors)
      case _ => throw new Exception(s"Unknown dependency graph interpreter $interpreter")
    }
  }

  /*
    Identifies the desired Gobra dependency nodes to be presented to the user.
    Each Gobra node is associated with a PNode and represents all SMT-level assumptions and assertions introduced while
    verifying this PNode (i.e. any Viper node associated with this PNode). As a consequence, all these assumptions depend on all the assertions, thus enabling the
    discovery of transitive dependencies.
    If the Gobra nodes are defined too fine-grained, dependency results might be unsound (because some transitive dependencies might be missed)!
    If the Gobra nodes are defined too coarse-grained, dependency results might be imprecise.
    Gobra nodes must not have subnodes, i.e. when statement stmt forms a node, none of its substatements/subexpressions may
    form a node, instead they are associated with stmt's node.
   */
  def identifyGobraNodes(typeInfo: TypeInfo): Iterable[GobraDependencyAnalysisInfo] = {
    val positionManager = typeInfo.tree.originalRoot.positions
    identifyGobraNodes(typeInfo.tree.originalRoot)(positionManager)
  }

  // TODO ake: should we also determine the assumption and assertion types here?
  private def identifyGobraNodes(pNode: PNode)(implicit positionManager: PositionManager): Iterable[GobraDependencyAnalysisInfo] = {

    def go(pNodes: Iterable[PNode]) = {
      pNodes.flatMap(identifyGobraNodes)
    }

    def goS(pNode: PNode) = identifyGobraNodes(pNode)

    def goOpt(pNode: Option[PNode]) = {
      pNode.map(identifyGobraNodes).getOrElse(Set.empty)
    }

    def goTopLevelConjuncts(pNode: PNode, origSource: Option[PNode]=None): Set[GobraDependencyAnalysisInfo] = pNode match {
      case PAnd(left, right) => goTopLevelConjuncts(left, origSource) ++ goTopLevelConjuncts(right, origSource)
      case _ => getGobraDependencyAnalysisInfo(pNode, origSource)
    }

    def getGobraDependencyAnalysisInfo(pNode: PNode, origSource: Option[PNode]=None): Set[GobraDependencyAnalysisInfo] = {
      try {
        val start = positionManager.positions.getStart(pNode).get
        val end = positionManager.positions.getFinish(pNode).get
        val sourcePosition = TranslatedPosition(positionManager.translate(start, end))
        val info = new GobraDependencyAnalysisInfo(pNode, start, end, sourcePosition, origSource, Some(pNode.toString))
        Set(info)
      } catch {
        case _ => Set.empty
      }
    }

    getGobraDependencyAnalysisInfo(pNode) ++ (pNode match {
      // packages and programs
      case PPackage(packageClause, programs, _, _) => go(packageClause +: programs)
      case PProgram(packageClause, pkgInvariants, initPosts, imports, friends, declarations) => go(packageClause +: (pkgInvariants ++ initPosts ++ imports ++ friends ++ declarations))
      case PPreamble(packageClause, pkgInvariants, initPosts, imports, friends, _) => getGobraDependencyAnalysisInfo(pNode) ++ go(packageClause +: (pkgInvariants ++ initPosts ++ imports ++ friends))
      case PPkgInvariant(inv, _) => getGobraDependencyAnalysisInfo(pNode) ++ goTopLevelConjuncts(inv)
      case PFriendPkgDecl(_, assertion) => goS(assertion)

      // constants
      case PConstDecl(specs) => go(specs)

      // functions and methods
      case PFunctionDecl(id, args, result, spec, body) => go(Set(id, result, spec) ++ args) ++ goOpt(body.map(_._2))
      case PFunctionLit(id, closure) => goOpt(id) ++ goS(closure)
      case PMethodDecl(id, receiver, args, result, spec, body) => go(Set(id, receiver, result, spec) ++ args) ++ goOpt(body.map(_._2))
      case PMethodImplementationProof(id, receiver, args, result, _, body) => goOpt(body.map(_._2)) ++ go(Set(id, receiver, result) ++ args)
      case PFunctionSpec(pres, preserves, posts, terminationMeasures, _, _, _, _, _) =>
        (pres ++ preserves ++ posts).flatMap(goTopLevelConjuncts(_, None)) ++ go(terminationMeasures)
      case PMethodSig(id, args, result, spec, _) => go(Set(id, result, spec) ++ args)
      case PResult(params) => go(params)
      case PExplicitGhostMember(m) => goS(m)
      case PImplementationProof(subT, superT, alias, memberProofs) => go(Set(subT, superT) ++ alias ++ memberProofs)

      // TODO ake: closures
      case PClosureDecl(args, result, spec, body) => go(args ++ Set(result, spec)) ++ goOpt(body.map(_._2))
      case PClosureImplProof(impl, block) => goS(impl) ++ goS(block)

      // composed statements
      case PBlock(stmts) => go(stmts)
      case PSeq(stmts) => go(stmts)
      case PIfStmt(ifs, els) => go(ifs) ++ goOpt(els)
      case PIfClause(pre, condition, body) => goOpt(pre) ++ goS(condition) ++ goS(body)

      // loops
      case PForStmt(pre, cond, post, spec, body) => goOpt(pre) ++ goOpt(post) ++ go(Set(cond, spec, body))
      case PAssForRange(range, ass, spec, body) =>
        goS(range) ++ go(ass) ++ goS(spec) ++ goS(body)
      case PShortForRange(range, shorts, _, spec, body) =>
        goS(range) ++ go(shorts) ++ goS(spec) ++ goS(body)
      case PLoopSpec(invs, terminationMeasure) => invs.flatMap(inv => goTopLevelConjuncts(inv, None)) ++ goOpt(terminationMeasure)

      // switch-case, match TODO ake: should matched expr be a dependency of all clauses?
      case PExprSwitchStmt(pre, exp, cases, dflt) => goOpt(pre) ++ goS(exp) ++ go(cases ++ dflt)
      case PExprSwitchDflt(body) => goS(body)
      case PExprSwitchCase(left, body) => go(left) ++ goS(body)
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => goOpt(pre) ++ goOpt(binder) ++ goS(exp) ++ go(cases ++ dflt)
      case PTypeSwitchDflt(body) => goS(body)
      case PTypeSwitchCase(left, body) => go(left) ++ goS(body)
      // TODO ake: treat as one statement or go more fine-grained?
      //      case PMatchStatement(exp, clauses, _) => goS(exp) ++ go(clauses)
      //      case PMatchStmtCase(pattern, stmts, _) => goS(pattern) ++ go(stmts)

      // select
      case PSelectStmt(send, rec, aRec, sRec, dflt) => go(send ++ rec ++ aRec ++ sRec ++ dflt)
      case PSelectDflt(body) => goS(body)
      case PSelectSend(send, body) => goS(send) ++ goS(body)
      case PSelectRecv(recv, body) => goS(recv) ++ goS(body)
      case PSelectAssRecv(recv, ass, body) => goS(recv) ++ go(ass) ++ goS(body)
      case PSelectShortRecv(recv, shorts, body) => goS(recv) ++ go(shorts) ++ goS(body)

      // TODO ake: what to do with those?
      case POutline(body, spec) => goS(body) ++ goS(spec)
      case PWildcardMeasure(cond) => goOpt(cond)
      case PTupleTerminationMeasure(tuple, cond) => go(tuple) ++ goOpt(cond)

      // ensure dependencies are determine on conjunct-level by splitting top-level conjunctions
      case PAssume(exp) => goTopLevelConjuncts(exp, Some(pNode))
      case PInhale(exp) => goTopLevelConjuncts(exp, Some(pNode))
//      case PAssert(exp) => goTopLevelConjuncts(exp, Some(pNode)) TODO ake
      case PExhale(exp) => goTopLevelConjuncts(exp, Some(pNode))
      case PRefute(exp) => goTopLevelConjuncts(exp, Some(pNode))

      // base case: we arrived at a "primitive" statement or expression. This is the granularity level of the analysis.
      // Importantly, we do not iterate over the children of these statements and expressions.
      case _ => Set.empty
    })
  }
}