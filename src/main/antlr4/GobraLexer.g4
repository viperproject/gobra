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
GHOST       : 'ghost';
IN          : 'in';
MULTI       : '#';
SUBSET      : 'subset';
UNION       : 'union';
INTERSECTION: 'intersectio';
SETMINUS    : 'setminus';
IMPLIES     : '==>';
QMARK       : '?';
RANGE       : 'range';
SEQ         : 'seq';
SET         : 'set';
MSET        : 'mset';
PRED        : 'pred';
TYPE_OF      : 'typeOf';
IS_COMPARABLE: 'isComparable';
ADDR_MOD    : '@';
DOT_DOT     : '..';
SHARED      : 'shared';
EXCLUSIVE   : 'exclusive';
PREDICATE   : 'predicate';
// END GOBRA
