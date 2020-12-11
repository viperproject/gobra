// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.util.GobraExecutionContext

import scala.concurrent.Future

trait Backend[I, C, R, P, O] {
  def verify(id: I, config: C, reporter: R, program: P)(executor: GobraExecutionContext): Future[O]
}
