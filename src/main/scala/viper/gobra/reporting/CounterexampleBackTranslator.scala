package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.gobra.ast.frontend._
import viper.gobra.reporting._
import viper.gobra.frontend.{Config,Parser}
import viper.silicon.interfaces.SiliconMappedCounterexample
import viper.silver
import viper.silver.{ast=> vpr}
import viper.silver.ast.{LineColumnPosition, SourcePosition}
import viper.silicon.{reporting=> sil}
import viper.gobra.frontend.info.base.{SymbolTable}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.TypeInfo
//import viper.gobra.internal.utility.Nodes
import viper.silver.verifier.{Counterexample,Model}




trait CounterexampleConfig
/**
  * simple counterexample distinction this is obsolete but kept to run the counterexamples on the config
  */
object CounterexampleConfigs{
	object MappedCounterexamples extends CounterexampleConfig
	object NativeCounterexamples extends CounterexampleConfig
	object ReducedCounterexamples extends CounterexampleConfig
	object ExtendedCounterexamples extends CounterexampleConfig
}

case class CounterexampleBackTranslator(backtrack: BackTranslator.BackTrackInfo){
	def translate(counterexample: silver.verifier.Counterexample): Option[GobraCounterexample] = {
		val typeinfo = backtrack.typeInfo
		InterpreterCache.setTypeInfo(typeinfo)
		val converter: sil.Converter = counterexample match {
			case c: SiliconMappedCounterexample => c.converter
			case _ => return None
		}
		val viperModel : Map[String,sil.ExtractedModel] = converter.modelAtLabel ++ Seq(("return",converter.extractedModel))
		val fi = viperModel.keys.head
		val viperVars : Seq[String] = viperModel.apply(fi).entries.keys.toSeq 
		val viperProgram = backtrack.viperprogram
		//all variable declarations of the viper program, issue: if two vipervariables have the same name we cannot distinguish (we have to assume they are unique)
		val varDeclNodes = viperProgram.collect(x => if(x.isInstanceOf[vpr.LocalVarDecl] && viperVars.contains(x.asInstanceOf[vpr.LocalVarDecl].name)) Some(x.asInstanceOf[vpr.LocalVarDecl]) else None).collect({case Some(x)=> x})
		//val labels = viperProgram.collect({case x:vpr.Label => x})
		//map from viper declarations to entries
		val declarationMap : Map[String,Map[vpr.LocalVarDecl, sil.ExtractedModelEntry]] = viperModel.map(y => (y._1, varDeclNodes.map(x => (x,y._2.entries.apply(x.name))).toMap))

		val predicates : Seq[vpr.Predicate] =viperProgram.collect(x=> x match {case p:vpr.Predicate =>p}).toSeq
		InterpreterCache.setPredicates(predicates)
		
		val gobraPredicates :Seq[PFPredicateDecl] = predicates.map(Source.unapply(_) match{case Some(t)=> Some(t.pnode); case _=> None}).collect(x=> x match{case Some(p)=> p}).collect(x=> x match{case p:PFPredicateDecl=>p})
		InterpreterCache.setGobraPredicates(gobraPredicates)

		//map from info to counterexample entry
		val declInfosMap: Map[String,Map[Source.Verifier.Info,sil.ExtractedModelEntry]] = declarationMap.map(y=>(y._1,y._2.map(x=>(Source.unapply(x._1) match {case Some(t) => (t,x._2) }))))

		val tranlator = MasterInterpreter(converter).interpret(_,_)	
		//translate the values
		//printf(s"${converter.extractedHeap}\n---${converter.store}")
		val translated = declInfosMap.map(y=>(y._1,y._2.map(x=>
							{InterpreterCache.clearCache();
								val typeval = Util.getType(x._1.pnode,typeinfo)
								//printf(s"$x;;$typeval\n")
								(x._1,tranlator(x._2,typeval))
							}
								)))												
		//the pnode does not always correspond to the same node possible (filter for which the pnode is not a substrong of the node)
		lazy val glabelModel = new GobraModelAtLabel(translated.map(y=>(y._1,new GobraModel(y._2.filterNot(x=>isUnnecessary(x._1) ).map(x=>((x._1,x._1.node.toString),x._2))))))
		//printf(s"${converter.domains}\n${converter.non_domain_functions}")
		InterpreterCache.clearPreds()
		InterpreterCache.clearGobraPreds()
		lazy val ret = Some(new GobraCounterexample(glabelModel))
		backtrack.config.counterexample match {
			case Some(CounterexampleConfigs.NativeCounterexamples) => Some(new GobraNativeCounterexample(counterexample.asInstanceOf[SiliconMappedCounterexample]))
			case _ => ret
		}
	
	}
	def isUnnecessary(info: Source.Verifier.Info): Boolean = {
		//Embeddings and such 			//usually for multi variable assgnent //function calls						// parameter and return values (ISSUE sometimes we want both TODO: on return label use result param and in old label use input params)
		info.node.toString.contains("$") || info.pnode.toString.contains("=") || info.pnode.toString.contains("(") || info.pnode.toString.contains("{")//|| info.pnode.toString.contains(" ")
	}
}
object Util{
	def getType(pnode: PNode, info: viper.gobra.frontend.info.ExternalTypeInfo): Type = {
		pnode match {
			case (x: PIdnNode) => info.typ(x)
			case (x: PParameter) => info.typ(x)
			case (x: PExpression) => info.typ(x)
			case (x: PMisc) => info.typ(x)
			case _ => UnknownType
		}
	}
	def getDefault(t: Type): GobraModelEntry = {
		t match {
			case _: IntT => LitIntEntry(0)
			case BooleanT => LitBoolEntry(false)
			case _: PointerT => LitNilEntry()
			case a: ArrayT => LitArrayEntry(a,Seq())
			case OptionT(elem) => LitOptionEntry(None)
			case s: StructT => LitStructEntry(s,s.fields.map(x=>(x._1,getDefault(x._2).asInstanceOf[LitEntry])))
			case StringT => LitStringEntry("")
			case _ => LitNilEntry()
		}
	}

