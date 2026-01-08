package puzzle.core.frontend.ast.builtin

import puzzle.core.frontend.ast.builtin.generator.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.util.format
import kotlin.time.measureTimedValue

object BuiltinAstGenerator {
	
	fun generate(): ProjectContext {
		val value = measureTimedValue {
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
					FileContext().also {
						it.node = node
					}
				}
			)
			ProjectContext(
				name = "puzzle-builtin",
				path = null,
				builtin = true,
				modules = listOf(puzzleBuiltinCore)
			)
		}
		println("内建类型生成完成, 用时: ${value.duration.format()}")
		return value.value
	}
}