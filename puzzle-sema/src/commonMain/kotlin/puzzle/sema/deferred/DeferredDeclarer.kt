package puzzle.sema.deferred

import puzzle.core.context.FileContext

sealed interface DeferredDeclarer {
	
	context(_: FileContext)
	fun declares()
}