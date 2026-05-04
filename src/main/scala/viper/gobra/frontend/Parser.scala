// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import com.typesafe.scalalogging.LazyLogging
import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter, Strategy}
import org.bitbucket.inkytonik.kiama.util.{Positions, Source}
import org.bitbucket.inkytonik.kiama.util.Messaging.{error, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.Source.{FromFileSource, TransformableSource, getPackageInfo}
import viper.gobra.reporting.{Source => _, _}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream, DefaultErrorStrategy, ParserRuleContext}
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.misc.ParseCancellationException
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.frontend.GobraParser.{ExprOnlyContext, ImportDeclContext, MemberContext, PreambleContext, SourceFileContext, StmtOnlyContext, TypeOnlyContext}
import viper.gobra.frontend.PackageResolver.{AbstractImport, AbstractPackage, BuiltInImport, RegularImport, RegularPackage}
import viper.gobra.util.{GobraExecutionContext, Job, TaskManager, Violation}
import viper.silver.ast.SourcePosition

import scala.collection.mutable.ListBuffer
import java.security.MessageDigest
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.concurrent.Future


// `LazyLogging` provides us with access to `logger` to emit log messages
object Parser extends LazyLogging {

  type ParseSuccessResult = (Vector[Source], PPackage)
  type ParseResult = Either[Vector[ParserError], ParseSuccessResult]
  type ImportToPkgInfoOrErrorMap = Vector[(AbstractPackage, Either[Vector[ParserError], (Vector[Source], PackageInfo)])]
  type PreprocessedSources = Vector[Source]

  class ParseManager(config: Config)(implicit executor: GobraExecutionContext) extends LazyLogging {
    private val manager = new TaskManager[AbstractPackage, PreprocessedSources, ParseResult](config.parseAndTypeCheckMode)

    // note that the returned future might never complete if typeCheckMode is `Lazy` and there is no trigger to actually
    // execute the parsing of the specified package
    def parse(pkgInfo: PackageInfo): Future[ParseResult] = {
      val pkg = RegularPackage(pkgInfo.id)
      val parseJob = ParseInfoJob(pkgInfo)
      manager.addIfAbsent(pkg, parseJob)
      parseJob.getFuture
    }

    trait ImportResolver {
      def pkgInfo: PackageInfo

      type ImportErrorFactory = String => Vector[ParserError]
      protected def getImports(importNodes: Vector[PImport], pom: PositionManager): ImportToPkgInfoOrErrorMap = {
        val explicitImports: Vector[(AbstractImport, ImportErrorFactory)] = importNodes
          .map(importNode => {
            val importErrorFactory: ImportErrorFactory = (errMsg: String) => {
              val err = pom.translate(message(importNode, errMsg), ParserError)
              err
            }
            (RegularImport(importNode.importPath), importErrorFactory)
          })
        val imports = if (pkgInfo.isBuiltIn) { explicitImports } else {
          val builtInImportErrorFactory: ImportErrorFactory = (errMsg: String) => {
            val err = Vector(ParserError(errMsg, None))
            config.reporter report ParserErrorMessage(err.head.position.get.file, err)
            err
          }
          val builtInImportTuple = (BuiltInImport, builtInImportErrorFactory)
          explicitImports :+ builtInImportTuple
        }

        val errsOrSources = imports.map { case (directImportTarget, importErrorFactory) =>
          val directImportPackage = AbstractPackage(directImportTarget)(config)
          val sourcesAndPkgInfo = for {
            resolveSourceResults <- PackageResolver.resolveSources(directImportTarget)(config).left.map(importErrorFactory)
            importedSources = resolveSourceResults.map(_.source)
            nonEmptyImportedSources <- if (importedSources.isEmpty) Left(importErrorFactory(s"No source files for package '$directImportTarget' found")) else Right(importedSources)
            pkgInfo <- getPackageInfo(nonEmptyImportedSources.head, config.projectRoot)
          } yield (nonEmptyImportedSources, pkgInfo)
          (directImportPackage, sourcesAndPkgInfo)
        }
        errsOrSources
      }
    }

