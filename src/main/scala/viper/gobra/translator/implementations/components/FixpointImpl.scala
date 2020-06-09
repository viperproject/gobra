package viper.gobra.translator.implementations.components

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.components.Fixpoint
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter
import viper.silver.ast.{Exp, Type, TypeVar}
import viper.silver.{ast => vpr}

class FixpointImpl extends Fixpoint {

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = {
    generatedDomains foreach col.addMember
  }

  override def create(gc: in.GlobalConstDecl)(ctx: Context): Unit = {
    // TODO generalize to GlobalConstDecls
    val sgc = gc.asInstanceOf[in.SingleGlobalConstDecl]

    val domainName = constantDomainName(sgc.left.id)
    val getFunc = constantGetDomainFunc(sgc.left)(ctx)
    val getAxiom = {
      val getAxiomName = s"get_constant${sgc.left.id}"
      val (pos, info, errT) = gc.vprMeta
      val funcEquality = ctx.loc.equal(sgc.left, sgc.right)(gc)(ctx)
      val bindings = funcEquality.sum.data.collect { case b: ViperWriter.Binding => b}
      val axiomExpr = bindings.foldLeft(funcEquality.res) { (expr, b) =>
        vpr.Let(vu.toVarDecl(b.v), b.e, expr)(pos, info, errT)
      }

      vpr.NamedDomainAxiom(
        name = getAxiomName,
        exp = axiomExpr,
      )(domainName = domainName)
    }

    val domain = vpr.Domain(
      domainName,
      Seq(getFunc),
      Seq(getAxiom),
      Seq()
    )()

    _generatedDomains ::= domain
  }

  override def get(gc: in.GlobalConst)(ctx: Context): vpr.DomainFuncApp =
    vpr.DomainFuncApp(constantGetDomainFunc(gc)(ctx), Seq[Exp](), Map[TypeVar, Type]())()

  def generatedDomains: List[vpr.Domain] = _generatedDomains

  private var _generatedDomains: List[vpr.Domain] = List.empty

  private def constantDomainName(id: String): String = s"Constant${id}"

  private def constantGetDomainFunc(gc: in.GlobalConst)(ctx: Context): vpr.DomainFunc = {
    // TODO generalize to GlobalConst
    val v = gc.asInstanceOf[in.GlobalConst.Val]

    val getFuncName = s"constant${v.id}"

    vpr.DomainFunc(
      name = getFuncName,
      formalArgs = Seq(),
      typ = ctx.typ.translate(v.typ)(ctx)
    )(domainName = constantDomainName(v.id))
  }
}
