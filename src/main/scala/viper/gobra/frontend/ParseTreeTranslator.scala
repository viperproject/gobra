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
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */



  override def visitExpression(ctx: GobraParser.ExpressionContext): PExpression = {
    if(ctx.primaryExpr() != null){
      visitPrimaryExpr(ctx.primaryExpr()).newpos(ctx)
    } else if(ctx.unaryExpr() != null){
      null
    } else {
      val left = visitExpression(ctx.expression(0))
      val right = visitExpression(ctx.expression(1))
      if (ctx.rel_op != null) {
        ctx.rel_op.getType match {
          case GobraParser.EQUALS =>
            PEquals(left, right).newpos(ctx)
        }
      } else if (ctx.add_op != null) {
        ctx.add_op.getType match {
          case GobraParser.PLUS =>
            PAdd(left, right).newpos(ctx)
        }
      } else {
        null
      }
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitPrimaryExpr(ctx: GobraParser.PrimaryExprContext): PExpression ={
    visitOperand(ctx.operand()).newpos(ctx)
  }

  override def visitInteger(ctx: GobraParser.IntegerContext): PIntLit = {
    if(ctx.DECIMAL_LIT() != null){
      PIntLit(BigInt(ctx.DECIMAL_LIT().getText)).newpos(ctx)
    } else {
      null
    }
  }

  override def visitBasicLit(ctx: GobraParser.BasicLitContext): PBasicLiteral = {
    if (ctx.integer()!=null){
      visitInteger(ctx.integer()).newpos(ctx)
    } else {
      null
    }
  }

  override def visitLiteral(ctx: GobraParser.LiteralContext): PLiteral = {
    if(ctx.basicLit() != null) {
      visitBasicLit(ctx.basicLit()).newpos(ctx)
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
  override def visitOperand(ctx: GobraParser.OperandContext): PExpression = {
    if(ctx.operandName() != null) {
      visitOperandName(ctx.operandName()).newpos(ctx)
    } else if (ctx.literal() != null) {
      visitLiteral(ctx.literal()).newpos(ctx)
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
  override def visitOperandName(ctx: GobraParser.OperandNameContext): PNamedOperand = {
    PNamedOperand(PIdnUse(ctx.IDENTIFIER(0).getSymbol.getText)).newpos(ctx)
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
    PProgram(packageClause, importDecls, functionDecls ++ methodDecls ++ decls.flatten).newpos(ctx)
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
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitImportDecl(ctx: GobraParser.ImportDeclContext): PImport = super.visitImportDecl(ctx).asInstanceOf[PImport]

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
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitDeclaration(ctx: GobraParser.DeclarationContext): Vector[PDeclaration] = {
    if(ctx.constDecl() != null){
      visitConstDecl(ctx.constDecl())
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

    PFunctionSpec(pres, Vector.empty, posts).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitFunctionDecl(ctx: GobraParser.FunctionDeclContext): PFunctionDecl = {
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty)
    val id = PIdnDef(ctx.IDENTIFIER().getText)
    val sig = visitSignature(ctx.signature())
    val paramInfo = PBodyParameterInfo(Vector.empty)
    val block = visitBlock(ctx.block())
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
    visitShortVarDecl(ctx.shortVarDecl()).newpos(ctx)
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

  override def visitStatement(ctx: GobraParser.StatementContext): PStatement = {
    if(ctx.simpleStmt() != null){
      visitSimpleStmt(ctx.simpleStmt()).newpos(ctx)
    } else if (ctx.returnStmt() != null){
      visitReturnStmt(ctx.returnStmt()).newpos(ctx)
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
    PAssignment(left = visitExpressionList(ctx.expressionList(0)).asInstanceOf[Vector[PAssignee]],
      right = visitExpressionList(ctx.expressionList(1)))
      .newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitType_(ctx: GobraParser.Type_Context): PType = {
    if (ctx.typeName() != null){
      visitTypeName(ctx.typeName()).newpos(ctx)
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
    PResult(Vector(PUnnamedParameter(visitType_(ctx.type_())).newpos(ctx))).newpos(ctx)
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
      val tok = term.getSymbol
      pom.positions.setStart(node, Position(tok.getLine, tok.getCharPositionInLine+1, source))
      // Possibly wrong end index
      pom.positions.setFinish(node, Position(tok.getLine, tok.getCharPositionInLine+term.getText.length+1, source))
      node
    }

    def newpos(ctx: ParserRuleContext): N = {
      pom.positions.setStart(node, Position(ctx.start.getLine, ctx.start.getCharPositionInLine+1, source))
      // Possibly wrong end index
      pom.positions.setFinish(node, Position(ctx.stop.getLine, ctx.stop.getCharPositionInLine+ctx.stop.getText.length+1, source))
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

  class PRewriter(override val positions: Positions) extends PositionedRewriter with Cloner {

  }

}
