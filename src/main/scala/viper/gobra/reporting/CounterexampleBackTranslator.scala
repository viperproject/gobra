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
	def translate(counterexample: silver.verifier.Counterexample): Option[GobraCounterexample] ={

		val typeinfo = backtrack.typeInfo
		val converter :sil.Converter = counterexample match {
			case c:SiliconMappedCounterexample => c.converter
			case _ => return None
		}
		val viperModel :Map[String,sil.ExtractedModel] = converter.modelAtLabel
		val fi = viperModel.keys.head
		val viperVars : Seq[String] = viperModel.apply(fi).entries.keys.toSeq 
		val viperProgram = backtrack.viperprogram
		//all variable declarations of the viper program, issue: if two vipervariables have the same name we cannot distinguish (we have to assume they are unique)
		val varDeclNodes = viperProgram.collect(x=>if(x.isInstanceOf[vpr.LocalVarDecl]&& viperVars.contains(x.asInstanceOf[vpr.LocalVarDecl].name)) Some (x.asInstanceOf[vpr.LocalVarDecl]) else None).collect({case Some(x)=> x})
		
		//map from viper declarations to entries
		val declarationMap : Map[String,Map[vpr.LocalVarDecl,sil.ExtractedModelEntry]]=viperModel.map(y=>(y._1,varDeclNodes.map(x=>(x,y._2.entries.apply(x.name))).toMap))

		//map from info to counterexample entry
		val declInfosMap: Map[String,Map[Source.Verifier.Info,sil.ExtractedModelEntry]] = declarationMap.map(y=>(y._1,y._2.map(x=>(Source.unapply(x._1) match {case Some(t) => (t,x._2) }))))

		val tranlator = MasterInterpreter(converter).interpret(_,_)	
		//translate the values

		val translated = declInfosMap.map(y=>(y._1,y._2.map(x=>(x._1,tranlator(x._2,Util.getType(x._1.pnode,typeinfo))))))													
		//the pnode does not always correspond to the same node possible (filter for which the pnode is not a substrong of the node)
		lazy val glabelModel = new GobraModelAtLabel(translated.map(y=>(y._1,new GobraModel(y._2.filterNot(_._1.node.toString.contains("$")).map(x=>((x._1.pnode,x._1.node.toString),x._2))))))
		//printf(s"${converter.domains}\n${converter.non_domain_functions}")
		lazy val ret = Some(new GobraCounterexample(glabelModel))
		backtrack.config.counterexample match {
			case Some(CounterexampleConfigs.NativeCounterexamples) => Some(new GobraNativeCounterexample(counterexample.asInstanceOf[SiliconMappedCounterexample]))
			case _ => ret
		}
	
	}
}
object Util{
	def getType(pnode:PNode,info:viper.gobra.frontend.info.ExternalTypeInfo):Type={
		pnode match {
			case (x:PIdnNode) => info.typ(x)
			case (x:PParameter) => info.typ(x)
			case (x:PExpression) => info.typ(x)
			case (x:PMisc) => info.typ(x)
			case _ => UnknownType
		}
	}

	def prettyPrint(input:GobraModelEntry,level:Int):String = {
		val indent = "\t\t\t" ++ "\t".repeat(level)
		val postdent = "\t\t" ++ "\t".repeat(level)
		val predent = "\t\t"++"\t".repeat(level)
		input match {
			case LitStructEntry(_,m) => {
				val sub = m.map(x=>(x._1,prettyPrint(x._2,level+1)))
				s"struct{\n${sub.map(x=>s"$indent${x._1}=${x._2}").mkString(";\n")}\n${postdent}}"
			}
			case LitDeclaredEntry(name,value) => {
				value match {
					case LitStructEntry(_,_) => prettyPrint(value,level).replaceFirst("struct",name)
					case _ => s"$name(${value.toString()})"  // can we assum that only structs show theit name?
				} 
				
			}
			//case LitSliceEntry(typ, begin, end, values) => {val sbegin = if(begin==0) "" else s"$begin";
			//												val send =if(end==values.length)""else s"$end";
			//												 s"[$sbegin:$send]${prettyPrint(LitSeqEntry(SequenceT(typ),values),0)}"}
			case v:WithSeq => {
				v.values.headOption match {//todo figure out why this does not work (maybe we dont even need it...)
					case Some(LitStructEntry(_,_)) =>  s"[\n$predent${v.values.map(x=>prettyPrint(x,level+1)).mkString(s",\n$predent")}\n$postdent]"
					case _ => s"[${v.values.map(x=>prettyPrint(x,level)).mkString(", ")}]"
				}
			}
			case x => x.toString()
		}
	}
}


