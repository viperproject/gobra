// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.


package viper.gobra.frontend
import org.antlr.v4.runtime.{ParserRuleContext, Token}
import org.antlr.v4.runtime.tree.{ParseTree, RuleNode, TerminalNode}
import org.bitbucket.inkytonik.kiama.util.{Position, Source}
import viper.carbon.boogie.Implicits.lift
import viper.gobra.ast.frontend._
import viper.gobra.util.{Binary, Hexadecimal, Octal}
import viper.gobra.frontend.GobraParser._
import viper.gobra.frontend.Parser.PRewriter
import viper.gobra.frontend.TranslationHelpers._
import viper.gobra.util.Violation.violation

import scala.StringContext.InvalidEscapeException
import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class ParseTreeTranslator(pom: PositionManager, source: Source, specOnly : Boolean = false) extends GobraParserBaseVisitor[AnyRef] {

  val warnings : ListBuffer[TranslationWarning] = ListBuffer.empty

  lazy val rewriter = new PRewriter(pom.positions)

  def translate[Rule <: ParserRuleContext, Node](tree: Rule): Node = {
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
  @unused private val idnUseLikeList = PIdnNodeListEx(idnUseLike)

  // These extractors may only be used where Go allows the blank identifier, but Gobra doesnt
  // They generate a unique PIdnNode whose name starts with "_" to not overlap any other identifiers
  private val goIdnDef = PIdnNodeEx(PIdnDef, term => Some(uniqueWildcard(PIdnDef, term).at(term)))
  private val goIdnDefList = PIdnNodeListEx(goIdnDef)

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
      case dec(digits) => PIntLit(BigInt(digits)).at(ctx)
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
      case _ => fail(ctx, "This literal is not supported yet")
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
      case idnUse(id) => visitTypeIdentifier(id) // replace with `PNamedOperand(id)` when arrays etc. of custom types are supported
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
      case "int8" => PInt8Type().at(typ)
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
      case Vector("[", "]", typ : PType) => PSliceType(typ)
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
    val ghost = has(ctx.GHOST())
    val actualDecl = if (ctx.embeddedField() != null) {
      val et = visitNode[PEmbeddedType](ctx.embeddedField())
      PEmbeddedDecl(et, PIdnDef(et.name).at(et))
    } else {
      val goIdnDefList(ids) = visitIdentifierList(ctx.identifierList())
      val t = visitNode[PType](ctx.type_())
      PFieldDecls(ids map (id => PFieldDecl(id, t.copy).at(id)))
    }
    if (ghost) PExplicitGhostStructClause(actualDecl).at(ctx) else actualDecl
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
      case _ : PDot | Vector("*", _ : PDot) => fail(ctx, "Imported types are not yet supported as embedded interface names")
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
    val params = visitNode[Vector[Vector[PParameter]]](ctx.parameters())
    val result = if (ctx.result() != null) visitResult(ctx.result()) else PResult(Vector.empty).at(ctx)
    (params.flatten, result)
  }

  override def visitParameters(ctx: ParametersContext): Vector[Vector[PParameter]] = {
    for (param <- ctx.parameterDecl().asScala.toVector) yield visitNode[Vector[PParameter]](param)
  }

  type Signature = (Vector[PParameter], PResult)

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitResult(ctx: GobraParser.ResultContext): PResult = {
    super.visitResult(ctx) match {
      case res: Vector[Vector[PParameter]@unchecked] => PResult(res.flatten).at(ctx)
      case typ: PType => PResult(Vector(PUnnamedParameter(typ).at(typ))).at(ctx)
    }
  }

  override def visitActualParameterDecl(ctx: ActualParameterDeclContext): Vector[PParameter] = super.visitActualParameterDecl(ctx) match {
    case Vector(goIdnDefList(ids), typ : PType) => ids.map(id => PNamedParameter(id, typ.copy).at(id))
    case typ : PType => Vector(PUnnamedParameter(typ).at(typ))
  }

  override def visitGhostParameterDecl(ctx: GhostParameterDeclContext): AnyRef = super.visitGhostParameterDecl(ctx) match {
    case Vector("ghost", rest@_*) => rest match {
      case Vector(goIdnDefList(ids), typ : PType) => ids.map(id => PExplicitGhostParameter(PNamedParameter(id, typ.copy).at(id)).at(id))
      case Vector(typ : PType) => Vector(PExplicitGhostParameter(PUnnamedParameter(typ).at(typ)).at(typ))
    }
  }

  override def visitParameterType(ctx: ParameterTypeContext): AnyRef = super.visitParameterType(ctx) match {
    case Vector("...", typ : PType) => PVariadicType(typ)
    case typ: PType => typ
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
      case tn: PUnqualifiedTypeName => PInterfaceName(tn).at(ctx)
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
    val spec = if (ctx.specification() != null)
      visitSpecification(ctx.specification())
    else
      PFunctionSpec(Vector.empty,Vector.empty,Vector.empty, Vector.empty, Vector.empty).at(ctx)
    // The name of each explicitly specified method must be unique and not blank.
    val id = idnDef.get(ctx.IDENTIFIER())
    val args = visitNode[Vector[Vector[PParameter]]](ctx.parameters())
    val result = visitNodeOrElse[PResult](ctx.result(), PResult(Vector.empty))
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
    val args = visitNode[Vector[Vector[PParameter]]](ctx.parameters())
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
    * {@inheritDoc }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostPointerType(ctx: GhostPointerTypeContext): PGhostPointerType = {
    val typ = visitNode[PType](ctx.elementType().type_())
    PGhostPointerType(typ).at(ctx)
  }

  /**
    * {@inheritDoc }
    *
    * <p>The default implementation returns the result of calling
    * {@link #   visitChildren} on {@code ctx}.</p>
    */
  override def visitGhostStructType(ctx: GhostStructTypeContext): PExplicitGhostStructType = {
    val actual = visitNode[PStructType](ctx.structType())
    PExplicitGhostStructType(actual).at(ctx)
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

  override def visitAdtType(ctx: AdtTypeContext): PAdtType = {
    val clauses = ctx.adtClause().asScala.toVector.map(visitAdtClause)
    PAdtType(clauses).at(ctx)
  }

  override def visitAdtClause(ctx: AdtClauseContext): PAdtClause = {
    val id = idnDef.unapply(ctx.IDENTIFIER()).get
    val fieldClauses = ctx.adtFieldDecl().asScala.toVector.map(visitAdtFieldDecl)
    val args = fieldClauses.zipWithIndex.map{
      case (AdtFieldDecl(Vector(), t), idx) =>
        PFieldDecls(Vector(PFieldDecl(PIdnDef(s"_${id.name}_$idx").at(t), t).at(t))).at(t)

      case (AdtFieldDecl(xs, t), _) =>
        PFieldDecls(xs.map(x => PFieldDecl(x, t.copy).at(x))).at(t)
    }

    PAdtClause(id, args).at(ctx)
  }

  case class AdtFieldDecl(vars: Vector[PIdnDef], typ: PType)
  override def visitAdtFieldDecl(ctx: AdtFieldDeclContext): AdtFieldDecl = {
    visitChildren(ctx) match {
      case t: PType => AdtFieldDecl(Vector.empty, t)
      case Vector(goIdnDefList(xs), t: PType) => AdtFieldDecl(xs, t)
      case _ => fail(ctx)
    }
  }

  override def visitMatchStmt(ctx: MatchStmtContext): PMatchStatement = {
    val expr = visitNode[PExpression](ctx.expression())
    val cases = ctx.matchStmtClause().asScala.toVector.map(visitNode[PMatchStmtCase](_))
    PMatchStatement(expr, cases).at(ctx)
  }
  override def visitMatchStmtClause(ctx: MatchStmtClauseContext): PMatchStmtCase = {
    val p = visitNode[PMatchPattern](ctx.matchCase())
    val s = visitNodeOrElse[Vector[PStatement]](ctx.statementList(), Vector.empty)
    PMatchStmtCase(p, s).at(ctx)
  }

  override def visitMatchExpr(ctx: MatchExprContext): PMatchExp = {
    val expr = visitNode[PExpression](ctx.expression())
    val cases = ctx.matchExprClause().asScala.toVector.map(visitNode[PMatchExpClause](_))
    PMatchExp(expr, cases).at(ctx)
  }
  override def visitMatchExprClause(ctx: MatchExprClauseContext): PMatchExpClause = {
    val p = visitNode[PMatchPattern](ctx.matchCase())
    val e = visitNode[PExpression](ctx.expression())

    p match {
      case _: PMatchWildcard => PMatchExpDefault(e).at(ctx)
      case _ => PMatchExpCase(p, e).at(ctx)
    }
  }

  override def visitMatchCase(ctx: MatchCaseContext): PMatchPattern = {
    visitChildren(ctx) match {
      case "default" => PMatchWildcard().at(ctx)
      case Vector("case", p: PMatchPattern) => p
    }
  }

  override def visitMatchPatternBind(ctx: MatchPatternBindContext): PMatchPattern = {
    visitChildren(ctx) match {
      case Vector("?", idnDef(id)) => PMatchBindVar(id).at(ctx)
    }
  }

  override def visitMatchPatternComposite(ctx: MatchPatternCompositeContext): PMatchPattern = {
    val t = visitNode[PType](ctx.literalType())
    val args =
      if (!has(ctx.matchPatternList())) Vector.empty
      else visitNode[Vector[PMatchPattern]](ctx.matchPatternList())

    PMatchAdt(t, args).at(ctx)
  }

  override def visitMatchPatternValue(ctx: MatchPatternValueContext): PMatchPattern = {
    visitNode[PExpression](ctx.expression()) match {
      case _: PBlankIdentifier => PMatchWildcard().at(ctx)
      case expr: PExpression => PMatchValue(expr).at(ctx)
    }
  }

  override def visitMatchPatternList(ctx: MatchPatternListContext): Vector[PMatchPattern] = {
    if (!has(ctx)) Vector.empty
    else (for (expr <- ctx.matchPattern().asScala.toVector) yield visitNode[PMatchPattern](expr)).at(ctx)
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
  override def visitBlock(ctx: GobraParser.BlockContext): PBlock = {
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
    val body = if (has(ctx.statementList())) PBlock(visitStatementList(ctx.statementList())).at(ctx) else PBlock(Vector.empty).at(ctx)
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
    visitListNode[PConstSpec](ctx.constSpec()) match {
      case specs =>
        val expandedDecls = specs.zipWithIndex.map { case (spec, idx) =>
          // In constant declarations, whenever a type AND the init expression of a constant are not provided, the Go
          // compiler chooses the type and init expression of the previous constant which has either an initialization
          // expression or type. If an init expression cannot be found for a constant, Gobra reports an error during
          // type-checking, regardless of whether a type has been specified for it or not. This behavior mimics what
          // the Go compiler does.
          if (spec.right.isEmpty && spec.typ.isEmpty) {
            // When the type and init expression of a constant are not specified, we search for the latest constant C
            // (in declaration order) which has either a type or init expression. Then, we update the parsed node
            // of type PConstSpec node to contain a copy of both the type and the init expression of C. Doing so here
            // makes it easier to evaluate constant expressions that contain `iota`, as it allows us to infer from
            // context the intended value of `iota`. If we don't do this, the best we could do when evaluating an
            // expression containing `iota` would be to pass the intended value of `iota` as a param to the constant
            // evaluator, which requires significant refactoring of the type checker.
            val lastValidSpec = specs.take(idx).findLast(d => d.right.nonEmpty || d.typ.nonEmpty)
            val rhs = lastValidSpec match {
              case Some(s) => s.right.map(_.copy)
              case None => Vector()
            }
            val typ = lastValidSpec.flatMap(_.typ.map(_.copy))
            PConstSpec(typ = typ, left = spec.left, right = rhs).at(spec)
          } else {
            spec
          }
        }
        Vector(PConstDecl(expandedDecls))
    }
  }

  /**
    * {@inheritDoc  }
    *
    * <p>The default implementation returns the result of calling
    * {@link #visitChildren} on {@code ctx}.</p>
    */
  override def visitConstSpec(ctx: GobraParser.ConstSpecContext): PConstSpec = {
    val typ = if (ctx.type_() != null) Some(visitNode[PType](ctx.type_())) else None

    val idnDefLikeList(left) = visitIdentifierList(ctx.identifierList())
    val right = visitExpressionList(ctx.expressionList())

    PConstSpec(typ, right, left).at(ctx)
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


  def visitAssignee(ctx: GobraParser.ExpressionContext): PAssignee = {
    visitNode[PExpression](ctx) match {
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
      (for (expr <- ctx.expression().asScala.toVector) yield visitNode[PExpression](expr)).at(ctx)
  }

  override def visitSpecMember(ctx: SpecMemberContext): PFunctionOrMethodDecl = super.visitSpecMember(ctx) match {
    case Vector(spec : PFunctionSpec, (id: PIdnDef, args: Vector[PParameter@unchecked], result: PResult, body: Option[(PBodyParameterInfo, PBlock)@unchecked]))
      => PFunctionDecl(id, args, result, spec, body)
    case Vector(spec : PFunctionSpec, (id: PIdnDef, receiver: PReceiver, args: Vector[PParameter@unchecked], result: PResult, body: Option[(PBodyParameterInfo, PBlock)@unchecked]))
      => PMethodDecl(id, receiver, args, result, spec, body)
  }

  /**
    * Visit a parse tree produced by `GobraParser`.
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitFunctionDecl(ctx: GobraParser.FunctionDeclContext): (PIdnDef, Vector[PParameter], PResult, Option[(PBodyParameterInfo, PBlock)]) = {
    // Go allows the blank identifier here, but PFunctionDecl doesn't.
    val id = goIdnDef.get(ctx.IDENTIFIER())
    val sig = visitNode[Signature](ctx.signature())
    // Translate the function body if the function is not abstract or trusted, specOnly isn't set or the function is pure
    val body = if (has(ctx.blockWithBodyParameterInfo()) && !ctx.trusted && (!specOnly || ctx.pure)) Some(visitNode[(PBodyParameterInfo, PBlock)](ctx.blockWithBodyParameterInfo())) else None
    (id, sig._1, sig._2, body)
  }


  override def visitMethodDecl(ctx: MethodDeclContext): (PIdnDef, PReceiver, Vector[PParameter], PResult, Option[(PBodyParameterInfo, PBlock)]) = {
    // Go allows the blank identifier here, but PFunctionDecl doesn't.
    val id = goIdnDef.get(ctx.IDENTIFIER())
    val recv = visitNode[PReceiver](ctx.receiver())
    val sig = visitNode[Signature](ctx.signature())
    val body = if (has(ctx.blockWithBodyParameterInfo()) && !ctx.trusted && (!specOnly || ctx.pure)) Some(visitNode[(PBodyParameterInfo, PBlock)](ctx.blockWithBodyParameterInfo())) else None
    (id, recv,sig._1, sig._2, body)
  }

  /**
    * Visits Gobra specifications
    *
    * */
  override def visitSpecification(ctx: GobraParser.SpecificationContext): PFunctionSpec = {
    // Get the backend options if available
    val annotations = {
      if (has(ctx.backendAnnotation()) && has(ctx.backendAnnotation().backendAnnotationList()))
        ctx
          .backendAnnotation()
          .backendAnnotationList()
          .singleBackendAnnotation()
          .asScala
          .map(visitSingleBackendAnnotation)
          .toVector
      else
        Vector.empty
    }
    // Group the specifications by keyword
    val groups = ctx.specStatement().asScala.view.groupBy(_.kind.getType)
    // Get the respective groups
    val pres = groups.getOrElse(GobraParser.PRE, Seq.empty).toVector.map(s => visitNode[PExpression](s.assertion().expression()))
    val preserves = groups.getOrElse(GobraParser.PRESERVES, Vector.empty).toVector.map(s => visitNode[PExpression](s.assertion().expression()))
    val posts = groups.getOrElse(GobraParser.POST, Vector.empty).toVector.map(s => visitNode[PExpression](s.assertion().expression()))
    val terms = groups.getOrElse(GobraParser.DEC, Vector.empty).toVector.map(s => visitTerminationMeasure(s.terminationMeasure()))

    PFunctionSpec(
      pres,
      preserves,
      posts,
      terms,
      annotations,
      isPure = ctx.pure,
      isTrusted = ctx.trusted,
      isOpaque = ctx.opaque,
      mayBeUsedInInit = ctx.mayInit,
    )
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
      case PDeref(t: PNamedOperand) => PMethodReceiveActualPointer(t).at(t)
      case PGhostPointerType(t: PNamedOperand) => PMethodReceiveGhostPointer(t).at(t)
      case f => fail(ctx.type_(), s"Expected declared type or pointer to declared type but got: $f.")
    }

    if (has(ctx.maybeAddressableIdentifier())) {
      val (goIdnDef(id), addr) = visitMaybeAddressableIdentifier(ctx.maybeAddressableIdentifier())
      PNamedReceiver(id, recvType, addr).at(ctx)
    } else PUnnamedReceiver(recvType).at(ctx)
  }

  //region Ghost members

  /**
    * Visits the rule
    * ghostMember: implementationProof
    *   | fpredicateDecl
    *   | mpredicateDecl
    *   | explicitGhostMember;
    *
    * @param ctx the parse tree
    */
  override def visitGhostMember(ctx: GhostMemberContext): Vector[PMember] = super.visitGhostMember(ctx) match {
    case v: Vector[PMember@unchecked] => v // note: we have to resort to PMember as an implementation proof is not a PGhostMember
    case m: PMember => Vector(m)
  }

  /**
    * Visits the rule
    * explicitGhostMember: GHOST (methodDecl | functionDecl | declaration);
    * */
  override def visitExplicitGhostMember(ctx: ExplicitGhostMemberContext): Vector[PExplicitGhostMember] = super.visitExplicitGhostMember(ctx) match {
    case Vector("ghost", decl : PGhostifiableMember) => Vector(PExplicitGhostMember(decl).at(ctx))
    case Vector("ghost", decl : Vector[PGhostifiableMember] @unchecked) => decl.map(PExplicitGhostMember(_).at(ctx))
  }

  override def visitSingleBackendAnnotation(ctx: SingleBackendAnnotationContext): PBackendAnnotation = {
    val key = visit(ctx.backendAnnotationEntry).toString
    val values =
      if (has(ctx.listOfValues())) {
        ctx
        .listOfValues()
        .backendAnnotationEntry()
        .asScala
        .view.map(ctx => visit(ctx).toString)
        .toVector
      } else {
        Vector.empty
      }
    PBackendAnnotation(key, values).at(ctx)
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
    val params = visitNode[Vector[Vector[PParameter]]](ctx.parameters())
    val body = if (has(ctx.predicateBody())) Some(visitNode[PExpression](ctx.predicateBody().expression())) else None
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
    val params = visitNode[Vector[Vector[PParameter]]](ctx.parameters())
    val body = if (has(ctx.predicateBody())) Some(visitNode[PExpression](ctx.predicateBody().expression())) else None
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
        case id@PIdnUse(name) => name match {
          case "iota" => PIota().at(id)
          case _ => PNamedOperand(id).at(id)
        }
        case PWildcard() => PBlankIdentifier().at(ctx)
        case _ => unexpected(ctx)
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
      case Vector(spec: PFunctionSpec, (id: Option[PIdnDef@unchecked], args: Vector[PParameter@unchecked], result: PResult, body: Option[(PBodyParameterInfo, PBlock)@unchecked])) =>
        PFunctionLit(id, PClosureDecl(args, result, spec, body).at(spec))
    }
  }

  override def visitClosureDecl(ctx: GobraParser.ClosureDeclContext): (Option[PIdnDef], Vector[PParameter], PResult, Option[(PBodyParameterInfo, PBlock)]) = {
    val id = if(ctx.IDENTIFIER() == null) None else Some(goIdnDef.get(ctx.IDENTIFIER()))
    val sig = visitNode[Signature](ctx.signature())
    // Translate the function body if the function is not abstract or trusted, specOnly isn't set or the function is pure
    val body = if (has(ctx.blockWithBodyParameterInfo()) && !ctx.trusted && (!specOnly || ctx.pure)) Some(visitNode[(PBodyParameterInfo, PBlock)](ctx.blockWithBodyParameterInfo())) else None
    (id, sig._1, sig._2, body)
  }

  override def visitClosureSpecInstance(ctx: ClosureSpecInstanceContext): PClosureSpecInstance = visitChildren(ctx) match {
    case name: TerminalNode => PClosureSpecInstance(PNamedOperand(idnUse.get(name)).at(name), Vector.empty)
    case imported: PDot => PClosureSpecInstance(imported, Vector.empty)
    case Vector(name: TerminalNode, "{", "}") => PClosureSpecInstance(PNamedOperand(idnUse.get(name)).at(name), Vector.empty)
    case Vector(imported: PDot, "{", "}") => PClosureSpecInstance(imported, Vector.empty)
    case Vector(name: TerminalNode, "{", params: Vector[PKeyedElement@unchecked], "}") => PClosureSpecInstance(PNamedOperand(idnUse.get(name)).at(name), params)
    case Vector(imported: PDot, "{", params: Vector[PKeyedElement@unchecked], "}") => PClosureSpecInstance(imported, params)
  }

  override def visitClosureSpecParams(ctx: ClosureSpecParamsContext): Vector[PKeyedElement] = visitChildren(ctx) match {
    case v: Vector[_] => v collect { case p: PKeyedElement => p }
    case p: PKeyedElement => Vector(p)
  }

  override def visitClosureSpecParam(ctx: ClosureSpecParamContext): PKeyedElement = visitChildren(ctx) match {
    case e: PExpression => PKeyedElement(None, PExpCompositeVal(e).at(e))
    case Vector(name: TerminalNode, ":", e: PExpression) =>
      PKeyedElement(Some(PIdentifierKey(idnUse.get(name)).at(name)), PExpCompositeVal(e).at(e))
  }

  override def visitClosureImplSpecExpr(ctx: ClosureImplSpecExprContext): PClosureImplements = {
    visitChildren(ctx) match {
      case Vector(closure: PExpression, "implements", spec: PClosureSpecInstance) =>
        PClosureImplements(closure, spec)
    }
  }

  override def visitClosureImplProofStmt(ctx: ClosureImplProofStmtContext): PClosureImplProof = visitChildren(ctx) match {
    case Vector("proof", closure: PExpression, "implements", spec:PClosureSpecInstance, body: PBlock) =>
      PClosureImplProof(PClosureImplements(closure, spec), body)
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
  override def visitIndexPrimaryExpr(ctx: IndexPrimaryExprContext): AnyRef = super.visitIndexPrimaryExpr(ctx) match {
    case Vector(pe: PExpression, Vector("[", index : PExpression, "]")) => PIndexedExp(pe, index).at(ctx)
  }

  override def visitSeqUpdPrimaryExpr(ctx: SeqUpdPrimaryExprContext): AnyRef = super.visitSeqUpdPrimaryExpr(ctx) match {
    case Vector(pe: PExpression, Updates(upd)) => PGhostCollectionUpdate(pe, upd)
  }

  override def visitPredConstrPrimaryExpr(ctx: PredConstrPrimaryExprContext): AnyRef = super.visitPredConstrPrimaryExpr(ctx) match {
    case Vector(pe: PExpression, PredArgs(args)) => val id = pe match {
      case recvWithId@PDot(_, _) => PDottedBase(recvWithId).at(recvWithId)
      case PNamedOperand(identifier@PIdnUse(_)) => PFPredBase(identifier).at(identifier)
      case _ => fail(ctx.primaryExpr(), "Wrong base type for predicate constructor.")
    }
      PPredConstructor(id, args).at(ctx)
    case _ => fail(ctx)
  }

  override def visitInvokePrimaryExpr(ctx: InvokePrimaryExprContext): AnyRef = super.visitInvokePrimaryExpr(ctx) match {
    case Vector(pe : PExpression, InvokeArgs(args)) => PInvoke(pe, args, None)
  }

  override def visitRevealInvokePrimaryExpr(ctx: RevealInvokePrimaryExprContext): AnyRef = super.visitRevealInvokePrimaryExpr(ctx) match {
    case Vector(_, pe : PExpression, InvokeArgs(args)) => PInvoke(pe, args, None, true)
  }

  override def visitInvokePrimaryExprWithSpec(ctx: InvokePrimaryExprWithSpecContext): AnyRef = super.visitInvokePrimaryExprWithSpec(ctx) match {
    case Vector(pe: PExpression, InvokeArgs(args), "as", pcs: PClosureSpecInstance) => PInvoke(pe, args, Some(pcs))
  }

  override def visitTypeAssertionPrimaryExpr(ctx: TypeAssertionPrimaryExprContext): AnyRef = super.visitTypeAssertionPrimaryExpr(ctx) match {
    case Vector(pe: PExpression, Vector(".", "(", typ : PType, ")")) => PTypeAssertion(pe, typ)
  }

  override def visitSelectorPrimaryExpr(ctx: SelectorPrimaryExprContext): AnyRef = super.visitSelectorPrimaryExpr(ctx) match {
    case Vector(pe : PExpression, ".", idnUse(id)) => PDot(pe, id)
  }

  override def visitSlicePrimaryExpr(ctx: SlicePrimaryExprContext): AnyRef = super.visitSlicePrimaryExpr(ctx) match {
    case Vector(pe: PExpression, (low: Option[PExpression] @unchecked, high : Option[PExpression] @unchecked, cap : Option[PExpression] @unchecked)) =>
      PSliceExp(pe, low, high, cap).at(ctx)
  }


  override def visitBuiltInCallExpr(ctx: BuiltInCallExprContext): AnyRef = super.visitBuiltInCallExpr(ctx) match {
    case Vector(call : String, "(", arg : PExpression, ")") => getUnaryOp(call, ctx)(arg).at(ctx)
  }

  /**
    * Visits the rule
    * type_ L_PAREN expression COMMA? R_PAREN;
    *
    * @param ctx the parse tree
    *     */
  override def visitConversion(ctx: ConversionContext): PInvoke= {
    visitChildren(ctx) match {
      case Vector(typ : PType, "(", exp : PExpression, _*) => PInvoke(typ, Vector(exp), None).at(ctx)
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
  override def visitSlice_(ctx: Slice_Context): (Option[PExpression], Option[PExpression], Option[PExpression]) = {
    val low = if (ctx.lowSliceArgument() != null) Some(visitNode[PExpression](ctx.lowSliceArgument().expression())).pos() else None
    val high = if (ctx.highSliceArgument() != null) Some(visitNode[PExpression](ctx.highSliceArgument().expression())).pos() else None
    val cap = if (ctx.capSliceArgument() != null) Some(visitNode[PExpression](ctx.capSliceArgument().expression())).pos() else None
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
        PGhostCollectionUpdateClause(visitNode[PExpression](upd.expression(0)), visitNode[PExpression](upd.expression(1))).at(upd)
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
    * typeOf: LOW L_PAREN expression R_PAREN;
    */
  override def visitLow(ctx: LowContext): AnyRef = super.visitLow(ctx) match {
    case Vector("low", "(", expr: PExpression, ")") => PLow(expr)
  }

  /**
    */
  override def visitLowc(ctx: LowcContext): AnyRef = super.visitLowc(ctx) match {
    case Vector("lowContext",  "(", ")") => PLowContext()
  }


  /**
    * Visits the rule
    * isComparable: IS_COMPARABLE L_PAREN expression R_PAREN;
    */
  override def visitQuantification(ctx: QuantificationContext): AnyRef = super.visitQuantification(ctx) match {
    case Vector(quantifier: String,
      vars : Vector[PBoundVariable@unchecked], ":", ":", triggers : Vector[PTrigger@unchecked], body : PExpression) =>
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
    val exp = visitNode[PExpression](ctx.expression())
    val conversion = ctx.kind.getType match {
      case GobraParser.SEQ => PSequenceConversion
      case GobraParser.SET => PSetConversion
      case GobraParser.MSET => PMultisetConversion
      case GobraParser.DICT => PMathMapConversion
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
    val expr = visitNode[PExpression](ctx.expression())
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
  override def visitBefore(ctx: BeforeContext): PGhostExpression = super.visitBefore(ctx) match {
    case Vector("before", "(", exp : PExpression, ")") => PBefore(exp)
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
    PTrigger(for (expr <- ctx.expression().asScala.toVector) yield visitNode[PExpression](expr))
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



  //region Binary Expressions

  def visitBinaryExpr(ctx: ParserRuleContext): PExpression = visitChildren(ctx) match {
    case Vector(l : PExpression, op : String, r : PExpression) => getBinOp(op, ctx)(l, r).at(ctx)

  }

  override def visitOrExpr(ctx: OrExprContext): PExpression = visitBinaryExpr(ctx)

  override def visitP41Expr(ctx: P41ExprContext): PExpression = visitBinaryExpr(ctx)

  override def visitP42Expr(ctx: P42ExprContext): PExpression = visitBinaryExpr(ctx)

  override def visitAddExpr(ctx: AddExprContext): PExpression = visitBinaryExpr(ctx)

  override def visitMulExpr(ctx: MulExprContext): PExpression = visitBinaryExpr(ctx)

  override def visitRelExpr(ctx: RelExprContext): PExpression = visitBinaryExpr(ctx)

  override def visitAndExpr(ctx: AndExprContext): PExpression = visitBinaryExpr(ctx)

  //endregion


  override def visitUnaryExpr(ctx: UnaryExprContext): PExpression = super.visitUnaryExpr(ctx) match {
    case Vector("+", e : PExpression) => PAdd(PIntLit(0).at(ctx), e).at(ctx)
    case Vector("-", e : PExpression) => PSub(PIntLit(0).at(ctx), e).at(ctx)
    case Vector(op : String, e : PExpression) => getUnaryOp(op, ctx)(e).at(ctx)
  }


  override def visitImplication(ctx: ImplicationContext): PExpression = visitBinaryExpr(ctx)


  override def visitTernaryExpr(ctx: TernaryExprContext): PExpression = super.visitTernaryExpr(ctx) match {
    case Vector(a : PExpression, "?", b : PExpression, ":", c : PExpression) => PConditional(a, b, c).at(ctx)
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
    val op = visitNode[PExpression](ctx.expression())
    PUnfolding(pred, op).at(ctx)
  }

  override def visitLet(ctx: LetContext): PLet = {
    val ass = visitNode[PShortVarDecl](ctx.shortVarDecl())
    val op = visitNode[PExpression](ctx.expression())
    PLet(ass, op).at(ctx)
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
  override def visitExpressionStmt(ctx: ExpressionStmtContext): PExpressionStmt = PExpressionStmt(visitNode[PExpression](ctx.expression())).at(ctx)
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
    val exp = visitNode[PExpression](ctx.expression()) match {
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
        case GobraParser.STAR => PMulOp()
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
        case _ => fail(ctx.expressionList(0), "Assignments with operators can only have exactly one right-hand expression.")
      }, ass_op, left match {
        case Vector(l) => l
        case _ => fail(ctx.expressionList(0), "Assignments with operators can only have exactly one left-hand expression.")
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
    val expr = visitNode[PExpression](clause.expression())
    // Emit a warning about syntax allowed by Gobra, but not by Go
    if (clause.expression().stop.getType == GobraParser.R_CURLY) warn(clause.expression(), "struct literals at the end of if clauses must be surrounded by parentheses!")
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
    val expr = visitNode[PExpression](ctx.typeSwitchGuard().primaryExpr())
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
    val body = PBlock(visitStatementList(ctx.statementList())).at(ctx.statementList())
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
      PForStmt(None, visitNode[PExpression](ctx.expression()), None, spec, block).at(specCtx)
    } else if(has(ctx.forClause())){
      // for (<pre> ;)? <cond>? ; <post>? {...}
      val pre = visitNodeOrNone[PSimpleStmt](ctx.forClause().initStmt)
      // if there is no condition, generated a true literal
      val cond = if (has(ctx.forClause().expression())) visitNode[PExpression](ctx.forClause().expression()) else PBoolLit(true).at(ctx.forClause().eos(0))
      val post = visitNodeOrNone[PSimpleStmt](ctx.forClause().postStmt)
      PForStmt(pre, cond, post, spec, block).at(specCtx)
    } else if (has(ctx.rangeClause())) {
      // for <assignees (:= | =)>? range <expr> (with enumerated)?
      val expr = visitNode[PExpression](ctx.rangeClause().expression()).at(ctx.rangeClause())
      // enumerated will be used no matter what, so we just make it a wildcard if it is not
      // present in the range clause
      val enumerated = visitChildren(ctx.rangeClause()) match {
        case Vector(_, _, "range", _, "with", i) if i.toString() == "_" => PWildcard().at(ctx.rangeClause().IDENTIFIER())
        case Vector("range", _, "with", i) if i.toString() == "_" => PWildcard().at(ctx.rangeClause().IDENTIFIER())
        case Vector(_, _, "range", _) | Vector("range", _) => PWildcard().at(ctx.rangeClause())
        case _ => idnUnk.get(ctx.rangeClause().IDENTIFIER()).at(ctx.rangeClause.IDENTIFIER())
      }
      val range = PRange(expr, enumerated).at(ctx.rangeClause())
      if (has(ctx.rangeClause().DECLARE_ASSIGN())) {
        // :=
        val (idnUnkLikeList(vars), addressable) = visitMaybeAddressableIdentifierList(ctx.rangeClause().maybeAddressableIdentifierList())
        PShortForRange(range, vars, addressable, spec, block).at(specCtx)
      } else {
        // =
        val assignees = visitAssigneeList(ctx.rangeClause().expressionList()) match {
          case v : Vector[PAssignee] => if (v.length > 0 ) v else Vector(PBlankIdentifier().at(ctx.rangeClause()))
          case _ => fail(ctx)
        }
        PAssForRange(range, assignees, spec, block).at(specCtx)
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
      case exprs if exprs.nonEmpty => PTupleTerminationMeasure(exprs, cond).at(ctx)
      case Vector() => PTupleTerminationMeasure(Vector.empty, cond).at(ctx.parent match {
        case s : SpecStatementContext => s.DEC()
        case l : LoopSpecContext => l.DEC()
      })
      case _ => unexpected(ctx)
    }
  }

  /** Visits a loop specification.
    *
    * @param ctx the loop spec context
    * @return a positioned PLoopSpec node
    */
  override def visitLoopSpec(ctx: LoopSpecContext): PLoopSpec = {
    val invs = for (inv <- ctx.expression().asScala.toVector) yield visitNode[PExpression](inv)
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
      val expr = visitNode[PExpression](ctx.commCase().recvStmt().recvExpr) match {
        case recv : PReceive => recv
        case _ => fail(ctx, "Receive expression required.")
      }
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
  override def visitDeferStmt(ctx: DeferStmtContext): PDeferStmt = super.visitDeferStmt(ctx) match {
    case Vector("defer", expr: PExpression) => PDeferStmt(expr)
    case Vector("defer", "fold", predAcc : PPredicateAccess)   => PDeferStmt(PFold(predAcc).at(ctx))
    case Vector("defer", "unfold", predAcc : PPredicateAccess) => PDeferStmt(PUnfold(predAcc).at(ctx))
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
    * Visit the production
    * fold_stmt=(FOLD | UNFOLD) predicateAccess
    *     */
  override def visitFoldStatement(ctx: FoldStatementContext): PGhostStatement = super.visitFoldStatement(ctx) match {
    case Vector("fold", predAcc : PPredicateAccess) => PFold(predAcc)
    case Vector("unfold", predAcc : PPredicateAccess) => PUnfold(predAcc)
  }

  override def visitPkgInvStatement(ctx: PkgInvStatementContext): POpenDupPkgInv = {
    POpenDupPkgInv().at(ctx)
  }

  override def visitFriendPkgDecl(ctx: FriendPkgDeclContext): PFriendPkgDecl = {
    val path = visitString_(ctx.importPath().string_()).lit
    val assertion = visitNode[PExpression](ctx.assertion())
    PFriendPkgDecl(path, assertion).at(ctx)
  }


    /**
    * Visits the production
    * kind=(ASSUME | ASSERT | INHALE | EXHALE) expression
    *     */
  override def visitProofStatement(ctx: ProofStatementContext): PGhostStatement = super.visitProofStatement(ctx) match {
    case Vector(kind : String, expr : PExpression) => kind match {
      case "assert" => PAssert(expr)
      case "refute" => PRefute(expr)
      case "assume" => PAssume(expr)
      case "inhale" => PInhale(expr)
      case "exhale" => PExhale(expr)
    }
  }

  /**
    * Visits the production
    * DERIVE expression BY block #deriveStatement
    */
  override def visitDeriveStatement(ctx: DeriveStatementContext): PGhostStatement = super.visitDeriveStatement(ctx) match {
    case Vector("derive", expr: PExpression, "by", block: PBlock) => PDeriveStmt(expr, block)
  }

  override def visitStatementWithSpec(ctx: StatementWithSpecContext): PStatement = super.visitStatementWithSpec(ctx) match {
    case Vector(spec: PFunctionSpec, body: PStatement) => POutline(body, spec)
  }

  override def visitOutlineStatement(ctx: OutlineStatementContext): PSeq = super.visitOutlineStatement(ctx) match {
    case Vector(_, _, stmts: Vector[PStatement@unchecked], _) => PSeq(stmts)
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
      case Vector("apply", w : PMagicWand) => PApplyWand(w).at(ctx)
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
    val packageClause: PPackageClause = visitNode(ctx.packageClause())
    val pkgInvs: Vector[PPkgInvariant] = visitListNode[PPkgInvariant](ctx.pkgInvariant())
    val importDecls = ctx.importDecl().asScala.toVector.flatMap(visitImportDecl)
    val friendPkgs: Vector[PFriendPkgDecl] = visitListNode[PFriendPkgDecl](ctx.friendPkgDecl())
    val members = ctx.member().asScala.toVector.flatMap(visitMember)
    PProgram(packageClause, pkgInvs, importDecls, friendPkgs, members).at(ctx)
  }

  override def visitPreamble(ctx: GobraParser.PreambleContext): PPreamble = {
    val packageClause: PPackageClause = visitNode(ctx.packageClause())
    val pkgInvs: Vector[PPkgInvariant] = visitListNode[PPkgInvariant](ctx.pkgInvariant())
    val importDecls = ctx.importDecl().asScala.toVector.flatMap(visitImportDecl)
    val friendPkgs: Vector[PFriendPkgDecl] = visitListNode[PFriendPkgDecl](ctx.friendPkgDecl())
    PPreamble(packageClause, pkgInvs, importDecls, friendPkgs, pom).at(ctx)
  }

  /**
   * Visits a pkgInvariant
   *
   * @param ctx the parse tree
   * @return the positioned PPkgInvariant
   */
  override def visitPkgInvariant(ctx: PkgInvariantContext): PPkgInvariant = {
    val dup = ctx.DUPLICABLE() != null
    val inv = visitNode[PExpression](ctx.assertion())
    PPkgInvariant(inv, dup).at(ctx)
  }

  /**
    * Visists an import precondition
    * @param ctx the parse tree
    * @return the positioned PPackageclause
    */
  override def visitImportPre(ctx: ImportPreContext): PExpression = visitNode[PExpression](ctx.expression())

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
    val importsVector: Vector[PImport] = visitListNode[PImport](ctx.importSpec())
    val importPres: Vector[PExpression] = visitListNode[PExpression](ctx.importPre())

    if (importsVector.length != 1 && importPres.nonEmpty) {
      /* The following is rejected:
       *   importRequires P
       *   import (
       *      "pkg1"
       *      "pkg2"
       *   )
       */
      fail(ctx, "An import declaration can have import preconditions only when it lists a single package")
    } else if (importsVector.length == 1 && importPres.nonEmpty && importsVector.exists(_.importPres.nonEmpty)) {
      /* The following is rejected:
       *   importRequires P
       *   import (
       *       importRequires Q
       *       "pkg1"
       *   )
       */
      fail(ctx, "An import declaration can have import preconditions only when the single package that is listed does not have import preconditions")
    } else if (importsVector.length == 1 && importPres.nonEmpty) {
      /* if there is only a single importSpec and the importDecl has specification,
       * then update the specification of the importSpec with the one from the importDecl.
       * In other words, the following
       *   importRequires P
       *   import (
       *       "pkg1"
       *   )
       * is transformed into
       *   import (
       *       importRequires P
       *       pkg1"
       *   )
       * This makes it easier to find the import precondition of a PImport later on (in particular, we do not need to find
       * the parent of the PImport to find its import preconditions)
       */
      importsVector.map {
        case i@PUnqualifiedImport(importPath, Vector()) =>
          PUnqualifiedImport(importPath, importPres).at(i)
        case i@PExplicitQualifiedImport(qualifier, importPath, Vector()) =>
          PExplicitQualifiedImport(qualifier, importPath, importPres).at(i)
        case i@PImplicitQualifiedImport(importPath, Vector()) =>
          PImplicitQualifiedImport(importPath, importPres).at(i)
        case i =>
          violation(s"Found unexpected import clause $i")
      }
    } else {
      importsVector
    }
  }

  /**
    * Visits and import Specification
    */
  override def visitImportSpec(ctx: GobraParser.ImportSpecContext): PImport = {
    // Get the actual path
    val path = visitString_(ctx.importPath().string_()).lit
    val importPres: Vector[PExpression] = visitListNode(ctx.importPre())
    if(ctx.DOT() != null){
      // . "<path>"
      PUnqualifiedImport(path, importPres).at(ctx)
    } else if (ctx.IDENTIFIER() != null) {
      // (<identifier> | _) "<path>"
      PExplicitQualifiedImport(idnDefLike.get(ctx.IDENTIFIER()), path, importPres).at(ctx)
    } else {
      PImplicitQualifiedImport(path, importPres).at(ctx)
    }
  }

  /**
    * Visit the rule "member: specMember | declaration | ghostMember;"
    *
    * @param ctx the parse tree
    * @return the visitor result
    */
  override def visitMember(ctx: GobraParser.MemberContext): Vector[PMember] = super.visitMember(ctx) match {
    case v: Vector[PMember@unchecked] => v
    case m: PMember => Vector(m)
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
      case context: ParserRuleContext =>
        if (context.children == null) Vector.empty
        else {
          context.children.asScala.view.map { n =>
            (n.accept(this), n) match {
              case (p: PNode, ctx: ParserRuleContext) => p.at(ctx)
              case (p: PNode, term: TerminalNode) => p.at(term)
              case (p, _) => p
            }
          }.toVector match {
            // make sure to avoid nested vectors of single items
            case Vector(res) => res
            case Vector("(", res, ")") => res
            case v => v
          }
        }

      case x => violation(s"Expected ParserRuleContext, but got $x")
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
    * @tparam P The expected return type
    * @return a positioned Option of a positioned PNode
    */
  def visitNodeOrNone[P](ctx : ParserRuleContext) : Option[P] = {
    if (ctx != null) {
      Some(visitNode(ctx)).pos()
    } else None
  }

  /** Helper Function for Nodes with a default
    *
    * @param ctx a context that might be null (signified with ? in the grammar)
    * @tparam P The expected return type
    * @return a positioned Option of a positioned PNode
    */
  def visitNodeOrElse[P](ctx : ParserRuleContext, default : P) : P = {
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
    val endPos : Position = if (ctx.getStop == null) ctx.getStart.startPos else ctx.getStop.endPos
    val name : String = GobraParser.ruleNames.array(ctx.getRuleIndex)
    val text : String = ctx.getText
  }

  // Terminal nodes are simply positioned at their Symbol
  implicit class PositionableTerminalNode[T <: TerminalNode](term: T) extends PositionableToken(term.getSymbol)

  implicit class PositionableToken[T <: Token](tok: T) extends NamedPositionable {
    val startPos: Position = source.offsetToPosition(tok.getStartIndex)
    val endPos: Position = source.offsetToPosition(tok.getStopIndex + 1)
    val name : String = GobraParser.VOCABULARY.getDisplayName(tok.getType)
    val text : String = tok.getText
  }


  implicit class PositionedAstNode[N <: AnyRef](node: N) {

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

    def pos(): N = {
      node match {
        case Some(a) => pom.positions.dupPos(a, node)
        case a @ (_: Iterable[_] | _ : Product) => pom.positions.dupRangePos(a.head, a.last, node)
        case a => violation(s"$a does not contain other nodes to base its position on.")
      }
    }
  }

  implicit class CopyableNode[N <: PNode](node : N) {
    def copy: N = rewriter.deepclone(node)
  }

  //endregion

  /**
    * Shorthand for checking if a reference is null
    * @return true iff the argument is not null
    */
  def has : AnyRef => Boolean = _ != null

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


class TranslationException(@unused cause: NamedPositionable, msg : String) extends Exception(msg)

case class TranslationFailure(cause: NamedPositionable, msg : String = "") extends TranslationException(cause, s"Translation of ${cause.name} ${cause.text} failed${if (msg.nonEmpty) ": " + msg else "."}")
case class TranslationWarning(cause: NamedPositionable, msg : String = "") extends TranslationException(cause, s"Warning in ${cause.name} ${cause.text}${if (msg.nonEmpty) ": " + msg else "."}")
case class UnsupportedOperatorException(cause: NamedPositionable, typ : String, msg : String = "") extends TranslationException(cause, s"Unsupported $typ operation: ${cause.text}.")
