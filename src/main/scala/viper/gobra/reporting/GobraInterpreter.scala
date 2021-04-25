package viper.gobra.reporting
import viper.silicon.{reporting => sil}
import viper.gobra.reporting._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.translator.Names
import viper.silver.{ast => vpr}


trait GobraInterpreter extends sil.ModelInterpreter[GobraModelEntry,Type]
trait GobraDomainInterpreter[T] extends sil.DomainInterpreter[GobraModelEntry,T]

case class DefaultGobraInterpreter() extends GobraInterpreter{
	override def interpret(entry:sil.ExtractedModelEntry,info:Type): GobraModelEntry = DummyEntry()
}
object InterpreterCache{
	private var heap: Seq[(BigInt,Type)] = Seq()
	def isDefined(address:BigInt,typ:Type):Boolean = heap.contains((address,typ))
	def addAddress(address:BigInt,typ:Type):Unit = {heap=(address,typ)+:heap}
	def clearCache():Unit ={heap = Seq()}
}

/**
  * interprets all Extracted values to Gobra values using its sub interpreters for each individual type
  *
  * @param c
  */
case class MasterInterpreter(c:sil.Converter) extends GobraInterpreter{
	val optionInterpreter: GobraDomainInterpreter[OptionT] = OptionInterpreter(c)
	val productInterpreter: GobraDomainInterpreter[StructT] = ProductInterpreter(c)
	val boxInterpreter:GobraDomainInterpreter[Type] = BoxInterpreter(c)
	val indexedInterpreter:GobraDomainInterpreter[ArrayT] = IndexedInterpreter(c)
	val sliceInterpreter: GobraDomainInterpreter[SliceT]= SliceInterpreter(c)
	val pointerInterpreter : sil.AbstractInterpreter[sil.RefEntry,Type,GobraModelEntry] = PointerInterpreter(c)
	def interpret(entry:sil.ExtractedModelEntry,info:Type): GobraModelEntry ={
		entry match{
			case sil.LitIntEntry(v) => info match {
												case StringT => StringInterpreter(c).interpret(entry,null)
												case _ =>LitIntEntry(v)
										}
			case sil.LitBoolEntry(b) => LitBoolEntry(b)
			case sil.LitPermEntry(p) => LitPermEntry(p)
			case _:sil.NullRefEntry => LitNilEntry()
			case v:sil.VarEntry => interpret(c.extractVal(v),info)//TODO:make shure this does not pingpong
			case d:sil.DomainValueEntry => if(d.getDomainName.contains("Embfn$$")){//TODO: get name from Names
											boxInterpreter.interpret(d,info)
										}else{
											info match {//TODO: More interpreters
												case t:OptionT => optionInterpreter.interpret(d,t)
												case t:StructT =>  productInterpreter.interpret(d,t)
												case t:ArrayT => indexedInterpreter.interpret(d,t)
												case t:SliceT => sliceInterpreter.interpret(d,t)
												case DeclaredT(d,c) => val name = d.left.name
																		val actual = interpret(entry,c.symbType(d.right)) match {
																			case l:LitEntry => l
																			case _ => FaultEntry("not a lit entry")
																		}
																		LitDeclaredEntry(name,actual)
												case _ => DummyEntry()
											}}
			case sil.ExtendedDomainValueEntry(o,i) => interpret(o,info)
			case r:sil.RefEntry => pointerInterpreter.interpret(r,info) 
			case rr:sil.RecursiveRefEntry => info match{
													case t:PointerT => {val address = PointerInterpreter(c).nameToInt(rr.name,PointerInterpreter(c).filedname(rr,t.elem))
											 			LitAdressedEntry(FaultEntry(s"recursive call"),address)
															}
													case _ => FaultEntry("recursive reference to non pointer... how?")
												}
			case s:sil.SeqEntry => 	info match {
										case a:ArrayT =>  LitArrayEntry(a,s.values.map(x=> interpret(x,a.elem)
																						match {case l:LitEntry=> l;
																								case _ => FaultEntry("Could not resolve Element")
																						})
																		)
										case _ => DummyEntry()
										}
			case _ => FaultEntry(s"illegal call of interpret: ${entry}")
		}
	}
}

