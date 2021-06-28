// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.util

sealed abstract class NumBase(val base: Int)
object Binary extends NumBase(2)
object Octal extends NumBase(8)
object Hexadecimal extends NumBase(16)
object Decimal extends NumBase(10)
