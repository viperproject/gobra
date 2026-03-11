package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier.GobraDependencyAnalysisInfo
import viper.gobra.reporting.VerifierError
import viper.silicon.dependencyAnalysis.{AssumptionType, DependencyGraphInterpreter, DependencyType}
import viper.silver.ast.TranslatedPosition
import viper.silver.dependencyAnalysis.AbstractDependencyGraphInterpreter
import viper.silver.plugin.standard.termination.PDecreasesClause

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
    val importedNodes = typeInfo.getTransitiveTypeInfos(includeThis=false).filterNot(_.pkgName.name.equals("builtin")).flatMap(pkg =>
      identifyGobraNodes(pkg.getTypeInfo.tree.originalRoot)(pkg.getTypeInfo.tree.originalRoot.positions, isImported=true)
    )
    identifyGobraNodes(typeInfo.tree.originalRoot)(positionManager, isImported=false) ++ importedNodes
  }


  private def identifyGobraNodes(pNode: PNode, dependencyTypeOuter: Option[DependencyType]=None)(implicit positionManager: PositionManager, isImported: Boolean): Iterable[GobraDependencyAnalysisInfo] = {

    val outerOrPathCondition = dependencyTypeOuter.orElse(Some(DependencyType.PathCondition))

    def go(pNodes: Iterable[PNode], dependencyType: Option[DependencyType]=dependencyTypeOuter) = {
      pNodes.flatMap(identifyGobraNodes(_, dependencyType))
    }

    def goS(pNode: PNode, dependencyType: Option[DependencyType]=dependencyTypeOuter) =
      identifyGobraNodes(pNode, dependencyType)

    def goOpt(pNode: Option[PNode], dependencyType: Option[DependencyType]=dependencyTypeOuter) = {
      pNode.map(identifyGobraNodes(_, dependencyType)).getOrElse(Set.empty)
    }

    def goSpec(spec: PFunctionSpec, isAbstractFunction: Boolean, dependencyType: Option[DependencyType]=dependencyTypeOuter) = {
      val postCondType = AssumptionType.getPostcondType(isAbstractFunction, dependencyType, isImported)
      spec.pres.flatMap(goTopLevelConjuncts(_, Some(DependencyType(AssumptionType.Precondition, AssumptionType.Precondition)))) ++
        spec.preserves.flatMap(goTopLevelConjuncts(_, Some(DependencyType(AssumptionType.Precondition, postCondType)))) ++
        spec.posts.flatMap(goTopLevelConjuncts(_, Some(DependencyType.make(postCondType)))) ++
        go(spec.terminationMeasures)
    }


    def goTopLevelConjuncts(pNode: PNode, dependencyType: Option[DependencyType]=dependencyTypeOuter): Set[GobraDependencyAnalysisInfo] = {
      pNode match {
        case PAnd(left, right) => goTopLevelConjuncts(left, dependencyType) ++ goTopLevelConjuncts(right, dependencyType)
        case _ => getGobraDependencyAnalysisInfo(pNode, dependencyType)
      }
    }

    def getDependencyTypeForPNode(pNode: PNode, dependencyType: Option[DependencyType]): DependencyType = {
      val enforcedDepTypeOpt = pNode match {
        case _: PAssert | _: PExhale | _: PRefute => Some(DependencyType.ExplicitAssertion)
        case _: PAssume | _: PInhale => Some(DependencyType.ExplicitAssumption)
        case _: PParameter | _: PResult | _: PReceiver => Some(DependencyType.Internal)
        case _: PFold | _: PUnfold | _: PPackageWand | _: PApplyWand => Some(DependencyType.Rewrite)
        case _: PPkgInvariant => Some(DependencyType.Invariant)
        case _: PExplicitGhostStatement => Some(DependencyType.Ghost)
        case _: PGhostStatement | _: PProofAnnotation | _: PImplementationProof | _: PDecreasesClause | _: PTerminationMeasure => Some(DependencyType.Annotation)
        case _: PMethodDecl | _: PFunctionDecl | _: PMethodSig | _: PFunctionSpec if isImported => Some(DependencyType(AssumptionType.Precondition, AssumptionType.ImportedPostcondition))
        case m: PMethodDecl if m.body.isDefined   => Some(DependencyType(AssumptionType.Precondition, AssumptionType.ImplicitPostcondition))
        case f: PFunctionDecl if f.body.isDefined => Some(DependencyType(AssumptionType.Precondition, AssumptionType.ImplicitPostcondition))
        case _: PMethodDecl | _: PFunctionDecl | _: PMethodSig | _: PFunctionSpec => Some(DependencyType(AssumptionType.Precondition, AssumptionType.ExplicitPostcondition))
        case _ => None
      }

      if(enforcedDepTypeOpt.isDefined)
        enforcedDepTypeOpt.get
      else if(dependencyType.isDefined)
        dependencyType.get
      else
        pNode match {
          case _: PInvoke => DependencyType.MethodCall
          case _: PActualStatement => DependencyType.SourceCode
          case _ => DependencyType.SourceCode
        }

    }

    def getGobraDependencyAnalysisInfo(pNode: PNode, dependencyType: Option[DependencyType]=dependencyTypeOuter): Set[GobraDependencyAnalysisInfo] = {
      try {
        val start = positionManager.positions.getStart(pNode).get
        val end = positionManager.positions.getFinish(pNode).get
        val sourcePosition = TranslatedPosition(positionManager.translate(start, end))
        val info = new GobraDependencyAnalysisInfo(pNode, start, end, sourcePosition, Some(getDependencyTypeForPNode(pNode, dependencyType)), Some(pNode.toString))
        Set(info)
      } catch {
        case _ => Set.empty
      }
    }

    getGobraDependencyAnalysisInfo(pNode, dependencyTypeOuter) ++ (pNode match {
      // packages and programs
      case PPackage(packageClause, programs, _, _) => go(packageClause +: programs)
      case PProgram(packageClause, pkgInvariants, imports, friends, declarations) => go(packageClause +: (pkgInvariants ++ imports ++ friends ++ declarations))
      case PPreamble(packageClause, pkgInvariants, imports, friends, _) => go(packageClause +: (pkgInvariants ++ imports ++ friends))
      case PPkgInvariant(inv, _) => goTopLevelConjuncts(inv, Some(DependencyType.Invariant))
      case PFriendPkgDecl(_, assertion) => goS(assertion, Some(DependencyType.Annotation))


      case PTypeDef(typeDef, _) => goS(typeDef)
      case PInterfaceType(_, methSpecs, _) => go(methSpecs, Some(DependencyType(AssumptionType.Precondition, AssumptionType.ImplicitPostcondition)))

      // constants
      case PConstDecl(specs) => go(specs)

      // functions and methods
      case PFunctionDecl(id, args, result, spec, body) => go(Set(id, result) ++ args) ++ goSpec(spec, body.isEmpty) ++ goOpt(body.map(_._2), if(spec.isPure) Some(DependencyType.make(AssumptionType.FunctionBody)) else dependencyTypeOuter)
      case PFunctionLit(id, closure) => goOpt(id) ++ goS(closure)
      case PMethodDecl(id, receiver, args, result, spec, body) => go(Set(id, receiver, result) ++ args) ++ goSpec(spec, body.isEmpty) ++ goOpt(body.map(_._2), if(spec.isPure) Some(DependencyType.make(AssumptionType.FunctionBody)) else dependencyTypeOuter)
      case PMethodImplementationProof(id, receiver, args, result, _, body) => goOpt(body.map(_._2)) ++ go(Set(id, receiver, result) ++ args)
      case funcSpec: PFunctionSpec => goSpec(funcSpec, !funcSpec.isTrusted)
      case PMethodSig(id, args, result, spec, _) => go(Set(id, result) ++ args) ++ goSpec(spec, isAbstractFunction=true)
      case PResult(params) => go(params)
      case PExplicitGhostMember(m) => go(Set(m), Some(DependencyType.Ghost))
      case PImplementationProof(_, _, _, _) => Set.empty

      // TODO ake: closures
      case PClosureDecl(args, result, spec, body) => go(args ++ Set(result)) ++ goOpt(body.map(_._2)) ++ goSpec(spec, body.isEmpty)
      case PClosureImplProof(impl, block) => goS(impl) ++ goS(block)

      // composed statements
      case PBlock(stmts) => go(stmts)
      case PSeq(stmts) => go(stmts)
      case PIfStmt(ifs, els) => go(ifs) ++ goOpt(els)
      case PIfClause(pre, condition, body) => goOpt(pre) ++ goS(condition, outerOrPathCondition) ++ goS(body)

      // loops
      case PForStmt(pre, cond, post, spec, body) => goOpt(pre) ++ goOpt(post) ++ goS(cond, outerOrPathCondition) ++ go(Set(spec, body))
      case PAssForRange(range, ass, spec, body) =>
        goS(range, outerOrPathCondition) ++ go(ass, outerOrPathCondition) ++ goS(spec) ++ goS(body)
      case PShortForRange(range, shorts, _, spec, body) =>
        goS(range, outerOrPathCondition) ++ go(shorts, outerOrPathCondition) ++ goS(spec) ++ goS(body)
      case PLoopSpec(invs, terminationMeasure) => invs.flatMap(inv => goTopLevelConjuncts(inv, Some(DependencyType.Invariant))) ++ goOpt(terminationMeasure, Some(DependencyType.Annotation))

      // switch-case, match TODO ake: should matched expr be a dependency of all clauses?
      case PExprSwitchStmt(pre, exp, cases, dflt) => goOpt(pre) ++ goS(exp, outerOrPathCondition) ++ go(cases ++ dflt)
      case PExprSwitchDflt(body) => goS(body)
      case PExprSwitchCase(left, body) => go(left, outerOrPathCondition) ++ goS(body)
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => goOpt(pre) ++ goOpt(binder, outerOrPathCondition) ++ goS(exp, outerOrPathCondition) ++ go(cases ++ dflt)
      case PTypeSwitchDflt(body) => goS(body)
      case PTypeSwitchCase(left, body) => go(left, outerOrPathCondition) ++ goS(body)


      // select
      case PSelectStmt(send, rec, aRec, sRec, dflt) => go(send ++ rec ++ aRec ++ sRec ++ dflt)
      case PSelectDflt(body) => goS(body)
      case PSelectSend(send, body) => goS(send) ++ goS(body)
      case PSelectRecv(recv, body) => goS(recv) ++ goS(body)
      case PSelectAssRecv(recv, ass, body) => goS(recv) ++ go(ass) ++ goS(body)
      case PSelectShortRecv(recv, shorts, body) => goS(recv) ++ go(shorts) ++ goS(body)

      // TODO ake: what to do with those?
      case POutline(body, spec) => goS(body) ++ goSpec(spec, isAbstractFunction=false)
      case PWildcardMeasure(cond) => goOpt(cond, Some(DependencyType.Invariant))
      case PTupleTerminationMeasure(tuple, cond) => go(tuple, Some(DependencyType.Invariant)) ++ goOpt(cond, Some(DependencyType.Invariant))

      // ensure dependencies are determine on conjunct-level by splitting top-level conjunctions
      case PAssume(exp) => goTopLevelConjuncts(exp, Some(DependencyType.ExplicitAssumption))
      case PInhale(exp) => goTopLevelConjuncts(exp, Some(DependencyType.ExplicitAssumption))
      case PAssert(exp) => goTopLevelConjuncts(exp, Some(DependencyType.ExplicitAssertion))
      case PExhale(exp) => goTopLevelConjuncts(exp, Some(DependencyType.ExplicitAssertion))
      case PRefute(exp) => goTopLevelConjuncts(exp, Some(DependencyType.ExplicitAssertion))

      case PExplicitGhostStatement(stmt) => goS(stmt, Some(DependencyType.Ghost))
      case PMatchStatement(exp, clauses, _) => goS(exp, Some(DependencyType.Ghost)) ++ go(clauses, Some(DependencyType.Ghost))
      case PMatchStmtCase(pattern, stmts, _) => goS(pattern, Some(DependencyType.Ghost)) ++ go(stmts,Some(DependencyType.Ghost))

      // base case: we arrived at a "primitive" statement or expression. This is the granularity level of the analysis.
      // Importantly, we do not iterate over the children of these statements and expressions.
      case _ => Set.empty
    })
  }
}