class GobraCounterexample(gModel:GobraModelAtLabel) extends Counterexample{
	override def toString:String = gModel.toString
	val model =null
}
//for debugging purposes 
class GobraNativeCounterexample(s:SiliconMappedCounterexample) extends GobraCounterexample(null){
	//override val gModel: GobraModelAtLabel = null
	//val model: Null = null
	override def toString(): String = s.toString
}

case class GobraModelAtLabel(labeledEntries:Map[String,GobraModel]){
	override def toString :String ={
		labeledEntries.map(x=>s"model at label ${x._1}:\n${x._2}").mkString("\n")
	}
}
case class GobraModel(entries:Map[(PNode,String),GobraModelEntry]){
	override def toString :String = {
		val params = entries.filter(x=>x._1.isInstanceOf[PParameter]) //first we separate the different types of values
		val rest = entries.filterNot(x=>x._1.isInstanceOf[PParameter])
		params.map(x=>s"${x._1._1.toString}\t<- ${Util.prettyPrint(x._2,0)}").mkString("\n") ++ "\n" ++
		rest.map(x=>s"${x._1._1.toString}\t<- ${Util.prettyPrint(x._2,0)}").mkString("\n")

	}
	
}

/** this is how we model the  values returned by the counterexamples...
  * 
  */

sealed trait GobraModelEntry{
	override def toString : String = "not implemented"
}


sealed trait LitEntry extends GobraModelEntry
sealed trait WithSeq {
	val values:Seq[LitEntry]
}


case class DummyEntry() extends LitEntry
case class FaultEntry(message:String)extends LitEntry {override def toString = message}
case class LitNilEntry() extends LitEntry{//TODO: Think of a way to implement pretty printing
	override def toString(): String = "null"	
}
case class LitIntEntry(value:BigInt) extends LitEntry {
	override def toString(): String = s"$value"
}
case class LitBoolEntry(value:Boolean) extends LitEntry {
	override def toString(): String = s"$value"
}
case class LitPermEntry(value:Double) extends LitEntry {//should this be a double? does this not cause trouble?
	override def toString(): String = s"$value"
}
case class LitOptionEntry(value:Option[LitEntry]) extends LitEntry {
	override def toString(): String = s"$value"
}
case class LitSetEntry(values:Set[LitEntry])extends LitEntry {
	override def toString(): String = s"{${values.map(_.toString).mkString(", ")}}"
}
case class LitMSetEntry(values:Set[LitEntry])extends LitEntry {
	override def toString(): String = s"{${values.map(_.toString).mkString(", ")}}"
}
case class LitArrayEntry(typ:ArrayT,values:Seq[LitEntry])extends LitEntry with WithSeq{
	override def toString(): String = Util.prettyPrint(this,0)//s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSliceEntry(typ:SliceT,begin:BigInt,end:BigInt,values:Seq[LitEntry]) extends LitEntry with WithSeq{
	override def toString(): String =  Util.prettyPrint(this,0)//s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSeqEntry(typ:SequenceT,values:Seq[LitEntry])extends LitEntry with WithSeq{
	override def toString(): String = Util.prettyPrint(this,0)//s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSparseEntry(full:WithSeq,relevant:Map[Int,LitEntry]){
	override def toString():String = relevant.map(x=>s"${x._1}:${x._2}").mkString("[",",","]")
}
case class LitStructEntry(typ:StructT,values:Map[String,LitEntry])extends LitEntry { //TODO: Pretty printing
	override def toString(): String = Util.prettyPrint(this,0) //s"struct{${values.map(x=>s"${x._1} = ${x._2.toString}").mkString("; ")}}"

}
case class LitStringEntry(value:String) extends LitEntry{
	override def toString() :String =s"${'"'}${value}${'"'}"
}
case class LitPointerEntry(typ:Type,value:LitEntry,address:BigInt)extends LitEntry {
	override def toString(): String = s"(&$address* -> $value)" 
}
//entries that are not designated (typed) as a pointer but are represented by a ref (adressable)
case class LitAdressedEntry(value:LitEntry,address:BigInt) extends LitEntry{
	override def toString():String = s"$value @$address"
}
case class LitDeclaredEntry(name:String,value:LitEntry)extends LitEntry {
	override def toString(): String = { Util.prettyPrint(this,0)
		/* value match {
			case LitStructEntry(_,_) => value.toString.replaceFirst("struct",name)
			case _ => value.toString()
		} */
	}
}
case class LitMapEntry(typ:MapT,value:Map[LitEntry,LitEntry]) extends LitEntry {
	override def toString(): String = value.toString()
}

//later
case class LitPredicateEntry()extends LitEntry {}

case class LitSharedEntry(value:LitEntry) extends LitEntry {
	override def toString:String = s"&$value"
}

