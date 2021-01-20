// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend.PIdnNode
import viper.gobra.frontend.info.base.SymbolTable.{BoundVariable, BuiltInFPredicate, BuiltInMPredicate, GhostRegular, Predicate}
import viper.gobra.frontend.info.base.Type.{AssertionT, FunctionT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

trait GhostIdTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostRegular(entity: GhostRegular, id: PIdnNode): ValidityMessages = entity match {
    case _: BoundVariable => LocalMessages(noMessages)
    case predicate: Predicate => unsafeMessage(! {
      predicate.args.forall(wellDefMisc.valid)
    })

  }

  private[typing] def ghostEntityType(entity: GhostRegular, id: PIdnNode): Type = entity match {

    case x: BoundVariable => typeSymbType(x.decl.typ)
    case predicate: Predicate => FunctionT(predicate.args map predicate.context.typ, AssertionT)
    case BuiltInFPredicate(tag, _, _) => tag.typ(config)
    case BuiltInMPredicate(tag, _, _) => tag.typ(config)
    case _ => violation("untypable")
  }
}
