// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import org.apache.commons.io.FileUtils
import org.bitbucket.inkytonik.kiama.relation.NodeNotInTreeException
import viper.gobra.ast.frontend.{PClosureDecl, PDomainType, PExpression, PFPredicateDecl, PFunctionDecl, PFunctionSpec, PMPredicateDecl, PMPredicateSig, PMethodDecl, PMethodImplementationProof, PMethodSig, PNode, PPackage, PParameter, PPredConstructor, PProgram}
import viper.gobra.ast.internal.BuiltInMember
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.util.Violation
import viper.silver.ast.{Function, Member, Method, Predicate}
import viper.silver.ast.utility.chopper.{Edges, Vertices}
import viper.silver.ast.utility.chopper.Vertices.Vertex
import viper.silver.reporter.Time

import scala.collection.concurrent.{Map, TrieMap}
import java.nio.charset.StandardCharsets.UTF_8
import java.io.File

/**
 * Enum used to identify the type of a Gobra node, used when exporting the statistics
 */
object GobraNodeType extends Enumeration {
  type GobraNodeType = Value
  val MethodDeclaration, FunctionDeclaration, MethodSignature, FunctionPredicateDeclaration, MethodPredicateDeclaration,
  MethodImplementationProof, MethodPredicateSignature, PredicateConstructor, Package, Program = Value
}

/**
 * Enum used to identify the type of a Viper node, used when exporting the statistics
 */
object ViperNodeType extends Enumeration {
  type ViperNodeType = Value
  val Function, Predicate, Method, Domain = Value
}

/**
 * Collects statistics for Gobra from received GobraMessages.
 * We won't store AST nodes, since to prevent memory overflows, they have to be garbage collected at some point
 *
 * @param reporter nested reporter, all messages received get forwarded to this reporter
 */
