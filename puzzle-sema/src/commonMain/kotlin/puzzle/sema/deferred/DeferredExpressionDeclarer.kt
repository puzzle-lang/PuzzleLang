package puzzle.sema.deferred

import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.sema.binding.declare
import puzzle.sema.scope.PzlScope

class DeferredExpressionDeclarer(
	private val parent: PzlScope<FileContext>,
	private val expression: Expression,
) : DeferredDeclarer {
	
	context(_: FileContext)
	override fun declares() {
		expression.declare(parent)
	}
}