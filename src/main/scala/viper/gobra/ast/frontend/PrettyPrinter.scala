// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.frontend

import org.bitbucket.inkytonik.kiama
import viper.gobra.ast.printing.PrettyPrinterCombinators
import viper.gobra.util.Constants

trait PrettyPrinter {
  def format(node: PNode): String
}

class DefaultPrettyPrinter extends PrettyPrinter with kiama.output.PrettyPrinter with PrettyPrinterCombinators {

  override val defaultIndent = 2
  override val defaultWidth = 80

  override def format(node: PNode): String = pretty(show(node)).layout

  def show(node: PNode): Doc = node match {
    case n: PPackage => showPackage(n)
    case n: PProgram => showProgram(n)
    case n: PPackageClause => showPackageClause(n)
    case n: PImport => showImport(n)
    case n: PMember => showMember(n)
    case n: PStatement => showStmt(n)
    case n: PExpression => showExpr(n)
    case n: PSpecification => showSpec(n)
    case n: PType => showType(n)
    case n: PIdnNode => showId(n)
    case n: PLabelNode => showLabel(n)
    case n: PPackageNode => showPackageId(n)
    case n: PFieldDecl => showFieldDecl(n)
    case n: PMisc => showMisc(n)
    case n: PGhostCollectionUpdateClause => showGhostCollectionUpdateClause(n)
    case n: PAssOp => showAssOp(n)
    case n: PLiteralType => showLiteralType(n)
    case n: PCompositeKey => showCompositeKey(n)
    case n: PIfClause => showIfClause(n)
    case n: PExprSwitchClause => showExprSwitchClause(n)
    case n: PTypeSwitchClause => showTypeSwitchClause(n)
    case n: PSelectClause => showSelectClause(n)
    case n: PStructClause => showStructClause(n)
    case n: PInterfaceClause => showInterfaceClause(n)
    case n: PBodyParameterInfo => showBodyParameterInfo(n)
    case PPos(_) => emptyDoc
  }

  // entire package

  def showPackage(p: PPackage): Doc =
    ssep(p.programs map showProgram, line) <> line

  // program

  def showProgram(p: PProgram): Doc = p match {
    case PProgram(packageClause, imports, declarations) =>
      showPackageClause(packageClause) <> line <> line <>
        ssep(imports map showImport, line) <> line <>
        ssep(declarations map showMember, line <> line) <> line
  }

  // package

  def showPackageClause(node: PPackageClause): Doc = "package" <+> showPackageId(node.id)
  def showPackageId(id: PPackageNode): Doc = id.name

  // imports

  def showImport(decl: PImport): Doc = decl match {
    case PExplicitQualifiedImport(PWildcard(), pkg) => "import" <+> "_" <+> pkg
    case PExplicitQualifiedImport(qualifier, pkg) => "import" <+> showId(qualifier) <+> pkg
    case PImplicitQualifiedImport(pkg) => "import" <+> pkg
    case PUnqualifiedImport(pkg) => "import" <+> "." <+> pkg
  }

  // members

  def showMember(mem: PMember): Doc = mem match {
    case mem: PActualMember => mem match {
      case n: PConstDecl => showConstDecl(n)
      case n: PVarDecl => showVarDecl(n)
      case n: PTypeDecl => showTypeDecl(n)
      case PFunctionDecl(id, args, res, spec, body) =>
        showSpec(spec) <> "func" <+> showId(id) <> parens(showParameterList(args)) <> showResult(res) <>
          opt(body)(b => space <> showBodyParameterInfoWithBlock(b._1, b._2))
      case PMethodDecl(id, rec, args, res, spec, body) =>
        showSpec(spec) <> "func" <+> showReceiver(rec) <+> showId(id) <> parens(showParameterList(args)) <> showResult(res) <>
        opt(body)(b => space <> showBodyParameterInfoWithBlock(b._1, b._2))
    }
    case member: PGhostMember => member match {
      case PExplicitGhostMember(m) => "ghost" <+> showMember(m)
      case PFPredicateDecl(id, args, body) =>
        "pred" <+> showId(id) <> parens(showParameterList(args)) <> opt(body)(b => space <> block(showExpr(b)))
      case PMPredicateDecl(id, recv, args, body) =>
        "pred" <+> showReceiver(recv) <+> showId(id) <> parens(showParameterList(args)) <> opt(body)(b => space <> block(showExpr(b)))
      case ip: PImplementationProof =>
        showType(ip.subT) <+> "implements" <+> showType(ip.superT) <> (
            if (ip.alias.isEmpty && ip.memberProofs.isEmpty) emptyDoc
            else block(ssep(ip.alias map showMisc, line) <> line <> ssep(ip.memberProofs map showMisc, line))
          )
    }
  }

