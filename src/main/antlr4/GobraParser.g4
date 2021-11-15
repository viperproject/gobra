parser grammar GobraParser;
import GoParser;

options {
    tokenVocab = GobraLexer;
	superClass = GobraParserBase;
}

// BEGIN GOBRA

ghostStatement:
    ASSERT expression
    ;

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
    : specStatement (specStatement)*
    ;

specStatement
    : kind=PRE assertion
    | kind=POST assertion
    ;

assertion:
    | expression
    | kind=EXCLAMATION assertion
    | assertion kind=LOGICAL_AND assertion
    | assertion kind=LOGICAL_OR assertion
    ;

functionDecl: specification? FUNC IDENTIFIER (signature block?);

// END GOBRA
