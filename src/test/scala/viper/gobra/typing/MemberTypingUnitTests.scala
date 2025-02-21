// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra.typing

import org.bitbucket.inkytonik.kiama.util.Positions
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.frontend.{PExplicitGhostMember, PExplicitGhostStructType, PFieldDecl, PFieldDecls, PGhostifier, PIdnDef, PIdnNode, PIdnUse, PIntType, PMember, PNamedOperand, PPackage, PPackageClause, PPkgDef, PProgram, PStructType, PTypeDecl, PTypeDef, PVarDecl, PositionManager}
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.{Config, PackageInfo}

class MemberTypingUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("Typing: An int ghost type declaration is ghost") {
    // syntax: "ghost type MyInt int"
    val idnDef = PIdnDef("MyInt")
    val typ = PTypeDef(PIntType(), idnDef)
    val decl = PExplicitGhostMember(typ)
    frontend.symbolTableEntry(decl)(idnDef) should matchPattern {
      case SymbolTable.NamedType(typDef, true, _) if typDef == typ =>
    }
  }

  test("Typing: a struct ghost type declaration is ghost") {
    // syntax: "ghost type MyStruct struct { Value int }"
    val idnDef = PIdnDef("MyStruct")
    val typ = PTypeDef(PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("Value"), PIntType()))))), idnDef)
    val decl = PExplicitGhostMember(typ)
    frontend.symbolTableEntry(decl)(idnDef) should matchPattern {
      case SymbolTable.NamedType(typDef, true, _) if typDef == typ =>
    }
  }

  test("Typing: a ghost struct type declaration is not ghost") {
    // syntax: "type MyStruct ghost struct { Value int }"
    val idnDef = PIdnDef("MyStruct")
    val typ = PTypeDef(PExplicitGhostStructType(PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("Value"), PIntType())))))), idnDef)
    frontend.symbolTableEntry(typ)(idnDef) should matchPattern {
      case SymbolTable.NamedType(typDef, false, _) if typDef == typ =>
    }
    frontend.typeDeclUnderlyingTyp(typ) should matchPattern {
      case Type.StructT(_, true, _, _) =>
    }
  }

  test("Typing: a ghost struct ghost type declaration is ghost") {
    // syntax: "ghost type MyStruct ghost struct { Value int }"
    val idnDef = PIdnDef("MyStruct")
    val typ = PTypeDef(PExplicitGhostStructType(PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("Value"), PIntType())))))), idnDef)
    frontend.symbolTableEntry(PExplicitGhostMember(typ))(idnDef) should matchPattern {
      case SymbolTable.NamedType(typDef, true, _) if typDef == typ =>
    }
    frontend.typeDeclUnderlyingTyp(typ) should matchPattern {
      case Type.StructT(_, true, _, _) =>
    }
  }


  class TestFrontend {

    /**
      * Creates the program:
      *
      * package pkg
      *
      * <member>
      * var x <idnUse> // if idnUse is provided
      *
      * and returns the program as well as x's PIdnDef (if it got inserted)
      */
    def singleMemberProgram(member: PMember, idnUse: Option[PIdnUse]): (PProgram, Option[PIdnDef]) = {
      val idnDefX = PIdnDef("x")
      val optVarDecl = idnUse.map(id => PVarDecl(Some(PNamedOperand(id)), Vector.empty, Vector(idnDefX), Vector(false)))
      val members = optVarDecl.map(varDecl => Vector(member, varDecl)).getOrElse(Vector(member))
      val prog = PProgram(
        PPackageClause(PPkgDef("pkg")),
        Vector(),
        Vector(),
        Vector(),
        members
      )
      (prog, idnUse.map(_ => idnDefX))
    }

    def singleMemberTypeInfoAndIdnDef(member: PMember, idnUse: Option[PIdnUse] = None): (TypeInfoImpl, Option[PIdnDef]) = {
      val (program, idnDef) = singleMemberProgram(member, idnUse)
      val positions = new Positions
      val pkg = PPackage(
        PPackageClause(PPkgDef("pkg")),
        Vector(program),
        new PositionManager(positions),
        new PackageInfo("pkg", "pkg", false)
      )
      val tree = new Info.GoTree(pkg)
      val config = Config()
      (new TypeInfoImpl(tree, Map.empty)(config), idnDef)
    }

    def singleMemberTypeInfo(member: PMember, idnUse: Option[PIdnUse] = None): TypeInfoImpl =
      singleMemberTypeInfoAndIdnDef(member, idnUse)._1

    def wellDefMember(member: PMember) =
      singleMemberTypeInfo(member).wellDefMember(member)

    def symbolTableEntry(member: PMember)(id: PIdnNode): SymbolTable.Regular =
      singleMemberTypeInfo(member).regular(id)

    def typeDeclUnderlyingTyp(typeDecl: PTypeDecl): Type.Type = {
      val idnUse = PIdnUse(typeDecl.left.name)
      val (info, optIdnDef) = singleMemberTypeInfoAndIdnDef(typeDecl, Some(idnUse))
      val idnDef = optIdnDef.get
      val typ = info.typ(idnDef)
      info.underlyingType(typ)
    }

    def ghostTypeDeclUnderlyingTyp(typeDecl: PGhostifier[PTypeDecl] with PMember): Type.Type = {
      val idnUse = PIdnUse(typeDecl.actual.left.name)
      val (info, optIdnDef) = singleMemberTypeInfoAndIdnDef(typeDecl, Some(idnUse))
      val idnDef = optIdnDef.get
      val typ = info.typ(idnDef)
      info.underlyingType(typ)
    }
  }
}
