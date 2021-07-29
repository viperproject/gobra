// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.frontend.Parser.FromFileSource

object Gobrafier {

  private def multilineComment(str: String): String = s"/\\*@\\s*$str\\s*@\\*/"
  private def singlelineComment(str: String): String = s"//@\\s*$str\\s*"

  /**
    * Keywords used in Goified files.
    */
  private val ghost_parameters: String = "ghost-parameters:"
  private val ghost_results: String = "ghost-results:"
  private val addressable_variables: String = "addressable:"
  private val with_keyword: String = "with:"
  private val unfolding_keyword: String = "unfolding:"
  private val ghost_keyword: String = "ghost"
  private val func_keyword: String = "func"
  private val pure_keyword: String = "pure"
  private val return_keyword: String = "return"

  /**
    * Regular expression patterns used in the regular expressions.
    */
  private val identifier = "(\\S+?)"
  private val args = "(.*?)"
  private val argList = "\\((.*?)\\)"
  private val functionInvocation = s"$identifier$argList"
  private val functionDecl = s"$func_keyword\\s+$identifier\\s*$argList\\s*($argList)?"
  private val spec = "(.*?)"
  private val vars = "(.*?)"
  private val predicate = "(.*?)"
  private val assignment = s"$vars\\s*(:=|=)\\s*$vars"
  private val goifiedComment = s"([;]\\s*(.*?))?"


  // "(?s)" means that `.` matches even new-line characters
  // "(?m)" means that `^` and `$` will match beginning and end of a line, respectively
  private val ghostParamsRegex = s"(?s)${singlelineComment(s"$ghost_parameters(.*?)\\n$spec$functionDecl")}".r
  private val ghostResultsRegex = s"(?s)${singlelineComment(s"$ghost_results\\s*(.*?)\\n$spec$functionDecl")}".r
  private val returnGhostRegex = s"(?m)$return_keyword\\s*$args${singlelineComment(s"$with_keyword\\s*$args")}$$".r
  private val pureKeywordRegex = s"(?m)${singlelineComment(s"$pure_keyword$spec")}$$".r
  private val assignGhostRegex = s"(?m)$assignment\\s*${singlelineComment(s"$with_keyword\\s*$assignment\\s*$goifiedComment")}$$".r

  private val addressableVariablesRegex = s"(?m)(^.*?)\\s*${singlelineComment(addressable_variables)}$vars$$".r
  private val ghostInvocationRegex = s"(?m)$functionInvocation\\s*${multilineComment(s"$with_keyword\\s*$args")}".r
  private val unfoldingAccessRegex = s"(?m)${multilineComment(s"$unfolding_keyword$predicate\\s*")}".r
  
  private val non_newline = "[\t\r\f]"

  /**
    * Remove the remaining goifying comments in the file.
    */
  private def removeGoifyingComments(fileContent: String): String =
    fileContent.replaceAll(s"//@\\s*", "").replaceAll(s"/\\*@$non_newline*", "").replaceAll(s"(?m)$non_newline*@\\*/", "")



  /**
    * Add parenthesis around the given string.
    */
  private def parens(str: String): String = "(" + str + ")"

  /**
    * Add ghost keywords between parameters.
    */
  private def addGhostKeywordToParamsList(paramsList: String): String =
    paramsList.trim.replaceAll("\\s+", " ").split("\\s*,\\s*").map(v => ghost_keyword + " " + v).mkString(", ")

  /**
    * Split string with list of addressable variables into a list
    * of all identifiers of the addressable variables.
    */
  private def splitArgList(argList: String): List[String] = {
    argList.replaceAll(" ", "").split(",").toList
  }


  /**
    * Converts a .go program with annotations in comments to a .gobra program.
    * Does not apply any transformations if a .gobra program is provided.
    */
  def gobrafy(source: FromFileSource): FromFileSource = source match {
    case source if source.path.toString.endsWith(s".${PackageResolver.goExtension}") => FromFileSource(source.path, gobrafy(source.content))
    case source => source
  }

  def gobrafy(content: String): String = {

    /**
      * Replace ghost-parameters annotations by adding ghost parameters
      * back to the function parameters.
      */
    var newFileContents = ghostParamsRegex.replaceAllIn(content, m => {
      "\n" + // added to maintain line consistency
      m.group(2) +
      "func " +
      m.group(3) +
      parens(m.group(4) +
      (if (m.group(4) == "") "" else ", ") +
      addGhostKeywordToParamsList(m.group(1))) +
      " " +
        (if (m.group(5) == null) "" else m.group(5))
    })

    /**
      * Replace ghost-results annotations by adding ghost results
      * back to the function results.
      */
    newFileContents = ghostResultsRegex.replaceAllIn(newFileContents, m => {
      "\n" + // added to maintain line consistency
      m.group(2) +
      "func " +
      m.group(3) +
      parens(m.group(4)) +
      " " +
      parens(
        (if (m.group(5) == null) "" else m.group(5)) +
        (if (m.group(5) == null || m.group(5) == "" || m.group(1) == null || m.group(1) == "") "" else ", ") +
        addGhostKeywordToParamsList(m.group(1))) +
      (if (m.group(5) == null) " " else "") // add an additional space if no returns have existed before
    })

    /**
      * Add Ghost parameter list to the end of the return statement.
      */
    newFileContents = returnGhostRegex.replaceAllIn(newFileContents, m => {
      "return " +
      m.group(1) +
      (if (m.group(1) == "") "" else ", ") +
      m.group(2)
    })
    

    /**
      * Add pure keyword to function declaration.
      */
    newFileContents = pureKeywordRegex.replaceAllIn(newFileContents, m => {
      "pure" +
      m.group(1)
    })

    /**
      * Add ghost variables back to both sides of assignment.
      */
    newFileContents = assignGhostRegex.replaceAllIn(newFileContents, m => {
      m.group(1) +
      (if (m.group(1) == "") "" else ", ") +
      m.group(4) + " " +
      m.group(2) + " " +
      m.group(3) +
      (if (m.group(3) == "" || m.group(6) == "" || m.group(6) == null) "" else ", ") +
      (if (m.group(6) == "" || m.group(6) == null) "" else  m.group(6)) +
      (if (m.group(8) == "" || m.group(8) == null) "" else "//@ " + m.group(8))
    })

    /**
      * Add exclamation mark to all addressable variables.
      */
    newFileContents = addressableVariablesRegex.replaceAllIn(newFileContents, m => {
      val addressableVariables = splitArgList(m.group(2))
      var code = m.group(1)

      addressableVariables.foreach(addrVar => {
        // remove all control characters
        val variable = addrVar.filter(_ >= ' ')

        code = code.replaceAll(variable, variable + "!")
      })

      code
    })

    /**
      * Add ghost parameters back to method invocations.
      */
    newFileContents = ghostInvocationRegex.replaceAllIn(newFileContents, m => {
      m.group(1) +
      parens(m.group(2) +
      (if (m.group(2) == "") "" else ", ") +
      m.group(3))
    })

    /**
      * Put unfolding expressions back.
      */
    newFileContents = unfoldingAccessRegex.replaceAllIn(newFileContents, m => {
      "unfolding" + m.group(1) + " in "
    })

    removeGoifyingComments(newFileContents)
  }
}
