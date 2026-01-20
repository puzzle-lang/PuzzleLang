package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.model.FileContext

sealed interface DeferredDeclarer {
	
	context(_: FileContext)
	fun declares()
}