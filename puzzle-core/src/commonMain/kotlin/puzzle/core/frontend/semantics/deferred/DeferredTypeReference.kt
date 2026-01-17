package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope

class DeferredTypeReference(
	val parent: PzlScope<FileContext>,
	val type: TypeReference,
)