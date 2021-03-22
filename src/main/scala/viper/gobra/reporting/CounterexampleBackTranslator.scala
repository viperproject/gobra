package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config,Parser}
import viper.silicon.interfaces.SiliconMappedCounterexample
import viper.silver
import viper.silver.ast.{LineColumnPosition, SourcePosition}
import viper.silicon.reporting.{Converter,ExtractedModel,ExtractedModelEntry}
//import viper.gobra.internal.utility.Nodes
import _root_.viper.silver.verifier.{Counterexample,Model}
import java.nio.file.{Path,Paths}
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
		val posMngr :PositionManager= backtrack.pom
		val typeinfo = backtrack.typeInfo
		val errinfo : Source.Verifier.Info = ret.info
		val errorigin = errinfo.origin
		val node  = errinfo.node // gobra internal node
		val pnode = errinfo.pnode
		val sourcepos :SourcePosition = errorigin.pos //error position
		val root=typeinfo.codeRoot(pnode)
		val variables : Vector[PIdnNode] = typeinfo.variables(root)
		printf(s"$variables")
		/*
		val constants = declarations.filter(_.isInstanceOf[PConstDecl]).map(_.asInstanceOf[PConstDecl])
		val variables = declarations.filter(_.isInstanceOf[PVarDecl]).map(_.asInstanceOf[PVarDecl])
		val globals = (variables.flatMap(x => x.left.map(y=> (x.typ,y)))) ++ (constants.flatMap(x => x.left.map(y=> (x.typ,y))))
		val funcvars = extractVaraibles(relevant_function)
		
		funcvars.map(x=>printf(s"${x._1},${x._2}\n")) */
		//what is the meaning of this methods?
/* 		val methods : Vector[PMethodDecl]= declarations.filter(_.isInstanceOf[PMethodDecl]).map(_.asInstanceOf[PMethodDecl])
		val variables = members.filter(_.isInstanceOf[PVarDecl]).map(_.asInstanceOf[PVarDecl])
		val constatns = members.filter(_.isInstanceOf[PConstDecl]).map(_.asInstanceOf[PConstDecl])
		 */
		//write the counterexamples
		ret.counterexample = error.counterexample match {
			case Some(example:SiliconMappedCounterexample) => Some(translator(example))
			case Some(_) => None // how to handle other counterexamples?
			case None => None
		}
		ret
	}
	def isPnodePartOfBlock(block:PBlock,node:PNode):Boolean={
		if(block.stmts.contains(node)){
			true
		}else{
			false
		}
	}

	def extractVaraibles(function:PFunctionDecl):Vector[(PType,PIdnNode)] = {
		val args = function.args.map(x=>(x.typ,x.asInstanceOf[PNamedParameter].id)) ++ function.result.outs.map(x=>(x.typ,x.asInstanceOf[PNamedParameter].id))

		val locals =(function.body match {
			case Some((params,block)) => params.shareableParameters.map(x=>(get_global_var_types(x),x)) /* ++ Nodes.subnodes(block).filter(_.isInstanceOf[]) */
			case None => Vector.empty
		})
		args ++ locals
	}
	def get_global_var_types(id:PIdnNode):PType={
		new PIntType()
	}

	def mappedTranslation(counterexample:SiliconMappedCounterexample):Counterexample ={
		val allinfo =counterexample.converter.modelAtLabel
																//filter to get rid of redundant info 		//mapping to make it more readable
		val map = allinfo.map(x=>(x._1,ExtractedModel(x._2.entries.filter(y=>filterLabel(y._1,x._1)).map(x=>(variableTranslateion(x._1),valueTranslation(x._2))))))
		new GobraCounterexample(map,counterexample.model)
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

case class GobraCounterexample(labelModels:Map[String,ExtractedModel],nativeModel:Model) extends Counterexample {
	val model = nativeModel

	override lazy val toString: String = {
		labelModels 	//label     model (ExtracredModel[String,ExtractedEntry])
      .map(x => s"${(x._1)}:\n${x._2.toString}\n")
      .mkString("\n") 
    //s"$buf\non return: \n${converter.extractedModel.toString}"
  }
}

/* trait gobraEntry extends ExtractedModelEntry{
	def viperModel: ExtractedModelEntry
} */