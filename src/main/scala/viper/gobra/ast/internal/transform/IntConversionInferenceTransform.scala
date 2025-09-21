package viper.gobra.ast.internal.transform
import viper.gobra.ast.internal.{AdtDestructor, AdtDiscriminator, ApplyWand, ArrayUpdate, Assert, Assignment, Assume, BinaryExpr, Block, BoolOperation, Break, Capacity, ClosureCall, ClosureImplements, ClosureObject, Conditional, Continue, Conversion, Defer, Deferrable, Deref, DfltVal, DomainFunctionCall, EffectfulConversion, Exhale, Exists, FieldRef, Fold, FunctionCall, FunctionObject, GhostCollectionUpdate, GoClosureCall, GoFunctionCall, GoMethodCall, If, IndexedExp, Inhale, Initialization, IntOperation, IsBehaviouralSubtype, IsComparableInterface, IsComparableType, Label, LabeledOld, Length, Lit, Location, Low, LowContext, MakeStmt, MapConversion, MapKeys, MapValues, MethodBody, MethodBodySeqn, MethodCall, MethodObject, MultisetConversion, New, NewMapLit, NewSliceLit, Old, OptionGet, OptionNone, OptionSome, Outline, PackageWand, PatternMatchExp, PatternMatchStmt, Permission, PredExprFold, PredExprUnfold, PredicateConstructor, PureClosureCall, PureForall, PureFunction, PureFunctionCall, PureLet, PureMethod, PureMethodCall, RangeSequence, Receive, Ref, Refute, Return, SafeMapLookup, SafeReceive, SafeTypeAssertion, Send, Seqn, SequenceConversion, SequenceDrop, SequenceTake, SetConversion, Slice, SpecImplementationProof, StructUpdate, ToInterface, Tuple, TypeAssertion, TypeExpr, TypeOf, Unfold, Unfolding, Var, While}
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.util.TypeBounds

// TODO: doc
object IntConversionInferenceTransform extends InternalTransform {
  override def name(): String = "int_conversion_inference"

  private val intType = in.IntT(Addressability.Exclusive, TypeBounds.DefaultInt)

  /**
   * Program-to-program transformation
   */
  override def transform(p: in.Program): in.Program =
    in.Program(
      types = p.types,
      members = p.members.map(transformMember(p)),
      table = p.table
    )(p.info)

  private def transformAssert(originalProg: in.Program)(a: in.Assertion): in.Assertion = {
    println(s"Assertion: $a")
    a match {
      case a@in.ExprAssertion(e) =>
        in.ExprAssertion(transformExpr(originalProg)(e))(a.info)
      case i@in.Implication(l, r) =>
        val newL = transformExpr(originalProg)(l)
        val newR = transformAssert(originalProg)(r)
        in.Implication(newL, newR)(i.info)
      case q@in.SepForall(vars, triggers, body) =>
        val newTriggers = triggers map { t =>
          val newExprs = t.exprs map {
            case e: in.Expr => transformExpr(originalProg)(e)
            case o =>
              // TODO: adapt these
              o
          }
          in.Trigger(newExprs)(t.info)
        }
        val newBody = transformAssert(originalProg)(body)
        in.SepForall(vars, newTriggers, newBody)(q.info)
      case a@in.SepAnd(l, r) =>
        val newL = transformAssert(originalProg)(l)
        val newR = transformAssert(originalProg)(r)
        in.SepAnd(newL, newR)(a.info)
      case a@in.Access(acc, p) =>
        val newAcc = transformAccessible(originalProg)(acc)
        val newP = transformExpr(originalProg)(p)
        in.Access(newAcc, newP)(a.info)
    }
  }

  private def transformAccess(originalProg: in.Program)(acc: in.Access): in.Access = {
    val newAcc = transformAccessible(originalProg)(acc.e)
    val newP = transformExpr(originalProg)(acc.p)
    in.Access(newAcc, newP)(acc.info)
  }

