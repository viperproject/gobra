// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import org.apache.commons.io.FileUtils
import org.bitbucket.inkytonik.kiama.relation.NodeNotInTreeException
import viper.gobra.ast.frontend.{PDomainFunction, PExpression, PFPredicateDecl, PFunctionDecl, PMPredicateDecl, PMPredicateSig, PMethodDecl, PMethodImplementationProof, PMethodSig, PNode, PParameter, PPredConstructor}
import viper.gobra.ast.internal.BuiltInMember
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.util.Violation
import viper.silver.ast.{FuncApp, Function, Member, Method, MethodCall, Node, Predicate, PredicateAccess}
import viper.silver.reporter.Time

import java.nio.charset.StandardCharsets.UTF_8
import scala.annotation.tailrec
import java.io.File

case class StatsCollector(reporter: GobraReporter) extends GobraReporter {
  // Stores type info for a task
  var typeInfos: Map[String, TypeInfo] = Map()
  // Maps a gobra member name to a gobra member entry
  private var memberMap: Map[String, GobraMemberEntry] = Map()
  // Maps a viper member name to a gobra member entry
  private var viperMemberNameGobraMemberMap: Map[String, GobraMemberEntry] = Map()

  case class GobraMemberEntry(pkgDir: String,
                              pkg: String,
                              memberName: String,
                              args: String,
                              var viperMembers: List[ViperMemberEntry],
                              isTrusted: Boolean,
                              var isAbstract: Boolean) {
    override def toString: String = pkg + "." + memberName + args + " trusted: " + isTrusted + " abstract: " + isAbstract + " builtin: "
  }
  case class ViperMemberEntry(member: Member, time: Time, success: Boolean, cached: Boolean)
  case class GobraMemberInfo(pkgDir: String,
                             pkg: String,
                             memberName: String,
                             args: String,
                             isTrusted: Boolean,
                             isAbstract: Boolean,
                             isImported: Boolean,
                             isBuiltIn: Boolean
                            )

  override val name: String = "StatsCollector"

  override def report(msg: GobraMessage): Unit = {
    msg match {
        // Capture typeInfo once it's available
      case TypeInfoMessage(typeInfo, taskName) => this.synchronized({ typeInfos = typeInfos + (taskName -> typeInfo)})
      case VerificationTaskFinishedMessage(taskName) => this.synchronized({ typeInfos = typeInfos - taskName})
      case GobraEntitySuccessMessage(taskName, _, e, info, time, cached) if !info.node.isInstanceOf[BuiltInMember] =>
        Violation.violation(typeInfos.contains(taskName), "No type info available for stats reporter")
        getMemberInformation(info.pnode, typeInfos(taskName)) match {
          case GobraMemberInfo(pkgDir, pkg, memberName, args, isTrusted, isAbstract, isImported, false)  =>
            addResult(
              pkgDir,
              pkg,
              memberName,
              args,
              ViperMemberEntry(e, time, success = true, cached = cached),
              isTrusted,
              isAbstract,
              isImported)
          case _ =>
        }
      case GobraEntityFailureMessage(taskName, _, e, info, _, time, cached) if !info.node.isInstanceOf[BuiltInMember] =>
        Violation.violation(typeInfos.contains(taskName), "No type info available for stats reporter")
        getMemberInformation(info.pnode, typeInfos(taskName)) match {
          case GobraMemberInfo(pkgDir, pkg, memberName, args, isTrusted, isAbstract, isImported, false) =>
            addResult(
              pkgDir,
              pkg,
              memberName,
              args,
              ViperMemberEntry(e, time, success = false, cached = cached),
              isTrusted,
              isAbstract,
              isImported)
          case _ =>
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
    "pkgDir": "${value.pkgDir}",
    "name": "${value.pkg}.${value.memberName}${value.args}",
    "trusted": ${value.isTrusted},
    "abstract": ${value.isAbstract},
    "viper_members": [ """ + "\n" +
          value.viperMembers.map(entry => s"""      {
        "name": "${entry.member.name}",
        "time": ${entry.time},
        "success": ${entry.success},
        "cached": ${entry.cached}
      }""").mkString(", \n") + "\n    ],\n" + s"""    "dependencies": [""" + "\n" +
        value.viperMembers
          .flatMap(entry => getDependencies(entry.member))
          .flatMap(dep => viperMemberNameGobraMemberMap.get(viperMemberKey(value.pkgDir, value.pkg, dep)))
          .map(entry => "        \"" + gobraMemberKey(entry.pkgDir, entry.pkg, entry.memberName, entry.args) + "\"")
          .toSet
          .mkString(", \n") + "\n    ]\n  }"
      }).mkString(", \n") + "\n]\n"

    if(shorten) {
      // Replaces all spaces, except the ones inside of quotes
      json.replaceAll("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", "")
    } else {
      json
    }
  }

  def getWarnings(pkgDir: String, pkg: String): Set[String] = {
    var warnings: List[String] = List()
    memberMap.values.filter(value => value.pkgDir == pkgDir && value.pkg == pkg).foreach(entry => {
      entry.viperMembers.foreach(v => {
        val name = entry.pkg + "." + entry.memberName + entry.args

        // Check if any viper dependencies correspond to a trusted or abstract, non-builtin gobra member
        getDependencies(v.member)
          .foreach(dep => viperMemberNameGobraMemberMap.get(viperMemberKey(pkgDir, pkg, dep)) match {
          // Trusted implies abstracted, so we match trusted first
          case Some(GobraMemberEntry(_, pkg, memberName, args, _, true, _)) =>
            warnings = warnings.appended("Warning: Member " + name + " depends on trusted member " + pkg + "." + memberName + args + "\n")
          case Some(GobraMemberEntry(_, pkg, memberName, args, _, _, true)) =>
            warnings = warnings.appended("Warning: Member " + name + " depends on abstract member " + pkg + "." + memberName + args + "\n")
          case _ =>
        })
      })
    })
    warnings.toSet
  }

  def addResult(pkgDir: String,
                pkg: String,
                memberName: String,
                args: String,
                entry: ViperMemberEntry,
                isTrusted: Boolean,
                isAbstract: Boolean,
                isImported: Boolean): Unit = this.synchronized({
    val key = gobraMemberKey(pkgDir, pkg, memberName, args)

    memberMap.get(key) match {
      case Some(existing) =>
        // Don't save viperMembers of abstract or trusted methods, since they give us little information
        if(!isImported) {
          existing.viperMembers = existing.viperMembers.appended(entry)
        }
        existing.isAbstract = existing.isAbstract || isAbstract
        Violation.violation(existing.isTrusted == isTrusted, "Same members with different trusted declarations found: \n " + key)
      case None => memberMap = memberMap + (key -> GobraMemberEntry(pkgDir, pkg, memberName, args, List(entry), isTrusted, isAbstract))
    }

    val viperKey = viperMemberKey(pkgDir, pkg, entry.member.name)

    viperMemberNameGobraMemberMap.get(viperKey) match {
      // Viper methods should only correspond to a single Gobra method.
      case Some(_) =>
        Violation.violation(viperMemberNameGobraMemberMap(viperKey).equals(memberMap(key)),
          "Viper method corresponds to multiple gobra methods: " + viperKey + " -> " + key)
      case None => viperMemberNameGobraMemberMap = viperMemberNameGobraMemberMap + (viperKey -> memberMap(key))
    }
  })

  def gobraMemberKey(pkgDir: String, pkg: String, memberName: String, args: String): String = pkgDir + "-" + pkg + "." + memberName + args

  def viperMemberKey(pkgDir: String, pkg: String, viperMemberName: String): String = pkgDir + "-" + pkg + "-" + viperMemberName

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

    val pkgName = nodeTypeInfo.tree.originalRoot.packageClause.id.name
    val pkgDir = nodeTypeInfo.tree.originalRoot.path.toString

    def formatArgs(args: Vector[PParameter]): String =
      ("(" + args.map(f => f.typ.formattedShort).mkString(", ") + ")").replaceAll("\\s+", " ")

    def formatPredConstructorArgs(args: Vector[Option[PExpression]]): String =
      ("(" + args.filter(e => e.isDefined).map(e => e.get.formattedShort).mkString(", ") + ")").replaceAll("\\s+", " ")

    // Check whether the program containing this node has the builtin tag
    val isBuiltIn = nodeTypeInfo.program(p).isBuiltin
    // Check if a node comes from a import, used to declare it non abstract, since imports are per default always
    // abstract and we don't want to generate unnecessary warnings
    val isImported = !typeInfo.eq(nodeTypeInfo)

    p match {
      case p: PDomainFunction =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isImported, isBuiltIn)
      case p: PFPredicateDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PFunctionDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMPredicateDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMPredicateSig =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isImported, isBuiltIn)
      case p: PMethodSig =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = false, isImported, isBuiltIn)
      case p: PMethodDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), p.spec.isTrusted, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodImplementationProof =>
        GobraMemberInfo(pkgDir, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PPredConstructor =>
        GobraMemberInfo(pkgDir, pkgName, p.id.id.name, formatPredConstructorArgs(p.args), isTrusted = false, isAbstract = false, isImported, isBuiltIn)
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
    case _ => Set()
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
