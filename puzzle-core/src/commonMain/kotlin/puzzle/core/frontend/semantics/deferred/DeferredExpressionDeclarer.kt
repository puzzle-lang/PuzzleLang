package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.binding.declare
import puzzle.core.frontend.semantics.scope.PzlScope

class DeferredExpressionDeclarer(
	private val parent: PzlScope<FileContext>,
	private val expression: Expression,
) : DeferredDeclarer {
	
	context(_: FileContext)
	override fun declares() {
		expression.declare(parent)
	}
}