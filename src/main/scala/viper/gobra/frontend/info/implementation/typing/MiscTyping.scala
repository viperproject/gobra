package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait MiscTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefMisc: WellDefinedness[PMisc] = createWellDef {
    case misc: PActualMisc => wellDefActualMisc(misc)
    case misc: PGhostMisc  => wellDefGhostMisc(misc)
  }

  private[typing] def wellDefActualMisc(misc: PActualMisc): Messages = misc match {

    case n@PRange(exp) => exprType(exp) match {
      case _: ArrayT | PointerT(_: ArrayT) | _: SliceT |
           _: MapT | ChannelT(_, ChannelModus.Recv | ChannelModus.Bi) => noMessages
      case t => message(n, s"type error: got $t but expected rangable type")
    }

    case _: PParameter | _: PReceiver | _: PResult | _: PEmbeddedType => noMessages

  }

  lazy val miscType: Typing[PMisc] = createTyping {
    case misc: PActualMisc => actualMiscType(misc)
    case misc: PGhostMisc  => ghostMiscType(misc)
  }

  private[typing] def actualMiscType(misc: PActualMisc): Type = misc match {

    case PRange(exp) => exprType(exp) match {
      case ArrayT(_, elem) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
      case PointerT(ArrayT(len, elem)) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
      case SliceT(elem) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
      case MapT(key, elem) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
      case ChannelT(elem, ChannelModus.Recv | ChannelModus.Bi) => elem
      case t => violation(s"unexpected range type $t")
    }

    case p: PParameter => typeType(p.typ)
    case r: PReceiver => typeType(r.typ)
    case PVoidResult() => VoidType
    case PResultClause(outs) =>
      if (outs.size == 1) miscType(outs.head) else InternalTupleT(outs.map(miscType))

    case PEmbeddedName(t) => typeType(t)
    case PEmbeddedPointer(t) => PointerT(typeType(t))
  }

  lazy val memberType: TypeMember => Type =
    attr[TypeMember, Type] {
      case mt: ActualTypeMember => actualMemberType(mt)
      case mt: GhostTypeMember  => ghostMemberType(mt)
    }

  private[typing] def actualMemberType(typeMember: ActualTypeMember): Type = typeMember match {

    case MethodImpl(PMethodDecl(_, _, args, result, _), _) => FunctionT(args map miscType, miscType(result))

    case MethodSpec(PMethodSig(_, args, result), _) => FunctionT(args map miscType, miscType(result))

    case Field(PFieldDecl(_, typ), _) => typeType(typ)

    case Embbed(PEmbeddedDecl(typ, _), _) => miscType(typ)
  }
}
