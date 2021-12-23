// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.


package viper.gobra.frontend
import org.antlr.v4.runtime.{ParserRuleContext, Token}
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
      case tree: SourceFileContext => visitSourceFile(tree).asInstanceOf[Node]
      case tree: ExpressionContext => visitExpression(tree).asInstanceOf[Node]
      case tree: ExprOnlyContext => visitExprOnly(tree).asInstanceOf[Node]
      case tree: StmtOnlyContext => visitStmtOnly(tree).asInstanceOf[Node]
      case tree: FunctionDeclContext => visitFunctionDecl(tree).asInstanceOf[Node]
      case tree: ImportDeclContext => visitImportDecl(tree).asInstanceOf[Node]
      case tree: Type_Context => visitType_(tree).asInstanceOf[Node]
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
    PUnfolding(pred, op).at(ctx)
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
      inv(exp).at(ctx)
    } else if (ctx.unary_op != null) {
      val e = visitExpression(ctx.expression())
      ctx.unary_op.getType match {
        case GobraParser.PLUS => PAdd(PIntLit(0).at(ctx), e).at(ctx)
        case GobraParser.MINUS => PSub(PIntLit(0).at(ctx), e).at(ctx)
        case GobraParser.EXCLAMATION => PNegation(e).at(ctx)
        case GobraParser.CARET => PBitNegation(e).at(ctx)
        case GobraParser.STAR => PDeref(e).at(ctx)
        case GobraParser.AMPERSAND => PReference(e).at(ctx)
        case op =>
          val (start, finish) = ctx.startFinish
          throw UnsupportedOperatorException(ctx.unary_op, "unary", "")
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
    if (ctx.primaryExpr() != null){
      visitPrimaryExpr(ctx.primaryExpr()).at(ctx)
    } else if(ctx.call_op != null) {
      val exp = visitExpression(ctx.expression(0))
      val call : PExpression => PExpression = ctx.call_op.getType match {
        case GobraParser.CAP => PCapacity
        case GobraParser.LEN => PLength
        case GobraParser.DOM => PMapKeys
        case GobraParser.RANGE => PMapValues
      }
      call(exp).at(ctx)
    } else if (has(ctx.new_())) {
      val typ = visitType_(ctx.new_().type_())
      PNew(typ).at(ctx)
    } else if (has(ctx.make())) {
      val typ = visitType_(ctx.make().type_())
      val args = visitExpressionList(ctx.make().expressionList())
      PMake(typ, args).at(ctx)
    } else if (has(ctx.unfolding())) {
      visitUnfolding(ctx.unfolding())
    } else if (ctx.unary_op != null) {
      val e = visitExpression(ctx.expression(0))
      val unary : PExpression => PExpression = ctx.unary_op.getType match {
        case GobraParser.PLUS => PAdd(PIntLit(0).at(ctx), _)
        case GobraParser.MINUS => PSub(PIntLit(0).at(ctx), _)
        case GobraParser.EXCLAMATION => PNegation
        case GobraParser.CARET => PBitNegation
        case GobraParser.STAR => PDeref
        case GobraParser.AMPERSAND => PReference
        case GobraParser.RECEIVE => PReceive
        case _ => throw UnsupportedOperatorException(ctx.unary_op, "unary")
      }
      unary(e).at(ctx)
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
            case _ => throw UnsupportedOperatorException(ctx.rel_op, "relation")
          }
        } else if (ctx.add_op != null) {
          ctx.add_op.getType match {
            case GobraParser.PLUS => PAdd
            case GobraParser.MINUS => PSub
            case GobraParser.OR => PBitOr
            case GobraParser.CARET => PBitXor
            case GobraParser.PLUS_PLUS => PSequenceAppend
            case op =>
              throw UnsupportedOperatorException(ctx.add_op, "addition")
          }
        } else if (ctx.mul_op != null) {
          ctx.mul_op.getType match {
            case GobraParser.STAR => PMul
            case GobraParser.DIV => PDiv
            case GobraParser.MOD => PMod
            case op => throw UnsupportedOperatorException(ctx.mul_op, "multiplication")
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
        operationType(left, right).at(ctx)
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
      exprs.init.appended(PUnpackSlice(exprs.last).at(ctx))
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
      idnUseOrWildcard(ctx.IDENTIFIER()).at(ctx.IDENTIFIER()) match {
        case id : PIdnUse => PAccess(expr, PNamedOperand(id).at(ctx.IDENTIFIER())).at(ctx.IDENTIFIER())
        case PWildcard() => PAccess(expr, PWildcardPerm().at(ctx.IDENTIFIER())).at(ctx.IDENTIFIER())
        case _ => fail(ctx.IDENTIFIER())
      }
    } else if (ctx.expression(1) != null) {
      val perm  = visitExpression(ctx.expression(1))
      PAccess(expr, perm).at(ctx)
    } else {
      PAccess(expr, PFullPerm().at(expr)).at(ctx)
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
        PLabelUse(ctx.oldLabelUse().labelUse().IDENTIFIER().getText).at(ctx) else
        PLabelUse(PLabelNode.lhsLabel).at(ctx)
      PLabeledOld(label, expr).at(ctx)
    } else {
      POld(expr).at(ctx)
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
    for (id <- ctx.IDENTIFIER().asScala.toVector) yield PBoundVariable(idnDef(id).at(ctx), typ.copy).at(id)
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
    PTrigger(for (expr <- ctx.expression().asScala.toVector) yield visitExpression(expr)).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitSConversion(ctx: SConversionContext): PGhostCollectionExp = {
    val exp = visitExpression(ctx.expression())
    val conversion = ctx.kind.getType match {
      case GobraParser.SEQ => PSequenceConversion
      case GobraParser.SET => PSetConversion
      case GobraParser.MSET => PMultisetConversion
    }
    conversion(exp).at(ctx)
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
      val seqrange = PRangeSequence(low, high).at(ctx)
      ctx.range().kind.getType match {
        case GobraParser.SEQ => seqrange
        case GobraParser.SET => PSetConversion(seqrange).at(ctx)
        case GobraParser.MSET => PMultisetConversion(seqrange).at(ctx)
      }
    } else  if (ctx.access() != null) {
      visitAccess(ctx.access())
    } else if (has(ctx.typeOf())) {
      PTypeOf(visitExpression(ctx.typeOf().expression())).at(ctx.typeOf())
    } else if (has(ctx.isComparable())) {
      PIsComparable(visitExpression(ctx.isComparable.expression())).at(ctx.isComparable)
    } else if (has(ctx.old())) {
      visitOld(ctx.old())
    } else if (has(ctx.FORALL())) {
      val vars = visitBoundVariables(ctx.boundVariables())
      val triggers = visitTriggers(ctx.triggers())
      val body = visitExpression(ctx.expression())
      PForall(vars, triggers, body).at(ctx)
    } else if (has(ctx.sConversion())) {
      visitSConversion(ctx.sConversion)
    } else if (has(ctx.optionNone())) {
      val typ = visitType_(ctx.optionNone().type_())
      POptionNone(typ).at(ctx)
    } else if (has(ctx.optionSome())) {
      val exp = visitExpression(ctx.optionSome().expression())
      POptionSome(exp).at(ctx)
    } else if (has(ctx.optionGet())) {
      val exp = visitExpression(ctx.optionGet().expression())
      POptionGet(exp).at(ctx)
    } else fail(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitConversion(ctx: ConversionContext): PInvoke= {
    val typ = visitType_(ctx.type_())
    val exp = visitExpression(ctx.expression())

    PInvoke(typ, Vector(exp)).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitPredConstructArgs(ctx: PredConstructArgsContext): Vector[Option[PExpression]] = {
    val exprs = withWildcards {
       visitExpressionList(ctx.expressionList())
    }
    exprs.map {
      case PNamedOperand(PIdnUse("_")) => None
      case e => Some(e)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodExpr(ctx: MethodExprContext): PDot = {
    val recv = visitType_(ctx.receiverType().type_())
    val id = idnUse(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())
    PDot(recv, id).at(ctx)
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
          case _ => PInvoke(pe, visitArguments(ctx.arguments())).at(ctx)
        }
      } else if(ctx.DOT() != null) {
        PDot(pe, idnUse(ctx.IDENTIFIER()).at(ctx)).at(ctx)
      } else if (ctx.index() != null) {
        PIndexedExp(pe, visitExpression(ctx.index().expression())).at(ctx)
      } else if (ctx.slice_() != null) {
        val low = if (ctx.slice_().low() != null) Some(visitExpression(ctx.slice_().low().expression())) else None
        val high = if (ctx.slice_().high() != null) Some(visitExpression(ctx.slice_().high().expression())) else None
        val cap = if (ctx.slice_().cap() != null) Some(visitExpression(ctx.slice_().cap().expression())) else None
        PSliceExp(pe, low, high, cap).at(ctx)
      } else if (ctx.seqUpdExp() != null) {
        val updates = for (upd <- ctx.seqUpdExp().seqUpdClause().asScala.toVector) yield {
          PGhostCollectionUpdateClause(visitExpression(upd.expression(0)), visitExpression(upd.expression(1))).at(upd)
        }
        PGhostCollectionUpdate(pe, updates).at(ctx)
      } else if (has(ctx.typeAssertion())) {
        val typ = visitType_(ctx.typeAssertion().type_())
        PTypeAssertion(pe, typ).at(ctx)
      } else if (has(ctx.predConstructArgs())) {
        val args = visitPredConstructArgs(ctx.predConstructArgs())
        val id = pe match {
          case recvWithId@PDot(_, _) => PDottedBase(recvWithId).at(recvWithId)
          case PNamedOperand(identifier@PIdnUse(_)) => PFPredBase(identifier).at(identifier)
          case _ => fail(ctx.primaryExpr(), "Wrong base type for predicate constructor.")
        }
        PPredConstructor(id, args).at(ctx)
      } else fail(ctx)
    } else if (has(ctx.conversion())) {
      visitConversion(ctx.conversion())
    } else if (has(ctx.methodExpr())) {
      visitMethodExpr(ctx.methodExpr())
    } else fail(ctx)
  }

  override def visitInteger(ctx: GobraParser.IntegerContext): PIntLit = {
    if(ctx.DECIMAL_LIT() != null){
      PIntLit(BigInt(ctx.DECIMAL_LIT().getText)).at(ctx)
    } else if (ctx.BINARY_LIT() != null) {
      PIntLit(BigInt(ctx.BINARY_LIT().getText.drop(2), 2), Binary).at(ctx)
    } else if (ctx.HEX_LIT() != null) {
      PIntLit(BigInt(ctx.HEX_LIT().getText.drop(2), 16), Hexadecimal)
    } else if (ctx.OCTAL_LIT() != null) {
      PIntLit(BigInt(ctx.OCTAL_LIT().getText.drop(2), 8), Octal)
    } else fail(ctx, "This number format is not supported yet.")
  }

  override def visitBasicLit(ctx: GobraParser.BasicLitContext): PBasicLiteral = {
    if (has(ctx.NIL_LIT())) {
      PNilLit().at(ctx)
    }else if (ctx.integer()!=null){
      visitInteger(ctx.integer()).at(ctx)
    } else if (ctx.TRUE() != null) {
      PBoolLit(true).at(ctx)
    } else if (ctx.FALSE() != null) {
      PBoolLit(false).at(ctx)
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

    PArrayType(length, typ).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitSliceType(ctx: SliceTypeContext): PSliceType = {
    val typ = visitType_(ctx.elementType().type_())
    PSliceType(typ).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMapType(ctx: MapTypeContext): PMapType = {
    val key = visitType_(ctx.type_())
    val elem = visitType_(ctx.elementType().type_())

    PMapType(key, elem).at(ctx)
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
    } else if (has(ctx.ELLIPSIS())){
      val typ = visitType_(ctx.elementType().type_())
      PImplicitSizeArrayType(typ).at(ctx)
    } else if (has(ctx.mapType())) {
      visitMapType(ctx.mapType())
    } else fail(ctx, "This literal type is not supported.")



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
      case n => PExpCompositeVal(n).at(ctx)
    } else if (ctx.literalValue() != null) {
      PLitCompositeVal(visitLiteralValue(ctx.literalValue())).at(ctx)
    } else if (ctx.IDENTIFIER() != null) {
      PIdentifierKey(idnUse(ctx.IDENTIFIER()).at(ctx)).at(ctx)
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
      PExpCompositeVal(visitExpression(ctx.expression())).at(ctx)
    } else if (ctx.literalValue() != null) {
      PLitCompositeVal(visitLiteralValue(ctx.literalValue())).at(ctx)
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

    PKeyedElement(key, elem).at(ctx)
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

    PLiteralValue(elems).at(ctx)
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
    PCompositeLit(typ, value).at(ctx)
  }

  override def visitLiteral(ctx: GobraParser.LiteralContext): PLiteral = {
    if (ctx.compositeLit() != null) {
      visitCompositeLit(ctx.compositeLit())
    } else if(ctx.basicLit() != null) {
      visitBasicLit(ctx.basicLit()).at(ctx)
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
        PDot(PNamedOperand(idnUse(ctx.IDENTIFIER(0)).at(ctx.IDENTIFIER(0))).at(ctx.IDENTIFIER(0)), idnUse(ctx.IDENTIFIER(1)).at(ctx)).at(ctx)
      }
    } else {
      PNamedOperand(idnUse(ctx.IDENTIFIER(0)).at(ctx.IDENTIFIER(0))).at(ctx)
    }
  }

  def visitUnkIdentifierList(list: Vector[TerminalNode]): Vector[PIdnUnk] = {
    for (id <- list) yield PIdnUnk(id.getSymbol.getText).at(id)
  }

  def visitDefLikeIdentifierList(list : Vector[TerminalNode]): Vector[PDefLikeId] = {
    for (id <- list) yield idnDefOrWildcard(id).at(id)
  }

  override def visitShortVarDecl(ctx: GobraParser.ShortVarDeclContext): PShortVarDecl = {
    val (ids, addressable) = ctx.maybeAddressableIdentifierList().maybeAddressableIdentifier().asScala.toVector.map(ctx => (ctx.IDENTIFIER(), has(ctx.ADDR_MOD()))).unzip
    val vars = visitUnkIdentifierList(ids)
    val right = visitExpressionList(ctx.expressionList())
    PShortVarDecl(right, vars, addressable).at(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitSignature(ctx: GobraParser.SignatureContext): (Vector[PParameter], PResult) = {
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    //val params_ = ctx.parameters().parameterDecl().asScala.flatMap(visitParameterDecl)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty)
    (params.flatten, result)
  }

  def visitMethodRecvType(ctx: Type_Context): PMethodRecvType = {
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
    (idnDef(ctx.IDENTIFIER()).at(ctx), has(ctx.ADDR_MOD()))
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitReceiver(ctx: ReceiverContext): PReceiver = {
    val typeName = PNamedOperand(idnUse(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())).at(ctx.IDENTIFIER())
    val recvType = if (has(ctx.STAR())) PMethodReceivePointer(typeName).at(ctx)  else PMethodReceiveName(typeName).at(ctx)

    if (has(ctx.maybeAddressableIdentifier())) {
      val (id, addr) = visitMaybeAddressableIdnDef(ctx.maybeAddressableIdentifier())
      PNamedReceiver(id, recvType, addr).at(ctx)
    } else PUnnamedReceiver(recvType).at(ctx)
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
      idnDef(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())
    }
    val sig = visitSignature(ctx.signature())
    val paramInfo = PBodyParameterInfo(Vector.empty).at(ctx)
    val body = if (ctx.blockWithBodyParameterInfo() == null || specOnly) None else Some(visitBlockWithBodyParameterInfo(ctx.blockWithBodyParameterInfo()))
    PMethodDecl(id, receiver,sig._1, sig._2, spec, body).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFpredicateDecl(ctx: FpredicateDeclContext): PFPredicateDecl = {
    val id = withWildcards{
      idnDef(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())
    }
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val body = if (has(ctx.predicateBody())) Some(visitExpression(ctx.predicateBody().expression())) else None
    PFPredicateDecl(id, params.flatten, body).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMpredicateDecl(ctx: MpredicateDeclContext): PMPredicateDecl = {
    val id = withWildcards{
      idnDef(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())
    }
    val receiver = visitReceiver(ctx.receiver())
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val body = visitExpression(ctx.predicateBody().expression())
    PMPredicateDecl(id, receiver, params.flatten, Some(body)).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitImplementationProofPredicateAlias(ctx: ImplementationProofPredicateAliasContext): PImplementationProofPredicateAlias = {
    val left = idnUse(ctx.IDENTIFIER()).at(ctx)
    val right = if (has(ctx.operandName())) {
      visitOperandName(ctx.operandName())
    } else {
      val id = idnUse(ctx.selection().IDENTIFIER()).at(ctx)
      if (has(ctx.selection().primaryExpr())) PDot(visitPrimaryExpr(ctx.selection().primaryExpr()), id).at(ctx)  else
        PDot(visitType_(ctx.selection().type_()), id).at(ctx)
    }
    PImplementationProofPredicateAlias(left, right).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitNonLocalReceiver(ctx: NonLocalReceiverContext): PParameter = {
    val name = visitTypeName(ctx.typeName())
    val typ = if (has(ctx.STAR())) PDeref(name).at(ctx) else name
    if (has(ctx.IDENTIFIER())) PNamedParameter(idnDef(ctx.IDENTIFIER()).at(ctx), typ).at(ctx)
    else PUnnamedParameter(typ).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodImplementationProof(ctx: MethodImplementationProofContext): PMethodImplementationProof = {
    val id = idnUse(ctx.IDENTIFIER()).at(ctx)
    val receiver = visitNonLocalReceiver(ctx.nonLocalReceiver())
    val (args, result) = visitSignature(ctx.signature())
    val isPure = has(ctx.PURE())
    val body = if (has(ctx.block())) Some((PBodyParameterInfo(Vector.empty).at(ctx), visitBlock(ctx.block()))) else None
    PMethodImplementationProof(id, receiver, args, result, isPure = isPure, body).at(ctx)
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
    PImplementationProof(subT, superT, alias, memberProofs).at(ctx)
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
        Vector(PExplicitGhostMember(visitMethodDecl(ctx.methodDecl())).at(ctx))
      } else if (ctx.functionDecl() != null) {
        Vector(PExplicitGhostMember(visitFunctionDecl(ctx.functionDecl())).at(ctx))
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
    PProgram(packageClause, importDecls.flatten, functionDecls ++ methodDecls ++ decls.flatten ++ ghostMembers.flatten).at(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitPackageClause(ctx: GobraParser.PackageClauseContext): PPackageClause = {
    PPackageClause(PPkgDef(ctx.packageName.getText)).at(ctx)
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
    PStringLit(str.substring(1,str.length-1)).at(ctx)
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
      PUnqualifiedImport(path).at(ctx)
    } else if (ctx.IDENTIFIER() != null) {
      PExplicitQualifiedImport(idnDefOrWildcard(ctx.IDENTIFIER()).at(ctx), path).at(ctx)
    } else {
      PImplicitQualifiedImport(path).at(ctx)
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

    PConstDecl(typ, right, left).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitConstDecl(ctx: GobraParser.ConstDeclContext): Vector[PConstDecl] = {
    (for (spec <- ctx.constSpec().asScala.toVector) yield visitConstSpec(spec)) match {
      // Make sure the first expression list is not empty
      case Vector(PConstDecl(_, Vector(), _), _*) => fail(ctx)
      case decls => decls
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeSpec(ctx: GobraParser.TypeSpecContext): PTypeDecl = {
    val left = withWildcards{
      idnDef(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())
    }
    val right = visitType_(ctx.type_())
    if (ctx.ASSIGN() != null) {
      PTypeAlias(right, left).at(ctx)
    } else {
      PTypeDef(right, left).at(ctx)
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
    PVarDecl(typ, right, vars, addressable).at(ctx)

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
    if (!has(ctx)) Vector.empty else
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

    PFunctionSpec(pres, preserves, posts, terms, isPure = isPure) match {
      // If we have empty specification, we can't get a position, for it.
      case PFunctionSpec(Vector(), Vector(), Vector(), Vector(), false) => PFunctionSpec(Vector.empty, Vector.empty, Vector.empty, Vector.empty).at(ctx.parent.asInstanceOf[ParserRuleContext])
      case spec => spec.at(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitBlockWithBodyParameterInfo(ctx: BlockWithBodyParameterInfoContext): (PBodyParameterInfo, PBlock) = {
    val shareable = if (has(ctx.SHARE())) (for (id <- ctx.identifierList().IDENTIFIER().asScala.toVector) yield idnUse(id).at(id)) else Vector.empty
    val paramInfo = PBodyParameterInfo(shareable)
    val body =   if (has(ctx.statementList())) PBlock(visitStatementList(ctx.statementList())).at(ctx) else PBlock(Vector.empty).at(ctx)
    (paramInfo, body)
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
      idnDef(ctx.IDENTIFIER()).at(ctx.IDENTIFIER())
    }
    val sig = visitSignature(ctx.signature())
    val body = if (ctx.blockWithBodyParameterInfo() == null || specOnly) None else Some(visitBlockWithBodyParameterInfo(ctx.blockWithBodyParameterInfo()))
    PFunctionDecl(id, sig._1, sig._2, spec, body).at(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitBlock(ctx: GobraParser.BlockContext): PBlock =  {
    if (ctx.statementList() != null) PBlock(visitStatementList(ctx.statementList())).at(ctx) else PBlock(Vector.empty).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitIncDecStmt(ctx: IncDecStmtContext): PAssignmentWithOp = {
    val exp = visitExpression(ctx.expression()) match {
      case assignee : PAssignee => assignee
      case _ => fail(ctx.expression(), "Increment/Decrement-statements must have an assignee as operand.")
    }
    if (has(ctx.PLUS_PLUS()))
      PAssignmentWithOp(PIntLit(1).at(ctx.PLUS_PLUS()), PAddOp().at(ctx.PLUS_PLUS()), exp).at(ctx) else
      PAssignmentWithOp(PIntLit(1).at(ctx.MINUS_MINUS()), PSubOp().at(ctx.MINUS_MINUS()), exp).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitSendStmt(ctx: SendStmtContext): PSendStmt = {
    val channel = visitExpression(ctx.channel)
    val msg = visitExpression(ctx.expression(1))
    PSendStmt(channel, msg).at(ctx)
  }

  override def visitSimpleStmt(ctx: GobraParser.SimpleStmtContext): PSimpleStmt = {
    if (ctx.shortVarDecl() != null){
      visitShortVarDecl(ctx.shortVarDecl())
    } else if (ctx.assignment() != null) {
      visitAssignment(ctx.assignment())
    } else if (ctx.expressionStmt() != null){
      PExpressionStmt(visitExpression(ctx.expressionStmt().expression())).at(ctx)
    } else if (has(ctx.incDecStmt())) {
      visitIncDecStmt(ctx.incDecStmt())
    } else if (has(ctx.sendStmt())) {
      visitSendStmt(ctx.sendStmt())
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
    PReturn(exprs).at(ctx)
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
        case invoke : PInvoke => PPredicateAccess(invoke, PFullPerm().at(invoke)).at(ctx)
        case PAccess(invoke: PInvoke, perm) => PPredicateAccess(invoke, perm).at(ctx)
        case _ => fail(ctx, "Expected invocation")
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
      PExplicitGhostStatement(visitStatement(ctx.statement())).at(ctx)
    } else if (ctx.ASSERT() != null) {
      PAssert(visitExpression(ctx.expression())).at(ctx)
    } else if (ctx.fold_stmt != null) {
      val predicateAccess = visitPredicateAccess(ctx.predicateAccess())
      ctx.fold_stmt.getType match {
        case GobraParser.FOLD => PFold(predicateAccess).at(ctx)
        case GobraParser.UNFOLD => PUnfold(predicateAccess).at(ctx)
      }
    }  else if (has(ctx.kind)) {
      val expr = visitExpression(ctx.expression())
      val kind = ctx.kind.getType match {
        case GobraParser.ASSERT => PAssert
        case GobraParser.ASSUME => PAssume
        case GobraParser.INHALE => PInhale
        case GobraParser.EXHALE => PExhale
      }
      kind(expr).at(ctx)
    } else fail(ctx)
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
    PIfClause(pre, expr, block).at(clause)
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
    PIfStmt(ifs,els).at(ctx)
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
      PForStmt(None, visitExpression(ctx.expression()), None, spec, block).at(ctx)
    } else if(has(ctx.forClause())){
      val pre = if (has(ctx.forClause().initStmt)) Some(visitSimpleStmt(ctx.forClause().initStmt)) else None
      val post = if (has(ctx.forClause().postStmt)) Some(visitSimpleStmt(ctx.forClause().postStmt)) else None
      val cond = if (has(ctx.forClause().expression())) visitExpression(ctx.forClause().expression()) else PBoolLit(true).at(ctx)
      PForStmt(pre, cond, post, spec, block).at(ctx)
    } else if (has(ctx.rangeClause())) {
      val expr = visitExpression(ctx.rangeClause().expression())
      val range = PRange(expr).at(ctx.rangeClause())
      if (has(ctx.rangeClause().DECLARE_ASSIGN())) {
        val idList = visitUnkIdentifierList(ctx.rangeClause().identifierList().IDENTIFIER().asScala.toVector)
        PShortForRange(range, idList, block).at(ctx)
      } else {
        val assignees = visitExpressionList(ctx.rangeClause().expressionList()) match {
          case v : Vector[PAssignee] => v
          case _ => fail(ctx)
        }
        PAssForRange(range, assignees, block).at(ctx)
      }
    } else {
      PForStmt(None, PBoolLit(true).at(ctx.FOR()), None, spec, block).at(ctx)
    }

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGoStmt(ctx: GoStmtContext): PGoStmt = {
    val expr = visitExpression(ctx.expression())
    PGoStmt(expr).at(ctx)
  }

  def visitLabelDef(node: TerminalNode) : PLabelDef = {
    PLabelDef(node.toString).at(node)
  }

  def visitLabelUse(node: TerminalNode) : PLabelUse = {
    PLabelUse(node.toString).at(node)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitLabeledStmt(ctx: LabeledStmtContext): PLabeledStmt = {
    val label = visitLabelDef(ctx.IDENTIFIER())
    val stmt = if(has(ctx.statement())) visitStatement(ctx.statement()) else PEmptyStmt().at(ctx)
    PLabeledStmt(label, stmt).at(ctx)
  }


  def visitExprDefaultClause(ctx: ExprCaseClauseContext): PExprSwitchDflt = {
    PExprSwitchDflt(PBlock(visitStatementList(ctx.statementList())).at(ctx.statementList())).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitExprCaseClause(ctx: ExprCaseClauseContext): PExprSwitchCase = {
    val body = PBlock(visitStatementList(ctx.statementList())).at(ctx)
    val left = visitExpressionList(ctx.exprSwitchCase().expressionList())
    PExprSwitchCase(left, body).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitExprSwitchStmt(ctx: ExprSwitchStmtContext): PExprSwitchStmt = {
    val pre = if (has(ctx.simpleStmt())) Some(visitSimpleStmt(ctx.simpleStmt())) else None
    val expr = if (has(ctx.expression())) visitExpression(ctx.expression()) else PBoolLit(true).at(ctx.SWITCH())
    val (dflt, cases) = ctx.exprCaseClause().asScala.toVector.partitionMap(clause =>
      if (has(clause.exprSwitchCase().DEFAULT())) Left(visitExprDefaultClause(clause).body)
      else Right(visitExprCaseClause(clause))
    )
    PExprSwitchStmt(pre, expr, cases, dflt).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeList(ctx: TypeListContext): Vector[PExpressionOrType] = {
    val types = if (has(ctx.type_())) for (typ <- ctx.type_().asScala.toVector) yield visitType_(typ) else Vector.empty
    if (has(ctx.NIL_LIT())) types.appended(PNilLit()) else types
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeCaseClause(ctx: TypeCaseClauseContext): PTypeSwitchCase = {
    val body = PBlock(visitStatementList(ctx.statementList())).at(ctx)
    val left = visitTypeList(ctx.typeSwitchCase().typeList())
    PTypeSwitchCase(left, body).at(ctx)
  }

  def visitTypeDefaultClause(ctx: TypeCaseClauseContext): PTypeSwitchDflt = {
    val body  = PBlock(visitStatementList(ctx.statementList())).at(ctx.statementList())
    PTypeSwitchDflt(body).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeSwitchStmt(ctx: TypeSwitchStmtContext): PTypeSwitchStmt = {
    val pre = if (has(ctx.simpleStmt())) Some(visitSimpleStmt(ctx.simpleStmt())) else None
    val binder = if (has(ctx.typeSwitchGuard().IDENTIFIER())) Some(idnDef(ctx.typeSwitchGuard().IDENTIFIER())) else None
    val expr = visitPrimaryExpr(ctx.typeSwitchGuard().primaryExpr())
    val (dflt, cases) = ctx.typeCaseClause().asScala.toVector.partitionMap(clause =>
      if (has(clause.typeSwitchCase().DEFAULT())) Left(visitTypeDefaultClause(clause).body)
      else Right(visitTypeCaseClause(clause))
    )
    PTypeSwitchStmt(pre, expr, binder, cases, dflt).at(ctx)

  }

  override def visitStatement(ctx: GobraParser.StatementContext): PStatement = {
    if (has(ctx.declaration())) {
      PSeq(visitDeclarationStmt(ctx.declaration())).at(ctx)
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
    } else if (has(ctx.goStmt())) {
      visitGoStmt(ctx.goStmt())
    } else if (has(ctx.labeledStmt())) {
      visitLabeledStmt(ctx.labeledStmt())
    } else if (has(ctx.gotoStmt())) {
      PGoto(visitLabelDef(ctx.gotoStmt().IDENTIFIER())).at(ctx)
    } else if (has(ctx.block())) {
      visitBlock(ctx.block())
    } else if (has(ctx.switchStmt())) {
      if (has(ctx.switchStmt().exprSwitchStmt()))
        visitExprSwitchStmt(ctx.switchStmt().exprSwitchStmt())
      else visitTypeSwitchStmt(ctx.switchStmt().typeSwitchStmt())
    } else fail(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitStatementList(ctx: GobraParser.StatementListContext): Vector[PStatement] = {
    if (ctx == null) return Vector.empty
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
  override def visitAssignment(ctx: GobraParser.AssignmentContext): PSimpleStmt = {
    allowWildcards = true
    val left = visitExpressionList(ctx.expressionList(0)) match {
      case v: Vector[PAssignee] => v
      case _ => fail(ctx, "Assignee List contains non-assignable expressions.")
    }
    allowWildcards = false
    val right = visitExpressionList(ctx.expressionList(1))
    if (has(ctx.assign_op().ass_op)) {
      val ass_op = ctx.assign_op().ass_op.getType match {
        case GobraParser.PLUS => PAddOp()
        case GobraParser.MINUS => PSubOp()
        case GobraParser.STAR =>  PMulOp()
        case GobraParser.DIV => PDivOp()
        case GobraParser.MOD => PModOp()
        case GobraParser.AMPERSAND => PBitAndOp()
        case GobraParser.OR => PBitOrOp()
        case GobraParser.CARET => PBitXorOp()
        case GobraParser.BIT_CLEAR => PBitClearOp()
        case GobraParser.LSHIFT => PShiftLeftOp()
        case GobraParser.RSHIFT => PShiftRightOp()
      }
      PAssignmentWithOp(right match {
        case Vector(r) => r
        case Vector(_, _*) => fail(ctx.expressionList(0), "Assignments with operators can only have one right-hand expression.")
      }, ass_op, left match {
        case Vector(l) => l
        case Vector(_, _*) => fail(ctx.expressionList(0), "Assignments with operators can only have one left-hand expression.")
      }).at(ctx)
    }
    else {
      PAssignment(right, left)
        .at(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodSpec(ctx: GobraParser.MethodSpecContext): PMethodSig = {
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    val id = idnDef(ctx.IDENTIFIER()).at(ctx)
    val args = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty).at(ctx)
    PMethodSig(id, args.flatten, result, spec, isGhost = false).at(ctx)
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
    PMPredicateSig(id, args.flatten).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitInterfaceType(ctx: GobraParser.InterfaceTypeContext): PInterfaceType = {
    val methodDecls = for (meth <- ctx.methodSpec().asScala.toVector) yield visitMethodSpec(meth).at(ctx)
    val embedded = for (typ <- ctx.typeName().asScala.toVector) yield
      PInterfaceName(PNamedOperand(idnUse(typ.IDENTIFIER()).at(typ)).at(typ)).at(typ)
    val predicateDecls = for (pred <- ctx.predicateSpec().asScala.toVector) yield visitPredicateSpec(pred)
    PInterfaceType(embedded, methodDecls, predicateDecls).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitEmbeddedField(ctx: EmbeddedFieldContext): PEmbeddedType = {
    if (ctx.STAR() != null) {
      PEmbeddedPointer(visitTypeName(ctx.typeName())).at(ctx)
    } else {
      PEmbeddedName(visitTypeName(ctx.typeName())).at(ctx)
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
      PEmbeddedDecl(et, PIdnDef(et.name).at(et)).at(ctx)
    } else {
      val ids = visitDefLikeIdentifierList(ctx.identifierList().IDENTIFIER().asScala.toVector)
      val t = visitType_(ctx.type_())
      PFieldDecls(ids map (id => PFieldDecl(id.asInstanceOf[PIdnDef], t.copy).at(id))).at(ctx)
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
    PStructType(decls).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFunctionType(ctx: FunctionTypeContext): PFunctionType = {
    val (args, result) = visitSignature(ctx.signature())
    PFunctionType(args, result).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPredTypeParams(ctx: PredTypeParamsContext): Vector[PType] = {
    for (typ <- ctx.type_().asScala.toVector) yield visitType_(typ)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPredType(ctx: PredTypeContext): PPredType = {
    val params = visitPredTypeParams(ctx.predTypeParams())
    PPredType(params).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitChannelType(ctx: ChannelTypeContext): PChannelType = {
    val elem = visitType_(ctx.elementType().type_())
    val channel = if (!(has(ctx.RECEIVE()))) {
      PBiChannelType
    } else if (ctx.CHAN().startPos < ctx.RECEIVE().startPos) {     // CHAN RECEIVE : chan <-
      PSendChannelType
    } else  { // RECEIVE CHAN : <- chan
      PRecvChannelType
    }
    channel(elem).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeLit(ctx: GobraParser.TypeLitContext): PTypeLit = {
    if (has(ctx.pointerType())) {
      PDeref(visitType_(ctx.pointerType().type_())).at(ctx)
    } else if(ctx.structType() != null) {
      visitStructType(ctx.structType())
    }else if(ctx.interfaceType() != null){
      visitInterfaceType(ctx.interfaceType())
    } else if (has(ctx.sliceType())) {
      visitSliceType(ctx.sliceType())
    } else  if (has(ctx.arrayType())) {
      visitArrayType(ctx.arrayType())
    } else if (has(ctx.functionType())) {
      visitFunctionType(ctx.functionType())
    } else if (has(ctx.mapType())) {
      visitMapType(ctx.mapType())
    } else if (has(ctx.predType())) {
      visitPredType(ctx.predType())
    } else if (has(ctx.channelType())) {
      visitChannelType(ctx.channelType())
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
    kind(typ).at(ctx)

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostSliceType(ctx: GhostSliceTypeContext): PGhostSliceType = {
    val typ = visitType_(ctx.elementType().type_())
    PGhostSliceType(typ).at(ctx)
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
    } else if (has(ctx.ghostSliceType())) {
      visitGhostSliceType(ctx.ghostSliceType())
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
        PDot(PNamedOperand(idnUse(base).at(base)).at(base), idnUse(id).at(id)).at(ctx.typeName())
      }
    } else if (ctx.ghostTypeLit() != null) {
        visitGhostTypeLit(ctx.ghostTypeLit())
    } else if (ctx.predefined != null) {
      val t = ctx.predefined.getType match {
        case GobraParser.BOOL => PBoolType()
        case GobraParser.STRING  => PStringType()
        case GobraParser.PERM  => PPermissionType()
        case GobraParser.RUNE  => PRune()
        case GobraParser.INT  => PIntType()
        case GobraParser.INT8  => PInt8Type()
        case GobraParser.INT16  => PInt16Type()
        case GobraParser.INT32  => PInt32Type()
        case GobraParser.INT64  => PInt64Type()
        case GobraParser.BYTE  =>  PByte()
        case GobraParser.UINT  => PUIntType()
        case GobraParser.UINT8  => PUInt8Type()
        case GobraParser.UINT16  => PUInt16Type()
        case GobraParser.UINT32  => PUInt32Type()
        case GobraParser.UINT64  => PUInt64Type()
        case GobraParser.UINTPTR  => PUIntPtr()
      }
      t.at(ctx)
    } else fail(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitQualifiedIdent(ctx: QualifiedIdentContext): PDot = {
    PDot(PNamedOperand(idnUse(ctx.IDENTIFIER(0)).at(ctx)).at(ctx.IDENTIFIER(0)), idnUse(ctx.IDENTIFIER(1)).at(ctx.IDENTIFIER(1))).at(ctx)
  }

  def visitTypeIdentifier(typ: TerminalNode): Either[PPredeclaredType, PNamedOperand] = {
    typ.getSymbol.getText match {
      case "perm" => Left(PPermissionType().at(typ))
      case "int" => Left(PIntType().at(typ))
      case "int16" => Left(PInt16Type().at(typ))
      case "int32" => Left(PInt32Type().at(typ))
      case "int64" => Left(PInt64Type().at(typ))
      case "uint" => Left(PUIntType().at(typ))
      case "byte" => Left(PByte().at(typ))
      case "uint8" => Left(PUInt8Type().at(typ))
      case "uint16" => Left(PUInt16Type().at(typ))
      case "uint32" => Left(PUInt32Type().at(typ))
      case "uint64" => Left(PUInt64Type().at(typ))
      case "uintptr" => Left(PUIntPtr().at(typ))
      case "bool" => Left(PBoolType().at(typ))
      case "string" => Left(PStringType().at(typ))
      case "rune" => Left(PRune().at(typ))
      case _ => Right(PNamedOperand(idnUse(typ).at(typ)).at(typ))
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
      case Left(value) => fail(ctx, ctx.IDENTIFIER().getText + value.formatted)
      case Right(value) => value
    }
    else if (ctx.qualifiedIdent() != null) {
      visitQualifiedIdent(ctx.qualifiedIdent())
    } else fail(ctx, "Predeclared Type where not applicable")
  }

  override def visitTypeName(ctx: GobraParser.TypeNameContext): PNamedType = {
    if (ctx.IDENTIFIER() != null)
      visitTypeIdentifier(ctx.IDENTIFIER()).merge
    else fail(ctx, "in visitTypeName")
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
      PResult(results.flatten).at(ctx)
    } else {
      PResult(Vector(PUnnamedParameter(visitType_(ctx.type_())).at(ctx))).at(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitParameterDecl(ctx: GobraParser.ParameterDeclContext): Vector[PParameter] = {
    val typ = if (has(ctx.ELLIPSIS())) PVariadicType(visitType_(ctx.type_())).at(ctx) else visitType_(ctx.type_())
    val params = if(ctx.identifierList() != null) {
      for (id <- ctx.identifierList().IDENTIFIER().asScala.toVector) yield PNamedParameter(idnDef(id).at(ctx), typ.copy).at(id)
    } else {
      Vector[PActualParameter](PUnnamedParameter(typ).at(ctx.type_()))
    }

    if (ctx.GHOST() != null) {
      params.map(PExplicitGhostParameter(_).at(ctx))
    } else params
  }


  private def fail(cause : NamedPositionable, msg : String = "") = {
    throw TranslationFailure(cause, msg)
  }

  private def warn(cause: NamedPositionable, msg : String): Unit = {
    warnings.append(TranslationWarning(cause, msg))
  }

  implicit class PositionableParserRuleContext[P <: ParserRuleContext](ctx: P) extends NamedPositionable {
    val startPos : Position = Position(ctx.start.getLine, ctx.start.getCharPositionInLine+1, source)
    val endPos : Position = Position(ctx.stop.getLine, ctx.stop.getCharPositionInLine+ctx.stop.getText.length+1, source)
    val name : String = GobraParser.ruleNames.array(ctx.getRuleIndex)
    val text : String = ctx.getText
  }

  implicit class PositionableTerminalNode[T <: TerminalNode](term: T) extends NamedPositionable {
    private val tok = term.getSymbol
    val startPos : Position = Position(tok.getLine, tok.getCharPositionInLine+1, source)
    val endPos : Position = Position(tok.getLine, tok.getCharPositionInLine+term.getText.length+1, source)
    val name : String = GobraParser.VOCABULARY.getDisplayName(term.getSymbol.getType)
    val text : String = term.getText
  }

  implicit class PositionableToken[T <: Token](tok: T) extends NamedPositionable {
    val startPos : Position = Position(tok.getLine, tok.getCharPositionInLine+1, source)
    val endPos : Position = Position(tok.getLine, tok.getCharPositionInLine+tok.getText.length+1, source)
    val name : String = GobraParser.VOCABULARY.getDisplayName(tok.getType)
    val text : String = tok.getText
  }

  implicit class PositionedPAstNode[N <: PNode](node: N) {

    def at(p: Positionable): N = {
      pom.positions.setStart(node, p.startPos)
      pom.positions.setFinish(node, p.endPos)
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

  def has(a : AnyRef): Boolean = {
    a != null
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

trait Named {
  val name : String
  val text : String
}

// Any object that can be assigned a start and end in a source is positionable
trait Positionable {
  val startPos : Position
  val endPos : Position
  val startFinish : (Position, Position) = (startPos, endPos)
}

// This trait is used for error messages, here we need both a position and a name.
sealed trait NamedPositionable extends Named with Positionable


class TranslationException(cause : NamedPositionable, msg : String)  extends Exception(msg)

case class TranslationFailure(cause: NamedPositionable, msg : String = "") extends TranslationException(cause, s"Translation of ${cause.name} ${cause.text} failed${if (msg.nonEmpty) ": " + msg else "."}")
case class TranslationWarning(cause: NamedPositionable, msg : String = "") extends TranslationException(cause, s"Warning in ${cause.name} ${cause.text}${if (msg.nonEmpty) ": " + msg else "."}")
case class UnsupportedOperatorException(cause: NamedPositionable, typ : String, msg : String = "") extends TranslationException(cause, s"Unsupported $typ operation: ${cause.text}.")

