package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend.{DefaultPrettyPrinter, PMember, PStatement}

class GhostLessPrinter(classifier: GhostClassifier) extends DefaultPrettyPrinter {

  override def showMember(mem: PMember): Doc =
    if (classifier.isMemberGhost(mem)) emptyDoc else super.showMember(mem)

  override def showStmt(stmt: PStatement): Doc =
    if (classifier.isStmtGhost(stmt)) emptyDoc else super.showStmt(stmt)



}