  def showPure: Doc = "pure" <> line
  def showPre(pre: PExpression): Doc = "requires" <+> showExpr(pre)
  def showPost(post: PExpression): Doc = "ensures" <+> showExpr(post)
  def showInv(inv: PExpression): Doc = "invariant" <+> showExpr(inv)
  
   
  def showConditionalMeasureCollection[T <: PConditionalMeasure](tuple:Vector[T]):Doc = showList(tuple)(showConditionalMeasure)

  def showConditionalMeasure(conditionalMeasure:PConditionalMeasure) : Doc = conditionalMeasure match {
       case PConditionalMeasureExpression((expression, condition)) =>
      "decreases" <+> showExprList(expression) <+> "if" <+>showExpr(condition)
       case PConditionalMeasureUnderscore((underscore,condition)) =>
      "decreases" <+> "_" <+> "if" <+>showExpr(condition)
       case PConditionalMeasureAdditionalStar() =>
         "decreases" <+> "*"
  }


      def showTerminationmeasure(ter:Option[PTerminationMeasure]): Doc = ter match  {
      case Some(terminationMeasure) => terminationMeasure match {
      case  PTupleTerminationMeasure(tuple) => "decreases" <+> showExprList(tuple)
      case PStarCharacter() =>"decreases" <+> "*"
      case PUnderscoreCharacter()=>"decreases" <+> "_"
      case PConditionalMeasureCollection(tuple)=> showConditionalMeasureCollection(tuple)
    }
    case None=>""
  }



  def showSpec(spec: PSpecification): Doc = spec match {
    case PFunctionSpec(pres, posts, terminationMeasure, isPure) =>
      (if (isPure) showPure else emptyDoc) <>
      hcat(pres map (showPre(_) <> line)) <>
        hcat(posts map (showPost(_) <> line))  <>
        showTerminationmeasure(terminationMeasure) <> line

    case PLoopSpec(inv,termination_measures) =>
      hcat(inv map (showInv(_) <> line))  <>
        showTerminationmeasure(termination_measures) <> line
  }

  def showBodyParameterInfoWithBlock(info: PBodyParameterInfo, block: PBlock): Doc = {
    this.block(
      showBodyParameterInfo(info) <> showStmtList(block.stmts)
    )
  }

  def showBodyParameterInfo(info: PBodyParameterInfo): Doc = {
    if (info.shareableParameters.nonEmpty) {
      Constants.SHARE_PARAMETER_KEYWORD <+> showIdList(info.shareableParameters) <> line
    } else emptyDoc
  }

  def showNestedStmtList[T <: PStatement](list: Vector[T]): Doc = sequence(ssep(list map showStmt, line))
  def showStmtList[T <: PStatement](list: Vector[T]): Doc = ssep(list map showStmt, line)
  def showParameterList[T <: PParameter](list: Vector[T]): Doc = showList(list)(showParameter)
  def showExprList[T <: PExpression](list: Vector[T]): Doc = showList(list)(showExpr)
  def showTypeList[T <: PType](list: Vector[T]): Doc = showList(list)(showType)
  def showIdList[T <: PIdnNode](list: Vector[T]): Doc = showList(list)(showId)

  def showList[T](list: Vector[T])(f: T => Doc): Doc = ssep(list map f, comma <> space)

  def showVarDecl(decl: PVarDecl): Doc = decl match {
    case PVarDecl(typ, right, left, addressable) =>
      val rhs: Doc = if (right.isEmpty) "" else space <> "=" <+> showExprList(right)
      "var" <+> showList(left zip addressable){ case (v, a) => showAddressable(a, v) } <> opt(typ)(space <> showType(_)) <> rhs
  }

  def showConstDecl(decl: PConstDecl): Doc = decl match {
    case PConstDecl(typ, right, left) => "const" <+> showIdList(left) <> opt(typ)(space <> showType(_)) <+> "=" <+> showExprList(right)
  }

  def showTypeDecl(decl: PTypeDecl): Doc = decl match {
    case PTypeDef(right, left) => "type" <+> showId(left) <+> showType(right)
    case PTypeAlias(right, left) => "type" <+> showId(left) <+> "=" <+> showType(right)
  }

