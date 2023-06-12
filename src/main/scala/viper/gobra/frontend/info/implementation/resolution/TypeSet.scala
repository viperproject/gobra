package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.frontend.info.base.Type._
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.ExternalTypeInfo
sealed trait TypeSet

object TypeSet {
  case object UnboundedTypeSet extends TypeSet
  case class BoundedTypeSet(ts: Set[Type]) extends TypeSet

  def from(constraint: PInterfaceType, ctx: ExternalTypeInfo): TypeSet = typeSetFromInterfaceType(constraint, ctx)

  private def typeSetFromInterfaceType(inter: PInterfaceType, ctx: ExternalTypeInfo): TypeSet = inter match {
    case PInterfaceType(embedded, _, _) => if (embedded.isEmpty) UnboundedTypeSet else intersect(embedded.map(el => typeSetFromElement(el, ctx)))
  }

  private def typeSetFromElement(element: PTypeElement, ctx: ExternalTypeInfo): TypeSet =
    if (element.terms.isEmpty) UnboundedTypeSet else union(element.terms.map(term => typeSetFromTerm(term, ctx)))

  private def typeSetFromTerm(term: PTypeTerm, ctx: ExternalTypeInfo): TypeSet = term match {
    case PComparable() => UnboundedTypeSet
    case i: PInterfaceType => typeSetFromInterfaceType(i, ctx)
    case t: PType => BoundedTypeSet(Set(ctx.symbType(t)))
  }

  def empty(): TypeSet = {
    BoundedTypeSet(Set.empty[Type])
  }

  def union(tss: Vector[TypeSet]): TypeSet = tss.size match {
    case 0 => empty()
    case 1 => tss.head
    case _ => tss.fold(empty()) {
      case (UnboundedTypeSet, _) => UnboundedTypeSet
      case (_, UnboundedTypeSet) => UnboundedTypeSet
      case (BoundedTypeSet(x), BoundedTypeSet(y)) => BoundedTypeSet(x union y)
    }
  }

  def intersect(tss: Vector[TypeSet]): TypeSet = tss.size match {
    case 0 => empty()
    case 1 => tss.head
    case _ => tss.fold(UnboundedTypeSet) {
      case (UnboundedTypeSet, ts) => ts
      case (ts, UnboundedTypeSet) => ts
      case (BoundedTypeSet(x), BoundedTypeSet(y)) => BoundedTypeSet(x intersect y)
    }
  }
}
