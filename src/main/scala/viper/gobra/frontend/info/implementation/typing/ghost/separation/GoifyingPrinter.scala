// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.util.{Constants, Violation}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import org.bitbucket.inkytonik.kiama.attribution.Decorators



/**
  * Whole spec of a function or method which needs goification.
  */
case class DeclarationSpec(ghostParams: Vector[PExplicitGhostParameter], ghostResults: Vector[PExplicitGhostParameter], spec: PFunctionSpec)


class GoifyingPrinter(info: TypeInfoImpl) extends DefaultPrettyPrinter {

  val classifier: GhostClassifier = info.asInstanceOf[GhostClassifier]

  /**
    * Used to determine if current expression is already in Goified scope.
    */
  val decorators = new Decorators(info.tree)
  lazy val isInGoifiedScope: PNode => Boolean =
    decorators.down(false){
      case _: PFPredicateDecl | _: PMPredicateDecl | _: PFunctionSpec | _: PGhostStatement | _: PUnfolding  => true
      case m: PMember => classifier.isMemberGhost(m)
      case s: PStatement => classifier.isStmtGhost(s)
    }
  def isWithinGoifiedScope(n: PNode): Boolean = info.tree.parent(n) match {
    case Vector(parent) =>
      val res = isInGoifiedScope(parent)
      res
    case _ =>
      false
  }

  /**
    * Keywords used in Goified files.
    */
  private val ghost_parameters: String = "ghost-parameters:"
  private val ghost_results: String = "ghost-results:"
  private val addressable_variables: String = "addressable:"
  private val with_keyword: String = "with:"
  private val unfolding_keyword: String = "unfolding:"

  private val specComment: Doc = "//@"
  private def blockSpecComment(doc: Doc): Doc = "/*@" <> line <> doc <> line <> "@*/"
  private def inlinedSpecComment(doc: Doc): Doc = "/*@" <+> doc <+> "@*/"

  private def with_prefix[T](vec: Vector[T]): Doc = if (vec.isEmpty) emptyDoc else space <> with_keyword


  /**
    * Helper methods to get the parameters and results filtered (without ghost)
    * and also only ghost
    */
  private def getActualParams[T <: PParameter](paras: Vector[T]): Vector[T] =
    paras.filter(!classifier.isParamGhost(_))

  private def getGhostParams[T <: PParameter](paras: Vector[T]): Vector[PExplicitGhostParameter] =
    paras.filter(classifier.isParamGhost(_)).asInstanceOf[Vector[PExplicitGhostParameter]]

  private def getActualResult(res: PResult): PResult = {
    val aOuts = res.outs.filter(!classifier.isParamGhost(_))
    PResult(aOuts)
  }

  /**
    * Shows the Goified version of the function / method specification
    */
  override def showSpec(spec: PSpecification): Doc = spec match {
    case PFunctionSpec(pres, preserves, posts, measures, backendAnnotations, isPure, isTrusted, isOpaque, isAtomic, opensInvs, mayInit) =>
      (if (isPure) specComment <+> showPure else emptyDoc) <>
      (if (isOpaque) specComment <+> showOpaque else emptyDoc) <>
      (if (opensInvs) specComment <+> showOpensInvs else emptyDoc) <>
      (if (isTrusted) specComment <+> showTrusted else emptyDoc) <>
      (if (mayInit) specComment <+> showMayInit else emptyDoc) <>
      (if (isAtomic) specComment <+> showAtomic else emptyDoc) <>
      hcat(pres map (p => specComment <+> showPre(p) <> line)) <>
      hcat(preserves map (p => specComment <+> showPreserves(p) <> line)) <>
      hcat(posts map (p => specComment <+> showPost(p) <> line)) <>
      hcat(measures map (p => specComment <+> showTerminationMeasure(p) <> line)) <>
      specComment <+> showBackendAnnotations(backendAnnotations) <> line

    case PLoopSpec(inv, measure) =>
      hcat(inv map (p => specComment <+> showInv(p) <> line)) <>
      opt(measure)(p => specComment <+> showTerminationMeasure(p) <> line)
  }

  /**
    * Shows the ghost parameters, the ghost results and the Goified version
    * of the function / method specification.
    */
  def showDeclarationSpec(spec: DeclarationSpec): Doc = spec match {
    case DeclarationSpec(ghostParams, ghostResults, spec) =>
      (if (ghostParams.isEmpty) emptyDoc else specComment <+> ghost_parameters <+> showParameterList(ghostParams.map(_.actual)) <> line) <>
      (if (ghostResults.isEmpty) emptyDoc else specComment <+> ghost_results <+> showParameterList(ghostResults.map(_.actual)) <> line) <>
      showSpec(spec)
  }

  /**
    * Shows a list of addressable variables.
    */
  def showAddressableVars(vars: Vector[PIdnNode], prefix: Doc): Doc =
      (if (vars.isEmpty) emptyDoc else prefix <+> addressable_variables <+> showList(vars)(showId))


