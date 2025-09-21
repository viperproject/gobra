package viper.gobra.ast.internal.transform
import viper.gobra.ast.{internal => in}
import viper.gobra.util.TypeBounds

// TODO: doc
object IntConversionInferenceTransform extends InternalTransform {
  override def name(): String = "int_conversion_inference"

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
      case a@in.ExprAssertion(e) => in.ExprAssertion(transformExpr(originalProg)(e))(a.info)
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
      case o =>
        // TODO: apply transform to all cases
        o
    }
  }

  private def transformPredicateAccess(originalProg: in.Program)(acc: in.PredicateAccess): in.PredicateAccess =
    // TODO: apply transform to all cases
    acc match {
      case in.FPredicateAccess(pred, args) => ???
      case in.MPredicateAccess(recv, pred, args) => ???
      case in.MemoryPredicateAccess(arg) => ???
    }

  private def transformExpr(originalProg: in.Program)(e: in.Expr): in.Expr = transformExprToIntendedType(originalProg)(e, e.typ)
  private def transformExprToIntendedType(originalProg: in.Program)(e: in.Expr, intededType: in.Type): in.Expr = {
    val newE: in.Expr = e match {
      case e@in.EqCmp(l, r) =>
        (l.typ, r.typ) match {
          case (t1: in.IntT, t2: in.IntT) if t1 != t2 =>
            assert(t1.addressability == t2.addressability)
            val newKind = TypeBounds.merge(t1.kind, t2.kind)
            val newType = in.IntT(t1.addressability, newKind)
            in.EqCmp(transformExprToIntendedType(originalProg)(l, newType), transformExprToIntendedType(originalProg)(r, newType))(e.info)
          case _ => e
        }
      case i => i
    }
    if (newE.typ == intededType) newE else in.Conversion(intededType, newE)(newE.info)
  }
  private def transformStmt(originalProg: in.Program)(s: in.Stmt): in.Stmt = s match {
    case b@in.Block(decls, stmts) => in.Block(decls, stmts map transformStmt(originalProg))(b.info)
    case s@in.Seqn(stmts) => in.Seqn(stmts map transformStmt(originalProg))(s.info)
    case i: in.Initialization => i
    case a@in.Assert(ass) => in.Assert(transformAssert(originalProg)(ass))(a.info)
    case a@in.Assume(ass) => in.Assume(transformAssert(originalProg)(ass))(a.info)
    case a@in.SingleAss(l, r) =>
      val newR = transformExprToIntendedType(originalProg)(r, l.op.typ)
      in.SingleAss(l, newR)(a.info)
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
}
