package viper.gobra.backend

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.silicon.dependencyAnalysis.{DependencyAnalysisNode, DependencyGraph, DependencyGraphInterpreter, ReadOnlyDependencyGraph}
import viper.silver.ast
import viper.silver.ast.{AbstractSourcePosition, Program, TranslatedPosition}
import viper.silver.dependencyAnalysis.AbstractDependencyGraphInterpreter



class GobraDependencyGraphInterpreter(dependencyGraph: ReadOnlyDependencyGraph, typeInfo: TypeInfo) extends DependencyGraphInterpreter("gobra", dependencyGraph, member=None) {

  def getPrunedProgram(crucialNodes: Set[DependencyAnalysisNode]): (PPackage, Double) = {

    // TODO ake

    (typeInfo.tree.originalRoot, 0.0)
  }

  private def pruneProgram(pProgram: PProgram)(implicit crucialNodes: Set[DependencyAnalysisNode]): PProgram = {
    PProgram(pProgram.packageClause, pProgram.pkgInvariants, pProgram.initPosts, pProgram.imports, pProgram.friends, pProgram.declarations.map(pruneMembers))
  }

  private def pruneMembers(pMember: PMember)(implicit crucialNodes: Set[DependencyAnalysisNode]): PMember = {
    pMember match {
      case PFunctionDecl(id, args, result, spec, body) => PFunctionDecl(id, args, result, pruneSpec(spec), pruneBody(body))
      case PMethodDecl(id, receiver, args, result, spec, body) => PMethodDecl(id, receiver, args, result, pruneSpec(spec), body)
      case _ => pMember
    }
  }

  private def pruneBody(body: Option[(PBodyParameterInfo, PBlock)])(implicit crucialNodes: Set[DependencyAnalysisNode]): Option[(PBodyParameterInfo, PBlock)] = {
    body.map {
      case (parameterInfo, block) => (parameterInfo, pruneBlock(block))
    }
  }

  private def pruneSpec(pSpec: PFunctionSpec)(implicit crucialNodes: Set[DependencyAnalysisNode]): PFunctionSpec = {
    PFunctionSpec(pruneExpressions(pSpec.pres), pruneExpressions(pSpec.preserves), pruneExpressions(pSpec.posts), pSpec.terminationMeasures,
      pSpec.backendAnnotations, pSpec.isPure, pSpec.isTrusted, pSpec.isOpaque, pSpec.mayBeUsedInInit)
  }

  private def pruneExpressions(exprs:  Vector[PExpression])(implicit crucialNodes: Set[DependencyAnalysisNode]):  Vector[PExpression] = {
    exprs
  }

  private def pruneIfClause(pIfClause: PIfClause)(implicit crucialNodes: Set[DependencyAnalysisNode]): PIfClause = {
    PIfClause(pIfClause.pre /* TODO */, pIfClause.condition, pruneBlock(pIfClause.body))
  }

  private def pruneBlock(pBlock: PBlock)(implicit crucialNodes: Set[DependencyAnalysisNode]) = {
    PBlock(pBlock.stmts.map(pruneStmt))
  }

  private def pruneStmt(pStmt: PStatement)(implicit crucialNodes: Set[DependencyAnalysisNode]): PStatement = {
    pStmt match {
      case declaration: PDeclaration => declaration

      case PExpressionStmt(exp) => ???
      case PAssignment(right, left) => ???
      case PAssignmentWithOp(right, op, left) => ???

      case PIfStmt(ifs, els) => PIfStmt(ifs.map(pruneIfClause), els.map(pruneBlock))
      case PExprSwitchStmt(pre, exp, cases, dflt) => ???
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => ???
      case PForStmt(pre, cond, post, spec, body) => ???
      case PAssForRange(range, ass, spec, body) => ???
      case PSendStmt(channel, msg) => ???
      case PShortForRange(range, shorts, addressable, spec, body) => ???
      case PDeferStmt(exp) => ???
      case pBlock: PBlock => pruneBlock(pBlock)
      case PSeq(stmts) => PSeq(stmts.map(pruneStmt))

      case PLabeledStmt(label, stmt) => PLabeledStmt(label, pruneStmt(stmt))

      case PGoStmt(exp) => ???
      case PDeferStmt(exp) => ???
      case PSelectStmt(send, rec, aRec, sRec, dflt) => ???
      case PReturn(exps) => ???
      case POutline(body, spec) => ???
      case PClosureImplProof(impl, block) => ???

      case PExplicitGhostStatement(actual) => ???
      case PAssert(exp) => ???
      case PRefute(exp) => ???
      case PAssume(exp) => ???
      case PExhale(exp) => ???
      case PInhale(exp) => ???
      case PFold(exp) => ???
      case PUnfold(exp) => ???
      case POpenDupPkgInv() => ???
      case PPackageWand(wand, proofScript) => ???
      case PApplyWand(wand) => ???
      case PMatchStatement(exp, clauses, strict) => ???
    }
  }

