/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

import org.apache.commons.io.FileUtils
import org.bitbucket.inkytonik.kiama.parsing.{NoSuccess, ParseResult, Parsers, Success}
import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter}
import org.bitbucket.inkytonik.kiama.util.{FileSource, Positions, Source}
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.reporting.{ParserError, VerifierError}
import viper.gobra.util.OutputUtil

object Parser {

  /**
    * Parses file and returns either the parsed program if the file was parsed successfully,
    * otherwise returns list of error messages
    *
    * @param file
    * @return
    *
    * The following transformations are performed:
    * e++  ~>  e += 1
    * e--  ~>  e -= 1
    * +e   ~>  0 + e
    * -e   ~>  0 - e
    *
    */

  def parse(file: File)(config: Config): Either[Vector[VerifierError], PProgram] = {
    parse(FileSource(file.getPath))(config)
  }

  def parseStmt(source: Source): Either[Messages, PStatement] = {
    val pom = new PositionManager
    val parsers = new SyntaxAnalyzer(pom)
    parsers.parseAll(parsers.statement, source) match {
      case Success(ast, _) =>
        Right(ast)

      case ns@NoSuccess(label, next) =>
        val pos = next.position
        pom.positions.setStart(ns, pos)
        pom.positions.setFinish(ns, pos)
        val messages = message(ns, label)
        Left(messages)
    }
  }

  def parseExpr(source: Source): Either[Messages, PExpression] = {
    val pom = new PositionManager
    val parsers = new SyntaxAnalyzer(pom)
    parsers.parseAll(parsers.expression, source) match {
      case Success(ast, _) =>
        Right(ast)

      case ns@NoSuccess(label, next) =>
        val pos = next.position
        pom.positions.setStart(ns, pos)
        pom.positions.setFinish(ns, pos)
        val messages = message(ns, label)
        Left(messages)
    }
  }

  def parseAssignee(source: Source): Either[Messages, PAssignee] = {
    val pom = new PositionManager
    val parsers = new SyntaxAnalyzer(pom)
    parsers.parseAll(parsers.assignee, source) match {
      case Success(ast, _) =>
        Right(ast)

      case ns@NoSuccess(label, next) =>
        val pos = next.position
        pom.positions.setStart(ns, pos)
        pom.positions.setFinish(ns, pos)
        val messages = message(ns, label)
        Left(messages)
    }
  }

  def parse(source: Source)(config: Config): Either[Vector[VerifierError], PProgram] = {
    val pom = new PositionManager
    val parsers = new SyntaxAnalyzer(pom)

    val res = handleParseResult(parsers.parseAll(parsers.program, source))(pom)
    res match {
      case Right(ast) if config.unparse() => {
          val outputFile = OutputUtil.postfixFile(config.inputFile(), "unparsed")
          FileUtils.writeStringToFile(
            outputFile,
            ast.formatted,
            UTF_8
          )
      }
      case _ =>
    }
    res
  }

  private def handleParseResult[T](res: ParseResult[T])(pom: PositionManager): Either[Vector[VerifierError], T] = {
    res match {
      case Success(ast, _) =>
        Right(ast)

      case ns@NoSuccess(label, next) =>
        val pos = next.position
        pom.positions.setStart(ns, pos)
        pom.positions.setFinish(ns, pos)
        val messages = message(ns, label)
        Left(pom.translate(messages, ParserError))
    }
  }

  private class SyntaxAnalyzer(pom: PositionManager) extends Parsers(pom.positions) {

    /**
      * Extends Parser by additional operators to better handle newlines
      */
    implicit class GobraParser[T](p: Parser[T]) {
      private def newline: Parser[String] = """\s""".r
      /**
        * Sequential composition allowing new lines in between.
        */
      def |~[U](q : => Parser[U]) : Parser[~[T, U]] = (p <~ newline.*) ~ q
      /**
        * Sequential composition ignoring left side and allowing new lines in between.
        */
      def |~>[U](q : => Parser[U]) : Parser[U] = (p ~ newline.*) ~> q

      /**
        * Sequential composition ignoring right side and allowing new lines in between.
        */
      def <~|[U](q : => Parser[U]) : Parser[T] = p <~ (newline.* ~ q)
    }

    lazy val rewriter = new PRewriter(pom.positions)

