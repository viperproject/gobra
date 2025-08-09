// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.ast.frontend._
import viper.gobra.frontend.GobraParser.VOCABULARY

object TranslationHelpers {
  // Maps to get the fitting PNode from an expression string
  val binOp: Map[String, (PExpression, PExpression) => PExpression] = Map(
    VOCABULARY.getLiteralName(GobraParser.EQUALS) -> PEquals,
    VOCABULARY.getLiteralName(GobraParser.NOT_EQUALS) -> PUnequals,
    VOCABULARY.getLiteralName(GobraParser.GHOST_EQUALS) -> PGhostEquals,
    VOCABULARY.getLiteralName(GobraParser.GHOST_NOT_EQUALS) -> PGhostUnequals,
    VOCABULARY.getLiteralName(GobraParser.LESS) -> PLess,
    VOCABULARY.getLiteralName(GobraParser.LESS_OR_EQUALS) -> PAtMost,
    VOCABULARY.getLiteralName(GobraParser.GREATER) -> PGreater,
    VOCABULARY.getLiteralName(GobraParser.GREATER_OR_EQUALS) -> PAtLeast,
    VOCABULARY.getLiteralName(GobraParser.PLUS) -> PAdd,
    VOCABULARY.getLiteralName(GobraParser.MINUS) -> PSub,
    VOCABULARY.getLiteralName(GobraParser.OR) -> PBitOr,
    VOCABULARY.getLiteralName(GobraParser.CARET) -> PBitXor,
    VOCABULARY.getLiteralName(GobraParser.PLUS_PLUS) -> PSequenceAppend,
    VOCABULARY.getLiteralName(GobraParser.WAND) -> PMagicWand,
    VOCABULARY.getLiteralName(GobraParser.STAR) -> PMul,
    VOCABULARY.getLiteralName(GobraParser.DIV) -> PDiv,
    VOCABULARY.getLiteralName(GobraParser.MOD) -> PMod,
    VOCABULARY.getLiteralName(GobraParser.LSHIFT) -> PShiftLeft,
    VOCABULARY.getLiteralName(GobraParser.RSHIFT) -> PShiftRight,
    VOCABULARY.getLiteralName(GobraParser.AMPERSAND) -> PBitAnd,
    VOCABULARY.getLiteralName(GobraParser.BIT_CLEAR) -> PBitClear,
    VOCABULARY.getLiteralName(GobraParser.ELEM) -> PElem,
    VOCABULARY.getLiteralName(GobraParser.MULTI) -> PMultiplicity,
    VOCABULARY.getLiteralName(GobraParser.SUBSET) -> PSubset,
    VOCABULARY.getLiteralName(GobraParser.UNION) -> PUnion,
    VOCABULARY.getLiteralName(GobraParser.INTERSECTION) -> PIntersection,
    VOCABULARY.getLiteralName(GobraParser.SETMINUS) -> PSetMinus,
    VOCABULARY.getLiteralName(GobraParser.LOGICAL_AND) -> PAnd,
    VOCABULARY.getLiteralName(GobraParser.LOGICAL_OR) -> POr,
    VOCABULARY.getLiteralName(GobraParser.IMPLIES) -> PImplication
  )

  val unaryOp: Map[String, PExpression => PExpression] = Map(
    VOCABULARY.getLiteralName(GobraParser.CAP) -> PCapacity,
    VOCABULARY.getLiteralName(GobraParser.LEN) -> PLength,
    VOCABULARY.getLiteralName(GobraParser.DOM) -> PMapKeys,
    VOCABULARY.getLiteralName(GobraParser.RANGE) -> PMapValues,
    VOCABULARY.getLiteralName(GobraParser.EXCLAMATION) -> PNegation,
    VOCABULARY.getLiteralName(GobraParser.CARET) -> PBitNegation,
    VOCABULARY.getLiteralName(GobraParser.STAR) -> PDeref,
    VOCABULARY.getLiteralName(GobraParser.AMPERSAND) -> PReference,
    VOCABULARY.getLiteralName(GobraParser.RECEIVE) -> PReceive,
  )
}
