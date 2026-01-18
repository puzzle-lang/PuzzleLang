package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.binding.declare

context(file: FileContext)
fun deferredBinding() {
	file.deferredExpressions.forEach {
		it.expression.declare(it.parent)
	}
	file.deferredTypeReferences.forEach {
	
	}
	file.deferredScopes.forEach { scope ->
		when (scope) {
			is DeferredCtorScope -> scope.node.body
			is DeferredFunScope -> TODO()
			is DeferredGetterScope -> TODO()
			is DeferredInitScope -> TODO()
			is DeferredSetterScope -> TODO()
		}
	}
}