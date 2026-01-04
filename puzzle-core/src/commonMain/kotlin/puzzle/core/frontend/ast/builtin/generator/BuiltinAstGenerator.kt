package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.model.AstModule
import kotlin.time.measureTimedValue

object BuiltinAstGenerator {
	
	fun generate(): AstModule {
		println("Puzzle 内置 AST 生成中...")
		val value = measureTimedValue {
			AstModule(
				name = "puzzle-builtin-ast",
				nodes = listOf(
					generateBuiltinAnyAst(),
					generateComparableAst(),
					generateBuiltinNumberAst(),
					generateBuiltinBooleanAst(),
				)
			)
		}
		println("生成完成，用时: ${value.duration}")
		return value.value
	}
}