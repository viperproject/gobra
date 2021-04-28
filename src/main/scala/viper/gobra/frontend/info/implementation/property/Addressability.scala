// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Constant, Variable, Wildcard}
import viper.gobra.frontend.info.base.Type.{ArrayT, MapT, SequenceT, SliceT, VariadicT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.theory.{Addressability => AddrMod}
import viper.gobra.util.Violation

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
    n => addressability(n) == AddrMod.Shared
  }

  /** checks if argument is addressable according to Go language specification */
  lazy val goAddressable: Property[PExpression] = createBinaryProperty("addressable") {
    case PNamedOperand(id) => entity(id).isInstanceOf[Variable]
    case n: PDeref => resolve(n).exists(_.isInstanceOf[ap.Deref])
    case PIndexedExp(b, _) => val bt = exprType(b); bt.isInstanceOf[SliceT] || (b.isInstanceOf[ArrayT] && goAddressable(b))
    case n: PDot => resolve(n) match {
      case Some(s: ap.FieldSelection) => goAddressable(s.base)
      case _ => false
    }
    case _ => false
  }

  /** returns addressability modifier of argument expression. See [[viper.gobra.theory.Addressability]] */
  override def addressability(expr: PExpression): AddrMod = addressabilityAttr(expr)

  private lazy val addressabilityAttr: PExpression => AddrMod =
    attr[PExpression, AddrMod] {
      case PNamedOperand(id) => addressableVar(id)
      case PBlankIdentifier() => AddrMod.defaultValue
      case _: PDeref => AddrMod.dereference
      case PIndexedExp(base, _) =>
        val baseType = exprType(base)
        baseType match {
          case _: SliceT => AddrMod.sliceLookup
          case _: VariadicT => AddrMod.variadicLookup
          case _: ArrayT => AddrMod.arrayLookup(addressability(base))
          case _: SequenceT => AddrMod.mathDataStructureLookup
          case _: MapT => AddrMod.mapLookup
          case t => Violation.violation(s"Expected slice, array, map, or sequence, but got $t")
        }
      case n: PDot => resolve(n) match {
        case Some(s: ap.FieldSelection) => AddrMod.fieldLookup(addressabilityMemberPath(addressability(s.base), s.path))
        case Some(_: ap.Constant) => AddrMod.constant
        case Some(_: ap.ReceivedMethod | _: ap.MethodExpr | _: ap.ReceivedPredicate | _: ap.PredicateExpr ) => AddrMod.rValue
        case Some(_: ap.NamedType | _: ap.Function | _: ap.Predicate | _: ap.DomainFunction) => AddrMod.rValue
        case Some(_: ap.ImplicitlyReceivedInterfaceMethod | _: ap.ImplicitlyReceivedInterfacePredicate) => AddrMod.rValue
        case p => Violation.violation(s"Unexpected dot resolve, got $p")
      }
      case _: PLiteral => AddrMod.literal
      case n: PInvoke => resolve(n) match {
        case Some(_: ap.Conversion) => AddrMod.conversionResult
        case Some(_: ap.FunctionCall) => AddrMod.callResult
        case Some(_: ap.PredicateCall) => AddrMod.rValue
        case p => Violation.violation(s"Unexpected invoke resolve, got $p")
      }
      case _: PLength | _: PCapacity => AddrMod.callResult
      case _: PSliceExp => AddrMod.sliceExpr
      case _: PTypeAssertion => AddrMod.typeAssertionResult
      case _: PReceive => AddrMod.receive
      case _: PReference => AddrMod.reference
      case _: PNegation => AddrMod.rValue
      case _: PBinaryExp[_,_] => AddrMod.rValue
      case _: PPermission => AddrMod.rValue
      case _: PPredConstructor => AddrMod.rValue
      case n: PUnfolding => AddrMod.unfolding(addressability(n.op))
      case _: POld | _: PLabeledOld => AddrMod.old
      case _: PConditional | _: PImplication | _: PForall | _: PExists => AddrMod.rValue
      case _: PAccess | _: PPredicateAccess => AddrMod.rValue
      case _: PTypeOf | _: PIsComparable => AddrMod.rValue
      case _: PIn | _: PCardinality | _: PMultiplicity | _: PSequenceAppend |
           _: PSequenceUpdate | _: PRangeSequence | _: PUnion | _: PIntersection |
           _: PSetMinus | _: PSubset => AddrMod.rValue
      case _: POptionNone | _: POptionSome | _: POptionGet => AddrMod.rValue
      case _: PSetConversion | _: PMultisetConversion | _: PSequenceConversion => AddrMod.conversionResult
      case _: PMake | _: PNew => AddrMod.make
      case _: PUnpackSlice => AddrMod.rValue
    }

  def addressabilityMemberPath(base: AddrMod, path: Vector[MemberPath]): AddrMod = {
    path.foldLeft(base){
      case (b, MemberPath.Underlying) => b
      case (b, _: MemberPath.Next) => AddrMod.fieldLookup(b)
      case (_, MemberPath.Deref) => AddrMod.dereference
      case (_, MemberPath.Ref) => AddrMod.reference
    }
  }

  /** returns addressability modifier of argument variable */
  override def addressableVar(id: PIdnNode): AddrMod = addressableVarAttr(id)

  private lazy val addressableVarAttr: PIdnNode => AddrMod =
    attr[PIdnNode, AddrMod] { n => regular(n) match {
      case v: Variable => if (v.addressable) AddrMod.sharedVariable else AddrMod.exclusiveVariable
      case _: Constant => AddrMod.constant
      case _: Wildcard => AddrMod.defaultValue
      case e => Violation.violation(s"Expected variable, but got $e")
    }}

  /** a parameter can be used as shared if it is included in the shared clause of the enclosing function or method */
  lazy val canParameterBeUsedAsShared: PParameter => Boolean =
    attr[PParameter, Boolean] {
      case n: PNamedParameter =>
        enclosingCodeRoot(n) match {
          case c: PMethodDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case c: PFunctionDecl => c.body.exists(_._1.shareableParameters.exists(_.name == n.id.name))
          case _ => false
        }
      case _: PUnnamedParameter => false
      case PExplicitGhostParameter(p) => canParameterBeUsedAsShared(p)
    }
}
