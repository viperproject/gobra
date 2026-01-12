package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier.GobraDependencyAnalysisInfo
import viper.gobra.reporting.VerifierError
import viper.silicon.dependencyAnalysis.{DependencyAnalysisNode, DependencyGraphInterpreter, ReadOnlyDependencyGraph}
import viper.silver.ast
import viper.silver.ast.{Program, TranslatedPosition}

import java.io.PrintWriter



class GobraDependencyGraphInterpreter(dependencyGraph: ReadOnlyDependencyGraph, typeInfo: TypeInfo, gobraErrors: List[VerifierError] /* TODO */) extends DependencyGraphInterpreter("gobra", dependencyGraph, List.empty, member=None) {

  private val positionManager = typeInfo.tree.root.positions

  private val nonDetermBoolFuncName = "nonDetermBoolForDA"
  private val nonDetermBoolEncoding = PFunctionDecl(PIdnDef(nonDetermBoolFuncName), Vector.empty, PResult(Vector(PNamedParameter(PIdnDef("res"), PBoolType()))),
    PFunctionSpec(Vector.empty, Vector.empty, Vector.empty, Vector.empty, Vector.empty), None)

  private def getPrunedProgram(crucialNodes: Set[DependencyAnalysisNode]): (PPackage, Double) = {
    val newPrograms = typeInfo.tree.originalRoot.programs.map(pruneProgram(_)(crucialNodes))
    val newPackage = PPackage(typeInfo.tree.root.packageClause, newPrograms, positionManager, typeInfo.tree.root.info)
    (newPackage, 0.0)
  }


  private def isCrucialNode(pNode: PNode)(implicit crucialNodes: Set[DependencyAnalysisNode]) = {
    val dependencyAnalysisInfo = getGobraDependencyAnalysisInfo(pNode)
    crucialNodes.exists(node => node.getSourceCodePosition.equals(dependencyAnalysisInfo.getPosition))
  }

  private def pruneProgram(pProgram: PProgram)(implicit crucialNodes: Set[DependencyAnalysisNode]): PProgram = {
    PProgram(pProgram.packageClause,
      pProgram.pkgInvariants.filter(inv => isCrucialNode(inv.inv)),
      pProgram.initPosts, pProgram.imports,
      pProgram.friends.map(friendDecl => if(isCrucialNode(friendDecl.assertion)) friendDecl else PFriendPkgDecl(friendDecl.path, PBoolLit(true))),
      pProgram.declarations.map(pruneMembers) ++ Vector(nonDetermBoolEncoding))
  }

  private def pruneMembers(pMember: PMember)(implicit crucialNodes: Set[DependencyAnalysisNode]): PMember = {
    pMember match {
      case PFunctionDecl(id, args, result, spec, body) => PFunctionDecl(id, args, result, pruneSpec(spec), pruneBody(body))
      case PMethodDecl(id, receiver, args, result, spec, body) => PMethodDecl(id, receiver, args, result, pruneSpec(spec), pruneBody(body))
      case PConstDecl(specs) => PConstDecl(specs.filter(isCrucialNode))
      case _ => pMember
    }
  }

  private def pruneBody(body: Option[(PBodyParameterInfo, PBlock)])(implicit crucialNodes: Set[DependencyAnalysisNode]): Option[(PBodyParameterInfo, PBlock)] = {
    body.map {
      case (parameterInfo, block) => (parameterInfo, pruneBlock(block))
    }
  }

  private def pruneSpec(pSpec: PFunctionSpec)(implicit crucialNodes: Set[DependencyAnalysisNode]): PFunctionSpec = {
    PFunctionSpec(pruneExpsConjunctLevel(pSpec.pres), pruneExpsConjunctLevel(pSpec.preserves), pruneExpsConjunctLevel(pSpec.posts), pSpec.terminationMeasures,
      pSpec.backendAnnotations, pSpec.isPure, pSpec.isTrusted, pSpec.isOpaque, pSpec.mayBeUsedInInit)
  }

