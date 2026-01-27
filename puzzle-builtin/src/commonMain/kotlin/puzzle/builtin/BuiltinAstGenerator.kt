package puzzle.builtin

import puzzle.core.context.ModuleContext
import puzzle.core.context.ProjectContext
import puzzle.builtin.generator.*

object BuiltinAstGenerator {
	
	fun generate(): ProjectContext {
		return ProjectContext().apply {
			this.name = "puzzle-builtin"
			this.modules = listOf(
				generateBuiltinCoreModule()
			)
		}
	}
	
	context(project: ProjectContext)
	private fun generateBuiltinCoreModule(): ModuleContext {
		return ModuleContext().apply {
			this.name = "puzzle-core"
			this.parent = project
			this.files = listOf(
				generateAnyAst(),
				generateComparableAst(),
				generateNumberAst(),
				generateCharAst(),
				generateBooleanAst(),
				generateStringAst()
			)
		}
	}
}