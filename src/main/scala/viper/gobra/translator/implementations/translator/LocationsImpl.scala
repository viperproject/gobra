package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.ast.internal.Types.{isStructType, structType, unrefType}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Locations
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{PrimitiveGenerator, ViperUtil}
import viper.gobra.translator.util.ViperWriter.{ExprWriter, MemberWriter, StmtWriter}
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source.{withInfo => nodeWithInfo}
import viper.gobra.util.Violation

class LocationsImpl extends Locations {

  import viper.gobra.translator.util.ViperWriter.ExprLevel._
  import viper.gobra.translator.util.ViperWriter.{StmtLevel => sl, MemberLevel => ml}

  override def finalize(col: Collector): Unit = {
    _pointerField.finalize(col)
  }

  private lazy val _pointerField: PrimitiveGenerator.PrimitiveGenerator[vpr.Type, vpr.Field] =
    PrimitiveGenerator.simpleGenerator(
      (t: vpr.Type) => {
        val f = vpr.Field(name = Names.pointerFields(t), typ = t)()
        (f, Vector(f))
      }
    )

  private def pointerField(t: in.Type)(ctx: Context): vpr.Field = _pointerField(ctx.typ.translate(t)(ctx))

  /**
    * [v]w -> v
    */
  override def variable(v: in.Var)(ctx: Context): ExprWriter[vpr.LocalVar] = withDeepInfo(v){

    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    v match {
      case in.Parameter(id, t)    => unit(vpr.LocalVar(id, goT(t))())
      case in.LocalVar.Val(id, t) => unit(vpr.LocalVar(id, goT(t))())
      case in.LocalVar.Ref(id, t) => unit(vpr.LocalVar(id, vpr.Ref)())
    }
  }

  override def fieldDecl(f: in.Field)(ctx: Context): Vector[vpr.Field] = {
    Vector(field(f)(ctx))
  }

  override def topDecl(v: in.TopDeclaration)(ctx: Context): MemberWriter[((vpr.LocalVarDecl, StmtWriter[vpr.Stmt]), Context)] = {
    v match {
      case v: in.Var =>
        ml.splitE(variable(v)(ctx)).map{ case (e, w) =>
          ((ViperUtil.toVarDecl(e), sl.closeE(w)(e)), ctx)
        }
    }
  }

  /**
    * [decl ?x: T] -> var x [T]; x := dflt(T)
    * [decl !x: not S] -> var x Ref; inhale acc(x.val); x.val := dflt(T)
    * [decl !x: S] -> var x Ref; FOREACH f: T in S. inhale acc(x.f); x.f := dflt(T)
    */
  override def bottomDecl(v: in.BottomDeclaration)(ctx: Context): StmtWriter[((vpr.Declaration, vpr.Stmt), Context)] = {
    val src = v.info

    v match {
      case v: in.BodyVar =>
        val declaration = for {
          r <- variable(v)(ctx)

          stOpt = structType(v.typ)
          _ <- v match {
            case v: in.LocalVar.Val =>
              val init = in.SingleAss(in.Assignee.Var(v), in.DfltVal(v.typ)(src))(src)
              prelim(ctx.stmt.translate(init)(ctx))

            case v: in.LocalVar.Ref if stOpt.isEmpty =>
              val vInhalePermissions =
                sl.withDeepInfo(v)(sl.seqnE(
                  for {
                    x <- variable(v)(ctx)
                    facc = vpr.FieldAccess(x, pointerField(v.typ)(ctx))()
                    faccp = vpr.FieldAccessPredicate(facc, vpr.FullPerm()())()
                  } yield vpr.Inhale(faccp)()
                ))

              val init = in.SingleAss(in.Assignee.Var(v), in.DfltVal(v.typ)(src))(src)

              prelim(
                vInhalePermissions,
                ctx.stmt.translate(init)(ctx)
              )

            case v: in.LocalVar.Ref if stOpt.nonEmpty =>

              val perField = stOpt.get.fields.flatMap{ f =>
                val fieldRef = in.FieldRef(v, f, in.MemberPath(Vector.empty))(src)

                val inhalePermission = in.Inhale(in.Access(in.Accessible.Field(fieldRef))(src))(src)
                val init = in.SingleAss(in.Assignee.Field(fieldRef), in.DfltVal(f.typ)(src))(src)

                Vector(inhalePermission, init)
              }

              prelim(perField map ctx.stmt.translateF(ctx): _*)
          }
        } yield r

        val (x, u) = declaration.cut
        sl.closeE(u)(x).map{ s => ((ViperUtil.toVarDecl(x), s), ctx)}
    }
  }


