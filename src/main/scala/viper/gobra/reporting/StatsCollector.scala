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

import scala.collection.concurrent.{Map, TrieMap}
import java.nio.charset.StandardCharsets.UTF_8
import java.io.File

object GobraNodeType extends Enumeration {
  type GobraNodeType = Value
  val MethodDeclaration, FunctionDeclaration, MethodSignature, FunctionPredicateDeclaration, MethodPredicateDeclaration,
  MethodImplementationProof, MethodPredicateSignature, DomainFunction, PredicateConstructor = Value
}

object ViperNodeType extends Enumeration {
  type ViperNodeType = Value
  val Function, Predicate, Method = Value
}

case class StatsCollector(reporter: GobraReporter) extends GobraReporter {
  import GobraNodeType._
  import ViperNodeType._

  // Stores type info for a task
  private[reporting] val typeInfos: Map[String, TypeInfo] = TrieMap()
  // Maps a gobra member name to a gobra member entry
  private[reporting] val memberMap: Map[String, GobraMemberEntry] = TrieMap()
  // Maps a viper member name to a gobra member entry
  private val viperMemberNameGobraMemberMap: Map[String, GobraMemberEntry] = TrieMap()

  // indentation prefix for generated json
  private val i = "  "
  case class GobraMemberEntry(pkgDir: String,
                              pkg: String,
                              memberName: String,
                              args: String,
                              nodeType: GobraNodeType,
                              viperMembers: Map[String, ViperMemberEntry],
                              isTrusted: Boolean,
                              isAbstract: Boolean) {

    def asJson(p: String = ""): String = {
      val viperMembersJson = viperMembers.values.map(_.asJson(s"$p$i$i"))
      val dependencies = this.dependencies().map(entry => s"""$p$i$i"${entry.key}"""")

      s"""$p{
         |$p$i"id": "${this.key}",
         |$p$i"pkgDir": "$pkgDir",
         |$p$i"pkg": "$pkg",
         |$p$i"name": "$pkg.$memberName",
         |$p$i"args": "$args",
         |$p$i"nodeType": "$nodeType",
         |$p$i"trusted": $isTrusted,
         |$p$i"abstract": $isAbstract,
         |$p$i"viperMembers": [
            |${viperMembersJson.mkString(",\n")}
         |$p$i],
         |$p$i"dependencies": [
            |${dependencies.mkString(",\n")}
         |$p$i]
         |$p}""".stripMargin
    }

    def dependencies(): Set[GobraMemberEntry] = {
      this.viperMembers.values
        .flatMap(viperMember => viperMember.dependencies
          .flatMap(dep => findGobraMemberByViperMemberName(viperMember.taskName, dep, pkgDir)))
        .filter(_.key != this.key).toSet
    }

    def key: String = gobraMemberKey(pkgDir, pkg, memberName, args)
  }

  case class ViperMemberEntry(memberName: String,
                              taskName: String,
                              time: Time,
                              nodeType: ViperNodeType,
                              dependencies: Set[String],
                              success: Boolean,
                              cached: Boolean,
                              fromImport: Boolean,
                              hasBody: Boolean) {

    def asJson(p: String = ""): String = {
      s"""$p{
         |$p$i"name": "${this.memberName}",
         |$p$i"taskName": "${this.taskName}",
         |$p$i"time": ${this.time},
         |$p$i"nodeType": "${this.nodeType}",
         |$p$i"success": ${this.success},
         |$p$i"cached": ${this.cached},
         |$p$i"fromImport": ${this.fromImport},
         |$p$i"hasBody": ${this.hasBody}
         |$p}""".stripMargin
    }
  }

  case class GobraMemberInfo(pkgDir: String,
                             pkg: String,
                             memberName: String,
                             args: String,
                             nodeType: GobraNodeType,
                             isTrusted: Boolean,
                             isAbstract: Boolean,
                             isImported: Boolean,
                             isBuiltIn: Boolean)

  override val name: String = "StatsCollector"

  override def report(msg: GobraMessage): Unit = {
    def handleEntityMessage(taskName: String, viperMember: Member, info: Source.Verifier.Info, time: Time, cached: Boolean, success: Boolean): Unit = {
      Violation.violation(typeInfos.contains(taskName), "No type info available for stats reporter")
      getMemberInformation(info.pnode, typeInfos(taskName)) match {
        case GobraMemberInfo(pkgDir, pkg, memberName, args, nodeType, isTrusted, isAbstract, isImported, false) =>
          addResult(
            pkgDir,
            pkg,
            memberName,
            args,
            nodeType,
            ViperMemberEntry(
              viperMemberName(viperMember),
              taskName,
              time,
              ViperNodeType.withName(viperMember.getClass.getSimpleName),
              getViperDependencies(viperMember),
              success,
              cached,
              isImported,
              viperMemberHasBody(viperMember)
            ),
            isTrusted,
            isAbstract)
        case _ =>
      }
    }

    msg match {
      // Capture typeInfo once it's available
      case TypeInfoMessage(typeInfo, taskName) => this.synchronized({typeInfos.put(taskName, typeInfo)})
      // Free up unneeded space, once a task is finished
      case VerificationTaskFinishedMessage(taskName) => typeInfos.remove(taskName)
      case GobraEntitySuccessMessage(taskName, _, e, info, time, cached) if !info.node.isInstanceOf[BuiltInMember] =>
        handleEntityMessage(taskName, e, info, time, cached, success = true)
      case GobraEntityFailureMessage(taskName, _, e, info, _, time, cached) if !info.node.isInstanceOf[BuiltInMember] =>
        handleEntityMessage(taskName, e, info, time, cached, success = false)
      case _ =>
    }
    // Pass message to next reporter
    reporter.report(msg)
  }

  def getNumberOfCachedViperMembers: Int = memberMap.values.flatMap(_.viperMembers.values).count(_.cached)

  def getNumberOfCacheableViperMembers: Int = memberMap.values.flatMap(_.viperMembers.values).count(member => member.hasBody && member.nodeType == "Method")

  def writeJsonReportToFile(file: File): Unit = {
    if((file.exists() && file.canWrite) || file.getParentFile.canWrite) {
      FileUtils.writeStringToFile(file, getJsonReport, UTF_8)
    }
  }

  def getJsonReport: String = {
    val memberJson = memberMap.values.map(_.asJson(i)).mkString(", \n")
    val json = s"""[
      |$memberJson
      |]""".stripMargin
    json
  }

  def findGobraMemberByViperMemberName(taskName: String, viperMemberName: String, pkgDir: String): Option[GobraMemberEntry] =
    viperMemberNameGobraMemberMap.get(viperMemberKey(taskName, viperMemberName, pkgDir))
      .orElse(viperMemberNameGobraMemberMap.get(viperMemberKey(taskName, viperMemberName)))

  def getWarnings(pkgDir: String, pkg: String, config: Config): Set[String] =
    memberMap.values
      .filter(gobraMember => gobraMember.pkgDir == pkgDir && gobraMember.pkg == pkg)
      .flatMap(gobraMember => {
        val name = gobraMember.pkg + "." + gobraMember.memberName + gobraMember.args
        gobraMember.dependencies().flatMap({
          // Trusted implies abstracted, so we match trusted first
          case GobraMemberEntry(_, pkg, memberName, _, args, _, true, _) =>
            Some("Warning: Member " + name + " depends on trusted member " + pkg + "." + memberName + args + "\n")
          case GobraMemberEntry(_, pkg, memberName, _, args, _, _, true) =>
            Some("Warning: Member " + name + " depends on abstract member " + pkg + "." + memberName + args + "\n")
          case GobraMemberEntry(pkgDir, pkg, _, _, _, _, _, _) if !config.inputPackageMap.contains(PackageEntry(pkgDir, pkg)) =>
            Some("Warning: Depending on imported package that is not verified: " + pkgDir + " - " + pkg)
          case _ => None
        })
      }).toSet

  def addResult(pkgDir: String,
                pkg: String,
                memberName: String,
                args: String,
                nodeType: GobraNodeType,
                viperMember: ViperMemberEntry,
                isTrusted: Boolean,
                isAbstract: Boolean): Unit = {
    val key = gobraMemberKey(pkgDir, pkg, memberName, args)

    val viperKey = viperMemberKey(viperMember.taskName, viperMember.memberName, pkgDir)
    val shortViperKey = viperMemberKey(viperMember.taskName, viperMember.memberName)

    memberMap.get(key) match {
      case Some(existing) =>
        existing.viperMembers.get(viperKey).orElse(existing.viperMembers.get(shortViperKey)) match {
          case Some(existingViperEntry) =>
            // Merge Viper members that occur multiple times during a task(used for chopper)
            existing.viperMembers.put(viperKey, ViperMemberEntry(
              existingViperEntry.memberName,
              existingViperEntry.taskName,
              existingViperEntry.time + viperMember.time,
              existingViperEntry.nodeType,
              existingViperEntry.dependencies ++ viperMember.dependencies,
              existingViperEntry.success && viperMember.success,
              existingViperEntry.cached || viperMember.cached,
              existingViperEntry.fromImport,
              existingViperEntry.hasBody || viperMember.hasBody
            ))
          case None => existing.viperMembers.put(viperKey, viperMember)
        }

        // If we encounter an abstract version of a member, we know for sure, its abstract
        val newAbstract = existing.isAbstract || isAbstract
        Violation.violation(existing.isTrusted == isTrusted, "Same members with different trusted declarations found: \n " + key)
        Violation.violation(existing.nodeType == nodeType, "Same members with different node types found: \n " + key)
        memberMap.put(key, existing.copy(isAbstract = newAbstract))

      case None => memberMap.put(key, GobraMemberEntry(pkgDir, pkg, memberName, args, nodeType, TrieMap(viperKey -> viperMember), isTrusted, isAbstract))
    }

    // TODO once issue #417 is fixed this hack won't be needed anymore
    // (JG 03.02.2022) Currently there can exist two viper members with the exact same name and the same arguments.
    // This happens if we are in a package that contains this said method and import another package that contains a
    // method with the same name. As a workaround for this, I add the pkgDir to the second ViperMember that appears
    // and first look for the viper member with the pkgDir when trying to lookup dependencies of a gobra member, since
    // these would be the more relevant ones.
    viperMemberNameGobraMemberMap.get(viperKey) match {
      case Some(otherMember) if !otherMember.key.eq(key) =>
        val fallBackKey = viperMemberKey(viperMember.taskName, viperMember.memberName, pkgDir)
        viperMemberNameGobraMemberMap.get(fallBackKey) match {
          case Some(otherMember) if otherMember.key != key =>
            Violation.violation("Viper method corresponds to multiple gobra methods: " + viperKey + ":\n " + otherMember.key + " \n" + key)
          case None => viperMemberNameGobraMemberMap.put(fallBackKey, memberMap(key))
          case _ =>
        }
      case None => viperMemberNameGobraMemberMap.put(viperKey, memberMap(key))
      case _ =>
    }
  }

  def gobraMemberKey(pkgDir: String, pkg: String, memberName: String, args: String): String = pkgDir + "-" + pkg + "." + memberName + args

  def viperMemberKey(taskName: String, viperMemberName: String, pkgDir: String = ""): String = taskName + "-" + viperMemberName + "-" + pkgDir

  def viperMemberHasBody(member: Member): Boolean = member match {
    case m: Method => m.body.isDefined && m.body.get.nonEmpty
    case p: Predicate => p.body.isDefined && p.body.get.nonEmpty
    case f: Function => f.body.isDefined && f.body.get.nonEmpty
    case _ => false
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
  def getMemberInformation(p: PNode, typeInfo: TypeInfo): GobraMemberInfo = {
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
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), FunctionDeclaration, isTrusted = p.spec.isTrusted, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), MethodDeclaration, p.spec.isTrusted, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodSig =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), MethodSignature, isTrusted = p.spec.isTrusted, isAbstract = false, isImported, isBuiltIn)
      case p: PFPredicateDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), FunctionPredicateDeclaration, isTrusted = false, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMPredicateDecl =>
        GobraMemberInfo(pkgDir, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), MethodPredicateDeclaration, isTrusted = false, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PMethodImplementationProof =>
        GobraMemberInfo(pkgDir, pkgName, "Impl_Proof." + p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), MethodImplementationProof, isTrusted = false, p.body.isEmpty && !isImported, isImported, isBuiltIn)
      case p: PDomainFunction =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), DomainFunction, isTrusted = false, isAbstract = false, isImported, isBuiltIn)
      case p: PMPredicateSig =>
        GobraMemberInfo(pkgDir, pkgName, p.id.name, formatArgs(p.args), MethodPredicateSignature, isTrusted = false, isAbstract = false, isImported, isBuiltIn)
      case p: PPredConstructor =>
        GobraMemberInfo(pkgDir, pkgName, p.id.id.name, formatPredConstructorArgs(p.args), PredicateConstructor, isTrusted = false, isAbstract = false, isImported, isBuiltIn)
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
  def getViperDependencies(m: Member): Set[String] = m match {
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
