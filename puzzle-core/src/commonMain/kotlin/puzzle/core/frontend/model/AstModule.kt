package puzzle.core.frontend.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AstFile
import puzzle.core.util.PathWrapper

@Serializable
class AstModule(
	val name: String,
	@Contextual
	val path: PathWrapper?,
	val isBuiltin: Boolean,
	val nodes: List<AstFile>,
)