  private def transformAccessible(originalProg: in.Program)(acc: in.Accessible): in.Accessible = {
    acc match {
      case p: in.Accessible.Predicate =>
        in.Accessible.Predicate(transformPredicateAccess(originalProg)(p.op))
      case p: in.Accessible.PredExpr =>
        val newOp = transformPredExprInstance(originalProg)(p.op)
        in.Accessible.PredExpr(newOp)
      case p: in.Accessible.Address =>
        val newOp = transformLocation(originalProg)(p.op)
        in.Accessible.Address(newOp)
      case p: in.Accessible.ExprAccess =>
        val newOp = transformExpr(originalProg)(p.op)
        in.Accessible.ExprAccess(newOp)
    }
  }

  private def transformLocation(originalProg: in.Program)(l: in.Location): in.Location = l match {
    case i@IndexedExp(base, index, baseUnderlyingType) =>
      val newBase = transformExpr(originalProg)(base)
      val newIdx = transformExprToIntendedType(originalProg)(index, intType)
      in.IndexedExp(newBase, newIdx, baseUnderlyingType)(i.info)
    case d: Deref => transformLocation(originalProg)(d)
    case r@Ref(ref, typ) =>
      val newRef = transformAddressable(originalProg)(ref)
      in.Ref(newRef, typ)(r.info)
    case f: FieldRef => transformFieldRef(originalProg)(f)
    case v: Var => v
  }


  private def transformPredExprInstance(originalProg: in.Program)(e: in.PredExprInstance): in.PredExprInstance = {
    // TODO
    e
  }

  private def transformPredicateAccess(originalProg: in.Program)(acc: in.PredicateAccess): in.PredicateAccess =
    // TODO: apply transform to all cases
    acc match {
      case p@in.FPredicateAccess(pred, args) =>
        val actualPred = originalProg.table.lookup(pred)
        val argTypes = actualPred match {
          case in.FPredicate(_, args, _) => args map (_.typ)
          case in.BuiltInFPredicate(_, _, argsT) => argsT
        }
        val newArgs = args zip argTypes map { case (arg, typ) =>
          transformExprToIntendedType(originalProg)(arg, typ)
        }
        in.FPredicateAccess(pred, newArgs)(p.info)
      case p@in.MPredicateAccess(recv, pred, args) =>
        // no need to cast the receiver to the intended receiver type, the type resolution would have failed if there
        // was a type mismatch
        val newRecv = transformExpr(originalProg)(recv)
        val actualPred = originalProg.table.lookup(pred)
        val argTypes = actualPred match {
          case in.MPredicate(_, _, args, _) => args map (_.typ)
          case in.BuiltInMPredicate(_, _, _, argsT) => argsT
        }
        val newArgs = args zip argTypes map { case (arg, typ) =>
          transformExprToIntendedType(originalProg)(arg, typ)
        }
        in.MPredicateAccess(newRecv, pred, newArgs)(p.info)
      case in.MemoryPredicateAccess(arg) => ???

    }

