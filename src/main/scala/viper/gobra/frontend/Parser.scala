// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import org.bitbucket.inkytonik.kiama.rewriting.Cloner.{rewrite, topdown}
import org.bitbucket.inkytonik.kiama.rewriting.PositionedRewriter.strategyWithName
import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter, Strategy}
import org.bitbucket.inkytonik.kiama.util.{Positions, Source}
import org.bitbucket.inkytonik.kiama.util.Messaging.{error, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.Source.{FromFileSource, TransformableSource}
import viper.gobra.reporting.{Source => _, _}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream, DefaultErrorStrategy, ParserRuleContext}
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.misc.ParseCancellationException
import viper.gobra.frontend.GobraParser.{ExprOnlyContext, ImportDeclContext, SourceFileContext, SpecMemberContext, StmtOnlyContext, TypeOnlyContext}
import viper.silver.ast.SourcePosition

import scala.collection.mutable.ListBuffer
import java.security.MessageDigest

object Parser {

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

  def parse(input: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean = false)(config: Config): Either[Vector[VerifierError], PPackage] = {
    val sources = input
      .map(Gobrafier.gobrafy)
    for {
      parseAst <- parseSources(sources, pkgInfo, specOnly)(config)
      postprocessedAst <- new ImportPostprocessor(parseAst.positions.positions).postprocess(parseAst)(config)
    } yield postprocessedAst
  }

  type SourceCacheKey = String
  // cache maps a key (obtained by hasing file path and file content) to the parse result
  private var sourceCache: Map[SourceCacheKey, (Either[Vector[ParserError], PProgram], Positions)] = Map.empty

  /** computes the key for caching a particular source. This takes the name, the specOnly flag, and the file's contents into account */
  private def getCacheKey(source: Source, specOnly: Boolean): SourceCacheKey = {
    val key = source.name ++ (if (specOnly) "1" else "0") ++ source.content
    val bytes = MessageDigest.getInstance("MD5").digest(key.getBytes)
    // convert `bytes` to a hex string representation such that we get equality on the key while performing cache lookups
    bytes.map { "%02x".format(_) }.mkString
  }

  def flushCache(): Unit = {
    sourceCache = Map.empty
  }

  private def parseSources(sources: Vector[Source], pkgInfo: PackageInfo, specOnly: Boolean)(config: Config): Either[Vector[VerifierError], PPackage] = {
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



    def parseSourceCached(source: Source): Either[Vector[ParserError], PProgram] = {
      var cacheHit = true
      def parseAndStore(): (Either[Vector[ParserError], PProgram], Positions) = {
        cacheHit = false
        val res = parseSource(source)
        sourceCache += getCacheKey(source, specOnly) -> (res, positions)
        (res, positions)
      }
      val (res, pos) = sourceCache.getOrElse(getCacheKey(source, specOnly), parseAndStore())
      if (cacheHit) {
        // a cached AST has been found in the cache. The position manager does not yet have any positions for nodes in
        // this AST. Therefore, the following strategy iterates over the entire AST and copies positional information
        // from the cached positions to the position manager
        val copyPosStrategy = strategyWithName[Any]("copyPositionInformation", {
          case n: PNode =>
            val start = pos.getStart(n)
            val finish = pos.getFinish(n)
            start.foreach(positions.setStart(n, _))
            finish.foreach(positions.setFinish(n, _))
            Some(n): Option[Any]
          case n => Some(n)
        })
        res.map(prog => rewrite(topdown(copyPosStrategy))(prog))
      } else {
        res
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

    val parsingFn = if (config.cacheParser) { parseSourceCached _ } else { parseSource _ }
    val parsedPrograms = sources.map(parsingFn)

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

  def parseFunction(source: Source, specOnly: Boolean = false): Either[Vector[ParserError], PMember] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parser = new SyntaxAnalyzer[SpecMemberContext, PMember](source, ListBuffer.empty[ParserError], pom, specOnly)
    parser.parse(parser.specMember())
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



  private class ImportPostprocessor(override val positions: Positions) extends PositionedRewriter {
    /**
      * Replaces all PQualifiedWoQualifierImport by PQualifiedImport nodes
      */
    def postprocess(pkg: PPackage)(config: Config): Either[Vector[VerifierError], PPackage] = {
      def createError(n: PImplicitQualifiedImport, errorMsg: String): Vector[VerifierError] =
        pkg.positions.translate(message(n,
          s"Explicit qualifier could not be derived (reason: '$errorMsg')"), ParserError)

      // unfortunately Kiama does not seem to offer a way to report errors while applying the strategy
      // hence, we keep ourselves track of errors
      var failedNodes: Vector[VerifierError] = Vector()

      def replace(n: PImplicitQualifiedImport): Option[PExplicitQualifiedImport] = {
        val qualifier = for {
          qualifierName <- PackageResolver.getQualifier(n)(config)
          // create a new PIdnDef node and set its positions according to the old node (PositionedRewriter ensures that
          // the same happens for the newly created PExplicitQualifiedImport)
          idnDef = PIdnDef(qualifierName)
          _ = pkg.positions.positions.dupPos(n, idnDef)
        } yield PExplicitQualifiedImport(idnDef, n.importPath)
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
        val updatedProg = PProgram(prog.packageClause, updatedImports, prog.declarations)
        pkg.positions.positions.dupPos(prog, updatedProg)
      })
      // create a new package node with the updated programs
      val updatedPkg = PPackage(pkg.packageClause, updatedProgs, pkg.positions, pkg.info)
      pkg.positions.positions.dupPos(pkg, updatedPkg)
      // check whether an error has occurred
      if (failedNodes.isEmpty) Right(updatedPkg)
      else Left(failedNodes)
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

    def parse_LL(rule : => Rule): ParserRuleContext = {
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
      res
    }

    def parse(rule : => Rule): Either[Vector[ParserError], Node] = {
      val tree = try rule
      catch {
        case _: AmbiguityException => parse_LL(rule) // Resolve `<IDENTIFIER> { }` ambiguities in switch/if-statements
        case _: ParseCancellationException => parse_LL(rule) // For even faster parsing, replace with `new ParserRuleContext()`.
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
