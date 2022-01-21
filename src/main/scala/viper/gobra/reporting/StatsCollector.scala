// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import org.apache.commons.io.FileUtils
import org.bitbucket.inkytonik.kiama.relation.NodeNotInTreeException
import viper.gobra.ast.frontend.{PDomainFunction, PFPredicateDecl, PFunctionDecl, PMPredicateDecl, PMPredicateSig, PMethodDecl, PMethodImplementationProof, PMethodSig, PNode, PParameter, PPredConstructor}
import viper.gobra.ast.internal.BuiltInMember
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.util.Violation
import viper.silver.ast.{FuncApp, Function, Member, Method, MethodCall, Node, Predicate, PredicateAccess}
import viper.silver.reporter.Time

import java.nio.charset.StandardCharsets.UTF_8
import scala.annotation.tailrec
import java.io.File

case class StatsCollector(reporter: GobraReporter) extends GobraReporter {
  var typeInfos: Map[String, TypeInfo] = Map()
  // Maps a gobra member name to a gobra member entry
  private var memberMap: Map[String, GobraMemberEntry] = Map()
  // Maps a viper member name to a gobra member entry
  private var viperMemberNameGobraMemberMap: Map[String, GobraMemberEntry] = Map()

  case class GobraMemberEntry(
                               taskName: String,
                               pkg: String,
                               memberName: String,
                               args: String,
                               var viperMembers: List[ViperMemberEntry],
                               isTrusted: Boolean,
                               isAbstract: Boolean,
                               isBuiltIn: Boolean
                             ) {
    override def toString: String = pkg + "." + memberName + args + " trusted: " + isTrusted + " abstract: " + isAbstract + " builtin: " + isBuiltIn
  }
  case class ViperMemberEntry(member: Member, time: Time, success: Boolean, cached: Boolean)
  case class GobraMemberInfo(pkg: String, memberName: String, args: String, isTrusted: Boolean, isAbstract: Boolean, isBuiltIn: Boolean)

  override val name: String = "StatsCollector"

  override def report(msg: GobraMessage): Unit = {
    msg match {
        // Ignore messages about BuildInMembers
      case GobraEntitySuccessMessage(_, _, _, info, _, _) if info.node.isInstanceOf[BuiltInMember] =>
      case GobraEntityFailureMessage(_, _, _, info, _, _, _) if info.node.isInstanceOf[BuiltInMember] =>
        // Capture typeInfo once it's available
      case TypeInfoMessage(typeInfo, taskName) => this.synchronized({ typeInfos = typeInfos + (taskName -> typeInfo)})
      case GobraEntitySuccessMessage(taskName, _, e, info, time, cached) =>
        Violation.violation(typeInfos.contains(taskName), "No type info available for stats reporter")
        getMemberInformation(info.pnode, typeInfos(taskName)) match {
          case GobraMemberInfo(pkg, memberName, args, isTrusted, isAbstract, isBuiltIn) =>
            addResult(taskName, pkg, memberName, args, ViperMemberEntry(e, time, success = true, cached = cached), isTrusted, isAbstract, isBuiltIn)
        }
      case GobraEntityFailureMessage(taskName, _, e, info, _, time, cached) =>
        Violation.violation(typeInfos.contains(taskName), "No type info available for stats reporter")
        getMemberInformation(info.pnode, typeInfos(taskName)) match {
          case GobraMemberInfo(pkg, memberName, args, isTrusted, isAbstract, isBuiltIn) =>
            addResult(taskName, pkg, memberName, args, ViperMemberEntry(e, time, success = false, cached = cached), isTrusted, isAbstract, isBuiltIn)
        }
      case _ =>
    }
    // Pass message to next reporter
    reporter.report(msg)
  }

  def writeJsonReportToFile(file: File): Unit = {
    if((file.exists() && file.canWrite) || file.getParentFile.canWrite) {
      FileUtils.writeStringToFile(file, getJsonReport(false), UTF_8)
    }
  }

  def getJsonReport(shorten: Boolean): String = {
    val json = "[\n" +
      memberMap.map({ case (key, value) => s"""  {
    "member_id": "$key",
    "task": "${value.taskName}",
    "name": "${value.pkg}.${value.memberName}${value.args}",
    "trusted": ${value.isTrusted},
    "abstract": ${value.isAbstract},
    "builtin": ${value.isBuiltIn},
    "viper_members": [ """ + "\n" +
          value.viperMembers.map(entry => s"""      {
        "name": "${entry.member.name}",
        "time": ${entry.time},
        "success": ${entry.success},
        "cached": ${entry.cached},
      }""").mkString(", \n") + "\n    ],\n" + s"""    "dependencies": [""" + "\n" +
        value.viperMembers
            .flatMap(entry => getDependencies(entry.member))
            .map(dep => viperMemberNameGobraMemberMap(value.taskName + "-" + dep))
            .map({ case GobraMemberEntry(_, pkg, memberName, args,_,_,_,_) => "        \"" + pkg + "." + memberName + args + "\""})
            .mkString(", \n") + "\n    ]\n  }"
      }).mkString(", \n") + "\n]\n"

    if(shorten) {
      json.replaceAll("\\s", "")
    } else {
      json
    }
  }

  def getWarnings(task: String): List[String] = {
    var warnings: List[String] = List()
    memberMap.keys.filter(_.startsWith(task)).foreach(g => {
      memberMap(g).viperMembers.foreach(v => {
        val name = memberMap(g).pkg + "." + memberMap(g).memberName + memberMap(g).args

        // Check if any viper dependencies correspond to a gobra method
        getDependencies(v.member).foreach(dep => viperMemberNameGobraMemberMap.get(memberMap(g).taskName + "-" + dep) match {
          // Trusted implies abstracted, so we match trusted first
          case Some(GobraMemberEntry(_, pkg, memberName, args, _, true, _, false)) => warnings = warnings.appended("Warning: Member " + name + " depends on trusted member " + pkg + "." + memberName + args + "\n")
          case Some(GobraMemberEntry(_, pkg, memberName, args, _, _, true, false)) => warnings = warnings.appended("Warning: Member " + name + " depends on abstract member " + pkg + "." + memberName + args + "\n")
          case _ =>
        })
      })
    })
    warnings
  }

  def addResult(taskName: String, pkg: String, memberName: String, args: String, entry: ViperMemberEntry, isTrusted: Boolean, isAbstract: Boolean, isBuiltIn: Boolean): Unit = this.synchronized({
    val key = taskName + " " + pkg + "." + memberName + args

    memberMap.get(key) match {
      case Some(existing) => existing.viperMembers = existing.viperMembers.appended(entry)
      case None => memberMap = memberMap + (key -> GobraMemberEntry(taskName, pkg, memberName, args, List(entry), isTrusted, isAbstract, isBuiltIn))
    }

    val viperKey = taskName + "-" + entry.member.name

    viperMemberNameGobraMemberMap.get(viperKey) match {
      // Viper methods should only correspond to a single Gobra method. If they somehow won't,
      case Some(_) =>
        Violation.violation(viperMemberNameGobraMemberMap(viperKey).equals(memberMap(key)), "Viper method corresponds to multiple gobra methods: " + viperKey + " -> " + key)
      case None => viperMemberNameGobraMemberMap = viperMemberNameGobraMemberMap + (viperKey -> memberMap(key))
    }
  })


  /**
   * Builds the member information for a given node out of the stored typeInfo
   *
   */
  @tailrec
  private def getMemberInformation(p: PNode, typeInfo: TypeInfo): GobraMemberInfo = {
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

    val isNotImported = typeInfo.eq(nodeTypeInfo)

    p match {
      case p: PDomainFunction =>
        GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isBuiltIn)
      case p: PFPredicateDecl =>
        GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty && isNotImported, isBuiltIn)
      case p: PFunctionDecl =>
        GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = p.body.isEmpty && isNotImported, isBuiltIn)
      case p: PMPredicateDecl =>
        GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty && isNotImported, isBuiltIn)
      case p: PMPredicateSig =>
        GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isBuiltIn)
      case p: PMethodSig =>
        GobraMemberInfo(pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = false, isBuiltIn)
      case p: PMethodDecl =>
        GobraMemberInfo(pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), p.spec.isTrusted, p.body.isEmpty && isNotImported, isBuiltIn)
      case p: PMethodImplementationProof =>
        GobraMemberInfo(pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty && isNotImported, isBuiltIn)
      case p: PPredConstructor =>
        // TODO does this make sense?
        GobraMemberInfo(pkgName, p.id.id.name, "(" + p.args.filter(e => e.isDefined).map(e => e.get.formattedShort).mkString(", ") + ")", isTrusted = false, isAbstract = false, isBuiltIn)
      // Fallback to the node's code root if we can't match the node
      case p: PNode => getMemberInformation(nodeTypeInfo.codeRoot(p), typeInfo)
    }
  }

  /**
   * Checks whether a Go Tree contains a node
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
