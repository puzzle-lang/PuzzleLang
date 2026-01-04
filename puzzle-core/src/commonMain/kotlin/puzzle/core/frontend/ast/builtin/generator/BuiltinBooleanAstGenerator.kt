package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.buildBuiltinAst

fun generateBuiltinBooleanAst(): AstFile {
	return buildBuiltinAst("Boolean.pzl") {
		appendStruct("Boolean") {
			appendFun("!", returnType = "Boolean")
			appendFun("&", listOf(parameter("other", "Boolean")), "Boolean")
			appendFun("|", listOf(parameter("other", "Boolean")), "Boolean")
			appendFun("^", listOf(parameter("other", "Boolean")), "Boolean")
			appendFun("toString", returnType = "String")
			appendFun("==", listOf(parameter("other", "Any", isNullable = true)), "Boolean")
			appendFun("hash", returnType = "Int")
		}
	}
}