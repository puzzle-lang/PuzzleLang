package puzzle.core.frontend.model

import kotlinx.serialization.Contextual
import puzzle.core.cli.PzlCliOption
import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.semantics.deferred.DeferredExpression
import puzzle.core.frontend.semantics.deferred.DeferredScope
import puzzle.core.frontend.semantics.deferred.DeferredTypeReference
import puzzle.core.frontend.semantics.symbol.FileSymbol
import puzzle.core.frontend.token.PzlToken
import puzzle.core.util.PathWrapper

sealed interface Context

class RootContext : Context {
	
	lateinit var options: List<PzlCliOption>
	
	lateinit var projects: List<ProjectContext>
}

class ProjectContext(
	val name: String,
	val path: PathWrapper?,
	val builtin: Boolean,
	val modules: List<ModuleContext>,
) : Context

class ModuleContext(
	val name: String,
	@Contextual
	val path: PathWrapper?,
	val builtin: Boolean,
	val files: List<FileContext>,
) : Context

class FileContext(
	val builtin: Boolean,
) : Context {
	
	lateinit var sourcePath: PathWrapper
	
	lateinit var lineStarts: IntArray
	
	lateinit var tokens: List<PzlToken>
	
	lateinit var node: AstFile
	
	lateinit var symbol: FileSymbol
	
	val deferredTypeReferences = mutableListOf<DeferredTypeReference>()
	
	val deferredExpressions = mutableListOf<DeferredExpression>()
	
	val deferredScopes = mutableListOf<DeferredScope>()
}