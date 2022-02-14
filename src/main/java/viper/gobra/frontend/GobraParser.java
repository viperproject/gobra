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
		SEQ=37, SET=38, MSET=39, DICT=40, OPT=41, LEN=42, NEW=43, MAKE=44, CAP=45, 
		SOME=46, GET=47, DOM=48, AXIOM=49, NONE=50, PRED=51, TYPE_OF=52, IS_COMPARABLE=53, 
		SHARE=54, ADDR_MOD=55, DOT_DOT=56, SHARED=57, EXCLUSIVE=58, PREDICATE=59, 
		WRITEPERM=60, NOPERM=61, TRUSTED=62, BREAK=63, DEFAULT=64, FUNC=65, INTERFACE=66, 
		SELECT=67, CASE=68, DEFER=69, GO=70, MAP=71, STRUCT=72, CHAN=73, ELSE=74, 
		GOTO=75, PACKAGE=76, SWITCH=77, CONST=78, FALLTHROUGH=79, IF=80, RANGE=81, 
		TYPE=82, CONTINUE=83, FOR=84, IMPORT=85, RETURN=86, VAR=87, NIL_LIT=88, 
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
		LINE_COMMENT=143, WS_NLSEMI=144, COMMENT_NLSEMI=145, LINE_COMMENT_NLSEMI=146, 
		EOS=147, OTHER=148;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_ghostStatement = 5, RULE_ghostPrimaryExpr = 6, 
		RULE_quantification = 7, RULE_permission = 8, RULE_typeExpr = 9, RULE_boundVariables = 10, 
		RULE_boundVariableDecl = 11, RULE_triggers = 12, RULE_trigger = 13, RULE_predicateAccess = 14, 
		RULE_optionSome = 15, RULE_optionNone = 16, RULE_optionGet = 17, RULE_sConversion = 18, 
		RULE_old = 19, RULE_oldLabelUse = 20, RULE_labelUse = 21, RULE_isComparable = 22, 
		RULE_typeOf = 23, RULE_access = 24, RULE_range = 25, RULE_seqUpdExp = 26, 
		RULE_seqUpdClause = 27, RULE_ghostTypeLit = 28, RULE_domainType = 29, 
		RULE_domainClause = 30, RULE_ghostSliceType = 31, RULE_sqType = 32, RULE_specification = 33, 
		RULE_specStatement = 34, RULE_terminationMeasure = 35, RULE_assertion = 36, 
		RULE_blockWithBodyParameterInfo = 37, RULE_implementationProof = 38, RULE_methodImplementationProof = 39, 
		RULE_nonLocalReceiver = 40, RULE_selection = 41, RULE_implementationProofPredicateAlias = 42, 
		RULE_make = 43, RULE_new_ = 44, RULE_functionDecl = 45, RULE_methodDecl = 46, 
		RULE_sourceFile = 47, RULE_ghostMember = 48, RULE_explicitGhostMember = 49, 
		RULE_fpredicateDecl = 50, RULE_predicateBody = 51, RULE_mpredicateDecl = 52, 
		RULE_varSpec = 53, RULE_shortVarDecl = 54, RULE_receiver = 55, RULE_parameterDecl = 56, 
		RULE_parameterType = 57, RULE_unfolding = 58, RULE_expression = 59, RULE_statement = 60, 
		RULE_applyStmt = 61, RULE_packageStmt = 62, RULE_specForStmt = 63, RULE_loopSpec = 64, 
		RULE_basicLit = 65, RULE_primaryExpr = 66, RULE_predConstructArgs = 67, 
		RULE_interfaceType = 68, RULE_predicateSpec = 69, RULE_methodSpec = 70, 
		RULE_type_ = 71, RULE_typeLit = 72, RULE_predType = 73, RULE_predTypeParams = 74, 
		RULE_literalType = 75, RULE_implicitArray = 76, RULE_slice_ = 77, RULE_low = 78, 
		RULE_high = 79, RULE_cap = 80, RULE_assign_op = 81, RULE_packageClause = 82, 
		RULE_importDecl = 83, RULE_importSpec = 84, RULE_importPath = 85, RULE_declaration = 86, 
		RULE_constDecl = 87, RULE_constSpec = 88, RULE_identifierList = 89, RULE_expressionList = 90, 
		RULE_typeDecl = 91, RULE_typeSpec = 92, RULE_varDecl = 93, RULE_block = 94, 
		RULE_statementList = 95, RULE_simpleStmt = 96, RULE_expressionStmt = 97, 
		RULE_sendStmt = 98, RULE_incDecStmt = 99, RULE_assignment = 100, RULE_emptyStmt = 101, 
		RULE_labeledStmt = 102, RULE_returnStmt = 103, RULE_breakStmt = 104, RULE_continueStmt = 105, 
		RULE_gotoStmt = 106, RULE_fallthroughStmt = 107, RULE_deferStmt = 108, 
		RULE_ifStmt = 109, RULE_switchStmt = 110, RULE_exprSwitchStmt = 111, RULE_exprCaseClause = 112, 
		RULE_exprSwitchCase = 113, RULE_typeSwitchStmt = 114, RULE_typeSwitchGuard = 115, 
		RULE_typeCaseClause = 116, RULE_typeSwitchCase = 117, RULE_typeList = 118, 
		RULE_selectStmt = 119, RULE_commClause = 120, RULE_commCase = 121, RULE_recvStmt = 122, 
		RULE_forStmt = 123, RULE_forClause = 124, RULE_rangeClause = 125, RULE_goStmt = 126, 
		RULE_typeName = 127, RULE_arrayType = 128, RULE_arrayLength = 129, RULE_elementType = 130, 
		RULE_pointerType = 131, RULE_sliceType = 132, RULE_mapType = 133, RULE_channelType = 134, 
		RULE_functionType = 135, RULE_signature = 136, RULE_result = 137, RULE_parameters = 138, 
		RULE_conversion = 139, RULE_nonNamedType = 140, RULE_operand = 141, RULE_literal = 142, 
		RULE_integer = 143, RULE_operandName = 144, RULE_qualifiedIdent = 145, 
		RULE_compositeLit = 146, RULE_literalValue = 147, RULE_elementList = 148, 
		RULE_keyedElement = 149, RULE_key = 150, RULE_element = 151, RULE_structType = 152, 
		RULE_fieldDecl = 153, RULE_string_ = 154, RULE_embeddedField = 155, RULE_functionLit = 156, 
		RULE_index = 157, RULE_typeAssertion = 158, RULE_arguments = 159, RULE_methodExpr = 160, 
		RULE_receiverType = 161, RULE_eos = 162;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "ghostStatement", "ghostPrimaryExpr", "quantification", 
			"permission", "typeExpr", "boundVariables", "boundVariableDecl", "triggers", 
			"trigger", "predicateAccess", "optionSome", "optionNone", "optionGet", 
			"sConversion", "old", "oldLabelUse", "labelUse", "isComparable", "typeOf", 
			"access", "range", "seqUpdExp", "seqUpdClause", "ghostTypeLit", "domainType", 
			"domainClause", "ghostSliceType", "sqType", "specification", "specStatement", 
			"terminationMeasure", "assertion", "blockWithBodyParameterInfo", "implementationProof", 
			"methodImplementationProof", "nonLocalReceiver", "selection", "implementationProofPredicateAlias", 
			"make", "new_", "functionDecl", "methodDecl", "sourceFile", "ghostMember", 
			"explicitGhostMember", "fpredicateDecl", "predicateBody", "mpredicateDecl", 
			"varSpec", "shortVarDecl", "receiver", "parameterDecl", "parameterType", 
			"unfolding", "expression", "statement", "applyStmt", "packageStmt", "specForStmt", 
			"loopSpec", "basicLit", "primaryExpr", "predConstructArgs", "interfaceType", 
			"predicateSpec", "methodSpec", "type_", "typeLit", "predType", "predTypeParams", 
			"literalType", "implicitArray", "slice_", "low", "high", "cap", "assign_op", 
			"packageClause", "importDecl", "importSpec", "importPath", "declaration", 
			"constDecl", "constSpec", "identifierList", "expressionList", "typeDecl", 
			"typeSpec", "varDecl", "block", "statementList", "simpleStmt", "expressionStmt", 
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
			"'decreases'", "'pure'", "'implements'", "'old'", "'#lhs'", "'forall'", 
			"'exists'", "'acc'", "'fold'", "'unfold'", "'unfolding'", "'ghost'", 
			"'in'", "'#'", "'subset'", "'union'", "'intersection'", "'setminus'", 
			"'==>'", "'--*'", "'apply'", "'?'", "'!<'", "'!>'", "'seq'", "'set'", 
			"'mset'", "'dict'", "'option'", "'len'", "'new'", "'make'", "'cap'", 
			"'some'", "'get'", "'domain'", "'axiom'", "'none'", "'pred'", "'typeOf'", 
			"'isComparable'", "'share'", "'@'", "'..'", "'shared'", "'exclusive'", 
			"'predicate'", "'writePerm'", "'noPerm'", "'trusted'", "'break'", "'default'", 
			"'func'", "'interface'", "'select'", "'case'", "'defer'", "'go'", "'map'", 
			"'struct'", "'chan'", "'else'", "'goto'", "'package'", "'switch'", "'const'", 
			"'fallthrough'", "'if'", "'range'", "'type'", "'continue'", "'for'", 
			"'import'", "'return'", "'var'", "'nil'", null, "'('", "')'", "'{'", 
			"'}'", "'['", "']'", "'='", "','", "';'", "':'", "'.'", "'++'", "'--'", 
			"':='", "'...'", "'||'", "'&&'", "'=='", "'!='", "'<'", "'<='", "'>'", 
			"'>='", "'|'", "'/'", "'%'", "'<<'", "'>>'", "'&^'", "'!'", "'+'", "'-'", 
			"'^'", "'*'", "'&'", "'<-'"
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
			"NOPERM", "TRUSTED", "BREAK", "DEFAULT", "FUNC", "INTERFACE", "SELECT", 
			"CASE", "DEFER", "GO", "MAP", "STRUCT", "CHAN", "ELSE", "GOTO", "PACKAGE", 
			"SWITCH", "CONST", "FALLTHROUGH", "IF", "RANGE", "TYPE", "CONTINUE", 
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
			setState(326);
			expression(0);
			setState(327);
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
			setState(329);
			statement();
			setState(330);
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
			setState(332);
			type_();
			setState(333);
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
			setState(335);
			maybeAddressableIdentifier();
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(336);
				match(COMMA);
				setState(337);
				maybeAddressableIdentifier();
				}
				}
				setState(342);
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
			setState(343);
			match(IDENTIFIER);
			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(344);
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
		enterRule(_localctx, 10, RULE_ghostStatement);
		int _la;
		try {
			setState(353);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				_localctx = new ExplicitGhostStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				match(GHOST);
				setState(348);
				statement();
				}
				break;
			case FOLD:
			case UNFOLD:
				_localctx = new FoldStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(349);
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
				setState(350);
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
				setState(351);
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
				setState(352);
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
		public QuantificationContext quantification() {
			return getRuleContext(QuantificationContext.class,0);
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
		enterRule(_localctx, 12, RULE_ghostPrimaryExpr);
		try {
			setState(367);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(355);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(356);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(357);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(358);
				typeExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(359);
				isComparable();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(360);
				old();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(361);
				sConversion();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(362);
				optionNone();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(363);
				optionSome();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(364);
				optionGet();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(365);
				quantification();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(366);
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

	public static class QuantificationContext extends ParserRuleContext {
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
		public QuantificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantification; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitQuantification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantificationContext quantification() throws RecognitionException {
		QuantificationContext _localctx = new QuantificationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_quantification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			_la = _input.LA(1);
			if ( !(_la==FORALL || _la==EXISTS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(370);
			boundVariables();
			setState(371);
			match(COLON);
			setState(372);
			match(COLON);
			setState(373);
			triggers();
			setState(374);
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
		enterRule(_localctx, 16, RULE_permission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
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
		enterRule(_localctx, 18, RULE_typeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			match(TYPE);
			setState(379);
			match(L_BRACKET);
			setState(380);
			type_();
			setState(381);
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
		enterRule(_localctx, 20, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			boundVariableDecl();
			setState(388);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(384);
					match(COMMA);
					setState(385);
					boundVariableDecl();
					}
					} 
				}
				setState(390);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(391);
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
		enterRule(_localctx, 22, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(394);
			match(IDENTIFIER);
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(395);
				match(COMMA);
				setState(396);
				match(IDENTIFIER);
				}
				}
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(402);
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
		enterRule(_localctx, 24, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(404);
				trigger();
				}
				}
				setState(409);
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
		enterRule(_localctx, 26, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			match(L_CURLY);
			setState(411);
			expression(0);
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(412);
				match(COMMA);
				setState(413);
				expression(0);
				}
				}
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(419);
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
		enterRule(_localctx, 28, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
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
		enterRule(_localctx, 30, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			match(SOME);
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
		enterRule(_localctx, 32, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			match(NONE);
			setState(429);
			match(L_BRACKET);
			setState(430);
			type_();
			setState(431);
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
		enterRule(_localctx, 34, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			match(GET);
			setState(434);
			match(L_PAREN);
			setState(435);
			expression(0);
			setState(436);
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
		enterRule(_localctx, 36, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
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
			setState(439);
			match(L_PAREN);
			setState(440);
			expression(0);
			setState(441);
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
		enterRule(_localctx, 38, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(443);
			match(OLD);
			setState(448);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(444);
				match(L_BRACKET);
				setState(445);
				oldLabelUse();
				setState(446);
				match(R_BRACKET);
				}
			}

			setState(450);
			match(L_PAREN);
			setState(451);
			expression(0);
			setState(452);
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
		enterRule(_localctx, 40, RULE_oldLabelUse);
		try {
			setState(456);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(454);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(455);
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
		enterRule(_localctx, 42, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
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
		enterRule(_localctx, 44, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(460);
			match(IS_COMPARABLE);
			setState(461);
			match(L_PAREN);
			setState(462);
			expression(0);
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
		enterRule(_localctx, 46, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			match(TYPE_OF);
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
		enterRule(_localctx, 48, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(ACCESS);
			setState(471);
			match(L_PAREN);
			setState(472);
			expression(0);
			setState(475);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(473);
				match(COMMA);
				setState(474);
				expression(0);
				}
			}

			setState(477);
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
		enterRule(_localctx, 50, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(479);
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
			setState(480);
			match(L_BRACKET);
			setState(481);
			expression(0);
			setState(482);
			match(DOT_DOT);
			setState(483);
			expression(0);
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
		enterRule(_localctx, 52, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			match(L_BRACKET);
			{
			setState(487);
			seqUpdClause();
			setState(492);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(488);
				match(COMMA);
				setState(489);
				seqUpdClause();
				}
				}
				setState(494);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(495);
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
		enterRule(_localctx, 54, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			expression(0);
			setState(498);
			match(ASSIGN);
			setState(499);
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
		enterRule(_localctx, 56, RULE_ghostTypeLit);
		try {
			setState(504);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(501);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(502);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(503);
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
		enterRule(_localctx, 58, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(506);
			match(DOM);
			setState(507);
			match(L_CURLY);
			setState(513);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(508);
				domainClause();
				setState(509);
				eos();
				}
				}
				setState(515);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(516);
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
		enterRule(_localctx, 60, RULE_domainClause);
		try {
			setState(527);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(518);
				match(FUNC);
				setState(519);
				match(IDENTIFIER);
				setState(520);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(521);
				match(AXIOM);
				setState(522);
				match(L_CURLY);
				setState(523);
				expression(0);
				setState(524);
				eos();
				setState(525);
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
		enterRule(_localctx, 62, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529);
			match(GHOST);
			setState(530);
			match(L_BRACKET);
			setState(531);
			match(R_BRACKET);
			setState(532);
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
		enterRule(_localctx, 64, RULE_sqType);
		int _la;
		try {
			setState(545);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(534);
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
				setState(535);
				match(L_BRACKET);
				setState(536);
				type_();
				setState(537);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(539);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(540);
				match(L_BRACKET);
				setState(541);
				type_();
				setState(542);
				match(R_BRACKET);
				setState(543);
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
		enterRule(_localctx, 66, RULE_specification);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(550);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
						{
						setState(547);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(548);
						match(PURE);
						}
						break;
					case TRUSTED:
						{
						setState(549);
						match(TRUSTED);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(552);
					eos();
					}
					} 
				}
				setState(557);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			setState(559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(558);
				match(PURE);
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
		enterRule(_localctx, 68, RULE_specStatement);
		try {
			setState(569);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(561);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(562);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(563);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(564);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(565);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(566);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(567);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(568);
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
		enterRule(_localctx, 70, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(572);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(571);
				expressionList();
				}
				break;
			}
			setState(576);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(574);
				match(IF);
				setState(575);
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
		enterRule(_localctx, 72, RULE_assertion);
		try {
			setState(580);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(579);
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
		enterRule(_localctx, 74, RULE_blockWithBodyParameterInfo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582);
			match(L_CURLY);
			setState(587);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(583);
				match(SHARE);
				setState(584);
				identifierList();
				setState(585);
				eos();
				}
				break;
			}
			setState(590);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(589);
				statementList();
				}
				break;
			}
			setState(592);
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
		enterRule(_localctx, 76, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
			type_();
			setState(595);
			match(IMPL);
			setState(596);
			type_();
			setState(615);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(597);
				match(L_CURLY);
				setState(603);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(598);
					implementationProofPredicateAlias();
					setState(599);
					eos();
					}
					}
					setState(605);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(611);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(606);
					methodImplementationProof();
					setState(607);
					eos();
					}
					}
					setState(613);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(614);
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
		enterRule(_localctx, 78, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(618);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(617);
				match(PURE);
				}
			}

			setState(620);
			nonLocalReceiver();
			setState(621);
			match(IDENTIFIER);
			setState(622);
			signature();
			setState(624);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(623);
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
		enterRule(_localctx, 80, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(626);
			match(L_PAREN);
			setState(628);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(627);
				match(IDENTIFIER);
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
			typeName();
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
		enterRule(_localctx, 82, RULE_selection);
		try {
			setState(641);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(636);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(637);
				type_();
				setState(638);
				match(DOT);
				setState(639);
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
		enterRule(_localctx, 84, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			match(PRED);
			setState(644);
			match(IDENTIFIER);
			setState(645);
			match(DECLARE_ASSIGN);
			setState(648);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(646);
				selection();
				}
				break;
			case 2:
				{
				setState(647);
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
		enterRule(_localctx, 86, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
			match(MAKE);
			setState(651);
			match(L_PAREN);
			setState(652);
			type_();
			setState(655);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(653);
				match(COMMA);
				setState(654);
				expressionList();
				}
			}

			setState(657);
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
		enterRule(_localctx, 88, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(659);
			match(NEW);
			setState(660);
			match(L_PAREN);
			setState(661);
			type_();
			setState(662);
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
		enterRule(_localctx, 90, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(664);
			specification();
			setState(665);
			match(FUNC);
			setState(666);
			match(IDENTIFIER);
			{
			setState(667);
			signature();
			setState(669);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(668);
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
		enterRule(_localctx, 92, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(671);
			specification();
			setState(672);
			match(FUNC);
			setState(673);
			receiver();
			setState(674);
			match(IDENTIFIER);
			{
			setState(675);
			signature();
			setState(677);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				setState(676);
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
		enterRule(_localctx, 94, RULE_sourceFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(679);
			packageClause();
			setState(680);
			eos();
			setState(686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(681);
				importDecl();
				setState(682);
				eos();
				}
				}
				setState(688);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(699);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED) | (1L << TRUSTED))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (CONST - 65)) | (1L << (TYPE - 65)) | (1L << (VAR - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (STAR - 65)) | (1L << (RECEIVE - 65)))) != 0)) {
				{
				{
				setState(693);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
				case 1:
					{
					setState(689);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(690);
					methodDecl();
					}
					break;
				case 3:
					{
					setState(691);
					declaration();
					}
					break;
				case 4:
					{
					setState(692);
					ghostMember();
					}
					break;
				}
				setState(695);
				eos();
				}
				}
				setState(701);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(702);
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
		enterRule(_localctx, 96, RULE_ghostMember);
		try {
			setState(708);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(704);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(705);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(706);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(707);
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

	public static class ExplicitGhostMemberContext extends ParserRuleContext {
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public MethodDeclContext methodDecl() {
			return getRuleContext(MethodDeclContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
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
		enterRule(_localctx, 98, RULE_explicitGhostMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(710);
			match(GHOST);
			setState(714);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(711);
				methodDecl();
				}
				break;
			case 2:
				{
				setState(712);
				functionDecl();
				}
				break;
			case 3:
				{
				setState(713);
				declaration();
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
		enterRule(_localctx, 100, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			match(PRED);
			setState(717);
			match(IDENTIFIER);
			setState(718);
			parameters();
			setState(720);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(719);
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
		enterRule(_localctx, 102, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(722);
			match(L_CURLY);
			setState(723);
			expression(0);
			setState(724);
			eos();
			setState(725);
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
		enterRule(_localctx, 104, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(727);
			match(PRED);
			setState(728);
			receiver();
			setState(729);
			match(IDENTIFIER);
			setState(730);
			parameters();
			setState(732);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(731);
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
		enterRule(_localctx, 106, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(734);
			maybeAddressableIdentifierList();
			setState(742);
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
				setState(735);
				type_();
				setState(738);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(736);
					match(ASSIGN);
					setState(737);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(740);
				match(ASSIGN);
				setState(741);
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
		enterRule(_localctx, 108, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(744);
			maybeAddressableIdentifierList();
			setState(745);
			match(DECLARE_ASSIGN);
			setState(746);
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
		enterRule(_localctx, 110, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(748);
			match(L_PAREN);
			setState(750);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(749);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(752);
			type_();
			setState(754);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(753);
				match(COMMA);
				}
			}

			setState(756);
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
		public ParameterTypeContext parameterType() {
			return getRuleContext(ParameterTypeContext.class,0);
		}
		public TerminalNode GHOST() { return getToken(GobraParser.GHOST, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
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
		enterRule(_localctx, 112, RULE_parameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(759);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(758);
				match(GHOST);
				}
				break;
			}
			setState(762);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(761);
				identifierList();
				}
				break;
			}
			setState(764);
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
		enterRule(_localctx, 114, RULE_parameterType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(767);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(766);
				match(ELLIPSIS);
				}
			}

			setState(769);
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
		enterRule(_localctx, 116, RULE_unfolding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(771);
			match(UNFOLDING);
			setState(772);
			predicateAccess();
			setState(773);
			match(IN);
			setState(774);
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
		int _startState = 118;
		enterRecursionRule(_localctx, 118, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(787);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(777);
				((ExpressionContext)_localctx).call_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 42)) & ~0x3f) == 0 && ((1L << (_la - 42)) & ((1L << (LEN - 42)) | (1L << (CAP - 42)) | (1L << (DOM - 42)) | (1L << (RANGE - 42)))) != 0)) ) {
					((ExpressionContext)_localctx).call_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(778);
				match(L_PAREN);
				setState(779);
				expression(0);
				setState(780);
				match(R_PAREN);
				}
				break;
			case 2:
				{
				setState(782);
				unfolding();
				}
				break;
			case 3:
				{
				setState(783);
				make();
				}
				break;
			case 4:
				{
				setState(784);
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
				setState(785);
				expression(11);
				}
				break;
			case 5:
				{
				setState(786);
				primaryExpr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(821);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(819);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(789);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(790);
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
						setState(791);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(792);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(793);
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
						setState(794);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(795);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(796);
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
						setState(797);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(798);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(799);
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
						setState(800);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(801);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(802);
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
						setState(803);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(804);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(805);
						match(LOGICAL_AND);
						setState(806);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(807);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(808);
						match(LOGICAL_OR);
						setState(809);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(810);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(811);
						match(IMPLIES);
						setState(812);
						expression(2);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(813);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(814);
						match(QMARK);
						setState(815);
						expression(0);
						setState(816);
						match(COLON);
						setState(817);
						expression(1);
						}
						break;
					}
					} 
				}
				setState(823);
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
		enterRule(_localctx, 120, RULE_statement);
		try {
			setState(842);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(824);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(825);
				packageStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(826);
				applyStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(827);
				declaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(828);
				labeledStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(829);
				simpleStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(830);
				goStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(831);
				returnStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(832);
				breakStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(833);
				continueStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(834);
				gotoStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(835);
				fallthroughStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(836);
				block();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(837);
				ifStmt();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(838);
				switchStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(839);
				selectStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(840);
				specForStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(841);
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
		enterRule(_localctx, 122, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			match(APPLY);
			setState(845);
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
		enterRule(_localctx, 124, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(847);
			match(PACKAGE);
			setState(848);
			expression(0);
			setState(850);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(849);
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
		enterRule(_localctx, 126, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(852);
			loopSpec();
			setState(853);
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
		enterRule(_localctx, 128, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(861);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(855);
				match(INV);
				setState(856);
				expression(0);
				setState(857);
				eos();
				}
				}
				setState(863);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(868);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(864);
				match(DEC);
				setState(865);
				terminationMeasure();
				setState(866);
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
		enterRule(_localctx, 130, RULE_basicLit);
		try {
			setState(878);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(870);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(871);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(872);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(873);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(874);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(875);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(876);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(877);
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
		int _startState = 132;
		enterRecursionRule(_localctx, 132, RULE_primaryExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(887);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				_localctx = new OperandPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(881);
				operand();
				}
				break;
			case 2:
				{
				_localctx = new ConversionPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(882);
				conversion();
				}
				break;
			case 3:
				{
				_localctx = new MethodPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(883);
				methodExpr();
				}
				break;
			case 4:
				{
				_localctx = new GhostPrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(884);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(885);
				new_();
				}
				break;
			case 6:
				{
				_localctx = new MakeExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(886);
				make();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(906);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(904);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(889);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(890);
						match(DOT);
						setState(891);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new IndexPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(892);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(893);
						index();
						}
						break;
					case 3:
						{
						_localctx = new SlicePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(894);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(895);
						slice_();
						}
						break;
					case 4:
						{
						_localctx = new SeqUpdPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(896);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(897);
						seqUpdExp();
						}
						break;
					case 5:
						{
						_localctx = new TypeAssertionPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(898);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(899);
						typeAssertion();
						}
						break;
					case 6:
						{
						_localctx = new InvokePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(900);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(901);
						arguments();
						}
						break;
					case 7:
						{
						_localctx = new PredConstrPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(902);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(903);
						predConstructArgs();
						}
						break;
					}
					} 
				}
				setState(908);
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
		enterRule(_localctx, 134, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(909);
			match(L_PRED);
			setState(911);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
				{
				setState(910);
				expressionList();
				}
			}

			setState(914);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(913);
				match(COMMA);
				}
			}

			setState(916);
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
		enterRule(_localctx, 136, RULE_interfaceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918);
			match(INTERFACE);
			setState(919);
			match(L_CURLY);
			setState(929);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << PRED) | (1L << TRUSTED))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(923);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
				case 1:
					{
					setState(920);
					methodSpec();
					}
					break;
				case 2:
					{
					setState(921);
					typeName();
					}
					break;
				case 3:
					{
					setState(922);
					predicateSpec();
					}
					break;
				}
				setState(925);
				eos();
				}
				}
				setState(931);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(932);
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
		enterRule(_localctx, 138, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			match(PRED);
			setState(935);
			match(IDENTIFIER);
			setState(936);
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
		enterRule(_localctx, 140, RULE_methodSpec);
		int _la;
		try {
			setState(953);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(939);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(938);
					match(GHOST);
					}
				}

				setState(941);
				specification();
				setState(942);
				match(IDENTIFIER);
				setState(943);
				parameters();
				setState(944);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(947);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(946);
					match(GHOST);
					}
				}

				setState(949);
				specification();
				setState(950);
				match(IDENTIFIER);
				setState(951);
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
		enterRule(_localctx, 142, RULE_type_);
		try {
			setState(962);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(955);
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
				setState(956);
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
				setState(957);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(958);
				match(L_PAREN);
				setState(959);
				type_();
				setState(960);
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
		enterRule(_localctx, 144, RULE_typeLit);
		try {
			setState(973);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(964);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(965);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(966);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(967);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(968);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(969);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(970);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(971);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(972);
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
		enterRule(_localctx, 146, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(975);
			match(PRED);
			setState(976);
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
		enterRule(_localctx, 148, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(978);
			match(L_PAREN);
			setState(990);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (STAR - 65)) | (1L << (RECEIVE - 65)))) != 0)) {
				{
				setState(979);
				type_();
				setState(984);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(980);
						match(COMMA);
						setState(981);
						type_();
						}
						} 
					}
					setState(986);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
				}
				setState(988);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(987);
					match(COMMA);
					}
				}

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
		enterRule(_localctx, 150, RULE_literalType);
		try {
			setState(1001);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(995);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(996);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(997);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(998);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(999);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1000);
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
		enterRule(_localctx, 152, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1003);
			match(L_BRACKET);
			setState(1004);
			match(ELLIPSIS);
			setState(1005);
			match(R_BRACKET);
			setState(1006);
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
		enterRule(_localctx, 154, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1008);
			match(L_BRACKET);
			setState(1024);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				{
				setState(1010);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
					{
					setState(1009);
					low();
					}
				}

				setState(1012);
				match(COLON);
				setState(1014);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
					{
					setState(1013);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1017);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
					{
					setState(1016);
					low();
					}
				}

				setState(1019);
				match(COLON);
				setState(1020);
				high();
				setState(1021);
				match(COLON);
				setState(1022);
				cap();
				}
				break;
			}
			setState(1026);
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
		enterRule(_localctx, 156, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1028);
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
		enterRule(_localctx, 158, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1030);
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
		enterRule(_localctx, 160, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1032);
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
		enterRule(_localctx, 162, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1035);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 113)) & ~0x3f) == 0 && ((1L << (_la - 113)) & ((1L << (OR - 113)) | (1L << (DIV - 113)) | (1L << (MOD - 113)) | (1L << (LSHIFT - 113)) | (1L << (RSHIFT - 113)) | (1L << (BIT_CLEAR - 113)) | (1L << (PLUS - 113)) | (1L << (MINUS - 113)) | (1L << (CARET - 113)) | (1L << (STAR - 113)) | (1L << (AMPERSAND - 113)))) != 0)) {
				{
				setState(1034);
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

			setState(1037);
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
		enterRule(_localctx, 164, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039);
			match(PACKAGE);
			setState(1040);
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
		enterRule(_localctx, 166, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1042);
			match(IMPORT);
			setState(1054);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DOT:
			case RAW_STRING_LIT:
			case INTERPRETED_STRING_LIT:
				{
				setState(1043);
				importSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1044);
				match(L_PAREN);
				setState(1050);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 89)) & ~0x3f) == 0 && ((1L << (_la - 89)) & ((1L << (IDENTIFIER - 89)) | (1L << (DOT - 89)) | (1L << (RAW_STRING_LIT - 89)) | (1L << (INTERPRETED_STRING_LIT - 89)))) != 0)) {
					{
					{
					setState(1045);
					importSpec();
					setState(1046);
					eos();
					}
					}
					setState(1052);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1053);
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
		enterRule(_localctx, 168, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(1056);
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

			setState(1059);
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
		enterRule(_localctx, 170, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1061);
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
		enterRule(_localctx, 172, RULE_declaration);
		try {
			setState(1066);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1063);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1064);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1065);
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
		enterRule(_localctx, 174, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1068);
			match(CONST);
			setState(1080);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1069);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1070);
				match(L_PAREN);
				setState(1076);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1071);
					constSpec();
					setState(1072);
					eos();
					}
					}
					setState(1078);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1079);
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
		enterRule(_localctx, 176, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1082);
			identifierList();
			setState(1088);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(1084);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (STAR - 65)) | (1L << (RECEIVE - 65)))) != 0)) {
					{
					setState(1083);
					type_();
					}
				}

				setState(1086);
				match(ASSIGN);
				setState(1087);
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
		enterRule(_localctx, 178, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1090);
			match(IDENTIFIER);
			setState(1095);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,89,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1091);
					match(COMMA);
					setState(1092);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1097);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,89,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 180, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1098);
			expression(0);
			setState(1103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1099);
					match(COMMA);
					setState(1100);
					expression(0);
					}
					} 
				}
				setState(1105);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 182, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1106);
			match(TYPE);
			setState(1118);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1107);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1108);
				match(L_PAREN);
				setState(1114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1109);
					typeSpec();
					setState(1110);
					eos();
					}
					}
					setState(1116);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1117);
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
		enterRule(_localctx, 184, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1120);
			match(IDENTIFIER);
			setState(1122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1121);
				match(ASSIGN);
				}
			}

			setState(1124);
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
		enterRule(_localctx, 186, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1126);
			match(VAR);
			setState(1138);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1127);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1128);
				match(L_PAREN);
				setState(1134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1129);
					varSpec();
					setState(1130);
					eos();
					}
					}
					setState(1136);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1137);
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
		enterRule(_localctx, 188, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1140);
			match(L_CURLY);
			setState(1142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				{
				setState(1141);
				statementList();
				}
				break;
			}
			setState(1144);
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
		enterRule(_localctx, 190, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1152); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1147);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
					case 1:
						{
						setState(1146);
						eos();
						}
						break;
					}
					setState(1149);
					statement();
					setState(1150);
					eos();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1154); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
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
		enterRule(_localctx, 192, RULE_simpleStmt);
		try {
			setState(1161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1156);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1157);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1158);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1159);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1160);
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
		enterRule(_localctx, 194, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1163);
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
		enterRule(_localctx, 196, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1165);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1166);
			match(RECEIVE);
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
		enterRule(_localctx, 198, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1169);
			expression(0);
			setState(1170);
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
		enterRule(_localctx, 200, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1172);
			expressionList();
			setState(1173);
			assign_op();
			setState(1174);
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
		enterRule(_localctx, 202, RULE_emptyStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1176);
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
		enterRule(_localctx, 204, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1178);
			match(IDENTIFIER);
			setState(1179);
			match(COLON);
			setState(1181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				{
				setState(1180);
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
		enterRule(_localctx, 206, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1183);
			match(RETURN);
			setState(1185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				{
				setState(1184);
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
		enterRule(_localctx, 208, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1187);
			match(BREAK);
			setState(1189);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				{
				setState(1188);
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
		enterRule(_localctx, 210, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1191);
			match(CONTINUE);
			setState(1193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
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
		enterRule(_localctx, 212, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1195);
			match(GOTO);
			setState(1196);
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
		enterRule(_localctx, 214, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1198);
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
		enterRule(_localctx, 216, RULE_deferStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1200);
			match(DEFER);
			setState(1201);
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
		enterRule(_localctx, 218, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1203);
			match(IF);
			setState(1212);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				{
				setState(1204);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1205);
				eos();
				setState(1206);
				expression(0);
				}
				break;
			case 3:
				{
				setState(1208);
				simpleStmt();
				setState(1209);
				eos();
				setState(1210);
				expression(0);
				}
				break;
			}
			setState(1214);
			block();
			setState(1220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1215);
				match(ELSE);
				setState(1218);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1216);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1217);
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
		enterRule(_localctx, 220, RULE_switchStmt);
		try {
			setState(1224);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1222);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1223);
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
		enterRule(_localctx, 222, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1226);
			match(SWITCH);
			setState(1237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1228);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
					{
					setState(1227);
					expression(0);
					}
				}

				}
				break;
			case 2:
				{
				setState(1231);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
				case 1:
					{
					setState(1230);
					simpleStmt();
					}
					break;
				}
				setState(1233);
				eos();
				setState(1235);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
					{
					setState(1234);
					expression(0);
					}
				}

				}
				break;
			}
			setState(1239);
			match(L_CURLY);
			setState(1243);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1240);
				exprCaseClause();
				}
				}
				setState(1245);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1246);
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
		enterRule(_localctx, 224, RULE_exprCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1248);
			exprSwitchCase();
			setState(1249);
			match(COLON);
			setState(1251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				{
				setState(1250);
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
		enterRule(_localctx, 226, RULE_exprSwitchCase);
		try {
			setState(1256);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1253);
				match(CASE);
				setState(1254);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1255);
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
		enterRule(_localctx, 228, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1258);
			match(SWITCH);
			setState(1267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				{
				setState(1259);
				typeSwitchGuard();
				}
				break;
			case 2:
				{
				setState(1260);
				eos();
				setState(1261);
				typeSwitchGuard();
				}
				break;
			case 3:
				{
				setState(1263);
				simpleStmt();
				setState(1264);
				eos();
				setState(1265);
				typeSwitchGuard();
				}
				break;
			}
			setState(1269);
			match(L_CURLY);
			setState(1273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1270);
				typeCaseClause();
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
		enterRule(_localctx, 230, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				{
				setState(1278);
				match(IDENTIFIER);
				setState(1279);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1282);
			primaryExpr(0);
			setState(1283);
			match(DOT);
			setState(1284);
			match(L_PAREN);
			setState(1285);
			match(TYPE);
			setState(1286);
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
		enterRule(_localctx, 232, RULE_typeCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1288);
			typeSwitchCase();
			setState(1289);
			match(COLON);
			setState(1291);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1290);
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
		enterRule(_localctx, 234, RULE_typeSwitchCase);
		try {
			setState(1296);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1293);
				match(CASE);
				setState(1294);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1295);
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
		enterRule(_localctx, 236, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1300);
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
				setState(1298);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1299);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1302);
				match(COMMA);
				setState(1305);
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
					setState(1303);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1304);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1311);
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
		enterRule(_localctx, 238, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1312);
			match(SELECT);
			setState(1313);
			match(L_CURLY);
			setState(1317);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1314);
				commClause();
				}
				}
				setState(1319);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1320);
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
		enterRule(_localctx, 240, RULE_commClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1322);
			commCase();
			setState(1323);
			match(COLON);
			setState(1325);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1324);
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
		enterRule(_localctx, 242, RULE_commCase);
		try {
			setState(1333);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1327);
				match(CASE);
				setState(1330);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
				case 1:
					{
					setState(1328);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1329);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1332);
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
		enterRule(_localctx, 244, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				{
				setState(1335);
				expressionList();
				setState(1336);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1338);
				identifierList();
				setState(1339);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1343);
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
		enterRule(_localctx, 246, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1345);
			match(FOR);
			setState(1349);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				{
				setState(1346);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1347);
				forClause();
				}
				break;
			case 3:
				{
				setState(1348);
				rangeClause();
				}
				break;
			}
			setState(1351);
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
		enterRule(_localctx, 248, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1354);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				{
				setState(1353);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1356);
			eos();
			setState(1358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1357);
				expression(0);
				}
				break;
			}
			setState(1360);
			eos();
			setState(1362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
				{
				setState(1361);
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
		enterRule(_localctx, 250, RULE_rangeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
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
			match(RANGE);
			setState(1373);
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
		enterRule(_localctx, 252, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1375);
			match(GO);
			setState(1376);
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
		enterRule(_localctx, 254, RULE_typeName);
		try {
			setState(1380);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1378);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1379);
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
		enterRule(_localctx, 256, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1382);
			match(L_BRACKET);
			setState(1383);
			arrayLength();
			setState(1384);
			match(R_BRACKET);
			setState(1385);
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
		enterRule(_localctx, 258, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
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
		enterRule(_localctx, 260, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1389);
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
		enterRule(_localctx, 262, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1391);
			match(STAR);
			setState(1392);
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
		enterRule(_localctx, 264, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1394);
			match(L_BRACKET);
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
		enterRule(_localctx, 266, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1398);
			match(MAP);
			setState(1399);
			match(L_BRACKET);
			setState(1400);
			type_();
			setState(1401);
			match(R_BRACKET);
			setState(1402);
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
		enterRule(_localctx, 268, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1404);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1405);
				match(CHAN);
				setState(1406);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1407);
				match(RECEIVE);
				setState(1408);
				match(CHAN);
				}
				break;
			}
			setState(1411);
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
		enterRule(_localctx, 270, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1413);
			match(FUNC);
			setState(1414);
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
		enterRule(_localctx, 272, RULE_signature);
		try {
			setState(1420);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1416);
				parameters();
				setState(1417);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1419);
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
		enterRule(_localctx, 274, RULE_result);
		try {
			setState(1424);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1422);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1423);
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
		enterRule(_localctx, 276, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1426);
			match(L_PAREN);
			setState(1438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << PRED))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (ELLIPSIS - 65)) | (1L << (STAR - 65)) | (1L << (RECEIVE - 65)))) != 0)) {
				{
				setState(1427);
				parameterDecl();
				setState(1432);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,137,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1428);
						match(COMMA);
						setState(1429);
						parameterDecl();
						}
						} 
					}
					setState(1434);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,137,_ctx);
				}
				setState(1436);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1435);
					match(COMMA);
					}
				}

				}
			}

			setState(1440);
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
		enterRule(_localctx, 278, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1442);
			nonNamedType();
			setState(1443);
			match(L_PAREN);
			setState(1444);
			expression(0);
			setState(1446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1445);
				match(COMMA);
				}
			}

			setState(1448);
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
		enterRule(_localctx, 280, RULE_nonNamedType);
		try {
			setState(1455);
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
				setState(1450);
				typeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1451);
				match(L_PAREN);
				setState(1452);
				nonNamedType();
				setState(1453);
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
		enterRule(_localctx, 282, RULE_operand);
		try {
			setState(1463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1457);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1458);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1459);
				match(L_PAREN);
				setState(1460);
				expression(0);
				setState(1461);
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
		enterRule(_localctx, 284, RULE_literal);
		try {
			setState(1468);
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
				setState(1465);
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
				setState(1466);
				compositeLit();
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1467);
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
		enterRule(_localctx, 286, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1470);
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
		enterRule(_localctx, 288, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1472);
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
		enterRule(_localctx, 290, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1474);
			match(IDENTIFIER);
			setState(1475);
			match(DOT);
			setState(1476);
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
		enterRule(_localctx, 292, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1478);
			literalType();
			setState(1479);
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
		enterRule(_localctx, 294, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1481);
			match(L_CURLY);
			setState(1486);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_CURLY - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
				{
				setState(1482);
				elementList();
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
		enterRule(_localctx, 296, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1490);
			keyedElement();
			setState(1495);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,146,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1491);
					match(COMMA);
					setState(1492);
					keyedElement();
					}
					} 
				}
				setState(1497);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,146,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 298, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1501);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
			case 1:
				{
				setState(1498);
				key();
				setState(1499);
				match(COLON);
				}
				break;
			}
			setState(1503);
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
		enterRule(_localctx, 300, RULE_key);
		try {
			setState(1507);
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
				setState(1505);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1506);
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
		enterRule(_localctx, 302, RULE_element);
		try {
			setState(1511);
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
				setState(1509);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1510);
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
		enterRule(_localctx, 304, RULE_structType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1513);
			match(STRUCT);
			setState(1514);
			match(L_CURLY);
			setState(1520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(1515);
				fieldDecl();
				setState(1516);
				eos();
				}
				}
				setState(1522);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1523);
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
		enterRule(_localctx, 306, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1529);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,151,_ctx) ) {
			case 1:
				{
				setState(1525);
				identifierList();
				setState(1526);
				type_();
				}
				break;
			case 2:
				{
				setState(1528);
				embeddedField();
				}
				break;
			}
			setState(1532);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1531);
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
		enterRule(_localctx, 308, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1534);
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
		enterRule(_localctx, 310, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1537);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1536);
				match(STAR);
				}
			}

			setState(1539);
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
		enterRule(_localctx, 312, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1541);
			match(FUNC);
			setState(1542);
			signature();
			setState(1543);
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
		enterRule(_localctx, 314, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1545);
			match(L_BRACKET);
			setState(1546);
			expression(0);
			setState(1547);
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
		enterRule(_localctx, 316, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1549);
			match(DOT);
			setState(1550);
			match(L_PAREN);
			setState(1551);
			type_();
			setState(1552);
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
		enterRule(_localctx, 318, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1554);
			match(L_PAREN);
			setState(1569);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << OLD) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE) | (1L << WRITEPERM) | (1L << NOPERM))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (FUNC - 65)) | (1L << (INTERFACE - 65)) | (1L << (MAP - 65)) | (1L << (STRUCT - 65)) | (1L << (CHAN - 65)) | (1L << (RANGE - 65)) | (1L << (TYPE - 65)) | (1L << (NIL_LIT - 65)) | (1L << (IDENTIFIER - 65)) | (1L << (L_PAREN - 65)) | (1L << (L_BRACKET - 65)) | (1L << (EXCLAMATION - 65)) | (1L << (PLUS - 65)) | (1L << (MINUS - 65)) | (1L << (CARET - 65)) | (1L << (STAR - 65)) | (1L << (AMPERSAND - 65)) | (1L << (RECEIVE - 65)) | (1L << (DECIMAL_LIT - 65)) | (1L << (BINARY_LIT - 65)) | (1L << (OCTAL_LIT - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (HEX_LIT - 129)) | (1L << (IMAGINARY_LIT - 129)) | (1L << (RUNE_LIT - 129)) | (1L << (RAW_STRING_LIT - 129)) | (1L << (INTERPRETED_STRING_LIT - 129)))) != 0)) {
				{
				setState(1561);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
				case 1:
					{
					setState(1555);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1556);
					nonNamedType();
					setState(1559);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
					case 1:
						{
						setState(1557);
						match(COMMA);
						setState(1558);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1564);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1563);
					match(ELLIPSIS);
					}
				}

				setState(1567);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1566);
					match(COMMA);
					}
				}

				}
			}

			setState(1571);
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
		enterRule(_localctx, 320, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1573);
			nonNamedType();
			setState(1574);
			match(DOT);
			setState(1575);
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
		enterRule(_localctx, 322, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1577);
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
		enterRule(_localctx, 324, RULE_eos);
		try {
			setState(1583);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1579);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1580);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1581);
				match(EOS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1582);
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
		case 59:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 66:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 162:
			return eos_sempred((EosContext)_localctx, predIndex);
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
			return precpred(_ctx, 7);
		case 10:
			return precpred(_ctx, 6);
		case 11:
			return precpred(_ctx, 5);
		case 12:
			return precpred(_ctx, 4);
		case 13:
			return precpred(_ctx, 3);
		case 14:
			return precpred(_ctx, 2);
		case 15:
			return precpred(_ctx, 1);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0096\u0634\4\2\t"+
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
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u0155\n\5\f\5\16"+
		"\5\u0158\13\5\3\6\3\6\5\6\u015c\n\6\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u0164"+
		"\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u0172\n\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f"+
		"\7\f\u0185\n\f\f\f\16\f\u0188\13\f\3\f\5\f\u018b\n\f\3\r\3\r\3\r\7\r\u0190"+
		"\n\r\f\r\16\r\u0193\13\r\3\r\3\r\3\16\7\16\u0198\n\16\f\16\16\16\u019b"+
		"\13\16\3\17\3\17\3\17\3\17\7\17\u01a1\n\17\f\17\16\17\u01a4\13\17\3\17"+
		"\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25"+
		"\5\25\u01c3\n\25\3\25\3\25\3\25\3\25\3\26\3\26\5\26\u01cb\n\26\3\27\3"+
		"\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3"+
		"\32\3\32\5\32\u01de\n\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\34\7\34\u01ed\n\34\f\34\16\34\u01f0\13\34\3\34\3\34"+
		"\3\35\3\35\3\35\3\35\3\36\3\36\3\36\5\36\u01fb\n\36\3\37\3\37\3\37\3\37"+
		"\3\37\7\37\u0202\n\37\f\37\16\37\u0205\13\37\3\37\3\37\3 \3 \3 \3 \3 "+
		"\3 \3 \3 \3 \5 \u0212\n \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"\"\3\"\3\"\3\"\5\"\u0224\n\"\3#\3#\3#\5#\u0229\n#\3#\7#\u022c\n#\f#\16"+
		"#\u022f\13#\3#\5#\u0232\n#\3$\3$\3$\3$\3$\3$\3$\3$\5$\u023c\n$\3%\5%\u023f"+
		"\n%\3%\3%\5%\u0243\n%\3&\3&\5&\u0247\n&\3\'\3\'\3\'\3\'\3\'\5\'\u024e"+
		"\n\'\3\'\5\'\u0251\n\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\7(\u025c\n(\f(\16"+
		"(\u025f\13(\3(\3(\3(\7(\u0264\n(\f(\16(\u0267\13(\3(\5(\u026a\n(\3)\5"+
		")\u026d\n)\3)\3)\3)\3)\5)\u0273\n)\3*\3*\5*\u0277\n*\3*\5*\u027a\n*\3"+
		"*\3*\3*\3+\3+\3+\3+\3+\5+\u0284\n+\3,\3,\3,\3,\3,\5,\u028b\n,\3-\3-\3"+
		"-\3-\3-\5-\u0292\n-\3-\3-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\5/\u02a0\n/\3"+
		"\60\3\60\3\60\3\60\3\60\3\60\5\60\u02a8\n\60\3\61\3\61\3\61\3\61\3\61"+
		"\7\61\u02af\n\61\f\61\16\61\u02b2\13\61\3\61\3\61\3\61\3\61\5\61\u02b8"+
		"\n\61\3\61\3\61\7\61\u02bc\n\61\f\61\16\61\u02bf\13\61\3\61\3\61\3\62"+
		"\3\62\3\62\3\62\5\62\u02c7\n\62\3\63\3\63\3\63\3\63\5\63\u02cd\n\63\3"+
		"\64\3\64\3\64\3\64\5\64\u02d3\n\64\3\65\3\65\3\65\3\65\3\65\3\66\3\66"+
		"\3\66\3\66\3\66\5\66\u02df\n\66\3\67\3\67\3\67\3\67\5\67\u02e5\n\67\3"+
		"\67\3\67\5\67\u02e9\n\67\38\38\38\38\39\39\59\u02f1\n9\39\39\59\u02f5"+
		"\n9\39\39\3:\5:\u02fa\n:\3:\5:\u02fd\n:\3:\3:\3;\5;\u0302\n;\3;\3;\3<"+
		"\3<\3<\3<\3<\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\5=\u0316\n=\3=\3=\3=\3="+
		"\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3="+
		"\3=\3=\3=\7=\u0336\n=\f=\16=\u0339\13=\3>\3>\3>\3>\3>\3>\3>\3>\3>\3>\3"+
		">\3>\3>\3>\3>\3>\3>\3>\5>\u034d\n>\3?\3?\3?\3@\3@\3@\5@\u0355\n@\3A\3"+
		"A\3A\3B\3B\3B\3B\7B\u035e\nB\fB\16B\u0361\13B\3B\3B\3B\3B\5B\u0367\nB"+
		"\3C\3C\3C\3C\3C\3C\3C\3C\5C\u0371\nC\3D\3D\3D\3D\3D\3D\3D\5D\u037a\nD"+
		"\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\7D\u038b\nD\fD\16D\u038e"+
		"\13D\3E\3E\5E\u0392\nE\3E\5E\u0395\nE\3E\3E\3F\3F\3F\3F\3F\5F\u039e\n"+
		"F\3F\3F\7F\u03a2\nF\fF\16F\u03a5\13F\3F\3F\3G\3G\3G\3G\3H\5H\u03ae\nH"+
		"\3H\3H\3H\3H\3H\3H\5H\u03b6\nH\3H\3H\3H\3H\5H\u03bc\nH\3I\3I\3I\3I\3I"+
		"\3I\3I\5I\u03c5\nI\3J\3J\3J\3J\3J\3J\3J\3J\3J\5J\u03d0\nJ\3K\3K\3K\3L"+
		"\3L\3L\3L\7L\u03d9\nL\fL\16L\u03dc\13L\3L\5L\u03df\nL\5L\u03e1\nL\3L\3"+
		"L\3M\3M\3M\3M\3M\3M\3M\5M\u03ec\nM\3N\3N\3N\3N\3N\3O\3O\5O\u03f5\nO\3"+
		"O\3O\5O\u03f9\nO\3O\5O\u03fc\nO\3O\3O\3O\3O\3O\5O\u0403\nO\3O\3O\3P\3"+
		"P\3Q\3Q\3R\3R\3S\5S\u040e\nS\3S\3S\3T\3T\3T\3U\3U\3U\3U\3U\3U\7U\u041b"+
		"\nU\fU\16U\u041e\13U\3U\5U\u0421\nU\3V\5V\u0424\nV\3V\3V\3W\3W\3X\3X\3"+
		"X\5X\u042d\nX\3Y\3Y\3Y\3Y\3Y\3Y\7Y\u0435\nY\fY\16Y\u0438\13Y\3Y\5Y\u043b"+
		"\nY\3Z\3Z\5Z\u043f\nZ\3Z\3Z\5Z\u0443\nZ\3[\3[\3[\7[\u0448\n[\f[\16[\u044b"+
		"\13[\3\\\3\\\3\\\7\\\u0450\n\\\f\\\16\\\u0453\13\\\3]\3]\3]\3]\3]\3]\7"+
		"]\u045b\n]\f]\16]\u045e\13]\3]\5]\u0461\n]\3^\3^\5^\u0465\n^\3^\3^\3_"+
		"\3_\3_\3_\3_\3_\7_\u046f\n_\f_\16_\u0472\13_\3_\5_\u0475\n_\3`\3`\5`\u0479"+
		"\n`\3`\3`\3a\5a\u047e\na\3a\3a\3a\6a\u0483\na\ra\16a\u0484\3b\3b\3b\3"+
		"b\3b\5b\u048c\nb\3c\3c\3d\3d\3d\3d\3e\3e\3e\3f\3f\3f\3f\3g\3g\3h\3h\3"+
		"h\5h\u04a0\nh\3i\3i\5i\u04a4\ni\3j\3j\5j\u04a8\nj\3k\3k\5k\u04ac\nk\3"+
		"l\3l\3l\3m\3m\3n\3n\3n\3o\3o\3o\3o\3o\3o\3o\3o\3o\5o\u04bf\no\3o\3o\3"+
		"o\3o\5o\u04c5\no\5o\u04c7\no\3p\3p\5p\u04cb\np\3q\3q\5q\u04cf\nq\3q\5"+
		"q\u04d2\nq\3q\3q\5q\u04d6\nq\5q\u04d8\nq\3q\3q\7q\u04dc\nq\fq\16q\u04df"+
		"\13q\3q\3q\3r\3r\3r\5r\u04e6\nr\3s\3s\3s\5s\u04eb\ns\3t\3t\3t\3t\3t\3"+
		"t\3t\3t\3t\5t\u04f6\nt\3t\3t\7t\u04fa\nt\ft\16t\u04fd\13t\3t\3t\3u\3u"+
		"\5u\u0503\nu\3u\3u\3u\3u\3u\3u\3v\3v\3v\5v\u050e\nv\3w\3w\3w\5w\u0513"+
		"\nw\3x\3x\5x\u0517\nx\3x\3x\3x\5x\u051c\nx\7x\u051e\nx\fx\16x\u0521\13"+
		"x\3y\3y\3y\7y\u0526\ny\fy\16y\u0529\13y\3y\3y\3z\3z\3z\5z\u0530\nz\3{"+
		"\3{\3{\5{\u0535\n{\3{\5{\u0538\n{\3|\3|\3|\3|\3|\3|\5|\u0540\n|\3|\3|"+
		"\3}\3}\3}\3}\5}\u0548\n}\3}\3}\3~\5~\u054d\n~\3~\3~\5~\u0551\n~\3~\3~"+
		"\5~\u0555\n~\3\177\3\177\3\177\3\177\3\177\3\177\5\177\u055d\n\177\3\177"+
		"\3\177\3\177\3\u0080\3\u0080\3\u0080\3\u0081\3\u0081\5\u0081\u0567\n\u0081"+
		"\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0084\3\u0084"+
		"\3\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086\3\u0086\3\u0087\3\u0087"+
		"\3\u0087\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088\3\u0088\3\u0088"+
		"\5\u0088\u0584\n\u0088\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u008a"+
		"\3\u008a\3\u008a\3\u008a\5\u008a\u058f\n\u008a\3\u008b\3\u008b\5\u008b"+
		"\u0593\n\u008b\3\u008c\3\u008c\3\u008c\3\u008c\7\u008c\u0599\n\u008c\f"+
		"\u008c\16\u008c\u059c\13\u008c\3\u008c\5\u008c\u059f\n\u008c\5\u008c\u05a1"+
		"\n\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\5\u008d\u05a9"+
		"\n\u008d\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\5\u008e"+
		"\u05b2\n\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\5\u008f"+
		"\u05ba\n\u008f\3\u0090\3\u0090\3\u0090\5\u0090\u05bf\n\u0090\3\u0091\3"+
		"\u0091\3\u0092\3\u0092\3\u0093\3\u0093\3\u0093\3\u0093\3\u0094\3\u0094"+
		"\3\u0094\3\u0095\3\u0095\3\u0095\5\u0095\u05cf\n\u0095\5\u0095\u05d1\n"+
		"\u0095\3\u0095\3\u0095\3\u0096\3\u0096\3\u0096\7\u0096\u05d8\n\u0096\f"+
		"\u0096\16\u0096\u05db\13\u0096\3\u0097\3\u0097\3\u0097\5\u0097\u05e0\n"+
		"\u0097\3\u0097\3\u0097\3\u0098\3\u0098\5\u0098\u05e6\n\u0098\3\u0099\3"+
		"\u0099\5\u0099\u05ea\n\u0099\3\u009a\3\u009a\3\u009a\3\u009a\3\u009a\7"+
		"\u009a\u05f1\n\u009a\f\u009a\16\u009a\u05f4\13\u009a\3\u009a\3\u009a\3"+
		"\u009b\3\u009b\3\u009b\3\u009b\5\u009b\u05fc\n\u009b\3\u009b\5\u009b\u05ff"+
		"\n\u009b\3\u009c\3\u009c\3\u009d\5\u009d\u0604\n\u009d\3\u009d\3\u009d"+
		"\3\u009e\3\u009e\3\u009e\3\u009e\3\u009f\3\u009f\3\u009f\3\u009f\3\u00a0"+
		"\3\u00a0\3\u00a0\3\u00a0\3\u00a0\3\u00a1\3\u00a1\3\u00a1\3\u00a1\3\u00a1"+
		"\5\u00a1\u061a\n\u00a1\5\u00a1\u061c\n\u00a1\3\u00a1\5\u00a1\u061f\n\u00a1"+
		"\3\u00a1\5\u00a1\u0622\n\u00a1\5\u00a1\u0624\n\u00a1\3\u00a1\3\u00a1\3"+
		"\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4"+
		"\3\u00a4\5\u00a4\u0632\n\u00a4\3\u00a4\3\u022d\4x\u0086\u00a5\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVX"+
		"Z\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"+
		"\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120"+
		"\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138"+
		"\u013a\u013c\u013e\u0140\u0142\u0144\u0146\2\25\3\2\27\30\3\2\7\n\3\2"+
		"\24\25\3\2>?\3\2\')\4\2\')++\6\2,,//\62\62SS\3\2y\177\4\2tx}~\6\2\"\""+
		"ggssz|\3\2\36 \3\2\33\35\3\2mr\4\2sxz~\4\2[[ff\3\2gh\4\2dd\u0095\u0095"+
		"\4\2\u0080\u0083\u0085\u0086\3\2\u008c\u008d\2\u068b\2\u0148\3\2\2\2\4"+
		"\u014b\3\2\2\2\6\u014e\3\2\2\2\b\u0151\3\2\2\2\n\u0159\3\2\2\2\f\u0163"+
		"\3\2\2\2\16\u0171\3\2\2\2\20\u0173\3\2\2\2\22\u017a\3\2\2\2\24\u017c\3"+
		"\2\2\2\26\u0181\3\2\2\2\30\u018c\3\2\2\2\32\u0199\3\2\2\2\34\u019c\3\2"+
		"\2\2\36\u01a7\3\2\2\2 \u01a9\3\2\2\2\"\u01ae\3\2\2\2$\u01b3\3\2\2\2&\u01b8"+
		"\3\2\2\2(\u01bd\3\2\2\2*\u01ca\3\2\2\2,\u01cc\3\2\2\2.\u01ce\3\2\2\2\60"+
		"\u01d3\3\2\2\2\62\u01d8\3\2\2\2\64\u01e1\3\2\2\2\66\u01e8\3\2\2\28\u01f3"+
		"\3\2\2\2:\u01fa\3\2\2\2<\u01fc\3\2\2\2>\u0211\3\2\2\2@\u0213\3\2\2\2B"+
		"\u0223\3\2\2\2D\u022d\3\2\2\2F\u023b\3\2\2\2H\u023e\3\2\2\2J\u0246\3\2"+
		"\2\2L\u0248\3\2\2\2N\u0254\3\2\2\2P\u026c\3\2\2\2R\u0274\3\2\2\2T\u0283"+
		"\3\2\2\2V\u0285\3\2\2\2X\u028c\3\2\2\2Z\u0295\3\2\2\2\\\u029a\3\2\2\2"+
		"^\u02a1\3\2\2\2`\u02a9\3\2\2\2b\u02c6\3\2\2\2d\u02c8\3\2\2\2f\u02ce\3"+
		"\2\2\2h\u02d4\3\2\2\2j\u02d9\3\2\2\2l\u02e0\3\2\2\2n\u02ea\3\2\2\2p\u02ee"+
		"\3\2\2\2r\u02f9\3\2\2\2t\u0301\3\2\2\2v\u0305\3\2\2\2x\u0315\3\2\2\2z"+
		"\u034c\3\2\2\2|\u034e\3\2\2\2~\u0351\3\2\2\2\u0080\u0356\3\2\2\2\u0082"+
		"\u035f\3\2\2\2\u0084\u0370\3\2\2\2\u0086\u0379\3\2\2\2\u0088\u038f\3\2"+
		"\2\2\u008a\u0398\3\2\2\2\u008c\u03a8\3\2\2\2\u008e\u03bb\3\2\2\2\u0090"+
		"\u03c4\3\2\2\2\u0092\u03cf\3\2\2\2\u0094\u03d1\3\2\2\2\u0096\u03d4\3\2"+
		"\2\2\u0098\u03eb\3\2\2\2\u009a\u03ed\3\2\2\2\u009c\u03f2\3\2\2\2\u009e"+
		"\u0406\3\2\2\2\u00a0\u0408\3\2\2\2\u00a2\u040a\3\2\2\2\u00a4\u040d\3\2"+
		"\2\2\u00a6\u0411\3\2\2\2\u00a8\u0414\3\2\2\2\u00aa\u0423\3\2\2\2\u00ac"+
		"\u0427\3\2\2\2\u00ae\u042c\3\2\2\2\u00b0\u042e\3\2\2\2\u00b2\u043c\3\2"+
		"\2\2\u00b4\u0444\3\2\2\2\u00b6\u044c\3\2\2\2\u00b8\u0454\3\2\2\2\u00ba"+
		"\u0462\3\2\2\2\u00bc\u0468\3\2\2\2\u00be\u0476\3\2\2\2\u00c0\u0482\3\2"+
		"\2\2\u00c2\u048b\3\2\2\2\u00c4\u048d\3\2\2\2\u00c6\u048f\3\2\2\2\u00c8"+
		"\u0493\3\2\2\2\u00ca\u0496\3\2\2\2\u00cc\u049a\3\2\2\2\u00ce\u049c\3\2"+
		"\2\2\u00d0\u04a1\3\2\2\2\u00d2\u04a5\3\2\2\2\u00d4\u04a9\3\2\2\2\u00d6"+
		"\u04ad\3\2\2\2\u00d8\u04b0\3\2\2\2\u00da\u04b2\3\2\2\2\u00dc\u04b5\3\2"+
		"\2\2\u00de\u04ca\3\2\2\2\u00e0\u04cc\3\2\2\2\u00e2\u04e2\3\2\2\2\u00e4"+
		"\u04ea\3\2\2\2\u00e6\u04ec\3\2\2\2\u00e8\u0502\3\2\2\2\u00ea\u050a\3\2"+
		"\2\2\u00ec\u0512\3\2\2\2\u00ee\u0516\3\2\2\2\u00f0\u0522\3\2\2\2\u00f2"+
		"\u052c\3\2\2\2\u00f4\u0537\3\2\2\2\u00f6\u053f\3\2\2\2\u00f8\u0543\3\2"+
		"\2\2\u00fa\u054c\3\2\2\2\u00fc\u055c\3\2\2\2\u00fe\u0561\3\2\2\2\u0100"+
		"\u0566\3\2\2\2\u0102\u0568\3\2\2\2\u0104\u056d\3\2\2\2\u0106\u056f\3\2"+
		"\2\2\u0108\u0571\3\2\2\2\u010a\u0574\3\2\2\2\u010c\u0578\3\2\2\2\u010e"+
		"\u0583\3\2\2\2\u0110\u0587\3\2\2\2\u0112\u058e\3\2\2\2\u0114\u0592\3\2"+
		"\2\2\u0116\u0594\3\2\2\2\u0118\u05a4\3\2\2\2\u011a\u05b1\3\2\2\2\u011c"+
		"\u05b9\3\2\2\2\u011e\u05be\3\2\2\2\u0120\u05c0\3\2\2\2\u0122\u05c2\3\2"+
		"\2\2\u0124\u05c4\3\2\2\2\u0126\u05c8\3\2\2\2\u0128\u05cb\3\2\2\2\u012a"+
		"\u05d4\3\2\2\2\u012c\u05df\3\2\2\2\u012e\u05e5\3\2\2\2\u0130\u05e9\3\2"+
		"\2\2\u0132\u05eb\3\2\2\2\u0134\u05fb\3\2\2\2\u0136\u0600\3\2\2\2\u0138"+
		"\u0603\3\2\2\2\u013a\u0607\3\2\2\2\u013c\u060b\3\2\2\2\u013e\u060f\3\2"+
		"\2\2\u0140\u0614\3\2\2\2\u0142\u0627\3\2\2\2\u0144\u062b\3\2\2\2\u0146"+
		"\u0631\3\2\2\2\u0148\u0149\5x=\2\u0149\u014a\7\2\2\3\u014a\3\3\2\2\2\u014b"+
		"\u014c\5z>\2\u014c\u014d\7\2\2\3\u014d\5\3\2\2\2\u014e\u014f\5\u0090I"+
		"\2\u014f\u0150\7\2\2\3\u0150\7\3\2\2\2\u0151\u0156\5\n\6\2\u0152\u0153"+
		"\7c\2\2\u0153\u0155\5\n\6\2\u0154\u0152\3\2\2\2\u0155\u0158\3\2\2\2\u0156"+
		"\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157\t\3\2\2\2\u0158\u0156\3\2\2\2"+
		"\u0159\u015b\7[\2\2\u015a\u015c\79\2\2\u015b\u015a\3\2\2\2\u015b\u015c"+
		"\3\2\2\2\u015c\13\3\2\2\2\u015d\u015e\7\32\2\2\u015e\u0164\5z>\2\u015f"+
		"\u0160\t\2\2\2\u0160\u0164\5\36\20\2\u0161\u0162\t\3\2\2\u0162\u0164\5"+
		"x=\2\u0163\u015d\3\2\2\2\u0163\u015f\3\2\2\2\u0163\u0161\3\2\2\2\u0164"+
		"\r\3\2\2\2\u0165\u0172\5\64\33\2\u0166\u0172\5\62\32\2\u0167\u0172\5\60"+
		"\31\2\u0168\u0172\5\24\13\2\u0169\u0172\5.\30\2\u016a\u0172\5(\25\2\u016b"+
		"\u0172\5&\24\2\u016c\u0172\5\"\22\2\u016d\u0172\5 \21\2\u016e\u0172\5"+
		"$\23\2\u016f\u0172\5\20\t\2\u0170\u0172\5\22\n\2\u0171\u0165\3\2\2\2\u0171"+
		"\u0166\3\2\2\2\u0171\u0167\3\2\2\2\u0171\u0168\3\2\2\2\u0171\u0169\3\2"+
		"\2\2\u0171\u016a\3\2\2\2\u0171\u016b\3\2\2\2\u0171\u016c\3\2\2\2\u0171"+
		"\u016d\3\2\2\2\u0171\u016e\3\2\2\2\u0171\u016f\3\2\2\2\u0171\u0170\3\2"+
		"\2\2\u0172\17\3\2\2\2\u0173\u0174\t\4\2\2\u0174\u0175\5\26\f\2\u0175\u0176"+
		"\7e\2\2\u0176\u0177\7e\2\2\u0177\u0178\5\32\16\2\u0178\u0179\5x=\2\u0179"+
		"\21\3\2\2\2\u017a\u017b\t\5\2\2\u017b\23\3\2\2\2\u017c\u017d\7T\2\2\u017d"+
		"\u017e\7`\2\2\u017e\u017f\5\u0090I\2\u017f\u0180\7a\2\2\u0180\25\3\2\2"+
		"\2\u0181\u0186\5\30\r\2\u0182\u0183\7c\2\2\u0183\u0185\5\30\r\2\u0184"+
		"\u0182\3\2\2\2\u0185\u0188\3\2\2\2\u0186\u0184\3\2\2\2\u0186\u0187\3\2"+
		"\2\2\u0187\u018a\3\2\2\2\u0188\u0186\3\2\2\2\u0189\u018b\7c\2\2\u018a"+
		"\u0189\3\2\2\2\u018a\u018b\3\2\2\2\u018b\27\3\2\2\2\u018c\u0191\7[\2\2"+
		"\u018d\u018e\7c\2\2\u018e\u0190\7[\2\2\u018f\u018d\3\2\2\2\u0190\u0193"+
		"\3\2\2\2\u0191\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0194\3\2\2\2\u0193"+
		"\u0191\3\2\2\2\u0194\u0195\5\u0106\u0084\2\u0195\31\3\2\2\2\u0196\u0198"+
		"\5\34\17\2\u0197\u0196\3\2\2\2\u0198\u019b\3\2\2\2\u0199\u0197\3\2\2\2"+
		"\u0199\u019a\3\2\2\2\u019a\33\3\2\2\2\u019b\u0199\3\2\2\2\u019c\u019d"+
		"\7^\2\2\u019d\u01a2\5x=\2\u019e\u019f\7c\2\2\u019f\u01a1\5x=\2\u01a0\u019e"+
		"\3\2\2\2\u01a1\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3"+
		"\u01a5\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5\u01a6\7_\2\2\u01a6\35\3\2\2\2"+
		"\u01a7\u01a8\5\u0086D\2\u01a8\37\3\2\2\2\u01a9\u01aa\7\60\2\2\u01aa\u01ab"+
		"\7\\\2\2\u01ab\u01ac\5x=\2\u01ac\u01ad\7]\2\2\u01ad!\3\2\2\2\u01ae\u01af"+
		"\7\64\2\2\u01af\u01b0\7`\2\2\u01b0\u01b1\5\u0090I\2\u01b1\u01b2\7a\2\2"+
		"\u01b2#\3\2\2\2\u01b3\u01b4\7\61\2\2\u01b4\u01b5\7\\\2\2\u01b5\u01b6\5"+
		"x=\2\u01b6\u01b7\7]\2\2\u01b7%\3\2\2\2\u01b8\u01b9\t\6\2\2\u01b9\u01ba"+
		"\7\\\2\2\u01ba\u01bb\5x=\2\u01bb\u01bc\7]\2\2\u01bc\'\3\2\2\2\u01bd\u01c2"+
		"\7\22\2\2\u01be\u01bf\7`\2\2\u01bf\u01c0\5*\26\2\u01c0\u01c1\7a\2\2\u01c1"+
		"\u01c3\3\2\2\2\u01c2\u01be\3\2\2\2\u01c2\u01c3\3\2\2\2\u01c3\u01c4\3\2"+
		"\2\2\u01c4\u01c5\7\\\2\2\u01c5\u01c6\5x=\2\u01c6\u01c7\7]\2\2\u01c7)\3"+
		"\2\2\2\u01c8\u01cb\5,\27\2\u01c9\u01cb\7\23\2\2\u01ca\u01c8\3\2\2\2\u01ca"+
		"\u01c9\3\2\2\2\u01cb+\3\2\2\2\u01cc\u01cd\7[\2\2\u01cd-\3\2\2\2\u01ce"+
		"\u01cf\7\67\2\2\u01cf\u01d0\7\\\2\2\u01d0\u01d1\5x=\2\u01d1\u01d2\7]\2"+
		"\2\u01d2/\3\2\2\2\u01d3\u01d4\7\66\2\2\u01d4\u01d5\7\\\2\2\u01d5\u01d6"+
		"\5x=\2\u01d6\u01d7\7]\2\2\u01d7\61\3\2\2\2\u01d8\u01d9\7\26\2\2\u01d9"+
		"\u01da\7\\\2\2\u01da\u01dd\5x=\2\u01db\u01dc\7c\2\2\u01dc\u01de\5x=\2"+
		"\u01dd\u01db\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01e0"+
		"\7]\2\2\u01e0\63\3\2\2\2\u01e1\u01e2\t\6\2\2\u01e2\u01e3\7`\2\2\u01e3"+
		"\u01e4\5x=\2\u01e4\u01e5\7:\2\2\u01e5\u01e6\5x=\2\u01e6\u01e7\7a\2\2\u01e7"+
		"\65\3\2\2\2\u01e8\u01e9\7`\2\2\u01e9\u01ee\58\35\2\u01ea\u01eb\7c\2\2"+
		"\u01eb\u01ed\58\35\2\u01ec\u01ea\3\2\2\2\u01ed\u01f0\3\2\2\2\u01ee\u01ec"+
		"\3\2\2\2\u01ee\u01ef\3\2\2\2\u01ef\u01f1\3\2\2\2\u01f0\u01ee\3\2\2\2\u01f1"+
		"\u01f2\7a\2\2\u01f2\67\3\2\2\2\u01f3\u01f4\5x=\2\u01f4\u01f5\7b\2\2\u01f5"+
		"\u01f6\5x=\2\u01f69\3\2\2\2\u01f7\u01fb\5B\"\2\u01f8\u01fb\5@!\2\u01f9"+
		"\u01fb\5<\37\2\u01fa\u01f7\3\2\2\2\u01fa\u01f8\3\2\2\2\u01fa\u01f9\3\2"+
		"\2\2\u01fb;\3\2\2\2\u01fc\u01fd\7\62\2\2\u01fd\u0203\7^\2\2\u01fe\u01ff"+
		"\5> \2\u01ff\u0200\5\u0146\u00a4\2\u0200\u0202\3\2\2\2\u0201\u01fe\3\2"+
		"\2\2\u0202\u0205\3\2\2\2\u0203\u0201\3\2\2\2\u0203\u0204\3\2\2\2\u0204"+
		"\u0206\3\2\2\2\u0205\u0203\3\2\2\2\u0206\u0207\7_\2\2\u0207=\3\2\2\2\u0208"+
		"\u0209\7C\2\2\u0209\u020a\7[\2\2\u020a\u0212\5\u0112\u008a\2\u020b\u020c"+
		"\7\63\2\2\u020c\u020d\7^\2\2\u020d\u020e\5x=\2\u020e\u020f\5\u0146\u00a4"+
		"\2\u020f\u0210\7_\2\2\u0210\u0212\3\2\2\2\u0211\u0208\3\2\2\2\u0211\u020b"+
		"\3\2\2\2\u0212?\3\2\2\2\u0213\u0214\7\32\2\2\u0214\u0215\7`\2\2\u0215"+
		"\u0216\7a\2\2\u0216\u0217\5\u0106\u0084\2\u0217A\3\2\2\2\u0218\u0219\t"+
		"\7\2\2\u0219\u021a\7`\2\2\u021a\u021b\5\u0090I\2\u021b\u021c\7a\2\2\u021c"+
		"\u0224\3\2\2\2\u021d\u021e\7*\2\2\u021e\u021f\7`\2\2\u021f\u0220\5\u0090"+
		"I\2\u0220\u0221\7a\2\2\u0221\u0222\5\u0090I\2\u0222\u0224\3\2\2\2\u0223"+
		"\u0218\3\2\2\2\u0223\u021d\3\2\2\2\u0224C\3\2\2\2\u0225\u0229\5F$\2\u0226"+
		"\u0229\7\20\2\2\u0227\u0229\7@\2\2\u0228\u0225\3\2\2\2\u0228\u0226\3\2"+
		"\2\2\u0228\u0227\3\2\2\2\u0229\u022a\3\2\2\2\u022a\u022c\5\u0146\u00a4"+
		"\2\u022b\u0228\3\2\2\2\u022c\u022f\3\2\2\2\u022d\u022e\3\2\2\2\u022d\u022b"+
		"\3\2\2\2\u022e\u0231\3\2\2\2\u022f\u022d\3\2\2\2\u0230\u0232\7\20\2\2"+
		"\u0231\u0230\3\2\2\2\u0231\u0232\3\2\2\2\u0232E\3\2\2\2\u0233\u0234\7"+
		"\13\2\2\u0234\u023c\5J&\2\u0235\u0236\7\f\2\2\u0236\u023c\5J&\2\u0237"+
		"\u0238\7\r\2\2\u0238\u023c\5J&\2\u0239\u023a\7\17\2\2\u023a\u023c\5H%"+
		"\2\u023b\u0233\3\2\2\2\u023b\u0235\3\2\2\2\u023b\u0237\3\2\2\2\u023b\u0239"+
		"\3\2\2\2\u023cG\3\2\2\2\u023d\u023f\5\u00b6\\\2\u023e\u023d\3\2\2\2\u023e"+
		"\u023f\3\2\2\2\u023f\u0242\3\2\2\2\u0240\u0241\7R\2\2\u0241\u0243\5x="+
		"\2\u0242\u0240\3\2\2\2\u0242\u0243\3\2\2\2\u0243I\3\2\2\2\u0244\u0247"+
		"\3\2\2\2\u0245\u0247\5x=\2\u0246\u0244\3\2\2\2\u0246\u0245\3\2\2\2\u0247"+
		"K\3\2\2\2\u0248\u024d\7^\2\2\u0249\u024a\78\2\2\u024a\u024b\5\u00b4[\2"+
		"\u024b\u024c\5\u0146\u00a4\2\u024c\u024e\3\2\2\2\u024d\u0249\3\2\2\2\u024d"+
		"\u024e\3\2\2\2\u024e\u0250\3\2\2\2\u024f\u0251\5\u00c0a\2\u0250\u024f"+
		"\3\2\2\2\u0250\u0251\3\2\2\2\u0251\u0252\3\2\2\2\u0252\u0253\7_\2\2\u0253"+
		"M\3\2\2\2\u0254\u0255\5\u0090I\2\u0255\u0256\7\21\2\2\u0256\u0269\5\u0090"+
		"I\2\u0257\u025d\7^\2\2\u0258\u0259\5V,\2\u0259\u025a\5\u0146\u00a4\2\u025a"+
		"\u025c\3\2\2\2\u025b\u0258\3\2\2\2\u025c\u025f\3\2\2\2\u025d\u025b\3\2"+
		"\2\2\u025d\u025e\3\2\2\2\u025e\u0265\3\2\2\2\u025f\u025d\3\2\2\2\u0260"+
		"\u0261\5P)\2\u0261\u0262\5\u0146\u00a4\2\u0262\u0264\3\2\2\2\u0263\u0260"+
		"\3\2\2\2\u0264\u0267\3\2\2\2\u0265\u0263\3\2\2\2\u0265\u0266\3\2\2\2\u0266"+
		"\u0268\3\2\2\2\u0267\u0265\3\2\2\2\u0268\u026a\7_\2\2\u0269\u0257\3\2"+
		"\2\2\u0269\u026a\3\2\2\2\u026aO\3\2\2\2\u026b\u026d\7\20\2\2\u026c\u026b"+
		"\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u026e\3\2\2\2\u026e\u026f\5R*\2\u026f"+
		"\u0270\7[\2\2\u0270\u0272\5\u0112\u008a\2\u0271\u0273\5\u00be`\2\u0272"+
		"\u0271\3\2\2\2\u0272\u0273\3\2\2\2\u0273Q\3\2\2\2\u0274\u0276\7\\\2\2"+
		"\u0275\u0277\7[\2\2\u0276\u0275\3\2\2\2\u0276\u0277\3\2\2\2\u0277\u0279"+
		"\3\2\2\2\u0278\u027a\7}\2\2\u0279\u0278\3\2\2\2\u0279\u027a\3\2\2\2\u027a"+
		"\u027b\3\2\2\2\u027b\u027c\5\u0100\u0081\2\u027c\u027d\7]\2\2\u027dS\3"+
		"\2\2\2\u027e\u0284\5\u0086D\2\u027f\u0280\5\u0090I\2\u0280\u0281\7f\2"+
		"\2\u0281\u0282\7[\2\2\u0282\u0284\3\2\2\2\u0283\u027e\3\2\2\2\u0283\u027f"+
		"\3\2\2\2\u0284U\3\2\2\2\u0285\u0286\7\65\2\2\u0286\u0287\7[\2\2\u0287"+
		"\u028a\7i\2\2\u0288\u028b\5T+\2\u0289\u028b\5\u0122\u0092\2\u028a\u0288"+
		"\3\2\2\2\u028a\u0289\3\2\2\2\u028bW\3\2\2\2\u028c\u028d\7.\2\2\u028d\u028e"+
		"\7\\\2\2\u028e\u0291\5\u0090I\2\u028f\u0290\7c\2\2\u0290\u0292\5\u00b6"+
		"\\\2\u0291\u028f\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u0293\3\2\2\2\u0293"+
		"\u0294\7]\2\2\u0294Y\3\2\2\2\u0295\u0296\7-\2\2\u0296\u0297\7\\\2\2\u0297"+
		"\u0298\5\u0090I\2\u0298\u0299\7]\2\2\u0299[\3\2\2\2\u029a\u029b\5D#\2"+
		"\u029b\u029c\7C\2\2\u029c\u029d\7[\2\2\u029d\u029f\5\u0112\u008a\2\u029e"+
		"\u02a0\5L\'\2\u029f\u029e\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0]\3\2\2\2\u02a1"+
		"\u02a2\5D#\2\u02a2\u02a3\7C\2\2\u02a3\u02a4\5p9\2\u02a4\u02a5\7[\2\2\u02a5"+
		"\u02a7\5\u0112\u008a\2\u02a6\u02a8\5L\'\2\u02a7\u02a6\3\2\2\2\u02a7\u02a8"+
		"\3\2\2\2\u02a8_\3\2\2\2\u02a9\u02aa\5\u00a6T\2\u02aa\u02b0\5\u0146\u00a4"+
		"\2\u02ab\u02ac\5\u00a8U\2\u02ac\u02ad\5\u0146\u00a4\2\u02ad\u02af\3\2"+
		"\2\2\u02ae\u02ab\3\2\2\2\u02af\u02b2\3\2\2\2\u02b0\u02ae\3\2\2\2\u02b0"+
		"\u02b1\3\2\2\2\u02b1\u02bd\3\2\2\2\u02b2\u02b0\3\2\2\2\u02b3\u02b8\5\\"+
		"/\2\u02b4\u02b8\5^\60\2\u02b5\u02b8\5\u00aeX\2\u02b6\u02b8\5b\62\2\u02b7"+
		"\u02b3\3\2\2\2\u02b7\u02b4\3\2\2\2\u02b7\u02b5\3\2\2\2\u02b7\u02b6\3\2"+
		"\2\2\u02b8\u02b9\3\2\2\2\u02b9\u02ba\5\u0146\u00a4\2\u02ba\u02bc\3\2\2"+
		"\2\u02bb\u02b7\3\2\2\2\u02bc\u02bf\3\2\2\2\u02bd\u02bb\3\2\2\2\u02bd\u02be"+
		"\3\2\2\2\u02be\u02c0\3\2\2\2\u02bf\u02bd\3\2\2\2\u02c0\u02c1\7\2\2\3\u02c1"+
		"a\3\2\2\2\u02c2\u02c7\5N(\2\u02c3\u02c7\5f\64\2\u02c4\u02c7\5j\66\2\u02c5"+
		"\u02c7\5d\63\2\u02c6\u02c2\3\2\2\2\u02c6\u02c3\3\2\2\2\u02c6\u02c4\3\2"+
		"\2\2\u02c6\u02c5\3\2\2\2\u02c7c\3\2\2\2\u02c8\u02cc\7\32\2\2\u02c9\u02cd"+
		"\5^\60\2\u02ca\u02cd\5\\/\2\u02cb\u02cd\5\u00aeX\2\u02cc\u02c9\3\2\2\2"+
		"\u02cc\u02ca\3\2\2\2\u02cc\u02cb\3\2\2\2\u02cde\3\2\2\2\u02ce\u02cf\7"+
		"\65\2\2\u02cf\u02d0\7[\2\2\u02d0\u02d2\5\u0116\u008c\2\u02d1\u02d3\5h"+
		"\65\2\u02d2\u02d1\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3g\3\2\2\2\u02d4\u02d5"+
		"\7^\2\2\u02d5\u02d6\5x=\2\u02d6\u02d7\5\u0146\u00a4\2\u02d7\u02d8\7_\2"+
		"\2\u02d8i\3\2\2\2\u02d9\u02da\7\65\2\2\u02da\u02db\5p9\2\u02db\u02dc\7"+
		"[\2\2\u02dc\u02de\5\u0116\u008c\2\u02dd\u02df\5h\65\2\u02de\u02dd\3\2"+
		"\2\2\u02de\u02df\3\2\2\2\u02dfk\3\2\2\2\u02e0\u02e8\5\b\5\2\u02e1\u02e4"+
		"\5\u0090I\2\u02e2\u02e3\7b\2\2\u02e3\u02e5\5\u00b6\\\2\u02e4\u02e2\3\2"+
		"\2\2\u02e4\u02e5\3\2\2\2\u02e5\u02e9\3\2\2\2\u02e6\u02e7\7b\2\2\u02e7"+
		"\u02e9\5\u00b6\\\2\u02e8\u02e1\3\2\2\2\u02e8\u02e6\3\2\2\2\u02e9m\3\2"+
		"\2\2\u02ea\u02eb\5\b\5\2\u02eb\u02ec\7i\2\2\u02ec\u02ed\5\u00b6\\\2\u02ed"+
		"o\3\2\2\2\u02ee\u02f0\7\\\2\2\u02ef\u02f1\5\n\6\2\u02f0\u02ef\3\2\2\2"+
		"\u02f0\u02f1\3\2\2\2\u02f1\u02f2\3\2\2\2\u02f2\u02f4\5\u0090I\2\u02f3"+
		"\u02f5\7c\2\2\u02f4\u02f3\3\2\2\2\u02f4\u02f5\3\2\2\2\u02f5\u02f6\3\2"+
		"\2\2\u02f6\u02f7\7]\2\2\u02f7q\3\2\2\2\u02f8\u02fa\7\32\2\2\u02f9\u02f8"+
		"\3\2\2\2\u02f9\u02fa\3\2\2\2\u02fa\u02fc\3\2\2\2\u02fb\u02fd\5\u00b4["+
		"\2\u02fc\u02fb\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd\u02fe\3\2\2\2\u02fe\u02ff"+
		"\5t;\2\u02ffs\3\2\2\2\u0300\u0302\7j\2\2\u0301\u0300\3\2\2\2\u0301\u0302"+
		"\3\2\2\2\u0302\u0303\3\2\2\2\u0303\u0304\5\u0090I\2\u0304u\3\2\2\2\u0305"+
		"\u0306\7\31\2\2\u0306\u0307\5\36\20\2\u0307\u0308\7\33\2\2\u0308\u0309"+
		"\5x=\2\u0309w\3\2\2\2\u030a\u030b\b=\1\2\u030b\u030c\t\b\2\2\u030c\u030d"+
		"\7\\\2\2\u030d\u030e\5x=\2\u030e\u030f\7]\2\2\u030f\u0316\3\2\2\2\u0310"+
		"\u0316\5v<\2\u0311\u0316\5X-\2\u0312\u0313\t\t\2\2\u0313\u0316\5x=\r\u0314"+
		"\u0316\5\u0086D\2\u0315\u030a\3\2\2\2\u0315\u0310\3\2\2\2\u0315\u0311"+
		"\3\2\2\2\u0315\u0312\3\2\2\2\u0315\u0314\3\2\2\2\u0316\u0337\3\2\2\2\u0317"+
		"\u0318\f\13\2\2\u0318\u0319\t\n\2\2\u0319\u0336\5x=\f\u031a\u031b\f\n"+
		"\2\2\u031b\u031c\t\13\2\2\u031c\u0336\5x=\13\u031d\u031e\f\t\2\2\u031e"+
		"\u031f\t\f\2\2\u031f\u0336\5x=\n\u0320\u0321\f\b\2\2\u0321\u0322\t\r\2"+
		"\2\u0322\u0336\5x=\t\u0323\u0324\f\7\2\2\u0324\u0325\t\16\2\2\u0325\u0336"+
		"\5x=\b\u0326\u0327\f\6\2\2\u0327\u0328\7l\2\2\u0328\u0336\5x=\7\u0329"+
		"\u032a\f\5\2\2\u032a\u032b\7k\2\2\u032b\u0336\5x=\6\u032c\u032d\f\4\2"+
		"\2\u032d\u032e\7!\2\2\u032e\u0336\5x=\4\u032f\u0330\f\3\2\2\u0330\u0331"+
		"\7$\2\2\u0331\u0332\5x=\2\u0332\u0333\7e\2\2\u0333\u0334\5x=\3\u0334\u0336"+
		"\3\2\2\2\u0335\u0317\3\2\2\2\u0335\u031a\3\2\2\2\u0335\u031d\3\2\2\2\u0335"+
		"\u0320\3\2\2\2\u0335\u0323\3\2\2\2\u0335\u0326\3\2\2\2\u0335\u0329\3\2"+
		"\2\2\u0335\u032c\3\2\2\2\u0335\u032f\3\2\2\2\u0336\u0339\3\2\2\2\u0337"+
		"\u0335\3\2\2\2\u0337\u0338\3\2\2\2\u0338y\3\2\2\2\u0339\u0337\3\2\2\2"+
		"\u033a\u034d\5\f\7\2\u033b\u034d\5~@\2\u033c\u034d\5|?\2\u033d\u034d\5"+
		"\u00aeX\2\u033e\u034d\5\u00ceh\2\u033f\u034d\5\u00c2b\2\u0340\u034d\5"+
		"\u00fe\u0080\2\u0341\u034d\5\u00d0i\2\u0342\u034d\5\u00d2j\2\u0343\u034d"+
		"\5\u00d4k\2\u0344\u034d\5\u00d6l\2\u0345\u034d\5\u00d8m\2\u0346\u034d"+
		"\5\u00be`\2\u0347\u034d\5\u00dco\2\u0348\u034d\5\u00dep\2\u0349\u034d"+
		"\5\u00f0y\2\u034a\u034d\5\u0080A\2\u034b\u034d\5\u00dan\2\u034c\u033a"+
		"\3\2\2\2\u034c\u033b\3\2\2\2\u034c\u033c\3\2\2\2\u034c\u033d\3\2\2\2\u034c"+
		"\u033e\3\2\2\2\u034c\u033f\3\2\2\2\u034c\u0340\3\2\2\2\u034c\u0341\3\2"+
		"\2\2\u034c\u0342\3\2\2\2\u034c\u0343\3\2\2\2\u034c\u0344\3\2\2\2\u034c"+
		"\u0345\3\2\2\2\u034c\u0346\3\2\2\2\u034c\u0347\3\2\2\2\u034c\u0348\3\2"+
		"\2\2\u034c\u0349\3\2\2\2\u034c\u034a\3\2\2\2\u034c\u034b\3\2\2\2\u034d"+
		"{\3\2\2\2\u034e\u034f\7#\2\2\u034f\u0350\5x=\2\u0350}\3\2\2\2\u0351\u0352"+
		"\7N\2\2\u0352\u0354\5x=\2\u0353\u0355\5\u00be`\2\u0354\u0353\3\2\2\2\u0354"+
		"\u0355\3\2\2\2\u0355\177\3\2\2\2\u0356\u0357\5\u0082B\2\u0357\u0358\5"+
		"\u00f8}\2\u0358\u0081\3\2\2\2\u0359\u035a\7\16\2\2\u035a\u035b\5x=\2\u035b"+
		"\u035c\5\u0146\u00a4\2\u035c\u035e\3\2\2\2\u035d\u0359\3\2\2\2\u035e\u0361"+
		"\3\2\2\2\u035f\u035d\3\2\2\2\u035f\u0360\3\2\2\2\u0360\u0366\3\2\2\2\u0361"+
		"\u035f\3\2\2\2\u0362\u0363\7\17\2\2\u0363\u0364\5H%\2\u0364\u0365\5\u0146"+
		"\u00a4\2\u0365\u0367\3\2\2\2\u0366\u0362\3\2\2\2\u0366\u0367\3\2\2\2\u0367"+
		"\u0083\3\2\2\2\u0368\u0371\7\5\2\2\u0369\u0371\7\6\2\2\u036a\u0371\7Z"+
		"\2\2\u036b\u0371\5\u0120\u0091\2\u036c\u0371\5\u0136\u009c\2\u036d\u0371"+
		"\7\3\2\2\u036e\u0371\7\u0085\2\2\u036f\u0371\7\u0086\2\2\u0370\u0368\3"+
		"\2\2\2\u0370\u0369\3\2\2\2\u0370\u036a\3\2\2\2\u0370\u036b\3\2\2\2\u0370"+
		"\u036c\3\2\2\2\u0370\u036d\3\2\2\2\u0370\u036e\3\2\2\2\u0370\u036f\3\2"+
		"\2\2\u0371\u0085\3\2\2\2\u0372\u0373\bD\1\2\u0373\u037a\5\u011c\u008f"+
		"\2\u0374\u037a\5\u0118\u008d\2\u0375\u037a\5\u0142\u00a2\2\u0376\u037a"+
		"\5\16\b\2\u0377\u037a\5Z.\2\u0378\u037a\5X-\2\u0379\u0372\3\2\2\2\u0379"+
		"\u0374\3\2\2\2\u0379\u0375\3\2\2\2\u0379\u0376\3\2\2\2\u0379\u0377\3\2"+
		"\2\2\u0379\u0378\3\2\2\2\u037a\u038c\3\2\2\2\u037b\u037c\f\t\2\2\u037c"+
		"\u037d\7f\2\2\u037d\u038b\7[\2\2\u037e\u037f\f\b\2\2\u037f\u038b\5\u013c"+
		"\u009f\2\u0380\u0381\f\7\2\2\u0381\u038b\5\u009cO\2\u0382\u0383\f\6\2"+
		"\2\u0383\u038b\5\66\34\2\u0384\u0385\f\5\2\2\u0385\u038b\5\u013e\u00a0"+
		"\2\u0386\u0387\f\4\2\2\u0387\u038b\5\u0140\u00a1\2\u0388\u0389\f\3\2\2"+
		"\u0389\u038b\5\u0088E\2\u038a\u037b\3\2\2\2\u038a\u037e\3\2\2\2\u038a"+
		"\u0380\3\2\2\2\u038a\u0382\3\2\2\2\u038a\u0384\3\2\2\2\u038a\u0386\3\2"+
		"\2\2\u038a\u0388\3\2\2\2\u038b\u038e\3\2\2\2\u038c\u038a\3\2\2\2\u038c"+
		"\u038d\3\2\2\2\u038d\u0087\3\2\2\2\u038e\u038c\3\2\2\2\u038f\u0391\7%"+
		"\2\2\u0390\u0392\5\u00b6\\\2\u0391\u0390\3\2\2\2\u0391\u0392\3\2\2\2\u0392"+
		"\u0394\3\2\2\2\u0393\u0395\7c\2\2\u0394\u0393\3\2\2\2\u0394\u0395\3\2"+
		"\2\2\u0395\u0396\3\2\2\2\u0396\u0397\7&\2\2\u0397\u0089\3\2\2\2\u0398"+
		"\u0399\7D\2\2\u0399\u03a3\7^\2\2\u039a\u039e\5\u008eH\2\u039b\u039e\5"+
		"\u0100\u0081\2\u039c\u039e\5\u008cG\2\u039d\u039a\3\2\2\2\u039d\u039b"+
		"\3\2\2\2\u039d\u039c\3\2\2\2\u039e\u039f\3\2\2\2\u039f\u03a0\5\u0146\u00a4"+
		"\2\u03a0\u03a2\3\2\2\2\u03a1\u039d\3\2\2\2\u03a2\u03a5\3\2\2\2\u03a3\u03a1"+
		"\3\2\2\2\u03a3\u03a4\3\2\2\2\u03a4\u03a6\3\2\2\2\u03a5\u03a3\3\2\2\2\u03a6"+
		"\u03a7\7_\2\2\u03a7\u008b\3\2\2\2\u03a8\u03a9\7\65\2\2\u03a9\u03aa\7["+
		"\2\2\u03aa\u03ab\5\u0116\u008c\2\u03ab\u008d\3\2\2\2\u03ac\u03ae\7\32"+
		"\2\2\u03ad\u03ac\3\2\2\2\u03ad\u03ae\3\2\2\2\u03ae\u03af\3\2\2\2\u03af"+
		"\u03b0\5D#\2\u03b0\u03b1\7[\2\2\u03b1\u03b2\5\u0116\u008c\2\u03b2\u03b3"+
		"\5\u0114\u008b\2\u03b3\u03bc\3\2\2\2\u03b4\u03b6\7\32\2\2\u03b5\u03b4"+
		"\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7\u03b8\5D#\2\u03b8"+
		"\u03b9\7[\2\2\u03b9\u03ba\5\u0116\u008c\2\u03ba\u03bc\3\2\2\2\u03bb\u03ad"+
		"\3\2\2\2\u03bb\u03b5\3\2\2\2\u03bc\u008f\3\2\2\2\u03bd\u03c5\5\u0100\u0081"+
		"\2\u03be\u03c5\5\u0092J\2\u03bf\u03c5\5:\36\2\u03c0\u03c1\7\\\2\2\u03c1"+
		"\u03c2\5\u0090I\2\u03c2\u03c3\7]\2\2\u03c3\u03c5\3\2\2\2\u03c4\u03bd\3"+
		"\2\2\2\u03c4\u03be\3\2\2\2\u03c4\u03bf\3\2\2\2\u03c4\u03c0\3\2\2\2\u03c5"+
		"\u0091\3\2\2\2\u03c6\u03d0\5\u0102\u0082\2\u03c7\u03d0\5\u0132\u009a\2"+
		"\u03c8\u03d0\5\u0108\u0085\2\u03c9\u03d0\5\u0110\u0089\2\u03ca\u03d0\5"+
		"\u008aF\2\u03cb\u03d0\5\u010a\u0086\2\u03cc\u03d0\5\u010c\u0087\2\u03cd"+
		"\u03d0\5\u010e\u0088\2\u03ce\u03d0\5\u0094K\2\u03cf\u03c6\3\2\2\2\u03cf"+
		"\u03c7\3\2\2\2\u03cf\u03c8\3\2\2\2\u03cf\u03c9\3\2\2\2\u03cf\u03ca\3\2"+
		"\2\2\u03cf\u03cb\3\2\2\2\u03cf\u03cc\3\2\2\2\u03cf\u03cd\3\2\2\2\u03cf"+
		"\u03ce\3\2\2\2\u03d0\u0093\3\2\2\2\u03d1\u03d2\7\65\2\2\u03d2\u03d3\5"+
		"\u0096L\2\u03d3\u0095\3\2\2\2\u03d4\u03e0\7\\\2\2\u03d5\u03da\5\u0090"+
		"I\2\u03d6\u03d7\7c\2\2\u03d7\u03d9\5\u0090I\2\u03d8\u03d6\3\2\2\2\u03d9"+
		"\u03dc\3\2\2\2\u03da\u03d8\3\2\2\2\u03da\u03db\3\2\2\2\u03db\u03de\3\2"+
		"\2\2\u03dc\u03da\3\2\2\2\u03dd\u03df\7c\2\2\u03de\u03dd\3\2\2\2\u03de"+
		"\u03df\3\2\2\2\u03df\u03e1\3\2\2\2\u03e0\u03d5\3\2\2\2\u03e0\u03e1\3\2"+
		"\2\2\u03e1\u03e2\3\2\2\2\u03e2\u03e3\7]\2\2\u03e3\u0097\3\2\2\2\u03e4"+
		"\u03ec\5\u0132\u009a\2\u03e5\u03ec\5\u0102\u0082\2\u03e6\u03ec\5\u009a"+
		"N\2\u03e7\u03ec\5\u010a\u0086\2\u03e8\u03ec\5\u010c\u0087\2\u03e9\u03ec"+
		"\5:\36\2\u03ea\u03ec\5\u0100\u0081\2\u03eb\u03e4\3\2\2\2\u03eb\u03e5\3"+
		"\2\2\2\u03eb\u03e6\3\2\2\2\u03eb\u03e7\3\2\2\2\u03eb\u03e8\3\2\2\2\u03eb"+
		"\u03e9\3\2\2\2\u03eb\u03ea\3\2\2\2\u03ec\u0099\3\2\2\2\u03ed\u03ee\7`"+
		"\2\2\u03ee\u03ef\7j\2\2\u03ef\u03f0\7a\2\2\u03f0\u03f1\5\u0106\u0084\2"+
		"\u03f1\u009b\3\2\2\2\u03f2\u0402\7`\2\2\u03f3\u03f5\5\u009eP\2\u03f4\u03f3"+
		"\3\2\2\2\u03f4\u03f5\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6\u03f8\7e\2\2\u03f7"+
		"\u03f9\5\u00a0Q\2\u03f8\u03f7\3\2\2\2\u03f8\u03f9\3\2\2\2\u03f9\u0403"+
		"\3\2\2\2\u03fa\u03fc\5\u009eP\2\u03fb\u03fa\3\2\2\2\u03fb\u03fc\3\2\2"+
		"\2\u03fc\u03fd\3\2\2\2\u03fd\u03fe\7e\2\2\u03fe\u03ff\5\u00a0Q\2\u03ff"+
		"\u0400\7e\2\2\u0400\u0401\5\u00a2R\2\u0401\u0403\3\2\2\2\u0402\u03f4\3"+
		"\2\2\2\u0402\u03fb\3\2\2\2\u0403\u0404\3\2\2\2\u0404\u0405\7a\2\2\u0405"+
		"\u009d\3\2\2\2\u0406\u0407\5x=\2\u0407\u009f\3\2\2\2\u0408\u0409\5x=\2"+
		"\u0409\u00a1\3\2\2\2\u040a\u040b\5x=\2\u040b\u00a3\3\2\2\2\u040c\u040e"+
		"\t\17\2\2\u040d\u040c\3\2\2\2\u040d\u040e\3\2\2\2\u040e\u040f\3\2\2\2"+
		"\u040f\u0410\7b\2\2\u0410\u00a5\3\2\2\2\u0411\u0412\7N\2\2\u0412\u0413"+
		"\7[\2\2\u0413\u00a7\3\2\2\2\u0414\u0420\7W\2\2\u0415\u0421\5\u00aaV\2"+
		"\u0416\u041c\7\\\2\2\u0417\u0418\5\u00aaV\2\u0418\u0419\5\u0146\u00a4"+
		"\2\u0419\u041b\3\2\2\2\u041a\u0417\3\2\2\2\u041b\u041e\3\2\2\2\u041c\u041a"+
		"\3\2\2\2\u041c\u041d\3\2\2\2\u041d\u041f\3\2\2\2\u041e\u041c\3\2\2\2\u041f"+
		"\u0421\7]\2\2\u0420\u0415\3\2\2\2\u0420\u0416\3\2\2\2\u0421\u00a9\3\2"+
		"\2\2\u0422\u0424\t\20\2\2\u0423\u0422\3\2\2\2\u0423\u0424\3\2\2\2\u0424"+
		"\u0425\3\2\2\2\u0425\u0426\5\u00acW\2\u0426\u00ab\3\2\2\2\u0427\u0428"+
		"\5\u0136\u009c\2\u0428\u00ad\3\2\2\2\u0429\u042d\5\u00b0Y\2\u042a\u042d"+
		"\5\u00b8]\2\u042b\u042d\5\u00bc_\2\u042c\u0429\3\2\2\2\u042c\u042a\3\2"+
		"\2\2\u042c\u042b\3\2\2\2\u042d\u00af\3\2\2\2\u042e\u043a\7P\2\2\u042f"+
		"\u043b\5\u00b2Z\2\u0430\u0436\7\\\2\2\u0431\u0432\5\u00b2Z\2\u0432\u0433"+
		"\5\u0146\u00a4\2\u0433\u0435\3\2\2\2\u0434\u0431\3\2\2\2\u0435\u0438\3"+
		"\2\2\2\u0436\u0434\3\2\2\2\u0436\u0437\3\2\2\2\u0437\u0439\3\2\2\2\u0438"+
		"\u0436\3\2\2\2\u0439\u043b\7]\2\2\u043a\u042f\3\2\2\2\u043a\u0430\3\2"+
		"\2\2\u043b\u00b1\3\2\2\2\u043c\u0442\5\u00b4[\2\u043d\u043f\5\u0090I\2"+
		"\u043e\u043d\3\2\2\2\u043e\u043f\3\2\2\2\u043f\u0440\3\2\2\2\u0440\u0441"+
		"\7b\2\2\u0441\u0443\5\u00b6\\\2\u0442\u043e\3\2\2\2\u0442\u0443\3\2\2"+
		"\2\u0443\u00b3\3\2\2\2\u0444\u0449\7[\2\2\u0445\u0446\7c\2\2\u0446\u0448"+
		"\7[\2\2\u0447\u0445\3\2\2\2\u0448\u044b\3\2\2\2\u0449\u0447\3\2\2\2\u0449"+
		"\u044a\3\2\2\2\u044a\u00b5\3\2\2\2\u044b\u0449\3\2\2\2\u044c\u0451\5x"+
		"=\2\u044d\u044e\7c\2\2\u044e\u0450\5x=\2\u044f\u044d\3\2\2\2\u0450\u0453"+
		"\3\2\2\2\u0451\u044f\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u00b7\3\2\2\2\u0453"+
		"\u0451\3\2\2\2\u0454\u0460\7T\2\2\u0455\u0461\5\u00ba^\2\u0456\u045c\7"+
		"\\\2\2\u0457\u0458\5\u00ba^\2\u0458\u0459\5\u0146\u00a4\2\u0459\u045b"+
		"\3\2\2\2\u045a\u0457\3\2\2\2\u045b\u045e\3\2\2\2\u045c\u045a\3\2\2\2\u045c"+
		"\u045d\3\2\2\2\u045d\u045f\3\2\2\2\u045e\u045c\3\2\2\2\u045f\u0461\7]"+
		"\2\2\u0460\u0455\3\2\2\2\u0460\u0456\3\2\2\2\u0461\u00b9\3\2\2\2\u0462"+
		"\u0464\7[\2\2\u0463\u0465\7b\2\2\u0464\u0463\3\2\2\2\u0464\u0465\3\2\2"+
		"\2\u0465\u0466\3\2\2\2\u0466\u0467\5\u0090I\2\u0467\u00bb\3\2\2\2\u0468"+
		"\u0474\7Y\2\2\u0469\u0475\5l\67\2\u046a\u0470\7\\\2\2\u046b\u046c\5l\67"+
		"\2\u046c\u046d\5\u0146\u00a4\2\u046d\u046f\3\2\2\2\u046e\u046b\3\2\2\2"+
		"\u046f\u0472\3\2\2\2\u0470\u046e\3\2\2\2\u0470\u0471\3\2\2\2\u0471\u0473"+
		"\3\2\2\2\u0472\u0470\3\2\2\2\u0473\u0475\7]\2\2\u0474\u0469\3\2\2\2\u0474"+
		"\u046a\3\2\2\2\u0475\u00bd\3\2\2\2\u0476\u0478\7^\2\2\u0477\u0479\5\u00c0"+
		"a\2\u0478\u0477\3\2\2\2\u0478\u0479\3\2\2\2\u0479\u047a\3\2\2\2\u047a"+
		"\u047b\7_\2\2\u047b\u00bf\3\2\2\2\u047c\u047e\5\u0146\u00a4\2\u047d\u047c"+
		"\3\2\2\2\u047d\u047e\3\2\2\2\u047e\u047f\3\2\2\2\u047f\u0480\5z>\2\u0480"+
		"\u0481\5\u0146\u00a4\2\u0481\u0483\3\2\2\2\u0482\u047d\3\2\2\2\u0483\u0484"+
		"\3\2\2\2\u0484\u0482\3\2\2\2\u0484\u0485\3\2\2\2\u0485\u00c1\3\2\2\2\u0486"+
		"\u048c\5\u00c6d\2\u0487\u048c\5\u00c8e\2\u0488\u048c\5\u00caf\2\u0489"+
		"\u048c\5\u00c4c\2\u048a\u048c\5n8\2\u048b\u0486\3\2\2\2\u048b\u0487\3"+
		"\2\2\2\u048b\u0488\3\2\2\2\u048b\u0489\3\2\2\2\u048b\u048a\3\2\2\2\u048c"+
		"\u00c3\3\2\2\2\u048d\u048e\5x=\2\u048e\u00c5\3\2\2\2\u048f\u0490\5x=\2"+
		"\u0490\u0491\7\177\2\2\u0491\u0492\5x=\2\u0492\u00c7\3\2\2\2\u0493\u0494"+
		"\5x=\2\u0494\u0495\t\21\2\2\u0495\u00c9\3\2\2\2\u0496\u0497\5\u00b6\\"+
		"\2\u0497\u0498\5\u00a4S\2\u0498\u0499\5\u00b6\\\2\u0499\u00cb\3\2\2\2"+
		"\u049a\u049b\t\22\2\2\u049b\u00cd\3\2\2\2\u049c\u049d\7[\2\2\u049d\u049f"+
		"\7e\2\2\u049e\u04a0\5z>\2\u049f\u049e\3\2\2\2\u049f\u04a0\3\2\2\2\u04a0"+
		"\u00cf\3\2\2\2\u04a1\u04a3\7X\2\2\u04a2\u04a4\5\u00b6\\\2\u04a3\u04a2"+
		"\3\2\2\2\u04a3\u04a4\3\2\2\2\u04a4\u00d1\3\2\2\2\u04a5\u04a7\7A\2\2\u04a6"+
		"\u04a8\7[\2\2\u04a7\u04a6\3\2\2\2\u04a7\u04a8\3\2\2\2\u04a8\u00d3\3\2"+
		"\2\2\u04a9\u04ab\7U\2\2\u04aa\u04ac\7[\2\2\u04ab\u04aa\3\2\2\2\u04ab\u04ac"+
		"\3\2\2\2\u04ac\u00d5\3\2\2\2\u04ad\u04ae\7M\2\2\u04ae\u04af\7[\2\2\u04af"+
		"\u00d7\3\2\2\2\u04b0\u04b1\7Q\2\2\u04b1\u00d9\3\2\2\2\u04b2\u04b3\7G\2"+
		"\2\u04b3\u04b4\5x=\2\u04b4\u00db\3\2\2\2\u04b5\u04be\7R\2\2\u04b6\u04bf"+
		"\5x=\2\u04b7\u04b8\5\u0146\u00a4\2\u04b8\u04b9\5x=\2\u04b9\u04bf\3\2\2"+
		"\2\u04ba\u04bb\5\u00c2b\2\u04bb\u04bc\5\u0146\u00a4\2\u04bc\u04bd\5x="+
		"\2\u04bd\u04bf\3\2\2\2\u04be\u04b6\3\2\2\2\u04be\u04b7\3\2\2\2\u04be\u04ba"+
		"\3\2\2\2\u04bf\u04c0\3\2\2\2\u04c0\u04c6\5\u00be`\2\u04c1\u04c4\7L\2\2"+
		"\u04c2\u04c5\5\u00dco\2\u04c3\u04c5\5\u00be`\2\u04c4\u04c2\3\2\2\2\u04c4"+
		"\u04c3\3\2\2\2\u04c5\u04c7\3\2\2\2\u04c6\u04c1\3\2\2\2\u04c6\u04c7\3\2"+
		"\2\2\u04c7\u00dd\3\2\2\2\u04c8\u04cb\5\u00e0q\2\u04c9\u04cb\5\u00e6t\2"+
		"\u04ca\u04c8\3\2\2\2\u04ca\u04c9\3\2\2\2\u04cb\u00df\3\2\2\2\u04cc\u04d7"+
		"\7O\2\2\u04cd\u04cf\5x=\2\u04ce\u04cd\3\2\2\2\u04ce\u04cf\3\2\2\2\u04cf"+
		"\u04d8\3\2\2\2\u04d0\u04d2\5\u00c2b\2\u04d1\u04d0\3\2\2\2\u04d1\u04d2"+
		"\3\2\2\2\u04d2\u04d3\3\2\2\2\u04d3\u04d5\5\u0146\u00a4\2\u04d4\u04d6\5"+
		"x=\2\u04d5\u04d4\3\2\2\2\u04d5\u04d6\3\2\2\2\u04d6\u04d8\3\2\2\2\u04d7"+
		"\u04ce\3\2\2\2\u04d7\u04d1\3\2\2\2\u04d8\u04d9\3\2\2\2\u04d9\u04dd\7^"+
		"\2\2\u04da\u04dc\5\u00e2r\2\u04db\u04da\3\2\2\2\u04dc\u04df\3\2\2\2\u04dd"+
		"\u04db\3\2\2\2\u04dd\u04de\3\2\2\2\u04de\u04e0\3\2\2\2\u04df\u04dd\3\2"+
		"\2\2\u04e0\u04e1\7_\2\2\u04e1\u00e1\3\2\2\2\u04e2\u04e3\5\u00e4s\2\u04e3"+
		"\u04e5\7e\2\2\u04e4\u04e6\5\u00c0a\2\u04e5\u04e4\3\2\2\2\u04e5\u04e6\3"+
		"\2\2\2\u04e6\u00e3\3\2\2\2\u04e7\u04e8\7F\2\2\u04e8\u04eb\5\u00b6\\\2"+
		"\u04e9\u04eb\7B\2\2\u04ea\u04e7\3\2\2\2\u04ea\u04e9\3\2\2\2\u04eb\u00e5"+
		"\3\2\2\2\u04ec\u04f5\7O\2\2\u04ed\u04f6\5\u00e8u\2\u04ee\u04ef\5\u0146"+
		"\u00a4\2\u04ef\u04f0\5\u00e8u\2\u04f0\u04f6\3\2\2\2\u04f1\u04f2\5\u00c2"+
		"b\2\u04f2\u04f3\5\u0146\u00a4\2\u04f3\u04f4\5\u00e8u\2\u04f4\u04f6\3\2"+
		"\2\2\u04f5\u04ed\3\2\2\2\u04f5\u04ee\3\2\2\2\u04f5\u04f1\3\2\2\2\u04f6"+
		"\u04f7\3\2\2\2\u04f7\u04fb\7^\2\2\u04f8\u04fa\5\u00eav\2\u04f9\u04f8\3"+
		"\2\2\2\u04fa\u04fd\3\2\2\2\u04fb\u04f9\3\2\2\2\u04fb\u04fc\3\2\2\2\u04fc"+
		"\u04fe\3\2\2\2\u04fd\u04fb\3\2\2\2\u04fe\u04ff\7_\2\2\u04ff\u00e7\3\2"+
		"\2\2\u0500\u0501\7[\2\2\u0501\u0503\7i\2\2\u0502\u0500\3\2\2\2\u0502\u0503"+
		"\3\2\2\2\u0503\u0504\3\2\2\2\u0504\u0505\5\u0086D\2\u0505\u0506\7f\2\2"+
		"\u0506\u0507\7\\\2\2\u0507\u0508\7T\2\2\u0508\u0509\7]\2\2\u0509\u00e9"+
		"\3\2\2\2\u050a\u050b\5\u00ecw\2\u050b\u050d\7e\2\2\u050c\u050e\5\u00c0"+
		"a\2\u050d\u050c\3\2\2\2\u050d\u050e\3\2\2\2\u050e\u00eb\3\2\2\2\u050f"+
		"\u0510\7F\2\2\u0510\u0513\5\u00eex\2\u0511\u0513\7B\2\2\u0512\u050f\3"+
		"\2\2\2\u0512\u0511\3\2\2\2\u0513\u00ed\3\2\2\2\u0514\u0517\5\u0090I\2"+
		"\u0515\u0517\7Z\2\2\u0516\u0514\3\2\2\2\u0516\u0515\3\2\2\2\u0517\u051f"+
		"\3\2\2\2\u0518\u051b\7c\2\2\u0519\u051c\5\u0090I\2\u051a\u051c\7Z\2\2"+
		"\u051b\u0519\3\2\2\2\u051b\u051a\3\2\2\2\u051c\u051e\3\2\2\2\u051d\u0518"+
		"\3\2\2\2\u051e\u0521\3\2\2\2\u051f\u051d\3\2\2\2\u051f\u0520\3\2\2\2\u0520"+
		"\u00ef\3\2\2\2\u0521\u051f\3\2\2\2\u0522\u0523\7E\2\2\u0523\u0527\7^\2"+
		"\2\u0524\u0526\5\u00f2z\2\u0525\u0524\3\2\2\2\u0526\u0529\3\2\2\2\u0527"+
		"\u0525\3\2\2\2\u0527\u0528\3\2\2\2\u0528\u052a\3\2\2\2\u0529\u0527\3\2"+
		"\2\2\u052a\u052b\7_\2\2\u052b\u00f1\3\2\2\2\u052c\u052d\5\u00f4{\2\u052d"+
		"\u052f\7e\2\2\u052e\u0530\5\u00c0a\2\u052f\u052e\3\2\2\2\u052f\u0530\3"+
		"\2\2\2\u0530\u00f3\3\2\2\2\u0531\u0534\7F\2\2\u0532\u0535\5\u00c6d\2\u0533"+
		"\u0535\5\u00f6|\2\u0534\u0532\3\2\2\2\u0534\u0533\3\2\2\2\u0535\u0538"+
		"\3\2\2\2\u0536\u0538\7B\2\2\u0537\u0531\3\2\2\2\u0537\u0536\3\2\2\2\u0538"+
		"\u00f5\3\2\2\2\u0539\u053a\5\u00b6\\\2\u053a\u053b\7b\2\2\u053b\u0540"+
		"\3\2\2\2\u053c\u053d\5\u00b4[\2\u053d\u053e\7i\2\2\u053e\u0540\3\2\2\2"+
		"\u053f\u0539\3\2\2\2\u053f\u053c\3\2\2\2\u053f\u0540\3\2\2\2\u0540\u0541"+
		"\3\2\2\2\u0541\u0542\5x=\2\u0542\u00f7\3\2\2\2\u0543\u0547\7V\2\2\u0544"+
		"\u0548\5x=\2\u0545\u0548\5\u00fa~\2\u0546\u0548\5\u00fc\177\2\u0547\u0544"+
		"\3\2\2\2\u0547\u0545\3\2\2\2\u0547\u0546\3\2\2\2\u0547\u0548\3\2\2\2\u0548"+
		"\u0549\3\2\2\2\u0549\u054a\5\u00be`\2\u054a\u00f9\3\2\2\2\u054b\u054d"+
		"\5\u00c2b\2\u054c\u054b\3\2\2\2\u054c\u054d\3\2\2\2\u054d\u054e\3\2\2"+
		"\2\u054e\u0550\5\u0146\u00a4\2\u054f\u0551\5x=\2\u0550\u054f\3\2\2\2\u0550"+
		"\u0551\3\2\2\2\u0551\u0552\3\2\2\2\u0552\u0554\5\u0146\u00a4\2\u0553\u0555"+
		"\5\u00c2b\2\u0554\u0553\3\2\2\2\u0554\u0555\3\2\2\2\u0555\u00fb\3\2\2"+
		"\2\u0556\u0557\5\u00b6\\\2\u0557\u0558\7b\2\2\u0558\u055d\3\2\2\2\u0559"+
		"\u055a\5\u00b4[\2\u055a\u055b\7i\2\2\u055b\u055d\3\2\2\2\u055c\u0556\3"+
		"\2\2\2\u055c\u0559\3\2\2\2\u055c\u055d\3\2\2\2\u055d\u055e\3\2\2\2\u055e"+
		"\u055f\7S\2\2\u055f\u0560\5x=\2\u0560\u00fd\3\2\2\2\u0561\u0562\7H\2\2"+
		"\u0562\u0563\5x=\2\u0563\u00ff\3\2\2\2\u0564\u0567\5\u0124\u0093\2\u0565"+
		"\u0567\7[\2\2\u0566\u0564\3\2\2\2\u0566\u0565\3\2\2\2\u0567\u0101\3\2"+
		"\2\2\u0568\u0569\7`\2\2\u0569\u056a\5\u0104\u0083\2\u056a\u056b\7a\2\2"+
		"\u056b\u056c\5\u0106\u0084\2\u056c\u0103\3\2\2\2\u056d\u056e\5x=\2\u056e"+
		"\u0105\3\2\2\2\u056f\u0570\5\u0090I\2\u0570\u0107\3\2\2\2\u0571\u0572"+
		"\7}\2\2\u0572\u0573\5\u0090I\2\u0573\u0109\3\2\2\2\u0574\u0575\7`\2\2"+
		"\u0575\u0576\7a\2\2\u0576\u0577\5\u0106\u0084\2\u0577\u010b\3\2\2\2\u0578"+
		"\u0579\7I\2\2\u0579\u057a\7`\2\2\u057a\u057b\5\u0090I\2\u057b\u057c\7"+
		"a\2\2\u057c\u057d\5\u0106\u0084\2\u057d\u010d\3\2\2\2\u057e\u0584\7K\2"+
		"\2\u057f\u0580\7K\2\2\u0580\u0584\7\177\2\2\u0581\u0582\7\177\2\2\u0582"+
		"\u0584\7K\2\2\u0583\u057e\3\2\2\2\u0583\u057f\3\2\2\2\u0583\u0581\3\2"+
		"\2\2\u0584\u0585\3\2\2\2\u0585\u0586\5\u0106\u0084\2\u0586\u010f\3\2\2"+
		"\2\u0587\u0588\7C\2\2\u0588\u0589\5\u0112\u008a\2\u0589\u0111\3\2\2\2"+
		"\u058a\u058b\5\u0116\u008c\2\u058b\u058c\5\u0114\u008b\2\u058c\u058f\3"+
		"\2\2\2\u058d\u058f\5\u0116\u008c\2\u058e\u058a\3\2\2\2\u058e\u058d\3\2"+
		"\2\2\u058f\u0113\3\2\2\2\u0590\u0593\5\u0116\u008c\2\u0591\u0593\5\u0090"+
		"I\2\u0592\u0590\3\2\2\2\u0592\u0591\3\2\2\2\u0593\u0115\3\2\2\2\u0594"+
		"\u05a0\7\\\2\2\u0595\u059a\5r:\2\u0596\u0597\7c\2\2\u0597\u0599\5r:\2"+
		"\u0598\u0596\3\2\2\2\u0599\u059c\3\2\2\2\u059a\u0598\3\2\2\2\u059a\u059b"+
		"\3\2\2\2\u059b\u059e\3\2\2\2\u059c\u059a\3\2\2\2\u059d\u059f\7c\2\2\u059e"+
		"\u059d\3\2\2\2\u059e\u059f\3\2\2\2\u059f\u05a1\3\2\2\2\u05a0\u0595\3\2"+
		"\2\2\u05a0\u05a1\3\2\2\2\u05a1\u05a2\3\2\2\2\u05a2\u05a3\7]\2\2\u05a3"+
		"\u0117\3\2\2\2\u05a4\u05a5\5\u011a\u008e\2\u05a5\u05a6\7\\\2\2\u05a6\u05a8"+
		"\5x=\2\u05a7\u05a9\7c\2\2\u05a8\u05a7\3\2\2\2\u05a8\u05a9\3\2\2\2\u05a9"+
		"\u05aa\3\2\2\2\u05aa\u05ab\7]\2\2\u05ab\u0119\3\2\2\2\u05ac\u05b2\5\u0092"+
		"J\2\u05ad\u05ae\7\\\2\2\u05ae\u05af\5\u011a\u008e\2\u05af\u05b0\7]\2\2"+
		"\u05b0\u05b2\3\2\2\2\u05b1\u05ac\3\2\2\2\u05b1\u05ad\3\2\2\2\u05b2\u011b"+
		"\3\2\2\2\u05b3\u05ba\5\u011e\u0090\2\u05b4\u05ba\5\u0122\u0092\2\u05b5"+
		"\u05b6\7\\\2\2\u05b6\u05b7\5x=\2\u05b7\u05b8\7]\2\2\u05b8\u05ba\3\2\2"+
		"\2\u05b9\u05b3\3\2\2\2\u05b9\u05b4\3\2\2\2\u05b9\u05b5\3\2\2\2\u05ba\u011d"+
		"\3\2\2\2\u05bb\u05bf\5\u0084C\2\u05bc\u05bf\5\u0126\u0094\2\u05bd\u05bf"+
		"\5\u013a\u009e\2\u05be\u05bb\3\2\2\2\u05be\u05bc\3\2\2\2\u05be\u05bd\3"+
		"\2\2\2\u05bf\u011f\3\2\2\2\u05c0\u05c1\t\23\2\2\u05c1\u0121\3\2\2\2\u05c2"+
		"\u05c3\7[\2\2\u05c3\u0123\3\2\2\2\u05c4\u05c5\7[\2\2\u05c5\u05c6\7f\2"+
		"\2\u05c6\u05c7\7[\2\2\u05c7\u0125\3\2\2\2\u05c8\u05c9\5\u0098M\2\u05c9"+
		"\u05ca\5\u0128\u0095\2\u05ca\u0127\3\2\2\2\u05cb\u05d0\7^\2\2\u05cc\u05ce"+
		"\5\u012a\u0096\2\u05cd\u05cf\7c\2\2\u05ce\u05cd\3\2\2\2\u05ce\u05cf\3"+
		"\2\2\2\u05cf\u05d1\3\2\2\2\u05d0\u05cc\3\2\2\2\u05d0\u05d1\3\2\2\2\u05d1"+
		"\u05d2\3\2\2\2\u05d2\u05d3\7_\2\2\u05d3\u0129\3\2\2\2\u05d4\u05d9\5\u012c"+
		"\u0097\2\u05d5\u05d6\7c\2\2\u05d6\u05d8\5\u012c\u0097\2\u05d7\u05d5\3"+
		"\2\2\2\u05d8\u05db\3\2\2\2\u05d9\u05d7\3\2\2\2\u05d9\u05da\3\2\2\2\u05da"+
		"\u012b\3\2\2\2\u05db\u05d9\3\2\2\2\u05dc\u05dd\5\u012e\u0098\2\u05dd\u05de"+
		"\7e\2\2\u05de\u05e0\3\2\2\2\u05df\u05dc\3\2\2\2\u05df\u05e0\3\2\2\2\u05e0"+
		"\u05e1\3\2\2\2\u05e1\u05e2\5\u0130\u0099\2\u05e2\u012d\3\2\2\2\u05e3\u05e6"+
		"\5x=\2\u05e4\u05e6\5\u0128\u0095\2\u05e5\u05e3\3\2\2\2\u05e5\u05e4\3\2"+
		"\2\2\u05e6\u012f\3\2\2\2\u05e7\u05ea\5x=\2\u05e8\u05ea\5\u0128\u0095\2"+
		"\u05e9\u05e7\3\2\2\2\u05e9\u05e8\3\2\2\2\u05ea\u0131\3\2\2\2\u05eb\u05ec"+
		"\7J\2\2\u05ec\u05f2\7^\2\2\u05ed\u05ee\5\u0134\u009b\2\u05ee\u05ef\5\u0146"+
		"\u00a4\2\u05ef\u05f1\3\2\2\2\u05f0\u05ed\3\2\2\2\u05f1\u05f4\3\2\2\2\u05f2"+
		"\u05f0\3\2\2\2\u05f2\u05f3\3\2\2\2\u05f3\u05f5\3\2\2\2\u05f4\u05f2\3\2"+
		"\2\2\u05f5\u05f6\7_\2\2\u05f6\u0133\3\2\2\2\u05f7\u05f8\5\u00b4[\2\u05f8"+
		"\u05f9\5\u0090I\2\u05f9\u05fc\3\2\2\2\u05fa\u05fc\5\u0138\u009d\2\u05fb"+
		"\u05f7\3\2\2\2\u05fb\u05fa\3\2\2\2\u05fc\u05fe\3\2\2\2\u05fd\u05ff\5\u0136"+
		"\u009c\2\u05fe\u05fd\3\2\2\2\u05fe\u05ff\3\2\2\2\u05ff\u0135\3\2\2\2\u0600"+
		"\u0601\t\24\2\2\u0601\u0137\3\2\2\2\u0602\u0604\7}\2\2\u0603\u0602\3\2"+
		"\2\2\u0603\u0604\3\2\2\2\u0604\u0605\3\2\2\2\u0605\u0606\5\u0100\u0081"+
		"\2\u0606\u0139\3\2\2\2\u0607\u0608\7C\2\2\u0608\u0609\5\u0112\u008a\2"+
		"\u0609\u060a\5\u00be`\2\u060a\u013b\3\2\2\2\u060b\u060c\7`\2\2\u060c\u060d"+
		"\5x=\2\u060d\u060e\7a\2\2\u060e\u013d\3\2\2\2\u060f\u0610\7f\2\2\u0610"+
		"\u0611\7\\\2\2\u0611\u0612\5\u0090I\2\u0612\u0613\7]\2\2\u0613\u013f\3"+
		"\2\2\2\u0614\u0623\7\\\2\2\u0615\u061c\5\u00b6\\\2\u0616\u0619\5\u011a"+
		"\u008e\2\u0617\u0618\7c\2\2\u0618\u061a\5\u00b6\\\2\u0619\u0617\3\2\2"+
		"\2\u0619\u061a\3\2\2\2\u061a\u061c\3\2\2\2\u061b\u0615\3\2\2\2\u061b\u0616"+
		"\3\2\2\2\u061c\u061e\3\2\2\2\u061d\u061f\7j\2\2\u061e\u061d\3\2\2\2\u061e"+
		"\u061f\3\2\2\2\u061f\u0621\3\2\2\2\u0620\u0622\7c\2\2\u0621\u0620\3\2"+
		"\2\2\u0621\u0622\3\2\2\2\u0622\u0624\3\2\2\2\u0623\u061b\3\2\2\2\u0623"+
		"\u0624\3\2\2\2\u0624\u0625\3\2\2\2\u0625\u0626\7]\2\2\u0626\u0141\3\2"+
		"\2\2\u0627\u0628\5\u011a\u008e\2\u0628\u0629\7f\2\2\u0629\u062a\7[\2\2"+
		"\u062a\u0143\3\2\2\2\u062b\u062c\5\u0090I\2\u062c\u0145\3\2\2\2\u062d"+
		"\u0632\7d\2\2\u062e\u0632\7\2\2\3\u062f\u0632\7\u0095\2\2\u0630\u0632"+
		"\6\u00a4\22\2\u0631\u062d\3\2\2\2\u0631\u062e\3\2\2\2\u0631\u062f\3\2"+
		"\2\2\u0631\u0630\3\2\2\2\u0632\u0147\3\2\2\2\u00a2\u0156\u015b\u0163\u0171"+
		"\u0186\u018a\u0191\u0199\u01a2\u01c2\u01ca\u01dd\u01ee\u01fa\u0203\u0211"+
		"\u0223\u0228\u022d\u0231\u023b\u023e\u0242\u0246\u024d\u0250\u025d\u0265"+
		"\u0269\u026c\u0272\u0276\u0279\u0283\u028a\u0291\u029f\u02a7\u02b0\u02b7"+
		"\u02bd\u02c6\u02cc\u02d2\u02de\u02e4\u02e8\u02f0\u02f4\u02f9\u02fc\u0301"+
		"\u0315\u0335\u0337\u034c\u0354\u035f\u0366\u0370\u0379\u038a\u038c\u0391"+
		"\u0394\u039d\u03a3\u03ad\u03b5\u03bb\u03c4\u03cf\u03da\u03de\u03e0\u03eb"+
		"\u03f4\u03f8\u03fb\u0402\u040d\u041c\u0420\u0423\u042c\u0436\u043a\u043e"+
		"\u0442\u0449\u0451\u045c\u0460\u0464\u0470\u0474\u0478\u047d\u0484\u048b"+
		"\u049f\u04a3\u04a7\u04ab\u04be\u04c4\u04c6\u04ca\u04ce\u04d1\u04d5\u04d7"+
		"\u04dd\u04e5\u04ea\u04f5\u04fb\u0502\u050d\u0512\u0516\u051b\u051f\u0527"+
		"\u052f\u0534\u0537\u053f\u0547\u054c\u0550\u0554\u055c\u0566\u0583\u058e"+
		"\u0592\u059a\u059e\u05a0\u05a8\u05b1\u05b9\u05be\u05ce\u05d0\u05d9\u05df"+
		"\u05e5\u05e9\u05f2\u05fb\u05fe\u0603\u0619\u061b\u061e\u0621\u0623\u0631";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}