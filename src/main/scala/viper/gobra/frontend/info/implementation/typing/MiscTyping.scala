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

    case n@PRange(exp, _) => isExpr(exp).out ++ (underlyingType(exprType(exp)) match {
      case _: ArrayT | PointerT(_: ArrayT) | _: SliceT | _: GhostSliceT | _: MapT |
          ChannelT(_, ChannelModus.Recv | ChannelModus.Bi) => noMessages
      case t => message(n, s"type error: got $t but expected rangeable type")
    })

    case n: PParameter => isType(n.typ).out
    case n: PReceiver => isType(n.typ).out
    case _: PResult => noMessages // children already taken care of

    case n: PEmbeddedName => isType(n.typ).out
    case n: PEmbeddedPointer => isType(n.typ).out

    case n: PExpCompositeVal => isExpr(n.exp).out
    case _: PLiteralValue | _: PKeyedElement | _: PCompositeVal => noMessages // these are checked at the level of the composite literal

    case _: PClosureDecl => noMessages // checks are done at the PFunctionLit level

    case n: PMethodImplementationProof =>
      val validPureCheck = wellDefIfPureMethodImplementationProof(n)
      if (validPureCheck.nonEmpty) validPureCheck
      else {
        entity(n.id) match {
          case spec: MethodSpec =>
            // check that the signatures match
            val matchingSignature = {
              val implSig = FunctionT(n.args map miscType, miscType(n.result))
              val specSig = memberType(spec)
              failedProp(
                s"implementation proof and interface member have a different signature (should be '$specSig', but is $implSig nad ${implSig == specSig})",
                cond = !identicalTypes(implSig, specSig)
              )
            }
            // check that pure annotations match
            val matchingPure = failedProp(
              s"The pure annotation does not match with the pure annotation of the interface member",
              cond = n.isPure != spec.isPure
            )
            // check that the receiver has the method
            val receiverHasMethod = failedProp(
              s"The type ${n.receiver.typ} does not have member ${n.id}",
              cond = tryMethodLikeLookup(miscType(n.receiver), n.id).isEmpty
            )
            // check that the body has the right shape
            val rightShape = {
              n.body match {
                case None => failedProp("A method in an implementation proof must not be abstract")
                case Some((_, block)) =>

                  val expectedReceiverOpt = n.receiver match {
                    case _: PUnnamedParameter => None
                    case p: PNamedParameter => Some(PNamedOperand(PIdnUse(p.id.name)))
                    case PExplicitGhostParameter(_: PUnnamedParameter) => None
                    case PExplicitGhostParameter(p: PNamedParameter) => Some(PNamedOperand(PIdnUse(p.id.name)))
                  }

                  val expectedArgs = n.args.flatMap {
                    case p: PNamedParameter => Some(PNamedOperand(PIdnUse(p.id.name)))
                    case PExplicitGhostParameter(p: PNamedParameter) => Some(PNamedOperand(PIdnUse(p.id.name)))
                    case _ => None
                  }

                  if (expectedReceiverOpt.isEmpty || expectedArgs.size != n.args.size) {
                    failedProp("Receiver and arguments must be named so that they can be used in a call")
                  } else {
                    val expectedReceiver = expectedReceiverOpt.getOrElse(violation(""))
                    val expectedInvoke = PInvoke(PDot(expectedReceiver, n.id), expectedArgs, None)

                    if (n.isPure) {
                      block.nonEmptyStmts match {
                        case Vector(PReturn(Vector(ret))) =>
                          pureImplementationProofHasRightShape(ret, _ == expectedInvoke, expectedInvoke.toString)

                        case _ => successProp // already checked before
                      }
                    } else {
                      implementationProofBodyHasRightShape(block, _ == expectedInvoke, expectedInvoke.toString, n.result)
                    }
                  }
              }
            }

            (matchingSignature and matchingPure and receiverHasMethod and rightShape)
              .asReason(n, "invalid method of an implementation proof")

          case e => Violation.violation(s"expected a method signature of an interface, but got $e")
        }
      }
  }

  lazy val miscType: Typing[PMisc] = createTyping {
    case misc: PActualMisc => actualMiscType(misc)
    case misc: PGhostMisc  => ghostMiscType(misc)
  }

  private[typing] def actualMiscType(misc: PActualMisc): Type = misc match {

    case PRange(exp, _) => underlyingType(exprType(exp)) match {
      case ArrayT(_, elem) => InternalSingleMulti(IntT(config.typeBounds.Int), InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case PointerT(ArrayT(_, elem)) => InternalSingleMulti(IntT(config.typeBounds.Int), InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case SliceT(elem) => InternalSingleMulti(IntT(config.typeBounds.Int), InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case GhostSliceT(elem) => InternalSingleMulti(IntT(config.typeBounds.Int), InternalTupleT(Vector(IntT(config.typeBounds.Int), elem)))
      case MapT(key, elem) => InternalSingleMulti(key, InternalTupleT(Vector(key, elem)))
      case ChannelT(elem, ChannelModus.Recv | ChannelModus.Bi) => elem
      case t => violation(s"unexpected range type $t")
    }

    case p: PParameter => typeSymbType(p.typ)
    case r: PReceiver => typeSymbType(r.typ)
    case PResult(outs) =>
      if (outs.size == 1) miscType(outs.head) else InternalTupleT(outs.map(miscType))

    case PClosureDecl(args, res, _, _)  => FunctionT(args.map(typ), miscType(res))

    case PEmbeddedName(t) => typeSymbType(t)
    case PEmbeddedPointer(t) => ActualPointerT(typeSymbType(t))

    case l: PLiteralValue => expectedMiscType(l)
    case l: PKeyedElement => miscType(l.exp)
    case l: PExpCompositeVal => exprType(l.exp)
    case l: PLitCompositeVal => expectedMiscType(l)

    case _: PMethodImplementationProof => UnknownType
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

    case BuiltInMethod(tag, _, _) => typ(tag)
  }
}
