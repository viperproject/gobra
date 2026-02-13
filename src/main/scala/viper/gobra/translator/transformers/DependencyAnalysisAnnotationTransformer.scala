package viper.gobra.translator.transformers

import viper.gobra.ast.frontend.PNode
import viper.gobra.ast.{frontend => gobra}
import viper.gobra.backend.BackendVerifier
import viper.gobra.dependencyAnalysis.GobraDependencyAnalysisAggregator
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier
import viper.gobra.reporting.Source.Verifier.GobraDependencyAnalysisInfo
import viper.silver.ast._
import viper.silver.ast.utility.ViperStrategy
import viper.silver.verifier.AbstractError
import viper.silver.{ast => vpr}

class DependencyAnalysisAnnotationTransformer(typeInfo: TypeInfo, config: Config) extends ViperTransformer {

  private val gobraNodes: Iterable[GobraDependencyAnalysisInfo] = GobraDependencyAnalysisAggregator.identifyGobraNodes(typeInfo)
  private val positions = typeInfo.tree.root.positions.positions

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    if(!config.enableDependencyAnalysis) return Right(task)

    val programWithAnalysisSources = addDependencyAnalysisSourceInfo(task.program)
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
        val newInfo = getNewInfo(member, member.pos, {_ => NoInfo}, disableDependencyAnalysis)
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

  private def getAnalysisInfoAnnotation(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val sourceInfo = node.info.getUniqueInfo[Verifier.Info]
    val sourceFileOpt = pos match {
      case position: AbstractSourcePosition =>
        Some(position.file.getFileName.toString)
      case _ => None
    }

    if(sourceInfo.isDefined && sourceFileOpt.exists(s => !s.equals("builtin.gobra")))
      pNodeMapper(sourceInfo.get.pnode)
    else
      default
  }

  private def getNewInfo(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val newInfo = getAnalysisInfoAnnotation(node, pos, pNodeMapper, default)
    MakeInfoPair(node.info, newInfo)
  }

  private def disableDependencyAnalysis: AnnotationInfo = {
    AnnotationInfo(Map(("enableDependencyAnalysis", List("false"))))
  }
}
