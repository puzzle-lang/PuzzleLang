package puzzle.sema.deferred

import puzzle.context.FileContext

sealed interface DeferredDeclarer {
	
	context(_: FileContext)
	fun declares()
}