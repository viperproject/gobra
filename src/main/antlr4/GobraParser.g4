parser grammar GobraParser;
import GoParser;

options {
    tokenVocab = GobraLexer;
	superClass = GobraParserBase;
}

// BEGIN GOBRA

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
