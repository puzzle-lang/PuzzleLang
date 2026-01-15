package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.semantics.scope.PzlScope

sealed interface DeferredExpression {
	
	val parent: PzlScope
	
	val expression: Expression
}

class DeferredPropertyExpression(
	override val parent: PzlScope,
	override val expression: Expression,
) : DeferredExpression

class DeferredParameterExpression(
	override val parent: PzlScope,
	override val expression: Expression,
) : DeferredExpression