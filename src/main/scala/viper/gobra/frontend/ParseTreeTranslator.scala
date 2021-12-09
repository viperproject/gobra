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
import viper.gobra.ast.frontend.{PAssignee, PCompositeKey, PDefLikeId, PExpression, PMethodReceiveName, _}
import viper.gobra.util.{Binary, Hexadecimal, Octal}
import viper.gobra.frontend.GobraParser._

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class ParseTreeTranslator(pom: PositionManager, source: Source, specOnly : Boolean = false) extends GobraParserBaseVisitor[AnyRef] {

  private var allowWildcards = false

  val warnings : ListBuffer[TranslationWarning] = ListBuffer.empty


  def translate[Rule <: ParserRuleContext, Node](tree: Rule):  Node = {
    tree match {
      case tree: SourceFileContext => visitSourceFile(tree.asInstanceOf[SourceFileContext]).asInstanceOf[Node]
      case tree: ExpressionContext => visitExpression(tree.asInstanceOf[ExpressionContext]).asInstanceOf[Node]
      case tree: ExprOnlyContext => visitExprOnly(tree.asInstanceOf[ExprOnlyContext]).asInstanceOf[Node]
      case tree: StmtOnlyContext => visitStmtOnly(tree.asInstanceOf[StmtOnlyContext]).asInstanceOf[Node]
      case tree: FunctionDeclContext => visitFunctionDecl(tree.asInstanceOf[FunctionDeclContext]).asInstanceOf[Node]
      case tree: ImportDeclContext => visitImportDecl(tree.asInstanceOf[ImportDeclContext]).asInstanceOf[Node]
      case tree: Type_Context => visitType_(tree.asInstanceOf[Type_Context]).asInstanceOf[Node]
    }
  }


  lazy val rewriter = new PRewriter(pom.positions)


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitUnfolding(ctx: UnfoldingContext): PUnfolding = {
    val pred = visitPredicateAccess(ctx.predicateAccess())
    val op = visitExpression(ctx.expression())

    PUnfolding(pred, op).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitUnaryExpr(ctx: GobraParser.UnaryExprContext): PExpression = {
    if (ctx.primaryExpr() != null) {
      visitPrimaryExpr(ctx.primaryExpr())
    } else if (has(ctx.kind)) {
      val exp = visitExpression(ctx.expression())
      val inv : (PExpression => PExpression) = ctx.kind.getType match {
        case GobraParser.CAP => PCapacity
        case GobraParser.LEN => PLength
        case GobraParser.DOM => PMapKeys
        case GobraParser.RANGE => PMapValues
      }
      inv(exp).newpos(ctx)
    } else if (ctx.unary_op != null) {
      val e = visitExpression(ctx.expression())
      ctx.unary_op.getType match {
        case GobraParser.PLUS => PAdd(PIntLit(0).newpos(ctx), e).newpos(ctx)
        case GobraParser.MINUS => PSub(PIntLit(0).newpos(ctx), e).newpos(ctx)
        case GobraParser.EXCLAMATION => PNegation(e).newpos(ctx)
        case GobraParser.CARET => PBitNegation(e).newpos(ctx)
        case GobraParser.STAR => PDeref(e).newpos(ctx)
        case GobraParser.AMPERSAND => PReference(e).newpos(ctx)
        case op =>
          val (start, finish) = getStartFinish(ctx)
          throw TranslationException("Unsupported unary expression: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
      }
    } else if (has(ctx.unfolding())) {
      visitUnfolding(ctx.unfolding())
    } else fail(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitExprOnly(ctx: ExprOnlyContext): PExpression = {
    visitExpression(ctx.expression())
  }

  def disableWildcards[R](block: => R): R = {
    val oldAllowWildCards = allowWildcards
    allowWildcards = false
    val res = block
    allowWildcards = oldAllowWildCards
    res
  }

  def enableWildcards[R](block: => R): R = {
    val oldAllowWildCards = allowWildcards
    allowWildcards = true
    val res = block
    allowWildcards = oldAllowWildCards
    res
  }

  override def visitExpression(ctx: GobraParser.ExpressionContext): PExpression = {
    if(ctx.primaryExpr() != null){
      visitPrimaryExpr(ctx.primaryExpr()).newpos(ctx)
    } else if(ctx.unaryExpr() != null){
      visitUnaryExpr(ctx.unaryExpr())
    } else {
      disableWildcards {
        val left = visitExpression(ctx.expression(0))
        val right = visitExpression(ctx.expression(1))

        val operationType : (PExpression, PExpression) => PExpression = if (ctx.rel_op != null) {
          ctx.rel_op.getType match {
            case GobraParser.EQUALS => PEquals
            case GobraParser.NOT_EQUALS => PUnequals
            case GobraParser.LESS => PLess
            case GobraParser.LESS_OR_EQUALS => PAtMost
            case GobraParser.GREATER => PGreater
            case GobraParser.GREATER_OR_EQUALS => PAtLeast
            case op =>
              val (start, finish) = getStartFinish(ctx)
              throw TranslationException("Unsupported binary operator: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
          }
        } else if (ctx.add_op != null) {
          ctx.add_op.getType match {
            case GobraParser.PLUS => PAdd
            case GobraParser.MINUS => PSub
            case GobraParser.PLUS_PLUS => PSequenceAppend
            case op =>
              val (start, finish) = getStartFinish(ctx)
              throw TranslationException("Unsupported binary operator: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
          }
        } else if (ctx.mul_op != null) {
          ctx.mul_op.getType match {
            case GobraParser.STAR => PMul
            case GobraParser.DIV => PDiv
            case GobraParser.MOD => PMod
            case op =>
              val (start, finish) = getStartFinish(ctx)
              throw TranslationException("Unsupported binary operator: " + GobraParser.VOCABULARY.getDisplayName(op), start, finish)
          }
        } else if (ctx.p41_op != null) {
          ctx.p41_op.getType match {
            case GobraParser.IN => PIn
            case GobraParser.MULTI => PMultiplicity
            case GobraParser.SUBSET => PSubset
          }
        } else if (ctx.p42_op != null) {
          ctx.p42_op.getType match {
            case GobraParser.UNION =>  PUnion
            case GobraParser.INTERSECTION => PIntersection
            case GobraParser.SETMINUS => PSetMinus
          }
        } else if (ctx.LOGICAL_AND() != null) {
          PAnd
        } else if (ctx.LOGICAL_OR() != null){
          POr
        } else if (ctx.IMPLIES() != null) {
          PImplication
        } else if (ctx.QMARK() != null) {
          val els = visitExpression(ctx.expression(2))
          (l, r) => PConditional(l,r, els)
        } else fail(ctx)
        operationType(left, right).newpos(ctx)
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
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitAccess(ctx: AccessContext): PAccess = {
    val expr = visitExpression(ctx.expression(0))
    if(ctx.IDENTIFIER() != null){
      idnUseOrWildcard(ctx.IDENTIFIER()) match {
        case id : PIdnUse => PAccess(expr, PNamedOperand(id).newpos(ctx.IDENTIFIER())).newpos(ctx.IDENTIFIER())
        case PWildcard() => PAccess(expr, PWildcardPerm().newpos(ctx.IDENTIFIER())).newpos(ctx.IDENTIFIER())
        case _ => fail(ctx.IDENTIFIER())
      }
    } else if (ctx.expression(1) != null) {
      val perm  = visitExpression(ctx.expression(1))
      PAccess(expr, perm).newpos(ctx)
    } else {
      PAccess(expr, PFullPerm().at(expr)).newpos(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitOld(ctx: OldContext): PGhostExpression = {
    val expr = visitExpression(ctx.expression())
    if (has(ctx.oldLabelUse())) {
      val label = if (has(ctx.oldLabelUse().labelUse()))
        PLabelUse(ctx.oldLabelUse().labelUse().IDENTIFIER().getText).newpos(ctx) else
        PLabelUse(PLabelNode.lhsLabel).newpos(ctx)
      PLabeledOld(label, expr).newpos(ctx)
    } else {
      POld(expr).newpos(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitBoundVariableDecl(ctx: BoundVariableDeclContext): Vector[PBoundVariable] = {
    val typ = visitType_(ctx.elementType().type_())
    for (id <- ctx.IDENTIFIER().asScala.toVector) yield PBoundVariable(idnDef(id).newpos(ctx), typ.copy).newpos(id)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitBoundVariables(ctx: BoundVariablesContext): Vector[PBoundVariable] = {
    (for (v <- ctx.boundVariableDecl().asScala.toVector) yield visitBoundVariableDecl(v)).flatten
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTriggers(ctx: TriggersContext): Vector[PTrigger] = {
    for (trigger <- ctx.trigger().asScala.toVector) yield visitTrigger(trigger)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTrigger(ctx: TriggerContext): PTrigger = {
    PTrigger(for (expr <- ctx.expression().asScala.toVector) yield visitExpression(expr)).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostPrimaryExpr(ctx: GhostPrimaryExprContext): PGhostExpression = {
    if (ctx.range() != null) {
      val low = visitExpression(ctx.range().expression(0))
      val high = visitExpression(ctx.range().expression(1))
      ctx.range().kind.getType match {
        case GobraParser.SEQ => PRangeSequence(low, high).newpos(ctx)
        case _ => fail(ctx.range())
      }
    } else  if (ctx.access() != null) {
      visitAccess(ctx.access())
    } else if (has(ctx.typeOf())) {
      PTypeOf(visitExpression(ctx.typeOf().expression())).newpos(ctx.typeOf())
    } else if (has(ctx.isComparable)) {
      PIsComparable(visitExpression(ctx.isComparable().expression())).newpos(ctx.isComparable)
    } else if (has(ctx.old())) {
      visitOld(ctx.old())
    } else if (has(ctx.FORALL())) {
      val vars = visitBoundVariables(ctx.boundVariables())
      val triggers = visitTriggers(ctx.triggers())
      val body = visitExpression(ctx.expression())
      PForall(vars, triggers, body).newpos(ctx)
    } else fail(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitPrimaryExpr(ctx: GobraParser.PrimaryExprContext): PExpression = {
    if (ctx.operand() != null) {
      visitOperand(ctx.operand())
    } else if (ctx.ghostPrimaryExpr() != null) {
      visitGhostPrimaryExpr(ctx.ghostPrimaryExpr())
    } else if (ctx.primaryExpr() != null) {
      val pe = visitPrimaryExpr(ctx.primaryExpr())
      if (ctx.arguments() != null) {
        pe match {
          case PNamedOperand(PIdnUse("len")) => PLength(visitExpression(ctx.arguments().expressionList().expression(0)))
          case _ => PInvoke(pe, visitArguments(ctx.arguments())).newpos(ctx)
        }
      } else if(ctx.DOT() != null) {
        PDot(pe, idnUse(ctx.IDENTIFIER()).newpos(ctx)).newpos(ctx)
      } else if (ctx.index() != null) {
        PIndexedExp(pe, visitExpression(ctx.index().expression())).newpos(ctx)
      } else if (ctx.slice_() != null) {
        val low = if (ctx.slice_().low() != null) Some(visitExpression(ctx.slice_().low().expression())) else None
        val high = if (ctx.slice_().high() != null) Some(visitExpression(ctx.slice_().high().expression())) else None
        val cap = if (ctx.slice_().cap() != null) Some(visitExpression(ctx.slice_().cap().expression())) else None
        PSliceExp(pe, low, high, cap).newpos(ctx)
      } else if (ctx.seqUpdExp() != null) {
        val updates = for (upd <- ctx.seqUpdExp().seqUpdClause().asScala.toVector) yield {
          PGhostCollectionUpdateClause(visitExpression(upd.expression(0)), visitExpression(upd.expression(1))).newpos(upd)
        }
        PGhostCollectionUpdate(pe, updates).newpos(ctx)
      } else if (has(ctx.typeAssertion())) {
        val typ = visitType_(ctx.typeAssertion().type_())
        PTypeAssertion(pe, typ).newpos(ctx)
      } else fail(ctx)
    } else fail(ctx)
  }

  override def visitInteger(ctx: GobraParser.IntegerContext): PIntLit = {
    if(ctx.DECIMAL_LIT() != null){
      PIntLit(BigInt(ctx.DECIMAL_LIT().getText)).newpos(ctx)
    } else if (ctx.BINARY_LIT() != null) {
      PIntLit(BigInt(ctx.BINARY_LIT().getText.drop(2), 2), Binary).newpos(ctx)
    } else if (ctx.HEX_LIT() != null) {
      PIntLit(BigInt(ctx.HEX_LIT().getText.drop(2), 16), Hexadecimal)
    } else if (ctx.OCTAL_LIT() != null) {
      PIntLit(BigInt(ctx.OCTAL_LIT().getText.drop(2), 8), Octal)
    } else fail_msg(ctx, "This number format is not supported yet.")
  }

  override def visitBasicLit(ctx: GobraParser.BasicLitContext): PBasicLiteral = {
    if (has(ctx.NIL_LIT())) {
      PNilLit().newpos(ctx)
    }else if (ctx.integer()!=null){
      visitInteger(ctx.integer()).newpos(ctx)
    } else if (ctx.TRUE() != null) {
      PBoolLit(true).newpos(ctx)
    } else if (ctx.FALSE() != null) {
      PBoolLit(false).newpos(ctx)
    } else if (has(ctx.string_())) {
      visitString_(ctx.string_())
    } else fail(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitArrayType(ctx: ArrayTypeContext): PArrayType = {
    val typ = visitType_(ctx.elementType().type_())
    val length = visitExpression(ctx.arrayLength().expression())

    PArrayType(length, typ).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitSliceType(ctx: SliceTypeContext): PSliceType = {
    val typ = visitType_(ctx.elementType().type_())
    PSliceType(typ).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitLiteralType(ctx: LiteralTypeContext): PLiteralType = {
    if (ctx.typeName() != null) {
      visitTypeNameNoPredeclared(ctx.typeName())
    } else if (ctx.structType() != null) {
      visitStructType(ctx.structType())
    } else if(ctx.ghostTypeLit() != null) {
      visitGhostTypeLit(ctx.ghostTypeLit())
    } else if (has(ctx.sliceType())) {
      visitSliceType(ctx.sliceType())
    } else if (has(ctx.arrayType())) {
      visitArrayType(ctx.arrayType())
    } else fail_msg(ctx, "This literal type is not supported")

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitKey(ctx: KeyContext): PCompositeKey = {
    if (ctx.expression() != null) visitExpression(ctx.expression()) match {
      case n@ PNamedOperand(id) => PIdentifierKey(id).at(n)
      case n => PExpCompositeVal(n).newpos(ctx)
    } else if (ctx.literalValue() != null) {
      PLitCompositeVal(visitLiteralValue(ctx.literalValue())).newpos(ctx)
    } else if (ctx.IDENTIFIER() != null) {
      PIdentifierKey(idnUse(ctx.IDENTIFIER()).newpos(ctx)).newpos(ctx)
    } else fail(ctx)

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitElement(ctx: ElementContext): PCompositeVal = {
    if (ctx.expression() != null) {
      PExpCompositeVal(visitExpression(ctx.expression())).newpos(ctx)
    } else if (ctx.literalValue() != null) {
      PLitCompositeVal(visitLiteralValue(ctx.literalValue())).newpos(ctx)
    } else {
      fail(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitKeyedElement(ctx: KeyedElementContext): PKeyedElement = {
    val key = if (ctx.key() != null) Some(visitKey(ctx.key())) else None
    val  elem = visitElement(ctx.element())

    PKeyedElement(key, elem).newpos(ctx)
  }

  /**
    * {@inheritDoc }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitLiteralValue(ctx: LiteralValueContext): PLiteralValue = {
    val elems = if (ctx.elementList() != null)
      for (elem <- ctx.elementList().keyedElement().asScala.toVector) yield visitKeyedElement(elem)
    else Vector.empty

    PLiteralValue(elems).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitCompositeLit(ctx: CompositeLitContext): PCompositeLit = {
    val typ = visitLiteralType(ctx.literalType())
    val value = visitLiteralValue(ctx.literalValue())
    PCompositeLit(typ, value).newpos(ctx)
  }

  override def visitLiteral(ctx: GobraParser.LiteralContext): PLiteral = {
    if (ctx.compositeLit() != null) {
      visitCompositeLit(ctx.compositeLit())
    } else if(ctx.basicLit() != null) {
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
    } else if (ctx.expression() != null ) {
      visitExpression(ctx.expression())
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
      disableWildcards {
        PDot(PNamedOperand(idnUse(ctx.IDENTIFIER(0)).newpos(ctx.IDENTIFIER(0))).newpos(ctx.IDENTIFIER(0)), idnUse(ctx.IDENTIFIER(1)).newpos(ctx)).newpos(ctx)
      }
    } else {
      PNamedOperand(idnUse(ctx.IDENTIFIER(0)).newpos(ctx.IDENTIFIER(0))).newpos(ctx)
    }
  }

  def visitUnkIdentifierList(list: Vector[TerminalNode]): Vector[PIdnUnk] = {
    for (id <- list) yield PIdnUnk(id.getSymbol.getText).newpos(id)
  }

  def visitDefLikeIdentifierList(list : Vector[TerminalNode]): Vector[PDefLikeId] = {
    for (id <- list) yield idnDefOrWildcard(id).newpos(id)
  }

  override def visitShortVarDecl(ctx: GobraParser.ShortVarDeclContext): PShortVarDecl = {
    val (ids, addressable) = ctx.maybeAddressableIdentifierList().maybeAddressableIdentifier().asScala.toVector.map(ctx => (ctx.IDENTIFIER(), has(ctx.ADDR_MOD()))).unzip
    val vars = visitUnkIdentifierList(ids)
    val right = visitExpressionList(ctx.expressionList())
    PShortVarDecl(right, vars, addressable).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitSignature(ctx: GobraParser.SignatureContext): (Vector[PParameter], PResult) = {
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val params_ = ctx.parameters().parameterDecl().asScala.flatMap(visitParameterDecl)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty)
    (params.flatten, result)
  }

  def visitMethodRecvType(ctx: Type_Context) = {
    visitType_(ctx) match {
      case name@PNamedOperand(_) => PMethodReceiveName(name)
      case PDeref(name@PNamedOperand(_)) => PMethodReceivePointer(name)
      case _ => fail(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  def visitMaybeAddressableIdnDef(ctx: MaybeAddressableIdentifierContext): (PIdnDef, Boolean) = {
    (idnDef(ctx.IDENTIFIER()).newpos(ctx), has(ctx.ADDR_MOD()))
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitReceiver(ctx: ReceiverContext): PReceiver = {
    val typeName = PNamedOperand(idnUse(ctx.IDENTIFIER()).newpos(ctx.IDENTIFIER())).newpos(ctx.IDENTIFIER())
    val recvType = if (has(ctx.STAR())) PMethodReceivePointer(typeName).newpos(ctx)  else PMethodReceiveName(typeName).newpos(ctx)

    if (has(ctx.maybeAddressableIdentifier())) {
      val (id, addr) = visitMaybeAddressableIdnDef(ctx.maybeAddressableIdentifier())
      PNamedReceiver(id, recvType, addr).newpos(ctx)
    } else PUnnamedReceiver(recvType).newpos(ctx)
  }

  /**
    * ``
    *
    * <p>The default implementation returns the result of calling
    * `#` on `ctx`.</p>
    */
  override def visitMethodDecl(ctx: GobraParser.MethodDeclContext): PMethodDecl = {
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    val receiver = visitReceiver(ctx.receiver())

    val id = withWildcards{
      idnDef(ctx.IDENTIFIER()).newpos(ctx.IDENTIFIER())
    }
    val sig = visitSignature(ctx.signature())
    val paramInfo = PBodyParameterInfo(Vector.empty).newpos(ctx)
    val block = if (ctx.block() == null || specOnly) PBlock(Vector.empty) else visitBlock(ctx.block())
    val body = if (ctx.block() == null || specOnly) None else Some((paramInfo, block))
    PMethodDecl(id, receiver,sig._1, sig._2, spec, body).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFpredicateDecl(ctx: FpredicateDeclContext): PFPredicateDecl = {
    val id = withWildcards{
      idnDef(ctx.IDENTIFIER()).newpos(ctx.IDENTIFIER())
    }
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val body = visitExpression(ctx.predicateBody().expression())
    PFPredicateDecl(id, params.flatten, Some(body)).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMpredicateDecl(ctx: MpredicateDeclContext): PMPredicateDecl = {
    val id = withWildcards{
      idnDef(ctx.IDENTIFIER()).newpos(ctx.IDENTIFIER())
    }
    val receiver = visitReceiver(ctx.receiver())
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val body = visitExpression(ctx.predicateBody().expression())
    PMPredicateDecl(id, receiver, params.flatten, Some(body)).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitImplementationProofPredicateAlias(ctx: ImplementationProofPredicateAliasContext): PImplementationProofPredicateAlias = {
    val left = idnUse(ctx.IDENTIFIER()).newpos(ctx)
    val right = if (has(ctx.operandName())) {
      visitOperandName(ctx.operandName())
    } else {
      val id = idnUse(ctx.selection().IDENTIFIER()).newpos(ctx)
      if (has(ctx.selection().primaryExpr())) PDot(visitPrimaryExpr(ctx.selection().primaryExpr()), id).newpos(ctx)  else
        PDot(visitType_(ctx.selection().type_()), id).newpos(ctx)
    }
    PImplementationProofPredicateAlias(left, right).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitNonLocalReceiver(ctx: NonLocalReceiverContext): PParameter = {
    val name = visitTypeName(ctx.typeName())
    val typ = if (has(ctx.STAR())) PDeref(name).newpos(ctx) else name
    if (has(ctx.IDENTIFIER())) PNamedParameter(idnDef(ctx.IDENTIFIER()).newpos(ctx), typ).newpos(ctx)
    else PUnnamedParameter(typ).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodImplementationProof(ctx: MethodImplementationProofContext): PMethodImplementationProof = {
    val id = idnUse(ctx.IDENTIFIER()).newpos(ctx)
    val receiver = visitNonLocalReceiver(ctx.nonLocalReceiver())
    val (args, result) = visitSignature(ctx.signature())
    val isPure = has(ctx.PURE())
    val body = if (has(ctx.block())) Some((PBodyParameterInfo(Vector.empty).newpos(ctx), visitBlock(ctx.block()))) else None
    PMethodImplementationProof(id, receiver, args, result, isPure = isPure, body).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitImplementationProof(ctx: ImplementationProofContext): PImplementationProof = {
    val subT = visitType_(ctx.type_(0))
    val superT = visitType_(ctx.type_(1))
    val alias = for (a <- ctx.implementationProofPredicateAlias().asScala.toVector) yield visitImplementationProofPredicateAlias(a)
    val memberProofs = for (m <- ctx.methodImplementationProof().asScala.toVector) yield visitMethodImplementationProof(m)
    PImplementationProof(subT, superT, alias, memberProofs).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostMember(ctx: GhostMemberContext): Vector[PGhostMember] = {
    if (ctx.fpredicateDecl() != null) {
      Vector(visitFpredicateDecl(ctx.fpredicateDecl()))
    } else if (ctx.mpredicateDecl() != null) {
      Vector(visitMpredicateDecl(ctx.mpredicateDecl()))
    } else if (ctx.implementationProof() != null) {
      Vector(visitImplementationProof(ctx.implementationProof()))
    }else if (ctx.GHOST() != null) {
      if (ctx.methodDecl() != null) {
        Vector(PExplicitGhostMember(visitMethodDecl(ctx.methodDecl())).newpos(ctx))
      } else if (ctx.functionDecl() != null) {
        Vector(PExplicitGhostMember(visitFunctionDecl(ctx.functionDecl())).newpos(ctx))
      } else if (ctx.varDecl() != null) {
        visitVarDecl(ctx.varDecl()).map(PExplicitGhostMember(_))
      } else if (ctx.typeDecl() != null) {
        visitTypeDecl(ctx.typeDecl()).map(PExplicitGhostMember(_))
      } else {
        visitConstDecl(ctx.constDecl()).map(PExplicitGhostMember(_))
      }
    } else fail(ctx)
  }

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
    val ghostMembers = for (gMember <- ctx.ghostMember().asScala.toVector) yield visitGhostMember(gMember)
    val decls = for (decl <- ctx.declaration().asScala.toVector) yield visitDeclaration(decl)
    PProgram(packageClause, importDecls.flatten, functionDecls ++ methodDecls ++ decls.flatten ++ ghostMembers.flatten).newpos(ctx)
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
    val str = if (ctx.RAW_STRING_LIT() != null){
      ctx.RAW_STRING_LIT().getText
    } else {
      ctx.INTERPRETED_STRING_LIT().getText
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
      PExplicitQualifiedImport(idnDefOrWildcard(ctx.IDENTIFIER()).newpos(ctx), path).newpos(ctx)
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
    /*if (ctx.importSpec() != null) {
      Vector(visitImportSpec(ctx.importSpec()))
    } else {
      for (imp <- ctx.importList().importSpec().asScala.toVector) yield visitImportSpec(imp)
    }*/
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

    val left = visitDefLikeIdentifierList(ctx.identifierList().IDENTIFIER().asScala.toVector)
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
    val left = withWildcards{
      idnDef(ctx.IDENTIFIER()).newpos(ctx.IDENTIFIER())
    }
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
  override def visitTypeDecl(ctx: GobraParser.TypeDeclContext): Vector[PTypeDecl] = {
    for (spec <- ctx.typeSpec().asScala.toVector) yield visitTypeSpec(spec)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitVarSpec(ctx: GobraParser.VarSpecContext): PVarDecl = {
    val (ids, addressable) = ctx.maybeAddressableIdentifierList().maybeAddressableIdentifier().asScala.toVector.map(ctx => (ctx.IDENTIFIER(), has(ctx.ADDR_MOD()))).unzip
    val vars = visitDefLikeIdentifierList(ids)
    val typ = if(has(ctx.type_())) Some(visitType_(ctx.type_())) else None
    val right = if (has(ctx.expressionList())) visitExpressionList(ctx.expressionList()) else Vector.empty
    PVarDecl(typ, right, vars, addressable).newpos(ctx)

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

  def visitDeclarationStmt(ctx: GobraParser.DeclarationContext): Vector[PStatement] = {
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
    val groups = ctx.specStatement().asScala.toVector.groupBy(_.kind.getType)
    val pres = groups.getOrElse(GobraParser.PRE, Vector.empty).map(s => visitExpression(s.assertion().expression()))
    val preserves = groups.getOrElse(GobraParser.PRESERVES, Vector.empty).map(s => visitExpression(s.assertion().expression()))
    val posts = groups.getOrElse(GobraParser.POST, Vector.empty).map(s => visitExpression(s.assertion().expression()))
    val terms = groups.getOrElse(GobraParser.DEC, Vector.empty).map(s => visitTerminationMeasure(s.terminationMeasure()))
    val isPure = has(ctx.PURE()) && !ctx.PURE().isEmpty

    PFunctionSpec(pres, preserves, posts, terms, isPure = isPure).newpos(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitFunctionDecl(ctx: GobraParser.FunctionDeclContext): PFunctionDecl = {
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    val id = withWildcards{
      idnDef(ctx.IDENTIFIER()).newpos(ctx.IDENTIFIER())
    }
    val sig = visitSignature(ctx.signature())
    val paramInfo = PBodyParameterInfo(Vector.empty).newpos(ctx)
    val block = if (ctx.block() == null || specOnly) PBlock(Vector.empty) else visitBlock(ctx.block())
    val body = if (ctx.block() == null || specOnly) None else Some((paramInfo, block))
    PFunctionDecl(id, sig._1, sig._2, spec, body).newpos(ctx)
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
    } else if (ctx.expressionStmt() != null){
      PExpressionStmt(visitExpression(ctx.expressionStmt().expression())).newpos(ctx)
    } else fail(ctx)

  }

  /**
    * ``
    *
    * <p>The default implementation returns the result of calling
    * `#` on `ctx`.</p>
    */
  override def visitReturnStmt(ctx: GobraParser.ReturnStmtContext): PReturn = {
    val exprs = if (ctx.expressionList() != null) visitExpressionList(ctx.expressionList()) else Vector.empty
    PReturn(exprs).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitPredicateAccess(ctx: PredicateAccessContext): PPredicateAccess = {
    if (ctx.primaryExpr() != null ) {
      visitPrimaryExpr(ctx.primaryExpr()) match {
        case invoke : PInvoke => PPredicateAccess(invoke, PFullPerm().at(invoke)).newpos(ctx)
        case PAccess(invoke: PInvoke, perm) => PPredicateAccess(invoke, perm).newpos(ctx)
        case _ => fail_msg(ctx, "Expected invocation")
      }
    } else fail(ctx)

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostStatement(ctx: GobraParser.GhostStatementContext): PGhostStatement = {
    if (ctx.GHOST() != null) {
      PExplicitGhostStatement(visitStatement(ctx.statement())).newpos(ctx)
    } else if (ctx.ASSERT() != null) {
      PAssert(visitExpression(ctx.expression())).newpos(ctx)
    } else if (ctx.fold_stmt != null) {
      val predicateAccess = visitPredicateAccess(ctx.predicateAccess())
      ctx.fold_stmt.getType match {
        case GobraParser.FOLD => PFold(predicateAccess).newpos(ctx)
        case GobraParser.UNFOLD => PUnfold(predicateAccess).newpos(ctx)
      }
    }  else fail(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitStmtOnly(ctx: StmtOnlyContext): PStatement = {
    visitStatement(ctx.statement())
  }

  def visitIfClause(clause: IfStmtContext) : PIfClause = {
    val pre = if (clause.simpleStmt() != null) Some(visitSimpleStmt(clause.simpleStmt())) else None
    val expr = visitExpression(clause.expression())
    // Not sure if this works...
    if (clause.expression().stop.getType == GobraParser.R_CURLY) warn(clause.expression(), "struct literals at the end of if clauses must be surrounded by parens!")
    val block = visitBlock(clause.block(0))
    PIfClause(pre, expr, block).newpos(clause)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitIfStmt(ctx: IfStmtContext): PIfStmt = {
    var current = ctx
    val ifclauses = ListBuffer[IfStmtContext](current)
    while (current.ifStmt() != null) {
      current = current.ifStmt()
      ifclauses.append(current)
    }
    val ifs : Vector[PIfClause] = ifclauses.map(visitIfClause).toVector
    val els = if (current.block(1) != null) Some(visitBlock(current.block(1))) else None
    PIfStmt(ifs,els).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTerminationMeasure(ctx: TerminationMeasureContext): PTerminationMeasure = {
    val cond = if (has(ctx.expression())) Some(visitExpression(ctx.expression())) else None
    enableWildcards {
      visitExpressionList(ctx.expressionList())
    } match {
        case Vector(PNamedOperand(PIdnUse("_"))) => PWildcardMeasure(cond)
        case exprs => PTupleTerminationMeasure(exprs, cond)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitLoopSpec(ctx: LoopSpecContext): PLoopSpec = {
    val invs = for (inv <- ctx.expression().asScala.toVector) yield visitExpression(inv)
    val decs = if (has(ctx.terminationMeasure())) Some(visitTerminationMeasure(ctx.terminationMeasure())) else None

    PLoopSpec(invs, decs)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitSpecForStmt(specCtx: SpecForStmtContext): PStatement = {
    val spec = visitLoopSpec(specCtx.loopSpec())

    val ctx = specCtx.forStmt()
    val block = visitBlock(ctx.block())

    if (has(ctx.expression())) {
      PForStmt(None, visitExpression(ctx.expression()), None, spec, block).newpos(ctx)
    } else if(has(ctx.forClause())){
      val pre = if (has(ctx.forClause().initStmt)) Some(visitSimpleStmt(ctx.forClause().initStmt)) else None
      val post = if (has(ctx.forClause().initStmt)) Some(visitSimpleStmt(ctx.forClause().postStmt)) else None
      val cond = if (has(ctx.forClause().expression())) visitExpression(ctx.forClause().expression()) else PBoolLit(true).newpos(ctx)
      PForStmt(pre, cond, post, spec, block).newpos(ctx)
    } else if (has(ctx.rangeClause())) {
      val expr = visitExpression(ctx.rangeClause().expression())
      val range = PRange(expr).newpos(ctx.rangeClause())
      if (has(ctx.rangeClause().DECLARE_ASSIGN())) {
        val idList = visitUnkIdentifierList(ctx.rangeClause().identifierList().IDENTIFIER().asScala.toVector)
        PShortForRange(range, idList, block).newpos(ctx)
      } else {
        val assignees = visitExpressionList(ctx.rangeClause().expressionList()) match {
          case v : Vector[PAssignee] => v
          case _ => fail(ctx)
        }
        PAssForRange(range, assignees, block).newpos(ctx)
      }
    } else fail(ctx)

  }

  override def visitStatement(ctx: GobraParser.StatementContext): PStatement = {
    if (has(ctx.declaration())) {
      PSeq(visitDeclarationStmt(ctx.declaration())).newpos(ctx)
    } else if(ctx.simpleStmt() != null){
      visitSimpleStmt(ctx.simpleStmt())
    } else if (ctx.returnStmt() != null){
      visitReturnStmt(ctx.returnStmt())
    } else if (ctx.ghostStatement() != null) {
      visitGhostStatement(ctx.ghostStatement())
    } else if (ctx.ifStmt() != null) {
      visitIfStmt(ctx.ifStmt())
    } else if (has(ctx.specForStmt())) {
      visitSpecForStmt(ctx.specForStmt())
    } else fail(ctx)
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


  def withWildcards[T](block : => T): T =  {
    allowWildcards = true
    val res = block
    allowWildcards = false
    res
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitAssignment(ctx: GobraParser.AssignmentContext): PAssignment = {
    allowWildcards = true
    val left =  visitExpressionList(ctx.expressionList(0)) match {
      case v : Vector[PAssignee] => v
      case _ => fail(ctx)
    }
    allowWildcards = false
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
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    val id = idnDef(ctx.IDENTIFIER()).newpos(ctx)
    val args = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty).newpos(ctx)
    PMethodSig(id, args.flatten, result, spec, isGhost = false).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitPredicateSpec(ctx: PredicateSpecContext): PMPredicateSig = {
    val id = idnDef(ctx.IDENTIFIER())
    val args = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    PMPredicateSig(id, args.flatten).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitInterfaceType(ctx: GobraParser.InterfaceTypeContext): PInterfaceType = {
    val methodDecls = for (meth <- ctx.methodSpec().asScala.toVector) yield visitMethodSpec(meth).newpos(ctx)
    val embedded = for (typ <- ctx.typeName().asScala.toVector) yield
      PInterfaceName(PNamedOperand(idnUse(typ.IDENTIFIER()).newpos(typ)).newpos(typ)).newpos(typ)
    val predicateDecls = for (pred <- ctx.predicateSpec().asScala.toVector) yield visitPredicateSpec(pred)
    PInterfaceType(embedded, methodDecls, predicateDecls).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitEmbeddedField(ctx: EmbeddedFieldContext): PEmbeddedType = {
    if (ctx.STAR() != null) {
      PEmbeddedPointer(visitTypeName(ctx.typeName())).newpos(ctx)
    } else {
      PEmbeddedName(visitTypeName(ctx.typeName())).newpos(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFieldDecl(ctx: FieldDeclContext): PStructClause = {
    if (ctx.embeddedField() != null) {
      val et = visitEmbeddedField(ctx.embeddedField())
      PEmbeddedDecl(et, PIdnDef(et.name).at(et)).newpos(ctx)
    } else {
      val ids = visitDefLikeIdentifierList(ctx.identifierList().IDENTIFIER().asScala.toVector)
      val t = visitType_(ctx.type_())
      PFieldDecls(ids map (id => PFieldDecl(id.asInstanceOf[PIdnDef], t.copy).at(id))).newpos(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitStructType(ctx: StructTypeContext): PStructType = {
    val decls = for (decl <- ctx.fieldDecl().asScala.toVector) yield visitFieldDecl(decl)
    PStructType(decls).newpos(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeLit(ctx: GobraParser.TypeLitContext): PTypeLit = {
    if (has(ctx.pointerType())) {
      PDeref(visitType_(ctx.pointerType().type_())).newpos(ctx)
    } else if(ctx.structType() != null) {
      visitStructType(ctx.structType())
    }else if(ctx.interfaceType() != null){
      visitInterfaceType(ctx.interfaceType())
    } else if (has(ctx.sliceType())) {
      visitSliceType(ctx.sliceType())
    } else  if (has(ctx.arrayType())) {
      visitArrayType(ctx.arrayType())
    } else fail(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitSqType(ctx: SqTypeContext): PGhostLiteralType = {
    val typ = visitType_(ctx.type_(0))
    val kind : (PType => PGhostLiteralType) = ctx.kind.getType match {
      case GobraParser.SEQ => PSequenceType
      case GobraParser.SET => PSetType
      case GobraParser.MSET => PMultisetType
      case GobraParser.DICT => PMathematicalMapType(_, visitType_(ctx.type_(1)))
      case GobraParser.OPT => POptionType
    }
    kind(typ).newpos(ctx)

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostTypeLit(ctx: GhostTypeLitContext): PGhostLiteralType = {
    if(ctx.sqType() != null) {
      visitSqType(ctx.sqType())
    } else fail(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitType_(ctx: GobraParser.Type_Context): PType = {
    if(ctx.type_() != null) {
      visitType_(ctx.type_())
    }else if (ctx.typeLit() != null) {
      visitTypeLit(ctx.typeLit())
    } else  if (ctx.typeName() != null){
      if (ctx.typeName().IDENTIFIER() != null) {
        visitTypeName(ctx.typeName())
      } else {
        val base = ctx.typeName().qualifiedIdent().IDENTIFIER(0)
        val id = ctx.typeName().qualifiedIdent().IDENTIFIER(1)
        PDot(PNamedOperand(idnUse(base).newpos(base)).newpos(base), idnUse(id).newpos(id)).newpos(ctx.typeName())
      }
    } else if (ctx.ghostTypeLit() != null) {
        visitGhostTypeLit(ctx.ghostTypeLit())
    } else {
      fail_msg(ctx, "in visitType_")
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitQualifiedIdent(ctx: QualifiedIdentContext): PDot = {
    PDot(PNamedOperand(idnUse(ctx.IDENTIFIER(0)).newpos(ctx)).newpos(ctx.IDENTIFIER(0)), idnUse(ctx.IDENTIFIER(1)).newpos(ctx.IDENTIFIER(1))).newpos(ctx)
  }

  def visitTypeIdentifier(typ: TerminalNode): Either[PPredeclaredType, PNamedOperand] = {
    typ.getSymbol.getText match {
      case "int" => Left(PIntType().newpos(typ))
      case "int16" => Left(PInt16Type().newpos(typ))
      case "int32" => Left(PInt32Type().newpos(typ))
      case "int64" => Left(PInt64Type().newpos(typ))
      case "uint" => Left(PUIntType().newpos(typ))
      case "uint16" => Left(PUInt16Type().newpos(typ))
      case "uint32" => Left(PUInt32Type().newpos(typ))
      case "uint64" => Left(PUInt64Type().newpos(typ))
      case "bool" => Left(PBoolType().newpos(typ))
      case "string" => Left(PStringType().newpos(typ))
      case _ => Right(PNamedOperand(idnUse(typ).newpos(typ)).newpos(typ))
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  def visitTypeNameNoPredeclared(ctx: GobraParser.TypeNameContext): PLiteralType = {
    if (ctx.IDENTIFIER() != null) visitTypeIdentifier(ctx.IDENTIFIER()) match {
      case Right(t) => t
      case Left(_) => fail(ctx)
    }
    else if (ctx.qualifiedIdent() != null) {
      visitQualifiedIdent(ctx.qualifiedIdent())
    } else fail_msg(ctx, "Predeclared Type where not applicable")
  }

  override def visitTypeName(ctx: GobraParser.TypeNameContext): PNamedType = {
    if (ctx.IDENTIFIER() != null)
      visitTypeIdentifier(ctx.IDENTIFIER()).merge
    else fail_msg(ctx, "in visitTypeName")
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
    val params = if(ctx.identifierList() != null) {
      for (id <- ctx.identifierList().IDENTIFIER().asScala.toVector) yield PNamedParameter(idnDef(id).newpos(ctx), typ.copy).newpos(id)
    } else {
      Vector[PActualParameter](PUnnamedParameter(typ).newpos(ctx.type_()))
    }

    if (ctx.GHOST() != null) {
      params.map(PExplicitGhostParameter(_).newpos(ctx))
    } else params
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

  def has(a : AnyRef): Boolean = {
    a != null
  }

  private def fail(ctx: ParserRuleContext) = {
    fail_msg(ctx, "")
  }

  private def fail(term: TerminalNode) = {
    fail_msg(term, "")
  }

  private def fail_msg(ctx: ParserRuleContext, msg : String) = {
    val (start, finish) = getStartFinish(ctx)
    val rule = GobraParser.ruleNames.array(ctx.getRuleIndex)
    val text = ctx.getText
    throw TranslationException(s"Translation of $rule $text failed: " + msg, start, finish)
  }

  private def fail_msg(term: TerminalNode, msg : String) = {
    val (start, finish) = getStartFinish(term)
    val rule = GobraParser.VOCABULARY.getDisplayName(term.getSymbol.getType)
    val text = term.getText
    throw TranslationException(s"Translation of $rule $text failed: " + msg, start, finish)
  }

  type TranslationWarning = TranslationException

  private def warn(ctx: ParserRuleContext, msg : String): Unit = {
    val (start, finish) = getStartFinish(ctx)
    val rule = GobraParser.ruleNames.array(ctx.getRuleIndex)
    val text = ctx.getText
    warnings.append(TranslationException(s"Warning in $rule $text: " + msg, start, finish))
  }

  private def idnUse(id: TerminalNode): PIdnUse = {
    if (allowWildcards) {
      PIdnUse(id.getSymbol.getText)
    } else idnUseOrWildcard(id, allowWildCard = false).asInstanceOf[PIdnUse]
  }

  private def idnUseOrWildcard(id : TerminalNode, allowWildCard: Boolean = true): PUseLikeId = {
    val name = id.getSymbol.getText
    if(name == "_"){
      if (allowWildCard) {
        PWildcard()
      } else {
        fail(id)
      }
    } else {
      PIdnUse(name)
    }
  }

  private def idnDef(id: TerminalNode): PIdnDef = {
    if (allowWildcards) {
      PIdnDef(id.getSymbol.getText)
    } else idnDefOrWildcard(id, allowWildCard = false).asInstanceOf[PIdnDef]
  }

  private def idnDefOrWildcard(id : TerminalNode, allowWildCard: Boolean = true):  PDefLikeId = {
    val name = id.getSymbol.getText
    if(name == "_"){
      if (allowWildCard) {
        PWildcard()
      } else {
        fail(id)
      }
    } else {
      PIdnDef(name)
    }
  }

  class PRewriter(override val positions: Positions) extends PositionedRewriter with Cloner {

  }

}

case class TranslationException(msg: String, start: Position, finish : Position)  extends Exception(msg)

