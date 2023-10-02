// Generated from src/main/antlr4/GobraParser.g4 by ANTLR 4.13.0
package viper.gobra.frontend;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GobraParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GobraParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GobraParser#exprOnly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprOnly(GobraParser.ExprOnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#stmtOnly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtOnly(GobraParser.StmtOnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeOnly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeOnly(GobraParser.TypeOnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#maybeAddressableIdentifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaybeAddressableIdentifierList(GobraParser.MaybeAddressableIdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#maybeAddressableIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaybeAddressableIdentifier(GobraParser.MaybeAddressableIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#sourceFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceFile(GobraParser.SourceFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#preamble}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreamble(GobraParser.PreambleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#initPost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitPost(GobraParser.InitPostContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#importPre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportPre(GobraParser.ImportPreContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#importSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportSpec(GobraParser.ImportSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#importDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDecl(GobraParser.ImportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#ghostMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGhostMember(GobraParser.GhostMemberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code explicitGhostStatement}
	 * labeled alternative in {@link GobraParser#ghostStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitGhostStatement(GobraParser.ExplicitGhostStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code foldStatement}
	 * labeled alternative in {@link GobraParser#ghostStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFoldStatement(GobraParser.FoldStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code proofStatement}
	 * labeled alternative in {@link GobraParser#ghostStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProofStatement(GobraParser.ProofStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code matchStmt_}
	 * labeled alternative in {@link GobraParser#ghostStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchStmt_(GobraParser.MatchStmt_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#auxiliaryStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuxiliaryStatement(GobraParser.AuxiliaryStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#statementWithSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementWithSpec(GobraParser.StatementWithSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#outlineStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutlineStatement(GobraParser.OutlineStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#ghostPrimaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGhostPrimaryExpr(GobraParser.GhostPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#permission}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPermission(GobraParser.PermissionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExpr(GobraParser.TypeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#boundVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundVariables(GobraParser.BoundVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#boundVariableDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundVariableDecl(GobraParser.BoundVariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#triggers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggers(GobraParser.TriggersContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger(GobraParser.TriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#predicateAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateAccess(GobraParser.PredicateAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#optionSome}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionSome(GobraParser.OptionSomeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#optionNone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionNone(GobraParser.OptionNoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#optionGet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionGet(GobraParser.OptionGetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#sConversion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSConversion(GobraParser.SConversionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#old}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOld(GobraParser.OldContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#oldLabelUse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOldLabelUse(GobraParser.OldLabelUseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#labelUse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelUse(GobraParser.LabelUseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#before}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBefore(GobraParser.BeforeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#isComparable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsComparable(GobraParser.IsComparableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#low}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLow(GobraParser.LowContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#lowc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLowc(GobraParser.LowcContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeOf(GobraParser.TypeOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccess(GobraParser.AccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(GobraParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#matchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchExpr(GobraParser.MatchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#matchExprClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchExprClause(GobraParser.MatchExprClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#seqUpdExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqUpdExp(GobraParser.SeqUpdExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#seqUpdClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqUpdClause(GobraParser.SeqUpdClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#ghostTypeLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGhostTypeLit(GobraParser.GhostTypeLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#domainType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomainType(GobraParser.DomainTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#domainClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomainClause(GobraParser.DomainClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#adtType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtType(GobraParser.AdtTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#adtClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtClause(GobraParser.AdtClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#adtFieldDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtFieldDecl(GobraParser.AdtFieldDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#ghostSliceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGhostSliceType(GobraParser.GhostSliceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#sqType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqType(GobraParser.SqTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecification(GobraParser.SpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#specStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecStatement(GobraParser.SpecStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#terminationMeasure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerminationMeasure(GobraParser.TerminationMeasureContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#assertion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertion(GobraParser.AssertionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#matchStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchStmt(GobraParser.MatchStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#matchStmtClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchStmtClause(GobraParser.MatchStmtClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#matchCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchCase(GobraParser.MatchCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code matchPatternBind}
	 * labeled alternative in {@link GobraParser#matchPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchPatternBind(GobraParser.MatchPatternBindContext ctx);
	/**
	 * Visit a parse tree produced by the {@code matchPatternComposite}
	 * labeled alternative in {@link GobraParser#matchPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchPatternComposite(GobraParser.MatchPatternCompositeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code matchPatternValue}
	 * labeled alternative in {@link GobraParser#matchPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchPatternValue(GobraParser.MatchPatternValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#matchPatternList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchPatternList(GobraParser.MatchPatternListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#blockWithBodyParameterInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockWithBodyParameterInfo(GobraParser.BlockWithBodyParameterInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#closureSpecInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosureSpecInstance(GobraParser.ClosureSpecInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#closureSpecParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosureSpecParams(GobraParser.ClosureSpecParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#closureSpecParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosureSpecParam(GobraParser.ClosureSpecParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#closureImplProofStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosureImplProofStmt(GobraParser.ClosureImplProofStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#implementationProof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementationProof(GobraParser.ImplementationProofContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#methodImplementationProof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodImplementationProof(GobraParser.MethodImplementationProofContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#nonLocalReceiver}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonLocalReceiver(GobraParser.NonLocalReceiverContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#selection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection(GobraParser.SelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#implementationProofPredicateAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementationProofPredicateAlias(GobraParser.ImplementationProofPredicateAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#make}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMake(GobraParser.MakeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#new_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_(GobraParser.New_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#specMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecMember(GobraParser.SpecMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(GobraParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#methodDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDecl(GobraParser.MethodDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#explicitGhostMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitGhostMember(GobraParser.ExplicitGhostMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#fpredicateDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpredicateDecl(GobraParser.FpredicateDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#predicateBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateBody(GobraParser.PredicateBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#mpredicateDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMpredicateDecl(GobraParser.MpredicateDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#varSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSpec(GobraParser.VarSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#shortVarDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShortVarDecl(GobraParser.ShortVarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#receiver}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReceiver(GobraParser.ReceiverContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#parameterDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDecl(GobraParser.ParameterDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#actualParameterDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActualParameterDecl(GobraParser.ActualParameterDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#ghostParameterDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGhostParameterDecl(GobraParser.GhostParameterDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#parameterType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterType(GobraParser.ParameterTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code closureImplSpecExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosureImplSpecExpr(GobraParser.ClosureImplSpecExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryExpr_}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr_(GobraParser.PrimaryExpr_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code quantification}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantification(GobraParser.QuantificationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unfolding}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnfolding(GobraParser.UnfoldingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(GobraParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code p41Expr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitP41Expr(GobraParser.P41ExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(GobraParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code p42Expr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitP42Expr(GobraParser.P42ExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ternaryExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryExpr(GobraParser.TernaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpr(GobraParser.AddExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code implication}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplication(GobraParser.ImplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mulExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExpr(GobraParser.MulExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code let}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet(GobraParser.LetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpr(GobraParser.RelExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link GobraParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(GobraParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(GobraParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#applyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplyStmt(GobraParser.ApplyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#packageStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageStmt(GobraParser.PackageStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#specForStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecForStmt(GobraParser.SpecForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#loopSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopSpec(GobraParser.LoopSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#deferStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeferStmt(GobraParser.DeferStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#basicLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicLit(GobraParser.BasicLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpr(GobraParser.NewExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code makeExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMakeExpr(GobraParser.MakeExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ghostPrimaryExpr_}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGhostPrimaryExpr_(GobraParser.GhostPrimaryExpr_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code invokePrimaryExprWithSpec}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvokePrimaryExprWithSpec(GobraParser.InvokePrimaryExprWithSpecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code indexPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexPrimaryExpr(GobraParser.IndexPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code seqUpdPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqUpdPrimaryExpr(GobraParser.SeqUpdPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodPrimaryExpr(GobraParser.MethodPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predConstrPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredConstrPrimaryExpr(GobraParser.PredConstrPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code invokePrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvokePrimaryExpr(GobraParser.InvokePrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operandPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandPrimaryExpr(GobraParser.OperandPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeAssertionPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAssertionPrimaryExpr(GobraParser.TypeAssertionPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code builtInCallExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuiltInCallExpr(GobraParser.BuiltInCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectorPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectorPrimaryExpr(GobraParser.SelectorPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conversionPrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversionPrimaryExpr(GobraParser.ConversionPrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code slicePrimaryExpr}
	 * labeled alternative in {@link GobraParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlicePrimaryExpr(GobraParser.SlicePrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#functionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionLit(GobraParser.FunctionLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#closureDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosureDecl(GobraParser.ClosureDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#predConstructArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredConstructArgs(GobraParser.PredConstructArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#interfaceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceType(GobraParser.InterfaceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#predicateSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateSpec(GobraParser.PredicateSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#methodSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodSpec(GobraParser.MethodSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(GobraParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeLit(GobraParser.TypeLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#predType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredType(GobraParser.PredTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#predTypeParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredTypeParams(GobraParser.PredTypeParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#literalType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralType(GobraParser.LiteralTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#implicitArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplicitArray(GobraParser.ImplicitArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#fieldDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDecl(GobraParser.FieldDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#slice_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlice_(GobraParser.Slice_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#lowSliceArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLowSliceArgument(GobraParser.LowSliceArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#highSliceArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHighSliceArgument(GobraParser.HighSliceArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#capSliceArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapSliceArgument(GobraParser.CapSliceArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#assign_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_op(GobraParser.Assign_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#rangeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeClause(GobraParser.RangeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#packageClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageClause(GobraParser.PackageClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#importPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportPath(GobraParser.ImportPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(GobraParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#constDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDecl(GobraParser.ConstDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#constSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstSpec(GobraParser.ConstSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(GobraParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(GobraParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDecl(GobraParser.TypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpec(GobraParser.TypeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(GobraParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(GobraParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(GobraParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#simpleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStmt(GobraParser.SimpleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStmt(GobraParser.ExpressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#sendStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSendStmt(GobraParser.SendStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#incDecStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncDecStmt(GobraParser.IncDecStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(GobraParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#emptyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStmt(GobraParser.EmptyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#labeledStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabeledStmt(GobraParser.LabeledStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(GobraParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#breakStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(GobraParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#continueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(GobraParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#gotoStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGotoStmt(GobraParser.GotoStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#fallthroughStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFallthroughStmt(GobraParser.FallthroughStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(GobraParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#switchStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStmt(GobraParser.SwitchStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#exprSwitchStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSwitchStmt(GobraParser.ExprSwitchStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#exprCaseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCaseClause(GobraParser.ExprCaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#exprSwitchCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSwitchCase(GobraParser.ExprSwitchCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeSwitchStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSwitchStmt(GobraParser.TypeSwitchStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeSwitchGuard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSwitchGuard(GobraParser.TypeSwitchGuardContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeCaseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCaseClause(GobraParser.TypeCaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeSwitchCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSwitchCase(GobraParser.TypeSwitchCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(GobraParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#selectStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStmt(GobraParser.SelectStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#commClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommClause(GobraParser.CommClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#commCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommCase(GobraParser.CommCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#recvStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecvStmt(GobraParser.RecvStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(GobraParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#forClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForClause(GobraParser.ForClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#goStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoStmt(GobraParser.GoStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(GobraParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(GobraParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#arrayLength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLength(GobraParser.ArrayLengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#elementType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementType(GobraParser.ElementTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#pointerType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerType(GobraParser.PointerTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#sliceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSliceType(GobraParser.SliceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(GobraParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#channelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannelType(GobraParser.ChannelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#functionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionType(GobraParser.FunctionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#signature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignature(GobraParser.SignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult(GobraParser.ResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(GobraParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#conversion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversion(GobraParser.ConversionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#nonNamedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonNamedType(GobraParser.NonNamedTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperand(GobraParser.OperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(GobraParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(GobraParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#operandName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandName(GobraParser.OperandNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#qualifiedIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdent(GobraParser.QualifiedIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#compositeLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompositeLit(GobraParser.CompositeLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#literalValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralValue(GobraParser.LiteralValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#elementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementList(GobraParser.ElementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#keyedElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyedElement(GobraParser.KeyedElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey(GobraParser.KeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(GobraParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#structType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructType(GobraParser.StructTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#string_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_(GobraParser.String_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#embeddedField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmbeddedField(GobraParser.EmbeddedFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(GobraParser.IndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#typeAssertion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAssertion(GobraParser.TypeAssertionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(GobraParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#methodExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodExpr(GobraParser.MethodExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#receiverType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReceiverType(GobraParser.ReceiverTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GobraParser#eos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEos(GobraParser.EosContext ctx);
}