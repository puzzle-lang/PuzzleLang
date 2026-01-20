package puzzle.core.frontend.model

import puzzle.core.cli.PzlCliOption
import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.semantics.deferred.DeferredDeclarer
import puzzle.core.frontend.semantics.symbol.FileSymbol
import puzzle.core.frontend.token.PzlToken
import puzzle.core.util.PathWrapper

sealed interface Context {
	
	val parent: Context
}

object RootContext : Context {
	
	override val parent: Context
		get() = error("RootContext 没有 parent")
	
	lateinit var options: List<PzlCliOption>
	
	lateinit var projects: List<ProjectContext>
	
	var maxPathLength = 0
}

class ProjectContext : Context {
	
	override val parent = RootContext
	
	lateinit var name: String
	
	var path: PathWrapper? = null
	
	var builtin = false
	
	lateinit var modules: List<ModuleContext>
}

class ModuleContext : Context {
	
	override lateinit var parent: ProjectContext
	
	lateinit var name: String
	
	var path: PathWrapper? = null
	
	var builtin = false
	
	lateinit var ignores: List<String>
	
	lateinit var deps: List<Dependence>
	
	lateinit var files: List<FileContext>
}

class FileContext : Context {
	
	override lateinit var parent: ModuleContext
	
	var builtin = false
	
	lateinit var path: PathWrapper
	
	lateinit var lineStarts: IntArray
	
	lateinit var tokens: List<PzlToken>
	
	lateinit var node: AstFile
	
	lateinit var symbol: FileSymbol
	
	val deferredDeclarers = mutableListOf<DeferredDeclarer>()
}

class Dependence(
	val projectName: String,
	val moduleName: String,
)

context(context: Context)
inline fun <reified CTX : Context> findContext(): CTX {
	var context = context
	while (context !is CTX) {
		context = context.parent
	}
	return context
}