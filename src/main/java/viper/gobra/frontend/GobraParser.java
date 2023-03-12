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
		LEN=44, NEW=45, MAKE=46, CAP=47, SOME=48, GET=49, DOM=50, AXIOM=51, ADT=52, 
		MATCH=53, NONE=54, PRED=55, TYPE_OF=56, IS_COMPARABLE=57, SHARE=58, ADDR_MOD=59, 
		DOT_DOT=60, SHARED=61, EXCLUSIVE=62, PREDICATE=63, PRIVATE=64, WRITEPERM=65, 
		NOPERM=66, TRUSTED=67, OUTLINE=68, INIT_POST=69, IMPORT_PRE=70, PROOF=71, 
		GHOST_EQUALS=72, GHOST_NOT_EQUALS=73, WITH=74, CONSTRUCT=75, DEREF=76, 
		ASSIGN_STRUCT=77, BREAK=78, DEFAULT=79, FUNC=80, INTERFACE=81, SELECT=82, 
		CASE=83, DEFER=84, GO=85, MAP=86, STRUCT=87, CHAN=88, ELSE=89, GOTO=90, 
		PACKAGE=91, SWITCH=92, CONST=93, FALLTHROUGH=94, IF=95, RANGE=96, TYPE=97, 
		CONTINUE=98, FOR=99, IMPORT=100, RETURN=101, VAR=102, NIL_LIT=103, IDENTIFIER=104, 
		L_PAREN=105, R_PAREN=106, L_CURLY=107, R_CURLY=108, L_BRACKET=109, R_BRACKET=110, 
		ASSIGN=111, COMMA=112, SEMI=113, COLON=114, DOT=115, PLUS_PLUS=116, MINUS_MINUS=117, 
		DECLARE_ASSIGN=118, ELLIPSIS=119, LOGICAL_OR=120, LOGICAL_AND=121, EQUALS=122, 
		NOT_EQUALS=123, LESS=124, LESS_OR_EQUALS=125, GREATER=126, GREATER_OR_EQUALS=127, 
		OR=128, DIV=129, MOD=130, LSHIFT=131, RSHIFT=132, BIT_CLEAR=133, EXCLAMATION=134, 
		PLUS=135, MINUS=136, CARET=137, STAR=138, AMPERSAND=139, RECEIVE=140, 
		DECIMAL_LIT=141, BINARY_LIT=142, OCTAL_LIT=143, HEX_LIT=144, HEX_FLOAT_LIT=145, 
		IMAGINARY_LIT=146, RUNE_LIT=147, BYTE_VALUE=148, OCTAL_BYTE_VALUE=149, 
		HEX_BYTE_VALUE=150, LITTLE_U_VALUE=151, BIG_U_VALUE=152, RAW_STRING_LIT=153, 
		INTERPRETED_STRING_LIT=154, WS=155, COMMENT=156, TERMINATOR=157, LINE_COMMENT=158, 
		WS_NLSEMI=159, COMMENT_NLSEMI=160, LINE_COMMENT_NLSEMI=161, EOS=162, OTHER=163;
	public static final int
		RULE_exprOnly = 0, RULE_stmtOnly = 1, RULE_typeOnly = 2, RULE_maybeAddressableIdentifierList = 3, 
		RULE_maybeAddressableIdentifier = 4, RULE_sourceFile = 5, RULE_initPost = 6, 
		RULE_importPre = 7, RULE_importSpec = 8, RULE_importDecl = 9, RULE_ghostMember = 10, 
		RULE_ghostStatement = 11, RULE_auxiliaryStatement = 12, RULE_statementWithSpec = 13, 
		RULE_outlineStatement = 14, RULE_ghostPrimaryExpr = 15, RULE_permission = 16, 
		RULE_typeExpr = 17, RULE_boundVariables = 18, RULE_boundVariableDecl = 19, 
		RULE_triggers = 20, RULE_trigger = 21, RULE_predicateAccess = 22, RULE_optionSome = 23, 
		RULE_optionNone = 24, RULE_optionGet = 25, RULE_sConversion = 26, RULE_old = 27, 
		RULE_oldLabelUse = 28, RULE_labelUse = 29, RULE_before = 30, RULE_isComparable = 31, 
		RULE_typeOf = 32, RULE_access = 33, RULE_range = 34, RULE_matchExpr = 35, 
		RULE_matchExprClause = 36, RULE_seqUpdExp = 37, RULE_seqUpdClause = 38, 
		RULE_ghostTypeLit = 39, RULE_domainType = 40, RULE_domainClause = 41, 
		RULE_adtType = 42, RULE_adtClause = 43, RULE_ghostSliceType = 44, RULE_sqType = 45, 
		RULE_specification = 46, RULE_specStatement = 47, RULE_terminationMeasure = 48, 
		RULE_assertion = 49, RULE_privateSpec = 50, RULE_privateEntailmentProof = 51, 
		RULE_matchStmt = 52, RULE_matchStmtClause = 53, RULE_matchCase = 54, RULE_matchPattern = 55, 
		RULE_matchPatternList = 56, RULE_blockWithBodyParameterInfo = 57, RULE_closureSpecInstance = 58, 
		RULE_closureSpecParams = 59, RULE_closureSpecParam = 60, RULE_closureImplProofStmt = 61, 
		RULE_implementationProof = 62, RULE_methodImplementationProof = 63, RULE_nonLocalReceiver = 64, 
		RULE_selection = 65, RULE_implementationProofPredicateAlias = 66, RULE_make = 67, 
		RULE_new_ = 68, RULE_specMember = 69, RULE_functionDecl = 70, RULE_methodDecl = 71, 
		RULE_explicitGhostMember = 72, RULE_fpredicateDecl = 73, RULE_predicateBody = 74, 
		RULE_mpredicateDecl = 75, RULE_constructDecl = 76, RULE_derefDecl = 77, 
		RULE_assignDecl = 78, RULE_varSpec = 79, RULE_shortVarDecl = 80, RULE_receiver = 81, 
		RULE_parameterDecl = 82, RULE_actualParameterDecl = 83, RULE_ghostParameterDecl = 84, 
		RULE_parameterType = 85, RULE_expression = 86, RULE_statement = 87, RULE_applyStmt = 88, 
		RULE_packageStmt = 89, RULE_specForStmt = 90, RULE_loopSpec = 91, RULE_deferStmt = 92, 
		RULE_basicLit = 93, RULE_primaryExpr = 94, RULE_functionLit = 95, RULE_closureDecl = 96, 
		RULE_predConstructArgs = 97, RULE_interfaceType = 98, RULE_predicateSpec = 99, 
		RULE_methodSpec = 100, RULE_type_ = 101, RULE_typeLit = 102, RULE_predType = 103, 
		RULE_predTypeParams = 104, RULE_literalType = 105, RULE_implicitArray = 106, 
		RULE_slice_ = 107, RULE_low = 108, RULE_high = 109, RULE_cap = 110, RULE_assign_op = 111, 
		RULE_rangeClause = 112, RULE_packageClause = 113, RULE_importPath = 114, 
		RULE_declaration = 115, RULE_constDecl = 116, RULE_constSpec = 117, RULE_identifierList = 118, 
		RULE_expressionList = 119, RULE_typeDecl = 120, RULE_typeSpec = 121, RULE_varDecl = 122, 
		RULE_block = 123, RULE_statementList = 124, RULE_simpleStmt = 125, RULE_expressionStmt = 126, 
		RULE_sendStmt = 127, RULE_incDecStmt = 128, RULE_assignment = 129, RULE_emptyStmt = 130, 
		RULE_labeledStmt = 131, RULE_returnStmt = 132, RULE_breakStmt = 133, RULE_continueStmt = 134, 
		RULE_gotoStmt = 135, RULE_fallthroughStmt = 136, RULE_ifStmt = 137, RULE_switchStmt = 138, 
		RULE_exprSwitchStmt = 139, RULE_exprCaseClause = 140, RULE_exprSwitchCase = 141, 
		RULE_typeSwitchStmt = 142, RULE_typeSwitchGuard = 143, RULE_typeCaseClause = 144, 
		RULE_typeSwitchCase = 145, RULE_typeList = 146, RULE_selectStmt = 147, 
		RULE_commClause = 148, RULE_commCase = 149, RULE_recvStmt = 150, RULE_forStmt = 151, 
		RULE_forClause = 152, RULE_goStmt = 153, RULE_typeName = 154, RULE_arrayType = 155, 
		RULE_arrayLength = 156, RULE_elementType = 157, RULE_pointerType = 158, 
		RULE_sliceType = 159, RULE_mapType = 160, RULE_channelType = 161, RULE_functionType = 162, 
		RULE_signature = 163, RULE_result = 164, RULE_parameters = 165, RULE_conversion = 166, 
		RULE_nonNamedType = 167, RULE_operand = 168, RULE_literal = 169, RULE_integer = 170, 
		RULE_operandName = 171, RULE_qualifiedIdent = 172, RULE_compositeLit = 173, 
		RULE_literalValue = 174, RULE_elementList = 175, RULE_keyedElement = 176, 
		RULE_key = 177, RULE_element = 178, RULE_structType = 179, RULE_fieldDecl = 180, 
		RULE_string_ = 181, RULE_embeddedField = 182, RULE_index = 183, RULE_typeAssertion = 184, 
		RULE_arguments = 185, RULE_methodExpr = 186, RULE_receiverType = 187, 
		RULE_eos = 188;
	private static String[] makeRuleNames() {
		return new String[] {
			"exprOnly", "stmtOnly", "typeOnly", "maybeAddressableIdentifierList", 
			"maybeAddressableIdentifier", "sourceFile", "initPost", "importPre", 
			"importSpec", "importDecl", "ghostMember", "ghostStatement", "auxiliaryStatement", 
			"statementWithSpec", "outlineStatement", "ghostPrimaryExpr", "permission", 
			"typeExpr", "boundVariables", "boundVariableDecl", "triggers", "trigger", 
			"predicateAccess", "optionSome", "optionNone", "optionGet", "sConversion", 
			"old", "oldLabelUse", "labelUse", "before", "isComparable", "typeOf", 
			"access", "range", "matchExpr", "matchExprClause", "seqUpdExp", "seqUpdClause", 
			"ghostTypeLit", "domainType", "domainClause", "adtType", "adtClause", 
			"ghostSliceType", "sqType", "specification", "specStatement", "terminationMeasure", 
			"assertion", "privateSpec", "privateEntailmentProof", "matchStmt", "matchStmtClause", 
			"matchCase", "matchPattern", "matchPatternList", "blockWithBodyParameterInfo", 
			"closureSpecInstance", "closureSpecParams", "closureSpecParam", "closureImplProofStmt", 
			"implementationProof", "methodImplementationProof", "nonLocalReceiver", 
			"selection", "implementationProofPredicateAlias", "make", "new_", "specMember", 
			"functionDecl", "methodDecl", "explicitGhostMember", "fpredicateDecl", 
			"predicateBody", "mpredicateDecl", "constructDecl", "derefDecl", "assignDecl", 
			"varSpec", "shortVarDecl", "receiver", "parameterDecl", "actualParameterDecl", 
			"ghostParameterDecl", "parameterType", "expression", "statement", "applyStmt", 
			"packageStmt", "specForStmt", "loopSpec", "deferStmt", "basicLit", "primaryExpr", 
			"functionLit", "closureDecl", "predConstructArgs", "interfaceType", "predicateSpec", 
			"methodSpec", "type_", "typeLit", "predType", "predTypeParams", "literalType", 
			"implicitArray", "slice_", "low", "high", "cap", "assign_op", "rangeClause", 
			"packageClause", "importPath", "declaration", "constDecl", "constSpec", 
			"identifierList", "expressionList", "typeDecl", "typeSpec", "varDecl", 
			"block", "statementList", "simpleStmt", "expressionStmt", "sendStmt", 
			"incDecStmt", "assignment", "emptyStmt", "labeledStmt", "returnStmt", 
			"breakStmt", "continueStmt", "gotoStmt", "fallthroughStmt", "ifStmt", 
			"switchStmt", "exprSwitchStmt", "exprCaseClause", "exprSwitchCase", "typeSwitchStmt", 
			"typeSwitchGuard", "typeCaseClause", "typeSwitchCase", "typeList", "selectStmt", 
			"commClause", "commCase", "recvStmt", "forStmt", "forClause", "goStmt", 
			"typeName", "arrayType", "arrayLength", "elementType", "pointerType", 
			"sliceType", "mapType", "channelType", "functionType", "signature", "result", 
			"parameters", "conversion", "nonNamedType", "operand", "literal", "integer", 
			"operandName", "qualifiedIdent", "compositeLit", "literalValue", "elementList", 
			"keyedElement", "key", "element", "structType", "fieldDecl", "string_", 
			"embeddedField", "index", "typeAssertion", "arguments", "methodExpr", 
			"receiverType", "eos"
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
			"'some'", "'get'", "'domain'", "'axiom'", "'adt'", "'match'", "'none'", 
			"'pred'", "'typeOf'", "'isComparable'", "'share'", "'@'", "'..'", "'shared'", 
			"'exclusive'", "'predicate'", "'private'", "'writePerm'", "'noPerm'", 
			"'trusted'", "'outline'", "'initEnsures'", "'importRequires'", "'proof'", 
			"'==='", "'!=='", "'with'", "'construct'", "'deref'", "'assign'", "'break'", 
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
			"GET", "DOM", "AXIOM", "ADT", "MATCH", "NONE", "PRED", "TYPE_OF", "IS_COMPARABLE", 
			"SHARE", "ADDR_MOD", "DOT_DOT", "SHARED", "EXCLUSIVE", "PREDICATE", "PRIVATE", 
			"WRITEPERM", "NOPERM", "TRUSTED", "OUTLINE", "INIT_POST", "IMPORT_PRE", 
			"PROOF", "GHOST_EQUALS", "GHOST_NOT_EQUALS", "WITH", "CONSTRUCT", "DEREF", 
			"ASSIGN_STRUCT", "BREAK", "DEFAULT", "FUNC", "INTERFACE", "SELECT", "CASE", 
			"DEFER", "GO", "MAP", "STRUCT", "CHAN", "ELSE", "GOTO", "PACKAGE", "SWITCH", 
			"CONST", "FALLTHROUGH", "IF", "RANGE", "TYPE", "CONTINUE", "FOR", "IMPORT", 
			"RETURN", "VAR", "NIL_LIT", "IDENTIFIER", "L_PAREN", "R_PAREN", "L_CURLY", 
			"R_CURLY", "L_BRACKET", "R_BRACKET", "ASSIGN", "COMMA", "SEMI", "COLON", 
			"DOT", "PLUS_PLUS", "MINUS_MINUS", "DECLARE_ASSIGN", "ELLIPSIS", "LOGICAL_OR", 
			"LOGICAL_AND", "EQUALS", "NOT_EQUALS", "LESS", "LESS_OR_EQUALS", "GREATER", 
			"GREATER_OR_EQUALS", "OR", "DIV", "MOD", "LSHIFT", "RSHIFT", "BIT_CLEAR", 
			"EXCLAMATION", "PLUS", "MINUS", "CARET", "STAR", "AMPERSAND", "RECEIVE", 
			"DECIMAL_LIT", "BINARY_LIT", "OCTAL_LIT", "HEX_LIT", "HEX_FLOAT_LIT", 
			"IMAGINARY_LIT", "RUNE_LIT", "BYTE_VALUE", "OCTAL_BYTE_VALUE", "HEX_BYTE_VALUE", 
			"LITTLE_U_VALUE", "BIG_U_VALUE", "RAW_STRING_LIT", "INTERPRETED_STRING_LIT", 
			"WS", "COMMENT", "TERMINATOR", "LINE_COMMENT", "WS_NLSEMI", "COMMENT_NLSEMI", 
			"LINE_COMMENT_NLSEMI", "EOS", "OTHER"
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
			setState(378);
			expression(0);
			setState(379);
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
			setState(381);
			statement();
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
			setState(384);
			type_();
			setState(385);
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
			setState(387);
			maybeAddressableIdentifier();
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(388);
				match(COMMA);
				setState(389);
				maybeAddressableIdentifier();
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
			setState(395);
			match(IDENTIFIER);
			setState(397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADDR_MOD) {
				{
				setState(396);
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
		public List<InitPostContext> initPost() {
			return getRuleContexts(InitPostContext.class);
		}
		public InitPostContext initPost(int i) {
			return getRuleContext(InitPostContext.class,i);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INIT_POST) {
				{
				{
				setState(399);
				initPost();
				setState(400);
				eos();
				}
				}
				setState(406);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(407);
			packageClause();
			setState(408);
			eos();
			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT_PRE || _la==IMPORT) {
				{
				{
				setState(409);
				importDecl();
				setState(410);
				eos();
				}
				}
				setState(416);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (TRUSTED - 64)) | (1L << (CONSTRUCT - 64)) | (1L << (DEREF - 64)) | (1L << (ASSIGN_STRUCT - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (CONST - 64)) | (1L << (TYPE - 64)) | (1L << (VAR - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || _la==STAR || _la==RECEIVE) {
				{
				{
				setState(420);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(417);
					specMember();
					}
					break;
				case 2:
					{
					setState(418);
					declaration();
					}
					break;
				case 3:
					{
					setState(419);
					ghostMember();
					}
					break;
				}
				setState(422);
				eos();
				}
				}
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(429);
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

	public static class InitPostContext extends ParserRuleContext {
		public TerminalNode INIT_POST() { return getToken(GobraParser.INIT_POST, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InitPostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initPost; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitInitPost(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitPostContext initPost() throws RecognitionException {
		InitPostContext _localctx = new InitPostContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_initPost);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			match(INIT_POST);
			setState(432);
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

	public static class ImportPreContext extends ParserRuleContext {
		public TerminalNode IMPORT_PRE() { return getToken(GobraParser.IMPORT_PRE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ImportPreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importPre; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitImportPre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportPreContext importPre() throws RecognitionException {
		ImportPreContext _localctx = new ImportPreContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_importPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(434);
			match(IMPORT_PRE);
			setState(435);
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

	public static class ImportSpecContext extends ParserRuleContext {
		public Token alias;
		public ImportPathContext importPath() {
			return getRuleContext(ImportPathContext.class,0);
		}
		public List<ImportPreContext> importPre() {
			return getRuleContexts(ImportPreContext.class);
		}
		public ImportPreContext importPre(int i) {
			return getRuleContext(ImportPreContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
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
		enterRule(_localctx, 16, RULE_importSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT_PRE) {
				{
				{
				setState(437);
				importPre();
				setState(438);
				eos();
				}
				}
				setState(444);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==DOT) {
				{
				setState(445);
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

			setState(448);
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
		public TerminalNode L_PAREN() { return getToken(GobraParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(GobraParser.R_PAREN, 0); }
		public List<ImportPreContext> importPre() {
			return getRuleContexts(ImportPreContext.class);
		}
		public ImportPreContext importPre(int i) {
			return getRuleContext(ImportPreContext.class,i);
		}
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
		enterRule(_localctx, 18, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT_PRE) {
				{
				{
				setState(450);
				importPre();
				setState(451);
				eos();
				}
				}
				setState(457);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(458);
				match(IMPORT);
				setState(459);
				importSpec();
				}
				break;
			case 2:
				{
				setState(460);
				match(IMPORT);
				setState(461);
				match(L_PAREN);
				setState(467);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (IMPORT_PRE - 70)) | (1L << (IDENTIFIER - 70)) | (1L << (DOT - 70)))) != 0) || _la==RAW_STRING_LIT || _la==INTERPRETED_STRING_LIT) {
					{
					{
					setState(462);
					importSpec();
					setState(463);
					eos();
					}
					}
					setState(469);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(470);
				match(R_PAREN);
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
		enterRule(_localctx, 20, RULE_ghostMember);
		try {
			setState(477);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(473);
				implementationProof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(474);
				fpredicateDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(475);
				mpredicateDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(476);
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
	public static class MatchStmt_Context extends GhostStatementContext {
		public MatchStmtContext matchStmt() {
			return getRuleContext(MatchStmtContext.class,0);
		}
		public MatchStmt_Context(GhostStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchStmt_(this);
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
		enterRule(_localctx, 22, RULE_ghostStatement);
		int _la;
		try {
			setState(486);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
				_localctx = new ExplicitGhostStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(479);
				match(GHOST);
				setState(480);
				statement();
				}
				break;
			case FOLD:
			case UNFOLD:
				_localctx = new FoldStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(481);
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
				setState(482);
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
				setState(483);
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
				setState(484);
				expression(0);
				}
				break;
			case MATCH:
				_localctx = new MatchStmt_Context(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(485);
				matchStmt();
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
		enterRule(_localctx, 24, RULE_auxiliaryStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
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
		enterRule(_localctx, 26, RULE_statementWithSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			((StatementWithSpecContext)_localctx).specification = specification();
			{
			setState(491);
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
		enterRule(_localctx, 28, RULE_outlineStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			match(OUTLINE);
			setState(494);
			match(L_PAREN);
			setState(496);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(495);
				statementList();
				}
				break;
			}
			setState(498);
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
		public MatchExprContext matchExpr() {
			return getRuleContext(MatchExprContext.class,0);
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
		enterRule(_localctx, 30, RULE_ghostPrimaryExpr);
		try {
			setState(513);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(500);
				range();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(501);
				access();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(502);
				typeOf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(503);
				typeExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(504);
				isComparable();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(505);
				old();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(506);
				before();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(507);
				sConversion();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(508);
				optionNone();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(509);
				optionSome();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(510);
				optionGet();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(511);
				permission();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(512);
				matchExpr();
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
		enterRule(_localctx, 32, RULE_permission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
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
		enterRule(_localctx, 34, RULE_typeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(517);
			match(TYPE);
			setState(518);
			match(L_BRACKET);
			setState(519);
			type_();
			setState(520);
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
		enterRule(_localctx, 36, RULE_boundVariables);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			boundVariableDecl();
			setState(527);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(523);
					match(COMMA);
					setState(524);
					boundVariableDecl();
					}
					} 
				}
				setState(529);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(530);
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
		enterRule(_localctx, 38, RULE_boundVariableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(533);
			match(IDENTIFIER);
			setState(538);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(534);
				match(COMMA);
				setState(535);
				match(IDENTIFIER);
				}
				}
				setState(540);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(541);
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
		enterRule(_localctx, 40, RULE_triggers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==L_CURLY) {
				{
				{
				setState(543);
				trigger();
				}
				}
				setState(548);
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
		enterRule(_localctx, 42, RULE_trigger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			match(L_CURLY);
			setState(550);
			expression(0);
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(551);
				match(COMMA);
				setState(552);
				expression(0);
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
		enterRule(_localctx, 44, RULE_predicateAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(560);
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
		enterRule(_localctx, 46, RULE_optionSome);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			match(SOME);
			setState(563);
			match(L_PAREN);
			setState(564);
			expression(0);
			setState(565);
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
		enterRule(_localctx, 48, RULE_optionNone);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(567);
			match(NONE);
			setState(568);
			match(L_BRACKET);
			setState(569);
			type_();
			setState(570);
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
		enterRule(_localctx, 50, RULE_optionGet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(572);
			match(GET);
			setState(573);
			match(L_PAREN);
			setState(574);
			expression(0);
			setState(575);
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
		enterRule(_localctx, 52, RULE_sConversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
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
			setState(578);
			match(L_PAREN);
			setState(579);
			expression(0);
			setState(580);
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
		enterRule(_localctx, 54, RULE_old);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582);
			match(OLD);
			setState(587);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==L_BRACKET) {
				{
				setState(583);
				match(L_BRACKET);
				setState(584);
				oldLabelUse();
				setState(585);
				match(R_BRACKET);
				}
			}

			setState(589);
			match(L_PAREN);
			setState(590);
			expression(0);
			setState(591);
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
		enterRule(_localctx, 56, RULE_oldLabelUse);
		try {
			setState(595);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(593);
				labelUse();
				}
				break;
			case LHS:
				enterOuterAlt(_localctx, 2);
				{
				setState(594);
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
		enterRule(_localctx, 58, RULE_labelUse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(597);
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
		enterRule(_localctx, 60, RULE_before);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(599);
			match(BEFORE);
			setState(600);
			match(L_PAREN);
			setState(601);
			expression(0);
			setState(602);
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
		enterRule(_localctx, 62, RULE_isComparable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(604);
			match(IS_COMPARABLE);
			setState(605);
			match(L_PAREN);
			setState(606);
			expression(0);
			setState(607);
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
		enterRule(_localctx, 64, RULE_typeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			match(TYPE_OF);
			setState(610);
			match(L_PAREN);
			setState(611);
			expression(0);
			setState(612);
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
		enterRule(_localctx, 66, RULE_access);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(614);
			match(ACCESS);
			setState(615);
			match(L_PAREN);
			setState(616);
			expression(0);
			setState(619);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(617);
				match(COMMA);
				setState(618);
				expression(0);
				}
			}

			setState(621);
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
		enterRule(_localctx, 68, RULE_range);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(623);
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
			setState(624);
			match(L_BRACKET);
			setState(625);
			expression(0);
			setState(626);
			match(DOT_DOT);
			setState(627);
			expression(0);
			setState(628);
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

	public static class MatchExprContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(GobraParser.MATCH, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<MatchExprClauseContext> matchExprClause() {
			return getRuleContexts(MatchExprClauseContext.class);
		}
		public MatchExprClauseContext matchExprClause(int i) {
			return getRuleContext(MatchExprClauseContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public MatchExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchExprContext matchExpr() throws RecognitionException {
		MatchExprContext _localctx = new MatchExprContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_matchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(630);
			match(MATCH);
			setState(631);
			expression(0);
			setState(632);
			match(L_CURLY);
			setState(638);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(633);
				matchExprClause();
				setState(634);
				eos();
				}
				}
				setState(640);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(641);
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

	public static class MatchExprClauseContext extends ParserRuleContext {
		public MatchCaseContext matchCase() {
			return getRuleContext(MatchCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public MatchExprClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchExprClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchExprClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchExprClauseContext matchExprClause() throws RecognitionException {
		MatchExprClauseContext _localctx = new MatchExprClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_matchExprClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			matchCase();
			setState(644);
			match(COLON);
			setState(645);
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
		enterRule(_localctx, 74, RULE_seqUpdExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647);
			match(L_BRACKET);
			{
			setState(648);
			seqUpdClause();
			setState(653);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(649);
				match(COMMA);
				setState(650);
				seqUpdClause();
				}
				}
				setState(655);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(656);
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
		enterRule(_localctx, 76, RULE_seqUpdClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(658);
			expression(0);
			setState(659);
			match(ASSIGN);
			setState(660);
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
		public AdtTypeContext adtType() {
			return getRuleContext(AdtTypeContext.class,0);
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
		enterRule(_localctx, 78, RULE_ghostTypeLit);
		try {
			setState(666);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				setState(662);
				sqType();
				}
				break;
			case GHOST:
				enterOuterAlt(_localctx, 2);
				{
				setState(663);
				ghostSliceType();
				}
				break;
			case DOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(664);
				domainType();
				}
				break;
			case ADT:
				enterOuterAlt(_localctx, 4);
				{
				setState(665);
				adtType();
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
		enterRule(_localctx, 80, RULE_domainType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(668);
			match(DOM);
			setState(669);
			match(L_CURLY);
			setState(675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AXIOM || _la==FUNC) {
				{
				{
				setState(670);
				domainClause();
				setState(671);
				eos();
				}
				}
				setState(677);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(678);
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
		enterRule(_localctx, 82, RULE_domainClause);
		try {
			setState(689);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(680);
				match(FUNC);
				setState(681);
				match(IDENTIFIER);
				setState(682);
				signature();
				}
				break;
			case AXIOM:
				enterOuterAlt(_localctx, 2);
				{
				setState(683);
				match(AXIOM);
				setState(684);
				match(L_CURLY);
				setState(685);
				expression(0);
				setState(686);
				eos();
				setState(687);
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

	public static class AdtTypeContext extends ParserRuleContext {
		public TerminalNode ADT() { return getToken(GobraParser.ADT, 0); }
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<AdtClauseContext> adtClause() {
			return getRuleContexts(AdtClauseContext.class);
		}
		public AdtClauseContext adtClause(int i) {
			return getRuleContext(AdtClauseContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public AdtTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adtType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAdtType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdtTypeContext adtType() throws RecognitionException {
		AdtTypeContext _localctx = new AdtTypeContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_adtType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(691);
			match(ADT);
			setState(692);
			match(L_CURLY);
			setState(698);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(693);
				adtClause();
				setState(694);
				eos();
				}
				}
				setState(700);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(701);
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

	public static class AdtClauseContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
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
		public AdtClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adtClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAdtClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdtClauseContext adtClause() throws RecognitionException {
		AdtClauseContext _localctx = new AdtClauseContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_adtClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(703);
			match(IDENTIFIER);
			setState(704);
			match(L_CURLY);
			setState(710);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(705);
				fieldDecl();
				setState(706);
				eos();
				}
				}
				setState(712);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(713);
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
		enterRule(_localctx, 88, RULE_ghostSliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(715);
			match(GHOST);
			setState(716);
			match(L_BRACKET);
			setState(717);
			match(R_BRACKET);
			setState(718);
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
		enterRule(_localctx, 90, RULE_sqType);
		int _la;
		try {
			setState(731);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEQ:
			case SET:
			case MSET:
			case OPT:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(720);
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
				setState(721);
				match(L_BRACKET);
				setState(722);
				type_();
				setState(723);
				match(R_BRACKET);
				}
				}
				break;
			case DICT:
				enterOuterAlt(_localctx, 2);
				{
				setState(725);
				((SqTypeContext)_localctx).kind = match(DICT);
				setState(726);
				match(L_BRACKET);
				setState(727);
				type_();
				setState(728);
				match(R_BRACKET);
				setState(729);
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
		enterRule(_localctx, 92, RULE_specification);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(743);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(738);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PRE:
					case PRESERVES:
					case POST:
					case DEC:
					case PRIVATE:
						{
						setState(733);
						specStatement();
						}
						break;
					case PURE:
						{
						setState(734);
						match(PURE);
						((SpecificationContext)_localctx).pure =  true;
						}
						break;
					case TRUSTED:
						{
						setState(736);
						match(TRUSTED);
						((SpecificationContext)_localctx).trusted =  true;
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(740);
					eos();
					}
					} 
				}
				setState(745);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(748);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(746);
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
		public PrivateSpecContext privateSpec() {
			return getRuleContext(PrivateSpecContext.class,0);
		}
		public TerminalNode PRIVATE() { return getToken(GobraParser.PRIVATE, 0); }
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
		enterRule(_localctx, 94, RULE_specStatement);
		try {
			setState(760);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
				enterOuterAlt(_localctx, 1);
				{
				setState(750);
				((SpecStatementContext)_localctx).kind = match(PRE);
				setState(751);
				assertion();
				}
				break;
			case PRESERVES:
				enterOuterAlt(_localctx, 2);
				{
				setState(752);
				((SpecStatementContext)_localctx).kind = match(PRESERVES);
				setState(753);
				assertion();
				}
				break;
			case POST:
				enterOuterAlt(_localctx, 3);
				{
				setState(754);
				((SpecStatementContext)_localctx).kind = match(POST);
				setState(755);
				assertion();
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 4);
				{
				setState(756);
				((SpecStatementContext)_localctx).kind = match(DEC);
				setState(757);
				terminationMeasure();
				}
				break;
			case PRIVATE:
				enterOuterAlt(_localctx, 5);
				{
				setState(758);
				((SpecStatementContext)_localctx).kind = match(PRIVATE);
				setState(759);
				privateSpec();
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
		enterRule(_localctx, 96, RULE_terminationMeasure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(763);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(762);
				expressionList();
				}
				break;
			}
			setState(767);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(765);
				match(IF);
				setState(766);
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
		enterRule(_localctx, 98, RULE_assertion);
		try {
			setState(771);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(770);
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

	public static class PrivateSpecContext extends ParserRuleContext {
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<SpecStatementContext> specStatement() {
			return getRuleContexts(SpecStatementContext.class);
		}
		public SpecStatementContext specStatement(int i) {
			return getRuleContext(SpecStatementContext.class,i);
		}
		public List<EosContext> eos() {
			return getRuleContexts(EosContext.class);
		}
		public EosContext eos(int i) {
			return getRuleContext(EosContext.class,i);
		}
		public PrivateEntailmentProofContext privateEntailmentProof() {
			return getRuleContext(PrivateEntailmentProofContext.class,0);
		}
		public PrivateSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privateSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPrivateSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivateSpecContext privateSpec() throws RecognitionException {
		PrivateSpecContext _localctx = new PrivateSpecContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_privateSpec);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			match(L_CURLY);
			setState(785);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(779);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(774);
						specStatement();
						setState(775);
						eos();
						}
						} 
					}
					setState(781);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
				}
				setState(783);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PROOF) {
					{
					setState(782);
					privateEntailmentProof();
					}
				}

				}
				break;
			}
			setState(787);
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

	public static class PrivateEntailmentProofContext extends ParserRuleContext {
		public TerminalNode PROOF() { return getToken(GobraParser.PROOF, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public EosContext eos() {
			return getRuleContext(EosContext.class,0);
		}
		public PrivateEntailmentProofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privateEntailmentProof; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitPrivateEntailmentProof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivateEntailmentProofContext privateEntailmentProof() throws RecognitionException {
		PrivateEntailmentProofContext _localctx = new PrivateEntailmentProofContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_privateEntailmentProof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(789);
			match(PROOF);
			setState(790);
			block();
			setState(791);
			eos();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchStmtContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(GobraParser.MATCH, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public List<MatchStmtClauseContext> matchStmtClause() {
			return getRuleContexts(MatchStmtClauseContext.class);
		}
		public MatchStmtClauseContext matchStmtClause(int i) {
			return getRuleContext(MatchStmtClauseContext.class,i);
		}
		public MatchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchStmtContext matchStmt() throws RecognitionException {
		MatchStmtContext _localctx = new MatchStmtContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_matchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(793);
			match(MATCH);
			setState(794);
			expression(0);
			setState(795);
			match(L_CURLY);
			setState(799);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(796);
				matchStmtClause();
				}
				}
				setState(801);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(802);
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

	public static class MatchStmtClauseContext extends ParserRuleContext {
		public MatchCaseContext matchCase() {
			return getRuleContext(MatchCaseContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GobraParser.COLON, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public MatchStmtClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStmtClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchStmtClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchStmtClauseContext matchStmtClause() throws RecognitionException {
		MatchStmtClauseContext _localctx = new MatchStmtClauseContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_matchStmtClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(804);
			matchCase();
			setState(805);
			match(COLON);
			setState(807);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(806);
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

	public static class MatchCaseContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(GobraParser.CASE, 0); }
		public MatchPatternContext matchPattern() {
			return getRuleContext(MatchPatternContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(GobraParser.DEFAULT, 0); }
		public MatchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchCase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchCaseContext matchCase() throws RecognitionException {
		MatchCaseContext _localctx = new MatchCaseContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_matchCase);
		try {
			setState(812);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(809);
				match(CASE);
				setState(810);
				matchPattern();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(811);
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

	public static class MatchPatternContext extends ParserRuleContext {
		public MatchPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchPattern; }
	 
		public MatchPatternContext() { }
		public void copyFrom(MatchPatternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MatchPatternValueContext extends MatchPatternContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public MatchPatternValueContext(MatchPatternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MatchPatternCompositeContext extends MatchPatternContext {
		public LiteralTypeContext literalType() {
			return getRuleContext(LiteralTypeContext.class,0);
		}
		public TerminalNode L_CURLY() { return getToken(GobraParser.L_CURLY, 0); }
		public TerminalNode R_CURLY() { return getToken(GobraParser.R_CURLY, 0); }
		public MatchPatternListContext matchPatternList() {
			return getRuleContext(MatchPatternListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GobraParser.COMMA, 0); }
		public MatchPatternCompositeContext(MatchPatternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternComposite(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MatchPatternBindContext extends MatchPatternContext {
		public TerminalNode QMARK() { return getToken(GobraParser.QMARK, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
		public MatchPatternBindContext(MatchPatternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternBind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchPatternContext matchPattern() throws RecognitionException {
		MatchPatternContext _localctx = new MatchPatternContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_matchPattern);
		int _la;
		try {
			setState(827);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				_localctx = new MatchPatternBindContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(814);
				match(QMARK);
				setState(815);
				match(IDENTIFIER);
				}
				break;
			case 2:
				_localctx = new MatchPatternCompositeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(816);
				literalType();
				setState(817);
				match(L_CURLY);
				setState(822);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << QMARK) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(818);
					matchPatternList();
					setState(820);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(819);
						match(COMMA);
						}
					}

					}
				}

				setState(824);
				match(R_CURLY);
				}
				break;
			case 3:
				_localctx = new MatchPatternValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(826);
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

	public static class MatchPatternListContext extends ParserRuleContext {
		public List<MatchPatternContext> matchPattern() {
			return getRuleContexts(MatchPatternContext.class);
		}
		public MatchPatternContext matchPattern(int i) {
			return getRuleContext(MatchPatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GobraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GobraParser.COMMA, i);
		}
		public MatchPatternListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchPatternList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitMatchPatternList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchPatternListContext matchPatternList() throws RecognitionException {
		MatchPatternListContext _localctx = new MatchPatternListContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_matchPatternList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(829);
			matchPattern();
			setState(834);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(830);
					match(COMMA);
					setState(831);
					matchPattern();
					}
					} 
				}
				setState(836);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 114, RULE_blockWithBodyParameterInfo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(837);
			match(L_CURLY);
			setState(842);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(838);
				match(SHARE);
				setState(839);
				identifierList();
				setState(840);
				eos();
				}
				break;
			}
			setState(845);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(844);
				statementList();
				}
				break;
			}
			setState(847);
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
		public QualifiedIdentContext qualifiedIdent() {
			return getRuleContext(QualifiedIdentContext.class,0);
		}
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
		enterRule(_localctx, 116, RULE_closureSpecInstance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(849);
				qualifiedIdent();
				}
				break;
			case 2:
				{
				setState(850);
				match(IDENTIFIER);
				}
				break;
			}
			setState(861);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(853);
				match(L_CURLY);
				setState(858);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(854);
					closureSpecParams();
					setState(856);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(855);
						match(COMMA);
						}
					}

					}
				}

				setState(860);
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
		enterRule(_localctx, 118, RULE_closureSpecParams);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(863);
			closureSpecParam();
			setState(868);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(864);
					match(COMMA);
					setState(865);
					closureSpecParam();
					}
					} 
				}
				setState(870);
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
		enterRule(_localctx, 120, RULE_closureSpecParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(873);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(871);
				match(IDENTIFIER);
				setState(872);
				match(COLON);
				}
				break;
			}
			setState(875);
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

	public static class ClosureImplProofStmtContext extends ParserRuleContext {
		public TerminalNode PROOF() { return getToken(GobraParser.PROOF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IMPL() { return getToken(GobraParser.IMPL, 0); }
		public ClosureSpecInstanceContext closureSpecInstance() {
			return getRuleContext(ClosureSpecInstanceContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ClosureImplProofStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closureImplProofStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitClosureImplProofStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosureImplProofStmtContext closureImplProofStmt() throws RecognitionException {
		ClosureImplProofStmtContext _localctx = new ClosureImplProofStmtContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_closureImplProofStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
			match(PROOF);
			setState(878);
			expression(0);
			setState(879);
			match(IMPL);
			setState(880);
			closureSpecInstance();
			setState(881);
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
		enterRule(_localctx, 124, RULE_implementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(883);
			type_();
			setState(884);
			match(IMPL);
			setState(885);
			type_();
			setState(904);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(886);
				match(L_CURLY);
				setState(892);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRED) {
					{
					{
					setState(887);
					implementationProofPredicateAlias();
					setState(888);
					eos();
					}
					}
					setState(894);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(900);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PURE || _la==L_PAREN) {
					{
					{
					setState(895);
					methodImplementationProof();
					setState(896);
					eos();
					}
					}
					setState(902);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(903);
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
		enterRule(_localctx, 126, RULE_methodImplementationProof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(907);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PURE) {
				{
				setState(906);
				match(PURE);
				}
			}

			setState(909);
			nonLocalReceiver();
			setState(910);
			match(IDENTIFIER);
			setState(911);
			signature();
			setState(913);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(912);
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
		enterRule(_localctx, 128, RULE_nonLocalReceiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			match(L_PAREN);
			setState(917);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(916);
				match(IDENTIFIER);
				}
				break;
			}
			setState(920);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(919);
				match(STAR);
				}
			}

			setState(922);
			typeName();
			setState(923);
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
		enterRule(_localctx, 130, RULE_selection);
		try {
			setState(930);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(925);
				primaryExpr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(926);
				type_();
				setState(927);
				match(DOT);
				setState(928);
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
		enterRule(_localctx, 132, RULE_implementationProofPredicateAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(932);
			match(PRED);
			setState(933);
			match(IDENTIFIER);
			setState(934);
			match(DECLARE_ASSIGN);
			setState(937);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(935);
				selection();
				}
				break;
			case 2:
				{
				setState(936);
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
		enterRule(_localctx, 134, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(939);
			match(MAKE);
			setState(940);
			match(L_PAREN);
			setState(941);
			type_();
			setState(944);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(942);
				match(COMMA);
				setState(943);
				expressionList();
				}
			}

			setState(946);
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
		enterRule(_localctx, 136, RULE_new_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(948);
			match(NEW);
			setState(949);
			match(L_PAREN);
			setState(950);
			type_();
			setState(951);
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
		public ConstructDeclContext constructDecl() {
			return getRuleContext(ConstructDeclContext.class,0);
		}
		public DerefDeclContext derefDecl() {
			return getRuleContext(DerefDeclContext.class,0);
		}
		public AssignDeclContext assignDecl() {
			return getRuleContext(AssignDeclContext.class,0);
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
		enterRule(_localctx, 138, RULE_specMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(953);
			((SpecMemberContext)_localctx).specification = specification();
			setState(959);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				{
				setState(954);
				functionDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 2:
				{
				setState(955);
				methodDecl(((SpecMemberContext)_localctx).specification.trusted, ((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 3:
				{
				setState(956);
				constructDecl();
				}
				break;
			case 4:
				{
				setState(957);
				derefDecl(((SpecMemberContext)_localctx).specification.pure);
				}
				break;
			case 5:
				{
				setState(958);
				assignDecl();
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
		enterRule(_localctx, 140, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(961);
			match(FUNC);
			setState(962);
			match(IDENTIFIER);
			{
			setState(963);
			signature();
			setState(965);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				{
				setState(964);
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
		enterRule(_localctx, 142, RULE_methodDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(967);
			match(FUNC);
			setState(968);
			receiver();
			setState(969);
			match(IDENTIFIER);
			{
			setState(970);
			signature();
			setState(972);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(971);
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
		enterRule(_localctx, 144, RULE_explicitGhostMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(974);
			match(GHOST);
			setState(977);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRE:
			case PRESERVES:
			case POST:
			case DEC:
			case PURE:
			case PRIVATE:
			case TRUSTED:
			case CONSTRUCT:
			case DEREF:
			case ASSIGN_STRUCT:
			case FUNC:
				{
				setState(975);
				specMember();
				}
				break;
			case CONST:
			case TYPE:
			case VAR:
				{
				setState(976);
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
		enterRule(_localctx, 146, RULE_fpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(979);
			match(PRED);
			setState(980);
			match(IDENTIFIER);
			setState(981);
			parameters();
			setState(983);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(982);
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
		enterRule(_localctx, 148, RULE_predicateBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(985);
			match(L_CURLY);
			setState(986);
			expression(0);
			setState(987);
			eos();
			setState(988);
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
		enterRule(_localctx, 150, RULE_mpredicateDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(990);
			match(PRED);
			setState(991);
			receiver();
			setState(992);
			match(IDENTIFIER);
			setState(993);
			parameters();
			setState(995);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(994);
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

	public static class ConstructDeclContext extends ParserRuleContext {
		public TerminalNode CONSTRUCT() { return getToken(GobraParser.CONSTRUCT, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
		}
		public ConstructDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitConstructDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructDeclContext constructDecl() throws RecognitionException {
		ConstructDeclContext _localctx = new ConstructDeclContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_constructDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(997);
			match(CONSTRUCT);
			setState(999);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AMPERSAND) {
				{
				setState(998);
				match(AMPERSAND);
				}
			}

			setState(1001);
			type_();
			{
			setState(1002);
			signature();
			setState(1004);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(1003);
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

	public static class DerefDeclContext extends ParserRuleContext {
		public boolean pure;
		public TerminalNode DEREF() { return getToken(GobraParser.DEREF, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
		}
		public DerefDeclContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public DerefDeclContext(ParserRuleContext parent, int invokingState, boolean pure) {
			super(parent, invokingState);
			this.pure = pure;
		}
		@Override public int getRuleIndex() { return RULE_derefDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitDerefDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DerefDeclContext derefDecl(boolean pure) throws RecognitionException {
		DerefDeclContext _localctx = new DerefDeclContext(_ctx, getState(), pure);
		enterRule(_localctx, 154, RULE_derefDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1006);
			match(DEREF);
			setState(1008);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AMPERSAND) {
				{
				setState(1007);
				match(AMPERSAND);
				}
			}

			setState(1010);
			type_();
			{
			setState(1011);
			signature();
			setState(1013);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(1012);
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

	public static class AssignDeclContext extends ParserRuleContext {
		public TerminalNode ASSIGN_STRUCT() { return getToken(GobraParser.ASSIGN_STRUCT, 0); }
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public TerminalNode AMPERSAND() { return getToken(GobraParser.AMPERSAND, 0); }
		public BlockWithBodyParameterInfoContext blockWithBodyParameterInfo() {
			return getRuleContext(BlockWithBodyParameterInfoContext.class,0);
		}
		public AssignDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GobraParserVisitor ) return ((GobraParserVisitor<? extends T>)visitor).visitAssignDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignDeclContext assignDecl() throws RecognitionException {
		AssignDeclContext _localctx = new AssignDeclContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_assignDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1015);
			match(ASSIGN_STRUCT);
			setState(1017);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AMPERSAND) {
				{
				setState(1016);
				match(AMPERSAND);
				}
			}

			setState(1019);
			type_();
			{
			setState(1020);
			signature();
			setState(1022);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				{
				setState(1021);
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
		enterRule(_localctx, 158, RULE_varSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1024);
			maybeAddressableIdentifierList();
			setState(1032);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
			case ADT:
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
				setState(1025);
				type_();
				setState(1028);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
				case 1:
					{
					setState(1026);
					match(ASSIGN);
					setState(1027);
					expressionList();
					}
					break;
				}
				}
				break;
			case ASSIGN:
				{
				setState(1030);
				match(ASSIGN);
				setState(1031);
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
		enterRule(_localctx, 160, RULE_shortVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1034);
			maybeAddressableIdentifierList();
			setState(1035);
			match(DECLARE_ASSIGN);
			setState(1036);
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
		enterRule(_localctx, 162, RULE_receiver);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1038);
			match(L_PAREN);
			setState(1040);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				{
				setState(1039);
				maybeAddressableIdentifier();
				}
				break;
			}
			setState(1042);
			type_();
			setState(1044);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1043);
				match(COMMA);
				}
			}

			setState(1046);
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
		enterRule(_localctx, 164, RULE_parameterDecl);
		try {
			setState(1050);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1048);
				actualParameterDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1049);
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
		enterRule(_localctx, 166, RULE_actualParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1053);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(1052);
				identifierList();
				}
				break;
			}
			setState(1055);
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
		enterRule(_localctx, 168, RULE_ghostParameterDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
			match(GHOST);
			setState(1059);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(1058);
				identifierList();
				}
				break;
			}
			setState(1061);
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
		enterRule(_localctx, 170, RULE_parameterType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1064);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELLIPSIS) {
				{
				setState(1063);
				match(ELLIPSIS);
				}
			}

			setState(1066);
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
		public TerminalNode GHOST_EQUALS() { return getToken(GobraParser.GHOST_EQUALS, 0); }
		public TerminalNode GHOST_NOT_EQUALS() { return getToken(GobraParser.GHOST_NOT_EQUALS, 0); }
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
		int _startState = 172;
		enterRecursionRule(_localctx, 172, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1084);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1069);
				((UnaryExprContext)_localctx).unary_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)))) != 0)) ) {
					((UnaryExprContext)_localctx).unary_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1070);
				expression(14);
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1071);
				primaryExpr(0);
				}
				break;
			case 3:
				{
				_localctx = new UnfoldingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1072);
				match(UNFOLDING);
				setState(1073);
				predicateAccess();
				setState(1074);
				match(IN);
				setState(1075);
				expression(2);
				}
				break;
			case 4:
				{
				_localctx = new QuantificationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1077);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1078);
				boundVariables();
				setState(1079);
				match(COLON);
				setState(1080);
				match(COLON);
				setState(1081);
				triggers();
				setState(1082);
				expression(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1121);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1119);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
					case 1:
						{
						_localctx = new MulExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1086);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1087);
						((MulExprContext)_localctx).mul_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (DIV - 129)) | (1L << (MOD - 129)) | (1L << (LSHIFT - 129)) | (1L << (RSHIFT - 129)) | (1L << (BIT_CLEAR - 129)) | (1L << (STAR - 129)) | (1L << (AMPERSAND - 129)))) != 0)) ) {
							((MulExprContext)_localctx).mul_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1088);
						expression(13);
						}
						break;
					case 2:
						{
						_localctx = new AddExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1089);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1090);
						((AddExprContext)_localctx).add_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==WAND || ((((_la - 116)) & ~0x3f) == 0 && ((1L << (_la - 116)) & ((1L << (PLUS_PLUS - 116)) | (1L << (OR - 116)) | (1L << (PLUS - 116)) | (1L << (MINUS - 116)) | (1L << (CARET - 116)))) != 0)) ) {
							((AddExprContext)_localctx).add_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1091);
						expression(12);
						}
						break;
					case 3:
						{
						_localctx = new P42ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1092);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1093);
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
						setState(1094);
						expression(11);
						}
						break;
					case 4:
						{
						_localctx = new P41ExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1095);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1096);
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
						setState(1097);
						expression(10);
						}
						break;
					case 5:
						{
						_localctx = new RelExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1098);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1099);
						((RelExprContext)_localctx).rel_op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (GHOST_EQUALS - 72)) | (1L << (GHOST_NOT_EQUALS - 72)) | (1L << (EQUALS - 72)) | (1L << (NOT_EQUALS - 72)) | (1L << (LESS - 72)) | (1L << (LESS_OR_EQUALS - 72)) | (1L << (GREATER - 72)) | (1L << (GREATER_OR_EQUALS - 72)))) != 0)) ) {
							((RelExprContext)_localctx).rel_op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1100);
						expression(9);
						}
						break;
					case 6:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1101);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1102);
						match(LOGICAL_AND);
						setState(1103);
						expression(7);
						}
						break;
					case 7:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1104);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1105);
						match(LOGICAL_OR);
						setState(1106);
						expression(6);
						}
						break;
					case 8:
						{
						_localctx = new ImplicationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1107);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1108);
						match(IMPLIES);
						setState(1109);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1110);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1111);
						match(QMARK);
						setState(1112);
						expression(0);
						setState(1113);
						match(COLON);
						setState(1114);
						expression(3);
						}
						break;
					case 10:
						{
						_localctx = new ClosureImplSpecExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1116);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1117);
						match(IMPL);
						setState(1118);
						closureSpecInstance();
						}
						break;
					}
					} 
				}
				setState(1123);
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
		public ClosureImplProofStmtContext closureImplProofStmt() {
			return getRuleContext(ClosureImplProofStmtContext.class,0);
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
		enterRule(_localctx, 174, RULE_statement);
		try {
			setState(1144);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1124);
				ghostStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1125);
				auxiliaryStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1126);
				packageStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1127);
				applyStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1128);
				declaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1129);
				labeledStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1130);
				simpleStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1131);
				goStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1132);
				returnStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1133);
				breakStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1134);
				continueStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1135);
				gotoStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1136);
				fallthroughStmt();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1137);
				block();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1138);
				ifStmt();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(1139);
				switchStmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(1140);
				selectStmt();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(1141);
				specForStmt();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(1142);
				deferStmt();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(1143);
				closureImplProofStmt();
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
		enterRule(_localctx, 176, RULE_applyStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1146);
			match(APPLY);
			setState(1147);
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
		enterRule(_localctx, 178, RULE_packageStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			match(PACKAGE);
			setState(1150);
			expression(0);
			setState(1152);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(1151);
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
		enterRule(_localctx, 180, RULE_specForStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1154);
			loopSpec();
			setState(1155);
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
		enterRule(_localctx, 182, RULE_loopSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INV) {
				{
				{
				setState(1157);
				match(INV);
				setState(1158);
				expression(0);
				setState(1159);
				eos();
				}
				}
				setState(1165);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEC) {
				{
				setState(1166);
				match(DEC);
				setState(1167);
				terminationMeasure();
				setState(1168);
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
		enterRule(_localctx, 184, RULE_deferStmt);
		int _la;
		try {
			setState(1177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1172);
				match(DEFER);
				setState(1173);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1174);
				match(DEFER);
				setState(1175);
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
				setState(1176);
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
		enterRule(_localctx, 186, RULE_basicLit);
		try {
			setState(1187);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1179);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1180);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1181);
				match(NIL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1182);
				integer();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1183);
				string_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1184);
				match(FLOAT_LIT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1185);
				match(IMAGINARY_LIT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1186);
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
		int _startState = 188;
		enterRecursionRule(_localctx, 188, RULE_primaryExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1201);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				_localctx = new OperandPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1190);
				operand();
				}
				break;
			case 2:
				{
				_localctx = new ConversionPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1191);
				conversion();
				}
				break;
			case 3:
				{
				_localctx = new MethodPrimaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1192);
				methodExpr();
				}
				break;
			case 4:
				{
				_localctx = new GhostPrimaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1193);
				ghostPrimaryExpr();
				}
				break;
			case 5:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1194);
				new_();
				}
				break;
			case 6:
				{
				_localctx = new MakeExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1195);
				make();
				}
				break;
			case 7:
				{
				_localctx = new BuiltInCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1196);
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
				setState(1197);
				match(L_PAREN);
				setState(1198);
				expression(0);
				setState(1199);
				match(R_PAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1225);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,97,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1223);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1203);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1204);
						match(DOT);
						setState(1205);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new IndexPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1206);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1207);
						index();
						}
						break;
					case 3:
						{
						_localctx = new SlicePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1208);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1209);
						slice_();
						}
						break;
					case 4:
						{
						_localctx = new SeqUpdPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1210);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1211);
						seqUpdExp();
						}
						break;
					case 5:
						{
						_localctx = new TypeAssertionPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1212);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1213);
						typeAssertion();
						}
						break;
					case 6:
						{
						_localctx = new InvokePrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1214);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1215);
						arguments();
						}
						break;
					case 7:
						{
						_localctx = new InvokePrimaryExprWithSpecContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1216);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1217);
						arguments();
						setState(1218);
						match(AS);
						setState(1219);
						closureSpecInstance();
						}
						break;
					case 8:
						{
						_localctx = new PredConstrPrimaryExprContext(new PrimaryExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpr);
						setState(1221);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(1222);
						predConstructArgs();
						}
						break;
					}
					} 
				}
				setState(1227);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,97,_ctx);
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
		enterRule(_localctx, 190, RULE_functionLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1228);
			((FunctionLitContext)_localctx).specification = specification();
			setState(1229);
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
		enterRule(_localctx, 192, RULE_closureDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			match(FUNC);
			setState(1233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1232);
				match(IDENTIFIER);
				}
			}

			{
			setState(1235);
			signature();
			setState(1237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(1236);
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
		enterRule(_localctx, 194, RULE_predConstructArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1239);
			match(L_PRED);
			setState(1241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
				{
				setState(1240);
				expressionList();
				}
			}

			setState(1244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1243);
				match(COMMA);
				}
			}

			setState(1246);
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
		enterRule(_localctx, 196, RULE_interfaceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1248);
			match(INTERFACE);
			setState(1249);
			match(L_CURLY);
			setState(1259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << GHOST) | (1L << PRED))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (TRUSTED - 64)) | (1L << (IDENTIFIER - 64)))) != 0)) {
				{
				{
				setState(1253);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
				case 1:
					{
					setState(1250);
					methodSpec();
					}
					break;
				case 2:
					{
					setState(1251);
					typeName();
					}
					break;
				case 3:
					{
					setState(1252);
					predicateSpec();
					}
					break;
				}
				setState(1255);
				eos();
				}
				}
				setState(1261);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1262);
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
		enterRule(_localctx, 198, RULE_predicateSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1264);
			match(PRED);
			setState(1265);
			match(IDENTIFIER);
			setState(1266);
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
		enterRule(_localctx, 200, RULE_methodSpec);
		int _la;
		try {
			setState(1283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1269);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1268);
					match(GHOST);
					}
				}

				setState(1271);
				specification();
				setState(1272);
				match(IDENTIFIER);
				setState(1273);
				parameters();
				setState(1274);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1277);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GHOST) {
					{
					setState(1276);
					match(GHOST);
					}
				}

				setState(1279);
				specification();
				setState(1280);
				match(IDENTIFIER);
				setState(1281);
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
		enterRule(_localctx, 202, RULE_type_);
		try {
			setState(1292);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1285);
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
				setState(1286);
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
			case ADT:
				enterOuterAlt(_localctx, 3);
				{
				setState(1287);
				ghostTypeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(1288);
				match(L_PAREN);
				setState(1289);
				type_();
				setState(1290);
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
		enterRule(_localctx, 204, RULE_typeLit);
		try {
			setState(1303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1294);
				arrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1295);
				structType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1296);
				pointerType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1297);
				functionType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1298);
				interfaceType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1299);
				sliceType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1300);
				mapType();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1301);
				channelType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1302);
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
		enterRule(_localctx, 206, RULE_predType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1305);
			match(PRED);
			setState(1306);
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
		enterRule(_localctx, 208, RULE_predTypeParams);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1308);
			match(L_PAREN);
			setState(1320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (FUNC - 80)) | (1L << (INTERFACE - 80)) | (1L << (MAP - 80)) | (1L << (STRUCT - 80)) | (1L << (CHAN - 80)) | (1L << (IDENTIFIER - 80)) | (1L << (L_PAREN - 80)) | (1L << (L_BRACKET - 80)) | (1L << (STAR - 80)) | (1L << (RECEIVE - 80)))) != 0)) {
				{
				setState(1309);
				type_();
				setState(1314);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,109,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1310);
						match(COMMA);
						setState(1311);
						type_();
						}
						} 
					}
					setState(1316);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,109,_ctx);
				}
				setState(1318);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1317);
					match(COMMA);
					}
				}

				}
			}

			setState(1322);
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
		enterRule(_localctx, 210, RULE_literalType);
		try {
			setState(1331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1324);
				structType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1325);
				arrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1326);
				implicitArray();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1327);
				sliceType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1328);
				mapType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1329);
				ghostTypeLit();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1330);
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
		enterRule(_localctx, 212, RULE_implicitArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1333);
			match(L_BRACKET);
			setState(1334);
			match(ELLIPSIS);
			setState(1335);
			match(R_BRACKET);
			setState(1336);
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
		enterRule(_localctx, 214, RULE_slice_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1338);
			match(L_BRACKET);
			setState(1354);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1340);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(1339);
					low();
					}
				}

				setState(1342);
				match(COLON);
				setState(1344);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(1343);
					high();
					}
				}

				}
				break;
			case 2:
				{
				setState(1347);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(1346);
					low();
					}
				}

				setState(1349);
				match(COLON);
				setState(1350);
				high();
				setState(1351);
				match(COLON);
				setState(1352);
				cap();
				}
				break;
			}
			setState(1356);
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
		enterRule(_localctx, 216, RULE_low);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1358);
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
		enterRule(_localctx, 218, RULE_high);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1360);
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
		enterRule(_localctx, 220, RULE_cap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1362);
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
		enterRule(_localctx, 222, RULE_assign_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OR - 128)) | (1L << (DIV - 128)) | (1L << (MOD - 128)) | (1L << (LSHIFT - 128)) | (1L << (RSHIFT - 128)) | (1L << (BIT_CLEAR - 128)) | (1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)))) != 0)) {
				{
				setState(1364);
				((Assign_opContext)_localctx).ass_op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (OR - 128)) | (1L << (DIV - 128)) | (1L << (MOD - 128)) | (1L << (LSHIFT - 128)) | (1L << (RSHIFT - 128)) | (1L << (BIT_CLEAR - 128)) | (1L << (PLUS - 128)) | (1L << (MINUS - 128)) | (1L << (CARET - 128)) | (1L << (STAR - 128)) | (1L << (AMPERSAND - 128)))) != 0)) ) {
					((Assign_opContext)_localctx).ass_op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1367);
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

	public static class RangeClauseContext extends ParserRuleContext {
		public TerminalNode RANGE() { return getToken(GobraParser.RANGE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(GobraParser.ASSIGN, 0); }
		public MaybeAddressableIdentifierListContext maybeAddressableIdentifierList() {
			return getRuleContext(MaybeAddressableIdentifierListContext.class,0);
		}
		public TerminalNode DECLARE_ASSIGN() { return getToken(GobraParser.DECLARE_ASSIGN, 0); }
		public TerminalNode WITH() { return getToken(GobraParser.WITH, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GobraParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 224, RULE_rangeClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1375);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1369);
				expressionList();
				setState(1370);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1372);
				maybeAddressableIdentifierList();
				setState(1373);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1377);
			match(RANGE);
			setState(1378);
			expression(0);
			setState(1383);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(1379);
				match(WITH);
				setState(1381);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1380);
					match(IDENTIFIER);
					}
				}

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
		enterRule(_localctx, 226, RULE_packageClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1385);
			match(PACKAGE);
			setState(1386);
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
		enterRule(_localctx, 228, RULE_importPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1388);
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
		enterRule(_localctx, 230, RULE_declaration);
		try {
			setState(1393);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(1390);
				constDecl();
				}
				break;
			case TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1391);
				typeDecl();
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(1392);
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
		enterRule(_localctx, 232, RULE_constDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1395);
			match(CONST);
			setState(1407);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1396);
				constSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1397);
				match(L_PAREN);
				setState(1403);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1398);
					constSpec();
					setState(1399);
					eos();
					}
					}
					setState(1405);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1406);
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
		enterRule(_localctx, 234, RULE_constSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1409);
			identifierList();
			setState(1415);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				{
				setState(1411);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (FUNC - 80)) | (1L << (INTERFACE - 80)) | (1L << (MAP - 80)) | (1L << (STRUCT - 80)) | (1L << (CHAN - 80)) | (1L << (IDENTIFIER - 80)) | (1L << (L_PAREN - 80)) | (1L << (L_BRACKET - 80)) | (1L << (STAR - 80)) | (1L << (RECEIVE - 80)))) != 0)) {
					{
					setState(1410);
					type_();
					}
				}

				setState(1413);
				match(ASSIGN);
				setState(1414);
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
		enterRule(_localctx, 236, RULE_identifierList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1417);
			match(IDENTIFIER);
			setState(1422);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,126,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1418);
					match(COMMA);
					setState(1419);
					match(IDENTIFIER);
					}
					} 
				}
				setState(1424);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,126,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 238, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1425);
			expression(0);
			setState(1430);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,127,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1426);
					match(COMMA);
					setState(1427);
					expression(0);
					}
					} 
				}
				setState(1432);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,127,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 240, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1433);
			match(TYPE);
			setState(1445);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1434);
				typeSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1435);
				match(L_PAREN);
				setState(1441);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1436);
					typeSpec();
					setState(1437);
					eos();
					}
					}
					setState(1443);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1444);
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
		enterRule(_localctx, 242, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1447);
			match(IDENTIFIER);
			setState(1449);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(1448);
				match(ASSIGN);
				}
			}

			setState(1451);
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
		enterRule(_localctx, 244, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1453);
			match(VAR);
			setState(1465);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(1454);
				varSpec();
				}
				break;
			case L_PAREN:
				{
				setState(1455);
				match(L_PAREN);
				setState(1461);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==IDENTIFIER) {
					{
					{
					setState(1456);
					varSpec();
					setState(1457);
					eos();
					}
					}
					setState(1463);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1464);
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
		enterRule(_localctx, 246, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1467);
			match(L_CURLY);
			setState(1469);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1468);
				statementList();
				}
				break;
			}
			setState(1471);
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
		enterRule(_localctx, 248, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1479); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1474);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
					case 1:
						{
						setState(1473);
						eos();
						}
						break;
					}
					setState(1476);
					statement();
					setState(1477);
					eos();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1481); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,135,_ctx);
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
		enterRule(_localctx, 250, RULE_simpleStmt);
		try {
			setState(1488);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1483);
				sendStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1484);
				incDecStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1485);
				assignment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1486);
				expressionStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1487);
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
		enterRule(_localctx, 252, RULE_expressionStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1490);
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
		enterRule(_localctx, 254, RULE_sendStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1492);
			((SendStmtContext)_localctx).channel = expression(0);
			setState(1493);
			match(RECEIVE);
			setState(1494);
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
		enterRule(_localctx, 256, RULE_incDecStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1496);
			expression(0);
			setState(1497);
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
		enterRule(_localctx, 258, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1499);
			expressionList();
			setState(1500);
			assign_op();
			setState(1501);
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
		enterRule(_localctx, 260, RULE_emptyStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1503);
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
		enterRule(_localctx, 262, RULE_labeledStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1505);
			match(IDENTIFIER);
			setState(1506);
			match(COLON);
			setState(1508);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				{
				setState(1507);
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
		enterRule(_localctx, 264, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1510);
			match(RETURN);
			setState(1512);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				{
				setState(1511);
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
		enterRule(_localctx, 266, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1514);
			match(BREAK);
			setState(1516);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
			case 1:
				{
				setState(1515);
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
		enterRule(_localctx, 268, RULE_continueStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1518);
			match(CONTINUE);
			setState(1520);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				{
				setState(1519);
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
		enterRule(_localctx, 270, RULE_gotoStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1522);
			match(GOTO);
			setState(1523);
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
		enterRule(_localctx, 272, RULE_fallthroughStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1525);
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
		enterRule(_localctx, 274, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1527);
			match(IF);
			setState(1536);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				{
				setState(1528);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1529);
				eos();
				setState(1530);
				expression(0);
				}
				break;
			case 3:
				{
				setState(1532);
				simpleStmt();
				setState(1533);
				eos();
				setState(1534);
				expression(0);
				}
				break;
			}
			setState(1538);
			block();
			setState(1544);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
			case 1:
				{
				setState(1539);
				match(ELSE);
				setState(1542);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IF:
					{
					setState(1540);
					ifStmt();
					}
					break;
				case L_CURLY:
					{
					setState(1541);
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
		enterRule(_localctx, 276, RULE_switchStmt);
		try {
			setState(1548);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1546);
				exprSwitchStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1547);
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
		enterRule(_localctx, 278, RULE_exprSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1550);
			match(SWITCH);
			setState(1561);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				{
				setState(1552);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(1551);
					expression(0);
					}
				}

				}
				break;
			case 2:
				{
				setState(1555);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
				case 1:
					{
					setState(1554);
					simpleStmt();
					}
					break;
				}
				setState(1557);
				eos();
				setState(1559);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
					{
					setState(1558);
					expression(0);
					}
				}

				}
				break;
			}
			setState(1563);
			match(L_CURLY);
			setState(1567);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1564);
				exprCaseClause();
				}
				}
				setState(1569);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1570);
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
		enterRule(_localctx, 280, RULE_exprCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1572);
			exprSwitchCase();
			setState(1573);
			match(COLON);
			setState(1575);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(1574);
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
		enterRule(_localctx, 282, RULE_exprSwitchCase);
		try {
			setState(1580);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1577);
				match(CASE);
				setState(1578);
				expressionList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1579);
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
		enterRule(_localctx, 284, RULE_typeSwitchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1582);
			match(SWITCH);
			setState(1591);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1583);
				typeSwitchGuard();
				}
				break;
			case 2:
				{
				setState(1584);
				eos();
				setState(1585);
				typeSwitchGuard();
				}
				break;
			case 3:
				{
				setState(1587);
				simpleStmt();
				setState(1588);
				eos();
				setState(1589);
				typeSwitchGuard();
				}
				break;
			}
			setState(1593);
			match(L_CURLY);
			setState(1597);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1594);
				typeCaseClause();
				}
				}
				setState(1599);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1600);
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
		enterRule(_localctx, 286, RULE_typeSwitchGuard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1604);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1602);
				match(IDENTIFIER);
				setState(1603);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1606);
			primaryExpr(0);
			setState(1607);
			match(DOT);
			setState(1608);
			match(L_PAREN);
			setState(1609);
			match(TYPE);
			setState(1610);
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
		enterRule(_localctx, 288, RULE_typeCaseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1612);
			typeSwitchCase();
			setState(1613);
			match(COLON);
			setState(1615);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				{
				setState(1614);
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
		enterRule(_localctx, 290, RULE_typeSwitchCase);
		try {
			setState(1620);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1617);
				match(CASE);
				setState(1618);
				typeList();
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1619);
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
		enterRule(_localctx, 292, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1624);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GHOST:
			case SEQ:
			case SET:
			case MSET:
			case DICT:
			case OPT:
			case DOM:
			case ADT:
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
				setState(1622);
				type_();
				}
				break;
			case NIL_LIT:
				{
				setState(1623);
				match(NIL_LIT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1633);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1626);
				match(COMMA);
				setState(1629);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GHOST:
				case SEQ:
				case SET:
				case MSET:
				case DICT:
				case OPT:
				case DOM:
				case ADT:
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
					setState(1627);
					type_();
					}
					break;
				case NIL_LIT:
					{
					setState(1628);
					match(NIL_LIT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1635);
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
		enterRule(_localctx, 294, RULE_selectStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1636);
			match(SELECT);
			setState(1637);
			match(L_CURLY);
			setState(1641);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DEFAULT || _la==CASE) {
				{
				{
				setState(1638);
				commClause();
				}
				}
				setState(1643);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1644);
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
		enterRule(_localctx, 296, RULE_commClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1646);
			commCase();
			setState(1647);
			match(COLON);
			setState(1649);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,161,_ctx) ) {
			case 1:
				{
				setState(1648);
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
		enterRule(_localctx, 298, RULE_commCase);
		try {
			setState(1657);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1651);
				match(CASE);
				setState(1654);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
				case 1:
					{
					setState(1652);
					sendStmt();
					}
					break;
				case 2:
					{
					setState(1653);
					recvStmt();
					}
					break;
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1656);
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
		enterRule(_localctx, 300, RULE_recvStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1665);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,164,_ctx) ) {
			case 1:
				{
				setState(1659);
				expressionList();
				setState(1660);
				match(ASSIGN);
				}
				break;
			case 2:
				{
				setState(1662);
				identifierList();
				setState(1663);
				match(DECLARE_ASSIGN);
				}
				break;
			}
			setState(1667);
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
		enterRule(_localctx, 302, RULE_forStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1669);
			match(FOR);
			setState(1673);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
			case 1:
				{
				setState(1670);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1671);
				forClause();
				}
				break;
			case 3:
				{
				setState(1672);
				rangeClause();
				}
				break;
			}
			setState(1675);
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
		enterRule(_localctx, 304, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1678);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				{
				setState(1677);
				((ForClauseContext)_localctx).initStmt = simpleStmt();
				}
				break;
			}
			setState(1680);
			eos();
			setState(1682);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
			case 1:
				{
				setState(1681);
				expression(0);
				}
				break;
			}
			setState(1684);
			eos();
			setState(1686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
				{
				setState(1685);
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
		enterRule(_localctx, 306, RULE_goStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1688);
			match(GO);
			setState(1689);
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
		enterRule(_localctx, 308, RULE_typeName);
		try {
			setState(1693);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1691);
				qualifiedIdent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1692);
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
		enterRule(_localctx, 310, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1695);
			match(L_BRACKET);
			setState(1696);
			arrayLength();
			setState(1697);
			match(R_BRACKET);
			setState(1698);
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
		enterRule(_localctx, 312, RULE_arrayLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1700);
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
		enterRule(_localctx, 314, RULE_elementType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1702);
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
		enterRule(_localctx, 316, RULE_pointerType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1704);
			match(STAR);
			setState(1705);
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
		enterRule(_localctx, 318, RULE_sliceType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1707);
			match(L_BRACKET);
			setState(1708);
			match(R_BRACKET);
			setState(1709);
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
		enterRule(_localctx, 320, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1711);
			match(MAP);
			setState(1712);
			match(L_BRACKET);
			setState(1713);
			type_();
			setState(1714);
			match(R_BRACKET);
			setState(1715);
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
		enterRule(_localctx, 322, RULE_channelType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1722);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
			case 1:
				{
				setState(1717);
				match(CHAN);
				}
				break;
			case 2:
				{
				setState(1718);
				match(CHAN);
				setState(1719);
				match(RECEIVE);
				}
				break;
			case 3:
				{
				setState(1720);
				match(RECEIVE);
				setState(1721);
				match(CHAN);
				}
				break;
			}
			setState(1724);
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
		enterRule(_localctx, 324, RULE_functionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1726);
			match(FUNC);
			setState(1727);
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
		enterRule(_localctx, 326, RULE_signature);
		try {
			setState(1733);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,171,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1729);
				parameters();
				setState(1730);
				result();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1732);
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
		enterRule(_localctx, 328, RULE_result);
		try {
			setState(1737);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,172,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1735);
				parameters();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1736);
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
		enterRule(_localctx, 330, RULE_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1739);
			match(L_PAREN);
			setState(1751);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << DOM) | (1L << ADT) | (1L << PRED))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (FUNC - 80)) | (1L << (INTERFACE - 80)) | (1L << (MAP - 80)) | (1L << (STRUCT - 80)) | (1L << (CHAN - 80)) | (1L << (IDENTIFIER - 80)) | (1L << (L_PAREN - 80)) | (1L << (L_BRACKET - 80)) | (1L << (ELLIPSIS - 80)) | (1L << (STAR - 80)) | (1L << (RECEIVE - 80)))) != 0)) {
				{
				setState(1740);
				parameterDecl();
				setState(1745);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,173,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1741);
						match(COMMA);
						setState(1742);
						parameterDecl();
						}
						} 
					}
					setState(1747);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,173,_ctx);
				}
				setState(1749);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1748);
					match(COMMA);
					}
				}

				}
			}

			setState(1753);
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
		enterRule(_localctx, 332, RULE_conversion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1755);
			nonNamedType();
			setState(1756);
			match(L_PAREN);
			setState(1757);
			expression(0);
			setState(1759);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1758);
				match(COMMA);
				}
			}

			setState(1761);
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
		enterRule(_localctx, 334, RULE_nonNamedType);
		try {
			setState(1768);
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
				setState(1763);
				typeLit();
				}
				break;
			case L_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1764);
				match(L_PAREN);
				setState(1765);
				nonNamedType();
				setState(1766);
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
		enterRule(_localctx, 336, RULE_operand);
		try {
			setState(1776);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1770);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1771);
				operandName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1772);
				match(L_PAREN);
				setState(1773);
				expression(0);
				setState(1774);
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
		enterRule(_localctx, 338, RULE_literal);
		try {
			setState(1781);
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
				setState(1778);
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
			case ADT:
			case MAP:
			case STRUCT:
			case IDENTIFIER:
			case L_BRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(1779);
				compositeLit();
				}
				break;
			case PRE:
			case PRESERVES:
			case POST:
			case DEC:
			case PURE:
			case PRIVATE:
			case TRUSTED:
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(1780);
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
		enterRule(_localctx, 340, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1783);
			_la = _input.LA(1);
			if ( !(((((_la - 141)) & ~0x3f) == 0 && ((1L << (_la - 141)) & ((1L << (DECIMAL_LIT - 141)) | (1L << (BINARY_LIT - 141)) | (1L << (OCTAL_LIT - 141)) | (1L << (HEX_LIT - 141)) | (1L << (IMAGINARY_LIT - 141)) | (1L << (RUNE_LIT - 141)))) != 0)) ) {
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
		enterRule(_localctx, 342, RULE_operandName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1785);
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
		enterRule(_localctx, 344, RULE_qualifiedIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1787);
			match(IDENTIFIER);
			setState(1788);
			match(DOT);
			setState(1789);
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
		enterRule(_localctx, 346, RULE_compositeLit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1791);
			literalType();
			setState(1792);
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
		enterRule(_localctx, 348, RULE_literalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1794);
			match(L_CURLY);
			setState(1799);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_CURLY - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
				{
				setState(1795);
				elementList();
				setState(1797);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1796);
					match(COMMA);
					}
				}

				}
			}

			setState(1801);
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
		enterRule(_localctx, 350, RULE_elementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1803);
			keyedElement();
			setState(1808);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,182,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1804);
					match(COMMA);
					setState(1805);
					keyedElement();
					}
					} 
				}
				setState(1810);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,182,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 352, RULE_keyedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1814);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,183,_ctx) ) {
			case 1:
				{
				setState(1811);
				key();
				setState(1812);
				match(COLON);
				}
				break;
			}
			setState(1816);
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
		enterRule(_localctx, 354, RULE_key);
		try {
			setState(1820);
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
			case ADT:
			case MATCH:
			case NONE:
			case PRED:
			case TYPE_OF:
			case IS_COMPARABLE:
			case PRIVATE:
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
				setState(1818);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1819);
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
		enterRule(_localctx, 356, RULE_element);
		try {
			setState(1824);
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
			case ADT:
			case MATCH:
			case NONE:
			case PRED:
			case TYPE_OF:
			case IS_COMPARABLE:
			case PRIVATE:
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
				setState(1822);
				expression(0);
				}
				break;
			case L_CURLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1823);
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
		enterRule(_localctx, 358, RULE_structType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1826);
			match(STRUCT);
			setState(1827);
			match(L_CURLY);
			setState(1833);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==STAR) {
				{
				{
				setState(1828);
				fieldDecl();
				setState(1829);
				eos();
				}
				}
				setState(1835);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1836);
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
		enterRule(_localctx, 360, RULE_fieldDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1842);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,187,_ctx) ) {
			case 1:
				{
				setState(1838);
				identifierList();
				setState(1839);
				type_();
				}
				break;
			case 2:
				{
				setState(1841);
				embeddedField();
				}
				break;
			}
			setState(1845);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,188,_ctx) ) {
			case 1:
				{
				setState(1844);
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
		enterRule(_localctx, 362, RULE_string_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1847);
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
		enterRule(_localctx, 364, RULE_embeddedField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1850);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1849);
				match(STAR);
				}
			}

			setState(1852);
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
		enterRule(_localctx, 366, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1854);
			match(L_BRACKET);
			setState(1855);
			expression(0);
			setState(1856);
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
		enterRule(_localctx, 368, RULE_typeAssertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1858);
			match(DOT);
			setState(1859);
			match(L_PAREN);
			setState(1860);
			type_();
			setState(1861);
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
		enterRule(_localctx, 370, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1863);
			match(L_PAREN);
			setState(1878);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FLOAT_LIT) | (1L << TRUE) | (1L << FALSE) | (1L << PRE) | (1L << PRESERVES) | (1L << POST) | (1L << DEC) | (1L << PURE) | (1L << OLD) | (1L << BEFORE) | (1L << FORALL) | (1L << EXISTS) | (1L << ACCESS) | (1L << UNFOLDING) | (1L << GHOST) | (1L << SEQ) | (1L << SET) | (1L << MSET) | (1L << DICT) | (1L << OPT) | (1L << LEN) | (1L << NEW) | (1L << MAKE) | (1L << CAP) | (1L << SOME) | (1L << GET) | (1L << DOM) | (1L << ADT) | (1L << MATCH) | (1L << NONE) | (1L << PRED) | (1L << TYPE_OF) | (1L << IS_COMPARABLE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (PRIVATE - 64)) | (1L << (WRITEPERM - 64)) | (1L << (NOPERM - 64)) | (1L << (TRUSTED - 64)) | (1L << (FUNC - 64)) | (1L << (INTERFACE - 64)) | (1L << (MAP - 64)) | (1L << (STRUCT - 64)) | (1L << (CHAN - 64)) | (1L << (RANGE - 64)) | (1L << (TYPE - 64)) | (1L << (NIL_LIT - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (L_PAREN - 64)) | (1L << (L_BRACKET - 64)))) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (EXCLAMATION - 134)) | (1L << (PLUS - 134)) | (1L << (MINUS - 134)) | (1L << (CARET - 134)) | (1L << (STAR - 134)) | (1L << (AMPERSAND - 134)) | (1L << (RECEIVE - 134)) | (1L << (DECIMAL_LIT - 134)) | (1L << (BINARY_LIT - 134)) | (1L << (OCTAL_LIT - 134)) | (1L << (HEX_LIT - 134)) | (1L << (IMAGINARY_LIT - 134)) | (1L << (RUNE_LIT - 134)) | (1L << (RAW_STRING_LIT - 134)) | (1L << (INTERPRETED_STRING_LIT - 134)))) != 0)) {
				{
				setState(1870);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,191,_ctx) ) {
				case 1:
					{
					setState(1864);
					expressionList();
					}
					break;
				case 2:
					{
					setState(1865);
					nonNamedType();
					setState(1868);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,190,_ctx) ) {
					case 1:
						{
						setState(1866);
						match(COMMA);
						setState(1867);
						expressionList();
						}
						break;
					}
					}
					break;
				}
				setState(1873);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELLIPSIS) {
					{
					setState(1872);
					match(ELLIPSIS);
					}
				}

				setState(1876);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1875);
					match(COMMA);
					}
				}

				}
			}

			setState(1880);
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
		enterRule(_localctx, 372, RULE_methodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1882);
			nonNamedType();
			setState(1883);
			match(DOT);
			setState(1884);
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
		enterRule(_localctx, 374, RULE_receiverType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1886);
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
		enterRule(_localctx, 376, RULE_eos);
		try {
			setState(1892);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,195,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1888);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1889);
				match(EOF);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1890);
				match(EOS);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1891);
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
		case 86:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 94:
			return primaryExpr_sempred((PrimaryExprContext)_localctx, predIndex);
		case 188:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a5\u0769\4\2\t"+
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
		"\4\u00ae\t\u00ae\4\u00af\t\u00af\4\u00b0\t\u00b0\4\u00b1\t\u00b1\4\u00b2"+
		"\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\4\u00b5\t\u00b5\4\u00b6\t\u00b6"+
		"\4\u00b7\t\u00b7\4\u00b8\t\u00b8\4\u00b9\t\u00b9\4\u00ba\t\u00ba\4\u00bb"+
		"\t\u00bb\4\u00bc\t\u00bc\4\u00bd\t\u00bd\4\u00be\t\u00be\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5\u0189\n\5\f\5\16\5\u018c\13\5\3"+
		"\6\3\6\5\6\u0190\n\6\3\7\3\7\3\7\7\7\u0195\n\7\f\7\16\7\u0198\13\7\3\7"+
		"\3\7\3\7\3\7\3\7\7\7\u019f\n\7\f\7\16\7\u01a2\13\7\3\7\3\7\3\7\5\7\u01a7"+
		"\n\7\3\7\3\7\7\7\u01ab\n\7\f\7\16\7\u01ae\13\7\3\7\3\7\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\n\3\n\3\n\7\n\u01bb\n\n\f\n\16\n\u01be\13\n\3\n\5\n\u01c1\n"+
		"\n\3\n\3\n\3\13\3\13\3\13\7\13\u01c8\n\13\f\13\16\13\u01cb\13\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u01d4\n\13\f\13\16\13\u01d7\13\13"+
		"\3\13\5\13\u01da\n\13\3\f\3\f\3\f\3\f\5\f\u01e0\n\f\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\5\r\u01e9\n\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\5\20"+
		"\u01f3\n\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\5\21\u0204\n\21\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24"+
		"\3\24\3\24\7\24\u0210\n\24\f\24\16\24\u0213\13\24\3\24\5\24\u0216\n\24"+
		"\3\25\3\25\3\25\7\25\u021b\n\25\f\25\16\25\u021e\13\25\3\25\3\25\3\26"+
		"\7\26\u0223\n\26\f\26\16\26\u0226\13\26\3\27\3\27\3\27\3\27\7\27\u022c"+
		"\n\27\f\27\16\27\u022f\13\27\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\31\3"+
		"\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3"+
		"\34\3\34\3\35\3\35\3\35\3\35\3\35\5\35\u024e\n\35\3\35\3\35\3\35\3\35"+
		"\3\36\3\36\5\36\u0256\n\36\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\""+
		"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\5#\u026e\n#\3#\3#\3$\3$\3$\3$\3$\3$\3"+
		"$\3%\3%\3%\3%\3%\3%\7%\u027f\n%\f%\16%\u0282\13%\3%\3%\3&\3&\3&\3&\3\'"+
		"\3\'\3\'\3\'\7\'\u028e\n\'\f\'\16\'\u0291\13\'\3\'\3\'\3(\3(\3(\3(\3)"+
		"\3)\3)\3)\5)\u029d\n)\3*\3*\3*\3*\3*\7*\u02a4\n*\f*\16*\u02a7\13*\3*\3"+
		"*\3+\3+\3+\3+\3+\3+\3+\3+\3+\5+\u02b4\n+\3,\3,\3,\3,\3,\7,\u02bb\n,\f"+
		",\16,\u02be\13,\3,\3,\3-\3-\3-\3-\3-\7-\u02c7\n-\f-\16-\u02ca\13-\3-\3"+
		"-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\5/\u02de\n/\3\60\3\60"+
		"\3\60\3\60\3\60\5\60\u02e5\n\60\3\60\7\60\u02e8\n\60\f\60\16\60\u02eb"+
		"\13\60\3\60\3\60\5\60\u02ef\n\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3"+
		"\61\3\61\3\61\5\61\u02fb\n\61\3\62\5\62\u02fe\n\62\3\62\3\62\5\62\u0302"+
		"\n\62\3\63\3\63\5\63\u0306\n\63\3\64\3\64\3\64\3\64\7\64\u030c\n\64\f"+
		"\64\16\64\u030f\13\64\3\64\5\64\u0312\n\64\5\64\u0314\n\64\3\64\3\64\3"+
		"\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\7\66\u0320\n\66\f\66\16\66\u0323"+
		"\13\66\3\66\3\66\3\67\3\67\3\67\5\67\u032a\n\67\38\38\38\58\u032f\n8\3"+
		"9\39\39\39\39\39\59\u0337\n9\59\u0339\n9\39\39\39\59\u033e\n9\3:\3:\3"+
		":\7:\u0343\n:\f:\16:\u0346\13:\3;\3;\3;\3;\3;\5;\u034d\n;\3;\5;\u0350"+
		"\n;\3;\3;\3<\3<\5<\u0356\n<\3<\3<\3<\5<\u035b\n<\5<\u035d\n<\3<\5<\u0360"+
		"\n<\3=\3=\3=\7=\u0365\n=\f=\16=\u0368\13=\3>\3>\5>\u036c\n>\3>\3>\3?\3"+
		"?\3?\3?\3?\3?\3@\3@\3@\3@\3@\3@\3@\7@\u037d\n@\f@\16@\u0380\13@\3@\3@"+
		"\3@\7@\u0385\n@\f@\16@\u0388\13@\3@\5@\u038b\n@\3A\5A\u038e\nA\3A\3A\3"+
		"A\3A\5A\u0394\nA\3B\3B\5B\u0398\nB\3B\5B\u039b\nB\3B\3B\3B\3C\3C\3C\3"+
		"C\3C\5C\u03a5\nC\3D\3D\3D\3D\3D\5D\u03ac\nD\3E\3E\3E\3E\3E\5E\u03b3\n"+
		"E\3E\3E\3F\3F\3F\3F\3F\3G\3G\3G\3G\3G\3G\5G\u03c2\nG\3H\3H\3H\3H\5H\u03c8"+
		"\nH\3I\3I\3I\3I\3I\5I\u03cf\nI\3J\3J\3J\5J\u03d4\nJ\3K\3K\3K\3K\5K\u03da"+
		"\nK\3L\3L\3L\3L\3L\3M\3M\3M\3M\3M\5M\u03e6\nM\3N\3N\5N\u03ea\nN\3N\3N"+
		"\3N\5N\u03ef\nN\3O\3O\5O\u03f3\nO\3O\3O\3O\5O\u03f8\nO\3P\3P\5P\u03fc"+
		"\nP\3P\3P\3P\5P\u0401\nP\3Q\3Q\3Q\3Q\5Q\u0407\nQ\3Q\3Q\5Q\u040b\nQ\3R"+
		"\3R\3R\3R\3S\3S\5S\u0413\nS\3S\3S\5S\u0417\nS\3S\3S\3T\3T\5T\u041d\nT"+
		"\3U\5U\u0420\nU\3U\3U\3V\3V\5V\u0426\nV\3V\3V\3W\5W\u042b\nW\3W\3W\3X"+
		"\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\5X\u043f\nX\3X\3X\3X\3X"+
		"\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X"+
		"\3X\3X\3X\3X\3X\3X\7X\u0462\nX\fX\16X\u0465\13X\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3"+
		"Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\5Y\u047b\nY\3Z\3Z\3Z\3[\3[\3[\5"+
		"[\u0483\n[\3\\\3\\\3\\\3]\3]\3]\3]\7]\u048c\n]\f]\16]\u048f\13]\3]\3]"+
		"\3]\3]\5]\u0495\n]\3^\3^\3^\3^\3^\5^\u049c\n^\3_\3_\3_\3_\3_\3_\3_\3_"+
		"\5_\u04a6\n_\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\5`\u04b4\n`\3`\3`\3`"+
		"\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\7`\u04ca\n`\f`\16"+
		"`\u04cd\13`\3a\3a\3a\3b\3b\5b\u04d4\nb\3b\3b\5b\u04d8\nb\3c\3c\5c\u04dc"+
		"\nc\3c\5c\u04df\nc\3c\3c\3d\3d\3d\3d\3d\5d\u04e8\nd\3d\3d\7d\u04ec\nd"+
		"\fd\16d\u04ef\13d\3d\3d\3e\3e\3e\3e\3f\5f\u04f8\nf\3f\3f\3f\3f\3f\3f\5"+
		"f\u0500\nf\3f\3f\3f\3f\5f\u0506\nf\3g\3g\3g\3g\3g\3g\3g\5g\u050f\ng\3"+
		"h\3h\3h\3h\3h\3h\3h\3h\3h\5h\u051a\nh\3i\3i\3i\3j\3j\3j\3j\7j\u0523\n"+
		"j\fj\16j\u0526\13j\3j\5j\u0529\nj\5j\u052b\nj\3j\3j\3k\3k\3k\3k\3k\3k"+
		"\3k\5k\u0536\nk\3l\3l\3l\3l\3l\3m\3m\5m\u053f\nm\3m\3m\5m\u0543\nm\3m"+
		"\5m\u0546\nm\3m\3m\3m\3m\3m\5m\u054d\nm\3m\3m\3n\3n\3o\3o\3p\3p\3q\5q"+
		"\u0558\nq\3q\3q\3r\3r\3r\3r\3r\3r\5r\u0562\nr\3r\3r\3r\3r\5r\u0568\nr"+
		"\5r\u056a\nr\3s\3s\3s\3t\3t\3u\3u\3u\5u\u0574\nu\3v\3v\3v\3v\3v\3v\7v"+
		"\u057c\nv\fv\16v\u057f\13v\3v\5v\u0582\nv\3w\3w\5w\u0586\nw\3w\3w\5w\u058a"+
		"\nw\3x\3x\3x\7x\u058f\nx\fx\16x\u0592\13x\3y\3y\3y\7y\u0597\ny\fy\16y"+
		"\u059a\13y\3z\3z\3z\3z\3z\3z\7z\u05a2\nz\fz\16z\u05a5\13z\3z\5z\u05a8"+
		"\nz\3{\3{\5{\u05ac\n{\3{\3{\3|\3|\3|\3|\3|\3|\7|\u05b6\n|\f|\16|\u05b9"+
		"\13|\3|\5|\u05bc\n|\3}\3}\5}\u05c0\n}\3}\3}\3~\5~\u05c5\n~\3~\3~\3~\6"+
		"~\u05ca\n~\r~\16~\u05cb\3\177\3\177\3\177\3\177\3\177\5\177\u05d3\n\177"+
		"\3\u0080\3\u0080\3\u0081\3\u0081\3\u0081\3\u0081\3\u0082\3\u0082\3\u0082"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085"+
		"\5\u0085\u05e7\n\u0085\3\u0086\3\u0086\5\u0086\u05eb\n\u0086\3\u0087\3"+
		"\u0087\5\u0087\u05ef\n\u0087\3\u0088\3\u0088\5\u0088\u05f3\n\u0088\3\u0089"+
		"\3\u0089\3\u0089\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b"+
		"\3\u008b\3\u008b\3\u008b\3\u008b\5\u008b\u0603\n\u008b\3\u008b\3\u008b"+
		"\3\u008b\3\u008b\5\u008b\u0609\n\u008b\5\u008b\u060b\n\u008b\3\u008c\3"+
		"\u008c\5\u008c\u060f\n\u008c\3\u008d\3\u008d\5\u008d\u0613\n\u008d\3\u008d"+
		"\5\u008d\u0616\n\u008d\3\u008d\3\u008d\5\u008d\u061a\n\u008d\5\u008d\u061c"+
		"\n\u008d\3\u008d\3\u008d\7\u008d\u0620\n\u008d\f\u008d\16\u008d\u0623"+
		"\13\u008d\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e\5\u008e\u062a\n\u008e"+
		"\3\u008f\3\u008f\3\u008f\5\u008f\u062f\n\u008f\3\u0090\3\u0090\3\u0090"+
		"\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\5\u0090\u063a\n\u0090"+
		"\3\u0090\3\u0090\7\u0090\u063e\n\u0090\f\u0090\16\u0090\u0641\13\u0090"+
		"\3\u0090\3\u0090\3\u0091\3\u0091\5\u0091\u0647\n\u0091\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092\5\u0092\u0652"+
		"\n\u0092\3\u0093\3\u0093\3\u0093\5\u0093\u0657\n\u0093\3\u0094\3\u0094"+
		"\5\u0094\u065b\n\u0094\3\u0094\3\u0094\3\u0094\5\u0094\u0660\n\u0094\7"+
		"\u0094\u0662\n\u0094\f\u0094\16\u0094\u0665\13\u0094\3\u0095\3\u0095\3"+
		"\u0095\7\u0095\u066a\n\u0095\f\u0095\16\u0095\u066d\13\u0095\3\u0095\3"+
		"\u0095\3\u0096\3\u0096\3\u0096\5\u0096\u0674\n\u0096\3\u0097\3\u0097\3"+
		"\u0097\5\u0097\u0679\n\u0097\3\u0097\5\u0097\u067c\n\u0097\3\u0098\3\u0098"+
		"\3\u0098\3\u0098\3\u0098\3\u0098\5\u0098\u0684\n\u0098\3\u0098\3\u0098"+
		"\3\u0099\3\u0099\3\u0099\3\u0099\5\u0099\u068c\n\u0099\3\u0099\3\u0099"+
		"\3\u009a\5\u009a\u0691\n\u009a\3\u009a\3\u009a\5\u009a\u0695\n\u009a\3"+
		"\u009a\3\u009a\5\u009a\u0699\n\u009a\3\u009b\3\u009b\3\u009b\3\u009c\3"+
		"\u009c\5\u009c\u06a0\n\u009c\3\u009d\3\u009d\3\u009d\3\u009d\3\u009d\3"+
		"\u009e\3\u009e\3\u009f\3\u009f\3\u00a0\3\u00a0\3\u00a0\3\u00a1\3\u00a1"+
		"\3\u00a1\3\u00a1\3\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a3"+
		"\3\u00a3\3\u00a3\3\u00a3\3\u00a3\5\u00a3\u06bd\n\u00a3\3\u00a3\3\u00a3"+
		"\3\u00a4\3\u00a4\3\u00a4\3\u00a5\3\u00a5\3\u00a5\3\u00a5\5\u00a5\u06c8"+
		"\n\u00a5\3\u00a6\3\u00a6\5\u00a6\u06cc\n\u00a6\3\u00a7\3\u00a7\3\u00a7"+
		"\3\u00a7\7\u00a7\u06d2\n\u00a7\f\u00a7\16\u00a7\u06d5\13\u00a7\3\u00a7"+
		"\5\u00a7\u06d8\n\u00a7\5\u00a7\u06da\n\u00a7\3\u00a7\3\u00a7\3\u00a8\3"+
		"\u00a8\3\u00a8\3\u00a8\5\u00a8\u06e2\n\u00a8\3\u00a8\3\u00a8\3\u00a9\3"+
		"\u00a9\3\u00a9\3\u00a9\3\u00a9\5\u00a9\u06eb\n\u00a9\3\u00aa\3\u00aa\3"+
		"\u00aa\3\u00aa\3\u00aa\3\u00aa\5\u00aa\u06f3\n\u00aa\3\u00ab\3\u00ab\3"+
		"\u00ab\5\u00ab\u06f8\n\u00ab\3\u00ac\3\u00ac\3\u00ad\3\u00ad\3\u00ae\3"+
		"\u00ae\3\u00ae\3\u00ae\3\u00af\3\u00af\3\u00af\3\u00b0\3\u00b0\3\u00b0"+
		"\5\u00b0\u0708\n\u00b0\5\u00b0\u070a\n\u00b0\3\u00b0\3\u00b0\3\u00b1\3"+
		"\u00b1\3\u00b1\7\u00b1\u0711\n\u00b1\f\u00b1\16\u00b1\u0714\13\u00b1\3"+
		"\u00b2\3\u00b2\3\u00b2\5\u00b2\u0719\n\u00b2\3\u00b2\3\u00b2\3\u00b3\3"+
		"\u00b3\5\u00b3\u071f\n\u00b3\3\u00b4\3\u00b4\5\u00b4\u0723\n\u00b4\3\u00b5"+
		"\3\u00b5\3\u00b5\3\u00b5\3\u00b5\7\u00b5\u072a\n\u00b5\f\u00b5\16\u00b5"+
		"\u072d\13\u00b5\3\u00b5\3\u00b5\3\u00b6\3\u00b6\3\u00b6\3\u00b6\5\u00b6"+
		"\u0735\n\u00b6\3\u00b6\5\u00b6\u0738\n\u00b6\3\u00b7\3\u00b7\3\u00b8\5"+
		"\u00b8\u073d\n\u00b8\3\u00b8\3\u00b8\3\u00b9\3\u00b9\3\u00b9\3\u00b9\3"+
		"\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00bb\3\u00bb\3\u00bb\3\u00bb"+
		"\3\u00bb\5\u00bb\u074f\n\u00bb\5\u00bb\u0751\n\u00bb\3\u00bb\5\u00bb\u0754"+
		"\n\u00bb\3\u00bb\5\u00bb\u0757\n\u00bb\5\u00bb\u0759\n\u00bb\3\u00bb\3"+
		"\u00bb\3\u00bc\3\u00bc\3\u00bc\3\u00bc\3\u00bd\3\u00bd\3\u00be\3\u00be"+
		"\3\u00be\3\u00be\5\u00be\u0767\n\u00be\3\u00be\4\u02e9\u030d\4\u00ae\u00be"+
		"\u00bf\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<"+
		">@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a"+
		"\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2"+
		"\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba"+
		"\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2"+
		"\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea"+
		"\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102"+
		"\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a"+
		"\u011c\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132"+
		"\u0134\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a"+
		"\u014c\u014e\u0150\u0152\u0154\u0156\u0158\u015a\u015c\u015e\u0160\u0162"+
		"\u0164\u0166\u0168\u016a\u016c\u016e\u0170\u0172\u0174\u0176\u0178\u017a"+
		"\2\25\4\2jjuu\3\2\31\32\3\2\7\n\3\2CD\3\2)+\4\2)+--\3\2\u0088\u008e\3"+
		"\2\26\27\4\2\u0083\u0087\u008c\u008d\6\2$$vv\u0082\u0082\u0089\u008b\3"+
		"\2 \"\3\2\35\37\4\2JK|\u0081\6\2..\61\61\64\64bb\4\2\u0082\u0087\u0089"+
		"\u008d\3\2vw\4\2ss\u00a4\u00a4\4\2\u008f\u0092\u0094\u0095\3\2\u009b\u009c"+
		"\2\u07d4\2\u017c\3\2\2\2\4\u017f\3\2\2\2\6\u0182\3\2\2\2\b\u0185\3\2\2"+
		"\2\n\u018d\3\2\2\2\f\u0196\3\2\2\2\16\u01b1\3\2\2\2\20\u01b4\3\2\2\2\22"+
		"\u01bc\3\2\2\2\24\u01c9\3\2\2\2\26\u01df\3\2\2\2\30\u01e8\3\2\2\2\32\u01ea"+
		"\3\2\2\2\34\u01ec\3\2\2\2\36\u01ef\3\2\2\2 \u0203\3\2\2\2\"\u0205\3\2"+
		"\2\2$\u0207\3\2\2\2&\u020c\3\2\2\2(\u0217\3\2\2\2*\u0224\3\2\2\2,\u0227"+
		"\3\2\2\2.\u0232\3\2\2\2\60\u0234\3\2\2\2\62\u0239\3\2\2\2\64\u023e\3\2"+
		"\2\2\66\u0243\3\2\2\28\u0248\3\2\2\2:\u0255\3\2\2\2<\u0257\3\2\2\2>\u0259"+
		"\3\2\2\2@\u025e\3\2\2\2B\u0263\3\2\2\2D\u0268\3\2\2\2F\u0271\3\2\2\2H"+
		"\u0278\3\2\2\2J\u0285\3\2\2\2L\u0289\3\2\2\2N\u0294\3\2\2\2P\u029c\3\2"+
		"\2\2R\u029e\3\2\2\2T\u02b3\3\2\2\2V\u02b5\3\2\2\2X\u02c1\3\2\2\2Z\u02cd"+
		"\3\2\2\2\\\u02dd\3\2\2\2^\u02e9\3\2\2\2`\u02fa\3\2\2\2b\u02fd\3\2\2\2"+
		"d\u0305\3\2\2\2f\u0307\3\2\2\2h\u0317\3\2\2\2j\u031b\3\2\2\2l\u0326\3"+
		"\2\2\2n\u032e\3\2\2\2p\u033d\3\2\2\2r\u033f\3\2\2\2t\u0347\3\2\2\2v\u0355"+
		"\3\2\2\2x\u0361\3\2\2\2z\u036b\3\2\2\2|\u036f\3\2\2\2~\u0375\3\2\2\2\u0080"+
		"\u038d\3\2\2\2\u0082\u0395\3\2\2\2\u0084\u03a4\3\2\2\2\u0086\u03a6\3\2"+
		"\2\2\u0088\u03ad\3\2\2\2\u008a\u03b6\3\2\2\2\u008c\u03bb\3\2\2\2\u008e"+
		"\u03c3\3\2\2\2\u0090\u03c9\3\2\2\2\u0092\u03d0\3\2\2\2\u0094\u03d5\3\2"+
		"\2\2\u0096\u03db\3\2\2\2\u0098\u03e0\3\2\2\2\u009a\u03e7\3\2\2\2\u009c"+
		"\u03f0\3\2\2\2\u009e\u03f9\3\2\2\2\u00a0\u0402\3\2\2\2\u00a2\u040c\3\2"+
		"\2\2\u00a4\u0410\3\2\2\2\u00a6\u041c\3\2\2\2\u00a8\u041f\3\2\2\2\u00aa"+
		"\u0423\3\2\2\2\u00ac\u042a\3\2\2\2\u00ae\u043e\3\2\2\2\u00b0\u047a\3\2"+
		"\2\2\u00b2\u047c\3\2\2\2\u00b4\u047f\3\2\2\2\u00b6\u0484\3\2\2\2\u00b8"+
		"\u048d\3\2\2\2\u00ba\u049b\3\2\2\2\u00bc\u04a5\3\2\2\2\u00be\u04b3\3\2"+
		"\2\2\u00c0\u04ce\3\2\2\2\u00c2\u04d1\3\2\2\2\u00c4\u04d9\3\2\2\2\u00c6"+
		"\u04e2\3\2\2\2\u00c8\u04f2\3\2\2\2\u00ca\u0505\3\2\2\2\u00cc\u050e\3\2"+
		"\2\2\u00ce\u0519\3\2\2\2\u00d0\u051b\3\2\2\2\u00d2\u051e\3\2\2\2\u00d4"+
		"\u0535\3\2\2\2\u00d6\u0537\3\2\2\2\u00d8\u053c\3\2\2\2\u00da\u0550\3\2"+
		"\2\2\u00dc\u0552\3\2\2\2\u00de\u0554\3\2\2\2\u00e0\u0557\3\2\2\2\u00e2"+
		"\u0561\3\2\2\2\u00e4\u056b\3\2\2\2\u00e6\u056e\3\2\2\2\u00e8\u0573\3\2"+
		"\2\2\u00ea\u0575\3\2\2\2\u00ec\u0583\3\2\2\2\u00ee\u058b\3\2\2\2\u00f0"+
		"\u0593\3\2\2\2\u00f2\u059b\3\2\2\2\u00f4\u05a9\3\2\2\2\u00f6\u05af\3\2"+
		"\2\2\u00f8\u05bd\3\2\2\2\u00fa\u05c9\3\2\2\2\u00fc\u05d2\3\2\2\2\u00fe"+
		"\u05d4\3\2\2\2\u0100\u05d6\3\2\2\2\u0102\u05da\3\2\2\2\u0104\u05dd\3\2"+
		"\2\2\u0106\u05e1\3\2\2\2\u0108\u05e3\3\2\2\2\u010a\u05e8\3\2\2\2\u010c"+
		"\u05ec\3\2\2\2\u010e\u05f0\3\2\2\2\u0110\u05f4\3\2\2\2\u0112\u05f7\3\2"+
		"\2\2\u0114\u05f9\3\2\2\2\u0116\u060e\3\2\2\2\u0118\u0610\3\2\2\2\u011a"+
		"\u0626\3\2\2\2\u011c\u062e\3\2\2\2\u011e\u0630\3\2\2\2\u0120\u0646\3\2"+
		"\2\2\u0122\u064e\3\2\2\2\u0124\u0656\3\2\2\2\u0126\u065a\3\2\2\2\u0128"+
		"\u0666\3\2\2\2\u012a\u0670\3\2\2\2\u012c\u067b\3\2\2\2\u012e\u0683\3\2"+
		"\2\2\u0130\u0687\3\2\2\2\u0132\u0690\3\2\2\2\u0134\u069a\3\2\2\2\u0136"+
		"\u069f\3\2\2\2\u0138\u06a1\3\2\2\2\u013a\u06a6\3\2\2\2\u013c\u06a8\3\2"+
		"\2\2\u013e\u06aa\3\2\2\2\u0140\u06ad\3\2\2\2\u0142\u06b1\3\2\2\2\u0144"+
		"\u06bc\3\2\2\2\u0146\u06c0\3\2\2\2\u0148\u06c7\3\2\2\2\u014a\u06cb\3\2"+
		"\2\2\u014c\u06cd\3\2\2\2\u014e\u06dd\3\2\2\2\u0150\u06ea\3\2\2\2\u0152"+
		"\u06f2\3\2\2\2\u0154\u06f7\3\2\2\2\u0156\u06f9\3\2\2\2\u0158\u06fb\3\2"+
		"\2\2\u015a\u06fd\3\2\2\2\u015c\u0701\3\2\2\2\u015e\u0704\3\2\2\2\u0160"+
		"\u070d\3\2\2\2\u0162\u0718\3\2\2\2\u0164\u071e\3\2\2\2\u0166\u0722\3\2"+
		"\2\2\u0168\u0724\3\2\2\2\u016a\u0734\3\2\2\2\u016c\u0739\3\2\2\2\u016e"+
		"\u073c\3\2\2\2\u0170\u0740\3\2\2\2\u0172\u0744\3\2\2\2\u0174\u0749\3\2"+
		"\2\2\u0176\u075c\3\2\2\2\u0178\u0760\3\2\2\2\u017a\u0766\3\2\2\2\u017c"+
		"\u017d\5\u00aeX\2\u017d\u017e\7\2\2\3\u017e\3\3\2\2\2\u017f\u0180\5\u00b0"+
		"Y\2\u0180\u0181\7\2\2\3\u0181\5\3\2\2\2\u0182\u0183\5\u00ccg\2\u0183\u0184"+
		"\7\2\2\3\u0184\7\3\2\2\2\u0185\u018a\5\n\6\2\u0186\u0187\7r\2\2\u0187"+
		"\u0189\5\n\6\2\u0188\u0186\3\2\2\2\u0189\u018c\3\2\2\2\u018a\u0188\3\2"+
		"\2\2\u018a\u018b\3\2\2\2\u018b\t\3\2\2\2\u018c\u018a\3\2\2\2\u018d\u018f"+
		"\7j\2\2\u018e\u0190\7=\2\2\u018f\u018e\3\2\2\2\u018f\u0190\3\2\2\2\u0190"+
		"\13\3\2\2\2\u0191\u0192\5\16\b\2\u0192\u0193\5\u017a\u00be\2\u0193\u0195"+
		"\3\2\2\2\u0194\u0191\3\2\2\2\u0195\u0198\3\2\2\2\u0196\u0194\3\2\2\2\u0196"+
		"\u0197\3\2\2\2\u0197\u0199\3\2\2\2\u0198\u0196\3\2\2\2\u0199\u019a\5\u00e4"+
		"s\2\u019a\u01a0\5\u017a\u00be\2\u019b\u019c\5\24\13\2\u019c\u019d\5\u017a"+
		"\u00be\2\u019d\u019f\3\2\2\2\u019e\u019b\3\2\2\2\u019f\u01a2\3\2\2\2\u01a0"+
		"\u019e\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01ac\3\2\2\2\u01a2\u01a0\3\2"+
		"\2\2\u01a3\u01a7\5\u008cG\2\u01a4\u01a7\5\u00e8u\2\u01a5\u01a7\5\26\f"+
		"\2\u01a6\u01a3\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a6\u01a5\3\2\2\2\u01a7\u01a8"+
		"\3\2\2\2\u01a8\u01a9\5\u017a\u00be\2\u01a9\u01ab\3\2\2\2\u01aa\u01a6\3"+
		"\2\2\2\u01ab\u01ae\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad"+
		"\u01af\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b0\7\2\2\3\u01b0\r\3\2\2\2"+
		"\u01b1\u01b2\7G\2\2\u01b2\u01b3\5\u00aeX\2\u01b3\17\3\2\2\2\u01b4\u01b5"+
		"\7H\2\2\u01b5\u01b6\5\u00aeX\2\u01b6\21\3\2\2\2\u01b7\u01b8\5\20\t\2\u01b8"+
		"\u01b9\5\u017a\u00be\2\u01b9\u01bb\3\2\2\2\u01ba\u01b7\3\2\2\2\u01bb\u01be"+
		"\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01c0\3\2\2\2\u01be"+
		"\u01bc\3\2\2\2\u01bf\u01c1\t\2\2\2\u01c0\u01bf\3\2\2\2\u01c0\u01c1\3\2"+
		"\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c3\5\u00e6t\2\u01c3\23\3\2\2\2\u01c4"+
		"\u01c5\5\20\t\2\u01c5\u01c6\5\u017a\u00be\2\u01c6\u01c8\3\2\2\2\u01c7"+
		"\u01c4\3\2\2\2\u01c8\u01cb\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2"+
		"\2\2\u01ca\u01d9\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cc\u01cd\7f\2\2\u01cd"+
		"\u01da\5\22\n\2\u01ce\u01cf\7f\2\2\u01cf\u01d5\7k\2\2\u01d0\u01d1\5\22"+
		"\n\2\u01d1\u01d2\5\u017a\u00be\2\u01d2\u01d4\3\2\2\2\u01d3\u01d0\3\2\2"+
		"\2\u01d4\u01d7\3\2\2\2\u01d5\u01d3\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d8"+
		"\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d8\u01da\7l\2\2\u01d9\u01cc\3\2\2\2\u01d9"+
		"\u01ce\3\2\2\2\u01da\25\3\2\2\2\u01db\u01e0\5~@\2\u01dc\u01e0\5\u0094"+
		"K\2\u01dd\u01e0\5\u0098M\2\u01de\u01e0\5\u0092J\2\u01df\u01db\3\2\2\2"+
		"\u01df\u01dc\3\2\2\2\u01df\u01dd\3\2\2\2\u01df\u01de\3\2\2\2\u01e0\27"+
		"\3\2\2\2\u01e1\u01e2\7\34\2\2\u01e2\u01e9\5\u00b0Y\2\u01e3\u01e4\t\3\2"+
		"\2\u01e4\u01e9\5.\30\2\u01e5\u01e6\t\4\2\2\u01e6\u01e9\5\u00aeX\2\u01e7"+
		"\u01e9\5j\66\2\u01e8\u01e1\3\2\2\2\u01e8\u01e3\3\2\2\2\u01e8\u01e5\3\2"+
		"\2\2\u01e8\u01e7\3\2\2\2\u01e9\31\3\2\2\2\u01ea\u01eb\5\34\17\2\u01eb"+
		"\33\3\2\2\2\u01ec\u01ed\5^\60\2\u01ed\u01ee\5\36\20\2\u01ee\35\3\2\2\2"+
		"\u01ef\u01f0\7F\2\2\u01f0\u01f2\7k\2\2\u01f1\u01f3\5\u00fa~\2\u01f2\u01f1"+
		"\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f5\7l\2\2\u01f5"+
		"\37\3\2\2\2\u01f6\u0204\5F$\2\u01f7\u0204\5D#\2\u01f8\u0204\5B\"\2\u01f9"+
		"\u0204\5$\23\2\u01fa\u0204\5@!\2\u01fb\u0204\58\35\2\u01fc\u0204\5> \2"+
		"\u01fd\u0204\5\66\34\2\u01fe\u0204\5\62\32\2\u01ff\u0204\5\60\31\2\u0200"+
		"\u0204\5\64\33\2\u0201\u0204\5\"\22\2\u0202\u0204\5H%\2\u0203\u01f6\3"+
		"\2\2\2\u0203\u01f7\3\2\2\2\u0203\u01f8\3\2\2\2\u0203\u01f9\3\2\2\2\u0203"+
		"\u01fa\3\2\2\2\u0203\u01fb\3\2\2\2\u0203\u01fc\3\2\2\2\u0203\u01fd\3\2"+
		"\2\2\u0203\u01fe\3\2\2\2\u0203\u01ff\3\2\2\2\u0203\u0200\3\2\2\2\u0203"+
		"\u0201\3\2\2\2\u0203\u0202\3\2\2\2\u0204!\3\2\2\2\u0205\u0206\t\5\2\2"+
		"\u0206#\3\2\2\2\u0207\u0208\7c\2\2\u0208\u0209\7o\2\2\u0209\u020a\5\u00cc"+
		"g\2\u020a\u020b\7p\2\2\u020b%\3\2\2\2\u020c\u0211\5(\25\2\u020d\u020e"+
		"\7r\2\2\u020e\u0210\5(\25\2\u020f\u020d\3\2\2\2\u0210\u0213\3\2\2\2\u0211"+
		"\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0215\3\2\2\2\u0213\u0211\3\2"+
		"\2\2\u0214\u0216\7r\2\2\u0215\u0214\3\2\2\2\u0215\u0216\3\2\2\2\u0216"+
		"\'\3\2\2\2\u0217\u021c\7j\2\2\u0218\u0219\7r\2\2\u0219\u021b\7j\2\2\u021a"+
		"\u0218\3\2\2\2\u021b\u021e\3\2\2\2\u021c\u021a\3\2\2\2\u021c\u021d\3\2"+
		"\2\2\u021d\u021f\3\2\2\2\u021e\u021c\3\2\2\2\u021f\u0220\5\u013c\u009f"+
		"\2\u0220)\3\2\2\2\u0221\u0223\5,\27\2\u0222\u0221\3\2\2\2\u0223\u0226"+
		"\3\2\2\2\u0224\u0222\3\2\2\2\u0224\u0225\3\2\2\2\u0225+\3\2\2\2\u0226"+
		"\u0224\3\2\2\2\u0227\u0228\7m\2\2\u0228\u022d\5\u00aeX\2\u0229\u022a\7"+
		"r\2\2\u022a\u022c\5\u00aeX\2\u022b\u0229\3\2\2\2\u022c\u022f\3\2\2\2\u022d"+
		"\u022b\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u0230\3\2\2\2\u022f\u022d\3\2"+
		"\2\2\u0230\u0231\7n\2\2\u0231-\3\2\2\2\u0232\u0233\5\u00be`\2\u0233/\3"+
		"\2\2\2\u0234\u0235\7\62\2\2\u0235\u0236\7k\2\2\u0236\u0237\5\u00aeX\2"+
		"\u0237\u0238\7l\2\2\u0238\61\3\2\2\2\u0239\u023a\78\2\2\u023a\u023b\7"+
		"o\2\2\u023b\u023c\5\u00ccg\2\u023c\u023d\7p\2\2\u023d\63\3\2\2\2\u023e"+
		"\u023f\7\63\2\2\u023f\u0240\7k\2\2\u0240\u0241\5\u00aeX\2\u0241\u0242"+
		"\7l\2\2\u0242\65\3\2\2\2\u0243\u0244\t\6\2\2\u0244\u0245\7k\2\2\u0245"+
		"\u0246\5\u00aeX\2\u0246\u0247\7l\2\2\u0247\67\3\2\2\2\u0248\u024d\7\23"+
		"\2\2\u0249\u024a\7o\2\2\u024a\u024b\5:\36\2\u024b\u024c\7p\2\2\u024c\u024e"+
		"\3\2\2\2\u024d\u0249\3\2\2\2\u024d\u024e\3\2\2\2\u024e\u024f\3\2\2\2\u024f"+
		"\u0250\7k\2\2\u0250\u0251\5\u00aeX\2\u0251\u0252\7l\2\2\u02529\3\2\2\2"+
		"\u0253\u0256\5<\37\2\u0254\u0256\7\25\2\2\u0255\u0253\3\2\2\2\u0255\u0254"+
		"\3\2\2\2\u0256;\3\2\2\2\u0257\u0258\7j\2\2\u0258=\3\2\2\2\u0259\u025a"+
		"\7\24\2\2\u025a\u025b\7k\2\2\u025b\u025c\5\u00aeX\2\u025c\u025d\7l\2\2"+
		"\u025d?\3\2\2\2\u025e\u025f\7;\2\2\u025f\u0260\7k\2\2\u0260\u0261\5\u00ae"+
		"X\2\u0261\u0262\7l\2\2\u0262A\3\2\2\2\u0263\u0264\7:\2\2\u0264\u0265\7"+
		"k\2\2\u0265\u0266\5\u00aeX\2\u0266\u0267\7l\2\2\u0267C\3\2\2\2\u0268\u0269"+
		"\7\30\2\2\u0269\u026a\7k\2\2\u026a\u026d\5\u00aeX\2\u026b\u026c\7r\2\2"+
		"\u026c\u026e\5\u00aeX\2\u026d\u026b\3\2\2\2\u026d\u026e\3\2\2\2\u026e"+
		"\u026f\3\2\2\2\u026f\u0270\7l\2\2\u0270E\3\2\2\2\u0271\u0272\t\6\2\2\u0272"+
		"\u0273\7o\2\2\u0273\u0274\5\u00aeX\2\u0274\u0275\7>\2\2\u0275\u0276\5"+
		"\u00aeX\2\u0276\u0277\7p\2\2\u0277G\3\2\2\2\u0278\u0279\7\67\2\2\u0279"+
		"\u027a\5\u00aeX\2\u027a\u0280\7m\2\2\u027b\u027c\5J&\2\u027c\u027d\5\u017a"+
		"\u00be\2\u027d\u027f\3\2\2\2\u027e\u027b\3\2\2\2\u027f\u0282\3\2\2\2\u0280"+
		"\u027e\3\2\2\2\u0280\u0281\3\2\2\2\u0281\u0283\3\2\2\2\u0282\u0280\3\2"+
		"\2\2\u0283\u0284\7n\2\2\u0284I\3\2\2\2\u0285\u0286\5n8\2\u0286\u0287\7"+
		"t\2\2\u0287\u0288\5\u00aeX\2\u0288K\3\2\2\2\u0289\u028a\7o\2\2\u028a\u028f"+
		"\5N(\2\u028b\u028c\7r\2\2\u028c\u028e\5N(\2\u028d\u028b\3\2\2\2\u028e"+
		"\u0291\3\2\2\2\u028f\u028d\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0292\3\2"+
		"\2\2\u0291\u028f\3\2\2\2\u0292\u0293\7p\2\2\u0293M\3\2\2\2\u0294\u0295"+
		"\5\u00aeX\2\u0295\u0296\7q\2\2\u0296\u0297\5\u00aeX\2\u0297O\3\2\2\2\u0298"+
		"\u029d\5\\/\2\u0299\u029d\5Z.\2\u029a\u029d\5R*\2\u029b\u029d\5V,\2\u029c"+
		"\u0298\3\2\2\2\u029c\u0299\3\2\2\2\u029c\u029a\3\2\2\2\u029c\u029b\3\2"+
		"\2\2\u029dQ\3\2\2\2\u029e\u029f\7\64\2\2\u029f\u02a5\7m\2\2\u02a0\u02a1"+
		"\5T+\2\u02a1\u02a2\5\u017a\u00be\2\u02a2\u02a4\3\2\2\2\u02a3\u02a0\3\2"+
		"\2\2\u02a4\u02a7\3\2\2\2\u02a5\u02a3\3\2\2\2\u02a5\u02a6\3\2\2\2\u02a6"+
		"\u02a8\3\2\2\2\u02a7\u02a5\3\2\2\2\u02a8\u02a9\7n\2\2\u02a9S\3\2\2\2\u02aa"+
		"\u02ab\7R\2\2\u02ab\u02ac\7j\2\2\u02ac\u02b4\5\u0148\u00a5\2\u02ad\u02ae"+
		"\7\65\2\2\u02ae\u02af\7m\2\2\u02af\u02b0\5\u00aeX\2\u02b0\u02b1\5\u017a"+
		"\u00be\2\u02b1\u02b2\7n\2\2\u02b2\u02b4\3\2\2\2\u02b3\u02aa\3\2\2\2\u02b3"+
		"\u02ad\3\2\2\2\u02b4U\3\2\2\2\u02b5\u02b6\7\66\2\2\u02b6\u02bc\7m\2\2"+
		"\u02b7\u02b8\5X-\2\u02b8\u02b9\5\u017a\u00be\2\u02b9\u02bb\3\2\2\2\u02ba"+
		"\u02b7\3\2\2\2\u02bb\u02be\3\2\2\2\u02bc\u02ba\3\2\2\2\u02bc\u02bd\3\2"+
		"\2\2\u02bd\u02bf\3\2\2\2\u02be\u02bc\3\2\2\2\u02bf\u02c0\7n\2\2\u02c0"+
		"W\3\2\2\2\u02c1\u02c2\7j\2\2\u02c2\u02c8\7m\2\2\u02c3\u02c4\5\u016a\u00b6"+
		"\2\u02c4\u02c5\5\u017a\u00be\2\u02c5\u02c7\3\2\2\2\u02c6\u02c3\3\2\2\2"+
		"\u02c7\u02ca\3\2\2\2\u02c8\u02c6\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9\u02cb"+
		"\3\2\2\2\u02ca\u02c8\3\2\2\2\u02cb\u02cc\7n\2\2\u02ccY\3\2\2\2\u02cd\u02ce"+
		"\7\34\2\2\u02ce\u02cf\7o\2\2\u02cf\u02d0\7p\2\2\u02d0\u02d1\5\u013c\u009f"+
		"\2\u02d1[\3\2\2\2\u02d2\u02d3\t\7\2\2\u02d3\u02d4\7o\2\2\u02d4\u02d5\5"+
		"\u00ccg\2\u02d5\u02d6\7p\2\2\u02d6\u02de\3\2\2\2\u02d7\u02d8\7,\2\2\u02d8"+
		"\u02d9\7o\2\2\u02d9\u02da\5\u00ccg\2\u02da\u02db\7p\2\2\u02db\u02dc\5"+
		"\u00ccg\2\u02dc\u02de\3\2\2\2\u02dd\u02d2\3\2\2\2\u02dd\u02d7\3\2\2\2"+
		"\u02de]\3\2\2\2\u02df\u02e5\5`\61\2\u02e0\u02e1\7\20\2\2\u02e1\u02e5\b"+
		"\60\1\2\u02e2\u02e3\7E\2\2\u02e3\u02e5\b\60\1\2\u02e4\u02df\3\2\2\2\u02e4"+
		"\u02e0\3\2\2\2\u02e4\u02e2\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6\u02e8\5\u017a"+
		"\u00be\2\u02e7\u02e4\3\2\2\2\u02e8\u02eb\3\2\2\2\u02e9\u02ea\3\2\2\2\u02e9"+
		"\u02e7\3\2\2\2\u02ea\u02ee\3\2\2\2\u02eb\u02e9\3\2\2\2\u02ec\u02ed\7\20"+
		"\2\2\u02ed\u02ef\b\60\1\2\u02ee\u02ec\3\2\2\2\u02ee\u02ef\3\2\2\2\u02ef"+
		"_\3\2\2\2\u02f0\u02f1\7\13\2\2\u02f1\u02fb\5d\63\2\u02f2\u02f3\7\f\2\2"+
		"\u02f3\u02fb\5d\63\2\u02f4\u02f5\7\r\2\2\u02f5\u02fb\5d\63\2\u02f6\u02f7"+
		"\7\17\2\2\u02f7\u02fb\5b\62\2\u02f8\u02f9\7B\2\2\u02f9\u02fb\5f\64\2\u02fa"+
		"\u02f0\3\2\2\2\u02fa\u02f2\3\2\2\2\u02fa\u02f4\3\2\2\2\u02fa\u02f6\3\2"+
		"\2\2\u02fa\u02f8\3\2\2\2\u02fba\3\2\2\2\u02fc\u02fe\5\u00f0y\2\u02fd\u02fc"+
		"\3\2\2\2\u02fd\u02fe\3\2\2\2\u02fe\u0301\3\2\2\2\u02ff\u0300\7a\2\2\u0300"+
		"\u0302\5\u00aeX\2\u0301\u02ff\3\2\2\2\u0301\u0302\3\2\2\2\u0302c\3\2\2"+
		"\2\u0303\u0306\3\2\2\2\u0304\u0306\5\u00aeX\2\u0305\u0303\3\2\2\2\u0305"+
		"\u0304\3\2\2\2\u0306e\3\2\2\2\u0307\u0313\7m\2\2\u0308\u0309\5`\61\2\u0309"+
		"\u030a\5\u017a\u00be\2\u030a\u030c\3\2\2\2\u030b\u0308\3\2\2\2\u030c\u030f"+
		"\3\2\2\2\u030d\u030e\3\2\2\2\u030d\u030b\3\2\2\2\u030e\u0311\3\2\2\2\u030f"+
		"\u030d\3\2\2\2\u0310\u0312\5h\65\2\u0311\u0310\3\2\2\2\u0311\u0312\3\2"+
		"\2\2\u0312\u0314\3\2\2\2\u0313\u030d\3\2\2\2\u0313\u0314\3\2\2\2\u0314"+
		"\u0315\3\2\2\2\u0315\u0316\7n\2\2\u0316g\3\2\2\2\u0317\u0318\7I\2\2\u0318"+
		"\u0319\5\u00f8}\2\u0319\u031a\5\u017a\u00be\2\u031ai\3\2\2\2\u031b\u031c"+
		"\7\67\2\2\u031c\u031d\5\u00aeX\2\u031d\u0321\7m\2\2\u031e\u0320\5l\67"+
		"\2\u031f\u031e\3\2\2\2\u0320\u0323\3\2\2\2\u0321\u031f\3\2\2\2\u0321\u0322"+
		"\3\2\2\2\u0322\u0324\3\2\2\2\u0323\u0321\3\2\2\2\u0324\u0325\7n\2\2\u0325"+
		"k\3\2\2\2\u0326\u0327\5n8\2\u0327\u0329\7t\2\2\u0328\u032a\5\u00fa~\2"+
		"\u0329\u0328\3\2\2\2\u0329\u032a\3\2\2\2\u032am\3\2\2\2\u032b\u032c\7"+
		"U\2\2\u032c\u032f\5p9\2\u032d\u032f\7Q\2\2\u032e\u032b\3\2\2\2\u032e\u032d"+
		"\3\2\2\2\u032fo\3\2\2\2\u0330\u0331\7&\2\2\u0331\u033e\7j\2\2\u0332\u0333"+
		"\5\u00d4k\2\u0333\u0338\7m\2\2\u0334\u0336\5r:\2\u0335\u0337\7r\2\2\u0336"+
		"\u0335\3\2\2\2\u0336\u0337\3\2\2\2\u0337\u0339\3\2\2\2\u0338\u0334\3\2"+
		"\2\2\u0338\u0339\3\2\2\2\u0339\u033a\3\2\2\2\u033a\u033b\7n\2\2\u033b"+
		"\u033e\3\2\2\2\u033c\u033e\5\u00aeX\2\u033d\u0330\3\2\2\2\u033d\u0332"+
		"\3\2\2\2\u033d\u033c\3\2\2\2\u033eq\3\2\2\2\u033f\u0344\5p9\2\u0340\u0341"+
		"\7r\2\2\u0341\u0343\5p9\2\u0342\u0340\3\2\2\2\u0343\u0346\3\2\2\2\u0344"+
		"\u0342\3\2\2\2\u0344\u0345\3\2\2\2\u0345s\3\2\2\2\u0346\u0344\3\2\2\2"+
		"\u0347\u034c\7m\2\2\u0348\u0349\7<\2\2\u0349\u034a\5\u00eex\2\u034a\u034b"+
		"\5\u017a\u00be\2\u034b\u034d\3\2\2\2\u034c\u0348\3\2\2\2\u034c\u034d\3"+
		"\2\2\2\u034d\u034f\3\2\2\2\u034e\u0350\5\u00fa~\2\u034f\u034e\3\2\2\2"+
		"\u034f\u0350\3\2\2\2\u0350\u0351\3\2\2\2\u0351\u0352\7n\2\2\u0352u\3\2"+
		"\2\2\u0353\u0356\5\u015a\u00ae\2\u0354\u0356\7j\2\2\u0355\u0353\3\2\2"+
		"\2\u0355\u0354\3\2\2\2\u0356\u035f\3\2\2\2\u0357\u035c\7m\2\2\u0358\u035a"+
		"\5x=\2\u0359\u035b\7r\2\2\u035a\u0359\3\2\2\2\u035a\u035b\3\2\2\2\u035b"+
		"\u035d\3\2\2\2\u035c\u0358\3\2\2\2\u035c\u035d\3\2\2\2\u035d\u035e\3\2"+
		"\2\2\u035e\u0360\7n\2\2\u035f\u0357\3\2\2\2\u035f\u0360\3\2\2\2\u0360"+
		"w\3\2\2\2\u0361\u0366\5z>\2\u0362\u0363\7r\2\2\u0363\u0365\5z>\2\u0364"+
		"\u0362\3\2\2\2\u0365\u0368\3\2\2\2\u0366\u0364\3\2\2\2\u0366\u0367\3\2"+
		"\2\2\u0367y\3\2\2\2\u0368\u0366\3\2\2\2\u0369\u036a\7j\2\2\u036a\u036c"+
		"\7t\2\2\u036b\u0369\3\2\2\2\u036b\u036c\3\2\2\2\u036c\u036d\3\2\2\2\u036d"+
		"\u036e\5\u00aeX\2\u036e{\3\2\2\2\u036f\u0370\7I\2\2\u0370\u0371\5\u00ae"+
		"X\2\u0371\u0372\7\21\2\2\u0372\u0373\5v<\2\u0373\u0374\5\u00f8}\2\u0374"+
		"}\3\2\2\2\u0375\u0376\5\u00ccg\2\u0376\u0377\7\21\2\2\u0377\u038a\5\u00cc"+
		"g\2\u0378\u037e\7m\2\2\u0379\u037a\5\u0086D\2\u037a\u037b\5\u017a\u00be"+
		"\2\u037b\u037d\3\2\2\2\u037c\u0379\3\2\2\2\u037d\u0380\3\2\2\2\u037e\u037c"+
		"\3\2\2\2\u037e\u037f\3\2\2\2\u037f\u0386\3\2\2\2\u0380\u037e\3\2\2\2\u0381"+
		"\u0382\5\u0080A\2\u0382\u0383\5\u017a\u00be\2\u0383\u0385\3\2\2\2\u0384"+
		"\u0381\3\2\2\2\u0385\u0388\3\2\2\2\u0386\u0384\3\2\2\2\u0386\u0387\3\2"+
		"\2\2\u0387\u0389\3\2\2\2\u0388\u0386\3\2\2\2\u0389\u038b\7n\2\2\u038a"+
		"\u0378\3\2\2\2\u038a\u038b\3\2\2\2\u038b\177\3\2\2\2\u038c\u038e\7\20"+
		"\2\2\u038d\u038c\3\2\2\2\u038d\u038e\3\2\2\2\u038e\u038f\3\2\2\2\u038f"+
		"\u0390\5\u0082B\2\u0390\u0391\7j\2\2\u0391\u0393\5\u0148\u00a5\2\u0392"+
		"\u0394\5\u00f8}\2\u0393\u0392\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0081"+
		"\3\2\2\2\u0395\u0397\7k\2\2\u0396\u0398\7j\2\2\u0397\u0396\3\2\2\2\u0397"+
		"\u0398\3\2\2\2\u0398\u039a\3\2\2\2\u0399\u039b\7\u008c\2\2\u039a\u0399"+
		"\3\2\2\2\u039a\u039b\3\2\2\2\u039b\u039c\3\2\2\2\u039c\u039d\5\u0136\u009c"+
		"\2\u039d\u039e\7l\2\2\u039e\u0083\3\2\2\2\u039f\u03a5\5\u00be`\2\u03a0"+
		"\u03a1\5\u00ccg\2\u03a1\u03a2\7u\2\2\u03a2\u03a3\7j\2\2\u03a3\u03a5\3"+
		"\2\2\2\u03a4\u039f\3\2\2\2\u03a4\u03a0\3\2\2\2\u03a5\u0085\3\2\2\2\u03a6"+
		"\u03a7\79\2\2\u03a7\u03a8\7j\2\2\u03a8\u03ab\7x\2\2\u03a9\u03ac\5\u0084"+
		"C\2\u03aa\u03ac\5\u0158\u00ad\2\u03ab\u03a9\3\2\2\2\u03ab\u03aa\3\2\2"+
		"\2\u03ac\u0087\3\2\2\2\u03ad\u03ae\7\60\2\2\u03ae\u03af\7k\2\2\u03af\u03b2"+
		"\5\u00ccg\2\u03b0\u03b1\7r\2\2\u03b1\u03b3\5\u00f0y\2\u03b2\u03b0\3\2"+
		"\2\2\u03b2\u03b3\3\2\2\2\u03b3\u03b4\3\2\2\2\u03b4\u03b5\7l\2\2\u03b5"+
		"\u0089\3\2\2\2\u03b6\u03b7\7/\2\2\u03b7\u03b8\7k\2\2\u03b8\u03b9\5\u00cc"+
		"g\2\u03b9\u03ba\7l\2\2\u03ba\u008b\3\2\2\2\u03bb\u03c1\5^\60\2\u03bc\u03c2"+
		"\5\u008eH\2\u03bd\u03c2\5\u0090I\2\u03be\u03c2\5\u009aN\2\u03bf\u03c2"+
		"\5\u009cO\2\u03c0\u03c2\5\u009eP\2\u03c1\u03bc\3\2\2\2\u03c1\u03bd\3\2"+
		"\2\2\u03c1\u03be\3\2\2\2\u03c1\u03bf\3\2\2\2\u03c1\u03c0\3\2\2\2\u03c2"+
		"\u008d\3\2\2\2\u03c3\u03c4\7R\2\2\u03c4\u03c5\7j\2\2\u03c5\u03c7\5\u0148"+
		"\u00a5\2\u03c6\u03c8\5t;\2\u03c7\u03c6\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8"+
		"\u008f\3\2\2\2\u03c9\u03ca\7R\2\2\u03ca\u03cb\5\u00a4S\2\u03cb\u03cc\7"+
		"j\2\2\u03cc\u03ce\5\u0148\u00a5\2\u03cd\u03cf\5t;\2\u03ce\u03cd\3\2\2"+
		"\2\u03ce\u03cf\3\2\2\2\u03cf\u0091\3\2\2\2\u03d0\u03d3\7\34\2\2\u03d1"+
		"\u03d4\5\u008cG\2\u03d2\u03d4\5\u00e8u\2\u03d3\u03d1\3\2\2\2\u03d3\u03d2"+
		"\3\2\2\2\u03d4\u0093\3\2\2\2\u03d5\u03d6\79\2\2\u03d6\u03d7\7j\2\2\u03d7"+
		"\u03d9\5\u014c\u00a7\2\u03d8\u03da\5\u0096L\2\u03d9\u03d8\3\2\2\2\u03d9"+
		"\u03da\3\2\2\2\u03da\u0095\3\2\2\2\u03db\u03dc\7m\2\2\u03dc\u03dd\5\u00ae"+
		"X\2\u03dd\u03de\5\u017a\u00be\2\u03de\u03df\7n\2\2\u03df\u0097\3\2\2\2"+
		"\u03e0\u03e1\79\2\2\u03e1\u03e2\5\u00a4S\2\u03e2\u03e3\7j\2\2\u03e3\u03e5"+
		"\5\u014c\u00a7\2\u03e4\u03e6\5\u0096L\2\u03e5\u03e4\3\2\2\2\u03e5\u03e6"+
		"\3\2\2\2\u03e6\u0099\3\2\2\2\u03e7\u03e9\7M\2\2\u03e8\u03ea\7\u008d\2"+
		"\2\u03e9\u03e8\3\2\2\2\u03e9\u03ea\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ec"+
		"\5\u00ccg\2\u03ec\u03ee\5\u0148\u00a5\2\u03ed\u03ef\5t;\2\u03ee\u03ed"+
		"\3\2\2\2\u03ee\u03ef\3\2\2\2\u03ef\u009b\3\2\2\2\u03f0\u03f2\7N\2\2\u03f1"+
		"\u03f3\7\u008d\2\2\u03f2\u03f1\3\2\2\2\u03f2\u03f3\3\2\2\2\u03f3\u03f4"+
		"\3\2\2\2\u03f4\u03f5\5\u00ccg\2\u03f5\u03f7\5\u0148\u00a5\2\u03f6\u03f8"+
		"\5t;\2\u03f7\u03f6\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u009d\3\2\2\2\u03f9"+
		"\u03fb\7O\2\2\u03fa\u03fc\7\u008d\2\2\u03fb\u03fa\3\2\2\2\u03fb\u03fc"+
		"\3\2\2\2\u03fc\u03fd\3\2\2\2\u03fd\u03fe\5\u00ccg\2\u03fe\u0400\5\u0148"+
		"\u00a5\2\u03ff\u0401\5t;\2\u0400\u03ff\3\2\2\2\u0400\u0401\3\2\2\2\u0401"+
		"\u009f\3\2\2\2\u0402\u040a\5\b\5\2\u0403\u0406\5\u00ccg\2\u0404\u0405"+
		"\7q\2\2\u0405\u0407\5\u00f0y\2\u0406\u0404\3\2\2\2\u0406\u0407\3\2\2\2"+
		"\u0407\u040b\3\2\2\2\u0408\u0409\7q\2\2\u0409\u040b\5\u00f0y\2\u040a\u0403"+
		"\3\2\2\2\u040a\u0408\3\2\2\2\u040b\u00a1\3\2\2\2\u040c\u040d\5\b\5\2\u040d"+
		"\u040e\7x\2\2\u040e\u040f\5\u00f0y\2\u040f\u00a3\3\2\2\2\u0410\u0412\7"+
		"k\2\2\u0411\u0413\5\n\6\2\u0412\u0411\3\2\2\2\u0412\u0413\3\2\2\2\u0413"+
		"\u0414\3\2\2\2\u0414\u0416\5\u00ccg\2\u0415\u0417\7r\2\2\u0416\u0415\3"+
		"\2\2\2\u0416\u0417\3\2\2\2\u0417\u0418\3\2\2\2\u0418\u0419\7l\2\2\u0419"+
		"\u00a5\3\2\2\2\u041a\u041d\5\u00a8U\2\u041b\u041d\5\u00aaV\2\u041c\u041a"+
		"\3\2\2\2\u041c\u041b\3\2\2\2\u041d\u00a7\3\2\2\2\u041e\u0420\5\u00eex"+
		"\2\u041f\u041e\3\2\2\2\u041f\u0420\3\2\2\2\u0420\u0421\3\2\2\2\u0421\u0422"+
		"\5\u00acW\2\u0422\u00a9\3\2\2\2\u0423\u0425\7\34\2\2\u0424\u0426\5\u00ee"+
		"x\2\u0425\u0424\3\2\2\2\u0425\u0426\3\2\2\2\u0426\u0427\3\2\2\2\u0427"+
		"\u0428\5\u00acW\2\u0428\u00ab\3\2\2\2\u0429\u042b\7y\2\2\u042a\u0429\3"+
		"\2\2\2\u042a\u042b\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u042d\5\u00ccg\2"+
		"\u042d\u00ad\3\2\2\2\u042e\u042f\bX\1\2\u042f\u0430\t\b\2\2\u0430\u043f"+
		"\5\u00aeX\20\u0431\u043f\5\u00be`\2\u0432\u0433\7\33\2\2\u0433\u0434\5"+
		".\30\2\u0434\u0435\7\35\2\2\u0435\u0436\5\u00aeX\4\u0436\u043f\3\2\2\2"+
		"\u0437\u0438\t\t\2\2\u0438\u0439\5&\24\2\u0439\u043a\7t\2\2\u043a\u043b"+
		"\7t\2\2\u043b\u043c\5*\26\2\u043c\u043d\5\u00aeX\3\u043d\u043f\3\2\2\2"+
		"\u043e\u042e\3\2\2\2\u043e\u0431\3\2\2\2\u043e\u0432\3\2\2\2\u043e\u0437"+
		"\3\2\2\2\u043f\u0463\3\2\2\2\u0440\u0441\f\16\2\2\u0441\u0442\t\n\2\2"+
		"\u0442\u0462\5\u00aeX\17\u0443\u0444\f\r\2\2\u0444\u0445\t\13\2\2\u0445"+
		"\u0462\5\u00aeX\16\u0446\u0447\f\f\2\2\u0447\u0448\t\f\2\2\u0448\u0462"+
		"\5\u00aeX\r\u0449\u044a\f\13\2\2\u044a\u044b\t\r\2\2\u044b\u0462\5\u00ae"+
		"X\f\u044c\u044d\f\n\2\2\u044d\u044e\t\16\2\2\u044e\u0462\5\u00aeX\13\u044f"+
		"\u0450\f\b\2\2\u0450\u0451\7{\2\2\u0451\u0462\5\u00aeX\t\u0452\u0453\f"+
		"\7\2\2\u0453\u0454\7z\2\2\u0454\u0462\5\u00aeX\b\u0455\u0456\f\6\2\2\u0456"+
		"\u0457\7#\2\2\u0457\u0462\5\u00aeX\6\u0458\u0459\f\5\2\2\u0459\u045a\7"+
		"&\2\2\u045a\u045b\5\u00aeX\2\u045b\u045c\7t\2\2\u045c\u045d\5\u00aeX\5"+
		"\u045d\u0462\3\2\2\2\u045e\u045f\f\t\2\2\u045f\u0460\7\21\2\2\u0460\u0462"+
		"\5v<\2\u0461\u0440\3\2\2\2\u0461\u0443\3\2\2\2\u0461\u0446\3\2\2\2\u0461"+
		"\u0449\3\2\2\2\u0461\u044c\3\2\2\2\u0461\u044f\3\2\2\2\u0461\u0452\3\2"+
		"\2\2\u0461\u0455\3\2\2\2\u0461\u0458\3\2\2\2\u0461\u045e\3\2\2\2\u0462"+
		"\u0465\3\2\2\2\u0463\u0461\3\2\2\2\u0463\u0464\3\2\2\2\u0464\u00af\3\2"+
		"\2\2\u0465\u0463\3\2\2\2\u0466\u047b\5\30\r\2\u0467\u047b\5\32\16\2\u0468"+
		"\u047b\5\u00b4[\2\u0469\u047b\5\u00b2Z\2\u046a\u047b\5\u00e8u\2\u046b"+
		"\u047b\5\u0108\u0085\2\u046c\u047b\5\u00fc\177\2\u046d\u047b\5\u0134\u009b"+
		"\2\u046e\u047b\5\u010a\u0086\2\u046f\u047b\5\u010c\u0087\2\u0470\u047b"+
		"\5\u010e\u0088\2\u0471\u047b\5\u0110\u0089\2\u0472\u047b\5\u0112\u008a"+
		"\2\u0473\u047b\5\u00f8}\2\u0474\u047b\5\u0114\u008b\2\u0475\u047b\5\u0116"+
		"\u008c\2\u0476\u047b\5\u0128\u0095\2\u0477\u047b\5\u00b6\\\2\u0478\u047b"+
		"\5\u00ba^\2\u0479\u047b\5|?\2\u047a\u0466\3\2\2\2\u047a\u0467\3\2\2\2"+
		"\u047a\u0468\3\2\2\2\u047a\u0469\3\2\2\2\u047a\u046a\3\2\2\2\u047a\u046b"+
		"\3\2\2\2\u047a\u046c\3\2\2\2\u047a\u046d\3\2\2\2\u047a\u046e\3\2\2\2\u047a"+
		"\u046f\3\2\2\2\u047a\u0470\3\2\2\2\u047a\u0471\3\2\2\2\u047a\u0472\3\2"+
		"\2\2\u047a\u0473\3\2\2\2\u047a\u0474\3\2\2\2\u047a\u0475\3\2\2\2\u047a"+
		"\u0476\3\2\2\2\u047a\u0477\3\2\2\2\u047a\u0478\3\2\2\2\u047a\u0479\3\2"+
		"\2\2\u047b\u00b1\3\2\2\2\u047c\u047d\7%\2\2\u047d\u047e\5\u00aeX\2\u047e"+
		"\u00b3\3\2\2\2\u047f\u0480\7]\2\2\u0480\u0482\5\u00aeX\2\u0481\u0483\5"+
		"\u00f8}\2\u0482\u0481\3\2\2\2\u0482\u0483\3\2\2\2\u0483\u00b5\3\2\2\2"+
		"\u0484\u0485\5\u00b8]\2\u0485\u0486\5\u0130\u0099\2\u0486\u00b7\3\2\2"+
		"\2\u0487\u0488\7\16\2\2\u0488\u0489\5\u00aeX\2\u0489\u048a\5\u017a\u00be"+
		"\2\u048a\u048c\3\2\2\2\u048b\u0487\3\2\2\2\u048c\u048f\3\2\2\2\u048d\u048b"+
		"\3\2\2\2\u048d\u048e\3\2\2\2\u048e\u0494\3\2\2\2\u048f\u048d\3\2\2\2\u0490"+
		"\u0491\7\17\2\2\u0491\u0492\5b\62\2\u0492\u0493\5\u017a\u00be\2\u0493"+
		"\u0495\3\2\2\2\u0494\u0490\3\2\2\2\u0494\u0495\3\2\2\2\u0495\u00b9\3\2"+
		"\2\2\u0496\u0497\7V\2\2\u0497\u049c\5\u00aeX\2\u0498\u0499\7V\2\2\u0499"+
		"\u049a\t\3\2\2\u049a\u049c\5.\30\2\u049b\u0496\3\2\2\2\u049b\u0498\3\2"+
		"\2\2\u049c\u00bb\3\2\2\2\u049d\u04a6\7\5\2\2\u049e\u04a6\7\6\2\2\u049f"+
		"\u04a6\7i\2\2\u04a0\u04a6\5\u0156\u00ac\2\u04a1\u04a6\5\u016c\u00b7\2"+
		"\u04a2\u04a6\7\3\2\2\u04a3\u04a6\7\u0094\2\2\u04a4\u04a6\7\u0095\2\2\u04a5"+
		"\u049d\3\2\2\2\u04a5\u049e\3\2\2\2\u04a5\u049f\3\2\2\2\u04a5\u04a0\3\2"+
		"\2\2\u04a5\u04a1\3\2\2\2\u04a5\u04a2\3\2\2\2\u04a5\u04a3\3\2\2\2\u04a5"+
		"\u04a4\3\2\2\2\u04a6\u00bd\3\2\2\2\u04a7\u04a8\b`\1\2\u04a8\u04b4\5\u0152"+
		"\u00aa\2\u04a9\u04b4\5\u014e\u00a8\2\u04aa\u04b4\5\u0176\u00bc\2\u04ab"+
		"\u04b4\5 \21\2\u04ac\u04b4\5\u008aF\2\u04ad\u04b4\5\u0088E\2\u04ae\u04af"+
		"\t\17\2\2\u04af\u04b0\7k\2\2\u04b0\u04b1\5\u00aeX\2\u04b1\u04b2\7l\2\2"+
		"\u04b2\u04b4\3\2\2\2\u04b3\u04a7\3\2\2\2\u04b3\u04a9\3\2\2\2\u04b3\u04aa"+
		"\3\2\2\2\u04b3\u04ab\3\2\2\2\u04b3\u04ac\3\2\2\2\u04b3\u04ad\3\2\2\2\u04b3"+
		"\u04ae\3\2\2\2\u04b4\u04cb\3\2\2\2\u04b5\u04b6\f\13\2\2\u04b6\u04b7\7"+
		"u\2\2\u04b7\u04ca\7j\2\2\u04b8\u04b9\f\n\2\2\u04b9\u04ca\5\u0170\u00b9"+
		"\2\u04ba\u04bb\f\t\2\2\u04bb\u04ca\5\u00d8m\2\u04bc\u04bd\f\b\2\2\u04bd"+
		"\u04ca\5L\'\2\u04be\u04bf\f\7\2\2\u04bf\u04ca\5\u0172\u00ba\2\u04c0\u04c1"+
		"\f\6\2\2\u04c1\u04ca\5\u0174\u00bb\2\u04c2\u04c3\f\5\2\2\u04c3\u04c4\5"+
		"\u0174\u00bb\2\u04c4\u04c5\7\22\2\2\u04c5\u04c6\5v<\2\u04c6\u04ca\3\2"+
		"\2\2\u04c7\u04c8\f\4\2\2\u04c8\u04ca\5\u00c4c\2\u04c9\u04b5\3\2\2\2\u04c9"+
		"\u04b8\3\2\2\2\u04c9\u04ba\3\2\2\2\u04c9\u04bc\3\2\2\2\u04c9\u04be\3\2"+
		"\2\2\u04c9\u04c0\3\2\2\2\u04c9\u04c2\3\2\2\2\u04c9\u04c7\3\2\2\2\u04ca"+
		"\u04cd\3\2\2\2\u04cb\u04c9\3\2\2\2\u04cb\u04cc\3\2\2\2\u04cc\u00bf\3\2"+
		"\2\2\u04cd\u04cb\3\2\2\2\u04ce\u04cf\5^\60\2\u04cf\u04d0\5\u00c2b\2\u04d0"+
		"\u00c1\3\2\2\2\u04d1\u04d3\7R\2\2\u04d2\u04d4\7j\2\2\u04d3\u04d2\3\2\2"+
		"\2\u04d3\u04d4\3\2\2\2\u04d4\u04d5\3\2\2\2\u04d5\u04d7\5\u0148\u00a5\2"+
		"\u04d6\u04d8\5t;\2\u04d7\u04d6\3\2\2\2\u04d7\u04d8\3\2\2\2\u04d8\u00c3"+
		"\3\2\2\2\u04d9\u04db\7\'\2\2\u04da\u04dc\5\u00f0y\2\u04db\u04da\3\2\2"+
		"\2\u04db\u04dc\3\2\2\2\u04dc\u04de\3\2\2\2\u04dd\u04df\7r\2\2\u04de\u04dd"+
		"\3\2\2\2\u04de\u04df\3\2\2\2\u04df\u04e0\3\2\2\2\u04e0\u04e1\7(\2\2\u04e1"+
		"\u00c5\3\2\2\2\u04e2\u04e3\7S\2\2\u04e3\u04ed\7m\2\2\u04e4\u04e8\5\u00ca"+
		"f\2\u04e5\u04e8\5\u0136\u009c\2\u04e6\u04e8\5\u00c8e\2\u04e7\u04e4\3\2"+
		"\2\2\u04e7\u04e5\3\2\2\2\u04e7\u04e6\3\2\2\2\u04e8\u04e9\3\2\2\2\u04e9"+
		"\u04ea\5\u017a\u00be\2\u04ea\u04ec\3\2\2\2\u04eb\u04e7\3\2\2\2\u04ec\u04ef"+
		"\3\2\2\2\u04ed\u04eb\3\2\2\2\u04ed\u04ee\3\2\2\2\u04ee\u04f0\3\2\2\2\u04ef"+
		"\u04ed\3\2\2\2\u04f0\u04f1\7n\2\2\u04f1\u00c7\3\2\2\2\u04f2\u04f3\79\2"+
		"\2\u04f3\u04f4\7j\2\2\u04f4\u04f5\5\u014c\u00a7\2\u04f5\u00c9\3\2\2\2"+
		"\u04f6\u04f8\7\34\2\2\u04f7\u04f6\3\2\2\2\u04f7\u04f8\3\2\2\2\u04f8\u04f9"+
		"\3\2\2\2\u04f9\u04fa\5^\60\2\u04fa\u04fb\7j\2\2\u04fb\u04fc\5\u014c\u00a7"+
		"\2\u04fc\u04fd\5\u014a\u00a6\2\u04fd\u0506\3\2\2\2\u04fe\u0500\7\34\2"+
		"\2\u04ff\u04fe\3\2\2\2\u04ff\u0500\3\2\2\2\u0500\u0501\3\2\2\2\u0501\u0502"+
		"\5^\60\2\u0502\u0503\7j\2\2\u0503\u0504\5\u014c\u00a7\2\u0504\u0506\3"+
		"\2\2\2\u0505\u04f7\3\2\2\2\u0505\u04ff\3\2\2\2\u0506\u00cb\3\2\2\2\u0507"+
		"\u050f\5\u0136\u009c\2\u0508\u050f\5\u00ceh\2\u0509\u050f\5P)\2\u050a"+
		"\u050b\7k\2\2\u050b\u050c\5\u00ccg\2\u050c\u050d\7l\2\2\u050d\u050f\3"+
		"\2\2\2\u050e\u0507\3\2\2\2\u050e\u0508\3\2\2\2\u050e\u0509\3\2\2\2\u050e"+
		"\u050a\3\2\2\2\u050f\u00cd\3\2\2\2\u0510\u051a\5\u0138\u009d\2\u0511\u051a"+
		"\5\u0168\u00b5\2\u0512\u051a\5\u013e\u00a0\2\u0513\u051a\5\u0146\u00a4"+
		"\2\u0514\u051a\5\u00c6d\2\u0515\u051a\5\u0140\u00a1\2\u0516\u051a\5\u0142"+
		"\u00a2\2\u0517\u051a\5\u0144\u00a3\2\u0518\u051a\5\u00d0i\2\u0519\u0510"+
		"\3\2\2\2\u0519\u0511\3\2\2\2\u0519\u0512\3\2\2\2\u0519\u0513\3\2\2\2\u0519"+
		"\u0514\3\2\2\2\u0519\u0515\3\2\2\2\u0519\u0516\3\2\2\2\u0519\u0517\3\2"+
		"\2\2\u0519\u0518\3\2\2\2\u051a\u00cf\3\2\2\2\u051b\u051c\79\2\2\u051c"+
		"\u051d\5\u00d2j\2\u051d\u00d1\3\2\2\2\u051e\u052a\7k\2\2\u051f\u0524\5"+
		"\u00ccg\2\u0520\u0521\7r\2\2\u0521\u0523\5\u00ccg\2\u0522\u0520\3\2\2"+
		"\2\u0523\u0526\3\2\2\2\u0524\u0522\3\2\2\2\u0524\u0525\3\2\2\2\u0525\u0528"+
		"\3\2\2\2\u0526\u0524\3\2\2\2\u0527\u0529\7r\2\2\u0528\u0527\3\2\2\2\u0528"+
		"\u0529\3\2\2\2\u0529\u052b\3\2\2\2\u052a\u051f\3\2\2\2\u052a\u052b\3\2"+
		"\2\2\u052b\u052c\3\2\2\2\u052c\u052d\7l\2\2\u052d\u00d3\3\2\2\2\u052e"+
		"\u0536\5\u0168\u00b5\2\u052f\u0536\5\u0138\u009d\2\u0530\u0536\5\u00d6"+
		"l\2\u0531\u0536\5\u0140\u00a1\2\u0532\u0536\5\u0142\u00a2\2\u0533\u0536"+
		"\5P)\2\u0534\u0536\5\u0136\u009c\2\u0535\u052e\3\2\2\2\u0535\u052f\3\2"+
		"\2\2\u0535\u0530\3\2\2\2\u0535\u0531\3\2\2\2\u0535\u0532\3\2\2\2\u0535"+
		"\u0533\3\2\2\2\u0535\u0534\3\2\2\2\u0536\u00d5\3\2\2\2\u0537\u0538\7o"+
		"\2\2\u0538\u0539\7y\2\2\u0539\u053a\7p\2\2\u053a\u053b\5\u013c\u009f\2"+
		"\u053b\u00d7\3\2\2\2\u053c\u054c\7o\2\2\u053d\u053f\5\u00dan\2\u053e\u053d"+
		"\3\2\2\2\u053e\u053f\3\2\2\2\u053f\u0540\3\2\2\2\u0540\u0542\7t\2\2\u0541"+
		"\u0543\5\u00dco\2\u0542\u0541\3\2\2\2\u0542\u0543\3\2\2\2\u0543\u054d"+
		"\3\2\2\2\u0544\u0546\5\u00dan\2\u0545\u0544\3\2\2\2\u0545\u0546\3\2\2"+
		"\2\u0546\u0547\3\2\2\2\u0547\u0548\7t\2\2\u0548\u0549\5\u00dco\2\u0549"+
		"\u054a\7t\2\2\u054a\u054b\5\u00dep\2\u054b\u054d\3\2\2\2\u054c\u053e\3"+
		"\2\2\2\u054c\u0545\3\2\2\2\u054d\u054e\3\2\2\2\u054e\u054f\7p\2\2\u054f"+
		"\u00d9\3\2\2\2\u0550\u0551\5\u00aeX\2\u0551\u00db\3\2\2\2\u0552\u0553"+
		"\5\u00aeX\2\u0553\u00dd\3\2\2\2\u0554\u0555\5\u00aeX\2\u0555\u00df\3\2"+
		"\2\2\u0556\u0558\t\20\2\2\u0557\u0556\3\2\2\2\u0557\u0558\3\2\2\2\u0558"+
		"\u0559\3\2\2\2\u0559\u055a\7q\2\2\u055a\u00e1\3\2\2\2\u055b\u055c\5\u00f0"+
		"y\2\u055c\u055d\7q\2\2\u055d\u0562\3\2\2\2\u055e\u055f\5\b\5\2\u055f\u0560"+
		"\7x\2\2\u0560\u0562\3\2\2\2\u0561\u055b\3\2\2\2\u0561\u055e\3\2\2\2\u0561"+
		"\u0562\3\2\2\2\u0562\u0563\3\2\2\2\u0563\u0564\7b\2\2\u0564\u0569\5\u00ae"+
		"X\2\u0565\u0567\7L\2\2\u0566\u0568\7j\2\2\u0567\u0566\3\2\2\2\u0567\u0568"+
		"\3\2\2\2\u0568\u056a\3\2\2\2\u0569\u0565\3\2\2\2\u0569\u056a\3\2\2\2\u056a"+
		"\u00e3\3\2\2\2\u056b\u056c\7]\2\2\u056c\u056d\7j\2\2\u056d\u00e5\3\2\2"+
		"\2\u056e\u056f\5\u016c\u00b7\2\u056f\u00e7\3\2\2\2\u0570\u0574\5\u00ea"+
		"v\2\u0571\u0574\5\u00f2z\2\u0572\u0574\5\u00f6|\2\u0573\u0570\3\2\2\2"+
		"\u0573\u0571\3\2\2\2\u0573\u0572\3\2\2\2\u0574\u00e9\3\2\2\2\u0575\u0581"+
		"\7_\2\2\u0576\u0582\5\u00ecw\2\u0577\u057d\7k\2\2\u0578\u0579\5\u00ec"+
		"w\2\u0579\u057a\5\u017a\u00be\2\u057a\u057c\3\2\2\2\u057b\u0578\3\2\2"+
		"\2\u057c\u057f\3\2\2\2\u057d\u057b\3\2\2\2\u057d\u057e\3\2\2\2\u057e\u0580"+
		"\3\2\2\2\u057f\u057d\3\2\2\2\u0580\u0582\7l\2\2\u0581\u0576\3\2\2\2\u0581"+
		"\u0577\3\2\2\2\u0582\u00eb\3\2\2\2\u0583\u0589\5\u00eex\2\u0584\u0586"+
		"\5\u00ccg\2\u0585\u0584\3\2\2\2\u0585\u0586\3\2\2\2\u0586\u0587\3\2\2"+
		"\2\u0587\u0588\7q\2\2\u0588\u058a\5\u00f0y\2\u0589\u0585\3\2\2\2\u0589"+
		"\u058a\3\2\2\2\u058a\u00ed\3\2\2\2\u058b\u0590\7j\2\2\u058c\u058d\7r\2"+
		"\2\u058d\u058f\7j\2\2\u058e\u058c\3\2\2\2\u058f\u0592\3\2\2\2\u0590\u058e"+
		"\3\2\2\2\u0590\u0591\3\2\2\2\u0591\u00ef\3\2\2\2\u0592\u0590\3\2\2\2\u0593"+
		"\u0598\5\u00aeX\2\u0594\u0595\7r\2\2\u0595\u0597\5\u00aeX\2\u0596\u0594"+
		"\3\2\2\2\u0597\u059a\3\2\2\2\u0598\u0596\3\2\2\2\u0598\u0599\3\2\2\2\u0599"+
		"\u00f1\3\2\2\2\u059a\u0598\3\2\2\2\u059b\u05a7\7c\2\2\u059c\u05a8\5\u00f4"+
		"{\2\u059d\u05a3\7k\2\2\u059e\u059f\5\u00f4{\2\u059f\u05a0\5\u017a\u00be"+
		"\2\u05a0\u05a2\3\2\2\2\u05a1\u059e\3\2\2\2\u05a2\u05a5\3\2\2\2\u05a3\u05a1"+
		"\3\2\2\2\u05a3\u05a4\3\2\2\2\u05a4\u05a6\3\2\2\2\u05a5\u05a3\3\2\2\2\u05a6"+
		"\u05a8\7l\2\2\u05a7\u059c\3\2\2\2\u05a7\u059d\3\2\2\2\u05a8\u00f3\3\2"+
		"\2\2\u05a9\u05ab\7j\2\2\u05aa\u05ac\7q\2\2\u05ab\u05aa\3\2\2\2\u05ab\u05ac"+
		"\3\2\2\2\u05ac\u05ad\3\2\2\2\u05ad\u05ae\5\u00ccg\2\u05ae\u00f5\3\2\2"+
		"\2\u05af\u05bb\7h\2\2\u05b0\u05bc\5\u00a0Q\2\u05b1\u05b7\7k\2\2\u05b2"+
		"\u05b3\5\u00a0Q\2\u05b3\u05b4\5\u017a\u00be\2\u05b4\u05b6\3\2\2\2\u05b5"+
		"\u05b2\3\2\2\2\u05b6\u05b9\3\2\2\2\u05b7\u05b5\3\2\2\2\u05b7\u05b8\3\2"+
		"\2\2\u05b8\u05ba\3\2\2\2\u05b9\u05b7\3\2\2\2\u05ba\u05bc\7l\2\2\u05bb"+
		"\u05b0\3\2\2\2\u05bb\u05b1\3\2\2\2\u05bc\u00f7\3\2\2\2\u05bd\u05bf\7m"+
		"\2\2\u05be\u05c0\5\u00fa~\2\u05bf\u05be\3\2\2\2\u05bf\u05c0\3\2\2\2\u05c0"+
		"\u05c1\3\2\2\2\u05c1\u05c2\7n\2\2\u05c2\u00f9\3\2\2\2\u05c3\u05c5\5\u017a"+
		"\u00be\2\u05c4\u05c3\3\2\2\2\u05c4\u05c5\3\2\2\2\u05c5\u05c6\3\2\2\2\u05c6"+
		"\u05c7\5\u00b0Y\2\u05c7\u05c8\5\u017a\u00be\2\u05c8\u05ca\3\2\2\2\u05c9"+
		"\u05c4\3\2\2\2\u05ca\u05cb\3\2\2\2\u05cb\u05c9\3\2\2\2\u05cb\u05cc\3\2"+
		"\2\2\u05cc\u00fb\3\2\2\2\u05cd\u05d3\5\u0100\u0081\2\u05ce\u05d3\5\u0102"+
		"\u0082\2\u05cf\u05d3\5\u0104\u0083\2\u05d0\u05d3\5\u00fe\u0080\2\u05d1"+
		"\u05d3\5\u00a2R\2\u05d2\u05cd\3\2\2\2\u05d2\u05ce\3\2\2\2\u05d2\u05cf"+
		"\3\2\2\2\u05d2\u05d0\3\2\2\2\u05d2\u05d1\3\2\2\2\u05d3\u00fd\3\2\2\2\u05d4"+
		"\u05d5\5\u00aeX\2\u05d5\u00ff\3\2\2\2\u05d6\u05d7\5\u00aeX\2\u05d7\u05d8"+
		"\7\u008e\2\2\u05d8\u05d9\5\u00aeX\2\u05d9\u0101\3\2\2\2\u05da\u05db\5"+
		"\u00aeX\2\u05db\u05dc\t\21\2\2\u05dc\u0103\3\2\2\2\u05dd\u05de\5\u00f0"+
		"y\2\u05de\u05df\5\u00e0q\2\u05df\u05e0\5\u00f0y\2\u05e0\u0105\3\2\2\2"+
		"\u05e1\u05e2\t\22\2\2\u05e2\u0107\3\2\2\2\u05e3\u05e4\7j\2\2\u05e4\u05e6"+
		"\7t\2\2\u05e5\u05e7\5\u00b0Y\2\u05e6\u05e5\3\2\2\2\u05e6\u05e7\3\2\2\2"+
		"\u05e7\u0109\3\2\2\2\u05e8\u05ea\7g\2\2\u05e9\u05eb\5\u00f0y\2\u05ea\u05e9"+
		"\3\2\2\2\u05ea\u05eb\3\2\2\2\u05eb\u010b\3\2\2\2\u05ec\u05ee\7P\2\2\u05ed"+
		"\u05ef\7j\2\2\u05ee\u05ed\3\2\2\2\u05ee\u05ef\3\2\2\2\u05ef\u010d\3\2"+
		"\2\2\u05f0\u05f2\7d\2\2\u05f1\u05f3\7j\2\2\u05f2\u05f1\3\2\2\2\u05f2\u05f3"+
		"\3\2\2\2\u05f3\u010f\3\2\2\2\u05f4\u05f5\7\\\2\2\u05f5\u05f6\7j\2\2\u05f6"+
		"\u0111\3\2\2\2\u05f7\u05f8\7`\2\2\u05f8\u0113\3\2\2\2\u05f9\u0602\7a\2"+
		"\2\u05fa\u0603\5\u00aeX\2\u05fb\u05fc\5\u017a\u00be\2\u05fc\u05fd\5\u00ae"+
		"X\2\u05fd\u0603\3\2\2\2\u05fe\u05ff\5\u00fc\177\2\u05ff\u0600\5\u017a"+
		"\u00be\2\u0600\u0601\5\u00aeX\2\u0601\u0603\3\2\2\2\u0602\u05fa\3\2\2"+
		"\2\u0602\u05fb\3\2\2\2\u0602\u05fe\3\2\2\2\u0603\u0604\3\2\2\2\u0604\u060a"+
		"\5\u00f8}\2\u0605\u0608\7[\2\2\u0606\u0609\5\u0114\u008b\2\u0607\u0609"+
		"\5\u00f8}\2\u0608\u0606\3\2\2\2\u0608\u0607\3\2\2\2\u0609\u060b\3\2\2"+
		"\2\u060a\u0605\3\2\2\2\u060a\u060b\3\2\2\2\u060b\u0115\3\2\2\2\u060c\u060f"+
		"\5\u0118\u008d\2\u060d\u060f\5\u011e\u0090\2\u060e\u060c\3\2\2\2\u060e"+
		"\u060d\3\2\2\2\u060f\u0117\3\2\2\2\u0610\u061b\7^\2\2\u0611\u0613\5\u00ae"+
		"X\2\u0612\u0611\3\2\2\2\u0612\u0613\3\2\2\2\u0613\u061c\3\2\2\2\u0614"+
		"\u0616\5\u00fc\177\2\u0615\u0614\3\2\2\2\u0615\u0616\3\2\2\2\u0616\u0617"+
		"\3\2\2\2\u0617\u0619\5\u017a\u00be\2\u0618\u061a\5\u00aeX\2\u0619\u0618"+
		"\3\2\2\2\u0619\u061a\3\2\2\2\u061a\u061c\3\2\2\2\u061b\u0612\3\2\2\2\u061b"+
		"\u0615\3\2\2\2\u061c\u061d\3\2\2\2\u061d\u0621\7m\2\2\u061e\u0620\5\u011a"+
		"\u008e\2\u061f\u061e\3\2\2\2\u0620\u0623\3\2\2\2\u0621\u061f\3\2\2\2\u0621"+
		"\u0622\3\2\2\2\u0622\u0624\3\2\2\2\u0623\u0621\3\2\2\2\u0624\u0625\7n"+
		"\2\2\u0625\u0119\3\2\2\2\u0626\u0627\5\u011c\u008f\2\u0627\u0629\7t\2"+
		"\2\u0628\u062a\5\u00fa~\2\u0629\u0628\3\2\2\2\u0629\u062a\3\2\2\2\u062a"+
		"\u011b\3\2\2\2\u062b\u062c\7U\2\2\u062c\u062f\5\u00f0y\2\u062d\u062f\7"+
		"Q\2\2\u062e\u062b\3\2\2\2\u062e\u062d\3\2\2\2\u062f\u011d\3\2\2\2\u0630"+
		"\u0639\7^\2\2\u0631\u063a\5\u0120\u0091\2\u0632\u0633\5\u017a\u00be\2"+
		"\u0633\u0634\5\u0120\u0091\2\u0634\u063a\3\2\2\2\u0635\u0636\5\u00fc\177"+
		"\2\u0636\u0637\5\u017a\u00be\2\u0637\u0638\5\u0120\u0091\2\u0638\u063a"+
		"\3\2\2\2\u0639\u0631\3\2\2\2\u0639\u0632\3\2\2\2\u0639\u0635\3\2\2\2\u063a"+
		"\u063b\3\2\2\2\u063b\u063f\7m\2\2\u063c\u063e\5\u0122\u0092\2\u063d\u063c"+
		"\3\2\2\2\u063e\u0641\3\2\2\2\u063f\u063d\3\2\2\2\u063f\u0640\3\2\2\2\u0640"+
		"\u0642\3\2\2\2\u0641\u063f\3\2\2\2\u0642\u0643\7n\2\2\u0643\u011f\3\2"+
		"\2\2\u0644\u0645\7j\2\2\u0645\u0647\7x\2\2\u0646\u0644\3\2\2\2\u0646\u0647"+
		"\3\2\2\2\u0647\u0648\3\2\2\2\u0648\u0649\5\u00be`\2\u0649\u064a\7u\2\2"+
		"\u064a\u064b\7k\2\2\u064b\u064c\7c\2\2\u064c\u064d\7l\2\2\u064d\u0121"+
		"\3\2\2\2\u064e\u064f\5\u0124\u0093\2\u064f\u0651\7t\2\2\u0650\u0652\5"+
		"\u00fa~\2\u0651\u0650\3\2\2\2\u0651\u0652\3\2\2\2\u0652\u0123\3\2\2\2"+
		"\u0653\u0654\7U\2\2\u0654\u0657\5\u0126\u0094\2\u0655\u0657\7Q\2\2\u0656"+
		"\u0653\3\2\2\2\u0656\u0655\3\2\2\2\u0657\u0125\3\2\2\2\u0658\u065b\5\u00cc"+
		"g\2\u0659\u065b\7i\2\2\u065a\u0658\3\2\2\2\u065a\u0659\3\2\2\2\u065b\u0663"+
		"\3\2\2\2\u065c\u065f\7r\2\2\u065d\u0660\5\u00ccg\2\u065e\u0660\7i\2\2"+
		"\u065f\u065d\3\2\2\2\u065f\u065e\3\2\2\2\u0660\u0662\3\2\2\2\u0661\u065c"+
		"\3\2\2\2\u0662\u0665\3\2\2\2\u0663\u0661\3\2\2\2\u0663\u0664\3\2\2\2\u0664"+
		"\u0127\3\2\2\2\u0665\u0663\3\2\2\2\u0666\u0667\7T\2\2\u0667\u066b\7m\2"+
		"\2\u0668\u066a\5\u012a\u0096\2\u0669\u0668\3\2\2\2\u066a\u066d\3\2\2\2"+
		"\u066b\u0669\3\2\2\2\u066b\u066c\3\2\2\2\u066c\u066e\3\2\2\2\u066d\u066b"+
		"\3\2\2\2\u066e\u066f\7n\2\2\u066f\u0129\3\2\2\2\u0670\u0671\5\u012c\u0097"+
		"\2\u0671\u0673\7t\2\2\u0672\u0674\5\u00fa~\2\u0673\u0672\3\2\2\2\u0673"+
		"\u0674\3\2\2\2\u0674\u012b\3\2\2\2\u0675\u0678\7U\2\2\u0676\u0679\5\u0100"+
		"\u0081\2\u0677\u0679\5\u012e\u0098\2\u0678\u0676\3\2\2\2\u0678\u0677\3"+
		"\2\2\2\u0679\u067c\3\2\2\2\u067a\u067c\7Q\2\2\u067b\u0675\3\2\2\2\u067b"+
		"\u067a\3\2\2\2\u067c\u012d\3\2\2\2\u067d\u067e\5\u00f0y\2\u067e\u067f"+
		"\7q\2\2\u067f\u0684\3\2\2\2\u0680\u0681\5\u00eex\2\u0681\u0682\7x\2\2"+
		"\u0682\u0684\3\2\2\2\u0683\u067d\3\2\2\2\u0683\u0680\3\2\2\2\u0683\u0684"+
		"\3\2\2\2\u0684\u0685\3\2\2\2\u0685\u0686\5\u00aeX\2\u0686\u012f\3\2\2"+
		"\2\u0687\u068b\7e\2\2\u0688\u068c\5\u00aeX\2\u0689\u068c\5\u0132\u009a"+
		"\2\u068a\u068c\5\u00e2r\2\u068b\u0688\3\2\2\2\u068b\u0689\3\2\2\2\u068b"+
		"\u068a\3\2\2\2\u068b\u068c\3\2\2\2\u068c\u068d\3\2\2\2\u068d\u068e\5\u00f8"+
		"}\2\u068e\u0131\3\2\2\2\u068f\u0691\5\u00fc\177\2\u0690\u068f\3\2\2\2"+
		"\u0690\u0691\3\2\2\2\u0691\u0692\3\2\2\2\u0692\u0694\5\u017a\u00be\2\u0693"+
		"\u0695\5\u00aeX\2\u0694\u0693\3\2\2\2\u0694\u0695\3\2\2\2\u0695\u0696"+
		"\3\2\2\2\u0696\u0698\5\u017a\u00be\2\u0697\u0699\5\u00fc\177\2\u0698\u0697"+
		"\3\2\2\2\u0698\u0699\3\2\2\2\u0699\u0133\3\2\2\2\u069a\u069b\7W\2\2\u069b"+
		"\u069c\5\u00aeX\2\u069c\u0135\3\2\2\2\u069d\u06a0\5\u015a\u00ae\2\u069e"+
		"\u06a0\7j\2\2\u069f\u069d\3\2\2\2\u069f\u069e\3\2\2\2\u06a0\u0137\3\2"+
		"\2\2\u06a1\u06a2\7o\2\2\u06a2\u06a3\5\u013a\u009e\2\u06a3\u06a4\7p\2\2"+
		"\u06a4\u06a5\5\u013c\u009f\2\u06a5\u0139\3\2\2\2\u06a6\u06a7\5\u00aeX"+
		"\2\u06a7\u013b\3\2\2\2\u06a8\u06a9\5\u00ccg\2\u06a9\u013d\3\2\2\2\u06aa"+
		"\u06ab\7\u008c\2\2\u06ab\u06ac\5\u00ccg\2\u06ac\u013f\3\2\2\2\u06ad\u06ae"+
		"\7o\2\2\u06ae\u06af\7p\2\2\u06af\u06b0\5\u013c\u009f\2\u06b0\u0141\3\2"+
		"\2\2\u06b1\u06b2\7X\2\2\u06b2\u06b3\7o\2\2\u06b3\u06b4\5\u00ccg\2\u06b4"+
		"\u06b5\7p\2\2\u06b5\u06b6\5\u013c\u009f\2\u06b6\u0143\3\2\2\2\u06b7\u06bd"+
		"\7Z\2\2\u06b8\u06b9\7Z\2\2\u06b9\u06bd\7\u008e\2\2\u06ba\u06bb\7\u008e"+
		"\2\2\u06bb\u06bd\7Z\2\2\u06bc\u06b7\3\2\2\2\u06bc\u06b8\3\2\2\2\u06bc"+
		"\u06ba\3\2\2\2\u06bd\u06be\3\2\2\2\u06be\u06bf\5\u013c\u009f\2\u06bf\u0145"+
		"\3\2\2\2\u06c0\u06c1\7R\2\2\u06c1\u06c2\5\u0148\u00a5\2\u06c2\u0147\3"+
		"\2\2\2\u06c3\u06c4\5\u014c\u00a7\2\u06c4\u06c5\5\u014a\u00a6\2\u06c5\u06c8"+
		"\3\2\2\2\u06c6\u06c8\5\u014c\u00a7\2\u06c7\u06c3\3\2\2\2\u06c7\u06c6\3"+
		"\2\2\2\u06c8\u0149\3\2\2\2\u06c9\u06cc\5\u014c\u00a7\2\u06ca\u06cc\5\u00cc"+
		"g\2\u06cb\u06c9\3\2\2\2\u06cb\u06ca\3\2\2\2\u06cc\u014b\3\2\2\2\u06cd"+
		"\u06d9\7k\2\2\u06ce\u06d3\5\u00a6T\2\u06cf\u06d0\7r\2\2\u06d0\u06d2\5"+
		"\u00a6T\2\u06d1\u06cf\3\2\2\2\u06d2\u06d5\3\2\2\2\u06d3\u06d1\3\2\2\2"+
		"\u06d3\u06d4\3\2\2\2\u06d4\u06d7\3\2\2\2\u06d5\u06d3\3\2\2\2\u06d6\u06d8"+
		"\7r\2\2\u06d7\u06d6\3\2\2\2\u06d7\u06d8\3\2\2\2\u06d8\u06da\3\2\2\2\u06d9"+
		"\u06ce\3\2\2\2\u06d9\u06da\3\2\2\2\u06da\u06db\3\2\2\2\u06db\u06dc\7l"+
		"\2\2\u06dc\u014d\3\2\2\2\u06dd\u06de\5\u0150\u00a9\2\u06de\u06df\7k\2"+
		"\2\u06df\u06e1\5\u00aeX\2\u06e0\u06e2\7r\2\2\u06e1\u06e0\3\2\2\2\u06e1"+
		"\u06e2\3\2\2\2\u06e2\u06e3\3\2\2\2\u06e3\u06e4\7l\2\2\u06e4\u014f\3\2"+
		"\2\2\u06e5\u06eb\5\u00ceh\2\u06e6\u06e7\7k\2\2\u06e7\u06e8\5\u0150\u00a9"+
		"\2\u06e8\u06e9\7l\2\2\u06e9\u06eb\3\2\2\2\u06ea\u06e5\3\2\2\2\u06ea\u06e6"+
		"\3\2\2\2\u06eb\u0151\3\2\2\2\u06ec\u06f3\5\u0154\u00ab\2\u06ed\u06f3\5"+
		"\u0158\u00ad\2\u06ee\u06ef\7k\2\2\u06ef\u06f0\5\u00aeX\2\u06f0\u06f1\7"+
		"l\2\2\u06f1\u06f3\3\2\2\2\u06f2\u06ec\3\2\2\2\u06f2\u06ed\3\2\2\2\u06f2"+
		"\u06ee\3\2\2\2\u06f3\u0153\3\2\2\2\u06f4\u06f8\5\u00bc_\2\u06f5\u06f8"+
		"\5\u015c\u00af\2\u06f6\u06f8\5\u00c0a\2\u06f7\u06f4\3\2\2\2\u06f7\u06f5"+
		"\3\2\2\2\u06f7\u06f6\3\2\2\2\u06f8\u0155\3\2\2\2\u06f9\u06fa\t\23\2\2"+
		"\u06fa\u0157\3\2\2\2\u06fb\u06fc\7j\2\2\u06fc\u0159\3\2\2\2\u06fd\u06fe"+
		"\7j\2\2\u06fe\u06ff\7u\2\2\u06ff\u0700\7j\2\2\u0700\u015b\3\2\2\2\u0701"+
		"\u0702\5\u00d4k\2\u0702\u0703\5\u015e\u00b0\2\u0703\u015d\3\2\2\2\u0704"+
		"\u0709\7m\2\2\u0705\u0707\5\u0160\u00b1\2\u0706\u0708\7r\2\2\u0707\u0706"+
		"\3\2\2\2\u0707\u0708\3\2\2\2\u0708\u070a\3\2\2\2\u0709\u0705\3\2\2\2\u0709"+
		"\u070a\3\2\2\2\u070a\u070b\3\2\2\2\u070b\u070c\7n\2\2\u070c\u015f\3\2"+
		"\2\2\u070d\u0712\5\u0162\u00b2\2\u070e\u070f\7r\2\2\u070f\u0711\5\u0162"+
		"\u00b2\2\u0710\u070e\3\2\2\2\u0711\u0714\3\2\2\2\u0712\u0710\3\2\2\2\u0712"+
		"\u0713\3\2\2\2\u0713\u0161\3\2\2\2\u0714\u0712\3\2\2\2\u0715\u0716\5\u0164"+
		"\u00b3\2\u0716\u0717\7t\2\2\u0717\u0719\3\2\2\2\u0718\u0715\3\2\2\2\u0718"+
		"\u0719\3\2\2\2\u0719\u071a\3\2\2\2\u071a\u071b\5\u0166\u00b4\2\u071b\u0163"+
		"\3\2\2\2\u071c\u071f\5\u00aeX\2\u071d\u071f\5\u015e\u00b0\2\u071e\u071c"+
		"\3\2\2\2\u071e\u071d\3\2\2\2\u071f\u0165\3\2\2\2\u0720\u0723\5\u00aeX"+
		"\2\u0721\u0723\5\u015e\u00b0\2\u0722\u0720\3\2\2\2\u0722\u0721\3\2\2\2"+
		"\u0723\u0167\3\2\2\2\u0724\u0725\7Y\2\2\u0725\u072b\7m\2\2\u0726\u0727"+
		"\5\u016a\u00b6\2\u0727\u0728\5\u017a\u00be\2\u0728\u072a\3\2\2\2\u0729"+
		"\u0726\3\2\2\2\u072a\u072d\3\2\2\2\u072b\u0729\3\2\2\2\u072b\u072c\3\2"+
		"\2\2\u072c\u072e\3\2\2\2\u072d\u072b\3\2\2\2\u072e\u072f\7n\2\2\u072f"+
		"\u0169\3\2\2\2\u0730\u0731\5\u00eex\2\u0731\u0732\5\u00ccg\2\u0732\u0735"+
		"\3\2\2\2\u0733\u0735\5\u016e\u00b8\2\u0734\u0730\3\2\2\2\u0734\u0733\3"+
		"\2\2\2\u0735\u0737\3\2\2\2\u0736\u0738\5\u016c\u00b7\2\u0737\u0736\3\2"+
		"\2\2\u0737\u0738\3\2\2\2\u0738\u016b\3\2\2\2\u0739\u073a\t\24\2\2\u073a"+
		"\u016d\3\2\2\2\u073b\u073d\7\u008c\2\2\u073c\u073b\3\2\2\2\u073c\u073d"+
		"\3\2\2\2\u073d\u073e\3\2\2\2\u073e\u073f\5\u0136\u009c\2\u073f\u016f\3"+
		"\2\2\2\u0740\u0741\7o\2\2\u0741\u0742\5\u00aeX\2\u0742\u0743\7p\2\2\u0743"+
		"\u0171\3\2\2\2\u0744\u0745\7u\2\2\u0745\u0746\7k\2\2\u0746\u0747\5\u00cc"+
		"g\2\u0747\u0748\7l\2\2\u0748\u0173\3\2\2\2\u0749\u0758\7k\2\2\u074a\u0751"+
		"\5\u00f0y\2\u074b\u074e\5\u0150\u00a9\2\u074c\u074d\7r\2\2\u074d\u074f"+
		"\5\u00f0y\2\u074e\u074c\3\2\2\2\u074e\u074f\3\2\2\2\u074f\u0751\3\2\2"+
		"\2\u0750\u074a\3\2\2\2\u0750\u074b\3\2\2\2\u0751\u0753\3\2\2\2\u0752\u0754"+
		"\7y\2\2\u0753\u0752\3\2\2\2\u0753\u0754\3\2\2\2\u0754\u0756\3\2\2\2\u0755"+
		"\u0757\7r\2\2\u0756\u0755\3\2\2\2\u0756\u0757\3\2\2\2\u0757\u0759\3\2"+
		"\2\2\u0758\u0750\3\2\2\2\u0758\u0759\3\2\2\2\u0759\u075a\3\2\2\2\u075a"+
		"\u075b\7l\2\2\u075b\u0175\3\2\2\2\u075c\u075d\5\u0150\u00a9\2\u075d\u075e"+
		"\7u\2\2\u075e\u075f\7j\2\2\u075f\u0177\3\2\2\2\u0760\u0761\5\u00ccg\2"+
		"\u0761\u0179\3\2\2\2\u0762\u0767\7s\2\2\u0763\u0767\7\2\2\3\u0764\u0767"+
		"\7\u00a4\2\2\u0765\u0767\6\u00be\24\2\u0766\u0762\3\2\2\2\u0766\u0763"+
		"\3\2\2\2\u0766\u0764\3\2\2\2\u0766\u0765\3\2\2\2\u0767\u017b\3\2\2\2\u00c6"+
		"\u018a\u018f\u0196\u01a0\u01a6\u01ac\u01bc\u01c0\u01c9\u01d5\u01d9\u01df"+
		"\u01e8\u01f2\u0203\u0211\u0215\u021c\u0224\u022d\u024d\u0255\u026d\u0280"+
		"\u028f\u029c\u02a5\u02b3\u02bc\u02c8\u02dd\u02e4\u02e9\u02ee\u02fa\u02fd"+
		"\u0301\u0305\u030d\u0311\u0313\u0321\u0329\u032e\u0336\u0338\u033d\u0344"+
		"\u034c\u034f\u0355\u035a\u035c\u035f\u0366\u036b\u037e\u0386\u038a\u038d"+
		"\u0393\u0397\u039a\u03a4\u03ab\u03b2\u03c1\u03c7\u03ce\u03d3\u03d9\u03e5"+
		"\u03e9\u03ee\u03f2\u03f7\u03fb\u0400\u0406\u040a\u0412\u0416\u041c\u041f"+
		"\u0425\u042a\u043e\u0461\u0463\u047a\u0482\u048d\u0494\u049b\u04a5\u04b3"+
		"\u04c9\u04cb\u04d3\u04d7\u04db\u04de\u04e7\u04ed\u04f7\u04ff\u0505\u050e"+
		"\u0519\u0524\u0528\u052a\u0535\u053e\u0542\u0545\u054c\u0557\u0561\u0567"+
		"\u0569\u0573\u057d\u0581\u0585\u0589\u0590\u0598\u05a3\u05a7\u05ab\u05b7"+
		"\u05bb\u05bf\u05c4\u05cb\u05d2\u05e6\u05ea\u05ee\u05f2\u0602\u0608\u060a"+
		"\u060e\u0612\u0615\u0619\u061b\u0621\u0629\u062e\u0639\u063f\u0646\u0651"+
		"\u0656\u065a\u065f\u0663\u066b\u0673\u0678\u067b\u0683\u068b\u0690\u0694"+
		"\u0698\u069f\u06bc\u06c7\u06cb\u06d3\u06d7\u06d9\u06e1\u06ea\u06f2\u06f7"+
		"\u0707\u0709\u0712\u0718\u071e\u0722\u072b\u0734\u0737\u073c\u074e\u0750"+
		"\u0753\u0756\u0758\u0766";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}