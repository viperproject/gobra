// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.


package viper.gobra.frontend
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter}
import org.bitbucket.inkytonik.kiama.util.{Position, Positions, Source}
import viper.gobra.ast.frontend._

import scala.jdk.CollectionConverters._

class ParseTreeTranslator(pom: PositionManager, source: Source) extends GobraParserBaseVisitor[AnyRef] {

  lazy val rewriter = new PRewriter(pom.positions)


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitUnaryExpr(ctx: GobraParser.UnaryExprContext): PExpression = {
    if (ctx.primaryExpr() != null) {
      visitPrimaryExpr(ctx.primaryExpr())
    } else if (ctx.unary_op != null) {
      val e = visitExpression(ctx.expression())
      ctx.unary_op.getType match {
        case GobraParser.PLUS => PAdd(PIntLit(0).newpos(ctx), e).newpos(ctx)
        case GobraParser.MINUS => PSub(PIntLit(0).newpos(ctx), e).newpos(ctx)
        case GobraParser.EXCLAMATION => PNegation(e).newpos(ctx)
        case GobraParser.CARET => PBitNegation(e).newpos(ctx)
        case op =>
          val (start, finish) = getStartFinish(ctx)
          throw TranslationException("Unsupported unary expression: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
      }
    } else { // should not be reachable
      fail(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */



  override def visitExpression(ctx: GobraParser.ExpressionContext): PExpression = {
    if(ctx.primaryExpr() != null){
      visitPrimaryExpr(ctx.primaryExpr()).newpos(ctx)
    } else if(ctx.unaryExpr() != null){
      visitUnaryExpr(ctx.unaryExpr())
    } else {
      val left = visitExpression(ctx.expression(0))
      val right = visitExpression(ctx.expression(1))
      if (ctx.rel_op != null) {
        ctx.rel_op.getType match {
          case GobraParser.EQUALS => PEquals(left, right).newpos(ctx)
          case GobraParser.NOT_EQUALS => PUnequals(left,right).newpos(ctx)
          case GobraParser.LESS => PLess(left,right).newpos(ctx)
          case GobraParser.LESS_OR_EQUALS => PAtMost(left, right).newpos(ctx)
          case GobraParser.GREATER => PGreater(left, right).newpos(ctx)
          case GobraParser.GREATER_OR_EQUALS => PAtLeast(left, right).newpos(ctx)
          case op =>
            val (start, finish) = getStartFinish(ctx)
            throw TranslationException("Unsupported binary operator: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
        }
      } else if (ctx.add_op != null) {
        ctx.add_op.getType match {
          case GobraParser.PLUS => PAdd(left, right).newpos(ctx)
          case GobraParser.MINUS => PSub(left, right).newpos(ctx)
          case op =>
            val (start, finish) = getStartFinish(ctx)
            throw TranslationException("Unsupported binary operator: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
        }
      } else if (ctx.mul_op != null) {
        ctx.mul_op.getType match {
          case GobraParser.STAR => PMul(left, right).newpos(ctx)
          case GobraParser.DIV => PDiv(left, right).newpos(ctx)
          case GobraParser.MOD => PMod(left, right).newpos(ctx)
          case op =>
            val (start, finish) = getStartFinish(ctx)
            throw TranslationException("Unsupported binary operator: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
        }
      } else if (ctx.LOGICAL_AND() != null) {
        PAnd(left, right).newpos(ctx)
      } else { // We have OR
        POr(left,right).newpos(ctx)
      }
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitArguments(ctx: GobraParser.ArgumentsContext): Vector[PExpression] = {
    val exprs : Vector[PExpression] = if (ctx.expressionList() != null) visitExpressionList(ctx.expressionList()) else Vector.empty
    if (ctx.ELLIPSIS() != null) {
      exprs.init.appended(PUnpackSlice(exprs.last).newpos(ctx))
    } else {
      exprs
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitPrimaryExpr(ctx: GobraParser.PrimaryExprContext): PExpression ={
    if(ctx.operand() != null){
      visitOperand(ctx.operand())
    } else if (ctx.primaryExpr() != null) {
      val pe = visitPrimaryExpr(ctx.primaryExpr())
      if(ctx.arguments() != null) {
        PInvoke(pe,visitArguments(ctx.arguments())).newpos(ctx)
      } else {
        fail(ctx)
      }
    } else {
      fail(ctx)
    }
  }

  override def visitInteger(ctx: GobraParser.IntegerContext): PIntLit = {
    if(ctx.DECIMAL_LIT() != null){
      PIntLit(BigInt(ctx.DECIMAL_LIT().getText)).newpos(ctx)
    } else {
      fail(ctx)
    }
  }

  override def visitBasicLit(ctx: GobraParser.BasicLitContext): PBasicLiteral = {
    if (ctx.integer()!=null){
      visitInteger(ctx.integer()).newpos(ctx)
    } else if (ctx.TRUE() != null) {
      PBoolLit(true).newpos(ctx)
    } else if (ctx.FALSE() != null) {
      PBoolLit(false).newpos(ctx)
    } else {
      fail(ctx)
    }
  }

  override def visitLiteral(ctx: GobraParser.LiteralContext): PLiteral = {
    if(ctx.basicLit() != null) {
      visitBasicLit(ctx.basicLit()).newpos(ctx)
    } else {
      fail(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitOperand(ctx: GobraParser.OperandContext): PExpression = {
    if(ctx.operandName() != null) {
      visitOperandName(ctx.operandName())
    } else if (ctx.literal() != null) {
      visitLiteral(ctx.literal())
    } else {
      fail(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitOperandName(ctx: GobraParser.OperandNameContext): PNameOrDot = {
    if (ctx.DOT() != null){
      // TODO: Fix this
      PDot(PNamedOperand(PIdnUse(ctx.IDENTIFIER(0).getText).newpos(ctx)), PIdnUse(ctx.IDENTIFIER(1).getText).newpos(ctx)).newpos(ctx)
    } else {
      PNamedOperand(PIdnUse(ctx.IDENTIFIER(0).getSymbol.getText).newpos(ctx.IDENTIFIER(0))).newpos(ctx)
    }
  }

  def visitUnkIdentifierList(ctx: GobraParser.IdentifierListContext): Vector[PIdnUnk] = {
    for (id <- ctx.IDENTIFIER().asScala.toVector) yield PIdnUnk(id.getSymbol.getText).newpos(ctx)
  }

  def visitDefIdentifierList(ctx: GobraParser.IdentifierListContext): Vector[PIdnDef] = {
    for (id <- ctx.IDENTIFIER().asScala.toVector) yield PIdnDef(id.getSymbol.getText).newpos(ctx)
  }

  override def visitShortVarDecl(ctx: GobraParser.ShortVarDeclContext): PShortVarDecl = {
    val left = visitUnkIdentifierList(ctx.identifierList())
    val right = visitExpressionList(ctx.expressionList())
    val addressable = Vector.fill(left.length)(true)
    PShortVarDecl(right, left, addressable).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitSignature(ctx: GobraParser.SignatureContext): (Vector[PParameter], PResult) = {
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty)
    (params.flatten, result)
  }

  /**
    * ``
    *
    * <p>The default implementation returns the result of calling
    * `#` on `ctx`.</p>
    */
  override def visitMethodDecl(ctx: GobraParser.MethodDeclContext): PMethodDecl = super.visitMethodDecl(ctx).asInstanceOf[PMethodDecl]

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitSourceFile(ctx: GobraParser.SourceFileContext): PProgram = {
    val packageClause = visitPackageClause(ctx.packageClause())
    val importDecls = for (importDecl <- ctx.importDecl().asScala.toVector) yield visitImportDecl(importDecl)
    val functionDecls = for (funcDecl <- ctx.functionDecl().asScala.toVector) yield visitFunctionDecl(funcDecl)
    val methodDecls = for (methodDecl <- ctx.methodDecl().asScala.toVector) yield visitMethodDecl(methodDecl)
    val decls = for (decl <- ctx.declaration().asScala.toVector) yield visitDeclaration(decl)
    PProgram(packageClause, importDecls.flatten, functionDecls ++ methodDecls ++ decls.flatten).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitPackageClause(ctx: GobraParser.PackageClauseContext): PPackageClause = {
    PPackageClause(PPkgDef(ctx.packageName.getText)).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitString_(ctx: GobraParser.String_Context): PStringLit = {
    var str : String = null
    if (ctx.RAW_STRING_LIT() != null){
      str = ctx.RAW_STRING_LIT().getText
    } else {
      str = ctx.INTERPRETED_STRING_LIT().getText
    }
    PStringLit(str.substring(1,str.length-1)).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitImportSpec(ctx: GobraParser.ImportSpecContext): PImport = {
    val path = visitString_(ctx.importPath().string_()).lit
    if(ctx.DOT() != null){
      PUnqualifiedImport(path).newpos(ctx)
    } else if (ctx.IDENTIFIER() != null) {
      PExplicitQualifiedImport(PIdnDef(ctx.IDENTIFIER().getText).newpos(ctx), path).newpos(ctx)
    } else {
      PImplicitQualifiedImport(path).newpos(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitImportDecl(ctx: GobraParser.ImportDeclContext): Vector[PImport] = {
    for (imp <- ctx.importSpec().asScala.toVector) yield visitImportSpec(imp)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitConstSpec(ctx: GobraParser.ConstSpecContext): PConstDecl = {
    val typ = if (ctx.type_() != null) Some(visitType_(ctx.type_())) else None

    val left = visitDefIdentifierList(ctx.identifierList())
    val right = visitExpressionList(ctx.expressionList())

    PConstDecl(typ, right, left).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitConstDecl(ctx: GobraParser.ConstDeclContext): Vector[PConstDecl] = {
    for (spec <- ctx.constSpec().asScala.toVector) yield visitConstSpec(spec)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeSpec(ctx: GobraParser.TypeSpecContext): PTypeDecl = {
    val left = PIdnDef(ctx.IDENTIFIER().getText).newpos(ctx.IDENTIFIER())
    val right = visitType_(ctx.type_())
    if (ctx.ASSIGN() != null) {
      PTypeAlias(right, left).newpos(ctx)
    } else {
      PTypeDef(right, left).newpos(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeDecl(ctx: GobraParser.TypeDeclContext): Vector[PDeclaration] = {
    for (spec <- ctx.typeSpec().asScala.toVector) yield visitTypeSpec(spec)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitVarSpec(ctx: GobraParser.VarSpecContext): PVarDecl = {
    fail(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitVarDecl(ctx: GobraParser.VarDeclContext): Vector[PVarDecl] = {
    for (spec <- ctx.varSpec().asScala.toVector) yield visitVarSpec(spec)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitDeclaration(ctx: GobraParser.DeclarationContext): Vector[PDeclaration] = {
    if(ctx.constDecl() != null){
      visitConstDecl(ctx.constDecl())
    } else if(ctx.typeDecl() != null){
      visitTypeDecl(ctx.typeDecl())
    } else if(ctx.varDecl() != null) {
      visitVarDecl(ctx.varDecl())
    } else {
      fail(ctx)
    }
  }


  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitExpressionList(ctx: GobraParser.ExpressionListContext): Vector[PExpression] = {
    for (expr <- ctx.expression().asScala.toVector) yield visitExpression(expr)
  }



  /**
    * ``
    *
    * <p>The default implementation returns the result of calling
    * `#` on `ctx`.</p>
    */
  override def visitSpecification(ctx: GobraParser.SpecificationContext): PFunctionSpec = {
    val pres = for (pre <- ctx.specStatement().asScala.toVector if pre.PRE() != null) yield visitExpression(pre.assertion().expression())
    val posts  = for(post <- ctx.specStatement().asScala.toVector if post.POST()!= null) yield visitExpression(post.assertion().expression())

    PFunctionSpec(pres, Vector.empty, posts, Vector.empty).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitFunctionDecl(ctx: GobraParser.FunctionDeclContext): PFunctionDecl = {
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    val id = PIdnDef(ctx.IDENTIFIER().getText).newpos(ctx.IDENTIFIER())
    val sig = visitSignature(ctx.signature())
    val paramInfo = PBodyParameterInfo(Vector.empty).newpos(ctx)
    val block = if (ctx.block() == null) PBlock(Vector.empty).newpos(ctx) else visitBlock(ctx.block())
    val body = (paramInfo, block)
    PFunctionDecl(id, sig._1, sig._2, spec, Some(body)).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitBlock(ctx: GobraParser.BlockContext): PBlock =  {
    if (ctx.statementList() != null) PBlock(visitStatementList(ctx.statementList())).newpos(ctx) else PBlock(Vector.empty).newpos(ctx)
  }

  override def visitSimpleStmt(ctx: GobraParser.SimpleStmtContext): PSimpleStmt = {
    if (ctx.shortVarDecl() != null){
      visitShortVarDecl(ctx.shortVarDecl())
    } else if (ctx.assignment() != null) {
      visitAssignment(ctx.assignment())
    } else {
      fail(ctx)
    }

  }

  /**
    * ``
    *
    * <p>The default implementation returns the result of calling
    * `#` on `ctx`.</p>
    */
  override def visitReturnStmt(ctx: GobraParser.ReturnStmtContext): PReturn = {
    PReturn(visitExpressionList(ctx.expressionList())).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostStatement(ctx: GobraParser.GhostStatementContext): PGhostStatement = {
    if (ctx.ASSERT() != null) {
      PAssert(visitExpression(ctx.expression())).newpos(ctx)
    } else {
      fail(ctx)
    }
  }

  override def visitStatement(ctx: GobraParser.StatementContext): PStatement = {
    if(ctx.simpleStmt() != null){
      visitSimpleStmt(ctx.simpleStmt())
    } else if (ctx.returnStmt() != null){
      visitReturnStmt(ctx.returnStmt())
    } else if (ctx.ghostStatement() != null) {
      visitGhostStatement(ctx.ghostStatement())
    } else {
      fail(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitStatementList(ctx: GobraParser.StatementListContext): Vector[PStatement] = {
    for (stmt <- ctx.statement().asScala.toVector) yield visitStatement(stmt)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitAssignment(ctx: GobraParser.AssignmentContext): PAssignment = {
    val left =  visitExpressionList(ctx.expressionList(0)).asInstanceOf[Vector[PAssignee]]
    val right =  visitExpressionList(ctx.expressionList(1))
    PAssignment(right, left)
      .newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodSpec(ctx: GobraParser.MethodSpecContext): PMethodSig = {
    val id = PIdnDef(ctx.IDENTIFIER().getText).newpos(ctx)
    val args = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty).newpos(ctx)
    PMethodSig(id, args.flatten, result, PFunctionSpec(Vector.empty,Vector.empty,Vector.empty,Vector.empty).newpos(ctx), isGhost = false).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitInterfaceType(ctx: GobraParser.InterfaceTypeContext): PInterfaceType = {
    val methodDecls = for (meth <- ctx.methodSpec().asScala.toVector) yield visitMethodSpec(meth).newpos(ctx)
    val embedded = Vector[PInterfaceName]()
    val predicateDecls = Vector[PMPredicateSig]()
    PInterfaceType(embedded, methodDecls, predicateDecls).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeLit(ctx: GobraParser.TypeLitContext): PTypeLit = {
    if(ctx.interfaceType() != null){
      visitInterfaceType(ctx.interfaceType())
    } else {
      fail(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitType_(ctx: GobraParser.Type_Context): PType = {
    if (ctx.typeLit() != null) {
      visitTypeLit(ctx.typeLit())
    } else if (ctx.typeName() != null){
      visitTypeName(ctx.typeName())
    } else {
      null
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitTypeName(ctx: GobraParser.TypeNameContext): PPredeclaredType = {
    PIntType().newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitResult(ctx: GobraParser.ResultContext): PResult = {
    if (ctx.parameters() != null) {
      val results = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
      PResult(results.flatten).newpos(ctx)
    } else {
      PResult(Vector(PUnnamedParameter(visitType_(ctx.type_())).newpos(ctx))).newpos(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitParameterDecl(ctx: GobraParser.ParameterDeclContext): Vector[PParameter] = {
    val typ = visitType_(ctx.type_())
    if(ctx.identifierList() != null) {
      for (id <- ctx.identifierList().IDENTIFIER().asScala.toVector) yield PNamedParameter(PIdnDef(id.getText).newpos(ctx), typ.copy).newpos(id)
    } else {
      Vector[PParameter](PUnnamedParameter(typ).newpos(ctx.type_()))
    }
  }


  implicit class PositionedPAstNode[N <: PNode](node: N) {

    def newpos(term: TerminalNode): N = {
      val (start, finish) = getStartFinish(term)
      pom.positions.setStart(node, start)
      pom.positions.setFinish(node, finish)
      node
    }

    def newpos(ctx: ParserRuleContext): N = {
      val (start, finish) = getStartFinish(ctx)
      pom.positions.setStart(node, start)
      pom.positions.setFinish(node, finish)
      node
    }

    def at(other: PNode): N = {
      pom.positions.dupPos(other, node)
    }

    def range(from: PNode, to: PNode): N = {
      pom.positions.dupRangePos(from, to, node)
    }

    def copy: N = rewriter.deepclone(node)
  }

  def getStartFinish(term: TerminalNode): (Position, Position) = {
    val tok = term.getSymbol
    val start = Position(tok.getLine, tok.getCharPositionInLine+1, source)
    val end = Position(tok.getLine, tok.getCharPositionInLine+term.getText.length+1, source)
    (start, end)
  }

  def getStartFinish(ctx: ParserRuleContext): (Position, Position) = {
    val start = Position(ctx.start.getLine, ctx.start.getCharPositionInLine+1, source)
    val end = Position(ctx.stop.getLine, ctx.stop.getCharPositionInLine+ctx.stop.getText.length+1, source)
    (start, end)
  }

  private def fail(ctx: ParserRuleContext) = {
    val (start, finish) = getStartFinish(ctx)
    val rule = GobraParser.ruleNames.array(ctx.getRuleIndex)
    throw TranslationException("Translation of " + rule + " " +  ctx.getText + " failed", start, finish)
  }

  class PRewriter(override val positions: Positions) extends PositionedRewriter with Cloner {

  }

}

case class TranslationException(msg: String, start: Position, finish : Position)  extends Exception(msg)

