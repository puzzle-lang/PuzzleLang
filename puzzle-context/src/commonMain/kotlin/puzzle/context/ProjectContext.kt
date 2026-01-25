package puzzle.context

class ProjectContext : Context() {
	
	override val parent = RootContext
	
	lateinit var name: String
	
	lateinit var modules: List<ModuleContext>
}