  def showParameter(para: PParameter): Doc = para match {
    case PExplicitGhostParameter(p) => "ghost" <+> showParameter(p)
    case PUnnamedParameter(typ) => showType(typ)
    case PNamedParameter(id, typ) => showId(id) <+> showType(typ)
  }

  def showReceiver(rec: PReceiver): Doc = rec match {
    case PNamedReceiver(id, typ, addressable) => parens(showAddressable(addressable, id) <+> showType(typ))
    case PUnnamedReceiver(typ) => parens(showType(typ))
  }

  def showResult(res: PResult): Doc = res match {
    case PResult(outs) => space <> parens(showParameterList(outs))
  }

  def showAddressable(addressable: Boolean, id: PIdnNode): Doc =
    (if (addressable) Constants.ADDRESSABILITY_MODIFIER else "") <> showId(id)

  // statements

  def showStmt(stmt: PStatement): Doc = stmt match {
    case stmt: PActualStatement => stmt match {
      case n: PConstDecl => showConstDecl(n)
      case n: PVarDecl => showVarDecl(n)
      case n: PTypeDecl => showTypeDecl(n)
      case PShortVarDecl(right, left, addressable) =>
        showList(left zip addressable){ case (l, a) => showAddressable(a, l) } <+> ":=" <+> showExprList(right)
      case PLabeledStmt(label, s) => label.name <> ":" <+> showStmt(s)
      case PEmptyStmt() => emptyDoc
      case PExpressionStmt(exp) => showExpr(exp)
      case PSendStmt(channel, msg) => showExpr(channel) <+> "<-" <+> showExpr(msg)
      case PAssignment(right, left) => showExprList(left) <+> "=" <+> showExprList(right)
      case PAssignmentWithOp(right, op, left) => showExpr(left) <+> showAssOp(op) <> "=" <+> showExpr(right)
      case PIfStmt(ifs, els) =>
        ssep(ifs map showIfClause, space <> "else" <> space) <>
          opt(els)(space <> "else" <+> showStmt(_) <> line)
      case PExprSwitchStmt(pre, _, cases, dflt) =>
        "switch" <> showPreStmt(pre) <+> block(
          ssep(cases map showExprSwitchClause, line) <>
            ssep(dflt map { d =>
              "default"  <> ":" <> showNestedStmtList(d.stmts)
            }, line)
        )
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) =>
        "switch" <> showPreStmt(pre) <> opt(binder)(space <> showId(_) <+> ":=") <+> showExpr(exp) <> ".(type)" <+> block(
          ssep(cases map showTypeSwitchClause, line)) <>
          ssep(dflt map {d => "default"  <> ":" <> showNestedStmtList(d.stmts) }, line)
      case PForStmt(pre, cond, post, spec, body) => showSpec(spec) <> ((pre, cond, post) match {
        case (None, PBoolLit(true), None) =>  "for" <+> showStmt(body)
        case (None, _, None) => "for" <+> showExpr(cond) <+> showStmt(body)
        case _ => "for" <+> opt(pre)(showStmt) <> ";" <+> showExpr(cond) <> ";" <+> opt(post)(showStmt) <+> showStmt(body)
      })
      case PAssForRange(range, ass, body) =>
        "for" <+> showExprList(ass) <+> "=" <+> showRange(range) <+> block(showStmt(body))
      case PShortForRange(range, shorts, body) =>
        "for" <+> showIdList(shorts) <+> ":=" <+> showRange(range) <+> block(showStmt(body))
      case PGoStmt(exp) => "go" <+> showExpr(exp)
      case PSelectStmt(send, rec, aRec, sRec, dflt) =>
        "select" <+> block(
          ssep(send map showSelectClause, line) <>
            ssep(rec map showSelectClause, line) <>
            ssep(aRec map showSelectClause, line) <>
            ssep(sRec map showSelectClause, line) <>
            ssep(dflt map showSelectClause, line)
        )
      case PReturn(exps) => "return" <+> showExprList(exps)
      case PBreak(label) => "break" <> opt(label)(space <> showLabel(_))
      case PContinue(label) => "continue" <> opt(label)(space <> showLabel(_))
      case PGoto(label) => "goto" <+> showLabel(label)
      case PDeferStmt(exp) => "defer" <+> showExpr(exp)
      case PBlock(stmts) => block(showStmtList(stmts))
      case PSeq(stmts) => showStmtList(stmts)
    }
    case statement: PGhostStatement => statement match {
      case PExplicitGhostStatement(actual) => "ghost" <+> showStmt(actual)
      case PAssert(exp) => "assert" <+> showExpr(exp)
      case PAssume(exp) => "assume" <+> showExpr(exp)
      case PExhale(exp) => "exhale" <+> showExpr(exp)
      case PInhale(exp) => "inhale" <+> showExpr(exp)
      case PUnfold(exp) => "unfold" <+> showExpr(exp)
      case PFold(exp) => "fold" <+> showExpr(exp)
    }
  }

  def showPreStmt(n: Option[PSimpleStmt]): Doc = opt(n)(space <> showStmt(_) <> ";")

  def showAssOp(n: PAssOp): Doc = n match {
    case PAddOp() => "+"
    case PSubOp() => "-"
    case PMulOp() => "*"
    case PDivOp() => "/"
    case PModOp() => "%"
  }

  def showIfClause(n: PIfClause): Doc = n match {
    case PIfClause(pre, condition, body) => "if" <> showPreStmt(pre) <+> showExpr(condition) <+> showStmt(body)
  }

  def showExprSwitchClause(n: PExprSwitchClause): Doc = n match {
    case PExprSwitchDflt(body) => "default"  <> ":" <> showNestedStmtList(body.stmts)
    case PExprSwitchCase(left, body) => "case" <+> showExprList(left) <> ":" <> showNestedStmtList(body.stmts)
  }

  def showTypeSwitchClause(n: PTypeSwitchClause): Doc = n match {
    case PTypeSwitchDflt(body) => "default"  <> ":" <> showNestedStmtList(body.stmts)
    case PTypeSwitchCase(left, body) => "case" <+> showTypeList(left) <> ":" <> sequence(ssep(body.stmts map showStmt, line))
  }

  def showSelectClause(n: PSelectClause): Doc = n match {
    case PSelectDflt(body) =>
      "default" <> ":" <> showNestedStmtList(body.stmts)
    case PSelectSend(send, body) =>
      "case" <+> showStmt(send) <> ":" <> showNestedStmtList(body.stmts)
    case PSelectRecv(recv, body) =>
      "case" <+> showExpr(recv) <> ":" <> showNestedStmtList(body.stmts)
    case PSelectAssRecv(recv, ass, body) =>
      "case" <+> showExprList(ass) <+> "=" <+> showExpr(recv) <> ":" <> showNestedStmtList(body.stmts)
    case PSelectShortRecv(recv, shorts, body) =>
      "case" <+> showIdList(shorts) <+> "=" <+> showExpr(recv) <> ":" <> showNestedStmtList(body.stmts)
  }

  def showRange(n: PRange): Doc = "range" <+> showExpr(n.exp)

  // expressions

  def showExprOrType(expr: PExpressionOrType): Doc = expr match {
    case expr: PExpression => expr match {
      case _: PReference => parens(showExpr(expr))
      case _ => showExpr(expr)
    }
    case typ: PType => showType(typ)
  }


  def showMultSubExpr(expr: PExpression): Doc = expr match {
    case _: PAdd => parens(showExpr(expr))
    case _: PSub => parens(showExpr(expr))
    case _ => showExpr(expr)
  }

  def showSubtractionSubExpr(expr: PExpression): Doc = expr match {
    case _: PAdd => parens(showExpr(expr))
    case _ => showExpr(expr)
  }

  def showDivSubExpr(expr: PExpression): Doc = expr match {
    case _: PAdd => parens(showExpr(expr))
    case _: PSub => parens(showExpr(expr))
    case _: PMul => parens(showExpr(expr))
    case _ => showExpr(expr)
  }

  /**
    * Precedence of expressions.
    */
  object Precedence extends Enumeration {
    val p1 = Value(1)
    val p1P5 = Value(2)
    val p2 = Value(3)
    val p3 = Value(4)
    val p4 = Value(5)
    val p5 = Value(6)
    val p6 = Value(7)
    val p7 = Value(8)
  }

  def getPrecedence(expr: PExpressionOrType): Precedence.Value = expr match {
    case _: PConditional => Precedence.p1
    case _: PImplication => Precedence.p1P5
    case _: POr => Precedence.p2
    case _: PAnd => Precedence.p3
    case _: PEquals | _: PUnequals | _: PLess | _: PAtMost | _: PGreater | _: PAtLeast => Precedence.p4
    case _: PAdd | _: PSub => Precedence.p5
    case _: PMul | _: PDiv | _: PMod => Precedence.p6
    case _ => Precedence.p7
  }

  def showSubExpr(expr: PExpression, subExpr: PExpression): Doc = {
    val exprPrecedence = getPrecedence(expr)
    val subExprPrecedence = getPrecedence(subExpr)

    if (subExprPrecedence < exprPrecedence)
      parens(showExpr(subExpr))
    else
      showExpr(subExpr)
  }

  def showSubExprOrType(expr: PExpressionOrType, subExpr: PExpressionOrType): Doc = {
    val exprPrecedence = getPrecedence(expr)
    val subExprPrecedence = getPrecedence(subExpr)

    if (subExprPrecedence < exprPrecedence)
      parens(showExprOrType(subExpr))
    else
      showExprOrType(subExpr)
  }


  def showExpr(expr: PExpression): Doc = expr match {
    case expr: PActualExpression => expr match {
      case PReceive(operand) => "<-" <> showExpr(operand)
      case PReference(operand) => "&" <> showExpr(operand)
      case PDeref(base) => "*" <> showExprOrType(base)
      case PDot(base, id) => showExprOrType(base) <> "." <>  showId(id)
      case PNegation(operand) => "!" <> showExpr(operand)
      case PNamedOperand(id) => showId(id)
      case PBoolLit(lit) => if(lit) "true" else "false"
      case PIntLit(lit) => lit.toString
      case PNilLit() => "nil"
      case PStringLit(lit) => "\"" <> lit <> "\""
      case PCompositeLit(typ, lit) => showLiteralType(typ) <+> showLiteralValue(lit)
      case PFunctionLit(args, result, body) =>
        "func" <> parens(showParameterList(args)) <> showResult(result) <> block(showStmt(body))
      case PInvoke(base, args) => showExprOrType(base) <> parens(showExprList(args))
      case PIndexedExp(base, index) => showExpr(base) <> brackets(showExpr(index))

      case PSliceExp(base, low, high, cap) => {
        val lowP = low.fold(emptyDoc)(showExpr)
        val highP = ":" <> high.fold(emptyDoc)(showExpr)
        val capP = cap.fold(emptyDoc)(":" <> showExpr(_))
        showExpr(base) <> brackets(lowP <> highP <> capP)
      }

      case PUnpackSlice(exp) => showExpr(exp) <> "..."

      case PTypeAssertion(base, typ) => showExpr(base) <> "." <> parens(showType(typ))
      case PEquals(left, right) => showSubExprOrType(expr, left) <+> "==" <+> showSubExprOrType(expr, right)
      case PUnequals(left, right) => showSubExprOrType(expr, left) <+> "!=" <+> showSubExprOrType(expr, right)
      case PAnd(left, right) => showSubExpr(expr, left) <+> "&&" <+> showSubExpr(expr, right)
      case POr(left, right) => showSubExpr(expr, left) <+> "||" <+> showSubExpr(expr, right)
      case PLess(left, right) => showSubExpr(expr, left) <+> "<" <+> showSubExpr(expr, right)
      case PAtMost(left, right) => showSubExpr(expr, left) <+> "<=" <+> showSubExpr(expr, right)
      case PGreater(left, right) => showSubExpr(expr, left) <+> ">" <+> showSubExpr(expr, right)
      case PAtLeast(left, right) => showSubExpr(expr, left) <+> ">=" <+> showSubExpr(expr, right)
      case PAdd(left, right) => showSubExpr(expr, left) <+> "+" <+> showSubExpr(expr, right)
      case PSub(left, right) => showSubExpr(expr, left) <+> "-" <+> showSubExpr(expr, right)
      case PMul(left, right) => showSubExpr(expr, left) <+> "*" <+> showSubExpr(expr, right)
      case PMod(left, right) => showSubExpr(expr, left) <+> "%" <+> showSubExpr(expr, right)
      case PDiv(left, right) => showSubExpr(expr, left) <+> "/" <+> showSubExpr(expr, right)
      case PUnfolding(acc, op) => "unfolding" <+> showExpr(acc) <+> "in" <+> showExpr(op)
      case PLength(expr) => "len" <> parens(showExpr(expr))
      case PCapacity(expr) => "cap" <> parens(showExpr(expr))
      case PMake(typ, args) => "make" <> parens(showList[PExpressionOrType](typ +: args){
        case x: PExpression => showExpr(x)
        case x: PType => showType(x)
      })
      case PNew(typ) => "new" <> parens(showType(typ))
      case PBlankIdentifier() => "_"
      // already using desired notation for predicate constructor instances, i.e. the "{}" delimiters for
      // partially applied predicates
      case PPredConstructor(base, args) => show(base) <> braces(showList(args)(_.fold(text("_"))(showExpr)))
    }
    case expr: PGhostExpression => expr match {
      case POld(e) => "old" <> parens(showExpr(e))
      case PLabeledOld(l, e) => "old" <> brackets(l.name) <> parens(showExpr(e))
      case PConditional(cond, thn, els) => showSubExpr(expr, cond) <> "?" <> showSubExpr(expr, thn) <> ":" <> showSubExpr(expr, els)
      case PForall(vars, triggers, body) =>
        "forall" <+> showList(vars)(showMisc) <+> "::" <+> showList(triggers)(showMisc) <+> showExpr(body)
      case PExists(vars, triggers, body) =>
        "exists" <+> showList(vars)(showMisc) <+> "::" <+> showList(triggers)(showMisc) <+> showExpr(body)
      case PImplication(left, right) => showSubExpr(expr, left) <+> "==>" <+> showSubExpr(expr, right)
      case PAccess(exp, PFullPerm()) => "acc" <> parens(showExpr(exp))
      case PAccess(exp, perm) => "acc" <> parens(showExpr(exp) <> "," <+> showExpr(perm))
      case PPredicateAccess(exp, perm) => exp match {
        case n: PInvoke if perm == PFullPerm() => showExpr(n)
        case n: PExpression if perm == PFullPerm() => "acc" <> parens(showExpr(n))
        case n: PExpression => "acc" <> parens(showExpr(n) <> "," <+> showExpr(perm))
      }

      case PTypeOf(exp) => "typeOf" <> parens(showExpr(exp))
      case PIsComparable(exp) => "isComparable" <> parens(showExprOrType(exp))

      case POptionNone(t) => "none" <> brackets(showType(t))
      case POptionSome(e) => "some" <> parens(showExpr(e))
      case POptionGet(e) => "get" <> parens(showExpr(e))

      case expr : PGhostCollectionExp => expr match {
        case PCardinality(operand) => "|" <> showExpr(operand) <> "|"
        case PIn(left, right) => showSubExpr(expr, left) <+> "in" <+> showSubExpr(expr, right)
        case PMultiplicity(left, right) => showSubExpr(expr, left) <+> "#" <+> showSubExpr(expr, right)
        case PGhostCollectionUpdate(seq, clauses) => showExpr(seq) <>
          (if (clauses.isEmpty) emptyDoc else brackets(showList(clauses)(showGhostColUpdateClause)))
        case expr : PSequenceExp => expr match {
          case PSequenceConversion(exp) => "seq" <> parens(showExpr(exp))
          case PRangeSequence(low, high) => "seq" <> brackets(showExpr(low) <+> ".." <+> showExpr(high))
          case PSequenceAppend(left, right) => showSubExpr(expr, left) <+> "++" <+> showSubExpr(expr, right)
        }
        case expr : PUnorderedGhostCollectionExp => expr match {
          case PUnion(left, right) => showSubExpr(expr, left) <+> "union" <+> showSubExpr(expr, right)
          case PIntersection(left, right) => showSubExpr(expr, left) <+> "intersection" <+> showSubExpr(expr, right)
          case PSetMinus(left, right) => showSubExpr(expr, left) <+> "setminus" <+> showSubExpr(expr, right)
          case PSubset(left, right) => showSubExpr(expr, left) <+> "subset" <+> showSubExpr(expr, right)
          case PSetConversion(exp) => "set" <> parens(showExpr(exp))
          case PMultisetConversion(exp) => "mset" <> parens(showExpr(exp))
          case PMapKeys(exp) => "domain" <> parens(showExpr(exp))
          case PMapValues(exp) => "range" <> parens(showExpr(exp))
        }
      }

      case expr: PPermission => expr match {
        case PFullPerm() => "write"
        case PNoPerm() => "none"
        case PWildcardPerm() => "_"
      }
    }
  }

  def showGhostColUpdateClause(clause : PGhostCollectionUpdateClause) : Doc =
    showExpr(clause.left) <+> "=" <+> showExpr(clause.right)

  def showLiteralType(typ: PLiteralType): Doc = typ match {
    case t: PType => showType(t)
    case PImplicitSizeArrayType(elem) => "[...]" <> showType(elem)
  }

  def showCompositeKey(n: PCompositeKey): Doc = n match {
    case PIdentifierKey(id) => showId(id)
    case cv: PCompositeVal => showCompositeVal(cv)
  }

  def showCompositeVal(n: PCompositeVal): Doc = n match {
    case PExpCompositeVal(exp) => showExpr(exp)
    case PLitCompositeVal(l) => showLiteralValue(l)
  }

  def showLiteralValue(lit: PLiteralValue): Doc =
    braces(ssep(lit.elems map showKeyedElement, comma) <> space)

  def showKeyedElement(n: PKeyedElement): Doc = n match {
    case PKeyedElement(key, exp) => opt(key)(showCompositeKey(_) <> ":") <+> showCompositeVal(exp)
  }

  // types

  def showType(typ: PType): Doc = typ match {
    case t: PActualType => showActualType(t)
    case t: PGhostType => showGhostType(t)
  }

  def showActualType(typ : PActualType) : Doc = typ match {
    case PNamedOperand(id) => showId(id)
    case PBoolType() => "bool"
    case PStringType() => "string"
    case PPermissionType() => "perm"
    case PIntType() => "int"
    case PInt8Type() => "int8"
    case PInt16Type() => "int16"
    case PInt32Type() => "int32"
    case PRune() => "rune"
    case PInt64Type() => "int64"
    case PUIntType() => "uint"
    case PUInt8Type() => "uint8"
    case PByte() => "byte"
    case PUInt16Type() => "uint16"
    case PUInt32Type() => "uint32"
    case PUInt64Type() => "uint64"
    case PUIntPtr() => "uintptr"
    case PArrayType(len, elem) => brackets(showExpr(len)) <> showType(elem)
    case PSliceType(elem) => brackets(emptyDoc) <> showType(elem)
    case PVariadicType(elem) => "..." <> showType(elem)
    case PMapType(key, elem) => "map" <> brackets(showType(key)) <> showType(elem)
    case PDeref(base) => "*" <> showExprOrType(base)
    case PDot(base, id) => showExprOrType(base) <> "." <>  showId(id)
    case channelType: PChannelType => channelType match {
      case PBiChannelType(elem)   => "chan" <+> showType(elem)
      case PSendChannelType(elem) => "chan" <> "<-" <+> showType(elem)
      case PRecvChannelType(elem) => "<-" <> "chan" <+> showType(elem)
    }
    case PStructType(clauses) => "struct" <+> block(ssep(clauses map showStructClause, line))
    case PFunctionType(args, result) => "func" <> parens(showParameterList(args)) <> showResult(result)
    case PPredType(args) => "pred" <> parens(showTypeList(args))
    case PInterfaceType(embedded, mspec, pspec) =>
      "interface" <+> block(
        ssep(embedded map showInterfaceClause, line) <>
          ssep(mspec map showInterfaceClause, line) <>
          ssep(pspec map showInterfaceClause, line)
      )
    case PMethodReceiveName(t) => showType(t)
    case PMethodReceivePointer(t) => "*" <> showType(t)
  }

  def showGhostType(typ : PGhostType) : Doc = typ match {
    case PSequenceType(elem) => "seq" <> brackets(showType(elem))
    case PSetType(elem) => "set" <> brackets(showType(elem))
    case PMultisetType(elem) => "mset" <> brackets(showType(elem))
    case PMathematicalMapType(keys, values) => "dict" <> brackets(showType(keys)) <> showType(values)
    case POptionType(elem) => "option" <> brackets(showType(elem))
    case PGhostSliceType(elem) => "ghost" <+> brackets(emptyDoc) <> showType(elem)
    case PDomainType(funcs, axioms) =>
      "domain" <+> block(
        ssep((funcs ++ axioms) map showMisc, line)
      )
  }

  def showStructClause(c: PStructClause): Doc = c match {
    case clause: PActualStructClause => clause match {
      case PFieldDecls(fields) =>
        require(fields.nonEmpty && fields.forall(_.typ == fields.head.typ))
        showIdList(fields map (_.id)) <+> showType(fields.head.typ)
      case PEmbeddedDecl(typ, _) => showEmbeddedType(typ)
    }
    case PExplicitGhostStructClause(actual) => showStructClause(actual)
  }

  def showFieldDecl(f: PFieldDecl): Doc = f match {
    case PFieldDecl(id, typ) => showId(id) <+> showType(typ)
  }

  def showEmbeddedType(t: PEmbeddedType): Doc = t match {
    case PEmbeddedName(typ) => showType(typ)
    case PEmbeddedPointer(typ) => "*" <> showType(typ)
  }

  def showInterfaceClause(n: PInterfaceClause): Doc = n match {
    case PInterfaceName(typ) => showType(typ)
    case PMethodSig(id, args, result, spec, isGhost) =>
      (if (isGhost) "ghost" <> line else emptyDoc) <> showSpec(spec) <>
        showId(id) <> parens(showParameterList(args)) <> showResult(result)
    case PMPredicateSig(id, args) => "pred"  <+> showId(id) <> parens(showParameterList(args))
  }

  // ids

  def showId(id: PIdnNode): Doc = id.name

  def showLabel(id: PLabelNode): Doc = id.name

  // misc

  def showMisc(id: PMisc): Doc = id match {
    case n: PRange => showRange(n)
    case receiver: PReceiver => showReceiver(receiver)
    case result: PResult => showResult(result)
    case embeddedType: PEmbeddedType => showEmbeddedType(embeddedType)
    case parameter: PParameter => showParameter(parameter)
    case literalValue: PLiteralValue => showLiteralValue(literalValue)
    case keyedElement: PKeyedElement => showKeyedElement(keyedElement)
    case compositeVal: PCompositeVal => showCompositeVal(compositeVal)
    case misc: PGhostMisc => misc match {
      case PFPredBase(id) => showId(id)
      case PDottedBase(expr) => showExprOrType(expr)
      case PBoundVariable(v, typ) => showId(v) <> ":" <+> showType(typ)
      case PTrigger(exps) => "{" <> showList(exps)(showExpr) <> "}"
      case PExplicitGhostParameter(actual) => showParameter(actual)
      case PDomainFunction(id, args, res) =>
        "func" <+> showId(id) <> parens(showParameterList(args)) <> showResult(res)
      case PDomainAxiom(exp) => "axiom" <+> block(showExpr(exp))
      case mip: PMethodImplementationProof =>
        (if (mip.isPure) "pure ": Doc else emptyDoc) <>
          parens(showParameter(mip.receiver)) <+> showId(mip.id) <> parens(showParameterList(mip.args)) <> showResult(mip.result) <>
          opt(mip.body)(b => space <> showBodyParameterInfoWithBlock(b._1, b._2))
      case ipa: PImplementationProofPredicateAlias =>
        "pred" <+> showId(ipa.left) <+> ":=" <+> showExprOrType(ipa.right)
    }
  }

  def showGhostCollectionUpdateClause(clause : PGhostCollectionUpdateClause) : Doc =
    showExpr(clause.left) <+> "=" <+> showExpr(clause.right)
}

