parser grammar GobraParser;
import GoParser;

options {
    tokenVocab = GobraLexer;
	superClass = GobraParserBase;
}

// New rules
@members {boolean specOnly = false;}

maybeAddressableIdentifierList: maybeAddressableIdentifier (COMMA maybeAddressableIdentifier)*;


maybeAddressableIdentifier: IDENTIFIER ADDR_MOD?;

ghostStatement:
    GHOST statement |
    ASSERT expression |
    fold_stmt=(FOLD | UNFOLD) predicateAccess
    ;

boundVariables
    : boundVariableDecl (COMMA boundVariableDecl)* COMMA?
    ;


boundVariableDecl
    : IDENTIFIER (COMMA IDENTIFIER)* elementType
    ;

predicateAccess: primaryExpr;

access: ACCESS L_PAREN expression (COMMA (IDENTIFIER | expression))? R_PAREN;

exprOnly: expression EOF;

stmtOnly: statement EOF;

ghostPrimaryExpr: range
                | access
                | typeOf
                | isComparable
                | old
                | sConversion
                | optionNone | optionSome | optionGet 
                | FORALL boundVariables COLON COLON triggers expression;

optionSome: SOME L_PAREN expression R_PAREN;

optionNone: NONE L_BRACKET type_ R_BRACKET;

optionGet: GET L_PAREN expression R_PAREN;

sConversion: kind=(SET | SEQ | MSET) L_PAREN expression R_PAREN;

triggers: trigger*;

trigger: L_CURLY expression (COMMA expression)* R_CURLY;

old: OLD (L_BRACKET oldLabelUse R_BRACKET)? L_PAREN expression R_PAREN;

oldLabelUse: labelUse | LHS;

labelUse: IDENTIFIER;

typeOf: TYPE_OF L_PAREN expression R_PAREN;

isComparable: IS_COMPARABLE L_PAREN expression R_PAREN;

ghostTypeLit: sqType | ghostSliceType;

ghostSliceType: GHOST L_BRACKET R_BRACKET elementType;

sqType: (kind=(SEQ | SET | MSET | OPT) L_BRACKET type_ R_BRACKET)
        | kind=DICT L_BRACKET type_ R_BRACKET type_;

seqUpdExp: L_BRACKET (seqUpdClause (COMMA seqUpdClause)*) R_BRACKET;

seqUpdClause: expression ASSIGN expression;

specification
    : ((specStatement) eos)* PURE
    | ((specStatement | PURE) eos)*
    ;

specStatement
    : kind=PRE assertion
    | kind=PRESERVES assertion
    | kind=POST assertion
    | kind=DEC terminationMeasure
    ;

functionDecl: specification FUNC IDENTIFIER (signature blockWithBodyParameterInfo?);

methodDecl: specification FUNC receiver IDENTIFIER ( signature blockWithBodyParameterInfo?);

blockWithBodyParameterInfo: L_CURLY (SHARE identifierList eos)? statementList? R_CURLY;

assertion:
    | expression
    ;

range: kind=(SEQ | SET | MSET) L_BRACKET expression DOT_DOT expression R_BRACKET;

// Changed Rules

// Ghost members

sourceFile:
	packageClause eos (importDecl eos)* (
		(functionDecl | methodDecl | declaration | ghostMember) eos
	)* EOF;

ghostMember: fpredicateDecl
            | mpredicateDecl
            | implementationProof
            | (GHOST | (GHOST eos)) (
            methodDecl 
            | functionDecl 
            | constDecl
            | typeDecl
            | varDecl
            );

fpredicateDecl: PRED IDENTIFIER parameters predicateBody;

predicateBody: L_CURLY expression eos R_CURLY;

mpredicateDecl: PRED receiver IDENTIFIER parameters predicateBody;

implementationProof: type_ IMPL type_ L_CURLY (implementationProofPredicateAlias eos)* (methodImplementationProof eos)*  R_CURLY;

methodImplementationProof: PURE? nonLocalReceiver IDENTIFIER signature block?;

selection: primaryExpr DOT IDENTIFIER
            | type_ DOT IDENTIFIER;

implementationProofPredicateAlias: PRED IDENTIFIER DECLARE_ASSIGN (selection | operandName);


// Addresability