	def prettyPrint(input: GobraModelEntry, level: Int): String = {
		val indent = "\t\t\t" ++ "\t".repeat(level)
		val postdent = "\t\t" ++ "\t".repeat(level)
		val predent = "\t\t"++"\t".repeat(level)
		input match {
			case LitStructEntry(_, m) => {
				if(m.isEmpty){
					"struct{}"
				}else{
				val sub = m.map(x=>(x._1,prettyPrint(x._2,level+1)))
				s"struct{\n${sub.map(x=>s"$indent${x._1}=${x._2}").mkString(";\n")}\n${postdent}}"
				}
			}
			case LitDeclaredEntry(name, value) => {
				value match {
					case LitStructEntry(_,_) => prettyPrint(value,level).replaceFirst("struct",name)
					case LitAdressedEntry(value, address,perm) => prettyPrint(LitDeclaredEntry(name,value),level) ++ s"@$address," /* (${perm.getOrElse("?")})" */
					case u: UserDomainEntry => prettyPrint(value,level).replaceFirst("domain",name)
					case e: ExtendedUserDomainEntry => prettyPrint(value,level).replaceFirst("domain",name)
					case _ => s"$name(${prettyPrint(value,level)})"  // can we assum that only structs show theit name?
				} 
				
			}
			//case LitSliceEntry(typ, begin, end, values) => {val sbegin = if(begin==0) "" else s"$begin";
			//												val send =if(end==values.length)""else s"$end";
			//												 s"[$sbegin:$send]${prettyPrint(LitSeqEntry(SequenceT(typ),values),0)}"}
			case v: WithSeq => {
				v.values.headOption match {//todo figure out why this does not work (maybe we dont even need it...)
					case Some(LitStructEntry(_,_)) =>  s"[\n$predent${v.values.map(x=>prettyPrint(x,level+1)).mkString(s",\n$predent")}\n$postdent]"
					case _ => s"[${v.values.map(x=>prettyPrint(x,level)).mkString(", ")}]"
				}
			}
			case LitPointerEntry(_,v,a, perm) => s"&$a* "/*+ s"(${perm.getOrElse("?")})" */+"-> " ++ prettyPrint(v,level)
			case LitAdressedEntry(value, address,perm) => "(" ++prettyPrint(value,level) ++ s") @$address"/* +s" (${perm.getOrElse("?")})" */
			case x => x.toString()
		}
	}
	def removeWhitespace(in: String): String ={
		in.replace(" ","").replace("\t","").replace("\n","")
	}
	def getSubnodes(s: Vector[PNode]): Seq[PNode] = {
		s.flatMap(getSubnodes(_)).toSeq
	}
	def getSubnodes(o: Option[PNode]): Seq[PNode] = {
		o.map(getSubnodes(_)).getOrElse(Seq())
	}
	/**
	 * 	extracts all subnodes (includeing itself) WARNING do not use on large pnodes (PPackages, large function etc)
	 * */
	def getSubnodes(a: PNode): Seq[PNode] ={
		val realSubs = a match {
			case PPackage(packageClause, programs, _) =>  getSubnodes(packageClause) ++ programs.flatMap(getSubnodes(_))
			case PProgram(packageClause, imports, _) =>  getSubnodes(packageClause) ++ imports.flatMap(getSubnodes(_))
			case PPackageClause(id) => getSubnodes(id)
			case PExplicitQualifiedImport(qualifier, _) => getSubnodes(qualifier)
			case PConstDecl(typ, right, left)  =>  getSubnodes(typ) ++ getSubnodes(right) ++ getSubnodes(left)
			case PVarDecl(typ, right, left, _) =>  getSubnodes(typ) ++ getSubnodes(right) ++ getSubnodes(left)
			case PFunctionDecl(id, args, result, spec, body) => getSubnodes(id) ++ getSubnodes(args) ++ getSubnodes(result) ++ getSubnodes(spec) ++getSubnodes(body.map(_._1)) ++getSubnodes(body.map(_._2))
			case PMethodDecl(id, receiver, args, result, spec, body) => getSubnodes(id) ++ getSubnodes(args) ++ getSubnodes(result) ++ getSubnodes(spec) ++getSubnodes(body.map(_._1)) ++getSubnodes(body.map(_._2)) ++getSubnodes(receiver)
			case PTypeDef(right, left) => getSubnodes(right) ++ getSubnodes(left)
			case PTypeAlias(right, left) => getSubnodes(right) ++ getSubnodes(left)
			case PLabeledStmt(label, stmt) => getSubnodes(label) ++ getSubnodes(stmt)
			case PExpressionStmt(exp) => getSubnodes(exp)
			case PGoStmt(exp) => getSubnodes(exp)
			case PDeferStmt(exp) => getSubnodes(exp)
			case PExpCompositeVal(exp) => getSubnodes(exp)
			case PLength(exp) => getSubnodes(exp)
			case PCapacity(exp) => getSubnodes(exp)
			case PRange(exp) => getSubnodes(exp)
			case PAssert(exp) => getSubnodes(exp)
			case PAssume(exp) => getSubnodes(exp)
			case PExhale(exp) => getSubnodes(exp)
			case PInhale(exp) => getSubnodes(exp)
			case PFold(exp) => getSubnodes(exp)
			case PUnfold(exp) => getSubnodes(exp)
			case PSendStmt(channel, msg)  => getSubnodes(channel) ++ getSubnodes(msg)
			case PAssignment(right, left) => getSubnodes(right) ++ getSubnodes(left)
			case PAssignmentWithOp(right, op, left) => getSubnodes(right) ++ getSubnodes(op) ++ getSubnodes(left)

			case PShortVarDecl(right, left, addressable) => getSubnodes(right)++ getSubnodes(left)
			case PIfStmt(ifs, els) => getSubnodes(ifs) ++ getSubnodes(els)
			case PIfClause(pre, condition, body) => getSubnodes(pre) ++ getSubnodes(condition) ++ getSubnodes(body)
			case PExprSwitchStmt(pre, exp, cases, dflt) => getSubnodes(pre) ++ getSubnodes(exp) ++getSubnodes(cases) ++getSubnodes(dflt)
			case PExprSwitchDflt(body) => getSubnodes(body)
			case PTypeSwitchDflt(body) => getSubnodes(body)
			case PExprSwitchCase(left, body) => getSubnodes(left) ++getSubnodes(body)
			case PTypeSwitchStmt(pre, exp, binder, cases, dflt) => getSubnodes(pre) ++ getSubnodes(exp) ++ getSubnodes(binder) ++ getSubnodes(cases) ++getSubnodes(dflt)
			
			case PTypeSwitchCase(left, body) => getSubnodes(left) ++ getSubnodes(body)
			case PForStmt(pre, cond, post, spec, body) => getSubnodes(pre)++getSubnodes(cond)++ getSubnodes(post)++ getSubnodes(spec)++ getSubnodes(body)
			case PAssForRange(range, ass, body) => getSubnodes(range) ++ getSubnodes(ass)++ getSubnodes(body)
			case PShortForRange(range, shorts, body) => getSubnodes(range) ++getSubnodes(shorts) ++ getSubnodes(body)
			
			case PSelectStmt(send, rec, aRec, sRec, dflt) => getSubnodes(send)++ getSubnodes(rec)++getSubnodes(aRec)++ getSubnodes(sRec)++ getSubnodes(dflt)
			case PSelectDflt(body) => getSubnodes(body)
			case PSelectSend(send, body) => getSubnodes(send)++getSubnodes(body)
			case PSelectRecv(recv, body) => getSubnodes(recv) ++getSubnodes(body)
			case PSelectAssRecv(recv, ass, body) => getSubnodes(recv) ++getSubnodes(ass) ++ getSubnodes(body)
			case PSelectShortRecv(recv, shorts, body) => getSubnodes(recv) ++getSubnodes(shorts)++getSubnodes(body)
			case PReturn(exps) => getSubnodes(exps)
			case PBreak(label) => getSubnodes(label)
			case PContinue(label) => getSubnodes(label)
			case PGoto(label) => getSubnodes(label)
			
			case PBlock(stmts) => getSubnodes(stmts)
			case PSeq(stmts) => getSubnodes(stmts)
			case PNamedOperand(id) => getSubnodes(id)
			case PCompositeLit(typ, lit) => getSubnodes(typ) ++ getSubnodes(lit)
			case PLiteralValue(elems) => getSubnodes(elems)
			case PKeyedElement(key, exp) => getSubnodes(key)++getSubnodes(exp)
			case PIdentifierKey(id) => getSubnodes(id)
			
			case PLitCompositeVal(lit) =>getSubnodes(lit)
			case PFunctionLit(args, result, body) => getSubnodes(args) ++ getSubnodes(result) ++ getSubnodes(body)
			case PInvoke(base, args) => getSubnodes(base) ++ getSubnodes(args)
			case PDot(base, id) => getSubnodes(base) ++getSubnodes(id)
			case PIndexedExp(base, index) => getSubnodes(base) ++getSubnodes(index)
			
			case PSliceExp(base, low, high, cap) => getSubnodes(base) ++getSubnodes(low) ++getSubnodes(high) ++getSubnodes(cap)
			case PUnpackSlice(elem) => getSubnodes(elem)
			case PTypeAssertion(base, typ) => getSubnodes(base) ++getSubnodes(typ)
			case PDeref(base) => getSubnodes(base)
			case u: PUnaryExp => getSubnodes(u.operand)
			case b: PBinaryExp[PNode,PNode] => getSubnodes(b.left) ++ getSubnodes(b.right)
			case  PUnfolding(pred, op) => getSubnodes(pred) ++ getSubnodes(op)
			case PMake(typ, args) => getSubnodes(typ) ++ getSubnodes(args)
			case PNew(typ) => getSubnodes(typ)
			case PFPredBase(id) => getSubnodes(id)
			case PDottedBase(recvWithId) => getSubnodes(recvWithId)
			case PPredConstructor(id, args) =>getSubnodes(id) ++ args.flatMap(getSubnodes(_))
			case PArrayType(len, elem) => getSubnodes(len) ++ getSubnodes(elem)
			case PImplicitSizeArrayType(elem) => getSubnodes(elem)
			case PSliceType(elem) => getSubnodes(elem)
			case PBiChannelType(elem) => getSubnodes(elem)
			case PSendChannelType(elem) => getSubnodes(elem)
			case PRecvChannelType(elem)=> getSubnodes(elem)
			case PVariadicType(elem) => getSubnodes(elem)
			case PMapType(key, elem) =>getSubnodes(key)++ getSubnodes(elem)
			case PStructType(clauses) => getSubnodes(clauses)
			case PFieldDecls(fields) =>getSubnodes(fields)
			case PFieldDecl(id, typ) => getSubnodes(id) ++ getSubnodes(typ)
			case PEmbeddedDecl(typ, id) => getSubnodes(id) ++ getSubnodes(typ)
			case PMethodReceiveName(typ) => getSubnodes(typ)
			case PMethodReceivePointer(typ) =>getSubnodes(typ)
			case PFunctionType(args, result) => getSubnodes(args) ++ getSubnodes(result)
			case PPredType(args) => getSubnodes(args)
			case PInterfaceType(embedded, methSpecs, predSpec) => getSubnodes(embedded) ++ getSubnodes(methSpecs) ++ getSubnodes(predSpec)
			case PInterfaceName(typ) => getSubnodes(typ)
			case PMethodSig(id, args, result, spec, isGhost) =>		getSubnodes(id) ++getSubnodes(args) ++ getSubnodes(result) ++ getSubnodes(spec)
			case PNamedParameter(id, typ) => getSubnodes(id) ++ getSubnodes(typ)
			case PUnnamedParameter(typ) => getSubnodes(typ)
			case PNamedReceiver(id, typ, addressable) => getSubnodes(typ) ++ getSubnodes(id)
			case PUnnamedReceiver(typ) => getSubnodes(typ)
			case PResult(outs) =>getSubnodes(outs)
			case PEmbeddedName(typ) =>getSubnodes(typ)
			case PEmbeddedPointer(typ) =>getSubnodes(typ)
			case PFunctionSpec(pres, posts, isPure) => getSubnodes(pres) ++getSubnodes(posts)
			case PBodyParameterInfo(shareableParameters) => getSubnodes(shareableParameters)
			case PLoopSpec(invariants) => getSubnodes(invariants)
			case PExplicitGhostMember(actual) => getSubnodes(actual)
			case PFPredicateDecl(id, args, body) => getSubnodes(id) ++ getSubnodes(args) ++ getSubnodes(body)
			case PMPredicateDecl(id, receiver, args, body) => getSubnodes(id) ++ getSubnodes(args) ++ getSubnodes(body) ++ getSubnodes(receiver)
			case PMPredicateSig(id, args) => getSubnodes(id) ++ getSubnodes(args)
			case PImplementationProof(subT, superT, alias, memberProofs) => getSubnodes(subT) ++ getSubnodes(superT) ++ getSubnodes(alias) ++ getSubnodes(memberProofs)
			case PMethodImplementationProof(id, receiver, args, result, isPure, body) => getSubnodes(id) ++ getSubnodes(receiver) ++ getSubnodes(args) ++ getSubnodes(result) ++ getSubnodes(body.map(_._1)) ++ getSubnodes(body.map(_._2))
			case PImplementationProofPredicateAlias(left, right) => getSubnodes(left) ++ getSubnodes(right)
			case PExplicitGhostStatement(actual) => getSubnodes(actual)

			case POld(operand) => getSubnodes(operand)
			case PLabeledOld(label, operand) => getSubnodes(label) ++ getSubnodes(operand)
			case PConditional(cond, thn, els) => getSubnodes(cond) ++ getSubnodes(thn) ++ getSubnodes(els)
			case PImplication(left, right) => getSubnodes(left) ++ getSubnodes(right)
			case PAccess(exp, perm) => getSubnodes(exp) ++ getSubnodes(perm)
			case PPredicateAccess(pred, perm) => getSubnodes(pred) ++ getSubnodes(perm)
			case PForall(vars, triggers, body) => getSubnodes(vars) ++ getSubnodes(triggers) ++ getSubnodes(body)
			case PExists(vars, triggers, body) => getSubnodes(vars) ++ getSubnodes(triggers) ++ getSubnodes(body)
			case PTypeOf(exp) => getSubnodes(exp)
			case PIsComparable(exp) => getSubnodes(exp)
			case POptionNone(t) => getSubnodes(t)
			case POptionSome(exp) => getSubnodes(exp)
			case POptionGet(exp) => getSubnodes(exp)
			case b: PBinaryGhostExp => getSubnodes(b.left)++ getSubnodes(b.right)
			case PSequenceConversion(exp) => getSubnodes(exp)
			case PGhostCollectionUpdate(col, clauses) => getSubnodes(col) ++ getSubnodes(clauses)
			case PGhostCollectionUpdateClause(left, right) =>  getSubnodes(left) ++ getSubnodes(right)
			case PRangeSequence(low, high) => getSubnodes(low) ++ getSubnodes(high)
			case PSetConversion(exp) =>getSubnodes(exp)
			case PMultisetConversion(exp) =>getSubnodes(exp)
			case PMapKeys(exp) =>getSubnodes(exp)
			case PMapValues(exp) =>getSubnodes(exp)
			case PSequenceType(elem) =>getSubnodes(elem)
			case PSetType(elem) =>getSubnodes(elem)
			case PMultisetType(elem) =>getSubnodes(elem)
			case PMathematicalMapType(keys, values) => getSubnodes(keys) ++ getSubnodes(values)
			case POptionType(elem) =>getSubnodes(elem)
			case PGhostSliceType(elem) =>getSubnodes(elem)
			case PDomainType(funcs, axioms) => getSubnodes(funcs) ++ getSubnodes(axioms)
			case PDomainFunction(id, args, result) => getSubnodes(id) ++ getSubnodes(args) ++ getSubnodes(result)
			case PDomainAxiom(exp) =>getSubnodes(exp)
			case PBoundVariable(id, typ) => getSubnodes(id) ++ getSubnodes(typ)
			case PTrigger(exps) => getSubnodes(exps)
			case PExplicitGhostParameter(actual) => getSubnodes(actual)
			case PExplicitGhostStructClause(actual) => getSubnodes(actual)
			case t => Seq()
		}
		Seq(a) ++ realSubs
	}

}
/*
object GobraCounterexample { 
	def apply(gModel: GobraModelAtLabel): GobraCounterexample = GobraCounterexample(gModel)
	def unapply(c: GobraCounterexample): GobraModelAtLabel = c.gModel
}
*/
class GobraCounterexample(gModel: GobraModelAtLabel) extends Counterexample {
	val reducedCounterexample: GobraModel =GobraModel(gModel.labeledEntries.last._2.entries.filterNot(_._2.isInstanceOf[FaultEntry]))
	def getRelevantEntries(node: PNode): Seq[((viper.gobra.reporting.Source.Verifier.Info, String), viper.gobra.reporting.GobraModelEntry)] = {
		val subnodes= Util.getSubnodes(node)
		reducedCounterexample.entries.toSeq.filter(x => subnodes.find(y => x._1._1.pnode.toString.split(" ").head==y.toString).isDefined)
	}
	override def toString: String = gModel.toString
	//TODO ensure the counterexamples are sorted alphabetically
	def testString: String =  gModel.labeledEntries.head._2.entries.toSeq.sortBy(_._1._1.toString).map(x=>Util.removeWhitespace(s"${x._1._1.toString}\t<- ${Util.prettyPrint(x._2,0).replaceAll("&-?\\d*","&").replaceAll("@-?\\d*","@")}")).mkString(";")
	val model = null
}
//for debugging purposes 
class GobraNativeCounterexample(s: SiliconMappedCounterexample) extends GobraCounterexample(null) {
	//override val gModel: GobraModelAtLabel = null
	//val model: Null = null
	override def toString(): String = s.toString
}