  private def transformExpr(originalProg: in.Program)(e: in.Expr): in.Expr = transformExprToIntendedType(originalProg)(e, e.typ)
  private def transformExprToIntendedType(originalProg: in.Program)(e: in.Expr, intededType: in.Type): in.Expr = {
    val newE: in.Expr = e match {
      case a@in.And(l, r) =>
        val newL = transformExpr(originalProg)(l)
        val newR = transformExpr(originalProg)(r)
        in.And(newL, newR)(a.info)
      case o@in.Or(l, r) =>
        val newL = transformExpr(originalProg)(l)
        val newR = transformExpr(originalProg)(r)
        in.Or(newL, newR)(o.info)
      case e@in.EqCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.EqCmp(newL, newR)(e.info)
      case e@in.GhostEqCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.GhostEqCmp(newL, newR)(e.info)
      case e@in.GhostUneqCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.GhostUneqCmp(newL, newR)(e.info)
      case u@in.UneqCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.UneqCmp(newL, newR)(u.info)
      case c@in.LessCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.LessCmp(newL, newR)(c.info)
      case c@in.GreaterCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.GreaterCmp(newL, newR)(c.info)
      case c@in.AtLeastCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.AtLeastCmp(newL, newR)(c.info)
      case c@in.AtMostCmp(l, r) =>
        val typ = mergeTypes(originalProg)(l.typ, r.typ)
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.AtMostCmp(newL, newR)(c.info)
      case c@in.Add(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.Add(newL, newR)(c.info)
      case c@in.Sub(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.Sub(newL, newR)(c.info)
      case c@in.Mul(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.Mul(newL, newR)(c.info)
      case c@in.Div(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.Div(newL, newR)(c.info)
      case c@in.Mod(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.Mod(newL, newR)(c.info)
      case c@in.BitAnd(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.BitAnd(newL, newR)(c.info)
      case c@in.BitOr(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.BitOr(newL, newR)(c.info)
      case c@in.BitXor(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.BitXor(newL, newR)(c.info)
      case c@in.BitClear(l, r) =>
        val typ = c.typ
        val newL = transformExprToIntendedType(originalProg)(l, typ)
        val newR = transformExprToIntendedType(originalProg)(r, typ)
        in.BitClear(newL, newR)(c.info)
      case c@in.BitNeg(op) =>
        val newOp = transformExprToIntendedType(originalProg)(op, c.typ)
        in.BitNeg(newOp)(c.info)
      case n@in.Negation(op) =>
        val newOp = transformExpr(originalProg)(op)
        in.Negation(newOp)(n.info)
      case c@in.PermGeCmp(l, r) =>
        val newL = transformExpr(originalProg)(l) // maybe cast this to Perm type instead
        val newR = transformExpr(originalProg)(r) // maybe cast this to Perm type instead
        in.PermGeCmp(newL, newR)(c.info)
      case c@in.PermGtCmp(l, r) =>
        val newL = transformExpr(originalProg)(l) // maybe cast this to Perm type instead
        val newR = transformExpr(originalProg)(r) // maybe cast this to Perm type instead
        in.PermGtCmp(newL, newR)(c.info)
      case c@in.PermLeCmp(l, r) =>
        val newL = transformExpr(originalProg)(l) // maybe cast this to Perm type instead
        val newR = transformExpr(originalProg)(r) // maybe cast this to Perm type instead
        in.PermLeCmp(newL, newR)(c.info)
      case c@in.PermLtCmp(l, r) =>
        val newL = transformExpr(originalProg)(l) // maybe cast this to Perm type instead
        val newR = transformExpr(originalProg)(r) // maybe cast this to Perm type instead
        in.PermLtCmp(newL, newR)(c.info)
      case f: in.FullPerm => f
      case n: in.NoPerm => n
      case w: in.WildcardPerm => w
      case p@in.FractionalPerm(l, r) =>
        val newL = transformExpr(originalProg)(l) // maybe cast this to Perm type instead
        val newR = transformExpr(originalProg)(r) // maybe cast this to Perm type instead
        in.FractionalPerm(newL, newR)(p.info)
      case c@in.Conversion(t, e) =>
        val newE = transformExpr(originalProg)(e)
        in.Conversion(t, newE)(c.info)
      case p@in.PureFunctionCall(func, args, typ, reveal) =>
        val f = originalProg.table.lookup(func)
        val argTypes = f match {
          case pf: PureFunction => pf.args map (_.typ)
          case _ =>
            // unreachable case
            ???
        }
        val newArgs = args zip argTypes map { case (arg, typ) =>
          transformExprToIntendedType(originalProg)(arg, typ)
        }
        in.PureFunctionCall(func, newArgs, typ, reveal)(p.info)
      case p@in.PureMethodCall(recv, func, args, typ, reveal) =>
        val newRecv = transformExpr(originalProg)(recv)
        val m = originalProg.table.lookup(func)
        val argTypes = m match {
          case pf: PureMethod => pf.args map (_.typ)
          case _ =>
            // unreachable case
            ???
        }
        val newArgs = args zip argTypes map { case (arg, typ) =>
          transformExprToIntendedType(originalProg)(arg, typ)
        }
        in.PureMethodCall(newRecv, func, newArgs, typ, reveal)(p.info)
      case l: in.Location =>
        transformLocation(originalProg)(l)
      case l: in.Lit => l match {
        case i: in.IntLit => i
        case o =>
          // TODO
        o
      }
      case l: in.Length => l
      case c: in.Capacity => c
      case s@in.Slice(base, low, high, max, baseUnderlyingType) =>
        s
        /*
        val newBase = transformExpr(originalProg)(base)
        // val newLow = transformExprToIntendedType(originalProg)(low, intType)
        // val newHigh = transformExprToIntendedType(originalProg)(high, intType)
        // val newMax = max map { m => transformExprToIntendedType(originalProg)(m, intType) }
        // in.Slice(newBase, newLow, newHigh, newMax, baseUnderlyingType)(s.info)
        in.Slice(newBase, low, high, max, baseUnderlyingType)(s.info)
         */

      case o: in.Old =>
        // TODO: check
        o
      case u: in.Unfolding =>
        val newAcc = transformAccess(originalProg)(u.acc)
        val newExpr = transformExprToIntendedType(originalProg)(u.in, u.typ)
        in.Unfolding(newAcc, newExpr)(u.info)

      case i =>
        println(s"Trying $i")
        ???
    }
    if (newE.typ == intededType) newE else in.Conversion(intededType, newE)(newE.info)
  }

  // TODO: drop
  private def allExprs(e: in.Expr): Unit = {
    e match {
      case PatternMatchExp(exp, typ, cases, default) => ???
      case PureLet(left, right, in) => ???
      case LabeledOld(label, operand) => ???
      case Conditional(cond, thn, els, typ) => ???
      case PureForall(vars, triggers, body) => ???
      case Exists(vars, triggers, body) => ???
      case permission: Permission => ???
      case TypeAssertion(exp, arg) => ???
      case TypeOf(exp) => ???
      case IsComparableType(exp) => ???
      case IsComparableInterface(exp) => ???
      case IsBehaviouralSubtype(subtype, supertype) => ???
      case ToInterface(exp, typ) => ???
      case expr: TypeExpr => ???
      case Low(exp) => ???
      case LowContext() => ???
      case PredicateConstructor(proxy, proxyT, args) => ???
      case OptionNone(elem) => ???
      case OptionSome(exp) => ???
      case OptionGet(exp) => ???
      case ArrayUpdate(base, left, right) => ???
      case RangeSequence(low, high) => ???
      case GhostCollectionUpdate(base, left, right, baseUnderlyingType) => ???
      case SequenceDrop(left, right) => ???
      case SequenceTake(left, right) => ???
      case SequenceConversion(expr) => ???
      case SetConversion(expr) => ???
      case MultisetConversion(expr) => ???
      case MapKeys(exp, expUnderlyingType) => ???
      case MapValues(exp, expUnderlyingType) => ???
      case MapConversion(expr) => ???
      case PureFunctionCall(func, args, typ, reveal) => ???
      case PureMethodCall(recv, meth, args, typ, reveal) => ???
      case PureClosureCall(closure, args, spec, typ) => ???
      case DomainFunctionCall(func, args, typ) => ???
      case StructUpdate(base, field, newVal) => ???
      case AdtDestructor(base, field) => ???
      case AdtDiscriminator(base, clause) => ???
      case operation: BoolOperation => ???
      case operation: IntOperation => ???
      case expr: BinaryExpr => ???
      case Conversion(newType, expr) => ???
      case Receive(channel, recvChannel, recvGivenPerm, recvGotPerm) => ???
      case lit: Lit => ???
      case DfltVal(typ) => ???
      case ClosureImplements(closure, spec) => ???
      case ClosureObject(func, typ) => ???
      case FunctionObject(func, typ) => ???
      case MethodObject(recv, meth, typ) => ???
      case Slice(base, low, high, max, baseUnderlyingType) => ???
      case Tuple(args) => ???
      case value: Var => ???
    }
  }

  private def transformStmt(originalProg: in.Program)(s: in.Stmt): in.Stmt = s match {
    case b@in.Block(decls, stmts) => in.Block(decls, stmts map transformStmt(originalProg))(b.info)
    case s@in.Seqn(stmts) => in.Seqn(stmts map transformStmt(originalProg))(s.info)
    case i: in.Initialization => i
    case a@in.Assert(ass) => in.Assert(transformAssert(originalProg)(ass))(a.info)
    case a@in.Assume(ass) => in.Assume(transformAssert(originalProg)(ass))(a.info)
    case a@in.SingleAss(l, r) =>
      val newL = transformAssignee(originalProg)(l)
      val newR = transformExprToIntendedType(originalProg)(r, l.op.typ)
      in.SingleAss(newL, newR)(a.info)
    case u@in.Fold(acc) =>
      val newAcc = transformAccess(originalProg)(acc)
      in.Fold(newAcc)(u.info)
    case u@in.Unfold(acc) =>
      val newAcc = transformAccess(originalProg)(acc)
      in.Unfold(newAcc)(u.info)
    case m@in.MakeSlice(target, typeParam, lenArg, capArg) =>
      // TODO: there's already some alternative means of preventing this error in the slice encoding.
      //       Chose a single solution
      m

    case i@in.If(cond, thn, els) =>
      val newCond = transformExpr(originalProg)(cond)
      val newThn = transformStmt(originalProg)(thn)
      val newEls = transformStmt(originalProg)(els)
      in.If(newCond, newThn, newEls)(i.info)
    case w@in.While(cond, invs, terminationMeasure, body) =>
      val newCond = transformExpr(originalProg)(cond)
      val newInvs = invs map transformAssert(originalProg)
      val newTerms = terminationMeasure map transformTermMeasure(originalProg)
      val newBody = transformStmt(originalProg)(body)
      in.While(newCond, newInvs, newTerms, newBody)(w.info)
    case l: in.Label => l
    case c: in.Continue => c
    case b: in.Break => b
    case r: Return => r

    case c@FunctionCall(targets, func, args) =>
      val f = originalProg.table.lookup(func)
      val argTypes = f match {
        case f: in.Function => f.args map (_.typ)
        case b: in.BuiltInFunction => b.argsT
        case _ =>
          // unreachable case
          ???
      }
      val newArgs = args zip argTypes map { case (arg, typ) =>
        transformExprToIntendedType(originalProg)(arg, typ)
      }
      in.FunctionCall(targets, func, newArgs)(c.info)
    case c@MethodCall(targets, recv, meth, args) =>
      val newRecv = transformExpr(originalProg)(recv)
      val m = originalProg.table.lookup(meth)
      val argTypes = m match {
        case f: in.Method => f.args map (_.typ)
        case b: in.BuiltInMethod => b.argsT
        case _ =>
          // unreachable case
          ???
      }
      val newArgs = args zip argTypes map { case (arg, typ) =>
        transformExprToIntendedType(originalProg)(arg, typ)
      }
      in.MethodCall(targets, newRecv, meth, newArgs)(c.info)
    case ClosureCall(targets, closure, args, spec) => ???
    case GoFunctionCall(func, args) => ???
    case GoMethodCall(recv, meth, args) => ???
    case GoClosureCall(closure, args, spec) => ???
  }

  private def allStmts(s: in.Stmt) = s match {
    case MethodBody(decls, seqn, postprocessing) => ???
    case MethodBodySeqn(stmts) => ???
    case stmt: MakeStmt => ???
    case New(target, expr) => ???
    case SafeTypeAssertion(resTarget, successTarget, expr, typ) => ???

    case deferrable: Deferrable => ???
    case Defer(stmt) => ???
    case Refute(ass) => ???
    case Inhale(ass) => ???
    case Exhale(ass) => ???
    case PackageWand(wand, block) => ???
    case ApplyWand(wand) => ???
    case Outline(name, pres, posts, terminationMeasures, backendAnnotations, body, trusted) => ???
    case Send(channel, expr, sendChannel, sendGivenPerm, sendGotPerm) => ???
    case SafeReceive(resTarget, successTarget, channel, recvChannel, recvGivenPerm, recvGotPerm, closed) => ???
    case SafeMapLookup(resTarget, successTarget, mapLookup) => ???
    case PatternMatchStmt(exp, cases, strict) => ???
    case PredExprFold(base, args, p) => ???
    case PredExprUnfold(base, args, p) => ???
    case EffectfulConversion(target, newType, expr) => ???
    case SpecImplementationProof(closure, spec, body, pres, posts) => ???
    case NewSliceLit(target, memberType, elems) => ???
    case NewMapLit(target, keys, values, entries) => ???
  }

  private def transformAssignee(originalProg: in.Program)(a: in.Assignee): in.Assignee = {
    // TODO
    a
  }

  private def transformAddressable(originalProg: in.Program)(a: in.Addressable): in.Addressable = a match {
    case v@in.Addressable.Var(_) => v
    case v@in.Addressable.GlobalVar(_) => v
    case in.Addressable.Pointer(op) =>
      val newOp = transformDeref(originalProg)(op)
      in.Addressable.Pointer(newOp)
    case in.Addressable.Field(op) =>
      val newOp = transformFieldRef(originalProg)(op)
      in.Addressable.Field(newOp)
    case in.Addressable.Index(op) =>
      val newBase = transformExpr(originalProg)(op.base)
      val newIdx = transformExprToIntendedType(originalProg)(op.index, intType)
      val newOp = in.IndexedExp(newBase, newIdx, op.baseUnderlyingType)(op.info)
      in.Addressable.Index(newOp)
  }

  private def transformFieldRef(originalProg: in.Program)(f: in.FieldRef): in.FieldRef = {
    val newExp = transformExpr(originalProg)(f.recv)
    in.FieldRef(newExp, f.field)(f.info)
  }

  private def transformDeref(originalProg: in.Program)(d: in.Deref): in.Deref = {
    val newExpr = transformExpr(originalProg)(d.exp)
    in.Deref(newExpr, d.underlyingTypeExpr)(d.info)
  }

  private def transformMethodBody(originalProg: in.Program)(b: in.MethodBody): in.MethodBody = b match {
    case b@in.MethodBody(decls, seqn, postprocessing) =>
      val newSeqn = in.MethodBodySeqn(seqn.stmts map transformStmt(originalProg))(seqn.info)
      val newPostprocessing = postprocessing map transformStmt(originalProg)
      in.MethodBody(decls, newSeqn, newPostprocessing)(b.info)
  }
  private def transformTermMeasure(originalProg: in.Program)(m: in.TerminationMeasure): in.TerminationMeasure = m match {
    case m@in.ItfMethodWildcardMeasure(cond) =>
      in.ItfMethodWildcardMeasure(cond map transformExpr(originalProg))(m.info)
    case m@in.NonItfMethodWildcardMeasure(cond) =>
      in.NonItfMethodWildcardMeasure(cond map transformExpr(originalProg))(m.info)
    case m@in.ItfTupleTerminationMeasure(tuple, cond) =>
      val newTuple = tuple map {
        case e: in.Expr => transformExpr(originalProg)(e)
        case o => o
      }
      val newCond = cond map transformExpr(originalProg)
      in.ItfTupleTerminationMeasure(newTuple, newCond)(m.info)
    case m@in.NonItfTupleTerminationMeasure(tuple, cond) =>
      val newTuple = tuple map {
        case e: in.Expr => transformExpr(originalProg)(e)
        case o => o
      }
      val newCond = cond map transformExpr(originalProg)
      in.NonItfTupleTerminationMeasure(newTuple, newCond)(m.info)
  }
  private def transformMember(originalProg: in.Program)(m: in.Member): in.Member =
    m match {
      case in.GlobalVarDecl(left, declStmts) => ???
      case g@in.GlobalConstDecl(left, right) =>
        ???
      case m@in.Method(receiver, name, args, results, pres, posts, terminationMeasures, backendAnnotations, body) =>
        val newPres = pres map transformAssert(originalProg)
        val newPosts = posts map transformAssert(originalProg)
        val newTermMeasures = terminationMeasures map transformTermMeasure(originalProg)
        val newBody = body map transformMethodBody(originalProg)
        in.Method(receiver, name, args, results, newPres, newPosts, newTermMeasures, backendAnnotations, newBody)(m.info)
      case m@in.PureMethod(receiver, name, args, results, pres, posts, terminationMeasures, backendAnnotations, body, isOpaque) =>
        val newPres = pres map transformAssert(originalProg)
        val newPosts = posts map transformAssert(originalProg)
        val newTermMeasures = terminationMeasures map transformTermMeasure(originalProg)
        val newBody = body map transformExpr(originalProg)
        in.PureMethod(receiver, name, args, results, newPres, newPosts, newTermMeasures, backendAnnotations, newBody, isOpaque)(m.info)
      case p@in.MethodSubtypeProof(subProxy, superT, superProxy, receiver, args, results, body) =>
        ???
      case p@in.PureMethodSubtypeProof(subProxy, superT, superProxy, receiver, args, results, body) =>
        ???
      case f@in.Function(name, args, results, pres, posts, terminationMeasures, backendAnnotations, body) =>
        val newPres = pres map transformAssert(originalProg)
        val newPosts = posts map transformAssert(originalProg)
        val newTermMeasures = terminationMeasures map transformTermMeasure(originalProg)
        val newBody = body map transformMethodBody(originalProg)
        in.Function(name, args, results, newPres, newPosts, newTermMeasures, backendAnnotations, newBody)(f.info)
      case f@in.PureFunction(name, args, results, pres, posts, terminationMeasures, backendAnnotations, body, isOpaque) =>
        val newPres = pres map transformAssert(originalProg)
        val newPosts = posts map transformAssert(originalProg)
        val newTermMeasures = terminationMeasures map transformTermMeasure(originalProg)
        val newBody = body map transformExpr(originalProg)
        in.PureFunction(name, args, results, newPres, newPosts, newTermMeasures, backendAnnotations, newBody, isOpaque)(f.info)
      case t: in.BuiltInMember => t
      case f@in.FPredicate(name, args, body) =>
        in.FPredicate(name, args, body.map(transformAssert(originalProg)))(f.info)
      case m@in.MPredicate(receiver, name, args, body) =>
        in.MPredicate(receiver, name, args, body.map(transformAssert(originalProg)))(m.info)
      case d@in.DomainDefinition(name, funcs, axioms) =>
        val newAxioms = axioms map {
          case a@in.DomainAxiom(ax) =>
            val newAx = transformExpr(originalProg)(ax)
            in.DomainAxiom(newAx)(a.info)
        }
        in.DomainDefinition(name, funcs, newAxioms)(d.info)
      case a: in.AdtDefinition => a
    }

  private def mergeTypes(originalProg: in.Program)(t1: in.Type, t2: in.Type): in.Type = {
    if (t1 == t2) {
      t1
    } else if (isNumericalType(originalProg)(t1) && isNumericalType(originalProg)(t2)) {
      (t1, t2) match {
        case (in.IntT(_, TypeBounds.UnboundedInteger), _) => t2
        case (_, in.IntT(_, TypeBounds.UnboundedInteger)) => t1
        case _ =>
          // cannot unify
          ???
      }

    } else {
      // cannot unify
      ???
    }

  }

  private def underlyingType(originalProg: in.Program)(t: in.Type): in.Type = t match {
    case d: in.DefinedT => underlyingType(originalProg)(originalProg.table.lookup(d))
    case o => o
  }

  private def isNumericalType(originalProg: in.Program)(t: in.Type): Boolean = {
    underlyingType(originalProg)(t) match {
      case i: in.IntT => true
      case _ => false
    }
  }
}
