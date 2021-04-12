package viper.gobra.reporting
import viper.silicon.{reporting => sil}
import viper.gobra.reporting._
import viper.gobra.frontend.info.base.Type._


trait GobraInterpreter extends sil.ModelInterpreter[GobraModelEntry,Type]
trait GobraDomainInterpreter[T] extends sil.DomainInterpreter[GobraModelEntry,T]

case class DefaultGobraInterpreter() extends GobraInterpreter{
	override def interpret(entry:sil.ExtractedModelEntry,info:Type): GobraModelEntry = DummyEntry()
}

/**
  * interprets all Extracted values to Gobra values using its sub interpreters for each individual type
  *
  * @param c
  */
case class MasterInterpreter(c:sil.Converter) extends GobraInterpreter{
	val optionInterpreter = OptionInterpreter(c)
	def interpret(entry:sil.ExtractedModelEntry,info:Type): GobraModelEntry ={
		entry match{
			case sil.LitIntEntry(v) => LitIntEntry(v)
			case sil.LitBoolEntry(b) => LitBoolEntry(b)
			case sil.LitPermEntry(p) => LitPermEntry(p)
			case v:sil.VarEntry => interpret(c.extractVal(v),info)//TODO:make shure this does not pingpong
			case d:sil.DomainValueEntry => info match {//TODO: More interpreters
												case t:OptionT => optionInterpreter.interpret(d,t)
												case _ => DummyEntry()
											}
			case sil.ExtendedDomainValueEntry(o,i) => interpret(o,info)
			case _ => FaultEntry("illegal call of interpret")
		}
		
	}
}

class OptionInterpreter(c:sil.Converter) extends GobraDomainInterpreter[OptionT]{
	val nonFuncName :String = "optIsNone"//TODO: find out where they are generated
	val getFuncName : String = "optGet"
	def interpret(entry:sil.DomainValueEntry,typ:OptionT): GobraModelEntry = {
		val doms = c.domains.filter(x=>x.valueName==entry.domain)
		if(doms.length==1){
			val functions = doms.head.functions
			//TODO: make this safe
			val noneFunc : sil.ExtractedFunction = functions.filter(_.fname==nonFuncName).head 
			val getFunc : sil.ExtractedFunction= functions.filter(_.fname==getFuncName).head
			val isNone : Boolean = noneFunc.apply(Seq(entry)) match {
				case Right(sil.LitBoolEntry(b)) => b
				case _ => return FaultEntry(s"could not relsove option: ${entry}")
			}
			if(isNone){
				LitOptionEntry(None)
			}else{
				val actualval:sil.ExtractedModelEntry = getFunc.apply(Seq(entry)) match {
					case Right(v) => v
					case _ => return FaultEntry(s"could not relsove option: ${entry}")
				}
				val gobraval = viper.gobra.reporting.Util.valueTranslation(actualval,typ.elem,c)
				LitOptionEntry(Some(gobraval))
			}
		}else{
			FaultEntry(s"could not relsove domain: ${entry.domain}")
		}
	}
}