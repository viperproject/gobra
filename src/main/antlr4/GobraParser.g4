// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

parser grammar GobraParser;
import GoParser;

options {
  tokenVocab = GobraLexer;
  superClass = GobraParserBase;
}
///////////////////
// New Rules     //
///////////////////

@members {boolean specOnly = false;}

// Rules for fine-grained parsing. Without the EOF, they might only parse parts of the input
// and succeed instead of failing
exprOnly: expression EOF;

stmtOnly: statement EOF;

typeOnly: type_ EOF;


// Identifier lists with added addressability modifiers
maybeAddressableIdentifierList: maybeAddressableIdentifier (COMMA maybeAddressableIdentifier)*;


maybeAddressableIdentifier: IDENTIFIER ADDR_MOD?;

// Ghost members

sourceFile:
  packageClause eos (importDecl eos)* (
    (specMember | declaration | ghostMember) eos
  )* EOF;

ghostMember: implementationProof
  | fpredicateDecl
  | mpredicateDecl
  | explicitGhostMember;

// Ghost statements

ghostStatement:
  GHOST statement  #explicitGhostStatement
  | fold_stmt=(FOLD | UNFOLD) predicateAccess #foldStatement
  | kind=(ASSUME | ASSERT | INHALE | EXHALE) expression #proofStatement
  ;

// Auxiliary statements

auxiliaryStatement:
  statementWithSpec
  ;

statementWithSpec: specification (outlineStatement[$specification.trusted, $specification.pure]);

outlineStatement[boolean trusted, boolean pure]:  OUTLINE L_PAREN statementList? R_PAREN;

// Ghost Primary Expressions

ghostPrimaryExpr: range
  | access
  | typeOf
  | typeExpr
  | isComparable
  | old
  | before
  | sConversion
  | optionNone | optionSome | optionGet
  | permission;

permission: WRITEPERM | NOPERM;

typeExpr: TYPE L_BRACKET type_ R_BRACKET;

boundVariables
  : boundVariableDecl (COMMA boundVariableDecl)* COMMA?
  ;

boundVariableDecl
  : IDENTIFIER (COMMA IDENTIFIER)* elementType
  ;

triggers: trigger*;

trigger: L_CURLY expression (COMMA expression)* R_CURLY;

predicateAccess: primaryExpr;

optionSome: SOME L_PAREN expression R_PAREN;

optionNone: NONE L_BRACKET type_ R_BRACKET;

optionGet: GET L_PAREN expression R_PAREN;

sConversion: kind=(SET | SEQ | MSET) L_PAREN expression R_PAREN;

old: OLD (L_BRACKET oldLabelUse R_BRACKET)? L_PAREN expression R_PAREN;

oldLabelUse: labelUse | LHS;

labelUse: IDENTIFIER;

before: BEFORE L_PAREN expression R_PAREN;

isComparable: IS_COMPARABLE L_PAREN expression R_PAREN;

typeOf: TYPE_OF L_PAREN expression R_PAREN;

access: ACCESS L_PAREN expression (COMMA expression)? R_PAREN;

range: kind=(SEQ | SET | MSET) L_BRACKET expression DOT_DOT expression R_BRACKET;

// Added directly to primaryExpr
seqUpdExp: L_BRACKET (seqUpdClause (COMMA seqUpdClause)*) R_BRACKET;

seqUpdClause: expression ASSIGN expression;

// Ghost Type Literals

ghostTypeLit: sqType | ghostSliceType | domainType;

domainType: DOM L_CURLY (domainClause eos)* R_CURLY;

domainClause: FUNC IDENTIFIER signature | AXIOM L_CURLY expression eos R_CURLY;

ghostSliceType: GHOST L_BRACKET R_BRACKET elementType;

sqType: (kind=(SEQ | SET | MSET | OPT) L_BRACKET type_ R_BRACKET)
    | kind=DICT L_BRACKET type_ R_BRACKET type_;

// Specifications

specification returns[boolean trusted = false, boolean pure = false;]:
  ((specStatement | PURE {$pure = true;} | TRUSTED {$trusted = true;}) eos)*? (PURE {$pure = true;})? // Non-greedily match PURE to avoid missing eos errors.
  ;

specStatement
  : kind=PRE assertion
  | kind=PRESERVES assertion
  | kind=POST assertion
  | kind=DEC terminationMeasure
  ;
terminationMeasure: expressionList? (IF expression)?;

assertion:
  | expression
  ;

blockWithBodyParameterInfo: L_CURLY (SHARE identifierList eos)? statementList? R_CURLY;

// Closures
closureSpecInstance: IDENTIFIER (L_CURLY (closureSpecParams COMMA?)? R_CURLY)?;

closureSpecParams: closureSpecParam (COMMA closureSpecParam)*;

closureSpecParam: (IDENTIFIER COLON)? expression;


// Implementation proofs
implementationProof: type_ IMPL type_ (L_CURLY (implementationProofPredicateAlias eos)* (methodImplementationProof eos)*  R_CURLY)?;

methodImplementationProof: PURE? nonLocalReceiver IDENTIFIER signature block?;

nonLocalReceiver: L_PAREN IDENTIFIER? STAR? typeName R_PAREN;

// Selection matches any primary Expression, make sure to check if it is truly a selection
// This cannot be done in the parser because of precedence
selection: primaryExpr
  | type_ DOT IDENTIFIER;

implementationProofPredicateAlias: PRED IDENTIFIER DECLARE_ASSIGN (selection | operandName);

// Built-in methods baked into the parser for now
make: MAKE L_PAREN type_ (COMMA expressionList)? R_PAREN;