  private def prune(pnode: PNode)(implicit crucialNodes: Set[DependencyAnalysisNode]): PNode = {
    pnode match {
      case scope: PScope => scope
      case PPackage(packageClause, programs, positions, info) => PPackage(packageClause, programs.map(pruneProgram), positions, info)
      case pProgram: PProgram => pruneProgram(pProgram)
      case inv: PPkgInvariant => inv
      case member: PMember => pruneMembers(member)
      case statement: PStatement => ???
      case PIfClause(pre, condition, body) => ???
      case clause: PExprSwitchClause => ???
      case clause: PTypeSwitchClause => ???
      case expression: PExpression => ???
      case clause: PStructClause => ???
      case _ => pnode
    }
  }

  override def getPrunedProgram(crucialNodes: Set[DependencyAnalysisNode], program: Program): (Program, Double) = {
    throw new Exception("Pruning of Gobra programs is not yet supported.")
  }

  override def pruneProgramAndExport(crucialNodes: Set[DependencyAnalysisNode], program: ast.Program , exportFileName: String): Unit = {
    throw new Exception("Pruning of Gobra programs is not yet supported.")
  }

}

object GobraDependencyGraphInterpreter {
  def convertFromDependencyGraphInterpreter(interpreter: AbstractDependencyGraphInterpreter, typeInfo: TypeInfo): GobraDependencyGraphInterpreter = {
    interpreter match {
      case interpreter: DependencyGraphInterpreter => new GobraDependencyGraphInterpreter(transformToGobraDependencyGraph(interpreter.getGraph, typeInfo), typeInfo)
      case _ => throw new Exception(s"Unknown dependency graph interpreter $interpreter")
    }
  }

  /*
    Adds (potentially missing) edges between Gobra nodes belonging to the same top-level statement or expression.
    This is necessary because the position stored in the lower level nodes is more fine-grained than what we need for the dependency analysis.
    Using this position directly would lead to unsound dependency results.
   */
  private def transformToGobraDependencyGraph(graph: ReadOnlyDependencyGraph, typeInfo: TypeInfo): DependencyGraph = {

    def addEdgesForSubnodes(newGraph: DependencyGraph): Unit = {
      val gobraNodes = identifyGobraNodes(typeInfo)
      val allNodes = graph.getNodes.groupBy(_.sourceInfo.getTopLevelSource.getPosition)

      gobraNodes.map(gNode => allNodes.filter { case (pos, _) => pos match {
          case p: AbstractSourcePosition => gNode.start <= p.start && (p.end.isEmpty || gNode.end.isEmpty || p.end.get <= gNode.end.get)
          case _ => false
        }})
        .filter(_.nonEmpty)
        .foreach(relevantNodes => {
          val (parentNodePos, parentNodes) = relevantNodes.maxBy(node => {
            val p = node._1.asInstanceOf[AbstractSourcePosition]
            (p.end.getOrElse(p.start).line - p.start.line, p.end.getOrElse(p.start).column - p.start.column)
          })
          relevantNodes.filterNot(_._1.equals(parentNodePos))
            .foreach { case (_, nodes) =>
              // add edges from subnodes to parent node
              newGraph.addEdges(nodes.map(_.id), parentNodes.map(_.id))
              newGraph.addEdges(parentNodes.map(_.id), nodes.map(_.id)) // TODO ake: is this necessary?
            }
        })
    }

    val newGraph  = new DependencyGraph()
    newGraph.addNodes(graph.getNodes)
    newGraph.setEdges(graph.getDirectEdges)
    addEdgesForSubnodes(newGraph)

    newGraph
  }

  /*
    Identifies the desired Gobra nodes. Currently, it guarantees that a node does not have subnodes which is necessary
    for soundness of the dependency analysis results.
   */
  private def identifyGobraNodes(typeInfo: TypeInfo): Iterable[AbstractSourcePosition] = {
    val positionManager = typeInfo.tree.originalRoot.positions
    identifyGobraNodes(typeInfo.tree.originalRoot)(positionManager)
  }

