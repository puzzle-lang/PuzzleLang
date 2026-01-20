package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope

class DeferredTypeReferenceDeclarer(
	private val parent: PzlScope<FileContext>,
	private val type: TypeReference,
) : DeferredDeclarer {
	
	context(_: FileContext)
	override fun declares() {
	
	}
}