  def getGobraDependencyAnalysisInfo(pNode: PNode, origSource: Option[PNode]=None): GobraDependencyAnalysisInfo = {
    val start = positionManager.positions.getStart(pNode).get
    val end = positionManager.positions.getFinish(pNode).get
    val sourcePosition = TranslatedPosition(positionManager.translate(start, end))
    val info = new GobraDependencyAnalysisInfo(pNode, start, end, sourcePosition, origSource, Some(pNode.toString))
    info
  }

  private def pruneExpressions(exprs: Vector[PExpression])(implicit crucialNodes: Set[DependencyAnalysisNode]):  Vector[PExpression] = {
    exprs.filter(isCrucialNode)
  }

  private def pruneCondition(expr: PExpression)(implicit crucialNodes: Set[DependencyAnalysisNode]):  PExpression = {
    if(isCrucialNode(expr)) expr else PInvoke(PNamedOperand(PIdnUse(nonDetermBoolFuncName)), Vector.empty, None)
  }


  private def pruneIfClause(pIfClause: PIfClause)(implicit crucialNodes: Set[DependencyAnalysisNode]): PIfClause = {
    PIfClause(pIfClause.pre.filter(isCrucialNode), pruneCondition(pIfClause.condition), pruneBlock(pIfClause.body))
  }

  private def pruneBlock(pBlock: PBlock)(implicit crucialNodes: Set[DependencyAnalysisNode]) = {
    PBlock(pBlock.stmts.map(pruneStmt))
  }

  private def pruneStmt(pStmt: PStatement)(implicit crucialNodes: Set[DependencyAnalysisNode]): PStatement = {
    pStmt match {
      case declaration: PDeclaration => declaration

      // constants
      case PConstDecl(specs) => PConstDecl(specs.filter(isCrucialNode))

      // functions and methods TODO ake
//      case PFunctionLit(id, closure) => goOpt(id) ++ goS(closure)
//      case PMethodImplementationProof(id, receiver, args, result, _, body) => goOpt(body.map(_._2)) ++ go(Set(id, receiver, result) ++ args)
//      case PFunctionSpec(pres, preserves, posts, terminationMeasures, _, _, _, _, _) =>
//        (pres ++ preserves ++ posts).flatMap(goTopLevelConjuncts(_, None)) ++ go(terminationMeasures)
//      case PMethodSig(id, args, result, spec, _) => go(Set(id, result, spec) ++ args)
//      case PResult(params) => go(params)
//      case PExplicitGhostMember(m) => goS(m)
//      case PImplementationProof(subT, superT, alias, memberProofs) => go(Set(subT, superT) ++ alias ++ memberProofs)
//
//      // TODO ake: closures
//      case PClosureDecl(args, result, spec, body) => go(args ++ Set(result, spec)) ++ goOpt(body.map(_._2))
//      case PClosureImplProof(impl, block) => goS(impl) ++ pruneStmt(block)

      // composed statements
      case PBlock(stmts) => PBlock(stmts.filter(isCrucialNode))
      case PSeq(stmts) => PSeq(stmts.filter(isCrucialNode))
      case PIfStmt(ifs, els) => PIfStmt(ifs.map(pruneIfClause), els.map(pruneBlock))

      // loops
      case PForStmt(pre, cond, post, spec, body) => PForStmt(pre.filter(isCrucialNode), pruneCondition(cond), post.filter(isCrucialNode), pruneLoopSpec(spec), pruneBlock(body))
      case PAssForRange(range, ass, spec, body) => PAssForRange(range, ass, pruneLoopSpec(spec), pruneBlock(body)) /* TODO */
      case PShortForRange(range, shorts, bs, spec, body) => PShortForRange(range, shorts, bs, pruneLoopSpec(spec), pruneBlock(body)) // TODO

      // switch-case, match
      case PExprSwitchStmt(pre, exp, cases, dflt) => PExprSwitchStmt(pre, exp /* TODO */, cases.map(c => PExprSwitchCase(pruneExpressions(c.left), pruneBlock(c.body))), dflt.map(pruneBlock))
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => PTypeSwitchStmt(pre, exp /* TODO */, binder, cases.map(c => PTypeSwitchCase(c.left /* TODO */, pruneBlock(c.body))), dflt.map(pruneBlock))
      // TODO ake
      //      case PMatchStatement(exp, clauses, _) => goS(exp) ++ go(clauses)
      //      case PMatchStmtCase(pattern, stmts, _) => goS(pattern) ++ go(stmts)

      // select TODO ake
//      case PSelectStmt(send, rec, aRec, sRec, dflt) =>
//      case PSelectDflt(body) => goS(body)
//      case PSelectSend(send, body) => goS(send) ++ goS(body)
//      case PSelectRecv(recv, body) => goS(recv) ++ goS(body)
//      case PSelectAssRecv(recv, ass, body) => Set(aggregateInfo(PAssignment(Vector(recv), ass), goS(recv) ++ go(ass))) ++ goS(body) // recv and ass form one dependency node
//      case PSelectShortRecv(recv, shorts, body) => Set(aggregateInfo(PAssignment(Vector(recv), shorts.map(id => PNamedOperand(PIdnUse(id.name)))), goS(recv) ++ go(shorts))) ++ goS(body) // recv and shorts form one dependency node

      // TODO ake: what to do with those?
      case POutline(body, spec) => POutline(pruneStmt(body), pruneFunctionSpec(spec))
//      case PWildcardMeasure(cond) => goOpt(cond)
//      case PTupleTerminationMeasure(tuple, cond) => go(tuple) ++ goOpt(cond)

      // ensure dependencies are determine on conjunct-level by splitting top-level conjunctions
      case PAssume(exp) => PAssume(pruneExpConjunctLevel(exp))
      case PInhale(exp) => PInhale(pruneExpConjunctLevel(exp))
//      case PAssert(exp) => PAssert(pruneExpConjunctLevel(exp)) TODO ake
      case PExhale(exp) => PExhale(pruneExpConjunctLevel(exp))
      case PRefute(exp) => PRefute(pruneExpConjunctLevel(exp))

      case _ => if(isCrucialNode(pStmt)) pStmt else PSeq(Vector.empty)
    }
  }