    /**
      * Job that preprocesses the specified sources, parses their preambles, and creates parse jobs for all imported
      * packages as part of the sequential pre-computations.
      * This job then fully parses and post-processes the package (identified via `pkgInfo` and `pkgSources`) in the
      * `compute` step and eventually produces this package's ParseResult. Additionally, the preprocessed sources are
      * provided alongside.
      */
    private trait ParseJob extends Job[PreprocessedSources, ParseResult] with ImportResolver {
      def pkgInfo: PackageInfo
      def pkgSources: Vector[Source]
      def specOnly: Boolean
      var preambleParsingDurationMs: Long = 0

      private def getImportsForPackage(preprocessedSources: Vector[Source]): ImportToPkgInfoOrErrorMap = {
        val preambles = preprocessedSources
          .map(preprocessedSource => processPreamble(preprocessedSource)(config))
          // we ignore imports in files that cannot be parsed:
          .collect { case Right(p) => p }
        preambles.flatMap(preamble => getImports(preamble.imports, preamble.positions))
      }

      protected override def sequentialPrecompute(): PreprocessedSources = {
        val preprocessedSources = preprocess(pkgSources)(config)
        val startPreambleParsingMs = System.currentTimeMillis()
        val imports = getImportsForPackage(preprocessedSources)
        preambleParsingDurationMs = System.currentTimeMillis() - startPreambleParsingMs

        // add imported packages to manager if not already
        imports.foreach {
          case (directImportPackage, Right((nonEmptySources, pkgInfo))) =>
            manager.addIfAbsent(directImportPackage, ParseSourcesJob(nonEmptySources, pkgInfo))
          case (directImportPackage, Left(errs)) =>
            manager.addIfAbsent(directImportPackage, ParseFailureJob(errs))
        }

        preprocessedSources
      }

      protected def compute(preprocessedSources: PreprocessedSources): ParseResult = {
        // note that we do not check here whether there have been parse errors in the imported packages as this would
        // introduce additional synchronization
        val startMs = System.currentTimeMillis()
        for {
          parsedProgram <- Parser.process(preprocessedSources, pkgInfo, specOnly = specOnly)(config)
          postprocessedProgram <- Parser.postprocess(Right((preprocessedSources, parsedProgram)), specOnly = specOnly)(config)
          _ = logger.trace {
            val parsingDurationMs = System.currentTimeMillis() - startMs
            val parsingDurationS = f"${parsingDurationMs / 1000f}%.1f"
            val preambleParsingRatio = f"${100f * preambleParsingDurationMs / parsingDurationMs}%.1f"
            s"parsing ${pkgInfo.id} done (took ${parsingDurationS}s; parsing preamble overhead is ${preambleParsingRatio}%)"
          }
        } yield (pkgSources, postprocessedProgram._2) // we use `pkgSources` as the preprocessing of sources should be transparent from the outside
      }
    }

    /** this job is used to parse the package that should be verified */
    private case class ParseInfoJob(override val pkgInfo: PackageInfo) extends ParseJob {
      lazy val pkgSources: Vector[Source] = config.packageInfoInputMap(pkgInfo)
      lazy val specOnly: Boolean = false
    }

    /** this job is used to parse all packages that are imported */
    private case class ParseSourcesJob(override val pkgSources: Vector[Source], override val pkgInfo: PackageInfo) extends ParseJob {
      require(pkgSources.nonEmpty)
      lazy val specOnly: Boolean = true
    }

    private case class ParseFailureJob(errs: Vector[ParserError]) extends Job[PreprocessedSources, ParseResult] {
      override protected def sequentialPrecompute(): PreprocessedSources =
        Vector.empty
      override protected def compute(precomputationResult: PreprocessedSources): ParseResult =
        Left(errs)
    }

    def getResults: Future[Map[AbstractPackage, ParseResult]] = manager.getAllResultsWithKeys
  }

  /**
    * Parses files and returns either the parsed program if the file was parsed successfully,
    * otherwise returns list of error messages.
    *
    * @param input
    * @param specOnly specifies whether only declarations and specifications should be parsed and implementation should be ignored
    * @return
    *
    * The following transformations are performed:
    * e++  ~>  e += 1
    * e--  ~>  e -= 1
    * +e   ~>  0 + e
    * -e   ~>  0 - e
    *
    */

