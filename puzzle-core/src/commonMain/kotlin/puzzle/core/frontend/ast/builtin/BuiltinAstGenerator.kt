package puzzle.core.frontend.ast.builtin

import puzzle.core.frontend.ast.builtin.generator.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.semantics.PzlSymbolBuilder

object BuiltinAstGenerator {
	
	fun generate(): ProjectContext {
		return ProjectContext().apply {
			this.name = "puzzle-builtin"
			this.builtin = true
			this.modules = listOf(
				generateBuiltinCoreModule()
			)
		}
	}
	
	context(project: ProjectContext)
	private fun generateBuiltinCoreModule(): ModuleContext {
		val nodes = listOf(
			generateAnyAst(),
			generateComparableAst(),
			generateNumberAst(),
			generateCharAst(),
			generateBooleanAst(),
			generateStringAst()
		)
		return ModuleContext().apply {
			val module = this
			this.name = "puzzle-core"
			this.builtin = true
			this.parent = project
			this.files = nodes.map { node ->
				FileContext().apply {
					this.builtin = true
					this.parent = module
					this.node = node
					this.symbol = PzlSymbolBuilder.buildFileSymbol()
				}
			}
		}
	}
}