// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap, _}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.base.Type.{ArrayT, BooleanT, ChannelModus, ChannelT, FunctionT, InterfaceT, InternalTupleT, MapT, PredT, SetT, SliceT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.theory.Addressability
import viper.gobra.util.Violation

import scala.annotation.tailrec

trait StmtTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  lazy val wellDefStmt: WellDefinedness[PStatement] = createWellDef {
    case stmt: PActualStatement => wellDefActualStmt(stmt)
    case stmt: PGhostStatement  => wellDefGhostStmt(stmt)
  }

  private[typing] def wellDefActualStmt(stmt: PActualStatement): Messages = stmt match {

    case PConstDecl(decls) => decls flatMap {
      case n@PConstSpec(typ, right, left) =>
        val mayInit = isEnclosingMayInit(n)
        right.flatMap(isExpr(_).out) ++
          declarableTo.errors(right map exprType, typ map typeSymbType, left map idType, mayInit)(n)
    }

    case n@PVarDecl(typ, right, left, _) =>
      if (isGlobalVarDeclaration(n)) {
        // in this case, the checks occur in MemberTyping
        noMessages
      } else {
        val mayInit = isEnclosingMayInit(n)
        right.flatMap(isExpr(_).out) ++
          declarableTo.errors(right map exprType, typ map typeSymbType, left map idType, mayInit)(n)
      }

    case n: PTypeDecl => isType(n.right).out ++ (n.right match {
      case s: PStructType =>
        error(n, s"invalid recursive type ${n.left.name}", cyclicStructDef(s, Some(n.left)))
      case s@PInterfaceType(_, methSpecs, _) =>
        methSpecs.flatMap(m => error(m, "Interface method signatures cannot be opaque.", m.spec.isOpaque)) ++
        error(n, s"invalid recursive type ${n.left.name}", cyclicInterfaceDef(s, Some(n.left)))
      case _ => noMessages
    })

    case n@PExpressionStmt(exp) => isExpr(exp).out ++ isExecutable.errors(exp)(n)

    case n@PSendStmt(chn, msg) =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(chn).out ++ isExpr(msg).out ++
        ((exprType(chn), exprType(msg)) match {
          case (ChannelT(elem, ChannelModus.Bi | ChannelModus.Send), t) => assignableTo.errors(t, elem, mayInit)(n)
          case (chnt, _) => error(n, s"type error: got $chnt but expected send-permitting channel")
        })

    case n@PAssignment(rights, lefts) =>
      val mayInit = isEnclosingMayInit(n)
      rights.flatMap(isExpr(_).out) ++ lefts.flatMap(isExpr(_).out) ++
        lefts.flatMap(a => assignable.errors(a)(a)) ++
        multiAssignableTo.errors(rights map exprType, lefts map exprType, mayInit)(n)

    case n@PAssignmentWithOp(right, op@(_: PShiftLeftOp | _: PShiftRightOp), left) =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(right).out ++ isExpr(left).out ++
        assignable.errors(left)(n) ++ compatibleWithAssOp.errors(exprType(left), op)(n) ++
        assignableTo.errors(exprType(right), UNTYPED_INT_CONST, mayInit)(n)

    case n@PAssignmentWithOp(right, op, left) =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(right).out ++ isExpr(left).out ++
        assignable.errors(left)(n) ++ compatibleWithAssOp.errors(exprType(left), op)(n) ++
        assignableTo.errors(exprType(right), exprType(left), mayInit)(n)

    case n@PShortVarDecl(rights, lefts, _) =>
      val mayInit = isEnclosingMayInit(n)
      // TODO: check that at least one of the variables is new
      if (lefts.forall(pointsToData))
        rights.flatMap(isExpr(_).out) ++
          multiAssignableTo.errors(rights map exprType, lefts map idType, mayInit)(n)
      else error(n, s"at least one assignee in $lefts points to a type")

    case _: PLabeledStmt => noMessages

    case n: PIfStmt => n.ifs.flatMap(ic =>
      isExpr(ic.condition).out ++
        assignableTo.errors(exprType(ic.condition), BooleanT, isEnclosingMayInit(ic))(ic)
    )

    case n@PExprSwitchStmt(_, exp, _, dflt) =>
      error(n, s"found more than one default case", dflt.size > 1) ++
        isExpr(exp).out ++ comparableType.errors(exprType(exp))(n)

    case n@tree.parent.pair(PExprSwitchCase(left, _), sw: PExprSwitchStmt) =>
      left.flatMap(e => isExpr(e).out ++ comparableTypes.errors(exprType(e), exprType(sw.exp))(n))

    case n: PTypeSwitchStmt =>
      val firstChecks = error(n, s"found more than one default case", n.dflt.size > 1) ++ isExpr(n.exp).out ++ {
        val et = exprType(n.exp)
        val ut = underlyingType(et)
        error(n, s"type error: got $et but expected underlying interface type", !ut.isInstanceOf[InterfaceT])
      }
      val latterChecks = {
        val expTyp = exprOrTypeType(n.exp)
        n.cases.flatMap(_.left).flatMap {
          case t: PType => implements(typeSymbType(t), expTyp).asReason(t, s"impossible type switch case: ${n.exp} (type $expTyp) cannot have dynamic type $t")
          case e: PExpression => error(e, s"$e is not valid in type switch clauses", !e.isInstanceOf[PNilLit])
        }
      }
      if (firstChecks.isEmpty) latterChecks else firstChecks

    case n@PForStmt(_, cond, _, spec, _) =>
      val isGhost = isEnclosingGhost(n)
      val noTerminationMeasureMsg = "Loop occurring in ghost context does not have a termination measure"
      isExpr(cond).out ++
        comparableTypes.errors(exprType(cond), BooleanT)(n) ++
        error(n, noTerminationMeasureMsg, isGhost && spec.terminationMeasure.isEmpty)

    case r@PShortForRange(range, shorts, _, _, _) =>
      val mayInit = isEnclosingMayInit(r)
      underlyingType(exprType(range.exp)) match {
        case _ : ArrayT | _ : SliceT =>
          multiAssignableTo.errors(Vector(miscType(range)), shorts map idType, mayInit)(range) ++
          assignableTo.errors(miscType(range), idType(range.enumerated), mayInit)(range)
        case MapT(key, _) => multiAssignableTo.errors(Vector(miscType(range)), shorts map idType, mayInit)(range) ++
          assignableTo.errors((SetT(key), idType(range.enumerated), mayInit))(range)
        case t => error(range, s"range not supported for type $t")
      }

    case a@PAssForRange(range, ass, _, _) =>
      val mayInit = isEnclosingMayInit(a)
      underlyingType(exprType(range.exp)) match {
        case _ : ArrayT | _ : SliceT | _ : MapT =>
          multiAssignableTo.errors(Vector(miscType(range)), ass map exprType, mayInit)(range)
        case t => error(range, s"range not supported for type $t")
      }

    case n@PGoStmt(exp) => isExpr(exp).out ++ isExecutable.errors(exp)(n)

    case n: PSelectStmt =>
      val mayInit = isEnclosingMayInit(n)
      n.aRec.flatMap(rec =>
        rec.ass.flatMap(isExpr(_).out) ++
          multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.ass.map(exprType), mayInit)(rec) ++
          rec.ass.flatMap(a => assignable.errors(a)(a))
      ) ++ n.sRec.flatMap(rec =>
        if (rec.shorts.forall(pointsToData))
          multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.shorts map idType, mayInit)(rec)
        else error(n, s"at least one assignee in ${rec.shorts} points to a type")
      )

    case n@PReturn(exps) =>
      val mayInit = isEnclosingMayInit(n)
      exps.flatMap(isExpr(_).out) ++ {
        if (exps.nonEmpty) {
          val closureImplProof = tryEnclosingClosureImplementationProof(n)
          if (closureImplProof.isEmpty) {
            val res = tryEnclosingCodeRootWithResult(n)
            if (res.isEmpty) return error(n, s"Statement does not root in a CodeRoot")
            if (!(res.get.result.outs forall wellDefMisc.valid)) return error(n, s"return cannot be checked because the enclosing signature is incorrect")
          }
          multiAssignableTo.errors(exps map exprType, returnParamsAndTypes(n).map(_._1), mayInit)(n)
        } else noMessages // a return without arguments is always well-defined
      }

    case n@PDeferStmt(exp: PExpression) => isExpr(exp).out ++ isExecutable.errors(exp)(n)
    case PDeferStmt(_: PUnfold | _: PFold) => noMessages

    case _: PBlock => noMessages
    case _: PSeq => noMessages

    case n: POutline =>
      val invalidNodes: Vector[Messages] = allChildren(n) collect {
        case n@ (_: POld | _: PLabeledOld) => error(n, "outline statements must not contain old expressions, use a before expression instead.")
        case n: PDeferStmt => error(n, "Currently, outline statements are not allowed to contain defer statements.")
        case n: PReturn => error(n, "outline statements must not contain return statements.")
      }
      error(n, s"pure outline statements are not supported.", n.spec.isPure) ++
        error(n, s"outline statements cannot be marked as atomic.", n.spec.isAtomic) ++
        error(n, s"outline statements cannot be marked with 'opensInvariants'.", n.spec.opensInvs) ++
        error(n, "Opaque outline statements are not supported.", n.spec.isOpaque) ++
        invalidNodes.flatten

    case n: PCritical =>
      def validArgAtomicFuncCall(e: PExpression): Boolean = e match {
        case e if isExprGhost(e) =>
          // ghost params are always ok
          true
        case _: PIntLit | _: PFloatLit | _: PStringLit | _: PNilLit =>
          true
        case v: PNamedOperand if addressability(v) == Addressability.Exclusive =>
          // exclusive variables cannot be concurrently modified during an atomic operation and thus, are ok
          true
        case p: PDot =>
          // if p is a package name, an exclusive var, or a type
          addressability(p) == Addressability.Exclusive &&
            (p.base match {
            case _: PType => true
            case e: PExpression =>
              e match {
                case _: PNamedOperand => true
                case _ => false
              }
            })
        case _ =>
          false
      }

      def nonGhostAtomicFuncCall(i: PInvoke): Boolean = {
        resolve(i.base) match {
          case Some(f: ap.Function) =>
            val decl = f.symb.decl
            val isGhost = f.symb.ghost
            val isAtomic = decl.spec.isAtomic
            val argsAreFine = i.args.forall(validArgAtomicFuncCall)
            !isGhost && isAtomic && argsAreFine
          case Some(m: ap.ReceivedMethod) =>
            val spec = m.symb match {
              case impl: st.MethodImpl => impl.decl.spec
              case spec: st.MethodSpec => spec.spec.spec
            }
            val isGhost = m.symb.ghost
            val isAtomic = spec.isAtomic
            val recvIsFine = validArgAtomicFuncCall(i.base.asInstanceOf[PDot].base.asInstanceOf[PExpression])
            val argsAreFine = i.args.forall(validArgAtomicFuncCall)
            !isGhost && isAtomic && recvIsFine && argsAreFine
          case _ => false
        }
      }

      // the only non-ghost operations that are allowed are calls to atomic functions. The parameters to these functions
      // are severely restricted. These restrictions ensure that the values of the parameters cannot change concurrently
      // to the atomic call.
      def notGhostAtomicOp(s: PStatement): Boolean = s match {
        case e: PExpressionStmt => e.exp match {
          case i: PInvoke =>
            nonGhostAtomicFuncCall(i)
          case _ => false
        }
        case a: PAssignment =>
          // we only allow a very limited form of assignments to be able to read the out parameters of a call to
          // an atomic function
          a.left.forall {
            case n: PNamedOperand => addressability(n) == Addressability.Exclusive
            case _: PBlankIdentifier => true
            case _ => false
          } &&
          a.right.length == 1 &&
          a.right(0).isInstanceOf[PInvoke] &&
          nonGhostAtomicFuncCall(a.right(0).asInstanceOf[PInvoke])
        case l: PLabeledStmt =>
          notGhostAtomicOp(l.stmt)
        case d: PShortVarDecl =>
          // we only allow a very limited form of assignments to be able to read the out parameters of a call to
          // an atomic function
          d.left.forall(e => addressableVar(e) == Addressability.Exclusive) &&
            d.right.length == 1 &&
            d.right(0).isInstanceOf[PInvoke] &&
            nonGhostAtomicFuncCall(d.right(0).asInstanceOf[PInvoke])
        case p: PCritical => p.stmts.exists(notGhostAtomicOp)
        case _ => false
      }

      val (inGhost, spec) = tryEnclosingFunctionOrMethod(n) match {
        case Some(f: PFunctionDecl) => (isEnclosingGhost(f), f.spec)
        case Some(m: PMethodDecl) => (isEnclosingGhost(m), m.spec)
        case _ => violation("Unexpected case reached")
      }
      // all ops are either ghost or non-ghost atomic operations
      val invalidOpOpt = n.stmts.find(s => !notGhostAtomicOp(s) && !isStmtGhost(s))

      val exprT = exprType(n.expr)
      val nonGhostAtomicOps = n.stmts.filter(notGhostAtomicOp)
      error(n.expr, s"Expression ${n.expr} is of type $exprT but it should be of type pred()",
        exprT != PredT(Vector.empty)) ++
      invalidOpOpt.toVector.flatMap(e => error(n, s"Found invalid operation $e in a critical region.")) ++
      nonGhostAtomicOps.lift(1).toVector.flatMap(e => error(e, s"At most one atomic operation is allowed in a critical region.")) ++
      error(n, s"Only ghost members marked with 'opensInvariants' may open invariants.", inGhost && !spec.opensInvs)

    case _: PEmptyStmt => noMessages
    case _: PGoto => ???

    case n@PBreak(l) =>
      l match {
        case None =>
          enclosingLoopUntilOutline(n) match {
            case Left(Some(_: POutline)) => error(n, "break must be inside of a loop without an outline statement in between.")
            case Left(_) => error(n, s"break must be inside a loop.")
            case Right(_) => noMessages
          }
        case Some(label) =>
          val maybeLoop = enclosingLabeledLoop(label, n)
          maybeLoop match {
            case Left(Some(_: POutline)) => error(n, "break label must point to an outer labeled loop without an outline statement in between.")
            case Left(_) => error(n, s"break label must point to an outer labeled loop.")
            case Right(_) => noMessages
          }
      }

    case n@PContinue(l) =>
      l match {
        case None =>
          enclosingLoopUntilOutline(n) match {
            case Left(Some(_: POutline)) => error(n, "continue must be inside of a loop without an outline statement in between.")
            case Left(_) => error(n, s"continue must be inside a loop.")
            case Right(_) => noMessages
          }
        case Some(label) =>
          val maybeLoop = enclosingLabeledLoop(label, n)
          maybeLoop match {
            case Left(Some(_: POutline)) => error(n, "continue label must point to an outer labeled loop without an outline statement in between.")
            case Left(_) => error(n, s"continue label must point to an outer labeled loop.")
            case Right(_) => noMessages
          }
      }

    case p: PClosureImplProof => wellDefClosureImplProof(p)

    case s => violation(s"$s was not handled")
  }

  private [typing] def returnParamsAndTypes(n: PReturn): Vector[(Type, PParameter)] = {
    val closureImplProof = tryEnclosingClosureImplementationProof(n)
    if (closureImplProof.nonEmpty) {
      (resolve(closureImplProof.get.impl.spec.func) match {
        case Some(AstPattern.Function(id, f)) => (idType(id).asInstanceOf[FunctionT].result, f.result.outs)
        case Some(AstPattern.Closure(id, c)) => (idType(id).asInstanceOf[FunctionT].result, c.result.outs)
        case _ => violation("this case should be unreachable")
      }) match {
        case (InternalTupleT(types), ps) => types zip ps
        case (t, ps) => Vector(t) zip ps
      }
    } else {
      val res = tryEnclosingCodeRootWithResult(n)
      res.get.result.outs map miscType zip res.get.result.outs
    }
  }

  private def nameFromParam(p: PParameter): Option[String] = p match {
    case PNamedParameter(id, _) => Some(id.name)
    case PExplicitGhostParameter(PNamedParameter(id, _)) => Some(id.name)
    case _ => None
  }

  private def wellDefClosureImplProof(p: PClosureImplProof): Messages = {
    val PClosureImplProof(impl@PClosureImplements(closure, spec), b: PBlock) = p

    val func = resolve(spec.func) match {
      case Some(ap.Function(_, f)) => f
      case Some(ap.Closure(_, c)) => c
      case _ => Violation.violation(s"expected a function or closure, but got ${spec.func}")
    }

    val specArgs = if (spec.params.forall(_.key.isEmpty)) func.args.drop(spec.params.size)
    else {
      val paramSet = spec.paramKeys.toSet
      func.args.filter(nameFromParam(_).fold(true)(!paramSet.contains(_)))
    }

    val isPure = func match {
      case f: st.Function => f.isPure
      case c: st.Closure => c.isPure
    }

    lazy val expectedCallArgs = specArgs.flatMap(nameFromParam).map(a => PNamedOperand(PIdnUse(a)))

    def isExpectedCall(i: PInvoke): Boolean = i.base == closure && i.args == expectedCallArgs

    lazy val expectedCallString: String = resolve(closure) match {
      case Some(_: ap.LocalVariable) => s"$closure(${specArgs.flatMap(nameFromParam).mkString(",")}) as _"
      case _ => s"$closure(${specArgs.flatMap(nameFromParam).mkString(",")}) [as _]"
    }

    def wellDefIfNecessaryArgsNamed: Messages = error(spec,
      s"cannot find a name for all arguments or results required by $spec",
      cond = !specArgs.forall {
        case _: PUnnamedParameter | PExplicitGhostParameter(_: PUnnamedParameter) => false
        case _ => true
      }
    )

    def wellDefIfArgNamesDoNotShadowClosure: Messages = {
      val names = (func.args ++ func.result.outs).map(nameFromParam).filter(_.nonEmpty).map(_.get)

      def isShadowed(id: PIdnNode): Boolean = id match {
        case _: PIdnUse | _: PIdnUnk =>
          val entityOutside = tryLookupAt(id, impl)
          entityOutside.nonEmpty && tryLookupAt(id, id).fold(false)(_ eq entityOutside.get) && names.contains(id.name)
        case _ => false
      }

      def shadowedInside(n: PNode): Option[PIdnNode] = n match {
        case id: PIdnNode => Some(id).filter(isShadowed)
        case _ => tree.child(n).iterator.map(shadowedInside).find(_.nonEmpty).flatten
      }

      val shadowed = shadowedInside(closure)
      error(impl,
        s"identifier ${shadowed.getOrElse("")} in $closure is shadowed by an argument or result with the same name in ${spec.func}",
        shadowed.nonEmpty)
    }

    def pureWellDefIfIsSinglePureReturnExpr: Messages = if (isPure) isPureBlock(b) else noMessages

    def pureWellDefIfRightShape: Messages = if (!isPure) {
      noMessages
    } else b.nonEmptyStmts match {
      case Vector(PReturn(Vector(retExpr))) =>
        pureImplementationProofHasRightShape(retExpr, isExpectedCall, expectedCallString)
          .asReason(retExpr, "invalid return expression of an implementation proof")
      case _ => error(b, "invalid body of a pure implementation proof: expected a single return")
    }

    def wellDefIfRightShape: Messages =
      if (isPure) noMessages
      else implementationProofBodyHasRightShape(b, isExpectedCall, expectedCallString, func.result)
        .asReason(b, "invalid body of an implementation proof")

    def wellDefIfTerminationMeasuresConsistent: Messages = {
      val specMeasures = func match {
        case st.Function(decl, _, _) => decl.spec.terminationMeasures
        case st.Closure(lit, _, _) => lit.spec.terminationMeasures
      }

      lazy val callMeasures = terminationMeasuresForProofCall(p)

      // If the spec has termination measures, then the call inside the proof
      // must be done with a spec that also has termination measures
      if (specMeasures.isEmpty) noMessages
      else error(p, s"spec ${p.impl.spec} has termination measures, so also ${closureImplProofCallAttr(p).base}" +
        s"(used inside the proof) must", callMeasures.isEmpty)
    }

    Seq(wellDefIfNecessaryArgsNamed, wellDefIfArgNamesDoNotShadowClosure, pureWellDefIfIsSinglePureReturnExpr,
      pureWellDefIfRightShape, wellDefIfRightShape, wellDefIfTerminationMeasuresConsistent)
      .iterator.find(_.nonEmpty).getOrElse(noMessages)
  }

  private[typing] def pureImplementationProofHasRightShape(retExpr: PExpression,
                                                          isExpectedCall: PInvoke => Boolean,
                                                          expectedCall: String): PropertyResult = {
    @tailrec
    def validExpression(expr: PExpression): PropertyResult = expr match {
      case invoke: PInvoke => failedProp(s"The call must be $expectedCall", !isExpectedCall(invoke))
      case f: PUnfolding => validExpression(f.op)
      case _ => failedProp(s"only unfolding expressions and the call $expectedCall is allowed")
    }

    validExpression(retExpr)
  }

  // the body can only contain fold, unfold, and the call
  private[typing] def implementationProofBodyHasRightShape(body: PBlock,
                                                          isExpectedCall: PInvoke => Boolean,
                                                          expectedCall: String,
                                                          result: PResult): PropertyResult = {
    val expectedResults = result.outs.flatMap(nameFromParam).map(t => PNamedOperand(PIdnUse(t)))

    def isExpectedAssignment(ass: PAssignment): Boolean = ass match {
      case PAssignment(Vector(i: PInvoke), left) => isExpectedCall(i) && expectedResults == left
      case _ => false
    }

    def isExpectedReturn(ret: PReturn): Boolean = ret match {
      case PReturn(exps) =>
        if (result.outs.isEmpty) exps == Vector.empty
        else if (result.outs.size != expectedResults.size)
          exps == Vector.empty || (exps.size == 1 && exps.head.isInstanceOf[PInvoke] && isExpectedCall(exps.head.asInstanceOf[PInvoke]))
        else exps == Vector.empty || exps == expectedResults || (exps.size == 1 && exps.head.isInstanceOf[PInvoke] && isExpectedCall(exps.head.asInstanceOf[PInvoke]))
    }

    lazy val expectedReturns: Seq[String] =
      if (result.outs.isEmpty) Seq("return")
      else if (result.outs.size != expectedResults.size) Seq("return", s"return $expectedCall")
      else Seq("return", PReturn(expectedResults).toString, s"return $expectedCall")

    lazy val expectedCallStatement = if (result.outs.isEmpty) expectedCall
    else if (expectedResults.size != result.outs.size) s"return $expectedCall"
    else s"${expectedResults.mkString(",")} = $expectedCall"

    lazy val lastStatement: PStatement = {
      @tailrec
      def aux(stmt: PStatement): PStatement = stmt match {
        case seq: PSeq => aux(seq.nonEmptyStmts.last)
        case block: PBlock => aux(block.nonEmptyStmts.last)
        case s => s
      }

      aux(body)
    }

    var numOfImplemetationCalls = 0

    def validStatements(stmts: Vector[PStatement]): PropertyResult =
      PropertyResult.bigAnd(stmts.map {
        case _: PUnfold | _: PFold | _: PAssert | _: PRefute | _: PEmptyStmt => successProp
        case _: PAssume | _: PInhale | _: PExhale => failedProp("Assume, inhale, and exhale are forbidden in implementation proofs")

        case b: PBlock => validStatements(b.nonEmptyStmts)
        case seq: PSeq => validStatements(seq.nonEmptyStmts)

        case ass: PAssignment =>
          // Right now, we only allow assignments that are used for the one call
          if (isExpectedAssignment(ass)) {
            numOfImplemetationCalls += 1
            successProp
          } else if (result.outs.isEmpty || expectedResults.size != result.outs.size) {
            val reason =
              if (result.outs.isEmpty) "Here, there are no out-parameters."
              else "Here, not all out-parameters have a name, so they cannot be assigned to."
            failedProp("An assignment must assign to all out-parameters. " + reason)
          } else {
            failedProp(s"The only allowed assignment is $expectedCallStatement")
          }

        // A call alone can only occur if there are no result parameters
        case PExpressionStmt(call: PInvoke) =>
          if (result.outs.nonEmpty) failedProp(s"The call '$call' is missing the out-parameters")
          else if (!isExpectedCall(call)) failedProp(s"The only allowed call is $expectedCall")
          else {
            numOfImplemetationCalls += 1
            successProp
          }

        case ret@PReturn(exps) =>
          // there has to be at most one return at the end of the block
          if (lastStatement != ret) {
            failedProp("A return must be the last statement")
          } else if (isExpectedReturn(ret)) {
            if (exps.size == 1 && exps.head.isInstanceOf[PInvoke] && isExpectedCall(exps.head.asInstanceOf[PInvoke])) numOfImplemetationCalls += 1
            successProp
          } else failedProp(s"A return must be one of ${expectedReturns.mkString(", ")}")

        case _ => failedProp("Only fold, unfold, assert, and one call to the implementation are allowed")
      })

    val bodyHasRightShape = validStatements(body.nonEmptyStmts)
    val notTooManyCalls = {
      if (numOfImplemetationCalls != 1) {
        failedProp(s"There must be exactly one call to the implementation " +
          s"(with results and arguments in the right order '$expectedCallStatement')")
      } else successProp
    }

    bodyHasRightShape and notTooManyCalls
  }

  /** Returns the termination measures of the spec used in the call within the body of the implementation proof */
  private def terminationMeasuresForProofCall(p: PClosureImplProof): Vector[PTerminationMeasure] = {
    def measuresFromMethod(m: st.Method): Vector[PTerminationMeasure] = m match {
      case st.MethodSpec(spec, _, _, _) => spec.spec.terminationMeasures
      case st.MethodImpl(decl, _, _) => decl.spec.terminationMeasures
    }

    resolve(closureImplProofCallAttr(p)) match {
      case Some(ap.FunctionCall(callee, _)) => callee match {
        case ap.Function(_, symb) => symb.decl.spec.terminationMeasures
        case ap.Closure(_, symb) => symb.lit.spec.terminationMeasures
        case ap.ReceivedMethod(_, _, _, symb) => measuresFromMethod(symb)
        case ap.ImplicitlyReceivedInterfaceMethod(_, symb) => symb.spec.spec.terminationMeasures
        case ap.MethodExpr(_, _, _, symb) => measuresFromMethod(symb)
        case _ => Violation.violation("this case should be unreachable")
      }
      case Some(ap.ClosureCall(_, _, spec)) => resolve(spec.func) match {
        case Some(ap.Function(_, f)) => f.decl.spec.terminationMeasures
        case Some(ap.Closure(_, c)) => c.lit.spec.terminationMeasures
        case _ => Violation.violation("this case should be unreachable")
      }
      case _ => Violation.violation("this case should be unreachable")
    }
  }
}
