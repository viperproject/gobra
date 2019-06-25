package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefType: WellDefinedness[PType] = createWellDef {
    case typ: PActualType => wellDefActualType(typ)
    case typ: PGhostType  => wellDefGhostType(typ)
  }

  private[typing] def wellDefActualType(typ: PActualType): Messages = typ match {

    case n@ PDeclaredType(id) => pointsToType.errors(id)(n)

    case _: PBoolType | _: PIntType => noMessages

    case n@PArrayType(len, _) =>
      message(n, s"expected constant array length but got $len", intConstantEval(len).isEmpty)

    case _: PSliceType | _: PPointerType |
         _: PBiChannelType | _: PSendChannelType | _: PRecvChannelType |
         _: PMethodReceiveName | _: PMethodReceivePointer | _: PFunctionType => noMessages

    case n@ PMapType(key, _) => message(n, s"map key $key is not comparable", !comparableType(typeType(key)))

    case t: PStructType => memberSet(StructT(t)).errors(t)

    case t: PInterfaceType => memberSet(InterfaceT(t)).errors(t)
  }

  lazy val typeType: Typing[PType] = createTyping {
    case typ: PActualType => actualTypeType(typ)
    case typ: PGhostType  => ghostTypeType(typ)
  }

  private[typing] def actualTypeType(typ: PActualType): Type = typ match {

    case PDeclaredType(id) => idType(id)

    case PBoolType() => BooleanT
    case PIntType() => IntT

    case PArrayType(len, elem) =>
      val lenOpt = intConstantEval(len)
      violation(lenOpt.isDefined, s"expected constant expression but got $len")
      ArrayT(lenOpt.get, typeType(elem))

    case PSliceType(elem) => SliceT(typeType(elem))

    case PMapType(key, elem) => MapT(typeType(key), typeType(elem))

    case PPointerType(elem) => PointerT(typeType(elem))

    case PBiChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Bi)

    case PSendChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Send)

    case PRecvChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Recv)

    case t: PStructType => StructT(t)

    case PMethodReceiveName(t) => typeType(t)

    case PMethodReceivePointer(t) => PointerT(typeType(t))

    case PFunctionType(args, r) => FunctionT(args map miscType, miscType(r))

    case t: PInterfaceType => InterfaceT(t)
  }
}
