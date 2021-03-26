package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config,Parser}
import viper.silicon.interfaces.SiliconMappedCounterexample
import viper.silver
import viper.silver.{ast=> vpr}
import viper.silver.ast.{LineColumnPosition, SourcePosition}
import viper.silicon.reporting.{Converter,ExtractedModel,ExtractedModelEntry}
import viper.gobra.frontend.info.base.SymbolTable
//import viper.gobra.internal.utility.Nodes
import viper.silver.verifier.{Counterexample,Model}
import org.bitbucket.inkytonik.kiama.util._
import org.bitbucket.inkytonik.kiama.relation.TreeRelation
import java.util.HashMap


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

class CounterexampleBackTranslator(backtrack: BackTranslator.BackTrackInfo,
										info:CounterexampleConfig=CounterexampleConfigs.MappedCounterexamples,
									)extends DefaultErrorBackTranslator(backtrack){
	val translator : ((SiliconMappedCounterexample)=>Counterexample) = info match {
		case CounterexampleConfigs.MappedCounterexamples => mappedTranslation(_)
		case CounterexampleConfigs.NativeCounterexamples => nativeTranslation(_)
		case CounterexampleConfigs.ReducedCounterexamples => reducedTranslation(_)
		case CounterexampleConfigs.ExtendedCounterexamples => extendedTranslation(_)
	}
	var relevant_function :PFunctionDecl=null
	
	//this adds counterexamples to the error of the default translation...
	//the translate(reason) function is inherited by the DefaultErrorBackTranslator
	override def translate(error: silver.verifier.VerificationError): VerificationError ={
		val ret : VerificationError = super.translate(error) 
		//TODO: find all variable in scope of the Pnode of the error

		val typeinfo = backtrack.typeInfo
		val errinfo : Source.Verifier.Info = ret.info
		val pnode = errinfo.pnode

		
		val typenodes = typeinfo.tree.nodes 


		val viperModel :Map[String,ExtractedModelEntry] =error.counterexample match {
			case Some(c:SiliconMappedCounterexample) => c.converter.modelAtLabel.apply("old").entries
			case None => Map.empty
			case _ => Map.empty
		}
		val viperVars : Vector[String] = viperModel.keys.toSeq.toVector
		val viperProgram = backtrack.viperprogram
		val viperNode =error.offendingNode
		
		//all variable declarations of the viper program, issue: they do not contain the expression we can use to translate back
		val varDeclNodes = viperProgram.collect(x=>if(x.isInstanceOf[vpr.LocalVarDecl]&& viperVars.contains(x.asInstanceOf[vpr.LocalVarDecl].name)) Some (x.asInstanceOf[vpr.LocalVarDecl]) else None).collect({case Some(x)=> x})
		
		//contains all variable expressions we can use this to translate back to gobra... issue: there is a lot of them... and not unique
		val varUseNodes = viperProgram.collect(x=>if(x.isInstanceOf[vpr.AbstractLocalVar]&& viperVars.contains(x.asInstanceOf[vpr.AbstractLocalVar].name)) Some(x.asInstanceOf[vpr.AbstractLocalVar]) else None).collect({case Some(x)=> x})

		
		val declarationMap =(varDeclNodes.map(x=>(x,viperModel.apply(x.name)))).toMap
		val nativeDeclarationModel  = new DeclarationModel(declarationMap)


		//map from info to counterexample entry
		val declInfosMap = declarationMap.map(x=>(Source.unapply(x._1) match {case Some(t) =>(t)},x._2)).toSeq.sortBy(x=>x._1.origin.pos.start.line).toMap
	

		val sourceModel = new SourceModel(declInfosMap)

		val pDeclMap = declInfosMap.map(x=>(x._1.pnode,x._2))
		val pNodeModel = new PNodeModel(pDeclMap)
		
		
		val pInputMap =pDeclMap.filter(x=>x.isInstanceOf[PParameter]||x.isInstanceOf[PResult]) 
		val pInputModel = new PNodeModel(pInputMap)

		//issue: takes some random values
		val nativeNodeModel = new NodeModel((varUseNodes.map(x=>(x,viperModel.apply(x.name)))).toMap)
		//printf(s"$varUseNodes\n")
		

		//touple of gobra varuable use and viper variable
		val pUseNodes = varUseNodes.map(x=> try{ Some((Source.Parser.Single.fromVpr(x),x)) }catch{case _:Throwable => None}).collect({case Some(x)=>x})
		
		
		//printf(s"$pUseNodes\n${pUseNodes.size}")

		
		ret.counterexample = Some(new GobraCounterexample(sourceModel))
		ret
	}
	def isPnodePartOfBlock(block:PBlock,node:PNode):Boolean={
		if(block.stmts.contains(node)){
			true
		}else{
			false
		}
	}

	def mappedTranslation(counterexample:SiliconMappedCounterexample):Counterexample ={
		counterexample
	}

	def nativeTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	} 
	def reducedTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	} 
	def extendedTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	} 
	
	def filterLabel(variable:String,label:String): Boolean ={
		// this should be possible without this hack but how?
		label match {
			case "old" => ! variable.contains("_CN")
			case "returnLabel" => variable.contains("_CN")
			case _ => true //there should be no other labels hopefully. otherwise we display them 
		}
	}

  	def variableTranslateion(input:String):String ={//does not work with names, contining "_"
			input.takeWhile(_!='_')
  	} 
    def valueTranslation(input:ExtractedModelEntry):ExtractedModelEntry={//no translation sofar
	  input
 	}

}