new_: NEW L_PAREN type_ R_PAREN;


///////////////////
// Changed Rules //
///////////////////

// Added specifications and parameter info

specMember: specification (functionDecl[$specification.trusted, $specification.pure] | methodDecl[$specification.trusted, $specification.pure]);

functionDecl[boolean trusted, boolean pure]:  FUNC IDENTIFIER (signature blockWithBodyParameterInfo?);

methodDecl[boolean trusted, boolean pure]: FUNC receiver IDENTIFIER (signature blockWithBodyParameterInfo?);



explicitGhostMember: GHOST (specMember | declaration);

fpredicateDecl: PRED IDENTIFIER parameters predicateBody?;

predicateBody: L_CURLY expression eos R_CURLY;

mpredicateDecl: PRED receiver IDENTIFIER parameters predicateBody?;

// Addressability

varSpec:
  maybeAddressableIdentifierList (
    type_ (ASSIGN expressionList)?
    | ASSIGN expressionList
  );

shortVarDecl: maybeAddressableIdentifierList DECLARE_ASSIGN expressionList;

receiver:   L_PAREN maybeAddressableIdentifier? type_ COMMA? R_PAREN;


// Added ghost parameters
parameterDecl: actualParameterDecl | ghostParameterDecl;

actualParameterDecl:  identifierList? parameterType;

ghostParameterDecl: GHOST identifierList? parameterType;

parameterType: ELLIPSIS? type_;

// Added Gobra's operators (set, wand, quantifications, etc)
expression:
  unary_op = (
    PLUS
    | MINUS
    | EXCLAMATION
    | CARET
    | STAR
    | AMPERSAND
    | RECEIVE
  ) expression #unaryExpr
  | primaryExpr #primaryExpr_
  | expression mul_op = (
    STAR
    | DIV
    | MOD
    | LSHIFT
    | RSHIFT
    | AMPERSAND
    | BIT_CLEAR
  ) expression #mulExpr
  | expression add_op = (PLUS | MINUS | OR | CARET | PLUS_PLUS | WAND) expression #addExpr
  | expression p42_op = (
    UNION
    | INTERSECTION
    | SETMINUS
  ) expression #p42Expr
  | expression p41_op = (
    IN
    | MULTI
    | SUBSET
  ) expression #p41Expr
  | expression rel_op = (
    EQUALS
    | NOT_EQUALS
    | LESS
    | LESS_OR_EQUALS
    | GREATER
    | GREATER_OR_EQUALS
  ) expression #relExpr
  | expression IMPL closureSpecInstance #closureImplSpecExpr
  | expression LOGICAL_AND expression #andExpr
  | expression LOGICAL_OR expression #orExpr
  |<assoc=right> expression IMPLIES expression #implication
  |<assoc=right> expression QMARK expression COLON expression #ternaryExpr
  | UNFOLDING predicateAccess IN expression #unfolding
  | (FORALL | EXISTS) boundVariables COLON COLON triggers expression #quantification
  ;


// Added ghost statements
statement:
  ghostStatement
  | auxiliaryStatement
  | packageStmt
  | applyStmt
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

applyStmt: APPLY expression;

packageStmt: PACKAGE expression block?;

specForStmt: loopSpec forStmt;

loopSpec: (INV expression eos)* (DEC terminationMeasure eos)?;

deferStmt:
  DEFER expression
  | DEFER fold_stmt=(FOLD | UNFOLD) predicateAccess;

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
  operand #operandPrimaryExpr
  | conversion #conversionPrimaryExpr
  | methodExpr #methodPrimaryExpr
  | ghostPrimaryExpr #ghostPrimaryExpr_
  | new_  #newExpr
  | make #makeExpr
  | primaryExpr DOT IDENTIFIER #selectorPrimaryExpr
  | primaryExpr index #indexPrimaryExpr
  | primaryExpr slice_ #slicePrimaryExpr
  | primaryExpr seqUpdExp #seqUpdPrimaryExpr
  | primaryExpr typeAssertion #typeAssertionPrimaryExpr
  | primaryExpr arguments #invokePrimaryExpr
  | primaryExpr arguments AS closureSpecInstance #invokePrimaryExprWithSpec
  | primaryExpr predConstructArgs #predConstrPrimaryExpr
  | call_op=(
  LEN
    | CAP
    | DOM
    | RANGE
  ) L_PAREN expression R_PAREN #builtInCallExpr // Remove this alternative when predeclared functions are properly handled
  ;

// Added function literal with specification
operand: literal | operandName | L_PAREN expression R_PAREN;

literal: basicLit | compositeLit | functionLit;

functionLit: specification closureDecl[$specification.trusted, $specification.pure];

closureDecl[boolean trusted, boolean pure]:  FUNC IDENTIFIER? (signature blockWithBodyParameterInfo?);

predConstructArgs: L_PRED expressionList? COMMA? R_PRED;

// Added predicate spec and method specifications
interfaceType:
  INTERFACE L_CURLY ((methodSpec | typeName| predicateSpec) eos)* R_CURLY;

predicateSpec: PRED IDENTIFIER parameters;

methodSpec:
  GHOST? specification IDENTIFIER parameters result
  | GHOST? specification IDENTIFIER parameters;

// Added ghostTypeLiterals
type_: typeName | typeLit | ghostTypeLit | L_PAREN type_ R_PAREN;

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

// Added ghost type and moved implicit size arrays to their own rule
literalType:
  structType
  | arrayType
  | implicitArray
  | sliceType
  | mapType
  | ghostTypeLit
  | typeName;

implicitArray: L_BRACKET ELLIPSIS R_BRACKET elementType;

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



// Introduce label for operator
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
