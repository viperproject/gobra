package viper.gobra.reporting
import viper.silicon.{reporting => sil}
import viper.gobra.reporting._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.{TypeInfo,ExternalTypeInfo}
import viper.gobra.translator.util.DomainGenerator
import viper.gobra.ast.frontend._
import viper.gobra.translator.Names
import viper.silver.{ast => vpr}
import scala.util.matching.Regex


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

	private var viperPredicates : Seq[vpr.Predicate] =Seq()
	def setPredicates(preds:Seq[vpr.Predicate]):Unit ={viperPredicates=preds}
	def clearPreds():Unit ={viperPredicates=Seq()}
	def getPreds():Seq[vpr.Predicate] = viperPredicates

	private var gobraPredicates : Seq[PFPredicateDecl] =Seq()
	def setGobraPredicates(preds:Seq[PFPredicateDecl]):Unit ={gobraPredicates=preds}
	def clearGobraPreds():Unit ={gobraPredicates=Seq()}
	def getGobraPreds():Seq[PFPredicateDecl] = gobraPredicates

	private var typeInfo : viper.gobra.frontend.info.ExternalTypeInfo =null
	def setTypeInfo(info:viper.gobra.frontend.info.ExternalTypeInfo):Unit ={typeInfo=info}
	def getType(pnode:PNode):Type={
		pnode match {
			case (x:PIdnNode) => typeInfo.typ(x)
			case (x:PParameter) => typeInfo.typ(x)
			case (x:PExpression) => typeInfo.typ(x)
			case (x:PMisc) => typeInfo.typ(x)
			case _ => UnknownType
		}
	}
}

/**
  * interprets all Extracted values to Gobra values using its sub interpreters for each individual type
  *
  * @param c
  */