  // TODO ake: check if complete, what to do with varDecls, parameter decls, predicates, ...
  private def identifyGobraNodes(pNode: PNode)(implicit positionManager: PositionManager): Iterable[AbstractSourcePosition] = {
    pNode match {
      case PPackage(packageClause, programs, positions, info) => programs.flatMap(identifyGobraNodes)
      case PProgram(packageClause, pkgInvariants, initPosts, imports, friends, declarations) => pkgInvariants.flatMap(identifyGobraNodes) ++ declarations.flatMap(identifyGobraNodes)
      case PPreamble(packageClause, pkgInvariants, initPosts, imports, friends, positions) => pkgInvariants.flatMap(identifyGobraNodes)
      case PPkgInvariant(inv, _) => identifyGobraNodes(inv)
      case PFriendPkgDecl(_, assertion) => identifyGobraNodes(assertion)
      case PConstDecl(specs) => specs.flatMap(identifyGobraNodes)
      case PFunctionDecl(id, args, result, spec, body) => identifyGobraNodes(spec) ++ body.map(_._2.stmts.flatMap(identifyGobraNodes)).getOrElse(Set.empty)
      case PMethodDecl(id, receiver, args, result, spec, body) => identifyGobraNodes(spec) ++ body.map(_._2.stmts.flatMap(identifyGobraNodes)).getOrElse(Set.empty)
      case PIfStmt(ifs, els) => ifs.flatMap(identifyGobraNodes) ++ els.map(identifyGobraNodes).getOrElse(Set.empty)
      case PExprSwitchStmt(pre, exp, cases, dflt) => cases.flatMap(identifyGobraNodes) ++ dflt.flatMap(identifyGobraNodes) ++ pre.map(identifyGobraNodes).getOrElse(Set.empty)
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => cases.flatMap(identifyGobraNodes) ++ dflt.flatMap(identifyGobraNodes) ++ pre.map(identifyGobraNodes).getOrElse(Set.empty)
      case PForStmt(pre, cond, post, spec, body) => pre.map(identifyGobraNodes).getOrElse(Set.empty) ++ identifyGobraNodes(cond) ++ post.map(identifyGobraNodes).getOrElse(Set.empty) ++ identifyGobraNodes(spec) ++ body.stmts.flatMap(identifyGobraNodes)
      case PAssForRange(range, ass, spec, body) => identifyGobraNodes(spec) ++ body.stmts.flatMap(identifyGobraNodes)
      case PShortForRange(range, shorts, addressable, spec, body) => identifyGobraNodes(spec) ++ body.stmts.flatMap(identifyGobraNodes)
      case PBlock(stmts) => stmts.flatMap(identifyGobraNodes)
      case PSeq(stmts) => stmts.flatMap(identifyGobraNodes)
      case POutline(body, spec) => identifyGobraNodes(spec) ++ identifyGobraNodes(body)
      case PClosureImplProof(impl, block) => identifyGobraNodes(impl) ++ identifyGobraNodes(block)
      case PImplementationProof(subT, superT, alias, memberProofs) => memberProofs.flatMap(identifyGobraNodes)
      case PMethodImplementationProof(id, receiver, args, result, isPure, body) => body.map(_._2.stmts.flatMap(identifyGobraNodes)).getOrElse(Set.empty)
      case PFunctionSpec(pres, preserves, posts, terminationMeasures, _, _, _, _, _) => pres.flatMap(identifyGobraNodes) ++ preserves.flatMap(identifyGobraNodes) ++ posts.flatMap(identifyGobraNodes) ++ terminationMeasures.flatMap(identifyGobraNodes)
      case PLoopSpec(invs, terminationMeasure) => invs.flatMap(identifyGobraNodes) ++ terminationMeasure.map(identifyGobraNodes).getOrElse(Set.empty)
      case PMethodSig(id, args, result, spec, isGhost) => identifyGobraNodes(spec)
      case PWildcardMeasure(cond) => cond.map(identifyGobraNodes).getOrElse(Set.empty)
      case PTupleTerminationMeasure(tuple, cond) => tuple.flatMap(identifyGobraNodes) ++ cond.map(identifyGobraNodes).getOrElse(Set.empty)


      case PSelectStmt(send, rec, aRec, sRec, dflt) => Vector.empty // TODO
      case PMatchStatement(exp, clauses, strict) => clauses.flatMap(identifyGobraNodes)
      case PIfClause(pre, condition, body) => pre.map(identifyGobraNodes).getOrElse(Set.empty) ++ identifyGobraNodes(condition) ++ body.stmts.flatMap(identifyGobraNodes)
      case PExprSwitchDflt(body) => body.stmts.flatMap(identifyGobraNodes)
      case PExprSwitchCase(left, body) => left.flatMap(identifyGobraNodes) ++ body.stmts.flatMap(identifyGobraNodes)
      case PTypeSwitchDflt(body) => body.stmts.flatMap(identifyGobraNodes)
      case PTypeSwitchCase(left, body) => left.flatMap(identifyGobraNodes) ++ body.stmts.flatMap(identifyGobraNodes)

      case PSelectDflt(body) => body.stmts.flatMap(identifyGobraNodes)
      // TODO ake: what to do with these nodes?
//      case PSelectSend(send, body) => ???
//      case PSelectRecv(recv, body) => ???
//      case PSelectAssRecv(recv, ass, body) => ???
//      case PSelectShortRecv(recv, shorts, body) => ???
//      case PClosureDecl(args, result, spec, body) => ???
//
//
//      case misc: PMisc => misc match {
//        case misc: PShortCircuitMisc => misc match {
//          case PLiteralValue(elems) => ???
//          case PKeyedElement(key, exp) => ???
//          case compositeVal: PCompositeVal => ???
//        }
//      }
      case PMatchStmtCase(pattern, stmt, default) => stmt.flatMap(identifyGobraNodes)

      // ensure dependencies are determine on conjunct-level
      case PAnd(left, right) => identifyGobraNodes(left) ++ identifyGobraNodes(right)
      // base case: we arrived at a "primitive" statement or expression. This is the granularity level of the analysis.
      case _ => {
        val start = positionManager.positions.getStart(pNode).get
        val end = positionManager.positions.getFinish(pNode).get
        val sourcePosition = TranslatedPosition(positionManager.translate(start, end))
        Set(sourcePosition)
      }
    }
  }
}


