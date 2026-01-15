package puzzle.core.frontend.ast.builtin

import puzzle.core.frontend.ast.builtin.generator.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.semantics.PzlSymbolBuilder

object BuiltinAstGenerator {
	
	fun generate(): ProjectContext {
		val nodes = listOf(
			generateAnyAst(),
			generateComparableAst(),
			generateNumberAst(),
			generateCharAst(),
			generateBooleanAst(),
			generateStringAst()
		)
		val puzzleBuiltinCore = ModuleContext(
			name = "puzzle-builtin-core",
			path = null,
			builtin = true,
			files = nodes.map { node ->
				FileContext(true).apply {
					this.node = node
					this.symbol = PzlSymbolBuilder.buildFileSymbol()
				}
			}
		)
		return ProjectContext(
			name = "puzzle-builtin",
			path = null,
			builtin = true,
			modules = listOf(puzzleBuiltinCore)
		)
	}
}