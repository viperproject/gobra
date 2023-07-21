// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Constant, GlobalVariable, Variable, Wildcard}
import viper.gobra.frontend.info.base.Type.{ArrayT, GhostSliceT, MapT, MathMapT, SequenceT, SliceT, VariadicT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}

trait Addressability extends BaseProperty { this: TypeInfoImpl =>

  lazy val effAddressable: Property[PExpression] = createBinaryProperty("effective addressable") {
    case _: PCompositeLit => true
    case e => addressable(e)
  }

  lazy val goEffAddressable: Property[PExpression] = createBinaryProperty("effective addressable") {
    case _: PCompositeLit => true
    case e => goAddressable(e)
  }

  // depends on: entity, type
  lazy val addressable: Property[PExpression] = createBinaryProperty("addressable") {
    n => this.modifierUnits.forall(_.addressable(n))
  }

  /** checks if argument is addressable according to Go language specification */
  lazy val goAddressable: Property[PExpression] = createBinaryProperty("addressable") {
    case PNamedOperand(id) => entity(id).isInstanceOf[Variable]
    case n: PDeref => resolve(n).exists(_.isInstanceOf[ap.Deref])
    case PIndexedExp(b, _) =>
      val bt = underlyingType(exprType(b))
      bt.isInstanceOf[SliceT] || bt.isInstanceOf[GhostSliceT] || (bt.isInstanceOf[ArrayT] && goAddressable(b))
    case n: PDot => resolve(n) match {
      case Some(s: ap.FieldSelection) => goAddressable(s.base)
      case Some(_: ap.GlobalVariable) => true
      case _ => false
    }
    case _ => false
  }

  /** a parameter can be used as shared if it is included in the shared clause of the enclosing function or method */
  lazy val canParameterBeUsedAsShared: PParameter => Boolean =
    attr[PParameter, Boolean] {
      case n: PNamedParameter =>
        enclosingCodeRoot(n) match {
          case c: PMethodDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case c: PFunctionDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case c: PClosureDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case _ => false
        }
      case _: PUnnamedParameter => false
      case PExplicitGhostParameter(p) => canParameterBeUsedAsShared(p)
    }

  /** a receiver can be used as shared if it is included in the shared clause of the enclosing function or method */
  lazy val canReceiverBeUsedAsShared: PReceiver => Boolean =
    attr[PReceiver, Boolean] {
      case n: PNamedReceiver =>
        enclosingCodeRoot(n) match {
          case c: PMethodDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case c: PFunctionDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case _ => false
        }
      case _: PUnnamedReceiver => false
    }
}
