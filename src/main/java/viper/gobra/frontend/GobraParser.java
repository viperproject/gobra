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
		IMPL=15, OLD=16, LHS=17, FORALL=18, EXISTS=19, ACCESS=20, FOLD=21, UNFOLD=22, 
		UNFOLDING=23, GHOST=24, IN=25, MULTI=26, SUBSET=27, UNION=28, INTERSECTION=29, 
		SETMINUS=30, IMPLIES=31, WAND=32, APPLY=33, QMARK=34, L_PRED=35, R_PRED=36, 
		SEQ=37, SET=38, MSET=39, DICT=40, OPT=41, LEN=42, NEW=43, MAKE=44, CAP=45, 
		SOME=46, GET=47, DOM=48, AXIOM=49, NONE=50, PRED=51, TYPE_OF=52, IS_COMPARABLE=53, 
		SHARE=54, ADDR_MOD=55, DOT_DOT=56, SHARED=57, EXCLUSIVE=58, PREDICATE=59, 
		WRITEPERM=60, NOPERM=61, TRUSTED=62, OUTLINE=63, BREAK=64, DEFAULT=65, 
		FUNC=66, INTERFACE=67, SELECT=68, CASE=69, DEFER=70, GO=71, MAP=72, STRUCT=73, 
		CHAN=74, ELSE=75, GOTO=76, PACKAGE=77, SWITCH=78, CONST=79, FALLTHROUGH=80, 
		IF=81, RANGE=82, TYPE=83, CONTINUE=84, FOR=85, IMPORT=86, RETURN=87, VAR=88, 
		NIL_LIT=89, IDENTIFIER=90, L_PAREN=91, R_PAREN=92, L_CURLY=93, R_CURLY=94, 
		L_BRACKET=95, R_BRACKET=96, ASSIGN=97, COMMA=98, SEMI=99, COLON=100, DOT=101, 
		PLUS_PLUS=102, MINUS_MINUS=103, DECLARE_ASSIGN=104, ELLIPSIS=105, LOGICAL_OR=106, 
		LOGICAL_AND=107, EQUALS=108, NOT_EQUALS=109, LESS=110, LESS_OR_EQUALS=111, 
		GREATER=112, GREATER_OR_EQUALS=113, OR=114, DIV=115, MOD=116, LSHIFT=117, 
		RSHIFT=118, BIT_CLEAR=119, EXCLAMATION=120, PLUS=121, MINUS=122, CARET=123, 
		STAR=124, AMPERSAND=125, RECEIVE=126, DECIMAL_LIT=127, BINARY_LIT=128, 
		OCTAL_LIT=129, HEX_LIT=130, HEX_FLOAT_LIT=131, IMAGINARY_LIT=132, RUNE_LIT=133, 
		BYTE_VALUE=134, OCTAL_BYTE_VALUE=135, HEX_BYTE_VALUE=136, LITTLE_U_VALUE=137, 
		BIG_U_VALUE=138, RAW_STRING_LIT=139, INTERPRETED_STRING_LIT=140, WS=141, 
		COMMENT=142, TERMINATOR=143, LINE_COMMENT=144, WS_NLSEMI=145, COMMENT_NLSEMI=146, 
		LINE_COMMENT_NLSEMI=147, EOS=148, OTHER=149;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_sourceFile = 5, RULE_ghostMember = 6, 
		RULE_ghostStatement = 7, RULE_auxiliaryStatement = 8, RULE_statementWithSpec = 9, 
		RULE_outlineStatement = 10, RULE_ghostPrimaryExpr = 11, RULE_permission = 12, 
		RULE_typeExpr = 13, RULE_boundVariables = 14, RULE_boundVariableDecl = 15, 
		RULE_triggers = 16, RULE_trigger = 17, RULE_predicateAccess = 18, RULE_optionSome = 19, 
		RULE_optionNone = 20, RULE_optionGet = 21, RULE_sConversion = 22, RULE_old = 23, 
		RULE_oldLabelUse = 24, RULE_labelUse = 25, RULE_isComparable = 26, RULE_typeOf = 27, 
		RULE_access = 28, RULE_range = 29, RULE_seqUpdExp = 30, RULE_seqUpdClause = 31, 
		RULE_ghostTypeLit = 32, RULE_domainType = 33, RULE_domainClause = 34, 
		RULE_ghostSliceType = 35, RULE_sqType = 36, RULE_specification = 37, RULE_specStatement = 38, 
		RULE_terminationMeasure = 39, RULE_assertion = 40, RULE_blockWithBodyParameterInfo = 41, 
		RULE_implementationProof = 42, RULE_methodImplementationProof = 43, RULE_nonLocalReceiver = 44, 
		RULE_selection = 45, RULE_implementationProofPredicateAlias = 46, RULE_make = 47, 
		RULE_new_ = 48, RULE_specMember = 49, RULE_functionDecl = 50, RULE_methodDecl = 51, 
		RULE_explicitGhostMember = 52, RULE_fpredicateDecl = 53, RULE_predicateBody = 54, 
		RULE_mpredicateDecl = 55, RULE_varSpec = 56, RULE_shortVarDecl = 57, RULE_receiver = 58, 
		RULE_parameterDecl = 59, RULE_actualParameterDecl = 60, RULE_ghostParameterDecl = 61, 
		RULE_parameterType = 62, RULE_expression = 63, RULE_statement = 64, RULE_applyStmt = 65, 
		RULE_packageStmt = 66, RULE_specForStmt = 67, RULE_loopSpec = 68, RULE_basicLit = 69, 
		RULE_primaryExpr = 70, RULE_predConstructArgs = 71, RULE_interfaceType = 72, 
		RULE_predicateSpec = 73, RULE_methodSpec = 74, RULE_type_ = 75, RULE_typeLit = 76, 
		RULE_predType = 77, RULE_predTypeParams = 78, RULE_literalType = 79, RULE_implicitArray = 80, 
		RULE_slice_ = 81, RULE_low = 82, RULE_high = 83, RULE_cap = 84, RULE_assign_op = 85, 
		RULE_packageClause = 86, RULE_importDecl = 87, RULE_importSpec = 88, RULE_importPath = 89, 
		RULE_declaration = 90, RULE_constDecl = 91, RULE_constSpec = 92, RULE_identifierList = 93, 
		RULE_expressionList = 94, RULE_typeDecl = 95, RULE_typeSpec = 96, RULE_varDecl = 97, 
		RULE_block = 98, RULE_statementList = 99, RULE_simpleStmt = 100, RULE_expressionStmt = 101, 
		RULE_sendStmt = 102, RULE_incDecStmt = 103, RULE_assignment = 104, RULE_emptyStmt = 105, 
		RULE_labeledStmt = 106, RULE_returnStmt = 107, RULE_breakStmt = 108, RULE_continueStmt = 109, 
		RULE_gotoStmt = 110, RULE_fallthroughStmt = 111, RULE_deferStmt = 112, 
		RULE_ifStmt = 113, RULE_switchStmt = 114, RULE_exprSwitchStmt = 115, RULE_exprCaseClause = 116, 
		RULE_exprSwitchCase = 117, RULE_typeSwitchStmt = 118, RULE_typeSwitchGuard = 119, 
		RULE_typeCaseClause = 120, RULE_typeSwitchCase = 121, RULE_typeList = 122, 
		RULE_selectStmt = 123, RULE_commClause = 124, RULE_commCase = 125, RULE_recvStmt = 126, 
		RULE_forStmt = 127, RULE_forClause = 128, RULE_rangeClause = 129, RULE_goStmt = 130, 
		RULE_typeName = 131, RULE_arrayType = 132, RULE_arrayLength = 133, RULE_elementType = 134, 
		RULE_pointerType = 135, RULE_sliceType = 136, RULE_mapType = 137, RULE_channelType = 138, 
		RULE_functionType = 139, RULE_signature = 140, RULE_result = 141, RULE_parameters = 142, 
		RULE_conversion = 143, RULE_nonNamedType = 144, RULE_operand = 145, RULE_literal = 146, 
		RULE_integer = 147, RULE_operandName = 148, RULE_qualifiedIdent = 149, 
		RULE_compositeLit = 150, RULE_literalValue = 151, RULE_elementList = 152, 
		RULE_keyedElement = 153, RULE_key = 154, RULE_element = 155, RULE_structType = 156, 
		RULE_fieldDecl = 157, RULE_string_ = 158, RULE_embeddedField = 159, RULE_functionLit = 160, 
		RULE_index = 161, RULE_typeAssertion = 162, RULE_arguments = 163, RULE_methodExpr = 164, 
		RULE_receiverType = 165, RULE_eos = 166;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "sourceFile", "ghostMember", "ghostStatement", 
			"auxiliaryStatement", "statementWithSpec", "outlineStatement", "ghostPrimaryExpr", 
			"permission", "typeExpr", "boundVariables", "boundVariableDecl", "triggers", 
			"trigger", "predicateAccess", "optionSome", "optionNone", "optionGet", 
			"sConversion", "old", "oldLabelUse", "labelUse", "isComparable", "typeOf", 
			"access", "range", "seqUpdExp", "seqUpdClause", "ghostTypeLit", "domainType", 
			"domainClause", "ghostSliceType", "sqType", "specification", "specStatement", 
			"terminationMeasure", "assertion", "blockWithBodyParameterInfo", "implementationProof", 
			"methodImplementationProof", "nonLocalReceiver", "selection", "implementationProofPredicateAlias", 
			"make", "new_", "specMember", "functionDecl", "methodDecl", "explicitGhostMember", 
			"fpredicateDecl", "predicateBody", "mpredicateDecl", "varSpec", "shortVarDecl", 
			"receiver", "parameterDecl", "actualParameterDecl", "ghostParameterDecl", 
			"parameterType", "expression", "statement", "applyStmt", "packageStmt", 
			"specForStmt", "loopSpec", "basicLit", "primaryExpr", "predConstructArgs", 
			"interfaceType", "predicateSpec", "methodSpec", "type_", "typeLit", "predType", 
			"predTypeParams", "literalType", "implicitArray", "slice_", "low", "high", 
			"cap", "assign_op", "packageClause", "importDecl", "importSpec", "importPath", 
			"declaration", "constDecl", "constSpec", "identifierList", "expressionList", 
			"typeDecl", "typeSpec", "varDecl", "block", "statementList", "simpleStmt", 
			"expressionStmt", "sendStmt", "incDecStmt", "assignment", "emptyStmt", 
			"labeledStmt", "returnStmt", "breakStmt", "continueStmt", "gotoStmt", 
			"fallthroughStmt", "deferStmt", "ifStmt", "switchStmt", "exprSwitchStmt", 
			"exprCaseClause", "exprSwitchCase", "typeSwitchStmt", "typeSwitchGuard", 
			"typeCaseClause", "typeSwitchCase", "typeList", "selectStmt", "commClause", 
			"commCase", "recvStmt", "forStmt", "forClause", "rangeClause", "goStmt", 
			"typeName", "arrayType", "arrayLength", "elementType", "pointerType", 
			"sliceType", "mapType", "channelType", "functionType", "signature", "result", 
			"parameters", "conversion", "nonNamedType", "operand", "literal", "integer", 
			"operandName", "qualifiedIdent", "compositeLit", "literalValue", "elementList", 
			"keyedElement", "key", "element", "structType", "fieldDecl", "string_", 
			"embeddedField", "functionLit", "index", "typeAssertion", "arguments", 
			"methodExpr", "receiverType", "eos"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'true'", "'false'", "'assert'", "'assume'", "'inhale'", 
			"'exhale'", "'requires'", "'preserves'", "'ensures'", "'invariant'", 
			"'decreases'", "'pure'", "'implements'", "'old'", "'#lhs'", "'forall'", 
			"'exists'", "'acc'", "'fold'", "'unfold'", "'unfolding'", "'ghost'", 
			"'in'", "'#'", "'subset'", "'union'", "'intersection'", "'setminus'", 
			"'==>'", "'--*'", "'apply'", "'?'", "'!<'", "'!>'", "'seq'", "'set'", 
			"'mset'", "'dict'", "'option'", "'len'", "'new'", "'make'", "'cap'", 
			"'some'", "'get'", "'domain'", "'axiom'", "'none'", "'pred'", "'typeOf'", 
			"'isComparable'", "'share'", "'@'", "'..'", "'shared'", "'exclusive'", 
			"'predicate'", "'writePerm'", "'noPerm'", "'trusted'", "'outline'", "'break'", 
			"'default'", "'func'", "'interface'", "'select'", "'case'", "'defer'", 
			"'go'", "'map'", "'struct'", "'chan'", "'else'", "'goto'", "'package'", 
			"'switch'", "'const'", "'fallthrough'", "'if'", "'range'", "'type'", 
			"'continue'", "'for'", "'import'", "'return'", "'var'", "'nil'", null, 
			"'('", "')'", "'{'", "'}'", "'['", "']'", "'='", "','", "';'", "':'", 
			"'.'", "'++'", "'--'", "':='", "'...'", "'||'", "'&&'", "'=='", "'!='", 
			"'<'", "'<='", "'>'", "'>='", "'|'", "'/'", "'%'", "'<<'", "'>>'", "'&^'", 
			"'!'", "'+'", "'-'", "'^'", "'*'", "'&'", "'<-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FLOAT_LIT", "DECIMAL_FLOAT_LIT", "TRUE", "FALSE", "ASSERT", "ASSUME", 
			"INHALE", "EXHALE", "PRE", "PRESERVES", "POST", "INV", "DEC", "PURE", 
			"IMPL", "OLD", "LHS", "FORALL", "EXISTS", "ACCESS", "FOLD", "UNFOLD", 
			"UNFOLDING", "GHOST", "IN", "MULTI", "SUBSET", "UNION", "INTERSECTION", 
			"SETMINUS", "IMPLIES", "WAND", "APPLY", "QMARK", "L_PRED", "R_PRED", 
			"SEQ", "SET", "MSET", "DICT", "OPT", "LEN", "NEW", "MAKE", "CAP", "SOME", 
			"GET", "DOM", "AXIOM", "NONE", "PRED", "TYPE_OF", "IS_COMPARABLE", "SHARE", 
			"ADDR_MOD", "DOT_DOT", "SHARED", "EXCLUSIVE", "PREDICATE", "WRITEPERM", 
			"NOPERM", "TRUSTED", "OUTLINE", "BREAK", "DEFAULT", "FUNC", "INTERFACE", 
			"SELECT", "CASE", "DEFER", "GO", "MAP", "STRUCT", "CHAN", "ELSE", "GOTO", 
			"PACKAGE", "SWITCH", "CONST", "FALLTHROUGH", "IF", "RANGE", "TYPE", "CONTINUE", 
			"FOR", "IMPORT", "RETURN", "VAR", "NIL_LIT", "IDENTIFIER", "L_PAREN", 
			"R_PAREN", "L_CURLY", "R_CURLY", "L_BRACKET", "R_BRACKET", "ASSIGN", 
			"COMMA", "SEMI", "COLON", "DOT", "PLUS_PLUS", "MINUS_MINUS", "DECLARE_ASSIGN", 
			"ELLIPSIS", "LOGICAL_OR", "LOGICAL_AND", "EQUALS", "NOT_EQUALS", "LESS", 
			"LESS_OR_EQUALS", "GREATER", "GREATER_OR_EQUALS", "OR", "DIV", "MOD", 
			"LSHIFT", "RSHIFT", "BIT_CLEAR", "EXCLAMATION", "PLUS", "MINUS", "CARET", 
			"STAR", "AMPERSAND", "RECEIVE", "DECIMAL_LIT", "BINARY_LIT", "OCTAL_LIT", 
			"HEX_LIT", "HEX_FLOAT_LIT", "IMAGINARY_LIT", "RUNE_LIT", "BYTE_VALUE", 
			"OCTAL_BYTE_VALUE", "HEX_BYTE_VALUE", "LITTLE_U_VALUE", "BIG_U_VALUE", 
			"RAW_STRING_LIT", "INTERPRETED_STRING_LIT", "WS", "COMMENT", "TERMINATOR", 
			"LINE_COMMENT", "WS_NLSEMI", "COMMENT_NLSEMI", "LINE_COMMENT_NLSEMI", 
			"EOS", "OTHER"
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
			setState(334);
			expression(0);
			setState(335);
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
			setState(337);
			statement();
			setState(338);
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
			setState(340);
			type_();
			setState(341);
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
			setState(343);
			maybeAddressableIdentifier();
			setState(348);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(344);
				match(COMMA);
				setState(345);
				maybeAddressableIdentifier();
				}
				}
				setState(350);
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
			setState(351);
			match(IDENTIFIER);
			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(352);
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
			setState(355);
			packageClause();
			setState(356);
			eos();
			setState(362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(357);
				importDecl();
				setState(358);
				eos();
				}
				}
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(374);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED) | (1L << TRUSTED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (CONST - 66)) | (1L << (TYPE - 66)) | (1L << (VAR - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
				{
				{
				setState(368);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(365);
					specMember();
					}
					break;
				case 2:
					{
					setState(366);
					declaration();
					}
					break;
				case 3:
					{
					setState(367);
					ghostMember();
					}
					break;
				}
				setState(370);
				eos();
				}
				}
				setState(376);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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
		enterRule(_localctx, 12, RULE_ghostMember);
		try {
			setState(383);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(379);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(380);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(381);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(382);
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
		enterRule(_localctx, 14, RULE_ghostStatement);
		int _la;
		try {
			setState(391);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				_localctx = new ExplicitGhostStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(385);
				match(GHOST);
				setState(386);
				statement();
				}
				break;
			case FOLD:
			case UNFOLD:
				_localctx = new FoldStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(387);
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
				setState(388);
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
				setState(389);
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
				setState(390);
				expression(0);
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
		enterRule(_localctx, 16, RULE_auxiliaryStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
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
		enterRule(_localctx, 18, RULE_statementWithSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			((StatementWithSpecContext)_localctx).specification = specification();
			{
			setState(396);
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
		enterRule(_localctx, 20, RULE_outlineStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(OUTLINE);
			setState(399);
			match(L_PAREN);
			setState(401);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(400);
				statementList();
				}
				break;
			}
			setState(403);
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
		public OldContext old() {
			return getRuleContext(OldContext.class,0);
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
		enterRule(_localctx, 22, RULE_ghostPrimaryExpr);
		try {
			setState(416);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(405);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(406);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(407);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(408);
				typeExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(409);
				isComparable();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(410);
				old();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(411);
				sConversion();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(412);
				optionNone();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(413);
				optionSome();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(414);
				optionGet();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(415);
				permission();
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
		enterRule(_localctx, 24, RULE_permission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
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
		enterRule(_localctx, 26, RULE_typeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			match(TYPE);
			setState(421);
			match(L_BRACKET);
			setState(422);
			type_();
			setState(423);
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
		enterRule(_localctx, 28, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(425);
			boundVariableDecl();
			setState(430);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(426);
					match(COMMA);
					setState(427);
					boundVariableDecl();
					}
					} 
				}
				setState(432);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(433);
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
		enterRule(_localctx, 30, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			match(IDENTIFIER);
			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(437);
				match(COMMA);
				setState(438);
				match(IDENTIFIER);
				}
				}
				setState(443);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(444);
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
		enterRule(_localctx, 32, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(449);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(446);
				trigger();
				}
				}
				setState(451);
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
		enterRule(_localctx, 34, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(452);
			match(L_CURLY);
			setState(453);
			expression(0);
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(454);
				match(COMMA);
				setState(455);
				expression(0);
				}
				}
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(461);
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
		enterRule(_localctx, 36, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
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
		enterRule(_localctx, 38, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			match(SOME);
			setState(466);
			match(L_PAREN);
			setState(467);
			expression(0);
			setState(468);
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
		enterRule(_localctx, 40, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(NONE);
			setState(471);
			match(L_BRACKET);
			setState(472);
			type_();
			setState(473);
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
		enterRule(_localctx, 42, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			match(GET);
			setState(476);
			match(L_PAREN);
			setState(477);
			expression(0);
			setState(478);
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
		enterRule(_localctx, 44, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
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
			setState(481);
			match(L_PAREN);
			setState(482);
			expression(0);
			setState(483);
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
		enterRule(_localctx, 46, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			match(OLD);
			setState(490);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(486);
				match(L_BRACKET);
				setState(487);
				oldLabelUse();
				setState(488);
				match(R_BRACKET);
				}
			}

			setState(492);
			match(L_PAREN);
			setState(493);
			expression(0);
			setState(494);
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
		enterRule(_localctx, 48, RULE_oldLabelUse);
		try {
			setState(498);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(496);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(497);
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
		enterRule(_localctx, 50, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(500);
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
		enterRule(_localctx, 52, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			match(IS_COMPARABLE);
			setState(503);
			match(L_PAREN);
			setState(504);
			expression(0);
			setState(505);
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
		enterRule(_localctx, 54, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(507);
			match(TYPE_OF);
			setState(508);
			match(L_PAREN);
			setState(509);
			expression(0);
			setState(510);
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
		enterRule(_localctx, 56, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(512);
			match(ACCESS);
			setState(513);
			match(L_PAREN);
			setState(514);
			expression(0);
			setState(517);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(515);
				match(COMMA);
				setState(516);
				expression(0);
				}
			}

			setState(519);
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
		enterRule(_localctx, 58, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(521);
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
			setState(522);
			match(L_BRACKET);
			setState(523);
			expression(0);
			setState(524);
			match(DOT_DOT);
			setState(525);
			expression(0);
			setState(526);
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
		enterRule(_localctx, 60, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			match(L_BRACKET);
			{
			setState(529);
			seqUpdClause();
			setState(534);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(530);
				match(COMMA);
				setState(531);
				seqUpdClause();
				}
				}
				setState(536);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(537);
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
		enterRule(_localctx, 62, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(539);
			expression(0);
			setState(540);
			match(ASSIGN);
			setState(541);
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
		enterRule(_localctx, 64, RULE_ghostTypeLit);
		try {
			setState(546);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(543);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(544);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(545);
				domainType();
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
		enterRule(_localctx, 66, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			match(DOM);
			setState(549);
			match(L_CURLY);
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(550);
				domainClause();
				setState(551);
				eos();
				}
				}
				setState(557);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(558);
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
		enterRule(_localctx, 68, RULE_domainClause);
		try {
			setState(569);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(560);
				match(FUNC);
				setState(561);
				match(IDENTIFIER);
				setState(562);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(563);
				match(AXIOM);
				setState(564);
				match(L_CURLY);
				setState(565);
				expression(0);
				setState(566);
				eos();
				setState(567);
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
		enterRule(_localctx, 70, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(571);
			match(GHOST);
			setState(572);
			match(L_BRACKET);
			setState(573);
			match(R_BRACKET);
			setState(574);
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
		enterRule(_localctx, 72, RULE_sqType);
		int _la;
		try {
			setState(587);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(576);
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
				setState(577);
				match(L_BRACKET);
				setState(578);
				type_();
				setState(579);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(581);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(582);
				match(L_BRACKET);
				setState(583);
				type_();
				setState(584);
				match(R_BRACKET);
				setState(585);
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
		enterRule(_localctx, 74, RULE_specification);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(599);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(594);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(589);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(590);
						match(PURE);
						((SpecificationContext)_localctx).pure =  true;
						}
						break;
					case TRUSTED:
						{
						setState(592);
						match(TRUSTED);
						((SpecificationContext)_localctx).trusted =  true;
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(596);
					eos();
					}
					} 
				}
				setState(601);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			}
			setState(604);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(602);
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
		enterRule(_localctx, 76, RULE_specStatement);
		try {
			setState(614);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(606);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(607);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(608);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(609);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(610);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(611);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(612);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(613);
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
		enterRule(_localctx, 78, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(617);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(616);
				expressionList();
				}
				break;
			}
			setState(621);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(619);
				match(IF);
				setState(620);
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
		enterRule(_localctx, 80, RULE_assertion);
		try {
			setState(625);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(624);
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
		enterRule(_localctx, 82, RULE_blockWithBodyParameterInfo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			match(L_CURLY);
			setState(632);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(628);
				match(SHARE);
				setState(629);
				identifierList();
				setState(630);
				eos();
				}
				break;
			}
			setState(635);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(634);
				statementList();
				}
				break;
			}
			setState(637);
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
		enterRule(_localctx, 84, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			type_();
			setState(640);
			match(IMPL);
			setState(641);
			type_();
			setState(660);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(642);
				match(L_CURLY);
				setState(648);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(643);
					implementationProofPredicateAlias();
					setState(644);
					eos();
					}
					}
					setState(650);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(656);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(651);
					methodImplementationProof();
					setState(652);
					eos();
					}
					}
					setState(658);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(659);
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
		enterRule(_localctx, 86, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(663);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(662);
				match(PURE);
				}
			}

			setState(665);
			nonLocalReceiver();
			setState(666);
			match(IDENTIFIER);
			setState(667);
			signature();
			setState(669);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(668);
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
		enterRule(_localctx, 88, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(671);
			match(L_PAREN);
			setState(673);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(672);
				match(IDENTIFIER);
				}
				break;
			}
			setState(676);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(675);
				match(STAR);
				}
			}

			setState(678);
			typeName();
			setState(679);
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
		enterRule(_localctx, 90, RULE_selection);
		try {
			setState(686);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(681);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(682);
				type_();
				setState(683);
				match(DOT);
				setState(684);
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
		enterRule(_localctx, 92, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(688);
			match(PRED);
			setState(689);
			match(IDENTIFIER);
			setState(690);
			match(DECLARE_ASSIGN);
			setState(693);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(691);
				selection();
				}
				break;
			case 2:
				{
				setState(692);
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
		enterRule(_localctx, 94, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(695);
			match(MAKE);
			setState(696);
			match(L_PAREN);
			setState(697);
			type_();
			setState(700);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(698);
				match(COMMA);
				setState(699);
				expressionList();
				}
			}

			setState(702);
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
		enterRule(_localctx, 96, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(704);
			match(NEW);
			setState(705);
			match(L_PAREN);
			setState(706);
			type_();
			setState(707);
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
		enterRule(_localctx, 98, RULE_specMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(709);
			((SpecMemberContext)_localctx).specification = specification();
			setState(712);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(710);
				functionDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 2:
				{
				setState(711);
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
		enterRule(_localctx, 100, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(714);
			match(FUNC);
			setState(715);
			match(IDENTIFIER);
			{
			setState(716);
			signature();
			setState(718);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(717);
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
		enterRule(_localctx, 102, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(720);
			match(FUNC);
			setState(721);
			receiver();
			setState(722);
			match(IDENTIFIER);
			{
			setState(723);
			signature();
			setState(725);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(724);
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
		enterRule(_localctx, 104, RULE_explicitGhostMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(727);
			match(GHOST);
			setState(730);
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
				setState(728);
				specMember();
				}
				break;
			case CONST:
			case TYPE:
			case VAR:
				{
				setState(729);
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
		enterRule(_localctx, 106, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(732);
			match(PRED);
			setState(733);
			match(IDENTIFIER);
			setState(734);
			parameters();
			setState(736);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(735);
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
		enterRule(_localctx, 108, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(738);
			match(L_CURLY);
			setState(739);
			expression(0);
			setState(740);
			eos();
			setState(741);
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
		enterRule(_localctx, 110, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(743);
			match(PRED);
			setState(744);
			receiver();
			setState(745);
			match(IDENTIFIER);
			setState(746);
			parameters();
			setState(748);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(747);
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
		enterRule(_localctx, 112, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			maybeAddressableIdentifierList();
			setState(758);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
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
				setState(751);
				type_();
				setState(754);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
				case 1:
					{
					setState(752);
					match(ASSIGN);
					setState(753);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(756);
				match(ASSIGN);
				setState(757);
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
		enterRule(_localctx, 114, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(760);
			maybeAddressableIdentifierList();
			setState(761);
			match(DECLARE_ASSIGN);
			setState(762);
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
		enterRule(_localctx, 116, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(764);
			match(L_PAREN);
			setState(766);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(765);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(768);
			type_();
			setState(770);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(769);
				match(COMMA);
				}
			}

			setState(772);
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
		enterRule(_localctx, 118, RULE_parameterDecl);
		try {
			setState(776);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(774);
				actualParameterDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(775);
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
		enterRule(_localctx, 120, RULE_actualParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(778);
				identifierList();
				}
				break;
			}
			setState(781);
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
		enterRule(_localctx, 122, RULE_ghostParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(783);
			match(GHOST);
			setState(785);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(784);
				identifierList();
				}
				break;
			}
			setState(787);
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
		enterRule(_localctx, 124, RULE_parameterType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(789);
				match(ELLIPSIS);
				}
			}

			setState(792);
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
		int _startState = 126;
		enterRecursionRule(_localctx, 126, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(810);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(795);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & ((1L << (EXCLAMATION - 120)) | (1L << (PLUS - 120)) | (1L << (MINUS - 120)) | (1L << (CARET - 120)) | (1L << (STAR - 120)) | (1L << (AMPERSAND - 120)) | (1L << (RECEIVE - 120)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(796);
				expression(13);
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(797);
				primaryExpr(0);
				}
				break;
			case 3:
				{
				_localctx = new UnfoldingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(798);
				match(UNFOLDING);
				setState(799);
				predicateAccess();
				setState(800);
				match(IN);
				setState(801);
				expression(2);
				}
				break;
			case 4:
				{
				_localctx = new QuantificationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(803);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(804);
				boundVariables();
				setState(805);
				match(COLON);
				setState(806);
				match(COLON);
				setState(807);
				triggers();
				setState(808);
				expression(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(844);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(842);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
					case 1:
						{
						_localctx = new MulExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(812);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(813);
						((MulExprContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (DIV - 115)) | (1L << (MOD - 115)) | (1L << (LSHIFT - 115)) | (1L << (RSHIFT - 115)) | (1L << (BIT_CLEAR - 115)) | (1L << (STAR - 115)) | (1L << (AMPERSAND - 115)))) != 0)) ) {
							((MulExprContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(814);
						expression(12);
						}
						break;
					case 2:
						{
						_localctx = new AddExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(815);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(816);
						((AddExprContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 102)) & ~0x3f) == 0 && ((1L << (_la - 102)) & ((1L << (PLUS_PLUS - 102)) | (1L << (OR - 102)) | (1L << (PLUS - 102)) | (1L << (MINUS - 102)) | (1L << (CARET - 102)))) != 0)) ) {
							((AddExprContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(817);
						expression(11);
						}
						break;
					case 3:
						{
						_localctx = new P42ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(818);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(819);
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
						setState(820);
						expression(10);
						}
						break;
					case 4:
						{
						_localctx = new P41ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(821);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(822);
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
						setState(823);
						expression(9);
						}
						break;
					case 5:
						{
						_localctx = new RelExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(824);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(825);
						((RelExprContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & ((1L << (EQUALS - 108)) | (1L << (NOT_EQUALS - 108)) | (1L << (LESS - 108)) | (1L << (LESS_OR_EQUALS - 108)) | (1L << (GREATER - 108)) | (1L << (GREATER_OR_EQUALS - 108)))) != 0)) ) {
							((RelExprContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(826);
						expression(8);
						}
						break;
					case 6:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(827);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(828);
						match(LOGICAL_AND);
						setState(829);
						expression(7);
						}
						break;
					case 7:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(830);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(831);
						match(LOGICAL_OR);
						setState(832);
						expression(6);
						}
						break;
					case 8:
						{
						_localctx = new ImplicationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(833);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(834);
						match(IMPLIES);
						setState(835);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(836);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(837);
						match(QMARK);
						setState(838);
						expression(0);
						setState(839);
						match(COLON);
						setState(840);
						expression(3);
						}
						break;
					}
					} 
				}
				setState(846);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
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
		enterRule(_localctx, 128, RULE_statement);
		try {
			setState(866);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(847);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(848);
				auxiliaryStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(849);
				packageStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(850);
				applyStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(851);
				declaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(852);
				labeledStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(853);
				simpleStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(854);
				goStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(855);
				returnStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(856);
				breakStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(857);
				continueStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(858);
				gotoStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(859);
				fallthroughStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(860);
				block();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(861);
				ifStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(862);
				switchStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(863);
				selectStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(864);
				specForStmt();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(865);
				deferStmt();
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
		enterRule(_localctx, 130, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
			match(APPLY);
			setState(869);
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
		enterRule(_localctx, 132, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(871);
			match(PACKAGE);
			setState(872);
			expression(0);
			setState(874);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(873);
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
		enterRule(_localctx, 134, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
			loopSpec();
			setState(877);
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
		enterRule(_localctx, 136, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(885);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(879);
				match(INV);
				setState(880);
				expression(0);
				setState(881);
				eos();
				}
				}
				setState(887);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(892);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(888);
				match(DEC);
				setState(889);
				terminationMeasure();
				setState(890);
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
		enterRule(_localctx, 138, RULE_basicLit);
		try {
			setState(902);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(894);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(895);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(896);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(897);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(898);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(899);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(900);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(901);
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
		int _startState = 140;
		enterRecursionRule(_localctx, 140, RULE_primaryExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(916);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				_localctx = new OperandPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(905);
				operand();
				}
				break;
			case 2:
				{
				_localctx = new ConversionPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(906);
				conversion();
				}
				break;
			case 3:
				{
				_localctx = new MethodPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(907);
				methodExpr();
				}
				break;
			case 4:
				{
				_localctx = new GhostPrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(908);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(909);
				new_();
				}
				break;
			case 6:
				{
				_localctx = new MakeExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(910);
				make();
				}
				break;
			case 7:
				{
				_localctx = new BuiltInCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(911);
				((BuiltInCallExprContext)_localctx).call_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 42)) & ~0x3f) == 0 && ((1L << (_la - 42)) & ((1L << (LEN - 42)) | (1L << (CAP - 42)) | (1L << (DOM - 42)) | (1L << (RANGE - 42)))) != 0)) ) {
					((BuiltInCallExprContext)_localctx).call_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(912);
				match(L_PAREN);
				setState(913);
				expression(0);
				setState(914);
				match(R_PAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(935);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(933);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(918);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(919);
						match(DOT);
						setState(920);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new IndexPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(921);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(922);
						index();
						}
						break;
					case 3:
						{
						_localctx = new SlicePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(923);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(924);
						slice_();
						}
						break;
					case 4:
						{
						_localctx = new SeqUpdPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(925);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(926);
						seqUpdExp();
						}
						break;
					case 5:
						{
						_localctx = new TypeAssertionPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(927);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(928);
						typeAssertion();
						}
						break;
					case 6:
						{
						_localctx = new InvokePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(929);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(930);
						arguments();
						}
						break;
					case 7:
						{
						_localctx = new PredConstrPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(931);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(932);
						predConstructArgs();
						}
						break;
					}
					} 
				}
				setState(937);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
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
		enterRule(_localctx, 142, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938);
			match(L_PRED);
			setState(940);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
				{
				setState(939);
				expressionList();
				}
			}

			setState(943);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(942);
				match(COMMA);
				}
			}

			setState(945);
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
		enterRule(_localctx, 144, RULE_interfaceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(947);
			match(INTERFACE);
			setState(948);
			match(L_CURLY);
			setState(958);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << PRED) | (1L << TRUSTED))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(952);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
				case 1:
					{
					setState(949);
					methodSpec();
					}
					break;
				case 2:
					{
					setState(950);
					typeName();
					}
					break;
				case 3:
					{
					setState(951);
					predicateSpec();
					}
					break;
				}
				setState(954);
				eos();
				}
				}
				setState(960);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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
		enterRule(_localctx, 146, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(963);
			match(PRED);
			setState(964);
			match(IDENTIFIER);
			setState(965);
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
		enterRule(_localctx, 148, RULE_methodSpec);
		int _la;
		try {
			setState(982);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(968);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(967);
					match(GHOST);
					}
				}

				setState(970);
				specification();
				setState(971);
				match(IDENTIFIER);
				setState(972);
				parameters();
				setState(973);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(976);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(975);
					match(GHOST);
					}
				}

				setState(978);
				specification();
				setState(979);
				match(IDENTIFIER);
				setState(980);
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
		enterRule(_localctx, 150, RULE_type_);
		try {
			setState(991);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(984);
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
				setState(985);
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
				enterOuterAlt(_localctx, 3);
				{
				setState(986);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(987);
				match(L_PAREN);
				setState(988);
				type_();
				setState(989);
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
		enterRule(_localctx, 152, RULE_typeLit);
		try {
			setState(1002);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(993);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(994);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(995);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(996);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(997);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(998);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(999);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1000);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1001);
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
		enterRule(_localctx, 154, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1004);
			match(PRED);
			setState(1005);
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
		enterRule(_localctx, 156, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1007);
			match(L_PAREN);
			setState(1019);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
				{
				setState(1008);
				type_();
				setState(1013);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1009);
						match(COMMA);
						setState(1010);
						type_();
						}
						} 
					}
					setState(1015);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
				}
				setState(1017);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1016);
					match(COMMA);
					}
				}

				}
			}

			setState(1021);
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
		enterRule(_localctx, 158, RULE_literalType);
		try {
			setState(1030);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1023);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1024);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1025);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1026);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1027);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1028);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1029);
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
		enterRule(_localctx, 160, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1032);
			match(L_BRACKET);
			setState(1033);
			match(ELLIPSIS);
			setState(1034);
			match(R_BRACKET);
			setState(1035);
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
		public HighContext high() {
			return getRuleContext(HighContext.class,0);
		}
		public CapContext cap() {
			return getRuleContext(CapContext.class,0);
		}
		public LowContext low() {
			return getRuleContext(LowContext.class,0);
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
		enterRule(_localctx, 162, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1037);
			match(L_BRACKET);
			setState(1053);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				{
				setState(1039);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
					{
					setState(1038);
					low();
					}
				}

				setState(1041);
				match(COLON);
				setState(1043);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
					{
					setState(1042);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1046);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
					{
					setState(1045);
					low();
					}
				}

				setState(1048);
				match(COLON);
				setState(1049);
				high();
				setState(1050);
				match(COLON);
				setState(1051);
				cap();
				}
				break;
			}
			setState(1055);
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

	public static class LowContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
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
		enterRule(_localctx, 164, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
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

	public static class HighContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public HighContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_high; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitHigh(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HighContext high() throws RecognitionException {
		HighContext _localctx = new HighContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1059);
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

	public static class CapContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cap; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitCap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CapContext cap() throws RecognitionException {
		CapContext _localctx = new CapContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1061);
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
		enterRule(_localctx, 170, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1064);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (OR - 114)) | (1L << (DIV - 114)) | (1L << (MOD - 114)) | (1L << (LSHIFT - 114)) | (1L << (RSHIFT - 114)) | (1L << (BIT_CLEAR - 114)) | (1L << (PLUS - 114)) | (1L << (MINUS - 114)) | (1L << (CARET - 114)) | (1L << (STAR - 114)) | (1L << (AMPERSAND - 114)))) != 0)) {
				{
				setState(1063);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (OR - 114)) | (1L << (DIV - 114)) | (1L << (MOD - 114)) | (1L << (LSHIFT - 114)) | (1L << (RSHIFT - 114)) | (1L << (BIT_CLEAR - 114)) | (1L << (PLUS - 114)) | (1L << (MINUS - 114)) | (1L << (CARET - 114)) | (1L << (STAR - 114)) | (1L << (AMPERSAND - 114)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1066);
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
		enterRule(_localctx, 172, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1068);
			match(PACKAGE);
			setState(1069);
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
		enterRule(_localctx, 174, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			match(IMPORT);
			setState(1083);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1072);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1073);
				match(L_PAREN);
				setState(1079);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 90)) & ~0x3f) == 0 && ((1L << (_la - 90)) & ((1L << (IDENTIFIER - 90)) | (1L << (DOT - 90)) | (1L << (RAW_STRING_LIT - 90)) | (1L << (INTERPRETED_STRING_LIT - 90)))) != 0)) {
					{
					{
					setState(1074);
					importSpec();
					setState(1075);
					eos();
					}
					}
					setState(1081);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1082);
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

	public static class ImportSpecContext extends ParserRuleContext {
		public Token alias;
		public ImportPathContext importPath() {
			return getRuleContext(ImportPathContext.class,0);
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
		enterRule(_localctx, 176, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1086);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1085);
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

			setState(1088);
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
		enterRule(_localctx, 178, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1090);
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
		enterRule(_localctx, 180, RULE_declaration);
		try {
			setState(1095);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1092);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1093);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1094);
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
		enterRule(_localctx, 182, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1097);
			match(CONST);
			setState(1109);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1098);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1099);
				match(L_PAREN);
				setState(1105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1100);
					constSpec();
					setState(1101);
					eos();
					}
					}
					setState(1107);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1108);
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
		enterRule(_localctx, 184, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1111);
			identifierList();
			setState(1117);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				{
				setState(1113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
					{
					setState(1112);
					type_();
					}
				}

				setState(1115);
				match(ASSIGN);
				setState(1116);
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
		enterRule(_localctx, 186, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1119);
			match(IDENTIFIER);
			setState(1124);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1120);
					match(COMMA);
					setState(1121);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
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
		enterRule(_localctx, 188, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1127);
			expression(0);
			setState(1132);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1128);
					match(COMMA);
					setState(1129);
					expression(0);
					}
					} 
				}
				setState(1134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
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
		enterRule(_localctx, 190, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1135);
			match(TYPE);
			setState(1147);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1136);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1137);
				match(L_PAREN);
				setState(1143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1138);
					typeSpec();
					setState(1139);
					eos();
					}
					}
					setState(1145);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1146);
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
		enterRule(_localctx, 192, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			match(IDENTIFIER);
			setState(1151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1150);
				match(ASSIGN);
				}
			}

			setState(1153);
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
		enterRule(_localctx, 194, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1155);
			match(VAR);
			setState(1167);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1156);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1157);
				match(L_PAREN);
				setState(1163);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1158);
					varSpec();
					setState(1159);
					eos();
					}
					}
					setState(1165);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1166);
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
		enterRule(_localctx, 196, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1169);
			match(L_CURLY);
			setState(1171);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(1170);
				statementList();
				}
				break;
			}
			setState(1173);
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
		enterRule(_localctx, 198, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1181); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1176);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
					case 1:
						{
						setState(1175);
						eos();
						}
						break;
					}
					setState(1178);
					statement();
					setState(1179);
					eos();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1183); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
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
		enterRule(_localctx, 200, RULE_simpleStmt);
		try {
			setState(1190);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1185);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1186);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1187);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1188);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1189);
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
		enterRule(_localctx, 202, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1192);
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
		enterRule(_localctx, 204, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1194);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1195);
			match(RECEIVE);
			setState(1196);
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
		enterRule(_localctx, 206, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1198);
			expression(0);
			setState(1199);
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
		enterRule(_localctx, 208, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1201);
			expressionList();
			setState(1202);
			assign_op();
			setState(1203);
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
		enterRule(_localctx, 210, RULE_emptyStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1205);
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
		enterRule(_localctx, 212, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			match(IDENTIFIER);
			setState(1208);
			match(COLON);
			setState(1210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				{
				setState(1209);
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
		enterRule(_localctx, 214, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1212);
			match(RETURN);
			setState(1214);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				{
				setState(1213);
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
		enterRule(_localctx, 216, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1216);
			match(BREAK);
			setState(1218);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1217);
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
		enterRule(_localctx, 218, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1220);
			match(CONTINUE);
			setState(1222);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1221);
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
		enterRule(_localctx, 220, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1224);
			match(GOTO);
			setState(1225);
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
		enterRule(_localctx, 222, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1227);
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

	public static class DeferStmtContext extends ParserRuleContext {
		public TerminalNode DEFER() { return getToken(GobraParser.DEFER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
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
		enterRule(_localctx, 224, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1229);
			match(DEFER);
			setState(1230);
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
		enterRule(_localctx, 226, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1232);
			match(IF);
			setState(1241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(1233);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1234);
				eos();
				setState(1235);
				expression(0);
				}
				break;
			case 3:
				{
				setState(1237);
				simpleStmt();
				setState(1238);
				eos();
				setState(1239);
				expression(0);
				}
				break;
			}
			setState(1243);
			block();
			setState(1249);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1244);
				match(ELSE);
				setState(1247);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1245);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1246);
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
		enterRule(_localctx, 228, RULE_switchStmt);
		try {
			setState(1253);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1251);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1252);
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
		enterRule(_localctx, 230, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1255);
			match(SWITCH);
			setState(1266);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
			case 1:
				{
				setState(1257);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
					{
					setState(1256);
					expression(0);
					}
				}

				}
				break;
			case 2:
				{
				setState(1260);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
				case 1:
					{
					setState(1259);
					simpleStmt();
					}
					break;
				}
				setState(1262);
				eos();
				setState(1264);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
					{
					setState(1263);
					expression(0);
					}
				}

				}
				break;
			}
			setState(1268);
			match(L_CURLY);
			setState(1272);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1269);
				exprCaseClause();
				}
				}
				setState(1274);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1275);
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
		enterRule(_localctx, 232, RULE_exprCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1277);
			exprSwitchCase();
			setState(1278);
			match(COLON);
			setState(1280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1279);
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
		enterRule(_localctx, 234, RULE_exprSwitchCase);
		try {
			setState(1285);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1282);
				match(CASE);
				setState(1283);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1284);
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
		enterRule(_localctx, 236, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1287);
			match(SWITCH);
			setState(1296);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1288);
				typeSwitchGuard();
				}
				break;
			case 2:
				{
				setState(1289);
				eos();
				setState(1290);
				typeSwitchGuard();
				}
				break;
			case 3:
				{
				setState(1292);
				simpleStmt();
				setState(1293);
				eos();
				setState(1294);
				typeSwitchGuard();
				}
				break;
			}
			setState(1298);
			match(L_CURLY);
			setState(1302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1299);
				typeCaseClause();
				}
				}
				setState(1304);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1305);
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
		enterRule(_localctx, 238, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1307);
				match(IDENTIFIER);
				setState(1308);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1311);
			primaryExpr(0);
			setState(1312);
			match(DOT);
			setState(1313);
			match(L_PAREN);
			setState(1314);
			match(TYPE);
			setState(1315);
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
		enterRule(_localctx, 240, RULE_typeCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
			typeSwitchCase();
			setState(1318);
			match(COLON);
			setState(1320);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1319);
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
		enterRule(_localctx, 242, RULE_typeSwitchCase);
		try {
			setState(1325);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1322);
				match(CASE);
				setState(1323);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1324);
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
		enterRule(_localctx, 244, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1329);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
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
				setState(1327);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1328);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1338);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1331);
				match(COMMA);
				setState(1334);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GHOST:
				case SEQ:
				case SET:
				case MSET:
				case DICT:
				case OPT:
				case DOM:
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
					setState(1332);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1333);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1340);
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
		enterRule(_localctx, 246, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			match(SELECT);
			setState(1342);
			match(L_CURLY);
			setState(1346);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1343);
				commClause();
				}
				}
				setState(1348);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1349);
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
		enterRule(_localctx, 248, RULE_commClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1351);
			commCase();
			setState(1352);
			match(COLON);
			setState(1354);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				{
				setState(1353);
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
		enterRule(_localctx, 250, RULE_commCase);
		try {
			setState(1362);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1356);
				match(CASE);
				setState(1359);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
				case 1:
					{
					setState(1357);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1358);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1361);
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
		enterRule(_localctx, 252, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1364);
				expressionList();
				setState(1365);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1367);
				identifierList();
				setState(1368);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1372);
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
		enterRule(_localctx, 254, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1374);
			match(FOR);
			setState(1378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1375);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1376);
				forClause();
				}
				break;
			case 3:
				{
				setState(1377);
				rangeClause();
				}
				break;
			}
			setState(1380);
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
		enterRule(_localctx, 256, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1383);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1382);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1385);
			eos();
			setState(1387);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1386);
				expression(0);
				}
				break;
			}
			setState(1389);
			eos();
			setState(1391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
				{
				setState(1390);
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

	public static class RangeClauseContext extends ParserRuleContext {
		public TerminalNode RANGE() { return getToken(GobraParser.RANGE, 0); }
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
		enterRule(_localctx, 258, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1399);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				{
				setState(1393);
				expressionList();
				setState(1394);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1396);
				identifierList();
				setState(1397);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1401);
			match(RANGE);
			setState(1402);
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
		enterRule(_localctx, 260, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1404);
			match(GO);
			setState(1405);
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
		enterRule(_localctx, 262, RULE_typeName);
		try {
			setState(1409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1407);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1408);
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
		enterRule(_localctx, 264, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1411);
			match(L_BRACKET);
			setState(1412);
			arrayLength();
			setState(1413);
			match(R_BRACKET);
			setState(1414);
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
		enterRule(_localctx, 266, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1416);
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
		enterRule(_localctx, 268, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
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
		enterRule(_localctx, 270, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1420);
			match(STAR);
			setState(1421);
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
		enterRule(_localctx, 272, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1423);
			match(L_BRACKET);
			setState(1424);
			match(R_BRACKET);
			setState(1425);
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
		enterRule(_localctx, 274, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1427);
			match(MAP);
			setState(1428);
			match(L_BRACKET);
			setState(1429);
			type_();
			setState(1430);
			match(R_BRACKET);
			setState(1431);
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
		enterRule(_localctx, 276, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1438);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				{
				setState(1433);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1434);
				match(CHAN);
				setState(1435);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1436);
				match(RECEIVE);
				setState(1437);
				match(CHAN);
				}
				break;
			}
			setState(1440);
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
		enterRule(_localctx, 278, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1442);
			match(FUNC);
			setState(1443);
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
		enterRule(_localctx, 280, RULE_signature);
		try {
			setState(1449);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1445);
				parameters();
				setState(1446);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1448);
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
		enterRule(_localctx, 282, RULE_result);
		try {
			setState(1453);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1451);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1452);
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
		enterRule(_localctx, 284, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1455);
			match(L_PAREN);
			setState(1467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (ELLIPSIS - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
				{
				setState(1456);
				parameterDecl();
				setState(1461);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1457);
						match(COMMA);
						setState(1458);
						parameterDecl();
						}
						} 
					}
					setState(1463);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
				}
				setState(1465);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1464);
					match(COMMA);
					}
				}

				}
			}

			setState(1469);
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
		enterRule(_localctx, 286, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1471);
			nonNamedType();
			setState(1472);
			match(L_PAREN);
			setState(1473);
			expression(0);
			setState(1475);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1474);
				match(COMMA);
				}
			}

			setState(1477);
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
		enterRule(_localctx, 288, RULE_nonNamedType);
		try {
			setState(1484);
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
				setState(1479);
				typeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1480);
				match(L_PAREN);
				setState(1481);
				nonNamedType();
				setState(1482);
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
		enterRule(_localctx, 290, RULE_operand);
		try {
			setState(1492);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1486);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1487);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1488);
				match(L_PAREN);
				setState(1489);
				expression(0);
				setState(1490);
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
		enterRule(_localctx, 292, RULE_literal);
		try {
			setState(1497);
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
				setState(1494);
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
			case MAP:
			case STRUCT:
			case IDENTIFIER:
			case L_BRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(1495);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1496);
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
		enterRule(_localctx, 294, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1499);
			_la = _input.LA(1);
			if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (DECIMAL_LIT - 127)) | (1L << (BINARY_LIT - 127)) | (1L << (OCTAL_LIT - 127)) | (1L << (HEX_LIT - 127)) | (1L << (IMAGINARY_LIT - 127)) | (1L << (RUNE_LIT - 127)))) != 0)) ) {
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
		enterRule(_localctx, 296, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1501);
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
		enterRule(_localctx, 298, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1503);
			match(IDENTIFIER);
			setState(1504);
			match(DOT);
			setState(1505);
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
		enterRule(_localctx, 300, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1507);
			literalType();
			setState(1508);
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
		enterRule(_localctx, 302, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1510);
			match(L_CURLY);
			setState(1515);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_CURLY - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
				{
				setState(1511);
				elementList();
				setState(1513);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1512);
					match(COMMA);
					}
				}

				}
			}

			setState(1517);
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
		enterRule(_localctx, 304, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1519);
			keyedElement();
			setState(1524);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,149,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1520);
					match(COMMA);
					setState(1521);
					keyedElement();
					}
					} 
				}
				setState(1526);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,149,_ctx);
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
		enterRule(_localctx, 306, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1530);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(1527);
				key();
				setState(1528);
				match(COLON);
				}
				break;
			}
			setState(1532);
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
		enterRule(_localctx, 308, RULE_key);
		try {
			setState(1536);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case OLD:
			case FORALL:
			case EXISTS:
			case ACCESS:
			case UNFOLDING:
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
			case NONE:
			case PRED:
			case TYPE_OF:
			case IS_COMPARABLE:
			case WRITEPERM:
			case NOPERM:
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
				setState(1534);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1535);
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
		enterRule(_localctx, 310, RULE_element);
		try {
			setState(1540);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case OLD:
			case FORALL:
			case EXISTS:
			case ACCESS:
			case UNFOLDING:
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
			case NONE:
			case PRED:
			case TYPE_OF:
			case IS_COMPARABLE:
			case WRITEPERM:
			case NOPERM:
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
				setState(1538);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1539);
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
		enterRule(_localctx, 312, RULE_structType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1542);
			match(STRUCT);
			setState(1543);
			match(L_CURLY);
			setState(1549);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(1544);
				fieldDecl();
				setState(1545);
				eos();
				}
				}
				setState(1551);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1552);
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
		enterRule(_localctx, 314, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1558);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1554);
				identifierList();
				setState(1555);
				type_();
				}
				break;
			case 2:
				{
				setState(1557);
				embeddedField();
				}
				break;
			}
			setState(1561);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				{
				setState(1560);
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
		enterRule(_localctx, 316, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1563);
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
		enterRule(_localctx, 318, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1566);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1565);
				match(STAR);
				}
			}

			setState(1568);
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

	public static class FunctionLitContext extends ParserRuleContext {
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
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
		enterRule(_localctx, 320, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1570);
			match(FUNC);
			setState(1571);
			signature();
			setState(1572);
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
		enterRule(_localctx, 322, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1574);
			match(L_BRACKET);
			setState(1575);
			expression(0);
			setState(1576);
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
		enterRule(_localctx, 324, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1578);
			match(DOT);
			setState(1579);
			match(L_PAREN);
			setState(1580);
			type_();
			setState(1581);
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
		enterRule(_localctx, 326, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1583);
			match(L_PAREN);
			setState(1598);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (RANGE - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (HEX_LIT - 130)) | (1L << (IMAGINARY_LIT - 130)) | (1L << (RUNE_LIT - 130)) | (1L << (RAW_STRING_LIT - 130)) | (1L << (INTERPRETED_STRING_LIT - 130)))) != 0)) {
				{
				setState(1590);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
				case 1:
					{
					setState(1584);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1585);
					nonNamedType();
					setState(1588);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
					case 1:
						{
						setState(1586);
						match(COMMA);
						setState(1587);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1593);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1592);
					match(ELLIPSIS);
					}
				}

				setState(1596);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1595);
					match(COMMA);
					}
				}

				}
			}

			setState(1600);
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
		enterRule(_localctx, 328, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1602);
			nonNamedType();
			setState(1603);
			match(DOT);
			setState(1604);
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
		enterRule(_localctx, 330, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1606);
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
		enterRule(_localctx, 332, RULE_eos);
		try {
			setState(1612);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1608);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1609);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1610);
				match(EOS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1611);
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
		case 63:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 70:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 166:
			return eos_sempred((EosContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 11);
		case 1:
			return precpred(_ctx, 10);
		case 2:
			return precpred(_ctx, 9);
		case 3:
			return precpred(_ctx, 8);
		case 4:
			return precpred(_ctx, 7);
		case 5:
			return precpred(_ctx, 6);
		case 6:
			return precpred(_ctx, 5);
		case 7:
			return precpred(_ctx, 4);
		case 8:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean primaryExpr_sempred(PrimaryExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 8);
		case 10:
			return precpred(_ctx, 7);
		case 11:
			return precpred(_ctx, 6);
		case 12:
			return precpred(_ctx, 5);
		case 13:
			return precpred(_ctx, 4);
		case 14:
			return precpred(_ctx, 3);
		case 15:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean eos_sempred(EosContext _localctx, int predIndex) {
		switch (predIndex) {
		case 16:
			return closingBracket();
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0097\u0651\4\2\t"+
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
		"\4\u00a5\t\u00a5\4\u00a6\t\u00a6\4\u00a7\t\u00a7\4\u00a8\t\u00a8\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u015d\n\5\f\5\16\5\u0160"+
		"\13\5\3\6\3\6\5\6\u0164\n\6\3\7\3\7\3\7\3\7\3\7\7\7\u016b\n\7\f\7\16\7"+
		"\u016e\13\7\3\7\3\7\3\7\5\7\u0173\n\7\3\7\3\7\7\7\u0177\n\7\f\7\16\7\u017a"+
		"\13\7\3\7\3\7\3\b\3\b\3\b\3\b\5\b\u0182\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5"+
		"\t\u018a\n\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\5\f\u0194\n\f\3\f\3\f"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u01a3\n\r\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\7\20\u01af\n\20\f\20\16\20\u01b2"+
		"\13\20\3\20\5\20\u01b5\n\20\3\21\3\21\3\21\7\21\u01ba\n\21\f\21\16\21"+
		"\u01bd\13\21\3\21\3\21\3\22\7\22\u01c2\n\22\f\22\16\22\u01c5\13\22\3\23"+
		"\3\23\3\23\3\23\7\23\u01cb\n\23\f\23\16\23\u01ce\13\23\3\23\3\23\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\5\31\u01ed"+
		"\n\31\3\31\3\31\3\31\3\31\3\32\3\32\5\32\u01f5\n\32\3\33\3\33\3\34\3\34"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\5\36"+
		"\u0208\n\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \7"+
		" \u0217\n \f \16 \u021a\13 \3 \3 \3!\3!\3!\3!\3\"\3\"\3\"\5\"\u0225\n"+
		"\"\3#\3#\3#\3#\3#\7#\u022c\n#\f#\16#\u022f\13#\3#\3#\3$\3$\3$\3$\3$\3"+
		"$\3$\3$\3$\5$\u023c\n$\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3"+
		"&\5&\u024e\n&\3\'\3\'\3\'\3\'\3\'\5\'\u0255\n\'\3\'\7\'\u0258\n\'\f\'"+
		"\16\'\u025b\13\'\3\'\3\'\5\'\u025f\n\'\3(\3(\3(\3(\3(\3(\3(\3(\5(\u0269"+
		"\n(\3)\5)\u026c\n)\3)\3)\5)\u0270\n)\3*\3*\5*\u0274\n*\3+\3+\3+\3+\3+"+
		"\5+\u027b\n+\3+\5+\u027e\n+\3+\3+\3,\3,\3,\3,\3,\3,\3,\7,\u0289\n,\f,"+
		"\16,\u028c\13,\3,\3,\3,\7,\u0291\n,\f,\16,\u0294\13,\3,\5,\u0297\n,\3"+
		"-\5-\u029a\n-\3-\3-\3-\3-\5-\u02a0\n-\3.\3.\5.\u02a4\n.\3.\5.\u02a7\n"+
		".\3.\3.\3.\3/\3/\3/\3/\3/\5/\u02b1\n/\3\60\3\60\3\60\3\60\3\60\5\60\u02b8"+
		"\n\60\3\61\3\61\3\61\3\61\3\61\5\61\u02bf\n\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\62\3\62\3\63\3\63\3\63\5\63\u02cb\n\63\3\64\3\64\3\64\3\64\5\64\u02d1"+
		"\n\64\3\65\3\65\3\65\3\65\3\65\5\65\u02d8\n\65\3\66\3\66\3\66\5\66\u02dd"+
		"\n\66\3\67\3\67\3\67\3\67\5\67\u02e3\n\67\38\38\38\38\38\39\39\39\39\3"+
		"9\59\u02ef\n9\3:\3:\3:\3:\5:\u02f5\n:\3:\3:\5:\u02f9\n:\3;\3;\3;\3;\3"+
		"<\3<\5<\u0301\n<\3<\3<\5<\u0305\n<\3<\3<\3=\3=\5=\u030b\n=\3>\5>\u030e"+
		"\n>\3>\3>\3?\3?\5?\u0314\n?\3?\3?\3@\5@\u0319\n@\3@\3@\3A\3A\3A\3A\3A"+
		"\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\5A\u032d\nA\3A\3A\3A\3A\3A\3A\3A\3A"+
		"\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\7A"+
		"\u034d\nA\fA\16A\u0350\13A\3B\3B\3B\3B\3B\3B\3B\3B\3B\3B\3B\3B\3B\3B\3"+
		"B\3B\3B\3B\3B\5B\u0365\nB\3C\3C\3C\3D\3D\3D\5D\u036d\nD\3E\3E\3E\3F\3"+
		"F\3F\3F\7F\u0376\nF\fF\16F\u0379\13F\3F\3F\3F\3F\5F\u037f\nF\3G\3G\3G"+
		"\3G\3G\3G\3G\3G\5G\u0389\nG\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\5H\u0397"+
		"\nH\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\7H\u03a8\nH\fH\16H\u03ab"+
		"\13H\3I\3I\5I\u03af\nI\3I\5I\u03b2\nI\3I\3I\3J\3J\3J\3J\3J\5J\u03bb\n"+
		"J\3J\3J\7J\u03bf\nJ\fJ\16J\u03c2\13J\3J\3J\3K\3K\3K\3K\3L\5L\u03cb\nL"+
		"\3L\3L\3L\3L\3L\3L\5L\u03d3\nL\3L\3L\3L\3L\5L\u03d9\nL\3M\3M\3M\3M\3M"+
		"\3M\3M\5M\u03e2\nM\3N\3N\3N\3N\3N\3N\3N\3N\3N\5N\u03ed\nN\3O\3O\3O\3P"+
		"\3P\3P\3P\7P\u03f6\nP\fP\16P\u03f9\13P\3P\5P\u03fc\nP\5P\u03fe\nP\3P\3"+
		"P\3Q\3Q\3Q\3Q\3Q\3Q\3Q\5Q\u0409\nQ\3R\3R\3R\3R\3R\3S\3S\5S\u0412\nS\3"+
		"S\3S\5S\u0416\nS\3S\5S\u0419\nS\3S\3S\3S\3S\3S\5S\u0420\nS\3S\3S\3T\3"+
		"T\3U\3U\3V\3V\3W\5W\u042b\nW\3W\3W\3X\3X\3X\3Y\3Y\3Y\3Y\3Y\3Y\7Y\u0438"+
		"\nY\fY\16Y\u043b\13Y\3Y\5Y\u043e\nY\3Z\5Z\u0441\nZ\3Z\3Z\3[\3[\3\\\3\\"+
		"\3\\\5\\\u044a\n\\\3]\3]\3]\3]\3]\3]\7]\u0452\n]\f]\16]\u0455\13]\3]\5"+
		"]\u0458\n]\3^\3^\5^\u045c\n^\3^\3^\5^\u0460\n^\3_\3_\3_\7_\u0465\n_\f"+
		"_\16_\u0468\13_\3`\3`\3`\7`\u046d\n`\f`\16`\u0470\13`\3a\3a\3a\3a\3a\3"+
		"a\7a\u0478\na\fa\16a\u047b\13a\3a\5a\u047e\na\3b\3b\5b\u0482\nb\3b\3b"+
		"\3c\3c\3c\3c\3c\3c\7c\u048c\nc\fc\16c\u048f\13c\3c\5c\u0492\nc\3d\3d\5"+
		"d\u0496\nd\3d\3d\3e\5e\u049b\ne\3e\3e\3e\6e\u04a0\ne\re\16e\u04a1\3f\3"+
		"f\3f\3f\3f\5f\u04a9\nf\3g\3g\3h\3h\3h\3h\3i\3i\3i\3j\3j\3j\3j\3k\3k\3"+
		"l\3l\3l\5l\u04bd\nl\3m\3m\5m\u04c1\nm\3n\3n\5n\u04c5\nn\3o\3o\5o\u04c9"+
		"\no\3p\3p\3p\3q\3q\3r\3r\3r\3s\3s\3s\3s\3s\3s\3s\3s\3s\5s\u04dc\ns\3s"+
		"\3s\3s\3s\5s\u04e2\ns\5s\u04e4\ns\3t\3t\5t\u04e8\nt\3u\3u\5u\u04ec\nu"+
		"\3u\5u\u04ef\nu\3u\3u\5u\u04f3\nu\5u\u04f5\nu\3u\3u\7u\u04f9\nu\fu\16"+
		"u\u04fc\13u\3u\3u\3v\3v\3v\5v\u0503\nv\3w\3w\3w\5w\u0508\nw\3x\3x\3x\3"+
		"x\3x\3x\3x\3x\3x\5x\u0513\nx\3x\3x\7x\u0517\nx\fx\16x\u051a\13x\3x\3x"+
		"\3y\3y\5y\u0520\ny\3y\3y\3y\3y\3y\3y\3z\3z\3z\5z\u052b\nz\3{\3{\3{\5{"+
		"\u0530\n{\3|\3|\5|\u0534\n|\3|\3|\3|\5|\u0539\n|\7|\u053b\n|\f|\16|\u053e"+
		"\13|\3}\3}\3}\7}\u0543\n}\f}\16}\u0546\13}\3}\3}\3~\3~\3~\5~\u054d\n~"+
		"\3\177\3\177\3\177\5\177\u0552\n\177\3\177\5\177\u0555\n\177\3\u0080\3"+
		"\u0080\3\u0080\3\u0080\3\u0080\3\u0080\5\u0080\u055d\n\u0080\3\u0080\3"+
		"\u0080\3\u0081\3\u0081\3\u0081\3\u0081\5\u0081\u0565\n\u0081\3\u0081\3"+
		"\u0081\3\u0082\5\u0082\u056a\n\u0082\3\u0082\3\u0082\5\u0082\u056e\n\u0082"+
		"\3\u0082\3\u0082\5\u0082\u0572\n\u0082\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\5\u0083\u057a\n\u0083\3\u0083\3\u0083\3\u0083\3\u0084"+
		"\3\u0084\3\u0084\3\u0085\3\u0085\5\u0085\u0584\n\u0085\3\u0086\3\u0086"+
		"\3\u0086\3\u0086\3\u0086\3\u0087\3\u0087\3\u0088\3\u0088\3\u0089\3\u0089"+
		"\3\u0089\3\u008a\3\u008a\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b"+
		"\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\5\u008c\u05a1"+
		"\n\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e"+
		"\3\u008e\5\u008e\u05ac\n\u008e\3\u008f\3\u008f\5\u008f\u05b0\n\u008f\3"+
		"\u0090\3\u0090\3\u0090\3\u0090\7\u0090\u05b6\n\u0090\f\u0090\16\u0090"+
		"\u05b9\13\u0090\3\u0090\5\u0090\u05bc\n\u0090\5\u0090\u05be\n\u0090\3"+
		"\u0090\3\u0090\3\u0091\3\u0091\3\u0091\3\u0091\5\u0091\u05c6\n\u0091\3"+
		"\u0091\3\u0091\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\5\u0092\u05cf\n"+
		"\u0092\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\5\u0093\u05d7\n"+
		"\u0093\3\u0094\3\u0094\3\u0094\5\u0094\u05dc\n\u0094\3\u0095\3\u0095\3"+
		"\u0096\3\u0096\3\u0097\3\u0097\3\u0097\3\u0097\3\u0098\3\u0098\3\u0098"+
		"\3\u0099\3\u0099\3\u0099\5\u0099\u05ec\n\u0099\5\u0099\u05ee\n\u0099\3"+
		"\u0099\3\u0099\3\u009a\3\u009a\3\u009a\7\u009a\u05f5\n\u009a\f\u009a\16"+
		"\u009a\u05f8\13\u009a\3\u009b\3\u009b\3\u009b\5\u009b\u05fd\n\u009b\3"+
		"\u009b\3\u009b\3\u009c\3\u009c\5\u009c\u0603\n\u009c\3\u009d\3\u009d\5"+
		"\u009d\u0607\n\u009d\3\u009e\3\u009e\3\u009e\3\u009e\3\u009e\7\u009e\u060e"+
		"\n\u009e\f\u009e\16\u009e\u0611\13\u009e\3\u009e\3\u009e\3\u009f\3\u009f"+
		"\3\u009f\3\u009f\5\u009f\u0619\n\u009f\3\u009f\5\u009f\u061c\n\u009f\3"+
		"\u00a0\3\u00a0\3\u00a1\5\u00a1\u0621\n\u00a1\3\u00a1\3\u00a1\3\u00a2\3"+
		"\u00a2\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a3\3\u00a3\3\u00a4\3\u00a4"+
		"\3\u00a4\3\u00a4\3\u00a4\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5\5\u00a5"+
		"\u0637\n\u00a5\5\u00a5\u0639\n\u00a5\3\u00a5\5\u00a5\u063c\n\u00a5\3\u00a5"+
		"\5\u00a5\u063f\n\u00a5\5\u00a5\u0641\n\u00a5\3\u00a5\3\u00a5\3\u00a6\3"+
		"\u00a6\3\u00a6\3\u00a6\3\u00a7\3\u00a7\3\u00a8\3\u00a8\3\u00a8\3\u00a8"+
		"\5\u00a8\u064f\n\u00a8\3\u00a8\3\u0259\4\u0080\u008e\u00a9\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^"+
		"`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"+
		"\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120"+
		"\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138"+
		"\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c\u014e\2\25"+
		"\3\2\27\30\3\2\7\n\3\2>?\3\2\')\4\2\')++\3\2z\u0080\3\2\24\25\4\2uy~\177"+
		"\6\2\"\"hhtt{}\3\2\36 \3\2\33\35\3\2ns\6\2,,//\62\62TT\4\2ty{\177\4\2"+
		"\\\\gg\3\2hi\4\2ee\u0096\u0096\4\2\u0081\u0084\u0086\u0087\3\2\u008d\u008e"+
		"\2\u06a5\2\u0150\3\2\2\2\4\u0153\3\2\2\2\6\u0156\3\2\2\2\b\u0159\3\2\2"+
		"\2\n\u0161\3\2\2\2\f\u0165\3\2\2\2\16\u0181\3\2\2\2\20\u0189\3\2\2\2\22"+
		"\u018b\3\2\2\2\24\u018d\3\2\2\2\26\u0190\3\2\2\2\30\u01a2\3\2\2\2\32\u01a4"+
		"\3\2\2\2\34\u01a6\3\2\2\2\36\u01ab\3\2\2\2 \u01b6\3\2\2\2\"\u01c3\3\2"+
		"\2\2$\u01c6\3\2\2\2&\u01d1\3\2\2\2(\u01d3\3\2\2\2*\u01d8\3\2\2\2,\u01dd"+
		"\3\2\2\2.\u01e2\3\2\2\2\60\u01e7\3\2\2\2\62\u01f4\3\2\2\2\64\u01f6\3\2"+
		"\2\2\66\u01f8\3\2\2\28\u01fd\3\2\2\2:\u0202\3\2\2\2<\u020b\3\2\2\2>\u0212"+
		"\3\2\2\2@\u021d\3\2\2\2B\u0224\3\2\2\2D\u0226\3\2\2\2F\u023b\3\2\2\2H"+
		"\u023d\3\2\2\2J\u024d\3\2\2\2L\u0259\3\2\2\2N\u0268\3\2\2\2P\u026b\3\2"+
		"\2\2R\u0273\3\2\2\2T\u0275\3\2\2\2V\u0281\3\2\2\2X\u0299\3\2\2\2Z\u02a1"+
		"\3\2\2\2\\\u02b0\3\2\2\2^\u02b2\3\2\2\2`\u02b9\3\2\2\2b\u02c2\3\2\2\2"+
		"d\u02c7\3\2\2\2f\u02cc\3\2\2\2h\u02d2\3\2\2\2j\u02d9\3\2\2\2l\u02de\3"+
		"\2\2\2n\u02e4\3\2\2\2p\u02e9\3\2\2\2r\u02f0\3\2\2\2t\u02fa\3\2\2\2v\u02fe"+
		"\3\2\2\2x\u030a\3\2\2\2z\u030d\3\2\2\2|\u0311\3\2\2\2~\u0318\3\2\2\2\u0080"+
		"\u032c\3\2\2\2\u0082\u0364\3\2\2\2\u0084\u0366\3\2\2\2\u0086\u0369\3\2"+
		"\2\2\u0088\u036e\3\2\2\2\u008a\u0377\3\2\2\2\u008c\u0388\3\2\2\2\u008e"+
		"\u0396\3\2\2\2\u0090\u03ac\3\2\2\2\u0092\u03b5\3\2\2\2\u0094\u03c5\3\2"+
		"\2\2\u0096\u03d8\3\2\2\2\u0098\u03e1\3\2\2\2\u009a\u03ec\3\2\2\2\u009c"+
		"\u03ee\3\2\2\2\u009e\u03f1\3\2\2\2\u00a0\u0408\3\2\2\2\u00a2\u040a\3\2"+
		"\2\2\u00a4\u040f\3\2\2\2\u00a6\u0423\3\2\2\2\u00a8\u0425\3\2\2\2\u00aa"+
		"\u0427\3\2\2\2\u00ac\u042a\3\2\2\2\u00ae\u042e\3\2\2\2\u00b0\u0431\3\2"+
		"\2\2\u00b2\u0440\3\2\2\2\u00b4\u0444\3\2\2\2\u00b6\u0449\3\2\2\2\u00b8"+
		"\u044b\3\2\2\2\u00ba\u0459\3\2\2\2\u00bc\u0461\3\2\2\2\u00be\u0469\3\2"+
		"\2\2\u00c0\u0471\3\2\2\2\u00c2\u047f\3\2\2\2\u00c4\u0485\3\2\2\2\u00c6"+
		"\u0493\3\2\2\2\u00c8\u049f\3\2\2\2\u00ca\u04a8\3\2\2\2\u00cc\u04aa\3\2"+
		"\2\2\u00ce\u04ac\3\2\2\2\u00d0\u04b0\3\2\2\2\u00d2\u04b3\3\2\2\2\u00d4"+
		"\u04b7\3\2\2\2\u00d6\u04b9\3\2\2\2\u00d8\u04be\3\2\2\2\u00da\u04c2\3\2"+
		"\2\2\u00dc\u04c6\3\2\2\2\u00de\u04ca\3\2\2\2\u00e0\u04cd\3\2\2\2\u00e2"+
		"\u04cf\3\2\2\2\u00e4\u04d2\3\2\2\2\u00e6\u04e7\3\2\2\2\u00e8\u04e9\3\2"+
		"\2\2\u00ea\u04ff\3\2\2\2\u00ec\u0507\3\2\2\2\u00ee\u0509\3\2\2\2\u00f0"+
		"\u051f\3\2\2\2\u00f2\u0527\3\2\2\2\u00f4\u052f\3\2\2\2\u00f6\u0533\3\2"+
		"\2\2\u00f8\u053f\3\2\2\2\u00fa\u0549\3\2\2\2\u00fc\u0554\3\2\2\2\u00fe"+
		"\u055c\3\2\2\2\u0100\u0560\3\2\2\2\u0102\u0569\3\2\2\2\u0104\u0579\3\2"+
		"\2\2\u0106\u057e\3\2\2\2\u0108\u0583\3\2\2\2\u010a\u0585\3\2\2\2\u010c"+
		"\u058a\3\2\2\2\u010e\u058c\3\2\2\2\u0110\u058e\3\2\2\2\u0112\u0591\3\2"+
		"\2\2\u0114\u0595\3\2\2\2\u0116\u05a0\3\2\2\2\u0118\u05a4\3\2\2\2\u011a"+
		"\u05ab\3\2\2\2\u011c\u05af\3\2\2\2\u011e\u05b1\3\2\2\2\u0120\u05c1\3\2"+
		"\2\2\u0122\u05ce\3\2\2\2\u0124\u05d6\3\2\2\2\u0126\u05db\3\2\2\2\u0128"+
		"\u05dd\3\2\2\2\u012a\u05df\3\2\2\2\u012c\u05e1\3\2\2\2\u012e\u05e5\3\2"+
		"\2\2\u0130\u05e8\3\2\2\2\u0132\u05f1\3\2\2\2\u0134\u05fc\3\2\2\2\u0136"+
		"\u0602\3\2\2\2\u0138\u0606\3\2\2\2\u013a\u0608\3\2\2\2\u013c\u0618\3\2"+
		"\2\2\u013e\u061d\3\2\2\2\u0140\u0620\3\2\2\2\u0142\u0624\3\2\2\2\u0144"+
		"\u0628\3\2\2\2\u0146\u062c\3\2\2\2\u0148\u0631\3\2\2\2\u014a\u0644\3\2"+
		"\2\2\u014c\u0648\3\2\2\2\u014e\u064e\3\2\2\2\u0150\u0151\5\u0080A\2\u0151"+
		"\u0152\7\2\2\3\u0152\3\3\2\2\2\u0153\u0154\5\u0082B\2\u0154\u0155\7\2"+
		"\2\3\u0155\5\3\2\2\2\u0156\u0157\5\u0098M\2\u0157\u0158\7\2\2\3\u0158"+
		"\7\3\2\2\2\u0159\u015e\5\n\6\2\u015a\u015b\7d\2\2\u015b\u015d\5\n\6\2"+
		"\u015c\u015a\3\2\2\2\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2\u015e\u015f"+
		"\3\2\2\2\u015f\t\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0163\7\\\2\2\u0162"+
		"\u0164\79\2\2\u0163\u0162\3\2\2\2\u0163\u0164\3\2\2\2\u0164\13\3\2\2\2"+
		"\u0165\u0166\5\u00aeX\2\u0166\u016c\5\u014e\u00a8\2\u0167\u0168\5\u00b0"+
		"Y\2\u0168\u0169\5\u014e\u00a8\2\u0169\u016b\3\2\2\2\u016a\u0167\3\2\2"+
		"\2\u016b\u016e\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u0178"+
		"\3\2\2\2\u016e\u016c\3\2\2\2\u016f\u0173\5d\63\2\u0170\u0173\5\u00b6\\"+
		"\2\u0171\u0173\5\16\b\2\u0172\u016f\3\2\2\2\u0172\u0170\3\2\2\2\u0172"+
		"\u0171\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0175\5\u014e\u00a8\2\u0175\u0177"+
		"\3\2\2\2\u0176\u0172\3\2\2\2\u0177\u017a\3\2\2\2\u0178\u0176\3\2\2\2\u0178"+
		"\u0179\3\2\2\2\u0179\u017b\3\2\2\2\u017a\u0178\3\2\2\2\u017b\u017c\7\2"+
		"\2\3\u017c\r\3\2\2\2\u017d\u0182\5V,\2\u017e\u0182\5l\67\2\u017f\u0182"+
		"\5p9\2\u0180\u0182\5j\66\2\u0181\u017d\3\2\2\2\u0181\u017e\3\2\2\2\u0181"+
		"\u017f\3\2\2\2\u0181\u0180\3\2\2\2\u0182\17\3\2\2\2\u0183\u0184\7\32\2"+
		"\2\u0184\u018a\5\u0082B\2\u0185\u0186\t\2\2\2\u0186\u018a\5&\24\2\u0187"+
		"\u0188\t\3\2\2\u0188\u018a\5\u0080A\2\u0189\u0183\3\2\2\2\u0189\u0185"+
		"\3\2\2\2\u0189\u0187\3\2\2\2\u018a\21\3\2\2\2\u018b\u018c\5\24\13\2\u018c"+
		"\23\3\2\2\2\u018d\u018e\5L\'\2\u018e\u018f\5\26\f\2\u018f\25\3\2\2\2\u0190"+
		"\u0191\7A\2\2\u0191\u0193\7]\2\2\u0192\u0194\5\u00c8e\2\u0193\u0192\3"+
		"\2\2\2\u0193\u0194\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0196\7^\2\2\u0196"+
		"\27\3\2\2\2\u0197\u01a3\5<\37\2\u0198\u01a3\5:\36\2\u0199\u01a3\58\35"+
		"\2\u019a\u01a3\5\34\17\2\u019b\u01a3\5\66\34\2\u019c\u01a3\5\60\31\2\u019d"+
		"\u01a3\5.\30\2\u019e\u01a3\5*\26\2\u019f\u01a3\5(\25\2\u01a0\u01a3\5,"+
		"\27\2\u01a1\u01a3\5\32\16\2\u01a2\u0197\3\2\2\2\u01a2\u0198\3\2\2\2\u01a2"+
		"\u0199\3\2\2\2\u01a2\u019a\3\2\2\2\u01a2\u019b\3\2\2\2\u01a2\u019c\3\2"+
		"\2\2\u01a2\u019d\3\2\2\2\u01a2\u019e\3\2\2\2\u01a2\u019f\3\2\2\2\u01a2"+
		"\u01a0\3\2\2\2\u01a2\u01a1\3\2\2\2\u01a3\31\3\2\2\2\u01a4\u01a5\t\4\2"+
		"\2\u01a5\33\3\2\2\2\u01a6\u01a7\7U\2\2\u01a7\u01a8\7a\2\2\u01a8\u01a9"+
		"\5\u0098M\2\u01a9\u01aa\7b\2\2\u01aa\35\3\2\2\2\u01ab\u01b0\5 \21\2\u01ac"+
		"\u01ad\7d\2\2\u01ad\u01af\5 \21\2\u01ae\u01ac\3\2\2\2\u01af\u01b2\3\2"+
		"\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b4\3\2\2\2\u01b2"+
		"\u01b0\3\2\2\2\u01b3\u01b5\7d\2\2\u01b4\u01b3\3\2\2\2\u01b4\u01b5\3\2"+
		"\2\2\u01b5\37\3\2\2\2\u01b6\u01bb\7\\\2\2\u01b7\u01b8\7d\2\2\u01b8\u01ba"+
		"\7\\\2\2\u01b9\u01b7\3\2\2\2\u01ba\u01bd\3\2\2\2\u01bb\u01b9\3\2\2\2\u01bb"+
		"\u01bc\3\2\2\2\u01bc\u01be\3\2\2\2\u01bd\u01bb\3\2\2\2\u01be\u01bf\5\u010e"+
		"\u0088\2\u01bf!\3\2\2\2\u01c0\u01c2\5$\23\2\u01c1\u01c0\3\2\2\2\u01c2"+
		"\u01c5\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4#\3\2\2\2"+
		"\u01c5\u01c3\3\2\2\2\u01c6\u01c7\7_\2\2\u01c7\u01cc\5\u0080A\2\u01c8\u01c9"+
		"\7d\2\2\u01c9\u01cb\5\u0080A\2\u01ca\u01c8\3\2\2\2\u01cb\u01ce\3\2\2\2"+
		"\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd\u01cf\3\2\2\2\u01ce\u01cc"+
		"\3\2\2\2\u01cf\u01d0\7`\2\2\u01d0%\3\2\2\2\u01d1\u01d2\5\u008eH\2\u01d2"+
		"\'\3\2\2\2\u01d3\u01d4\7\60\2\2\u01d4\u01d5\7]\2\2\u01d5\u01d6\5\u0080"+
		"A\2\u01d6\u01d7\7^\2\2\u01d7)\3\2\2\2\u01d8\u01d9\7\64\2\2\u01d9\u01da"+
		"\7a\2\2\u01da\u01db\5\u0098M\2\u01db\u01dc\7b\2\2\u01dc+\3\2\2\2\u01dd"+
		"\u01de\7\61\2\2\u01de\u01df\7]\2\2\u01df\u01e0\5\u0080A\2\u01e0\u01e1"+
		"\7^\2\2\u01e1-\3\2\2\2\u01e2\u01e3\t\5\2\2\u01e3\u01e4\7]\2\2\u01e4\u01e5"+
		"\5\u0080A\2\u01e5\u01e6\7^\2\2\u01e6/\3\2\2\2\u01e7\u01ec\7\22\2\2\u01e8"+
		"\u01e9\7a\2\2\u01e9\u01ea\5\62\32\2\u01ea\u01eb\7b\2\2\u01eb\u01ed\3\2"+
		"\2\2\u01ec\u01e8\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee"+
		"\u01ef\7]\2\2\u01ef\u01f0\5\u0080A\2\u01f0\u01f1\7^\2\2\u01f1\61\3\2\2"+
		"\2\u01f2\u01f5\5\64\33\2\u01f3\u01f5\7\23\2\2\u01f4\u01f2\3\2\2\2\u01f4"+
		"\u01f3\3\2\2\2\u01f5\63\3\2\2\2\u01f6\u01f7\7\\\2\2\u01f7\65\3\2\2\2\u01f8"+
		"\u01f9\7\67\2\2\u01f9\u01fa\7]\2\2\u01fa\u01fb\5\u0080A\2\u01fb\u01fc"+
		"\7^\2\2\u01fc\67\3\2\2\2\u01fd\u01fe\7\66\2\2\u01fe\u01ff\7]\2\2\u01ff"+
		"\u0200\5\u0080A\2\u0200\u0201\7^\2\2\u02019\3\2\2\2\u0202\u0203\7\26\2"+
		"\2\u0203\u0204\7]\2\2\u0204\u0207\5\u0080A\2\u0205\u0206\7d\2\2\u0206"+
		"\u0208\5\u0080A\2\u0207\u0205\3\2\2\2\u0207\u0208\3\2\2\2\u0208\u0209"+
		"\3\2\2\2\u0209\u020a\7^\2\2\u020a;\3\2\2\2\u020b\u020c\t\5\2\2\u020c\u020d"+
		"\7a\2\2\u020d\u020e\5\u0080A\2\u020e\u020f\7:\2\2\u020f\u0210\5\u0080"+
		"A\2\u0210\u0211\7b\2\2\u0211=\3\2\2\2\u0212\u0213\7a\2\2\u0213\u0218\5"+
		"@!\2\u0214\u0215\7d\2\2\u0215\u0217\5@!\2\u0216\u0214\3\2\2\2\u0217\u021a"+
		"\3\2\2\2\u0218\u0216\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021b\3\2\2\2\u021a"+
		"\u0218\3\2\2\2\u021b\u021c\7b\2\2\u021c?\3\2\2\2\u021d\u021e\5\u0080A"+
		"\2\u021e\u021f\7c\2\2\u021f\u0220\5\u0080A\2\u0220A\3\2\2\2\u0221\u0225"+
		"\5J&\2\u0222\u0225\5H%\2\u0223\u0225\5D#\2\u0224\u0221\3\2\2\2\u0224\u0222"+
		"\3\2\2\2\u0224\u0223\3\2\2\2\u0225C\3\2\2\2\u0226\u0227\7\62\2\2\u0227"+
		"\u022d\7_\2\2\u0228\u0229\5F$\2\u0229\u022a\5\u014e\u00a8\2\u022a\u022c"+
		"\3\2\2\2\u022b\u0228\3\2\2\2\u022c\u022f\3\2\2\2\u022d\u022b\3\2\2\2\u022d"+
		"\u022e\3\2\2\2\u022e\u0230\3\2\2\2\u022f\u022d\3\2\2\2\u0230\u0231\7`"+
		"\2\2\u0231E\3\2\2\2\u0232\u0233\7D\2\2\u0233\u0234\7\\\2\2\u0234\u023c"+
		"\5\u011a\u008e\2\u0235\u0236\7\63\2\2\u0236\u0237\7_\2\2\u0237\u0238\5"+
		"\u0080A\2\u0238\u0239\5\u014e\u00a8\2\u0239\u023a\7`\2\2\u023a\u023c\3"+
		"\2\2\2\u023b\u0232\3\2\2\2\u023b\u0235\3\2\2\2\u023cG\3\2\2\2\u023d\u023e"+
		"\7\32\2\2\u023e\u023f\7a\2\2\u023f\u0240\7b\2\2\u0240\u0241\5\u010e\u0088"+
		"\2\u0241I\3\2\2\2\u0242\u0243\t\6\2\2\u0243\u0244\7a\2\2\u0244\u0245\5"+
		"\u0098M\2\u0245\u0246\7b\2\2\u0246\u024e\3\2\2\2\u0247\u0248\7*\2\2\u0248"+
		"\u0249\7a\2\2\u0249\u024a\5\u0098M\2\u024a\u024b\7b\2\2\u024b\u024c\5"+
		"\u0098M\2\u024c\u024e\3\2\2\2\u024d\u0242\3\2\2\2\u024d\u0247\3\2\2\2"+
		"\u024eK\3\2\2\2\u024f\u0255\5N(\2\u0250\u0251\7\20\2\2\u0251\u0255\b\'"+
		"\1\2\u0252\u0253\7@\2\2\u0253\u0255\b\'\1\2\u0254\u024f\3\2\2\2\u0254"+
		"\u0250\3\2\2\2\u0254\u0252\3\2\2\2\u0255\u0256\3\2\2\2\u0256\u0258\5\u014e"+
		"\u00a8\2\u0257\u0254\3\2\2\2\u0258\u025b\3\2\2\2\u0259\u025a\3\2\2\2\u0259"+
		"\u0257\3\2\2\2\u025a\u025e\3\2\2\2\u025b\u0259\3\2\2\2\u025c\u025d\7\20"+
		"\2\2\u025d\u025f\b\'\1\2\u025e\u025c\3\2\2\2\u025e\u025f\3\2\2\2\u025f"+
		"M\3\2\2\2\u0260\u0261\7\13\2\2\u0261\u0269\5R*\2\u0262\u0263\7\f\2\2\u0263"+
		"\u0269\5R*\2\u0264\u0265\7\r\2\2\u0265\u0269\5R*\2\u0266\u0267\7\17\2"+
		"\2\u0267\u0269\5P)\2\u0268\u0260\3\2\2\2\u0268\u0262\3\2\2\2\u0268\u0264"+
		"\3\2\2\2\u0268\u0266\3\2\2\2\u0269O\3\2\2\2\u026a\u026c\5\u00be`\2\u026b"+
		"\u026a\3\2\2\2\u026b\u026c\3\2\2\2\u026c\u026f\3\2\2\2\u026d\u026e\7S"+
		"\2\2\u026e\u0270\5\u0080A\2\u026f\u026d\3\2\2\2\u026f\u0270\3\2\2\2\u0270"+
		"Q\3\2\2\2\u0271\u0274\3\2\2\2\u0272\u0274\5\u0080A\2\u0273\u0271\3\2\2"+
		"\2\u0273\u0272\3\2\2\2\u0274S\3\2\2\2\u0275\u027a\7_\2\2\u0276\u0277\7"+
		"8\2\2\u0277\u0278\5\u00bc_\2\u0278\u0279\5\u014e\u00a8\2\u0279\u027b\3"+
		"\2\2\2\u027a\u0276\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027d\3\2\2\2\u027c"+
		"\u027e\5\u00c8e\2\u027d\u027c\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u027f"+
		"\3\2\2\2\u027f\u0280\7`\2\2\u0280U\3\2\2\2\u0281\u0282\5\u0098M\2\u0282"+
		"\u0283\7\21\2\2\u0283\u0296\5\u0098M\2\u0284\u028a\7_\2\2\u0285\u0286"+
		"\5^\60\2\u0286\u0287\5\u014e\u00a8\2\u0287\u0289\3\2\2\2\u0288\u0285\3"+
		"\2\2\2\u0289\u028c\3\2\2\2\u028a\u0288\3\2\2\2\u028a\u028b\3\2\2\2\u028b"+
		"\u0292\3\2\2\2\u028c\u028a\3\2\2\2\u028d\u028e\5X-\2\u028e\u028f\5\u014e"+
		"\u00a8\2\u028f\u0291\3\2\2\2\u0290\u028d\3\2\2\2\u0291\u0294\3\2\2\2\u0292"+
		"\u0290\3\2\2\2\u0292\u0293\3\2\2\2\u0293\u0295\3\2\2\2\u0294\u0292\3\2"+
		"\2\2\u0295\u0297\7`\2\2\u0296\u0284\3\2\2\2\u0296\u0297\3\2\2\2\u0297"+
		"W\3\2\2\2\u0298\u029a\7\20\2\2\u0299\u0298\3\2\2\2\u0299\u029a\3\2\2\2"+
		"\u029a\u029b\3\2\2\2\u029b\u029c\5Z.\2\u029c\u029d\7\\\2\2\u029d\u029f"+
		"\5\u011a\u008e\2\u029e\u02a0\5\u00c6d\2\u029f\u029e\3\2\2\2\u029f\u02a0"+
		"\3\2\2\2\u02a0Y\3\2\2\2\u02a1\u02a3\7]\2\2\u02a2\u02a4\7\\\2\2\u02a3\u02a2"+
		"\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a6\3\2\2\2\u02a5\u02a7\7~\2\2\u02a6"+
		"\u02a5\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02a8\3\2\2\2\u02a8\u02a9\5\u0108"+
		"\u0085\2\u02a9\u02aa\7^\2\2\u02aa[\3\2\2\2\u02ab\u02b1\5\u008eH\2\u02ac"+
		"\u02ad\5\u0098M\2\u02ad\u02ae\7g\2\2\u02ae\u02af\7\\\2\2\u02af\u02b1\3"+
		"\2\2\2\u02b0\u02ab\3\2\2\2\u02b0\u02ac\3\2\2\2\u02b1]\3\2\2\2\u02b2\u02b3"+
		"\7\65\2\2\u02b3\u02b4\7\\\2\2\u02b4\u02b7\7j\2\2\u02b5\u02b8\5\\/\2\u02b6"+
		"\u02b8\5\u012a\u0096\2\u02b7\u02b5\3\2\2\2\u02b7\u02b6\3\2\2\2\u02b8_"+
		"\3\2\2\2\u02b9\u02ba\7.\2\2\u02ba\u02bb\7]\2\2\u02bb\u02be\5\u0098M\2"+
		"\u02bc\u02bd\7d\2\2\u02bd\u02bf\5\u00be`\2\u02be\u02bc\3\2\2\2\u02be\u02bf"+
		"\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0\u02c1\7^\2\2\u02c1a\3\2\2\2\u02c2\u02c3"+
		"\7-\2\2\u02c3\u02c4\7]\2\2\u02c4\u02c5\5\u0098M\2\u02c5\u02c6\7^\2\2\u02c6"+
		"c\3\2\2\2\u02c7\u02ca\5L\'\2\u02c8\u02cb\5f\64\2\u02c9\u02cb\5h\65\2\u02ca"+
		"\u02c8\3\2\2\2\u02ca\u02c9\3\2\2\2\u02cbe\3\2\2\2\u02cc\u02cd\7D\2\2\u02cd"+
		"\u02ce\7\\\2\2\u02ce\u02d0\5\u011a\u008e\2\u02cf\u02d1\5T+\2\u02d0\u02cf"+
		"\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1g\3\2\2\2\u02d2\u02d3\7D\2\2\u02d3\u02d4"+
		"\5v<\2\u02d4\u02d5\7\\\2\2\u02d5\u02d7\5\u011a\u008e\2\u02d6\u02d8\5T"+
		"+\2\u02d7\u02d6\3\2\2\2\u02d7\u02d8\3\2\2\2\u02d8i\3\2\2\2\u02d9\u02dc"+
		"\7\32\2\2\u02da\u02dd\5d\63\2\u02db\u02dd\5\u00b6\\\2\u02dc\u02da\3\2"+
		"\2\2\u02dc\u02db\3\2\2\2\u02ddk\3\2\2\2\u02de\u02df\7\65\2\2\u02df\u02e0"+
		"\7\\\2\2\u02e0\u02e2\5\u011e\u0090\2\u02e1\u02e3\5n8\2\u02e2\u02e1\3\2"+
		"\2\2\u02e2\u02e3\3\2\2\2\u02e3m\3\2\2\2\u02e4\u02e5\7_\2\2\u02e5\u02e6"+
		"\5\u0080A\2\u02e6\u02e7\5\u014e\u00a8\2\u02e7\u02e8\7`\2\2\u02e8o\3\2"+
		"\2\2\u02e9\u02ea\7\65\2\2\u02ea\u02eb\5v<\2\u02eb\u02ec\7\\\2\2\u02ec"+
		"\u02ee\5\u011e\u0090\2\u02ed\u02ef\5n8\2\u02ee\u02ed\3\2\2\2\u02ee\u02ef"+
		"\3\2\2\2\u02efq\3\2\2\2\u02f0\u02f8\5\b\5\2\u02f1\u02f4\5\u0098M\2\u02f2"+
		"\u02f3\7c\2\2\u02f3\u02f5\5\u00be`\2\u02f4\u02f2\3\2\2\2\u02f4\u02f5\3"+
		"\2\2\2\u02f5\u02f9\3\2\2\2\u02f6\u02f7\7c\2\2\u02f7\u02f9\5\u00be`\2\u02f8"+
		"\u02f1\3\2\2\2\u02f8\u02f6\3\2\2\2\u02f9s\3\2\2\2\u02fa\u02fb\5\b\5\2"+
		"\u02fb\u02fc\7j\2\2\u02fc\u02fd\5\u00be`\2\u02fdu\3\2\2\2\u02fe\u0300"+
		"\7]\2\2\u02ff\u0301\5\n\6\2\u0300\u02ff\3\2\2\2\u0300\u0301\3\2\2\2\u0301"+
		"\u0302\3\2\2\2\u0302\u0304\5\u0098M\2\u0303\u0305\7d\2\2\u0304\u0303\3"+
		"\2\2\2\u0304\u0305\3\2\2\2\u0305\u0306\3\2\2\2\u0306\u0307\7^\2\2\u0307"+
		"w\3\2\2\2\u0308\u030b\5z>\2\u0309\u030b\5|?\2\u030a\u0308\3\2\2\2\u030a"+
		"\u0309\3\2\2\2\u030by\3\2\2\2\u030c\u030e\5\u00bc_\2\u030d\u030c\3\2\2"+
		"\2\u030d\u030e\3\2\2\2\u030e\u030f\3\2\2\2\u030f\u0310\5~@\2\u0310{\3"+
		"\2\2\2\u0311\u0313\7\32\2\2\u0312\u0314\5\u00bc_\2\u0313\u0312\3\2\2\2"+
		"\u0313\u0314\3\2\2\2\u0314\u0315\3\2\2\2\u0315\u0316\5~@\2\u0316}\3\2"+
		"\2\2\u0317\u0319\7k\2\2\u0318\u0317\3\2\2\2\u0318\u0319\3\2\2\2\u0319"+
		"\u031a\3\2\2\2\u031a\u031b\5\u0098M\2\u031b\177\3\2\2\2\u031c\u031d\b"+
		"A\1\2\u031d\u031e\t\7\2\2\u031e\u032d\5\u0080A\17\u031f\u032d\5\u008e"+
		"H\2\u0320\u0321\7\31\2\2\u0321\u0322\5&\24\2\u0322\u0323\7\33\2\2\u0323"+
		"\u0324\5\u0080A\4\u0324\u032d\3\2\2\2\u0325\u0326\t\b\2\2\u0326\u0327"+
		"\5\36\20\2\u0327\u0328\7f\2\2\u0328\u0329\7f\2\2\u0329\u032a\5\"\22\2"+
		"\u032a\u032b\5\u0080A\3\u032b\u032d\3\2\2\2\u032c\u031c\3\2\2\2\u032c"+
		"\u031f\3\2\2\2\u032c\u0320\3\2\2\2\u032c\u0325\3\2\2\2\u032d\u034e\3\2"+
		"\2\2\u032e\u032f\f\r\2\2\u032f\u0330\t\t\2\2\u0330\u034d\5\u0080A\16\u0331"+
		"\u0332\f\f\2\2\u0332\u0333\t\n\2\2\u0333\u034d\5\u0080A\r\u0334\u0335"+
		"\f\13\2\2\u0335\u0336\t\13\2\2\u0336\u034d\5\u0080A\f\u0337\u0338\f\n"+
		"\2\2\u0338\u0339\t\f\2\2\u0339\u034d\5\u0080A\13\u033a\u033b\f\t\2\2\u033b"+
		"\u033c\t\r\2\2\u033c\u034d\5\u0080A\n\u033d\u033e\f\b\2\2\u033e\u033f"+
		"\7m\2\2\u033f\u034d\5\u0080A\t\u0340\u0341\f\7\2\2\u0341\u0342\7l\2\2"+
		"\u0342\u034d\5\u0080A\b\u0343\u0344\f\6\2\2\u0344\u0345\7!\2\2\u0345\u034d"+
		"\5\u0080A\6\u0346\u0347\f\5\2\2\u0347\u0348\7$\2\2\u0348\u0349\5\u0080"+
		"A\2\u0349\u034a\7f\2\2\u034a\u034b\5\u0080A\5\u034b\u034d\3\2\2\2\u034c"+
		"\u032e\3\2\2\2\u034c\u0331\3\2\2\2\u034c\u0334\3\2\2\2\u034c\u0337\3\2"+
		"\2\2\u034c\u033a\3\2\2\2\u034c\u033d\3\2\2\2\u034c\u0340\3\2\2\2\u034c"+
		"\u0343\3\2\2\2\u034c\u0346\3\2\2\2\u034d\u0350\3\2\2\2\u034e\u034c\3\2"+
		"\2\2\u034e\u034f\3\2\2\2\u034f\u0081\3\2\2\2\u0350\u034e\3\2\2\2\u0351"+
		"\u0365\5\20\t\2\u0352\u0365\5\22\n\2\u0353\u0365\5\u0086D\2\u0354\u0365"+
		"\5\u0084C\2\u0355\u0365\5\u00b6\\\2\u0356\u0365\5\u00d6l\2\u0357\u0365"+
		"\5\u00caf\2\u0358\u0365\5\u0106\u0084\2\u0359\u0365\5\u00d8m\2\u035a\u0365"+
		"\5\u00dan\2\u035b\u0365\5\u00dco\2\u035c\u0365\5\u00dep\2\u035d\u0365"+
		"\5\u00e0q\2\u035e\u0365\5\u00c6d\2\u035f\u0365\5\u00e4s\2\u0360\u0365"+
		"\5\u00e6t\2\u0361\u0365\5\u00f8}\2\u0362\u0365\5\u0088E\2\u0363\u0365"+
		"\5\u00e2r\2\u0364\u0351\3\2\2\2\u0364\u0352\3\2\2\2\u0364\u0353\3\2\2"+
		"\2\u0364\u0354\3\2\2\2\u0364\u0355\3\2\2\2\u0364\u0356\3\2\2\2\u0364\u0357"+
		"\3\2\2\2\u0364\u0358\3\2\2\2\u0364\u0359\3\2\2\2\u0364\u035a\3\2\2\2\u0364"+
		"\u035b\3\2\2\2\u0364\u035c\3\2\2\2\u0364\u035d\3\2\2\2\u0364\u035e\3\2"+
		"\2\2\u0364\u035f\3\2\2\2\u0364\u0360\3\2\2\2\u0364\u0361\3\2\2\2\u0364"+
		"\u0362\3\2\2\2\u0364\u0363\3\2\2\2\u0365\u0083\3\2\2\2\u0366\u0367\7#"+
		"\2\2\u0367\u0368\5\u0080A\2\u0368\u0085\3\2\2\2\u0369\u036a\7O\2\2\u036a"+
		"\u036c\5\u0080A\2\u036b\u036d\5\u00c6d\2\u036c\u036b\3\2\2\2\u036c\u036d"+
		"\3\2\2\2\u036d\u0087\3\2\2\2\u036e\u036f\5\u008aF\2\u036f\u0370\5\u0100"+
		"\u0081\2\u0370\u0089\3\2\2\2\u0371\u0372\7\16\2\2\u0372\u0373\5\u0080"+
		"A\2\u0373\u0374\5\u014e\u00a8\2\u0374\u0376\3\2\2\2\u0375\u0371\3\2\2"+
		"\2\u0376\u0379\3\2\2\2\u0377\u0375\3\2\2\2\u0377\u0378\3\2\2\2\u0378\u037e"+
		"\3\2\2\2\u0379\u0377\3\2\2\2\u037a\u037b\7\17\2\2\u037b\u037c\5P)\2\u037c"+
		"\u037d\5\u014e\u00a8\2\u037d\u037f\3\2\2\2\u037e\u037a\3\2\2\2\u037e\u037f"+
		"\3\2\2\2\u037f\u008b\3\2\2\2\u0380\u0389\7\5\2\2\u0381\u0389\7\6\2\2\u0382"+
		"\u0389\7[\2\2\u0383\u0389\5\u0128\u0095\2\u0384\u0389\5\u013e\u00a0\2"+
		"\u0385\u0389\7\3\2\2\u0386\u0389\7\u0086\2\2\u0387\u0389\7\u0087\2\2\u0388"+
		"\u0380\3\2\2\2\u0388\u0381\3\2\2\2\u0388\u0382\3\2\2\2\u0388\u0383\3\2"+
		"\2\2\u0388\u0384\3\2\2\2\u0388\u0385\3\2\2\2\u0388\u0386\3\2\2\2\u0388"+
		"\u0387\3\2\2\2\u0389\u008d\3\2\2\2\u038a\u038b\bH\1\2\u038b\u0397\5\u0124"+
		"\u0093\2\u038c\u0397\5\u0120\u0091\2\u038d\u0397\5\u014a\u00a6\2\u038e"+
		"\u0397\5\30\r\2\u038f\u0397\5b\62\2\u0390\u0397\5`\61\2\u0391\u0392\t"+
		"\16\2\2\u0392\u0393\7]\2\2\u0393\u0394\5\u0080A\2\u0394\u0395\7^\2\2\u0395"+
		"\u0397\3\2\2\2\u0396\u038a\3\2\2\2\u0396\u038c\3\2\2\2\u0396\u038d\3\2"+
		"\2\2\u0396\u038e\3\2\2\2\u0396\u038f\3\2\2\2\u0396\u0390\3\2\2\2\u0396"+
		"\u0391\3\2\2\2\u0397\u03a9\3\2\2\2\u0398\u0399\f\n\2\2\u0399\u039a\7g"+
		"\2\2\u039a\u03a8\7\\\2\2\u039b\u039c\f\t\2\2\u039c\u03a8\5\u0144\u00a3"+
		"\2\u039d\u039e\f\b\2\2\u039e\u03a8\5\u00a4S\2\u039f\u03a0\f\7\2\2\u03a0"+
		"\u03a8\5> \2\u03a1\u03a2\f\6\2\2\u03a2\u03a8\5\u0146\u00a4\2\u03a3\u03a4"+
		"\f\5\2\2\u03a4\u03a8\5\u0148\u00a5\2\u03a5\u03a6\f\4\2\2\u03a6\u03a8\5"+
		"\u0090I\2\u03a7\u0398\3\2\2\2\u03a7\u039b\3\2\2\2\u03a7\u039d\3\2\2\2"+
		"\u03a7\u039f\3\2\2\2\u03a7\u03a1\3\2\2\2\u03a7\u03a3\3\2\2\2\u03a7\u03a5"+
		"\3\2\2\2\u03a8\u03ab\3\2\2\2\u03a9\u03a7\3\2\2\2\u03a9\u03aa\3\2\2\2\u03aa"+
		"\u008f\3\2\2\2\u03ab\u03a9\3\2\2\2\u03ac\u03ae\7%\2\2\u03ad\u03af\5\u00be"+
		"`\2\u03ae\u03ad\3\2\2\2\u03ae\u03af\3\2\2\2\u03af\u03b1\3\2\2\2\u03b0"+
		"\u03b2\7d\2\2\u03b1\u03b0\3\2\2\2\u03b1\u03b2\3\2\2\2\u03b2\u03b3\3\2"+
		"\2\2\u03b3\u03b4\7&\2\2\u03b4\u0091\3\2\2\2\u03b5\u03b6\7E\2\2\u03b6\u03c0"+
		"\7_\2\2\u03b7\u03bb\5\u0096L\2\u03b8\u03bb\5\u0108\u0085\2\u03b9\u03bb"+
		"\5\u0094K\2\u03ba\u03b7\3\2\2\2\u03ba\u03b8\3\2\2\2\u03ba\u03b9\3\2\2"+
		"\2\u03bb\u03bc\3\2\2\2\u03bc\u03bd\5\u014e\u00a8\2\u03bd\u03bf\3\2\2\2"+
		"\u03be\u03ba\3\2\2\2\u03bf\u03c2\3\2\2\2\u03c0\u03be\3\2\2\2\u03c0\u03c1"+
		"\3\2\2\2\u03c1\u03c3\3\2\2\2\u03c2\u03c0\3\2\2\2\u03c3\u03c4\7`\2\2\u03c4"+
		"\u0093\3\2\2\2\u03c5\u03c6\7\65\2\2\u03c6\u03c7\7\\\2\2\u03c7\u03c8\5"+
		"\u011e\u0090\2\u03c8\u0095\3\2\2\2\u03c9\u03cb\7\32\2\2\u03ca\u03c9\3"+
		"\2\2\2\u03ca\u03cb\3\2\2\2\u03cb\u03cc\3\2\2\2\u03cc\u03cd\5L\'\2\u03cd"+
		"\u03ce\7\\\2\2\u03ce\u03cf\5\u011e\u0090\2\u03cf\u03d0\5\u011c\u008f\2"+
		"\u03d0\u03d9\3\2\2\2\u03d1\u03d3\7\32\2\2\u03d2\u03d1\3\2\2\2\u03d2\u03d3"+
		"\3\2\2\2\u03d3\u03d4\3\2\2\2\u03d4\u03d5\5L\'\2\u03d5\u03d6\7\\\2\2\u03d6"+
		"\u03d7\5\u011e\u0090\2\u03d7\u03d9\3\2\2\2\u03d8\u03ca\3\2\2\2\u03d8\u03d2"+
		"\3\2\2\2\u03d9\u0097\3\2\2\2\u03da\u03e2\5\u0108\u0085\2\u03db\u03e2\5"+
		"\u009aN\2\u03dc\u03e2\5B\"\2\u03dd\u03de\7]\2\2\u03de\u03df\5\u0098M\2"+
		"\u03df\u03e0\7^\2\2\u03e0\u03e2\3\2\2\2\u03e1\u03da\3\2\2\2\u03e1\u03db"+
		"\3\2\2\2\u03e1\u03dc\3\2\2\2\u03e1\u03dd\3\2\2\2\u03e2\u0099\3\2\2\2\u03e3"+
		"\u03ed\5\u010a\u0086\2\u03e4\u03ed\5\u013a\u009e\2\u03e5\u03ed\5\u0110"+
		"\u0089\2\u03e6\u03ed\5\u0118\u008d\2\u03e7\u03ed\5\u0092J\2\u03e8\u03ed"+
		"\5\u0112\u008a\2\u03e9\u03ed\5\u0114\u008b\2\u03ea\u03ed\5\u0116\u008c"+
		"\2\u03eb\u03ed\5\u009cO\2\u03ec\u03e3\3\2\2\2\u03ec\u03e4\3\2\2\2\u03ec"+
		"\u03e5\3\2\2\2\u03ec\u03e6\3\2\2\2\u03ec\u03e7\3\2\2\2\u03ec\u03e8\3\2"+
		"\2\2\u03ec\u03e9\3\2\2\2\u03ec\u03ea\3\2\2\2\u03ec\u03eb\3\2\2\2\u03ed"+
		"\u009b\3\2\2\2\u03ee\u03ef\7\65\2\2\u03ef\u03f0\5\u009eP\2\u03f0\u009d"+
		"\3\2\2\2\u03f1\u03fd\7]\2\2\u03f2\u03f7\5\u0098M\2\u03f3\u03f4\7d\2\2"+
		"\u03f4\u03f6\5\u0098M\2\u03f5\u03f3\3\2\2\2\u03f6\u03f9\3\2\2\2\u03f7"+
		"\u03f5\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03fb\3\2\2\2\u03f9\u03f7\3\2"+
		"\2\2\u03fa\u03fc\7d\2\2\u03fb\u03fa\3\2\2\2\u03fb\u03fc\3\2\2\2\u03fc"+
		"\u03fe\3\2\2\2\u03fd\u03f2\3\2\2\2\u03fd\u03fe\3\2\2\2\u03fe\u03ff\3\2"+
		"\2\2\u03ff\u0400\7^\2\2\u0400\u009f\3\2\2\2\u0401\u0409\5\u013a\u009e"+
		"\2\u0402\u0409\5\u010a\u0086\2\u0403\u0409\5\u00a2R\2\u0404\u0409\5\u0112"+
		"\u008a\2\u0405\u0409\5\u0114\u008b\2\u0406\u0409\5B\"\2\u0407\u0409\5"+
		"\u0108\u0085\2\u0408\u0401\3\2\2\2\u0408\u0402\3\2\2\2\u0408\u0403\3\2"+
		"\2\2\u0408\u0404\3\2\2\2\u0408\u0405\3\2\2\2\u0408\u0406\3\2\2\2\u0408"+
		"\u0407\3\2\2\2\u0409\u00a1\3\2\2\2\u040a\u040b\7a\2\2\u040b\u040c\7k\2"+
		"\2\u040c\u040d\7b\2\2\u040d\u040e\5\u010e\u0088\2\u040e\u00a3\3\2\2\2"+
		"\u040f\u041f\7a\2\2\u0410\u0412\5\u00a6T\2\u0411\u0410\3\2\2\2\u0411\u0412"+
		"\3\2\2\2\u0412\u0413\3\2\2\2\u0413\u0415\7f\2\2\u0414\u0416\5\u00a8U\2"+
		"\u0415\u0414\3\2\2\2\u0415\u0416\3\2\2\2\u0416\u0420\3\2\2\2\u0417\u0419"+
		"\5\u00a6T\2\u0418\u0417\3\2\2\2\u0418\u0419\3\2\2\2\u0419\u041a\3\2\2"+
		"\2\u041a\u041b\7f\2\2\u041b\u041c\5\u00a8U\2\u041c\u041d\7f\2\2\u041d"+
		"\u041e\5\u00aaV\2\u041e\u0420\3\2\2\2\u041f\u0411\3\2\2\2\u041f\u0418"+
		"\3\2\2\2\u0420\u0421\3\2\2\2\u0421\u0422\7b\2\2\u0422\u00a5\3\2\2\2\u0423"+
		"\u0424\5\u0080A\2\u0424\u00a7\3\2\2\2\u0425\u0426\5\u0080A\2\u0426\u00a9"+
		"\3\2\2\2\u0427\u0428\5\u0080A\2\u0428\u00ab\3\2\2\2\u0429\u042b\t\17\2"+
		"\2\u042a\u0429\3\2\2\2\u042a\u042b\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u042d"+
		"\7c\2\2\u042d\u00ad\3\2\2\2\u042e\u042f\7O\2\2\u042f\u0430\7\\\2\2\u0430"+
		"\u00af\3\2\2\2\u0431\u043d\7X\2\2\u0432\u043e\5\u00b2Z\2\u0433\u0439\7"+
		"]\2\2\u0434\u0435\5\u00b2Z\2\u0435\u0436\5\u014e\u00a8\2\u0436\u0438\3"+
		"\2\2\2\u0437\u0434\3\2\2\2\u0438\u043b\3\2\2\2\u0439\u0437\3\2\2\2\u0439"+
		"\u043a\3\2\2\2\u043a\u043c\3\2\2\2\u043b\u0439\3\2\2\2\u043c\u043e\7^"+
		"\2\2\u043d\u0432\3\2\2\2\u043d\u0433\3\2\2\2\u043e\u00b1\3\2\2\2\u043f"+
		"\u0441\t\20\2\2\u0440\u043f\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u0442\3"+
		"\2\2\2\u0442\u0443\5\u00b4[\2\u0443\u00b3\3\2\2\2\u0444\u0445\5\u013e"+
		"\u00a0\2\u0445\u00b5\3\2\2\2\u0446\u044a\5\u00b8]\2\u0447\u044a\5\u00c0"+
		"a\2\u0448\u044a\5\u00c4c\2\u0449\u0446\3\2\2\2\u0449\u0447\3\2\2\2\u0449"+
		"\u0448\3\2\2\2\u044a\u00b7\3\2\2\2\u044b\u0457\7Q\2\2\u044c\u0458\5\u00ba"+
		"^\2\u044d\u0453\7]\2\2\u044e\u044f\5\u00ba^\2\u044f\u0450\5\u014e\u00a8"+
		"\2\u0450\u0452\3\2\2\2\u0451\u044e\3\2\2\2\u0452\u0455\3\2\2\2\u0453\u0451"+
		"\3\2\2\2\u0453\u0454\3\2\2\2\u0454\u0456\3\2\2\2\u0455\u0453\3\2\2\2\u0456"+
		"\u0458\7^\2\2\u0457\u044c\3\2\2\2\u0457\u044d\3\2\2\2\u0458\u00b9\3\2"+
		"\2\2\u0459\u045f\5\u00bc_\2\u045a\u045c\5\u0098M\2\u045b\u045a\3\2\2\2"+
		"\u045b\u045c\3\2\2\2\u045c\u045d\3\2\2\2\u045d\u045e\7c\2\2\u045e\u0460"+
		"\5\u00be`\2\u045f\u045b\3\2\2\2\u045f\u0460\3\2\2\2\u0460\u00bb\3\2\2"+
		"\2\u0461\u0466\7\\\2\2\u0462\u0463\7d\2\2\u0463\u0465\7\\\2\2\u0464\u0462"+
		"\3\2\2\2\u0465\u0468\3\2\2\2\u0466\u0464\3\2\2\2\u0466\u0467\3\2\2\2\u0467"+
		"\u00bd\3\2\2\2\u0468\u0466\3\2\2\2\u0469\u046e\5\u0080A\2\u046a\u046b"+
		"\7d\2\2\u046b\u046d\5\u0080A\2\u046c\u046a\3\2\2\2\u046d\u0470\3\2\2\2"+
		"\u046e\u046c\3\2\2\2\u046e\u046f\3\2\2\2\u046f\u00bf\3\2\2\2\u0470\u046e"+
		"\3\2\2\2\u0471\u047d\7U\2\2\u0472\u047e\5\u00c2b\2\u0473\u0479\7]\2\2"+
		"\u0474\u0475\5\u00c2b\2\u0475\u0476\5\u014e\u00a8\2\u0476\u0478\3\2\2"+
		"\2\u0477\u0474\3\2\2\2\u0478\u047b\3\2\2\2\u0479\u0477\3\2\2\2\u0479\u047a"+
		"\3\2\2\2\u047a\u047c\3\2\2\2\u047b\u0479\3\2\2\2\u047c\u047e\7^\2\2\u047d"+
		"\u0472\3\2\2\2\u047d\u0473\3\2\2\2\u047e\u00c1\3\2\2\2\u047f\u0481\7\\"+
		"\2\2\u0480\u0482\7c\2\2\u0481\u0480\3\2\2\2\u0481\u0482\3\2\2\2\u0482"+
		"\u0483\3\2\2\2\u0483\u0484\5\u0098M\2\u0484\u00c3\3\2\2\2\u0485\u0491"+
		"\7Z\2\2\u0486\u0492\5r:\2\u0487\u048d\7]\2\2\u0488\u0489\5r:\2\u0489\u048a"+
		"\5\u014e\u00a8\2\u048a\u048c\3\2\2\2\u048b\u0488\3\2\2\2\u048c\u048f\3"+
		"\2\2\2\u048d\u048b\3\2\2\2\u048d\u048e\3\2\2\2\u048e\u0490\3\2\2\2\u048f"+
		"\u048d\3\2\2\2\u0490\u0492\7^\2\2\u0491\u0486\3\2\2\2\u0491\u0487\3\2"+
		"\2\2\u0492\u00c5\3\2\2\2\u0493\u0495\7_\2\2\u0494\u0496\5\u00c8e\2\u0495"+
		"\u0494\3\2\2\2\u0495\u0496\3\2\2\2\u0496\u0497\3\2\2\2\u0497\u0498\7`"+
		"\2\2\u0498\u00c7\3\2\2\2\u0499\u049b\5\u014e\u00a8\2\u049a\u0499\3\2\2"+
		"\2\u049a\u049b\3\2\2\2\u049b\u049c\3\2\2\2\u049c\u049d\5\u0082B\2\u049d"+
		"\u049e\5\u014e\u00a8\2\u049e\u04a0\3\2\2\2\u049f\u049a\3\2\2\2\u04a0\u04a1"+
		"\3\2\2\2\u04a1\u049f\3\2\2\2\u04a1\u04a2\3\2\2\2\u04a2\u00c9\3\2\2\2\u04a3"+
		"\u04a9\5\u00ceh\2\u04a4\u04a9\5\u00d0i\2\u04a5\u04a9\5\u00d2j\2\u04a6"+
		"\u04a9\5\u00ccg\2\u04a7\u04a9\5t;\2\u04a8\u04a3\3\2\2\2\u04a8\u04a4\3"+
		"\2\2\2\u04a8\u04a5\3\2\2\2\u04a8\u04a6\3\2\2\2\u04a8\u04a7\3\2\2\2\u04a9"+
		"\u00cb\3\2\2\2\u04aa\u04ab\5\u0080A\2\u04ab\u00cd\3\2\2\2\u04ac\u04ad"+
		"\5\u0080A\2\u04ad\u04ae\7\u0080\2\2\u04ae\u04af\5\u0080A\2\u04af\u00cf"+
		"\3\2\2\2\u04b0\u04b1\5\u0080A\2\u04b1\u04b2\t\21\2\2\u04b2\u00d1\3\2\2"+
		"\2\u04b3\u04b4\5\u00be`\2\u04b4\u04b5\5\u00acW\2\u04b5\u04b6\5\u00be`"+
		"\2\u04b6\u00d3\3\2\2\2\u04b7\u04b8\t\22\2\2\u04b8\u00d5\3\2\2\2\u04b9"+
		"\u04ba\7\\\2\2\u04ba\u04bc\7f\2\2\u04bb\u04bd\5\u0082B\2\u04bc\u04bb\3"+
		"\2\2\2\u04bc\u04bd\3\2\2\2\u04bd\u00d7\3\2\2\2\u04be\u04c0\7Y\2\2\u04bf"+
		"\u04c1\5\u00be`\2\u04c0\u04bf\3\2\2\2\u04c0\u04c1\3\2\2\2\u04c1\u00d9"+
		"\3\2\2\2\u04c2\u04c4\7B\2\2\u04c3\u04c5\7\\\2\2\u04c4\u04c3\3\2\2\2\u04c4"+
		"\u04c5\3\2\2\2\u04c5\u00db\3\2\2\2\u04c6\u04c8\7V\2\2\u04c7\u04c9\7\\"+
		"\2\2\u04c8\u04c7\3\2\2\2\u04c8\u04c9\3\2\2\2\u04c9\u00dd\3\2\2\2\u04ca"+
		"\u04cb\7N\2\2\u04cb\u04cc\7\\\2\2\u04cc\u00df\3\2\2\2\u04cd\u04ce\7R\2"+
		"\2\u04ce\u00e1\3\2\2\2\u04cf\u04d0\7H\2\2\u04d0\u04d1\5\u0080A\2\u04d1"+
		"\u00e3\3\2\2\2\u04d2\u04db\7S\2\2\u04d3\u04dc\5\u0080A\2\u04d4\u04d5\5"+
		"\u014e\u00a8\2\u04d5\u04d6\5\u0080A\2\u04d6\u04dc\3\2\2\2\u04d7\u04d8"+
		"\5\u00caf\2\u04d8\u04d9\5\u014e\u00a8\2\u04d9\u04da\5\u0080A\2\u04da\u04dc"+
		"\3\2\2\2\u04db\u04d3\3\2\2\2\u04db\u04d4\3\2\2\2\u04db\u04d7\3\2\2\2\u04dc"+
		"\u04dd\3\2\2\2\u04dd\u04e3\5\u00c6d\2\u04de\u04e1\7M\2\2\u04df\u04e2\5"+
		"\u00e4s\2\u04e0\u04e2\5\u00c6d\2\u04e1\u04df\3\2\2\2\u04e1\u04e0\3\2\2"+
		"\2\u04e2\u04e4\3\2\2\2\u04e3\u04de\3\2\2\2\u04e3\u04e4\3\2\2\2\u04e4\u00e5"+
		"\3\2\2\2\u04e5\u04e8\5\u00e8u\2\u04e6\u04e8\5\u00eex\2\u04e7\u04e5\3\2"+
		"\2\2\u04e7\u04e6\3\2\2\2\u04e8\u00e7\3\2\2\2\u04e9\u04f4\7P\2\2\u04ea"+
		"\u04ec\5\u0080A\2\u04eb\u04ea\3\2\2\2\u04eb\u04ec\3\2\2\2\u04ec\u04f5"+
		"\3\2\2\2\u04ed\u04ef\5\u00caf\2\u04ee\u04ed\3\2\2\2\u04ee\u04ef\3\2\2"+
		"\2\u04ef\u04f0\3\2\2\2\u04f0\u04f2\5\u014e\u00a8\2\u04f1\u04f3\5\u0080"+
		"A\2\u04f2\u04f1\3\2\2\2\u04f2\u04f3\3\2\2\2\u04f3\u04f5\3\2\2\2\u04f4"+
		"\u04eb\3\2\2\2\u04f4\u04ee\3\2\2\2\u04f5\u04f6\3\2\2\2\u04f6\u04fa\7_"+
		"\2\2\u04f7\u04f9\5\u00eav\2\u04f8\u04f7\3\2\2\2\u04f9\u04fc\3\2\2\2\u04fa"+
		"\u04f8\3\2\2\2\u04fa\u04fb\3\2\2\2\u04fb\u04fd\3\2\2\2\u04fc\u04fa\3\2"+
		"\2\2\u04fd\u04fe\7`\2\2\u04fe\u00e9\3\2\2\2\u04ff\u0500\5\u00ecw\2\u0500"+
		"\u0502\7f\2\2\u0501\u0503\5\u00c8e\2\u0502\u0501\3\2\2\2\u0502\u0503\3"+
		"\2\2\2\u0503\u00eb\3\2\2\2\u0504\u0505\7G\2\2\u0505\u0508\5\u00be`\2\u0506"+
		"\u0508\7C\2\2\u0507\u0504\3\2\2\2\u0507\u0506\3\2\2\2\u0508\u00ed\3\2"+
		"\2\2\u0509\u0512\7P\2\2\u050a\u0513\5\u00f0y\2\u050b\u050c\5\u014e\u00a8"+
		"\2\u050c\u050d\5\u00f0y\2\u050d\u0513\3\2\2\2\u050e\u050f\5\u00caf\2\u050f"+
		"\u0510\5\u014e\u00a8\2\u0510\u0511\5\u00f0y\2\u0511\u0513\3\2\2\2\u0512"+
		"\u050a\3\2\2\2\u0512\u050b\3\2\2\2\u0512\u050e\3\2\2\2\u0513\u0514\3\2"+
		"\2\2\u0514\u0518\7_\2\2\u0515\u0517\5\u00f2z\2\u0516\u0515\3\2\2\2\u0517"+
		"\u051a\3\2\2\2\u0518\u0516\3\2\2\2\u0518\u0519\3\2\2\2\u0519\u051b\3\2"+
		"\2\2\u051a\u0518\3\2\2\2\u051b\u051c\7`\2\2\u051c\u00ef\3\2\2\2\u051d"+
		"\u051e\7\\\2\2\u051e\u0520\7j\2\2\u051f\u051d\3\2\2\2\u051f\u0520\3\2"+
		"\2\2\u0520\u0521\3\2\2\2\u0521\u0522\5\u008eH\2\u0522\u0523\7g\2\2\u0523"+
		"\u0524\7]\2\2\u0524\u0525\7U\2\2\u0525\u0526\7^\2\2\u0526\u00f1\3\2\2"+
		"\2\u0527\u0528\5\u00f4{\2\u0528\u052a\7f\2\2\u0529\u052b\5\u00c8e\2\u052a"+
		"\u0529\3\2\2\2\u052a\u052b\3\2\2\2\u052b\u00f3\3\2\2\2\u052c\u052d\7G"+
		"\2\2\u052d\u0530\5\u00f6|\2\u052e\u0530\7C\2\2\u052f\u052c\3\2\2\2\u052f"+
		"\u052e\3\2\2\2\u0530\u00f5\3\2\2\2\u0531\u0534\5\u0098M\2\u0532\u0534"+
		"\7[\2\2\u0533\u0531\3\2\2\2\u0533\u0532\3\2\2\2\u0534\u053c\3\2\2\2\u0535"+
		"\u0538\7d\2\2\u0536\u0539\5\u0098M\2\u0537\u0539\7[\2\2\u0538\u0536\3"+
		"\2\2\2\u0538\u0537\3\2\2\2\u0539\u053b\3\2\2\2\u053a\u0535\3\2\2\2\u053b"+
		"\u053e\3\2\2\2\u053c\u053a\3\2\2\2\u053c\u053d\3\2\2\2\u053d\u00f7\3\2"+
		"\2\2\u053e\u053c\3\2\2\2\u053f\u0540\7F\2\2\u0540\u0544\7_\2\2\u0541\u0543"+
		"\5\u00fa~\2\u0542\u0541\3\2\2\2\u0543\u0546\3\2\2\2\u0544\u0542\3\2\2"+
		"\2\u0544\u0545\3\2\2\2\u0545\u0547\3\2\2\2\u0546\u0544\3\2\2\2\u0547\u0548"+
		"\7`\2\2\u0548\u00f9\3\2\2\2\u0549\u054a\5\u00fc\177\2\u054a\u054c\7f\2"+
		"\2\u054b\u054d\5\u00c8e\2\u054c\u054b\3\2\2\2\u054c\u054d\3\2\2\2\u054d"+
		"\u00fb\3\2\2\2\u054e\u0551\7G\2\2\u054f\u0552\5\u00ceh\2\u0550\u0552\5"+
		"\u00fe\u0080\2\u0551\u054f\3\2\2\2\u0551\u0550\3\2\2\2\u0552\u0555\3\2"+
		"\2\2\u0553\u0555\7C\2\2\u0554\u054e\3\2\2\2\u0554\u0553\3\2\2\2\u0555"+
		"\u00fd\3\2\2\2\u0556\u0557\5\u00be`\2\u0557\u0558\7c\2\2\u0558\u055d\3"+
		"\2\2\2\u0559\u055a\5\u00bc_\2\u055a\u055b\7j\2\2\u055b\u055d\3\2\2\2\u055c"+
		"\u0556\3\2\2\2\u055c\u0559\3\2\2\2\u055c\u055d\3\2\2\2\u055d\u055e\3\2"+
		"\2\2\u055e\u055f\5\u0080A\2\u055f\u00ff\3\2\2\2\u0560\u0564\7W\2\2\u0561"+
		"\u0565\5\u0080A\2\u0562\u0565\5\u0102\u0082\2\u0563\u0565\5\u0104\u0083"+
		"\2\u0564\u0561\3\2\2\2\u0564\u0562\3\2\2\2\u0564\u0563\3\2\2\2\u0564\u0565"+
		"\3\2\2\2\u0565\u0566\3\2\2\2\u0566\u0567\5\u00c6d\2\u0567\u0101\3\2\2"+
		"\2\u0568\u056a\5\u00caf\2\u0569\u0568\3\2\2\2\u0569\u056a\3\2\2\2\u056a"+
		"\u056b\3\2\2\2\u056b\u056d\5\u014e\u00a8\2\u056c\u056e\5\u0080A\2\u056d"+
		"\u056c\3\2\2\2\u056d\u056e\3\2\2\2\u056e\u056f\3\2\2\2\u056f\u0571\5\u014e"+
		"\u00a8\2\u0570\u0572\5\u00caf\2\u0571\u0570\3\2\2\2\u0571\u0572\3\2\2"+
		"\2\u0572\u0103\3\2\2\2\u0573\u0574\5\u00be`\2\u0574\u0575\7c\2\2\u0575"+
		"\u057a\3\2\2\2\u0576\u0577\5\u00bc_\2\u0577\u0578\7j\2\2\u0578\u057a\3"+
		"\2\2\2\u0579\u0573\3\2\2\2\u0579\u0576\3\2\2\2\u0579\u057a\3\2\2\2\u057a"+
		"\u057b\3\2\2\2\u057b\u057c\7T\2\2\u057c\u057d\5\u0080A\2\u057d\u0105\3"+
		"\2\2\2\u057e\u057f\7I\2\2\u057f\u0580\5\u0080A\2\u0580\u0107\3\2\2\2\u0581"+
		"\u0584\5\u012c\u0097\2\u0582\u0584\7\\\2\2\u0583\u0581\3\2\2\2\u0583\u0582"+
		"\3\2\2\2\u0584\u0109\3\2\2\2\u0585\u0586\7a\2\2\u0586\u0587\5\u010c\u0087"+
		"\2\u0587\u0588\7b\2\2\u0588\u0589\5\u010e\u0088\2\u0589\u010b\3\2\2\2"+
		"\u058a\u058b\5\u0080A\2\u058b\u010d\3\2\2\2\u058c\u058d\5\u0098M\2\u058d"+
		"\u010f\3\2\2\2\u058e\u058f\7~\2\2\u058f\u0590\5\u0098M\2\u0590\u0111\3"+
		"\2\2\2\u0591\u0592\7a\2\2\u0592\u0593\7b\2\2\u0593\u0594\5\u010e\u0088"+
		"\2\u0594\u0113\3\2\2\2\u0595\u0596\7J\2\2\u0596\u0597\7a\2\2\u0597\u0598"+
		"\5\u0098M\2\u0598\u0599\7b\2\2\u0599\u059a\5\u010e\u0088\2\u059a\u0115"+
		"\3\2\2\2\u059b\u05a1\7L\2\2\u059c\u059d\7L\2\2\u059d\u05a1\7\u0080\2\2"+
		"\u059e\u059f\7\u0080\2\2\u059f\u05a1\7L\2\2\u05a0\u059b\3\2\2\2\u05a0"+
		"\u059c\3\2\2\2\u05a0\u059e\3\2\2\2\u05a1\u05a2\3\2\2\2\u05a2\u05a3\5\u010e"+
		"\u0088\2\u05a3\u0117\3\2\2\2\u05a4\u05a5\7D\2\2\u05a5\u05a6\5\u011a\u008e"+
		"\2\u05a6\u0119\3\2\2\2\u05a7\u05a8\5\u011e\u0090\2\u05a8\u05a9\5\u011c"+
		"\u008f\2\u05a9\u05ac\3\2\2\2\u05aa\u05ac\5\u011e\u0090\2\u05ab\u05a7\3"+
		"\2\2\2\u05ab\u05aa\3\2\2\2\u05ac\u011b\3\2\2\2\u05ad\u05b0\5\u011e\u0090"+
		"\2\u05ae\u05b0\5\u0098M\2\u05af\u05ad\3\2\2\2\u05af\u05ae\3\2\2\2\u05b0"+
		"\u011d\3\2\2\2\u05b1\u05bd\7]\2\2\u05b2\u05b7\5x=\2\u05b3\u05b4\7d\2\2"+
		"\u05b4\u05b6\5x=\2\u05b5\u05b3\3\2\2\2\u05b6\u05b9\3\2\2\2\u05b7\u05b5"+
		"\3\2\2\2\u05b7\u05b8\3\2\2\2\u05b8\u05bb\3\2\2\2\u05b9\u05b7\3\2\2\2\u05ba"+
		"\u05bc\7d\2\2\u05bb\u05ba\3\2\2\2\u05bb\u05bc\3\2\2\2\u05bc\u05be\3\2"+
		"\2\2\u05bd\u05b2\3\2\2\2\u05bd\u05be\3\2\2\2\u05be\u05bf\3\2\2\2\u05bf"+
		"\u05c0\7^\2\2\u05c0\u011f\3\2\2\2\u05c1\u05c2\5\u0122\u0092\2\u05c2\u05c3"+
		"\7]\2\2\u05c3\u05c5\5\u0080A\2\u05c4\u05c6\7d\2\2\u05c5\u05c4\3\2\2\2"+
		"\u05c5\u05c6\3\2\2\2\u05c6\u05c7\3\2\2\2\u05c7\u05c8\7^\2\2\u05c8\u0121"+
		"\3\2\2\2\u05c9\u05cf\5\u009aN\2\u05ca\u05cb\7]\2\2\u05cb\u05cc\5\u0122"+
		"\u0092\2\u05cc\u05cd\7^\2\2\u05cd\u05cf\3\2\2\2\u05ce\u05c9\3\2\2\2\u05ce"+
		"\u05ca\3\2\2\2\u05cf\u0123\3\2\2\2\u05d0\u05d7\5\u0126\u0094\2\u05d1\u05d7"+
		"\5\u012a\u0096\2\u05d2\u05d3\7]\2\2\u05d3\u05d4\5\u0080A\2\u05d4\u05d5"+
		"\7^\2\2\u05d5\u05d7\3\2\2\2\u05d6\u05d0\3\2\2\2\u05d6\u05d1\3\2\2\2\u05d6"+
		"\u05d2\3\2\2\2\u05d7\u0125\3\2\2\2\u05d8\u05dc\5\u008cG\2\u05d9\u05dc"+
		"\5\u012e\u0098\2\u05da\u05dc\5\u0142\u00a2\2\u05db\u05d8\3\2\2\2\u05db"+
		"\u05d9\3\2\2\2\u05db\u05da\3\2\2\2\u05dc\u0127\3\2\2\2\u05dd\u05de\t\23"+
		"\2\2\u05de\u0129\3\2\2\2\u05df\u05e0\7\\\2\2\u05e0\u012b\3\2\2\2\u05e1"+
		"\u05e2\7\\\2\2\u05e2\u05e3\7g\2\2\u05e3\u05e4\7\\\2\2\u05e4\u012d\3\2"+
		"\2\2\u05e5\u05e6\5\u00a0Q\2\u05e6\u05e7\5\u0130\u0099\2\u05e7\u012f\3"+
		"\2\2\2\u05e8\u05ed\7_\2\2\u05e9\u05eb\5\u0132\u009a\2\u05ea\u05ec\7d\2"+
		"\2\u05eb\u05ea\3\2\2\2\u05eb\u05ec\3\2\2\2\u05ec\u05ee\3\2\2\2\u05ed\u05e9"+
		"\3\2\2\2\u05ed\u05ee\3\2\2\2\u05ee\u05ef\3\2\2\2\u05ef\u05f0\7`\2\2\u05f0"+
		"\u0131\3\2\2\2\u05f1\u05f6\5\u0134\u009b\2\u05f2\u05f3\7d\2\2\u05f3\u05f5"+
		"\5\u0134\u009b\2\u05f4\u05f2\3\2\2\2\u05f5\u05f8\3\2\2\2\u05f6\u05f4\3"+
		"\2\2\2\u05f6\u05f7\3\2\2\2\u05f7\u0133\3\2\2\2\u05f8\u05f6\3\2\2\2\u05f9"+
		"\u05fa\5\u0136\u009c\2\u05fa\u05fb\7f\2\2\u05fb\u05fd\3\2\2\2\u05fc\u05f9"+
		"\3\2\2\2\u05fc\u05fd\3\2\2\2\u05fd\u05fe\3\2\2\2\u05fe\u05ff\5\u0138\u009d"+
		"\2\u05ff\u0135\3\2\2\2\u0600\u0603\5\u0080A\2\u0601\u0603\5\u0130\u0099"+
		"\2\u0602\u0600\3\2\2\2\u0602\u0601\3\2\2\2\u0603\u0137\3\2\2\2\u0604\u0607"+
		"\5\u0080A\2\u0605\u0607\5\u0130\u0099\2\u0606\u0604\3\2\2\2\u0606\u0605"+
		"\3\2\2\2\u0607\u0139\3\2\2\2\u0608\u0609\7K\2\2\u0609\u060f\7_\2\2\u060a"+
		"\u060b\5\u013c\u009f\2\u060b\u060c\5\u014e\u00a8\2\u060c\u060e\3\2\2\2"+
		"\u060d\u060a\3\2\2\2\u060e\u0611\3\2\2\2\u060f\u060d\3\2\2\2\u060f\u0610"+
		"\3\2\2\2\u0610\u0612\3\2\2\2\u0611\u060f\3\2\2\2\u0612\u0613\7`\2\2\u0613"+
		"\u013b\3\2\2\2\u0614\u0615\5\u00bc_\2\u0615\u0616\5\u0098M\2\u0616\u0619"+
		"\3\2\2\2\u0617\u0619\5\u0140\u00a1\2\u0618\u0614\3\2\2\2\u0618\u0617\3"+
		"\2\2\2\u0619\u061b\3\2\2\2\u061a\u061c\5\u013e\u00a0\2\u061b\u061a\3\2"+
		"\2\2\u061b\u061c\3\2\2\2\u061c\u013d\3\2\2\2\u061d\u061e\t\24\2\2\u061e"+
		"\u013f\3\2\2\2\u061f\u0621\7~\2\2\u0620\u061f\3\2\2\2\u0620\u0621\3\2"+
		"\2\2\u0621\u0622\3\2\2\2\u0622\u0623\5\u0108\u0085\2\u0623\u0141\3\2\2"+
		"\2\u0624\u0625\7D\2\2\u0625\u0626\5\u011a\u008e\2\u0626\u0627\5\u00c6"+
		"d\2\u0627\u0143\3\2\2\2\u0628\u0629\7a\2\2\u0629\u062a\5\u0080A\2\u062a"+
		"\u062b\7b\2\2\u062b\u0145\3\2\2\2\u062c\u062d\7g\2\2\u062d\u062e\7]\2"+
		"\2\u062e\u062f\5\u0098M\2\u062f\u0630\7^\2\2\u0630\u0147\3\2\2\2\u0631"+
		"\u0640\7]\2\2\u0632\u0639\5\u00be`\2\u0633\u0636\5\u0122\u0092\2\u0634"+
		"\u0635\7d\2\2\u0635\u0637\5\u00be`\2\u0636\u0634\3\2\2\2\u0636\u0637\3"+
		"\2\2\2\u0637\u0639\3\2\2\2\u0638\u0632\3\2\2\2\u0638\u0633\3\2\2\2\u0639"+
		"\u063b\3\2\2\2\u063a\u063c\7k\2\2\u063b\u063a\3\2\2\2\u063b\u063c\3\2"+
		"\2\2\u063c\u063e\3\2\2\2\u063d\u063f\7d\2\2\u063e\u063d\3\2\2\2\u063e"+
		"\u063f\3\2\2\2\u063f\u0641\3\2\2\2\u0640\u0638\3\2\2\2\u0640\u0641\3\2"+
		"\2\2\u0641\u0642\3\2\2\2\u0642\u0643\7^\2\2\u0643\u0149\3\2\2\2\u0644"+
		"\u0645\5\u0122\u0092\2\u0645\u0646\7g\2\2\u0646\u0647\7\\\2\2\u0647\u014b"+
		"\3\2\2\2\u0648\u0649\5\u0098M\2\u0649\u014d\3\2\2\2\u064a\u064f\7e\2\2"+
		"\u064b\u064f\7\2\2\3\u064c\u064f\7\u0096\2\2\u064d\u064f\6\u00a8\22\2"+
		"\u064e\u064a\3\2\2\2\u064e\u064b\3\2\2\2\u064e\u064c\3\2\2\2\u064e\u064d"+
		"\3\2\2\2\u064f\u014f\3\2\2\2\u00a5\u015e\u0163\u016c\u0172\u0178\u0181"+
		"\u0189\u0193\u01a2\u01b0\u01b4\u01bb\u01c3\u01cc\u01ec\u01f4\u0207\u0218"+
		"\u0224\u022d\u023b\u024d\u0254\u0259\u025e\u0268\u026b\u026f\u0273\u027a"+
		"\u027d\u028a\u0292\u0296\u0299\u029f\u02a3\u02a6\u02b0\u02b7\u02be\u02ca"+
		"\u02d0\u02d7\u02dc\u02e2\u02ee\u02f4\u02f8\u0300\u0304\u030a\u030d\u0313"+
		"\u0318\u032c\u034c\u034e\u0364\u036c\u0377\u037e\u0388\u0396\u03a7\u03a9"+
		"\u03ae\u03b1\u03ba\u03c0\u03ca\u03d2\u03d8\u03e1\u03ec\u03f7\u03fb\u03fd"+
		"\u0408\u0411\u0415\u0418\u041f\u042a\u0439\u043d\u0440\u0449\u0453\u0457"+
		"\u045b\u045f\u0466\u046e\u0479\u047d\u0481\u048d\u0491\u0495\u049a\u04a1"+
		"\u04a8\u04bc\u04c0\u04c4\u04c8\u04db\u04e1\u04e3\u04e7\u04eb\u04ee\u04f2"+
		"\u04f4\u04fa\u0502\u0507\u0512\u0518\u051f\u052a\u052f\u0533\u0538\u053c"+
		"\u0544\u054c\u0551\u0554\u055c\u0564\u0569\u056d\u0571\u0579\u0583\u05a0"+
		"\u05ab\u05af\u05b7\u05bb\u05bd\u05c5\u05ce\u05d6\u05db\u05eb\u05ed\u05f6"+
		"\u05fc\u0602\u0606\u060f\u0618\u061b\u0620\u0636\u0638\u063b\u063e\u0640"+
		"\u064e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}