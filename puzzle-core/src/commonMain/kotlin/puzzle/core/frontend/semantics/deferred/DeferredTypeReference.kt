package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.semantics.scope.FileContextScope

class DeferredTypeReference(
	val parent: FileContextScope,
	val type: TypeReference,
)