  /**
    * Shows the Goified version of a program member.
    */
  override def showMember(mem: PMember): Doc = mem match {

    case _ if isWithinGoifiedScope(mem) => super.showMember(mem)
    case _ if classifier.isMemberGhost(mem) => blockSpecComment(super.showMember(mem))

    case PMethodDecl(id, rec, args, res, spec, body) =>
      showDeclarationSpec(DeclarationSpec(getGhostParams(args), getGhostParams(res.outs), spec)) <>
      super.showMember(
        PMethodDecl(
          id,
          rec,
          getActualParams(args),
          getActualResult(res),
          PFunctionSpec(Vector.empty, Vector.empty, Vector.empty, Vector.empty, Vector.empty),
          body
        )
      )

    case PFunctionDecl(id, args, res, spec, body) =>
      showDeclarationSpec(DeclarationSpec(getGhostParams(args), getGhostParams(res.outs), spec)) <>
      super.showMember(
        PFunctionDecl(
          id,
          getActualParams(args),
          getActualResult(res),
          PFunctionSpec(Vector.empty, Vector.empty, Vector.empty, Vector.empty, Vector.empty),
          body
        )
      )

    case pred: PFPredicateDecl => blockSpecComment(super.showMember(pred))

    case pred: PMPredicateDecl => blockSpecComment(super.showMember(pred))

    case m => super.showMember(m)
  }

  override def showBodyParameterInfo(info: PBodyParameterInfo): Doc = {
    if (info.shareableParameters.nonEmpty) {
      specComment <+> Constants.SHARE_PARAMETER_KEYWORD <+> showIdList(info.shareableParameters) <> line
    } else emptyDoc
  }

  /**
    * Shows a ghost statement in the goified version with a given prefix.
    */
  def showGhostStmt(stmt: PStatement, prefix: Doc): Doc = stmt match {
    case _: PClosureImplProof | _: PExplicitGhostStatement => blockSpecComment(prefix <+> super.showStmt(stmt))
    case _ => specComment <> prefix <+> super.showStmt(stmt)
  }
  def showGhostExprList(exprs: Vector[PExpression], prefix: Doc): Doc = specComment <> prefix <+> super.showExprList(exprs)

