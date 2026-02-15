package puzzle.builtin

import puzzle.builtin.generator.*
import puzzle.core.context.ModuleContext
import puzzle.core.context.ProjectContext

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
            this.group = "puzzle.core".split(".")
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