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
  * simple counterexample distinction
  */
object CounterexampleConfigs{
	object MappedCounterexamples extends CounterexampleConfig
	object NativeCounterexamples extends CounterexampleConfig
	object ReducedCounterexamples extends CounterexampleConfig
	object ExtendedCounterexamples extends CounterexampleConfig
}

case class CounterexampleBackTranslator(backtrack: BackTranslator.BackTrackInfo){
	//this adds counterexamples to the error of the default translation...
	//the translate(reason) function is inherited by the DefaultErrorBackTranslator
	def translate(counterexample: silver.verifier.Counterexample): Option[GobraCounterexample] ={
				
		val typeinfo = backtrack.typeInfo
		val info = backtrack.config.counterexample match {case Some(x)=> x}
		val viperModel :Map[String,sil.ExtractedModelEntry] = counterexample match {
			case c:SiliconMappedCounterexample => c.converter.modelAtLabel.apply("old").entries
			case _ => Map.empty
		}
		val viperVars : Seq[String] = viperModel.keys.toSeq
		val viperProgram = backtrack.viperprogram
		//val viperNode =error.offendingNode
		
		//all variable declarations of the viper program, issue: if two vipervariables have the same name we cannot distinguish
		val varDeclNodes = viperProgram.collect(x=>if(x.isInstanceOf[vpr.LocalVarDecl]&& viperVars.contains(x.asInstanceOf[vpr.LocalVarDecl].name)) Some (x.asInstanceOf[vpr.LocalVarDecl]) else None).collect({case Some(x)=> x})
		
		//contains all variable expressions we can use this to translate back to gobra... issue: there is a lot of them... and not unique
		//val varUseNodes = viperProgram.collect(x=>if(x.isInstanceOf[vpr.AbstractLocalVar]&& viperVars.contains(x.asInstanceOf[vpr.AbstractLocalVar].name)) Some(x.asInstanceOf[vpr.AbstractLocalVar]) else None).collect({case Some(x)=> x})

		//map from viper declarations to entries
		val declarationMap =(varDeclNodes.map(x=>(x,viperModel.apply(x.name)))).toMap
		//val nativeDeclarationModel  = new DeclarationModel(declarationMap)


		//map from info to counterexample entry
		val declInfosMap = declarationMap.map(x=>(Source.unapply(x._1) match
																		 {case Some(t) =>
																			(t,Util.valueTranslation(x._2,Util.getType(t.pnode,typeinfo) match {case Some(v)=>v}))}))
		val sourceModel = new SourceModel(declInfosMap,typeinfo)
		//we lose some information when we cast to a string sadly...
		val gModel = new GobraModel(declInfosMap.map(x=>(x._1.pnode.toString,x._2)))
		val glabelModel = new GobraModelAtLabel(Seq(("old",gModel)).toMap)
		//map from gobra declaration to entries
		val pDeclMap = declInfosMap.map(x=>(x._1.pnode,x._2))
		/* val pNodeModel = new PNodeModel(pDeclMap) */
		
		//add type info
		val pTypedMap = declInfosMap.map(x=>(((x._1.pnode,Util.getType(x._1.pnode,typeinfo)),x._2)))
		/* val pTypeModel = new TypeModel(pTypedMap) */
		//issue: takes some random values
		//val nativeNodeModel = new NodeModel((varUseNodes.map(x=>(x,viperModel.apply(x.name)))).toMap)
		//printf(s"$varUseNodes\n")
		
		
		//printf(s"$pUseNodes\n${pUseNodes.size}")
		val ret = info match {
			case CounterexampleConfigs.NativeCounterexamples => Some(new GobraCounterexample(glabelModel,sourceModel))
			case CounterexampleConfigs.ReducedCounterexamples => Some(new GobraCounterexample(glabelModel,sourceModel))
			case CounterexampleConfigs.ExtendedCounterexamples => Some(new GobraCounterexample(glabelModel,sourceModel))
			case CounterexampleConfigs.MappedCounterexamples => Some(new GobraCounterexample(glabelModel,sourceModel))
		} 
		ret
	}
}
object Util{
	def getType(pnode:PNode,info:viper.gobra.frontend.info.TypeInfo):Option[Type]={
		pnode match {
			case (x:PIdnNode) => Some(info.typ(x))
			case (x:PParameter) => Some(info.typ(x))
			case (x:PExpression) => Some(info.typ(x))
			case (x:PMisc) => Some(info.typ(x))
			case _ => None
		}
	}
	def valueTranslation(input:sil.ExtractedModelEntry,typ:Type):GobraModelEntry={
	  input match {//simple types will be propagated
		  case sil.LitIntEntry(v) => LitIntEntry(v)
		  case sil.LitBoolEntry(b) => LitBoolEntry(b)
		  case sil.LitPermEntry(p) => LitPermEntry(p)
		  case _  => 
		  		typ match {
					  //this is obvously false, it serves illustrative purposes
					  case OptionT(elem) => LitOptionEntry(Some(valueTranslation(sil.LitBoolEntry(false),elem).asInstanceOf[LitEntry]))
					  case ArrayT(length, elem) => LitArrayEntry(ArrayT(length, elem),Seq.fill(length.toInt)(LitIntEntry(1)))
					  case StructT(clauses, decl, context) => LitStructEntry(typ.asInstanceOf[StructT],clauses.map(x=>(x._1,LitStructEntry(null,Seq.fill(3)(("t",LitIntEntry(1))).toMap))))
					  case _ => DummyEntry()
				  }
	  }
 	}
	type Identifier = String
	type Label = String
}



