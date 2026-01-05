package puzzle.core.frontend.ast.builtin

import puzzle.core.frontend.ast.builtin.generator.*
import puzzle.core.frontend.model.AstModule
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue

object BuiltinAstGenerator {
	
	fun generate(): AstModule {
		val value = measureTimedValue {
			AstModule(
				name = "puzzle-builtin-ast",
				nodes = listOf(
					generateBuiltinAnyAst(),
					generateComparableAst(),
					generateBuiltinNumberAst(),
					generateBuiltinCharAst(),
					generateBuiltinBooleanAst(),
				)
			)
		}
		println("内建类型生成完成，用时: ${value.duration.toString(DurationUnit.MILLISECONDS, decimals = 3)}")
		return value.value
	}
}