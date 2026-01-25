package puzzle.context

object RootContext : Context() {
	
	override val parent: Context
		get() = error("RootContext 没有 parent")
	
	lateinit var projects: List<ProjectContext>
	
	var maxPathLength = 0
}