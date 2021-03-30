package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.gobra.ast.frontend._
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
import org.bitbucket.inkytonik.kiama.util._



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
		val viperModel :Map[String,sil.ExtractedModel] = counterexample match {
			case c:SiliconMappedCounterexample => c.converter.modelAtLabel
			case _ => Map.empty
		}
		val fi = viperModel.keys.head
		val viperVars : Seq[String] = viperModel.apply(fi).entries.keys.toSeq 
		val viperProgram = backtrack.viperprogram
		
		//all variable declarations of the viper program, issue: if two vipervariables have the same name we cannot distinguish (we have to assume they are unique)
		val varDeclNodes = viperProgram.collect(x=>if(x.isInstanceOf[vpr.LocalVarDecl]&& viperVars.contains(x.asInstanceOf[vpr.LocalVarDecl].name)) Some (x.asInstanceOf[vpr.LocalVarDecl]) else None).collect({case Some(x)=> x})
		
		//map from viper declarations to entries
		val declarationMap : Map[String,Map[vpr.LocalVarDecl,sil.ExtractedModelEntry]]=viperModel.map(y=>(y._1,varDeclNodes.map(x=>(x,y._2.entries.apply(x.name))).toMap))

		//map from info to counterexample entry
		val declInfosMap: Map[String,Map[Source.Verifier.Info,sil.ExtractedModelEntry]] = declarationMap.map(y=>(y._1,y._2.map(x=>(Source.unapply(x._1) match {case Some(t) => (t,x._2) }))))

		//translate the values
		val translated = declInfosMap.map(y=>(y._1,y._2.map(x=>(x._1,Util.valueTranslation(x._2,Util.getType(x._1.pnode,typeinfo))))))													
																			
		
		val glabelModel = new GobraModelAtLabel(translated.map(y=>(y._1,new GobraModel(y._2.map(x=>(x._1.pnode,x._2))))))

		
		
		//printf(s"$pUseNodes\n${pUseNodes.size}")
		val ret =  Some(new GobraCounterexample(glabelModel))
		ret
	}
}
object Util{
	def getType(pnode:PNode,info:viper.gobra.frontend.info.TypeInfo):Type={
		pnode match {
			case (x:PIdnNode) => info.typ(x)
			case (x:PParameter) => info.typ(x)
			case (x:PExpression) => info.typ(x)
			case (x:PMisc) => info.typ(x)
			case _ => UnknownType
		}
	}
	/**
	  * translates the input from viper to Gobra
	  *
	  * @param input 
	  * @param typ is used to help distinguish between differnet domains
	  * @return
	  */
	def valueTranslation(input:sil.ExtractedModelEntry,typ:Type):LitEntry={
	  input match {//simple types will be propagated
		  case sil.LitIntEntry(v) => LitIntEntry(v)
		  case sil.LitBoolEntry(b) => LitBoolEntry(b)
		  case sil.LitPermEntry(p) => LitPermEntry(p)
		  case sil.SeqEntry(_,v) => typ match { //here it is assumed, that at some point we see asequence
			  							case SequenceT(e) => LitSeqEntry(SequenceT(e),v.map(x => valueTranslation(x,e)))
										case ArrayT(l,e) => LitArrayEntry(ArrayT(l,e),v.map(x => valueTranslation(x,e)))
										case _ => DummyEntry()
									}
		  case _  => 
		  		typ match {
					  case _ => DummyEntry()
				  }
	  }
 	}
	type Identifier = String
	type Label = String
}


case class GobraCounterexample(gModel:GobraModelAtLabel) extends Counterexample{
	override def toString:String = gModel.toString
	val model =null
}


case class GobraModelAtLabel(labeledEntries:Map[Util.Label,GobraModel]){
	override def toString :String ={
		labeledEntries.map(x=>s"model at label ${x._1}:\n${x._2}").mkString("\n")
	}
}
case class GobraModel(entries:Map[PNode,GobraModelEntry]){
	override def toString :String = {
		val params = entries.filter(x=>x._1.isInstanceOf[PParameter]) //first we separate the different types of values
		val rest = entries.filterNot(x=>x._1.isInstanceOf[PParameter])
		params.map(x=>s"${x._1.toString}\t<- ${x._2.toString}").mkString("\n") ++ "\n" ++
		rest.map(x=>s"${x._1.toString}\t<- ${x._2.toString}").mkString("\n")
	}
	
}

/** this is how we model the  values returned by the counterexamples...
  * 
  */

sealed trait GobraModelEntry{
	override def toString : String = "not implemented"
}


sealed trait LitEntry extends GobraModelEntry

case class DummyEntry() extends LitEntry
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
case class LitArrayEntry(typ:ArrayT,values:Seq[LitEntry])extends LitEntry {
	override def toString(): String = s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSliceEntry(typ:SliceT,values:Seq[LitEntry]) extends LitEntry {
	override def toString(): String = s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSeqEntry(typ:SequenceT,values:Seq[LitEntry])extends LitEntry {
	override def toString(): String = s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitStructEntry(typ:StructT,values:Map[String,LitEntry])extends LitEntry { //TODO: Pretty printing
	override def toString(): String = s"struct{${values.map(x=>s"${x._1} = ${x._2.toString}").mkString("; ")}}"

}
case class LitPointerEntry(typ:Type,value:LitEntry)extends LitEntry {
	override def toString(): String = s"*->$value" 
}
case class LitDeclaredEntry(name:Util.Identifier,value:LitEntry)extends LitEntry {
	override def toString(): String = {
		value match {
			case LitStructEntry(_,_) => name ++ value.toString().substring(6)// remove "struct" from the value could also be done by inlining
			case _ => value.toString()
		}
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

