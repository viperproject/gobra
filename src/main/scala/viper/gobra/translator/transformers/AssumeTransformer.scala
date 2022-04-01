// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers
import viper.gobra.backend.BackendVerifier
import viper.silver.ast.utility.ImpureAssumeRewriter
import viper.silver.{ast => vpr}

class AssumeTransformer extends ViperTransformer {
  override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    val progWithoutAssumes = {
      val uncleanProg = ImpureAssumeRewriter.rewriteAssumes(task.program)
      // FIXME: required due to inconvenient silver assume rewriter
      val cleanedDomains: Seq[vpr.Domain] = uncleanProg.domains.map{ d =>
        if (d.name == "Assume") d.copy(name = "Assume$")(d.pos, d.info, d.errT)
        else d
      }
      uncleanProg.copy(domains = cleanedDomains)(uncleanProg.pos, uncleanProg.info, uncleanProg.errT)
    }

    task.copy(program = progWithoutAssumes)
  }
}
