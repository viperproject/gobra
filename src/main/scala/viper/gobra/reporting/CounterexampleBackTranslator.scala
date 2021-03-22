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
										parsedPackage:PPackage,
										inputfiles:Vector[Path]
									)extends BackTranslator.ErrorBackTranslator{
	val default = new DefaultErrorBackTranslator(backtrack)
	var relevant_function: PFunctionDecl =null
	//moved logic fom config to translator
	val translator : ((SiliconMappedCounterexample)=>Counterexample) = info match {
		case CounterexampleConfigs.MappedCounterexamples => mappedTranslation(_)
		case CounterexampleConfigs.NativeCounterexamples => nativeTranslation(_)
		case CounterexampleConfigs.ReducedCounterexamples => reducedTranslation(_)
		case CounterexampleConfigs.ExtendedCounterexamples => extendedTranslation(_)
	}
	def translate(reason: silver.verifier.ErrorReason): VerificationErrorReason = default.translate(reason)

	def translate(error: silver.verifier.VerificationError): VerificationError ={
		val ret : VerificationError = default.translate(error) // this is the error we return all we do is append the counterexample
		//TODO: find all variable in scope of the Pnode of the error
		val posMngr = parsedPackage.positions
		val errinfo : Source.Verifier.Info= ret.info
		val sourcepos :SourcePosition = errinfo.origin.pos
		val pnode = errinfo.pnode
		val origin = errinfo.origin
		val (file:Path , linenr:Int, colnr:Int) = SourcePosition.unapply(sourcepos) match {
																						case Some((p,s,e))=>(p,s.line,s.column)
																						case _ =>(sourcepos.file,0,0)
																						}
		//TODO: find out if this works with multiple files
		printf(s"$inputfiles\n")
		val absolutePath = inputfiles.filter(p=>p.endsWith(file)).apply(0)
		val klarpos :Position = new Position(linenr,colnr,new FileSource(s"${absolutePath.toString()}"))
		

		val declarations =parsedPackage.declarations 
		val errorContext = posMngr.positions.findNodesContaining(declarations,klarpos)
		if(errorContext.size==1){
			//TODO what if we don't have a function?
			relevant_function = errorContext.apply(0).asInstanceOf[PFunctionDecl]
			//printf(s"$relevant_function")
		}else{
			printf("no single function context...")
		}
		val constants = declarations.filter(_.isInstanceOf[PConstDecl]).map(_.asInstanceOf[PConstDecl])
		val variables = declarations.filter(_.isInstanceOf[PVarDecl]).map(_.asInstanceOf[PVarDecl])
		val globals = (variables.flatMap(x => x.left.map(y=> (x.typ,y)))) ++ (constants.flatMap(x => x.left.map(y=> (x.typ,y))))
		val funcvars = extractVaraibles(relevant_function)
		
		funcvars.map(x=>printf(s"${x._1},${x._2}\n"))
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
		new GobraCounterexample(map,counterexample.model,relevant_function.id.toString())
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

case class GobraCounterexample(labelModels:Map[String,ExtractedModel],nativeModel:Model,functionid:String) extends Counterexample {
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