// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GoStringUnitTests extends AnyFunSuite with Matchers {
  test("GoString: raw literals are UTF-8 encoded and carriage returns are discarded") {
    GoString.fromRawLiteral("é\r\\n").bytes should be (Vector(0xc3.toByte, 0xa9.toByte, '\\'.toByte, 'n'.toByte))
  }

  test("GoString: interpreted literals distinguish byte and Unicode escapes") {
    GoString.fromInterpretedLiteral("\\xff") should be (Right(GoString(Vector(0xff.toByte))))
    GoString.fromInterpretedLiteral("\\u00ff") should be (Right(GoString(Vector(0xc3.toByte, 0xbf.toByte))))
  }

  test("GoString: equivalent literal spellings have equal byte representations") {
    val expected = GoString.fromRawLiteral("a")
    GoString.fromInterpretedLiteral("a") should be (Right(expected))
    GoString.fromInterpretedLiteral("\\x61") should be (Right(expected))
    GoString.fromInterpretedLiteral("\\u0061") should be (Right(expected))
  }

  test("GoString: quoted returns a canonical lossless Go literal") {
    GoString(Vector('a'.toByte, '\n'.toByte, '"'.toByte, '\\'.toByte, 0xff.toByte)).quoted should be
      ("\"a\\n\\\"\\\\\\xff\"")
  }
}
