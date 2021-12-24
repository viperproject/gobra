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
		TRUE=1, FALSE=2, ASSERT=3, ASSUME=4, INHALE=5, EXHALE=6, PRE=7, PRESERVES=8, 
		POST=9, INV=10, DEC=11, PURE=12, IMPL=13, OLD=14, LHS=15, FORALL=16, EXISTS=17, 
		ACCESS=18, FOLD=19, UNFOLD=20, UNFOLDING=21, GHOST=22, IN=23, MULTI=24, 
		SUBSET=25, UNION=26, INTERSECTION=27, SETMINUS=28, IMPLIES=29, QMARK=30, 
		L_PRED=31, R_PRED=32, RANGE=33, SEQ=34, SET=35, MSET=36, DICT=37, OPT=38, 
		LEN=39, NEW=40, MAKE=41, CAP=42, SOME=43, GET=44, DOM=45, NONE=46, PRED=47, 
		TYPE_OF=48, IS_COMPARABLE=49, SHARE=50, ADDR_MOD=51, DOT_DOT=52, SHARED=53, 
		EXCLUSIVE=54, PREDICATE=55, BOOL=56, STRING=57, PERM=58, RUNE=59, INT=60, 
		INT8=61, INT16=62, INT32=63, INT64=64, BYTE=65, UINT=66, UINT8=67, UINT16=68, 
		UINT32=69, UINT64=70, UINTPTR=71, BREAK=72, DEFAULT=73, FUNC=74, INTERFACE=75, 
		SELECT=76, CASE=77, DEFER=78, GO=79, MAP=80, STRUCT=81, CHAN=82, ELSE=83, 
		GOTO=84, PACKAGE=85, SWITCH=86, CONST=87, FALLTHROUGH=88, IF=89, TYPE=90, 
		CONTINUE=91, FOR=92, IMPORT=93, RETURN=94, VAR=95, NIL_LIT=96, IDENTIFIER=97, 
		L_PAREN=98, R_PAREN=99, L_CURLY=100, R_CURLY=101, L_BRACKET=102, R_BRACKET=103, 
		ASSIGN=104, COMMA=105, SEMI=106, COLON=107, DOT=108, PLUS_PLUS=109, MINUS_MINUS=110, 
		DECLARE_ASSIGN=111, ELLIPSIS=112, LOGICAL_OR=113, LOGICAL_AND=114, EQUALS=115, 
		NOT_EQUALS=116, LESS=117, LESS_OR_EQUALS=118, GREATER=119, GREATER_OR_EQUALS=120, 
		OR=121, DIV=122, MOD=123, LSHIFT=124, RSHIFT=125, BIT_CLEAR=126, EXCLAMATION=127, 
		PLUS=128, MINUS=129, CARET=130, STAR=131, AMPERSAND=132, RECEIVE=133, 
		DECIMAL_LIT=134, BINARY_LIT=135, OCTAL_LIT=136, HEX_LIT=137, FLOAT_LIT=138, 
		DECIMAL_FLOAT_LIT=139, HEX_FLOAT_LIT=140, IMAGINARY_LIT=141, RUNE_LIT=142, 
		BYTE_VALUE=143, OCTAL_BYTE_VALUE=144, HEX_BYTE_VALUE=145, LITTLE_U_VALUE=146, 
		BIG_U_VALUE=147, RAW_STRING_LIT=148, INTERPRETED_STRING_LIT=149, WS=150, 
		COMMENT=151, TERMINATOR=152, LINE_COMMENT=153;
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
		RULE_low = 68, RULE_high = 69, RULE_cap = 70, RULE_ifStmt = 71, RULE_exprSwitchStmt = 72, 
		RULE_typeSwitchStmt = 73, RULE_assign_op = 74, RULE_eos = 75, RULE_packageClause = 76, 
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
			"low", "high", "cap", "ifStmt", "exprSwitchStmt", "typeSwitchStmt", "assign_op", 
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
			null, "'true'", "'false'", "'assert'", "'assume'", "'inhale'", "'exhale'", 
			"'requires'", "'preserves'", "'ensures'", "'invariant'", "'decreases'", 
			"'pure'", "'implements'", "'old'", "'#lhs'", "'forall'", "'exists'", 
			"'acc'", "'fold'", "'unfold'", "'unfolding'", "'ghost'", "'in'", "'#'", 
			"'subset'", "'union'", "'intersection'", "'setminus'", "'==>'", "'?'", 
			"'!<'", "'!>'", "'range'", "'seq'", "'set'", "'mset'", "'dict'", "'option'", 
			"'len'", "'new'", "'make'", "'cap'", "'some'", "'get'", "'domain'", "'none'", 
			"'pred'", "'typeOf'", "'isComparable'", "'share'", "'@'", "'..'", "'shared'", 
			"'exclusive'", "'predicate'", "'bool'", "'string'", "'perm'", "'rune'", 
			"'int'", "'int8'", "'int16'", "'int32'", "'int64'", "'byte'", "'uint'", 
			"'uint8'", "'uint16'", "'uint32'", "'uint64'", "'uintptr'", "'break'", 
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
			null, "TRUE", "FALSE", "ASSERT", "ASSUME", "INHALE", "EXHALE", "PRE", 
			"PRESERVES", "POST", "INV", "DEC", "PURE", "IMPL", "OLD", "LHS", "FORALL", 
			"EXISTS", "ACCESS", "FOLD", "UNFOLD", "UNFOLDING", "GHOST", "IN", "MULTI", 
			"SUBSET", "UNION", "INTERSECTION", "SETMINUS", "IMPLIES", "QMARK", "L_PRED", 
			"R_PRED", "RANGE", "SEQ", "SET", "MSET", "DICT", "OPT", "LEN", "NEW", 
			"MAKE", "CAP", "SOME", "GET", "DOM", "NONE", "PRED", "TYPE_OF", "IS_COMPARABLE", 
			"SHARE", "ADDR_MOD", "DOT_DOT", "SHARED", "EXCLUSIVE", "PREDICATE", "BOOL", 
			"STRING", "PERM", "RUNE", "INT", "INT8", "INT16", "INT32", "INT64", "BYTE", 
			"UINT", "UINT8", "UINT16", "UINT32", "UINT64", "UINTPTR", "BREAK", "DEFAULT", 
			"FUNC", "INTERFACE", "SELECT", "CASE", "DEFER", "GO", "MAP", "STRUCT", 
			"CHAN", "ELSE", "GOTO", "PACKAGE", "SWITCH", "CONST", "FALLTHROUGH", 
			"IF", "TYPE", "CONTINUE", "FOR", "IMPORT", "RETURN", "VAR", "NIL_LIT", 
			"IDENTIFIER", "L_PAREN", "R_PAREN", "L_CURLY", "R_CURLY", "L_BRACKET", 
			"R_BRACKET", "ASSIGN", "COMMA", "SEMI", "COLON", "DOT", "PLUS_PLUS", 
			"MINUS_MINUS", "DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", "LOGICAL_AND", 
			"EQUALS", "NOT_EQUALS", "LESS", "LESS_OR_EQUALS", "GREATER", "GREATER_OR_EQUALS", 
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
		public Token kind;
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
		public TerminalNode ASSUME() { return getToken(GobraParser.ASSUME, 0); }
		public TerminalNode INHALE() { return getToken(GobraParser.INHALE, 0); }
		public TerminalNode EXHALE() { return getToken(GobraParser.EXHALE, 0); }
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
			setState(324);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(316);
				match(GHOST);
				setState(317);
				statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(318);
				match(ASSERT);
				setState(319);
				expression(0);
				}
				break;
			case 3:
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
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(322);
				((GhostStatementContext)_localctx).kind = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE))) != 0)) ) {
					((GhostStatementContext)_localctx).kind = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(323);
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
			setState(326);
			boundVariableDecl();
			setState(331);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(327);
					match(COMMA);
					setState(328);
					boundVariableDecl();
					}
					} 
				}
				setState(333);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(335);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(334);
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
			setState(337);
			match(IDENTIFIER);
			setState(342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(338);
				match(COMMA);
				setState(339);
				match(IDENTIFIER);
				}
				}
				setState(344);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(345);
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
			setState(347);
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
			setState(349);
			match(ACCESS);
			setState(350);
			match(L_PAREN);
			setState(351);
			expression(0);
			setState(357);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(352);
				match(COMMA);
				setState(355);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(353);
					match(IDENTIFIER);
					}
					break;
				case 2:
					{
					setState(354);
					expression(0);
					}
					break;
				}
				}
			}

			setState(359);
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
			setState(361);
			expression(0);
			setState(362);
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
			setState(364);
			statement();
			setState(365);
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
			setState(383);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(367);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(368);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(369);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(370);
				isComparable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(371);
				old();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(372);
				sConversion();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(373);
				optionNone();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(374);
				optionSome();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(375);
				optionGet();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(376);
				match(FORALL);
				setState(377);
				boundVariables();
				setState(378);
				match(COLON);
				setState(379);
				match(COLON);
				setState(380);
				triggers();
				setState(381);
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
			setState(385);
			match(SOME);
			setState(386);
			match(L_PAREN);
			setState(387);
			expression(0);
			setState(388);
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
			setState(390);
			match(NONE);
			setState(391);
			match(L_BRACKET);
			setState(392);
			type_();
			setState(393);
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
			setState(395);
			match(GET);
			setState(396);
			match(L_PAREN);
			setState(397);
			expression(0);
			setState(398);
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
			setState(400);
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
			setState(401);
			match(L_PAREN);
			setState(402);
			expression(0);
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
			setState(408);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(405);
				trigger();
				}
				}
				setState(410);
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
			setState(411);
			match(L_CURLY);
			setState(412);
			expression(0);
			setState(417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(413);
				match(COMMA);
				setState(414);
				expression(0);
				}
				}
				setState(419);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(420);
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
			setState(422);
			match(OLD);
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(423);
				match(L_BRACKET);
				setState(424);
				oldLabelUse();
				setState(425);
				match(R_BRACKET);
				}
			}

			setState(429);
			match(L_PAREN);
			setState(430);
			expression(0);
			setState(431);
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
			setState(435);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(433);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(434);
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
			setState(437);
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
			setState(439);
			match(TYPE_OF);
			setState(440);
			match(L_PAREN);
			setState(441);
			expression(0);
			setState(442);
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
			setState(444);
			match(IS_COMPARABLE);
			setState(445);
			match(L_PAREN);
			setState(446);
			expression(0);
			setState(447);
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
			setState(451);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(449);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(450);
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
			setState(453);
			match(GHOST);
			setState(454);
			match(L_BRACKET);
			setState(455);
			match(R_BRACKET);
			setState(456);
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
			setState(469);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(458);
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
				setState(459);
				match(L_BRACKET);
				setState(460);
				type_();
				setState(461);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(463);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(464);
				match(L_BRACKET);
				setState(465);
				type_();
				setState(466);
				match(R_BRACKET);
				setState(467);
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
			setState(471);
			match(L_BRACKET);
			{
			setState(472);
			seqUpdClause();
			setState(477);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(473);
				match(COMMA);
				setState(474);
				seqUpdClause();
				}
				}
				setState(479);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(480);
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
			setState(482);
			expression(0);
			setState(483);
			match(ASSIGN);
			setState(484);
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
			setState(505);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(491);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC))) != 0)) {
					{
					{
					{
					setState(486);
					specStatement();
					}
					setState(487);
					eos();
					}
					}
					setState(493);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(494);
				match(PURE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(502);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE))) != 0)) {
					{
					{
					setState(497);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(495);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(496);
						match(PURE);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(499);
					eos();
					}
					}
					setState(504);
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
			setState(515);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(507);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(508);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(509);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(510);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(511);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(512);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(513);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(514);
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
			setState(517);
			specification();
			setState(518);
			match(FUNC);
			setState(519);
			match(IDENTIFIER);
			{
			setState(520);
			signature();
			setState(522);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(521);
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
			setState(524);
			specification();
			setState(525);
			match(FUNC);
			setState(526);
			receiver();
			setState(527);
			match(IDENTIFIER);
			{
			setState(528);
			signature();
			setState(530);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(529);
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
			setState(532);
			match(L_CURLY);
			setState(537);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SHARE) {
				{
				setState(533);
				match(SHARE);
				setState(534);
				identifierList();
				setState(535);
				eos();
				}
			}

			setState(540);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(539);
				statementList();
				}
			}

			setState(542);
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
			setState(546);
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
				setState(545);
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
			setState(548);
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
			setState(549);
			match(L_BRACKET);
			setState(550);
			expression(0);
			setState(551);
			match(DOT_DOT);
			setState(552);
			expression(0);
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
			setState(555);
			packageClause();
			setState(556);
			eos();
			setState(562);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(557);
				importDecl();
				setState(558);
				eos();
				}
				}
				setState(564);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (PRE - 7)) | (1L << (PRESERVES - 7)) | (1L << (POST - 7)) | (1L << (DEC - 7)) | (1L << (PURE - 7)) | (1L << (GHOST - 7)) | (1L << (SEQ - 7)) | (1L << (SET - 7)) | (1L << (MSET - 7)) | (1L << (DICT - 7)) | (1L << (OPT - 7)) | (1L << (PRED - 7)) | (1L << (BOOL - 7)) | (1L << (STRING - 7)) | (1L << (PERM - 7)) | (1L << (RUNE - 7)) | (1L << (INT - 7)) | (1L << (INT8 - 7)) | (1L << (INT16 - 7)) | (1L << (INT32 - 7)) | (1L << (INT64 - 7)) | (1L << (BYTE - 7)) | (1L << (UINT - 7)) | (1L << (UINT8 - 7)) | (1L << (UINT16 - 7)) | (1L << (UINT32 - 7)) | (1L << (UINT64 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (UINTPTR - 71)) | (1L << (FUNC - 71)) | (1L << (INTERFACE - 71)) | (1L << (MAP - 71)) | (1L << (STRUCT - 71)) | (1L << (CHAN - 71)) | (1L << (CONST - 71)) | (1L << (TYPE - 71)) | (1L << (VAR - 71)) | (1L << (IDENTIFIER - 71)) | (1L << (L_PAREN - 71)) | (1L << (L_BRACKET - 71)) | (1L << (STAR - 71)) | (1L << (RECEIVE - 71)))) != 0)) {
				{
				{
				setState(569);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(565);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(566);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(567);
					declaration();
					}
					break;
				case 4:
					{
					setState(568);
					ghostMember();
					}
					break;
				}
				setState(571);
				eos();
				}
				}
				setState(577);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(578);
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
			setState(595);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(580);
				fpredicateDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(581);
				mpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(582);
				implementationProof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(586);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(583);
					match(GHOST);
					}
					break;
				case 2:
					{
					{
					setState(584);
					match(GHOST);
					setState(585);
					eos();
					}
					}
					break;
				}
				setState(593);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
				case 1:
					{
					setState(588);
					methodDecl();
					}
					break;
				case 2:
					{
					setState(589);
					functionDecl();
					}
					break;
				case 3:
					{
					setState(590);
					constDecl();
					}
					break;
				case 4:
					{
					setState(591);
					typeDecl();
					}
					break;
				case 5:
					{
					setState(592);
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
			setState(597);
			match(PRED);
			setState(598);
			match(IDENTIFIER);
			setState(599);
			parameters();
			setState(601);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(600);
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
		enterRule(_localctx, 72, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(603);
			match(L_CURLY);
			setState(604);
			expression(0);
			setState(605);
			eos();
			setState(606);
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
			setState(608);
			match(PRED);
			setState(609);
			receiver();
			setState(610);
			match(IDENTIFIER);
			setState(611);
			parameters();
			setState(613);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(612);
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
			setState(615);
			type_();
			setState(616);
			match(IMPL);
			setState(617);
			type_();
			setState(618);
			match(L_CURLY);
			setState(624);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRED) {
				{
				{
				setState(619);
				implementationProofPredicateAlias();
				setState(620);
				eos();
				}
				}
				setState(626);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(632);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PURE || _la==L_PAREN) {
				{
				{
				setState(627);
				methodImplementationProof();
				setState(628);
				eos();
				}
				}
				setState(634);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(635);
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
			setState(638);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(637);
				match(PURE);
				}
			}

			setState(640);
			nonLocalReceiver();
			setState(641);
			match(IDENTIFIER);
			setState(642);
			signature();
			setState(644);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				setState(643);
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
			setState(654);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(646);
				primaryExpr(0);
				setState(647);
				match(DOT);
				setState(648);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(650);
				type_();
				setState(651);
				match(DOT);
				setState(652);
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
			setState(656);
			match(PRED);
			setState(657);
			match(IDENTIFIER);
			setState(658);
			match(DECLARE_ASSIGN);
			setState(661);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(659);
				selection();
				}
				break;
			case 2:
				{
				setState(660);
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
			setState(663);
			maybeAddressableIdentifierList();
			setState(671);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case PRED:
			case BOOL:
			case STRING:
			case PERM:
			case RUNE:
			case INT:
			case INT8:
			case INT16:
			case INT32:
			case INT64:
			case BYTE:
			case UINT:
			case UINT8:
			case UINT16:
			case UINT32:
			case UINT64:
			case UINTPTR:
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
				setState(664);
				type_();
				setState(667);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(665);
					match(ASSIGN);
					setState(666);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(669);
				match(ASSIGN);
				setState(670);
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
			setState(673);
			maybeAddressableIdentifierList();
			setState(674);
			match(DECLARE_ASSIGN);
			setState(675);
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
			setState(677);
			match(L_PAREN);
			setState(679);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(678);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(682);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(681);
				match(STAR);
				}
			}

			setState(684);
			match(IDENTIFIER);
			setState(685);
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
			setState(687);
			match(L_PAREN);
			setState(689);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(688);
				match(IDENTIFIER);
				}
				break;
			}
			setState(692);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(691);
				match(STAR);
				}
			}

			setState(694);
			typeName();
			setState(695);
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
			setState(698);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(697);
				match(GHOST);
				}
				break;
			}
			setState(701);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(700);
				identifierList();
				}
				break;
			}
			setState(704);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(703);
				match(ELLIPSIS);
				}
			}

			setState(706);
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
			setState(717);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(708);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(709);
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
				setState(710);
				match(L_PAREN);
				setState(711);
				expression(0);
				setState(712);
				match(R_PAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(714);
				unfolding();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(715);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (EXCLAMATION - 127)) | (1L << (PLUS - 127)) | (1L << (MINUS - 127)) | (1L << (CARET - 127)) | (1L << (STAR - 127)) | (1L << (AMPERSAND - 127)) | (1L << (RECEIVE - 127)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(716);
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
			setState(719);
			match(UNFOLDING);
			setState(720);
			predicateAccess();
			setState(721);
			match(IN);
			setState(722);
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
		public TerminalNode TYPE() { return getToken(GobraParser.TYPE, 0); }
		public TerminalNode L_BRACKET() { return getToken(GobraParser.L_BRACKET, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public TerminalNode R_BRACKET() { return getToken(GobraParser.R_BRACKET, 0); }
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
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
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
			setState(741);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(725);
				match(TYPE);
				setState(726);
				match(L_BRACKET);
				setState(727);
				type_();
				setState(728);
				match(R_BRACKET);
				}
				break;
			case 2:
				{
				setState(730);
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
				setState(731);
				match(L_PAREN);
				setState(732);
				expression(0);
				setState(733);
				match(R_PAREN);
				}
				break;
			case 3:
				{
				setState(735);
				unfolding();
				}
				break;
			case 4:
				{
				setState(736);
				new_();
				}
				break;
			case 5:
				{
				setState(737);
				make();
				}
				break;
			case 6:
				{
				setState(738);
				((ExpressionContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (EXCLAMATION - 127)) | (1L << (PLUS - 127)) | (1L << (MINUS - 127)) | (1L << (CARET - 127)) | (1L << (STAR - 127)) | (1L << (AMPERSAND - 127)) | (1L << (RECEIVE - 127)))) != 0)) ) {
					((ExpressionContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(739);
				expression(11);
				}
				break;
			case 7:
				{
				setState(740);
				primaryExpr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(775);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(773);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(743);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(744);
						((ExpressionContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 122)) & ~0x3f) == 0 && ((1L << (_la - 122)) & ((1L << (DIV - 122)) | (1L << (MOD - 122)) | (1L << (LSHIFT - 122)) | (1L << (RSHIFT - 122)) | (1L << (BIT_CLEAR - 122)) | (1L << (STAR - 122)) | (1L << (AMPERSAND - 122)))) != 0)) ) {
							((ExpressionContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(745);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(746);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(747);
						((ExpressionContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & ((1L << (PLUS_PLUS - 109)) | (1L << (OR - 109)) | (1L << (PLUS - 109)) | (1L << (MINUS - 109)) | (1L << (CARET - 109)))) != 0)) ) {
							((ExpressionContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(748);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(749);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(750);
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
						setState(751);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(752);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(753);
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
						setState(754);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(755);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(756);
						((ExpressionContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (EQUALS - 115)) | (1L << (NOT_EQUALS - 115)) | (1L << (LESS - 115)) | (1L << (LESS_OR_EQUALS - 115)) | (1L << (GREATER - 115)) | (1L << (GREATER_OR_EQUALS - 115)))) != 0)) ) {
							((ExpressionContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(757);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(758);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(759);
						match(LOGICAL_AND);
						setState(760);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(761);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(762);
						match(LOGICAL_OR);
						setState(763);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(764);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(765);
						match(IMPLIES);
						setState(766);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(767);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(768);
						match(QMARK);
						setState(769);
						expression(0);
						setState(770);
						match(COLON);
						setState(771);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(777);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
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
			setState(778);
			match(MAKE);
			setState(779);
			match(L_PAREN);
			setState(780);
			type_();
			setState(783);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(781);
				match(COMMA);
				setState(782);
				expressionList();
				}
			}

			setState(785);
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
			setState(787);
			match(NEW);
			setState(788);
			match(L_PAREN);
			setState(789);
			type_();
			setState(790);
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
			setState(808);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(792);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(793);
				declaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(794);
				labeledStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(795);
				simpleStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(796);
				goStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(797);
				returnStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(798);
				breakStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(799);
				continueStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(800);
				gotoStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(801);
				fallthroughStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(802);
				block();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(803);
				ifStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(804);
				switchStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(805);
				selectStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(806);
				specForStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(807);
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
			setState(810);
			loopSpec();
			setState(811);
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
			setState(819);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(813);
				match(INV);
				setState(814);
				expression(0);
				setState(815);
				eos();
				}
				}
				setState(821);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(826);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(822);
				match(DEC);
				setState(823);
				terminationMeasure();
				setState(824);
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
			setState(829);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(828);
				expressionList();
				}
				break;
			}
			setState(833);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(831);
				match(IF);
				setState(832);
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
			setState(843);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(835);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(836);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(837);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(838);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(839);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(840);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(841);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(842);
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
			setState(850);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(846);
				operand();
				}
				break;
			case 2:
				{
				setState(847);
				conversion();
				}
				break;
			case 3:
				{
				setState(848);
				methodExpr();
				}
				break;
			case 4:
				{
				setState(849);
				ghostPrimaryExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(866);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrimaryExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
					setState(852);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(862);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
					case 1:
						{
						{
						setState(853);
						match(DOT);
						setState(854);
						match(IDENTIFIER);
						}
						}
						break;
					case 2:
						{
						setState(855);
						index();
						}
						break;
					case 3:
						{
						setState(856);
						slice_();
						}
						break;
					case 4:
						{
						setState(857);
						seqUpdExp();
						}
						break;
					case 5:
						{
						setState(858);
						typeAssertion();
						}
						break;
					case 6:
						{
						setState(859);
						if (!(noTerminatorBetween(1))) throw new FailedPredicateException(this, "noTerminatorBetween(1)");
						setState(860);
						arguments();
						}
						break;
					case 7:
						{
						setState(861);
						predConstructArgs();
						}
						break;
					}
					}
					} 
				}
				setState(868);
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
			setState(869);
			match(L_PRED);
			setState(871);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(870);
				expressionList();
				}
			}

			setState(874);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(873);
				match(COMMA);
				}
			}

			setState(876);
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
			setState(878);
			match(INTERFACE);
			setState(879);
			match(L_CURLY);
			setState(889);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(883);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
					case 1:
						{
						setState(880);
						methodSpec();
						}
						break;
					case 2:
						{
						setState(881);
						typeName();
						}
						break;
					case 3:
						{
						setState(882);
						predicateSpec();
						}
						break;
					}
					setState(885);
					eos();
					}
					} 
				}
				setState(891);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			}
			setState(892);
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
			setState(894);
			match(PRED);
			setState(895);
			match(IDENTIFIER);
			setState(896);
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
			setState(914);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(898);
				if (!(noTerminatorAfterParams(2))) throw new FailedPredicateException(this, "noTerminatorAfterParams(2)");
				setState(900);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(899);
					match(GHOST);
					}
				}

				setState(902);
				specification();
				setState(903);
				match(IDENTIFIER);
				setState(904);
				parameters();
				setState(905);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(908);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(907);
					match(GHOST);
					}
				}

				setState(910);
				specification();
				setState(911);
				match(IDENTIFIER);
				setState(912);
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
		public Token predefined;
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
		public TerminalNode BOOL() { return getToken(GobraParser.BOOL, 0); }
		public TerminalNode STRING() { return getToken(GobraParser.STRING, 0); }
		public TerminalNode PERM() { return getToken(GobraParser.PERM, 0); }
		public TerminalNode RUNE() { return getToken(GobraParser.RUNE, 0); }
		public TerminalNode INT() { return getToken(GobraParser.INT, 0); }
		public TerminalNode INT8() { return getToken(GobraParser.INT8, 0); }
		public TerminalNode INT16() { return getToken(GobraParser.INT16, 0); }
		public TerminalNode INT32() { return getToken(GobraParser.INT32, 0); }
		public TerminalNode INT64() { return getToken(GobraParser.INT64, 0); }
		public TerminalNode BYTE() { return getToken(GobraParser.BYTE, 0); }
		public TerminalNode UINT() { return getToken(GobraParser.UINT, 0); }
		public TerminalNode UINT8() { return getToken(GobraParser.UINT8, 0); }
		public TerminalNode UINT16() { return getToken(GobraParser.UINT16, 0); }
		public TerminalNode UINT32() { return getToken(GobraParser.UINT32, 0); }
		public TerminalNode UINT64() { return getToken(GobraParser.UINT64, 0); }
		public TerminalNode UINTPTR() { return getToken(GobraParser.UINTPTR, 0); }
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
		int _la;
		try {
			setState(924);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(916);
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
				setState(917);
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
				setState(918);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(919);
				match(L_PAREN);
				setState(920);
				type_();
				setState(921);
				match(R_PAREN);
				}
				break;
			case BOOL:
			case STRING:
			case PERM:
			case RUNE:
			case INT:
			case INT8:
			case INT16:
			case INT32:
			case INT64:
			case BYTE:
			case UINT:
			case UINT8:
			case UINT16:
			case UINT32:
			case UINT64:
			case UINTPTR:
				enterOuterAlt(_localctx, 5);
				{
				setState(923);
				((Type_Context)_localctx).predefined = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & ((1L << (BOOL - 56)) | (1L << (STRING - 56)) | (1L << (PERM - 56)) | (1L << (RUNE - 56)) | (1L << (INT - 56)) | (1L << (INT8 - 56)) | (1L << (INT16 - 56)) | (1L << (INT32 - 56)) | (1L << (INT64 - 56)) | (1L << (BYTE - 56)) | (1L << (UINT - 56)) | (1L << (UINT8 - 56)) | (1L << (UINT16 - 56)) | (1L << (UINT32 - 56)) | (1L << (UINT64 - 56)) | (1L << (UINTPTR - 56)))) != 0)) ) {
					((Type_Context)_localctx).predefined = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
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
			setState(935);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(926);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(927);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(928);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(929);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(930);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(931);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(932);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(933);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(934);
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
			setState(937);
			match(PRED);
			setState(938);
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
			setState(940);
			match(L_PAREN);
			setState(952);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & ((1L << (GHOST - 22)) | (1L << (SEQ - 22)) | (1L << (SET - 22)) | (1L << (MSET - 22)) | (1L << (DICT - 22)) | (1L << (OPT - 22)) | (1L << (PRED - 22)) | (1L << (BOOL - 22)) | (1L << (STRING - 22)) | (1L << (PERM - 22)) | (1L << (RUNE - 22)) | (1L << (INT - 22)) | (1L << (INT8 - 22)) | (1L << (INT16 - 22)) | (1L << (INT32 - 22)) | (1L << (INT64 - 22)) | (1L << (BYTE - 22)) | (1L << (UINT - 22)) | (1L << (UINT8 - 22)) | (1L << (UINT16 - 22)) | (1L << (UINT32 - 22)) | (1L << (UINT64 - 22)) | (1L << (UINTPTR - 22)) | (1L << (FUNC - 22)) | (1L << (INTERFACE - 22)) | (1L << (MAP - 22)) | (1L << (STRUCT - 22)) | (1L << (CHAN - 22)))) != 0) || ((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (IDENTIFIER - 97)) | (1L << (L_PAREN - 97)) | (1L << (L_BRACKET - 97)) | (1L << (STAR - 97)) | (1L << (RECEIVE - 97)))) != 0)) {
				{
				setState(941);
				type_();
				setState(946);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(942);
						match(COMMA);
						setState(943);
						type_();
						}
						} 
					}
					setState(948);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
				}
				setState(950);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(949);
					match(COMMA);
					}
				}

				}
			}

			setState(954);
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
			setState(966);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(956);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(957);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(958);
				match(L_BRACKET);
				setState(959);
				match(ELLIPSIS);
				setState(960);
				match(R_BRACKET);
				setState(961);
				elementType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(962);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(963);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(964);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(965);
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
			setState(968);
			match(L_BRACKET);
			setState(984);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				{
				setState(970);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(969);
					low();
					}
				}

				setState(972);
				match(COLON);
				setState(974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(973);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(977);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
					{
					setState(976);
					low();
					}
				}

				setState(979);
				match(COLON);
				setState(980);
				high();
				setState(981);
				match(COLON);
				setState(982);
				cap();
				}
				break;
			}
			setState(986);
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
			setState(988);
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
			setState(990);
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
			setState(992);
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
			setState(994);
			match(IF);
			setState(999);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(996);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
				case 1:
					{
					setState(995);
					simpleStmt();
					}
					break;
				}
				setState(998);
				match(SEMI);
				}
				break;
			}
			setState(1001);
			expression(0);
			setState(1002);
			block();
			setState(1008);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(1003);
				match(ELSE);
				setState(1006);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1004);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1005);
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
		enterRule(_localctx, 144, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			match(SWITCH);
			setState(1015);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				{
				setState(1012);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(1011);
					simpleStmt();
					}
					break;
				}
				setState(1014);
				match(SEMI);
				}
				break;
			}
			setState(1018);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1017);
				expression(0);
				}
			}

			setState(1020);
			match(L_CURLY);
			setState(1024);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1021);
				exprCaseClause();
				}
				}
				setState(1026);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1027);
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
		enterRule(_localctx, 146, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1029);
			match(SWITCH);
			setState(1034);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				{
				setState(1031);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(1030);
					simpleStmt();
					}
					break;
				}
				setState(1033);
				match(SEMI);
				}
				break;
			}
			setState(1036);
			typeSwitchGuard();
			setState(1037);
			match(L_CURLY);
			setState(1041);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1038);
				typeCaseClause();
				}
				}
				setState(1043);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1044);
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
		enterRule(_localctx, 148, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1047);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (OR - 121)) | (1L << (DIV - 121)) | (1L << (MOD - 121)) | (1L << (LSHIFT - 121)) | (1L << (RSHIFT - 121)) | (1L << (BIT_CLEAR - 121)) | (1L << (PLUS - 121)) | (1L << (MINUS - 121)) | (1L << (CARET - 121)) | (1L << (STAR - 121)) | (1L << (AMPERSAND - 121)))) != 0)) {
				{
				setState(1046);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (OR - 121)) | (1L << (DIV - 121)) | (1L << (MOD - 121)) | (1L << (LSHIFT - 121)) | (1L << (RSHIFT - 121)) | (1L << (BIT_CLEAR - 121)) | (1L << (PLUS - 121)) | (1L << (MINUS - 121)) | (1L << (CARET - 121)) | (1L << (STAR - 121)) | (1L << (AMPERSAND - 121)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1049);
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
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1051);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1052);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1053);
				if (!(lineTerminatorAhead())) throw new FailedPredicateException(this, "lineTerminatorAhead()");
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1054);
				if (!(checkPreviousTokenText("}"))) throw new FailedPredicateException(this, "checkPreviousTokenText(\"}\")");
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1055);
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
			setState(1058);
			match(PACKAGE);
			setState(1059);
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
			setState(1061);
			match(IMPORT);
			setState(1073);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1062);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1063);
				match(L_PAREN);
				setState(1069);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (IDENTIFIER - 97)) | (1L << (DOT - 97)) | (1L << (RAW_STRING_LIT - 97)) | (1L << (INTERPRETED_STRING_LIT - 97)))) != 0)) {
					{
					{
					setState(1064);
					importSpec();
					setState(1065);
					eos();
					}
					}
					setState(1071);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1072);
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
			setState(1076);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1075);
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

			setState(1078);
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
			setState(1080);
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
			setState(1085);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1082);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1083);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1084);
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
			setState(1087);
			match(CONST);
			setState(1099);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1088);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1089);
				match(L_PAREN);
				setState(1095);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1090);
					constSpec();
					setState(1091);
					eos();
					}
					}
					setState(1097);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1098);
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
			setState(1101);
			identifierList();
			setState(1107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				{
				setState(1103);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & ((1L << (GHOST - 22)) | (1L << (SEQ - 22)) | (1L << (SET - 22)) | (1L << (MSET - 22)) | (1L << (DICT - 22)) | (1L << (OPT - 22)) | (1L << (PRED - 22)) | (1L << (BOOL - 22)) | (1L << (STRING - 22)) | (1L << (PERM - 22)) | (1L << (RUNE - 22)) | (1L << (INT - 22)) | (1L << (INT8 - 22)) | (1L << (INT16 - 22)) | (1L << (INT32 - 22)) | (1L << (INT64 - 22)) | (1L << (BYTE - 22)) | (1L << (UINT - 22)) | (1L << (UINT8 - 22)) | (1L << (UINT16 - 22)) | (1L << (UINT32 - 22)) | (1L << (UINT64 - 22)) | (1L << (UINTPTR - 22)) | (1L << (FUNC - 22)) | (1L << (INTERFACE - 22)) | (1L << (MAP - 22)) | (1L << (STRUCT - 22)) | (1L << (CHAN - 22)))) != 0) || ((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (IDENTIFIER - 97)) | (1L << (L_PAREN - 97)) | (1L << (L_BRACKET - 97)) | (1L << (STAR - 97)) | (1L << (RECEIVE - 97)))) != 0)) {
					{
					setState(1102);
					type_();
					}
				}

				setState(1105);
				match(ASSIGN);
				setState(1106);
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
			setState(1109);
			match(IDENTIFIER);
			setState(1114);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1110);
					match(COMMA);
					setState(1111);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1116);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
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
			setState(1117);
			expression(0);
			setState(1122);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1118);
					match(COMMA);
					setState(1119);
					expression(0);
					}
					} 
				}
				setState(1124);
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
			setState(1125);
			match(TYPE);
			setState(1137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1126);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1127);
				match(L_PAREN);
				setState(1133);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1128);
					typeSpec();
					setState(1129);
					eos();
					}
					}
					setState(1135);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1136);
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
			setState(1139);
			match(IDENTIFIER);
			setState(1141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1140);
				match(ASSIGN);
				}
			}

			setState(1143);
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
			setState(1145);
			match(VAR);
			setState(1157);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1146);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1147);
				match(L_PAREN);
				setState(1153);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1148);
					varSpec();
					setState(1149);
					eos();
					}
					}
					setState(1155);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1156);
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
			setState(1159);
			match(L_CURLY);
			setState(1161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1160);
				statementList();
				}
			}

			setState(1163);
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
			setState(1168); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1165);
				statement();
				setState(1166);
				eos();
				}
				}
				setState(1170); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0) );
			}
		}
		catch (RecognitionException re) {
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
			setState(1178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1172);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1173);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1174);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1175);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1176);
				shortVarDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1177);
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
			setState(1180);
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
			setState(1182);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1183);
			match(RECEIVE);
			setState(1184);
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
			setState(1186);
			expression(0);
			setState(1187);
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
			setState(1189);
			expressionList();
			setState(1190);
			assign_op();
			setState(1191);
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
			setState(1193);
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
			setState(1195);
			match(IDENTIFIER);
			setState(1196);
			match(COLON);
			setState(1198);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1197);
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
			setState(1200);
			match(RETURN);
			setState(1202);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				{
				setState(1201);
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
			setState(1204);
			match(BREAK);
			setState(1206);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				{
				setState(1205);
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
			setState(1208);
			match(CONTINUE);
			setState(1210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
			case 1:
				{
				setState(1209);
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
			setState(1212);
			match(GOTO);
			setState(1213);
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
			setState(1215);
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
			setState(1217);
			match(DEFER);
			setState(1218);
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
			setState(1222);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1220);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1221);
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
			setState(1224);
			exprSwitchCase();
			setState(1225);
			match(COLON);
			setState(1227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1226);
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
			setState(1232);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1229);
				match(CASE);
				setState(1230);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1231);
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
			setState(1236);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1234);
				match(IDENTIFIER);
				setState(1235);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1238);
			primaryExpr(0);
			setState(1239);
			match(DOT);
			setState(1240);
			match(L_PAREN);
			setState(1241);
			match(TYPE);
			setState(1242);
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
			setState(1244);
			typeSwitchCase();
			setState(1245);
			match(COLON);
			setState(1247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1246);
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
			setState(1252);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1249);
				match(CASE);
				setState(1250);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1251);
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
			setState(1256);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case PRED:
			case BOOL:
			case STRING:
			case PERM:
			case RUNE:
			case INT:
			case INT8:
			case INT16:
			case INT32:
			case INT64:
			case BYTE:
			case UINT:
			case UINT8:
			case UINT16:
			case UINT32:
			case UINT64:
			case UINTPTR:
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
				setState(1254);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1255);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1265);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1258);
				match(COMMA);
				setState(1261);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GHOST:
				case SEQ:
				case SET:
				case MSET:
				case DICT:
				case OPT:
				case PRED:
				case BOOL:
				case STRING:
				case PERM:
				case RUNE:
				case INT:
				case INT8:
				case INT16:
				case INT32:
				case INT64:
				case BYTE:
				case UINT:
				case UINT8:
				case UINT16:
				case UINT32:
				case UINT64:
				case UINTPTR:
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
					setState(1259);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1260);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1267);
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
			setState(1268);
			match(SELECT);
			setState(1269);
			match(L_CURLY);
			setState(1273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1270);
				commClause();
				}
				}
				setState(1275);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1276);
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
			setState(1278);
			commCase();
			setState(1279);
			match(COLON);
			setState(1281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1280);
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
			setState(1289);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1283);
				match(CASE);
				setState(1286);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
				case 1:
					{
					setState(1284);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1285);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1288);
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
			setState(1297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				{
				setState(1291);
				expressionList();
				setState(1292);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1294);
				identifierList();
				setState(1295);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1299);
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
			setState(1301);
			match(FOR);
			setState(1305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				{
				setState(1302);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1303);
				forClause();
				}
				break;
			case 3:
				{
				setState(1304);
				rangeClause();
				}
				break;
			}
			setState(1307);
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
			setState(1310);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1309);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1312);
			match(SEMI);
			setState(1314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1313);
				expression(0);
				}
			}

			setState(1316);
			match(SEMI);
			setState(1318);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1317);
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
			setState(1326);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
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
				identifierList();
				setState(1324);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1328);
			match(RANGE);
			setState(1329);
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
			setState(1331);
			match(GO);
			setState(1332);
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
			setState(1336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1334);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1335);
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
			setState(1338);
			match(L_BRACKET);
			setState(1339);
			arrayLength();
			setState(1340);
			match(R_BRACKET);
			setState(1341);
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
			setState(1343);
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
			setState(1345);
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
			setState(1347);
			match(STAR);
			setState(1348);
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
			setState(1350);
			match(L_BRACKET);
			setState(1351);
			match(R_BRACKET);
			setState(1352);
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
			setState(1354);
			match(MAP);
			setState(1355);
			match(L_BRACKET);
			setState(1356);
			type_();
			setState(1357);
			match(R_BRACKET);
			setState(1358);
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
			setState(1365);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				{
				setState(1360);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1361);
				match(CHAN);
				setState(1362);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1363);
				match(RECEIVE);
				setState(1364);
				match(CHAN);
				}
				break;
			}
			setState(1367);
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
			setState(1369);
			match(FUNC);
			setState(1370);
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
			setState(1377);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1372);
				if (!(noTerminatorAfterParams(1))) throw new FailedPredicateException(this, "noTerminatorAfterParams(1)");
				setState(1373);
				parameters();
				setState(1374);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1376);
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
			setState(1381);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1379);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1380);
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
			setState(1383);
			match(L_PAREN);
			setState(1395);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & ((1L << (GHOST - 22)) | (1L << (SEQ - 22)) | (1L << (SET - 22)) | (1L << (MSET - 22)) | (1L << (DICT - 22)) | (1L << (OPT - 22)) | (1L << (PRED - 22)) | (1L << (BOOL - 22)) | (1L << (STRING - 22)) | (1L << (PERM - 22)) | (1L << (RUNE - 22)) | (1L << (INT - 22)) | (1L << (INT8 - 22)) | (1L << (INT16 - 22)) | (1L << (INT32 - 22)) | (1L << (INT64 - 22)) | (1L << (BYTE - 22)) | (1L << (UINT - 22)) | (1L << (UINT8 - 22)) | (1L << (UINT16 - 22)) | (1L << (UINT32 - 22)) | (1L << (UINT64 - 22)) | (1L << (UINTPTR - 22)) | (1L << (FUNC - 22)) | (1L << (INTERFACE - 22)) | (1L << (MAP - 22)) | (1L << (STRUCT - 22)) | (1L << (CHAN - 22)))) != 0) || ((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (IDENTIFIER - 97)) | (1L << (L_PAREN - 97)) | (1L << (L_BRACKET - 97)) | (1L << (ELLIPSIS - 97)) | (1L << (STAR - 97)) | (1L << (RECEIVE - 97)))) != 0)) {
				{
				setState(1384);
				parameterDecl();
				setState(1389);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,138,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1385);
						match(COMMA);
						setState(1386);
						parameterDecl();
						}
						} 
					}
					setState(1391);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,138,_ctx);
				}
				setState(1393);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1392);
					match(COMMA);
					}
				}

				}
			}

			setState(1397);
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
			setState(1399);
			type_();
			setState(1400);
			match(L_PAREN);
			setState(1401);
			expression(0);
			setState(1403);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1402);
				match(COMMA);
				}
			}

			setState(1405);
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
			setState(1413);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1407);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1408);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1409);
				match(L_PAREN);
				setState(1410);
				expression(0);
				setState(1411);
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
			setState(1418);
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
				setState(1415);
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
				setState(1416);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1417);
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
			setState(1420);
			_la = _input.LA(1);
			if ( !(((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)))) != 0)) ) {
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
			setState(1422);
			match(IDENTIFIER);
			setState(1425);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				{
				setState(1423);
				match(DOT);
				setState(1424);
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
			setState(1427);
			match(IDENTIFIER);
			setState(1428);
			match(DOT);
			setState(1429);
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
			setState(1431);
			literalType();
			setState(1432);
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
			setState(1434);
			match(L_CURLY);
			setState(1439);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1435);
				elementList();
				setState(1437);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1436);
					match(COMMA);
					}
				}

				}
			}

			setState(1441);
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
			setState(1443);
			keyedElement();
			setState(1448);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,147,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1444);
					match(COMMA);
					setState(1445);
					keyedElement();
					}
					} 
				}
				setState(1450);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,147,_ctx);
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
			setState(1454);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				{
				setState(1451);
				key();
				setState(1452);
				match(COLON);
				}
				break;
			}
			setState(1456);
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
			setState(1461);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1458);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1459);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1460);
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
			setState(1465);
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
			case BOOL:
			case STRING:
			case PERM:
			case RUNE:
			case INT:
			case INT8:
			case INT16:
			case INT32:
			case INT64:
			case BYTE:
			case UINT:
			case UINT8:
			case UINT16:
			case UINT32:
			case UINT64:
			case UINTPTR:
			case FUNC:
			case INTERFACE:
			case MAP:
			case STRUCT:
			case CHAN:
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
			case FLOAT_LIT:
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1463);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1464);
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
			setState(1467);
			match(STRUCT);
			setState(1468);
			match(L_CURLY);
			setState(1474);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,151,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1469);
					fieldDecl();
					setState(1470);
					eos();
					}
					} 
				}
				setState(1476);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,151,_ctx);
			}
			setState(1477);
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
			setState(1484);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1479);
				if (!(noTerminatorBetween(2))) throw new FailedPredicateException(this, "noTerminatorBetween(2)");
				setState(1480);
				identifierList();
				setState(1481);
				type_();
				}
				break;
			case 2:
				{
				setState(1483);
				embeddedField();
				}
				break;
			}
			setState(1487);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				{
				setState(1486);
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
			setState(1489);
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
			setState(1492);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1491);
				match(STAR);
				}
			}

			setState(1494);
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
			setState(1496);
			match(FUNC);
			setState(1497);
			signature();
			setState(1498);
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
			setState(1500);
			match(L_BRACKET);
			setState(1501);
			expression(0);
			setState(1502);
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
			setState(1504);
			match(DOT);
			setState(1505);
			match(L_PAREN);
			setState(1506);
			type_();
			setState(1507);
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
			setState(1509);
			match(L_PAREN);
			setState(1524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << BOOL) | (1L << STRING) | (1L << PERM) | (1L << RUNE) | (1L << INT) | (1L << INT8) | (1L << INT16) | (1L << INT32))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (EXCLAMATION - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)) | (1L << (RECEIVE - 128)) | (1L << (DECIMAL_LIT - 128)) | (1L << (BINARY_LIT - 128)) | (1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (FLOAT_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1516);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
				case 1:
					{
					setState(1510);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1511);
					type_();
					setState(1514);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
					case 1:
						{
						setState(1512);
						match(COMMA);
						setState(1513);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1519);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1518);
					match(ELLIPSIS);
					}
				}

				setState(1522);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1521);
					match(COMMA);
					}
				}

				}
			}

			setState(1526);
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
			setState(1528);
			receiverType();
			setState(1529);
			match(DOT);
			setState(1530);
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
			setState(1532);
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
		case 10:
			return noTerminatorBetween(1);
		}
		return true;
	}
	private boolean methodSpec_sempred(MethodSpecContext _localctx, int predIndex) {
		switch (predIndex) {
		case 11:
			return noTerminatorAfterParams(2);
		}
		return true;
	}
	private boolean eos_sempred(EosContext _localctx, int predIndex) {
		switch (predIndex) {
		case 12:
			return lineTerminatorAhead();
		case 13:
			return checkPreviousTokenText("}");
		case 14:
			return checkPreviousTokenText(")");
		}
		return true;
	}
	private boolean signature_sempred(SignatureContext _localctx, int predIndex) {
		switch (predIndex) {
		case 15:
			return noTerminatorAfterParams(1);
		}
		return true;
	}
	private boolean fieldDecl_sempred(FieldDeclContext _localctx, int predIndex) {
		switch (predIndex) {
		case 16:
			return noTerminatorBetween(2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u009b\u0601\4\2\t"+
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
		"\16\2\u0139\13\2\3\3\3\3\5\3\u013d\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\5\4\u0147\n\4\3\5\3\5\3\5\7\5\u014c\n\5\f\5\16\5\u014f\13\5\3\5\5\5\u0152"+
		"\n\5\3\6\3\6\3\6\7\6\u0157\n\6\f\6\16\6\u015a\13\6\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\5\b\u0166\n\b\5\b\u0168\n\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u0182\n\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3"+
		"\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\7\20\u0199"+
		"\n\20\f\20\16\20\u019c\13\20\3\21\3\21\3\21\3\21\7\21\u01a2\n\21\f\21"+
		"\16\21\u01a5\13\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\5\22\u01ae\n\22"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\5\23\u01b6\n\23\3\24\3\24\3\25\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\5\27\u01c6\n\27\3\30\3\30"+
		"\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\5\31\u01d8\n\31\3\32\3\32\3\32\3\32\7\32\u01de\n\32\f\32\16\32\u01e1"+
		"\13\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\7\34\u01ec\n\34\f"+
		"\34\16\34\u01ef\13\34\3\34\3\34\3\34\5\34\u01f4\n\34\3\34\7\34\u01f7\n"+
		"\34\f\34\16\34\u01fa\13\34\5\34\u01fc\n\34\3\35\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\5\35\u0206\n\35\3\36\3\36\3\36\3\36\3\36\5\36\u020d\n\36"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u0215\n\37\3 \3 \3 \3 \3 \5 \u021c"+
		"\n \3 \5 \u021f\n \3 \3 \3!\3!\5!\u0225\n!\3\"\3\"\3\"\3\"\3\"\3\"\3\""+
		"\3#\3#\3#\3#\3#\7#\u0233\n#\f#\16#\u0236\13#\3#\3#\3#\3#\5#\u023c\n#\3"+
		"#\3#\7#\u0240\n#\f#\16#\u0243\13#\3#\3#\3$\3$\3$\3$\3$\3$\5$\u024d\n$"+
		"\3$\3$\3$\3$\3$\5$\u0254\n$\5$\u0256\n$\3%\3%\3%\3%\5%\u025c\n%\3&\3&"+
		"\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\5\'\u0268\n\'\3(\3(\3(\3(\3(\3(\3(\7(\u0271"+
		"\n(\f(\16(\u0274\13(\3(\3(\3(\7(\u0279\n(\f(\16(\u027c\13(\3(\3(\3)\5"+
		")\u0281\n)\3)\3)\3)\3)\5)\u0287\n)\3*\3*\3*\3*\3*\3*\3*\3*\5*\u0291\n"+
		"*\3+\3+\3+\3+\3+\5+\u0298\n+\3,\3,\3,\3,\5,\u029e\n,\3,\3,\5,\u02a2\n"+
		",\3-\3-\3-\3-\3.\3.\5.\u02aa\n.\3.\5.\u02ad\n.\3.\3.\3.\3/\3/\5/\u02b4"+
		"\n/\3/\5/\u02b7\n/\3/\3/\3/\3\60\5\60\u02bd\n\60\3\60\5\60\u02c0\n\60"+
		"\3\60\5\60\u02c3\n\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61"+
		"\3\61\5\61\u02d0\n\61\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\5\63\u02e8"+
		"\n\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\7\63\u0308\n\63\f\63\16\63\u030b\13\63\3\64\3\64\3\64"+
		"\3\64\3\64\5\64\u0312\n\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\66\3\66"+
		"\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66"+
		"\5\66\u032b\n\66\3\67\3\67\3\67\38\38\38\38\78\u0334\n8\f8\168\u0337\13"+
		"8\38\38\38\38\58\u033d\n8\39\59\u0340\n9\39\39\59\u0344\n9\3:\3:\3:\3"+
		":\3:\3:\3:\3:\5:\u034e\n:\3;\3;\3;\3;\3;\5;\u0355\n;\3;\3;\3;\3;\3;\3"+
		";\3;\3;\3;\3;\5;\u0361\n;\7;\u0363\n;\f;\16;\u0366\13;\3<\3<\5<\u036a"+
		"\n<\3<\5<\u036d\n<\3<\3<\3=\3=\3=\3=\3=\5=\u0376\n=\3=\3=\7=\u037a\n="+
		"\f=\16=\u037d\13=\3=\3=\3>\3>\3>\3>\3?\3?\5?\u0387\n?\3?\3?\3?\3?\3?\3"+
		"?\5?\u038f\n?\3?\3?\3?\3?\5?\u0395\n?\3@\3@\3@\3@\3@\3@\3@\3@\5@\u039f"+
		"\n@\3A\3A\3A\3A\3A\3A\3A\3A\3A\5A\u03aa\nA\3B\3B\3B\3C\3C\3C\3C\7C\u03b3"+
		"\nC\fC\16C\u03b6\13C\3C\5C\u03b9\nC\5C\u03bb\nC\3C\3C\3D\3D\3D\3D\3D\3"+
		"D\3D\3D\3D\3D\5D\u03c9\nD\3E\3E\5E\u03cd\nE\3E\3E\5E\u03d1\nE\3E\5E\u03d4"+
		"\nE\3E\3E\3E\3E\3E\5E\u03db\nE\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\5I\u03e7"+
		"\nI\3I\5I\u03ea\nI\3I\3I\3I\3I\3I\5I\u03f1\nI\5I\u03f3\nI\3J\3J\5J\u03f7"+
		"\nJ\3J\5J\u03fa\nJ\3J\5J\u03fd\nJ\3J\3J\7J\u0401\nJ\fJ\16J\u0404\13J\3"+
		"J\3J\3K\3K\5K\u040a\nK\3K\5K\u040d\nK\3K\3K\3K\7K\u0412\nK\fK\16K\u0415"+
		"\13K\3K\3K\3L\5L\u041a\nL\3L\3L\3M\3M\3M\3M\3M\5M\u0423\nM\3N\3N\3N\3"+
		"O\3O\3O\3O\3O\3O\7O\u042e\nO\fO\16O\u0431\13O\3O\5O\u0434\nO\3P\5P\u0437"+
		"\nP\3P\3P\3Q\3Q\3R\3R\3R\5R\u0440\nR\3S\3S\3S\3S\3S\3S\7S\u0448\nS\fS"+
		"\16S\u044b\13S\3S\5S\u044e\nS\3T\3T\5T\u0452\nT\3T\3T\5T\u0456\nT\3U\3"+
		"U\3U\7U\u045b\nU\fU\16U\u045e\13U\3V\3V\3V\7V\u0463\nV\fV\16V\u0466\13"+
		"V\3W\3W\3W\3W\3W\3W\7W\u046e\nW\fW\16W\u0471\13W\3W\5W\u0474\nW\3X\3X"+
		"\5X\u0478\nX\3X\3X\3Y\3Y\3Y\3Y\3Y\3Y\7Y\u0482\nY\fY\16Y\u0485\13Y\3Y\5"+
		"Y\u0488\nY\3Z\3Z\5Z\u048c\nZ\3Z\3Z\3[\3[\3[\6[\u0493\n[\r[\16[\u0494\3"+
		"\\\3\\\3\\\3\\\3\\\3\\\5\\\u049d\n\\\3]\3]\3^\3^\3^\3^\3_\3_\3_\3`\3`"+
		"\3`\3`\3a\3a\3b\3b\3b\5b\u04b1\nb\3c\3c\5c\u04b5\nc\3d\3d\5d\u04b9\nd"+
		"\3e\3e\5e\u04bd\ne\3f\3f\3f\3g\3g\3h\3h\3h\3i\3i\5i\u04c9\ni\3j\3j\3j"+
		"\5j\u04ce\nj\3k\3k\3k\5k\u04d3\nk\3l\3l\5l\u04d7\nl\3l\3l\3l\3l\3l\3l"+
		"\3m\3m\3m\5m\u04e2\nm\3n\3n\3n\5n\u04e7\nn\3o\3o\5o\u04eb\no\3o\3o\3o"+
		"\5o\u04f0\no\7o\u04f2\no\fo\16o\u04f5\13o\3p\3p\3p\7p\u04fa\np\fp\16p"+
		"\u04fd\13p\3p\3p\3q\3q\3q\5q\u0504\nq\3r\3r\3r\5r\u0509\nr\3r\5r\u050c"+
		"\nr\3s\3s\3s\3s\3s\3s\5s\u0514\ns\3s\3s\3t\3t\3t\3t\5t\u051c\nt\3t\3t"+
		"\3u\5u\u0521\nu\3u\3u\5u\u0525\nu\3u\3u\5u\u0529\nu\3v\3v\3v\3v\3v\3v"+
		"\5v\u0531\nv\3v\3v\3v\3w\3w\3w\3x\3x\5x\u053b\nx\3y\3y\3y\3y\3y\3z\3z"+
		"\3{\3{\3|\3|\3|\3}\3}\3}\3}\3~\3~\3~\3~\3~\3~\3\177\3\177\3\177\3\177"+
		"\3\177\5\177\u0558\n\177\3\177\3\177\3\u0080\3\u0080\3\u0080\3\u0081\3"+
		"\u0081\3\u0081\3\u0081\3\u0081\5\u0081\u0564\n\u0081\3\u0082\3\u0082\5"+
		"\u0082\u0568\n\u0082\3\u0083\3\u0083\3\u0083\3\u0083\7\u0083\u056e\n\u0083"+
		"\f\u0083\16\u0083\u0571\13\u0083\3\u0083\5\u0083\u0574\n\u0083\5\u0083"+
		"\u0576\n\u0083\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084\5\u0084"+
		"\u057e\n\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\5\u0085\u0588\n\u0085\3\u0086\3\u0086\3\u0086\5\u0086\u058d\n"+
		"\u0086\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088\5\u0088\u0594\n\u0088\3"+
		"\u0089\3\u0089\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3\u008b\3\u008b"+
		"\3\u008b\5\u008b\u05a0\n\u008b\5\u008b\u05a2\n\u008b\3\u008b\3\u008b\3"+
		"\u008c\3\u008c\3\u008c\7\u008c\u05a9\n\u008c\f\u008c\16\u008c\u05ac\13"+
		"\u008c\3\u008d\3\u008d\3\u008d\5\u008d\u05b1\n\u008d\3\u008d\3\u008d\3"+
		"\u008e\3\u008e\3\u008e\5\u008e\u05b8\n\u008e\3\u008f\3\u008f\5\u008f\u05bc"+
		"\n\u008f\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\7\u0090\u05c3\n\u0090"+
		"\f\u0090\16\u0090\u05c6\13\u0090\3\u0090\3\u0090\3\u0091\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\5\u0091\u05cf\n\u0091\3\u0091\5\u0091\u05d2\n\u0091\3"+
		"\u0092\3\u0092\3\u0093\5\u0093\u05d7\n\u0093\3\u0093\3\u0093\3\u0094\3"+
		"\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3\u0095\3\u0095\3\u0096\3\u0096"+
		"\3\u0096\3\u0096\3\u0096\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\5\u0097"+
		"\u05ed\n\u0097\5\u0097\u05ef\n\u0097\3\u0097\5\u0097\u05f2\n\u0097\3\u0097"+
		"\5\u0097\u05f5\n\u0097\5\u0097\u05f7\n\u0097\3\u0097\3\u0097\3\u0098\3"+
		"\u0098\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\2\4dt\u009a\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^"+
		"`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"+
		"\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120"+
		"\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\2\23\3\2\25\26\3\2\5"+
		"\b\3\2$&\4\2$&((\6\2##)),,//\3\2\u0081\u0087\4\2|\u0080\u0085\u0086\5"+
		"\2oo{{\u0082\u0084\3\2\34\36\3\2\31\33\3\2uz\3\2:I\4\2{\u0080\u0082\u0086"+
		"\4\2ccnn\3\2op\4\2\u0088\u008b\u008f\u0090\3\2\u0096\u0097\2\u0664\2\u0132"+
		"\3\2\2\2\4\u013a\3\2\2\2\6\u0146\3\2\2\2\b\u0148\3\2\2\2\n\u0153\3\2\2"+
		"\2\f\u015d\3\2\2\2\16\u015f\3\2\2\2\20\u016b\3\2\2\2\22\u016e\3\2\2\2"+
		"\24\u0181\3\2\2\2\26\u0183\3\2\2\2\30\u0188\3\2\2\2\32\u018d\3\2\2\2\34"+
		"\u0192\3\2\2\2\36\u019a\3\2\2\2 \u019d\3\2\2\2\"\u01a8\3\2\2\2$\u01b5"+
		"\3\2\2\2&\u01b7\3\2\2\2(\u01b9\3\2\2\2*\u01be\3\2\2\2,\u01c5\3\2\2\2."+
		"\u01c7\3\2\2\2\60\u01d7\3\2\2\2\62\u01d9\3\2\2\2\64\u01e4\3\2\2\2\66\u01fb"+
		"\3\2\2\28\u0205\3\2\2\2:\u0207\3\2\2\2<\u020e\3\2\2\2>\u0216\3\2\2\2@"+
		"\u0224\3\2\2\2B\u0226\3\2\2\2D\u022d\3\2\2\2F\u0255\3\2\2\2H\u0257\3\2"+
		"\2\2J\u025d\3\2\2\2L\u0262\3\2\2\2N\u0269\3\2\2\2P\u0280\3\2\2\2R\u0290"+
		"\3\2\2\2T\u0292\3\2\2\2V\u0299\3\2\2\2X\u02a3\3\2\2\2Z\u02a7\3\2\2\2\\"+
		"\u02b1\3\2\2\2^\u02bc\3\2\2\2`\u02cf\3\2\2\2b\u02d1\3\2\2\2d\u02e7\3\2"+
		"\2\2f\u030c\3\2\2\2h\u0315\3\2\2\2j\u032a\3\2\2\2l\u032c\3\2\2\2n\u0335"+
		"\3\2\2\2p\u033f\3\2\2\2r\u034d\3\2\2\2t\u0354\3\2\2\2v\u0367\3\2\2\2x"+
		"\u0370\3\2\2\2z\u0380\3\2\2\2|\u0394\3\2\2\2~\u039e\3\2\2\2\u0080\u03a9"+
		"\3\2\2\2\u0082\u03ab\3\2\2\2\u0084\u03ae\3\2\2\2\u0086\u03c8\3\2\2\2\u0088"+
		"\u03ca\3\2\2\2\u008a\u03de\3\2\2\2\u008c\u03e0\3\2\2\2\u008e\u03e2\3\2"+
		"\2\2\u0090\u03e4\3\2\2\2\u0092\u03f4\3\2\2\2\u0094\u0407\3\2\2\2\u0096"+
		"\u0419\3\2\2\2\u0098\u0422\3\2\2\2\u009a\u0424\3\2\2\2\u009c\u0427\3\2"+
		"\2\2\u009e\u0436\3\2\2\2\u00a0\u043a\3\2\2\2\u00a2\u043f\3\2\2\2\u00a4"+
		"\u0441\3\2\2\2\u00a6\u044f\3\2\2\2\u00a8\u0457\3\2\2\2\u00aa\u045f\3\2"+
		"\2\2\u00ac\u0467\3\2\2\2\u00ae\u0475\3\2\2\2\u00b0\u047b\3\2\2\2\u00b2"+
		"\u0489\3\2\2\2\u00b4\u0492\3\2\2\2\u00b6\u049c\3\2\2\2\u00b8\u049e\3\2"+
		"\2\2\u00ba\u04a0\3\2\2\2\u00bc\u04a4\3\2\2\2\u00be\u04a7\3\2\2\2\u00c0"+
		"\u04ab\3\2\2\2\u00c2\u04ad\3\2\2\2\u00c4\u04b2\3\2\2\2\u00c6\u04b6\3\2"+
		"\2\2\u00c8\u04ba\3\2\2\2\u00ca\u04be\3\2\2\2\u00cc\u04c1\3\2\2\2\u00ce"+
		"\u04c3\3\2\2\2\u00d0\u04c8\3\2\2\2\u00d2\u04ca\3\2\2\2\u00d4\u04d2\3\2"+
		"\2\2\u00d6\u04d6\3\2\2\2\u00d8\u04de\3\2\2\2\u00da\u04e6\3\2\2\2\u00dc"+
		"\u04ea\3\2\2\2\u00de\u04f6\3\2\2\2\u00e0\u0500\3\2\2\2\u00e2\u050b\3\2"+
		"\2\2\u00e4\u0513\3\2\2\2\u00e6\u0517\3\2\2\2\u00e8\u0520\3\2\2\2\u00ea"+
		"\u0530\3\2\2\2\u00ec\u0535\3\2\2\2\u00ee\u053a\3\2\2\2\u00f0\u053c\3\2"+
		"\2\2\u00f2\u0541\3\2\2\2\u00f4\u0543\3\2\2\2\u00f6\u0545\3\2\2\2\u00f8"+
		"\u0548\3\2\2\2\u00fa\u054c\3\2\2\2\u00fc\u0557\3\2\2\2\u00fe\u055b\3\2"+
		"\2\2\u0100\u0563\3\2\2\2\u0102\u0567\3\2\2\2\u0104\u0569\3\2\2\2\u0106"+
		"\u0579\3\2\2\2\u0108\u0587\3\2\2\2\u010a\u058c\3\2\2\2\u010c\u058e\3\2"+
		"\2\2\u010e\u0590\3\2\2\2\u0110\u0595\3\2\2\2\u0112\u0599\3\2\2\2\u0114"+
		"\u059c\3\2\2\2\u0116\u05a5\3\2\2\2\u0118\u05b0\3\2\2\2\u011a\u05b7\3\2"+
		"\2\2\u011c\u05bb\3\2\2\2\u011e\u05bd\3\2\2\2\u0120\u05ce\3\2\2\2\u0122"+
		"\u05d3\3\2\2\2\u0124\u05d6\3\2\2\2\u0126\u05da\3\2\2\2\u0128\u05de\3\2"+
		"\2\2\u012a\u05e2\3\2\2\2\u012c\u05e7\3\2\2\2\u012e\u05fa\3\2\2\2\u0130"+
		"\u05fe\3\2\2\2\u0132\u0137\5\4\3\2\u0133\u0134\7k\2\2\u0134\u0136\5\4"+
		"\3\2\u0135\u0133\3\2\2\2\u0136\u0139\3\2\2\2\u0137\u0135\3\2\2\2\u0137"+
		"\u0138\3\2\2\2\u0138\3\3\2\2\2\u0139\u0137\3\2\2\2\u013a\u013c\7c\2\2"+
		"\u013b\u013d\7\65\2\2\u013c\u013b\3\2\2\2\u013c\u013d\3\2\2\2\u013d\5"+
		"\3\2\2\2\u013e\u013f\7\30\2\2\u013f\u0147\5j\66\2\u0140\u0141\7\5\2\2"+
		"\u0141\u0147\5d\63\2\u0142\u0143\t\2\2\2\u0143\u0147\5\f\7\2\u0144\u0145"+
		"\t\3\2\2\u0145\u0147\5d\63\2\u0146\u013e\3\2\2\2\u0146\u0140\3\2\2\2\u0146"+
		"\u0142\3\2\2\2\u0146\u0144\3\2\2\2\u0147\7\3\2\2\2\u0148\u014d\5\n\6\2"+
		"\u0149\u014a\7k\2\2\u014a\u014c\5\n\6\2\u014b\u0149\3\2\2\2\u014c\u014f"+
		"\3\2\2\2\u014d\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u0151\3\2\2\2\u014f"+
		"\u014d\3\2\2\2\u0150\u0152\7k\2\2\u0151\u0150\3\2\2\2\u0151\u0152\3\2"+
		"\2\2\u0152\t\3\2\2\2\u0153\u0158\7c\2\2\u0154\u0155\7k\2\2\u0155\u0157"+
		"\7c\2\2\u0156\u0154\3\2\2\2\u0157\u015a\3\2\2\2\u0158\u0156\3\2\2\2\u0158"+
		"\u0159\3\2\2\2\u0159\u015b\3\2\2\2\u015a\u0158\3\2\2\2\u015b\u015c\5\u00f4"+
		"{\2\u015c\13\3\2\2\2\u015d\u015e\5t;\2\u015e\r\3\2\2\2\u015f\u0160\7\24"+
		"\2\2\u0160\u0161\7d\2\2\u0161\u0167\5d\63\2\u0162\u0165\7k\2\2\u0163\u0166"+
		"\7c\2\2\u0164\u0166\5d\63\2\u0165\u0163\3\2\2\2\u0165\u0164\3\2\2\2\u0166"+
		"\u0168\3\2\2\2\u0167\u0162\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u0169\3\2"+
		"\2\2\u0169\u016a\7e\2\2\u016a\17\3\2\2\2\u016b\u016c\5d\63\2\u016c\u016d"+
		"\7\2\2\3\u016d\21\3\2\2\2\u016e\u016f\5j\66\2\u016f\u0170\7\2\2\3\u0170"+
		"\23\3\2\2\2\u0171\u0182\5B\"\2\u0172\u0182\5\16\b\2\u0173\u0182\5(\25"+
		"\2\u0174\u0182\5*\26\2\u0175\u0182\5\"\22\2\u0176\u0182\5\34\17\2\u0177"+
		"\u0182\5\30\r\2\u0178\u0182\5\26\f\2\u0179\u0182\5\32\16\2\u017a\u017b"+
		"\7\22\2\2\u017b\u017c\5\b\5\2\u017c\u017d\7m\2\2\u017d\u017e\7m\2\2\u017e"+
		"\u017f\5\36\20\2\u017f\u0180\5d\63\2\u0180\u0182\3\2\2\2\u0181\u0171\3"+
		"\2\2\2\u0181\u0172\3\2\2\2\u0181\u0173\3\2\2\2\u0181\u0174\3\2\2\2\u0181"+
		"\u0175\3\2\2\2\u0181\u0176\3\2\2\2\u0181\u0177\3\2\2\2\u0181\u0178\3\2"+
		"\2\2\u0181\u0179\3\2\2\2\u0181\u017a\3\2\2\2\u0182\25\3\2\2\2\u0183\u0184"+
		"\7-\2\2\u0184\u0185\7d\2\2\u0185\u0186\5d\63\2\u0186\u0187\7e\2\2\u0187"+
		"\27\3\2\2\2\u0188\u0189\7\60\2\2\u0189\u018a\7h\2\2\u018a\u018b\5~@\2"+
		"\u018b\u018c\7i\2\2\u018c\31\3\2\2\2\u018d\u018e\7.\2\2\u018e\u018f\7"+
		"d\2\2\u018f\u0190\5d\63\2\u0190\u0191\7e\2\2\u0191\33\3\2\2\2\u0192\u0193"+
		"\t\4\2\2\u0193\u0194\7d\2\2\u0194\u0195\5d\63\2\u0195\u0196\7e\2\2\u0196"+
		"\35\3\2\2\2\u0197\u0199\5 \21\2\u0198\u0197\3\2\2\2\u0199\u019c\3\2\2"+
		"\2\u019a\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b\37\3\2\2\2\u019c\u019a"+
		"\3\2\2\2\u019d\u019e\7f\2\2\u019e\u01a3\5d\63\2\u019f\u01a0\7k\2\2\u01a0"+
		"\u01a2\5d\63\2\u01a1\u019f\3\2\2\2\u01a2\u01a5\3\2\2\2\u01a3\u01a1\3\2"+
		"\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a6\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6"+
		"\u01a7\7g\2\2\u01a7!\3\2\2\2\u01a8\u01ad\7\20\2\2\u01a9\u01aa\7h\2\2\u01aa"+
		"\u01ab\5$\23\2\u01ab\u01ac\7i\2\2\u01ac\u01ae\3\2\2\2\u01ad\u01a9\3\2"+
		"\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01af\3\2\2\2\u01af\u01b0\7d\2\2\u01b0"+
		"\u01b1\5d\63\2\u01b1\u01b2\7e\2\2\u01b2#\3\2\2\2\u01b3\u01b6\5&\24\2\u01b4"+
		"\u01b6\7\21\2\2\u01b5\u01b3\3\2\2\2\u01b5\u01b4\3\2\2\2\u01b6%\3\2\2\2"+
		"\u01b7\u01b8\7c\2\2\u01b8\'\3\2\2\2\u01b9\u01ba\7\62\2\2\u01ba\u01bb\7"+
		"d\2\2\u01bb\u01bc\5d\63\2\u01bc\u01bd\7e\2\2\u01bd)\3\2\2\2\u01be\u01bf"+
		"\7\63\2\2\u01bf\u01c0\7d\2\2\u01c0\u01c1\5d\63\2\u01c1\u01c2\7e\2\2\u01c2"+
		"+\3\2\2\2\u01c3\u01c6\5\60\31\2\u01c4\u01c6\5.\30\2\u01c5\u01c3\3\2\2"+
		"\2\u01c5\u01c4\3\2\2\2\u01c6-\3\2\2\2\u01c7\u01c8\7\30\2\2\u01c8\u01c9"+
		"\7h\2\2\u01c9\u01ca\7i\2\2\u01ca\u01cb\5\u00f4{\2\u01cb/\3\2\2\2\u01cc"+
		"\u01cd\t\5\2\2\u01cd\u01ce\7h\2\2\u01ce\u01cf\5~@\2\u01cf\u01d0\7i\2\2"+
		"\u01d0\u01d8\3\2\2\2\u01d1\u01d2\7\'\2\2\u01d2\u01d3\7h\2\2\u01d3\u01d4"+
		"\5~@\2\u01d4\u01d5\7i\2\2\u01d5\u01d6\5~@\2\u01d6\u01d8\3\2\2\2\u01d7"+
		"\u01cc\3\2\2\2\u01d7\u01d1\3\2\2\2\u01d8\61\3\2\2\2\u01d9\u01da\7h\2\2"+
		"\u01da\u01df\5\64\33\2\u01db\u01dc\7k\2\2\u01dc\u01de\5\64\33\2\u01dd"+
		"\u01db\3\2\2\2\u01de\u01e1\3\2\2\2\u01df\u01dd\3\2\2\2\u01df\u01e0\3\2"+
		"\2\2\u01e0\u01e2\3\2\2\2\u01e1\u01df\3\2\2\2\u01e2\u01e3\7i\2\2\u01e3"+
		"\63\3\2\2\2\u01e4\u01e5\5d\63\2\u01e5\u01e6\7j\2\2\u01e6\u01e7\5d\63\2"+
		"\u01e7\65\3\2\2\2\u01e8\u01e9\58\35\2\u01e9\u01ea\5\u0098M\2\u01ea\u01ec"+
		"\3\2\2\2\u01eb\u01e8\3\2\2\2\u01ec\u01ef\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ed"+
		"\u01ee\3\2\2\2\u01ee\u01f0\3\2\2\2\u01ef\u01ed\3\2\2\2\u01f0\u01fc\7\16"+
		"\2\2\u01f1\u01f4\58\35\2\u01f2\u01f4\7\16\2\2\u01f3\u01f1\3\2\2\2\u01f3"+
		"\u01f2\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5\u01f7\5\u0098M\2\u01f6\u01f3"+
		"\3\2\2\2\u01f7\u01fa\3\2\2\2\u01f8\u01f6\3\2\2\2\u01f8\u01f9\3\2\2\2\u01f9"+
		"\u01fc\3\2\2\2\u01fa\u01f8\3\2\2\2\u01fb\u01ed\3\2\2\2\u01fb\u01f8\3\2"+
		"\2\2\u01fc\67\3\2\2\2\u01fd\u01fe\7\t\2\2\u01fe\u0206\5@!\2\u01ff\u0200"+
		"\7\n\2\2\u0200\u0206\5@!\2\u0201\u0202\7\13\2\2\u0202\u0206\5@!\2\u0203"+
		"\u0204\7\r\2\2\u0204\u0206\5p9\2\u0205\u01fd\3\2\2\2\u0205\u01ff\3\2\2"+
		"\2\u0205\u0201\3\2\2\2\u0205\u0203\3\2\2\2\u02069\3\2\2\2\u0207\u0208"+
		"\5\66\34\2\u0208\u0209\7L\2\2\u0209\u020a\7c\2\2\u020a\u020c\5\u0100\u0081"+
		"\2\u020b\u020d\5> \2\u020c\u020b\3\2\2\2\u020c\u020d\3\2\2\2\u020d;\3"+
		"\2\2\2\u020e\u020f\5\66\34\2\u020f\u0210\7L\2\2\u0210\u0211\5Z.\2\u0211"+
		"\u0212\7c\2\2\u0212\u0214\5\u0100\u0081\2\u0213\u0215\5> \2\u0214\u0213"+
		"\3\2\2\2\u0214\u0215\3\2\2\2\u0215=\3\2\2\2\u0216\u021b\7f\2\2\u0217\u0218"+
		"\7\64\2\2\u0218\u0219\5\u00a8U\2\u0219\u021a\5\u0098M\2\u021a\u021c\3"+
		"\2\2\2\u021b\u0217\3\2\2\2\u021b\u021c\3\2\2\2\u021c\u021e\3\2\2\2\u021d"+
		"\u021f\5\u00b4[\2\u021e\u021d\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0220"+
		"\3\2\2\2\u0220\u0221\7g\2\2\u0221?\3\2\2\2\u0222\u0225\3\2\2\2\u0223\u0225"+
		"\5d\63\2\u0224\u0222\3\2\2\2\u0224\u0223\3\2\2\2\u0225A\3\2\2\2\u0226"+
		"\u0227\t\4\2\2\u0227\u0228\7h\2\2\u0228\u0229\5d\63\2\u0229\u022a\7\66"+
		"\2\2\u022a\u022b\5d\63\2\u022b\u022c\7i\2\2\u022cC\3\2\2\2\u022d\u022e"+
		"\5\u009aN\2\u022e\u0234\5\u0098M\2\u022f\u0230\5\u009cO\2\u0230\u0231"+
		"\5\u0098M\2\u0231\u0233\3\2\2\2\u0232\u022f\3\2\2\2\u0233\u0236\3\2\2"+
		"\2\u0234\u0232\3\2\2\2\u0234\u0235\3\2\2\2\u0235\u0241\3\2\2\2\u0236\u0234"+
		"\3\2\2\2\u0237\u023c\5:\36\2\u0238\u023c\5<\37\2\u0239\u023c\5\u00a2R"+
		"\2\u023a\u023c\5F$\2\u023b\u0237\3\2\2\2\u023b\u0238\3\2\2\2\u023b\u0239"+
		"\3\2\2\2\u023b\u023a\3\2\2\2\u023c\u023d\3\2\2\2\u023d\u023e\5\u0098M"+
		"\2\u023e\u0240\3\2\2\2\u023f\u023b\3\2\2\2\u0240\u0243\3\2\2\2\u0241\u023f"+
		"\3\2\2\2\u0241\u0242\3\2\2\2\u0242\u0244\3\2\2\2\u0243\u0241\3\2\2\2\u0244"+
		"\u0245\7\2\2\3\u0245E\3\2\2\2\u0246\u0256\5H%\2\u0247\u0256\5L\'\2\u0248"+
		"\u0256\5N(\2\u0249\u024d\7\30\2\2\u024a\u024b\7\30\2\2\u024b\u024d\5\u0098"+
		"M\2\u024c\u0249\3\2\2\2\u024c\u024a\3\2\2\2\u024d\u0253\3\2\2\2\u024e"+
		"\u0254\5<\37\2\u024f\u0254\5:\36\2\u0250\u0254\5\u00a4S\2\u0251\u0254"+
		"\5\u00acW\2\u0252\u0254\5\u00b0Y\2\u0253\u024e\3\2\2\2\u0253\u024f\3\2"+
		"\2\2\u0253\u0250\3\2\2\2\u0253\u0251\3\2\2\2\u0253\u0252\3\2\2\2\u0254"+
		"\u0256\3\2\2\2\u0255\u0246\3\2\2\2\u0255\u0247\3\2\2\2\u0255\u0248\3\2"+
		"\2\2\u0255\u024c\3\2\2\2\u0256G\3\2\2\2\u0257\u0258\7\61\2\2\u0258\u0259"+
		"\7c\2\2\u0259\u025b\5\u0104\u0083\2\u025a\u025c\5J&\2\u025b\u025a\3\2"+
		"\2\2\u025b\u025c\3\2\2\2\u025cI\3\2\2\2\u025d\u025e\7f\2\2\u025e\u025f"+
		"\5d\63\2\u025f\u0260\5\u0098M\2\u0260\u0261\7g\2\2\u0261K\3\2\2\2\u0262"+
		"\u0263\7\61\2\2\u0263\u0264\5Z.\2\u0264\u0265\7c\2\2\u0265\u0267\5\u0104"+
		"\u0083\2\u0266\u0268\5J&\2\u0267\u0266\3\2\2\2\u0267\u0268\3\2\2\2\u0268"+
		"M\3\2\2\2\u0269\u026a\5~@\2\u026a\u026b\7\17\2\2\u026b\u026c\5~@\2\u026c"+
		"\u0272\7f\2\2\u026d\u026e\5T+\2\u026e\u026f\5\u0098M\2\u026f\u0271\3\2"+
		"\2\2\u0270\u026d\3\2\2\2\u0271\u0274\3\2\2\2\u0272\u0270\3\2\2\2\u0272"+
		"\u0273\3\2\2\2\u0273\u027a\3\2\2\2\u0274\u0272\3\2\2\2\u0275\u0276\5P"+
		")\2\u0276\u0277\5\u0098M\2\u0277\u0279\3\2\2\2\u0278\u0275\3\2\2\2\u0279"+
		"\u027c\3\2\2\2\u027a\u0278\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027d\3\2"+
		"\2\2\u027c\u027a\3\2\2\2\u027d\u027e\7g\2\2\u027eO\3\2\2\2\u027f\u0281"+
		"\7\16\2\2\u0280\u027f\3\2\2\2\u0280\u0281\3\2\2\2\u0281\u0282\3\2\2\2"+
		"\u0282\u0283\5\\/\2\u0283\u0284\7c\2\2\u0284\u0286\5\u0100\u0081\2\u0285"+
		"\u0287\5\u00b2Z\2\u0286\u0285\3\2\2\2\u0286\u0287\3\2\2\2\u0287Q\3\2\2"+
		"\2\u0288\u0289\5t;\2\u0289\u028a\7n\2\2\u028a\u028b\7c\2\2\u028b\u0291"+
		"\3\2\2\2\u028c\u028d\5~@\2\u028d\u028e\7n\2\2\u028e\u028f\7c\2\2\u028f"+
		"\u0291\3\2\2\2\u0290\u0288\3\2\2\2\u0290\u028c\3\2\2\2\u0291S\3\2\2\2"+
		"\u0292\u0293\7\61\2\2\u0293\u0294\7c\2\2\u0294\u0297\7q\2\2\u0295\u0298"+
		"\5R*\2\u0296\u0298\5\u010e\u0088\2\u0297\u0295\3\2\2\2\u0297\u0296\3\2"+
		"\2\2\u0298U\3\2\2\2\u0299\u02a1\5\2\2\2\u029a\u029d\5~@\2\u029b\u029c"+
		"\7j\2\2\u029c\u029e\5\u00aaV\2\u029d\u029b\3\2\2\2\u029d\u029e\3\2\2\2"+
		"\u029e\u02a2\3\2\2\2\u029f\u02a0\7j\2\2\u02a0\u02a2\5\u00aaV\2\u02a1\u029a"+
		"\3\2\2\2\u02a1\u029f\3\2\2\2\u02a2W\3\2\2\2\u02a3\u02a4\5\2\2\2\u02a4"+
		"\u02a5\7q\2\2\u02a5\u02a6\5\u00aaV\2\u02a6Y\3\2\2\2\u02a7\u02a9\7d\2\2"+
		"\u02a8\u02aa\5\4\3\2\u02a9\u02a8\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02ac"+
		"\3\2\2\2\u02ab\u02ad\7\u0085\2\2\u02ac\u02ab\3\2\2\2\u02ac\u02ad\3\2\2"+
		"\2\u02ad\u02ae\3\2\2\2\u02ae\u02af\7c\2\2\u02af\u02b0\7e\2\2\u02b0[\3"+
		"\2\2\2\u02b1\u02b3\7d\2\2\u02b2\u02b4\7c\2\2\u02b3\u02b2\3\2\2\2\u02b3"+
		"\u02b4\3\2\2\2\u02b4\u02b6\3\2\2\2\u02b5\u02b7\7\u0085\2\2\u02b6\u02b5"+
		"\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02b9\5\u00eex"+
		"\2\u02b9\u02ba\7e\2\2\u02ba]\3\2\2\2\u02bb\u02bd\7\30\2\2\u02bc\u02bb"+
		"\3\2\2\2\u02bc\u02bd\3\2\2\2\u02bd\u02bf\3\2\2\2\u02be\u02c0\5\u00a8U"+
		"\2\u02bf\u02be\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0\u02c2\3\2\2\2\u02c1\u02c3"+
		"\7r\2\2\u02c2\u02c1\3\2\2\2\u02c2\u02c3\3\2\2\2\u02c3\u02c4\3\2\2\2\u02c4"+
		"\u02c5\5~@\2\u02c5_\3\2\2\2\u02c6\u02d0\5t;\2\u02c7\u02c8\t\6\2\2\u02c8"+
		"\u02c9\7d\2\2\u02c9\u02ca\5d\63\2\u02ca\u02cb\7e\2\2\u02cb\u02d0\3\2\2"+
		"\2\u02cc\u02d0\5b\62\2\u02cd\u02ce\t\7\2\2\u02ce\u02d0\5d\63\2\u02cf\u02c6"+
		"\3\2\2\2\u02cf\u02c7\3\2\2\2\u02cf\u02cc\3\2\2\2\u02cf\u02cd\3\2\2\2\u02d0"+
		"a\3\2\2\2\u02d1\u02d2\7\27\2\2\u02d2\u02d3\5\f\7\2\u02d3\u02d4\7\31\2"+
		"\2\u02d4\u02d5\5d\63\2\u02d5c\3\2\2\2\u02d6\u02d7\b\63\1\2\u02d7\u02d8"+
		"\7\\\2\2\u02d8\u02d9\7h\2\2\u02d9\u02da\5~@\2\u02da\u02db\7i\2\2\u02db"+
		"\u02e8\3\2\2\2\u02dc\u02dd\t\6\2\2\u02dd\u02de\7d\2\2\u02de\u02df\5d\63"+
		"\2\u02df\u02e0\7e\2\2\u02e0\u02e8\3\2\2\2\u02e1\u02e8\5b\62\2\u02e2\u02e8"+
		"\5h\65\2\u02e3\u02e8\5f\64\2\u02e4\u02e5\t\7\2\2\u02e5\u02e8\5d\63\r\u02e6"+
		"\u02e8\5t;\2\u02e7\u02d6\3\2\2\2\u02e7\u02dc\3\2\2\2\u02e7\u02e1\3\2\2"+
		"\2\u02e7\u02e2\3\2\2\2\u02e7\u02e3\3\2\2\2\u02e7\u02e4\3\2\2\2\u02e7\u02e6"+
		"\3\2\2\2\u02e8\u0309\3\2\2\2\u02e9\u02ea\f\13\2\2\u02ea\u02eb\t\b\2\2"+
		"\u02eb\u0308\5d\63\f\u02ec\u02ed\f\n\2\2\u02ed\u02ee\t\t\2\2\u02ee\u0308"+
		"\5d\63\13\u02ef\u02f0\f\t\2\2\u02f0\u02f1\t\n\2\2\u02f1\u0308\5d\63\n"+
		"\u02f2\u02f3\f\b\2\2\u02f3\u02f4\t\13\2\2\u02f4\u0308\5d\63\t\u02f5\u02f6"+
		"\f\7\2\2\u02f6\u02f7\t\f\2\2\u02f7\u0308\5d\63\b\u02f8\u02f9\f\6\2\2\u02f9"+
		"\u02fa\7t\2\2\u02fa\u0308\5d\63\7\u02fb\u02fc\f\5\2\2\u02fc\u02fd\7s\2"+
		"\2\u02fd\u0308\5d\63\6\u02fe\u02ff\f\4\2\2\u02ff\u0300\7\37\2\2\u0300"+
		"\u0308\5d\63\4\u0301\u0302\f\3\2\2\u0302\u0303\7 \2\2\u0303\u0304\5d\63"+
		"\2\u0304\u0305\7m\2\2\u0305\u0306\5d\63\3\u0306\u0308\3\2\2\2\u0307\u02e9"+
		"\3\2\2\2\u0307\u02ec\3\2\2\2\u0307\u02ef\3\2\2\2\u0307\u02f2\3\2\2\2\u0307"+
		"\u02f5\3\2\2\2\u0307\u02f8\3\2\2\2\u0307\u02fb\3\2\2\2\u0307\u02fe\3\2"+
		"\2\2\u0307\u0301\3\2\2\2\u0308\u030b\3\2\2\2\u0309\u0307\3\2\2\2\u0309"+
		"\u030a\3\2\2\2\u030ae\3\2\2\2\u030b\u0309\3\2\2\2\u030c\u030d\7+\2\2\u030d"+
		"\u030e\7d\2\2\u030e\u0311\5~@\2\u030f\u0310\7k\2\2\u0310\u0312\5\u00aa"+
		"V\2\u0311\u030f\3\2\2\2\u0311\u0312\3\2\2\2\u0312\u0313\3\2\2\2\u0313"+
		"\u0314\7e\2\2\u0314g\3\2\2\2\u0315\u0316\7*\2\2\u0316\u0317\7d\2\2\u0317"+
		"\u0318\5~@\2\u0318\u0319\7e\2\2\u0319i\3\2\2\2\u031a\u032b\5\6\4\2\u031b"+
		"\u032b\5\u00a2R\2\u031c\u032b\5\u00c2b\2\u031d\u032b\5\u00b6\\\2\u031e"+
		"\u032b\5\u00ecw\2\u031f\u032b\5\u00c4c\2\u0320\u032b\5\u00c6d\2\u0321"+
		"\u032b\5\u00c8e\2\u0322\u032b\5\u00caf\2\u0323\u032b\5\u00ccg\2\u0324"+
		"\u032b\5\u00b2Z\2\u0325\u032b\5\u0090I\2\u0326\u032b\5\u00d0i\2\u0327"+
		"\u032b\5\u00dep\2\u0328\u032b\5l\67\2\u0329\u032b\5\u00ceh\2\u032a\u031a"+
		"\3\2\2\2\u032a\u031b\3\2\2\2\u032a\u031c\3\2\2\2\u032a\u031d\3\2\2\2\u032a"+
		"\u031e\3\2\2\2\u032a\u031f\3\2\2\2\u032a\u0320\3\2\2\2\u032a\u0321\3\2"+
		"\2\2\u032a\u0322\3\2\2\2\u032a\u0323\3\2\2\2\u032a\u0324\3\2\2\2\u032a"+
		"\u0325\3\2\2\2\u032a\u0326\3\2\2\2\u032a\u0327\3\2\2\2\u032a\u0328\3\2"+
		"\2\2\u032a\u0329\3\2\2\2\u032bk\3\2\2\2\u032c\u032d\5n8\2\u032d\u032e"+
		"\5\u00e6t\2\u032em\3\2\2\2\u032f\u0330\7\f\2\2\u0330\u0331\5d\63\2\u0331"+
		"\u0332\5\u0098M\2\u0332\u0334\3\2\2\2\u0333\u032f\3\2\2\2\u0334\u0337"+
		"\3\2\2\2\u0335\u0333\3\2\2\2\u0335\u0336\3\2\2\2\u0336\u033c\3\2\2\2\u0337"+
		"\u0335\3\2\2\2\u0338\u0339\7\r\2\2\u0339\u033a\5p9\2\u033a\u033b\5\u0098"+
		"M\2\u033b\u033d\3\2\2\2\u033c\u0338\3\2\2\2\u033c\u033d\3\2\2\2\u033d"+
		"o\3\2\2\2\u033e\u0340\5\u00aaV\2\u033f\u033e\3\2\2\2\u033f\u0340\3\2\2"+
		"\2\u0340\u0343\3\2\2\2\u0341\u0342\7[\2\2\u0342\u0344\5d\63\2\u0343\u0341"+
		"\3\2\2\2\u0343\u0344\3\2\2\2\u0344q\3\2\2\2\u0345\u034e\7\3\2\2\u0346"+
		"\u034e\7\4\2\2\u0347\u034e\7b\2\2\u0348\u034e\5\u010c\u0087\2\u0349\u034e"+
		"\5\u0122\u0092\2\u034a\u034e\7\u008c\2\2\u034b\u034e\7\u008f\2\2\u034c"+
		"\u034e\7\u0090\2\2\u034d\u0345\3\2\2\2\u034d\u0346\3\2\2\2\u034d\u0347"+
		"\3\2\2\2\u034d\u0348\3\2\2\2\u034d\u0349\3\2\2\2\u034d\u034a\3\2\2\2\u034d"+
		"\u034b\3\2\2\2\u034d\u034c\3\2\2\2\u034es\3\2\2\2\u034f\u0350\b;\1\2\u0350"+
		"\u0355\5\u0108\u0085\2\u0351\u0355\5\u0106\u0084\2\u0352\u0355\5\u012e"+
		"\u0098\2\u0353\u0355\5\24\13\2\u0354\u034f\3\2\2\2\u0354\u0351\3\2\2\2"+
		"\u0354\u0352\3\2\2\2\u0354\u0353\3\2\2\2\u0355\u0364\3\2\2\2\u0356\u0360"+
		"\f\3\2\2\u0357\u0358\7n\2\2\u0358\u0361\7c\2\2\u0359\u0361\5\u0128\u0095"+
		"\2\u035a\u0361\5\u0088E\2\u035b\u0361\5\62\32\2\u035c\u0361\5\u012a\u0096"+
		"\2\u035d\u035e\6;\f\2\u035e\u0361\5\u012c\u0097\2\u035f\u0361\5v<\2\u0360"+
		"\u0357\3\2\2\2\u0360\u0359\3\2\2\2\u0360\u035a\3\2\2\2\u0360\u035b\3\2"+
		"\2\2\u0360\u035c\3\2\2\2\u0360\u035d\3\2\2\2\u0360\u035f\3\2\2\2\u0361"+
		"\u0363\3\2\2\2\u0362\u0356\3\2\2\2\u0363\u0366\3\2\2\2\u0364\u0362\3\2"+
		"\2\2\u0364\u0365\3\2\2\2\u0365u\3\2\2\2\u0366\u0364\3\2\2\2\u0367\u0369"+
		"\7!\2\2\u0368\u036a\5\u00aaV\2\u0369\u0368\3\2\2\2\u0369\u036a\3\2\2\2"+
		"\u036a\u036c\3\2\2\2\u036b\u036d\7k\2\2\u036c\u036b\3\2\2\2\u036c\u036d"+
		"\3\2\2\2\u036d\u036e\3\2\2\2\u036e\u036f\7\"\2\2\u036fw\3\2\2\2\u0370"+
		"\u0371\7M\2\2\u0371\u037b\7f\2\2\u0372\u0376\5|?\2\u0373\u0376\5\u00ee"+
		"x\2\u0374\u0376\5z>\2\u0375\u0372\3\2\2\2\u0375\u0373\3\2\2\2\u0375\u0374"+
		"\3\2\2\2\u0376\u0377\3\2\2\2\u0377\u0378\5\u0098M\2\u0378\u037a\3\2\2"+
		"\2\u0379\u0375\3\2\2\2\u037a\u037d\3\2\2\2\u037b\u0379\3\2\2\2\u037b\u037c"+
		"\3\2\2\2\u037c\u037e\3\2\2\2\u037d\u037b\3\2\2\2\u037e\u037f\7g\2\2\u037f"+
		"y\3\2\2\2\u0380\u0381\7\61\2\2\u0381\u0382\7c\2\2\u0382\u0383\5\u0104"+
		"\u0083\2\u0383{\3\2\2\2\u0384\u0386\6?\r\2\u0385\u0387\7\30\2\2\u0386"+
		"\u0385\3\2\2\2\u0386\u0387\3\2\2\2\u0387\u0388\3\2\2\2\u0388\u0389\5\66"+
		"\34\2\u0389\u038a\7c\2\2\u038a\u038b\5\u0104\u0083\2\u038b\u038c\5\u0102"+
		"\u0082\2\u038c\u0395\3\2\2\2\u038d\u038f\7\30\2\2\u038e\u038d\3\2\2\2"+
		"\u038e\u038f\3\2\2\2\u038f\u0390\3\2\2\2\u0390\u0391\5\66\34\2\u0391\u0392"+
		"\7c\2\2\u0392\u0393\5\u0104\u0083\2\u0393\u0395\3\2\2\2\u0394\u0384\3"+
		"\2\2\2\u0394\u038e\3\2\2\2\u0395}\3\2\2\2\u0396\u039f\5\u00eex\2\u0397"+
		"\u039f\5\u0080A\2\u0398\u039f\5,\27\2\u0399\u039a\7d\2\2\u039a\u039b\5"+
		"~@\2\u039b\u039c\7e\2\2\u039c\u039f\3\2\2\2\u039d\u039f\t\r\2\2\u039e"+
		"\u0396\3\2\2\2\u039e\u0397\3\2\2\2\u039e\u0398\3\2\2\2\u039e\u0399\3\2"+
		"\2\2\u039e\u039d\3\2\2\2\u039f\177\3\2\2\2\u03a0\u03aa\5\u00f0y\2\u03a1"+
		"\u03aa\5\u011e\u0090\2\u03a2\u03aa\5\u00f6|\2\u03a3\u03aa\5\u00fe\u0080"+
		"\2\u03a4\u03aa\5x=\2\u03a5\u03aa\5\u00f8}\2\u03a6\u03aa\5\u00fa~\2\u03a7"+
		"\u03aa\5\u00fc\177\2\u03a8\u03aa\5\u0082B\2\u03a9\u03a0\3\2\2\2\u03a9"+
		"\u03a1\3\2\2\2\u03a9\u03a2\3\2\2\2\u03a9\u03a3\3\2\2\2\u03a9\u03a4\3\2"+
		"\2\2\u03a9\u03a5\3\2\2\2\u03a9\u03a6\3\2\2\2\u03a9\u03a7\3\2\2\2\u03a9"+
		"\u03a8\3\2\2\2\u03aa\u0081\3\2\2\2\u03ab\u03ac\7\61\2\2\u03ac\u03ad\5"+
		"\u0084C\2\u03ad\u0083\3\2\2\2\u03ae\u03ba\7d\2\2\u03af\u03b4\5~@\2\u03b0"+
		"\u03b1\7k\2\2\u03b1\u03b3\5~@\2\u03b2\u03b0\3\2\2\2\u03b3\u03b6\3\2\2"+
		"\2\u03b4\u03b2\3\2\2\2\u03b4\u03b5\3\2\2\2\u03b5\u03b8\3\2\2\2\u03b6\u03b4"+
		"\3\2\2\2\u03b7\u03b9\7k\2\2\u03b8\u03b7\3\2\2\2\u03b8\u03b9\3\2\2\2\u03b9"+
		"\u03bb\3\2\2\2\u03ba\u03af\3\2\2\2\u03ba\u03bb\3\2\2\2\u03bb\u03bc\3\2"+
		"\2\2\u03bc\u03bd\7e\2\2\u03bd\u0085\3\2\2\2\u03be\u03c9\5\u011e\u0090"+
		"\2\u03bf\u03c9\5\u00f0y\2\u03c0\u03c1\7h\2\2\u03c1\u03c2\7r\2\2\u03c2"+
		"\u03c3\7i\2\2\u03c3\u03c9\5\u00f4{\2\u03c4\u03c9\5\u00f8}\2\u03c5\u03c9"+
		"\5\u00fa~\2\u03c6\u03c9\5,\27\2\u03c7\u03c9\5\u00eex\2\u03c8\u03be\3\2"+
		"\2\2\u03c8\u03bf\3\2\2\2\u03c8\u03c0\3\2\2\2\u03c8\u03c4\3\2\2\2\u03c8"+
		"\u03c5\3\2\2\2\u03c8\u03c6\3\2\2\2\u03c8\u03c7\3\2\2\2\u03c9\u0087\3\2"+
		"\2\2\u03ca\u03da\7h\2\2\u03cb\u03cd\5\u008aF\2\u03cc\u03cb\3\2\2\2\u03cc"+
		"\u03cd\3\2\2\2\u03cd\u03ce\3\2\2\2\u03ce\u03d0\7m\2\2\u03cf\u03d1\5\u008c"+
		"G\2\u03d0\u03cf\3\2\2\2\u03d0\u03d1\3\2\2\2\u03d1\u03db\3\2\2\2\u03d2"+
		"\u03d4\5\u008aF\2\u03d3\u03d2\3\2\2\2\u03d3\u03d4\3\2\2\2\u03d4\u03d5"+
		"\3\2\2\2\u03d5\u03d6\7m\2\2\u03d6\u03d7\5\u008cG\2\u03d7\u03d8\7m\2\2"+
		"\u03d8\u03d9\5\u008eH\2\u03d9\u03db\3\2\2\2\u03da\u03cc\3\2\2\2\u03da"+
		"\u03d3\3\2\2\2\u03db\u03dc\3\2\2\2\u03dc\u03dd\7i\2\2\u03dd\u0089\3\2"+
		"\2\2\u03de\u03df\5d\63\2\u03df\u008b\3\2\2\2\u03e0\u03e1\5d\63\2\u03e1"+
		"\u008d\3\2\2\2\u03e2\u03e3\5d\63\2\u03e3\u008f\3\2\2\2\u03e4\u03e9\7["+
		"\2\2\u03e5\u03e7\5\u00b6\\\2\u03e6\u03e5\3\2\2\2\u03e6\u03e7\3\2\2\2\u03e7"+
		"\u03e8\3\2\2\2\u03e8\u03ea\7l\2\2\u03e9\u03e6\3\2\2\2\u03e9\u03ea\3\2"+
		"\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ec\5d\63\2\u03ec\u03f2\5\u00b2Z\2\u03ed"+
		"\u03f0\7U\2\2\u03ee\u03f1\5\u0090I\2\u03ef\u03f1\5\u00b2Z\2\u03f0\u03ee"+
		"\3\2\2\2\u03f0\u03ef\3\2\2\2\u03f1\u03f3\3\2\2\2\u03f2\u03ed\3\2\2\2\u03f2"+
		"\u03f3\3\2\2\2\u03f3\u0091\3\2\2\2\u03f4\u03f9\7X\2\2\u03f5\u03f7\5\u00b6"+
		"\\\2\u03f6\u03f5\3\2\2\2\u03f6\u03f7\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8"+
		"\u03fa\7l\2\2\u03f9\u03f6\3\2\2\2\u03f9\u03fa\3\2\2\2\u03fa\u03fc\3\2"+
		"\2\2\u03fb\u03fd\5d\63\2\u03fc\u03fb\3\2\2\2\u03fc\u03fd\3\2\2\2\u03fd"+
		"\u03fe\3\2\2\2\u03fe\u0402\7f\2\2\u03ff\u0401\5\u00d2j\2\u0400\u03ff\3"+
		"\2\2\2\u0401\u0404\3\2\2\2\u0402\u0400\3\2\2\2\u0402\u0403\3\2\2\2\u0403"+
		"\u0405\3\2\2\2\u0404\u0402\3\2\2\2\u0405\u0406\7g\2\2\u0406\u0093\3\2"+
		"\2\2\u0407\u040c\7X\2\2\u0408\u040a\5\u00b6\\\2\u0409\u0408\3\2\2\2\u0409"+
		"\u040a\3\2\2\2\u040a\u040b\3\2\2\2\u040b\u040d\7l\2\2\u040c\u0409\3\2"+
		"\2\2\u040c\u040d\3\2\2\2\u040d\u040e\3\2\2\2\u040e\u040f\5\u00d6l\2\u040f"+
		"\u0413\7f\2\2\u0410\u0412\5\u00d8m\2\u0411\u0410\3\2\2\2\u0412\u0415\3"+
		"\2\2\2\u0413\u0411\3\2\2\2\u0413\u0414\3\2\2\2\u0414\u0416\3\2\2\2\u0415"+
		"\u0413\3\2\2\2\u0416\u0417\7g\2\2\u0417\u0095\3\2\2\2\u0418\u041a\t\16"+
		"\2\2\u0419\u0418\3\2\2\2\u0419\u041a\3\2\2\2\u041a\u041b\3\2\2\2\u041b"+
		"\u041c\7j\2\2\u041c\u0097\3\2\2\2\u041d\u0423\7l\2\2\u041e\u0423\7\2\2"+
		"\3\u041f\u0423\6M\16\2\u0420\u0423\6M\17\2\u0421\u0423\6M\20\2\u0422\u041d"+
		"\3\2\2\2\u0422\u041e\3\2\2\2\u0422\u041f\3\2\2\2\u0422\u0420\3\2\2\2\u0422"+
		"\u0421\3\2\2\2\u0423\u0099\3\2\2\2\u0424\u0425\7W\2\2\u0425\u0426\7c\2"+
		"\2\u0426\u009b\3\2\2\2\u0427\u0433\7_\2\2\u0428\u0434\5\u009eP\2\u0429"+
		"\u042f\7d\2\2\u042a\u042b\5\u009eP\2\u042b\u042c\5\u0098M\2\u042c\u042e"+
		"\3\2\2\2\u042d\u042a\3\2\2\2\u042e\u0431\3\2\2\2\u042f\u042d\3\2\2\2\u042f"+
		"\u0430\3\2\2\2\u0430\u0432\3\2\2\2\u0431\u042f\3\2\2\2\u0432\u0434\7e"+
		"\2\2\u0433\u0428\3\2\2\2\u0433\u0429\3\2\2\2\u0434\u009d\3\2\2\2\u0435"+
		"\u0437\t\17\2\2\u0436\u0435\3\2\2\2\u0436\u0437\3\2\2\2\u0437\u0438\3"+
		"\2\2\2\u0438\u0439\5\u00a0Q\2\u0439\u009f\3\2\2\2\u043a\u043b\5\u0122"+
		"\u0092\2\u043b\u00a1\3\2\2\2\u043c\u0440\5\u00a4S\2\u043d\u0440\5\u00ac"+
		"W\2\u043e\u0440\5\u00b0Y\2\u043f\u043c\3\2\2\2\u043f\u043d\3\2\2\2\u043f"+
		"\u043e\3\2\2\2\u0440\u00a3\3\2\2\2\u0441\u044d\7Y\2\2\u0442\u044e\5\u00a6"+
		"T\2\u0443\u0449\7d\2\2\u0444\u0445\5\u00a6T\2\u0445\u0446\5\u0098M\2\u0446"+
		"\u0448\3\2\2\2\u0447\u0444\3\2\2\2\u0448\u044b\3\2\2\2\u0449\u0447\3\2"+
		"\2\2\u0449\u044a\3\2\2\2\u044a\u044c\3\2\2\2\u044b\u0449\3\2\2\2\u044c"+
		"\u044e\7e\2\2\u044d\u0442\3\2\2\2\u044d\u0443\3\2\2\2\u044e\u00a5\3\2"+
		"\2\2\u044f\u0455\5\u00a8U\2\u0450\u0452\5~@\2\u0451\u0450\3\2\2\2\u0451"+
		"\u0452\3\2\2\2\u0452\u0453\3\2\2\2\u0453\u0454\7j\2\2\u0454\u0456\5\u00aa"+
		"V\2\u0455\u0451\3\2\2\2\u0455\u0456\3\2\2\2\u0456\u00a7\3\2\2\2\u0457"+
		"\u045c\7c\2\2\u0458\u0459\7k\2\2\u0459\u045b\7c\2\2\u045a\u0458\3\2\2"+
		"\2\u045b\u045e\3\2\2\2\u045c\u045a\3\2\2\2\u045c\u045d\3\2\2\2\u045d\u00a9"+
		"\3\2\2\2\u045e\u045c\3\2\2\2\u045f\u0464\5d\63\2\u0460\u0461\7k\2\2\u0461"+
		"\u0463\5d\63\2\u0462\u0460\3\2\2\2\u0463\u0466\3\2\2\2\u0464\u0462\3\2"+
		"\2\2\u0464\u0465\3\2\2\2\u0465\u00ab\3\2\2\2\u0466\u0464\3\2\2\2\u0467"+
		"\u0473\7\\\2\2\u0468\u0474\5\u00aeX\2\u0469\u046f\7d\2\2\u046a\u046b\5"+
		"\u00aeX\2\u046b\u046c\5\u0098M\2\u046c\u046e\3\2\2\2\u046d\u046a\3\2\2"+
		"\2\u046e\u0471\3\2\2\2\u046f\u046d\3\2\2\2\u046f\u0470\3\2\2\2\u0470\u0472"+
		"\3\2\2\2\u0471\u046f\3\2\2\2\u0472\u0474\7e\2\2\u0473\u0468\3\2\2\2\u0473"+
		"\u0469\3\2\2\2\u0474\u00ad\3\2\2\2\u0475\u0477\7c\2\2\u0476\u0478\7j\2"+
		"\2\u0477\u0476\3\2\2\2\u0477\u0478\3\2\2\2\u0478\u0479\3\2\2\2\u0479\u047a"+
		"\5~@\2\u047a\u00af\3\2\2\2\u047b\u0487\7a\2\2\u047c\u0488\5V,\2\u047d"+
		"\u0483\7d\2\2\u047e\u047f\5V,\2\u047f\u0480\5\u0098M\2\u0480\u0482\3\2"+
		"\2\2\u0481\u047e\3\2\2\2\u0482\u0485\3\2\2\2\u0483\u0481\3\2\2\2\u0483"+
		"\u0484\3\2\2\2\u0484\u0486\3\2\2\2\u0485\u0483\3\2\2\2\u0486\u0488\7e"+
		"\2\2\u0487\u047c\3\2\2\2\u0487\u047d\3\2\2\2\u0488\u00b1\3\2\2\2\u0489"+
		"\u048b\7f\2\2\u048a\u048c\5\u00b4[\2\u048b\u048a\3\2\2\2\u048b\u048c\3"+
		"\2\2\2\u048c\u048d\3\2\2\2\u048d\u048e\7g\2\2\u048e\u00b3\3\2\2\2\u048f"+
		"\u0490\5j\66\2\u0490\u0491\5\u0098M\2\u0491\u0493\3\2\2\2\u0492\u048f"+
		"\3\2\2\2\u0493\u0494\3\2\2\2\u0494\u0492\3\2\2\2\u0494\u0495\3\2\2\2\u0495"+
		"\u00b5\3\2\2\2\u0496\u049d\5\u00ba^\2\u0497\u049d\5\u00bc_\2\u0498\u049d"+
		"\5\u00be`\2\u0499\u049d\5\u00b8]\2\u049a\u049d\5X-\2\u049b\u049d\5\u00c0"+
		"a\2\u049c\u0496\3\2\2\2\u049c\u0497\3\2\2\2\u049c\u0498\3\2\2\2\u049c"+
		"\u0499\3\2\2\2\u049c\u049a\3\2\2\2\u049c\u049b\3\2\2\2\u049d\u00b7\3\2"+
		"\2\2\u049e\u049f\5d\63\2\u049f\u00b9\3\2\2\2\u04a0\u04a1\5d\63\2\u04a1"+
		"\u04a2\7\u0087\2\2\u04a2\u04a3\5d\63\2\u04a3\u00bb\3\2\2\2\u04a4\u04a5"+
		"\5d\63\2\u04a5\u04a6\t\20\2\2\u04a6\u00bd\3\2\2\2\u04a7\u04a8\5\u00aa"+
		"V\2\u04a8\u04a9\5\u0096L\2\u04a9\u04aa\5\u00aaV\2\u04aa\u00bf\3\2\2\2"+
		"\u04ab\u04ac\7l\2\2\u04ac\u00c1\3\2\2\2\u04ad\u04ae\7c\2\2\u04ae\u04b0"+
		"\7m\2\2\u04af\u04b1\5j\66\2\u04b0\u04af\3\2\2\2\u04b0\u04b1\3\2\2\2\u04b1"+
		"\u00c3\3\2\2\2\u04b2\u04b4\7`\2\2\u04b3\u04b5\5\u00aaV\2\u04b4\u04b3\3"+
		"\2\2\2\u04b4\u04b5\3\2\2\2\u04b5\u00c5\3\2\2\2\u04b6\u04b8\7J\2\2\u04b7"+
		"\u04b9\7c\2\2\u04b8\u04b7\3\2\2\2\u04b8\u04b9\3\2\2\2\u04b9\u00c7\3\2"+
		"\2\2\u04ba\u04bc\7]\2\2\u04bb\u04bd\7c\2\2\u04bc\u04bb\3\2\2\2\u04bc\u04bd"+
		"\3\2\2\2\u04bd\u00c9\3\2\2\2\u04be\u04bf\7V\2\2\u04bf\u04c0\7c\2\2\u04c0"+
		"\u00cb\3\2\2\2\u04c1\u04c2\7Z\2\2\u04c2\u00cd\3\2\2\2\u04c3\u04c4\7P\2"+
		"\2\u04c4\u04c5\5d\63\2\u04c5\u00cf\3\2\2\2\u04c6\u04c9\5\u0092J\2\u04c7"+
		"\u04c9\5\u0094K\2\u04c8\u04c6\3\2\2\2\u04c8\u04c7\3\2\2\2\u04c9\u00d1"+
		"\3\2\2\2\u04ca\u04cb\5\u00d4k\2\u04cb\u04cd\7m\2\2\u04cc\u04ce\5\u00b4"+
		"[\2\u04cd\u04cc\3\2\2\2\u04cd\u04ce\3\2\2\2\u04ce\u00d3\3\2\2\2\u04cf"+
		"\u04d0\7O\2\2\u04d0\u04d3\5\u00aaV\2\u04d1\u04d3\7K\2\2\u04d2\u04cf\3"+
		"\2\2\2\u04d2\u04d1\3\2\2\2\u04d3\u00d5\3\2\2\2\u04d4\u04d5\7c\2\2\u04d5"+
		"\u04d7\7q\2\2\u04d6\u04d4\3\2\2\2\u04d6\u04d7\3\2\2\2\u04d7\u04d8\3\2"+
		"\2\2\u04d8\u04d9\5t;\2\u04d9\u04da\7n\2\2\u04da\u04db\7d\2\2\u04db\u04dc"+
		"\7\\\2\2\u04dc\u04dd\7e\2\2\u04dd\u00d7\3\2\2\2\u04de\u04df\5\u00dan\2"+
		"\u04df\u04e1\7m\2\2\u04e0\u04e2\5\u00b4[\2\u04e1\u04e0\3\2\2\2\u04e1\u04e2"+
		"\3\2\2\2\u04e2\u00d9\3\2\2\2\u04e3\u04e4\7O\2\2\u04e4\u04e7\5\u00dco\2"+
		"\u04e5\u04e7\7K\2\2\u04e6\u04e3\3\2\2\2\u04e6\u04e5\3\2\2\2\u04e7\u00db"+
		"\3\2\2\2\u04e8\u04eb\5~@\2\u04e9\u04eb\7b\2\2\u04ea\u04e8\3\2\2\2\u04ea"+
		"\u04e9\3\2\2\2\u04eb\u04f3\3\2\2\2\u04ec\u04ef\7k\2\2\u04ed\u04f0\5~@"+
		"\2\u04ee\u04f0\7b\2\2\u04ef\u04ed\3\2\2\2\u04ef\u04ee\3\2\2\2\u04f0\u04f2"+
		"\3\2\2\2\u04f1\u04ec\3\2\2\2\u04f2\u04f5\3\2\2\2\u04f3\u04f1\3\2\2\2\u04f3"+
		"\u04f4\3\2\2\2\u04f4\u00dd\3\2\2\2\u04f5\u04f3\3\2\2\2\u04f6\u04f7\7N"+
		"\2\2\u04f7\u04fb\7f\2\2\u04f8\u04fa\5\u00e0q\2\u04f9\u04f8\3\2\2\2\u04fa"+
		"\u04fd\3\2\2\2\u04fb\u04f9\3\2\2\2\u04fb\u04fc\3\2\2\2\u04fc\u04fe\3\2"+
		"\2\2\u04fd\u04fb\3\2\2\2\u04fe\u04ff\7g\2\2\u04ff\u00df\3\2\2\2\u0500"+
		"\u0501\5\u00e2r\2\u0501\u0503\7m\2\2\u0502\u0504\5\u00b4[\2\u0503\u0502"+
		"\3\2\2\2\u0503\u0504\3\2\2\2\u0504\u00e1\3\2\2\2\u0505\u0508\7O\2\2\u0506"+
		"\u0509\5\u00ba^\2\u0507\u0509\5\u00e4s\2\u0508\u0506\3\2\2\2\u0508\u0507"+
		"\3\2\2\2\u0509\u050c\3\2\2\2\u050a\u050c\7K\2\2\u050b\u0505\3\2\2\2\u050b"+
		"\u050a\3\2\2\2\u050c\u00e3\3\2\2\2\u050d\u050e\5\u00aaV\2\u050e\u050f"+
		"\7j\2\2\u050f\u0514\3\2\2\2\u0510\u0511\5\u00a8U\2\u0511\u0512\7q\2\2"+
		"\u0512\u0514\3\2\2\2\u0513\u050d\3\2\2\2\u0513\u0510\3\2\2\2\u0513\u0514"+
		"\3\2\2\2\u0514\u0515\3\2\2\2\u0515\u0516\5d\63\2\u0516\u00e5\3\2\2\2\u0517"+
		"\u051b\7^\2\2\u0518\u051c\5d\63\2\u0519\u051c\5\u00e8u\2\u051a\u051c\5"+
		"\u00eav\2\u051b\u0518\3\2\2\2\u051b\u0519\3\2\2\2\u051b\u051a\3\2\2\2"+
		"\u051b\u051c\3\2\2\2\u051c\u051d\3\2\2\2\u051d\u051e\5\u00b2Z\2\u051e"+
		"\u00e7\3\2\2\2\u051f\u0521\5\u00b6\\\2\u0520\u051f\3\2\2\2\u0520\u0521"+
		"\3\2\2\2\u0521\u0522\3\2\2\2\u0522\u0524\7l\2\2\u0523\u0525\5d\63\2\u0524"+
		"\u0523\3\2\2\2\u0524\u0525\3\2\2\2\u0525\u0526\3\2\2\2\u0526\u0528\7l"+
		"\2\2\u0527\u0529\5\u00b6\\\2\u0528\u0527\3\2\2\2\u0528\u0529\3\2\2\2\u0529"+
		"\u00e9\3\2\2\2\u052a\u052b\5\u00aaV\2\u052b\u052c\7j\2\2\u052c\u0531\3"+
		"\2\2\2\u052d\u052e\5\u00a8U\2\u052e\u052f\7q\2\2\u052f\u0531\3\2\2\2\u0530"+
		"\u052a\3\2\2\2\u0530\u052d\3\2\2\2\u0530\u0531\3\2\2\2\u0531\u0532\3\2"+
		"\2\2\u0532\u0533\7#\2\2\u0533\u0534\5d\63\2\u0534\u00eb\3\2\2\2\u0535"+
		"\u0536\7Q\2\2\u0536\u0537\5d\63\2\u0537\u00ed\3\2\2\2\u0538\u053b\5\u0110"+
		"\u0089\2\u0539\u053b\7c\2\2\u053a\u0538\3\2\2\2\u053a\u0539\3\2\2\2\u053b"+
		"\u00ef\3\2\2\2\u053c\u053d\7h\2\2\u053d\u053e\5\u00f2z\2\u053e\u053f\7"+
		"i\2\2\u053f\u0540\5\u00f4{\2\u0540\u00f1\3\2\2\2\u0541\u0542\5d\63\2\u0542"+
		"\u00f3\3\2\2\2\u0543\u0544\5~@\2\u0544\u00f5\3\2\2\2\u0545\u0546\7\u0085"+
		"\2\2\u0546\u0547\5~@\2\u0547\u00f7\3\2\2\2\u0548\u0549\7h\2\2\u0549\u054a"+
		"\7i\2\2\u054a\u054b\5\u00f4{\2\u054b\u00f9\3\2\2\2\u054c\u054d\7R\2\2"+
		"\u054d\u054e\7h\2\2\u054e\u054f\5~@\2\u054f\u0550\7i\2\2\u0550\u0551\5"+
		"\u00f4{\2\u0551\u00fb\3\2\2\2\u0552\u0558\7T\2\2\u0553\u0554\7T\2\2\u0554"+
		"\u0558\7\u0087\2\2\u0555\u0556\7\u0087\2\2\u0556\u0558\7T\2\2\u0557\u0552"+
		"\3\2\2\2\u0557\u0553\3\2\2\2\u0557\u0555\3\2\2\2\u0558\u0559\3\2\2\2\u0559"+
		"\u055a\5\u00f4{\2\u055a\u00fd\3\2\2\2\u055b\u055c\7L\2\2\u055c\u055d\5"+
		"\u0100\u0081\2\u055d\u00ff\3\2\2\2\u055e\u055f\6\u0081\21\2\u055f\u0560"+
		"\5\u0104\u0083\2\u0560\u0561\5\u0102\u0082\2\u0561\u0564\3\2\2\2\u0562"+
		"\u0564\5\u0104\u0083\2\u0563\u055e\3\2\2\2\u0563\u0562\3\2\2\2\u0564\u0101"+
		"\3\2\2\2\u0565\u0568\5\u0104\u0083\2\u0566\u0568\5~@\2\u0567\u0565\3\2"+
		"\2\2\u0567\u0566\3\2\2\2\u0568\u0103\3\2\2\2\u0569\u0575\7d\2\2\u056a"+
		"\u056f\5^\60\2\u056b\u056c\7k\2\2\u056c\u056e\5^\60\2\u056d\u056b\3\2"+
		"\2\2\u056e\u0571\3\2\2\2\u056f\u056d\3\2\2\2\u056f\u0570\3\2\2\2\u0570"+
		"\u0573\3\2\2\2\u0571\u056f\3\2\2\2\u0572\u0574\7k\2\2\u0573\u0572\3\2"+
		"\2\2\u0573\u0574\3\2\2\2\u0574\u0576\3\2\2\2\u0575\u056a\3\2\2\2\u0575"+
		"\u0576\3\2\2\2\u0576\u0577\3\2\2\2\u0577\u0578\7e\2\2\u0578\u0105\3\2"+
		"\2\2\u0579\u057a\5~@\2\u057a\u057b\7d\2\2\u057b\u057d\5d\63\2\u057c\u057e"+
		"\7k\2\2\u057d\u057c\3\2\2\2\u057d\u057e\3\2\2\2\u057e\u057f\3\2\2\2\u057f"+
		"\u0580\7e\2\2\u0580\u0107\3\2\2\2\u0581\u0588\5\u010a\u0086\2\u0582\u0588"+
		"\5\u010e\u0088\2\u0583\u0584\7d\2\2\u0584\u0585\5d\63\2\u0585\u0586\7"+
		"e\2\2\u0586\u0588\3\2\2\2\u0587\u0581\3\2\2\2\u0587\u0582\3\2\2\2\u0587"+
		"\u0583\3\2\2\2\u0588\u0109\3\2\2\2\u0589\u058d\5r:\2\u058a\u058d\5\u0112"+
		"\u008a\2\u058b\u058d\5\u0126\u0094\2\u058c\u0589\3\2\2\2\u058c\u058a\3"+
		"\2\2\2\u058c\u058b\3\2\2\2\u058d\u010b\3\2\2\2\u058e\u058f\t\21\2\2\u058f"+
		"\u010d\3\2\2\2\u0590\u0593\7c\2\2\u0591\u0592\7n\2\2\u0592\u0594\7c\2"+
		"\2\u0593\u0591\3\2\2\2\u0593\u0594\3\2\2\2\u0594\u010f\3\2\2\2\u0595\u0596"+
		"\7c\2\2\u0596\u0597\7n\2\2\u0597\u0598\7c\2\2\u0598\u0111\3\2\2\2\u0599"+
		"\u059a\5\u0086D\2\u059a\u059b\5\u0114\u008b\2\u059b\u0113\3\2\2\2\u059c"+
		"\u05a1\7f\2\2\u059d\u059f\5\u0116\u008c\2\u059e\u05a0\7k\2\2\u059f\u059e"+
		"\3\2\2\2\u059f\u05a0\3\2\2\2\u05a0\u05a2\3\2\2\2\u05a1\u059d\3\2\2\2\u05a1"+
		"\u05a2\3\2\2\2\u05a2\u05a3\3\2\2\2\u05a3\u05a4\7g\2\2\u05a4\u0115\3\2"+
		"\2\2\u05a5\u05aa\5\u0118\u008d\2\u05a6\u05a7\7k\2\2\u05a7\u05a9\5\u0118"+
		"\u008d\2\u05a8\u05a6\3\2\2\2\u05a9\u05ac\3\2\2\2\u05aa\u05a8\3\2\2\2\u05aa"+
		"\u05ab\3\2\2\2\u05ab\u0117\3\2\2\2\u05ac\u05aa\3\2\2\2\u05ad\u05ae\5\u011a"+
		"\u008e\2\u05ae\u05af\7m\2\2\u05af\u05b1\3\2\2\2\u05b0\u05ad\3\2\2\2\u05b0"+
		"\u05b1\3\2\2\2\u05b1\u05b2\3\2\2\2\u05b2\u05b3\5\u011c\u008f\2\u05b3\u0119"+
		"\3\2\2\2\u05b4\u05b8\7c\2\2\u05b5\u05b8\5d\63\2\u05b6\u05b8\5\u0114\u008b"+
		"\2\u05b7\u05b4\3\2\2\2\u05b7\u05b5\3\2\2\2\u05b7\u05b6\3\2\2\2\u05b8\u011b"+
		"\3\2\2\2\u05b9\u05bc\5d\63\2\u05ba\u05bc\5\u0114\u008b\2\u05bb\u05b9\3"+
		"\2\2\2\u05bb\u05ba\3\2\2\2\u05bc\u011d\3\2\2\2\u05bd\u05be\7S\2\2\u05be"+
		"\u05c4\7f\2\2\u05bf\u05c0\5\u0120\u0091\2\u05c0\u05c1\5\u0098M\2\u05c1"+
		"\u05c3\3\2\2\2\u05c2\u05bf\3\2\2\2\u05c3\u05c6\3\2\2\2\u05c4\u05c2\3\2"+
		"\2\2\u05c4\u05c5\3\2\2\2\u05c5\u05c7\3\2\2\2\u05c6\u05c4\3\2\2\2\u05c7"+
		"\u05c8\7g\2\2\u05c8\u011f\3\2\2\2\u05c9\u05ca\6\u0091\22\2\u05ca\u05cb"+
		"\5\u00a8U\2\u05cb\u05cc\5~@\2\u05cc\u05cf\3\2\2\2\u05cd\u05cf\5\u0124"+
		"\u0093\2\u05ce\u05c9\3\2\2\2\u05ce\u05cd\3\2\2\2\u05cf\u05d1\3\2\2\2\u05d0"+
		"\u05d2\5\u0122\u0092\2\u05d1\u05d0\3\2\2\2\u05d1\u05d2\3\2\2\2\u05d2\u0121"+
		"\3\2\2\2\u05d3\u05d4\t\22\2\2\u05d4\u0123\3\2\2\2\u05d5\u05d7\7\u0085"+
		"\2\2\u05d6\u05d5\3\2\2\2\u05d6\u05d7\3\2\2\2\u05d7\u05d8\3\2\2\2\u05d8"+
		"\u05d9\5\u00eex\2\u05d9\u0125\3\2\2\2\u05da\u05db\7L\2\2\u05db\u05dc\5"+
		"\u0100\u0081\2\u05dc\u05dd\5\u00b2Z\2\u05dd\u0127\3\2\2\2\u05de\u05df"+
		"\7h\2\2\u05df\u05e0\5d\63\2\u05e0\u05e1\7i\2\2\u05e1\u0129\3\2\2\2\u05e2"+
		"\u05e3\7n\2\2\u05e3\u05e4\7d\2\2\u05e4\u05e5\5~@\2\u05e5\u05e6\7e\2\2"+
		"\u05e6\u012b\3\2\2\2\u05e7\u05f6\7d\2\2\u05e8\u05ef\5\u00aaV\2\u05e9\u05ec"+
		"\5~@\2\u05ea\u05eb\7k\2\2\u05eb\u05ed\5\u00aaV\2\u05ec\u05ea\3\2\2\2\u05ec"+
		"\u05ed\3\2\2\2\u05ed\u05ef\3\2\2\2\u05ee\u05e8\3\2\2\2\u05ee\u05e9\3\2"+
		"\2\2\u05ef\u05f1\3\2\2\2\u05f0\u05f2\7r\2\2\u05f1\u05f0\3\2\2\2\u05f1"+
		"\u05f2\3\2\2\2\u05f2\u05f4\3\2\2\2\u05f3\u05f5\7k\2\2\u05f4\u05f3\3\2"+
		"\2\2\u05f4\u05f5\3\2\2\2\u05f5\u05f7\3\2\2\2\u05f6\u05ee\3\2\2\2\u05f6"+
		"\u05f7\3\2\2\2\u05f7\u05f8\3\2\2\2\u05f8\u05f9\7e\2\2\u05f9\u012d\3\2"+
		"\2\2\u05fa\u05fb\5\u0130\u0099\2\u05fb\u05fc\7n\2\2\u05fc\u05fd\7c\2\2"+
		"\u05fd\u012f\3\2\2\2\u05fe\u05ff\5~@\2\u05ff\u0131\3\2\2\2\u00a2\u0137"+
		"\u013c\u0146\u014d\u0151\u0158\u0165\u0167\u0181\u019a\u01a3\u01ad\u01b5"+
		"\u01c5\u01d7\u01df\u01ed\u01f3\u01f8\u01fb\u0205\u020c\u0214\u021b\u021e"+
		"\u0224\u0234\u023b\u0241\u024c\u0253\u0255\u025b\u0267\u0272\u027a\u0280"+
		"\u0286\u0290\u0297\u029d\u02a1\u02a9\u02ac\u02b3\u02b6\u02bc\u02bf\u02c2"+
		"\u02cf\u02e7\u0307\u0309\u0311\u032a\u0335\u033c\u033f\u0343\u034d\u0354"+
		"\u0360\u0364\u0369\u036c\u0375\u037b\u0386\u038e\u0394\u039e\u03a9\u03b4"+
		"\u03b8\u03ba\u03c8\u03cc\u03d0\u03d3\u03da\u03e6\u03e9\u03f0\u03f2\u03f6"+
		"\u03f9\u03fc\u0402\u0409\u040c\u0413\u0419\u0422\u042f\u0433\u0436\u043f"+
		"\u0449\u044d\u0451\u0455\u045c\u0464\u046f\u0473\u0477\u0483\u0487\u048b"+
		"\u0494\u049c\u04b0\u04b4\u04b8\u04bc\u04c8\u04cd\u04d2\u04d6\u04e1\u04e6"+
		"\u04ea\u04ef\u04f3\u04fb\u0503\u0508\u050b\u0513\u051b\u0520\u0524\u0528"+
		"\u0530\u053a\u0557\u0563\u0567\u056f\u0573\u0575\u057d\u0587\u058c\u0593"+
		"\u059f\u05a1\u05aa\u05b0\u05b7\u05bb\u05c4\u05ce\u05d1\u05d6\u05ec\u05ee"+
		"\u05f1\u05f4\u05f6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}