case class OptionInterpreter(c:sil.Converter) extends GobraDomainInterpreter[OptionT]{
	val nonFuncName :String = Names.optionIsNone
	val getFuncName : String = Names.optionGet
	def interpret(entry:sil.DomainValueEntry,info:OptionT): GobraModelEntry = {
		val doms = c.domains.filter(x=>x.valueName==entry.domain)
		if(doms.length==1){
			val functions:Seq[sil.ExtractedFunction] =doms.head.functions
			val noneFunc : sil.ExtractedFunction = functions.find(_.fname==nonFuncName) match {
																							case Some(value) => value; 
																							case None => return FaultEntry(s"${entry}: could not relsove, ($nonFuncName) not found")}
			val getFunc : sil.ExtractedFunction= functions.find(_.fname==getFuncName) match {
																						case Some(value) => value; 
																						case None => return FaultEntry(s"${entry}: could not relsove, ($getFuncName) not found")
																					}
			val isNone : Boolean = noneFunc.apply(Seq(entry)) match {
				case Right(sil.LitBoolEntry(b)) => b
				case _ => return FaultEntry(s"${entry}: could not relsove ($nonFuncName)")
			}
			if(isNone){
				LitOptionEntry(None)
			}else{
				val actualval:sil.ExtractedModelEntry = getFunc.apply(Seq(entry)) match {
					case Right(v) => v
					case _ => return FaultEntry(s"${entry}: could not relsove ($getFuncName)")
				}
				val gobraval = MasterInterpreter(c).interpret(actualval,info.elem) match {
					case e:LitEntry => e
					case x => return FaultEntry(s"internal error: ${x}")
				}
				LitOptionEntry(Some(gobraval))
			}
		}else{
			FaultEntry(s"could not relsove domain: ${entry.domain}")
		}
	}
}
case class ProductInterpreter(c:sil.Converter) extends GobraDomainInterpreter[StructT]{
	def getterFunc(i:Int,n:Int) = Names.getterFunc(i,n) 
	def interpret(entry:sil.DomainValueEntry,info:StructT) :GobraModelEntry ={
		val doms = c.domains.find(_.valueName==entry.domain)
		if(doms.isDefined){
			//printf(s"${doms.get}")
			val functions:Seq[sil.ExtractedFunction] =doms.get.functions
			val fields = info.fields
			val numFields = fields.size
			val getterNames = Seq.range(0,numFields).map(getterFunc(_,numFields))
			val getterfuncs = getterNames.map(x=>functions.find(n=>(n.fname==x||n.fname==s"ShStruct$x"))).collect({case Some(v)=> v})
			val fieldToVals = (fields.keys.toSeq.zip(getterfuncs).map(
												x=>(x._1,x._2.apply(Seq(entry)) match {case Right(v)=> v; 
																				case _ => return FaultEntry(s"${entry}: could not relsove (${x._1})")
																			})
												)).toMap
			try{
			val values = fields.map(x=>(x._1,MasterInterpreter(c).interpret(fieldToVals.apply(x._1),x._2) match{
																										case l:LitEntry=> l;
																										case _ => return FaultEntry("internal error struct")
																									}
										)) 
			LitStructEntry(info,values)
			}catch{
				case t:Throwable => printf(s"$t");return FaultEntry(s"${entry.domain} wrong domain for type: ${info.toString.replace("\n",";")}")
			}
			
		}else{
			FaultEntry(s"could not relsove domain: ${entry.domain}")
		}
	}
}

