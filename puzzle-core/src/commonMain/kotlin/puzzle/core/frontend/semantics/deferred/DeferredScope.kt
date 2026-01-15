package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.expression.LambdaExpression
import puzzle.core.frontend.semantics.scope.BlockScope
import puzzle.core.frontend.semantics.scope.CtorScope
import puzzle.core.frontend.semantics.scope.FunScope
import puzzle.core.frontend.semantics.scope.PzlScope

sealed interface DeferredScope {
	
	val scope: PzlScope
	
	val node: PzlAstNode
}

class DeferredFunScope(
	override val scope: FunScope,
	override val node: FunDeclaration,
) : DeferredScope

class DeferredInitScope(
	override val scope: BlockScope,
	override val node: InitDeclaration,
) : DeferredScope

class DeferredLambdaScope(
	override val scope: BlockScope,
	override val node: LambdaExpression,
) : DeferredScope

class DeferredCtorScope(
	override val scope: CtorScope,
	override val node: CtorDeclaration,
) : DeferredScope

class DeferredGetterScope(
	override val scope: BlockScope,
	override val node: PropertyGetter,
) : DeferredScope

class DeferredSetterScope(
	override val scope: BlockScope,
	override val node: PropertySetter,
) : DeferredScope