varSpec:
	maybeAddressableIdentifierList (
		type_ (ASSIGN expressionList)?
		| ASSIGN expressionList
	);

shortVarDecl: maybeAddressableIdentifierList DECLARE_ASSIGN expressionList;

receiver: 	L_PAREN maybeAddressableIdentifier? STAR? IDENTIFIER R_PAREN;

nonLocalReceiver: 	L_PAREN IDENTIFIER? STAR? typeName R_PAREN;



// Added ghost parameters
parameterDecl: GHOST? identifierList? ELLIPSIS? type_;

// Added unfolding
unaryExpr:
	primaryExpr
	| kind=(
    LEN
    | CAP
    | DOM
    | RANGE
	) L_PAREN expression R_PAREN
	| unfolding
	| unary_op = (
		PLUS
		| MINUS
		| EXCLAMATION
		| CARET
		| STAR
		| AMPERSAND
		| RECEIVE
	) expression
    ;

unfolding: UNFOLDING predicateAccess IN expression;

// Added ++ operator
expression:
	primaryExpr
	| call_op=(
    LEN
    | CAP
    | DOM
    | RANGE
	) L_PAREN expression R_PAREN
	| unfolding
	| new_
	| make
	| unary_op = (
		PLUS
		| MINUS
		| EXCLAMATION
		| CARET
		| STAR
		| AMPERSAND
		| RECEIVE
	) expression
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
    | expression p42_op = (
        UNION
        | INTERSECTION
        | SETMINUS
    ) expression
	| expression p41_op = (
    	IN
    	| MULTI
    	| SUBSET
    ) expression
	| expression rel_op = (
		EQUALS
		| NOT_EQUALS
		| LESS
		| LESS_OR_EQUALS
		| GREATER
		| GREATER_OR_EQUALS
	) expression
	| expression LOGICAL_AND expression
	| expression LOGICAL_OR expression
	|<assoc=right> expression IMPLIES expression
	|<assoc=right> expression QMARK expression COLON expression;

make: MAKE L_PAREN type_ (COMMA expressionList)? R_PAREN;

new_: NEW L_PAREN type_ R_PAREN;

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
	| specForStmt
	| deferStmt;

specForStmt: loopSpec forStmt;

loopSpec: (INV expression eos)* (DEC terminationMeasure eos)?;

terminationMeasure: expressionList (IF expression)?;

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
		| predConstructArgs
	);

predConstructArgs: L_PRED expressionList? COMMA? R_PRED;

// Added predicate spec and method specifications
interfaceType:
	INTERFACE L_CURLY ((methodSpec | typeName| predicateSpec) eos)* R_CURLY;

predicateSpec: PRED IDENTIFIER parameters;

methodSpec:
	{noTerminatorAfterParams(2)}? GHOST? specification IDENTIFIER parameters result
	| GHOST? specification IDENTIFIER parameters;

// Added ghostTypeLiterals
type_: typeName | typeLit | ghostTypeLit | L_PAREN type_ R_PAREN
        | predefined=(
               BOOL |
                 STRING |
                 PERM |
                // signed integer types
                 RUNE |
                 INT |
                 INT8 |
                 INT16 |
                 INT32 |
                 INT64 |
                // unsigned integer types
                 BYTE |
                 UINT |
                 UINT8 |
                 UINT16 |
                 UINT32 |
                 UINT64 |
                 UINTPTR
        );
// Added pred types
typeLit:
	arrayType
	| structType
	| pointerType
	| functionType
	| interfaceType
	| sliceType
	| mapType
	| channelType
	| predType;

predType: PRED predTypeParams;

predTypeParams: L_PAREN (type_ (COMMA type_)* COMMA?)? R_PAREN;

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

// distinguish low,high cap
slice_:
	L_BRACKET (
		low? COLON high?
		| low? COLON high COLON cap
	) R_BRACKET;

low : expression;
high: expression;
cap: expression;

// allow "if ; true {}"
ifStmt:
	IF (simpleStmt? SEMI)? expression block (
		ELSE (ifStmt | block)
	)?;

// Introduce name for operator
assign_op: ass_op=(
		PLUS
		| MINUS
		| OR
		| CARET
		| STAR
		| DIV
		| MOD
		| LSHIFT
		| RSHIFT
		| AMPERSAND
		| BIT_CLEAR
	)? ASSIGN;

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
