// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PBlock, PCodeRootWithResult, PExplicitGhostMember, PFPredicateDecl, PFunctionDecl, PFunctionSpec, PGhostMember, PIdnUse, PImplementationProof, PMPredicateDecl, PMethodDecl, PMethodImplementationProof, PParameter, PReturn, PVariadicType, PWithBody}
import viper.gobra.frontend.info.base.SymbolTable.{MPredicateSpec, MethodImpl, MethodSpec}
import viper.gobra.frontend.info.base.Type.{InterfaceT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMemberTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMember(member: PGhostMember): Messages = member match {
    case PExplicitGhostMember(_) => noMessages

    case PFPredicateDecl(_, args, body) =>
      body.fold(noMessages)(assignableToSpec) ++ nonVariadicArguments(args)

    case PMPredicateDecl(_, receiver, args, body) =>
      body.fold(noMessages)(assignableToSpec) ++
        isClassType.errors(miscType(receiver))(member) ++
        nonVariadicArguments(args)

    case ip: PImplementationProof =>
      val subType = symbType(ip.subT)
      val superType = symbType(ip.superT)

      val syntaxImplementsMsgs = syntaxImplements(subType, superType).asReason(ip, s"${ip.subT} does not implement the interface ${ip.superT}")
      if (syntaxImplementsMsgs.nonEmpty) syntaxImplementsMsgs
      else {
        addDemandedImplements(subType, superType)

        {
          val badReceiverTypes = ip.memberProofs.map(m => miscType(m.receiver))
            .filter(t => !identicalTypes(t, subType))
          error(ip, s"The receiver of all methods included in the implementation proof must be $subType, " +
            s"but encountered: ${badReceiverTypes.distinct.mkString(", ")}", cond = badReceiverTypes.nonEmpty)
        } ++ {
          val superPredNames = memberSet(superType).collect{ case (n, m: MPredicateSpec) => (n, m) }
          val allPredicatesDefined = PropertyResult.bigAnd(superPredNames.map{ case (name, symb) =>
            val valid = tryMethodLikeLookup(subType, PIdnUse(name)).isDefined ||
              ip.alias.exists(al => al.left.name == name)
            failedProp({
              val argTypes = symb.args map symb.context.typ

              s"predicate $name is not defined for type $subType. " +
                s"Either declare a predicate 'pred ($subType) $name(${argTypes.mkString(", ")})' " +
                s"or declare a predicate 'pred p($subType${if (argTypes.isEmpty) "" else ", "}${argTypes.mkString(", ")})' with some name p and add 'pred $name := p' to the implementation proof."
            }, !valid)
          })
          allPredicatesDefined.asReason(ip, "Some predicate definitions are missing")
        }
      }
  }

  private[typing] def wellDefIfPureMethod(member: PMethodDecl): Messages = {

    if (member.spec.isPure) {
      isSingleResultArg(member) ++
        isSinglePureReturnExpr(member) ++
        isPurePostcondition(member.spec) ++
        nonVariadicArguments(member.args)
    } else noMessages
  }

  private[typing] def wellDefIfPureMethodImplementationProof(implProof: PMethodImplementationProof): Messages = {
    if (implProof.isPure) {
      isSinglePureReturnExpr(implProof) // all other checks are taken care of by super implementation
    } else noMessages
  }

  private[typing] def wellDefIfPureFunction(member: PFunctionDecl): Messages = {
    if (member.spec.isPure) {
      isSingleResultArg(member) ++
        isSinglePureReturnExpr(member) ++
        isPurePostcondition(member.spec) ++
        nonVariadicArguments(member.args)
    } else noMessages
  }

  private def isSingleResultArg(member: PCodeRootWithResult): Messages = {
    error(member, "For now, pure methods and pure functions must have exactly one result argument", member.result.outs.size != 1)
  }

  private def isSinglePureReturnExpr(member: PWithBody): Messages = {
    member.body match {
      case Some((_, b: PBlock)) => isPureBlock(b)
      case None => noMessages
      case Some(b) => error(member, s"For now, the body of a pure method or pure function is expected to be a single return with a pure expression, got $b instead")
    }
  }

  private def isPureBlock(block: PBlock): Messages = {
    block.nonEmptyStmts match {
      case Vector(PReturn(Vector(ret))) => isPureExpr(ret)
      case b => error(block, s"For now, the body of a pure block is expected to be a single return with a pure expression, got $b instead")
    }
  }

  private def isPurePostcondition(spec: PFunctionSpec): Messages = spec.posts flatMap isPureExpr

  private[typing] def nonVariadicArguments(args: Vector[PParameter]): Messages = args.flatMap {
    p: PParameter => error(p, s"Pure members cannot have variadic arguments, but got $p", p.typ.isInstanceOf[PVariadicType])
  }

  override lazy val localImplementationProofs: Vector[(Type, InterfaceT, Vector[String], Vector[String])] = {
    val implementationProofs = tree.root.programs.flatMap(_.declarations.collect{ case m: PImplementationProof => m})
    implementationProofs.flatMap { ip =>
      val z = (symbType(ip.subT), underlyingType(symbType(ip.superT)))
      z match {
        case (UnknownType, _) => None
        case (subT, superT: InterfaceT) =>
          Some((subT, superT, ip.alias.map(_.left.name), ip.memberProofs.map(_.id.name)))
        case _ => None
      }
    }
  }

  /**
    * Depends on which packages are loaded. Only call at the end of type checking.
    * Either returns a set of errors caused by missing implementation proofs
    * or a set of implementation proofs that have to be generated.
    **/
  def wellImplementationProofs: Either[Messages, Vector[(Type, InterfaceT, MethodImpl, MethodSpec)]] = {
    if (isMainContext && requiredImplements.nonEmpty) {
      // For every required implementation, check that there is at most one proof
      // and if not all predicates are defined, then check that there is a proof.

      val providedImplProofs = localImplementationProofs ++ context.getContexts.flatMap(_.localImplementationProofs)
      val groupedProofs = requiredImplements.toVector.map{ case (impl, itf) =>
        (impl, itf, providedImplProofs.collect{ case (`impl`, `itf`, alias, proofs) => (alias, proofs) })
      }
      val multiples = groupedProofs.collect{ case (impl, itf, ls) if ls.size > 1 => (impl, itf) }
      val msgs = if (multiples.nonEmpty) {
        error(tree.root, s"There is more than one proof for ${multiples.mkString(", ")}")
      } else {
        // check that all predicates are defined
        groupedProofs.flatMap{ case (impl, itf, ls) =>
          if (ls.nonEmpty) noMessages //
          else {
            val superPredNames = memberSet(itf).collect{ case (n, m: MPredicateSpec) => (n, m) }
            val allPredicatesDefined = PropertyResult.bigAnd(superPredNames.map{ case (name, symb) =>
              val valid = tryMethodLikeLookup(impl, PIdnUse(name)).isDefined
              failedProp({
                val argTypes = symb.args map symb.context.typ

                s"predicate $name is not defined for type $impl. " +
                  s"Either declare a predicate 'pred ($impl) $name(${argTypes.mkString(", ")})' " +
                  s"or declare a predicate 'pred p($impl${if (argTypes.isEmpty) "" else ", "}${argTypes.mkString(", ")})' with some name p and add 'pred $name := p' to the implementation proof."
              }, !valid)
            })
            allPredicatesDefined.asReason(tree.root,
              s"The code requires that $impl implements the interface $itf. An implementation proof cannot be inferred because predicate definitions are missing."
            )
          }
        }
      }
      if (msgs.nonEmpty) Left(msgs)
      else {
        Right(
          // missing implementation proofs
          groupedProofs.flatMap{ case (impl, itf, ls) =>
            val superMethNames = memberSet(itf).collect{ case (n, m: MethodSpec) => (n, m) }
            val proofs = if (ls.isEmpty) Vector.empty else ls.head._2
            val missingSuperMethNames = superMethNames.flatMap{
              case (n, itfSymb) if !proofs.contains(n) =>
                getMember(impl, n) match {
                  case Some((implSymb: MethodImpl, _)) =>
                    Some((itfSymb, implSymb))
                  case _ => None
                }
              case _ => None
            }
            missingSuperMethNames.map{ case (itfSymb, implSymb) => (impl, itf, implSymb, itfSymb) }
          }
        )
      }
    } else Left(noMessages)
  }

}
