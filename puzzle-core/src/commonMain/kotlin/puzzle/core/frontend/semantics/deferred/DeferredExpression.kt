package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.semantics.scope.FileContextScope

sealed interface DeferredExpression {
	
	val parent: FileContextScope
	
	val expression: Expression
}

class DeferredPropertyExpression(
	override val parent: FileContextScope,
	override val expression: Expression,
) : DeferredExpression

class DeferredParameterExpression(
	override val parent: FileContextScope,
	override val expression: Expression,
) : DeferredExpression