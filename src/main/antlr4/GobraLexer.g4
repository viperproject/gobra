lexer grammar GobraLexer;
import GoLexer;

/*
DECIMAL_FLOAT_LIT      : DECIMALS ('.'{_input.LA(1) != '.'}? DECIMALS? EXPONENT? | EXPONENT)
                       | '.'{_input.LA(-1) != '.'}? DECIMALS EXPONENT?
                       ;
*/
// BEGIN GOBRA
//CURLIES : '{' (CURLIES|~[{}])* '}' ;

TRUE        : 'true';
FALSE       : 'false';
ASSERT      : 'assert';
ASSUME      : 'assume';
INHALE      : 'inhale';
EXHALE      : 'exhale';
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
WAND        : '--*';
APPLY       : 'apply';
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
AXIOM       : 'axiom';
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
WRITEPERM   : 'writePerm';
NOPERM      : 'noPerm';


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
         // float types
         FLOAT32: 'float32';
         FLOAT64: 'float64';
         //  complex types
         COMPLEX64: 'complex64';
         COMPLEX128: 'complex128';
// END GOBRA
