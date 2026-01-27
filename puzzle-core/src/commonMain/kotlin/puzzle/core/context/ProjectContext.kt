package puzzle.core.context

import puzzle.core.io.FilePath

class ProjectContext : Context() {
	
	override val parent = RootContext
	
	lateinit var name: String
	
	lateinit var modules: List<ModuleContext>
	
	var path: FilePath? = null
}