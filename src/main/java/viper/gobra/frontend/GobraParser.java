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
		PREDICATE=60, WRITEPERM=61, NOPERM=62, TRUSTED=63, BREAK=64, DEFAULT=65, 
		FUNC=66, INTERFACE=67, SELECT=68, CASE=69, DEFER=70, GO=71, MAP=72, STRUCT=73, 
		CHAN=74, ELSE=75, GOTO=76, PACKAGE=77, SWITCH=78, CONST=79, FALLTHROUGH=80, 
		IF=81, TYPE=82, CONTINUE=83, FOR=84, IMPORT=85, RETURN=86, VAR=87, NIL_LIT=88, 
		IDENTIFIER=89, L_PAREN=90, R_PAREN=91, L_CURLY=92, R_CURLY=93, L_BRACKET=94, 
		R_BRACKET=95, ASSIGN=96, COMMA=97, SEMI=98, COLON=99, DOT=100, PLUS_PLUS=101, 
		MINUS_MINUS=102, DECLARE_ASSIGN=103, ELLIPSIS=104, LOGICAL_OR=105, LOGICAL_AND=106, 
		EQUALS=107, NOT_EQUALS=108, LESS=109, LESS_OR_EQUALS=110, GREATER=111, 
		GREATER_OR_EQUALS=112, OR=113, DIV=114, MOD=115, LSHIFT=116, RSHIFT=117, 
		BIT_CLEAR=118, EXCLAMATION=119, PLUS=120, MINUS=121, CARET=122, STAR=123, 
		AMPERSAND=124, RECEIVE=125, DECIMAL_LIT=126, BINARY_LIT=127, OCTAL_LIT=128, 
		HEX_LIT=129, HEX_FLOAT_LIT=130, IMAGINARY_LIT=131, RUNE_LIT=132, BYTE_VALUE=133, 
		OCTAL_BYTE_VALUE=134, HEX_BYTE_VALUE=135, LITTLE_U_VALUE=136, BIG_U_VALUE=137, 
		RAW_STRING_LIT=138, INTERPRETED_STRING_LIT=139, WS=140, COMMENT=141, TERMINATOR=142, 
		LINE_COMMENT=143;
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
		RULE_literalType = 71, RULE_implicitArray = 72, RULE_slice_ = 73, RULE_low = 74, 
		RULE_high = 75, RULE_cap = 76, RULE_assign_op = 77, RULE_eos = 78, RULE_packageClause = 79, 
		RULE_importDecl = 80, RULE_importSpec = 81, RULE_importPath = 82, RULE_declaration = 83, 
		RULE_constDecl = 84, RULE_constSpec = 85, RULE_identifierList = 86, RULE_expressionList = 87, 
		RULE_typeDecl = 88, RULE_typeSpec = 89, RULE_varDecl = 90, RULE_block = 91, 
		RULE_statementList = 92, RULE_simpleStmt = 93, RULE_terminatedSimpleStmt = 94, 
		RULE_expressionStmt = 95, RULE_sendStmt = 96, RULE_incDecStmt = 97, RULE_assignment = 98, 
		RULE_emptyStmt = 99, RULE_labeledStmt = 100, RULE_returnStmt = 101, RULE_breakStmt = 102, 
		RULE_continueStmt = 103, RULE_gotoStmt = 104, RULE_fallthroughStmt = 105, 
		RULE_deferStmt = 106, RULE_ifStmt = 107, RULE_switchStmt = 108, RULE_exprSwitchStmt = 109, 
		RULE_exprCaseClause = 110, RULE_exprSwitchCase = 111, RULE_typeSwitchStmt = 112, 
		RULE_typeSwitchGuard = 113, RULE_typeCaseClause = 114, RULE_typeSwitchCase = 115, 
		RULE_typeList = 116, RULE_selectStmt = 117, RULE_commClause = 118, RULE_commCase = 119, 
		RULE_recvStmt = 120, RULE_forStmt = 121, RULE_forClause = 122, RULE_rangeClause = 123, 
		RULE_goStmt = 124, RULE_typeName = 125, RULE_arrayType = 126, RULE_arrayLength = 127, 
		RULE_elementType = 128, RULE_pointerType = 129, RULE_sliceType = 130, 
		RULE_mapType = 131, RULE_channelType = 132, RULE_functionType = 133, RULE_signature = 134, 
		RULE_result = 135, RULE_parameters = 136, RULE_conversion = 137, RULE_operand = 138, 
		RULE_literal = 139, RULE_integer = 140, RULE_operandName = 141, RULE_qualifiedIdent = 142, 
		RULE_compositeLit = 143, RULE_literalValue = 144, RULE_elementList = 145, 
		RULE_keyedElement = 146, RULE_key = 147, RULE_element = 148, RULE_structType = 149, 
		RULE_fieldDecl = 150, RULE_string_ = 151, RULE_embeddedField = 152, RULE_functionLit = 153, 
		RULE_index = 154, RULE_typeAssertion = 155, RULE_arguments = 156, RULE_methodExpr = 157, 
		RULE_receiverType = 158;
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
			"implicitArray", "slice_", "low", "high", "cap", "assign_op", "eos", 
			"packageClause", "importDecl", "importSpec", "importPath", "declaration", 
			"constDecl", "constSpec", "identifierList", "expressionList", "typeDecl", 
			"typeSpec", "varDecl", "block", "statementList", "simpleStmt", "terminatedSimpleStmt", 
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
			"'predicate'", "'writePerm'", "'noPerm'", "'trusted'", "'break'", "'default'", 
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
			"NOPERM", "TRUSTED", "BREAK", "DEFAULT", "FUNC", "INTERFACE", "SELECT", 
			"CASE", "DEFER", "GO", "MAP", "STRUCT", "CHAN", "ELSE", "GOTO", "PACKAGE", 
			"SWITCH", "CONST", "FALLTHROUGH", "IF", "TYPE", "CONTINUE", "FOR", "IMPORT", 
			"RETURN", "VAR", "NIL_LIT", "IDENTIFIER", "L_PAREN", "R_PAREN", "L_CURLY", 
			"R_CURLY", "L_BRACKET", "R_BRACKET", "ASSIGN", "COMMA", "SEMI", "COLON", 
			"DOT", "PLUS_PLUS", "MINUS_MINUS", "DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", 
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
			setState(318);
			expression(0);
			setState(319);
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
			setState(321);
			statement();
			setState(322);
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
			setState(324);
			type_();
			setState(325);
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
			setState(327);
			maybeAddressableIdentifier();
			setState(332);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(328);
				match(COMMA);
				setState(329);
				maybeAddressableIdentifier();
				}
				}
				setState(334);
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
			setState(335);
			match(IDENTIFIER);
			setState(337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(336);
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
			setState(347);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(339);
				match(GHOST);
				setState(340);
				statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(341);
				match(ASSERT);
				setState(342);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(343);
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
				setState(344);
				predicateAccess();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(345);
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
				setState(346);
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
			setState(366);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(349);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(350);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(351);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(352);
				isComparable();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(353);
				old();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(354);
				sConversion();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(355);
				optionNone();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(356);
				optionSome();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(357);
				optionGet();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(358);
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
				setState(359);
				boundVariables();
				setState(360);
				match(COLON);
				setState(361);
				match(COLON);
				setState(362);
				triggers();
				setState(363);
				expression(0);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(365);
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
			setState(368);
			boundVariableDecl();
			setState(373);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(369);
					match(COMMA);
					setState(370);
					boundVariableDecl();
					}
					} 
				}
				setState(375);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(376);
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
			setState(379);
			match(IDENTIFIER);
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(380);
				match(COMMA);
				setState(381);
				match(IDENTIFIER);
				}
				}
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(387);
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
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(389);
				trigger();
				}
				}
				setState(394);
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
			setState(395);
			match(L_CURLY);
			setState(396);
			expression(0);
			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(397);
				match(COMMA);
				setState(398);
				expression(0);
				}
				}
				setState(403);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(404);
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
			setState(406);
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
			setState(408);
			match(SOME);
			setState(409);
			match(L_PAREN);
			setState(410);
			expression(0);
			setState(411);
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
			setState(413);
			match(NONE);
			setState(414);
			match(L_BRACKET);
			setState(415);
			type_();
			setState(416);
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
			setState(418);
			match(GET);
			setState(419);
			match(L_PAREN);
			setState(420);
			expression(0);
			setState(421);
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
			setState(423);
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
			setState(424);
			match(L_PAREN);
			setState(425);
			expression(0);
			setState(426);
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
			setState(428);
			match(OLD);
			setState(433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(429);
				match(L_BRACKET);
				setState(430);
				oldLabelUse();
				setState(431);
				match(R_BRACKET);
				}
			}

			setState(435);
			match(L_PAREN);
			setState(436);
			expression(0);
			setState(437);
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
			setState(441);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(439);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(440);
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
			setState(443);
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
			setState(445);
			match(IS_COMPARABLE);
			setState(446);
			match(L_PAREN);
			setState(447);
			expression(0);
			setState(448);
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
			setState(450);
			match(TYPE_OF);
			setState(451);
			match(L_PAREN);
			setState(452);
			expression(0);
			setState(453);
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
			setState(455);
			match(ACCESS);
			setState(456);
			match(L_PAREN);
			setState(457);
			expression(0);
			setState(463);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(458);
				match(COMMA);
				setState(461);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(459);
					match(IDENTIFIER);
					}
					break;
				case 2:
					{
					setState(460);
					expression(0);
					}
					break;
				}
				}
			}

			setState(465);
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
			setState(467);
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
			setState(468);
			match(L_BRACKET);
			setState(469);
			expression(0);
			setState(470);
			match(DOT_DOT);
			setState(471);
			expression(0);
			setState(472);
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
			setState(474);
			match(L_BRACKET);
			{
			setState(475);
			seqUpdClause();
			setState(480);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(476);
				match(COMMA);
				setState(477);
				seqUpdClause();
				}
				}
				setState(482);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(483);
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
			setState(485);
			expression(0);
			setState(486);
			match(ASSIGN);
			setState(487);
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
			setState(492);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(489);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(490);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(491);
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
			setState(494);
			match(DOM);
			setState(495);
			match(L_CURLY);
			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(496);
				domainClause();
				setState(497);
				eos();
				}
				}
				setState(503);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(504);
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
			setState(515);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(506);
				match(FUNC);
				setState(507);
				match(IDENTIFIER);
				setState(508);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(509);
				match(AXIOM);
				setState(510);
				match(L_CURLY);
				setState(511);
				expression(0);
				setState(512);
				eos();
				setState(513);
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
			setState(517);
			match(GHOST);
			setState(518);
			match(L_BRACKET);
			setState(519);
			match(R_BRACKET);
			setState(520);
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
			setState(533);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(522);
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
				setState(523);
				match(L_BRACKET);
				setState(524);
				type_();
				setState(525);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(527);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(528);
				match(L_BRACKET);
				setState(529);
				type_();
				setState(530);
				match(R_BRACKET);
				setState(531);
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
			setState(555);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(540);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC))) != 0)) {
					{
					{
					{
					setState(535);
					specStatement();
					}
					setState(536);
					eos();
					}
					}
					setState(542);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(543);
				match(PURE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(552);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << TRUSTED))) != 0)) {
					{
					{
					setState(547);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(544);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(545);
						match(PURE);
						}
						break;
					case TRUSTED:
						{
						setState(546);
						match(TRUSTED);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(549);
					eos();
					}
					}
					setState(554);
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
			setState(565);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(557);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(558);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(559);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(560);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(561);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(562);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(563);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(564);
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
			setState(568);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(567);
				expressionList();
				}
				break;
			}
			setState(572);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(570);
				match(IF);
				setState(571);
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
			setState(576);
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
				setState(575);
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
			setState(578);
			match(L_CURLY);
			setState(583);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SHARE) {
				{
				setState(579);
				match(SHARE);
				setState(580);
				identifierList();
				setState(581);
				eos();
				}
			}

			setState(586);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(585);
				statementList();
				}
			}

			setState(588);
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
			setState(590);
			type_();
			setState(591);
			match(IMPL);
			setState(592);
			type_();
			setState(611);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(593);
				match(L_CURLY);
				setState(599);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(594);
					implementationProofPredicateAlias();
					setState(595);
					eos();
					}
					}
					setState(601);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(607);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(602);
					methodImplementationProof();
					setState(603);
					eos();
					}
					}
					setState(609);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(610);
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
			setState(614);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(613);
				match(PURE);
				}
			}

			setState(616);
			nonLocalReceiver();
			setState(617);
			match(IDENTIFIER);
			setState(618);
			signature();
			setState(620);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(619);
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
			setState(622);
			match(L_PAREN);
			setState(624);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(623);
				match(IDENTIFIER);
				}
				break;
			}
			setState(627);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(626);
				match(STAR);
				}
			}

			setState(629);
			typeName();
			setState(630);
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
			setState(637);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(632);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(633);
				type_();
				setState(634);
				match(DOT);
				setState(635);
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
			setState(639);
			match(PRED);
			setState(640);
			match(IDENTIFIER);
			setState(641);
			match(DECLARE_ASSIGN);
			setState(644);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(642);
				selection();
				}
				break;
			case 2:
				{
				setState(643);
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
			setState(646);
			match(MAKE);
			setState(647);
			match(L_PAREN);
			setState(648);
			type_();
			setState(651);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(649);
				match(COMMA);
				setState(650);
				expressionList();
				}
			}

			setState(653);
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
			setState(655);
			match(NEW);
			setState(656);
			match(L_PAREN);
			setState(657);
			type_();
			setState(658);
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
			setState(660);
			specification();
			setState(661);
			match(FUNC);
			setState(662);
			match(IDENTIFIER);
			{
			setState(663);
			signature();
			setState(665);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(664);
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
			setState(667);
			specification();
			setState(668);
			match(FUNC);
			setState(669);
			receiver();
			setState(670);
			match(IDENTIFIER);
			{
			setState(671);
			signature();
			setState(673);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(672);
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
			setState(675);
			packageClause();
			setState(676);
			eos();
			setState(682);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(677);
				importDecl();
				setState(678);
				eos();
				}
				}
				setState(684);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(695);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED) | (1L << TRUSTED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (CONST - 66)) | (1L << (TYPE - 66)) | (1L << (VAR - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
				{
				{
				setState(689);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
				case 1:
					{
					setState(685);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(686);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(687);
					declaration();
					}
					break;
				case 4:
					{
					setState(688);
					ghostMember();
					}
					break;
				}
				setState(691);
				eos();
				}
				}
				setState(697);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(698);
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
			setState(715);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(700);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(701);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(702);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(706);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
				case 1:
					{
					setState(703);
					match(GHOST);
					}
					break;
				case 2:
					{
					{
					setState(704);
					match(GHOST);
					setState(705);
					eos();
					}
					}
					break;
				}
				setState(713);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
				case 1:
					{
					setState(708);
					methodDecl();
					}
					break;
				case 2:
					{
					setState(709);
					functionDecl();
					}
					break;
				case 3:
					{
					setState(710);
					constDecl();
					}
					break;
				case 4:
					{
					setState(711);
					typeDecl();
					}
					break;
				case 5:
					{
					setState(712);
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
			setState(717);
			match(PRED);
			setState(718);
			match(IDENTIFIER);
			setState(719);
			parameters();
			setState(721);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(720);
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
			setState(723);
			match(L_CURLY);
			setState(724);
			expression(0);
			setState(725);
			eos();
			setState(726);
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
			setState(728);
			match(PRED);
			setState(729);
			receiver();
			setState(730);
			match(IDENTIFIER);
			setState(731);
			parameters();
			setState(733);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(732);
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
			setState(735);
			maybeAddressableIdentifierList();
			setState(743);
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
				setState(736);
				type_();
				setState(739);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(737);
					match(ASSIGN);
					setState(738);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(741);
				match(ASSIGN);
				setState(742);
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
			setState(745);
			maybeAddressableIdentifierList();
			setState(746);
			match(DECLARE_ASSIGN);
			setState(747);
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
			setState(749);
			match(L_PAREN);
			setState(751);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(750);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(753);
			type_();
			setState(755);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(754);
				match(COMMA);
				}
			}

			setState(757);
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
			setState(760);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(759);
				match(GHOST);
				}
				break;
			}
			setState(763);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(762);
				identifierList();
				}
				break;
			}
			setState(766);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(765);
				match(ELLIPSIS);
				}
			}

			setState(768);
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
			setState(779);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(770);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(771);
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
				setState(772);
				match(L_PAREN);
				setState(773);
				expression(0);
				setState(774);
				match(R_PAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(776);
				unfolding();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(777);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 119)) & ~0x3f) == 0 && ((1L << (_la - 119)) & ((1L << (EXCLAMATION - 119)) | (1L << (PLUS - 119)) | (1L << (MINUS - 119)) | (1L << (CARET - 119)) | (1L << (STAR - 119)) | (1L << (AMPERSAND - 119)) | (1L << (RECEIVE - 119)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(778);
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
			setState(781);
			match(UNFOLDING);
			setState(782);
			predicateAccess();
			setState(783);
			match(IN);
			setState(784);
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
			setState(802);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(787);
				match(TYPE);
				setState(788);
				match(L_BRACKET);
				setState(789);
				type_();
				setState(790);
				match(R_BRACKET);
				}
				break;
			case 2:
				{
				setState(792);
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
				setState(793);
				match(L_PAREN);
				setState(794);
				expression(0);
				setState(795);
				match(R_PAREN);
				}
				break;
			case 3:
				{
				setState(797);
				unfolding();
				}
				break;
			case 4:
				{
				setState(798);
				make();
				}
				break;
			case 5:
				{
				setState(799);
				((ExpressionContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 119)) & ~0x3f) == 0 && ((1L << (_la - 119)) & ((1L << (EXCLAMATION - 119)) | (1L << (PLUS - 119)) | (1L << (MINUS - 119)) | (1L << (CARET - 119)) | (1L << (STAR - 119)) | (1L << (AMPERSAND - 119)) | (1L << (RECEIVE - 119)))) != 0)) ) {
					((ExpressionContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(800);
				expression(11);
				}
				break;
			case 6:
				{
				setState(801);
				primaryExpr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(836);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(834);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(804);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(805);
						((ExpressionContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (DIV - 114)) | (1L << (MOD - 114)) | (1L << (LSHIFT - 114)) | (1L << (RSHIFT - 114)) | (1L << (BIT_CLEAR - 114)) | (1L << (STAR - 114)) | (1L << (AMPERSAND - 114)))) != 0)) ) {
							((ExpressionContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(806);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(807);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(808);
						((ExpressionContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 101)) & ~0x3f) == 0 && ((1L << (_la - 101)) & ((1L << (PLUS_PLUS - 101)) | (1L << (OR - 101)) | (1L << (PLUS - 101)) | (1L << (MINUS - 101)) | (1L << (CARET - 101)))) != 0)) ) {
							((ExpressionContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(809);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(810);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(811);
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
						setState(812);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(813);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(814);
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
						setState(815);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(816);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(817);
						((ExpressionContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 107)) & ~0x3f) == 0 && ((1L << (_la - 107)) & ((1L << (EQUALS - 107)) | (1L << (NOT_EQUALS - 107)) | (1L << (LESS - 107)) | (1L << (LESS_OR_EQUALS - 107)) | (1L << (GREATER - 107)) | (1L << (GREATER_OR_EQUALS - 107)))) != 0)) ) {
							((ExpressionContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(818);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(819);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(820);
						match(LOGICAL_AND);
						setState(821);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(822);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(823);
						match(LOGICAL_OR);
						setState(824);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(825);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(826);
						match(IMPLIES);
						setState(827);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(828);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(829);
						match(QMARK);
						setState(830);
						expression(0);
						setState(831);
						match(COLON);
						setState(832);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(838);
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
			setState(857);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(839);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(840);
				packageStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(841);
				applyStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(842);
				declaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(843);
				labeledStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(844);
				simpleStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(845);
				goStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(846);
				returnStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(847);
				breakStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(848);
				continueStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(849);
				gotoStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(850);
				fallthroughStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(851);
				block();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(852);
				ifStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(853);
				switchStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(854);
				selectStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(855);
				specForStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(856);
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
			setState(859);
			match(APPLY);
			setState(860);
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
			setState(862);
			match(PACKAGE);
			setState(863);
			expression(0);
			setState(865);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(864);
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
			setState(867);
			loopSpec();
			setState(868);
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
			setState(876);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(870);
				match(INV);
				setState(871);
				expression(0);
				setState(872);
				eos();
				}
				}
				setState(878);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(883);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(879);
				match(DEC);
				setState(880);
				terminationMeasure();
				setState(881);
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
			setState(893);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(885);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(886);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(887);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(888);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(889);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(890);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(891);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(892);
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
			setState(901);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(896);
				operand();
				}
				break;
			case 2:
				{
				setState(897);
				conversion();
				}
				break;
			case 3:
				{
				setState(898);
				methodExpr();
				}
				break;
			case 4:
				{
				setState(899);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				setState(900);
				new_();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(917);
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
					setState(903);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(913);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
					case 1:
						{
						{
						setState(904);
						match(DOT);
						setState(905);
						match(IDENTIFIER);
						}
						}
						break;
					case 2:
						{
						setState(906);
						index();
						}
						break;
					case 3:
						{
						setState(907);
						slice_();
						}
						break;
					case 4:
						{
						setState(908);
						seqUpdExp();
						}
						break;
					case 5:
						{
						setState(909);
						typeAssertion();
						}
						break;
					case 6:
						{
						setState(910);
						if (!(noTerminatorBetween(1))) throw new FailedPredicateException(this, "noTerminatorBetween(1)");
						setState(911);
						arguments();
						}
						break;
					case 7:
						{
						setState(912);
						predConstructArgs();
						}
						break;
					}
					}
					} 
				}
				setState(919);
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
			setState(920);
			match(L_PRED);
			setState(922);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(921);
				expressionList();
				}
			}

			setState(925);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(924);
				match(COMMA);
				}
			}

			setState(927);
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
			setState(929);
			match(INTERFACE);
			setState(930);
			match(L_CURLY);
			setState(940);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(934);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
					case 1:
						{
						setState(931);
						methodSpec();
						}
						break;
					case 2:
						{
						setState(932);
						typeName();
						}
						break;
					case 3:
						{
						setState(933);
						predicateSpec();
						}
						break;
					}
					setState(936);
					eos();
					}
					} 
				}
				setState(942);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			}
			setState(943);
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
			setState(945);
			match(PRED);
			setState(946);
			match(IDENTIFIER);
			setState(947);
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
			setState(965);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(949);
				if (!(noTerminatorAfterParams(2))) throw new FailedPredicateException(this, "noTerminatorAfterParams(2)");
				setState(951);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(950);
					match(GHOST);
					}
				}

				setState(953);
				specification();
				setState(954);
				match(IDENTIFIER);
				setState(955);
				parameters();
				setState(956);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(959);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(958);
					match(GHOST);
					}
				}

				setState(961);
				specification();
				setState(962);
				match(IDENTIFIER);
				setState(963);
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
		enterRule(_localctx, 134, RULE_type_);
		try {
			setState(974);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(967);
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
				setState(968);
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
				setState(969);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(970);
				match(L_PAREN);
				setState(971);
				type_();
				setState(972);
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
		enterRule(_localctx, 136, RULE_typeLit);
		try {
			setState(985);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(976);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(977);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(978);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(979);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(980);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(981);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(982);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(983);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(984);
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
			setState(987);
			match(PRED);
			setState(988);
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
			setState(990);
			match(L_PAREN);
			setState(1002);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
				{
				setState(991);
				type_();
				setState(996);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(992);
						match(COMMA);
						setState(993);
						type_();
						}
						} 
					}
					setState(998);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				}
				setState(1000);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(999);
					match(COMMA);
					}
				}

				}
			}

			setState(1004);
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
		enterRule(_localctx, 142, RULE_literalType);
		try {
			setState(1013);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1006);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1007);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1008);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1009);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1010);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1011);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1012);
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
		enterRule(_localctx, 144, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1015);
			match(L_BRACKET);
			setState(1016);
			match(ELLIPSIS);
			setState(1017);
			match(R_BRACKET);
			setState(1018);
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
		enterRule(_localctx, 146, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1020);
			match(L_BRACKET);
			setState(1036);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(1022);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1021);
					low();
					}
				}

				setState(1024);
				match(COLON);
				setState(1026);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1025);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1029);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
					{
					setState(1028);
					low();
					}
				}

				setState(1031);
				match(COLON);
				setState(1032);
				high();
				setState(1033);
				match(COLON);
				setState(1034);
				cap();
				}
				break;
			}
			setState(1038);
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
		enterRule(_localctx, 148, RULE_low);
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
		enterRule(_localctx, 150, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1042);
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
		enterRule(_localctx, 152, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1044);
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
		enterRule(_localctx, 154, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1047);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 113)) & ~0x3f) == 0 && ((1L << (_la - 113)) & ((1L << (OR - 113)) | (1L << (DIV - 113)) | (1L << (MOD - 113)) | (1L << (LSHIFT - 113)) | (1L << (RSHIFT - 113)) | (1L << (BIT_CLEAR - 113)) | (1L << (PLUS - 113)) | (1L << (MINUS - 113)) | (1L << (CARET - 113)) | (1L << (STAR - 113)) | (1L << (AMPERSAND - 113)))) != 0)) {
				{
				setState(1046);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 113)) & ~0x3f) == 0 && ((1L << (_la - 113)) & ((1L << (OR - 113)) | (1L << (DIV - 113)) | (1L << (MOD - 113)) | (1L << (LSHIFT - 113)) | (1L << (RSHIFT - 113)) | (1L << (BIT_CLEAR - 113)) | (1L << (PLUS - 113)) | (1L << (MINUS - 113)) | (1L << (CARET - 113)) | (1L << (STAR - 113)) | (1L << (AMPERSAND - 113)))) != 0)) ) {
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
		enterRule(_localctx, 156, RULE_eos);
		try {
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
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
		enterRule(_localctx, 158, RULE_packageClause);
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
		enterRule(_localctx, 160, RULE_importDecl);
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
				while (((((_la - 89)) & ~0x3f) == 0 && ((1L << (_la - 89)) & ((1L << (IDENTIFIER - 89)) | (1L << (DOT - 89)) | (1L << (RAW_STRING_LIT - 89)) | (1L << (INTERPRETED_STRING_LIT - 89)))) != 0)) {
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
		enterRule(_localctx, 162, RULE_importSpec);
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
		enterRule(_localctx, 164, RULE_importPath);
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
		enterRule(_localctx, 166, RULE_declaration);
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
		enterRule(_localctx, 168, RULE_constDecl);
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
		enterRule(_localctx, 170, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1101);
			identifierList();
			setState(1107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				{
				setState(1103);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
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
		enterRule(_localctx, 172, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1109);
			match(IDENTIFIER);
			setState(1114);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,94,_ctx);
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
		enterRule(_localctx, 174, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1117);
			expression(0);
			setState(1122);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,95,_ctx);
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
		enterRule(_localctx, 176, RULE_typeDecl);
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
		enterRule(_localctx, 178, RULE_typeSpec);
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
		enterRule(_localctx, 180, RULE_varDecl);
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
		enterRule(_localctx, 182, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1159);
			match(L_CURLY);
			setState(1161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
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
		enterRule(_localctx, 184, RULE_statementList);
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
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0) );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 186, RULE_simpleStmt);
		try {
			setState(1178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
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
		enterRule(_localctx, 188, RULE_terminatedSimpleStmt);
		try {
			setState(1196);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1180);
				sendStmt();
				setState(1181);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1183);
				incDecStmt();
				setState(1184);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1186);
				assignment();
				setState(1187);
				match(SEMI);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1189);
				expressionStmt();
				setState(1190);
				match(SEMI);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1192);
				shortVarDecl();
				setState(1193);
				match(SEMI);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1195);
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
		enterRule(_localctx, 190, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1198);
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
		enterRule(_localctx, 192, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1200);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1201);
			match(RECEIVE);
			setState(1202);
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
		enterRule(_localctx, 194, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1204);
			expression(0);
			setState(1205);
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
		enterRule(_localctx, 196, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			expressionList();
			setState(1208);
			assign_op();
			setState(1209);
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
		enterRule(_localctx, 198, RULE_emptyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1211);
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
		enterRule(_localctx, 200, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1213);
			match(IDENTIFIER);
			setState(1214);
			match(COLON);
			setState(1216);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1215);
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
		enterRule(_localctx, 202, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1218);
			match(RETURN);
			setState(1220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1219);
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
		enterRule(_localctx, 204, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1222);
			match(BREAK);
			setState(1224);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(1223);
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
		enterRule(_localctx, 206, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1226);
			match(CONTINUE);
			setState(1228);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				{
				setState(1227);
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
		enterRule(_localctx, 208, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1230);
			match(GOTO);
			setState(1231);
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
		enterRule(_localctx, 210, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1233);
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
		enterRule(_localctx, 212, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			match(DEFER);
			setState(1236);
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
		enterRule(_localctx, 214, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1238);
			match(IF);
			setState(1240);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1239);
				terminatedSimpleStmt();
				}
				break;
			}
			setState(1242);
			expression(0);
			setState(1243);
			block();
			setState(1249);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
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
		enterRule(_localctx, 216, RULE_switchStmt);
		try {
			setState(1253);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
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
		enterRule(_localctx, 218, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1255);
			match(SWITCH);
			setState(1257);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				{
				setState(1256);
				terminatedSimpleStmt();
				}
				break;
			}
			setState(1260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1259);
				expression(0);
				}
			}

			setState(1262);
			match(L_CURLY);
			setState(1266);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1263);
				exprCaseClause();
				}
				}
				setState(1268);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1269);
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
		enterRule(_localctx, 220, RULE_exprCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1271);
			exprSwitchCase();
			setState(1272);
			match(COLON);
			setState(1274);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1273);
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
		enterRule(_localctx, 222, RULE_exprSwitchCase);
		try {
			setState(1279);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1276);
				match(CASE);
				setState(1277);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1278);
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
		enterRule(_localctx, 224, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1281);
			match(SWITCH);
			setState(1283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1282);
				terminatedSimpleStmt();
				}
				break;
			}
			setState(1285);
			typeSwitchGuard();
			setState(1286);
			match(L_CURLY);
			setState(1290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1287);
				typeCaseClause();
				}
				}
				setState(1292);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1293);
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
		enterRule(_localctx, 226, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1295);
				match(IDENTIFIER);
				setState(1296);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1299);
			primaryExpr(0);
			setState(1300);
			match(DOT);
			setState(1301);
			match(L_PAREN);
			setState(1302);
			match(TYPE);
			setState(1303);
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
		enterRule(_localctx, 228, RULE_typeCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1305);
			typeSwitchCase();
			setState(1306);
			match(COLON);
			setState(1308);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1307);
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
		enterRule(_localctx, 230, RULE_typeSwitchCase);
		try {
			setState(1313);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1310);
				match(CASE);
				setState(1311);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1312);
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
		enterRule(_localctx, 232, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
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
				setState(1315);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1316);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1319);
				match(COMMA);
				setState(1322);
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
					setState(1320);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1321);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1328);
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
		enterRule(_localctx, 234, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1329);
			match(SELECT);
			setState(1330);
			match(L_CURLY);
			setState(1334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1331);
				commClause();
				}
				}
				setState(1336);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1337);
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
		enterRule(_localctx, 236, RULE_commClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1339);
			commCase();
			setState(1340);
			match(COLON);
			setState(1342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSERT) | (1L << ASSUME) | (1L << INHALE) | (1L << EXHALE) | (1L << INV) | (1L << DEC) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << FOLD) | (1L << UNFOLD) | (1L << UNFOLDING) | (1L << GHOST) | (1L << APPLY) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BREAK - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (SELECT - 64)) | (1L << (DEFER - 64)) | (1L << (GO - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (GOTO - 64)) | (1L << (PACKAGE - 64)) | (1L << (SWITCH - 64)) | (1L << (CONST - 64)) | (1L << (FALLTHROUGH - 64)) | (1L << (IF - 64)) | (1L << (TYPE - 64)) | (1L << (CONTINUE - 64)) | (1L << (FOR - 64)) | (1L << (RETURN - 64)) | (1L << (VAR - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)) | (1L << (SEMI - 64)) | (1L << (EXCLAMATION - 64)) | (1L << (PLUS - 64)) | (1L << (MINUS - 64)) | (1L << (CARET - 64)) | (1L << (STAR - 64)) | (1L << (AMPERSAND - 64)) | (1L << (RECEIVE - 64)) | (1L << (DECIMAL_LIT - 64)) | (1L << (BINARY_LIT - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OCTAL_LIT - 128)) | (1L << (HEX_LIT - 128)) | (1L << (IMAGINARY_LIT - 128)) | (1L << (RUNE_LIT - 128)) | (1L << (RAW_STRING_LIT - 128)) | (1L << (INTERPRETED_STRING_LIT - 128)))) != 0)) {
				{
				setState(1341);
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
		enterRule(_localctx, 238, RULE_commCase);
		try {
			setState(1350);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1344);
				match(CASE);
				setState(1347);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
				case 1:
					{
					setState(1345);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1346);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1349);
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
		enterRule(_localctx, 240, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1352);
				expressionList();
				setState(1353);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1355);
				identifierList();
				setState(1356);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1360);
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
		enterRule(_localctx, 242, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1362);
			match(FOR);
			setState(1366);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1363);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1364);
				forClause();
				}
				break;
			case 3:
				{
				setState(1365);
				rangeClause();
				}
				break;
			}
			setState(1368);
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
		enterRule(_localctx, 244, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			((ForClauseContext)_localctx).initStmt = terminatedSimpleStmt();
			setState(1372);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1371);
				expression(0);
				}
			}

			setState(1374);
			match(SEMI);
			setState(1376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (SEMI - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1375);
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
		enterRule(_localctx, 246, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1384);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1378);
				expressionList();
				setState(1379);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1381);
				identifierList();
				setState(1382);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1386);
			match(RANGE);
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
		enterRule(_localctx, 248, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1389);
			match(GO);
			setState(1390);
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
		enterRule(_localctx, 250, RULE_typeName);
		try {
			setState(1394);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1392);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1393);
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
		enterRule(_localctx, 252, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1396);
			match(L_BRACKET);
			setState(1397);
			arrayLength();
			setState(1398);
			match(R_BRACKET);
			setState(1399);
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
		enterRule(_localctx, 254, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1401);
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
		enterRule(_localctx, 256, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
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
		enterRule(_localctx, 258, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1405);
			match(STAR);
			setState(1406);
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
		enterRule(_localctx, 260, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1408);
			match(L_BRACKET);
			setState(1409);
			match(R_BRACKET);
			setState(1410);
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
		enterRule(_localctx, 262, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1412);
			match(MAP);
			setState(1413);
			match(L_BRACKET);
			setState(1414);
			type_();
			setState(1415);
			match(R_BRACKET);
			setState(1416);
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
		enterRule(_localctx, 264, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1423);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1418);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1419);
				match(CHAN);
				setState(1420);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1421);
				match(RECEIVE);
				setState(1422);
				match(CHAN);
				}
				break;
			}
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
		enterRule(_localctx, 266, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1427);
			match(FUNC);
			setState(1428);
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
		enterRule(_localctx, 268, RULE_signature);
		try {
			setState(1435);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1430);
				if (!(noTerminatorAfterParams(1))) throw new FailedPredicateException(this, "noTerminatorAfterParams(1)");
				setState(1431);
				parameters();
				setState(1432);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1434);
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
		enterRule(_localctx, 270, RULE_result);
		try {
			setState(1439);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1437);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1438);
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
		enterRule(_localctx, 272, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1441);
			match(L_PAREN);
			setState(1453);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (ELLIPSIS - 66)) | (1L << (STAR - 66)) | (1L << (RECEIVE - 66)))) != 0)) {
				{
				setState(1442);
				parameterDecl();
				setState(1447);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1443);
						match(COMMA);
						setState(1444);
						parameterDecl();
						}
						} 
					}
					setState(1449);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
				}
				setState(1451);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1450);
					match(COMMA);
					}
				}

				}
			}

			setState(1455);
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
		enterRule(_localctx, 274, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1457);
			type_();
			setState(1458);
			match(L_PAREN);
			setState(1459);
			expression(0);
			setState(1461);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1460);
				match(COMMA);
				}
			}

			setState(1463);
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
		enterRule(_localctx, 276, RULE_operand);
		try {
			setState(1471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1465);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1466);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1467);
				match(L_PAREN);
				setState(1468);
				expression(0);
				setState(1469);
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
		enterRule(_localctx, 278, RULE_literal);
		try {
			setState(1476);
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
				setState(1473);
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
				setState(1474);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1475);
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
		enterRule(_localctx, 280, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1478);
			_la = _input.LA(1);
			if ( !(((((_la - 126)) & ~0x3f) == 0 && ((1L << (_la - 126)) & ((1L << (DECIMAL_LIT - 126)) | (1L << (BINARY_LIT - 126)) | (1L << (OCTAL_LIT - 126)) | (1L << (HEX_LIT - 126)) | (1L << (IMAGINARY_LIT - 126)) | (1L << (RUNE_LIT - 126)))) != 0)) ) {
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
		enterRule(_localctx, 282, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1480);
			match(IDENTIFIER);
			setState(1483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				{
				setState(1481);
				match(DOT);
				setState(1482);
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
		enterRule(_localctx, 284, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1485);
			match(IDENTIFIER);
			setState(1486);
			match(DOT);
			setState(1487);
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
		enterRule(_localctx, 286, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1489);
			literalType();
			setState(1490);
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
		enterRule(_localctx, 288, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1492);
			match(L_CURLY);
			setState(1497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_CURLY - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1493);
				elementList();
				setState(1495);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1494);
					match(COMMA);
					}
				}

				}
			}

			setState(1499);
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
		enterRule(_localctx, 290, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1501);
			keyedElement();
			setState(1506);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,148,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1502);
					match(COMMA);
					setState(1503);
					keyedElement();
					}
					} 
				}
				setState(1508);
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
		enterRule(_localctx, 292, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1512);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				{
				setState(1509);
				key();
				setState(1510);
				match(COLON);
				}
				break;
			}
			setState(1514);
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
		enterRule(_localctx, 294, RULE_key);
		try {
			setState(1519);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1516);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1517);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1518);
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
		enterRule(_localctx, 296, RULE_element);
		try {
			setState(1523);
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
				setState(1521);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1522);
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
		enterRule(_localctx, 298, RULE_structType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1525);
			match(STRUCT);
			setState(1526);
			match(L_CURLY);
			setState(1532);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,152,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1527);
					fieldDecl();
					setState(1528);
					eos();
					}
					} 
				}
				setState(1534);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,152,_ctx);
			}
			setState(1535);
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
		enterRule(_localctx, 300, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1542);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				{
				setState(1537);
				if (!(noTerminatorBetween(2))) throw new FailedPredicateException(this, "noTerminatorBetween(2)");
				setState(1538);
				identifierList();
				setState(1539);
				type_();
				}
				break;
			case 2:
				{
				setState(1541);
				embeddedField();
				}
				break;
			}
			setState(1545);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1544);
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
		enterRule(_localctx, 302, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1547);
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
		enterRule(_localctx, 304, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1550);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1549);
				match(STAR);
				}
			}

			setState(1552);
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
		enterRule(_localctx, 306, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1554);
			match(FUNC);
			setState(1555);
			signature();
			setState(1556);
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
		enterRule(_localctx, 308, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1558);
			match(L_BRACKET);
			setState(1559);
			expression(0);
			setState(1560);
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
		enterRule(_localctx, 310, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1562);
			match(DOT);
			setState(1563);
			match(L_PAREN);
			setState(1564);
			type_();
			setState(1565);
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
		enterRule(_localctx, 312, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1567);
			match(L_PAREN);
			setState(1582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << RANGE) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (FUNC - 66)) | (1L << (INTERFACE - 66)) | (1L << (MAP - 66)) | (1L << (STRUCT - 66)) | (1L << (CHAN - 66)) | (1L << (TYPE - 66)) | (1L << (NIL_LIT - 66)) | (1L << (IDENTIFIER - 66)) | (1L << (L_PAREN - 66)) | (1L << (L_BRACKET - 66)) | (1L << (EXCLAMATION - 66)) | (1L << (PLUS - 66)) | (1L << (MINUS - 66)) | (1L << (CARET - 66)) | (1L << (STAR - 66)) | (1L << (AMPERSAND - 66)) | (1L << (RECEIVE - 66)) | (1L << (DECIMAL_LIT - 66)) | (1L << (BINARY_LIT - 66)) | (1L << (OCTAL_LIT - 66)) | (1L << (HEX_LIT - 66)))) != 0) || ((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (IMAGINARY_LIT - 131)) | (1L << (RUNE_LIT - 131)) | (1L << (RAW_STRING_LIT - 131)) | (1L << (INTERPRETED_STRING_LIT - 131)))) != 0)) {
				{
				setState(1574);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
				case 1:
					{
					setState(1568);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1569);
					type_();
					setState(1572);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
					case 1:
						{
						setState(1570);
						match(COMMA);
						setState(1571);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1577);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1576);
					match(ELLIPSIS);
					}
				}

				setState(1580);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1579);
					match(COMMA);
					}
				}

				}
			}

			setState(1584);
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
		enterRule(_localctx, 314, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1586);
			receiverType();
			setState(1587);
			match(DOT);
			setState(1588);
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
		enterRule(_localctx, 316, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1590);
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
		case 78:
			return eos_sempred((EosContext)_localctx, predIndex);
		case 134:
			return signature_sempred((SignatureContext)_localctx, predIndex);
		case 150:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0091\u063b\4\2\t"+
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
		"\t\u00a0\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u014d\n\5"+
		"\f\5\16\5\u0150\13\5\3\6\3\6\5\6\u0154\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\5\7\u015e\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\5\b\u0171\n\b\3\t\3\t\3\t\7\t\u0176\n\t\f\t\16\t\u0179"+
		"\13\t\3\t\5\t\u017c\n\t\3\n\3\n\3\n\7\n\u0181\n\n\f\n\16\n\u0184\13\n"+
		"\3\n\3\n\3\13\7\13\u0189\n\13\f\13\16\13\u018c\13\13\3\f\3\f\3\f\3\f\7"+
		"\f\u0192\n\f\f\f\16\f\u0195\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3"+
		"\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\3\22\3\22\3\22\3\22\3\22\5\22\u01b4\n\22\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\5\23\u01bc\n\23\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u01d0\n\27\5\27\u01d2"+
		"\n\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31"+
		"\7\31\u01e1\n\31\f\31\16\31\u01e4\13\31\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\5\33\u01ef\n\33\3\34\3\34\3\34\3\34\3\34\7\34\u01f6\n"+
		"\34\f\34\16\34\u01f9\13\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\5\35\u0206\n\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u0218\n\37\3 \3 \3 \7 \u021d"+
		"\n \f \16 \u0220\13 \3 \3 \3 \3 \5 \u0226\n \3 \7 \u0229\n \f \16 \u022c"+
		"\13 \5 \u022e\n \3!\3!\3!\3!\3!\3!\3!\3!\5!\u0238\n!\3\"\5\"\u023b\n\""+
		"\3\"\3\"\5\"\u023f\n\"\3#\3#\5#\u0243\n#\3$\3$\3$\3$\3$\5$\u024a\n$\3"+
		"$\5$\u024d\n$\3$\3$\3%\3%\3%\3%\3%\3%\3%\7%\u0258\n%\f%\16%\u025b\13%"+
		"\3%\3%\3%\7%\u0260\n%\f%\16%\u0263\13%\3%\5%\u0266\n%\3&\5&\u0269\n&\3"+
		"&\3&\3&\3&\5&\u026f\n&\3\'\3\'\5\'\u0273\n\'\3\'\5\'\u0276\n\'\3\'\3\'"+
		"\3\'\3(\3(\3(\3(\3(\5(\u0280\n(\3)\3)\3)\3)\3)\5)\u0287\n)\3*\3*\3*\3"+
		"*\3*\5*\u028e\n*\3*\3*\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\5,\u029c\n,\3-\3"+
		"-\3-\3-\3-\3-\5-\u02a4\n-\3.\3.\3.\3.\3.\7.\u02ab\n.\f.\16.\u02ae\13."+
		"\3.\3.\3.\3.\5.\u02b4\n.\3.\3.\7.\u02b8\n.\f.\16.\u02bb\13.\3.\3.\3/\3"+
		"/\3/\3/\3/\3/\5/\u02c5\n/\3/\3/\3/\3/\3/\5/\u02cc\n/\5/\u02ce\n/\3\60"+
		"\3\60\3\60\3\60\5\60\u02d4\n\60\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\62\3\62\5\62\u02e0\n\62\3\63\3\63\3\63\3\63\5\63\u02e6\n\63\3\63\3"+
		"\63\5\63\u02ea\n\63\3\64\3\64\3\64\3\64\3\65\3\65\5\65\u02f2\n\65\3\65"+
		"\3\65\5\65\u02f6\n\65\3\65\3\65\3\66\5\66\u02fb\n\66\3\66\5\66\u02fe\n"+
		"\66\3\66\5\66\u0301\n\66\3\66\3\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67"+
		"\3\67\3\67\5\67\u030e\n\67\38\38\38\38\38\39\39\39\39\39\39\39\39\39\3"+
		"9\39\39\39\39\39\39\59\u0325\n9\39\39\39\39\39\39\39\39\39\39\39\39\3"+
		"9\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\39\79\u0345\n9\f9\16"+
		"9\u0348\139\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:\5:\u035c"+
		"\n:\3;\3;\3;\3<\3<\3<\5<\u0364\n<\3=\3=\3=\3>\3>\3>\3>\7>\u036d\n>\f>"+
		"\16>\u0370\13>\3>\3>\3>\3>\5>\u0376\n>\3?\3?\3?\3?\3?\3?\3?\3?\5?\u0380"+
		"\n?\3@\3@\3@\3@\3@\3@\5@\u0388\n@\3@\3@\3@\3@\3@\3@\3@\3@\3@\3@\5@\u0394"+
		"\n@\7@\u0396\n@\f@\16@\u0399\13@\3A\3A\5A\u039d\nA\3A\5A\u03a0\nA\3A\3"+
		"A\3B\3B\3B\3B\3B\5B\u03a9\nB\3B\3B\7B\u03ad\nB\fB\16B\u03b0\13B\3B\3B"+
		"\3C\3C\3C\3C\3D\3D\5D\u03ba\nD\3D\3D\3D\3D\3D\3D\5D\u03c2\nD\3D\3D\3D"+
		"\3D\5D\u03c8\nD\3E\3E\3E\3E\3E\3E\3E\5E\u03d1\nE\3F\3F\3F\3F\3F\3F\3F"+
		"\3F\3F\5F\u03dc\nF\3G\3G\3G\3H\3H\3H\3H\7H\u03e5\nH\fH\16H\u03e8\13H\3"+
		"H\5H\u03eb\nH\5H\u03ed\nH\3H\3H\3I\3I\3I\3I\3I\3I\3I\5I\u03f8\nI\3J\3"+
		"J\3J\3J\3J\3K\3K\5K\u0401\nK\3K\3K\5K\u0405\nK\3K\5K\u0408\nK\3K\3K\3"+
		"K\3K\3K\5K\u040f\nK\3K\3K\3L\3L\3M\3M\3N\3N\3O\5O\u041a\nO\3O\3O\3P\3"+
		"P\3P\3P\3P\5P\u0423\nP\3Q\3Q\3Q\3R\3R\3R\3R\3R\3R\7R\u042e\nR\fR\16R\u0431"+
		"\13R\3R\5R\u0434\nR\3S\5S\u0437\nS\3S\3S\3T\3T\3U\3U\3U\5U\u0440\nU\3"+
		"V\3V\3V\3V\3V\3V\7V\u0448\nV\fV\16V\u044b\13V\3V\5V\u044e\nV\3W\3W\5W"+
		"\u0452\nW\3W\3W\5W\u0456\nW\3X\3X\3X\7X\u045b\nX\fX\16X\u045e\13X\3Y\3"+
		"Y\3Y\7Y\u0463\nY\fY\16Y\u0466\13Y\3Z\3Z\3Z\3Z\3Z\3Z\7Z\u046e\nZ\fZ\16"+
		"Z\u0471\13Z\3Z\5Z\u0474\nZ\3[\3[\5[\u0478\n[\3[\3[\3\\\3\\\3\\\3\\\3\\"+
		"\3\\\7\\\u0482\n\\\f\\\16\\\u0485\13\\\3\\\5\\\u0488\n\\\3]\3]\5]\u048c"+
		"\n]\3]\3]\3^\3^\3^\6^\u0493\n^\r^\16^\u0494\3_\3_\3_\3_\3_\3_\5_\u049d"+
		"\n_\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\5`\u04af\n`\3a\3a"+
		"\3b\3b\3b\3b\3c\3c\3c\3d\3d\3d\3d\3e\3e\3f\3f\3f\5f\u04c3\nf\3g\3g\5g"+
		"\u04c7\ng\3h\3h\5h\u04cb\nh\3i\3i\5i\u04cf\ni\3j\3j\3j\3k\3k\3l\3l\3l"+
		"\3m\3m\5m\u04db\nm\3m\3m\3m\3m\3m\5m\u04e2\nm\5m\u04e4\nm\3n\3n\5n\u04e8"+
		"\nn\3o\3o\5o\u04ec\no\3o\5o\u04ef\no\3o\3o\7o\u04f3\no\fo\16o\u04f6\13"+
		"o\3o\3o\3p\3p\3p\5p\u04fd\np\3q\3q\3q\5q\u0502\nq\3r\3r\5r\u0506\nr\3"+
		"r\3r\3r\7r\u050b\nr\fr\16r\u050e\13r\3r\3r\3s\3s\5s\u0514\ns\3s\3s\3s"+
		"\3s\3s\3s\3t\3t\3t\5t\u051f\nt\3u\3u\3u\5u\u0524\nu\3v\3v\5v\u0528\nv"+
		"\3v\3v\3v\5v\u052d\nv\7v\u052f\nv\fv\16v\u0532\13v\3w\3w\3w\7w\u0537\n"+
		"w\fw\16w\u053a\13w\3w\3w\3x\3x\3x\5x\u0541\nx\3y\3y\3y\5y\u0546\ny\3y"+
		"\5y\u0549\ny\3z\3z\3z\3z\3z\3z\5z\u0551\nz\3z\3z\3{\3{\3{\3{\5{\u0559"+
		"\n{\3{\3{\3|\3|\5|\u055f\n|\3|\3|\5|\u0563\n|\3}\3}\3}\3}\3}\3}\5}\u056b"+
		"\n}\3}\3}\3}\3~\3~\3~\3\177\3\177\5\177\u0575\n\177\3\u0080\3\u0080\3"+
		"\u0080\3\u0080\3\u0080\3\u0081\3\u0081\3\u0082\3\u0082\3\u0083\3\u0083"+
		"\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086\3\u0086\3\u0086\5\u0086\u0592"+
		"\n\u0086\3\u0086\3\u0086\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088"+
		"\3\u0088\3\u0088\5\u0088\u059e\n\u0088\3\u0089\3\u0089\5\u0089\u05a2\n"+
		"\u0089\3\u008a\3\u008a\3\u008a\3\u008a\7\u008a\u05a8\n\u008a\f\u008a\16"+
		"\u008a\u05ab\13\u008a\3\u008a\5\u008a\u05ae\n\u008a\5\u008a\u05b0\n\u008a"+
		"\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b\5\u008b\u05b8\n\u008b"+
		"\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\5\u008c"+
		"\u05c2\n\u008c\3\u008d\3\u008d\3\u008d\5\u008d\u05c7\n\u008d\3\u008e\3"+
		"\u008e\3\u008f\3\u008f\3\u008f\5\u008f\u05ce\n\u008f\3\u0090\3\u0090\3"+
		"\u0090\3\u0090\3\u0091\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092\5\u0092"+
		"\u05da\n\u0092\5\u0092\u05dc\n\u0092\3\u0092\3\u0092\3\u0093\3\u0093\3"+
		"\u0093\7\u0093\u05e3\n\u0093\f\u0093\16\u0093\u05e6\13\u0093\3\u0094\3"+
		"\u0094\3\u0094\5\u0094\u05eb\n\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3"+
		"\u0095\5\u0095\u05f2\n\u0095\3\u0096\3\u0096\5\u0096\u05f6\n\u0096\3\u0097"+
		"\3\u0097\3\u0097\3\u0097\3\u0097\7\u0097\u05fd\n\u0097\f\u0097\16\u0097"+
		"\u0600\13\u0097\3\u0097\3\u0097\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098"+
		"\5\u0098\u0609\n\u0098\3\u0098\5\u0098\u060c\n\u0098\3\u0099\3\u0099\3"+
		"\u009a\5\u009a\u0611\n\u009a\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\3"+
		"\u009b\3\u009c\3\u009c\3\u009c\3\u009c\3\u009d\3\u009d\3\u009d\3\u009d"+
		"\3\u009d\3\u009e\3\u009e\3\u009e\3\u009e\3\u009e\5\u009e\u0627\n\u009e"+
		"\5\u009e\u0629\n\u009e\3\u009e\5\u009e\u062c\n\u009e\3\u009e\5\u009e\u062f"+
		"\n\u009e\5\u009e\u0631\n\u009e\3\u009e\3\u009e\3\u009f\3\u009f\3\u009f"+
		"\3\u009f\3\u00a0\3\u00a0\3\u00a0\2\4p~\u00a1\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|"+
		"~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096"+
		"\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae"+
		"\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6"+
		"\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de"+
		"\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6"+
		"\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e"+
		"\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126"+
		"\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138\u013a\u013c\u013e"+
		"\2\24\3\2\27\30\3\2\7\n\3\2\24\25\3\2?@\3\2(*\4\2(*,,\6\2\'\'--\60\60"+
		"\63\63\3\2y\177\4\2tx}~\6\2\"\"ggssz|\3\2\36 \3\2\33\35\3\2mr\4\2sxz~"+
		"\4\2[[ff\3\2gh\4\2\u0080\u0083\u0085\u0086\3\2\u008c\u008d\2\u06a0\2\u0140"+
		"\3\2\2\2\4\u0143\3\2\2\2\6\u0146\3\2\2\2\b\u0149\3\2\2\2\n\u0151\3\2\2"+
		"\2\f\u015d\3\2\2\2\16\u0170\3\2\2\2\20\u0172\3\2\2\2\22\u017d\3\2\2\2"+
		"\24\u018a\3\2\2\2\26\u018d\3\2\2\2\30\u0198\3\2\2\2\32\u019a\3\2\2\2\34"+
		"\u019f\3\2\2\2\36\u01a4\3\2\2\2 \u01a9\3\2\2\2\"\u01ae\3\2\2\2$\u01bb"+
		"\3\2\2\2&\u01bd\3\2\2\2(\u01bf\3\2\2\2*\u01c4\3\2\2\2,\u01c9\3\2\2\2."+
		"\u01d5\3\2\2\2\60\u01dc\3\2\2\2\62\u01e7\3\2\2\2\64\u01ee\3\2\2\2\66\u01f0"+
		"\3\2\2\28\u0205\3\2\2\2:\u0207\3\2\2\2<\u0217\3\2\2\2>\u022d\3\2\2\2@"+
		"\u0237\3\2\2\2B\u023a\3\2\2\2D\u0242\3\2\2\2F\u0244\3\2\2\2H\u0250\3\2"+
		"\2\2J\u0268\3\2\2\2L\u0270\3\2\2\2N\u027f\3\2\2\2P\u0281\3\2\2\2R\u0288"+
		"\3\2\2\2T\u0291\3\2\2\2V\u0296\3\2\2\2X\u029d\3\2\2\2Z\u02a5\3\2\2\2\\"+
		"\u02cd\3\2\2\2^\u02cf\3\2\2\2`\u02d5\3\2\2\2b\u02da\3\2\2\2d\u02e1\3\2"+
		"\2\2f\u02eb\3\2\2\2h\u02ef\3\2\2\2j\u02fa\3\2\2\2l\u030d\3\2\2\2n\u030f"+
		"\3\2\2\2p\u0324\3\2\2\2r\u035b\3\2\2\2t\u035d\3\2\2\2v\u0360\3\2\2\2x"+
		"\u0365\3\2\2\2z\u036e\3\2\2\2|\u037f\3\2\2\2~\u0387\3\2\2\2\u0080\u039a"+
		"\3\2\2\2\u0082\u03a3\3\2\2\2\u0084\u03b3\3\2\2\2\u0086\u03c7\3\2\2\2\u0088"+
		"\u03d0\3\2\2\2\u008a\u03db\3\2\2\2\u008c\u03dd\3\2\2\2\u008e\u03e0\3\2"+
		"\2\2\u0090\u03f7\3\2\2\2\u0092\u03f9\3\2\2\2\u0094\u03fe\3\2\2\2\u0096"+
		"\u0412\3\2\2\2\u0098\u0414\3\2\2\2\u009a\u0416\3\2\2\2\u009c\u0419\3\2"+
		"\2\2\u009e\u0422\3\2\2\2\u00a0\u0424\3\2\2\2\u00a2\u0427\3\2\2\2\u00a4"+
		"\u0436\3\2\2\2\u00a6\u043a\3\2\2\2\u00a8\u043f\3\2\2\2\u00aa\u0441\3\2"+
		"\2\2\u00ac\u044f\3\2\2\2\u00ae\u0457\3\2\2\2\u00b0\u045f\3\2\2\2\u00b2"+
		"\u0467\3\2\2\2\u00b4\u0475\3\2\2\2\u00b6\u047b\3\2\2\2\u00b8\u0489\3\2"+
		"\2\2\u00ba\u0492\3\2\2\2\u00bc\u049c\3\2\2\2\u00be\u04ae\3\2\2\2\u00c0"+
		"\u04b0\3\2\2\2\u00c2\u04b2\3\2\2\2\u00c4\u04b6\3\2\2\2\u00c6\u04b9\3\2"+
		"\2\2\u00c8\u04bd\3\2\2\2\u00ca\u04bf\3\2\2\2\u00cc\u04c4\3\2\2\2\u00ce"+
		"\u04c8\3\2\2\2\u00d0\u04cc\3\2\2\2\u00d2\u04d0\3\2\2\2\u00d4\u04d3\3\2"+
		"\2\2\u00d6\u04d5\3\2\2\2\u00d8\u04d8\3\2\2\2\u00da\u04e7\3\2\2\2\u00dc"+
		"\u04e9\3\2\2\2\u00de\u04f9\3\2\2\2\u00e0\u0501\3\2\2\2\u00e2\u0503\3\2"+
		"\2\2\u00e4\u0513\3\2\2\2\u00e6\u051b\3\2\2\2\u00e8\u0523\3\2\2\2\u00ea"+
		"\u0527\3\2\2\2\u00ec\u0533\3\2\2\2\u00ee\u053d\3\2\2\2\u00f0\u0548\3\2"+
		"\2\2\u00f2\u0550\3\2\2\2\u00f4\u0554\3\2\2\2\u00f6\u055c\3\2\2\2\u00f8"+
		"\u056a\3\2\2\2\u00fa\u056f\3\2\2\2\u00fc\u0574\3\2\2\2\u00fe\u0576\3\2"+
		"\2\2\u0100\u057b\3\2\2\2\u0102\u057d\3\2\2\2\u0104\u057f\3\2\2\2\u0106"+
		"\u0582\3\2\2\2\u0108\u0586\3\2\2\2\u010a\u0591\3\2\2\2\u010c\u0595\3\2"+
		"\2\2\u010e\u059d\3\2\2\2\u0110\u05a1\3\2\2\2\u0112\u05a3\3\2\2\2\u0114"+
		"\u05b3\3\2\2\2\u0116\u05c1\3\2\2\2\u0118\u05c6\3\2\2\2\u011a\u05c8\3\2"+
		"\2\2\u011c\u05ca\3\2\2\2\u011e\u05cf\3\2\2\2\u0120\u05d3\3\2\2\2\u0122"+
		"\u05d6\3\2\2\2\u0124\u05df\3\2\2\2\u0126\u05ea\3\2\2\2\u0128\u05f1\3\2"+
		"\2\2\u012a\u05f5\3\2\2\2\u012c\u05f7\3\2\2\2\u012e\u0608\3\2\2\2\u0130"+
		"\u060d\3\2\2\2\u0132\u0610\3\2\2\2\u0134\u0614\3\2\2\2\u0136\u0618\3\2"+
		"\2\2\u0138\u061c\3\2\2\2\u013a\u0621\3\2\2\2\u013c\u0634\3\2\2\2\u013e"+
		"\u0638\3\2\2\2\u0140\u0141\5p9\2\u0141\u0142\7\2\2\3\u0142\3\3\2\2\2\u0143"+
		"\u0144\5r:\2\u0144\u0145\7\2\2\3\u0145\5\3\2\2\2\u0146\u0147\5\u0088E"+
		"\2\u0147\u0148\7\2\2\3\u0148\7\3\2\2\2\u0149\u014e\5\n\6\2\u014a\u014b"+
		"\7c\2\2\u014b\u014d\5\n\6\2\u014c\u014a\3\2\2\2\u014d\u0150\3\2\2\2\u014e"+
		"\u014c\3\2\2\2\u014e\u014f\3\2\2\2\u014f\t\3\2\2\2\u0150\u014e\3\2\2\2"+
		"\u0151\u0153\7[\2\2\u0152\u0154\7:\2\2\u0153\u0152\3\2\2\2\u0153\u0154"+
		"\3\2\2\2\u0154\13\3\2\2\2\u0155\u0156\7\32\2\2\u0156\u015e\5r:\2\u0157"+
		"\u0158\7\7\2\2\u0158\u015e\5p9\2\u0159\u015a\t\2\2\2\u015a\u015e\5\30"+
		"\r\2\u015b\u015c\t\3\2\2\u015c\u015e\5p9\2\u015d\u0155\3\2\2\2\u015d\u0157"+
		"\3\2\2\2\u015d\u0159\3\2\2\2\u015d\u015b\3\2\2\2\u015e\r\3\2\2\2\u015f"+
		"\u0171\5.\30\2\u0160\u0171\5,\27\2\u0161\u0171\5*\26\2\u0162\u0171\5("+
		"\25\2\u0163\u0171\5\"\22\2\u0164\u0171\5 \21\2\u0165\u0171\5\34\17\2\u0166"+
		"\u0171\5\32\16\2\u0167\u0171\5\36\20\2\u0168\u0169\t\4\2\2\u0169\u016a"+
		"\5\20\t\2\u016a\u016b\7e\2\2\u016b\u016c\7e\2\2\u016c\u016d\5\24\13\2"+
		"\u016d\u016e\5p9\2\u016e\u0171\3\2\2\2\u016f\u0171\t\5\2\2\u0170\u015f"+
		"\3\2\2\2\u0170\u0160\3\2\2\2\u0170\u0161\3\2\2\2\u0170\u0162\3\2\2\2\u0170"+
		"\u0163\3\2\2\2\u0170\u0164\3\2\2\2\u0170\u0165\3\2\2\2\u0170\u0166\3\2"+
		"\2\2\u0170\u0167\3\2\2\2\u0170\u0168\3\2\2\2\u0170\u016f\3\2\2\2\u0171"+
		"\17\3\2\2\2\u0172\u0177\5\22\n\2\u0173\u0174\7c\2\2\u0174\u0176\5\22\n"+
		"\2\u0175\u0173\3\2\2\2\u0176\u0179\3\2\2\2\u0177\u0175\3\2\2\2\u0177\u0178"+
		"\3\2\2\2\u0178\u017b\3\2\2\2\u0179\u0177\3\2\2\2\u017a\u017c\7c\2\2\u017b"+
		"\u017a\3\2\2\2\u017b\u017c\3\2\2\2\u017c\21\3\2\2\2\u017d\u0182\7[\2\2"+
		"\u017e\u017f\7c\2\2\u017f\u0181\7[\2\2\u0180\u017e\3\2\2\2\u0181\u0184"+
		"\3\2\2\2\u0182\u0180\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0185\3\2\2\2\u0184"+
		"\u0182\3\2\2\2\u0185\u0186\5\u0102\u0082\2\u0186\23\3\2\2\2\u0187\u0189"+
		"\5\26\f\2\u0188\u0187\3\2\2\2\u0189\u018c\3\2\2\2\u018a\u0188\3\2\2\2"+
		"\u018a\u018b\3\2\2\2\u018b\25\3\2\2\2\u018c\u018a\3\2\2\2\u018d\u018e"+
		"\7^\2\2\u018e\u0193\5p9\2\u018f\u0190\7c\2\2\u0190\u0192\5p9\2\u0191\u018f"+
		"\3\2\2\2\u0192\u0195\3\2\2\2\u0193\u0191\3\2\2\2\u0193\u0194\3\2\2\2\u0194"+
		"\u0196\3\2\2\2\u0195\u0193\3\2\2\2\u0196\u0197\7_\2\2\u0197\27\3\2\2\2"+
		"\u0198\u0199\5~@\2\u0199\31\3\2\2\2\u019a\u019b\7\61\2\2\u019b\u019c\7"+
		"\\\2\2\u019c\u019d\5p9\2\u019d\u019e\7]\2\2\u019e\33\3\2\2\2\u019f\u01a0"+
		"\7\65\2\2\u01a0\u01a1\7`\2\2\u01a1\u01a2\5\u0088E\2\u01a2\u01a3\7a\2\2"+
		"\u01a3\35\3\2\2\2\u01a4\u01a5\7\62\2\2\u01a5\u01a6\7\\\2\2\u01a6\u01a7"+
		"\5p9\2\u01a7\u01a8\7]\2\2\u01a8\37\3\2\2\2\u01a9\u01aa\t\6\2\2\u01aa\u01ab"+
		"\7\\\2\2\u01ab\u01ac\5p9\2\u01ac\u01ad\7]\2\2\u01ad!\3\2\2\2\u01ae\u01b3"+
		"\7\22\2\2\u01af\u01b0\7`\2\2\u01b0\u01b1\5$\23\2\u01b1\u01b2\7a\2\2\u01b2"+
		"\u01b4\3\2\2\2\u01b3\u01af\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5\3\2"+
		"\2\2\u01b5\u01b6\7\\\2\2\u01b6\u01b7\5p9\2\u01b7\u01b8\7]\2\2\u01b8#\3"+
		"\2\2\2\u01b9\u01bc\5&\24\2\u01ba\u01bc\7\23\2\2\u01bb\u01b9\3\2\2\2\u01bb"+
		"\u01ba\3\2\2\2\u01bc%\3\2\2\2\u01bd\u01be\7[\2\2\u01be\'\3\2\2\2\u01bf"+
		"\u01c0\78\2\2\u01c0\u01c1\7\\\2\2\u01c1\u01c2\5p9\2\u01c2\u01c3\7]\2\2"+
		"\u01c3)\3\2\2\2\u01c4\u01c5\7\67\2\2\u01c5\u01c6\7\\\2\2\u01c6\u01c7\5"+
		"p9\2\u01c7\u01c8\7]\2\2\u01c8+\3\2\2\2\u01c9\u01ca\7\26\2\2\u01ca\u01cb"+
		"\7\\\2\2\u01cb\u01d1\5p9\2\u01cc\u01cf\7c\2\2\u01cd\u01d0\7[\2\2\u01ce"+
		"\u01d0\5p9\2\u01cf\u01cd\3\2\2\2\u01cf\u01ce\3\2\2\2\u01d0\u01d2\3\2\2"+
		"\2\u01d1\u01cc\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u01d4"+
		"\7]\2\2\u01d4-\3\2\2\2\u01d5\u01d6\t\6\2\2\u01d6\u01d7\7`\2\2\u01d7\u01d8"+
		"\5p9\2\u01d8\u01d9\7;\2\2\u01d9\u01da\5p9\2\u01da\u01db\7a\2\2\u01db/"+
		"\3\2\2\2\u01dc\u01dd\7`\2\2\u01dd\u01e2\5\62\32\2\u01de\u01df\7c\2\2\u01df"+
		"\u01e1\5\62\32\2\u01e0\u01de\3\2\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e0\3"+
		"\2\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5"+
		"\u01e6\7a\2\2\u01e6\61\3\2\2\2\u01e7\u01e8\5p9\2\u01e8\u01e9\7b\2\2\u01e9"+
		"\u01ea\5p9\2\u01ea\63\3\2\2\2\u01eb\u01ef\5<\37\2\u01ec\u01ef\5:\36\2"+
		"\u01ed\u01ef\5\66\34\2\u01ee\u01eb\3\2\2\2\u01ee\u01ec\3\2\2\2\u01ee\u01ed"+
		"\3\2\2\2\u01ef\65\3\2\2\2\u01f0\u01f1\7\63\2\2\u01f1\u01f7\7^\2\2\u01f2"+
		"\u01f3\58\35\2\u01f3\u01f4\5\u009eP\2\u01f4\u01f6\3\2\2\2\u01f5\u01f2"+
		"\3\2\2\2\u01f6\u01f9\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8"+
		"\u01fa\3\2\2\2\u01f9\u01f7\3\2\2\2\u01fa\u01fb\7_\2\2\u01fb\67\3\2\2\2"+
		"\u01fc\u01fd\7D\2\2\u01fd\u01fe\7[\2\2\u01fe\u0206\5\u010e\u0088\2\u01ff"+
		"\u0200\7\64\2\2\u0200\u0201\7^\2\2\u0201\u0202\5p9\2\u0202\u0203\5\u009e"+
		"P\2\u0203\u0204\7_\2\2\u0204\u0206\3\2\2\2\u0205\u01fc\3\2\2\2\u0205\u01ff"+
		"\3\2\2\2\u02069\3\2\2\2\u0207\u0208\7\32\2\2\u0208\u0209\7`\2\2\u0209"+
		"\u020a\7a\2\2\u020a\u020b\5\u0102\u0082\2\u020b;\3\2\2\2\u020c\u020d\t"+
		"\7\2\2\u020d\u020e\7`\2\2\u020e\u020f\5\u0088E\2\u020f\u0210\7a\2\2\u0210"+
		"\u0218\3\2\2\2\u0211\u0212\7+\2\2\u0212\u0213\7`\2\2\u0213\u0214\5\u0088"+
		"E\2\u0214\u0215\7a\2\2\u0215\u0216\5\u0088E\2\u0216\u0218\3\2\2\2\u0217"+
		"\u020c\3\2\2\2\u0217\u0211\3\2\2\2\u0218=\3\2\2\2\u0219\u021a\5@!\2\u021a"+
		"\u021b\5\u009eP\2\u021b\u021d\3\2\2\2\u021c\u0219\3\2\2\2\u021d\u0220"+
		"\3\2\2\2\u021e\u021c\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0221\3\2\2\2\u0220"+
		"\u021e\3\2\2\2\u0221\u022e\7\20\2\2\u0222\u0226\5@!\2\u0223\u0226\7\20"+
		"\2\2\u0224\u0226\7A\2\2\u0225\u0222\3\2\2\2\u0225\u0223\3\2\2\2\u0225"+
		"\u0224\3\2\2\2\u0226\u0227\3\2\2\2\u0227\u0229\5\u009eP\2\u0228\u0225"+
		"\3\2\2\2\u0229\u022c\3\2\2\2\u022a\u0228\3\2\2\2\u022a\u022b\3\2\2\2\u022b"+
		"\u022e\3\2\2\2\u022c\u022a\3\2\2\2\u022d\u021e\3\2\2\2\u022d\u022a\3\2"+
		"\2\2\u022e?\3\2\2\2\u022f\u0230\7\13\2\2\u0230\u0238\5D#\2\u0231\u0232"+
		"\7\f\2\2\u0232\u0238\5D#\2\u0233\u0234\7\r\2\2\u0234\u0238\5D#\2\u0235"+
		"\u0236\7\17\2\2\u0236\u0238\5B\"\2\u0237\u022f\3\2\2\2\u0237\u0231\3\2"+
		"\2\2\u0237\u0233\3\2\2\2\u0237\u0235\3\2\2\2\u0238A\3\2\2\2\u0239\u023b"+
		"\5\u00b0Y\2\u023a\u0239\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u023e\3\2\2"+
		"\2\u023c\u023d\7S\2\2\u023d\u023f\5p9\2\u023e\u023c\3\2\2\2\u023e\u023f"+
		"\3\2\2\2\u023fC\3\2\2\2\u0240\u0243\3\2\2\2\u0241\u0243\5p9\2\u0242\u0240"+
		"\3\2\2\2\u0242\u0241\3\2\2\2\u0243E\3\2\2\2\u0244\u0249\7^\2\2\u0245\u0246"+
		"\79\2\2\u0246\u0247\5\u00aeX\2\u0247\u0248\5\u009eP\2\u0248\u024a\3\2"+
		"\2\2\u0249\u0245\3\2\2\2\u0249\u024a\3\2\2\2\u024a\u024c\3\2\2\2\u024b"+
		"\u024d\5\u00ba^\2\u024c\u024b\3\2\2\2\u024c\u024d\3\2\2\2\u024d\u024e"+
		"\3\2\2\2\u024e\u024f\7_\2\2\u024fG\3\2\2\2\u0250\u0251\5\u0088E\2\u0251"+
		"\u0252\7\21\2\2\u0252\u0265\5\u0088E\2\u0253\u0259\7^\2\2\u0254\u0255"+
		"\5P)\2\u0255\u0256\5\u009eP\2\u0256\u0258\3\2\2\2\u0257\u0254\3\2\2\2"+
		"\u0258\u025b\3\2\2\2\u0259\u0257\3\2\2\2\u0259\u025a\3\2\2\2\u025a\u0261"+
		"\3\2\2\2\u025b\u0259\3\2\2\2\u025c\u025d\5J&\2\u025d\u025e\5\u009eP\2"+
		"\u025e\u0260\3\2\2\2\u025f\u025c\3\2\2\2\u0260\u0263\3\2\2\2\u0261\u025f"+
		"\3\2\2\2\u0261\u0262\3\2\2\2\u0262\u0264\3\2\2\2\u0263\u0261\3\2\2\2\u0264"+
		"\u0266\7_\2\2\u0265\u0253\3\2\2\2\u0265\u0266\3\2\2\2\u0266I\3\2\2\2\u0267"+
		"\u0269\7\20\2\2\u0268\u0267\3\2\2\2\u0268\u0269\3\2\2\2\u0269\u026a\3"+
		"\2\2\2\u026a\u026b\5L\'\2\u026b\u026c\7[\2\2\u026c\u026e\5\u010e\u0088"+
		"\2\u026d\u026f\5\u00b8]\2\u026e\u026d\3\2\2\2\u026e\u026f\3\2\2\2\u026f"+
		"K\3\2\2\2\u0270\u0272\7\\\2\2\u0271\u0273\7[\2\2\u0272\u0271\3\2\2\2\u0272"+
		"\u0273\3\2\2\2\u0273\u0275\3\2\2\2\u0274\u0276\7}\2\2\u0275\u0274\3\2"+
		"\2\2\u0275\u0276\3\2\2\2\u0276\u0277\3\2\2\2\u0277\u0278\5\u00fc\177\2"+
		"\u0278\u0279\7]\2\2\u0279M\3\2\2\2\u027a\u0280\5~@\2\u027b\u027c\5\u0088"+
		"E\2\u027c\u027d\7f\2\2\u027d\u027e\7[\2\2\u027e\u0280\3\2\2\2\u027f\u027a"+
		"\3\2\2\2\u027f\u027b\3\2\2\2\u0280O\3\2\2\2\u0281\u0282\7\66\2\2\u0282"+
		"\u0283\7[\2\2\u0283\u0286\7i\2\2\u0284\u0287\5N(\2\u0285\u0287\5\u011c"+
		"\u008f\2\u0286\u0284\3\2\2\2\u0286\u0285\3\2\2\2\u0287Q\3\2\2\2\u0288"+
		"\u0289\7/\2\2\u0289\u028a\7\\\2\2\u028a\u028d\5\u0088E\2\u028b\u028c\7"+
		"c\2\2\u028c\u028e\5\u00b0Y\2\u028d\u028b\3\2\2\2\u028d\u028e\3\2\2\2\u028e"+
		"\u028f\3\2\2\2\u028f\u0290\7]\2\2\u0290S\3\2\2\2\u0291\u0292\7.\2\2\u0292"+
		"\u0293\7\\\2\2\u0293\u0294\5\u0088E\2\u0294\u0295\7]\2\2\u0295U\3\2\2"+
		"\2\u0296\u0297\5> \2\u0297\u0298\7D\2\2\u0298\u0299\7[\2\2\u0299\u029b"+
		"\5\u010e\u0088\2\u029a\u029c\5F$\2\u029b\u029a\3\2\2\2\u029b\u029c\3\2"+
		"\2\2\u029cW\3\2\2\2\u029d\u029e\5> \2\u029e\u029f\7D\2\2\u029f\u02a0\5"+
		"h\65\2\u02a0\u02a1\7[\2\2\u02a1\u02a3\5\u010e\u0088\2\u02a2\u02a4\5F$"+
		"\2\u02a3\u02a2\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4Y\3\2\2\2\u02a5\u02a6"+
		"\5\u00a0Q\2\u02a6\u02ac\5\u009eP\2\u02a7\u02a8\5\u00a2R\2\u02a8\u02a9"+
		"\5\u009eP\2\u02a9\u02ab\3\2\2\2\u02aa\u02a7\3\2\2\2\u02ab\u02ae\3\2\2"+
		"\2\u02ac\u02aa\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad\u02b9\3\2\2\2\u02ae\u02ac"+
		"\3\2\2\2\u02af\u02b4\5V,\2\u02b0\u02b4\5X-\2\u02b1\u02b4\5\u00a8U\2\u02b2"+
		"\u02b4\5\\/\2\u02b3\u02af\3\2\2\2\u02b3\u02b0\3\2\2\2\u02b3\u02b1\3\2"+
		"\2\2\u02b3\u02b2\3\2\2\2\u02b4\u02b5\3\2\2\2\u02b5\u02b6\5\u009eP\2\u02b6"+
		"\u02b8\3\2\2\2\u02b7\u02b3\3\2\2\2\u02b8\u02bb\3\2\2\2\u02b9\u02b7\3\2"+
		"\2\2\u02b9\u02ba\3\2\2\2\u02ba\u02bc\3\2\2\2\u02bb\u02b9\3\2\2\2\u02bc"+
		"\u02bd\7\2\2\3\u02bd[\3\2\2\2\u02be\u02ce\5H%\2\u02bf\u02ce\5^\60\2\u02c0"+
		"\u02ce\5b\62\2\u02c1\u02c5\7\32\2\2\u02c2\u02c3\7\32\2\2\u02c3\u02c5\5"+
		"\u009eP\2\u02c4\u02c1\3\2\2\2\u02c4\u02c2\3\2\2\2\u02c5\u02cb\3\2\2\2"+
		"\u02c6\u02cc\5X-\2\u02c7\u02cc\5V,\2\u02c8\u02cc\5\u00aaV\2\u02c9\u02cc"+
		"\5\u00b2Z\2\u02ca\u02cc\5\u00b6\\\2\u02cb\u02c6\3\2\2\2\u02cb\u02c7\3"+
		"\2\2\2\u02cb\u02c8\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02ca\3\2\2\2\u02cc"+
		"\u02ce\3\2\2\2\u02cd\u02be\3\2\2\2\u02cd\u02bf\3\2\2\2\u02cd\u02c0\3\2"+
		"\2\2\u02cd\u02c4\3\2\2\2\u02ce]\3\2\2\2\u02cf\u02d0\7\66\2\2\u02d0\u02d1"+
		"\7[\2\2\u02d1\u02d3\5\u0112\u008a\2\u02d2\u02d4\5`\61\2\u02d3\u02d2\3"+
		"\2\2\2\u02d3\u02d4\3\2\2\2\u02d4_\3\2\2\2\u02d5\u02d6\7^\2\2\u02d6\u02d7"+
		"\5p9\2\u02d7\u02d8\5\u009eP\2\u02d8\u02d9\7_\2\2\u02d9a\3\2\2\2\u02da"+
		"\u02db\7\66\2\2\u02db\u02dc\5h\65\2\u02dc\u02dd\7[\2\2\u02dd\u02df\5\u0112"+
		"\u008a\2\u02de\u02e0\5`\61\2\u02df\u02de\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0"+
		"c\3\2\2\2\u02e1\u02e9\5\b\5\2\u02e2\u02e5\5\u0088E\2\u02e3\u02e4\7b\2"+
		"\2\u02e4\u02e6\5\u00b0Y\2\u02e5\u02e3\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6"+
		"\u02ea\3\2\2\2\u02e7\u02e8\7b\2\2\u02e8\u02ea\5\u00b0Y\2\u02e9\u02e2\3"+
		"\2\2\2\u02e9\u02e7\3\2\2\2\u02eae\3\2\2\2\u02eb\u02ec\5\b\5\2\u02ec\u02ed"+
		"\7i\2\2\u02ed\u02ee\5\u00b0Y\2\u02eeg\3\2\2\2\u02ef\u02f1\7\\\2\2\u02f0"+
		"\u02f2\5\n\6\2\u02f1\u02f0\3\2\2\2\u02f1\u02f2\3\2\2\2\u02f2\u02f3\3\2"+
		"\2\2\u02f3\u02f5\5\u0088E\2\u02f4\u02f6\7c\2\2\u02f5\u02f4\3\2\2\2\u02f5"+
		"\u02f6\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u02f8\7]\2\2\u02f8i\3\2\2\2\u02f9"+
		"\u02fb\7\32\2\2\u02fa\u02f9\3\2\2\2\u02fa\u02fb\3\2\2\2\u02fb\u02fd\3"+
		"\2\2\2\u02fc\u02fe\5\u00aeX\2\u02fd\u02fc\3\2\2\2\u02fd\u02fe\3\2\2\2"+
		"\u02fe\u0300\3\2\2\2\u02ff\u0301\7j\2\2\u0300\u02ff\3\2\2\2\u0300\u0301"+
		"\3\2\2\2\u0301\u0302\3\2\2\2\u0302\u0303\5\u0088E\2\u0303k\3\2\2\2\u0304"+
		"\u030e\5~@\2\u0305\u0306\t\b\2\2\u0306\u0307\7\\\2\2\u0307\u0308\5p9\2"+
		"\u0308\u0309\7]\2\2\u0309\u030e\3\2\2\2\u030a\u030e\5n8\2\u030b\u030c"+
		"\t\t\2\2\u030c\u030e\5p9\2\u030d\u0304\3\2\2\2\u030d\u0305\3\2\2\2\u030d"+
		"\u030a\3\2\2\2\u030d\u030b\3\2\2\2\u030em\3\2\2\2\u030f\u0310\7\31\2\2"+
		"\u0310\u0311\5\30\r\2\u0311\u0312\7\33\2\2\u0312\u0313\5p9\2\u0313o\3"+
		"\2\2\2\u0314\u0315\b9\1\2\u0315\u0316\7T\2\2\u0316\u0317\7`\2\2\u0317"+
		"\u0318\5\u0088E\2\u0318\u0319\7a\2\2\u0319\u0325\3\2\2\2\u031a\u031b\t"+
		"\b\2\2\u031b\u031c\7\\\2\2\u031c\u031d\5p9\2\u031d\u031e\7]\2\2\u031e"+
		"\u0325\3\2\2\2\u031f\u0325\5n8\2\u0320\u0325\5R*\2\u0321\u0322\t\t\2\2"+
		"\u0322\u0325\5p9\r\u0323\u0325\5~@\2\u0324\u0314\3\2\2\2\u0324\u031a\3"+
		"\2\2\2\u0324\u031f\3\2\2\2\u0324\u0320\3\2\2\2\u0324\u0321\3\2\2\2\u0324"+
		"\u0323\3\2\2\2\u0325\u0346\3\2\2\2\u0326\u0327\f\13\2\2\u0327\u0328\t"+
		"\n\2\2\u0328\u0345\5p9\f\u0329\u032a\f\n\2\2\u032a\u032b\t\13\2\2\u032b"+
		"\u0345\5p9\13\u032c\u032d\f\t\2\2\u032d\u032e\t\f\2\2\u032e\u0345\5p9"+
		"\n\u032f\u0330\f\b\2\2\u0330\u0331\t\r\2\2\u0331\u0345\5p9\t\u0332\u0333"+
		"\f\7\2\2\u0333\u0334\t\16\2\2\u0334\u0345\5p9\b\u0335\u0336\f\6\2\2\u0336"+
		"\u0337\7l\2\2\u0337\u0345\5p9\7\u0338\u0339\f\5\2\2\u0339\u033a\7k\2\2"+
		"\u033a\u0345\5p9\6\u033b\u033c\f\4\2\2\u033c\u033d\7!\2\2\u033d\u0345"+
		"\5p9\4\u033e\u033f\f\3\2\2\u033f\u0340\7$\2\2\u0340\u0341\5p9\2\u0341"+
		"\u0342\7e\2\2\u0342\u0343\5p9\3\u0343\u0345\3\2\2\2\u0344\u0326\3\2\2"+
		"\2\u0344\u0329\3\2\2\2\u0344\u032c\3\2\2\2\u0344\u032f\3\2\2\2\u0344\u0332"+
		"\3\2\2\2\u0344\u0335\3\2\2\2\u0344\u0338\3\2\2\2\u0344\u033b\3\2\2\2\u0344"+
		"\u033e\3\2\2\2\u0345\u0348\3\2\2\2\u0346\u0344\3\2\2\2\u0346\u0347\3\2"+
		"\2\2\u0347q\3\2\2\2\u0348\u0346\3\2\2\2\u0349\u035c\5\f\7\2\u034a\u035c"+
		"\5v<\2\u034b\u035c\5t;\2\u034c\u035c\5\u00a8U\2\u034d\u035c\5\u00caf\2"+
		"\u034e\u035c\5\u00bc_\2\u034f\u035c\5\u00fa~\2\u0350\u035c\5\u00ccg\2"+
		"\u0351\u035c\5\u00ceh\2\u0352\u035c\5\u00d0i\2\u0353\u035c\5\u00d2j\2"+
		"\u0354\u035c\5\u00d4k\2\u0355\u035c\5\u00b8]\2\u0356\u035c\5\u00d8m\2"+
		"\u0357\u035c\5\u00dan\2\u0358\u035c\5\u00ecw\2\u0359\u035c\5x=\2\u035a"+
		"\u035c\5\u00d6l\2\u035b\u0349\3\2\2\2\u035b\u034a\3\2\2\2\u035b\u034b"+
		"\3\2\2\2\u035b\u034c\3\2\2\2\u035b\u034d\3\2\2\2\u035b\u034e\3\2\2\2\u035b"+
		"\u034f\3\2\2\2\u035b\u0350\3\2\2\2\u035b\u0351\3\2\2\2\u035b\u0352\3\2"+
		"\2\2\u035b\u0353\3\2\2\2\u035b\u0354\3\2\2\2\u035b\u0355\3\2\2\2\u035b"+
		"\u0356\3\2\2\2\u035b\u0357\3\2\2\2\u035b\u0358\3\2\2\2\u035b\u0359\3\2"+
		"\2\2\u035b\u035a\3\2\2\2\u035cs\3\2\2\2\u035d\u035e\7#\2\2\u035e\u035f"+
		"\5p9\2\u035fu\3\2\2\2\u0360\u0361\7O\2\2\u0361\u0363\5p9\2\u0362\u0364"+
		"\5\u00b8]\2\u0363\u0362\3\2\2\2\u0363\u0364\3\2\2\2\u0364w\3\2\2\2\u0365"+
		"\u0366\5z>\2\u0366\u0367\5\u00f4{\2\u0367y\3\2\2\2\u0368\u0369\7\16\2"+
		"\2\u0369\u036a\5p9\2\u036a\u036b\5\u009eP\2\u036b\u036d\3\2\2\2\u036c"+
		"\u0368\3\2\2\2\u036d\u0370\3\2\2\2\u036e\u036c\3\2\2\2\u036e\u036f\3\2"+
		"\2\2\u036f\u0375\3\2\2\2\u0370\u036e\3\2\2\2\u0371\u0372\7\17\2\2\u0372"+
		"\u0373\5B\"\2\u0373\u0374\5\u009eP\2\u0374\u0376\3\2\2\2\u0375\u0371\3"+
		"\2\2\2\u0375\u0376\3\2\2\2\u0376{\3\2\2\2\u0377\u0380\7\5\2\2\u0378\u0380"+
		"\7\6\2\2\u0379\u0380\7Z\2\2\u037a\u0380\5\u011a\u008e\2\u037b\u0380\5"+
		"\u0130\u0099\2\u037c\u0380\7\3\2\2\u037d\u0380\7\u0085\2\2\u037e\u0380"+
		"\7\u0086\2\2\u037f\u0377\3\2\2\2\u037f\u0378\3\2\2\2\u037f\u0379\3\2\2"+
		"\2\u037f\u037a\3\2\2\2\u037f\u037b\3\2\2\2\u037f\u037c\3\2\2\2\u037f\u037d"+
		"\3\2\2\2\u037f\u037e\3\2\2\2\u0380}\3\2\2\2\u0381\u0382\b@\1\2\u0382\u0388"+
		"\5\u0116\u008c\2\u0383\u0388\5\u0114\u008b\2\u0384\u0388\5\u013c\u009f"+
		"\2\u0385\u0388\5\16\b\2\u0386\u0388\5T+\2\u0387\u0381\3\2\2\2\u0387\u0383"+
		"\3\2\2\2\u0387\u0384\3\2\2\2\u0387\u0385\3\2\2\2\u0387\u0386\3\2\2\2\u0388"+
		"\u0397\3\2\2\2\u0389\u0393\f\3\2\2\u038a\u038b\7f\2\2\u038b\u0394\7[\2"+
		"\2\u038c\u0394\5\u0136\u009c\2\u038d\u0394\5\u0094K\2\u038e\u0394\5\60"+
		"\31\2\u038f\u0394\5\u0138\u009d\2\u0390\u0391\6@\f\2\u0391\u0394\5\u013a"+
		"\u009e\2\u0392\u0394\5\u0080A\2\u0393\u038a\3\2\2\2\u0393\u038c\3\2\2"+
		"\2\u0393\u038d\3\2\2\2\u0393\u038e\3\2\2\2\u0393\u038f\3\2\2\2\u0393\u0390"+
		"\3\2\2\2\u0393\u0392\3\2\2\2\u0394\u0396\3\2\2\2\u0395\u0389\3\2\2\2\u0396"+
		"\u0399\3\2\2\2\u0397\u0395\3\2\2\2\u0397\u0398\3\2\2\2\u0398\177\3\2\2"+
		"\2\u0399\u0397\3\2\2\2\u039a\u039c\7%\2\2\u039b\u039d\5\u00b0Y\2\u039c"+
		"\u039b\3\2\2\2\u039c\u039d\3\2\2\2\u039d\u039f\3\2\2\2\u039e\u03a0\7c"+
		"\2\2\u039f\u039e\3\2\2\2\u039f\u03a0\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1"+
		"\u03a2\7&\2\2\u03a2\u0081\3\2\2\2\u03a3\u03a4\7E\2\2\u03a4\u03ae\7^\2"+
		"\2\u03a5\u03a9\5\u0086D\2\u03a6\u03a9\5\u00fc\177\2\u03a7\u03a9\5\u0084"+
		"C\2\u03a8\u03a5\3\2\2\2\u03a8\u03a6\3\2\2\2\u03a8\u03a7\3\2\2\2\u03a9"+
		"\u03aa\3\2\2\2\u03aa\u03ab\5\u009eP\2\u03ab\u03ad\3\2\2\2\u03ac\u03a8"+
		"\3\2\2\2\u03ad\u03b0\3\2\2\2\u03ae\u03ac\3\2\2\2\u03ae\u03af\3\2\2\2\u03af"+
		"\u03b1\3\2\2\2\u03b0\u03ae\3\2\2\2\u03b1\u03b2\7_\2\2\u03b2\u0083\3\2"+
		"\2\2\u03b3\u03b4\7\66\2\2\u03b4\u03b5\7[\2\2\u03b5\u03b6\5\u0112\u008a"+
		"\2\u03b6\u0085\3\2\2\2\u03b7\u03b9\6D\r\2\u03b8\u03ba\7\32\2\2\u03b9\u03b8"+
		"\3\2\2\2\u03b9\u03ba\3\2\2\2\u03ba\u03bb\3\2\2\2\u03bb\u03bc\5> \2\u03bc"+
		"\u03bd\7[\2\2\u03bd\u03be\5\u0112\u008a\2\u03be\u03bf\5\u0110\u0089\2"+
		"\u03bf\u03c8\3\2\2\2\u03c0\u03c2\7\32\2\2\u03c1\u03c0\3\2\2\2\u03c1\u03c2"+
		"\3\2\2\2\u03c2\u03c3\3\2\2\2\u03c3\u03c4\5> \2\u03c4\u03c5\7[\2\2\u03c5"+
		"\u03c6\5\u0112\u008a\2\u03c6\u03c8\3\2\2\2\u03c7\u03b7\3\2\2\2\u03c7\u03c1"+
		"\3\2\2\2\u03c8\u0087\3\2\2\2\u03c9\u03d1\5\u00fc\177\2\u03ca\u03d1\5\u008a"+
		"F\2\u03cb\u03d1\5\64\33\2\u03cc\u03cd\7\\\2\2\u03cd\u03ce\5\u0088E\2\u03ce"+
		"\u03cf\7]\2\2\u03cf\u03d1\3\2\2\2\u03d0\u03c9\3\2\2\2\u03d0\u03ca\3\2"+
		"\2\2\u03d0\u03cb\3\2\2\2\u03d0\u03cc\3\2\2\2\u03d1\u0089\3\2\2\2\u03d2"+
		"\u03dc\5\u00fe\u0080\2\u03d3\u03dc\5\u012c\u0097\2\u03d4\u03dc\5\u0104"+
		"\u0083\2\u03d5\u03dc\5\u010c\u0087\2\u03d6\u03dc\5\u0082B\2\u03d7\u03dc"+
		"\5\u0106\u0084\2\u03d8\u03dc\5\u0108\u0085\2\u03d9\u03dc\5\u010a\u0086"+
		"\2\u03da\u03dc\5\u008cG\2\u03db\u03d2\3\2\2\2\u03db\u03d3\3\2\2\2\u03db"+
		"\u03d4\3\2\2\2\u03db\u03d5\3\2\2\2\u03db\u03d6\3\2\2\2\u03db\u03d7\3\2"+
		"\2\2\u03db\u03d8\3\2\2\2\u03db\u03d9\3\2\2\2\u03db\u03da\3\2\2\2\u03dc"+
		"\u008b\3\2\2\2\u03dd\u03de\7\66\2\2\u03de\u03df\5\u008eH\2\u03df\u008d"+
		"\3\2\2\2\u03e0\u03ec\7\\\2\2\u03e1\u03e6\5\u0088E\2\u03e2\u03e3\7c\2\2"+
		"\u03e3\u03e5\5\u0088E\2\u03e4\u03e2\3\2\2\2\u03e5\u03e8\3\2\2\2\u03e6"+
		"\u03e4\3\2\2\2\u03e6\u03e7\3\2\2\2\u03e7\u03ea\3\2\2\2\u03e8\u03e6\3\2"+
		"\2\2\u03e9\u03eb\7c\2\2\u03ea\u03e9\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb"+
		"\u03ed\3\2\2\2\u03ec\u03e1\3\2\2\2\u03ec\u03ed\3\2\2\2\u03ed\u03ee\3\2"+
		"\2\2\u03ee\u03ef\7]\2\2\u03ef\u008f\3\2\2\2\u03f0\u03f8\5\u012c\u0097"+
		"\2\u03f1\u03f8\5\u00fe\u0080\2\u03f2\u03f8\5\u0092J\2\u03f3\u03f8\5\u0106"+
		"\u0084\2\u03f4\u03f8\5\u0108\u0085\2\u03f5\u03f8\5\64\33\2\u03f6\u03f8"+
		"\5\u00fc\177\2\u03f7\u03f0\3\2\2\2\u03f7\u03f1\3\2\2\2\u03f7\u03f2\3\2"+
		"\2\2\u03f7\u03f3\3\2\2\2\u03f7\u03f4\3\2\2\2\u03f7\u03f5\3\2\2\2\u03f7"+
		"\u03f6\3\2\2\2\u03f8\u0091\3\2\2\2\u03f9\u03fa\7`\2\2\u03fa\u03fb\7j\2"+
		"\2\u03fb\u03fc\7a\2\2\u03fc\u03fd\5\u0102\u0082\2\u03fd\u0093\3\2\2\2"+
		"\u03fe\u040e\7`\2\2\u03ff\u0401\5\u0096L\2\u0400\u03ff\3\2\2\2\u0400\u0401"+
		"\3\2\2\2\u0401\u0402\3\2\2\2\u0402\u0404\7e\2\2\u0403\u0405\5\u0098M\2"+
		"\u0404\u0403\3\2\2\2\u0404\u0405\3\2\2\2\u0405\u040f\3\2\2\2\u0406\u0408"+
		"\5\u0096L\2\u0407\u0406\3\2\2\2\u0407\u0408\3\2\2\2\u0408\u0409\3\2\2"+
		"\2\u0409\u040a\7e\2\2\u040a\u040b\5\u0098M\2\u040b\u040c\7e\2\2\u040c"+
		"\u040d\5\u009aN\2\u040d\u040f\3\2\2\2\u040e\u0400\3\2\2\2\u040e\u0407"+
		"\3\2\2\2\u040f\u0410\3\2\2\2\u0410\u0411\7a\2\2\u0411\u0095\3\2\2\2\u0412"+
		"\u0413\5p9\2\u0413\u0097\3\2\2\2\u0414\u0415\5p9\2\u0415\u0099\3\2\2\2"+
		"\u0416\u0417\5p9\2\u0417\u009b\3\2\2\2\u0418\u041a\t\17\2\2\u0419\u0418"+
		"\3\2\2\2\u0419\u041a\3\2\2\2\u041a\u041b\3\2\2\2\u041b\u041c\7b\2\2\u041c"+
		"\u009d\3\2\2\2\u041d\u0423\7d\2\2\u041e\u0423\7\2\2\3\u041f\u0423\6P\16"+
		"\2\u0420\u0423\6P\17\2\u0421\u0423\6P\20\2\u0422\u041d\3\2\2\2\u0422\u041e"+
		"\3\2\2\2\u0422\u041f\3\2\2\2\u0422\u0420\3\2\2\2\u0422\u0421\3\2\2\2\u0423"+
		"\u009f\3\2\2\2\u0424\u0425\7O\2\2\u0425\u0426\7[\2\2\u0426\u00a1\3\2\2"+
		"\2\u0427\u0433\7W\2\2\u0428\u0434\5\u00a4S\2\u0429\u042f\7\\\2\2\u042a"+
		"\u042b\5\u00a4S\2\u042b\u042c\5\u009eP\2\u042c\u042e\3\2\2\2\u042d\u042a"+
		"\3\2\2\2\u042e\u0431\3\2\2\2\u042f\u042d\3\2\2\2\u042f\u0430\3\2\2\2\u0430"+
		"\u0432\3\2\2\2\u0431\u042f\3\2\2\2\u0432\u0434\7]\2\2\u0433\u0428\3\2"+
		"\2\2\u0433\u0429\3\2\2\2\u0434\u00a3\3\2\2\2\u0435\u0437\t\20\2\2\u0436"+
		"\u0435\3\2\2\2\u0436\u0437\3\2\2\2\u0437\u0438\3\2\2\2\u0438\u0439\5\u00a6"+
		"T\2\u0439\u00a5\3\2\2\2\u043a\u043b\5\u0130\u0099\2\u043b\u00a7\3\2\2"+
		"\2\u043c\u0440\5\u00aaV\2\u043d\u0440\5\u00b2Z\2\u043e\u0440\5\u00b6\\"+
		"\2\u043f\u043c\3\2\2\2\u043f\u043d\3\2\2\2\u043f\u043e\3\2\2\2\u0440\u00a9"+
		"\3\2\2\2\u0441\u044d\7Q\2\2\u0442\u044e\5\u00acW\2\u0443\u0449\7\\\2\2"+
		"\u0444\u0445\5\u00acW\2\u0445\u0446\5\u009eP\2\u0446\u0448\3\2\2\2\u0447"+
		"\u0444\3\2\2\2\u0448\u044b\3\2\2\2\u0449\u0447\3\2\2\2\u0449\u044a\3\2"+
		"\2\2\u044a\u044c\3\2\2\2\u044b\u0449\3\2\2\2\u044c\u044e\7]\2\2\u044d"+
		"\u0442\3\2\2\2\u044d\u0443\3\2\2\2\u044e\u00ab\3\2\2\2\u044f\u0455\5\u00ae"+
		"X\2\u0450\u0452\5\u0088E\2\u0451\u0450\3\2\2\2\u0451\u0452\3\2\2\2\u0452"+
		"\u0453\3\2\2\2\u0453\u0454\7b\2\2\u0454\u0456\5\u00b0Y\2\u0455\u0451\3"+
		"\2\2\2\u0455\u0456\3\2\2\2\u0456\u00ad\3\2\2\2\u0457\u045c\7[\2\2\u0458"+
		"\u0459\7c\2\2\u0459\u045b\7[\2\2\u045a\u0458\3\2\2\2\u045b\u045e\3\2\2"+
		"\2\u045c\u045a\3\2\2\2\u045c\u045d\3\2\2\2\u045d\u00af\3\2\2\2\u045e\u045c"+
		"\3\2\2\2\u045f\u0464\5p9\2\u0460\u0461\7c\2\2\u0461\u0463\5p9\2\u0462"+
		"\u0460\3\2\2\2\u0463\u0466\3\2\2\2\u0464\u0462\3\2\2\2\u0464\u0465\3\2"+
		"\2\2\u0465\u00b1\3\2\2\2\u0466\u0464\3\2\2\2\u0467\u0473\7T\2\2\u0468"+
		"\u0474\5\u00b4[\2\u0469\u046f\7\\\2\2\u046a\u046b\5\u00b4[\2\u046b\u046c"+
		"\5\u009eP\2\u046c\u046e\3\2\2\2\u046d\u046a\3\2\2\2\u046e\u0471\3\2\2"+
		"\2\u046f\u046d\3\2\2\2\u046f\u0470\3\2\2\2\u0470\u0472\3\2\2\2\u0471\u046f"+
		"\3\2\2\2\u0472\u0474\7]\2\2\u0473\u0468\3\2\2\2\u0473\u0469\3\2\2\2\u0474"+
		"\u00b3\3\2\2\2\u0475\u0477\7[\2\2\u0476\u0478\7b\2\2\u0477\u0476\3\2\2"+
		"\2\u0477\u0478\3\2\2\2\u0478\u0479\3\2\2\2\u0479\u047a\5\u0088E\2\u047a"+
		"\u00b5\3\2\2\2\u047b\u0487\7Y\2\2\u047c\u0488\5d\63\2\u047d\u0483\7\\"+
		"\2\2\u047e\u047f\5d\63\2\u047f\u0480\5\u009eP\2\u0480\u0482\3\2\2\2\u0481"+
		"\u047e\3\2\2\2\u0482\u0485\3\2\2\2\u0483\u0481\3\2\2\2\u0483\u0484\3\2"+
		"\2\2\u0484\u0486\3\2\2\2\u0485\u0483\3\2\2\2\u0486\u0488\7]\2\2\u0487"+
		"\u047c\3\2\2\2\u0487\u047d\3\2\2\2\u0488\u00b7\3\2\2\2\u0489\u048b\7^"+
		"\2\2\u048a\u048c\5\u00ba^\2\u048b\u048a\3\2\2\2\u048b\u048c\3\2\2\2\u048c"+
		"\u048d\3\2\2\2\u048d\u048e\7_\2\2\u048e\u00b9\3\2\2\2\u048f\u0490\5r:"+
		"\2\u0490\u0491\5\u009eP\2\u0491\u0493\3\2\2\2\u0492\u048f\3\2\2\2\u0493"+
		"\u0494\3\2\2\2\u0494\u0492\3\2\2\2\u0494\u0495\3\2\2\2\u0495\u00bb\3\2"+
		"\2\2\u0496\u049d\5\u00c2b\2\u0497\u049d\5\u00c4c\2\u0498\u049d\5\u00c6"+
		"d\2\u0499\u049d\5\u00c0a\2\u049a\u049d\5f\64\2\u049b\u049d\5\u00c8e\2"+
		"\u049c\u0496\3\2\2\2\u049c\u0497\3\2\2\2\u049c\u0498\3\2\2\2\u049c\u0499"+
		"\3\2\2\2\u049c\u049a\3\2\2\2\u049c\u049b\3\2\2\2\u049d\u00bd\3\2\2\2\u049e"+
		"\u049f\5\u00c2b\2\u049f\u04a0\7d\2\2\u04a0\u04af\3\2\2\2\u04a1\u04a2\5"+
		"\u00c4c\2\u04a2\u04a3\7d\2\2\u04a3\u04af\3\2\2\2\u04a4\u04a5\5\u00c6d"+
		"\2\u04a5\u04a6\7d\2\2\u04a6\u04af\3\2\2\2\u04a7\u04a8\5\u00c0a\2\u04a8"+
		"\u04a9\7d\2\2\u04a9\u04af\3\2\2\2\u04aa\u04ab\5f\64\2\u04ab\u04ac\7d\2"+
		"\2\u04ac\u04af\3\2\2\2\u04ad\u04af\5\u00c8e\2\u04ae\u049e\3\2\2\2\u04ae"+
		"\u04a1\3\2\2\2\u04ae\u04a4\3\2\2\2\u04ae\u04a7\3\2\2\2\u04ae\u04aa\3\2"+
		"\2\2\u04ae\u04ad\3\2\2\2\u04af\u00bf\3\2\2\2\u04b0\u04b1\5p9\2\u04b1\u00c1"+
		"\3\2\2\2\u04b2\u04b3\5p9\2\u04b3\u04b4\7\177\2\2\u04b4\u04b5\5p9\2\u04b5"+
		"\u00c3\3\2\2\2\u04b6\u04b7\5p9\2\u04b7\u04b8\t\21\2\2\u04b8\u00c5\3\2"+
		"\2\2\u04b9\u04ba\5\u00b0Y\2\u04ba\u04bb\5\u009cO\2\u04bb\u04bc\5\u00b0"+
		"Y\2\u04bc\u00c7\3\2\2\2\u04bd\u04be\7d\2\2\u04be\u00c9\3\2\2\2\u04bf\u04c0"+
		"\7[\2\2\u04c0\u04c2\7e\2\2\u04c1\u04c3\5r:\2\u04c2\u04c1\3\2\2\2\u04c2"+
		"\u04c3\3\2\2\2\u04c3\u00cb\3\2\2\2\u04c4\u04c6\7X\2\2\u04c5\u04c7\5\u00b0"+
		"Y\2\u04c6\u04c5\3\2\2\2\u04c6\u04c7\3\2\2\2\u04c7\u00cd\3\2\2\2\u04c8"+
		"\u04ca\7B\2\2\u04c9\u04cb\7[\2\2\u04ca\u04c9\3\2\2\2\u04ca\u04cb\3\2\2"+
		"\2\u04cb\u00cf\3\2\2\2\u04cc\u04ce\7U\2\2\u04cd\u04cf\7[\2\2\u04ce\u04cd"+
		"\3\2\2\2\u04ce\u04cf\3\2\2\2\u04cf\u00d1\3\2\2\2\u04d0\u04d1\7N\2\2\u04d1"+
		"\u04d2\7[\2\2\u04d2\u00d3\3\2\2\2\u04d3\u04d4\7R\2\2\u04d4\u00d5\3\2\2"+
		"\2\u04d5\u04d6\7H\2\2\u04d6\u04d7\5p9\2\u04d7\u00d7\3\2\2\2\u04d8\u04da"+
		"\7S\2\2\u04d9\u04db\5\u00be`\2\u04da\u04d9\3\2\2\2\u04da\u04db\3\2\2\2"+
		"\u04db\u04dc\3\2\2\2\u04dc\u04dd\5p9\2\u04dd\u04e3\5\u00b8]\2\u04de\u04e1"+
		"\7M\2\2\u04df\u04e2\5\u00d8m\2\u04e0\u04e2\5\u00b8]\2\u04e1\u04df\3\2"+
		"\2\2\u04e1\u04e0\3\2\2\2\u04e2\u04e4\3\2\2\2\u04e3\u04de\3\2\2\2\u04e3"+
		"\u04e4\3\2\2\2\u04e4\u00d9\3\2\2\2\u04e5\u04e8\5\u00dco\2\u04e6\u04e8"+
		"\5\u00e2r\2\u04e7\u04e5\3\2\2\2\u04e7\u04e6\3\2\2\2\u04e8\u00db\3\2\2"+
		"\2\u04e9\u04eb\7P\2\2\u04ea\u04ec\5\u00be`\2\u04eb\u04ea\3\2\2\2\u04eb"+
		"\u04ec\3\2\2\2\u04ec\u04ee\3\2\2\2\u04ed\u04ef\5p9\2\u04ee\u04ed\3\2\2"+
		"\2\u04ee\u04ef\3\2\2\2\u04ef\u04f0\3\2\2\2\u04f0\u04f4\7^\2\2\u04f1\u04f3"+
		"\5\u00dep\2\u04f2\u04f1\3\2\2\2\u04f3\u04f6\3\2\2\2\u04f4\u04f2\3\2\2"+
		"\2\u04f4\u04f5\3\2\2\2\u04f5\u04f7\3\2\2\2\u04f6\u04f4\3\2\2\2\u04f7\u04f8"+
		"\7_\2\2\u04f8\u00dd\3\2\2\2\u04f9\u04fa\5\u00e0q\2\u04fa\u04fc\7e\2\2"+
		"\u04fb\u04fd\5\u00ba^\2\u04fc\u04fb\3\2\2\2\u04fc\u04fd\3\2\2\2\u04fd"+
		"\u00df\3\2\2\2\u04fe\u04ff\7G\2\2\u04ff\u0502\5\u00b0Y\2\u0500\u0502\7"+
		"C\2\2\u0501\u04fe\3\2\2\2\u0501\u0500\3\2\2\2\u0502\u00e1\3\2\2\2\u0503"+
		"\u0505\7P\2\2\u0504\u0506\5\u00be`\2\u0505\u0504\3\2\2\2\u0505\u0506\3"+
		"\2\2\2\u0506\u0507\3\2\2\2\u0507\u0508\5\u00e4s\2\u0508\u050c\7^\2\2\u0509"+
		"\u050b\5\u00e6t\2\u050a\u0509\3\2\2\2\u050b\u050e\3\2\2\2\u050c\u050a"+
		"\3\2\2\2\u050c\u050d\3\2\2\2\u050d\u050f\3\2\2\2\u050e\u050c\3\2\2\2\u050f"+
		"\u0510\7_\2\2\u0510\u00e3\3\2\2\2\u0511\u0512\7[\2\2\u0512\u0514\7i\2"+
		"\2\u0513\u0511\3\2\2\2\u0513\u0514\3\2\2\2\u0514\u0515\3\2\2\2\u0515\u0516"+
		"\5~@\2\u0516\u0517\7f\2\2\u0517\u0518\7\\\2\2\u0518\u0519\7T\2\2\u0519"+
		"\u051a\7]\2\2\u051a\u00e5\3\2\2\2\u051b\u051c\5\u00e8u\2\u051c\u051e\7"+
		"e\2\2\u051d\u051f\5\u00ba^\2\u051e\u051d\3\2\2\2\u051e\u051f\3\2\2\2\u051f"+
		"\u00e7\3\2\2\2\u0520\u0521\7G\2\2\u0521\u0524\5\u00eav\2\u0522\u0524\7"+
		"C\2\2\u0523\u0520\3\2\2\2\u0523\u0522\3\2\2\2\u0524\u00e9\3\2\2\2\u0525"+
		"\u0528\5\u0088E\2\u0526\u0528\7Z\2\2\u0527\u0525\3\2\2\2\u0527\u0526\3"+
		"\2\2\2\u0528\u0530\3\2\2\2\u0529\u052c\7c\2\2\u052a\u052d\5\u0088E\2\u052b"+
		"\u052d\7Z\2\2\u052c\u052a\3\2\2\2\u052c\u052b\3\2\2\2\u052d\u052f\3\2"+
		"\2\2\u052e\u0529\3\2\2\2\u052f\u0532\3\2\2\2\u0530\u052e\3\2\2\2\u0530"+
		"\u0531\3\2\2\2\u0531\u00eb\3\2\2\2\u0532\u0530\3\2\2\2\u0533\u0534\7F"+
		"\2\2\u0534\u0538\7^\2\2\u0535\u0537\5\u00eex\2\u0536\u0535\3\2\2\2\u0537"+
		"\u053a\3\2\2\2\u0538\u0536\3\2\2\2\u0538\u0539\3\2\2\2\u0539\u053b\3\2"+
		"\2\2\u053a\u0538\3\2\2\2\u053b\u053c\7_\2\2\u053c\u00ed\3\2\2\2\u053d"+
		"\u053e\5\u00f0y\2\u053e\u0540\7e\2\2\u053f\u0541\5\u00ba^\2\u0540\u053f"+
		"\3\2\2\2\u0540\u0541\3\2\2\2\u0541\u00ef\3\2\2\2\u0542\u0545\7G\2\2\u0543"+
		"\u0546\5\u00c2b\2\u0544\u0546\5\u00f2z\2\u0545\u0543\3\2\2\2\u0545\u0544"+
		"\3\2\2\2\u0546\u0549\3\2\2\2\u0547\u0549\7C\2\2\u0548\u0542\3\2\2\2\u0548"+
		"\u0547\3\2\2\2\u0549\u00f1\3\2\2\2\u054a\u054b\5\u00b0Y\2\u054b\u054c"+
		"\7b\2\2\u054c\u0551\3\2\2\2\u054d\u054e\5\u00aeX\2\u054e\u054f\7i\2\2"+
		"\u054f\u0551\3\2\2\2\u0550\u054a\3\2\2\2\u0550\u054d\3\2\2\2\u0550\u0551"+
		"\3\2\2\2\u0551\u0552\3\2\2\2\u0552\u0553\5p9\2\u0553\u00f3\3\2\2\2\u0554"+
		"\u0558\7V\2\2\u0555\u0559\5p9\2\u0556\u0559\5\u00f6|\2\u0557\u0559\5\u00f8"+
		"}\2\u0558\u0555\3\2\2\2\u0558\u0556\3\2\2\2\u0558\u0557\3\2\2\2\u0558"+
		"\u0559\3\2\2\2\u0559\u055a\3\2\2\2\u055a\u055b\5\u00b8]\2\u055b\u00f5"+
		"\3\2\2\2\u055c\u055e\5\u00be`\2\u055d\u055f\5p9\2\u055e\u055d\3\2\2\2"+
		"\u055e\u055f\3\2\2\2\u055f\u0560\3\2\2\2\u0560\u0562\7d\2\2\u0561\u0563"+
		"\5\u00bc_\2\u0562\u0561\3\2\2\2\u0562\u0563\3\2\2\2\u0563\u00f7\3\2\2"+
		"\2\u0564\u0565\5\u00b0Y\2\u0565\u0566\7b\2\2\u0566\u056b\3\2\2\2\u0567"+
		"\u0568\5\u00aeX\2\u0568\u0569\7i\2\2\u0569\u056b\3\2\2\2\u056a\u0564\3"+
		"\2\2\2\u056a\u0567\3\2\2\2\u056a\u056b\3\2\2\2\u056b\u056c\3\2\2\2\u056c"+
		"\u056d\7\'\2\2\u056d\u056e\5p9\2\u056e\u00f9\3\2\2\2\u056f\u0570\7I\2"+
		"\2\u0570\u0571\5p9\2\u0571\u00fb\3\2\2\2\u0572\u0575\5\u011e\u0090\2\u0573"+
		"\u0575\7[\2\2\u0574\u0572\3\2\2\2\u0574\u0573\3\2\2\2\u0575\u00fd\3\2"+
		"\2\2\u0576\u0577\7`\2\2\u0577\u0578\5\u0100\u0081\2\u0578\u0579\7a\2\2"+
		"\u0579\u057a\5\u0102\u0082\2\u057a\u00ff\3\2\2\2\u057b\u057c\5p9\2\u057c"+
		"\u0101\3\2\2\2\u057d\u057e\5\u0088E\2\u057e\u0103\3\2\2\2\u057f\u0580"+
		"\7}\2\2\u0580\u0581\5\u0088E\2\u0581\u0105\3\2\2\2\u0582\u0583\7`\2\2"+
		"\u0583\u0584\7a\2\2\u0584\u0585\5\u0102\u0082\2\u0585\u0107\3\2\2\2\u0586"+
		"\u0587\7J\2\2\u0587\u0588\7`\2\2\u0588\u0589\5\u0088E\2\u0589\u058a\7"+
		"a\2\2\u058a\u058b\5\u0102\u0082\2\u058b\u0109\3\2\2\2\u058c\u0592\7L\2"+
		"\2\u058d\u058e\7L\2\2\u058e\u0592\7\177\2\2\u058f\u0590\7\177\2\2\u0590"+
		"\u0592\7L\2\2\u0591\u058c\3\2\2\2\u0591\u058d\3\2\2\2\u0591\u058f\3\2"+
		"\2\2\u0592\u0593\3\2\2\2\u0593\u0594\5\u0102\u0082\2\u0594\u010b\3\2\2"+
		"\2\u0595\u0596\7D\2\2\u0596\u0597\5\u010e\u0088\2\u0597\u010d\3\2\2\2"+
		"\u0598\u0599\6\u0088\21\2\u0599\u059a\5\u0112\u008a\2\u059a\u059b\5\u0110"+
		"\u0089\2\u059b\u059e\3\2\2\2\u059c\u059e\5\u0112\u008a\2\u059d\u0598\3"+
		"\2\2\2\u059d\u059c\3\2\2\2\u059e\u010f\3\2\2\2\u059f\u05a2\5\u0112\u008a"+
		"\2\u05a0\u05a2\5\u0088E\2\u05a1\u059f\3\2\2\2\u05a1\u05a0\3\2\2\2\u05a2"+
		"\u0111\3\2\2\2\u05a3\u05af\7\\\2\2\u05a4\u05a9\5j\66\2\u05a5\u05a6\7c"+
		"\2\2\u05a6\u05a8\5j\66\2\u05a7\u05a5\3\2\2\2\u05a8\u05ab\3\2\2\2\u05a9"+
		"\u05a7\3\2\2\2\u05a9\u05aa\3\2\2\2\u05aa\u05ad\3\2\2\2\u05ab\u05a9\3\2"+
		"\2\2\u05ac\u05ae\7c\2\2\u05ad\u05ac\3\2\2\2\u05ad\u05ae\3\2\2\2\u05ae"+
		"\u05b0\3\2\2\2\u05af\u05a4\3\2\2\2\u05af\u05b0\3\2\2\2\u05b0\u05b1\3\2"+
		"\2\2\u05b1\u05b2\7]\2\2\u05b2\u0113\3\2\2\2\u05b3\u05b4\5\u0088E\2\u05b4"+
		"\u05b5\7\\\2\2\u05b5\u05b7\5p9\2\u05b6\u05b8\7c\2\2\u05b7\u05b6\3\2\2"+
		"\2\u05b7\u05b8\3\2\2\2\u05b8\u05b9\3\2\2\2\u05b9\u05ba\7]\2\2\u05ba\u0115"+
		"\3\2\2\2\u05bb\u05c2\5\u0118\u008d\2\u05bc\u05c2\5\u011c\u008f\2\u05bd"+
		"\u05be\7\\\2\2\u05be\u05bf\5p9\2\u05bf\u05c0\7]\2\2\u05c0\u05c2\3\2\2"+
		"\2\u05c1\u05bb\3\2\2\2\u05c1\u05bc\3\2\2\2\u05c1\u05bd\3\2\2\2\u05c2\u0117"+
		"\3\2\2\2\u05c3\u05c7\5|?\2\u05c4\u05c7\5\u0120\u0091\2\u05c5\u05c7\5\u0134"+
		"\u009b\2\u05c6\u05c3\3\2\2\2\u05c6\u05c4\3\2\2\2\u05c6\u05c5\3\2\2\2\u05c7"+
		"\u0119\3\2\2\2\u05c8\u05c9\t\22\2\2\u05c9\u011b\3\2\2\2\u05ca\u05cd\7"+
		"[\2\2\u05cb\u05cc\7f\2\2\u05cc\u05ce\7[\2\2\u05cd\u05cb\3\2\2\2\u05cd"+
		"\u05ce\3\2\2\2\u05ce\u011d\3\2\2\2\u05cf\u05d0\7[\2\2\u05d0\u05d1\7f\2"+
		"\2\u05d1\u05d2\7[\2\2\u05d2\u011f\3\2\2\2\u05d3\u05d4\5\u0090I\2\u05d4"+
		"\u05d5\5\u0122\u0092\2\u05d5\u0121\3\2\2\2\u05d6\u05db\7^\2\2\u05d7\u05d9"+
		"\5\u0124\u0093\2\u05d8\u05da\7c\2\2\u05d9\u05d8\3\2\2\2\u05d9\u05da\3"+
		"\2\2\2\u05da\u05dc\3\2\2\2\u05db\u05d7\3\2\2\2\u05db\u05dc\3\2\2\2\u05dc"+
		"\u05dd\3\2\2\2\u05dd\u05de\7_\2\2\u05de\u0123\3\2\2\2\u05df\u05e4\5\u0126"+
		"\u0094\2\u05e0\u05e1\7c\2\2\u05e1\u05e3\5\u0126\u0094\2\u05e2\u05e0\3"+
		"\2\2\2\u05e3\u05e6\3\2\2\2\u05e4\u05e2\3\2\2\2\u05e4\u05e5\3\2\2\2\u05e5"+
		"\u0125\3\2\2\2\u05e6\u05e4\3\2\2\2\u05e7\u05e8\5\u0128\u0095\2\u05e8\u05e9"+
		"\7e\2\2\u05e9\u05eb\3\2\2\2\u05ea\u05e7\3\2\2\2\u05ea\u05eb\3\2\2\2\u05eb"+
		"\u05ec\3\2\2\2\u05ec\u05ed\5\u012a\u0096\2\u05ed\u0127\3\2\2\2\u05ee\u05f2"+
		"\7[\2\2\u05ef\u05f2\5p9\2\u05f0\u05f2\5\u0122\u0092\2\u05f1\u05ee\3\2"+
		"\2\2\u05f1\u05ef\3\2\2\2\u05f1\u05f0\3\2\2\2\u05f2\u0129\3\2\2\2\u05f3"+
		"\u05f6\5p9\2\u05f4\u05f6\5\u0122\u0092\2\u05f5\u05f3\3\2\2\2\u05f5\u05f4"+
		"\3\2\2\2\u05f6\u012b\3\2\2\2\u05f7\u05f8\7K\2\2\u05f8\u05fe\7^\2\2\u05f9"+
		"\u05fa\5\u012e\u0098\2\u05fa\u05fb\5\u009eP\2\u05fb\u05fd\3\2\2\2\u05fc"+
		"\u05f9\3\2\2\2\u05fd\u0600\3\2\2\2\u05fe\u05fc\3\2\2\2\u05fe\u05ff\3\2"+
		"\2\2\u05ff\u0601\3\2\2\2\u0600\u05fe\3\2\2\2\u0601\u0602\7_\2\2\u0602"+
		"\u012d\3\2\2\2\u0603\u0604\6\u0098\22\2\u0604\u0605\5\u00aeX\2\u0605\u0606"+
		"\5\u0088E\2\u0606\u0609\3\2\2\2\u0607\u0609\5\u0132\u009a\2\u0608\u0603"+
		"\3\2\2\2\u0608\u0607\3\2\2\2\u0609\u060b\3\2\2\2\u060a\u060c\5\u0130\u0099"+
		"\2\u060b\u060a\3\2\2\2\u060b\u060c\3\2\2\2\u060c\u012f\3\2\2\2\u060d\u060e"+
		"\t\23\2\2\u060e\u0131\3\2\2\2\u060f\u0611\7}\2\2\u0610\u060f\3\2\2\2\u0610"+
		"\u0611\3\2\2\2\u0611\u0612\3\2\2\2\u0612\u0613\5\u00fc\177\2\u0613\u0133"+
		"\3\2\2\2\u0614\u0615\7D\2\2\u0615\u0616\5\u010e\u0088\2\u0616\u0617\5"+
		"\u00b8]\2\u0617\u0135\3\2\2\2\u0618\u0619\7`\2\2\u0619\u061a\5p9\2\u061a"+
		"\u061b\7a\2\2\u061b\u0137\3\2\2\2\u061c\u061d\7f\2\2\u061d\u061e\7\\\2"+
		"\2\u061e\u061f\5\u0088E\2\u061f\u0620\7]\2\2\u0620\u0139\3\2\2\2\u0621"+
		"\u0630\7\\\2\2\u0622\u0629\5\u00b0Y\2\u0623\u0626\5\u0088E\2\u0624\u0625"+
		"\7c\2\2\u0625\u0627\5\u00b0Y\2\u0626\u0624\3\2\2\2\u0626\u0627\3\2\2\2"+
		"\u0627\u0629\3\2\2\2\u0628\u0622\3\2\2\2\u0628\u0623\3\2\2\2\u0629\u062b"+
		"\3\2\2\2\u062a\u062c\7j\2\2\u062b\u062a\3\2\2\2\u062b\u062c\3\2\2\2\u062c"+
		"\u062e\3\2\2\2\u062d\u062f\7c\2\2\u062e\u062d\3\2\2\2\u062e\u062f\3\2"+
		"\2\2\u062f\u0631\3\2\2\2\u0630\u0628\3\2\2\2\u0630\u0631\3\2\2\2\u0631"+
		"\u0632\3\2\2\2\u0632\u0633\7]\2\2\u0633\u013b\3\2\2\2\u0634\u0635\5\u013e"+
		"\u00a0\2\u0635\u0636\7f\2\2\u0636\u0637\7[\2\2\u0637\u013d\3\2\2\2\u0638"+
		"\u0639\5\u0088E\2\u0639\u013f\3\2\2\2\u00a3\u014e\u0153\u015d\u0170\u0177"+
		"\u017b\u0182\u018a\u0193\u01b3\u01bb\u01cf\u01d1\u01e2\u01ee\u01f7\u0205"+
		"\u0217\u021e\u0225\u022a\u022d\u0237\u023a\u023e\u0242\u0249\u024c\u0259"+
		"\u0261\u0265\u0268\u026e\u0272\u0275\u027f\u0286\u028d\u029b\u02a3\u02ac"+
		"\u02b3\u02b9\u02c4\u02cb\u02cd\u02d3\u02df\u02e5\u02e9\u02f1\u02f5\u02fa"+
		"\u02fd\u0300\u030d\u0324\u0344\u0346\u035b\u0363\u036e\u0375\u037f\u0387"+
		"\u0393\u0397\u039c\u039f\u03a8\u03ae\u03b9\u03c1\u03c7\u03d0\u03db\u03e6"+
		"\u03ea\u03ec\u03f7\u0400\u0404\u0407\u040e\u0419\u0422\u042f\u0433\u0436"+
		"\u043f\u0449\u044d\u0451\u0455\u045c\u0464\u046f\u0473\u0477\u0483\u0487"+
		"\u048b\u0494\u049c\u04ae\u04c2\u04c6\u04ca\u04ce\u04da\u04e1\u04e3\u04e7"+
		"\u04eb\u04ee\u04f4\u04fc\u0501\u0505\u050c\u0513\u051e\u0523\u0527\u052c"+
		"\u0530\u0538\u0540\u0545\u0548\u0550\u0558\u055e\u0562\u056a\u0574\u0591"+
		"\u059d\u05a1\u05a9\u05ad\u05af\u05b7\u05c1\u05c6\u05cd\u05d9\u05db\u05e4"+
		"\u05ea\u05f1\u05f5\u05fe\u0608\u060b\u0610\u0626\u0628\u062b\u062e\u0630";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}