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
import viper.gobra.frontend.info.base.SymbolTable.NamedType
import viper.gobra.frontend.info.base.Type.{StructT, _}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.UnderlyingType

trait TypeTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val isType: WellDefinedness[PExpressionOrType] = createWellDef[PExpressionOrType] { n: PExpressionOrType =>
    val isTypeCondition = exprOrType(n).isRight
    error(n, s"expected type, but got $n", !isTypeCondition)
  }

  lazy val wellDefAndType: WellDefinedness[PType] = createWellDef { n =>
    wellDefType(n).out ++ isType(n).out
  }

  implicit lazy val wellDefType: WellDefinedness[PType] = createWellDef {
    case typ: PActualType => wellDefActualType(typ)
    case typ: PGhostType  => wellDefGhostType(typ)
  }

  private[typing] def wellDefActualType(typ: PActualType): Messages = typ match {

    case _: PBoolType | _: PIntegerType | _: PFloatType | _: PStringType | _: PPermissionType => noMessages

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

    case t: PInterfaceType =>
      val isRecursiveInterface = error(t, "invalid recursive interface", cyclicInterfaceDef(t))
      if (isRecursiveInterface.isEmpty) {
        addressableMethodSet(InterfaceT(t, this)).errors(t) ++
          containsRedeclarations(t) // temporary check
      } else {
        isRecursiveInterface
      }

    case t: PParameterizedType => entity(t.typeName.id) match {
      case NamedType(decl, _, _) =>
        wellDefFullIndexTypeArguments(t, decl, t.typeArgs)
    }

    case t: PExpressionAndType => wellDefExprAndType(t).out
  }

  lazy val typeSymbType: Typing[PType] = {
    // TODO: currently, this is required in order for Gobra to handle type alias to types from another package. This
    //       should be eventually generalized to all typing operations.
    def handleTypeAlias(t: Type): Type = t match {
      case DeclaredT(PTypeAlias(right, _), context) => context.symbType(right)
      case _ => t
    }
    createTyping {
      case typ: PActualType => handleTypeAlias(actualTypeSymbType(typ))
      case typ: PGhostType  => handleTypeAlias(ghostTypeSymbType(typ))
    }
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
    case PFloat32() => Float32T
    case PFloat64() => Float64T
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

    case t: PInterfaceType =>
      val res = InterfaceT(t, this)
      addDemandedEmbeddedInterfaceImplements(res)
      res

    case n: PNamedOperand => idSymType(n.id)

    case n: PDeref =>
      resolve(n) match {
        case Some(p: ap.PointerType) => PointerT(typeSymbType(p.base))
        case _ => violation(s"expected type, but got $n")
      }

    case n: PDot =>
      resolve(n) match {
        case Some(p: ap.NamedType) => DeclaredT(p.symb.decl, p.symb.context)

        // ADT clause is special since it is a type with a name that is not a named type
        case Some(p: ap.AdtClause) =>
          val fields = p.symb.fields.map(f => f.id.name -> p.symb.context.symbType(f.typ))
          AdtClauseT(fields.toMap, fields.map(_._1), p.symb.decl, p.symb.adtDecl, p.symb.context)

        case _ => violation(s"expected type, but got $n")
      }

    case n: PIndexedExp =>
      resolve(n) match {
        case Some(f@ap.Function(id, symb)) =>
          val typeArgs = f.typeArgs.map(typeSymbType)
          val substitution = symb.typeParameters.map(_.id).zip(typeArgs).toMap

          FunctionT(symb.args.map(miscType), miscType(symb.result)).substitute(substitution)

        case Some(t@ap.NamedType(id, symb)) if symb.decl.isInstanceOf[PTypeDef] =>
          val typeArgs = t.typeArgs.map(typeSymbType)
          val typeDecl = symb.decl.asInstanceOf[PTypeDef]
          val substitution = typeDecl.typeParameters.map(_.id).zip(typeArgs).toMap

          underlyingType(symbType(symb.decl.right)).substitute(substitution)
        case _ => violation(s"expected function or named type, but got $n")
      }

    case typ: PParameterizedType => entity(typ.typeName.id) match {
      case NamedType(decl, _, _) =>
        val typeArgs = typ.typeArgs map typeSymbType
        val substitution = decl.typeParameters.map(_.id).zip(typeArgs).toMap
        typeSymbType(decl.right).substitute(substitution)
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
    // this function is indirectly cached because `underlyingTypeWithCtxP` is cached.
    def isUnderlyingStructType(n: PUnqualifiedTypeName, ctx: UnderlyingType): Option[(PStructType, UnderlyingType)] = ctx.underlyingTypeWithCtxP(n) match {
      case Some((structT: PStructType, structCtx)) => Some(structT, structCtx)
      case _ => None
    }

    // `visitedTypes` keeps track of the types that were already discovered and checked,
    // used for detecting cyclic definition chains
    // `ctx` of type UnderlyingType represents the current context in which a lookup should happen
    // ExternalTypeInfo is not used as we need access to `underlyingTypeWithCtxP`, which is not exposed by the interface.
    def isCyclic(struct: PStructType, visitedTypes: Set[String], ctx: UnderlyingType): Boolean = {
      // the goal is to detect whether a struct type has infinity size and cause a corresponding error before
      // running into non-termination issues in Gobra.
      // Cycles in the field types are one possible source for infinite size of the resulting struct.
      // Another source are fields of (fixed-size) array type with the same element type as the struct.
      // Note however that pointers (and thus e.g. slices) are enough to break these cycles and thus a field with
      // pointer type to the current struct can be permitted and still results in a finite struct size.
      val fieldTypes = struct.fields.map(_.typ) ++ struct.embedded.map(_.typ.typ)
      fieldTypes exists {
        case s: PStructType => isCyclic(s, visitedTypes, this)
        case n: PUnqualifiedTypeName if visitedTypes.contains(n.name) => true
        case n: PUnqualifiedTypeName if isUnderlyingStructType(n, ctx).isDefined =>
          val (structT, structCtx) = isUnderlyingStructType(n, ctx).get
          isCyclic(structT, visitedTypes + n.name, structCtx)
        case PArrayType(_, elemT: PUnqualifiedTypeName) if visitedTypes.contains(elemT.name) => true
        case PArrayType(_, elemT: PUnqualifiedTypeName) if isUnderlyingStructType(elemT, ctx).isDefined =>
          val (structT, structCtx) = isUnderlyingStructType(elemT, ctx).get
          isCyclic(structT, visitedTypes + elemT.name, structCtx)
        // Qualified type names do not need to be handled because cycles are prevented by non-cyclic imports
        case _ => false
      }

    }

    isCyclic(struct, name.map(_.name).toSet, this)
  }

  /**
    * Checks whether an interface is cyclically defined in terms of itself (if its name is provided)
    * or other cyclic interfaces.
    */
  def cyclicInterfaceDef(itfT: PInterfaceType, name: Option[PIdnDef] = None): Boolean = {
    // `visitedTypes` keeps track of the types that were already discovered and checked,
    // used for detecting cyclic definition chains
    // `ctx` of type UnderlyingType represents the current context in which a lookup should happen
    // ExternalTypeInfo is not used as we need access to `underlyingTypeWithCtxP`, which is not exposed by the interface.
    def isCyclic(itfT: PInterfaceType, visitedTypes: Set[String], ctx: UnderlyingType): Boolean = {
      val fieldTypes = itfT.embedded.flatMap(_.terms)
      fieldTypes exists {
        case n: PUnqualifiedTypeName if visitedTypes.contains(n.name) => true
        case n: PUnqualifiedTypeName if isUnderlyingInterfaceType(n, ctx).isDefined =>
          val (itfT, itfCtx) = isUnderlyingInterfaceType(n, ctx).get
          isCyclic(itfT, visitedTypes + n.name, itfCtx)
        // Qualified type names do not need to be handled because cycles are prevented by non-cyclic imports
        case _ => false
      }
    }

    isCyclic(itfT, name.map(_.name).toSet, this)
  }

  /**
    * Checks whether an interface contains a redeclaration of an embedded method, which is currently not allowed.
    * We expect this to change in the near future by introducing support for refining the contract of an embedded field.
    * This method expects an acyclic interface type.
    */
  def containsRedeclarations(t: PInterfaceType): Messages = {
    def findAllEmbeddedMethods(itfT: PInterfaceType, ctx: UnderlyingType): Set[String] = {
      val fieldTypes = itfT.embedded.flatMap(_.terms).toSet
      fieldTypes.flatMap{
        case n: PTypeName if isUnderlyingInterfaceType(n, ctx).isDefined =>
          val (itfT, itfCtx) = isUnderlyingInterfaceType(n, ctx).get
          itfT.methSpecs.map(_.id.name) ++ findAllEmbeddedMethods(itfT, itfCtx)
        case _ =>
          // if the type is ill-formed and Gobra the previous case was not entered,
          // then we assume that another error will be reported while type-checking
          // this type
          Set.empty
      }
    }

    val allEmbeddedMethods = findAllEmbeddedMethods(t, this)
    t.methSpecs.flatMap { sig =>
      error(sig, s"interface redeclares embedded method ${sig.id.name}", allEmbeddedMethods.contains(sig.id.name))
    }

  }

  // this function is indirectly cached because `underlyingTypeWithCtxP` is cached.
  private def isUnderlyingInterfaceType(n: PTypeName, ctx: UnderlyingType): Option[(PInterfaceType, UnderlyingType)] = {
    ctx.underlyingTypeWithCtxP(n) match {
      case Some((itfT: PInterfaceType, itfCtx)) => Some(itfT, itfCtx)
      case _ => None
    }
  }
}