  def parse(config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): EitherT[Vector[VerifierError], Future, Map[AbstractPackage, ParseResult]] = {
    val parseManager = new ParseManager(config)
    parseManager.parse(pkgInfo)
    val res: Future[Either[Vector[VerifierError], Map[AbstractPackage, ParseResult]]] = for {
      results <- parseManager.getResults
      res = results.get(RegularPackage(pkgInfo.id)) match {
        case Some(Right(_)) => Right(results)
        case Some(Left(errs)) => Left(errs)
        case _ => Violation.violation(s"No parse result for package '$pkgInfo' found")
      }
    } yield res
    EitherT.fromEither(res)
  }

  private def preprocess(input: Vector[Source])(config: Config): Vector[Source] = {
    val sources = input.map(Gobrafier.gobrafy)
    sources.foreach { s => config.reporter report PreprocessedInputMessage(s.name, () => s.content) }
    sources
  }

  /**
    * Parses a source's preamble containing all (file-level) imports; This function expects that the input has already
    * been preprocessed
    */
  private def processPreamble(preprocessedSource: Source)(config: Config): Either[Vector[ParserError], PPreamble] = {
    def parseSource(preprocessedSource: Source): Either[Vector[ParserError], PPreamble] = {
      val positions = new Positions
      val pom = new PositionManager(positions)
      val parser = new SyntaxAnalyzer[PreambleContext, PPreamble](preprocessedSource, ListBuffer.empty[ParserError], pom, specOnly = true)
      parser.parse(parser.preamble)
    }

    var cacheHit: Boolean = true
    def parseSourceCached(preprocessedSource: Source): Either[Vector[ParserError], PPreamble] = {
      def parseAndStore(): Either[Vector[ParserError], PPreamble] = {
        cacheHit = false
        parseSource(preprocessedSource)
      }

      val res = preambleCache.computeIfAbsent(getPreambleCacheKey(preprocessedSource), _ => parseAndStore())
      if (!cacheHit) {
        logger.trace(s"No cache hit for ${res.map(_.packageClause.id.name)}'s preamble")
      }
      res
    }

    if (config.cacheParserAndTypeChecker) parseSourceCached(preprocessedSource) else parseSource(preprocessedSource)
  }

  private def process(preprocessedInputs: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean)(config: Config): Either[Vector[ParserError], PPackage] = {
    parseSources(preprocessedInputs, pkgInfo, specOnly = specOnly)(config)
  }

  private def postprocess(processResult: Either[Vector[ParserError], (Vector[Source], PPackage)], specOnly: Boolean)(config: Config): Either[Vector[ParserError], (Vector[Source], PPackage)] = {
    for {
      successfulProcessResult <- processResult
      (preprocessedInputs, parseAst) = successfulProcessResult
      postprocessors = Seq(
        new ImportPostprocessor(parseAst.positions.positions),
        new TerminationMeasurePostprocessor(parseAst.positions.positions, specOnly = specOnly),
      )
      postprocessedAst <- postprocessors.foldLeft[Either[Vector[ParserError], PPackage]](Right(parseAst)) {
        case (Right(ast), postprocessor) => postprocessor.postprocess(ast)(config)
        case (e, _) => e
      }
    } yield (preprocessedInputs, postprocessedAst)
  }

  type SourceCacheKey = String
  // cache maps a key (obtained by hashing file path and file content) to the parse result
  private val preambleCache: ConcurrentMap[SourceCacheKey, Either[Vector[ParserError], PPreamble]] = new ConcurrentHashMap()
  type PackageCacheKey = String
  // we cache entire packages and not individual files (i.e. PProgram) as this saves us from copying over positional information
  // from one to the other position manager. Also, this transformation of copying positional information results in
  // differen PPackage instances that is problematic for caching type-check results.
  private val packageCache: ConcurrentMap[PackageCacheKey, Either[Vector[ParserError], PPackage]] = new ConcurrentHashMap()