case class BoxInterpreter(c:sil.Converter) extends GobraDomainInterpreter[Type]{
	def unboxFunc(domain:String) = s"${Names.embeddingUnboxFunc}_$domain"
	def boxFunc(domain:String) = s"${Names.embeddingBoxFunc}_$domain"
	def interpret(entry:sil.DomainValueEntry,info:Type):GobraModelEntry={
		val functions = c.non_domain_functions
		val unbox = functions.find(_.fname==unboxFunc(entry.domain))
		val box = functions.find(_.fname==boxFunc(entry.domain))
		if(unbox.isDefined&&box.isDefined){
			val unboxed : sil.ExtractedModelEntry= Right(unbox.get.options.head._2) match{ //unboxing has some strange behaviour snaps and such
					case Right(x) => x
					case _ => return FaultEntry(s"wrong application of function $unbox")
			} 
			MasterInterpreter(c).interpret(unboxed,info)
		}else{
			FaultEntry(s"${unboxFunc(entry.domain)} not found")
		}
	}
}
case class IndexedInterpreter(c:sil.Converter) extends GobraDomainInterpreter[ArrayT] {
	def lenName(d:String) = Names.length(d) 
	def index(d:String) = Names.location(d)
	def interpret(entry:sil.DomainValueEntry,info:ArrayT) :GobraModelEntry ={
		val doms = c.domains.find(_.valueName==entry.domain)
		if(doms.isDefined){
			//printf(s"${doms.get}")
			val functions:Seq[sil.ExtractedFunction] =doms.get.functions
			val lengthFunc = functions.find(_.fname==lenName(doms.get.name))
			val length = lengthFunc match{
				case Some(value) => value.apply(Seq(entry)) match{
					case Right(i:sil.LitIntEntry) => i.value
					case _=> return FaultEntry("length ill defined")
				}
				case None =>  return FaultEntry("length not found")
			}
			//require(length==info.length) this cannot be enforced because we do not always know the array length ahead of time
			val offsetFunc = functions.find(_.fname==index(doms.get.name))
			if(offsetFunc.isDefined){
				val indexFunc = offsetFunc.get
				val values = 0.until(length.toInt-1).map(i=>{
				val x = indexFunc.apply(Seq(entry,sil.LitIntEntry(i))) match{
					case Right(x) => MasterInterpreter(c).interpret(x,info.elem) match {
						case x:LitEntry => x
						case _ => FaultEntry("not a literal")
					}
					case _=> return FaultEntry("could not resolve")
				}
				x
				})
				LitArrayEntry(info,values)
			}else{
				FaultEntry(s"offset not found")
			}
			
		}else{
			FaultEntry(s"could not relsove domain: ${entry.domain}")
		}
	}
}
case class SliceInterpreter(c:sil.Converter) extends GobraDomainInterpreter[SliceT]{
	val sarray = Names.sliceArray
	val soffset = Names.sliceOffset
	val slen = Names.sliceLength
	def interpret(entry:sil.DomainValueEntry,info:SliceT):GobraModelEntry={
		val doms = c.domains.find(_.valueName==entry.domain)
		if(doms.isDefined){
			val functions = doms.get.functions
			val funcSarray = functions.find(_.fname==sarray)
			val funcOffset = functions.find(_.fname==soffset)
			val funclength = functions.find(_.fname==slen)
			if(funclength.isDefined && funcOffset.isDefined && funcSarray.isDefined){
				val length = funclength.get.apply(Seq(entry)) match{
					case Right(i:sil.LitIntEntry) => i.value
					case _ => return FaultEntry("length not defined")
				}
				val offset = funcOffset.get.apply(Seq(entry)) match{
					case Right(i:sil.LitIntEntry) => i.value
					case _ => return FaultEntry("offset not defined")
				}
				val (array,arraytyp) = funcSarray.get.apply(Seq(entry)) match{
					case Right(x) => x match {
						case v:sil.VarEntry => c.extractVal(v) match {
														case s:sil.SeqEntry => (s,ArrayT(s.values.size,info.elem))
														case _ => return FaultEntry("extracted Value not a sequence")
													}
						case s:sil.SeqEntry => (s,ArrayT(s.values.size,info.elem))
						case d:sil.DomainValueEntry => (d,ArrayT(0,info.elem)) 
						case _ => (sil.OtherEntry("internal","error"),UnknownType)
					}
					case _ => return FaultEntry(s"$sarray false application")
				}
				val original = MasterInterpreter(c).interpret(array,arraytyp) match {
					case x:LitArrayEntry => x
					case _=> return FaultEntry("not an array")
				}
				LitSliceEntry(info,offset,offset+length,original.values.drop(offset.toInt).take(length.toInt))
			}else{
				FaultEntry(s"functions ($sarray ,$soffset, $slen) not found")
			}

		}else{
			FaultEntry(s"${entry.domain} not found")
		}
	}
}

