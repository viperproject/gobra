package viper.gobra.translator.transformers

import viper.gobra.ast.{frontend => gobra}
import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.Source.Verifier
import viper.silver.ast.utility.ViperStrategy
import viper.silver.ast.{AbstractSourcePosition, AnnotationInfo, MakeInfoPair, NoInfo}
import viper.silver.verifier.AbstractError
import viper.silver.{ast => vpr}

class AssumptionAnalysisAnnotationTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    val programWithAnalysisAnnotation = addAssumptionAnalysisAnnotation(task.program)
    Right(task.copy(program = programWithAnalysisAnnotation))
  }

  // existing assumption analysis annotations are preserved
  private def addAssumptionAnalysisAnnotation(p: vpr.Program): vpr.Program = {
    ViperStrategy.Slim({
      case aInput @ (_: vpr.Inhale | _: vpr.Assume) =>
        val a = aInput.asInstanceOf[vpr.Stmt]
        val newInfo = getNewInfo(a, a.pos, {
            case _: gobra.PAssume | _: gobra.PInhale  =>
              NoInfo
            case _                                    =>
              implicitAnnotation
          }, implicitAnnotation)
        a.withMeta((a.pos, newInfo, a.errT))

      case dInput @ (_: vpr.Domain | _: vpr.Function | _: vpr.Predicate) =>
        val d = dInput.asInstanceOf[vpr.Member]
        val newInfo = getNewInfo(d, d.pos, {
          case _: gobra.PFunctionDecl | _: gobra.PMethodDecl
            | _: gobra.PDomainType | _: gobra.PPredType
              => NoInfo
          case _ => disableAssumptionAnalysis
        }, disableAssumptionAnalysis)
        d.withMeta((d.pos, newInfo, d.errT))

      case meth: vpr.Method =>
        val newInfo = getNewInfo(meth, meth.pos, {
          case _: gobra.PFunctionDecl | _: gobra.PMethodDecl
               | _: gobra.PDomainType | _: gobra.PPredType
          => NoInfo
          case _ => disableAssumptionAnalysis
        }, disableAssumptionAnalysis)
        vpr.Method(meth.name, meth.formalArgs, meth.formalReturns, getNewExps(meth.pres), meth.posts, meth.body)(meth.pos, newInfo, meth.errT)


      case seqn: vpr.Seqn =>
        val annotationInfo = getAnalysisInfoAnnotation(seqn, seqn.pos, {
          case _: gobra.PAssume | _: gobra.PInhale =>
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

  private def getAnalysisInfoAnnotation(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: (gobra.PNode => vpr.Info), default: vpr.Info): vpr.Info = {
    val sourceInfo = node.info.getUniqueInfo[Verifier.Info]
    val sourceFileOpt = getSourceFileOpt(pos)
    if (sourceInfo.isDefined && sourceFileOpt.exists(s => !s.equals("builtin.gobra"))) {
      pNodeMapper(sourceInfo.get.pnode)
    } else {
      default
    }
  }

  private def getNewInfo(node: vpr.Infoed, pos: vpr.Position, pNodeMapper: (gobra.PNode => vpr.Info), default: vpr.Info): vpr.Info = {
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

  private def disableAssumptionAnalysis: AnnotationInfo = {
    AnnotationInfo(Map(("enableAssumptionAnalysis", List("false"))))
  }
}
