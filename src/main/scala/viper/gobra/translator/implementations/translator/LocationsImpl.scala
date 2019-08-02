package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
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

  override def bottomDecl(v: in.BottomDeclaration)(ctx: Context): StmtWriter[((vpr.Declaration, vpr.Stmt), Context)] = {
    v match {
      case v: in.BodyVar =>
        val declaration = for {
          r <- variable(v)(ctx)

          // TODO handle underlying struct case: inhale permissions to fields ...
          // inhale permissions if necessary
          _ <- v match {
            case v: in.LocalVar.Ref =>
              val inhalePermissions =
                in.Inhale(in.Access(
                  in.Accessible.Pointer(in.Deref(in.Ref(in.Addressable.Var(v), in.PointerT(v.typ))(v.info), v.typ)(v.info))
                )(v.info))(v.info)

              prelim(ctx.stmt.translate(inhalePermissions)(ctx))
            case _ => unit(())
          }

          // TODO: handle underlying struct case: ... and assign the values correspondingly
          // assign default Value
          _ <- {
            val init = in.SingleAss(in.Assignee.Var(v), in.DfltVal(v.typ)(v.info))(v.info)
            prelim(ctx.stmt.translate(init)(ctx))
          }
        } yield r

        val (x, u) = declaration.cut
        sl.closeE(u)(x).map{ s => ((ViperUtil.toVarDecl(x), s), ctx)}
    }
  }


  override def assignment(ass: in.SingleAss)(ctx: Context): StmtWriter[vpr.Stmt] =
    assignment(ass.left, ass.right)(ass)(ctx)

  def assignment(left: in.Assignee, right: in.Expr)(src: in.Node)(ctx: Context): StmtWriter[vpr.Stmt] = sl.withDeepInfo(src){sl.seqnE{
    for {
      right <- ctx.expr.translate(right)(ctx)

      assignment <- left match {
        case in.Assignee.Var(v: in.LocalVar.Val) =>
          for {l <- rvalue(v)(ctx)} yield vpr.LocalVarAssign(l.asInstanceOf[vpr.LocalVar], right)()

        case in.Assignee.Var(v: in.LocalVar.Ref) =>
          for {l <- rvalue(v)(ctx)} yield vpr.FieldAssign(l.asInstanceOf[vpr.FieldAccess], right)()

        case in.Assignee.Pointer(p) => ???
          // for {l <- deref(p)(ctx)} yield vpr.FieldAssign(l, right)()

        case in.Assignee.Field(f) => ???
      }
    } yield assignment
  }}



  /**
    * L[x]  -> x
    * L[&e] -> assert L[e] != null; L[e]
    * L[*e] -> L[e]
    * L[e.f] -> LProj[e](path(e.f) ++ f)
    *
    * translates a location for a receiver position, except keeps addresses if possible
    */
  override def lvalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){
    l match {
      case v: in.Var => variable(v)(ctx)

      case in.Ref(ref, _) =>
        for {
          address <- lvalue(ref.op)(ctx)
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
        lproj(recv, fields)(ctx)
    }
  }

  /**
    * R[!x: not S]  -> L[!x].val
    * R[*e: not S]  -> L[*e].val
    * R[e.!f: not S] -> L[e.f].val
    * R[e] -> L[e]
    *
    * translates a location for a receiver position
    */
  override def rvalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){
    val lval = lvalue(l)(ctx)

    if (isAddressable(l) && !isStructValue(l.typ)) {
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

    if (isAddressable(l) && isStructValue(l.typ)) {
      ??? // TODO: purify
    } else {
      rval
    }
  }

  override def callReceiver(recv: in.Expr, path: in.MemberPath)(ctx: Context): ExprWriter[vpr.Exp] = {
    val lastFieldIdx = path.path.lastIndexWhere(_.isInstanceOf[in.MemberPath.Next])
    val (promotionPath, afterPath) = path.path.splitAt(lastFieldIdx + 1)

    val fields = promotionPath.collect{ case in.MemberPath.Next(f) => f }

    // after the field accesses, only one ref, deref, or nothing can occur
    afterPath match {
      case Vector() => rproj(recv, fields)(ctx)
      case Vector(in.MemberPath.Deref) => ??? // TODO purify  rproj
      case Vector(in.MemberPath.Ref) => lproj(recv, fields)(ctx)
      case _ => Violation.violation("Found ill formed resolution path")
    }
  }

  /**
    * LProj[e](f:g:fs) = LProj[RProj[e]f](g:fs)
    *
    * LProj[!x]f   -> R[!x].f
    * LProj[*e]f   -> R[*e].f
    * LProj[e.!f]f -> R[e.!g].f
    * LProj[e: S]f -> f(R[e])
    * LProj[e]f    -> R[e].f
    *
    * RProj[!x: not S]f   -> LProj[!x]f.val
    * RProj[*e: not S]f   -> LProj[*e]f.val
    * RProj[e.!g: not S]f -> LProj[e.g]f.val
    * RProj[e]f           -> LProj[e]f
    *
    * translates a a field access keeping the last address
    */
  def lproj(recv: in.Expr, fields: Vector[in.Field])(ctx: Context): ExprWriter[vpr.Exp] = {
    if (fields.isEmpty) {
      ctx.expr.translate(recv)(ctx)
    } else {
      def isAddressableF(f: in.Field): Boolean = f.isInstanceOf[in.Field.Ref]
      def isStructValueF(f: in.Field): Boolean = isStructValue(f.typ)

      def initlproj(exp: in.Expr, f: in.Field)(ctx: Context): ExprWriter[vpr.Exp] = exp match {
        case r: in.Location if isAddressable(r) => rvalue(r)(ctx) map (r => vpr.FieldAccess(r, field(f)(ctx))())
        case e if isStructValue(e.typ) => ??? // TODO: tuple projection
        case e => ctx.expr.translate(e)(ctx) map (r => vpr.FieldAccess(r, field(f)(ctx))())
      }

      fields.tail.foldLeft((initlproj(recv, fields.head)(ctx), fields.head)){
        case ((lrecv: ExprWriter[vpr.Exp], lastF: in.Field), f: in.Field) =>

          val isLastA = isAddressableF(lastF)
          val isLastS = isStructValueF(lastF)

          val e = for {
            lr <- lrecv

            // inlined r-value
            rr = if (isLastA && !isLastS) {
              vpr.FieldAccess(lr, pointerField(lastF.typ)(ctx))()
            } else lr

            // inlined field projection
            proj = if (!isLastA && isLastS) {
              ??? // TODO: tuple projection
            } else {
              vpr.FieldAccess(rr, field(f)(ctx))()
            }
          } yield proj

          (e, f)
      }._1
    }
  }

  /**
    * RProj[!x: not S]f   -> LProj[!x]f.val
    * RProj[*e: not S]f   -> LProj[*e]f.val
    * RProj[e.!g: not S]f -> LProj[e.g]f.val
    * RProj[e]f           -> LProj[e]f
    *
    * translates a a field access
    */
  def rproj(recv: in.Expr, fields: Vector[in.Field])(ctx: Context): ExprWriter[vpr.Exp] = {
    if (fields.isEmpty) {
      ctx.expr.translate(recv)(ctx)
    } else {
      val lastF = fields.last
      val isNonStructAddressable = lastF.isInstanceOf[in.Field.Ref] && !isStructValue(lastF.typ)

      val lval = lproj(recv, fields)(ctx)

      if (isNonStructAddressable) {
        lval map (r => vpr.FieldAccess(r, pointerField(lastF.typ)(ctx))())
      } else {
        lval
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
        case Some(st) => rvalue(der)(ctx) map (structAccess(_, st))
        case None => rvalue(der)(ctx) map (derefAccess(_, der.typ))
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
    * Acc[!x]f   -> acc(R[!x].f, perm)
    * Acc[*e]f   -> acc(R[*e].f, perm)
    * Acc[e.!f]f -> acc(R[e.!g].f, perm)
    * Acc[e: S]f -> true
    * Acc[e]f    -> acc(R[e].f, perm)
    *
    * translates a field access to a permission
    */
  def projAccess(recv: in.Expr, pathFields: Vector[in.Field], f: in.Field, perm: vpr.Exp = vpr.FullPerm()())(ctx: Context): ExprWriter[vpr.Exp] = {

    def fieldAccessPred(e: vpr.Exp): vpr.Exp =
      vpr.FieldAccessPredicate(vpr.FieldAccess(e, field(f)(ctx))(), perm)()

    if (pathFields.isEmpty) {
      recv match {
        case r: in.Location if isAddressable(r) => rvalue(r)(ctx) map fieldAccessPred
        case e if isStructValue(e.typ) => unit(vpr.TrueLit()())
        case e => ctx.expr.translate(e)(ctx) map fieldAccessPred
      }
    } else {
      val lastF = pathFields.last
      val isTupleCase = !lastF.isInstanceOf[in.Field.Ref] && isStructValue(lastF.typ)

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



  def isAddressable(exp: in.Expr): Boolean = exp match {
    case _: in.LocalVar.Ref => true
    case fr: in.FieldRef => fr.field.isInstanceOf[in.Field.Ref]
    case _: in.Deref => true
    case _ => false
  }

  def isStructValue(typ: in.Type): Boolean = structType(typ).nonEmpty

  def structType(typ: in.Type): Option[in.StructT] = typ match {
    case in.DefinedT(_, right) => structType(right)
    case st: in.StructT => Some(st)
    case _ => None
  }
}