    override val whitespace: Parser[String] =
      """([^\S\r\n]|(//.*)|/\*(?s:.*?)\*/)*""".r
      //[^\S\r\n]: matches whitespaces but not \r and \n

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
      "case", "defer", "go", "map", "struct",
      "chan", "else", "goto", "package", "switch",
      "const", "fallthrough", "if", "range", "type",
      "continue", "for", "import", "return", "var",
      // new keywords introduced by Gobra
      "ghost", "acc", "assert", "exhale", "assume", "inhale",
      "memory", "fold", "unfold", "unfolding", "pure",
      "predicate", "old",
      // predeclared types:
      "bool", "int", "string"
    )

    def isReservedWord(word: String): Boolean = reservedWords contains word

    /**
      * Member
      */

    lazy val program: Parser[PProgram] =
      (Parser("") |~> packageClause <~ eos) ~ importDecls ~ members ^^ {
        case pkgClause ~ importDecls ~ members =>
          PProgram(pkgClause, importDecls.flatten, members.flatten, pom)
      }

    lazy val importDecls: Parser[Vector[Vector[PImportDecl]]] =
      (importDecl <~ zeroOrMoreEos).*

    lazy val members: Parser[Vector[Vector[PMember]]] =
      (member <~ zeroOrMoreEos).*

    lazy val packageClause: Parser[PPackageClause] =
      "package" ~> pkgDef ^^ PPackageClause

    lazy val importDecl: Parser[Vector[PImportDecl]] =
      ("import" ~> importSpec ^^ (decl => Vector(decl))) |
        (Parser("import") |~> "(" |~> (importSpec <~ zeroOrMoreEos).* <~| ")")

    lazy val importSpec: Parser[PImportDecl] =
      unqualifiedImportSpec | qualifiedImportSpec

    lazy val unqualifiedImportSpec: Parser[PUnqualifiedImport] =
      "." ~> idnImportPath ^^ PUnqualifiedImport

