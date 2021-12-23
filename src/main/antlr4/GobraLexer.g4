lexer grammar GobraLexer;
import GoLexer;

// BEGIN GOBRA
//CURLIES : '{' (CURLIES|~[{}])* '}' ;

TRUE        : 'true';
FALSE       : 'false';
ASSERT      : 'assert';
ASSUME      : 'assume';
PRE         : 'requires';
PRESERVES   : 'preserves';
POST        : 'ensures';
INV         : 'invariant';
DEC         : 'decreases';
PURE        : 'pure';
IMPL        : 'implements';
OLD         : 'old';
LHS         : '#lhs';
FORALL      : 'forall';
EXISTS      : 'exists';
ACCESS      : 'acc';
FOLD        : 'fold';
UNFOLD      : 'unfold';
UNFOLDING   : 'unfolding';
GHOST       : 'ghost';
IN          : 'in';
MULTI       : '#';
SUBSET      : 'subset';
UNION       : 'union';
INTERSECTION: 'intersection';
SETMINUS    : 'setminus';
IMPLIES     : '==>';
QMARK       : '?';
L_PRED      : '!<';
R_PRED      : '!>';
RANGE       : 'range';
SEQ         : 'seq';
SET         : 'set';
MSET        : 'mset';
DICT        : 'dict';
OPT         : 'option';
LEN         : 'len';
NEW         : 'new';
MAKE        : 'make';
CAP         : 'cap';
SOME        : 'some';
GET         : 'get';
DOM         : 'domain';
NONE        : 'none';
PRED        : 'pred';
TYPE_OF      : 'typeOf';
IS_COMPARABLE: 'isComparable';
SHARE       : 'share';
ADDR_MOD    : '@';
DOT_DOT     : '..';
SHARED      : 'shared';
EXCLUSIVE   : 'exclusive';
PREDICATE   : 'predicate';

// Types
       BOOL: 'bool';
         STRING: 'string';
         PERM: 'perm';
        // signed integer types
         RUNE: 'rune';
         INT: 'int';
         INT8: 'int8';
         INT16: 'int16';
         INT32: 'int32';
         INT64: 'int64';
        // unsigned integer types
         BYTE: 'byte';
         UINT: 'uint';
         UINT8: 'uint8';
         UINT16: 'uint16';
         UINT32: 'uint32';
         UINT64: 'uint64';
         UINTPTR: 'uintptr';
// END GOBRA