trait GobraModel {
	def toString :String
	def toSilverModel:viper.silver.verifier.Model
}

case class DeclarationModel(entries:Map[vpr.LocalVarDecl,ExtractedModelEntry]) extends GobraModel{//with this we can map to the declaration of the variables
	override def toString: String = {
		entries.map(x => s"${x._1.name}:${x._1.typ} <- ${x._2.toString}").mkString("\n")
	}
	def toSilverModel ={
		new Model(entries.map(x=>(x._1.name,x._2.asValueEntry)))
	}
}
case class NodeModel(entries:Map[vpr.Node,ExtractedModelEntry]) extends GobraModel{
	override lazy val toString: String = {
    entries.map(x => s"${x._1} <- ${x._2.toString}").mkString("\n")
  }
  def toSilverModel ={
		new Model(entries.map(x=>(x._1.toString,x._2.asValueEntry)))
	}
}
case class PNodeModel(entries:Map[PNode,ExtractedModelEntry]) extends GobraModel{
	override lazy val toString: String = {
    entries.map(x => s"(${x._1}) <- ${x._2.toString}").mkString("\n")
  }
  def toSilverModel ={
		new Model(entries.map(x=>(x._1.toString,x._2.asValueEntry)))
	}
}
case class INodeModel(entries:Map[viper.gobra.ast.internal.Node,ExtractedModelEntry]) extends GobraModel{
	override lazy val toString: String = {
    entries.map(x => s"(${x._1}) <- ${x._2.toString}").mkString("\n")
  }
  def toSilverModel ={
		new Model(entries.map(x=>(x._1.toString,x._2.asValueEntry)))
	}
}
case class SourceModel(entries:Map[Source.Verifier.Info,ExtractedModelEntry]) extends GobraModel{
	override lazy val toString: String = {
    entries.map(x => s"(${x._1.pnode}\t/\t${x._1.node})\t<-\t${x._2.toString}\tat (${x._1.origin.pos})").mkString("\n")
  }
  def toSilverModel ={
		new Model(entries.map(x=>(x._1.toString,x._2.asValueEntry)))
	}
}
case class GobraCounterexample(gModel:GobraModel) extends Counterexample {
	val model = gModel.toSilverModel

	override lazy val toString: String = {
		gModel.toString() 
    //s"$buf\non return: \n${converter.extractedModel.toString}"
  }
}

/* trait gobraEntry extends ExtractedModelEntry{
	def viperModel: ExtractedModelEntry
} */