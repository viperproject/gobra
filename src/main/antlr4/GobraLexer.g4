// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

lexer grammar GobraLexer;
import GoLexer;

FLOAT_LIT : (DECIMAL_FLOAT_LIT | HEX_FLOAT_LIT) -> mode(NLSEMI);

// Add lookahead to avoid parsing range expressions like '[1..3]' as two floats '1.' and '.3'
DECIMAL_FLOAT_LIT      : DECIMALS ('.'{_input.LA(1) != '.'}? DECIMALS? EXPONENT? | EXPONENT)
                       | '.'{_input.index() <2 || _input.LA(-2) != '.'}? DECIMALS EXPONENT?
                       ;

// ->mode(NLSEMI) means line breaks directly after this token
// emit a semicolon. (just like after identifiers, literals, ')}]' etc in base Go)

TRUE        : 'true' -> mode(NLSEMI);
FALSE       : 'false' -> mode(NLSEMI);
ASSERT      : 'assert';
ASSUME      : 'assume';
INHALE      : 'inhale';
EXHALE      : 'exhale';
PRE         : 'requires';
PRESERVES   : 'preserves';
POST        : 'ensures';
INV         : 'invariant';
DEC         : 'decreases' -> mode(NLSEMI);
PURE        : 'pure' -> mode(NLSEMI);
IMPL        : 'implements';
AS          : 'as';
OLD         : 'old'-> mode(NLSEMI);
BEFORE      : 'before'-> mode(NLSEMI);
LHS         : '#lhs';
FORALL      : 'forall';
EXISTS      : 'exists';
ACCESS      : 'acc' -> mode(NLSEMI);
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
R_PRED      : '!>' -> mode(NLSEMI);
SEQ         : 'seq'-> mode(NLSEMI);
SET         : 'set'-> mode(NLSEMI);
MSET        : 'mset'-> mode(NLSEMI);
DICT        : 'dict'-> mode(NLSEMI);
OPT         : 'option'-> mode(NLSEMI);
LEN         : 'len'-> mode(NLSEMI);
NEW         : 'new'-> mode(NLSEMI);
MAKE        : 'make'-> mode(NLSEMI);
CAP         : 'cap'-> mode(NLSEMI);
SOME        : 'some'-> mode(NLSEMI);
GET         : 'get'-> mode(NLSEMI);
DOM         : 'domain'-> mode(NLSEMI);
AXIOM       : 'axiom'-> mode(NLSEMI);
NONE        : 'none' -> mode(NLSEMI);
PRED        : 'pred';
TYPE_OF      : 'typeOf'-> mode(NLSEMI);
IS_COMPARABLE: 'isComparable'-> mode(NLSEMI);
SHARE       : 'share';
ADDR_MOD    : '@'-> mode(NLSEMI);
DOT_DOT     : '..';
SHARED      : 'shared';
EXCLUSIVE   : 'exclusive';
PREDICATE   : 'predicate';
WRITEPERM   : 'writePerm' -> mode(NLSEMI);
NOPERM      : 'noPerm' -> mode(NLSEMI);
TRUSTED     : 'trusted' -> mode(NLSEMI);
OUTLINE     : 'outline';
PROOF       : 'proof';