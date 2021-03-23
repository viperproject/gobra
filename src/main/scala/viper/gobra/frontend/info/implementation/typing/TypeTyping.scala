// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import scala.collection.immutable.ListMap
import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.Type.{StructT, _}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val isType: WellDefinedness[PExpressionOrType] = createWellDef[PExpressionOrType] { n: PExpressionOrType =>
    val isTypeCondition = exprOrType(n).isRight
    error(n, s"expected expression, but got $n", !isTypeCondition)
  }

  lazy val wellDefAndType: WellDefinedness[PType] = createWellDef { n =>
    wellDefType(n).out ++ isType(n).out
  }

  implicit lazy val wellDefType: WellDefinedness[PType] = createWellDef {
    case typ: PActualType => wellDefActualType(typ)
    case typ: PGhostType  => wellDefGhostType(typ)
  }

  private[typing] def wellDefActualType(typ: PActualType): Messages = typ match {

    case _: PBoolType | _: PIntegerType | _: PStringType | _: PPermissionType => noMessages

    case typ @ PArrayType(_, PNamedOperand(_)) =>
      error(typ, s"arrays of custom declared types are currently not supported")

    case n @ PArrayType(len, t) => isType(t).out ++ {
      intConstantEval(len) match {
        case None => error(n, s"expected constant array length, but got $len")
        case Some(v) => error(len, s"array length should be positive, but got $v", v < 0)
      }
    }

    case n: PVariadicType => isType(n.elem).out
    case n: PSliceType => isType(n.elem).out
    case n: PBiChannelType => isType(n.elem).out
    case n: PSendChannelType => isType(n.elem).out
    case n: PRecvChannelType => isType(n.elem).out
    case n: PMethodReceiveName => isType(n.typ).out
    case n: PMethodReceivePointer => isType(n.typ).out
    case _: PFunctionType => noMessages // parameters and result is implied by well definedness of children
    case _: PPredType => noMessages // well definedness implied by well definedness of children

    case n@ PMapType(key, elem) => isType(key).out ++ isType(elem).out ++
      error(n, s"map key $key is not comparable", !comparableType(typeSymbType(key)))

    case t: PStructType =>
      t.embedded.flatMap(e => isNotPointerTypePE.errors(e.typ)(e)) ++
      t.fields.flatMap(f => isType(f.typ).out ++ isNotPointerTypeP.errors(f.typ)(f)) ++
      structMemberSet(structSymbType(t)).errors(t) ++ addressableMethodSet(structSymbType(t)).errors(t) ++
      error(t, "invalid recursive struct", cyclicStructDef(t))

    case t: PInterfaceType => addressableMethodSet(InterfaceT(t, this)).errors(t)

    case t: PExpressionAndType => wellDefExprAndType(t).out
  }

  lazy val typeSymbType: Typing[PType] = createTyping {
    case typ: PActualType => actualTypeSymbType(typ)
    case typ: PGhostType  => ghostTypeSymbType(typ)
  }

  private[typing] def actualTypeSymbType(typ: PActualType): Type = typ match {

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
    case PStringType() => StringT
    case PPermissionType() => PermissionT

    case PArrayType(len, elem) =>
      val lenOpt = intConstantEval(len)
      violation(lenOpt.isDefined, s"expected constant expression, but got $len")
      ArrayT(lenOpt.get, typeSymbType(elem))

    case PSliceType(elem) => SliceT(typeSymbType(elem))

    case PVariadicType(elem) => VariadicT(typeSymbType(elem))

    case PMapType(key, elem) => MapT(typeSymbType(key), typeSymbType(elem))

    case PBiChannelType(elem) => ChannelT(typeSymbType(elem), ChannelModus.Bi)

    case PSendChannelType(elem) => ChannelT(typeSymbType(elem), ChannelModus.Send)

    case PRecvChannelType(elem) => ChannelT(typeSymbType(elem), ChannelModus.Recv)

    case t: PStructType => structSymbType(t)

    case PMethodReceiveName(t) => typeSymbType(t)

    case PMethodReceivePointer(t) => PointerT(typeSymbType(t))

    case PFunctionType(args, r) => FunctionT(args map miscType, miscType(r))

    case PPredType(args) => PredT(args map typeSymbType)

    case t: PInterfaceType => InterfaceT(t, this)

    case n: PNamedOperand => idSymType(n.id)

    case n: PDeref =>
      resolve(n) match {
        case Some(p: ap.PointerType) => PointerT(typeSymbType(p.base))
        case _ => violation(s"expected type, but got $n")
      }

    case n: PDot =>
      resolve(n) match {
        case Some(p: ap.NamedType) => DeclaredT(p.symb.decl, p.symb.context)
        case _ => violation(s"expected type, but got $n")
      }
  }

  private def structSymbType(t: PStructType): Type = {
    def makeFields(x: PFieldDecls): ListMap[String, (Boolean, Type)] = {
      x.fields.foldLeft(ListMap[String, (Boolean, Type)]()) { case (prev, f) => prev + (f.id.name -> (true, typeSymbType(f.typ))) }
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