class ShortPrettyPrinter extends DefaultPrettyPrinter {
  override val defaultIndent = 2
  override val defaultWidth  = 80

  override def showMember(mem: PMember): Doc = mem match {
    case mem: PActualMember => mem match {
      case n: PConstDecl => showConstDecl(n)
      case n: PVarDecl => showVarDecl(n)
      case n: PTypeDecl => showTypeDecl(n)
      case PFunctionDecl(id, args, res, spec, _) =>
        showSpec(spec) <> "func" <+> showId(id) <> parens(showParameterList(args)) <> showResult(res)
      case PMethodDecl(id, rec, args, res, spec, _) =>
        showSpec(spec) <> "func" <+> showReceiver(rec) <+> showId(id) <> parens(showParameterList(args)) <> showResult(res)
    }
    case member: PGhostMember => member match {
      case PExplicitGhostMember(m) => "ghost" <+> showMember(m)
      case PFPredicateDecl(id, args, _) =>
        "pred" <+> showId(id) <> parens(showParameterList(args))
      case PMPredicateDecl(id, recv, args, _) =>
        "pred" <+> showReceiver(recv) <+> showId(id) <> parens(showParameterList(args))
      case ip: PImplementationProof =>
        showType(ip.subT) <+> "implements" <+> showType(ip.superT)
    }
  }
}
