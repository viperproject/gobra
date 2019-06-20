package viper.gobra.translator.implementations

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer}
import viper.gobra.translator.interfaces.Collector
import viper.silver.{ast => vpr}

class CollectorImpl extends Collector {

  protected var _domains:    List[vpr.Domain]    = List.empty
  protected var _fields:     List[vpr.Field]     = List.empty
  protected var _predicates: List[vpr.Predicate] = List.empty
  protected var _functions:  List[vpr.Function]  = List.empty
  protected var _methods:    List[vpr.Method]    = List.empty
  protected var _errorT:  List[ErrorTransformer] = List.empty
  protected var _reasonT: List[ReasonTransformer] = List.empty


  override def addMember(m: vpr.Member): Unit = m match {
    case d: vpr.Domain    => _domains ::= d
    case f: vpr.Field     => _fields ::= f
    case p: vpr.Predicate => _predicates ::= p
    case f: vpr.Function  => _functions ::= f
    case m: vpr.Method    => _methods ::= m
  }

  override def addErrorT(errT: ErrorTransformer): Unit = _errorT ::= errT
  override def addReasonT(resT: ReasonTransformer): Unit = _reasonT ::= resT


  override def domains: Seq[vpr.Domain] = _domains
  override def fields: Seq[vpr.Field] = _fields
  override def predicate: Seq[vpr.Predicate] = _predicates
  override def functions: Seq[vpr.Function] = _functions
  override def methods: Seq[vpr.Method] = _methods

  override def errorT: Seq[ErrorTransformer] = _errorT
  override def reasonT: Seq[ReasonTransformer] = _reasonT
}
