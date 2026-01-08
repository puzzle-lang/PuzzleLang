package puzzle.core.frontend.model

import kotlinx.serialization.Contextual
import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.semantics.scope.FileScope
import puzzle.core.frontend.token.PzlToken
import puzzle.core.util.PathWrapper

class RootContext(
	val projects: List<ProjectContext>,
)

class ProjectContext(
	val name: String,
	val path: PathWrapper?,
	val builtin: Boolean,
	val modules: List<ModuleContext>,
)

class ModuleContext(
	val name: String,
	@Contextual
	val path: PathWrapper?,
	val builtin: Boolean,
	val files: List<FileContext>,
)

class FileContext {
	
	lateinit var sourcePath: PathWrapper
	
	lateinit var lineStarts: IntArray
	
	lateinit var tokens: List<PzlToken>
	
	lateinit var node: AstFile
	
	lateinit var scope: FileScope
}