  override def showStmt(stmt: PStatement): Doc = stmt match {

    case s if isWithinGoifiedScope(s) => super.showStmt(s)
    case s if classifier.isStmtGhost(s) => showGhostStmt(stmt, emptyDoc)

    case PAssignment(right, left) =>
      StrictAssignMode(left.size, right.size) match {
        case AssignMode.Single =>
          val (aRight, aLeft) = right.zip(left).filter(p => !classifier.isExprGhost(p._2)).unzip
          val (ghostRight, ghostLeft) = right.zip(left).filter(p => classifier.isExprGhost(p._2)).unzip

          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PAssignment(aRight, aLeft))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PAssignment(ghostRight, ghostLeft), with_prefix(aLeft)))
          
        case AssignMode.Multi =>
          val (aLeft, ghostLeft) = left.partition(!classifier.isExprGhost(_))
          val (aRight, ghostRight) = right.partition(!classifier.isExprGhost(_))

          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PAssignment(aRight, aLeft))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PAssignment(ghostRight, ghostLeft), with_prefix(aLeft)))

        case AssignMode.Error | AssignMode.Variadic => errorMsg
      }

    case PShortVarDecl(right, left, addressable) =>
      StrictAssignMode(left.size, right.size) match {
        case AssignMode.Single =>
          val (aRight, aLeft) = right.zip(left).filter(p => !classifier.isIdGhost(p._2)).unzip
          // List of all non-ghost addressable variables.
          val aAddressableVars = left.zip(addressable).filter(p => !classifier.isIdGhost(p._1) && p._2).map(_._1)
          val (ghostRight, ghostLeft) = right.zip(left).filter(p => classifier.isIdGhost(p._2)).unzip
          // Boolean vector of whether ghost variables are addressable or not.
          val ghostAddressable = left.zip(addressable).filter(p => classifier.isIdGhost(p._1)).map(_._2)

          val prefix: Doc = if (ghostLeft.isEmpty) specComment else ";"

          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PShortVarDecl(aRight, aLeft, aLeft.map(_ => false)))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PShortVarDecl(ghostRight, ghostLeft, ghostAddressable), with_prefix(aLeft))) <+>
          showAddressableVars(aAddressableVars, prefix)

        case AssignMode.Multi =>
          val (aLeft, ghostLeft) = left.partition(!classifier.isIdGhost(_))
          val (aRight, ghostRight) = right.partition(!classifier.isExprGhost(_))
          // List of all non-ghost addressable variables.
          val aAddressableVars = left.zip(addressable).filter(p => !classifier.isIdGhost(p._1)).map(_._1)
          // Boolean vector of whether ghost variables are addressable or not.
          val ghostAddressable = left.zip(addressable).filter(p => classifier.isIdGhost(p._1)).map(_._2)

          val prefix: Doc = if (ghostLeft.isEmpty) specComment else ";"
          
          (if (aLeft.isEmpty) emptyDoc else super.showStmt(PShortVarDecl(aRight, aLeft, aLeft.map(_ => false)))) <>
          (if (ghostLeft.isEmpty) emptyDoc else showGhostStmt(PShortVarDecl(ghostRight, ghostLeft, ghostAddressable), with_prefix(aLeft))) <+>
          showAddressableVars(aAddressableVars, prefix)

        case AssignMode.Error | AssignMode.Variadic => errorMsg
      }

    case n@ PReturn(right) =>
      val gt = classifier.expectedReturnGhostTyping(n)
      val aRight = right.zip(gt.toTuple).filter(!_._2).map(_._1)
      val ghostRight = right.zip(gt.toTuple).filter(_._2).map(_._1)
      
      (if (aRight.isEmpty) emptyDoc else super.showStmt(PReturn(aRight))) <>
      (if (ghostRight.isEmpty) emptyDoc else showGhostExprList(ghostRight, with_prefix(ghostRight)))

    case n: PProofAnnotation => n match {
      case n: POutline =>
        showSpec(n.spec) <> specComment <> "outline (" <> line <> showStmt(n.body) <> line <>  specComment <> ")" <> line
      case PClosureImplProof(impl, PBlock(stmts)) => blockSpecComment("proof" <+> showExpr(impl) <> block(showStmtList(stmts)))
    }

    case _ => super.showStmt(stmt)

  }


  override def showExpr(expr: PExpression): Doc = expr match {

    case e if isWithinGoifiedScope(e) => super.showExpr(e)
    
    case n: PInvoke =>
      val gt = classifier.expectedArgGhostTyping(n)
      val aArgs = n.args.zip(gt.toTuple).filter(!_._2).map(_._1)
      val ghostArgs = n.args.zip(gt.toTuple).filter(_._2).map(_._1)

      super.showExpr(n.copy(args = aArgs)) <>
        opt(n.spec)(s => emptyDoc <+> inlinedSpecComment("as" <+> showMisc(s))) <>
          (if (ghostArgs.isEmpty) emptyDoc else space <> inlinedSpecComment(with_keyword <+> showExprList(ghostArgs)))

    case e: PProofAnnotation => e match {
      case e: PUnfolding =>
        parens(inlinedSpecComment(unfolding_keyword <+> super.showExpr(e.pred)) <+> showExpr(e.op))
    }

    case e => super.showExpr(e)
  }



  /**
    * Shows ghost types in the goified version.
    */
  def showGhostType(typ: PType): Doc = specComment <+> super.showType(typ)

  override def showType(typ: PType): Doc = typ match {

    case PStructType(clauses) =>
      val (actualClauses, ghostClauses) = partitionStructClauses(clauses)

      super.showType(PStructType(actualClauses)) <+>
      (if (ghostClauses.isEmpty) emptyDoc else showGhostType(PStructType(ghostClauses)))

    case PInterfaceType(embedded, mspecs, pspecs) =>
      val (actualEmbedded, ghostEmbedded) = partitionInterfaceClauses(embedded)
      val (actualMspecs, ghostMspecs) = partitionInterfaceClauses(mspecs)
      val (actualPspecs, ghostPspecs) = partitionInterfaceClauses(pspecs)

      "interface" <+> block(
        ssep(actualEmbedded map showInterfaceClause, line) <> line <>
        (if (ghostEmbedded.isEmpty) emptyDoc else ssep(ghostEmbedded map (specComment <+> showInterfaceClause(_)), line) <> line) <>

        ssep(actualMspecs map showInterfaceClause, line) <> line <>
        (if (ghostMspecs.isEmpty) emptyDoc else ssep(ghostMspecs map (m => inlinedSpecComment(showInterfaceClause(m))), line) <> line) <>

        ssep(actualPspecs map showInterfaceClause, line) <>
        (if (ghostPspecs.isEmpty) emptyDoc else ssep(ghostPspecs map (specComment <+> showInterfaceClause(_)), line) <> line)
      )

    case t => super.showType(t)
  }

  private def partitionStructClauses[T <: PStructClause](cl: Vector[T]): (Vector[T], Vector[T]) =
    cl.partition(!classifier.isStructClauseGhost(_))

  private def partitionInterfaceClauses[T <: PInterfaceClause](cl: Vector[T]): (Vector[T], Vector[T]) =
    cl.partition(!classifier.isInterfaceClauseGhost(_))


  private def errorMsg: Nothing = Violation.violation("GoifyingPrinter has to be run after the type check")
}


