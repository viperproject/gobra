package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier.GobraDependencyAnalysisInfo
import viper.gobra.reporting.VerifierError
import viper.silicon.dependencyAnalysis.{AssumptionType, DependencyGraphInterpreter, DependencyType}
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


  private def identifyGobraNodes(pNode: PNode, dependencyTypeOuter: Option[DependencyType]=None)(implicit positionManager: PositionManager): Iterable[GobraDependencyAnalysisInfo] = {

    def go(pNodes: Iterable[PNode], dependencyType: Option[DependencyType]=None) = {
      pNodes.flatMap(identifyGobraNodes(_, List(dependencyTypeOuter, dependencyType).find(_.isDefined).flatten))
    }

    def goS(pNode: PNode, dependencyType: Option[DependencyType]=None) =
      identifyGobraNodes(pNode, List(dependencyTypeOuter, dependencyType).find(_.isDefined).flatten)

    def goOpt(pNode: Option[PNode], dependencyType: Option[DependencyType]=None) = {
      pNode.map(identifyGobraNodes(_, List(dependencyTypeOuter, dependencyType).find(_.isDefined).flatten)).getOrElse(Set.empty)
    }

    def goSpec(spec: PFunctionSpec, isAbstractFunction: Boolean) = {
      val postCondType = if(isAbstractFunction) AssumptionType.ExplicitPostcondition else AssumptionType.ImplicitPostcondition
      spec.pres.flatMap(goTopLevelConjuncts(_, Some(DependencyType(AssumptionType.Precondition, AssumptionType.Precondition)))) ++
        spec.preserves.flatMap(goTopLevelConjuncts(_, Some(DependencyType(AssumptionType.Precondition, postCondType)))) ++
        spec.posts.flatMap(goTopLevelConjuncts(_, Some(DependencyType.make(postCondType)))) ++
        go(spec.terminationMeasures)
    }


    def goTopLevelConjuncts(pNode: PNode, dependencyType: Option[DependencyType]=None): Set[GobraDependencyAnalysisInfo] = {
      val depType = List(dependencyTypeOuter, dependencyType).find(_.isDefined).flatten
      pNode match {
        case PAnd(left, right) => goTopLevelConjuncts(left, depType) ++ goTopLevelConjuncts(right, depType)
        case _ => getGobraDependencyAnalysisInfo(pNode, depType)
      }
    }

    def getDependencyType(pNode: PNode, dependencyType: Option[DependencyType]): DependencyType = {
      val pNodeDepType = pNode match {
        case _: PFold | _: PUnfold | _: PPackageWand | _: PApplyWand => DependencyType.Rewrite
        case _: PAssert | _: PExhale | _: PRefute =>
          DependencyType.ExplicitAssertion
        case _: PAssume | _: PInhale => DependencyType.ExplicitAssumption
        case _: PInvoke => DependencyType.MethodCall
        case _: PParameter | _: PResult => DependencyType.Internal
        case _: PExplicitGhostStatement | _: PImplementationProof => DependencyType.Ghost
        case _ => DependencyType.SourceCode
      }
      val allAvailDepTypes = List(dependencyTypeOuter, dependencyType, Some(pNodeDepType)).filter(_.isDefined).flatten
      val assertionType = allAvailDepTypes.head.assertionType
      val availAssumptionTypes = allAvailDepTypes.map(_.assumptionType)
      if(availAssumptionTypes.contains(AssumptionType.Explicit)){
        DependencyType(AssumptionType.Explicit, assertionType)
      }else if(availAssumptionTypes.contains(AssumptionType.Internal))
        DependencyType(AssumptionType.Internal, assertionType)
      else
        allAvailDepTypes.head
    }

    def getGobraDependencyAnalysisInfo(pNode: PNode, dependencyType: Option[DependencyType]=None): Set[GobraDependencyAnalysisInfo] = {
      try {
        val start = positionManager.positions.getStart(pNode).get
        val end = positionManager.positions.getFinish(pNode).get
        val sourcePosition = TranslatedPosition(positionManager.translate(start, end))
        val info = new GobraDependencyAnalysisInfo(pNode, start, end, sourcePosition, Some(getDependencyType(pNode, dependencyType)), Some(pNode.toString))
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
      case PFriendPkgDecl(_, assertion) => goS(assertion)


      case PTypeDef(typeDef, _) => goS(typeDef)
      case PInterfaceType(_, methSpecs, _) => {
        go(methSpecs)
      }

      // constants
      case PConstDecl(specs) => go(specs)

      // functions and methods
      case PFunctionDecl(id, args, result, spec, body) => go(Set(id, result) ++ args) ++ goSpec(spec, body.isEmpty) ++ goOpt(body.map(_._2))
      case PFunctionLit(id, closure) => goOpt(id) ++ goS(closure)
      case PMethodDecl(id, receiver, args, result, spec, body) => go(Set(id, receiver, result) ++ args) ++ goSpec(spec, body.isEmpty) ++ goOpt(body.map(_._2))
      case PMethodImplementationProof(id, receiver, args, result, _, body) => goOpt(body.map(_._2)) ++ go(Set(id, receiver, result) ++ args)
      case PFunctionSpec(pres, preserves, posts, terminationMeasures, _, _, _, _, _) =>
        (pres ++ preserves ++ posts).flatMap(goTopLevelConjuncts(_, None)) ++ go(terminationMeasures, Some(DependencyType.Invariant))
      case PMethodSig(id, args, result, spec, _) => go(Set(id, result, spec) ++ args)
      case PResult(params) => go(params)
      case PExplicitGhostMember(m) => goS(m, Some(DependencyType.Ghost))
      case PImplementationProof(subT, superT, alias, memberProofs) => Set.empty // TODO ake: what to do here?

      // TODO ake: closures
      case PClosureDecl(args, result, spec, body) => go(args ++ Set(result, spec)) ++ goOpt(body.map(_._2))
      case PClosureImplProof(impl, block) => goS(impl) ++ goS(block)

      // composed statements
      case PBlock(stmts) => go(stmts)
      case PSeq(stmts) => go(stmts)
      case PIfStmt(ifs, els) => go(ifs) ++ goOpt(els)
      case PIfClause(pre, condition, body) => goOpt(pre) ++ goS(condition, Some(DependencyType.PathCondition)) ++ goS(body)

      // loops
      case PForStmt(pre, cond, post, spec, body) => goOpt(pre) ++ goOpt(post) ++ goS(cond, Some(DependencyType.PathCondition)) ++ go(Set(spec, body))
      case PAssForRange(range, ass, spec, body) =>
        goS(range, Some(DependencyType.PathCondition)) ++ go(ass, Some(DependencyType.PathCondition)) ++ goS(spec) ++ goS(body)
      case PShortForRange(range, shorts, _, spec, body) =>
        goS(range, Some(DependencyType.PathCondition)) ++ go(shorts, Some(DependencyType.PathCondition)) ++ goS(spec) ++ goS(body)
      case PLoopSpec(invs, terminationMeasure) => invs.flatMap(inv => goTopLevelConjuncts(inv, Some(DependencyType.Invariant))) ++ goOpt(terminationMeasure, Some(DependencyType.Invariant))

      // switch-case, match TODO ake: should matched expr be a dependency of all clauses?
      case PExprSwitchStmt(pre, exp, cases, dflt) => goOpt(pre) ++ goS(exp, Some(DependencyType.PathCondition)) ++ go(cases ++ dflt)
      case PExprSwitchDflt(body) => goS(body)
      case PExprSwitchCase(left, body) => go(left, Some(DependencyType.PathCondition)) ++ goS(body)
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => goOpt(pre) ++ goOpt(binder, Some(DependencyType.PathCondition)) ++ goS(exp, Some(DependencyType.PathCondition)) ++ go(cases ++ dflt)
      case PTypeSwitchDflt(body) => goS(body)
      case PTypeSwitchCase(left, body) => go(left, Some(DependencyType.PathCondition)) ++ goS(body)


      // select
      case PSelectStmt(send, rec, aRec, sRec, dflt) => go(send ++ rec ++ aRec ++ sRec ++ dflt)
      case PSelectDflt(body) => goS(body)
      case PSelectSend(send, body) => goS(send) ++ goS(body)
      case PSelectRecv(recv, body) => goS(recv) ++ goS(body)
      case PSelectAssRecv(recv, ass, body) => goS(recv) ++ go(ass) ++ goS(body)
      case PSelectShortRecv(recv, shorts, body) => goS(recv) ++ go(shorts) ++ goS(body)

      // TODO ake: what to do with those?
      case POutline(body, spec) => goS(body) ++ goS(spec)
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