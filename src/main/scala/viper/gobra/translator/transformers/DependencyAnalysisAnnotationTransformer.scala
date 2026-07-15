package viper.gobra.translator.transformers

import viper.gobra.ast.frontend.PNode
import viper.gobra.ast.{frontend => gobra}
import viper.gobra.backend.BackendVerifier
import viper.gobra.dependencyAnalysis.{GobraDependencyAnalysisHelper, GobraDependencyAnalysisSourceInfo}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.reporting.Source.Verifier
import viper.silver.ast._
import viper.silver.ast.utility.ViperStrategy
import viper.silver.dependencyAnalysis.{DependencyAnalysisSourceInfo, DependencyTypeInfo}
import viper.silver.verifier.AbstractError
import viper.silver.{ast, ast => vpr}

import java.nio.file.{Path, Paths}
import scala.reflect.ClassTag

class DependencyAnalysisAnnotationTransformer(typeInfo: TypeInfo, config: Config) extends ViperTransformer {

  private lazy val gobraNodes: Iterable[ast.Info] = GobraDependencyAnalysisHelper.identifyGobraNodes(typeInfo)
  private lazy val gNodes = gobraNodes.map(n => {
    val sourceInfo = n.getUniqueInfo[GobraDependencyAnalysisSourceInfo].get
    ((sourceInfo.pNode, sourceInfo.getPosition), n)
  }).toMap
  private val allTypeInfos = typeInfo.getTransitiveTypeInfos().filterNot(_.pkgName.name.contains("builtin")).map(typeInfos => typeInfos.getTypeInfo)
  private lazy val resourceDirectories: Seq[Path] = Seq("builtin", "noaxioms", "stubs") flatMap getResourcesPathOpt

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    if(!config.enableDependencyAnalysis) return Right(task)

    val programWithAnalysisSources = addDependencyAnalysisSourceInfo(task.program)
    Right(task.copy(program = programWithAnalysisSources))
  }


  private def addDependencyAnalysisSourceInfo(p: vpr.Program): vpr.Program = {
    ViperStrategy.Slim({
      case member: vpr.Member =>
        val newInfo = getNewInfo(member, member.pos, {_ => NoInfo}, disableDependencyAnalysis)
        val newInfo2 = getDependencyAnalysisEnhancedInfo(newInfo)
        member.withMeta(member.pos, newInfo2, member.errT) // TODO ake: try to avoid using withMeta
      case stmt: vpr.Stmt =>
        val newInfo = getDependencyAnalysisEnhancedInfo(stmt.info)
        stmt.withMeta(stmt.pos, newInfo, stmt.errT)
      case exp: vpr.Exp =>
        val newInfo = getDependencyAnalysisEnhancedInfo(exp.info)
        exp.withMeta(exp.pos, newInfo, exp.errT)
    }).forceCopy().execute(p)
  }

  private def getDependencyAnalysisEnhancedInfo(oldInfo: Info) = {
    val sourceInfo = oldInfo.getUniqueInfo[Verifier.Info]
    val depInfoOpt = getDependencyAnalysisInfoFromAncestorPNode(sourceInfo)
    if (depInfoOpt.isDefined) {
      val depInfo = depInfoOpt.get
      // do not override existing infos
      val newInfo = attachInfoIfNotExists[DependencyAnalysisSourceInfo](oldInfo, depInfo)
      val resInfo = attachInfoIfNotExists[DependencyTypeInfo](newInfo, depInfo)
      resInfo
    } else
      oldInfo
  }

  private def attachInfoIfNotExists[T <: Info : ClassTag](oldInfo: Info, newInfo: Info) = {
    oldInfo.getUniqueInfo[T] match {
      case Some(_) =>
        oldInfo
      case None    => MakeInfoPair(oldInfo, newInfo.getUniqueInfo[T].getOrElse(NoInfo))
    }
  }

  private def getDependencyAnalysisInfoFromAncestorPNode(sourceInfo: Option[Verifier.Info]): Option[ast.Info] = {
    if (sourceInfo.isEmpty) return None
    val pNode = sourceInfo.get.pnode
    try {
      val typeInfoToUse = allTypeInfos.filter(_.tree.root.positions.positions.getStart(pNode).isDefined).head
      var pNodes = Vector(pNode)
      var gNodeCandidates = pNodes.flatMap(pN => gNodes.get((pN, getPosition(pN, typeInfoToUse))))
      while (pNodes.nonEmpty && gNodeCandidates.isEmpty) {
        pNodes = pNodes.flatMap(node => typeInfoToUse.tree.parent(node))
        gNodeCandidates = pNodes.flatMap(pN => gNodes.get((pN, getPosition(pN, typeInfoToUse))))
      }

      gNodeCandidates.headOption
    } catch {
      case _: Throwable => None
    }
  }

  // TODO ake: duplicate! (see GobraDependencyAnalysisAggregator)
  private def getPosition(pNode: PNode, typeInfoToUse: TypeInfo): TranslatedPosition = {
    val positions = typeInfoToUse.tree.originalRoot.positions
    val start = positions.positions.getStart(pNode).get
    val end = positions.positions.getFinish(pNode).get
    val sourcePosition = TranslatedPosition(positions.translate(start, end))
    sourcePosition
  }

  // TODO ake: review
  private def getAnalysisInfoAnnotation(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val sourceInfo = node.info.getUniqueInfo[Verifier.Info]
    val sourceFileOpt = pos match {
      case position: AbstractSourcePosition =>
        Some(position.file)
      case _ => None
    }

    if (sourceInfo.isDefined && sourceFileOpt.exists(file => !isUnderResources(file)))
      pNodeMapper(sourceInfo.get.pnode)
    else {
      val annotationInfos = node.info.getAllInfos[AnnotationInfo]
      val enableDependencyAnalysisInfos = annotationInfos.filter(i =>
        i.values.contains("enableDependencyAnalysis") && i.values("enableDependencyAnalysis").contains("false"))
      if (enableDependencyAnalysisInfos.nonEmpty) {
        println(s"node has info: $node")
        node.info
      } else
        default
    }
  }

  private def getNewInfo(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: gobra.PNode => vpr.Info, default: vpr.Info): vpr.Info = {
    val newInfo = getAnalysisInfoAnnotation(node, pos, pNodeMapper, default)
    MakeInfoPair(node.info, newInfo)
  }

  private def disableDependencyAnalysis: AnnotationInfo = {
    AnnotationInfo(Map(("enableDependencyAnalysis", List("false"))))
  }

  private def getResourcesPathOpt(resource: String): Option[Path] = {
    Option(Thread.currentThread().getContextClassLoader.getResource(resource)).flatMap { resource =>
      try {
        Some(Paths.get(resource.toURI).toAbsolutePath.normalize())
      } catch {
        case _: Throwable => None
      }
    }
  }

  private def isUnderResources(path: Path): Boolean = {
    val normalizedPath = path.toAbsolutePath.normalize()
    resourceDirectories.exists { resourcesPath =>
      normalizedPath == resourcesPath || normalizedPath.startsWith(resourcesPath)
    }
  }
}
