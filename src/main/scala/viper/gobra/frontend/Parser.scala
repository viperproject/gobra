// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.Reader
import java.nio.file.{Files, Path}

import org.apache.commons.text.StringEscapeUtils
import org.bitbucket.inkytonik.kiama.parsing.{NoSuccess, ParseResult, Parsers, Success}
import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter, Strategy}
import org.bitbucket.inkytonik.kiama.util.{Filenames, IO, Positions, Source, StringSource}
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.reporting.{ParsedInputMessage, ParserError, ParserErrorMessage, PreprocessedInputMessage, VerifierError}
import viper.gobra.util.{Constants, Violation}

import scala.io.BufferedSource
import scala.util.matching.Regex

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

  def parse(input: Vector[Path], specOnly: Boolean = false)(config: Config): Either[Vector[VerifierError], PPackage] = {
    val preprocessedSources = input
      .map{ getSource }
      .map{ source => SemicolonPreprocessor.preprocess(source)(config) }
    for {
      parseAst <- parseSources(preprocessedSources, specOnly)(config)
      postprocessedAst <- new ImportPostprocessor(parseAst.positions.positions).postprocess(parseAst)(config)
    } yield postprocessedAst
  }

  private def getSource(path: Path): FromFileSource = {
    val inputStream = Files.newInputStream(path)
    val bufferedSource = new BufferedSource(inputStream)
    val content = bufferedSource.mkString
    bufferedSource.close()
    FromFileSource(path, content)
  }

  private def parseSources(sources: Vector[FromFileSource], specOnly: Boolean)(config: Config): Either[Vector[VerifierError], PPackage] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom, specOnly)

    def parseSource(source: FromFileSource): Either[Vector[VerifierError], PProgram] = {
      parsers.parseAll(parsers.program, source) match {
        case Success(ast, _) =>
          config.reporter report ParsedInputMessage(source.path, () => ast)
          Right(ast)

        case ns@NoSuccess(label, next) =>
          val pos = next.position
          pom.positions.setStart(ns, pos)
          pom.positions.setFinish(ns, pos)
          val messages = message(ns, label)
          val errors = pom.translate(messages, ParserError)
          
          val groupedErrors = errors.groupBy{ _.position.get.file }
          groupedErrors.foreach{ case (p, pErrors) =>
            config.reporter report ParserErrorMessage(p, pErrors)
          }

          Left(errors)

        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
    }

    val parsedPrograms = {
      val parserResults = sources.map(parseSource)
      val (errors, programs) = parserResults.partitionMap(identity)

      if (errors.nonEmpty) {
        Left(errors.flatten)
      } else {
        // check that each of the parsed programs has the same package clause. If not, the algorithm collecting all files
        // of the same package has failed
        assert(programs.nonEmpty)
        assert{
          val packageName = programs.head.packageClause.id.name
          programs.forall(_.packageClause.id.name == packageName)
        }

        Right(programs)
      }
    }

    parsedPrograms.map(programs => {
      val clause = parsers.rewriter.deepclone(programs.head.packageClause)
      val parsedPackage = PPackage(clause, programs, pom)
      // The package parse tree node gets the position of the package clause:
      pom.positions.dupPos(clause, parsedPackage)
      parsedPackage
    })
  }

  def parseProgram(source: Source, specOnly: Boolean = false): Either[Messages, PProgram] = {
    val preprocessedSource = SemicolonPreprocessor.preprocess(source)
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom, specOnly)
    translateParseResult(pom)(parsers.parseAll(parsers.program, preprocessedSource))
  }

  def parseMember(source: Source, specOnly: Boolean = false): Either[Messages, Vector[PMember]] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom, specOnly)
    translateParseResult(pom)(parsers.parseAll(parsers.member, source))
  }

  def parseStmt(source: Source): Either[Messages, PStatement] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom)
    translateParseResult(pom)(parsers.parseAll(parsers.statement, source))
  }

  def parseExpr(source: Source): Either[Messages, PExpression] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom)
    translateParseResult(pom)(parsers.parseAll(parsers.expression, source))
  }

  def parseImportDecl(source: Source): Either[Messages, Vector[PImport]] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom)
    translateParseResult(pom)(parsers.parseAll(parsers.importDecl, source))
  }

  def parseType(source : Source) : Either[Messages, PType] = {
    val positions = new Positions
    val pom = new PositionManager(positions)
    val parsers = new SyntaxAnalyzer(pom)
    translateParseResult(pom)(parsers.parseAll(parsers.typ, source))
  }

  private def translateParseResult[T](pom: PositionManager)(r: ParseResult[T]): Either[Messages, T] = {
    r match {
      case Success(ast, _) => Right(ast)

      case ns@NoSuccess(label, next) =>
        val pos = next.position
        pom.positions.setStart(ns, pos)
        pom.positions.setFinish(ns, pos)
        Left(message(ns, label))

      case c => Violation.violation(s"This case should be unreachable, but got $c")
    }
  }

  private object SemicolonPreprocessor {

    /**
      * Assumes that source corresponds to an existing file
      */
    def preprocess(source: FromFileSource)(config: Config): FromFileSource = {
      val translatedContent = translate(source.content)
      config.reporter report PreprocessedInputMessage(source.path, () => translatedContent)
      FromFileSource(source.path, translatedContent)
    }

    def preprocess(source: Source): Source = {
      val translatedContent = translate(source.content)
      StringSource(translatedContent)
    }

    private def translate(content: String): String =
      content.split("\r\n|\n").map(translateLine).mkString("\n") ++ "\n"

    private def translateLine(line: String): String = {
      val identifier = """[a-zA-Z_][a-zA-Z0-9_]*"""
      val integer = """[0-9]+"""
      val rawStringLit = """`(?:.|\n)*`"""
      val interpretedStringLit = """\".*\""""
      val stringLit = s"$rawStringLit|$interpretedStringLit"
      val specialKeywords = """break|continue|fallthrough|return"""
      val specialOperators = """\+\+|--"""
      val closingParens = """\)|]|}"""
      val finalTokenRequiringSemicolon = s"$identifier|$integer|$stringLit|$specialKeywords|$specialOperators|$closingParens"

      val ignoreLineComments = """\/\/.*"""
      val ignoreSelfContainedGeneralComments = """\/\*.*?\*\/"""
      val ignoreStartingGeneralComments = """\/\*(?!.*?\*\/).*"""
      val ignoreGeneralComments = s"$ignoreSelfContainedGeneralComments|$ignoreStartingGeneralComments"
      val ignoreComments = s"$ignoreLineComments|$ignoreGeneralComments"
      val ignoreWhitespace = """\s"""

      val r = s"($finalTokenRequiringSemicolon)((?:$ignoreComments|$ignoreWhitespace)*)$$".r
      // group(1) contains the finalTokenRequiringSemicolon after which a semicolon should be inserted
      // group(2) contains the line's remainder after finalTokenRequiringSemicolon
      r.replaceAllIn(line, m => StringEscapeUtils.escapeJava(m.group(1) ++ ";" ++ m.group(2)))
    }
  }

  case class FromFileSource(path: Path, content: String) extends Source {
    override val name: String = path.getFileName.toString
    val shortName : Option[String] = Some(Filenames.dropCurrentPath(name))
    def reader : Reader = IO.stringreader(content)

    def useAsFile[T](fn : String => T) : T = {
      // copied from StringSource
      val filename = Filenames.makeTempFilename(name)
      IO.createFile(filename, content)
      val t = fn(filename)
      IO.deleteFile(filename)
      t
    }
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
          qualifierName <- PackageResolver.getQualifier(n, config.includeDirs)
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
      val updatedPkg = PPackage(pkg.packageClause, updatedProgs, pkg.positions)
      pkg.positions.positions.dupPos(pkg, updatedPkg)
      // check whether an error has occurred
      if (failedNodes.isEmpty) Right(updatedPkg)
      else Left(failedNodes)
    }
  }

  private class SyntaxAnalyzer(pom: PositionManager, specOnly: Boolean = false) extends Parsers(pom.positions) {

    lazy val rewriter = new PRewriter(pom.positions)

    val singleWhitespaceChar: String = """(\s|(//.*\s*\n)|/\*(?:.|[\n\r])*?\*/)"""
    override val whitespace: Parser[String] =
      s"$singleWhitespaceChar*".r

    //    """(\s|(//.*\s*\n)|/\*(?s:(.*)?)\*/)*""".r
    // The above regex matches the same whitespace strings as this one:
    //   (\s|(//.*\s*\n)|/\*(?:.|[\n\r])*?\*/)*
    // but (hopefully) avoids potential stack overflows caused by an issue
    // of Oracle's JDK. Note: the issue was reported for Java 6 and 7, it
    // appears to not affect Java 8.
    // See also:
    //   - http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6882582
    //   - https://stackoverflow.com/a/31691056
    //

    val reservedWords: Set[String] = Set(
      "break", "default", "func", "interface", "select",
      "case", "defer", "go", "map", "struct", "domain",
      "chan", "else", "goto", "package", "switch",
      "const", "fallthrough", "if", "range", "type",
      "continue", "for", "import", "return", "var",
      "len", "cap", "make", "new",
      // new keywords introduced by Gobra
      "ghost", "acc", "assert", "exhale", "assume", "inhale",
      "memory", "fold", "unfold", "unfolding", "pure",
      "predicate", "old", "seq", "set", "in", "union",
      "intersection", "setminus", "subset", "mset", "option",
      "none", "some", "get", "writePerm", "noPerm",
      "typeOf", "isComparable"
    )

    def isReservedWord(word: String): Boolean = reservedWords contains word

    /**
      * Optionally consumes nested curly brackets with arbitrary content if `specOnly` is turned on, otherwise optionally applies the parser `p`
      */
    def specOnlyParser[T](p: Parser[T]): Parser[Option[T]] =
      if (specOnly) nestedCurlyBracketsConsumer.? ^^ (_.flatten)
      else p.?

    /**
      * Consumes nested curly brackets with arbitrary content and returns None
      */
    lazy val nestedCurlyBracketsConsumer: Parser[Option[Nothing]] =
      "{" ~> ("""[^{}]""".r | nestedCurlyBracketsConsumer).* <~ "}" ^^ (_ => None)

    /**
      * Member
      */

    lazy val program: Parser[PProgram] =
      (packageClause <~ eos) ~ importDecls ~ members ^^ {
        case pkgClause ~ importDecls ~ members =>
          PProgram(pkgClause, importDecls.flatten, members.flatten)
      }

    lazy val packageClause: Parser[PPackageClause] =
      "package" ~> pkgDef ^^ PPackageClause

    lazy val importDecls: Parser[Vector[Vector[PImport]]] =
      (importDecl <~ eos).*

    lazy val members: Parser[Vector[Vector[PMember]]] =
      (member <~ eos).*

    lazy val importDecl: Parser[Vector[PImport]] =
      ("import" ~> importSpec ^^ (decl => Vector(decl))) |
        ("import" ~> "(" ~> repsep(importSpec, eos) <~ eos.? <~ ")")

    lazy val importSpec: Parser[PImport] =
      unqualifiedImportSpec | qualifiedImportSpec

    lazy val unqualifiedImportSpec: Parser[PUnqualifiedImport] =
      "." ~> idnImportPath ^^ PUnqualifiedImport

    lazy val qualifiedImportSpec: Parser[PQualifiedImport] =
      idnDefLike.? ~ idnImportPath ^^ {
        case Some(id) ~ pkg => PExplicitQualifiedImport(id, pkg)
        case None ~ pkg => PImplicitQualifiedImport(pkg)
      }

    lazy val member: Parser[Vector[PMember]] =
      (methodDecl | functionDecl) ^^ (Vector(_)) |
      constDecl | varDecl | typeDecl | ghostMember

    lazy val declarationStmt: Parser[PStatement] =
      (constDecl | varDecl | typeDecl) ^^ PSeq

    lazy val constDecl: Parser[Vector[PConstDecl]] =
      "const" ~> constSpec ^^ (decl => Vector(decl)) |
        "const" ~> "(" ~> (constSpec <~ eos).* <~ ")"

    lazy val constSpec: Parser[PConstDecl] =
      rep1sep(idnDefLike, ",") ~ (typ.? ~ ("=" ~> rep1sep(expression, ","))).? ^^ {
        case left ~ None => PConstDecl(None, Vector.empty, left)
        case left ~ Some(t ~ right) => PConstDecl(t, right, left)
      }

    lazy val varDecl: Parser[Vector[PVarDecl]] =
      "var" ~> varSpec ^^ (decl => Vector(decl)) |
        "var" ~> "(" ~> (varSpec <~ eos).* <~ ")"

    lazy val varSpec: Parser[PVarDecl] =
      rep1sep(maybeAddressableIdn(idnDefLike), ",") ~ typ ~ ("=" ~> rep1sep(expression, ",")).? ^^ {
        case left ~ t ~ None =>
          val (vars, addressable) = left.unzip
          PVarDecl(Some(t), Vector.empty, vars, addressable)
        case left ~ t ~ Some(right) =>
          val (vars, addressable) = left.unzip
          PVarDecl(Some(t), right, vars, addressable)
      } |
        (rep1sep(maybeAddressableIdn(idnDefLike), ",") <~ "=") ~ rep1sep(expression, ",") ^^ {
          case left ~ right =>
            val (vars, addressable) = left.unzip
            PVarDecl(None, right, vars, addressable)
        }

    lazy val typeDecl: Parser[Vector[PTypeDecl]] =
      "type" ~> typeSpec ^^ (decl => Vector(decl)) |
        "type" ~> "(" ~> (typeSpec <~ eos).* <~ ")"

    lazy val typeSpec: Parser[PTypeDecl] =
      typeDefSpec | typeAliasSpec

    lazy val typeDefSpec: Parser[PTypeDef] =
      idnDef ~ typ ^^ { case left ~ right => PTypeDef(right, left)}

    lazy val typeAliasSpec: Parser[PTypeAlias] =
      (idnDef <~ "=") ~ typ ^^ { case left ~ right => PTypeAlias(right, left)}

    lazy val functionDecl: Parser[PFunctionDecl] =
      functionSpec ~ ("func" ~> idnDef) ~ signature ~ specOnlyParser(blockWithBodyParameterInfo) ^^ {
        case spec ~ name ~ sig ~ body =>
          PFunctionDecl(name, sig._1, sig._2, spec, body)
      }

    lazy val functionSpec: Parser[PFunctionSpec] =
      ("requires" ~> expression <~ eos).* ~ ("ensures" ~> expression <~ eos).* ~ "pure".? ^^ {
        case pres ~ posts ~ isPure => PFunctionSpec(pres, posts, isPure.nonEmpty)
      }

    lazy val methodDecl: Parser[PMethodDecl] =
      functionSpec ~ ("func" ~> receiver) ~ idnDef ~ signature ~ specOnlyParser(blockWithBodyParameterInfo) ^^ {
        case spec ~ rcv ~ name ~ sig ~ body => PMethodDecl(name, rcv, sig._1, sig._2, spec, body)
      }

    /**
      * Statements
      */

    lazy val statement: Parser[PStatement] =
      ghostStatement |
      declarationStmt |
        goStmt |
        deferStmt |
        returnStmt |
        controlStmt |
        ifStmt |
        anyForStmt |
        exprSwitchStmt |
        typeSwitchStmt |
        selectStmt |
        block |
        simpleStmt |
        labeledStmt |
        expressionStmt |
        emptyStmt


    lazy val simpleStmt: Parser[PSimpleStmt] =
      sendStmt | assignmentWithOp | assignment | shortVarDecl // expressionStmt is parsed separately

    lazy val simpleStmtWithEmpty: Parser[PSimpleStmt] =
      simpleStmt | emptyStmt

    lazy val emptyStmt: Parser[PEmptyStmt] = /* parse last because always succeeds */
      success(PEmptyStmt())

    lazy val expressionStmt: Parser[PExpressionStmt] =
      expression ^^ PExpressionStmt

    lazy val sendStmt: Parser[PSendStmt] =
      (expression <~ "<-") ~ expression ^^ PSendStmt

    lazy val assignment: Parser[PAssignment] =
      (rep1sep(assignee, ",") <~ "=") ~ rep1sep(expression, ",") ^^ { case left ~ right => PAssignment(right, left) }

    lazy val assignmentWithOp: Parser[PAssignmentWithOp] =
      nonBlankAssignee ~ (assOp <~ "=") ~ expression ^^ { case left ~ op ~ right => PAssignmentWithOp(right, op, left) }  |
        nonBlankAssignee <~ "++" ^^ (e => PAssignmentWithOp(PIntLit(1).at(e), PAddOp().at(e), e).at(e)) |
        nonBlankAssignee <~ "--" ^^ (e => PAssignmentWithOp(PIntLit(1).at(e), PSubOp().at(e), e).at(e))

    lazy val assOp: Parser[PAssOp] =
      "+" ^^^ PAddOp() |
        "-" ^^^ PSubOp() |
        "*" ^^^ PMulOp() |
        "/" ^^^ PDivOp() |
        "%" ^^^ PModOp()

    lazy val nonBlankAssignee: Parser[PAssignee] =
      selection | indexedExp | dereference | namedOperand

    lazy val assignee: Parser[PAssignee] =
      nonBlankAssignee | blankIdentifier

    lazy val blankIdentifier: Parser[PAssignee] = "_" ^^^ PBlankIdentifier()

    lazy val shortVarDecl: Parser[PShortVarDecl] =
      (rep1sep(maybeAddressableIdn(idnUnkLike), ",") <~ ":=") ~ rep1sep(expression, ",") ^^ {
        case lefts ~ rights =>
          val (vars, addressable) = lefts.unzip
          PShortVarDecl(rights, vars, addressable)
      }

    lazy val labeledStmt: Parser[PLabeledStmt] =
      (labelDef <~ ":") ~ statement.? ^^ {
        case id ~ Some(s) => PLabeledStmt(id, s)
        case id ~ None    => PLabeledStmt(id, PEmptyStmt().at(id))
      }

    lazy val returnStmt: Parser[PReturn] =
      "return" ~> repsep(expression, ",") ^^ PReturn

    lazy val goStmt: Parser[PGoStmt] =
      "go" ~> expression ^^ PGoStmt

    lazy val controlStmt: Parser[PStatement] =
      breakStmt | continueStmt | gotoStmt

    lazy val breakStmt: Parser[PBreak] =
      "break" ~> labelUse.? ^^ PBreak

    lazy val continueStmt: Parser[PContinue] =
      "continue" ~> labelUse.? ^^ PContinue

    lazy val gotoStmt: Parser[PGoto] =
      "goto" ~> labelDef ^^ PGoto

    lazy val deferStmt: Parser[PDeferStmt] =
      "defer" ~> expression ^^ PDeferStmt

    lazy val block: Parser[PBlock] =
      "{" ~> repsep(statement, eos) <~ eos.? <~ "}" ^^ PBlock

    lazy val blockWithoutBraces: Parser[PBlock] =
      repsep(statement, eos) <~ eos.? ^^ PBlock

    lazy val blockWithBodyParameterInfo: Parser[(PBodyParameterInfo, PBlock)] =
      "{" ~> bodyParameterInfo ~ blockWithoutBraces <~ "}"

    lazy val bodyParameterInfo: Parser[PBodyParameterInfo] =
      Constants.SHARE_PARAMETER_KEYWORD ~> repsep(idnUse, ",") <~ eos.? ^^ PBodyParameterInfo |
        success(PBodyParameterInfo(Vector.empty))

    lazy val ifStmt: Parser[PIfStmt] =
      ifClause ~ ("else" ~> ifStmt) ^^ { case clause ~ PIfStmt(ifs, els) => PIfStmt(clause +: ifs, els) } |
        ifClause ~ ("else" ~> block).? ^^ { case clause ~ els => PIfStmt(Vector(clause), els) }

    lazy val ifClause: Parser[PIfClause] =
      ("if" ~> (simpleStmt <~ ";").?) ~ expression ~ block ^^ PIfClause

    lazy val exprSwitchStmt: Parser[PExprSwitchStmt] =
      ("switch" ~> (simpleStmt <~ ";").?) ~ pos(expression.?) ~ ("{" ~> exprSwitchClause.* <~ "}") ^^ {
        case pre ~ cond ~ clauses =>
          val cases = clauses collect { case v: PExprSwitchCase => v }
          val dflt = clauses collect { case v: PExprSwitchDflt => v.body }

          cond.get match {
            case None => PExprSwitchStmt(pre, PBoolLit(true).at(cond), cases, dflt)
            case Some(c) => PExprSwitchStmt(pre, c, cases, dflt)
          }
      }

    lazy val exprSwitchClause: Parser[PExprSwitchClause] =
      exprSwitchCase | exprSwitchDflt

    lazy val exprSwitchCase: Parser[PExprSwitchCase] =
      ("case" ~> rep1sep(expression, ",") <~ ":") ~ pos((statement <~ eos).*) ^^ {
        case guards ~ stmts => PExprSwitchCase(guards, PBlock(stmts.get).at(stmts))
      }

    lazy val exprSwitchDflt: Parser[PExprSwitchDflt] =
      "default" ~> ":" ~> pos((statement <~ eos).*) ^^ (stmts => PExprSwitchDflt(PBlock(stmts.get).at(stmts)))

    lazy val typeSwitchStmt: Parser[PTypeSwitchStmt] =
      ("switch" ~> (simpleStmt <~ ";").?) ~
        (idnDef <~ ":=").? ~ (primaryExp <~ "." <~ "(" <~ "type" <~ ")") ~
        ("{" ~> exprSwitchClause.* <~ "}") ^^ {
        case pre ~ binder ~ exp ~ clauses =>
          val cases = clauses collect { case v: PTypeSwitchCase => v }
          val dflt = clauses collect { case v: PTypeSwitchDflt => v.body }

          PTypeSwitchStmt(pre, exp, binder, cases, dflt)
      }

    lazy val typeSwitchClause: Parser[PTypeSwitchClause] =
      typeSwitchCase | typeSwitchDflt

    lazy val typeSwitchCase: Parser[PTypeSwitchCase] =
      ("case" ~> rep1sep(typ, ",") <~ ":") ~ pos((statement <~ eos).*) ^^ {
        case guards ~ stmts => PTypeSwitchCase(guards, PBlock(stmts.get).at(stmts))
      }

    lazy val typeSwitchDflt: Parser[PTypeSwitchDflt] =
      "default" ~> ":" ~> pos((statement <~ eos).*) ^^ (stmts => PTypeSwitchDflt(PBlock(stmts.get).at(stmts)))

    lazy val selectStmt: Parser[PSelectStmt] =
      "select" ~> "{" ~> selectClause.* <~ "}" ^^ { clauses =>
        val send = clauses collect { case v: PSelectSend => v }
        val rec = clauses collect { case v: PSelectRecv => v }
        val arec = clauses collect { case v: PSelectAssRecv => v }
        val srec = clauses collect { case v: PSelectShortRecv => v }
        val dflt = clauses collect { case v: PSelectDflt => v }

        PSelectStmt(send, rec, arec, srec, dflt)
      }

    lazy val selectClause: Parser[PSelectClause] =
      selectDflt | selectShortRecv | selectAssRecv | selectRecv

    lazy val selectRecv: Parser[PSelectRecv] =
      ("case" ~> receiveExp <~ ":") ~ pos((statement <~ eos).*) ^^ {
        case receive ~ stmts => PSelectRecv(receive, PBlock(stmts.get).at(stmts))
      }

    lazy val selectAssRecv: Parser[PSelectAssRecv] =
      ("case" ~> rep1sep(assignee, ",") <~ "=") ~ (receiveExp <~ ":") ~ pos((statement <~ eos).*) ^^ {
        case receive ~ left ~ stmts => PSelectAssRecv(left, receive, PBlock(stmts.get).at(stmts))
      }

    lazy val selectShortRecv: Parser[PSelectShortRecv] =
      ("case" ~> rep1sep(idnUnk, ",") <~ ":=") ~ (receiveExp <~ ":") ~ pos((statement <~ eos).*) ^^ {
        case left ~ receive ~ stmts => PSelectShortRecv(receive, left, PBlock(stmts.get).at(stmts))
      }

    lazy val selectSend: Parser[PSelectSend] =
      ("case" ~> sendStmt <~ ":") ~ pos((statement <~ eos).*) ^^ {
        case send ~ stmts => PSelectSend(send, PBlock(stmts.get).at(stmts))
      }

    lazy val selectDflt: Parser[PSelectDflt] =
      "default" ~> ":" ~> pos((statement <~ eos).*) ^^ (stmts => PSelectDflt(PBlock(stmts.get).at(stmts)))

    lazy val anyForStmt: Parser[PStatement] =
      forStmt | assForRange | shortForRange

    lazy val forStmt: Parser[PForStmt] =
      loopSpec ~ pos("for") ~ block ^^ { case spec ~ pos ~ b => PForStmt(None, PBoolLit(true).at(pos), None, spec, b) } |
        loopSpec ~ ("for" ~> simpleStmt.? <~ ";") ~ (pos(expression.?) <~ ";") ~ simpleStmt.? ~ block ^^ {
          case spec ~ pre ~ (pos@PPos(None)) ~ post ~ body => PForStmt(pre, PBoolLit(true).at(pos), post, spec, body)
          case spec ~ pre ~ PPos(Some(cond)) ~ post ~ body => PForStmt(pre, cond, post, spec, body)
        } |
        loopSpec ~ ("for" ~> expression) ~ block ^^ {
          case spec ~ cond ~ body => PForStmt(None, cond, None, spec, body)
        }


    lazy val loopSpec: Parser[PLoopSpec] =
      ("invariant" ~> expression <~ eos).* ^^ PLoopSpec

    lazy val assForRange: Parser[PAssForRange] =
      ("for" ~> rep1sep(assignee, ",") <~ "=") ~ ("range" ~> expression) ~ block ^^
        { case lefts ~ exp ~ bod => PAssForRange(PRange(exp).at(exp), lefts, bod) }

    lazy val shortForRange: Parser[PShortForRange] =
      ("for" ~> rep1sep(idnUnk, ",") <~ ":=") ~ ("range" ~> expression) ~ block ^^
        { case lefts ~ exp ~ bod => PShortForRange(PRange(exp).at(exp), lefts, bod) }

    /**
      * Expressions
      */

    lazy val expression: Parser[PExpression] =
      precedence1

    lazy val precedence1: PackratParser[PExpression] = /* Right-associative */
      precedence1P5 ~ ("?" ~> precedence1 <~ ":") ~ precedence1 ^^ PConditional |
        precedence1P5

    lazy val precedence1P5: PackratParser[PExpression] = /* Right-associative */
      precedence2 ~ ("==>" ~> precedence1P5) ^^ PImplication |
        precedence2

    lazy val precedence2: PackratParser[PExpression] = /* Left-associative */
      precedence2 ~ ("||" ~> precedence3) ^^ POr |
        precedence3

    lazy val precedence3: PackratParser[PExpression] = /* Left-associative */
      precedence3 ~ ("&&" ~> precedence4) ^^ PAnd |
        precedence4

    lazy val precedence4: PackratParser[PExpression] = /* Left-associative */
      ((typ <~ guard("==")) | precedence4) ~ ("==" ~> (typMinusExpr | precedence4P1)) ^^ PEquals |
          ((typ <~ guard("!=")) | precedence4) ~ ("!=" ~> (typMinusExpr | precedence4P1)) ^^ PUnequals |
        // note that `<-` should not be parsed as PLess with PSub on the right-hand side as it is the receive channel operator
        precedence4 ~ (s"<$singleWhitespaceChar".r ~> precedence4P1) ^^ PLess |
        precedence4 ~ ("<" ~> not("-") ~> precedence4P1) ^^ PLess |
        precedence4 ~ ("<=" ~> precedence4P1) ^^ PAtMost |
        precedence4 ~ (">" ~> precedence4P1) ^^ PGreater |
        precedence4 ~ (">=" ~> precedence4P1) ^^ PAtLeast |
        precedence4P1

    lazy val precedence4P1 : PackratParser[PExpression] = /* Left-associative */
      precedence4P1 ~ ("in" ~> precedence4P2) ^^ PIn |
        precedence4P1 ~ ("#" ~> precedence4P2) ^^ PMultiplicity |
        precedence4P1 ~ ("subset" ~> precedence4P2) ^^ PSubset |
        precedence4P2

    lazy val precedence4P2 : PackratParser[PExpression] = /* Left-associative */
      precedence4P2 ~ ("union" ~> precedence5) ^^ PUnion |
        precedence4P2 ~ ("intersection" ~> precedence5) ^^ PIntersection |
        precedence4P2 ~ ("setminus" ~> precedence5) ^^ PSetMinus |
        precedence5

    lazy val precedence5 : PackratParser[PExpression] = /* Left-associative */
      precedence5 ~ ("++" ~> precedence6) ^^ PSequenceAppend |
        precedence5 ~ ("+" ~> precedence6) ^^ PAdd |
        precedence5 ~ ("-" ~> precedence6) ^^ PSub |
        precedence6

    lazy val precedence6: PackratParser[PExpression] = /* Left-associative */
      precedence6 ~ ("*" ~> precedence7) ^^ PMul |
        precedence6 ~ ("/" ~> precedence7) ^^ PDiv |
        precedence6 ~ ("%" ~> precedence7) ^^ PMod |
        precedence7

    lazy val precedence7: PackratParser[PExpression] =
      unaryExp

    // expressionOrType version



    lazy val unaryExp: Parser[PExpression] =
      "+" ~> unaryExp ^^ (e => PAdd(PIntLit(0).at(e), e)) |
        "-" ~> unaryExp ^^ (e => PSub(PIntLit(0).at(e), e)) |
        "!" ~> unaryExp ^^ PNegation |
        reference |
        dereference |
        receiveExp |
        unfolding |
        make |
        newExp |
        len |
        cap |
        keys |
        values |
        ghostUnaryExp |
        primaryExp

    lazy val make : Parser[PMake] =
      "make" ~> ("(" ~> typ ~ expSequence <~ ")") ^^ PMake

    lazy val expSequence: Parser[Vector[PExpression]] =
      ("," ~> rep1sep(expression, ",") <~ ",".?).? ^^ (opt => opt.getOrElse(Vector.empty))

    lazy val newExp : Parser[PNew] =
      "new" ~> ("(" ~> typ <~ ")") ^^ PNew

    lazy val len : Parser[PLength] =
      "len" ~> ("(" ~> expression <~ ")") ^^ PLength

    lazy val cap : Parser[PCapacity] =
      "cap" ~> ("(" ~> expression <~ ")") ^^ PCapacity

    lazy val keys : Parser[PMapKeys] =
      "domain" ~> ("(" ~> expression <~ ")") ^^ PMapKeys

    lazy val values : Parser[PMapValues] =
      "range" ~> ("(" ~> expression <~ ")") ^^ PMapValues

    lazy val reference: Parser[PReference] =
      "&" ~> unaryExp ^^ PReference

    lazy val dereference: Parser[PDeref] =
      "*" ~> unaryExp ^^ PDeref

    lazy val receiveExp: Parser[PReceive] =
      "<-" ~> unaryExp ^^ PReceive

    lazy val unfolding: Parser[PUnfolding] =
      "unfolding" ~> predicateAccess ~ ("in" ~> expression) ^^ PUnfolding

    lazy val ghostUnaryExp : Parser[PGhostExpression] =
      "|" ~> expression <~ "|" ^^ PCardinality

    lazy val sequenceConversion : Parser[PSequenceConversion] =
      "seq" ~> ("(" ~> expression <~ ")") ^^ PSequenceConversion

    lazy val setConversion : Parser[PSetConversion] =
      "set" ~> ("(" ~> expression <~ ")") ^^ PSetConversion

    lazy val multisetConversion : Parser[PMultisetConversion] =
      "mset" ~> ("(" ~> expression <~ ")") ^^ PMultisetConversion

    lazy val primaryExp: Parser[PExpression] =
      conversion |
        call |
        predConstruct |
        selection |
        indexedExp |
        sliceExp |
        seqUpdExp |
        typeAssertion |
        ghostPrimaryExp |
        operand

    // TODO: change delimiters to { and } and implement required ambiguity resolution
    // current format: declaredPred!<d1, ..., dn!>
    lazy val fpredConstruct: Parser[PPredConstructor] =
      (idnUse ~ predConstructArgs) ^^ {
        case identifier ~ args => PPredConstructor(PFPredBase(identifier).at(identifier), args)
      }

    lazy val mpredConstruct: Parser[PPredConstructor] =
      selection ~ predConstructArgs ^^ {
        case recvWithId ~ args => PPredConstructor(PDottedBase(recvWithId).at(recvWithId), args)
      }

    lazy val predConstruct: Parser[PPredConstructor] =
      mpredConstruct | fpredConstruct

    lazy val predConstructArgs: Parser[Vector[Option[PExpression]]] =
      ("!<" ~> (rep1sep(predConstructArg, ",") <~ ",".?).? <~ "!>") ^^ (opt => opt.getOrElse(Vector.empty))

    lazy val predConstructArg: Parser[Option[PExpression]] =
      (expression ^^ Some[PExpression]) | ("_" ^^^ None)

    lazy val conversion: Parser[PInvoke] =
      typ ~ ("(" ~> expression <~ ",".? <~ ")") ^^ {
        case t ~ e => PInvoke(t, Vector(e))
      }

    lazy val call: PackratParser[PInvoke] =
      primaryExp ~ callArguments ^^ PInvoke

    lazy val callArguments: Parser[Vector[PExpression]] = {
      val parseArg: Parser[PExpression] = expression ~ "...".? ^^ {
        case exp ~ None => exp
        case exp ~ Some(_) => PUnpackSlice(exp)
      }
      ("(" ~> (rep1sep(parseArg, ",") <~ ",".?).? <~ ")") ^^ (opt => opt.getOrElse(Vector.empty))
    }

    lazy val selection: PackratParser[PDot] =
      primaryExp ~ ("." ~> idnUse) ^^ PDot |
      typ ~ ("." ~> idnUse) ^^ PDot

    lazy val idBasedSelection: Parser[PDot] =
      nestedIdnUse ~ ("." ~> idnUse) ^^ {
        case base ~ field => PDot(PNamedOperand(base).at(base), field)
      }

    lazy val indexedExp: PackratParser[PIndexedExp] =
      primaryExp ~ ("[" ~> expression <~ "]") ^^ PIndexedExp

    lazy val sliceExp: PackratParser[PSliceExp] =
      primaryExp ~ ("[" ~> expression.?) ~ (":" ~> expression.?) ~ ((":" ~> expression).? <~ "]") ^^ PSliceExp

    lazy val seqUpdExp : PackratParser[PGhostCollectionUpdate] =
      primaryExp ~ ("[" ~> rep1sep(seqUpdClause, ",") <~ "]") ^^ PGhostCollectionUpdate

    lazy val seqUpdClause : Parser[PGhostCollectionUpdateClause] =
      expression ~ ("=" ~> expression) ^^ PGhostCollectionUpdateClause

    lazy val typeAssertion: PackratParser[PTypeAssertion] =
      primaryExp ~ ("." ~> "(" ~> typ <~ ")") ^^ PTypeAssertion

    lazy val operand: Parser[PExpression] =
      literal | namedOperand | "(" ~> expression <~ ")"

    lazy val namedOperand: Parser[PNamedOperand] =
      idnUse ^^ PNamedOperand

    lazy val literal: Parser[PLiteral] =
      basicLit | compositeLit | functionLit

    lazy val basicLit: Parser[PBasicLiteral] =
      "true" ^^^ PBoolLit(true) |
        "false" ^^^ PBoolLit(false) |
        "nil" ^^^ PNilLit() |
        regex("[0-9]+".r) ^^ (lit => PIntLit(BigInt(lit))) |
        stringLit

    lazy val stringLit: Parser[PStringLit] =
      rawStringLit | interpretedStringLit

    lazy val rawStringLit: Parser[PStringLit] =
      // unicode characters and newlines are allowed
      "`" ~> "[^`]*".r <~ "`" ^^ (lit => PStringLit(lit))

    lazy val interpretedStringLit: Parser[PStringLit] =
    // unicode values and byte values are allowed
      "\"" ~> """(?:\\"|[^"\n])*""".r <~ "\"" ^^ (lit => PStringLit(lit))

    lazy val compositeLit: Parser[PCompositeLit] =
      literalType ~ literalValue ^^ PCompositeLit

    lazy val literalValue: Parser[PLiteralValue] =
      "{" ~> (rep1sep(keyedElement, ",") <~ ",".?).? <~ "}" ^^ {
        case None => PLiteralValue(Vector.empty)
        case Some(ps) => PLiteralValue(ps)
      }

    lazy val keyedElement: Parser[PKeyedElement] =
      (compositeKey <~ ":").? ~ compositeVal ^^ PKeyedElement

    lazy val compositeKey: Parser[PCompositeKey] =
      compositeVal ^^ {
        case n@ PExpCompositeVal(PNamedOperand(id)) => PIdentifierKey(id).at(n)
        case n => n
      }

    lazy val compositeVal: Parser[PCompositeVal] =
      expCompositeLiteral | litCompositeLiteral

    lazy val expCompositeLiteral: Parser[PExpCompositeVal] =
      expression ^^ PExpCompositeVal

    lazy val litCompositeLiteral: Parser[PLitCompositeVal] =
      literalValue ^^ PLitCompositeVal

    lazy val functionLit: Parser[PFunctionLit] =
      "func" ~> signature ~ block ^^ { case sig ~ body => PFunctionLit(sig._1, sig._2, body) }



    /**
      * Types
      */

    lazy val typMinusExpr: Parser[PType] =
      (
        ("(" ~> typMinusExpr <~ ")") |
          ("*" ~> typMinusExpr ^^ PDeref) |
          sliceType | arrayType | mapType | channelType | functionType | structType | interfaceType | predType |
          sequenceType | setType | multisetType | optionType | domainType |
          predeclaredType
        ) <~ not("(" | "{")

    lazy val typ : Parser[PType] =
      "(" ~> typ <~ ")" | typeLit | qualifiedType | namedType | ghostTypeLit

    lazy val ghostTyp : Parser[PGhostType] =
      "(" ~> ghostTyp <~ ")" | ghostTypeLit

    lazy val typeLit: Parser[PTypeLit] =
      pointerType | sliceType | arrayType | mapType |
        channelType | functionType | structType | interfaceType | predType

    lazy val ghostTypeLit : Parser[PGhostLiteralType] =
      sequenceType | setType | multisetType | dictType | optionType | domainType  | ghostSliceType

    lazy val pointerType: Parser[PDeref] =
      "*" ~> typ ^^ PDeref

    lazy val sliceType: Parser[PSliceType] =
      ("[" ~ "]") ~> typ ^^ PSliceType

    lazy val ghostSliceType: Parser[PGhostSliceType] =
      "ghost" ~> ("[" ~ "]") ~> typ ^^ PGhostSliceType

    lazy val mapType: Parser[PMapType] =
      ("map" ~> ("[" ~> typ <~ "]")) ~ typ ^^ PMapType

    lazy val channelType: Parser[PChannelType] =
      ("chan" ~> "<-") ~> typ ^^ PSendChannelType |
        ("<-" ~> "chan") ~> typ ^^ PRecvChannelType |
        "chan" ~> typ ^^ PBiChannelType

    lazy val functionType: Parser[PFunctionType] =
      "func" ~> signature ^^ PFunctionType.tupled

    lazy val predType: Parser[PPredType] =
      "pred" ~> predTypeParams ^^ PPredType

    lazy val predTypeParams: Parser[Vector[PType]] =
      "(" ~> (rep1sep(typ, ",") <~ ",".?).? <~ ")" ^^ (_.toVector.flatten)

    lazy val arrayType: Parser[PArrayType] =
      ("[" ~> expression <~ "]") ~ typ ^^ PArrayType

    lazy val sequenceType : Parser[PSequenceType] =
      "seq" ~> ("[" ~> typ <~ "]") ^^ PSequenceType

    lazy val setType : Parser[PSetType] =
      "set" ~> ("[" ~> typ <~ "]") ^^ PSetType

    lazy val multisetType : Parser[PMultisetType] =
      "mset" ~> ("[" ~> typ <~ "]") ^^ PMultisetType

    lazy val dictType: Parser[PMathematicalMapType] =
      ("dict" ~> ("[" ~> typ <~ "]")) ~ typ ^^ PMathematicalMapType

    lazy val optionType : Parser[POptionType] =
      "option" ~> ("[" ~> typ <~ "]") ^^ POptionType

    lazy val domainType: Parser[PDomainType] =
      "domain" ~> "{" ~> repsep(domainClause, eos) <~ eos.? <~ "}" ^^ { clauses =>
        val funcs = clauses.collect{ case x: PDomainFunction => x }
        val axioms = clauses.collect{ case x: PDomainAxiom => x }
        PDomainType(funcs, axioms)
      }

    lazy val domainClause: Parser[PDomainClause] =
      "func" ~> idnDef ~ signature ^^ { case id ~ sig => PDomainFunction(id, sig._1, sig._2) } |
      "axiom" ~> "{" ~> expression <~ eos.? <~ "}" ^^ PDomainAxiom

    lazy val structType: Parser[PStructType] =
      "struct" ~> "{" ~> repsep(structClause, eos) <~ eos.? <~ "}" ^^ PStructType

    lazy val structClause: Parser[PStructClause] =
      fieldDecls | embeddedDecl

    lazy val embeddedDecl: Parser[PEmbeddedDecl] =
      embeddedType ^^ (et => PEmbeddedDecl(et, PIdnDef(et.name).at(et)))

    lazy val fieldDecls: Parser[PFieldDecls] =
      rep1sep(idnDef, ",") ~ typ ^^ { case ids ~ t =>
        PFieldDecls(ids map (id => PFieldDecl(id, t.copy).at(id)))
      }

    lazy val interfaceType: Parser[PInterfaceType] =
      "interface" ~> "{" ~> (interfaceClause <~ eos).* <~ "}" ^^ { clauses =>
        val embedded = clauses collect { case v: PInterfaceName => v }
        val methodDecls = clauses collect { case v: PMethodSig => v }
        val predicateDecls = clauses collect { case v: PMPredicateSig => v }

        PInterfaceType(embedded, methodDecls, predicateDecls)
      }

    lazy val interfaceClause: Parser[PInterfaceClause] =
      predicateSpec | methodSpec | interfaceName

    lazy val interfaceName: Parser[PInterfaceName] =
      declaredType ^^ PInterfaceName

    lazy val methodSpec: Parser[PMethodSig] =
      "ghost".? ~ functionSpec ~ idnDef ~ signature ^^ { case isGhost ~ spec ~ id ~ sig => PMethodSig(id, sig._1, sig._2, spec, isGhost.isDefined) }

    lazy val predicateSpec: Parser[PMPredicateSig] =
      ("pred" ~> idnDef) ~ parameters ^^ PMPredicateSig


    lazy val namedType: Parser[PNamedType] =
      predeclaredType |
        declaredType

    lazy val predeclaredType: Parser[PPredeclaredType] =
      exactWord("bool") ^^^ PBoolType() |
        exactWord("string") ^^^ PStringType() |
        exactWord("perm") ^^^ PPermissionType() |
        // signed integer types
        exactWord("rune") ^^^ PRune() |
        exactWord("int") ^^^ PIntType() |
        exactWord("int8") ^^^ PInt8Type() |
        exactWord("int16") ^^^ PInt16Type() |
        exactWord("int32") ^^^ PInt32Type() |
        exactWord("int64") ^^^ PInt64Type() |
        // unsigned integer types
        exactWord("byte") ^^^ PByte() |
        exactWord("uint") ^^^ PUIntType() |
        exactWord("uint8") ^^^ PUInt8Type() |
        exactWord("uint16") ^^^ PUInt16Type() |
        exactWord("uint32") ^^^ PUInt32Type() |
        exactWord("uint64") ^^^ PUInt64Type() |
        exactWord("uintptr") ^^^ PUIntPtr()

    lazy val predeclaredTypeSeparate: Parser[PPredeclaredType] =
      exactWord("bool") ~ not("(" | ".") ^^^ PBoolType() |
        exactWord("string") ~ not("(" | ".") ^^^ PStringType() |
        exactWord("perm") ~ not("(" | ".") ^^^ PPermissionType() |
        // signed integer types
        exactWord("rune") ~ not("(" | ".") ^^^ PRune() |
        exactWord("int") ~ not("(" | ".") ^^^ PIntType() |
        exactWord("int8") ~ not("(" | ".") ^^^ PInt8Type() |
        exactWord("int16") ~ not("(" | ".") ^^^ PInt16Type() |
        exactWord("int32") ~ not("(" | ".") ^^^ PInt32Type() |
        exactWord("int64") ~ not("(" | ".") ^^^ PInt64Type() |
        // unsigned integer types
        exactWord("byte") ~ not("(" | ".") ^^^ PByte() |
        exactWord("uint") ~ not("(" | ".") ^^^ PUIntType() |
        exactWord("uint8") ~ not("(" | ".") ^^^ PUInt8Type() |
        exactWord("uint16") ~ not("(" | ".") ^^^ PUInt16Type() |
        exactWord("uint32") ~ not("(" | ".") ^^^ PUInt32Type() |
        exactWord("uint64") ~ not("(" | ".") ^^^ PUInt64Type() |
        exactWord("uintptr") ~ not("(" | ".") ^^^ PUIntPtr()

    private def exactWord(s: String): Regex = ("\\b" ++ s ++ "\\b").r

    lazy val qualifiedType: Parser[PDot] =
      declaredType ~ ("." ~> idnUse) ^^ PDot

    lazy val declaredType: Parser[PNamedOperand] =
      idnUse ^^ PNamedOperand

    lazy val literalType: Parser[PLiteralType] =
      sliceType |
        arrayType |
        implicitSizeArrayType |
        mapType |
        structType |
        qualifiedType |
        ghostTypeLit |
        declaredType

    lazy val implicitSizeArrayType: Parser[PImplicitSizeArrayType] =
      "[" ~> "..." ~> "]" ~> typ ^^ PImplicitSizeArrayType


    /**
      * Misc
      */

    lazy val receiver: PackratParser[PReceiver] =
      "(" ~> maybeAddressableIdnDef.? ~ methodRecvType <~ ")" ^^ {
        case None ~ t => PUnnamedReceiver(t)
        case Some((name, addressable)) ~ t => PNamedReceiver(name, t, addressable)
      }

    lazy val signature: Parser[(Vector[PParameter], PResult)] =
      parameters ~ result


    lazy val result: PackratParser[PResult] =
      parameters ^^ PResult |
        typ ^^ (t => PResult(Vector(PUnnamedParameter(t).at(t)))) |
        success(PResult(Vector.empty))

    lazy val parameters: Parser[Vector[PParameter]] =
      "(" ~> (parameterList <~ ",".?).? <~ ")" ^^ {
        case None => Vector.empty
        case Some(ps) => ps
      }

    lazy val parameterList: Parser[Vector[PParameter]] =
      rep1sep(parameterDecl, ",") ^^ Vector.concat

    lazy val parameterDecl: Parser[Vector[PParameter]] = {
      val namedParam = rep1sep(idnDef, ",") ~ "...".? ~ typ ^^ {
        case ids ~ variadicOpt ~ t =>
          ids map { id =>
            val typ = if (variadicOpt.isDefined) PVariadicType(t.copy) else t.copy
            PNamedParameter(id, typ).at(id)
          }
      }
      val unnamedParam = ("...".? ~ typ) ^^ {
        case variadicOpt ~ t =>
          val typ = if (variadicOpt.isDefined) PVariadicType(t) else t
          Vector(PUnnamedParameter(typ).at(t))
      }

      ghostParameter | namedParam | unnamedParam
    }


    lazy val nestedIdnUse: PackratParser[PIdnUse] =
      "(" ~> nestedIdnUse <~ ")" | idnUse

    lazy val embeddedType: PackratParser[PEmbeddedType] =
      "(" ~> embeddedType <~ ")" |
        "*".? ~ namedType ^^ {
          case None ~ t => PEmbeddedName(t)
          case _ ~ t => PEmbeddedPointer(t)
        }


    lazy val methodRecvType: PackratParser[PMethodRecvType] =
      "(" ~> methodRecvType <~ ")" |
        "*".? ~ declaredType ^^ {
          case None ~ t => PMethodReceiveName(t)
          case _ ~ t => PMethodReceivePointer(t)
        }

    /**
      * Identifiers
      */

    lazy val idnDef: Parser[PIdnDef] = identifier ^^ PIdnDef
    lazy val idnUse: Parser[PIdnUse] = identifier ^^ PIdnUse
    lazy val idnUnk: Parser[PIdnUnk] = identifier ^^ PIdnUnk

    def maybeAddressableIdn[T <: PIdnNode](p: Parser[T]): Parser[(T, Boolean)] =
      p ~ addressabilityMod.? ^^ { case id ~ opt => (id, opt.isDefined) }

    lazy val maybeAddressableIdnDef: Parser[(PIdnDef, Boolean)] = maybeAddressableIdn(idnDef)
    lazy val maybeAddressableIdnUnk: Parser[(PIdnUnk, Boolean)] = maybeAddressableIdn(idnUnk)

    lazy val idnDefLike: Parser[PDefLikeId] = idnDef | wildcard
    lazy val idnUseLike: Parser[PUseLikeId] = idnUse | wildcard
    lazy val idnUnkLike: Parser[PUnkLikeId] = idnUnk | wildcard

    lazy val labelDef: Parser[PLabelDef] = identifier ^^ PLabelDef
    lazy val labelUse: Parser[PLabelUse] = identifier ^^ PLabelUse

    lazy val pkgDef: Parser[PPkgDef] = identifier ^^ PPkgDef
    lazy val pkgUse: Parser[PPkgUse] = identifier ^^ PPkgUse

    lazy val wildcard: Parser[PWildcard] = "_" ^^^ PWildcard()

    lazy val addressabilityMod: Parser[String] = Constants.ADDRESSABILITY_MODIFIER

    lazy val identifier: Parser[String] =
      // "_" is not an identifier (but a wildcard)
      "(?:_[a-zA-Z0-9_]+|[a-zA-Z][a-zA-Z0-9_]*)".r into (s => {
        if (isReservedWord(s))
          failure(s"""keyword "$s" found where identifier expected""")
        else
          success(s)
      })

    lazy val idnImportPath: Parser[String] =
      // this allows for seemingly meaningless paths such as ".......". It is not problematic that Gobra parses these
      // paths given that it will throw an error if they do not exist in the filesystem
      "\"" ~> "[.a-zA-Z0-9_/]*".r <~ "\""
      // """[^\P{L}\P{M}\P{N}\P{P}\P{S}!\"#$%&'()*,:;<=>?[\\\]^{|}\x{FFFD}]+""".r // \P resp. \p is currently not supported

    /**
      * Ghost
      */

    lazy val ghostMember: Parser[Vector[PGhostMember]] =
      fpredicateDecl ^^ (Vector(_)) |
        mpredicateDecl ^^ (Vector(_)) |
        implementationProof ^^ (Vector(_)) |
      "ghost" ~ eos.? ~> (methodDecl | functionDecl) ^^ (m => Vector(PExplicitGhostMember(m).at(m))) |
        "ghost" ~ eos.? ~> (constDecl | varDecl | typeDecl) ^^ (ms => ms.map(m => PExplicitGhostMember(m).at(m)))

    // expression can be terminated with a semicolon to simply preprocessing
    lazy val fpredicateDecl: Parser[PFPredicateDecl] =
      ("pred" ~> idnDef) ~ parameters ~ predicateBody ^^ PFPredicateDecl

    // expression can be terminated with a semicolon to simply preprocessing
    lazy val mpredicateDecl: Parser[PMPredicateDecl] =
      ("pred" ~> receiver) ~ idnDef ~ parameters ~ predicateBody ^^ {
        case rcv ~ name ~ paras ~ body => PMPredicateDecl(name, rcv, paras, body)
      }

    lazy val implementationProof: Parser[PImplementationProof] =
      (typ <~ "implements") ~ typ ~ ("{" ~> (implementationProofPredicateAlias <~ eos).* ~ (methodImplementationProof <~ eos).* <~ "}").? ^^ {
        case subT ~ superT ~ Some(predAlias ~ memberProof) =>PImplementationProof(subT, superT, predAlias, memberProof)
        case subT ~ superT ~ None => PImplementationProof(subT, superT, Vector.empty, Vector.empty)
      }

    lazy val implementationProofPredicateAlias: Parser[PImplementationProofPredicateAlias] =
      ("pred" ~> idnUse <~ ":=") ~ (selection | namedOperand) ^^ PImplementationProofPredicateAlias

    lazy val methodImplementationProof: Parser[PMethodImplementationProof] =
      "pure".? ~ nonLocalReceiver ~ idnUse ~ signature ~ blockWithBodyParameterInfo.? ^^ {
        case spec ~ recv ~ name ~ sig ~ body => PMethodImplementationProof(name, recv, sig._1, sig._2, spec.isDefined, body)
      }

    lazy val nonLocalReceiver: PackratParser[PParameter] =
      "(" ~> idnDef.? ~ typ <~ ")" ^^ {
        case None ~ t => PUnnamedParameter(t)
        case Some(id) ~ t => PNamedParameter(id, t)
      }

    lazy val predicateBody: Parser[Option[PExpression]] =
      ("{" ~> expression <~ eos.? ~ "}").?

    lazy val ghostStatement: Parser[PGhostStatement] =
      "ghost" ~> statement ^^ PExplicitGhostStatement |
      "assert" ~> expression ^^ PAssert |
      "exhale" ~> expression ^^ PExhale |
      "assume" ~> expression ^^ PAssume |
      "inhale" ~> expression ^^ PInhale |
      "fold" ~> predicateAccess ^^ PFold |
      "unfold" ~> predicateAccess ^^ PUnfold

    lazy val ghostParameter: Parser[Vector[PParameter]] = {
      val namedParam =
        "ghost" ~> rep1sep(idnDef, ",") ~ "...".? ~ typ ^^ {
          case ids ~ variadicOpt ~ t => ids map { id =>
            val typ = if (variadicOpt.isDefined) PVariadicType(t.copy) else t.copy
            PExplicitGhostParameter(PNamedParameter(id, typ).at(id)).at(id)
          }
        }

      val unnamedParam =
        "ghost" ~> "...".? ~ typ ^^ {
          case variadicOpt ~ t =>
            val typ = if (variadicOpt.isDefined) PVariadicType(t) else t
            Vector(PExplicitGhostParameter(PUnnamedParameter(typ).at(t)).at(t))
        }

      namedParam | unnamedParam
    }

    lazy val ghostPrimaryExp : Parser[PGhostExpression] =
      forall |
        exists |
        old |
        access |
        typeOf |
        isComparable |
        rangeSequence |
        rangeSet |
        rangeMultiset |
        sequenceConversion |
        setConversion |
        multisetConversion |
        optionNone | optionSome | optionGet | permission

    lazy val forall : Parser[PForall] =
      ("forall" ~> boundVariables <~ "::") ~ triggers ~ expression ^^ PForall

    lazy val exists : Parser[PExists] =
      ("exists" ~> boundVariables <~ "::") ~ triggers ~ expression ^^ PExists

    lazy val old : Parser[PGhostExpression] =
      (("old" ~> ("[" ~> labelUse <~ "]").?) ~ ("(" ~> expression <~ ")")) ^^ {
        case Some(l) ~ e => PLabeledOld(l, e)
        case None ~ e => POld(e)
      }

    lazy val access : Parser[PAccess] =
      "acc" ~> "(" ~> expression <~ ")" ^^ { exp => PAccess(exp, PFullPerm().at(exp)) } |
      // parsing wildcard permissions should be done here instead of in [[permission]] to avoid parsing "_"
      // as an expression in arbitrary parts of the code
      "acc" ~> "(" ~> expression <~ ("," ~> wildcard <~ ")") ^^ { exp => PAccess(exp, PWildcardPerm().at(exp)) } |
      "acc" ~> "(" ~> expression ~ ("," ~> expression <~ ")") ^^ PAccess

    lazy val permission: Parser[PPermission] =
      "writePerm" ^^^ PFullPerm() |
      "noPerm" ^^^ PNoPerm()

    lazy val typeOf: Parser[PTypeOf] =
      "typeOf" ~> "(" ~> expression <~ ")" ^^ PTypeOf

    lazy val isComparable: Parser[PIsComparable] =
      "isComparable" ~> "(" ~> (expression | typ) <~ ")" ^^ PIsComparable

    private lazy val rangeExprBody : Parser[PExpression ~ PExpression] =
      "[" ~> expression ~ (".." ~> expression <~ "]")

    lazy val rangeSequence : Parser[PRangeSequence] = "seq" ~> rangeExprBody ^^ PRangeSequence

    /**
      * Expressions of the form "set[`left` .. `right`]" are directly
      * transformed into "set(seq[`left` .. `right`])" (to later lift on
      * the existing type checking support for range sequences.)
      */
    lazy val rangeSet : Parser[PGhostExpression] = "set" ~> rangeExprBody ^^ {
      case left ~ right => PSetConversion(PRangeSequence(left, right).range(left, right))
    }

    /**
      * Expressions of the form "mset[`left` .. `right`]" are directly
      * transformed into "mset(seq[`left` .. `right`])" (to later lift on
      * the existing type checking support for range sequences.)
      */
    lazy val rangeMultiset : Parser[PGhostExpression] = "mset" ~> rangeExprBody ^^ {
      case left ~ right => PMultisetConversion(PRangeSequence(left, right).range(left, right))
    }

    lazy val optionNone : Parser[POptionNone] =
      "none" ~> ("[" ~> typ <~ "]") ^^ POptionNone

    lazy val optionSome : Parser[POptionSome] =
      "some" ~> ("(" ~> expression <~ ")") ^^ POptionSome

    lazy val optionGet : Parser[POptionGet] =
      "get" ~> ("(" ~> expression <~ ")") ^^ POptionGet

    lazy val predicateAccess: Parser[PPredicateAccess] =
      // call ^^ PPredicateAccess // | "acc" ~> "(" ~> call <~ ")" ^^ PPredicateAccess
      primaryExp into { // this is somehow not equivalent to `call ^^ PPredicateAccess` as the latter cannot parse "b.RectMem(&r)"
        case invoke: PInvoke => success(PPredicateAccess(invoke, PFullPerm().at(invoke)))
        case PAccess(invoke: PInvoke, perm) => success(PPredicateAccess(invoke, perm))
        case e => failure(s"expected invoke but got ${e.getClass}")
      }

    lazy val boundVariables: Parser[Vector[PBoundVariable]] =
      rep1sep(boundVariableDecl, ",") ^^ Vector.concat

    lazy val boundVariableDecl: Parser[Vector[PBoundVariable]] =
      rep1sep(idnDef, ",") ~ typ ^^ { case ids ~ t =>
        ids map (id => PBoundVariable(id, t.copy).at(id))
      }

    lazy val triggers: Parser[Vector[PTrigger]] = trigger.*

    lazy val trigger: Parser[PTrigger] =
      "{" ~> rep1sep(expression, ",") <~ "}" ^^ PTrigger


    /**
      * EOS
      */

    lazy val eos: Parser[String] =
      ";"

    def eol[T](p: => Parser[T]): Parser[T] =
      p into (r => eos ^^^ r)


    implicit class PositionedPAstNode[N <: PNode](node: N) {
      def at(other: PNode): N = {
        pom.positions.dupPos(other, node)
      }

      def range(from: PNode, to: PNode): N = {
        pom.positions.dupRangePos(from, to, node)
      }

      def copy: N = rewriter.deepclone(node)
    }

    def pos[T](p: => Parser[T]): Parser[PPos[T]] = p ^^ PPos[T]

  }

  private class PRewriter(override val positions: Positions) extends PositionedRewriter with Cloner {

  }


}
