import viper.silicon.{reporting => sil}
import viper.gobra.reporting._
import viper.gobra.frontend.info.base.Type._
import org.scalatest.{Assertion, Inside, Succeeded}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.util.TypeBounds
import viper.gobra.translator.Names
import viper.silicon.state.{terms => term}

class CounterexampleUnitTests extends AnyFunSuite with Matchers with Inside {
	val constFalse = sil.LitBoolEntry(false)
	val constTrue =sil.LitBoolEntry(true)
	val constZero = sil.LitIntEntry(0)
	val intType = IntT(TypeBounds.DefaultInt)

	test("Integer"){
		testInterpreterSingle(null,sil.LitIntEntry(42),intType,LitIntEntry(42))
	}
	test("String"){
		val stringId = sil.LitIntEntry(1)
		val helloWorld = Names.stringPrefix ++ "68656c6c6f20776f726c64"
		val func = sil.ExtractedFunction(helloWorld,Seq(),term.sorts.Int,Map(),stringId)
		
		val StringDomain = sil.DomainEntry(Names.stringsDomain,Seq(),Seq(func))
		val converter = new TestConverter(Seq(StringDomain),Seq(),identity(_))
		val should = LitStringEntry("hello world")
		testInterpreterSingle(converter,stringId,StringT,should)
	}
	
	test("Boolean"){
		testInterpreterSingle(null,sil.LitBoolEntry(false),BooleanT,LitBoolEntry(false))
	}
	test("Integer Array"){
		val length = 10
		val values = 0.until(length)
		val typ = ArrayT(length,IntT(TypeBounds.DefaultInt))
		val should = LitArrayEntry(typ,values.map(x=>LitIntEntry(x)))
		val embedding = Names.embeddingDomain ++ Names.freshName
		val seqName = "Seq!val!1"
		val entry1 = sil.VarEntry(seqName,term.sorts.Seq(term.sorts.Int))
		val entry2 = sil.VarEntry(seqName++"0",term.sorts.Seq(term.sorts.Int))
		val boxSort = term.sorts.UserSort(viper.silicon.state.Identifier(embedding))
		val sort = term.sorts.Seq(term.sorts.Int)
		val entry = sil.SeqEntry(seqName,values.map(sil.LitIntEntry(_)).toVector)
		val getValEntry = (v:sil.VarEntry) => v.name match { case seqName => entry	 case _ => sil.NullRefEntry("Ref!val!1")	}
		val boxedVal = sil.DomainValueEntry(embedding,"1")
		val unbox = sil.ExtractedFunction(Names.embeddingUnboxFunc++embedding, Seq(boxSort), sort, Map((Seq(boxedVal),entry1)), entry2)
		val box = sil.ExtractedFunction(Names.embeddingBoxFunc++embedding, Seq(sort), boxSort, Map((Seq(entry1),boxedVal)), sil.DomainValueEntry(embedding,"0"))
		val embDomain = sil.DomainEntry(embedding,Seq(),Seq())
		val converter = new TestConverter(Seq(embDomain),Seq(unbox,box),getValEntry)
		
		testInterpreterSingle(converter,entry1,typ,should)
	}
	test("Options"){
		val box = 1024
		val value  = sil.LitIntEntry(box)
		val domName = "Option"
		val intSorts = term.sorts.Int
		val concDomName = domName ++ "[Int]"
		val optSort = term.sorts.UserSort(viper.silicon.state.Identifier(concDomName))
		val someVal = sil.DomainValueEntry(concDomName,"1")
		val someOther = sil.DomainValueEntry(concDomName,"2")
		val noneVal = sil.DomainValueEntry(concDomName,"0")
		val emptymap = Map.empty[Seq[sil.ExtractedModelEntry],sil.ExtractedModelEntry]
		val domfuncs = Seq(
						(Names.optionNone,Seq(optSort),intSorts,emptymap,someVal),
						(Names.optionIsNone,Seq(optSort),term.sorts.Bool,Map((Seq(noneVal), constTrue)),constFalse),
						(Names.optionGet, Seq(optSort), intSorts, Map((Seq(someVal),value)),constZero),
						(Names.optionSome, Seq(intSorts),optSort,Map((Seq(value),someVal)),someOther)
						).map(x=>sil.ExtractedFunction(x._1,x._2,x._3,x._4.asInstanceOf[Map[Seq[sil.ExtractedModelEntry],sil.ExtractedModelEntry]],x._5))
		val optionDomain = sil.DomainEntry(domName,Seq(viper.silver.ast.Int),domfuncs)
		val converter = new TestConverter(Seq(optionDomain),Seq(),identity(_))
		testInterpreterSingle(converter,someVal,OptionT(intType),LitOptionEntry(Some(LitIntEntry(box))))
		testInterpreterSingle(converter,noneVal,OptionT(intType),LitOptionEntry(None))
	}
	

	def testInterpreterSingle(c:sil.Converter, example:sil.ExtractedModelEntry,typ:Type,expectedOutput:GobraModelEntry): Unit = {
		val produced = MasterInterpreter(c).interpret(example,typ)
		assert(	produced == expectedOutput ,s"Should be: $expectedOutput, but got: $produced")
	}

	def testFile(path:String,expected:GobraCounterexample): Boolean ={
		false
	}

	def mkConverter() :sil.Converter ={
		new TestConverter(null,null,null)
	}
}

class TestConverter(doms:Seq[sil.DomainEntry],functions:Seq[sil.ExtractedFunction],extractValnew:(sil.VarEntry => sil.ExtractedModelEntry)) extends sil.Converter(null,null,null,null,null) {
	override lazy val non_domain_functions = functions
	override lazy val domains = doms
	override def extractVal(x:sil.VarEntry) = extractValnew(x)
}