case class GobraModelAtLabel(labeledEntries: Map[String, GobraModel]) {
	override def toString: String = {
		labeledEntries.map(x=>s"model at label ${x._1}:\n${x._2}").mkString("\n")
	}
}
case class GobraModel(entries: Map[(Source.Verifier.Info,String),GobraModelEntry]) {
	override def toString: String = {
		val params = entries.filter(x=>x._1.isInstanceOf[PParameter]) //first we separate the different types of values
		val rest = entries.filterNot(x=>x._1.isInstanceOf[PParameter])
		params.map(x=>s"${x._1._1.pnode.toString}\t<- ${Util.prettyPrint(x._2,0)}").mkString("\n") ++ "\n" ++
		rest.map(x=>s"${x._1._1.pnode.toString}\t<- ${Util.prettyPrint(x._2,0)}").mkString("\n")

	}
	
}

/** this is how we model the  values returned by the counterexamples...
  * 
  */

sealed trait GobraModelEntry{
	override def toString: String = "not implemented"
}


sealed trait LitEntry extends GobraModelEntry
sealed trait WithSeq {
	val values:Seq[LitEntry]
}


case class DummyEntry() extends LitEntry
case class FaultEntry(message: String)extends LitEntry {override def toString = message}
case class LitNilEntry() extends LitEntry {//TODO: Think of a way to implement pretty printing
	override def toString(): String = "nil"	
}
case class LitIntEntry(value: BigInt) extends LitEntry {
	override def toString(): String = s"$value"
}
case class LitBoolEntry(value: Boolean) extends LitEntry {
	override def toString(): String = s"$value"
}
case class LitPermEntry(value: viper.silicon.state.terms.Rational) extends LitEntry {//should this be a double? does this not cause trouble?
	override def toString(): String = s"$value"
}
case class LitOptionEntry(value: Option[LitEntry]) extends LitEntry {
	override def toString(): String = s"$value"
}
case class LitSetEntry(values: Set[LitEntry]) extends LitEntry {
	override def toString(): String = values.map(_.toString).mkString("{",", ","}")
}
case class LitMSetEntry(values: Map[LitEntry,Int]) extends LitEntry {
	override def toString(): String = values.map(x=>s"(${x._1}:${x._2})").mkString("{",", ","}")
}
case class LitArrayEntry(typ: ArrayT, values: Seq[LitEntry]) extends LitEntry with WithSeq {
	override def toString(): String = Util.prettyPrint(this, 0)//s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSliceEntry(typ: SliceT, begin: BigInt, end: BigInt, values: Seq[LitEntry]) extends LitEntry with WithSeq {
	override def toString(): String =  Util.prettyPrint(this, 0)//s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSeqEntry(typ: Type, values: Seq[LitEntry]) extends LitEntry with WithSeq {
	override def toString(): String = Util.prettyPrint(this, 0)//s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSparseEntry(full: WithSeq, relevant: Map[(Int, Int), LitEntry]) extends LitEntry {
	override def toString():String = relevant.toSeq.sortBy(_._1._1).map(x=>s"${if(x._1._2-x._1._1 <=1)s"${x._1._1}" else s"${x._1._1}-${x._1._2}"}:${x._2}").mkString("[",",","]")
}
case class LitStructEntry(typ:StructT, values: Map[String, LitEntry]) extends LitEntry { //TODO: Pretty printing
	override def toString(): String = /* Util.prettyPrint(this,0) // */s"struct{${values.map(x=>s"${x._1} = ${x._2.toString}").mkString(if(values.size < 5)("; ")else(";\n\t"))}}"

}
case class UnresolvedInterface(typ: InterfaceT, possibleVals: Seq[GobraModelEntry]) extends LitEntry {
	override def toString(): String = "unable to uniquely determine interface. (possibilities include but might not limited to):\n" ++
									((possibleVals take 3).map(x=>Util.removeWhitespace(x.toString()))).mkString("---\n") ++"\n"
}
case class LitStringEntry(value: String) extends LitEntry {
	override def toString(): String = s"${'"'}${value}${'"'}"
}
case class ConcatString(v1: LitEntry, v2: LitEntry) extends LitEntry {
	override def toString(): String = s"$v1 + $v2"
}
case class LitPointerEntry(typ: Type, value: LitEntry, address: BigInt,perm: Option[LitPermEntry] = None) extends LitEntry with HeapEntry {
	override def toString(): String = s"(&$address*"+ s"(${perm.getOrElse("?")})" + s"-> $value)" 
}
//entries that are not designated (typed) as a pointer but are represented by a ref (adressable)
case class LitAdressedEntry(value: LitEntry, address: BigInt, perm: Option[LitPermEntry] = None) extends LitEntry with HeapEntry {
	override def toString(): String = s"$value @$address" +s"(${perm.getOrElse("?")})"// not included for easier readability
}
case class LitRecursive(address: BigInt) extends LitEntry {
	override def toString(): String = s"&$address (recursive)"
}
case class LitDeclaredEntry(name: String,value: LitEntry) extends LitEntry {
	override def toString(): String = { //Util.prettyPrint(this,0)
		value match {
					case LitStructEntry(_,_) => value.toString().replaceFirst("struct",name)
					case LitAdressedEntry(value, address,perm) => LitDeclaredEntry(name,value).toString() ++ s"@$address,(${perm.getOrElse("?")})"
					case _: UserDomainEntry | _: ExtendedUserDomainEntry => value.toString.replaceFirst("domain",name)
					case _ => s"$name(${value})" 
				} 
	}
}

