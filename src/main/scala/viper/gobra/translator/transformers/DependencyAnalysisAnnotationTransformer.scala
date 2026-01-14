package viper.gobra.translator.transformers

import viper.gobra.ast.frontend.PNode
import viper.gobra.ast.{frontend => gobra}
import viper.gobra.backend.BackendVerifier
import viper.gobra.dependencyAnalysis.GobraDependencyAnalysisAggregator
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier
import viper.gobra.reporting.Source.Verifier.GobraDependencyAnalysisInfo
import viper.silver.ast._
import viper.silver.ast.utility.ViperStrategy
import viper.silver.verifier.AbstractError
import viper.silver.{ast => vpr}

class DependencyAnalysisAnnotationTransformer(typeInfo: TypeInfo) extends ViperTransformer {

  private val gobraNodes: Iterable[GobraDependencyAnalysisInfo] = GobraDependencyAnalysisAggregator.identifyGobraNodes(typeInfo)
  private val positions = typeInfo.tree.root.positions.positions

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    val programWithAnalysisSources = addDependencyAnalysisSourceInfo(task.program)
//    val programWithAnalysisAnnotation = addDependencyAnalysisAnnotation(programWithAnalysisSources)
    Right(task.copy(program = programWithAnalysisSources))
  }

  /**
    * Adds GobraDependencyAnalysisInfos to the Viper nodes. This info is used to merge lower-level dependency nodes
    * into Gobra dependency nodes such that soundness of transitive dependencies is guaranteed. Soundness depends on the
    * correctness of [[GobraDependencyAnalysisAggregator.identifyGobraNodes]].
   */
  private def addDependencyAnalysisSourceInfo(p: vpr.Program): vpr.Program = {
    ViperStrategy.Slim({
      case member: vpr.Member =>
        val newInfo = getNewInfo(member, member.pos, {
          case _: gobra.PFunctionDecl | _: gobra.PMethodDecl
               | _: gobra.PDomainType | _: gobra.PPredType
          => NoInfo
          case _ => disableDependencyAnalysis
        }, disableDependencyAnalysis)
        member.withMeta((member.pos, newInfo, member.errT))

      case stmt: vpr.Stmt =>
        val sourceInfo = stmt.info.getUniqueInfo[Verifier.Info]
        val depInfo = getDependencyAnalysisInfo(sourceInfo)
        val newInfo = if(depInfo.isDefined) MakeInfoPair(depInfo.get, stmt.info) else stmt.info
        stmt.withMeta(stmt.pos, newInfo, stmt.errT)
      case exp: vpr.Exp =>
        val sourceInfo = exp.info.getUniqueInfo[Verifier.Info]
        val depInfo = getDependencyAnalysisInfo(sourceInfo)
        val newInfo = if(depInfo.isDefined) MakeInfoPair(depInfo.get, exp.info) else exp.info
        exp.withMeta(exp.pos, newInfo, exp.errT)
    }).forceCopy().execute(p)
  }

  private def getDependencyAnalysisInfo(sourceInfo: Option[Verifier.Info]): Option[GobraDependencyAnalysisInfo] = {
    if (sourceInfo.isEmpty) return None
    getDependencyAnalysisInfo(sourceInfo.get.pnode)
  }

  private def getDependencyAnalysisInfo(pNode: PNode): Option[GobraDependencyAnalysisInfo] = {
    try {
      var pNodes = Vector(pNode)
      val gNodes = gobraNodes.map(n => ((n.getPNode, n.getPosition), n)).toMap
      var gNodeCandidates = pNodes.flatMap(pN => gNodes.get((pN, getPosition(pN))))
      while (pNodes.nonEmpty && gNodeCandidates.isEmpty) {
        pNodes = pNodes.flatMap(node => typeInfo.tree.parent(node))
        gNodeCandidates = pNodes.flatMap(pN => gNodes.get((pN, getPosition(pN))))
      }

      gNodeCandidates.headOption
    } catch {
      case _ => None
    }
  }


  // TODO ake: duplicate! (see GobraDependencyAnalysisAggregator)
  private def getPosition(pNode: PNode): TranslatedPosition = {
    val start = positions.getStart(pNode).get
    val end = positions.getFinish(pNode).get
    val sourcePosition = TranslatedPosition(typeInfo.tree.root.positions.translate(start, end))
    sourcePosition
  }

  /**
   * Adds assumption type information to the Viper nodes of program p such that they resemble the assumption type
   * expected on the Gobra level (i.e. the assumption type associated with the Gobra node).
   */
  private def addDependencyAnalysisAnnotation(p: vpr.Program): vpr.Program = {
    ViperStrategy.Slim({
      case aInput @ (_: vpr.Inhale | _: vpr.Assume) =>
        val a = aInput.asInstanceOf[vpr.Stmt]
        val newInfo = getNewInfo(a, a.pos, {
            case _: gobra.PAssume | _: gobra.PInhale  =>
              NoInfo
            case _ =>
              implicitAnnotation
          }, implicitAnnotation)
        a.withMeta((a.pos, newInfo, a.errT))



      case seqn: vpr.Seqn =>
        val annotationInfo = getAnalysisInfoAnnotation(seqn, seqn.pos, {
          case _: gobra.PAssume | _: gobra.PInhale  =>
            explicitAnnotation
          case _ => NoInfo
        }, NoInfo)
        attachAnalysisInfoToSeqn(seqn, annotationInfo)

      case stmt: vpr.Stmt =>
        val newInfo = getNewInfo(stmt, stmt.pos, {
          case _: gobra.PAssume | _: gobra.PInhale =>
            explicitAnnotation
          case _ => NoInfo
        }, NoInfo)
        stmt.withMeta((stmt.pos, newInfo, stmt.errT))

    }).forceCopy().execute(p)
  }


  private def mergeInfoOptionally(oldInfo: vpr.Info, newInfo: vpr.Info): vpr.Info = {
    newInfo match {
      case _: AnnotationInfo =>
        if(oldInfo.getUniqueInfo[AnnotationInfo].isDefined) oldInfo
        else MakeInfoPair(oldInfo, newInfo)
      case _ => oldInfo
    }
  }

  private def attachAnalysisInfoToSeqn(seqn: vpr.Seqn, analysisInfo: vpr.Info): vpr.Seqn = analysisInfo match {
      case NoInfo => seqn
      case _ => vpr.Seqn(seqn.ss.map(s => s.withMeta((s.pos, mergeInfoOptionally(s.info, analysisInfo), s.errT))),
        seqn.scopedSeqnDeclarations)(seqn.pos, seqn.info, seqn.errT)
    }

  private def getSourceFileOpt(pos: vpr.Position): Option[String] = {
    pos match {
      case position: AbstractSourcePosition =>
        Some(position.file.getFileName.toString)
      case _ => None
    }
  }

  private def getAnalysisInfoAnnotation(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val sourceInfo = node.info.getUniqueInfo[Verifier.Info]
    if(sourceInfo.isDefined)
      getAnalysisInfoAnnotation(sourceInfo.get.pnode, pos, pNodeMapper, default)
    else
      default
  }

  private def getAnalysisInfoAnnotation(pNode: PNode, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val sourceFileOpt = getSourceFileOpt(pos)
    if (sourceFileOpt.exists(s => !s.equals("builtin.gobra"))) {
      pNodeMapper(pNode)
    } else {
      default
    }
  }

  private def getNewInfo(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val newInfo = getAnalysisInfoAnnotation(node, pos, pNodeMapper, default)
    mergeInfoOptionally(node.info, newInfo)
  }

  private def getNewExps(es: Seq[vpr.Exp]): Seq[vpr.Exp] = {
    es map (e => {
      val newInfo = getNewInfo(e, e.pos, {
        case _: gobra.PDeclaration | _: gobra.PNamedParameter => implicitAnnotation
        case _ => NoInfo}, NoInfo)
        e.withMeta((e.pos, newInfo, e.errT))
    })
  }

  private def explicitAnnotation: AnnotationInfo = {
    AnnotationInfo(Map(("assumptionType", List("Explicit"))))
  }

  private def implicitAnnotation: AnnotationInfo = {
    AnnotationInfo(Map(("assumptionType", List("Implicit"))))
  }

  private def disableDependencyAnalysis: AnnotationInfo = {
    AnnotationInfo(Map(("enableDependencyAnalysis", List("false"))))
  }
}
