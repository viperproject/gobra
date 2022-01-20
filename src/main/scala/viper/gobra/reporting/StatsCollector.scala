package viper.gobra.reporting

import org.bitbucket.inkytonik.kiama.relation.NodeNotInTreeException
import viper.gobra.ast.frontend.{PDomainFunction, PFPredicateDecl, PFunctionDecl, PMPredicateDecl, PMPredicateSig, PMethodDecl, PMethodImplementationProof, PMethodSig, PNode, PParameter, PPredConstructor}
import viper.gobra.ast.internal.BuiltInMember
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.util.Violation
import viper.silver.ast.{FuncApp, Function, Member, Method, MethodCall, Node, Predicate, PredicateAccess}
import viper.silver.reporter.Time

import scala.annotation.tailrec

case class StatsCollector(reporter: GobraReporter) extends GobraReporter {
  var typeInfo: TypeInfo = _

  // Maps a gobra member name to a gobra member entry
  private var memberMap: Map[String, GobraMemberEntry] = Map()
  // Maps a viper member name to a gobra member entry
  private var viperMemberNameGobraMemberMap: Map[String, GobraMemberEntry] = Map()

  case class GobraMemberEntry(pkg: String, memberName: String, args: String, var viperMembers: List[ViperMemberEntry], isTrusted: Boolean, isAbstract: Boolean, isBuiltIn: Boolean) {
    override def toString: String = pkg + "." + memberName + args + " trusted: " + isTrusted + " abstract: " + isAbstract + " builtin: " + isBuiltIn
  }
  case class ViperMemberEntry(member: Member, time: Time, success: Boolean, cached: Boolean)
  case class GobraMemberInfo(pkg: String, memberName: String, args: String, isTrusted: Boolean, isAbstract: Boolean, isBuiltIn: Boolean)

  def getWarnings: List[String] = {
    var warnings: List[String] = List()
    memberMap.keys.foreach(g => {
      //println(memberMap(g));
      memberMap(g).viperMembers.foreach(v => {
        // Check if any viper dependencies correspond to a gobra method
        getDependencies(v.member).foreach(dep => viperMemberNameGobraMemberMap.get(dep) match {
          // Trusted implies abstracted, so we match trusted first
          case Some(GobraMemberEntry(pkg, memberName, args, _, true, _, false)) => warnings = warnings.appended("Warning: Member " + g + " depends on trusted member " + pkg + "." + memberName + args + "\n")
          case Some(GobraMemberEntry(pkg, memberName, args, _, _, true, false)) => warnings = warnings.appended("Warning: Member " + g + " depends on abstract member " + pkg + "." + memberName + args + "\n")
          case _ =>
        })
      })
    })
    warnings
  }

  def addResult(pkg: String, memberName: String, args: String, entry: ViperMemberEntry, isTrusted: Boolean, isAbstract: Boolean, isBuiltIn: Boolean): Unit = this.synchronized({
    val key = pkg + "." + memberName + args

    memberMap.get(key) match {
      case Some(existing) => existing.viperMembers = existing.viperMembers.appended(entry)
      case None => memberMap = memberMap + (key -> GobraMemberEntry(pkg, memberName, args, List(entry), isTrusted, isAbstract, isBuiltIn))
    }

    viperMemberNameGobraMemberMap.get(entry.member.name) match {
      // Viper methods should only correspond to a single Gobra method. If they somehow won't,
      case Some(_) =>
        Violation.violation(viperMemberNameGobraMemberMap(entry.member.name).equals(memberMap(key)), "Viper method corresponds to multiple gobra methods")
      case None => viperMemberNameGobraMemberMap = viperMemberNameGobraMemberMap + (entry.member.name -> memberMap(key))
    }
  })

  override val name: String = "StatsCollector"

  override def report(msg: GobraMessage): Unit = {
    msg match {
        // Ignore messages about BuildInMembers
      case GobraEntitySuccessMessage(_, _, info, _, _) if info.node.isInstanceOf[BuiltInMember] =>
      case GobraEntityFailureMessage(_, _, info, _, _, _) if info.node.isInstanceOf[BuiltInMember] =>

      case GobraEntitySuccessMessage(_, e, info, time, cached) =>
        Violation.violation(typeInfo != null, "No type info available for stats reporter")
        getMemberInformation(info.pnode) match {
          case GobraMemberInfo(pkg, memberName, args, isTrusted, isAbstract, isBuiltIn) =>
            addResult(pkg, memberName, args, ViperMemberEntry(e, time, success = true, cached = cached), isTrusted, isAbstract, isBuiltIn)
        }
      case GobraEntityFailureMessage(_, e, info, _, time, cached) =>
        Violation.violation(typeInfo != null, "No type info available for stats reporter")
        getMemberInformation(info.pnode) match {
          case GobraMemberInfo(pkg, memberName, args, isTrusted, isAbstract, isBuiltIn) =>
            addResult(pkg, memberName, args, ViperMemberEntry(e, time, success = false, cached = cached), isTrusted, isAbstract, isBuiltIn)
        }
      case _ =>
    }
    // Pass message to next reporter
    reporter.report(msg)
  }

  /**
   * Builds the member information for a given node out of the stored typeInfo
   *
   */
  @tailrec
  private def getMemberInformation(p: PNode): GobraMemberInfo = {
    val nodeTypeInfo =
      if (treeContains(typeInfo.tree, p)) {
        typeInfo
      } else {
        // Try to find the correct typeInfo for the member
        val typeInfoOption = typeInfo.context.getContexts
          .map(externalTypeInfo => externalTypeInfo.getTypeInfo)
          .find(typeInfo => treeContains(typeInfo.tree, p))
        typeInfoOption match {
          case Some(typeInfo) => typeInfo
          case None => Violation.violation("Couldn't find typeInfo for node " + p.formattedShort)
        }
      }

    val pkgName = nodeTypeInfo.pkgName.formatted

    // Check whether the program containing this node has the builtin tag
    val isBuiltIn = nodeTypeInfo.program(p).builtin

    def formatArgs(args: Vector[PParameter]) = "(" + args.map(f => f.typ.formattedShort).mkString(", ") + ")"

    p match {
      case p: PDomainFunction => GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isBuiltIn)
      case p: PFPredicateDecl => GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty, isBuiltIn)
      case p: PFunctionDecl   => GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = p.body.isEmpty, isBuiltIn)
      case p: PMPredicateDecl => GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty, isBuiltIn)
      case p: PMPredicateSig  => GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isBuiltIn)
      case p: PMethodSig      => GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = false, isBuiltIn)
      case p: PMethodDecl =>
        GobraMemberInfo(pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), p.spec.isTrusted, p.body.isEmpty , isBuiltIn)
      case p: PMethodImplementationProof =>
        GobraMemberInfo(pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty, isBuiltIn)
      case p: PPredConstructor =>
        // TODO does this make sense?
        GobraMemberInfo(pkgName, p.id.id.name, "(" + p.args.filter(e => e.isDefined).map(e => e.get.formattedShort).mkString(", ") + ")", isTrusted = false, isAbstract = false, isBuiltIn)
      // Fallback to the node's code root if we can't match the node
      case p: PNode => getMemberInformation(nodeTypeInfo.codeRoot(p))
    }
  }

  /**
   * Checks whether a given Go Tree contains a given node
   */
  def treeContains(tree: Info.GoTree, p: PNode): Boolean = {
    try {
      tree.whenContains(p, true)
    } catch {
      case NodeNotInTreeException(_) => false
    }
  }

  /**
   * Returns a set of viper member names (Methods, Predicates & Functions) which are accessed by a given viper member
   */
  def getDependencies(m: Member): Set[String] = m match {
    case m: Method => (m.pres ++ m.posts ++ m.body.toSeq).flatMap(getMemberCalls).toSet
    case p: Predicate => p.body.toSeq.flatMap(getMemberCalls).toSet
    case f: Function => (f.pres ++ f.posts ++ f.body.toSeq).flatMap(getMemberCalls).toSet
  }

  /**
   * Collects all method calls, function applications and predicate accesses inside a viper node and returns a list containing
   * all names of the members accessed this way
   */
  def getMemberCalls(n: Node): Seq[String] = n.deepCollect {
    case method_call: MethodCall => method_call.methodName
    case func_app: FuncApp => func_app.funcname
    case pred_access: PredicateAccess => pred_access.predicateName
  }
}
