parser grammar GobraParser;
import GoParser;

options {
    tokenVocab = GobraLexer;
	superClass = GobraParserBase;
}

// New rules
@members {boolean specOnly = false;}

ghostStatement:
    GHOST statement |
    ASSERT expression |
    fold_stmt=(FOLD | UNFOLD) predicateAccess
    ;

predicateAccess: primaryExpr;

access: ACCESS L_PAREN expression (COMMA (IDENTIFIER | expression))? R_PAREN;

exprOnly: expression EOF;

stmtOnly: statement EOF;

ghostPrimaryExpr: range
                | access;

ghostTypeLit: sequenceType;

sequenceType: SEQ L_BRACKET type_ R_BRACKET;

seqUpdExp: L_BRACKET (seqUpdClause (COMMA seqUpdClause)*) R_BRACKET;

seqUpdClause: expression ASSIGN expression;

specification
    : specStatement (specStatement)* PURE?
    ;

specStatement
    : kind=PRE assertion
    | kind=PRESERVES assertion
    | kind=POST assertion
    ;

functionDecl: specification? FUNC IDENTIFIER (signature block?);

methodDecl: specification? FUNC receiver IDENTIFIER ( signature block?);

assertion:
    | expression
    | kind=EXCLAMATION assertion
    | assertion kind=LOGICAL_AND assertion
    | assertion kind=LOGICAL_OR assertion
    ;

range: kind=(SEQ | SET | MSET) L_BRACKET expression DOT_DOT expression R_BRACKET;

// Changed Rules

// Added ghost parameters
parameterDecl: GHOST? identifierList? ELLIPSIS? type_;


// Added ++ operator
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

// Added ghost statements
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

// Added true, false as literals
basicLit:
    TRUE
	| FALSE
	| NIL_LIT
	| integer
	| string_
	| FLOAT_LIT
	| IMAGINARY_LIT
	| RUNE_LIT;

// Added ghostPrimaryExprs
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

// Added predicate spec
interfaceType:
	INTERFACE L_CURLY ((methodSpec | typeName| predicateSpec) eos)* R_CURLY;

predicateSpec: PRED IDENTIFIER parameters;

// Added ghostTypeLiterals
type_: typeName | typeLit | ghostTypeLit | L_PAREN type_ R_PAREN;

// Ditto
literalType:
	structType
	| arrayType
	| L_BRACKET ELLIPSIS R_BRACKET elementType
	| sliceType
	| mapType
	| ghostTypeLit
	| typeName;

// ANTLR Grammar fixes

// allow "if ; true {}"
ifStmt:
	IF (simpleStmt? SEMI)? expression block (
		ELSE (ifStmt | block)
	)?;

// same for switch
exprSwitchStmt:
	SWITCH (simpleStmt? SEMI)? expression? L_CURLY exprCaseClause* R_CURLY;

typeSwitchStmt:
	SWITCH (simpleStmt? SEMI)? typeSwitchGuard L_CURLY typeCaseClause* R_CURLY;

// allow "import ("import1";"import2") without semicolon at the end
eos:
	SEMI
	| EOF
	| {lineTerminatorAhead()}?
	| {checkPreviousTokenText("}")}?
	| {checkPreviousTokenText(")")}?;