case class MasterInterpreter(c:sil.Converter) extends GobraInterpreter{
	val optionInterpreter: GobraDomainInterpreter[OptionT] = OptionInterpreter(c)
	val productInterpreter: GobraDomainInterpreter[StructT] = ProductInterpreter(c)
	val sharedStructInterpreter : GobraDomainInterpreter[StructT] = SharedStructInterpreter(c)
	val boxInterpreter:GobraDomainInterpreter[Type] = BoxInterpreter(c)
	val indexedInterpreter:GobraDomainInterpreter[ArrayT] = IndexedInterpreter(c)
	val sliceInterpreter: GobraDomainInterpreter[SliceT]= SliceInterpreter(c)
	val pointerInterpreter : sil.AbstractInterpreter[sil.RefEntry,Type,GobraModelEntry] = PointerInterpreter(c)
	val interfaceInterpreter :GobraDomainInterpreter[InterfaceT] = InterfaceInterpreter(c)
	val channelInterpreter : sil.AbstractInterpreter[sil.LitIntEntry,ChannelT,GobraModelEntry] = ChannelInterpreter(c)
	def interpret(entry:sil.ExtractedModelEntry,info:Type): GobraModelEntry ={
		entry match{
			case sil.LitIntEntry(v) => info match {
												case StringT => StringInterpreter(c).interpret(entry,null)
												case DeclaredT(d,c) => val name = d.left.name
																		val actual = interpret(entry,c.symbType(d.right)) match {
																			case l:LitEntry => l
																			case _ => FaultEntry("not a lit entry")
																		}
																		LitDeclaredEntry(name,actual)
												case ch:ChannelT => channelInterpreter.interpret(sil.LitIntEntry(v),ch)
												case _ =>LitIntEntry(v)
										}
			case sil.LitBoolEntry(b) => info match{
				case DeclaredT(d,c) => val name = d.left.name; LitDeclaredEntry(name,LitBoolEntry(b))
				case _ =>LitBoolEntry(b)
			}
			case sil.LitPermEntry(p) => LitPermEntry(p)
			case _:sil.NullRefEntry => LitNilEntry()
			case v:sil.VarEntry => interpret(c.extractVal(v),info)//TODO:make shure this does not pingpong
			case d:sil.DomainValueEntry => if(d.getDomainName.contains("Embfn$$")){//TODO: get name from Names
											boxInterpreter.interpret(d,info)
										}else{
											info match {//TODO: More interpreters
												case t:OptionT => optionInterpreter.interpret(d,t)
												case t:StructT =>  if(d.getDomainName.startsWith("ShStruct"))
																		 sharedStructInterpreter.interpret(d,t) 
																	else 
																		productInterpreter.interpret(d,t)
												case t:ArrayT => if(d.getDomainName.startsWith("Slice")){
																		sliceInterpreter.interpret(d,SliceT(t.elem))
																}else{
																	indexedInterpreter.interpret(d,t)
																}
													
												case t:SliceT => sliceInterpreter.interpret(d,t)
												case DeclaredT(d,c) => val name = d.left.name
																		val actual = interpret(entry,c.symbType(d.right)) match {
																			case l:LitEntry => l
																			case _ => FaultEntry("not a lit entry")
																		}
																		LitDeclaredEntry(name,actual)
												case i:InterfaceT => interfaceInterpreter.interpret(d,i)
												case FunctionT(args,res) => FaultEntry("TODO: Functions")
												case p:PointerT => 	val newType =  p.elem match {
																				case a:ArrayT => sharedTypify(a) //TODO handle slices and such (move to concrete interpreter)
																				
																				case x => x
																	}
																	val adress = PointerInterpreter(c).nameToInt("*!1000",p.elem.toString)
																	//InterpreterCache.addAddress(adress,info)
																	interpret(entry,p.elem) match {
																		case LitAdressedEntry(value, address) => LitPointerEntry(p.elem,value,address)
																		case p:LitPointerEntry => p
																		case LitDeclaredEntry(name,LitAdressedEntry(value,address)) => LitPointerEntry(p.elem,LitDeclaredEntry(name,value),address)
																		case LitDeclaredEntry(_,LitNilEntry())  => LitNilEntry()
																		case x:LitEntry => LitPointerEntry(p.elem,x,adress)
																	}
												case pred:PredT => PredicateInterpreter(c).interpret(d,pred)
												case dom:DomainT => UserDomainInterpreter(c).interpret(d,dom)
												case x => FaultEntry(s"$x ${DummyEntry()}")
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
										case a:ArrayT=>  LitArrayEntry(a,s.values.map(x=> interpret(x,a.elem)
																						match {case l:LitEntry=> l;
																								case _ => FaultEntry("Could not resolve Element")
																						})
																		)
										case seq:SequenceT => LitSeqEntry(seq,s.values.map(x=> interpret(x,seq.elem)
																						match {case l:LitEntry=> l;
																								case _ => FaultEntry("Could not resolve Element")
																						})
																		)
										case _ => FaultEntry(s"$info not implemented")
										}
			/* case s:sil.SetEntry => info match {
				case _ => FaultEntry(s"$s ,$info not implemented")
			} */
			
			case sil.PredHeapEntry(name,args,perm) => info match {
						case PredT(types) =>LitPredicateEntry(name,
															args.zip(types).map(x=>interpret(x._1,x._2).asInstanceOf[LitEntry]),
															perm.map(LitPermEntry))
						case _ => FaultEntry(s"Predicate type expected but found: $info")
							}
			case _ => FaultEntry(s"illegal call of interpret: ${entry}")
		}
	}
	def sharedTypify(old:Type):Type ={
		old match {
			case DeclaredT(d,c) => 	val actual = sharedTypify(c.symbType(d.right))
								//DeclaredT(viper.gobra.ast.frontend.PTypeDef(actual, d.left),c)
								actual
			case ArrayT(l,elem) => ArrayT(l,PointerT(elem))
			case StructT(clauses, decl, context) =>StructT(clauses.map(x=>(x._1,(x._2._1,PointerT(x._2._2)))), decl, context)
			case x=> x
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
case class SharedStructInterpreter(c:sil.Converter) extends GobraDomainInterpreter[StructT]{
def getterFunc(i:Int,n:Int) = Names.sharedStructDomain ++ Names.getterFunc(i,n) 
	def interpret(entry:sil.DomainValueEntry,info:StructT) :GobraModelEntry ={
		val doms = c.domains.find(_.valueName==entry.domain)
		//val default =  c.non_domain_functions.find(_.fname.startsWith(Names.sharedStructDfltFunc)).get.default
		//if(entry==default) return LitNilEntry()
		if(doms.isDefined){
			//printf(s"${doms.get}")
			val functions:Seq[sil.ExtractedFunction] =doms.get.functions
			val fields = info.fields
			val numFields = fields.size
			val getterNames = Seq.range(0,numFields).map(getterFunc(_,numFields))
			val getterfuncs = getterNames.map(x=>functions.find(n=>n.fname==x)).collect({case Some(v)=> v})
			val fieldToVals = (fields.keys.toSeq.zip(getterfuncs).map(
												x=>(x._1,x._2.apply(Seq(entry)) match {case Right(v)=> v; 
																				case _ => return FaultEntry(s"${entry}: could not relsove (${x._1})")
																			})
												)).toMap
			val mem =  Integer.parseInt(entry.id)
			val offset =  (entry.domain).hashCode.abs % 100
			var address =BigInt( mem * 100 + (if(mem!=0) offset else 0) ) // shuld be 0 if we are dealing with the default value
			try{
				
			val values = fields.map(x=>(x._1,MasterInterpreter(c).interpret(fieldToVals.apply(x._1),PointerT(x._2)) match{
																										case p:LitPointerEntry => if(address==0){address = offset}; p.value
																										case l:LitEntry=> l;
																										case _ => return FaultEntry("internal error struct")
																									}
										)) 
			//if(address!=0)
			LitAdressedEntry(LitStructEntry(info,values),address)
			//else
			//FaultEntry("unknown")//LitNilEntry()			
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
			val unboxed : sil.ExtractedModelEntry= try{Right(unbox.get.options.head._2) match{ //unboxing has some strange behaviour snaps and such
					case Right(x) => x
					case _ => return FaultEntry(s"wrong application of function $unbox")
			} 
		}catch {
			case _:Throwable => unbox.get.default
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
			var address :BigInt= 0;
			if(offsetFunc.isDefined){
				val indexFunc = offsetFunc.get
				val values = 0.until(length.toInt).map(i=>{
					val x = indexFunc.apply(Seq(entry,sil.LitIntEntry(i))) match{
						case Right(x) => MasterInterpreter(c).interpret(x,info.elem) match {
							case a:LitAdressedEntry => address+= a.address; a.value
							case x:LitEntry => x
							case _ => FaultEntry("not a literal")
						}
						case _=> return FaultEntry("could not resolve")
					}
					x
				})
				val value = LitArrayEntry(info,values)
				if(address==0){
					value
				}else{
					LitAdressedEntry(value,address)
				}
				
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
				val (array,arraytyp,locfun:sil.ExtractedFunction) = funcSarray.get.apply(Seq(entry)) match{
					case Right(x) => x match {
						case v:sil.VarEntry => c.extractVal(v) match {
														case s:sil.SeqEntry => (s,ArrayT(s.values.size,info.elem),null)
														case _ => return FaultEntry("extracted Value not a sequence")
													}
						case s:sil.SeqEntry => (s,ArrayT(s.values.size,info.elem),null) // same here
						case d:sil.DomainValueEntry => (d,ArrayT(0,PointerT(info.elem)),
														c.domains.find(_.valueName==d.domain).get.functions.find(_.fname==Names.location(d.getDomainName)).get.apply(Seq(d)) match{
															case Left(f) => f
															case _ => return FaultEntry("function ShArrayloc not found...")
														}
														) 
						case _ => (sil.OtherEntry("internal","error"),UnknownType,null)
					}
					case _ => return FaultEntry(s"$sarray false application")
				}
				/* val original = MasterInterpreter(c).interpret(array,arraytyp) match {
					case x:LitArrayEntry => x
					case _=> return FaultEntry("not an array")
				} */
				def loc(v:BigInt) = {
						locfun.apply(Seq(sil.LitIntEntry(v))) match {
							case Right(t) => MasterInterpreter(c).interpret(t,PointerT(info.elem)) match {
								case p:LitPointerEntry => p.value
								case l:LitEntry => l
							}
							case _ => FaultEntry("not resolvable function loc")
						}
				}
				val values = (offset until (offset+length)).map(x=> loc(x))
				LitSliceEntry(info,offset,offset+length,values)
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
					val perm = kek._2 
					//printf(s"$perm\n")
					val fieldval = if(extracted.fields.isEmpty) return  LitNilEntry()
						else if(kek._1.isInstanceOf[sil.OtherEntry]) extracted.fields.head._2._1 match {
												case r:sil.RefEntry => return LitAdressedEntry(interpret(r,info).asInstanceOf[LitEntry],nameToInt(entry.name,filedname(entry,info))) //problem this happens on the last step and therefore we cannot distinguish
												case n:sil.NullRefEntry => return LitNilEntry() 
												case x => x 
						}
					InterpreterCache.addAddress(address,info);
					val value = kek._1 match {
						case x:sil.OtherEntry => FaultEntry(s"not Found Field:$field but found $fieldval in ${extracted.fields.head._1}")																												
						case r:sil.RefEntry =>  interpret(r,elem) match {
							case LitAdressedEntry(value, a) => return LitAdressedEntry(LitPointerEntry(elem,value.asInstanceOf[LitEntry],a),address)
							case x=> x
						} //this we could potentially handle internally		
						case t => MasterInterpreter(c).interpret(t,elem) 
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
						case n:LitNilEntry => Util.getDefault(t)
						case x:LitEntry => LitAdressedEntry(x,address)
					}	
				}
		}
		
	}
	def nameToInt(name:String,field:String) :Int ={
		try{
		val id = Integer.parseInt(name.split("!").last)
		val offset = field.hashCode()
		(id +1)* 100 + (offset %100)
		}catch{
			case _:NumberFormatException => 0
		}
	}
	 
	def filedname(entry:sil.ExtractedModelEntry,i:Type) : String ={ //TODO: improve (especially shared structs with more than one type (Ref,Shstruct))
		i match{
			case _:IntT => Names.pointerField(vpr.Int)
			case BooleanT => Names.pointerField(vpr.Bool)
			case StringT => Names.pointerField(vpr.Int)
			case PointerT(elem) => elem match{
				case x:StructT => Names.pointerField(vpr.Int).replace("Tuple","ShStruct")
				case d:DeclaredT => filedname(entry,d.context.symbType(d.decl.right))
				//case _:IntT => Names.pointerField(vpr.Int)
				//case BooleanT => Names.pointerField(vpr.Bool)
				//case StringT => Names.pointerField(vpr.Int)
				case _ => Names.pointerField(vpr.Ref)
			} 
			//case _:ArrayT => "val$_ShArray_fRef"
			case d:DeclaredT => filedname(entry,d.context.symbType(d.decl.right))
			case x:StructT => entry match {
				case r:sil.RefEntry=>s"val$$_ShStruct${x.fields.size}${if(x.fields.size==0)""else "_"}${"Ref".repeat(x.fields.size)}" //ISSUE: can be val$_Tuple{n}_{Types} or val$_Tuple{n}_{Types}
				case _ => s"val$$_Tuple${x.fields.size}_${x.fields.map(x=>filedname(null,x._2).replaceFirst("val$$_Tuple",""))}"
			}
			case InterfaceT(decl, context) => "val$_Tuple2_RefTypes" //TODO: make this more general
			case DomainT(decl,context) => "TODO: resolve Domain field"
			case ch:ChannelT => Names.pointerField(vpr.Int)
			case x => s"$x" //TODO: resolve all types 
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
			first+:toArray(second)
		}
	}
}
case class InterfaceInterpreter(c:sil.Converter) extends GobraDomainInterpreter[InterfaceT]{
	def interpret(entry:sil.DomainValueEntry,info:InterfaceT): GobraModelEntry ={
		 val doms = c.domains.find(_.valueName==entry.domain)
		 if(doms.isDefined){
			val valuefunc = doms.get.functions.find(_.fname==Names.getterFunc(0,2))match{case Some(x) => x ;case _ => return FaultEntry("Value function not defined")}
			val typfunc   = doms.get.functions.find(_.fname==Names.getterFunc(1,2))match{case Some(x) => x ; case _ => return FaultEntry("no type function?")}
			val value = valuefunc.apply(Seq(entry)) match{case Right(v) => v}
			val typ = typfunc.apply(Seq(entry)) match{case Right(v) => v}
			val typeDom = c.domains.find(_.valueName==Names.typesDomain).get 
			//either which it is or a list ow which it could be
			val typeinfo =getType(typ,typeDom)
			val typeDecls = info.context.asInstanceOf[TypeInfo].tree.nodes.filter(_.isInstanceOf[PTypeDecl]).map(_.asInstanceOf[PTypeDecl])
			val gobraType = typeinfo match {
				case (Left(v),p) => {
					// we can find out which named type it is TODO: make named finding better (e.g named with _ in them)
					
					val declaredT = fnameToType(v.fname,info.context) match {
						case Right(x) => x
						case Left(x) => return x
					}
					if(p){
						PointerT(declaredT)
					}else{
						declaredT
					}
				}
				case (Right(o),p) => {
									val opts = get_options(o,value,typeDecls,info.context) 
									if(opts.length > 1) return UnresolvedInterface(info,opts)
									else if(opts.length == 1) return opts.head // there is only one option this must be the one
									else if(p) return FaultEntry("possibly nil")
									else return FaultEntry("possibly empty interface")
									}
			}
			val fieldName = PointerInterpreter(c).filedname(if(typeinfo._2){sil.RefEntry("l",null)}else{sil.LitBoolEntry(false)},gobraType)
			//printf(s"$gobraType")
			//
			//ISSUE: we do not know the (viper) type of the entry...
			//TODO: find correct viper type
			val polyDom = c.domains.find(x=>isSamePoly(x.valueName,fieldName))
			val viperVal = polyDom match{
				case Some(x) => {
					x.functions.find(_.fname=="unbox_Poly").get.apply(Seq(value)) match{ case Right(x) => x}
				}
				case _ => try{
					c.extractVal(value.asInstanceOf[sil.VarEntry])
				}catch{case _:Throwable => return FaultEntry(s"no corresponding Polymorphism for ${fieldName.drop(5).takeWhile(_!='_')}")}
			}
			//printf(s"\n${info.context.symbType(info.decl)};;\n $field\n ${fieldname.drop(5)}\n")
			MasterInterpreter(c).interpret(viperVal,gobraType)
		 }else{
			FaultEntry(s"${entry.domain} not found")
		 }

	}

	def isSamePoly(domName:String,typeName:String) :Boolean = {
		val ts = domName.drop(5).replace('[', '_')
      			.replace("]", "")
      				.replace(",", "") // a parameterized Viper type uses comma-space separated types if there are multiple
      					.replace(" ", "")
		 s"val$$_$ts" == typeName
	}

	def fnameToType(fname:String,context:ExternalTypeInfo):Either[GobraModelEntry,Type] ={
		val typeDecls = context.asInstanceOf[TypeInfo].tree.nodes.filter(_.isInstanceOf[PTypeDecl]).map(_.asInstanceOf[PTypeDecl])
		val namedName = fname.takeWhile(_!='_')
		typeDecls.find(_.left.name==namedName) match {
						case Some(decl) => Right(DeclaredT(decl, context))
						case _ => namedName match {
								case "nil" => Left(FaultEntry("probably nil interface"))
								case "empty" => Left(FaultEntry("probably empty interface"))
								case _ => Left(FaultEntry(s"could not resolve $namedName"))
							}
								
					}
	}


	def getType(typ:sil.ExtractedModelEntry,typeDom:sil.DomainEntry) :(Either[sil.ExtractedFunction,Seq[sil.ExtractedFunction]],Boolean) = { //TODO: handle pointer to pointer
		//context.externalRegular(PIdnDef(function.takeWhile(_!='_'))).get.rep.toString
		val pointerfunc = typeDom.functions.find(_.fname=="pointer_Types")
		val isreversable = pointerfunc.isDefined && pointerfunc.get.options.values.toSeq.contains(typ)
		val isDefault= pointerfunc.isDefined && pointerfunc.get.default == typ
		if(isreversable){
			val actualtyp = pointerfunc.get.options.toSeq.find(_._2==typ).get._1.head
			//printf(s"$actualtyp;;\n$typeDom")
			val corr_typ_func =typeDom.functions.find(_.default==actualtyp) match{case Some(x)=> x}
			(Left(corr_typ_func),true) 
		
		}else if(isDefault){
			val isNot = pointerfunc.get.options.map(_._1)
			val corr_types = typeDom.functions.filterNot(y=>(isNot.find(_==y.default).isDefined || isnonTypeFunction(y.fname)))
			(Right(corr_types.toSeq),true)
		}
		else{
			val corr_typ_func =  typeDom.functions.find(_.default==typ) match{case Some(x)=> x ; 
																				case _ => typeDom.functions.find(_.fname==s"${Names.emptyInterface}_Types") match {
																					case Some(x)=>x
																					case _ => typeDom.functions.find(_.fname=="nil_Types") match{
																						case Some(x) => x
																						case _=> return (Right(Seq()),false)
																					}
																				}
																			}
			(Left(corr_typ_func),false)
		}
	}
	//get all posibilities of values the 
	def get_options(functions:Seq[sil.ExtractedFunction],value:sil.ExtractedModelEntry,typedecls:Seq[PTypeDecl],context:ExternalTypeInfo): Seq[GobraModelEntry] = {
		val names = functions.map(_.fname.takeWhile(_!='_'))
		val declarations = typedecls.collect(x=>{if(names.contains(x.left.name.toString)) Some(DeclaredT(x, context))else None} ).collect(_ match {case Some(x) => x})
		val fieldNameswithtyps = declarations map (x=>(PointerInterpreter(c).filedname(sil.RefEntry("l",null),PointerT(x)),PointerT(x)))
		//TODO: find correct viper type
		val polysandtyps = fieldNameswithtyps.collect(f=>c.domains.find(d=>isSamePoly(d.valueName,f._1))match{case Some(x)=> (x,f._2)})
		val relevantpolys = polysandtyps.filter(x=>isPointerViper(x._1.valueName.drop(5)))
		//TODO: more filter
		val withcorrectunbox = relevantpolys.filter(x=>x._1.functions.find(_.fname=="box_Poly")match {case Some(f)=>f.image.contains(value) })
		val values = relevantpolys.collect(x=>x._1.functions.find(_.fname=="unbox_Poly")match {case Some(f)=>f.apply(Seq(value))match{case Right(r)=>(r,x._2)} } )
		values.map(x=>MasterInterpreter(c).interpret(x._1,x._2))
	}
	def isPointerViper(name:String) :Boolean ={ //TODO: add more
		name.startsWith("Sh") || name.equals("Ref")
	}
	//filter out functions that are irrelevant
	def isnonTypeFunction(fname:String) :Boolean ={//TODO: make this smarter
		fname.startsWith(Names.emptyInterface) ||
		fname.startsWith(Names.toInterfaceFunc) ||
		fname.startsWith(Names.typeOfFunc) ||
		fname.startsWith(Names.dynamicPredicate) ||
		fname.startsWith(Names.implicitThis) 
	}
}
case class ChannelInterpreter(c:sil.Converter) extends sil.AbstractInterpreter[sil.LitIntEntry,ChannelT,GobraModelEntry] {
	def interpret(entry:sil.LitIntEntry,info:ChannelT) :GobraModelEntry= {
		val preds = c.extractedHeap.entries.filter(x=>x.isInstanceOf[sil.PredHeapEntry]&&x.asInstanceOf[sil.PredHeapEntry].args==Seq(entry))
		if(entry.value == 0){
			return LitNilEntry()
		}

		
		//printf(s"$preds\n")
		//TODO: move to Names
		val isSend = preds.find(_.toString.startsWith("SendChannel")) match {case Some(x)=> interPerm(x.asInstanceOf[sil.PredHeapEntry]);case None => Some(false)}
		val isRecv = preds.find(_.toString.startsWith("RecvChannel")) match {case Some(x)=> interPerm(x.asInstanceOf[sil.PredHeapEntry]);case None => Some(false)}
		val isOpen = preds.find(_.toString.startsWith("IsChannel")) match {
			case Some(x)=> if (interPerm(x.asInstanceOf[sil.PredHeapEntry]).getOrElse(false)) {if(isRecv.getOrElse(false)||isSend.getOrElse(false)) Some(2) else Some(1)} else Some(0)
			case None => if(isRecv.getOrElse(false)||isSend.getOrElse(false)) Some(2) else None }
		val buffSize=c.non_domain_functions.find(_.fname.startsWith("BufferSize")) match {
			case Some(x)=> x.apply(Seq(entry)) match {
				case Right(sil.LitIntEntry(v))=> v;
				 case _=> BigInt(0)}
			case _ => return FaultEntry("buffsize not found")
		}
		ChannelEntry(info,buffSize,isOpen,isSend,isRecv)
	}
	def interPerm(x:sil.PredHeapEntry):Option[Boolean] = {
		import viper.silicon.state.terms._
		x.perm match{
			case Some(Rational.zero) => Some(false)
			case Some(Rational.one) => Some(true)
			case Some(x) if(x > Rational(0,1)) =>  Some(true)
			case _ => None
		}
	}
}

case class PredicateInterpreter(c:sil.Converter) extends GobraDomainInterpreter[PredT] {
	def interpret(entry:sil.DomainValueEntry,info:PredT) = {
		val preds = c.extractedHeap.entries.filter(x=>x.isInstanceOf[sil.PredHeapEntry]/* &&x.asInstanceOf[sil.PredHeapEntry].args==Seq(entry) */)
		val domOpt = c.domains.find(_.valueName == entry.domain) 
		val symbolConv =new viper.silicon.state.DefaultSymbolConverter 
		//printf(s"$preds --- ${c.non_domain_functions}\n")
		//TODO: wait until reverse functions are generated
		if( domOpt.isDefined){
			val dom = domOpt.get
			val corr_function = dom.functions.find(_.image.contains(entry)).get
			//This may look complicated but essentially we see wether the argumnets of a predicate and the corresponding entry match
			val viperPred = InterpreterCache.getPreds().filter(p=>p.formalArgs.map(x=>symbolConv.toSort(x.typ)).zip(corr_function.argtypes).find(x=>x._1!=x._2).isEmpty && 
																					{val gop = InterpreterCache.getGobraPreds().find(x=> p.name.startsWith(x.id.name))
																					gop.isDefined && gop.get.args.drop(corr_function.argtypes.size).map(InterpreterCache.getType(_)).zip(info.args).find(x=>x._1!=x._2).isEmpty
																				}  
															) // TODO: get corresponding predicate
			val gobraPred = InterpreterCache.getGobraPreds().filter(x=> viperPred.find(_.name.startsWith(x.id.name)).isDefined) 
			//printf(s"$viperPred -- $gobraPred \n")
			val args = 	corr_function.options.find(_._2 == entry) match {
				case Some(x) => if(gobraPred.size>=1)
									Some(x._1.zip(gobraPred.head.args.map(InterpreterCache.getType(_))).map((x=>MasterInterpreter(c).interpret(x._1,x._2).asInstanceOf[LitEntry])))
								else
									None
				case _ => if (gobraPred.size>=1)
								Some(gobraPred.head.args.dropRight(info.args.size).zipWithIndex.map(x=>UnknownValueButKnownType(s"a${x._2}".replace("\n",""),InterpreterCache.getType(x._1))))
						else 
							None
			} 
			
			FCPredicate(if(gobraPred.size==1) gobraPred.head.id.name else gobraPred.map(_.id.name).mkString("/"),
				args,info.args)
			
		}else{
			FaultEntry("Predicate domain not found")
		}
	}
}

case class UserDomainInterpreter(c:sil.Converter) extends GobraDomainInterpreter[DomainT] {
	def interpret(entry:sil.DomainValueEntry,info:DomainT) = {
		FaultEntry("Domain not implemented")
	}
}

