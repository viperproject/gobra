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
					case LitAdressedEntry(value, address) => prettyPrint(LitDeclaredEntry(name,value),level) ++ s"@$address"
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
			case LitPointerEntry(_,v,a) => s"&$a* -> " ++ prettyPrint(v,level)
			case LitAdressedEntry(value, address) => "(" ++prettyPrint(value,level) ++ s") @$address"
			case x => x.toString()
		}
	}
	def removeWhitespace(in: String): String ={
		in.replace(" ","").replace("\t","").replace("\n","")
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
	override def toString(): String = Util.prettyPrint(this,0) //s"struct{${values.map(x=>s"${x._1} = ${x._2.toString}").mkString("; ")}}"

}
case class UnresolvedInterface(typ: InterfaceT, possibleVals: Seq[GobraModelEntry]) extends LitEntry {
	override def toString(): String = "unable to uniquely determine interface. (possibilities include but might not limited to):\n" ++
									((possibleVals take 3).map(x=>Util.removeWhitespace(x.toString()))).mkString("---\n") ++"\n"
}
case class LitStringEntry(value: String) extends LitEntry {
	override def toString(): String =s"${'"'}${value}${'"'}"
}
case class LitPointerEntry(typ: Type, value: LitEntry, address: BigInt) extends LitEntry with HeapEntry {
	override def toString(): String = s"(&$address* -> $value)" 
	//TODO: put permission up
	val perm: Option[LitPermEntry] = None
}
//entries that are not designated (typed) as a pointer but are represented by a ref (adressable)
case class LitAdressedEntry(value: LitEntry, address: BigInt) extends LitEntry with HeapEntry {
	override def toString(): String = s"$value @$address"
	//TODO: put permission up
	val perm: Option[LitPermEntry] = None
}
case class LitRecursive(address: BigInt) extends LitEntry {
	override def toString(): String = s"&$address (recursive)"
}
case class LitDeclaredEntry(name: String,value: LitEntry) extends LitEntry {
	override def toString(): String = { Util.prettyPrint(this,0)
		/* value match {
			case LitStructEntry(_,_) => value.toString.replaceFirst("struct",name)
			case _ => value.toString()
		} */
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
	override def toString(): String = s"$original${functions.map(f => s"\t${f.fname} = ${f.options.getOrElse(Seq(original),f.default)}").mkString("{\n",",\n","\n}")}"
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

