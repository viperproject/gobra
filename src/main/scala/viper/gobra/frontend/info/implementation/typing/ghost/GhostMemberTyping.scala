// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PBlock, PCodeRootWithResult, PExplicitGhostMember, PFPredicateDecl, PFunctionDecl, PFunctionSpec, PGhostMember, PIdnUse, PImplementationProof, PMember, PMPredicateDecl, PMethodDecl, PMethodImplementationProof, PParameter, PReturn, PVariadicType, PWithBody}
import viper.gobra.frontend.info.base.SymbolTable.{MPredicateSpec, MethodImpl, MethodSpec}
import viper.gobra.frontend.info.base.Type.{InterfaceT, Type, UnknownType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation

trait GhostMemberTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMember(member: PGhostMember): Messages = member match {
    case PExplicitGhostMember(_) => noMessages

    case PFPredicateDecl(_, args, body) =>
      body.fold(noMessages)(assignableToSpec) ++ nonVariadicArguments(args)

    case PMPredicateDecl(_, receiver, args, body) =>
      body.fold(noMessages)(assignableToSpec) ++
        isReceiverType.errors(miscType(receiver))(member) ++
        nonVariadicArguments(args)
  }

  private[typing] def wellFoundedIfNeeded(member: PMember): Messages = {
    val spec = member match {
      case m: PMethodDecl => m.spec
      case f: PFunctionDecl => f.spec
      case _ => Violation.violation("Unexpected member type")
    }
    val hasMeasureIfNeeded =
      if (spec.isPure || isEnclosingGhost(member))
        config.disableCheckTerminationPureFns || spec.terminationMeasures.nonEmpty
      else
        true
    val needsMeasureError =
      error(member, "All pure or ghost functions and methods must have termination measures, but none was found for this member.", !hasMeasureIfNeeded)
    needsMeasureError
  }

  private def pureFunctionsDoNotNeedMayInitMsg = "Pure functions and methods cannot open package invariants," +
    "and thus, they must not be annotated with 'mayInit'."

  private[typing] def wellDefIfPureMethod(member: PMethodDecl): Messages = {
    if (member.spec.isPure) {
      isSingleResultArg(member) ++
        isSinglePureReturnExpr(member) ++
        isPurePostcondition(member.spec) ++
        pureMembersCannotHavePreserves(member.spec) ++
        nonVariadicArguments(member.args) ++
        error(member, pureFunctionsDoNotNeedMayInitMsg, member.spec.mayBeUsedInInit)
    } else noMessages
  }

  // Preserves clauses are unnecessary in pure functions. Any condition that holds on entry is guaranteed to
  // hold on exit, thus it is redundant to make properties both pre- and postconditions.
  private[typing] def pureMembersCannotHavePreserves(member: PFunctionSpec): Messages = {
    assert(member.isPure)
    member.preserves flatMap { c =>
      error(c, "Pure functions and pure methods cannot have preserves clauses." +
        "Considering replacing this preserves clause with a precondition.")
    }
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
        pureMembersCannotHavePreserves(member.spec) ++
        nonVariadicArguments(member.args) ++
        error(member, pureFunctionsDoNotNeedMayInitMsg, member.spec.mayBeUsedInInit)
    } else noMessages
  }

  private[typing] def atomicMemberIsWellFormed(member: PMember): Messages = {
    val (bodyOpt, spec) = member match {
      case f: PFunctionDecl => (f.body, f.spec)
      case m: PMethodDecl => (m.body, m.spec)
      case o => Violation.violation(s"Unexpected case: $o")
    }
    val atomicsAreAbstract = error(
      member,
      "Gobra does not support proving that implementations are atomic. Thus, atomic members cannot contain a body.",
      spec.isAtomic && bodyOpt.nonEmpty
    )

    val isGhost = isEnclosingGhost(member)
    val atomicsAreActual = error(
      member,
      "Ghost members cannot be marked as atomic.",
      spec.isAtomic && isGhost
    )
    val decreases = spec.terminationMeasures.nonEmpty
    val atomicMethodsTerminate = error(
      member,
      "Atomic members must be specified as terminating with a decreases-clause.",
      spec.isAtomic && !decreases
    )
    atomicsAreAbstract ++ atomicsAreActual ++ atomicMethodsTerminate
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

  private[typing] def isPureBlock(block: PBlock): Messages = {
    block.nonEmptyStmts match {
      case Vector(PReturn(Vector(ret))) => isPureExpr(ret)
      case b => error(block, s"For now, the body of a pure block is expected to be a single return with a pure expression, got $b instead")
    }
  }

  private def isPurePostcondition(spec: PFunctionSpec): Messages = (spec.posts ++ spec.preserves) flatMap isPureExpr

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
    * Either returns a set of errors caused by invalid or missing implementation proofs
    * or a set of implementation proofs that have to be generated.
    **/
  def wellImplementationProofs: Either[Messages, Vector[(Type, InterfaceT, MethodImpl, MethodSpec)]] = {
    // the main context reports missing implementation proof for all packages (i.e. all packages that have been parsed & typechecked so far)
    if (isMainContext) {
      // we not only collect the type information for directly imported packages but for all transitively imported ones:
      val typeInfos = getTransitiveTypeInfos()
      val allRequiredImplements = {
        val foundRequired = typeInfos.flatMap(_.localRequiredImplements)
        val foundGuaranteed = typeInfos.flatMap(_.localGuaranteedImplements)
        foundRequired diff foundGuaranteed
      }
      if (allRequiredImplements.nonEmpty) {
        // For every required implementation, check that there is at most one proof
        // and if not all predicates are defined, then check that there is a proof.

        val providedImplProofs = typeInfos.flatMap(_.localImplementationProofs)
        val groupedProofs = allRequiredImplements.toVector.map{ case (impl, itf) =>
          (impl, itf, providedImplProofs.collect{ case (`impl`, `itf`, alias, proofs) => (alias, proofs) })
        }
        val multiples = groupedProofs.collect{ case (impl, itf, ls) if ls.size > 1 => (impl, itf) }

        lazy val groupedProofs2 = groupedProofs.foldLeft(Map.empty[(MethodImpl, MethodSpec), Vector[(Type, InterfaceT)]]){
          case (res, (impl, itf, aliasAndProofs)) =>
            if (aliasAndProofs.nonEmpty) {
              val x = aliasAndProofs.head._2.flatMap{ methodName => (getMember(impl, methodName), getMember(itf, methodName)) match {
                case (Some((implSymb: MethodImpl, _)), Some((itfSymb: MethodSpec, _))) => Some((implSymb, itfSymb) -> (impl, itf))
                case _ => None
              }}.toMap
              (res.keySet ++ x.keySet).map(k => k -> (res.getOrElse(k, Vector.empty) ++ x.get(k))).toMap
            } else res
        }
        // if there is more than one proof for the same pair of implementation and spec (can happen with embedded interfaces)
        lazy val multiples2 = groupedProofs2.toVector.collect{ case (key, values) if values.size > 1 => (key, values) }

        val msgs = if (multiples.nonEmpty) {
          error(multiples.head._2.decl, s"There is more than one proof for type ${multiples.head._1} implementing an interface")
        } else if (multiples2.nonEmpty) {
          val firstNode = multiples2.head._1._1.context.getTypeInfo.regular(multiples2.head._1._1.decl.id).rep
          error(firstNode, s"There is more than one proof for ${multiples2.map(x => x._1._1.decl.id.name).mkString(", ")}.")
        } else {
          // check that all predicates are defined
          groupedProofs.flatMap { case (impl, itf, ls) =>
            if (ls.nonEmpty) noMessages //
            else {
              val superPredNames = memberSet(itf).collect { case (n, m: MPredicateSpec) => (n, m) }
              val allPredicatesDefined = PropertyResult.bigAnd(superPredNames.map { case (name, symb) =>
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

          val requiredImplMethAndSuperMeth = allRequiredImplements.flatMap { case (impl, itf) =>
            val superMethNames = memberSet(itf).collect { case (n, m: MethodSpec) => (n, m) }
            superMethNames.flatMap{ case (name, itfSymb) => getMember(impl, name) match {
              case Some((implSymb: MethodImpl, _)) => Some((implSymb, itfSymb))
              case _ => None
            }}
          }

          // syntactically detect errors that prevent one method from implementing a spec
          val implErrors = requiredImplMethAndSuperMeth.toVector flatMap {
            case (mImpl, mSpec) => methodImplMightImplementSpec(mImpl, mSpec)
          }
          if (implErrors.nonEmpty) {
            Left(implErrors)
          } else {
            // compute missing implementation proofs
            val missingImplMethAndSuperMeth = requiredImplMethAndSuperMeth
              .filter { case (implSymb, itfSymb) => !groupedProofs2.contains((implSymb, itfSymb)) }

            Right(missingImplMethAndSuperMeth.toVector.map { case (implSymb, itfSymb) =>
              val impl = implSymb.context.symbType(implSymb.decl.receiver.typ)
              val itf = itfSymb.itfType
              (impl, itf, implSymb, itfSymb)
            })
          }
        }
      } else Left(noMessages)
    } else Left(noMessages)
  }

  // Syntactically determine if a method implementation mImpl cannot possibly implement a specification mSpec.
  // This is useful to provide feedback quickly, before we verify the program.
  private def methodImplMightImplementSpec(mImpl: MethodImpl, mSpec: MethodSpec): Messages = {
    if (mSpec.spec.spec.terminationMeasures.nonEmpty && mImpl.decl.spec.terminationMeasures.isEmpty)
      error(mImpl.decl.spec, s"This method tries to implement a terminating interface method, " +
        s"but it does not provide a termination measure.")
    else
      noMessages
  }
}
