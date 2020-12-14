// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import scala.collection.immutable.ListMap
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.{StructT, _}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val isType: WellDefinedness[PExpressionOrType] = createWellDef[PExpressionOrType] { n: PExpressionOrType =>
    val isTypeCondition = exprOrType(n).isRight
    message(n, s"expected expression, but got $n", !isTypeCondition)
  }

  lazy val wellDefAndType: WellDefinedness[PType] = createWellDef { n =>
    wellDefType(n).out ++ isType(n).out
  }

  implicit lazy val wellDefType: WellDefinedness[PType] = createWellDef {
    case typ: PActualType => wellDefActualType(typ)
    case typ: PGhostType  => wellDefGhostType(typ)
  }

  private[typing] def wellDefActualType(typ: PActualType): Messages = typ match {

    case _: PBoolType | _: PIntegerType => noMessages

    case typ @ PArrayType(_, PNamedOperand(_)) =>
      message(typ, s"arrays of custom declared types are currently not supported")

    case n @ PArrayType(len, t) => isType(t).out ++ {
      intConstantEval(len) match {
        case None => message(n, s"expected constant array length, but got $len")
        case Some(v) => message(len, s"array length should be positive, but got $v", v < 0)
      }
    }

    case n: PSliceType => isType(n.elem).out
    case n: PBiChannelType => isType(n.elem).out
    case n: PSendChannelType => isType(n.elem).out
    case n: PRecvChannelType => isType(n.elem).out
    case n: PMethodReceiveName => isType(n.typ).out
    case n: PMethodReceivePointer => isType(n.typ).out
    case n: PFunctionType => noMessages // parameters and result is implied by well definedness of children

    case n@ PMapType(key, elem) => isType(key).out ++ isType(elem).out ++
      message(n, s"map key $key is not comparable", !comparableType(typeType(key)))

    case t: PStructType =>
      t.embedded.flatMap(e => isNotPointerTypePE.errors(e.typ)(e)) ++
      t.fields.flatMap(f => isType(f.typ).out ++ isNotPointerTypeP.errors(f.typ)(f)) ++
      structMemberSet(structType(t)).errors(t) ++ addressableMethodSet(structType(t)).errors(t) ++
      message(t, "invalid recursive struct", cyclicStructDef(t))

    case t: PInterfaceType => addressableMethodSet(InterfaceT(t)).errors(t)

    case t: PExpressionAndType => wellDefExprAndType(t).out
  }

  lazy val typeType: Typing[PType] = createTyping {
    case typ: PActualType => actualTypeType(typ)
    case typ: PGhostType  => ghostTypeType(typ)
  }

  private[typing] def actualTypeType(typ: PActualType): Type = typ match {

    case PBoolType() => BooleanT
    case PIntType() => IntT(config.typeBounds.Int)
    case PInt8Type() => IntT(config.typeBounds.Int8)
    case PInt16Type() => IntT(config.typeBounds.Int16)
    case PInt32Type() => IntT(config.typeBounds.Int32)
    case PInt64Type() => IntT(config.typeBounds.Int64)
    case PRune() => IntT(config.typeBounds.Rune)
    case PUIntType() => IntT(config.typeBounds.UInt)
    case PUInt8Type() => IntT(config.typeBounds.UInt8)
    case PUInt16Type() => IntT(config.typeBounds.UInt16)
    case PUInt32Type() => IntT(config.typeBounds.UInt32)
    case PUInt64Type() => IntT(config.typeBounds.UInt64)
    case PByte() => IntT(config.typeBounds.Byte)
    case PUIntPtr() => IntT(config.typeBounds.UIntPtr)

    case PArrayType(len, elem) =>
      val lenOpt = intConstantEval(len)
      violation(lenOpt.isDefined, s"expected constant expression, but got $len")
      ArrayT(lenOpt.get, typeType(elem))

    case PSliceType(elem) => SliceT(typeType(elem))

    case PMapType(key, elem) => MapT(typeType(key), typeType(elem))

    case PBiChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Bi)

    case PSendChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Send)

    case PRecvChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Recv)

    case t: PStructType => structType(t)

    case PMethodReceiveName(t) => typeType(t)

    case PMethodReceivePointer(t) => PointerT(typeType(t))

    case PFunctionType(args, r) => FunctionT(args map miscType, miscType(r))

    case t: PInterfaceType => InterfaceT(t)

    case t: PExpressionAndType => exprAndTypeType(t)
  }

  def litTypeType(typ: PLiteralType): Type = typ match {
    case PImplicitSizeArrayType(t) => typeType(t)
    case t: PType => typeType(t)
  }

  private def structType(t: PStructType): Type = {
    def makeFields(x: PFieldDecls): ListMap[String, (Boolean, Type)] = {
      x.fields.foldLeft(ListMap[String, (Boolean, Type)]()) { case (prev, f) => prev + (f.id.name -> (true, typeType(f.typ))) }
    }
    def makeEmbedded(x: PEmbeddedDecl): ListMap[String, (Boolean, Type)] =
      ListMap[String, (Boolean, Type)](x.id.name -> (false, miscType(x.typ)))

    val clauses = t.clauses.foldLeft(ListMap[String, (Boolean, Type)]()) {
      case (prev, x: PFieldDecls) => prev ++ makeFields(x)
      case (prev, PExplicitGhostStructClause(x: PFieldDecls)) => prev ++ makeFields(x)
      case (prev, x: PEmbeddedDecl) => prev ++ makeEmbedded(x)
      case (prev, PExplicitGhostStructClause(x: PEmbeddedDecl)) => prev ++ makeEmbedded(x)
    }
    StructT(clauses, t, this)
  }

  /**
    * Checks whether a struct is cyclically defined in terms of itself (if its name is provided)
    * or other cyclic structures.
    */
  def cyclicStructDef(struct: PStructType, name: Option[PIdnDef] = None) : Boolean = {
    // `visitedTypes` keeps track of the types that were already discovered and checked,
    // used for detecting cyclic definition chains
    def isCyclic: (PStructType, Set[String]) => Boolean = (struct, visitedTypes) => {
      val fieldTypes = struct.fields.map(_.typ) ++ struct.embedded.map(_.typ.typ)

      fieldTypes exists {
        case s: PStructType => isCyclic(s, visitedTypes)
        case n: PNamedType if visitedTypes.contains(n.name) => true
        case n: PNamedType if underlyingTypeP(n).exists(_.isInstanceOf[PStructType]) =>
          isCyclic(underlyingTypeP(n).get.asInstanceOf[PStructType], visitedTypes + n.name)
        case _ => false
      }
    }

    isCyclic(struct, name.map(_.name).toSet)
  }
}
