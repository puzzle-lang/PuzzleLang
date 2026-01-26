package puzzle.sema.deferred

import puzzle.ast.type.TypeReference
import puzzle.context.FileContext
import puzzle.sema.scope.PzlScope

class DeferredTypeReferenceDeclarer(
	private val parent: PzlScope<FileContext>,
	private val type: TypeReference,
) : DeferredDeclarer {
	
	context(_: FileContext)
	override fun declares() {
	
	}
}