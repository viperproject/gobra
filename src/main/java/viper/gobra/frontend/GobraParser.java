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
		SUBSET=25, UNION=26, INTERSECTION=27, SETMINUS=28, IMPLIES=29, WAND=30, 
		APPLY=31, QMARK=32, L_PRED=33, R_PRED=34, RANGE=35, SEQ=36, SET=37, MSET=38, 
		DICT=39, OPT=40, LEN=41, NEW=42, MAKE=43, CAP=44, SOME=45, GET=46, DOM=47, 
		AXIOM=48, NONE=49, PRED=50, TYPE_OF=51, IS_COMPARABLE=52, SHARE=53, ADDR_MOD=54, 
		DOT_DOT=55, SHARED=56, EXCLUSIVE=57, PREDICATE=58, WRITEPERM=59, NOPERM=60, 
		BOOL=61, STRING=62, PERM=63, RUNE=64, INT=65, INT8=66, INT16=67, INT32=68, 
		INT64=69, BYTE=70, UINT=71, UINT8=72, UINT16=73, UINT32=74, UINT64=75, 
		UINTPTR=76, BREAK=77, DEFAULT=78, FUNC=79, INTERFACE=80, SELECT=81, CASE=82, 
		DEFER=83, GO=84, MAP=85, STRUCT=86, CHAN=87, ELSE=88, GOTO=89, PACKAGE=90, 
		SWITCH=91, CONST=92, FALLTHROUGH=93, IF=94, TYPE=95, CONTINUE=96, FOR=97, 
		IMPORT=98, RETURN=99, VAR=100, NIL_LIT=101, IDENTIFIER=102, L_PAREN=103, 
		R_PAREN=104, L_CURLY=105, R_CURLY=106, L_BRACKET=107, R_BRACKET=108, ASSIGN=109, 
		COMMA=110, SEMI=111, COLON=112, DOT=113, PLUS_PLUS=114, MINUS_MINUS=115, 
		DECLARE_ASSIGN=116, ELLIPSIS=117, LOGICAL_OR=118, LOGICAL_AND=119, EQUALS=120, 
		NOT_EQUALS=121, LESS=122, LESS_OR_EQUALS=123, GREATER=124, GREATER_OR_EQUALS=125, 
		OR=126, DIV=127, MOD=128, LSHIFT=129, RSHIFT=130, BIT_CLEAR=131, EXCLAMATION=132, 
		PLUS=133, MINUS=134, CARET=135, STAR=136, AMPERSAND=137, RECEIVE=138, 
		DECIMAL_LIT=139, BINARY_LIT=140, OCTAL_LIT=141, HEX_LIT=142, FLOAT_LIT=143, 
		DECIMAL_FLOAT_LIT=144, HEX_FLOAT_LIT=145, IMAGINARY_LIT=146, RUNE_LIT=147, 
		BYTE_VALUE=148, OCTAL_BYTE_VALUE=149, HEX_BYTE_VALUE=150, LITTLE_U_VALUE=151, 
		BIG_U_VALUE=152, RAW_STRING_LIT=153, INTERPRETED_STRING_LIT=154, WS=155, 
		COMMENT=156, TERMINATOR=157, LINE_COMMENT=158;
	public static final int
		RULE_maybeAddressableIdentifierList = 0, RULE_maybeAddressableIdentifier = 1, 
		RULE_ghostStatement = 2, RULE_boundVariables = 3, RULE_boundVariableDecl = 4, 
		RULE_predicateAccess = 5, RULE_access = 6, RULE_exprOnly = 7, RULE_stmtOnly = 8, 
		RULE_typeOnly = 9, RULE_ghostPrimaryExpr = 10, RULE_optionSome = 11, RULE_optionNone = 12, 
		RULE_optionGet = 13, RULE_sConversion = 14, RULE_triggers = 15, RULE_trigger = 16, 
		RULE_old = 17, RULE_oldLabelUse = 18, RULE_labelUse = 19, RULE_typeOf = 20, 
		RULE_isComparable = 21, RULE_ghostTypeLit = 22, RULE_domainType = 23, 
		RULE_domainClause = 24, RULE_ghostSliceType = 25, RULE_sqType = 26, RULE_seqUpdExp = 27, 
		RULE_seqUpdClause = 28, RULE_specification = 29, RULE_specStatement = 30, 
		RULE_functionDecl = 31, RULE_methodDecl = 32, RULE_blockWithBodyParameterInfo = 33, 
		RULE_assertion = 34, RULE_range = 35, RULE_sourceFile = 36, RULE_ghostMember = 37, 
		RULE_fpredicateDecl = 38, RULE_predicateBody = 39, RULE_mpredicateDecl = 40, 
		RULE_implementationProof = 41, RULE_methodImplementationProof = 42, RULE_selection = 43, 
		RULE_implementationProofPredicateAlias = 44, RULE_varSpec = 45, RULE_shortVarDecl = 46, 
		RULE_receiver = 47, RULE_nonLocalReceiver = 48, RULE_parameterDecl = 49, 
		RULE_unaryExpr = 50, RULE_unfolding = 51, RULE_expression = 52, RULE_make = 53, 
		RULE_new_ = 54, RULE_statement = 55, RULE_applyStmt = 56, RULE_packageStmt = 57, 
		RULE_specForStmt = 58, RULE_loopSpec = 59, RULE_terminationMeasure = 60, 
		RULE_basicLit = 61, RULE_primaryExpr = 62, RULE_predConstructArgs = 63, 
		RULE_interfaceType = 64, RULE_predicateSpec = 65, RULE_methodSpec = 66, 
		RULE_type_ = 67, RULE_typeLit = 68, RULE_predType = 69, RULE_predTypeParams = 70, 
		RULE_literalType = 71, RULE_slice_ = 72, RULE_low = 73, RULE_high = 74, 
		RULE_cap = 75, RULE_ifStmt = 76, RULE_exprSwitchStmt = 77, RULE_typeSwitchStmt = 78, 
		RULE_assign_op = 79, RULE_eos = 80, RULE_packageClause = 81, RULE_importDecl = 82, 
		RULE_importSpec = 83, RULE_importPath = 84, RULE_declaration = 85, RULE_constDecl = 86, 
		RULE_constSpec = 87, RULE_identifierList = 88, RULE_expressionList = 89, 
		RULE_typeDecl = 90, RULE_typeSpec = 91, RULE_varDecl = 92, RULE_block = 93, 
		RULE_statementList = 94, RULE_simpleStmt = 95, RULE_expressionStmt = 96, 
		RULE_sendStmt = 97, RULE_incDecStmt = 98, RULE_assignment = 99, RULE_emptyStmt = 100, 
		RULE_labeledStmt = 101, RULE_returnStmt = 102, RULE_breakStmt = 103, RULE_continueStmt = 104, 
		RULE_gotoStmt = 105, RULE_fallthroughStmt = 106, RULE_deferStmt = 107, 
		RULE_switchStmt = 108, RULE_exprCaseClause = 109, RULE_exprSwitchCase = 110, 
		RULE_typeSwitchGuard = 111, RULE_typeCaseClause = 112, RULE_typeSwitchCase = 113, 
		RULE_typeList = 114, RULE_selectStmt = 115, RULE_commClause = 116, RULE_commCase = 117, 
		RULE_recvStmt = 118, RULE_forStmt = 119, RULE_forClause = 120, RULE_rangeClause = 121, 
		RULE_goStmt = 122, RULE_typeName = 123, RULE_arrayType = 124, RULE_arrayLength = 125, 
		RULE_elementType = 126, RULE_pointerType = 127, RULE_sliceType = 128, 
		RULE_mapType = 129, RULE_channelType = 130, RULE_functionType = 131, RULE_signature = 132, 
		RULE_result = 133, RULE_parameters = 134, RULE_conversion = 135, RULE_operand = 136, 
		RULE_literal = 137, RULE_integer = 138, RULE_operandName = 139, RULE_qualifiedIdent = 140, 
		RULE_compositeLit = 141, RULE_literalValue = 142, RULE_elementList = 143, 
		RULE_keyedElement = 144, RULE_key = 145, RULE_element = 146, RULE_structType = 147, 
		RULE_fieldDecl = 148, RULE_string_ = 149, RULE_embeddedField = 150, RULE_functionLit = 151, 
		RULE_index = 152, RULE_typeAssertion = 153, RULE_arguments = 154, RULE_methodExpr = 155, 
		RULE_receiverType = 156;
	private static String[] makeRuleNames() {
		return new String[] {
			"maybeAddressableIdentifierList", "maybeAddressableIdentifier", "ghostStatement", 
			"boundVariables", "boundVariableDecl", "predicateAccess", "access", "exprOnly", 
			"stmtOnly", "typeOnly", "ghostPrimaryExpr", "optionSome", "optionNone", 
			"optionGet", "sConversion", "triggers", "trigger", "old", "oldLabelUse", 
			"labelUse", "typeOf", "isComparable", "ghostTypeLit", "domainType", "domainClause", 
			"ghostSliceType", "sqType", "seqUpdExp", "seqUpdClause", "specification", 
			"specStatement", "functionDecl", "methodDecl", "blockWithBodyParameterInfo", 
			"assertion", "range", "sourceFile", "ghostMember", "fpredicateDecl", 
			"predicateBody", "mpredicateDecl", "implementationProof", "methodImplementationProof", 
			"selection", "implementationProofPredicateAlias", "varSpec", "shortVarDecl", 
			"receiver", "nonLocalReceiver", "parameterDecl", "unaryExpr", "unfolding", 
			"expression", "make", "new_", "statement", "applyStmt", "packageStmt", 
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
			"'subset'", "'union'", "'intersection'", "'setminus'", "'==>'", "'--*'", 
			"'apply'", "'?'", "'!<'", "'!>'", "'range'", "'seq'", "'set'", "'mset'", 
			"'dict'", "'option'", "'len'", "'new'", "'make'", "'cap'", "'some'", 
			"'get'", "'domain'", "'axiom'", "'none'", "'pred'", "'typeOf'", "'isComparable'", 
			"'share'", "'@'", "'..'", "'shared'", "'exclusive'", "'predicate'", "'writePerm'", 
			"'noPerm'", "'bool'", "'string'", "'perm'", "'rune'", "'int'", "'int8'", 
			"'int16'", "'int32'", "'int64'", "'byte'", "'uint'", "'uint8'", "'uint16'", 
			"'uint32'", "'uint64'", "'uintptr'", "'break'", "'default'", "'func'", 
			"'interface'", "'select'", "'case'", "'defer'", "'go'", "'map'", "'struct'", 
			"'chan'", "'else'", "'goto'", "'package'", "'switch'", "'const'", "'fallthrough'", 
			"'if'", "'type'", "'continue'", "'for'", "'import'", "'return'", "'var'", 
			"'nil'", null, "'('", "')'", "'{'", "'}'", "'['", "']'", "'='", "','", 
			"';'", "':'", "'.'", "'++'", "'--'", "':='", "'...'", "'||'", "'&&'", 
			"'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'|'", "'/'", "'%'", "'<<'", 
			"'>>'", "'&^'", "'!'", "'+'", "'-'", "'^'", "'*'", "'&'", "'<-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TRUE", "FALSE", "ASSERT", "ASSUME", "INHALE", "EXHALE", "PRE", 
			"PRESERVES", "POST", "INV", "DEC", "PURE", "IMPL", "OLD", "LHS", "FORALL", 
			"EXISTS", "ACCESS", "FOLD", "UNFOLD", "UNFOLDING", "GHOST", "IN", "MULTI", 
			"SUBSET", "UNION", "INTERSECTION", "SETMINUS", "IMPLIES", "WAND", "APPLY", 
			"QMARK", "L_PRED", "R_PRED", "RANGE", "SEQ", "SET", "MSET", "DICT", "OPT", 
			"LEN", "NEW", "MAKE", "CAP", "SOME", "GET", "DOM", "AXIOM", "NONE", "PRED", 
			"TYPE_OF", "IS_COMPARABLE", "SHARE", "ADDR_MOD", "DOT_DOT", "SHARED", 
			"EXCLUSIVE", "PREDICATE", "WRITEPERM", "NOPERM", "BOOL", "STRING", "PERM", 
			"RUNE", "INT", "INT8", "INT16", "INT32", "INT64", "BYTE", "UINT", "UINT8", 
			"UINT16", "UINT32", "UINT64", "UINTPTR", "BREAK", "DEFAULT", "FUNC", 
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
			setState(314);
			maybeAddressableIdentifier();
			setState(319);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(315);
				match(COMMA);
				setState(316);
				maybeAddressableIdentifier();
				}
				}
				setState(321);
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
			setState(322);
			match(IDENTIFIER);
			setState(324);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(323);
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
			setState(334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(326);
				match(GHOST);
				setState(327);
				statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				match(ASSERT);
				setState(329);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(330);
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
				setState(331);
				predicateAccess();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(332);
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
				setState(333);
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
			setState(336);
			boundVariableDecl();
			setState(341);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(337);
					match(COMMA);
					setState(338);
					boundVariableDecl();
					}
					} 
				}
				setState(343);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(344);
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
			setState(347);
			match(IDENTIFIER);
			setState(352);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(348);
				match(COMMA);
				setState(349);
				match(IDENTIFIER);
				}
				}
				setState(354);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(355);
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
			setState(357);
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
			setState(359);
			match(ACCESS);
			setState(360);
			match(L_PAREN);
			setState(361);
			expression(0);
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(362);
				match(COMMA);
				setState(365);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(363);
					match(IDENTIFIER);
					}
					break;
				case 2:
					{
					setState(364);
					expression(0);
					}
					break;
				}
				}
			}

			setState(369);
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
			setState(371);
			expression(0);
			setState(372);
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
			setState(374);
			statement();
			setState(375);
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
		enterRule(_localctx, 18, RULE_typeOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(377);
			type_();
			setState(378);
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
		public Token quantifier;
		public Token permission;
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
		public TerminalNode WRITEPERM() { return getToken(GobraParser.WRITEPERM, 0); }
		public TerminalNode NOPERM() { return getToken(GobraParser.NOPERM, 0); }
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
		enterRule(_localctx, 20, RULE_ghostPrimaryExpr);
		int _la;
		try {
			setState(397);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(381);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(382);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(383);
				isComparable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(384);
				old();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(385);
				sConversion();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(386);
				optionNone();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(387);
				optionSome();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(388);
				optionGet();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(389);
				((GhostPrimaryExprContext)_localctx).quantifier = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
					((GhostPrimaryExprContext)_localctx).quantifier = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(390);
				boundVariables();
				setState(391);
				match(COLON);
				setState(392);
				match(COLON);
				setState(393);
				triggers();
				setState(394);
				expression(0);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(396);
				((GhostPrimaryExprContext)_localctx).permission = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==WRITEPERM || _la==NOPERM) ) {
					((GhostPrimaryExprContext)_localctx).permission = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
		enterRule(_localctx, 22, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(SOME);
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
		enterRule(_localctx, 24, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(NONE);
			setState(405);
			match(L_BRACKET);
			setState(406);
			type_();
			setState(407);
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
		enterRule(_localctx, 26, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			match(GET);
			setState(410);
			match(L_PAREN);
			setState(411);
			expression(0);
			setState(412);
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
		enterRule(_localctx, 28, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
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
			setState(415);
			match(L_PAREN);
			setState(416);
			expression(0);
			setState(417);
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
		enterRule(_localctx, 30, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(422);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(419);
				trigger();
				}
				}
				setState(424);
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
		enterRule(_localctx, 32, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(425);
			match(L_CURLY);
			setState(426);
			expression(0);
			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(427);
				match(COMMA);
				setState(428);
				expression(0);
				}
				}
				setState(433);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(434);
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
		enterRule(_localctx, 34, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			match(OLD);
			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(437);
				match(L_BRACKET);
				setState(438);
				oldLabelUse();
				setState(439);
				match(R_BRACKET);
				}
			}

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
		enterRule(_localctx, 36, RULE_oldLabelUse);
		try {
			setState(449);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(447);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(448);
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
		enterRule(_localctx, 38, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
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
		enterRule(_localctx, 40, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			match(TYPE_OF);
			setState(454);
			match(L_PAREN);
			setState(455);
			expression(0);
			setState(456);
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
		enterRule(_localctx, 42, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			match(IS_COMPARABLE);
			setState(459);
			match(L_PAREN);
			setState(460);
			expression(0);
			setState(461);
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
		enterRule(_localctx, 44, RULE_ghostTypeLit);
		try {
			setState(466);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(463);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(464);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(465);
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
		enterRule(_localctx, 46, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(468);
			match(DOM);
			setState(469);
			match(L_CURLY);
			setState(475);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(470);
				domainClause();
				setState(471);
				eos();
				}
				}
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(478);
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
		enterRule(_localctx, 48, RULE_domainClause);
		try {
			setState(489);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(480);
				match(FUNC);
				setState(481);
				match(IDENTIFIER);
				setState(482);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(483);
				match(AXIOM);
				setState(484);
				match(L_CURLY);
				setState(485);
				expression(0);
				setState(486);
				eos();
				setState(487);
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
		enterRule(_localctx, 50, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			match(GHOST);
			setState(492);
			match(L_BRACKET);
			setState(493);
			match(R_BRACKET);
			setState(494);
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
		enterRule(_localctx, 52, RULE_sqType);
		int _la;
		try {
			setState(507);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(496);
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
				setState(497);
				match(L_BRACKET);
				setState(498);
				type_();
				setState(499);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(501);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(502);
				match(L_BRACKET);
				setState(503);
				type_();
				setState(504);
				match(R_BRACKET);
				setState(505);
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
		enterRule(_localctx, 54, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			match(L_BRACKET);
			{
			setState(510);
			seqUpdClause();
			setState(515);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(511);
				match(COMMA);
				setState(512);
				seqUpdClause();
				}
				}
				setState(517);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(518);
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
		enterRule(_localctx, 56, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(520);
			expression(0);
			setState(521);
			match(ASSIGN);
			setState(522);
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
		enterRule(_localctx, 58, RULE_specification);
		int _la;
		try {
			setState(543);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(529);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC))) != 0)) {
					{
					{
					{
					setState(524);
					specStatement();
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
				match(PURE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(540);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE))) != 0)) {
					{
					{
					setState(535);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(533);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(534);
						match(PURE);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(537);
					eos();
					}
					}
					setState(542);
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
		enterRule(_localctx, 60, RULE_specStatement);
		try {
			setState(553);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(545);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(546);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(547);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(548);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(549);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(550);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(551);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(552);
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
		enterRule(_localctx, 62, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			specification();
			setState(556);
			match(FUNC);
			setState(557);
			match(IDENTIFIER);
			{
			setState(558);
			signature();
			setState(560);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(559);
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
		enterRule(_localctx, 64, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			specification();
			setState(563);
			match(FUNC);
			setState(564);
			receiver();
			setState(565);
			match(IDENTIFIER);
			{
			setState(566);
			signature();
			setState(568);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(567);
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
		enterRule(_localctx, 66, RULE_blockWithBodyParameterInfo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			match(L_CURLY);
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SHARE) {
				{
				setState(571);
				match(SHARE);
				setState(572);
				identifierList();
				setState(573);
				eos();
				}
			}

			setState(578);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(577);
				statementList();
				}
			}

			setState(580);
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
		enterRule(_localctx, 68, RULE_assertion);
		try {
			setState(584);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(583);
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
		enterRule(_localctx, 70, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(586);
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
			setState(587);
			match(L_BRACKET);
			setState(588);
			expression(0);
			setState(589);
			match(DOT_DOT);
			setState(590);
			expression(0);
			setState(591);
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
		enterRule(_localctx, 72, RULE_sourceFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(593);
			packageClause();
			setState(594);
			eos();
			setState(600);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(595);
				importDecl();
				setState(596);
				eos();
				}
				}
				setState(602);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(613);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (CONST - 64)) | (1L << (TYPE - 64)) | (1L << (VAR - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || _la==STAR || _la==RECEIVE) {
				{
				{
				setState(607);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(603);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(604);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(605);
					declaration();
					}
					break;
				case 4:
					{
					setState(606);
					ghostMember();
					}
					break;
				}
				setState(609);
				eos();
				}
				}
				setState(615);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(616);
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
		enterRule(_localctx, 74, RULE_ghostMember);
		try {
			setState(633);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(618);
				fpredicateDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(619);
				mpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(620);
				implementationProof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(624);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(621);
					match(GHOST);
					}
					break;
				case 2:
					{
					{
					setState(622);
					match(GHOST);
					setState(623);
					eos();
					}
					}
					break;
				}
				setState(631);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(626);
					methodDecl();
					}
					break;
				case 2:
					{
					setState(627);
					functionDecl();
					}
					break;
				case 3:
					{
					setState(628);
					constDecl();
					}
					break;
				case 4:
					{
					setState(629);
					typeDecl();
					}
					break;
				case 5:
					{
					setState(630);
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
		enterRule(_localctx, 76, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(635);
			match(PRED);
			setState(636);
			match(IDENTIFIER);
			setState(637);
			parameters();
			setState(639);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(638);
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
		enterRule(_localctx, 78, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(641);
			match(L_CURLY);
			setState(642);
			expression(0);
			setState(643);
			eos();
			setState(644);
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
		enterRule(_localctx, 80, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(646);
			match(PRED);
			setState(647);
			receiver();
			setState(648);
			match(IDENTIFIER);
			setState(649);
			parameters();
			setState(651);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(650);
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
		enterRule(_localctx, 82, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			type_();
			setState(654);
			match(IMPL);
			setState(655);
			type_();
			setState(656);
			match(L_CURLY);
			setState(662);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRED) {
				{
				{
				setState(657);
				implementationProofPredicateAlias();
				setState(658);
				eos();
				}
				}
				setState(664);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(670);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PURE || _la==L_PAREN) {
				{
				{
				setState(665);
				methodImplementationProof();
				setState(666);
				eos();
				}
				}
				setState(672);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(673);
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
		enterRule(_localctx, 84, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(675);
				match(PURE);
				}
			}

			setState(678);
			nonLocalReceiver();
			setState(679);
			match(IDENTIFIER);
			setState(680);
			signature();
			setState(682);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(681);
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
		enterRule(_localctx, 86, RULE_selection);
		try {
			setState(692);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(684);
				primaryExpr(0);
				setState(685);
				match(DOT);
				setState(686);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(688);
				type_();
				setState(689);
				match(DOT);
				setState(690);
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
		enterRule(_localctx, 88, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(694);
			match(PRED);
			setState(695);
			match(IDENTIFIER);
			setState(696);
			match(DECLARE_ASSIGN);
			setState(699);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(697);
				selection();
				}
				break;
			case 2:
				{
				setState(698);
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
		enterRule(_localctx, 90, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			maybeAddressableIdentifierList();
			setState(709);
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
				setState(702);
				type_();
				setState(705);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(703);
					match(ASSIGN);
					setState(704);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(707);
				match(ASSIGN);
				setState(708);
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
		enterRule(_localctx, 92, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(711);
			maybeAddressableIdentifierList();
			setState(712);
			match(DECLARE_ASSIGN);
			setState(713);
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
		enterRule(_localctx, 94, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(715);
			match(L_PAREN);
			setState(717);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(716);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(719);
			type_();
			setState(721);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(720);
				match(COMMA);
				}
			}

			setState(723);
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
		enterRule(_localctx, 96, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(725);
			match(L_PAREN);
			setState(727);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(726);
				match(IDENTIFIER);
				}
				break;
			}
			setState(730);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(729);
				match(STAR);
				}
			}

			setState(732);
			typeName();
			setState(733);
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
		enterRule(_localctx, 98, RULE_parameterDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(736);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(735);
				match(GHOST);
				}
				break;
			}
			setState(739);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(738);
				identifierList();
				}
				break;
			}
			setState(742);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(741);
				match(ELLIPSIS);
				}
			}

			setState(744);
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
		enterRule(_localctx, 100, RULE_unaryExpr);
		int _la;
		try {
			setState(755);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(746);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(747);
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
				setState(748);
				match(L_PAREN);
				setState(749);
				expression(0);
				setState(750);
				match(R_PAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(752);
				unfolding();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(753);
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
				setState(754);
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
		enterRule(_localctx, 102, RULE_unfolding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(757);
			match(UNFOLDING);
			setState(758);
			predicateAccess();
			setState(759);
			match(IN);
			setState(760);
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
		public TerminalNode WAND() { return getToken(GobraParser.WAND, 0); }
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
		int _startState = 104;
		enterRecursionRule(_localctx, 104, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(763);
				match(TYPE);
				setState(764);
				match(L_BRACKET);
				setState(765);
				type_();
				setState(766);
				match(R_BRACKET);
				}
				break;
			case 2:
				{
				setState(768);
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
				setState(769);
				match(L_PAREN);
				setState(770);
				expression(0);
				setState(771);
				match(R_PAREN);
				}
				break;
			case 3:
				{
				setState(773);
				unfolding();
				}
				break;
			case 4:
				{
				setState(774);
				new_();
				}
				break;
			case 5:
				{
				setState(775);
				make();
				}
				break;
			case 6:
				{
				setState(776);
				((ExpressionContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)))) != 0)) ) {
					((ExpressionContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(777);
				expression(11);
				}
				break;
			case 7:
				{
				setState(778);
				primaryExpr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(813);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(811);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(781);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(782);
						((ExpressionContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (DIV - 127)) | (1L << (MOD - 127)) | (1L << (LSHIFT - 127)) | (1L << (RSHIFT - 127)) | (1L << (BIT_CLEAR - 127)) | (1L << (STAR - 127)) | (1L << (AMPERSAND - 127)))) != 0)) ) {
							((ExpressionContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(783);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(784);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(785);
						((ExpressionContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (PLUS_PLUS - 114)) | (1L << (OR - 114)) | (1L << (PLUS - 114)) | (1L << (MINUS - 114)) | (1L << (CARET - 114)))) != 0)) ) {
							((ExpressionContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(786);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(787);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(788);
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
						setState(789);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(790);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(791);
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
						setState(792);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(793);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(794);
						((ExpressionContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & ((1L << (EQUALS - 120)) | (1L << (NOT_EQUALS - 120)) | (1L << (LESS - 120)) | (1L << (LESS_OR_EQUALS - 120)) | (1L << (GREATER - 120)) | (1L << (GREATER_OR_EQUALS - 120)))) != 0)) ) {
							((ExpressionContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(795);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(796);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(797);
						match(LOGICAL_AND);
						setState(798);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(799);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(800);
						match(LOGICAL_OR);
						setState(801);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(802);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(803);
						match(IMPLIES);
						setState(804);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(805);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(806);
						match(QMARK);
						setState(807);
						expression(0);
						setState(808);
						match(COLON);
						setState(809);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(815);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
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
		enterRule(_localctx, 106, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(816);
			match(MAKE);
			setState(817);
			match(L_PAREN);
			setState(818);
			type_();
			setState(821);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(819);
				match(COMMA);
				setState(820);
				expressionList();
				}
			}

			setState(823);
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
		enterRule(_localctx, 108, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			match(NEW);
			setState(826);
			match(L_PAREN);
			setState(827);
			type_();
			setState(828);
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
		enterRule(_localctx, 110, RULE_statement);
		try {
			setState(848);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(830);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(831);
				packageStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(832);
				applyStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(833);
				declaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(834);
				labeledStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(835);
				simpleStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(836);
				goStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(837);
				returnStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(838);
				breakStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(839);
				continueStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(840);
				gotoStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(841);
				fallthroughStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(842);
				block();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(843);
				ifStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(844);
				switchStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(845);
				selectStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(846);
				specForStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(847);
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
		enterRule(_localctx, 112, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(850);
			match(APPLY);
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
		enterRule(_localctx, 114, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			match(PACKAGE);
			setState(854);
			expression(0);
			setState(856);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(855);
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
		enterRule(_localctx, 116, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			loopSpec();
			setState(859);
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
		enterRule(_localctx, 118, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(861);
				match(INV);
				setState(862);
				expression(0);
				setState(863);
				eos();
				}
				}
				setState(869);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(874);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(870);
				match(DEC);
				setState(871);
				terminationMeasure();
				setState(872);
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
		enterRule(_localctx, 120, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(876);
				expressionList();
				}
				break;
			}
			setState(881);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(879);
				match(IF);
				setState(880);
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
		enterRule(_localctx, 122, RULE_basicLit);
		try {
			setState(891);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(883);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(884);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(885);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(886);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(887);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(888);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(889);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(890);
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
		int _startState = 124;
		enterRecursionRule(_localctx, 124, RULE_primaryExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(898);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(894);
				operand();
				}
				break;
			case 2:
				{
				setState(895);
				conversion();
				}
				break;
			case 3:
				{
				setState(896);
				methodExpr();
				}
				break;
			case 4:
				{
				setState(897);
				ghostPrimaryExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(914);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrimaryExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
					setState(900);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(910);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
					case 1:
						{
						{
						setState(901);
						match(DOT);
						setState(902);
						match(IDENTIFIER);
						}
						}
						break;
					case 2:
						{
						setState(903);
						index();
						}
						break;
					case 3:
						{
						setState(904);
						slice_();
						}
						break;
					case 4:
						{
						setState(905);
						seqUpdExp();
						}
						break;
					case 5:
						{
						setState(906);
						typeAssertion();
						}
						break;
					case 6:
						{
						setState(907);
						if (!(noTerminatorBetween(1))) throw new FailedPredicateException(this, "noTerminatorBetween(1)");
						setState(908);
						arguments();
						}
						break;
					case 7:
						{
						setState(909);
						predConstructArgs();
						}
						break;
					}
					}
					} 
				}
				setState(916);
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
		enterRule(_localctx, 126, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(917);
			match(L_PRED);
			setState(919);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(918);
				expressionList();
				}
			}

			setState(922);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(921);
				match(COMMA);
				}
			}

			setState(924);
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
		enterRule(_localctx, 128, RULE_interfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(926);
			match(INTERFACE);
			setState(927);
			match(L_CURLY);
			setState(937);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(931);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
					case 1:
						{
						setState(928);
						methodSpec();
						}
						break;
					case 2:
						{
						setState(929);
						typeName();
						}
						break;
					case 3:
						{
						setState(930);
						predicateSpec();
						}
						break;
					}
					setState(933);
					eos();
					}
					} 
				}
				setState(939);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			}
			setState(940);
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
		enterRule(_localctx, 130, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(942);
			match(PRED);
			setState(943);
			match(IDENTIFIER);
			setState(944);
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
		enterRule(_localctx, 132, RULE_methodSpec);
		int _la;
		try {
			setState(962);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(946);
				if (!(noTerminatorAfterParams(2))) throw new FailedPredicateException(this, "noTerminatorAfterParams(2)");
				setState(948);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(947);
					match(GHOST);
					}
				}

				setState(950);
				specification();
				setState(951);
				match(IDENTIFIER);
				setState(952);
				parameters();
				setState(953);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(956);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(955);
					match(GHOST);
					}
				}

				setState(958);
				specification();
				setState(959);
				match(IDENTIFIER);
				setState(960);
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
		enterRule(_localctx, 134, RULE_type_);
		int _la;
		try {
			setState(972);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(964);
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
				setState(965);
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
				setState(966);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(967);
				match(L_PAREN);
				setState(968);
				type_();
				setState(969);
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
				setState(971);
				((Type_Context)_localctx).predefined = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (BOOL - 61)) | (1L << (STRING - 61)) | (1L << (PERM - 61)) | (1L << (RUNE - 61)) | (1L << (INT - 61)) | (1L << (INT8 - 61)) | (1L << (INT16 - 61)) | (1L << (INT32 - 61)) | (1L << (INT64 - 61)) | (1L << (BYTE - 61)) | (1L << (UINT - 61)) | (1L << (UINT8 - 61)) | (1L << (UINT16 - 61)) | (1L << (UINT32 - 61)) | (1L << (UINT64 - 61)) | (1L << (UINTPTR - 61)))) != 0)) ) {
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
		enterRule(_localctx, 136, RULE_typeLit);
		try {
			setState(983);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(974);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(975);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(976);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(977);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(978);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(979);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(980);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(981);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(982);
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
		enterRule(_localctx, 138, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(985);
			match(PRED);
			setState(986);
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
		enterRule(_localctx, 140, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			match(L_PAREN);
			setState(1000);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & ((1L << (GHOST - 22)) | (1L << (SEQ - 22)) | (1L << (SET - 22)) | (1L << (MSET - 22)) | (1L << (DICT - 22)) | (1L << (OPT - 22)) | (1L << (DOM - 22)) | (1L << (PRED - 22)) | (1L << (BOOL - 22)) | (1L << (STRING - 22)) | (1L << (PERM - 22)) | (1L << (RUNE - 22)) | (1L << (INT - 22)) | (1L << (INT8 - 22)) | (1L << (INT16 - 22)) | (1L << (INT32 - 22)) | (1L << (INT64 - 22)) | (1L << (BYTE - 22)) | (1L << (UINT - 22)) | (1L << (UINT8 - 22)) | (1L << (UINT16 - 22)) | (1L << (UINT32 - 22)) | (1L << (UINT64 - 22)) | (1L << (UINTPTR - 22)) | (1L << (FUNC - 22)) | (1L << (INTERFACE - 22)) | (1L << (MAP - 22)))) != 0) || ((((_la - 86)) & ~0x3f) == 0 && ((1L << (_la - 86)) & ((1L << (STRUCT - 86)) | (1L << (CHAN - 86)) | (1L << (IDENTIFIER - 86)) | (1L << (L_PAREN - 86)) | (1L << (L_BRACKET - 86)) | (1L << (STAR - 86)) | (1L << (RECEIVE - 86)))) != 0)) {
				{
				setState(989);
				type_();
				setState(994);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(990);
						match(COMMA);
						setState(991);
						type_();
						}
						} 
					}
					setState(996);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
				}
				setState(998);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(997);
					match(COMMA);
					}
				}

				}
			}

			setState(1002);
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
		enterRule(_localctx, 142, RULE_literalType);
		try {
			setState(1014);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1004);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1005);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1006);
				match(L_BRACKET);
				setState(1007);
				match(ELLIPSIS);
				setState(1008);
				match(R_BRACKET);
				setState(1009);
				elementType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1010);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1011);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1012);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1013);
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
		enterRule(_localctx, 144, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1016);
			match(L_BRACKET);
			setState(1032);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				{
				setState(1018);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1017);
					low();
					}
				}

				setState(1020);
				match(COLON);
				setState(1022);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1021);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1025);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
					{
					setState(1024);
					low();
					}
				}

				setState(1027);
				match(COLON);
				setState(1028);
				high();
				setState(1029);
				match(COLON);
				setState(1030);
				cap();
				}
				break;
			}
			setState(1034);
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
		enterRule(_localctx, 146, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1036);
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
		enterRule(_localctx, 148, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1038);
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
		enterRule(_localctx, 150, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1040);
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
		enterRule(_localctx, 152, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1042);
			match(IF);
			setState(1047);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(1044);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
				case 1:
					{
					setState(1043);
					simpleStmt();
					}
					break;
				}
				setState(1046);
				match(SEMI);
				}
				break;
			}
			setState(1049);
			expression(0);
			setState(1050);
			block();
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(1051);
				match(ELSE);
				setState(1054);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1052);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1053);
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
		enterRule(_localctx, 154, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			match(SWITCH);
			setState(1063);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(1060);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
				case 1:
					{
					setState(1059);
					simpleStmt();
					}
					break;
				}
				setState(1062);
				match(SEMI);
				}
				break;
			}
			setState(1066);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1065);
				expression(0);
				}
			}

			setState(1068);
			match(L_CURLY);
			setState(1072);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1069);
				exprCaseClause();
				}
				}
				setState(1074);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1075);
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
		enterRule(_localctx, 156, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1077);
			match(SWITCH);
			setState(1082);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				{
				setState(1079);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
				case 1:
					{
					setState(1078);
					simpleStmt();
					}
					break;
				}
				setState(1081);
				match(SEMI);
				}
				break;
			}
			setState(1084);
			typeSwitchGuard();
			setState(1085);
			match(L_CURLY);
			setState(1089);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1086);
				typeCaseClause();
				}
				}
				setState(1091);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1092);
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
		enterRule(_localctx, 158, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 126)) & ~0x3f) == 0 && ((1L << (_la - 126)) & ((1L << (OR - 126)) | (1L << (DIV - 126)) | (1L << (MOD - 126)) | (1L << (LSHIFT - 126)) | (1L << (RSHIFT - 126)) | (1L << (BIT_CLEAR - 126)) | (1L << (PLUS - 126)) | (1L << (MINUS - 126)) | (1L << (CARET - 126)) | (1L << (STAR - 126)) | (1L << (AMPERSAND - 126)))) != 0)) {
				{
				setState(1094);
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

			setState(1097);
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
		enterRule(_localctx, 160, RULE_eos);
		try {
			setState(1104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1099);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1100);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1101);
				if (!(lineTerminatorAhead())) throw new FailedPredicateException(this, "lineTerminatorAhead()");
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1102);
				if (!(checkPreviousTokenText("}"))) throw new FailedPredicateException(this, "checkPreviousTokenText(\"}\")");
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1103);
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
		enterRule(_localctx, 162, RULE_packageClause);
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
		enterRule(_localctx, 164, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1109);
			match(IMPORT);
			setState(1121);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1110);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1111);
				match(L_PAREN);
				setState(1117);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 102)) & ~0x3f) == 0 && ((1L << (_la - 102)) & ((1L << (IDENTIFIER - 102)) | (1L << (DOT - 102)) | (1L << (RAW_STRING_LIT - 102)) | (1L << (INTERPRETED_STRING_LIT - 102)))) != 0)) {
					{
					{
					setState(1112);
					importSpec();
					setState(1113);
					eos();
					}
					}
					setState(1119);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1120);
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
		enterRule(_localctx, 166, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1123);
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

			setState(1126);
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
		enterRule(_localctx, 168, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1128);
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
		enterRule(_localctx, 170, RULE_declaration);
		try {
			setState(1133);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1130);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1131);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1132);
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
		enterRule(_localctx, 172, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1135);
			match(CONST);
			setState(1147);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1136);
				constSpec();
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
					constSpec();
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
		enterRule(_localctx, 174, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			identifierList();
			setState(1155);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				{
				setState(1151);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & ((1L << (GHOST - 22)) | (1L << (SEQ - 22)) | (1L << (SET - 22)) | (1L << (MSET - 22)) | (1L << (DICT - 22)) | (1L << (OPT - 22)) | (1L << (DOM - 22)) | (1L << (PRED - 22)) | (1L << (BOOL - 22)) | (1L << (STRING - 22)) | (1L << (PERM - 22)) | (1L << (RUNE - 22)) | (1L << (INT - 22)) | (1L << (INT8 - 22)) | (1L << (INT16 - 22)) | (1L << (INT32 - 22)) | (1L << (INT64 - 22)) | (1L << (BYTE - 22)) | (1L << (UINT - 22)) | (1L << (UINT8 - 22)) | (1L << (UINT16 - 22)) | (1L << (UINT32 - 22)) | (1L << (UINT64 - 22)) | (1L << (UINTPTR - 22)) | (1L << (FUNC - 22)) | (1L << (INTERFACE - 22)) | (1L << (MAP - 22)))) != 0) || ((((_la - 86)) & ~0x3f) == 0 && ((1L << (_la - 86)) & ((1L << (STRUCT - 86)) | (1L << (CHAN - 86)) | (1L << (IDENTIFIER - 86)) | (1L << (L_PAREN - 86)) | (1L << (L_BRACKET - 86)) | (1L << (STAR - 86)) | (1L << (RECEIVE - 86)))) != 0)) {
					{
					setState(1150);
					type_();
					}
				}

				setState(1153);
				match(ASSIGN);
				setState(1154);
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
		enterRule(_localctx, 176, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1157);
			match(IDENTIFIER);
			setState(1162);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1158);
					match(COMMA);
					setState(1159);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1164);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 178, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1165);
			expression(0);
			setState(1170);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,105,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1166);
					match(COMMA);
					setState(1167);
					expression(0);
					}
					} 
				}
				setState(1172);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,105,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 180, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1173);
			match(TYPE);
			setState(1185);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1174);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1175);
				match(L_PAREN);
				setState(1181);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1176);
					typeSpec();
					setState(1177);
					eos();
					}
					}
					setState(1183);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1184);
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
		enterRule(_localctx, 182, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1187);
			match(IDENTIFIER);
			setState(1189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1188);
				match(ASSIGN);
				}
			}

			setState(1191);
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
		enterRule(_localctx, 184, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1193);
			match(VAR);
			setState(1205);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1194);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1195);
				match(L_PAREN);
				setState(1201);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1196);
					varSpec();
					setState(1197);
					eos();
					}
					}
					setState(1203);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1204);
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
		enterRule(_localctx, 186, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			match(L_CURLY);
			setState(1209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1208);
				statementList();
				}
			}

			setState(1211);
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
		enterRule(_localctx, 188, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1216); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1213);
				statement();
				setState(1214);
				eos();
				}
				}
				setState(1218); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0) );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 190, RULE_simpleStmt);
		try {
			setState(1226);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1220);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1221);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1222);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1223);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1224);
				shortVarDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1225);
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
		enterRule(_localctx, 192, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1228);
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
		enterRule(_localctx, 194, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1230);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1231);
			match(RECEIVE);
			setState(1232);
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
		enterRule(_localctx, 196, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1234);
			expression(0);
			setState(1235);
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
		enterRule(_localctx, 198, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1237);
			expressionList();
			setState(1238);
			assign_op();
			setState(1239);
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
		enterRule(_localctx, 200, RULE_emptyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1241);
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
		enterRule(_localctx, 202, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1243);
			match(IDENTIFIER);
			setState(1244);
			match(COLON);
			setState(1246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
			case 1:
				{
				setState(1245);
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
		enterRule(_localctx, 204, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1248);
			match(RETURN);
			setState(1250);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				{
				setState(1249);
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
		enterRule(_localctx, 206, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1252);
			match(BREAK);
			setState(1254);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1253);
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
		enterRule(_localctx, 208, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1256);
			match(CONTINUE);
			setState(1258);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				{
				setState(1257);
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
		enterRule(_localctx, 210, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1260);
			match(GOTO);
			setState(1261);
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
		enterRule(_localctx, 212, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1263);
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
		enterRule(_localctx, 214, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1265);
			match(DEFER);
			setState(1266);
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
		enterRule(_localctx, 216, RULE_switchStmt);
		try {
			setState(1270);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1268);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1269);
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
		enterRule(_localctx, 218, RULE_exprCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1272);
			exprSwitchCase();
			setState(1273);
			match(COLON);
			setState(1275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1274);
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
		enterRule(_localctx, 220, RULE_exprSwitchCase);
		try {
			setState(1280);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1277);
				match(CASE);
				setState(1278);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1279);
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
		enterRule(_localctx, 222, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1284);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1282);
				match(IDENTIFIER);
				setState(1283);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1286);
			primaryExpr(0);
			setState(1287);
			match(DOT);
			setState(1288);
			match(L_PAREN);
			setState(1289);
			match(TYPE);
			setState(1290);
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
		enterRule(_localctx, 224, RULE_typeCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1292);
			typeSwitchCase();
			setState(1293);
			match(COLON);
			setState(1295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1294);
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
		enterRule(_localctx, 226, RULE_typeSwitchCase);
		try {
			setState(1300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1297);
				match(CASE);
				setState(1298);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1299);
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
		enterRule(_localctx, 228, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1304);
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
				setState(1302);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1303);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1306);
				match(COMMA);
				setState(1309);
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
					setState(1307);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1308);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1315);
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
		enterRule(_localctx, 230, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1316);
			match(SELECT);
			setState(1317);
			match(L_CURLY);
			setState(1321);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1318);
				commClause();
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
		enterRule(_localctx, 232, RULE_commClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1326);
			commCase();
			setState(1327);
			match(COLON);
			setState(1329);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1328);
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
		enterRule(_localctx, 234, RULE_commCase);
		try {
			setState(1337);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1331);
				match(CASE);
				setState(1334);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
				case 1:
					{
					setState(1332);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1333);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1336);
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
		enterRule(_localctx, 236, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1345);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1339);
				expressionList();
				setState(1340);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1342);
				identifierList();
				setState(1343);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1347);
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
		enterRule(_localctx, 238, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1349);
			match(FOR);
			setState(1353);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1350);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1351);
				forClause();
				}
				break;
			case 3:
				{
				setState(1352);
				rangeClause();
				}
				break;
			}
			setState(1355);
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
		enterRule(_localctx, 240, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1357);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1360);
			match(SEMI);
			setState(1362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1361);
				expression(0);
				}
			}

			setState(1364);
			match(SEMI);
			setState(1366);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1365);
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
		enterRule(_localctx, 242, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1374);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1368);
				expressionList();
				setState(1369);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1371);
				identifierList();
				setState(1372);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1376);
			match(RANGE);
			setState(1377);
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
		enterRule(_localctx, 244, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1379);
			match(GO);
			setState(1380);
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
		enterRule(_localctx, 246, RULE_typeName);
		try {
			setState(1384);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1382);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1383);
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
		enterRule(_localctx, 248, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1386);
			match(L_BRACKET);
			setState(1387);
			arrayLength();
			setState(1388);
			match(R_BRACKET);
			setState(1389);
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
		enterRule(_localctx, 250, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1391);
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
		enterRule(_localctx, 252, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1393);
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
		enterRule(_localctx, 254, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1395);
			match(STAR);
			setState(1396);
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
		enterRule(_localctx, 256, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1398);
			match(L_BRACKET);
			setState(1399);
			match(R_BRACKET);
			setState(1400);
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
		enterRule(_localctx, 258, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1402);
			match(MAP);
			setState(1403);
			match(L_BRACKET);
			setState(1404);
			type_();
			setState(1405);
			match(R_BRACKET);
			setState(1406);
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
		enterRule(_localctx, 260, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1413);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				{
				setState(1408);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1409);
				match(CHAN);
				setState(1410);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1411);
				match(RECEIVE);
				setState(1412);
				match(CHAN);
				}
				break;
			}
			setState(1415);
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
		enterRule(_localctx, 262, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1417);
			match(FUNC);
			setState(1418);
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
		enterRule(_localctx, 264, RULE_signature);
		try {
			setState(1425);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1420);
				if (!(noTerminatorAfterParams(1))) throw new FailedPredicateException(this, "noTerminatorAfterParams(1)");
				setState(1421);
				parameters();
				setState(1422);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1424);
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
		enterRule(_localctx, 266, RULE_result);
		try {
			setState(1429);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1427);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1428);
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
		enterRule(_localctx, 268, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1431);
			match(L_PAREN);
			setState(1443);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & ((1L << (GHOST - 22)) | (1L << (SEQ - 22)) | (1L << (SET - 22)) | (1L << (MSET - 22)) | (1L << (DICT - 22)) | (1L << (OPT - 22)) | (1L << (DOM - 22)) | (1L << (PRED - 22)) | (1L << (BOOL - 22)) | (1L << (STRING - 22)) | (1L << (PERM - 22)) | (1L << (RUNE - 22)) | (1L << (INT - 22)) | (1L << (INT8 - 22)) | (1L << (INT16 - 22)) | (1L << (INT32 - 22)) | (1L << (INT64 - 22)) | (1L << (BYTE - 22)) | (1L << (UINT - 22)) | (1L << (UINT8 - 22)) | (1L << (UINT16 - 22)) | (1L << (UINT32 - 22)) | (1L << (UINT64 - 22)) | (1L << (UINTPTR - 22)) | (1L << (FUNC - 22)) | (1L << (INTERFACE - 22)) | (1L << (MAP - 22)))) != 0) || ((((_la - 86)) & ~0x3f) == 0 && ((1L << (_la - 86)) & ((1L << (STRUCT - 86)) | (1L << (CHAN - 86)) | (1L << (IDENTIFIER - 86)) | (1L << (L_PAREN - 86)) | (1L << (L_BRACKET - 86)) | (1L << (ELLIPSIS - 86)) | (1L << (STAR - 86)) | (1L << (RECEIVE - 86)))) != 0)) {
				{
				setState(1432);
				parameterDecl();
				setState(1437);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,141,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1433);
						match(COMMA);
						setState(1434);
						parameterDecl();
						}
						} 
					}
					setState(1439);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,141,_ctx);
				}
				setState(1441);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1440);
					match(COMMA);
					}
				}

				}
			}

			setState(1445);
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
		enterRule(_localctx, 270, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1447);
			type_();
			setState(1448);
			match(L_PAREN);
			setState(1449);
			expression(0);
			setState(1451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1450);
				match(COMMA);
				}
			}

			setState(1453);
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
		enterRule(_localctx, 272, RULE_operand);
		try {
			setState(1461);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1455);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1456);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1457);
				match(L_PAREN);
				setState(1458);
				expression(0);
				setState(1459);
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
		enterRule(_localctx, 274, RULE_literal);
		try {
			setState(1466);
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
				setState(1463);
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
				setState(1464);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1465);
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
		enterRule(_localctx, 276, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1468);
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
		enterRule(_localctx, 278, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1470);
			match(IDENTIFIER);
			setState(1473);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
			case 1:
				{
				setState(1471);
				match(DOT);
				setState(1472);
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
		enterRule(_localctx, 280, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1475);
			match(IDENTIFIER);
			setState(1476);
			match(DOT);
			setState(1477);
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
		enterRule(_localctx, 282, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1479);
			literalType();
			setState(1480);
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
		enterRule(_localctx, 284, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1482);
			match(L_CURLY);
			setState(1487);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1483);
				elementList();
				setState(1485);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1484);
					match(COMMA);
					}
				}

				}
			}

			setState(1489);
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
		enterRule(_localctx, 286, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1491);
			keyedElement();
			setState(1496);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1492);
					match(COMMA);
					setState(1493);
					keyedElement();
					}
					} 
				}
				setState(1498);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 288, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1502);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,151,_ctx) ) {
			case 1:
				{
				setState(1499);
				key();
				setState(1500);
				match(COLON);
				}
				break;
			}
			setState(1504);
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
		enterRule(_localctx, 290, RULE_key);
		try {
			setState(1509);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1506);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1507);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1508);
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
		enterRule(_localctx, 292, RULE_element);
		try {
			setState(1513);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case OLD:
			case FORALL:
			case EXISTS:
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
			case WRITEPERM:
			case NOPERM:
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
				setState(1511);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1512);
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
		enterRule(_localctx, 294, RULE_structType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1515);
			match(STRUCT);
			setState(1516);
			match(L_CURLY);
			setState(1522);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,154,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1517);
					fieldDecl();
					setState(1518);
					eos();
					}
					} 
				}
				setState(1524);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,154,_ctx);
			}
			setState(1525);
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
		enterRule(_localctx, 296, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1532);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				{
				setState(1527);
				if (!(noTerminatorBetween(2))) throw new FailedPredicateException(this, "noTerminatorBetween(2)");
				setState(1528);
				identifierList();
				setState(1529);
				type_();
				}
				break;
			case 2:
				{
				setState(1531);
				embeddedField();
				}
				break;
			}
			setState(1535);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1534);
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
		enterRule(_localctx, 298, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1537);
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
		enterRule(_localctx, 300, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1540);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1539);
				match(STAR);
				}
			}

			setState(1542);
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
		enterRule(_localctx, 302, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1544);
			match(FUNC);
			setState(1545);
			signature();
			setState(1546);
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
		enterRule(_localctx, 304, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1548);
			match(L_BRACKET);
			setState(1549);
			expression(0);
			setState(1550);
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
		enterRule(_localctx, 306, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1552);
			match(DOT);
			setState(1553);
			match(L_PAREN);
			setState(1554);
			type_();
			setState(1555);
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
		enterRule(_localctx, 308, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1557);
			match(L_PAREN);
			setState(1572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM) | (1L << BOOL) | (1L << STRING) | (1L << PERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (EXCLAMATION - 132)) | (1L << (PLUS - 132)) | (1L << (MINUS - 132)) | (1L << (CARET - 132)) | (1L << (STAR - 132)) | (1L << (AMPERSAND - 132)) | (1L << (RECEIVE - 132)) | (1L << (DECIMAL_LIT - 132)) | (1L << (BINARY_LIT - 132)) | (1L << (OCTAL_LIT - 132)) | (1L << (HEX_LIT - 132)) | (1L << (FLOAT_LIT - 132)) | (1L << (IMAGINARY_LIT - 132)) | (1L << (RUNE_LIT - 132)) | (1L << (RAW_STRING_LIT - 132)) | (1L << (INTERPRETED_STRING_LIT - 132)))) != 0)) {
				{
				setState(1564);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
				case 1:
					{
					setState(1558);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1559);
					type_();
					setState(1562);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
					case 1:
						{
						setState(1560);
						match(COMMA);
						setState(1561);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1567);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1566);
					match(ELLIPSIS);
					}
				}

				setState(1570);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1569);
					match(COMMA);
					}
				}

				}
			}

			setState(1574);
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
		enterRule(_localctx, 310, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1576);
			receiverType();
			setState(1577);
			match(DOT);
			setState(1578);
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
		enterRule(_localctx, 312, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1580);
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
		case 52:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 62:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 66:
			return methodSpec_sempred((MethodSpecContext)_localctx, predIndex);
		case 80:
			return eos_sempred((EosContext)_localctx, predIndex);
		case 132:
			return signature_sempred((SignatureContext)_localctx, predIndex);
		case 148:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a0\u0631\4\2\t"+
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
		"\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\3\2\3\2\3\2\7\2\u0140"+
		"\n\2\f\2\16\2\u0143\13\2\3\3\3\3\5\3\u0147\n\3\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\5\4\u0151\n\4\3\5\3\5\3\5\7\5\u0156\n\5\f\5\16\5\u0159\13\5\3"+
		"\5\5\5\u015c\n\5\3\6\3\6\3\6\7\6\u0161\n\6\f\6\16\6\u0164\13\6\3\6\3\6"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u0170\n\b\5\b\u0172\n\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u0190\n\f\3\r\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\7\21\u01a7\n\21\f\21\16\21\u01aa\13\21\3\22\3\22\3\22\3\22"+
		"\7\22\u01b0\n\22\f\22\16\22\u01b3\13\22\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\5\23\u01bc\n\23\3\23\3\23\3\23\3\23\3\24\3\24\5\24\u01c4\n\24\3"+
		"\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3"+
		"\30\5\30\u01d5\n\30\3\31\3\31\3\31\3\31\3\31\7\31\u01dc\n\31\f\31\16\31"+
		"\u01df\13\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\5"+
		"\32\u01ec\n\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\5\34\u01fe\n\34\3\35\3\35\3\35\3\35\7\35\u0204"+
		"\n\35\f\35\16\35\u0207\13\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3"+
		"\37\7\37\u0212\n\37\f\37\16\37\u0215\13\37\3\37\3\37\3\37\5\37\u021a\n"+
		"\37\3\37\7\37\u021d\n\37\f\37\16\37\u0220\13\37\5\37\u0222\n\37\3 \3 "+
		"\3 \3 \3 \3 \3 \3 \5 \u022c\n \3!\3!\3!\3!\3!\5!\u0233\n!\3\"\3\"\3\""+
		"\3\"\3\"\3\"\5\"\u023b\n\"\3#\3#\3#\3#\3#\5#\u0242\n#\3#\5#\u0245\n#\3"+
		"#\3#\3$\3$\5$\u024b\n$\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\7&\u0259\n"+
		"&\f&\16&\u025c\13&\3&\3&\3&\3&\5&\u0262\n&\3&\3&\7&\u0266\n&\f&\16&\u0269"+
		"\13&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u0273\n\'\3\'\3\'\3\'\3\'\3\'\5"+
		"\'\u027a\n\'\5\'\u027c\n\'\3(\3(\3(\3(\5(\u0282\n(\3)\3)\3)\3)\3)\3*\3"+
		"*\3*\3*\3*\5*\u028e\n*\3+\3+\3+\3+\3+\3+\3+\7+\u0297\n+\f+\16+\u029a\13"+
		"+\3+\3+\3+\7+\u029f\n+\f+\16+\u02a2\13+\3+\3+\3,\5,\u02a7\n,\3,\3,\3,"+
		"\3,\5,\u02ad\n,\3-\3-\3-\3-\3-\3-\3-\3-\5-\u02b7\n-\3.\3.\3.\3.\3.\5."+
		"\u02be\n.\3/\3/\3/\3/\5/\u02c4\n/\3/\3/\5/\u02c8\n/\3\60\3\60\3\60\3\60"+
		"\3\61\3\61\5\61\u02d0\n\61\3\61\3\61\5\61\u02d4\n\61\3\61\3\61\3\62\3"+
		"\62\5\62\u02da\n\62\3\62\5\62\u02dd\n\62\3\62\3\62\3\62\3\63\5\63\u02e3"+
		"\n\63\3\63\5\63\u02e6\n\63\3\63\5\63\u02e9\n\63\3\63\3\63\3\64\3\64\3"+
		"\64\3\64\3\64\3\64\3\64\3\64\3\64\5\64\u02f6\n\64\3\65\3\65\3\65\3\65"+
		"\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\3\66\3\66\3\66\5\66\u030e\n\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\7\66\u032e\n\66\f\66\16"+
		"\66\u0331\13\66\3\67\3\67\3\67\3\67\3\67\5\67\u0338\n\67\3\67\3\67\38"+
		"\38\38\38\38\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\59"+
		"\u0353\n9\3:\3:\3:\3;\3;\3;\5;\u035b\n;\3<\3<\3<\3=\3=\3=\3=\7=\u0364"+
		"\n=\f=\16=\u0367\13=\3=\3=\3=\3=\5=\u036d\n=\3>\5>\u0370\n>\3>\3>\5>\u0374"+
		"\n>\3?\3?\3?\3?\3?\3?\3?\3?\5?\u037e\n?\3@\3@\3@\3@\3@\5@\u0385\n@\3@"+
		"\3@\3@\3@\3@\3@\3@\3@\3@\3@\5@\u0391\n@\7@\u0393\n@\f@\16@\u0396\13@\3"+
		"A\3A\5A\u039a\nA\3A\5A\u039d\nA\3A\3A\3B\3B\3B\3B\3B\5B\u03a6\nB\3B\3"+
		"B\7B\u03aa\nB\fB\16B\u03ad\13B\3B\3B\3C\3C\3C\3C\3D\3D\5D\u03b7\nD\3D"+
		"\3D\3D\3D\3D\3D\5D\u03bf\nD\3D\3D\3D\3D\5D\u03c5\nD\3E\3E\3E\3E\3E\3E"+
		"\3E\3E\5E\u03cf\nE\3F\3F\3F\3F\3F\3F\3F\3F\3F\5F\u03da\nF\3G\3G\3G\3H"+
		"\3H\3H\3H\7H\u03e3\nH\fH\16H\u03e6\13H\3H\5H\u03e9\nH\5H\u03eb\nH\3H\3"+
		"H\3I\3I\3I\3I\3I\3I\3I\3I\3I\3I\5I\u03f9\nI\3J\3J\5J\u03fd\nJ\3J\3J\5"+
		"J\u0401\nJ\3J\5J\u0404\nJ\3J\3J\3J\3J\3J\5J\u040b\nJ\3J\3J\3K\3K\3L\3"+
		"L\3M\3M\3N\3N\5N\u0417\nN\3N\5N\u041a\nN\3N\3N\3N\3N\3N\5N\u0421\nN\5"+
		"N\u0423\nN\3O\3O\5O\u0427\nO\3O\5O\u042a\nO\3O\5O\u042d\nO\3O\3O\7O\u0431"+
		"\nO\fO\16O\u0434\13O\3O\3O\3P\3P\5P\u043a\nP\3P\5P\u043d\nP\3P\3P\3P\7"+
		"P\u0442\nP\fP\16P\u0445\13P\3P\3P\3Q\5Q\u044a\nQ\3Q\3Q\3R\3R\3R\3R\3R"+
		"\5R\u0453\nR\3S\3S\3S\3T\3T\3T\3T\3T\3T\7T\u045e\nT\fT\16T\u0461\13T\3"+
		"T\5T\u0464\nT\3U\5U\u0467\nU\3U\3U\3V\3V\3W\3W\3W\5W\u0470\nW\3X\3X\3"+
		"X\3X\3X\3X\7X\u0478\nX\fX\16X\u047b\13X\3X\5X\u047e\nX\3Y\3Y\5Y\u0482"+
		"\nY\3Y\3Y\5Y\u0486\nY\3Z\3Z\3Z\7Z\u048b\nZ\fZ\16Z\u048e\13Z\3[\3[\3[\7"+
		"[\u0493\n[\f[\16[\u0496\13[\3\\\3\\\3\\\3\\\3\\\3\\\7\\\u049e\n\\\f\\"+
		"\16\\\u04a1\13\\\3\\\5\\\u04a4\n\\\3]\3]\5]\u04a8\n]\3]\3]\3^\3^\3^\3"+
		"^\3^\3^\7^\u04b2\n^\f^\16^\u04b5\13^\3^\5^\u04b8\n^\3_\3_\5_\u04bc\n_"+
		"\3_\3_\3`\3`\3`\6`\u04c3\n`\r`\16`\u04c4\3a\3a\3a\3a\3a\3a\5a\u04cd\n"+
		"a\3b\3b\3c\3c\3c\3c\3d\3d\3d\3e\3e\3e\3e\3f\3f\3g\3g\3g\5g\u04e1\ng\3"+
		"h\3h\5h\u04e5\nh\3i\3i\5i\u04e9\ni\3j\3j\5j\u04ed\nj\3k\3k\3k\3l\3l\3"+
		"m\3m\3m\3n\3n\5n\u04f9\nn\3o\3o\3o\5o\u04fe\no\3p\3p\3p\5p\u0503\np\3"+
		"q\3q\5q\u0507\nq\3q\3q\3q\3q\3q\3q\3r\3r\3r\5r\u0512\nr\3s\3s\3s\5s\u0517"+
		"\ns\3t\3t\5t\u051b\nt\3t\3t\3t\5t\u0520\nt\7t\u0522\nt\ft\16t\u0525\13"+
		"t\3u\3u\3u\7u\u052a\nu\fu\16u\u052d\13u\3u\3u\3v\3v\3v\5v\u0534\nv\3w"+
		"\3w\3w\5w\u0539\nw\3w\5w\u053c\nw\3x\3x\3x\3x\3x\3x\5x\u0544\nx\3x\3x"+
		"\3y\3y\3y\3y\5y\u054c\ny\3y\3y\3z\5z\u0551\nz\3z\3z\5z\u0555\nz\3z\3z"+
		"\5z\u0559\nz\3{\3{\3{\3{\3{\3{\5{\u0561\n{\3{\3{\3{\3|\3|\3|\3}\3}\5}"+
		"\u056b\n}\3~\3~\3~\3~\3~\3\177\3\177\3\u0080\3\u0080\3\u0081\3\u0081\3"+
		"\u0081\3\u0082\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\5\u0084\u0588"+
		"\n\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086"+
		"\3\u0086\3\u0086\5\u0086\u0594\n\u0086\3\u0087\3\u0087\5\u0087\u0598\n"+
		"\u0087\3\u0088\3\u0088\3\u0088\3\u0088\7\u0088\u059e\n\u0088\f\u0088\16"+
		"\u0088\u05a1\13\u0088\3\u0088\5\u0088\u05a4\n\u0088\5\u0088\u05a6\n\u0088"+
		"\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u0089\5\u0089\u05ae\n\u0089"+
		"\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\5\u008a"+
		"\u05b8\n\u008a\3\u008b\3\u008b\3\u008b\5\u008b\u05bd\n\u008b\3\u008c\3"+
		"\u008c\3\u008d\3\u008d\3\u008d\5\u008d\u05c4\n\u008d\3\u008e\3\u008e\3"+
		"\u008e\3\u008e\3\u008f\3\u008f\3\u008f\3\u0090\3\u0090\3\u0090\5\u0090"+
		"\u05d0\n\u0090\5\u0090\u05d2\n\u0090\3\u0090\3\u0090\3\u0091\3\u0091\3"+
		"\u0091\7\u0091\u05d9\n\u0091\f\u0091\16\u0091\u05dc\13\u0091\3\u0092\3"+
		"\u0092\3\u0092\5\u0092\u05e1\n\u0092\3\u0092\3\u0092\3\u0093\3\u0093\3"+
		"\u0093\5\u0093\u05e8\n\u0093\3\u0094\3\u0094\5\u0094\u05ec\n\u0094\3\u0095"+
		"\3\u0095\3\u0095\3\u0095\3\u0095\7\u0095\u05f3\n\u0095\f\u0095\16\u0095"+
		"\u05f6\13\u0095\3\u0095\3\u0095\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096"+
		"\5\u0096\u05ff\n\u0096\3\u0096\5\u0096\u0602\n\u0096\3\u0097\3\u0097\3"+
		"\u0098\5\u0098\u0607\n\u0098\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\3"+
		"\u0099\3\u009a\3\u009a\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\3\u009b"+
		"\3\u009b\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\5\u009c\u061d\n\u009c"+
		"\5\u009c\u061f\n\u009c\3\u009c\5\u009c\u0622\n\u009c\3\u009c\5\u009c\u0625"+
		"\n\u009c\5\u009c\u0627\n\u009c\3\u009c\3\u009c\3\u009d\3\u009d\3\u009d"+
		"\3\u009d\3\u009e\3\u009e\3\u009e\2\4j~\u009f\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|"+
		"~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096"+
		"\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae"+
		"\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6"+
		"\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de"+
		"\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6"+
		"\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e"+
		"\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126"+
		"\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138\u013a\2\25\3\2"+
		"\25\26\3\2\5\b\3\2\22\23\3\2=>\3\2&(\4\2&(**\6\2%%++..\61\61\3\2\u0086"+
		"\u008c\4\2\u0081\u0085\u008a\u008b\6\2  tt\u0080\u0080\u0087\u0089\3\2"+
		"\34\36\3\2\31\33\3\2z\177\3\2?N\4\2\u0080\u0085\u0087\u008b\4\2hhss\3"+
		"\2tu\4\2\u008d\u0090\u0094\u0095\3\2\u009b\u009c\2\u0696\2\u013c\3\2\2"+
		"\2\4\u0144\3\2\2\2\6\u0150\3\2\2\2\b\u0152\3\2\2\2\n\u015d\3\2\2\2\f\u0167"+
		"\3\2\2\2\16\u0169\3\2\2\2\20\u0175\3\2\2\2\22\u0178\3\2\2\2\24\u017b\3"+
		"\2\2\2\26\u018f\3\2\2\2\30\u0191\3\2\2\2\32\u0196\3\2\2\2\34\u019b\3\2"+
		"\2\2\36\u01a0\3\2\2\2 \u01a8\3\2\2\2\"\u01ab\3\2\2\2$\u01b6\3\2\2\2&\u01c3"+
		"\3\2\2\2(\u01c5\3\2\2\2*\u01c7\3\2\2\2,\u01cc\3\2\2\2.\u01d4\3\2\2\2\60"+
		"\u01d6\3\2\2\2\62\u01eb\3\2\2\2\64\u01ed\3\2\2\2\66\u01fd\3\2\2\28\u01ff"+
		"\3\2\2\2:\u020a\3\2\2\2<\u0221\3\2\2\2>\u022b\3\2\2\2@\u022d\3\2\2\2B"+
		"\u0234\3\2\2\2D\u023c\3\2\2\2F\u024a\3\2\2\2H\u024c\3\2\2\2J\u0253\3\2"+
		"\2\2L\u027b\3\2\2\2N\u027d\3\2\2\2P\u0283\3\2\2\2R\u0288\3\2\2\2T\u028f"+
		"\3\2\2\2V\u02a6\3\2\2\2X\u02b6\3\2\2\2Z\u02b8\3\2\2\2\\\u02bf\3\2\2\2"+
		"^\u02c9\3\2\2\2`\u02cd\3\2\2\2b\u02d7\3\2\2\2d\u02e2\3\2\2\2f\u02f5\3"+
		"\2\2\2h\u02f7\3\2\2\2j\u030d\3\2\2\2l\u0332\3\2\2\2n\u033b\3\2\2\2p\u0352"+
		"\3\2\2\2r\u0354\3\2\2\2t\u0357\3\2\2\2v\u035c\3\2\2\2x\u0365\3\2\2\2z"+
		"\u036f\3\2\2\2|\u037d\3\2\2\2~\u0384\3\2\2\2\u0080\u0397\3\2\2\2\u0082"+
		"\u03a0\3\2\2\2\u0084\u03b0\3\2\2\2\u0086\u03c4\3\2\2\2\u0088\u03ce\3\2"+
		"\2\2\u008a\u03d9\3\2\2\2\u008c\u03db\3\2\2\2\u008e\u03de\3\2\2\2\u0090"+
		"\u03f8\3\2\2\2\u0092\u03fa\3\2\2\2\u0094\u040e\3\2\2\2\u0096\u0410\3\2"+
		"\2\2\u0098\u0412\3\2\2\2\u009a\u0414\3\2\2\2\u009c\u0424\3\2\2\2\u009e"+
		"\u0437\3\2\2\2\u00a0\u0449\3\2\2\2\u00a2\u0452\3\2\2\2\u00a4\u0454\3\2"+
		"\2\2\u00a6\u0457\3\2\2\2\u00a8\u0466\3\2\2\2\u00aa\u046a\3\2\2\2\u00ac"+
		"\u046f\3\2\2\2\u00ae\u0471\3\2\2\2\u00b0\u047f\3\2\2\2\u00b2\u0487\3\2"+
		"\2\2\u00b4\u048f\3\2\2\2\u00b6\u0497\3\2\2\2\u00b8\u04a5\3\2\2\2\u00ba"+
		"\u04ab\3\2\2\2\u00bc\u04b9\3\2\2\2\u00be\u04c2\3\2\2\2\u00c0\u04cc\3\2"+
		"\2\2\u00c2\u04ce\3\2\2\2\u00c4\u04d0\3\2\2\2\u00c6\u04d4\3\2\2\2\u00c8"+
		"\u04d7\3\2\2\2\u00ca\u04db\3\2\2\2\u00cc\u04dd\3\2\2\2\u00ce\u04e2\3\2"+
		"\2\2\u00d0\u04e6\3\2\2\2\u00d2\u04ea\3\2\2\2\u00d4\u04ee\3\2\2\2\u00d6"+
		"\u04f1\3\2\2\2\u00d8\u04f3\3\2\2\2\u00da\u04f8\3\2\2\2\u00dc\u04fa\3\2"+
		"\2\2\u00de\u0502\3\2\2\2\u00e0\u0506\3\2\2\2\u00e2\u050e\3\2\2\2\u00e4"+
		"\u0516\3\2\2\2\u00e6\u051a\3\2\2\2\u00e8\u0526\3\2\2\2\u00ea\u0530\3\2"+
		"\2\2\u00ec\u053b\3\2\2\2\u00ee\u0543\3\2\2\2\u00f0\u0547\3\2\2\2\u00f2"+
		"\u0550\3\2\2\2\u00f4\u0560\3\2\2\2\u00f6\u0565\3\2\2\2\u00f8\u056a\3\2"+
		"\2\2\u00fa\u056c\3\2\2\2\u00fc\u0571\3\2\2\2\u00fe\u0573\3\2\2\2\u0100"+
		"\u0575\3\2\2\2\u0102\u0578\3\2\2\2\u0104\u057c\3\2\2\2\u0106\u0587\3\2"+
		"\2\2\u0108\u058b\3\2\2\2\u010a\u0593\3\2\2\2\u010c\u0597\3\2\2\2\u010e"+
		"\u0599\3\2\2\2\u0110\u05a9\3\2\2\2\u0112\u05b7\3\2\2\2\u0114\u05bc\3\2"+
		"\2\2\u0116\u05be\3\2\2\2\u0118\u05c0\3\2\2\2\u011a\u05c5\3\2\2\2\u011c"+
		"\u05c9\3\2\2\2\u011e\u05cc\3\2\2\2\u0120\u05d5\3\2\2\2\u0122\u05e0\3\2"+
		"\2\2\u0124\u05e7\3\2\2\2\u0126\u05eb\3\2\2\2\u0128\u05ed\3\2\2\2\u012a"+
		"\u05fe\3\2\2\2\u012c\u0603\3\2\2\2\u012e\u0606\3\2\2\2\u0130\u060a\3\2"+
		"\2\2\u0132\u060e\3\2\2\2\u0134\u0612\3\2\2\2\u0136\u0617\3\2\2\2\u0138"+
		"\u062a\3\2\2\2\u013a\u062e\3\2\2\2\u013c\u0141\5\4\3\2\u013d\u013e\7p"+
		"\2\2\u013e\u0140\5\4\3\2\u013f\u013d\3\2\2\2\u0140\u0143\3\2\2\2\u0141"+
		"\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\3\3\2\2\2\u0143\u0141\3\2\2\2"+
		"\u0144\u0146\7h\2\2\u0145\u0147\78\2\2\u0146\u0145\3\2\2\2\u0146\u0147"+
		"\3\2\2\2\u0147\5\3\2\2\2\u0148\u0149\7\30\2\2\u0149\u0151\5p9\2\u014a"+
		"\u014b\7\5\2\2\u014b\u0151\5j\66\2\u014c\u014d\t\2\2\2\u014d\u0151\5\f"+
		"\7\2\u014e\u014f\t\3\2\2\u014f\u0151\5j\66\2\u0150\u0148\3\2\2\2\u0150"+
		"\u014a\3\2\2\2\u0150\u014c\3\2\2\2\u0150\u014e\3\2\2\2\u0151\7\3\2\2\2"+
		"\u0152\u0157\5\n\6\2\u0153\u0154\7p\2\2\u0154\u0156\5\n\6\2\u0155\u0153"+
		"\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158"+
		"\u015b\3\2\2\2\u0159\u0157\3\2\2\2\u015a\u015c\7p\2\2\u015b\u015a\3\2"+
		"\2\2\u015b\u015c\3\2\2\2\u015c\t\3\2\2\2\u015d\u0162\7h\2\2\u015e\u015f"+
		"\7p\2\2\u015f\u0161\7h\2\2\u0160\u015e\3\2\2\2\u0161\u0164\3\2\2\2\u0162"+
		"\u0160\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0165\3\2\2\2\u0164\u0162\3\2"+
		"\2\2\u0165\u0166\5\u00fe\u0080\2\u0166\13\3\2\2\2\u0167\u0168\5~@\2\u0168"+
		"\r\3\2\2\2\u0169\u016a\7\24\2\2\u016a\u016b\7i\2\2\u016b\u0171\5j\66\2"+
		"\u016c\u016f\7p\2\2\u016d\u0170\7h\2\2\u016e\u0170\5j\66\2\u016f\u016d"+
		"\3\2\2\2\u016f\u016e\3\2\2\2\u0170\u0172\3\2\2\2\u0171\u016c\3\2\2\2\u0171"+
		"\u0172\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0174\7j\2\2\u0174\17\3\2\2\2"+
		"\u0175\u0176\5j\66\2\u0176\u0177\7\2\2\3\u0177\21\3\2\2\2\u0178\u0179"+
		"\5p9\2\u0179\u017a\7\2\2\3\u017a\23\3\2\2\2\u017b\u017c\5\u0088E\2\u017c"+
		"\u017d\7\2\2\3\u017d\25\3\2\2\2\u017e\u0190\5H%\2\u017f\u0190\5\16\b\2"+
		"\u0180\u0190\5*\26\2\u0181\u0190\5,\27\2\u0182\u0190\5$\23\2\u0183\u0190"+
		"\5\36\20\2\u0184\u0190\5\32\16\2\u0185\u0190\5\30\r\2\u0186\u0190\5\34"+
		"\17\2\u0187\u0188\t\4\2\2\u0188\u0189\5\b\5\2\u0189\u018a\7r\2\2\u018a"+
		"\u018b\7r\2\2\u018b\u018c\5 \21\2\u018c\u018d\5j\66\2\u018d\u0190\3\2"+
		"\2\2\u018e\u0190\t\5\2\2\u018f\u017e\3\2\2\2\u018f\u017f\3\2\2\2\u018f"+
		"\u0180\3\2\2\2\u018f\u0181\3\2\2\2\u018f\u0182\3\2\2\2\u018f\u0183\3\2"+
		"\2\2\u018f\u0184\3\2\2\2\u018f\u0185\3\2\2\2\u018f\u0186\3\2\2\2\u018f"+
		"\u0187\3\2\2\2\u018f\u018e\3\2\2\2\u0190\27\3\2\2\2\u0191\u0192\7/\2\2"+
		"\u0192\u0193\7i\2\2\u0193\u0194\5j\66\2\u0194\u0195\7j\2\2\u0195\31\3"+
		"\2\2\2\u0196\u0197\7\63\2\2\u0197\u0198\7m\2\2\u0198\u0199\5\u0088E\2"+
		"\u0199\u019a\7n\2\2\u019a\33\3\2\2\2\u019b\u019c\7\60\2\2\u019c\u019d"+
		"\7i\2\2\u019d\u019e\5j\66\2\u019e\u019f\7j\2\2\u019f\35\3\2\2\2\u01a0"+
		"\u01a1\t\6\2\2\u01a1\u01a2\7i\2\2\u01a2\u01a3\5j\66\2\u01a3\u01a4\7j\2"+
		"\2\u01a4\37\3\2\2\2\u01a5\u01a7\5\"\22\2\u01a6\u01a5\3\2\2\2\u01a7\u01aa"+
		"\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9!\3\2\2\2\u01aa"+
		"\u01a8\3\2\2\2\u01ab\u01ac\7k\2\2\u01ac\u01b1\5j\66\2\u01ad\u01ae\7p\2"+
		"\2\u01ae\u01b0\5j\66\2\u01af\u01ad\3\2\2\2\u01b0\u01b3\3\2\2\2\u01b1\u01af"+
		"\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2\u01b4\3\2\2\2\u01b3\u01b1\3\2\2\2\u01b4"+
		"\u01b5\7l\2\2\u01b5#\3\2\2\2\u01b6\u01bb\7\20\2\2\u01b7\u01b8\7m\2\2\u01b8"+
		"\u01b9\5&\24\2\u01b9\u01ba\7n\2\2\u01ba\u01bc\3\2\2\2\u01bb\u01b7\3\2"+
		"\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\7i\2\2\u01be"+
		"\u01bf\5j\66\2\u01bf\u01c0\7j\2\2\u01c0%\3\2\2\2\u01c1\u01c4\5(\25\2\u01c2"+
		"\u01c4\7\21\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c2\3\2\2\2\u01c4\'\3\2\2"+
		"\2\u01c5\u01c6\7h\2\2\u01c6)\3\2\2\2\u01c7\u01c8\7\65\2\2\u01c8\u01c9"+
		"\7i\2\2\u01c9\u01ca\5j\66\2\u01ca\u01cb\7j\2\2\u01cb+\3\2\2\2\u01cc\u01cd"+
		"\7\66\2\2\u01cd\u01ce\7i\2\2\u01ce\u01cf\5j\66\2\u01cf\u01d0\7j\2\2\u01d0"+
		"-\3\2\2\2\u01d1\u01d5\5\66\34\2\u01d2\u01d5\5\64\33\2\u01d3\u01d5\5\60"+
		"\31\2\u01d4\u01d1\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d4\u01d3\3\2\2\2\u01d5"+
		"/\3\2\2\2\u01d6\u01d7\7\61\2\2\u01d7\u01dd\7k\2\2\u01d8\u01d9\5\62\32"+
		"\2\u01d9\u01da\5\u00a2R\2\u01da\u01dc\3\2\2\2\u01db\u01d8\3\2\2\2\u01dc"+
		"\u01df\3\2\2\2\u01dd\u01db\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01e0\3\2"+
		"\2\2\u01df\u01dd\3\2\2\2\u01e0\u01e1\7l\2\2\u01e1\61\3\2\2\2\u01e2\u01e3"+
		"\7Q\2\2\u01e3\u01e4\7h\2\2\u01e4\u01ec\5\u010a\u0086\2\u01e5\u01e6\7\62"+
		"\2\2\u01e6\u01e7\7k\2\2\u01e7\u01e8\5j\66\2\u01e8\u01e9\5\u00a2R\2\u01e9"+
		"\u01ea\7l\2\2\u01ea\u01ec\3\2\2\2\u01eb\u01e2\3\2\2\2\u01eb\u01e5\3\2"+
		"\2\2\u01ec\63\3\2\2\2\u01ed\u01ee\7\30\2\2\u01ee\u01ef\7m\2\2\u01ef\u01f0"+
		"\7n\2\2\u01f0\u01f1\5\u00fe\u0080\2\u01f1\65\3\2\2\2\u01f2\u01f3\t\7\2"+
		"\2\u01f3\u01f4\7m\2\2\u01f4\u01f5\5\u0088E\2\u01f5\u01f6\7n\2\2\u01f6"+
		"\u01fe\3\2\2\2\u01f7\u01f8\7)\2\2\u01f8\u01f9\7m\2\2\u01f9\u01fa\5\u0088"+
		"E\2\u01fa\u01fb\7n\2\2\u01fb\u01fc\5\u0088E\2\u01fc\u01fe\3\2\2\2\u01fd"+
		"\u01f2\3\2\2\2\u01fd\u01f7\3\2\2\2\u01fe\67\3\2\2\2\u01ff\u0200\7m\2\2"+
		"\u0200\u0205\5:\36\2\u0201\u0202\7p\2\2\u0202\u0204\5:\36\2\u0203\u0201"+
		"\3\2\2\2\u0204\u0207\3\2\2\2\u0205\u0203\3\2\2\2\u0205\u0206\3\2\2\2\u0206"+
		"\u0208\3\2\2\2\u0207\u0205\3\2\2\2\u0208\u0209\7n\2\2\u02099\3\2\2\2\u020a"+
		"\u020b\5j\66\2\u020b\u020c\7o\2\2\u020c\u020d\5j\66\2\u020d;\3\2\2\2\u020e"+
		"\u020f\5> \2\u020f\u0210\5\u00a2R\2\u0210\u0212\3\2\2\2\u0211\u020e\3"+
		"\2\2\2\u0212\u0215\3\2\2\2\u0213\u0211\3\2\2\2\u0213\u0214\3\2\2\2\u0214"+
		"\u0216\3\2\2\2\u0215\u0213\3\2\2\2\u0216\u0222\7\16\2\2\u0217\u021a\5"+
		"> \2\u0218\u021a\7\16\2\2\u0219\u0217\3\2\2\2\u0219\u0218\3\2\2\2\u021a"+
		"\u021b\3\2\2\2\u021b\u021d\5\u00a2R\2\u021c\u0219\3\2\2\2\u021d\u0220"+
		"\3\2\2\2\u021e\u021c\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0222\3\2\2\2\u0220"+
		"\u021e\3\2\2\2\u0221\u0213\3\2\2\2\u0221\u021e\3\2\2\2\u0222=\3\2\2\2"+
		"\u0223\u0224\7\t\2\2\u0224\u022c\5F$\2\u0225\u0226\7\n\2\2\u0226\u022c"+
		"\5F$\2\u0227\u0228\7\13\2\2\u0228\u022c\5F$\2\u0229\u022a\7\r\2\2\u022a"+
		"\u022c\5z>\2\u022b\u0223\3\2\2\2\u022b\u0225\3\2\2\2\u022b\u0227\3\2\2"+
		"\2\u022b\u0229\3\2\2\2\u022c?\3\2\2\2\u022d\u022e\5<\37\2\u022e\u022f"+
		"\7Q\2\2\u022f\u0230\7h\2\2\u0230\u0232\5\u010a\u0086\2\u0231\u0233\5D"+
		"#\2\u0232\u0231\3\2\2\2\u0232\u0233\3\2\2\2\u0233A\3\2\2\2\u0234\u0235"+
		"\5<\37\2\u0235\u0236\7Q\2\2\u0236\u0237\5`\61\2\u0237\u0238\7h\2\2\u0238"+
		"\u023a\5\u010a\u0086\2\u0239\u023b\5D#\2\u023a\u0239\3\2\2\2\u023a\u023b"+
		"\3\2\2\2\u023bC\3\2\2\2\u023c\u0241\7k\2\2\u023d\u023e\7\67\2\2\u023e"+
		"\u023f\5\u00b2Z\2\u023f\u0240\5\u00a2R\2\u0240\u0242\3\2\2\2\u0241\u023d"+
		"\3\2\2\2\u0241\u0242\3\2\2\2\u0242\u0244\3\2\2\2\u0243\u0245\5\u00be`"+
		"\2\u0244\u0243\3\2\2\2\u0244\u0245\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u0247"+
		"\7l\2\2\u0247E\3\2\2\2\u0248\u024b\3\2\2\2\u0249\u024b\5j\66\2\u024a\u0248"+
		"\3\2\2\2\u024a\u0249\3\2\2\2\u024bG\3\2\2\2\u024c\u024d\t\6\2\2\u024d"+
		"\u024e\7m\2\2\u024e\u024f\5j\66\2\u024f\u0250\79\2\2\u0250\u0251\5j\66"+
		"\2\u0251\u0252\7n\2\2\u0252I\3\2\2\2\u0253\u0254\5\u00a4S\2\u0254\u025a"+
		"\5\u00a2R\2\u0255\u0256\5\u00a6T\2\u0256\u0257\5\u00a2R\2\u0257\u0259"+
		"\3\2\2\2\u0258\u0255\3\2\2\2\u0259\u025c\3\2\2\2\u025a\u0258\3\2\2\2\u025a"+
		"\u025b\3\2\2\2\u025b\u0267\3\2\2\2\u025c\u025a\3\2\2\2\u025d\u0262\5@"+
		"!\2\u025e\u0262\5B\"\2\u025f\u0262\5\u00acW\2\u0260\u0262\5L\'\2\u0261"+
		"\u025d\3\2\2\2\u0261\u025e\3\2\2\2\u0261\u025f\3\2\2\2\u0261\u0260\3\2"+
		"\2\2\u0262\u0263\3\2\2\2\u0263\u0264\5\u00a2R\2\u0264\u0266\3\2\2\2\u0265"+
		"\u0261\3\2\2\2\u0266\u0269\3\2\2\2\u0267\u0265\3\2\2\2\u0267\u0268\3\2"+
		"\2\2\u0268\u026a\3\2\2\2\u0269\u0267\3\2\2\2\u026a\u026b\7\2\2\3\u026b"+
		"K\3\2\2\2\u026c\u027c\5N(\2\u026d\u027c\5R*\2\u026e\u027c\5T+\2\u026f"+
		"\u0273\7\30\2\2\u0270\u0271\7\30\2\2\u0271\u0273\5\u00a2R\2\u0272\u026f"+
		"\3\2\2\2\u0272\u0270\3\2\2\2\u0273\u0279\3\2\2\2\u0274\u027a\5B\"\2\u0275"+
		"\u027a\5@!\2\u0276\u027a\5\u00aeX\2\u0277\u027a\5\u00b6\\\2\u0278\u027a"+
		"\5\u00ba^\2\u0279\u0274\3\2\2\2\u0279\u0275\3\2\2\2\u0279\u0276\3\2\2"+
		"\2\u0279\u0277\3\2\2\2\u0279\u0278\3\2\2\2\u027a\u027c\3\2\2\2\u027b\u026c"+
		"\3\2\2\2\u027b\u026d\3\2\2\2\u027b\u026e\3\2\2\2\u027b\u0272\3\2\2\2\u027c"+
		"M\3\2\2\2\u027d\u027e\7\64\2\2\u027e\u027f\7h\2\2\u027f\u0281\5\u010e"+
		"\u0088\2\u0280\u0282\5P)\2\u0281\u0280\3\2\2\2\u0281\u0282\3\2\2\2\u0282"+
		"O\3\2\2\2\u0283\u0284\7k\2\2\u0284\u0285\5j\66\2\u0285\u0286\5\u00a2R"+
		"\2\u0286\u0287\7l\2\2\u0287Q\3\2\2\2\u0288\u0289\7\64\2\2\u0289\u028a"+
		"\5`\61\2\u028a\u028b\7h\2\2\u028b\u028d\5\u010e\u0088\2\u028c\u028e\5"+
		"P)\2\u028d\u028c\3\2\2\2\u028d\u028e\3\2\2\2\u028eS\3\2\2\2\u028f\u0290"+
		"\5\u0088E\2\u0290\u0291\7\17\2\2\u0291\u0292\5\u0088E\2\u0292\u0298\7"+
		"k\2\2\u0293\u0294\5Z.\2\u0294\u0295\5\u00a2R\2\u0295\u0297\3\2\2\2\u0296"+
		"\u0293\3\2\2\2\u0297\u029a\3\2\2\2\u0298\u0296\3\2\2\2\u0298\u0299\3\2"+
		"\2\2\u0299\u02a0\3\2\2\2\u029a\u0298\3\2\2\2\u029b\u029c\5V,\2\u029c\u029d"+
		"\5\u00a2R\2\u029d\u029f\3\2\2\2\u029e\u029b\3\2\2\2\u029f\u02a2\3\2\2"+
		"\2\u02a0\u029e\3\2\2\2\u02a0\u02a1\3\2\2\2\u02a1\u02a3\3\2\2\2\u02a2\u02a0"+
		"\3\2\2\2\u02a3\u02a4\7l\2\2\u02a4U\3\2\2\2\u02a5\u02a7\7\16\2\2\u02a6"+
		"\u02a5\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02a8\3\2\2\2\u02a8\u02a9\5b"+
		"\62\2\u02a9\u02aa\7h\2\2\u02aa\u02ac\5\u010a\u0086\2\u02ab\u02ad\5\u00bc"+
		"_\2\u02ac\u02ab\3\2\2\2\u02ac\u02ad\3\2\2\2\u02adW\3\2\2\2\u02ae\u02af"+
		"\5~@\2\u02af\u02b0\7s\2\2\u02b0\u02b1\7h\2\2\u02b1\u02b7\3\2\2\2\u02b2"+
		"\u02b3\5\u0088E\2\u02b3\u02b4\7s\2\2\u02b4\u02b5\7h\2\2\u02b5\u02b7\3"+
		"\2\2\2\u02b6\u02ae\3\2\2\2\u02b6\u02b2\3\2\2\2\u02b7Y\3\2\2\2\u02b8\u02b9"+
		"\7\64\2\2\u02b9\u02ba\7h\2\2\u02ba\u02bd\7v\2\2\u02bb\u02be\5X-\2\u02bc"+
		"\u02be\5\u0118\u008d\2\u02bd\u02bb\3\2\2\2\u02bd\u02bc\3\2\2\2\u02be["+
		"\3\2\2\2\u02bf\u02c7\5\2\2\2\u02c0\u02c3\5\u0088E\2\u02c1\u02c2\7o\2\2"+
		"\u02c2\u02c4\5\u00b4[\2\u02c3\u02c1\3\2\2\2\u02c3\u02c4\3\2\2\2\u02c4"+
		"\u02c8\3\2\2\2\u02c5\u02c6\7o\2\2\u02c6\u02c8\5\u00b4[\2\u02c7\u02c0\3"+
		"\2\2\2\u02c7\u02c5\3\2\2\2\u02c8]\3\2\2\2\u02c9\u02ca\5\2\2\2\u02ca\u02cb"+
		"\7v\2\2\u02cb\u02cc\5\u00b4[\2\u02cc_\3\2\2\2\u02cd\u02cf\7i\2\2\u02ce"+
		"\u02d0\5\4\3\2\u02cf\u02ce\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0\u02d1\3\2"+
		"\2\2\u02d1\u02d3\5\u0088E\2\u02d2\u02d4\7p\2\2\u02d3\u02d2\3\2\2\2\u02d3"+
		"\u02d4\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5\u02d6\7j\2\2\u02d6a\3\2\2\2\u02d7"+
		"\u02d9\7i\2\2\u02d8\u02da\7h\2\2\u02d9\u02d8\3\2\2\2\u02d9\u02da\3\2\2"+
		"\2\u02da\u02dc\3\2\2\2\u02db\u02dd\7\u008a\2\2\u02dc\u02db\3\2\2\2\u02dc"+
		"\u02dd\3\2\2\2\u02dd\u02de\3\2\2\2\u02de\u02df\5\u00f8}\2\u02df\u02e0"+
		"\7j\2\2\u02e0c\3\2\2\2\u02e1\u02e3\7\30\2\2\u02e2\u02e1\3\2\2\2\u02e2"+
		"\u02e3\3\2\2\2\u02e3\u02e5\3\2\2\2\u02e4\u02e6\5\u00b2Z\2\u02e5\u02e4"+
		"\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6\u02e8\3\2\2\2\u02e7\u02e9\7w\2\2\u02e8"+
		"\u02e7\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02ea\3\2\2\2\u02ea\u02eb\5\u0088"+
		"E\2\u02ebe\3\2\2\2\u02ec\u02f6\5~@\2\u02ed\u02ee\t\b\2\2\u02ee\u02ef\7"+
		"i\2\2\u02ef\u02f0\5j\66\2\u02f0\u02f1\7j\2\2\u02f1\u02f6\3\2\2\2\u02f2"+
		"\u02f6\5h\65\2\u02f3\u02f4\t\t\2\2\u02f4\u02f6\5j\66\2\u02f5\u02ec\3\2"+
		"\2\2\u02f5\u02ed\3\2\2\2\u02f5\u02f2\3\2\2\2\u02f5\u02f3\3\2\2\2\u02f6"+
		"g\3\2\2\2\u02f7\u02f8\7\27\2\2\u02f8\u02f9\5\f\7\2\u02f9\u02fa\7\31\2"+
		"\2\u02fa\u02fb\5j\66\2\u02fbi\3\2\2\2\u02fc\u02fd\b\66\1\2\u02fd\u02fe"+
		"\7a\2\2\u02fe\u02ff\7m\2\2\u02ff\u0300\5\u0088E\2\u0300\u0301\7n\2\2\u0301"+
		"\u030e\3\2\2\2\u0302\u0303\t\b\2\2\u0303\u0304\7i\2\2\u0304\u0305\5j\66"+
		"\2\u0305\u0306\7j\2\2\u0306\u030e\3\2\2\2\u0307\u030e\5h\65\2\u0308\u030e"+
		"\5n8\2\u0309\u030e\5l\67\2\u030a\u030b\t\t\2\2\u030b\u030e\5j\66\r\u030c"+
		"\u030e\5~@\2\u030d\u02fc\3\2\2\2\u030d\u0302\3\2\2\2\u030d\u0307\3\2\2"+
		"\2\u030d\u0308\3\2\2\2\u030d\u0309\3\2\2\2\u030d\u030a\3\2\2\2\u030d\u030c"+
		"\3\2\2\2\u030e\u032f\3\2\2\2\u030f\u0310\f\13\2\2\u0310\u0311\t\n\2\2"+
		"\u0311\u032e\5j\66\f\u0312\u0313\f\n\2\2\u0313\u0314\t\13\2\2\u0314\u032e"+
		"\5j\66\13\u0315\u0316\f\t\2\2\u0316\u0317\t\f\2\2\u0317\u032e\5j\66\n"+
		"\u0318\u0319\f\b\2\2\u0319\u031a\t\r\2\2\u031a\u032e\5j\66\t\u031b\u031c"+
		"\f\7\2\2\u031c\u031d\t\16\2\2\u031d\u032e\5j\66\b\u031e\u031f\f\6\2\2"+
		"\u031f\u0320\7y\2\2\u0320\u032e\5j\66\7\u0321\u0322\f\5\2\2\u0322\u0323"+
		"\7x\2\2\u0323\u032e\5j\66\6\u0324\u0325\f\4\2\2\u0325\u0326\7\37\2\2\u0326"+
		"\u032e\5j\66\4\u0327\u0328\f\3\2\2\u0328\u0329\7\"\2\2\u0329\u032a\5j"+
		"\66\2\u032a\u032b\7r\2\2\u032b\u032c\5j\66\3\u032c\u032e\3\2\2\2\u032d"+
		"\u030f\3\2\2\2\u032d\u0312\3\2\2\2\u032d\u0315\3\2\2\2\u032d\u0318\3\2"+
		"\2\2\u032d\u031b\3\2\2\2\u032d\u031e\3\2\2\2\u032d\u0321\3\2\2\2\u032d"+
		"\u0324\3\2\2\2\u032d\u0327\3\2\2\2\u032e\u0331\3\2\2\2\u032f\u032d\3\2"+
		"\2\2\u032f\u0330\3\2\2\2\u0330k\3\2\2\2\u0331\u032f\3\2\2\2\u0332\u0333"+
		"\7-\2\2\u0333\u0334\7i\2\2\u0334\u0337\5\u0088E\2\u0335\u0336\7p\2\2\u0336"+
		"\u0338\5\u00b4[\2\u0337\u0335\3\2\2\2\u0337\u0338\3\2\2\2\u0338\u0339"+
		"\3\2\2\2\u0339\u033a\7j\2\2\u033am\3\2\2\2\u033b\u033c\7,\2\2\u033c\u033d"+
		"\7i\2\2\u033d\u033e\5\u0088E\2\u033e\u033f\7j\2\2\u033fo\3\2\2\2\u0340"+
		"\u0353\5\6\4\2\u0341\u0353\5t;\2\u0342\u0353\5r:\2\u0343\u0353\5\u00ac"+
		"W\2\u0344\u0353\5\u00ccg\2\u0345\u0353\5\u00c0a\2\u0346\u0353\5\u00f6"+
		"|\2\u0347\u0353\5\u00ceh\2\u0348\u0353\5\u00d0i\2\u0349\u0353\5\u00d2"+
		"j\2\u034a\u0353\5\u00d4k\2\u034b\u0353\5\u00d6l\2\u034c\u0353\5\u00bc"+
		"_\2\u034d\u0353\5\u009aN\2\u034e\u0353\5\u00dan\2\u034f\u0353\5\u00e8"+
		"u\2\u0350\u0353\5v<\2\u0351\u0353\5\u00d8m\2\u0352\u0340\3\2\2\2\u0352"+
		"\u0341\3\2\2\2\u0352\u0342\3\2\2\2\u0352\u0343\3\2\2\2\u0352\u0344\3\2"+
		"\2\2\u0352\u0345\3\2\2\2\u0352\u0346\3\2\2\2\u0352\u0347\3\2\2\2\u0352"+
		"\u0348\3\2\2\2\u0352\u0349\3\2\2\2\u0352\u034a\3\2\2\2\u0352\u034b\3\2"+
		"\2\2\u0352\u034c\3\2\2\2\u0352\u034d\3\2\2\2\u0352\u034e\3\2\2\2\u0352"+
		"\u034f\3\2\2\2\u0352\u0350\3\2\2\2\u0352\u0351\3\2\2\2\u0353q\3\2\2\2"+
		"\u0354\u0355\7!\2\2\u0355\u0356\5j\66\2\u0356s\3\2\2\2\u0357\u0358\7\\"+
		"\2\2\u0358\u035a\5j\66\2\u0359\u035b\5\u00bc_\2\u035a\u0359\3\2\2\2\u035a"+
		"\u035b\3\2\2\2\u035bu\3\2\2\2\u035c\u035d\5x=\2\u035d\u035e\5\u00f0y\2"+
		"\u035ew\3\2\2\2\u035f\u0360\7\f\2\2\u0360\u0361\5j\66\2\u0361\u0362\5"+
		"\u00a2R\2\u0362\u0364\3\2\2\2\u0363\u035f\3\2\2\2\u0364\u0367\3\2\2\2"+
		"\u0365\u0363\3\2\2\2\u0365\u0366\3\2\2\2\u0366\u036c\3\2\2\2\u0367\u0365"+
		"\3\2\2\2\u0368\u0369\7\r\2\2\u0369\u036a\5z>\2\u036a\u036b\5\u00a2R\2"+
		"\u036b\u036d\3\2\2\2\u036c\u0368\3\2\2\2\u036c\u036d\3\2\2\2\u036dy\3"+
		"\2\2\2\u036e\u0370\5\u00b4[\2\u036f\u036e\3\2\2\2\u036f\u0370\3\2\2\2"+
		"\u0370\u0373\3\2\2\2\u0371\u0372\7`\2\2\u0372\u0374\5j\66\2\u0373\u0371"+
		"\3\2\2\2\u0373\u0374\3\2\2\2\u0374{\3\2\2\2\u0375\u037e\7\3\2\2\u0376"+
		"\u037e\7\4\2\2\u0377\u037e\7g\2\2\u0378\u037e\5\u0116\u008c\2\u0379\u037e"+
		"\5\u012c\u0097\2\u037a\u037e\7\u0091\2\2\u037b\u037e\7\u0094\2\2\u037c"+
		"\u037e\7\u0095\2\2\u037d\u0375\3\2\2\2\u037d\u0376\3\2\2\2\u037d\u0377"+
		"\3\2\2\2\u037d\u0378\3\2\2\2\u037d\u0379\3\2\2\2\u037d\u037a\3\2\2\2\u037d"+
		"\u037b\3\2\2\2\u037d\u037c\3\2\2\2\u037e}\3\2\2\2\u037f\u0380\b@\1\2\u0380"+
		"\u0385\5\u0112\u008a\2\u0381\u0385\5\u0110\u0089\2\u0382\u0385\5\u0138"+
		"\u009d\2\u0383\u0385\5\26\f\2\u0384\u037f\3\2\2\2\u0384\u0381\3\2\2\2"+
		"\u0384\u0382\3\2\2\2\u0384\u0383\3\2\2\2\u0385\u0394\3\2\2\2\u0386\u0390"+
		"\f\3\2\2\u0387\u0388\7s\2\2\u0388\u0391\7h\2\2\u0389\u0391\5\u0132\u009a"+
		"\2\u038a\u0391\5\u0092J\2\u038b\u0391\58\35\2\u038c\u0391\5\u0134\u009b"+
		"\2\u038d\u038e\6@\f\2\u038e\u0391\5\u0136\u009c\2\u038f\u0391\5\u0080"+
		"A\2\u0390\u0387\3\2\2\2\u0390\u0389\3\2\2\2\u0390\u038a\3\2\2\2\u0390"+
		"\u038b\3\2\2\2\u0390\u038c\3\2\2\2\u0390\u038d\3\2\2\2\u0390\u038f\3\2"+
		"\2\2\u0391\u0393\3\2\2\2\u0392\u0386\3\2\2\2\u0393\u0396\3\2\2\2\u0394"+
		"\u0392\3\2\2\2\u0394\u0395\3\2\2\2\u0395\177\3\2\2\2\u0396\u0394\3\2\2"+
		"\2\u0397\u0399\7#\2\2\u0398\u039a\5\u00b4[\2\u0399\u0398\3\2\2\2\u0399"+
		"\u039a\3\2\2\2\u039a\u039c\3\2\2\2\u039b\u039d\7p\2\2\u039c\u039b\3\2"+
		"\2\2\u039c\u039d\3\2\2\2\u039d\u039e\3\2\2\2\u039e\u039f\7$\2\2\u039f"+
		"\u0081\3\2\2\2\u03a0\u03a1\7R\2\2\u03a1\u03ab\7k\2\2\u03a2\u03a6\5\u0086"+
		"D\2\u03a3\u03a6\5\u00f8}\2\u03a4\u03a6\5\u0084C\2\u03a5\u03a2\3\2\2\2"+
		"\u03a5\u03a3\3\2\2\2\u03a5\u03a4\3\2\2\2\u03a6\u03a7\3\2\2\2\u03a7\u03a8"+
		"\5\u00a2R\2\u03a8\u03aa\3\2\2\2\u03a9\u03a5\3\2\2\2\u03aa\u03ad\3\2\2"+
		"\2\u03ab\u03a9\3\2\2\2\u03ab\u03ac\3\2\2\2\u03ac\u03ae\3\2\2\2\u03ad\u03ab"+
		"\3\2\2\2\u03ae\u03af\7l\2\2\u03af\u0083\3\2\2\2\u03b0\u03b1\7\64\2\2\u03b1"+
		"\u03b2\7h\2\2\u03b2\u03b3\5\u010e\u0088\2\u03b3\u0085\3\2\2\2\u03b4\u03b6"+
		"\6D\r\2\u03b5\u03b7\7\30\2\2\u03b6\u03b5\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7"+
		"\u03b8\3\2\2\2\u03b8\u03b9\5<\37\2\u03b9\u03ba\7h\2\2\u03ba\u03bb\5\u010e"+
		"\u0088\2\u03bb\u03bc\5\u010c\u0087\2\u03bc\u03c5\3\2\2\2\u03bd\u03bf\7"+
		"\30\2\2\u03be\u03bd\3\2\2\2\u03be\u03bf\3\2\2\2\u03bf\u03c0\3\2\2\2\u03c0"+
		"\u03c1\5<\37\2\u03c1\u03c2\7h\2\2\u03c2\u03c3\5\u010e\u0088\2\u03c3\u03c5"+
		"\3\2\2\2\u03c4\u03b4\3\2\2\2\u03c4\u03be\3\2\2\2\u03c5\u0087\3\2\2\2\u03c6"+
		"\u03cf\5\u00f8}\2\u03c7\u03cf\5\u008aF\2\u03c8\u03cf\5.\30\2\u03c9\u03ca"+
		"\7i\2\2\u03ca\u03cb\5\u0088E\2\u03cb\u03cc\7j\2\2\u03cc\u03cf\3\2\2\2"+
		"\u03cd\u03cf\t\17\2\2\u03ce\u03c6\3\2\2\2\u03ce\u03c7\3\2\2\2\u03ce\u03c8"+
		"\3\2\2\2\u03ce\u03c9\3\2\2\2\u03ce\u03cd\3\2\2\2\u03cf\u0089\3\2\2\2\u03d0"+
		"\u03da\5\u00fa~\2\u03d1\u03da\5\u0128\u0095\2\u03d2\u03da\5\u0100\u0081"+
		"\2\u03d3\u03da\5\u0108\u0085\2\u03d4\u03da\5\u0082B\2\u03d5\u03da\5\u0102"+
		"\u0082\2\u03d6\u03da\5\u0104\u0083\2\u03d7\u03da\5\u0106\u0084\2\u03d8"+
		"\u03da\5\u008cG\2\u03d9\u03d0\3\2\2\2\u03d9\u03d1\3\2\2\2\u03d9\u03d2"+
		"\3\2\2\2\u03d9\u03d3\3\2\2\2\u03d9\u03d4\3\2\2\2\u03d9\u03d5\3\2\2\2\u03d9"+
		"\u03d6\3\2\2\2\u03d9\u03d7\3\2\2\2\u03d9\u03d8\3\2\2\2\u03da\u008b\3\2"+
		"\2\2\u03db\u03dc\7\64\2\2\u03dc\u03dd\5\u008eH\2\u03dd\u008d\3\2\2\2\u03de"+
		"\u03ea\7i\2\2\u03df\u03e4\5\u0088E\2\u03e0\u03e1\7p\2\2\u03e1\u03e3\5"+
		"\u0088E\2\u03e2\u03e0\3\2\2\2\u03e3\u03e6\3\2\2\2\u03e4\u03e2\3\2\2\2"+
		"\u03e4\u03e5\3\2\2\2\u03e5\u03e8\3\2\2\2\u03e6\u03e4\3\2\2\2\u03e7\u03e9"+
		"\7p\2\2\u03e8\u03e7\3\2\2\2\u03e8\u03e9\3\2\2\2\u03e9\u03eb\3\2\2\2\u03ea"+
		"\u03df\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ec\3\2\2\2\u03ec\u03ed\7j"+
		"\2\2\u03ed\u008f\3\2\2\2\u03ee\u03f9\5\u0128\u0095\2\u03ef\u03f9\5\u00fa"+
		"~\2\u03f0\u03f1\7m\2\2\u03f1\u03f2\7w\2\2\u03f2\u03f3\7n\2\2\u03f3\u03f9"+
		"\5\u00fe\u0080\2\u03f4\u03f9\5\u0102\u0082\2\u03f5\u03f9\5\u0104\u0083"+
		"\2\u03f6\u03f9\5.\30\2\u03f7\u03f9\5\u00f8}\2\u03f8\u03ee\3\2\2\2\u03f8"+
		"\u03ef\3\2\2\2\u03f8\u03f0\3\2\2\2\u03f8\u03f4\3\2\2\2\u03f8\u03f5\3\2"+
		"\2\2\u03f8\u03f6\3\2\2\2\u03f8\u03f7\3\2\2\2\u03f9\u0091\3\2\2\2\u03fa"+
		"\u040a\7m\2\2\u03fb\u03fd\5\u0094K\2\u03fc\u03fb\3\2\2\2\u03fc\u03fd\3"+
		"\2\2\2\u03fd\u03fe\3\2\2\2\u03fe\u0400\7r\2\2\u03ff\u0401\5\u0096L\2\u0400"+
		"\u03ff\3\2\2\2\u0400\u0401\3\2\2\2\u0401\u040b\3\2\2\2\u0402\u0404\5\u0094"+
		"K\2\u0403\u0402\3\2\2\2\u0403\u0404\3\2\2\2\u0404\u0405\3\2\2\2\u0405"+
		"\u0406\7r\2\2\u0406\u0407\5\u0096L\2\u0407\u0408\7r\2\2\u0408\u0409\5"+
		"\u0098M\2\u0409\u040b\3\2\2\2\u040a\u03fc\3\2\2\2\u040a\u0403\3\2\2\2"+
		"\u040b\u040c\3\2\2\2\u040c\u040d\7n\2\2\u040d\u0093\3\2\2\2\u040e\u040f"+
		"\5j\66\2\u040f\u0095\3\2\2\2\u0410\u0411\5j\66\2\u0411\u0097\3\2\2\2\u0412"+
		"\u0413\5j\66\2\u0413\u0099\3\2\2\2\u0414\u0419\7`\2\2\u0415\u0417\5\u00c0"+
		"a\2\u0416\u0415\3\2\2\2\u0416\u0417\3\2\2\2\u0417\u0418\3\2\2\2\u0418"+
		"\u041a\7q\2\2\u0419\u0416\3\2\2\2\u0419\u041a\3\2\2\2\u041a\u041b\3\2"+
		"\2\2\u041b\u041c\5j\66\2\u041c\u0422\5\u00bc_\2\u041d\u0420\7Z\2\2\u041e"+
		"\u0421\5\u009aN\2\u041f\u0421\5\u00bc_\2\u0420\u041e\3\2\2\2\u0420\u041f"+
		"\3\2\2\2\u0421\u0423\3\2\2\2\u0422\u041d\3\2\2\2\u0422\u0423\3\2\2\2\u0423"+
		"\u009b\3\2\2\2\u0424\u0429\7]\2\2\u0425\u0427\5\u00c0a\2\u0426\u0425\3"+
		"\2\2\2\u0426\u0427\3\2\2\2\u0427\u0428\3\2\2\2\u0428\u042a\7q\2\2\u0429"+
		"\u0426\3\2\2\2\u0429\u042a\3\2\2\2\u042a\u042c\3\2\2\2\u042b\u042d\5j"+
		"\66\2\u042c\u042b\3\2\2\2\u042c\u042d\3\2\2\2\u042d\u042e\3\2\2\2\u042e"+
		"\u0432\7k\2\2\u042f\u0431\5\u00dco\2\u0430\u042f\3\2\2\2\u0431\u0434\3"+
		"\2\2\2\u0432\u0430\3\2\2\2\u0432\u0433\3\2\2\2\u0433\u0435\3\2\2\2\u0434"+
		"\u0432\3\2\2\2\u0435\u0436\7l\2\2\u0436\u009d\3\2\2\2\u0437\u043c\7]\2"+
		"\2\u0438\u043a\5\u00c0a\2\u0439\u0438\3\2\2\2\u0439\u043a\3\2\2\2\u043a"+
		"\u043b\3\2\2\2\u043b\u043d\7q\2\2\u043c\u0439\3\2\2\2\u043c\u043d\3\2"+
		"\2\2\u043d\u043e\3\2\2\2\u043e\u043f\5\u00e0q\2\u043f\u0443\7k\2\2\u0440"+
		"\u0442\5\u00e2r\2\u0441\u0440\3\2\2\2\u0442\u0445\3\2\2\2\u0443\u0441"+
		"\3\2\2\2\u0443\u0444\3\2\2\2\u0444\u0446\3\2\2\2\u0445\u0443\3\2\2\2\u0446"+
		"\u0447\7l\2\2\u0447\u009f\3\2\2\2\u0448\u044a\t\20\2\2\u0449\u0448\3\2"+
		"\2\2\u0449\u044a\3\2\2\2\u044a\u044b\3\2\2\2\u044b\u044c\7o\2\2\u044c"+
		"\u00a1\3\2\2\2\u044d\u0453\7q\2\2\u044e\u0453\7\2\2\3\u044f\u0453\6R\16"+
		"\2\u0450\u0453\6R\17\2\u0451\u0453\6R\20\2\u0452\u044d\3\2\2\2\u0452\u044e"+
		"\3\2\2\2\u0452\u044f\3\2\2\2\u0452\u0450\3\2\2\2\u0452\u0451\3\2\2\2\u0453"+
		"\u00a3\3\2\2\2\u0454\u0455\7\\\2\2\u0455\u0456\7h\2\2\u0456\u00a5\3\2"+
		"\2\2\u0457\u0463\7d\2\2\u0458\u0464\5\u00a8U\2\u0459\u045f\7i\2\2\u045a"+
		"\u045b\5\u00a8U\2\u045b\u045c\5\u00a2R\2\u045c\u045e\3\2\2\2\u045d\u045a"+
		"\3\2\2\2\u045e\u0461\3\2\2\2\u045f\u045d\3\2\2\2\u045f\u0460\3\2\2\2\u0460"+
		"\u0462\3\2\2\2\u0461\u045f\3\2\2\2\u0462\u0464\7j\2\2\u0463\u0458\3\2"+
		"\2\2\u0463\u0459\3\2\2\2\u0464\u00a7\3\2\2\2\u0465\u0467\t\21\2\2\u0466"+
		"\u0465\3\2\2\2\u0466\u0467\3\2\2\2\u0467\u0468\3\2\2\2\u0468\u0469\5\u00aa"+
		"V\2\u0469\u00a9\3\2\2\2\u046a\u046b\5\u012c\u0097\2\u046b\u00ab\3\2\2"+
		"\2\u046c\u0470\5\u00aeX\2\u046d\u0470\5\u00b6\\\2\u046e\u0470\5\u00ba"+
		"^\2\u046f\u046c\3\2\2\2\u046f\u046d\3\2\2\2\u046f\u046e\3\2\2\2\u0470"+
		"\u00ad\3\2\2\2\u0471\u047d\7^\2\2\u0472\u047e\5\u00b0Y\2\u0473\u0479\7"+
		"i\2\2\u0474\u0475\5\u00b0Y\2\u0475\u0476\5\u00a2R\2\u0476\u0478\3\2\2"+
		"\2\u0477\u0474\3\2\2\2\u0478\u047b\3\2\2\2\u0479\u0477\3\2\2\2\u0479\u047a"+
		"\3\2\2\2\u047a\u047c\3\2\2\2\u047b\u0479\3\2\2\2\u047c\u047e\7j\2\2\u047d"+
		"\u0472\3\2\2\2\u047d\u0473\3\2\2\2\u047e\u00af\3\2\2\2\u047f\u0485\5\u00b2"+
		"Z\2\u0480\u0482\5\u0088E\2\u0481\u0480\3\2\2\2\u0481\u0482\3\2\2\2\u0482"+
		"\u0483\3\2\2\2\u0483\u0484\7o\2\2\u0484\u0486\5\u00b4[\2\u0485\u0481\3"+
		"\2\2\2\u0485\u0486\3\2\2\2\u0486\u00b1\3\2\2\2\u0487\u048c\7h\2\2\u0488"+
		"\u0489\7p\2\2\u0489\u048b\7h\2\2\u048a\u0488\3\2\2\2\u048b\u048e\3\2\2"+
		"\2\u048c\u048a\3\2\2\2\u048c\u048d\3\2\2\2\u048d\u00b3\3\2\2\2\u048e\u048c"+
		"\3\2\2\2\u048f\u0494\5j\66\2\u0490\u0491\7p\2\2\u0491\u0493\5j\66\2\u0492"+
		"\u0490\3\2\2\2\u0493\u0496\3\2\2\2\u0494\u0492\3\2\2\2\u0494\u0495\3\2"+
		"\2\2\u0495\u00b5\3\2\2\2\u0496\u0494\3\2\2\2\u0497\u04a3\7a\2\2\u0498"+
		"\u04a4\5\u00b8]\2\u0499\u049f\7i\2\2\u049a\u049b\5\u00b8]\2\u049b\u049c"+
		"\5\u00a2R\2\u049c\u049e\3\2\2\2\u049d\u049a\3\2\2\2\u049e\u04a1\3\2\2"+
		"\2\u049f\u049d\3\2\2\2\u049f\u04a0\3\2\2\2\u04a0\u04a2\3\2\2\2\u04a1\u049f"+
		"\3\2\2\2\u04a2\u04a4\7j\2\2\u04a3\u0498\3\2\2\2\u04a3\u0499\3\2\2\2\u04a4"+
		"\u00b7\3\2\2\2\u04a5\u04a7\7h\2\2\u04a6\u04a8\7o\2\2\u04a7\u04a6\3\2\2"+
		"\2\u04a7\u04a8\3\2\2\2\u04a8\u04a9\3\2\2\2\u04a9\u04aa\5\u0088E\2\u04aa"+
		"\u00b9\3\2\2\2\u04ab\u04b7\7f\2\2\u04ac\u04b8\5\\/\2\u04ad\u04b3\7i\2"+
		"\2\u04ae\u04af\5\\/\2\u04af\u04b0\5\u00a2R\2\u04b0\u04b2\3\2\2\2\u04b1"+
		"\u04ae\3\2\2\2\u04b2\u04b5\3\2\2\2\u04b3\u04b1\3\2\2\2\u04b3\u04b4\3\2"+
		"\2\2\u04b4\u04b6\3\2\2\2\u04b5\u04b3\3\2\2\2\u04b6\u04b8\7j\2\2\u04b7"+
		"\u04ac\3\2\2\2\u04b7\u04ad\3\2\2\2\u04b8\u00bb\3\2\2\2\u04b9\u04bb\7k"+
		"\2\2\u04ba\u04bc\5\u00be`\2\u04bb\u04ba\3\2\2\2\u04bb\u04bc\3\2\2\2\u04bc"+
		"\u04bd\3\2\2\2\u04bd\u04be\7l\2\2\u04be\u00bd\3\2\2\2\u04bf\u04c0\5p9"+
		"\2\u04c0\u04c1\5\u00a2R\2\u04c1\u04c3\3\2\2\2\u04c2\u04bf\3\2\2\2\u04c3"+
		"\u04c4\3\2\2\2\u04c4\u04c2\3\2\2\2\u04c4\u04c5\3\2\2\2\u04c5\u00bf\3\2"+
		"\2\2\u04c6\u04cd\5\u00c4c\2\u04c7\u04cd\5\u00c6d\2\u04c8\u04cd\5\u00c8"+
		"e\2\u04c9\u04cd\5\u00c2b\2\u04ca\u04cd\5^\60\2\u04cb\u04cd\5\u00caf\2"+
		"\u04cc\u04c6\3\2\2\2\u04cc\u04c7\3\2\2\2\u04cc\u04c8\3\2\2\2\u04cc\u04c9"+
		"\3\2\2\2\u04cc\u04ca\3\2\2\2\u04cc\u04cb\3\2\2\2\u04cd\u00c1\3\2\2\2\u04ce"+
		"\u04cf\5j\66\2\u04cf\u00c3\3\2\2\2\u04d0\u04d1\5j\66\2\u04d1\u04d2\7\u008c"+
		"\2\2\u04d2\u04d3\5j\66\2\u04d3\u00c5\3\2\2\2\u04d4\u04d5\5j\66\2\u04d5"+
		"\u04d6\t\22\2\2\u04d6\u00c7\3\2\2\2\u04d7\u04d8\5\u00b4[\2\u04d8\u04d9"+
		"\5\u00a0Q\2\u04d9\u04da\5\u00b4[\2\u04da\u00c9\3\2\2\2\u04db\u04dc\7q"+
		"\2\2\u04dc\u00cb\3\2\2\2\u04dd\u04de\7h\2\2\u04de\u04e0\7r\2\2\u04df\u04e1"+
		"\5p9\2\u04e0\u04df\3\2\2\2\u04e0\u04e1\3\2\2\2\u04e1\u00cd\3\2\2\2\u04e2"+
		"\u04e4\7e\2\2\u04e3\u04e5\5\u00b4[\2\u04e4\u04e3\3\2\2\2\u04e4\u04e5\3"+
		"\2\2\2\u04e5\u00cf\3\2\2\2\u04e6\u04e8\7O\2\2\u04e7\u04e9\7h\2\2\u04e8"+
		"\u04e7\3\2\2\2\u04e8\u04e9\3\2\2\2\u04e9\u00d1\3\2\2\2\u04ea\u04ec\7b"+
		"\2\2\u04eb\u04ed\7h\2\2\u04ec\u04eb\3\2\2\2\u04ec\u04ed\3\2\2\2\u04ed"+
		"\u00d3\3\2\2\2\u04ee\u04ef\7[\2\2\u04ef\u04f0\7h\2\2\u04f0\u00d5\3\2\2"+
		"\2\u04f1\u04f2\7_\2\2\u04f2\u00d7\3\2\2\2\u04f3\u04f4\7U\2\2\u04f4\u04f5"+
		"\5j\66\2\u04f5\u00d9\3\2\2\2\u04f6\u04f9\5\u009cO\2\u04f7\u04f9\5\u009e"+
		"P\2\u04f8\u04f6\3\2\2\2\u04f8\u04f7\3\2\2\2\u04f9\u00db\3\2\2\2\u04fa"+
		"\u04fb\5\u00dep\2\u04fb\u04fd\7r\2\2\u04fc\u04fe\5\u00be`\2\u04fd\u04fc"+
		"\3\2\2\2\u04fd\u04fe\3\2\2\2\u04fe\u00dd\3\2\2\2\u04ff\u0500\7T\2\2\u0500"+
		"\u0503\5\u00b4[\2\u0501\u0503\7P\2\2\u0502\u04ff\3\2\2\2\u0502\u0501\3"+
		"\2\2\2\u0503\u00df\3\2\2\2\u0504\u0505\7h\2\2\u0505\u0507\7v\2\2\u0506"+
		"\u0504\3\2\2\2\u0506\u0507\3\2\2\2\u0507\u0508\3\2\2\2\u0508\u0509\5~"+
		"@\2\u0509\u050a\7s\2\2\u050a\u050b\7i\2\2\u050b\u050c\7a\2\2\u050c\u050d"+
		"\7j\2\2\u050d\u00e1\3\2\2\2\u050e\u050f\5\u00e4s\2\u050f\u0511\7r\2\2"+
		"\u0510\u0512\5\u00be`\2\u0511\u0510\3\2\2\2\u0511\u0512\3\2\2\2\u0512"+
		"\u00e3\3\2\2\2\u0513\u0514\7T\2\2\u0514\u0517\5\u00e6t\2\u0515\u0517\7"+
		"P\2\2\u0516\u0513\3\2\2\2\u0516\u0515\3\2\2\2\u0517\u00e5\3\2\2\2\u0518"+
		"\u051b\5\u0088E\2\u0519\u051b\7g\2\2\u051a\u0518\3\2\2\2\u051a\u0519\3"+
		"\2\2\2\u051b\u0523\3\2\2\2\u051c\u051f\7p\2\2\u051d\u0520\5\u0088E\2\u051e"+
		"\u0520\7g\2\2\u051f\u051d\3\2\2\2\u051f\u051e\3\2\2\2\u0520\u0522\3\2"+
		"\2\2\u0521\u051c\3\2\2\2\u0522\u0525\3\2\2\2\u0523\u0521\3\2\2\2\u0523"+
		"\u0524\3\2\2\2\u0524\u00e7\3\2\2\2\u0525\u0523\3\2\2\2\u0526\u0527\7S"+
		"\2\2\u0527\u052b\7k\2\2\u0528\u052a\5\u00eav\2\u0529\u0528\3\2\2\2\u052a"+
		"\u052d\3\2\2\2\u052b\u0529\3\2\2\2\u052b\u052c\3\2\2\2\u052c\u052e\3\2"+
		"\2\2\u052d\u052b\3\2\2\2\u052e\u052f\7l\2\2\u052f\u00e9\3\2\2\2\u0530"+
		"\u0531\5\u00ecw\2\u0531\u0533\7r\2\2\u0532\u0534\5\u00be`\2\u0533\u0532"+
		"\3\2\2\2\u0533\u0534\3\2\2\2\u0534\u00eb\3\2\2\2\u0535\u0538\7T\2\2\u0536"+
		"\u0539\5\u00c4c\2\u0537\u0539\5\u00eex\2\u0538\u0536\3\2\2\2\u0538\u0537"+
		"\3\2\2\2\u0539\u053c\3\2\2\2\u053a\u053c\7P\2\2\u053b\u0535\3\2\2\2\u053b"+
		"\u053a\3\2\2\2\u053c\u00ed\3\2\2\2\u053d\u053e\5\u00b4[\2\u053e\u053f"+
		"\7o\2\2\u053f\u0544\3\2\2\2\u0540\u0541\5\u00b2Z\2\u0541\u0542\7v\2\2"+
		"\u0542\u0544\3\2\2\2\u0543\u053d\3\2\2\2\u0543\u0540\3\2\2\2\u0543\u0544"+
		"\3\2\2\2\u0544\u0545\3\2\2\2\u0545\u0546\5j\66\2\u0546\u00ef\3\2\2\2\u0547"+
		"\u054b\7c\2\2\u0548\u054c\5j\66\2\u0549\u054c\5\u00f2z\2\u054a\u054c\5"+
		"\u00f4{\2\u054b\u0548\3\2\2\2\u054b\u0549\3\2\2\2\u054b\u054a\3\2\2\2"+
		"\u054b\u054c\3\2\2\2\u054c\u054d\3\2\2\2\u054d\u054e\5\u00bc_\2\u054e"+
		"\u00f1\3\2\2\2\u054f\u0551\5\u00c0a\2\u0550\u054f\3\2\2\2\u0550\u0551"+
		"\3\2\2\2\u0551\u0552\3\2\2\2\u0552\u0554\7q\2\2\u0553\u0555\5j\66\2\u0554"+
		"\u0553\3\2\2\2\u0554\u0555\3\2\2\2\u0555\u0556\3\2\2\2\u0556\u0558\7q"+
		"\2\2\u0557\u0559\5\u00c0a\2\u0558\u0557\3\2\2\2\u0558\u0559\3\2\2\2\u0559"+
		"\u00f3\3\2\2\2\u055a\u055b\5\u00b4[\2\u055b\u055c\7o\2\2\u055c\u0561\3"+
		"\2\2\2\u055d\u055e\5\u00b2Z\2\u055e\u055f\7v\2\2\u055f\u0561\3\2\2\2\u0560"+
		"\u055a\3\2\2\2\u0560\u055d\3\2\2\2\u0560\u0561\3\2\2\2\u0561\u0562\3\2"+
		"\2\2\u0562\u0563\7%\2\2\u0563\u0564\5j\66\2\u0564\u00f5\3\2\2\2\u0565"+
		"\u0566\7V\2\2\u0566\u0567\5j\66\2\u0567\u00f7\3\2\2\2\u0568\u056b\5\u011a"+
		"\u008e\2\u0569\u056b\7h\2\2\u056a\u0568\3\2\2\2\u056a\u0569\3\2\2\2\u056b"+
		"\u00f9\3\2\2\2\u056c\u056d\7m\2\2\u056d\u056e\5\u00fc\177\2\u056e\u056f"+
		"\7n\2\2\u056f\u0570\5\u00fe\u0080\2\u0570\u00fb\3\2\2\2\u0571\u0572\5"+
		"j\66\2\u0572\u00fd\3\2\2\2\u0573\u0574\5\u0088E\2\u0574\u00ff\3\2\2\2"+
		"\u0575\u0576\7\u008a\2\2\u0576\u0577\5\u0088E\2\u0577\u0101\3\2\2\2\u0578"+
		"\u0579\7m\2\2\u0579\u057a\7n\2\2\u057a\u057b\5\u00fe\u0080\2\u057b\u0103"+
		"\3\2\2\2\u057c\u057d\7W\2\2\u057d\u057e\7m\2\2\u057e\u057f\5\u0088E\2"+
		"\u057f\u0580\7n\2\2\u0580\u0581\5\u00fe\u0080\2\u0581\u0105\3\2\2\2\u0582"+
		"\u0588\7Y\2\2\u0583\u0584\7Y\2\2\u0584\u0588\7\u008c\2\2\u0585\u0586\7"+
		"\u008c\2\2\u0586\u0588\7Y\2\2\u0587\u0582\3\2\2\2\u0587\u0583\3\2\2\2"+
		"\u0587\u0585\3\2\2\2\u0588\u0589\3\2\2\2\u0589\u058a\5\u00fe\u0080\2\u058a"+
		"\u0107\3\2\2\2\u058b\u058c\7Q\2\2\u058c\u058d\5\u010a\u0086\2\u058d\u0109"+
		"\3\2\2\2\u058e\u058f\6\u0086\21\2\u058f\u0590\5\u010e\u0088\2\u0590\u0591"+
		"\5\u010c\u0087\2\u0591\u0594\3\2\2\2\u0592\u0594\5\u010e\u0088\2\u0593"+
		"\u058e\3\2\2\2\u0593\u0592\3\2\2\2\u0594\u010b\3\2\2\2\u0595\u0598\5\u010e"+
		"\u0088\2\u0596\u0598\5\u0088E\2\u0597\u0595\3\2\2\2\u0597\u0596\3\2\2"+
		"\2\u0598\u010d\3\2\2\2\u0599\u05a5\7i\2\2\u059a\u059f\5d\63\2\u059b\u059c"+
		"\7p\2\2\u059c\u059e\5d\63\2\u059d\u059b\3\2\2\2\u059e\u05a1\3\2\2\2\u059f"+
		"\u059d\3\2\2\2\u059f\u05a0\3\2\2\2\u05a0\u05a3\3\2\2\2\u05a1\u059f\3\2"+
		"\2\2\u05a2\u05a4\7p\2\2\u05a3\u05a2\3\2\2\2\u05a3\u05a4\3\2\2\2\u05a4"+
		"\u05a6\3\2\2\2\u05a5\u059a\3\2\2\2\u05a5\u05a6\3\2\2\2\u05a6\u05a7\3\2"+
		"\2\2\u05a7\u05a8\7j\2\2\u05a8\u010f\3\2\2\2\u05a9\u05aa\5\u0088E\2\u05aa"+
		"\u05ab\7i\2\2\u05ab\u05ad\5j\66\2\u05ac\u05ae\7p\2\2\u05ad\u05ac\3\2\2"+
		"\2\u05ad\u05ae\3\2\2\2\u05ae\u05af\3\2\2\2\u05af\u05b0\7j\2\2\u05b0\u0111"+
		"\3\2\2\2\u05b1\u05b8\5\u0114\u008b\2\u05b2\u05b8\5\u0118\u008d\2\u05b3"+
		"\u05b4\7i\2\2\u05b4\u05b5\5j\66\2\u05b5\u05b6\7j\2\2\u05b6\u05b8\3\2\2"+
		"\2\u05b7\u05b1\3\2\2\2\u05b7\u05b2\3\2\2\2\u05b7\u05b3\3\2\2\2\u05b8\u0113"+
		"\3\2\2\2\u05b9\u05bd\5|?\2\u05ba\u05bd\5\u011c\u008f\2\u05bb\u05bd\5\u0130"+
		"\u0099\2\u05bc\u05b9\3\2\2\2\u05bc\u05ba\3\2\2\2\u05bc\u05bb\3\2\2\2\u05bd"+
		"\u0115\3\2\2\2\u05be\u05bf\t\23\2\2\u05bf\u0117\3\2\2\2\u05c0\u05c3\7"+
		"h\2\2\u05c1\u05c2\7s\2\2\u05c2\u05c4\7h\2\2\u05c3\u05c1\3\2\2\2\u05c3"+
		"\u05c4\3\2\2\2\u05c4\u0119\3\2\2\2\u05c5\u05c6\7h\2\2\u05c6\u05c7\7s\2"+
		"\2\u05c7\u05c8\7h\2\2\u05c8\u011b\3\2\2\2\u05c9\u05ca\5\u0090I\2\u05ca"+
		"\u05cb\5\u011e\u0090\2\u05cb\u011d\3\2\2\2\u05cc\u05d1\7k\2\2\u05cd\u05cf"+
		"\5\u0120\u0091\2\u05ce\u05d0\7p\2\2\u05cf\u05ce\3\2\2\2\u05cf\u05d0\3"+
		"\2\2\2\u05d0\u05d2\3\2\2\2\u05d1\u05cd\3\2\2\2\u05d1\u05d2\3\2\2\2\u05d2"+
		"\u05d3\3\2\2\2\u05d3\u05d4\7l\2\2\u05d4\u011f\3\2\2\2\u05d5\u05da\5\u0122"+
		"\u0092\2\u05d6\u05d7\7p\2\2\u05d7\u05d9\5\u0122\u0092\2\u05d8\u05d6\3"+
		"\2\2\2\u05d9\u05dc\3\2\2\2\u05da\u05d8\3\2\2\2\u05da\u05db\3\2\2\2\u05db"+
		"\u0121\3\2\2\2\u05dc\u05da\3\2\2\2\u05dd\u05de\5\u0124\u0093\2\u05de\u05df"+
		"\7r\2\2\u05df\u05e1\3\2\2\2\u05e0\u05dd\3\2\2\2\u05e0\u05e1\3\2\2\2\u05e1"+
		"\u05e2\3\2\2\2\u05e2\u05e3\5\u0126\u0094\2\u05e3\u0123\3\2\2\2\u05e4\u05e8"+
		"\7h\2\2\u05e5\u05e8\5j\66\2\u05e6\u05e8\5\u011e\u0090\2\u05e7\u05e4\3"+
		"\2\2\2\u05e7\u05e5\3\2\2\2\u05e7\u05e6\3\2\2\2\u05e8\u0125\3\2\2\2\u05e9"+
		"\u05ec\5j\66\2\u05ea\u05ec\5\u011e\u0090\2\u05eb\u05e9\3\2\2\2\u05eb\u05ea"+
		"\3\2\2\2\u05ec\u0127\3\2\2\2\u05ed\u05ee\7X\2\2\u05ee\u05f4\7k\2\2\u05ef"+
		"\u05f0\5\u012a\u0096\2\u05f0\u05f1\5\u00a2R\2\u05f1\u05f3\3\2\2\2\u05f2"+
		"\u05ef\3\2\2\2\u05f3\u05f6\3\2\2\2\u05f4\u05f2\3\2\2\2\u05f4\u05f5\3\2"+
		"\2\2\u05f5\u05f7\3\2\2\2\u05f6\u05f4\3\2\2\2\u05f7\u05f8\7l\2\2\u05f8"+
		"\u0129\3\2\2\2\u05f9\u05fa\6\u0096\22\2\u05fa\u05fb\5\u00b2Z\2\u05fb\u05fc"+
		"\5\u0088E\2\u05fc\u05ff\3\2\2\2\u05fd\u05ff\5\u012e\u0098\2\u05fe\u05f9"+
		"\3\2\2\2\u05fe\u05fd\3\2\2\2\u05ff\u0601\3\2\2\2\u0600\u0602\5\u012c\u0097"+
		"\2\u0601\u0600\3\2\2\2\u0601\u0602\3\2\2\2\u0602\u012b\3\2\2\2\u0603\u0604"+
		"\t\24\2\2\u0604\u012d\3\2\2\2\u0605\u0607\7\u008a\2\2\u0606\u0605\3\2"+
		"\2\2\u0606\u0607\3\2\2\2\u0607\u0608\3\2\2\2\u0608\u0609\5\u00f8}\2\u0609"+
		"\u012f\3\2\2\2\u060a\u060b\7Q\2\2\u060b\u060c\5\u010a\u0086\2\u060c\u060d"+
		"\5\u00bc_\2\u060d\u0131\3\2\2\2\u060e\u060f\7m\2\2\u060f\u0610\5j\66\2"+
		"\u0610\u0611\7n\2\2\u0611\u0133\3\2\2\2\u0612\u0613\7s\2\2\u0613\u0614"+
		"\7i\2\2\u0614\u0615\5\u0088E\2\u0615\u0616\7j\2\2\u0616\u0135\3\2\2\2"+
		"\u0617\u0626\7i\2\2\u0618\u061f\5\u00b4[\2\u0619\u061c\5\u0088E\2\u061a"+
		"\u061b\7p\2\2\u061b\u061d\5\u00b4[\2\u061c\u061a\3\2\2\2\u061c\u061d\3"+
		"\2\2\2\u061d\u061f\3\2\2\2\u061e\u0618\3\2\2\2\u061e\u0619\3\2\2\2\u061f"+
		"\u0621\3\2\2\2\u0620\u0622\7w\2\2\u0621\u0620\3\2\2\2\u0621\u0622\3\2"+
		"\2\2\u0622\u0624\3\2\2\2\u0623\u0625\7p\2\2\u0624\u0623\3\2\2\2\u0624"+
		"\u0625\3\2\2\2\u0625\u0627\3\2\2\2\u0626\u061e\3\2\2\2\u0626\u0627\3\2"+
		"\2\2\u0627\u0628\3\2\2\2\u0628\u0629\7j\2\2\u0629\u0137\3\2\2\2\u062a"+
		"\u062b\5\u013a\u009e\2\u062b\u062c\7s\2\2\u062c\u062d\7h\2\2\u062d\u0139"+
		"\3\2\2\2\u062e\u062f\5\u0088E\2\u062f\u013b\3\2\2\2\u00a5\u0141\u0146"+
		"\u0150\u0157\u015b\u0162\u016f\u0171\u018f\u01a8\u01b1\u01bb\u01c3\u01d4"+
		"\u01dd\u01eb\u01fd\u0205\u0213\u0219\u021e\u0221\u022b\u0232\u023a\u0241"+
		"\u0244\u024a\u025a\u0261\u0267\u0272\u0279\u027b\u0281\u028d\u0298\u02a0"+
		"\u02a6\u02ac\u02b6\u02bd\u02c3\u02c7\u02cf\u02d3\u02d9\u02dc\u02e2\u02e5"+
		"\u02e8\u02f5\u030d\u032d\u032f\u0337\u0352\u035a\u0365\u036c\u036f\u0373"+
		"\u037d\u0384\u0390\u0394\u0399\u039c\u03a5\u03ab\u03b6\u03be\u03c4\u03ce"+
		"\u03d9\u03e4\u03e8\u03ea\u03f8\u03fc\u0400\u0403\u040a\u0416\u0419\u0420"+
		"\u0422\u0426\u0429\u042c\u0432\u0439\u043c\u0443\u0449\u0452\u045f\u0463"+
		"\u0466\u046f\u0479\u047d\u0481\u0485\u048c\u0494\u049f\u04a3\u04a7\u04b3"+
		"\u04b7\u04bb\u04c4\u04cc\u04e0\u04e4\u04e8\u04ec\u04f8\u04fd\u0502\u0506"+
		"\u0511\u0516\u051a\u051f\u0523\u052b\u0533\u0538\u053b\u0543\u054b\u0550"+
		"\u0554\u0558\u0560\u056a\u0587\u0593\u0597\u059f\u05a3\u05a5\u05ad\u05b7"+
		"\u05bc\u05c3\u05cf\u05d1\u05da\u05e0\u05e7\u05eb\u05f4\u05fe\u0601\u0606"+
		"\u061c\u061e\u0621\u0624\u0626";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}