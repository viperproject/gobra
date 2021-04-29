// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation

trait MiscTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefMisc: WellDefinedness[PMisc] = createWellDef {
    case misc: PActualMisc => wellDefActualMisc(misc)
    case misc: PGhostMisc  => wellDefGhostMisc(misc)
  }

  private[typing] def wellDefActualMisc(misc: PActualMisc): Messages = misc match {

    case n@PRange(exp) => isExpr(exp).out ++ (exprType(exp) match {
      case _: ArrayT | PointerT(_: ArrayT) | _: SliceT | _: GhostSliceT |
           _: MapT | ChannelT(_, ChannelModus.Recv | ChannelModus.Bi) => noMessages
      case t => message(n, s"type error: got $t but expected rangeable type")
    })

    case n: PParameter => isType(n.typ).out
    case n: PReceiver => isType(n.typ).out
    case _: PResult => noMessages // children already taken care of

    case n: PEmbeddedName => isType(n.typ).out
    case n: PEmbeddedPointer => isType(n.typ).out

    case f: PFieldDecl => isType(f.typ).out ++ isNotPointerTypeP.errors(f.typ)(f)

    case n: PExpCompositeVal => isExpr(n.exp).out
    case _: PLiteralValue | _: PKeyedElement | _: PCompositeVal => noMessages // these are checked at the level of the composite literal
  }

  lazy val miscType: Typing[PMisc] = createTyping {
    case misc: PActualMisc => actualMiscType(misc)
    case misc: PGhostMisc  => ghostMiscType(misc)
  }

  private[typing] def actualMiscType(misc: PActualMisc): Type = misc match {

    case PRange(exp) => exprType(exp) match {
      case ArrayT(_, elem) => InternalSingleMulti(elem, InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case PointerT(ArrayT(_, elem)) => InternalSingleMulti(elem, InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case SliceT(elem) => InternalSingleMulti(elem, InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case GhostSliceT(elem) => InternalSingleMulti(elem, InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case MapT(key, elem) => InternalSingleMulti(key, InternalTupleT(Vector(key, elem)))
      case ChannelT(elem, ChannelModus.Recv | ChannelModus.Bi) => elem
      case t => violation(s"unexpected range type $t")
    }

    case p: PParameter => typeSymbType(p.typ)
    case r: PReceiver => typeSymbType(r.typ)
    case PResult(outs) =>
      if (outs.size == 1) miscType(outs.head) else InternalTupleT(outs.map(miscType))

    case PEmbeddedName(t) => typeSymbType(t)
    case PEmbeddedPointer(t) => PointerT(typeSymbType(t))

    case f: PFieldDecl => typeSymbType(f.typ)

    case l: PLiteralValue => expectedMiscType(l)
    case l: PKeyedElement => miscType(l.exp)
    case l: PExpCompositeVal => exprType(l.exp)
    case l: PLitCompositeVal => expectedMiscType(l)
  }

  lazy val expectedMiscType: PShortCircuitMisc => Type =
    attr[PShortCircuitMisc, Type] {

      case tree.parent.pair(_: PLiteralValue, p) => p match {
        case cl: PCompositeLit => expectedCompositeLitType(cl)
        case cv: PCompositeVal => expectedMiscType(cv)
        case _ => Violation.violation(s"found unexpected literal: $p")
      }

      case tree.parent.pair(e: PKeyedElement, lv: PLiteralValue) => underlyingType(expectedMiscType(lv)) match {
        case t: ArrayT => t.elem
        case t: SliceT => t.elem
        case t: GhostSliceT => t.elem
        case t: MapT  => t.elem
        case t: StructT =>
          e.key match {
            case Some(k: PIdentifierKey) =>
              val fieldOpt = t.fields.find(_._1 == k.id.name)
              fieldOpt.map(_._2).getOrElse(UnknownType)

            case _ =>
              val idx = lv.elems.indexOf(e)
              val fieldOpt = t.fields.values.toVector.lift(idx)
              fieldOpt.getOrElse(UnknownType)
          }
        case t => Violation.violation(s"found unexpected type: $t")
      }

      case tree.parent.pair(_: PCompositeVal, ke: PKeyedElement) => expectedMiscType(ke)

      case c => Violation.violation(s"This case should be unreachable, but got $c")
    }



  // received member type
  lazy val memberType: TypeMember => Type = // TODO: maybe merge with idType
    attr[TypeMember, Type] {
      case mt: ActualTypeMember => actualMemberType(mt)
      case mt: GhostTypeMember  => ghostMemberType(mt)
    }

  /** extends a function type by adding a type as the first argument type **/
  def extendFunctionType(functionT: FunctionT, base: Type): Type = FunctionT(base +: functionT.args, functionT.result)

  private[typing] def actualMemberType(typeMember: ActualTypeMember): Type = typeMember match {

    case MethodImpl(PMethodDecl(_, _, args, result, _, _), _, context) => FunctionT(args map context.typ, context.typ(result))

    case MethodSpec(PMethodSig(_, args, result, _, _), _, _, context) => FunctionT(args map context.typ, context.typ(result))

    case Field(PFieldDecl(_, typ), _, context) => context.symbType(typ)

    case Embbed(PEmbeddedDecl(typ, _), _, context) => context.typ(typ)

    case BuiltInMethod(tag, _, _) => tag.typ(config)
  }
}
