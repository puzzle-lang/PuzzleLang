package puzzle.core.frontend.ast.builtin

import puzzle.core.frontend.ast.builtin.generator.*
import puzzle.core.frontend.model.AstModule
import puzzle.core.frontend.model.AstProject
import puzzle.core.util.format
import kotlin.time.measureTimedValue

object BuiltinAstGenerator {
	
	fun generate(): AstProject {
		val value = measureTimedValue {
			val puzzleBuiltinCore = AstModule(
				name = "puzzle-builtin-core",
				path = null,
				builtin = true,
				files = listOf(
					generateAnyAst(),
					generateComparableAst(),
					generateNumberAst(),
					generateCharAst(),
					generateBooleanAst(),
					generateStringAst()
				)
			)
			AstProject(
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