  /** computes the key for caching the preamble of a particular source. This takes the name and the source's content into account */
  private def getPreambleCacheKey(source: Source): SourceCacheKey = {
    val key = source.name ++ source.content
    val bytes = MessageDigest.getInstance("MD5").digest(key.getBytes)
    // convert `bytes` to a hex string representation such that we get equality on the key while performing cache lookups
    bytes.map { "%02x".format(_) }.mkString
  }

  /** computes the key for caching a package. This takes the name and the content of each source, the package info and the `specOnly` flag into account */
  private def getPackageCacheKey(sources: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean): PackageCacheKey = {
    val key = sources.map(source => source.name ++ source.content).mkString("") ++ pkgInfo.hashCode.toString ++ (if (specOnly) "1" else "0")
    val bytes = MessageDigest.getInstance("MD5").digest(key.getBytes)
    // convert `bytes` to a hex string representation such that we get equality on the key while performing cache lookups
    bytes.map { "%02x".format(_) }.mkString
  }

  def flushCache(): Unit = {
    preambleCache.clear()
    packageCache.clear()
  }

  private def parseSources(sources: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean)(config: Config): Either[Vector[ParserError], PPackage] = {
    def parseSourcesCached(sources: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean)(config: Config): Either[Vector[ParserError], PPackage] = {
      var cacheHit: Boolean = true
      val res = packageCache.computeIfAbsent(getPackageCacheKey(sources, pkgInfo, specOnly), _ => {
        cacheHit = false
        parseSourcesUncached(sources, pkgInfo, specOnly)(config)
      })
      if (!cacheHit) {
        logger.trace(s"No cache hit for package ${pkgInfo.id}'s parse AST)")
      }
      res
    }

    val parseFn = if (config.cacheParserAndTypeChecker) { parseSourcesCached _ } else { parseSourcesUncached _ }
    parseFn(sources, pkgInfo, specOnly)(config)
  }

  /** parses a package not taking the package cache but only the program cache into account */
  private def parseSourcesUncached(sources: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean)(config: Config): Either[Vector[ParserError], PPackage] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    lazy val rewriter = new PRewriter(pom.positions)

    def parseSource(source: Source): Either[Vector[ParserError], PProgram] = {
      val errors = ListBuffer.empty[ParserError]
      val parser = new SyntaxAnalyzer[SourceFileContext, PProgram](source, errors, pom, specOnly)
      parser.parse(parser.sourceFile) match {
        case Right(ast) =>
          config.reporter report ParsedInputMessage(source.name, () => ast)
          Right(ast)
        case Left(errors) =>
          val (positionedErrors, nonpos) = errors.partition(_.position.nonEmpty)
          // Non-positioned errors imply some unexpected problems within the parser. We can't continue.
          if (nonpos.nonEmpty) throw new Exception("ANTLR threw unexpected errors" + nonpos.mkString(","))
          Left(positionedErrors)
      }
    }

    def isErrorFree(parserResults: Vector[Either[Vector[ParserError], PProgram]]): Either[Vector[ParserError], Vector[PProgram]] = {
      val (errors, programs) = parserResults.partitionMap(identity)
      if (errors.isEmpty) Right(programs) else Left(errors.flatten)
    }

    def checkPackageInfo(programs: Vector[PProgram]): Either[Vector[ParserError], Vector[PProgram]] = {
      require(programs.nonEmpty)
      val differingPkgNameMsgs = programs.flatMap(p =>
        error(
          p.packageClause,
          s"Files have differing package clauses, expected ${pkgInfo.name} but got ${p.packageClause.id.name}",
          p.packageClause.id.name != pkgInfo.name)
      )

      if (differingPkgNameMsgs.isEmpty) {
        Right(programs)
      } else {
        Left(pom.translate(differingPkgNameMsgs, ParserError))
      }
    }

    def makePackage(programs: Vector[PProgram]): Either[Vector[ParserError], PPackage] = {
      val clause = rewriter.deepclone(programs.head.packageClause)

      val parsedPackage = PPackage(clause, programs, pom, pkgInfo)

      // The package parse tree node gets the position of the package clause:
      pom.positions.dupPos(clause, parsedPackage)
      Right(parsedPackage)
    }

    val parsedPrograms = sources.map(parseSource)

