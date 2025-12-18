package viper.gobra.dependencyAnalysis

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.silicon.dependencyAnalysis.{DependencyAnalysisNode, DependencyGraphInterpreter, ReadOnlyDependencyGraph}
import viper.silver.ast
import viper.silver.ast.Program



class GobraDependencyGraphInterpreter(dependencyGraph: ReadOnlyDependencyGraph, typeInfo: TypeInfo) extends DependencyGraphInterpreter("gobra", dependencyGraph, member=None) {

  def getPrunedProgram(crucialNodes: Set[DependencyAnalysisNode]): (PPackage, Double) = {

    // TODO ake: implement pruning for Gobra programs

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




