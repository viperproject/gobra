// Generated from src/main/antlr4/GobraParser.g4 by ANTLR 4.9.2
package viper.gobra.frontend;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GobraParser extends GobraParserBase {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		FLOAT_LIT=1, DECIMAL_FLOAT_LIT=2, TRUE=3, FALSE=4, ASSERT=5, ASSUME=6, 
		INHALE=7, EXHALE=8, PRE=9, PRESERVES=10, POST=11, INV=12, DEC=13, PURE=14, 
		IMPL=15, AS=16, OLD=17, BEFORE=18, LHS=19, FORALL=20, EXISTS=21, ACCESS=22, 
		FOLD=23, UNFOLD=24, UNFOLDING=25, LET=26, GHOST=27, IN=28, MULTI=29, SUBSET=30, 
		UNION=31, INTERSECTION=32, SETMINUS=33, IMPLIES=34, WAND=35, APPLY=36, 
		QMARK=37, L_PRED=38, R_PRED=39, SEQ=40, SET=41, MSET=42, DICT=43, OPT=44, 
		LEN=45, NEW=46, MAKE=47, CAP=48, SOME=49, GET=50, DOM=51, AXIOM=52, ADT=53, 
		MATCH=54, NONE=55, PRED=56, TYPE_OF=57, IS_COMPARABLE=58, LOW=59, SHARE=60, 
		ADDR_MOD=61, DOT_DOT=62, SHARED=63, EXCLUSIVE=64, PREDICATE=65, WRITEPERM=66, 
		NOPERM=67, TRUSTED=68, OUTLINE=69, INIT_POST=70, IMPORT_PRE=71, PROOF=72, 
		GHOST_EQUALS=73, GHOST_NOT_EQUALS=74, WITH=75, BREAK=76, DEFAULT=77, FUNC=78, 
		INTERFACE=79, SELECT=80, CASE=81, DEFER=82, GO=83, MAP=84, STRUCT=85, 
		CHAN=86, ELSE=87, GOTO=88, PACKAGE=89, SWITCH=90, CONST=91, FALLTHROUGH=92, 
		IF=93, RANGE=94, TYPE=95, CONTINUE=96, FOR=97, IMPORT=98, RETURN=99, VAR=100, 
		NIL_LIT=101, IDENTIFIER=102, L_PAREN=103, R_PAREN=104, L_CURLY=105, R_CURLY=106, 
		L_BRACKET=107, R_BRACKET=108, ASSIGN=109, COMMA=110, SEMI=111, COLON=112, 
		DOT=113, PLUS_PLUS=114, MINUS_MINUS=115, DECLARE_ASSIGN=116, ELLIPSIS=117, 
		LOGICAL_OR=118, LOGICAL_AND=119, EQUALS=120, NOT_EQUALS=121, LESS=122, 
		LESS_OR_EQUALS=123, GREATER=124, GREATER_OR_EQUALS=125, OR=126, DIV=127, 
		MOD=128, LSHIFT=129, RSHIFT=130, BIT_CLEAR=131, EXCLAMATION=132, PLUS=133, 
		MINUS=134, CARET=135, STAR=136, AMPERSAND=137, RECEIVE=138, DECIMAL_LIT=139, 
		BINARY_LIT=140, OCTAL_LIT=141, HEX_LIT=142, HEX_FLOAT_LIT=143, IMAGINARY_LIT=144, 
		RUNE_LIT=145, BYTE_VALUE=146, OCTAL_BYTE_VALUE=147, HEX_BYTE_VALUE=148, 
		LITTLE_U_VALUE=149, BIG_U_VALUE=150, RAW_STRING_LIT=151, INTERPRETED_STRING_LIT=152, 
		WS=153, COMMENT=154, TERMINATOR=155, LINE_COMMENT=156, WS_NLSEMI=157, 
		COMMENT_NLSEMI=158, LINE_COMMENT_NLSEMI=159, EOS=160, OTHER=161;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_sourceFile = 5, RULE_initPost = 6, 
		RULE_importPre = 7, RULE_importSpec = 8, RULE_importDecl = 9, RULE_ghostMember = 10, 
		RULE_ghostStatement = 11, RULE_auxiliaryStatement = 12, RULE_statementWithSpec = 13, 
		RULE_outlineStatement = 14, RULE_ghostPrimaryExpr = 15, RULE_permission = 16, 
		RULE_typeExpr = 17, RULE_boundVariables = 18, RULE_boundVariableDecl = 19, 
		RULE_triggers = 20, RULE_trigger = 21, RULE_predicateAccess = 22, RULE_optionSome = 23, 
		RULE_optionNone = 24, RULE_optionGet = 25, RULE_sConversion = 26, RULE_old = 27, 
		RULE_oldLabelUse = 28, RULE_labelUse = 29, RULE_before = 30, RULE_isComparable = 31, 
		RULE_low = 32, RULE_typeOf = 33, RULE_access = 34, RULE_range = 35, RULE_matchExpr = 36, 
		RULE_matchExprClause = 37, RULE_seqUpdExp = 38, RULE_seqUpdClause = 39, 
		RULE_ghostTypeLit = 40, RULE_domainType = 41, RULE_domainClause = 42, 
		RULE_adtType = 43, RULE_adtClause = 44, RULE_ghostSliceType = 45, RULE_sqType = 46, 
		RULE_specification = 47, RULE_specStatement = 48, RULE_terminationMeasure = 49, 
		RULE_assertion = 50, RULE_matchStmt = 51, RULE_matchStmtClause = 52, RULE_matchCase = 53, 
		RULE_matchPattern = 54, RULE_matchPatternList = 55, RULE_blockWithBodyParameterInfo = 56, 
		RULE_closureSpecInstance = 57, RULE_closureSpecParams = 58, RULE_closureSpecParam = 59, 
		RULE_closureImplProofStmt = 60, RULE_implementationProof = 61, RULE_methodImplementationProof = 62, 
		RULE_nonLocalReceiver = 63, RULE_selection = 64, RULE_implementationProofPredicateAlias = 65, 
		RULE_make = 66, RULE_new_ = 67, RULE_specMember = 68, RULE_functionDecl = 69, 
		RULE_methodDecl = 70, RULE_explicitGhostMember = 71, RULE_fpredicateDecl = 72, 
		RULE_predicateBody = 73, RULE_mpredicateDecl = 74, RULE_varSpec = 75, 
		RULE_shortVarDecl = 76, RULE_receiver = 77, RULE_parameterDecl = 78, RULE_actualParameterDecl = 79, 
		RULE_ghostParameterDecl = 80, RULE_parameterType = 81, RULE_expression = 82, 
		RULE_statement = 83, RULE_applyStmt = 84, RULE_packageStmt = 85, RULE_specForStmt = 86, 
		RULE_loopSpec = 87, RULE_deferStmt = 88, RULE_basicLit = 89, RULE_primaryExpr = 90, 
		RULE_functionLit = 91, RULE_closureDecl = 92, RULE_predConstructArgs = 93, 
		RULE_interfaceType = 94, RULE_predicateSpec = 95, RULE_methodSpec = 96, 
		RULE_type_ = 97, RULE_typeLit = 98, RULE_predType = 99, RULE_predTypeParams = 100, 
		RULE_literalType = 101, RULE_implicitArray = 102, RULE_slice_ = 103, RULE_lowSliceArgument = 104, 
		RULE_highSliceArgument = 105, RULE_capSliceArgument = 106, RULE_assign_op = 107, 
		RULE_rangeClause = 108, RULE_packageClause = 109, RULE_importPath = 110, 
		RULE_declaration = 111, RULE_constDecl = 112, RULE_constSpec = 113, RULE_identifierList = 114, 
		RULE_expressionList = 115, RULE_typeDecl = 116, RULE_typeSpec = 117, RULE_varDecl = 118, 
		RULE_block = 119, RULE_statementList = 120, RULE_simpleStmt = 121, RULE_expressionStmt = 122, 
		RULE_sendStmt = 123, RULE_incDecStmt = 124, RULE_assignment = 125, RULE_emptyStmt = 126, 
		RULE_labeledStmt = 127, RULE_returnStmt = 128, RULE_breakStmt = 129, RULE_continueStmt = 130, 
		RULE_gotoStmt = 131, RULE_fallthroughStmt = 132, RULE_ifStmt = 133, RULE_switchStmt = 134, 
		RULE_exprSwitchStmt = 135, RULE_exprCaseClause = 136, RULE_exprSwitchCase = 137, 
		RULE_typeSwitchStmt = 138, RULE_typeSwitchGuard = 139, RULE_typeCaseClause = 140, 
		RULE_typeSwitchCase = 141, RULE_typeList = 142, RULE_selectStmt = 143, 
		RULE_commClause = 144, RULE_commCase = 145, RULE_recvStmt = 146, RULE_forStmt = 147, 
		RULE_forClause = 148, RULE_goStmt = 149, RULE_typeName = 150, RULE_arrayType = 151, 
		RULE_arrayLength = 152, RULE_elementType = 153, RULE_pointerType = 154, 
		RULE_sliceType = 155, RULE_mapType = 156, RULE_channelType = 157, RULE_functionType = 158, 
		RULE_signature = 159, RULE_result = 160, RULE_parameters = 161, RULE_conversion = 162, 
		RULE_nonNamedType = 163, RULE_operand = 164, RULE_literal = 165, RULE_integer = 166, 
		RULE_operandName = 167, RULE_qualifiedIdent = 168, RULE_compositeLit = 169, 
		RULE_literalValue = 170, RULE_elementList = 171, RULE_keyedElement = 172, 
		RULE_key = 173, RULE_element = 174, RULE_structType = 175, RULE_fieldDecl = 176, 
		RULE_string_ = 177, RULE_embeddedField = 178, RULE_index = 179, RULE_typeAssertion = 180, 
		RULE_arguments = 181, RULE_methodExpr = 182, RULE_receiverType = 183, 
		RULE_eos = 184;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "sourceFile", "initPost", "importPre", 
			"importSpec", "importDecl", "ghostMember", "ghostStatement", "auxiliaryStatement", 
			"statementWithSpec", "outlineStatement", "ghostPrimaryExpr", "permission", 
			"typeExpr", "boundVariables", "boundVariableDecl", "triggers", "trigger", 
			"predicateAccess", "optionSome", "optionNone", "optionGet", "sConversion", 
			"old", "oldLabelUse", "labelUse", "before", "isComparable", "low", "typeOf", 
			"access", "range", "matchExpr", "matchExprClause", "seqUpdExp", "seqUpdClause", 
			"ghostTypeLit", "domainType", "domainClause", "adtType", "adtClause", 
			"ghostSliceType", "sqType", "specification", "specStatement", "terminationMeasure", 
			"assertion", "matchStmt", "matchStmtClause", "matchCase", "matchPattern", 
			"matchPatternList", "blockWithBodyParameterInfo", "closureSpecInstance", 
			"closureSpecParams", "closureSpecParam", "closureImplProofStmt", "implementationProof", 
			"methodImplementationProof", "nonLocalReceiver", "selection", "implementationProofPredicateAlias", 
			"make", "new_", "specMember", "functionDecl", "methodDecl", "explicitGhostMember", 
			"fpredicateDecl", "predicateBody", "mpredicateDecl", "varSpec", "shortVarDecl", 
			"receiver", "parameterDecl", "actualParameterDecl", "ghostParameterDecl", 
			"parameterType", "expression", "statement", "applyStmt", "packageStmt", 
			"specForStmt", "loopSpec", "deferStmt", "basicLit", "primaryExpr", "functionLit", 
			"closureDecl", "predConstructArgs", "interfaceType", "predicateSpec", 
			"methodSpec", "type_", "typeLit", "predType", "predTypeParams", "literalType", 
			"implicitArray", "slice_", "lowSliceArgument", "highSliceArgument", "capSliceArgument", 
			"assign_op", "rangeClause", "packageClause", "importPath", "declaration", 
			"constDecl", "constSpec", "identifierList", "expressionList", "typeDecl", 
			"typeSpec", "varDecl", "block", "statementList", "simpleStmt", "expressionStmt", 
			"sendStmt", "incDecStmt", "assignment", "emptyStmt", "labeledStmt", "returnStmt", 
			"breakStmt", "continueStmt", "gotoStmt", "fallthroughStmt", "ifStmt", 
			"switchStmt", "exprSwitchStmt", "exprCaseClause", "exprSwitchCase", "typeSwitchStmt", 
			"typeSwitchGuard", "typeCaseClause", "typeSwitchCase", "typeList", "selectStmt", 
			"commClause", "commCase", "recvStmt", "forStmt", "forClause", "goStmt", 
			"typeName", "arrayType", "arrayLength", "elementType", "pointerType", 
			"sliceType", "mapType", "channelType", "functionType", "signature", "result", 
			"parameters", "conversion", "nonNamedType", "operand", "literal", "integer", 
			"operandName", "qualifiedIdent", "compositeLit", "literalValue", "elementList", 
			"keyedElement", "key", "element", "structType", "fieldDecl", "string_", 
			"embeddedField", "index", "typeAssertion", "arguments", "methodExpr", 
			"receiverType", "eos"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'true'", "'false'", "'assert'", "'assume'", "'inhale'", 
			"'exhale'", "'requires'", "'preserves'", "'ensures'", "'invariant'", 
			"'decreases'", "'pure'", "'implements'", "'as'", "'old'", "'before'", 
			"'#lhs'", "'forall'", "'exists'", "'acc'", "'fold'", "'unfold'", "'unfolding'", 
			"'let'", "'ghost'", "'in'", "'#'", "'subset'", "'union'", "'intersection'", 
			"'setminus'", "'==>'", "'--*'", "'apply'", "'?'", "'!<'", "'!>'", "'seq'", 
			"'set'", "'mset'", "'dict'", "'option'", "'len'", "'new'", "'make'", 
			"'cap'", "'some'", "'get'", "'domain'", "'axiom'", "'adt'", "'match'", 
			"'none'", "'pred'", "'typeOf'", "'isComparable'", "'low'", "'share'", 
			"'@'", "'..'", "'shared'", "'exclusive'", "'predicate'", "'writePerm'", 
			"'noPerm'", "'trusted'", "'outline'", "'initEnsures'", "'importRequires'", 
			"'proof'", "'==='", "'!=='", "'with'", "'break'", "'default'", "'func'", 
			"'interface'", "'select'", "'case'", "'defer'", "'go'", "'map'", "'struct'", 
			"'chan'", "'else'", "'goto'", "'package'", "'switch'", "'const'", "'fallthrough'", 
			"'if'", "'range'", "'type'", "'continue'", "'for'", "'import'", "'return'", 
			"'var'", "'nil'", null, "'('", "')'", "'{'", "'}'", "'['", "']'", "'='", 
			"','", "';'", "':'", "'.'", "'++'", "'--'", "':='", "'...'", "'||'", 
			"'&&'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'|'", "'/'", "'%'", 
			"'<<'", "'>>'", "'&^'", "'!'", "'+'", "'-'", "'^'", "'*'", "'&'", "'<-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FLOAT_LIT", "DECIMAL_FLOAT_LIT", "TRUE", "FALSE", "ASSERT", "ASSUME", 
			"INHALE", "EXHALE", "PRE", "PRESERVES", "POST", "INV", "DEC", "PURE", 
			"IMPL", "AS", "OLD", "BEFORE", "LHS", "FORALL", "EXISTS", "ACCESS", "FOLD", 
			"UNFOLD", "UNFOLDING", "LET", "GHOST", "IN", "MULTI", "SUBSET", "UNION", 
			"INTERSECTION", "SETMINUS", "IMPLIES", "WAND", "APPLY", "QMARK", "L_PRED", 
			"R_PRED", "SEQ", "SET", "MSET", "DICT", "OPT", "LEN", "NEW", "MAKE", 
			"CAP", "SOME", "GET", "DOM", "AXIOM", "ADT", "MATCH", "NONE", "PRED", 
			"TYPE_OF", "IS_COMPARABLE", "LOW", "SHARE", "ADDR_MOD", "DOT_DOT", "SHARED", 
			"EXCLUSIVE", "PREDICATE", "WRITEPERM", "NOPERM", "TRUSTED", "OUTLINE", 
			"INIT_POST", "IMPORT_PRE", "PROOF", "GHOST_EQUALS", "GHOST_NOT_EQUALS", 
			"WITH", "BREAK", "DEFAULT", "FUNC", "INTERFACE", "SELECT", "CASE", "DEFER", 
			"GO", "MAP", "STRUCT", "CHAN", "ELSE", "GOTO", "PACKAGE", "SWITCH", "CONST", 
			"FALLTHROUGH", "IF", "RANGE", "TYPE", "CONTINUE", "FOR", "IMPORT", "RETURN", 
			"VAR", "NIL_LIT", "IDENTIFIER", "L_PAREN", "R_PAREN", "L_CURLY", "R_CURLY", 
			"L_BRACKET", "R_BRACKET", "ASSIGN", "COMMA", "SEMI", "COLON", "DOT", 
			"PLUS_PLUS", "MINUS_MINUS", "DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", 
			"LOGICAL_AND", "EQUALS", "NOT_EQUALS", "LESS", "LESS_OR_EQUALS", "GREATER", 
			"GREATER_OR_EQUALS", "OR", "DIV", "MOD", "LSHIFT", "RSHIFT", "BIT_CLEAR", 
			"EXCLAMATION", "PLUS", "MINUS", "CARET", "STAR", "AMPERSAND", "RECEIVE", 
			"DECIMAL_LIT", "BINARY_LIT", "OCTAL_LIT", "HEX_LIT", "HEX_FLOAT_LIT", 
			"IMAGINARY_LIT", "RUNE_LIT", "BYTE_VALUE", "OCTAL_BYTE_VALUE", "HEX_BYTE_VALUE", 
			"LITTLE_U_VALUE", "BIG_U_VALUE", "RAW_STRING_LIT", "INTERPRETED_STRING_LIT", 
			"WS", "COMMENT", "TERMINATOR", "LINE_COMMENT", "WS_NLSEMI", "COMMENT_NLSEMI", 
			"LINE_COMMENT_NLSEMI", "EOS", "OTHER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "GobraParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	boolean specOnly = false;
	public GobraParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ExprOnlyContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(GobraParser.EOF, 0); }
		public ExprOnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprOnly; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExprOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprOnlyContext exprOnly() throws RecognitionException {
		ExprOnlyContext _localctx = new ExprOnlyContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_exprOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			expression(0);
			setState(371);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmtOnlyContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode EOF() { return getToken(GobraParser.EOF, 0); }
		public StmtOnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmtOnly; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitStmtOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtOnlyContext stmtOnly() throws RecognitionException {
		StmtOnlyContext _localctx = new StmtOnlyContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmtOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(373);
			statement();
			setState(374);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeOnlyContext extends ParserRuleContext {
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode EOF() { return getToken(GobraParser.EOF, 0); }
		public TypeOnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeOnly; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeOnlyContext typeOnly() throws RecognitionException {
		TypeOnlyContext _localctx = new TypeOnlyContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_typeOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			type_();
			setState(377);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MaybeAddressableIdentifierListContext extends ParserRuleContext {
		public List<MaybeAddressableIdentifierContext> maybeAddressableIdentifier() {
			return getRuleContexts(MaybeAddressableIdentifierContext.class);
		}
		public MaybeAddressableIdentifierContext maybeAddressableIdentifier(int i) {
			return getRuleContext(MaybeAddressableIdentifierContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public MaybeAddressableIdentifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maybeAddressableIdentifierList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMaybeAddressableIdentifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaybeAddressableIdentifierListContext maybeAddressableIdentifierList() throws RecognitionException {
		MaybeAddressableIdentifierListContext _localctx = new MaybeAddressableIdentifierListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_maybeAddressableIdentifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			maybeAddressableIdentifier();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(380);
				match(COMMA);
				setState(381);
				maybeAddressableIdentifier();
				}
				}
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MaybeAddressableIdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode ADDR_MOD() { return getToken(GobraParser.ADDR_MOD, 0); }
		public MaybeAddressableIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maybeAddressableIdentifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMaybeAddressableIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaybeAddressableIdentifierContext maybeAddressableIdentifier() throws RecognitionException {
		MaybeAddressableIdentifierContext _localctx = new MaybeAddressableIdentifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_maybeAddressableIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			match(IDENTIFIER);
			setState(389);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(388);
				match(ADDR_MOD);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SourceFileContext extends ParserRuleContext {
		public PackageClauseContext packageClause() {
			return getRuleContext(PackageClauseContext.class,0);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public TerminalNode EOF() { return getToken(GobraParser.EOF, 0); }
		public List<InitPostContext> initPost() {
			return getRuleContexts(InitPostContext.class);
		}
		public InitPostContext initPost(int i) {
			return getRuleContext(InitPostContext.class,i);
		}
		public List<ImportDeclContext> importDecl() {
			return getRuleContexts(ImportDeclContext.class);
		}
		public ImportDeclContext importDecl(int i) {
			return getRuleContext(ImportDeclContext.class,i);
		}
		public List<SpecMemberContext> specMember() {
			return getRuleContexts(SpecMemberContext.class);
		}
		public SpecMemberContext specMember(int i) {
			return getRuleContext(SpecMemberContext.class,i);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<GhostMemberContext> ghostMember() {
			return getRuleContexts(GhostMemberContext.class);
		}
		public GhostMemberContext ghostMember(int i) {
			return getRuleContext(GhostMemberContext.class,i);
		}
		public SourceFileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceFile; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSourceFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceFileContext sourceFile() throws RecognitionException {
		SourceFileContext _localctx = new SourceFileContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_sourceFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INIT_POST) {
				{
				{
				setState(391);
				initPost();
				setState(392);
				eos();
				}
				}
				setState(398);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(399);
			packageClause();
			setState(400);
			eos();
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT_PRE || _la==IMPORT) {
				{
				{
				setState(401);
				importDecl();
				setState(402);
				eos();
				}
				}
				setState(408);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(418);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & ((1L << (PRE - 9)) | (1L << (PRESERVES - 9)) | (1L << (POST - 9)) | (1L << (DEC - 9)) | (1L << (PURE - 9)) | (1L << (GHOST - 9)) | (1L << (SEQ - 9)) | (1L << (SET - 9)) | (1L << (MSET - 9)) | (1L << (DICT - 9)) | (1L << (OPT - 9)) | (1L << (DOM - 9)) | (1L << (ADT - 9)) | (1L << (PRED - 9)) | (1L << (TRUSTED - 9)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (FUNC - 78)) | (1L << (INTERFACE - 78)) | (1L << (MAP - 78)) | (1L << (STRUCT - 78)) | (1L << (CHAN - 78)) | (1L << (CONST - 78)) | (1L << (TYPE - 78)) | (1L << (VAR - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (STAR - 78)) | (1L << (RECEIVE - 78)))) != 0)) {
				{
				{
				setState(412);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(409);
					specMember();
					}
					break;
				case 2:
					{
					setState(410);
					declaration();
					}
					break;
				case 3:
					{
					setState(411);
					ghostMember();
					}
					break;
				}
				setState(414);
				eos();
				}
				}
				setState(420);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(421);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitPostContext extends ParserRuleContext {
		public TerminalNode INIT_POST() { return getToken(GobraParser.INIT_POST, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InitPostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initPost; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitInitPost(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitPostContext initPost() throws RecognitionException {
		InitPostContext _localctx = new InitPostContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_initPost);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			match(INIT_POST);
			setState(424);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportPreContext extends ParserRuleContext {
		public TerminalNode IMPORT_PRE() { return getToken(GobraParser.IMPORT_PRE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ImportPreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importPre; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImportPre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportPreContext importPre() throws RecognitionException {
		ImportPreContext _localctx = new ImportPreContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_importPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(IMPORT_PRE);
			setState(427);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportSpecContext extends ParserRuleContext {
		public Token alias;
		public ImportPathContext importPath() {
			return getRuleContext(ImportPathContext.class,0);
		}
		public List<ImportPreContext> importPre() {
			return getRuleContexts(ImportPreContext.class);
		}
		public ImportPreContext importPre(int i) {
			return getRuleContext(ImportPreContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public ImportSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImportSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportSpecContext importSpec() throws RecognitionException {
		ImportSpecContext _localctx = new ImportSpecContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT_PRE) {
				{
				{
				setState(429);
				importPre();
				setState(430);
				eos();
				}
				}
				setState(436);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(437);
				((ImportSpecContext)_localctx).alias = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==IDENTIFIER || _la==DOT) ) {
					((ImportSpecContext)_localctx).alias = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(440);
			importPath();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportDeclContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(GobraParser.IMPORT, 0); }
		public List<ImportSpecContext> importSpec() {
			return getRuleContexts(ImportSpecContext.class);
		}
		public ImportSpecContext importSpec(int i) {
			return getRuleContext(ImportSpecContext.class,i);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<ImportPreContext> importPre() {
			return getRuleContexts(ImportPreContext.class);
		}
		public ImportPreContext importPre(int i) {
			return getRuleContext(ImportPreContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public ImportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclContext importDecl() throws RecognitionException {
		ImportDeclContext _localctx = new ImportDeclContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT_PRE) {
				{
				{
				setState(442);
				importPre();
				setState(443);
				eos();
				}
				}
				setState(449);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(450);
				match(IMPORT);
				setState(451);
				importSpec();
				}
				break;
			case 2:
				{
				setState(452);
				match(IMPORT);
				setState(453);
				match(L_PAREN);
				setState(459);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (IMPORT_PRE - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (DOT - 71)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
					{
					{
					setState(454);
					importSpec();
					setState(455);
					eos();
					}
					}
					setState(461);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(462);
				match(R_PAREN);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GhostMemberContext extends ParserRuleContext {
		public ImplementationProofContext implementationProof() {
			return getRuleContext(ImplementationProofContext.class,0);
		}
		public FpredicateDeclContext fpredicateDecl() {
			return getRuleContext(FpredicateDeclContext.class,0);
		}
		public MpredicateDeclContext mpredicateDecl() {
			return getRuleContext(MpredicateDeclContext.class,0);
		}
		public ExplicitGhostMemberContext explicitGhostMember() {
			return getRuleContext(ExplicitGhostMemberContext.class,0);
		}
		public GhostMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostMember; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostMemberContext ghostMember() throws RecognitionException {
		GhostMemberContext _localctx = new GhostMemberContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_ghostMember);
		try {
			setState(469);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(465);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(466);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(467);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(468);
				explicitGhostMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GhostStatementContext extends ParserRuleContext {
		public GhostStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostStatement; }
	 
		public GhostStatementContext() { }
		public void copyFrom(GhostStatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ProofStatementContext extends GhostStatementContext {
		public Token kind;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ASSUME() { return getToken(GobraParser.ASSUME, 0); }
		public TerminalNode ASSERT() { return getToken(GobraParser.ASSERT, 0); }
		public TerminalNode INHALE() { return getToken(GobraParser.INHALE, 0); }
		public TerminalNode EXHALE() { return getToken(GobraParser.EXHALE, 0); }
		public ProofStatementContext(GhostStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitProofStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MatchStmt_Context extends GhostStatementContext {
		public MatchStmtContext matchStmt() {
			return getRuleContext(MatchStmtContext.class,0);
		}
		public MatchStmt_Context(GhostStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchStmt_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExplicitGhostStatementContext extends GhostStatementContext {
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExplicitGhostStatementContext(GhostStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExplicitGhostStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FoldStatementContext extends GhostStatementContext {
		public Token fold_stmt;
		public PredicateAccessContext predicateAccess() {
			return getRuleContext(PredicateAccessContext.class,0);
		}
		public TerminalNode FOLD() { return getToken(GobraParser.FOLD, 0); }
		public TerminalNode UNFOLD() { return getToken(GobraParser.UNFOLD, 0); }
		public FoldStatementContext(GhostStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFoldStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostStatementContext ghostStatement() throws RecognitionException {
		GhostStatementContext _localctx = new GhostStatementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_ghostStatement);
		int _la;
		try {
			setState(478);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				_localctx = new ExplicitGhostStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(471);
				match(GHOST);
				setState(472);
				statement();
				}
				break;
			case FOLD:
			case UNFOLD:
				_localctx = new FoldStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(473);
				((FoldStatementContext)_localctx).fold_stmt = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==FOLD || _la==UNFOLD) ) {
					((FoldStatementContext)_localctx).fold_stmt = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(474);
				predicateAccess();
				}
				break;
			case ASSERT:
			case ASSUME:
			case INHALE:
			case EXHALE:
				_localctx = new ProofStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(475);
				((ProofStatementContext)_localctx).kind = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE))) != 0)) ) {
					((ProofStatementContext)_localctx).kind = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(476);
				expression(0);
				}
				break;
			case MATCH:
				_localctx = new MatchStmt_Context(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(477);
				matchStmt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AuxiliaryStatementContext extends ParserRuleContext {
		public StatementWithSpecContext statementWithSpec() {
			return getRuleContext(StatementWithSpecContext.class,0);
		}
		public AuxiliaryStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_auxiliaryStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAuxiliaryStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AuxiliaryStatementContext auxiliaryStatement() throws RecognitionException {
		AuxiliaryStatementContext _localctx = new AuxiliaryStatementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_auxiliaryStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			statementWithSpec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementWithSpecContext extends ParserRuleContext {
		public SpecificationContext specification;
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public OutlineStatementContext outlineStatement() {
			return getRuleContext(OutlineStatementContext.class,0);
		}
		public StatementWithSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementWithSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitStatementWithSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementWithSpecContext statementWithSpec() throws RecognitionException {
		StatementWithSpecContext _localctx = new StatementWithSpecContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_statementWithSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			((StatementWithSpecContext)_localctx).specification = specification();
			{
			setState(483);
			outlineStatement(((StatementWithSpecContext)_localctx).specification.trusted, ((StatementWithSpecContext)_localctx).specification.pure);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutlineStatementContext extends ParserRuleContext {
		public boolean trusted;
		public boolean pure;
		public TerminalNode OUTLINE() { return getToken(GobraParser.OUTLINE, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public OutlineStatementContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public OutlineStatementContext(ParserRuleContext parent, int invokingState, boolean trusted, boolean pure) {
			super(parent, invokingState);
			this.trusted = trusted;
			this.pure = pure;
		}
		@Override public int getRuleIndex() { return RULE_outlineStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOutlineStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OutlineStatementContext outlineStatement(boolean trusted,boolean pure) throws RecognitionException {
		OutlineStatementContext _localctx = new OutlineStatementContext(_ctx, getState(), trusted, pure);
		enterRule(_localctx, 28, RULE_outlineStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			match(OUTLINE);
			setState(486);
			match(L_PAREN);
			setState(488);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(487);
				statementList();
				}
				break;
			}
			setState(490);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GhostPrimaryExprContext extends ParserRuleContext {
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public AccessContext access() {
			return getRuleContext(AccessContext.class,0);
		}
		public TypeOfContext typeOf() {
			return getRuleContext(TypeOfContext.class,0);
		}
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public IsComparableContext isComparable() {
			return getRuleContext(IsComparableContext.class,0);
		}
		public LowContext low() {
			return getRuleContext(LowContext.class,0);
		}
		public OldContext old() {
			return getRuleContext(OldContext.class,0);
		}
		public BeforeContext before() {
			return getRuleContext(BeforeContext.class,0);
		}
		public SConversionContext sConversion() {
			return getRuleContext(SConversionContext.class,0);
		}
		public OptionNoneContext optionNone() {
			return getRuleContext(OptionNoneContext.class,0);
		}
		public OptionSomeContext optionSome() {
			return getRuleContext(OptionSomeContext.class,0);
		}
		public OptionGetContext optionGet() {
			return getRuleContext(OptionGetContext.class,0);
		}
		public PermissionContext permission() {
			return getRuleContext(PermissionContext.class,0);
		}
		public MatchExprContext matchExpr() {
			return getRuleContext(MatchExprContext.class,0);
		}
		public GhostPrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostPrimaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostPrimaryExprContext ghostPrimaryExpr() throws RecognitionException {
		GhostPrimaryExprContext _localctx = new GhostPrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ghostPrimaryExpr);
		try {
			setState(506);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(492);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(493);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(494);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(495);
				typeExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(496);
				isComparable();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(497);
				low();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(498);
				old();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(499);
				before();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(500);
				sConversion();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(501);
				optionNone();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(502);
				optionSome();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(503);
				optionGet();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(504);
				permission();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(505);
				matchExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PermissionContext extends ParserRuleContext {
		public TerminalNode WRITEPERM() { return getToken(GobraParser.WRITEPERM, 0); }
		public TerminalNode NOPERM() { return getToken(GobraParser.NOPERM, 0); }
		public PermissionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_permission; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPermission(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PermissionContext permission() throws RecognitionException {
		PermissionContext _localctx = new PermissionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_permission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(508);
			_la = _input.LA(1);
			if ( !(_la==WRITEPERM || _la==NOPERM) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeExprContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(GobraParser.TYPE, 0); }
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public TypeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeExprContext typeExpr() throws RecognitionException {
		TypeExprContext _localctx = new TypeExprContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_typeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(TYPE);
			setState(511);
			match(L_BRACKET);
			setState(512);
			type_();
			setState(513);
			match(R_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoundVariablesContext extends ParserRuleContext {
		public List<BoundVariableDeclContext> boundVariableDecl() {
			return getRuleContexts(BoundVariableDeclContext.class);
		}
		public BoundVariableDeclContext boundVariableDecl(int i) {
			return getRuleContext(BoundVariableDeclContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public BoundVariablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boundVariables; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBoundVariables(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoundVariablesContext boundVariables() throws RecognitionException {
		BoundVariablesContext _localctx = new BoundVariablesContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			boundVariableDecl();
			setState(520);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(516);
					match(COMMA);
					setState(517);
					boundVariableDecl();
					}
					} 
				}
				setState(522);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(523);
				match(COMMA);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoundVariableDeclContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(GobraParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(GobraParser.IDENTIFIER, i);
		}
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public BoundVariableDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boundVariableDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBoundVariableDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoundVariableDeclContext boundVariableDecl() throws RecognitionException {
		BoundVariableDeclContext _localctx = new BoundVariableDeclContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			match(IDENTIFIER);
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(527);
				match(COMMA);
				setState(528);
				match(IDENTIFIER);
				}
				}
				setState(533);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(534);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggersContext extends ParserRuleContext {
		public List<TriggerContext> trigger() {
			return getRuleContexts(TriggerContext.class);
		}
		public TriggerContext trigger(int i) {
			return getRuleContext(TriggerContext.class,i);
		}
		public TriggersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggers; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTriggers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TriggersContext triggers() throws RecognitionException {
		TriggersContext _localctx = new TriggersContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(539);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(536);
				trigger();
				}
				}
				setState(541);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerContext extends ParserRuleContext {
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public TriggerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trigger; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTrigger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TriggerContext trigger() throws RecognitionException {
		TriggerContext _localctx = new TriggerContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(542);
			match(L_CURLY);
			setState(543);
			expression(0);
			setState(548);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(544);
				match(COMMA);
				setState(545);
				expression(0);
				}
				}
				setState(550);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(551);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateAccessContext extends ParserRuleContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public PredicateAccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateAccess; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredicateAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateAccessContext predicateAccess() throws RecognitionException {
		PredicateAccessContext _localctx = new PredicateAccessContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			primaryExpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OptionSomeContext extends ParserRuleContext {
		public TerminalNode SOME() { return getToken(GobraParser.SOME, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public OptionSomeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optionSome; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOptionSome(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptionSomeContext optionSome() throws RecognitionException {
		OptionSomeContext _localctx = new OptionSomeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			match(SOME);
			setState(556);
			match(L_PAREN);
			setState(557);
			expression(0);
			setState(558);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OptionNoneContext extends ParserRuleContext {
		public TerminalNode NONE() { return getToken(GobraParser.NONE, 0); }
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public OptionNoneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optionNone; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOptionNone(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptionNoneContext optionNone() throws RecognitionException {
		OptionNoneContext _localctx = new OptionNoneContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(560);
			match(NONE);
			setState(561);
			match(L_BRACKET);
			setState(562);
			type_();
			setState(563);
			match(R_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OptionGetContext extends ParserRuleContext {
		public TerminalNode GET() { return getToken(GobraParser.GET, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public OptionGetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optionGet; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOptionGet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptionGetContext optionGet() throws RecognitionException {
		OptionGetContext _localctx = new OptionGetContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			match(GET);
			setState(566);
			match(L_PAREN);
			setState(567);
			expression(0);
			setState(568);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SConversionContext extends ParserRuleContext {
		public Token kind;
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode SET() { return getToken(GobraParser.SET, 0); }
		public TerminalNode SEQ() { return getToken(GobraParser.SEQ, 0); }
		public TerminalNode MSET() { return getToken(GobraParser.MSET, 0); }
		public SConversionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sConversion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSConversion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SConversionContext sConversion() throws RecognitionException {
		SConversionContext _localctx = new SConversionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			((SConversionContext)_localctx).kind = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SEQ) | (1L << SET) | (1L << MSET))) != 0)) ) {
				((SConversionContext)_localctx).kind = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(571);
			match(L_PAREN);
			setState(572);
			expression(0);
			setState(573);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OldContext extends ParserRuleContext {
		public TerminalNode OLD() { return getToken(GobraParser.OLD, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public OldLabelUseContext oldLabelUse() {
			return getRuleContext(OldLabelUseContext.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public OldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_old; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOld(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OldContext old() throws RecognitionException {
		OldContext _localctx = new OldContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575);
			match(OLD);
			setState(580);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(576);
				match(L_BRACKET);
				setState(577);
				oldLabelUse();
				setState(578);
				match(R_BRACKET);
				}
			}

			setState(582);
			match(L_PAREN);
			setState(583);
			expression(0);
			setState(584);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OldLabelUseContext extends ParserRuleContext {
		public LabelUseContext labelUse() {
			return getRuleContext(LabelUseContext.class,0);
		}
		public TerminalNode LHS() { return getToken(GobraParser.LHS, 0); }
		public OldLabelUseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oldLabelUse; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOldLabelUse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OldLabelUseContext oldLabelUse() throws RecognitionException {
		OldLabelUseContext _localctx = new OldLabelUseContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_oldLabelUse);
		try {
			setState(588);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(586);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(587);
				match(LHS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LabelUseContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public LabelUseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labelUse; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLabelUse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelUseContext labelUse() throws RecognitionException {
		LabelUseContext _localctx = new LabelUseContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BeforeContext extends ParserRuleContext {
		public TerminalNode BEFORE() { return getToken(GobraParser.BEFORE, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public BeforeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_before; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBefore(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BeforeContext before() throws RecognitionException {
		BeforeContext _localctx = new BeforeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_before);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(592);
			match(BEFORE);
			setState(593);
			match(L_PAREN);
			setState(594);
			expression(0);
			setState(595);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsComparableContext extends ParserRuleContext {
		public TerminalNode IS_COMPARABLE() { return getToken(GobraParser.IS_COMPARABLE, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public IsComparableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isComparable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitIsComparable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsComparableContext isComparable() throws RecognitionException {
		IsComparableContext _localctx = new IsComparableContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(597);
			match(IS_COMPARABLE);
			setState(598);
			match(L_PAREN);
			setState(599);
			expression(0);
			setState(600);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LowContext extends ParserRuleContext {
		public TerminalNode LOW() { return getToken(GobraParser.LOW, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public LowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_low; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LowContext low() throws RecognitionException {
		LowContext _localctx = new LowContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(602);
			match(LOW);
			setState(603);
			match(L_PAREN);
			setState(604);
			expression(0);
			setState(605);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeOfContext extends ParserRuleContext {
		public TerminalNode TYPE_OF() { return getToken(GobraParser.TYPE_OF, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TypeOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeOf; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeOfContext typeOf() throws RecognitionException {
		TypeOfContext _localctx = new TypeOfContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(607);
			match(TYPE_OF);
			setState(608);
			match(L_PAREN);
			setState(609);
			expression(0);
			setState(610);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AccessContext extends ParserRuleContext {
		public TerminalNode ACCESS() { return getToken(GobraParser.ACCESS, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public AccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_access; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessContext access() throws RecognitionException {
		AccessContext _localctx = new AccessContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			match(ACCESS);
			setState(613);
			match(L_PAREN);
			setState(614);
			expression(0);
			setState(617);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(615);
				match(COMMA);
				setState(616);
				expression(0);
				}
			}

			setState(619);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeContext extends ParserRuleContext {
		public Token kind;
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode DOT_DOT() { return getToken(GobraParser.DOT_DOT, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public TerminalNode SEQ() { return getToken(GobraParser.SEQ, 0); }
		public TerminalNode SET() { return getToken(GobraParser.SET, 0); }
		public TerminalNode MSET() { return getToken(GobraParser.MSET, 0); }
		public RangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeContext range() throws RecognitionException {
		RangeContext _localctx = new RangeContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(621);
			((RangeContext)_localctx).kind = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SEQ) | (1L << SET) | (1L << MSET))) != 0)) ) {
				((RangeContext)_localctx).kind = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(622);
			match(L_BRACKET);
			setState(623);
			expression(0);
			setState(624);
			match(DOT_DOT);
			setState(625);
			expression(0);
			setState(626);
			match(R_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchExprContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(GobraParser.MATCH, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<MatchExprClauseContext> matchExprClause() {
			return getRuleContexts(MatchExprClauseContext.class);
		}
		public MatchExprClauseContext matchExprClause(int i) {
			return getRuleContext(MatchExprClauseContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public MatchExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchExprContext matchExpr() throws RecognitionException {
		MatchExprContext _localctx = new MatchExprContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_matchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			match(MATCH);
			setState(629);
			expression(0);
			setState(630);
			match(L_CURLY);
			setState(636);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(631);
				matchExprClause();
				setState(632);
				eos();
				}
				}
				setState(638);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(639);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchExprClauseContext extends ParserRuleContext {
		public MatchCaseContext matchCase() {
			return getRuleContext(MatchCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public MatchExprClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchExprClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchExprClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchExprClauseContext matchExprClause() throws RecognitionException {
		MatchExprClauseContext _localctx = new MatchExprClauseContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_matchExprClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(641);
			matchCase();
			setState(642);
			match(COLON);
			setState(643);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SeqUpdExpContext extends ParserRuleContext {
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public List<SeqUpdClauseContext> seqUpdClause() {
			return getRuleContexts(SeqUpdClauseContext.class);
		}
		public SeqUpdClauseContext seqUpdClause(int i) {
			return getRuleContext(SeqUpdClauseContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public SeqUpdExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seqUpdExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSeqUpdExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeqUpdExpContext seqUpdExp() throws RecognitionException {
		SeqUpdExpContext _localctx = new SeqUpdExpContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			match(L_BRACKET);
			{
			setState(646);
			seqUpdClause();
			setState(651);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(647);
				match(COMMA);
				setState(648);
				seqUpdClause();
				}
				}
				setState(653);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(654);
			match(R_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SeqUpdClauseContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public SeqUpdClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seqUpdClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSeqUpdClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeqUpdClauseContext seqUpdClause() throws RecognitionException {
		SeqUpdClauseContext _localctx = new SeqUpdClauseContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(656);
			expression(0);
			setState(657);
			match(ASSIGN);
			setState(658);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GhostTypeLitContext extends ParserRuleContext {
		public SqTypeContext sqType() {
			return getRuleContext(SqTypeContext.class,0);
		}
		public GhostSliceTypeContext ghostSliceType() {
			return getRuleContext(GhostSliceTypeContext.class,0);
		}
		public DomainTypeContext domainType() {
			return getRuleContext(DomainTypeContext.class,0);
		}
		public AdtTypeContext adtType() {
			return getRuleContext(AdtTypeContext.class,0);
		}
		public GhostTypeLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostTypeLit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostTypeLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostTypeLitContext ghostTypeLit() throws RecognitionException {
		GhostTypeLitContext _localctx = new GhostTypeLitContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_ghostTypeLit);
		try {
			setState(664);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(660);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(661);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(662);
				domainType();
				}
				break;
			case ADT:
				enterOuterAlt(_localctx, 4);
				{
				setState(663);
				adtType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DomainTypeContext extends ParserRuleContext {
		public TerminalNode DOM() { return getToken(GobraParser.DOM, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<DomainClauseContext> domainClause() {
			return getRuleContexts(DomainClauseContext.class);
		}
		public DomainClauseContext domainClause(int i) {
			return getRuleContext(DomainClauseContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public DomainTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_domainType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitDomainType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DomainTypeContext domainType() throws RecognitionException {
		DomainTypeContext _localctx = new DomainTypeContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
			match(DOM);
			setState(667);
			match(L_CURLY);
			setState(673);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(668);
				domainClause();
				setState(669);
				eos();
				}
				}
				setState(675);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(676);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DomainClauseContext extends ParserRuleContext {
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public TerminalNode AXIOM() { return getToken(GobraParser.AXIOM, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public DomainClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_domainClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitDomainClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DomainClauseContext domainClause() throws RecognitionException {
		DomainClauseContext _localctx = new DomainClauseContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_domainClause);
		try {
			setState(687);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(678);
				match(FUNC);
				setState(679);
				match(IDENTIFIER);
				setState(680);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(681);
				match(AXIOM);
				setState(682);
				match(L_CURLY);
				setState(683);
				expression(0);
				setState(684);
				eos();
				setState(685);
				match(R_CURLY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdtTypeContext extends ParserRuleContext {
		public TerminalNode ADT() { return getToken(GobraParser.ADT, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<AdtClauseContext> adtClause() {
			return getRuleContexts(AdtClauseContext.class);
		}
		public AdtClauseContext adtClause(int i) {
			return getRuleContext(AdtClauseContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public AdtTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adtType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAdtType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdtTypeContext adtType() throws RecognitionException {
		AdtTypeContext _localctx = new AdtTypeContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_adtType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(689);
			match(ADT);
			setState(690);
			match(L_CURLY);
			setState(696);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(691);
				adtClause();
				setState(692);
				eos();
				}
				}
				setState(698);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(699);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdtClauseContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<FieldDeclContext> fieldDecl() {
			return getRuleContexts(FieldDeclContext.class);
		}
		public FieldDeclContext fieldDecl(int i) {
			return getRuleContext(FieldDeclContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public AdtClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adtClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAdtClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdtClauseContext adtClause() throws RecognitionException {
		AdtClauseContext _localctx = new AdtClauseContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_adtClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			match(IDENTIFIER);
			setState(702);
			match(L_CURLY);
			setState(708);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(703);
				fieldDecl();
				setState(704);
				eos();
				}
				}
				setState(710);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(711);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GhostSliceTypeContext extends ParserRuleContext {
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public GhostSliceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostSliceType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostSliceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostSliceTypeContext ghostSliceType() throws RecognitionException {
		GhostSliceTypeContext _localctx = new GhostSliceTypeContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(713);
			match(GHOST);
			setState(714);
			match(L_BRACKET);
			setState(715);
			match(R_BRACKET);
			setState(716);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SqTypeContext extends ParserRuleContext {
		public Token kind;
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public List<Type_Context> type_() {
			return getRuleContexts(Type_Context.class);
		}
		public Type_Context type_(int i) {
			return getRuleContext(Type_Context.class,i);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public TerminalNode SEQ() { return getToken(GobraParser.SEQ, 0); }
		public TerminalNode SET() { return getToken(GobraParser.SET, 0); }
		public TerminalNode MSET() { return getToken(GobraParser.MSET, 0); }
		public TerminalNode OPT() { return getToken(GobraParser.OPT, 0); }
		public TerminalNode DICT() { return getToken(GobraParser.DICT, 0); }
		public SqTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSqType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqTypeContext sqType() throws RecognitionException {
		SqTypeContext _localctx = new SqTypeContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_sqType);
		int _la;
		try {
			setState(729);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(718);
				((SqTypeContext)_localctx).kind = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << OPT))) != 0)) ) {
					((SqTypeContext)_localctx).kind = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(719);
				match(L_BRACKET);
				setState(720);
				type_();
				setState(721);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(723);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(724);
				match(L_BRACKET);
				setState(725);
				type_();
				setState(726);
				match(R_BRACKET);
				setState(727);
				type_();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpecificationContext extends ParserRuleContext {
		public boolean trusted = false;
		public boolean pure = false;;
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public List<TerminalNode> PURE() { return getTokens(GobraParser.PURE); }
		public TerminalNode PURE(int i) {
			return getToken(GobraParser.PURE, i);
		}
		public List<SpecStatementContext> specStatement() {
			return getRuleContexts(SpecStatementContext.class);
		}
		public SpecStatementContext specStatement(int i) {
			return getRuleContext(SpecStatementContext.class,i);
		}
		public List<TerminalNode> TRUSTED() { return getTokens(GobraParser.TRUSTED); }
		public TerminalNode TRUSTED(int i) {
			return getToken(GobraParser.TRUSTED, i);
		}
		public SpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specification; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecificationContext specification() throws RecognitionException {
		SpecificationContext _localctx = new SpecificationContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_specification);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(741);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(736);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(731);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(732);
						match(PURE);
						((SpecificationContext)_localctx).pure =  true;
						}
						break;
					case TRUSTED:
						{
						setState(734);
						match(TRUSTED);
						((SpecificationContext)_localctx).trusted =  true;
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(738);
					eos();
					}
					} 
				}
				setState(743);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(746);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(744);
				match(PURE);
				((SpecificationContext)_localctx).pure =  true;
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpecStatementContext extends ParserRuleContext {
		public Token kind;
		public AssertionContext assertion() {
			return getRuleContext(AssertionContext.class,0);
		}
		public TerminalNode PRE() { return getToken(GobraParser.PRE, 0); }
		public TerminalNode PRESERVES() { return getToken(GobraParser.PRESERVES, 0); }
		public TerminalNode POST() { return getToken(GobraParser.POST, 0); }
		public TerminationMeasureContext terminationMeasure() {
			return getRuleContext(TerminationMeasureContext.class,0);
		}
		public TerminalNode DEC() { return getToken(GobraParser.DEC, 0); }
		public SpecStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSpecStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecStatementContext specStatement() throws RecognitionException {
		SpecStatementContext _localctx = new SpecStatementContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_specStatement);
		try {
			setState(756);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(748);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(749);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(750);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(751);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(752);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(753);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(754);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(755);
				terminationMeasure();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TerminationMeasureContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode IF() { return getToken(GobraParser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminationMeasureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terminationMeasure; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTerminationMeasure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TerminationMeasureContext terminationMeasure() throws RecognitionException {
		TerminationMeasureContext _localctx = new TerminationMeasureContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(759);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(758);
				expressionList();
				}
				break;
			}
			setState(763);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(761);
				match(IF);
				setState(762);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssertionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssertionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assertion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAssertion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssertionContext assertion() throws RecognitionException {
		AssertionContext _localctx = new AssertionContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_assertion);
		try {
			setState(767);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(766);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchStmtContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(GobraParser.MATCH, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<MatchStmtClauseContext> matchStmtClause() {
			return getRuleContexts(MatchStmtClauseContext.class);
		}
		public MatchStmtClauseContext matchStmtClause(int i) {
			return getRuleContext(MatchStmtClauseContext.class,i);
		}
		public MatchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchStmtContext matchStmt() throws RecognitionException {
		MatchStmtContext _localctx = new MatchStmtContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_matchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			match(MATCH);
			setState(770);
			expression(0);
			setState(771);
			match(L_CURLY);
			setState(775);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(772);
				matchStmtClause();
				}
				}
				setState(777);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(778);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchStmtClauseContext extends ParserRuleContext {
		public MatchCaseContext matchCase() {
			return getRuleContext(MatchCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public MatchStmtClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStmtClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchStmtClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchStmtClauseContext matchStmtClause() throws RecognitionException {
		MatchStmtClauseContext _localctx = new MatchStmtClauseContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_matchStmtClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(780);
			matchCase();
			setState(781);
			match(COLON);
			setState(783);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(782);
				statementList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchCaseContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(GobraParser.CASE, 0); }
		public MatchPatternContext matchPattern() {
			return getRuleContext(MatchPatternContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(GobraParser.DEFAULT, 0); }
		public MatchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchCase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchCaseContext matchCase() throws RecognitionException {
		MatchCaseContext _localctx = new MatchCaseContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_matchCase);
		try {
			setState(788);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(785);
				match(CASE);
				setState(786);
				matchPattern();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(787);
				match(DEFAULT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchPatternContext extends ParserRuleContext {
		public MatchPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchPattern; }
	 
		public MatchPatternContext() { }
		public void copyFrom(MatchPatternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MatchPatternValueContext extends MatchPatternContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public MatchPatternValueContext(MatchPatternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MatchPatternCompositeContext extends MatchPatternContext {
		public LiteralTypeContext literalType() {
			return getRuleContext(LiteralTypeContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public MatchPatternListContext matchPatternList() {
			return getRuleContext(MatchPatternListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public MatchPatternCompositeContext(MatchPatternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternComposite(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MatchPatternBindContext extends MatchPatternContext {
		public TerminalNode QMARK() { return getToken(GobraParser.QMARK, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public MatchPatternBindContext(MatchPatternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternBind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchPatternContext matchPattern() throws RecognitionException {
		MatchPatternContext _localctx = new MatchPatternContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_matchPattern);
		int _la;
		try {
			setState(803);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				_localctx = new MatchPatternBindContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(790);
				match(QMARK);
				setState(791);
				match(IDENTIFIER);
				}
				break;
			case 2:
				_localctx = new MatchPatternCompositeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(792);
				literalType();
				setState(793);
				match(L_CURLY);
				setState(798);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << QMARK) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(794);
					matchPatternList();
					setState(796);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(795);
						match(COMMA);
						}
					}

					}
				}

				setState(800);
				match(R_CURLY);
				}
				break;
			case 3:
				_localctx = new MatchPatternValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(802);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchPatternListContext extends ParserRuleContext {
		public List<MatchPatternContext> matchPattern() {
			return getRuleContexts(MatchPatternContext.class);
		}
		public MatchPatternContext matchPattern(int i) {
			return getRuleContext(MatchPatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public MatchPatternListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchPatternList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchPatternListContext matchPatternList() throws RecognitionException {
		MatchPatternListContext _localctx = new MatchPatternListContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_matchPatternList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(805);
			matchPattern();
			setState(810);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(806);
					match(COMMA);
					setState(807);
					matchPattern();
					}
					} 
				}
				setState(812);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockWithBodyParameterInfoContext extends ParserRuleContext {
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public TerminalNode SHARE() { return getToken(GobraParser.SHARE, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public BlockWithBodyParameterInfoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockWithBodyParameterInfo; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBlockWithBodyParameterInfo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() throws RecognitionException {
		BlockWithBodyParameterInfoContext _localctx = new BlockWithBodyParameterInfoContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_blockWithBodyParameterInfo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(813);
			match(L_CURLY);
			setState(818);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(814);
				match(SHARE);
				setState(815);
				identifierList();
				setState(816);
				eos();
				}
				break;
			}
			setState(821);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(820);
				statementList();
				}
				break;
			}
			setState(823);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClosureSpecInstanceContext extends ParserRuleContext {
		public QualifiedIdentContext qualifiedIdent() {
			return getRuleContext(QualifiedIdentContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public ClosureSpecParamsContext closureSpecParams() {
			return getRuleContext(ClosureSpecParamsContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public ClosureSpecInstanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closureSpecInstance; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureSpecInstance(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosureSpecInstanceContext closureSpecInstance() throws RecognitionException {
		ClosureSpecInstanceContext _localctx = new ClosureSpecInstanceContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_closureSpecInstance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(827);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(825);
				qualifiedIdent();
				}
				break;
			case 2:
				{
				setState(826);
				match(IDENTIFIER);
				}
				break;
			}
			setState(837);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(829);
				match(L_CURLY);
				setState(834);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(830);
					closureSpecParams();
					setState(832);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(831);
						match(COMMA);
						}
					}

					}
				}

				setState(836);
				match(R_CURLY);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClosureSpecParamsContext extends ParserRuleContext {
		public List<ClosureSpecParamContext> closureSpecParam() {
			return getRuleContexts(ClosureSpecParamContext.class);
		}
		public ClosureSpecParamContext closureSpecParam(int i) {
			return getRuleContext(ClosureSpecParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public ClosureSpecParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closureSpecParams; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureSpecParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosureSpecParamsContext closureSpecParams() throws RecognitionException {
		ClosureSpecParamsContext _localctx = new ClosureSpecParamsContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_closureSpecParams);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(839);
			closureSpecParam();
			setState(844);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(840);
					match(COMMA);
					setState(841);
					closureSpecParam();
					}
					} 
				}
				setState(846);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClosureSpecParamContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public ClosureSpecParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closureSpecParam; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureSpecParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosureSpecParamContext closureSpecParam() throws RecognitionException {
		ClosureSpecParamContext _localctx = new ClosureSpecParamContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_closureSpecParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(849);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(847);
				match(IDENTIFIER);
				setState(848);
				match(COLON);
				}
				break;
			}
			setState(851);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClosureImplProofStmtContext extends ParserRuleContext {
		public TerminalNode PROOF() { return getToken(GobraParser.PROOF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IMPL() { return getToken(GobraParser.IMPL, 0); }
		public ClosureSpecInstanceContext closureSpecInstance() {
			return getRuleContext(ClosureSpecInstanceContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ClosureImplProofStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closureImplProofStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureImplProofStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosureImplProofStmtContext closureImplProofStmt() throws RecognitionException {
		ClosureImplProofStmtContext _localctx = new ClosureImplProofStmtContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_closureImplProofStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			match(PROOF);
			setState(854);
			expression(0);
			setState(855);
			match(IMPL);
			setState(856);
			closureSpecInstance();
			setState(857);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplementationProofContext extends ParserRuleContext {
		public List<Type_Context> type_() {
			return getRuleContexts(Type_Context.class);
		}
		public Type_Context type_(int i) {
			return getRuleContext(Type_Context.class,i);
		}
		public TerminalNode IMPL() { return getToken(GobraParser.IMPL, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<ImplementationProofPredicateAliasContext> implementationProofPredicateAlias() {
			return getRuleContexts(ImplementationProofPredicateAliasContext.class);
		}
		public ImplementationProofPredicateAliasContext implementationProofPredicateAlias(int i) {
			return getRuleContext(ImplementationProofPredicateAliasContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public List<MethodImplementationProofContext> methodImplementationProof() {
			return getRuleContexts(MethodImplementationProofContext.class);
		}
		public MethodImplementationProofContext methodImplementationProof(int i) {
			return getRuleContext(MethodImplementationProofContext.class,i);
		}
		public ImplementationProofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implementationProof; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImplementationProof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImplementationProofContext implementationProof() throws RecognitionException {
		ImplementationProofContext _localctx = new ImplementationProofContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(859);
			type_();
			setState(860);
			match(IMPL);
			setState(861);
			type_();
			setState(880);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(862);
				match(L_CURLY);
				setState(868);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(863);
					implementationProofPredicateAlias();
					setState(864);
					eos();
					}
					}
					setState(870);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(876);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(871);
					methodImplementationProof();
					setState(872);
					eos();
					}
					}
					setState(878);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(879);
				match(R_CURLY);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodImplementationProofContext extends ParserRuleContext {
		public NonLocalReceiverContext nonLocalReceiver() {
			return getRuleContext(NonLocalReceiverContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public TerminalNode PURE() { return getToken(GobraParser.PURE, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MethodImplementationProofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodImplementationProof; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMethodImplementationProof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodImplementationProofContext methodImplementationProof() throws RecognitionException {
		MethodImplementationProofContext _localctx = new MethodImplementationProofContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(883);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(882);
				match(PURE);
				}
			}

			setState(885);
			nonLocalReceiver();
			setState(886);
			match(IDENTIFIER);
			setState(887);
			signature();
			setState(889);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(888);
				block();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonLocalReceiverContext extends ParserRuleContext {
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public NonLocalReceiverContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonLocalReceiver; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitNonLocalReceiver(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonLocalReceiverContext nonLocalReceiver() throws RecognitionException {
		NonLocalReceiverContext _localctx = new NonLocalReceiverContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(891);
			match(L_PAREN);
			setState(893);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(892);
				match(IDENTIFIER);
				}
				break;
			}
			setState(896);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(895);
				match(STAR);
				}
			}

			setState(898);
			typeName();
			setState(899);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionContext extends ParserRuleContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSelection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionContext selection() throws RecognitionException {
		SelectionContext _localctx = new SelectionContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_selection);
		try {
			setState(906);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(901);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(902);
				type_();
				setState(903);
				match(DOT);
				setState(904);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplementationProofPredicateAliasContext extends ParserRuleContext {
		public TerminalNode PRED() { return getToken(GobraParser.PRED, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode DECLARE_ASSIGN() { return getToken(GobraParser.DECLARE_ASSIGN, 0); }
		public SelectionContext selection() {
			return getRuleContext(SelectionContext.class,0);
		}
		public OperandNameContext operandName() {
			return getRuleContext(OperandNameContext.class,0);
		}
		public ImplementationProofPredicateAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implementationProofPredicateAlias; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImplementationProofPredicateAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImplementationProofPredicateAliasContext implementationProofPredicateAlias() throws RecognitionException {
		ImplementationProofPredicateAliasContext _localctx = new ImplementationProofPredicateAliasContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(908);
			match(PRED);
			setState(909);
			match(IDENTIFIER);
			setState(910);
			match(DECLARE_ASSIGN);
			setState(913);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(911);
				selection();
				}
				break;
			case 2:
				{
				setState(912);
				operandName();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeContext extends ParserRuleContext {
		public TerminalNode MAKE() { return getToken(GobraParser.MAKE, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public MakeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_make; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMake(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MakeContext make() throws RecognitionException {
		MakeContext _localctx = new MakeContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			match(MAKE);
			setState(916);
			match(L_PAREN);
			setState(917);
			type_();
			setState(920);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(918);
				match(COMMA);
				setState(919);
				expressionList();
				}
			}

			setState(922);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class New_Context extends ParserRuleContext {
		public TerminalNode NEW() { return getToken(GobraParser.NEW, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public New_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_new_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitNew_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final New_Context new_() throws RecognitionException {
		New_Context _localctx = new New_Context(_ctx, getState());
		enterRule(_localctx, 134, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(924);
			match(NEW);
			setState(925);
			match(L_PAREN);
			setState(926);
			type_();
			setState(927);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpecMemberContext extends ParserRuleContext {
		public SpecificationContext specification;
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public MethodDeclContext methodDecl() {
			return getRuleContext(MethodDeclContext.class,0);
		}
		public SpecMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specMember; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSpecMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecMemberContext specMember() throws RecognitionException {
		SpecMemberContext _localctx = new SpecMemberContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_specMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			((SpecMemberContext)_localctx).specification = specification();
			setState(932);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(930);
				functionDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 2:
				{
				setState(931);
				methodDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionDeclContext extends ParserRuleContext {
		public boolean trusted;
		public boolean pure;
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public FunctionDeclContext(ParserRuleContext parent, int invokingState, boolean trusted, boolean pure) {
			super(parent, invokingState);
			this.trusted = trusted;
			this.pure = pure;
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl(boolean trusted,boolean pure) throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState(), trusted, pure);
		enterRule(_localctx, 138, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			match(FUNC);
			setState(935);
			match(IDENTIFIER);
			{
			setState(936);
			signature();
			setState(938);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(937);
				blockWithBodyParameterInfo();
				}
				break;
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodDeclContext extends ParserRuleContext {
		public boolean trusted;
		public boolean pure;
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public ReceiverContext receiver() {
			return getRuleContext(ReceiverContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
		}
		public MethodDeclContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public MethodDeclContext(ParserRuleContext parent, int invokingState, boolean trusted, boolean pure) {
			super(parent, invokingState);
			this.trusted = trusted;
			this.pure = pure;
		}
		@Override public int getRuleIndex() { return RULE_methodDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMethodDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodDeclContext methodDecl(boolean trusted,boolean pure) throws RecognitionException {
		MethodDeclContext _localctx = new MethodDeclContext(_ctx, getState(), trusted, pure);
		enterRule(_localctx, 140, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940);
			match(FUNC);
			setState(941);
			receiver();
			setState(942);
			match(IDENTIFIER);
			{
			setState(943);
			signature();
			setState(945);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(944);
				blockWithBodyParameterInfo();
				}
				break;
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGhostMemberContext extends ParserRuleContext {
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public SpecMemberContext specMember() {
			return getRuleContext(SpecMemberContext.class,0);
		}
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public ExplicitGhostMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGhostMember; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExplicitGhostMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplicitGhostMemberContext explicitGhostMember() throws RecognitionException {
		ExplicitGhostMemberContext _localctx = new ExplicitGhostMemberContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_explicitGhostMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(947);
			match(GHOST);
			setState(950);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
			case PRESERVES:
			case POST:
			case DEC:
			case PURE:
			case TRUSTED:
			case FUNC:
				{
				setState(948);
				specMember();
				}
				break;
			case CONST:
			case TYPE:
			case VAR:
				{
				setState(949);
				declaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FpredicateDeclContext extends ParserRuleContext {
		public TerminalNode PRED() { return getToken(GobraParser.PRED, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public PredicateBodyContext predicateBody() {
			return getRuleContext(PredicateBodyContext.class,0);
		}
		public FpredicateDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fpredicateDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFpredicateDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FpredicateDeclContext fpredicateDecl() throws RecognitionException {
		FpredicateDeclContext _localctx = new FpredicateDeclContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(952);
			match(PRED);
			setState(953);
			match(IDENTIFIER);
			setState(954);
			parameters();
			setState(956);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				{
				setState(955);
				predicateBody();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateBodyContext extends ParserRuleContext {
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public PredicateBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredicateBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateBodyContext predicateBody() throws RecognitionException {
		PredicateBodyContext _localctx = new PredicateBodyContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(958);
			match(L_CURLY);
			setState(959);
			expression(0);
			setState(960);
			eos();
			setState(961);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MpredicateDeclContext extends ParserRuleContext {
		public TerminalNode PRED() { return getToken(GobraParser.PRED, 0); }
		public ReceiverContext receiver() {
			return getRuleContext(ReceiverContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public PredicateBodyContext predicateBody() {
			return getRuleContext(PredicateBodyContext.class,0);
		}
		public MpredicateDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mpredicateDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMpredicateDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MpredicateDeclContext mpredicateDecl() throws RecognitionException {
		MpredicateDeclContext _localctx = new MpredicateDeclContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(963);
			match(PRED);
			setState(964);
			receiver();
			setState(965);
			match(IDENTIFIER);
			setState(966);
			parameters();
			setState(968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(967);
				predicateBody();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarSpecContext extends ParserRuleContext {
		public MaybeAddressableIdentifierListContext maybeAddressableIdentifierList() {
			return getRuleContext(MaybeAddressableIdentifierListContext.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public VarSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitVarSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarSpecContext varSpec() throws RecognitionException {
		VarSpecContext _localctx = new VarSpecContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
			maybeAddressableIdentifierList();
			setState(978);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
			case ADT:
			case PRED:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
			case IDENTIFIER:
			case L_PAREN:
			case L_BRACKET:
			case STAR:
			case RECEIVE:
				{
				setState(971);
				type_();
				setState(974);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
				case 1:
					{
					setState(972);
					match(ASSIGN);
					setState(973);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(976);
				match(ASSIGN);
				setState(977);
				expressionList();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ShortVarDeclContext extends ParserRuleContext {
		public MaybeAddressableIdentifierListContext maybeAddressableIdentifierList() {
			return getRuleContext(MaybeAddressableIdentifierListContext.class,0);
		}
		public TerminalNode DECLARE_ASSIGN() { return getToken(GobraParser.DECLARE_ASSIGN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ShortVarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shortVarDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitShortVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShortVarDeclContext shortVarDecl() throws RecognitionException {
		ShortVarDeclContext _localctx = new ShortVarDeclContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(980);
			maybeAddressableIdentifierList();
			setState(981);
			match(DECLARE_ASSIGN);
			setState(982);
			expressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReceiverContext extends ParserRuleContext {
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public MaybeAddressableIdentifierContext maybeAddressableIdentifier() {
			return getRuleContext(MaybeAddressableIdentifierContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public ReceiverContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_receiver; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitReceiver(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReceiverContext receiver() throws RecognitionException {
		ReceiverContext _localctx = new ReceiverContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(984);
			match(L_PAREN);
			setState(986);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(985);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(988);
			type_();
			setState(990);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(989);
				match(COMMA);
				}
			}

			setState(992);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterDeclContext extends ParserRuleContext {
		public ActualParameterDeclContext actualParameterDecl() {
			return getRuleContext(ActualParameterDeclContext.class,0);
		}
		public GhostParameterDeclContext ghostParameterDecl() {
			return getRuleContext(GhostParameterDeclContext.class,0);
		}
		public ParameterDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitParameterDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclContext parameterDecl() throws RecognitionException {
		ParameterDeclContext _localctx = new ParameterDeclContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_parameterDecl);
		try {
			setState(996);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				actualParameterDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(995);
				ghostParameterDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActualParameterDeclContext extends ParserRuleContext {
		public ParameterTypeContext parameterType() {
			return getRuleContext(ParameterTypeContext.class,0);
		}
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public ActualParameterDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actualParameterDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitActualParameterDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActualParameterDeclContext actualParameterDecl() throws RecognitionException {
		ActualParameterDeclContext _localctx = new ActualParameterDeclContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_actualParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(999);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(998);
				identifierList();
				}
				break;
			}
			setState(1001);
			parameterType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GhostParameterDeclContext extends ParserRuleContext {
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public ParameterTypeContext parameterType() {
			return getRuleContext(ParameterTypeContext.class,0);
		}
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GhostParameterDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostParameterDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostParameterDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostParameterDeclContext ghostParameterDecl() throws RecognitionException {
		GhostParameterDeclContext _localctx = new GhostParameterDeclContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_ghostParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1003);
			match(GHOST);
			setState(1005);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(1004);
				identifierList();
				}
				break;
			}
			setState(1007);
			parameterType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterTypeContext extends ParserRuleContext {
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode ELLIPSIS() { return getToken(GobraParser.ELLIPSIS, 0); }
		public ParameterTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitParameterType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterTypeContext parameterType() throws RecognitionException {
		ParameterTypeContext _localctx = new ParameterTypeContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_parameterType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(1009);
				match(ELLIPSIS);
				}
			}

			setState(1012);
			type_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ClosureImplSpecExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IMPL() { return getToken(GobraParser.IMPL, 0); }
		public ClosureSpecInstanceContext closureSpecInstance() {
			return getRuleContext(ClosureSpecInstanceContext.class,0);
		}
		public ClosureImplSpecExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureImplSpecExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrimaryExpr_Context extends ExpressionContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public PrimaryExpr_Context(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPrimaryExpr_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class QuantificationContext extends ExpressionContext {
		public BoundVariablesContext boundVariables() {
			return getRuleContext(BoundVariablesContext.class,0);
		}
		public List<TerminalNode> COLON() { return getTokens(GobraParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(GobraParser.COLON, i);
		}
		public TriggersContext triggers() {
			return getRuleContext(TriggersContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode FORALL() { return getToken(GobraParser.FORALL, 0); }
		public TerminalNode EXISTS() { return getToken(GobraParser.EXISTS, 0); }
		public QuantificationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitQuantification(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnfoldingContext extends ExpressionContext {
		public TerminalNode UNFOLDING() { return getToken(GobraParser.UNFOLDING, 0); }
		public PredicateAccessContext predicateAccess() {
			return getRuleContext(PredicateAccessContext.class,0);
		}
		public TerminalNode IN() { return getToken(GobraParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnfoldingContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitUnfolding(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LOGICAL_OR() { return getToken(GobraParser.LOGICAL_OR, 0); }
		public OrExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class P41ExprContext extends ExpressionContext {
		public Token p41_op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode IN() { return getToken(GobraParser.IN, 0); }
		public TerminalNode MULTI() { return getToken(GobraParser.MULTI, 0); }
		public TerminalNode SUBSET() { return getToken(GobraParser.SUBSET, 0); }
		public P41ExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitP41Expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryExprContext extends ExpressionContext {
		public Token unary_op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(GobraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(GobraParser.MINUS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(GobraParser.EXCLAMATION, 0); }
		public TerminalNode CARET() { return getToken(GobraParser.CARET, 0); }
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public TerminalNode RECEIVE() { return getToken(GobraParser.RECEIVE, 0); }
		public UnaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class P42ExprContext extends ExpressionContext {
		public Token p42_op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode UNION() { return getToken(GobraParser.UNION, 0); }
		public TerminalNode INTERSECTION() { return getToken(GobraParser.INTERSECTION, 0); }
		public TerminalNode SETMINUS() { return getToken(GobraParser.SETMINUS, 0); }
		public P42ExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitP42Expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TernaryExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode QMARK() { return getToken(GobraParser.QMARK, 0); }
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public TernaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTernaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddExprContext extends ExpressionContext {
		public Token add_op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(GobraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(GobraParser.MINUS, 0); }
		public TerminalNode OR() { return getToken(GobraParser.OR, 0); }
		public TerminalNode CARET() { return getToken(GobraParser.CARET, 0); }
		public TerminalNode PLUS_PLUS() { return getToken(GobraParser.PLUS_PLUS, 0); }
		public TerminalNode WAND() { return getToken(GobraParser.WAND, 0); }
		public AddExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAddExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ImplicationContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode IMPLIES() { return getToken(GobraParser.IMPLIES, 0); }
		public ImplicationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImplication(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MulExprContext extends ExpressionContext {
		public Token mul_op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public TerminalNode DIV() { return getToken(GobraParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(GobraParser.MOD, 0); }
		public TerminalNode LSHIFT() { return getToken(GobraParser.LSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(GobraParser.RSHIFT, 0); }
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public TerminalNode BIT_CLEAR() { return getToken(GobraParser.BIT_CLEAR, 0); }
		public MulExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMulExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LetContext extends ExpressionContext {
		public TerminalNode LET() { return getToken(GobraParser.LET, 0); }
		public ShortVarDeclContext shortVarDecl() {
			return getRuleContext(ShortVarDeclContext.class,0);
		}
		public TerminalNode IN() { return getToken(GobraParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LetContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RelExprContext extends ExpressionContext {
		public Token rel_op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQUALS() { return getToken(GobraParser.EQUALS, 0); }
		public TerminalNode NOT_EQUALS() { return getToken(GobraParser.NOT_EQUALS, 0); }
		public TerminalNode LESS() { return getToken(GobraParser.LESS, 0); }
		public TerminalNode LESS_OR_EQUALS() { return getToken(GobraParser.LESS_OR_EQUALS, 0); }
		public TerminalNode GREATER() { return getToken(GobraParser.GREATER, 0); }
		public TerminalNode GREATER_OR_EQUALS() { return getToken(GobraParser.GREATER_OR_EQUALS, 0); }
		public TerminalNode GHOST_EQUALS() { return getToken(GobraParser.GHOST_EQUALS, 0); }
		public TerminalNode GHOST_NOT_EQUALS() { return getToken(GobraParser.GHOST_NOT_EQUALS, 0); }
		public RelExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitRelExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LOGICAL_AND() { return getToken(GobraParser.LOGICAL_AND, 0); }
		public AndExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 164;
		enterRecursionRule(_localctx, 164, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1035);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1015);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1016);
				expression(15);
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1017);
				primaryExpr(0);
				}
				break;
			case 3:
				{
				_localctx = new UnfoldingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1018);
				match(UNFOLDING);
				setState(1019);
				predicateAccess();
				setState(1020);
				match(IN);
				setState(1021);
				expression(3);
				}
				break;
			case 4:
				{
				_localctx = new LetContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1023);
				match(LET);
				setState(1024);
				shortVarDecl();
				setState(1025);
				match(IN);
				setState(1026);
				expression(2);
				}
				break;
			case 5:
				{
				_localctx = new QuantificationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1028);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1029);
				boundVariables();
				setState(1030);
				match(COLON);
				setState(1031);
				match(COLON);
				setState(1032);
				triggers();
				setState(1033);
				expression(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1072);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1070);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
					case 1:
						{
						_localctx = new MulExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1037);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1038);
						((MulExprContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (DIV - 127)) | (1L << (MOD - 127)) | (1L << (LSHIFT - 127)) | (1L << (RSHIFT - 127)) | (1L << (BIT_CLEAR - 127)) | (1L << (STAR - 127)) | (1L << (AMPERSAND - 127)))) != 0)) ) {
							((MulExprContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1039);
						expression(14);
						}
						break;
					case 2:
						{
						_localctx = new AddExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1040);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1041);
						((AddExprContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (PLUS_PLUS - 114)) | (1L << (OR - 114)) | (1L << (PLUS - 114)) | (1L << (MINUS - 114)) | (1L << (CARET - 114)))) != 0)) ) {
							((AddExprContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1042);
						expression(13);
						}
						break;
					case 3:
						{
						_localctx = new P42ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1043);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1044);
						((P42ExprContext)_localctx).p42_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UNION) | (1L << INTERSECTION) | (1L << SETMINUS))) != 0)) ) {
							((P42ExprContext)_localctx).p42_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1045);
						expression(12);
						}
						break;
					case 4:
						{
						_localctx = new P41ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1046);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1047);
						((P41ExprContext)_localctx).p41_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IN) | (1L << MULTI) | (1L << SUBSET))) != 0)) ) {
							((P41ExprContext)_localctx).p41_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1048);
						expression(11);
						}
						break;
					case 5:
						{
						_localctx = new RelExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1049);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1050);
						((RelExprContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & ((1L << (GHOST_EQUALS - 73)) | (1L << (GHOST_NOT_EQUALS - 73)) | (1L << (EQUALS - 73)) | (1L << (NOT_EQUALS - 73)) | (1L << (LESS - 73)) | (1L << (LESS_OR_EQUALS - 73)) | (1L << (GREATER - 73)) | (1L << (GREATER_OR_EQUALS - 73)))) != 0)) ) {
							((RelExprContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1051);
						expression(10);
						}
						break;
					case 6:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1052);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1053);
						match(LOGICAL_AND);
						setState(1054);
						expression(8);
						}
						break;
					case 7:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1055);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1056);
						match(LOGICAL_OR);
						setState(1057);
						expression(7);
						}
						break;
					case 8:
						{
						_localctx = new ImplicationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1058);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1059);
						match(IMPLIES);
						setState(1060);
						expression(5);
						}
						break;
					case 9:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1061);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1062);
						match(QMARK);
						setState(1063);
						expression(0);
						setState(1064);
						match(COLON);
						setState(1065);
						expression(4);
						}
						break;
					case 10:
						{
						_localctx = new ClosureImplSpecExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1067);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1068);
						match(IMPL);
						setState(1069);
						closureSpecInstance();
						}
						break;
					}
					} 
				}
				setState(1074);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public GhostStatementContext ghostStatement() {
			return getRuleContext(GhostStatementContext.class,0);
		}
		public AuxiliaryStatementContext auxiliaryStatement() {
			return getRuleContext(AuxiliaryStatementContext.class,0);
		}
		public PackageStmtContext packageStmt() {
			return getRuleContext(PackageStmtContext.class,0);
		}
		public ApplyStmtContext applyStmt() {
			return getRuleContext(ApplyStmtContext.class,0);
		}
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public LabeledStmtContext labeledStmt() {
			return getRuleContext(LabeledStmtContext.class,0);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
		}
		public GoStmtContext goStmt() {
			return getRuleContext(GoStmtContext.class,0);
		}
		public ReturnStmtContext returnStmt() {
			return getRuleContext(ReturnStmtContext.class,0);
		}
		public BreakStmtContext breakStmt() {
			return getRuleContext(BreakStmtContext.class,0);
		}
		public ContinueStmtContext continueStmt() {
			return getRuleContext(ContinueStmtContext.class,0);
		}
		public GotoStmtContext gotoStmt() {
			return getRuleContext(GotoStmtContext.class,0);
		}
		public FallthroughStmtContext fallthroughStmt() {
			return getRuleContext(FallthroughStmtContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public SwitchStmtContext switchStmt() {
			return getRuleContext(SwitchStmtContext.class,0);
		}
		public SelectStmtContext selectStmt() {
			return getRuleContext(SelectStmtContext.class,0);
		}
		public SpecForStmtContext specForStmt() {
			return getRuleContext(SpecForStmtContext.class,0);
		}
		public DeferStmtContext deferStmt() {
			return getRuleContext(DeferStmtContext.class,0);
		}
		public ClosureImplProofStmtContext closureImplProofStmt() {
			return getRuleContext(ClosureImplProofStmtContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_statement);
		try {
			setState(1095);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1075);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1076);
				auxiliaryStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1077);
				packageStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1078);
				applyStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1079);
				declaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1080);
				labeledStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1081);
				simpleStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1082);
				goStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1083);
				returnStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1084);
				breakStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1085);
				continueStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1086);
				gotoStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1087);
				fallthroughStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1088);
				block();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1089);
				ifStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(1090);
				switchStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(1091);
				selectStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(1092);
				specForStmt();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(1093);
				deferStmt();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(1094);
				closureImplProofStmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ApplyStmtContext extends ParserRuleContext {
		public TerminalNode APPLY() { return getToken(GobraParser.APPLY, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ApplyStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_applyStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitApplyStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ApplyStmtContext applyStmt() throws RecognitionException {
		ApplyStmtContext _localctx = new ApplyStmtContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1097);
			match(APPLY);
			setState(1098);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PackageStmtContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(GobraParser.PACKAGE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public PackageStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPackageStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageStmtContext packageStmt() throws RecognitionException {
		PackageStmtContext _localctx = new PackageStmtContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1100);
			match(PACKAGE);
			setState(1101);
			expression(0);
			setState(1103);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(1102);
				block();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpecForStmtContext extends ParserRuleContext {
		public LoopSpecContext loopSpec() {
			return getRuleContext(LoopSpecContext.class,0);
		}
		public ForStmtContext forStmt() {
			return getRuleContext(ForStmtContext.class,0);
		}
		public SpecForStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specForStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSpecForStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecForStmtContext specForStmt() throws RecognitionException {
		SpecForStmtContext _localctx = new SpecForStmtContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1105);
			loopSpec();
			setState(1106);
			forStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoopSpecContext extends ParserRuleContext {
		public List<TerminalNode> INV() { return getTokens(GobraParser.INV); }
		public TerminalNode INV(int i) {
			return getToken(GobraParser.INV, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public TerminalNode DEC() { return getToken(GobraParser.DEC, 0); }
		public TerminationMeasureContext terminationMeasure() {
			return getRuleContext(TerminationMeasureContext.class,0);
		}
		public LoopSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLoopSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopSpecContext loopSpec() throws RecognitionException {
		LoopSpecContext _localctx = new LoopSpecContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(1108);
				match(INV);
				setState(1109);
				expression(0);
				setState(1110);
				eos();
				}
				}
				setState(1116);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(1117);
				match(DEC);
				setState(1118);
				terminationMeasure();
				setState(1119);
				eos();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeferStmtContext extends ParserRuleContext {
		public Token fold_stmt;
		public TerminalNode DEFER() { return getToken(GobraParser.DEFER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PredicateAccessContext predicateAccess() {
			return getRuleContext(PredicateAccessContext.class,0);
		}
		public TerminalNode FOLD() { return getToken(GobraParser.FOLD, 0); }
		public TerminalNode UNFOLD() { return getToken(GobraParser.UNFOLD, 0); }
		public DeferStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deferStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitDeferStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeferStmtContext deferStmt() throws RecognitionException {
		DeferStmtContext _localctx = new DeferStmtContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_deferStmt);
		int _la;
		try {
			setState(1128);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1123);
				match(DEFER);
				setState(1124);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1125);
				match(DEFER);
				setState(1126);
				((DeferStmtContext)_localctx).fold_stmt = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==FOLD || _la==UNFOLD) ) {
					((DeferStmtContext)_localctx).fold_stmt = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1127);
				predicateAccess();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BasicLitContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(GobraParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(GobraParser.FALSE, 0); }
		public TerminalNode NIL_LIT() { return getToken(GobraParser.NIL_LIT, 0); }
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public String_Context string_() {
			return getRuleContext(String_Context.class,0);
		}
		public TerminalNode FLOAT_LIT() { return getToken(GobraParser.FLOAT_LIT, 0); }
		public TerminalNode IMAGINARY_LIT() { return getToken(GobraParser.IMAGINARY_LIT, 0); }
		public TerminalNode RUNE_LIT() { return getToken(GobraParser.RUNE_LIT, 0); }
		public BasicLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basicLit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBasicLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BasicLitContext basicLit() throws RecognitionException {
		BasicLitContext _localctx = new BasicLitContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_basicLit);
		try {
			setState(1138);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1130);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1131);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1132);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1133);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1134);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1135);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1136);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1137);
				match(RUNE_LIT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryExprContext extends ParserRuleContext {
		public PrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpr; }
	 
		public PrimaryExprContext() { }
		public void copyFrom(PrimaryExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NewExprContext extends PrimaryExprContext {
		public New_Context new_() {
			return getRuleContext(New_Context.class,0);
		}
		public NewExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitNewExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MakeExprContext extends PrimaryExprContext {
		public MakeContext make() {
			return getRuleContext(MakeContext.class,0);
		}
		public MakeExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMakeExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GhostPrimaryExpr_Context extends PrimaryExprContext {
		public GhostPrimaryExprContext ghostPrimaryExpr() {
			return getRuleContext(GhostPrimaryExprContext.class,0);
		}
		public GhostPrimaryExpr_Context(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostPrimaryExpr_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InvokePrimaryExprWithSpecContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode AS() { return getToken(GobraParser.AS, 0); }
		public ClosureSpecInstanceContext closureSpecInstance() {
			return getRuleContext(ClosureSpecInstanceContext.class,0);
		}
		public InvokePrimaryExprWithSpecContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitInvokePrimaryExprWithSpec(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IndexPrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public IndexPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitIndexPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SeqUpdPrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public SeqUpdExpContext seqUpdExp() {
			return getRuleContext(SeqUpdExpContext.class,0);
		}
		public SeqUpdPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSeqUpdPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodPrimaryExprContext extends PrimaryExprContext {
		public MethodExprContext methodExpr() {
			return getRuleContext(MethodExprContext.class,0);
		}
		public MethodPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMethodPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PredConstrPrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public PredConstructArgsContext predConstructArgs() {
			return getRuleContext(PredConstructArgsContext.class,0);
		}
		public PredConstrPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredConstrPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InvokePrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public InvokePrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitInvokePrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OperandPrimaryExprContext extends PrimaryExprContext {
		public OperandContext operand() {
			return getRuleContext(OperandContext.class,0);
		}
		public OperandPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOperandPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeAssertionPrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public TypeAssertionContext typeAssertion() {
			return getRuleContext(TypeAssertionContext.class,0);
		}
		public TypeAssertionPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeAssertionPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BuiltInCallExprContext extends PrimaryExprContext {
		public Token call_op;
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode LEN() { return getToken(GobraParser.LEN, 0); }
		public TerminalNode CAP() { return getToken(GobraParser.CAP, 0); }
		public TerminalNode DOM() { return getToken(GobraParser.DOM, 0); }
		public TerminalNode RANGE() { return getToken(GobraParser.RANGE, 0); }
		public BuiltInCallExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBuiltInCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SelectorPrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SelectorPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSelectorPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConversionPrimaryExprContext extends PrimaryExprContext {
		public ConversionContext conversion() {
			return getRuleContext(ConversionContext.class,0);
		}
		public ConversionPrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitConversionPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SlicePrimaryExprContext extends PrimaryExprContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public Slice_Context slice_() {
			return getRuleContext(Slice_Context.class,0);
		}
		public SlicePrimaryExprContext(PrimaryExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSlicePrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		return primaryExpr(0);
	}

	private PrimaryExprContext primaryExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, _parentState);
		PrimaryExprContext _prevctx = _localctx;
		int _startState = 180;
		enterRecursionRule(_localctx, 180, RULE_primaryExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1152);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				_localctx = new OperandPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1141);
				operand();
				}
				break;
			case 2:
				{
				_localctx = new ConversionPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1142);
				conversion();
				}
				break;
			case 3:
				{
				_localctx = new MethodPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1143);
				methodExpr();
				}
				break;
			case 4:
				{
				_localctx = new GhostPrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1144);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1145);
				new_();
				}
				break;
			case 6:
				{
				_localctx = new MakeExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1146);
				make();
				}
				break;
			case 7:
				{
				_localctx = new BuiltInCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1147);
				((BuiltInCallExprContext)_localctx).call_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 45)) & ~0x3f) == 0 && ((1L << (_la - 45)) & ((1L << (LEN - 45)) | (1L << (CAP - 45)) | (1L << (DOM - 45)) | (1L << (RANGE - 45)))) != 0)) ) {
					((BuiltInCallExprContext)_localctx).call_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1148);
				match(L_PAREN);
				setState(1149);
				expression(0);
				setState(1150);
				match(R_PAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1176);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1174);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1154);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1155);
						match(DOT);
						setState(1156);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new IndexPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1157);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1158);
						index();
						}
						break;
					case 3:
						{
						_localctx = new SlicePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1159);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1160);
						slice_();
						}
						break;
					case 4:
						{
						_localctx = new SeqUpdPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1161);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1162);
						seqUpdExp();
						}
						break;
					case 5:
						{
						_localctx = new TypeAssertionPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1163);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1164);
						typeAssertion();
						}
						break;
					case 6:
						{
						_localctx = new InvokePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1165);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1166);
						arguments();
						}
						break;
					case 7:
						{
						_localctx = new InvokePrimaryExprWithSpecContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1167);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1168);
						arguments();
						setState(1169);
						match(AS);
						setState(1170);
						closureSpecInstance();
						}
						break;
					case 8:
						{
						_localctx = new PredConstrPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1172);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(1173);
						predConstructArgs();
						}
						break;
					}
					} 
				}
				setState(1178);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FunctionLitContext extends ParserRuleContext {
		public SpecificationContext specification;
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public ClosureDeclContext closureDecl() {
			return getRuleContext(ClosureDeclContext.class,0);
		}
		public FunctionLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionLit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFunctionLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionLitContext functionLit() throws RecognitionException {
		FunctionLitContext _localctx = new FunctionLitContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1179);
			((FunctionLitContext)_localctx).specification = specification();
			setState(1180);
			closureDecl(((FunctionLitContext)_localctx).specification.trusted, ((FunctionLitContext)_localctx).specification.pure);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClosureDeclContext extends ParserRuleContext {
		public boolean trusted;
		public boolean pure;
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
		}
		public ClosureDeclContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public ClosureDeclContext(ParserRuleContext parent, int invokingState, boolean trusted, boolean pure) {
			super(parent, invokingState);
			this.trusted = trusted;
			this.pure = pure;
		}
		@Override public int getRuleIndex() { return RULE_closureDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosureDeclContext closureDecl(boolean trusted,boolean pure) throws RecognitionException {
		ClosureDeclContext _localctx = new ClosureDeclContext(_ctx, getState(), trusted, pure);
		enterRule(_localctx, 184, RULE_closureDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1182);
			match(FUNC);
			setState(1184);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1183);
				match(IDENTIFIER);
				}
			}

			{
			setState(1186);
			signature();
			setState(1188);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(1187);
				blockWithBodyParameterInfo();
				}
				break;
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredConstructArgsContext extends ParserRuleContext {
		public TerminalNode L_PRED() { return getToken(GobraParser.L_PRED, 0); }
		public TerminalNode R_PRED() { return getToken(GobraParser.R_PRED, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public PredConstructArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predConstructArgs; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredConstructArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredConstructArgsContext predConstructArgs() throws RecognitionException {
		PredConstructArgsContext _localctx = new PredConstructArgsContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1190);
			match(L_PRED);
			setState(1192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1191);
				expressionList();
				}
			}

			setState(1195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1194);
				match(COMMA);
				}
			}

			setState(1197);
			match(R_PRED);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceTypeContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(GobraParser.INTERFACE, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public List<MethodSpecContext> methodSpec() {
			return getRuleContexts(MethodSpecContext.class);
		}
		public MethodSpecContext methodSpec(int i) {
			return getRuleContext(MethodSpecContext.class,i);
		}
		public List<TypeNameContext> typeName() {
			return getRuleContexts(TypeNameContext.class);
		}
		public TypeNameContext typeName(int i) {
			return getRuleContext(TypeNameContext.class,i);
		}
		public List<PredicateSpecContext> predicateSpec() {
			return getRuleContexts(PredicateSpecContext.class);
		}
		public PredicateSpecContext predicateSpec(int i) {
			return getRuleContext(PredicateSpecContext.class,i);
		}
		public InterfaceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitInterfaceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceTypeContext interfaceType() throws RecognitionException {
		InterfaceTypeContext _localctx = new InterfaceTypeContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_interfaceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(INTERFACE);
			setState(1200);
			match(L_CURLY);
			setState(1210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << PRED))) != 0) || _la==TRUSTED || _la==IDENTIFIER) {
				{
				{
				setState(1204);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
				case 1:
					{
					setState(1201);
					methodSpec();
					}
					break;
				case 2:
					{
					setState(1202);
					typeName();
					}
					break;
				case 3:
					{
					setState(1203);
					predicateSpec();
					}
					break;
				}
				setState(1206);
				eos();
				}
				}
				setState(1212);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1213);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateSpecContext extends ParserRuleContext {
		public TerminalNode PRED() { return getToken(GobraParser.PRED, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public PredicateSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredicateSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateSpecContext predicateSpec() throws RecognitionException {
		PredicateSpecContext _localctx = new PredicateSpecContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1215);
			match(PRED);
			setState(1216);
			match(IDENTIFIER);
			setState(1217);
			parameters();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodSpecContext extends ParserRuleContext {
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public ResultContext result() {
			return getRuleContext(ResultContext.class,0);
		}
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public MethodSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMethodSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodSpecContext methodSpec() throws RecognitionException {
		MethodSpecContext _localctx = new MethodSpecContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_methodSpec);
		int _la;
		try {
			setState(1234);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1220);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1219);
					match(GHOST);
					}
				}

				setState(1222);
				specification();
				setState(1223);
				match(IDENTIFIER);
				setState(1224);
				parameters();
				setState(1225);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1228);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1227);
					match(GHOST);
					}
				}

				setState(1230);
				specification();
				setState(1231);
				match(IDENTIFIER);
				setState(1232);
				parameters();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_Context extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TypeLitContext typeLit() {
			return getRuleContext(TypeLitContext.class,0);
		}
		public GhostTypeLitContext ghostTypeLit() {
			return getRuleContext(GhostTypeLitContext.class,0);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public Type_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitType_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Type_Context type_() throws RecognitionException {
		Type_Context _localctx = new Type_Context(_ctx, getState());
		enterRule(_localctx, 194, RULE_type_);
		try {
			setState(1243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1236);
				typeName();
				}
				break;
			case PRED:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
			case L_BRACKET:
			case STAR:
			case RECEIVE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1237);
				typeLit();
				}
				break;
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
			case ADT:
				enterOuterAlt(_localctx, 3);
				{
				setState(1238);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(1239);
				match(L_PAREN);
				setState(1240);
				type_();
				setState(1241);
				match(R_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeLitContext extends ParserRuleContext {
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public StructTypeContext structType() {
			return getRuleContext(StructTypeContext.class,0);
		}
		public PointerTypeContext pointerType() {
			return getRuleContext(PointerTypeContext.class,0);
		}
		public FunctionTypeContext functionType() {
			return getRuleContext(FunctionTypeContext.class,0);
		}
		public InterfaceTypeContext interfaceType() {
			return getRuleContext(InterfaceTypeContext.class,0);
		}
		public SliceTypeContext sliceType() {
			return getRuleContext(SliceTypeContext.class,0);
		}
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public ChannelTypeContext channelType() {
			return getRuleContext(ChannelTypeContext.class,0);
		}
		public PredTypeContext predType() {
			return getRuleContext(PredTypeContext.class,0);
		}
		public TypeLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeLit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeLitContext typeLit() throws RecognitionException {
		TypeLitContext _localctx = new TypeLitContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_typeLit);
		try {
			setState(1254);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1245);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1246);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1247);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1248);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1249);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1250);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1251);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1252);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1253);
				predType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredTypeContext extends ParserRuleContext {
		public TerminalNode PRED() { return getToken(GobraParser.PRED, 0); }
		public PredTypeParamsContext predTypeParams() {
			return getRuleContext(PredTypeParamsContext.class,0);
		}
		public PredTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredTypeContext predType() throws RecognitionException {
		PredTypeContext _localctx = new PredTypeContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1256);
			match(PRED);
			setState(1257);
			predTypeParams();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredTypeParamsContext extends ParserRuleContext {
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<Type_Context> type_() {
			return getRuleContexts(Type_Context.class);
		}
		public Type_Context type_(int i) {
			return getRuleContext(Type_Context.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public PredTypeParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predTypeParams; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPredTypeParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredTypeParamsContext predTypeParams() throws RecognitionException {
		PredTypeParamsContext _localctx = new PredTypeParamsContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1259);
			match(L_PAREN);
			setState(1271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (FUNC - 78)) | (1L << (INTERFACE - 78)) | (1L << (MAP - 78)) | (1L << (STRUCT - 78)) | (1L << (CHAN - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (STAR - 78)) | (1L << (RECEIVE - 78)))) != 0)) {
				{
				setState(1260);
				type_();
				setState(1265);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,100,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1261);
						match(COMMA);
						setState(1262);
						type_();
						}
						} 
					}
					setState(1267);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,100,_ctx);
				}
				setState(1269);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1268);
					match(COMMA);
					}
				}

				}
			}

			setState(1273);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralTypeContext extends ParserRuleContext {
		public StructTypeContext structType() {
			return getRuleContext(StructTypeContext.class,0);
		}
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public ImplicitArrayContext implicitArray() {
			return getRuleContext(ImplicitArrayContext.class,0);
		}
		public SliceTypeContext sliceType() {
			return getRuleContext(SliceTypeContext.class,0);
		}
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public GhostTypeLitContext ghostTypeLit() {
			return getRuleContext(GhostTypeLitContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public LiteralTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLiteralType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralTypeContext literalType() throws RecognitionException {
		LiteralTypeContext _localctx = new LiteralTypeContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_literalType);
		try {
			setState(1282);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1275);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1276);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1277);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1278);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1279);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1280);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1281);
				typeName();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplicitArrayContext extends ParserRuleContext {
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public TerminalNode ELLIPSIS() { return getToken(GobraParser.ELLIPSIS, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public ImplicitArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implicitArray; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImplicitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImplicitArrayContext implicitArray() throws RecognitionException {
		ImplicitArrayContext _localctx = new ImplicitArrayContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1284);
			match(L_BRACKET);
			setState(1285);
			match(ELLIPSIS);
			setState(1286);
			match(R_BRACKET);
			setState(1287);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Slice_Context extends ParserRuleContext {
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public List<TerminalNode> COLON() { return getTokens(GobraParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(GobraParser.COLON, i);
		}
		public HighSliceArgumentContext highSliceArgument() {
			return getRuleContext(HighSliceArgumentContext.class,0);
		}
		public CapSliceArgumentContext capSliceArgument() {
			return getRuleContext(CapSliceArgumentContext.class,0);
		}
		public LowSliceArgumentContext lowSliceArgument() {
			return getRuleContext(LowSliceArgumentContext.class,0);
		}
		public Slice_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slice_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSlice_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Slice_Context slice_() throws RecognitionException {
		Slice_Context _localctx = new Slice_Context(_ctx, getState());
		enterRule(_localctx, 206, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1289);
			match(L_BRACKET);
			setState(1305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(1291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1290);
					lowSliceArgument();
					}
				}

				setState(1293);
				match(COLON);
				setState(1295);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1294);
					highSliceArgument();
					}
				}

				}
				break;
			case 2:
				{
				setState(1298);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1297);
					lowSliceArgument();
					}
				}

				setState(1300);
				match(COLON);
				setState(1301);
				highSliceArgument();
				setState(1302);
				match(COLON);
				setState(1303);
				capSliceArgument();
				}
				break;
			}
			setState(1307);
			match(R_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LowSliceArgumentContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LowSliceArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lowSliceArgument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLowSliceArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LowSliceArgumentContext lowSliceArgument() throws RecognitionException {
		LowSliceArgumentContext _localctx = new LowSliceArgumentContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_lowSliceArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1309);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HighSliceArgumentContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public HighSliceArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_highSliceArgument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitHighSliceArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HighSliceArgumentContext highSliceArgument() throws RecognitionException {
		HighSliceArgumentContext _localctx = new HighSliceArgumentContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_highSliceArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1311);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CapSliceArgumentContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CapSliceArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_capSliceArgument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitCapSliceArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CapSliceArgumentContext capSliceArgument() throws RecognitionException {
		CapSliceArgumentContext _localctx = new CapSliceArgumentContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_capSliceArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1313);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assign_opContext extends ParserRuleContext {
		public Token ass_op;
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public TerminalNode PLUS() { return getToken(GobraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(GobraParser.MINUS, 0); }
		public TerminalNode OR() { return getToken(GobraParser.OR, 0); }
		public TerminalNode CARET() { return getToken(GobraParser.CARET, 0); }
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public TerminalNode DIV() { return getToken(GobraParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(GobraParser.MOD, 0); }
		public TerminalNode LSHIFT() { return getToken(GobraParser.LSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(GobraParser.RSHIFT, 0); }
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public TerminalNode BIT_CLEAR() { return getToken(GobraParser.BIT_CLEAR, 0); }
		public Assign_opContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_op; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAssign_op(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assign_opContext assign_op() throws RecognitionException {
		Assign_opContext _localctx = new Assign_opContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1316);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 126)) & ~0x3f) == 0 && ((1L << (_la - 126)) & ((1L << (OR - 126)) | (1L << (DIV - 126)) | (1L << (MOD - 126)) | (1L << (LSHIFT - 126)) | (1L << (RSHIFT - 126)) | (1L << (BIT_CLEAR - 126)) | (1L << (PLUS - 126)) | (1L << (MINUS - 126)) | (1L << (CARET - 126)) | (1L << (STAR - 126)) | (1L << (AMPERSAND - 126)))) != 0)) {
				{
				setState(1315);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 126)) & ~0x3f) == 0 && ((1L << (_la - 126)) & ((1L << (OR - 126)) | (1L << (DIV - 126)) | (1L << (MOD - 126)) | (1L << (LSHIFT - 126)) | (1L << (RSHIFT - 126)) | (1L << (BIT_CLEAR - 126)) | (1L << (PLUS - 126)) | (1L << (MINUS - 126)) | (1L << (CARET - 126)) | (1L << (STAR - 126)) | (1L << (AMPERSAND - 126)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1318);
			match(ASSIGN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeClauseContext extends ParserRuleContext {
		public TerminalNode RANGE() { return getToken(GobraParser.RANGE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public MaybeAddressableIdentifierListContext maybeAddressableIdentifierList() {
			return getRuleContext(MaybeAddressableIdentifierListContext.class,0);
		}
		public TerminalNode DECLARE_ASSIGN() { return getToken(GobraParser.DECLARE_ASSIGN, 0); }
		public TerminalNode WITH() { return getToken(GobraParser.WITH, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public RangeClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitRangeClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeClauseContext rangeClause() throws RecognitionException {
		RangeClauseContext _localctx = new RangeClauseContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_rangeClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1326);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1320);
				expressionList();
				setState(1321);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1323);
				maybeAddressableIdentifierList();
				setState(1324);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1328);
			match(RANGE);
			setState(1329);
			expression(0);
			setState(1334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(1330);
				match(WITH);
				setState(1332);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1331);
					match(IDENTIFIER);
					}
				}

				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PackageClauseContext extends ParserRuleContext {
		public Token packageName;
		public TerminalNode PACKAGE() { return getToken(GobraParser.PACKAGE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public PackageClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPackageClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageClauseContext packageClause() throws RecognitionException {
		PackageClauseContext _localctx = new PackageClauseContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1336);
			match(PACKAGE);
			setState(1337);
			((PackageClauseContext)_localctx).packageName = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportPathContext extends ParserRuleContext {
		public String_Context string_() {
			return getRuleContext(String_Context.class,0);
		}
		public ImportPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importPath; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImportPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportPathContext importPath() throws RecognitionException {
		ImportPathContext _localctx = new ImportPathContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1339);
			string_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public ConstDeclContext constDecl() {
			return getRuleContext(ConstDeclContext.class,0);
		}
		public TypeDeclContext typeDecl() {
			return getRuleContext(TypeDeclContext.class,0);
		}
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_declaration);
		try {
			setState(1344);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1341);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1342);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1343);
				varDecl();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstDeclContext extends ParserRuleContext {
		public TerminalNode CONST() { return getToken(GobraParser.CONST, 0); }
		public List<ConstSpecContext> constSpec() {
			return getRuleContexts(ConstSpecContext.class);
		}
		public ConstSpecContext constSpec(int i) {
			return getRuleContext(ConstSpecContext.class,i);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public ConstDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitConstDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstDeclContext constDecl() throws RecognitionException {
		ConstDeclContext _localctx = new ConstDeclContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1346);
			match(CONST);
			setState(1358);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1347);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1348);
				match(L_PAREN);
				setState(1354);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1349);
					constSpec();
					setState(1350);
					eos();
					}
					}
					setState(1356);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1357);
				match(R_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstSpecContext extends ParserRuleContext {
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public ConstSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitConstSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstSpecContext constSpec() throws RecognitionException {
		ConstSpecContext _localctx = new ConstSpecContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1360);
			identifierList();
			setState(1366);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1362);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (FUNC - 78)) | (1L << (INTERFACE - 78)) | (1L << (MAP - 78)) | (1L << (STRUCT - 78)) | (1L << (CHAN - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (STAR - 78)) | (1L << (RECEIVE - 78)))) != 0)) {
					{
					setState(1361);
					type_();
					}
				}

				setState(1364);
				match(ASSIGN);
				setState(1365);
				expressionList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierListContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(GobraParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(GobraParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public IdentifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitIdentifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierListContext identifierList() throws RecognitionException {
		IdentifierListContext _localctx = new IdentifierListContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1368);
			match(IDENTIFIER);
			setState(1373);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,117,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1369);
					match(COMMA);
					setState(1370);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1375);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,117,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1376);
			expression(0);
			setState(1381);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,118,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1377);
					match(COMMA);
					setState(1378);
					expression(0);
					}
					} 
				}
				setState(1383);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,118,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeDeclContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(GobraParser.TYPE, 0); }
		public List<TypeSpecContext> typeSpec() {
			return getRuleContexts(TypeSpecContext.class);
		}
		public TypeSpecContext typeSpec(int i) {
			return getRuleContext(TypeSpecContext.class,i);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public TypeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclContext typeDecl() throws RecognitionException {
		TypeDeclContext _localctx = new TypeDeclContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1384);
			match(TYPE);
			setState(1396);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1385);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1386);
				match(L_PAREN);
				setState(1392);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1387);
					typeSpec();
					setState(1388);
					eos();
					}
					}
					setState(1394);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1395);
				match(R_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeSpecContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public TypeSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecContext typeSpec() throws RecognitionException {
		TypeSpecContext _localctx = new TypeSpecContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1398);
			match(IDENTIFIER);
			setState(1400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1399);
				match(ASSIGN);
				}
			}

			setState(1402);
			type_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(GobraParser.VAR, 0); }
		public List<VarSpecContext> varSpec() {
			return getRuleContexts(VarSpecContext.class);
		}
		public VarSpecContext varSpec(int i) {
			return getRuleContext(VarSpecContext.class,i);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1404);
			match(VAR);
			setState(1416);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1405);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1406);
				match(L_PAREN);
				setState(1412);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1407);
					varSpec();
					setState(1408);
					eos();
					}
					}
					setState(1414);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1415);
				match(R_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
			match(L_CURLY);
			setState(1420);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1419);
				statementList();
				}
				break;
			}
			setState(1422);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementListContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public StatementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitStatementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementListContext statementList() throws RecognitionException {
		StatementListContext _localctx = new StatementListContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1430); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1425);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
					case 1:
						{
						setState(1424);
						eos();
						}
						break;
					}
					setState(1427);
					statement();
					setState(1428);
					eos();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1432); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,126,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleStmtContext extends ParserRuleContext {
		public SendStmtContext sendStmt() {
			return getRuleContext(SendStmtContext.class,0);
		}
		public IncDecStmtContext incDecStmt() {
			return getRuleContext(IncDecStmtContext.class,0);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public ExpressionStmtContext expressionStmt() {
			return getRuleContext(ExpressionStmtContext.class,0);
		}
		public ShortVarDeclContext shortVarDecl() {
			return getRuleContext(ShortVarDeclContext.class,0);
		}
		public SimpleStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSimpleStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleStmtContext simpleStmt() throws RecognitionException {
		SimpleStmtContext _localctx = new SimpleStmtContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_simpleStmt);
		try {
			setState(1439);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1434);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1435);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1436);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1437);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1438);
				shortVarDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionStmtContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExpressionStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionStmtContext expressionStmt() throws RecognitionException {
		ExpressionStmtContext _localctx = new ExpressionStmtContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1441);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SendStmtContext extends ParserRuleContext {
		public ExpressionContext channel;
		public TerminalNode RECEIVE() { return getToken(GobraParser.RECEIVE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SendStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sendStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSendStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SendStmtContext sendStmt() throws RecognitionException {
		SendStmtContext _localctx = new SendStmtContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1443);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1444);
			match(RECEIVE);
			setState(1445);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncDecStmtContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode PLUS_PLUS() { return getToken(GobraParser.PLUS_PLUS, 0); }
		public TerminalNode MINUS_MINUS() { return getToken(GobraParser.MINUS_MINUS, 0); }
		public IncDecStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_incDecStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitIncDecStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncDecStmtContext incDecStmt() throws RecognitionException {
		IncDecStmtContext _localctx = new IncDecStmtContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1447);
			expression(0);
			setState(1448);
			_la = _input.LA(1);
			if ( !(_la==PLUS_PLUS || _la==MINUS_MINUS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public List<ExpressionListContext> expressionList() {
			return getRuleContexts(ExpressionListContext.class);
		}
		public ExpressionListContext expressionList(int i) {
			return getRuleContext(ExpressionListContext.class,i);
		}
		public Assign_opContext assign_op() {
			return getRuleContext(Assign_opContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1450);
			expressionList();
			setState(1451);
			assign_op();
			setState(1452);
			expressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EmptyStmtContext extends ParserRuleContext {
		public TerminalNode EOS() { return getToken(GobraParser.EOS, 0); }
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public EmptyStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitEmptyStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyStmtContext emptyStmt() throws RecognitionException {
		EmptyStmtContext _localctx = new EmptyStmtContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_emptyStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1454);
			_la = _input.LA(1);
			if ( !(_la==SEMI || _la==EOS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LabeledStmtContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public LabeledStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labeledStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLabeledStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabeledStmtContext labeledStmt() throws RecognitionException {
		LabeledStmtContext _localctx = new LabeledStmtContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1456);
			match(IDENTIFIER);
			setState(1457);
			match(COLON);
			setState(1459);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				{
				setState(1458);
				statement();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnStmtContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(GobraParser.RETURN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ReturnStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStmtContext returnStmt() throws RecognitionException {
		ReturnStmtContext _localctx = new ReturnStmtContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1461);
			match(RETURN);
			setState(1463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				{
				setState(1462);
				expressionList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BreakStmtContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(GobraParser.BREAK, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public BreakStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStmtContext breakStmt() throws RecognitionException {
		BreakStmtContext _localctx = new BreakStmtContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1465);
			match(BREAK);
			setState(1467);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1466);
				match(IDENTIFIER);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContinueStmtContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(GobraParser.CONTINUE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public ContinueStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStmtContext continueStmt() throws RecognitionException {
		ContinueStmtContext _localctx = new ContinueStmtContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1469);
			match(CONTINUE);
			setState(1471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1470);
				match(IDENTIFIER);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GotoStmtContext extends ParserRuleContext {
		public TerminalNode GOTO() { return getToken(GobraParser.GOTO, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public GotoStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gotoStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGotoStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GotoStmtContext gotoStmt() throws RecognitionException {
		GotoStmtContext _localctx = new GotoStmtContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1473);
			match(GOTO);
			setState(1474);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FallthroughStmtContext extends ParserRuleContext {
		public TerminalNode FALLTHROUGH() { return getToken(GobraParser.FALLTHROUGH, 0); }
		public FallthroughStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fallthroughStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFallthroughStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FallthroughStmtContext fallthroughStmt() throws RecognitionException {
		FallthroughStmtContext _localctx = new FallthroughStmtContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1476);
			match(FALLTHROUGH);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfStmtContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(GobraParser.IF, 0); }
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
		}
		public TerminalNode ELSE() { return getToken(GobraParser.ELSE, 0); }
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public IfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStmtContext ifStmt() throws RecognitionException {
		IfStmtContext _localctx = new IfStmtContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1478);
			match(IF);
			setState(1487);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1479);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1480);
				eos();
				setState(1481);
				expression(0);
				}
				break;
			case 3:
				{
				setState(1483);
				simpleStmt();
				setState(1484);
				eos();
				setState(1485);
				expression(0);
				}
				break;
			}
			setState(1489);
			block();
			setState(1495);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1490);
				match(ELSE);
				setState(1493);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1491);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1492);
					block();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchStmtContext extends ParserRuleContext {
		public ExprSwitchStmtContext exprSwitchStmt() {
			return getRuleContext(ExprSwitchStmtContext.class,0);
		}
		public TypeSwitchStmtContext typeSwitchStmt() {
			return getRuleContext(TypeSwitchStmtContext.class,0);
		}
		public SwitchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSwitchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStmtContext switchStmt() throws RecognitionException {
		SwitchStmtContext _localctx = new SwitchStmtContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_switchStmt);
		try {
			setState(1499);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1497);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1498);
				typeSwitchStmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprSwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(GobraParser.SWITCH, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public List<ExprCaseClauseContext> exprCaseClause() {
			return getRuleContexts(ExprCaseClauseContext.class);
		}
		public ExprCaseClauseContext exprCaseClause(int i) {
			return getRuleContext(ExprCaseClauseContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
		}
		public ExprSwitchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprSwitchStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExprSwitchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSwitchStmtContext exprSwitchStmt() throws RecognitionException {
		ExprSwitchStmtContext _localctx = new ExprSwitchStmtContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1501);
			match(SWITCH);
			setState(1512);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
			case 1:
				{
				setState(1503);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1502);
					expression(0);
					}
				}

				}
				break;
			case 2:
				{
				setState(1506);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
				case 1:
					{
					setState(1505);
					simpleStmt();
					}
					break;
				}
				setState(1508);
				eos();
				setState(1510);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1509);
					expression(0);
					}
				}

				}
				break;
			}
			setState(1514);
			match(L_CURLY);
			setState(1518);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1515);
				exprCaseClause();
				}
				}
				setState(1520);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1521);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprCaseClauseContext extends ParserRuleContext {
		public ExprSwitchCaseContext exprSwitchCase() {
			return getRuleContext(ExprSwitchCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public ExprCaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprCaseClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExprCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprCaseClauseContext exprCaseClause() throws RecognitionException {
		ExprCaseClauseContext _localctx = new ExprCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_exprCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1523);
			exprSwitchCase();
			setState(1524);
			match(COLON);
			setState(1526);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				{
				setState(1525);
				statementList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprSwitchCaseContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(GobraParser.CASE, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(GobraParser.DEFAULT, 0); }
		public ExprSwitchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprSwitchCase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExprSwitchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSwitchCaseContext exprSwitchCase() throws RecognitionException {
		ExprSwitchCaseContext _localctx = new ExprSwitchCaseContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_exprSwitchCase);
		try {
			setState(1531);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1528);
				match(CASE);
				setState(1529);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1530);
				match(DEFAULT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeSwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(GobraParser.SWITCH, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public TypeSwitchGuardContext typeSwitchGuard() {
			return getRuleContext(TypeSwitchGuardContext.class,0);
		}
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
		}
		public List<TypeCaseClauseContext> typeCaseClause() {
			return getRuleContexts(TypeCaseClauseContext.class);
		}
		public TypeCaseClauseContext typeCaseClause(int i) {
			return getRuleContext(TypeCaseClauseContext.class,i);
		}
		public TypeSwitchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSwitchStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeSwitchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchStmtContext typeSwitchStmt() throws RecognitionException {
		TypeSwitchStmtContext _localctx = new TypeSwitchStmtContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1533);
			match(SWITCH);
			setState(1542);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
			case 1:
				{
				setState(1534);
				typeSwitchGuard();
				}
				break;
			case 2:
				{
				setState(1535);
				eos();
				setState(1536);
				typeSwitchGuard();
				}
				break;
			case 3:
				{
				setState(1538);
				simpleStmt();
				setState(1539);
				eos();
				setState(1540);
				typeSwitchGuard();
				}
				break;
			}
			setState(1544);
			match(L_CURLY);
			setState(1548);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1545);
				typeCaseClause();
				}
				}
				setState(1550);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1551);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeSwitchGuardContext extends ParserRuleContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode TYPE() { return getToken(GobraParser.TYPE, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode DECLARE_ASSIGN() { return getToken(GobraParser.DECLARE_ASSIGN, 0); }
		public TypeSwitchGuardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSwitchGuard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeSwitchGuard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchGuardContext typeSwitchGuard() throws RecognitionException {
		TypeSwitchGuardContext _localctx = new TypeSwitchGuardContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1555);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				{
				setState(1553);
				match(IDENTIFIER);
				setState(1554);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1557);
			primaryExpr(0);
			setState(1558);
			match(DOT);
			setState(1559);
			match(L_PAREN);
			setState(1560);
			match(TYPE);
			setState(1561);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeCaseClauseContext extends ParserRuleContext {
		public TypeSwitchCaseContext typeSwitchCase() {
			return getRuleContext(TypeSwitchCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public TypeCaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeCaseClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeCaseClauseContext typeCaseClause() throws RecognitionException {
		TypeCaseClauseContext _localctx = new TypeCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_typeCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1563);
			typeSwitchCase();
			setState(1564);
			match(COLON);
			setState(1566);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
			case 1:
				{
				setState(1565);
				statementList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeSwitchCaseContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(GobraParser.CASE, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(GobraParser.DEFAULT, 0); }
		public TypeSwitchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSwitchCase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeSwitchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchCaseContext typeSwitchCase() throws RecognitionException {
		TypeSwitchCaseContext _localctx = new TypeSwitchCaseContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_typeSwitchCase);
		try {
			setState(1571);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1568);
				match(CASE);
				setState(1569);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1570);
				match(DEFAULT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeListContext extends ParserRuleContext {
		public List<Type_Context> type_() {
			return getRuleContexts(Type_Context.class);
		}
		public Type_Context type_(int i) {
			return getRuleContext(Type_Context.class,i);
		}
		public List<TerminalNode> NIL_LIT() { return getTokens(GobraParser.NIL_LIT); }
		public TerminalNode NIL_LIT(int i) {
			return getToken(GobraParser.NIL_LIT, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1575);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
			case ADT:
			case PRED:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
			case IDENTIFIER:
			case L_PAREN:
			case L_BRACKET:
			case STAR:
			case RECEIVE:
				{
				setState(1573);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1574);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1584);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1577);
				match(COMMA);
				setState(1580);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GHOST:
				case SEQ:
				case SET:
				case MSET:
				case DICT:
				case OPT:
				case DOM:
				case ADT:
				case PRED:
				case FUNC:
				case INTERFACE:
				case MAP:
				case STRUCT:
				case CHAN:
				case IDENTIFIER:
				case L_PAREN:
				case L_BRACKET:
				case STAR:
				case RECEIVE:
					{
					setState(1578);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1579);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1586);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectStmtContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(GobraParser.SELECT, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<CommClauseContext> commClause() {
			return getRuleContexts(CommClauseContext.class);
		}
		public CommClauseContext commClause(int i) {
			return getRuleContext(CommClauseContext.class,i);
		}
		public SelectStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSelectStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStmtContext selectStmt() throws RecognitionException {
		SelectStmtContext _localctx = new SelectStmtContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1587);
			match(SELECT);
			setState(1588);
			match(L_CURLY);
			setState(1592);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1589);
				commClause();
				}
				}
				setState(1594);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1595);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommClauseContext extends ParserRuleContext {
		public CommCaseContext commCase() {
			return getRuleContext(CommCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public CommClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitCommClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommClauseContext commClause() throws RecognitionException {
		CommClauseContext _localctx = new CommClauseContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_commClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1597);
			commCase();
			setState(1598);
			match(COLON);
			setState(1600);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1599);
				statementList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommCaseContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(GobraParser.CASE, 0); }
		public SendStmtContext sendStmt() {
			return getRuleContext(SendStmtContext.class,0);
		}
		public RecvStmtContext recvStmt() {
			return getRuleContext(RecvStmtContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(GobraParser.DEFAULT, 0); }
		public CommCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commCase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitCommCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommCaseContext commCase() throws RecognitionException {
		CommCaseContext _localctx = new CommCaseContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_commCase);
		try {
			setState(1608);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1602);
				match(CASE);
				setState(1605);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
				case 1:
					{
					setState(1603);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1604);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1607);
				match(DEFAULT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecvStmtContext extends ParserRuleContext {
		public ExpressionContext recvExpr;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode DECLARE_ASSIGN() { return getToken(GobraParser.DECLARE_ASSIGN, 0); }
		public RecvStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recvStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitRecvStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RecvStmtContext recvStmt() throws RecognitionException {
		RecvStmtContext _localctx = new RecvStmtContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1616);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				{
				setState(1610);
				expressionList();
				setState(1611);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1613);
				identifierList();
				setState(1614);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1618);
			((RecvStmtContext)_localctx).recvExpr = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForStmtContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(GobraParser.FOR, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForClauseContext forClause() {
			return getRuleContext(ForClauseContext.class,0);
		}
		public RangeClauseContext rangeClause() {
			return getRuleContext(RangeClauseContext.class,0);
		}
		public ForStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStmtContext forStmt() throws RecognitionException {
		ForStmtContext _localctx = new ForStmtContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1620);
			match(FOR);
			setState(1624);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1621);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1622);
				forClause();
				}
				break;
			case 3:
				{
				setState(1623);
				rangeClause();
				}
				break;
			}
			setState(1626);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForClauseContext extends ParserRuleContext {
		public SimpleStmtContext initStmt;
		public SimpleStmtContext postStmt;
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<SimpleStmtContext> simpleStmt() {
			return getRuleContexts(SimpleStmtContext.class);
		}
		public SimpleStmtContext simpleStmt(int i) {
			return getRuleContext(SimpleStmtContext.class,i);
		}
		public ForClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitForClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForClauseContext forClause() throws RecognitionException {
		ForClauseContext _localctx = new ForClauseContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1629);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
			case 1:
				{
				setState(1628);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1631);
			eos();
			setState(1633);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
			case 1:
				{
				setState(1632);
				expression(0);
				}
				break;
			}
			setState(1635);
			eos();
			setState(1637);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1636);
				((ForClauseContext)_localctx).postStmt = simpleStmt();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GoStmtContext extends ParserRuleContext {
		public TerminalNode GO() { return getToken(GobraParser.GO, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public GoStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_goStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGoStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GoStmtContext goStmt() throws RecognitionException {
		GoStmtContext _localctx = new GoStmtContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1639);
			match(GO);
			setState(1640);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNameContext extends ParserRuleContext {
		public QualifiedIdentContext qualifiedIdent() {
			return getRuleContext(QualifiedIdentContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_typeName);
		try {
			setState(1644);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1642);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1643);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayTypeContext extends ParserRuleContext {
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public ArrayLengthContext arrayLength() {
			return getRuleContext(ArrayLengthContext.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1646);
			match(L_BRACKET);
			setState(1647);
			arrayLength();
			setState(1648);
			match(R_BRACKET);
			setState(1649);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayLengthContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayLengthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLength; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitArrayLength(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLengthContext arrayLength() throws RecognitionException {
		ArrayLengthContext _localctx = new ArrayLengthContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1651);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementTypeContext extends ParserRuleContext {
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public ElementTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitElementType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementTypeContext elementType() throws RecognitionException {
		ElementTypeContext _localctx = new ElementTypeContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1653);
			type_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PointerTypeContext extends ParserRuleContext {
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public PointerTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPointerType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PointerTypeContext pointerType() throws RecognitionException {
		PointerTypeContext _localctx = new PointerTypeContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1655);
			match(STAR);
			setState(1656);
			type_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SliceTypeContext extends ParserRuleContext {
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public SliceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sliceType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSliceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SliceTypeContext sliceType() throws RecognitionException {
		SliceTypeContext _localctx = new SliceTypeContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1658);
			match(L_BRACKET);
			setState(1659);
			match(R_BRACKET);
			setState(1660);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapTypeContext extends ParserRuleContext {
		public TerminalNode MAP() { return getToken(GobraParser.MAP, 0); }
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public MapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1662);
			match(MAP);
			setState(1663);
			match(L_BRACKET);
			setState(1664);
			type_();
			setState(1665);
			match(R_BRACKET);
			setState(1666);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChannelTypeContext extends ParserRuleContext {
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
		}
		public TerminalNode CHAN() { return getToken(GobraParser.CHAN, 0); }
		public TerminalNode RECEIVE() { return getToken(GobraParser.RECEIVE, 0); }
		public ChannelTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_channelType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitChannelType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChannelTypeContext channelType() throws RecognitionException {
		ChannelTypeContext _localctx = new ChannelTypeContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1673);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,161,_ctx) ) {
			case 1:
				{
				setState(1668);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1669);
				match(CHAN);
				setState(1670);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1671);
				match(RECEIVE);
				setState(1672);
				match(CHAN);
				}
				break;
			}
			setState(1675);
			elementType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionTypeContext extends ParserRuleContext {
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public FunctionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFunctionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTypeContext functionType() throws RecognitionException {
		FunctionTypeContext _localctx = new FunctionTypeContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1677);
			match(FUNC);
			setState(1678);
			signature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SignatureContext extends ParserRuleContext {
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public ResultContext result() {
			return getRuleContext(ResultContext.class,0);
		}
		public SignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signature; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitSignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignatureContext signature() throws RecognitionException {
		SignatureContext _localctx = new SignatureContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_signature);
		try {
			setState(1684);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1680);
				parameters();
				setState(1681);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1683);
				parameters();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResultContext extends ParserRuleContext {
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public ResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultContext result() throws RecognitionException {
		ResultContext _localctx = new ResultContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_result);
		try {
			setState(1688);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,163,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1686);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1687);
				type_();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametersContext extends ParserRuleContext {
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<ParameterDeclContext> parameterDecl() {
			return getRuleContexts(ParameterDeclContext.class);
		}
		public ParameterDeclContext parameterDecl(int i) {
			return getRuleContext(ParameterDeclContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1690);
			match(L_PAREN);
			setState(1702);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (FUNC - 78)) | (1L << (INTERFACE - 78)) | (1L << (MAP - 78)) | (1L << (STRUCT - 78)) | (1L << (CHAN - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (ELLIPSIS - 78)) | (1L << (STAR - 78)) | (1L << (RECEIVE - 78)))) != 0)) {
				{
				setState(1691);
				parameterDecl();
				setState(1696);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,164,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1692);
						match(COMMA);
						setState(1693);
						parameterDecl();
						}
						} 
					}
					setState(1698);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,164,_ctx);
				}
				setState(1700);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1699);
					match(COMMA);
					}
				}

				}
			}

			setState(1704);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConversionContext extends ParserRuleContext {
		public NonNamedTypeContext nonNamedType() {
			return getRuleContext(NonNamedTypeContext.class,0);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public ConversionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitConversion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConversionContext conversion() throws RecognitionException {
		ConversionContext _localctx = new ConversionContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1706);
			nonNamedType();
			setState(1707);
			match(L_PAREN);
			setState(1708);
			expression(0);
			setState(1710);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1709);
				match(COMMA);
				}
			}

			setState(1712);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonNamedTypeContext extends ParserRuleContext {
		public TypeLitContext typeLit() {
			return getRuleContext(TypeLitContext.class,0);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public NonNamedTypeContext nonNamedType() {
			return getRuleContext(NonNamedTypeContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public NonNamedTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonNamedType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitNonNamedType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonNamedTypeContext nonNamedType() throws RecognitionException {
		NonNamedTypeContext _localctx = new NonNamedTypeContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_nonNamedType);
		try {
			setState(1719);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRED:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
			case L_BRACKET:
			case STAR:
			case RECEIVE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1714);
				typeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1715);
				match(L_PAREN);
				setState(1716);
				nonNamedType();
				setState(1717);
				match(R_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperandContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public OperandNameContext operandName() {
			return getRuleContext(OperandNameContext.class,0);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public OperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operand; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandContext operand() throws RecognitionException {
		OperandContext _localctx = new OperandContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_operand);
		try {
			setState(1727);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1721);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1722);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1723);
				match(L_PAREN);
				setState(1724);
				expression(0);
				setState(1725);
				match(R_PAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public BasicLitContext basicLit() {
			return getRuleContext(BasicLitContext.class,0);
		}
		public CompositeLitContext compositeLit() {
			return getRuleContext(CompositeLitContext.class,0);
		}
		public FunctionLitContext functionLit() {
			return getRuleContext(FunctionLitContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_literal);
		try {
			setState(1732);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case NIL_LIT:
			case DECIMAL_LIT:
			case BINARY_LIT:
			case OCTAL_LIT:
			case HEX_LIT:
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1729);
				basicLit();
				}
				break;
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
			case ADT:
			case MAP:
			case STRUCT:
			case IDENTIFIER:
			case L_BRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(1730);
				compositeLit();
				}
				break;
			case PRE:
			case PRESERVES:
			case POST:
			case DEC:
			case PURE:
			case TRUSTED:
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1731);
				functionLit();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LIT() { return getToken(GobraParser.DECIMAL_LIT, 0); }
		public TerminalNode BINARY_LIT() { return getToken(GobraParser.BINARY_LIT, 0); }
		public TerminalNode OCTAL_LIT() { return getToken(GobraParser.OCTAL_LIT, 0); }
		public TerminalNode HEX_LIT() { return getToken(GobraParser.HEX_LIT, 0); }
		public TerminalNode IMAGINARY_LIT() { return getToken(GobraParser.IMAGINARY_LIT, 0); }
		public TerminalNode RUNE_LIT() { return getToken(GobraParser.RUNE_LIT, 0); }
		public IntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerContext integer() throws RecognitionException {
		IntegerContext _localctx = new IntegerContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1734);
			_la = _input.LA(1);
			if ( !(((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperandNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public OperandNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operandName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitOperandName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandNameContext operandName() throws RecognitionException {
		OperandNameContext _localctx = new OperandNameContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1736);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedIdentContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(GobraParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(GobraParser.IDENTIFIER, i);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public QualifiedIdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedIdent; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitQualifiedIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedIdentContext qualifiedIdent() throws RecognitionException {
		QualifiedIdentContext _localctx = new QualifiedIdentContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1738);
			match(IDENTIFIER);
			setState(1739);
			match(DOT);
			setState(1740);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompositeLitContext extends ParserRuleContext {
		public LiteralTypeContext literalType() {
			return getRuleContext(LiteralTypeContext.class,0);
		}
		public LiteralValueContext literalValue() {
			return getRuleContext(LiteralValueContext.class,0);
		}
		public CompositeLitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compositeLit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitCompositeLit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompositeLitContext compositeLit() throws RecognitionException {
		CompositeLitContext _localctx = new CompositeLitContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1742);
			literalType();
			setState(1743);
			literalValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralValueContext extends ParserRuleContext {
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public ElementListContext elementList() {
			return getRuleContext(ElementListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public LiteralValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitLiteralValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralValueContext literalValue() throws RecognitionException {
		LiteralValueContext _localctx = new LiteralValueContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1745);
			match(L_CURLY);
			setState(1750);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_CURLY - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1746);
				elementList();
				setState(1748);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1747);
					match(COMMA);
					}
				}

				}
			}

			setState(1752);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementListContext extends ParserRuleContext {
		public List<KeyedElementContext> keyedElement() {
			return getRuleContexts(KeyedElementContext.class);
		}
		public KeyedElementContext keyedElement(int i) {
			return getRuleContext(KeyedElementContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public ElementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitElementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementListContext elementList() throws RecognitionException {
		ElementListContext _localctx = new ElementListContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1754);
			keyedElement();
			setState(1759);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,173,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1755);
					match(COMMA);
					setState(1756);
					keyedElement();
					}
					} 
				}
				setState(1761);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,173,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyedElementContext extends ParserRuleContext {
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public KeyContext key() {
			return getRuleContext(KeyContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public KeyedElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyedElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitKeyedElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyedElementContext keyedElement() throws RecognitionException {
		KeyedElementContext _localctx = new KeyedElementContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1765);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
			case 1:
				{
				setState(1762);
				key();
				setState(1763);
				match(COLON);
				}
				break;
			}
			setState(1767);
			element();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LiteralValueContext literalValue() {
			return getRuleContext(LiteralValueContext.class,0);
		}
		public KeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_key; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyContext key() throws RecognitionException {
		KeyContext _localctx = new KeyContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_key);
		try {
			setState(1771);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case PRE:
			case PRESERVES:
			case POST:
			case DEC:
			case PURE:
			case OLD:
			case BEFORE:
			case FORALL:
			case EXISTS:
			case ACCESS:
			case UNFOLDING:
			case LET:
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case LEN:
			case NEW:
			case MAKE:
			case CAP:
			case SOME:
			case GET:
			case DOM:
			case ADT:
			case MATCH:
			case NONE:
			case PRED:
			case TYPE_OF:
			case IS_COMPARABLE:
			case LOW:
			case WRITEPERM:
			case NOPERM:
			case TRUSTED:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
			case RANGE:
			case TYPE:
			case NIL_LIT:
			case IDENTIFIER:
			case L_PAREN:
			case L_BRACKET:
			case EXCLAMATION:
			case PLUS:
			case MINUS:
			case CARET:
			case STAR:
			case AMPERSAND:
			case RECEIVE:
			case DECIMAL_LIT:
			case BINARY_LIT:
			case OCTAL_LIT:
			case HEX_LIT:
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1769);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1770);
				literalValue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LiteralValueContext literalValue() {
			return getRuleContext(LiteralValueContext.class,0);
		}
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_element);
		try {
			setState(1775);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case PRE:
			case PRESERVES:
			case POST:
			case DEC:
			case PURE:
			case OLD:
			case BEFORE:
			case FORALL:
			case EXISTS:
			case ACCESS:
			case UNFOLDING:
			case LET:
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case LEN:
			case NEW:
			case MAKE:
			case CAP:
			case SOME:
			case GET:
			case DOM:
			case ADT:
			case MATCH:
			case NONE:
			case PRED:
			case TYPE_OF:
			case IS_COMPARABLE:
			case LOW:
			case WRITEPERM:
			case NOPERM:
			case TRUSTED:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
			case RANGE:
			case TYPE:
			case NIL_LIT:
			case IDENTIFIER:
			case L_PAREN:
			case L_BRACKET:
			case EXCLAMATION:
			case PLUS:
			case MINUS:
			case CARET:
			case STAR:
			case AMPERSAND:
			case RECEIVE:
			case DECIMAL_LIT:
			case BINARY_LIT:
			case OCTAL_LIT:
			case HEX_LIT:
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1773);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1774);
				literalValue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StructTypeContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(GobraParser.STRUCT, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<FieldDeclContext> fieldDecl() {
			return getRuleContexts(FieldDeclContext.class);
		}
		public FieldDeclContext fieldDecl(int i) {
			return getRuleContext(FieldDeclContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public StructTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitStructType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructTypeContext structType() throws RecognitionException {
		StructTypeContext _localctx = new StructTypeContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_structType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1777);
			match(STRUCT);
			setState(1778);
			match(L_CURLY);
			setState(1784);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(1779);
				fieldDecl();
				setState(1780);
				eos();
				}
				}
				setState(1786);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1787);
			match(R_CURLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldDeclContext extends ParserRuleContext {
		public String_Context tag;
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public EmbeddedFieldContext embeddedField() {
			return getRuleContext(EmbeddedFieldContext.class,0);
		}
		public String_Context string_() {
			return getRuleContext(String_Context.class,0);
		}
		public FieldDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFieldDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldDeclContext fieldDecl() throws RecognitionException {
		FieldDeclContext _localctx = new FieldDeclContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1793);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				{
				setState(1789);
				identifierList();
				setState(1790);
				type_();
				}
				break;
			case 2:
				{
				setState(1792);
				embeddedField();
				}
				break;
			}
			setState(1796);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
			case 1:
				{
				setState(1795);
				((FieldDeclContext)_localctx).tag = string_();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class String_Context extends ParserRuleContext {
		public TerminalNode RAW_STRING_LIT() { return getToken(GobraParser.RAW_STRING_LIT, 0); }
		public TerminalNode INTERPRETED_STRING_LIT() { return getToken(GobraParser.INTERPRETED_STRING_LIT, 0); }
		public String_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitString_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_Context string_() throws RecognitionException {
		String_Context _localctx = new String_Context(_ctx, getState());
		enterRule(_localctx, 354, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1798);
			_la = _input.LA(1);
			if ( !(_la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EmbeddedFieldContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public EmbeddedFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_embeddedField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitEmbeddedField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmbeddedFieldContext embeddedField() throws RecognitionException {
		EmbeddedFieldContext _localctx = new EmbeddedFieldContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1801);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1800);
				match(STAR);
				}
			}

			setState(1803);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexContext extends ParserRuleContext {
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public IndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexContext index() throws RecognitionException {
		IndexContext _localctx = new IndexContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1805);
			match(L_BRACKET);
			setState(1806);
			expression(0);
			setState(1807);
			match(R_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeAssertionContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TypeAssertionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAssertion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTypeAssertion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeAssertionContext typeAssertion() throws RecognitionException {
		TypeAssertionContext _localctx = new TypeAssertionContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1809);
			match(DOT);
			setState(1810);
			match(L_PAREN);
			setState(1811);
			type_();
			setState(1812);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public NonNamedTypeContext nonNamedType() {
			return getRuleContext(NonNamedTypeContext.class,0);
		}
		public TerminalNode ELLIPSIS() { return getToken(GobraParser.ELLIPSIS, 0); }
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1814);
			match(L_PAREN);
			setState(1829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << LET) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << LOW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (WRITEPERM - 66)) | (1L << (NOPERM - 66)) | (1L << (TRUSTED - 66)) | (1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1821);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,182,_ctx) ) {
				case 1:
					{
					setState(1815);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1816);
					nonNamedType();
					setState(1819);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,181,_ctx) ) {
					case 1:
						{
						setState(1817);
						match(COMMA);
						setState(1818);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1824);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1823);
					match(ELLIPSIS);
					}
				}

				setState(1827);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1826);
					match(COMMA);
					}
				}

				}
			}

			setState(1831);
			match(R_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodExprContext extends ParserRuleContext {
		public NonNamedTypeContext nonNamedType() {
			return getRuleContext(NonNamedTypeContext.class,0);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public MethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodExprContext methodExpr() throws RecognitionException {
		MethodExprContext _localctx = new MethodExprContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1833);
			nonNamedType();
			setState(1834);
			match(DOT);
			setState(1835);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReceiverTypeContext extends ParserRuleContext {
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public ReceiverTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_receiverType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitReceiverType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReceiverTypeContext receiverType() throws RecognitionException {
		ReceiverTypeContext _localctx = new ReceiverTypeContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1837);
			type_();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EosContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public TerminalNode EOF() { return getToken(GobraParser.EOF, 0); }
		public TerminalNode EOS() { return getToken(GobraParser.EOS, 0); }
		public EosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eos; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitEos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EosContext eos() throws RecognitionException {
		EosContext _localctx = new EosContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_eos);
		try {
			setState(1843);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,186,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1839);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1840);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1841);
				match(EOS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1842);
				if (!(closingBracket())) throw new FailedPredicateException(this, "closingBracket()");
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 82:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 90:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 184:
			return eos_sempred((EosContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 11);
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 9);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 8);
		}
		return true;
	}
	private boolean primaryExpr_sempred(PrimaryExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return precpred(_ctx, 9);
		case 11:
			return precpred(_ctx, 8);
		case 12:
			return precpred(_ctx, 7);
		case 13:
			return precpred(_ctx, 6);
		case 14:
			return precpred(_ctx, 5);
		case 15:
			return precpred(_ctx, 4);
		case 16:
			return precpred(_ctx, 3);
		case 17:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean eos_sempred(EosContext _localctx, int predIndex) {
		switch (predIndex) {
		case 18:
			return closingBracket();
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a3\u0738\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"+
		"\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d\4\u008e"+
		"\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\4\u0092\t\u0092"+
		"\4\u0093\t\u0093\4\u0094\t\u0094\4\u0095\t\u0095\4\u0096\t\u0096\4\u0097"+
		"\t\u0097\4\u0098\t\u0098\4\u0099\t\u0099\4\u009a\t\u009a\4\u009b\t\u009b"+
		"\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f\4\u00a0"+
		"\t\u00a0\4\u00a1\t\u00a1\4\u00a2\t\u00a2\4\u00a3\t\u00a3\4\u00a4\t\u00a4"+
		"\4\u00a5\t\u00a5\4\u00a6\t\u00a6\4\u00a7\t\u00a7\4\u00a8\t\u00a8\4\u00a9"+
		"\t\u00a9\4\u00aa\t\u00aa\4\u00ab\t\u00ab\4\u00ac\t\u00ac\4\u00ad\t\u00ad"+
		"\4\u00ae\t\u00ae\4\u00af\t\u00af\4\u00b0\t\u00b0\4\u00b1\t\u00b1\4\u00b2"+
		"\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\4\u00b5\t\u00b5\4\u00b6\t\u00b6"+
		"\4\u00b7\t\u00b7\4\u00b8\t\u00b8\4\u00b9\t\u00b9\4\u00ba\t\u00ba\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u0181\n\5\f\5\16\5\u0184"+
		"\13\5\3\6\3\6\5\6\u0188\n\6\3\7\3\7\3\7\7\7\u018d\n\7\f\7\16\7\u0190\13"+
		"\7\3\7\3\7\3\7\3\7\3\7\7\7\u0197\n\7\f\7\16\7\u019a\13\7\3\7\3\7\3\7\5"+
		"\7\u019f\n\7\3\7\3\7\7\7\u01a3\n\7\f\7\16\7\u01a6\13\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\t\3\t\3\t\3\n\3\n\3\n\7\n\u01b3\n\n\f\n\16\n\u01b6\13\n\3\n\5\n"+
		"\u01b9\n\n\3\n\3\n\3\13\3\13\3\13\7\13\u01c0\n\13\f\13\16\13\u01c3\13"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u01cc\n\13\f\13\16\13\u01cf"+
		"\13\13\3\13\5\13\u01d2\n\13\3\f\3\f\3\f\3\f\5\f\u01d8\n\f\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\5\r\u01e1\n\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\5\20\u01eb\n\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\5\21\u01fd\n\21\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\7\24\u0209\n\24\f\24\16\24\u020c\13\24\3\24\5\24"+
		"\u020f\n\24\3\25\3\25\3\25\7\25\u0214\n\25\f\25\16\25\u0217\13\25\3\25"+
		"\3\25\3\26\7\26\u021c\n\26\f\26\16\26\u021f\13\26\3\27\3\27\3\27\3\27"+
		"\7\27\u0225\n\27\f\27\16\27\u0228\13\27\3\27\3\27\3\30\3\30\3\31\3\31"+
		"\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34"+
		"\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\5\35\u0247\n\35\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\5\36\u024f\n\36\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3"+
		"!\3!\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\5$\u026c\n$"+
		"\3$\3$\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\7&\u027d\n&\f&\16&\u0280"+
		"\13&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\7(\u028c\n(\f(\16(\u028f\13(\3"+
		"(\3(\3)\3)\3)\3)\3*\3*\3*\3*\5*\u029b\n*\3+\3+\3+\3+\3+\7+\u02a2\n+\f"+
		"+\16+\u02a5\13+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3,\5,\u02b2\n,\3-\3-\3-"+
		"\3-\3-\7-\u02b9\n-\f-\16-\u02bc\13-\3-\3-\3.\3.\3.\3.\3.\7.\u02c5\n.\f"+
		".\16.\u02c8\13.\3.\3.\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\5\60\u02dc\n\60\3\61\3\61\3\61\3\61\3\61\5\61\u02e3"+
		"\n\61\3\61\7\61\u02e6\n\61\f\61\16\61\u02e9\13\61\3\61\3\61\5\61\u02ed"+
		"\n\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u02f7\n\62\3\63\5\63"+
		"\u02fa\n\63\3\63\3\63\5\63\u02fe\n\63\3\64\3\64\5\64\u0302\n\64\3\65\3"+
		"\65\3\65\3\65\7\65\u0308\n\65\f\65\16\65\u030b\13\65\3\65\3\65\3\66\3"+
		"\66\3\66\5\66\u0312\n\66\3\67\3\67\3\67\5\67\u0317\n\67\38\38\38\38\3"+
		"8\38\58\u031f\n8\58\u0321\n8\38\38\38\58\u0326\n8\39\39\39\79\u032b\n"+
		"9\f9\169\u032e\139\3:\3:\3:\3:\3:\5:\u0335\n:\3:\5:\u0338\n:\3:\3:\3;"+
		"\3;\5;\u033e\n;\3;\3;\3;\5;\u0343\n;\5;\u0345\n;\3;\5;\u0348\n;\3<\3<"+
		"\3<\7<\u034d\n<\f<\16<\u0350\13<\3=\3=\5=\u0354\n=\3=\3=\3>\3>\3>\3>\3"+
		">\3>\3?\3?\3?\3?\3?\3?\3?\7?\u0365\n?\f?\16?\u0368\13?\3?\3?\3?\7?\u036d"+
		"\n?\f?\16?\u0370\13?\3?\5?\u0373\n?\3@\5@\u0376\n@\3@\3@\3@\3@\5@\u037c"+
		"\n@\3A\3A\5A\u0380\nA\3A\5A\u0383\nA\3A\3A\3A\3B\3B\3B\3B\3B\5B\u038d"+
		"\nB\3C\3C\3C\3C\3C\5C\u0394\nC\3D\3D\3D\3D\3D\5D\u039b\nD\3D\3D\3E\3E"+
		"\3E\3E\3E\3F\3F\3F\5F\u03a7\nF\3G\3G\3G\3G\5G\u03ad\nG\3H\3H\3H\3H\3H"+
		"\5H\u03b4\nH\3I\3I\3I\5I\u03b9\nI\3J\3J\3J\3J\5J\u03bf\nJ\3K\3K\3K\3K"+
		"\3K\3L\3L\3L\3L\3L\5L\u03cb\nL\3M\3M\3M\3M\5M\u03d1\nM\3M\3M\5M\u03d5"+
		"\nM\3N\3N\3N\3N\3O\3O\5O\u03dd\nO\3O\3O\5O\u03e1\nO\3O\3O\3P\3P\5P\u03e7"+
		"\nP\3Q\5Q\u03ea\nQ\3Q\3Q\3R\3R\5R\u03f0\nR\3R\3R\3S\5S\u03f5\nS\3S\3S"+
		"\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\5T\u040e"+
		"\nT\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T"+
		"\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\3T\7T\u0431\nT\fT\16T\u0434\13T\3U\3U\3"+
		"U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\5U\u044a\nU\3V\3"+
		"V\3V\3W\3W\3W\5W\u0452\nW\3X\3X\3X\3Y\3Y\3Y\3Y\7Y\u045b\nY\fY\16Y\u045e"+
		"\13Y\3Y\3Y\3Y\3Y\5Y\u0464\nY\3Z\3Z\3Z\3Z\3Z\5Z\u046b\nZ\3[\3[\3[\3[\3"+
		"[\3[\3[\3[\5[\u0475\n[\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\"+
		"\5\\\u0483\n\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\"+
		"\3\\\3\\\3\\\3\\\3\\\3\\\7\\\u0499\n\\\f\\\16\\\u049c\13\\\3]\3]\3]\3"+
		"^\3^\5^\u04a3\n^\3^\3^\5^\u04a7\n^\3_\3_\5_\u04ab\n_\3_\5_\u04ae\n_\3"+
		"_\3_\3`\3`\3`\3`\3`\5`\u04b7\n`\3`\3`\7`\u04bb\n`\f`\16`\u04be\13`\3`"+
		"\3`\3a\3a\3a\3a\3b\5b\u04c7\nb\3b\3b\3b\3b\3b\3b\5b\u04cf\nb\3b\3b\3b"+
		"\3b\5b\u04d5\nb\3c\3c\3c\3c\3c\3c\3c\5c\u04de\nc\3d\3d\3d\3d\3d\3d\3d"+
		"\3d\3d\5d\u04e9\nd\3e\3e\3e\3f\3f\3f\3f\7f\u04f2\nf\ff\16f\u04f5\13f\3"+
		"f\5f\u04f8\nf\5f\u04fa\nf\3f\3f\3g\3g\3g\3g\3g\3g\3g\5g\u0505\ng\3h\3"+
		"h\3h\3h\3h\3i\3i\5i\u050e\ni\3i\3i\5i\u0512\ni\3i\5i\u0515\ni\3i\3i\3"+
		"i\3i\3i\5i\u051c\ni\3i\3i\3j\3j\3k\3k\3l\3l\3m\5m\u0527\nm\3m\3m\3n\3"+
		"n\3n\3n\3n\3n\5n\u0531\nn\3n\3n\3n\3n\5n\u0537\nn\5n\u0539\nn\3o\3o\3"+
		"o\3p\3p\3q\3q\3q\5q\u0543\nq\3r\3r\3r\3r\3r\3r\7r\u054b\nr\fr\16r\u054e"+
		"\13r\3r\5r\u0551\nr\3s\3s\5s\u0555\ns\3s\3s\5s\u0559\ns\3t\3t\3t\7t\u055e"+
		"\nt\ft\16t\u0561\13t\3u\3u\3u\7u\u0566\nu\fu\16u\u0569\13u\3v\3v\3v\3"+
		"v\3v\3v\7v\u0571\nv\fv\16v\u0574\13v\3v\5v\u0577\nv\3w\3w\5w\u057b\nw"+
		"\3w\3w\3x\3x\3x\3x\3x\3x\7x\u0585\nx\fx\16x\u0588\13x\3x\5x\u058b\nx\3"+
		"y\3y\5y\u058f\ny\3y\3y\3z\5z\u0594\nz\3z\3z\3z\6z\u0599\nz\rz\16z\u059a"+
		"\3{\3{\3{\3{\3{\5{\u05a2\n{\3|\3|\3}\3}\3}\3}\3~\3~\3~\3\177\3\177\3\177"+
		"\3\177\3\u0080\3\u0080\3\u0081\3\u0081\3\u0081\5\u0081\u05b6\n\u0081\3"+
		"\u0082\3\u0082\5\u0082\u05ba\n\u0082\3\u0083\3\u0083\5\u0083\u05be\n\u0083"+
		"\3\u0084\3\u0084\5\u0084\u05c2\n\u0084\3\u0085\3\u0085\3\u0085\3\u0086"+
		"\3\u0086\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087"+
		"\3\u0087\5\u0087\u05d2\n\u0087\3\u0087\3\u0087\3\u0087\3\u0087\5\u0087"+
		"\u05d8\n\u0087\5\u0087\u05da\n\u0087\3\u0088\3\u0088\5\u0088\u05de\n\u0088"+
		"\3\u0089\3\u0089\5\u0089\u05e2\n\u0089\3\u0089\5\u0089\u05e5\n\u0089\3"+
		"\u0089\3\u0089\5\u0089\u05e9\n\u0089\5\u0089\u05eb\n\u0089\3\u0089\3\u0089"+
		"\7\u0089\u05ef\n\u0089\f\u0089\16\u0089\u05f2\13\u0089\3\u0089\3\u0089"+
		"\3\u008a\3\u008a\3\u008a\5\u008a\u05f9\n\u008a\3\u008b\3\u008b\3\u008b"+
		"\5\u008b\u05fe\n\u008b\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c"+
		"\3\u008c\3\u008c\3\u008c\5\u008c\u0609\n\u008c\3\u008c\3\u008c\7\u008c"+
		"\u060d\n\u008c\f\u008c\16\u008c\u0610\13\u008c\3\u008c\3\u008c\3\u008d"+
		"\3\u008d\5\u008d\u0616\n\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d"+
		"\3\u008d\3\u008e\3\u008e\3\u008e\5\u008e\u0621\n\u008e\3\u008f\3\u008f"+
		"\3\u008f\5\u008f\u0626\n\u008f\3\u0090\3\u0090\5\u0090\u062a\n\u0090\3"+
		"\u0090\3\u0090\3\u0090\5\u0090\u062f\n\u0090\7\u0090\u0631\n\u0090\f\u0090"+
		"\16\u0090\u0634\13\u0090\3\u0091\3\u0091\3\u0091\7\u0091\u0639\n\u0091"+
		"\f\u0091\16\u0091\u063c\13\u0091\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092"+
		"\5\u0092\u0643\n\u0092\3\u0093\3\u0093\3\u0093\5\u0093\u0648\n\u0093\3"+
		"\u0093\5\u0093\u064b\n\u0093\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3"+
		"\u0094\5\u0094\u0653\n\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3\u0095\3"+
		"\u0095\5\u0095\u065b\n\u0095\3\u0095\3\u0095\3\u0096\5\u0096\u0660\n\u0096"+
		"\3\u0096\3\u0096\5\u0096\u0664\n\u0096\3\u0096\3\u0096\5\u0096\u0668\n"+
		"\u0096\3\u0097\3\u0097\3\u0097\3\u0098\3\u0098\5\u0098\u066f\n\u0098\3"+
		"\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u009a\3\u009a\3\u009b\3\u009b"+
		"\3\u009c\3\u009c\3\u009c\3\u009d\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e"+
		"\3\u009e\3\u009e\3\u009e\3\u009e\3\u009f\3\u009f\3\u009f\3\u009f\3\u009f"+
		"\5\u009f\u068c\n\u009f\3\u009f\3\u009f\3\u00a0\3\u00a0\3\u00a0\3\u00a1"+
		"\3\u00a1\3\u00a1\3\u00a1\5\u00a1\u0697\n\u00a1\3\u00a2\3\u00a2\5\u00a2"+
		"\u069b\n\u00a2\3\u00a3\3\u00a3\3\u00a3\3\u00a3\7\u00a3\u06a1\n\u00a3\f"+
		"\u00a3\16\u00a3\u06a4\13\u00a3\3\u00a3\5\u00a3\u06a7\n\u00a3\5\u00a3\u06a9"+
		"\n\u00a3\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4\3\u00a4\5\u00a4\u06b1"+
		"\n\u00a4\3\u00a4\3\u00a4\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5\5\u00a5"+
		"\u06ba\n\u00a5\3\u00a6\3\u00a6\3\u00a6\3\u00a6\3\u00a6\3\u00a6\5\u00a6"+
		"\u06c2\n\u00a6\3\u00a7\3\u00a7\3\u00a7\5\u00a7\u06c7\n\u00a7\3\u00a8\3"+
		"\u00a8\3\u00a9\3\u00a9\3\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00ab\3\u00ab"+
		"\3\u00ab\3\u00ac\3\u00ac\3\u00ac\5\u00ac\u06d7\n\u00ac\5\u00ac\u06d9\n"+
		"\u00ac\3\u00ac\3\u00ac\3\u00ad\3\u00ad\3\u00ad\7\u00ad\u06e0\n\u00ad\f"+
		"\u00ad\16\u00ad\u06e3\13\u00ad\3\u00ae\3\u00ae\3\u00ae\5\u00ae\u06e8\n"+
		"\u00ae\3\u00ae\3\u00ae\3\u00af\3\u00af\5\u00af\u06ee\n\u00af\3\u00b0\3"+
		"\u00b0\5\u00b0\u06f2\n\u00b0\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\7"+
		"\u00b1\u06f9\n\u00b1\f\u00b1\16\u00b1\u06fc\13\u00b1\3\u00b1\3\u00b1\3"+
		"\u00b2\3\u00b2\3\u00b2\3\u00b2\5\u00b2\u0704\n\u00b2\3\u00b2\5\u00b2\u0707"+
		"\n\u00b2\3\u00b3\3\u00b3\3\u00b4\5\u00b4\u070c\n\u00b4\3\u00b4\3\u00b4"+
		"\3\u00b5\3\u00b5\3\u00b5\3\u00b5\3\u00b6\3\u00b6\3\u00b6\3\u00b6\3\u00b6"+
		"\3\u00b7\3\u00b7\3\u00b7\3\u00b7\3\u00b7\5\u00b7\u071e\n\u00b7\5\u00b7"+
		"\u0720\n\u00b7\3\u00b7\5\u00b7\u0723\n\u00b7\3\u00b7\5\u00b7\u0726\n\u00b7"+
		"\5\u00b7\u0728\n\u00b7\3\u00b7\3\u00b7\3\u00b8\3\u00b8\3\u00b8\3\u00b8"+
		"\3\u00b9\3\u00b9\3\u00ba\3\u00ba\3\u00ba\3\u00ba\5\u00ba\u0736\n\u00ba"+
		"\3\u00ba\3\u02e7\4\u00a6\u00b6\u00bb\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"+
		"\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098"+
		"\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0"+
		"\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8"+
		"\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0"+
		"\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8"+
		"\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110"+
		"\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128"+
		"\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138\u013a\u013c\u013e\u0140"+
		"\u0142\u0144\u0146\u0148\u014a\u014c\u014e\u0150\u0152\u0154\u0156\u0158"+
		"\u015a\u015c\u015e\u0160\u0162\u0164\u0166\u0168\u016a\u016c\u016e\u0170"+
		"\u0172\2\25\4\2hhss\3\2\31\32\3\2\7\n\3\2DE\3\2*,\4\2*,..\3\2\u0086\u008c"+
		"\3\2\26\27\4\2\u0081\u0085\u008a\u008b\6\2%%tt\u0080\u0080\u0087\u0089"+
		"\3\2!#\3\2\36 \4\2KLz\177\6\2//\62\62\65\65``\4\2\u0080\u0085\u0087\u008b"+
		"\3\2tu\4\2qq\u00a2\u00a2\4\2\u008d\u0090\u0092\u0093\3\2\u0099\u009a\2"+
		"\u079c\2\u0174\3\2\2\2\4\u0177\3\2\2\2\6\u017a\3\2\2\2\b\u017d\3\2\2\2"+
		"\n\u0185\3\2\2\2\f\u018e\3\2\2\2\16\u01a9\3\2\2\2\20\u01ac\3\2\2\2\22"+
		"\u01b4\3\2\2\2\24\u01c1\3\2\2\2\26\u01d7\3\2\2\2\30\u01e0\3\2\2\2\32\u01e2"+
		"\3\2\2\2\34\u01e4\3\2\2\2\36\u01e7\3\2\2\2 \u01fc\3\2\2\2\"\u01fe\3\2"+
		"\2\2$\u0200\3\2\2\2&\u0205\3\2\2\2(\u0210\3\2\2\2*\u021d\3\2\2\2,\u0220"+
		"\3\2\2\2.\u022b\3\2\2\2\60\u022d\3\2\2\2\62\u0232\3\2\2\2\64\u0237\3\2"+
		"\2\2\66\u023c\3\2\2\28\u0241\3\2\2\2:\u024e\3\2\2\2<\u0250\3\2\2\2>\u0252"+
		"\3\2\2\2@\u0257\3\2\2\2B\u025c\3\2\2\2D\u0261\3\2\2\2F\u0266\3\2\2\2H"+
		"\u026f\3\2\2\2J\u0276\3\2\2\2L\u0283\3\2\2\2N\u0287\3\2\2\2P\u0292\3\2"+
		"\2\2R\u029a\3\2\2\2T\u029c\3\2\2\2V\u02b1\3\2\2\2X\u02b3\3\2\2\2Z\u02bf"+
		"\3\2\2\2\\\u02cb\3\2\2\2^\u02db\3\2\2\2`\u02e7\3\2\2\2b\u02f6\3\2\2\2"+
		"d\u02f9\3\2\2\2f\u0301\3\2\2\2h\u0303\3\2\2\2j\u030e\3\2\2\2l\u0316\3"+
		"\2\2\2n\u0325\3\2\2\2p\u0327\3\2\2\2r\u032f\3\2\2\2t\u033d\3\2\2\2v\u0349"+
		"\3\2\2\2x\u0353\3\2\2\2z\u0357\3\2\2\2|\u035d\3\2\2\2~\u0375\3\2\2\2\u0080"+
		"\u037d\3\2\2\2\u0082\u038c\3\2\2\2\u0084\u038e\3\2\2\2\u0086\u0395\3\2"+
		"\2\2\u0088\u039e\3\2\2\2\u008a\u03a3\3\2\2\2\u008c\u03a8\3\2\2\2\u008e"+
		"\u03ae\3\2\2\2\u0090\u03b5\3\2\2\2\u0092\u03ba\3\2\2\2\u0094\u03c0\3\2"+
		"\2\2\u0096\u03c5\3\2\2\2\u0098\u03cc\3\2\2\2\u009a\u03d6\3\2\2\2\u009c"+
		"\u03da\3\2\2\2\u009e\u03e6\3\2\2\2\u00a0\u03e9\3\2\2\2\u00a2\u03ed\3\2"+
		"\2\2\u00a4\u03f4\3\2\2\2\u00a6\u040d\3\2\2\2\u00a8\u0449\3\2\2\2\u00aa"+
		"\u044b\3\2\2\2\u00ac\u044e\3\2\2\2\u00ae\u0453\3\2\2\2\u00b0\u045c\3\2"+
		"\2\2\u00b2\u046a\3\2\2\2\u00b4\u0474\3\2\2\2\u00b6\u0482\3\2\2\2\u00b8"+
		"\u049d\3\2\2\2\u00ba\u04a0\3\2\2\2\u00bc\u04a8\3\2\2\2\u00be\u04b1\3\2"+
		"\2\2\u00c0\u04c1\3\2\2\2\u00c2\u04d4\3\2\2\2\u00c4\u04dd\3\2\2\2\u00c6"+
		"\u04e8\3\2\2\2\u00c8\u04ea\3\2\2\2\u00ca\u04ed\3\2\2\2\u00cc\u0504\3\2"+
		"\2\2\u00ce\u0506\3\2\2\2\u00d0\u050b\3\2\2\2\u00d2\u051f\3\2\2\2\u00d4"+
		"\u0521\3\2\2\2\u00d6\u0523\3\2\2\2\u00d8\u0526\3\2\2\2\u00da\u0530\3\2"+
		"\2\2\u00dc\u053a\3\2\2\2\u00de\u053d\3\2\2\2\u00e0\u0542\3\2\2\2\u00e2"+
		"\u0544\3\2\2\2\u00e4\u0552\3\2\2\2\u00e6\u055a\3\2\2\2\u00e8\u0562\3\2"+
		"\2\2\u00ea\u056a\3\2\2\2\u00ec\u0578\3\2\2\2\u00ee\u057e\3\2\2\2\u00f0"+
		"\u058c\3\2\2\2\u00f2\u0598\3\2\2\2\u00f4\u05a1\3\2\2\2\u00f6\u05a3\3\2"+
		"\2\2\u00f8\u05a5\3\2\2\2\u00fa\u05a9\3\2\2\2\u00fc\u05ac\3\2\2\2\u00fe"+
		"\u05b0\3\2\2\2\u0100\u05b2\3\2\2\2\u0102\u05b7\3\2\2\2\u0104\u05bb\3\2"+
		"\2\2\u0106\u05bf\3\2\2\2\u0108\u05c3\3\2\2\2\u010a\u05c6\3\2\2\2\u010c"+
		"\u05c8\3\2\2\2\u010e\u05dd\3\2\2\2\u0110\u05df\3\2\2\2\u0112\u05f5\3\2"+
		"\2\2\u0114\u05fd\3\2\2\2\u0116\u05ff\3\2\2\2\u0118\u0615\3\2\2\2\u011a"+
		"\u061d\3\2\2\2\u011c\u0625\3\2\2\2\u011e\u0629\3\2\2\2\u0120\u0635\3\2"+
		"\2\2\u0122\u063f\3\2\2\2\u0124\u064a\3\2\2\2\u0126\u0652\3\2\2\2\u0128"+
		"\u0656\3\2\2\2\u012a\u065f\3\2\2\2\u012c\u0669\3\2\2\2\u012e\u066e\3\2"+
		"\2\2\u0130\u0670\3\2\2\2\u0132\u0675\3\2\2\2\u0134\u0677\3\2\2\2\u0136"+
		"\u0679\3\2\2\2\u0138\u067c\3\2\2\2\u013a\u0680\3\2\2\2\u013c\u068b\3\2"+
		"\2\2\u013e\u068f\3\2\2\2\u0140\u0696\3\2\2\2\u0142\u069a\3\2\2\2\u0144"+
		"\u069c\3\2\2\2\u0146\u06ac\3\2\2\2\u0148\u06b9\3\2\2\2\u014a\u06c1\3\2"+
		"\2\2\u014c\u06c6\3\2\2\2\u014e\u06c8\3\2\2\2\u0150\u06ca\3\2\2\2\u0152"+
		"\u06cc\3\2\2\2\u0154\u06d0\3\2\2\2\u0156\u06d3\3\2\2\2\u0158\u06dc\3\2"+
		"\2\2\u015a\u06e7\3\2\2\2\u015c\u06ed\3\2\2\2\u015e\u06f1\3\2\2\2\u0160"+
		"\u06f3\3\2\2\2\u0162\u0703\3\2\2\2\u0164\u0708\3\2\2\2\u0166\u070b\3\2"+
		"\2\2\u0168\u070f\3\2\2\2\u016a\u0713\3\2\2\2\u016c\u0718\3\2\2\2\u016e"+
		"\u072b\3\2\2\2\u0170\u072f\3\2\2\2\u0172\u0735\3\2\2\2\u0174\u0175\5\u00a6"+
		"T\2\u0175\u0176\7\2\2\3\u0176\3\3\2\2\2\u0177\u0178\5\u00a8U\2\u0178\u0179"+
		"\7\2\2\3\u0179\5\3\2\2\2\u017a\u017b\5\u00c4c\2\u017b\u017c\7\2\2\3\u017c"+
		"\7\3\2\2\2\u017d\u0182\5\n\6\2\u017e\u017f\7p\2\2\u017f\u0181\5\n\6\2"+
		"\u0180\u017e\3\2\2\2\u0181\u0184\3\2\2\2\u0182\u0180\3\2\2\2\u0182\u0183"+
		"\3\2\2\2\u0183\t\3\2\2\2\u0184\u0182\3\2\2\2\u0185\u0187\7h\2\2\u0186"+
		"\u0188\7?\2\2\u0187\u0186\3\2\2\2\u0187\u0188\3\2\2\2\u0188\13\3\2\2\2"+
		"\u0189\u018a\5\16\b\2\u018a\u018b\5\u0172\u00ba\2\u018b\u018d\3\2\2\2"+
		"\u018c\u0189\3\2\2\2\u018d\u0190\3\2\2\2\u018e\u018c\3\2\2\2\u018e\u018f"+
		"\3\2\2\2\u018f\u0191\3\2\2\2\u0190\u018e\3\2\2\2\u0191\u0192\5\u00dco"+
		"\2\u0192\u0198\5\u0172\u00ba\2\u0193\u0194\5\24\13\2\u0194\u0195\5\u0172"+
		"\u00ba\2\u0195\u0197\3\2\2\2\u0196\u0193\3\2\2\2\u0197\u019a\3\2\2\2\u0198"+
		"\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u01a4\3\2\2\2\u019a\u0198\3\2"+
		"\2\2\u019b\u019f\5\u008aF\2\u019c\u019f\5\u00e0q\2\u019d\u019f\5\26\f"+
		"\2\u019e\u019b\3\2\2\2\u019e\u019c\3\2\2\2\u019e\u019d\3\2\2\2\u019f\u01a0"+
		"\3\2\2\2\u01a0\u01a1\5\u0172\u00ba\2\u01a1\u01a3\3\2\2\2\u01a2\u019e\3"+
		"\2\2\2\u01a3\u01a6\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5"+
		"\u01a7\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a7\u01a8\7\2\2\3\u01a8\r\3\2\2\2"+
		"\u01a9\u01aa\7H\2\2\u01aa\u01ab\5\u00a6T\2\u01ab\17\3\2\2\2\u01ac\u01ad"+
		"\7I\2\2\u01ad\u01ae\5\u00a6T\2\u01ae\21\3\2\2\2\u01af\u01b0\5\20\t\2\u01b0"+
		"\u01b1\5\u0172\u00ba\2\u01b1\u01b3\3\2\2\2\u01b2\u01af\3\2\2\2\u01b3\u01b6"+
		"\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b8\3\2\2\2\u01b6"+
		"\u01b4\3\2\2\2\u01b7\u01b9\t\2\2\2\u01b8\u01b7\3\2\2\2\u01b8\u01b9\3\2"+
		"\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\5\u00dep\2\u01bb\23\3\2\2\2\u01bc"+
		"\u01bd\5\20\t\2\u01bd\u01be\5\u0172\u00ba\2\u01be\u01c0\3\2\2\2\u01bf"+
		"\u01bc\3\2\2\2\u01c0\u01c3\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c1\u01c2\3\2"+
		"\2\2\u01c2\u01d1\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c4\u01c5\7d\2\2\u01c5"+
		"\u01d2\5\22\n\2\u01c6\u01c7\7d\2\2\u01c7\u01cd\7i\2\2\u01c8\u01c9\5\22"+
		"\n\2\u01c9\u01ca\5\u0172\u00ba\2\u01ca\u01cc\3\2\2\2\u01cb\u01c8\3\2\2"+
		"\2\u01cc\u01cf\3\2\2\2\u01cd\u01cb\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01d0"+
		"\3\2\2\2\u01cf\u01cd\3\2\2\2\u01d0\u01d2\7j\2\2\u01d1\u01c4\3\2\2\2\u01d1"+
		"\u01c6\3\2\2\2\u01d2\25\3\2\2\2\u01d3\u01d8\5|?\2\u01d4\u01d8\5\u0092"+
		"J\2\u01d5\u01d8\5\u0096L\2\u01d6\u01d8\5\u0090I\2\u01d7\u01d3\3\2\2\2"+
		"\u01d7\u01d4\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d6\3\2\2\2\u01d8\27"+
		"\3\2\2\2\u01d9\u01da\7\35\2\2\u01da\u01e1\5\u00a8U\2\u01db\u01dc\t\3\2"+
		"\2\u01dc\u01e1\5.\30\2\u01dd\u01de\t\4\2\2\u01de\u01e1\5\u00a6T\2\u01df"+
		"\u01e1\5h\65\2\u01e0\u01d9\3\2\2\2\u01e0\u01db\3\2\2\2\u01e0\u01dd\3\2"+
		"\2\2\u01e0\u01df\3\2\2\2\u01e1\31\3\2\2\2\u01e2\u01e3\5\34\17\2\u01e3"+
		"\33\3\2\2\2\u01e4\u01e5\5`\61\2\u01e5\u01e6\5\36\20\2\u01e6\35\3\2\2\2"+
		"\u01e7\u01e8\7G\2\2\u01e8\u01ea\7i\2\2\u01e9\u01eb\5\u00f2z\2\u01ea\u01e9"+
		"\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ed\7j\2\2\u01ed"+
		"\37\3\2\2\2\u01ee\u01fd\5H%\2\u01ef\u01fd\5F$\2\u01f0\u01fd\5D#\2\u01f1"+
		"\u01fd\5$\23\2\u01f2\u01fd\5@!\2\u01f3\u01fd\5B\"\2\u01f4\u01fd\58\35"+
		"\2\u01f5\u01fd\5> \2\u01f6\u01fd\5\66\34\2\u01f7\u01fd\5\62\32\2\u01f8"+
		"\u01fd\5\60\31\2\u01f9\u01fd\5\64\33\2\u01fa\u01fd\5\"\22\2\u01fb\u01fd"+
		"\5J&\2\u01fc\u01ee\3\2\2\2\u01fc\u01ef\3\2\2\2\u01fc\u01f0\3\2\2\2\u01fc"+
		"\u01f1\3\2\2\2\u01fc\u01f2\3\2\2\2\u01fc\u01f3\3\2\2\2\u01fc\u01f4\3\2"+
		"\2\2\u01fc\u01f5\3\2\2\2\u01fc\u01f6\3\2\2\2\u01fc\u01f7\3\2\2\2\u01fc"+
		"\u01f8\3\2\2\2\u01fc\u01f9\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fc\u01fb\3\2"+
		"\2\2\u01fd!\3\2\2\2\u01fe\u01ff\t\5\2\2\u01ff#\3\2\2\2\u0200\u0201\7a"+
		"\2\2\u0201\u0202\7m\2\2\u0202\u0203\5\u00c4c\2\u0203\u0204\7n\2\2\u0204"+
		"%\3\2\2\2\u0205\u020a\5(\25\2\u0206\u0207\7p\2\2\u0207\u0209\5(\25\2\u0208"+
		"\u0206\3\2\2\2\u0209\u020c\3\2\2\2\u020a\u0208\3\2\2\2\u020a\u020b\3\2"+
		"\2\2\u020b\u020e\3\2\2\2\u020c\u020a\3\2\2\2\u020d\u020f\7p\2\2\u020e"+
		"\u020d\3\2\2\2\u020e\u020f\3\2\2\2\u020f\'\3\2\2\2\u0210\u0215\7h\2\2"+
		"\u0211\u0212\7p\2\2\u0212\u0214\7h\2\2\u0213\u0211\3\2\2\2\u0214\u0217"+
		"\3\2\2\2\u0215\u0213\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0218\3\2\2\2\u0217"+
		"\u0215\3\2\2\2\u0218\u0219\5\u0134\u009b\2\u0219)\3\2\2\2\u021a\u021c"+
		"\5,\27\2\u021b\u021a\3\2\2\2\u021c\u021f\3\2\2\2\u021d\u021b\3\2\2\2\u021d"+
		"\u021e\3\2\2\2\u021e+\3\2\2\2\u021f\u021d\3\2\2\2\u0220\u0221\7k\2\2\u0221"+
		"\u0226\5\u00a6T\2\u0222\u0223\7p\2\2\u0223\u0225\5\u00a6T\2\u0224\u0222"+
		"\3\2\2\2\u0225\u0228\3\2\2\2\u0226\u0224\3\2\2\2\u0226\u0227\3\2\2\2\u0227"+
		"\u0229\3\2\2\2\u0228\u0226\3\2\2\2\u0229\u022a\7l\2\2\u022a-\3\2\2\2\u022b"+
		"\u022c\5\u00b6\\\2\u022c/\3\2\2\2\u022d\u022e\7\63\2\2\u022e\u022f\7i"+
		"\2\2\u022f\u0230\5\u00a6T\2\u0230\u0231\7j\2\2\u0231\61\3\2\2\2\u0232"+
		"\u0233\79\2\2\u0233\u0234\7m\2\2\u0234\u0235\5\u00c4c\2\u0235\u0236\7"+
		"n\2\2\u0236\63\3\2\2\2\u0237\u0238\7\64\2\2\u0238\u0239\7i\2\2\u0239\u023a"+
		"\5\u00a6T\2\u023a\u023b\7j\2\2\u023b\65\3\2\2\2\u023c\u023d\t\6\2\2\u023d"+
		"\u023e\7i\2\2\u023e\u023f\5\u00a6T\2\u023f\u0240\7j\2\2\u0240\67\3\2\2"+
		"\2\u0241\u0246\7\23\2\2\u0242\u0243\7m\2\2\u0243\u0244\5:\36\2\u0244\u0245"+
		"\7n\2\2\u0245\u0247\3\2\2\2\u0246\u0242\3\2\2\2\u0246\u0247\3\2\2\2\u0247"+
		"\u0248\3\2\2\2\u0248\u0249\7i\2\2\u0249\u024a\5\u00a6T\2\u024a\u024b\7"+
		"j\2\2\u024b9\3\2\2\2\u024c\u024f\5<\37\2\u024d\u024f\7\25\2\2\u024e\u024c"+
		"\3\2\2\2\u024e\u024d\3\2\2\2\u024f;\3\2\2\2\u0250\u0251\7h\2\2\u0251="+
		"\3\2\2\2\u0252\u0253\7\24\2\2\u0253\u0254\7i\2\2\u0254\u0255\5\u00a6T"+
		"\2\u0255\u0256\7j\2\2\u0256?\3\2\2\2\u0257\u0258\7<\2\2\u0258\u0259\7"+
		"i\2\2\u0259\u025a\5\u00a6T\2\u025a\u025b\7j\2\2\u025bA\3\2\2\2\u025c\u025d"+
		"\7=\2\2\u025d\u025e\7i\2\2\u025e\u025f\5\u00a6T\2\u025f\u0260\7j\2\2\u0260"+
		"C\3\2\2\2\u0261\u0262\7;\2\2\u0262\u0263\7i\2\2\u0263\u0264\5\u00a6T\2"+
		"\u0264\u0265\7j\2\2\u0265E\3\2\2\2\u0266\u0267\7\30\2\2\u0267\u0268\7"+
		"i\2\2\u0268\u026b\5\u00a6T\2\u0269\u026a\7p\2\2\u026a\u026c\5\u00a6T\2"+
		"\u026b\u0269\3\2\2\2\u026b\u026c\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u026e"+
		"\7j\2\2\u026eG\3\2\2\2\u026f\u0270\t\6\2\2\u0270\u0271\7m\2\2\u0271\u0272"+
		"\5\u00a6T\2\u0272\u0273\7@\2\2\u0273\u0274\5\u00a6T\2\u0274\u0275\7n\2"+
		"\2\u0275I\3\2\2\2\u0276\u0277\78\2\2\u0277\u0278\5\u00a6T\2\u0278\u027e"+
		"\7k\2\2\u0279\u027a\5L\'\2\u027a\u027b\5\u0172\u00ba\2\u027b\u027d\3\2"+
		"\2\2\u027c\u0279\3\2\2\2\u027d\u0280\3\2\2\2\u027e\u027c\3\2\2\2\u027e"+
		"\u027f\3\2\2\2\u027f\u0281\3\2\2\2\u0280\u027e\3\2\2\2\u0281\u0282\7l"+
		"\2\2\u0282K\3\2\2\2\u0283\u0284\5l\67\2\u0284\u0285\7r\2\2\u0285\u0286"+
		"\5\u00a6T\2\u0286M\3\2\2\2\u0287\u0288\7m\2\2\u0288\u028d\5P)\2\u0289"+
		"\u028a\7p\2\2\u028a\u028c\5P)\2\u028b\u0289\3\2\2\2\u028c\u028f\3\2\2"+
		"\2\u028d\u028b\3\2\2\2\u028d\u028e\3\2\2\2\u028e\u0290\3\2\2\2\u028f\u028d"+
		"\3\2\2\2\u0290\u0291\7n\2\2\u0291O\3\2\2\2\u0292\u0293\5\u00a6T\2\u0293"+
		"\u0294\7o\2\2\u0294\u0295\5\u00a6T\2\u0295Q\3\2\2\2\u0296\u029b\5^\60"+
		"\2\u0297\u029b\5\\/\2\u0298\u029b\5T+\2\u0299\u029b\5X-\2\u029a\u0296"+
		"\3\2\2\2\u029a\u0297\3\2\2\2\u029a\u0298\3\2\2\2\u029a\u0299\3\2\2\2\u029b"+
		"S\3\2\2\2\u029c\u029d\7\65\2\2\u029d\u02a3\7k\2\2\u029e\u029f\5V,\2\u029f"+
		"\u02a0\5\u0172\u00ba\2\u02a0\u02a2\3\2\2\2\u02a1\u029e\3\2\2\2\u02a2\u02a5"+
		"\3\2\2\2\u02a3\u02a1\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a6\3\2\2\2\u02a5"+
		"\u02a3\3\2\2\2\u02a6\u02a7\7l\2\2\u02a7U\3\2\2\2\u02a8\u02a9\7P\2\2\u02a9"+
		"\u02aa\7h\2\2\u02aa\u02b2\5\u0140\u00a1\2\u02ab\u02ac\7\66\2\2\u02ac\u02ad"+
		"\7k\2\2\u02ad\u02ae\5\u00a6T\2\u02ae\u02af\5\u0172\u00ba\2\u02af\u02b0"+
		"\7l\2\2\u02b0\u02b2\3\2\2\2\u02b1\u02a8\3\2\2\2\u02b1\u02ab\3\2\2\2\u02b2"+
		"W\3\2\2\2\u02b3\u02b4\7\67\2\2\u02b4\u02ba\7k\2\2\u02b5\u02b6\5Z.\2\u02b6"+
		"\u02b7\5\u0172\u00ba\2\u02b7\u02b9\3\2\2\2\u02b8\u02b5\3\2\2\2\u02b9\u02bc"+
		"\3\2\2\2\u02ba\u02b8\3\2\2\2\u02ba\u02bb\3\2\2\2\u02bb\u02bd\3\2\2\2\u02bc"+
		"\u02ba\3\2\2\2\u02bd\u02be\7l\2\2\u02beY\3\2\2\2\u02bf\u02c0\7h\2\2\u02c0"+
		"\u02c6\7k\2\2\u02c1\u02c2\5\u0162\u00b2\2\u02c2\u02c3\5\u0172\u00ba\2"+
		"\u02c3\u02c5\3\2\2\2\u02c4\u02c1\3\2\2\2\u02c5\u02c8\3\2\2\2\u02c6\u02c4"+
		"\3\2\2\2\u02c6\u02c7\3\2\2\2\u02c7\u02c9\3\2\2\2\u02c8\u02c6\3\2\2\2\u02c9"+
		"\u02ca\7l\2\2\u02ca[\3\2\2\2\u02cb\u02cc\7\35\2\2\u02cc\u02cd\7m\2\2\u02cd"+
		"\u02ce\7n\2\2\u02ce\u02cf\5\u0134\u009b\2\u02cf]\3\2\2\2\u02d0\u02d1\t"+
		"\7\2\2\u02d1\u02d2\7m\2\2\u02d2\u02d3\5\u00c4c\2\u02d3\u02d4\7n\2\2\u02d4"+
		"\u02dc\3\2\2\2\u02d5\u02d6\7-\2\2\u02d6\u02d7\7m\2\2\u02d7\u02d8\5\u00c4"+
		"c\2\u02d8\u02d9\7n\2\2\u02d9\u02da\5\u00c4c\2\u02da\u02dc\3\2\2\2\u02db"+
		"\u02d0\3\2\2\2\u02db\u02d5\3\2\2\2\u02dc_\3\2\2\2\u02dd\u02e3\5b\62\2"+
		"\u02de\u02df\7\20\2\2\u02df\u02e3\b\61\1\2\u02e0\u02e1\7F\2\2\u02e1\u02e3"+
		"\b\61\1\2\u02e2\u02dd\3\2\2\2\u02e2\u02de\3\2\2\2\u02e2\u02e0\3\2\2\2"+
		"\u02e3\u02e4\3\2\2\2\u02e4\u02e6\5\u0172\u00ba\2\u02e5\u02e2\3\2\2\2\u02e6"+
		"\u02e9\3\2\2\2\u02e7\u02e8\3\2\2\2\u02e7\u02e5\3\2\2\2\u02e8\u02ec\3\2"+
		"\2\2\u02e9\u02e7\3\2\2\2\u02ea\u02eb\7\20\2\2\u02eb\u02ed\b\61\1\2\u02ec"+
		"\u02ea\3\2\2\2\u02ec\u02ed\3\2\2\2\u02eda\3\2\2\2\u02ee\u02ef\7\13\2\2"+
		"\u02ef\u02f7\5f\64\2\u02f0\u02f1\7\f\2\2\u02f1\u02f7\5f\64\2\u02f2\u02f3"+
		"\7\r\2\2\u02f3\u02f7\5f\64\2\u02f4\u02f5\7\17\2\2\u02f5\u02f7\5d\63\2"+
		"\u02f6\u02ee\3\2\2\2\u02f6\u02f0\3\2\2\2\u02f6\u02f2\3\2\2\2\u02f6\u02f4"+
		"\3\2\2\2\u02f7c\3\2\2\2\u02f8\u02fa\5\u00e8u\2\u02f9\u02f8\3\2\2\2\u02f9"+
		"\u02fa\3\2\2\2\u02fa\u02fd\3\2\2\2\u02fb\u02fc\7_\2\2\u02fc\u02fe\5\u00a6"+
		"T\2\u02fd\u02fb\3\2\2\2\u02fd\u02fe\3\2\2\2\u02fee\3\2\2\2\u02ff\u0302"+
		"\3\2\2\2\u0300\u0302\5\u00a6T\2\u0301\u02ff\3\2\2\2\u0301\u0300\3\2\2"+
		"\2\u0302g\3\2\2\2\u0303\u0304\78\2\2\u0304\u0305\5\u00a6T\2\u0305\u0309"+
		"\7k\2\2\u0306\u0308\5j\66\2\u0307\u0306\3\2\2\2\u0308\u030b\3\2\2\2\u0309"+
		"\u0307\3\2\2\2\u0309\u030a\3\2\2\2\u030a\u030c\3\2\2\2\u030b\u0309\3\2"+
		"\2\2\u030c\u030d\7l\2\2\u030di\3\2\2\2\u030e\u030f\5l\67\2\u030f\u0311"+
		"\7r\2\2\u0310\u0312\5\u00f2z\2\u0311\u0310\3\2\2\2\u0311\u0312\3\2\2\2"+
		"\u0312k\3\2\2\2\u0313\u0314\7S\2\2\u0314\u0317\5n8\2\u0315\u0317\7O\2"+
		"\2\u0316\u0313\3\2\2\2\u0316\u0315\3\2\2\2\u0317m\3\2\2\2\u0318\u0319"+
		"\7\'\2\2\u0319\u0326\7h\2\2\u031a\u031b\5\u00ccg\2\u031b\u0320\7k\2\2"+
		"\u031c\u031e\5p9\2\u031d\u031f\7p\2\2\u031e\u031d\3\2\2\2\u031e\u031f"+
		"\3\2\2\2\u031f\u0321\3\2\2\2\u0320\u031c\3\2\2\2\u0320\u0321\3\2\2\2\u0321"+
		"\u0322\3\2\2\2\u0322\u0323\7l\2\2\u0323\u0326\3\2\2\2\u0324\u0326\5\u00a6"+
		"T\2\u0325\u0318\3\2\2\2\u0325\u031a\3\2\2\2\u0325\u0324\3\2\2\2\u0326"+
		"o\3\2\2\2\u0327\u032c\5n8\2\u0328\u0329\7p\2\2\u0329\u032b\5n8\2\u032a"+
		"\u0328\3\2\2\2\u032b\u032e\3\2\2\2\u032c\u032a\3\2\2\2\u032c\u032d\3\2"+
		"\2\2\u032dq\3\2\2\2\u032e\u032c\3\2\2\2\u032f\u0334\7k\2\2\u0330\u0331"+
		"\7>\2\2\u0331\u0332\5\u00e6t\2\u0332\u0333\5\u0172\u00ba\2\u0333\u0335"+
		"\3\2\2\2\u0334\u0330\3\2\2\2\u0334\u0335\3\2\2\2\u0335\u0337\3\2\2\2\u0336"+
		"\u0338\5\u00f2z\2\u0337\u0336\3\2\2\2\u0337\u0338\3\2\2\2\u0338\u0339"+
		"\3\2\2\2\u0339\u033a\7l\2\2\u033as\3\2\2\2\u033b\u033e\5\u0152\u00aa\2"+
		"\u033c\u033e\7h\2\2\u033d\u033b\3\2\2\2\u033d\u033c\3\2\2\2\u033e\u0347"+
		"\3\2\2\2\u033f\u0344\7k\2\2\u0340\u0342\5v<\2\u0341\u0343\7p\2\2\u0342"+
		"\u0341\3\2\2\2\u0342\u0343\3\2\2\2\u0343\u0345\3\2\2\2\u0344\u0340\3\2"+
		"\2\2\u0344\u0345\3\2\2\2\u0345\u0346\3\2\2\2\u0346\u0348\7l\2\2\u0347"+
		"\u033f\3\2\2\2\u0347\u0348\3\2\2\2\u0348u\3\2\2\2\u0349\u034e\5x=\2\u034a"+
		"\u034b\7p\2\2\u034b\u034d\5x=\2\u034c\u034a\3\2\2\2\u034d\u0350\3\2\2"+
		"\2\u034e\u034c\3\2\2\2\u034e\u034f\3\2\2\2\u034fw\3\2\2\2\u0350\u034e"+
		"\3\2\2\2\u0351\u0352\7h\2\2\u0352\u0354\7r\2\2\u0353\u0351\3\2\2\2\u0353"+
		"\u0354\3\2\2\2\u0354\u0355\3\2\2\2\u0355\u0356\5\u00a6T\2\u0356y\3\2\2"+
		"\2\u0357\u0358\7J\2\2\u0358\u0359\5\u00a6T\2\u0359\u035a\7\21\2\2\u035a"+
		"\u035b\5t;\2\u035b\u035c\5\u00f0y\2\u035c{\3\2\2\2\u035d\u035e\5\u00c4"+
		"c\2\u035e\u035f\7\21\2\2\u035f\u0372\5\u00c4c\2\u0360\u0366\7k\2\2\u0361"+
		"\u0362\5\u0084C\2\u0362\u0363\5\u0172\u00ba\2\u0363\u0365\3\2\2\2\u0364"+
		"\u0361\3\2\2\2\u0365\u0368\3\2\2\2\u0366\u0364\3\2\2\2\u0366\u0367\3\2"+
		"\2\2\u0367\u036e\3\2\2\2\u0368\u0366\3\2\2\2\u0369\u036a\5~@\2\u036a\u036b"+
		"\5\u0172\u00ba\2\u036b\u036d\3\2\2\2\u036c\u0369\3\2\2\2\u036d\u0370\3"+
		"\2\2\2\u036e\u036c\3\2\2\2\u036e\u036f\3\2\2\2\u036f\u0371\3\2\2\2\u0370"+
		"\u036e\3\2\2\2\u0371\u0373\7l\2\2\u0372\u0360\3\2\2\2\u0372\u0373\3\2"+
		"\2\2\u0373}\3\2\2\2\u0374\u0376\7\20\2\2\u0375\u0374\3\2\2\2\u0375\u0376"+
		"\3\2\2\2\u0376\u0377\3\2\2\2\u0377\u0378\5\u0080A\2\u0378\u0379\7h\2\2"+
		"\u0379\u037b\5\u0140\u00a1\2\u037a\u037c\5\u00f0y\2\u037b\u037a\3\2\2"+
		"\2\u037b\u037c\3\2\2\2\u037c\177\3\2\2\2\u037d\u037f\7i\2\2\u037e\u0380"+
		"\7h\2\2\u037f\u037e\3\2\2\2\u037f\u0380\3\2\2\2\u0380\u0382\3\2\2\2\u0381"+
		"\u0383\7\u008a\2\2\u0382\u0381\3\2\2\2\u0382\u0383\3\2\2\2\u0383\u0384"+
		"\3\2\2\2\u0384\u0385\5\u012e\u0098\2\u0385\u0386\7j\2\2\u0386\u0081\3"+
		"\2\2\2\u0387\u038d\5\u00b6\\\2\u0388\u0389\5\u00c4c\2\u0389\u038a\7s\2"+
		"\2\u038a\u038b\7h\2\2\u038b\u038d\3\2\2\2\u038c\u0387\3\2\2\2\u038c\u0388"+
		"\3\2\2\2\u038d\u0083\3\2\2\2\u038e\u038f\7:\2\2\u038f\u0390\7h\2\2\u0390"+
		"\u0393\7v\2\2\u0391\u0394\5\u0082B\2\u0392\u0394\5\u0150\u00a9\2\u0393"+
		"\u0391\3\2\2\2\u0393\u0392\3\2\2\2\u0394\u0085\3\2\2\2\u0395\u0396\7\61"+
		"\2\2\u0396\u0397\7i\2\2\u0397\u039a\5\u00c4c\2\u0398\u0399\7p\2\2\u0399"+
		"\u039b\5\u00e8u\2\u039a\u0398\3\2\2\2\u039a\u039b\3\2\2\2\u039b\u039c"+
		"\3\2\2\2\u039c\u039d\7j\2\2\u039d\u0087\3\2\2\2\u039e\u039f\7\60\2\2\u039f"+
		"\u03a0\7i\2\2\u03a0\u03a1\5\u00c4c\2\u03a1\u03a2\7j\2\2\u03a2\u0089\3"+
		"\2\2\2\u03a3\u03a6\5`\61\2\u03a4\u03a7\5\u008cG\2\u03a5\u03a7\5\u008e"+
		"H\2\u03a6\u03a4\3\2\2\2\u03a6\u03a5\3\2\2\2\u03a7\u008b\3\2\2\2\u03a8"+
		"\u03a9\7P\2\2\u03a9\u03aa\7h\2\2\u03aa\u03ac\5\u0140\u00a1\2\u03ab\u03ad"+
		"\5r:\2\u03ac\u03ab\3\2\2\2\u03ac\u03ad\3\2\2\2\u03ad\u008d\3\2\2\2\u03ae"+
		"\u03af\7P\2\2\u03af\u03b0\5\u009cO\2\u03b0\u03b1\7h\2\2\u03b1\u03b3\5"+
		"\u0140\u00a1\2\u03b2\u03b4\5r:\2\u03b3\u03b2\3\2\2\2\u03b3\u03b4\3\2\2"+
		"\2\u03b4\u008f\3\2\2\2\u03b5\u03b8\7\35\2\2\u03b6\u03b9\5\u008aF\2\u03b7"+
		"\u03b9\5\u00e0q\2\u03b8\u03b6\3\2\2\2\u03b8\u03b7\3\2\2\2\u03b9\u0091"+
		"\3\2\2\2\u03ba\u03bb\7:\2\2\u03bb\u03bc\7h\2\2\u03bc\u03be\5\u0144\u00a3"+
		"\2\u03bd\u03bf\5\u0094K\2\u03be\u03bd\3\2\2\2\u03be\u03bf\3\2\2\2\u03bf"+
		"\u0093\3\2\2\2\u03c0\u03c1\7k\2\2\u03c1\u03c2\5\u00a6T\2\u03c2\u03c3\5"+
		"\u0172\u00ba\2\u03c3\u03c4\7l\2\2\u03c4\u0095\3\2\2\2\u03c5\u03c6\7:\2"+
		"\2\u03c6\u03c7\5\u009cO\2\u03c7\u03c8\7h\2\2\u03c8\u03ca\5\u0144\u00a3"+
		"\2\u03c9\u03cb\5\u0094K\2\u03ca\u03c9\3\2\2\2\u03ca\u03cb\3\2\2\2\u03cb"+
		"\u0097\3\2\2\2\u03cc\u03d4\5\b\5\2\u03cd\u03d0\5\u00c4c\2\u03ce\u03cf"+
		"\7o\2\2\u03cf\u03d1\5\u00e8u\2\u03d0\u03ce\3\2\2\2\u03d0\u03d1\3\2\2\2"+
		"\u03d1\u03d5\3\2\2\2\u03d2\u03d3\7o\2\2\u03d3\u03d5\5\u00e8u\2\u03d4\u03cd"+
		"\3\2\2\2\u03d4\u03d2\3\2\2\2\u03d5\u0099\3\2\2\2\u03d6\u03d7\5\b\5\2\u03d7"+
		"\u03d8\7v\2\2\u03d8\u03d9\5\u00e8u\2\u03d9\u009b\3\2\2\2\u03da\u03dc\7"+
		"i\2\2\u03db\u03dd\5\n\6\2\u03dc\u03db\3\2\2\2\u03dc\u03dd\3\2\2\2\u03dd"+
		"\u03de\3\2\2\2\u03de\u03e0\5\u00c4c\2\u03df\u03e1\7p\2\2\u03e0\u03df\3"+
		"\2\2\2\u03e0\u03e1\3\2\2\2\u03e1\u03e2\3\2\2\2\u03e2\u03e3\7j\2\2\u03e3"+
		"\u009d\3\2\2\2\u03e4\u03e7\5\u00a0Q\2\u03e5\u03e7\5\u00a2R\2\u03e6\u03e4"+
		"\3\2\2\2\u03e6\u03e5\3\2\2\2\u03e7\u009f\3\2\2\2\u03e8\u03ea\5\u00e6t"+
		"\2\u03e9\u03e8\3\2\2\2\u03e9\u03ea\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ec"+
		"\5\u00a4S\2\u03ec\u00a1\3\2\2\2\u03ed\u03ef\7\35\2\2\u03ee\u03f0\5\u00e6"+
		"t\2\u03ef\u03ee\3\2\2\2\u03ef\u03f0\3\2\2\2\u03f0\u03f1\3\2\2\2\u03f1"+
		"\u03f2\5\u00a4S\2\u03f2\u00a3\3\2\2\2\u03f3\u03f5\7w\2\2\u03f4\u03f3\3"+
		"\2\2\2\u03f4\u03f5\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6\u03f7\5\u00c4c\2"+
		"\u03f7\u00a5\3\2\2\2\u03f8\u03f9\bT\1\2\u03f9\u03fa\t\b\2\2\u03fa\u040e"+
		"\5\u00a6T\21\u03fb\u040e\5\u00b6\\\2\u03fc\u03fd\7\33\2\2\u03fd\u03fe"+
		"\5.\30\2\u03fe\u03ff\7\36\2\2\u03ff\u0400\5\u00a6T\5\u0400\u040e\3\2\2"+
		"\2\u0401\u0402\7\34\2\2\u0402\u0403\5\u009aN\2\u0403\u0404\7\36\2\2\u0404"+
		"\u0405\5\u00a6T\4\u0405\u040e\3\2\2\2\u0406\u0407\t\t\2\2\u0407\u0408"+
		"\5&\24\2\u0408\u0409\7r\2\2\u0409\u040a\7r\2\2\u040a\u040b\5*\26\2\u040b"+
		"\u040c\5\u00a6T\3\u040c\u040e\3\2\2\2\u040d\u03f8\3\2\2\2\u040d\u03fb"+
		"\3\2\2\2\u040d\u03fc\3\2\2\2\u040d\u0401\3\2\2\2\u040d\u0406\3\2\2\2\u040e"+
		"\u0432\3\2\2\2\u040f\u0410\f\17\2\2\u0410\u0411\t\n\2\2\u0411\u0431\5"+
		"\u00a6T\20\u0412\u0413\f\16\2\2\u0413\u0414\t\13\2\2\u0414\u0431\5\u00a6"+
		"T\17\u0415\u0416\f\r\2\2\u0416\u0417\t\f\2\2\u0417\u0431\5\u00a6T\16\u0418"+
		"\u0419\f\f\2\2\u0419\u041a\t\r\2\2\u041a\u0431\5\u00a6T\r\u041b\u041c"+
		"\f\13\2\2\u041c\u041d\t\16\2\2\u041d\u0431\5\u00a6T\f\u041e\u041f\f\t"+
		"\2\2\u041f\u0420\7y\2\2\u0420\u0431\5\u00a6T\n\u0421\u0422\f\b\2\2\u0422"+
		"\u0423\7x\2\2\u0423\u0431\5\u00a6T\t\u0424\u0425\f\7\2\2\u0425\u0426\7"+
		"$\2\2\u0426\u0431\5\u00a6T\7\u0427\u0428\f\6\2\2\u0428\u0429\7\'\2\2\u0429"+
		"\u042a\5\u00a6T\2\u042a\u042b\7r\2\2\u042b\u042c\5\u00a6T\6\u042c\u0431"+
		"\3\2\2\2\u042d\u042e\f\n\2\2\u042e\u042f\7\21\2\2\u042f\u0431\5t;\2\u0430"+
		"\u040f\3\2\2\2\u0430\u0412\3\2\2\2\u0430\u0415\3\2\2\2\u0430\u0418\3\2"+
		"\2\2\u0430\u041b\3\2\2\2\u0430\u041e\3\2\2\2\u0430\u0421\3\2\2\2\u0430"+
		"\u0424\3\2\2\2\u0430\u0427\3\2\2\2\u0430\u042d\3\2\2\2\u0431\u0434\3\2"+
		"\2\2\u0432\u0430\3\2\2\2\u0432\u0433\3\2\2\2\u0433\u00a7\3\2\2\2\u0434"+
		"\u0432\3\2\2\2\u0435\u044a\5\30\r\2\u0436\u044a\5\32\16\2\u0437\u044a"+
		"\5\u00acW\2\u0438\u044a\5\u00aaV\2\u0439\u044a\5\u00e0q\2\u043a\u044a"+
		"\5\u0100\u0081\2\u043b\u044a\5\u00f4{\2\u043c\u044a\5\u012c\u0097\2\u043d"+
		"\u044a\5\u0102\u0082\2\u043e\u044a\5\u0104\u0083\2\u043f\u044a\5\u0106"+
		"\u0084\2\u0440\u044a\5\u0108\u0085\2\u0441\u044a\5\u010a\u0086\2\u0442"+
		"\u044a\5\u00f0y\2\u0443\u044a\5\u010c\u0087\2\u0444\u044a\5\u010e\u0088"+
		"\2\u0445\u044a\5\u0120\u0091\2\u0446\u044a\5\u00aeX\2\u0447\u044a\5\u00b2"+
		"Z\2\u0448\u044a\5z>\2\u0449\u0435\3\2\2\2\u0449\u0436\3\2\2\2\u0449\u0437"+
		"\3\2\2\2\u0449\u0438\3\2\2\2\u0449\u0439\3\2\2\2\u0449\u043a\3\2\2\2\u0449"+
		"\u043b\3\2\2\2\u0449\u043c\3\2\2\2\u0449\u043d\3\2\2\2\u0449\u043e\3\2"+
		"\2\2\u0449\u043f\3\2\2\2\u0449\u0440\3\2\2\2\u0449\u0441\3\2\2\2\u0449"+
		"\u0442\3\2\2\2\u0449\u0443\3\2\2\2\u0449\u0444\3\2\2\2\u0449\u0445\3\2"+
		"\2\2\u0449\u0446\3\2\2\2\u0449\u0447\3\2\2\2\u0449\u0448\3\2\2\2\u044a"+
		"\u00a9\3\2\2\2\u044b\u044c\7&\2\2\u044c\u044d\5\u00a6T\2\u044d\u00ab\3"+
		"\2\2\2\u044e\u044f\7[\2\2\u044f\u0451\5\u00a6T\2\u0450\u0452\5\u00f0y"+
		"\2\u0451\u0450\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u00ad\3\2\2\2\u0453\u0454"+
		"\5\u00b0Y\2\u0454\u0455\5\u0128\u0095\2\u0455\u00af\3\2\2\2\u0456\u0457"+
		"\7\16\2\2\u0457\u0458\5\u00a6T\2\u0458\u0459\5\u0172\u00ba\2\u0459\u045b"+
		"\3\2\2\2\u045a\u0456\3\2\2\2\u045b\u045e\3\2\2\2\u045c\u045a\3\2\2\2\u045c"+
		"\u045d\3\2\2\2\u045d\u0463\3\2\2\2\u045e\u045c\3\2\2\2\u045f\u0460\7\17"+
		"\2\2\u0460\u0461\5d\63\2\u0461\u0462\5\u0172\u00ba\2\u0462\u0464\3\2\2"+
		"\2\u0463\u045f\3\2\2\2\u0463\u0464\3\2\2\2\u0464\u00b1\3\2\2\2\u0465\u0466"+
		"\7T\2\2\u0466\u046b\5\u00a6T\2\u0467\u0468\7T\2\2\u0468\u0469\t\3\2\2"+
		"\u0469\u046b\5.\30\2\u046a\u0465\3\2\2\2\u046a\u0467\3\2\2\2\u046b\u00b3"+
		"\3\2\2\2\u046c\u0475\7\5\2\2\u046d\u0475\7\6\2\2\u046e\u0475\7g\2\2\u046f"+
		"\u0475\5\u014e\u00a8\2\u0470\u0475\5\u0164\u00b3\2\u0471\u0475\7\3\2\2"+
		"\u0472\u0475\7\u0092\2\2\u0473\u0475\7\u0093\2\2\u0474\u046c\3\2\2\2\u0474"+
		"\u046d\3\2\2\2\u0474\u046e\3\2\2\2\u0474\u046f\3\2\2\2\u0474\u0470\3\2"+
		"\2\2\u0474\u0471\3\2\2\2\u0474\u0472\3\2\2\2\u0474\u0473\3\2\2\2\u0475"+
		"\u00b5\3\2\2\2\u0476\u0477\b\\\1\2\u0477\u0483\5\u014a\u00a6\2\u0478\u0483"+
		"\5\u0146\u00a4\2\u0479\u0483\5\u016e\u00b8\2\u047a\u0483\5 \21\2\u047b"+
		"\u0483\5\u0088E\2\u047c\u0483\5\u0086D\2\u047d\u047e\t\17\2\2\u047e\u047f"+
		"\7i\2\2\u047f\u0480\5\u00a6T\2\u0480\u0481\7j\2\2\u0481\u0483\3\2\2\2"+
		"\u0482\u0476\3\2\2\2\u0482\u0478\3\2\2\2\u0482\u0479\3\2\2\2\u0482\u047a"+
		"\3\2\2\2\u0482\u047b\3\2\2\2\u0482\u047c\3\2\2\2\u0482\u047d\3\2\2\2\u0483"+
		"\u049a\3\2\2\2\u0484\u0485\f\13\2\2\u0485\u0486\7s\2\2\u0486\u0499\7h"+
		"\2\2\u0487\u0488\f\n\2\2\u0488\u0499\5\u0168\u00b5\2\u0489\u048a\f\t\2"+
		"\2\u048a\u0499\5\u00d0i\2\u048b\u048c\f\b\2\2\u048c\u0499\5N(\2\u048d"+
		"\u048e\f\7\2\2\u048e\u0499\5\u016a\u00b6\2\u048f\u0490\f\6\2\2\u0490\u0499"+
		"\5\u016c\u00b7\2\u0491\u0492\f\5\2\2\u0492\u0493\5\u016c\u00b7\2\u0493"+
		"\u0494\7\22\2\2\u0494\u0495\5t;\2\u0495\u0499\3\2\2\2\u0496\u0497\f\4"+
		"\2\2\u0497\u0499\5\u00bc_\2\u0498\u0484\3\2\2\2\u0498\u0487\3\2\2\2\u0498"+
		"\u0489\3\2\2\2\u0498\u048b\3\2\2\2\u0498\u048d\3\2\2\2\u0498\u048f\3\2"+
		"\2\2\u0498\u0491\3\2\2\2\u0498\u0496\3\2\2\2\u0499\u049c\3\2\2\2\u049a"+
		"\u0498\3\2\2\2\u049a\u049b\3\2\2\2\u049b\u00b7\3\2\2\2\u049c\u049a\3\2"+
		"\2\2\u049d\u049e\5`\61\2\u049e\u049f\5\u00ba^\2\u049f\u00b9\3\2\2\2\u04a0"+
		"\u04a2\7P\2\2\u04a1\u04a3\7h\2\2\u04a2\u04a1\3\2\2\2\u04a2\u04a3\3\2\2"+
		"\2\u04a3\u04a4\3\2\2\2\u04a4\u04a6\5\u0140\u00a1\2\u04a5\u04a7\5r:\2\u04a6"+
		"\u04a5\3\2\2\2\u04a6\u04a7\3\2\2\2\u04a7\u00bb\3\2\2\2\u04a8\u04aa\7("+
		"\2\2\u04a9\u04ab\5\u00e8u\2\u04aa\u04a9\3\2\2\2\u04aa\u04ab\3\2\2\2\u04ab"+
		"\u04ad\3\2\2\2\u04ac\u04ae\7p\2\2\u04ad\u04ac\3\2\2\2\u04ad\u04ae\3\2"+
		"\2\2\u04ae\u04af\3\2\2\2\u04af\u04b0\7)\2\2\u04b0\u00bd\3\2\2\2\u04b1"+
		"\u04b2\7Q\2\2\u04b2\u04bc\7k\2\2\u04b3\u04b7\5\u00c2b\2\u04b4\u04b7\5"+
		"\u012e\u0098\2\u04b5\u04b7\5\u00c0a\2\u04b6\u04b3\3\2\2\2\u04b6\u04b4"+
		"\3\2\2\2\u04b6\u04b5\3\2\2\2\u04b7\u04b8\3\2\2\2\u04b8\u04b9\5\u0172\u00ba"+
		"\2\u04b9\u04bb\3\2\2\2\u04ba\u04b6\3\2\2\2\u04bb\u04be\3\2\2\2\u04bc\u04ba"+
		"\3\2\2\2\u04bc\u04bd\3\2\2\2\u04bd\u04bf\3\2\2\2\u04be\u04bc\3\2\2\2\u04bf"+
		"\u04c0\7l\2\2\u04c0\u00bf\3\2\2\2\u04c1\u04c2\7:\2\2\u04c2\u04c3\7h\2"+
		"\2\u04c3\u04c4\5\u0144\u00a3\2\u04c4\u00c1\3\2\2\2\u04c5\u04c7\7\35\2"+
		"\2\u04c6\u04c5\3\2\2\2\u04c6\u04c7\3\2\2\2\u04c7\u04c8\3\2\2\2\u04c8\u04c9"+
		"\5`\61\2\u04c9\u04ca\7h\2\2\u04ca\u04cb\5\u0144\u00a3\2\u04cb\u04cc\5"+
		"\u0142\u00a2\2\u04cc\u04d5\3\2\2\2\u04cd\u04cf\7\35\2\2\u04ce\u04cd\3"+
		"\2\2\2\u04ce\u04cf\3\2\2\2\u04cf\u04d0\3\2\2\2\u04d0\u04d1\5`\61\2\u04d1"+
		"\u04d2\7h\2\2\u04d2\u04d3\5\u0144\u00a3\2\u04d3\u04d5\3\2\2\2\u04d4\u04c6"+
		"\3\2\2\2\u04d4\u04ce\3\2\2\2\u04d5\u00c3\3\2\2\2\u04d6\u04de\5\u012e\u0098"+
		"\2\u04d7\u04de\5\u00c6d\2\u04d8\u04de\5R*\2\u04d9\u04da\7i\2\2\u04da\u04db"+
		"\5\u00c4c\2\u04db\u04dc\7j\2\2\u04dc\u04de\3\2\2\2\u04dd\u04d6\3\2\2\2"+
		"\u04dd\u04d7\3\2\2\2\u04dd\u04d8\3\2\2\2\u04dd\u04d9\3\2\2\2\u04de\u00c5"+
		"\3\2\2\2\u04df\u04e9\5\u0130\u0099\2\u04e0\u04e9\5\u0160\u00b1\2\u04e1"+
		"\u04e9\5\u0136\u009c\2\u04e2\u04e9\5\u013e\u00a0\2\u04e3\u04e9\5\u00be"+
		"`\2\u04e4\u04e9\5\u0138\u009d\2\u04e5\u04e9\5\u013a\u009e\2\u04e6\u04e9"+
		"\5\u013c\u009f\2\u04e7\u04e9\5\u00c8e\2\u04e8\u04df\3\2\2\2\u04e8\u04e0"+
		"\3\2\2\2\u04e8\u04e1\3\2\2\2\u04e8\u04e2\3\2\2\2\u04e8\u04e3\3\2\2\2\u04e8"+
		"\u04e4\3\2\2\2\u04e8\u04e5\3\2\2\2\u04e8\u04e6\3\2\2\2\u04e8\u04e7\3\2"+
		"\2\2\u04e9\u00c7\3\2\2\2\u04ea\u04eb\7:\2\2\u04eb\u04ec\5\u00caf\2\u04ec"+
		"\u00c9\3\2\2\2\u04ed\u04f9\7i\2\2\u04ee\u04f3\5\u00c4c\2\u04ef\u04f0\7"+
		"p\2\2\u04f0\u04f2\5\u00c4c\2\u04f1\u04ef\3\2\2\2\u04f2\u04f5\3\2\2\2\u04f3"+
		"\u04f1\3\2\2\2\u04f3\u04f4\3\2\2\2\u04f4\u04f7\3\2\2\2\u04f5\u04f3\3\2"+
		"\2\2\u04f6\u04f8\7p\2\2\u04f7\u04f6\3\2\2\2\u04f7\u04f8\3\2\2\2\u04f8"+
		"\u04fa\3\2\2\2\u04f9\u04ee\3\2\2\2\u04f9\u04fa\3\2\2\2\u04fa\u04fb\3\2"+
		"\2\2\u04fb\u04fc\7j\2\2\u04fc\u00cb\3\2\2\2\u04fd\u0505\5\u0160\u00b1"+
		"\2\u04fe\u0505\5\u0130\u0099\2\u04ff\u0505\5\u00ceh\2\u0500\u0505\5\u0138"+
		"\u009d\2\u0501\u0505\5\u013a\u009e\2\u0502\u0505\5R*\2\u0503\u0505\5\u012e"+
		"\u0098\2\u0504\u04fd\3\2\2\2\u0504\u04fe\3\2\2\2\u0504\u04ff\3\2\2\2\u0504"+
		"\u0500\3\2\2\2\u0504\u0501\3\2\2\2\u0504\u0502\3\2\2\2\u0504\u0503\3\2"+
		"\2\2\u0505\u00cd\3\2\2\2\u0506\u0507\7m\2\2\u0507\u0508\7w\2\2\u0508\u0509"+
		"\7n\2\2\u0509\u050a\5\u0134\u009b\2\u050a\u00cf\3\2\2\2\u050b\u051b\7"+
		"m\2\2\u050c\u050e\5\u00d2j\2\u050d\u050c\3\2\2\2\u050d\u050e\3\2\2\2\u050e"+
		"\u050f\3\2\2\2\u050f\u0511\7r\2\2\u0510\u0512\5\u00d4k\2\u0511\u0510\3"+
		"\2\2\2\u0511\u0512\3\2\2\2\u0512\u051c\3\2\2\2\u0513\u0515\5\u00d2j\2"+
		"\u0514\u0513\3\2\2\2\u0514\u0515\3\2\2\2\u0515\u0516\3\2\2\2\u0516\u0517"+
		"\7r\2\2\u0517\u0518\5\u00d4k\2\u0518\u0519\7r\2\2\u0519\u051a\5\u00d6"+
		"l\2\u051a\u051c\3\2\2\2\u051b\u050d\3\2\2\2\u051b\u0514\3\2\2\2\u051c"+
		"\u051d\3\2\2\2\u051d\u051e\7n\2\2\u051e\u00d1\3\2\2\2\u051f\u0520\5\u00a6"+
		"T\2\u0520\u00d3\3\2\2\2\u0521\u0522\5\u00a6T\2\u0522\u00d5\3\2\2\2\u0523"+
		"\u0524\5\u00a6T\2\u0524\u00d7\3\2\2\2\u0525\u0527\t\20\2\2\u0526\u0525"+
		"\3\2\2\2\u0526\u0527\3\2\2\2\u0527\u0528\3\2\2\2\u0528\u0529\7o\2\2\u0529"+
		"\u00d9\3\2\2\2\u052a\u052b\5\u00e8u\2\u052b\u052c\7o\2\2\u052c\u0531\3"+
		"\2\2\2\u052d\u052e\5\b\5\2\u052e\u052f\7v\2\2\u052f\u0531\3\2\2\2\u0530"+
		"\u052a\3\2\2\2\u0530\u052d\3\2\2\2\u0530\u0531\3\2\2\2\u0531\u0532\3\2"+
		"\2\2\u0532\u0533\7`\2\2\u0533\u0538\5\u00a6T\2\u0534\u0536\7M\2\2\u0535"+
		"\u0537\7h\2\2\u0536\u0535\3\2\2\2\u0536\u0537\3\2\2\2\u0537\u0539\3\2"+
		"\2\2\u0538\u0534\3\2\2\2\u0538\u0539\3\2\2\2\u0539\u00db\3\2\2\2\u053a"+
		"\u053b\7[\2\2\u053b\u053c\7h\2\2\u053c\u00dd\3\2\2\2\u053d\u053e\5\u0164"+
		"\u00b3\2\u053e\u00df\3\2\2\2\u053f\u0543\5\u00e2r\2\u0540\u0543\5\u00ea"+
		"v\2\u0541\u0543\5\u00eex\2\u0542\u053f\3\2\2\2\u0542\u0540\3\2\2\2\u0542"+
		"\u0541\3\2\2\2\u0543\u00e1\3\2\2\2\u0544\u0550\7]\2\2\u0545\u0551\5\u00e4"+
		"s\2\u0546\u054c\7i\2\2\u0547\u0548\5\u00e4s\2\u0548\u0549\5\u0172\u00ba"+
		"\2\u0549\u054b\3\2\2\2\u054a\u0547\3\2\2\2\u054b\u054e\3\2\2\2\u054c\u054a"+
		"\3\2\2\2\u054c\u054d\3\2\2\2\u054d\u054f\3\2\2\2\u054e\u054c\3\2\2\2\u054f"+
		"\u0551\7j\2\2\u0550\u0545\3\2\2\2\u0550\u0546\3\2\2\2\u0551\u00e3\3\2"+
		"\2\2\u0552\u0558\5\u00e6t\2\u0553\u0555\5\u00c4c\2\u0554\u0553\3\2\2\2"+
		"\u0554\u0555\3\2\2\2\u0555\u0556\3\2\2\2\u0556\u0557\7o\2\2\u0557\u0559"+
		"\5\u00e8u\2\u0558\u0554\3\2\2\2\u0558\u0559\3\2\2\2\u0559\u00e5\3\2\2"+
		"\2\u055a\u055f\7h\2\2\u055b\u055c\7p\2\2\u055c\u055e\7h\2\2\u055d\u055b"+
		"\3\2\2\2\u055e\u0561\3\2\2\2\u055f\u055d\3\2\2\2\u055f\u0560\3\2\2\2\u0560"+
		"\u00e7\3\2\2\2\u0561\u055f\3\2\2\2\u0562\u0567\5\u00a6T\2\u0563\u0564"+
		"\7p\2\2\u0564\u0566\5\u00a6T\2\u0565\u0563\3\2\2\2\u0566\u0569\3\2\2\2"+
		"\u0567\u0565\3\2\2\2\u0567\u0568\3\2\2\2\u0568\u00e9\3\2\2\2\u0569\u0567"+
		"\3\2\2\2\u056a\u0576\7a\2\2\u056b\u0577\5\u00ecw\2\u056c\u0572\7i\2\2"+
		"\u056d\u056e\5\u00ecw\2\u056e\u056f\5\u0172\u00ba\2\u056f\u0571\3\2\2"+
		"\2\u0570\u056d\3\2\2\2\u0571\u0574\3\2\2\2\u0572\u0570\3\2\2\2\u0572\u0573"+
		"\3\2\2\2\u0573\u0575\3\2\2\2\u0574\u0572\3\2\2\2\u0575\u0577\7j\2\2\u0576"+
		"\u056b\3\2\2\2\u0576\u056c\3\2\2\2\u0577\u00eb\3\2\2\2\u0578\u057a\7h"+
		"\2\2\u0579\u057b\7o\2\2\u057a\u0579\3\2\2\2\u057a\u057b\3\2\2\2\u057b"+
		"\u057c\3\2\2\2\u057c\u057d\5\u00c4c\2\u057d\u00ed\3\2\2\2\u057e\u058a"+
		"\7f\2\2\u057f\u058b\5\u0098M\2\u0580\u0586\7i\2\2\u0581\u0582\5\u0098"+
		"M\2\u0582\u0583\5\u0172\u00ba\2\u0583\u0585\3\2\2\2\u0584\u0581\3\2\2"+
		"\2\u0585\u0588\3\2\2\2\u0586\u0584\3\2\2\2\u0586\u0587\3\2\2\2\u0587\u0589"+
		"\3\2\2\2\u0588\u0586\3\2\2\2\u0589\u058b\7j\2\2\u058a\u057f\3\2\2\2\u058a"+
		"\u0580\3\2\2\2\u058b\u00ef\3\2\2\2\u058c\u058e\7k\2\2\u058d\u058f\5\u00f2"+
		"z\2\u058e\u058d\3\2\2\2\u058e\u058f\3\2\2\2\u058f\u0590\3\2\2\2\u0590"+
		"\u0591\7l\2\2\u0591\u00f1\3\2\2\2\u0592\u0594\5\u0172\u00ba\2\u0593\u0592"+
		"\3\2\2\2\u0593\u0594\3\2\2\2\u0594\u0595\3\2\2\2\u0595\u0596\5\u00a8U"+
		"\2\u0596\u0597\5\u0172\u00ba\2\u0597\u0599\3\2\2\2\u0598\u0593\3\2\2\2"+
		"\u0599\u059a\3\2\2\2\u059a\u0598\3\2\2\2\u059a\u059b\3\2\2\2\u059b\u00f3"+
		"\3\2\2\2\u059c\u05a2\5\u00f8}\2\u059d\u05a2\5\u00fa~\2\u059e\u05a2\5\u00fc"+
		"\177\2\u059f\u05a2\5\u00f6|\2\u05a0\u05a2\5\u009aN\2\u05a1\u059c\3\2\2"+
		"\2\u05a1\u059d\3\2\2\2\u05a1\u059e\3\2\2\2\u05a1\u059f\3\2\2\2\u05a1\u05a0"+
		"\3\2\2\2\u05a2\u00f5\3\2\2\2\u05a3\u05a4\5\u00a6T\2\u05a4\u00f7\3\2\2"+
		"\2\u05a5\u05a6\5\u00a6T\2\u05a6\u05a7\7\u008c\2\2\u05a7\u05a8\5\u00a6"+
		"T\2\u05a8\u00f9\3\2\2\2\u05a9\u05aa\5\u00a6T\2\u05aa\u05ab\t\21\2\2\u05ab"+
		"\u00fb\3\2\2\2\u05ac\u05ad\5\u00e8u\2\u05ad\u05ae\5\u00d8m\2\u05ae\u05af"+
		"\5\u00e8u\2\u05af\u00fd\3\2\2\2\u05b0\u05b1\t\22\2\2\u05b1\u00ff\3\2\2"+
		"\2\u05b2\u05b3\7h\2\2\u05b3\u05b5\7r\2\2\u05b4\u05b6\5\u00a8U\2\u05b5"+
		"\u05b4\3\2\2\2\u05b5\u05b6\3\2\2\2\u05b6\u0101\3\2\2\2\u05b7\u05b9\7e"+
		"\2\2\u05b8\u05ba\5\u00e8u\2\u05b9\u05b8\3\2\2\2\u05b9\u05ba\3\2\2\2\u05ba"+
		"\u0103\3\2\2\2\u05bb\u05bd\7N\2\2\u05bc\u05be\7h\2\2\u05bd\u05bc\3\2\2"+
		"\2\u05bd\u05be\3\2\2\2\u05be\u0105\3\2\2\2\u05bf\u05c1\7b\2\2\u05c0\u05c2"+
		"\7h\2\2\u05c1\u05c0\3\2\2\2\u05c1\u05c2\3\2\2\2\u05c2\u0107\3\2\2\2\u05c3"+
		"\u05c4\7Z\2\2\u05c4\u05c5\7h\2\2\u05c5\u0109\3\2\2\2\u05c6\u05c7\7^\2"+
		"\2\u05c7\u010b\3\2\2\2\u05c8\u05d1\7_\2\2\u05c9\u05d2\5\u00a6T\2\u05ca"+
		"\u05cb\5\u0172\u00ba\2\u05cb\u05cc\5\u00a6T\2\u05cc\u05d2\3\2\2\2\u05cd"+
		"\u05ce\5\u00f4{\2\u05ce\u05cf\5\u0172\u00ba\2\u05cf\u05d0\5\u00a6T\2\u05d0"+
		"\u05d2\3\2\2\2\u05d1\u05c9\3\2\2\2\u05d1\u05ca\3\2\2\2\u05d1\u05cd\3\2"+
		"\2\2\u05d2\u05d3\3\2\2\2\u05d3\u05d9\5\u00f0y\2\u05d4\u05d7\7Y\2\2\u05d5"+
		"\u05d8\5\u010c\u0087\2\u05d6\u05d8\5\u00f0y\2\u05d7\u05d5\3\2\2\2\u05d7"+
		"\u05d6\3\2\2\2\u05d8\u05da\3\2\2\2\u05d9\u05d4\3\2\2\2\u05d9\u05da\3\2"+
		"\2\2\u05da\u010d\3\2\2\2\u05db\u05de\5\u0110\u0089\2\u05dc\u05de\5\u0116"+
		"\u008c\2\u05dd\u05db\3\2\2\2\u05dd\u05dc\3\2\2\2\u05de\u010f\3\2\2\2\u05df"+
		"\u05ea\7\\\2\2\u05e0\u05e2\5\u00a6T\2\u05e1\u05e0\3\2\2\2\u05e1\u05e2"+
		"\3\2\2\2\u05e2\u05eb\3\2\2\2\u05e3\u05e5\5\u00f4{\2\u05e4\u05e3\3\2\2"+
		"\2\u05e4\u05e5\3\2\2\2\u05e5\u05e6\3\2\2\2\u05e6\u05e8\5\u0172\u00ba\2"+
		"\u05e7\u05e9\5\u00a6T\2\u05e8\u05e7\3\2\2\2\u05e8\u05e9\3\2\2\2\u05e9"+
		"\u05eb\3\2\2\2\u05ea\u05e1\3\2\2\2\u05ea\u05e4\3\2\2\2\u05eb\u05ec\3\2"+
		"\2\2\u05ec\u05f0\7k\2\2\u05ed\u05ef\5\u0112\u008a\2\u05ee\u05ed\3\2\2"+
		"\2\u05ef\u05f2\3\2\2\2\u05f0\u05ee\3\2\2\2\u05f0\u05f1\3\2\2\2\u05f1\u05f3"+
		"\3\2\2\2\u05f2\u05f0\3\2\2\2\u05f3\u05f4\7l\2\2\u05f4\u0111\3\2\2\2\u05f5"+
		"\u05f6\5\u0114\u008b\2\u05f6\u05f8\7r\2\2\u05f7\u05f9\5\u00f2z\2\u05f8"+
		"\u05f7\3\2\2\2\u05f8\u05f9\3\2\2\2\u05f9\u0113\3\2\2\2\u05fa\u05fb\7S"+
		"\2\2\u05fb\u05fe\5\u00e8u\2\u05fc\u05fe\7O\2\2\u05fd\u05fa\3\2\2\2\u05fd"+
		"\u05fc\3\2\2\2\u05fe\u0115\3\2\2\2\u05ff\u0608\7\\\2\2\u0600\u0609\5\u0118"+
		"\u008d\2\u0601\u0602\5\u0172\u00ba\2\u0602\u0603\5\u0118\u008d\2\u0603"+
		"\u0609\3\2\2\2\u0604\u0605\5\u00f4{\2\u0605\u0606\5\u0172\u00ba\2\u0606"+
		"\u0607\5\u0118\u008d\2\u0607\u0609\3\2\2\2\u0608\u0600\3\2\2\2\u0608\u0601"+
		"\3\2\2\2\u0608\u0604\3\2\2\2\u0609\u060a\3\2\2\2\u060a\u060e\7k\2\2\u060b"+
		"\u060d\5\u011a\u008e\2\u060c\u060b\3\2\2\2\u060d\u0610\3\2\2\2\u060e\u060c"+
		"\3\2\2\2\u060e\u060f\3\2\2\2\u060f\u0611\3\2\2\2\u0610\u060e\3\2\2\2\u0611"+
		"\u0612\7l\2\2\u0612\u0117\3\2\2\2\u0613\u0614\7h\2\2\u0614\u0616\7v\2"+
		"\2\u0615\u0613\3\2\2\2\u0615\u0616\3\2\2\2\u0616\u0617\3\2\2\2\u0617\u0618"+
		"\5\u00b6\\\2\u0618\u0619\7s\2\2\u0619\u061a\7i\2\2\u061a\u061b\7a\2\2"+
		"\u061b\u061c\7j\2\2\u061c\u0119\3\2\2\2\u061d\u061e\5\u011c\u008f\2\u061e"+
		"\u0620\7r\2\2\u061f\u0621\5\u00f2z\2\u0620\u061f\3\2\2\2\u0620\u0621\3"+
		"\2\2\2\u0621\u011b\3\2\2\2\u0622\u0623\7S\2\2\u0623\u0626\5\u011e\u0090"+
		"\2\u0624\u0626\7O\2\2\u0625\u0622\3\2\2\2\u0625\u0624\3\2\2\2\u0626\u011d"+
		"\3\2\2\2\u0627\u062a\5\u00c4c\2\u0628\u062a\7g\2\2\u0629\u0627\3\2\2\2"+
		"\u0629\u0628\3\2\2\2\u062a\u0632\3\2\2\2\u062b\u062e\7p\2\2\u062c\u062f"+
		"\5\u00c4c\2\u062d\u062f\7g\2\2\u062e\u062c\3\2\2\2\u062e\u062d\3\2\2\2"+
		"\u062f\u0631\3\2\2\2\u0630\u062b\3\2\2\2\u0631\u0634\3\2\2\2\u0632\u0630"+
		"\3\2\2\2\u0632\u0633\3\2\2\2\u0633\u011f\3\2\2\2\u0634\u0632\3\2\2\2\u0635"+
		"\u0636\7R\2\2\u0636\u063a\7k\2\2\u0637\u0639\5\u0122\u0092\2\u0638\u0637"+
		"\3\2\2\2\u0639\u063c\3\2\2\2\u063a\u0638\3\2\2\2\u063a\u063b\3\2\2\2\u063b"+
		"\u063d\3\2\2\2\u063c\u063a\3\2\2\2\u063d\u063e\7l\2\2\u063e\u0121\3\2"+
		"\2\2\u063f\u0640\5\u0124\u0093\2\u0640\u0642\7r\2\2\u0641\u0643\5\u00f2"+
		"z\2\u0642\u0641\3\2\2\2\u0642\u0643\3\2\2\2\u0643\u0123\3\2\2\2\u0644"+
		"\u0647\7S\2\2\u0645\u0648\5\u00f8}\2\u0646\u0648\5\u0126\u0094\2\u0647"+
		"\u0645\3\2\2\2\u0647\u0646\3\2\2\2\u0648\u064b\3\2\2\2\u0649\u064b\7O"+
		"\2\2\u064a\u0644\3\2\2\2\u064a\u0649\3\2\2\2\u064b\u0125\3\2\2\2\u064c"+
		"\u064d\5\u00e8u\2\u064d\u064e\7o\2\2\u064e\u0653\3\2\2\2\u064f\u0650\5"+
		"\u00e6t\2\u0650\u0651\7v\2\2\u0651\u0653\3\2\2\2\u0652\u064c\3\2\2\2\u0652"+
		"\u064f\3\2\2\2\u0652\u0653\3\2\2\2\u0653\u0654\3\2\2\2\u0654\u0655\5\u00a6"+
		"T\2\u0655\u0127\3\2\2\2\u0656\u065a\7c\2\2\u0657\u065b\5\u00a6T\2\u0658"+
		"\u065b\5\u012a\u0096\2\u0659\u065b\5\u00dan\2\u065a\u0657\3\2\2\2\u065a"+
		"\u0658\3\2\2\2\u065a\u0659\3\2\2\2\u065a\u065b\3\2\2\2\u065b\u065c\3\2"+
		"\2\2\u065c\u065d\5\u00f0y\2\u065d\u0129\3\2\2\2\u065e\u0660\5\u00f4{\2"+
		"\u065f\u065e\3\2\2\2\u065f\u0660\3\2\2\2\u0660\u0661\3\2\2\2\u0661\u0663"+
		"\5\u0172\u00ba\2\u0662\u0664\5\u00a6T\2\u0663\u0662\3\2\2\2\u0663\u0664"+
		"\3\2\2\2\u0664\u0665\3\2\2\2\u0665\u0667\5\u0172\u00ba\2\u0666\u0668\5"+
		"\u00f4{\2\u0667\u0666\3\2\2\2\u0667\u0668\3\2\2\2\u0668\u012b\3\2\2\2"+
		"\u0669\u066a\7U\2\2\u066a\u066b\5\u00a6T\2\u066b\u012d\3\2\2\2\u066c\u066f"+
		"\5\u0152\u00aa\2\u066d\u066f\7h\2\2\u066e\u066c\3\2\2\2\u066e\u066d\3"+
		"\2\2\2\u066f\u012f\3\2\2\2\u0670\u0671\7m\2\2\u0671\u0672\5\u0132\u009a"+
		"\2\u0672\u0673\7n\2\2\u0673\u0674\5\u0134\u009b\2\u0674\u0131\3\2\2\2"+
		"\u0675\u0676\5\u00a6T\2\u0676\u0133\3\2\2\2\u0677\u0678\5\u00c4c\2\u0678"+
		"\u0135\3\2\2\2\u0679\u067a\7\u008a\2\2\u067a\u067b\5\u00c4c\2\u067b\u0137"+
		"\3\2\2\2\u067c\u067d\7m\2\2\u067d\u067e\7n\2\2\u067e\u067f\5\u0134\u009b"+
		"\2\u067f\u0139\3\2\2\2\u0680\u0681\7V\2\2\u0681\u0682\7m\2\2\u0682\u0683"+
		"\5\u00c4c\2\u0683\u0684\7n\2\2\u0684\u0685\5\u0134\u009b\2\u0685\u013b"+
		"\3\2\2\2\u0686\u068c\7X\2\2\u0687\u0688\7X\2\2\u0688\u068c\7\u008c\2\2"+
		"\u0689\u068a\7\u008c\2\2\u068a\u068c\7X\2\2\u068b\u0686\3\2\2\2\u068b"+
		"\u0687\3\2\2\2\u068b\u0689\3\2\2\2\u068c\u068d\3\2\2\2\u068d\u068e\5\u0134"+
		"\u009b\2\u068e\u013d\3\2\2\2\u068f\u0690\7P\2\2\u0690\u0691\5\u0140\u00a1"+
		"\2\u0691\u013f\3\2\2\2\u0692\u0693\5\u0144\u00a3\2\u0693\u0694\5\u0142"+
		"\u00a2\2\u0694\u0697\3\2\2\2\u0695\u0697\5\u0144\u00a3\2\u0696\u0692\3"+
		"\2\2\2\u0696\u0695\3\2\2\2\u0697\u0141\3\2\2\2\u0698\u069b\5\u0144\u00a3"+
		"\2\u0699\u069b\5\u00c4c\2\u069a\u0698\3\2\2\2\u069a\u0699\3\2\2\2\u069b"+
		"\u0143\3\2\2\2\u069c\u06a8\7i\2\2\u069d\u06a2\5\u009eP\2\u069e\u069f\7"+
		"p\2\2\u069f\u06a1\5\u009eP\2\u06a0\u069e\3\2\2\2\u06a1\u06a4\3\2\2\2\u06a2"+
		"\u06a0\3\2\2\2\u06a2\u06a3\3\2\2\2\u06a3\u06a6\3\2\2\2\u06a4\u06a2\3\2"+
		"\2\2\u06a5\u06a7\7p\2\2\u06a6\u06a5\3\2\2\2\u06a6\u06a7\3\2\2\2\u06a7"+
		"\u06a9\3\2\2\2\u06a8\u069d\3\2\2\2\u06a8\u06a9\3\2\2\2\u06a9\u06aa\3\2"+
		"\2\2\u06aa\u06ab\7j\2\2\u06ab\u0145\3\2\2\2\u06ac\u06ad\5\u0148\u00a5"+
		"\2\u06ad\u06ae\7i\2\2\u06ae\u06b0\5\u00a6T\2\u06af\u06b1\7p\2\2\u06b0"+
		"\u06af\3\2\2\2\u06b0\u06b1\3\2\2\2\u06b1\u06b2\3\2\2\2\u06b2\u06b3\7j"+
		"\2\2\u06b3\u0147\3\2\2\2\u06b4\u06ba\5\u00c6d\2\u06b5\u06b6\7i\2\2\u06b6"+
		"\u06b7\5\u0148\u00a5\2\u06b7\u06b8\7j\2\2\u06b8\u06ba\3\2\2\2\u06b9\u06b4"+
		"\3\2\2\2\u06b9\u06b5\3\2\2\2\u06ba\u0149\3\2\2\2\u06bb\u06c2\5\u014c\u00a7"+
		"\2\u06bc\u06c2\5\u0150\u00a9\2\u06bd\u06be\7i\2\2\u06be\u06bf\5\u00a6"+
		"T\2\u06bf\u06c0\7j\2\2\u06c0\u06c2\3\2\2\2\u06c1\u06bb\3\2\2\2\u06c1\u06bc"+
		"\3\2\2\2\u06c1\u06bd\3\2\2\2\u06c2\u014b\3\2\2\2\u06c3\u06c7\5\u00b4["+
		"\2\u06c4\u06c7\5\u0154\u00ab\2\u06c5\u06c7\5\u00b8]\2\u06c6\u06c3\3\2"+
		"\2\2\u06c6\u06c4\3\2\2\2\u06c6\u06c5\3\2\2\2\u06c7\u014d\3\2\2\2\u06c8"+
		"\u06c9\t\23\2\2\u06c9\u014f\3\2\2\2\u06ca\u06cb\7h\2\2\u06cb\u0151\3\2"+
		"\2\2\u06cc\u06cd\7h\2\2\u06cd\u06ce\7s\2\2\u06ce\u06cf\7h\2\2\u06cf\u0153"+
		"\3\2\2\2\u06d0\u06d1\5\u00ccg\2\u06d1\u06d2\5\u0156\u00ac\2\u06d2\u0155"+
		"\3\2\2\2\u06d3\u06d8\7k\2\2\u06d4\u06d6\5\u0158\u00ad\2\u06d5\u06d7\7"+
		"p\2\2\u06d6\u06d5\3\2\2\2\u06d6\u06d7\3\2\2\2\u06d7\u06d9\3\2\2\2\u06d8"+
		"\u06d4\3\2\2\2\u06d8\u06d9\3\2\2\2\u06d9\u06da\3\2\2\2\u06da\u06db\7l"+
		"\2\2\u06db\u0157\3\2\2\2\u06dc\u06e1\5\u015a\u00ae\2\u06dd\u06de\7p\2"+
		"\2\u06de\u06e0\5\u015a\u00ae\2\u06df\u06dd\3\2\2\2\u06e0\u06e3\3\2\2\2"+
		"\u06e1\u06df\3\2\2\2\u06e1\u06e2\3\2\2\2\u06e2\u0159\3\2\2\2\u06e3\u06e1"+
		"\3\2\2\2\u06e4\u06e5\5\u015c\u00af\2\u06e5\u06e6\7r\2\2\u06e6\u06e8\3"+
		"\2\2\2\u06e7\u06e4\3\2\2\2\u06e7\u06e8\3\2\2\2\u06e8\u06e9\3\2\2\2\u06e9"+
		"\u06ea\5\u015e\u00b0\2\u06ea\u015b\3\2\2\2\u06eb\u06ee\5\u00a6T\2\u06ec"+
		"\u06ee\5\u0156\u00ac\2\u06ed\u06eb\3\2\2\2\u06ed\u06ec\3\2\2\2\u06ee\u015d"+
		"\3\2\2\2\u06ef\u06f2\5\u00a6T\2\u06f0\u06f2\5\u0156\u00ac\2\u06f1\u06ef"+
		"\3\2\2\2\u06f1\u06f0\3\2\2\2\u06f2\u015f\3\2\2\2\u06f3\u06f4\7W\2\2\u06f4"+
		"\u06fa\7k\2\2\u06f5\u06f6\5\u0162\u00b2\2\u06f6\u06f7\5\u0172\u00ba\2"+
		"\u06f7\u06f9\3\2\2\2\u06f8\u06f5\3\2\2\2\u06f9\u06fc\3\2\2\2\u06fa\u06f8"+
		"\3\2\2\2\u06fa\u06fb\3\2\2\2\u06fb\u06fd\3\2\2\2\u06fc\u06fa\3\2\2\2\u06fd"+
		"\u06fe\7l\2\2\u06fe\u0161\3\2\2\2\u06ff\u0700\5\u00e6t\2\u0700\u0701\5"+
		"\u00c4c\2\u0701\u0704\3\2\2\2\u0702\u0704\5\u0166\u00b4\2\u0703\u06ff"+
		"\3\2\2\2\u0703\u0702\3\2\2\2\u0704\u0706\3\2\2\2\u0705\u0707\5\u0164\u00b3"+
		"\2\u0706\u0705\3\2\2\2\u0706\u0707\3\2\2\2\u0707\u0163\3\2\2\2\u0708\u0709"+
		"\t\24\2\2\u0709\u0165\3\2\2\2\u070a\u070c\7\u008a\2\2\u070b\u070a\3\2"+
		"\2\2\u070b\u070c\3\2\2\2\u070c\u070d\3\2\2\2\u070d\u070e\5\u012e\u0098"+
		"\2\u070e\u0167\3\2\2\2\u070f\u0710\7m\2\2\u0710\u0711\5\u00a6T\2\u0711"+
		"\u0712\7n\2\2\u0712\u0169\3\2\2\2\u0713\u0714\7s\2\2\u0714\u0715\7i\2"+
		"\2\u0715\u0716\5\u00c4c\2\u0716\u0717\7j\2\2\u0717\u016b\3\2\2\2\u0718"+
		"\u0727\7i\2\2\u0719\u0720\5\u00e8u\2\u071a\u071d\5\u0148\u00a5\2\u071b"+
		"\u071c\7p\2\2\u071c\u071e\5\u00e8u\2\u071d\u071b\3\2\2\2\u071d\u071e\3"+
		"\2\2\2\u071e\u0720\3\2\2\2\u071f\u0719\3\2\2\2\u071f\u071a\3\2\2\2\u0720"+
		"\u0722\3\2\2\2\u0721\u0723\7w\2\2\u0722\u0721\3\2\2\2\u0722\u0723\3\2"+
		"\2\2\u0723\u0725\3\2\2\2\u0724\u0726\7p\2\2\u0725\u0724\3\2\2\2\u0725"+
		"\u0726\3\2\2\2\u0726\u0728\3\2\2\2\u0727\u071f\3\2\2\2\u0727\u0728\3\2"+
		"\2\2\u0728\u0729\3\2\2\2\u0729\u072a\7j\2\2\u072a\u016d\3\2\2\2\u072b"+
		"\u072c\5\u0148\u00a5\2\u072c\u072d\7s\2\2\u072d\u072e\7h\2\2\u072e\u016f"+
		"\3\2\2\2\u072f\u0730\5\u00c4c\2\u0730\u0171\3\2\2\2\u0731\u0736\7q\2\2"+
		"\u0732\u0736\7\2\2\3\u0733\u0736\7\u00a2\2\2\u0734\u0736\6\u00ba\24\2"+
		"\u0735\u0731\3\2\2\2\u0735\u0732\3\2\2\2\u0735\u0733\3\2\2\2\u0735\u0734"+
		"\3\2\2\2\u0736\u0173\3\2\2\2\u00bd\u0182\u0187\u018e\u0198\u019e\u01a4"+
		"\u01b4\u01b8\u01c1\u01cd\u01d1\u01d7\u01e0\u01ea\u01fc\u020a\u020e\u0215"+
		"\u021d\u0226\u0246\u024e\u026b\u027e\u028d\u029a\u02a3\u02b1\u02ba\u02c6"+
		"\u02db\u02e2\u02e7\u02ec\u02f6\u02f9\u02fd\u0301\u0309\u0311\u0316\u031e"+
		"\u0320\u0325\u032c\u0334\u0337\u033d\u0342\u0344\u0347\u034e\u0353\u0366"+
		"\u036e\u0372\u0375\u037b\u037f\u0382\u038c\u0393\u039a\u03a6\u03ac\u03b3"+
		"\u03b8\u03be\u03ca\u03d0\u03d4\u03dc\u03e0\u03e6\u03e9\u03ef\u03f4\u040d"+
		"\u0430\u0432\u0449\u0451\u045c\u0463\u046a\u0474\u0482\u0498\u049a\u04a2"+
		"\u04a6\u04aa\u04ad\u04b6\u04bc\u04c6\u04ce\u04d4\u04dd\u04e8\u04f3\u04f7"+
		"\u04f9\u0504\u050d\u0511\u0514\u051b\u0526\u0530\u0536\u0538\u0542\u054c"+
		"\u0550\u0554\u0558\u055f\u0567\u0572\u0576\u057a\u0586\u058a\u058e\u0593"+
		"\u059a\u05a1\u05b5\u05b9\u05bd\u05c1\u05d1\u05d7\u05d9\u05dd\u05e1\u05e4"+
		"\u05e8\u05ea\u05f0\u05f8\u05fd\u0608\u060e\u0615\u0620\u0625\u0629\u062e"+
		"\u0632\u063a\u0642\u0647\u064a\u0652\u065a\u065f\u0663\u0667\u066e\u068b"+
		"\u0696\u069a\u06a2\u06a6\u06a8\u06b0\u06b9\u06c1\u06c6\u06d6\u06d8\u06e1"+
		"\u06e7\u06ed\u06f1\u06fa\u0703\u0706\u070b\u071d\u071f\u0722\u0725\u0727"+
		"\u0735";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}