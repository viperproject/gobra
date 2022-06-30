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
		FOLD=23, UNFOLD=24, UNFOLDING=25, GHOST=26, IN=27, MULTI=28, SUBSET=29, 
		UNION=30, INTERSECTION=31, SETMINUS=32, IMPLIES=33, WAND=34, APPLY=35, 
		QMARK=36, L_PRED=37, R_PRED=38, SEQ=39, SET=40, MSET=41, DICT=42, OPT=43, 
		LEN=44, NEW=45, MAKE=46, CAP=47, SOME=48, GET=49, DOM=50, AXIOM=51, NONE=52, 
		PRED=53, TYPE_OF=54, IS_COMPARABLE=55, SHARE=56, ADDR_MOD=57, DOT_DOT=58, 
		SHARED=59, EXCLUSIVE=60, PREDICATE=61, WRITEPERM=62, NOPERM=63, TRUSTED=64, 
		OUTLINE=65, BREAK=66, DEFAULT=67, FUNC=68, INTERFACE=69, SELECT=70, CASE=71, 
		DEFER=72, GO=73, MAP=74, STRUCT=75, CHAN=76, ELSE=77, GOTO=78, PACKAGE=79, 
		SWITCH=80, CONST=81, FALLTHROUGH=82, IF=83, RANGE=84, TYPE=85, CONTINUE=86, 
		FOR=87, IMPORT=88, RETURN=89, VAR=90, NIL_LIT=91, IDENTIFIER=92, L_PAREN=93, 
		R_PAREN=94, L_CURLY=95, R_CURLY=96, L_BRACKET=97, R_BRACKET=98, ASSIGN=99, 
		COMMA=100, SEMI=101, COLON=102, DOT=103, PLUS_PLUS=104, MINUS_MINUS=105, 
		DECLARE_ASSIGN=106, ELLIPSIS=107, LOGICAL_OR=108, LOGICAL_AND=109, EQUALS=110, 
		NOT_EQUALS=111, LESS=112, LESS_OR_EQUALS=113, GREATER=114, GREATER_OR_EQUALS=115, 
		OR=116, DIV=117, MOD=118, LSHIFT=119, RSHIFT=120, BIT_CLEAR=121, EXCLAMATION=122, 
		PLUS=123, MINUS=124, CARET=125, STAR=126, AMPERSAND=127, RECEIVE=128, 
		DECIMAL_LIT=129, BINARY_LIT=130, OCTAL_LIT=131, HEX_LIT=132, HEX_FLOAT_LIT=133, 
		IMAGINARY_LIT=134, RUNE_LIT=135, BYTE_VALUE=136, OCTAL_BYTE_VALUE=137, 
		HEX_BYTE_VALUE=138, LITTLE_U_VALUE=139, BIG_U_VALUE=140, RAW_STRING_LIT=141, 
		INTERPRETED_STRING_LIT=142, WS=143, COMMENT=144, TERMINATOR=145, LINE_COMMENT=146, 
		WS_NLSEMI=147, COMMENT_NLSEMI=148, LINE_COMMENT_NLSEMI=149, EOS=150, OTHER=151;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_sourceFile = 5, RULE_ghostMember = 6, 
		RULE_ghostStatement = 7, RULE_auxiliaryStatement = 8, RULE_statementWithSpec = 9, 
		RULE_outlineStatement = 10, RULE_ghostPrimaryExpr = 11, RULE_permission = 12, 
		RULE_typeExpr = 13, RULE_boundVariables = 14, RULE_boundVariableDecl = 15, 
		RULE_triggers = 16, RULE_trigger = 17, RULE_predicateAccess = 18, RULE_optionSome = 19, 
		RULE_optionNone = 20, RULE_optionGet = 21, RULE_sConversion = 22, RULE_old = 23, 
		RULE_oldLabelUse = 24, RULE_labelUse = 25, RULE_before = 26, RULE_isComparable = 27, 
		RULE_typeOf = 28, RULE_access = 29, RULE_range = 30, RULE_seqUpdExp = 31, 
		RULE_seqUpdClause = 32, RULE_ghostTypeLit = 33, RULE_domainType = 34, 
		RULE_domainClause = 35, RULE_ghostSliceType = 36, RULE_sqType = 37, RULE_specification = 38, 
		RULE_specStatement = 39, RULE_terminationMeasure = 40, RULE_assertion = 41, 
		RULE_blockWithBodyParameterInfo = 42, RULE_closureSpecInstance = 43, RULE_closureSpecParams = 44, 
		RULE_closureSpecParam = 45, RULE_implementationProof = 46, RULE_methodImplementationProof = 47, 
		RULE_nonLocalReceiver = 48, RULE_selection = 49, RULE_implementationProofPredicateAlias = 50, 
		RULE_make = 51, RULE_new_ = 52, RULE_specMember = 53, RULE_functionDecl = 54, 
		RULE_methodDecl = 55, RULE_explicitGhostMember = 56, RULE_fpredicateDecl = 57, 
		RULE_predicateBody = 58, RULE_mpredicateDecl = 59, RULE_varSpec = 60, 
		RULE_shortVarDecl = 61, RULE_receiver = 62, RULE_parameterDecl = 63, RULE_actualParameterDecl = 64, 
		RULE_ghostParameterDecl = 65, RULE_parameterType = 66, RULE_expression = 67, 
		RULE_statement = 68, RULE_applyStmt = 69, RULE_packageStmt = 70, RULE_specForStmt = 71, 
		RULE_loopSpec = 72, RULE_deferStmt = 73, RULE_basicLit = 74, RULE_primaryExpr = 75, 
		RULE_operand = 76, RULE_literal = 77, RULE_functionLit = 78, RULE_closureDecl = 79, 
		RULE_predConstructArgs = 80, RULE_interfaceType = 81, RULE_predicateSpec = 82, 
		RULE_methodSpec = 83, RULE_type_ = 84, RULE_typeLit = 85, RULE_predType = 86, 
		RULE_predTypeParams = 87, RULE_literalType = 88, RULE_implicitArray = 89, 
		RULE_slice_ = 90, RULE_low = 91, RULE_high = 92, RULE_cap = 93, RULE_assign_op = 94, 
		RULE_packageClause = 95, RULE_importDecl = 96, RULE_importSpec = 97, RULE_importPath = 98, 
		RULE_declaration = 99, RULE_constDecl = 100, RULE_constSpec = 101, RULE_identifierList = 102, 
		RULE_expressionList = 103, RULE_typeDecl = 104, RULE_typeSpec = 105, RULE_varDecl = 106, 
		RULE_block = 107, RULE_statementList = 108, RULE_simpleStmt = 109, RULE_expressionStmt = 110, 
		RULE_sendStmt = 111, RULE_incDecStmt = 112, RULE_assignment = 113, RULE_emptyStmt = 114, 
		RULE_labeledStmt = 115, RULE_returnStmt = 116, RULE_breakStmt = 117, RULE_continueStmt = 118, 
		RULE_gotoStmt = 119, RULE_fallthroughStmt = 120, RULE_ifStmt = 121, RULE_switchStmt = 122, 
		RULE_exprSwitchStmt = 123, RULE_exprCaseClause = 124, RULE_exprSwitchCase = 125, 
		RULE_typeSwitchStmt = 126, RULE_typeSwitchGuard = 127, RULE_typeCaseClause = 128, 
		RULE_typeSwitchCase = 129, RULE_typeList = 130, RULE_selectStmt = 131, 
		RULE_commClause = 132, RULE_commCase = 133, RULE_recvStmt = 134, RULE_forStmt = 135, 
		RULE_forClause = 136, RULE_rangeClause = 137, RULE_goStmt = 138, RULE_typeName = 139, 
		RULE_arrayType = 140, RULE_arrayLength = 141, RULE_elementType = 142, 
		RULE_pointerType = 143, RULE_sliceType = 144, RULE_mapType = 145, RULE_channelType = 146, 
		RULE_functionType = 147, RULE_signature = 148, RULE_result = 149, RULE_parameters = 150, 
		RULE_conversion = 151, RULE_nonNamedType = 152, RULE_integer = 153, RULE_operandName = 154, 
		RULE_qualifiedIdent = 155, RULE_compositeLit = 156, RULE_literalValue = 157, 
		RULE_elementList = 158, RULE_keyedElement = 159, RULE_key = 160, RULE_element = 161, 
		RULE_structType = 162, RULE_fieldDecl = 163, RULE_string_ = 164, RULE_embeddedField = 165, 
		RULE_index = 166, RULE_typeAssertion = 167, RULE_arguments = 168, RULE_methodExpr = 169, 
		RULE_receiverType = 170, RULE_eos = 171;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "sourceFile", "ghostMember", "ghostStatement", 
			"auxiliaryStatement", "statementWithSpec", "outlineStatement", "ghostPrimaryExpr", 
			"permission", "typeExpr", "boundVariables", "boundVariableDecl", "triggers", 
			"trigger", "predicateAccess", "optionSome", "optionNone", "optionGet", 
			"sConversion", "old", "oldLabelUse", "labelUse", "before", "isComparable", 
			"typeOf", "access", "range", "seqUpdExp", "seqUpdClause", "ghostTypeLit", 
			"domainType", "domainClause", "ghostSliceType", "sqType", "specification", 
			"specStatement", "terminationMeasure", "assertion", "blockWithBodyParameterInfo", 
			"closureSpecInstance", "closureSpecParams", "closureSpecParam", "implementationProof", 
			"methodImplementationProof", "nonLocalReceiver", "selection", "implementationProofPredicateAlias", 
			"make", "new_", "specMember", "functionDecl", "methodDecl", "explicitGhostMember", 
			"fpredicateDecl", "predicateBody", "mpredicateDecl", "varSpec", "shortVarDecl", 
			"receiver", "parameterDecl", "actualParameterDecl", "ghostParameterDecl", 
			"parameterType", "expression", "statement", "applyStmt", "packageStmt", 
			"specForStmt", "loopSpec", "deferStmt", "basicLit", "primaryExpr", "operand", 
			"literal", "functionLit", "closureDecl", "predConstructArgs", "interfaceType", 
			"predicateSpec", "methodSpec", "type_", "typeLit", "predType", "predTypeParams", 
			"literalType", "implicitArray", "slice_", "low", "high", "cap", "assign_op", 
			"packageClause", "importDecl", "importSpec", "importPath", "declaration", 
			"constDecl", "constSpec", "identifierList", "expressionList", "typeDecl", 
			"typeSpec", "varDecl", "block", "statementList", "simpleStmt", "expressionStmt", 
			"sendStmt", "incDecStmt", "assignment", "emptyStmt", "labeledStmt", "returnStmt", 
			"breakStmt", "continueStmt", "gotoStmt", "fallthroughStmt", "ifStmt", 
			"switchStmt", "exprSwitchStmt", "exprCaseClause", "exprSwitchCase", "typeSwitchStmt", 
			"typeSwitchGuard", "typeCaseClause", "typeSwitchCase", "typeList", "selectStmt", 
			"commClause", "commCase", "recvStmt", "forStmt", "forClause", "rangeClause", 
			"goStmt", "typeName", "arrayType", "arrayLength", "elementType", "pointerType", 
			"sliceType", "mapType", "channelType", "functionType", "signature", "result", 
			"parameters", "conversion", "nonNamedType", "integer", "operandName", 
			"qualifiedIdent", "compositeLit", "literalValue", "elementList", "keyedElement", 
			"key", "element", "structType", "fieldDecl", "string_", "embeddedField", 
			"index", "typeAssertion", "arguments", "methodExpr", "receiverType", 
			"eos"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'true'", "'false'", "'assert'", "'assume'", "'inhale'", 
			"'exhale'", "'requires'", "'preserves'", "'ensures'", "'invariant'", 
			"'decreases'", "'pure'", "'implements'", "'as'", "'old'", "'before'", 
			"'#lhs'", "'forall'", "'exists'", "'acc'", "'fold'", "'unfold'", "'unfolding'", 
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
			"IMPL", "AS", "OLD", "BEFORE", "LHS", "FORALL", "EXISTS", "ACCESS", "FOLD", 
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
			setState(344);
			expression(0);
			setState(345);
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
			setState(347);
			statement();
			setState(348);
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
			setState(350);
			type_();
			setState(351);
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
			setState(353);
			maybeAddressableIdentifier();
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(354);
				match(COMMA);
				setState(355);
				maybeAddressableIdentifier();
				}
				}
				setState(360);
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
			setState(361);
			match(IDENTIFIER);
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(362);
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
			setState(365);
			packageClause();
			setState(366);
			eos();
			setState(372);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(367);
				importDecl();
				setState(368);
				eos();
				}
				}
				setState(374);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & ((1L << (PRE - 9)) | (1L << (PRESERVES - 9)) | (1L << (POST - 9)) | (1L << (DEC - 9)) | (1L << (PURE - 9)) | (1L << (GHOST - 9)) | (1L << (SEQ - 9)) | (1L << (SET - 9)) | (1L << (MSET - 9)) | (1L << (DICT - 9)) | (1L << (OPT - 9)) | (1L << (DOM - 9)) | (1L << (PRED - 9)) | (1L << (TRUSTED - 9)) | (1L << (FUNC - 9)) | (1L << (INTERFACE - 9)))) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (MAP - 74)) | (1L << (STRUCT - 74)) | (1L << (CHAN - 74)) | (1L << (CONST - 74)) | (1L << (TYPE - 74)) | (1L << (VAR - 74)) | (1L << (IDENTIFIER - 74)) | (1L << (L_PAREN - 74)) | (1L << (L_BRACKET - 74)) | (1L << (STAR - 74)) | (1L << (RECEIVE - 74)))) != 0)) {
				{
				{
				setState(378);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(375);
					specMember();
					}
					break;
				case 2:
					{
					setState(376);
					declaration();
					}
					break;
				case 3:
					{
					setState(377);
					ghostMember();
					}
					break;
				}
				setState(380);
				eos();
				}
				}
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(387);
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
			setState(393);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(389);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(390);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(391);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(392);
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
			setState(401);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				_localctx = new ExplicitGhostStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(395);
				match(GHOST);
				setState(396);
				statement();
				}
				break;
			case FOLD:
			case UNFOLD:
				_localctx = new FoldStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(397);
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
				setState(398);
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
				setState(399);
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
				setState(400);
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
			setState(403);
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
			setState(405);
			((StatementWithSpecContext)_localctx).specification = specification();
			{
			setState(406);
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
			setState(408);
			match(OUTLINE);
			setState(409);
			match(L_PAREN);
			setState(411);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(410);
				statementList();
				}
				break;
			}
			setState(413);
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
		enterRule(_localctx, 22, RULE_ghostPrimaryExpr);
		try {
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(415);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(416);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(417);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(418);
				typeExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(419);
				isComparable();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(420);
				old();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(421);
				before();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(422);
				sConversion();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(423);
				optionNone();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(424);
				optionSome();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(425);
				optionGet();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(426);
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
			setState(429);
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
			setState(431);
			match(TYPE);
			setState(432);
			match(L_BRACKET);
			setState(433);
			type_();
			setState(434);
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
			setState(436);
			boundVariableDecl();
			setState(441);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(437);
					match(COMMA);
					setState(438);
					boundVariableDecl();
					}
					} 
				}
				setState(443);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(444);
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
			setState(447);
			match(IDENTIFIER);
			setState(452);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(448);
				match(COMMA);
				setState(449);
				match(IDENTIFIER);
				}
				}
				setState(454);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(455);
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
			setState(460);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(457);
				trigger();
				}
				}
				setState(462);
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
			setState(463);
			match(L_CURLY);
			setState(464);
			expression(0);
			setState(469);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(465);
				match(COMMA);
				setState(466);
				expression(0);
				}
				}
				setState(471);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(472);
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
			setState(474);
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
			setState(476);
			match(SOME);
			setState(477);
			match(L_PAREN);
			setState(478);
			expression(0);
			setState(479);
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
			setState(481);
			match(NONE);
			setState(482);
			match(L_BRACKET);
			setState(483);
			type_();
			setState(484);
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
			setState(486);
			match(GET);
			setState(487);
			match(L_PAREN);
			setState(488);
			expression(0);
			setState(489);
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
			setState(491);
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
			setState(496);
			match(OLD);
			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(497);
				match(L_BRACKET);
				setState(498);
				oldLabelUse();
				setState(499);
				match(R_BRACKET);
				}
			}

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
			setState(509);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(507);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(508);
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
			setState(511);
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
		enterRule(_localctx, 52, RULE_before);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			match(BEFORE);
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
		enterRule(_localctx, 54, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(518);
			match(IS_COMPARABLE);
			setState(519);
			match(L_PAREN);
			setState(520);
			expression(0);
			setState(521);
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
		enterRule(_localctx, 56, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(523);
			match(TYPE_OF);
			setState(524);
			match(L_PAREN);
			setState(525);
			expression(0);
			setState(526);
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
		enterRule(_localctx, 58, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			match(ACCESS);
			setState(529);
			match(L_PAREN);
			setState(530);
			expression(0);
			setState(533);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(531);
				match(COMMA);
				setState(532);
				expression(0);
				}
			}

			setState(535);
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
		enterRule(_localctx, 60, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(537);
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
			setState(538);
			match(L_BRACKET);
			setState(539);
			expression(0);
			setState(540);
			match(DOT_DOT);
			setState(541);
			expression(0);
			setState(542);
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
		enterRule(_localctx, 62, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(544);
			match(L_BRACKET);
			{
			setState(545);
			seqUpdClause();
			setState(550);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(546);
				match(COMMA);
				setState(547);
				seqUpdClause();
				}
				}
				setState(552);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(553);
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
		enterRule(_localctx, 64, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			expression(0);
			setState(556);
			match(ASSIGN);
			setState(557);
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
		enterRule(_localctx, 66, RULE_ghostTypeLit);
		try {
			setState(562);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(559);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(560);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(561);
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
		enterRule(_localctx, 68, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			match(DOM);
			setState(565);
			match(L_CURLY);
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(566);
				domainClause();
				setState(567);
				eos();
				}
				}
				setState(573);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(574);
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
		enterRule(_localctx, 70, RULE_domainClause);
		try {
			setState(585);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(576);
				match(FUNC);
				setState(577);
				match(IDENTIFIER);
				setState(578);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(579);
				match(AXIOM);
				setState(580);
				match(L_CURLY);
				setState(581);
				expression(0);
				setState(582);
				eos();
				setState(583);
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
		enterRule(_localctx, 72, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(587);
			match(GHOST);
			setState(588);
			match(L_BRACKET);
			setState(589);
			match(R_BRACKET);
			setState(590);
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
		enterRule(_localctx, 74, RULE_sqType);
		int _la;
		try {
			setState(603);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(592);
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
				setState(593);
				match(L_BRACKET);
				setState(594);
				type_();
				setState(595);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(597);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(598);
				match(L_BRACKET);
				setState(599);
				type_();
				setState(600);
				match(R_BRACKET);
				setState(601);
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
		enterRule(_localctx, 76, RULE_specification);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(615);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(610);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(605);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(606);
						match(PURE);
						((SpecificationContext)_localctx).pure =  true;
						}
						break;
					case TRUSTED:
						{
						setState(608);
						match(TRUSTED);
						((SpecificationContext)_localctx).trusted =  true;
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(612);
					eos();
					}
					} 
				}
				setState(617);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			}
			setState(620);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(618);
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
		enterRule(_localctx, 78, RULE_specStatement);
		try {
			setState(630);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(622);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(623);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(624);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(625);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(626);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(627);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(628);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(629);
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
		enterRule(_localctx, 80, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(633);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(632);
				expressionList();
				}
				break;
			}
			setState(637);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(635);
				match(IF);
				setState(636);
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
		enterRule(_localctx, 82, RULE_assertion);
		try {
			setState(641);
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
				setState(640);
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
		enterRule(_localctx, 84, RULE_blockWithBodyParameterInfo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			match(L_CURLY);
			setState(648);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(644);
				match(SHARE);
				setState(645);
				identifierList();
				setState(646);
				eos();
				}
				break;
			}
			setState(651);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(650);
				statementList();
				}
				break;
			}
			setState(653);
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
		enterRule(_localctx, 86, RULE_closureSpecInstance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(655);
			match(IDENTIFIER);
			setState(664);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(656);
				match(L_CURLY);
				setState(661);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(657);
					closureSpecParams();
					setState(659);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(658);
						match(COMMA);
						}
					}

					}
				}

				setState(663);
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
		enterRule(_localctx, 88, RULE_closureSpecParams);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
			closureSpecParam();
			setState(671);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(667);
					match(COMMA);
					setState(668);
					closureSpecParam();
					}
					} 
				}
				setState(673);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
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
		enterRule(_localctx, 90, RULE_closureSpecParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(674);
				match(IDENTIFIER);
				setState(675);
				match(COLON);
				}
				break;
			}
			setState(678);
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
		enterRule(_localctx, 92, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(680);
			type_();
			setState(681);
			match(IMPL);
			setState(682);
			type_();
			setState(701);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(683);
				match(L_CURLY);
				setState(689);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(684);
					implementationProofPredicateAlias();
					setState(685);
					eos();
					}
					}
					setState(691);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(697);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(692);
					methodImplementationProof();
					setState(693);
					eos();
					}
					}
					setState(699);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(700);
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
		enterRule(_localctx, 94, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(704);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(703);
				match(PURE);
				}
			}

			setState(706);
			nonLocalReceiver();
			setState(707);
			match(IDENTIFIER);
			setState(708);
			signature();
			setState(710);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(709);
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
		enterRule(_localctx, 96, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(712);
			match(L_PAREN);
			setState(714);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(713);
				match(IDENTIFIER);
				}
				break;
			}
			setState(717);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(716);
				match(STAR);
				}
			}

			setState(719);
			typeName();
			setState(720);
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
		enterRule(_localctx, 98, RULE_selection);
		try {
			setState(727);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(722);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(723);
				type_();
				setState(724);
				match(DOT);
				setState(725);
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
		enterRule(_localctx, 100, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(729);
			match(PRED);
			setState(730);
			match(IDENTIFIER);
			setState(731);
			match(DECLARE_ASSIGN);
			setState(734);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(732);
				selection();
				}
				break;
			case 2:
				{
				setState(733);
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
		enterRule(_localctx, 102, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(736);
			match(MAKE);
			setState(737);
			match(L_PAREN);
			setState(738);
			type_();
			setState(741);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(739);
				match(COMMA);
				setState(740);
				expressionList();
				}
			}

			setState(743);
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
		enterRule(_localctx, 104, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(745);
			match(NEW);
			setState(746);
			match(L_PAREN);
			setState(747);
			type_();
			setState(748);
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
		enterRule(_localctx, 106, RULE_specMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			((SpecMemberContext)_localctx).specification = specification();
			setState(753);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(751);
				functionDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 2:
				{
				setState(752);
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
		enterRule(_localctx, 108, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(755);
			match(FUNC);
			setState(756);
			match(IDENTIFIER);
			{
			setState(757);
			signature();
			setState(759);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(758);
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
		enterRule(_localctx, 110, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(761);
			match(FUNC);
			setState(762);
			receiver();
			setState(763);
			match(IDENTIFIER);
			{
			setState(764);
			signature();
			setState(766);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(765);
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
		enterRule(_localctx, 112, RULE_explicitGhostMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(768);
			match(GHOST);
			setState(771);
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
				setState(769);
				specMember();
				}
				break;
			case CONST:
			case TYPE:
			case VAR:
				{
				setState(770);
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
		enterRule(_localctx, 114, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			match(PRED);
			setState(774);
			match(IDENTIFIER);
			setState(775);
			parameters();
			setState(777);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(776);
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
		enterRule(_localctx, 116, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
			match(L_CURLY);
			setState(780);
			expression(0);
			setState(781);
			eos();
			setState(782);
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
		enterRule(_localctx, 118, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(784);
			match(PRED);
			setState(785);
			receiver();
			setState(786);
			match(IDENTIFIER);
			setState(787);
			parameters();
			setState(789);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(788);
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
		enterRule(_localctx, 120, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(791);
			maybeAddressableIdentifierList();
			setState(799);
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
				setState(792);
				type_();
				setState(795);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
				case 1:
					{
					setState(793);
					match(ASSIGN);
					setState(794);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(797);
				match(ASSIGN);
				setState(798);
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
		enterRule(_localctx, 122, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			maybeAddressableIdentifierList();
			setState(802);
			match(DECLARE_ASSIGN);
			setState(803);
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
		enterRule(_localctx, 124, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(805);
			match(L_PAREN);
			setState(807);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(806);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(809);
			type_();
			setState(811);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(810);
				match(COMMA);
				}
			}

			setState(813);
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
		enterRule(_localctx, 126, RULE_parameterDecl);
		try {
			setState(817);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(815);
				actualParameterDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(816);
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
		enterRule(_localctx, 128, RULE_actualParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(820);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(819);
				identifierList();
				}
				break;
			}
			setState(822);
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
		enterRule(_localctx, 130, RULE_ghostParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(824);
			match(GHOST);
			setState(826);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(825);
				identifierList();
				}
				break;
			}
			setState(828);
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
		enterRule(_localctx, 132, RULE_parameterType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(831);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(830);
				match(ELLIPSIS);
				}
			}

			setState(833);
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
		int _startState = 134;
		enterRecursionRule(_localctx, 134, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(836);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 122)) & ~0x3f) == 0 && ((1L << (_la - 122)) & ((1L << (EXCLAMATION - 122)) | (1L << (PLUS - 122)) | (1L << (MINUS - 122)) | (1L << (CARET - 122)) | (1L << (STAR - 122)) | (1L << (AMPERSAND - 122)) | (1L << (RECEIVE - 122)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(837);
				expression(14);
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(838);
				primaryExpr(0);
				}
				break;
			case 3:
				{
				_localctx = new UnfoldingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(839);
				match(UNFOLDING);
				setState(840);
				predicateAccess();
				setState(841);
				match(IN);
				setState(842);
				expression(2);
				}
				break;
			case 4:
				{
				_localctx = new QuantificationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(844);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(845);
				boundVariables();
				setState(846);
				match(COLON);
				setState(847);
				match(COLON);
				setState(848);
				triggers();
				setState(849);
				expression(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(888);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(886);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
					case 1:
						{
						_localctx = new MulExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(853);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(854);
						((MulExprContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 117)) & ~0x3f) == 0 && ((1L << (_la - 117)) & ((1L << (DIV - 117)) | (1L << (MOD - 117)) | (1L << (LSHIFT - 117)) | (1L << (RSHIFT - 117)) | (1L << (BIT_CLEAR - 117)) | (1L << (STAR - 117)) | (1L << (AMPERSAND - 117)))) != 0)) ) {
							((MulExprContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(855);
						expression(13);
						}
						break;
					case 2:
						{
						_localctx = new AddExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(856);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(857);
						((AddExprContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 104)) & ~0x3f) == 0 && ((1L << (_la - 104)) & ((1L << (PLUS_PLUS - 104)) | (1L << (OR - 104)) | (1L << (PLUS - 104)) | (1L << (MINUS - 104)) | (1L << (CARET - 104)))) != 0)) ) {
							((AddExprContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(858);
						expression(12);
						}
						break;
					case 3:
						{
						_localctx = new P42ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(859);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(860);
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
						setState(861);
						expression(11);
						}
						break;
					case 4:
						{
						_localctx = new P41ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(862);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(863);
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
						setState(864);
						expression(10);
						}
						break;
					case 5:
						{
						_localctx = new RelExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(865);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(866);
						((RelExprContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 110)) & ~0x3f) == 0 && ((1L << (_la - 110)) & ((1L << (EQUALS - 110)) | (1L << (NOT_EQUALS - 110)) | (1L << (LESS - 110)) | (1L << (LESS_OR_EQUALS - 110)) | (1L << (GREATER - 110)) | (1L << (GREATER_OR_EQUALS - 110)))) != 0)) ) {
							((RelExprContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(867);
						expression(9);
						}
						break;
					case 6:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(868);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(869);
						match(LOGICAL_AND);
						setState(870);
						expression(7);
						}
						break;
					case 7:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(871);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(872);
						match(LOGICAL_OR);
						setState(873);
						expression(6);
						}
						break;
					case 8:
						{
						_localctx = new ImplicationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(874);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(875);
						match(IMPLIES);
						setState(876);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(877);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(878);
						match(QMARK);
						setState(879);
						expression(0);
						setState(880);
						match(COLON);
						setState(881);
						expression(3);
						}
						break;
					case 10:
						{
						_localctx = new ClosureImplSpecExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(883);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(884);
						match(IMPL);
						setState(885);
						closureSpecInstance();
						}
						break;
					}
					} 
				}
				setState(890);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
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
		enterRule(_localctx, 136, RULE_statement);
		try {
			setState(910);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(891);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(892);
				auxiliaryStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(893);
				packageStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(894);
				applyStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(895);
				declaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(896);
				labeledStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(897);
				simpleStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(898);
				goStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(899);
				returnStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(900);
				breakStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(901);
				continueStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(902);
				gotoStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(903);
				fallthroughStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(904);
				block();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(905);
				ifStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(906);
				switchStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(907);
				selectStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(908);
				specForStmt();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(909);
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
		enterRule(_localctx, 138, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(912);
			match(APPLY);
			setState(913);
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
		enterRule(_localctx, 140, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			match(PACKAGE);
			setState(916);
			expression(0);
			setState(918);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(917);
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
		enterRule(_localctx, 142, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(920);
			loopSpec();
			setState(921);
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
		enterRule(_localctx, 144, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(923);
				match(INV);
				setState(924);
				expression(0);
				setState(925);
				eos();
				}
				}
				setState(931);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(936);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(932);
				match(DEC);
				setState(933);
				terminationMeasure();
				setState(934);
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
		enterRule(_localctx, 146, RULE_deferStmt);
		int _la;
		try {
			setState(943);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(938);
				match(DEFER);
				setState(939);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(940);
				match(DEFER);
				setState(941);
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
				setState(942);
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
		enterRule(_localctx, 148, RULE_basicLit);
		try {
			setState(953);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(945);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(946);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(947);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(948);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(949);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(950);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(951);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(952);
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
		int _startState = 150;
		enterRecursionRule(_localctx, 150, RULE_primaryExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(967);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				_localctx = new OperandPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(956);
				operand();
				}
				break;
			case 2:
				{
				_localctx = new ConversionPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(957);
				conversion();
				}
				break;
			case 3:
				{
				_localctx = new MethodPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(958);
				methodExpr();
				}
				break;
			case 4:
				{
				_localctx = new GhostPrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(959);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(960);
				new_();
				}
				break;
			case 6:
				{
				_localctx = new MakeExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(961);
				make();
				}
				break;
			case 7:
				{
				_localctx = new BuiltInCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(962);
				((BuiltInCallExprContext)_localctx).call_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & ((1L << (LEN - 44)) | (1L << (CAP - 44)) | (1L << (DOM - 44)) | (1L << (RANGE - 44)))) != 0)) ) {
					((BuiltInCallExprContext)_localctx).call_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(963);
				match(L_PAREN);
				setState(964);
				expression(0);
				setState(965);
				match(R_PAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(991);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(989);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(969);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(970);
						match(DOT);
						setState(971);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new IndexPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(972);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(973);
						index();
						}
						break;
					case 3:
						{
						_localctx = new SlicePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(974);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(975);
						slice_();
						}
						break;
					case 4:
						{
						_localctx = new SeqUpdPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(976);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(977);
						seqUpdExp();
						}
						break;
					case 5:
						{
						_localctx = new TypeAssertionPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(978);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(979);
						typeAssertion();
						}
						break;
					case 6:
						{
						_localctx = new InvokePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(980);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(981);
						arguments();
						}
						break;
					case 7:
						{
						_localctx = new InvokePrimaryExprWithSpecContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(982);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(983);
						arguments();
						setState(984);
						match(AS);
						setState(985);
						closureSpecInstance();
						}
						break;
					case 8:
						{
						_localctx = new PredConstrPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(987);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(988);
						predConstructArgs();
						}
						break;
					}
					} 
				}
				setState(993);
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
		enterRule(_localctx, 152, RULE_operand);
		try {
			setState(1000);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(995);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(996);
				match(L_PAREN);
				setState(997);
				expression(0);
				setState(998);
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
		enterRule(_localctx, 154, RULE_literal);
		try {
			setState(1005);
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
				setState(1002);
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
				setState(1003);
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
				setState(1004);
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
		enterRule(_localctx, 156, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1007);
			((FunctionLitContext)_localctx).specification = specification();
			setState(1008);
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
		enterRule(_localctx, 158, RULE_closureDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			match(FUNC);
			setState(1012);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1011);
				match(IDENTIFIER);
				}
			}

			{
			setState(1014);
			signature();
			setState(1016);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(1015);
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
		enterRule(_localctx, 160, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1018);
			match(L_PRED);
			setState(1020);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1019);
				expressionList();
				}
			}

			setState(1023);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1022);
				match(COMMA);
				}
			}

			setState(1025);
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
		enterRule(_localctx, 162, RULE_interfaceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1027);
			match(INTERFACE);
			setState(1028);
			match(L_CURLY);
			setState(1038);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << PRED))) != 0) || _la==TRUSTED || _la==IDENTIFIER) {
				{
				{
				setState(1032);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
				case 1:
					{
					setState(1029);
					methodSpec();
					}
					break;
				case 2:
					{
					setState(1030);
					typeName();
					}
					break;
				case 3:
					{
					setState(1031);
					predicateSpec();
					}
					break;
				}
				setState(1034);
				eos();
				}
				}
				setState(1040);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1041);
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
		enterRule(_localctx, 164, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1043);
			match(PRED);
			setState(1044);
			match(IDENTIFIER);
			setState(1045);
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
		enterRule(_localctx, 166, RULE_methodSpec);
		int _la;
		try {
			setState(1062);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1048);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1047);
					match(GHOST);
					}
				}

				setState(1050);
				specification();
				setState(1051);
				match(IDENTIFIER);
				setState(1052);
				parameters();
				setState(1053);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1056);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1055);
					match(GHOST);
					}
				}

				setState(1058);
				specification();
				setState(1059);
				match(IDENTIFIER);
				setState(1060);
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
		enterRule(_localctx, 168, RULE_type_);
		try {
			setState(1071);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1064);
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
				setState(1065);
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
				setState(1066);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(1067);
				match(L_PAREN);
				setState(1068);
				type_();
				setState(1069);
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
		enterRule(_localctx, 170, RULE_typeLit);
		try {
			setState(1082);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1073);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1074);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1075);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1076);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1077);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1078);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1079);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1080);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1081);
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
		enterRule(_localctx, 172, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1084);
			match(PRED);
			setState(1085);
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
		enterRule(_localctx, 174, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1087);
			match(L_PAREN);
			setState(1099);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (FUNC - 68)) | (1L << (INTERFACE - 68)) | (1L << (MAP - 68)) | (1L << (STRUCT - 68)) | (1L << (CHAN - 68)) | (1L << (IDENTIFIER - 68)) | (1L << (L_PAREN - 68)) | (1L << (L_BRACKET - 68)) | (1L << (STAR - 68)) | (1L << (RECEIVE - 68)))) != 0)) {
				{
				setState(1088);
				type_();
				setState(1093);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1089);
						match(COMMA);
						setState(1090);
						type_();
						}
						} 
					}
					setState(1095);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
				}
				setState(1097);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1096);
					match(COMMA);
					}
				}

				}
			}

			setState(1101);
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
		enterRule(_localctx, 176, RULE_literalType);
		try {
			setState(1110);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1103);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1104);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1105);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1106);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1107);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1108);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1109);
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
		enterRule(_localctx, 178, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1112);
			match(L_BRACKET);
			setState(1113);
			match(ELLIPSIS);
			setState(1114);
			match(R_BRACKET);
			setState(1115);
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
		enterRule(_localctx, 180, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1117);
			match(L_BRACKET);
			setState(1133);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				{
				setState(1119);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(1118);
					low();
					}
				}

				setState(1121);
				match(COLON);
				setState(1123);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(1122);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1126);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(1125);
					low();
					}
				}

				setState(1128);
				match(COLON);
				setState(1129);
				high();
				setState(1130);
				match(COLON);
				setState(1131);
				cap();
				}
				break;
			}
			setState(1135);
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
		enterRule(_localctx, 182, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1137);
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
		enterRule(_localctx, 184, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1139);
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
		enterRule(_localctx, 186, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1141);
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
		enterRule(_localctx, 188, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 116)) & ~0x3f) == 0 && ((1L << (_la - 116)) & ((1L << (OR - 116)) | (1L << (DIV - 116)) | (1L << (MOD - 116)) | (1L << (LSHIFT - 116)) | (1L << (RSHIFT - 116)) | (1L << (BIT_CLEAR - 116)) | (1L << (PLUS - 116)) | (1L << (MINUS - 116)) | (1L << (CARET - 116)) | (1L << (STAR - 116)) | (1L << (AMPERSAND - 116)))) != 0)) {
				{
				setState(1143);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 116)) & ~0x3f) == 0 && ((1L << (_la - 116)) & ((1L << (OR - 116)) | (1L << (DIV - 116)) | (1L << (MOD - 116)) | (1L << (LSHIFT - 116)) | (1L << (RSHIFT - 116)) | (1L << (BIT_CLEAR - 116)) | (1L << (PLUS - 116)) | (1L << (MINUS - 116)) | (1L << (CARET - 116)) | (1L << (STAR - 116)) | (1L << (AMPERSAND - 116)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1146);
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
		enterRule(_localctx, 190, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1148);
			match(PACKAGE);
			setState(1149);
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
		enterRule(_localctx, 192, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1151);
			match(IMPORT);
			setState(1163);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1152);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1153);
				match(L_PAREN);
				setState(1159);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 92)) & ~0x3f) == 0 && ((1L << (_la - 92)) & ((1L << (IDENTIFIER - 92)) | (1L << (DOT - 92)) | (1L << (RAW_STRING_LIT - 92)) | (1L << (INTERPRETED_STRING_LIT - 92)))) != 0)) {
					{
					{
					setState(1154);
					importSpec();
					setState(1155);
					eos();
					}
					}
					setState(1161);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1162);
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
		enterRule(_localctx, 194, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1165);
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

			setState(1168);
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
		enterRule(_localctx, 196, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1170);
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
		enterRule(_localctx, 198, RULE_declaration);
		try {
			setState(1175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1172);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1173);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1174);
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
		enterRule(_localctx, 200, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1177);
			match(CONST);
			setState(1189);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1178);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1179);
				match(L_PAREN);
				setState(1185);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1180);
					constSpec();
					setState(1181);
					eos();
					}
					}
					setState(1187);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1188);
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
		enterRule(_localctx, 202, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1191);
			identifierList();
			setState(1197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				{
				setState(1193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (FUNC - 68)) | (1L << (INTERFACE - 68)) | (1L << (MAP - 68)) | (1L << (STRUCT - 68)) | (1L << (CHAN - 68)) | (1L << (IDENTIFIER - 68)) | (1L << (L_PAREN - 68)) | (1L << (L_BRACKET - 68)) | (1L << (STAR - 68)) | (1L << (RECEIVE - 68)))) != 0)) {
					{
					setState(1192);
					type_();
					}
				}

				setState(1195);
				match(ASSIGN);
				setState(1196);
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
		enterRule(_localctx, 204, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(IDENTIFIER);
			setState(1204);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1200);
					match(COMMA);
					setState(1201);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1206);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
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
		enterRule(_localctx, 206, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			expression(0);
			setState(1212);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,103,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1208);
					match(COMMA);
					setState(1209);
					expression(0);
					}
					} 
				}
				setState(1214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,103,_ctx);
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
		enterRule(_localctx, 208, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1215);
			match(TYPE);
			setState(1227);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1216);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1217);
				match(L_PAREN);
				setState(1223);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1218);
					typeSpec();
					setState(1219);
					eos();
					}
					}
					setState(1225);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1226);
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
		enterRule(_localctx, 210, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1229);
			match(IDENTIFIER);
			setState(1231);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1230);
				match(ASSIGN);
				}
			}

			setState(1233);
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
		enterRule(_localctx, 212, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			match(VAR);
			setState(1247);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1236);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1237);
				match(L_PAREN);
				setState(1243);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1238);
					varSpec();
					setState(1239);
					eos();
					}
					}
					setState(1245);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1246);
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
		enterRule(_localctx, 214, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1249);
			match(L_CURLY);
			setState(1251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1250);
				statementList();
				}
				break;
			}
			setState(1253);
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
		enterRule(_localctx, 216, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1261); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1256);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
					case 1:
						{
						setState(1255);
						eos();
						}
						break;
					}
					setState(1258);
					statement();
					setState(1259);
					eos();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1263); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,111,_ctx);
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
		enterRule(_localctx, 218, RULE_simpleStmt);
		try {
			setState(1270);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1265);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1266);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1267);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1268);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1269);
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
		enterRule(_localctx, 220, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1272);
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
		enterRule(_localctx, 222, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1274);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1275);
			match(RECEIVE);
			setState(1276);
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
		enterRule(_localctx, 224, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1278);
			expression(0);
			setState(1279);
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
		enterRule(_localctx, 226, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1281);
			expressionList();
			setState(1282);
			assign_op();
			setState(1283);
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
		enterRule(_localctx, 228, RULE_emptyStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1285);
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
		enterRule(_localctx, 230, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1287);
			match(IDENTIFIER);
			setState(1288);
			match(COLON);
			setState(1290);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				{
				setState(1289);
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
		enterRule(_localctx, 232, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1292);
			match(RETURN);
			setState(1294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
			case 1:
				{
				setState(1293);
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
		enterRule(_localctx, 234, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1296);
			match(BREAK);
			setState(1298);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				{
				setState(1297);
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
		enterRule(_localctx, 236, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1300);
			match(CONTINUE);
			setState(1302);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1301);
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
		enterRule(_localctx, 238, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1304);
			match(GOTO);
			setState(1305);
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
		enterRule(_localctx, 240, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1307);
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
		enterRule(_localctx, 242, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1309);
			match(IF);
			setState(1318);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				{
				setState(1310);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1311);
				eos();
				setState(1312);
				expression(0);
				}
				break;
			case 3:
				{
				setState(1314);
				simpleStmt();
				setState(1315);
				eos();
				setState(1316);
				expression(0);
				}
				break;
			}
			setState(1320);
			block();
			setState(1326);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
			case 1:
				{
				setState(1321);
				match(ELSE);
				setState(1324);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1322);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1323);
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
		enterRule(_localctx, 244, RULE_switchStmt);
		try {
			setState(1330);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1328);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1329);
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
		enterRule(_localctx, 246, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1332);
			match(SWITCH);
			setState(1343);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1334);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(1333);
					expression(0);
					}
				}

				}
				break;
			case 2:
				{
				setState(1337);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
				case 1:
					{
					setState(1336);
					simpleStmt();
					}
					break;
				}
				setState(1339);
				eos();
				setState(1341);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(1340);
					expression(0);
					}
				}

				}
				break;
			}
			setState(1345);
			match(L_CURLY);
			setState(1349);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1346);
				exprCaseClause();
				}
				}
				setState(1351);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1352);
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
		enterRule(_localctx, 248, RULE_exprCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1354);
			exprSwitchCase();
			setState(1355);
			match(COLON);
			setState(1357);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
			case 1:
				{
				setState(1356);
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
		enterRule(_localctx, 250, RULE_exprSwitchCase);
		try {
			setState(1362);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1359);
				match(CASE);
				setState(1360);
				expressionList();
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
		enterRule(_localctx, 252, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1364);
			match(SWITCH);
			setState(1373);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				{
				setState(1365);
				typeSwitchGuard();
				}
				break;
			case 2:
				{
				setState(1366);
				eos();
				setState(1367);
				typeSwitchGuard();
				}
				break;
			case 3:
				{
				setState(1369);
				simpleStmt();
				setState(1370);
				eos();
				setState(1371);
				typeSwitchGuard();
				}
				break;
			}
			setState(1375);
			match(L_CURLY);
			setState(1379);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1376);
				typeCaseClause();
				}
				}
				setState(1381);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1382);
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
		enterRule(_localctx, 254, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1386);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1384);
				match(IDENTIFIER);
				setState(1385);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1388);
			primaryExpr(0);
			setState(1389);
			match(DOT);
			setState(1390);
			match(L_PAREN);
			setState(1391);
			match(TYPE);
			setState(1392);
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
		enterRule(_localctx, 256, RULE_typeCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1394);
			typeSwitchCase();
			setState(1395);
			match(COLON);
			setState(1397);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1396);
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
		enterRule(_localctx, 258, RULE_typeSwitchCase);
		try {
			setState(1402);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1399);
				match(CASE);
				setState(1400);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1401);
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
		enterRule(_localctx, 260, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1406);
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
				setState(1404);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1405);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1415);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1408);
				match(COMMA);
				setState(1411);
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
					setState(1409);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1410);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1417);
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
		enterRule(_localctx, 262, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
			match(SELECT);
			setState(1419);
			match(L_CURLY);
			setState(1423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1420);
				commClause();
				}
				}
				setState(1425);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1426);
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
		enterRule(_localctx, 264, RULE_commClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1428);
			commCase();
			setState(1429);
			match(COLON);
			setState(1431);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				{
				setState(1430);
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
		enterRule(_localctx, 266, RULE_commCase);
		try {
			setState(1439);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1433);
				match(CASE);
				setState(1436);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
				case 1:
					{
					setState(1434);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1435);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1438);
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
		enterRule(_localctx, 268, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1447);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				{
				setState(1441);
				expressionList();
				setState(1442);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1444);
				identifierList();
				setState(1445);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1449);
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
		enterRule(_localctx, 270, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1451);
			match(FOR);
			setState(1455);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				{
				setState(1452);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1453);
				forClause();
				}
				break;
			case 3:
				{
				setState(1454);
				rangeClause();
				}
				break;
			}
			setState(1457);
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
		enterRule(_localctx, 272, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1460);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
			case 1:
				{
				setState(1459);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1462);
			eos();
			setState(1464);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
			case 1:
				{
				setState(1463);
				expression(0);
				}
				break;
			}
			setState(1466);
			eos();
			setState(1468);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1467);
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
		enterRule(_localctx, 274, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1476);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				{
				setState(1470);
				expressionList();
				setState(1471);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1473);
				identifierList();
				setState(1474);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1478);
			match(RANGE);
			setState(1479);
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
		enterRule(_localctx, 276, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1481);
			match(GO);
			setState(1482);
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
		enterRule(_localctx, 278, RULE_typeName);
		try {
			setState(1486);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1484);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1485);
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
		enterRule(_localctx, 280, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1488);
			match(L_BRACKET);
			setState(1489);
			arrayLength();
			setState(1490);
			match(R_BRACKET);
			setState(1491);
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
		enterRule(_localctx, 282, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1493);
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
		enterRule(_localctx, 284, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1495);
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
		enterRule(_localctx, 286, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1497);
			match(STAR);
			setState(1498);
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
		enterRule(_localctx, 288, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1500);
			match(L_BRACKET);
			setState(1501);
			match(R_BRACKET);
			setState(1502);
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
		enterRule(_localctx, 290, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1504);
			match(MAP);
			setState(1505);
			match(L_BRACKET);
			setState(1506);
			type_();
			setState(1507);
			match(R_BRACKET);
			setState(1508);
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
		enterRule(_localctx, 292, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1515);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
			case 1:
				{
				setState(1510);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1511);
				match(CHAN);
				setState(1512);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1513);
				match(RECEIVE);
				setState(1514);
				match(CHAN);
				}
				break;
			}
			setState(1517);
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
		enterRule(_localctx, 294, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1519);
			match(FUNC);
			setState(1520);
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
		enterRule(_localctx, 296, RULE_signature);
		try {
			setState(1526);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1522);
				parameters();
				setState(1523);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1525);
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
		enterRule(_localctx, 298, RULE_result);
		try {
			setState(1530);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1528);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1529);
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
		enterRule(_localctx, 300, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1532);
			match(L_PAREN);
			setState(1544);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (FUNC - 68)) | (1L << (INTERFACE - 68)) | (1L << (MAP - 68)) | (1L << (STRUCT - 68)) | (1L << (CHAN - 68)) | (1L << (IDENTIFIER - 68)) | (1L << (L_PAREN - 68)) | (1L << (L_BRACKET - 68)) | (1L << (ELLIPSIS - 68)) | (1L << (STAR - 68)) | (1L << (RECEIVE - 68)))) != 0)) {
				{
				setState(1533);
				parameterDecl();
				setState(1538);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1534);
						match(COMMA);
						setState(1535);
						parameterDecl();
						}
						} 
					}
					setState(1540);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
				}
				setState(1542);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1541);
					match(COMMA);
					}
				}

				}
			}

			setState(1546);
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
		enterRule(_localctx, 302, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1548);
			nonNamedType();
			setState(1549);
			match(L_PAREN);
			setState(1550);
			expression(0);
			setState(1552);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1551);
				match(COMMA);
				}
			}

			setState(1554);
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
		enterRule(_localctx, 304, RULE_nonNamedType);
		try {
			setState(1561);
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
				setState(1556);
				typeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1557);
				match(L_PAREN);
				setState(1558);
				nonNamedType();
				setState(1559);
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
		enterRule(_localctx, 306, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1563);
			_la = _input.LA(1);
			if ( !(((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (DECIMAL_LIT - 129)) | (1L << (BINARY_LIT - 129)) | (1L << (OCTAL_LIT - 129)) | (1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)))) != 0)) ) {
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
		enterRule(_localctx, 308, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1565);
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
		enterRule(_localctx, 310, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1567);
			match(IDENTIFIER);
			setState(1568);
			match(DOT);
			setState(1569);
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
		enterRule(_localctx, 312, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1571);
			literalType();
			setState(1572);
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
		enterRule(_localctx, 314, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1574);
			match(L_CURLY);
			setState(1579);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1575);
				elementList();
				setState(1577);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1576);
					match(COMMA);
					}
				}

				}
			}

			setState(1581);
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
		enterRule(_localctx, 316, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1583);
			keyedElement();
			setState(1588);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,157,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1584);
					match(COMMA);
					setState(1585);
					keyedElement();
					}
					} 
				}
				setState(1590);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,157,_ctx);
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
		enterRule(_localctx, 318, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1594);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
			case 1:
				{
				setState(1591);
				key();
				setState(1592);
				match(COLON);
				}
				break;
			}
			setState(1596);
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
		enterRule(_localctx, 320, RULE_key);
		try {
			setState(1600);
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
				setState(1598);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1599);
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
		enterRule(_localctx, 322, RULE_element);
		try {
			setState(1604);
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
				setState(1602);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1603);
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
		enterRule(_localctx, 324, RULE_structType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1606);
			match(STRUCT);
			setState(1607);
			match(L_CURLY);
			setState(1613);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(1608);
				fieldDecl();
				setState(1609);
				eos();
				}
				}
				setState(1615);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1616);
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
		enterRule(_localctx, 326, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1622);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
			case 1:
				{
				setState(1618);
				identifierList();
				setState(1619);
				type_();
				}
				break;
			case 2:
				{
				setState(1621);
				embeddedField();
				}
				break;
			}
			setState(1625);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,163,_ctx) ) {
			case 1:
				{
				setState(1624);
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
		enterRule(_localctx, 328, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1627);
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
		enterRule(_localctx, 330, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1629);
				match(STAR);
				}
			}

			setState(1632);
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
		enterRule(_localctx, 332, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1634);
			match(L_BRACKET);
			setState(1635);
			expression(0);
			setState(1636);
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
		enterRule(_localctx, 334, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1638);
			match(DOT);
			setState(1639);
			match(L_PAREN);
			setState(1640);
			type_();
			setState(1641);
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
		enterRule(_localctx, 336, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1643);
			match(L_PAREN);
			setState(1658);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1650);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
				case 1:
					{
					setState(1644);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1645);
					nonNamedType();
					setState(1648);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
					case 1:
						{
						setState(1646);
						match(COMMA);
						setState(1647);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1653);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1652);
					match(ELLIPSIS);
					}
				}

				setState(1656);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1655);
					match(COMMA);
					}
				}

				}
			}

			setState(1660);
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
		enterRule(_localctx, 338, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1662);
			nonNamedType();
			setState(1663);
			match(DOT);
			setState(1664);
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
		enterRule(_localctx, 340, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1666);
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
		enterRule(_localctx, 342, RULE_eos);
		try {
			setState(1672);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1668);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1669);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1670);
				match(EOS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1671);
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
		case 67:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 75:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 171:
			return eos_sempred((EosContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 6);
		case 6:
			return precpred(_ctx, 5);
		case 7:
			return precpred(_ctx, 4);
		case 8:
			return precpred(_ctx, 3);
		case 9:
			return precpred(_ctx, 7);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0099\u068d\4\2\t"+
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
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u0167\n\5\f\5\16"+
		"\5\u016a\13\5\3\6\3\6\5\6\u016e\n\6\3\7\3\7\3\7\3\7\3\7\7\7\u0175\n\7"+
		"\f\7\16\7\u0178\13\7\3\7\3\7\3\7\5\7\u017d\n\7\3\7\3\7\7\7\u0181\n\7\f"+
		"\7\16\7\u0184\13\7\3\7\3\7\3\b\3\b\3\b\3\b\5\b\u018c\n\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\5\t\u0194\n\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\5\f\u019e"+
		"\n\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u01ae"+
		"\n\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\7\20\u01ba\n\20"+
		"\f\20\16\20\u01bd\13\20\3\20\5\20\u01c0\n\20\3\21\3\21\3\21\7\21\u01c5"+
		"\n\21\f\21\16\21\u01c8\13\21\3\21\3\21\3\22\7\22\u01cd\n\22\f\22\16\22"+
		"\u01d0\13\22\3\23\3\23\3\23\3\23\7\23\u01d6\n\23\f\23\16\23\u01d9\13\23"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31"+
		"\3\31\5\31\u01f8\n\31\3\31\3\31\3\31\3\31\3\32\3\32\5\32\u0200\n\32\3"+
		"\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3"+
		"\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\5\37\u0218\n\37\3\37\3\37\3 \3"+
		" \3 \3 \3 \3 \3 \3!\3!\3!\3!\7!\u0227\n!\f!\16!\u022a\13!\3!\3!\3\"\3"+
		"\"\3\"\3\"\3#\3#\3#\5#\u0235\n#\3$\3$\3$\3$\3$\7$\u023c\n$\f$\16$\u023f"+
		"\13$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u024c\n%\3&\3&\3&\3&\3&\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u025e\n\'\3(\3(\3(\3(\3(\5"+
		"(\u0265\n(\3(\7(\u0268\n(\f(\16(\u026b\13(\3(\3(\5(\u026f\n(\3)\3)\3)"+
		"\3)\3)\3)\3)\3)\5)\u0279\n)\3*\5*\u027c\n*\3*\3*\5*\u0280\n*\3+\3+\5+"+
		"\u0284\n+\3,\3,\3,\3,\3,\5,\u028b\n,\3,\5,\u028e\n,\3,\3,\3-\3-\3-\3-"+
		"\5-\u0296\n-\5-\u0298\n-\3-\5-\u029b\n-\3.\3.\3.\7.\u02a0\n.\f.\16.\u02a3"+
		"\13.\3/\3/\5/\u02a7\n/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\7\60\u02b2"+
		"\n\60\f\60\16\60\u02b5\13\60\3\60\3\60\3\60\7\60\u02ba\n\60\f\60\16\60"+
		"\u02bd\13\60\3\60\5\60\u02c0\n\60\3\61\5\61\u02c3\n\61\3\61\3\61\3\61"+
		"\3\61\5\61\u02c9\n\61\3\62\3\62\5\62\u02cd\n\62\3\62\5\62\u02d0\n\62\3"+
		"\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63\5\63\u02da\n\63\3\64\3\64\3\64"+
		"\3\64\3\64\5\64\u02e1\n\64\3\65\3\65\3\65\3\65\3\65\5\65\u02e8\n\65\3"+
		"\65\3\65\3\66\3\66\3\66\3\66\3\66\3\67\3\67\3\67\5\67\u02f4\n\67\38\3"+
		"8\38\38\58\u02fa\n8\39\39\39\39\39\59\u0301\n9\3:\3:\3:\5:\u0306\n:\3"+
		";\3;\3;\3;\5;\u030c\n;\3<\3<\3<\3<\3<\3=\3=\3=\3=\3=\5=\u0318\n=\3>\3"+
		">\3>\3>\5>\u031e\n>\3>\3>\5>\u0322\n>\3?\3?\3?\3?\3@\3@\5@\u032a\n@\3"+
		"@\3@\5@\u032e\n@\3@\3@\3A\3A\5A\u0334\nA\3B\5B\u0337\nB\3B\3B\3C\3C\5"+
		"C\u033d\nC\3C\3C\3D\5D\u0342\nD\3D\3D\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3"+
		"E\3E\3E\3E\3E\3E\5E\u0356\nE\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3"+
		"E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\3E\7E\u0379\n"+
		"E\fE\16E\u037c\13E\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F\3F"+
		"\3F\3F\5F\u0391\nF\3G\3G\3G\3H\3H\3H\5H\u0399\nH\3I\3I\3I\3J\3J\3J\3J"+
		"\7J\u03a2\nJ\fJ\16J\u03a5\13J\3J\3J\3J\3J\5J\u03ab\nJ\3K\3K\3K\3K\3K\5"+
		"K\u03b2\nK\3L\3L\3L\3L\3L\3L\3L\3L\5L\u03bc\nL\3M\3M\3M\3M\3M\3M\3M\3"+
		"M\3M\3M\3M\3M\5M\u03ca\nM\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3"+
		"M\3M\3M\3M\3M\3M\7M\u03e0\nM\fM\16M\u03e3\13M\3N\3N\3N\3N\3N\3N\5N\u03eb"+
		"\nN\3O\3O\3O\5O\u03f0\nO\3P\3P\3P\3Q\3Q\5Q\u03f7\nQ\3Q\3Q\5Q\u03fb\nQ"+
		"\3R\3R\5R\u03ff\nR\3R\5R\u0402\nR\3R\3R\3S\3S\3S\3S\3S\5S\u040b\nS\3S"+
		"\3S\7S\u040f\nS\fS\16S\u0412\13S\3S\3S\3T\3T\3T\3T\3U\5U\u041b\nU\3U\3"+
		"U\3U\3U\3U\3U\5U\u0423\nU\3U\3U\3U\3U\5U\u0429\nU\3V\3V\3V\3V\3V\3V\3"+
		"V\5V\u0432\nV\3W\3W\3W\3W\3W\3W\3W\3W\3W\5W\u043d\nW\3X\3X\3X\3Y\3Y\3"+
		"Y\3Y\7Y\u0446\nY\fY\16Y\u0449\13Y\3Y\5Y\u044c\nY\5Y\u044e\nY\3Y\3Y\3Z"+
		"\3Z\3Z\3Z\3Z\3Z\3Z\5Z\u0459\nZ\3[\3[\3[\3[\3[\3\\\3\\\5\\\u0462\n\\\3"+
		"\\\3\\\5\\\u0466\n\\\3\\\5\\\u0469\n\\\3\\\3\\\3\\\3\\\3\\\5\\\u0470\n"+
		"\\\3\\\3\\\3]\3]\3^\3^\3_\3_\3`\5`\u047b\n`\3`\3`\3a\3a\3a\3b\3b\3b\3"+
		"b\3b\3b\7b\u0488\nb\fb\16b\u048b\13b\3b\5b\u048e\nb\3c\5c\u0491\nc\3c"+
		"\3c\3d\3d\3e\3e\3e\5e\u049a\ne\3f\3f\3f\3f\3f\3f\7f\u04a2\nf\ff\16f\u04a5"+
		"\13f\3f\5f\u04a8\nf\3g\3g\5g\u04ac\ng\3g\3g\5g\u04b0\ng\3h\3h\3h\7h\u04b5"+
		"\nh\fh\16h\u04b8\13h\3i\3i\3i\7i\u04bd\ni\fi\16i\u04c0\13i\3j\3j\3j\3"+
		"j\3j\3j\7j\u04c8\nj\fj\16j\u04cb\13j\3j\5j\u04ce\nj\3k\3k\5k\u04d2\nk"+
		"\3k\3k\3l\3l\3l\3l\3l\3l\7l\u04dc\nl\fl\16l\u04df\13l\3l\5l\u04e2\nl\3"+
		"m\3m\5m\u04e6\nm\3m\3m\3n\5n\u04eb\nn\3n\3n\3n\6n\u04f0\nn\rn\16n\u04f1"+
		"\3o\3o\3o\3o\3o\5o\u04f9\no\3p\3p\3q\3q\3q\3q\3r\3r\3r\3s\3s\3s\3s\3t"+
		"\3t\3u\3u\3u\5u\u050d\nu\3v\3v\5v\u0511\nv\3w\3w\5w\u0515\nw\3x\3x\5x"+
		"\u0519\nx\3y\3y\3y\3z\3z\3{\3{\3{\3{\3{\3{\3{\3{\3{\5{\u0529\n{\3{\3{"+
		"\3{\3{\5{\u052f\n{\5{\u0531\n{\3|\3|\5|\u0535\n|\3}\3}\5}\u0539\n}\3}"+
		"\5}\u053c\n}\3}\3}\5}\u0540\n}\5}\u0542\n}\3}\3}\7}\u0546\n}\f}\16}\u0549"+
		"\13}\3}\3}\3~\3~\3~\5~\u0550\n~\3\177\3\177\3\177\5\177\u0555\n\177\3"+
		"\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080"+
		"\5\u0080\u0560\n\u0080\3\u0080\3\u0080\7\u0080\u0564\n\u0080\f\u0080\16"+
		"\u0080\u0567\13\u0080\3\u0080\3\u0080\3\u0081\3\u0081\5\u0081\u056d\n"+
		"\u0081\3\u0081\3\u0081\3\u0081\3\u0081\3\u0081\3\u0081\3\u0082\3\u0082"+
		"\3\u0082\5\u0082\u0578\n\u0082\3\u0083\3\u0083\3\u0083\5\u0083\u057d\n"+
		"\u0083\3\u0084\3\u0084\5\u0084\u0581\n\u0084\3\u0084\3\u0084\3\u0084\5"+
		"\u0084\u0586\n\u0084\7\u0084\u0588\n\u0084\f\u0084\16\u0084\u058b\13\u0084"+
		"\3\u0085\3\u0085\3\u0085\7\u0085\u0590\n\u0085\f\u0085\16\u0085\u0593"+
		"\13\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086\5\u0086\u059a\n\u0086"+
		"\3\u0087\3\u0087\3\u0087\5\u0087\u059f\n\u0087\3\u0087\5\u0087\u05a2\n"+
		"\u0087\3\u0088\3\u0088\3\u0088\3\u0088\3\u0088\3\u0088\5\u0088\u05aa\n"+
		"\u0088\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u0089\5\u0089\u05b2\n"+
		"\u0089\3\u0089\3\u0089\3\u008a\5\u008a\u05b7\n\u008a\3\u008a\3\u008a\5"+
		"\u008a\u05bb\n\u008a\3\u008a\3\u008a\5\u008a\u05bf\n\u008a\3\u008b\3\u008b"+
		"\3\u008b\3\u008b\3\u008b\3\u008b\5\u008b\u05c7\n\u008b\3\u008b\3\u008b"+
		"\3\u008b\3\u008c\3\u008c\3\u008c\3\u008d\3\u008d\5\u008d\u05d1\n\u008d"+
		"\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008f\3\u008f\3\u0090\3\u0090"+
		"\3\u0091\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092\3\u0092\3\u0093\3\u0093"+
		"\3\u0093\3\u0093\3\u0093\3\u0093\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094"+
		"\5\u0094\u05ee\n\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3\u0095\3\u0096"+
		"\3\u0096\3\u0096\3\u0096\5\u0096\u05f9\n\u0096\3\u0097\3\u0097\5\u0097"+
		"\u05fd\n\u0097\3\u0098\3\u0098\3\u0098\3\u0098\7\u0098\u0603\n\u0098\f"+
		"\u0098\16\u0098\u0606\13\u0098\3\u0098\5\u0098\u0609\n\u0098\5\u0098\u060b"+
		"\n\u0098\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\3\u0099\5\u0099\u0613"+
		"\n\u0099\3\u0099\3\u0099\3\u009a\3\u009a\3\u009a\3\u009a\3\u009a\5\u009a"+
		"\u061c\n\u009a\3\u009b\3\u009b\3\u009c\3\u009c\3\u009d\3\u009d\3\u009d"+
		"\3\u009d\3\u009e\3\u009e\3\u009e\3\u009f\3\u009f\3\u009f\5\u009f\u062c"+
		"\n\u009f\5\u009f\u062e\n\u009f\3\u009f\3\u009f\3\u00a0\3\u00a0\3\u00a0"+
		"\7\u00a0\u0635\n\u00a0\f\u00a0\16\u00a0\u0638\13\u00a0\3\u00a1\3\u00a1"+
		"\3\u00a1\5\u00a1\u063d\n\u00a1\3\u00a1\3\u00a1\3\u00a2\3\u00a2\5\u00a2"+
		"\u0643\n\u00a2\3\u00a3\3\u00a3\5\u00a3\u0647\n\u00a3\3\u00a4\3\u00a4\3"+
		"\u00a4\3\u00a4\3\u00a4\7\u00a4\u064e\n\u00a4\f\u00a4\16\u00a4\u0651\13"+
		"\u00a4\3\u00a4\3\u00a4\3\u00a5\3\u00a5\3\u00a5\3\u00a5\5\u00a5\u0659\n"+
		"\u00a5\3\u00a5\5\u00a5\u065c\n\u00a5\3\u00a6\3\u00a6\3\u00a7\5\u00a7\u0661"+
		"\n\u00a7\3\u00a7\3\u00a7\3\u00a8\3\u00a8\3\u00a8\3\u00a8\3\u00a9\3\u00a9"+
		"\3\u00a9\3\u00a9\3\u00a9\3\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00aa\5\u00aa"+
		"\u0673\n\u00aa\5\u00aa\u0675\n\u00aa\3\u00aa\5\u00aa\u0678\n\u00aa\3\u00aa"+
		"\5\u00aa\u067b\n\u00aa\5\u00aa\u067d\n\u00aa\3\u00aa\3\u00aa\3\u00ab\3"+
		"\u00ab\3\u00ab\3\u00ab\3\u00ac\3\u00ac\3\u00ad\3\u00ad\3\u00ad\3\u00ad"+
		"\5\u00ad\u068b\n\u00ad\3\u00ad\3\u0269\4\u0088\u0098\u00ae\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^"+
		"`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"+
		"\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120"+
		"\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138"+
		"\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c\u014e\u0150"+
		"\u0152\u0154\u0156\u0158\2\25\3\2\31\32\3\2\7\n\3\2@A\3\2)+\4\2)+--\3"+
		"\2|\u0082\3\2\26\27\4\2w{\u0080\u0081\6\2$$jjvv}\177\3\2 \"\3\2\35\37"+
		"\3\2pu\6\2..\61\61\64\64VV\4\2v{}\u0081\4\2^^ii\3\2jk\4\2gg\u0098\u0098"+
		"\4\2\u0083\u0086\u0088\u0089\3\2\u008f\u0090\2\u06e7\2\u015a\3\2\2\2\4"+
		"\u015d\3\2\2\2\6\u0160\3\2\2\2\b\u0163\3\2\2\2\n\u016b\3\2\2\2\f\u016f"+
		"\3\2\2\2\16\u018b\3\2\2\2\20\u0193\3\2\2\2\22\u0195\3\2\2\2\24\u0197\3"+
		"\2\2\2\26\u019a\3\2\2\2\30\u01ad\3\2\2\2\32\u01af\3\2\2\2\34\u01b1\3\2"+
		"\2\2\36\u01b6\3\2\2\2 \u01c1\3\2\2\2\"\u01ce\3\2\2\2$\u01d1\3\2\2\2&\u01dc"+
		"\3\2\2\2(\u01de\3\2\2\2*\u01e3\3\2\2\2,\u01e8\3\2\2\2.\u01ed\3\2\2\2\60"+
		"\u01f2\3\2\2\2\62\u01ff\3\2\2\2\64\u0201\3\2\2\2\66\u0203\3\2\2\28\u0208"+
		"\3\2\2\2:\u020d\3\2\2\2<\u0212\3\2\2\2>\u021b\3\2\2\2@\u0222\3\2\2\2B"+
		"\u022d\3\2\2\2D\u0234\3\2\2\2F\u0236\3\2\2\2H\u024b\3\2\2\2J\u024d\3\2"+
		"\2\2L\u025d\3\2\2\2N\u0269\3\2\2\2P\u0278\3\2\2\2R\u027b\3\2\2\2T\u0283"+
		"\3\2\2\2V\u0285\3\2\2\2X\u0291\3\2\2\2Z\u029c\3\2\2\2\\\u02a6\3\2\2\2"+
		"^\u02aa\3\2\2\2`\u02c2\3\2\2\2b\u02ca\3\2\2\2d\u02d9\3\2\2\2f\u02db\3"+
		"\2\2\2h\u02e2\3\2\2\2j\u02eb\3\2\2\2l\u02f0\3\2\2\2n\u02f5\3\2\2\2p\u02fb"+
		"\3\2\2\2r\u0302\3\2\2\2t\u0307\3\2\2\2v\u030d\3\2\2\2x\u0312\3\2\2\2z"+
		"\u0319\3\2\2\2|\u0323\3\2\2\2~\u0327\3\2\2\2\u0080\u0333\3\2\2\2\u0082"+
		"\u0336\3\2\2\2\u0084\u033a\3\2\2\2\u0086\u0341\3\2\2\2\u0088\u0355\3\2"+
		"\2\2\u008a\u0390\3\2\2\2\u008c\u0392\3\2\2\2\u008e\u0395\3\2\2\2\u0090"+
		"\u039a\3\2\2\2\u0092\u03a3\3\2\2\2\u0094\u03b1\3\2\2\2\u0096\u03bb\3\2"+
		"\2\2\u0098\u03c9\3\2\2\2\u009a\u03ea\3\2\2\2\u009c\u03ef\3\2\2\2\u009e"+
		"\u03f1\3\2\2\2\u00a0\u03f4\3\2\2\2\u00a2\u03fc\3\2\2\2\u00a4\u0405\3\2"+
		"\2\2\u00a6\u0415\3\2\2\2\u00a8\u0428\3\2\2\2\u00aa\u0431\3\2\2\2\u00ac"+
		"\u043c\3\2\2\2\u00ae\u043e\3\2\2\2\u00b0\u0441\3\2\2\2\u00b2\u0458\3\2"+
		"\2\2\u00b4\u045a\3\2\2\2\u00b6\u045f\3\2\2\2\u00b8\u0473\3\2\2\2\u00ba"+
		"\u0475\3\2\2\2\u00bc\u0477\3\2\2\2\u00be\u047a\3\2\2\2\u00c0\u047e\3\2"+
		"\2\2\u00c2\u0481\3\2\2\2\u00c4\u0490\3\2\2\2\u00c6\u0494\3\2\2\2\u00c8"+
		"\u0499\3\2\2\2\u00ca\u049b\3\2\2\2\u00cc\u04a9\3\2\2\2\u00ce\u04b1\3\2"+
		"\2\2\u00d0\u04b9\3\2\2\2\u00d2\u04c1\3\2\2\2\u00d4\u04cf\3\2\2\2\u00d6"+
		"\u04d5\3\2\2\2\u00d8\u04e3\3\2\2\2\u00da\u04ef\3\2\2\2\u00dc\u04f8\3\2"+
		"\2\2\u00de\u04fa\3\2\2\2\u00e0\u04fc\3\2\2\2\u00e2\u0500\3\2\2\2\u00e4"+
		"\u0503\3\2\2\2\u00e6\u0507\3\2\2\2\u00e8\u0509\3\2\2\2\u00ea\u050e\3\2"+
		"\2\2\u00ec\u0512\3\2\2\2\u00ee\u0516\3\2\2\2\u00f0\u051a\3\2\2\2\u00f2"+
		"\u051d\3\2\2\2\u00f4\u051f\3\2\2\2\u00f6\u0534\3\2\2\2\u00f8\u0536\3\2"+
		"\2\2\u00fa\u054c\3\2\2\2\u00fc\u0554\3\2\2\2\u00fe\u0556\3\2\2\2\u0100"+
		"\u056c\3\2\2\2\u0102\u0574\3\2\2\2\u0104\u057c\3\2\2\2\u0106\u0580\3\2"+
		"\2\2\u0108\u058c\3\2\2\2\u010a\u0596\3\2\2\2\u010c\u05a1\3\2\2\2\u010e"+
		"\u05a9\3\2\2\2\u0110\u05ad\3\2\2\2\u0112\u05b6\3\2\2\2\u0114\u05c6\3\2"+
		"\2\2\u0116\u05cb\3\2\2\2\u0118\u05d0\3\2\2\2\u011a\u05d2\3\2\2\2\u011c"+
		"\u05d7\3\2\2\2\u011e\u05d9\3\2\2\2\u0120\u05db\3\2\2\2\u0122\u05de\3\2"+
		"\2\2\u0124\u05e2\3\2\2\2\u0126\u05ed\3\2\2\2\u0128\u05f1\3\2\2\2\u012a"+
		"\u05f8\3\2\2\2\u012c\u05fc\3\2\2\2\u012e\u05fe\3\2\2\2\u0130\u060e\3\2"+
		"\2\2\u0132\u061b\3\2\2\2\u0134\u061d\3\2\2\2\u0136\u061f\3\2\2\2\u0138"+
		"\u0621\3\2\2\2\u013a\u0625\3\2\2\2\u013c\u0628\3\2\2\2\u013e\u0631\3\2"+
		"\2\2\u0140\u063c\3\2\2\2\u0142\u0642\3\2\2\2\u0144\u0646\3\2\2\2\u0146"+
		"\u0648\3\2\2\2\u0148\u0658\3\2\2\2\u014a\u065d\3\2\2\2\u014c\u0660\3\2"+
		"\2\2\u014e\u0664\3\2\2\2\u0150\u0668\3\2\2\2\u0152\u066d\3\2\2\2\u0154"+
		"\u0680\3\2\2\2\u0156\u0684\3\2\2\2\u0158\u068a\3\2\2\2\u015a\u015b\5\u0088"+
		"E\2\u015b\u015c\7\2\2\3\u015c\3\3\2\2\2\u015d\u015e\5\u008aF\2\u015e\u015f"+
		"\7\2\2\3\u015f\5\3\2\2\2\u0160\u0161\5\u00aaV\2\u0161\u0162\7\2\2\3\u0162"+
		"\7\3\2\2\2\u0163\u0168\5\n\6\2\u0164\u0165\7f\2\2\u0165\u0167\5\n\6\2"+
		"\u0166\u0164\3\2\2\2\u0167\u016a\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169"+
		"\3\2\2\2\u0169\t\3\2\2\2\u016a\u0168\3\2\2\2\u016b\u016d\7^\2\2\u016c"+
		"\u016e\7;\2\2\u016d\u016c\3\2\2\2\u016d\u016e\3\2\2\2\u016e\13\3\2\2\2"+
		"\u016f\u0170\5\u00c0a\2\u0170\u0176\5\u0158\u00ad\2\u0171\u0172\5\u00c2"+
		"b\2\u0172\u0173\5\u0158\u00ad\2\u0173\u0175\3\2\2\2\u0174\u0171\3\2\2"+
		"\2\u0175\u0178\3\2\2\2\u0176\u0174\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u0182"+
		"\3\2\2\2\u0178\u0176\3\2\2\2\u0179\u017d\5l\67\2\u017a\u017d\5\u00c8e"+
		"\2\u017b\u017d\5\16\b\2\u017c\u0179\3\2\2\2\u017c\u017a\3\2\2\2\u017c"+
		"\u017b\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u017f\5\u0158\u00ad\2\u017f\u0181"+
		"\3\2\2\2\u0180\u017c\3\2\2\2\u0181\u0184\3\2\2\2\u0182\u0180\3\2\2\2\u0182"+
		"\u0183\3\2\2\2\u0183\u0185\3\2\2\2\u0184\u0182\3\2\2\2\u0185\u0186\7\2"+
		"\2\3\u0186\r\3\2\2\2\u0187\u018c\5^\60\2\u0188\u018c\5t;\2\u0189\u018c"+
		"\5x=\2\u018a\u018c\5r:\2\u018b\u0187\3\2\2\2\u018b\u0188\3\2\2\2\u018b"+
		"\u0189\3\2\2\2\u018b\u018a\3\2\2\2\u018c\17\3\2\2\2\u018d\u018e\7\34\2"+
		"\2\u018e\u0194\5\u008aF\2\u018f\u0190\t\2\2\2\u0190\u0194\5&\24\2\u0191"+
		"\u0192\t\3\2\2\u0192\u0194\5\u0088E\2\u0193\u018d\3\2\2\2\u0193\u018f"+
		"\3\2\2\2\u0193\u0191\3\2\2\2\u0194\21\3\2\2\2\u0195\u0196\5\24\13\2\u0196"+
		"\23\3\2\2\2\u0197\u0198\5N(\2\u0198\u0199\5\26\f\2\u0199\25\3\2\2\2\u019a"+
		"\u019b\7C\2\2\u019b\u019d\7_\2\2\u019c\u019e\5\u00dan\2\u019d\u019c\3"+
		"\2\2\2\u019d\u019e\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a0\7`\2\2\u01a0"+
		"\27\3\2\2\2\u01a1\u01ae\5> \2\u01a2\u01ae\5<\37\2\u01a3\u01ae\5:\36\2"+
		"\u01a4\u01ae\5\34\17\2\u01a5\u01ae\58\35\2\u01a6\u01ae\5\60\31\2\u01a7"+
		"\u01ae\5\66\34\2\u01a8\u01ae\5.\30\2\u01a9\u01ae\5*\26\2\u01aa\u01ae\5"+
		"(\25\2\u01ab\u01ae\5,\27\2\u01ac\u01ae\5\32\16\2\u01ad\u01a1\3\2\2\2\u01ad"+
		"\u01a2\3\2\2\2\u01ad\u01a3\3\2\2\2\u01ad\u01a4\3\2\2\2\u01ad\u01a5\3\2"+
		"\2\2\u01ad\u01a6\3\2\2\2\u01ad\u01a7\3\2\2\2\u01ad\u01a8\3\2\2\2\u01ad"+
		"\u01a9\3\2\2\2\u01ad\u01aa\3\2\2\2\u01ad\u01ab\3\2\2\2\u01ad\u01ac\3\2"+
		"\2\2\u01ae\31\3\2\2\2\u01af\u01b0\t\4\2\2\u01b0\33\3\2\2\2\u01b1\u01b2"+
		"\7W\2\2\u01b2\u01b3\7c\2\2\u01b3\u01b4\5\u00aaV\2\u01b4\u01b5\7d\2\2\u01b5"+
		"\35\3\2\2\2\u01b6\u01bb\5 \21\2\u01b7\u01b8\7f\2\2\u01b8\u01ba\5 \21\2"+
		"\u01b9\u01b7\3\2\2\2\u01ba\u01bd\3\2\2\2\u01bb\u01b9\3\2\2\2\u01bb\u01bc"+
		"\3\2\2\2\u01bc\u01bf\3\2\2\2\u01bd\u01bb\3\2\2\2\u01be\u01c0\7f\2\2\u01bf"+
		"\u01be\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\37\3\2\2\2\u01c1\u01c6\7^\2\2"+
		"\u01c2\u01c3\7f\2\2\u01c3\u01c5\7^\2\2\u01c4\u01c2\3\2\2\2\u01c5\u01c8"+
		"\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c9\3\2\2\2\u01c8"+
		"\u01c6\3\2\2\2\u01c9\u01ca\5\u011e\u0090\2\u01ca!\3\2\2\2\u01cb\u01cd"+
		"\5$\23\2\u01cc\u01cb\3\2\2\2\u01cd\u01d0\3\2\2\2\u01ce\u01cc\3\2\2\2\u01ce"+
		"\u01cf\3\2\2\2\u01cf#\3\2\2\2\u01d0\u01ce\3\2\2\2\u01d1\u01d2\7a\2\2\u01d2"+
		"\u01d7\5\u0088E\2\u01d3\u01d4\7f\2\2\u01d4\u01d6\5\u0088E\2\u01d5\u01d3"+
		"\3\2\2\2\u01d6\u01d9\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8"+
		"\u01da\3\2\2\2\u01d9\u01d7\3\2\2\2\u01da\u01db\7b\2\2\u01db%\3\2\2\2\u01dc"+
		"\u01dd\5\u0098M\2\u01dd\'\3\2\2\2\u01de\u01df\7\62\2\2\u01df\u01e0\7_"+
		"\2\2\u01e0\u01e1\5\u0088E\2\u01e1\u01e2\7`\2\2\u01e2)\3\2\2\2\u01e3\u01e4"+
		"\7\66\2\2\u01e4\u01e5\7c\2\2\u01e5\u01e6\5\u00aaV\2\u01e6\u01e7\7d\2\2"+
		"\u01e7+\3\2\2\2\u01e8\u01e9\7\63\2\2\u01e9\u01ea\7_\2\2\u01ea\u01eb\5"+
		"\u0088E\2\u01eb\u01ec\7`\2\2\u01ec-\3\2\2\2\u01ed\u01ee\t\5\2\2\u01ee"+
		"\u01ef\7_\2\2\u01ef\u01f0\5\u0088E\2\u01f0\u01f1\7`\2\2\u01f1/\3\2\2\2"+
		"\u01f2\u01f7\7\23\2\2\u01f3\u01f4\7c\2\2\u01f4\u01f5\5\62\32\2\u01f5\u01f6"+
		"\7d\2\2\u01f6\u01f8\3\2\2\2\u01f7\u01f3\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8"+
		"\u01f9\3\2\2\2\u01f9\u01fa\7_\2\2\u01fa\u01fb\5\u0088E\2\u01fb\u01fc\7"+
		"`\2\2\u01fc\61\3\2\2\2\u01fd\u0200\5\64\33\2\u01fe\u0200\7\25\2\2\u01ff"+
		"\u01fd\3\2\2\2\u01ff\u01fe\3\2\2\2\u0200\63\3\2\2\2\u0201\u0202\7^\2\2"+
		"\u0202\65\3\2\2\2\u0203\u0204\7\24\2\2\u0204\u0205\7_\2\2\u0205\u0206"+
		"\5\u0088E\2\u0206\u0207\7`\2\2\u0207\67\3\2\2\2\u0208\u0209\79\2\2\u0209"+
		"\u020a\7_\2\2\u020a\u020b\5\u0088E\2\u020b\u020c\7`\2\2\u020c9\3\2\2\2"+
		"\u020d\u020e\78\2\2\u020e\u020f\7_\2\2\u020f\u0210\5\u0088E\2\u0210\u0211"+
		"\7`\2\2\u0211;\3\2\2\2\u0212\u0213\7\30\2\2\u0213\u0214\7_\2\2\u0214\u0217"+
		"\5\u0088E\2\u0215\u0216\7f\2\2\u0216\u0218\5\u0088E\2\u0217\u0215\3\2"+
		"\2\2\u0217\u0218\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021a\7`\2\2\u021a"+
		"=\3\2\2\2\u021b\u021c\t\5\2\2\u021c\u021d\7c\2\2\u021d\u021e\5\u0088E"+
		"\2\u021e\u021f\7<\2\2\u021f\u0220\5\u0088E\2\u0220\u0221\7d\2\2\u0221"+
		"?\3\2\2\2\u0222\u0223\7c\2\2\u0223\u0228\5B\"\2\u0224\u0225\7f\2\2\u0225"+
		"\u0227\5B\"\2\u0226\u0224\3\2\2\2\u0227\u022a\3\2\2\2\u0228\u0226\3\2"+
		"\2\2\u0228\u0229\3\2\2\2\u0229\u022b\3\2\2\2\u022a\u0228\3\2\2\2\u022b"+
		"\u022c\7d\2\2\u022cA\3\2\2\2\u022d\u022e\5\u0088E\2\u022e\u022f\7e\2\2"+
		"\u022f\u0230\5\u0088E\2\u0230C\3\2\2\2\u0231\u0235\5L\'\2\u0232\u0235"+
		"\5J&\2\u0233\u0235\5F$\2\u0234\u0231\3\2\2\2\u0234\u0232\3\2\2\2\u0234"+
		"\u0233\3\2\2\2\u0235E\3\2\2\2\u0236\u0237\7\64\2\2\u0237\u023d\7a\2\2"+
		"\u0238\u0239\5H%\2\u0239\u023a\5\u0158\u00ad\2\u023a\u023c\3\2\2\2\u023b"+
		"\u0238\3\2\2\2\u023c\u023f\3\2\2\2\u023d\u023b\3\2\2\2\u023d\u023e\3\2"+
		"\2\2\u023e\u0240\3\2\2\2\u023f\u023d\3\2\2\2\u0240\u0241\7b\2\2\u0241"+
		"G\3\2\2\2\u0242\u0243\7F\2\2\u0243\u0244\7^\2\2\u0244\u024c\5\u012a\u0096"+
		"\2\u0245\u0246\7\65\2\2\u0246\u0247\7a\2\2\u0247\u0248\5\u0088E\2\u0248"+
		"\u0249\5\u0158\u00ad\2\u0249\u024a\7b\2\2\u024a\u024c\3\2\2\2\u024b\u0242"+
		"\3\2\2\2\u024b\u0245\3\2\2\2\u024cI\3\2\2\2\u024d\u024e\7\34\2\2\u024e"+
		"\u024f\7c\2\2\u024f\u0250\7d\2\2\u0250\u0251\5\u011e\u0090\2\u0251K\3"+
		"\2\2\2\u0252\u0253\t\6\2\2\u0253\u0254\7c\2\2\u0254\u0255\5\u00aaV\2\u0255"+
		"\u0256\7d\2\2\u0256\u025e\3\2\2\2\u0257\u0258\7,\2\2\u0258\u0259\7c\2"+
		"\2\u0259\u025a\5\u00aaV\2\u025a\u025b\7d\2\2\u025b\u025c\5\u00aaV\2\u025c"+
		"\u025e\3\2\2\2\u025d\u0252\3\2\2\2\u025d\u0257\3\2\2\2\u025eM\3\2\2\2"+
		"\u025f\u0265\5P)\2\u0260\u0261\7\20\2\2\u0261\u0265\b(\1\2\u0262\u0263"+
		"\7B\2\2\u0263\u0265\b(\1\2\u0264\u025f\3\2\2\2\u0264\u0260\3\2\2\2\u0264"+
		"\u0262\3\2\2\2\u0265\u0266\3\2\2\2\u0266\u0268\5\u0158\u00ad\2\u0267\u0264"+
		"\3\2\2\2\u0268\u026b\3\2\2\2\u0269\u026a\3\2\2\2\u0269\u0267\3\2\2\2\u026a"+
		"\u026e\3\2\2\2\u026b\u0269\3\2\2\2\u026c\u026d\7\20\2\2\u026d\u026f\b"+
		"(\1\2\u026e\u026c\3\2\2\2\u026e\u026f\3\2\2\2\u026fO\3\2\2\2\u0270\u0271"+
		"\7\13\2\2\u0271\u0279\5T+\2\u0272\u0273\7\f\2\2\u0273\u0279\5T+\2\u0274"+
		"\u0275\7\r\2\2\u0275\u0279\5T+\2\u0276\u0277\7\17\2\2\u0277\u0279\5R*"+
		"\2\u0278\u0270\3\2\2\2\u0278\u0272\3\2\2\2\u0278\u0274\3\2\2\2\u0278\u0276"+
		"\3\2\2\2\u0279Q\3\2\2\2\u027a\u027c\5\u00d0i\2\u027b\u027a\3\2\2\2\u027b"+
		"\u027c\3\2\2\2\u027c\u027f\3\2\2\2\u027d\u027e\7U\2\2\u027e\u0280\5\u0088"+
		"E\2\u027f\u027d\3\2\2\2\u027f\u0280\3\2\2\2\u0280S\3\2\2\2\u0281\u0284"+
		"\3\2\2\2\u0282\u0284\5\u0088E\2\u0283\u0281\3\2\2\2\u0283\u0282\3\2\2"+
		"\2\u0284U\3\2\2\2\u0285\u028a\7a\2\2\u0286\u0287\7:\2\2\u0287\u0288\5"+
		"\u00ceh\2\u0288\u0289\5\u0158\u00ad\2\u0289\u028b\3\2\2\2\u028a\u0286"+
		"\3\2\2\2\u028a\u028b\3\2\2\2\u028b\u028d\3\2\2\2\u028c\u028e\5\u00dan"+
		"\2\u028d\u028c\3\2\2\2\u028d\u028e\3\2\2\2\u028e\u028f\3\2\2\2\u028f\u0290"+
		"\7b\2\2\u0290W\3\2\2\2\u0291\u029a\7^\2\2\u0292\u0297\7a\2\2\u0293\u0295"+
		"\5Z.\2\u0294\u0296\7f\2\2\u0295\u0294\3\2\2\2\u0295\u0296\3\2\2\2\u0296"+
		"\u0298\3\2\2\2\u0297\u0293\3\2\2\2\u0297\u0298\3\2\2\2\u0298\u0299\3\2"+
		"\2\2\u0299\u029b\7b\2\2\u029a\u0292\3\2\2\2\u029a\u029b\3\2\2\2\u029b"+
		"Y\3\2\2\2\u029c\u02a1\5\\/\2\u029d\u029e\7f\2\2\u029e\u02a0\5\\/\2\u029f"+
		"\u029d\3\2\2\2\u02a0\u02a3\3\2\2\2\u02a1\u029f\3\2\2\2\u02a1\u02a2\3\2"+
		"\2\2\u02a2[\3\2\2\2\u02a3\u02a1\3\2\2\2\u02a4\u02a5\7^\2\2\u02a5\u02a7"+
		"\7h\2\2\u02a6\u02a4\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02a8\3\2\2\2\u02a8"+
		"\u02a9\5\u0088E\2\u02a9]\3\2\2\2\u02aa\u02ab\5\u00aaV\2\u02ab\u02ac\7"+
		"\21\2\2\u02ac\u02bf\5\u00aaV\2\u02ad\u02b3\7a\2\2\u02ae\u02af\5f\64\2"+
		"\u02af\u02b0\5\u0158\u00ad\2\u02b0\u02b2\3\2\2\2\u02b1\u02ae\3\2\2\2\u02b2"+
		"\u02b5\3\2\2\2\u02b3\u02b1\3\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02bb\3\2"+
		"\2\2\u02b5\u02b3\3\2\2\2\u02b6\u02b7\5`\61\2\u02b7\u02b8\5\u0158\u00ad"+
		"\2\u02b8\u02ba\3\2\2\2\u02b9\u02b6\3\2\2\2\u02ba\u02bd\3\2\2\2\u02bb\u02b9"+
		"\3\2\2\2\u02bb\u02bc\3\2\2\2\u02bc\u02be\3\2\2\2\u02bd\u02bb\3\2\2\2\u02be"+
		"\u02c0\7b\2\2\u02bf\u02ad\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0_\3\2\2\2\u02c1"+
		"\u02c3\7\20\2\2\u02c2\u02c1\3\2\2\2\u02c2\u02c3\3\2\2\2\u02c3\u02c4\3"+
		"\2\2\2\u02c4\u02c5\5b\62\2\u02c5\u02c6\7^\2\2\u02c6\u02c8\5\u012a\u0096"+
		"\2\u02c7\u02c9\5\u00d8m\2\u02c8\u02c7\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9"+
		"a\3\2\2\2\u02ca\u02cc\7_\2\2\u02cb\u02cd\7^\2\2\u02cc\u02cb\3\2\2\2\u02cc"+
		"\u02cd\3\2\2\2\u02cd\u02cf\3\2\2\2\u02ce\u02d0\7\u0080\2\2\u02cf\u02ce"+
		"\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1\u02d2\5\u0118\u008d"+
		"\2\u02d2\u02d3\7`\2\2\u02d3c\3\2\2\2\u02d4\u02da\5\u0098M\2\u02d5\u02d6"+
		"\5\u00aaV\2\u02d6\u02d7\7i\2\2\u02d7\u02d8\7^\2\2\u02d8\u02da\3\2\2\2"+
		"\u02d9\u02d4\3\2\2\2\u02d9\u02d5\3\2\2\2\u02dae\3\2\2\2\u02db\u02dc\7"+
		"\67\2\2\u02dc\u02dd\7^\2\2\u02dd\u02e0\7l\2\2\u02de\u02e1\5d\63\2\u02df"+
		"\u02e1\5\u0136\u009c\2\u02e0\u02de\3\2\2\2\u02e0\u02df\3\2\2\2\u02e1g"+
		"\3\2\2\2\u02e2\u02e3\7\60\2\2\u02e3\u02e4\7_\2\2\u02e4\u02e7\5\u00aaV"+
		"\2\u02e5\u02e6\7f\2\2\u02e6\u02e8\5\u00d0i\2\u02e7\u02e5\3\2\2\2\u02e7"+
		"\u02e8\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02ea\7`\2\2\u02eai\3\2\2\2\u02eb"+
		"\u02ec\7/\2\2\u02ec\u02ed\7_\2\2\u02ed\u02ee\5\u00aaV\2\u02ee\u02ef\7"+
		"`\2\2\u02efk\3\2\2\2\u02f0\u02f3\5N(\2\u02f1\u02f4\5n8\2\u02f2\u02f4\5"+
		"p9\2\u02f3\u02f1\3\2\2\2\u02f3\u02f2\3\2\2\2\u02f4m\3\2\2\2\u02f5\u02f6"+
		"\7F\2\2\u02f6\u02f7\7^\2\2\u02f7\u02f9\5\u012a\u0096\2\u02f8\u02fa\5V"+
		",\2\u02f9\u02f8\3\2\2\2\u02f9\u02fa\3\2\2\2\u02fao\3\2\2\2\u02fb\u02fc"+
		"\7F\2\2\u02fc\u02fd\5~@\2\u02fd\u02fe\7^\2\2\u02fe\u0300\5\u012a\u0096"+
		"\2\u02ff\u0301\5V,\2\u0300\u02ff\3\2\2\2\u0300\u0301\3\2\2\2\u0301q\3"+
		"\2\2\2\u0302\u0305\7\34\2\2\u0303\u0306\5l\67\2\u0304\u0306\5\u00c8e\2"+
		"\u0305\u0303\3\2\2\2\u0305\u0304\3\2\2\2\u0306s\3\2\2\2\u0307\u0308\7"+
		"\67\2\2\u0308\u0309\7^\2\2\u0309\u030b\5\u012e\u0098\2\u030a\u030c\5v"+
		"<\2\u030b\u030a\3\2\2\2\u030b\u030c\3\2\2\2\u030cu\3\2\2\2\u030d\u030e"+
		"\7a\2\2\u030e\u030f\5\u0088E\2\u030f\u0310\5\u0158\u00ad\2\u0310\u0311"+
		"\7b\2\2\u0311w\3\2\2\2\u0312\u0313\7\67\2\2\u0313\u0314\5~@\2\u0314\u0315"+
		"\7^\2\2\u0315\u0317\5\u012e\u0098\2\u0316\u0318\5v<\2\u0317\u0316\3\2"+
		"\2\2\u0317\u0318\3\2\2\2\u0318y\3\2\2\2\u0319\u0321\5\b\5\2\u031a\u031d"+
		"\5\u00aaV\2\u031b\u031c\7e\2\2\u031c\u031e\5\u00d0i\2\u031d\u031b\3\2"+
		"\2\2\u031d\u031e\3\2\2\2\u031e\u0322\3\2\2\2\u031f\u0320\7e\2\2\u0320"+
		"\u0322\5\u00d0i\2\u0321\u031a\3\2\2\2\u0321\u031f\3\2\2\2\u0322{\3\2\2"+
		"\2\u0323\u0324\5\b\5\2\u0324\u0325\7l\2\2\u0325\u0326\5\u00d0i\2\u0326"+
		"}\3\2\2\2\u0327\u0329\7_\2\2\u0328\u032a\5\n\6\2\u0329\u0328\3\2\2\2\u0329"+
		"\u032a\3\2\2\2\u032a\u032b\3\2\2\2\u032b\u032d\5\u00aaV\2\u032c\u032e"+
		"\7f\2\2\u032d\u032c\3\2\2\2\u032d\u032e\3\2\2\2\u032e\u032f\3\2\2\2\u032f"+
		"\u0330\7`\2\2\u0330\177\3\2\2\2\u0331\u0334\5\u0082B\2\u0332\u0334\5\u0084"+
		"C\2\u0333\u0331\3\2\2\2\u0333\u0332\3\2\2\2\u0334\u0081\3\2\2\2\u0335"+
		"\u0337\5\u00ceh\2\u0336\u0335\3\2\2\2\u0336\u0337\3\2\2\2\u0337\u0338"+
		"\3\2\2\2\u0338\u0339\5\u0086D\2\u0339\u0083\3\2\2\2\u033a\u033c\7\34\2"+
		"\2\u033b\u033d\5\u00ceh\2\u033c\u033b\3\2\2\2\u033c\u033d\3\2\2\2\u033d"+
		"\u033e\3\2\2\2\u033e\u033f\5\u0086D\2\u033f\u0085\3\2\2\2\u0340\u0342"+
		"\7m\2\2\u0341\u0340\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u0343\3\2\2\2\u0343"+
		"\u0344\5\u00aaV\2\u0344\u0087\3\2\2\2\u0345\u0346\bE\1\2\u0346\u0347\t"+
		"\7\2\2\u0347\u0356\5\u0088E\20\u0348\u0356\5\u0098M\2\u0349\u034a\7\33"+
		"\2\2\u034a\u034b\5&\24\2\u034b\u034c\7\35\2\2\u034c\u034d\5\u0088E\4\u034d"+
		"\u0356\3\2\2\2\u034e\u034f\t\b\2\2\u034f\u0350\5\36\20\2\u0350\u0351\7"+
		"h\2\2\u0351\u0352\7h\2\2\u0352\u0353\5\"\22\2\u0353\u0354\5\u0088E\3\u0354"+
		"\u0356\3\2\2\2\u0355\u0345\3\2\2\2\u0355\u0348\3\2\2\2\u0355\u0349\3\2"+
		"\2\2\u0355\u034e\3\2\2\2\u0356\u037a\3\2\2\2\u0357\u0358\f\16\2\2\u0358"+
		"\u0359\t\t\2\2\u0359\u0379\5\u0088E\17\u035a\u035b\f\r\2\2\u035b\u035c"+
		"\t\n\2\2\u035c\u0379\5\u0088E\16\u035d\u035e\f\f\2\2\u035e\u035f\t\13"+
		"\2\2\u035f\u0379\5\u0088E\r\u0360\u0361\f\13\2\2\u0361\u0362\t\f\2\2\u0362"+
		"\u0379\5\u0088E\f\u0363\u0364\f\n\2\2\u0364\u0365\t\r\2\2\u0365\u0379"+
		"\5\u0088E\13\u0366\u0367\f\b\2\2\u0367\u0368\7o\2\2\u0368\u0379\5\u0088"+
		"E\t\u0369\u036a\f\7\2\2\u036a\u036b\7n\2\2\u036b\u0379\5\u0088E\b\u036c"+
		"\u036d\f\6\2\2\u036d\u036e\7#\2\2\u036e\u0379\5\u0088E\6\u036f\u0370\f"+
		"\5\2\2\u0370\u0371\7&\2\2\u0371\u0372\5\u0088E\2\u0372\u0373\7h\2\2\u0373"+
		"\u0374\5\u0088E\5\u0374\u0379\3\2\2\2\u0375\u0376\f\t\2\2\u0376\u0377"+
		"\7\21\2\2\u0377\u0379\5X-\2\u0378\u0357\3\2\2\2\u0378\u035a\3\2\2\2\u0378"+
		"\u035d\3\2\2\2\u0378\u0360\3\2\2\2\u0378\u0363\3\2\2\2\u0378\u0366\3\2"+
		"\2\2\u0378\u0369\3\2\2\2\u0378\u036c\3\2\2\2\u0378\u036f\3\2\2\2\u0378"+
		"\u0375\3\2\2\2\u0379\u037c\3\2\2\2\u037a\u0378\3\2\2\2\u037a\u037b\3\2"+
		"\2\2\u037b\u0089\3\2\2\2\u037c\u037a\3\2\2\2\u037d\u0391\5\20\t\2\u037e"+
		"\u0391\5\22\n\2\u037f\u0391\5\u008eH\2\u0380\u0391\5\u008cG\2\u0381\u0391"+
		"\5\u00c8e\2\u0382\u0391\5\u00e8u\2\u0383\u0391\5\u00dco\2\u0384\u0391"+
		"\5\u0116\u008c\2\u0385\u0391\5\u00eav\2\u0386\u0391\5\u00ecw\2\u0387\u0391"+
		"\5\u00eex\2\u0388\u0391\5\u00f0y\2\u0389\u0391\5\u00f2z\2\u038a\u0391"+
		"\5\u00d8m\2\u038b\u0391\5\u00f4{\2\u038c\u0391\5\u00f6|\2\u038d\u0391"+
		"\5\u0108\u0085\2\u038e\u0391\5\u0090I\2\u038f\u0391\5\u0094K\2\u0390\u037d"+
		"\3\2\2\2\u0390\u037e\3\2\2\2\u0390\u037f\3\2\2\2\u0390\u0380\3\2\2\2\u0390"+
		"\u0381\3\2\2\2\u0390\u0382\3\2\2\2\u0390\u0383\3\2\2\2\u0390\u0384\3\2"+
		"\2\2\u0390\u0385\3\2\2\2\u0390\u0386\3\2\2\2\u0390\u0387\3\2\2\2\u0390"+
		"\u0388\3\2\2\2\u0390\u0389\3\2\2\2\u0390\u038a\3\2\2\2\u0390\u038b\3\2"+
		"\2\2\u0390\u038c\3\2\2\2\u0390\u038d\3\2\2\2\u0390\u038e\3\2\2\2\u0390"+
		"\u038f\3\2\2\2\u0391\u008b\3\2\2\2\u0392\u0393\7%\2\2\u0393\u0394\5\u0088"+
		"E\2\u0394\u008d\3\2\2\2\u0395\u0396\7Q\2\2\u0396\u0398\5\u0088E\2\u0397"+
		"\u0399\5\u00d8m\2\u0398\u0397\3\2\2\2\u0398\u0399\3\2\2\2\u0399\u008f"+
		"\3\2\2\2\u039a\u039b\5\u0092J\2\u039b\u039c\5\u0110\u0089\2\u039c\u0091"+
		"\3\2\2\2\u039d\u039e\7\16\2\2\u039e\u039f\5\u0088E\2\u039f\u03a0\5\u0158"+
		"\u00ad\2\u03a0\u03a2\3\2\2\2\u03a1\u039d\3\2\2\2\u03a2\u03a5\3\2\2\2\u03a3"+
		"\u03a1\3\2\2\2\u03a3\u03a4\3\2\2\2\u03a4\u03aa\3\2\2\2\u03a5\u03a3\3\2"+
		"\2\2\u03a6\u03a7\7\17\2\2\u03a7\u03a8\5R*\2\u03a8\u03a9\5\u0158\u00ad"+
		"\2\u03a9\u03ab\3\2\2\2\u03aa\u03a6\3\2\2\2\u03aa\u03ab\3\2\2\2\u03ab\u0093"+
		"\3\2\2\2\u03ac\u03ad\7J\2\2\u03ad\u03b2\5\u0088E\2\u03ae\u03af\7J\2\2"+
		"\u03af\u03b0\t\2\2\2\u03b0\u03b2\5&\24\2\u03b1\u03ac\3\2\2\2\u03b1\u03ae"+
		"\3\2\2\2\u03b2\u0095\3\2\2\2\u03b3\u03bc\7\5\2\2\u03b4\u03bc\7\6\2\2\u03b5"+
		"\u03bc\7]\2\2\u03b6\u03bc\5\u0134\u009b\2\u03b7\u03bc\5\u014a\u00a6\2"+
		"\u03b8\u03bc\7\3\2\2\u03b9\u03bc\7\u0088\2\2\u03ba\u03bc\7\u0089\2\2\u03bb"+
		"\u03b3\3\2\2\2\u03bb\u03b4\3\2\2\2\u03bb\u03b5\3\2\2\2\u03bb\u03b6\3\2"+
		"\2\2\u03bb\u03b7\3\2\2\2\u03bb\u03b8\3\2\2\2\u03bb\u03b9\3\2\2\2\u03bb"+
		"\u03ba\3\2\2\2\u03bc\u0097\3\2\2\2\u03bd\u03be\bM\1\2\u03be\u03ca\5\u009a"+
		"N\2\u03bf\u03ca\5\u0130\u0099\2\u03c0\u03ca\5\u0154\u00ab\2\u03c1\u03ca"+
		"\5\30\r\2\u03c2\u03ca\5j\66\2\u03c3\u03ca\5h\65\2\u03c4\u03c5\t\16\2\2"+
		"\u03c5\u03c6\7_\2\2\u03c6\u03c7\5\u0088E\2\u03c7\u03c8\7`\2\2\u03c8\u03ca"+
		"\3\2\2\2\u03c9\u03bd\3\2\2\2\u03c9\u03bf\3\2\2\2\u03c9\u03c0\3\2\2\2\u03c9"+
		"\u03c1\3\2\2\2\u03c9\u03c2\3\2\2\2\u03c9\u03c3\3\2\2\2\u03c9\u03c4\3\2"+
		"\2\2\u03ca\u03e1\3\2\2\2\u03cb\u03cc\f\13\2\2\u03cc\u03cd\7i\2\2\u03cd"+
		"\u03e0\7^\2\2\u03ce\u03cf\f\n\2\2\u03cf\u03e0\5\u014e\u00a8\2\u03d0\u03d1"+
		"\f\t\2\2\u03d1\u03e0\5\u00b6\\\2\u03d2\u03d3\f\b\2\2\u03d3\u03e0\5@!\2"+
		"\u03d4\u03d5\f\7\2\2\u03d5\u03e0\5\u0150\u00a9\2\u03d6\u03d7\f\6\2\2\u03d7"+
		"\u03e0\5\u0152\u00aa\2\u03d8\u03d9\f\5\2\2\u03d9\u03da\5\u0152\u00aa\2"+
		"\u03da\u03db\7\22\2\2\u03db\u03dc\5X-\2\u03dc\u03e0\3\2\2\2\u03dd\u03de"+
		"\f\4\2\2\u03de\u03e0\5\u00a2R\2\u03df\u03cb\3\2\2\2\u03df\u03ce\3\2\2"+
		"\2\u03df\u03d0\3\2\2\2\u03df\u03d2\3\2\2\2\u03df\u03d4\3\2\2\2\u03df\u03d6"+
		"\3\2\2\2\u03df\u03d8\3\2\2\2\u03df\u03dd\3\2\2\2\u03e0\u03e3\3\2\2\2\u03e1"+
		"\u03df\3\2\2\2\u03e1\u03e2\3\2\2\2\u03e2\u0099\3\2\2\2\u03e3\u03e1\3\2"+
		"\2\2\u03e4\u03eb\5\u009cO\2\u03e5\u03eb\5\u0136\u009c\2\u03e6\u03e7\7"+
		"_\2\2\u03e7\u03e8\5\u0088E\2\u03e8\u03e9\7`\2\2\u03e9\u03eb\3\2\2\2\u03ea"+
		"\u03e4\3\2\2\2\u03ea\u03e5\3\2\2\2\u03ea\u03e6\3\2\2\2\u03eb\u009b\3\2"+
		"\2\2\u03ec\u03f0\5\u0096L\2\u03ed\u03f0\5\u013a\u009e\2\u03ee\u03f0\5"+
		"\u009eP\2\u03ef\u03ec\3\2\2\2\u03ef\u03ed\3\2\2\2\u03ef\u03ee\3\2\2\2"+
		"\u03f0\u009d\3\2\2\2\u03f1\u03f2\5N(\2\u03f2\u03f3\5\u00a0Q\2\u03f3\u009f"+
		"\3\2\2\2\u03f4\u03f6\7F\2\2\u03f5\u03f7\7^\2\2\u03f6\u03f5\3\2\2\2\u03f6"+
		"\u03f7\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03fa\5\u012a\u0096\2\u03f9\u03fb"+
		"\5V,\2\u03fa\u03f9\3\2\2\2\u03fa\u03fb\3\2\2\2\u03fb\u00a1\3\2\2\2\u03fc"+
		"\u03fe\7\'\2\2\u03fd\u03ff\5\u00d0i\2\u03fe\u03fd\3\2\2\2\u03fe\u03ff"+
		"\3\2\2\2\u03ff\u0401\3\2\2\2\u0400\u0402\7f\2\2\u0401\u0400\3\2\2\2\u0401"+
		"\u0402\3\2\2\2\u0402\u0403\3\2\2\2\u0403\u0404\7(\2\2\u0404\u00a3\3\2"+
		"\2\2\u0405\u0406\7G\2\2\u0406\u0410\7a\2\2\u0407\u040b\5\u00a8U\2\u0408"+
		"\u040b\5\u0118\u008d\2\u0409\u040b\5\u00a6T\2\u040a\u0407\3\2\2\2\u040a"+
		"\u0408\3\2\2\2\u040a\u0409\3\2\2\2\u040b\u040c\3\2\2\2\u040c\u040d\5\u0158"+
		"\u00ad\2\u040d\u040f\3\2\2\2\u040e\u040a\3\2\2\2\u040f\u0412\3\2\2\2\u0410"+
		"\u040e\3\2\2\2\u0410\u0411\3\2\2\2\u0411\u0413\3\2\2\2\u0412\u0410\3\2"+
		"\2\2\u0413\u0414\7b\2\2\u0414\u00a5\3\2\2\2\u0415\u0416\7\67\2\2\u0416"+
		"\u0417\7^\2\2\u0417\u0418\5\u012e\u0098\2\u0418\u00a7\3\2\2\2\u0419\u041b"+
		"\7\34\2\2\u041a\u0419\3\2\2\2\u041a\u041b\3\2\2\2\u041b\u041c\3\2\2\2"+
		"\u041c\u041d\5N(\2\u041d\u041e\7^\2\2\u041e\u041f\5\u012e\u0098\2\u041f"+
		"\u0420\5\u012c\u0097\2\u0420\u0429\3\2\2\2\u0421\u0423\7\34\2\2\u0422"+
		"\u0421\3\2\2\2\u0422\u0423\3\2\2\2\u0423\u0424\3\2\2\2\u0424\u0425\5N"+
		"(\2\u0425\u0426\7^\2\2\u0426\u0427\5\u012e\u0098\2\u0427\u0429\3\2\2\2"+
		"\u0428\u041a\3\2\2\2\u0428\u0422\3\2\2\2\u0429\u00a9\3\2\2\2\u042a\u0432"+
		"\5\u0118\u008d\2\u042b\u0432\5\u00acW\2\u042c\u0432\5D#\2\u042d\u042e"+
		"\7_\2\2\u042e\u042f\5\u00aaV\2\u042f\u0430\7`\2\2\u0430\u0432\3\2\2\2"+
		"\u0431\u042a\3\2\2\2\u0431\u042b\3\2\2\2\u0431\u042c\3\2\2\2\u0431\u042d"+
		"\3\2\2\2\u0432\u00ab\3\2\2\2\u0433\u043d\5\u011a\u008e\2\u0434\u043d\5"+
		"\u0146\u00a4\2\u0435\u043d\5\u0120\u0091\2\u0436\u043d\5\u0128\u0095\2"+
		"\u0437\u043d\5\u00a4S\2\u0438\u043d\5\u0122\u0092\2\u0439\u043d\5\u0124"+
		"\u0093\2\u043a\u043d\5\u0126\u0094\2\u043b\u043d\5\u00aeX\2\u043c\u0433"+
		"\3\2\2\2\u043c\u0434\3\2\2\2\u043c\u0435\3\2\2\2\u043c\u0436\3\2\2\2\u043c"+
		"\u0437\3\2\2\2\u043c\u0438\3\2\2\2\u043c\u0439\3\2\2\2\u043c\u043a\3\2"+
		"\2\2\u043c\u043b\3\2\2\2\u043d\u00ad\3\2\2\2\u043e\u043f\7\67\2\2\u043f"+
		"\u0440\5\u00b0Y\2\u0440\u00af\3\2\2\2\u0441\u044d\7_\2\2\u0442\u0447\5"+
		"\u00aaV\2\u0443\u0444\7f\2\2\u0444\u0446\5\u00aaV\2\u0445\u0443\3\2\2"+
		"\2\u0446\u0449\3\2\2\2\u0447\u0445\3\2\2\2\u0447\u0448\3\2\2\2\u0448\u044b"+
		"\3\2\2\2\u0449\u0447\3\2\2\2\u044a\u044c\7f\2\2\u044b\u044a\3\2\2\2\u044b"+
		"\u044c\3\2\2\2\u044c\u044e\3\2\2\2\u044d\u0442\3\2\2\2\u044d\u044e\3\2"+
		"\2\2\u044e\u044f\3\2\2\2\u044f\u0450\7`\2\2\u0450\u00b1\3\2\2\2\u0451"+
		"\u0459\5\u0146\u00a4\2\u0452\u0459\5\u011a\u008e\2\u0453\u0459\5\u00b4"+
		"[\2\u0454\u0459\5\u0122\u0092\2\u0455\u0459\5\u0124\u0093\2\u0456\u0459"+
		"\5D#\2\u0457\u0459\5\u0118\u008d\2\u0458\u0451\3\2\2\2\u0458\u0452\3\2"+
		"\2\2\u0458\u0453\3\2\2\2\u0458\u0454\3\2\2\2\u0458\u0455\3\2\2\2\u0458"+
		"\u0456\3\2\2\2\u0458\u0457\3\2\2\2\u0459\u00b3\3\2\2\2\u045a\u045b\7c"+
		"\2\2\u045b\u045c\7m\2\2\u045c\u045d\7d\2\2\u045d\u045e\5\u011e\u0090\2"+
		"\u045e\u00b5\3\2\2\2\u045f\u046f\7c\2\2\u0460\u0462\5\u00b8]\2\u0461\u0460"+
		"\3\2\2\2\u0461\u0462\3\2\2\2\u0462\u0463\3\2\2\2\u0463\u0465\7h\2\2\u0464"+
		"\u0466\5\u00ba^\2\u0465\u0464\3\2\2\2\u0465\u0466\3\2\2\2\u0466\u0470"+
		"\3\2\2\2\u0467\u0469\5\u00b8]\2\u0468\u0467\3\2\2\2\u0468\u0469\3\2\2"+
		"\2\u0469\u046a\3\2\2\2\u046a\u046b\7h\2\2\u046b\u046c\5\u00ba^\2\u046c"+
		"\u046d\7h\2\2\u046d\u046e\5\u00bc_\2\u046e\u0470\3\2\2\2\u046f\u0461\3"+
		"\2\2\2\u046f\u0468\3\2\2\2\u0470\u0471\3\2\2\2\u0471\u0472\7d\2\2\u0472"+
		"\u00b7\3\2\2\2\u0473\u0474\5\u0088E\2\u0474\u00b9\3\2\2\2\u0475\u0476"+
		"\5\u0088E\2\u0476\u00bb\3\2\2\2\u0477\u0478\5\u0088E\2\u0478\u00bd\3\2"+
		"\2\2\u0479\u047b\t\17\2\2\u047a\u0479\3\2\2\2\u047a\u047b\3\2\2\2\u047b"+
		"\u047c\3\2\2\2\u047c\u047d\7e\2\2\u047d\u00bf\3\2\2\2\u047e\u047f\7Q\2"+
		"\2\u047f\u0480\7^\2\2\u0480\u00c1\3\2\2\2\u0481\u048d\7Z\2\2\u0482\u048e"+
		"\5\u00c4c\2\u0483\u0489\7_\2\2\u0484\u0485\5\u00c4c\2\u0485\u0486\5\u0158"+
		"\u00ad\2\u0486\u0488\3\2\2\2\u0487\u0484\3\2\2\2\u0488\u048b\3\2\2\2\u0489"+
		"\u0487\3\2\2\2\u0489\u048a\3\2\2\2\u048a\u048c\3\2\2\2\u048b\u0489\3\2"+
		"\2\2\u048c\u048e\7`\2\2\u048d\u0482\3\2\2\2\u048d\u0483\3\2\2\2\u048e"+
		"\u00c3\3\2\2\2\u048f\u0491\t\20\2\2\u0490\u048f\3\2\2\2\u0490\u0491\3"+
		"\2\2\2\u0491\u0492\3\2\2\2\u0492\u0493\5\u00c6d\2\u0493\u00c5\3\2\2\2"+
		"\u0494\u0495\5\u014a\u00a6\2\u0495\u00c7\3\2\2\2\u0496\u049a\5\u00caf"+
		"\2\u0497\u049a\5\u00d2j\2\u0498\u049a\5\u00d6l\2\u0499\u0496\3\2\2\2\u0499"+
		"\u0497\3\2\2\2\u0499\u0498\3\2\2\2\u049a\u00c9\3\2\2\2\u049b\u04a7\7S"+
		"\2\2\u049c\u04a8\5\u00ccg\2\u049d\u04a3\7_\2\2\u049e\u049f\5\u00ccg\2"+
		"\u049f\u04a0\5\u0158\u00ad\2\u04a0\u04a2\3\2\2\2\u04a1\u049e\3\2\2\2\u04a2"+
		"\u04a5\3\2\2\2\u04a3\u04a1\3\2\2\2\u04a3\u04a4\3\2\2\2\u04a4\u04a6\3\2"+
		"\2\2\u04a5\u04a3\3\2\2\2\u04a6\u04a8\7`\2\2\u04a7\u049c\3\2\2\2\u04a7"+
		"\u049d\3\2\2\2\u04a8\u00cb\3\2\2\2\u04a9\u04af\5\u00ceh\2\u04aa\u04ac"+
		"\5\u00aaV\2\u04ab\u04aa\3\2\2\2\u04ab\u04ac\3\2\2\2\u04ac\u04ad\3\2\2"+
		"\2\u04ad\u04ae\7e\2\2\u04ae\u04b0\5\u00d0i\2\u04af\u04ab\3\2\2\2\u04af"+
		"\u04b0\3\2\2\2\u04b0\u00cd\3\2\2\2\u04b1\u04b6\7^\2\2\u04b2\u04b3\7f\2"+
		"\2\u04b3\u04b5\7^\2\2\u04b4\u04b2\3\2\2\2\u04b5\u04b8\3\2\2\2\u04b6\u04b4"+
		"\3\2\2\2\u04b6\u04b7\3\2\2\2\u04b7\u00cf\3\2\2\2\u04b8\u04b6\3\2\2\2\u04b9"+
		"\u04be\5\u0088E\2\u04ba\u04bb\7f\2\2\u04bb\u04bd\5\u0088E\2\u04bc\u04ba"+
		"\3\2\2\2\u04bd\u04c0\3\2\2\2\u04be\u04bc\3\2\2\2\u04be\u04bf\3\2\2\2\u04bf"+
		"\u00d1\3\2\2\2\u04c0\u04be\3\2\2\2\u04c1\u04cd\7W\2\2\u04c2\u04ce\5\u00d4"+
		"k\2\u04c3\u04c9\7_\2\2\u04c4\u04c5\5\u00d4k\2\u04c5\u04c6\5\u0158\u00ad"+
		"\2\u04c6\u04c8\3\2\2\2\u04c7\u04c4\3\2\2\2\u04c8\u04cb\3\2\2\2\u04c9\u04c7"+
		"\3\2\2\2\u04c9\u04ca\3\2\2\2\u04ca\u04cc\3\2\2\2\u04cb\u04c9\3\2\2\2\u04cc"+
		"\u04ce\7`\2\2\u04cd\u04c2\3\2\2\2\u04cd\u04c3\3\2\2\2\u04ce\u00d3\3\2"+
		"\2\2\u04cf\u04d1\7^\2\2\u04d0\u04d2\7e\2\2\u04d1\u04d0\3\2\2\2\u04d1\u04d2"+
		"\3\2\2\2\u04d2\u04d3\3\2\2\2\u04d3\u04d4\5\u00aaV\2\u04d4\u00d5\3\2\2"+
		"\2\u04d5\u04e1\7\\\2\2\u04d6\u04e2\5z>\2\u04d7\u04dd\7_\2\2\u04d8\u04d9"+
		"\5z>\2\u04d9\u04da\5\u0158\u00ad\2\u04da\u04dc\3\2\2\2\u04db\u04d8\3\2"+
		"\2\2\u04dc\u04df\3\2\2\2\u04dd\u04db\3\2\2\2\u04dd\u04de\3\2\2\2\u04de"+
		"\u04e0\3\2\2\2\u04df\u04dd\3\2\2\2\u04e0\u04e2\7`\2\2\u04e1\u04d6\3\2"+
		"\2\2\u04e1\u04d7\3\2\2\2\u04e2\u00d7\3\2\2\2\u04e3\u04e5\7a\2\2\u04e4"+
		"\u04e6\5\u00dan\2\u04e5\u04e4\3\2\2\2\u04e5\u04e6\3\2\2\2\u04e6\u04e7"+
		"\3\2\2\2\u04e7\u04e8\7b\2\2\u04e8\u00d9\3\2\2\2\u04e9\u04eb\5\u0158\u00ad"+
		"\2\u04ea\u04e9\3\2\2\2\u04ea\u04eb\3\2\2\2\u04eb\u04ec\3\2\2\2\u04ec\u04ed"+
		"\5\u008aF\2\u04ed\u04ee\5\u0158\u00ad\2\u04ee\u04f0\3\2\2\2\u04ef\u04ea"+
		"\3\2\2\2\u04f0\u04f1\3\2\2\2\u04f1\u04ef\3\2\2\2\u04f1\u04f2\3\2\2\2\u04f2"+
		"\u00db\3\2\2\2\u04f3\u04f9\5\u00e0q\2\u04f4\u04f9\5\u00e2r\2\u04f5\u04f9"+
		"\5\u00e4s\2\u04f6\u04f9\5\u00dep\2\u04f7\u04f9\5|?\2\u04f8\u04f3\3\2\2"+
		"\2\u04f8\u04f4\3\2\2\2\u04f8\u04f5\3\2\2\2\u04f8\u04f6\3\2\2\2\u04f8\u04f7"+
		"\3\2\2\2\u04f9\u00dd\3\2\2\2\u04fa\u04fb\5\u0088E\2\u04fb\u00df\3\2\2"+
		"\2\u04fc\u04fd\5\u0088E\2\u04fd\u04fe\7\u0082\2\2\u04fe\u04ff\5\u0088"+
		"E\2\u04ff\u00e1\3\2\2\2\u0500\u0501\5\u0088E\2\u0501\u0502\t\21\2\2\u0502"+
		"\u00e3\3\2\2\2\u0503\u0504\5\u00d0i\2\u0504\u0505\5\u00be`\2\u0505\u0506"+
		"\5\u00d0i\2\u0506\u00e5\3\2\2\2\u0507\u0508\t\22\2\2\u0508\u00e7\3\2\2"+
		"\2\u0509\u050a\7^\2\2\u050a\u050c\7h\2\2\u050b\u050d\5\u008aF\2\u050c"+
		"\u050b\3\2\2\2\u050c\u050d\3\2\2\2\u050d\u00e9\3\2\2\2\u050e\u0510\7["+
		"\2\2\u050f\u0511\5\u00d0i\2\u0510\u050f\3\2\2\2\u0510\u0511\3\2\2\2\u0511"+
		"\u00eb\3\2\2\2\u0512\u0514\7D\2\2\u0513\u0515\7^\2\2\u0514\u0513\3\2\2"+
		"\2\u0514\u0515\3\2\2\2\u0515\u00ed\3\2\2\2\u0516\u0518\7X\2\2\u0517\u0519"+
		"\7^\2\2\u0518\u0517\3\2\2\2\u0518\u0519\3\2\2\2\u0519\u00ef\3\2\2\2\u051a"+
		"\u051b\7P\2\2\u051b\u051c\7^\2\2\u051c\u00f1\3\2\2\2\u051d\u051e\7T\2"+
		"\2\u051e\u00f3\3\2\2\2\u051f\u0528\7U\2\2\u0520\u0529\5\u0088E\2\u0521"+
		"\u0522\5\u0158\u00ad\2\u0522\u0523\5\u0088E\2\u0523\u0529\3\2\2\2\u0524"+
		"\u0525\5\u00dco\2\u0525\u0526\5\u0158\u00ad\2\u0526\u0527\5\u0088E\2\u0527"+
		"\u0529\3\2\2\2\u0528\u0520\3\2\2\2\u0528\u0521\3\2\2\2\u0528\u0524\3\2"+
		"\2\2\u0529\u052a\3\2\2\2\u052a\u0530\5\u00d8m\2\u052b\u052e\7O\2\2\u052c"+
		"\u052f\5\u00f4{\2\u052d\u052f\5\u00d8m\2\u052e\u052c\3\2\2\2\u052e\u052d"+
		"\3\2\2\2\u052f\u0531\3\2\2\2\u0530\u052b\3\2\2\2\u0530\u0531\3\2\2\2\u0531"+
		"\u00f5\3\2\2\2\u0532\u0535\5\u00f8}\2\u0533\u0535\5\u00fe\u0080\2\u0534"+
		"\u0532\3\2\2\2\u0534\u0533\3\2\2\2\u0535\u00f7\3\2\2\2\u0536\u0541\7R"+
		"\2\2\u0537\u0539\5\u0088E\2\u0538\u0537\3\2\2\2\u0538\u0539\3\2\2\2\u0539"+
		"\u0542\3\2\2\2\u053a\u053c\5\u00dco\2\u053b\u053a\3\2\2\2\u053b\u053c"+
		"\3\2\2\2\u053c\u053d\3\2\2\2\u053d\u053f\5\u0158\u00ad\2\u053e\u0540\5"+
		"\u0088E\2\u053f\u053e\3\2\2\2\u053f\u0540\3\2\2\2\u0540\u0542\3\2\2\2"+
		"\u0541\u0538\3\2\2\2\u0541\u053b\3\2\2\2\u0542\u0543\3\2\2\2\u0543\u0547"+
		"\7a\2\2\u0544\u0546\5\u00fa~\2\u0545\u0544\3\2\2\2\u0546\u0549\3\2\2\2"+
		"\u0547\u0545\3\2\2\2\u0547\u0548\3\2\2\2\u0548\u054a\3\2\2\2\u0549\u0547"+
		"\3\2\2\2\u054a\u054b\7b\2\2\u054b\u00f9\3\2\2\2\u054c\u054d\5\u00fc\177"+
		"\2\u054d\u054f\7h\2\2\u054e\u0550\5\u00dan\2\u054f\u054e\3\2\2\2\u054f"+
		"\u0550\3\2\2\2\u0550\u00fb\3\2\2\2\u0551\u0552\7I\2\2\u0552\u0555\5\u00d0"+
		"i\2\u0553\u0555\7E\2\2\u0554\u0551\3\2\2\2\u0554\u0553\3\2\2\2\u0555\u00fd"+
		"\3\2\2\2\u0556\u055f\7R\2\2\u0557\u0560\5\u0100\u0081\2\u0558\u0559\5"+
		"\u0158\u00ad\2\u0559\u055a\5\u0100\u0081\2\u055a\u0560\3\2\2\2\u055b\u055c"+
		"\5\u00dco\2\u055c\u055d\5\u0158\u00ad\2\u055d\u055e\5\u0100\u0081\2\u055e"+
		"\u0560\3\2\2\2\u055f\u0557\3\2\2\2\u055f\u0558\3\2\2\2\u055f\u055b\3\2"+
		"\2\2\u0560\u0561\3\2\2\2\u0561\u0565\7a\2\2\u0562\u0564\5\u0102\u0082"+
		"\2\u0563\u0562\3\2\2\2\u0564\u0567\3\2\2\2\u0565\u0563\3\2\2\2\u0565\u0566"+
		"\3\2\2\2\u0566\u0568\3\2\2\2\u0567\u0565\3\2\2\2\u0568\u0569\7b\2\2\u0569"+
		"\u00ff\3\2\2\2\u056a\u056b\7^\2\2\u056b\u056d\7l\2\2\u056c\u056a\3\2\2"+
		"\2\u056c\u056d\3\2\2\2\u056d\u056e\3\2\2\2\u056e\u056f\5\u0098M\2\u056f"+
		"\u0570\7i\2\2\u0570\u0571\7_\2\2\u0571\u0572\7W\2\2\u0572\u0573\7`\2\2"+
		"\u0573\u0101\3\2\2\2\u0574\u0575\5\u0104\u0083\2\u0575\u0577\7h\2\2\u0576"+
		"\u0578\5\u00dan\2\u0577\u0576\3\2\2\2\u0577\u0578\3\2\2\2\u0578\u0103"+
		"\3\2\2\2\u0579\u057a\7I\2\2\u057a\u057d\5\u0106\u0084\2\u057b\u057d\7"+
		"E\2\2\u057c\u0579\3\2\2\2\u057c\u057b\3\2\2\2\u057d\u0105\3\2\2\2\u057e"+
		"\u0581\5\u00aaV\2\u057f\u0581\7]\2\2\u0580\u057e\3\2\2\2\u0580\u057f\3"+
		"\2\2\2\u0581\u0589\3\2\2\2\u0582\u0585\7f\2\2\u0583\u0586\5\u00aaV\2\u0584"+
		"\u0586\7]\2\2\u0585\u0583\3\2\2\2\u0585\u0584\3\2\2\2\u0586\u0588\3\2"+
		"\2\2\u0587\u0582\3\2\2\2\u0588\u058b\3\2\2\2\u0589\u0587\3\2\2\2\u0589"+
		"\u058a\3\2\2\2\u058a\u0107\3\2\2\2\u058b\u0589\3\2\2\2\u058c\u058d\7H"+
		"\2\2\u058d\u0591\7a\2\2\u058e\u0590\5\u010a\u0086\2\u058f\u058e\3\2\2"+
		"\2\u0590\u0593\3\2\2\2\u0591\u058f\3\2\2\2\u0591\u0592\3\2\2\2\u0592\u0594"+
		"\3\2\2\2\u0593\u0591\3\2\2\2\u0594\u0595\7b\2\2\u0595\u0109\3\2\2\2\u0596"+
		"\u0597\5\u010c\u0087\2\u0597\u0599\7h\2\2\u0598\u059a\5\u00dan\2\u0599"+
		"\u0598\3\2\2\2\u0599\u059a\3\2\2\2\u059a\u010b\3\2\2\2\u059b\u059e\7I"+
		"\2\2\u059c\u059f\5\u00e0q\2\u059d\u059f\5\u010e\u0088\2\u059e\u059c\3"+
		"\2\2\2\u059e\u059d\3\2\2\2\u059f\u05a2\3\2\2\2\u05a0\u05a2\7E\2\2\u05a1"+
		"\u059b\3\2\2\2\u05a1\u05a0\3\2\2\2\u05a2\u010d\3\2\2\2\u05a3\u05a4\5\u00d0"+
		"i\2\u05a4\u05a5\7e\2\2\u05a5\u05aa\3\2\2\2\u05a6\u05a7\5\u00ceh\2\u05a7"+
		"\u05a8\7l\2\2\u05a8\u05aa\3\2\2\2\u05a9\u05a3\3\2\2\2\u05a9\u05a6\3\2"+
		"\2\2\u05a9\u05aa\3\2\2\2\u05aa\u05ab\3\2\2\2\u05ab\u05ac\5\u0088E\2\u05ac"+
		"\u010f\3\2\2\2\u05ad\u05b1\7Y\2\2\u05ae\u05b2\5\u0088E\2\u05af\u05b2\5"+
		"\u0112\u008a\2\u05b0\u05b2\5\u0114\u008b\2\u05b1\u05ae\3\2\2\2\u05b1\u05af"+
		"\3\2\2\2\u05b1\u05b0\3\2\2\2\u05b1\u05b2\3\2\2\2\u05b2\u05b3\3\2\2\2\u05b3"+
		"\u05b4\5\u00d8m\2\u05b4\u0111\3\2\2\2\u05b5\u05b7\5\u00dco\2\u05b6\u05b5"+
		"\3\2\2\2\u05b6\u05b7\3\2\2\2\u05b7\u05b8\3\2\2\2\u05b8\u05ba\5\u0158\u00ad"+
		"\2\u05b9\u05bb\5\u0088E\2\u05ba\u05b9\3\2\2\2\u05ba\u05bb\3\2\2\2\u05bb"+
		"\u05bc\3\2\2\2\u05bc\u05be\5\u0158\u00ad\2\u05bd\u05bf\5\u00dco\2\u05be"+
		"\u05bd\3\2\2\2\u05be\u05bf\3\2\2\2\u05bf\u0113\3\2\2\2\u05c0\u05c1\5\u00d0"+
		"i\2\u05c1\u05c2\7e\2\2\u05c2\u05c7\3\2\2\2\u05c3\u05c4\5\u00ceh\2\u05c4"+
		"\u05c5\7l\2\2\u05c5\u05c7\3\2\2\2\u05c6\u05c0\3\2\2\2\u05c6\u05c3\3\2"+
		"\2\2\u05c6\u05c7\3\2\2\2\u05c7\u05c8\3\2\2\2\u05c8\u05c9\7V\2\2\u05c9"+
		"\u05ca\5\u0088E\2\u05ca\u0115\3\2\2\2\u05cb\u05cc\7K\2\2\u05cc\u05cd\5"+
		"\u0088E\2\u05cd\u0117\3\2\2\2\u05ce\u05d1\5\u0138\u009d\2\u05cf\u05d1"+
		"\7^\2\2\u05d0\u05ce\3\2\2\2\u05d0\u05cf\3\2\2\2\u05d1\u0119\3\2\2\2\u05d2"+
		"\u05d3\7c\2\2\u05d3\u05d4\5\u011c\u008f\2\u05d4\u05d5\7d\2\2\u05d5\u05d6"+
		"\5\u011e\u0090\2\u05d6\u011b\3\2\2\2\u05d7\u05d8\5\u0088E\2\u05d8\u011d"+
		"\3\2\2\2\u05d9\u05da\5\u00aaV\2\u05da\u011f\3\2\2\2\u05db\u05dc\7\u0080"+
		"\2\2\u05dc\u05dd\5\u00aaV\2\u05dd\u0121\3\2\2\2\u05de\u05df\7c\2\2\u05df"+
		"\u05e0\7d\2\2\u05e0\u05e1\5\u011e\u0090\2\u05e1\u0123\3\2\2\2\u05e2\u05e3"+
		"\7L\2\2\u05e3\u05e4\7c\2\2\u05e4\u05e5\5\u00aaV\2\u05e5\u05e6\7d\2\2\u05e6"+
		"\u05e7\5\u011e\u0090\2\u05e7\u0125\3\2\2\2\u05e8\u05ee\7N\2\2\u05e9\u05ea"+
		"\7N\2\2\u05ea\u05ee\7\u0082\2\2\u05eb\u05ec\7\u0082\2\2\u05ec\u05ee\7"+
		"N\2\2\u05ed\u05e8\3\2\2\2\u05ed\u05e9\3\2\2\2\u05ed\u05eb\3\2\2\2\u05ee"+
		"\u05ef\3\2\2\2\u05ef\u05f0\5\u011e\u0090\2\u05f0\u0127\3\2\2\2\u05f1\u05f2"+
		"\7F\2\2\u05f2\u05f3\5\u012a\u0096\2\u05f3\u0129\3\2\2\2\u05f4\u05f5\5"+
		"\u012e\u0098\2\u05f5\u05f6\5\u012c\u0097\2\u05f6\u05f9\3\2\2\2\u05f7\u05f9"+
		"\5\u012e\u0098\2\u05f8\u05f4\3\2\2\2\u05f8\u05f7\3\2\2\2\u05f9\u012b\3"+
		"\2\2\2\u05fa\u05fd\5\u012e\u0098\2\u05fb\u05fd\5\u00aaV\2\u05fc\u05fa"+
		"\3\2\2\2\u05fc\u05fb\3\2\2\2\u05fd\u012d\3\2\2\2\u05fe\u060a\7_\2\2\u05ff"+
		"\u0604\5\u0080A\2\u0600\u0601\7f\2\2\u0601\u0603\5\u0080A\2\u0602\u0600"+
		"\3\2\2\2\u0603\u0606\3\2\2\2\u0604\u0602\3\2\2\2\u0604\u0605\3\2\2\2\u0605"+
		"\u0608\3\2\2\2\u0606\u0604\3\2\2\2\u0607\u0609\7f\2\2\u0608\u0607\3\2"+
		"\2\2\u0608\u0609\3\2\2\2\u0609\u060b\3\2\2\2\u060a\u05ff\3\2\2\2\u060a"+
		"\u060b\3\2\2\2\u060b\u060c\3\2\2\2\u060c\u060d\7`\2\2\u060d\u012f\3\2"+
		"\2\2\u060e\u060f\5\u0132\u009a\2\u060f\u0610\7_\2\2\u0610\u0612\5\u0088"+
		"E\2\u0611\u0613\7f\2\2\u0612\u0611\3\2\2\2\u0612\u0613\3\2\2\2\u0613\u0614"+
		"\3\2\2\2\u0614\u0615\7`\2\2\u0615\u0131\3\2\2\2\u0616\u061c\5\u00acW\2"+
		"\u0617\u0618\7_\2\2\u0618\u0619\5\u0132\u009a\2\u0619\u061a\7`\2\2\u061a"+
		"\u061c\3\2\2\2\u061b\u0616\3\2\2\2\u061b\u0617\3\2\2\2\u061c\u0133\3\2"+
		"\2\2\u061d\u061e\t\23\2\2\u061e\u0135\3\2\2\2\u061f\u0620\7^\2\2\u0620"+
		"\u0137\3\2\2\2\u0621\u0622\7^\2\2\u0622\u0623\7i\2\2\u0623\u0624\7^\2"+
		"\2\u0624\u0139\3\2\2\2\u0625\u0626\5\u00b2Z\2\u0626\u0627\5\u013c\u009f"+
		"\2\u0627\u013b\3\2\2\2\u0628\u062d\7a\2\2\u0629\u062b\5\u013e\u00a0\2"+
		"\u062a\u062c\7f\2\2\u062b\u062a\3\2\2\2\u062b\u062c\3\2\2\2\u062c\u062e"+
		"\3\2\2\2\u062d\u0629\3\2\2\2\u062d\u062e\3\2\2\2\u062e\u062f\3\2\2\2\u062f"+
		"\u0630\7b\2\2\u0630\u013d\3\2\2\2\u0631\u0636\5\u0140\u00a1\2\u0632\u0633"+
		"\7f\2\2\u0633\u0635\5\u0140\u00a1\2\u0634\u0632\3\2\2\2\u0635\u0638\3"+
		"\2\2\2\u0636\u0634\3\2\2\2\u0636\u0637\3\2\2\2\u0637\u013f\3\2\2\2\u0638"+
		"\u0636\3\2\2\2\u0639\u063a\5\u0142\u00a2\2\u063a\u063b\7h\2\2\u063b\u063d"+
		"\3\2\2\2\u063c\u0639\3\2\2\2\u063c\u063d\3\2\2\2\u063d\u063e\3\2\2\2\u063e"+
		"\u063f\5\u0144\u00a3\2\u063f\u0141\3\2\2\2\u0640\u0643\5\u0088E\2\u0641"+
		"\u0643\5\u013c\u009f\2\u0642\u0640\3\2\2\2\u0642\u0641\3\2\2\2\u0643\u0143"+
		"\3\2\2\2\u0644\u0647\5\u0088E\2\u0645\u0647\5\u013c\u009f\2\u0646\u0644"+
		"\3\2\2\2\u0646\u0645\3\2\2\2\u0647\u0145\3\2\2\2\u0648\u0649\7M\2\2\u0649"+
		"\u064f\7a\2\2\u064a\u064b\5\u0148\u00a5\2\u064b\u064c\5\u0158\u00ad\2"+
		"\u064c\u064e\3\2\2\2\u064d\u064a\3\2\2\2\u064e\u0651\3\2\2\2\u064f\u064d"+
		"\3\2\2\2\u064f\u0650\3\2\2\2\u0650\u0652\3\2\2\2\u0651\u064f\3\2\2\2\u0652"+
		"\u0653\7b\2\2\u0653\u0147\3\2\2\2\u0654\u0655\5\u00ceh\2\u0655\u0656\5"+
		"\u00aaV\2\u0656\u0659\3\2\2\2\u0657\u0659\5\u014c\u00a7\2\u0658\u0654"+
		"\3\2\2\2\u0658\u0657\3\2\2\2\u0659\u065b\3\2\2\2\u065a\u065c\5\u014a\u00a6"+
		"\2\u065b\u065a\3\2\2\2\u065b\u065c\3\2\2\2\u065c\u0149\3\2\2\2\u065d\u065e"+
		"\t\24\2\2\u065e\u014b\3\2\2\2\u065f\u0661\7\u0080\2\2\u0660\u065f\3\2"+
		"\2\2\u0660\u0661\3\2\2\2\u0661\u0662\3\2\2\2\u0662\u0663\5\u0118\u008d"+
		"\2\u0663\u014d\3\2\2\2\u0664\u0665\7c\2\2\u0665\u0666\5\u0088E\2\u0666"+
		"\u0667\7d\2\2\u0667\u014f\3\2\2\2\u0668\u0669\7i\2\2\u0669\u066a\7_\2"+
		"\2\u066a\u066b\5\u00aaV\2\u066b\u066c\7`\2\2\u066c\u0151\3\2\2\2\u066d"+
		"\u067c\7_\2\2\u066e\u0675\5\u00d0i\2\u066f\u0672\5\u0132\u009a\2\u0670"+
		"\u0671\7f\2\2\u0671\u0673\5\u00d0i\2\u0672\u0670\3\2\2\2\u0672\u0673\3"+
		"\2\2\2\u0673\u0675\3\2\2\2\u0674\u066e\3\2\2\2\u0674\u066f\3\2\2\2\u0675"+
		"\u0677\3\2\2\2\u0676\u0678\7m\2\2\u0677\u0676\3\2\2\2\u0677\u0678\3\2"+
		"\2\2\u0678\u067a\3\2\2\2\u0679\u067b\7f\2\2\u067a\u0679\3\2\2\2\u067a"+
		"\u067b\3\2\2\2\u067b\u067d\3\2\2\2\u067c\u0674\3\2\2\2\u067c\u067d\3\2"+
		"\2\2\u067d\u067e\3\2\2\2\u067e\u067f\7`\2\2\u067f\u0153\3\2\2\2\u0680"+
		"\u0681\5\u0132\u009a\2\u0681\u0682\7i\2\2\u0682\u0683\7^\2\2\u0683\u0155"+
		"\3\2\2\2\u0684\u0685\5\u00aaV\2\u0685\u0157\3\2\2\2\u0686\u068b\7g\2\2"+
		"\u0687\u068b\7\2\2\3\u0688\u068b\7\u0098\2\2\u0689\u068b\6\u00ad\24\2"+
		"\u068a\u0686\3\2\2\2\u068a\u0687\3\2\2\2\u068a\u0688\3\2\2\2\u068a\u0689"+
		"\3\2\2\2\u068b\u0159\3\2\2\2\u00ad\u0168\u016d\u0176\u017c\u0182\u018b"+
		"\u0193\u019d\u01ad\u01bb\u01bf\u01c6\u01ce\u01d7\u01f7\u01ff\u0217\u0228"+
		"\u0234\u023d\u024b\u025d\u0264\u0269\u026e\u0278\u027b\u027f\u0283\u028a"+
		"\u028d\u0295\u0297\u029a\u02a1\u02a6\u02b3\u02bb\u02bf\u02c2\u02c8\u02cc"+
		"\u02cf\u02d9\u02e0\u02e7\u02f3\u02f9\u0300\u0305\u030b\u0317\u031d\u0321"+
		"\u0329\u032d\u0333\u0336\u033c\u0341\u0355\u0378\u037a\u0390\u0398\u03a3"+
		"\u03aa\u03b1\u03bb\u03c9\u03df\u03e1\u03ea\u03ef\u03f6\u03fa\u03fe\u0401"+
		"\u040a\u0410\u041a\u0422\u0428\u0431\u043c\u0447\u044b\u044d\u0458\u0461"+
		"\u0465\u0468\u046f\u047a\u0489\u048d\u0490\u0499\u04a3\u04a7\u04ab\u04af"+
		"\u04b6\u04be\u04c9\u04cd\u04d1\u04dd\u04e1\u04e5\u04ea\u04f1\u04f8\u050c"+
		"\u0510\u0514\u0518\u0528\u052e\u0530\u0534\u0538\u053b\u053f\u0541\u0547"+
		"\u054f\u0554\u055f\u0565\u056c\u0577\u057c\u0580\u0585\u0589\u0591\u0599"+
		"\u059e\u05a1\u05a9\u05b1\u05b6\u05ba\u05be\u05c6\u05d0\u05ed\u05f8\u05fc"+
		"\u0604\u0608\u060a\u0612\u061b\u062b\u062d\u0636\u063c\u0642\u0646\u064f"+
		"\u0658\u065b\u0660\u0672\u0674\u0677\u067a\u067c\u068a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}