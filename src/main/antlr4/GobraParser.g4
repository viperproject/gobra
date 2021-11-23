parser grammar GobraParser;
import GoParser;

options {
    tokenVocab = GobraLexer;
	superClass = GobraParserBase;
}

// BEGIN GOBRA
@members {boolean specOnly = false;}

ghostStatement:
    ASSERT expression
    ;

exprOnly: expression EOF;

stmtOnly: statement EOF;

expression:
	primaryExpr
	| unaryExpr
	| expression mul_op = (
		STAR
		| DIV
		| MOD
		| LSHIFT
		| RSHIFT
		| AMPERSAND
		| BIT_CLEAR
	) expression
	| expression add_op = (PLUS | MINUS | OR | CARET | PLUS_PLUS ) expression
	| expression rel_op = (
		EQUALS
		| NOT_EQUALS
		| LESS
		| LESS_OR_EQUALS
		| GREATER
		| GREATER_OR_EQUALS
	) expression
	| expression LOGICAL_AND expression
	| expression LOGICAL_OR expression;

statement:
    ghostStatement
	| declaration
	| labeledStmt
	| simpleStmt
	| goStmt
	| returnStmt
	| breakStmt
	| continueStmt
	| gotoStmt
	| fallthroughStmt
	| block
	| ifStmt
	| switchStmt
	| selectStmt
	| forStmt
	| deferStmt;

primaryExpr:
	operand
	| conversion
	| methodExpr
	| ghostPrimaryExpr
	| primaryExpr (
		(DOT IDENTIFIER)
		| index
		| slice_
		| seqUpdExp
		| typeAssertion
		| arguments
	);

ghostPrimaryExpr: range;

range: kind=(SEQ | SET | MSET) L_BRACKET expression DOT_DOT expression R_BRACKET;


type_: typeName | typeLit | ghostTypeLit | L_PAREN type_ R_PAREN;

ghostTypeLit: sequenceType;

literalType:
	structType
	| arrayType
	| L_BRACKET ELLIPSIS R_BRACKET elementType
	| sliceType
	| mapType
	| ghostTypeLit
	| typeName;

sequenceType: SEQ L_BRACKET type_ R_BRACKET;

seqUpdExp: L_BRACKET (seqUpdClause (COMMA seqUpdClause)* COMMA?) R_BRACKET;

seqUpdClause: expression ASSIGN expression;


basicLit:
    TRUE
	| FALSE
	| NIL_LIT
	| integer
	| string_
	| FLOAT_LIT
	| IMAGINARY_LIT
	| RUNE_LIT;


specification
    : specStatement (specStatement)* PURE?
    ;

specStatement
    : kind=PRE assertion
    | kind=PRESERVES assertion
    | kind=POST assertion
    ;

assertion:
    | expression
    | kind=EXCLAMATION assertion
    | assertion kind=LOGICAL_AND assertion
    | assertion kind=LOGICAL_OR assertion
    ;

functionDecl: specification? FUNC IDENTIFIER (signature block?);

eos:
	SEMI
	| EOF
	| {lineTerminatorAhead()}?
	| {checkPreviousTokenText("}")}?
	| {checkPreviousTokenText(")")}?;
// END GOBRA