  override def assignment(ass: in.SingleAss)(ctx: Context): StmtWriter[vpr.Stmt] =
    assignment(ass.left, ass.right)(ass)(ctx)

  /**
    * [!r: not S = e] -> A[!r].val = [e]
    * [!r: S = e] -> ~z = [e]; ~l = R[!r]; FOREACH g in S. l.g = g(z)
    *
    * [x = e]   -> x = [e]
    * [r.f = e] -> ProjAss[r ~ [e] ](path(r.f), f)
    */
  def assignment(left: in.Assignee, right: in.Expr)(src: in.Node)(ctx: Context): StmtWriter[vpr.Stmt] = sl.withDeepInfo(src){

    left.op match {
      case l: in.Location if isAddressable(l) =>
        structType(l.typ) match {
          case None =>
            sl.seqnE(
              for {
                e <- ctx.expr.translate(right)(ctx)
                r <- avalue(l)(ctx)
              } yield vpr.FieldAssign(vpr.FieldAccess(r, pointerField(l.typ)(ctx))(), e)()
            )

          case Some(st) =>
            sl.seqnE(
              for {
                e <- ctx.expr.translate(right)(ctx)
                z = vpr.LocalVar(Names.freshName, e.typ)()
                _ <- addStatements(vpr.LocalVarAssign(z, e)())

                r <- rvalue(l)(ctx)
                l = vpr.LocalVar(Names.freshName, r.typ)()
                _ <- addStatements(vpr.LocalVarAssign(l, r)())

                _ <- addLocals(z, l)
                ass <- sequence(st.fields map {g =>
                  for {
                    rhs <- accessStructValueField(z, g)(ctx)
                    ass = vpr.FieldAssign(vpr.FieldAccess(l, field(g)(ctx))(), rhs)()
                  } yield ass
                })
              } yield vpr.Seqn(ass, Vector.empty)()
            )
        }

      case x: in.LocalVar.Val =>
        sl.seqnE(
          for {
            e <- ctx.expr.translate(right)(ctx)
            v <- variable(x)(ctx)
          } yield vpr.LocalVarAssign(v, e)()
        )

      case fa: in.FieldRef =>
        val pathFields = fa.path.path.collect{ case in.MemberPath.Next(f) => f }
        sl.seqnE(
          for {
            e <- ctx.expr.translate(right)(ctx)
            ass <- exprS(projAss(fa.recv, pathFields, fa.field, e)(ctx))
          } yield ass
        )
    }
  }

  override def make(mk: in.Make)(ctx: Context): StmtWriter[vpr.Stmt] = {
    val src = mk.info

    mk.typ match {
      case in.CompositeObject.Struct(slit) =>
        val v = in.LocalVar.Val(Names.freshName, mk.target.typ)(src)
        val perField = slit.fieldZip.flatMap{ case (f, e) =>
          val fieldRef = in.FieldRef(v, f, in.MemberPath(Vector.empty))(src)
          val inhalePermission = in.Inhale(in.Access(in.Accessible.Field(fieldRef))(src))(src)
          val init = in.SingleAss(in.Assignee.Field(fieldRef), in.DfltVal(f.typ)(src))(src)
          Vector(inhalePermission, init)
        }

        sl.seqnE{
          for {
            vTarget <- variable(mk.target)(ctx)

            vv <- variable(v)(ctx)
            _ <- addLocals(vv)

            _ <- prelim(perField map ctx.stmt.translateF(ctx): _*)

          } yield vpr.LocalVarAssign(vTarget, vv)()
        }
    }
  }