case class PointerInterpreter(c:sil.Converter) extends sil.AbstractInterpreter[sil.RefEntry,Type,GobraModelEntry]{
	
	def interpret(entry:sil.RefEntry,info:Type): GobraModelEntry ={
		
		info match{
			case PointerT(elem )=>{
					val field = filedname(entry,elem)
					val address = nameToInt(entry.name,field)
					if(InterpreterCache.isDefined(address,info)){
						return LitRecursive(address)
					}
					val extracted: sil.RefEntry = c.extractVal(sil.VarEntry(entry.name,viper.silicon.state.terms.sorts.Ref)) match{
										//TODO: what if we don't find it?
								case r:sil.RefEntry =>r 
								case _ => return FaultEntry("false extraction")
					}
					val kek = extracted.fields.getOrElse(field,(sil.OtherEntry(s"$field: Field not found",s"help"),None))
					val fieldval = if(extracted.fields.isEmpty) return Util.getDefault(elem) 
						else if(kek._1.isInstanceOf[sil.OtherEntry]) extracted.fields.head._2._1 match {
												case r:sil.RefEntry => return LitAdressedEntry(interpret(r,info).asInstanceOf[LitEntry],nameToInt(entry.name,filedname(entry,info))) //problem this happens on the last step and therefore we cannot distinguish
												case x => x 
						}
					InterpreterCache.addAddress(address,info);
					val value = kek._1 match {
						case x:sil.OtherEntry => FaultEntry(s"$fieldval,$field")																												
						case r:sil.RefEntry =>  interpret(r,elem) //this we could potentially handle internally
						case v:sil.VarEntry => LitNilEntry()
						case d:sil.DomainValueEntry => MasterInterpreter(c).interpret(d,elem)//FaultEntry(s"$d") //TODO: resolve recursive entries
						case s:sil.SeqEntry=> LitNilEntry()
						case t => MasterInterpreter(c).interpret(t,elem)  //we know we don't have recursion
					}/* MasterInterpreter(c).interpret(kek._1,elem) match {
											case l:LitEntry => l
							} */
					LitPointerEntry(elem,value.asInstanceOf[LitEntry],address)
					}				
			case t => {
				//printf(s"halleluja:$t\n")
				val address = nameToInt(entry.name,filedname(entry,t))
				val value = interpret(entry,PointerT(t))
				value match {
						case p:LitPointerEntry =>  LitAdressedEntry(p.value,address)
						case x:LitEntry => LitAdressedEntry(x,address)
					}	
				}
		}
		
	}
	def nameToInt(name:String,field:String) :Int ={
		val id = Integer.parseInt(name.split("!").last)
		val offset = field.hashCode()
		(id +1)* 100 + (offset %100)
	}
	 
	def filedname(entry:sil.ExtractedModelEntry,i:Type) : String ={
		i match{
			case _:IntT => Names.pointerField(vpr.Int)
			case BooleanT => Names.pointerField(vpr.Bool)
			case StringT => Names.pointerField(vpr.Int)
			case PointerT(elem) => elem match{
				case x:StructT => "val$_ShStruct2_RefRef"
				case d:DeclaredT => filedname(entry,d.context.symbType(d.decl.right))
				case _ => Names.pointerField(vpr.Ref)
			} 
			//case _:ArrayT => "val$_ShArray_fRef"
			case _:DeclaredT => "val$_ShStruct2_RefRef"
			case x:StructT => "val$_ShStruct2_RefRef"
			case x => s"$x" //TODO: resolve all types or find a better way of doing it (maybe go through all fields and find one you like)
		}
	}
}


case class StringInterpreter(c:sil.Converter) extends sil.ModelInterpreter[GobraModelEntry,Any]{
	val stringDomain = Names.stringsDomain 
	val prefix = Names.stringPrefix
	def interpret(entry:sil.ExtractedModelEntry,info:Any): GobraModelEntry ={
		val doms = c.domains.find(_.valueName==stringDomain)
		if(doms.isDefined){
			val functions:Seq[sil.ExtractedFunction]=doms.get.functions
			entry match{
				case e:sil.LitIntEntry => functions.find(x=>x.fname.startsWith(prefix)&&x.default==e) match {
					case Some(f) => { val hex :String= f.fname.stripPrefix(prefix)
									val value = hexToChar(toArray(hex))
									LitStringEntry(value)

								}
					case _=>  FaultEntry("string literal not found")
				}

				case _=>  FaultEntry("could not resolve string because not an Int Entry")
			}
		}else{
			FaultEntry("could not resolve string")
		}
	}
	def hexToChar(input:Seq[String]):String ={
		input.map(Integer.parseInt(_,16)).map(_.toChar).mkString
	}
	def toArray(input:String) : Seq[String] ={
		if(input=="") Seq()
		else if(input.size<2) throw new IllegalArgumentException()
		else {
			val (first,second) = input.splitAt(2)
			Seq(first)++toArray(second)
		}
	}
}
