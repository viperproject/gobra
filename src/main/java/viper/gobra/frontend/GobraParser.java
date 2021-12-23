// Generated from /home/nico/Documents/repositories/projects/eth/BA/gobraHome/gobra/src/main/antlr4/GobraParser.g4 by ANTLR 4.9.2
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
		TRUE=1, FALSE=2, ASSERT=3, ASSUME=4, PRE=5, PRESERVES=6, POST=7, INV=8, 
		DEC=9, PURE=10, IMPL=11, OLD=12, LHS=13, FORALL=14, EXISTS=15, ACCESS=16, 
		FOLD=17, UNFOLD=18, UNFOLDING=19, GHOST=20, IN=21, MULTI=22, SUBSET=23, 
		UNION=24, INTERSECTION=25, SETMINUS=26, IMPLIES=27, QMARK=28, L_PRED=29, 
		R_PRED=30, RANGE=31, SEQ=32, SET=33, MSET=34, DICT=35, OPT=36, LEN=37, 
		NEW=38, MAKE=39, CAP=40, SOME=41, GET=42, DOM=43, NONE=44, PRED=45, TYPE_OF=46, 
		IS_COMPARABLE=47, SHARE=48, ADDR_MOD=49, DOT_DOT=50, SHARED=51, EXCLUSIVE=52, 
		PREDICATE=53, BREAK=54, DEFAULT=55, FUNC=56, INTERFACE=57, SELECT=58, 
		CASE=59, DEFER=60, GO=61, MAP=62, STRUCT=63, CHAN=64, ELSE=65, GOTO=66, 
		PACKAGE=67, SWITCH=68, CONST=69, FALLTHROUGH=70, IF=71, TYPE=72, CONTINUE=73, 
		FOR=74, IMPORT=75, RETURN=76, VAR=77, NIL_LIT=78, IDENTIFIER=79, L_PAREN=80, 
		R_PAREN=81, L_CURLY=82, R_CURLY=83, L_BRACKET=84, R_BRACKET=85, ASSIGN=86, 
		COMMA=87, SEMI=88, COLON=89, DOT=90, PLUS_PLUS=91, MINUS_MINUS=92, DECLARE_ASSIGN=93, 
		ELLIPSIS=94, LOGICAL_OR=95, LOGICAL_AND=96, EQUALS=97, NOT_EQUALS=98, 
		LESS=99, LESS_OR_EQUALS=100, GREATER=101, GREATER_OR_EQUALS=102, OR=103, 
		DIV=104, MOD=105, LSHIFT=106, RSHIFT=107, BIT_CLEAR=108, EXCLAMATION=109, 
		PLUS=110, MINUS=111, CARET=112, STAR=113, AMPERSAND=114, RECEIVE=115, 
		DECIMAL_LIT=116, BINARY_LIT=117, OCTAL_LIT=118, HEX_LIT=119, FLOAT_LIT=120, 
		DECIMAL_FLOAT_LIT=121, HEX_FLOAT_LIT=122, IMAGINARY_LIT=123, RUNE_LIT=124, 
		BYTE_VALUE=125, OCTAL_BYTE_VALUE=126, HEX_BYTE_VALUE=127, LITTLE_U_VALUE=128, 
		BIG_U_VALUE=129, RAW_STRING_LIT=130, INTERPRETED_STRING_LIT=131, WS=132, 
		COMMENT=133, TERMINATOR=134, LINE_COMMENT=135;
	public static final int
		RULE_maybeAddressableIdentifierList = 0, RULE_maybeAddressableIdentifier = 1, 
		RULE_ghostStatement = 2, RULE_boundVariables = 3, RULE_boundVariableDecl = 4, 
		RULE_predicateAccess = 5, RULE_access = 6, RULE_exprOnly = 7, RULE_stmtOnly = 8, 
		RULE_ghostPrimaryExpr = 9, RULE_optionSome = 10, RULE_optionNone = 11, 
		RULE_optionGet = 12, RULE_sConversion = 13, RULE_triggers = 14, RULE_trigger = 15, 
		RULE_old = 16, RULE_oldLabelUse = 17, RULE_labelUse = 18, RULE_typeOf = 19, 
		RULE_isComparable = 20, RULE_ghostTypeLit = 21, RULE_ghostSliceType = 22, 
		RULE_sqType = 23, RULE_seqUpdExp = 24, RULE_seqUpdClause = 25, RULE_specification = 26, 
		RULE_specStatement = 27, RULE_functionDecl = 28, RULE_methodDecl = 29, 
		RULE_blockWithBodyParameterInfo = 30, RULE_assertion = 31, RULE_range = 32, 
		RULE_sourceFile = 33, RULE_ghostMember = 34, RULE_fpredicateDecl = 35, 
		RULE_predicateBody = 36, RULE_mpredicateDecl = 37, RULE_implementationProof = 38, 
		RULE_methodImplementationProof = 39, RULE_selection = 40, RULE_implementationProofPredicateAlias = 41, 
		RULE_varSpec = 42, RULE_shortVarDecl = 43, RULE_receiver = 44, RULE_nonLocalReceiver = 45, 
		RULE_parameterDecl = 46, RULE_unaryExpr = 47, RULE_unfolding = 48, RULE_expression = 49, 
		RULE_make = 50, RULE_new_ = 51, RULE_statement = 52, RULE_specForStmt = 53, 
		RULE_loopSpec = 54, RULE_terminationMeasure = 55, RULE_basicLit = 56, 
		RULE_primaryExpr = 57, RULE_predConstructArgs = 58, RULE_interfaceType = 59, 
		RULE_predicateSpec = 60, RULE_methodSpec = 61, RULE_type_ = 62, RULE_typeLit = 63, 
		RULE_predType = 64, RULE_predTypeParams = 65, RULE_literalType = 66, RULE_slice_ = 67, 
		RULE_low = 68, RULE_high = 69, RULE_cap = 70, RULE_ifStmt = 71, RULE_assign_op = 72, 
		RULE_exprSwitchStmt = 73, RULE_typeSwitchStmt = 74, RULE_eos = 75, RULE_packageClause = 76, 
		RULE_importDecl = 77, RULE_importSpec = 78, RULE_importPath = 79, RULE_declaration = 80, 
		RULE_constDecl = 81, RULE_constSpec = 82, RULE_identifierList = 83, RULE_expressionList = 84, 
		RULE_typeDecl = 85, RULE_typeSpec = 86, RULE_varDecl = 87, RULE_block = 88, 
		RULE_statementList = 89, RULE_simpleStmt = 90, RULE_expressionStmt = 91, 
		RULE_sendStmt = 92, RULE_incDecStmt = 93, RULE_assignment = 94, RULE_emptyStmt = 95, 
		RULE_labeledStmt = 96, RULE_returnStmt = 97, RULE_breakStmt = 98, RULE_continueStmt = 99, 
		RULE_gotoStmt = 100, RULE_fallthroughStmt = 101, RULE_deferStmt = 102, 
		RULE_switchStmt = 103, RULE_exprCaseClause = 104, RULE_exprSwitchCase = 105, 
		RULE_typeSwitchGuard = 106, RULE_typeCaseClause = 107, RULE_typeSwitchCase = 108, 
		RULE_typeList = 109, RULE_selectStmt = 110, RULE_commClause = 111, RULE_commCase = 112, 
		RULE_recvStmt = 113, RULE_forStmt = 114, RULE_forClause = 115, RULE_rangeClause = 116, 
		RULE_goStmt = 117, RULE_typeName = 118, RULE_arrayType = 119, RULE_arrayLength = 120, 
		RULE_elementType = 121, RULE_pointerType = 122, RULE_sliceType = 123, 
		RULE_mapType = 124, RULE_channelType = 125, RULE_functionType = 126, RULE_signature = 127, 
		RULE_result = 128, RULE_parameters = 129, RULE_conversion = 130, RULE_operand = 131, 
		RULE_literal = 132, RULE_integer = 133, RULE_operandName = 134, RULE_qualifiedIdent = 135, 
		RULE_compositeLit = 136, RULE_literalValue = 137, RULE_elementList = 138, 
		RULE_keyedElement = 139, RULE_key = 140, RULE_element = 141, RULE_structType = 142, 
		RULE_fieldDecl = 143, RULE_string_ = 144, RULE_embeddedField = 145, RULE_functionLit = 146, 
		RULE_index = 147, RULE_typeAssertion = 148, RULE_arguments = 149, RULE_methodExpr = 150, 
		RULE_receiverType = 151;
	private static String[] makeRuleNames() {
		return new String[] {
			"maybeAddressableIdentifierList", "maybeAddressableIdentifier", "ghostStatement", 
			"boundVariables", "boundVariableDecl", "predicateAccess", "access", "exprOnly", 
			"stmtOnly", "ghostPrimaryExpr", "optionSome", "optionNone", "optionGet", 
			"sConversion", "triggers", "trigger", "old", "oldLabelUse", "labelUse", 
			"typeOf", "isComparable", "ghostTypeLit", "ghostSliceType", "sqType", 
			"seqUpdExp", "seqUpdClause", "specification", "specStatement", "functionDecl", 
			"methodDecl", "blockWithBodyParameterInfo", "assertion", "range", "sourceFile", 
			"ghostMember", "fpredicateDecl", "predicateBody", "mpredicateDecl", "implementationProof", 
			"methodImplementationProof", "selection", "implementationProofPredicateAlias", 
			"varSpec", "shortVarDecl", "receiver", "nonLocalReceiver", "parameterDecl", 
			"unaryExpr", "unfolding", "expression", "make", "new_", "statement", 
			"specForStmt", "loopSpec", "terminationMeasure", "basicLit", "primaryExpr", 
			"predConstructArgs", "interfaceType", "predicateSpec", "methodSpec", 
			"type_", "typeLit", "predType", "predTypeParams", "literalType", "slice_", 
			"low", "high", "cap", "ifStmt", "assign_op", "exprSwitchStmt", "typeSwitchStmt", 
			"eos", "packageClause", "importDecl", "importSpec", "importPath", "declaration", 
			"constDecl", "constSpec", "identifierList", "expressionList", "typeDecl", 
			"typeSpec", "varDecl", "block", "statementList", "simpleStmt", "expressionStmt", 
			"sendStmt", "incDecStmt", "assignment", "emptyStmt", "labeledStmt", "returnStmt", 
			"breakStmt", "continueStmt", "gotoStmt", "fallthroughStmt", "deferStmt", 
			"switchStmt", "exprCaseClause", "exprSwitchCase", "typeSwitchGuard", 
			"typeCaseClause", "typeSwitchCase", "typeList", "selectStmt", "commClause", 
			"commCase", "recvStmt", "forStmt", "forClause", "rangeClause", "goStmt", 
			"typeName", "arrayType", "arrayLength", "elementType", "pointerType", 
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
			"'setminus'", "'==>'", "'?'", "'!<'", "'!>'", "'range'", "'seq'", "'set'", 
			"'mset'", "'dict'", "'option'", "'len'", "'new'", "'make'", "'cap'", 
			"'some'", "'get'", "'domain'", "'none'", "'pred'", "'typeOf'", "'isComparable'", 
			"'share'", "'@'", "'..'", "'shared'", "'exclusive'", "'predicate'", "'break'", 
			"'default'", "'func'", "'interface'", "'select'", "'case'", "'defer'", 
			"'go'", "'map'", "'struct'", "'chan'", "'else'", "'goto'", "'package'", 
			"'switch'", "'const'", "'fallthrough'", "'if'", "'type'", "'continue'", 
			"'for'", "'import'", "'return'", "'var'", "'nil'", null, "'('", "')'", 
			"'{'", "'}'", "'['", "']'", "'='", "','", "';'", "':'", "'.'", "'++'", 
			"'--'", "':='", "'...'", "'||'", "'&&'", "'=='", "'!='", "'<'", "'<='", 
			"'>'", "'>='", "'|'", "'/'", "'%'", "'<<'", "'>>'", "'&^'", "'!'", "'+'", 
			"'-'", "'^'", "'*'", "'&'", "'<-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TRUE", "FALSE", "ASSERT", "ASSUME", "PRE", "PRESERVES", "POST", 
			"INV", "DEC", "PURE", "IMPL", "OLD", "LHS", "FORALL", "EXISTS", "ACCESS", 
			"FOLD", "UNFOLD", "UNFOLDING", "GHOST", "IN", "MULTI", "SUBSET", "UNION", 
			"INTERSECTION", "SETMINUS", "IMPLIES", "QMARK", "L_PRED", "R_PRED", "RANGE", 
			"SEQ", "SET", "MSET", "DICT", "OPT", "LEN", "NEW", "MAKE", "CAP", "SOME", 
			"GET", "DOM", "NONE", "PRED", "TYPE_OF", "IS_COMPARABLE", "SHARE", "ADDR_MOD", 
			"DOT_DOT", "SHARED", "EXCLUSIVE", "PREDICATE", "BREAK", "DEFAULT", "FUNC", 
			"INTERFACE", "SELECT", "CASE", "DEFER", "GO", "MAP", "STRUCT", "CHAN", 
			"ELSE", "GOTO", "PACKAGE", "SWITCH", "CONST", "FALLTHROUGH", "IF", "TYPE", 
			"CONTINUE", "FOR", "IMPORT", "RETURN", "VAR", "NIL_LIT", "IDENTIFIER", 
			"L_PAREN", "R_PAREN", "L_CURLY", "R_CURLY", "L_BRACKET", "R_BRACKET", 
			"ASSIGN", "COMMA", "SEMI", "COLON", "DOT", "PLUS_PLUS", "MINUS_MINUS", 
			"DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", "LOGICAL_AND", "EQUALS", 
			"NOT_EQUALS", "LESS", "LESS_OR_EQUALS", "GREATER", "GREATER_OR_EQUALS", 
			"OR", "DIV", "MOD", "LSHIFT", "RSHIFT", "BIT_CLEAR", "EXCLAMATION", "PLUS", 
			"MINUS", "CARET", "STAR", "AMPERSAND", "RECEIVE", "DECIMAL_LIT", "BINARY_LIT", 
			"OCTAL_LIT", "HEX_LIT", "FLOAT_LIT", "DECIMAL_FLOAT_LIT", "HEX_FLOAT_LIT", 
			"IMAGINARY_LIT", "RUNE_LIT", "BYTE_VALUE", "OCTAL_BYTE_VALUE", "HEX_BYTE_VALUE", 
			"LITTLE_U_VALUE", "BIG_U_VALUE", "RAW_STRING_LIT", "INTERPRETED_STRING_LIT", 
			"WS", "COMMENT", "TERMINATOR", "LINE_COMMENT"
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
			setState(304);
			maybeAddressableIdentifier();
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(305);
				match(COMMA);
				setState(306);
				maybeAddressableIdentifier();
				}
				}
				setState(311);
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
			setState(312);
			match(IDENTIFIER);
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(313);
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
			setState(322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				enterOuterAlt(_localctx, 1);
				{
				setState(316);
				match(GHOST);
				setState(317);
				statement();
				}
				break;
			case ASSERT:
				enterOuterAlt(_localctx, 2);
				{
				setState(318);
				match(ASSERT);
				setState(319);
				expression(0);
				}
				break;
			case FOLD:
			case UNFOLD:
				enterOuterAlt(_localctx, 3);
				{
				setState(320);
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
				setState(321);
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
			setState(324);
			boundVariableDecl();
			setState(329);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(325);
					match(COMMA);
					setState(326);
					boundVariableDecl();
					}
					} 
				}
				setState(331);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(332);
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
			setState(335);
			match(IDENTIFIER);
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(336);
				match(COMMA);
				setState(337);
				match(IDENTIFIER);
				}
				}
				setState(342);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(343);
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
			setState(345);
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
			setState(347);
			match(ACCESS);
			setState(348);
			match(L_PAREN);
			setState(349);
			expression(0);
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(350);
				match(COMMA);
				setState(353);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(351);
					match(IDENTIFIER);
					}
					break;
				case 2:
					{
					setState(352);
					expression(0);
					}
					break;
				}
				}
			}

			setState(357);
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
			setState(359);
			expression(0);
			setState(360);
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
			setState(362);
			statement();
			setState(363);
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
		public OptionNoneContext optionNone() {
			return getRuleContext(OptionNoneContext.class,0);
		}
		public OptionSomeContext optionSome() {
			return getRuleContext(OptionSomeContext.class,0);
		}
		public OptionGetContext optionGet() {
			return getRuleContext(OptionGetContext.class,0);
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
			setState(381);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(365);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(366);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(367);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(368);
				isComparable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(369);
				old();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(370);
				sConversion();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(371);
				optionNone();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(372);
				optionSome();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(373);
				optionGet();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(374);
				match(FORALL);
				setState(375);
				boundVariables();
				setState(376);
				match(COLON);
				setState(377);
				match(COLON);
				setState(378);
				triggers();
				setState(379);
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
		enterRule(_localctx, 20, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			match(SOME);
			setState(384);
			match(L_PAREN);
			setState(385);
			expression(0);
			setState(386);
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
		enterRule(_localctx, 22, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			match(NONE);
			setState(389);
			match(L_BRACKET);
			setState(390);
			type_();
			setState(391);
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
		enterRule(_localctx, 24, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(GET);
			setState(394);
			match(L_PAREN);
			setState(395);
			expression(0);
			setState(396);
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
		enterRule(_localctx, 26, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
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
			setState(399);
			match(L_PAREN);
			setState(400);
			expression(0);
			setState(401);
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
		enterRule(_localctx, 28, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(403);
				trigger();
				}
				}
				setState(408);
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
		enterRule(_localctx, 30, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			match(L_CURLY);
			setState(410);
			expression(0);
			setState(415);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(411);
				match(COMMA);
				setState(412);
				expression(0);
				}
				}
				setState(417);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(418);
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
		enterRule(_localctx, 32, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			match(OLD);
			setState(425);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(421);
				match(L_BRACKET);
				setState(422);
				oldLabelUse();
				setState(423);
				match(R_BRACKET);
				}
			}

			setState(427);
			match(L_PAREN);
			setState(428);
			expression(0);
			setState(429);
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
		enterRule(_localctx, 34, RULE_oldLabelUse);
		try {
			setState(433);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(431);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(432);
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
		enterRule(_localctx, 36, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
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
		enterRule(_localctx, 38, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(437);
			match(TYPE_OF);
			setState(438);
			match(L_PAREN);
			setState(439);
			expression(0);
			setState(440);
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
		enterRule(_localctx, 40, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			match(IS_COMPARABLE);
			setState(443);
			match(L_PAREN);
			setState(444);
			expression(0);
			setState(445);
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
		public GhostSliceTypeContext ghostSliceType() {
			return getRuleContext(GhostSliceTypeContext.class,0);
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
		enterRule(_localctx, 42, RULE_ghostTypeLit);
		try {
			setState(449);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(447);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(448);
				ghostSliceType();
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
		enterRule(_localctx, 44, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			match(GHOST);
			setState(452);
			match(L_BRACKET);
			setState(453);
			match(R_BRACKET);
			setState(454);
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
		enterRule(_localctx, 46, RULE_sqType);
		int _la;
		try {
			setState(467);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(456);
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
				setState(457);
				match(L_BRACKET);
				setState(458);
				type_();
				setState(459);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(461);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(462);
				match(L_BRACKET);
				setState(463);
				type_();
				setState(464);
				match(R_BRACKET);
				setState(465);
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
		enterRule(_localctx, 48, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			match(L_BRACKET);
			{
			setState(470);
			seqUpdClause();
			setState(475);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(471);
				match(COMMA);
				setState(472);
				seqUpdClause();
				}
				}
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(478);
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
		enterRule(_localctx, 50, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			expression(0);
			setState(481);
			match(ASSIGN);
			setState(482);
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
		enterRule(_localctx, 52, RULE_specification);
		int _la;
		try {
			setState(503);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(489);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC))) != 0)) {
					{
					{
					{
					setState(484);
					specStatement();
					}
					setState(485);
					eos();
					}
					}
					setState(491);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(492);
				match(PURE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(500);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE))) != 0)) {
					{
					{
					setState(495);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(493);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(494);
						match(PURE);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(497);
					eos();
					}
					}
					setState(502);
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
		enterRule(_localctx, 54, RULE_specStatement);
		try {
			setState(513);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(505);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(506);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(507);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(508);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(509);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(510);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(511);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(512);
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
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
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
		enterRule(_localctx, 56, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			specification();
			setState(516);
			match(FUNC);
			setState(517);
			match(IDENTIFIER);
			{
			setState(518);
			signature();
			setState(520);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(519);
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
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
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
		enterRule(_localctx, 58, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			specification();
			setState(523);
			match(FUNC);
			setState(524);
			receiver();
			setState(525);
			match(IDENTIFIER);
			{
			setState(526);
			signature();
			setState(528);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(527);
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
		enterRule(_localctx, 60, RULE_blockWithBodyParameterInfo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(530);
			match(L_CURLY);
			setState(535);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SHARE) {
				{
				setState(531);
				match(SHARE);
				setState(532);
				identifierList();
				setState(533);
				eos();
				}
			}

			setState(538);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
				{
				setState(537);
				statementList();
				}
			}

			setState(540);
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
		enterRule(_localctx, 62, RULE_assertion);
		try {
			setState(544);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(543);
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
			setState(546);
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
			setState(547);
			match(L_BRACKET);
			setState(548);
			expression(0);
			setState(549);
			match(DOT_DOT);
			setState(550);
			expression(0);
			setState(551);
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
		enterRule(_localctx, 66, RULE_sourceFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			packageClause();
			setState(554);
			eos();
			setState(560);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(555);
				importDecl();
				setState(556);
				eos();
				}
				}
				setState(562);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(573);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << PRED) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (CONST - 64)) | (1L << (TYPE - 64)) | (1L << (VAR - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (STAR - 64)) | (1L << (RECEIVE - 64)))) != 0)) {
				{
				{
				setState(567);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(563);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(564);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(565);
					declaration();
					}
					break;
				case 4:
					{
					setState(566);
					ghostMember();
					}
					break;
				}
				setState(569);
				eos();
				}
				}
				setState(575);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(576);
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
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
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
		enterRule(_localctx, 68, RULE_ghostMember);
		try {
			setState(593);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(578);
				fpredicateDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(579);
				mpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(580);
				implementationProof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(584);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(581);
					match(GHOST);
					}
					break;
				case 2:
					{
					{
					setState(582);
					match(GHOST);
					setState(583);
					eos();
					}
					}
					break;
				}
				setState(591);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
				case 1:
					{
					setState(586);
					methodDecl();
					}
					break;
				case 2:
					{
					setState(587);
					functionDecl();
					}
					break;
				case 3:
					{
					setState(588);
					constDecl();
					}
					break;
				case 4:
					{
					setState(589);
					typeDecl();
					}
					break;
				case 5:
					{
					setState(590);
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
		enterRule(_localctx, 70, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(595);
			match(PRED);
			setState(596);
			match(IDENTIFIER);
			setState(597);
			parameters();
			setState(598);
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
		enterRule(_localctx, 72, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(600);
			match(L_CURLY);
			setState(601);
			expression(0);
			setState(602);
			eos();
			setState(603);
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
		enterRule(_localctx, 74, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(605);
			match(PRED);
			setState(606);
			receiver();
			setState(607);
			match(IDENTIFIER);
			setState(608);
			parameters();
			setState(609);
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
		enterRule(_localctx, 76, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			type_();
			setState(612);
			match(IMPL);
			setState(613);
			type_();
			setState(614);
			match(L_CURLY);
			setState(620);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRED) {
				{
				{
				setState(615);
				implementationProofPredicateAlias();
				setState(616);
				eos();
				}
				}
				setState(622);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PURE || _la==L_PAREN) {
				{
				{
				setState(623);
				methodImplementationProof();
				setState(624);
				eos();
				}
				}
				setState(630);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(631);
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
		enterRule(_localctx, 78, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(634);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(633);
				match(PURE);
				}
			}

			setState(636);
			nonLocalReceiver();
			setState(637);
			match(IDENTIFIER);
			setState(638);
			signature();
			setState(640);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(639);
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
		enterRule(_localctx, 80, RULE_selection);
		try {
			setState(650);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(642);
				primaryExpr(0);
				setState(643);
				match(DOT);
				setState(644);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(646);
				type_();
				setState(647);
				match(DOT);
				setState(648);
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
		enterRule(_localctx, 82, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(652);
			match(PRED);
			setState(653);
			match(IDENTIFIER);
			setState(654);
			match(DECLARE_ASSIGN);
			setState(657);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				setState(655);
				selection();
				}
				break;
			case 2:
				{
				setState(656);
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
		enterRule(_localctx, 84, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(659);
			maybeAddressableIdentifierList();
			setState(667);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
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
				setState(660);
				type_();
				setState(663);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
				case 1:
					{
					setState(661);
					match(ASSIGN);
					setState(662);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(665);
				match(ASSIGN);
				setState(666);
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
		enterRule(_localctx, 86, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(669);
			maybeAddressableIdentifierList();
			setState(670);
			match(DECLARE_ASSIGN);
			setState(671);
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
		enterRule(_localctx, 88, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			match(L_PAREN);
			setState(675);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(674);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(678);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(677);
				match(STAR);
				}
			}

			setState(680);
			match(IDENTIFIER);
			setState(681);
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
		enterRule(_localctx, 90, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683);
			match(L_PAREN);
			setState(685);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(684);
				match(IDENTIFIER);
				}
				break;
			}
			setState(688);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(687);
				match(STAR);
				}
			}

			setState(690);
			typeName();
			setState(691);
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
		enterRule(_localctx, 92, RULE_parameterDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(694);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(693);
				match(GHOST);
				}
				break;
			}
			setState(697);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(696);
				identifierList();
				}
				break;
			}
			setState(700);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(699);
				match(ELLIPSIS);
				}
			}

			setState(702);
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
		enterRule(_localctx, 94, RULE_unaryExpr);
		int _la;
		try {
			setState(713);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(704);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(705);
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
				setState(706);
				match(L_PAREN);
				setState(707);
				expression(0);
				setState(708);
				match(R_PAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(710);
				unfolding();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(711);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & ((1L << (EXCLAMATION - 109)) | (1L << (PLUS - 109)) | (1L << (MINUS - 109)) | (1L << (CARET - 109)) | (1L << (STAR - 109)) | (1L << (AMPERSAND - 109)) | (1L << (RECEIVE - 109)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(712);
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
		enterRule(_localctx, 96, RULE_unfolding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(715);
			match(UNFOLDING);
			setState(716);
			predicateAccess();
			setState(717);
			match(IN);
			setState(718);
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
		public Token call_op;
		public Token unary_op;
		public Token mul_op;
		public Token add_op;
		public Token p42_op;
		public Token p41_op;
		public Token rel_op;
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public TerminalNode LEN() { return getToken(GobraParser.LEN, 0); }
		public TerminalNode CAP() { return getToken(GobraParser.CAP, 0); }
		public TerminalNode DOM() { return getToken(GobraParser.DOM, 0); }
		public TerminalNode RANGE() { return getToken(GobraParser.RANGE, 0); }
		public UnfoldingContext unfolding() {
			return getRuleContext(UnfoldingContext.class,0);
		}
		public New_Context new_() {
			return getRuleContext(New_Context.class,0);
		}
		public MakeContext make() {
			return getRuleContext(MakeContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(GobraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(GobraParser.MINUS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(GobraParser.EXCLAMATION, 0); }
		public TerminalNode CARET() { return getToken(GobraParser.CARET, 0); }
		public TerminalNode STAR() { return getToken(GobraParser.STAR, 0); }
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public TerminalNode RECEIVE() { return getToken(GobraParser.RECEIVE, 0); }
		public TerminalNode DIV() { return getToken(GobraParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(GobraParser.MOD, 0); }
		public TerminalNode LSHIFT() { return getToken(GobraParser.LSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(GobraParser.RSHIFT, 0); }
		public TerminalNode BIT_CLEAR() { return getToken(GobraParser.BIT_CLEAR, 0); }
		public TerminalNode OR() { return getToken(GobraParser.OR, 0); }
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
		int _startState = 98;
		enterRecursionRule(_localctx, 98, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(732);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(721);
				primaryExpr(0);
				}
				break;
			case 2:
				{
				setState(722);
				((ExpressionContext)_localctx).call_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << RANGE) | (1L << LEN) | (1L << CAP) | (1L << DOM))) != 0)) ) {
					((ExpressionContext)_localctx).call_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(723);
				match(L_PAREN);
				setState(724);
				expression(0);
				setState(725);
				match(R_PAREN);
				}
				break;
			case 3:
				{
				setState(727);
				unfolding();
				}
				break;
			case 4:
				{
				setState(728);
				new_();
				}
				break;
			case 5:
				{
				setState(729);
				make();
				}
				break;
			case 6:
				{
				setState(730);
				((ExpressionContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & ((1L << (EXCLAMATION - 109)) | (1L << (PLUS - 109)) | (1L << (MINUS - 109)) | (1L << (CARET - 109)) | (1L << (STAR - 109)) | (1L << (AMPERSAND - 109)) | (1L << (RECEIVE - 109)))) != 0)) ) {
					((ExpressionContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(731);
				expression(10);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(766);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(764);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(734);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(735);
						((ExpressionContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 104)) & ~0x3f) == 0 && ((1L << (_la - 104)) & ((1L << (DIV - 104)) | (1L << (MOD - 104)) | (1L << (LSHIFT - 104)) | (1L << (RSHIFT - 104)) | (1L << (BIT_CLEAR - 104)) | (1L << (STAR - 104)) | (1L << (AMPERSAND - 104)))) != 0)) ) {
							((ExpressionContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(736);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(737);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(738);
						((ExpressionContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & ((1L << (PLUS_PLUS - 91)) | (1L << (OR - 91)) | (1L << (PLUS - 91)) | (1L << (MINUS - 91)) | (1L << (CARET - 91)))) != 0)) ) {
							((ExpressionContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(739);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(740);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(741);
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
						setState(742);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(743);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(744);
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
						setState(745);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(746);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(747);
						((ExpressionContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (EQUALS - 97)) | (1L << (NOT_EQUALS - 97)) | (1L << (LESS - 97)) | (1L << (LESS_OR_EQUALS - 97)) | (1L << (GREATER - 97)) | (1L << (GREATER_OR_EQUALS - 97)))) != 0)) ) {
							((ExpressionContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(748);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(749);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(750);
						match(LOGICAL_AND);
						setState(751);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(752);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(753);
						match(LOGICAL_OR);
						setState(754);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(755);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(756);
						match(IMPLIES);
						setState(757);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(758);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(759);
						match(QMARK);
						setState(760);
						expression(0);
						setState(761);
						match(COLON);
						setState(762);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(768);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
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
			setState(769);
			match(MAKE);
			setState(770);
			match(L_PAREN);
			setState(771);
			type_();
			setState(774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(772);
				match(COMMA);
				setState(773);
				expressionList();
				}
			}

			setState(776);
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
			setState(778);
			match(NEW);
			setState(779);
			match(L_PAREN);
			setState(780);
			type_();
			setState(781);
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
		enterRule(_localctx, 104, RULE_statement);
		try {
			setState(799);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(783);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(784);
				declaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(785);
				labeledStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(786);
				simpleStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(787);
				goStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(788);
				returnStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(789);
				breakStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(790);
				continueStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(791);
				gotoStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(792);
				fallthroughStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(793);
				block();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(794);
				ifStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(795);
				switchStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(796);
				selectStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(797);
				specForStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(798);
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
		enterRule(_localctx, 106, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			loopSpec();
			setState(802);
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
		enterRule(_localctx, 108, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(810);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(804);
				match(INV);
				setState(805);
				expression(0);
				setState(806);
				eos();
				}
				}
				setState(812);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(817);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(813);
				match(DEC);
				setState(814);
				terminationMeasure();
				setState(815);
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
		enterRule(_localctx, 110, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			expressionList();
			setState(822);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(820);
				match(IF);
				setState(821);
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
		enterRule(_localctx, 112, RULE_basicLit);
		try {
			setState(832);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(824);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(825);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(826);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(827);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(828);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(829);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(830);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(831);
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
		public PredConstructArgsContext predConstructArgs() {
			return getRuleContext(PredConstructArgsContext.class,0);
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
		int _startState = 114;
		enterRecursionRule(_localctx, 114, RULE_primaryExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(839);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(835);
				operand();
				}
				break;
			case 2:
				{
				setState(836);
				conversion();
				}
				break;
			case 3:
				{
				setState(837);
				methodExpr();
				}
				break;
			case 4:
				{
				setState(838);
				ghostPrimaryExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(854);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrimaryExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
					setState(841);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(850);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
					case 1:
						{
						{
						setState(842);
						match(DOT);
						setState(843);
						match(IDENTIFIER);
						}
						}
						break;
					case 2:
						{
						setState(844);
						index();
						}
						break;
					case 3:
						{
						setState(845);
						slice_();
						}
						break;
					case 4:
						{
						setState(846);
						seqUpdExp();
						}
						break;
					case 5:
						{
						setState(847);
						typeAssertion();
						}
						break;
					case 6:
						{
						setState(848);
						arguments();
						}
						break;
					case 7:
						{
						setState(849);
						predConstructArgs();
						}
						break;
					}
					}
					} 
				}
				setState(856);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
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
		enterRule(_localctx, 116, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(857);
			match(L_PRED);
			setState(859);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
				{
				setState(858);
				expressionList();
				}
			}

			setState(862);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(861);
				match(COMMA);
				}
			}

			setState(864);
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
		enterRule(_localctx, 118, RULE_interfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
			match(INTERFACE);
			setState(867);
			match(L_CURLY);
			setState(877);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(871);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
					case 1:
						{
						setState(868);
						methodSpec();
						}
						break;
					case 2:
						{
						setState(869);
						typeName();
						}
						break;
					case 3:
						{
						setState(870);
						predicateSpec();
						}
						break;
					}
					setState(873);
					eos();
					}
					} 
				}
				setState(879);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			}
			setState(880);
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
		enterRule(_localctx, 120, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(882);
			match(PRED);
			setState(883);
			match(IDENTIFIER);
			setState(884);
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
		enterRule(_localctx, 122, RULE_methodSpec);
		int _la;
		try {
			setState(902);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(886);
				if (!(noTerminatorAfterParams(2))) throw new FailedPredicateException(this, "noTerminatorAfterParams(2)");
				setState(888);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(887);
					match(GHOST);
					}
				}

				setState(890);
				specification();
				setState(891);
				match(IDENTIFIER);
				setState(892);
				parameters();
				setState(893);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(896);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(895);
					match(GHOST);
					}
				}

				setState(898);
				specification();
				setState(899);
				match(IDENTIFIER);
				setState(900);
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
		enterRule(_localctx, 124, RULE_type_);
		try {
			setState(911);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(904);
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
				setState(905);
				typeLit();
				}
				break;
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 3);
				{
				setState(906);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(907);
				match(L_PAREN);
				setState(908);
				type_();
				setState(909);
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
		enterRule(_localctx, 126, RULE_typeLit);
		try {
			setState(922);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(913);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(914);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(915);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(916);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(917);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(918);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(919);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(920);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(921);
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
		enterRule(_localctx, 128, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(924);
			match(PRED);
			setState(925);
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
		enterRule(_localctx, 130, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(927);
			match(L_PAREN);
			setState(939);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << PRED) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (STAR - 64)) | (1L << (RECEIVE - 64)))) != 0)) {
				{
				setState(928);
				type_();
				setState(933);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(929);
						match(COMMA);
						setState(930);
						type_();
						}
						} 
					}
					setState(935);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
				}
				setState(937);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(936);
					match(COMMA);
					}
				}

				}
			}

			setState(941);
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
		enterRule(_localctx, 132, RULE_literalType);
		try {
			setState(953);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(943);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(944);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(945);
				match(L_BRACKET);
				setState(946);
				match(ELLIPSIS);
				setState(947);
				match(R_BRACKET);
				setState(948);
				elementType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(949);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(950);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(951);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(952);
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
		enterRule(_localctx, 134, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(955);
			match(L_BRACKET);
			setState(971);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				{
				setState(957);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
					{
					setState(956);
					low();
					}
				}

				setState(959);
				match(COLON);
				setState(961);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
					{
					setState(960);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(964);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
					{
					setState(963);
					low();
					}
				}

				setState(966);
				match(COLON);
				setState(967);
				high();
				setState(968);
				match(COLON);
				setState(969);
				cap();
				}
				break;
			}
			setState(973);
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
		enterRule(_localctx, 136, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(975);
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
		enterRule(_localctx, 138, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(977);
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
		enterRule(_localctx, 140, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(979);
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
		enterRule(_localctx, 142, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			match(IF);
			setState(986);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				{
				setState(983);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
				case 1:
					{
					setState(982);
					simpleStmt();
					}
					break;
				}
				setState(985);
				match(SEMI);
				}
				break;
			}
			setState(988);
			expression(0);
			setState(989);
			block();
			setState(995);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				{
				setState(990);
				match(ELSE);
				setState(993);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(991);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(992);
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
		enterRule(_localctx, 144, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(998);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 103)) & ~0x3f) == 0 && ((1L << (_la - 103)) & ((1L << (OR - 103)) | (1L << (DIV - 103)) | (1L << (MOD - 103)) | (1L << (LSHIFT - 103)) | (1L << (RSHIFT - 103)) | (1L << (BIT_CLEAR - 103)) | (1L << (PLUS - 103)) | (1L << (MINUS - 103)) | (1L << (CARET - 103)) | (1L << (STAR - 103)) | (1L << (AMPERSAND - 103)))) != 0)) {
				{
				setState(997);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 103)) & ~0x3f) == 0 && ((1L << (_la - 103)) & ((1L << (OR - 103)) | (1L << (DIV - 103)) | (1L << (MOD - 103)) | (1L << (LSHIFT - 103)) | (1L << (RSHIFT - 103)) | (1L << (BIT_CLEAR - 103)) | (1L << (PLUS - 103)) | (1L << (MINUS - 103)) | (1L << (CARET - 103)) | (1L << (STAR - 103)) | (1L << (AMPERSAND - 103)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1000);
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
		enterRule(_localctx, 146, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1002);
			match(SWITCH);
			setState(1007);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(1004);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
				case 1:
					{
					setState(1003);
					simpleStmt();
					}
					break;
				}
				setState(1006);
				match(SEMI);
				}
				break;
			}
			setState(1010);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
				{
				setState(1009);
				expression(0);
				}
			}

			setState(1012);
			match(L_CURLY);
			setState(1016);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1013);
				exprCaseClause();
				}
				}
				setState(1018);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1019);
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
		enterRule(_localctx, 148, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1021);
			match(SWITCH);
			setState(1026);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(1023);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
				case 1:
					{
					setState(1022);
					simpleStmt();
					}
					break;
				}
				setState(1025);
				match(SEMI);
				}
				break;
			}
			setState(1028);
			typeSwitchGuard();
			setState(1029);
			match(L_CURLY);
			setState(1033);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1030);
				typeCaseClause();
				}
				}
				setState(1035);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1036);
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
		enterRule(_localctx, 150, RULE_eos);
		try {
			setState(1043);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1038);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1039);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1040);
				if (!(lineTerminatorAhead())) throw new FailedPredicateException(this, "lineTerminatorAhead()");
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1041);
				if (!(checkPreviousTokenText("}"))) throw new FailedPredicateException(this, "checkPreviousTokenText(\"}\")");
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1042);
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
		enterRule(_localctx, 152, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			match(PACKAGE);
			setState(1046);
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
		enterRule(_localctx, 154, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1048);
			match(IMPORT);
			setState(1060);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1049);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1050);
				match(L_PAREN);
				setState(1056);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (IDENTIFIER - 79)) | (1L << (DOT - 79)) | (1L << (RAW_STRING_LIT - 79)) | (1L << (INTERPRETED_STRING_LIT - 79)))) != 0)) {
					{
					{
					setState(1051);
					importSpec();
					setState(1052);
					eos();
					}
					}
					setState(1058);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1059);
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
		enterRule(_localctx, 156, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1063);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1062);
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

			setState(1065);
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
		enterRule(_localctx, 158, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1067);
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
		enterRule(_localctx, 160, RULE_declaration);
		try {
			setState(1072);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1069);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1070);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1071);
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
		enterRule(_localctx, 162, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1074);
			match(CONST);
			setState(1086);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1075);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1076);
				match(L_PAREN);
				setState(1082);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1077);
					constSpec();
					setState(1078);
					eos();
					}
					}
					setState(1084);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1085);
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
		enterRule(_localctx, 164, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1088);
			identifierList();
			setState(1094);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				{
				setState(1090);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << PRED) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (STAR - 64)) | (1L << (RECEIVE - 64)))) != 0)) {
					{
					setState(1089);
					type_();
					}
				}

				setState(1092);
				match(ASSIGN);
				setState(1093);
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
		enterRule(_localctx, 166, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1096);
			match(IDENTIFIER);
			setState(1101);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1097);
					match(COMMA);
					setState(1098);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1103);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
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
		enterRule(_localctx, 168, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1104);
			expression(0);
			setState(1109);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,99,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1105);
					match(COMMA);
					setState(1106);
					expression(0);
					}
					} 
				}
				setState(1111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,99,_ctx);
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
		enterRule(_localctx, 170, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1112);
			match(TYPE);
			setState(1124);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1113);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1114);
				match(L_PAREN);
				setState(1120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1115);
					typeSpec();
					setState(1116);
					eos();
					}
					}
					setState(1122);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1123);
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
		enterRule(_localctx, 172, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1126);
			match(IDENTIFIER);
			setState(1128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1127);
				match(ASSIGN);
				}
			}

			setState(1130);
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
		enterRule(_localctx, 174, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1132);
			match(VAR);
			setState(1144);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1133);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1134);
				match(L_PAREN);
				setState(1140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1135);
					varSpec();
					setState(1136);
					eos();
					}
					}
					setState(1142);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1143);
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
		enterRule(_localctx, 176, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1146);
			match(L_CURLY);
			setState(1148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
				{
				setState(1147);
				statementList();
				}
			}

			setState(1150);
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
		enterRule(_localctx, 178, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1155); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1152);
				statement();
				setState(1153);
				eos();
				}
				}
				setState(1157); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 180, RULE_simpleStmt);
		try {
			setState(1165);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1159);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1160);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1161);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1162);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1163);
				shortVarDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1164);
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
		enterRule(_localctx, 182, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1167);
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
		enterRule(_localctx, 184, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1169);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1170);
			match(RECEIVE);
			setState(1171);
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
		enterRule(_localctx, 186, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1173);
			expression(0);
			setState(1174);
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
		enterRule(_localctx, 188, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1176);
			expressionList();
			setState(1177);
			assign_op();
			setState(1178);
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
		enterRule(_localctx, 190, RULE_emptyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1180);
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
		enterRule(_localctx, 192, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1182);
			match(IDENTIFIER);
			setState(1183);
			match(COLON);
			setState(1185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				{
				setState(1184);
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
		enterRule(_localctx, 194, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1187);
			match(RETURN);
			setState(1189);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1188);
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
		enterRule(_localctx, 196, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1191);
			match(BREAK);
			setState(1193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				{
				setState(1192);
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
		enterRule(_localctx, 198, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1195);
			match(CONTINUE);
			setState(1197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1196);
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
		enterRule(_localctx, 200, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(GOTO);
			setState(1200);
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
		enterRule(_localctx, 202, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1202);
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
		enterRule(_localctx, 204, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1204);
			match(DEFER);
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
		enterRule(_localctx, 206, RULE_switchStmt);
		try {
			setState(1209);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1207);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1208);
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
		enterRule(_localctx, 208, RULE_exprCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1211);
			exprSwitchCase();
			setState(1212);
			match(COLON);
			setState(1214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
				{
				setState(1213);
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
		enterRule(_localctx, 210, RULE_exprSwitchCase);
		try {
			setState(1219);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1216);
				match(CASE);
				setState(1217);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1218);
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
		enterRule(_localctx, 212, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1223);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				{
				setState(1221);
				match(IDENTIFIER);
				setState(1222);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1225);
			primaryExpr(0);
			setState(1226);
			match(DOT);
			setState(1227);
			match(L_PAREN);
			setState(1228);
			match(TYPE);
			setState(1229);
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
		enterRule(_localctx, 214, RULE_typeCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			typeSwitchCase();
			setState(1232);
			match(COLON);
			setState(1234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
				{
				setState(1233);
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
		enterRule(_localctx, 216, RULE_typeSwitchCase);
		try {
			setState(1239);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1236);
				match(CASE);
				setState(1237);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1238);
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
		enterRule(_localctx, 218, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
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
				setState(1241);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1242);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1245);
				match(COMMA);
				setState(1248);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GHOST:
				case SEQ:
				case SET:
				case MSET:
				case DICT:
				case OPT:
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
					setState(1246);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1247);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1254);
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
		enterRule(_localctx, 220, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1255);
			match(SELECT);
			setState(1256);
			match(L_CURLY);
			setState(1260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1257);
				commClause();
				}
				}
				setState(1262);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1263);
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
		enterRule(_localctx, 222, RULE_commClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1265);
			commCase();
			setState(1266);
			match(COLON);
			setState(1268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BREAK) | (1L << FUNC) | (1L << INTERFACE) | (1L << SELECT) | (1L << DEFER) | (1L << GO) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)) | (1L << (OCTAL_LIT - 64)) | (1L << (HEX_LIT - 64)) | (1L << (FLOAT_LIT - 64)) | (1L << (IMAGINARY_LIT - 64)) | (1L << (RUNE_LIT - 64)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
				{
				setState(1267);
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
		enterRule(_localctx, 224, RULE_commCase);
		try {
			setState(1276);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1270);
				match(CASE);
				setState(1273);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
				case 1:
					{
					setState(1271);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1272);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1275);
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
		enterRule(_localctx, 226, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1284);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				{
				setState(1278);
				expressionList();
				setState(1279);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1281);
				identifierList();
				setState(1282);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1286);
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
		enterRule(_localctx, 228, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1288);
			match(FOR);
			setState(1292);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
			case 1:
				{
				setState(1289);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1290);
				forClause();
				}
				break;
			case 3:
				{
				setState(1291);
				rangeClause();
				}
				break;
			}
			setState(1294);
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
		enterRule(_localctx, 230, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				{
				setState(1296);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1299);
			match(SEMI);
			setState(1301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
				{
				setState(1300);
				expression(0);
				}
			}

			setState(1303);
			match(SEMI);
			setState(1305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (SEMI - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
				{
				setState(1304);
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
		enterRule(_localctx, 232, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1313);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1307);
				expressionList();
				setState(1308);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1310);
				identifierList();
				setState(1311);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1315);
			match(RANGE);
			setState(1316);
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
		enterRule(_localctx, 234, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1318);
			match(GO);
			setState(1319);
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
		enterRule(_localctx, 236, RULE_typeName);
		try {
			setState(1323);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1321);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1322);
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
		enterRule(_localctx, 238, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1325);
			match(L_BRACKET);
			setState(1326);
			arrayLength();
			setState(1327);
			match(R_BRACKET);
			setState(1328);
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
		enterRule(_localctx, 240, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1330);
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
		enterRule(_localctx, 242, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1332);
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
		enterRule(_localctx, 244, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1334);
			match(STAR);
			setState(1335);
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
		enterRule(_localctx, 246, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1337);
			match(L_BRACKET);
			setState(1338);
			match(R_BRACKET);
			setState(1339);
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
		enterRule(_localctx, 248, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			match(MAP);
			setState(1342);
			match(L_BRACKET);
			setState(1343);
			type_();
			setState(1344);
			match(R_BRACKET);
			setState(1345);
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
		enterRule(_localctx, 250, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1352);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1347);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1348);
				match(CHAN);
				setState(1349);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1350);
				match(RECEIVE);
				setState(1351);
				match(CHAN);
				}
				break;
			}
			setState(1354);
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
		enterRule(_localctx, 252, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1356);
			match(FUNC);
			setState(1357);
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
		enterRule(_localctx, 254, RULE_signature);
		try {
			setState(1364);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1359);
				if (!(noTerminatorAfterParams(1))) throw new FailedPredicateException(this, "noTerminatorAfterParams(1)");
				setState(1360);
				parameters();
				setState(1361);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1363);
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
		enterRule(_localctx, 256, RULE_result);
		try {
			setState(1368);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1366);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1367);
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
		enterRule(_localctx, 258, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			match(L_PAREN);
			setState(1382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << PRED) | (1L << FUNC) | (1L << INTERFACE) | (1L << MAP) | (1L << STRUCT))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (CHAN - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (ELLIPSIS - 64)) | (1L << (STAR - 64)) | (1L << (RECEIVE - 64)))) != 0)) {
				{
				setState(1371);
				parameterDecl();
				setState(1376);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,135,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1372);
						match(COMMA);
						setState(1373);
						parameterDecl();
						}
						} 
					}
					setState(1378);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,135,_ctx);
				}
				setState(1380);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1379);
					match(COMMA);
					}
				}

				}
			}

			setState(1384);
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
		enterRule(_localctx, 260, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1386);
			type_();
			setState(1387);
			match(L_PAREN);
			setState(1388);
			expression(0);
			setState(1390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1389);
				match(COMMA);
				}
			}

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
		enterRule(_localctx, 262, RULE_operand);
		try {
			setState(1400);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1394);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1395);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1396);
				match(L_PAREN);
				setState(1397);
				expression(0);
				setState(1398);
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
		enterRule(_localctx, 264, RULE_literal);
		try {
			setState(1405);
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
				setState(1402);
				basicLit();
				}
				break;
			case GHOST:
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
				setState(1403);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1404);
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
		enterRule(_localctx, 266, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1407);
			_la = _input.LA(1);
			if ( !(((((_la - 116)) & ~0x3f) == 0 && ((1L << (_la - 116)) & ((1L << (DECIMAL_LIT - 116)) | (1L << (BINARY_LIT - 116)) | (1L << (OCTAL_LIT - 116)) | (1L << (HEX_LIT - 116)) | (1L << (IMAGINARY_LIT - 116)) | (1L << (RUNE_LIT - 116)))) != 0)) ) {
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
		enterRule(_localctx, 268, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1409);
			match(IDENTIFIER);
			setState(1412);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				{
				setState(1410);
				match(DOT);
				setState(1411);
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
		enterRule(_localctx, 270, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1414);
			match(IDENTIFIER);
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
		enterRule(_localctx, 272, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1418);
			literalType();
			setState(1419);
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
		enterRule(_localctx, 274, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1421);
			match(L_CURLY);
			setState(1426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_CURLY - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
				{
				setState(1422);
				elementList();
				setState(1424);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1423);
					match(COMMA);
					}
				}

				}
			}

			setState(1428);
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
		enterRule(_localctx, 276, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1430);
			keyedElement();
			setState(1435);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,144,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1431);
					match(COMMA);
					setState(1432);
					keyedElement();
					}
					} 
				}
				setState(1437);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,144,_ctx);
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
		enterRule(_localctx, 278, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1441);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				{
				setState(1438);
				key();
				setState(1439);
				match(COLON);
				}
				break;
			}
			setState(1443);
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
		enterRule(_localctx, 280, RULE_key);
		try {
			setState(1448);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1445);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1446);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1447);
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
		enterRule(_localctx, 282, RULE_element);
		try {
			setState(1452);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case OLD:
			case FORALL:
			case ACCESS:
			case UNFOLDING:
			case GHOST:
			case RANGE:
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
				setState(1450);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1451);
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
		enterRule(_localctx, 284, RULE_structType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1454);
			match(STRUCT);
			setState(1455);
			match(L_CURLY);
			setState(1461);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,148,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1456);
					fieldDecl();
					setState(1457);
					eos();
					}
					} 
				}
				setState(1463);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,148,_ctx);
			}
			setState(1464);
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
		enterRule(_localctx, 286, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				{
				setState(1466);
				if (!(noTerminatorBetween(2))) throw new FailedPredicateException(this, "noTerminatorBetween(2)");
				setState(1467);
				identifierList();
				setState(1468);
				type_();
				}
				break;
			case 2:
				{
				setState(1470);
				embeddedField();
				}
				break;
			}
			setState(1474);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(1473);
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
		enterRule(_localctx, 288, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1476);
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
		enterRule(_localctx, 290, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1479);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1478);
				match(STAR);
				}
			}

			setState(1481);
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
		enterRule(_localctx, 292, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1483);
			match(FUNC);
			setState(1484);
			signature();
			setState(1485);
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
		enterRule(_localctx, 294, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1487);
			match(L_BRACKET);
			setState(1488);
			expression(0);
			setState(1489);
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
		enterRule(_localctx, 296, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1491);
			match(DOT);
			setState(1492);
			match(L_PAREN);
			setState(1493);
			type_();
			setState(1494);
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
		enterRule(_localctx, 298, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1496);
			match(L_PAREN);
			setState(1511);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (TRUE - 1)) | (1L << (FALSE - 1)) | (1L << (OLD - 1)) | (1L << (FORALL - 1)) | (1L << (ACCESS - 1)) | (1L << (UNFOLDING - 1)) | (1L << (GHOST - 1)) | (1L << (RANGE - 1)) | (1L << (SEQ - 1)) | (1L << (SET - 1)) | (1L << (MSET - 1)) | (1L << (DICT - 1)) | (1L << (OPT - 1)) | (1L << (LEN - 1)) | (1L << (NEW - 1)) | (1L << (MAKE - 1)) | (1L << (CAP - 1)) | (1L << (SOME - 1)) | (1L << (GET - 1)) | (1L << (DOM - 1)) | (1L << (NONE - 1)) | (1L << (PRED - 1)) | (1L << (TYPE_OF - 1)) | (1L << (IS_COMPARABLE - 1)) | (1L << (FUNC - 1)) | (1L << (INTERFACE - 1)) | (1L << (MAP - 1)) | (1L << (STRUCT - 1)) | (1L << (CHAN - 1)))) != 0) || ((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (NIL_LIT - 78)) | (1L << (IDENTIFIER - 78)) | (1L << (L_PAREN - 78)) | (1L << (L_BRACKET - 78)) | (1L << (EXCLAMATION - 78)) | (1L << (PLUS - 78)) | (1L << (MINUS - 78)) | (1L << (CARET - 78)) | (1L << (STAR - 78)) | (1L << (AMPERSAND - 78)) | (1L << (RECEIVE - 78)) | (1L << (DECIMAL_LIT - 78)) | (1L << (BINARY_LIT - 78)) | (1L << (OCTAL_LIT - 78)) | (1L << (HEX_LIT - 78)) | (1L << (FLOAT_LIT - 78)) | (1L << (IMAGINARY_LIT - 78)) | (1L << (RUNE_LIT - 78)) | (1L << (RAW_STRING_LIT - 78)) | (1L << (INTERPRETED_STRING_LIT - 78)))) != 0)) {
				{
				setState(1503);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
				case 1:
					{
					setState(1497);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1498);
					type_();
					setState(1501);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
					case 1:
						{
						setState(1499);
						match(COMMA);
						setState(1500);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1506);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1505);
					match(ELLIPSIS);
					}
				}

				setState(1509);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1508);
					match(COMMA);
					}
				}

				}
			}

			setState(1513);
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
		enterRule(_localctx, 300, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1515);
			receiverType();
			setState(1516);
			match(DOT);
			setState(1517);
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
		enterRule(_localctx, 302, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1519);
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
		case 49:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 57:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 61:
			return methodSpec_sempred((MethodSpecContext)_localctx, predIndex);
		case 75:
			return eos_sempred((EosContext)_localctx, predIndex);
		case 127:
			return signature_sempred((SignatureContext)_localctx, predIndex);
		case 143:
			return fieldDecl_sempred((FieldDeclContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 9);
		case 1:
			return precpred(_ctx, 8);
		case 2:
			return precpred(_ctx, 7);
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 5);
		case 5:
			return precpred(_ctx, 4);
		case 6:
			return precpred(_ctx, 3);
		case 7:
			return precpred(_ctx, 2);
		case 8:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean primaryExpr_sempred(PrimaryExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean methodSpec_sempred(MethodSpecContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return noTerminatorAfterParams(2);
		}
		return true;
	}
	private boolean eos_sempred(EosContext _localctx, int predIndex) {
		switch (predIndex) {
		case 11:
			return lineTerminatorAhead();
		case 12:
			return checkPreviousTokenText("}");
		case 13:
			return checkPreviousTokenText(")");
		}
		return true;
	}
	private boolean signature_sempred(SignatureContext _localctx, int predIndex) {
		switch (predIndex) {
		case 14:
			return noTerminatorAfterParams(1);
		}
		return true;
	}
	private boolean fieldDecl_sempred(FieldDeclContext _localctx, int predIndex) {
		switch (predIndex) {
		case 15:
			return noTerminatorBetween(2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0089\u05f4\4\2\t"+
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
		"\t\u0097\4\u0098\t\u0098\4\u0099\t\u0099\3\2\3\2\3\2\7\2\u0136\n\2\f\2"+
		"\16\2\u0139\13\2\3\3\3\3\5\3\u013d\n\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0145"+
		"\n\4\3\5\3\5\3\5\7\5\u014a\n\5\f\5\16\5\u014d\13\5\3\5\5\5\u0150\n\5\3"+
		"\6\3\6\3\6\7\6\u0155\n\6\f\6\16\6\u0158\13\6\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\5\b\u0164\n\b\5\b\u0166\n\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\5\13\u0180\n\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\7\20\u0197\n"+
		"\20\f\20\16\20\u019a\13\20\3\21\3\21\3\21\3\21\7\21\u01a0\n\21\f\21\16"+
		"\21\u01a3\13\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\5\22\u01ac\n\22\3\22"+
		"\3\22\3\22\3\22\3\23\3\23\5\23\u01b4\n\23\3\24\3\24\3\25\3\25\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\5\27\u01c4\n\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31"+
		"\u01d6\n\31\3\32\3\32\3\32\3\32\7\32\u01dc\n\32\f\32\16\32\u01df\13\32"+
		"\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\7\34\u01ea\n\34\f\34\16"+
		"\34\u01ed\13\34\3\34\3\34\3\34\5\34\u01f2\n\34\3\34\7\34\u01f5\n\34\f"+
		"\34\16\34\u01f8\13\34\5\34\u01fa\n\34\3\35\3\35\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\5\35\u0204\n\35\3\36\3\36\3\36\3\36\3\36\5\36\u020b\n\36\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\5\37\u0213\n\37\3 \3 \3 \3 \3 \5 \u021a\n \3"+
		" \5 \u021d\n \3 \3 \3!\3!\5!\u0223\n!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3"+
		"#\3#\3#\3#\7#\u0231\n#\f#\16#\u0234\13#\3#\3#\3#\3#\5#\u023a\n#\3#\3#"+
		"\7#\u023e\n#\f#\16#\u0241\13#\3#\3#\3$\3$\3$\3$\3$\3$\5$\u024b\n$\3$\3"+
		"$\3$\3$\3$\5$\u0252\n$\5$\u0254\n$\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\7(\u026d\n(\f(\16(\u0270\13(\3"+
		"(\3(\3(\7(\u0275\n(\f(\16(\u0278\13(\3(\3(\3)\5)\u027d\n)\3)\3)\3)\3)"+
		"\5)\u0283\n)\3*\3*\3*\3*\3*\3*\3*\3*\5*\u028d\n*\3+\3+\3+\3+\3+\5+\u0294"+
		"\n+\3,\3,\3,\3,\5,\u029a\n,\3,\3,\5,\u029e\n,\3-\3-\3-\3-\3.\3.\5.\u02a6"+
		"\n.\3.\5.\u02a9\n.\3.\3.\3.\3/\3/\5/\u02b0\n/\3/\5/\u02b3\n/\3/\3/\3/"+
		"\3\60\5\60\u02b9\n\60\3\60\5\60\u02bc\n\60\3\60\5\60\u02bf\n\60\3\60\3"+
		"\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u02cc\n\61\3\62"+
		"\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\5\63\u02df\n\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\63\7\63\u02ff\n\63\f\63\16\63\u0302\13"+
		"\63\3\64\3\64\3\64\3\64\3\64\5\64\u0309\n\64\3\64\3\64\3\65\3\65\3\65"+
		"\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\3\66\3\66\3\66\5\66\u0322\n\66\3\67\3\67\3\67\38\38\38\38\78\u032b"+
		"\n8\f8\168\u032e\138\38\38\38\38\58\u0334\n8\39\39\39\59\u0339\n9\3:\3"+
		":\3:\3:\3:\3:\3:\3:\5:\u0343\n:\3;\3;\3;\3;\3;\5;\u034a\n;\3;\3;\3;\3"+
		";\3;\3;\3;\3;\3;\5;\u0355\n;\7;\u0357\n;\f;\16;\u035a\13;\3<\3<\5<\u035e"+
		"\n<\3<\5<\u0361\n<\3<\3<\3=\3=\3=\3=\3=\5=\u036a\n=\3=\3=\7=\u036e\n="+
		"\f=\16=\u0371\13=\3=\3=\3>\3>\3>\3>\3?\3?\5?\u037b\n?\3?\3?\3?\3?\3?\3"+
		"?\5?\u0383\n?\3?\3?\3?\3?\5?\u0389\n?\3@\3@\3@\3@\3@\3@\3@\5@\u0392\n"+
		"@\3A\3A\3A\3A\3A\3A\3A\3A\3A\5A\u039d\nA\3B\3B\3B\3C\3C\3C\3C\7C\u03a6"+
		"\nC\fC\16C\u03a9\13C\3C\5C\u03ac\nC\5C\u03ae\nC\3C\3C\3D\3D\3D\3D\3D\3"+
		"D\3D\3D\3D\3D\5D\u03bc\nD\3E\3E\5E\u03c0\nE\3E\3E\5E\u03c4\nE\3E\5E\u03c7"+
		"\nE\3E\3E\3E\3E\3E\5E\u03ce\nE\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\5I\u03da"+
		"\nI\3I\5I\u03dd\nI\3I\3I\3I\3I\3I\5I\u03e4\nI\5I\u03e6\nI\3J\5J\u03e9"+
		"\nJ\3J\3J\3K\3K\5K\u03ef\nK\3K\5K\u03f2\nK\3K\5K\u03f5\nK\3K\3K\7K\u03f9"+
		"\nK\fK\16K\u03fc\13K\3K\3K\3L\3L\5L\u0402\nL\3L\5L\u0405\nL\3L\3L\3L\7"+
		"L\u040a\nL\fL\16L\u040d\13L\3L\3L\3M\3M\3M\3M\3M\5M\u0416\nM\3N\3N\3N"+
		"\3O\3O\3O\3O\3O\3O\7O\u0421\nO\fO\16O\u0424\13O\3O\5O\u0427\nO\3P\5P\u042a"+
		"\nP\3P\3P\3Q\3Q\3R\3R\3R\5R\u0433\nR\3S\3S\3S\3S\3S\3S\7S\u043b\nS\fS"+
		"\16S\u043e\13S\3S\5S\u0441\nS\3T\3T\5T\u0445\nT\3T\3T\5T\u0449\nT\3U\3"+
		"U\3U\7U\u044e\nU\fU\16U\u0451\13U\3V\3V\3V\7V\u0456\nV\fV\16V\u0459\13"+
		"V\3W\3W\3W\3W\3W\3W\7W\u0461\nW\fW\16W\u0464\13W\3W\5W\u0467\nW\3X\3X"+
		"\5X\u046b\nX\3X\3X\3Y\3Y\3Y\3Y\3Y\3Y\7Y\u0475\nY\fY\16Y\u0478\13Y\3Y\5"+
		"Y\u047b\nY\3Z\3Z\5Z\u047f\nZ\3Z\3Z\3[\3[\3[\6[\u0486\n[\r[\16[\u0487\3"+
		"\\\3\\\3\\\3\\\3\\\3\\\5\\\u0490\n\\\3]\3]\3^\3^\3^\3^\3_\3_\3_\3`\3`"+
		"\3`\3`\3a\3a\3b\3b\3b\5b\u04a4\nb\3c\3c\5c\u04a8\nc\3d\3d\5d\u04ac\nd"+
		"\3e\3e\5e\u04b0\ne\3f\3f\3f\3g\3g\3h\3h\3h\3i\3i\5i\u04bc\ni\3j\3j\3j"+
		"\5j\u04c1\nj\3k\3k\3k\5k\u04c6\nk\3l\3l\5l\u04ca\nl\3l\3l\3l\3l\3l\3l"+
		"\3m\3m\3m\5m\u04d5\nm\3n\3n\3n\5n\u04da\nn\3o\3o\5o\u04de\no\3o\3o\3o"+
		"\5o\u04e3\no\7o\u04e5\no\fo\16o\u04e8\13o\3p\3p\3p\7p\u04ed\np\fp\16p"+
		"\u04f0\13p\3p\3p\3q\3q\3q\5q\u04f7\nq\3r\3r\3r\5r\u04fc\nr\3r\5r\u04ff"+
		"\nr\3s\3s\3s\3s\3s\3s\5s\u0507\ns\3s\3s\3t\3t\3t\3t\5t\u050f\nt\3t\3t"+
		"\3u\5u\u0514\nu\3u\3u\5u\u0518\nu\3u\3u\5u\u051c\nu\3v\3v\3v\3v\3v\3v"+
		"\5v\u0524\nv\3v\3v\3v\3w\3w\3w\3x\3x\5x\u052e\nx\3y\3y\3y\3y\3y\3z\3z"+
		"\3{\3{\3|\3|\3|\3}\3}\3}\3}\3~\3~\3~\3~\3~\3~\3\177\3\177\3\177\3\177"+
		"\3\177\5\177\u054b\n\177\3\177\3\177\3\u0080\3\u0080\3\u0080\3\u0081\3"+
		"\u0081\3\u0081\3\u0081\3\u0081\5\u0081\u0557\n\u0081\3\u0082\3\u0082\5"+
		"\u0082\u055b\n\u0082\3\u0083\3\u0083\3\u0083\3\u0083\7\u0083\u0561\n\u0083"+
		"\f\u0083\16\u0083\u0564\13\u0083\3\u0083\5\u0083\u0567\n\u0083\5\u0083"+
		"\u0569\n\u0083\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084\5\u0084"+
		"\u0571\n\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\5\u0085\u057b\n\u0085\3\u0086\3\u0086\3\u0086\5\u0086\u0580\n"+
		"\u0086\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088\5\u0088\u0587\n\u0088\3"+
		"\u0089\3\u0089\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3\u008b\3\u008b"+
		"\3\u008b\5\u008b\u0593\n\u008b\5\u008b\u0595\n\u008b\3\u008b\3\u008b\3"+
		"\u008c\3\u008c\3\u008c\7\u008c\u059c\n\u008c\f\u008c\16\u008c\u059f\13"+
		"\u008c\3\u008d\3\u008d\3\u008d\5\u008d\u05a4\n\u008d\3\u008d\3\u008d\3"+
		"\u008e\3\u008e\3\u008e\5\u008e\u05ab\n\u008e\3\u008f\3\u008f\5\u008f\u05af"+
		"\n\u008f\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\7\u0090\u05b6\n\u0090"+
		"\f\u0090\16\u0090\u05b9\13\u0090\3\u0090\3\u0090\3\u0091\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\5\u0091\u05c2\n\u0091\3\u0091\5\u0091\u05c5\n\u0091\3"+
		"\u0092\3\u0092\3\u0093\5\u0093\u05ca\n\u0093\3\u0093\3\u0093\3\u0094\3"+
		"\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3\u0095\3\u0095\3\u0096\3\u0096"+
		"\3\u0096\3\u0096\3\u0096\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\5\u0097"+
		"\u05e0\n\u0097\5\u0097\u05e2\n\u0097\3\u0097\5\u0097\u05e5\n\u0097\3\u0097"+
		"\5\u0097\u05e8\n\u0097\5\u0097\u05ea\n\u0097\3\u0097\3\u0097\3\u0098\3"+
		"\u0098\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\2\4dt\u009a\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^"+
		"`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"+
		"\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120"+
		"\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\2\21\3\2\23\24\3\2\""+
		"$\4\2\"$&&\6\2!!\'\'**--\3\2ou\4\2jnst\5\2]]iipr\3\2\32\34\3\2\27\31\3"+
		"\2ch\4\2inpt\4\2QQ\\\\\3\2]^\4\2vy}~\3\2\u0084\u0085\2\u0651\2\u0132\3"+
		"\2\2\2\4\u013a\3\2\2\2\6\u0144\3\2\2\2\b\u0146\3\2\2\2\n\u0151\3\2\2\2"+
		"\f\u015b\3\2\2\2\16\u015d\3\2\2\2\20\u0169\3\2\2\2\22\u016c\3\2\2\2\24"+
		"\u017f\3\2\2\2\26\u0181\3\2\2\2\30\u0186\3\2\2\2\32\u018b\3\2\2\2\34\u0190"+
		"\3\2\2\2\36\u0198\3\2\2\2 \u019b\3\2\2\2\"\u01a6\3\2\2\2$\u01b3\3\2\2"+
		"\2&\u01b5\3\2\2\2(\u01b7\3\2\2\2*\u01bc\3\2\2\2,\u01c3\3\2\2\2.\u01c5"+
		"\3\2\2\2\60\u01d5\3\2\2\2\62\u01d7\3\2\2\2\64\u01e2\3\2\2\2\66\u01f9\3"+
		"\2\2\28\u0203\3\2\2\2:\u0205\3\2\2\2<\u020c\3\2\2\2>\u0214\3\2\2\2@\u0222"+
		"\3\2\2\2B\u0224\3\2\2\2D\u022b\3\2\2\2F\u0253\3\2\2\2H\u0255\3\2\2\2J"+
		"\u025a\3\2\2\2L\u025f\3\2\2\2N\u0265\3\2\2\2P\u027c\3\2\2\2R\u028c\3\2"+
		"\2\2T\u028e\3\2\2\2V\u0295\3\2\2\2X\u029f\3\2\2\2Z\u02a3\3\2\2\2\\\u02ad"+
		"\3\2\2\2^\u02b8\3\2\2\2`\u02cb\3\2\2\2b\u02cd\3\2\2\2d\u02de\3\2\2\2f"+
		"\u0303\3\2\2\2h\u030c\3\2\2\2j\u0321\3\2\2\2l\u0323\3\2\2\2n\u032c\3\2"+
		"\2\2p\u0335\3\2\2\2r\u0342\3\2\2\2t\u0349\3\2\2\2v\u035b\3\2\2\2x\u0364"+
		"\3\2\2\2z\u0374\3\2\2\2|\u0388\3\2\2\2~\u0391\3\2\2\2\u0080\u039c\3\2"+
		"\2\2\u0082\u039e\3\2\2\2\u0084\u03a1\3\2\2\2\u0086\u03bb\3\2\2\2\u0088"+
		"\u03bd\3\2\2\2\u008a\u03d1\3\2\2\2\u008c\u03d3\3\2\2\2\u008e\u03d5\3\2"+
		"\2\2\u0090\u03d7\3\2\2\2\u0092\u03e8\3\2\2\2\u0094\u03ec\3\2\2\2\u0096"+
		"\u03ff\3\2\2\2\u0098\u0415\3\2\2\2\u009a\u0417\3\2\2\2\u009c\u041a\3\2"+
		"\2\2\u009e\u0429\3\2\2\2\u00a0\u042d\3\2\2\2\u00a2\u0432\3\2\2\2\u00a4"+
		"\u0434\3\2\2\2\u00a6\u0442\3\2\2\2\u00a8\u044a\3\2\2\2\u00aa\u0452\3\2"+
		"\2\2\u00ac\u045a\3\2\2\2\u00ae\u0468\3\2\2\2\u00b0\u046e\3\2\2\2\u00b2"+
		"\u047c\3\2\2\2\u00b4\u0485\3\2\2\2\u00b6\u048f\3\2\2\2\u00b8\u0491\3\2"+
		"\2\2\u00ba\u0493\3\2\2\2\u00bc\u0497\3\2\2\2\u00be\u049a\3\2\2\2\u00c0"+
		"\u049e\3\2\2\2\u00c2\u04a0\3\2\2\2\u00c4\u04a5\3\2\2\2\u00c6\u04a9\3\2"+
		"\2\2\u00c8\u04ad\3\2\2\2\u00ca\u04b1\3\2\2\2\u00cc\u04b4\3\2\2\2\u00ce"+
		"\u04b6\3\2\2\2\u00d0\u04bb\3\2\2\2\u00d2\u04bd\3\2\2\2\u00d4\u04c5\3\2"+
		"\2\2\u00d6\u04c9\3\2\2\2\u00d8\u04d1\3\2\2\2\u00da\u04d9\3\2\2\2\u00dc"+
		"\u04dd\3\2\2\2\u00de\u04e9\3\2\2\2\u00e0\u04f3\3\2\2\2\u00e2\u04fe\3\2"+
		"\2\2\u00e4\u0506\3\2\2\2\u00e6\u050a\3\2\2\2\u00e8\u0513\3\2\2\2\u00ea"+
		"\u0523\3\2\2\2\u00ec\u0528\3\2\2\2\u00ee\u052d\3\2\2\2\u00f0\u052f\3\2"+
		"\2\2\u00f2\u0534\3\2\2\2\u00f4\u0536\3\2\2\2\u00f6\u0538\3\2\2\2\u00f8"+
		"\u053b\3\2\2\2\u00fa\u053f\3\2\2\2\u00fc\u054a\3\2\2\2\u00fe\u054e\3\2"+
		"\2\2\u0100\u0556\3\2\2\2\u0102\u055a\3\2\2\2\u0104\u055c\3\2\2\2\u0106"+
		"\u056c\3\2\2\2\u0108\u057a\3\2\2\2\u010a\u057f\3\2\2\2\u010c\u0581\3\2"+
		"\2\2\u010e\u0583\3\2\2\2\u0110\u0588\3\2\2\2\u0112\u058c\3\2\2\2\u0114"+
		"\u058f\3\2\2\2\u0116\u0598\3\2\2\2\u0118\u05a3\3\2\2\2\u011a\u05aa\3\2"+
		"\2\2\u011c\u05ae\3\2\2\2\u011e\u05b0\3\2\2\2\u0120\u05c1\3\2\2\2\u0122"+
		"\u05c6\3\2\2\2\u0124\u05c9\3\2\2\2\u0126\u05cd\3\2\2\2\u0128\u05d1\3\2"+
		"\2\2\u012a\u05d5\3\2\2\2\u012c\u05da\3\2\2\2\u012e\u05ed\3\2\2\2\u0130"+
		"\u05f1\3\2\2\2\u0132\u0137\5\4\3\2\u0133\u0134\7Y\2\2\u0134\u0136\5\4"+
		"\3\2\u0135\u0133\3\2\2\2\u0136\u0139\3\2\2\2\u0137\u0135\3\2\2\2\u0137"+
		"\u0138\3\2\2\2\u0138\3\3\2\2\2\u0139\u0137\3\2\2\2\u013a\u013c\7Q\2\2"+
		"\u013b\u013d\7\63\2\2\u013c\u013b\3\2\2\2\u013c\u013d\3\2\2\2\u013d\5"+
		"\3\2\2\2\u013e\u013f\7\26\2\2\u013f\u0145\5j\66\2\u0140\u0141\7\5\2\2"+
		"\u0141\u0145\5d\63\2\u0142\u0143\t\2\2\2\u0143\u0145\5\f\7\2\u0144\u013e"+
		"\3\2\2\2\u0144\u0140\3\2\2\2\u0144\u0142\3\2\2\2\u0145\7\3\2\2\2\u0146"+
		"\u014b\5\n\6\2\u0147\u0148\7Y\2\2\u0148\u014a\5\n\6\2\u0149\u0147\3\2"+
		"\2\2\u014a\u014d\3\2\2\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c"+
		"\u014f\3\2\2\2\u014d\u014b\3\2\2\2\u014e\u0150\7Y\2\2\u014f\u014e\3\2"+
		"\2\2\u014f\u0150\3\2\2\2\u0150\t\3\2\2\2\u0151\u0156\7Q\2\2\u0152\u0153"+
		"\7Y\2\2\u0153\u0155\7Q\2\2\u0154\u0152\3\2\2\2\u0155\u0158\3\2\2\2\u0156"+
		"\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0159\3\2\2\2\u0158\u0156\3\2"+
		"\2\2\u0159\u015a\5\u00f4{\2\u015a\13\3\2\2\2\u015b\u015c\5t;\2\u015c\r"+
		"\3\2\2\2\u015d\u015e\7\22\2\2\u015e\u015f\7R\2\2\u015f\u0165\5d\63\2\u0160"+
		"\u0163\7Y\2\2\u0161\u0164\7Q\2\2\u0162\u0164\5d\63\2\u0163\u0161\3\2\2"+
		"\2\u0163\u0162\3\2\2\2\u0164\u0166\3\2\2\2\u0165\u0160\3\2\2\2\u0165\u0166"+
		"\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0168\7S\2\2\u0168\17\3\2\2\2\u0169"+
		"\u016a\5d\63\2\u016a\u016b\7\2\2\3\u016b\21\3\2\2\2\u016c\u016d\5j\66"+
		"\2\u016d\u016e\7\2\2\3\u016e\23\3\2\2\2\u016f\u0180\5B\"\2\u0170\u0180"+
		"\5\16\b\2\u0171\u0180\5(\25\2\u0172\u0180\5*\26\2\u0173\u0180\5\"\22\2"+
		"\u0174\u0180\5\34\17\2\u0175\u0180\5\30\r\2\u0176\u0180\5\26\f\2\u0177"+
		"\u0180\5\32\16\2\u0178\u0179\7\20\2\2\u0179\u017a\5\b\5\2\u017a\u017b"+
		"\7[\2\2\u017b\u017c\7[\2\2\u017c\u017d\5\36\20\2\u017d\u017e\5d\63\2\u017e"+
		"\u0180\3\2\2\2\u017f\u016f\3\2\2\2\u017f\u0170\3\2\2\2\u017f\u0171\3\2"+
		"\2\2\u017f\u0172\3\2\2\2\u017f\u0173\3\2\2\2\u017f\u0174\3\2\2\2\u017f"+
		"\u0175\3\2\2\2\u017f\u0176\3\2\2\2\u017f\u0177\3\2\2\2\u017f\u0178\3\2"+
		"\2\2\u0180\25\3\2\2\2\u0181\u0182\7+\2\2\u0182\u0183\7R\2\2\u0183\u0184"+
		"\5d\63\2\u0184\u0185\7S\2\2\u0185\27\3\2\2\2\u0186\u0187\7.\2\2\u0187"+
		"\u0188\7V\2\2\u0188\u0189\5~@\2\u0189\u018a\7W\2\2\u018a\31\3\2\2\2\u018b"+
		"\u018c\7,\2\2\u018c\u018d\7R\2\2\u018d\u018e\5d\63\2\u018e\u018f\7S\2"+
		"\2\u018f\33\3\2\2\2\u0190\u0191\t\3\2\2\u0191\u0192\7R\2\2\u0192\u0193"+
		"\5d\63\2\u0193\u0194\7S\2\2\u0194\35\3\2\2\2\u0195\u0197\5 \21\2\u0196"+
		"\u0195\3\2\2\2\u0197\u019a\3\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199\3\2"+
		"\2\2\u0199\37\3\2\2\2\u019a\u0198\3\2\2\2\u019b\u019c\7T\2\2\u019c\u01a1"+
		"\5d\63\2\u019d\u019e\7Y\2\2\u019e\u01a0\5d\63\2\u019f\u019d\3\2\2\2\u01a0"+
		"\u01a3\3\2\2\2\u01a1\u019f\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a4\3\2"+
		"\2\2\u01a3\u01a1\3\2\2\2\u01a4\u01a5\7U\2\2\u01a5!\3\2\2\2\u01a6\u01ab"+
		"\7\16\2\2\u01a7\u01a8\7V\2\2\u01a8\u01a9\5$\23\2\u01a9\u01aa\7W\2\2\u01aa"+
		"\u01ac\3\2\2\2\u01ab\u01a7\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\3\2"+
		"\2\2\u01ad\u01ae\7R\2\2\u01ae\u01af\5d\63\2\u01af\u01b0\7S\2\2\u01b0#"+
		"\3\2\2\2\u01b1\u01b4\5&\24\2\u01b2\u01b4\7\17\2\2\u01b3\u01b1\3\2\2\2"+
		"\u01b3\u01b2\3\2\2\2\u01b4%\3\2\2\2\u01b5\u01b6\7Q\2\2\u01b6\'\3\2\2\2"+
		"\u01b7\u01b8\7\60\2\2\u01b8\u01b9\7R\2\2\u01b9\u01ba\5d\63\2\u01ba\u01bb"+
		"\7S\2\2\u01bb)\3\2\2\2\u01bc\u01bd\7\61\2\2\u01bd\u01be\7R\2\2\u01be\u01bf"+
		"\5d\63\2\u01bf\u01c0\7S\2\2\u01c0+\3\2\2\2\u01c1\u01c4\5\60\31\2\u01c2"+
		"\u01c4\5.\30\2\u01c3\u01c1\3\2\2\2\u01c3\u01c2\3\2\2\2\u01c4-\3\2\2\2"+
		"\u01c5\u01c6\7\26\2\2\u01c6\u01c7\7V\2\2\u01c7\u01c8\7W\2\2\u01c8\u01c9"+
		"\5\u00f4{\2\u01c9/\3\2\2\2\u01ca\u01cb\t\4\2\2\u01cb\u01cc\7V\2\2\u01cc"+
		"\u01cd\5~@\2\u01cd\u01ce\7W\2\2\u01ce\u01d6\3\2\2\2\u01cf\u01d0\7%\2\2"+
		"\u01d0\u01d1\7V\2\2\u01d1\u01d2\5~@\2\u01d2\u01d3\7W\2\2\u01d3\u01d4\5"+
		"~@\2\u01d4\u01d6\3\2\2\2\u01d5\u01ca\3\2\2\2\u01d5\u01cf\3\2\2\2\u01d6"+
		"\61\3\2\2\2\u01d7\u01d8\7V\2\2\u01d8\u01dd\5\64\33\2\u01d9\u01da\7Y\2"+
		"\2\u01da\u01dc\5\64\33\2\u01db\u01d9\3\2\2\2\u01dc\u01df\3\2\2\2\u01dd"+
		"\u01db\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01e0\3\2\2\2\u01df\u01dd\3\2"+
		"\2\2\u01e0\u01e1\7W\2\2\u01e1\63\3\2\2\2\u01e2\u01e3\5d\63\2\u01e3\u01e4"+
		"\7X\2\2\u01e4\u01e5\5d\63\2\u01e5\65\3\2\2\2\u01e6\u01e7\58\35\2\u01e7"+
		"\u01e8\5\u0098M\2\u01e8\u01ea\3\2\2\2\u01e9\u01e6\3\2\2\2\u01ea\u01ed"+
		"\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ee\3\2\2\2\u01ed"+
		"\u01eb\3\2\2\2\u01ee\u01fa\7\f\2\2\u01ef\u01f2\58\35\2\u01f0\u01f2\7\f"+
		"\2\2\u01f1\u01ef\3\2\2\2\u01f1\u01f0\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3"+
		"\u01f5\5\u0098M\2\u01f4\u01f1\3\2\2\2\u01f5\u01f8\3\2\2\2\u01f6\u01f4"+
		"\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01fa\3\2\2\2\u01f8\u01f6\3\2\2\2\u01f9"+
		"\u01eb\3\2\2\2\u01f9\u01f6\3\2\2\2\u01fa\67\3\2\2\2\u01fb\u01fc\7\7\2"+
		"\2\u01fc\u0204\5@!\2\u01fd\u01fe\7\b\2\2\u01fe\u0204\5@!\2\u01ff\u0200"+
		"\7\t\2\2\u0200\u0204\5@!\2\u0201\u0202\7\13\2\2\u0202\u0204\5p9\2\u0203"+
		"\u01fb\3\2\2\2\u0203\u01fd\3\2\2\2\u0203\u01ff\3\2\2\2\u0203\u0201\3\2"+
		"\2\2\u02049\3\2\2\2\u0205\u0206\5\66\34\2\u0206\u0207\7:\2\2\u0207\u0208"+
		"\7Q\2\2\u0208\u020a\5\u0100\u0081\2\u0209\u020b\5> \2\u020a\u0209\3\2"+
		"\2\2\u020a\u020b\3\2\2\2\u020b;\3\2\2\2\u020c\u020d\5\66\34\2\u020d\u020e"+
		"\7:\2\2\u020e\u020f\5Z.\2\u020f\u0210\7Q\2\2\u0210\u0212\5\u0100\u0081"+
		"\2\u0211\u0213\5> \2\u0212\u0211\3\2\2\2\u0212\u0213\3\2\2\2\u0213=\3"+
		"\2\2\2\u0214\u0219\7T\2\2\u0215\u0216\7\62\2\2\u0216\u0217\5\u00a8U\2"+
		"\u0217\u0218\5\u0098M\2\u0218\u021a\3\2\2\2\u0219\u0215\3\2\2\2\u0219"+
		"\u021a\3\2\2\2\u021a\u021c\3\2\2\2\u021b\u021d\5\u00b4[\2\u021c\u021b"+
		"\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u021f\7U\2\2\u021f"+
		"?\3\2\2\2\u0220\u0223\3\2\2\2\u0221\u0223\5d\63\2\u0222\u0220\3\2\2\2"+
		"\u0222\u0221\3\2\2\2\u0223A\3\2\2\2\u0224\u0225\t\3\2\2\u0225\u0226\7"+
		"V\2\2\u0226\u0227\5d\63\2\u0227\u0228\7\64\2\2\u0228\u0229\5d\63\2\u0229"+
		"\u022a\7W\2\2\u022aC\3\2\2\2\u022b\u022c\5\u009aN\2\u022c\u0232\5\u0098"+
		"M\2\u022d\u022e\5\u009cO\2\u022e\u022f\5\u0098M\2\u022f\u0231\3\2\2\2"+
		"\u0230\u022d\3\2\2\2\u0231\u0234\3\2\2\2\u0232\u0230\3\2\2\2\u0232\u0233"+
		"\3\2\2\2\u0233\u023f\3\2\2\2\u0234\u0232\3\2\2\2\u0235\u023a\5:\36\2\u0236"+
		"\u023a\5<\37\2\u0237\u023a\5\u00a2R\2\u0238\u023a\5F$\2\u0239\u0235\3"+
		"\2\2\2\u0239\u0236\3\2\2\2\u0239\u0237\3\2\2\2\u0239\u0238\3\2\2\2\u023a"+
		"\u023b\3\2\2\2\u023b\u023c\5\u0098M\2\u023c\u023e\3\2\2\2\u023d\u0239"+
		"\3\2\2\2\u023e\u0241\3\2\2\2\u023f\u023d\3\2\2\2\u023f\u0240\3\2\2\2\u0240"+
		"\u0242\3\2\2\2\u0241\u023f\3\2\2\2\u0242\u0243\7\2\2\3\u0243E\3\2\2\2"+
		"\u0244\u0254\5H%\2\u0245\u0254\5L\'\2\u0246\u0254\5N(\2\u0247\u024b\7"+
		"\26\2\2\u0248\u0249\7\26\2\2\u0249\u024b\5\u0098M\2\u024a\u0247\3\2\2"+
		"\2\u024a\u0248\3\2\2\2\u024b\u0251\3\2\2\2\u024c\u0252\5<\37\2\u024d\u0252"+
		"\5:\36\2\u024e\u0252\5\u00a4S\2\u024f\u0252\5\u00acW\2\u0250\u0252\5\u00b0"+
		"Y\2\u0251\u024c\3\2\2\2\u0251\u024d\3\2\2\2\u0251\u024e\3\2\2\2\u0251"+
		"\u024f\3\2\2\2\u0251\u0250\3\2\2\2\u0252\u0254\3\2\2\2\u0253\u0244\3\2"+
		"\2\2\u0253\u0245\3\2\2\2\u0253\u0246\3\2\2\2\u0253\u024a\3\2\2\2\u0254"+
		"G\3\2\2\2\u0255\u0256\7/\2\2\u0256\u0257\7Q\2\2\u0257\u0258\5\u0104\u0083"+
		"\2\u0258\u0259\5J&\2\u0259I\3\2\2\2\u025a\u025b\7T\2\2\u025b\u025c\5d"+
		"\63\2\u025c\u025d\5\u0098M\2\u025d\u025e\7U\2\2\u025eK\3\2\2\2\u025f\u0260"+
		"\7/\2\2\u0260\u0261\5Z.\2\u0261\u0262\7Q\2\2\u0262\u0263\5\u0104\u0083"+
		"\2\u0263\u0264\5J&\2\u0264M\3\2\2\2\u0265\u0266\5~@\2\u0266\u0267\7\r"+
		"\2\2\u0267\u0268\5~@\2\u0268\u026e\7T\2\2\u0269\u026a\5T+\2\u026a\u026b"+
		"\5\u0098M\2\u026b\u026d\3\2\2\2\u026c\u0269\3\2\2\2\u026d\u0270\3\2\2"+
		"\2\u026e\u026c\3\2\2\2\u026e\u026f\3\2\2\2\u026f\u0276\3\2\2\2\u0270\u026e"+
		"\3\2\2\2\u0271\u0272\5P)\2\u0272\u0273\5\u0098M\2\u0273\u0275\3\2\2\2"+
		"\u0274\u0271\3\2\2\2\u0275\u0278\3\2\2\2\u0276\u0274\3\2\2\2\u0276\u0277"+
		"\3\2\2\2\u0277\u0279\3\2\2\2\u0278\u0276\3\2\2\2\u0279\u027a\7U\2\2\u027a"+
		"O\3\2\2\2\u027b\u027d\7\f\2\2\u027c\u027b\3\2\2\2\u027c\u027d\3\2\2\2"+
		"\u027d\u027e\3\2\2\2\u027e\u027f\5\\/\2\u027f\u0280\7Q\2\2\u0280\u0282"+
		"\5\u0100\u0081\2\u0281\u0283\5\u00b2Z\2\u0282\u0281\3\2\2\2\u0282\u0283"+
		"\3\2\2\2\u0283Q\3\2\2\2\u0284\u0285\5t;\2\u0285\u0286\7\\\2\2\u0286\u0287"+
		"\7Q\2\2\u0287\u028d\3\2\2\2\u0288\u0289\5~@\2\u0289\u028a\7\\\2\2\u028a"+
		"\u028b\7Q\2\2\u028b\u028d\3\2\2\2\u028c\u0284\3\2\2\2\u028c\u0288\3\2"+
		"\2\2\u028dS\3\2\2\2\u028e\u028f\7/\2\2\u028f\u0290\7Q\2\2\u0290\u0293"+
		"\7_\2\2\u0291\u0294\5R*\2\u0292\u0294\5\u010e\u0088\2\u0293\u0291\3\2"+
		"\2\2\u0293\u0292\3\2\2\2\u0294U\3\2\2\2\u0295\u029d\5\2\2\2\u0296\u0299"+
		"\5~@\2\u0297\u0298\7X\2\2\u0298\u029a\5\u00aaV\2\u0299\u0297\3\2\2\2\u0299"+
		"\u029a\3\2\2\2\u029a\u029e\3\2\2\2\u029b\u029c\7X\2\2\u029c\u029e\5\u00aa"+
		"V\2\u029d\u0296\3\2\2\2\u029d\u029b\3\2\2\2\u029eW\3\2\2\2\u029f\u02a0"+
		"\5\2\2\2\u02a0\u02a1\7_\2\2\u02a1\u02a2\5\u00aaV\2\u02a2Y\3\2\2\2\u02a3"+
		"\u02a5\7R\2\2\u02a4\u02a6\5\4\3\2\u02a5\u02a4\3\2\2\2\u02a5\u02a6\3\2"+
		"\2\2\u02a6\u02a8\3\2\2\2\u02a7\u02a9\7s\2\2\u02a8\u02a7\3\2\2\2\u02a8"+
		"\u02a9\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02ab\7Q\2\2\u02ab\u02ac\7S\2"+
		"\2\u02ac[\3\2\2\2\u02ad\u02af\7R\2\2\u02ae\u02b0\7Q\2\2\u02af\u02ae\3"+
		"\2\2\2\u02af\u02b0\3\2\2\2\u02b0\u02b2\3\2\2\2\u02b1\u02b3\7s\2\2\u02b2"+
		"\u02b1\3\2\2\2\u02b2\u02b3\3\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02b5\5\u00ee"+
		"x\2\u02b5\u02b6\7S\2\2\u02b6]\3\2\2\2\u02b7\u02b9\7\26\2\2\u02b8\u02b7"+
		"\3\2\2\2\u02b8\u02b9\3\2\2\2\u02b9\u02bb\3\2\2\2\u02ba\u02bc\5\u00a8U"+
		"\2\u02bb\u02ba\3\2\2\2\u02bb\u02bc\3\2\2\2\u02bc\u02be\3\2\2\2\u02bd\u02bf"+
		"\7`\2\2\u02be\u02bd\3\2\2\2\u02be\u02bf\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0"+
		"\u02c1\5~@\2\u02c1_\3\2\2\2\u02c2\u02cc\5t;\2\u02c3\u02c4\t\5\2\2\u02c4"+
		"\u02c5\7R\2\2\u02c5\u02c6\5d\63\2\u02c6\u02c7\7S\2\2\u02c7\u02cc\3\2\2"+
		"\2\u02c8\u02cc\5b\62\2\u02c9\u02ca\t\6\2\2\u02ca\u02cc\5d\63\2\u02cb\u02c2"+
		"\3\2\2\2\u02cb\u02c3\3\2\2\2\u02cb\u02c8\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cc"+
		"a\3\2\2\2\u02cd\u02ce\7\25\2\2\u02ce\u02cf\5\f\7\2\u02cf\u02d0\7\27\2"+
		"\2\u02d0\u02d1\5d\63\2\u02d1c\3\2\2\2\u02d2\u02d3\b\63\1\2\u02d3\u02df"+
		"\5t;\2\u02d4\u02d5\t\5\2\2\u02d5\u02d6\7R\2\2\u02d6\u02d7\5d\63\2\u02d7"+
		"\u02d8\7S\2\2\u02d8\u02df\3\2\2\2\u02d9\u02df\5b\62\2\u02da\u02df\5h\65"+
		"\2\u02db\u02df\5f\64\2\u02dc\u02dd\t\6\2\2\u02dd\u02df\5d\63\f\u02de\u02d2"+
		"\3\2\2\2\u02de\u02d4\3\2\2\2\u02de\u02d9\3\2\2\2\u02de\u02da\3\2\2\2\u02de"+
		"\u02db\3\2\2\2\u02de\u02dc\3\2\2\2\u02df\u0300\3\2\2\2\u02e0\u02e1\f\13"+
		"\2\2\u02e1\u02e2\t\7\2\2\u02e2\u02ff\5d\63\f\u02e3\u02e4\f\n\2\2\u02e4"+
		"\u02e5\t\b\2\2\u02e5\u02ff\5d\63\13\u02e6\u02e7\f\t\2\2\u02e7\u02e8\t"+
		"\t\2\2\u02e8\u02ff\5d\63\n\u02e9\u02ea\f\b\2\2\u02ea\u02eb\t\n\2\2\u02eb"+
		"\u02ff\5d\63\t\u02ec\u02ed\f\7\2\2\u02ed\u02ee\t\13\2\2\u02ee\u02ff\5"+
		"d\63\b\u02ef\u02f0\f\6\2\2\u02f0\u02f1\7b\2\2\u02f1\u02ff\5d\63\7\u02f2"+
		"\u02f3\f\5\2\2\u02f3\u02f4\7a\2\2\u02f4\u02ff\5d\63\6\u02f5\u02f6\f\4"+
		"\2\2\u02f6\u02f7\7\35\2\2\u02f7\u02ff\5d\63\4\u02f8\u02f9\f\3\2\2\u02f9"+
		"\u02fa\7\36\2\2\u02fa\u02fb\5d\63\2\u02fb\u02fc\7[\2\2\u02fc\u02fd\5d"+
		"\63\3\u02fd\u02ff\3\2\2\2\u02fe\u02e0\3\2\2\2\u02fe\u02e3\3\2\2\2\u02fe"+
		"\u02e6\3\2\2\2\u02fe\u02e9\3\2\2\2\u02fe\u02ec\3\2\2\2\u02fe\u02ef\3\2"+
		"\2\2\u02fe\u02f2\3\2\2\2\u02fe\u02f5\3\2\2\2\u02fe\u02f8\3\2\2\2\u02ff"+
		"\u0302\3\2\2\2\u0300\u02fe\3\2\2\2\u0300\u0301\3\2\2\2\u0301e\3\2\2\2"+
		"\u0302\u0300\3\2\2\2\u0303\u0304\7)\2\2\u0304\u0305\7R\2\2\u0305\u0308"+
		"\5~@\2\u0306\u0307\7Y\2\2\u0307\u0309\5\u00aaV\2\u0308\u0306\3\2\2\2\u0308"+
		"\u0309\3\2\2\2\u0309\u030a\3\2\2\2\u030a\u030b\7S\2\2\u030bg\3\2\2\2\u030c"+
		"\u030d\7(\2\2\u030d\u030e\7R\2\2\u030e\u030f\5~@\2\u030f\u0310\7S\2\2"+
		"\u0310i\3\2\2\2\u0311\u0322\5\6\4\2\u0312\u0322\5\u00a2R\2\u0313\u0322"+
		"\5\u00c2b\2\u0314\u0322\5\u00b6\\\2\u0315\u0322\5\u00ecw\2\u0316\u0322"+
		"\5\u00c4c\2\u0317\u0322\5\u00c6d\2\u0318\u0322\5\u00c8e\2\u0319\u0322"+
		"\5\u00caf\2\u031a\u0322\5\u00ccg\2\u031b\u0322\5\u00b2Z\2\u031c\u0322"+
		"\5\u0090I\2\u031d\u0322\5\u00d0i\2\u031e\u0322\5\u00dep\2\u031f\u0322"+
		"\5l\67\2\u0320\u0322\5\u00ceh\2\u0321\u0311\3\2\2\2\u0321\u0312\3\2\2"+
		"\2\u0321\u0313\3\2\2\2\u0321\u0314\3\2\2\2\u0321\u0315\3\2\2\2\u0321\u0316"+
		"\3\2\2\2\u0321\u0317\3\2\2\2\u0321\u0318\3\2\2\2\u0321\u0319\3\2\2\2\u0321"+
		"\u031a\3\2\2\2\u0321\u031b\3\2\2\2\u0321\u031c\3\2\2\2\u0321\u031d\3\2"+
		"\2\2\u0321\u031e\3\2\2\2\u0321\u031f\3\2\2\2\u0321\u0320\3\2\2\2\u0322"+
		"k\3\2\2\2\u0323\u0324\5n8\2\u0324\u0325\5\u00e6t\2\u0325m\3\2\2\2\u0326"+
		"\u0327\7\n\2\2\u0327\u0328\5d\63\2\u0328\u0329\5\u0098M\2\u0329\u032b"+
		"\3\2\2\2\u032a\u0326\3\2\2\2\u032b\u032e\3\2\2\2\u032c\u032a\3\2\2\2\u032c"+
		"\u032d\3\2\2\2\u032d\u0333\3\2\2\2\u032e\u032c\3\2\2\2\u032f\u0330\7\13"+
		"\2\2\u0330\u0331\5p9\2\u0331\u0332\5\u0098M\2\u0332\u0334\3\2\2\2\u0333"+
		"\u032f\3\2\2\2\u0333\u0334\3\2\2\2\u0334o\3\2\2\2\u0335\u0338\5\u00aa"+
		"V\2\u0336\u0337\7I\2\2\u0337\u0339\5d\63\2\u0338\u0336\3\2\2\2\u0338\u0339"+
		"\3\2\2\2\u0339q\3\2\2\2\u033a\u0343\7\3\2\2\u033b\u0343\7\4\2\2\u033c"+
		"\u0343\7P\2\2\u033d\u0343\5\u010c\u0087\2\u033e\u0343\5\u0122\u0092\2"+
		"\u033f\u0343\7z\2\2\u0340\u0343\7}\2\2\u0341\u0343\7~\2\2\u0342\u033a"+
		"\3\2\2\2\u0342\u033b\3\2\2\2\u0342\u033c\3\2\2\2\u0342\u033d\3\2\2\2\u0342"+
		"\u033e\3\2\2\2\u0342\u033f\3\2\2\2\u0342\u0340\3\2\2\2\u0342\u0341\3\2"+
		"\2\2\u0343s\3\2\2\2\u0344\u0345\b;\1\2\u0345\u034a\5\u0108\u0085\2\u0346"+
		"\u034a\5\u0106\u0084\2\u0347\u034a\5\u012e\u0098\2\u0348\u034a\5\24\13"+
		"\2\u0349\u0344\3\2\2\2\u0349\u0346\3\2\2\2\u0349\u0347\3\2\2\2\u0349\u0348"+
		"\3\2\2\2\u034a\u0358\3\2\2\2\u034b\u0354\f\3\2\2\u034c\u034d\7\\\2\2\u034d"+
		"\u0355\7Q\2\2\u034e\u0355\5\u0128\u0095\2\u034f\u0355\5\u0088E\2\u0350"+
		"\u0355\5\62\32\2\u0351\u0355\5\u012a\u0096\2\u0352\u0355\5\u012c\u0097"+
		"\2\u0353\u0355\5v<\2\u0354\u034c\3\2\2\2\u0354\u034e\3\2\2\2\u0354\u034f"+
		"\3\2\2\2\u0354\u0350\3\2\2\2\u0354\u0351\3\2\2\2\u0354\u0352\3\2\2\2\u0354"+
		"\u0353\3\2\2\2\u0355\u0357\3\2\2\2\u0356\u034b\3\2\2\2\u0357\u035a\3\2"+
		"\2\2\u0358\u0356\3\2\2\2\u0358\u0359\3\2\2\2\u0359u\3\2\2\2\u035a\u0358"+
		"\3\2\2\2\u035b\u035d\7\37\2\2\u035c\u035e\5\u00aaV\2\u035d\u035c\3\2\2"+
		"\2\u035d\u035e\3\2\2\2\u035e\u0360\3\2\2\2\u035f\u0361\7Y\2\2\u0360\u035f"+
		"\3\2\2\2\u0360\u0361\3\2\2\2\u0361\u0362\3\2\2\2\u0362\u0363\7 \2\2\u0363"+
		"w\3\2\2\2\u0364\u0365\7;\2\2\u0365\u036f\7T\2\2\u0366\u036a\5|?\2\u0367"+
		"\u036a\5\u00eex\2\u0368\u036a\5z>\2\u0369\u0366\3\2\2\2\u0369\u0367\3"+
		"\2\2\2\u0369\u0368\3\2\2\2\u036a\u036b\3\2\2\2\u036b\u036c\5\u0098M\2"+
		"\u036c\u036e\3\2\2\2\u036d\u0369\3\2\2\2\u036e\u0371\3\2\2\2\u036f\u036d"+
		"\3\2\2\2\u036f\u0370\3\2\2\2\u0370\u0372\3\2\2\2\u0371\u036f\3\2\2\2\u0372"+
		"\u0373\7U\2\2\u0373y\3\2\2\2\u0374\u0375\7/\2\2\u0375\u0376\7Q\2\2\u0376"+
		"\u0377\5\u0104\u0083\2\u0377{\3\2\2\2\u0378\u037a\6?\f\2\u0379\u037b\7"+
		"\26\2\2\u037a\u0379\3\2\2\2\u037a\u037b\3\2\2\2\u037b\u037c\3\2\2\2\u037c"+
		"\u037d\5\66\34\2\u037d\u037e\7Q\2\2\u037e\u037f\5\u0104\u0083\2\u037f"+
		"\u0380\5\u0102\u0082\2\u0380\u0389\3\2\2\2\u0381\u0383\7\26\2\2\u0382"+
		"\u0381\3\2\2\2\u0382\u0383\3\2\2\2\u0383\u0384\3\2\2\2\u0384\u0385\5\66"+
		"\34\2\u0385\u0386\7Q\2\2\u0386\u0387\5\u0104\u0083\2\u0387\u0389\3\2\2"+
		"\2\u0388\u0378\3\2\2\2\u0388\u0382\3\2\2\2\u0389}\3\2\2\2\u038a\u0392"+
		"\5\u00eex\2\u038b\u0392\5\u0080A\2\u038c\u0392\5,\27\2\u038d\u038e\7R"+
		"\2\2\u038e\u038f\5~@\2\u038f\u0390\7S\2\2\u0390\u0392\3\2\2\2\u0391\u038a"+
		"\3\2\2\2\u0391\u038b\3\2\2\2\u0391\u038c\3\2\2\2\u0391\u038d\3\2\2\2\u0392"+
		"\177\3\2\2\2\u0393\u039d\5\u00f0y\2\u0394\u039d\5\u011e\u0090\2\u0395"+
		"\u039d\5\u00f6|\2\u0396\u039d\5\u00fe\u0080\2\u0397\u039d\5x=\2\u0398"+
		"\u039d\5\u00f8}\2\u0399\u039d\5\u00fa~\2\u039a\u039d\5\u00fc\177\2\u039b"+
		"\u039d\5\u0082B\2\u039c\u0393\3\2\2\2\u039c\u0394\3\2\2\2\u039c\u0395"+
		"\3\2\2\2\u039c\u0396\3\2\2\2\u039c\u0397\3\2\2\2\u039c\u0398\3\2\2\2\u039c"+
		"\u0399\3\2\2\2\u039c\u039a\3\2\2\2\u039c\u039b\3\2\2\2\u039d\u0081\3\2"+
		"\2\2\u039e\u039f\7/\2\2\u039f\u03a0\5\u0084C\2\u03a0\u0083\3\2\2\2\u03a1"+
		"\u03ad\7R\2\2\u03a2\u03a7\5~@\2\u03a3\u03a4\7Y\2\2\u03a4\u03a6\5~@\2\u03a5"+
		"\u03a3\3\2\2\2\u03a6\u03a9\3\2\2\2\u03a7\u03a5\3\2\2\2\u03a7\u03a8\3\2"+
		"\2\2\u03a8\u03ab\3\2\2\2\u03a9\u03a7\3\2\2\2\u03aa\u03ac\7Y\2\2\u03ab"+
		"\u03aa\3\2\2\2\u03ab\u03ac\3\2\2\2\u03ac\u03ae\3\2\2\2\u03ad\u03a2\3\2"+
		"\2\2\u03ad\u03ae\3\2\2\2\u03ae\u03af\3\2\2\2\u03af\u03b0\7S\2\2\u03b0"+
		"\u0085\3\2\2\2\u03b1\u03bc\5\u011e\u0090\2\u03b2\u03bc\5\u00f0y\2\u03b3"+
		"\u03b4\7V\2\2\u03b4\u03b5\7`\2\2\u03b5\u03b6\7W\2\2\u03b6\u03bc\5\u00f4"+
		"{\2\u03b7\u03bc\5\u00f8}\2\u03b8\u03bc\5\u00fa~\2\u03b9\u03bc\5,\27\2"+
		"\u03ba\u03bc\5\u00eex\2\u03bb\u03b1\3\2\2\2\u03bb\u03b2\3\2\2\2\u03bb"+
		"\u03b3\3\2\2\2\u03bb\u03b7\3\2\2\2\u03bb\u03b8\3\2\2\2\u03bb\u03b9\3\2"+
		"\2\2\u03bb\u03ba\3\2\2\2\u03bc\u0087\3\2\2\2\u03bd\u03cd\7V\2\2\u03be"+
		"\u03c0\5\u008aF\2\u03bf\u03be\3\2\2\2\u03bf\u03c0\3\2\2\2\u03c0\u03c1"+
		"\3\2\2\2\u03c1\u03c3\7[\2\2\u03c2\u03c4\5\u008cG\2\u03c3\u03c2\3\2\2\2"+
		"\u03c3\u03c4\3\2\2\2\u03c4\u03ce\3\2\2\2\u03c5\u03c7\5\u008aF\2\u03c6"+
		"\u03c5\3\2\2\2\u03c6\u03c7\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8\u03c9\7["+
		"\2\2\u03c9\u03ca\5\u008cG\2\u03ca\u03cb\7[\2\2\u03cb\u03cc\5\u008eH\2"+
		"\u03cc\u03ce\3\2\2\2\u03cd\u03bf\3\2\2\2\u03cd\u03c6\3\2\2\2\u03ce\u03cf"+
		"\3\2\2\2\u03cf\u03d0\7W\2\2\u03d0\u0089\3\2\2\2\u03d1\u03d2\5d\63\2\u03d2"+
		"\u008b\3\2\2\2\u03d3\u03d4\5d\63\2\u03d4\u008d\3\2\2\2\u03d5\u03d6\5d"+
		"\63\2\u03d6\u008f\3\2\2\2\u03d7\u03dc\7I\2\2\u03d8\u03da\5\u00b6\\\2\u03d9"+
		"\u03d8\3\2\2\2\u03d9\u03da\3\2\2\2\u03da\u03db\3\2\2\2\u03db\u03dd\7Z"+
		"\2\2\u03dc\u03d9\3\2\2\2\u03dc\u03dd\3\2\2\2\u03dd\u03de\3\2\2\2\u03de"+
		"\u03df\5d\63\2\u03df\u03e5\5\u00b2Z\2\u03e0\u03e3\7C\2\2\u03e1\u03e4\5"+
		"\u0090I\2\u03e2\u03e4\5\u00b2Z\2\u03e3\u03e1\3\2\2\2\u03e3\u03e2\3\2\2"+
		"\2\u03e4\u03e6\3\2\2\2\u03e5\u03e0\3\2\2\2\u03e5\u03e6\3\2\2\2\u03e6\u0091"+
		"\3\2\2\2\u03e7\u03e9\t\f\2\2\u03e8\u03e7\3\2\2\2\u03e8\u03e9\3\2\2\2\u03e9"+
		"\u03ea\3\2\2\2\u03ea\u03eb\7X\2\2\u03eb\u0093\3\2\2\2\u03ec\u03f1\7F\2"+
		"\2\u03ed\u03ef\5\u00b6\\\2\u03ee\u03ed\3\2\2\2\u03ee\u03ef\3\2\2\2\u03ef"+
		"\u03f0\3\2\2\2\u03f0\u03f2\7Z\2\2\u03f1\u03ee\3\2\2\2\u03f1\u03f2\3\2"+
		"\2\2\u03f2\u03f4\3\2\2\2\u03f3\u03f5\5d\63\2\u03f4\u03f3\3\2\2\2\u03f4"+
		"\u03f5\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6\u03fa\7T\2\2\u03f7\u03f9\5\u00d2"+
		"j\2\u03f8\u03f7\3\2\2\2\u03f9\u03fc\3\2\2\2\u03fa\u03f8\3\2\2\2\u03fa"+
		"\u03fb\3\2\2\2\u03fb\u03fd\3\2\2\2\u03fc\u03fa\3\2\2\2\u03fd\u03fe\7U"+
		"\2\2\u03fe\u0095\3\2\2\2\u03ff\u0404\7F\2\2\u0400\u0402\5\u00b6\\\2\u0401"+
		"\u0400\3\2\2\2\u0401\u0402\3\2\2\2\u0402\u0403\3\2\2\2\u0403\u0405\7Z"+
		"\2\2\u0404\u0401\3\2\2\2\u0404\u0405\3\2\2\2\u0405\u0406\3\2\2\2\u0406"+
		"\u0407\5\u00d6l\2\u0407\u040b\7T\2\2\u0408\u040a\5\u00d8m\2\u0409\u0408"+
		"\3\2\2\2\u040a\u040d\3\2\2\2\u040b\u0409\3\2\2\2\u040b\u040c\3\2\2\2\u040c"+
		"\u040e\3\2\2\2\u040d\u040b\3\2\2\2\u040e\u040f\7U\2\2\u040f\u0097\3\2"+
		"\2\2\u0410\u0416\7Z\2\2\u0411\u0416\7\2\2\3\u0412\u0416\6M\r\2\u0413\u0416"+
		"\6M\16\2\u0414\u0416\6M\17\2\u0415\u0410\3\2\2\2\u0415\u0411\3\2\2\2\u0415"+
		"\u0412\3\2\2\2\u0415\u0413\3\2\2\2\u0415\u0414\3\2\2\2\u0416\u0099\3\2"+
		"\2\2\u0417\u0418\7E\2\2\u0418\u0419\7Q\2\2\u0419\u009b\3\2\2\2\u041a\u0426"+
		"\7M\2\2\u041b\u0427\5\u009eP\2\u041c\u0422\7R\2\2\u041d\u041e\5\u009e"+
		"P\2\u041e\u041f\5\u0098M\2\u041f\u0421\3\2\2\2\u0420\u041d\3\2\2\2\u0421"+
		"\u0424\3\2\2\2\u0422\u0420\3\2\2\2\u0422\u0423\3\2\2\2\u0423\u0425\3\2"+
		"\2\2\u0424\u0422\3\2\2\2\u0425\u0427\7S\2\2\u0426\u041b\3\2\2\2\u0426"+
		"\u041c\3\2\2\2\u0427\u009d\3\2\2\2\u0428\u042a\t\r\2\2\u0429\u0428\3\2"+
		"\2\2\u0429\u042a\3\2\2\2\u042a\u042b\3\2\2\2\u042b\u042c\5\u00a0Q\2\u042c"+
		"\u009f\3\2\2\2\u042d\u042e\5\u0122\u0092\2\u042e\u00a1\3\2\2\2\u042f\u0433"+
		"\5\u00a4S\2\u0430\u0433\5\u00acW\2\u0431\u0433\5\u00b0Y\2\u0432\u042f"+
		"\3\2\2\2\u0432\u0430\3\2\2\2\u0432\u0431\3\2\2\2\u0433\u00a3\3\2\2\2\u0434"+
		"\u0440\7G\2\2\u0435\u0441\5\u00a6T\2\u0436\u043c\7R\2\2\u0437\u0438\5"+
		"\u00a6T\2\u0438\u0439\5\u0098M\2\u0439\u043b\3\2\2\2\u043a\u0437\3\2\2"+
		"\2\u043b\u043e\3\2\2\2\u043c\u043a\3\2\2\2\u043c\u043d\3\2\2\2\u043d\u043f"+
		"\3\2\2\2\u043e\u043c\3\2\2\2\u043f\u0441\7S\2\2\u0440\u0435\3\2\2\2\u0440"+
		"\u0436\3\2\2\2\u0441\u00a5\3\2\2\2\u0442\u0448\5\u00a8U\2\u0443\u0445"+
		"\5~@\2\u0444\u0443\3\2\2\2\u0444\u0445\3\2\2\2\u0445\u0446\3\2\2\2\u0446"+
		"\u0447\7X\2\2\u0447\u0449\5\u00aaV\2\u0448\u0444\3\2\2\2\u0448\u0449\3"+
		"\2\2\2\u0449\u00a7\3\2\2\2\u044a\u044f\7Q\2\2\u044b\u044c\7Y\2\2\u044c"+
		"\u044e\7Q\2\2\u044d\u044b\3\2\2\2\u044e\u0451\3\2\2\2\u044f\u044d\3\2"+
		"\2\2\u044f\u0450\3\2\2\2\u0450\u00a9\3\2\2\2\u0451\u044f\3\2\2\2\u0452"+
		"\u0457\5d\63\2\u0453\u0454\7Y\2\2\u0454\u0456\5d\63\2\u0455\u0453\3\2"+
		"\2\2\u0456\u0459\3\2\2\2\u0457\u0455\3\2\2\2\u0457\u0458\3\2\2\2\u0458"+
		"\u00ab\3\2\2\2\u0459\u0457\3\2\2\2\u045a\u0466\7J\2\2\u045b\u0467\5\u00ae"+
		"X\2\u045c\u0462\7R\2\2\u045d\u045e\5\u00aeX\2\u045e\u045f\5\u0098M\2\u045f"+
		"\u0461\3\2\2\2\u0460\u045d\3\2\2\2\u0461\u0464\3\2\2\2\u0462\u0460\3\2"+
		"\2\2\u0462\u0463\3\2\2\2\u0463\u0465\3\2\2\2\u0464\u0462\3\2\2\2\u0465"+
		"\u0467\7S\2\2\u0466\u045b\3\2\2\2\u0466\u045c\3\2\2\2\u0467\u00ad\3\2"+
		"\2\2\u0468\u046a\7Q\2\2\u0469\u046b\7X\2\2\u046a\u0469\3\2\2\2\u046a\u046b"+
		"\3\2\2\2\u046b\u046c\3\2\2\2\u046c\u046d\5~@\2\u046d\u00af\3\2\2\2\u046e"+
		"\u047a\7O\2\2\u046f\u047b\5V,\2\u0470\u0476\7R\2\2\u0471\u0472\5V,\2\u0472"+
		"\u0473\5\u0098M\2\u0473\u0475\3\2\2\2\u0474\u0471\3\2\2\2\u0475\u0478"+
		"\3\2\2\2\u0476\u0474\3\2\2\2\u0476\u0477\3\2\2\2\u0477\u0479\3\2\2\2\u0478"+
		"\u0476\3\2\2\2\u0479\u047b\7S\2\2\u047a\u046f\3\2\2\2\u047a\u0470\3\2"+
		"\2\2\u047b\u00b1\3\2\2\2\u047c\u047e\7T\2\2\u047d\u047f\5\u00b4[\2\u047e"+
		"\u047d\3\2\2\2\u047e\u047f\3\2\2\2\u047f\u0480\3\2\2\2\u0480\u0481\7U"+
		"\2\2\u0481\u00b3\3\2\2\2\u0482\u0483\5j\66\2\u0483\u0484\5\u0098M\2\u0484"+
		"\u0486\3\2\2\2\u0485\u0482\3\2\2\2\u0486\u0487\3\2\2\2\u0487\u0485\3\2"+
		"\2\2\u0487\u0488\3\2\2\2\u0488\u00b5\3\2\2\2\u0489\u0490\5\u00ba^\2\u048a"+
		"\u0490\5\u00bc_\2\u048b\u0490\5\u00be`\2\u048c\u0490\5\u00b8]\2\u048d"+
		"\u0490\5X-\2\u048e\u0490\5\u00c0a\2\u048f\u0489\3\2\2\2\u048f\u048a\3"+
		"\2\2\2\u048f\u048b\3\2\2\2\u048f\u048c\3\2\2\2\u048f\u048d\3\2\2\2\u048f"+
		"\u048e\3\2\2\2\u0490\u00b7\3\2\2\2\u0491\u0492\5d\63\2\u0492\u00b9\3\2"+
		"\2\2\u0493\u0494\5d\63\2\u0494\u0495\7u\2\2\u0495\u0496\5d\63\2\u0496"+
		"\u00bb\3\2\2\2\u0497\u0498\5d\63\2\u0498\u0499\t\16\2\2\u0499\u00bd\3"+
		"\2\2\2\u049a\u049b\5\u00aaV\2\u049b\u049c\5\u0092J\2\u049c\u049d\5\u00aa"+
		"V\2\u049d\u00bf\3\2\2\2\u049e\u049f\7Z\2\2\u049f\u00c1\3\2\2\2\u04a0\u04a1"+
		"\7Q\2\2\u04a1\u04a3\7[\2\2\u04a2\u04a4\5j\66\2\u04a3\u04a2\3\2\2\2\u04a3"+
		"\u04a4\3\2\2\2\u04a4\u00c3\3\2\2\2\u04a5\u04a7\7N\2\2\u04a6\u04a8\5\u00aa"+
		"V\2\u04a7\u04a6\3\2\2\2\u04a7\u04a8\3\2\2\2\u04a8\u00c5\3\2\2\2\u04a9"+
		"\u04ab\78\2\2\u04aa\u04ac\7Q\2\2\u04ab\u04aa\3\2\2\2\u04ab\u04ac\3\2\2"+
		"\2\u04ac\u00c7\3\2\2\2\u04ad\u04af\7K\2\2\u04ae\u04b0\7Q\2\2\u04af\u04ae"+
		"\3\2\2\2\u04af\u04b0\3\2\2\2\u04b0\u00c9\3\2\2\2\u04b1\u04b2\7D\2\2\u04b2"+
		"\u04b3\7Q\2\2\u04b3\u00cb\3\2\2\2\u04b4\u04b5\7H\2\2\u04b5\u00cd\3\2\2"+
		"\2\u04b6\u04b7\7>\2\2\u04b7\u04b8\5d\63\2\u04b8\u00cf\3\2\2\2\u04b9\u04bc"+
		"\5\u0094K\2\u04ba\u04bc\5\u0096L\2\u04bb\u04b9\3\2\2\2\u04bb\u04ba\3\2"+
		"\2\2\u04bc\u00d1\3\2\2\2\u04bd\u04be\5\u00d4k\2\u04be\u04c0\7[\2\2\u04bf"+
		"\u04c1\5\u00b4[\2\u04c0\u04bf\3\2\2\2\u04c0\u04c1\3\2\2\2\u04c1\u00d3"+
		"\3\2\2\2\u04c2\u04c3\7=\2\2\u04c3\u04c6\5\u00aaV\2\u04c4\u04c6\79\2\2"+
		"\u04c5\u04c2\3\2\2\2\u04c5\u04c4\3\2\2\2\u04c6\u00d5\3\2\2\2\u04c7\u04c8"+
		"\7Q\2\2\u04c8\u04ca\7_\2\2\u04c9\u04c7\3\2\2\2\u04c9\u04ca\3\2\2\2\u04ca"+
		"\u04cb\3\2\2\2\u04cb\u04cc\5t;\2\u04cc\u04cd\7\\\2\2\u04cd\u04ce\7R\2"+
		"\2\u04ce\u04cf\7J\2\2\u04cf\u04d0\7S\2\2\u04d0\u00d7\3\2\2\2\u04d1\u04d2"+
		"\5\u00dan\2\u04d2\u04d4\7[\2\2\u04d3\u04d5\5\u00b4[\2\u04d4\u04d3\3\2"+
		"\2\2\u04d4\u04d5\3\2\2\2\u04d5\u00d9\3\2\2\2\u04d6\u04d7\7=\2\2\u04d7"+
		"\u04da\5\u00dco\2\u04d8\u04da\79\2\2\u04d9\u04d6\3\2\2\2\u04d9\u04d8\3"+
		"\2\2\2\u04da\u00db\3\2\2\2\u04db\u04de\5~@\2\u04dc\u04de\7P\2\2\u04dd"+
		"\u04db\3\2\2\2\u04dd\u04dc\3\2\2\2\u04de\u04e6\3\2\2\2\u04df\u04e2\7Y"+
		"\2\2\u04e0\u04e3\5~@\2\u04e1\u04e3\7P\2\2\u04e2\u04e0\3\2\2\2\u04e2\u04e1"+
		"\3\2\2\2\u04e3\u04e5\3\2\2\2\u04e4\u04df\3\2\2\2\u04e5\u04e8\3\2\2\2\u04e6"+
		"\u04e4\3\2\2\2\u04e6\u04e7\3\2\2\2\u04e7\u00dd\3\2\2\2\u04e8\u04e6\3\2"+
		"\2\2\u04e9\u04ea\7<\2\2\u04ea\u04ee\7T\2\2\u04eb\u04ed\5\u00e0q\2\u04ec"+
		"\u04eb\3\2\2\2\u04ed\u04f0\3\2\2\2\u04ee\u04ec\3\2\2\2\u04ee\u04ef\3\2"+
		"\2\2\u04ef\u04f1\3\2\2\2\u04f0\u04ee\3\2\2\2\u04f1\u04f2\7U\2\2\u04f2"+
		"\u00df\3\2\2\2\u04f3\u04f4\5\u00e2r\2\u04f4\u04f6\7[\2\2\u04f5\u04f7\5"+
		"\u00b4[\2\u04f6\u04f5\3\2\2\2\u04f6\u04f7\3\2\2\2\u04f7\u00e1\3\2\2\2"+
		"\u04f8\u04fb\7=\2\2\u04f9\u04fc\5\u00ba^\2\u04fa\u04fc\5\u00e4s\2\u04fb"+
		"\u04f9\3\2\2\2\u04fb\u04fa\3\2\2\2\u04fc\u04ff\3\2\2\2\u04fd\u04ff\79"+
		"\2\2\u04fe\u04f8\3\2\2\2\u04fe\u04fd\3\2\2\2\u04ff\u00e3\3\2\2\2\u0500"+
		"\u0501\5\u00aaV\2\u0501\u0502\7X\2\2\u0502\u0507\3\2\2\2\u0503\u0504\5"+
		"\u00a8U\2\u0504\u0505\7_\2\2\u0505\u0507\3\2\2\2\u0506\u0500\3\2\2\2\u0506"+
		"\u0503\3\2\2\2\u0506\u0507\3\2\2\2\u0507\u0508\3\2\2\2\u0508\u0509\5d"+
		"\63\2\u0509\u00e5\3\2\2\2\u050a\u050e\7L\2\2\u050b\u050f\5d\63\2\u050c"+
		"\u050f\5\u00e8u\2\u050d\u050f\5\u00eav\2\u050e\u050b\3\2\2\2\u050e\u050c"+
		"\3\2\2\2\u050e\u050d\3\2\2\2\u050e\u050f\3\2\2\2\u050f\u0510\3\2\2\2\u0510"+
		"\u0511\5\u00b2Z\2\u0511\u00e7\3\2\2\2\u0512\u0514\5\u00b6\\\2\u0513\u0512"+
		"\3\2\2\2\u0513\u0514\3\2\2\2\u0514\u0515\3\2\2\2\u0515\u0517\7Z\2\2\u0516"+
		"\u0518\5d\63\2\u0517\u0516\3\2\2\2\u0517\u0518\3\2\2\2\u0518\u0519\3\2"+
		"\2\2\u0519\u051b\7Z\2\2\u051a\u051c\5\u00b6\\\2\u051b\u051a\3\2\2\2\u051b"+
		"\u051c\3\2\2\2\u051c\u00e9\3\2\2\2\u051d\u051e\5\u00aaV\2\u051e\u051f"+
		"\7X\2\2\u051f\u0524\3\2\2\2\u0520\u0521\5\u00a8U\2\u0521\u0522\7_\2\2"+
		"\u0522\u0524\3\2\2\2\u0523\u051d\3\2\2\2\u0523\u0520\3\2\2\2\u0523\u0524"+
		"\3\2\2\2\u0524\u0525\3\2\2\2\u0525\u0526\7!\2\2\u0526\u0527\5d\63\2\u0527"+
		"\u00eb\3\2\2\2\u0528\u0529\7?\2\2\u0529\u052a\5d\63\2\u052a\u00ed\3\2"+
		"\2\2\u052b\u052e\5\u0110\u0089\2\u052c\u052e\7Q\2\2\u052d\u052b\3\2\2"+
		"\2\u052d\u052c\3\2\2\2\u052e\u00ef\3\2\2\2\u052f\u0530\7V\2\2\u0530\u0531"+
		"\5\u00f2z\2\u0531\u0532\7W\2\2\u0532\u0533\5\u00f4{\2\u0533\u00f1\3\2"+
		"\2\2\u0534\u0535\5d\63\2\u0535\u00f3\3\2\2\2\u0536\u0537\5~@\2\u0537\u00f5"+
		"\3\2\2\2\u0538\u0539\7s\2\2\u0539\u053a\5~@\2\u053a\u00f7\3\2\2\2\u053b"+
		"\u053c\7V\2\2\u053c\u053d\7W\2\2\u053d\u053e\5\u00f4{\2\u053e\u00f9\3"+
		"\2\2\2\u053f\u0540\7@\2\2\u0540\u0541\7V\2\2\u0541\u0542\5~@\2\u0542\u0543"+
		"\7W\2\2\u0543\u0544\5\u00f4{\2\u0544\u00fb\3\2\2\2\u0545\u054b\7B\2\2"+
		"\u0546\u0547\7B\2\2\u0547\u054b\7u\2\2\u0548\u0549\7u\2\2\u0549\u054b"+
		"\7B\2\2\u054a\u0545\3\2\2\2\u054a\u0546\3\2\2\2\u054a\u0548\3\2\2\2\u054b"+
		"\u054c\3\2\2\2\u054c\u054d\5\u00f4{\2\u054d\u00fd\3\2\2\2\u054e\u054f"+
		"\7:\2\2\u054f\u0550\5\u0100\u0081\2\u0550\u00ff\3\2\2\2\u0551\u0552\6"+
		"\u0081\20\2\u0552\u0553\5\u0104\u0083\2\u0553\u0554\5\u0102\u0082\2\u0554"+
		"\u0557\3\2\2\2\u0555\u0557\5\u0104\u0083\2\u0556\u0551\3\2\2\2\u0556\u0555"+
		"\3\2\2\2\u0557\u0101\3\2\2\2\u0558\u055b\5\u0104\u0083\2\u0559\u055b\5"+
		"~@\2\u055a\u0558\3\2\2\2\u055a\u0559\3\2\2\2\u055b\u0103\3\2\2\2\u055c"+
		"\u0568\7R\2\2\u055d\u0562\5^\60\2\u055e\u055f\7Y\2\2\u055f\u0561\5^\60"+
		"\2\u0560\u055e\3\2\2\2\u0561\u0564\3\2\2\2\u0562\u0560\3\2\2\2\u0562\u0563"+
		"\3\2\2\2\u0563\u0566\3\2\2\2\u0564\u0562\3\2\2\2\u0565\u0567\7Y\2\2\u0566"+
		"\u0565\3\2\2\2\u0566\u0567\3\2\2\2\u0567\u0569\3\2\2\2\u0568\u055d\3\2"+
		"\2\2\u0568\u0569\3\2\2\2\u0569\u056a\3\2\2\2\u056a\u056b\7S\2\2\u056b"+
		"\u0105\3\2\2\2\u056c\u056d\5~@\2\u056d\u056e\7R\2\2\u056e\u0570\5d\63"+
		"\2\u056f\u0571\7Y\2\2\u0570\u056f\3\2\2\2\u0570\u0571\3\2\2\2\u0571\u0572"+
		"\3\2\2\2\u0572\u0573\7S\2\2\u0573\u0107\3\2\2\2\u0574\u057b\5\u010a\u0086"+
		"\2\u0575\u057b\5\u010e\u0088\2\u0576\u0577\7R\2\2\u0577\u0578\5d\63\2"+
		"\u0578\u0579\7S\2\2\u0579\u057b\3\2\2\2\u057a\u0574\3\2\2\2\u057a\u0575"+
		"\3\2\2\2\u057a\u0576\3\2\2\2\u057b\u0109\3\2\2\2\u057c\u0580\5r:\2\u057d"+
		"\u0580\5\u0112\u008a\2\u057e\u0580\5\u0126\u0094\2\u057f\u057c\3\2\2\2"+
		"\u057f\u057d\3\2\2\2\u057f\u057e\3\2\2\2\u0580\u010b\3\2\2\2\u0581\u0582"+
		"\t\17\2\2\u0582\u010d\3\2\2\2\u0583\u0586\7Q\2\2\u0584\u0585\7\\\2\2\u0585"+
		"\u0587\7Q\2\2\u0586\u0584\3\2\2\2\u0586\u0587\3\2\2\2\u0587\u010f\3\2"+
		"\2\2\u0588\u0589\7Q\2\2\u0589\u058a\7\\\2\2\u058a\u058b\7Q\2\2\u058b\u0111"+
		"\3\2\2\2\u058c\u058d\5\u0086D\2\u058d\u058e\5\u0114\u008b\2\u058e\u0113"+
		"\3\2\2\2\u058f\u0594\7T\2\2\u0590\u0592\5\u0116\u008c\2\u0591\u0593\7"+
		"Y\2\2\u0592\u0591\3\2\2\2\u0592\u0593\3\2\2\2\u0593\u0595\3\2\2\2\u0594"+
		"\u0590\3\2\2\2\u0594\u0595\3\2\2\2\u0595\u0596\3\2\2\2\u0596\u0597\7U"+
		"\2\2\u0597\u0115\3\2\2\2\u0598\u059d\5\u0118\u008d\2\u0599\u059a\7Y\2"+
		"\2\u059a\u059c\5\u0118\u008d\2\u059b\u0599\3\2\2\2\u059c\u059f\3\2\2\2"+
		"\u059d\u059b\3\2\2\2\u059d\u059e\3\2\2\2\u059e\u0117\3\2\2\2\u059f\u059d"+
		"\3\2\2\2\u05a0\u05a1\5\u011a\u008e\2\u05a1\u05a2\7[\2\2\u05a2\u05a4\3"+
		"\2\2\2\u05a3\u05a0\3\2\2\2\u05a3\u05a4\3\2\2\2\u05a4\u05a5\3\2\2\2\u05a5"+
		"\u05a6\5\u011c\u008f\2\u05a6\u0119\3\2\2\2\u05a7\u05ab\7Q\2\2\u05a8\u05ab"+
		"\5d\63\2\u05a9\u05ab\5\u0114\u008b\2\u05aa\u05a7\3\2\2\2\u05aa\u05a8\3"+
		"\2\2\2\u05aa\u05a9\3\2\2\2\u05ab\u011b\3\2\2\2\u05ac\u05af\5d\63\2\u05ad"+
		"\u05af\5\u0114\u008b\2\u05ae\u05ac\3\2\2\2\u05ae\u05ad\3\2\2\2\u05af\u011d"+
		"\3\2\2\2\u05b0\u05b1\7A\2\2\u05b1\u05b7\7T\2\2\u05b2\u05b3\5\u0120\u0091"+
		"\2\u05b3\u05b4\5\u0098M\2\u05b4\u05b6\3\2\2\2\u05b5\u05b2\3\2\2\2\u05b6"+
		"\u05b9\3\2\2\2\u05b7\u05b5\3\2\2\2\u05b7\u05b8\3\2\2\2\u05b8\u05ba\3\2"+
		"\2\2\u05b9\u05b7\3\2\2\2\u05ba\u05bb\7U\2\2\u05bb\u011f\3\2\2\2\u05bc"+
		"\u05bd\6\u0091\21\2\u05bd\u05be\5\u00a8U\2\u05be\u05bf\5~@\2\u05bf\u05c2"+
		"\3\2\2\2\u05c0\u05c2\5\u0124\u0093\2\u05c1\u05bc\3\2\2\2\u05c1\u05c0\3"+
		"\2\2\2\u05c2\u05c4\3\2\2\2\u05c3\u05c5\5\u0122\u0092\2\u05c4\u05c3\3\2"+
		"\2\2\u05c4\u05c5\3\2\2\2\u05c5\u0121\3\2\2\2\u05c6\u05c7\t\20\2\2\u05c7"+
		"\u0123\3\2\2\2\u05c8\u05ca\7s\2\2\u05c9\u05c8\3\2\2\2\u05c9\u05ca\3\2"+
		"\2\2\u05ca\u05cb\3\2\2\2\u05cb\u05cc\5\u00eex\2\u05cc\u0125\3\2\2\2\u05cd"+
		"\u05ce\7:\2\2\u05ce\u05cf\5\u0100\u0081\2\u05cf\u05d0\5\u00b2Z\2\u05d0"+
		"\u0127\3\2\2\2\u05d1\u05d2\7V\2\2\u05d2\u05d3\5d\63\2\u05d3\u05d4\7W\2"+
		"\2\u05d4\u0129\3\2\2\2\u05d5\u05d6\7\\\2\2\u05d6\u05d7\7R\2\2\u05d7\u05d8"+
		"\5~@\2\u05d8\u05d9\7S\2\2\u05d9\u012b\3\2\2\2\u05da\u05e9\7R\2\2\u05db"+
		"\u05e2\5\u00aaV\2\u05dc\u05df\5~@\2\u05dd\u05de\7Y\2\2\u05de\u05e0\5\u00aa"+
		"V\2\u05df\u05dd\3\2\2\2\u05df\u05e0\3\2\2\2\u05e0\u05e2\3\2\2\2\u05e1"+
		"\u05db\3\2\2\2\u05e1\u05dc\3\2\2\2\u05e2\u05e4\3\2\2\2\u05e3\u05e5\7`"+
		"\2\2\u05e4\u05e3\3\2\2\2\u05e4\u05e5\3\2\2\2\u05e5\u05e7\3\2\2\2\u05e6"+
		"\u05e8\7Y\2\2\u05e7\u05e6\3\2\2\2\u05e7\u05e8\3\2\2\2\u05e8\u05ea\3\2"+
		"\2\2\u05e9\u05e1\3\2\2\2\u05e9\u05ea\3\2\2\2\u05ea\u05eb\3\2\2\2\u05eb"+
		"\u05ec\7S\2\2\u05ec\u012d\3\2\2\2\u05ed\u05ee\5\u0130\u0099\2\u05ee\u05ef"+
		"\7\\\2\2\u05ef\u05f0\7Q\2\2\u05f0\u012f\3\2\2\2\u05f1\u05f2\5~@\2\u05f2"+
		"\u0131\3\2\2\2\u009f\u0137\u013c\u0144\u014b\u014f\u0156\u0163\u0165\u017f"+
		"\u0198\u01a1\u01ab\u01b3\u01c3\u01d5\u01dd\u01eb\u01f1\u01f6\u01f9\u0203"+
		"\u020a\u0212\u0219\u021c\u0222\u0232\u0239\u023f\u024a\u0251\u0253\u026e"+
		"\u0276\u027c\u0282\u028c\u0293\u0299\u029d\u02a5\u02a8\u02af\u02b2\u02b8"+
		"\u02bb\u02be\u02cb\u02de\u02fe\u0300\u0308\u0321\u032c\u0333\u0338\u0342"+
		"\u0349\u0354\u0358\u035d\u0360\u0369\u036f\u037a\u0382\u0388\u0391\u039c"+
		"\u03a7\u03ab\u03ad\u03bb\u03bf\u03c3\u03c6\u03cd\u03d9\u03dc\u03e3\u03e5"+
		"\u03e8\u03ee\u03f1\u03f4\u03fa\u0401\u0404\u040b\u0415\u0422\u0426\u0429"+
		"\u0432\u043c\u0440\u0444\u0448\u044f\u0457\u0462\u0466\u046a\u0476\u047a"+
		"\u047e\u0487\u048f\u04a3\u04a7\u04ab\u04af\u04bb\u04c0\u04c5\u04c9\u04d4"+
		"\u04d9\u04dd\u04e2\u04e6\u04ee\u04f6\u04fb\u04fe\u0506\u050e\u0513\u0517"+
		"\u051b\u0523\u052d\u054a\u0556\u055a\u0562\u0566\u0568\u0570\u057a\u057f"+
		"\u0586\u0592\u0594\u059d\u05a3\u05aa\u05ae\u05b7\u05c1\u05c4\u05c9\u05df"+
		"\u05e1\u05e4\u05e7\u05e9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}