    val res = for {
      // check that each of the parsed programs has the same package clause. If not, the algorithm collecting all files
      // of the same package has failed or users have entered an invalid collection of inputs
      programs <- isErrorFree(parsedPrograms)
      programs <- checkPackageInfo(programs)
      pkg <- makePackage(programs)
    } yield pkg
    // report potential errors:
    res.left.foreach(errors => {
      val groupedErrors = errors.groupBy{ _.position.get.file }
      groupedErrors.foreach { case (p, pErrors) =>
        config.reporter report ParserErrorMessage(p, pErrors)
      }
    })
    res
  }

  def parseProgram(source: Source, specOnly: Boolean = false): Either[Vector[ParserError], PProgram] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[SourceFileContext, PProgram](source, ListBuffer.empty[ParserError], pom, specOnly)
    parser.parse(parser.sourceFile())
  }

  def parseMember(source: Source, specOnly: Boolean = false): Either[Vector[ParserError], Vector[PMember]] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[MemberContext, Vector[PMember]](source, ListBuffer.empty[ParserError], pom, specOnly)
    parser.parse(parser.member())
  }

  def parseStmt(source: Source): Either[Vector[ParserError], PStatement] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[StmtOnlyContext, PStatement](source, ListBuffer.empty[ParserError], pom, false)
    parser.parse(parser.stmtOnly())
  }

  def parseExpr(source: Source): Either[Vector[ParserError], PExpression] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[ExprOnlyContext, PExpression](source, ListBuffer.empty[ParserError], pom, false)
    parser.parse(parser.exprOnly())
  }

  def parseImportDecl(source: Source): Either[Vector[ParserError], Vector[PImport]] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[ImportDeclContext, Vector[PImport]](source, ListBuffer.empty[ParserError], pom, false)
    parser.parse(parser.importDecl())
  }

  def parseType(source : Source) : Either[Vector[ParserError], PType] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[TypeOnlyContext, PType](source, ListBuffer.empty[ParserError], pom, false)
    parser.parse(parser.typeOnly())
  }


  trait Postprocessor extends PositionedRewriter {
    /** this PositionedAstNode contains a subset of the utility functions found in ParseTreeTranslator.PositionedAstNode */
    implicit class PositionedAstNode[N <: AnyRef](node: N) {
      def at(other: PNode): N = {
        positions.dupPos(other, node)
      }
    }

    def postprocess(pkg: PPackage)(config: Config): Either[Vector[ParserError], PPackage]
  }

  private class ImportPostprocessor(override val positions: Positions) extends Postprocessor {
    /**
      * Replaces all PQualifiedWoQualifierImport by PQualifiedImport nodes
      */
    def postprocess(pkg: PPackage)(config: Config): Either[Vector[ParserError], PPackage] = {
      def createError(n: PImplicitQualifiedImport, errorMsg: String): Vector[ParserError] = {
        val err = pkg.positions.translate(message(n,
          s"Explicit qualifier could not be derived (reason: '$errorMsg')"), ParserError)
        config.reporter report ParserErrorMessage(err.head.position.get.file, err)
        err
      }

      // unfortunately Kiama does not seem to offer a way to report errors while applying the strategy
      // hence, we keep ourselves track of errors
      var failedNodes: Vector[ParserError] = Vector()

      def replace(n: PImplicitQualifiedImport): Option[PExplicitQualifiedImport] = {
        val qualifier = for {
          qualifierName <- PackageResolver.getQualifier(n)(config)
          // create a new PIdnDef node and set its positions according to the old node (PositionedRewriter ensures that
          // the same happens for the newly created PExplicitQualifiedImport)
          idnDef = PIdnDef(qualifierName).at(n)
        } yield PExplicitQualifiedImport(idnDef, n.importPath, n.importPres)
        // record errors:
        qualifier.left.foreach(errorMsg => failedNodes = failedNodes ++ createError(n, errorMsg))
        qualifier.toOption
      }

      // note that the next term after PPackageClause to which the strategy will be applied is a Vector of PProgram
      val resolveImports: Strategy =
        strategyWithName[Any]("resolveImports", {
          case n: PImplicitQualifiedImport => replace(n)
          case n => Some(n)
        })

      // apply strategy only to import nodes in each program
      val updatedProgs = pkg.programs.map(prog => {
        // apply the resolveImports to all import nodes and children and continue even if a strategy returns None
        // note that the resolveImports strategy could be embedded in e.g. a logfail strategy to report a
        // failed strategy application
        val updatedImports = rewrite(topdown(attempt(resolveImports)))(prog.imports)
        PProgram(prog.packageClause, prog.pkgInvariants, updatedImports, prog.friends, prog.declarations).at(prog)
      })
      // create a new package node with the updated programs
      val updatedPkg = PPackage(pkg.packageClause, updatedProgs, pkg.positions, pkg.info).at(pkg)
      // check whether an error has occurred
      if (failedNodes.isEmpty) Right(updatedPkg)
      else Left(failedNodes)
    }
  }

  private class TerminationMeasurePostprocessor(override val positions: Positions, specOnly: Boolean) extends Postprocessor {
    /**
      * if `specOnly` is set to true, this postprocessor replaces all tuple termination measures specified for pure functions
      * or pure methods by wildcard termination measures while maintaining their condition (if any). Note that termination
      * measures specified for interface methods remain untouched.
      *
      * These steps ensure that termination of imported pure functions and pure methods is not checked again (in case
      * a decreases measure has been provided) and instead gets simply assumed.
      *
      * Note that we do not transform the body of pure functions and pure methods (e.g. by turning the body into a
      * postcondition) because this would result in a matching loop for recursive functions.
      */
    def postprocess(pkg: PPackage)(config: Config): Either[Vector[ParserError], PPackage] = {
      if (specOnly) replaceTerminationMeasures(pkg) else Right(pkg)
    }

    private def replaceTerminationMeasures(pkg: PPackage): Either[Vector[ParserError], PPackage] = {
      def replace(spec: PFunctionSpec): PFunctionSpec = {
        val replacedMeasures = spec.terminationMeasures.map {
          case n@PTupleTerminationMeasure(_, cond) => PWildcardMeasure(cond).at(n)
          case t => t
        }
        PFunctionSpec(spec.pres, spec.preserves, spec.posts, replacedMeasures, spec.backendAnnotations, spec.isPure, spec.isTrusted, mayBeUsedInInit = spec.mayBeUsedInInit)
      }

      val replaceTerminationMeasuresForFunctionsAndMethods: Strategy =
        strategyWithName[Any]("replaceTerminationMeasuresForFunctionsAndMethods", {
          // apply transformation only to the specification of function or method declaration (in particular, do not
          // apply the transformation to method signatures in interface declarations)
          case n: PFunctionDecl => Some(PFunctionDecl(n.id, n.args, n.result, replace(n.spec), n.body))
          case n: PMethodDecl => Some(PMethodDecl(n.id, n.receiver, n.args, n.result, replace(n.spec), n.body))
          case n: PMember => Some(n)
        })

      val updatedProgs = pkg.programs.map(prog => {
        // apply the replaceTerminationMeasuresForFunctionsAndMethods to declarations until the strategy has succeeded
        // (i.e. has reached PMember nodes) and stop then
        val updatedDecls = rewrite(alltd(replaceTerminationMeasuresForFunctionsAndMethods))(prog.declarations)
        PProgram(prog.packageClause, prog.pkgInvariants, prog.imports, prog.friends, updatedDecls).at(prog)
      })
      // create a new package node with the updated programs
      Right(PPackage(pkg.packageClause, updatedProgs, pkg.positions, pkg.info).at(pkg))
    }
  }

  private class SyntaxAnalyzer[Rule <: ParserRuleContext, Node <: AnyRef](tokens: CommonTokenStream, source: Source, errors: ListBuffer[ParserError], pom: PositionManager, specOnly: Boolean = false) extends GobraParser(tokens){


    def this(source: Source, errors: ListBuffer[ParserError], pom: PositionManager, specOnly: Boolean) = {
      this({
        val charStream = CharStreams.fromReader(source.reader)
        val lexer = new GobraLexer(charStream)
        lexer.removeErrorListeners()
        lexer.addErrorListener(new InformativeErrorListener(errors, source))
        new CommonTokenStream(lexer)
        },
        source, errors, pom, specOnly)
      getInterpreter.setPredictionMode(PredictionMode.SLL)
      // Remove the default error listener
      removeErrorListeners()
      // Add the error handler that doesn't try to recover, in case of errors, we just
      // switch to the more powerful, but slower LL parsing
      setErrorHandler(new ReportFirstErrorStrategy)
      // Add our own error listener that generates better messages from ANTLRs errors
      addErrorListener(new InformativeErrorListener(errors, source))
    }

    def parse_LL(rule : => Rule, overrideErrors : Boolean): ParserRuleContext = {
      // The second stage of the two stage parsing process, as described in
      // the official ANTLR Guide.
      // thrown by ReportFirstErrorStrategy
      tokens.seek(0)
      // rewind input stream
      reset()
      val ll_errors = ListBuffer.empty[ParserError]
      removeErrorListeners()
      // For full analysis, we want to get all errors.
      addErrorListener(new InformativeErrorListener(ll_errors, source))
      setErrorHandler(new DefaultErrorStrategy)
      // full now with full LL(*)
      getInterpreter.setPredictionMode(PredictionMode.LL)

      val res = try rule
      catch {
        case e : Exception => errors.append(ParserError(e.getMessage, Some(SourcePosition(source.toPath, 0, 0))))
          new ParserRuleContext()
      }
      // If we did not find any errors with LL-parsing, we know that the input is correct,
      // so errors from the weaker SLL parsing can be disregarded
      if (ll_errors.isEmpty) errors.clear()
      // Replace the errors, in the case of `<IDENTIFIER> { }` ambiguities in switch/if-statements/closure-proofs
      // This is to avoid reporting spurious errors caused by stack dependencies for the Gobra grammar
      if (ll_errors.nonEmpty && overrideErrors) {
        errors.clear()
        errors.append(ll_errors.head)
      }
      res
    }

    def parse(rule : => Rule): Either[Vector[ParserError], Node] = {
      val tree = try rule
      catch {
        case _: AmbiguityException => parse_LL(rule, overrideErrors = true) // Resolve `<IDENTIFIER> { }` ambiguities in switch/if-statements/closure-proofs
        case _: ParseCancellationException => parse_LL(rule, overrideErrors = false) // For even faster parsing, replace with `new ParserRuleContext()`.
        case e: Throwable => errors.append(ParserError(e.getMessage, Some(SourcePosition(source.toPath, 0, 0)))); new ParserRuleContext()
      }
      if(errors.isEmpty) {
        val translator = new ParseTreeTranslator(pom, source, specOnly)
        val parseAst : Node = try translator.translate(tree)
          catch {
            case e: TranslationFailure =>
              val pos = source match {
                case fileSource: FromFileSource => Some(SourcePosition(fileSource.path , e.cause.startPos.line, e.cause.endPos.column))
                case _ => None
              }
              return Left(Vector(ParserError(e.getMessage + " " + e.getStackTrace.toVector(1), pos)))
            case e: UnsupportedOperatorException =>
              val pos = source match {
                case fileSource: FromFileSource => Some(SourcePosition(fileSource.path, e.cause.startPos.line, e.cause.endPos.column))
                case _ => None
              }
              return Left(Vector(ParserError(e.getMessage + " " + e.getStackTrace.toVector(0), pos)))
            case e: Throwable =>
              val pos = source match {
                case fileSource: FromFileSource => Some(SourcePosition(fileSource.path, 0, 0))
                case _ => None
              }
              return Left(Vector(ParserError(e.getMessage + " " + e.getStackTrace.take(4).mkString("Array(", ", ", ")"), pos)))
          }

        // TODO : Add support for non-critical warnings in the verification process instead of printing them to stdout
        if (translator.warnings.nonEmpty){
          println(translator.warnings.mkString("Warnings: [", "\n" , " ]"))
        }
        Right(parseAst)
      } else {
        Left(errors.toVector)
      }
    }
  }

  class PRewriter(override val positions: Positions) extends PositionedRewriter with Cloner

}
