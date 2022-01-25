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
		FLOAT_LIT=1, DECIMAL_FLOAT_LIT=2, TRUE=3, FALSE=4, ASSERT=5, ASSUME=6, 
		INHALE=7, EXHALE=8, PRE=9, PRESERVES=10, POST=11, INV=12, DEC=13, PURE=14, 
		IMPL=15, OLD=16, LHS=17, FORALL=18, EXISTS=19, ACCESS=20, FOLD=21, UNFOLD=22, 
		UNFOLDING=23, GHOST=24, IN=25, MULTI=26, SUBSET=27, UNION=28, INTERSECTION=29, 
		SETMINUS=30, IMPLIES=31, WAND=32, APPLY=33, QMARK=34, L_PRED=35, R_PRED=36, 
		RANGE=37, SEQ=38, SET=39, MSET=40, DICT=41, OPT=42, LEN=43, NEW=44, MAKE=45, 
		CAP=46, SOME=47, GET=48, DOM=49, AXIOM=50, NONE=51, PRED=52, TYPE_OF=53, 
		IS_COMPARABLE=54, SHARE=55, ADDR_MOD=56, DOT_DOT=57, SHARED=58, EXCLUSIVE=59, 
		PREDICATE=60, WRITEPERM=61, NOPERM=62, TRUSTED=63, BOOL=64, STRING=65, 
		PERM=66, RUNE=67, INT=68, INT8=69, INT16=70, INT32=71, INT64=72, BYTE=73, 
		UINT=74, UINT8=75, UINT16=76, UINT32=77, UINT64=78, UINTPTR=79, FLOAT32=80, 
		FLOAT64=81, COMPLEX64=82, COMPLEX128=83, BREAK=84, DEFAULT=85, FUNC=86, 
		INTERFACE=87, SELECT=88, CASE=89, DEFER=90, GO=91, MAP=92, STRUCT=93, 
		CHAN=94, ELSE=95, GOTO=96, PACKAGE=97, SWITCH=98, CONST=99, FALLTHROUGH=100, 
		IF=101, TYPE=102, CONTINUE=103, FOR=104, IMPORT=105, RETURN=106, VAR=107, 
		NIL_LIT=108, IDENTIFIER=109, L_PAREN=110, R_PAREN=111, L_CURLY=112, R_CURLY=113, 
		L_BRACKET=114, R_BRACKET=115, ASSIGN=116, COMMA=117, SEMI=118, COLON=119, 
		DOT=120, PLUS_PLUS=121, MINUS_MINUS=122, DECLARE_ASSIGN=123, ELLIPSIS=124, 
		LOGICAL_OR=125, LOGICAL_AND=126, EQUALS=127, NOT_EQUALS=128, LESS=129, 
		LESS_OR_EQUALS=130, GREATER=131, GREATER_OR_EQUALS=132, OR=133, DIV=134, 
		MOD=135, LSHIFT=136, RSHIFT=137, BIT_CLEAR=138, EXCLAMATION=139, PLUS=140, 
		MINUS=141, CARET=142, STAR=143, AMPERSAND=144, RECEIVE=145, DECIMAL_LIT=146, 
		BINARY_LIT=147, OCTAL_LIT=148, HEX_LIT=149, HEX_FLOAT_LIT=150, IMAGINARY_LIT=151, 
		RUNE_LIT=152, BYTE_VALUE=153, OCTAL_BYTE_VALUE=154, HEX_BYTE_VALUE=155, 
		LITTLE_U_VALUE=156, BIG_U_VALUE=157, RAW_STRING_LIT=158, INTERPRETED_STRING_LIT=159, 
		WS=160, COMMENT=161, TERMINATOR=162, LINE_COMMENT=163;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_ghostStatement = 5, RULE_ghostPrimaryExpr = 6, 
		RULE_boundVariables = 7, RULE_boundVariableDecl = 8, RULE_triggers = 9, 
		RULE_trigger = 10, RULE_predicateAccess = 11, RULE_optionSome = 12, RULE_optionNone = 13, 
		RULE_optionGet = 14, RULE_sConversion = 15, RULE_old = 16, RULE_oldLabelUse = 17, 
		RULE_labelUse = 18, RULE_isComparable = 19, RULE_typeOf = 20, RULE_access = 21, 
		RULE_range = 22, RULE_seqUpdExp = 23, RULE_seqUpdClause = 24, RULE_ghostTypeLit = 25, 
		RULE_domainType = 26, RULE_domainClause = 27, RULE_ghostSliceType = 28, 
		RULE_sqType = 29, RULE_specification = 30, RULE_specStatement = 31, RULE_terminationMeasure = 32, 
		RULE_assertion = 33, RULE_blockWithBodyParameterInfo = 34, RULE_implementationProof = 35, 
		RULE_methodImplementationProof = 36, RULE_nonLocalReceiver = 37, RULE_selection = 38, 
		RULE_implementationProofPredicateAlias = 39, RULE_make = 40, RULE_new_ = 41, 
		RULE_functionDecl = 42, RULE_methodDecl = 43, RULE_sourceFile = 44, RULE_ghostMember = 45, 
		RULE_fpredicateDecl = 46, RULE_predicateBody = 47, RULE_mpredicateDecl = 48, 
		RULE_varSpec = 49, RULE_shortVarDecl = 50, RULE_receiver = 51, RULE_parameterDecl = 52, 
		RULE_unaryExpr = 53, RULE_unfolding = 54, RULE_expression = 55, RULE_statement = 56, 
		RULE_applyStmt = 57, RULE_packageStmt = 58, RULE_specForStmt = 59, RULE_loopSpec = 60, 
		RULE_basicLit = 61, RULE_primaryExpr = 62, RULE_predConstructArgs = 63, 
		RULE_interfaceType = 64, RULE_predicateSpec = 65, RULE_methodSpec = 66, 
		RULE_type_ = 67, RULE_typeLit = 68, RULE_predType = 69, RULE_predTypeParams = 70, 
		RULE_literalType = 71, RULE_slice_ = 72, RULE_low = 73, RULE_high = 74, 
		RULE_cap = 75, RULE_assign_op = 76, RULE_eos = 77, RULE_packageClause = 78, 
		RULE_importDecl = 79, RULE_importSpec = 80, RULE_importPath = 81, RULE_declaration = 82, 
		RULE_constDecl = 83, RULE_constSpec = 84, RULE_identifierList = 85, RULE_expressionList = 86, 
		RULE_typeDecl = 87, RULE_typeSpec = 88, RULE_varDecl = 89, RULE_block = 90, 
		RULE_statementList = 91, RULE_simpleStmt = 92, RULE_terminatedSimpleStmt = 93, 
		RULE_expressionStmt = 94, RULE_sendStmt = 95, RULE_incDecStmt = 96, RULE_assignment = 97, 
		RULE_emptyStmt = 98, RULE_labeledStmt = 99, RULE_returnStmt = 100, RULE_breakStmt = 101, 
		RULE_continueStmt = 102, RULE_gotoStmt = 103, RULE_fallthroughStmt = 104, 
		RULE_deferStmt = 105, RULE_ifStmt = 106, RULE_switchStmt = 107, RULE_exprSwitchStmt = 108, 
		RULE_exprCaseClause = 109, RULE_exprSwitchCase = 110, RULE_typeSwitchStmt = 111, 
		RULE_typeSwitchGuard = 112, RULE_typeCaseClause = 113, RULE_typeSwitchCase = 114, 
		RULE_typeList = 115, RULE_selectStmt = 116, RULE_commClause = 117, RULE_commCase = 118, 
		RULE_recvStmt = 119, RULE_forStmt = 120, RULE_forClause = 121, RULE_rangeClause = 122, 
		RULE_goStmt = 123, RULE_typeName = 124, RULE_arrayType = 125, RULE_arrayLength = 126, 
		RULE_elementType = 127, RULE_pointerType = 128, RULE_sliceType = 129, 
		RULE_mapType = 130, RULE_channelType = 131, RULE_functionType = 132, RULE_signature = 133, 
		RULE_result = 134, RULE_parameters = 135, RULE_conversion = 136, RULE_operand = 137, 
		RULE_literal = 138, RULE_integer = 139, RULE_operandName = 140, RULE_qualifiedIdent = 141, 
		RULE_compositeLit = 142, RULE_literalValue = 143, RULE_elementList = 144, 
		RULE_keyedElement = 145, RULE_key = 146, RULE_element = 147, RULE_structType = 148, 
		RULE_fieldDecl = 149, RULE_string_ = 150, RULE_embeddedField = 151, RULE_functionLit = 152, 
		RULE_index = 153, RULE_typeAssertion = 154, RULE_arguments = 155, RULE_methodExpr = 156, 
		RULE_receiverType = 157;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "ghostStatement", "ghostPrimaryExpr", "boundVariables", 
			"boundVariableDecl", "triggers", "trigger", "predicateAccess", "optionSome", 
			"optionNone", "optionGet", "sConversion", "old", "oldLabelUse", "labelUse", 
			"isComparable", "typeOf", "access", "range", "seqUpdExp", "seqUpdClause", 
			"ghostTypeLit", "domainType", "domainClause", "ghostSliceType", "sqType", 
			"specification", "specStatement", "terminationMeasure", "assertion", 
			"blockWithBodyParameterInfo", "implementationProof", "methodImplementationProof", 
			"nonLocalReceiver", "selection", "implementationProofPredicateAlias", 
			"make", "new_", "functionDecl", "methodDecl", "sourceFile", "ghostMember", 
			"fpredicateDecl", "predicateBody", "mpredicateDecl", "varSpec", "shortVarDecl", 
			"receiver", "parameterDecl", "unaryExpr", "unfolding", "expression", 
			"statement", "applyStmt", "packageStmt", "specForStmt", "loopSpec", "basicLit", 
			"primaryExpr", "predConstructArgs", "interfaceType", "predicateSpec", 
			"methodSpec", "type_", "typeLit", "predType", "predTypeParams", "literalType", 
			"slice_", "low", "high", "cap", "assign_op", "eos", "packageClause", 
			"importDecl", "importSpec", "importPath", "declaration", "constDecl", 
			"constSpec", "identifierList", "expressionList", "typeDecl", "typeSpec", 
			"varDecl", "block", "statementList", "simpleStmt", "terminatedSimpleStmt", 
			"expressionStmt", "sendStmt", "incDecStmt", "assignment", "emptyStmt", 
			"labeledStmt", "returnStmt", "breakStmt", "continueStmt", "gotoStmt", 
			"fallthroughStmt", "deferStmt", "ifStmt", "switchStmt", "exprSwitchStmt", 
			"exprCaseClause", "exprSwitchCase", "typeSwitchStmt", "typeSwitchGuard", 
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
			null, null, null, "'true'", "'false'", "'assert'", "'assume'", "'inhale'", 
			"'exhale'", "'requires'", "'preserves'", "'ensures'", "'invariant'", 
			"'decreases'", "'pure'", "'implements'", "'old'", "'#lhs'", "'forall'", 
			"'exists'", "'acc'", "'fold'", "'unfold'", "'unfolding'", "'ghost'", 
			"'in'", "'#'", "'subset'", "'union'", "'intersection'", "'setminus'", 
			"'==>'", "'--*'", "'apply'", "'?'", "'!<'", "'!>'", "'range'", "'seq'", 
			"'set'", "'mset'", "'dict'", "'option'", "'len'", "'new'", "'make'", 
			"'cap'", "'some'", "'get'", "'domain'", "'axiom'", "'none'", "'pred'", 
			"'typeOf'", "'isComparable'", "'share'", "'@'", "'..'", "'shared'", "'exclusive'", 
			"'predicate'", "'writePerm'", "'noPerm'", "'trusted'", "'bool'", "'string'", 
			"'perm'", "'rune'", "'int'", "'int8'", "'int16'", "'int32'", "'int64'", 
			"'byte'", "'uint'", "'uint8'", "'uint16'", "'uint32'", "'uint64'", "'uintptr'", 
			"'float32'", "'float64'", "'complex64'", "'complex128'", "'break'", "'default'", 
			"'func'", "'interface'", "'select'", "'case'", "'defer'", "'go'", "'map'", 
			"'struct'", "'chan'", "'else'", "'goto'", "'package'", "'switch'", "'const'", 
			"'fallthrough'", "'if'", "'type'", "'continue'", "'for'", "'import'", 
			"'return'", "'var'", "'nil'", null, "'('", "')'", "'{'", "'}'", "'['", 
			"']'", "'='", "','", "';'", "':'", "'.'", "'++'", "'--'", "':='", "'...'", 
			"'||'", "'&&'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'|'", 
			"'/'", "'%'", "'<<'", "'>>'", "'&^'", "'!'", "'+'", "'-'", "'^'", "'*'", 
			"'&'", "'<-'"
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
			"RANGE", "SEQ", "SET", "MSET", "DICT", "OPT", "LEN", "NEW", "MAKE", "CAP", 
			"SOME", "GET", "DOM", "AXIOM", "NONE", "PRED", "TYPE_OF", "IS_COMPARABLE", 
			"SHARE", "ADDR_MOD", "DOT_DOT", "SHARED", "EXCLUSIVE", "PREDICATE", "WRITEPERM", 
			"NOPERM", "TRUSTED", "BOOL", "STRING", "PERM", "RUNE", "INT", "INT8", 
			"INT16", "INT32", "INT64", "BYTE", "UINT", "UINT8", "UINT16", "UINT32", 
			"UINT64", "UINTPTR", "FLOAT32", "FLOAT64", "COMPLEX64", "COMPLEX128", 
			"BREAK", "DEFAULT", "FUNC", "INTERFACE", "SELECT", "CASE", "DEFER", "GO", 
			"MAP", "STRUCT", "CHAN", "ELSE", "GOTO", "PACKAGE", "SWITCH", "CONST", 
			"FALLTHROUGH", "IF", "TYPE", "CONTINUE", "FOR", "IMPORT", "RETURN", "VAR", 
			"NIL_LIT", "IDENTIFIER", "L_PAREN", "R_PAREN", "L_CURLY", "R_CURLY", 
			"L_BRACKET", "R_BRACKET", "ASSIGN", "COMMA", "SEMI", "COLON", "DOT", 
			"PLUS_PLUS", "MINUS_MINUS", "DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", 
			"LOGICAL_AND", "EQUALS", "NOT_EQUALS", "LESS", "LESS_OR_EQUALS", "GREATER", 
			"GREATER_OR_EQUALS", "OR", "DIV", "MOD", "LSHIFT", "RSHIFT", "BIT_CLEAR", 
			"EXCLAMATION", "PLUS", "MINUS", "CARET", "STAR", "AMPERSAND", "RECEIVE", 
			"DECIMAL_LIT", "BINARY_LIT", "OCTAL_LIT", "HEX_LIT", "HEX_FLOAT_LIT", 
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
			setState(316);
			expression(0);
			setState(317);
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
			setState(319);
			statement();
			setState(320);
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
			setState(322);
			type_();
			setState(323);
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
			setState(325);
			maybeAddressableIdentifier();
			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(326);
				match(COMMA);
				setState(327);
				maybeAddressableIdentifier();
				}
				}
				setState(332);
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
			setState(333);
			match(IDENTIFIER);
			setState(335);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(334);
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
		enterRule(_localctx, 10, RULE_ghostStatement);
		int _la;
		try {
			setState(345);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(337);
				match(GHOST);
				setState(338);
				statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(339);
				match(ASSERT);
				setState(340);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(341);
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
				setState(342);
				predicateAccess();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(343);
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
				setState(344);
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
		enterRule(_localctx, 12, RULE_ghostPrimaryExpr);
		int _la;
		try {
			setState(364);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(348);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(349);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(350);
				isComparable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(351);
				old();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(352);
				sConversion();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(353);
				optionNone();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(354);
				optionSome();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(355);
				optionGet();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(356);
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
				setState(357);
				boundVariables();
				setState(358);
				match(COLON);
				setState(359);
				match(COLON);
				setState(360);
				triggers();
				setState(361);
				expression(0);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(363);
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
		enterRule(_localctx, 14, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			boundVariableDecl();
			setState(371);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(367);
					match(COMMA);
					setState(368);
					boundVariableDecl();
					}
					} 
				}
				setState(373);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(375);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(374);
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
		enterRule(_localctx, 16, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(377);
			match(IDENTIFIER);
			setState(382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(378);
				match(COMMA);
				setState(379);
				match(IDENTIFIER);
				}
				}
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(385);
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
		enterRule(_localctx, 18, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(387);
				trigger();
				}
				}
				setState(392);
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
		enterRule(_localctx, 20, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(L_CURLY);
			setState(394);
			expression(0);
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(395);
				match(COMMA);
				setState(396);
				expression(0);
				}
				}
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(402);
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
		enterRule(_localctx, 22, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
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
		enterRule(_localctx, 24, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406);
			match(SOME);
			setState(407);
			match(L_PAREN);
			setState(408);
			expression(0);
			setState(409);
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
		enterRule(_localctx, 26, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(NONE);
			setState(412);
			match(L_BRACKET);
			setState(413);
			type_();
			setState(414);
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
		enterRule(_localctx, 28, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(GET);
			setState(417);
			match(L_PAREN);
			setState(418);
			expression(0);
			setState(419);
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
		enterRule(_localctx, 30, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
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
			setState(422);
			match(L_PAREN);
			setState(423);
			expression(0);
			setState(424);
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
		enterRule(_localctx, 32, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(OLD);
			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(427);
				match(L_BRACKET);
				setState(428);
				oldLabelUse();
				setState(429);
				match(R_BRACKET);
				}
			}

			setState(433);
			match(L_PAREN);
			setState(434);
			expression(0);
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
			setState(439);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(437);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(438);
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
			setState(441);
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
		enterRule(_localctx, 38, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(443);
			match(IS_COMPARABLE);
			setState(444);
			match(L_PAREN);
			setState(445);
			expression(0);
			setState(446);
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
		enterRule(_localctx, 40, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
			match(TYPE_OF);
			setState(449);
			match(L_PAREN);
			setState(450);
			expression(0);
			setState(451);
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
		enterRule(_localctx, 42, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			match(ACCESS);
			setState(454);
			match(L_PAREN);
			setState(455);
			expression(0);
			setState(461);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(456);
				match(COMMA);
				setState(459);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(457);
					match(IDENTIFIER);
					}
					break;
				case 2:
					{
					setState(458);
					expression(0);
					}
					break;
				}
				}
			}

			setState(463);
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
		enterRule(_localctx, 44, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
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
			setState(466);
			match(L_BRACKET);
			setState(467);
			expression(0);
			setState(468);
			match(DOT_DOT);
			setState(469);
			expression(0);
			setState(470);
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
		enterRule(_localctx, 46, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(472);
			match(L_BRACKET);
			{
			setState(473);
			seqUpdClause();
			setState(478);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(474);
				match(COMMA);
				setState(475);
				seqUpdClause();
				}
				}
				setState(480);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(481);
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
		enterRule(_localctx, 48, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			expression(0);
			setState(484);
			match(ASSIGN);
			setState(485);
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
		enterRule(_localctx, 50, RULE_ghostTypeLit);
		try {
			setState(490);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(487);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(488);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(489);
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
		enterRule(_localctx, 52, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(492);
			match(DOM);
			setState(493);
			match(L_CURLY);
			setState(499);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(494);
				domainClause();
				setState(495);
				eos();
				}
				}
				setState(501);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(502);
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
		enterRule(_localctx, 54, RULE_domainClause);
		try {
			setState(513);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(504);
				match(FUNC);
				setState(505);
				match(IDENTIFIER);
				setState(506);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(507);
				match(AXIOM);
				setState(508);
				match(L_CURLY);
				setState(509);
				expression(0);
				setState(510);
				eos();
				setState(511);
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
		enterRule(_localctx, 56, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			match(GHOST);
			setState(516);
			match(L_BRACKET);
			setState(517);
			match(R_BRACKET);
			setState(518);
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
		enterRule(_localctx, 58, RULE_sqType);
		int _la;
		try {
			setState(531);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(520);
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
				setState(521);
				match(L_BRACKET);
				setState(522);
				type_();
				setState(523);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(525);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(526);
				match(L_BRACKET);
				setState(527);
				type_();
				setState(528);
				match(R_BRACKET);
				setState(529);
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
		enterRule(_localctx, 60, RULE_specification);
		int _la;
		try {
			setState(553);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(538);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC))) != 0)) {
					{
					{
					{
					setState(533);
					specStatement();
					}
					setState(534);
					eos();
					}
					}
					setState(540);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(541);
				match(PURE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(550);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << TRUSTED))) != 0)) {
					{
					{
					setState(545);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(542);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(543);
						match(PURE);
						}
						break;
					case TRUSTED:
						{
						setState(544);
						match(TRUSTED);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(547);
					eos();
					}
					}
					setState(552);
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
		enterRule(_localctx, 62, RULE_specStatement);
		try {
			setState(563);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(555);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(556);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(557);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(558);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(559);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(560);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(561);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(562);
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
		enterRule(_localctx, 64, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(565);
				expressionList();
				}
				break;
			}
			setState(570);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(568);
				match(IF);
				setState(569);
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
		enterRule(_localctx, 66, RULE_assertion);
		try {
			setState(574);
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
				setState(573);
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
		enterRule(_localctx, 68, RULE_blockWithBodyParameterInfo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(576);
			match(L_CURLY);
			setState(581);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SHARE) {
				{
				setState(577);
				match(SHARE);
				setState(578);
				identifierList();
				setState(579);
				eos();
				}
			}

			setState(584);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(583);
				statementList();
				}
			}

			setState(586);
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
		enterRule(_localctx, 70, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(588);
			type_();
			setState(589);
			match(IMPL);
			setState(590);
			type_();
			setState(609);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(591);
				match(L_CURLY);
				setState(597);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(592);
					implementationProofPredicateAlias();
					setState(593);
					eos();
					}
					}
					setState(599);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(605);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(600);
					methodImplementationProof();
					setState(601);
					eos();
					}
					}
					setState(607);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(608);
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
		enterRule(_localctx, 72, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(611);
				match(PURE);
				}
			}

			setState(614);
			nonLocalReceiver();
			setState(615);
			match(IDENTIFIER);
			setState(616);
			signature();
			setState(618);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(617);
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
		enterRule(_localctx, 74, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620);
			match(L_PAREN);
			setState(622);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(621);
				match(IDENTIFIER);
				}
				break;
			}
			setState(625);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(624);
				match(STAR);
				}
			}

			setState(627);
			typeName();
			setState(628);
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
		enterRule(_localctx, 76, RULE_selection);
		try {
			setState(635);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(630);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(631);
				type_();
				setState(632);
				match(DOT);
				setState(633);
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
		enterRule(_localctx, 78, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			match(PRED);
			setState(638);
			match(IDENTIFIER);
			setState(639);
			match(DECLARE_ASSIGN);
			setState(642);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(640);
				selection();
				}
				break;
			case 2:
				{
				setState(641);
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
		enterRule(_localctx, 80, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(644);
			match(MAKE);
			setState(645);
			match(L_PAREN);
			setState(646);
			type_();
			setState(649);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(647);
				match(COMMA);
				setState(648);
				expressionList();
				}
			}

			setState(651);
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
		enterRule(_localctx, 82, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			match(NEW);
			setState(654);
			match(L_PAREN);
			setState(655);
			type_();
			setState(656);
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
		enterRule(_localctx, 84, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(658);
			specification();
			setState(659);
			match(FUNC);
			setState(660);
			match(IDENTIFIER);
			{
			setState(661);
			signature();
			setState(663);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(662);
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
		enterRule(_localctx, 86, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(665);
			specification();
			setState(666);
			match(FUNC);
			setState(667);
			receiver();
			setState(668);
			match(IDENTIFIER);
			{
			setState(669);
			signature();
			setState(671);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(670);
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
		enterRule(_localctx, 88, RULE_sourceFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			packageClause();
			setState(674);
			eos();
			setState(680);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(675);
				importDecl();
				setState(676);
				eos();
				}
				}
				setState(682);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(693);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED) | (1L << TRUSTED))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (CONST - 64)) | (1L << (TYPE - 64)) | (1L << (VAR - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || _la==STAR || _la==RECEIVE) {
				{
				{
				setState(687);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
				case 1:
					{
					setState(683);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(684);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(685);
					declaration();
					}
					break;
				case 4:
					{
					setState(686);
					ghostMember();
					}
					break;
				}
				setState(689);
				eos();
				}
				}
				setState(695);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(696);
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
		enterRule(_localctx, 90, RULE_ghostMember);
		try {
			setState(713);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(698);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(699);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(700);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(704);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
				case 1:
					{
					setState(701);
					match(GHOST);
					}
					break;
				case 2:
					{
					{
					setState(702);
					match(GHOST);
					setState(703);
					eos();
					}
					}
					break;
				}
				setState(711);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
				case 1:
					{
					setState(706);
					methodDecl();
					}
					break;
				case 2:
					{
					setState(707);
					functionDecl();
					}
					break;
				case 3:
					{
					setState(708);
					constDecl();
					}
					break;
				case 4:
					{
					setState(709);
					typeDecl();
					}
					break;
				case 5:
					{
					setState(710);
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
		enterRule(_localctx, 92, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(715);
			match(PRED);
			setState(716);
			match(IDENTIFIER);
			setState(717);
			parameters();
			setState(719);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(718);
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
		enterRule(_localctx, 94, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(721);
			match(L_CURLY);
			setState(722);
			expression(0);
			setState(723);
			eos();
			setState(724);
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
		enterRule(_localctx, 96, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			match(PRED);
			setState(727);
			receiver();
			setState(728);
			match(IDENTIFIER);
			setState(729);
			parameters();
			setState(731);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(730);
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
		enterRule(_localctx, 98, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(733);
			maybeAddressableIdentifierList();
			setState(741);
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
			case FLOAT32:
			case FLOAT64:
			case COMPLEX64:
			case COMPLEX128:
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
				setState(734);
				type_();
				setState(737);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(735);
					match(ASSIGN);
					setState(736);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(739);
				match(ASSIGN);
				setState(740);
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
		enterRule(_localctx, 100, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(743);
			maybeAddressableIdentifierList();
			setState(744);
			match(DECLARE_ASSIGN);
			setState(745);
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
		enterRule(_localctx, 102, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(747);
			match(L_PAREN);
			setState(749);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(748);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(751);
			type_();
			setState(753);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(752);
				match(COMMA);
				}
			}

			setState(755);
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
		enterRule(_localctx, 104, RULE_parameterDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(758);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(757);
				match(GHOST);
				}
				break;
			}
			setState(761);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(760);
				identifierList();
				}
				break;
			}
			setState(764);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(763);
				match(ELLIPSIS);
				}
			}

			setState(766);
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
		enterRule(_localctx, 106, RULE_unaryExpr);
		int _la;
		try {
			setState(777);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(768);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(769);
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
				setState(770);
				match(L_PAREN);
				setState(771);
				expression(0);
				setState(772);
				match(R_PAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(774);
				unfolding();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(775);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(776);
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
		enterRule(_localctx, 108, RULE_unfolding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
			match(UNFOLDING);
			setState(780);
			predicateAccess();
			setState(781);
			match(IN);
			setState(782);
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
		int _startState = 110;
		enterRecursionRule(_localctx, 110, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(785);
				match(TYPE);
				setState(786);
				match(L_BRACKET);
				setState(787);
				type_();
				setState(788);
				match(R_BRACKET);
				}
				break;
			case 2:
				{
				setState(790);
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
				setState(791);
				match(L_PAREN);
				setState(792);
				expression(0);
				setState(793);
				match(R_PAREN);
				}
				break;
			case 3:
				{
				setState(795);
				unfolding();
				}
				break;
			case 4:
				{
				setState(796);
				make();
				}
				break;
			case 5:
				{
				setState(797);
				((ExpressionContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)))) != 0)) ) {
					((ExpressionContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(798);
				expression(11);
				}
				break;
			case 6:
				{
				setState(799);
				primaryExpr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(834);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(832);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(802);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(803);
						((ExpressionContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (DIV - 134)) | (1L << (MOD - 134)) | (1L << (LSHIFT - 134)) | (1L << (RSHIFT - 134)) | (1L << (BIT_CLEAR - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)))) != 0)) ) {
							((ExpressionContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(804);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(805);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(806);
						((ExpressionContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (PLUS_PLUS - 121)) | (1L << (OR - 121)) | (1L << (PLUS - 121)) | (1L << (MINUS - 121)) | (1L << (CARET - 121)))) != 0)) ) {
							((ExpressionContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(807);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(808);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(809);
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
						setState(810);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(811);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(812);
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
						setState(813);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(814);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(815);
						((ExpressionContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (EQUALS - 127)) | (1L << (NOT_EQUALS - 127)) | (1L << (LESS - 127)) | (1L << (LESS_OR_EQUALS - 127)) | (1L << (GREATER - 127)) | (1L << (GREATER_OR_EQUALS - 127)))) != 0)) ) {
							((ExpressionContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(816);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(817);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(818);
						match(LOGICAL_AND);
						setState(819);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(820);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(821);
						match(LOGICAL_OR);
						setState(822);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(823);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(824);
						match(IMPLIES);
						setState(825);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(826);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(827);
						match(QMARK);
						setState(828);
						expression(0);
						setState(829);
						match(COLON);
						setState(830);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(836);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
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
		enterRule(_localctx, 112, RULE_statement);
		try {
			setState(855);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(837);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(838);
				packageStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(839);
				applyStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(840);
				declaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(841);
				labeledStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(842);
				simpleStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(843);
				goStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(844);
				returnStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(845);
				breakStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(846);
				continueStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(847);
				gotoStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(848);
				fallthroughStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(849);
				block();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(850);
				ifStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(851);
				switchStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(852);
				selectStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(853);
				specForStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(854);
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
		enterRule(_localctx, 114, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(857);
			match(APPLY);
			setState(858);
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
		enterRule(_localctx, 116, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(860);
			match(PACKAGE);
			setState(861);
			expression(0);
			setState(863);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(862);
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
		enterRule(_localctx, 118, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(865);
			loopSpec();
			setState(866);
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
		enterRule(_localctx, 120, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(868);
				match(INV);
				setState(869);
				expression(0);
				setState(870);
				eos();
				}
				}
				setState(876);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(881);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(877);
				match(DEC);
				setState(878);
				terminationMeasure();
				setState(879);
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
		enterRule(_localctx, 122, RULE_basicLit);
		try {
			setState(891);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
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
		public New_Context new_() {
			return getRuleContext(New_Context.class,0);
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
			setState(899);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
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
			case 5:
				{
				setState(898);
				new_();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(915);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrimaryExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
					setState(901);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(911);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
					case 1:
						{
						{
						setState(902);
						match(DOT);
						setState(903);
						match(IDENTIFIER);
						}
						}
						break;
					case 2:
						{
						setState(904);
						index();
						}
						break;
					case 3:
						{
						setState(905);
						slice_();
						}
						break;
					case 4:
						{
						setState(906);
						seqUpdExp();
						}
						break;
					case 5:
						{
						setState(907);
						typeAssertion();
						}
						break;
					case 6:
						{
						setState(908);
						if (!(noTerminatorBetween(1))) throw new FailedPredicateException(this, "noTerminatorBetween(1)");
						setState(909);
						arguments();
						}
						break;
					case 7:
						{
						setState(910);
						predConstructArgs();
						}
						break;
					}
					}
					} 
				}
				setState(917);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
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
			setState(918);
			match(L_PRED);
			setState(920);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(919);
				expressionList();
				}
			}

			setState(923);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(922);
				match(COMMA);
				}
			}

			setState(925);
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
			setState(927);
			match(INTERFACE);
			setState(928);
			match(L_CURLY);
			setState(938);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(932);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
					case 1:
						{
						setState(929);
						methodSpec();
						}
						break;
					case 2:
						{
						setState(930);
						typeName();
						}
						break;
					case 3:
						{
						setState(931);
						predicateSpec();
						}
						break;
					}
					setState(934);
					eos();
					}
					} 
				}
				setState(940);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			}
			setState(941);
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
			setState(943);
			match(PRED);
			setState(944);
			match(IDENTIFIER);
			setState(945);
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
			setState(963);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(947);
				if (!(noTerminatorAfterParams(2))) throw new FailedPredicateException(this, "noTerminatorAfterParams(2)");
				setState(949);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(948);
					match(GHOST);
					}
				}

				setState(951);
				specification();
				setState(952);
				match(IDENTIFIER);
				setState(953);
				parameters();
				setState(954);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(957);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(956);
					match(GHOST);
					}
				}

				setState(959);
				specification();
				setState(960);
				match(IDENTIFIER);
				setState(961);
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
		public TerminalNode FLOAT32() { return getToken(GobraParser.FLOAT32, 0); }
		public TerminalNode FLOAT64() { return getToken(GobraParser.FLOAT64, 0); }
		public TerminalNode COMPLEX64() { return getToken(GobraParser.COMPLEX64, 0); }
		public TerminalNode COMPLEX128() { return getToken(GobraParser.COMPLEX128, 0); }
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
			setState(973);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(965);
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
				setState(966);
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
				setState(967);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(968);
				match(L_PAREN);
				setState(969);
				type_();
				setState(970);
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
			case FLOAT32:
			case FLOAT64:
			case COMPLEX64:
			case COMPLEX128:
				enterOuterAlt(_localctx, 5);
				{
				setState(972);
				((Type_Context)_localctx).predefined = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)))) != 0)) ) {
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
			setState(984);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(975);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(976);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(977);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(978);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(979);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(980);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(981);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(982);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(983);
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
			setState(986);
			match(PRED);
			setState(987);
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
			setState(989);
			match(L_PAREN);
			setState(1001);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & ((1L << (GHOST - 24)) | (1L << (SEQ - 24)) | (1L << (SET - 24)) | (1L << (MSET - 24)) | (1L << (DICT - 24)) | (1L << (OPT - 24)) | (1L << (DOM - 24)) | (1L << (PRED - 24)) | (1L << (BOOL - 24)) | (1L << (STRING - 24)) | (1L << (PERM - 24)) | (1L << (RUNE - 24)) | (1L << (INT - 24)) | (1L << (INT8 - 24)) | (1L << (INT16 - 24)) | (1L << (INT32 - 24)) | (1L << (INT64 - 24)) | (1L << (BYTE - 24)) | (1L << (UINT - 24)) | (1L << (UINT8 - 24)) | (1L << (UINT16 - 24)) | (1L << (UINT32 - 24)) | (1L << (UINT64 - 24)) | (1L << (UINTPTR - 24)) | (1L << (FLOAT32 - 24)) | (1L << (FLOAT64 - 24)) | (1L << (COMPLEX64 - 24)) | (1L << (COMPLEX128 - 24)) | (1L << (FUNC - 24)) | (1L << (INTERFACE - 24)))) != 0) || ((((_la - 92)) & ~0x3f) == 0 && ((1L << (_la - 92)) & ((1L << (MAP - 92)) | (1L << (STRUCT - 92)) | (1L << (CHAN - 92)) | (1L << (IDENTIFIER - 92)) | (1L << (L_PAREN - 92)) | (1L << (L_BRACKET - 92)) | (1L << (STAR - 92)) | (1L << (RECEIVE - 92)))) != 0)) {
				{
				setState(990);
				type_();
				setState(995);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(991);
						match(COMMA);
						setState(992);
						type_();
						}
						} 
					}
					setState(997);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				}
				setState(999);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(998);
					match(COMMA);
					}
				}

				}
			}

			setState(1003);
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
			setState(1015);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1005);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1006);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1007);
				match(L_BRACKET);
				setState(1008);
				match(ELLIPSIS);
				setState(1009);
				match(R_BRACKET);
				setState(1010);
				elementType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1011);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1012);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1013);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1014);
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
			setState(1017);
			match(L_BRACKET);
			setState(1033);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(1019);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
					{
					setState(1018);
					low();
					}
				}

				setState(1021);
				match(COLON);
				setState(1023);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
					{
					setState(1022);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1026);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
					{
					setState(1025);
					low();
					}
				}

				setState(1028);
				match(COLON);
				setState(1029);
				high();
				setState(1030);
				match(COLON);
				setState(1031);
				cap();
				}
				break;
			}
			setState(1035);
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
			setState(1037);
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
			setState(1039);
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
			setState(1041);
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
		enterRule(_localctx, 152, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1044);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (OR - 133)) | (1L << (DIV - 133)) | (1L << (MOD - 133)) | (1L << (LSHIFT - 133)) | (1L << (RSHIFT - 133)) | (1L << (BIT_CLEAR - 133)) | (1L << (PLUS - 133)) | (1L << (MINUS - 133)) | (1L << (CARET - 133)) | (1L << (STAR - 133)) | (1L << (AMPERSAND - 133)))) != 0)) {
				{
				setState(1043);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (OR - 133)) | (1L << (DIV - 133)) | (1L << (MOD - 133)) | (1L << (LSHIFT - 133)) | (1L << (RSHIFT - 133)) | (1L << (BIT_CLEAR - 133)) | (1L << (PLUS - 133)) | (1L << (MINUS - 133)) | (1L << (CARET - 133)) | (1L << (STAR - 133)) | (1L << (AMPERSAND - 133)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1046);
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
		enterRule(_localctx, 154, RULE_eos);
		try {
			setState(1053);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1048);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1049);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1050);
				if (!(lineTerminatorAhead())) throw new FailedPredicateException(this, "lineTerminatorAhead()");
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1051);
				if (!(checkPreviousTokenText("}"))) throw new FailedPredicateException(this, "checkPreviousTokenText(\"}\")");
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1052);
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
		enterRule(_localctx, 156, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1055);
			match(PACKAGE);
			setState(1056);
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
		enterRule(_localctx, 158, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			match(IMPORT);
			setState(1070);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1059);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1060);
				match(L_PAREN);
				setState(1066);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & ((1L << (IDENTIFIER - 109)) | (1L << (DOT - 109)) | (1L << (RAW_STRING_LIT - 109)) | (1L << (INTERPRETED_STRING_LIT - 109)))) != 0)) {
					{
					{
					setState(1061);
					importSpec();
					setState(1062);
					eos();
					}
					}
					setState(1068);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1069);
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
		enterRule(_localctx, 160, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1073);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1072);
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

			setState(1075);
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
		enterRule(_localctx, 162, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1077);
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
		enterRule(_localctx, 164, RULE_declaration);
		try {
			setState(1082);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1079);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1080);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1081);
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
		enterRule(_localctx, 166, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1084);
			match(CONST);
			setState(1096);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1085);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1086);
				match(L_PAREN);
				setState(1092);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1087);
					constSpec();
					setState(1088);
					eos();
					}
					}
					setState(1094);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1095);
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
		enterRule(_localctx, 168, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1098);
			identifierList();
			setState(1104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				{
				setState(1100);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & ((1L << (GHOST - 24)) | (1L << (SEQ - 24)) | (1L << (SET - 24)) | (1L << (MSET - 24)) | (1L << (DICT - 24)) | (1L << (OPT - 24)) | (1L << (DOM - 24)) | (1L << (PRED - 24)) | (1L << (BOOL - 24)) | (1L << (STRING - 24)) | (1L << (PERM - 24)) | (1L << (RUNE - 24)) | (1L << (INT - 24)) | (1L << (INT8 - 24)) | (1L << (INT16 - 24)) | (1L << (INT32 - 24)) | (1L << (INT64 - 24)) | (1L << (BYTE - 24)) | (1L << (UINT - 24)) | (1L << (UINT8 - 24)) | (1L << (UINT16 - 24)) | (1L << (UINT32 - 24)) | (1L << (UINT64 - 24)) | (1L << (UINTPTR - 24)) | (1L << (FLOAT32 - 24)) | (1L << (FLOAT64 - 24)) | (1L << (COMPLEX64 - 24)) | (1L << (COMPLEX128 - 24)) | (1L << (FUNC - 24)) | (1L << (INTERFACE - 24)))) != 0) || ((((_la - 92)) & ~0x3f) == 0 && ((1L << (_la - 92)) & ((1L << (MAP - 92)) | (1L << (STRUCT - 92)) | (1L << (CHAN - 92)) | (1L << (IDENTIFIER - 92)) | (1L << (L_PAREN - 92)) | (1L << (L_BRACKET - 92)) | (1L << (STAR - 92)) | (1L << (RECEIVE - 92)))) != 0)) {
					{
					setState(1099);
					type_();
					}
				}

				setState(1102);
				match(ASSIGN);
				setState(1103);
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
		enterRule(_localctx, 170, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1106);
			match(IDENTIFIER);
			setState(1111);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,94,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1107);
					match(COMMA);
					setState(1108);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1113);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,94,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 172, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1114);
			expression(0);
			setState(1119);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,95,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1115);
					match(COMMA);
					setState(1116);
					expression(0);
					}
					} 
				}
				setState(1121);
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
		enterRule(_localctx, 174, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1122);
			match(TYPE);
			setState(1134);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1123);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1124);
				match(L_PAREN);
				setState(1130);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1125);
					typeSpec();
					setState(1126);
					eos();
					}
					}
					setState(1132);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1133);
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
		enterRule(_localctx, 176, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1136);
			match(IDENTIFIER);
			setState(1138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1137);
				match(ASSIGN);
				}
			}

			setState(1140);
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
		enterRule(_localctx, 178, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1142);
			match(VAR);
			setState(1154);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1143);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1144);
				match(L_PAREN);
				setState(1150);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1145);
					varSpec();
					setState(1146);
					eos();
					}
					}
					setState(1152);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1153);
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
		enterRule(_localctx, 180, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1156);
			match(L_CURLY);
			setState(1158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1157);
				statementList();
				}
			}

			setState(1160);
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
		enterRule(_localctx, 182, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1165); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1162);
				statement();
				setState(1163);
				eos();
				}
				}
				setState(1167); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0) );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 184, RULE_simpleStmt);
		try {
			setState(1175);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1169);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1170);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1171);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1172);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1173);
				shortVarDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1174);
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

	public static class TerminatedSimpleStmtContext extends ParserRuleContext {
		public SendStmtContext sendStmt() {
			return getRuleContext(SendStmtContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
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
		public TerminatedSimpleStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terminatedSimpleStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitTerminatedSimpleStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TerminatedSimpleStmtContext terminatedSimpleStmt() throws RecognitionException {
		TerminatedSimpleStmtContext _localctx = new TerminatedSimpleStmtContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_terminatedSimpleStmt);
		try {
			setState(1193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1177);
				sendStmt();
				setState(1178);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1180);
				incDecStmt();
				setState(1181);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1183);
				assignment();
				setState(1184);
				match(SEMI);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1186);
				expressionStmt();
				setState(1187);
				match(SEMI);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1189);
				shortVarDecl();
				setState(1190);
				match(SEMI);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1192);
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
		enterRule(_localctx, 188, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1195);
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
		enterRule(_localctx, 190, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1197);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1198);
			match(RECEIVE);
			setState(1199);
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
		enterRule(_localctx, 192, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1201);
			expression(0);
			setState(1202);
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
		enterRule(_localctx, 194, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1204);
			expressionList();
			setState(1205);
			assign_op();
			setState(1206);
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
		enterRule(_localctx, 196, RULE_emptyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1208);
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
		enterRule(_localctx, 198, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1210);
			match(IDENTIFIER);
			setState(1211);
			match(COLON);
			setState(1213);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1212);
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
		enterRule(_localctx, 200, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1215);
			match(RETURN);
			setState(1217);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1216);
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
		enterRule(_localctx, 202, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1219);
			match(BREAK);
			setState(1221);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(1220);
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
		enterRule(_localctx, 204, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1223);
			match(CONTINUE);
			setState(1225);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				{
				setState(1224);
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
		enterRule(_localctx, 206, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1227);
			match(GOTO);
			setState(1228);
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
		enterRule(_localctx, 208, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1230);
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
		enterRule(_localctx, 210, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1232);
			match(DEFER);
			setState(1233);
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
		public TerminatedSimpleStmtContext terminatedSimpleStmt() {
			return getRuleContext(TerminatedSimpleStmtContext.class,0);
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
		enterRule(_localctx, 212, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			match(IF);
			setState(1237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1236);
				terminatedSimpleStmt();
				}
				break;
			}
			setState(1239);
			expression(0);
			setState(1240);
			block();
			setState(1246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1241);
				match(ELSE);
				setState(1244);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1242);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1243);
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
		enterRule(_localctx, 214, RULE_switchStmt);
		try {
			setState(1250);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1248);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1249);
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
		public TerminatedSimpleStmtContext terminatedSimpleStmt() {
			return getRuleContext(TerminatedSimpleStmtContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<ExprCaseClauseContext> exprCaseClause() {
			return getRuleContexts(ExprCaseClauseContext.class);
		}
		public ExprCaseClauseContext exprCaseClause(int i) {
			return getRuleContext(ExprCaseClauseContext.class,i);
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
		enterRule(_localctx, 216, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1252);
			match(SWITCH);
			setState(1254);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				{
				setState(1253);
				terminatedSimpleStmt();
				}
				break;
			}
			setState(1257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1256);
				expression(0);
				}
			}

			setState(1259);
			match(L_CURLY);
			setState(1263);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1260);
				exprCaseClause();
				}
				}
				setState(1265);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1266);
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
		enterRule(_localctx, 218, RULE_exprCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1268);
			exprSwitchCase();
			setState(1269);
			match(COLON);
			setState(1271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1270);
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
			setState(1276);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1273);
				match(CASE);
				setState(1274);
				expressionList();
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

	public static class TypeSwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(GobraParser.SWITCH, 0); }
		public TypeSwitchGuardContext typeSwitchGuard() {
			return getRuleContext(TypeSwitchGuardContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public TerminatedSimpleStmtContext terminatedSimpleStmt() {
			return getRuleContext(TerminatedSimpleStmtContext.class,0);
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
		enterRule(_localctx, 222, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1278);
			match(SWITCH);
			setState(1280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1279);
				terminatedSimpleStmt();
				}
				break;
			}
			setState(1282);
			typeSwitchGuard();
			setState(1283);
			match(L_CURLY);
			setState(1287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1284);
				typeCaseClause();
				}
				}
				setState(1289);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1290);
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
		enterRule(_localctx, 224, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1292);
				match(IDENTIFIER);
				setState(1293);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1296);
			primaryExpr(0);
			setState(1297);
			match(DOT);
			setState(1298);
			match(L_PAREN);
			setState(1299);
			match(TYPE);
			setState(1300);
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
		enterRule(_localctx, 226, RULE_typeCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1302);
			typeSwitchCase();
			setState(1303);
			match(COLON);
			setState(1305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1304);
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
		enterRule(_localctx, 228, RULE_typeSwitchCase);
		try {
			setState(1310);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1307);
				match(CASE);
				setState(1308);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1309);
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
		enterRule(_localctx, 230, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1314);
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
			case FLOAT32:
			case FLOAT64:
			case COMPLEX64:
			case COMPLEX128:
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
				setState(1312);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1313);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1316);
				match(COMMA);
				setState(1319);
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
				case FLOAT32:
				case FLOAT64:
				case COMPLEX64:
				case COMPLEX128:
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
					setState(1317);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1318);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1325);
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
		enterRule(_localctx, 232, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1326);
			match(SELECT);
			setState(1327);
			match(L_CURLY);
			setState(1331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1328);
				commClause();
				}
				}
				setState(1333);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1334);
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
		enterRule(_localctx, 234, RULE_commClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1336);
			commCase();
			setState(1337);
			match(COLON);
			setState(1339);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1338);
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
		enterRule(_localctx, 236, RULE_commCase);
		try {
			setState(1347);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1341);
				match(CASE);
				setState(1344);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
				case 1:
					{
					setState(1342);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1343);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1346);
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
		enterRule(_localctx, 238, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1355);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1349);
				expressionList();
				setState(1350);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1352);
				identifierList();
				setState(1353);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1357);
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
		enterRule(_localctx, 240, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1359);
			match(FOR);
			setState(1363);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1360);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1361);
				forClause();
				}
				break;
			case 3:
				{
				setState(1362);
				rangeClause();
				}
				break;
			}
			setState(1365);
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
		public TerminatedSimpleStmtContext initStmt;
		public SimpleStmtContext postStmt;
		public TerminalNode SEMI() { return getToken(GobraParser.SEMI, 0); }
		public TerminatedSimpleStmtContext terminatedSimpleStmt() {
			return getRuleContext(TerminatedSimpleStmtContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SimpleStmtContext simpleStmt() {
			return getRuleContext(SimpleStmtContext.class,0);
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
		enterRule(_localctx, 242, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1367);
			((ForClauseContext)_localctx).initStmt = terminatedSimpleStmt();
			setState(1369);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1368);
				expression(0);
				}
			}

			setState(1371);
			match(SEMI);
			setState(1373);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1372);
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
		enterRule(_localctx, 244, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1381);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1375);
				expressionList();
				setState(1376);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1378);
				identifierList();
				setState(1379);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1383);
			match(RANGE);
			setState(1384);
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
		enterRule(_localctx, 246, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1386);
			match(GO);
			setState(1387);
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
		enterRule(_localctx, 248, RULE_typeName);
		try {
			setState(1391);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1389);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1390);
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
		enterRule(_localctx, 250, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1393);
			match(L_BRACKET);
			setState(1394);
			arrayLength();
			setState(1395);
			match(R_BRACKET);
			setState(1396);
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
		enterRule(_localctx, 252, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1398);
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
		enterRule(_localctx, 254, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1400);
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
		enterRule(_localctx, 256, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1402);
			match(STAR);
			setState(1403);
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
		enterRule(_localctx, 258, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1405);
			match(L_BRACKET);
			setState(1406);
			match(R_BRACKET);
			setState(1407);
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
		enterRule(_localctx, 260, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1409);
			match(MAP);
			setState(1410);
			match(L_BRACKET);
			setState(1411);
			type_();
			setState(1412);
			match(R_BRACKET);
			setState(1413);
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
		enterRule(_localctx, 262, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1420);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1415);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1416);
				match(CHAN);
				setState(1417);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1418);
				match(RECEIVE);
				setState(1419);
				match(CHAN);
				}
				break;
			}
			setState(1422);
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
		enterRule(_localctx, 264, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1424);
			match(FUNC);
			setState(1425);
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
		enterRule(_localctx, 266, RULE_signature);
		try {
			setState(1432);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1427);
				if (!(noTerminatorAfterParams(1))) throw new FailedPredicateException(this, "noTerminatorAfterParams(1)");
				setState(1428);
				parameters();
				setState(1429);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1431);
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
		enterRule(_localctx, 268, RULE_result);
		try {
			setState(1436);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1434);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1435);
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
		enterRule(_localctx, 270, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1438);
			match(L_PAREN);
			setState(1450);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & ((1L << (GHOST - 24)) | (1L << (SEQ - 24)) | (1L << (SET - 24)) | (1L << (MSET - 24)) | (1L << (DICT - 24)) | (1L << (OPT - 24)) | (1L << (DOM - 24)) | (1L << (PRED - 24)) | (1L << (BOOL - 24)) | (1L << (STRING - 24)) | (1L << (PERM - 24)) | (1L << (RUNE - 24)) | (1L << (INT - 24)) | (1L << (INT8 - 24)) | (1L << (INT16 - 24)) | (1L << (INT32 - 24)) | (1L << (INT64 - 24)) | (1L << (BYTE - 24)) | (1L << (UINT - 24)) | (1L << (UINT8 - 24)) | (1L << (UINT16 - 24)) | (1L << (UINT32 - 24)) | (1L << (UINT64 - 24)) | (1L << (UINTPTR - 24)) | (1L << (FLOAT32 - 24)) | (1L << (FLOAT64 - 24)) | (1L << (COMPLEX64 - 24)) | (1L << (COMPLEX128 - 24)) | (1L << (FUNC - 24)) | (1L << (INTERFACE - 24)))) != 0) || ((((_la - 92)) & ~0x3f) == 0 && ((1L << (_la - 92)) & ((1L << (MAP - 92)) | (1L << (STRUCT - 92)) | (1L << (CHAN - 92)) | (1L << (IDENTIFIER - 92)) | (1L << (L_PAREN - 92)) | (1L << (L_BRACKET - 92)) | (1L << (ELLIPSIS - 92)) | (1L << (STAR - 92)) | (1L << (RECEIVE - 92)))) != 0)) {
				{
				setState(1439);
				parameterDecl();
				setState(1444);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1440);
						match(COMMA);
						setState(1441);
						parameterDecl();
						}
						} 
					}
					setState(1446);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
				}
				setState(1448);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1447);
					match(COMMA);
					}
				}

				}
			}

			setState(1452);
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
		enterRule(_localctx, 272, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1454);
			type_();
			setState(1455);
			match(L_PAREN);
			setState(1456);
			expression(0);
			setState(1458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1457);
				match(COMMA);
				}
			}

			setState(1460);
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
		enterRule(_localctx, 274, RULE_operand);
		try {
			setState(1468);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1462);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1463);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1464);
				match(L_PAREN);
				setState(1465);
				expression(0);
				setState(1466);
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
		enterRule(_localctx, 276, RULE_literal);
		try {
			setState(1473);
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
				setState(1470);
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
				setState(1471);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1472);
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
		enterRule(_localctx, 278, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1475);
			_la = _input.LA(1);
			if ( !(((((_la - 146)) & ~0x3f) == 0 && ((1L << (_la - 146)) & ((1L << (DECIMAL_LIT - 146)) | (1L << (BINARY_LIT - 146)) | (1L << (OCTAL_LIT - 146)) | (1L << (HEX_LIT - 146)) | (1L << (IMAGINARY_LIT - 146)) | (1L << (RUNE_LIT - 146)))) != 0)) ) {
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
		enterRule(_localctx, 280, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1477);
			match(IDENTIFIER);
			setState(1480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				{
				setState(1478);
				match(DOT);
				setState(1479);
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
		enterRule(_localctx, 282, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1482);
			match(IDENTIFIER);
			setState(1483);
			match(DOT);
			setState(1484);
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
		enterRule(_localctx, 284, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1486);
			literalType();
			setState(1487);
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
		enterRule(_localctx, 286, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1489);
			match(L_CURLY);
			setState(1494);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1490);
				elementList();
				setState(1492);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1491);
					match(COMMA);
					}
				}

				}
			}

			setState(1496);
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
		enterRule(_localctx, 288, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1498);
			keyedElement();
			setState(1503);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,148,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1499);
					match(COMMA);
					setState(1500);
					keyedElement();
					}
					} 
				}
				setState(1505);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,148,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 290, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1509);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				{
				setState(1506);
				key();
				setState(1507);
				match(COLON);
				}
				break;
			}
			setState(1511);
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
		enterRule(_localctx, 292, RULE_key);
		try {
			setState(1516);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1513);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1514);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1515);
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
		enterRule(_localctx, 294, RULE_element);
		try {
			setState(1520);
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
			case FLOAT32:
			case FLOAT64:
			case COMPLEX64:
			case COMPLEX128:
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
			case IMAGINARY_LIT:
			case RUNE_LIT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1518);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1519);
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
		enterRule(_localctx, 296, RULE_structType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1522);
			match(STRUCT);
			setState(1523);
			match(L_CURLY);
			setState(1529);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,152,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1524);
					fieldDecl();
					setState(1525);
					eos();
					}
					} 
				}
				setState(1531);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,152,_ctx);
			}
			setState(1532);
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
		enterRule(_localctx, 298, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1539);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				{
				setState(1534);
				if (!(noTerminatorBetween(2))) throw new FailedPredicateException(this, "noTerminatorBetween(2)");
				setState(1535);
				identifierList();
				setState(1536);
				type_();
				}
				break;
			case 2:
				{
				setState(1538);
				embeddedField();
				}
				break;
			}
			setState(1542);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1541);
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
		enterRule(_localctx, 300, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1544);
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
		enterRule(_localctx, 302, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1547);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1546);
				match(STAR);
				}
			}

			setState(1549);
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
		enterRule(_localctx, 304, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1551);
			match(FUNC);
			setState(1552);
			signature();
			setState(1553);
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
		enterRule(_localctx, 306, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1555);
			match(L_BRACKET);
			setState(1556);
			expression(0);
			setState(1557);
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
		enterRule(_localctx, 308, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1559);
			match(DOT);
			setState(1560);
			match(L_PAREN);
			setState(1561);
			type_();
			setState(1562);
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
		enterRule(_localctx, 310, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1564);
			match(L_PAREN);
			setState(1579);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BOOL - 64)) | (1L << (STRING - 64)) | (1L << (PERM - 64)) | (1L << (RUNE - 64)) | (1L << (INT - 64)) | (1L << (INT8 - 64)) | (1L << (INT16 - 64)) | (1L << (INT32 - 64)) | (1L << (INT64 - 64)) | (1L << (BYTE - 64)) | (1L << (UINT - 64)) | (1L << (UINT8 - 64)) | (1L << (UINT16 - 64)) | (1L << (UINT32 - 64)) | (1L << (UINT64 - 64)) | (1L << (UINTPTR - 64)) | (1L << (FLOAT32 - 64)) | (1L << (FLOAT64 - 64)) | (1L << (COMPLEX64 - 64)) | (1L << (COMPLEX128 - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & ((1L << (EXCLAMATION - 139)) | (1L << (PLUS - 139)) | (1L << (MINUS - 139)) | (1L << (CARET - 139)) | (1L << (STAR - 139)) | (1L << (AMPERSAND - 139)) | (1L << (RECEIVE - 139)) | (1L << (DECIMAL_LIT - 139)) | (1L << (BINARY_LIT - 139)) | (1L << (OCTAL_LIT - 139)) | (1L << (HEX_LIT - 139)) | (1L << (IMAGINARY_LIT - 139)) | (1L << (RUNE_LIT - 139)) | (1L << (RAW_STRING_LIT - 139)) | (1L << (INTERPRETED_STRING_LIT - 139)))) != 0)) {
				{
				setState(1571);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
				case 1:
					{
					setState(1565);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1566);
					type_();
					setState(1569);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
					case 1:
						{
						setState(1567);
						match(COMMA);
						setState(1568);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1574);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1573);
					match(ELLIPSIS);
					}
				}

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
		enterRule(_localctx, 312, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1583);
			receiverType();
			setState(1584);
			match(DOT);
			setState(1585);
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
		enterRule(_localctx, 314, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1587);
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
		case 55:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 62:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 66:
			return methodSpec_sempred((MethodSpecContext)_localctx, predIndex);
		case 77:
			return eos_sempred((EosContext)_localctx, predIndex);
		case 133:
			return signature_sempred((SignatureContext)_localctx, predIndex);
		case 149:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a5\u0638\4\2\t"+
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
		"\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u014b\n\5\f\5\16\5\u014e"+
		"\13\5\3\6\3\6\5\6\u0152\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u015c"+
		"\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\5\b\u016f\n\b\3\t\3\t\3\t\7\t\u0174\n\t\f\t\16\t\u0177\13\t\3\t\5\t"+
		"\u017a\n\t\3\n\3\n\3\n\7\n\u017f\n\n\f\n\16\n\u0182\13\n\3\n\3\n\3\13"+
		"\7\13\u0187\n\13\f\13\16\13\u018a\13\13\3\f\3\f\3\f\3\f\7\f\u0190\n\f"+
		"\f\f\16\f\u0193\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\3\22\5\22\u01b2\n\22\3\22\3\22\3\22\3\22\3\23\3\23\5\23"+
		"\u01ba\n\23\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u01ce\n\27\5\27\u01d0\n\27\3\27\3"+
		"\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\7\31\u01df"+
		"\n\31\f\31\16\31\u01e2\13\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\3"+
		"\33\5\33\u01ed\n\33\3\34\3\34\3\34\3\34\3\34\7\34\u01f4\n\34\f\34\16\34"+
		"\u01f7\13\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\5"+
		"\35\u0204\n\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\5\37\u0216\n\37\3 \3 \3 \7 \u021b\n \f \16 "+
		"\u021e\13 \3 \3 \3 \3 \5 \u0224\n \3 \7 \u0227\n \f \16 \u022a\13 \5 "+
		"\u022c\n \3!\3!\3!\3!\3!\3!\3!\3!\5!\u0236\n!\3\"\5\"\u0239\n\"\3\"\3"+
		"\"\5\"\u023d\n\"\3#\3#\5#\u0241\n#\3$\3$\3$\3$\3$\5$\u0248\n$\3$\5$\u024b"+
		"\n$\3$\3$\3%\3%\3%\3%\3%\3%\3%\7%\u0256\n%\f%\16%\u0259\13%\3%\3%\3%\7"+
		"%\u025e\n%\f%\16%\u0261\13%\3%\5%\u0264\n%\3&\5&\u0267\n&\3&\3&\3&\3&"+
		"\5&\u026d\n&\3\'\3\'\5\'\u0271\n\'\3\'\5\'\u0274\n\'\3\'\3\'\3\'\3(\3"+
		"(\3(\3(\3(\5(\u027e\n(\3)\3)\3)\3)\3)\5)\u0285\n)\3*\3*\3*\3*\3*\5*\u028c"+
		"\n*\3*\3*\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\5,\u029a\n,\3-\3-\3-\3-\3-\3-"+
		"\5-\u02a2\n-\3.\3.\3.\3.\3.\7.\u02a9\n.\f.\16.\u02ac\13.\3.\3.\3.\3.\5"+
		".\u02b2\n.\3.\3.\7.\u02b6\n.\f.\16.\u02b9\13.\3.\3.\3/\3/\3/\3/\3/\3/"+
		"\5/\u02c3\n/\3/\3/\3/\3/\3/\5/\u02ca\n/\5/\u02cc\n/\3\60\3\60\3\60\3\60"+
		"\5\60\u02d2\n\60\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\5\62"+
		"\u02de\n\62\3\63\3\63\3\63\3\63\5\63\u02e4\n\63\3\63\3\63\5\63\u02e8\n"+
		"\63\3\64\3\64\3\64\3\64\3\65\3\65\5\65\u02f0\n\65\3\65\3\65\5\65\u02f4"+
		"\n\65\3\65\3\65\3\66\5\66\u02f9\n\66\3\66\5\66\u02fc\n\66\3\66\5\66\u02ff"+
		"\n\66\3\66\3\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\5\67\u030c"+
		"\n\67\38\38\38\38\38\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\5"+
		"9\u0323\n9\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\3"+
		"9\39\39\39\39\39\39\39\39\39\39\79\u0343\n9\f9\169\u0346\139\3:\3:\3:"+
		"\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\5:\u035a\n:\3;\3;\3;\3<"+
		"\3<\3<\5<\u0362\n<\3=\3=\3=\3>\3>\3>\3>\7>\u036b\n>\f>\16>\u036e\13>\3"+
		">\3>\3>\3>\5>\u0374\n>\3?\3?\3?\3?\3?\3?\3?\3?\5?\u037e\n?\3@\3@\3@\3"+
		"@\3@\3@\5@\u0386\n@\3@\3@\3@\3@\3@\3@\3@\3@\3@\3@\5@\u0392\n@\7@\u0394"+
		"\n@\f@\16@\u0397\13@\3A\3A\5A\u039b\nA\3A\5A\u039e\nA\3A\3A\3B\3B\3B\3"+
		"B\3B\5B\u03a7\nB\3B\3B\7B\u03ab\nB\fB\16B\u03ae\13B\3B\3B\3C\3C\3C\3C"+
		"\3D\3D\5D\u03b8\nD\3D\3D\3D\3D\3D\3D\5D\u03c0\nD\3D\3D\3D\3D\5D\u03c6"+
		"\nD\3E\3E\3E\3E\3E\3E\3E\3E\5E\u03d0\nE\3F\3F\3F\3F\3F\3F\3F\3F\3F\5F"+
		"\u03db\nF\3G\3G\3G\3H\3H\3H\3H\7H\u03e4\nH\fH\16H\u03e7\13H\3H\5H\u03ea"+
		"\nH\5H\u03ec\nH\3H\3H\3I\3I\3I\3I\3I\3I\3I\3I\3I\3I\5I\u03fa\nI\3J\3J"+
		"\5J\u03fe\nJ\3J\3J\5J\u0402\nJ\3J\5J\u0405\nJ\3J\3J\3J\3J\3J\5J\u040c"+
		"\nJ\3J\3J\3K\3K\3L\3L\3M\3M\3N\5N\u0417\nN\3N\3N\3O\3O\3O\3O\3O\5O\u0420"+
		"\nO\3P\3P\3P\3Q\3Q\3Q\3Q\3Q\3Q\7Q\u042b\nQ\fQ\16Q\u042e\13Q\3Q\5Q\u0431"+
		"\nQ\3R\5R\u0434\nR\3R\3R\3S\3S\3T\3T\3T\5T\u043d\nT\3U\3U\3U\3U\3U\3U"+
		"\7U\u0445\nU\fU\16U\u0448\13U\3U\5U\u044b\nU\3V\3V\5V\u044f\nV\3V\3V\5"+
		"V\u0453\nV\3W\3W\3W\7W\u0458\nW\fW\16W\u045b\13W\3X\3X\3X\7X\u0460\nX"+
		"\fX\16X\u0463\13X\3Y\3Y\3Y\3Y\3Y\3Y\7Y\u046b\nY\fY\16Y\u046e\13Y\3Y\5"+
		"Y\u0471\nY\3Z\3Z\5Z\u0475\nZ\3Z\3Z\3[\3[\3[\3[\3[\3[\7[\u047f\n[\f[\16"+
		"[\u0482\13[\3[\5[\u0485\n[\3\\\3\\\5\\\u0489\n\\\3\\\3\\\3]\3]\3]\6]\u0490"+
		"\n]\r]\16]\u0491\3^\3^\3^\3^\3^\3^\5^\u049a\n^\3_\3_\3_\3_\3_\3_\3_\3"+
		"_\3_\3_\3_\3_\3_\3_\3_\3_\5_\u04ac\n_\3`\3`\3a\3a\3a\3a\3b\3b\3b\3c\3"+
		"c\3c\3c\3d\3d\3e\3e\3e\5e\u04c0\ne\3f\3f\5f\u04c4\nf\3g\3g\5g\u04c8\n"+
		"g\3h\3h\5h\u04cc\nh\3i\3i\3i\3j\3j\3k\3k\3k\3l\3l\5l\u04d8\nl\3l\3l\3"+
		"l\3l\3l\5l\u04df\nl\5l\u04e1\nl\3m\3m\5m\u04e5\nm\3n\3n\5n\u04e9\nn\3"+
		"n\5n\u04ec\nn\3n\3n\7n\u04f0\nn\fn\16n\u04f3\13n\3n\3n\3o\3o\3o\5o\u04fa"+
		"\no\3p\3p\3p\5p\u04ff\np\3q\3q\5q\u0503\nq\3q\3q\3q\7q\u0508\nq\fq\16"+
		"q\u050b\13q\3q\3q\3r\3r\5r\u0511\nr\3r\3r\3r\3r\3r\3r\3s\3s\3s\5s\u051c"+
		"\ns\3t\3t\3t\5t\u0521\nt\3u\3u\5u\u0525\nu\3u\3u\3u\5u\u052a\nu\7u\u052c"+
		"\nu\fu\16u\u052f\13u\3v\3v\3v\7v\u0534\nv\fv\16v\u0537\13v\3v\3v\3w\3"+
		"w\3w\5w\u053e\nw\3x\3x\3x\5x\u0543\nx\3x\5x\u0546\nx\3y\3y\3y\3y\3y\3"+
		"y\5y\u054e\ny\3y\3y\3z\3z\3z\3z\5z\u0556\nz\3z\3z\3{\3{\5{\u055c\n{\3"+
		"{\3{\5{\u0560\n{\3|\3|\3|\3|\3|\3|\5|\u0568\n|\3|\3|\3|\3}\3}\3}\3~\3"+
		"~\5~\u0572\n~\3\177\3\177\3\177\3\177\3\177\3\u0080\3\u0080\3\u0081\3"+
		"\u0081\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\3\u0083\3\u0084"+
		"\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\5\u0085\u058f\n\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086"+
		"\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\5\u0087\u059b\n\u0087\3\u0088"+
		"\3\u0088\5\u0088\u059f\n\u0088\3\u0089\3\u0089\3\u0089\3\u0089\7\u0089"+
		"\u05a5\n\u0089\f\u0089\16\u0089\u05a8\13\u0089\3\u0089\5\u0089\u05ab\n"+
		"\u0089\5\u0089\u05ad\n\u0089\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3"+
		"\u008a\5\u008a\u05b5\n\u008a\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3"+
		"\u008b\3\u008b\3\u008b\5\u008b\u05bf\n\u008b\3\u008c\3\u008c\3\u008c\5"+
		"\u008c\u05c4\n\u008c\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e\5\u008e\u05cb"+
		"\n\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3\u0090\3\u0090\3\u0090\3\u0091"+
		"\3\u0091\3\u0091\5\u0091\u05d7\n\u0091\5\u0091\u05d9\n\u0091\3\u0091\3"+
		"\u0091\3\u0092\3\u0092\3\u0092\7\u0092\u05e0\n\u0092\f\u0092\16\u0092"+
		"\u05e3\13\u0092\3\u0093\3\u0093\3\u0093\5\u0093\u05e8\n\u0093\3\u0093"+
		"\3\u0093\3\u0094\3\u0094\3\u0094\5\u0094\u05ef\n\u0094\3\u0095\3\u0095"+
		"\5\u0095\u05f3\n\u0095\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096\7\u0096"+
		"\u05fa\n\u0096\f\u0096\16\u0096\u05fd\13\u0096\3\u0096\3\u0096\3\u0097"+
		"\3\u0097\3\u0097\3\u0097\3\u0097\5\u0097\u0606\n\u0097\3\u0097\5\u0097"+
		"\u0609\n\u0097\3\u0098\3\u0098\3\u0099\5\u0099\u060e\n\u0099\3\u0099\3"+
		"\u0099\3\u009a\3\u009a\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\3\u009b"+
		"\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009d\3\u009d\3\u009d\3\u009d"+
		"\3\u009d\5\u009d\u0624\n\u009d\5\u009d\u0626\n\u009d\3\u009d\5\u009d\u0629"+
		"\n\u009d\3\u009d\5\u009d\u062c\n\u009d\5\u009d\u062e\n\u009d\3\u009d\3"+
		"\u009d\3\u009e\3\u009e\3\u009e\3\u009e\3\u009f\3\u009f\3\u009f\2\4p~\u00a0"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFH"+
		"JLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4"+
		"\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc"+
		"\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4"+
		"\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec"+
		"\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104"+
		"\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c"+
		"\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134"+
		"\u0136\u0138\u013a\u013c\2\25\3\2\27\30\3\2\7\n\3\2\24\25\3\2?@\3\2(*"+
		"\4\2(*,,\6\2\'\'--\60\60\63\63\3\2\u008d\u0093\4\2\u0088\u008c\u0091\u0092"+
		"\6\2\"\"{{\u0087\u0087\u008e\u0090\3\2\36 \3\2\33\35\3\2\u0081\u0086\3"+
		"\2BU\4\2\u0087\u008c\u008e\u0092\4\2oozz\3\2{|\4\2\u0094\u0097\u0099\u009a"+
		"\3\2\u00a0\u00a1\2\u069f\2\u013e\3\2\2\2\4\u0141\3\2\2\2\6\u0144\3\2\2"+
		"\2\b\u0147\3\2\2\2\n\u014f\3\2\2\2\f\u015b\3\2\2\2\16\u016e\3\2\2\2\20"+
		"\u0170\3\2\2\2\22\u017b\3\2\2\2\24\u0188\3\2\2\2\26\u018b\3\2\2\2\30\u0196"+
		"\3\2\2\2\32\u0198\3\2\2\2\34\u019d\3\2\2\2\36\u01a2\3\2\2\2 \u01a7\3\2"+
		"\2\2\"\u01ac\3\2\2\2$\u01b9\3\2\2\2&\u01bb\3\2\2\2(\u01bd\3\2\2\2*\u01c2"+
		"\3\2\2\2,\u01c7\3\2\2\2.\u01d3\3\2\2\2\60\u01da\3\2\2\2\62\u01e5\3\2\2"+
		"\2\64\u01ec\3\2\2\2\66\u01ee\3\2\2\28\u0203\3\2\2\2:\u0205\3\2\2\2<\u0215"+
		"\3\2\2\2>\u022b\3\2\2\2@\u0235\3\2\2\2B\u0238\3\2\2\2D\u0240\3\2\2\2F"+
		"\u0242\3\2\2\2H\u024e\3\2\2\2J\u0266\3\2\2\2L\u026e\3\2\2\2N\u027d\3\2"+
		"\2\2P\u027f\3\2\2\2R\u0286\3\2\2\2T\u028f\3\2\2\2V\u0294\3\2\2\2X\u029b"+
		"\3\2\2\2Z\u02a3\3\2\2\2\\\u02cb\3\2\2\2^\u02cd\3\2\2\2`\u02d3\3\2\2\2"+
		"b\u02d8\3\2\2\2d\u02df\3\2\2\2f\u02e9\3\2\2\2h\u02ed\3\2\2\2j\u02f8\3"+
		"\2\2\2l\u030b\3\2\2\2n\u030d\3\2\2\2p\u0322\3\2\2\2r\u0359\3\2\2\2t\u035b"+
		"\3\2\2\2v\u035e\3\2\2\2x\u0363\3\2\2\2z\u036c\3\2\2\2|\u037d\3\2\2\2~"+
		"\u0385\3\2\2\2\u0080\u0398\3\2\2\2\u0082\u03a1\3\2\2\2\u0084\u03b1\3\2"+
		"\2\2\u0086\u03c5\3\2\2\2\u0088\u03cf\3\2\2\2\u008a\u03da\3\2\2\2\u008c"+
		"\u03dc\3\2\2\2\u008e\u03df\3\2\2\2\u0090\u03f9\3\2\2\2\u0092\u03fb\3\2"+
		"\2\2\u0094\u040f\3\2\2\2\u0096\u0411\3\2\2\2\u0098\u0413\3\2\2\2\u009a"+
		"\u0416\3\2\2\2\u009c\u041f\3\2\2\2\u009e\u0421\3\2\2\2\u00a0\u0424\3\2"+
		"\2\2\u00a2\u0433\3\2\2\2\u00a4\u0437\3\2\2\2\u00a6\u043c\3\2\2\2\u00a8"+
		"\u043e\3\2\2\2\u00aa\u044c\3\2\2\2\u00ac\u0454\3\2\2\2\u00ae\u045c\3\2"+
		"\2\2\u00b0\u0464\3\2\2\2\u00b2\u0472\3\2\2\2\u00b4\u0478\3\2\2\2\u00b6"+
		"\u0486\3\2\2\2\u00b8\u048f\3\2\2\2\u00ba\u0499\3\2\2\2\u00bc\u04ab\3\2"+
		"\2\2\u00be\u04ad\3\2\2\2\u00c0\u04af\3\2\2\2\u00c2\u04b3\3\2\2\2\u00c4"+
		"\u04b6\3\2\2\2\u00c6\u04ba\3\2\2\2\u00c8\u04bc\3\2\2\2\u00ca\u04c1\3\2"+
		"\2\2\u00cc\u04c5\3\2\2\2\u00ce\u04c9\3\2\2\2\u00d0\u04cd\3\2\2\2\u00d2"+
		"\u04d0\3\2\2\2\u00d4\u04d2\3\2\2\2\u00d6\u04d5\3\2\2\2\u00d8\u04e4\3\2"+
		"\2\2\u00da\u04e6\3\2\2\2\u00dc\u04f6\3\2\2\2\u00de\u04fe\3\2\2\2\u00e0"+
		"\u0500\3\2\2\2\u00e2\u0510\3\2\2\2\u00e4\u0518\3\2\2\2\u00e6\u0520\3\2"+
		"\2\2\u00e8\u0524\3\2\2\2\u00ea\u0530\3\2\2\2\u00ec\u053a\3\2\2\2\u00ee"+
		"\u0545\3\2\2\2\u00f0\u054d\3\2\2\2\u00f2\u0551\3\2\2\2\u00f4\u0559\3\2"+
		"\2\2\u00f6\u0567\3\2\2\2\u00f8\u056c\3\2\2\2\u00fa\u0571\3\2\2\2\u00fc"+
		"\u0573\3\2\2\2\u00fe\u0578\3\2\2\2\u0100\u057a\3\2\2\2\u0102\u057c\3\2"+
		"\2\2\u0104\u057f\3\2\2\2\u0106\u0583\3\2\2\2\u0108\u058e\3\2\2\2\u010a"+
		"\u0592\3\2\2\2\u010c\u059a\3\2\2\2\u010e\u059e\3\2\2\2\u0110\u05a0\3\2"+
		"\2\2\u0112\u05b0\3\2\2\2\u0114\u05be\3\2\2\2\u0116\u05c3\3\2\2\2\u0118"+
		"\u05c5\3\2\2\2\u011a\u05c7\3\2\2\2\u011c\u05cc\3\2\2\2\u011e\u05d0\3\2"+
		"\2\2\u0120\u05d3\3\2\2\2\u0122\u05dc\3\2\2\2\u0124\u05e7\3\2\2\2\u0126"+
		"\u05ee\3\2\2\2\u0128\u05f2\3\2\2\2\u012a\u05f4\3\2\2\2\u012c\u0605\3\2"+
		"\2\2\u012e\u060a\3\2\2\2\u0130\u060d\3\2\2\2\u0132\u0611\3\2\2\2\u0134"+
		"\u0615\3\2\2\2\u0136\u0619\3\2\2\2\u0138\u061e\3\2\2\2\u013a\u0631\3\2"+
		"\2\2\u013c\u0635\3\2\2\2\u013e\u013f\5p9\2\u013f\u0140\7\2\2\3\u0140\3"+
		"\3\2\2\2\u0141\u0142\5r:\2\u0142\u0143\7\2\2\3\u0143\5\3\2\2\2\u0144\u0145"+
		"\5\u0088E\2\u0145\u0146\7\2\2\3\u0146\7\3\2\2\2\u0147\u014c\5\n\6\2\u0148"+
		"\u0149\7w\2\2\u0149\u014b\5\n\6\2\u014a\u0148\3\2\2\2\u014b\u014e\3\2"+
		"\2\2\u014c\u014a\3\2\2\2\u014c\u014d\3\2\2\2\u014d\t\3\2\2\2\u014e\u014c"+
		"\3\2\2\2\u014f\u0151\7o\2\2\u0150\u0152\7:\2\2\u0151\u0150\3\2\2\2\u0151"+
		"\u0152\3\2\2\2\u0152\13\3\2\2\2\u0153\u0154\7\32\2\2\u0154\u015c\5r:\2"+
		"\u0155\u0156\7\7\2\2\u0156\u015c\5p9\2\u0157\u0158\t\2\2\2\u0158\u015c"+
		"\5\30\r\2\u0159\u015a\t\3\2\2\u015a\u015c\5p9\2\u015b\u0153\3\2\2\2\u015b"+
		"\u0155\3\2\2\2\u015b\u0157\3\2\2\2\u015b\u0159\3\2\2\2\u015c\r\3\2\2\2"+
		"\u015d\u016f\5.\30\2\u015e\u016f\5,\27\2\u015f\u016f\5*\26\2\u0160\u016f"+
		"\5(\25\2\u0161\u016f\5\"\22\2\u0162\u016f\5 \21\2\u0163\u016f\5\34\17"+
		"\2\u0164\u016f\5\32\16\2\u0165\u016f\5\36\20\2\u0166\u0167\t\4\2\2\u0167"+
		"\u0168\5\20\t\2\u0168\u0169\7y\2\2\u0169\u016a\7y\2\2\u016a\u016b\5\24"+
		"\13\2\u016b\u016c\5p9\2\u016c\u016f\3\2\2\2\u016d\u016f\t\5\2\2\u016e"+
		"\u015d\3\2\2\2\u016e\u015e\3\2\2\2\u016e\u015f\3\2\2\2\u016e\u0160\3\2"+
		"\2\2\u016e\u0161\3\2\2\2\u016e\u0162\3\2\2\2\u016e\u0163\3\2\2\2\u016e"+
		"\u0164\3\2\2\2\u016e\u0165\3\2\2\2\u016e\u0166\3\2\2\2\u016e\u016d\3\2"+
		"\2\2\u016f\17\3\2\2\2\u0170\u0175\5\22\n\2\u0171\u0172\7w\2\2\u0172\u0174"+
		"\5\22\n\2\u0173\u0171\3\2\2\2\u0174\u0177\3\2\2\2\u0175\u0173\3\2\2\2"+
		"\u0175\u0176\3\2\2\2\u0176\u0179\3\2\2\2\u0177\u0175\3\2\2\2\u0178\u017a"+
		"\7w\2\2\u0179\u0178\3\2\2\2\u0179\u017a\3\2\2\2\u017a\21\3\2\2\2\u017b"+
		"\u0180\7o\2\2\u017c\u017d\7w\2\2\u017d\u017f\7o\2\2\u017e\u017c\3\2\2"+
		"\2\u017f\u0182\3\2\2\2\u0180\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0183"+
		"\3\2\2\2\u0182\u0180\3\2\2\2\u0183\u0184\5\u0100\u0081\2\u0184\23\3\2"+
		"\2\2\u0185\u0187\5\26\f\2\u0186\u0185\3\2\2\2\u0187\u018a\3\2\2\2\u0188"+
		"\u0186\3\2\2\2\u0188\u0189\3\2\2\2\u0189\25\3\2\2\2\u018a\u0188\3\2\2"+
		"\2\u018b\u018c\7r\2\2\u018c\u0191\5p9\2\u018d\u018e\7w\2\2\u018e\u0190"+
		"\5p9\2\u018f\u018d\3\2\2\2\u0190\u0193\3\2\2\2\u0191\u018f\3\2\2\2\u0191"+
		"\u0192\3\2\2\2\u0192\u0194\3\2\2\2\u0193\u0191\3\2\2\2\u0194\u0195\7s"+
		"\2\2\u0195\27\3\2\2\2\u0196\u0197\5~@\2\u0197\31\3\2\2\2\u0198\u0199\7"+
		"\61\2\2\u0199\u019a\7p\2\2\u019a\u019b\5p9\2\u019b\u019c\7q\2\2\u019c"+
		"\33\3\2\2\2\u019d\u019e\7\65\2\2\u019e\u019f\7t\2\2\u019f\u01a0\5\u0088"+
		"E\2\u01a0\u01a1\7u\2\2\u01a1\35\3\2\2\2\u01a2\u01a3\7\62\2\2\u01a3\u01a4"+
		"\7p\2\2\u01a4\u01a5\5p9\2\u01a5\u01a6\7q\2\2\u01a6\37\3\2\2\2\u01a7\u01a8"+
		"\t\6\2\2\u01a8\u01a9\7p\2\2\u01a9\u01aa\5p9\2\u01aa\u01ab\7q\2\2\u01ab"+
		"!\3\2\2\2\u01ac\u01b1\7\22\2\2\u01ad\u01ae\7t\2\2\u01ae\u01af\5$\23\2"+
		"\u01af\u01b0\7u\2\2\u01b0\u01b2\3\2\2\2\u01b1\u01ad\3\2\2\2\u01b1\u01b2"+
		"\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b4\7p\2\2\u01b4\u01b5\5p9\2\u01b5"+
		"\u01b6\7q\2\2\u01b6#\3\2\2\2\u01b7\u01ba\5&\24\2\u01b8\u01ba\7\23\2\2"+
		"\u01b9\u01b7\3\2\2\2\u01b9\u01b8\3\2\2\2\u01ba%\3\2\2\2\u01bb\u01bc\7"+
		"o\2\2\u01bc\'\3\2\2\2\u01bd\u01be\78\2\2\u01be\u01bf\7p\2\2\u01bf\u01c0"+
		"\5p9\2\u01c0\u01c1\7q\2\2\u01c1)\3\2\2\2\u01c2\u01c3\7\67\2\2\u01c3\u01c4"+
		"\7p\2\2\u01c4\u01c5\5p9\2\u01c5\u01c6\7q\2\2\u01c6+\3\2\2\2\u01c7\u01c8"+
		"\7\26\2\2\u01c8\u01c9\7p\2\2\u01c9\u01cf\5p9\2\u01ca\u01cd\7w\2\2\u01cb"+
		"\u01ce\7o\2\2\u01cc\u01ce\5p9\2\u01cd\u01cb\3\2\2\2\u01cd\u01cc\3\2\2"+
		"\2\u01ce\u01d0\3\2\2\2\u01cf\u01ca\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d1"+
		"\3\2\2\2\u01d1\u01d2\7q\2\2\u01d2-\3\2\2\2\u01d3\u01d4\t\6\2\2\u01d4\u01d5"+
		"\7t\2\2\u01d5\u01d6\5p9\2\u01d6\u01d7\7;\2\2\u01d7\u01d8\5p9\2\u01d8\u01d9"+
		"\7u\2\2\u01d9/\3\2\2\2\u01da\u01db\7t\2\2\u01db\u01e0\5\62\32\2\u01dc"+
		"\u01dd\7w\2\2\u01dd\u01df\5\62\32\2\u01de\u01dc\3\2\2\2\u01df\u01e2\3"+
		"\2\2\2\u01e0\u01de\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e3\3\2\2\2\u01e2"+
		"\u01e0\3\2\2\2\u01e3\u01e4\7u\2\2\u01e4\61\3\2\2\2\u01e5\u01e6\5p9\2\u01e6"+
		"\u01e7\7v\2\2\u01e7\u01e8\5p9\2\u01e8\63\3\2\2\2\u01e9\u01ed\5<\37\2\u01ea"+
		"\u01ed\5:\36\2\u01eb\u01ed\5\66\34\2\u01ec\u01e9\3\2\2\2\u01ec\u01ea\3"+
		"\2\2\2\u01ec\u01eb\3\2\2\2\u01ed\65\3\2\2\2\u01ee\u01ef\7\63\2\2\u01ef"+
		"\u01f5\7r\2\2\u01f0\u01f1\58\35\2\u01f1\u01f2\5\u009cO\2\u01f2\u01f4\3"+
		"\2\2\2\u01f3\u01f0\3\2\2\2\u01f4\u01f7\3\2\2\2\u01f5\u01f3\3\2\2\2\u01f5"+
		"\u01f6\3\2\2\2\u01f6\u01f8\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f8\u01f9\7s"+
		"\2\2\u01f9\67\3\2\2\2\u01fa\u01fb\7X\2\2\u01fb\u01fc\7o\2\2\u01fc\u0204"+
		"\5\u010c\u0087\2\u01fd\u01fe\7\64\2\2\u01fe\u01ff\7r\2\2\u01ff\u0200\5"+
		"p9\2\u0200\u0201\5\u009cO\2\u0201\u0202\7s\2\2\u0202\u0204\3\2\2\2\u0203"+
		"\u01fa\3\2\2\2\u0203\u01fd\3\2\2\2\u02049\3\2\2\2\u0205\u0206\7\32\2\2"+
		"\u0206\u0207\7t\2\2\u0207\u0208\7u\2\2\u0208\u0209\5\u0100\u0081\2\u0209"+
		";\3\2\2\2\u020a\u020b\t\7\2\2\u020b\u020c\7t\2\2\u020c\u020d\5\u0088E"+
		"\2\u020d\u020e\7u\2\2\u020e\u0216\3\2\2\2\u020f\u0210\7+\2\2\u0210\u0211"+
		"\7t\2\2\u0211\u0212\5\u0088E\2\u0212\u0213\7u\2\2\u0213\u0214\5\u0088"+
		"E\2\u0214\u0216\3\2\2\2\u0215\u020a\3\2\2\2\u0215\u020f\3\2\2\2\u0216"+
		"=\3\2\2\2\u0217\u0218\5@!\2\u0218\u0219\5\u009cO\2\u0219\u021b\3\2\2\2"+
		"\u021a\u0217\3\2\2\2\u021b\u021e\3\2\2\2\u021c\u021a\3\2\2\2\u021c\u021d"+
		"\3\2\2\2\u021d\u021f\3\2\2\2\u021e\u021c\3\2\2\2\u021f\u022c\7\20\2\2"+
		"\u0220\u0224\5@!\2\u0221\u0224\7\20\2\2\u0222\u0224\7A\2\2\u0223\u0220"+
		"\3\2\2\2\u0223\u0221\3\2\2\2\u0223\u0222\3\2\2\2\u0224\u0225\3\2\2\2\u0225"+
		"\u0227\5\u009cO\2\u0226\u0223\3\2\2\2\u0227\u022a\3\2\2\2\u0228\u0226"+
		"\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u022c\3\2\2\2\u022a\u0228\3\2\2\2\u022b"+
		"\u021c\3\2\2\2\u022b\u0228\3\2\2\2\u022c?\3\2\2\2\u022d\u022e\7\13\2\2"+
		"\u022e\u0236\5D#\2\u022f\u0230\7\f\2\2\u0230\u0236\5D#\2\u0231\u0232\7"+
		"\r\2\2\u0232\u0236\5D#\2\u0233\u0234\7\17\2\2\u0234\u0236\5B\"\2\u0235"+
		"\u022d\3\2\2\2\u0235\u022f\3\2\2\2\u0235\u0231\3\2\2\2\u0235\u0233\3\2"+
		"\2\2\u0236A\3\2\2\2\u0237\u0239\5\u00aeX\2\u0238\u0237\3\2\2\2\u0238\u0239"+
		"\3\2\2\2\u0239\u023c\3\2\2\2\u023a\u023b\7g\2\2\u023b\u023d\5p9\2\u023c"+
		"\u023a\3\2\2\2\u023c\u023d\3\2\2\2\u023dC\3\2\2\2\u023e\u0241\3\2\2\2"+
		"\u023f\u0241\5p9\2\u0240\u023e\3\2\2\2\u0240\u023f\3\2\2\2\u0241E\3\2"+
		"\2\2\u0242\u0247\7r\2\2\u0243\u0244\79\2\2\u0244\u0245\5\u00acW\2\u0245"+
		"\u0246\5\u009cO\2\u0246\u0248\3\2\2\2\u0247\u0243\3\2\2\2\u0247\u0248"+
		"\3\2\2\2\u0248\u024a\3\2\2\2\u0249\u024b\5\u00b8]\2\u024a\u0249\3\2\2"+
		"\2\u024a\u024b\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u024d\7s\2\2\u024dG\3"+
		"\2\2\2\u024e\u024f\5\u0088E\2\u024f\u0250\7\21\2\2\u0250\u0263\5\u0088"+
		"E\2\u0251\u0257\7r\2\2\u0252\u0253\5P)\2\u0253\u0254\5\u009cO\2\u0254"+
		"\u0256\3\2\2\2\u0255\u0252\3\2\2\2\u0256\u0259\3\2\2\2\u0257\u0255\3\2"+
		"\2\2\u0257\u0258\3\2\2\2\u0258\u025f\3\2\2\2\u0259\u0257\3\2\2\2\u025a"+
		"\u025b\5J&\2\u025b\u025c\5\u009cO\2\u025c\u025e\3\2\2\2\u025d\u025a\3"+
		"\2\2\2\u025e\u0261\3\2\2\2\u025f\u025d\3\2\2\2\u025f\u0260\3\2\2\2\u0260"+
		"\u0262\3\2\2\2\u0261\u025f\3\2\2\2\u0262\u0264\7s\2\2\u0263\u0251\3\2"+
		"\2\2\u0263\u0264\3\2\2\2\u0264I\3\2\2\2\u0265\u0267\7\20\2\2\u0266\u0265"+
		"\3\2\2\2\u0266\u0267\3\2\2\2\u0267\u0268\3\2\2\2\u0268\u0269\5L\'\2\u0269"+
		"\u026a\7o\2\2\u026a\u026c\5\u010c\u0087\2\u026b\u026d\5\u00b6\\\2\u026c"+
		"\u026b\3\2\2\2\u026c\u026d\3\2\2\2\u026dK\3\2\2\2\u026e\u0270\7p\2\2\u026f"+
		"\u0271\7o\2\2\u0270\u026f\3\2\2\2\u0270\u0271\3\2\2\2\u0271\u0273\3\2"+
		"\2\2\u0272\u0274\7\u0091\2\2\u0273\u0272\3\2\2\2\u0273\u0274\3\2\2\2\u0274"+
		"\u0275\3\2\2\2\u0275\u0276\5\u00fa~\2\u0276\u0277\7q\2\2\u0277M\3\2\2"+
		"\2\u0278\u027e\5~@\2\u0279\u027a\5\u0088E\2\u027a\u027b\7z\2\2\u027b\u027c"+
		"\7o\2\2\u027c\u027e\3\2\2\2\u027d\u0278\3\2\2\2\u027d\u0279\3\2\2\2\u027e"+
		"O\3\2\2\2\u027f\u0280\7\66\2\2\u0280\u0281\7o\2\2\u0281\u0284\7}\2\2\u0282"+
		"\u0285\5N(\2\u0283\u0285\5\u011a\u008e\2\u0284\u0282\3\2\2\2\u0284\u0283"+
		"\3\2\2\2\u0285Q\3\2\2\2\u0286\u0287\7/\2\2\u0287\u0288\7p\2\2\u0288\u028b"+
		"\5\u0088E\2\u0289\u028a\7w\2\2\u028a\u028c\5\u00aeX\2\u028b\u0289\3\2"+
		"\2\2\u028b\u028c\3\2\2\2\u028c\u028d\3\2\2\2\u028d\u028e\7q\2\2\u028e"+
		"S\3\2\2\2\u028f\u0290\7.\2\2\u0290\u0291\7p\2\2\u0291\u0292\5\u0088E\2"+
		"\u0292\u0293\7q\2\2\u0293U\3\2\2\2\u0294\u0295\5> \2\u0295\u0296\7X\2"+
		"\2\u0296\u0297\7o\2\2\u0297\u0299\5\u010c\u0087\2\u0298\u029a\5F$\2\u0299"+
		"\u0298\3\2\2\2\u0299\u029a\3\2\2\2\u029aW\3\2\2\2\u029b\u029c\5> \2\u029c"+
		"\u029d\7X\2\2\u029d\u029e\5h\65\2\u029e\u029f\7o\2\2\u029f\u02a1\5\u010c"+
		"\u0087\2\u02a0\u02a2\5F$\2\u02a1\u02a0\3\2\2\2\u02a1\u02a2\3\2\2\2\u02a2"+
		"Y\3\2\2\2\u02a3\u02a4\5\u009eP\2\u02a4\u02aa\5\u009cO\2\u02a5\u02a6\5"+
		"\u00a0Q\2\u02a6\u02a7\5\u009cO\2\u02a7\u02a9\3\2\2\2\u02a8\u02a5\3\2\2"+
		"\2\u02a9\u02ac\3\2\2\2\u02aa\u02a8\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02b7"+
		"\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ad\u02b2\5V,\2\u02ae\u02b2\5X-\2\u02af"+
		"\u02b2\5\u00a6T\2\u02b0\u02b2\5\\/\2\u02b1\u02ad\3\2\2\2\u02b1\u02ae\3"+
		"\2\2\2\u02b1\u02af\3\2\2\2\u02b1\u02b0\3\2\2\2\u02b2\u02b3\3\2\2\2\u02b3"+
		"\u02b4\5\u009cO\2\u02b4\u02b6\3\2\2\2\u02b5\u02b1\3\2\2\2\u02b6\u02b9"+
		"\3\2\2\2\u02b7\u02b5\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02ba\3\2\2\2\u02b9"+
		"\u02b7\3\2\2\2\u02ba\u02bb\7\2\2\3\u02bb[\3\2\2\2\u02bc\u02cc\5H%\2\u02bd"+
		"\u02cc\5^\60\2\u02be\u02cc\5b\62\2\u02bf\u02c3\7\32\2\2\u02c0\u02c1\7"+
		"\32\2\2\u02c1\u02c3\5\u009cO\2\u02c2\u02bf\3\2\2\2\u02c2\u02c0\3\2\2\2"+
		"\u02c3\u02c9\3\2\2\2\u02c4\u02ca\5X-\2\u02c5\u02ca\5V,\2\u02c6\u02ca\5"+
		"\u00a8U\2\u02c7\u02ca\5\u00b0Y\2\u02c8\u02ca\5\u00b4[\2\u02c9\u02c4\3"+
		"\2\2\2\u02c9\u02c5\3\2\2\2\u02c9\u02c6\3\2\2\2\u02c9\u02c7\3\2\2\2\u02c9"+
		"\u02c8\3\2\2\2\u02ca\u02cc\3\2\2\2\u02cb\u02bc\3\2\2\2\u02cb\u02bd\3\2"+
		"\2\2\u02cb\u02be\3\2\2\2\u02cb\u02c2\3\2\2\2\u02cc]\3\2\2\2\u02cd\u02ce"+
		"\7\66\2\2\u02ce\u02cf\7o\2\2\u02cf\u02d1\5\u0110\u0089\2\u02d0\u02d2\5"+
		"`\61\2\u02d1\u02d0\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2_\3\2\2\2\u02d3\u02d4"+
		"\7r\2\2\u02d4\u02d5\5p9\2\u02d5\u02d6\5\u009cO\2\u02d6\u02d7\7s\2\2\u02d7"+
		"a\3\2\2\2\u02d8\u02d9\7\66\2\2\u02d9\u02da\5h\65\2\u02da\u02db\7o\2\2"+
		"\u02db\u02dd\5\u0110\u0089\2\u02dc\u02de\5`\61\2\u02dd\u02dc\3\2\2\2\u02dd"+
		"\u02de\3\2\2\2\u02dec\3\2\2\2\u02df\u02e7\5\b\5\2\u02e0\u02e3\5\u0088"+
		"E\2\u02e1\u02e2\7v\2\2\u02e2\u02e4\5\u00aeX\2\u02e3\u02e1\3\2\2\2\u02e3"+
		"\u02e4\3\2\2\2\u02e4\u02e8\3\2\2\2\u02e5\u02e6\7v\2\2\u02e6\u02e8\5\u00ae"+
		"X\2\u02e7\u02e0\3\2\2\2\u02e7\u02e5\3\2\2\2\u02e8e\3\2\2\2\u02e9\u02ea"+
		"\5\b\5\2\u02ea\u02eb\7}\2\2\u02eb\u02ec\5\u00aeX\2\u02ecg\3\2\2\2\u02ed"+
		"\u02ef\7p\2\2\u02ee\u02f0\5\n\6\2\u02ef\u02ee\3\2\2\2\u02ef\u02f0\3\2"+
		"\2\2\u02f0\u02f1\3\2\2\2\u02f1\u02f3\5\u0088E\2\u02f2\u02f4\7w\2\2\u02f3"+
		"\u02f2\3\2\2\2\u02f3\u02f4\3\2\2\2\u02f4\u02f5\3\2\2\2\u02f5\u02f6\7q"+
		"\2\2\u02f6i\3\2\2\2\u02f7\u02f9\7\32\2\2\u02f8\u02f7\3\2\2\2\u02f8\u02f9"+
		"\3\2\2\2\u02f9\u02fb\3\2\2\2\u02fa\u02fc\5\u00acW\2\u02fb\u02fa\3\2\2"+
		"\2\u02fb\u02fc\3\2\2\2\u02fc\u02fe\3\2\2\2\u02fd\u02ff\7~\2\2\u02fe\u02fd"+
		"\3\2\2\2\u02fe\u02ff\3\2\2\2\u02ff\u0300\3\2\2\2\u0300\u0301\5\u0088E"+
		"\2\u0301k\3\2\2\2\u0302\u030c\5~@\2\u0303\u0304\t\b\2\2\u0304\u0305\7"+
		"p\2\2\u0305\u0306\5p9\2\u0306\u0307\7q\2\2\u0307\u030c\3\2\2\2\u0308\u030c"+
		"\5n8\2\u0309\u030a\t\t\2\2\u030a\u030c\5p9\2\u030b\u0302\3\2\2\2\u030b"+
		"\u0303\3\2\2\2\u030b\u0308\3\2\2\2\u030b\u0309\3\2\2\2\u030cm\3\2\2\2"+
		"\u030d\u030e\7\31\2\2\u030e\u030f\5\30\r\2\u030f\u0310\7\33\2\2\u0310"+
		"\u0311\5p9\2\u0311o\3\2\2\2\u0312\u0313\b9\1\2\u0313\u0314\7h\2\2\u0314"+
		"\u0315\7t\2\2\u0315\u0316\5\u0088E\2\u0316\u0317\7u\2\2\u0317\u0323\3"+
		"\2\2\2\u0318\u0319\t\b\2\2\u0319\u031a\7p\2\2\u031a\u031b\5p9\2\u031b"+
		"\u031c\7q\2\2\u031c\u0323\3\2\2\2\u031d\u0323\5n8\2\u031e\u0323\5R*\2"+
		"\u031f\u0320\t\t\2\2\u0320\u0323\5p9\r\u0321\u0323\5~@\2\u0322\u0312\3"+
		"\2\2\2\u0322\u0318\3\2\2\2\u0322\u031d\3\2\2\2\u0322\u031e\3\2\2\2\u0322"+
		"\u031f\3\2\2\2\u0322\u0321\3\2\2\2\u0323\u0344\3\2\2\2\u0324\u0325\f\13"+
		"\2\2\u0325\u0326\t\n\2\2\u0326\u0343\5p9\f\u0327\u0328\f\n\2\2\u0328\u0329"+
		"\t\13\2\2\u0329\u0343\5p9\13\u032a\u032b\f\t\2\2\u032b\u032c\t\f\2\2\u032c"+
		"\u0343\5p9\n\u032d\u032e\f\b\2\2\u032e\u032f\t\r\2\2\u032f\u0343\5p9\t"+
		"\u0330\u0331\f\7\2\2\u0331\u0332\t\16\2\2\u0332\u0343\5p9\b\u0333\u0334"+
		"\f\6\2\2\u0334\u0335\7\u0080\2\2\u0335\u0343\5p9\7\u0336\u0337\f\5\2\2"+
		"\u0337\u0338\7\177\2\2\u0338\u0343\5p9\6\u0339\u033a\f\4\2\2\u033a\u033b"+
		"\7!\2\2\u033b\u0343\5p9\4\u033c\u033d\f\3\2\2\u033d\u033e\7$\2\2\u033e"+
		"\u033f\5p9\2\u033f\u0340\7y\2\2\u0340\u0341\5p9\3\u0341\u0343\3\2\2\2"+
		"\u0342\u0324\3\2\2\2\u0342\u0327\3\2\2\2\u0342\u032a\3\2\2\2\u0342\u032d"+
		"\3\2\2\2\u0342\u0330\3\2\2\2\u0342\u0333\3\2\2\2\u0342\u0336\3\2\2\2\u0342"+
		"\u0339\3\2\2\2\u0342\u033c\3\2\2\2\u0343\u0346\3\2\2\2\u0344\u0342\3\2"+
		"\2\2\u0344\u0345\3\2\2\2\u0345q\3\2\2\2\u0346\u0344\3\2\2\2\u0347\u035a"+
		"\5\f\7\2\u0348\u035a\5v<\2\u0349\u035a\5t;\2\u034a\u035a\5\u00a6T\2\u034b"+
		"\u035a\5\u00c8e\2\u034c\u035a\5\u00ba^\2\u034d\u035a\5\u00f8}\2\u034e"+
		"\u035a\5\u00caf\2\u034f\u035a\5\u00ccg\2\u0350\u035a\5\u00ceh\2\u0351"+
		"\u035a\5\u00d0i\2\u0352\u035a\5\u00d2j\2\u0353\u035a\5\u00b6\\\2\u0354"+
		"\u035a\5\u00d6l\2\u0355\u035a\5\u00d8m\2\u0356\u035a\5\u00eav\2\u0357"+
		"\u035a\5x=\2\u0358\u035a\5\u00d4k\2\u0359\u0347\3\2\2\2\u0359\u0348\3"+
		"\2\2\2\u0359\u0349\3\2\2\2\u0359\u034a\3\2\2\2\u0359\u034b\3\2\2\2\u0359"+
		"\u034c\3\2\2\2\u0359\u034d\3\2\2\2\u0359\u034e\3\2\2\2\u0359\u034f\3\2"+
		"\2\2\u0359\u0350\3\2\2\2\u0359\u0351\3\2\2\2\u0359\u0352\3\2\2\2\u0359"+
		"\u0353\3\2\2\2\u0359\u0354\3\2\2\2\u0359\u0355\3\2\2\2\u0359\u0356\3\2"+
		"\2\2\u0359\u0357\3\2\2\2\u0359\u0358\3\2\2\2\u035as\3\2\2\2\u035b\u035c"+
		"\7#\2\2\u035c\u035d\5p9\2\u035du\3\2\2\2\u035e\u035f\7c\2\2\u035f\u0361"+
		"\5p9\2\u0360\u0362\5\u00b6\\\2\u0361\u0360\3\2\2\2\u0361\u0362\3\2\2\2"+
		"\u0362w\3\2\2\2\u0363\u0364\5z>\2\u0364\u0365\5\u00f2z\2\u0365y\3\2\2"+
		"\2\u0366\u0367\7\16\2\2\u0367\u0368\5p9\2\u0368\u0369\5\u009cO\2\u0369"+
		"\u036b\3\2\2\2\u036a\u0366\3\2\2\2\u036b\u036e\3\2\2\2\u036c\u036a\3\2"+
		"\2\2\u036c\u036d\3\2\2\2\u036d\u0373\3\2\2\2\u036e\u036c\3\2\2\2\u036f"+
		"\u0370\7\17\2\2\u0370\u0371\5B\"\2\u0371\u0372\5\u009cO\2\u0372\u0374"+
		"\3\2\2\2\u0373\u036f\3\2\2\2\u0373\u0374\3\2\2\2\u0374{\3\2\2\2\u0375"+
		"\u037e\7\5\2\2\u0376\u037e\7\6\2\2\u0377\u037e\7n\2\2\u0378\u037e\5\u0118"+
		"\u008d\2\u0379\u037e\5\u012e\u0098\2\u037a\u037e\7\3\2\2\u037b\u037e\7"+
		"\u0099\2\2\u037c\u037e\7\u009a\2\2\u037d\u0375\3\2\2\2\u037d\u0376\3\2"+
		"\2\2\u037d\u0377\3\2\2\2\u037d\u0378\3\2\2\2\u037d\u0379\3\2\2\2\u037d"+
		"\u037a\3\2\2\2\u037d\u037b\3\2\2\2\u037d\u037c\3\2\2\2\u037e}\3\2\2\2"+
		"\u037f\u0380\b@\1\2\u0380\u0386\5\u0114\u008b\2\u0381\u0386\5\u0112\u008a"+
		"\2\u0382\u0386\5\u013a\u009e\2\u0383\u0386\5\16\b\2\u0384\u0386\5T+\2"+
		"\u0385\u037f\3\2\2\2\u0385\u0381\3\2\2\2\u0385\u0382\3\2\2\2\u0385\u0383"+
		"\3\2\2\2\u0385\u0384\3\2\2\2\u0386\u0395\3\2\2\2\u0387\u0391\f\3\2\2\u0388"+
		"\u0389\7z\2\2\u0389\u0392\7o\2\2\u038a\u0392\5\u0134\u009b\2\u038b\u0392"+
		"\5\u0092J\2\u038c\u0392\5\60\31\2\u038d\u0392\5\u0136\u009c\2\u038e\u038f"+
		"\6@\f\2\u038f\u0392\5\u0138\u009d\2\u0390\u0392\5\u0080A\2\u0391\u0388"+
		"\3\2\2\2\u0391\u038a\3\2\2\2\u0391\u038b\3\2\2\2\u0391\u038c\3\2\2\2\u0391"+
		"\u038d\3\2\2\2\u0391\u038e\3\2\2\2\u0391\u0390\3\2\2\2\u0392\u0394\3\2"+
		"\2\2\u0393\u0387\3\2\2\2\u0394\u0397\3\2\2\2\u0395\u0393\3\2\2\2\u0395"+
		"\u0396\3\2\2\2\u0396\177\3\2\2\2\u0397\u0395\3\2\2\2\u0398\u039a\7%\2"+
		"\2\u0399\u039b\5\u00aeX\2\u039a\u0399\3\2\2\2\u039a\u039b\3\2\2\2\u039b"+
		"\u039d\3\2\2\2\u039c\u039e\7w\2\2\u039d\u039c\3\2\2\2\u039d\u039e\3\2"+
		"\2\2\u039e\u039f\3\2\2\2\u039f\u03a0\7&\2\2\u03a0\u0081\3\2\2\2\u03a1"+
		"\u03a2\7Y\2\2\u03a2\u03ac\7r\2\2\u03a3\u03a7\5\u0086D\2\u03a4\u03a7\5"+
		"\u00fa~\2\u03a5\u03a7\5\u0084C\2\u03a6\u03a3\3\2\2\2\u03a6\u03a4\3\2\2"+
		"\2\u03a6\u03a5\3\2\2\2\u03a7\u03a8\3\2\2\2\u03a8\u03a9\5\u009cO\2\u03a9"+
		"\u03ab\3\2\2\2\u03aa\u03a6\3\2\2\2\u03ab\u03ae\3\2\2\2\u03ac\u03aa\3\2"+
		"\2\2\u03ac\u03ad\3\2\2\2\u03ad\u03af\3\2\2\2\u03ae\u03ac\3\2\2\2\u03af"+
		"\u03b0\7s\2\2\u03b0\u0083\3\2\2\2\u03b1\u03b2\7\66\2\2\u03b2\u03b3\7o"+
		"\2\2\u03b3\u03b4\5\u0110\u0089\2\u03b4\u0085\3\2\2\2\u03b5\u03b7\6D\r"+
		"\2\u03b6\u03b8\7\32\2\2\u03b7\u03b6\3\2\2\2\u03b7\u03b8\3\2\2\2\u03b8"+
		"\u03b9\3\2\2\2\u03b9\u03ba\5> \2\u03ba\u03bb\7o\2\2\u03bb\u03bc\5\u0110"+
		"\u0089\2\u03bc\u03bd\5\u010e\u0088\2\u03bd\u03c6\3\2\2\2\u03be\u03c0\7"+
		"\32\2\2\u03bf\u03be\3\2\2\2\u03bf\u03c0\3\2\2\2\u03c0\u03c1\3\2\2\2\u03c1"+
		"\u03c2\5> \2\u03c2\u03c3\7o\2\2\u03c3\u03c4\5\u0110\u0089\2\u03c4\u03c6"+
		"\3\2\2\2\u03c5\u03b5\3\2\2\2\u03c5\u03bf\3\2\2\2\u03c6\u0087\3\2\2\2\u03c7"+
		"\u03d0\5\u00fa~\2\u03c8\u03d0\5\u008aF\2\u03c9\u03d0\5\64\33\2\u03ca\u03cb"+
		"\7p\2\2\u03cb\u03cc\5\u0088E\2\u03cc\u03cd\7q\2\2\u03cd\u03d0\3\2\2\2"+
		"\u03ce\u03d0\t\17\2\2\u03cf\u03c7\3\2\2\2\u03cf\u03c8\3\2\2\2\u03cf\u03c9"+
		"\3\2\2\2\u03cf\u03ca\3\2\2\2\u03cf\u03ce\3\2\2\2\u03d0\u0089\3\2\2\2\u03d1"+
		"\u03db\5\u00fc\177\2\u03d2\u03db\5\u012a\u0096\2\u03d3\u03db\5\u0102\u0082"+
		"\2\u03d4\u03db\5\u010a\u0086\2\u03d5\u03db\5\u0082B\2\u03d6\u03db\5\u0104"+
		"\u0083\2\u03d7\u03db\5\u0106\u0084\2\u03d8\u03db\5\u0108\u0085\2\u03d9"+
		"\u03db\5\u008cG\2\u03da\u03d1\3\2\2\2\u03da\u03d2\3\2\2\2\u03da\u03d3"+
		"\3\2\2\2\u03da\u03d4\3\2\2\2\u03da\u03d5\3\2\2\2\u03da\u03d6\3\2\2\2\u03da"+
		"\u03d7\3\2\2\2\u03da\u03d8\3\2\2\2\u03da\u03d9\3\2\2\2\u03db\u008b\3\2"+
		"\2\2\u03dc\u03dd\7\66\2\2\u03dd\u03de\5\u008eH\2\u03de\u008d\3\2\2\2\u03df"+
		"\u03eb\7p\2\2\u03e0\u03e5\5\u0088E\2\u03e1\u03e2\7w\2\2\u03e2\u03e4\5"+
		"\u0088E\2\u03e3\u03e1\3\2\2\2\u03e4\u03e7\3\2\2\2\u03e5\u03e3\3\2\2\2"+
		"\u03e5\u03e6\3\2\2\2\u03e6\u03e9\3\2\2\2\u03e7\u03e5\3\2\2\2\u03e8\u03ea"+
		"\7w\2\2\u03e9\u03e8\3\2\2\2\u03e9\u03ea\3\2\2\2\u03ea\u03ec\3\2\2\2\u03eb"+
		"\u03e0\3\2\2\2\u03eb\u03ec\3\2\2\2\u03ec\u03ed\3\2\2\2\u03ed\u03ee\7q"+
		"\2\2\u03ee\u008f\3\2\2\2\u03ef\u03fa\5\u012a\u0096\2\u03f0\u03fa\5\u00fc"+
		"\177\2\u03f1\u03f2\7t\2\2\u03f2\u03f3\7~\2\2\u03f3\u03f4\7u\2\2\u03f4"+
		"\u03fa\5\u0100\u0081\2\u03f5\u03fa\5\u0104\u0083\2\u03f6\u03fa\5\u0106"+
		"\u0084\2\u03f7\u03fa\5\64\33\2\u03f8\u03fa\5\u00fa~\2\u03f9\u03ef\3\2"+
		"\2\2\u03f9\u03f0\3\2\2\2\u03f9\u03f1\3\2\2\2\u03f9\u03f5\3\2\2\2\u03f9"+
		"\u03f6\3\2\2\2\u03f9\u03f7\3\2\2\2\u03f9\u03f8\3\2\2\2\u03fa\u0091\3\2"+
		"\2\2\u03fb\u040b\7t\2\2\u03fc\u03fe\5\u0094K\2\u03fd\u03fc\3\2\2\2\u03fd"+
		"\u03fe\3\2\2\2\u03fe\u03ff\3\2\2\2\u03ff\u0401\7y\2\2\u0400\u0402\5\u0096"+
		"L\2\u0401\u0400\3\2\2\2\u0401\u0402\3\2\2\2\u0402\u040c\3\2\2\2\u0403"+
		"\u0405\5\u0094K\2\u0404\u0403\3\2\2\2\u0404\u0405\3\2\2\2\u0405\u0406"+
		"\3\2\2\2\u0406\u0407\7y\2\2\u0407\u0408\5\u0096L\2\u0408\u0409\7y\2\2"+
		"\u0409\u040a\5\u0098M\2\u040a\u040c\3\2\2\2\u040b\u03fd\3\2\2\2\u040b"+
		"\u0404\3\2\2\2\u040c\u040d\3\2\2\2\u040d\u040e\7u\2\2\u040e\u0093\3\2"+
		"\2\2\u040f\u0410\5p9\2\u0410\u0095\3\2\2\2\u0411\u0412\5p9\2\u0412\u0097"+
		"\3\2\2\2\u0413\u0414\5p9\2\u0414\u0099\3\2\2\2\u0415\u0417\t\20\2\2\u0416"+
		"\u0415\3\2\2\2\u0416\u0417\3\2\2\2\u0417\u0418\3\2\2\2\u0418\u0419\7v"+
		"\2\2\u0419\u009b\3\2\2\2\u041a\u0420\7x\2\2\u041b\u0420\7\2\2\3\u041c"+
		"\u0420\6O\16\2\u041d\u0420\6O\17\2\u041e\u0420\6O\20\2\u041f\u041a\3\2"+
		"\2\2\u041f\u041b\3\2\2\2\u041f\u041c\3\2\2\2\u041f\u041d\3\2\2\2\u041f"+
		"\u041e\3\2\2\2\u0420\u009d\3\2\2\2\u0421\u0422\7c\2\2\u0422\u0423\7o\2"+
		"\2\u0423\u009f\3\2\2\2\u0424\u0430\7k\2\2\u0425\u0431\5\u00a2R\2\u0426"+
		"\u042c\7p\2\2\u0427\u0428\5\u00a2R\2\u0428\u0429\5\u009cO\2\u0429\u042b"+
		"\3\2\2\2\u042a\u0427\3\2\2\2\u042b\u042e\3\2\2\2\u042c\u042a\3\2\2\2\u042c"+
		"\u042d\3\2\2\2\u042d\u042f\3\2\2\2\u042e\u042c\3\2\2\2\u042f\u0431\7q"+
		"\2\2\u0430\u0425\3\2\2\2\u0430\u0426\3\2\2\2\u0431\u00a1\3\2\2\2\u0432"+
		"\u0434\t\21\2\2\u0433\u0432\3\2\2\2\u0433\u0434\3\2\2\2\u0434\u0435\3"+
		"\2\2\2\u0435\u0436\5\u00a4S\2\u0436\u00a3\3\2\2\2\u0437\u0438\5\u012e"+
		"\u0098\2\u0438\u00a5\3\2\2\2\u0439\u043d\5\u00a8U\2\u043a\u043d\5\u00b0"+
		"Y\2\u043b\u043d\5\u00b4[\2\u043c\u0439\3\2\2\2\u043c\u043a\3\2\2\2\u043c"+
		"\u043b\3\2\2\2\u043d\u00a7\3\2\2\2\u043e\u044a\7e\2\2\u043f\u044b\5\u00aa"+
		"V\2\u0440\u0446\7p\2\2\u0441\u0442\5\u00aaV\2\u0442\u0443\5\u009cO\2\u0443"+
		"\u0445\3\2\2\2\u0444\u0441\3\2\2\2\u0445\u0448\3\2\2\2\u0446\u0444\3\2"+
		"\2\2\u0446\u0447\3\2\2\2\u0447\u0449\3\2\2\2\u0448\u0446\3\2\2\2\u0449"+
		"\u044b\7q\2\2\u044a\u043f\3\2\2\2\u044a\u0440\3\2\2\2\u044b\u00a9\3\2"+
		"\2\2\u044c\u0452\5\u00acW\2\u044d\u044f\5\u0088E\2\u044e\u044d\3\2\2\2"+
		"\u044e\u044f\3\2\2\2\u044f\u0450\3\2\2\2\u0450\u0451\7v\2\2\u0451\u0453"+
		"\5\u00aeX\2\u0452\u044e\3\2\2\2\u0452\u0453\3\2\2\2\u0453\u00ab\3\2\2"+
		"\2\u0454\u0459\7o\2\2\u0455\u0456\7w\2\2\u0456\u0458\7o\2\2\u0457\u0455"+
		"\3\2\2\2\u0458\u045b\3\2\2\2\u0459\u0457\3\2\2\2\u0459\u045a\3\2\2\2\u045a"+
		"\u00ad\3\2\2\2\u045b\u0459\3\2\2\2\u045c\u0461\5p9\2\u045d\u045e\7w\2"+
		"\2\u045e\u0460\5p9\2\u045f\u045d\3\2\2\2\u0460\u0463\3\2\2\2\u0461\u045f"+
		"\3\2\2\2\u0461\u0462\3\2\2\2\u0462\u00af\3\2\2\2\u0463\u0461\3\2\2\2\u0464"+
		"\u0470\7h\2\2\u0465\u0471\5\u00b2Z\2\u0466\u046c\7p\2\2\u0467\u0468\5"+
		"\u00b2Z\2\u0468\u0469\5\u009cO\2\u0469\u046b\3\2\2\2\u046a\u0467\3\2\2"+
		"\2\u046b\u046e\3\2\2\2\u046c\u046a\3\2\2\2\u046c\u046d\3\2\2\2\u046d\u046f"+
		"\3\2\2\2\u046e\u046c\3\2\2\2\u046f\u0471\7q\2\2\u0470\u0465\3\2\2\2\u0470"+
		"\u0466\3\2\2\2\u0471\u00b1\3\2\2\2\u0472\u0474\7o\2\2\u0473\u0475\7v\2"+
		"\2\u0474\u0473\3\2\2\2\u0474\u0475\3\2\2\2\u0475\u0476\3\2\2\2\u0476\u0477"+
		"\5\u0088E\2\u0477\u00b3\3\2\2\2\u0478\u0484\7m\2\2\u0479\u0485\5d\63\2"+
		"\u047a\u0480\7p\2\2\u047b\u047c\5d\63\2\u047c\u047d\5\u009cO\2\u047d\u047f"+
		"\3\2\2\2\u047e\u047b\3\2\2\2\u047f\u0482\3\2\2\2\u0480\u047e\3\2\2\2\u0480"+
		"\u0481\3\2\2\2\u0481\u0483\3\2\2\2\u0482\u0480\3\2\2\2\u0483\u0485\7q"+
		"\2\2\u0484\u0479\3\2\2\2\u0484\u047a\3\2\2\2\u0485\u00b5\3\2\2\2\u0486"+
		"\u0488\7r\2\2\u0487\u0489\5\u00b8]\2\u0488\u0487\3\2\2\2\u0488\u0489\3"+
		"\2\2\2\u0489\u048a\3\2\2\2\u048a\u048b\7s\2\2\u048b\u00b7\3\2\2\2\u048c"+
		"\u048d\5r:\2\u048d\u048e\5\u009cO\2\u048e\u0490\3\2\2\2\u048f\u048c\3"+
		"\2\2\2\u0490\u0491\3\2\2\2\u0491\u048f\3\2\2\2\u0491\u0492\3\2\2\2\u0492"+
		"\u00b9\3\2\2\2\u0493\u049a\5\u00c0a\2\u0494\u049a\5\u00c2b\2\u0495\u049a"+
		"\5\u00c4c\2\u0496\u049a\5\u00be`\2\u0497\u049a\5f\64\2\u0498\u049a\5\u00c6"+
		"d\2\u0499\u0493\3\2\2\2\u0499\u0494\3\2\2\2\u0499\u0495\3\2\2\2\u0499"+
		"\u0496\3\2\2\2\u0499\u0497\3\2\2\2\u0499\u0498\3\2\2\2\u049a\u00bb\3\2"+
		"\2\2\u049b\u049c\5\u00c0a\2\u049c\u049d\7x\2\2\u049d\u04ac\3\2\2\2\u049e"+
		"\u049f\5\u00c2b\2\u049f\u04a0\7x\2\2\u04a0\u04ac\3\2\2\2\u04a1\u04a2\5"+
		"\u00c4c\2\u04a2\u04a3\7x\2\2\u04a3\u04ac\3\2\2\2\u04a4\u04a5\5\u00be`"+
		"\2\u04a5\u04a6\7x\2\2\u04a6\u04ac\3\2\2\2\u04a7\u04a8\5f\64\2\u04a8\u04a9"+
		"\7x\2\2\u04a9\u04ac\3\2\2\2\u04aa\u04ac\5\u00c6d\2\u04ab\u049b\3\2\2\2"+
		"\u04ab\u049e\3\2\2\2\u04ab\u04a1\3\2\2\2\u04ab\u04a4\3\2\2\2\u04ab\u04a7"+
		"\3\2\2\2\u04ab\u04aa\3\2\2\2\u04ac\u00bd\3\2\2\2\u04ad\u04ae\5p9\2\u04ae"+
		"\u00bf\3\2\2\2\u04af\u04b0\5p9\2\u04b0\u04b1\7\u0093\2\2\u04b1\u04b2\5"+
		"p9\2\u04b2\u00c1\3\2\2\2\u04b3\u04b4\5p9\2\u04b4\u04b5\t\22\2\2\u04b5"+
		"\u00c3\3\2\2\2\u04b6\u04b7\5\u00aeX\2\u04b7\u04b8\5\u009aN\2\u04b8\u04b9"+
		"\5\u00aeX\2\u04b9\u00c5\3\2\2\2\u04ba\u04bb\7x\2\2\u04bb\u00c7\3\2\2\2"+
		"\u04bc\u04bd\7o\2\2\u04bd\u04bf\7y\2\2\u04be\u04c0\5r:\2\u04bf\u04be\3"+
		"\2\2\2\u04bf\u04c0\3\2\2\2\u04c0\u00c9\3\2\2\2\u04c1\u04c3\7l\2\2\u04c2"+
		"\u04c4\5\u00aeX\2\u04c3\u04c2\3\2\2\2\u04c3\u04c4\3\2\2\2\u04c4\u00cb"+
		"\3\2\2\2\u04c5\u04c7\7V\2\2\u04c6\u04c8\7o\2\2\u04c7\u04c6\3\2\2\2\u04c7"+
		"\u04c8\3\2\2\2\u04c8\u00cd\3\2\2\2\u04c9\u04cb\7i\2\2\u04ca\u04cc\7o\2"+
		"\2\u04cb\u04ca\3\2\2\2\u04cb\u04cc\3\2\2\2\u04cc\u00cf\3\2\2\2\u04cd\u04ce"+
		"\7b\2\2\u04ce\u04cf\7o\2\2\u04cf\u00d1\3\2\2\2\u04d0\u04d1\7f\2\2\u04d1"+
		"\u00d3\3\2\2\2\u04d2\u04d3\7\\\2\2\u04d3\u04d4\5p9\2\u04d4\u00d5\3\2\2"+
		"\2\u04d5\u04d7\7g\2\2\u04d6\u04d8\5\u00bc_\2\u04d7\u04d6\3\2\2\2\u04d7"+
		"\u04d8\3\2\2\2\u04d8\u04d9\3\2\2\2\u04d9\u04da\5p9\2\u04da\u04e0\5\u00b6"+
		"\\\2\u04db\u04de\7a\2\2\u04dc\u04df\5\u00d6l\2\u04dd\u04df\5\u00b6\\\2"+
		"\u04de\u04dc\3\2\2\2\u04de\u04dd\3\2\2\2\u04df\u04e1\3\2\2\2\u04e0\u04db"+
		"\3\2\2\2\u04e0\u04e1\3\2\2\2\u04e1\u00d7\3\2\2\2\u04e2\u04e5\5\u00dan"+
		"\2\u04e3\u04e5\5\u00e0q\2\u04e4\u04e2\3\2\2\2\u04e4\u04e3\3\2\2\2\u04e5"+
		"\u00d9\3\2\2\2\u04e6\u04e8\7d\2\2\u04e7\u04e9\5\u00bc_\2\u04e8\u04e7\3"+
		"\2\2\2\u04e8\u04e9\3\2\2\2\u04e9\u04eb\3\2\2\2\u04ea\u04ec\5p9\2\u04eb"+
		"\u04ea\3\2\2\2\u04eb\u04ec\3\2\2\2\u04ec\u04ed\3\2\2\2\u04ed\u04f1\7r"+
		"\2\2\u04ee\u04f0\5\u00dco\2\u04ef\u04ee\3\2\2\2\u04f0\u04f3\3\2\2\2\u04f1"+
		"\u04ef\3\2\2\2\u04f1\u04f2\3\2\2\2\u04f2\u04f4\3\2\2\2\u04f3\u04f1\3\2"+
		"\2\2\u04f4\u04f5\7s\2\2\u04f5\u00db\3\2\2\2\u04f6\u04f7\5\u00dep\2\u04f7"+
		"\u04f9\7y\2\2\u04f8\u04fa\5\u00b8]\2\u04f9\u04f8\3\2\2\2\u04f9\u04fa\3"+
		"\2\2\2\u04fa\u00dd\3\2\2\2\u04fb\u04fc\7[\2\2\u04fc\u04ff\5\u00aeX\2\u04fd"+
		"\u04ff\7W\2\2\u04fe\u04fb\3\2\2\2\u04fe\u04fd\3\2\2\2\u04ff\u00df\3\2"+
		"\2\2\u0500\u0502\7d\2\2\u0501\u0503\5\u00bc_\2\u0502\u0501\3\2\2\2\u0502"+
		"\u0503\3\2\2\2\u0503\u0504\3\2\2\2\u0504\u0505\5\u00e2r\2\u0505\u0509"+
		"\7r\2\2\u0506\u0508\5\u00e4s\2\u0507\u0506\3\2\2\2\u0508\u050b\3\2\2\2"+
		"\u0509\u0507\3\2\2\2\u0509\u050a\3\2\2\2\u050a\u050c\3\2\2\2\u050b\u0509"+
		"\3\2\2\2\u050c\u050d\7s\2\2\u050d\u00e1\3\2\2\2\u050e\u050f\7o\2\2\u050f"+
		"\u0511\7}\2\2\u0510\u050e\3\2\2\2\u0510\u0511\3\2\2\2\u0511\u0512\3\2"+
		"\2\2\u0512\u0513\5~@\2\u0513\u0514\7z\2\2\u0514\u0515\7p\2\2\u0515\u0516"+
		"\7h\2\2\u0516\u0517\7q\2\2\u0517\u00e3\3\2\2\2\u0518\u0519\5\u00e6t\2"+
		"\u0519\u051b\7y\2\2\u051a\u051c\5\u00b8]\2\u051b\u051a\3\2\2\2\u051b\u051c"+
		"\3\2\2\2\u051c\u00e5\3\2\2\2\u051d\u051e\7[\2\2\u051e\u0521\5\u00e8u\2"+
		"\u051f\u0521\7W\2\2\u0520\u051d\3\2\2\2\u0520\u051f\3\2\2\2\u0521\u00e7"+
		"\3\2\2\2\u0522\u0525\5\u0088E\2\u0523\u0525\7n\2\2\u0524\u0522\3\2\2\2"+
		"\u0524\u0523\3\2\2\2\u0525\u052d\3\2\2\2\u0526\u0529\7w\2\2\u0527\u052a"+
		"\5\u0088E\2\u0528\u052a\7n\2\2\u0529\u0527\3\2\2\2\u0529\u0528\3\2\2\2"+
		"\u052a\u052c\3\2\2\2\u052b\u0526\3\2\2\2\u052c\u052f\3\2\2\2\u052d\u052b"+
		"\3\2\2\2\u052d\u052e\3\2\2\2\u052e\u00e9\3\2\2\2\u052f\u052d\3\2\2\2\u0530"+
		"\u0531\7Z\2\2\u0531\u0535\7r\2\2\u0532\u0534\5\u00ecw\2\u0533\u0532\3"+
		"\2\2\2\u0534\u0537\3\2\2\2\u0535\u0533\3\2\2\2\u0535\u0536\3\2\2\2\u0536"+
		"\u0538\3\2\2\2\u0537\u0535\3\2\2\2\u0538\u0539\7s\2\2\u0539\u00eb\3\2"+
		"\2\2\u053a\u053b\5\u00eex\2\u053b\u053d\7y\2\2\u053c\u053e\5\u00b8]\2"+
		"\u053d\u053c\3\2\2\2\u053d\u053e\3\2\2\2\u053e\u00ed\3\2\2\2\u053f\u0542"+
		"\7[\2\2\u0540\u0543\5\u00c0a\2\u0541\u0543\5\u00f0y\2\u0542\u0540\3\2"+
		"\2\2\u0542\u0541\3\2\2\2\u0543\u0546\3\2\2\2\u0544\u0546\7W\2\2\u0545"+
		"\u053f\3\2\2\2\u0545\u0544\3\2\2\2\u0546\u00ef\3\2\2\2\u0547\u0548\5\u00ae"+
		"X\2\u0548\u0549\7v\2\2\u0549\u054e\3\2\2\2\u054a\u054b\5\u00acW\2\u054b"+
		"\u054c\7}\2\2\u054c\u054e\3\2\2\2\u054d\u0547\3\2\2\2\u054d\u054a\3\2"+
		"\2\2\u054d\u054e\3\2\2\2\u054e\u054f\3\2\2\2\u054f\u0550\5p9\2\u0550\u00f1"+
		"\3\2\2\2\u0551\u0555\7j\2\2\u0552\u0556\5p9\2\u0553\u0556\5\u00f4{\2\u0554"+
		"\u0556\5\u00f6|\2\u0555\u0552\3\2\2\2\u0555\u0553\3\2\2\2\u0555\u0554"+
		"\3\2\2\2\u0555\u0556\3\2\2\2\u0556\u0557\3\2\2\2\u0557\u0558\5\u00b6\\"+
		"\2\u0558\u00f3\3\2\2\2\u0559\u055b\5\u00bc_\2\u055a\u055c\5p9\2\u055b"+
		"\u055a\3\2\2\2\u055b\u055c\3\2\2\2\u055c\u055d\3\2\2\2\u055d\u055f\7x"+
		"\2\2\u055e\u0560\5\u00ba^\2\u055f\u055e\3\2\2\2\u055f\u0560\3\2\2\2\u0560"+
		"\u00f5\3\2\2\2\u0561\u0562\5\u00aeX\2\u0562\u0563\7v\2\2\u0563\u0568\3"+
		"\2\2\2\u0564\u0565\5\u00acW\2\u0565\u0566\7}\2\2\u0566\u0568\3\2\2\2\u0567"+
		"\u0561\3\2\2\2\u0567\u0564\3\2\2\2\u0567\u0568\3\2\2\2\u0568\u0569\3\2"+
		"\2\2\u0569\u056a\7\'\2\2\u056a\u056b\5p9\2\u056b\u00f7\3\2\2\2\u056c\u056d"+
		"\7]\2\2\u056d\u056e\5p9\2\u056e\u00f9\3\2\2\2\u056f\u0572\5\u011c\u008f"+
		"\2\u0570\u0572\7o\2\2\u0571\u056f\3\2\2\2\u0571\u0570\3\2\2\2\u0572\u00fb"+
		"\3\2\2\2\u0573\u0574\7t\2\2\u0574\u0575\5\u00fe\u0080\2\u0575\u0576\7"+
		"u\2\2\u0576\u0577\5\u0100\u0081\2\u0577\u00fd\3\2\2\2\u0578\u0579\5p9"+
		"\2\u0579\u00ff\3\2\2\2\u057a\u057b\5\u0088E\2\u057b\u0101\3\2\2\2\u057c"+
		"\u057d\7\u0091\2\2\u057d\u057e\5\u0088E\2\u057e\u0103\3\2\2\2\u057f\u0580"+
		"\7t\2\2\u0580\u0581\7u\2\2\u0581\u0582\5\u0100\u0081\2\u0582\u0105\3\2"+
		"\2\2\u0583\u0584\7^\2\2\u0584\u0585\7t\2\2\u0585\u0586\5\u0088E\2\u0586"+
		"\u0587\7u\2\2\u0587\u0588\5\u0100\u0081\2\u0588\u0107\3\2\2\2\u0589\u058f"+
		"\7`\2\2\u058a\u058b\7`\2\2\u058b\u058f\7\u0093\2\2\u058c\u058d\7\u0093"+
		"\2\2\u058d\u058f\7`\2\2\u058e\u0589\3\2\2\2\u058e\u058a\3\2\2\2\u058e"+
		"\u058c\3\2\2\2\u058f\u0590\3\2\2\2\u0590\u0591\5\u0100\u0081\2\u0591\u0109"+
		"\3\2\2\2\u0592\u0593\7X\2\2\u0593\u0594\5\u010c\u0087\2\u0594\u010b\3"+
		"\2\2\2\u0595\u0596\6\u0087\21\2\u0596\u0597\5\u0110\u0089\2\u0597\u0598"+
		"\5\u010e\u0088\2\u0598\u059b\3\2\2\2\u0599\u059b\5\u0110\u0089\2\u059a"+
		"\u0595\3\2\2\2\u059a\u0599\3\2\2\2\u059b\u010d\3\2\2\2\u059c\u059f\5\u0110"+
		"\u0089\2\u059d\u059f\5\u0088E\2\u059e\u059c\3\2\2\2\u059e\u059d\3\2\2"+
		"\2\u059f\u010f\3\2\2\2\u05a0\u05ac\7p\2\2\u05a1\u05a6\5j\66\2\u05a2\u05a3"+
		"\7w\2\2\u05a3\u05a5\5j\66\2\u05a4\u05a2\3\2\2\2\u05a5\u05a8\3\2\2\2\u05a6"+
		"\u05a4\3\2\2\2\u05a6\u05a7\3\2\2\2\u05a7\u05aa\3\2\2\2\u05a8\u05a6\3\2"+
		"\2\2\u05a9\u05ab\7w\2\2\u05aa\u05a9\3\2\2\2\u05aa\u05ab\3\2\2\2\u05ab"+
		"\u05ad\3\2\2\2\u05ac\u05a1\3\2\2\2\u05ac\u05ad\3\2\2\2\u05ad\u05ae\3\2"+
		"\2\2\u05ae\u05af\7q\2\2\u05af\u0111\3\2\2\2\u05b0\u05b1\5\u0088E\2\u05b1"+
		"\u05b2\7p\2\2\u05b2\u05b4\5p9\2\u05b3\u05b5\7w\2\2\u05b4\u05b3\3\2\2\2"+
		"\u05b4\u05b5\3\2\2\2\u05b5\u05b6\3\2\2\2\u05b6\u05b7\7q\2\2\u05b7\u0113"+
		"\3\2\2\2\u05b8\u05bf\5\u0116\u008c\2\u05b9\u05bf\5\u011a\u008e\2\u05ba"+
		"\u05bb\7p\2\2\u05bb\u05bc\5p9\2\u05bc\u05bd\7q\2\2\u05bd\u05bf\3\2\2\2"+
		"\u05be\u05b8\3\2\2\2\u05be\u05b9\3\2\2\2\u05be\u05ba\3\2\2\2\u05bf\u0115"+
		"\3\2\2\2\u05c0\u05c4\5|?\2\u05c1\u05c4\5\u011e\u0090\2\u05c2\u05c4\5\u0132"+
		"\u009a\2\u05c3\u05c0\3\2\2\2\u05c3\u05c1\3\2\2\2\u05c3\u05c2\3\2\2\2\u05c4"+
		"\u0117\3\2\2\2\u05c5\u05c6\t\23\2\2\u05c6\u0119\3\2\2\2\u05c7\u05ca\7"+
		"o\2\2\u05c8\u05c9\7z\2\2\u05c9\u05cb\7o\2\2\u05ca\u05c8\3\2\2\2\u05ca"+
		"\u05cb\3\2\2\2\u05cb\u011b\3\2\2\2\u05cc\u05cd\7o\2\2\u05cd\u05ce\7z\2"+
		"\2\u05ce\u05cf\7o\2\2\u05cf\u011d\3\2\2\2\u05d0\u05d1\5\u0090I\2\u05d1"+
		"\u05d2\5\u0120\u0091\2\u05d2\u011f\3\2\2\2\u05d3\u05d8\7r\2\2\u05d4\u05d6"+
		"\5\u0122\u0092\2\u05d5\u05d7\7w\2\2\u05d6\u05d5\3\2\2\2\u05d6\u05d7\3"+
		"\2\2\2\u05d7\u05d9\3\2\2\2\u05d8\u05d4\3\2\2\2\u05d8\u05d9\3\2\2\2\u05d9"+
		"\u05da\3\2\2\2\u05da\u05db\7s\2\2\u05db\u0121\3\2\2\2\u05dc\u05e1\5\u0124"+
		"\u0093\2\u05dd\u05de\7w\2\2\u05de\u05e0\5\u0124\u0093\2\u05df\u05dd\3"+
		"\2\2\2\u05e0\u05e3\3\2\2\2\u05e1\u05df\3\2\2\2\u05e1\u05e2\3\2\2\2\u05e2"+
		"\u0123\3\2\2\2\u05e3\u05e1\3\2\2\2\u05e4\u05e5\5\u0126\u0094\2\u05e5\u05e6"+
		"\7y\2\2\u05e6\u05e8\3\2\2\2\u05e7\u05e4\3\2\2\2\u05e7\u05e8\3\2\2\2\u05e8"+
		"\u05e9\3\2\2\2\u05e9\u05ea\5\u0128\u0095\2\u05ea\u0125\3\2\2\2\u05eb\u05ef"+
		"\7o\2\2\u05ec\u05ef\5p9\2\u05ed\u05ef\5\u0120\u0091\2\u05ee\u05eb\3\2"+
		"\2\2\u05ee\u05ec\3\2\2\2\u05ee\u05ed\3\2\2\2\u05ef\u0127\3\2\2\2\u05f0"+
		"\u05f3\5p9\2\u05f1\u05f3\5\u0120\u0091\2\u05f2\u05f0\3\2\2\2\u05f2\u05f1"+
		"\3\2\2\2\u05f3\u0129\3\2\2\2\u05f4\u05f5\7_\2\2\u05f5\u05fb\7r\2\2\u05f6"+
		"\u05f7\5\u012c\u0097\2\u05f7\u05f8\5\u009cO\2\u05f8\u05fa\3\2\2\2\u05f9"+
		"\u05f6\3\2\2\2\u05fa\u05fd\3\2\2\2\u05fb\u05f9\3\2\2\2\u05fb\u05fc\3\2"+
		"\2\2\u05fc\u05fe\3\2\2\2\u05fd\u05fb\3\2\2\2\u05fe\u05ff\7s\2\2\u05ff"+
		"\u012b\3\2\2\2\u0600\u0601\6\u0097\22\2\u0601\u0602\5\u00acW\2\u0602\u0603"+
		"\5\u0088E\2\u0603\u0606\3\2\2\2\u0604\u0606\5\u0130\u0099\2\u0605\u0600"+
		"\3\2\2\2\u0605\u0604\3\2\2\2\u0606\u0608\3\2\2\2\u0607\u0609\5\u012e\u0098"+
		"\2\u0608\u0607\3\2\2\2\u0608\u0609\3\2\2\2\u0609\u012d\3\2\2\2\u060a\u060b"+
		"\t\24\2\2\u060b\u012f\3\2\2\2\u060c\u060e\7\u0091\2\2\u060d\u060c\3\2"+
		"\2\2\u060d\u060e\3\2\2\2\u060e\u060f\3\2\2\2\u060f\u0610\5\u00fa~\2\u0610"+
		"\u0131\3\2\2\2\u0611\u0612\7X\2\2\u0612\u0613\5\u010c\u0087\2\u0613\u0614"+
		"\5\u00b6\\\2\u0614\u0133\3\2\2\2\u0615\u0616\7t\2\2\u0616\u0617\5p9\2"+
		"\u0617\u0618\7u\2\2\u0618\u0135\3\2\2\2\u0619\u061a\7z\2\2\u061a\u061b"+
		"\7p\2\2\u061b\u061c\5\u0088E\2\u061c\u061d\7q\2\2\u061d\u0137\3\2\2\2"+
		"\u061e\u062d\7p\2\2\u061f\u0626\5\u00aeX\2\u0620\u0623\5\u0088E\2\u0621"+
		"\u0622\7w\2\2\u0622\u0624\5\u00aeX\2\u0623\u0621\3\2\2\2\u0623\u0624\3"+
		"\2\2\2\u0624\u0626\3\2\2\2\u0625\u061f\3\2\2\2\u0625\u0620\3\2\2\2\u0626"+
		"\u0628\3\2\2\2\u0627\u0629\7~\2\2\u0628\u0627\3\2\2\2\u0628\u0629\3\2"+
		"\2\2\u0629\u062b\3\2\2\2\u062a\u062c\7w\2\2\u062b\u062a\3\2\2\2\u062b"+
		"\u062c\3\2\2\2\u062c\u062e\3\2\2\2\u062d\u0625\3\2\2\2\u062d\u062e\3\2"+
		"\2\2\u062e\u062f\3\2\2\2\u062f\u0630\7q\2\2\u0630\u0139\3\2\2\2\u0631"+
		"\u0632\5\u013c\u009f\2\u0632\u0633\7z\2\2\u0633\u0634\7o\2\2\u0634\u013b"+
		"\3\2\2\2\u0635\u0636\5\u0088E\2\u0636\u013d\3\2\2\2\u00a3\u014c\u0151"+
		"\u015b\u016e\u0175\u0179\u0180\u0188\u0191\u01b1\u01b9\u01cd\u01cf\u01e0"+
		"\u01ec\u01f5\u0203\u0215\u021c\u0223\u0228\u022b\u0235\u0238\u023c\u0240"+
		"\u0247\u024a\u0257\u025f\u0263\u0266\u026c\u0270\u0273\u027d\u0284\u028b"+
		"\u0299\u02a1\u02aa\u02b1\u02b7\u02c2\u02c9\u02cb\u02d1\u02dd\u02e3\u02e7"+
		"\u02ef\u02f3\u02f8\u02fb\u02fe\u030b\u0322\u0342\u0344\u0359\u0361\u036c"+
		"\u0373\u037d\u0385\u0391\u0395\u039a\u039d\u03a6\u03ac\u03b7\u03bf\u03c5"+
		"\u03cf\u03da\u03e5\u03e9\u03eb\u03f9\u03fd\u0401\u0404\u040b\u0416\u041f"+
		"\u042c\u0430\u0433\u043c\u0446\u044a\u044e\u0452\u0459\u0461\u046c\u0470"+
		"\u0474\u0480\u0484\u0488\u0491\u0499\u04ab\u04bf\u04c3\u04c7\u04cb\u04d7"+
		"\u04de\u04e0\u04e4\u04e8\u04eb\u04f1\u04f9\u04fe\u0502\u0509\u0510\u051b"+
		"\u0520\u0524\u0529\u052d\u0535\u053d\u0542\u0545\u054d\u0555\u055b\u055f"+
		"\u0567\u0571\u058e\u059a\u059e\u05a6\u05aa\u05ac\u05b4\u05be\u05c3\u05ca"+
		"\u05d6\u05d8\u05e1\u05e7\u05ee\u05f2\u05fb\u0605\u0608\u060d\u0623\u0625"+
		"\u0628\u062b\u062d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}