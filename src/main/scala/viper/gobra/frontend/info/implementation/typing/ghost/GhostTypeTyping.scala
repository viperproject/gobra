// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostTypeTyping extends BaseTyping { this : TypeInfoImpl =>

  private[typing] def wellDefGhostType(typ : PGhostType) : Messages = typ match {
    case PSequenceType(elem) => isType(elem).out
    case PSetType(elem) => isType(elem).out
    case PMultisetType(elem) => isType(elem).out
    case PMathematicalMapType(key, value) => isType(key).out ++ isType(value).out
    case POptionType(elem) => isType(elem).out
    case n: PGhostSliceType => isType(n.elem).out

    case _: PDomainType => noMessages
    case _: PAdtType => noMessages
  }

  private[typing] def ghostTypeSymbType(typ : PGhostType) : Type = typ match {
    case PSequenceType(elem) => SequenceT(typeSymbType(elem))
    case PSetType(elem) => SetT(typeSymbType(elem))
    case PMultisetType(elem) => MultisetT(typeSymbType(elem))
    case PMathematicalMapType(keys, values) => MathMapT(typeSymbType(keys), typeSymbType(values))
    case POptionType(elem) => OptionT(typeSymbType(elem))
    case PGhostSliceType(elem) => GhostSliceT(typeSymbType(elem))
    case t: PDomainType => DomainT(t, this)
    case a: PAdtType => AdtT(a, this)
  }
}
