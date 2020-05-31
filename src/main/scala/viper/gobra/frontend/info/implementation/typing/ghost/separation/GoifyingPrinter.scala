package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignModi}
import viper.gobra.util.Violation


/**
  * Whole spec of a function or method which needs goification.
  */
case class DeclarationSpec(ghostParams: Vector[PParameter], ghostResults: PResult, spec: PFunctionSpec)


class GoifyingPrinter(classifier: GhostClassifier) extends DefaultPrettyPrinter {
  private val specComment: String = "//@"
  private def prefix[T](vec: Vector[T]): Doc = if (vec.isEmpty) emptyDoc else space <> "with:"

  /**
    * Vector containing the full function invocations.
    * This is used to reconstruct function invocations when ghost arguments
    * were in the original invocation.
    */
  private var fullFunctionInvocations: Vector[PInvoke] = Vector()

  /**
    * Shows the Goified version of the full function invocation arguments.
    */
  def showFullFunctionInvocations: Doc = {
    val doc =
      if (fullFunctionInvocations.isEmpty) emptyDoc
      //else specComment <+> "ghost-invocations:" <+> showList(fullFunctionInvocations)(super.showExpr(_))
      else specComment <+> "ghost-invocations:" <+>
        showList(fullFunctionInvocations)({ case PInvoke(base, args) => super.showExprOrType(base) <+> "->" <+> parens(showExprList(args))})
    fullFunctionInvocations = Vector()
    doc
  }

  /**
    * Shows the Goified version of the function / method specification
    */
  override def showSpec(spec: PSpecification): Doc = spec match {
    case PFunctionSpec(pres, posts, isPure) =>
      (if (isPure) specComment <+> showPure else emptyDoc) <>
      hcat(pres map (p => specComment <+> showPre(p) <+> showFullFunctionInvocations <> line)) <>
      hcat(posts map (p => specComment <+> showPost(p) <+> showFullFunctionInvocations <> line))

    case PLoopSpec(inv) =>
      hcat(inv map (p => specComment <+> showInv(p) <+> showFullFunctionInvocations <> line))
  }

  /**
    * Shows the ghost parameters, the ghost results and the Goified version
    * of the function / method specification.
    */
  def showDeclarationSpec(spec: DeclarationSpec): Doc = spec match {
    case DeclarationSpec(ghostParams, ghostResults, spec) =>
      (if (ghostParams.isEmpty) emptyDoc else specComment <+> "ghost-parameters:" <+> showParameterList(ghostParams) <> line) <>
      (if (ghostResults.outs.isEmpty) emptyDoc else specComment <+> "ghost-results:" <+> showResult(ghostResults) <> line) <>
      showSpec(spec)
  }

  /**
    * Shows a list of addressable variables.
    */
  def showAddressableVars(vars: Vector[PIdnUnk]): Doc =
      (if (vars.isEmpty) emptyDoc else specComment <+> "addressable-variables:" <+> showList(vars)(showId(_)) <> line)


  /**
    * Helper methods to get the parameters and results filtered (without ghost)
    * and also unfiltered (only ghost)
    */
  private def filterParamList[T <: PParameter](paras: Vector[T]): Vector[T] =
    paras.filter(!classifier.isParamGhost(_))

  private def unfilterParamList[T <: PParameter](paras: Vector[T]): Vector[T] =
    paras.filter(classifier.isParamGhost(_))

  private def filterResult(res: PResult): PResult = {
    val aOuts = res.outs.filter(!classifier.isParamGhost(_))
    PResult(aOuts)
  }

