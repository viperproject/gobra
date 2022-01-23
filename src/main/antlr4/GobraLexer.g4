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

PERM: 'perm';
// END GOBRA
