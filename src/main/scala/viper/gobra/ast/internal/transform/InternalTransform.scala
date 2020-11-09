// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.internal.{Member, Program}

/**
  * Encodes endo-transformations of programs written in Gobra's internal language
  */
trait InternalTransform {
  def name(): String

  /**
    * Program-to-program transformation
    */
  def transform(p: Program): Program

  /**
    * Lifts a member-to-member transformation to a program-to-program transformation
    */
  def transformMembers(memberTrans: Member => Member): Program => Program = {
    case p@Program(types, members, table) => Program(types, members map memberTrans, table)(p.info)
  }
}