  private def unfilterResult(res: PResult): PResult = {
    val aOuts = res.outs.filter(classifier.isParamGhost(_))
    PResult(aOuts)
  }

  
  /**
    * Shows the Goified version of a program member.
    */
  override def showMember(mem: PMember): Doc = mem match {
    case PMethodDecl(id, rec, args, res, spec, body) =>
      showDeclarationSpec(DeclarationSpec(unfilterParamList(args), unfilterResult(res), spec)) <>
      super.showMember(PMethodDecl(id, rec, filterParamList(args), filterResult(res), PFunctionSpec(Vector.empty, Vector.empty), body))

    case PFunctionDecl(id, args, res, spec, body) =>
      showDeclarationSpec(DeclarationSpec(unfilterParamList(args), unfilterResult(res), spec)) <>
      super.showMember(PFunctionDecl(id, filterParamList(args), filterResult(res), PFunctionSpec(Vector.empty, Vector.empty), body))

    case PFPredicateDecl(id, args, body) =>
      specComment <+> showFPredicateDeclHeader(id, args) <>
      opt(body)(b => space <> blockClosingBraceGoified(specComment <+> showExpr(b) <+> showFullFunctionInvocations))

    case PMPredicateDecl(id, recv, args, body) =>
      specComment <+> showMPredicateDeclHeader(id, recv, args) <>
      opt(body)(b => space <> blockClosingBraceGoified(specComment <+> showExpr(b) <+> showFullFunctionInvocations))

    case m if classifier.isMemberGhost(m) => specComment <+> super.showMember(m)
    case m => super.showMember(m)
  }


  /**
    * Shows a ghost statement in the goified version with a given prefix.
    */
  def showGhostStmt(stmt: PStatement, prefix: Doc): Doc = specComment <> prefix <+> super.showStmt(stmt)

  override def showStmt(stmt: PStatement): Doc = (stmt match {

    case PForStmt(pre, cond, post, _, body) =>
      super.showStmt(PForStmt(pre, cond, post, PLoopSpec(Vector.empty), body))

    case PAssignment(right, left) =>
      StrictAssignModi(left.size, right.size) match {
        case AssignMode.Single =>
          val (aRight, aLeft) = right.zip(left).filter(p => !classifier.isExprGhost(p._2)).unzip
          val (ghostRight, ghostLeft) = right.zip(left).filter(p => classifier.isExprGhost(p._2)).unzip

          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PAssignment(aRight, aLeft))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PAssignment(ghostRight, ghostLeft), prefix(aLeft)))
          
