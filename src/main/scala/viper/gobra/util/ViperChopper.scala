// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0

package viper.gobra.util
import viper.silver.{ast => vpr}

object ViperChopper {
  /** chops 'choppee' into independent Viper programs */
  def chop(choppee: vpr.Program): Vector[vpr.Program] = {

    val methodTable = choppee.methods.map(n => (n.name, n)).toMap
    val functionTable = choppee.functions.map(n => (n.name, n)).toMap
    val predicateTable = choppee.predicates.map(n => (n.name, n)).toMap
    val fieldTable = choppee.fields.map(n => (n.name, n)).toMap
    val domainTable = choppee.domains.map(n => (n.name, n)).toMap
    val domainFunctionTable = choppee.domains.flatMap(d => d.functions.map(f => (f.name, (f, d))))

    val edges = choppee.members.flatMap(Edges.dependencies)

    val (components, componentMap, componentDAG) = SCC.compute(edges)



    Vector(choppee)
  }

  object SCC {

    case class Component[T](nodes: Seq[T])

    def compute[T](graph: Seq[Edge[T]]): (Vector[Component[T]], Map[T, Component[T]], Seq[Edge[Component[T]]]) = ???

  }


  sealed trait Vertex
  object Vertex {
    case class Method(methodName: String) extends Vertex
    case class MethodSpec(methodName: String) extends Vertex
    case class Function(functionName: String) extends Vertex
    case class Predicate(predicateName: String) extends Vertex
    case class Field(fieldName: String) extends Vertex
    case class DomainFunction(funcName: String) extends Vertex
    case class DomainAxiom(v: vpr.DomainAxiom, d: vpr.Domain) extends Vertex
    case class DomainType(v: vpr.DomainType) extends Vertex
    case object Always extends Vertex // if something always has to be included
  }

  type Edge[T] = (T, T)
  object Edges {

    def dependencies(member: vpr.Member): Seq[Edge[Vertex]] = {
      member match {
        case m: vpr.Method =>
          {
            val from = Vertex.Method(m.name)
            usages(m).map((from, _))
          } ++ {
            val from = Vertex.MethodSpec(m.name)
            (m.pres ++ m.posts).flatMap(exp => usages(exp).map((from, _)))
          }

        case f: vpr.Function =>
          val from = Vertex.Function(f.name)
          usages(f).map((from, _))

        case p: vpr.Predicate =>
          val from = Vertex.Predicate(p.name)
          usages(p).map((from, _))

        case _: vpr.Field =>
          Vector.empty

        case d: vpr.Domain =>
          d.axioms.flatMap{ ax =>
            val mid = Vertex.DomainAxiom(ax, d)
            val tos = usages(ax.exp)
            val froms = tos // this is just an approximation for now. This could be improved.
            val finalFroms = if (froms.nonEmpty) froms else Vector(Vertex.Always)
            finalFroms.map((_, mid)) ++ tos.map((mid, _))
          }

        case _ => Vector.empty
      }
    }

    def usages(n: vpr.Node): Seq[Vertex] = {

      def unit(n: vpr.Node): Seq[Vertex] = {
        n match {
          case n: vpr.Exp if n.typ.isInstanceOf[vpr.DomainType] =>
            Vector(Vertex.DomainType(n.typ.asInstanceOf[vpr.DomainType]))

          case _ => Vector.empty
        }
      }

      n.deepCollect{
        case n: vpr.MethodCall => Vertex.MethodSpec(n.methodName) +: unit(n)
        case n: vpr.FuncApp => Vertex.Function(n.funcname) +: unit(n)
        case n: vpr.DomainFuncApp => Vertex.DomainFunction(n.funcname) +: unit(n)
        case n: vpr.PredicateAccess => Vertex.Predicate(n.predicateName) +: unit(n)
        case n: vpr.FieldAccess => Vertex.Field(n.field.name) +: unit(n)
        case n: vpr.Exp => unit(n)
      }.flatten
    }
  }



}
