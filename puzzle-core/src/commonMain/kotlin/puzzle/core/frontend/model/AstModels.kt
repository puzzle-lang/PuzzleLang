package puzzle.core.frontend.model

import kotlinx.serialization.Contextual
import puzzle.core.frontend.ast.AstFile
import puzzle.core.util.PathWrapper

class AstRoot(
	val projects: List<AstProject>,
)

class AstProject(
	val name: String,
	val path: PathWrapper?,
	val builtin: Boolean,
	val modules: List<AstModule>,
)

class AstModule(
	val name: String,
	@Contextual
	val path: PathWrapper?,
	val builtin: Boolean,
	val files: List<AstFile>,
)