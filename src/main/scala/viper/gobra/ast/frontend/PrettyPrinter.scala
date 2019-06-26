package viper.gobra.ast.frontend

import org.bitbucket.inkytonik.kiama

trait PrettyPrinter {
  def format(node: PNode): String
}

trait PrettyPrinterCombinators { this: kiama.output.PrettyPrinter =>

  def opt[T](x: Option[T])(f: T => Doc): Doc = x.fold(emptyDoc)(f)

  def block(doc: Doc): Doc = {
    braces(nest(doc) <> line)
  }
}

class DefaultPrettyPrinter extends PrettyPrinter with kiama.output.PrettyPrinter with PrettyPrinterCombinators {

  override val defaultIndent = 2
  override val defaultWidth = 80

  override def format(node: PNode): String = pretty(show(node)).layout

  def show(node: PNode): Doc = node match {
    case n: PPackageClause => show(n)
    case n: PImportDecl => show(n)
    case n: PMember => show(n)
    case n: PStatement => show(n)
    case n: PExpression => show(n)
    case n: PType => show(n)
    case n: PIdnNode => show(n)
    case _ => ???
  }

  // program

  def show(p: PProgram): Doc = p match {
    case PProgram(packageClause, imports, declarations, _) =>
      show(packageClause) <> line <> line <>
        ssep(imports map show, line) <> line <>
        ssep(declarations map show, line <> line) <> line
  }

  // package

  def show(node: PPackageClause): Doc = "package" <+> show(node.id)
  def show(id: PPackegeNode): Doc = id.name

  // imports

  def show(decl: PImportDecl): Doc = decl match {
    case PQualifiedImport(qualifier, pkg) => "import" <+> show(qualifier) <+> pkg
    case PUnqualifiedImport(pkg) => "import" <+> "." <+> pkg
  }

  // members

  def show(member: PMember): Doc = member match {
    case member: PActualMember => member match {
      case n: PConstDecl => constDecl(n)
      case n: PVarDecl => varDecl(n)
      case n: PTypeDecl => typeDecl(n)
      case PFunctionDecl(id, args, result, body) =>
        "func" <+> show(id) <> parens(ssep(args map show, comma)) <> show(result) <> opt(body)(b => space <> block(show(b)))
      case PMethodDecl(id, receiver, args, result, body) =>
        "func" <+> show(receiver) <+> show(id) <> parens(ssep(args map show, comma)) <> show(result) <> opt(body)(b => space <> block(show(b)))
    }
    case member: PGhostMember => member match {
      case PExplicitGhostMember(m) => "ghost" <+> show(m)
    }
  }

  def varDecl(decl: PVarDecl): Doc = decl match {
    case PVarDecl(typ, right, left) => "var" <+> ssep(left map show, comma) <> opt(typ)(space <> show(_)) <+> "=" <+> ssep(right map show, comma)
  }

  def constDecl(decl: PConstDecl): Doc = decl match {
    case PConstDecl(typ, right, left) => "const" <+> ssep(left map show, comma) <> opt(typ)(space <> show(_)) <+> "=" <+> ssep(right map show, comma)
  }

  def typeDecl(decl: PTypeDecl): Doc = decl match {
    case PTypeDef(right, left) => "type" <+> show(left) <+> show(right)
    case PTypeAlias(right, left) => "type" <+> show(left) <+> "=" <+> show(right)
  }

  def show(para: PParameter): Doc = para match {
    case PExplicitGhostParameter(p) => "ghost" <+> show(p)
    case PUnnamedParameter(typ) => show(typ)
    case PNamedParameter(id, typ) => show(id) <+> show(typ)
  }

  def show(rec: PReceiver): Doc = rec match {
    case PNamedReceiver(id, typ) => parens(show(id) <+> show(typ))
    case PUnnamedReceiver(typ) => parens(show(typ))
  }

  def show(res: PResult): Doc = res match {
    case PVoidResult() => emptyDoc
    case PResultClause(outs) => space <> (if (outs.size == 1) show(outs.head) else parens(ssep(outs map show, comma)))
  }

  // statements

  def show(stmt: PStatement): Doc = stmt match {
    case statement: PActualStatement => statement match {
      case n: PConstDecl => constDecl(n)
      case n: PVarDecl => varDecl(n)
      case n: PTypeDecl => typeDecl(n)
      case PShortVarDecl(right, left) => ssep(left map show, comma) <+> ":=" <+> ssep(right map show, comma)
      case PLabeledStmt(label, stmt) => show(label) <> ":" <+> show(stmt)
      case PEmptyStmt() => emptyDoc
      case PExpressionStmt(exp) => show(exp)
      case PSendStmt(channel, msg) => show(channel) <+> "<-" <+> show(msg)
      case PAssignment(left, right) => ssep(left map show, comma) <+> "=" <+> ssep(right map show, comma)
      case PAssignmentWithOp(left, op, right) => show(left) <+> show(op) <> "=" <+> show(right)
      case PIfStmt(ifs, els) =>
        ssep(
          ifs map (f => "if" <> preStmt(f.pre) <+> show(f.condition) <+> show(f.body)),
        line) <> opt(els)("else" <+> show(_) <> line)
      case PExprSwitchStmt(pre, exp, cases, dflt) =>
        "switch" <> preStmt(pre) <+> block(ssep(cases map {
          case PExprSwitchCase(left, body) => "case" <+> ssep(left map show, comma) <> ":" <> nest (line <> ssep(body.stmts map show, line))
        }, line)) <> ssep(dflt map {
          d => "default"  <> ":" <> nest (line <> ssep(d.stmts map show, line))
        }, line)
      case PTypeSwitchStmt(pre, exp, binder, cases, dflt) =>
        "switch" <> preStmt(pre) <> opt(binder)(space <> show(_) <+> ":=") <+> show(exp) <> ".(type)" <+> block(ssep(cases map {
          case PTypeSwitchCase(left, body) => "case" <+> ssep(left map show, comma) <> ":" <> nest (line <> ssep(body.stmts map show, line))
        }, line)) <> ssep(dflt map {
          d => "default"  <> ":" <> nest (line <> ssep(d.stmts map show, line))
        }, line)
      case PForStmt(pre, cond, post, body) =>
      case PAssForRange(range, ass, body) =>
      case PShortForRange(range, shorts, body) =>
      case PGoStmt(exp) =>
      case PSelectStmt(send, rec, aRec, sRec, dflt) =>
      case PReturn(exps) =>
      case PBreak(label) =>
      case PContinue(label) =>
      case PGoto(label) =>
      case PDeferStmt(exp) =>
      case PBlock(stmts) =>
      case PSeq(stmts) =>
    }
    case statement: PGhostStatement => statement match {
      case PExplicitGhostStatement(actual) =>
      case PAssert(exp) =>
      case PAssume(exp) =>
      case PExhale(exp) =>
      case PInhale(exp) =>
    }
  }

  def preStmt(n: Option[PSimpleStmt]): Doc = opt(n)(space <> show(_) <> ";")

  def show(n: PAssOp): Doc = n match {
    case PAddOp() => "+"
    case PSubOp() => "-"
    case PMulOp() => "*"
    case PDivOp() => "/"
    case PModOp() => "%"
  }

  // expressions

  def show(expr: PExpression): Doc = ???

  // types

  def show(typ: PType): Doc = ???

  // ids

  def show(id: PIdnNode): Doc = id.name
}