case class SourceModel(entries:Map[Source.Verifier.Info,GobraModelEntry],typeinfo:TypeInfo){
	override lazy val toString: String = {
	val params = entries.filter(x=>x._1.pnode.isInstanceOf[PParameter])
	val rest = entries.filterNot(x=>x._1.pnode.isInstanceOf[PParameter]||(!x._1.pnode.isInstanceOf[PIdnNode]))//we do not have gurantee that these are actually pidndefs as sources

    "Old:\n" ++params.map(x => {val node = x._1.pnode.asInstanceOf[PNamedParameter];s"(${node.id}:${Util.getType(node,typeinfo)}\t/\t${x._1.node})\t<-\t${x._2.toString}\tat (${x._1.origin.pos})"}).mkString("\n") ++ "\n" ++
    "\nAt assertion:\n" ++ rest.map(x =>{val node = x._1.pnode ;s"(${node}:${Util.getType(node,typeinfo)}\t/\t${x._1.node})\t<-\t${x._2.toString}\tat (${x._1.origin.pos})"}).mkString("\n")
	
  }
  def toSilverModel ={
	  
		new Model(entries.map(x=>(x._1.toString,silver.verifier.ConstantEntry(x._2.toString))))
	}
}

case class GobraCounterexample(gModel:GobraModelAtLabel,
								sources:SourceModel) extends Counterexample{
	override def toString:String = gModel.toString
	val model = sources.toSilverModel
}


case class GobraModelAtLabel(labeledEntries:Map[Util.Label,GobraModel]){
	override def toString :String ={
		labeledEntries.map(x=>s"model at label ${x._1}:\n${x._2}").mkString("\n")
	}
}
case class GobraModel(entries:Map[Util.Identifier,GobraModelEntry]){
	override def toString :String = entries.map(x=>s"${x._1}\t<- ${x._2.toString}").mkString("\n")
}

sealed trait GobraModelEntry{
	override def toString : String = "not implemented"
}


sealed trait LitEntry extends GobraModelEntry

case class DummyEntry() extends LitEntry
case class LitNilEntry() extends LitEntry{
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
case class LitArrayEntry(typ:ArrayT,values:Seq[LitEntry])extends LitEntry {//how to tell whether the entries match the type? maybe we don't care
	override def toString(): String = s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSliceEntry(typ:SliceT,values:Seq[LitEntry]) extends LitEntry {
	override def toString(): String = s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitSeqEntry(typ:SequenceT,values:Seq[LitEntry])extends LitEntry {
	override def toString(): String = s"[${values.map(_.toString).mkString(", ")}]"
}
case class LitStructEntry(typ:StructT,values:Map[String,LitEntry])extends LitEntry {
	override def toString(): String = s"{\n${values.map(x=>s"\t\t${x._1} = ${prettySubprint(x._2.toString)}").mkString(";\n")}\t}"
	def prettySubprint(formatted:String): String ={
		val split = formatted.split("\n")
		val first = split.head ++ "\n" // first value will be on the first line with no special indentation
		val rest = split.tail.map(x=>s"\t$x").mkString("\n")
		first ++ rest
	}	

}
case class LitPointerEntry(typ:Type,value:LitEntry)extends LitEntry {

}
case class LitDeclaredEntry(name:PTypeDecl,value:LitEntry)extends LitEntry {

}
case class LitMapEntry(typ:MapT,value:Map[LitEntry,LitEntry]) extends LitEntry {

}

//later
case class LitPredicateEntry()extends LitEntry {}

case class LitSharedEntry(value:LitEntry) extends LitEntry {
	override def toString:String = s"&$value"
}

