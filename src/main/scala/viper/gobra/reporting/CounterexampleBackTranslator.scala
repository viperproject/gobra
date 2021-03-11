package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config,Parser}
import viper.silicon.interfaces.SiliconMappedCounterexample
import viper.silver
import viper.silver.ast.{LineColumnPosition, SourcePosition}
import viper.silicon.reporting.{Converter,ExtractedModel,ExtractedModelEntry}
import _root_.viper.silver.verifier.{Counterexample,Model}

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
										parsedPackage:PPackage
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
	def translate(error: silver.verifier.VerificationError): VerificationError ={
		val ret : VerificationError = default.translate(error) // this is the error we return all we do is append the counterexample
		//TODO: find all variable in scope of the Pnode of the error
		val errinfo : Source.Verifier.Info= ret.info
		val pnode =errinfo.pnode
		printf(s"${pnode.getClass()}")
		//assert(pnode.getClass() <: PExpression)// if not we are in big trouble...
		//val errorPosition : SourcePosition= pnode.positions
		val pos = errinfo.origin
		val node =errinfo.node
		val vec =backtrack.errorT
		//assert(node.isInstanceOf[PExpression])
		//for lack of better methods we just have to reparse the file...
		
		//split the packages up... TODO: what is important?
		val declarations =parsedPackage.declarations //or all?
		val members = declarations.filter(_.isInstanceOf[PActualMember])
		val allfunctions = declarations.filter(_.isInstanceOf[PFunctionDecl]).map(_.asInstanceOf[PFunctionDecl])
		val functions_with_same_pre =allfunctions.filter(_.spec.posts.contains(pnode))//extracts all functions with spec as
		val functions_with_same_post =allfunctions.filter(_.spec.pres.contains(pnode))//carefull this is not what we want... we want the context of the caller --> find caller =(
		
		//this only looks for first block asserttion assertions within if statements not covered TODO
		val functions_with_assertion =allfunctions.filter(x=> x.body match {case None => false;case Some((params,block))=> block.stmts.contains(pnode)})
		//relevant_function =  functions_with_same_spec.apply(0)
		//printf(s"$functions_with_same_pre%s",functions_with_same_pre.size)
		printf(s"$functions_with_assertion%s",functions_with_assertion.size)
		
		
		//what is the meaning of this methods?
		val methods : Vector[PMethodDecl]= declarations.filter(_.isInstanceOf[PMethodDecl]).map(_.asInstanceOf[PMethodDecl])
		val variables = members.filter(_.isInstanceOf[PVarDecl]).map(_.asInstanceOf[PVarDecl])
		val constatns = members.filter(_.isInstanceOf[PConstDecl]).map(_.asInstanceOf[PConstDecl])
		
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



	def translate(reason: silver.verifier.ErrorReason): VerificationErrorReason = default.translate(reason)

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
	/**
	  * this method is supposed to reeturn the function in which the counterexamples is located
	  *
	  * @param functions
	  * @param model
	  */
	def findFunction(functions:Vector[PFunctionDecl],model:ExtractedModel):PFunctionDecl={
		val filterdunc = ((f:PFunctionDecl)=>model.entries.keySet.exists(x=>f.result.toString().contains(variableTranslateion(x))))
		val havesame_params = functions.filter(filterdunc)
		havesame_params.apply(0)
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