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
import viper.gobra.frontend.{Config, PackageEntry}
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
                              nodeType: String,
                              args: String,
                              var viperMembers: List[ViperMemberEntry],
                              isTrusted: Boolean,
                              var isAbstract: Boolean)

  case class ViperMemberEntry(member: Member,
                              taskName: String,
                              time: Time,
                              nodeType: String,
                              success: Boolean,
                              cached: Boolean,
                              fromImport: Boolean,
                              hasBody: Boolean)

  case class GobraMemberInfo(pkgDir: String,
                             pkg: String,
                             memberName: String,
                             args: String,
                             isTrusted: Boolean,
                             isAbstract: Boolean,
                             isImported: Boolean,
                             isBuiltIn: Boolean)

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
              info.pnode.getClass.getSimpleName,
              args,
              ViperMemberEntry(e, taskName, time, e.getClass.getSimpleName, success = true, cached = cached, isImported, hasBody = viperMemberHasBody(e)),
              isTrusted,
              isAbstract)
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
              info.pnode.getClass.getSimpleName,
              args,
              ViperMemberEntry(e, taskName, time, e.getClass.getSimpleName, success = false, cached = cached, isImported, hasBody = viperMemberHasBody(e)),
              isTrusted,
              isAbstract)
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
      memberMap.map({ case (key, gobraMember) => s"""  {
    "id": "$key",
    "pkgDir": "${gobraMember.pkgDir}",
    "pkg": "${gobraMember.pkg}",
    "name": "${gobraMember.pkg}.${gobraMember.memberName}",
    "args": "${gobraMember.args}",
    "nodeType": "${gobraMember.nodeType}",
    "trusted": ${gobraMember.isTrusted},
    "abstract": ${gobraMember.isAbstract},
    "viperMembers": [ """ + "\n" +
          gobraMember.viperMembers.map(viperMember => s"""      {
        "name": "${viperMemberName(viperMember.member)}",
        "taskName": "${viperMember.taskName}",
        "nodeType": "${viperMember.nodeType}",
        "time": ${viperMember.time},
        "success": ${viperMember.success},
        "fromImport": ${viperMember.fromImport},
        "hasBody": ${viperMember.hasBody},
        "cached": ${viperMember.cached}
      }""").mkString(", \n") + "\n    ],\n" + s"""    "dependencies": [""" + "\n" +
        gobraMember.viperMembers
          .flatMap(viperMember => getDependencies(viperMember.member)
            .flatMap(dep => findGobraMemberByViperMemberName(viperMember.taskName, dep, gobraMember.pkgDir))
          )
          // Filter out dependencies on one self, since this information isn't very useful
          .filter(_ == gobraMember)
          .map(gobraMemberEntry => "        \"" + gobraMemberKey(gobraMemberEntry.pkgDir, gobraMemberEntry.pkg, gobraMemberEntry.memberName, gobraMemberEntry.args) + "\"")
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

  def findGobraMemberByViperMemberName(taskName: String, viperMemberName: String, pkgDir: String): Option[GobraMemberEntry] =
    viperMemberNameGobraMemberMap.get(viperMemberKey(taskName, viperMemberName, pkgDir))
      .orElse(viperMemberNameGobraMemberMap.get(viperMemberKey(taskName, viperMemberName)))

  def getWarnings(pkgDir: String, pkg: String, config: Config): Set[String] =
    memberMap.values
      .filter(gobraMember => gobraMember.pkgDir == pkgDir && gobraMember.pkg == pkg)
      .flatMap(gobraMember =>
        gobraMember.viperMembers.flatMap(viperMember => {
            val name = gobraMember.pkg + "." + gobraMember.memberName + gobraMember.args
            // Check if any viper dependencies correspond to a trusted or abstract, non-builtin gobra member
            getDependencies(viperMember.member)
              .flatMap(dep => findGobraMemberByViperMemberName(viperMember.taskName, dep, pkgDir))
              .flatMap({
                // Trusted implies abstracted, so we match trusted first
                case GobraMemberEntry(_, pkg, memberName, _, args, _, true, _) =>
                  Some("Warning: Member " + name + " depends on trusted member " + pkg + "." + memberName + args + "\n")
                case GobraMemberEntry(_, pkg, memberName, _, args, _, _, true) =>
                  Some("Warning: Member " + name + " depends on abstract member " + pkg + "." + memberName + args + "\n")
                case GobraMemberEntry(pkgDir, pkg, _, _, _, _, _, _)  if !config.inputPackageMap.contains(PackageEntry(pkgDir, pkg)) =>
                  Some("Warning: Depending on imported package, that is not verified: " + pkgDir + " - " + pkg)
                case _ => None
              })
        })
      ).toSet

  def addResult(pkgDir: String,
                pkg: String,
                memberName: String,
                nodeType: String,
                args: String,
                viperMember: ViperMemberEntry,
                isTrusted: Boolean,
                isAbstract: Boolean): Unit = this.synchronized({
    val key = gobraMemberKey(pkgDir, pkg, memberName, args)

    memberMap.get(key) match {
      case Some(existing) =>
        existing.viperMembers = existing.viperMembers.appended(viperMember)
        // If we encounter an abstract version of a member, we know for sure, its abstract
        existing.isAbstract = existing.isAbstract || isAbstract
        Violation.violation(existing.isTrusted == isTrusted, "Same members with different trusted declarations found: \n " + key)
      case None => memberMap = memberMap + (key -> GobraMemberEntry(pkgDir, pkg, memberName, nodeType, args, List(viperMember), isTrusted, isAbstract))
    }

    val viperKey = viperMemberKey(viperMember.taskName, viperMemberName(viperMember.member))

    // (JG 03.02.2022) Currently there can exist two viper members with the exact same name and the same arguments.
    // This happens if we are in a package that contains this said method and import another package that contains a
    // method with the same name. As a workaround for this, I add the pkgDir to the second ViperMember that appears
    // and first look for the viper member with the pkgDir when trying to lookup dependencies of a gobra member, since
    // these would be the more relevant ones.
    viperMemberNameGobraMemberMap.get(viperKey) match {
      case Some(value) if !value.eq(memberMap(key)) =>
        val fallBackKey = viperMemberKey(viperMember.taskName, viperMemberName(viperMember.member), pkgDir)
        viperMemberNameGobraMemberMap.get(fallBackKey) match {
          case Some(gobraMember) if !gobraMember.eq(memberMap(key)) =>
            val gobraMemberName = gobraMember.pkgDir + " - " + gobraMember.pkg + "." + gobraMember.memberName + gobraMember.args
            val otherGobraMember = memberMap(key)
            val otherGobraMemberName = otherGobraMember.pkgDir + " - " + otherGobraMember.pkg + "." + otherGobraMember.memberName + otherGobraMember.args
            Violation.violation("Viper method corresponds to multiple gobra methods: " + viperKey + ":\n " + gobraMemberName + " \n" + otherGobraMemberName)
          case None => viperMemberNameGobraMemberMap = viperMemberNameGobraMemberMap + (fallBackKey -> memberMap(key))
          case _ =>
        }
      case None => viperMemberNameGobraMemberMap = viperMemberNameGobraMemberMap + (viperKey -> memberMap(key))
      case _ =>
    }
  })

  def gobraMemberKey(pkgDir: String, pkg: String, memberName: String, args: String): String = pkgDir + "-" + pkg + "." + memberName + args

  def viperMemberKey(taskName: String, viperMemberName: String, pkgDir: String = ""): String = taskName + "-" + viperMemberName + "-" + pkgDir

  def viperMemberHasBody(member: Member): Boolean = member match {
    case m: Method => m.body.isDefined && m.body.get.nonEmpty
    case p: Predicate => p.body.isDefined && p.body.get.nonEmpty
    case f: Function => f.body.isDefined && f.body.get.nonEmpty
  }

  /**
   * Gets the name of a viper member from some node. The member should be a call to a callable member or a callable member
   */
  def viperMemberName(member: Node): String = member match {
    case m: Method => m.name + "(" + m.formalArgs.map(arg => arg.typ).mkString(",") + ")"
    case p: Predicate => p.name + "(" + p.formalArgs.map(arg => arg.typ).mkString(",") + ")"
    case f: Function => f.name + "(" + f.formalArgs.map(arg => arg.typ).mkString(",") + ")"
    case mc: MethodCall => mc.methodName + "(" + mc.args.map(arg => arg.typ).mkString(",") + ")"
    case fa: FuncApp => fa.funcname + "(" + fa.args.map(arg => arg.typ).mkString(",") + ")"
    case pa: PredicateAccess => pa.predicateName + "(" + pa.args.map(arg => arg.typ).mkString(",") + ")"
  }

  /**
   * Builds the member information for a given node out of the stored typeInfo
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
    val isImported = !typeInfo.tree.originalRoot.eq(nodeTypeInfo.tree.originalRoot)

    p match {
      case p: PFunctionDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), p.spec.isTrusted, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodSig =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = p.spec.isTrusted, isAbstract = false, isImported, isBuiltIn)
      case p: PFPredicateDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMPredicateDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), isTrusted = false, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodImplementationProof =>
        GobraMemberInfo(pkgDir, pkgName, "Impl_Proof." + p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), isTrusted = false, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PDomainFunction =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isImported, isBuiltIn)
      case p: PMPredicateSig =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), isTrusted = false, isAbstract = false, isImported, isBuiltIn)
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
    case method_call: MethodCall => viperMemberName(method_call)
    case func_app: FuncApp => viperMemberName(func_app)
    case pred_access: PredicateAccess => viperMemberName(pred_access)
  }
}
