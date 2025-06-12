package viper.gobra.translator.transformers

import viper.gobra.ast.frontend.{PAssume, PInhale}
import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.Source.Verifier
import viper.silver.ast.utility.ViperStrategy
import viper.silver.ast._
import viper.silver.verifier.AbstractError

class AssumptionAnalysisAnnotationTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    val programWithAnalysisAnnotation = addAssumptionAnalysisAnnotation(task.program)
    Right(task.copy(program = programWithAnalysisAnnotation))
  }


  private def addAssumptionAnalysisAnnotation(p: Program): Program = {
    ViperStrategy.Slim({
      case aInput @ ( _: Inhale | _: Assume) =>
        val a = aInput.asInstanceOf[Stmt]
        val sourceInfo = a.info.getUniqueInfo[Verifier.Info]
        if (sourceInfo.isDefined) {
          val annotationInfo = sourceInfo.get.pnode match {
            case _: PAssume | _: PInhale =>
              explicitAnnotation
            case _ =>
              implicitAnnotation
          }
          val nA = a.withMeta((a.pos, MakeInfoPair(a.info, annotationInfo), a.errT))
          nA
        } else {
          a
        }
    }).forceCopy().execute(p)
  }

  private def explicitAnnotation: AnnotationInfo = {
    AnnotationInfo(Map(("assumptionType", List("Explicit"))))
  }

  private def implicitAnnotation: AnnotationInfo = {
    AnnotationInfo(Map(("assumptionType", List("Implicit"))))
  }
}