  private def pruneExpsConjunctLevel(exps: Vector[PExpression])(implicit crucialNodes: Set[DependencyAnalysisNode]): Vector[PExpression] = {
    exps.map(pruneExpConjunctLevel).filterNot(_.equals(PBoolLit(true)))
  }

  private def pruneExpConjunctLevel(exp: PExpression)(implicit crucialNodes: Set[DependencyAnalysisNode]): PExpression = {
    exp match {
      case PAnd(left, right) =>
        (pruneExpConjunctLevel(left), pruneExpConjunctLevel(right)) match {
          case (PBoolLit(true), r) => r
          case (l, PBoolLit(true)) => l
          case (l, r) => PAnd(l, r)
        }
      case _ => if(isCrucialNode(exp)) exp else PBoolLit(true)
    }
  }

  private def pruneFunctionSpec(spec: PFunctionSpec)(implicit crucialNodes: Set[DependencyAnalysisNode]) =
    PFunctionSpec(pruneExpsConjunctLevel(spec.pres), pruneExpsConjunctLevel(spec.preserves), pruneExpsConjunctLevel(spec.posts),
      spec.terminationMeasures.filter(isCrucialNode), spec.backendAnnotations, spec.isPure, spec.isTrusted, spec.isOpaque, spec.mayBeUsedInInit)

  private def pruneLoopSpec(spec: PLoopSpec)(implicit crucialNodes: Set[DependencyAnalysisNode]) = {
    PLoopSpec(pruneExpsConjunctLevel(spec.invariants), spec.terminationMeasure.filter(isCrucialNode))
  }

  override def getPrunedProgram(crucialNodes: Set[DependencyAnalysisNode], program: Program): (Program, Double) = {
    val newPkg = getPrunedProgram(crucialNodes)
    println(newPkg)
    throw new Exception("whatever") // TODO ake: adjust return type
  }

  override def pruneProgramAndExport(crucialNodes: Set[DependencyAnalysisNode], program: ast.Program , exportFileName: String): Unit = {
    val writer = new PrintWriter(exportFileName)
    val newProgram = getPrunedProgram(crucialNodes)
    writer.println(newProgram._1.toString())
    writer.close()
  }

}




