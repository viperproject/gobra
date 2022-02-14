// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.


package viper.gobra.frontend
import org.antlr.v4.runtime.{ParserRuleContext, Token}
import org.antlr.v4.runtime.tree.{ParseTree, RuleNode, TerminalNode}
import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter}
import org.bitbucket.inkytonik.kiama.util.{Position, Positions, Source}
import viper.gobra.ast.frontend.{PAssignee, PCompositeKey, PExpression, PMethodReceiveName, _}
import viper.gobra.util.{Binary, Hexadecimal, Octal}
import viper.gobra.frontend.GobraParser._
import viper.gobra.frontend.TranslationHelpers._
import viper.gobra.util.Violation.violation

import scala.StringContext.InvalidEscapeException
import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class ParseTreeTranslator(pom: PositionManager, source: Source, specOnly : Boolean = false) extends GobraParserBaseVisitor[AnyRef] {

  val warnings : ListBuffer[TranslationWarning] = ListBuffer.empty

  lazy val rewriter = new PRewriter(pom.positions)

  def translate[Rule <: ParserRuleContext, Node](tree: Rule):  Node = {
    visit(tree) match {
      case n : Node @unchecked => n
    }
  }

  //region Partial parsing
  /**
    * This region contains functions to support parsing only one specific rule, and failing
    * if the entire input cannot be matched by this rule.
    */

  /**
    * Visit the rule
    * typeOnly: type_ EOF;
    */
  override def visitTypeOnly(ctx: TypeOnlyContext): PType = visitNode[PType](ctx.type_())

  /**
    * Visit the rule
    * stmtOnly: statement EOF;
    */
  override def visitStmtOnly(ctx: StmtOnlyContext): PStatement = visitNode[PStatement](ctx.statement())

  /**
    * Visit the rule
    * exprOnly: expression EOF;
    */
  override def visitExprOnly(ctx: ExprOnlyContext): PExpression = visitNode[PExpression](ctx.expression())
  //endregion

  //region Lexical Elements
  /**
    * This region contains visitors for the basic lexical elements, such as
    * identifiers and basic literals.
    */

  //region Identifiers

  /**
    * This class extracts PIdnNodes from IDENTIFIER tokens
    *
    * @param constructor The constructor used to create the PIdnNode
    * @param blankIdentifier A function to handle blank identifiers. If blank identifiers are not allowed, it should
    *                        always return None, otherwise it should return the appropriate PIdnNode.
    * @tparam N the type of the PIdnNode to return
    */
  case class PIdnNodeEx[N <: PNode](constructor : String => N, blankIdentifier : TerminalNode => Option[N]) {
    def unapply(arg: TerminalNode) : Option[N] = {
      arg.getText match {
        case "_" => blankIdentifier(arg)
        case a : String => Some(constructor(a).at(arg))
        case _ => None
      }
    }
    /**
      * Directly extract a PIdnNode from an IDENTIFIER token. Fails if the token doesn't match.
      * @param arg The IDENTIFIER to extract from
      * @return A vector of PIdnNodes
      */
    def get(arg : TerminalNode) : N = unapply(arg) match {
      case Some(value) => value
      case None => fail(arg)
    }
  }

  /**
    * This class extracts Vectors of PIdnNode from Lists of IDENTIFIER tokens
    *
    * @param extractor The extractor used for the individual identifiers
    * @tparam N The type of the PIdnNode to return
    */
  case class PIdnNodeListEx[N <: PNode](extractor : PIdnNodeEx[N]) {
    def unapply(arg : Iterable[TerminalNode]) : Option[Vector[N]] = {
      Some(arg.map(extractor.get).toVector)
    }

    /**
      * Directly extract a Vector of PIdnNode from a List of IDENTIFIER tokens. Fails if one of the tokens
      * doen't match.
      * @param arg An iterable of TerminalNodes
      * @return A vector of PIdnNodes
      */
    def get(arg: Iterable[TerminalNode]) : Vector[N] = unapply(arg) match {
      case Some(value) => value
      case None => fail(arg.head)
    }
  }

  // Extractors for all the possible types of PIdnNodes
  private val idnDef = PIdnNodeEx(PIdnDef, _ => None)
  private val idnDefList = PIdnNodeListEx(idnDef)
  private val idnDefLike = PIdnNodeEx(PIdnDef, term => Some(PWildcard().at(term)))
  private val idnDefLikeList = PIdnNodeListEx(idnDefLike)
  private val idnUnk = PIdnNodeEx(PIdnUnk, _ => None)
  private val idnUnkList = PIdnNodeListEx(idnUnk)
  private val idnUnkLike = PIdnNodeEx(PIdnUnk, term => Some(PWildcard().at(term)))
  private val idnUnkLikeList = PIdnNodeListEx(idnUnkLike)
  private val idnUse = PIdnNodeEx(PIdnUse, _ => None)
  private val idnUseList = PIdnNodeListEx(idnUse)
  private val idnUseLike = PIdnNodeEx(PIdnUse, term => Some(PWildcard().at(term)))
  @unused private val idnUseLikeList  = PIdnNodeListEx(idnUseLike)

  // These extractors may only be used where Go allows the blank identifier, but Gobra doesnt
  // They generate a unique PIdnNode whose name starts with "_" to not overlap any other identifiers
  private val goIdnDef = PIdnNodeEx(PIdnDef, term => Some(uniqueWildcard(PIdnDef, term).at(term)))
  private val goIdnDefList = PIdnNodeListEx(goIdnDef)
  private val goIdnUnk = PIdnNodeEx(PIdnUnk, term => Some(uniqueWildcard(PIdnUnk, term).at(term)))
  private val goIdnUnkList = PIdnNodeListEx(goIdnUnk)

  def uniqueWildcard[N](constructor : String => N, term : TerminalNode) : N = constructor("_"+term.getSymbol.getTokenIndex)
  //endregion

  //region Basic Literals

  /**
    * Visits integer literals. Supports octal, hex, binary, decimal and single char or little-u
    * runes. (For example 'a', '本', '\t', '\u12e4')
    * @param ctx the parse tree
    *     */
  override def visitInteger(ctx: GobraParser.IntegerContext): PIntLit = {
    val octal = raw"0[oO]?([0-7_]+)".r
    val hex = "0[xX]([0-9A-Fa-f_]+)".r
    val bin = "0[bB]([01_]+)".r
    val dec = "([0-9_]+)".r
    val runeChar = "'(.*)'".r
    val bigU = """\\U([0-9a-fA-F]{8})""".r
    val hex_byte = """\\x([0-9a-fA-F]{2})""".r
    val octal_byte = """\\([0-7]{3})""".r
    visitChildren(ctx).asInstanceOf[String] match {
      case octal(digits) => PIntLit(BigInt(digits, 8), Octal).at(ctx)
      case hex(digits) => PIntLit(BigInt(digits, 16), Hexadecimal).at(ctx)
      case bin(digits) => PIntLit(BigInt(digits, 2), Binary).at(ctx)
      case dec(digits)  => PIntLit(BigInt(digits)).at(ctx)
      case runeChar(rune) => rune match {
        case bigU(digits) => PIntLit(BigInt(digits, 16), Hexadecimal).at(ctx)
        case hex_byte(digits) => PIntLit(BigInt(digits, 16), Hexadecimal).at(ctx)
        case octal_byte(digits) => PIntLit(BigInt(digits, 8), Octal).at(ctx)
        // Single rune char
        case _ => try {
          StringContext.processEscapes(rune).codePoints().toArray match {
            case Array(i : Int) => PIntLit(BigInt(i)).at(ctx)
          }
        } catch {
          case e : InvalidEscapeException => violation(s"Rune literal should not have been parsed: ${e}")
        }
      }
      case _  => fail(ctx, "This literal is not supported yet")
    }
  }

  def visitFloat(node: TerminalNode): PBasicLiteral = {
    val hex = "(0[xX].*)".r
    node.getText.replace("_","") match {
      // TODO : This only supports hex floats in the Double range (53 + 12 bits), Go requests up to 256 + 16 bits
      case hex(float) => java.lang.Double.parseDouble(float) match {
        case Double.NegativeInfinity | Double.PositiveInfinity => fail(node, "Gobra only supports hex floats in the float64 range")
        case float => PFloatLit(BigDecimal(float)).at(node)
      }
      case float => PFloatLit(BigDecimal(float)).at(node)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitString_(ctx: GobraParser.String_Context): PStringLit = {
    // Remove the delimiters
    val string = ".((?:.|\n)*).".r
    visitChildren(ctx).asInstanceOf[String] match {
      case string(str) => PStringLit(str).at(ctx)
    }
  }
  //endregion

  //endregion

  //region Types

  override def visitTypeName(ctx: GobraParser.TypeNameContext): PTypeName = {
    visitChildren(ctx) match {
      case idnUse(id) => visitTypeIdentifier(id) // replace with `PNamedOperand(id)` when arrays of custom types are supported
      case qualified : PDot => qualified
      case _ => unexpected(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitQualifiedIdent(ctx: QualifiedIdentContext): PDot = {
    visitChildren(ctx) match {
      case Vector(idnUse(base), ".", idnUse(id)) => PDot(PNamedOperand(base).at(base), id).at(ctx)
      case _ => unexpected(ctx)
    }
  }

  def visitTypeIdentifier(typ: PIdnUse): PUnqualifiedTypeName = {
    typ.name match {
      case "perm" => PPermissionType().at(typ)
      case "int" => PIntType().at(typ)
      case "int16" => PInt16Type().at(typ)
      case "int32" => PInt32Type().at(typ)
      case "int64" => PInt64Type().at(typ)
      case "uint" => PUIntType().at(typ)
      case "byte" => PByte().at(typ)
      case "uint8" => PUInt8Type().at(typ)
      case "uint16" => PUInt16Type().at(typ)
      case "uint32" => PUInt32Type().at(typ)
      case "uint64" => PUInt64Type().at(typ)
      case "uintptr" => PUIntPtr().at(typ)
      case "float32" => PFloat32().at(typ)
      case "float64" => PFloat64().at(typ)
      case "bool" => PBoolType().at(typ)
      case "string" => PStringType().at(typ)
      case "rune" => PRune().at(typ)
      case _ => PNamedOperand(typ).at(typ)
    }
  }

  /**
    * Visits the rule
    * arrayType: L_BRACKET arrayLength R_BRACKET elementType;
    */
  override def visitArrayType(ctx: ArrayTypeContext): PArrayType = {
    visitChildren(ctx) match {
      case Vector("[", len : PExpression, "]", elem : PType) => PArrayType(len, elem)
      case _ => unexpected(ctx)
    }
  }


  /**
    * Visits the rule
    * implicitArray: L_BRACKET ELLIPSIS R_BRACKET elementType;
    */
  override def visitImplicitArray(ctx: ImplicitArrayContext): AnyRef = visitChildren(ctx) match {
    case Vector("[", "...", "]", elem: PType) => PImplicitSizeArrayType(elem)
  }

  /**
    * Visits the rule
    * sliceType: L_BRACKET R_BRACKET elementType;
    */
  override def visitSliceType(ctx: SliceTypeContext): PSliceType = visitChildren(ctx) match {
      case Vector("[", "]", typ : PType)  => PSliceType(typ)
      case _ => unexpected(ctx)
    }

  //region Struct Types
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitStructType(ctx: StructTypeContext): PStructType = {
    val decls = for (decl <- ctx.fieldDecl().asScala.toVector) yield visitNode[PStructClause](decl)
    PStructType(decls).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFieldDecl(ctx: FieldDeclContext): PStructClause = {
    if (ctx.embeddedField() != null) {
      val et = visitNode[PEmbeddedType](ctx.embeddedField())
      PEmbeddedDecl(et, PIdnDef(et.name).at(et))
    } else {
      val goIdnDefList(ids) = visitIdentifierList(ctx.identifierList())
      val t = visitNode[PType](ctx.type_())
      PFieldDecls(ids map (id => PFieldDecl(id, t.copy).at(id)))
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitEmbeddedField(ctx: EmbeddedFieldContext): PEmbeddedType = {
    visitChildren(ctx) match {
      case name : PUnqualifiedTypeName => PEmbeddedName(name)
      case Vector("*", name : PUnqualifiedTypeName) => PEmbeddedPointer(name)
      case  _ : PDot | Vector("*", _ : PDot) => fail(ctx, "Imported types are not yet supported as embedded interface names")
    }
  }
  //endregion

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPointerType(ctx: PointerTypeContext): PDeref = visitChildren(ctx) match {
    case Vector("*", typ : PType) => PDeref(typ)
    case _ => unexpected(ctx)
  }

  //region Function Types
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFunctionType(ctx: FunctionTypeContext): PFunctionType = {
    visitChildren(ctx) match {
      case Vector("func", (args: Vector[PParameter] @unchecked, result : PResult)) => PFunctionType(args, result)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitSignature(ctx: GobraParser.SignatureContext): Signature = {
    val params : Vector[Vector[PParameter]] = (for (param <- ctx.parameters().parameterDecl().asScala.view)
      yield visitParameterDecl(param)
      ).toVector
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty)
    (params.flatten, result)
  }

  type Signature = (Vector[PParameter], PResult)

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  // TODO : Refactor easy
  override def visitResult(ctx: GobraParser.ResultContext): PResult = {
    if (ctx.parameters() != null) {
      val results = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
      PResult(results.flatten).at(ctx)
    } else {
      PResult(Vector(PUnnamedParameter(visitNode[PType](ctx.type_())).at(ctx))).at(ctx)
    }
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitParameterDecl(ctx: GobraParser.ParameterDeclContext): Vector[PParameter] = {
    val typ = if (has(ctx.ELLIPSIS())) PVariadicType(visitNode[PType](ctx.type_())).at(ctx) else visitNode[PType](ctx.type_())
    val params = if(ctx.identifierList() != null) {
      // "_" should be supported here to allow unused parameters for interfaces etc.
      goIdnDefList.get(visitIdentifierList(ctx.identifierList())).map(id => PNamedParameter(id, typ.copy).at(id))
    } else {
      Vector[PActualParameter](PUnnamedParameter(typ).at(ctx.type_()))
    }

    if (ctx.GHOST() != null) {
      params.map(PExplicitGhostParameter(_).at(ctx))
    } else params
  }
  //endregion

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitInterfaceType(ctx: GobraParser.InterfaceTypeContext): PInterfaceType = {
    val methodDecls = visitListNode[PMethodSig](ctx.methodSpec())
    val embedded = visitListNode[PTypeName](ctx.typeName()).map {
      case tn: PUnqualifiedTypeName => PInterfaceName(tn)
      case _: PDot => fail(ctx, "Imported types are not yet supported as embedded fields.")
      case _ => fail(ctx, s"Interface embeds predeclared type.")
    }
    val predicateDecls = visitListNode[PMPredicateSig](ctx.predicateSpec())
    PInterfaceType(embedded, methodDecls, predicateDecls).at(ctx)
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodSpec(ctx: GobraParser.MethodSpecContext): PMethodSig = {
    val ghost = has(ctx.GHOST())
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty).at(ctx)
    // The name of each explicitly specified method must be unique and not blank.
    val id = idnDef.get(ctx.IDENTIFIER())
    val args = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty).at(ctx)
    PMethodSig(id, args.flatten, result, spec, isGhost = ghost).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitPredicateSpec(ctx: PredicateSpecContext): PMPredicateSig = {
    val id = idnDef.get(ctx.IDENTIFIER())
    val args = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    PMPredicateSig(id, args.flatten).at(ctx)
  }


  /**
    * Visits the rule
    * mapType: MAP L_BRACKET type_ R_BRACKET elementType;
    */
  override def visitMapType(ctx: MapTypeContext): PMapType = {
    visitChildren(ctx) match {
      case Vector("map", "[", key : PType, "]", elem : PType) => PMapType(key, elem).at(ctx)
    }
  }

  /**
    * Visits channel types.
    * @param ctx the channel context
    *     */
  override def visitChannelType(ctx: ChannelTypeContext): PChannelType = {
    val res = visitChildren(ctx) match {
      case Vector("chan", typ : PType) => PBiChannelType(typ)
      case Vector("chan", "<-", typ : PType) => PSendChannelType(typ)
      case Vector("<-", "chan", typ : PType) => PRecvChannelType(typ)
    }
    res.at(ctx)
  }

  //region Ghost types
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitSqType(ctx: SqTypeContext): PGhostLiteralType = {
    visitChildren(ctx) match {
      case Vector(kind : String, "[", typ : PType, "]") => (kind match {
        case "seq" => PSequenceType(typ)
        case "set" => PSetType(typ)
        case "mset" => PMultisetType(typ)
        case "option" => POptionType(typ)
      }).at(ctx)
      case Vector("dict", "[", keys : PType, "]", values : PType) => PMathematicalMapType(keys, values).at(ctx)
    }
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostSliceType(ctx: GhostSliceTypeContext): PGhostSliceType = {
    val typ = visitNode[PType](ctx.elementType().type_())
    PGhostSliceType(typ).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitDomainType(ctx: DomainTypeContext): PDomainType = {
    val (funcs, axioms) = ctx.domainClause().asScala.toVector.partitionMap(visitDomainClause)
    PDomainType(funcs, axioms).at(ctx)
  }

  override def visitDomainClause(ctx: DomainClauseContext): Either[PDomainFunction, PDomainAxiom] = {
    visitChildren(ctx) match {
      case Vector("func", idnDef(id), (params : Vector[PParameter] @unchecked, result : PResult)) => Left(PDomainFunction(id, params, result).at(ctx))
      case Vector("axiom", "{", expr : PExpression, _, "}") => Right(PDomainAxiom(expr).at(ctx))
      case _ => fail(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPredType(ctx: PredTypeContext): PPredType = visitChildren(ctx) match {
    case Vector("pred", params : Vector[PType] @unchecked) => PPredType(params)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPredTypeParams(ctx: PredTypeParamsContext): Vector[PType] = {
    for (typ <- ctx.type_().asScala.toVector) yield visitNode[PType](typ)
  }

  //endregion


  //endregion

  //region Blocks
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
  override def visitBlockWithBodyParameterInfo(ctx: BlockWithBodyParameterInfoContext): (PBodyParameterInfo, PBlock) = {
    val shareable = if (has(ctx.SHARE())) idnUseList.get(visitIdentifierList(ctx.identifierList())) else Vector.empty
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
  override def visitStatementList(ctx: GobraParser.StatementListContext): Vector[PStatement] = {
    if (ctx == null) return Vector.empty
    for (stmt <- ctx.statement().asScala.toVector) yield visitStatement(stmt)
  }
  //endregion

  //region Declarations
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitConstDecl(ctx: GobraParser.ConstDeclContext): Vector[PConstDecl] = {
    visitListNode[PConstDecl](ctx.constSpec()) match {
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
  override def visitConstSpec(ctx: GobraParser.ConstSpecContext): PConstDecl = {
    val typ = if (ctx.type_() != null) Some(visitNode[PType](ctx.type_())) else None

    val idnDefLikeList(left) = visitIdentifierList(ctx.identifierList())
    val right = visitExpressionList(ctx.expressionList())

    PConstDecl(typ, right, left).at(ctx)
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeDecl(ctx: GobraParser.TypeDeclContext): Vector[PTypeDecl] = {
    visitListNode[PTypeDecl](ctx.typeSpec())
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeSpec(ctx: GobraParser.TypeSpecContext): PTypeDecl = {
    val left = goIdnDef.get(ctx.IDENTIFIER())
    val right = visitNode[PType](ctx.type_())
    if (ctx.ASSIGN() != null) {
      // <identifier> = <type> -> This is a type alias
      PTypeAlias(right, left).at(ctx)
    } else {
      // <identifier <type> -> This is a type definition
      PTypeDef(right, left).at(ctx)
    }
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitVarDecl(ctx: GobraParser.VarDeclContext): Vector[PVarDecl] = {
    visitListNode[PVarDecl](ctx.varSpec())
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitVarSpec(ctx: GobraParser.VarSpecContext): PVarDecl = {
    val (idnDefLikeList(vars), addressable) = visitMaybeAddressableIdentifierList(ctx.maybeAddressableIdentifierList())
    val typ = if(has(ctx.type_())) Some(visitNode[PType](ctx.type_())) else None
    val right = if (has(ctx.expressionList())) visitExpressionList(ctx.expressionList()) else Vector.empty
    PVarDecl(typ, right, vars, addressable).at(ctx)
  }

  override def visitShortVarDecl(ctx: GobraParser.ShortVarDeclContext): PShortVarDecl = {
    val (idnUnkLikeList(vars), addressable) = visitMaybeAddressableIdentifierList(ctx.maybeAddressableIdentifierList())
    val right = visitExpressionList(ctx.expressionList())
    PShortVarDecl(right, vars, addressable).at(ctx)
  }


  /**
    * Visits an identifier list and returns a vector of TerminalNodes that can be matched by the
    * appropriate PIdnExtractor.
    * @param ctx the parse tree
    *     */
  override def visitIdentifierList(ctx: IdentifierListContext): Iterable[TerminalNode] = {
    ctx.IDENTIFIER().asScala.view
  }

  /**
    * Visits a list of identifiers that may have the addressability modifier `@`
    */
  override def visitMaybeAddressableIdentifierList(ctx: MaybeAddressableIdentifierListContext): (Vector[TerminalNode], Vector[Boolean]) = {
    ctx.maybeAddressableIdentifier().asScala.toVector
      .map(ctx => (ctx.IDENTIFIER(), has(ctx.ADDR_MOD())))
      .unzip
  }

  /**
    * Visits a single identifier that may have the addressability modifier `@`
    */
  override def visitMaybeAddressableIdentifier(ctx: MaybeAddressableIdentifierContext): (TerminalNode, Boolean) = {
    (ctx.IDENTIFIER(), has(ctx.ADDR_MOD()))
  }



  // TODO : Move Blank identifier handling into the Type Checker
  def visitAssignee(ctx: GobraParser.ExpressionContext): PAssignee = {
    if (has(ctx.primaryExpr())) {
      visitPrimaryExpr(ctx.primaryExpr()) match {
        case a : PAssignee => a
        case _ =>  fail(ctx, "not an assignee")
      }
    } else visitNode[PExpression](ctx) match {
      case a : PAssignee => a
      case _ => fail(ctx, "not an assignee")
    }
  }

  def visitAssigneeList(ctx : GobraParser.ExpressionListContext) : Vector[PAssignee] = {
    if (!has(ctx)) Vector.empty else
      (for (expr <- ctx.expression().asScala.view) yield visitAssignee(expr)).toVector.at(ctx)
  }


  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitExpressionList(ctx: GobraParser.ExpressionListContext): Vector[PExpression] = {
    if (!has(ctx)) Vector.empty else
      (for (expr <- ctx.expression().asScala.toVector) yield visitExpression(expr)).at(ctx)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitFunctionDecl(ctx: GobraParser.FunctionDeclContext): PFunctionDecl = {
    // TODO : Make this more readable
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    // Go allows the blank identifier here, but PFunctionDecl doesn't.
    val id = goIdnDef.get(ctx.IDENTIFIER())
    val sig = visitNode[Signature](ctx.signature())
    val body = if (ctx.blockWithBodyParameterInfo() == null || specOnly || spec.isTrusted) None else Some(visitBlockWithBodyParameterInfo(ctx.blockWithBodyParameterInfo()))
    PFunctionDecl(id, sig._1, sig._2, spec, body).at(ctx)
  }

  /**
    * Visits Gobra specifications
    *
    * */
  override def visitSpecification(ctx: GobraParser.SpecificationContext): PFunctionSpec = {
    // Group the specifications by keyword
    val groups = ctx.specStatement().asScala.view.groupBy(_.kind.getType)
    // Get the respective groups
    val pres = groups.getOrElse(GobraParser.PRE, Seq.empty).toVector.map(s => visitExpression(s.assertion().expression()))
    val preserves = groups.getOrElse(GobraParser.PRESERVES, Vector.empty).toVector.map(s => visitExpression(s.assertion().expression()))
    val posts = groups.getOrElse(GobraParser.POST, Vector.empty).toVector.map(s => visitExpression(s.assertion().expression()))
    val terms = groups.getOrElse(GobraParser.DEC, Vector.empty).toVector.map(s => visitTerminationMeasure(s.terminationMeasure()))
    val isPure = has(ctx.PURE()) && !ctx.PURE().isEmpty
    val isTrusted = has(ctx.TRUSTED()) && !ctx.TRUSTED().isEmpty

    PFunctionSpec(pres, preserves, posts, terms, isPure = isPure, isTrusted = isTrusted) match {
      // If we have empty specification, we can't get a position, for it.
      case PFunctionSpec(Vector(), Vector(), Vector(), Vector(), false, false) => PFunctionSpec(Vector.empty, Vector.empty, Vector.empty, Vector.empty).at(ctx.parent.asInstanceOf[ParserRuleContext])
      case spec => spec.at(ctx)
    }
  }

  /**
    * Visits a method declaration.
    * */
  override def visitMethodDecl(ctx: GobraParser.MethodDeclContext): PMethodDecl = {
    val spec = if (ctx.specification() != null) visitSpecification(ctx.specification()) else PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty)
    val receiver = visitReceiver(ctx.receiver())
    // Go allows _ here, but PMethodDecl doesn't
    val id = goIdnDef.get(ctx.IDENTIFIER())
    val sig = visitNode[Signature](ctx.signature())
    val body = if (ctx.blockWithBodyParameterInfo() == null || specOnly || spec.isTrusted) None else Some(visitBlockWithBodyParameterInfo(ctx.blockWithBodyParameterInfo()))
    PMethodDecl(id, receiver,sig._1, sig._2, spec, body).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitReceiver(ctx: ReceiverContext): PReceiver = {
    val recvType = visitNode[PType](ctx.type_()) match {
      case t : PNamedOperand => PMethodReceiveName(t).at(t)
      case PDeref(t : PNamedOperand) => PMethodReceivePointer(t).at(t)
      case f => fail(ctx.type_(), s"Excpected declared type or pointer to declared type but got: $f.")
    }

    if (has(ctx.maybeAddressableIdentifier())) {
      val (goIdnDef(id), addr) = visitMaybeAddressableIdentifier(ctx.maybeAddressableIdentifier())
      PNamedReceiver(id, recvType, addr).at(ctx)
    } else PUnnamedReceiver(recvType).at(ctx)
  }

  //region Ghost members
  /**
    * Visits the rule
    * explicitGhostMember: GHOST (methodDecl | functionDecl | declaration);
    * */
  override def visitExplicitGhostMember(ctx: ExplicitGhostMemberContext): Vector[PExplicitGhostMember] = super.visitExplicitGhostMember(ctx) match {
    case Vector("ghost", decl : PGhostifiableMember) => Vector(PExplicitGhostMember(decl).at(ctx))
    case Vector("ghost", decl : Vector[PGhostifiableMember] @unchecked) => decl.map(PExplicitGhostMember(_).at(ctx))
  }


  //region Implementation proofs
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitImplementationProof(ctx: ImplementationProofContext): Vector[PImplementationProof] = {
    val subT : PType = visitNode(ctx.type_(0))
    val superT : PType = visitNode[PType](ctx.type_(1))
    val alias = visitListNode[PImplementationProofPredicateAlias](ctx.implementationProofPredicateAlias())
    val memberProofs = visitListNode[PMethodImplementationProof](ctx.methodImplementationProof())
    Vector(PImplementationProof(subT, superT, alias, memberProofs).at(ctx))
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMethodImplementationProof(ctx: MethodImplementationProofContext): PMethodImplementationProof = {
    val id = idnUse.get(ctx.IDENTIFIER())
    val receiver : PParameter = visitNode(ctx.nonLocalReceiver())
    val (args, result) = visitNode[Signature](ctx.signature())
    val isPure = has(ctx.PURE())
    val body = if (has(ctx.block())) Some((PBodyParameterInfo(Vector.empty).at(ctx), visitBlock(ctx.block()))) else None
    body match {
      case Some((a, b)) => pom.positions.dupRangePos(a, b, body)
      case _ =>
    }
    PMethodImplementationProof(id, receiver, args, result, isPure = isPure, body).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitNonLocalReceiver(ctx: NonLocalReceiverContext): PParameter = {
    visitChildren(ctx) match {
      case Vector("(", idnDef(name), "*", typ : PTypeName, ")") => PNamedParameter(name, PDeref(typ).at(typ)).at(ctx)
      case Vector("(", idnDef(name), typ : PTypeName, ")") => PNamedParameter(name, typ).at(ctx)
      case Vector("(", "*", typ : PTypeName, ")") => PUnnamedParameter(PDeref(typ).at(typ)).at(ctx)
      case Vector("(", typ : PTypeName, ")") => PUnnamedParameter(typ).at(ctx)
      case _ => unexpected(ctx)
    }
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitImplementationProofPredicateAlias(ctx: ImplementationProofPredicateAliasContext): PImplementationProofPredicateAlias = {
    val left = idnUse.get(ctx.IDENTIFIER())
    val right = visitChildren(ctx.selection()) match {
      case name : PNamedOperand => name
      case dot : PDot => dot
      case Vector(typ : PType, ".", _) => PDot(typ, idnUse.get(ctx.selection().IDENTIFIER())).at(ctx.selection())
      case _ => fail(ctx, "must be either a selection or a named operand")
    }
    PImplementationProofPredicateAlias(left, right).at(ctx)
  }
  //endregion



  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitMpredicateDecl(ctx: MpredicateDeclContext): Vector[PMPredicateDecl] = {
    val id = idnDef.get(ctx.IDENTIFIER())
    val receiver = visitReceiver(ctx.receiver())
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val body = if (has(ctx.predicateBody())) Some(visitExpression(ctx.predicateBody().expression())) else None
    Vector(PMPredicateDecl(id, receiver, params.flatten, body).at(ctx))
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitFpredicateDecl(ctx: FpredicateDeclContext): Vector[PFPredicateDecl] = {
    val id = idnDef.get(ctx.IDENTIFIER())
    val params = for (param <- ctx.parameters().parameterDecl().asScala.toVector) yield visitParameterDecl(param)
    val body = if (has(ctx.predicateBody())) Some(visitExpression(ctx.predicateBody().expression())) else None
    Vector(PFPredicateDecl(id, params.flatten, body).at(ctx))
  }
  //endregion

  //endregion

  //region Expressions

  //region Operands
  /**
    * Translates the rule
    * operand: literal | operandName | L_PAREN expression R_PAREN;
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitOperand(ctx: GobraParser.OperandContext): PExpression = {
    visitChildren(ctx) match {
      case e : PExpression => e
      case Vector("(", e : PExpression, ")") => e
      case _ => fail(ctx)
    }
  }

  /**
    * Translates the rule
    *
    * operandName: IDENTIFIER (DOT IDENTIFIER)?;
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitOperandName(ctx: GobraParser.OperandNameContext): PExpression = {
    visitChildren(ctx) match {
      case idnUseLike(id) => id match {
        case id@PIdnUse(_) => PNamedOperand(id).at(id)
        case PWildcard() => PBlankIdentifier().at(ctx)
      }
      case _ => unexpected(ctx)
    }
  }

  override def visitBasicLit(ctx: GobraParser.BasicLitContext): PBasicLiteral = {
    visitChildren(ctx) match {
      case "true" => PBoolLit(true).at(ctx)
      case "false" => PBoolLit(false).at(ctx)
      case "nil" => PNilLit().at(ctx)
      case _ if has(ctx.FLOAT_LIT()) => visitFloat(ctx.FLOAT_LIT())
      case lit : PBasicLiteral => lit
    }
  }

  //endregion

  //region Composite Literals
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitCompositeLit(ctx: CompositeLitContext): PCompositeLit = {
    visitChildren(ctx) match {
      case Vector(typ : PLiteralType, lit : PLiteralValue) => PCompositeLit(typ, lit).at(ctx)
    }
  }


  /** Translates the rule
    * literalValue: L_CURLY (elementList COMMA?)? R_CURLY;
    *
    * Not positioned
    */
  override def visitLiteralValue(ctx: LiteralValueContext): PLiteralValue = {
    visitChildren(ctx) match {
      case Vector(_, elems : Vector[PKeyedElement @unchecked], _*) => PLiteralValue(elems)
      case _ => PLiteralValue(Vector.empty)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitElementList(ctx: ElementListContext): Vector[PKeyedElement] = {
    visitListNode[PKeyedElement](ctx.keyedElement())
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitKeyedElement(ctx: KeyedElementContext): PKeyedElement = {
    val key = visitNodeOrNone[PCompositeKey](ctx.key())
    val elem = visitElement(ctx.element())

    PKeyedElement(key, elem).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitKey(ctx: KeyContext): PCompositeKey = {
    visitChildren(ctx) match {
      case idnUse(id) => PIdentifierKey(id).at(ctx)
      case lit : PLiteralValue => PLitCompositeVal(lit).at(ctx)
      case n@PNamedOperand(id) => PIdentifierKey(id).at(n)
      case n : PExpression => PExpCompositeVal(n).at(n)
      case _ => fail(ctx, "Invalid key")
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitElement(ctx: ElementContext): PCompositeVal = {
    visitChildren(ctx) match {
      case e : PExpression => PExpCompositeVal(e).at(e)
      case lit : PLiteralValue => PLitCompositeVal(lit).at(lit)
      case _ => fail(ctx)
    }
  }

  //endregion

  /** visits a function literal
    * does not position itself
    */
  override def visitFunctionLit(ctx: FunctionLitContext): PFunctionLit = {
    visitChildren(ctx) match {
      case Vector(_, (params: Vector[PParameter] @unchecked, result : PResult), b : PBlock ) => PFunctionLit(params, result, b)
    }
  }

  //region Primary Expressions
  /**
    * Visits the rule
    * primaryExpr: operand | conversion | methodExpr | ghostPrimaryExpr | new_
    *         | primaryExpr ( (DOT IDENTIFIER)| index| slice_| seqUpdExp| typeAssertion| arguments| predConstructArgs );
    *
    * @param ctx the parse tree
    * @return the unpositioned visitor result
    */
  override def visitPrimaryExpr(ctx: PrimaryExprContext): PExpression = {
    visitChildren(ctx) match {
      case e : PExpression => e
      case Vector(pe : PExpression, rest@_*) => rest match {
        // (DOT IDENTIFIER)
        case Vector(".", idnUse(id)) => PDot(pe, id).at(ctx)
        // L_BRACKET expression R_BRACKET
        case Vector(Vector("[", index : PExpression, "]")) => PIndexedExp(pe, index).at(ctx)
        // slice_
        case Vector((low: Option[PExpression] @unchecked, high : Option[PExpression] @unchecked, cap : Option[PExpression] @unchecked)) =>
          PSliceExp(pe, low, high, cap).at(ctx)
        // DOT L_PAREN type_ R_PAREN;
        case Vector(Vector(".", "(", typ : PType, ")")) => PTypeAssertion(pe, typ).at(ctx)
        // seqUpdExp
        case Vector(Updates(upd)) =>
          PGhostCollectionUpdate(pe, upd).at(ctx)
        // arguments
        case Vector(InvokeArgs(args)) => PInvoke(pe, args).at(ctx)
        // predConstructArgs
        case Vector(PredArgs(args)) => val id = pe match {
          case recvWithId@PDot(_, _) => PDottedBase(recvWithId).at(recvWithId)
          case PNamedOperand(identifier@PIdnUse(_)) => PFPredBase(identifier).at(identifier)
          case _ => fail(ctx.primaryExpr(), "Wrong base type for predicate constructor.")
        }
          PPredConstructor(id, args).at(ctx)
        case _ => fail(ctx)
      }
    }
  }


  /**
    * Visits the rule
    * type_ L_PAREN expression COMMA? R_PAREN;
    * @param ctx the parse tree
    *     */
  override def visitConversion(ctx: ConversionContext): PInvoke= {
    visitChildren(ctx) match {
      case Vector(typ : PType, "(", exp : PExpression, _*) => PInvoke(typ, Vector(exp)).at(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitArguments(ctx: GobraParser.ArgumentsContext): InvokeArgs = {
    val exprs : Vector[PExpression] = if (ctx.expressionList() != null) visitExpressionList(ctx.expressionList()) else Vector.empty
    if (ctx.ELLIPSIS() != null) {
      // We have ..., so the last parameter has to be unpacked
      InvokeArgs(exprs.init.appended(PUnpackSlice(exprs.last).at(ctx)))
    } else {
      InvokeArgs(exprs)
    }
  }
  case class InvokeArgs(args : Vector[PExpression])


  /**
    * Visits the rule
    * predConstructArgs: L_PRED expressionList? COMMA? R_PRED
    * @param ctx the parse tree
    *     */
  override def visitPredConstructArgs(ctx: PredConstructArgsContext): PredArgs = {
    PredArgs( // Wrap this to ensure type safe pattern matching
      visitExpressionList(ctx.expressionList()).map {
        case PBlankIdentifier() => None
        case e => Some(e)
      }
    )
  }
  case class PredArgs(args: Vector[Option[PExpression]])

  /**
    * Visits the rule
    * methodExpr: nonNamedType DOT IDENTIFIER
    *
    */
  override def visitMethodExpr(ctx: MethodExprContext): PDot = visitChildren(ctx) match {
    case Vector(typ : PType, ".", idnUse(id)) => PDot(typ, id)
  }

  /**
    * @param ctx the parse tree
    *     */
  override def visitNew_(ctx: New_Context): PNew = {
    val typ : PType = visitNode(ctx.type_())
    PNew(typ).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitMake(ctx: MakeContext): PMake = {
    val typ = visitNode[PType](ctx.type_())
    val args = visitExpressionList(ctx.expressionList())
    PMake(typ, args).at(ctx)
  }



  /**
    * Visits the rule
    * slice_: L_BRACKET ( low? COLON high? | low? COLON high COLON cap) R_BRACKET;
    *
    * @param ctx the parse tree
    */
  override def visitSlice_(ctx: Slice_Context): (Option[PExpression], Option[PExpression], Option[PExpression])  = {
    val low = if (ctx.low() != null) Some(visitExpression(ctx.low().expression())).pos() else None
    val high = if (ctx.high() != null) Some(visitExpression(ctx.high().expression())).pos()  else None
    val cap = if (ctx.cap() != null) Some(visitExpression(ctx.cap().expression())).pos()  else None
    (low, high, cap)
  }

  /**
    * Visits the rule
    * seqUpdExp: L_BRACKET (seqUpdClause (COMMA seqUpdClause)*) R_BRACKET;
    * @param ctx the parse tree
    */
  override def visitSeqUpdExp(ctx: SeqUpdExpContext): Updates = {
    Updates( // Wrap this to ensure type safe matching
      ctx.seqUpdClause().asScala.view.map { upd =>
        PGhostCollectionUpdateClause(visitExpression(upd.expression(0)), visitExpression(upd.expression(1))).at(upd)
      }.toVector
    )
  }
  // Wrapper class to distinguish from Vector[PExpression] in patterns
  case class Updates(upd : Vector[PGhostCollectionUpdateClause])

  //region Ghost primary expressions

  /**
    * Visits the rule
    * range: kind=(SEQ | SET | MSET) L_BRACKET expression DOT_DOT expression R_BRACKET;
    */
  override def visitRange(ctx: RangeContext): AnyRef = super.visitRange(ctx) match {
    case Vector(kind : String, "[", low: PExpression, "..", high : PExpression, "]") =>
      val seqRange = PRangeSequence(low, high).at(ctx)
      kind match {
        case "seq" => seqRange
        case "set" => PSetConversion(seqRange)
        case "mset" => PMultisetConversion(seqRange)
      }
  }


  /**
    * Visits the rule
    * typeOf: TYPE_OF L_PAREN expression R_PAREN;
    */
  override def visitTypeOf(ctx: TypeOfContext): AnyRef = super.visitTypeOf(ctx) match {
    case Vector("typeOf", "(", expr: PExpression, ")") => PTypeOf(expr)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeExpr(ctx: TypeExprContext): AnyRef = super.visitTypeExpr(ctx) match {
    case Vector("type", "[", typ : PType, "]") => PTypeExpr(typ)
  }


  /**
    * Visits the rule
    * isComparable: IS_COMPARABLE L_PAREN expression R_PAREN;
    */
  override def visitIsComparable(ctx: IsComparableContext): AnyRef = super.visitIsComparable(ctx) match {
    case Vector("isComparable", "(", e : PExpression, ")") => PIsComparable(e)
  }


  /**
    * Visits the rule
    * isComparable: IS_COMPARABLE L_PAREN expression R_PAREN;
    */
  override def visitQuantification(ctx: QuantificationContext): AnyRef = super.visitQuantification(ctx) match {
    case Vector(quantifier: String,
      vars : Vector[PBoundVariable], ":", ":", triggers : Vector[PTrigger], body : PExpression) =>
      (quantifier match {
        case "forall" => PForall
        case "exists" => PExists
      })(vars, triggers, body)
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
    conversion(exp)
  }


  /**
    * Visits the rule
    * NONE L_BRACKET type_ R_BRACKET;
    */
  override def visitOptionNone(ctx: OptionNoneContext): AnyRef = super.visitOptionNone(ctx) match {
    case Vector("none" , "[", typ : PType, "]") => POptionNone(typ)
  }


  /**
    * Visits the rule
    * optionSome: SOME L_PAREN expression R_PAREN;
    */
  override def visitOptionSome(ctx: OptionSomeContext): AnyRef = super.visitOptionSome(ctx) match {
    case Vector("some", "(", exp : PExpression, ")") => POptionSome(exp)
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitOptionGet(ctx: OptionGetContext): AnyRef = super.visitOptionGet(ctx) match {
    case Vector("get", "(", exp : PExpression, ")") => POptionGet(exp)
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPermission(ctx: PermissionContext): AnyRef = super.visitPermission(ctx) match {
    case "writePerm" => PFullPerm()
    case "noPerm" => PNoPerm()
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
      // old [<label>] ( <expr )
      val label = if (has(ctx.oldLabelUse().labelUse()))
        PLabelUse(ctx.oldLabelUse().labelUse().IDENTIFIER().getText).at(ctx) else
        PLabelUse(PLabelNode.lhsLabel).at(ctx)
      PLabeledOld(label, expr).at(ctx)
    } else {
      // old ( <expr> )
      POld(expr).at(ctx)
    }
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitBoundVariables(ctx: BoundVariablesContext): Vector[PBoundVariable] = {
    // TODO: Implement visitListNode for non-PNode vectors
    (for (v <- ctx.boundVariableDecl().asScala.toVector) yield visitBoundVariableDecl(v)).flatten
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitBoundVariableDecl(ctx: BoundVariableDeclContext): Vector[PBoundVariable] = {
    val typ = visitNode[PType](ctx.elementType().type_())
    idnDefList.get(ctx.IDENTIFIER().asScala).map(id => PBoundVariable(id, typ.copy).at(id))
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTriggers(ctx: TriggersContext): Vector[PTrigger] = {
    visitListNode[PTrigger](ctx.trigger())
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
  override def visitAccess(ctx: AccessContext): PAccess = {
    visitChildren(ctx) match {
      case Vector("acc", "(", expr : PExpression, ")") =>
        PAccess(expr, PFullPerm().at(expr)).at(ctx)

      case Vector("acc", "(", expr : PExpression, ",", blank : PBlankIdentifier, ")") =>
        PAccess(expr, PWildcardPerm().at(blank)).at(ctx)

      case Vector("acc", "(", expr : PExpression, ",", perm : PExpression, ")") =>
        PAccess(expr, perm).at(ctx)
      case _ => fail(ctx)
    }
  }


  //endregion



  //endregion

  /**
    * visits an expression
    */
  override def visitExpression(ctx: ExpressionContext): PExpression = {
    // Otherwise we have a normal expression
    visitChildren(ctx) match {
      case e : PExpression => e // primaryExpression, unfolding etc
      case Vector(l : PExpression, op : String, r : PExpression) => getBinOp(op, ctx)(l, r).at(ctx)
      case Vector(call : String, "(", arg : PExpression, ")") => getUnaryOp(call, ctx)(arg).at(ctx)
      case Vector("+", e : PExpression) => PAdd(PIntLit(0).at(ctx), e).at(ctx)
      case Vector("-", e : PExpression) => PSub(PIntLit(0).at(ctx), e).at(ctx)
      case Vector(op : String, e : PExpression) => getUnaryOp(op, ctx)(e).at(ctx)
      case Vector(a : PExpression, "?", b : PExpression, ":", c : PExpression) =>  PConditional(a, b, c).at(ctx)
      case x => unexpected(ctx)
    }
  }


  def getBinOp(op : String, ctx : ParserRuleContext) : (PExpression, PExpression) => PExpression = {
    binOp.getOrElse("'"+op+"'", fail(ctx, "invalid binary expression"))
  }

  def getUnaryOp(op : String, ctx : ParserRuleContext) : (PExpression => PExpression) = {
    unaryOp.getOrElse("'"+op+"'", fail(ctx, "invalid unary expression"))
  }


  /**
    *
    * @param ctx the parse tree
    * @return the positioned PUnfolding
    */

  override def visitUnfolding(ctx: UnfoldingContext): PUnfolding = {
    val pred = visitNode[PPredicateAccess](ctx.predicateAccess())
    val op = visitExpression(ctx.expression())
    PUnfolding(pred, op).at(ctx)
  }
  //endregion

  //region Statements

  override def visitStatement(ctx: GobraParser.StatementContext): PStatement = {
    visitChildren(ctx) match {
      // Declaration statements are wrapped in sequences to constitute a statement
      case decl : Vector[PDeclaration] @unchecked => PSeq(decl).at(ctx)
      case s : PStatement => s
      case _ => unexpected(ctx)
    }
  }


  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitEmptyStmt(ctx: EmptyStmtContext): PEmptyStmt = PEmptyStmt().at(ctx)

  //region Labeled statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitLabeledStmt(ctx: LabeledStmtContext): PLabeledStmt = {
    val label = visitLabelDef(ctx.IDENTIFIER())
    val stmt = visitNodeOrElse[PStatement](ctx.statement(), PEmptyStmt().at(label))
    PLabeledStmt(label, stmt).at(ctx)
  }

  def visitLabelDef(node: TerminalNode) : PLabelDef = {
    PLabelDef(node.toString).at(node)
  }

  def visitLabelUse(node: TerminalNode) : PLabelUse = {
    PLabelUse(node.toString).at(node)
  }
  //endregion

  //region Expression statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitExpressionStmt(ctx: ExpressionStmtContext): PExpressionStmt = PExpressionStmt(visitExpression(ctx.expression())).at(ctx)
  //endregion

  //region Send statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitSendStmt(ctx: SendStmtContext): PSendStmt = {
    visitChildren(ctx) match {
      case Vector(channel : PExpression, "<-", msg : PExpression) => PSendStmt(channel, msg).at(ctx)
    }
  }
  //endregion

  //region IncDec statements
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
  //endregion

  //region Assignments
  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  // TODO : Refactor easy
  override def visitAssignment(ctx: GobraParser.AssignmentContext): PSimpleStmt = {
    val left = visitAssigneeList(ctx.expressionList(0)) match {
      case v: Vector[PAssignee] => v
      case _ => fail(ctx, "Assignee List contains non-assignable expressions.")
    }
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
        case Vector(_*) => fail(ctx.expressionList(0), "Assignments with operators can only have exactly one right-hand expression.")
      }, ass_op, left match {
        case Vector(l) => l
        case Vector(_*) => fail(ctx.expressionList(0), "Assignments with operators can only have exactly one left-hand expression.")
      }).at(ctx)
    }
    else {
      PAssignment(right, left)
        .at(ctx)
    }
  }
  //endregion

  //region If statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitIfStmt(ctx: IfStmtContext): PIfStmt = {
    // Because of the structure of PIfStmt, we have to iterate over the if-else chain
    var current = ctx
    // Remember all previous clause contexts
    val ifclauses = ListBuffer[IfStmtContext](current)
    // Move down until we have reached the end
    while (current.ifStmt() != null) {
      current = current.ifStmt()
      ifclauses.append(current)
    }
    // visit each clause
    val ifs : Vector[PIfClause] = ifclauses.view.map(visitIfClause).toVector
    val els = if (current.block(1) != null) Some(visitBlock(current.block(1))) else None
    PIfStmt(ifs,els).at(ctx)
  }

  /**
    * visits an if clause
    * //1
    *
    * @param clause
    * @return
    */

  def visitIfClause(clause: IfStmtContext) : PIfClause = {
    val pre = visitNodeOrNone[PSimpleStmt](clause.simpleStmt())
    val expr = visitExpression(clause.expression())
    // Emit a warning about syntax allowed by Gobra, but not by Go
    if (clause.expression().stop.getType == GobraParser.R_CURLY) warn(clause.expression(), "struct literals at the end of if clauses must be surrounded by parens!")
    val block = visitBlock(clause.block(0))
    PIfClause(pre, expr, block).at(clause)
  }

  //endregion

  //region Switch statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitExprSwitchStmt(ctx: ExprSwitchStmtContext): PExprSwitchStmt = {
    val pre = visitNodeOrNone[PSimpleStmt](ctx.simpleStmt())
    // if the switch has no expression, generate a bool lit
    val expr = visitNodeOrElse[PExpression](ctx.expression(),PBoolLit(true).at(ctx.SWITCH()))
    // iterate through the clauses, partitioning them into normal cases and the default case
    val (dflt, cases) = ctx.exprCaseClause().asScala.toVector.partitionMap(clause =>
      if (has(clause.exprSwitchCase().DEFAULT())) Left(visitExprDefaultClause(clause).body)
      else Right(visitNode[PExprSwitchCase](clause))
    )
    PExprSwitchStmt(pre, expr, cases, dflt).at(ctx)
  }

  def visitExprDefaultClause(ctx: ExprCaseClauseContext): PExprSwitchDflt = {
    PExprSwitchDflt(PBlock(visitStatementList(ctx.statementList())).at(ctx)).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitExprCaseClause(ctx: ExprCaseClauseContext): PExprSwitchCase = {
    val left = visitExpressionList(ctx.exprSwitchCase().expressionList())
    val body = PBlock(visitStatementList(ctx.statementList())).at(ctx)
    PExprSwitchCase(left, body).at(ctx)
  }

  /** Visits a type switch node
    *
    * @param ctx the type switch context
    * @return a positioned PTypeSwitch
    */
  override def visitTypeSwitchStmt(ctx: TypeSwitchStmtContext): PTypeSwitchStmt = {
    val pre = visitNodeOrNone[PSimpleStmt](ctx.simpleStmt())
    val binder = if (has(ctx.typeSwitchGuard().IDENTIFIER())) Some(idnDef.get(ctx.typeSwitchGuard().IDENTIFIER())) else None
    val expr = visitPrimaryExpr(ctx.typeSwitchGuard().primaryExpr())
    // iterate through the cases and partition them into normal cases and the default case
    val (dflt, cases) = ctx.typeCaseClause().asScala.toVector.partitionMap(clause =>
      if (has(clause.typeSwitchCase().DEFAULT())) Left(visitTypeDefaultClause(clause).body) // default : <statements>
      else Right(visitTypeCaseClause(clause)) // case <types> : <statements>
    )
    PTypeSwitchStmt(pre, expr, binder, cases, dflt).at(ctx)

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeList(ctx: TypeListContext): Vector[PExpressionOrType] = {
    val types = visitListNode[PType](ctx.type_())
    // Need to check whether this includes nil, since it's a predeclared identifier and not a type
    if (has(ctx.NIL_LIT()) && !ctx.NIL_LIT().isEmpty) types.appended(PNilLit().at(ctx.NIL_LIT(0))) else types
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitTypeCaseClause(ctx: TypeCaseClauseContext): PTypeSwitchCase = {
    val body = PBlock(visitStatementList(ctx.statementList()))
      .at(if (has(ctx.statementList())) ctx.statementList() else ctx) // If we have no statement list, we need to position at the context
    val left = visitTypeList(ctx.typeSwitchCase().typeList())
    PTypeSwitchCase(left, body).at(ctx)
  }

  def visitTypeDefaultClause(ctx: TypeCaseClauseContext): PTypeSwitchDflt = {
    val body  = PBlock(visitStatementList(ctx.statementList())).at(ctx.statementList())
    PTypeSwitchDflt(body).at(ctx)
  }
  //endregion

  //region For statements
  /**
    * Visits a for statement with specifications
    * @param specCtx
    * @return a positioned PStatement
    */
  // TODO : Refactor
  override def visitSpecForStmt(specCtx: SpecForStmtContext): PStatement = {
    // Visit the loop specifications
    val spec = visitLoopSpec(specCtx.loopSpec())
    // Focus on the for statement now.
    val ctx = specCtx.forStmt()
    val block = visitBlock(ctx.block())

    if (has(ctx.expression())) {
      // for <expression> {...}
      PForStmt(None, visitExpression(ctx.expression()), None, spec, block).at(specCtx)
    } else if(has(ctx.forClause())){
      // for (<pre> ;)? <cond>? ; <post>? {...}
      val pre = visitNodeOrNone[PSimpleStmt](ctx.forClause().initStmt)
      // if there is no condition, generated a true literal
      val cond = if (has(ctx.forClause().expression())) visitExpression(ctx.forClause().expression()) else PBoolLit(true).at(ctx.forClause().expression())
      val post = visitNodeOrNone[PSimpleStmt](ctx.forClause().postStmt)
      PForStmt(pre, cond, post, spec, block).at(specCtx)
    } else if (has(ctx.rangeClause())) {
      // for <assignees (:= | =)>? range <expr>
      val expr = visitExpression(ctx.rangeClause().expression())
      val range = PRange(expr).at(ctx.rangeClause())
      if (has(ctx.rangeClause().DECLARE_ASSIGN())) {
        // :=
        // identifiers should include the blank identifier, but this is currently not supported by PShortForRange
        val goIdnUnkList(idList) = visitIdentifierList(ctx.rangeClause().identifierList())
        PShortForRange(range, idList, block).at(specCtx)
      } else {
        // =
        val assignees = visitAssigneeList(ctx.rangeClause().expressionList()) match {
          case v : Vector[PAssignee] => v
          case _ => fail(ctx)
        }
        PAssForRange(range, assignees, block).at(specCtx)
      }
    } else {
      // for { }
      PForStmt(None, PBoolLit(true).at(ctx.FOR()), None, spec, block).at(specCtx)
    }

  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitTerminationMeasure(ctx: TerminationMeasureContext): PTerminationMeasure = {
    val cond = visitNodeOrNone[PExpression](ctx.expression())
    visitExpressionList(ctx.expressionList()) match {
      case Vector(PBlankIdentifier()) => PWildcardMeasure(cond).at(ctx)
      case exprs@Vector(_, _*) => PTupleTerminationMeasure(exprs, cond).at(ctx)
      case Vector() => PTupleTerminationMeasure(Vector.empty, cond).at(ctx.parent match {
        case s : SpecStatementContext => s.DEC()
        case l : LoopSpecContext => l.DEC()
      })
    }
  }

  /** Visits a loop specification.
    *
    * @param ctx the loop spec context
    * @return a positioned PLoopSpec node
    */
  override def visitLoopSpec(ctx: LoopSpecContext): PLoopSpec = {
    val invs = for (inv <- ctx.expression().asScala.toVector) yield visitExpression(inv)
    val decs = if (has(ctx.terminationMeasure())) Some(visitTerminationMeasure(ctx.terminationMeasure())).pos() else None

    PLoopSpec(invs, decs).at(ctx)
  }
  //endregion

  //region Communicaton statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGoStmt(ctx: GoStmtContext): PGoStmt = {
    visitChildren(ctx) match {
      case Vector("go", exp : PExpression) => PGoStmt(exp).at(ctx)
    }
  }

  //region Select statements
  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitSelectStmt(ctx: SelectStmtContext): PSelectStmt = {
    val clauses = ctx.commClause().asScala.toVector.map(visitCommClause)
    // TODO: Do this in one pass
    val send = clauses collect { case v: PSelectSend => v }
    val rec = clauses collect { case v: PSelectRecv => v }
    val arec = clauses collect { case v: PSelectAssRecv => v }
    val srec = clauses collect { case v: PSelectShortRecv => v }
    val dflt = clauses collect { case v: PSelectDflt => v }

    PSelectStmt(send, rec, arec, srec, dflt).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  // TODO : Refactor
  override def visitCommClause(ctx: CommClauseContext): PSelectClause = {
    val stmts : Vector[PStatement] = visitStatementList(ctx.statementList())
    val body = stmts match {
      case Vector() => PBlock(Vector.empty).at(ctx.COLON())
      case v => PBlock(v).at(ctx.statementList())
    }
    if (has(ctx.commCase().sendStmt())) {
      PSelectSend(visitNode(ctx.commCase().sendStmt()), body)
    } else if (has(ctx.commCase().recvStmt())) {
      val expr = visitExpression(ctx.commCase().recvStmt().recvExpr) match {
        case recv : PReceive => recv
        case _ => fail(ctx, "Receive expression required.")
      }
      // TODO : Refactor Assignees
      if (has(ctx.commCase().recvStmt().ASSIGN())) {
        val ass = visitAssigneeList(ctx.commCase().recvStmt().expressionList()) match {
          case v: Vector[PAssignee] => v
          case _ => fail(ctx, "Assignee List contains non-assignable expressions.")
        }
        PSelectAssRecv(expr, ass, body).at(ctx)
      } else if (has(ctx.commCase().recvStmt().DECLARE_ASSIGN())) {
        val idnUnkList(left) = visitIdentifierList(ctx.commCase().recvStmt().identifierList())
        PSelectShortRecv(expr, left, body).at(ctx)
      } else PSelectRecv(expr, body).at(ctx)
    } else PSelectDflt(body).at(ctx)
  }
  //endregion

  //endregion

  //region Controlflow statements
  /**
    * Visits a return statement
    */
  override def visitReturnStmt(ctx: GobraParser.ReturnStmtContext): PReturn = {
    visitChildren(ctx) match {
      case "return" => PReturn(Vector.empty).at(ctx)
      case Vector("return", exps :Vector[PExpression] @unchecked) => PReturn(exps).at(ctx)
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitBreakStmt(ctx: BreakStmtContext): PBreak = {
    val label = if (has(ctx.IDENTIFIER())) Some(visitLabelUse(ctx.IDENTIFIER())) else None
    PBreak(label).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitContinueStmt(ctx: ContinueStmtContext): PContinue = {
    val label = if (has(ctx.IDENTIFIER())) Some(visitLabelUse(ctx.IDENTIFIER())) else None
    PContinue(label).at(ctx)
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGotoStmt(ctx: GotoStmtContext): PGoto = PGoto(visitLabelUse(ctx.IDENTIFIER())).at(ctx)


  /**
    * Fallthrough
    */
  override def visitFallthroughStmt(ctx: FallthroughStmtContext): AnyRef = fail(ctx, "Fallthrough statements are not supported")

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitDeferStmt(ctx: DeferStmtContext): PDeferStmt = {
    val expr : PExpression = visitExpression(ctx.expression())
    PDeferStmt(expr).at(ctx)
  }
  //endregion

  //region Ghost statement

  /**
    * Visits the production
    * GHOST statement
   **/
  override def visitExplicitGhostStatement(ctx: ExplicitGhostStatementContext): PExplicitGhostStatement = super.visitExplicitGhostStatement(ctx) match {
    case Vector("ghost", stmt : PStatement) => PExplicitGhostStatement(stmt)
  }

  /**
    * Visist the production
    * fold_stmt=(FOLD | UNFOLD) predicateAccess
    *     */
  override def visitFoldStatement(ctx: FoldStatementContext): PGhostStatement = super.visitFoldStatement(ctx) match {
    case Vector("fold", predAcc : PPredicateAccess) => PFold(predAcc)
    case Vector("unfold", predAcc : PPredicateAccess) => PUnfold(predAcc)
  }

  /**
    * Visits the production
    * kind=(ASSUME | ASSERT | INHALE | EXHALE) expression
    *     */
  override def visitGhostHelperStmt(ctx: GhostHelperStmtContext): AnyRef = super.visitGhostHelperStmt(ctx) match {
    case Vector(kind : String, expr : PExpression) => kind match {
      case "assert" => PAssert(expr)
      case "assume" => PAssume(expr)
      case "inhale" => PInhale(expr)
      case "exhale" => PExhale(expr)
    }
  }


  override def visitPredicateAccess(ctx: PredicateAccessContext): PPredicateAccess = super.visitPredicateAccess(ctx) match {
    case invoke : PInvoke => PPredicateAccess(invoke, PFullPerm().at(invoke))
    case PAccess(invoke: PInvoke, perm) => PPredicateAccess(invoke, perm)
    case _ => fail(ctx, "Expected invocation")
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitApplyStmt(ctx: ApplyStmtContext): PApplyWand = {
    visitChildren(ctx) match {
      case Vector("apply", w  : PMagicWand) => PApplyWand(w).at(ctx)
      case e => fail(ctx, s"expected a magic wand but instead got $e")
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitPackageStmt(ctx: PackageStmtContext): PPackageWand = {
    visitChildren(ctx) match {
      case Vector("package", w : PMagicWand) => PPackageWand(w,None).at(ctx)
      case Vector("package", w : PMagicWand, b : PBlock) => PPackageWand(w,Some(b)).at(ctx)
      case Vector(_, e, _*) => fail(ctx,s"expected a magic wand but instead got $e")
    }
  }


  //endregion

  //endregion

  //region Packages
  /**
    * Visits a SourceFileContext. Declarations with blank identifiers are discarded.
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitSourceFile(ctx: GobraParser.SourceFileContext): PProgram = {
    val packageClause : PPackageClause = visitNode(ctx.packageClause())
    val importDecls = ctx.importDecl().asScala.toVector.flatMap(visitImportDecl)

    // Don't parse functions/methods if the identifier is blank
    val functionDecls= visitListNode[PFunctionDecl](ctx.functionDecl())
    val methodDecls = visitListNode[PMethodDecl](ctx.methodDecl())
    val ghostMembers = ctx.ghostMember().asScala.flatMap(visitNode[Vector[PGhostMember]])
    val decls = ctx.declaration().asScala.toVector.flatMap(visitDeclaration(_).asInstanceOf[Vector[PDeclaration]])
    PProgram(packageClause, importDecls, functionDecls ++ methodDecls ++ decls ++ ghostMembers).at(ctx)
  }

  /**
    * Visists a package clause
    * @param ctx the parse tree
    * @return the positioned PPackageclause
    */
  override def visitPackageClause(ctx: GobraParser.PackageClauseContext): PPackageClause = {
    PPackageClause(PPkgDef(ctx.packageName.getText).at(ctx)).at(ctx)
  }

  /**
    * Visit an import declaration
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitImportDecl(ctx: GobraParser.ImportDeclContext): Vector[PImport] = {
    visitListNode[PImport](ctx.importSpec())
  }

  /**
    * Visits and import Specification
    */
  override def visitImportSpec(ctx: GobraParser.ImportSpecContext): PImport = {
    // Get the actual path
    val path = visitString_(ctx.importPath().string_()).lit
    if(ctx.DOT() != null){
      // . "<path>"
      PUnqualifiedImport(path).at(ctx)
    } else if (ctx.IDENTIFIER() != null) {
      // (<identifier> | _) "<path>"
      PExplicitQualifiedImport(idnDefLike.get(ctx.IDENTIFIER()), path).at(ctx)
    } else {
      PImplicitQualifiedImport(path).at(ctx)
    }
  }


  //endregion


  //region Visitor overrides

  override def visitTerminal(node: TerminalNode): AnyRef = node.getSymbol.getType match {
    // Pass Identifiers up so they can be handled by the invoking rule
    case GobraParser.IDENTIFIER => node
    case _ => node.getText
  }

  override def visitChildren(node: RuleNode): AnyRef = {
    node match {
      case context: ParserRuleContext => {
        context.children.asScala.view.map{n =>
          (n.accept(this), n) match {
            case (p : PNode, ctx : ParserRuleContext) => p.at(ctx)
            case (p : PNode, term : TerminalNode) => p.at(term)
            case (p, _) => p
          }
        }.toVector match {
          // make sure to avoid nested vectors of single items
          case Vector(res) => res
          case Vector("(", res, ")") => res
          case Vector("(", res, ")") => res
          case v => v
        }
      }
      // TODO: violation
      case x => violation(s"Expected ParserRuleContext, but got ${x}")
    }
  }

  /** An implementation of the visit function that positions the result, if the
    * context is [[Positionable]] and the result can be positioned.
    *
    * @param tree the tree to be visited
    * @return
    */

  override def visit(tree: ParseTree): AnyRef = {
    val res = super.visit(tree)
    (tree, res) match {
      case (ctx: ParserRuleContext, node : PNode) => node.at(ctx)
      case (ctx: TerminalNode, node : PNode) => node.at(ctx)
      case (ctx: Token, node : PNode) => node.at(ctx)
      case (_, node) => node
    }
  }
  //endregion

  //region Visitor helpers

  /** Helper Function to avoid having to cast when visiting single nodes
    *
    * @param ctx a rule context
    * @tparam P
    * @return
    */
  def visitNode[P <: AnyRef](ctx : ParserRuleContext) : P = {
    visit(ctx) match {
      case p : P @unchecked => p
    }
  }

  /** Helper Function for optional Nodes
    *
    * @param ctx a context that might be null (signified with ? in the grammar)
    * @tparam P The PNode type
    * @return a positioned Option of a positioned PNode
    */
  def visitNodeOrNone[P <: PNode](ctx : ParserRuleContext) : Option[P] = {
    if (ctx != null) {
      Some(visitNode(ctx)).pos()
    } else None
  }

  /** Helper Function for Nodes with a default
    *
    * @param ctx a context that might be null (signified with ? in the grammar)
    * @tparam P The PNode type
    * @return a positioned Option of a positioned PNode
    */
  def visitNodeOrElse[P <: PNode](ctx : ParserRuleContext, default : P) : P = {
    visitNodeOrNone[P](ctx) match {
      case Some(value) => value
      case None => default
    }
  }

  def visitListNode[P <: PNode](ctx : java.util.List[_ <: ParserRuleContext]) : Vector[P] = {
    ctx.asScala.view.map(visitNode[P]).toVector match {
      case e@Vector() => e
      case v => v.range(v.head, v.last)
    }
  }

  def visitNodeIf[P <: PNode, C <: ParserRuleContext](ctx : java.util.List[C], cond : (C => Boolean)) : Vector[P] = {
    ctx.asScala.toVector.collect {
      case c if cond(c) => visitNode(c)
    }.asInstanceOf[Vector[P]]
  }
  //endregion

  //region Error reporting and positioning

  private def fail(cause : NamedPositionable, msg : String = "") = {
    throw TranslationFailure(cause, msg)
  }

  private def unexpected(ctx: ParserRuleContext) = {
    violation(s"could not translate '${ctx.getText}', got unexpected production '${visitChildren(ctx)}'")
  }

  private def warn(cause: NamedPositionable, msg : String): Unit = {
    warnings.append(TranslationWarning(cause, msg))
  }

  implicit class PositionableParserRuleContext[P <: ParserRuleContext](ctx: P) extends NamedPositionable {
    val startPos : Position = ctx.getStart.startPos
    val endPos : Position = ctx.getStop.endPos
    val name : String = GobraParser.ruleNames.array(ctx.getRuleIndex)
    val text : String = ctx.getText
  }

  implicit class PositionableTerminalNode[T <: TerminalNode](term: T) extends NamedPositionable {
    private val tok = term.getSymbol
    val startPos : Position = tok.startPos
    val endPos : Position = tok.endPos
    val name : String = GobraParser.VOCABULARY.getDisplayName(tok.getType)
    val text : String = term.getText
  }

  implicit class PositionableToken[T <: Token](tok: T) extends NamedPositionable {
    val startPos : Position = Position(tok.getLine, tok.getCharPositionInLine + 1, source)
    val endPos : Position = Position(tok.getLine, tok.getCharPositionInLine + 1 +(tok.getStopIndex -tok.getStartIndex), source)
    val name : String = GobraParser.VOCABULARY.getDisplayName(tok.getType)
    val text : String = tok.getText
  }

  implicit class PositionedPNodeOption[O <: Some[PNode]](some: O) {
    def pos(): O = {
      pom.positions.dupPos(some.get, some)
    }

    def copy: O = rewriter.deepclone(some)

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

  implicit class PositionedPAstNodeVector[V <: Vector[PNode]](v: V) {

    def at(p: Positionable): V = {
      pom.positions.setStart(v, p.startPos)
      pom.positions.setFinish(v, p.endPos)
      v
    }

    def at(other: PNode): V = {
      pom.positions.dupPos(other, v)
    }

    def range(from: PNode, to: PNode): V = {
      pom.positions.dupRangePos(from, to, v)
    }

    //def copy: V = rewriter.deepclone(v)
  }
  //endregion


  /**
    * Shorthand for checking if a reference is null
    * @return true iff the argument is not null
    */
  def has : AnyRef => Boolean = _ != null

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


class TranslationException(@unused cause: NamedPositionable, msg : String)  extends Exception(msg)

case class TranslationFailure(cause: NamedPositionable, msg : String = "") extends TranslationException(cause, s"Translation of ${cause.name} ${cause.text} failed${if (msg.nonEmpty) ": " + msg else "."}")
case class TranslationWarning(cause: NamedPositionable, msg : String = "") extends TranslationException(cause, s"Warning in ${cause.name} ${cause.text}${if (msg.nonEmpty) ": " + msg else "."}")
case class UnsupportedOperatorException(cause: NamedPositionable, typ : String, msg : String = "") extends TranslationException(cause, s"Unsupported $typ operation: ${cause.text}.")