case class StatsCollector(reporter: GobraReporter) extends GobraReporter {
  import GobraNodeType._
  import ViperNodeType._

  type Warning = String

  // We use concurrent maps, because silicon verifies & reports results concurrently. Additionally we want to be prepared
  // if the type checking or other processes are done concurrently in the future
  // Stores type info for a task
  private[reporting] val typeInfos: Map[String, TypeInfo] = TrieMap()
  // Maps a gobra member name to a gobra member entry
  private[reporting] val memberMap: Map[String, GobraMemberEntry] = TrieMap()
  // Maps a viper member name to a gobra member entry
  private val viperMemberNameGobraMemberMap: Map[String, GobraMemberEntry] = TrieMap()

  // indentation prefix for generated json
  private val i = "  "
  case class GobraMemberEntry(info: GobraMemberInfo, viperMembers: Map[String, ViperMemberEntry]) {
    def asJson(p: String = ""): String = {
      val viperMembersJson = viperMembers.values.map(_.asJson(s"$p$i$i"))
      val dependencies = this.dependencies().map(entry => s"""$p$i$i"${entry.info.id}"""")

      s"""$p{
         |$p$i"id": "${this.info.id}",
         |$p$i"pkgId": "${info.pkgId}",
         |$p$i"pkg": "${info.pkg}",
         |$p$i"name": "${info.pkg}.${info.memberName}",
         |$p$i"args": "${info.args}",
         |$p$i"nodeType": "${info.nodeType}",
         |$p$i"trusted": ${info.isTrusted},
         |$p$i"abstract": ${info.isAbstractAndNotImported},
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
          .flatMap(dep => viperMemberNameGobraMemberMap.get(viperMemberKey(viperMember.taskName, dep)))
        )
        .filter(_.info.id != this.info.id).toSet
    }

  }

  /**
   * We don't store AST nodes to prevent memory overflows, since they otherwise would not be garbage collected
   */
  case class ViperMemberEntry(memberName: String,
                              taskName: String,
                              time: Time,
                              nodeType: ViperNodeType,
                              dependencies: Set[String],
                              success: Boolean,
                              cached: Boolean,
                              fromImport: Boolean,
                              hasBody: Boolean,
                              verified: Boolean) {

    val id: String = viperMemberKey(taskName, memberName)

    def asJson(p: String = ""): String = {
      s"""$p{
         |$p$i"name": "${this.memberName}",
         |$p$i"taskName": "${this.taskName}",
         |$p$i"time": ${this.time},
         |$p$i"nodeType": "${this.nodeType}",
         |$p$i"success": ${this.success},
         |$p$i"cached": ${this.cached},
         |$p$i"fromImport": ${this.fromImport},
         |$p$i"hasBody": ${this.hasBody},
         |$p$i"verified": ${this.verified}
         |$p}""".stripMargin
    }
  }

  /**
   * We don't store AST nodes to prevent memory overflows, since they otherwise would not be garbage collected
   */
  case class GobraMemberInfo(pkgId: String,
                             pkg: String,
                             memberName: String,
                             args: String,
                             nodeType: GobraNodeType,
                             hasSpecification: Boolean,
                             isTrusted: Boolean,
                             isAbstractAndNotImported: Boolean,
                             isImported: Boolean,
                             isBuiltIn: Boolean) {
    val id: String = gobraMemberKey(pkgId, memberName, args)
  }

  override val name: String = "StatsCollector"

  override def report(msg: GobraMessage): Unit = {

    /**
     * Checks whether the given source information is relevant for the statistics. BuiltIn members or members that
     * are not itself verified count as non relevant for statistics collection.
     */
    def isRelevantInfo(info: Source.Verifier.Info): Boolean = !info.node.isInstanceOf[BuiltInMember] && !info.pnode.isInstanceOf[PDomainType]

    def addViperMember(taskName: String, viperMember: Member, info: Source.Verifier.Info, time: Time, cached: Boolean, verified: Boolean, success: Boolean): Unit = {
      Violation.violation(typeInfos.contains(taskName), "No type info available for stats reporter")
      getMemberInformation(info.pnode, typeInfos(taskName), viperMember) match {
        case memberInfo if !memberInfo.isBuiltIn =>
          addResult(
            memberInfo,
            ViperMemberEntry(
              viperMember.name,
              taskName,
              time,
              ViperNodeType.withName(viperMember.getClass.getSimpleName),
              EdgesImpl.dependencies(viperMember).flatMap(edge => vertexToName(edge._2)).toSet,
              success,
              cached,
              memberInfo.isImported,
              viperMemberHasBody(viperMember),
              verified
            ))
        case _ =>
      }
    }

    msg match {
      // Capture typeInfo once it's available
      case TypeCheckSuccessMessage(_, taskName, typeInfo, _,_,_) => typeInfos.put(taskName, typeInfo.apply())
      // Free up unneeded space, once a task is finished
      case VerificationTaskFinishedMessage(taskName) => typeInfos.remove(taskName)
      // Store every viperMember with an info attached to see, which ones didn't verify on an unexpected shutdown
      case GeneratedViperMessage(taskName, _, vprAst, _) =>
        vprAst().members
          .filter(m => m.info.isInstanceOf[Source.Verifier.Info] && isRelevantInfo(m.info.asInstanceOf[Source.Verifier.Info]))
          .foreach(m => addViperMember(taskName, m, m.info.asInstanceOf[Source.Verifier.Info], 0, cached = false, verified = false, success = true))
      case GobraEntitySuccessMessage(taskName, _, e, info, time, cached) if isRelevantInfo(info) =>
        addViperMember(taskName, e, info, time, cached, verified = true, success = true)
      case GobraEntityFailureMessage(taskName, _, e, info, _, time, cached) if isRelevantInfo(info) =>
        addViperMember(taskName, e, info, time, cached, verified = true, success = false)
      case _ =>
    }
    // Pass message to next reporter
    reporter.report(msg)
  }

  private object EdgesImpl extends Edges with Vertices

  private def gobraMemberKey(pkgId: String,memberName: String, args: String): String = pkgId + "." + memberName + args

  private def viperMemberKey(taskName: String, viperMemberName: String): String = taskName + "-" + viperMemberName

  private def addResult(gobraMemberInfo: GobraMemberInfo, viperMember: ViperMemberEntry): Unit = {
    memberMap.get(gobraMemberInfo.id) match {
      case Some(existing) =>
        existing.viperMembers.get(viperMember.id) match {
          case Some(existingViperEntry) =>
            // Merge Viper members that occur multiple times during a task(used for chopper)
            existing.viperMembers.put(viperMember.id, ViperMemberEntry(
              existingViperEntry.memberName,
              existingViperEntry.taskName,
              existingViperEntry.time + viperMember.time,
              existingViperEntry.nodeType,
              existingViperEntry.dependencies ++ viperMember.dependencies,
              existingViperEntry.success && viperMember.success,
              existingViperEntry.cached || viperMember.cached,
              existingViperEntry.fromImport,
              existingViperEntry.hasBody || viperMember.hasBody,
              // Only consider verification results for viper members with a body, since otherwise if a result for an abstract
              // version of a member occurs, we would mark the method as verified even though the version with the body was not.
              existingViperEntry.verified || (!viperMember.fromImport && viperMember.verified && viperMember.hasBody)
            ))
          case None => existing.viperMembers.put(viperMember.id, viperMember)
        }

        // If we encounter an abstract version of a member, we know for sure, its abstract
        val newAbstract = existing.info.isAbstractAndNotImported || gobraMemberInfo.isAbstractAndNotImported
        Violation.violation(existing.info.isTrusted == gobraMemberInfo.isTrusted, "Same members with different trusted declarations found: \n " + gobraMemberInfo.id)
        Violation.violation(existing.info.nodeType == gobraMemberInfo.nodeType, "Same members with different node types found: \n " + gobraMemberInfo.id)
        memberMap.put(gobraMemberInfo.id, existing.copy(info = existing.info.copy(isAbstractAndNotImported = newAbstract)))
      case None => memberMap.put(gobraMemberInfo.id, GobraMemberEntry(gobraMemberInfo, TrieMap(viperMember.id -> viperMember)))
    }

    viperMemberNameGobraMemberMap.get(viperMember.id) match {
      case Some(otherMember) if !(otherMember.info.id == gobraMemberInfo.id) =>
        Violation.violation("Viper method corresponds to multiple gobra methods: " + viperMember.id + ":\n " + otherMember.info.id + " \n" + gobraMemberInfo.id)
      case None => viperMemberNameGobraMemberMap.put(viperMember.id, memberMap(gobraMemberInfo.id))
      case _ =>
    }
  }

  /**
   * Returns the name of the viper member corresponding to a vertex or None if the viper member is not relevant
   * for the statistics
   */
  private def vertexToName(vertex: Vertex): Option[String] = vertex match {
    case Vertices.Method(name) => Some(name)
    case Vertices.MethodSpec(name) => Some(name)
    case Vertices.Function(name) => Some(name)
    case Vertices.PredicateBody(name) => Some(name)
    case Vertices.PredicateSig(name) => Some(name)
    case _ => None
  }

  private def viperMemberHasBody(member: Member): Boolean = member match {
    case m: Method => m.body.isDefined && m.body.get.nonEmpty
    case p: Predicate => p.body.isDefined && p.body.get.nonEmpty
    case f: Function => f.body.isDefined && f.body.get.nonEmpty
    case _ => false
  }

  /**
   * Builds the member information for a given node out of the stored typeInfo
   */
  def getMemberInformation(p: PNode, typeInfo: TypeInfo, viperMember: Member): GobraMemberInfo = {
    val nodeTypeInfo =
      if (treeContains(typeInfo.tree, p)) {
        typeInfo
      } else {
        // Try to find the correct typeInfo for the member
        val typeInfoOption = typeInfo.getTransitiveTypeInfos()
          .map(_.getTypeInfo)
          .find(typeInfo => treeContains(typeInfo.tree, p))
        typeInfoOption match {
          case Some(typeInfo) => typeInfo
          case None => Violation.violation("Couldn't find typeInfo for node " + p.formattedShort)
        }
      }

    val pkgName = nodeTypeInfo.pkgName.name
    val pkgId = nodeTypeInfo.pkgInfo.id
    val isBuiltIn = nodeTypeInfo.pkgInfo.isBuiltIn

    // Replace whitespaces in arguments by a single space, since some types contain newlines
    def formatArgs(args: Vector[PParameter]): String =
      ("(" + args.map(f => f.typ.formattedShort).mkString(", ") + ")").replaceAll("\\s+", " ")

    def formatPredConstructorArgs(args: Vector[Option[PExpression]]): String =
      ("(" + args.filter(e => e.isDefined).map(e => e.get.formattedShort).mkString(", ") + ")").replaceAll("\\s+", " ")

    def hasFormalSpec(spec : PFunctionSpec) = spec.pres.nonEmpty || spec.posts.nonEmpty || spec.preserves.nonEmpty || spec.terminationMeasures.nonEmpty

    // Check whether the program containing this node has the builtin tag
    // Check if a node comes from a import, used to declare it non abstract, since imports are per default always
    // abstract and we don't want to generate unnecessary warnings
    val isImported = !typeInfo.tree.originalRoot.eq(nodeTypeInfo.tree.originalRoot)

    p match {
      case p: PFunctionDecl =>
        GobraMemberInfo(pkgId, pkgName, p.id.name, formatArgs(p.args), FunctionDeclaration,
          hasSpecification = hasFormalSpec(p.spec),
          isTrusted = p.spec.isTrusted,
          isAbstractAndNotImported = p.body.isEmpty && !isImported,
          isImported,
          isBuiltIn)
      case p: PMethodDecl =>
        GobraMemberInfo(pkgId, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), MethodDeclaration,
          hasSpecification = hasFormalSpec(p.spec),
          isTrusted = p.spec.isTrusted,
          isAbstractAndNotImported = p.body.isEmpty && !isImported,
          isImported,
          isBuiltIn)
      case p: PMethodSig =>
        GobraMemberInfo(pkgId, pkgName, p.id.name, formatArgs(p.args), MethodSignature,
          hasSpecification = hasFormalSpec(p.spec),
          isTrusted = p.spec.isTrusted,
          isAbstractAndNotImported = false,
          isImported,
          isBuiltIn)
      case p: PFPredicateDecl =>
        GobraMemberInfo(pkgId, pkgName, p.id.name, formatArgs(p.args), FunctionPredicateDeclaration,
          hasSpecification = false,
          isTrusted = false,
          isAbstractAndNotImported = p.body.isEmpty && !isImported,
          isImported,
          isBuiltIn)
      case p: PMPredicateDecl =>
        GobraMemberInfo(pkgId, pkgName, p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), MethodPredicateDeclaration,
          hasSpecification = false,
          isTrusted = false,
          isAbstractAndNotImported = p.body.isEmpty && !isImported,
          isImported,
          isBuiltIn)
      case p: PMethodImplementationProof =>
        GobraMemberInfo(pkgId, pkgName, "Impl_Proof." + p.receiver.typ.formattedShort + "." + p.id.name, formatArgs(p.args), MethodImplementationProof,
          hasSpecification = false,
          isTrusted = false,
          isAbstractAndNotImported = p.body.isEmpty && !isImported,
          isImported,
          isBuiltIn)
      case p: PMPredicateSig =>
        GobraMemberInfo(pkgId, pkgName, p.id.name, formatArgs(p.args), MethodPredicateSignature,
          hasSpecification = false,
          isTrusted = false,
          isAbstractAndNotImported = false,
          isImported,
          isBuiltIn)
      case p: PPredConstructor =>
        GobraMemberInfo(pkgId, pkgName, p.id.id.name, formatPredConstructorArgs(p.args), PredicateConstructor,
          hasSpecification = false,
          isTrusted = false,
          isAbstractAndNotImported = false,
          isImported,
          isBuiltIn)
      case _: PProgram =>
        GobraMemberInfo(
          pkgId = pkgId,
          pkg = pkgName,
          memberName = pkgId ++ "_program_init",
          args = "",
          nodeType = Program,
          hasSpecification = true,
          isTrusted = false,
          isAbstractAndNotImported = false,
          isImported = isImported,
          isBuiltIn = isBuiltIn)
      case _: PPackage =>
        GobraMemberInfo(
          pkgId = pkgId,
          pkg = pkgName,
          memberName = pkgId ++ "_package_init",
          args = "",
          nodeType = Package,
          hasSpecification = false,
          isTrusted = false,
          isAbstractAndNotImported = false,
          isImported = isImported,
          isBuiltIn = isBuiltIn)
      // Consider the enclosing function, for closure declarations
      case p: PClosureDecl => getMemberInformation(nodeTypeInfo.enclosingFunctionOrMethod(p).get, typeInfo, viperMember)
      // Fallback to the node's code root if we can't match the node
      case p: PNode => getMemberInformation(nodeTypeInfo.codeRoot(p), typeInfo, viperMember)
    }
  }

  /**
   * Checks whether a Go Tree contains a node
   */
  private def treeContains(tree: Info.GoTree, p: PNode): Boolean = {
    try {
      tree.whenContains(p, true)
    } catch {
      case NodeNotInTreeException(_) => false
    }
  }

  private def getNonImportedMembers: Iterable[GobraMemberEntry] =
    memberMap.values
      .filter(_.viperMembers.values.exists(!_.fromImport))

  private def getNonImportedVerifiedMembers: Iterable[GobraMemberEntry] =
    memberMap.values
      .filter(_.viperMembers.values.exists(viperMember => !viperMember.fromImport && viperMember.verified))

  /**
   * Returns the number of viper members that were reported as cached
   */
  def getNumberOfCachedViperMembers: Int =
    memberMap.values
      .flatMap(_.viperMembers.values).count(_.cached)

  /**
   * Returns the number of viper methods that have a body
   */
  def getNumberOfCacheableViperMembers: Int =
    memberMap.values
      .flatMap(_.viperMembers.values)
      .count(member => member.hasBody && member.nodeType == ViperNodeType.Method)

  /**
   * Returns the number of non-imported Gobra members that could have a specification
   */
  def getNumberOfVerifiableMembers: Int =
    getNonImportedMembers
      .count(member => Vector(MethodDeclaration, FunctionDeclaration, MethodSignature).contains(member.info.nodeType))

  /**
   * Returns the number of non-imported Gobra members that have a specification and were verified
   */
  def getNumberOfSpecifiedMembers: Int = getNonImportedVerifiedMembers.count(_.info.hasSpecification)

  /**
   * Returns the number of non-imported Gobra members that have a specification, were verified and are trusted or abstract
   */
  def getNumberOfSpecifiedMembersWithAssumptions: Int =
    getNonImportedVerifiedMembers
      .filter(entry => entry.info.isAbstractAndNotImported || entry.info.isTrusted)
      .count(_.info.hasSpecification)

  /**
   * Returns a list of non-imported, non-abstract Gobra members, for which at least one viper member's verification did
   * not terminate. Contains errors for all tasks for which a VerificationTaskFinishedMessage was not reported.
   */
  def getTimeoutErrorsForNonFinishedTasks: List[TimeoutError] =
    memberMap.values
      // check if there exists a stored typeInfo for the task, to see whether a VerificationTaskFinishedMessage was reported or not
      .filter(_.viperMembers.values.exists(viperMember => typeInfos.contains(viperMember.taskName) && !viperMember.fromImport && !viperMember.verified && viperMember.hasBody))
      .map(timeoutError).toList

  /**
   * Returns a list of non-imported, non-abstract Gobra members, for which at least one viper member's verification did
   * not terminate for a specified task
   */
  def getTimeoutErrors(taskName: String): List[TimeoutError] =
    memberMap.values
      .filter(_.viperMembers.values.exists(viperMember => viperMember.taskName == taskName && !viperMember.fromImport && !viperMember.verified && viperMember.hasBody))
      .map(timeoutError).toList

  private def timeoutError(gobraEntry: GobraMemberEntry) = TimeoutError(s"The verification of member ${gobraEntry.info.id} did not terminate")

  /**
   * Writes all statistics that have been collected with this instance of the StatsCollector to a file.
   * Returns true iff the file was written.
   */
  def writeJsonReportToFile(file: File): Boolean = {
    val canWrite = (file.exists() && file.canWrite) || file.getParentFile.canWrite
    if (canWrite) {
      FileUtils.writeStringToFile(file, getJsonReport, UTF_8)
    }
    canWrite
  }

  def getJsonReport: String = {
    val memberJson = memberMap.values.map(_.asJson(i)).mkString(", \n")
    s"""[
     |$memberJson
     |]""".stripMargin
  }

  /**
   * Returns a set of warnings for all members in a package
   */
  def getMessagesAboutDependencies(pkgId: String, config: Config): Set[Warning] =
    memberMap.values
      .filter(gobraMember => gobraMember.info.pkgId == pkgId)
      .flatMap(gobraMember => {
        val name = gobraMember.info.pkg + "." + gobraMember.info.memberName + gobraMember.info.args
        gobraMember.dependencies().flatMap({
          // Trusted implies abstracted, so we match trusted first
          case GobraMemberEntry(info, _) if info.isTrusted =>
            Some("Member " + name + " depends on trusted member " + info.pkg + "." + info.memberName + info.args + "\n")
          case GobraMemberEntry(info, _) if info.isAbstractAndNotImported =>
            Some("Member " + name + " depends on abstract member " + info.pkg + "." + info.memberName + info.args + "\n")
          // Only generate warnings about non-verified packages, when we actually have any info about which packages are verified
          case GobraMemberEntry(info, _) if config.packageInfoInputMap.nonEmpty && !config.packageInfoInputMap.keys.exists(_.id == info.pkgId) =>
            Some("Depending on imported package that is not verified: " + info.pkgId)
          case _ => None
        })
      }).toSet
}