case class ChannelEntry(typ: ChannelT, buffSize: BigInt, isOpen: Option[Int], isSend: Option[Boolean], isRecieve: Option[Boolean]) extends LitEntry {
	override def toString():String = s"(${typ}) [$buffSize] (state: $state" +
  		s"${if(typ.mod!=ChannelModus.Recv)s", can send: $sending" else ""}" +
  		s"${if(typ.mod!=ChannelModus.Send)s", can receive: $rec" else ""}" +
		  ")"
	private val state = isOpen match {
		case Some(2) => "initialized"
		case Some(1) => "created"
		case Some(0) => "closed"
		case _ => "?"
	}
	private val sending = isSend match {
		case Some(true) => "yes"
		case Some(false) => "no"
		case _ => "?"
	}
	private val rec = isRecieve match {
		case Some(true) =>  "yes"
		case Some(false) => "no"
		case None => "?"
	}


}
case class FunctionEntry(fname: String, argTypes: Seq[Type], resType: Type, options: Map[Seq[GobraModelEntry], GobraModelEntry], default: GobraModelEntry) extends GobraModelEntry { //maybe extend gobraentry
	override def toString: String = {
    if (options.nonEmpty)
      s"$fname${argTypes.mkString("(",",",")")}:$resType{\n" + options.map(o => "    " + o._1.mkString(" ") + " -> " + o._2).mkString("\n") + "\n    else -> " + default +"\n}"
    else
      s"$fname{\n    " + default +"\n}"
  }
}

