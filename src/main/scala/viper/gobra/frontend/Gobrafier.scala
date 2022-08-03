// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.frontend.Source.TransformableSource
import viper.gobra.util.Constants

import scala.util.matching.Regex

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
  private val closureSpec = s"$identifier\\{([^@]*)\\}"
  private val spec = "(.*?)"
  private val vars = "(.*?)"
  private val predicate = "(.*?)"
  private val assignment = s"$vars\\s*(:=|=)\\s*$vars"
  private val goifiedComment = s"(?:[;]\\s*(.*?))?"


  // "(?s)" means that `.` matches even new-line characters
  // "(?m)" means that `^` and `$` will match beginning and end of a line, respectively
  private val ghostParamsRegex = s"(?s)${singlelineComment(s"$ghost_parameters(.*?)\\n$spec$functionDecl")}".r
  private val ghostResultsRegex = s"(?s)${singlelineComment(s"$ghost_results\\s*(.*?)\\n$spec$functionDecl")}".r
  private val returnGhostRegex = s"(?m)$return_keyword\\s*$args\\s*${singlelineComment(s"$with_keyword\\s*$args")}$$".r
  private val pureKeywordRegex = s"(?m)${singlelineComment(s"$pure_keyword$spec")}$$".r
  private val assignGhostRegex = s"(?m)$assignment\\s*${singlelineComment(s"$with_keyword\\s*$assignment\\s*$goifiedComment")}$$".r

  private val addressableVariablesRegex = s"(?m)(^.*?)\\s*${singlelineComment(addressable_variables)}$vars$$".r
  private val ghostInvocationSinglelineRegex = s"(?m)$functionInvocation\\s*${singlelineComment(s"$with_keyword\\s*$args")}$$".r
  private val ghostInvocationMultilineRegex = s"(?m)$functionInvocation\\s*${multilineComment(s"$with_keyword\\s*$args")}".r
  private val ghostCallWithSpecSinglelineRegex = s"(?m)$functionInvocation\\s*${multilineComment(s"as\\s*$closureSpec")}\\s*${singlelineComment(s"$with_keyword\\s*$args")}$$".r
  private val ghostCallWithSpecMultilineRegex = s"(?m)$functionInvocation\\s*${multilineComment(s"as\\s*$closureSpec")}\\s*${multilineComment(s"$with_keyword\\s*$args")}".r
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
    * Add braces around the given string.
    */
  private def braces(str: String): String = "{" + str + "}"

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
  def gobrafy(source: Source): Source = {
    def isGoFile(path: String): Boolean = path.endsWith(s".${PackageResolver.goExtension}")

    if (isGoFile(source.name)) source.transformContent(gobrafy(source.content))
    else source // is not a go file, keep the source unchanged
  }

  def gobrafy(content: String): String = {

    /**
      * Replace ghost-parameters annotations by adding ghost parameters
      * back to the function parameters.
      */
    var newFileContents = ghostParamsRegex.replaceAllIn(content, m => {
      val ghostParamsList = m.group(1)
      val functionSpec = m.group(2)
      val functionName = m.group(3)
      val actualParams = m.group(4)
      val returns = m.group(5)

      "\n" + // added to maintain line consistency
      functionSpec +
      "func " +
      functionName +
      parens(actualParams +
      (if (actualParams == "") "" else ", ") +
      addGhostKeywordToParamsList(ghostParamsList)) +
      " " +
        (if (returns == null) "" else returns)
    })

    /**
      * Replace ghost-results annotations by adding ghost results
      * back to the function results.
      */
    newFileContents = ghostResultsRegex.replaceAllIn(newFileContents, m => {
      val ghostReturns = m.group(1)
      val functionSpec = m.group(2)
      val functionName = m.group(3)
      val params = m.group(4)
      val actualReturns = m.group(5)

      "\n" + // added to maintain line consistency
        functionSpec +
      "func " +
        functionName +
      parens(params) +
      " " +
      parens(
        (if (actualReturns == null) "" else actualReturns) +
        (if (actualReturns == null || actualReturns == "" || ghostReturns == null || ghostReturns == "") "" else ", ") +
        addGhostKeywordToParamsList(ghostReturns)) +
      (if (actualReturns == null) " " else "") // add an additional space if no returns have existed before
    })

    /**
      * Add Ghost parameter list to the end of the return statement.
      */
    newFileContents = returnGhostRegex.replaceAllIn(newFileContents, m => {
      val actualValues = m.group(1)
      val ghostValues = m.group(2)

      "return " +
      actualValues +
      (if (actualValues == null || actualValues == "" || ghostValues == null || ghostValues == "") "" else ", ") +
      ghostValues
    })
    

    /**
      * Add pure keyword to function declaration.
      */
    newFileContents = pureKeywordRegex.replaceAllIn(newFileContents, m => {
      val spec = m.group(1)

      "pure" +
      spec
    })

    /**
      * Add ghost variables back to both sides of assignment.
      */
    newFileContents = assignGhostRegex.replaceAllIn(newFileContents, m => {
      val actualLhs = m.group(1)
      val actualOperator = m.group(2)
      val actualRhs = m.group(3)
      val ghostLhs = m.group(4)
      // val ghostOperator = m.group(5) // unused
      val ghostRhs = m.group(6)
      val comment = m.group(7)

      actualLhs +
      (if (actualLhs == "") "" else ", ") +
      ghostLhs + " " +
      actualOperator + " " +
      actualRhs +
      (if (actualRhs == null || actualRhs == "" || ghostRhs == "" || ghostRhs == null) "" else ", ") +
      (if (ghostRhs == "" || ghostRhs == null) "" else ghostRhs) +
      (if (comment == "" || comment == null) "" else "//@ " + comment)
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

        code = code.replaceAll(variable, variable + Constants.ADDRESSABILITY_MODIFIER)
      })

      code
    })

    /**
      * Add ghost parameters back to method invocations.
      */
    val ghostInvocationTransformer: Regex.Match => String = m => {
      val functionName = m.group(1)
      val actualArgs = m.group(2)
      val ghostArgs = m.group(3)

      functionName +
      parens(
        actualArgs +
        (if (actualArgs == null || actualArgs == "" || ghostArgs == null || ghostArgs == "") "" else ", ") +
        ghostArgs)
    }
    newFileContents = ghostInvocationSinglelineRegex.replaceAllIn(newFileContents, ghostInvocationTransformer)
    newFileContents = ghostInvocationMultilineRegex.replaceAllIn(newFileContents, ghostInvocationTransformer)

    /**
      * Add ghost parameters back to a call with spec.
      */
    val ghostCallWithSpecTransformer: Regex.Match => String = m => {
      val functionName = m.group(1)
      val actualArgs = m.group(2)
      val specName = m.group(3)
      val specParams = m.group(4)
      val ghostArgs = m.group(5)

      functionName +
        parens(
          actualArgs +
            (if (actualArgs == null || actualArgs == "" || ghostArgs == null || ghostArgs == "") "" else ", ") +
            ghostArgs) +
          " as " + specName + braces(specParams)
    }
    newFileContents = ghostCallWithSpecSinglelineRegex.replaceAllIn(newFileContents, ghostCallWithSpecTransformer)
    newFileContents = ghostCallWithSpecMultilineRegex.replaceAllIn(newFileContents, ghostCallWithSpecTransformer)

    /**
      * Put unfolding expressions back.
      */
    newFileContents = unfoldingAccessRegex.replaceAllIn(newFileContents, m => {
      val predInst = m.group(1)

      "unfolding" + predInst + " in"
    })

    removeGoifyingComments(newFileContents)
  }
}
