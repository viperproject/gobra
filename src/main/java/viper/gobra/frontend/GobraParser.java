// Generated from /home/nico/Documents/repositories/projects/eth/BA/gobraHome/gobra/src/main/antlr4/GobraParser.g4 by ANTLR 4.9.1
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
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, ASSERT=3, ASSUME=4, PRE=5, PRESERVES=6, POST=7, INV=8, 
		DEC=9, PURE=10, IMPL=11, OLD=12, LHS=13, FORALL=14, EXISTS=15, ACCESS=16, 
		FOLD=17, UNFOLD=18, UNFOLDING=19, GHOST=20, IN=21, MULTI=22, SUBSET=23, 
		UNION=24, INTERSECTION=25, SETMINUS=26, IMPLIES=27, QMARK=28, RANGE=29, 
		SEQ=30, SET=31, MSET=32, DICT=33, OPT=34, LEN=35, NEW=36, CAP=37, DOM=38, 
		PRED=39, TYPE_OF=40, IS_COMPARABLE=41, ADDR_MOD=42, DOT_DOT=43, SHARED=44, 
		EXCLUSIVE=45, PREDICATE=46, BREAK=47, DEFAULT=48, FUNC=49, INTERFACE=50, 
		SELECT=51, CASE=52, DEFER=53, GO=54, MAP=55, STRUCT=56, CHAN=57, ELSE=58, 
		GOTO=59, PACKAGE=60, SWITCH=61, CONST=62, FALLTHROUGH=63, IF=64, TYPE=65, 
		CONTINUE=66, FOR=67, IMPORT=68, RETURN=69, VAR=70, NIL_LIT=71, IDENTIFIER=72, 
		L_PAREN=73, R_PAREN=74, L_CURLY=75, R_CURLY=76, L_BRACKET=77, R_BRACKET=78, 
		ASSIGN=79, COMMA=80, SEMI=81, COLON=82, DOT=83, PLUS_PLUS=84, MINUS_MINUS=85, 
		DECLARE_ASSIGN=86, ELLIPSIS=87, LOGICAL_OR=88, LOGICAL_AND=89, EQUALS=90, 
		NOT_EQUALS=91, LESS=92, LESS_OR_EQUALS=93, GREATER=94, GREATER_OR_EQUALS=95, 
		OR=96, DIV=97, MOD=98, LSHIFT=99, RSHIFT=100, BIT_CLEAR=101, EXCLAMATION=102, 
		PLUS=103, MINUS=104, CARET=105, STAR=106, AMPERSAND=107, RECEIVE=108, 
		DECIMAL_LIT=109, BINARY_LIT=110, OCTAL_LIT=111, HEX_LIT=112, FLOAT_LIT=113, 
		DECIMAL_FLOAT_LIT=114, HEX_FLOAT_LIT=115, IMAGINARY_LIT=116, RUNE_LIT=117, 
		BYTE_VALUE=118, OCTAL_BYTE_VALUE=119, HEX_BYTE_VALUE=120, LITTLE_U_VALUE=121, 
		BIG_U_VALUE=122, RAW_STRING_LIT=123, INTERPRETED_STRING_LIT=124, WS=125, 
		COMMENT=126, TERMINATOR=127, LINE_COMMENT=128;
	public static final int
		RULE_maybeAddressableIdentifierList = 0, RULE_maybeAddressableIdentifier = 1, 
		RULE_ghostStatement = 2, RULE_boundVariables = 3, RULE_boundVariableDecl = 4, 
		RULE_predicateAccess = 5, RULE_access = 6, RULE_exprOnly = 7, RULE_stmtOnly = 8, 
		RULE_ghostPrimaryExpr = 9, RULE_sConversion = 10, RULE_triggers = 11, 
		RULE_trigger = 12, RULE_old = 13, RULE_oldLabelUse = 14, RULE_labelUse = 15, 
		RULE_typeOf = 16, RULE_isComparable = 17, RULE_ghostTypeLit = 18, RULE_sqType = 19, 
		RULE_seqUpdExp = 20, RULE_seqUpdClause = 21, RULE_specification = 22, 
		RULE_specStatement = 23, RULE_functionDecl = 24, RULE_methodDecl = 25, 
		RULE_assertion = 26, RULE_range = 27, RULE_sourceFile = 28, RULE_ghostMember = 29, 
		RULE_fpredicateDecl = 30, RULE_predicateBody = 31, RULE_mpredicateDecl = 32, 
		RULE_implementationProof = 33, RULE_methodImplementationProof = 34, RULE_selection = 35, 
		RULE_implementationProofPredicateAlias = 36, RULE_varSpec = 37, RULE_shortVarDecl = 38, 
		RULE_receiver = 39, RULE_nonLocalReceiver = 40, RULE_parameterDecl = 41, 
		RULE_unaryExpr = 42, RULE_unfolding = 43, RULE_expression = 44, RULE_statement = 45, 
		RULE_specForStmt = 46, RULE_loopSpec = 47, RULE_terminationMeasure = 48, 
		RULE_basicLit = 49, RULE_primaryExpr = 50, RULE_interfaceType = 51, RULE_predicateSpec = 52, 
		RULE_methodSpec = 53, RULE_type_ = 54, RULE_literalType = 55, RULE_slice_ = 56, 
		RULE_low = 57, RULE_high = 58, RULE_cap = 59, RULE_ifStmt = 60, RULE_exprSwitchStmt = 61, 
		RULE_typeSwitchStmt = 62, RULE_eos = 63, RULE_packageClause = 64, RULE_importDecl = 65, 
		RULE_importSpec = 66, RULE_importPath = 67, RULE_declaration = 68, RULE_constDecl = 69, 
		RULE_constSpec = 70, RULE_identifierList = 71, RULE_expressionList = 72, 
		RULE_typeDecl = 73, RULE_typeSpec = 74, RULE_varDecl = 75, RULE_block = 76, 
		RULE_statementList = 77, RULE_simpleStmt = 78, RULE_expressionStmt = 79, 
		RULE_sendStmt = 80, RULE_incDecStmt = 81, RULE_assignment = 82, RULE_assign_op = 83, 
		RULE_emptyStmt = 84, RULE_labeledStmt = 85, RULE_returnStmt = 86, RULE_breakStmt = 87, 
		RULE_continueStmt = 88, RULE_gotoStmt = 89, RULE_fallthroughStmt = 90, 
		RULE_deferStmt = 91, RULE_switchStmt = 92, RULE_exprCaseClause = 93, RULE_exprSwitchCase = 94, 
		RULE_typeSwitchGuard = 95, RULE_typeCaseClause = 96, RULE_typeSwitchCase = 97, 
		RULE_typeList = 98, RULE_selectStmt = 99, RULE_commClause = 100, RULE_commCase = 101, 
		RULE_recvStmt = 102, RULE_forStmt = 103, RULE_forClause = 104, RULE_rangeClause = 105, 
		RULE_goStmt = 106, RULE_typeName = 107, RULE_typeLit = 108, RULE_arrayType = 109, 
		RULE_arrayLength = 110, RULE_elementType = 111, RULE_pointerType = 112, 
		RULE_sliceType = 113, RULE_mapType = 114, RULE_channelType = 115, RULE_functionType = 116, 
		RULE_signature = 117, RULE_result = 118, RULE_parameters = 119, RULE_conversion = 120, 
		RULE_operand = 121, RULE_literal = 122, RULE_integer = 123, RULE_operandName = 124, 
		RULE_qualifiedIdent = 125, RULE_compositeLit = 126, RULE_literalValue = 127, 
		RULE_elementList = 128, RULE_keyedElement = 129, RULE_key = 130, RULE_element = 131, 
		RULE_structType = 132, RULE_fieldDecl = 133, RULE_string_ = 134, RULE_embeddedField = 135, 
		RULE_functionLit = 136, RULE_index = 137, RULE_typeAssertion = 138, RULE_arguments = 139, 
		RULE_methodExpr = 140, RULE_receiverType = 141;
	private static String[] makeRuleNames() {
		return new String[] {
			"maybeAddressableIdentifierList", "maybeAddressableIdentifier", "ghostStatement", 
			"boundVariables", "boundVariableDecl", "predicateAccess", "access", "exprOnly", 
			"stmtOnly", "ghostPrimaryExpr", "sConversion", "triggers", "trigger", 
			"old", "oldLabelUse", "labelUse", "typeOf", "isComparable", "ghostTypeLit", 
			"sqType", "seqUpdExp", "seqUpdClause", "specification", "specStatement", 
			"functionDecl", "methodDecl", "assertion", "range", "sourceFile", "ghostMember", 
			"fpredicateDecl", "predicateBody", "mpredicateDecl", "implementationProof", 
			"methodImplementationProof", "selection", "implementationProofPredicateAlias", 
			"varSpec", "shortVarDecl", "receiver", "nonLocalReceiver", "parameterDecl", 
			"unaryExpr", "unfolding", "expression", "statement", "specForStmt", "loopSpec", 
			"terminationMeasure", "basicLit", "primaryExpr", "interfaceType", "predicateSpec", 
			"methodSpec", "type_", "literalType", "slice_", "low", "high", "cap", 
			"ifStmt", "exprSwitchStmt", "typeSwitchStmt", "eos", "packageClause", 
			"importDecl", "importSpec", "importPath", "declaration", "constDecl", 
			"constSpec", "identifierList", "expressionList", "typeDecl", "typeSpec", 
			"varDecl", "block", "statementList", "simpleStmt", "expressionStmt", 
			"sendStmt", "incDecStmt", "assignment", "assign_op", "emptyStmt", "labeledStmt", 
			"returnStmt", "breakStmt", "continueStmt", "gotoStmt", "fallthroughStmt", 
			"deferStmt", "switchStmt", "exprCaseClause", "exprSwitchCase", "typeSwitchGuard", 
			"typeCaseClause", "typeSwitchCase", "typeList", "selectStmt", "commClause", 
			"commCase", "recvStmt", "forStmt", "forClause", "rangeClause", "goStmt", 
			"typeName", "typeLit", "arrayType", "arrayLength", "elementType", "pointerType", 
			"sliceType", "mapType", "channelType", "functionType", "signature", "result", 
			"parameters", "conversion", "operand", "literal", "integer", "operandName", 
			"qualifiedIdent", "compositeLit", "literalValue", "elementList", "keyedElement", 
			"key", "element", "structType", "fieldDecl", "string_", "embeddedField", 
			"functionLit", "index", "typeAssertion", "arguments", "methodExpr", "receiverType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'true'", "'false'", "'assert'", "'assume'", "'requires'", "'preserves'", 
			"'ensures'", "'invariant'", "'decreases'", "'pure'", "'implements'", 
			"'old'", "'#lhs'", "'forall'", "'exists'", "'acc'", "'fold'", "'unfold'", 
			"'unfolding'", "'ghost'", "'in'", "'#'", "'subset'", "'union'", "'intersection'", 
			"'setminus'", "'==>'", "'?'", "'range'", "'seq'", "'set'", "'mset'", 
			"'dict'", "'option'", "'len'", "'new'", "'cap'", "'domain'", "'pred'", 
			"'typeOf'", "'isComparable'", "'@'", "'..'", "'shared'", "'exclusive'", 
			"'predicate'", "'break'", "'default'", "'func'", "'interface'", "'select'", 
			"'case'", "'defer'", "'go'", "'map'", "'struct'", "'chan'", "'else'", 
			"'goto'", "'package'", "'switch'", "'const'", "'fallthrough'", "'if'", 
			"'type'", "'continue'", "'for'", "'import'", "'return'", "'var'", "'nil'", 
			null, "'('", "')'", "'{'", "'}'", "'['", "']'", "'='", "','", "';'", 
			"':'", "'.'", "'++'", "'--'", "':='", "'...'", "'||'", "'&&'", "'=='", 
			"'!='", "'<'", "'<='", "'>'", "'>='", "'|'", "'/'", "'%'", "'<<'", "'>>'", 
			"'&^'", "'!'", "'+'", "'-'", "'^'", "'*'", "'&'", "'<-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TRUE", "FALSE", "ASSERT", "ASSUME", "PRE", "PRESERVES", "POST", 
			"INV", "DEC", "PURE", "IMPL", "OLD", "LHS", "FORALL", "EXISTS", "ACCESS", 
			"FOLD", "UNFOLD", "UNFOLDING", "GHOST", "IN", "MULTI", "SUBSET", "UNION", 
			"INTERSECTION", "SETMINUS", "IMPLIES", "QMARK", "RANGE", "SEQ", "SET", 
			"MSET", "DICT", "OPT", "LEN", "NEW", "CAP", "DOM", "PRED", "TYPE_OF", 
			"IS_COMPARABLE", "ADDR_MOD", "DOT_DOT", "SHARED", "EXCLUSIVE", "PREDICATE", 
			"BREAK", "DEFAULT", "FUNC", "INTERFACE", "SELECT", "CASE", "DEFER", "GO", 
			"MAP", "STRUCT", "CHAN", "ELSE", "GOTO", "PACKAGE", "SWITCH", "CONST", 
			"FALLTHROUGH", "IF", "TYPE", "CONTINUE", "FOR", "IMPORT", "RETURN", "VAR", 
			"NIL_LIT", "IDENTIFIER", "L_PAREN", "R_PAREN", "L_CURLY", "R_CURLY", 
			"L_BRACKET", "R_BRACKET", "ASSIGN", "COMMA", "SEMI", "COLON", "DOT", 
			"PLUS_PLUS", "MINUS_MINUS", "DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", 
			"LOGICAL_AND", "EQUALS", "NOT_EQUALS", "LESS", "LESS_OR_EQUALS", "GREATER", 
			"GREATER_OR_EQUALS", "OR", "DIV", "MOD", "LSHIFT", "RSHIFT", "BIT_CLEAR", 
			"EXCLAMATION", "PLUS", "MINUS", "CARET", "STAR", "AMPERSAND", "RECEIVE", 
			"DECIMAL_LIT", "BINARY_LIT", "OCTAL_LIT", "HEX_LIT", "FLOAT_LIT", "DECIMAL_FLOAT_LIT", 
			"HEX_FLOAT_LIT", "IMAGINARY_LIT", "RUNE_LIT", "BYTE_VALUE", "OCTAL_BYTE_VALUE", 
			"HEX_BYTE_VALUE", "LITTLE_U_VALUE", "BIG_U_VALUE", "RAW_STRING_LIT", 
			"INTERPRETED_STRING_LIT", "WS", "COMMENT", "TERMINATOR", "LINE_COMMENT"
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
		enterRule(_localctx, 0, RULE_maybeAddressableIdentifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			maybeAddressableIdentifier();
			setState(289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(285);
				match(COMMA);
				setState(286);
				maybeAddressableIdentifier();
				}
				}
				setState(291);
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
		enterRule(_localctx, 2, RULE_maybeAddressableIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			match(IDENTIFIER);
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(293);
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

	public static class GhostStatementContext extends ParserRuleContext {
		public Token fold_stmt;
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode ASSERT() { return getToken(GobraParser.ASSERT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PredicateAccessContext predicateAccess() {
			return getRuleContext(PredicateAccessContext.class,0);
		}
		public TerminalNode FOLD() { return getToken(GobraParser.FOLD, 0); }
		public TerminalNode UNFOLD() { return getToken(GobraParser.UNFOLD, 0); }
		public GhostStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ghostStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitGhostStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GhostStatementContext ghostStatement() throws RecognitionException {
		GhostStatementContext _localctx = new GhostStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ghostStatement);
		int _la;
		try {
			setState(302);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				match(GHOST);
				setState(297);
				statement();
				}
				break;
			case ASSERT:
				enterOuterAlt(_localctx, 2);
				{
				setState(298);
				match(ASSERT);
				setState(299);
				expression(0);
				}
				break;
			case FOLD:
			case UNFOLD:
				enterOuterAlt(_localctx, 3);
				{
				setState(300);
				((GhostStatementContext)_localctx).fold_stmt = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==FOLD || _la==UNFOLD) ) {
					((GhostStatementContext)_localctx).fold_stmt = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(301);
				predicateAccess();
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
		enterRule(_localctx, 6, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			boundVariableDecl();
			setState(309);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(305);
					match(COMMA);
					setState(306);
					boundVariableDecl();
					}
					} 
				}
				setState(311);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(312);
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
		enterRule(_localctx, 8, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(IDENTIFIER);
			setState(320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(316);
				match(COMMA);
				setState(317);
				match(IDENTIFIER);
				}
				}
				setState(322);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(323);
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
		enterRule(_localctx, 10, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325);
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
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 12, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			match(ACCESS);
			setState(328);
			match(L_PAREN);
			setState(329);
			expression(0);
			setState(335);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(330);
				match(COMMA);
				setState(333);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(331);
					match(IDENTIFIER);
					}
					break;
				case 2:
					{
					setState(332);
					expression(0);
					}
					break;
				}
				}
			}

			setState(337);
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
		enterRule(_localctx, 14, RULE_exprOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			expression(0);
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
		enterRule(_localctx, 16, RULE_stmtOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			statement();
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
		public IsComparableContext isComparable() {
			return getRuleContext(IsComparableContext.class,0);
		}
		public OldContext old() {
			return getRuleContext(OldContext.class,0);
		}
		public SConversionContext sConversion() {
			return getRuleContext(SConversionContext.class,0);
		}
		public TerminalNode FORALL() { return getToken(GobraParser.FORALL, 0); }
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
		enterRule(_localctx, 18, RULE_ghostPrimaryExpr);
		try {
			setState(358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(345);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(346);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(347);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(348);
				isComparable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(349);
				old();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(350);
				sConversion();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(351);
				match(FORALL);
				setState(352);
				boundVariables();
				setState(353);
				match(COLON);
				setState(354);
				match(COLON);
				setState(355);
				triggers();
				setState(356);
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
		enterRule(_localctx, 20, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
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
			setState(361);
			match(L_PAREN);
			setState(362);
			expression(0);
			setState(363);
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
		enterRule(_localctx, 22, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(365);
				trigger();
				}
				}
				setState(370);
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
		enterRule(_localctx, 24, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			match(L_CURLY);
			setState(372);
			expression(0);
			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(373);
				match(COMMA);
				setState(374);
				expression(0);
				}
				}
				setState(379);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(380);
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
		enterRule(_localctx, 26, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(OLD);
			setState(387);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(383);
				match(L_BRACKET);
				setState(384);
				oldLabelUse();
				setState(385);
				match(R_BRACKET);
				}
			}

			setState(389);
			match(L_PAREN);
			setState(390);
			expression(0);
			setState(391);
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
		enterRule(_localctx, 28, RULE_oldLabelUse);
		try {
			setState(395);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(393);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(394);
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
		enterRule(_localctx, 30, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
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
		enterRule(_localctx, 32, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(TYPE_OF);
			setState(400);
			match(L_PAREN);
			setState(401);
			expression(0);
			setState(402);
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
		enterRule(_localctx, 34, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(IS_COMPARABLE);
			setState(405);
			match(L_PAREN);
			setState(406);
			expression(0);
			setState(407);
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

	public static class GhostTypeLitContext extends ParserRuleContext {
		public SqTypeContext sqType() {
			return getRuleContext(SqTypeContext.class,0);
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
		enterRule(_localctx, 36, RULE_ghostTypeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			sqType();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 38, RULE_sqType);
		int _la;
		try {
			setState(422);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(411);
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
				setState(412);
				match(L_BRACKET);
				setState(413);
				type_();
				setState(414);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(416);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(417);
				match(L_BRACKET);
				setState(418);
				type_();
				setState(419);
				match(R_BRACKET);
				setState(420);
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
		enterRule(_localctx, 40, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(L_BRACKET);
			{
			setState(425);
			seqUpdClause();
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(426);
				match(COMMA);
				setState(427);
				seqUpdClause();
				}
				}
				setState(432);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(433);
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
		enterRule(_localctx, 42, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			expression(0);
			setState(436);
			match(ASSIGN);
			setState(437);
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

	public static class SpecificationContext extends ParserRuleContext {
		public List<TerminalNode> PURE() { return getTokens(GobraParser.PURE); }
		public TerminalNode PURE(int i) {
			return getToken(GobraParser.PURE, i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public List<SpecStatementContext> specStatement() {
			return getRuleContexts(SpecStatementContext.class);
		}
		public SpecStatementContext specStatement(int i) {
			return getRuleContext(SpecStatementContext.class,i);
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
		enterRule(_localctx, 44, RULE_specification);
		int _la;
		try {
			setState(458);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(444);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC))) != 0)) {
					{
					{
					{
					setState(439);
					specStatement();
					}
					setState(440);
					eos();
					}
					}
					setState(446);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(447);
				match(PURE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(455);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE))) != 0)) {
					{
					{
					setState(450);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(448);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(449);
						match(PURE);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(452);
					eos();
					}
					}
					setState(457);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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
		enterRule(_localctx, 46, RULE_specStatement);
		try {
			setState(468);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(460);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(461);
				assertion(0);
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(462);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(463);
				assertion(0);
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(464);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(465);
				assertion(0);
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(466);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(467);
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

	public static class FunctionDeclContext extends ParserRuleContext {
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			specification();
			setState(471);
			match(FUNC);
			setState(472);
			match(IDENTIFIER);
			{
			setState(473);
			signature();
			setState(475);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(474);
				block();
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
		public SpecificationContext specification() {
			return getRuleContext(SpecificationContext.class,0);
		}
		public TerminalNode FUNC() { return getToken(GobraParser.FUNC, 0); }
		public ReceiverContext receiver() {
			return getRuleContext(ReceiverContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MethodDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMethodDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodDeclContext methodDecl() throws RecognitionException {
		MethodDeclContext _localctx = new MethodDeclContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			specification();
			setState(478);
			match(FUNC);
			setState(479);
			receiver();
			setState(480);
			match(IDENTIFIER);
			{
			setState(481);
			signature();
			setState(483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(482);
				block();
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

	public static class AssertionContext extends ParserRuleContext {
		public Token kind;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<AssertionContext> assertion() {
			return getRuleContexts(AssertionContext.class);
		}
		public AssertionContext assertion(int i) {
			return getRuleContext(AssertionContext.class,i);
		}
		public TerminalNode EXCLAMATION() { return getToken(GobraParser.EXCLAMATION, 0); }
		public TerminalNode LOGICAL_AND() { return getToken(GobraParser.LOGICAL_AND, 0); }
		public TerminalNode LOGICAL_OR() { return getToken(GobraParser.LOGICAL_OR, 0); }
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
		return assertion(0);
	}

	private AssertionContext assertion(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		AssertionContext _localctx = new AssertionContext(_ctx, _parentState);
		AssertionContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_assertion, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(489);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				}
				break;
			case 2:
				{
				setState(486);
				expression(0);
				}
				break;
			case 3:
				{
				setState(487);
				((AssertionContext)_localctx).kind = match(EXCLAMATION);
				setState(488);
				assertion(3);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(499);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(497);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						_localctx = new AssertionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_assertion);
						setState(491);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(492);
						((AssertionContext)_localctx).kind = match(LOGICAL_AND);
						setState(493);
						assertion(3);
						}
						break;
					case 2:
						{
						_localctx = new AssertionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_assertion);
						setState(494);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(495);
						((AssertionContext)_localctx).kind = match(LOGICAL_OR);
						setState(496);
						assertion(2);
						}
						break;
					}
					} 
				}
				setState(501);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
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
		enterRule(_localctx, 54, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
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
			setState(503);
			match(L_BRACKET);
			setState(504);
			expression(0);
			setState(505);
			match(DOT_DOT);
			setState(506);
			expression(0);
			setState(507);
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
		public List<FunctionDeclContext> functionDecl() {
			return getRuleContexts(FunctionDeclContext.class);
		}
		public FunctionDeclContext functionDecl(int i) {
			return getRuleContext(FunctionDeclContext.class,i);
		}
		public List<MethodDeclContext> methodDecl() {
			return getRuleContexts(MethodDeclContext.class);
		}
		public MethodDeclContext methodDecl(int i) {
			return getRuleContext(MethodDeclContext.class,i);
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
		enterRule(_localctx, 56, RULE_sourceFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			packageClause();
			setState(510);
			eos();
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(511);
				importDecl();
				setState(512);
				eos();
				}
				}
				setState(518);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(529);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << PRED) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN) | (1L << CONST))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (TYPE - 65)) | (1L << (VAR - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (STAR - 65)) | (1L << (RECEIVE - 65)))) != 0)) {
				{
				{
				setState(523);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(519);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(520);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(521);
					declaration();
					}
					break;
				case 4:
					{
					setState(522);
					ghostMember();
					}
					break;
				}
				setState(525);
				eos();
				}
				}
				setState(531);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(532);
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
		public FpredicateDeclContext fpredicateDecl() {
			return getRuleContext(FpredicateDeclContext.class,0);
		}
		public MpredicateDeclContext mpredicateDecl() {
			return getRuleContext(MpredicateDeclContext.class,0);
		}
		public ImplementationProofContext implementationProof() {
			return getRuleContext(ImplementationProofContext.class,0);
		}
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public MethodDeclContext methodDecl() {
			return getRuleContext(MethodDeclContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public ConstDeclContext constDecl() {
			return getRuleContext(ConstDeclContext.class,0);
		}
		public TypeDeclContext typeDecl() {
			return getRuleContext(TypeDeclContext.class,0);
		}
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
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
		enterRule(_localctx, 58, RULE_ghostMember);
		try {
			setState(546);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(534);
				fpredicateDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(535);
				mpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(536);
				implementationProof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(537);
				match(GHOST);
				setState(538);
				eos();
				setState(544);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(539);
					methodDecl();
					}
					break;
				case 2:
					{
					setState(540);
					functionDecl();
					}
					break;
				case 3:
					{
					setState(541);
					constDecl();
					}
					break;
				case 4:
					{
					setState(542);
					typeDecl();
					}
					break;
				case 5:
					{
					setState(543);
					varDecl();
					}
					break;
				}
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
		enterRule(_localctx, 60, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			match(PRED);
			setState(549);
			match(IDENTIFIER);
			setState(550);
			parameters();
			setState(551);
			predicateBody();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 62, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			match(L_CURLY);
			setState(554);
			expression(0);
			setState(555);
			eos();
			setState(556);
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
		enterRule(_localctx, 64, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(558);
			match(PRED);
			setState(559);
			receiver();
			setState(560);
			match(IDENTIFIER);
			setState(561);
			parameters();
			setState(562);
			predicateBody();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 66, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			type_();
			setState(565);
			match(IMPL);
			setState(566);
			type_();
			setState(567);
			match(L_CURLY);
			setState(573);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRED) {
				{
				{
				setState(568);
				implementationProofPredicateAlias();
				setState(569);
				eos();
				}
				}
				setState(575);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(581);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PURE || _la==L_PAREN) {
				{
				{
				setState(576);
				methodImplementationProof();
				setState(577);
				eos();
				}
				}
				setState(583);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(584);
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
		enterRule(_localctx, 68, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(587);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(586);
				match(PURE);
				}
			}

			setState(589);
			nonLocalReceiver();
			setState(590);
			match(IDENTIFIER);
			setState(591);
			signature();
			setState(593);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(592);
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

	public static class SelectionContext extends ParserRuleContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
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
		enterRule(_localctx, 70, RULE_selection);
		try {
			setState(603);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(595);
				primaryExpr(0);
				setState(596);
				match(DOT);
				setState(597);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(599);
				type_();
				setState(600);
				match(DOT);
				setState(601);
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
		enterRule(_localctx, 72, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(605);
			match(PRED);
			setState(606);
			match(IDENTIFIER);
			setState(607);
			match(DECLARE_ASSIGN);
			setState(610);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(608);
				selection();
				}
				break;
			case 2:
				{
				setState(609);
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
		enterRule(_localctx, 74, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			maybeAddressableIdentifierList();
			setState(620);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
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
				setState(613);
				type_();
				setState(616);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(614);
					match(ASSIGN);
					setState(615);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(618);
				match(ASSIGN);
				setState(619);
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
		enterRule(_localctx, 76, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(622);
			maybeAddressableIdentifierList();
			setState(623);
			match(DECLARE_ASSIGN);
			setState(624);
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
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public MaybeAddressableIdentifierContext maybeAddressableIdentifier() {
			return getRuleContext(MaybeAddressableIdentifierContext.class,0);
		}
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
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
		enterRule(_localctx, 78, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(626);
			match(L_PAREN);
			setState(628);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(627);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(631);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(630);
				match(STAR);
				}
			}

			setState(633);
			match(IDENTIFIER);
			setState(634);
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
		enterRule(_localctx, 80, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(636);
			match(L_PAREN);
			setState(638);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(637);
				match(IDENTIFIER);
				}
				break;
			}
			setState(641);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(640);
				match(STAR);
				}
			}

			setState(643);
			typeName();
			setState(644);
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
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode ELLIPSIS() { return getToken(GobraParser.ELLIPSIS, 0); }
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
		enterRule(_localctx, 82, RULE_parameterDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GHOST) {
				{
				setState(646);
				match(GHOST);
				}
			}

			setState(650);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(649);
				identifierList();
				}
				break;
			}
			setState(653);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(652);
				match(ELLIPSIS);
				}
			}

			setState(655);
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

	public static class UnaryExprContext extends ParserRuleContext {
		public Token kind;
		public Token unary_op;
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode LEN() { return getToken(GobraParser.LEN, 0); }
		public TerminalNode CAP() { return getToken(GobraParser.CAP, 0); }
		public TerminalNode DOM() { return getToken(GobraParser.DOM, 0); }
		public TerminalNode RANGE() { return getToken(GobraParser.RANGE, 0); }
		public UnfoldingContext unfolding() {
			return getRuleContext(UnfoldingContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(GobraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(GobraParser.MINUS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(GobraParser.EXCLAMATION, 0); }
		public TerminalNode CARET() { return getToken(GobraParser.CARET, 0); }
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public TerminalNode RECEIVE() { return getToken(GobraParser.RECEIVE, 0); }
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_unaryExpr);
		int _la;
		try {
			setState(666);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(657);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(658);
				((UnaryExprContext)_localctx).kind = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << RANGE) | (1L << LEN) | (1L << CAP) | (1L << DOM))) != 0)) ) {
					((UnaryExprContext)_localctx).kind = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(659);
				match(L_PAREN);
				setState(660);
				expression(0);
				setState(661);
				match(R_PAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(663);
				unfolding();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(664);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 102)) & ~0x3f) == 0 && ((1L << (_la - 102)) & ((1L << (EXCLAMATION - 102)) | (1L << (PLUS - 102)) | (1L << (MINUS - 102)) | (1L << (CARET - 102)) | (1L << (STAR - 102)) | (1L << (AMPERSAND - 102)) | (1L << (RECEIVE - 102)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(665);
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

	public static class UnfoldingContext extends ParserRuleContext {
		public TerminalNode UNFOLDING() { return getToken(GobraParser.UNFOLDING, 0); }
		public PredicateAccessContext predicateAccess() {
			return getRuleContext(PredicateAccessContext.class,0);
		}
		public TerminalNode IN() { return getToken(GobraParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnfoldingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unfolding; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitUnfolding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnfoldingContext unfolding() throws RecognitionException {
		UnfoldingContext _localctx = new UnfoldingContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_unfolding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(668);
			match(UNFOLDING);
			setState(669);
			predicateAccess();
			setState(670);
			match(IN);
			setState(671);
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

	public static class ExpressionContext extends ParserRuleContext {
		public Token mul_op;
		public Token add_op;
		public Token p42_op;
		public Token p41_op;
		public Token rel_op;
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public UnaryExprContext unaryExpr() {
			return getRuleContext(UnaryExprContext.class,0);
		}
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
		public TerminalNode PLUS() { return getToken(GobraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(GobraParser.MINUS, 0); }
		public TerminalNode OR() { return getToken(GobraParser.OR, 0); }
		public TerminalNode CARET() { return getToken(GobraParser.CARET, 0); }
		public TerminalNode PLUS_PLUS() { return getToken(GobraParser.PLUS_PLUS, 0); }
		public TerminalNode UNION() { return getToken(GobraParser.UNION, 0); }
		public TerminalNode INTERSECTION() { return getToken(GobraParser.INTERSECTION, 0); }
		public TerminalNode SETMINUS() { return getToken(GobraParser.SETMINUS, 0); }
		public TerminalNode IN() { return getToken(GobraParser.IN, 0); }
		public TerminalNode MULTI() { return getToken(GobraParser.MULTI, 0); }
		public TerminalNode SUBSET() { return getToken(GobraParser.SUBSET, 0); }
		public TerminalNode EQUALS() { return getToken(GobraParser.EQUALS, 0); }
		public TerminalNode NOT_EQUALS() { return getToken(GobraParser.NOT_EQUALS, 0); }
		public TerminalNode LESS() { return getToken(GobraParser.LESS, 0); }
		public TerminalNode LESS_OR_EQUALS() { return getToken(GobraParser.LESS_OR_EQUALS, 0); }
		public TerminalNode GREATER() { return getToken(GobraParser.GREATER, 0); }
		public TerminalNode GREATER_OR_EQUALS() { return getToken(GobraParser.GREATER_OR_EQUALS, 0); }
		public TerminalNode LOGICAL_AND() { return getToken(GobraParser.LOGICAL_AND, 0); }
		public TerminalNode LOGICAL_OR() { return getToken(GobraParser.LOGICAL_OR, 0); }
		public TerminalNode IMPLIES() { return getToken(GobraParser.IMPLIES, 0); }
		public TerminalNode QMARK() { return getToken(GobraParser.QMARK, 0); }
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitExpression(this);
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
		int _startState = 88;
		enterRecursionRule(_localctx, 88, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(674);
				primaryExpr(0);
				}
				break;
			case 2:
				{
				setState(675);
				unaryExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(710);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(708);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(678);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(679);
						((ExpressionContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (DIV - 97)) | (1L << (MOD - 97)) | (1L << (LSHIFT - 97)) | (1L << (RSHIFT - 97)) | (1L << (BIT_CLEAR - 97)) | (1L << (STAR - 97)) | (1L << (AMPERSAND - 97)))) != 0)) ) {
							((ExpressionContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(680);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(681);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(682);
						((ExpressionContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 84)) & ~0x3f) == 0 && ((1L << (_la - 84)) & ((1L << (PLUS_PLUS - 84)) | (1L << (OR - 84)) | (1L << (PLUS - 84)) | (1L << (MINUS - 84)) | (1L << (CARET - 84)))) != 0)) ) {
							((ExpressionContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(683);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(684);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(685);
						((ExpressionContext)_localctx).p42_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UNION) | (1L << INTERSECTION) | (1L << SETMINUS))) != 0)) ) {
							((ExpressionContext)_localctx).p42_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(686);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(687);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(688);
						((ExpressionContext)_localctx).p41_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IN) | (1L << MULTI) | (1L << SUBSET))) != 0)) ) {
							((ExpressionContext)_localctx).p41_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(689);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(690);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(691);
						((ExpressionContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 90)) & ~0x3f) == 0 && ((1L << (_la - 90)) & ((1L << (EQUALS - 90)) | (1L << (NOT_EQUALS - 90)) | (1L << (LESS - 90)) | (1L << (LESS_OR_EQUALS - 90)) | (1L << (GREATER - 90)) | (1L << (GREATER_OR_EQUALS - 90)))) != 0)) ) {
							((ExpressionContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(692);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(693);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(694);
						match(LOGICAL_AND);
						setState(695);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(696);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(697);
						match(LOGICAL_OR);
						setState(698);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(699);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(700);
						match(IMPLIES);
						setState(701);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(702);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(703);
						match(QMARK);
						setState(704);
						expression(0);
						setState(705);
						match(COLON);
						setState(706);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(712);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
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
		enterRule(_localctx, 90, RULE_statement);
		try {
			setState(729);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(713);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(714);
				declaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(715);
				labeledStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(716);
				simpleStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(717);
				goStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(718);
				returnStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(719);
				breakStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(720);
				continueStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(721);
				gotoStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(722);
				fallthroughStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(723);
				block();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(724);
				ifStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(725);
				switchStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(726);
				selectStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(727);
				specForStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(728);
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
		enterRule(_localctx, 92, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(731);
			loopSpec();
			setState(732);
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
		enterRule(_localctx, 94, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(740);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(734);
				match(INV);
				setState(735);
				expression(0);
				setState(736);
				eos();
				}
				}
				setState(742);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(747);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(743);
				match(DEC);
				setState(744);
				terminationMeasure();
				setState(745);
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
		enterRule(_localctx, 96, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(749);
			expressionList();
			{
			setState(750);
			match(IF);
			setState(751);
			expression(0);
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
		enterRule(_localctx, 98, RULE_basicLit);
		try {
			setState(761);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(753);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(754);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(755);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(756);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(757);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(758);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(759);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(760);
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
		public OperandContext operand() {
			return getRuleContext(OperandContext.class,0);
		}
		public ConversionContext conversion() {
			return getRuleContext(ConversionContext.class,0);
		}
		public MethodExprContext methodExpr() {
			return getRuleContext(MethodExprContext.class,0);
		}
		public GhostPrimaryExprContext ghostPrimaryExpr() {
			return getRuleContext(GhostPrimaryExprContext.class,0);
		}
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public Slice_Context slice_() {
			return getRuleContext(Slice_Context.class,0);
		}
		public SeqUpdExpContext seqUpdExp() {
			return getRuleContext(SeqUpdExpContext.class,0);
		}
		public TypeAssertionContext typeAssertion() {
			return getRuleContext(TypeAssertionContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public PrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPrimaryExpr(this);
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
		int _startState = 100;
		enterRecursionRule(_localctx, 100, RULE_primaryExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(768);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(764);
				operand();
				}
				break;
			case 2:
				{
				setState(765);
				conversion();
				}
				break;
			case 3:
				{
				setState(766);
				methodExpr();
				}
				break;
			case 4:
				{
				setState(767);
				ghostPrimaryExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(782);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrimaryExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
					setState(770);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(778);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
					case 1:
						{
						{
						setState(771);
						match(DOT);
						setState(772);
						match(IDENTIFIER);
						}
						}
						break;
					case 2:
						{
						setState(773);
						index();
						}
						break;
					case 3:
						{
						setState(774);
						slice_();
						}
						break;
					case 4:
						{
						setState(775);
						seqUpdExp();
						}
						break;
					case 5:
						{
						setState(776);
						typeAssertion();
						}
						break;
					case 6:
						{
						setState(777);
						arguments();
						}
						break;
					}
					}
					} 
				}
				setState(784);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
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
		enterRule(_localctx, 102, RULE_interfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(785);
			match(INTERFACE);
			setState(786);
			match(L_CURLY);
			setState(796);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(790);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
					case 1:
						{
						setState(787);
						methodSpec();
						}
						break;
					case 2:
						{
						setState(788);
						typeName();
						}
						break;
					case 3:
						{
						setState(789);
						predicateSpec();
						}
						break;
					}
					setState(792);
					eos();
					}
					} 
				}
				setState(798);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			}
			setState(799);
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
		enterRule(_localctx, 104, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			match(PRED);
			setState(802);
			match(IDENTIFIER);
			setState(803);
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
		enterRule(_localctx, 106, RULE_methodSpec);
		int _la;
		try {
			setState(821);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(805);
				if (!(noTerminatorAfterParams(2))) throw new FailedPredicateException(this, "noTerminatorAfterParams(2)");
				setState(807);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(806);
					match(GHOST);
					}
				}

				setState(809);
				specification();
				setState(810);
				match(IDENTIFIER);
				setState(811);
				parameters();
				setState(812);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(815);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(814);
					match(GHOST);
					}
				}

				setState(817);
				specification();
				setState(818);
				match(IDENTIFIER);
				setState(819);
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
		enterRule(_localctx, 108, RULE_type_);
		try {
			setState(830);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(823);
				typeName();
				}
				break;
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
				setState(824);
				typeLit();
				}
				break;
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 3);
				{
				setState(825);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(826);
				match(L_PAREN);
				setState(827);
				type_();
				setState(828);
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

	public static class LiteralTypeContext extends ParserRuleContext {
		public StructTypeContext structType() {
			return getRuleContext(StructTypeContext.class,0);
		}
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public TerminalNode ELLIPSIS() { return getToken(GobraParser.ELLIPSIS, 0); }
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
		public ElementTypeContext elementType() {
			return getRuleContext(ElementTypeContext.class,0);
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
		enterRule(_localctx, 110, RULE_literalType);
		try {
			setState(842);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(832);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(833);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(834);
				match(L_BRACKET);
				setState(835);
				match(ELLIPSIS);
				setState(836);
				match(R_BRACKET);
				setState(837);
				elementType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(838);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(839);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(840);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(841);
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
		enterRule(_localctx, 112, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			match(L_BRACKET);
			setState(860);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				{
				setState(846);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
					{
					setState(845);
					low();
					}
				}

				setState(848);
				match(COLON);
				setState(850);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
					{
					setState(849);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(853);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
					{
					setState(852);
					low();
					}
				}

				setState(855);
				match(COLON);
				setState(856);
				high();
				setState(857);
				match(COLON);
				setState(858);
				cap();
				}
				break;
			}
			setState(862);
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
		enterRule(_localctx, 114, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
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
		enterRule(_localctx, 116, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
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
		enterRule(_localctx, 118, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
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
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public TerminalNode ELSE() { return getToken(GobraParser.ELSE, 0); }
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
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
		enterRule(_localctx, 120, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
			match(IF);
			setState(875);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(872);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
				case 1:
					{
					setState(871);
					simpleStmt();
					}
					break;
				}
				setState(874);
				match(SEMI);
				}
				break;
			}
			setState(877);
			expression(0);
			setState(878);
			block();
			setState(884);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(879);
				match(ELSE);
				setState(882);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(880);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(881);
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

	public static class ExprSwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(GobraParser.SWITCH, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<ExprCaseClauseContext> exprCaseClause() {
			return getRuleContexts(ExprCaseClauseContext.class);
		}
		public ExprCaseClauseContext exprCaseClause(int i) {
			return getRuleContext(ExprCaseClauseContext.class,i);
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
		enterRule(_localctx, 122, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(886);
			match(SWITCH);
			setState(891);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(888);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
				case 1:
					{
					setState(887);
					simpleStmt();
					}
					break;
				}
				setState(890);
				match(SEMI);
				}
				break;
			}
			setState(894);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
				{
				setState(893);
				expression(0);
				}
			}

			setState(896);
			match(L_CURLY);
			setState(900);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(897);
				exprCaseClause();
				}
				}
				setState(902);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(903);
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

	public static class TypeSwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(GobraParser.SWITCH, 0); }
		public TypeSwitchGuardContext typeSwitchGuard() {
			return getRuleContext(TypeSwitchGuardContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public List<TypeCaseClauseContext> typeCaseClause() {
			return getRuleContexts(TypeCaseClauseContext.class);
		}
		public TypeCaseClauseContext typeCaseClause(int i) {
			return getRuleContext(TypeCaseClauseContext.class,i);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
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
		enterRule(_localctx, 124, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(905);
			match(SWITCH);
			setState(910);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				{
				setState(907);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
				case 1:
					{
					setState(906);
					simpleStmt();
					}
					break;
				}
				setState(909);
				match(SEMI);
				}
				break;
			}
			setState(912);
			typeSwitchGuard();
			setState(913);
			match(L_CURLY);
			setState(917);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(914);
				typeCaseClause();
				}
				}
				setState(919);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(920);
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

	public static class EosContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public TerminalNode EOF() { return getToken(GobraParser.EOF, 0); }
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
		enterRule(_localctx, 126, RULE_eos);
		try {
			setState(927);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(922);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(923);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(924);
				if (!(lineTerminatorAhead())) throw new FailedPredicateException(this, "lineTerminatorAhead()");
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(925);
				if (!(checkPreviousTokenText("}"))) throw new FailedPredicateException(this, "checkPrevi   ousTokenText(\"}\")");
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(926);
				if (!(checkPreviousTokenText(")"))) throw new FailedPredicateException(this, "checkPreviousTokenText(\")\")");
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
		enterRule(_localctx, 128, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			match(PACKAGE);
			setState(930);
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
		enterRule(_localctx, 130, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(932);
			match(IMPORT);
			setState(944);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(933);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(934);
				match(L_PAREN);
				setState(940);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (IDENTIFIER - 72)) | (1L << (DOT - 72)) | (1L << (RAW_STRING_LIT - 72)) | (1L << (INTERPRETED_STRING_LIT - 72)))) != 0)) {
					{
					{
					setState(935);
					importSpec();
					setState(936);
					eos();
					}
					}
					setState(942);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(943);
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
		enterRule(_localctx, 132, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(946);
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

			setState(949);
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
		enterRule(_localctx, 134, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
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
		enterRule(_localctx, 136, RULE_declaration);
		try {
			setState(956);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(953);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(954);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(955);
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
		enterRule(_localctx, 138, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(958);
			match(CONST);
			setState(970);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(959);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(960);
				match(L_PAREN);
				setState(966);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(961);
					constSpec();
					setState(962);
					eos();
					}
					}
					setState(968);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(969);
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
		enterRule(_localctx, 140, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(972);
			identifierList();
			setState(978);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (IDENTIFIER - 72)) | (1L << (L_PAREN - 72)) | (1L << (L_BRACKET - 72)) | (1L << (STAR - 72)) | (1L << (RECEIVE - 72)))) != 0)) {
					{
					setState(973);
					type_();
					}
				}

				setState(976);
				match(ASSIGN);
				setState(977);
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
		enterRule(_localctx, 142, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(980);
			match(IDENTIFIER);
			setState(985);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,87,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(981);
					match(COMMA);
					setState(982);
					match(IDENTIFIER);
					}
					} 
				}
				setState(987);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,87,_ctx);
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
		enterRule(_localctx, 144, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			expression(0);
			setState(993);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(989);
					match(COMMA);
					setState(990);
					expression(0);
					}
					} 
				}
				setState(995);
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
		enterRule(_localctx, 146, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(996);
			match(TYPE);
			setState(1008);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(997);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(998);
				match(L_PAREN);
				setState(1004);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(999);
					typeSpec();
					setState(1000);
					eos();
					}
					}
					setState(1006);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1007);
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
		enterRule(_localctx, 148, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			match(IDENTIFIER);
			setState(1012);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1011);
				match(ASSIGN);
				}
			}

			setState(1014);
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
		enterRule(_localctx, 150, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1016);
			match(VAR);
			setState(1028);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1017);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1018);
				match(L_PAREN);
				setState(1024);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1019);
					varSpec();
					setState(1020);
					eos();
					}
					}
					setState(1026);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1027);
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
		enterRule(_localctx, 152, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1030);
			match(L_CURLY);
			setState(1032);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN) | (1L << GOTO) | (1L << SWITCH) | (1L << CONST) | (1L << FALLTHROUGH))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)) | (1L << (RAW_STRING_LIT - 64)) | (1L << (INTERPRETED_STRING_LIT - 64)))) != 0)) {
				{
				setState(1031);
				statementList();
				}
			}

			setState(1034);
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
		enterRule(_localctx, 154, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1036);
				statement();
				setState(1037);
				eos();
				}
				}
				setState(1041); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN) | (1L << GOTO) | (1L << SWITCH) | (1L << CONST) | (1L << FALLTHROUGH))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)) | (1L << (RAW_STRING_LIT - 64)) | (1L << (INTERPRETED_STRING_LIT - 64)))) != 0) );
			}
		}
		catch (RecognitionException re) {
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
		public EmptyStmtContext emptyStmt() {
			return getRuleContext(EmptyStmtContext.class,0);
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
		enterRule(_localctx, 156, RULE_simpleStmt);
		try {
			setState(1049);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1043);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1044);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1045);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1046);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1047);
				shortVarDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1048);
				emptyStmt();
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
		enterRule(_localctx, 158, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1051);
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
		enterRule(_localctx, 160, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1053);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1054);
			match(RECEIVE);
			setState(1055);
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
		enterRule(_localctx, 162, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
			expression(0);
			setState(1058);
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
		enterRule(_localctx, 164, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1060);
			expressionList();
			setState(1061);
			assign_op();
			setState(1062);
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

	public static class Assign_opContext extends ParserRuleContext {
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
		enterRule(_localctx, 166, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1065);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (OR - 96)) | (1L << (DIV - 96)) | (1L << (MOD - 96)) | (1L << (LSHIFT - 96)) | (1L << (RSHIFT - 96)) | (1L << (BIT_CLEAR - 96)) | (1L << (PLUS - 96)) | (1L << (MINUS - 96)) | (1L << (CARET - 96)) | (1L << (STAR - 96)) | (1L << (AMPERSAND - 96)))) != 0)) {
				{
				setState(1064);
				_la = _input.LA(1);
				if ( !(((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (OR - 96)) | (1L << (DIV - 96)) | (1L << (MOD - 96)) | (1L << (LSHIFT - 96)) | (1L << (RSHIFT - 96)) | (1L << (BIT_CLEAR - 96)) | (1L << (PLUS - 96)) | (1L << (MINUS - 96)) | (1L << (CARET - 96)) | (1L << (STAR - 96)) | (1L << (AMPERSAND - 96)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1067);
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

	public static class EmptyStmtContext extends ParserRuleContext {
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
		enterRule(_localctx, 168, RULE_emptyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1069);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 170, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			match(IDENTIFIER);
			setState(1072);
			match(COLON);
			setState(1074);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
			case 1:
				{
				setState(1073);
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
		enterRule(_localctx, 172, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1076);
			match(RETURN);
			setState(1078);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(1077);
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
		enterRule(_localctx, 174, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1080);
			match(BREAK);
			setState(1082);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				{
				setState(1081);
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
		enterRule(_localctx, 176, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1084);
			match(CONTINUE);
			setState(1086);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				{
				setState(1085);
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
		enterRule(_localctx, 178, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1088);
			match(GOTO);
			setState(1089);
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
		enterRule(_localctx, 180, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1091);
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
		enterRule(_localctx, 182, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1093);
			match(DEFER);
			setState(1094);
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
		enterRule(_localctx, 184, RULE_switchStmt);
		try {
			setState(1098);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1096);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1097);
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
		enterRule(_localctx, 186, RULE_exprCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1100);
			exprSwitchCase();
			setState(1101);
			match(COLON);
			setState(1103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN) | (1L << GOTO) | (1L << SWITCH) | (1L << CONST) | (1L << FALLTHROUGH))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)) | (1L << (RAW_STRING_LIT - 64)) | (1L << (INTERPRETED_STRING_LIT - 64)))) != 0)) {
				{
				setState(1102);
				statementList();
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
		enterRule(_localctx, 188, RULE_exprSwitchCase);
		try {
			setState(1108);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1105);
				match(CASE);
				setState(1106);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1107);
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
		enterRule(_localctx, 190, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1112);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1110);
				match(IDENTIFIER);
				setState(1111);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1114);
			primaryExpr(0);
			setState(1115);
			match(DOT);
			setState(1116);
			match(L_PAREN);
			setState(1117);
			match(TYPE);
			setState(1118);
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
		enterRule(_localctx, 192, RULE_typeCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1120);
			typeSwitchCase();
			setState(1121);
			match(COLON);
			setState(1123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN) | (1L << GOTO) | (1L << SWITCH) | (1L << CONST) | (1L << FALLTHROUGH))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)) | (1L << (RAW_STRING_LIT - 64)) | (1L << (INTERPRETED_STRING_LIT - 64)))) != 0)) {
				{
				setState(1122);
				statementList();
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
		enterRule(_localctx, 194, RULE_typeSwitchCase);
		try {
			setState(1128);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1125);
				match(CASE);
				setState(1126);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1127);
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
		enterRule(_localctx, 196, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1132);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
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
				setState(1130);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1131);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1134);
				match(COMMA);
				setState(1137);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SEQ:
				case SET:
				case MSET:
				case DICT:
				case OPT:
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
					setState(1135);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1136);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1143);
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
		enterRule(_localctx, 198, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1144);
			match(SELECT);
			setState(1145);
			match(L_CURLY);
			setState(1149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1146);
				commClause();
				}
				}
				setState(1151);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1152);
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
		enterRule(_localctx, 200, RULE_commClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1154);
			commCase();
			setState(1155);
			match(COLON);
			setState(1157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN) | (1L << GOTO) | (1L << SWITCH) | (1L << CONST) | (1L << FALLTHROUGH))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)) | (1L << (RAW_STRING_LIT - 64)) | (1L << (INTERPRETED_STRING_LIT - 64)))) != 0)) {
				{
				setState(1156);
				statementList();
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
		enterRule(_localctx, 202, RULE_commCase);
		try {
			setState(1165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1159);
				match(CASE);
				setState(1162);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
				case 1:
					{
					setState(1160);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1161);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1164);
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
		enterRule(_localctx, 204, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1173);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				{
				setState(1167);
				expressionList();
				setState(1168);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1170);
				identifierList();
				setState(1171);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1175);
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
		enterRule(_localctx, 206, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1177);
			match(FOR);
			setState(1181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1178);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1179);
				forClause();
				}
				break;
			case 3:
				{
				setState(1180);
				rangeClause();
				}
				break;
			}
			setState(1183);
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
		public List<TerminalNode> SEMI() { return getTokens(GobraParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(GobraParser.SEMI, i);
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
		enterRule(_localctx, 208, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1186);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				{
				setState(1185);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1188);
			match(SEMI);
			setState(1190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
				{
				setState(1189);
				expression(0);
				}
			}

			setState(1192);
			match(SEMI);
			setState(1194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (SEMI - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
				{
				setState(1193);
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
		enterRule(_localctx, 210, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1202);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1196);
				expressionList();
				setState(1197);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1199);
				identifierList();
				setState(1200);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1204);
			match(RANGE);
			setState(1205);
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
		enterRule(_localctx, 212, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			match(GO);
			setState(1208);
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
		enterRule(_localctx, 214, RULE_typeName);
		try {
			setState(1212);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1210);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1211);
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
		enterRule(_localctx, 216, RULE_typeLit);
		try {
			setState(1222);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1214);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1215);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1216);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1217);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1218);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1219);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1220);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1221);
				channelType();
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
		enterRule(_localctx, 218, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1224);
			match(L_BRACKET);
			setState(1225);
			arrayLength();
			setState(1226);
			match(R_BRACKET);
			setState(1227);
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
		enterRule(_localctx, 220, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1229);
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
		enterRule(_localctx, 222, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
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
		enterRule(_localctx, 224, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1233);
			match(STAR);
			setState(1234);
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
		enterRule(_localctx, 226, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1236);
			match(L_BRACKET);
			setState(1237);
			match(R_BRACKET);
			setState(1238);
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
		enterRule(_localctx, 228, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1240);
			match(MAP);
			setState(1241);
			match(L_BRACKET);
			setState(1242);
			type_();
			setState(1243);
			match(R_BRACKET);
			setState(1244);
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
		enterRule(_localctx, 230, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				{
				setState(1246);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1247);
				match(CHAN);
				setState(1248);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1249);
				match(RECEIVE);
				setState(1250);
				match(CHAN);
				}
				break;
			}
			setState(1253);
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
		enterRule(_localctx, 232, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1255);
			match(FUNC);
			setState(1256);
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
		enterRule(_localctx, 234, RULE_signature);
		try {
			setState(1263);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1258);
				if (!(noTerminatorAfterParams(1))) throw new FailedPredicateException(this, "noTerminatorAfterParams(1)");
				setState(1259);
				parameters();
				setState(1260);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1262);
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
		enterRule(_localctx, 236, RULE_result);
		try {
			setState(1267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1265);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1266);
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
		enterRule(_localctx, 238, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1269);
			match(L_PAREN);
			setState(1281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (IDENTIFIER - 72)) | (1L << (L_PAREN - 72)) | (1L << (L_BRACKET - 72)) | (1L << (ELLIPSIS - 72)) | (1L << (STAR - 72)) | (1L << (RECEIVE - 72)))) != 0)) {
				{
				setState(1270);
				parameterDecl();
				setState(1275);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,126,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1271);
						match(COMMA);
						setState(1272);
						parameterDecl();
						}
						} 
					}
					setState(1277);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,126,_ctx);
				}
				setState(1279);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1278);
					match(COMMA);
					}
				}

				}
			}

			setState(1283);
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
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
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
		enterRule(_localctx, 240, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1285);
			type_();
			setState(1286);
			match(L_PAREN);
			setState(1287);
			expression(0);
			setState(1289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1288);
				match(COMMA);
				}
			}

			setState(1291);
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
		enterRule(_localctx, 242, RULE_operand);
		try {
			setState(1299);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1293);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1294);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1295);
				match(L_PAREN);
				setState(1296);
				expression(0);
				setState(1297);
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
		enterRule(_localctx, 244, RULE_literal);
		try {
			setState(1304);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NIL_LIT:
			case DECIMAL_LIT:
			case BINARY_LIT:
			case OCTAL_LIT:
			case HEX_LIT:
			case FLOAT_LIT:
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1301);
				basicLit();
				}
				break;
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case MAP:
			case STRUCT:
			case IDENTIFIER:
			case L_BRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(1302);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1303);
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
		enterRule(_localctx, 246, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1306);
			_la = _input.LA(1);
			if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & ((1L << (DECIMAL_LIT - 109)) | (1L << (BINARY_LIT - 109)) | (1L << (OCTAL_LIT - 109)) | (1L << (HEX_LIT - 109)) | (1L << (IMAGINARY_LIT - 109)) | (1L << (RUNE_LIT - 109)))) != 0)) ) {
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
		public List<TerminalNode> IDENTIFIER() { return getTokens(GobraParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(GobraParser.IDENTIFIER, i);
		}
		public TerminalNode DOT() { return getToken(GobraParser.DOT, 0); }
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
		enterRule(_localctx, 248, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1308);
			match(IDENTIFIER);
			setState(1311);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1309);
				match(DOT);
				setState(1310);
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
		enterRule(_localctx, 250, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1313);
			match(IDENTIFIER);
			setState(1314);
			match(DOT);
			setState(1315);
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
		enterRule(_localctx, 252, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
			literalType();
			setState(1318);
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
		enterRule(_localctx, 254, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1320);
			match(L_CURLY);
			setState(1325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_CURLY - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
				{
				setState(1321);
				elementList();
				setState(1323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1322);
					match(COMMA);
					}
				}

				}
			}

			setState(1327);
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
		enterRule(_localctx, 256, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1329);
			keyedElement();
			setState(1334);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,135,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1330);
					match(COMMA);
					setState(1331);
					keyedElement();
					}
					} 
				}
				setState(1336);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,135,_ctx);
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
		enterRule(_localctx, 258, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1340);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1337);
				key();
				setState(1338);
				match(COLON);
				}
				break;
			}
			setState(1342);
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
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 260, RULE_key);
		try {
			setState(1347);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1344);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1345);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1346);
				literalValue();
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
		enterRule(_localctx, 262, RULE_element);
		try {
			setState(1351);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case OLD:
			case FORALL:
			case ACCESS:
			case UNFOLDING:
			case RANGE:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case LEN:
			case CAP:
			case DOM:
			case TYPE_OF:
			case IS_COMPARABLE:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
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
			case FLOAT_LIT:
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1349);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1350);
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
		enterRule(_localctx, 264, RULE_structType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1353);
			match(STRUCT);
			setState(1354);
			match(L_CURLY);
			setState(1360);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1355);
					fieldDecl();
					setState(1356);
					eos();
					}
					} 
				}
				setState(1362);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
			}
			setState(1363);
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
		enterRule(_localctx, 266, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				{
				setState(1365);
				if (!(noTerminatorBetween(2))) throw new FailedPredicateException(this, "noTerminatorBetween(2)");
				setState(1366);
				identifierList();
				setState(1367);
				type_();
				}
				break;
			case 2:
				{
				setState(1369);
				embeddedField();
				}
				break;
			}
			setState(1373);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				{
				setState(1372);
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
		enterRule(_localctx, 268, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1375);
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
		enterRule(_localctx, 270, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1378);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1377);
				match(STAR);
				}
			}

			setState(1380);
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
		enterRule(_localctx, 272, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1382);
			match(FUNC);
			setState(1383);
			signature();
			setState(1384);
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
		enterRule(_localctx, 274, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1386);
			match(L_BRACKET);
			setState(1387);
			expression(0);
			setState(1388);
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
		enterRule(_localctx, 276, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1390);
			match(DOT);
			setState(1391);
			match(L_PAREN);
			setState(1392);
			type_();
			setState(1393);
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
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
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
		enterRule(_localctx, 278, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1395);
			match(L_PAREN);
			setState(1410);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << CAP) | (1L << DOM) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT) | (1L << CHAN))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (NIL_LIT - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (EXCLAMATION - 71)) | (1L << (PLUS - 71)) | (1L << (MINUS - 71)) | (1L << (CARET - 71)) | (1L << (STAR - 71)) | (1L << (AMPERSAND - 71)) | (1L << (RECEIVE - 71)) | (1L << (DECIMAL_LIT - 71)) | (1L << (BINARY_LIT - 71)) | (1L << (OCTAL_LIT - 71)) | (1L << (HEX_LIT - 71)) | (1L << (FLOAT_LIT - 71)) | (1L << (IMAGINARY_LIT - 71)) | (1L << (RUNE_LIT - 71)) | (1L << (RAW_STRING_LIT - 71)) | (1L << (INTERPRETED_STRING_LIT - 71)))) != 0)) {
				{
				setState(1402);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
				case 1:
					{
					setState(1396);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1397);
					type_();
					setState(1400);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
					case 1:
						{
						setState(1398);
						match(COMMA);
						setState(1399);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1404);
					match(ELLIPSIS);
					}
				}

				setState(1408);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1407);
					match(COMMA);
					}
				}

				}
			}

			setState(1412);
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
		public ReceiverTypeContext receiverType() {
			return getRuleContext(ReceiverTypeContext.class,0);
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
		enterRule(_localctx, 280, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1414);
			receiverType();
			setState(1415);
			match(DOT);
			setState(1416);
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
		enterRule(_localctx, 282, RULE_receiverType);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 26:
			return assertion_sempred((AssertionContext)_localctx, predIndex);
		case 44:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 50:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 53:
			return methodSpec_sempred((MethodSpecContext)_localctx, predIndex);
		case 63:
			return eos_sempred((EosContext)_localctx, predIndex);
		case 117:
			return signature_sempred((SignatureContext)_localctx, predIndex);
		case 133:
			return fieldDecl_sempred((FieldDeclContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean assertion_sempred(AssertionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
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
		case 9:
			return precpred(_ctx, 2);
		case 10:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean primaryExpr_sempred(PrimaryExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 11:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean methodSpec_sempred(MethodSpecContext _localctx, int predIndex) {
		switch (predIndex) {
		case 12:
			return noTerminatorAfterParams(2);
		}
		return true;
	}
	private boolean eos_sempred(EosContext _localctx, int predIndex) {
		switch (predIndex) {
		case 13:
			return lineTerminatorAhead();
		case 14:
			return checkPreviousTokenText("}");
		case 15:
			return checkPreviousTokenText(")");
		}
		return true;
	}
	private boolean signature_sempred(SignatureContext _localctx, int predIndex) {
		switch (predIndex) {
		case 16:
			return noTerminatorAfterParams(1);
		}
		return true;
	}
	private boolean fieldDecl_sempred(FieldDeclContext _localctx, int predIndex) {
		switch (predIndex) {
		case 17:
			return noTerminatorBetween(2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0082\u058f\4\2\t"+
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
		"\t\u008e\4\u008f\t\u008f\3\2\3\2\3\2\7\2\u0122\n\2\f\2\16\2\u0125\13\2"+
		"\3\3\3\3\5\3\u0129\n\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0131\n\4\3\5\3\5\3"+
		"\5\7\5\u0136\n\5\f\5\16\5\u0139\13\5\3\5\5\5\u013c\n\5\3\6\3\6\3\6\7\6"+
		"\u0141\n\6\f\6\16\6\u0144\13\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\5\b\u0150\n\b\5\b\u0152\n\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u0169\n\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\r\7\r\u0171\n\r\f\r\16\r\u0174\13\r\3\16\3\16\3"+
		"\16\3\16\7\16\u017a\n\16\f\16\16\16\u017d\13\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\3\17\5\17\u0186\n\17\3\17\3\17\3\17\3\17\3\20\3\20\5\20\u018e"+
		"\n\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u01a9"+
		"\n\25\3\26\3\26\3\26\3\26\7\26\u01af\n\26\f\26\16\26\u01b2\13\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\7\30\u01bd\n\30\f\30\16\30\u01c0"+
		"\13\30\3\30\3\30\3\30\5\30\u01c5\n\30\3\30\7\30\u01c8\n\30\f\30\16\30"+
		"\u01cb\13\30\5\30\u01cd\n\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5"+
		"\31\u01d7\n\31\3\32\3\32\3\32\3\32\3\32\5\32\u01de\n\32\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\5\33\u01e6\n\33\3\34\3\34\3\34\3\34\5\34\u01ec\n\34\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\7\34\u01f4\n\34\f\34\16\34\u01f7\13\34\3"+
		"\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\7\36\u0205"+
		"\n\36\f\36\16\36\u0208\13\36\3\36\3\36\3\36\3\36\5\36\u020e\n\36\3\36"+
		"\3\36\7\36\u0212\n\36\f\36\16\36\u0215\13\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u0223\n\37\5\37\u0225\n\37\3"+
		" \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3"+
		"#\3#\7#\u023e\n#\f#\16#\u0241\13#\3#\3#\3#\7#\u0246\n#\f#\16#\u0249\13"+
		"#\3#\3#\3$\5$\u024e\n$\3$\3$\3$\3$\5$\u0254\n$\3%\3%\3%\3%\3%\3%\3%\3"+
		"%\5%\u025e\n%\3&\3&\3&\3&\3&\5&\u0265\n&\3\'\3\'\3\'\3\'\5\'\u026b\n\'"+
		"\3\'\3\'\5\'\u026f\n\'\3(\3(\3(\3(\3)\3)\5)\u0277\n)\3)\5)\u027a\n)\3"+
		")\3)\3)\3*\3*\5*\u0281\n*\3*\5*\u0284\n*\3*\3*\3*\3+\5+\u028a\n+\3+\5"+
		"+\u028d\n+\3+\5+\u0290\n+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3,\5,\u029d\n"+
		",\3-\3-\3-\3-\3-\3.\3.\3.\5.\u02a7\n.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3"+
		".\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\7.\u02c7\n"+
		".\f.\16.\u02ca\13.\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\5/"+
		"\u02dc\n/\3\60\3\60\3\60\3\61\3\61\3\61\3\61\7\61\u02e5\n\61\f\61\16\61"+
		"\u02e8\13\61\3\61\3\61\3\61\3\61\5\61\u02ee\n\61\3\62\3\62\3\62\3\62\3"+
		"\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\5\63\u02fc\n\63\3\64\3\64\3\64"+
		"\3\64\3\64\5\64\u0303\n\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\5\64"+
		"\u030d\n\64\7\64\u030f\n\64\f\64\16\64\u0312\13\64\3\65\3\65\3\65\3\65"+
		"\3\65\5\65\u0319\n\65\3\65\3\65\7\65\u031d\n\65\f\65\16\65\u0320\13\65"+
		"\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\5\67\u032a\n\67\3\67\3\67\3\67"+
		"\3\67\3\67\3\67\5\67\u0332\n\67\3\67\3\67\3\67\3\67\5\67\u0338\n\67\3"+
		"8\38\38\38\38\38\38\58\u0341\n8\39\39\39\39\39\39\39\39\39\39\59\u034d"+
		"\n9\3:\3:\5:\u0351\n:\3:\3:\5:\u0355\n:\3:\5:\u0358\n:\3:\3:\3:\3:\3:"+
		"\5:\u035f\n:\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\5>\u036b\n>\3>\5>\u036e\n>"+
		"\3>\3>\3>\3>\3>\5>\u0375\n>\5>\u0377\n>\3?\3?\5?\u037b\n?\3?\5?\u037e"+
		"\n?\3?\5?\u0381\n?\3?\3?\7?\u0385\n?\f?\16?\u0388\13?\3?\3?\3@\3@\5@\u038e"+
		"\n@\3@\5@\u0391\n@\3@\3@\3@\7@\u0396\n@\f@\16@\u0399\13@\3@\3@\3A\3A\3"+
		"A\3A\3A\5A\u03a2\nA\3B\3B\3B\3C\3C\3C\3C\3C\3C\7C\u03ad\nC\fC\16C\u03b0"+
		"\13C\3C\5C\u03b3\nC\3D\5D\u03b6\nD\3D\3D\3E\3E\3F\3F\3F\5F\u03bf\nF\3"+
		"G\3G\3G\3G\3G\3G\7G\u03c7\nG\fG\16G\u03ca\13G\3G\5G\u03cd\nG\3H\3H\5H"+
		"\u03d1\nH\3H\3H\5H\u03d5\nH\3I\3I\3I\7I\u03da\nI\fI\16I\u03dd\13I\3J\3"+
		"J\3J\7J\u03e2\nJ\fJ\16J\u03e5\13J\3K\3K\3K\3K\3K\3K\7K\u03ed\nK\fK\16"+
		"K\u03f0\13K\3K\5K\u03f3\nK\3L\3L\5L\u03f7\nL\3L\3L\3M\3M\3M\3M\3M\3M\7"+
		"M\u0401\nM\fM\16M\u0404\13M\3M\5M\u0407\nM\3N\3N\5N\u040b\nN\3N\3N\3O"+
		"\3O\3O\6O\u0412\nO\rO\16O\u0413\3P\3P\3P\3P\3P\3P\5P\u041c\nP\3Q\3Q\3"+
		"R\3R\3R\3R\3S\3S\3S\3T\3T\3T\3T\3U\5U\u042c\nU\3U\3U\3V\3V\3W\3W\3W\5"+
		"W\u0435\nW\3X\3X\5X\u0439\nX\3Y\3Y\5Y\u043d\nY\3Z\3Z\5Z\u0441\nZ\3[\3"+
		"[\3[\3\\\3\\\3]\3]\3]\3^\3^\5^\u044d\n^\3_\3_\3_\5_\u0452\n_\3`\3`\3`"+
		"\5`\u0457\n`\3a\3a\5a\u045b\na\3a\3a\3a\3a\3a\3a\3b\3b\3b\5b\u0466\nb"+
		"\3c\3c\3c\5c\u046b\nc\3d\3d\5d\u046f\nd\3d\3d\3d\5d\u0474\nd\7d\u0476"+
		"\nd\fd\16d\u0479\13d\3e\3e\3e\7e\u047e\ne\fe\16e\u0481\13e\3e\3e\3f\3"+
		"f\3f\5f\u0488\nf\3g\3g\3g\5g\u048d\ng\3g\5g\u0490\ng\3h\3h\3h\3h\3h\3"+
		"h\5h\u0498\nh\3h\3h\3i\3i\3i\3i\5i\u04a0\ni\3i\3i\3j\5j\u04a5\nj\3j\3"+
		"j\5j\u04a9\nj\3j\3j\5j\u04ad\nj\3k\3k\3k\3k\3k\3k\5k\u04b5\nk\3k\3k\3"+
		"k\3l\3l\3l\3m\3m\5m\u04bf\nm\3n\3n\3n\3n\3n\3n\3n\3n\5n\u04c9\nn\3o\3"+
		"o\3o\3o\3o\3p\3p\3q\3q\3r\3r\3r\3s\3s\3s\3s\3t\3t\3t\3t\3t\3t\3u\3u\3"+
		"u\3u\3u\5u\u04e6\nu\3u\3u\3v\3v\3v\3w\3w\3w\3w\3w\5w\u04f2\nw\3x\3x\5"+
		"x\u04f6\nx\3y\3y\3y\3y\7y\u04fc\ny\fy\16y\u04ff\13y\3y\5y\u0502\ny\5y"+
		"\u0504\ny\3y\3y\3z\3z\3z\3z\5z\u050c\nz\3z\3z\3{\3{\3{\3{\3{\3{\5{\u0516"+
		"\n{\3|\3|\3|\5|\u051b\n|\3}\3}\3~\3~\3~\5~\u0522\n~\3\177\3\177\3\177"+
		"\3\177\3\u0080\3\u0080\3\u0080\3\u0081\3\u0081\3\u0081\5\u0081\u052e\n"+
		"\u0081\5\u0081\u0530\n\u0081\3\u0081\3\u0081\3\u0082\3\u0082\3\u0082\7"+
		"\u0082\u0537\n\u0082\f\u0082\16\u0082\u053a\13\u0082\3\u0083\3\u0083\3"+
		"\u0083\5\u0083\u053f\n\u0083\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\5"+
		"\u0084\u0546\n\u0084\3\u0085\3\u0085\5\u0085\u054a\n\u0085\3\u0086\3\u0086"+
		"\3\u0086\3\u0086\3\u0086\7\u0086\u0551\n\u0086\f\u0086\16\u0086\u0554"+
		"\13\u0086\3\u0086\3\u0086\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\5\u0087"+
		"\u055d\n\u0087\3\u0087\5\u0087\u0560\n\u0087\3\u0088\3\u0088\3\u0089\5"+
		"\u0089\u0565\n\u0089\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3\u008a\3"+
		"\u008b\3\u008b\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c"+
		"\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\5\u008d\u057b\n\u008d\5\u008d"+
		"\u057d\n\u008d\3\u008d\5\u008d\u0580\n\u008d\3\u008d\5\u008d\u0583\n\u008d"+
		"\5\u008d\u0585\n\u008d\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e\3\u008e"+
		"\3\u008f\3\u008f\3\u008f\2\5\66Zf\u0090\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"+
		"\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098"+
		"\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0"+
		"\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8"+
		"\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0"+
		"\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8"+
		"\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110"+
		"\u0112\u0114\u0116\u0118\u011a\u011c\2\21\3\2\23\24\3\2 \"\4\2 \"$$\5"+
		"\2\37\37%%\'(\3\2hn\4\2cglm\5\2VVbbik\3\2\32\34\3\2\27\31\3\2\\a\4\2J"+
		"JUU\3\2VW\4\2bgim\4\2orvw\3\2}~\2\u05e5\2\u011e\3\2\2\2\4\u0126\3\2\2"+
		"\2\6\u0130\3\2\2\2\b\u0132\3\2\2\2\n\u013d\3\2\2\2\f\u0147\3\2\2\2\16"+
		"\u0149\3\2\2\2\20\u0155\3\2\2\2\22\u0158\3\2\2\2\24\u0168\3\2\2\2\26\u016a"+
		"\3\2\2\2\30\u0172\3\2\2\2\32\u0175\3\2\2\2\34\u0180\3\2\2\2\36\u018d\3"+
		"\2\2\2 \u018f\3\2\2\2\"\u0191\3\2\2\2$\u0196\3\2\2\2&\u019b\3\2\2\2(\u01a8"+
		"\3\2\2\2*\u01aa\3\2\2\2,\u01b5\3\2\2\2.\u01cc\3\2\2\2\60\u01d6\3\2\2\2"+
		"\62\u01d8\3\2\2\2\64\u01df\3\2\2\2\66\u01eb\3\2\2\28\u01f8\3\2\2\2:\u01ff"+
		"\3\2\2\2<\u0224\3\2\2\2>\u0226\3\2\2\2@\u022b\3\2\2\2B\u0230\3\2\2\2D"+
		"\u0236\3\2\2\2F\u024d\3\2\2\2H\u025d\3\2\2\2J\u025f\3\2\2\2L\u0266\3\2"+
		"\2\2N\u0270\3\2\2\2P\u0274\3\2\2\2R\u027e\3\2\2\2T\u0289\3\2\2\2V\u029c"+
		"\3\2\2\2X\u029e\3\2\2\2Z\u02a6\3\2\2\2\\\u02db\3\2\2\2^\u02dd\3\2\2\2"+
		"`\u02e6\3\2\2\2b\u02ef\3\2\2\2d\u02fb\3\2\2\2f\u0302\3\2\2\2h\u0313\3"+
		"\2\2\2j\u0323\3\2\2\2l\u0337\3\2\2\2n\u0340\3\2\2\2p\u034c\3\2\2\2r\u034e"+
		"\3\2\2\2t\u0362\3\2\2\2v\u0364\3\2\2\2x\u0366\3\2\2\2z\u0368\3\2\2\2|"+
		"\u0378\3\2\2\2~\u038b\3\2\2\2\u0080\u03a1\3\2\2\2\u0082\u03a3\3\2\2\2"+
		"\u0084\u03a6\3\2\2\2\u0086\u03b5\3\2\2\2\u0088\u03b9\3\2\2\2\u008a\u03be"+
		"\3\2\2\2\u008c\u03c0\3\2\2\2\u008e\u03ce\3\2\2\2\u0090\u03d6\3\2\2\2\u0092"+
		"\u03de\3\2\2\2\u0094\u03e6\3\2\2\2\u0096\u03f4\3\2\2\2\u0098\u03fa\3\2"+
		"\2\2\u009a\u0408\3\2\2\2\u009c\u0411\3\2\2\2\u009e\u041b\3\2\2\2\u00a0"+
		"\u041d\3\2\2\2\u00a2\u041f\3\2\2\2\u00a4\u0423\3\2\2\2\u00a6\u0426\3\2"+
		"\2\2\u00a8\u042b\3\2\2\2\u00aa\u042f\3\2\2\2\u00ac\u0431\3\2\2\2\u00ae"+
		"\u0436\3\2\2\2\u00b0\u043a\3\2\2\2\u00b2\u043e\3\2\2\2\u00b4\u0442\3\2"+
		"\2\2\u00b6\u0445\3\2\2\2\u00b8\u0447\3\2\2\2\u00ba\u044c\3\2\2\2\u00bc"+
		"\u044e\3\2\2\2\u00be\u0456\3\2\2\2\u00c0\u045a\3\2\2\2\u00c2\u0462\3\2"+
		"\2\2\u00c4\u046a\3\2\2\2\u00c6\u046e\3\2\2\2\u00c8\u047a\3\2\2\2\u00ca"+
		"\u0484\3\2\2\2\u00cc\u048f\3\2\2\2\u00ce\u0497\3\2\2\2\u00d0\u049b\3\2"+
		"\2\2\u00d2\u04a4\3\2\2\2\u00d4\u04b4\3\2\2\2\u00d6\u04b9\3\2\2\2\u00d8"+
		"\u04be\3\2\2\2\u00da\u04c8\3\2\2\2\u00dc\u04ca\3\2\2\2\u00de\u04cf\3\2"+
		"\2\2\u00e0\u04d1\3\2\2\2\u00e2\u04d3\3\2\2\2\u00e4\u04d6\3\2\2\2\u00e6"+
		"\u04da\3\2\2\2\u00e8\u04e5\3\2\2\2\u00ea\u04e9\3\2\2\2\u00ec\u04f1\3\2"+
		"\2\2\u00ee\u04f5\3\2\2\2\u00f0\u04f7\3\2\2\2\u00f2\u0507\3\2\2\2\u00f4"+
		"\u0515\3\2\2\2\u00f6\u051a\3\2\2\2\u00f8\u051c\3\2\2\2\u00fa\u051e\3\2"+
		"\2\2\u00fc\u0523\3\2\2\2\u00fe\u0527\3\2\2\2\u0100\u052a\3\2\2\2\u0102"+
		"\u0533\3\2\2\2\u0104\u053e\3\2\2\2\u0106\u0545\3\2\2\2\u0108\u0549\3\2"+
		"\2\2\u010a\u054b\3\2\2\2\u010c\u055c\3\2\2\2\u010e\u0561\3\2\2\2\u0110"+
		"\u0564\3\2\2\2\u0112\u0568\3\2\2\2\u0114\u056c\3\2\2\2\u0116\u0570\3\2"+
		"\2\2\u0118\u0575\3\2\2\2\u011a\u0588\3\2\2\2\u011c\u058c\3\2\2\2\u011e"+
		"\u0123\5\4\3\2\u011f\u0120\7R\2\2\u0120\u0122\5\4\3\2\u0121\u011f\3\2"+
		"\2\2\u0122\u0125\3\2\2\2\u0123\u0121\3\2\2\2\u0123\u0124\3\2\2\2\u0124"+
		"\3\3\2\2\2\u0125\u0123\3\2\2\2\u0126\u0128\7J\2\2\u0127\u0129\7,\2\2\u0128"+
		"\u0127\3\2\2\2\u0128\u0129\3\2\2\2\u0129\5\3\2\2\2\u012a\u012b\7\26\2"+
		"\2\u012b\u0131\5\\/\2\u012c\u012d\7\5\2\2\u012d\u0131\5Z.\2\u012e\u012f"+
		"\t\2\2\2\u012f\u0131\5\f\7\2\u0130\u012a\3\2\2\2\u0130\u012c\3\2\2\2\u0130"+
		"\u012e\3\2\2\2\u0131\7\3\2\2\2\u0132\u0137\5\n\6\2\u0133\u0134\7R\2\2"+
		"\u0134\u0136\5\n\6\2\u0135\u0133\3\2\2\2\u0136\u0139\3\2\2\2\u0137\u0135"+
		"\3\2\2\2\u0137\u0138\3\2\2\2\u0138\u013b\3\2\2\2\u0139\u0137\3\2\2\2\u013a"+
		"\u013c\7R\2\2\u013b\u013a\3\2\2\2\u013b\u013c\3\2\2\2\u013c\t\3\2\2\2"+
		"\u013d\u0142\7J\2\2\u013e\u013f\7R\2\2\u013f\u0141\7J\2\2\u0140\u013e"+
		"\3\2\2\2\u0141\u0144\3\2\2\2\u0142\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143"+
		"\u0145\3\2\2\2\u0144\u0142\3\2\2\2\u0145\u0146\5\u00e0q\2\u0146\13\3\2"+
		"\2\2\u0147\u0148\5f\64\2\u0148\r\3\2\2\2\u0149\u014a\7\22\2\2\u014a\u014b"+
		"\7K\2\2\u014b\u0151\5Z.\2\u014c\u014f\7R\2\2\u014d\u0150\7J\2\2\u014e"+
		"\u0150\5Z.\2\u014f\u014d\3\2\2\2\u014f\u014e\3\2\2\2\u0150\u0152\3\2\2"+
		"\2\u0151\u014c\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0154"+
		"\7L\2\2\u0154\17\3\2\2\2\u0155\u0156\5Z.\2\u0156\u0157\7\2\2\3\u0157\21"+
		"\3\2\2\2\u0158\u0159\5\\/\2\u0159\u015a\7\2\2\3\u015a\23\3\2\2\2\u015b"+
		"\u0169\58\35\2\u015c\u0169\5\16\b\2\u015d\u0169\5\"\22\2\u015e\u0169\5"+
		"$\23\2\u015f\u0169\5\34\17\2\u0160\u0169\5\26\f\2\u0161\u0162\7\20\2\2"+
		"\u0162\u0163\5\b\5\2\u0163\u0164\7T\2\2\u0164\u0165\7T\2\2\u0165\u0166"+
		"\5\30\r\2\u0166\u0167\5Z.\2\u0167\u0169\3\2\2\2\u0168\u015b\3\2\2\2\u0168"+
		"\u015c\3\2\2\2\u0168\u015d\3\2\2\2\u0168\u015e\3\2\2\2\u0168\u015f\3\2"+
		"\2\2\u0168\u0160\3\2\2\2\u0168\u0161\3\2\2\2\u0169\25\3\2\2\2\u016a\u016b"+
		"\t\3\2\2\u016b\u016c\7K\2\2\u016c\u016d\5Z.\2\u016d\u016e\7L\2\2\u016e"+
		"\27\3\2\2\2\u016f\u0171\5\32\16\2\u0170\u016f\3\2\2\2\u0171\u0174\3\2"+
		"\2\2\u0172\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173\31\3\2\2\2\u0174\u0172"+
		"\3\2\2\2\u0175\u0176\7M\2\2\u0176\u017b\5Z.\2\u0177\u0178\7R\2\2\u0178"+
		"\u017a\5Z.\2\u0179\u0177\3\2\2\2\u017a\u017d\3\2\2\2\u017b\u0179\3\2\2"+
		"\2\u017b\u017c\3\2\2\2\u017c\u017e\3\2\2\2\u017d\u017b\3\2\2\2\u017e\u017f"+
		"\7N\2\2\u017f\33\3\2\2\2\u0180\u0185\7\16\2\2\u0181\u0182\7O\2\2\u0182"+
		"\u0183\5\36\20\2\u0183\u0184\7P\2\2\u0184\u0186\3\2\2\2\u0185\u0181\3"+
		"\2\2\2\u0185\u0186\3\2\2\2\u0186\u0187\3\2\2\2\u0187\u0188\7K\2\2\u0188"+
		"\u0189\5Z.\2\u0189\u018a\7L\2\2\u018a\35\3\2\2\2\u018b\u018e\5 \21\2\u018c"+
		"\u018e\7\17\2\2\u018d\u018b\3\2\2\2\u018d\u018c\3\2\2\2\u018e\37\3\2\2"+
		"\2\u018f\u0190\7J\2\2\u0190!\3\2\2\2\u0191\u0192\7*\2\2\u0192\u0193\7"+
		"K\2\2\u0193\u0194\5Z.\2\u0194\u0195\7L\2\2\u0195#\3\2\2\2\u0196\u0197"+
		"\7+\2\2\u0197\u0198\7K\2\2\u0198\u0199\5Z.\2\u0199\u019a\7L\2\2\u019a"+
		"%\3\2\2\2\u019b\u019c\5(\25\2\u019c\'\3\2\2\2\u019d\u019e\t\4\2\2\u019e"+
		"\u019f\7O\2\2\u019f\u01a0\5n8\2\u01a0\u01a1\7P\2\2\u01a1\u01a9\3\2\2\2"+
		"\u01a2\u01a3\7#\2\2\u01a3\u01a4\7O\2\2\u01a4\u01a5\5n8\2\u01a5\u01a6\7"+
		"P\2\2\u01a6\u01a7\5n8\2\u01a7\u01a9\3\2\2\2\u01a8\u019d\3\2\2\2\u01a8"+
		"\u01a2\3\2\2\2\u01a9)\3\2\2\2\u01aa\u01ab\7O\2\2\u01ab\u01b0\5,\27\2\u01ac"+
		"\u01ad\7R\2\2\u01ad\u01af\5,\27\2\u01ae\u01ac\3\2\2\2\u01af\u01b2\3\2"+
		"\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3\3\2\2\2\u01b2"+
		"\u01b0\3\2\2\2\u01b3\u01b4\7P\2\2\u01b4+\3\2\2\2\u01b5\u01b6\5Z.\2\u01b6"+
		"\u01b7\7Q\2\2\u01b7\u01b8\5Z.\2\u01b8-\3\2\2\2\u01b9\u01ba\5\60\31\2\u01ba"+
		"\u01bb\5\u0080A\2\u01bb\u01bd\3\2\2\2\u01bc\u01b9\3\2\2\2\u01bd\u01c0"+
		"\3\2\2\2\u01be\u01bc\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c1\3\2\2\2\u01c0"+
		"\u01be\3\2\2\2\u01c1\u01cd\7\f\2\2\u01c2\u01c5\5\60\31\2\u01c3\u01c5\7"+
		"\f\2\2\u01c4\u01c2\3\2\2\2\u01c4\u01c3\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6"+
		"\u01c8\5\u0080A\2\u01c7\u01c4\3\2\2\2\u01c8\u01cb\3\2\2\2\u01c9\u01c7"+
		"\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cd\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cc"+
		"\u01be\3\2\2\2\u01cc\u01c9\3\2\2\2\u01cd/\3\2\2\2\u01ce\u01cf\7\7\2\2"+
		"\u01cf\u01d7\5\66\34\2\u01d0\u01d1\7\b\2\2\u01d1\u01d7\5\66\34\2\u01d2"+
		"\u01d3\7\t\2\2\u01d3\u01d7\5\66\34\2\u01d4\u01d5\7\13\2\2\u01d5\u01d7"+
		"\5b\62\2\u01d6\u01ce\3\2\2\2\u01d6\u01d0\3\2\2\2\u01d6\u01d2\3\2\2\2\u01d6"+
		"\u01d4\3\2\2\2\u01d7\61\3\2\2\2\u01d8\u01d9\5.\30\2\u01d9\u01da\7\63\2"+
		"\2\u01da\u01db\7J\2\2\u01db\u01dd\5\u00ecw\2\u01dc\u01de\5\u009aN\2\u01dd"+
		"\u01dc\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\63\3\2\2\2\u01df\u01e0\5.\30"+
		"\2\u01e0\u01e1\7\63\2\2\u01e1\u01e2\5P)\2\u01e2\u01e3\7J\2\2\u01e3\u01e5"+
		"\5\u00ecw\2\u01e4\u01e6\5\u009aN\2\u01e5\u01e4\3\2\2\2\u01e5\u01e6\3\2"+
		"\2\2\u01e6\65\3\2\2\2\u01e7\u01ec\b\34\1\2\u01e8\u01ec\5Z.\2\u01e9\u01ea"+
		"\7h\2\2\u01ea\u01ec\5\66\34\5\u01eb\u01e7\3\2\2\2\u01eb\u01e8\3\2\2\2"+
		"\u01eb\u01e9\3\2\2\2\u01ec\u01f5\3\2\2\2\u01ed\u01ee\f\4\2\2\u01ee\u01ef"+
		"\7[\2\2\u01ef\u01f4\5\66\34\5\u01f0\u01f1\f\3\2\2\u01f1\u01f2\7Z\2\2\u01f2"+
		"\u01f4\5\66\34\4\u01f3\u01ed\3\2\2\2\u01f3\u01f0\3\2\2\2\u01f4\u01f7\3"+
		"\2\2\2\u01f5\u01f3\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6\67\3\2\2\2\u01f7"+
		"\u01f5\3\2\2\2\u01f8\u01f9\t\3\2\2\u01f9\u01fa\7O\2\2\u01fa\u01fb\5Z."+
		"\2\u01fb\u01fc\7-\2\2\u01fc\u01fd\5Z.\2\u01fd\u01fe\7P\2\2\u01fe9\3\2"+
		"\2\2\u01ff\u0200\5\u0082B\2\u0200\u0206\5\u0080A\2\u0201\u0202\5\u0084"+
		"C\2\u0202\u0203\5\u0080A\2\u0203\u0205\3\2\2\2\u0204\u0201\3\2\2\2\u0205"+
		"\u0208\3\2\2\2\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0213\3\2"+
		"\2\2\u0208\u0206\3\2\2\2\u0209\u020e\5\62\32\2\u020a\u020e\5\64\33\2\u020b"+
		"\u020e\5\u008aF\2\u020c\u020e\5<\37\2\u020d\u0209\3\2\2\2\u020d\u020a"+
		"\3\2\2\2\u020d\u020b\3\2\2\2\u020d\u020c\3\2\2\2\u020e\u020f\3\2\2\2\u020f"+
		"\u0210\5\u0080A\2\u0210\u0212\3\2\2\2\u0211\u020d\3\2\2\2\u0212\u0215"+
		"\3\2\2\2\u0213\u0211\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0216\3\2\2\2\u0215"+
		"\u0213\3\2\2\2\u0216\u0217\7\2\2\3\u0217;\3\2\2\2\u0218\u0225\5> \2\u0219"+
		"\u0225\5B\"\2\u021a\u0225\5D#\2\u021b\u021c\7\26\2\2\u021c\u0222\5\u0080"+
		"A\2\u021d\u0223\5\64\33\2\u021e\u0223\5\62\32\2\u021f\u0223\5\u008cG\2"+
		"\u0220\u0223\5\u0094K\2\u0221\u0223\5\u0098M\2\u0222\u021d\3\2\2\2\u0222"+
		"\u021e\3\2\2\2\u0222\u021f\3\2\2\2\u0222\u0220\3\2\2\2\u0222\u0221\3\2"+
		"\2\2\u0223\u0225\3\2\2\2\u0224\u0218\3\2\2\2\u0224\u0219\3\2\2\2\u0224"+
		"\u021a\3\2\2\2\u0224\u021b\3\2\2\2\u0225=\3\2\2\2\u0226\u0227\7)\2\2\u0227"+
		"\u0228\7J\2\2\u0228\u0229\5\u00f0y\2\u0229\u022a\5@!\2\u022a?\3\2\2\2"+
		"\u022b\u022c\7M\2\2\u022c\u022d\5Z.\2\u022d\u022e\5\u0080A\2\u022e\u022f"+
		"\7N\2\2\u022fA\3\2\2\2\u0230\u0231\7)\2\2\u0231\u0232\5P)\2\u0232\u0233"+
		"\7J\2\2\u0233\u0234\5\u00f0y\2\u0234\u0235\5@!\2\u0235C\3\2\2\2\u0236"+
		"\u0237\5n8\2\u0237\u0238\7\r\2\2\u0238\u0239\5n8\2\u0239\u023f\7M\2\2"+
		"\u023a\u023b\5J&\2\u023b\u023c\5\u0080A\2\u023c\u023e\3\2\2\2\u023d\u023a"+
		"\3\2\2\2\u023e\u0241\3\2\2\2\u023f\u023d\3\2\2\2\u023f\u0240\3\2\2\2\u0240"+
		"\u0247\3\2\2\2\u0241\u023f\3\2\2\2\u0242\u0243\5F$\2\u0243\u0244\5\u0080"+
		"A\2\u0244\u0246\3\2\2\2\u0245\u0242\3\2\2\2\u0246\u0249\3\2\2\2\u0247"+
		"\u0245\3\2\2\2\u0247\u0248\3\2\2\2\u0248\u024a\3\2\2\2\u0249\u0247\3\2"+
		"\2\2\u024a\u024b\7N\2\2\u024bE\3\2\2\2\u024c\u024e\7\f\2\2\u024d\u024c"+
		"\3\2\2\2\u024d\u024e\3\2\2\2\u024e\u024f\3\2\2\2\u024f\u0250\5R*\2\u0250"+
		"\u0251\7J\2\2\u0251\u0253\5\u00ecw\2\u0252\u0254\5\u009aN\2\u0253\u0252"+
		"\3\2\2\2\u0253\u0254\3\2\2\2\u0254G\3\2\2\2\u0255\u0256\5f\64\2\u0256"+
		"\u0257\7U\2\2\u0257\u0258\7J\2\2\u0258\u025e\3\2\2\2\u0259\u025a\5n8\2"+
		"\u025a\u025b\7U\2\2\u025b\u025c\7J\2\2\u025c\u025e\3\2\2\2\u025d\u0255"+
		"\3\2\2\2\u025d\u0259\3\2\2\2\u025eI\3\2\2\2\u025f\u0260\7)\2\2\u0260\u0261"+
		"\7J\2\2\u0261\u0264\7X\2\2\u0262\u0265\5H%\2\u0263\u0265\5\u00fa~\2\u0264"+
		"\u0262\3\2\2\2\u0264\u0263\3\2\2\2\u0265K\3\2\2\2\u0266\u026e\5\2\2\2"+
		"\u0267\u026a\5n8\2\u0268\u0269\7Q\2\2\u0269\u026b\5\u0092J\2\u026a\u0268"+
		"\3\2\2\2\u026a\u026b\3\2\2\2\u026b\u026f\3\2\2\2\u026c\u026d\7Q\2\2\u026d"+
		"\u026f\5\u0092J\2\u026e\u0267\3\2\2\2\u026e\u026c\3\2\2\2\u026fM\3\2\2"+
		"\2\u0270\u0271\5\2\2\2\u0271\u0272\7X\2\2\u0272\u0273\5\u0092J\2\u0273"+
		"O\3\2\2\2\u0274\u0276\7K\2\2\u0275\u0277\5\4\3\2\u0276\u0275\3\2\2\2\u0276"+
		"\u0277\3\2\2\2\u0277\u0279\3\2\2\2\u0278\u027a\7l\2\2\u0279\u0278\3\2"+
		"\2\2\u0279\u027a\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027c\7J\2\2\u027c"+
		"\u027d\7L\2\2\u027dQ\3\2\2\2\u027e\u0280\7K\2\2\u027f\u0281\7J\2\2\u0280"+
		"\u027f\3\2\2\2\u0280\u0281\3\2\2\2\u0281\u0283\3\2\2\2\u0282\u0284\7l"+
		"\2\2\u0283\u0282\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0285\3\2\2\2\u0285"+
		"\u0286\5\u00d8m\2\u0286\u0287\7L\2\2\u0287S\3\2\2\2\u0288\u028a\7\26\2"+
		"\2\u0289\u0288\3\2\2\2\u0289\u028a\3\2\2\2\u028a\u028c\3\2\2\2\u028b\u028d"+
		"\5\u0090I\2\u028c\u028b\3\2\2\2\u028c\u028d\3\2\2\2\u028d\u028f\3\2\2"+
		"\2\u028e\u0290\7Y\2\2\u028f\u028e\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0291"+
		"\3\2\2\2\u0291\u0292\5n8\2\u0292U\3\2\2\2\u0293\u029d\5f\64\2\u0294\u0295"+
		"\t\5\2\2\u0295\u0296\7K\2\2\u0296\u0297\5Z.\2\u0297\u0298\7L\2\2\u0298"+
		"\u029d\3\2\2\2\u0299\u029d\5X-\2\u029a\u029b\t\6\2\2\u029b\u029d\5Z.\2"+
		"\u029c\u0293\3\2\2\2\u029c\u0294\3\2\2\2\u029c\u0299\3\2\2\2\u029c\u029a"+
		"\3\2\2\2\u029dW\3\2\2\2\u029e\u029f\7\25\2\2\u029f\u02a0\5\f\7\2\u02a0"+
		"\u02a1\7\27\2\2\u02a1\u02a2\5Z.\2\u02a2Y\3\2\2\2\u02a3\u02a4\b.\1\2\u02a4"+
		"\u02a7\5f\64\2\u02a5\u02a7\5V,\2\u02a6\u02a3\3\2\2\2\u02a6\u02a5\3\2\2"+
		"\2\u02a7\u02c8\3\2\2\2\u02a8\u02a9\f\13\2\2\u02a9\u02aa\t\7\2\2\u02aa"+
		"\u02c7\5Z.\f\u02ab\u02ac\f\n\2\2\u02ac\u02ad\t\b\2\2\u02ad\u02c7\5Z.\13"+
		"\u02ae\u02af\f\t\2\2\u02af\u02b0\t\t\2\2\u02b0\u02c7\5Z.\n\u02b1\u02b2"+
		"\f\b\2\2\u02b2\u02b3\t\n\2\2\u02b3\u02c7\5Z.\t\u02b4\u02b5\f\7\2\2\u02b5"+
		"\u02b6\t\13\2\2\u02b6\u02c7\5Z.\b\u02b7\u02b8\f\6\2\2\u02b8\u02b9\7[\2"+
		"\2\u02b9\u02c7\5Z.\7\u02ba\u02bb\f\5\2\2\u02bb\u02bc\7Z\2\2\u02bc\u02c7"+
		"\5Z.\6\u02bd\u02be\f\4\2\2\u02be\u02bf\7\35\2\2\u02bf\u02c7\5Z.\4\u02c0"+
		"\u02c1\f\3\2\2\u02c1\u02c2\7\36\2\2\u02c2\u02c3\5Z.\2\u02c3\u02c4\7T\2"+
		"\2\u02c4\u02c5\5Z.\3\u02c5\u02c7\3\2\2\2\u02c6\u02a8\3\2\2\2\u02c6\u02ab"+
		"\3\2\2\2\u02c6\u02ae\3\2\2\2\u02c6\u02b1\3\2\2\2\u02c6\u02b4\3\2\2\2\u02c6"+
		"\u02b7\3\2\2\2\u02c6\u02ba\3\2\2\2\u02c6\u02bd\3\2\2\2\u02c6\u02c0\3\2"+
		"\2\2\u02c7\u02ca\3\2\2\2\u02c8\u02c6\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9"+
		"[\3\2\2\2\u02ca\u02c8\3\2\2\2\u02cb\u02dc\5\6\4\2\u02cc\u02dc\5\u008a"+
		"F\2\u02cd\u02dc\5\u00acW\2\u02ce\u02dc\5\u009eP\2\u02cf\u02dc\5\u00d6"+
		"l\2\u02d0\u02dc\5\u00aeX\2\u02d1\u02dc\5\u00b0Y\2\u02d2\u02dc\5\u00b2"+
		"Z\2\u02d3\u02dc\5\u00b4[\2\u02d4\u02dc\5\u00b6\\\2\u02d5\u02dc\5\u009a"+
		"N\2\u02d6\u02dc\5z>\2\u02d7\u02dc\5\u00ba^\2\u02d8\u02dc\5\u00c8e\2\u02d9"+
		"\u02dc\5^\60\2\u02da\u02dc\5\u00b8]\2\u02db\u02cb\3\2\2\2\u02db\u02cc"+
		"\3\2\2\2\u02db\u02cd\3\2\2\2\u02db\u02ce\3\2\2\2\u02db\u02cf\3\2\2\2\u02db"+
		"\u02d0\3\2\2\2\u02db\u02d1\3\2\2\2\u02db\u02d2\3\2\2\2\u02db\u02d3\3\2"+
		"\2\2\u02db\u02d4\3\2\2\2\u02db\u02d5\3\2\2\2\u02db\u02d6\3\2\2\2\u02db"+
		"\u02d7\3\2\2\2\u02db\u02d8\3\2\2\2\u02db\u02d9\3\2\2\2\u02db\u02da\3\2"+
		"\2\2\u02dc]\3\2\2\2\u02dd\u02de\5`\61\2\u02de\u02df\5\u00d0i\2\u02df_"+
		"\3\2\2\2\u02e0\u02e1\7\n\2\2\u02e1\u02e2\5Z.\2\u02e2\u02e3\5\u0080A\2"+
		"\u02e3\u02e5\3\2\2\2\u02e4\u02e0\3\2\2\2\u02e5\u02e8\3\2\2\2\u02e6\u02e4"+
		"\3\2\2\2\u02e6\u02e7\3\2\2\2\u02e7\u02ed\3\2\2\2\u02e8\u02e6\3\2\2\2\u02e9"+
		"\u02ea\7\13\2\2\u02ea\u02eb\5b\62\2\u02eb\u02ec\5\u0080A\2\u02ec\u02ee"+
		"\3\2\2\2\u02ed\u02e9\3\2\2\2\u02ed\u02ee\3\2\2\2\u02eea\3\2\2\2\u02ef"+
		"\u02f0\5\u0092J\2\u02f0\u02f1\7B\2\2\u02f1\u02f2\5Z.\2\u02f2c\3\2\2\2"+
		"\u02f3\u02fc\7\3\2\2\u02f4\u02fc\7\4\2\2\u02f5\u02fc\7I\2\2\u02f6\u02fc"+
		"\5\u00f8}\2\u02f7\u02fc\5\u010e\u0088\2\u02f8\u02fc\7s\2\2\u02f9\u02fc"+
		"\7v\2\2\u02fa\u02fc\7w\2\2\u02fb\u02f3\3\2\2\2\u02fb\u02f4\3\2\2\2\u02fb"+
		"\u02f5\3\2\2\2\u02fb\u02f6\3\2\2\2\u02fb\u02f7\3\2\2\2\u02fb\u02f8\3\2"+
		"\2\2\u02fb\u02f9\3\2\2\2\u02fb\u02fa\3\2\2\2\u02fce\3\2\2\2\u02fd\u02fe"+
		"\b\64\1\2\u02fe\u0303\5\u00f4{\2\u02ff\u0303\5\u00f2z\2\u0300\u0303\5"+
		"\u011a\u008e\2\u0301\u0303\5\24\13\2\u0302\u02fd\3\2\2\2\u0302\u02ff\3"+
		"\2\2\2\u0302\u0300\3\2\2\2\u0302\u0301\3\2\2\2\u0303\u0310\3\2\2\2\u0304"+
		"\u030c\f\3\2\2\u0305\u0306\7U\2\2\u0306\u030d\7J\2\2\u0307\u030d\5\u0114"+
		"\u008b\2\u0308\u030d\5r:\2\u0309\u030d\5*\26\2\u030a\u030d\5\u0116\u008c"+
		"\2\u030b\u030d\5\u0118\u008d\2\u030c\u0305\3\2\2\2\u030c\u0307\3\2\2\2"+
		"\u030c\u0308\3\2\2\2\u030c\u0309\3\2\2\2\u030c\u030a\3\2\2\2\u030c\u030b"+
		"\3\2\2\2\u030d\u030f\3\2\2\2\u030e\u0304\3\2\2\2\u030f\u0312\3\2\2\2\u0310"+
		"\u030e\3\2\2\2\u0310\u0311\3\2\2\2\u0311g\3\2\2\2\u0312\u0310\3\2\2\2"+
		"\u0313\u0314\7\64\2\2\u0314\u031e\7M\2\2\u0315\u0319\5l\67\2\u0316\u0319"+
		"\5\u00d8m\2\u0317\u0319\5j\66\2\u0318\u0315\3\2\2\2\u0318\u0316\3\2\2"+
		"\2\u0318\u0317\3\2\2\2\u0319\u031a\3\2\2\2\u031a\u031b\5\u0080A\2\u031b"+
		"\u031d\3\2\2\2\u031c\u0318\3\2\2\2\u031d\u0320\3\2\2\2\u031e\u031c\3\2"+
		"\2\2\u031e\u031f\3\2\2\2\u031f\u0321\3\2\2\2\u0320\u031e\3\2\2\2\u0321"+
		"\u0322\7N\2\2\u0322i\3\2\2\2\u0323\u0324\7)\2\2\u0324\u0325\7J\2\2\u0325"+
		"\u0326\5\u00f0y\2\u0326k\3\2\2\2\u0327\u0329\6\67\16\2\u0328\u032a\7\26"+
		"\2\2\u0329\u0328\3\2\2\2\u0329\u032a\3\2\2\2\u032a\u032b\3\2\2\2\u032b"+
		"\u032c\5.\30\2\u032c\u032d\7J\2\2\u032d\u032e\5\u00f0y\2\u032e\u032f\5"+
		"\u00eex\2\u032f\u0338\3\2\2\2\u0330\u0332\7\26\2\2\u0331\u0330\3\2\2\2"+
		"\u0331\u0332\3\2\2\2\u0332\u0333\3\2\2\2\u0333\u0334\5.\30\2\u0334\u0335"+
		"\7J\2\2\u0335\u0336\5\u00f0y\2\u0336\u0338\3\2\2\2\u0337\u0327\3\2\2\2"+
		"\u0337\u0331\3\2\2\2\u0338m\3\2\2\2\u0339\u0341\5\u00d8m\2\u033a\u0341"+
		"\5\u00dan\2\u033b\u0341\5&\24\2\u033c\u033d\7K\2\2\u033d\u033e\5n8\2\u033e"+
		"\u033f\7L\2\2\u033f\u0341\3\2\2\2\u0340\u0339\3\2\2\2\u0340\u033a\3\2"+
		"\2\2\u0340\u033b\3\2\2\2\u0340\u033c\3\2\2\2\u0341o\3\2\2\2\u0342\u034d"+
		"\5\u010a\u0086\2\u0343\u034d\5\u00dco\2\u0344\u0345\7O\2\2\u0345\u0346"+
		"\7Y\2\2\u0346\u0347\7P\2\2\u0347\u034d\5\u00e0q\2\u0348\u034d\5\u00e4"+
		"s\2\u0349\u034d\5\u00e6t\2\u034a\u034d\5&\24\2\u034b\u034d\5\u00d8m\2"+
		"\u034c\u0342\3\2\2\2\u034c\u0343\3\2\2\2\u034c\u0344\3\2\2\2\u034c\u0348"+
		"\3\2\2\2\u034c\u0349\3\2\2\2\u034c\u034a\3\2\2\2\u034c\u034b\3\2\2\2\u034d"+
		"q\3\2\2\2\u034e\u035e\7O\2\2\u034f\u0351\5t;\2\u0350\u034f\3\2\2\2\u0350"+
		"\u0351\3\2\2\2\u0351\u0352\3\2\2\2\u0352\u0354\7T\2\2\u0353\u0355\5v<"+
		"\2\u0354\u0353\3\2\2\2\u0354\u0355\3\2\2\2\u0355\u035f\3\2\2\2\u0356\u0358"+
		"\5t;\2\u0357\u0356\3\2\2\2\u0357\u0358\3\2\2\2\u0358\u0359\3\2\2\2\u0359"+
		"\u035a\7T\2\2\u035a\u035b\5v<\2\u035b\u035c\7T\2\2\u035c\u035d\5x=\2\u035d"+
		"\u035f\3\2\2\2\u035e\u0350\3\2\2\2\u035e\u0357\3\2\2\2\u035f\u0360\3\2"+
		"\2\2\u0360\u0361\7P\2\2\u0361s\3\2\2\2\u0362\u0363\5Z.\2\u0363u\3\2\2"+
		"\2\u0364\u0365\5Z.\2\u0365w\3\2\2\2\u0366\u0367\5Z.\2\u0367y\3\2\2\2\u0368"+
		"\u036d\7B\2\2\u0369\u036b\5\u009eP\2\u036a\u0369\3\2\2\2\u036a\u036b\3"+
		"\2\2\2\u036b\u036c\3\2\2\2\u036c\u036e\7S\2\2\u036d\u036a\3\2\2\2\u036d"+
		"\u036e\3\2\2\2\u036e\u036f\3\2\2\2\u036f\u0370\5Z.\2\u0370\u0376\5\u009a"+
		"N\2\u0371\u0374\7<\2\2\u0372\u0375\5z>\2\u0373\u0375\5\u009aN\2\u0374"+
		"\u0372\3\2\2\2\u0374\u0373\3\2\2\2\u0375\u0377\3\2\2\2\u0376\u0371\3\2"+
		"\2\2\u0376\u0377\3\2\2\2\u0377{\3\2\2\2\u0378\u037d\7?\2\2\u0379\u037b"+
		"\5\u009eP\2\u037a\u0379\3\2\2\2\u037a\u037b\3\2\2\2\u037b\u037c\3\2\2"+
		"\2\u037c\u037e\7S\2\2\u037d\u037a\3\2\2\2\u037d\u037e\3\2\2\2\u037e\u0380"+
		"\3\2\2\2\u037f\u0381\5Z.\2\u0380\u037f\3\2\2\2\u0380\u0381\3\2\2\2\u0381"+
		"\u0382\3\2\2\2\u0382\u0386\7M\2\2\u0383\u0385\5\u00bc_\2\u0384\u0383\3"+
		"\2\2\2\u0385\u0388\3\2\2\2\u0386\u0384\3\2\2\2\u0386\u0387\3\2\2\2\u0387"+
		"\u0389\3\2\2\2\u0388\u0386\3\2\2\2\u0389\u038a\7N\2\2\u038a}\3\2\2\2\u038b"+
		"\u0390\7?\2\2\u038c\u038e\5\u009eP\2\u038d\u038c\3\2\2\2\u038d\u038e\3"+
		"\2\2\2\u038e\u038f\3\2\2\2\u038f\u0391\7S\2\2\u0390\u038d\3\2\2\2\u0390"+
		"\u0391\3\2\2\2\u0391\u0392\3\2\2\2\u0392\u0393\5\u00c0a\2\u0393\u0397"+
		"\7M\2\2\u0394\u0396\5\u00c2b\2\u0395\u0394\3\2\2\2\u0396\u0399\3\2\2\2"+
		"\u0397\u0395\3\2\2\2\u0397\u0398\3\2\2\2\u0398\u039a\3\2\2\2\u0399\u0397"+
		"\3\2\2\2\u039a\u039b\7N\2\2\u039b\177\3\2\2\2\u039c\u03a2\7S\2\2\u039d"+
		"\u03a2\7\2\2\3\u039e\u03a2\6A\17\2\u039f\u03a2\6A\20\2\u03a0\u03a2\6A"+
		"\21\2\u03a1\u039c\3\2\2\2\u03a1\u039d\3\2\2\2\u03a1\u039e\3\2\2\2\u03a1"+
		"\u039f\3\2\2\2\u03a1\u03a0\3\2\2\2\u03a2\u0081\3\2\2\2\u03a3\u03a4\7>"+
		"\2\2\u03a4\u03a5\7J\2\2\u03a5\u0083\3\2\2\2\u03a6\u03b2\7F\2\2\u03a7\u03b3"+
		"\5\u0086D\2\u03a8\u03ae\7K\2\2\u03a9\u03aa\5\u0086D\2\u03aa\u03ab\5\u0080"+
		"A\2\u03ab\u03ad\3\2\2\2\u03ac\u03a9\3\2\2\2\u03ad\u03b0\3\2\2\2\u03ae"+
		"\u03ac\3\2\2\2\u03ae\u03af\3\2\2\2\u03af\u03b1\3\2\2\2\u03b0\u03ae\3\2"+
		"\2\2\u03b1\u03b3\7L\2\2\u03b2\u03a7\3\2\2\2\u03b2\u03a8\3\2\2\2\u03b3"+
		"\u0085\3\2\2\2\u03b4\u03b6\t\f\2\2\u03b5\u03b4\3\2\2\2\u03b5\u03b6\3\2"+
		"\2\2\u03b6\u03b7\3\2\2\2\u03b7\u03b8\5\u0088E\2\u03b8\u0087\3\2\2\2\u03b9"+
		"\u03ba\5\u010e\u0088\2\u03ba\u0089\3\2\2\2\u03bb\u03bf\5\u008cG\2\u03bc"+
		"\u03bf\5\u0094K\2\u03bd\u03bf\5\u0098M\2\u03be\u03bb\3\2\2\2\u03be\u03bc"+
		"\3\2\2\2\u03be\u03bd\3\2\2\2\u03bf\u008b\3\2\2\2\u03c0\u03cc\7@\2\2\u03c1"+
		"\u03cd\5\u008eH\2\u03c2\u03c8\7K\2\2\u03c3\u03c4\5\u008eH\2\u03c4\u03c5"+
		"\5\u0080A\2\u03c5\u03c7\3\2\2\2\u03c6\u03c3\3\2\2\2\u03c7\u03ca\3\2\2"+
		"\2\u03c8\u03c6\3\2\2\2\u03c8\u03c9\3\2\2\2\u03c9\u03cb\3\2\2\2\u03ca\u03c8"+
		"\3\2\2\2\u03cb\u03cd\7L\2\2\u03cc\u03c1\3\2\2\2\u03cc\u03c2\3\2\2\2\u03cd"+
		"\u008d\3\2\2\2\u03ce\u03d4\5\u0090I\2\u03cf\u03d1\5n8\2\u03d0\u03cf\3"+
		"\2\2\2\u03d0\u03d1\3\2\2\2\u03d1\u03d2\3\2\2\2\u03d2\u03d3\7Q\2\2\u03d3"+
		"\u03d5\5\u0092J\2\u03d4\u03d0\3\2\2\2\u03d4\u03d5\3\2\2\2\u03d5\u008f"+
		"\3\2\2\2\u03d6\u03db\7J\2\2\u03d7\u03d8\7R\2\2\u03d8\u03da\7J\2\2\u03d9"+
		"\u03d7\3\2\2\2\u03da\u03dd\3\2\2\2\u03db\u03d9\3\2\2\2\u03db\u03dc\3\2"+
		"\2\2\u03dc\u0091\3\2\2\2\u03dd\u03db\3\2\2\2\u03de\u03e3\5Z.\2\u03df\u03e0"+
		"\7R\2\2\u03e0\u03e2\5Z.\2\u03e1\u03df\3\2\2\2\u03e2\u03e5\3\2\2\2\u03e3"+
		"\u03e1\3\2\2\2\u03e3\u03e4\3\2\2\2\u03e4\u0093\3\2\2\2\u03e5\u03e3\3\2"+
		"\2\2\u03e6\u03f2\7C\2\2\u03e7\u03f3\5\u0096L\2\u03e8\u03ee\7K\2\2\u03e9"+
		"\u03ea\5\u0096L\2\u03ea\u03eb\5\u0080A\2\u03eb\u03ed\3\2\2\2\u03ec\u03e9"+
		"\3\2\2\2\u03ed\u03f0\3\2\2\2\u03ee\u03ec\3\2\2\2\u03ee\u03ef\3\2\2\2\u03ef"+
		"\u03f1\3\2\2\2\u03f0\u03ee\3\2\2\2\u03f1\u03f3\7L\2\2\u03f2\u03e7\3\2"+
		"\2\2\u03f2\u03e8\3\2\2\2\u03f3\u0095\3\2\2\2\u03f4\u03f6\7J\2\2\u03f5"+
		"\u03f7\7Q\2\2\u03f6\u03f5\3\2\2\2\u03f6\u03f7\3\2\2\2\u03f7\u03f8\3\2"+
		"\2\2\u03f8\u03f9\5n8\2\u03f9\u0097\3\2\2\2\u03fa\u0406\7H\2\2\u03fb\u0407"+
		"\5L\'\2\u03fc\u0402\7K\2\2\u03fd\u03fe\5L\'\2\u03fe\u03ff\5\u0080A\2\u03ff"+
		"\u0401\3\2\2\2\u0400\u03fd\3\2\2\2\u0401\u0404\3\2\2\2\u0402\u0400\3\2"+
		"\2\2\u0402\u0403\3\2\2\2\u0403\u0405\3\2\2\2\u0404\u0402\3\2\2\2\u0405"+
		"\u0407\7L\2\2\u0406\u03fb\3\2\2\2\u0406\u03fc\3\2\2\2\u0407\u0099\3\2"+
		"\2\2\u0408\u040a\7M\2\2\u0409\u040b\5\u009cO\2\u040a\u0409\3\2\2\2\u040a"+
		"\u040b\3\2\2\2\u040b\u040c\3\2\2\2\u040c\u040d\7N\2\2\u040d\u009b\3\2"+
		"\2\2\u040e\u040f\5\\/\2\u040f\u0410\5\u0080A\2\u0410\u0412\3\2\2\2\u0411"+
		"\u040e\3\2\2\2\u0412\u0413\3\2\2\2\u0413\u0411\3\2\2\2\u0413\u0414\3\2"+
		"\2\2\u0414\u009d\3\2\2\2\u0415\u041c\5\u00a2R\2\u0416\u041c\5\u00a4S\2"+
		"\u0417\u041c\5\u00a6T\2\u0418\u041c\5\u00a0Q\2\u0419\u041c\5N(\2\u041a"+
		"\u041c\5\u00aaV\2\u041b\u0415\3\2\2\2\u041b\u0416\3\2\2\2\u041b\u0417"+
		"\3\2\2\2\u041b\u0418\3\2\2\2\u041b\u0419\3\2\2\2\u041b\u041a\3\2\2\2\u041c"+
		"\u009f\3\2\2\2\u041d\u041e\5Z.\2\u041e\u00a1\3\2\2\2\u041f\u0420\5Z.\2"+
		"\u0420\u0421\7n\2\2\u0421\u0422\5Z.\2\u0422\u00a3\3\2\2\2\u0423\u0424"+
		"\5Z.\2\u0424\u0425\t\r\2\2\u0425\u00a5\3\2\2\2\u0426\u0427\5\u0092J\2"+
		"\u0427\u0428\5\u00a8U\2\u0428\u0429\5\u0092J\2\u0429\u00a7\3\2\2\2\u042a"+
		"\u042c\t\16\2\2\u042b\u042a\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u042d\3"+
		"\2\2\2\u042d\u042e\7Q\2\2\u042e\u00a9\3\2\2\2\u042f\u0430\7S\2\2\u0430"+
		"\u00ab\3\2\2\2\u0431\u0432\7J\2\2\u0432\u0434\7T\2\2\u0433\u0435\5\\/"+
		"\2\u0434\u0433\3\2\2\2\u0434\u0435\3\2\2\2\u0435\u00ad\3\2\2\2\u0436\u0438"+
		"\7G\2\2\u0437\u0439\5\u0092J\2\u0438\u0437\3\2\2\2\u0438\u0439\3\2\2\2"+
		"\u0439\u00af\3\2\2\2\u043a\u043c\7\61\2\2\u043b\u043d\7J\2\2\u043c\u043b"+
		"\3\2\2\2\u043c\u043d\3\2\2\2\u043d\u00b1\3\2\2\2\u043e\u0440\7D\2\2\u043f"+
		"\u0441\7J\2\2\u0440\u043f\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u00b3\3\2"+
		"\2\2\u0442\u0443\7=\2\2\u0443\u0444\7J\2\2\u0444\u00b5\3\2\2\2\u0445\u0446"+
		"\7A\2\2\u0446\u00b7\3\2\2\2\u0447\u0448\7\67\2\2\u0448\u0449\5Z.\2\u0449"+
		"\u00b9\3\2\2\2\u044a\u044d\5|?\2\u044b\u044d\5~@\2\u044c\u044a\3\2\2\2"+
		"\u044c\u044b\3\2\2\2\u044d\u00bb\3\2\2\2\u044e\u044f\5\u00be`\2\u044f"+
		"\u0451\7T\2\2\u0450\u0452\5\u009cO\2\u0451\u0450\3\2\2\2\u0451\u0452\3"+
		"\2\2\2\u0452\u00bd\3\2\2\2\u0453\u0454\7\66\2\2\u0454\u0457\5\u0092J\2"+
		"\u0455\u0457\7\62\2\2\u0456\u0453\3\2\2\2\u0456\u0455\3\2\2\2\u0457\u00bf"+
		"\3\2\2\2\u0458\u0459\7J\2\2\u0459\u045b\7X\2\2\u045a\u0458\3\2\2\2\u045a"+
		"\u045b\3\2\2\2\u045b\u045c\3\2\2\2\u045c\u045d\5f\64\2\u045d\u045e\7U"+
		"\2\2\u045e\u045f\7K\2\2\u045f\u0460\7C\2\2\u0460\u0461\7L\2\2\u0461\u00c1"+
		"\3\2\2\2\u0462\u0463\5\u00c4c\2\u0463\u0465\7T\2\2\u0464\u0466\5\u009c"+
		"O\2\u0465\u0464\3\2\2\2\u0465\u0466\3\2\2\2\u0466\u00c3\3\2\2\2\u0467"+
		"\u0468\7\66\2\2\u0468\u046b\5\u00c6d\2\u0469\u046b\7\62\2\2\u046a\u0467"+
		"\3\2\2\2\u046a\u0469\3\2\2\2\u046b\u00c5\3\2\2\2\u046c\u046f\5n8\2\u046d"+
		"\u046f\7I\2\2\u046e\u046c\3\2\2\2\u046e\u046d\3\2\2\2\u046f\u0477\3\2"+
		"\2\2\u0470\u0473\7R\2\2\u0471\u0474\5n8\2\u0472\u0474\7I\2\2\u0473\u0471"+
		"\3\2\2\2\u0473\u0472\3\2\2\2\u0474\u0476\3\2\2\2\u0475\u0470\3\2\2\2\u0476"+
		"\u0479\3\2\2\2\u0477\u0475\3\2\2\2\u0477\u0478\3\2\2\2\u0478\u00c7\3\2"+
		"\2\2\u0479\u0477\3\2\2\2\u047a\u047b\7\65\2\2\u047b\u047f\7M\2\2\u047c"+
		"\u047e\5\u00caf\2\u047d\u047c\3\2\2\2\u047e\u0481\3\2\2\2\u047f\u047d"+
		"\3\2\2\2\u047f\u0480\3\2\2\2\u0480\u0482\3\2\2\2\u0481\u047f\3\2\2\2\u0482"+
		"\u0483\7N\2\2\u0483\u00c9\3\2\2\2\u0484\u0485\5\u00ccg\2\u0485\u0487\7"+
		"T\2\2\u0486\u0488\5\u009cO\2\u0487\u0486\3\2\2\2\u0487\u0488\3\2\2\2\u0488"+
		"\u00cb\3\2\2\2\u0489\u048c\7\66\2\2\u048a\u048d\5\u00a2R\2\u048b\u048d"+
		"\5\u00ceh\2\u048c\u048a\3\2\2\2\u048c\u048b\3\2\2\2\u048d\u0490\3\2\2"+
		"\2\u048e\u0490\7\62\2\2\u048f\u0489\3\2\2\2\u048f\u048e\3\2\2\2\u0490"+
		"\u00cd\3\2\2\2\u0491\u0492\5\u0092J\2\u0492\u0493\7Q\2\2\u0493\u0498\3"+
		"\2\2\2\u0494\u0495\5\u0090I\2\u0495\u0496\7X\2\2\u0496\u0498\3\2\2\2\u0497"+
		"\u0491\3\2\2\2\u0497\u0494\3\2\2\2\u0497\u0498\3\2\2\2\u0498\u0499\3\2"+
		"\2\2\u0499\u049a\5Z.\2\u049a\u00cf\3\2\2\2\u049b\u049f\7E\2\2\u049c\u04a0"+
		"\5Z.\2\u049d\u04a0\5\u00d2j\2\u049e\u04a0\5\u00d4k\2\u049f\u049c\3\2\2"+
		"\2\u049f\u049d\3\2\2\2\u049f\u049e\3\2\2\2\u049f\u04a0\3\2\2\2\u04a0\u04a1"+
		"\3\2\2\2\u04a1\u04a2\5\u009aN\2\u04a2\u00d1\3\2\2\2\u04a3\u04a5\5\u009e"+
		"P\2\u04a4\u04a3\3\2\2\2\u04a4\u04a5\3\2\2\2\u04a5\u04a6\3\2\2\2\u04a6"+
		"\u04a8\7S\2\2\u04a7\u04a9\5Z.\2\u04a8\u04a7\3\2\2\2\u04a8\u04a9\3\2\2"+
		"\2\u04a9\u04aa\3\2\2\2\u04aa\u04ac\7S\2\2\u04ab\u04ad\5\u009eP\2\u04ac"+
		"\u04ab\3\2\2\2\u04ac\u04ad\3\2\2\2\u04ad\u00d3\3\2\2\2\u04ae\u04af\5\u0092"+
		"J\2\u04af\u04b0\7Q\2\2\u04b0\u04b5\3\2\2\2\u04b1\u04b2\5\u0090I\2\u04b2"+
		"\u04b3\7X\2\2\u04b3\u04b5\3\2\2\2\u04b4\u04ae\3\2\2\2\u04b4\u04b1\3\2"+
		"\2\2\u04b4\u04b5\3\2\2\2\u04b5\u04b6\3\2\2\2\u04b6\u04b7\7\37\2\2\u04b7"+
		"\u04b8\5Z.\2\u04b8\u00d5\3\2\2\2\u04b9\u04ba\78\2\2\u04ba\u04bb\5Z.\2"+
		"\u04bb\u00d7\3\2\2\2\u04bc\u04bf\5\u00fc\177\2\u04bd\u04bf\7J\2\2\u04be"+
		"\u04bc\3\2\2\2\u04be\u04bd\3\2\2\2\u04bf\u00d9\3\2\2\2\u04c0\u04c9\5\u00dc"+
		"o\2\u04c1\u04c9\5\u010a\u0086\2\u04c2\u04c9\5\u00e2r\2\u04c3\u04c9\5\u00ea"+
		"v\2\u04c4\u04c9\5h\65\2\u04c5\u04c9\5\u00e4s\2\u04c6\u04c9\5\u00e6t\2"+
		"\u04c7\u04c9\5\u00e8u\2\u04c8\u04c0\3\2\2\2\u04c8\u04c1\3\2\2\2\u04c8"+
		"\u04c2\3\2\2\2\u04c8\u04c3\3\2\2\2\u04c8\u04c4\3\2\2\2\u04c8\u04c5\3\2"+
		"\2\2\u04c8\u04c6\3\2\2\2\u04c8\u04c7\3\2\2\2\u04c9\u00db\3\2\2\2\u04ca"+
		"\u04cb\7O\2\2\u04cb\u04cc\5\u00dep\2\u04cc\u04cd\7P\2\2\u04cd\u04ce\5"+
		"\u00e0q\2\u04ce\u00dd\3\2\2\2\u04cf\u04d0\5Z.\2\u04d0\u00df\3\2\2\2\u04d1"+
		"\u04d2\5n8\2\u04d2\u00e1\3\2\2\2\u04d3\u04d4\7l\2\2\u04d4\u04d5\5n8\2"+
		"\u04d5\u00e3\3\2\2\2\u04d6\u04d7\7O\2\2\u04d7\u04d8\7P\2\2\u04d8\u04d9"+
		"\5\u00e0q\2\u04d9\u00e5\3\2\2\2\u04da\u04db\79\2\2\u04db\u04dc\7O\2\2"+
		"\u04dc\u04dd\5n8\2\u04dd\u04de\7P\2\2\u04de\u04df\5\u00e0q\2\u04df\u00e7"+
		"\3\2\2\2\u04e0\u04e6\7;\2\2\u04e1\u04e2\7;\2\2\u04e2\u04e6\7n\2\2\u04e3"+
		"\u04e4\7n\2\2\u04e4\u04e6\7;\2\2\u04e5\u04e0\3\2\2\2\u04e5\u04e1\3\2\2"+
		"\2\u04e5\u04e3\3\2\2\2\u04e6\u04e7\3\2\2\2\u04e7\u04e8\5\u00e0q\2\u04e8"+
		"\u00e9\3\2\2\2\u04e9\u04ea\7\63\2\2\u04ea\u04eb\5\u00ecw\2\u04eb\u00eb"+
		"\3\2\2\2\u04ec\u04ed\6w\22\2\u04ed\u04ee\5\u00f0y\2\u04ee\u04ef\5\u00ee"+
		"x\2\u04ef\u04f2\3\2\2\2\u04f0\u04f2\5\u00f0y\2\u04f1\u04ec\3\2\2\2\u04f1"+
		"\u04f0\3\2\2\2\u04f2\u00ed\3\2\2\2\u04f3\u04f6\5\u00f0y\2\u04f4\u04f6"+
		"\5n8\2\u04f5\u04f3\3\2\2\2\u04f5\u04f4\3\2\2\2\u04f6\u00ef\3\2\2\2\u04f7"+
		"\u0503\7K\2\2\u04f8\u04fd\5T+\2\u04f9\u04fa\7R\2\2\u04fa\u04fc\5T+\2\u04fb"+
		"\u04f9\3\2\2\2\u04fc\u04ff\3\2\2\2\u04fd\u04fb\3\2\2\2\u04fd\u04fe\3\2"+
		"\2\2\u04fe\u0501\3\2\2\2\u04ff\u04fd\3\2\2\2\u0500\u0502\7R\2\2\u0501"+
		"\u0500\3\2\2\2\u0501\u0502\3\2\2\2\u0502\u0504\3\2\2\2\u0503\u04f8\3\2"+
		"\2\2\u0503\u0504\3\2\2\2\u0504\u0505\3\2\2\2\u0505\u0506\7L\2\2\u0506"+
		"\u00f1\3\2\2\2\u0507\u0508\5n8\2\u0508\u0509\7K\2\2\u0509\u050b\5Z.\2"+
		"\u050a\u050c\7R\2\2\u050b\u050a\3\2\2\2\u050b\u050c\3\2\2\2\u050c\u050d"+
		"\3\2\2\2\u050d\u050e\7L\2\2\u050e\u00f3\3\2\2\2\u050f\u0516\5\u00f6|\2"+
		"\u0510\u0516\5\u00fa~\2\u0511\u0512\7K\2\2\u0512\u0513\5Z.\2\u0513\u0514"+
		"\7L\2\2\u0514\u0516\3\2\2\2\u0515\u050f\3\2\2\2\u0515\u0510\3\2\2\2\u0515"+
		"\u0511\3\2\2\2\u0516\u00f5\3\2\2\2\u0517\u051b\5d\63\2\u0518\u051b\5\u00fe"+
		"\u0080\2\u0519\u051b\5\u0112\u008a\2\u051a\u0517\3\2\2\2\u051a\u0518\3"+
		"\2\2\2\u051a\u0519\3\2\2\2\u051b\u00f7\3\2\2\2\u051c\u051d\t\17\2\2\u051d"+
		"\u00f9\3\2\2\2\u051e\u0521\7J\2\2\u051f\u0520\7U\2\2\u0520\u0522\7J\2"+
		"\2\u0521\u051f\3\2\2\2\u0521\u0522\3\2\2\2\u0522\u00fb\3\2\2\2\u0523\u0524"+
		"\7J\2\2\u0524\u0525\7U\2\2\u0525\u0526\7J\2\2\u0526\u00fd\3\2\2\2\u0527"+
		"\u0528\5p9\2\u0528\u0529\5\u0100\u0081\2\u0529\u00ff\3\2\2\2\u052a\u052f"+
		"\7M\2\2\u052b\u052d\5\u0102\u0082\2\u052c\u052e\7R\2\2\u052d\u052c\3\2"+
		"\2\2\u052d\u052e\3\2\2\2\u052e\u0530\3\2\2\2\u052f\u052b\3\2\2\2\u052f"+
		"\u0530\3\2\2\2\u0530\u0531\3\2\2\2\u0531\u0532\7N\2\2\u0532\u0101\3\2"+
		"\2\2\u0533\u0538\5\u0104\u0083\2\u0534\u0535\7R\2\2\u0535\u0537\5\u0104"+
		"\u0083\2\u0536\u0534\3\2\2\2\u0537\u053a\3\2\2\2\u0538\u0536\3\2\2\2\u0538"+
		"\u0539\3\2\2\2\u0539\u0103\3\2\2\2\u053a\u0538\3\2\2\2\u053b\u053c\5\u0106"+
		"\u0084\2\u053c\u053d\7T\2\2\u053d\u053f\3\2\2\2\u053e\u053b\3\2\2\2\u053e"+
		"\u053f\3\2\2\2\u053f\u0540\3\2\2\2\u0540\u0541\5\u0108\u0085\2\u0541\u0105"+
		"\3\2\2\2\u0542\u0546\7J\2\2\u0543\u0546\5Z.\2\u0544\u0546\5\u0100\u0081"+
		"\2\u0545\u0542\3\2\2\2\u0545\u0543\3\2\2\2\u0545\u0544\3\2\2\2\u0546\u0107"+
		"\3\2\2\2\u0547\u054a\5Z.\2\u0548\u054a\5\u0100\u0081\2\u0549\u0547\3\2"+
		"\2\2\u0549\u0548\3\2\2\2\u054a\u0109\3\2\2\2\u054b\u054c\7:\2\2\u054c"+
		"\u0552\7M\2\2\u054d\u054e\5\u010c\u0087\2\u054e\u054f\5\u0080A\2\u054f"+
		"\u0551\3\2\2\2\u0550\u054d\3\2\2\2\u0551\u0554\3\2\2\2\u0552\u0550\3\2"+
		"\2\2\u0552\u0553\3\2\2\2\u0553\u0555\3\2\2\2\u0554\u0552\3\2\2\2\u0555"+
		"\u0556\7N\2\2\u0556\u010b\3\2\2\2\u0557\u0558\6\u0087\23\2\u0558\u0559"+
		"\5\u0090I\2\u0559\u055a\5n8\2\u055a\u055d\3\2\2\2\u055b\u055d\5\u0110"+
		"\u0089\2\u055c\u0557\3\2\2\2\u055c\u055b\3\2\2\2\u055d\u055f\3\2\2\2\u055e"+
		"\u0560\5\u010e\u0088\2\u055f\u055e\3\2\2\2\u055f\u0560\3\2\2\2\u0560\u010d"+
		"\3\2\2\2\u0561\u0562\t\20\2\2\u0562\u010f\3\2\2\2\u0563\u0565\7l\2\2\u0564"+
		"\u0563\3\2\2\2\u0564\u0565\3\2\2\2\u0565\u0566\3\2\2\2\u0566\u0567\5\u00d8"+
		"m\2\u0567\u0111\3\2\2\2\u0568\u0569\7\63\2\2\u0569\u056a\5\u00ecw\2\u056a"+
		"\u056b\5\u009aN\2\u056b\u0113\3\2\2\2\u056c\u056d\7O\2\2\u056d\u056e\5"+
		"Z.\2\u056e\u056f\7P\2\2\u056f\u0115\3\2\2\2\u0570\u0571\7U\2\2\u0571\u0572"+
		"\7K\2\2\u0572\u0573\5n8\2\u0573\u0574\7L\2\2\u0574\u0117\3\2\2\2\u0575"+
		"\u0584\7K\2\2\u0576\u057d\5\u0092J\2\u0577\u057a\5n8\2\u0578\u0579\7R"+
		"\2\2\u0579\u057b\5\u0092J\2\u057a\u0578\3\2\2\2\u057a\u057b\3\2\2\2\u057b"+
		"\u057d\3\2\2\2\u057c\u0576\3\2\2\2\u057c\u0577\3\2\2\2\u057d\u057f\3\2"+
		"\2\2\u057e\u0580\7Y\2\2\u057f\u057e\3\2\2\2\u057f\u0580\3\2\2\2\u0580"+
		"\u0582\3\2\2\2\u0581\u0583\7R\2\2\u0582\u0581\3\2\2\2\u0582\u0583\3\2"+
		"\2\2\u0583\u0585\3\2\2\2\u0584\u057c\3\2\2\2\u0584\u0585\3\2\2\2\u0585"+
		"\u0586\3\2\2\2\u0586\u0587\7L\2\2\u0587\u0119\3\2\2\2\u0588\u0589\5\u011c"+
		"\u008f\2\u0589\u058a\7U\2\2\u058a\u058b\7J\2\2\u058b\u011b\3\2\2\2\u058c"+
		"\u058d\5n8\2\u058d\u011d\3\2\2\2\u0096\u0123\u0128\u0130\u0137\u013b\u0142"+
		"\u014f\u0151\u0168\u0172\u017b\u0185\u018d\u01a8\u01b0\u01be\u01c4\u01c9"+
		"\u01cc\u01d6\u01dd\u01e5\u01eb\u01f3\u01f5\u0206\u020d\u0213\u0222\u0224"+
		"\u023f\u0247\u024d\u0253\u025d\u0264\u026a\u026e\u0276\u0279\u0280\u0283"+
		"\u0289\u028c\u028f\u029c\u02a6\u02c6\u02c8\u02db\u02e6\u02ed\u02fb\u0302"+
		"\u030c\u0310\u0318\u031e\u0329\u0331\u0337\u0340\u034c\u0350\u0354\u0357"+
		"\u035e\u036a\u036d\u0374\u0376\u037a\u037d\u0380\u0386\u038d\u0390\u0397"+
		"\u03a1\u03ae\u03b2\u03b5\u03be\u03c8\u03cc\u03d0\u03d4\u03db\u03e3\u03ee"+
		"\u03f2\u03f6\u0402\u0406\u040a\u0413\u041b\u042b\u0434\u0438\u043c\u0440"+
		"\u044c\u0451\u0456\u045a\u0465\u046a\u046e\u0473\u0477\u047f\u0487\u048c"+
		"\u048f\u0497\u049f\u04a4\u04a8\u04ac\u04b4\u04be\u04c8\u04e5\u04f1\u04f5"+
		"\u04fd\u0501\u0503\u050b\u0515\u051a\u0521\u052d\u052f\u0538\u053e\u0545"+
		"\u0549\u0552\u055c\u055f\u0564\u057a\u057c\u057f\u0582\u0584";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}