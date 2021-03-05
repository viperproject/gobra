package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.silicon.interfaces.SiliconMappedCounterexample
import viper.silver
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
										info:CounterexampleConfig=CounterexampleConfigs.MappedCounterexamples
									)extends BackTranslator.ErrorBackTranslator{
	val default = new DefaultErrorBackTranslator(backtrack)
	//moved logic fom config to translator
	val translator : ((SiliconMappedCounterexample)=>Counterexample) = info match {
		case CounterexampleConfigs.MappedCounterexamples => mappedTranslation(_)
		case CounterexampleConfigs.NativeCounterexamples => nativeTranslation(_)
		case CounterexampleConfigs.ReducedCounterexamples => reducedTranslation(_)
		case CounterexampleConfigs.ExtendedCounterexamples => extendedTranslation(_)
	}
	def translate(error: silver.verifier.VerificationError): VerificationError ={
		val ret =default.translate(error)
		ret.counterexample = error.counterexample match {
			case Some(example:SiliconMappedCounterexample) => Some(translator(example))
			case Some(_) => None // how to handle other counterexamples?
			case None => None
		}
		ret
	} 
	def translate(reason: silver.verifier.ErrorReason): VerificationErrorReason = default.translate(reason)

	def mappedTranslation(counterexample:SiliconMappedCounterexample):Counterexample ={
		new GobraCounterexample(counterexample.converter,counterexample.model)
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

}

case class GobraCounterexample(converter:Converter,nativeModel:Model) extends Counterexample {
	val model = nativeModel

	override lazy val toString: String = {
	val map :Map[String,ExtractedModel]= converter.modelAtLabel.map(x=>(x._1,ExtractedModel(x._2.entries.map(x=>(variableTranslateion(x._1),valueTranslation(x._2))))))
	

    map
      .map(x => s"model at label: ${(x._1)}\n${x._2.toString}\n")
      .mkString("\n")
    //s"$buf\non return: \n${converter.extractedModel.toString}"
  }

  def variableTranslateion(input:String):String ={
	  input.takeWhile(_!='_')
  }

  def valueTranslation(input:ExtractedModelEntry):ExtractedModelEntry={
	  input
  }
}