case class UserDomain(name: String, typeArgs: Seq[Type], functions: Seq[FunctionEntry]) extends GobraModelEntry {
	//TODO: tostring
}
case class UserDomainEntry(name: String, id: String) extends LitEntry {
	override def toString(): String = name + "_" + id
}

case class ExtendedUserDomainEntry(original: UserDomainEntry, functions: Seq[FunctionEntry]) extends LitEntry {
	override def toString(): String = s"$original${functions.map(f => s"${f.fname} = ${f.options.getOrElse(Seq(original),f.default)}").mkString("{\n\t",if(functions.size > 3)(",\n\t")else(","),"\n}")}"
}

trait HeapEntry extends GobraModelEntry {
	val perm:Option[LitPermEntry]
}

//later
case class LitMapEntry(typ: MapT, value: Map[LitEntry,LitEntry]) extends LitEntry {
	override def toString(): String = value.toString()
}

case class LitPredicateEntry(name: String, args: Seq[LitEntry], perm:Option[LitPermEntry]) extends LitEntry with HeapEntry {
	override def toString(): String = s"$name(${args.mkString(",")}) ${perm.getOrElse("?")}"
}
case class UnknownValueButKnownType(info: String, typ: Type) extends LitEntry {
	override def toString(): String = s"$info:$typ"
}

case class FCPredicate(name: String, argsApplied: Option[Seq[LitEntry]], argsUnapplied: Seq[Type]) extends LitEntry {
	override def toString(): String = s"$name(${argsApplied.getOrElse(List("?")).mkString("[",",","]")},"++
	s" ${argsUnapplied.mkString((if(argsUnapplied.size==0)"[(fully applied)" else "[_:"),", _:", "]")})"
}

