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
		IMPL=15, OLD=16, BEFORE=17, LHS=18, FORALL=19, EXISTS=20, ACCESS=21, FOLD=22, 
		UNFOLD=23, UNFOLDING=24, GHOST=25, IN=26, MULTI=27, SUBSET=28, UNION=29, 
		INTERSECTION=30, SETMINUS=31, IMPLIES=32, WAND=33, APPLY=34, QMARK=35, 
		L_PRED=36, R_PRED=37, SEQ=38, SET=39, MSET=40, DICT=41, OPT=42, LEN=43, 
		NEW=44, MAKE=45, CAP=46, SOME=47, GET=48, DOM=49, AXIOM=50, NONE=51, PRED=52, 
		TYPE_OF=53, IS_COMPARABLE=54, SHARE=55, ADDR_MOD=56, DOT_DOT=57, SHARED=58, 
		EXCLUSIVE=59, PREDICATE=60, WRITEPERM=61, NOPERM=62, TRUSTED=63, OUTLINE=64, 
		BREAK=65, DEFAULT=66, FUNC=67, INTERFACE=68, SELECT=69, CASE=70, DEFER=71, 
		GO=72, MAP=73, STRUCT=74, CHAN=75, ELSE=76, GOTO=77, PACKAGE=78, SWITCH=79, 
		CONST=80, FALLTHROUGH=81, IF=82, RANGE=83, TYPE=84, CONTINUE=85, FOR=86, 
		IMPORT=87, RETURN=88, VAR=89, NIL_LIT=90, IDENTIFIER=91, L_PAREN=92, R_PAREN=93, 
		L_CURLY=94, R_CURLY=95, L_BRACKET=96, R_BRACKET=97, ASSIGN=98, COMMA=99, 
		SEMI=100, COLON=101, DOT=102, PLUS_PLUS=103, MINUS_MINUS=104, DECLARE_ASSIGN=105, 
		ELLIPSIS=106, LOGICAL_OR=107, LOGICAL_AND=108, EQUALS=109, NOT_EQUALS=110, 
		LESS=111, LESS_OR_EQUALS=112, GREATER=113, GREATER_OR_EQUALS=114, OR=115, 
		DIV=116, MOD=117, LSHIFT=118, RSHIFT=119, BIT_CLEAR=120, EXCLAMATION=121, 
		PLUS=122, MINUS=123, CARET=124, STAR=125, AMPERSAND=126, RECEIVE=127, 
		DECIMAL_LIT=128, BINARY_LIT=129, OCTAL_LIT=130, HEX_LIT=131, HEX_FLOAT_LIT=132, 
		IMAGINARY_LIT=133, RUNE_LIT=134, BYTE_VALUE=135, OCTAL_BYTE_VALUE=136, 
		HEX_BYTE_VALUE=137, LITTLE_U_VALUE=138, BIG_U_VALUE=139, RAW_STRING_LIT=140, 
		INTERPRETED_STRING_LIT=141, WS=142, COMMENT=143, TERMINATOR=144, LINE_COMMENT=145, 
		WS_NLSEMI=146, COMMENT_NLSEMI=147, LINE_COMMENT_NLSEMI=148, EOS=149, OTHER=150;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_sourceFile = 5, RULE_importSpec = 6, 
		RULE_importDecl = 7, RULE_ghostMember = 8, RULE_ghostStatement = 9, RULE_auxiliaryStatement = 10, 
		RULE_statementWithSpec = 11, RULE_outlineStatement = 12, RULE_ghostPrimaryExpr = 13, 
		RULE_permission = 14, RULE_typeExpr = 15, RULE_boundVariables = 16, RULE_boundVariableDecl = 17, 
		RULE_triggers = 18, RULE_trigger = 19, RULE_predicateAccess = 20, RULE_optionSome = 21, 
		RULE_optionNone = 22, RULE_optionGet = 23, RULE_sConversion = 24, RULE_old = 25, 
		RULE_oldLabelUse = 26, RULE_labelUse = 27, RULE_before = 28, RULE_isComparable = 29, 
		RULE_typeOf = 30, RULE_access = 31, RULE_range = 32, RULE_seqUpdExp = 33, 
		RULE_seqUpdClause = 34, RULE_ghostTypeLit = 35, RULE_domainType = 36, 
		RULE_domainClause = 37, RULE_ghostSliceType = 38, RULE_sqType = 39, RULE_specification = 40, 
		RULE_specStatement = 41, RULE_terminationMeasure = 42, RULE_assertion = 43, 
		RULE_blockWithBodyParameterInfo = 44, RULE_implementationProof = 45, RULE_methodImplementationProof = 46, 
		RULE_nonLocalReceiver = 47, RULE_selection = 48, RULE_implementationProofPredicateAlias = 49, 
		RULE_make = 50, RULE_new_ = 51, RULE_specMember = 52, RULE_functionDecl = 53, 
		RULE_methodDecl = 54, RULE_explicitGhostMember = 55, RULE_fpredicateDecl = 56, 
		RULE_predicateBody = 57, RULE_mpredicateDecl = 58, RULE_varSpec = 59, 
		RULE_shortVarDecl = 60, RULE_receiver = 61, RULE_parameterDecl = 62, RULE_actualParameterDecl = 63, 
		RULE_ghostParameterDecl = 64, RULE_parameterType = 65, RULE_expression = 66, 
		RULE_statement = 67, RULE_applyStmt = 68, RULE_packageStmt = 69, RULE_specForStmt = 70, 
		RULE_loopSpec = 71, RULE_basicLit = 72, RULE_primaryExpr = 73, RULE_predConstructArgs = 74, 
		RULE_interfaceType = 75, RULE_predicateSpec = 76, RULE_methodSpec = 77, 
		RULE_type_ = 78, RULE_typeLit = 79, RULE_predType = 80, RULE_predTypeParams = 81, 
		RULE_literalType = 82, RULE_implicitArray = 83, RULE_slice_ = 84, RULE_low = 85, 
		RULE_high = 86, RULE_cap = 87, RULE_assign_op = 88, RULE_packageClause = 89, 
		RULE_importPath = 90, RULE_declaration = 91, RULE_constDecl = 92, RULE_constSpec = 93, 
		RULE_identifierList = 94, RULE_expressionList = 95, RULE_typeDecl = 96, 
		RULE_typeSpec = 97, RULE_varDecl = 98, RULE_block = 99, RULE_statementList = 100, 
		RULE_simpleStmt = 101, RULE_expressionStmt = 102, RULE_sendStmt = 103, 
		RULE_incDecStmt = 104, RULE_assignment = 105, RULE_emptyStmt = 106, RULE_labeledStmt = 107, 
		RULE_returnStmt = 108, RULE_breakStmt = 109, RULE_continueStmt = 110, 
		RULE_gotoStmt = 111, RULE_fallthroughStmt = 112, RULE_deferStmt = 113, 
		RULE_ifStmt = 114, RULE_switchStmt = 115, RULE_exprSwitchStmt = 116, RULE_exprCaseClause = 117, 
		RULE_exprSwitchCase = 118, RULE_typeSwitchStmt = 119, RULE_typeSwitchGuard = 120, 
		RULE_typeCaseClause = 121, RULE_typeSwitchCase = 122, RULE_typeList = 123, 
		RULE_selectStmt = 124, RULE_commClause = 125, RULE_commCase = 126, RULE_recvStmt = 127, 
		RULE_forStmt = 128, RULE_forClause = 129, RULE_rangeClause = 130, RULE_goStmt = 131, 
		RULE_typeName = 132, RULE_arrayType = 133, RULE_arrayLength = 134, RULE_elementType = 135, 
		RULE_pointerType = 136, RULE_sliceType = 137, RULE_mapType = 138, RULE_channelType = 139, 
		RULE_functionType = 140, RULE_signature = 141, RULE_result = 142, RULE_parameters = 143, 
		RULE_conversion = 144, RULE_nonNamedType = 145, RULE_operand = 146, RULE_literal = 147, 
		RULE_integer = 148, RULE_operandName = 149, RULE_qualifiedIdent = 150, 
		RULE_compositeLit = 151, RULE_literalValue = 152, RULE_elementList = 153, 
		RULE_keyedElement = 154, RULE_key = 155, RULE_element = 156, RULE_structType = 157, 
		RULE_fieldDecl = 158, RULE_string_ = 159, RULE_embeddedField = 160, RULE_functionLit = 161, 
		RULE_index = 162, RULE_typeAssertion = 163, RULE_arguments = 164, RULE_methodExpr = 165, 
		RULE_receiverType = 166, RULE_eos = 167;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "sourceFile", "importSpec", "importDecl", 
			"ghostMember", "ghostStatement", "auxiliaryStatement", "statementWithSpec", 
			"outlineStatement", "ghostPrimaryExpr", "permission", "typeExpr", "boundVariables", 
			"boundVariableDecl", "triggers", "trigger", "predicateAccess", "optionSome", 
			"optionNone", "optionGet", "sConversion", "old", "oldLabelUse", "labelUse", 
			"before", "isComparable", "typeOf", "access", "range", "seqUpdExp", "seqUpdClause", 
			"ghostTypeLit", "domainType", "domainClause", "ghostSliceType", "sqType", 
			"specification", "specStatement", "terminationMeasure", "assertion", 
			"blockWithBodyParameterInfo", "implementationProof", "methodImplementationProof", 
			"nonLocalReceiver", "selection", "implementationProofPredicateAlias", 
			"make", "new_", "specMember", "functionDecl", "methodDecl", "explicitGhostMember", 
			"fpredicateDecl", "predicateBody", "mpredicateDecl", "varSpec", "shortVarDecl", 
			"receiver", "parameterDecl", "actualParameterDecl", "ghostParameterDecl", 
			"parameterType", "expression", "statement", "applyStmt", "packageStmt", 
			"specForStmt", "loopSpec", "basicLit", "primaryExpr", "predConstructArgs", 
			"interfaceType", "predicateSpec", "methodSpec", "type_", "typeLit", "predType", 
			"predTypeParams", "literalType", "implicitArray", "slice_", "low", "high", 
			"cap", "assign_op", "packageClause", "importPath", "declaration", "constDecl", 
			"constSpec", "identifierList", "expressionList", "typeDecl", "typeSpec", 
			"varDecl", "block", "statementList", "simpleStmt", "expressionStmt", 
			"sendStmt", "incDecStmt", "assignment", "emptyStmt", "labeledStmt", "returnStmt", 
			"breakStmt", "continueStmt", "gotoStmt", "fallthroughStmt", "deferStmt", 
			"ifStmt", "switchStmt", "exprSwitchStmt", "exprCaseClause", "exprSwitchCase", 
			"typeSwitchStmt", "typeSwitchGuard", "typeCaseClause", "typeSwitchCase", 
			"typeList", "selectStmt", "commClause", "commCase", "recvStmt", "forStmt", 
			"forClause", "rangeClause", "goStmt", "typeName", "arrayType", "arrayLength", 
			"elementType", "pointerType", "sliceType", "mapType", "channelType", 
			"functionType", "signature", "result", "parameters", "conversion", "nonNamedType", 
			"operand", "literal", "integer", "operandName", "qualifiedIdent", "compositeLit", 
			"literalValue", "elementList", "keyedElement", "key", "element", "structType", 
			"fieldDecl", "string_", "embeddedField", "functionLit", "index", "typeAssertion", 
			"arguments", "methodExpr", "receiverType", "eos"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'true'", "'false'", "'assert'", "'assume'", "'inhale'", 
			"'exhale'", "'requires'", "'preserves'", "'ensures'", "'invariant'", 
			"'decreases'", "'pure'", "'implements'", "'old'", "'before'", "'#lhs'", 
			"'forall'", "'exists'", "'acc'", "'fold'", "'unfold'", "'unfolding'", 
			"'ghost'", "'in'", "'#'", "'subset'", "'union'", "'intersection'", "'setminus'", 
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
			"IMPL", "OLD", "BEFORE", "LHS", "FORALL", "EXISTS", "ACCESS", "FOLD", 
			"UNFOLD", "UNFOLDING", "GHOST", "IN", "MULTI", "SUBSET", "UNION", "INTERSECTION", 
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
			setState(336);
			expression(0);
			setState(337);
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
			setState(339);
			statement();
			setState(340);
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
			setState(342);
			type_();
			setState(343);
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
			setState(345);
			maybeAddressableIdentifier();
			setState(350);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(346);
				match(COMMA);
				setState(347);
				maybeAddressableIdentifier();
				}
				}
				setState(352);
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
			setState(353);
			match(IDENTIFIER);
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(354);
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
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
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
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(357);
				specification();
				}
				break;
			}
			setState(360);
			packageClause();
			setState(361);
			eos();
			setState(367);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(362);
					importDecl();
					setState(363);
					eos();
					}
					} 
				}
				setState(369);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(379);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED) | (1L << TRUSTED))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (CONST - 67)) | (1L << (TYPE - 67)) | (1L << (VAR - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (STAR - 67)) | (1L << (RECEIVE - 67)))) != 0)) {
				{
				{
				setState(373);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(370);
					specMember();
					}
					break;
				case 2:
					{
					setState(371);
					declaration();
					}
					break;
				case 3:
					{
					setState(372);
					ghostMember();
					}
					break;
				}
				setState(375);
				eos();
				}
				}
				setState(381);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(382);
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

	public static class ImportSpecContext extends ParserRuleContext {
		public Token alias;
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
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
		enterRule(_localctx, 12, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			specification();
			setState(386);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(385);
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

			setState(388);
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
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
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
		enterRule(_localctx, 14, RULE_importDecl);
		int _la;
		try {
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(394);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(390);
					specification();
					setState(392);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						setState(391);
						eos();
						}
						break;
					}
					}
					break;
				}
				setState(396);
				match(IMPORT);
				setState(397);
				importSpec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(398);
				match(IMPORT);
				setState(399);
				match(L_PAREN);
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << TRUSTED))) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & ((1L << (IDENTIFIER - 91)) | (1L << (DOT - 91)) | (1L << (RAW_STRING_LIT - 91)) | (1L << (INTERPRETED_STRING_LIT - 91)))) != 0)) {
					{
					{
					setState(400);
					importSpec();
					setState(401);
					eos();
					}
					}
					setState(407);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(408);
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
		enterRule(_localctx, 16, RULE_ghostMember);
		try {
			setState(415);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(412);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(413);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(414);
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
		enterRule(_localctx, 18, RULE_ghostStatement);
		int _la;
		try {
			setState(423);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				_localctx = new ExplicitGhostStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(417);
				match(GHOST);
				setState(418);
				statement();
				}
				break;
			case FOLD:
			case UNFOLD:
				_localctx = new FoldStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(419);
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
				setState(420);
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
				setState(421);
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
				setState(422);
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
		enterRule(_localctx, 20, RULE_auxiliaryStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(425);
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
		enterRule(_localctx, 22, RULE_statementWithSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			((StatementWithSpecContext)_localctx).specification = specification();
			{
			setState(428);
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
		enterRule(_localctx, 24, RULE_outlineStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(430);
			match(OUTLINE);
			setState(431);
			match(L_PAREN);
			setState(433);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(432);
				statementList();
				}
				break;
			}
			setState(435);
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
		enterRule(_localctx, 26, RULE_ghostPrimaryExpr);
		try {
			setState(449);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(437);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(438);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(439);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(440);
				typeExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(441);
				isComparable();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(442);
				old();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(443);
				before();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(444);
				sConversion();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(445);
				optionNone();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(446);
				optionSome();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(447);
				optionGet();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(448);
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
		enterRule(_localctx, 28, RULE_permission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
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
		enterRule(_localctx, 30, RULE_typeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			match(TYPE);
			setState(454);
			match(L_BRACKET);
			setState(455);
			type_();
			setState(456);
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
		enterRule(_localctx, 32, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			boundVariableDecl();
			setState(463);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(459);
					match(COMMA);
					setState(460);
					boundVariableDecl();
					}
					} 
				}
				setState(465);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(466);
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
		enterRule(_localctx, 34, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			match(IDENTIFIER);
			setState(474);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(470);
				match(COMMA);
				setState(471);
				match(IDENTIFIER);
				}
				}
				setState(476);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(477);
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
		enterRule(_localctx, 36, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(479);
				trigger();
				}
				}
				setState(484);
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
		enterRule(_localctx, 38, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			match(L_CURLY);
			setState(486);
			expression(0);
			setState(491);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(487);
				match(COMMA);
				setState(488);
				expression(0);
				}
				}
				setState(493);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(494);
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
		enterRule(_localctx, 40, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
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
		enterRule(_localctx, 42, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			match(SOME);
			setState(499);
			match(L_PAREN);
			setState(500);
			expression(0);
			setState(501);
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
		enterRule(_localctx, 44, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			match(NONE);
			setState(504);
			match(L_BRACKET);
			setState(505);
			type_();
			setState(506);
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
		enterRule(_localctx, 46, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(508);
			match(GET);
			setState(509);
			match(L_PAREN);
			setState(510);
			expression(0);
			setState(511);
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
		enterRule(_localctx, 48, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
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
			setState(514);
			match(L_PAREN);
			setState(515);
			expression(0);
			setState(516);
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
		enterRule(_localctx, 50, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(518);
			match(OLD);
			setState(523);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(519);
				match(L_BRACKET);
				setState(520);
				oldLabelUse();
				setState(521);
				match(R_BRACKET);
				}
			}

			setState(525);
			match(L_PAREN);
			setState(526);
			expression(0);
			setState(527);
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
		enterRule(_localctx, 52, RULE_oldLabelUse);
		try {
			setState(531);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(529);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(530);
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
		enterRule(_localctx, 54, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(533);
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
		enterRule(_localctx, 56, RULE_before);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(BEFORE);
			setState(536);
			match(L_PAREN);
			setState(537);
			expression(0);
			setState(538);
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
		enterRule(_localctx, 58, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(540);
			match(IS_COMPARABLE);
			setState(541);
			match(L_PAREN);
			setState(542);
			expression(0);
			setState(543);
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
		enterRule(_localctx, 60, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(TYPE_OF);
			setState(546);
			match(L_PAREN);
			setState(547);
			expression(0);
			setState(548);
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
		enterRule(_localctx, 62, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(550);
			match(ACCESS);
			setState(551);
			match(L_PAREN);
			setState(552);
			expression(0);
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(553);
				match(COMMA);
				setState(554);
				expression(0);
				}
			}

			setState(557);
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
		enterRule(_localctx, 64, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
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
			setState(560);
			match(L_BRACKET);
			setState(561);
			expression(0);
			setState(562);
			match(DOT_DOT);
			setState(563);
			expression(0);
			setState(564);
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
		enterRule(_localctx, 66, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			match(L_BRACKET);
			{
			setState(567);
			seqUpdClause();
			setState(572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(568);
				match(COMMA);
				setState(569);
				seqUpdClause();
				}
				}
				setState(574);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(575);
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
		enterRule(_localctx, 68, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
			expression(0);
			setState(578);
			match(ASSIGN);
			setState(579);
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
		enterRule(_localctx, 70, RULE_ghostTypeLit);
		try {
			setState(584);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(581);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(582);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(583);
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
		enterRule(_localctx, 72, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586);
			match(DOM);
			setState(587);
			match(L_CURLY);
			setState(593);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(588);
				domainClause();
				setState(589);
				eos();
				}
				}
				setState(595);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(596);
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
		enterRule(_localctx, 74, RULE_domainClause);
		try {
			setState(607);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(598);
				match(FUNC);
				setState(599);
				match(IDENTIFIER);
				setState(600);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(601);
				match(AXIOM);
				setState(602);
				match(L_CURLY);
				setState(603);
				expression(0);
				setState(604);
				eos();
				setState(605);
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
		enterRule(_localctx, 76, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			match(GHOST);
			setState(610);
			match(L_BRACKET);
			setState(611);
			match(R_BRACKET);
			setState(612);
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
		enterRule(_localctx, 78, RULE_sqType);
		int _la;
		try {
			setState(625);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(614);
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
				setState(615);
				match(L_BRACKET);
				setState(616);
				type_();
				setState(617);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(619);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(620);
				match(L_BRACKET);
				setState(621);
				type_();
				setState(622);
				match(R_BRACKET);
				setState(623);
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
		enterRule(_localctx, 80, RULE_specification);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(632);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(627);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(628);
						match(PURE);
						((SpecificationContext)_localctx).pure =  true;
						}
						break;
					case TRUSTED:
						{
						setState(630);
						match(TRUSTED);
						((SpecificationContext)_localctx).trusted =  true;
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(634);
					eos();
					}
					} 
				}
				setState(639);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			setState(642);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(640);
				match(PURE);
				((SpecificationContext)_localctx).pure =  true;
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
		enterRule(_localctx, 82, RULE_specStatement);
		try {
			setState(652);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(644);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(645);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(646);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(647);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(648);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(649);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(650);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(651);
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
		enterRule(_localctx, 84, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(655);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(654);
				expressionList();
				}
				break;
			}
			setState(659);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(657);
				match(IF);
				setState(658);
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
		enterRule(_localctx, 86, RULE_assertion);
		try {
			setState(663);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(662);
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
		enterRule(_localctx, 88, RULE_blockWithBodyParameterInfo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(665);
			match(L_CURLY);
			setState(670);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(666);
				match(SHARE);
				setState(667);
				identifierList();
				setState(668);
				eos();
				}
				break;
			}
			setState(673);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(672);
				statementList();
				}
				break;
			}
			setState(675);
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
		enterRule(_localctx, 90, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(677);
			type_();
			setState(678);
			match(IMPL);
			setState(679);
			type_();
			setState(698);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(680);
				match(L_CURLY);
				setState(686);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(681);
					implementationProofPredicateAlias();
					setState(682);
					eos();
					}
					}
					setState(688);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(694);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(689);
					methodImplementationProof();
					setState(690);
					eos();
					}
					}
					setState(696);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(697);
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
		enterRule(_localctx, 92, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(700);
				match(PURE);
				}
			}

			setState(703);
			nonLocalReceiver();
			setState(704);
			match(IDENTIFIER);
			setState(705);
			signature();
			setState(707);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(706);
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
		enterRule(_localctx, 94, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(709);
			match(L_PAREN);
			setState(711);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(710);
				match(IDENTIFIER);
				}
				break;
			}
			setState(714);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(713);
				match(STAR);
				}
			}

			setState(716);
			typeName();
			setState(717);
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
		enterRule(_localctx, 96, RULE_selection);
		try {
			setState(724);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(719);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(720);
				type_();
				setState(721);
				match(DOT);
				setState(722);
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
		enterRule(_localctx, 98, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			match(PRED);
			setState(727);
			match(IDENTIFIER);
			setState(728);
			match(DECLARE_ASSIGN);
			setState(731);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(729);
				selection();
				}
				break;
			case 2:
				{
				setState(730);
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
		enterRule(_localctx, 100, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(733);
			match(MAKE);
			setState(734);
			match(L_PAREN);
			setState(735);
			type_();
			setState(738);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(736);
				match(COMMA);
				setState(737);
				expressionList();
				}
			}

			setState(740);
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
		enterRule(_localctx, 102, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(742);
			match(NEW);
			setState(743);
			match(L_PAREN);
			setState(744);
			type_();
			setState(745);
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
		enterRule(_localctx, 104, RULE_specMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(747);
			((SpecMemberContext)_localctx).specification = specification();
			setState(750);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(748);
				functionDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 2:
				{
				setState(749);
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
		enterRule(_localctx, 106, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(752);
			match(FUNC);
			setState(753);
			match(IDENTIFIER);
			{
			setState(754);
			signature();
			setState(756);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(755);
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
		enterRule(_localctx, 108, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(758);
			match(FUNC);
			setState(759);
			receiver();
			setState(760);
			match(IDENTIFIER);
			{
			setState(761);
			signature();
			setState(763);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(762);
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
		enterRule(_localctx, 110, RULE_explicitGhostMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(765);
			match(GHOST);
			setState(768);
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
				setState(766);
				specMember();
				}
				break;
			case CONST:
			case TYPE:
			case VAR:
				{
				setState(767);
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
		enterRule(_localctx, 112, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(770);
			match(PRED);
			setState(771);
			match(IDENTIFIER);
			setState(772);
			parameters();
			setState(774);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(773);
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
		enterRule(_localctx, 114, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			match(L_CURLY);
			setState(777);
			expression(0);
			setState(778);
			eos();
			setState(779);
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
		enterRule(_localctx, 116, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(781);
			match(PRED);
			setState(782);
			receiver();
			setState(783);
			match(IDENTIFIER);
			setState(784);
			parameters();
			setState(786);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(785);
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
		enterRule(_localctx, 118, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(788);
			maybeAddressableIdentifierList();
			setState(796);
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
				setState(789);
				type_();
				setState(792);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
				case 1:
					{
					setState(790);
					match(ASSIGN);
					setState(791);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(794);
				match(ASSIGN);
				setState(795);
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
		enterRule(_localctx, 120, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(798);
			maybeAddressableIdentifierList();
			setState(799);
			match(DECLARE_ASSIGN);
			setState(800);
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
		enterRule(_localctx, 122, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(802);
			match(L_PAREN);
			setState(804);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(803);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(806);
			type_();
			setState(808);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(807);
				match(COMMA);
				}
			}

			setState(810);
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
		enterRule(_localctx, 124, RULE_parameterDecl);
		try {
			setState(814);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(812);
				actualParameterDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(813);
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
		enterRule(_localctx, 126, RULE_actualParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(816);
				identifierList();
				}
				break;
			}
			setState(819);
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
		enterRule(_localctx, 128, RULE_ghostParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			match(GHOST);
			setState(823);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(822);
				identifierList();
				}
				break;
			}
			setState(825);
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
		enterRule(_localctx, 130, RULE_parameterType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(828);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(827);
				match(ELLIPSIS);
				}
			}

			setState(830);
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
		int _startState = 132;
		enterRecursionRule(_localctx, 132, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(848);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(833);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (EXCLAMATION - 121)) | (1L << (PLUS - 121)) | (1L << (MINUS - 121)) | (1L << (CARET - 121)) | (1L << (STAR - 121)) | (1L << (AMPERSAND - 121)) | (1L << (RECEIVE - 121)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(834);
				expression(13);
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(835);
				primaryExpr(0);
				}
				break;
			case 3:
				{
				_localctx = new UnfoldingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(836);
				match(UNFOLDING);
				setState(837);
				predicateAccess();
				setState(838);
				match(IN);
				setState(839);
				expression(2);
				}
				break;
			case 4:
				{
				_localctx = new QuantificationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(841);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(842);
				boundVariables();
				setState(843);
				match(COLON);
				setState(844);
				match(COLON);
				setState(845);
				triggers();
				setState(846);
				expression(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(882);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(880);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
					case 1:
						{
						_localctx = new MulExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(850);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(851);
						((MulExprContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 116)) & ~0x3f) == 0 && ((1L << (_la - 116)) & ((1L << (DIV - 116)) | (1L << (MOD - 116)) | (1L << (LSHIFT - 116)) | (1L << (RSHIFT - 116)) | (1L << (BIT_CLEAR - 116)) | (1L << (STAR - 116)) | (1L << (AMPERSAND - 116)))) != 0)) ) {
							((MulExprContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(852);
						expression(12);
						}
						break;
					case 2:
						{
						_localctx = new AddExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(853);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(854);
						((AddExprContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 103)) & ~0x3f) == 0 && ((1L << (_la - 103)) & ((1L << (PLUS_PLUS - 103)) | (1L << (OR - 103)) | (1L << (PLUS - 103)) | (1L << (MINUS - 103)) | (1L << (CARET - 103)))) != 0)) ) {
							((AddExprContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(855);
						expression(11);
						}
						break;
					case 3:
						{
						_localctx = new P42ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(856);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(857);
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
						setState(858);
						expression(10);
						}
						break;
					case 4:
						{
						_localctx = new P41ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(859);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(860);
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
						setState(861);
						expression(9);
						}
						break;
					case 5:
						{
						_localctx = new RelExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(862);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(863);
						((RelExprContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & ((1L << (EQUALS - 109)) | (1L << (NOT_EQUALS - 109)) | (1L << (LESS - 109)) | (1L << (LESS_OR_EQUALS - 109)) | (1L << (GREATER - 109)) | (1L << (GREATER_OR_EQUALS - 109)))) != 0)) ) {
							((RelExprContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(864);
						expression(8);
						}
						break;
					case 6:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(865);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(866);
						match(LOGICAL_AND);
						setState(867);
						expression(7);
						}
						break;
					case 7:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(868);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(869);
						match(LOGICAL_OR);
						setState(870);
						expression(6);
						}
						break;
					case 8:
						{
						_localctx = new ImplicationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(871);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(872);
						match(IMPLIES);
						setState(873);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(874);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(875);
						match(QMARK);
						setState(876);
						expression(0);
						setState(877);
						match(COLON);
						setState(878);
						expression(3);
						}
						break;
					}
					} 
				}
				setState(884);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
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
		enterRule(_localctx, 134, RULE_statement);
		try {
			setState(904);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(885);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(886);
				auxiliaryStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(887);
				packageStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(888);
				applyStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(889);
				declaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(890);
				labeledStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(891);
				simpleStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(892);
				goStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(893);
				returnStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(894);
				breakStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(895);
				continueStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(896);
				gotoStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(897);
				fallthroughStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(898);
				block();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(899);
				ifStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(900);
				switchStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(901);
				selectStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(902);
				specForStmt();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(903);
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
		enterRule(_localctx, 136, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(906);
			match(APPLY);
			setState(907);
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
		enterRule(_localctx, 138, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(909);
			match(PACKAGE);
			setState(910);
			expression(0);
			setState(912);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(911);
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
		enterRule(_localctx, 140, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(914);
			loopSpec();
			setState(915);
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
		enterRule(_localctx, 142, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(923);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(917);
				match(INV);
				setState(918);
				expression(0);
				setState(919);
				eos();
				}
				}
				setState(925);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(930);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(926);
				match(DEC);
				setState(927);
				terminationMeasure();
				setState(928);
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
		enterRule(_localctx, 144, RULE_basicLit);
		try {
			setState(940);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(932);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(933);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(934);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(935);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(936);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(937);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(938);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(939);
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
		int _startState = 146;
		enterRecursionRule(_localctx, 146, RULE_primaryExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(954);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				_localctx = new OperandPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(943);
				operand();
				}
				break;
			case 2:
				{
				_localctx = new ConversionPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(944);
				conversion();
				}
				break;
			case 3:
				{
				_localctx = new MethodPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(945);
				methodExpr();
				}
				break;
			case 4:
				{
				_localctx = new GhostPrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(946);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(947);
				new_();
				}
				break;
			case 6:
				{
				_localctx = new MakeExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(948);
				make();
				}
				break;
			case 7:
				{
				_localctx = new BuiltInCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(949);
				((BuiltInCallExprContext)_localctx).call_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 43)) & ~0x3f) == 0 && ((1L << (_la - 43)) & ((1L << (LEN - 43)) | (1L << (CAP - 43)) | (1L << (DOM - 43)) | (1L << (RANGE - 43)))) != 0)) ) {
					((BuiltInCallExprContext)_localctx).call_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(950);
				match(L_PAREN);
				setState(951);
				expression(0);
				setState(952);
				match(R_PAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(973);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(971);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(956);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(957);
						match(DOT);
						setState(958);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new IndexPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(959);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(960);
						index();
						}
						break;
					case 3:
						{
						_localctx = new SlicePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(961);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(962);
						slice_();
						}
						break;
					case 4:
						{
						_localctx = new SeqUpdPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(963);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(964);
						seqUpdExp();
						}
						break;
					case 5:
						{
						_localctx = new TypeAssertionPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(965);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(966);
						typeAssertion();
						}
						break;
					case 6:
						{
						_localctx = new InvokePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(967);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(968);
						arguments();
						}
						break;
					case 7:
						{
						_localctx = new PredConstrPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(969);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(970);
						predConstructArgs();
						}
						break;
					}
					} 
				}
				setState(975);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
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
		enterRule(_localctx, 148, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(976);
			match(L_PRED);
			setState(978);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(977);
				expressionList();
				}
			}

			setState(981);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(980);
				match(COMMA);
				}
			}

			setState(983);
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
		enterRule(_localctx, 150, RULE_interfaceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(985);
			match(INTERFACE);
			setState(986);
			match(L_CURLY);
			setState(996);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << PRED) | (1L << TRUSTED))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(990);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
				case 1:
					{
					setState(987);
					methodSpec();
					}
					break;
				case 2:
					{
					setState(988);
					typeName();
					}
					break;
				case 3:
					{
					setState(989);
					predicateSpec();
					}
					break;
				}
				setState(992);
				eos();
				}
				}
				setState(998);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(999);
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
		enterRule(_localctx, 152, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001);
			match(PRED);
			setState(1002);
			match(IDENTIFIER);
			setState(1003);
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
		enterRule(_localctx, 154, RULE_methodSpec);
		int _la;
		try {
			setState(1020);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1006);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1005);
					match(GHOST);
					}
				}

				setState(1008);
				specification();
				setState(1009);
				match(IDENTIFIER);
				setState(1010);
				parameters();
				setState(1011);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1014);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1013);
					match(GHOST);
					}
				}

				setState(1016);
				specification();
				setState(1017);
				match(IDENTIFIER);
				setState(1018);
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
		enterRule(_localctx, 156, RULE_type_);
		try {
			setState(1029);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1022);
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
				setState(1023);
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
				setState(1024);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(1025);
				match(L_PAREN);
				setState(1026);
				type_();
				setState(1027);
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
		enterRule(_localctx, 158, RULE_typeLit);
		try {
			setState(1040);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1031);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1032);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1033);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1034);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1035);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1036);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1037);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1038);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1039);
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
		enterRule(_localctx, 160, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1042);
			match(PRED);
			setState(1043);
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
		enterRule(_localctx, 162, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			match(L_PAREN);
			setState(1057);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (STAR - 67)) | (1L << (RECEIVE - 67)))) != 0)) {
				{
				setState(1046);
				type_();
				setState(1051);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1047);
						match(COMMA);
						setState(1048);
						type_();
						}
						} 
					}
					setState(1053);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
				}
				setState(1055);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1054);
					match(COMMA);
					}
				}

				}
			}

			setState(1059);
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
		enterRule(_localctx, 164, RULE_literalType);
		try {
			setState(1068);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1061);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1062);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1063);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1064);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1065);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1066);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1067);
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
		enterRule(_localctx, 166, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1070);
			match(L_BRACKET);
			setState(1071);
			match(ELLIPSIS);
			setState(1072);
			match(R_BRACKET);
			setState(1073);
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
		enterRule(_localctx, 168, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1075);
			match(L_BRACKET);
			setState(1091);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(1077);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1076);
					low();
					}
				}

				setState(1079);
				match(COLON);
				setState(1081);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1080);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1084);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1083);
					low();
					}
				}

				setState(1086);
				match(COLON);
				setState(1087);
				high();
				setState(1088);
				match(COLON);
				setState(1089);
				cap();
				}
				break;
			}
			setState(1093);
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
		enterRule(_localctx, 170, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
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
		enterRule(_localctx, 172, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1097);
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
		enterRule(_localctx, 174, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1099);
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
		enterRule(_localctx, 176, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (OR - 115)) | (1L << (DIV - 115)) | (1L << (MOD - 115)) | (1L << (LSHIFT - 115)) | (1L << (RSHIFT - 115)) | (1L << (BIT_CLEAR - 115)) | (1L << (PLUS - 115)) | (1L << (MINUS - 115)) | (1L << (CARET - 115)) | (1L << (STAR - 115)) | (1L << (AMPERSAND - 115)))) != 0)) {
				{
				setState(1101);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (OR - 115)) | (1L << (DIV - 115)) | (1L << (MOD - 115)) | (1L << (LSHIFT - 115)) | (1L << (RSHIFT - 115)) | (1L << (BIT_CLEAR - 115)) | (1L << (PLUS - 115)) | (1L << (MINUS - 115)) | (1L << (CARET - 115)) | (1L << (STAR - 115)) | (1L << (AMPERSAND - 115)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1104);
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
		enterRule(_localctx, 178, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1106);
			match(PACKAGE);
			setState(1107);
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
		enterRule(_localctx, 180, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1109);
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
		enterRule(_localctx, 182, RULE_declaration);
		try {
			setState(1114);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1111);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1112);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1113);
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
		enterRule(_localctx, 184, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1116);
			match(CONST);
			setState(1128);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1117);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1118);
				match(L_PAREN);
				setState(1124);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1119);
					constSpec();
					setState(1120);
					eos();
					}
					}
					setState(1126);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1127);
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
		enterRule(_localctx, 186, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1130);
			identifierList();
			setState(1136);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				{
				setState(1132);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (STAR - 67)) | (1L << (RECEIVE - 67)))) != 0)) {
					{
					setState(1131);
					type_();
					}
				}

				setState(1134);
				match(ASSIGN);
				setState(1135);
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
		enterRule(_localctx, 188, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1138);
			match(IDENTIFIER);
			setState(1143);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,95,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1139);
					match(COMMA);
					setState(1140);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,95,_ctx);
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
		enterRule(_localctx, 190, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1146);
			expression(0);
			setState(1151);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,96,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1147);
					match(COMMA);
					setState(1148);
					expression(0);
					}
					} 
				}
				setState(1153);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,96,_ctx);
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
		enterRule(_localctx, 192, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1154);
			match(TYPE);
			setState(1166);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1155);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1156);
				match(L_PAREN);
				setState(1162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1157);
					typeSpec();
					setState(1158);
					eos();
					}
					}
					setState(1164);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1165);
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
		enterRule(_localctx, 194, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1168);
			match(IDENTIFIER);
			setState(1170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1169);
				match(ASSIGN);
				}
			}

			setState(1172);
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
		enterRule(_localctx, 196, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1174);
			match(VAR);
			setState(1186);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1175);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1176);
				match(L_PAREN);
				setState(1182);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1177);
					varSpec();
					setState(1178);
					eos();
					}
					}
					setState(1184);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1185);
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
		enterRule(_localctx, 198, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1188);
			match(L_CURLY);
			setState(1190);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				{
				setState(1189);
				statementList();
				}
				break;
			}
			setState(1192);
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
		enterRule(_localctx, 200, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1200); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1195);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
					case 1:
						{
						setState(1194);
						eos();
						}
						break;
					}
					setState(1197);
					statement();
					setState(1198);
					eos();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1202); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
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
		enterRule(_localctx, 202, RULE_simpleStmt);
		try {
			setState(1209);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1204);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1205);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1206);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1207);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1208);
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
		enterRule(_localctx, 204, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1211);
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
		enterRule(_localctx, 206, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1213);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1214);
			match(RECEIVE);
			setState(1215);
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
		enterRule(_localctx, 208, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1217);
			expression(0);
			setState(1218);
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
		enterRule(_localctx, 210, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1220);
			expressionList();
			setState(1221);
			assign_op();
			setState(1222);
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
		enterRule(_localctx, 212, RULE_emptyStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1224);
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
		enterRule(_localctx, 214, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1226);
			match(IDENTIFIER);
			setState(1227);
			match(COLON);
			setState(1229);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1228);
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
		enterRule(_localctx, 216, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			match(RETURN);
			setState(1233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(1232);
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
		enterRule(_localctx, 218, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			match(BREAK);
			setState(1237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				{
				setState(1236);
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
		enterRule(_localctx, 220, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1239);
			match(CONTINUE);
			setState(1241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1240);
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
		enterRule(_localctx, 222, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1243);
			match(GOTO);
			setState(1244);
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
		enterRule(_localctx, 224, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1246);
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
		enterRule(_localctx, 226, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1248);
			match(DEFER);
			setState(1249);
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
		enterRule(_localctx, 228, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			match(IF);
			setState(1260);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				{
				setState(1252);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1253);
				eos();
				setState(1254);
				expression(0);
				}
				break;
			case 3:
				{
				setState(1256);
				simpleStmt();
				setState(1257);
				eos();
				setState(1258);
				expression(0);
				}
				break;
			}
			setState(1262);
			block();
			setState(1268);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				{
				setState(1263);
				match(ELSE);
				setState(1266);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1264);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1265);
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
		enterRule(_localctx, 230, RULE_switchStmt);
		try {
			setState(1272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1270);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1271);
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
		enterRule(_localctx, 232, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1274);
			match(SWITCH);
			setState(1285);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				{
				setState(1276);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1275);
					expression(0);
					}
				}

				}
				break;
			case 2:
				{
				setState(1279);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
				case 1:
					{
					setState(1278);
					simpleStmt();
					}
					break;
				}
				setState(1281);
				eos();
				setState(1283);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1282);
					expression(0);
					}
				}

				}
				break;
			}
			setState(1287);
			match(L_CURLY);
			setState(1291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1288);
				exprCaseClause();
				}
				}
				setState(1293);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1294);
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
		enterRule(_localctx, 234, RULE_exprCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1296);
			exprSwitchCase();
			setState(1297);
			match(COLON);
			setState(1299);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
			case 1:
				{
				setState(1298);
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
		enterRule(_localctx, 236, RULE_exprSwitchCase);
		try {
			setState(1304);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1301);
				match(CASE);
				setState(1302);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1303);
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
		enterRule(_localctx, 238, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1306);
			match(SWITCH);
			setState(1315);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1307);
				typeSwitchGuard();
				}
				break;
			case 2:
				{
				setState(1308);
				eos();
				setState(1309);
				typeSwitchGuard();
				}
				break;
			case 3:
				{
				setState(1311);
				simpleStmt();
				setState(1312);
				eos();
				setState(1313);
				typeSwitchGuard();
				}
				break;
			}
			setState(1317);
			match(L_CURLY);
			setState(1321);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1318);
				typeCaseClause();
				}
				}
				setState(1323);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1324);
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
		enterRule(_localctx, 240, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1328);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				{
				setState(1326);
				match(IDENTIFIER);
				setState(1327);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1330);
			primaryExpr(0);
			setState(1331);
			match(DOT);
			setState(1332);
			match(L_PAREN);
			setState(1333);
			match(TYPE);
			setState(1334);
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
		enterRule(_localctx, 242, RULE_typeCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1336);
			typeSwitchCase();
			setState(1337);
			match(COLON);
			setState(1339);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1338);
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
		enterRule(_localctx, 244, RULE_typeSwitchCase);
		try {
			setState(1344);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1341);
				match(CASE);
				setState(1342);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1343);
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
		enterRule(_localctx, 246, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1348);
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
				setState(1346);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1347);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1357);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1350);
				match(COMMA);
				setState(1353);
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
					setState(1351);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1352);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1359);
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
		enterRule(_localctx, 248, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1360);
			match(SELECT);
			setState(1361);
			match(L_CURLY);
			setState(1365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1362);
				commClause();
				}
				}
				setState(1367);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1368);
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
		enterRule(_localctx, 250, RULE_commClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			commCase();
			setState(1371);
			match(COLON);
			setState(1373);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1372);
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
		enterRule(_localctx, 252, RULE_commCase);
		try {
			setState(1381);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1375);
				match(CASE);
				setState(1378);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
				case 1:
					{
					setState(1376);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1377);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1380);
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
		enterRule(_localctx, 254, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1389);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1383);
				expressionList();
				setState(1384);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1386);
				identifierList();
				setState(1387);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1391);
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
		enterRule(_localctx, 256, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1393);
			match(FOR);
			setState(1397);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1394);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1395);
				forClause();
				}
				break;
			case 3:
				{
				setState(1396);
				rangeClause();
				}
				break;
			}
			setState(1399);
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
		enterRule(_localctx, 258, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1402);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				{
				setState(1401);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1404);
			eos();
			setState(1406);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1405);
				expression(0);
				}
				break;
			}
			setState(1408);
			eos();
			setState(1410);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1409);
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
		enterRule(_localctx, 260, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				{
				setState(1412);
				expressionList();
				setState(1413);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1415);
				identifierList();
				setState(1416);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1420);
			match(RANGE);
			setState(1421);
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
		enterRule(_localctx, 262, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1423);
			match(GO);
			setState(1424);
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
		enterRule(_localctx, 264, RULE_typeName);
		try {
			setState(1428);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1426);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1427);
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
		enterRule(_localctx, 266, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1430);
			match(L_BRACKET);
			setState(1431);
			arrayLength();
			setState(1432);
			match(R_BRACKET);
			setState(1433);
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
		enterRule(_localctx, 268, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1435);
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
		enterRule(_localctx, 270, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1437);
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
		enterRule(_localctx, 272, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1439);
			match(STAR);
			setState(1440);
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
		enterRule(_localctx, 274, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1442);
			match(L_BRACKET);
			setState(1443);
			match(R_BRACKET);
			setState(1444);
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
		enterRule(_localctx, 276, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1446);
			match(MAP);
			setState(1447);
			match(L_BRACKET);
			setState(1448);
			type_();
			setState(1449);
			match(R_BRACKET);
			setState(1450);
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
		enterRule(_localctx, 278, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1457);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				{
				setState(1452);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1453);
				match(CHAN);
				setState(1454);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1455);
				match(RECEIVE);
				setState(1456);
				match(CHAN);
				}
				break;
			}
			setState(1459);
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
		enterRule(_localctx, 280, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1461);
			match(FUNC);
			setState(1462);
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
		enterRule(_localctx, 282, RULE_signature);
		try {
			setState(1468);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1464);
				parameters();
				setState(1465);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1467);
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
		enterRule(_localctx, 284, RULE_result);
		try {
			setState(1472);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1470);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1471);
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
		enterRule(_localctx, 286, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1474);
			match(L_PAREN);
			setState(1486);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (ELLIPSIS - 67)) | (1L << (STAR - 67)) | (1L << (RECEIVE - 67)))) != 0)) {
				{
				setState(1475);
				parameterDecl();
				setState(1480);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,143,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1476);
						match(COMMA);
						setState(1477);
						parameterDecl();
						}
						} 
					}
					setState(1482);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,143,_ctx);
				}
				setState(1484);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1483);
					match(COMMA);
					}
				}

				}
			}

			setState(1488);
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
		enterRule(_localctx, 288, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1490);
			nonNamedType();
			setState(1491);
			match(L_PAREN);
			setState(1492);
			expression(0);
			setState(1494);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1493);
				match(COMMA);
				}
			}

			setState(1496);
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
		enterRule(_localctx, 290, RULE_nonNamedType);
		try {
			setState(1503);
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
				setState(1498);
				typeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1499);
				match(L_PAREN);
				setState(1500);
				nonNamedType();
				setState(1501);
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
		enterRule(_localctx, 292, RULE_operand);
		try {
			setState(1511);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1505);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1506);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1507);
				match(L_PAREN);
				setState(1508);
				expression(0);
				setState(1509);
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
		enterRule(_localctx, 294, RULE_literal);
		try {
			setState(1516);
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
				setState(1513);
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
				setState(1514);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1515);
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
		enterRule(_localctx, 296, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1518);
			_la = _input.LA(1);
			if ( !(((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)))) != 0)) ) {
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
		enterRule(_localctx, 298, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1520);
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
		enterRule(_localctx, 300, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1522);
			match(IDENTIFIER);
			setState(1523);
			match(DOT);
			setState(1524);
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
		enterRule(_localctx, 302, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1526);
			literalType();
			setState(1527);
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
		enterRule(_localctx, 304, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1529);
			match(L_CURLY);
			setState(1534);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_CURLY - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1530);
				elementList();
				setState(1532);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1531);
					match(COMMA);
					}
				}

				}
			}

			setState(1536);
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
		enterRule(_localctx, 306, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1538);
			keyedElement();
			setState(1543);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,152,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1539);
					match(COMMA);
					setState(1540);
					keyedElement();
					}
					} 
				}
				setState(1545);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,152,_ctx);
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
		enterRule(_localctx, 308, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1549);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				{
				setState(1546);
				key();
				setState(1547);
				match(COLON);
				}
				break;
			}
			setState(1551);
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
		enterRule(_localctx, 310, RULE_key);
		try {
			setState(1555);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case OLD:
			case BEFORE:
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
				setState(1553);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1554);
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
		enterRule(_localctx, 312, RULE_element);
		try {
			setState(1559);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LIT:
			case TRUE:
			case FALSE:
			case OLD:
			case BEFORE:
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
				setState(1557);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1558);
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
		enterRule(_localctx, 314, RULE_structType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1561);
			match(STRUCT);
			setState(1562);
			match(L_CURLY);
			setState(1568);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(1563);
				fieldDecl();
				setState(1564);
				eos();
				}
				}
				setState(1570);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1571);
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
		enterRule(_localctx, 316, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1577);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
			case 1:
				{
				setState(1573);
				identifierList();
				setState(1574);
				type_();
				}
				break;
			case 2:
				{
				setState(1576);
				embeddedField();
				}
				break;
			}
			setState(1580);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
			case 1:
				{
				setState(1579);
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
		enterRule(_localctx, 318, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1582);
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
		enterRule(_localctx, 320, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1585);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1584);
				match(STAR);
				}
			}

			setState(1587);
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
		enterRule(_localctx, 322, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1589);
			match(FUNC);
			setState(1590);
			signature();
			setState(1591);
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
		enterRule(_localctx, 324, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1593);
			match(L_BRACKET);
			setState(1594);
			expression(0);
			setState(1595);
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
		enterRule(_localctx, 326, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1597);
			match(DOT);
			setState(1598);
			match(L_PAREN);
			setState(1599);
			type_();
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
		enterRule(_localctx, 328, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1602);
			match(L_PAREN);
			setState(1617);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (FUNC - 67)) | (1L << (INTERFACE - 67)) | (1L << (MAP - 67)) | (1L << (STRUCT - 67)) | (1L << (CHAN - 67)) | (1L << (RANGE - 67)) | (1L << (TYPE - 67)) | (1L << (NIL_LIT - 67)) | (1L << (IDENTIFIER - 67)) | (1L << (L_PAREN - 67)) | (1L << (L_BRACKET - 67)) | (1L << (EXCLAMATION - 67)) | (1L << (PLUS - 67)) | (1L << (MINUS - 67)) | (1L << (CARET - 67)) | (1L << (STAR - 67)) | (1L << (AMPERSAND - 67)) | (1L << (RECEIVE - 67)) | (1L << (DECIMAL_LIT - 67)) | (1L << (BINARY_LIT - 67)) | (1L << (OCTAL_LIT - 67)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (HEX_LIT - 131)) | (1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1609);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,161,_ctx) ) {
				case 1:
					{
					setState(1603);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1604);
					nonNamedType();
					setState(1607);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
					case 1:
						{
						setState(1605);
						match(COMMA);
						setState(1606);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1612);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1611);
					match(ELLIPSIS);
					}
				}

				setState(1615);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1614);
					match(COMMA);
					}
				}

				}
			}

			setState(1619);
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
		enterRule(_localctx, 330, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1621);
			nonNamedType();
			setState(1622);
			match(DOT);
			setState(1623);
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
		enterRule(_localctx, 332, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1625);
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
		enterRule(_localctx, 334, RULE_eos);
		try {
			setState(1631);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1627);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1628);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1629);
				match(EOS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1630);
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
		case 66:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 73:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 167:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0098\u0664\4\2\t"+
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
		"\t\u00a9\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u015f\n\5"+
		"\f\5\16\5\u0162\13\5\3\6\3\6\5\6\u0166\n\6\3\7\5\7\u0169\n\7\3\7\3\7\3"+
		"\7\3\7\3\7\7\7\u0170\n\7\f\7\16\7\u0173\13\7\3\7\3\7\3\7\5\7\u0178\n\7"+
		"\3\7\3\7\7\7\u017c\n\7\f\7\16\7\u017f\13\7\3\7\3\7\3\b\3\b\5\b\u0185\n"+
		"\b\3\b\3\b\3\t\3\t\5\t\u018b\n\t\5\t\u018d\n\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\7\t\u0196\n\t\f\t\16\t\u0199\13\t\3\t\5\t\u019c\n\t\3\n\3\n\3\n\3"+
		"\n\5\n\u01a2\n\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u01aa\n\13\3\f\3\f"+
		"\3\r\3\r\3\r\3\16\3\16\3\16\5\16\u01b4\n\16\3\16\3\16\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u01c4\n\17\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\7\22\u01d0\n\22\f\22\16\22\u01d3"+
		"\13\22\3\22\5\22\u01d6\n\22\3\23\3\23\3\23\7\23\u01db\n\23\f\23\16\23"+
		"\u01de\13\23\3\23\3\23\3\24\7\24\u01e3\n\24\f\24\16\24\u01e6\13\24\3\25"+
		"\3\25\3\25\3\25\7\25\u01ec\n\25\f\25\16\25\u01ef\13\25\3\25\3\25\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\5\33\u020e"+
		"\n\33\3\33\3\33\3\33\3\33\3\34\3\34\5\34\u0216\n\34\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\3"+
		"!\5!\u022e\n!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\7#\u023d\n"+
		"#\f#\16#\u0240\13#\3#\3#\3$\3$\3$\3$\3%\3%\3%\5%\u024b\n%\3&\3&\3&\3&"+
		"\3&\7&\u0252\n&\f&\16&\u0255\13&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'"+
		"\3\'\5\'\u0262\n\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\5)"+
		"\u0274\n)\3*\3*\3*\3*\3*\5*\u027b\n*\3*\7*\u027e\n*\f*\16*\u0281\13*\3"+
		"*\3*\5*\u0285\n*\3+\3+\3+\3+\3+\3+\3+\3+\5+\u028f\n+\3,\5,\u0292\n,\3"+
		",\3,\5,\u0296\n,\3-\3-\5-\u029a\n-\3.\3.\3.\3.\3.\5.\u02a1\n.\3.\5.\u02a4"+
		"\n.\3.\3.\3/\3/\3/\3/\3/\3/\3/\7/\u02af\n/\f/\16/\u02b2\13/\3/\3/\3/\7"+
		"/\u02b7\n/\f/\16/\u02ba\13/\3/\5/\u02bd\n/\3\60\5\60\u02c0\n\60\3\60\3"+
		"\60\3\60\3\60\5\60\u02c6\n\60\3\61\3\61\5\61\u02ca\n\61\3\61\5\61\u02cd"+
		"\n\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\5\62\u02d7\n\62\3\63\3\63"+
		"\3\63\3\63\3\63\5\63\u02de\n\63\3\64\3\64\3\64\3\64\3\64\5\64\u02e5\n"+
		"\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\5\66\u02f1\n\66"+
		"\3\67\3\67\3\67\3\67\5\67\u02f7\n\67\38\38\38\38\38\58\u02fe\n8\39\39"+
		"\39\59\u0303\n9\3:\3:\3:\3:\5:\u0309\n:\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<"+
		"\5<\u0315\n<\3=\3=\3=\3=\5=\u031b\n=\3=\3=\5=\u031f\n=\3>\3>\3>\3>\3?"+
		"\3?\5?\u0327\n?\3?\3?\5?\u032b\n?\3?\3?\3@\3@\5@\u0331\n@\3A\5A\u0334"+
		"\nA\3A\3A\3B\3B\5B\u033a\nB\3B\3B\3C\5C\u033f\nC\3C\3C\3D\3D\3D\3D\3D"+
		"\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\5D\u0353\nD\3D\3D\3D\3D\3D\3D\3D\3D"+
		"\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\7D"+
		"\u0373\nD\fD\16D\u0376\13D\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3"+
		"E\3E\3E\3E\3E\5E\u038b\nE\3F\3F\3F\3G\3G\3G\5G\u0393\nG\3H\3H\3H\3I\3"+
		"I\3I\3I\7I\u039c\nI\fI\16I\u039f\13I\3I\3I\3I\3I\5I\u03a5\nI\3J\3J\3J"+
		"\3J\3J\3J\3J\3J\5J\u03af\nJ\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\5K\u03bd"+
		"\nK\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\7K\u03ce\nK\fK\16K\u03d1"+
		"\13K\3L\3L\5L\u03d5\nL\3L\5L\u03d8\nL\3L\3L\3M\3M\3M\3M\3M\5M\u03e1\n"+
		"M\3M\3M\7M\u03e5\nM\fM\16M\u03e8\13M\3M\3M\3N\3N\3N\3N\3O\5O\u03f1\nO"+
		"\3O\3O\3O\3O\3O\3O\5O\u03f9\nO\3O\3O\3O\3O\5O\u03ff\nO\3P\3P\3P\3P\3P"+
		"\3P\3P\5P\u0408\nP\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3Q\5Q\u0413\nQ\3R\3R\3R\3S"+
		"\3S\3S\3S\7S\u041c\nS\fS\16S\u041f\13S\3S\5S\u0422\nS\5S\u0424\nS\3S\3"+
		"S\3T\3T\3T\3T\3T\3T\3T\5T\u042f\nT\3U\3U\3U\3U\3U\3V\3V\5V\u0438\nV\3"+
		"V\3V\5V\u043c\nV\3V\5V\u043f\nV\3V\3V\3V\3V\3V\5V\u0446\nV\3V\3V\3W\3"+
		"W\3X\3X\3Y\3Y\3Z\5Z\u0451\nZ\3Z\3Z\3[\3[\3[\3\\\3\\\3]\3]\3]\5]\u045d"+
		"\n]\3^\3^\3^\3^\3^\3^\7^\u0465\n^\f^\16^\u0468\13^\3^\5^\u046b\n^\3_\3"+
		"_\5_\u046f\n_\3_\3_\5_\u0473\n_\3`\3`\3`\7`\u0478\n`\f`\16`\u047b\13`"+
		"\3a\3a\3a\7a\u0480\na\fa\16a\u0483\13a\3b\3b\3b\3b\3b\3b\7b\u048b\nb\f"+
		"b\16b\u048e\13b\3b\5b\u0491\nb\3c\3c\5c\u0495\nc\3c\3c\3d\3d\3d\3d\3d"+
		"\3d\7d\u049f\nd\fd\16d\u04a2\13d\3d\5d\u04a5\nd\3e\3e\5e\u04a9\ne\3e\3"+
		"e\3f\5f\u04ae\nf\3f\3f\3f\6f\u04b3\nf\rf\16f\u04b4\3g\3g\3g\3g\3g\5g\u04bc"+
		"\ng\3h\3h\3i\3i\3i\3i\3j\3j\3j\3k\3k\3k\3k\3l\3l\3m\3m\3m\5m\u04d0\nm"+
		"\3n\3n\5n\u04d4\nn\3o\3o\5o\u04d8\no\3p\3p\5p\u04dc\np\3q\3q\3q\3r\3r"+
		"\3s\3s\3s\3t\3t\3t\3t\3t\3t\3t\3t\3t\5t\u04ef\nt\3t\3t\3t\3t\5t\u04f5"+
		"\nt\5t\u04f7\nt\3u\3u\5u\u04fb\nu\3v\3v\5v\u04ff\nv\3v\5v\u0502\nv\3v"+
		"\3v\5v\u0506\nv\5v\u0508\nv\3v\3v\7v\u050c\nv\fv\16v\u050f\13v\3v\3v\3"+
		"w\3w\3w\5w\u0516\nw\3x\3x\3x\5x\u051b\nx\3y\3y\3y\3y\3y\3y\3y\3y\3y\5"+
		"y\u0526\ny\3y\3y\7y\u052a\ny\fy\16y\u052d\13y\3y\3y\3z\3z\5z\u0533\nz"+
		"\3z\3z\3z\3z\3z\3z\3{\3{\3{\5{\u053e\n{\3|\3|\3|\5|\u0543\n|\3}\3}\5}"+
		"\u0547\n}\3}\3}\3}\5}\u054c\n}\7}\u054e\n}\f}\16}\u0551\13}\3~\3~\3~\7"+
		"~\u0556\n~\f~\16~\u0559\13~\3~\3~\3\177\3\177\3\177\5\177\u0560\n\177"+
		"\3\u0080\3\u0080\3\u0080\5\u0080\u0565\n\u0080\3\u0080\5\u0080\u0568\n"+
		"\u0080\3\u0081\3\u0081\3\u0081\3\u0081\3\u0081\3\u0081\5\u0081\u0570\n"+
		"\u0081\3\u0081\3\u0081\3\u0082\3\u0082\3\u0082\3\u0082\5\u0082\u0578\n"+
		"\u0082\3\u0082\3\u0082\3\u0083\5\u0083\u057d\n\u0083\3\u0083\3\u0083\5"+
		"\u0083\u0581\n\u0083\3\u0083\3\u0083\5\u0083\u0585\n\u0083\3\u0084\3\u0084"+
		"\3\u0084\3\u0084\3\u0084\3\u0084\5\u0084\u058d\n\u0084\3\u0084\3\u0084"+
		"\3\u0084\3\u0085\3\u0085\3\u0085\3\u0086\3\u0086\5\u0086\u0597\n\u0086"+
		"\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0089\3\u0089"+
		"\3\u008a\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b\3\u008c\3\u008c"+
		"\3\u008c\3\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d"+
		"\5\u008d\u05b4\n\u008d\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e\3\u008f"+
		"\3\u008f\3\u008f\3\u008f\5\u008f\u05bf\n\u008f\3\u0090\3\u0090\5\u0090"+
		"\u05c3\n\u0090\3\u0091\3\u0091\3\u0091\3\u0091\7\u0091\u05c9\n\u0091\f"+
		"\u0091\16\u0091\u05cc\13\u0091\3\u0091\5\u0091\u05cf\n\u0091\5\u0091\u05d1"+
		"\n\u0091\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092\3\u0092\5\u0092\u05d9"+
		"\n\u0092\3\u0092\3\u0092\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\5\u0093"+
		"\u05e2\n\u0093\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\5\u0094"+
		"\u05ea\n\u0094\3\u0095\3\u0095\3\u0095\5\u0095\u05ef\n\u0095\3\u0096\3"+
		"\u0096\3\u0097\3\u0097\3\u0098\3\u0098\3\u0098\3\u0098\3\u0099\3\u0099"+
		"\3\u0099\3\u009a\3\u009a\3\u009a\5\u009a\u05ff\n\u009a\5\u009a\u0601\n"+
		"\u009a\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\7\u009b\u0608\n\u009b\f"+
		"\u009b\16\u009b\u060b\13\u009b\3\u009c\3\u009c\3\u009c\5\u009c\u0610\n"+
		"\u009c\3\u009c\3\u009c\3\u009d\3\u009d\5\u009d\u0616\n\u009d\3\u009e\3"+
		"\u009e\5\u009e\u061a\n\u009e\3\u009f\3\u009f\3\u009f\3\u009f\3\u009f\7"+
		"\u009f\u0621\n\u009f\f\u009f\16\u009f\u0624\13\u009f\3\u009f\3\u009f\3"+
		"\u00a0\3\u00a0\3\u00a0\3\u00a0\5\u00a0\u062c\n\u00a0\3\u00a0\5\u00a0\u062f"+
		"\n\u00a0\3\u00a1\3\u00a1\3\u00a2\5\u00a2\u0634\n\u00a2\3\u00a2\3\u00a2"+
		"\3\u00a3\3\u00a3\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4\3\u00a4\3\u00a5"+
		"\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a6\3\u00a6\3\u00a6\3\u00a6\3\u00a6"+
		"\5\u00a6\u064a\n\u00a6\5\u00a6\u064c\n\u00a6\3\u00a6\5\u00a6\u064f\n\u00a6"+
		"\3\u00a6\5\u00a6\u0652\n\u00a6\5\u00a6\u0654\n\u00a6\3\u00a6\3\u00a6\3"+
		"\u00a7\3\u00a7\3\u00a7\3\u00a7\3\u00a8\3\u00a8\3\u00a9\3\u00a9\3\u00a9"+
		"\3\u00a9\5\u00a9\u0662\n\u00a9\3\u00a9\3\u027f\4\u0086\u0094\u00aa\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP"+
		"RTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e"+
		"\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6"+
		"\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be"+
		"\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6"+
		"\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee"+
		"\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106"+
		"\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e"+
		"\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136"+
		"\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c\u014e"+
		"\u0150\2\25\4\2]]hh\3\2\30\31\3\2\7\n\3\2?@\3\2(*\4\2(*,,\3\2{\u0081\3"+
		"\2\25\26\4\2vz\177\u0080\6\2##iiuu|~\3\2\37!\3\2\34\36\3\2ot\6\2--\60"+
		"\60\63\63UU\4\2uz|\u0080\3\2ij\4\2ff\u0097\u0097\4\2\u0082\u0085\u0087"+
		"\u0088\3\2\u008e\u008f\2\u06bb\2\u0152\3\2\2\2\4\u0155\3\2\2\2\6\u0158"+
		"\3\2\2\2\b\u015b\3\2\2\2\n\u0163\3\2\2\2\f\u0168\3\2\2\2\16\u0182\3\2"+
		"\2\2\20\u019b\3\2\2\2\22\u01a1\3\2\2\2\24\u01a9\3\2\2\2\26\u01ab\3\2\2"+
		"\2\30\u01ad\3\2\2\2\32\u01b0\3\2\2\2\34\u01c3\3\2\2\2\36\u01c5\3\2\2\2"+
		" \u01c7\3\2\2\2\"\u01cc\3\2\2\2$\u01d7\3\2\2\2&\u01e4\3\2\2\2(\u01e7\3"+
		"\2\2\2*\u01f2\3\2\2\2,\u01f4\3\2\2\2.\u01f9\3\2\2\2\60\u01fe\3\2\2\2\62"+
		"\u0203\3\2\2\2\64\u0208\3\2\2\2\66\u0215\3\2\2\28\u0217\3\2\2\2:\u0219"+
		"\3\2\2\2<\u021e\3\2\2\2>\u0223\3\2\2\2@\u0228\3\2\2\2B\u0231\3\2\2\2D"+
		"\u0238\3\2\2\2F\u0243\3\2\2\2H\u024a\3\2\2\2J\u024c\3\2\2\2L\u0261\3\2"+
		"\2\2N\u0263\3\2\2\2P\u0273\3\2\2\2R\u027f\3\2\2\2T\u028e\3\2\2\2V\u0291"+
		"\3\2\2\2X\u0299\3\2\2\2Z\u029b\3\2\2\2\\\u02a7\3\2\2\2^\u02bf\3\2\2\2"+
		"`\u02c7\3\2\2\2b\u02d6\3\2\2\2d\u02d8\3\2\2\2f\u02df\3\2\2\2h\u02e8\3"+
		"\2\2\2j\u02ed\3\2\2\2l\u02f2\3\2\2\2n\u02f8\3\2\2\2p\u02ff\3\2\2\2r\u0304"+
		"\3\2\2\2t\u030a\3\2\2\2v\u030f\3\2\2\2x\u0316\3\2\2\2z\u0320\3\2\2\2|"+
		"\u0324\3\2\2\2~\u0330\3\2\2\2\u0080\u0333\3\2\2\2\u0082\u0337\3\2\2\2"+
		"\u0084\u033e\3\2\2\2\u0086\u0352\3\2\2\2\u0088\u038a\3\2\2\2\u008a\u038c"+
		"\3\2\2\2\u008c\u038f\3\2\2\2\u008e\u0394\3\2\2\2\u0090\u039d\3\2\2\2\u0092"+
		"\u03ae\3\2\2\2\u0094\u03bc\3\2\2\2\u0096\u03d2\3\2\2\2\u0098\u03db\3\2"+
		"\2\2\u009a\u03eb\3\2\2\2\u009c\u03fe\3\2\2\2\u009e\u0407\3\2\2\2\u00a0"+
		"\u0412\3\2\2\2\u00a2\u0414\3\2\2\2\u00a4\u0417\3\2\2\2\u00a6\u042e\3\2"+
		"\2\2\u00a8\u0430\3\2\2\2\u00aa\u0435\3\2\2\2\u00ac\u0449\3\2\2\2\u00ae"+
		"\u044b\3\2\2\2\u00b0\u044d\3\2\2\2\u00b2\u0450\3\2\2\2\u00b4\u0454\3\2"+
		"\2\2\u00b6\u0457\3\2\2\2\u00b8\u045c\3\2\2\2\u00ba\u045e\3\2\2\2\u00bc"+
		"\u046c\3\2\2\2\u00be\u0474\3\2\2\2\u00c0\u047c\3\2\2\2\u00c2\u0484\3\2"+
		"\2\2\u00c4\u0492\3\2\2\2\u00c6\u0498\3\2\2\2\u00c8\u04a6\3\2\2\2\u00ca"+
		"\u04b2\3\2\2\2\u00cc\u04bb\3\2\2\2\u00ce\u04bd\3\2\2\2\u00d0\u04bf\3\2"+
		"\2\2\u00d2\u04c3\3\2\2\2\u00d4\u04c6\3\2\2\2\u00d6\u04ca\3\2\2\2\u00d8"+
		"\u04cc\3\2\2\2\u00da\u04d1\3\2\2\2\u00dc\u04d5\3\2\2\2\u00de\u04d9\3\2"+
		"\2\2\u00e0\u04dd\3\2\2\2\u00e2\u04e0\3\2\2\2\u00e4\u04e2\3\2\2\2\u00e6"+
		"\u04e5\3\2\2\2\u00e8\u04fa\3\2\2\2\u00ea\u04fc\3\2\2\2\u00ec\u0512\3\2"+
		"\2\2\u00ee\u051a\3\2\2\2\u00f0\u051c\3\2\2\2\u00f2\u0532\3\2\2\2\u00f4"+
		"\u053a\3\2\2\2\u00f6\u0542\3\2\2\2\u00f8\u0546\3\2\2\2\u00fa\u0552\3\2"+
		"\2\2\u00fc\u055c\3\2\2\2\u00fe\u0567\3\2\2\2\u0100\u056f\3\2\2\2\u0102"+
		"\u0573\3\2\2\2\u0104\u057c\3\2\2\2\u0106\u058c\3\2\2\2\u0108\u0591\3\2"+
		"\2\2\u010a\u0596\3\2\2\2\u010c\u0598\3\2\2\2\u010e\u059d\3\2\2\2\u0110"+
		"\u059f\3\2\2\2\u0112\u05a1\3\2\2\2\u0114\u05a4\3\2\2\2\u0116\u05a8\3\2"+
		"\2\2\u0118\u05b3\3\2\2\2\u011a\u05b7\3\2\2\2\u011c\u05be\3\2\2\2\u011e"+
		"\u05c2\3\2\2\2\u0120\u05c4\3\2\2\2\u0122\u05d4\3\2\2\2\u0124\u05e1\3\2"+
		"\2\2\u0126\u05e9\3\2\2\2\u0128\u05ee\3\2\2\2\u012a\u05f0\3\2\2\2\u012c"+
		"\u05f2\3\2\2\2\u012e\u05f4\3\2\2\2\u0130\u05f8\3\2\2\2\u0132\u05fb\3\2"+
		"\2\2\u0134\u0604\3\2\2\2\u0136\u060f\3\2\2\2\u0138\u0615\3\2\2\2\u013a"+
		"\u0619\3\2\2\2\u013c\u061b\3\2\2\2\u013e\u062b\3\2\2\2\u0140\u0630\3\2"+
		"\2\2\u0142\u0633\3\2\2\2\u0144\u0637\3\2\2\2\u0146\u063b\3\2\2\2\u0148"+
		"\u063f\3\2\2\2\u014a\u0644\3\2\2\2\u014c\u0657\3\2\2\2\u014e\u065b\3\2"+
		"\2\2\u0150\u0661\3\2\2\2\u0152\u0153\5\u0086D\2\u0153\u0154\7\2\2\3\u0154"+
		"\3\3\2\2\2\u0155\u0156\5\u0088E\2\u0156\u0157\7\2\2\3\u0157\5\3\2\2\2"+
		"\u0158\u0159\5\u009eP\2\u0159\u015a\7\2\2\3\u015a\7\3\2\2\2\u015b\u0160"+
		"\5\n\6\2\u015c\u015d\7e\2\2\u015d\u015f\5\n\6\2\u015e\u015c\3\2\2\2\u015f"+
		"\u0162\3\2\2\2\u0160\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u0161\t\3\2\2\2"+
		"\u0162\u0160\3\2\2\2\u0163\u0165\7]\2\2\u0164\u0166\7:\2\2\u0165\u0164"+
		"\3\2\2\2\u0165\u0166\3\2\2\2\u0166\13\3\2\2\2\u0167\u0169\5R*\2\u0168"+
		"\u0167\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\5\u00b4"+
		"[\2\u016b\u0171\5\u0150\u00a9\2\u016c\u016d\5\20\t\2\u016d\u016e\5\u0150"+
		"\u00a9\2\u016e\u0170\3\2\2\2\u016f\u016c\3\2\2\2\u0170\u0173\3\2\2\2\u0171"+
		"\u016f\3\2\2\2\u0171\u0172\3\2\2\2\u0172\u017d\3\2\2\2\u0173\u0171\3\2"+
		"\2\2\u0174\u0178\5j\66\2\u0175\u0178\5\u00b8]\2\u0176\u0178\5\22\n\2\u0177"+
		"\u0174\3\2\2\2\u0177\u0175\3\2\2\2\u0177\u0176\3\2\2\2\u0178\u0179\3\2"+
		"\2\2\u0179\u017a\5\u0150\u00a9\2\u017a\u017c\3\2\2\2\u017b\u0177\3\2\2"+
		"\2\u017c\u017f\3\2\2\2\u017d\u017b\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u0180"+
		"\3\2\2\2\u017f\u017d\3\2\2\2\u0180\u0181\7\2\2\3\u0181\r\3\2\2\2\u0182"+
		"\u0184\5R*\2\u0183\u0185\t\2\2\2\u0184\u0183\3\2\2\2\u0184\u0185\3\2\2"+
		"\2\u0185\u0186\3\2\2\2\u0186\u0187\5\u00b6\\\2\u0187\17\3\2\2\2\u0188"+
		"\u018a\5R*\2\u0189\u018b\5\u0150\u00a9\2\u018a\u0189\3\2\2\2\u018a\u018b"+
		"\3\2\2\2\u018b\u018d\3\2\2\2\u018c\u0188\3\2\2\2\u018c\u018d\3\2\2\2\u018d"+
		"\u018e\3\2\2\2\u018e\u018f\7Y\2\2\u018f\u019c\5\16\b\2\u0190\u0191\7Y"+
		"\2\2\u0191\u0197\7^\2\2\u0192\u0193\5\16\b\2\u0193\u0194\5\u0150\u00a9"+
		"\2\u0194\u0196\3\2\2\2\u0195\u0192\3\2\2\2\u0196\u0199\3\2\2\2\u0197\u0195"+
		"\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u019a\3\2\2\2\u0199\u0197\3\2\2\2\u019a"+
		"\u019c\7_\2\2\u019b\u018c\3\2\2\2\u019b\u0190\3\2\2\2\u019c\21\3\2\2\2"+
		"\u019d\u01a2\5\\/\2\u019e\u01a2\5r:\2\u019f\u01a2\5v<\2\u01a0\u01a2\5"+
		"p9\2\u01a1\u019d\3\2\2\2\u01a1\u019e\3\2\2\2\u01a1\u019f\3\2\2\2\u01a1"+
		"\u01a0\3\2\2\2\u01a2\23\3\2\2\2\u01a3\u01a4\7\33\2\2\u01a4\u01aa\5\u0088"+
		"E\2\u01a5\u01a6\t\3\2\2\u01a6\u01aa\5*\26\2\u01a7\u01a8\t\4\2\2\u01a8"+
		"\u01aa\5\u0086D\2\u01a9\u01a3\3\2\2\2\u01a9\u01a5\3\2\2\2\u01a9\u01a7"+
		"\3\2\2\2\u01aa\25\3\2\2\2\u01ab\u01ac\5\30\r\2\u01ac\27\3\2\2\2\u01ad"+
		"\u01ae\5R*\2\u01ae\u01af\5\32\16\2\u01af\31\3\2\2\2\u01b0\u01b1\7B\2\2"+
		"\u01b1\u01b3\7^\2\2\u01b2\u01b4\5\u00caf\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4"+
		"\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b6\7_\2\2\u01b6\33\3\2\2\2\u01b7"+
		"\u01c4\5B\"\2\u01b8\u01c4\5@!\2\u01b9\u01c4\5> \2\u01ba\u01c4\5 \21\2"+
		"\u01bb\u01c4\5<\37\2\u01bc\u01c4\5\64\33\2\u01bd\u01c4\5:\36\2\u01be\u01c4"+
		"\5\62\32\2\u01bf\u01c4\5.\30\2\u01c0\u01c4\5,\27\2\u01c1\u01c4\5\60\31"+
		"\2\u01c2\u01c4\5\36\20\2\u01c3\u01b7\3\2\2\2\u01c3\u01b8\3\2\2\2\u01c3"+
		"\u01b9\3\2\2\2\u01c3\u01ba\3\2\2\2\u01c3\u01bb\3\2\2\2\u01c3\u01bc\3\2"+
		"\2\2\u01c3\u01bd\3\2\2\2\u01c3\u01be\3\2\2\2\u01c3\u01bf\3\2\2\2\u01c3"+
		"\u01c0\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c2\3\2\2\2\u01c4\35\3\2\2"+
		"\2\u01c5\u01c6\t\5\2\2\u01c6\37\3\2\2\2\u01c7\u01c8\7V\2\2\u01c8\u01c9"+
		"\7b\2\2\u01c9\u01ca\5\u009eP\2\u01ca\u01cb\7c\2\2\u01cb!\3\2\2\2\u01cc"+
		"\u01d1\5$\23\2\u01cd\u01ce\7e\2\2\u01ce\u01d0\5$\23\2\u01cf\u01cd\3\2"+
		"\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2"+
		"\u01d5\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d4\u01d6\7e\2\2\u01d5\u01d4\3\2"+
		"\2\2\u01d5\u01d6\3\2\2\2\u01d6#\3\2\2\2\u01d7\u01dc\7]\2\2\u01d8\u01d9"+
		"\7e\2\2\u01d9\u01db\7]\2\2\u01da\u01d8\3\2\2\2\u01db\u01de\3\2\2\2\u01dc"+
		"\u01da\3\2\2\2\u01dc\u01dd\3\2\2\2\u01dd\u01df\3\2\2\2\u01de\u01dc\3\2"+
		"\2\2\u01df\u01e0\5\u0110\u0089\2\u01e0%\3\2\2\2\u01e1\u01e3\5(\25\2\u01e2"+
		"\u01e1\3\2\2\2\u01e3\u01e6\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e4\u01e5\3\2"+
		"\2\2\u01e5\'\3\2\2\2\u01e6\u01e4\3\2\2\2\u01e7\u01e8\7`\2\2\u01e8\u01ed"+
		"\5\u0086D\2\u01e9\u01ea\7e\2\2\u01ea\u01ec\5\u0086D\2\u01eb\u01e9\3\2"+
		"\2\2\u01ec\u01ef\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee"+
		"\u01f0\3\2\2\2\u01ef\u01ed\3\2\2\2\u01f0\u01f1\7a\2\2\u01f1)\3\2\2\2\u01f2"+
		"\u01f3\5\u0094K\2\u01f3+\3\2\2\2\u01f4\u01f5\7\61\2\2\u01f5\u01f6\7^\2"+
		"\2\u01f6\u01f7\5\u0086D\2\u01f7\u01f8\7_\2\2\u01f8-\3\2\2\2\u01f9\u01fa"+
		"\7\65\2\2\u01fa\u01fb\7b\2\2\u01fb\u01fc\5\u009eP\2\u01fc\u01fd\7c\2\2"+
		"\u01fd/\3\2\2\2\u01fe\u01ff\7\62\2\2\u01ff\u0200\7^\2\2\u0200\u0201\5"+
		"\u0086D\2\u0201\u0202\7_\2\2\u0202\61\3\2\2\2\u0203\u0204\t\6\2\2\u0204"+
		"\u0205\7^\2\2\u0205\u0206\5\u0086D\2\u0206\u0207\7_\2\2\u0207\63\3\2\2"+
		"\2\u0208\u020d\7\22\2\2\u0209\u020a\7b\2\2\u020a\u020b\5\66\34\2\u020b"+
		"\u020c\7c\2\2\u020c\u020e\3\2\2\2\u020d\u0209\3\2\2\2\u020d\u020e\3\2"+
		"\2\2\u020e\u020f\3\2\2\2\u020f\u0210\7^\2\2\u0210\u0211\5\u0086D\2\u0211"+
		"\u0212\7_\2\2\u0212\65\3\2\2\2\u0213\u0216\58\35\2\u0214\u0216\7\24\2"+
		"\2\u0215\u0213\3\2\2\2\u0215\u0214\3\2\2\2\u0216\67\3\2\2\2\u0217\u0218"+
		"\7]\2\2\u02189\3\2\2\2\u0219\u021a\7\23\2\2\u021a\u021b\7^\2\2\u021b\u021c"+
		"\5\u0086D\2\u021c\u021d\7_\2\2\u021d;\3\2\2\2\u021e\u021f\78\2\2\u021f"+
		"\u0220\7^\2\2\u0220\u0221\5\u0086D\2\u0221\u0222\7_\2\2\u0222=\3\2\2\2"+
		"\u0223\u0224\7\67\2\2\u0224\u0225\7^\2\2\u0225\u0226\5\u0086D\2\u0226"+
		"\u0227\7_\2\2\u0227?\3\2\2\2\u0228\u0229\7\27\2\2\u0229\u022a\7^\2\2\u022a"+
		"\u022d\5\u0086D\2\u022b\u022c\7e\2\2\u022c\u022e\5\u0086D\2\u022d\u022b"+
		"\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u0230\7_\2\2\u0230"+
		"A\3\2\2\2\u0231\u0232\t\6\2\2\u0232\u0233\7b\2\2\u0233\u0234\5\u0086D"+
		"\2\u0234\u0235\7;\2\2\u0235\u0236\5\u0086D\2\u0236\u0237\7c\2\2\u0237"+
		"C\3\2\2\2\u0238\u0239\7b\2\2\u0239\u023e\5F$\2\u023a\u023b\7e\2\2\u023b"+
		"\u023d\5F$\2\u023c\u023a\3\2\2\2\u023d\u0240\3\2\2\2\u023e\u023c\3\2\2"+
		"\2\u023e\u023f\3\2\2\2\u023f\u0241\3\2\2\2\u0240\u023e\3\2\2\2\u0241\u0242"+
		"\7c\2\2\u0242E\3\2\2\2\u0243\u0244\5\u0086D\2\u0244\u0245\7d\2\2\u0245"+
		"\u0246\5\u0086D\2\u0246G\3\2\2\2\u0247\u024b\5P)\2\u0248\u024b\5N(\2\u0249"+
		"\u024b\5J&\2\u024a\u0247\3\2\2\2\u024a\u0248\3\2\2\2\u024a\u0249\3\2\2"+
		"\2\u024bI\3\2\2\2\u024c\u024d\7\63\2\2\u024d\u0253\7`\2\2\u024e\u024f"+
		"\5L\'\2\u024f\u0250\5\u0150\u00a9\2\u0250\u0252\3\2\2\2\u0251\u024e\3"+
		"\2\2\2\u0252\u0255\3\2\2\2\u0253\u0251\3\2\2\2\u0253\u0254\3\2\2\2\u0254"+
		"\u0256\3\2\2\2\u0255\u0253\3\2\2\2\u0256\u0257\7a\2\2\u0257K\3\2\2\2\u0258"+
		"\u0259\7E\2\2\u0259\u025a\7]\2\2\u025a\u0262\5\u011c\u008f\2\u025b\u025c"+
		"\7\64\2\2\u025c\u025d\7`\2\2\u025d\u025e\5\u0086D\2\u025e\u025f\5\u0150"+
		"\u00a9\2\u025f\u0260\7a\2\2\u0260\u0262\3\2\2\2\u0261\u0258\3\2\2\2\u0261"+
		"\u025b\3\2\2\2\u0262M\3\2\2\2\u0263\u0264\7\33\2\2\u0264\u0265\7b\2\2"+
		"\u0265\u0266\7c\2\2\u0266\u0267\5\u0110\u0089\2\u0267O\3\2\2\2\u0268\u0269"+
		"\t\7\2\2\u0269\u026a\7b\2\2\u026a\u026b\5\u009eP\2\u026b\u026c\7c\2\2"+
		"\u026c\u0274\3\2\2\2\u026d\u026e\7+\2\2\u026e\u026f\7b\2\2\u026f\u0270"+
		"\5\u009eP\2\u0270\u0271\7c\2\2\u0271\u0272\5\u009eP\2\u0272\u0274\3\2"+
		"\2\2\u0273\u0268\3\2\2\2\u0273\u026d\3\2\2\2\u0274Q\3\2\2\2\u0275\u027b"+
		"\5T+\2\u0276\u0277\7\20\2\2\u0277\u027b\b*\1\2\u0278\u0279\7A\2\2\u0279"+
		"\u027b\b*\1\2\u027a\u0275\3\2\2\2\u027a\u0276\3\2\2\2\u027a\u0278\3\2"+
		"\2\2\u027b\u027c\3\2\2\2\u027c\u027e\5\u0150\u00a9\2\u027d\u027a\3\2\2"+
		"\2\u027e\u0281\3\2\2\2\u027f\u0280\3\2\2\2\u027f\u027d\3\2\2\2\u0280\u0284"+
		"\3\2\2\2\u0281\u027f\3\2\2\2\u0282\u0283\7\20\2\2\u0283\u0285\b*\1\2\u0284"+
		"\u0282\3\2\2\2\u0284\u0285\3\2\2\2\u0285S\3\2\2\2\u0286\u0287\7\13\2\2"+
		"\u0287\u028f\5X-\2\u0288\u0289\7\f\2\2\u0289\u028f\5X-\2\u028a\u028b\7"+
		"\r\2\2\u028b\u028f\5X-\2\u028c\u028d\7\17\2\2\u028d\u028f\5V,\2\u028e"+
		"\u0286\3\2\2\2\u028e\u0288\3\2\2\2\u028e\u028a\3\2\2\2\u028e\u028c\3\2"+
		"\2\2\u028fU\3\2\2\2\u0290\u0292\5\u00c0a\2\u0291\u0290\3\2\2\2\u0291\u0292"+
		"\3\2\2\2\u0292\u0295\3\2\2\2\u0293\u0294\7T\2\2\u0294\u0296\5\u0086D\2"+
		"\u0295\u0293\3\2\2\2\u0295\u0296\3\2\2\2\u0296W\3\2\2\2\u0297\u029a\3"+
		"\2\2\2\u0298\u029a\5\u0086D\2\u0299\u0297\3\2\2\2\u0299\u0298\3\2\2\2"+
		"\u029aY\3\2\2\2\u029b\u02a0\7`\2\2\u029c\u029d\79\2\2\u029d\u029e\5\u00be"+
		"`\2\u029e\u029f\5\u0150\u00a9\2\u029f\u02a1\3\2\2\2\u02a0\u029c\3\2\2"+
		"\2\u02a0\u02a1\3\2\2\2\u02a1\u02a3\3\2\2\2\u02a2\u02a4\5\u00caf\2\u02a3"+
		"\u02a2\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a5\3\2\2\2\u02a5\u02a6\7a"+
		"\2\2\u02a6[\3\2\2\2\u02a7\u02a8\5\u009eP\2\u02a8\u02a9\7\21\2\2\u02a9"+
		"\u02bc\5\u009eP\2\u02aa\u02b0\7`\2\2\u02ab\u02ac\5d\63\2\u02ac\u02ad\5"+
		"\u0150\u00a9\2\u02ad\u02af\3\2\2\2\u02ae\u02ab\3\2\2\2\u02af\u02b2\3\2"+
		"\2\2\u02b0\u02ae\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1\u02b8\3\2\2\2\u02b2"+
		"\u02b0\3\2\2\2\u02b3\u02b4\5^\60\2\u02b4\u02b5\5\u0150\u00a9\2\u02b5\u02b7"+
		"\3\2\2\2\u02b6\u02b3\3\2\2\2\u02b7\u02ba\3\2\2\2\u02b8\u02b6\3\2\2\2\u02b8"+
		"\u02b9\3\2\2\2\u02b9\u02bb\3\2\2\2\u02ba\u02b8\3\2\2\2\u02bb\u02bd\7a"+
		"\2\2\u02bc\u02aa\3\2\2\2\u02bc\u02bd\3\2\2\2\u02bd]\3\2\2\2\u02be\u02c0"+
		"\7\20\2\2\u02bf\u02be\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0\u02c1\3\2\2\2"+
		"\u02c1\u02c2\5`\61\2\u02c2\u02c3\7]\2\2\u02c3\u02c5\5\u011c\u008f\2\u02c4"+
		"\u02c6\5\u00c8e\2\u02c5\u02c4\3\2\2\2\u02c5\u02c6\3\2\2\2\u02c6_\3\2\2"+
		"\2\u02c7\u02c9\7^\2\2\u02c8\u02ca\7]\2\2\u02c9\u02c8\3\2\2\2\u02c9\u02ca"+
		"\3\2\2\2\u02ca\u02cc\3\2\2\2\u02cb\u02cd\7\177\2\2\u02cc\u02cb\3\2\2\2"+
		"\u02cc\u02cd\3\2\2\2\u02cd\u02ce\3\2\2\2\u02ce\u02cf\5\u010a\u0086\2\u02cf"+
		"\u02d0\7_\2\2\u02d0a\3\2\2\2\u02d1\u02d7\5\u0094K\2\u02d2\u02d3\5\u009e"+
		"P\2\u02d3\u02d4\7h\2\2\u02d4\u02d5\7]\2\2\u02d5\u02d7\3\2\2\2\u02d6\u02d1"+
		"\3\2\2\2\u02d6\u02d2\3\2\2\2\u02d7c\3\2\2\2\u02d8\u02d9\7\66\2\2\u02d9"+
		"\u02da\7]\2\2\u02da\u02dd\7k\2\2\u02db\u02de\5b\62\2\u02dc\u02de\5\u012c"+
		"\u0097\2\u02dd\u02db\3\2\2\2\u02dd\u02dc\3\2\2\2\u02dee\3\2\2\2\u02df"+
		"\u02e0\7/\2\2\u02e0\u02e1\7^\2\2\u02e1\u02e4\5\u009eP\2\u02e2\u02e3\7"+
		"e\2\2\u02e3\u02e5\5\u00c0a\2\u02e4\u02e2\3\2\2\2\u02e4\u02e5\3\2\2\2\u02e5"+
		"\u02e6\3\2\2\2\u02e6\u02e7\7_\2\2\u02e7g\3\2\2\2\u02e8\u02e9\7.\2\2\u02e9"+
		"\u02ea\7^\2\2\u02ea\u02eb\5\u009eP\2\u02eb\u02ec\7_\2\2\u02eci\3\2\2\2"+
		"\u02ed\u02f0\5R*\2\u02ee\u02f1\5l\67\2\u02ef\u02f1\5n8\2\u02f0\u02ee\3"+
		"\2\2\2\u02f0\u02ef\3\2\2\2\u02f1k\3\2\2\2\u02f2\u02f3\7E\2\2\u02f3\u02f4"+
		"\7]\2\2\u02f4\u02f6\5\u011c\u008f\2\u02f5\u02f7\5Z.\2\u02f6\u02f5\3\2"+
		"\2\2\u02f6\u02f7\3\2\2\2\u02f7m\3\2\2\2\u02f8\u02f9\7E\2\2\u02f9\u02fa"+
		"\5|?\2\u02fa\u02fb\7]\2\2\u02fb\u02fd\5\u011c\u008f\2\u02fc\u02fe\5Z."+
		"\2\u02fd\u02fc\3\2\2\2\u02fd\u02fe\3\2\2\2\u02feo\3\2\2\2\u02ff\u0302"+
		"\7\33\2\2\u0300\u0303\5j\66\2\u0301\u0303\5\u00b8]\2\u0302\u0300\3\2\2"+
		"\2\u0302\u0301\3\2\2\2\u0303q\3\2\2\2\u0304\u0305\7\66\2\2\u0305\u0306"+
		"\7]\2\2\u0306\u0308\5\u0120\u0091\2\u0307\u0309\5t;\2\u0308\u0307\3\2"+
		"\2\2\u0308\u0309\3\2\2\2\u0309s\3\2\2\2\u030a\u030b\7`\2\2\u030b\u030c"+
		"\5\u0086D\2\u030c\u030d\5\u0150\u00a9\2\u030d\u030e\7a\2\2\u030eu\3\2"+
		"\2\2\u030f\u0310\7\66\2\2\u0310\u0311\5|?\2\u0311\u0312\7]\2\2\u0312\u0314"+
		"\5\u0120\u0091\2\u0313\u0315\5t;\2\u0314\u0313\3\2\2\2\u0314\u0315\3\2"+
		"\2\2\u0315w\3\2\2\2\u0316\u031e\5\b\5\2\u0317\u031a\5\u009eP\2\u0318\u0319"+
		"\7d\2\2\u0319\u031b\5\u00c0a\2\u031a\u0318\3\2\2\2\u031a\u031b\3\2\2\2"+
		"\u031b\u031f\3\2\2\2\u031c\u031d\7d\2\2\u031d\u031f\5\u00c0a\2\u031e\u0317"+
		"\3\2\2\2\u031e\u031c\3\2\2\2\u031fy\3\2\2\2\u0320\u0321\5\b\5\2\u0321"+
		"\u0322\7k\2\2\u0322\u0323\5\u00c0a\2\u0323{\3\2\2\2\u0324\u0326\7^\2\2"+
		"\u0325\u0327\5\n\6\2\u0326\u0325\3\2\2\2\u0326\u0327\3\2\2\2\u0327\u0328"+
		"\3\2\2\2\u0328\u032a\5\u009eP\2\u0329\u032b\7e\2\2\u032a\u0329\3\2\2\2"+
		"\u032a\u032b\3\2\2\2\u032b\u032c\3\2\2\2\u032c\u032d\7_\2\2\u032d}\3\2"+
		"\2\2\u032e\u0331\5\u0080A\2\u032f\u0331\5\u0082B\2\u0330\u032e\3\2\2\2"+
		"\u0330\u032f\3\2\2\2\u0331\177\3\2\2\2\u0332\u0334\5\u00be`\2\u0333\u0332"+
		"\3\2\2\2\u0333\u0334\3\2\2\2\u0334\u0335\3\2\2\2\u0335\u0336\5\u0084C"+
		"\2\u0336\u0081\3\2\2\2\u0337\u0339\7\33\2\2\u0338\u033a\5\u00be`\2\u0339"+
		"\u0338\3\2\2\2\u0339\u033a\3\2\2\2\u033a\u033b\3\2\2\2\u033b\u033c\5\u0084"+
		"C\2\u033c\u0083\3\2\2\2\u033d\u033f\7l\2\2\u033e\u033d\3\2\2\2\u033e\u033f"+
		"\3\2\2\2\u033f\u0340\3\2\2\2\u0340\u0341\5\u009eP\2\u0341\u0085\3\2\2"+
		"\2\u0342\u0343\bD\1\2\u0343\u0344\t\b\2\2\u0344\u0353\5\u0086D\17\u0345"+
		"\u0353\5\u0094K\2\u0346\u0347\7\32\2\2\u0347\u0348\5*\26\2\u0348\u0349"+
		"\7\34\2\2\u0349\u034a\5\u0086D\4\u034a\u0353\3\2\2\2\u034b\u034c\t\t\2"+
		"\2\u034c\u034d\5\"\22\2\u034d\u034e\7g\2\2\u034e\u034f\7g\2\2\u034f\u0350"+
		"\5&\24\2\u0350\u0351\5\u0086D\3\u0351\u0353\3\2\2\2\u0352\u0342\3\2\2"+
		"\2\u0352\u0345\3\2\2\2\u0352\u0346\3\2\2\2\u0352\u034b\3\2\2\2\u0353\u0374"+
		"\3\2\2\2\u0354\u0355\f\r\2\2\u0355\u0356\t\n\2\2\u0356\u0373\5\u0086D"+
		"\16\u0357\u0358\f\f\2\2\u0358\u0359\t\13\2\2\u0359\u0373\5\u0086D\r\u035a"+
		"\u035b\f\13\2\2\u035b\u035c\t\f\2\2\u035c\u0373\5\u0086D\f\u035d\u035e"+
		"\f\n\2\2\u035e\u035f\t\r\2\2\u035f\u0373\5\u0086D\13\u0360\u0361\f\t\2"+
		"\2\u0361\u0362\t\16\2\2\u0362\u0373\5\u0086D\n\u0363\u0364\f\b\2\2\u0364"+
		"\u0365\7n\2\2\u0365\u0373\5\u0086D\t\u0366\u0367\f\7\2\2\u0367\u0368\7"+
		"m\2\2\u0368\u0373\5\u0086D\b\u0369\u036a\f\6\2\2\u036a\u036b\7\"\2\2\u036b"+
		"\u0373\5\u0086D\6\u036c\u036d\f\5\2\2\u036d\u036e\7%\2\2\u036e\u036f\5"+
		"\u0086D\2\u036f\u0370\7g\2\2\u0370\u0371\5\u0086D\5\u0371\u0373\3\2\2"+
		"\2\u0372\u0354\3\2\2\2\u0372\u0357\3\2\2\2\u0372\u035a\3\2\2\2\u0372\u035d"+
		"\3\2\2\2\u0372\u0360\3\2\2\2\u0372\u0363\3\2\2\2\u0372\u0366\3\2\2\2\u0372"+
		"\u0369\3\2\2\2\u0372\u036c\3\2\2\2\u0373\u0376\3\2\2\2\u0374\u0372\3\2"+
		"\2\2\u0374\u0375\3\2\2\2\u0375\u0087\3\2\2\2\u0376\u0374\3\2\2\2\u0377"+
		"\u038b\5\24\13\2\u0378\u038b\5\26\f\2\u0379\u038b\5\u008cG\2\u037a\u038b"+
		"\5\u008aF\2\u037b\u038b\5\u00b8]\2\u037c\u038b\5\u00d8m\2\u037d\u038b"+
		"\5\u00ccg\2\u037e\u038b\5\u0108\u0085\2\u037f\u038b\5\u00dan\2\u0380\u038b"+
		"\5\u00dco\2\u0381\u038b\5\u00dep\2\u0382\u038b\5\u00e0q\2\u0383\u038b"+
		"\5\u00e2r\2\u0384\u038b\5\u00c8e\2\u0385\u038b\5\u00e6t\2\u0386\u038b"+
		"\5\u00e8u\2\u0387\u038b\5\u00fa~\2\u0388\u038b\5\u008eH\2\u0389\u038b"+
		"\5\u00e4s\2\u038a\u0377\3\2\2\2\u038a\u0378\3\2\2\2\u038a\u0379\3\2\2"+
		"\2\u038a\u037a\3\2\2\2\u038a\u037b\3\2\2\2\u038a\u037c\3\2\2\2\u038a\u037d"+
		"\3\2\2\2\u038a\u037e\3\2\2\2\u038a\u037f\3\2\2\2\u038a\u0380\3\2\2\2\u038a"+
		"\u0381\3\2\2\2\u038a\u0382\3\2\2\2\u038a\u0383\3\2\2\2\u038a\u0384\3\2"+
		"\2\2\u038a\u0385\3\2\2\2\u038a\u0386\3\2\2\2\u038a\u0387\3\2\2\2\u038a"+
		"\u0388\3\2\2\2\u038a\u0389\3\2\2\2\u038b\u0089\3\2\2\2\u038c\u038d\7$"+
		"\2\2\u038d\u038e\5\u0086D\2\u038e\u008b\3\2\2\2\u038f\u0390\7P\2\2\u0390"+
		"\u0392\5\u0086D\2\u0391\u0393\5\u00c8e\2\u0392\u0391\3\2\2\2\u0392\u0393"+
		"\3\2\2\2\u0393\u008d\3\2\2\2\u0394\u0395\5\u0090I\2\u0395\u0396\5\u0102"+
		"\u0082\2\u0396\u008f\3\2\2\2\u0397\u0398\7\16\2\2\u0398\u0399\5\u0086"+
		"D\2\u0399\u039a\5\u0150\u00a9\2\u039a\u039c\3\2\2\2\u039b\u0397\3\2\2"+
		"\2\u039c\u039f\3\2\2\2\u039d\u039b\3\2\2\2\u039d\u039e\3\2\2\2\u039e\u03a4"+
		"\3\2\2\2\u039f\u039d\3\2\2\2\u03a0\u03a1\7\17\2\2\u03a1\u03a2\5V,\2\u03a2"+
		"\u03a3\5\u0150\u00a9\2\u03a3\u03a5\3\2\2\2\u03a4\u03a0\3\2\2\2\u03a4\u03a5"+
		"\3\2\2\2\u03a5\u0091\3\2\2\2\u03a6\u03af\7\5\2\2\u03a7\u03af\7\6\2\2\u03a8"+
		"\u03af\7\\\2\2\u03a9\u03af\5\u012a\u0096\2\u03aa\u03af\5\u0140\u00a1\2"+
		"\u03ab\u03af\7\3\2\2\u03ac\u03af\7\u0087\2\2\u03ad\u03af\7\u0088\2\2\u03ae"+
		"\u03a6\3\2\2\2\u03ae\u03a7\3\2\2\2\u03ae\u03a8\3\2\2\2\u03ae\u03a9\3\2"+
		"\2\2\u03ae\u03aa\3\2\2\2\u03ae\u03ab\3\2\2\2\u03ae\u03ac\3\2\2\2\u03ae"+
		"\u03ad\3\2\2\2\u03af\u0093\3\2\2\2\u03b0\u03b1\bK\1\2\u03b1\u03bd\5\u0126"+
		"\u0094\2\u03b2\u03bd\5\u0122\u0092\2\u03b3\u03bd\5\u014c\u00a7\2\u03b4"+
		"\u03bd\5\34\17\2\u03b5\u03bd\5h\65\2\u03b6\u03bd\5f\64\2\u03b7\u03b8\t"+
		"\17\2\2\u03b8\u03b9\7^\2\2\u03b9\u03ba\5\u0086D\2\u03ba\u03bb\7_\2\2\u03bb"+
		"\u03bd\3\2\2\2\u03bc\u03b0\3\2\2\2\u03bc\u03b2\3\2\2\2\u03bc\u03b3\3\2"+
		"\2\2\u03bc\u03b4\3\2\2\2\u03bc\u03b5\3\2\2\2\u03bc\u03b6\3\2\2\2\u03bc"+
		"\u03b7\3\2\2\2\u03bd\u03cf\3\2\2\2\u03be\u03bf\f\n\2\2\u03bf\u03c0\7h"+
		"\2\2\u03c0\u03ce\7]\2\2\u03c1\u03c2\f\t\2\2\u03c2\u03ce\5\u0146\u00a4"+
		"\2\u03c3\u03c4\f\b\2\2\u03c4\u03ce\5\u00aaV\2\u03c5\u03c6\f\7\2\2\u03c6"+
		"\u03ce\5D#\2\u03c7\u03c8\f\6\2\2\u03c8\u03ce\5\u0148\u00a5\2\u03c9\u03ca"+
		"\f\5\2\2\u03ca\u03ce\5\u014a\u00a6\2\u03cb\u03cc\f\4\2\2\u03cc\u03ce\5"+
		"\u0096L\2\u03cd\u03be\3\2\2\2\u03cd\u03c1\3\2\2\2\u03cd\u03c3\3\2\2\2"+
		"\u03cd\u03c5\3\2\2\2\u03cd\u03c7\3\2\2\2\u03cd\u03c9\3\2\2\2\u03cd\u03cb"+
		"\3\2\2\2\u03ce\u03d1\3\2\2\2\u03cf\u03cd\3\2\2\2\u03cf\u03d0\3\2\2\2\u03d0"+
		"\u0095\3\2\2\2\u03d1\u03cf\3\2\2\2\u03d2\u03d4\7&\2\2\u03d3\u03d5\5\u00c0"+
		"a\2\u03d4\u03d3\3\2\2\2\u03d4\u03d5\3\2\2\2\u03d5\u03d7\3\2\2\2\u03d6"+
		"\u03d8\7e\2\2\u03d7\u03d6\3\2\2\2\u03d7\u03d8\3\2\2\2\u03d8\u03d9\3\2"+
		"\2\2\u03d9\u03da\7\'\2\2\u03da\u0097\3\2\2\2\u03db\u03dc\7F\2\2\u03dc"+
		"\u03e6\7`\2\2\u03dd\u03e1\5\u009cO\2\u03de\u03e1\5\u010a\u0086\2\u03df"+
		"\u03e1\5\u009aN\2\u03e0\u03dd\3\2\2\2\u03e0\u03de\3\2\2\2\u03e0\u03df"+
		"\3\2\2\2\u03e1\u03e2\3\2\2\2\u03e2\u03e3\5\u0150\u00a9\2\u03e3\u03e5\3"+
		"\2\2\2\u03e4\u03e0\3\2\2\2\u03e5\u03e8\3\2\2\2\u03e6\u03e4\3\2\2\2\u03e6"+
		"\u03e7\3\2\2\2\u03e7\u03e9\3\2\2\2\u03e8\u03e6\3\2\2\2\u03e9\u03ea\7a"+
		"\2\2\u03ea\u0099\3\2\2\2\u03eb\u03ec\7\66\2\2\u03ec\u03ed\7]\2\2\u03ed"+
		"\u03ee\5\u0120\u0091\2\u03ee\u009b\3\2\2\2\u03ef\u03f1\7\33\2\2\u03f0"+
		"\u03ef\3\2\2\2\u03f0\u03f1\3\2\2\2\u03f1\u03f2\3\2\2\2\u03f2\u03f3\5R"+
		"*\2\u03f3\u03f4\7]\2\2\u03f4\u03f5\5\u0120\u0091\2\u03f5\u03f6\5\u011e"+
		"\u0090\2\u03f6\u03ff\3\2\2\2\u03f7\u03f9\7\33\2\2\u03f8\u03f7\3\2\2\2"+
		"\u03f8\u03f9\3\2\2\2\u03f9\u03fa\3\2\2\2\u03fa\u03fb\5R*\2\u03fb\u03fc"+
		"\7]\2\2\u03fc\u03fd\5\u0120\u0091\2\u03fd\u03ff\3\2\2\2\u03fe\u03f0\3"+
		"\2\2\2\u03fe\u03f8\3\2\2\2\u03ff\u009d\3\2\2\2\u0400\u0408\5\u010a\u0086"+
		"\2\u0401\u0408\5\u00a0Q\2\u0402\u0408\5H%\2\u0403\u0404\7^\2\2\u0404\u0405"+
		"\5\u009eP\2\u0405\u0406\7_\2\2\u0406\u0408\3\2\2\2\u0407\u0400\3\2\2\2"+
		"\u0407\u0401\3\2\2\2\u0407\u0402\3\2\2\2\u0407\u0403\3\2\2\2\u0408\u009f"+
		"\3\2\2\2\u0409\u0413\5\u010c\u0087\2\u040a\u0413\5\u013c\u009f\2\u040b"+
		"\u0413\5\u0112\u008a\2\u040c\u0413\5\u011a\u008e\2\u040d\u0413\5\u0098"+
		"M\2\u040e\u0413\5\u0114\u008b\2\u040f\u0413\5\u0116\u008c\2\u0410\u0413"+
		"\5\u0118\u008d\2\u0411\u0413\5\u00a2R\2\u0412\u0409\3\2\2\2\u0412\u040a"+
		"\3\2\2\2\u0412\u040b\3\2\2\2\u0412\u040c\3\2\2\2\u0412\u040d\3\2\2\2\u0412"+
		"\u040e\3\2\2\2\u0412\u040f\3\2\2\2\u0412\u0410\3\2\2\2\u0412\u0411\3\2"+
		"\2\2\u0413\u00a1\3\2\2\2\u0414\u0415\7\66\2\2\u0415\u0416\5\u00a4S\2\u0416"+
		"\u00a3\3\2\2\2\u0417\u0423\7^\2\2\u0418\u041d\5\u009eP\2\u0419\u041a\7"+
		"e\2\2\u041a\u041c\5\u009eP\2\u041b\u0419\3\2\2\2\u041c\u041f\3\2\2\2\u041d"+
		"\u041b\3\2\2\2\u041d\u041e\3\2\2\2\u041e\u0421\3\2\2\2\u041f\u041d\3\2"+
		"\2\2\u0420\u0422\7e\2\2\u0421\u0420\3\2\2\2\u0421\u0422\3\2\2\2\u0422"+
		"\u0424\3\2\2\2\u0423\u0418\3\2\2\2\u0423\u0424\3\2\2\2\u0424\u0425\3\2"+
		"\2\2\u0425\u0426\7_\2\2\u0426\u00a5\3\2\2\2\u0427\u042f\5\u013c\u009f"+
		"\2\u0428\u042f\5\u010c\u0087\2\u0429\u042f\5\u00a8U\2\u042a\u042f\5\u0114"+
		"\u008b\2\u042b\u042f\5\u0116\u008c\2\u042c\u042f\5H%\2\u042d\u042f\5\u010a"+
		"\u0086\2\u042e\u0427\3\2\2\2\u042e\u0428\3\2\2\2\u042e\u0429\3\2\2\2\u042e"+
		"\u042a\3\2\2\2\u042e\u042b\3\2\2\2\u042e\u042c\3\2\2\2\u042e\u042d\3\2"+
		"\2\2\u042f\u00a7\3\2\2\2\u0430\u0431\7b\2\2\u0431\u0432\7l\2\2\u0432\u0433"+
		"\7c\2\2\u0433\u0434\5\u0110\u0089\2\u0434\u00a9\3\2\2\2\u0435\u0445\7"+
		"b\2\2\u0436\u0438\5\u00acW\2\u0437\u0436\3\2\2\2\u0437\u0438\3\2\2\2\u0438"+
		"\u0439\3\2\2\2\u0439\u043b\7g\2\2\u043a\u043c\5\u00aeX\2\u043b\u043a\3"+
		"\2\2\2\u043b\u043c\3\2\2\2\u043c\u0446\3\2\2\2\u043d\u043f\5\u00acW\2"+
		"\u043e\u043d\3\2\2\2\u043e\u043f\3\2\2\2\u043f\u0440\3\2\2\2\u0440\u0441"+
		"\7g\2\2\u0441\u0442\5\u00aeX\2\u0442\u0443\7g\2\2\u0443\u0444\5\u00b0"+
		"Y\2\u0444\u0446\3\2\2\2\u0445\u0437\3\2\2\2\u0445\u043e\3\2\2\2\u0446"+
		"\u0447\3\2\2\2\u0447\u0448\7c\2\2\u0448\u00ab\3\2\2\2\u0449\u044a\5\u0086"+
		"D\2\u044a\u00ad\3\2\2\2\u044b\u044c\5\u0086D\2\u044c\u00af\3\2\2\2\u044d"+
		"\u044e\5\u0086D\2\u044e\u00b1\3\2\2\2\u044f\u0451\t\20\2\2\u0450\u044f"+
		"\3\2\2\2\u0450\u0451\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u0453\7d\2\2\u0453"+
		"\u00b3\3\2\2\2\u0454\u0455\7P\2\2\u0455\u0456\7]\2\2\u0456\u00b5\3\2\2"+
		"\2\u0457\u0458\5\u0140\u00a1\2\u0458\u00b7\3\2\2\2\u0459\u045d\5\u00ba"+
		"^\2\u045a\u045d\5\u00c2b\2\u045b\u045d\5\u00c6d\2\u045c\u0459\3\2\2\2"+
		"\u045c\u045a\3\2\2\2\u045c\u045b\3\2\2\2\u045d\u00b9\3\2\2\2\u045e\u046a"+
		"\7R\2\2\u045f\u046b\5\u00bc_\2\u0460\u0466\7^\2\2\u0461\u0462\5\u00bc"+
		"_\2\u0462\u0463\5\u0150\u00a9\2\u0463\u0465\3\2\2\2\u0464\u0461\3\2\2"+
		"\2\u0465\u0468\3\2\2\2\u0466\u0464\3\2\2\2\u0466\u0467\3\2\2\2\u0467\u0469"+
		"\3\2\2\2\u0468\u0466\3\2\2\2\u0469\u046b\7_\2\2\u046a\u045f\3\2\2\2\u046a"+
		"\u0460\3\2\2\2\u046b\u00bb\3\2\2\2\u046c\u0472\5\u00be`\2\u046d\u046f"+
		"\5\u009eP\2\u046e\u046d\3\2\2\2\u046e\u046f\3\2\2\2\u046f\u0470\3\2\2"+
		"\2\u0470\u0471\7d\2\2\u0471\u0473\5\u00c0a\2\u0472\u046e\3\2\2\2\u0472"+
		"\u0473\3\2\2\2\u0473\u00bd\3\2\2\2\u0474\u0479\7]\2\2\u0475\u0476\7e\2"+
		"\2\u0476\u0478\7]\2\2\u0477\u0475\3\2\2\2\u0478\u047b\3\2\2\2\u0479\u0477"+
		"\3\2\2\2\u0479\u047a\3\2\2\2\u047a\u00bf\3\2\2\2\u047b\u0479\3\2\2\2\u047c"+
		"\u0481\5\u0086D\2\u047d\u047e\7e\2\2\u047e\u0480\5\u0086D\2\u047f\u047d"+
		"\3\2\2\2\u0480\u0483\3\2\2\2\u0481\u047f\3\2\2\2\u0481\u0482\3\2\2\2\u0482"+
		"\u00c1\3\2\2\2\u0483\u0481\3\2\2\2\u0484\u0490\7V\2\2\u0485\u0491\5\u00c4"+
		"c\2\u0486\u048c\7^\2\2\u0487\u0488\5\u00c4c\2\u0488\u0489\5\u0150\u00a9"+
		"\2\u0489\u048b\3\2\2\2\u048a\u0487\3\2\2\2\u048b\u048e\3\2\2\2\u048c\u048a"+
		"\3\2\2\2\u048c\u048d\3\2\2\2\u048d\u048f\3\2\2\2\u048e\u048c\3\2\2\2\u048f"+
		"\u0491\7_\2\2\u0490\u0485\3\2\2\2\u0490\u0486\3\2\2\2\u0491\u00c3\3\2"+
		"\2\2\u0492\u0494\7]\2\2\u0493\u0495\7d\2\2\u0494\u0493\3\2\2\2\u0494\u0495"+
		"\3\2\2\2\u0495\u0496\3\2\2\2\u0496\u0497\5\u009eP\2\u0497\u00c5\3\2\2"+
		"\2\u0498\u04a4\7[\2\2\u0499\u04a5\5x=\2\u049a\u04a0\7^\2\2\u049b\u049c"+
		"\5x=\2\u049c\u049d\5\u0150\u00a9\2\u049d\u049f\3\2\2\2\u049e\u049b\3\2"+
		"\2\2\u049f\u04a2\3\2\2\2\u04a0\u049e\3\2\2\2\u04a0\u04a1\3\2\2\2\u04a1"+
		"\u04a3\3\2\2\2\u04a2\u04a0\3\2\2\2\u04a3\u04a5\7_\2\2\u04a4\u0499\3\2"+
		"\2\2\u04a4\u049a\3\2\2\2\u04a5\u00c7\3\2\2\2\u04a6\u04a8\7`\2\2\u04a7"+
		"\u04a9\5\u00caf\2\u04a8\u04a7\3\2\2\2\u04a8\u04a9\3\2\2\2\u04a9\u04aa"+
		"\3\2\2\2\u04aa\u04ab\7a\2\2\u04ab\u00c9\3\2\2\2\u04ac\u04ae\5\u0150\u00a9"+
		"\2\u04ad\u04ac\3\2\2\2\u04ad\u04ae\3\2\2\2\u04ae\u04af\3\2\2\2\u04af\u04b0"+
		"\5\u0088E\2\u04b0\u04b1\5\u0150\u00a9\2\u04b1\u04b3\3\2\2\2\u04b2\u04ad"+
		"\3\2\2\2\u04b3\u04b4\3\2\2\2\u04b4\u04b2\3\2\2\2\u04b4\u04b5\3\2\2\2\u04b5"+
		"\u00cb\3\2\2\2\u04b6\u04bc\5\u00d0i\2\u04b7\u04bc\5\u00d2j\2\u04b8\u04bc"+
		"\5\u00d4k\2\u04b9\u04bc\5\u00ceh\2\u04ba\u04bc\5z>\2\u04bb\u04b6\3\2\2"+
		"\2\u04bb\u04b7\3\2\2\2\u04bb\u04b8\3\2\2\2\u04bb\u04b9\3\2\2\2\u04bb\u04ba"+
		"\3\2\2\2\u04bc\u00cd\3\2\2\2\u04bd\u04be\5\u0086D\2\u04be\u00cf\3\2\2"+
		"\2\u04bf\u04c0\5\u0086D\2\u04c0\u04c1\7\u0081\2\2\u04c1\u04c2\5\u0086"+
		"D\2\u04c2\u00d1\3\2\2\2\u04c3\u04c4\5\u0086D\2\u04c4\u04c5\t\21\2\2\u04c5"+
		"\u00d3\3\2\2\2\u04c6\u04c7\5\u00c0a\2\u04c7\u04c8\5\u00b2Z\2\u04c8\u04c9"+
		"\5\u00c0a\2\u04c9\u00d5\3\2\2\2\u04ca\u04cb\t\22\2\2\u04cb\u00d7\3\2\2"+
		"\2\u04cc\u04cd\7]\2\2\u04cd\u04cf\7g\2\2\u04ce\u04d0\5\u0088E\2\u04cf"+
		"\u04ce\3\2\2\2\u04cf\u04d0\3\2\2\2\u04d0\u00d9\3\2\2\2\u04d1\u04d3\7Z"+
		"\2\2\u04d2\u04d4\5\u00c0a\2\u04d3\u04d2\3\2\2\2\u04d3\u04d4\3\2\2\2\u04d4"+
		"\u00db\3\2\2\2\u04d5\u04d7\7C\2\2\u04d6\u04d8\7]\2\2\u04d7\u04d6\3\2\2"+
		"\2\u04d7\u04d8\3\2\2\2\u04d8\u00dd\3\2\2\2\u04d9\u04db\7W\2\2\u04da\u04dc"+
		"\7]\2\2\u04db\u04da\3\2\2\2\u04db\u04dc\3\2\2\2\u04dc\u00df\3\2\2\2\u04dd"+
		"\u04de\7O\2\2\u04de\u04df\7]\2\2\u04df\u00e1\3\2\2\2\u04e0\u04e1\7S\2"+
		"\2\u04e1\u00e3\3\2\2\2\u04e2\u04e3\7I\2\2\u04e3\u04e4\5\u0086D\2\u04e4"+
		"\u00e5\3\2\2\2\u04e5\u04ee\7T\2\2\u04e6\u04ef\5\u0086D\2\u04e7\u04e8\5"+
		"\u0150\u00a9\2\u04e8\u04e9\5\u0086D\2\u04e9\u04ef\3\2\2\2\u04ea\u04eb"+
		"\5\u00ccg\2\u04eb\u04ec\5\u0150\u00a9\2\u04ec\u04ed\5\u0086D\2\u04ed\u04ef"+
		"\3\2\2\2\u04ee\u04e6\3\2\2\2\u04ee\u04e7\3\2\2\2\u04ee\u04ea\3\2\2\2\u04ef"+
		"\u04f0\3\2\2\2\u04f0\u04f6\5\u00c8e\2\u04f1\u04f4\7N\2\2\u04f2\u04f5\5"+
		"\u00e6t\2\u04f3\u04f5\5\u00c8e\2\u04f4\u04f2\3\2\2\2\u04f4\u04f3\3\2\2"+
		"\2\u04f5\u04f7\3\2\2\2\u04f6\u04f1\3\2\2\2\u04f6\u04f7\3\2\2\2\u04f7\u00e7"+
		"\3\2\2\2\u04f8\u04fb\5\u00eav\2\u04f9\u04fb\5\u00f0y\2\u04fa\u04f8\3\2"+
		"\2\2\u04fa\u04f9\3\2\2\2\u04fb\u00e9\3\2\2\2\u04fc\u0507\7Q\2\2\u04fd"+
		"\u04ff\5\u0086D\2\u04fe\u04fd\3\2\2\2\u04fe\u04ff\3\2\2\2\u04ff\u0508"+
		"\3\2\2\2\u0500\u0502\5\u00ccg\2\u0501\u0500\3\2\2\2\u0501\u0502\3\2\2"+
		"\2\u0502\u0503\3\2\2\2\u0503\u0505\5\u0150\u00a9\2\u0504\u0506\5\u0086"+
		"D\2\u0505\u0504\3\2\2\2\u0505\u0506\3\2\2\2\u0506\u0508\3\2\2\2\u0507"+
		"\u04fe\3\2\2\2\u0507\u0501\3\2\2\2\u0508\u0509\3\2\2\2\u0509\u050d\7`"+
		"\2\2\u050a\u050c\5\u00ecw\2\u050b\u050a\3\2\2\2\u050c\u050f\3\2\2\2\u050d"+
		"\u050b\3\2\2\2\u050d\u050e\3\2\2\2\u050e\u0510\3\2\2\2\u050f\u050d\3\2"+
		"\2\2\u0510\u0511\7a\2\2\u0511\u00eb\3\2\2\2\u0512\u0513\5\u00eex\2\u0513"+
		"\u0515\7g\2\2\u0514\u0516\5\u00caf\2\u0515\u0514\3\2\2\2\u0515\u0516\3"+
		"\2\2\2\u0516\u00ed\3\2\2\2\u0517\u0518\7H\2\2\u0518\u051b\5\u00c0a\2\u0519"+
		"\u051b\7D\2\2\u051a\u0517\3\2\2\2\u051a\u0519\3\2\2\2\u051b\u00ef\3\2"+
		"\2\2\u051c\u0525\7Q\2\2\u051d\u0526\5\u00f2z\2\u051e\u051f\5\u0150\u00a9"+
		"\2\u051f\u0520\5\u00f2z\2\u0520\u0526\3\2\2\2\u0521\u0522\5\u00ccg\2\u0522"+
		"\u0523\5\u0150\u00a9\2\u0523\u0524\5\u00f2z\2\u0524\u0526\3\2\2\2\u0525"+
		"\u051d\3\2\2\2\u0525\u051e\3\2\2\2\u0525\u0521\3\2\2\2\u0526\u0527\3\2"+
		"\2\2\u0527\u052b\7`\2\2\u0528\u052a\5\u00f4{\2\u0529\u0528\3\2\2\2\u052a"+
		"\u052d\3\2\2\2\u052b\u0529\3\2\2\2\u052b\u052c\3\2\2\2\u052c\u052e\3\2"+
		"\2\2\u052d\u052b\3\2\2\2\u052e\u052f\7a\2\2\u052f\u00f1\3\2\2\2\u0530"+
		"\u0531\7]\2\2\u0531\u0533\7k\2\2\u0532\u0530\3\2\2\2\u0532\u0533\3\2\2"+
		"\2\u0533\u0534\3\2\2\2\u0534\u0535\5\u0094K\2\u0535\u0536\7h\2\2\u0536"+
		"\u0537\7^\2\2\u0537\u0538\7V\2\2\u0538\u0539\7_\2\2\u0539\u00f3\3\2\2"+
		"\2\u053a\u053b\5\u00f6|\2\u053b\u053d\7g\2\2\u053c\u053e\5\u00caf\2\u053d"+
		"\u053c\3\2\2\2\u053d\u053e\3\2\2\2\u053e\u00f5\3\2\2\2\u053f\u0540\7H"+
		"\2\2\u0540\u0543\5\u00f8}\2\u0541\u0543\7D\2\2\u0542\u053f\3\2\2\2\u0542"+
		"\u0541\3\2\2\2\u0543\u00f7\3\2\2\2\u0544\u0547\5\u009eP\2\u0545\u0547"+
		"\7\\\2\2\u0546\u0544\3\2\2\2\u0546\u0545\3\2\2\2\u0547\u054f\3\2\2\2\u0548"+
		"\u054b\7e\2\2\u0549\u054c\5\u009eP\2\u054a\u054c\7\\\2\2\u054b\u0549\3"+
		"\2\2\2\u054b\u054a\3\2\2\2\u054c\u054e\3\2\2\2\u054d\u0548\3\2\2\2\u054e"+
		"\u0551\3\2\2\2\u054f\u054d\3\2\2\2\u054f\u0550\3\2\2\2\u0550\u00f9\3\2"+
		"\2\2\u0551\u054f\3\2\2\2\u0552\u0553\7G\2\2\u0553\u0557\7`\2\2\u0554\u0556"+
		"\5\u00fc\177\2\u0555\u0554\3\2\2\2\u0556\u0559\3\2\2\2\u0557\u0555\3\2"+
		"\2\2\u0557\u0558\3\2\2\2\u0558\u055a\3\2\2\2\u0559\u0557\3\2\2\2\u055a"+
		"\u055b\7a\2\2\u055b\u00fb\3\2\2\2\u055c\u055d\5\u00fe\u0080\2\u055d\u055f"+
		"\7g\2\2\u055e\u0560\5\u00caf\2\u055f\u055e\3\2\2\2\u055f\u0560\3\2\2\2"+
		"\u0560\u00fd\3\2\2\2\u0561\u0564\7H\2\2\u0562\u0565\5\u00d0i\2\u0563\u0565"+
		"\5\u0100\u0081\2\u0564\u0562\3\2\2\2\u0564\u0563\3\2\2\2\u0565\u0568\3"+
		"\2\2\2\u0566\u0568\7D\2\2\u0567\u0561\3\2\2\2\u0567\u0566\3\2\2\2\u0568"+
		"\u00ff\3\2\2\2\u0569\u056a\5\u00c0a\2\u056a\u056b\7d\2\2\u056b\u0570\3"+
		"\2\2\2\u056c\u056d\5\u00be`\2\u056d\u056e\7k\2\2\u056e\u0570\3\2\2\2\u056f"+
		"\u0569\3\2\2\2\u056f\u056c\3\2\2\2\u056f\u0570\3\2\2\2\u0570\u0571\3\2"+
		"\2\2\u0571\u0572\5\u0086D\2\u0572\u0101\3\2\2\2\u0573\u0577\7X\2\2\u0574"+
		"\u0578\5\u0086D\2\u0575\u0578\5\u0104\u0083\2\u0576\u0578\5\u0106\u0084"+
		"\2\u0577\u0574\3\2\2\2\u0577\u0575\3\2\2\2\u0577\u0576\3\2\2\2\u0577\u0578"+
		"\3\2\2\2\u0578\u0579\3\2\2\2\u0579\u057a\5\u00c8e\2\u057a\u0103\3\2\2"+
		"\2\u057b\u057d\5\u00ccg\2\u057c\u057b\3\2\2\2\u057c\u057d\3\2\2\2\u057d"+
		"\u057e\3\2\2\2\u057e\u0580\5\u0150\u00a9\2\u057f\u0581\5\u0086D\2\u0580"+
		"\u057f\3\2\2\2\u0580\u0581\3\2\2\2\u0581\u0582\3\2\2\2\u0582\u0584\5\u0150"+
		"\u00a9\2\u0583\u0585\5\u00ccg\2\u0584\u0583\3\2\2\2\u0584\u0585\3\2\2"+
		"\2\u0585\u0105\3\2\2\2\u0586\u0587\5\u00c0a\2\u0587\u0588\7d\2\2\u0588"+
		"\u058d\3\2\2\2\u0589\u058a\5\u00be`\2\u058a\u058b\7k\2\2\u058b\u058d\3"+
		"\2\2\2\u058c\u0586\3\2\2\2\u058c\u0589\3\2\2\2\u058c\u058d\3\2\2\2\u058d"+
		"\u058e\3\2\2\2\u058e\u058f\7U\2\2\u058f\u0590\5\u0086D\2\u0590\u0107\3"+
		"\2\2\2\u0591\u0592\7J\2\2\u0592\u0593\5\u0086D\2\u0593\u0109\3\2\2\2\u0594"+
		"\u0597\5\u012e\u0098\2\u0595\u0597\7]\2\2\u0596\u0594\3\2\2\2\u0596\u0595"+
		"\3\2\2\2\u0597\u010b\3\2\2\2\u0598\u0599\7b\2\2\u0599\u059a\5\u010e\u0088"+
		"\2\u059a\u059b\7c\2\2\u059b\u059c\5\u0110\u0089\2\u059c\u010d\3\2\2\2"+
		"\u059d\u059e\5\u0086D\2\u059e\u010f\3\2\2\2\u059f\u05a0\5\u009eP\2\u05a0"+
		"\u0111\3\2\2\2\u05a1\u05a2\7\177\2\2\u05a2\u05a3\5\u009eP\2\u05a3\u0113"+
		"\3\2\2\2\u05a4\u05a5\7b\2\2\u05a5\u05a6\7c\2\2\u05a6\u05a7\5\u0110\u0089"+
		"\2\u05a7\u0115\3\2\2\2\u05a8\u05a9\7K\2\2\u05a9\u05aa\7b\2\2\u05aa\u05ab"+
		"\5\u009eP\2\u05ab\u05ac\7c\2\2\u05ac\u05ad\5\u0110\u0089\2\u05ad\u0117"+
		"\3\2\2\2\u05ae\u05b4\7M\2\2\u05af\u05b0\7M\2\2\u05b0\u05b4\7\u0081\2\2"+
		"\u05b1\u05b2\7\u0081\2\2\u05b2\u05b4\7M\2\2\u05b3\u05ae\3\2\2\2\u05b3"+
		"\u05af\3\2\2\2\u05b3\u05b1\3\2\2\2\u05b4\u05b5\3\2\2\2\u05b5\u05b6\5\u0110"+
		"\u0089\2\u05b6\u0119\3\2\2\2\u05b7\u05b8\7E\2\2\u05b8\u05b9\5\u011c\u008f"+
		"\2\u05b9\u011b\3\2\2\2\u05ba\u05bb\5\u0120\u0091\2\u05bb\u05bc\5\u011e"+
		"\u0090\2\u05bc\u05bf\3\2\2\2\u05bd\u05bf\5\u0120\u0091\2\u05be\u05ba\3"+
		"\2\2\2\u05be\u05bd\3\2\2\2\u05bf\u011d\3\2\2\2\u05c0\u05c3\5\u0120\u0091"+
		"\2\u05c1\u05c3\5\u009eP\2\u05c2\u05c0\3\2\2\2\u05c2\u05c1\3\2\2\2\u05c3"+
		"\u011f\3\2\2\2\u05c4\u05d0\7^\2\2\u05c5\u05ca\5~@\2\u05c6\u05c7\7e\2\2"+
		"\u05c7\u05c9\5~@\2\u05c8\u05c6\3\2\2\2\u05c9\u05cc\3\2\2\2\u05ca\u05c8"+
		"\3\2\2\2\u05ca\u05cb\3\2\2\2\u05cb\u05ce\3\2\2\2\u05cc\u05ca\3\2\2\2\u05cd"+
		"\u05cf\7e\2\2\u05ce\u05cd\3\2\2\2\u05ce\u05cf\3\2\2\2\u05cf\u05d1\3\2"+
		"\2\2\u05d0\u05c5\3\2\2\2\u05d0\u05d1\3\2\2\2\u05d1\u05d2\3\2\2\2\u05d2"+
		"\u05d3\7_\2\2\u05d3\u0121\3\2\2\2\u05d4\u05d5\5\u0124\u0093\2\u05d5\u05d6"+
		"\7^\2\2\u05d6\u05d8\5\u0086D\2\u05d7\u05d9\7e\2\2\u05d8\u05d7\3\2\2\2"+
		"\u05d8\u05d9\3\2\2\2\u05d9\u05da\3\2\2\2\u05da\u05db\7_\2\2\u05db\u0123"+
		"\3\2\2\2\u05dc\u05e2\5\u00a0Q\2\u05dd\u05de\7^\2\2\u05de\u05df\5\u0124"+
		"\u0093\2\u05df\u05e0\7_\2\2\u05e0\u05e2\3\2\2\2\u05e1\u05dc\3\2\2\2\u05e1"+
		"\u05dd\3\2\2\2\u05e2\u0125\3\2\2\2\u05e3\u05ea\5\u0128\u0095\2\u05e4\u05ea"+
		"\5\u012c\u0097\2\u05e5\u05e6\7^\2\2\u05e6\u05e7\5\u0086D\2\u05e7\u05e8"+
		"\7_\2\2\u05e8\u05ea\3\2\2\2\u05e9\u05e3\3\2\2\2\u05e9\u05e4\3\2\2\2\u05e9"+
		"\u05e5\3\2\2\2\u05ea\u0127\3\2\2\2\u05eb\u05ef\5\u0092J\2\u05ec\u05ef"+
		"\5\u0130\u0099\2\u05ed\u05ef\5\u0144\u00a3\2\u05ee\u05eb\3\2\2\2\u05ee"+
		"\u05ec\3\2\2\2\u05ee\u05ed\3\2\2\2\u05ef\u0129\3\2\2\2\u05f0\u05f1\t\23"+
		"\2\2\u05f1\u012b\3\2\2\2\u05f2\u05f3\7]\2\2\u05f3\u012d\3\2\2\2\u05f4"+
		"\u05f5\7]\2\2\u05f5\u05f6\7h\2\2\u05f6\u05f7\7]\2\2\u05f7\u012f\3\2\2"+
		"\2\u05f8\u05f9\5\u00a6T\2\u05f9\u05fa\5\u0132\u009a\2\u05fa\u0131\3\2"+
		"\2\2\u05fb\u0600\7`\2\2\u05fc\u05fe\5\u0134\u009b\2\u05fd\u05ff\7e\2\2"+
		"\u05fe\u05fd\3\2\2\2\u05fe\u05ff\3\2\2\2\u05ff\u0601\3\2\2\2\u0600\u05fc"+
		"\3\2\2\2\u0600\u0601\3\2\2\2\u0601\u0602\3\2\2\2\u0602\u0603\7a\2\2\u0603"+
		"\u0133\3\2\2\2\u0604\u0609\5\u0136\u009c\2\u0605\u0606\7e\2\2\u0606\u0608"+
		"\5\u0136\u009c\2\u0607\u0605\3\2\2\2\u0608\u060b\3\2\2\2\u0609\u0607\3"+
		"\2\2\2\u0609\u060a\3\2\2\2\u060a\u0135\3\2\2\2\u060b\u0609\3\2\2\2\u060c"+
		"\u060d\5\u0138\u009d\2\u060d\u060e\7g\2\2\u060e\u0610\3\2\2\2\u060f\u060c"+
		"\3\2\2\2\u060f\u0610\3\2\2\2\u0610\u0611\3\2\2\2\u0611\u0612\5\u013a\u009e"+
		"\2\u0612\u0137\3\2\2\2\u0613\u0616\5\u0086D\2\u0614\u0616\5\u0132\u009a"+
		"\2\u0615\u0613\3\2\2\2\u0615\u0614\3\2\2\2\u0616\u0139\3\2\2\2\u0617\u061a"+
		"\5\u0086D\2\u0618\u061a\5\u0132\u009a\2\u0619\u0617\3\2\2\2\u0619\u0618"+
		"\3\2\2\2\u061a\u013b\3\2\2\2\u061b\u061c\7L\2\2\u061c\u0622\7`\2\2\u061d"+
		"\u061e\5\u013e\u00a0\2\u061e\u061f\5\u0150\u00a9\2\u061f\u0621\3\2\2\2"+
		"\u0620\u061d\3\2\2\2\u0621\u0624\3\2\2\2\u0622\u0620\3\2\2\2\u0622\u0623"+
		"\3\2\2\2\u0623\u0625\3\2\2\2\u0624\u0622\3\2\2\2\u0625\u0626\7a\2\2\u0626"+
		"\u013d\3\2\2\2\u0627\u0628\5\u00be`\2\u0628\u0629\5\u009eP\2\u0629\u062c"+
		"\3\2\2\2\u062a\u062c\5\u0142\u00a2\2\u062b\u0627\3\2\2\2\u062b\u062a\3"+
		"\2\2\2\u062c\u062e\3\2\2\2\u062d\u062f\5\u0140\u00a1\2\u062e\u062d\3\2"+
		"\2\2\u062e\u062f\3\2\2\2\u062f\u013f\3\2\2\2\u0630\u0631\t\24\2\2\u0631"+
		"\u0141\3\2\2\2\u0632\u0634\7\177\2\2\u0633\u0632\3\2\2\2\u0633\u0634\3"+
		"\2\2\2\u0634\u0635\3\2\2\2\u0635\u0636\5\u010a\u0086\2\u0636\u0143\3\2"+
		"\2\2\u0637\u0638\7E\2\2\u0638\u0639\5\u011c\u008f\2\u0639\u063a\5\u00c8"+
		"e\2\u063a\u0145\3\2\2\2\u063b\u063c\7b\2\2\u063c\u063d\5\u0086D\2\u063d"+
		"\u063e\7c\2\2\u063e\u0147\3\2\2\2\u063f\u0640\7h\2\2\u0640\u0641\7^\2"+
		"\2\u0641\u0642\5\u009eP\2\u0642\u0643\7_\2\2\u0643\u0149\3\2\2\2\u0644"+
		"\u0653\7^\2\2\u0645\u064c\5\u00c0a\2\u0646\u0649\5\u0124\u0093\2\u0647"+
		"\u0648\7e\2\2\u0648\u064a\5\u00c0a\2\u0649\u0647\3\2\2\2\u0649\u064a\3"+
		"\2\2\2\u064a\u064c\3\2\2\2\u064b\u0645\3\2\2\2\u064b\u0646\3\2\2\2\u064c"+
		"\u064e\3\2\2\2\u064d\u064f\7l\2\2\u064e\u064d\3\2\2\2\u064e\u064f\3\2"+
		"\2\2\u064f\u0651\3\2\2\2\u0650\u0652\7e\2\2\u0651\u0650\3\2\2\2\u0651"+
		"\u0652\3\2\2\2\u0652\u0654\3\2\2\2\u0653\u064b\3\2\2\2\u0653\u0654\3\2"+
		"\2\2\u0654\u0655\3\2\2\2\u0655\u0656\7_\2\2\u0656\u014b\3\2\2\2\u0657"+
		"\u0658\5\u0124\u0093\2\u0658\u0659\7h\2\2\u0659\u065a\7]\2\2\u065a\u014d"+
		"\3\2\2\2\u065b\u065c\5\u009eP\2\u065c\u014f\3\2\2\2\u065d\u0662\7f\2\2"+
		"\u065e\u0662\7\2\2\3\u065f\u0662\7\u0097\2\2\u0660\u0662\6\u00a9\22\2"+
		"\u0661\u065d\3\2\2\2\u0661\u065e\3\2\2\2\u0661\u065f\3\2\2\2\u0661\u0660"+
		"\3\2\2\2\u0662\u0151\3\2\2\2\u00a8\u0160\u0165\u0168\u0171\u0177\u017d"+
		"\u0184\u018a\u018c\u0197\u019b\u01a1\u01a9\u01b3\u01c3\u01d1\u01d5\u01dc"+
		"\u01e4\u01ed\u020d\u0215\u022d\u023e\u024a\u0253\u0261\u0273\u027a\u027f"+
		"\u0284\u028e\u0291\u0295\u0299\u02a0\u02a3\u02b0\u02b8\u02bc\u02bf\u02c5"+
		"\u02c9\u02cc\u02d6\u02dd\u02e4\u02f0\u02f6\u02fd\u0302\u0308\u0314\u031a"+
		"\u031e\u0326\u032a\u0330\u0333\u0339\u033e\u0352\u0372\u0374\u038a\u0392"+
		"\u039d\u03a4\u03ae\u03bc\u03cd\u03cf\u03d4\u03d7\u03e0\u03e6\u03f0\u03f8"+
		"\u03fe\u0407\u0412\u041d\u0421\u0423\u042e\u0437\u043b\u043e\u0445\u0450"+
		"\u045c\u0466\u046a\u046e\u0472\u0479\u0481\u048c\u0490\u0494\u04a0\u04a4"+
		"\u04a8\u04ad\u04b4\u04bb\u04cf\u04d3\u04d7\u04db\u04ee\u04f4\u04f6\u04fa"+
		"\u04fe\u0501\u0505\u0507\u050d\u0515\u051a\u0525\u052b\u0532\u053d\u0542"+
		"\u0546\u054b\u054f\u0557\u055f\u0564\u0567\u056f\u0577\u057c\u0580\u0584"+
		"\u058c\u0596\u05b3\u05be\u05c2\u05ca\u05ce\u05d0\u05d8\u05e1\u05e9\u05ee"+
		"\u05fe\u0600\u0609\u060f\u0615\u0619\u0622\u062b\u062e\u0633\u0649\u064b"+
		"\u064e\u0651\u0653\u0661";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}