  /**
    * A[x]   -> x
    * A[&e]  -> assert A[e] != null; A[e]
    * A[*e]  -> A[e]
    * A[e.f] -> AProj[e](path(e.f), f)
    *
    * translates a location for a receiver position, except keeps addresses if possible
    */
  def avalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){
    l match {
      case v: in.Var => variable(v)(ctx)

      case in.Ref(ref, _) =>
        for {
          address <- avalue(ref.op)(ctx)
          // assert address != null
          _ <- addStatements(vpr.Assert(vpr.NeCmp(address, vpr.NullLit()())())())
        } yield address

      case in.Deref(exp, typ) =>
        for {
          rcv <- ctx.expr.translate(exp)(ctx)
          field = pointerField(typ)(ctx)
        } yield vpr.FieldAccess(rcv, field)()

      case in.FieldRef(recv, field, path) =>
        val fields = path.path.collect{ case in.MemberPath.Next(f) => f } :+ field
        aproj(recv, fields)(ctx)
    }
  }

  def extendedRValue(exp: in.Expr)(ctx: Context): ExprWriter[vpr.Exp] = exp match {
    case l: in.Location => rvalue(l)(ctx)
    case _ => ctx.expr.translate(exp)(ctx)
  }

  /**
    * R[!x: not S]   -> A[!x].val
    * R[*e: not S]   -> A[*e].val
    * R[e.!f: not S] -> A[e.f].val
    * R[e] -> A[e]
    *
    * translates a location for a receiver position
    */
  override def rvalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){
    val lval = avalue(l)(ctx)

    if (isAddressable(l) && !isStructType(l.typ)) {
      lval map (r => vpr.FieldAccess(r, pointerField(l.typ)(ctx))())
    } else {
      lval
    }
  }

  /**
    * E[!x: S]  -> purify(R[!x])
    * E[*e: S]  -> purify(R[*e])
    * E[e.!f: S] -> purify(R[e.f])
    * E[e] -> R[e]
    *
    * translates a location for an expression position
    */
  override def evalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){
    val rval = rvalue(l)(ctx)

    if (isAddressable(l) && isStructType(l.typ)) {
      rval flatMap (r => purify(r, l.typ)(ctx))
    } else {
      rval
    }
  }

  /**
    * CR[r](mp) -> CRH[r](fs;l)
    *  where fs, l = mp such that l does not contain fields and fs is either empty or ends with a field
    *
    * CRH[r](fs;()) -> RProj[r]fs
    * CRH[r](fs;&)  -> AProj[r]fs
    * CRH[r](fs;*)  -> purify(RProj[r]fs) // Note: the qualified receiver has to be a struct
    *
    * translates a receiver of a call
    */
  override def callReceiver(recv: in.Expr, path: in.MemberPath)(ctx: Context): ExprWriter[vpr.Exp] = {
    val lastFieldIdx = path.path.lastIndexWhere(_.isInstanceOf[in.MemberPath.Next])
    val (promotionPath, afterPath) = path.path.splitAt(lastFieldIdx + 1)

    val fields = promotionPath.collect{ case in.MemberPath.Next(f) => f }

    // after the field accesses, only one ref, deref, or nothing can occur
    afterPath match {
      case Vector() => rproj(recv, fields)(ctx)
      case Vector(in.MemberPath.Ref) => aproj(recv, fields)(ctx)

      case Vector(in.MemberPath.Deref) =>
        val pt = fields.headOption.map(f => f.typ).getOrElse(recv.typ)
        rproj(recv, fields)(ctx) flatMap (r => purify(r, unrefType(pt).get)(ctx))

      case _ => Violation.violation("Found ill formed resolution path")
    }
  }

  /**
    * AProj[e]() -> A[e], where e is addressable
    *
    * AProj[!r]f    -> R[!r].f
    * AProj[?e: S]f -> f(R[e])
    * AProj[e]f     -> R[e].f
    *
    * AProj[e](fs, ?g: S, f) -> f(RProj[e](fs, g))
    * AProj[e](fs, g, f)     -> RProj[e](fs, g).f
    *
    * translates a a field access keeping the last address
    */
  def aproj(recv: in.Expr, fields: Vector[in.Field])(ctx: Context): ExprWriter[vpr.Exp] = fields match {
    case Vector() =>
      Violation.violation(isAddressable(recv), s"expected addressable expression, but got $recv")
      avalue(recv.asInstanceOf[in.Location])(ctx)

    case Vector(f) => recv match {
      case r: in.Location if isAddressable(r) => rvalue(r)(ctx) map (r => vpr.FieldAccess(r, field(f)(ctx))())
      case e if isStructType(e.typ) => ctx.expr.translate(e)(ctx) flatMap (r => accessStructValueField(r, f)(ctx))
      case e => ctx.expr.translate(e)(ctx) map (r => vpr.FieldAccess(r, field(f)(ctx))())
    }

    case fsgf =>
      val (fsg, f) = (fsgf.init, fsgf.last)
      val (fs, g)  = (fsg.init, fsg.last)

      if (!isAddressableF(g) && isStructTypeF(g)) {
        rproj(recv, fsg)(ctx) flatMap (r => accessStructValueField(r, f)(ctx))
      } else {
        rproj(recv, fsg)(ctx) map (r => vpr.FieldAccess(r, field(f)(ctx))())
      }
  }

  /**
    * RProj[e]() -> R[e]
    * RProj[e](fs, !f: not S) -> AProj[e](fs, !f).val
    * RProj[e]fs -> AProj[e]fs
    *
    * translates a field access
    */
  def rproj(recv: in.Expr, fields: Vector[in.Field])(ctx: Context): ExprWriter[vpr.Exp] = {
    if (fields.isEmpty) {
      extendedRValue(recv)(ctx)
    } else {
      val lastF = fields.last
      val lval = aproj(recv, fields)(ctx)

      if (isAddressableF(lastF) && !isStructTypeF(lastF)) {
        lval map (r => vpr.FieldAccess(r, pointerField(lastF.typ)(ctx))())
      } else {
        lval
      }
    }
  }

  /**
    * ProjAss[r ~ e](fs, !f: not S) -> AProj[e](fs, !f).val = e
    * ProjAss[r ~ e](fs, !f: S) -> ~z = e; ~l = RProj[r](fs,f); FOREACH g in S. l.g = g(z)
    *
    * ProjAss[?x: S ~ e]f -> x = x{f -> e}
    * ProjAss[r.?g: S ~ e]f -> ProjAss[r ~ e](path(r.g), g, f)
    * ProjAss[?r: S ~ e]f -> _ = R[r]
    * ProjAss[r ~ e](fs, ?g: S, f) -> ProjAss[r ~ RProj[r](fs,g){f -> e}](fs, g)
    *
    * ProjAss[r ~ e](fs,f) -> RProj[r]fs.f = e
    *
    * translates a field assignment
    */

  def projAss(recv: in.Expr, pathFields: Vector[in.Field], f: in.Field, e: vpr.Exp)(ctx: Context): StmtWriter[vpr.Stmt] = {
    if (isAddressableF(f)) {
      if (isStructTypeF(f)) {
        val st = structType(f.typ).get
        val z = vpr.LocalVar(Names.freshName, e.typ)()
        sl.seqnE(
          for {
            _ <- addStatements(vpr.LocalVarAssign(z, e)())

            r <- rproj(recv, pathFields :+ f)(ctx)
            l = vpr.LocalVar(Names.freshName, r.typ)()
            _ <- addStatements(vpr.LocalVarAssign(l, r)())

            _ <- addLocals(z, l)
            ass <- sequence(st.fields map {g =>
              for {
                rhs <- accessStructValueField(z, g)(ctx)
                ass = vpr.FieldAssign(vpr.FieldAccess(l, field(g)(ctx))(), rhs)()
              } yield ass
            })
          } yield vpr.Seqn(ass, Vector.empty)()
        )
      } else {
        sl.seqnE(
          for {
            r <- aproj(recv, pathFields :+ f)(ctx)
          } yield vpr.FieldAssign(vpr.FieldAccess(r, pointerField(f.typ)(ctx))(), e)()
        )
      }
    } else if (pathFields.isEmpty) {
      recv match {
        case l: in.Location if isAddressable(l) =>
          sl.seqnE(for {r <- rvalue(l)(ctx)} yield vpr.FieldAssign(vpr.FieldAccess(r, field(f)(ctx))(), e)())

        case v: in.LocalVar.Val =>
          sl.seqnE(for {x <- variable(v)(ctx)} yield vpr.LocalVarAssign(x, e)())

        case fa: in.FieldRef =>
          val pathFields = fa.path.path.collect{ case in.MemberPath.Next(q) => q }
          projAss(recv, pathFields :+ fa.field, f, e)(ctx)

        case _ => sl.seqnE(ctx.expr.translate(recv)(ctx) map (_ => vpr.Seqn(Vector.empty, Vector.empty)()))
      }
    } else {
      val lastF = pathFields.last
      if (!isAddressableF(lastF) && isStructTypeF(lastF)) {
        sl.seqnE(
          for {
            r <- rproj(recv, pathFields)(ctx)
            upd <- updateStructValueField(r, f, e)(ctx)
            ass <- exprS(projAss(recv, pathFields.init, lastF, upd)(ctx))
          } yield ass
        )
      } else {
        sl.seqnE(for {r <- rproj(recv, pathFields)(ctx)} yield vpr.FieldAssign(vpr.FieldAccess(r, field(f)(ctx))(), e)())
      }
    }
  }

  /**
    * [acc(*e: S)] -> AND g in S. acc(R[e].g)
    * [acc(*e: not S)] -> acc(R[e].val)
    * [acc(e.f)] -> Acc[e]f
    * [acc(e.!f: S)] -> Acc[e]f && AND g in S. acc(R[e.!f].g)
    * [acc(e.!f: not S)] -> Acc[e]f && acc(R[e.!f].val)
    *
    * TODO
    * [acc(&e.!f)] -> Acc[e]f
    * [acc(&!x)] -> true
    *
    * translates a a field access keeping the last address
    */
  override def access(acc: in.Access)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(acc){

    def structAccess(recv: vpr.Exp, st: in.StructT, perm: vpr.Exp = vpr.FullPerm()()): vpr.Exp =
      ViperUtil.bigAnd(st.fields map (f =>
        vpr.FieldAccessPredicate(vpr.FieldAccess(recv, field(f)(ctx))(), perm)())
      )

    def derefAccess(recv: vpr.Exp, t: in.Type, perm: vpr.Exp = vpr.FullPerm()()): vpr.Exp =
      vpr.FieldAccessPredicate(vpr.FieldAccess(recv, pointerField(t)(ctx))(), perm)()

    acc.e match {
      case in.Accessible.Pointer(der) => structType(der.typ) match {
        case Some(st) => extendedRValue(der.exp)(ctx) map (structAccess(_, st))
        case None => extendedRValue(der.exp)(ctx) map (derefAccess(_, der.typ))
      }

      case in.Accessible.Field(fa) =>
        val pathFields = fa.path.path.collect{ case in.MemberPath.Next(f) => f }
        val pacc = projAccess(fa.recv, pathFields, fa.field)(ctx)
        lazy val base = rproj(fa.recv, pathFields :+ fa.field)(ctx)

        (fa.field, structType(fa.typ)) match {
        case (f: in.Field.Ref, Some(st)) => for {pa <- pacc; r <- base} yield vpr.And(pa, structAccess(r, st))()
        case (f: in.Field.Ref, None)     => for {pa <- pacc; r <- base} yield vpr.And(pa, derefAccess(r, fa.typ))()
        case (f: in.Field.Val, _)        => pacc
      }
    }
  }

  /**
    * Acc[?e: S]f -> true
    * Acc[e]f    -> acc(R[e].f, perm)
    *
    * Acc[e](fs, ?g: S, f) -> true
    * Acc[e](fs, g, f)     -> acc(RProj[e](fs, g).f, perm)
    *
    * translates a field access to a permission
    */
  def projAccess(recv: in.Expr, pathFields: Vector[in.Field], f: in.Field, perm: vpr.Exp = vpr.FullPerm()())(ctx: Context): ExprWriter[vpr.Exp] = {

    def fieldAccessPred(e: vpr.Exp): vpr.Exp =
      vpr.FieldAccessPredicate(vpr.FieldAccess(e, field(f)(ctx))(), perm)()

    if (pathFields.isEmpty) {
      recv match {
        case r: in.Location if isAddressable(r) => rvalue(r)(ctx) map fieldAccessPred
        case e if isStructType(e.typ) => unit(vpr.TrueLit()())
        case e => ctx.expr.translate(e)(ctx) map fieldAccessPred
      }
    } else {
      val lastF = pathFields.last
      val isTupleCase = !isAddressableF(lastF) && isStructTypeF(lastF)

      if (isTupleCase) {
        unit(vpr.TrueLit()())
      } else {
        val actualRecv = rproj(recv, pathFields)(ctx)
        actualRecv map fieldAccessPred
      }
    }
  }

  def field(f: in.Field)(ctx: Context): vpr.Field = f match {
    case _: in.Field.Val => nodeWithInfo(vpr.Field(f.name, ctx.typ.translate(f.typ)(ctx)))(f)
    case _: in.Field.Ref => nodeWithInfo(vpr.Field(f.name, vpr.Ref))(f)
  }

  def isAddressableF(f: in.Field): Boolean = f.isInstanceOf[in.Field.Ref]
  def isStructTypeF(f: in.Field): Boolean = isStructType(f.typ)

  def isAddressable(exp: in.Expr): Boolean = exp match {
    case _: in.LocalVar.Ref => true
    case fr: in.FieldRef => fr.field.isInstanceOf[in.Field.Ref]
    case _: in.Deref => true
    case _ => false
  }


  def purify(exp: vpr.Exp, typ: in.Type)(ctx: Context): ExprWriter[vpr.Exp] = {
    require(isStructType(typ))
    ???
  }
  def accessStructValueField(exp: vpr.Exp, f: in.Field)(ctx: Context): ExprWriter[vpr.Exp] = ???
  def updateStructValueField(exp: vpr.Exp, f: in.Field, newVal: vpr.Exp)(ctx: Context): ExprWriter[vpr.Exp] = ???
}
