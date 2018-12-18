/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import java.io.File

import org.bitbucket.inkytonik.kiama.parsing.Parsers
import org.bitbucket.inkytonik.kiama.util.Positions
import viper.gobra.ast.parser._
import viper.gobra.reporting.VerifierError

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

  def parse(file: File): Either[Vector[VerifierError], PProgram] = {
    Left(Vector.empty)
  }

  private class PureSyntaxAnalyzer(positions: Positions) extends Parsers(positions) {

    val reservedWords: Set[String] = Set(
      "break", "default", "func", "interface", "select",
      "case", "defer", "go", "map", "struct",
      "chan", "else", "goto", "package", "switch",
      "const", "fallthrough", "if", "range", "type",
      "continue", "for", "import", "return", "var"
    )

    def isReservedWord(word: String): Boolean = reservedWords contains word


    /**
      * Member
      */

    lazy val declarationStmt: Parser[PStatement] =
      (constDecl | varDecl | typeDecl) ^^ PSeq

    lazy val constDecl: Parser[Vector[PConstDecl]] =
      "const" ~> constSpec ^^ (decl => Vector(decl)) |
        "const" ~> "(" ~> (constSpec <~ eos).* <~ ")"

    lazy val constSpec: Parser[PConstDecl] =
      rep1sep(idnDef, ",") ~ (typ.? ~ ("=" ~> rep1sep(expression, ","))).? ^^ {
        case left ~ None => PConstDecl(left, None, Vector.empty)
        case left ~ Some(typ ~ right) => PConstDecl(left, typ, right)
      }

    lazy val varDecl: Parser[Vector[PVarDecl]] =
      "var" ~> varSpec ^^ (decl => Vector(decl)) |
        "var" ~> "(" ~> (varSpec <~ eos).* <~ ")"

    lazy val varSpec: Parser[PVarDecl] =
      rep1sep(idnDef, ",") ~ typ ~ ("=" ~> rep1sep(expression, ",")).? ^^ {
        case left ~ typ ~ None => PVarDecl(left, Some(typ), Vector.empty)
        case left ~ typ ~ Some(right) => PVarDecl(left, Some(typ), right)
      } |
        rep1sep(idnDef, ",") ~ ("=" ~> rep1sep(expression, ",")) ^^ {
          case left ~ right => PVarDecl(left, None, right)
        }

    lazy val typeDecl: Parser[Vector[PTypeDecl]] =
      "type" ~> typeSpec ^^ (decl => Vector(decl)) |
        "type" ~> "(" ~> (typeSpec <~ eos).* <~ ")"

    lazy val typeSpec: Parser[PTypeDecl] =
      typeDefSpec | typeAliasSpec

    lazy val typeDefSpec: Parser[PTypeDef] =
      idnDef ~ typ ^^ PTypeDef

    lazy val typeAliasSpec: Parser[PTypeAlias] =
      (idnDef <~ "=") ~ typ ^^ PTypeAlias

    lazy val functionDecl: Parser[PFunctionDecl] =
      ("func" ~> idnDef) ~ signature ~ block.? ^^ {
        case name ~ sig ~ body => PFunctionDecl(name, sig._1, sig._2, body)
      }

    lazy val methodDecl: Parser[PMethodDecl] =
      ("func" ~> idnDef) ~ receiver ~ signature ~ block.? ^^ {
        case name ~ receiver ~ sig ~ body => PMethodDecl(name, receiver, sig._1, sig._2, body)
      }

    /**
      * Statements
      */


    lazy val statement: Parser[PStatement] =
      declarationStmt |
        goStmt |
        deferStmt |
        returnStmt |
        controlStmt |
        ifStmt |
        anyForStmt |
        exprSwitchStmt |
        typeSwitchStmt |
        block |
        simpleStmt |
        emptyStmt


    lazy val simpleStmt: Parser[PSimpleStmt] =
      expressionStmt | sendStmt | assignmentWithOp | assignment | shortVarDecl

    lazy val simpleStmtWithEmpty: Parser[PSimpleStmt] =
      simpleStmt | emptyStmt

    lazy val emptyStmt: Parser[PEmptyStmt] = /* parse last because always succeeds */
      success(PEmptyStmt())

    lazy val expressionStmt: Parser[PExpressionStmt] =
      expression ^^ PExpressionStmt

    lazy val sendStmt: Parser[PSendStmt] =
      (expression <~ "<-") ~ expression ^^ PSendStmt

    lazy val assignment: Parser[PAssignment] =
      (rep1sep(assignee, ",") <~ "=") ~ rep1sep(expression, ",") ^^ PAssignment

    lazy val assignmentWithOp: Parser[PAssignmentWithOp] =
      assignee ~ (assOp <~ "=") ~ expression ^^ PAssignmentWithOp |
        assignee <~ "++" ^^ (e => PAssignmentWithOp(e, PAddOp().at(e), e).at(e)) |
        assignee <~ "--" ^^ (e => PAssignmentWithOp(e, PSubOp().at(e), e).at(e))

    lazy val assOp: Parser[PAssOp] =
      "+" ^^^ PAddOp() |
        "-" ^^^ PSubOp() |
        "*" ^^^ PMulOp() |
        "/" ^^^ PDivOp() |
        "%" ^^^ PModOp()

    lazy val assignee: Parser[PAssignee] =
      selectionOrMethodExpr | selection | indexedExp | "&" ~> unaryExp ^^ PDereference

    lazy val shortVarDecl: Parser[PShortVarDecl] =
      (rep1sep(idnUnknown, ",") <~ ":=") ~ rep1sep(expression, ",") ^^ PShortVarDecl

    lazy val labeledStmt: Parser[PLabeledStmt] =
      (idnDef <~ ":") ~ statement ^^ PLabeledStmt

    lazy val returnStmt: Parser[PReturn] =
      "return" ~> repsep(expression, ",") ^^ PReturn

    lazy val goStmt: Parser[PGoStmt] =
      "go" ~> expression ^^ PGoStmt

    lazy val controlStmt: Parser[PStatement] =
      breakStmt | continueStmt | gotoStmt

    lazy val breakStmt: Parser[PBreak] =
      "break" ~> idnUse.? ^^ PBreak

    lazy val continueStmt: Parser[PContinue] =
      "continue" ~> idnUse.? ^^ PContinue

    lazy val gotoStmt: Parser[PGoto] =
      "goto" ~> idnUse ^^ PGoto

    lazy val deferStmt: Parser[PDeferStmt] =
      "defer" ~> expression ^^ PDeferStmt

    lazy val block: Parser[PBlock] =
      "{" ~> repsep(statement, eos) <~ "}" ^^ PBlock

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
      ("case" ~> rep1sep(expression, ",") <~ ":") ~ pos(repsep(statement, eos)) ^^ {
        case guards ~ stmts => PExprSwitchCase(guards, PBlock(stmts.get).at(stmts))
      }

    lazy val exprSwitchDflt: Parser[PExprSwitchDflt] =
      "default" ~> ":" ~> pos(repsep(statement, eos)) ^^ (stmts => PExprSwitchDflt(PBlock(stmts.get).at(stmts)))

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
      ("case" ~> rep1sep(typ, ",") <~ ":") ~ pos(repsep(statement, eos)) ^^ {
        case guards ~ stmts => PTypeSwitchCase(guards, PBlock(stmts.get).at(stmts))
      }

    lazy val typeSwitchDflt: Parser[PTypeSwitchDflt] =
      "default" ~> ":" ~> pos(repsep(statement, eos)) ^^ (stmts => PTypeSwitchDflt(PBlock(stmts.get).at(stmts)))

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
      ("case" ~> receiveExp <~ ":") ~ pos(repsep(statement, eos)) ^^ {
        case receive ~ stmts => PSelectRecv(receive, PBlock(stmts.get).at(stmts))
      }

    lazy val selectAssRecv: Parser[PSelectAssRecv] =
      ("case" ~> rep1sep(assignee, ",") <~ "=") ~ (receiveExp <~ ":") ~ pos(repsep(statement, eos)) ^^ {
        case left ~ receive ~ stmts => PSelectAssRecv(left, receive, PBlock(stmts.get).at(stmts))
      }

    lazy val selectShortRecv: Parser[PSelectShortRecv] =
      ("case" ~> rep1sep(idnUnknown, ",") <~ ":=") ~ (receiveExp <~ ":") ~ pos(repsep(statement, eos)) ^^ {
        case left ~ receive ~ stmts => PSelectShortRecv(left, receive, PBlock(stmts.get).at(stmts))
      }

    lazy val selectSend: Parser[PSelectSend] =
      ("case" ~> sendStmt <~ ":") ~ pos(repsep(statement, eos)) ^^ {
        case send ~ stmts => PSelectSend(send, PBlock(stmts.get).at(stmts))
      }

    lazy val selectDflt: Parser[PSelectDflt] =
      "default" ~> ":" ~> pos(repsep(statement, eos)) ^^ (stmts => PSelectDflt(PBlock(stmts.get).at(stmts)))

    lazy val anyForStmt: Parser[PStatement] =
      forStmt | assForRange | shortForRange

    lazy val forStmt: Parser[PForStmt] =
      ("for" ~> simpleStmt.? <~ ";") ~ (pos(expression.?) <~ ";") ~ simpleStmt.? ~ block ^^ {
        case pre ~ (pos@PPos(None)) ~ post ~ body => PForStmt(pre, PBoolLit(true).at(pos), post, body)
        case pre ~ PPos(Some(cond)) ~ post ~ body => PForStmt(pre, cond, post, body)
      }

    lazy val assForRange: Parser[PAssForRange] =
      ("for" ~> rep1sep(assignee, ",") <~ "=") ~ ("range" ~> expression) ~ block ^^ PAssForRange

    lazy val shortForRange: Parser[PShortForRange] =
      ("for" ~> rep1sep(idnUnknown, ",") <~ ":=") ~ ("range" ~> expression) ~ block ^^ PShortForRange

    /**
      * Expressions
      */

    lazy val expression: Parser[PExpression] =
      precedence1

    lazy val precedence1: Parser[PExpression] = /* Left-associative */
      precedence1 ~ ("||" ~> precedence2) ^^ POr |
        precedence2

    lazy val precedence2: Parser[PExpression] = /* Left-associative */
      precedence2 ~ ("&&" ~> precedence3) ^^ PAnd |
        precedence3

    lazy val precedence3: Parser[PExpression] = /* Left-associative */
      precedence3 ~ ("==" ~> precedence4) ^^ PEquals |
        precedence3 ~ ("!=" ~> precedence4) ^^ PUnequals |
        precedence3 ~ ("<" ~> precedence4) ^^ PLess |
        precedence3 ~ ("<=" ~> precedence4) ^^ PAtMost |
        precedence3 ~ (">" ~> precedence4) ^^ PGreater |
        precedence3 ~ (">=" ~> precedence4) ^^ PAtLeast |
        precedence4

    lazy val precedence4: Parser[PExpression] = /* Left-associative */
      precedence4 ~ ("+" ~> precedence5) ^^ PAdd |
        precedence4 ~ ("-" ~> precedence5) ^^ PSub |
        precedence3

    lazy val precedence5: Parser[PExpression] = /* Left-associative */
      precedence5 ~ ("*" ~> precedence6) ^^ PMul |
        precedence5 ~ ("/" ~> precedence6) ^^ PDiv |
        precedence5 ~ ("%" ~> precedence6) ^^ PMod |
        precedence6

    lazy val precedence6: Parser[PExpression] =
      unaryExp


    lazy val unaryExp: Parser[PExpression] =
      "+" ~> unaryExp ^^ (e => PAdd(PIntLit(0).at(e), e)) |
        "-" ~> unaryExp ^^ (e => PSub(PIntLit(0).at(e), e)) |
        "!" ~> unaryExp ^^ PNegation |
        "*" ~> unaryExp ^^ PReference |
        "&" ~> unaryExp ^^ PDereference |
        receiveExp |
        primaryExp

    lazy val receiveExp: Parser[PReceive] =
      "<-" ~> unaryExp ^^ PReceive


    lazy val primaryExp: Parser[PExpression] =
      operand |
        conversionOrUnaryCall |
        conversion |
        call |
        selectionOrMethodExpr |
        methodExpr |
        selection |
        indexedExp |
        sliceExp |
        typeAssertion

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
        regex("[0-9]+".r) ^^ (lit => PIntLit(BigInt(lit)))

    lazy val compositeLit: Parser[PCompositeLit] =
      literalType ~ literalValue ^^ PCompositeLit

    lazy val literalValue: Parser[PLiteralValue] =
      "{" ~> (rep1sep(keyedElement, ",") <~ ",".?).? <~ "}" ^^ {
        case None => PLiteralValue(Vector.empty)
        case Some(ps) => PLiteralValue(ps)
      }

    lazy val keyedElement: Parser[PKeyedElement] =
      (compositeVal <~ ":").? ~ compositeVal ^^ PKeyedElement

    lazy val compositeVal: Parser[PCompositeVal] =
      expCompositeLiteral | litCompositeLiteral

    lazy val expCompositeLiteral: Parser[PExpCompositeVal] =
      expression ^^ PExpCompositeVal

    lazy val litCompositeLiteral: Parser[PLitCompositeVal] =
      literalValue ^^ PLitCompositeVal

    lazy val functionLit: Parser[PFunctionLit] =
      "func" ~> signature ~ block ^^ { case sig ~ body => PFunctionLit(sig._1, sig._2, body) }

    lazy val conversionOrUnaryCall: Parser[PConversionOrUnaryCall] =
      nestedIdnUse ~ ("(" ~> expression <~ ",".? <~ ")") ^^ {
        PConversionOrUnaryCall
      }

    lazy val conversion: Parser[PConversion] =
      typ ~ ("(" ~> expression <~ ",".? <~ ")") ^^ PConversion

    lazy val call: Parser[PCall] =
      primaryExp ~ ("(" ~> (rep1sep(expression, ",") <~ ",".?).? <~ ")") ^^ {
        case base ~ None => PCall(base, Vector.empty)
        case base ~ Some(args) => PCall(base, args)
      }

    lazy val selectionOrMethodExpr: Parser[PSelectionOrMethodExpr] =
      nestedIdnUse ~ ("." ~> idnUnqualifiedUse) ^^ PSelectionOrMethodExpr

    lazy val methodExpr: Parser[PMethodExpr] =
      methodRecvType ~ ("." ~> idnUnqualifiedUse) ^^ PMethodExpr

    lazy val selection: Parser[PSelection] =
      primaryExp ~ ("." ~> idnUnqualifiedUse) ^^ PSelection

    lazy val indexedExp: Parser[PIndexedExp] =
      primaryExp ~ ("[" ~> expression <~ "]") ^^ PIndexedExp

    lazy val sliceExp: Parser[PSliceExp] =
      primaryExp ~ ("[" ~> expression) ~ ("," ~> expression) ~ (("," ~> expression).? <~ "]") ^^ PSliceExp

    lazy val typeAssertion: Parser[PTypeAssertion] =
      primaryExp ~ ("." ~> "(" ~> typ <~ ")") ^^ PTypeAssertion


    /**
      * Types
      */

    lazy val typ: Parser[PType] =
      "(" ~> typ <~ ")" | typeLit | namedType

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
      "struct" ~> "{" ~> repsep(structClause, eos) <~ "}" ^^ { clauses =>
        val embedded = clauses collect { case v: PEmbeddedDecl => v }
        val declss = clauses collect { case v: PFieldDecls => v }

        PStructType(embedded, declss flatMap (_.fields))
      }

    lazy val structClause: Parser[PStructClause] =
      embeddedDecl | fieldDecls

    lazy val embeddedDecl: Parser[PEmbeddedDecl] =
      methodRecvType ^^ PEmbeddedDecl

    lazy val fieldDecls: Parser[PFieldDecls] =
      rep1sep(idnDef, ",") ~ typ ^^ { case ids ~ t =>
        PFieldDecls(ids map (id => PFieldDecl(id, t).at(id)))
      }

    lazy val interfaceType: Parser[PInterfaceType] =
      "interface" ~> "{" ~> repsep(interfaceClause, eos) <~ "}" ^^ { clauses =>
        val embedded = clauses collect { case v: PInterfaceName => v }
        val decls = clauses collect { case v: PMethodSpec => v }

        PInterfaceType(embedded, decls)
      }

    lazy val interfaceClause: Parser[PInterfaceClause] =
      methodSpec | interfaceName

    lazy val interfaceName: Parser[PInterfaceName] =
      namedType ^^ PInterfaceName

    lazy val methodSpec: Parser[PMethodSpec] =
      idnDef ~ signature ^^ { case id ~ sig => PMethodSpec(id, sig._1, sig._2) }


    lazy val namedType: Parser[PNamedType] =
      predeclaredType |
        declaredType

    lazy val predeclaredType: Parser[PPredeclaredType] =
      "bool" ^^^ PBoolType() |
        "int" ^^^ PIntType()

    lazy val declaredType: Parser[PDeclaredType] =
      idnUse ^^ PDeclaredType

    lazy val literalType: Parser[PLiteralType] =
      sliceType | arrayType | implicitSizeArrayType | mapType | structType

    lazy val implicitSizeArrayType: Parser[PImplicitSizeArrayType] =
      "[" ~> "..." ~> "]" ~> typ ^^ PImplicitSizeArrayType

    /**
      * Misc
      */

    lazy val receiver: Parser[PReceiver] =
      "(" ~> idnDef.? ~ methodRecvType <~ ")" ^^ {
        case None ~ typ => PUnnamedReceiver(typ)
        case Some(name) ~ typ => PNamedReceiver(name, typ)
      }

    lazy val signature: Parser[(Vector[PParameter], PResult)] =
      parameters ~ result


    lazy val result: Parser[PResult] =
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
      repsep(idnDef, ",") ~ typ ^^ { case ids ~ t =>

        val names = ids filter (!PIdnNode.isWildcard(_))
        if (names.isEmpty) {
          Vector(PUnnamedParameter(t).at(t))
        } else {
          ids map (id => PNamedParameter(id, t).at(id))
        }
      }

    lazy val nestedIdnUse: Parser[PIdnUse] =
      "(" ~> nestedIdnUse <~ ")" | idnUse

    lazy val methodRecvType: Parser[PMethodRecvType] =
      "(" ~> methodRecvType <~ ")" |
        "*".? ~ namedType ^^ {
          case None ~ t => PMethodReceiveName(t)
          case _ ~ t => PMethodReceivePointer(t)
        }

    /**
      * Identifiers
      */

    lazy val idnUnknown: Parser[PIdnUnknown] =
      identifier ^^ PIdnUnknown


    lazy val idnDef: Parser[PIdnDef] =
      identifier ^^ PIdnDef

    lazy val idnUse: Parser[PIdnUse] =
      idnUnqualifiedUse

    lazy val idnUnqualifiedUse: Parser[PIdnUnqualifiedUse] =
      identifier ^^ PIdnUnqualifiedUse

    lazy val identifier: Parser[String] =
      "[a-zA-Z_][a-zA-Z0-9_]*".r into (s => {
        if (isReservedWord(s))
          failure(s"""keyword "$s" found where identifier expected""")
        else
          success(s)
      })

    /**
      * EOS
      */

    lazy val eos: Parser[String] =
      ";"


    implicit class PositionedPAstNode[N <: PNode](node: N) {
      def at(other: PNode): N = {
        positions.dupPos(other, node)
      }

      def range(from: PNode, to: PNode): N = {
        positions.dupRangePos(from, to, node)
      }
    }

    def pos[T](p: => Parser[T]): Parser[PPos[T]] = p ^^ PPos[T]

  }


}