    lazy val qualifiedImportSpec: Parser[PQualifiedImport] =
      idnDef.? ~ idnImportPath ^^ {
        //case id ~ pkg => PQualifiedImport(id.getOrElse(PIdnDef(???).at(???)), pkg)
        case id ~ pkg => PQualifiedImport(id, pkg)
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
      rep1sep(idnDef, ",") ~ (typ.? ~ ("=" ~> rep1sep(expression, ","))).? ^^ {
        case left ~ None => PConstDecl(None, Vector.empty, left)
        case left ~ Some(t ~ right) => PConstDecl(t, right, left)
      }

    lazy val varDecl: Parser[Vector[PVarDecl]] =
      "var" ~> varSpec ^^ (decl => Vector(decl)) |
        "var" ~> "(" ~> (varSpec <~ eos).* <~ ")"

    lazy val varSpec: Parser[PVarDecl] =
      rep1sep(maybeAddressableIdnDef, ",") ~ typ ~ ("=" ~> rep1sep(expression, ",")).? ^^ {
        case left ~ t ~ None =>
          val (vars, addressable) = left.unzip
          PVarDecl(Some(t), Vector.empty, vars, addressable)
        case left ~ t ~ Some(right) =>
          val (vars, addressable) = left.unzip
          PVarDecl(Some(t), right, vars, addressable)
      } |
        (rep1sep(maybeAddressableIdnDef, ",") <~ "=") ~ rep1sep(expression, ",") ^^ {
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
      functionSpec ~ ("func" ~> idnDef) ~ signature ~ block.? ^^ {
        case spec ~ name ~ sig ~ body => PFunctionDecl(name, sig._1, sig._2, spec, body)
      }

    lazy val functionSpec: Parser[PFunctionSpec] =
      ("requires" ~> expression <~ eos).* ~ ("ensures" ~> expression <~ eos).* ~ "pure".? ^^ {
        case pres ~ posts ~ isPure => PFunctionSpec(pres, posts, isPure.nonEmpty)
      }

    lazy val methodDecl: Parser[PMethodDecl] =
      functionSpec ~ ("func" ~> receiver) ~ idnDef ~ signature ~ block.? ^^ {
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
        emptyStmt


    lazy val simpleStmt: Parser[PSimpleStmt] =
      sendStmt | assignmentWithOp | assignment | shortVarDecl | expressionStmt

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
      assignee ~ (assOp <~ "=") ~ expression ^^ { case left ~ op ~ right => PAssignmentWithOp(right, op, left) }  |
        assignee <~ "++" ^^ (e => PAssignmentWithOp(PIntLit(1).at(e), PAddOp().at(e), e).at(e)) |
        assignee <~ "--" ^^ (e => PAssignmentWithOp(PIntLit(1).at(e), PSubOp().at(e), e).at(e))

    lazy val assOp: Parser[PAssOp] =
      "+" ^^^ PAddOp() |
        "-" ^^^ PSubOp() |
        "*" ^^^ PMulOp() |
        "/" ^^^ PDivOp() |
        "%" ^^^ PModOp()

    lazy val assignee: PackratParser[PAssignee] =
      dot | indexedExp | dereference | namedOperand

    lazy val shortVarDecl: Parser[PShortVarDecl] =
      (rep1sep(maybeAddressableIdnUnk, ",") <~ ":=") ~ rep1sep(expression, ",") ^^ {
        case lefts ~ rights =>
          val (vars, addressable) = lefts.unzip
          PShortVarDecl(rights, vars, addressable)
      }

    lazy val labeledStmt: Parser[PLabeledStmt] =
      (idnDef <~ ":") ~ statement ^^ PLabeledStmt

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
      Parser("{") |~> statements <~| "}" ^^ PBlock

    lazy val statements: Parser[Vector[PStatement]] =
      ((statement <~ eos).* ~ statement.?) ^^ {
        case ss ~ None => ss
        case ss ~ Some(s) => ss :+ s
      }

    lazy val ifStmt: Parser[PIfStmt] =
      (ifClause |~ (Parser("else") |~> ifStmt)) ^^ { case clause ~ PIfStmt(ifs, els) => PIfStmt(clause +: ifs, els) } |
        (ifClause |~ (Parser("else") |~> block).?) ^^ { case clause ~ els => PIfStmt(Vector(clause), els) }

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

    lazy val nestedTypeOrExpr: Parser[PTypeOrExpr] =
      "(" ~> nestedTypeOrExpr <~ ")" | typeOrExpr

    lazy val typeOrExpr: Parser[PTypeOrExpr] =
      expression | typ

    lazy val expression: Parser[PExpression] =
      precedence1

    lazy val precedence1: PackratParser[PExpression] = /* Right-associative */
      (precedence2 ~ (Parser("?") |~> precedence1 <~| ":") |~ precedence1) ^^ PConditional |
        precedence2

    lazy val precedence2: PackratParser[PExpression] = /* Left-associative */
      precedence2 ~ (Parser("||") |~> precedence3) ^^ POr |
        precedence3

    lazy val precedence3: PackratParser[PExpression] = /* Left-associative */
      precedence3 ~ (Parser("&&") |~> precedence4) ^^ PAnd |
        precedence4

    lazy val precedence4: PackratParser[PExpression] =  /* Right-associative */
      ((precedence5 <~ "==>") |~ precedence4) ^^ PImplication |
      precedence5

    lazy val precedence5: PackratParser[PExpression] = /* Left-associative */
      precedence5 ~ (Parser("==") |~> precedence6) ^^ PEquals |
        precedence5 ~ (Parser("!=") |~> precedence6) ^^ PUnequals |
        precedence5 ~ (Parser("<") |~> precedence6) ^^ PLess |
        precedence5 ~ (Parser("<=") |~> precedence6) ^^ PAtMost |
        precedence5 ~ (Parser(">") |~> precedence6) ^^ PGreater |
        precedence5 ~ (Parser(">=") |~> precedence6) ^^ PAtLeast |
        precedence6

    lazy val precedence6: PackratParser[PExpression] = /* Left-associative */
      precedence6 ~ (Parser("+") |~> precedence7) ^^ PAdd |
        precedence6 ~ (Parser("-") |~> precedence7) ^^ PSub |
        precedence7

    lazy val precedence7: PackratParser[PExpression] = /* Left-associative */
      precedence7 ~ (Parser("*") |~> precedence8) ^^ PMul |
        precedence7 ~ (Parser("/") |~> precedence8) ^^ PDiv |
        precedence7 ~ (Parser("%") |~> precedence8) ^^ PMod |
        precedence8

    lazy val precedence8: PackratParser[PExpression] =
      unaryExp


    lazy val unaryExp: Parser[PExpression] =
      ghostUnaryExpression |
        ("+" ~> unaryExp) ^^ (e => PAdd(PIntLit(0).at(e), e)) |
        ("-" ~> unaryExp) ^^ (e => PSub(PIntLit(0).at(e), e)) |
        ("!" ~> unaryExp) ^^ PNegation |
        reference |
        dereference |
        receiveExp |
        unfolding |
        primaryExp

    lazy val reference: Parser[PReference] =
      "&" ~> unaryExp ^^ PReference

    lazy val dereference: Parser[PDereference] =
      "*" ~> unaryExp ^^ PDereference

    lazy val receiveExp: Parser[PReceive] =
      "<-" ~> unaryExp ^^ PReceive

    lazy val unfolding: Parser[PUnfolding] =
      ("unfolding" ~> predicateAccess |~ (Parser("in") |~> expression)) ^^ PUnfolding

    lazy val primaryExpOrType: Parser[PTypeOrExpr] =
      primaryExp | typ

    lazy val primaryExp: Parser[PExpression] =
      invoke |
        dot |
        indexedExp |
        sliceExp |
        typeAssertion |
        operand

    lazy val invoke: PackratParser[PInvoke] =
      primaryExpOrType ~ callArguments ^^ PInvoke

    lazy val callArguments: Parser[Vector[PExpression]] =
      (Parser("(") |~> (rep1sep(expression, ",") <~| ",".?).? <~| ")") ^^ (opt => opt.getOrElse(Vector.empty))

    lazy val dot: PackratParser[PDot] =
      primaryExpOrType ~ ("." ~> idnUse) ^^ PDot

    lazy val selection: PackratParser[PSelection] =
      primaryExp ~ ("." ~> idnUse) ^^ PSelection

    lazy val idBasedSelection: Parser[PSelection] =
      nestedIdnUse ~ ("." ~> idnUse) ^^ {
        case base ~ field => PSelection(PNamedOperand(base).at(base), field)
      }

    lazy val indexedExp: PackratParser[PIndexedExp] =
      primaryExp ~ (Parser("[") |~> expression <~| "]") ^^ PIndexedExp

    lazy val sliceExp: PackratParser[PSliceExp] =
      (primaryExp ~ (Parser("[") |~> expression) |~ (Parser(",") |~> expression) |~ ((Parser(",") |~> expression).? <~| "]")) ^^ PSliceExp

    lazy val typeAssertion: PackratParser[PTypeAssertion] =
      primaryExp ~ ("." ~> "(" |~> typ <~| ")") ^^ PTypeAssertion

    lazy val operand: Parser[PExpression] =
      literal | namedOperand | (Parser("(") |~> expression <~| ")")

    lazy val namedOperand: Parser[PNamedOperand] =
      idnUse ^^ PNamedOperand

    lazy val literal: Parser[PLiteral] =
      basicLit | compositeLit | functionLit

    lazy val basicLit: Parser[PBasicLiteral] =
      "true" ^^^ PBoolLit(true) |
        "false" ^^^ PBoolLit(false) |
        "nil" ^^^ PNilLit() |
        regex("[0-9]+".r) ^^ (lit => PIntLit(BigInt(lit))) |
        //runeLit |
        stringLit

    lazy val stringLit: Parser[PStringLit] =
      rawStringLit | interpretedStringLit

    lazy val rawStringLit: Parser[PStringLit] =
      // unicode characters and newlines are allowed
      "`" ~> "[^`]*".r <~ "`" ^^ (lit => PStringLit(lit))

    lazy val interpretedStringLit: Parser[PStringLit] =
      // unicode values and byte values are allowed
      //"\"" ~> "[^\"\n]*".r <~ "\"" ^^ (lit => PStringLit(lit))
      "\"" ~> """(?:\\"|[^"\n])*""".r <~ "\"" ^^ (lit => PStringLit(lit))

    lazy val compositeLit: Parser[PCompositeLit] =
      literalType ~ literalValue ^^ PCompositeLit

    lazy val literalValue: Parser[PLiteralValue] =
      (Parser("{") |~> (rep1sep(keyedElement, ",") <~| ",".?).? <~| "}") ^^ {
        case None => PLiteralValue(Vector.empty)
        case Some(ps) => PLiteralValue(ps)
      }

    lazy val keyedElement: Parser[PKeyedElement] =
      ((compositeKey <~ ":").? |~ compositeVal) ^^ PKeyedElement

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

    lazy val typ: Parser[PType] =
      "(" ~> typ <~ ")" | typeLit | predeclaredType | namedOperand

    lazy val typeLit: Parser[PTypeLit] =
      pointerType | sliceType | arrayType | mapType | channelType | functionType | structType | interfaceType


    lazy val pointerType: Parser[PPointerType] =
      "*" ~> typ ^^ PPointerType

    lazy val sliceType: Parser[PSliceType] =
      "[]" ~> typ ^^ PSliceType

    lazy val mapType: Parser[PMapType] =
      ("map" ~> ("[" ~> typ <~ "]")) ~ typ ^^ PMapType

    lazy val channelType: Parser[PChannelType] =
      ("chan" ~> "<-") ~> typ ^^ PRecvChannelType |
        ("<-" ~> "chan") ~> typ ^^ PSendChannelType |
        "chan" ~> typ ^^ PBiChannelType

    lazy val functionType: Parser[PFunctionType] =
      "func" ~> signature ^^ PFunctionType.tupled

    lazy val arrayType: Parser[PArrayType] =
      ("[" ~> expression <~ "]") ~ typ ^^ PArrayType

    lazy val structType: Parser[PStructType] =
      "struct" ~> "{" |~> (structClause <~ eos).* <~ "}" ^^ PStructType

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
      methodSpec | interfaceName

    lazy val interfaceName: Parser[PInterfaceName] =
      declaredType ^^ PInterfaceName

    lazy val methodSpec: Parser[PMethodSig] =
      idnDef ~ signature ^^ { case id ~ sig => PMethodSig(id, sig._1, sig._2) }

    lazy val predicateSpec: Parser[PMPredicateSig] =
      idnDef ~ parameters ^^ PMPredicateSig

    lazy val namedType: Parser[PNamedType] =
      predeclaredType |
        declaredType

    lazy val predeclaredType: Parser[PPredeclaredType] =
      "bool" ^^^ PBoolType() |
        "int" ^^^ PIntType() |
          "string" ^^^ PStringType()

    lazy val declaredType: Parser[PDeclaredType] =
      idnUse ^^ PDeclaredType

    lazy val literalType: Parser[PLiteralType] =
      sliceType | arrayType | implicitSizeArrayType | mapType | structType | declaredType

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
      parameters ^^ PResultClause |
        typ ^^ (t => PResultClause(Vector(PUnnamedParameter(t).at(t)))) |
        success(PVoidResult())

    lazy val parameters: Parser[Vector[PParameter]] =
      "(" ~> (parameterList <~ ",".?).? <~ ")" ^^ {
        case None => Vector.empty
        case Some(ps) => ps
      }

    lazy val parameterList: Parser[Vector[PParameter]] =
      rep1sep(parameterDecl, ",") ^^ Vector.concat

    lazy val parameterDecl: Parser[Vector[PParameter]] =
      ghostParameter |
      rep1sep(maybeAddressableIdnDef, ",") ~ typ ^^ { case ids ~ t =>
        ids map (id => PNamedParameter(id._1, t.copy, id._2).at(id._1): PParameter)
      } |  typ ^^ (t => Vector(PUnnamedParameter(t).at(t)))


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

    lazy val maybeAddressableIdnDef: Parser[(PIdnDef, Boolean)] =
      idnDef ~ "!".? ^^ { case id ~ opt => (id, opt.isDefined) }

    lazy val maybeAddressableIdnUnk: Parser[(PIdnUnk, Boolean)] =
      idnUnk ~ "!".? ^^ { case id ~ opt => (id, opt.isDefined) }

    lazy val idnDefLike: Parser[PDefLikeId] = idnDef | wildcard
    lazy val idnUseLike: Parser[PUseLikeId] = idnUse | wildcard

    lazy val labelDef: Parser[PLabelDef] = identifier ^^ PLabelDef
    lazy val labelUse: Parser[PLabelUse] = identifier ^^ PLabelUse

    lazy val pkgDef: Parser[PPkgDef] = identifier ^^ PPkgDef
    lazy val pkgUse: Parser[PPkgUse] = identifier ^^ PPkgUse

    lazy val wildcard: Parser[PWildcard] = "_" ^^^ PWildcard()


    lazy val identifier: Parser[String] =
      "[a-zA-Z_][a-zA-Z0-9_]*".r into (s => {
        if (isReservedWord(s))
          failure(s"""keyword "$s" found where identifier expected""")
        else
          success(s)
      })

    lazy val idnImportPath: Parser[String] =
      "\"[a-zA-Z0-9_/]*\"".r
      // """[^\P{L}\P{M}\P{N}\P{P}\P{S}!\"#$%&'()*,:;<=>?[\\\]^{|}\x{FFFD}]+""".r // \P resp. \p is currently not supported

    /**
      * Ghost
      */

    lazy val ghostMember: Parser[Vector[PGhostMember]] =
      (fpredicateDecl ^^ (Vector(_))) |
        (mpredicateDecl ^^ (Vector(_))) |
        (Parser("ghost") |~> (methodDecl | functionDecl) ^^ (m => Vector(PExplicitGhostMember(m).at(m)))) |
        (Parser("ghost") |~> (constDecl | varDecl | typeDecl) ^^ (ms => ms.map(m => PExplicitGhostMember(m).at(m))))

    lazy val fpredicateDecl: Parser[PFPredicateDecl] =
      ("pred" ~> idnDef) ~ parameters ~ (Parser("{") |~> expression <~| "}").? ^^ PFPredicateDecl

    lazy val mpredicateDecl: Parser[PMPredicateDecl] =
      ("pred" ~> receiver) ~ idnDef ~ parameters ~ (Parser("{") |~> expression <~| "}").? ^^ {
        case rcv ~ name ~ paras ~ body => PMPredicateDecl(name, rcv, paras, body)
      }

    lazy val ghostStatement: Parser[PGhostStatement] =
      "ghost" ~> statement ^^ PExplicitGhostStatement |
      "assert" ~> expression ^^ PAssert |
      "exhale" ~> expression ^^ PExhale |
      "assume" ~> expression ^^ PAssume |
      "inhale" ~> expression ^^ PInhale |
      "fold" ~> predicateAccess ^^ PFold |
      "unfold" ~> predicateAccess ^^ PUnfold

    lazy val accessible: Parser[PAccessible] =
       dereference | reference | idBasedSelection | selection

    lazy val predicateCall: Parser[PPredicateCall] =
      ("memory" ~> "(" |~> expression <~| ")") ^^ PMemoryPredicateCall |
      idnUse ~ callArguments ^^ PFPredOrBoolFuncCall |
      nestedIdnUse ~ ("." ~> idnUse) ~ callArguments ^^ PMPredOrMethRecvOrExprCall |
      primaryExp ~ ("." ~> idnUse) ~ callArguments ^^ PMPredOrBoolMethCall |
      methodRecvType ~ ("." ~> idnUse) ~ callArguments ^^ PMPredOrMethExprCall

    lazy val predicateAccess: Parser[PPredicateAccess] =
      predicateCall ^^ PPredicateAccess |
        ("acc" ~> "(" |~> predicateCall <~| ")") ^^ PPredicateAccess

    lazy val ghostParameter: Parser[Vector[PParameter]] =
      (Parser("ghost") |~> rep1sep(maybeAddressableIdnDef, ",") ~ typ) ^^ { case ids ~ t =>
        ids map (id => PExplicitGhostParameter(PNamedParameter(id._1, t.copy, id._2).at(id._1)).at(id._1): PParameter)
      } | (Parser("ghost") |~> typ) ^^ (t => Vector(PExplicitGhostParameter(PUnnamedParameter(t).at(t)).at(t)))

    lazy val ghostUnaryExpression: Parser[PExpression] =
      ("old" ~> "(" |~> expression <~| ")") ^^ POld |
        ("acc" ~> "(" |~> accessible <~| ")") ^^ PAccess |
        ("acc" ~> "(" |~> predicateCall <~| ")") ^^ PPredicateAccess

    /**
      * EOS
      */

    lazy val eos: Parser[Vector[String]] =
      (";" | """\s""".r).+

    /***
      * accepts zero or more semicolons and whitespaces including new line characters
      */
    lazy val zeroOrMoreEos: Parser[Option[String]] =
      ";".? <~ Parser("""\s""".r).*

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


