// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.utility

import org.bitbucket.inkytonik.kiama.rewriting.{CallbackRewriter, Cloner}

class Rewriter extends CallbackRewriter with Cloner {

  override def rewriting[T](oldTerm: T, newTerm: T): T = newTerm
}
