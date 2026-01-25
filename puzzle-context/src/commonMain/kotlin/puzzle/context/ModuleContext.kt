package puzzle.context

class ModuleContext : Context() {
	
	override lateinit var parent: ProjectContext
	
	lateinit var name: String
	
	lateinit var files: List<FileContext>
	
	//	var ignoreRules = emptyList<IgnoreRule>()
//
//	var deps = emptySet<Dependence>()
}

data class Dependence(
	val projectName: String,
	val moduleName: String,
)