        case AssignMode.Multi =>
          val aLeft = left.filter(!classifier.isExprGhost(_))
          val ghostLeft = left.filter(classifier.isExprGhost(_))

          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PAssignment(right, aLeft))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PAssignment(right, ghostLeft), prefix(aLeft)))

        case AssignMode.Error => errorMsg
      }

    case PShortVarDecl(right, left, addressable) =>
      StrictAssignModi(left.size, right.size) match {
        case AssignMode.Single =>
          val (aRight, aLeft) = right.zip(left).filter(p => !classifier.isIdGhost(p._2)).unzip
          // List of all non-ghost addressable variables.
          val aAddressableVars = left.zip(addressable).filter(p => !classifier.isIdGhost(p._1) && p._2).map(_._1)
          val (ghostRight, ghostLeft) = right.zip(left).filter(p => classifier.isIdGhost(p._2)).unzip
          // Boolean vector of whether ghost variables are addressable or not.
          val ghostAddressable = left.zip(addressable).filter(p => classifier.isIdGhost(p._1)).map(_._2)

          showAddressableVars(aAddressableVars) <>
          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PShortVarDecl(aRight, aLeft, aLeft.map(_ => false)))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PShortVarDecl(ghostRight, ghostLeft, ghostAddressable), prefix(aLeft)))

        case AssignMode.Multi =>
          val aLeft = left.filter(!classifier.isIdGhost(_))
          // List of all non-ghost addressable variables.
          val aAddressableVars = left.zip(addressable).filter(p => !classifier.isIdGhost(p._1)).map(_._1)
          val ghostLeft = left.filter(classifier.isIdGhost(_))
          // Boolean vector of whether ghost variables are addressable or not.
          val ghostAddressable = left.zip(addressable).filter(p => classifier.isIdGhost(p._1)).map(_._2)
          
          showAddressableVars(aAddressableVars) <>
          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PShortVarDecl(right, aLeft, aLeft.map(_ => false)))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PShortVarDecl(right, ghostLeft, ghostAddressable), prefix(aLeft)))

        case AssignMode.Error => errorMsg
      }

    case n@ PReturn(right) =>
      val gt = classifier.expectedReturnGhostTyping(n)
      val aRight = right.zip(gt.toTuple).filter(!_._2).map(_._1)
      val ghostRight = right.zip(gt.toTuple).filter(_._2).map(_._1)
      
      (if (aRight.isEmpty) emptyDoc else super.showStmt(PReturn(aRight))) <>
      (if (ghostRight.isEmpty) emptyDoc else showGhostStmt(PReturn(ghostRight), prefix(aRight)))

    case s if classifier.isStmtGhost(s) => showGhostStmt(stmt, emptyDoc)
    case s => super.showStmt(stmt)

  }) <+> showFullFunctionInvocations

  

  override def showExpr(expr: PExpression): Doc = expr match {

    case n: PInvoke =>
      val gt = classifier.expectedArgGhostTyping(n)
      val aArgs = n.args.zip(gt.toTuple).filter(!_._2).map(_._1)
      val ghostArgs = n.args.zip(gt.toTuple).filter(_._2).map(_._1)

      if (!ghostArgs.isEmpty) fullFunctionInvocations = fullFunctionInvocations :+ n.copy(args = ghostArgs)

      super.showExpr(n.copy(args = aArgs))

    case e: PActualExprProofAnnotation => showExpr(e.op)
    // TODO: see if this works with just commenting out
    //case e if classifier.isExprGhost(e) => "<removed expr>" // should not be printed
    case e => super.showExpr(e)
  }

  /**
    * Shows ghost types in the goified version.
    */
  def showGhostType(typ: PType): Doc = specComment <+> super.showType(typ)

  override def showType(typ: PType): Doc = typ match {

    case PStructType(clauses) =>
      val ghostClauses = unfilterStructClauses(clauses)

      super.showType(PStructType(filterStructClauses(clauses))) <+>
      (if (ghostClauses.isEmpty) emptyDoc else showGhostType(PStructType(ghostClauses)))

    case PInterfaceType(embedded, mspecs, pspecs) =>
      val ghostEmbedded = unfilterInterfaceClause(embedded)
      val ghostMspecs = unfilterInterfaceClause(mspecs)
      val ghostPspecs = unfilterInterfaceClause(pspecs)

      "interface" <+> block(
        ssep(filterInterfaceClause(embedded) map showInterfaceClause, line) <>
        (if (ghostEmbedded.isEmpty) emptyDoc else ssep(ghostEmbedded map (specComment <+> showInterfaceClause(_)), line)) <>

        ssep(filterInterfaceClause(mspecs) map showInterfaceClause, line) <>
        (if (ghostMspecs.isEmpty) emptyDoc else ssep(ghostMspecs map (specComment <+> showInterfaceClause(_)), line)) <>

        ssep(filterInterfaceClause(pspecs) map showInterfaceClause, line) <>
        (if (ghostPspecs.isEmpty) emptyDoc else ssep(ghostPspecs map (specComment <+> showInterfaceClause(_)), line))
      )

    case t => super.showType(t)
  }

  private def filterStructClauses[T <: PStructClause](cl: Vector[T]): Vector[T] =
    cl.filter(!classifier.isStructClauseGhost(_))

  private def unfilterStructClauses[T <: PStructClause](cl: Vector[T]): Vector[T] =
    cl.filter(classifier.isStructClauseGhost(_))

  private def filterInterfaceClause[T <: PInterfaceClause](cl: Vector[T]): Vector[T] =
    cl.filter(!classifier.isInterfaceClauseGhost(_))

  private def unfilterInterfaceClause[T <: PInterfaceClause](cl: Vector[T]): Vector[T] =
    cl.filter(classifier.isInterfaceClauseGhost(_))


  private def errorMsg: Nothing = Violation.violation("GoifyingPrinter has to be run after the type check")



}


