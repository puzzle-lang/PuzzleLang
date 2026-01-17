package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.model.FileContext

context(file: FileContext)
fun deferredBinding() {
	file.deferredExpressions.forEach {
		
	}
	file.deferredTypeReferences.forEach {
	
	}
	file.deferredScopes.forEach {
//		when (it) {
//			is DeferredCtorScope -> TODO()
//			is DeferredFunScope -> TODO()
//			is DeferredGetterScope -> TODO()
//			is DeferredInitScope -> TODO()
//			is DeferredSetterScope -> TODO()
//		}
	}
}